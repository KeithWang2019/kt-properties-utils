package com.cangsg.brick.kt;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cangsg.brick.kt.base.KtBeanUtils;
import com.cangsg.brick.kt.entry.KtCommonValidateObj;
import com.cangsg.brick.kt.entry.KtDepthTreeNode;
import com.cangsg.brick.kt.entry.KtEntryImpl;
import com.cangsg.brick.kt.entry.KtEntryList;
import com.cangsg.brick.kt.entry.KtValidateHandler;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public class KtValidateUtils {
	private static ThreadLocal<Map<String, KtValidator<? extends Annotation, ?>>> threadLocalHandlerMap = new ThreadLocal<>();

	private KtValidateUtils() {
		throw new IllegalStateException("KtValidateUtils class");
	}

	/**
	 * 检查对象是否满足条件
	 * 
	 * @param <T> 数据类型
	 * @param obj 数据对象
	 * @return KtValidateResult检查结果，不为NULL
	 */
	public static <T, V> KtValidateResult<V> valid(T obj) {
		try {
			threadLocalHandlerMap.set(new HashMap<>());
			return valid(obj, 0, new KtDepthTreeNode(null, null));
		} finally {
			threadLocalHandlerMap.remove();
		}
	}

	public static <T, V> KtValidateResult<V> valid(List<T> list) {
		KtCommonValidateObj<T> obj = new KtCommonValidateObj<>();
		obj.setList(list);
		return valid(obj);
	}

	/**
	 * 先序变量对象，进行验证
	 * 
	 * @param <T> 被验证对象类型
	 * @param obj 被验证对象
	 * @return 错误信息
	 * @throws Exception 异常信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T, V> KtValidateResult<V> valid(T obj, int depth, KtDepthTreeNode currentDepthTreeNode) {
		if (depth > 20) {
			throw new RuntimeException("避免引用循环，最大支持20层的对象");
		}

		KtValidateResult result = null;
		KtEntryList needLaterHandleList = new KtEntryList();
		currentDepthTreeNode.setObj(obj);

		// Field[] fields = obj.getClass().getDeclaredFields();
		if (obj == null) {
			return null;
		}
		List<Field> fields = getAllFields(obj.getClass());

		for (int i = 0; i < fields.size(); i++) {
			Field currentField = fields.get(i);
			String fieldTypeName = currentField.getType().getTypeName();

			Method valMethod = getGetterMethodAndCache(obj.getClass(), currentField.getName());
			if (valMethod == null) {
				continue;
			}

			Object val = getValue(valMethod, obj);

			if (val == null || simplePrimitive(fieldTypeName)) {
				// 简单基础类型直接处理
				result = unfoldValid(currentField, obj, valMethod, depth, currentDepthTreeNode);
				if (result != null && result.hasError()) {
					return result;
				}
			} else {
				if (fieldTypeName.lastIndexOf("[]") > 0) {
					// 复杂数组 先处理数组校验
					result = unfoldValid(currentField, obj, valMethod, depth, currentDepthTreeNode);
					if (result != null && result.hasError()) {
						return result;
					}
					// 数组校验通过，拉平数据，并滞后处理数组中的对象
					if (val != null) {
						int arrayLength = Array.getLength(val);
						KtEntryImpl<KtEntryList> needLaterEntry = new KtEntryImpl(currentField.getName(),
								new KtEntryList());
						for (int j = 0; j < arrayLength; j++) {
							needLaterEntry.getValue().add(new KtEntryImpl(j, Array.get(val, j)));
						}
						needLaterHandleList.add(needLaterEntry);
					}
				} else if (val instanceof Map) {
					Map mapVal = (Map) val;
					// 复杂MAP 先处理MAP校验
					result = unfoldValid(currentField, obj, valMethod, depth, currentDepthTreeNode);
					if (result != null && result.hasError()) {
						return result;
					}
					// MAP校验通过，拉平数据，并滞后处理MAP中的对象
					KtEntryImpl<KtEntryList> needLaterEntry = new KtEntryImpl(currentField.getName(),
							new KtEntryList());
					mapVal.forEach((key, value) -> {
						needLaterEntry.getValue().add(new KtEntryImpl(key, value));
					});
					needLaterHandleList.add(needLaterEntry);
				} else if (val instanceof List) {
					List listVal = (List) val;
					// 复杂List 先处理List校验
					result = unfoldValid(currentField, obj, valMethod, depth, currentDepthTreeNode);
					if (result != null && result.hasError()) {
						return result;
					}
					// List校验通过，拉平数据，并滞后处理List中的对象
					KtEntryImpl<KtEntryList> needLaterEntry = new KtEntryImpl(currentField.getName(),
							new KtEntryList());
					for (int j = 0; j < listVal.size(); j++) {
						needLaterEntry.getValue().add(new KtEntryImpl(j, listVal.get(j)));
					}
					needLaterHandleList.add(needLaterEntry);
				} else {
					needLaterHandleList.add(new KtEntryImpl(currentField.getName(), val));
				}
			}
		}
		for (int i = 0; i < needLaterHandleList.size(); i++) {
			KtEntryImpl entry = needLaterHandleList.get(i);

			if (entry.getValue() instanceof KtEntryList) {
				KtDepthTreeNode arrayDepthTreeNode = new KtDepthTreeNode(currentDepthTreeNode,
						entry.getKey().toString());
				currentDepthTreeNode.addChild(arrayDepthTreeNode);

				KtEntryList list = (KtEntryList) entry.getValue();

				for (int j = 0; j < list.size(); j++) {
					KtEntryImpl needLaterHandleItem = list.get(j);
					KtDepthTreeNode childDepthTreeNode = new KtDepthTreeNode(arrayDepthTreeNode,
							needLaterHandleItem.getKey());
					arrayDepthTreeNode.addChild(childDepthTreeNode);
					result = valid(needLaterHandleItem.getValue(), depth + 2, childDepthTreeNode);
					if (result != null && result.hasError()) {
						return result;
					}
				}
			} else {
				KtDepthTreeNode childDepthTreeNode = new KtDepthTreeNode(currentDepthTreeNode, entry.getKey());
				currentDepthTreeNode.addChild(childDepthTreeNode);
				result = valid(entry.getValue(), depth + 1, childDepthTreeNode);
				if (result != null && result.hasError()) {
					return result;
				}
			}

		}

		return new KtValidateResult(null, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static KtValidateResult unfoldValid(Field currentField, Object obj, Method valMethod, int depth,
			KtDepthTreeNode depthTreeNode) {
		Annotation[] annotations = currentField.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(KtValidateHandler.class)) {
				KtValidateHandler validateAnnotation = annotation.annotationType()
						.getDeclaredAnnotation(KtValidateHandler.class);
				Class<? extends KtValidator> validatorClass = validateAnnotation.handleBy();
				if (validatorClass != null) {
					KtValidator validator;
					try {
						String validatorClassName = validatorClass.getName();
						validator = threadLocalHandlerMap.get().get(validatorClassName);
						if (validator == null) {
							validator = (KtValidator) KtBeanUtils.instantiateClass(validatorClass);
							threadLocalHandlerMap.get().put(validatorClassName, validator);
						}
						validator.discover(currentField.getName(), depth, depthTreeNode);
						if (validator.init(annotation)) {
							KtValidateResult result = validator.call(getValue(valMethod, obj),
									annotation);
							if (result != null && result.hasError()) {
								return result;
							}
						}
					} catch (ClassCastException e) {
						throw new RuntimeException("传入处理程序数据类型不符");
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					}
				}
			}
		}
		return null;
	}

	private static boolean simplePrimitive(String fieldTypeName) {
		switch (fieldTypeName) {
			case "int":
			case "float":
			case "long":
			case "double":
			case "java.lang.Character":
			case "java.lang.Character[]":
			case "char[]":
			case "java.lang.Integer":
			case "java.lang.Integer[]":
			case "int[]":
			case "java.lang.Byte":
			case "java.lang.Byte[]":
			case "byte[]":
			case "java.lang.Short":
			case "java.lang.Short[]":
			case "short[]":
			case "java.lang.Long":
			case "java.lang.Long[]":
			case "long[]":
			case "java.lang.Float":
			case "java.lang.Float[]":
			case "float[]":
			case "java.lang.Double":
			case "java.lang.Double[]":
			case "double[]":
			case "java.lang.Boolean":
			case "java.lang.Boolean[]":
			case "boolean[]":
			case "java.lang.String":
			case "java.lang.String[]":
			case "java.util.Date":
			case "java.util.Calendar":
			case "java.time.LocalDate":
			case "java.time.LocalDateTime":
				return true;
			default:
				return false;
		}
	}

	private static List<Field> getAllFields(Class<?> clazz) {
		List<Field> allFields = new ArrayList<>();
		traverseAllFields(clazz, allFields);
		return allFields;
	}

	private static void traverseAllFields(Class<?> clazz, List<Field> allFields) {
		Field[] currentFields = clazz.getDeclaredFields();
		for (int i = 0; i < currentFields.length; i++) {
			allFields.add(currentFields[i]);
		}
		if (clazz.getSuperclass() != Object.class && clazz.getSuperclass() != null) {
			traverseAllFields(clazz.getSuperclass(), allFields);
		}
	}

	private static Method getGetterMethodAndCache(Class<?> clazz, String fieldName) {
		PropertyDescriptor propertyDescriptor = KtBeanUtils.getPropertyDescriptor(clazz, fieldName);
		if (propertyDescriptor != null) {
			return propertyDescriptor.getReadMethod();
		} else {
			return null;
		}
	}

	private static Object getValue(Method methodGetter, Object obj) {
		try {
			return methodGetter.invoke(obj);
		} catch (Exception e) {
			throw new RuntimeException("通过Getter获取数据异常");
		}
	}
}

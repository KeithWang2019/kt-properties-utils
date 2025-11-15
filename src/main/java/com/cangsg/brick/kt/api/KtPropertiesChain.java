package com.cangsg.brick.kt.api;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KtPropertiesChain<T, V> {
    private KtMapValueHandler mapValueHandler;
    private T source;
    private V target;
    private int depth;

    private Class<?> actualEditable;
    private PropertyDescriptor[] targetPds;

    private Map<String, Class<?>> mapTypes = new HashMap<>();

    public KtPropertiesChain(T source, V target) {
        this(source, target, 0);
    }

    public KtPropertiesChain(T source, V target, int depth) {
        this(source, target, depth, new HashMap<>());
    }

    protected KtPropertiesChain(T source, V target, int depth, Map<String, Class<?>> mapTypes) {
        this.source = source;
        this.target = target;
        this.depth = depth;

        this.actualEditable = target.getClass();
        this.targetPds = BeanUtils.getPropertyDescriptors(actualEditable);

        this.mapTypes = mapTypes;
    }

    public KtPropertiesChain<T, V> mapValue(KtMapValueHandler valueHandler) {
        this.mapValueHandler = valueHandler;
        return this;
    }

    public KtPropertiesChain<T, V> mapType(Class<?> sourceType, Class<?> targetType) {
        this.mapTypes.put(sourceType.getName(), targetType);
        return this;
    }

    public V build() {
        if (this.source == null) {
            return null;
        }
        if (this.source == this.target) {
            return (V) this.target;
        }
        if (depth > 20) {
            throw new RuntimeException("避免引用循环，最大支持20层的对象");
        }

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        try {
                            this.setSourceAccessible(readMethod);
                            this.setTargetAccessible(writeMethod);

                            Object sourceValue = readMethod.invoke(this.source);

                            String sourceType = readMethod.getReturnType().getName();
                            String destType = writeMethod.getParameterTypes()[0].getName();

                            if (isPrimitiveType(destType)) {
                                // 基本类型赋值
                                if (primitiveWithObjectTypeEquals(sourceType, destType)) {
                                    writeMethod.invoke(target, sourceValue);
                                } else {
                                    Object mapValue = handleMapValue(destType, sourcePd.getName(), sourceValue);
                                    if (mapValue != null) {
                                        writeMethod.invoke(target, mapValue);
                                    }
                                }
                            } else if (isPrimitiveObjectType(destType)) {
                                // 基本对象赋值
                                if (primitiveWithObjectTypeEquals(sourceType, destType)) {
                                    Object targetItem = clonePrimitiveObjectType(destType, sourceValue);
                                    writeMethod.invoke(target, targetItem);
                                } else {
                                    Object mapValue = handleMapValue(destType, sourcePd.getName(), sourceValue);
                                    if (mapValue != null) {
                                        writeMethod.invoke(target, mapValue);
                                    }
                                }
                            } else if (isTypeMap(destType)) {
                                // Map赋值
                                this.handleMap(writeMethod, (Map<Object, Object>) sourceValue);
                            } else if (isArray(destType)) {
                                this.handleArray(writeMethod, sourceValue, destType);
                            } else if (isCollection(destType)) {
                                this.handleCollection(writeMethod, sourceValue);
                            } else {
                                // 通用对象
                                Object mapValue = handleMapValue(destType, sourcePd.getName(), sourceValue);
                                if (mapValue != null) {
                                    writeMethod.invoke(target, mapValue);
                                } else {
                                    Object targetValue = targetPd.getReadMethod().invoke(this.target);
                                    this.handleObject(writeMethod, sourceValue, targetValue);
                                }
                            }
                        } catch (Throwable ex) {
                            throw new RuntimeException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }

        return this.target;
    }

    private Object handleMapValue(String destType, String name, Object sourceValue)
            throws IllegalAccessException, InvocationTargetException {
        if (this.mapValueHandler != null) {
            return this.mapValueHandler.handle(destType, name, sourceValue);
        }
        return null;
    }

    private boolean primitiveWithObjectTypeEquals(String sourceType, String distType) {
        if (sourceType.equals(distType)) {
            return true;
        }

        int sourceIndex = -1;
        if (sourceType.contains(".")) {
            sourceIndex = primitiveObjectTypes.indexOf(sourceType);
        } else {
            sourceIndex = primitiveTypes.indexOf(sourceType);
        }

        int distIndex = -1;
        if (distType.contains(".")) {
            distIndex = primitiveObjectTypes.indexOf(distType);
        } else {
            distIndex = primitiveTypes.indexOf(distType);
        }

        if (sourceIndex == distIndex) {
            return true;
        }
        return false;
    }

    private static boolean isCollection(String fullType) {
        return "java.util.List".equals(fullType);
    }

    private boolean isArray(String fullType) {
        return fullType != null && fullType.startsWith("[");
    }

    @SuppressWarnings("rawtypes")
    private void handleObject(Method writeMethod, Object sourceValue, Object targetValue)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, IllegalArgumentException,
            NoSuchMethodException, SecurityException {
        // 通用对象
        Type genericType = writeMethod.getGenericParameterTypes()[0];
        Class<?> targetClass = null;

        if (genericType instanceof Class) {
            targetClass = (Class<?>) genericType;
        } else if (genericType instanceof TypeVariable) {
            if (sourceValue != null && this.mapTypes.containsKey(sourceValue.getClass().getName())) {
                targetClass = this.mapTypes.get(sourceValue.getClass().getName());
            }
        }

        if (targetClass != null) {
            if (targetClass.isEnum()) {
                if (sourceValue == null) {
                    // writeMethod.invoke(target, null);
                } else {
                    Class<Enum> targetEnumClass = (Class<Enum>) targetClass;
                    Enum targetEnum = Enum.valueOf(targetEnumClass, ((Enum) sourceValue).name());
                    writeMethod.invoke(target, targetEnum);
                }
            } else {
                Object targetItem = null;
                if (targetValue != null) {
                    targetItem = targetValue;
                } else {
                    targetItem = targetClass.getDeclaredConstructor().newInstance();
                }
                if (sourceValue != null) {
                    targetItem = (new KtPropertiesChain<>(sourceValue, targetItem,
                            this.depth + 1, this.mapTypes).mapValue(this.mapValueHandler)).build();
                    writeMethod.invoke(target, targetItem);
                } else {
                    writeMethod.invoke(target, sourceValue);
                }
            }
        }
    }

    private void handleMap(Method writeMethod, Map<Object, Object> sourceMap) {
        try {
            Map<Object, Object> targetMap = new HashMap<>();
            ParameterizedType genericType = (ParameterizedType) writeMethod.getGenericParameterTypes()[0];
            Type actualGenericType = genericType.getActualTypeArguments()[1];
            Class<?> targetClass = null;
            if (actualGenericType instanceof Class) {
                targetClass = (Class<?>) actualGenericType;
            }

            for (Entry<Object, Object> entry : sourceMap.entrySet()) {
                Object sourceValue = entry.getValue();
                String sourceType = sourceValue.getClass().getName();

                if (sourceValue != null) {
                    if (this.isPrimitiveType(sourceType)) {
                        targetMap.put(entry.getKey(), sourceValue);
                    } else if (this.isPrimitiveObjectType(sourceType)) {
                        Object targetItem = clonePrimitiveObjectType(sourceType, sourceValue);
                        targetMap.put(entry.getKey(), targetItem);
                    } else {
                        if (targetClass == null && this.mapTypes.containsKey(sourceValue.getClass().getName())) {
                            targetClass = this.mapTypes.get(sourceValue.getClass().getName());
                        }
                        if (targetClass != null) {
                            Object targetValue = targetClass.getDeclaredConstructor().newInstance();
                            targetValue = (new KtPropertiesChain<>(sourceValue, targetValue, this.depth + 1,
                                    this.mapTypes)
                                    .mapValue(this.mapValueHandler)).build();

                            targetMap.put(entry.getKey(), targetValue);
                        }
                    }
                }
            }

            writeMethod.invoke(target, targetMap);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | SecurityException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void handleArray(Method writeMethod, Object sourceValue, String destType)
            throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException,
            IllegalArgumentException, NoSuchMethodException, SecurityException {
        if (isPrimitiveArrayType(destType)) {
            Object[] targetArray = new Object[] { null };

            switch (destType) {
                case "[B":
                    byte[] sourceByteArray = (byte[]) sourceValue;
                    if (sourceByteArray != null) {
                        byte[] targetObjectArray = new byte[sourceByteArray.length];
                        for (int i = 0; i < sourceByteArray.length; i++) {
                            targetObjectArray[i] = sourceByteArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[S":
                    short[] sourceShortArray = (short[]) sourceValue;
                    if (sourceShortArray != null) {
                        short[] targetObjectArray = new short[sourceShortArray.length];
                        for (int i = 0; i < sourceShortArray.length; i++) {
                            targetObjectArray[i] = sourceShortArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[I":
                    int[] sourceIntArray = (int[]) sourceValue;
                    if (sourceIntArray != null) {
                        int[] targetObjectArray = new int[sourceIntArray.length];
                        for (int i = 0; i < sourceIntArray.length; i++) {
                            targetObjectArray[i] = sourceIntArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[J":
                    long[] sourceLongArray = (long[]) sourceValue;
                    if (sourceLongArray != null) {
                        long[] targetObjectArray = new long[sourceLongArray.length];
                        for (int i = 0; i < sourceLongArray.length; i++) {
                            targetObjectArray[i] = sourceLongArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[F":
                    float[] sourceFloatArray = (float[]) sourceValue;
                    if (sourceFloatArray != null) {
                        float[] targetObjectArray = new float[sourceFloatArray.length];
                        for (int i = 0; i < sourceFloatArray.length; i++) {
                            targetObjectArray[i] = sourceFloatArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[D":
                    double[] sourceDoubleArray = (double[]) sourceValue;
                    if (sourceDoubleArray != null) {
                        double[] targetObjectArray = new double[sourceDoubleArray.length];
                        for (int i = 0; i < sourceDoubleArray.length; i++) {
                            targetObjectArray[i] = sourceDoubleArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Z":
                    boolean[] sourceBooleanArray = (boolean[]) sourceValue;
                    if (sourceBooleanArray != null) {
                        boolean[] targetObjectArray = new boolean[sourceBooleanArray.length];
                        for (int i = 0; i < sourceBooleanArray.length; i++) {
                            targetObjectArray[i] = sourceBooleanArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.lang.Boolean;":
                    Boolean[] sourceBBooleanArray = (Boolean[]) sourceValue;
                    if (sourceBBooleanArray != null) {
                        Boolean[] targetObjectArray = new Boolean[sourceBBooleanArray.length];
                        for (int i = 0; i < sourceBBooleanArray.length; i++) {
                            targetObjectArray[i] = sourceBBooleanArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.math.BigDecimal;":
                    BigDecimal[] sourceBigDecimalArray = (BigDecimal[]) sourceValue;
                    if (sourceBigDecimalArray != null) {
                        BigDecimal[] targetObjectArray = new BigDecimal[sourceBigDecimalArray.length];
                        for (int i = 0; i < sourceBigDecimalArray.length; i++) {
                            targetObjectArray[i] = sourceBigDecimalArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.lang.Double;":
                    Double[] sourceDDoubleArray = (Double[]) sourceValue;
                    if (sourceDDoubleArray != null) {
                        Double[] targetObjectArray = new Double[sourceDDoubleArray.length];
                        for (int i = 0; i < sourceDDoubleArray.length; i++) {
                            targetObjectArray[i] = sourceDDoubleArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.util.Date;":
                    Date[] sourceDateArray = (Date[]) sourceValue;
                    if (sourceDateArray != null) {
                        Date[] targetObjectArray = new Date[sourceDateArray.length];
                        for (int i = 0; i < sourceDateArray.length; i++) {
                            targetObjectArray[i] = sourceDateArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.lang.Float;":
                    Float[] sourceFFloatArray = (Float[]) sourceValue;
                    if (sourceFFloatArray != null) {
                        Float[] targetObjectArray = new Float[sourceFFloatArray.length];
                        for (int i = 0; i < sourceFFloatArray.length; i++) {
                            targetObjectArray[i] = sourceFFloatArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.lang.Integer;":
                    Integer[] sourceIIntegerArray = (Integer[]) sourceValue;
                    if (sourceIIntegerArray != null) {
                        Integer[] targetObjectArray = new Integer[sourceIIntegerArray.length];
                        for (int i = 0; i < sourceIIntegerArray.length; i++) {
                            targetObjectArray[i] = sourceIIntegerArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.lang.String;":
                    String[] sourceStringArray = (String[]) sourceValue;
                    if (sourceStringArray != null) {
                        String[] targetObjectArray = new String[sourceStringArray.length];
                        for (int i = 0; i < sourceStringArray.length; i++) {
                            targetObjectArray[i] = sourceStringArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.lang.Byte;":
                    Byte[] sourceBByteArray = (Byte[]) sourceValue;
                    if (sourceBByteArray != null) {
                        Byte[] targetObjectArray = new Byte[sourceBByteArray.length];
                        for (int i = 0; i < sourceBByteArray.length; i++) {
                            targetObjectArray[i] = sourceBByteArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.lang.Long;":
                    Long[] sourceLLongArray = (Long[]) sourceValue;
                    if (sourceLLongArray != null) {
                        Long[] targetObjectArray = new Long[sourceLLongArray.length];
                        for (int i = 0; i < sourceLLongArray.length; i++) {
                            targetObjectArray[i] = sourceLLongArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.time.LocalDate;":
                    LocalDate[] sourceLocalDateArray = (LocalDate[]) sourceValue;
                    if (sourceLocalDateArray != null) {
                        LocalDate[] targetObjectArray = new LocalDate[sourceLocalDateArray.length];
                        for (int i = 0; i < sourceLocalDateArray.length; i++) {
                            targetObjectArray[i] = sourceLocalDateArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                case "[Ljava.time.LocalDateTime;":
                    LocalDateTime[] sourceLocalDateTimeArray = (LocalDateTime[]) sourceValue;
                    if (sourceLocalDateTimeArray != null) {
                        LocalDateTime[] targetObjectArray = new LocalDateTime[sourceLocalDateTimeArray.length];
                        for (int i = 0; i < sourceLocalDateTimeArray.length; i++) {
                            targetObjectArray[i] = sourceLocalDateTimeArray[i];
                        }
                        targetArray = new Object[] { targetObjectArray };
                    }
                    break;
                default:
                    Object[] sourceArray = (Object[]) sourceValue;
                    String targetArrayItemClassType = destType.substring(2, destType.length() - 1);
                    Class<?> targetArrayItemClass = null;
                    if (targetArrayItemClassType.indexOf(".") > 0) {
                        targetArrayItemClass = Class.forName(targetArrayItemClassType);
                    }
                    if (targetArrayItemClass != null) {
                        Object tempTargetArray = Array.newInstance(targetArrayItemClass,
                                sourceArray.length);
                        for (int i = 0; i < sourceArray.length; i++) {
                            Object sourceItem = sourceArray[i];
                            if (sourceItem != null) {
                                Object targetItem = null;
                                if (isPrimitiveObjectType(targetArrayItemClassType)) {
                                    targetItem = clonePrimitiveObjectType(targetArrayItemClassType,
                                            sourceItem);
                                } else {
                                    targetItem = targetArrayItemClass.getDeclaredConstructor().newInstance();
                                    targetItem = (new KtPropertiesChain<>(sourceItem, targetItem, this.depth + 1,
                                            this.mapTypes)
                                            .mapValue(this.mapValueHandler)).build();
                                }
                                Array.set(tempTargetArray, i, targetItem);
                            } else {
                                Array.set(tempTargetArray, i, null);
                            }
                        }
                        targetArray = new Object[] { tempTargetArray };
                    }
                    break;
            }
            writeMethod.invoke(target, targetArray);
        }
    }

    private void handleCollection(Method writeMethod, Object sourceValue)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, IllegalArgumentException,
            NoSuchMethodException, SecurityException {
        // 集合
        List<Object> sourceList = (List<Object>) sourceValue;
        if (sourceList == null) {
            return;
        }

        ParameterizedType genericType = (ParameterizedType) writeMethod.getGenericParameterTypes()[0];
        Type actualGenericType = genericType.getActualTypeArguments()[0];
        Class<?> targetClass = null;
        if (actualGenericType instanceof Class) {
            targetClass = (Class<?>) actualGenericType;
        }
        if (targetClass == null && !sourceList.isEmpty()) {
            String sourceType = sourceList.get(0).getClass().getName();
            if (this.mapTypes.containsKey(sourceType)) {
                targetClass = this.mapTypes.get(sourceType);
            }
        }
        if (targetClass != null) {
            List<Object> targetList = new ArrayList<>();

            for (int i = 0; i < sourceList.size(); i++) {
                Object sourceItem = sourceList.get(i);
                Object targetItem = null;
                if (sourceItem != null) {
                    if (this.isPrimitiveType(targetClass.getName())) {
                        targetItem = sourceItem;
                    } else if (this.isPrimitiveObjectType(targetClass.getName())) {
                        targetItem = clonePrimitiveObjectType(targetClass.getName(), sourceItem);
                    } else {
                        targetItem = targetClass.getDeclaredConstructor().newInstance();
                        targetItem = (new KtPropertiesChain<>(sourceItem, targetItem, this.depth + 1,
                                this.mapTypes)
                                .mapValue(this.mapValueHandler)).build();
                    }
                    targetList.add(targetItem);
                } else {
                    targetList.add(null);
                }

            }
            writeMethod.invoke(target, targetList);
        }

    }

    private void setSourceAccessible(Method readMethod) {
        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
            readMethod.setAccessible(true);
        }
    }

    private void setTargetAccessible(Method writeMethod) {
        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
            writeMethod.setAccessible(true);
        }
    }

    private static List<String> primitiveTypes = new ArrayList<>(
            Arrays.asList("byte", "short", "int", "long", "float", "double", "boolean"));

    private boolean isPrimitiveType(String type) {
        return primitiveTypes.contains(type);
    }

    private static List<String> primitiveObjectTypes = new ArrayList<>(
            Arrays.asList("java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float",
                    "java.lang.Double", "java.lang.Boolean", "java.util.Date", "java.lang.String",
                    "java.math.BigDecimal", "java.time.LocalDate", "java.time.LocalDateTime"));

    private boolean isPrimitiveObjectType(String type) {
        return primitiveObjectTypes.contains(type);
    }

    private static List<String> primitiveArrayTypes = new ArrayList<>(
            Arrays.asList("[B", "[S", "[I", "[J", "[F", "[D", "[Z", "[L"));

    private static boolean isPrimitiveArrayType(String type) {
        return primitiveArrayTypes.contains(type.substring(0, 2));
    }

    private static Object clonePrimitiveObjectType(String typeName, Object sourceObject) {
        if (sourceObject == null) {
            return null;
        }
        switch (typeName) {
            case "java.lang.Byte":
                Byte sourceByte = (Byte) sourceObject;
                return sourceByte.byteValue();
            case "java.lang.Short":
                Short sourceShort = (Short) sourceObject;
                return sourceShort.shortValue();
            case "java.lang.Integer":
                Integer sourceInteger = (Integer) sourceObject;
                return sourceInteger.intValue();
            case "java.lang.Long":
                Long sourceLong = (Long) sourceObject;
                return sourceLong.longValue();
            case "java.lang.Float":
                Float sourceFloat = (Float) sourceObject;
                return sourceFloat.floatValue();
            case "java.lang.Double":
                Double sourceDouble = (Double) sourceObject;
                return sourceDouble.doubleValue();
            case "java.lang.Boolean":
                Boolean sourceBoolean = (Boolean) sourceObject;
                return sourceBoolean.booleanValue();
            case "java.util.Date":
                Date sourceDate = (Date) sourceObject;
                return new Date(sourceDate.getTime());
            case "java.lang.String":
                return (String) sourceObject;
            case "java.math.BigDecimal":
                return (BigDecimal) sourceObject;
            case "java.time.LocalDateTime":
                return (LocalDateTime) sourceObject;
            case "java.time.LocalDate":
                return (LocalDate) sourceObject;
            default:
                return null;
        }
    }

    private static Pattern MAP_PATTERN = Pattern.compile("^java\\.util\\..*Map.*(<.*>)*$");

    private static boolean isTypeMap(String fullType) {
        String type = substringBefore(fullType, "<");
        Matcher matcher = MAP_PATTERN.matcher(type);
        return matcher.matches();
    }

    private static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    private static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.isEmpty()) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }
}

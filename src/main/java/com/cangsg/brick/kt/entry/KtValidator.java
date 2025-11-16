package com.cangsg.brick.kt.entry;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class KtValidator<A extends Annotation, T> {
    protected String fieldName;
    protected int depath;
    protected KtDepthTreeNode depthTreeNode;

    public boolean init(A annotation) {
        return true;
    }

    public abstract KtValidateResult<?> call(T val, A annotation) throws RuntimeException;

    public void discover(String fieldName, int depth, KtDepthTreeNode depthTreeNode) {
        this.fieldName = fieldName;
        this.depath = depth;
        this.depthTreeNode = depthTreeNode;
    }

    @SuppressWarnings("unchecked")
    public final <U> U getPreconditionValue(int distance, String name) {
        KtDepthTreeNode tempDepthTreeNode = depthTreeNode;
        for (int i = 0; i < distance; i++) {
            tempDepthTreeNode = tempDepthTreeNode.getParent();
        }
        Object preconditionObject = tempDepthTreeNode.getObj();

        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(name, preconditionObject.getClass());
            Method method = pd.getReadMethod();
            return (U) method.invoke(preconditionObject);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取前置条件错误");
        }
    }

    public List<Object> getFieldPath() {
        List<Object> pathList = new ArrayList<>();
        pathList.add(fieldName);
        KtDepthTreeNode tempDepthTreeNode = depthTreeNode;
        do {
            if (tempDepthTreeNode.getFieldPath() != null) {
                pathList.add(tempDepthTreeNode.getFieldPath());
            }
            tempDepthTreeNode = tempDepthTreeNode.getParent();
        } while (tempDepthTreeNode != null);
        Collections.reverse(pathList);
        return pathList;
    }

    public final void setOneself(T val) {
        Object preconditionObject = depthTreeNode.getObj();
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, preconditionObject.getClass());
            Method method = pd.getWriteMethod();
            method.invoke(preconditionObject, val);
        } catch (Exception e) {
            throw new RuntimeException("更改自身值错误");
        }
    }
}

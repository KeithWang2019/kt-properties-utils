package com.cangsg.brick.kt.entry;

import java.util.ArrayList;
import java.util.List;

public final class KtDepthTreeNode {
    private Object obj;
    private KtDepthTreeNode parent;
    private Object fieldPath;
    private List<KtDepthTreeNode> childs = new ArrayList<>();

    public KtDepthTreeNode(KtDepthTreeNode parent, Object fieldPath) {
        this.parent = parent;
        this.fieldPath = fieldPath;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public KtDepthTreeNode getParent() {
        return parent;
    }

    public void setParent(KtDepthTreeNode parent) {
        this.parent = parent;
    }

    public List<KtDepthTreeNode> getChilds() {
        return childs;
    }

    public void addChild(KtDepthTreeNode node) {
        childs.add(node);
    }

    public Object getFieldPath() {
        return fieldPath;
    }

    public void setFieldName(Object fieldPath) {
        this.fieldPath = fieldPath;
    }
}

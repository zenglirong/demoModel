package com.iflytek.dynamicSpeed.utils;

import androidx.annotation.NonNull;

public class TestFrameworkClassModel {
    private String itemName;
    private boolean isBase;
    private boolean isStability;
    private boolean isFunction;
    private boolean isCheck;
    private Class<?> isBaseClass;
    private Class<?> isFunctionClass;
    private Class<?> isStabilityClass;

    public TestFrameworkClassModel(String itemName) {
        this.itemName = itemName;
    }

    public TestFrameworkClassModel(boolean isCheck, String itemName) {
        this.itemName = itemName;
        this.isCheck = isCheck;
    }

    public TestFrameworkClassModel(String itemName, boolean isBase, boolean isStability, boolean isFunction) {
        this.itemName = itemName;
        this.isBase = isBase;
        this.isStability = isStability;
        this.isFunction = isFunction;
    }

    public TestFrameworkClassModel(String itemName, boolean isBase, boolean isStability, boolean isFunction,
                                   Class<?> isBaseClass, Class<?> isFunctionClass, Class<?> isStabilityClass) {
        this.itemName = itemName;
        this.isBase = isBase;
        this.isStability = isStability;
        this.isFunction = isFunction;
        this.isBaseClass = isBaseClass;
        this.isFunctionClass = isFunctionClass;
        this.isStabilityClass = isStabilityClass;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isBase() {
        return isBase;
    }

    public void setBase(boolean isBase) {
        isBase = isBase;
    }

    public boolean isStability() {
        return isStability;
    }

    public void setStability(boolean isStability) {
        isStability = isStability;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public void setFunction(boolean isFunction) {
        isFunction = isFunction;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public Class<?> getIsBaseClass(){
        return isBaseClass;
    }

    public void setIsBaseClass(Class<?> isBaseClass) {
        this.isBaseClass = isBaseClass;
    }

    public Class<?> getIsFunctionClass(){
        return isFunctionClass;
    }

    public void setisFunctionClass(Class<?> isFunctionClass) {
        this.isFunctionClass = isFunctionClass;
    }

    public Class<?> getIsStabilityClass(){
        return isStabilityClass;
    }

    public void setIsStabilityClass(Class<?> isStabilityClass) {
        this.isStabilityClass = isStabilityClass;
    }

    @NonNull
    @Override
    public String toString() {
        return "itemName= " + itemName + ", isBase= " + isBase + ", isStability="+isStability+ ", isFunction="+isFunction+
                ", isBaseClass="+isBaseClass+", isFunctionClass="+isFunctionClass+", isStabilityClass="+isStabilityClass;
    }
}

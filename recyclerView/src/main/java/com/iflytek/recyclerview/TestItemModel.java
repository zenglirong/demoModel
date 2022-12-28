package com.iflytek.recyclerview;

import androidx.annotation.NonNull;

public class TestItemModel {
    private String itemName;
    private boolean isCheck;
    private Class<?> isItemClass;

    public TestItemModel(String itemName) {
        this.itemName = itemName;
    }

    public TestItemModel(String itemName, Class isItemClass) {
        this.itemName = itemName;
        this.isItemClass = isItemClass;
    }

    public TestItemModel(Boolean isCheck, String itemName) {
        this.isCheck = isCheck;
        this.itemName = itemName;
    }

    public TestItemModel(Boolean isCheck, String itemName, Class isItemClass) {
        this.isCheck = isCheck;
        this.itemName = itemName;
        this.isItemClass = isItemClass;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Class getItemClass() {
        return isItemClass;
    }

    public void setItemClass(Class<?> isItemClass) {
        this.isItemClass = isItemClass;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @NonNull
    @Override
    public String toString() {
        return "itemName= " + itemName + ",isItemClass=" + isItemClass;
    }
}

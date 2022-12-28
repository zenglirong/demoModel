package com.iflytek.expandablelistview;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Item {

    private int id;
    private String name;

    public Item() {
    }

    public Item(int iId, String iName) {
        this.id = iId;
        this.name = iName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}

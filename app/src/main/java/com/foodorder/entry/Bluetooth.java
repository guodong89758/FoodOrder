package com.foodorder.entry;

import java.io.Serializable;

/**
 * Created by guodong on 2017/5/30.
 */

public class Bluetooth implements Serializable {

    private boolean hasTitle;
    private String type;
    private String title;
    private String address;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isHasTitle() {
        return hasTitle;
    }

    public void setHasTitle(boolean hasTitle) {
        this.hasTitle = hasTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

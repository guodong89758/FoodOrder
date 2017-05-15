package com.foodorder.entry;

import java.io.Serializable;

/**
 * Created by guodong on 2017/5/13.
 */

public class Printer implements Serializable {

    private String id;
    private String name;
    private boolean isChecked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

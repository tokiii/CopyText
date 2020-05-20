package com.example.copytext;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 记录实体
 */
public class RecordBean extends LitePalSupport {
    private int addTimes;
    private String copyStr;
    private String time;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAddTimes() {
        return addTimes;
    }

    public void setAddTimes(int addTimes) {
        this.addTimes = addTimes;
    }

    public String getCopyStr() {
        return copyStr == null ? "" : copyStr;
    }

    public void setCopyStr(String copyStr) {
        this.copyStr = copyStr;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package com.lvchehui.www.xiangbc.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 张灿能 on 2016/5/10.
 * 作用：
 */
@Table(name = "text")
public class TextDb {
    @Column(name = "id",isId = true)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "msg")
    private String msg;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TextDb{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

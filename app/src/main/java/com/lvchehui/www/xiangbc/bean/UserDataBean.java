package com.lvchehui.www.xiangbc.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 张灿能 on 2016/5/24.
 * 作用：数据信息
 */
@Table(name = "user")
public class UserDataBean {

    @Column(name = "gid")
    public String gid;
    @Column(name = "username")
    public String username;
    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserDataBean{" +
                "gid='" + gid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

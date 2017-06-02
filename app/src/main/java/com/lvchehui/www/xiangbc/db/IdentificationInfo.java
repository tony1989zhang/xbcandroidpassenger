package com.lvchehui.www.xiangbc.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 张灿能 on 2016/7/7.
 * 作用：
 */
@Table(name = "identificationInfo")
public class IdentificationInfo {
//
    @Column(name = "id",isId = true)
    public int id;
    @Column(name = "identification_id")
    public int identification_id;
    @Column(name = "identification_gid")
    public String identification_gid;
    @Column(name = "name")
    public String name;
    @Column(name = "address")
    public String address;
    @Column(name = "owner")
    public String owner;
    @Column(name = "industry_type")
    public String industry_type;

    @Override
    public String toString() {
        return "IdentificationInfo{" +
                ", identification_id='" + identification_id + '\'' +
                ", identification_gid='" + identification_gid + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", owner='" + owner + '\'' +
                ", industry_type='" + industry_type + '\'' +
                '}';
    }
}

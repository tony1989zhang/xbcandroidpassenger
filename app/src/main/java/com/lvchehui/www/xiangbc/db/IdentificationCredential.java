package com.lvchehui.www.xiangbc.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 张灿能 on 2016/7/7.
 * 作用：
 */
@Table(name = "IdentificationCredential")
public class IdentificationCredential {

    @Column(name = "id",isId = true)
    public int id;
    @Column(name = "identification_id")
    public int identification_id;
    @Column(name = "identification_gid")
    public String identification_gid;
    @Column(name = "credential_number")
    public String credential_number;
    @Column(name = "credential_photo_url")
    public String credential_photo_url;
}

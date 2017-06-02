package com.lvchehui.www.xiangbc.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 张灿能 on 2016/7/7.
 * 作用：
 */
@Table(name = "IdentificationSign")
public class IdentificationSign {

    @Column(name = "id",isId = true)
    public int id;
    @Column(name = "identification_id")
    public int identification_id;
    @Column(name = "identification_gid")
    public String identification_gid;
    @Column(name = "sign_in_person_name")
    public String sign_in_person_name;
    @Column(name="sign_in_preson_id_card_no")
    public String sign_in_preson_id_card_no;
    @Column(name = "sign_in_person_id_card_photo_url")
    public String sign_in_person_id_card_photo_url;
    @Column(name = "users_account_auth_log_gid")
    public String users_account_auth_log_gid;
}

package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/7/6.
 * 作用：
 */
public class PushDetailGetListDataBean {

    public String push_detail_gid;
    public String push_content_gid;
    public String client_id;
    public int type;
    public int push_status;
    public int add_time;
    public ContentBean content;
    public static class ContentBean {
        public String title;
        public String summary;
        public String open_url;
        public String down_url;
        public String logo_url;
        public int update_time;
    }
}

package com.lvchehui.www.xiangbc.bean;

import java.util.List;

/**
 * Created by 张灿能 on 2016/7/27.
 * 作用：
 */
public class YunTuResponseBean extends YunTuResponseBaseBean{

    public int count;
    public List<DatasBean> datas;

    public static class DatasBean {
        public String _id;
        public String _location;
        public String _name;
        public String _address;
        public String xbc_gid;
        public String xbc_phone;
        public String xbc_username;
        public int _locstatus;
        public String _createtime;
        public String _updatetime;
        public String _province;
        public String _city;
        public String _district;
        public List<String> _image;

        @Override
        public String toString() {
            return "DatasBean{" +
                    "_id='" + _id + '\'' +
                    ", _location='" + _location + '\'' +
                    ", _name='" + _name + '\'' +
                    ", _address='" + _address + '\'' +
                    ", xbc_gid='" + xbc_gid + '\'' +
                    ", xbc_phone='" + xbc_phone + '\'' +
                    ", xbc_username='" + xbc_username + '\'' +
                    ", _locstatus=" + _locstatus +
                    ", _createtime='" + _createtime + '\'' +
                    ", _updatetime='" + _updatetime + '\'' +
                    ", _province='" + _province + '\'' +
                    ", _city='" + _city + '\'' +
                    ", _district='" + _district + '\'' +
                    ", _image=" + _image +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "YunTuResponseBean{" +
                "count=" + count +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", status=" + status +
                ", datas=" + datas +
                '}';
    }
}

package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/7/13.
 * 作用：成为代理商具体的实现
 */
public class AgentApplyDataBean {
        public String users_gid;
        public String name;
        public String phone;
        public String occupation;
        public String parent_agent_gid;
        public double brokerage_rate;
        public String number_name;
        public String nick_name;
        public int add_time;
        public String agent_gid;

        @Override
        public String toString() {
                return "AgentApplyDataBean{" +
                        "users_gid='" + users_gid + '\'' +
                        ", name='" + name + '\'' +
                        ", phone='" + phone + '\'' +
                        ", occupation='" + occupation + '\'' +
                        ", parent_agent_gid='" + parent_agent_gid + '\'' +
                        ", brokerage_rate=" + brokerage_rate +
                        ", number_name='" + number_name + '\'' +
                        ", nick_name='" + nick_name + '\'' +
                        ", add_time=" + add_time +
                        ", agent_gid='" + agent_gid + '\'' +
                        '}';
        }
}

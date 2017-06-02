package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/7/13.
 * 作用：申请成为代理商
 */
public class AgentApplyBean extends BaseBean {
    public AgentApplyDataBean resData;

    @Override
    public String toString() {
        return "AgentApplyBean{" +
                "resData=" + resData +
                '}' + "err_code" + errCode + "resMsg:" + resMsg;
    }
}

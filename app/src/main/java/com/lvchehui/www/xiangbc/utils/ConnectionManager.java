package com.lvchehui.www.xiangbc.utils;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.DemandSubmitRequestBean;
import com.lvchehui.www.xiangbc.bean.GetQuotationedListBean;
import com.lvchehui.www.xiangbc.bean.RideDemandSubmitRequest;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil.OnDataLoadEndListener;
import com.lvchehui.www.xiangbc.utils.frompost.MultipartRequest;
import com.lvchehui.www.xiangbc.utils.frompost.MultipartRequestParams;
import com.lvchehui.www.xiangbc.zxing.android.PreferencesActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作用：网络连接工具类
 */
public class ConnectionManager {

    /**
     * 提交反馈
     */
    private static final String FEEDBACK_SUBMIT = "/Feedback/submit";
    /**
     * 短信验证码
     */
    private static final String PICTRUE_UPLOAD = "/picture/upload";
    private static final String SEND_SMS_SEND = "/sendsms/send";
    private static final String SEND_SMS_DESTRUCSMS = "/main/sendsms/destructSms";
    private static final String SEND_SMS_FINDSMS = "/sendsms/findSms";

    /**
     * 用户基础
     */
    private static final String USERS_REGISTER = "/Users/register";
    private static final String USERS_LOGIN = "/Users/login";
    private static final String USERS_RESETUSERINFO = "/Users/resetUserInfo";
    private static final String USERS_RESETPASSWORD = "/Users/resetPassword";
    private static final String USERS_SETPAYPASSWORD = "/Users/setPayPassword";
    private static final String USERS_USERINFO = "/Users/userInfo";
    private static final String CHECK_USER_NAME = "/Users/checkUserName";

    /**
     * 车队入住
     * */
    private static final String CARTEAM_SETTLED_SUBMIT = "/Carteam_settled/submit";

    /**
     * 红包
     */
    private static final String REDPACKET_REDPACKETLIST = "/Redpacket/redPacketList";
    private static final String REDPACKET_SHARE = "/Redpacket/share";

    /**
     * 常用信息 联系人
     */
    private static final String CONTACTS_ADDCONTACTS = "/Contacts/addContacts";
    private static final String CONTACTS_EDITCONSTACTS = "/Contacts/editConstacts";
    private static final String CONTACTS_CONTACTSLIST = "/Contacts/contactsList";
    private static final String CONTACTS_CHECKPHONEEXISTS = "/Contacts/checkPhoneExists";
    private static final String CONTACTS_DELCONSTACTS = "/Contacts/delConstacts";

    /**
     * 常用信息 发票抬头
     */
    private static final String INVOICE_INVOICELIST = "/Invoice/invoiceList";
    private static final String INVOICE_INVOICEADD = "/Invoice/invoiceAdd";
    private static final String INVOICE_INVOICEEDIT = "/Invoice/invoiceEdit";
    private static final String INVOICE_INVOICEDEL = "/Invoice/invoiceDel";
    private static final String INVOICE_SETINVOICEDEFAULT = "/Invoice/setInvoiceDefault";
    private static final String INVOICE_GETINVOICEINFO = "/Invoice/getInvoiceInfo";
    private static final String INVOICE_BINDEXPRESSINFO = "/Invoice/bindExpressInfo";

    /**
     * 发票抬头_收件地址
     */
    private static final String EXPRESS_INFO_EXPRESSINFOADD = "/Express_info/expressInfoAdd";
    private static final String EXPRESS_INFO_EXPRESSINFOEDIT = "/Express_info/expressInfoEdit";
    private static final String EXPRESS_INFO_EXPRESSINFODEL = "/Express_info/expressInfoDel";

    /**
     * 申请代理商
     */
    private static final String AGENT_APPLY = "/Agent/apply";
    private static final String AGENT_AUDIT = "/Agent/audit";
    private static final String AGENT_GETAGENTINFO = "/Agent/getAgentInfo";
    private static final String AGENT_EXTENDS_GETCODE = "/Agent_Extends/getCode";

    /**
     * 消息列表
     */
    private static final String PUSH_DETAIL_GETLIST = "/Push_detail/getList";
    private static final String PUSH_DETAIL_COUNTUNREAD = "/Push_detail/countUnread";
    private static final String PUSH_DETAIL_SETREAD = "/Push_detail/setRead";

    /**
     * 用户特权认证管理
     */
    private static final String IDENTIFICATION_GETIDENTIFICATIONLIST = "/Identification/getIdentificationList";//获取认证特权类型
    private static final String IDENTIFICATION_GETIDENTIFICATIONINFO = "/Identification/getIdentificationInfo";//查询认证特权认证项目
    private static final String USERS_ACCOUNT_AUTH_LOG_SUBMITAUTH = "/Users_account_auth_log/submitAuth";//提交认证记录
    private static final String USERS_ACCOUNT_AUTH_LOG_USERSACCOUNTAUTHLIST = "/Users_account_auth_log/usersAccountAuthlist";//用户所有认证记录
    private static final String USERS_ACCOUNT_AUTH_LOG_GETAUTHINFO = "/Users_account_auth_log/getAuthInfo";//查询提交的认证信息

    /**
     * 用户需求管理
     */
    private static final String DEMAND_SUBMIT = "/Demand/submit";
    private static final String DEMAND_ISSUE = "/Demand/issue";
    private static final String DEMAND_CANCEL = "/Demand/cancel";
    private static final String DEMAND_GETLIST = "/Demand/getList";
    private static final String DEMAND_DEAL_PUSH = "/Demand_deal/push";
    /**
     * 保存用户客户端或者设备ID
     */
    private static final String USERS_SAVECLIENTID = "/Users/saveClientId";
    private static final String AMAP_YUNTU_API_ADD_DATA = "http://yuntuapi.amap.com/datamanage/data/create";
    private static final String AMAP_YUNTU_API_UPDATE_DATA = "http://yuntuapi.amap.com/datamanage/data/update";
    /**
     * 获取已报价的列表
     * */
    private static final String QUOTATION_GETQUOTATIONEDLIST = "/Quotation/getQuotationedList";
    /**
     * 保险
     * */
    private static final String INSURANCE_GETLIST = "/Insurance/getList";
    private static ConnectionManager mConnectionManager;
    /**
     * 提交订单
     * */
    private static final String ORDER_SUBMIT = "/Order/submit";
    private static final String ORDER_GETINFO = "/Order/getInfo";
    /**
     * 需求保险
     * */
    private  static final String INSURANCE_DETAIL_SUBMIT = "/Insurance_Detail/submit";
    private  static final String QUOTATION_GETQUOTATIONEDCOUNT = "/Quotation/getQuotationedCount";

    /**
     * 提交再线支付
     * */
    private static final String PREPARE_PAY_SUBMIT = "/Prepare_Pay/submit";
    private static final String ONLINE_PAY_SUBMIT = "/Online_Pay/submit";
    private static final String ONLINE_PAY_CREATE = "/Online_Pay/create";
    private static final String PREPARE_PAY_PAY = "/Prepare_Pay/Pay";
    /**
     * 顺风车
     * */
    private static final String USERS_ACCOUNT_FUND_FLOW_SUBMIT = "Users_Account_Fund_Flow/submit";
    private static final String QUOTATION_FILTERQUOTATIONLIST = "/Quotation/filterQuotationList";


    private static final String QUOTATION_GETINFO = "/Quotation/getInfo";
    private ConnectionManager() {
    }

    public static final ConnectionManager getInstance() {
        if (mConnectionManager == null) {
            mConnectionManager = new ConnectionManager();
        }
        return mConnectionManager;
    }


    /**
     * 检测用户名是否可以注册
     * @param context
     * @param username
     * @param listener
     * @return
     */
    public Request checkUserName(Context context, String username, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        return ConnectionUtil.getInstance().doPost(context, CHECK_USER_NAME, params, listener);
    }

    public Request sendSms(Context context, String mobileNo, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("to", mobileNo);
        return ConnectionUtil.getInstance().doPost(context, SEND_SMS_SEND, params, listener);
    }

    public Request destructSms(Context context, String mobileNo, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("to", mobileNo);
        return ConnectionUtil.getInstance().doPost(context, SEND_SMS_DESTRUCSMS, params, listener);
    }

    public Request findSms(Context context, String mobileNo, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("to", mobileNo);
        return ConnectionUtil.getInstance().doPost(context, SEND_SMS_FINDSMS, params, listener);
    }

    public Request upLoadFile(Context context, String headImgFile, OnDataLoadEndListener listener) {
        MultipartRequestParams multipartRequestParams = new MultipartRequestParams();
        multipartRequestParams.put("file", new File(headImgFile));
        Request request = new MultipartRequest(ConnectionUtil.getInstance().getUrl(PICTRUE_UPLOAD), multipartRequestParams, listener);
        ConnectionUtil.getInstance().getRequestQueue(context).add(request);
        return request;
    }

    /**
     * 注册
     * */
    public Request register(Context context, String username, String password, String smscode, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("smscode", smscode);
        return ConnectionUtil.getInstance().doPost(context, USERS_REGISTER, params, listener);
    }

    /**
     * 登录
     * */
    public Request login(Context context, String username, String password, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        return ConnectionUtil.getInstance().doPost(context, USERS_LOGIN, params, listener);
    }

    /**
     * 重设密码
     * */
    public Request resetPassword(Context context, String username, String password, String smscode, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("smscode", smscode);
        return ConnectionUtil.getInstance().doPost(context, USERS_RESETPASSWORD, params, listener);

    }

    /*
     * 从新设置用户信息
     * */
    public Request resetUserInfo(Context context, String gid, String nick_name, String sex, String birthday, String email, String head_url, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("nick_name", nick_name);
        params.put("sex", sex);
        params.put("birthday", birthday);
        params.put("email", email);
        params.put("head_url", head_url);
        return ConnectionUtil.getInstance().doPost(context, USERS_RESETUSERINFO, params, listener);
    }

    public Request usersUserInfo(Context context,String gid,OnDataLoadEndListener listener){
        Map<String,String> params = new HashMap<>();
        params.put("gid",gid);
        return ConnectionUtil.getInstance().doPost(context,USERS_USERINFO,params,listener);
    }

    /**
     * 设置支付密码
     */
    public Request userSetPayPassword(Context context, String users_gid, String pay_password, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        params.put("pay_password", pay_password);
        return ConnectionUtil.getInstance().doPost(context, USERS_SETPAYPASSWORD, params, listener);
    }

    /**
     * 红包列表
     */
    public Request redPacketList(Context context, String gid, String usage, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("usage", usage);
        return ConnectionUtil.getInstance().doPost(context, REDPACKET_REDPACKETLIST, params, listener);
    }

    /**
     * 分享获取红包
     */
    public Request redpacket_Share(Context context, String gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        return ConnectionUtil.getInstance().doPost(context, REDPACKET_SHARE, params, listener);
    }

    /**
     * 增加联系人
     */
    public Request addContacts(Context context, String gid, String phone, String name, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("phone", phone);
        params.put("name", name);
        return ConnectionUtil.getInstance().doPost(context, CONTACTS_ADDCONTACTS, params, listener);
    }

    /**
     * 更改联系人
     */
    public Request editContacts(Context context, String gid, String contacts_gid, String phone, String name, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("contacts_gid", contacts_gid);
        params.put("phone", phone);
        params.put("name", name);
        return ConnectionUtil.getInstance().doPost(context, CONTACTS_EDITCONSTACTS, params, listener);
    }

    /**
     * 删除联系人
     */
    public Request delConstacts(Context context, String gid, String contacts_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("contacts_gid", contacts_gid);
        return ConnectionUtil.getInstance().doPost(context, CONTACTS_DELCONSTACTS, params, listener);
    }

    /**
     * 联系人列表
     */
    public Request contactsList(Context context, String gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        return ConnectionUtil.getInstance().doPost(context, CONTACTS_CONTACTSLIST, params, listener);
    }

    /**
     * 检查联系人是否存在
     */
    public Request checkPhoneExists(Context context, String gid, String phone, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("phone", phone);
        return ConnectionUtil.getInstance().doPost(context, CONTACTS_CHECKPHONEEXISTS, params, listener);
    }

    public Request carteamSettledSubmit(Context context,String users_gid,String name,String phone,String content,OnDataLoadEndListener listener){
        Map<String,String> params = new HashMap<>();
        params.put("users_gid",users_gid);
        params.put("name",name);
        params.put("phone",phone);
        params.put("content",content);
        return ConnectionUtil.getInstance().doPost(context,CARTEAM_SETTLED_SUBMIT,params,listener);
    }
    /**
     * 发票列表
     */
    public Request invoiceList(Context context, String gid, String parse_fields, OnDataLoadEndListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("parse_fields", parse_fields);
        return ConnectionUtil.getInstance().doPost(context, INVOICE_INVOICELIST, params, listener);
    }

    /**
     * 添加发票抬头
     */
    public Request invoiceAdd(Context context, String gid, String invoice_name, OnDataLoadEndListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("invoice_name", invoice_name);
        return ConnectionUtil.getInstance().doPost(context, INVOICE_INVOICEADD, params, listener);
    }

    /**
     * 编辑发票抬头
     */
    public Request invoiceEdit(Context context, String gid, String invoice_gid, String invoice_name, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("invoice_gid", invoice_gid);
        params.put("invoice_name", invoice_name);
        return ConnectionUtil.getInstance().doPost(context, INVOICE_INVOICEEDIT, params, listener);
    }

    /**
     * 删除发票抬头
     */
    public Request invoiceDel(Context context, String gid, String invoice_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("invoice_gid", invoice_gid);
        return ConnectionUtil.getInstance().doPost(context, INVOICE_INVOICEDEL, params, listener);
    }

    /**
     * 设置默认发票
     */
    public Request setInvoiceDefault(Context context, String gid, String invoice_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("invoice_gid", invoice_gid);
        return ConnectionUtil.getInstance().doPost(context, INVOICE_SETINVOICEDEFAULT, params, listener);
    }

    /**
     * 获取发票抬头信息
     */
    public Request getInvoiceInfo(Context context, String gid, String invoice_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("invoice_gid", invoice_gid);
        return ConnectionUtil.getInstance().doPost(context, INVOICE_GETINVOICEINFO, params, listener);
    }

    /**
     * 绑定发票收件地址
     */
    public Request bindExpressInfo(Context context, String gid, String invoice_gid, String express_info_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("invoice_gid", invoice_gid);
        params.put("express_info_gid", express_info_gid);
        return ConnectionUtil.getInstance().doPost(context, INVOICE_BINDEXPRESSINFO, params, listener);
    }

    /**
     * 添加收件人地址
     */
    public Request expressInfoAdd(Context context, String gid, String consignee, String phone, String address, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("address", address);
        return ConnectionUtil.getInstance().doPost(context, EXPRESS_INFO_EXPRESSINFOADD, params, listener);
    }

    /**
     * 删除收件人地址
     */
    public Request expressInfoDel(Context context, String gid, String express_info_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("express_info_gid", express_info_gid);
        return ConnectionUtil.getInstance().doPost(context, EXPRESS_INFO_EXPRESSINFODEL, params, listener);
    }

    /**
     * 编辑收件人地址
     */
    public Request expressInfoEdit(Context context, String gid, String express_info_gid, String consignee, String phone, String address, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("express_info_gid", express_info_gid);
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("address", address);
        return ConnectionUtil.getInstance().doPost(context, EXPRESS_INFO_EXPRESSINFOEDIT, params, listener);
    }

    /**
     * 保存clientId
     */
    public Request usersSaveClientId(Context context, String gid, String client_id, String item, String users_type, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("gid", gid);
        params.put("client_id", client_id);
        params.put("item", item);
        params.put("users_type", users_type);
        return ConnectionUtil.getInstance().doPost(context, USERS_SAVECLIENTID, params, listener);
    }

    /**
     * 申请代理商
     */
    public Request agentApply(Context context, String users_gid, String phone, String true_name, String occupation, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        params.put("phone", phone);
        params.put("true_name", true_name);
        params.put("occupation", occupation);
        return ConnectionUtil.getInstance().doPost(context, AGENT_APPLY, params, listener);
    }

    /**
     * 代理商审核操作 API
     */
    public Request agentAudit(Context context, String users_gid, String agent_gid, String agent_status, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        params.put("agent_gid", agent_gid);
        params.put("agent_status", agent_status);
        return ConnectionUtil.getInstance().doPost(context, AGENT_AUDIT, params, listener);
    }

    /**
     * 查询代理商信息
     */
    public Request agentGetAgentInfo(Context context, String users_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        return ConnectionUtil.getInstance().doPost(context, AGENT_GETAGENTINFO, params, listener);
    }

    /**
     * 分享特征码
     */
    public Request agentExtendsGetcode(Context context, String users_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        return ConnectionUtil.getInstance().doPost(context, AGENT_EXTENDS_GETCODE, params, listener);

    }

    /**
     * 获取消息列表
     */
    public Request pushDetailGetList(Context context, String users_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        return ConnectionUtil.getInstance().doPost(context, PUSH_DETAIL_GETLIST, params, listener);
    }

    /**
     * 首页显示未读消息
     */
    public Request pushDetailCountUnread(Context context, String users_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        return ConnectionUtil.getInstance().doPost(context, PUSH_DETAIL_COUNTUNREAD, params, listener);
    }

    /**
     * 设置消息已经读
     */
    public Request pushDetailSetRead(Context context, String users_gid, String push_detail_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        params.put("push_detail_gid", push_detail_gid);
        return ConnectionUtil.getInstance().doPost(context, PUSH_DETAIL_SETREAD, params, listener);
    }

    /**
     * 获取认证类型列表
     */
    public Request getIdentificationList(Context context, String users_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        return ConnectionUtil.getInstance().doPost(context, IDENTIFICATION_GETIDENTIFICATIONLIST, params, listener);
    }

    /**
     * 特权认证——————提交认证
     */
    public Request SubmitAuth(Context context,
                              String users_gid,
                              String identification_id,
                              String identification_gid,
                              String name,
                              String address,
                              String owner,
                              String industry_type,
                              String credential_number,
                              String credential_photo_url,
                              String sign_in_person_name,
                              String sign_in_preson_id_card_no,
                              String sign_in_person_id_card_photo_url,
                              // String users_account_auth_log_gid,
                              OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        params.put("identification_id", identification_id);
        params.put("identification_gid", identification_gid);
        params.put("name", name);
        params.put("address", address);
        params.put("owner", owner);
        params.put("industry_type", industry_type);
        params.put("credential_number", credential_number);
        params.put("credential_photo_url", credential_photo_url);
        params.put("sign_in_person_name", sign_in_person_name);
        params.put("sign_in_preson_id_card_no", sign_in_preson_id_card_no);
        params.put("sign_in_person_id_card_photo_url", sign_in_person_id_card_photo_url);
        // params.put("users_account_auth_log_gid", users_account_auth_log_gid);
        return ConnectionUtil.getInstance().doPost(context, USERS_ACCOUNT_AUTH_LOG_SUBMITAUTH, params, listener);
    }

    /**
     * 用户特权认证特权
     * */
    public Request usersAccountAuthlist(Context context, String users_gid, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        return ConnectionUtil.getInstance().doPost(context,USERS_ACCOUNT_AUTH_LOG_USERSACCOUNTAUTHLIST, params,listener);
    }

    /**
     * 查询认证特权项目
     * */
    public Request getIdentificationInfo(Context context,String users_gid,String identification_id,String identification_gid,OnDataLoadEndListener listener){
        Map<String,String> params = new HashMap<>();
        params.put("users_gid",users_gid);
        params.put("identification_id",identification_id);
        params.put("identification_gid",identification_gid);
        return ConnectionUtil.getInstance().doPost(context,IDENTIFICATION_GETIDENTIFICATIONINFO,params,listener);
    }

    /**
     * 获取用户认证记录
     * */
    public Request getAuthInfo(Context context,String users_gid,OnDataLoadEndListener listener){
        Map<String,String> params = new HashMap<>();
        params.put("users_gid",users_gid);
        return  ConnectionUtil.getInstance().doPost(context,USERS_ACCOUNT_AUTH_LOG_GETAUTHINFO,params,listener);
    }

    public Request demandSubmit(Context context,RideDemandSubmitRequest demandSubmitRequestBean,OnDataLoadEndListener listener)
    {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", (String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("use_type", demandSubmitRequestBean.use_type);
        params.put("begin_time", demandSubmitRequestBean.begin_time);
        if (!StringUtils.isEmpty(demandSubmitRequestBean.end_time)) {
            params.put("end_time", demandSubmitRequestBean.end_time);
        }
        params.put("pepole_num", demandSubmitRequestBean.pepole_num);
        if (!StringUtils.isEmpty(demandSubmitRequestBean.car_num)) {
            params.put("car_num", demandSubmitRequestBean.car_num);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.car_model)) {
            params.put("car_model", demandSubmitRequestBean.car_model);
        }
        params.put("begin_address", demandSubmitRequestBean.begin_address);
        if (!StringUtils.isEmpty(demandSubmitRequestBean.end_address)) {
            params.put("end_address", demandSubmitRequestBean.end_address);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.midway_address)) {
            params.put("midway_address", demandSubmitRequestBean.midway_address);
        }
        params.put("contacts_name", demandSubmitRequestBean.contacts_name);
        params.put("contacts_phone", demandSubmitRequestBean.contacts_phone);
        if (!StringUtils.isEmpty(demandSubmitRequestBean.use_remark)) {
            params.put("use_remark", demandSubmitRequestBean.use_remark);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.use_trip)) {
            params.put("use_trip", demandSubmitRequestBean.use_trip);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.invoice_name)) {
            params.put("invoice_name", demandSubmitRequestBean.invoice_name);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.invoice_consignee)) {
            params.put("invoice_consignee", demandSubmitRequestBean.invoice_consignee);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.invoice_phone)) {
            params.put("invoice_phone", demandSubmitRequestBean.invoice_phone);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.invoice_address)) {
            params.put("invoice_address", demandSubmitRequestBean.invoice_address);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.request_start_city)) {
            params.put("request_start_city", demandSubmitRequestBean.request_start_city);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.quotation_gid)){
            params.put("quotation_gid",demandSubmitRequestBean.quotation_gid);
        }
        return ConnectionUtil.getInstance().doPost(context, DEMAND_SUBMIT, params, listener);
    }
    /**
     * 用户需求之提交需求
     */
    public Request demandSubmit(Context context, String users_gid, DemandSubmitRequestBean demandSubmitRequestBean, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        params.put("use_type", demandSubmitRequestBean.use_type);
        params.put("begin_time", demandSubmitRequestBean.begin_time);
        if (!StringUtils.isEmpty(demandSubmitRequestBean.end_time)) {
            params.put("end_time", demandSubmitRequestBean.end_time);
        }
        params.put("pepole_num", demandSubmitRequestBean.pepole_num);
        if (!StringUtils.isEmpty(demandSubmitRequestBean.car_num)) {
            params.put("car_num", demandSubmitRequestBean.car_num);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.car_model)) {
            params.put("car_model", demandSubmitRequestBean.car_model);
        }
        params.put("begin_address", demandSubmitRequestBean.begin_address);
        if (!StringUtils.isEmpty(demandSubmitRequestBean.end_address)) {
            params.put("end_address", demandSubmitRequestBean.end_address);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.midway_address)) {
            params.put("midway_address", demandSubmitRequestBean.midway_address);
        }
        params.put("contacts_name", demandSubmitRequestBean.contacts_name);
        params.put("contacts_phone", demandSubmitRequestBean.contacts_phone);
        if (!StringUtils.isEmpty(demandSubmitRequestBean.use_remark)) {
            params.put("use_remark", demandSubmitRequestBean.use_remark);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.use_trip)) {
            params.put("use_trip", demandSubmitRequestBean.use_trip);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.invoice_name)) {
            params.put("invoice_name", demandSubmitRequestBean.invoice_name);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.invoice_consignee)) {
            params.put("invoice_consignee", demandSubmitRequestBean.invoice_consignee);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.invoice_phone)) {
            params.put("invoice_phone", demandSubmitRequestBean.invoice_phone);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.invoice_address)) {
            params.put("invoice_address", demandSubmitRequestBean.invoice_address);
        }
        if (!StringUtils.isEmpty(demandSubmitRequestBean.request_start_city)) {
            params.put("request_start_city", demandSubmitRequestBean.request_start_city);
        }
        return ConnectionUtil.getInstance().doPost(context, DEMAND_SUBMIT, params, listener);
    }

    /**
     * 取消需求
     */
    public Request demandCancel(Context context,String demand_gid, String cancel_reason, String deduct_credit, OnDataLoadEndListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("demand_gid", demand_gid);
        params.put("cancel_reason", cancel_reason);
        params.put("deduct_credit", deduct_credit);
        return ConnectionUtil.getInstance().doPost(context, DEMAND_CANCEL, params, listener);
    }

    /**
     * 发布需求。
     */
    public Request demandIssue(Context context, String users_gid, String demand_gid, OnDataLoadEndListener listener) {

        HashMap<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        params.put("demand_gid", demand_gid);
        return ConnectionUtil.getInstance().doPost(context, DEMAND_ISSUE, params, listener);
    }

    public Request demandGetList(Context context,String users_gid,int page,int list_rows,OnDataLoadEndListener listener)
    {
        return demandGetList(context, users_gid, page, list_rows, null, listener);
    }
    /**
     * 获取需求列表
     */
    public Request demandGetList(Context context, String users_gid, int page, int list_rows,String requset_category, OnDataLoadEndListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("users_gid", users_gid);
        params.put("page", "" + page);
        params.put("list_rows", "" + list_rows);
        if (StringUtils.isNotEmpty(requset_category))
        params.put("requset_category",requset_category);
        return ConnectionUtil.getInstance().doPost(context, DEMAND_GETLIST, params, listener);
    }

    /**
     * 发布需求
     * */
    public Request feedbackSubmit(Context context,String users_gid,String use_car_category,String function_category,String content,OnDataLoadEndListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("users_gid",users_gid);
        params.put("use_car_category",use_car_category);
        params.put("function_category",function_category);
        params.put("content",content);
        return ConnectionUtil.getInstance().doPost(context,FEEDBACK_SUBMIT,params,listener);
    }


    public Request demandDealPush(Context context,String demand_gid,OnDataLoadEndListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("demand_gid",demand_gid);
        return ConnectionUtil.getInstance().doPost(context,DEMAND_DEAL_PUSH,params,listener);
    }


    /**
     * 云图初始创建
     * */
    public Request amapYuntuCreate(Context context,String key,String dataJson,OnDataLoadEndListener listener){
       HashMap<String,String> params = new HashMap<>();
        params.put("key",key);
        params.put("tableid", Constants.AMAP_YUNTU_TABLEID);
        params.put("data",dataJson);
        return ConnectionUtil.getInstance().doPost(context,AMAP_YUNTU_API_ADD_DATA,params, "application/x-www-form-urlencoded",listener);
    }
    /**
     * 云图更新
     * */
    public Request amapYuntuUpdate(Context context,String key,String dataJson,OnDataLoadEndListener listener){
        HashMap<String, String> params = new HashMap<>();
        params.put("key",key);
        params.put("tableid", Constants.AMAP_YUNTU_TABLEID);
        params.put("data",dataJson);
        return ConnectionUtil.getInstance().doPost(context, AMAP_YUNTU_API_UPDATE_DATA, params, "application/x-www-form-urlencoded", listener);
    }
    /**
     * 云图查询
     * */
    public Request amapYuntuSelector(Context context,OnDataLoadEndListener listener){
        String url = "http://yuntuapi.amap.com/datamanage/data/list?tableid="+ Constants.AMAP_YUNTU_TABLEID+"&filter=xbc_gid:"+SPUtil.getInstance(context).get(Constants.USER_GID,"")+"+type:全国&limit=1&page=1&key=" + Constants.AMAP_WEB_API_KEY;
        return ConnectionUtil.getInstance().doAbPathGet(context, url, listener);
    }


    /**
     * 获取报价列表
     * */
    public Request<GetQuotationedListBean> getQuotationedList(Context context,String demand_gid,OnDataLoadEndListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("demand_gid",demand_gid);
        return ConnectionUtil.getInstance().doPost(context, QUOTATION_GETQUOTATIONEDLIST, params, listener);
    }

    /**
     * 获取保险列表
     * */
    public Request insuranceGetList(Context context,OnDataLoadEndListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        return ConnectionUtil.getInstance().doPost(context,INSURANCE_GETLIST,params,listener);
    }
    /**
     * 需求保险
     * */
    public Request insuranceDetailSubmit(Context context,String insurance_gid,String quantity,OnDataLoadEndListener listener){
        HashMap<String,String> params =new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("insurance_gid",insurance_gid);
        params.put("quantity",quantity);
        return ConnectionUtil.getInstance().doPost(context,INSURANCE_DETAIL_SUBMIT,params,listener);
    }

    /**
     * 提交订单
     * */
    public Request orderSubmit(Context context,
                               String demand_gid,
                               String quotation_gid,
                               List<String> insurance_details,
                               String order_category,
                               String is_public,
                               OnDataLoadEndListener listener)
    {
        HashMap<String,String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        if (null != insurance_details)
        params.put("insurance_details", new Gson().toJson(insurance_details));
        params.put("demand_gid",demand_gid);
        params.put("quotation_gid",quotation_gid);
        params.put("order_category",order_category);
        params.put("is_public",is_public);
        return ConnectionUtil.getInstance().doPost(context, ORDER_SUBMIT, params, listener);
    }

    public Request preparePaySubmit(Context context,String order_gid,OnDataLoadEndListener listener){
        return preparePaySubmit(context, null, order_gid, null, null, listener);
    }
    /**
     * 提交预支付
     * */
    public Request preparePaySubmit(Context context,
                                    HashMap<String,String> pay_handle,
                                   String order_gid,
                                   String prepare_pay_red_packet_gids,

                                   String is_bind_demand,
                                   OnDataLoadEndListener listener){
        HashMap<String, String> params = null;
        if (pay_handle == null){
            params = new HashMap<>();
        }
        params = pay_handle;
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("order_gid",order_gid);
        if (!StringUtils.isEmpty(prepare_pay_red_packet_gids))
        params.put("prepare_pay_red_packet_gids",prepare_pay_red_packet_gids);
        if (!StringUtils.isEmpty(is_bind_demand))
        params.put("is_bind_demand",is_bind_demand);
        return ConnectionUtil.getInstance().doPost(context, PREPARE_PAY_SUBMIT, params, listener);
    }

    public Request onlinePaySubmit(Context context,
                                   String online_pay_category,
                                   String total_amount,
                                   String prepare_pay_gid,
                                   String pay_title,
                                   String pay_body,
                                   OnDataLoadEndListener listener)
    {
        return onlinePaySubmit(context,
                online_pay_category,
                total_amount,
                prepare_pay_gid,
                pay_title,
                pay_body,
                null,
                null,
                listener);
    }

    /**
     * 提交在线支付
     * */
    public Request onlinePaySubmit(
            Context context,
            String online_pay_category,
            String total_amount,
            String prepare_pay_gid,
            String pay_title,
            String pay_body,
            String discount,
            String seller_id,
            OnDataLoadEndListener listener){
        HashMap<String,String> params  = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("online_pay_category",online_pay_category);
        params.put("total_amount",total_amount);
        params.put("prepare_pay_gid",prepare_pay_gid);
        params.put("pay_title",pay_title);
        params.put("pay_body",pay_body);
        if (!StringUtils.isEmpty(discount))
        params.put("discount",discount);
        if (!StringUtils.isEmpty(seller_id))
        params.put("seller_id",seller_id);

        System.out.print("params:" + params.toString());
        return ConnectionUtil.getInstance().doPost(
                context, ONLINE_PAY_SUBMIT, params, listener);
    }

    /**
     *创建在线支付订单
     * */

    public Request onlinePayCreate(Context context,String online_pay_gid,OnDataLoadEndListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("online_pay_gid", online_pay_gid);
        return ConnectionUtil.getInstance().doPost(context,ONLINE_PAY_CREATE,params,listener);
    }

    public Request quotationFilterquotationlist(Context context,
                                                String pepole_num,
                                                String begin_time,
                                                String begin_address,
                                                String end_address,
                                                OnDataLoadEndListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("pepole_num",pepole_num);
        params.put("begin_time",begin_time);
        params.put("begin_address",begin_address);
        params.put("end_address",end_address);
        return ConnectionUtil.getInstance().doPost(context,QUOTATION_FILTERQUOTATIONLIST,params,listener);
    }

    public Request quotationGetinfo(Context context,String quotation_gid,OnDataLoadEndListener listener){
        HashMap<String, String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("quotation_gid",quotation_gid);
        return ConnectionUtil.getInstance().doPost(context,QUOTATION_GETINFO,params,listener);
    }

    public Request orderGetinfo(Context context,String order_gid,OnDataLoadEndListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("order_gid",order_gid);
        return ConnectionUtil.getInstance().doPost(context,ORDER_GETINFO,params,listener);
    }

    public Request PreparePayPay(Context context,String prepare_pay_gid,OnDataLoadEndListener listener){
        usersAccountFundFlowSubmit(context, new OnDataLoadEndListener() {
                    @Override
                    public void OnLoadEnd(String ret) {
                    }
                }
        );
        HashMap<String, String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("prepare_pay_gid",prepare_pay_gid);
        return ConnectionUtil.getInstance().doPost(context,PREPARE_PAY_PAY,params,listener);
    }


    public Request quotationGetquotationedcount(Context context,String demand_gid,OnDataLoadEndListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("demand_gid",demand_gid);
        return  ConnectionUtil.getInstance().doPost(context,QUOTATION_GETQUOTATIONEDCOUNT,params,listener);

    }


    public Request usersAccountFundFlowSubmit(Context context,OnDataLoadEndListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("users_gid",(String)SPUtil.getInstance(context).get(Constants.USER_GID,""));
        params.put("money","10000000000000");
        params.put("type","1");
        params.put("description","测试财务流水");
        params.put("flow_status","1");
        params.put("flow_category","4");
        return ConnectionUtil.getInstance().doPost(context,USERS_ACCOUNT_FUND_FLOW_SUBMIT,params,listener);
    }
}

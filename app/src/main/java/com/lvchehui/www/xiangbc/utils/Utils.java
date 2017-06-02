package com.lvchehui.www.xiangbc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.utils.updateHelper.UpdateHelper;
import com.lvchehui.www.xiangbc.view.wheelData.DateChooseWheelViewDialog;
import com.lvchehui.www.xiangbc.zxing.android.CaptureActivity;
import com.lvchehui.www.xiangbc.zxing.encode.CodeCreator;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.x;

public final class Utils {

    private Utils() {

    }


    public static String getDemandUrl(String demand_gid){
        return  getDemandUrl(demand_gid,1,null);
    }

    public static String getDemandUrl(String demand_gid,int order_status){
        return  getDemandUrl(demand_gid,order_status,null);
    }

    public static String getDemandUrl(String demand_gid,int order_status,String quotation_gid) {
        StringBuffer demandUrl = new StringBuffer("http://120.76.119.55:8009/h5_xbc/order_bill_driver.html?");
        demandUrlappend((String) SPUtil.getInstance(x.app()).get(Constants.USER_GID, ""), demandUrl, "uri_users_gid=");
        if (StringUtils.isNotEmpty(demand_gid)) {
            demandUrlappend(demand_gid, demandUrl, "uri_demand_gid=");
        }
        if (StringUtils.isNotEmpty(quotation_gid)) {
            demandUrlappend(quotation_gid, demandUrl, "uri_quotation_gid=");
        }
        if (StringUtils.isNotEmpty(order_status)) {
            demandUrlappend(""+order_status, demandUrl, "uri_order_status=");
        }
        return demandUrl.toString();
    }

    private static void demandUrlappend(String append, StringBuffer demandUrl, String string) {
        demandUrl.append(string);
        demandUrl.append(append);
        demandUrl.append("&");
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phone
     */
    public static void call(Context context, String phone) {

        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (SecurityException e) {
            showToast(context, phone);
        }


    }

    /**
     * 发送短信
     *
     * @param context
     * @param phone
     * @param message
     */
    public static void sendSMS(Context context, String phone, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> divideMessage = smsManager.divideMessage(message);
        String SENT_SMS_ACTION = "com.lvchehui.www.ACTION_SENT_SMS_ACTION";
        Intent intent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, intent, 0);
        for (String text : divideMessage) {
            smsManager.sendTextMessage(phone, null, text, sentPI, null);
        }

    }

    /**
     * 查看联系人调用
     *
     * @param
     */
    public static void getCursorRequest(Activity activity) {
        activity.startActivityForResult(new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI), 0);
    }
    // 调用查看联系人 startActivityForResult(new Intent(Intent.ACTION_PICK,
    // ContactsContract.Contacts.CONTENT_URI), 0);
    // 在OnActivityResult 判断 resultCode == Activity.RESULT_OK

    public static HashMap<String, Object> getCursorResult(Intent data, Activity activity) {
        if (null == activity) {
            return null;
        }
        HashMap<String, Object> hashMaps = new HashMap<String, Object>();
        ArrayList<String> uphone = new ArrayList<String>();
        ContentResolver reContentResolverol = activity.getContentResolver();
        Uri contactData = data.getData();
        @SuppressWarnings("deprecation")
        Cursor cursor = activity.managedQuery(contactData, null, null, null, null);
        cursor.moveToFirst();
        String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        hashMaps.put("name", username);
        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

        while (phone.moveToNext()) {
            String u = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            uphone.add(u);
        }

        hashMaps.put("phone", uphone);
        return hashMaps;
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity a, float bgAlpha) {
        WindowManager.LayoutParams lp = a.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        a.getWindow().setAttributes(lp);
    }


    public static void showToast(Context context, String hint) {
        showToast(context, hint, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String hint, int duration) {

        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        Toast toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
        toast.setView(toastView);
        TextView tv = (TextView) toastView.findViewById(R.id.toastMessage);
        tv.setText(hint);
        toast.setDuration(duration);
        toast.show();
    }

    public static void saveToSd(String text) {
        try {
            File file = new File("/sdcard/" + File.separator + "121.log");

            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            pw.write(text);
            pw.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * app升级
     */
    public static void updateHelper(String url) {
        UpdateHelper updateHelper = new UpdateHelper.Builder(App.getInstance().getBaseContext())
                .checkUrl(url)
                .isAutoInstall(false) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。

                .isHintNewVersion(true)//没有新版本是否提示
                .build();
        updateHelper.check();
    }

    /**
     * 取消该页面EditText 占焦点
     */
    public static void hideInput(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 关闭软键盘
     */
    public static void toggleInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }


    /**
     * 设置时间控件
     * 支持日期
     * 支持日期加时分秒
     */

    public static void setDataTime(Context context, String title, final TextView tv, boolean timePickerGone) {
        DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(context, new DateChooseWheelViewDialog.DateChooseInterface() {
            @Override
            public void getDateTime(String time, boolean longTimeChecked) {
                tv.setText(time);
            }
        }, true);

        startDateChooseDialog.setDateDialogTitle(title);
        startDateChooseDialog.setTimePickerGone(timePickerGone);
        startDateChooseDialog.showDateChooseDialog();
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化价格 强制保留2位小数
     */
    public static String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(price);
        return format;
    }

    /**
     * 去除特殊符号
     */
    public static String formatUri(String content) {
        if (content.contains("&")) {

            content = content.replace("&#039;", "'");
            content = content.replace("&quot;", "\"");
            content = content.replace("&lt;", "<");
            content = content.replace("&gt;", ">");
            content = content.replace("&amp;", "&");
        }
        return content;
    }

    /**
     * url 解码
     * */
    public static String deCodeString(String content){
        try {
           return URLDecoder.decode(content,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateToStr(long theDate, String pattern) {

        Date date = new Date(theDate*1000);
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        String format = sf.format(date);
        return format;
    }
}

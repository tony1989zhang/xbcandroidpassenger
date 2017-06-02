package com.lvchehui.www.xiangbc.bean;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.lvchehui.www.xiangbc.app.App;

import java.lang.reflect.Type;

/**
 * Created by 张灿能 on 2016/7/13.
 * 作用：解决同一个字段不同类型的情况
 */
public class UsersAccountAuthDeserializer implements JsonDeserializer<UsersAccountAuthlistBean> {
    @Override
    public UsersAccountAuthlistBean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        BaseBean errorBean = App.getInstance().getBeanFromJson(jsonElement.toString(), BaseBean.class);
        UsersAccountAuthlistBean usersAccountAuthDeserializer = new UsersAccountAuthlistBean();
        usersAccountAuthDeserializer.resData = null;
        usersAccountAuthDeserializer.resMsg = errorBean.resMsg;
        usersAccountAuthDeserializer.errCode = errorBean.errCode;
        return usersAccountAuthDeserializer;
    }
}

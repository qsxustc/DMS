package com.dms.springboot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.*;

class Sample {

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    String res;

    public static void main(String []args) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
            .url("https://aip.baidubce.com/oauth/2.0/token?client_id=IYy2QKBA2vln55ob0H24K2Dd&client_secret=OjEikLMsMxUMncYUalK83Yaeq5aWDKbL&grant_type=client_credentials")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        JSONObject jsonObject=JSON.parseObject(response.body().string());
        Sample sample=new Sample();
        sample.res=(String) jsonObject.get("access_token");
        sample.getaccesstoken();
    }
    public String getaccesstoken(){
        System.out.println(this.res);
        return this.res;
    }
}


/*
输入完成后运行代码，服务器将返回json文本参数，如下：
access_token：要获取的Access Token；
图片11.png

expires_in：Access Token的有效期(秒为单位，有效期30天)；
图片12.png

其他参数忽略，暂时不用;
若请求错误，服务器将返回的JSON文本包含以下参数：
error： 错误码；关于错误码的详细信息请参考下方鉴权认证错误码。
error_description： 错误描述信息，帮助理解和解决发生的错误。
* */
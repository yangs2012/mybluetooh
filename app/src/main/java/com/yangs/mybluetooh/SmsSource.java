package com.yangs.mybluetooh;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yangs on 2017/11/3 0003.
 */

public class SmsSource {
    private OkHttpClient mOkHttpClient;
    private Headers requestHeaders;
    private static final String accountSid = "55e1f542aafa451b995a2f8a1a1400ac";
    private static final String authtoken = "80e97f1e93354f189c3a9e91c6d2fbe2";
    private static final String ACCOUNT_INFO = "https://api.miaodiyun.com/20150822/query/accountInfo";

    public SmsSource() {
        requestHeaders = new Headers.Builder()
                .add("Content-type", "application/x-www-form-urlencoded")
                .add("User-Agent", "yangs mybluetooh").build();
        mOkHttpClient = new OkHttpClient.Builder().followRedirects(false)
                .followSslRedirects(false).build();
    }

    public void sendSms(String msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = df.format(new Date());
        String sig;
        try {
            sig = Bit32(accountSid + authtoken + time);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add("accountSid", accountSid)
                .add("timestamp", time).add("sig", sig).add("respDataType", "JSON");
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(ACCOUNT_INFO).headers(requestHeaders)
                .post(requestBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            String respCode = jsonObject.getString("respCode");
            if ("00000".equals(respCode)) {

            } else {
                String respDesc = jsonObject.getString("respDesc");
                APPAplication.showToast("发送失败:  " + respCode + "\n" + respDesc, 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String Bit32(String SourceString) throws Exception {
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(SourceString.getBytes());
        byte messageDigest[] = digest.digest();
        return toHexString(messageDigest);
    }

    public static String Bit16(String SourceString) throws Exception {
        return Bit32(SourceString).substring(8, 24);
    }
}

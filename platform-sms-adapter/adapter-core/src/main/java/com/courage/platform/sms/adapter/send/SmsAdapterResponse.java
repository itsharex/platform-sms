package com.courage.platform.sms.adapter.send;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 短信发送结果(适配器)
 * Created by zhangyong on 2023/3/1.
 */
public class SmsAdapterResponse {

    public static final int SUCCESS_CODE = 200;

    public static final int FAIL_CODE = 500;

    private int code;

    public SmsAdapterResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

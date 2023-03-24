package com.courage.platform.sms.admin.service;

import com.courage.platform.sms.admin.model.BaseModel;
import com.courage.platform.sms.domain.TSmsChannel;

import java.util.List;
import java.util.Map;

public interface SmsChannelService {

    List<TSmsChannel> queryChannels(Map<String, String> param);

    BaseModel addSmsChannel(TSmsChannel tSmsChannel);

    BaseModel updateSmsChannel(TSmsChannel tSmsChannel);

    BaseModel deleteSmsChannel(String id);

}

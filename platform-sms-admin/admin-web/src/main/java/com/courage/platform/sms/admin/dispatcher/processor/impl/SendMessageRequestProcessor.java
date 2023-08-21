package com.courage.platform.sms.admin.dispatcher.processor.impl;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.admin.common.config.IdGenerator;
import com.courage.platform.sms.admin.dao.TSmsRecordDAO;
import com.courage.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import com.courage.platform.sms.admin.dao.TSmsTemplateDAO;
import com.courage.platform.sms.admin.dao.domain.TSmsRecord;
import com.courage.platform.sms.admin.dao.domain.TSmsTemplate;
import com.courage.platform.sms.admin.dispatcher.SmsAdapterLoader;
import com.courage.platform.sms.admin.dispatcher.SmsAdapterSchedule;
import com.courage.platform.sms.admin.dispatcher.SmsAdatperProcessor;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorRequest;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorResponse;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorResponseCode;
import com.courage.platform.sms.admin.dispatcher.processor.body.SendMessageRequestBody;
import com.courage.platform.sms.client.SmsSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 发送短信处理器
 * Created by zhangyong on 2023/7/14.
 */
@Component
public class SendMessageRequestProcessor implements SmsAdatperProcessor<SendMessageRequestBody, SmsSenderResult> {

    private static Logger logger = LoggerFactory.getLogger(SendMessageRequestProcessor.class);

    @Autowired
    private SmsAdapterLoader smsAdapterLoader;

    @Autowired
    private TSmsTemplateBindingDAO bindingDAO;

    @Autowired
    private TSmsTemplateDAO templateDAO;

    @Autowired
    private TSmsRecordDAO smsRecordDAO;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SmsAdapterSchedule smsAdapterSchedule;

    @Override
    public ProcessorResponse<SmsSenderResult> processRequest(ProcessorRequest<SendMessageRequestBody> processorRequest) {
        SendMessageRequestBody param = processorRequest.getData();
        String templateId = param.getTemplateId();
        TSmsTemplate tSmsTemplate = templateDAO.selectByPrimaryKey(Long.valueOf(templateId));
        if (tSmsTemplate == null) {
            return ProcessorResponse.build(
                    ProcessorResponseCode.TEMPLATE_NOT_EXIST.getCode(),
                    ProcessorResponseCode.TEMPLATE_NOT_EXIST.getValue()
            );
        }
        // 插入到记录 t_sms_record
        Long smsId = idGenerator.createUniqueId(param.getAppId());
        TSmsRecord tSmsRecord = new TSmsRecord();
        tSmsRecord.setId(smsId);
        tSmsRecord.setTemplateId(Long.valueOf(param.getTemplateId()));
        tSmsRecord.setAppId(param.getAppId());
        tSmsRecord.setMobile(param.getMobile());
        tSmsRecord.setSendStatus(-1);
        tSmsRecord.setUpdateTime(new Date());
        tSmsRecord.setCreateTime(new Date());
        smsRecordDAO.insertSelective(tSmsRecord);
        // 异步执行

        SmsSenderResult smsSenderResult = new SmsSenderResult(String.valueOf(smsId));
        return ProcessorResponse.success(smsSenderResult);
    }

}

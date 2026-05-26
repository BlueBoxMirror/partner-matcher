package com.partner.partnermatch.service.impl;

import com.partner.partnermatch.entity.ApiCallLog;
import com.partner.partnermatch.mapper.ApiCallLogMapper;
import com.partner.partnermatch.service.ApiCallLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiCallLogServiceImpl implements ApiCallLogService {

    private final ApiCallLogMapper apiCallLogMapper;

    @Override
    public void saveLog(ApiCallLog log) {
        apiCallLogMapper.insert(log);
    }
}
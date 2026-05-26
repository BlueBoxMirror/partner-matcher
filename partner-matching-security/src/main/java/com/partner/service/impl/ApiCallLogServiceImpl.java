package com.partner.service.impl;

import com.partner.entity.ApiCallLog;
import com.partner.mapper.ApiCallLogMapper;
import com.partner.service.ApiCallLogService;
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
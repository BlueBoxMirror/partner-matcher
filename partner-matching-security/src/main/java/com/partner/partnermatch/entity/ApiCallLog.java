package com.partner.partnermatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("api_log")
public class ApiCallLog {
    @TableId(type = IdType.AUTO)
    private Long id;


    private Long userId;


    private String apiName;


    private String requestParams;


    private String responseResult;


    private Long costTime;


    private LocalDateTime createTime;
}
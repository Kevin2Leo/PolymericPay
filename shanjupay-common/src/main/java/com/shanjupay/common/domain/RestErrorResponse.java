package com.shanjupay.common.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description:
 * @Date Created in 0:34 2022/6/26
 * @Author: Chen_zhuo
 * @Modified By
 */
@ApiModel(value = "RestErrorResponse", description = "错误响应参数包装")
@Data
public class RestErrorResponse {

    private String errCode;
    private String errMessage;

    public RestErrorResponse(String errCode,String errMessage){
        this.errCode = errCode;
        this.errMessage= errMessage;
    }
}

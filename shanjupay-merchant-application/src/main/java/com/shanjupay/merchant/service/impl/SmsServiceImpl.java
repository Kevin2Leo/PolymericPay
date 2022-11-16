package com.shanjupay.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.merchant.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Date Created in 8:44 2022/6/24
 * @Author: Chen_zhuo
 * @Modified By
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.effectiveTime}")
    private String effectiveTime;

    @Autowired
    private RestTemplate restTemplate;
    /**
     * 获取短信验证码
     * @param phone
     * @return
     */
    public String sendMsg(String phone) {
        String url = smsUrl + "/generate?name=sms&effectiveTime=" + effectiveTime;//验证码过期时间为600秒 10分钟
        log.info("调用短信微服务发送验证码：url:{}", url);
        Map<String, Object> body = new HashMap<String, Object>();

        body.put("mobile", phone);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(body, httpHeaders);
        Map responseMap;
        try {
            ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, entity,
                    Map.class);
            log.info("调用短信微服务发送验证码: 返回值:{}", JSON.toJSONString(exchange));
            responseMap = exchange.getBody();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new RuntimeException("发送验证码出错");
        }
        if (responseMap == null || responseMap.get("result") == null) {
            throw new RuntimeException("发送验证码出错");
        }
        Map resultMap = (Map) responseMap.get("result");
        String key = resultMap.get("key").toString();
        log.info("得到发送验证码对应的key:{}",key);
        return key;
    }

    public void checkVerifiyCode(String verifiyKey,String verifiyCode) throws BusinessException {
        //实现校验验证码的逻辑
        String url = smsUrl+"/verify?name=sms&verificationCode="+verifiyCode+"&verificationKey="+verifiyKey;
        Map responseMap = null;
        try {
            //请求校验验证码
            ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, HttpEntity.EMPTY, Map.class);
            responseMap = exchange.getBody();
            log.info("校验验证码，响应内容：{}",JSON.toJSONString(responseMap));
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage(),e);
            throw new BusinessException(CommonErrorCode.E_100102);
//            throw new RuntimeException("验证码错误");
        }
        if(responseMap == null || responseMap.get("result")==null || !(Boolean) responseMap.get("result")){
            throw new BusinessException(CommonErrorCode.E_100102);
        }
    }
}

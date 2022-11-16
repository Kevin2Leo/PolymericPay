package com.shanjupay.transaction.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 支付宝接口对接测试类
 * @author Administrator
 * @version 1.0
 **/

@Slf4j
@Controller
//@RestController//请求方法响应统一json格式
public class PayTestController {

    //应用id
    String APP_ID = "2021000121612761";
    //应用私钥
    String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC4fZs2wOXiRNESlkafCF4EkR+7ty/etlZXxLUONjewhS/H7XRQdhLtJrtBznE8mBOf4G5FySB8DB1d6MhtxrsXQvnaa5sbkso19iSp0JeP3xQQtV7EevRDntYpHkivGn+JimiSRZkUd+CJhQQ5g05yNVfseIVn70rmYlkIT6Y31Nh4rO9ttLZp/TSO/MwFhBTCHhNhaBSEgQhVV9QKsoFaNveYRyRSVaUj5PywqtKyS0htl2zhzvkh1Xl2k6mFZCuurBXDuCSTdhnPAs4eY6sC3kCoV3wOd1QOT0HigR4JpY4J1mDjeZ11JHLDQ+PeIqwUfOv+hAa29nq+H+vZjVddAgMBAAECggEAC5kZ4N9PlGHvItP/rI/CGVdmP07M25tq4imjmDkgVyCIWHE9FrHVjsMYkpJ/2+dTlIFvbUOvnxzD29t+fig4KsYzHuAU6C7aN3o549MRNCI6CLLeOkvgfz/FBuC4gQmRavwghtGJRhgjLYIHOfZfQB7KYDtTZ61LcCBOqI8ZS/kh6cbdkn/pXxPeuBOFmuJZMF+IF5C5sWUI4SnEHv91y3f/5qFTU7edKjWKi8RXSXkmu8RI6xTImXzKAv1pMvC9KFcjZDDlXnP3a/93y1A65U/XNcxsWxihwr+T7FPChC2hL1wvxKrQ2rOrOspjTzi6P8KyFnzPYK5qRGrSFbi4AQKBgQD6RlD6WV+z5ij5UYHM/SckHT/m16SJcx4TlirpmOECu2udzBHe8P9MhU6GYc6BGdo5YLrSGvqquiXfwtTXS8KYDYeYf4qd8iahP8Vn5HJcd9zh36W5SpHJG/XKsby/EDKeFZdAU87RpIm0P9MT/MhUAUYlqgv/mqMaspv9BAVuwQKBgQC8tgn6dmok4HVstCUk6Qyn5nBJ7CLn5DsMOMwnCrLf3g+9TOClATd40R9c4BRMwqq1RNhWN8+akRtAPXl3PH5HAs1EZDjcqqwPrSJSK4aLVyNjZ4lc5GT+D5guX4NTDFRIICdSN4WrKU1TvEG08APCa+Km8J9uKeJaZqnN/h4rnQKBgQD1fVuY9WmmcLDoPUbw57ApAHfZ2tQN7tklpTraf5bWszhQRAeTgxko2em4bt+8Srt/ZJ5b0RLnvEfCQL29oRZzR3Il55T0cL3rlT3xNmfmw7vp4xIPulFdzws9kzHsx9qerdb6xjor8RURtTk6dc2aeNVxxGaYhfpPCbtwZy1dAQKBgFWN8lgq91RgrtvpkC17utEkGCRreifHwT15QpUwtIfqFRitkes88flEVgO1U365gstLuwjbpLNetgwZ0sfMFTP8gTNQfLcOaZNx2u2F7imeB905Q31pzp+gUk4z30X3gP5yFWGg6mj/AJ/F6cQuTIYIg2lMDfvT4Vs60CEj5GnBAoGBAO022x1zw6tr6PplKN+JJRxoqEMwd7Cl1ZJZDGOF2wOawQOC7i66izVpePx3ujEpfiusrUSy0VEhw9dIYAVRdbEt8YTohY4mvyorBlKDr1XI8uN+3cRV3sKP/p2pYc1w++3tJ+225C0e2gXEPu+AW6HCw/I1kkvUUkYrG4YeF5L0";
    //支付宝公钥
    String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn7rDZRJNR+FVPyl4wlt+4uXY8DbkU49EvbqHrTKv/YihDiH9fcGNulzucDFsSR2xmzLDeIvcPvM7iM6QWjpPaOx8tPCVEXK1I3rCfqBV/1SJ3zz+JYnb95Y6XsEohdzGn0LsjxnnFlB/kxRvdTanu15FIX9ZIgs9e7gCq98OFzjRzGKbwlpvwJDcxFyasfE4xJ3+V6KCB4Nq58sXvQdXncF9u9kQStokFmN6RPMo0ZTnYZ7qVNYyKXo3xuXS2bnDJe/y70FI5PEA6kdhFu7GEf+H6GDeTi3y1UgJefxbKUumkaZcV2/kGxBSt+dsYglZ82yd1jFjgddda4RkIGVkXQIDAQAB";
    String CHARSET = "utf-8";
    //支付宝接口的网关地址，正式"https://openapi.alipay.com/gateway.do"
    String serverUrl = "https://openapi.alipaydev.com/gateway.do";
    //签名算法类型
    String sign_type = "RSA2";

    @GetMapping("/alipaytest")
    public void alipaytest(HttpServletRequest httpRequest,
                           HttpServletResponse httpResponse) throws ServletException, IOException {
        //构造sdk的客户端对象
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, sign_type); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
//        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
//        alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\"20150420010101011\"," +
                " \"total_amount\":\"88.88\"," +
                " \"subject\":\"Iphone6 16G\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"" +
                " }");//填充业务参数
        String form="";
        try {
            //请求支付宝下单接口,发起http请求
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

}

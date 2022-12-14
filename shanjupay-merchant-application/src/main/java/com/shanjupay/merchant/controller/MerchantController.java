package com.shanjupay.merchant.controller;

import java.io.IOException;
import java.sql.BatchUpdateException;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.PhoneUtil;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.common.util.SecurityUtil;
import com.shanjupay.merchant.convert.MerchantDetailConvert;
import com.shanjupay.merchant.convert.MerchantRegisterConvert;
import com.shanjupay.merchant.service.FileService;
import com.shanjupay.merchant.service.SmsService;
import com.shanjupay.merchant.vo.MerchantDetailVO;
import com.shanjupay.merchant.vo.MerchantRegisterVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @Description:
 * @Date Created in 5:18 2022/6/24
 * @Author: Chen_zhuo
 * @Modified By
 */

@Api(value = "商户平台-商户相关", tags = "商户平台-商户相关", description = "商户平台-商户相关")
@RestController
@RequestMapping
@Slf4j
public class MerchantController {


    @org.apache.dubbo.config.annotation.Reference
    MerchantService merchantService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SmsService smsService;

    @ApiOperation("根据id查询商户")
    @GetMapping("/merchants/{id}")
    public MerchantDTO queryMerchantById(@PathVariable("id") Long id) {

        MerchantDTO merchantDTO = merchantService.queryMerchantById(id);
        return merchantDTO;
    }

    @ApiOperation("获取登录用户的商户信息")
    @GetMapping(value="/my/merchants")
    public MerchantDTO getMyMerchantInfo(){
        //从token中获取商户id
        Long merchantId = SecurityUtil.getMerchantId();
        return merchantService.queryMerchantById(merchantId);
    }

    @ApiOperation("获取手机验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query")
    @GetMapping("/sms")
    public String getSMSCode(@RequestParam("phone") String phone) {
        log.info("向手机号:{}发送验证码", phone);
        return smsService.sendMsg(phone);
    }

    @ApiOperation("注册商户")
    @ApiImplicitParam(name = "merchantRegister",
                      value = "注册信息",
                      required = true,
                      dataType = "MerchantRegisterVO",
                      paramType = "body")
    @PostMapping("/merchants/register")
    public MerchantRegisterVO registerMerchant(@RequestBody MerchantRegisterVO merchantRegister) {

        // 1.校验
        if (merchantRegister == null) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        //手机号非空校验
        if (StringUtils.isBlank(merchantRegister.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        //校验手机号的合法性
        if (!PhoneUtil.isMatches(merchantRegister.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        //联系人非空校验
        if (StringUtils.isBlank(merchantRegister.getUsername())) {
            throw new BusinessException(CommonErrorCode.E_100110);
        }
        //密码非空校验
        if (StringUtils.isBlank(merchantRegister.getPassword())) {
            throw new BusinessException(CommonErrorCode.E_100111);
        }
        //验证码非空校验
        if (StringUtils.isBlank(merchantRegister.getVerifiyCode()) ||
                StringUtils.isBlank(merchantRegister.getVerifiykey())) {
            throw new BusinessException(CommonErrorCode.E_100103);
        }

        //校验验证码
        smsService.checkVerifiyCode(merchantRegister.getVerifiykey(),
                merchantRegister.getVerifiyCode());
        //注册商户
        MerchantDTO merchantDTO = MerchantRegisterConvert.INSTANCE.vo2dto(merchantRegister);
        merchantService.createMerchant(merchantDTO);
        return merchantRegister;
    }


    @Autowired
    private FileService fileService;


    @ApiOperation("证件上传")
    @PostMapping("/upload")
    public String upload(@ApiParam(value = "上传的文件", required = true) @RequestParam("file") MultipartFile file) throws IOException, BatchUpdateException {
        //调用fileService
        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") - 1);
        //文件名称
        String fileName = UUID.randomUUID().toString() + suffix;
        //上传文件，返回文件下载url
        String fileurl = fileService.upload(file.getBytes(), fileName);
        return fileurl;
    }


    @ApiOperation("商户资质申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantInfo",
                    value = "商户认证资料",
                    required = true,
                    dataType = "MerchantDetailVO",
                    paramType = "body")
    })
    @PostMapping("/my/merchants/save")
    public void saveMerchant(@RequestBody MerchantDetailVO merchantInfo) {
        //解析token，取出当前登录商户的id
        Long merchantId = SecurityUtil.getMerchantId();
        MerchantDTO merchantDTO = MerchantDetailConvert.INSTANCE.vo2dto(merchantInfo);
        //资质申请
        merchantService.applyMerchant(merchantId,merchantDTO);

    }
}

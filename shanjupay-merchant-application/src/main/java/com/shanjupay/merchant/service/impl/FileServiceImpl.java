package com.shanjupay.merchant.service.impl;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.QiniuUtils;
import com.shanjupay.merchant.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.BatchUpdateException;

/**
 * @Description:
 * @Date Created in 18:10 2022/6/26
 * @Author: Chen_zhuo
 * @Modified By
 */
@Service
public class FileServiceImpl implements FileService {

    @Value("${oss.qiniu.url}")
    private String qiniuUrl;

    @Value("${oss.qiniu.accessKey}")
    private String accessKey;

    @Value("${oss.qiniu.secretKey}")
    private String secretKey;

    @Value("${oss.qiniu.bucket}")
    private String bucket;

    /**
     * 上传文件
     * @param bytes 文件字节
     * @param fileName 文件名称
     * @return 文件下载路径
     * @throws BusinessException
     */
    @Override
    public String upload(byte[] bytes, String fileName) throws BusinessException {

        //调用common下的工具类
        try {
            QiniuUtils.upload2Qiniu(accessKey, secretKey, bucket, bytes, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100106);
        }
        //返回文件名称
        return qiniuUrl + fileName;
    }
}

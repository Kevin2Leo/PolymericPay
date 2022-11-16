package com.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;

import java.sql.BatchUpdateException;

/**
 * @Description:
 * @Date Created in 18:09 2022/6/26
 * @Author: Chen_zhuo
 * @Modified By
 */
public interface FileService {

    /**
     * 上传文件
     * @param bytes 文件字节
     * @param fileName 文件名称
     * @return 文件下载路径
     * @throws BusinessException
     */
    String upload(byte[] bytes,String fileName) throws BusinessException;
}

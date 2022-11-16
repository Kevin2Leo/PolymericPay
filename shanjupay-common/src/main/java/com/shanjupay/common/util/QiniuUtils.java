package com.shanjupay.common.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description:
 * @Date Created in 17:45 2022/6/26
 * @Author: Chen_zhuo
 * @Modified By
 */
public class QiniuUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuUtils.class);

    //工具方法，上传文件
    public static void upload2Qiniu(String accessKey, String secretKey, String bucket, byte[] bytes, String fileName){

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名，这里建议由自己来控制文件名
        String key = fileName;
        //通常这里得到文件的字节数组
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            LOGGER.error(r.toString());
            try {
                LOGGER.error(r.bodyString());
            } catch (QiniuException e) {
                e.printStackTrace();
            }
            throw new RuntimeException(r.toString());
        }
    }

    //上传测试
    private static void testUpload(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "iRYfoY0YglAfBFG2jAz9RgsA3VnkgaG7ztjgW19k";
        String secretKey = "-R_skPGs3AKduyc1GXJRxWAWtRKSfkUcq0e28NgY";
        String bucket = "shanjupayy";
        //默认不指定key的情况下，以文件内容的hash值作为文件名，这里建议由自己来控制文件名
        String key = UUID.randomUUID()+".png";
        FileInputStream fileInputStream = null;
        try {
            //通常这里得到文件的字节数组
            fileInputStream = new FileInputStream(new File("C:\\Users\\Chen_Zhuo\\Pictures\\1.png"));
            byte[] uploadBytes = IOUtils.toByteArray(fileInputStream);
            // byte[] uploadBytes = "hello qiniu cloud".getBytes("utf‐8");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (IOException ex) {
            //ignore
        }finally {
            try {
                if(fileInputStream!=null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //测试上传
        QiniuUtils.testUpload();
    }
}

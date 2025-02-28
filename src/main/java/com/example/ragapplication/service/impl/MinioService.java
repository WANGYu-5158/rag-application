package com.example.ragapplication.service.impl;

import io.minio.*;
import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
/**
 * @author wangyu
 * @date 2025/2/10 18:50
 */
@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    // 上传文件到 MinIO
    public String uploadFile(MultipartFile file){
        try {
            String objectName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            // 构建 PutObjectArgs
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)       // 设置桶名称
                    .object(objectName)       // 设置文件名称
                    .stream(inputStream, file.getSize(), -1)  // 设置文件流和大小
                    .contentType(file.getContentType())  // 设置文件的 content-type
                    .build();

            // 调用 putObject 方法
            ObjectWriteResponse response = minioClient.putObject(args);

            return response.object();  // 返回上传的文件名
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            // 处理异常并返回错误信息
            throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage(), e);
        }
    }

    // 从 MinIO 下载文件
    public InputStream downloadFile(String objectName) {
        try {
            // 构建 GetObjectArgs
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucketName)   // 设置桶名称
                    .object(objectName)   // 设置对象名称
                    .build();

            // 调用 getObject 方法
            InputStream inputStream = minioClient.getObject(args);

            // 返回文件的输入流
            return inputStream;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            // 处理异常并返回错误信息
            throw new RuntimeException("Error downloading file from MinIO: " + e.getMessage(), e);
        }
    }

    //从MinIO删除文件
    public void deleteFile(String filename){
        try {
            // 删除对象
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)       // 设置桶名称
                    .object(filename)       // 设置文件名称
                    .build());

        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from MinIO: " + e.getMessage(), e);
        }
    }
}

package com.example.ragapplication.controller;

import com.example.ragapplication.pojo.FileData;
import com.example.ragapplication.pojo.Page;
import com.example.ragapplication.service.FileService;
import com.example.ragapplication.service.impl.MinioService;
import io.minio.errors.MinioException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author wangyu
 * @date 2024/11/4 21:10
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private MinioService minioService;

    @GetMapping("/list")
    public ResponseEntity<Page<FileData>> getFiles(
            @RequestParam String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FileData> filePage = fileService.getFilesById(id, page, size);
        return ResponseEntity.ok(filePage);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("dbId") int dbId) {
        ResponseEntity<String> response = fileService.handleFileUpload(file, dbId);
        return response;
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam int dbId,
                                             @RequestParam("fileId") int fileId,
                                             @RequestParam("filename") String filename) {
        ResponseEntity<String> response = fileService.deleteFile(dbId, fileId, filename);
        return response;
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam String objectName, HttpServletResponse response) {
        try {
            // 获取文件流
            InputStream fileStream = minioService.downloadFile(objectName);

            // 设置响应头，指示文件下载
            response.setHeader("Content-Disposition", "attachment;filename=" + objectName);

            // 设置文件类型，这里使用默认的 "application/octet-stream"
            response.setContentType("application/octet-stream");

            // 设置字符编码（处理中文文件名）
            response.setCharacterEncoding("UTF-8");

            // 将文件流写入响应输出流
            IOUtils.copy(fileStream, response.getOutputStream());
        } catch (Exception e) {
            // 捕获异常并处理
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
}


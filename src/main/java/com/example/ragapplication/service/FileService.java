package com.example.ragapplication.service;

import com.example.ragapplication.pojo.FileData;
import com.example.ragapplication.pojo.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wangyu
 * @date 2024/11/4 21:16
 */
public interface FileService {
    Page<FileData> getFilesById(String id, int page, int size);

    ResponseEntity<String> handleFileUpload(MultipartFile file,int dbId);

    ResponseEntity<String> deleteFile(int dbId, int fileId);
}

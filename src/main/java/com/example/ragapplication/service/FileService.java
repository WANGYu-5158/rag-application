package com.example.ragapplication.service;

import com.example.ragapplication.pojo.FileData;
import com.example.ragapplication.pojo.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author wangyu
 * @date 2024/11/4 21:16
 */
public interface FileService {
    Page<FileData> getFilesById(String id, int page, int size);

    ResponseEntity<String> handleFileUpload(MultipartFile file,int dbId);

    ResponseEntity<String> deleteFile(int dbId, int fileId, String filename);

    ResponseEntity<List<String>> uploadMultipleFiles(List<MultipartFile> files, int dbId);

    ResponseEntity<String> deleteBatchFiles(List<Integer> ids, Integer dbId, List<String> filenames);
}

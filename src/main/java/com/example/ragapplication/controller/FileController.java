package com.example.ragapplication.controller;

import com.example.ragapplication.pojo.FileData;
import com.example.ragapplication.pojo.Page;
import com.example.ragapplication.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wangyu
 * @date 2024/11/4 21:10
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService; // 注入服务层

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
    public ResponseEntity<String> deleteFile(@RequestParam("dbId") int dbId,
                                             @RequestParam("fileId") int fileId) {
        ResponseEntity<String> response = fileService.deleteFile(dbId, fileId);
        return response;
    }
}


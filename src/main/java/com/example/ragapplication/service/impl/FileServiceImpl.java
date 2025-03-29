package com.example.ragapplication.service.impl;

import com.example.ragapplication.mapper.FileMapper;
import com.example.ragapplication.mapper.KnowledgedbMapper;
import com.example.ragapplication.pojo.FileData;
import com.example.ragapplication.pojo.Page;
import com.example.ragapplication.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author wangyu
 * @date 2024/11/4 21:21
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper; // 注入 MyBatis Mapper
    @Autowired
    private KnowledgedbMapper knowledgedbMapper;
    @Autowired
    private MinioService minioService;

    @Override
    public Page<FileData> getFilesById(String id, int page, int size) {
        int offset = page * size;
        List<FileData> files = fileMapper.selectFilesById(id, offset, size);
        int totalElements = fileMapper.countFilesById(id);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new Page<>(files, totalElements, totalPages, page+1);
    }

    @Override
    public ResponseEntity<String> handleFileUpload(MultipartFile file, int dbId) {
        try {
            FileData fileData = new FileData();
            String fileName = file.getOriginalFilename();
            fileData.setFilename(fileName);
            fileData.setDbId(dbId);

            //在file数据库中增加文件记录，并在fileData获取id
            fileMapper.insertFile(fileData);
            //更新file数据表之后在knowledgedb数据库中增加文件数量，并更新数据库的更新时间
            knowledgedbMapper.addDbFileNum(dbId);

            // 创建 RestTemplate 实例
            RestTemplate restTemplate = new RestTemplate();
            String targetUrl = "http://localhost:8000/upload"; // 后续替换为 RAG 系统的实际 URL

            // 构建请求体
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            // 将 MultipartFile 转换为 ByteArrayResource 以便上传
            body.add("files", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
            // 在body中增加fileid和dbid以便后续查找以及删除文件(需要修改 rag api)
            body.add("file_id", fileData.getId());
            body.add("db_id", dbId);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 创建请求实体
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 发送 POST 请求
            ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, requestEntity, String.class);

            // 将文件上传到 MinIO
            String uploadedFileName = minioService.uploadFile(file);  // 使用之前配置的 MinIO 服务类

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to process the file：" + e.getMessage());
        }

        return ResponseEntity.ok("File uploaded successfully");
    }

    @Override
    public ResponseEntity<String> deleteFile(int dbId, int fileId, String filename) {
        try {
            // 创建 RestTemplate 实例
            RestTemplate restTemplate = new RestTemplate();
            String targetUrl = "http://localhost:8000/delete";

            // 创建请求体
            Map<String, Object> body = new HashMap<>();
            body.put("db_id", String.valueOf(dbId));  // 将 dbId 转换为字符串
            body.put("file_id", String.valueOf(fileId));  // 将 fileId 转换为字符

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // 使用json提交

            // 创建请求实体
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 发送 POST 请求
            ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, requestEntity, String.class);

            // 在MinIO中删除文件
            minioService.deleteFile(filename);
            // 调用 Mapper 方法删除文件
            fileMapper.deleteFileById(fileId);
            // 更新knowledgedb数据库中的file_num信息，并更新数据库的更新时间
            knowledgedbMapper.reduceDbFileNum(dbId);

            return ResponseEntity.ok("File deleted successfully！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File deletion failed：" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<String>> uploadMultipleFiles(List<MultipartFile> files, int dbId){
        List<String> responseMessages = new ArrayList<>();
        List<FileData> fileDataList = new ArrayList<>();
        List<Integer> fileIds = new ArrayList<>();

        try {
            // 批量插入数据库
            for (MultipartFile file : files) {
                FileData fileData = new FileData();
                fileData.setFilename(file.getOriginalFilename());
                fileData.setDbId(dbId);
                fileDataList.add(fileData);
            }
            fileMapper.insertBatchFiles(fileDataList); // 在数据库中批量插入
            fileIds = fileDataList.stream().map(FileData::getId).collect(Collectors.toList());
            System.out.println("fileIds: " + fileIds);
            System.out.println("fileIds.size(): " + fileIds.size());
            try {
                knowledgedbMapper.addDbMultiFileNum(dbId, fileIds.size()); // 更新knowledgedb数据库中的file_num信息，并更新数据库的更新时间
            } catch (Exception e) {
                // 打印详细的错误日志
                System.out.println("Error during DB operation: " + e.getMessage());
                throw e; // 重新抛出异常，或者根据情况处理
            }

            final List<Integer> finalFileIds = new ArrayList<>(fileIds); // 复制一份(final)
            // 批量上传文件到MinIO(多线程并行)
            Map<Integer, String> uploadedFiles = new ConcurrentHashMap<>();
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            for (int i = 0; i < files.size(); i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        String fileName = minioService.uploadFile(files.get(index));
                        uploadedFiles.put(finalFileIds.get(index), fileName);
                        log.info("Successfully uploaded: {} -> {}", finalFileIds.get(index), fileName);
                    } catch (Exception e) {
                        log.error("Error uploading file to MinIO: " + files.get(index).getOriginalFilename(), e);
                        e.printStackTrace();
                    }
                });
            }
            executorService.shutdown();
            boolean finished = executorService.awaitTermination(10, TimeUnit.MINUTES);
            if (!finished) {
                log.error("MinIO file upload executor did not finish in time!");
            }

            // 逐个上传 RAG
            RestTemplate restTemplate = new RestTemplate();
            String targetUrl = "http://localhost:8000/upload";
            for (int i = 0; i < files.size(); i++) {
                Integer fileId = fileIds.get(i);
                MultipartFile file = files.get(i);

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("files", new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                });
                body.add("file_id", fileId);
                body.add("db_id", dbId);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, requestEntity, String.class);
                responseMessages.add("File: " + files.get(i).getOriginalFilename() + " uploaded successfully.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Failed to process files: " + e.getMessage()));
        }

        //这里把文件名返回回去，但其实前端并没有使用，后续看是否需要使用
        return ResponseEntity.ok(responseMessages);
    }
}

package com.example.ragapplication.service.impl;

import com.example.ragapplication.mapper.FileMapper;
import com.example.ragapplication.mapper.KnowledgedbMapper;
import com.example.ragapplication.pojo.FileData;
import com.example.ragapplication.pojo.Page;
import com.example.ragapplication.service.FileService;
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

import java.util.List;

/**
 * @author wangyu
 * @date 2024/11/4 21:21
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper; // 注入 MyBatis Mapper
    @Autowired
    private KnowledgedbMapper knowledgedbMapper;

    @Override
    public Page<FileData> getFilesById(String id, int page, int size) {
        int offset = page * size;
        List<FileData> files = fileMapper.selectFilesById(id, offset, size);
        int totalElements = fileMapper.countFilesById(id);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new Page<>(files, totalElements, totalPages, page);
    }

    @Override
    public ResponseEntity<String> handleFileUpload(MultipartFile file, int dbId) {
        try {
            FileData fileData = new FileData();
            String fileName = file.getOriginalFilename();
            byte[] fileContent = file.getBytes(); // 文件内容以字节数组形式获取
            fileData.setFilename(fileName);
            fileData.setDbId(dbId);
            //在file数据库中增加文件记录，并在fileData获取id
            fileMapper.insertFile(fileData);
            //更新file数据表之后在knowledgedb数据库中增加文件数量
            knowledgedbMapper.addDbFileNum(dbId);
            //验证是否获取到了文件的id
            //System.out.println("Received file: " + fileName + " with ID: " + fileData.getId());

            // 创建 RestTemplate 实例
            RestTemplate restTemplate = new RestTemplate();
            String targetUrl = "http://0.0.0.0:8000/upload"; // 后续替换为 RAG 系统的实际 URL

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
            //body.add("file_id", fileData.getId());
            //body.add("db_id", dbId);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 创建请求实体
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 发送 POST 请求
            ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, requestEntity, String.class);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("文件处理失败：" + e.getMessage());
        }

        return ResponseEntity.ok("文件上传成功！");
    }

    @Override
    public ResponseEntity<String> deleteFile(int dbId, int fileId) {
        try {
            // 调用 Mapper 方法删除文件
            fileMapper.deleteFileById(fileId);
            // 更新knowledgedb数据库中的file_num信息
            knowledgedbMapper.reduceDbFileNum(dbId);
            return ResponseEntity.ok("文件删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("文件删除失败：" + e.getMessage());
        }
    }
}

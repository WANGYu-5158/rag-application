package com.example.ragapplication.service.impl;

import com.example.ragapplication.mapper.FileMapper;
import com.example.ragapplication.pojo.FileData;
import com.example.ragapplication.pojo.Page;
import com.example.ragapplication.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangyu
 * @date 2024/11/4 21:21
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper; // 注入 MyBatis Mapper

    @Override
    public Page<FileData> getFilesById(String id, int page, int size) {
        int offset = page * size;
        List<FileData> files = fileMapper.selectFilesById(id, offset, size);
        int totalElements = fileMapper.countFilesById(id);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new Page<>(files, totalElements, totalPages, page);
    }
}

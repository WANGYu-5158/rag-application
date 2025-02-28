package com.example.ragapplication.service.impl;

import com.example.ragapplication.mapper.FileMapper;
import com.example.ragapplication.mapper.KnowledgedbMapper;
import com.example.ragapplication.pojo.Knowledgedb;
import com.example.ragapplication.service.KdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangyu
 * @date 2024/10/28 17:08
 */
@Service
public class KdbServiceImpl implements KdbService {

    @Autowired
    KnowledgedbMapper knowledgedbMapper;
    @Autowired
    FileMapper fileMapper;

    @Override
    public List<Knowledgedb> kdbList() {
        return knowledgedbMapper.queryAll();
    }

    @Override
    public List<Knowledgedb> queryByName(String dbname) {
        return knowledgedbMapper.queryByName(dbname);
    }

    @Override
    public ResponseEntity<String> addDb(String dbname) {
        // 空值检查
        if (dbname == null || dbname.trim().isEmpty()) {
            return new ResponseEntity<>("Knowledge base name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        // 唯一性检查：查询是否已存在该数据库名称
        if (knowledgedbMapper.existsByName(dbname)) {
            return new ResponseEntity<>("The Knowledge base name already exists", HttpStatus.BAD_REQUEST);
        }

        // 如果通过了所有检查，则调用Mapper添加数据库
        int result = knowledgedbMapper.addDb(dbname);

        // 检查数据库是否添加成功
        if (result > 0) {
            return new ResponseEntity<>("数据库创建成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("数据库创建失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteDb(int dbId) {
        //删除文件表中属于本知识库的文件
        fileMapper.deleteFilesByDbId(dbId);
        //删除知识库中记录
        knowledgedbMapper.deleteDbById(dbId);
        return new ResponseEntity<>("数据库删除成功", HttpStatus.OK);
    }
}

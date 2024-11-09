package com.example.ragapplication.service.impl;

import com.example.ragapplication.mapper.KnowledgedbMapper;
import com.example.ragapplication.pojo.Knowledgedb;
import com.example.ragapplication.service.KdbService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<Knowledgedb> kdbList() {
        return knowledgedbMapper.queryAll();
    }

    @Override
    public List<Knowledgedb> queryByName(String dbname) {
        return knowledgedbMapper.queryByName(dbname);
    }

    @Override
    public int addDb(String dbname) {
        return knowledgedbMapper.addDb(dbname);
    }
}

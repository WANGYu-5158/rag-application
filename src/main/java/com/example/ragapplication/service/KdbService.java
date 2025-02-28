package com.example.ragapplication.service;

import com.example.ragapplication.pojo.Knowledgedb;

import java.util.List;

/**
 * @author wangyu
 * @date 2024/10/28 17:08
 */
public interface KdbService {

    List<Knowledgedb> kdbList();

    List<Knowledgedb> queryByName(String name);

    int addDb(String dbname);
}

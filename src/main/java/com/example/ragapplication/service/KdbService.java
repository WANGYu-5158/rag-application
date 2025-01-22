package com.example.ragapplication.service;

import com.example.ragapplication.pojo.Knowledgedb;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author wangyu
 * @date 2024/10/28 17:08
 */
public interface KdbService {

    List<Knowledgedb> kdbList();

    List<Knowledgedb> queryByName(String name);

    ResponseEntity<String> addDb(String dbname);

    ResponseEntity<String> deleteDb(int dbId);
}

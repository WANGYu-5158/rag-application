package com.example.ragapplication.mapper;

import com.example.ragapplication.pojo.Knowledgedb;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
*   @author wangyu
*   @date 2024/10/28 16:52
*/
@Mapper
public interface KnowledgedbMapper {

    /**
     * 查询全部数据库信息
     * @return
     */
    List<Knowledgedb> queryAll();
}

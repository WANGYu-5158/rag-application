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

    /**
     * 根据数据库名模糊查询数据库信息
     * @param dbname
     * @return
     */
    List<Knowledgedb> queryByName(String dbname);

    /**
     * 新增数据库信息
     * @param dbname
     * @return
     */
    int addDb(String dbname);

    /**
     * 更新数据库文件数量信息并更新知识库更新时间
     * @param dbId
     * @return
     */
    int addDbFileNum(int dbId);

    /**
     * 减少数据库中文件数量信息并更新知识库更新时间
     * @param dbId
     * @return
     */
    int reduceDbFileNum(int dbId);
}

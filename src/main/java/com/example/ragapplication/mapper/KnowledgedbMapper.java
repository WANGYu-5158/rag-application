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
    List<Knowledgedb> queryAllByUserId(Long userId);

    /**
     * 根据数据库名模糊查询数据库信息
     * @param dbname
     * @return
     */
    List<Knowledgedb> queryByName(String dbname, Long userId);

    /**
     * 新增数据库信息
     * @param dbname
     * @return
     */
    int addDb(String dbname, Long userId);

    /**
     * 更新数据库文件数量信息并更新知识库更新时间(单个文件)
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

    /**
     * 根据知识库名查找是否已经存在该名称知识库
     * @param dbname 知识库名
     * @return true/false
     */
    boolean existsByName(String dbname, Long userId);

    /**
     * 根据知识库id删除知识库
     * @param dbId 知识库id
     * @return int
     */
    int deleteDbById(int dbId);

    /**
     * 更新数据库文件数量信息并更新知识库更新时间(批量增加文件)
     * @param dbId
     * @return
     */
    int addDbMultiFileNum(int dbId, int fileNum);

    /**
     * 减少数据库中文件数量信息并更新知识库更新时间(批量减少文件)
     * @param dbId
     * @param fileNum
     * @return
     */
    int reduceDbMultiFileNum(int dbId, int fileNum);
}

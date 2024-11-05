package com.example.ragapplication.mapper;

import com.example.ragapplication.pojo.FileData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyu
 * @date 2024/11/4 21:23
 */
@Mapper
public interface FileMapper {

    List<FileData> selectFilesById(@Param("id") String id, @Param("offset") int offset, @Param("limit") int limit);

    int countFilesById(@Param("id") String id);
}

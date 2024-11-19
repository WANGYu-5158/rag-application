package com.example.ragapplication.mapper;

import com.example.ragapplication.pojo.Workspace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: WorkspaceMapper
 * Package: com.example.ragapplication.mapper
 * Description:
 *
 * @Author 刘翼
 * @Create 2024/10/29 22:37
 * @Vertion 1.0
 */
@Mapper
@Component
public interface WorkspaceMapper {

    List<Workspace> showAll();

    void save(Workspace workspace);

    void deleteById(Integer id);

    Workspace selectById(Integer id);
}

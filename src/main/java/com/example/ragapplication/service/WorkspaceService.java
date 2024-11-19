package com.example.ragapplication.service;

import com.example.ragapplication.pojo.UpdateWorkspaceDTO;
import com.example.ragapplication.pojo.Workspace;
import com.example.ragapplication.pojo.WorkspaceDTO;

import java.util.List;

/**
 * ClassName: WorkspaceService
 * Package: com.example.ragapplication.service
 * Description:
 *
 * @Author 刘翼
 * @Create 2024/10/29 16:12
 * @Vertion 1.0
 */

public interface WorkspaceService {

    List<Workspace> showAll();


    void addWorkspace(WorkspaceDTO dto);

    void deleteById(Integer id);

    void updateById(UpdateWorkspaceDTO dto);
}

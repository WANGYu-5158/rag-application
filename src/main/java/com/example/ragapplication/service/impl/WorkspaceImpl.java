package com.example.ragapplication.service.impl;

import com.example.ragapplication.mapper.WorkspaceMapper;
import com.example.ragapplication.pojo.UpdateWorkspaceDTO;
import com.example.ragapplication.pojo.Workspace;
import com.example.ragapplication.pojo.WorkspaceDTO;
import com.example.ragapplication.service.WorkspaceService;
import com.example.ragapplication.utils.LoginUserContext;
import com.example.ragapplication.utils.SnowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * ClassName: WorkspaceImpl
 * Package: com.example.ragapplication.service.impl
 * Description:
 *
 * @Author 刘翼
 * @Create 2024/10/29 16:13
 * @Vertion 1.0
 */
@Service
@Transactional
@Slf4j
public class WorkspaceImpl implements WorkspaceService {
    @Autowired
    private WorkspaceMapper workspaceMapper;

    @Override
    public List<Workspace> showAll() {
        Long userId = LoginUserContext.getId();
        log.info("userId:" + userId);
        return workspaceMapper.showAll(userId);
    }

    @Override
    public void addWorkspace(WorkspaceDTO dto) {
        if(dto != null) {
            Long userId = LoginUserContext.getId();
            Workspace workspace = new Workspace();
            workspace.setId(SnowUtil.getSnowflakeNextId());
            workspace.setUserId(userId);
            workspace.setName(dto.getName());
            workspace.setSelectedDatabase(dto.getSelectedDatabase());
            workspace.setCreateTime(new Date());
            workspace.setUpdateTime(new Date());
            workspaceMapper.save(workspace);
        }
    }

    @Override
    public void deleteById(Integer id) {
        if(id != null) {
            workspaceMapper.deleteById(id);
        }
    }

    @Override
    public void updateById(UpdateWorkspaceDTO dto) {
        if(dto != null) {
            Integer id = dto.getId();
            Workspace dbWorkspace = workspaceMapper.selectById(id);
            if(dbWorkspace != null) {
                Workspace workspace = new Workspace();
                Long userId = LoginUserContext.getId();
                workspace.setId(SnowUtil.getSnowflakeNextId());
                workspace.setUserId(userId);
                workspace.setName(dto.getName());
                workspace.setSelectedDatabase(dto.getSelectedDatabase());
                workspace.setCreateTime(dbWorkspace.getCreateTime());
                workspace.setUpdateTime(new Date());
                workspaceMapper.deleteById(id);
                workspaceMapper.save(workspace);
            }

        }
    }
}

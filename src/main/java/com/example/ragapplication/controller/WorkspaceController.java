package com.example.ragapplication.controller;


import com.example.ragapplication.pojo.UpdateWorkspaceDTO;
import com.example.ragapplication.pojo.Workspace;
import com.example.ragapplication.pojo.WorkspaceDTO;
import com.example.ragapplication.service.WorkspaceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: WorkSpaceController
 * Package: com.example.ragapplication.controller
 * Description:
 *
 * @Author 刘翼
 * @Create 2024/10/29 16:06
 * @Vertion 1.0
 */
@RestController
@RequestMapping("/workspace")
public class WorkspaceController {
    @Resource
    private WorkspaceService workspaceService;

    @GetMapping("/list")
    public List<Workspace> showAll(){
        return workspaceService.showAll();
//        return Result.success(workspaces);
    }

    @PostMapping("/add")
    public void addWorkspace(@RequestBody WorkspaceDTO dto) {
        workspaceService.addWorkspace(dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Integer id) {
        workspaceService.deleteById(id);
    }

    @PostMapping("/update")
    public void updateById(@RequestBody UpdateWorkspaceDTO dto) {
        workspaceService.updateById(dto);
    }
}

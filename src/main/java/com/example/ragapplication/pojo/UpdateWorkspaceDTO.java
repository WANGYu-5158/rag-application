package com.example.ragapplication.pojo;

import lombok.Data;

/**
 * ClassName: UpdateWorkspaceDTO
 * Package: com.example.ragapplication.dto
 * Description:
 *
 * @Author 刘翼
 * @Create 2024/11/12 21:47
 * @Vertion 1.0
 */
@Data
public class UpdateWorkspaceDTO {
    private Integer id;
    private String name;
    private String selectedDatabase;
}

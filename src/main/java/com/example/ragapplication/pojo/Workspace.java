package com.example.ragapplication.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * ClassName: WorkSpace
 * Package: com.example.ragapplication.pojo
 * Description:
 *
 * @Author 刘翼
 * @Create 2024/10/29 16:10
 * @Vertion 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Workspace {
    private Integer id;
    private String name;
    private Date createTime;
    private Date updateTime;
    private String selectedDatabase;
}

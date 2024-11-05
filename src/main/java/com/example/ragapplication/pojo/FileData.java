package com.example.ragapplication.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wangyu
 * @date 2024/11/4 21:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileData {
    private Integer id;
    private String filename;
    private Integer dbId;
    private Date uploadTime;
}

package com.example.ragapplication.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *   @author wangyu
 *   @date 2024/10/28 17:19
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Knowledgedb {

    private Integer id;
    private String name;
    private Date createTime;
    private Date updateTime;
    private Integer fileNum;
}

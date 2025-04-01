package com.example.ragapplication.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author wangyu
 * @date 2025/3/30 01:22
 */
@Data
public class DeleteBatchRequest {
    private List<Integer> ids;
    private Integer dbId;
    private List<String> filenames;
}

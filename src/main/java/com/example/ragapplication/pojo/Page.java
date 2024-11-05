package com.example.ragapplication.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author wangyu
 * @date 2024/11/4 21:18
 */
@Data
public class Page<T> {
    private List<T> content;
    private int totalElements;
    private int totalPages;
    private int number;

    public Page(List<T> content, int totalElements, int totalPages, int number) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
    }

}


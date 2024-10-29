package com.example.ragapplication.controller;

import com.example.ragapplication.pojo.Knowledgedb;
import com.example.ragapplication.service.KdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *   @author wangyu
 *   @date 2024/10/28 17:19
 */

@RestController
@RequestMapping("/kdb")
public class KdbController {

    @Autowired
    private KdbService kdbService;

    @GetMapping("/list")
    public List<Knowledgedb> queryAll(){
        return kdbService.kdbList();
    }
}

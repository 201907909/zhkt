package com.wisdom.interaction.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 问答业务模块
 * @author WisdomCR
 * @since  2022/10/30
 */
@RestController
@RequestMapping("interlocution")
public class InterlocutionController {

    @GetMapping("/test")
    public String test() {
        return "问答管理模块";
    }

}

package com.codeqiu.apiinterface.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.codeqiu.apiinterface.domain.LoverPrattle;
import com.codeqiu.apiinterface.mapper.LoverPrattleMapper;
import com.codeqiu.apiclientsdk.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟接口调用
 */
@RestController
@RequestMapping
public class NameController {
    @Resource
    private LoverPrattleMapper loverPrattleMapper;

    @GetMapping("/name/get")
    public String getNameByGet(String name) {
        return "Get 你的名字" + name;
    }

    @PostMapping("/name/post")
    public String getNameByPost(@RequestParam String name) {
        return "Post 你的名字" + name;
    }

    @PostMapping("/name/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest servletRequest) {
        return "Post 用户名字的名字" + user.getUsername();
    }
    @PostMapping("/getLoverPrattle")
    public String getLoverPrattle(HttpServletRequest servletRequest) {
        String s = RandomUtil.randomNumbers(1);
        LoverPrattle loverPrattle = loverPrattleMapper.selectById(s);
        return loverPrattle.getLoverName();
    }

    @GetMapping("/dygirl")
    public String getdyGirlGet(){
        Map<String,String> map= new HashMap<>();
        map.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44");
        HttpResponse httpResponse = HttpRequest.get("https://zj.v.api.aa1.cn/api/video_dyv2")
                .addHeaders(map)
                .execute();
        String location = httpResponse.header("Location");
        HttpResponse httpResponse1 = HttpRequest.get(location).execute();
        return httpResponse1.body();
    }

    @GetMapping("/wyrp")
    public String getwyrp(){
        Map<String,String> map= new HashMap<>();
        map.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44");
        HttpResponse httpResponse = HttpRequest.get("https://v.api.aa1.cn/api/api-wenan-wangyiyunreping/index.php?aa1=json")
                .addHeaders(map)
                .execute();
        return httpResponse.body();
    }


}

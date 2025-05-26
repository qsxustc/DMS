package com.dms.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dms.springboot.common.Constants;
import com.dms.springboot.common.Result;
import com.dms.springboot.config.AuthAccess;
import com.dms.springboot.entity.User;
import com.dms.springboot.entity.Files;
import com.dms.springboot.mapper.FileMapper;
import com.dms.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @Resource
    private FileMapper fileMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/example")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }


    @AuthAccess
    @GetMapping("/file/front/all")
//    @Cacheable(value = "files" ,key = "'frontAll'")
    public Result frontAll() {
        // 1. 从缓存获取数据
        String jsonStr = stringRedisTemplate.opsForValue().get(Constants.FILES_KEY);
        List<Files> files;
        if (StrUtil.isBlank(jsonStr)) {  // 2. 取出来的json是空的
            files = fileMapper.selectList(null);  // 3. 从数据库取出数据
            // 4. 再去缓存到redis
            stringRedisTemplate.opsForValue().set(Constants.FILES_KEY, JSONUtil.toJsonStr(files));
        } else {
            // 减轻数据库的压力
            // 5. 如果有, 从redis缓存中获取数据
            files = JSONUtil.toBean(jsonStr, new TypeReference<List<Files>>() {
            }, true);
        }
        return Result.success(files);
    }

}

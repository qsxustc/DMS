package com.dms.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.common.Constants;
import com.dms.springboot.common.Result;
import com.dms.springboot.config.AuthAccess;
import com.dms.springboot.controller.dto.UserDTO;

import com.dms.springboot.mapper.FileMapper;
import com.dms.springboot.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.dms.springboot.service.IUserService;
import com.dms.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.dms.springboot.service.IVideoService;
import com.dms.springboot.entity.Video;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 本表是为了存储视频信息 前端控制器
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Resource
    private IVideoService videoService;

    @Resource
    private VideoMapper videoMapper;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Video video) {
        videoService.saveOrUpdate(video);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        videoService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        videoService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(videoService.list());
    }




    @Value("/Users/qsx/Desktop/DMS/src/main/resources/videos/")
    private String fileUploadPath;

    @Value("${server.ip}")
    private String serverIp;


    /**
     * 文件上传接口
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @AuthAccess
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Video dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://" + serverIp + ":9090/video/" + fileUUID;
        }

        // 存储数据库
        Video saveFile = new Video();
        saveFile.setName(originalFilename);
        saveFile.setSize(size / 1024); // 单位 kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        videoMapper.insert(saveFile);
        return url;
    }

    /**
     * 文件下载接口   http://localhost:9090/file/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Video getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Video> filesList = videoMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    //    @CachePut(value = "files", key = "'frontAll'")
    @PostMapping("/update")
    public Result update(@RequestBody Video files) {
        videoMapper.updateById(files);
        //flushRedis(Constants.FILES_KEY);
        return Result.success();
    }

    @AuthAccess
    @GetMapping("/detail/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(videoMapper.selectById(id));
    }


    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(videoMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }
}


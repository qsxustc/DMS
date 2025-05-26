package com.dms.springboot.service.impl;

import com.dms.springboot.entity.Video;
import com.dms.springboot.mapper.VideoMapper;
import com.dms.springboot.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 本表是为了存储视频信息 服务实现类
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

}

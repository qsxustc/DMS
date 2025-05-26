package com.dms.springboot.service.impl;

import com.dms.springboot.entity.Userinfo;
import com.dms.springboot.mapper.UserinfoMapper;
import com.dms.springboot.service.IUserinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
@Service
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo> implements IUserinfoService {

}

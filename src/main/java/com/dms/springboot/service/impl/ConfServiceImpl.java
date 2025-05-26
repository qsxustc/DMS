package com.dms.springboot.service.impl;

import com.dms.springboot.entity.Conf;
import com.dms.springboot.mapper.ConfMapper;
import com.dms.springboot.service.IConfService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置文件 服务实现类
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-04-25
 */
@Service
public class ConfServiceImpl extends ServiceImpl<ConfMapper, Conf> implements IConfService {

}

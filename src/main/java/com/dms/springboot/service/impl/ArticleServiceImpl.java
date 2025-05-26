package com.dms.springboot.service.impl;

import com.dms.springboot.entity.Article;
import com.dms.springboot.mapper.ArticleMapper;
import com.dms.springboot.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-03-22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}

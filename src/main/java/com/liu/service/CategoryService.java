package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}

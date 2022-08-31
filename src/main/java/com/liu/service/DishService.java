package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.dto.DishDto;
import com.liu.entity.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto) ;

}

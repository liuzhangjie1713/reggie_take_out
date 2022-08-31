package com.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.common.CustomException;
import com.liu.entity.Category;
import com.liu.entity.Dish;
import com.liu.entity.Setmeal;
import com.liu.service.DishService;
import com.liu.service.SetmealService;
import com.liu.mapper.CategoryMapper;
import com.liu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int countDish = dishService.count(dishQueryWrapper);

        //查询当前分类是否关联菜品,如果已经关联，抛出业务异常
        if(countDish>0){
            //已经关联菜品，抛出业务异常
            throw new CustomException("已经关联菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int counSetmeal = setmealService.count(setmealQueryWrapper);

        if( counSetmeal>0){
            //已经关联套餐，抛出业务异常
            throw new CustomException("已经关联套餐，不能删除");
        }
        //正常删除分类
        if(!super.removeById(id)){
            throw new CustomException("分类删除失败");
        }
    }
}

package com.liu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.common.Result;
import com.liu.dto.DishDto;
import com.liu.entity.Category;
import com.liu.entity.Dish;
import com.liu.entity.DishFlavor;
import com.liu.service.CategoryService;
import com.liu.service.DishFlavorService;
import com.liu.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);

        // 清理 后台修改分类 下面的菜品缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);

        return Result.success("新增菜品成功");
    }


    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        Page<DishDto> dishDtoPage = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name), Dish::getName, name);

        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //进行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto=new DishDto();

            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            //根据id查分类对象
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
    }

    //根据Id查询菜品信息与对应的口味信息
    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }

    //修改菜品
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);

        // 清理 后台修改分类 下面的菜品缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);

        return Result.success("修改菜品成功");
    }

    //停售起售菜品
    @PostMapping("/status/{status}")
    public Result<String> sale(@PathVariable int status,
                          String[] ids){
        for(String id: ids){
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return Result.success("修改成功");
    }
    //删除菜品
    @DeleteMapping
    public Result<String> delete(String[] ids){
        for (String id:ids) {
            dishService.removeById(id);
        }
        return Result.success("删除成功");
    }

    //根据条件查询对应菜品数据
    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish){

        List<DishDto> dishDtoList = null;
        //  根据菜品的分类(湘菜、川菜) 去缓存菜品数据
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();

        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        if (dishDtoList != null){
            return Result.success(dishDtoList);
        }

        // dishDtoList == null,即Redis中没有 对应的菜品数据，需要去查询数据库
        //构造查询条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加条件，查询状态为1的（起售状态）
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //条件排序条件
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list=dishService.list(lambdaQueryWrapper);
        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);
            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, dishId);
            //SQL: select* from dishflavor where dish_id=?;
            List<DishFlavor> dishFlavorlist = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavorlist);
            return dishDto;
        }).collect(Collectors.toList());

        // 将查询到的菜品数据缓存到Redis,并且设置其 查询到的菜品数据有效时间为1小时，其后会清除菜品该菜品数据
        redisTemplate.opsForValue().set(key,dishDtoList,60L, TimeUnit.MINUTES);

        return Result.success(dishDtoList);

    }


}

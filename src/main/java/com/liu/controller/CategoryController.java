package com.liu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.common.CustomException;
import com.liu.common.Result;
import com.liu.entity.Category;
import com.liu.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody Category category){
        log.info("category: {}", category);
        categoryService.save(category);
        return Result.success("新增分类成功");
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize){
        log.info("page={},pageSize={}", page, pageSize);

        //构造分页构造器
        Page<Category> pageInfo=new Page<>(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();

        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //执行查询
        categoryService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo);
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(Long ids){
        log.info("删除分类，id为{}",ids);
        categoryService.remove(ids);
        return Result.success("分类信息删除成功");
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        log.info("category: {}", category);
        if(!categoryService.updateById(category)){
            throw new CustomException("分类修改失败");
        }
        return Result.success("分类修改成功");
    }



    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return Result.success(list);
    }


}

package com.liu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liu.common.Result;
import com.liu.entity.ShoppingCart;
import com.liu.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 购物车
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 加入购物车
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        log.info("购物车数据：{}", shoppingCart);
        //设置用户id，指定当前是哪个用户的购物车数据
        Long userId =(Long) session.getAttribute("user");
        shoppingCart.setUserId(userId);

        //查询当前菜品或者套餐是否已经在购物车当中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        if (dishId != null) {
            //添加到购物车的为菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加到购物车的为套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //SQL:select *from shopping_cart where user_id=? and dish_id/setmeal_id =?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        ShoppingCart oneCart = shoppingCartService.getOne(queryWrapper);
        //  如果购物车中 已经存在该菜品或套餐，其数量+1，不存在，就将该购物车数据保存到数据库中
        if (oneCart != null){
            Integer number = oneCart.getNumber();
            oneCart.setNumber(number + 1);

            shoppingCartService.updateById(oneCart);
        }else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);

            oneCart = shoppingCart;
        }


        return Result.success(oneCart);
    }

    // 在购物车中删减订单
    @PostMapping("/sub")
    public Result<String> subToCart(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        log.info("购物车中的数据:{}"+shoppingCart.toString());

        Long userId =(Long) session.getAttribute("user");
        shoppingCart.setUserId(userId);


        // 查询当前菜品或套餐是否 在购物车中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);  // 根据登录用户的 userId去ShoppingCart表中查询该用户的购物车数据

        if (dishId != null){ // 添加进购物车的是菜品，且 购物车中已经添加过 该菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }


        ShoppingCart oneCart = shoppingCartService.getOne(queryWrapper);
        //  如果购物车中 已经存在该菜品或套餐，其数量+1，不存在，就将该购物车数据保存到数据库中
        if (oneCart != null){
            Integer number = oneCart.getNumber();
            if (number != 0){
                oneCart.setNumber(number - 1);
                shoppingCartService.updateById(oneCart);
            }else {
                shoppingCartService.remove(queryWrapper);
            }

        }
        return Result.success("成功删减订单!");
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(HttpSession session){
        Long userId =(Long) session.getAttribute("user");


        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);

        // 最晚下单的 菜品或套餐在购物车中最先展示
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result<String> cleanCart(HttpSession session){
        Long userId =(Long) session.getAttribute("user");


        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        // DELETE FROM shopping_cart WHERE (user_id = ?)
        queryWrapper.eq(ShoppingCart::getUserId,userId);


        shoppingCartService.remove(queryWrapper);

        return Result.success("成功清空购物车！");
    }



}

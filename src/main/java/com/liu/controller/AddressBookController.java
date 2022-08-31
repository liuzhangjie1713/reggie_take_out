package com.liu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.liu.common.Result;
import com.liu.entity.AddressBook;
import com.liu.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * 地址簿管理
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @param session
     * @return
     */
    @PostMapping()
    public Result<String> save(@RequestBody AddressBook addressBook, HttpSession session){
        addressBook.setUserId((Long) session.getAttribute("user"));
        log.info("addressBook: {}", addressBook);
        addressBookService.save(addressBook);
        return Result.success("地址新增成功");
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @param session
     * @return
     */
    @PutMapping("/default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook, HttpSession session){
        log.info("addressBook: {}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        Long userId = (Long) session.getAttribute("user");
        wrapper.eq(AddressBook::getUserId, userId);
        wrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 根据id查地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById((id));
        if(addressBook != null){
            return Result.success(addressBook);
        }
        return Result.error("没有找到地址");
    }

    /**
     * 查询默认地址
     * @param session
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault( HttpSession session){
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId);
        wrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(wrapper);
        if(null == addressBook){
            return Result.error("没有找到默认地址");
        }
        return Result.success(addressBook);
    }


    @GetMapping("/list")
    public Result<List<AddressBook>> list( HttpSession session){
        Long userId = (Long) session.getAttribute("user");

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != userId, AddressBook::getUserId,userId);
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        return Result.success(addressBookService.list(queryWrapper));
    }




}

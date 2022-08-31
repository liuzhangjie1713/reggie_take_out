package com.liu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.entity.AddressBook;
import com.liu.mapper.AddressBookMapper;
import com.liu.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService{
}

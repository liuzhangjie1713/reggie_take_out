package com.liu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.entity.Employee;
import com.liu.mapper.EmployeeMapper;
import com.liu.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

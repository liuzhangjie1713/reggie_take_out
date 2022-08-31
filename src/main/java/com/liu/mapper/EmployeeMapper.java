package com.liu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.entity.Employee;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

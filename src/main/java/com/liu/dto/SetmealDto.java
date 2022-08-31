package com.liu.dto;

import com.liu.entity.Setmeal;
import com.liu.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaoning
 * @date 2022/10/02
 */
@Data
@TableName("role")
public class Role {

    @TableId
    private Integer id;

    private String code;

    private String name;

}

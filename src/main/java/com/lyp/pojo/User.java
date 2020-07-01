package com.lyp.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    //如果需要设置主键id增长策略
//    @TableId(type = IdType.AUTO) // 设置主键自增
    private Long id;
    private String name;
    // 当不想将某个字段查询出来的时候可以使用
//    @TableField(select = false)
    private Integer age;
    // 当实体类的属性名称与数据库的字段名称不一致的使用可以使用@TableField(value = 数据库中字段的名称)
    @TableField(value = "email")
    private String mail;
    private Long managerId;
    private Date createTime;
    // 当某个字段在数据库中不存在的时候需要进行声明
    @TableField(exist = false)
    private String address;
}

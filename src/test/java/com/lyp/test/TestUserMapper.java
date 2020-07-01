package com.lyp.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lyp.mapper.UserMapper;
import com.lyp.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserMapper {

    @Resource
    private UserMapper userMapper;

    /**
     * 添加数据
     */
    @Test
    public void insertTest() {

        User user = new User();
        user.setAge(12);
        user.setCreateTime(new Date());
        user.setMail("123456@gmail.com");
        user.setManagerId(123456L);
        user.setName("张三");
        user.setId(1234568L);
        user.setAddress("中国");
        int result = userMapper.insert(user); // 返回值是一个数据库库被影响的行数
        System.out.println("result => " + result);
        System.out.println(user.getId());// 插入之后主键id会自动填充
    }

    /***
     * 查询所有
     */
    @Test
    public void selectTest() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }


    /**
     * 根据id进行更新
     */
    @Test
    public void updateByIdTest() {
        User user = new User();
        user.setId(123456L);
        user.setAge(166);
        user.setMail("123@Gmal.com");
        int result = userMapper.updateById(user);
        System.out.println("====================" + result + "==================");
    }

    /**
     * 根据条件进行更新 01
     */
    @Test
    public void updateByWrapper() {
        User user = new User();
        user.setAge(1000);
        user.setMail("1000@Gmail.com");
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.eq("manager_id", "1087982257332887553");
        int result = userMapper.update(user, wrapper);
        System.out.println(result);
    }

    /**
     * 根据条件进行更新的操作02
     */
    @Test
    public void updateByWrapper02() {
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>();
        wrapper.set("age", 100).set("email", "666888@qq.com") // 需要更新的字段
                .eq("manager_id", "1087982257332887553");// 更新的条件
        int result = userMapper.update(null, wrapper);
        System.out.println("=======操作结果========" + result);
    }


    /**
     * 根据id进行删除
     */
    @Test
    public void deleteById() {
        int result = userMapper.deleteById(12345678L);
        System.out.println("==========删除执行结果=========" + result);
    }


    /**
     * 根据Map条件删除数据 ， 多个条件之间使用and连接
     */
    @Test
    public void deleteByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("manager_id", "123456");
        map.put("age", 12);
        int result = userMapper.deleteByMap(map);
        System.out.println("==========执行结果==========" + result);
    }

    /**
     * 根据条件进行删除 QueryWrapper
     */
    @Test
    public void deleteByQueryWrapper() {
        // 方法一 ：
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "张三").eq("id", "1234567");
        int result = userMapper.delete(wrapper);
        System.out.println("=========执行结果=========" + result);
    }

    /**
     * 根据id进行批量的删除操作
     */
    @Test
    public void testBatchId() {
        int result = userMapper.deleteBatchIds(Arrays.asList(1234567L, 1234568L));
        System.out.println("==========执行结果============" + result);
    }

    /**
     * 根据主键id进行查询
     */
    @Test
    public void testSelectById() {
        User user = userMapper.selectById(123456);
        System.out.println(user);
    }

    /**
     * 根据id进行批量查询
     */
    @Test
    public void testSelectByBatchId() {
        List<User> userList = userMapper.selectBatchIds(Arrays.asList(123456L, 1087982257332887553L));
        userList.forEach(System.out::println);
    }
}

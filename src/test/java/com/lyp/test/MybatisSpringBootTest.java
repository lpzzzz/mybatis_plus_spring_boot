package com.lyp.test;

import com.lyp.mapper.UserMapper;
import com.lyp.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisSpringBootTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void findAll() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }
}

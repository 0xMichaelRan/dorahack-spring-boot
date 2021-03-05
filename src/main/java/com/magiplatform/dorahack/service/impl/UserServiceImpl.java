package com.magiplatform.dorahack.service.impl;

import com.magiplatform.dorahack.entity.User;
import com.magiplatform.dorahack.mapper.UserMapper;
import com.magiplatform.dorahack.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Michael Ran
 * @since 2021-03-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

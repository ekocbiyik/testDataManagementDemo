package com.ekocbiyik.tdmdemo.service;

import com.ekocbiyik.tdmdemo.dao.IUserDao;
import com.ekocbiyik.tdmdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by enbiya on 05.01.2017.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Transactional
    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Transactional
    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Transactional
    @Override
    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    @Transactional
    @Override
    public List<User> getUsersForAdmin(User user) {
        return userDao.getUsersForAdmin(user);
    }

    @Transactional
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional
    @Override
    public List<User> getDefaultSysAdminUser() {
        return userDao.getDefaultSysAdminUser();
    }

    @Transactional
    @Override
    public List<User> getDefaultAdminUser() {
        return userDao.getDefaultAdminUser();
    }

    @Transactional
    @Override
    public List<User> getDefaultTestUser() {
        return userDao.getDefaultTestUser();
    }

    @Transactional
    @Override
    public boolean isUsernameExist(String username) {
        return userDao.isUsernameExist(username);
    }
}

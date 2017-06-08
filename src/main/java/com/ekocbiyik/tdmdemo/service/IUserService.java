package com.ekocbiyik.tdmdemo.service;

import com.ekocbiyik.tdmdemo.model.User;

import java.util.List;

/**
 * Created by enbiya on 05.01.2017.
 */
public interface IUserService {

    void save(User user);

    void delete(User user);

    User login(String username, String password);

    List<User> getUsersForAdmin(User user);

    List<User> getAllUsers();

    List<User> getDefaultSysAdminUser();

    List<User> getDefaultAdminUser();

    List<User> getDefaultTestUser();

    boolean isUsernameExist(String username);

}

package com.ekocbiyik.tdmdemo.dao;

import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.model.User;
import com.ekocbiyik.tdmdemo.utils.EncryptionUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by enbiya on 05.01.2017.
 */
@Component
public class UserDaoImpl implements IUserDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(User user) {
        getCurrentSession().saveOrUpdate(user);
    }

    @Override
    public void delete(User user) {
        getCurrentSession().delete(user);
    }

    @Override
    public User login(String username, String password) {

        Query q = getCurrentSession().createQuery("from User where username = :username and password = :password and inActive = true");
        q.setParameter("username", username);
        q.setParameter("password", EncryptionUtils.hexMd5(password));

        List<User> user = q.list();
        if (user.size() == 0) {

            return null;
        } else {

            return user.get(0);
        }
    }

    @Override
    public List<User> getUsersForAdmin(User user) {

        Query query = getCurrentSession().createQuery("from User where company = :company and role != :role");
        query.setParameter("company", user.getCompany());
        query.setParameter("role", EUserRole.SYSADMIN);
        return query.list();
    }

    @Override
    public List<User> getAllUsers() {
        return getCurrentSession().createQuery("from User").list();
    }

    @Override
    public List<User> getDefaultSysAdminUser() {
        return getCurrentSession().createQuery("from User where username = 'sysadmin'").list();
    }

    @Override
    public List<User> getDefaultAdminUser() {
        return getCurrentSession().createQuery("from User where username = 'admin'").list();
    }

    @Override
    public List<User> getDefaultTestUser() {
        return getCurrentSession().createQuery("from User where username = 'test'").list();
    }

    @Override
    public boolean isUsernameExist(String username) {

        Query query = getCurrentSession().createQuery("from User where username = :username");
        query.setParameter("username", username);
        return query.list().size() > 0;
    }

}

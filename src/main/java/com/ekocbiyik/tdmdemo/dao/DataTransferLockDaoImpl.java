package com.ekocbiyik.tdmdemo.dao;

import com.ekocbiyik.tdmdemo.model.DataTransferLock;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by enbiya on 07.06.2017.
 */
@Component
public class DataTransferLockDaoImpl implements IDataTransferLockDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveTransferLock(DataTransferLock transferLock) {
        getCurrentSession().save(transferLock);
    }

    @Override
    public void deleteTransferLock() {
        getCurrentSession().createQuery("delete from DataTransferLock").executeUpdate();
    }

    @Override
    public List<DataTransferLock> getTransferLock() {
        return getCurrentSession().createQuery("from DataTransferLock").list();
    }
}

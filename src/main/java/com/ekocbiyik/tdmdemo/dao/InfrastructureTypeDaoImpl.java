package com.ekocbiyik.tdmdemo.dao;

import com.ekocbiyik.tdmdemo.model.InfrastructureType;
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
public class InfrastructureTypeDaoImpl implements IInfrastructureTypeDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(InfrastructureType infType) {
        getCurrentSession().saveOrUpdate(infType);
    }

    @Override
    public void delete(InfrastructureType infType) {
        getCurrentSession().delete(infType);
    }

    @Override
    public List<String> getDistinctTypeFields() {
        return getCurrentSession().createQuery("select distinct i.type from InfrastructureType as i").list();
    }

    @Override
    public List<String> getInternetTypeValues() {
        Query q = getCurrentSession().createQuery("select distinct  i.value from InfrastructureType as i where i.type = :internetType");
        q.setParameter("internetType", "INTERNET");
        return q.list();
    }

    @Override
    public List<String> getTvTypeValues() {
        Query q = getCurrentSession().createQuery("select distinct  i.value from InfrastructureType as i where i.type = :tvType");
        q.setParameter("tvType", "TV");
        return q.list();
    }

    @Override
    public List<String> getTelefonTypeValues() {
        Query q = getCurrentSession().createQuery("select distinct  i.value from InfrastructureType as i where i.type = :telefonType");
        q.setParameter("telefonType", "TELEFON");
        return q.list();
    }


    @Override
    public List<InfrastructureType> getAllInfrastructureTypes() {
        return getCurrentSession().createQuery("from InfrastructureType").list();
    }
}

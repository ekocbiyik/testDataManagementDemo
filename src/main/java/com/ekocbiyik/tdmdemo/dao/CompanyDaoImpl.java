package com.ekocbiyik.tdmdemo.dao;

import com.ekocbiyik.tdmdemo.model.Company;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by enbiya on 16.03.2017.
 */
@Component
public class CompanyDaoImpl implements ICompanyDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Company company) {
        getCurrentSession().saveOrUpdate(company);
    }

    @Override
    public List<Company> getCompanies() {
        return getCurrentSession().createQuery("from Company").list();
    }

}

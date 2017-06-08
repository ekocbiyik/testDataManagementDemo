package com.ekocbiyik.tdmdemo.service;

import com.ekocbiyik.tdmdemo.dao.ICompanyDao;
import com.ekocbiyik.tdmdemo.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by enbiya on 16.03.2017.
 */
@Service
public class CompanyServiceImpl implements ICompanyService {

    @Autowired
    private ICompanyDao companyDao;

    @Transactional
    @Override
    public void save(Company company) {
        companyDao.save(company);
    }

    @Transactional
    @Override
    public List<Company> getCompanies() {
        return companyDao.getCompanies();
    }

}

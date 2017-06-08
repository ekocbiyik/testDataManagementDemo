package com.ekocbiyik.tdmdemo.dao;

import com.ekocbiyik.tdmdemo.model.Company;

import java.util.List;

/**
 * Created by enbiya on 16.03.2017.
 */
public interface ICompanyDao {

    void save(Company company);

    List<Company> getCompanies();

}

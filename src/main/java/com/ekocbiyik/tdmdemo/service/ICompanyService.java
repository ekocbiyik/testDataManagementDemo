package com.ekocbiyik.tdmdemo.service;

import com.ekocbiyik.tdmdemo.model.Company;

import java.util.List;

/**
 * Created by enbiya on 16.03.2017.
 */
public interface ICompanyService {

    void save(Company company);

    List<Company> getCompanies();

}

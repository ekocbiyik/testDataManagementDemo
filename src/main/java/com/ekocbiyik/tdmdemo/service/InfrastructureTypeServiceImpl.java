package com.ekocbiyik.tdmdemo.service;

import com.ekocbiyik.tdmdemo.dao.IInfrastructureTypeDao;
import com.ekocbiyik.tdmdemo.model.InfrastructureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by enbiya on 05.01.2017.
 */
@Service
public class InfrastructureTypeServiceImpl implements IInfrastructureTypeService {

    @Autowired
    private IInfrastructureTypeDao infrastructureTypeDao;

    @Transactional
    @Override
    public void save(InfrastructureType infType) {
        infrastructureTypeDao.save(infType);
    }

    @Transactional
    @Override
    public void delete(InfrastructureType infType) {
        infrastructureTypeDao.delete(infType);
    }

    @Transactional
    @Override
    public List<String> getDistinctTypeFields() {
        return infrastructureTypeDao.getDistinctTypeFields();
    }

    @Transactional
    @Override
    public List<String> getInternetTypeValues() {
        return infrastructureTypeDao.getInternetTypeValues();
    }

    @Transactional
    @Override
    public List<String> getTvTypeValues() {
        return infrastructureTypeDao.getTvTypeValues();
    }

    @Transactional
    @Override
    public List<String> getTelefonTypeValues() {
        return infrastructureTypeDao.getTelefonTypeValues();
    }

    @Transactional
    @Override
    public List<InfrastructureType> getAllInfrastructureTypes() {
        return infrastructureTypeDao.getAllInfrastructureTypes();
    }

}

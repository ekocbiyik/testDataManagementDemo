package com.ekocbiyik.tdmdemo.service;

import com.ekocbiyik.tdmdemo.model.InfrastructureType;

import java.util.List;

/**
 * Created by enbiya on 05.01.2017.
 */
public interface IInfrastructureTypeService {

    void save(InfrastructureType infType);

    void delete(InfrastructureType infType);

    List<String> getDistinctTypeFields();

    List<String> getInternetTypeValues();

    List<String> getTvTypeValues();

    List<String> getTelefonTypeValues();

    List<InfrastructureType> getAllInfrastructureTypes();

}

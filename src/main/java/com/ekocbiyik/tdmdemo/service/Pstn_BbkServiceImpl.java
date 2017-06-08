package com.ekocbiyik.tdmdemo.service;

import com.ekocbiyik.tdmdemo.dao.IPstn_BbkDao;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.enums.EnvironmentType;
import com.ekocbiyik.tdmdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by enbiya on 05.01.2017.
 */
@Service
public class Pstn_BbkServiceImpl implements IPstn_BbkService {

    @Autowired
    private IPstn_BbkDao pstn_bbkDao;

    @Transactional
    @Override
    public void save(Pstn_Bbk pstn_bbk) {
        pstn_bbkDao.save(pstn_bbk);
    }

    @Transactional
    @Override
    public void delete(Pstn_Bbk pstn_bbk) {
        pstn_bbkDao.delete(pstn_bbk);
    }

    @Transactional
    @Override
    public void removeUserPstnBbk(User user, Pstn_Bbk pstn_bbk) {
        pstn_bbkDao.removeUserPstnBbk(user, pstn_bbk);
    }

    @Transactional
    @Override
    public void clearInReservePstnBbk() {
        pstn_bbkDao.clearInReservePstnBbk();
    }

    @Transactional
    @Override
    public List<Pstn_Bbk> getExpiredBbkList() {
        return pstn_bbkDao.getExpiredBbkList();
    }

    @Transactional
    @Override
    public List<Pstn_Bbk> getInResevedPstnBbk(User user) {
        return pstn_bbkDao.getInResevedPstnBbk(user);
    }

    @Transactional
    @Override
    public List<Pstn_Bbk> getUsersAllHiddenPstnByBbk(User user, Pstn_Bbk pstn_bbk) {
        return pstn_bbkDao.getUsersAllHiddenPstnByBbk(user, pstn_bbk);
    }

    @Transactional
    @Override
    public List<Pstn_Bbk> getHiddenPstnByBbk(Pstn_Bbk pstn_bbk) {
        return pstn_bbkDao.getHiddenPstnByBbk(pstn_bbk);
    }

    @Transactional
    @Override
    public List<Pstn_Bbk> getUserPstnBbkList(User user) {
        return pstn_bbkDao.getUserPstnBbkList(user);
    }

    @Transactional
    @Override
    public List<Pstn_Bbk> getAllPstnBbk() {
        return pstn_bbkDao.getAllPstnBbk();
    }

    @Transactional
    @Override
    public List<Pstn_Bbk> getNvdslBbk(User user, String dataType, EnvironmentType envType) {
        return pstn_bbkDao.getNvdslBbk(user, dataType, envType); // orjinali
    }

    @Transactional
    @Override
    public List<Pstn_Bbk> getVdslBbk(User user, String dataType, EnvironmentType envType) {
        return pstn_bbkDao.getVdslBbk(user, dataType, envType);
    }

    @Transactional
    @Override
    public void deleteAllPstnBbkByEnvironmet(EnvironmentType envType) {
        pstn_bbkDao.deleteAllPstnBbkByEnvironmet(envType);
    }

    @Transactional
    @Override
    public void savePstnBbkByEnvironmet(Pstn_Bbk pstn_bbk) {
        pstn_bbkDao.savePstnBbkByEnvironmet(pstn_bbk);
    }

    @Transactional
    @Override
    public int getPstnTotalCountByEnvironment(EnvironmentType env) {
        return pstn_bbkDao.getPstnTotalCountByEnvironment(env);
    }

    @Transactional
    @Override
    public int getPstnUsingCountByEnvironment(EnvironmentType env) {
        return pstn_bbkDao.getPstnUsingCountByEnvironment(env);
    }

    @Transactional
    @Override
    public List<Pstn_Bbk> getUsersDataByAdmin(User user) {
        return pstn_bbkDao.getUsersDataByAdmin(user);
    }

    @Transactional
    @Override
    public int getPstnBbkCountByEnvironment(EnvironmentType envType, User user) {
        return pstn_bbkDao.getPstnBbkCountByEnvironment(envType, user);
    }
}

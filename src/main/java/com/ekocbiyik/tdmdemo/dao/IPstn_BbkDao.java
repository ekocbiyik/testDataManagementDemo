package com.ekocbiyik.tdmdemo.dao;

import com.ekocbiyik.tdmdemo.enums.EnvironmentType;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.model.User;

import java.util.List;

/**
 * Created by enbiya on 05.01.2017.
 */
public interface IPstn_BbkDao {

    void save(Pstn_Bbk pstn_bbk);

    void delete(Pstn_Bbk pstn_bbk);

    void removeUserPstnBbk(User user, Pstn_Bbk pstn_bbk);

    void clearInReservePstnBbk();

    List<Pstn_Bbk> getExpiredBbkList();

    List<Pstn_Bbk> getInResevedPstnBbk(User user);

    List<Pstn_Bbk> getUsersAllHiddenPstnByBbk(User user, Pstn_Bbk pstn_bbk);

    List<Pstn_Bbk> getHiddenPstnByBbk(Pstn_Bbk pstn_bbk);

    List<Pstn_Bbk> getUserPstnBbkList(User user);

    List<Pstn_Bbk> getAllPstnBbk();

    List<Pstn_Bbk> getNvdslBbk(User user, String dataType, EnvironmentType envType);

    List<Pstn_Bbk> getVdslBbk(User user, String dataType, EnvironmentType envType);

    void deleteAllPstnBbkByEnvironmet(EnvironmentType envType);

    void savePstnBbkByEnvironmet(Pstn_Bbk pstn_bbk);

    int getPstnTotalCountByEnvironment(EnvironmentType env);

    int getPstnUsingCountByEnvironment(EnvironmentType env);

    List<Pstn_Bbk> getUsersDataByAdmin(User user);

    int getPstnBbkCountByEnvironment(EnvironmentType envType, User user);

}

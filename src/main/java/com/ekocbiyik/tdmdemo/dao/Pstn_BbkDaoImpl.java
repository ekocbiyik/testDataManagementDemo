package com.ekocbiyik.tdmdemo.dao;

import com.ekocbiyik.tdmdemo.enums.EnvironmentType;
import com.ekocbiyik.tdmdemo.enums.PstnStatus;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 05.01.2017.
 */
@Component
public class Pstn_BbkDaoImpl implements IPstn_BbkDao {


    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Pstn_Bbk pstn_bbk) {
        getCurrentSession().saveOrUpdate(pstn_bbk);
    }

    @Override
    public void delete(Pstn_Bbk pstn_bbk) {
        getCurrentSession().delete(pstn_bbk);
    }

    @Override
    public void removeUserPstnBbk(User user, Pstn_Bbk pstn_bbk) {


        /** bu bbknın gizli pstnleri var mı? */
        List<Pstn_Bbk> hiddenList = getUsersAllHiddenPstnByBbk(user, pstn_bbk);

        for (Pstn_Bbk i : hiddenList) {
            i.setPstnStatus(PstnStatus.BOSTA);
            i.setExpireDate(null);
            i.setCreationDate(null);
            i.setOwner(null);
            getCurrentSession().saveOrUpdate(i);
        }

        pstn_bbk.setPstnStatus(PstnStatus.BOSTA);
        pstn_bbk.setExpireDate(null);
        pstn_bbk.setCreationDate(null);
        pstn_bbk.setOwner(null);

        getCurrentSession().saveOrUpdate(pstn_bbk);
    }

    @Override
    public void clearInReservePstnBbk() {

        /** task çalıştığında; rezerv aşamasındaki ve kullanıcıya ait tüm pstnleri temizle  */
        Query query = getCurrentSession().createQuery(
                "update Pstn_Bbk set inReserve = false, owner = null, pstnStatus = :pstnStatus " +
                        "where inReserve = true " +
                        "and owner is not null ");
        query.setParameter("pstnStatus", PstnStatus.BOSTA);

        query.executeUpdate();
    }

    @Override
    public List<Pstn_Bbk> getExpiredBbkList() {

        /** expireDate geçmiş olan sadece bbkları döndürür */

        Query query = getCurrentSession().createQuery("from Pstn_Bbk " +
                "where owner is not null " +
                "and expireDate <= :exDate " +
                "and pstnStatus = :status");

        query.setParameter("exDate", new Date());
        query.setParameter("status", PstnStatus.KULLANIMDA);

        return query.list();
    }

    @Override
    public List<Pstn_Bbk> getUserPstnBbkList(User user) {

        Query query = getCurrentSession().createQuery("from Pstn_Bbk " +
                "where owner = :user " +
                "and pstnStatus = :status " +
                "and expireDate is not null order by expireDate, bbk asc");

        query.setParameter("user", user);
        query.setParameter("status", PstnStatus.KULLANIMDA);
        return query.list();
    }

    @Override
    public List<Pstn_Bbk> getInResevedPstnBbk(User user) {

        /** burada kullanıcı ekranında sadece bir tane pstn bbk gözüksün diye
         *  KULLANIMDA olan şartı koyduk
         * */
        Query query = getCurrentSession().createQuery(
                "from Pstn_Bbk where inReserve = true " +
                        "and owner = :user " +
                        "and pstnStatus = :pstnStatus");
        query.setParameter("user", user);
        query.setParameter("pstnStatus", PstnStatus.KULLANIMDA);

        return query.list();
    }

    @Override
    public List<Pstn_Bbk> getUsersAllHiddenPstnByBbk(User user, Pstn_Bbk pstn_bbk) {

        /** reserve aşamasındaki bbk'yı kullanıcı üzerine alırken
         *  gizli pstn varsa onları arka planda listelemek için bu metot çağırılacak
         * */

        Query query = getCurrentSession().createQuery("from Pstn_Bbk where owner = :user " +
                "and bbk = :bbk " +
                "and environmentType = :envType " +
                "and pstnStatus = :status");
        query.setParameter("user", user);
        query.setParameter("bbk", pstn_bbk.getBbk());
        query.setParameter("envType", pstn_bbk.getEnvironmentType());
        query.setParameter("status", PstnStatus.BLOK);

        return query.list();
    }

    @Override
    public List<Pstn_Bbk> getHiddenPstnByBbk(Pstn_Bbk pstn_bbk) {

        /** bu kısım süresi dolmuş pstnleri oto. silmek için,
         * (bkz.ExpirePstnBbkUtils)
         *
         * bbk'ya ait tüm pstnler
         * */

        Query query = getCurrentSession().createQuery("from Pstn_Bbk where " +
                "bbk = :bbk " +
                "and environmentType = :envType " +
                "and pstnStatus = :status");
        query.setParameter("bbk", pstn_bbk.getBbk());
        query.setParameter("envType", pstn_bbk.getEnvironmentType());
        query.setParameter("status", PstnStatus.BLOK);

        return query.list();
    }

    @Override
    public List<Pstn_Bbk> getAllPstnBbk() {
        return getCurrentSession().createQuery("from Pstn_Bbk").list();
    }

    @Override
    public synchronized void deleteAllPstnBbkByEnvironmet(EnvironmentType envType) {
        Query query = getCurrentSession().createQuery("delete from Pstn_Bbk where environmentType = :envType");
        query.setParameter("envType", envType);
        query.executeUpdate();
    }

    public synchronized void savePstnBbkByEnvironmet(Pstn_Bbk pstn_bbk) {
        getCurrentSession().save(pstn_bbk);
    }

    @Override
    public synchronized List<Pstn_Bbk> getNvdslBbk(User user, String dataType, EnvironmentType envType) {

        Query query = getCurrentSession().createQuery(
                "select bbk from Pstn_Bbk " +
                        "where pstnStatus = :status " +
                        "and owner is null " +
                        "and inReserve=false " +
                        "and expireDate=null " +
                        "and creationDate=null " +
                        "and dslBasvuru=false " +
                        "and inUsefull=true " +
                        "and dataType = :dataType " +
                        "and environmentType = :envType " +
                        "group by bbk order by count(pstn) asc");// ASC ÇOK ÖNEMLİ

        query.setParameter("dataType", dataType);
        query.setParameter("envType", envType);
        query.setParameter("status", PstnStatus.BOSTA);

        /** ilk bbk'yı getir */
        List uniqBbk = query.setMaxResults(1).list();
        if (uniqBbk.size() == 0) {
            return new ArrayList<>();
        }

        /** bbkya ait tüm pstnleri getir */
        Query query1 = getCurrentSession().createQuery(
                "from Pstn_Bbk " +
                        "where bbk = :uniqBBK " +
                        "and inReserve=false " +
                        "and expireDate=null " +
                        "and creationDate=null " +
                        "and dslBasvuru=false " +
                        "and inUsefull=true " +
                        "and dataType = :dataType " +
                        "and environmentType = :envType "
        );

        query1.setParameter("uniqBBK", uniqBbk.get(0));
        query1.setParameter("dataType", dataType);
        query1.setParameter("envType", envType);

        /** tüm pstnleri rezerv et, ilkini göster, diğerlerini gizle */
        List<Pstn_Bbk> pstnList = query1.list();
        for (int i = 0; i < pstnList.size(); i++) {

            Pstn_Bbk temp = pstnList.get(i);

            /** ilk kayıt gorülecek */
            if (i == 0) {
                temp.setPstnStatus(PstnStatus.KULLANIMDA);
                temp.setHizmetTuru("Yalın Internet");
            } else {
                temp.setPstnStatus(PstnStatus.BLOK);
            }

            temp.setInReserve(true);
            temp.setOwner(user);
            getCurrentSession().saveOrUpdate(temp);
        }

        return new ArrayList<Pstn_Bbk>(Arrays.asList(pstnList.get(0)));
    }

    @Override
    public synchronized List<Pstn_Bbk> getVdslBbk(User user, String dataType, EnvironmentType envType) {

        /** ilk olarak bbkları pstni çoktan aza sırala ilk bbkyı getir*/

        Query query = getCurrentSession().createQuery(
                "select bbk from Pstn_Bbk " +
                        "where inReserve=false " +
                        "and pstnStatus= :status " +
                        "and expireDate=null " +
                        "and creationDate=null " +
                        "and dslBasvuru=false " +
                        "and inUsefull=true " +
                        "and dataType = :dataType " +
                        "and environmentType = :envType " +
                        "group by bbk order by count(pstn) desc");
        query.setParameter("dataType", dataType);
        query.setParameter("envType", envType);
        query.setParameter("status", PstnStatus.BOSTA);


        List uniqBBK = query.setMaxResults(1).list(); //buradan 1 tane bbk gelecek
        if (uniqBBK.size() == 0) {
            return new ArrayList<Pstn_Bbk>();
        }

        /** bbkya ait ilk pstn'i getir
         select * from t_pstn_bbk  where
         in_reserve=0
         and expire_date is null
         and creation_date is null
         and dsl_basvuru = 0
         and is_usefull=1
         and data_type ='VDSLADSLIPTV'
         and bbk='42325710'
         --and rownum=1
         order by pstn asc;
         */
        Query query1 = getCurrentSession().createQuery("from Pstn_Bbk " +
                "where bbk = :uniqBBK " +
                "and inReserve=false " +
                "and pstnStatus= :status " +
                "and expireDate=null " +
                "and creationDate=null " +
                "and dslBasvuru=false " +
                "and inUsefull=true " +
                "and dataType = :dataType " +
                "and environmentType = :envType " +
                "order by pstn asc");
        query1.setParameter("uniqBBK", uniqBBK.get(0));
        query1.setParameter("dataType", dataType);
        query1.setParameter("envType", envType);
        query1.setParameter("status", PstnStatus.BOSTA);

        List<Pstn_Bbk> bbkPstnList = query1.setMaxResults(1).list();


        /** kullanıcıya rezerv et */
        Pstn_Bbk tempBbk = bbkPstnList.get(0);
        tempBbk.setInReserve(true);
        tempBbk.setOwner(user);
        tempBbk.setPstnStatus(PstnStatus.KULLANIMDA);
        tempBbk.setHizmetTuru("Internet");

        getCurrentSession().saveOrUpdate(tempBbk);

        return new ArrayList<Pstn_Bbk>(Arrays.asList(tempBbk));
    }

    @Override
    public int getPstnTotalCountByEnvironment(EnvironmentType env) {
        return ((Long) getCurrentSession().createQuery("select count (*) from Pstn_Bbk where environmentType = :env").setParameter("env", env).uniqueResult()).intValue();
    }

    @Override
    public int getPstnUsingCountByEnvironment(EnvironmentType env) {
        return ((Long) getCurrentSession().createQuery("select count(*) from Pstn_Bbk where environmentType = :env and owner is not null").setParameter("env", env).uniqueResult()).intValue();
    }

    @Override
    public List<Pstn_Bbk> getUsersDataByAdmin(User user) {

        Query query = getCurrentSession().createQuery("from Pstn_Bbk where owner is not null " +
                "and owner.company = :company " +
                "and pstnStatus = :status " +
                "order by owner, environmentType, hizmetTuru");

        query.setParameter("company", user.getCompany());
        query.setParameter("status", PstnStatus.KULLANIMDA);

        return query.list();
    }

    public int getPstnBbkCountByEnvironment(EnvironmentType envType, User user) {

        /** burada kullanıcının firmasına ait tüm kullanımda olan bbk sayısını döndürür */

        Query query = getCurrentSession().createQuery("select count(*) from Pstn_Bbk where owner.company = :company " +
                "and pstnStatus = :status " +
                "and environmentType = :envType");

        query.setParameter("company", user.getCompany());
        query.setParameter("status", PstnStatus.KULLANIMDA);
        query.setParameter("envType", envType);

        return ((Long) query.uniqueResult()).intValue();
    }

}

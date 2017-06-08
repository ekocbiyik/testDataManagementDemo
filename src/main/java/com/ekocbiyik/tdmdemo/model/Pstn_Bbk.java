package com.ekocbiyik.tdmdemo.model;

import com.ekocbiyik.tdmdemo.enums.EnvironmentType;
import com.ekocbiyik.tdmdemo.enums.PstnStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by enbiya on 05.01.2017.
 */
@Entity
@Table(name = "t_pstn_bbk")
//@Table(name = "t_pstn_bbk_test")
public class Pstn_Bbk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "t_pstn_bbk_id_seq_generator")
    @SequenceGenerator(name = "t_pstn_bbk_id_seq_generator", sequenceName = "t_pstn_seq")
    @Column(name = "pstn_Id")
    private int pstnId;

    @Column(name = "pstn")
    private String pstn;

    @Column(name = "bbk")
    private String bbk;

    @Column(name = "environment_type")
    @Enumerated(EnumType.STRING)
    private EnvironmentType environmentType;// ortam: test/reg/bugfix

    @Column(name = "data_type")
    private String dataType;// veri tipi: dsl/adsliptv..

    @Column(name = "hizmet_turu")
    private String hizmetTuru; // bu kolon sadece kullanıcılar veriyi hangi amaçla kullandı göstermek için

    @Column(name = "pstn_status", columnDefinition = "varchar(25) default 'BOSTA'")
    @Enumerated(EnumType.STRING)
    private PstnStatus pstnStatus;

    @Column(name = "tckno")
    private String tckNo;

    @Column(name = "telaura_pstn_no")
    private String telauraPstnNo;

    @Column(name = "is_usefull")
    private Boolean inUsefull;

    @Column(name = "dsl_basvuru")
    private Boolean dslBasvuru;

    @Column(name = "crm_cust_no")
    private String crmCustNo;

    @Column(name = "in_out_door")
    private Boolean inOutDoor; //true:in, false:out

    @Column(name = "tms_santral_id")
    private String tmsSantralId;

    @Column(name = "xdsl_santral_id")
    private String xdslSantralId;

    @Column(name = "in_reserve")
    private Boolean inReserve;//kullanıcı bu pstn'i almak için işlemi başlatmıssa diğerleri kullanamasın

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "expire_date")
    private Date expireDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_Id")
    private User owner;

    @Column(name = "gercek_tuzel")
    private Boolean gercekTuzel;

    public int getPstnId() {
        return pstnId;
    }

    public void setPstnId(int pstnId) {
        this.pstnId = pstnId;
    }

    public String getPstn() {
        return pstn;
    }

    public void setPstn(String pstn) {
        this.pstn = pstn;
    }

    public String getBbk() {
        return bbk;
    }

    public void setBbk(String bbk) {
        this.bbk = bbk;
    }

    public String getTckNo() {
        return tckNo;
    }

    public void setTckNo(String tckNo) {
        this.tckNo = tckNo;
    }

    public String getTelauraPstnNo() {
        return telauraPstnNo;
    }

    public void setTelauraPstnNo(String telauraPstnNo) {
        this.telauraPstnNo = telauraPstnNo;
    }

    public Boolean getInUsefull() {
        return inUsefull;
    }

    public void setInUsefull(Boolean inUsefull) {
        this.inUsefull = inUsefull;
    }

    public Boolean getDslBasvuru() {
        return dslBasvuru;
    }

    public void setDslBasvuru(Boolean dslBasvuru) {
        this.dslBasvuru = dslBasvuru;
    }

    public String getCrmCustNo() {
        return crmCustNo;
    }

    public void setCrmCustNo(String crmCustNo) {
        this.crmCustNo = crmCustNo;
    }

    public Boolean getInOutDoor() {
        return inOutDoor;
    }

    public void setInOutDoor(Boolean inOutDoor) {
        this.inOutDoor = inOutDoor;
    }

    public String getTmsSantralId() {
        return tmsSantralId;
    }

    public void setTmsSantralId(String tmsSantralId) {
        this.tmsSantralId = tmsSantralId;
    }

    public String getXdslSantralId() {
        return xdslSantralId;
    }

    public void setXdslSantralId(String xdslSantralId) {
        this.xdslSantralId = xdslSantralId;
    }

    public Boolean getInReserve() {
        return inReserve;
    }

    public void setInReserve(Boolean inReserve) {
        this.inReserve = inReserve;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Boolean getGercekTuzel() {
        return gercekTuzel;
    }

    public void setGercekTuzel(Boolean gercekTuzel) {
        this.gercekTuzel = gercekTuzel;
    }

    public PstnStatus getPstnStatus() {
        return pstnStatus;
    }

    public void setPstnStatus(PstnStatus pstnStatus) {
        this.pstnStatus = pstnStatus;
    }

    public String getHizmetTuru() {
        return hizmetTuru;
    }

    public void setHizmetTuru(String hizmetTuru) {
        this.hizmetTuru = hizmetTuru;
    }
}

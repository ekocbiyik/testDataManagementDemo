package com.ekocbiyik.tdmdemo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by enbiya on 29.12.2016.
 */
@Entity
@Table(name = "t_infrastructure_type")
public class InfrastructureType {

    /**
     * bu class altyapı türlerini barındıracak, ihtiyaç veya değişiklik olması halinde
     * veri tabanına admin tarafından eklenebilecek
     * burada dikkat etmemiz gereken
     * altyapi "type" ve "value" belli kriterlerde olmalı (büyük harf, ingilizce karakterler, iki kelime ise alt tire)
     * info istediği şekilde yazılabilir, type veya valu değerini en açık ifade edecek şekilde girilmesi uygun olur
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "t_infra_id_seq_generator")
    @SequenceGenerator(name = "t_infra_id_seq_generator", sequenceName = "t_infra_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "info")
    private String info; //veri tabanında hangi tablodan verileri çekeceğini gösterecek

    public InfrastructureType() {
    }

    public InfrastructureType(String type, String value, String info) {
        this.type = type;
        this.value = value;
        this.info = info;
    }

//    public static List<String> getTypeInternetValues() {
//
//        List<String> internetValues = new ArrayList<>();
//        internetValues.add("ADSL");
//        internetValues.add("NDSL");
//        internetValues.add("VDSL");
//        internetValues.add("VDSL2");
//        internetValues.add("NVDSL");
//        internetValues.add("NVDSL2");
//        internetValues.add("FIBER");
//        internetValues.add("YALIN_FIBER");
//
//        return internetValues;
//    }
//
//    public static List<String> getTypeTvValues() {
//
//        List<String> tvValues = new ArrayList<>();
//        tvValues.add("IPTV");
//
//        return tvValues;
//    }
//
//    public static List<String> getTypeTelephoneValues() {
//
//        List<String> telephoneValues = new ArrayList<>();
//        telephoneValues.add("TOPTAN");
//        telephoneValues.add("PERAKENDE");
//
//        return telephoneValues;
//    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value.toUpperCase();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

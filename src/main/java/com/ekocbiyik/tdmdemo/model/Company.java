package com.ekocbiyik.tdmdemo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by enbiya on 16.03.2017.
 */
@Entity
@Table(name = "t_company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "t_company_id_seq_generator")
    @SequenceGenerator(name = "t_company_id_seq_generator", sequenceName = "t_company_seq")
    @Column(name = "company_id", nullable = false)
    private int companyId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

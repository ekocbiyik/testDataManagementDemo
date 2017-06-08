package com.ekocbiyik.tdmdemo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by enbiya on 07.06.2017.
 */
@Entity
@Table(name = "t_data_transfer_lock")
public class DataTransferLock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transfer_id")
    private int transferId;

    @Column(name = "is_transfer_running")
    private boolean isTransferRunning;


    public DataTransferLock() {

    }

    public DataTransferLock(boolean isTransferRunning) {
        this.isTransferRunning = isTransferRunning;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public boolean isTransferRunning() {
        return isTransferRunning;
    }

    public void setTransferRunning(boolean transferRunning) {
        isTransferRunning = transferRunning;
    }

}

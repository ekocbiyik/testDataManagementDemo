package com.ekocbiyik.tdmdemo.dao;

import com.ekocbiyik.tdmdemo.model.DataTransferLock;

import java.util.List;

/**
 * Created by enbiya on 07.06.2017.
 */
public interface IDataTransferLockDao {

    void saveTransferLock(DataTransferLock transferLock);

    void deleteTransferLock();

    List<DataTransferLock> getTransferLock();

}

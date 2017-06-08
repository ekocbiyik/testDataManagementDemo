package com.ekocbiyik.tdmdemo.service;

import com.ekocbiyik.tdmdemo.dao.IDataTransferLockDao;
import com.ekocbiyik.tdmdemo.model.DataTransferLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by enbiya on 07.06.2017.
 */
@Service
public class DataTransferLockServiceImpl implements IDataTransferLockService {

    @Autowired
    private IDataTransferLockDao transferLockDao;

    @Transactional
    @Override
    public void saveTransferLock(DataTransferLock transferLock) {
        transferLockDao.saveTransferLock(transferLock);
    }

    @Transactional
    @Override
    public void deleteTransferLock() {
        transferLockDao.deleteTransferLock();
    }

    @Transactional
    @Override
    public List<DataTransferLock> getTransferLock() {
        return transferLockDao.getTransferLock();
    }

}

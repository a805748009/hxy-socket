package com.business.dao;

import com.business.bean.IosReceiptMd5;

public interface IosReceiptMd5Mapper {
    int deleteByPrimaryKey(String receiptData);

    int insert(IosReceiptMd5 record);

    int insertSelective(IosReceiptMd5 record);

    IosReceiptMd5 selectByPrimaryKey(String receiptData);

}
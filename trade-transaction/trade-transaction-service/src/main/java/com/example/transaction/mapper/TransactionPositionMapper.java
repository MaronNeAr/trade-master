package com.example.transaction.mapper;

import com.example.transaction.model.po.TransactionPosition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransactionPositionMapper {
    List<TransactionPosition> selectPositionListByUserId(@Param("uid") String uid);

    List<TransactionPosition> selectPositionListByUserIdAndStatus(@Param("uid") String uid, @Param("status") String status);

    TransactionPosition selectPositionByUserIdAndCode(@Param("uid") String uid, @Param("code") String code);

    int insertPosition(TransactionPosition position);

    int updatePosition(TransactionPosition position);

    int updatePositionStatus(@Param("pid") Integer pid, @Param("status") String status);
}

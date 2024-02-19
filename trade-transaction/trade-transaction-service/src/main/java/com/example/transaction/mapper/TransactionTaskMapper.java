package com.example.transaction.mapper;

import com.example.transaction.model.po.TransactionTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransactionTaskMapper {
    List<TransactionTask> selectTaskListByUserId(@Param("uid") String uid);

    List<TransactionTask> selectTaskListByUserIdAndStatus(@Param("uid") String uid, @Param("status") String status);

    List<TransactionTask> selectTaskListByStatusAndType(@Param("status") String status, @Param("type") String type);

    TransactionTask selectTaskByTaskId(@Param("tid") Integer tid);

    String selectTaskTypeByTaskId(@Param("tid") Integer tid);

    int insertTask(TransactionTask task);

    int updateTaskStatusById(@Param("tid") Integer tid, @Param("status") String status);

    int deleteTaskById(@Param("tid") Integer tid);

}

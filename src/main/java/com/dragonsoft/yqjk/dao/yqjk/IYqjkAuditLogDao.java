package com.dragonsoft.yqjk.dao.yqjk;


import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 审计日志Dao
 * @author ronin
 */
public interface IYqjkAuditLogDao {

    /**
     * 记录getYqDataFromBu方法操作的日志
     *      auditLogParams.yqjkQueryCondition 调用者传递的远程调用接口的入参
     *      auditLogParams.jsonResult 远程调用接口返回的JSON字符串
     *      auditLogParams.invokeStatus 请查看RMIStatus枚举中描述
     * @param auditLogParams
     * @return
     */

    int auditLogGetYqDataFromBu(@Param("auditLogParams") Map<String,Object> auditLogParams);
}

package com.dragonsoft.yqjk.service;


import com.dragonsoft.yqjk.entity.YqjkQueryCondition;

import java.util.Map;

/**
 * 疫情监控接口
 */
public interface IYqjkService {

    /**
     * 从公安部提供的接口中获取疫情数据
     * @param yqjkQueryCondition 封装了查询条件的入参
     * @return
     */
    String getYqDataFromBu(YqjkQueryCondition yqjkQueryCondition) throws Exception;

    /**
     * 从T_USER表中获取用户信息
     * @param userId 用户的id
     * @return
     */
    Map<String,String> getUserInfo(String userId);
}

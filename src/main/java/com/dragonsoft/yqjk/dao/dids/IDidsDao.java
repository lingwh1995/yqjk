package com.dragonsoft.yqjk.dao.dids;

import java.util.Map;

/**
 * @author ronin
 */
public interface IDidsDao {

    /**
     * 从T_USER表中获取用户信息
     * @param userId 用户的id
     * @return
     */
    Map<String,String> getUserInfo(String userId);
}

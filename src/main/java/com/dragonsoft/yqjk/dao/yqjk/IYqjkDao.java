package com.dragonsoft.yqjk.dao.yqjk;

import com.dragonsoft.yqjk.entity.YqjkRMIParam;

import java.util.Map;


/**
 * 疫情监控参数配置
 */
public interface IYqjkDao {

    /**
     * 根据type获取数据
     * @return
     */
    YqjkRMIParam findListByType(String type);

}

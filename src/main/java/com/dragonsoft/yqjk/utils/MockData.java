package com.dragonsoft.yqjk.utils;

import com.alibaba.fastjson.JSON;
import com.dragonsoft.yqjk.entity.YqjkQueryCondition;
import com.dragonsoft.yqjk.entity.YqjkRMIParam;
import com.dragonsoft.yqjk.entity.YqjkToken;

/**
 * @author ronin
 */
public class MockData {

    /**
     * 模拟获取Token
     * 获取Token
     * @param yqjkParam
     * @return
     */
    public static YqjkToken mockGetYqjkToken(YqjkRMIParam yqjkParam) throws Exception{
        String tokenJson = "{\n" +
                "    \"access_token\": \"88bd183bf11d8c2dd5911a01e89eeab5\",\n" +
                "    \"refresh_token\": \"2b764eb8d83abc02ce6707b63d61d3f2\",\n" +
                "    \"expires_in\": 2900\n" +
                "}\n";
        return JSON.parseObject(tokenJson,YqjkToken.class);
    }

    /**
     * 模拟获取从远程接口中返回的数据:
     *      0:可能密切接触者
     *      1:确诊和疑似病例
     * @param yqjkQueryCondition
     * @return
     */
    public static String getMockRMIEmpty(YqjkQueryCondition yqjkQueryCondition){
        if(yqjkQueryCondition.getType().equals(("1"))){
            return "{\"data\":{},\"code\":\"200\",\"msg\":\"对不起没有查询到相关信息\",\"isContactPerson\":null}";
        }else{
            return "{\"data\":[],\"code\":\"200\",\"msg\":\"对不起没有查询到相关信息\",\"isContactPerson\":\"0\"}";
        }
    }

    /**
     * 模拟从可能密切接触者接口中查询到的数据
     * @return
     */
    public static String mockKnmqjczList(){
        /**
         * data不为空:
         */
        return "{\n" +
                "    \"code\": \"200\",\n" +
                "\"msg\": \"查询成功\",\n" +
                "\"iscontactperson\": \"是否为可能密切接触者\",\n" +
                "\"data\":[\n" +
                "{\n" +
                "\t\"xm\": \"姓名\",\n" +
                "\t\t\"sfzhm\": \"身份证号码\",\n" +
                "\t\t\"jcrq\": \"接触日期\",\n" +
                "\t\t\"jclx\": \"接触类型\"\n" +
                "\t},\n" +
                "{\n" +
                "\t\"xm\": \"姓名\",\n" +
                "\t\t\"sfzhm\": \"身份证号码\",\n" +
                "\t\t\"jcrq\": \"接触日期\",\n" +
                "\t\t\"jclx\": \"接触类型\"\n" +
                "\t}\n" +
                "]\n" +
                "}\n";
//        return "{\"code\": \"200\",\"msg\": \"查询成功\",\"iscontactperson\": \"是否为可能密切接触者\",\"data\":[]}\n";
//    return "{\"data\":[],\"code\":\"200\",\"msg\":\"对不起没有查询到相关信息\",\"isContactPerson\":\"0\"}";
    }

    /**
     * 模拟从确诊和疑似病例接口中查询到的数据
     * @return
     */
    public static String mockQzhysblList(){
        return "{\n" +
                "    \"code\": \"200\",\n" +
                "    \"msg\": \"查询成功\",\n" +
                "\"data\":\n" +
                "{\n" +
                "\t\"xm\": \"患者姓名\",\n" +
                "\t\t\"sfzhm\": \"患者身份证号码\",\n" +
                "\t\t\"fbsj\": \"发病日期\",\n" +
                "\t\t\"qzsj\": \"确诊日期\",\n" +
                "\t\t\"bllx\": \"病历类型\"\n" +
                "\t}\n" +
                "}\n";
//        return "{\"code\": \"200\",\"msg\": \"查询成功\",\"data\":{}}";
//        return "{\"data\":{},\"code\":\"200\",\"msg\":\"对不起没有查询到相关信息\",\"isContactPerson\":null}";
    }
}

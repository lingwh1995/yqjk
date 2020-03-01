package com.dragonsoft.yqjk.service;

import com.alibaba.fastjson.JSON;
import com.dragonsoft.yqjk.dao.dids.IDidsDao;
import com.dragonsoft.yqjk.dao.yqjk.IYqjkParamDao;
import com.dragonsoft.yqjk.entity.YqjkRMIParam;
import com.dragonsoft.yqjk.entity.YqjkQueryCondition;
import com.dragonsoft.yqjk.entity.YqjkToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 疫情监控
 */
@Service
@Transactional
public class YqjkService implements IYqjkService {

    @Autowired
    private IYqjkParamDao yqjkParamDao;

    @Autowired
    private IDidsDao didsDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private static final String TOKEN_KEY = "TOKEN_KEY";
    private static final String FWLX = "1";

    private static final Logger logger = LoggerFactory.getLogger(YqjkService.class);

    @Override
    public synchronized String getYqDataFromBu(YqjkQueryCondition yqjkQueryCondition) throws Exception {
        logger.info("--------------------------------------------------------");
        logger.info("前台传递过来的请求信息:"+yqjkQueryCondition);
        YqjkRMIParam yqjkRMIParam = yqjkParamDao.findListByType(yqjkQueryCondition.getType());
        YqjkToken yqjkToken = getYqjkToken(yqjkRMIParam);
//        YqjkToken yqjkToken = MockData.mockGetYqjkToken(yqjkRMIParam);
        String apikenUrl = yqjkRMIParam.getApiUrl();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", yqjkRMIParam.getApiContentType());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = dateFormat.format(new Date());
        requestHeaders.add("processNum", timestamp);
        requestHeaders.add("stepNum", yqjkRMIParam.getStepNum());
        requestHeaders.add("receiveAppId", yqjkRMIParam.getReceiveAppId());
        requestHeaders.add("invokingAppId", yqjkRMIParam.getInvokingAppId());
        requestHeaders.add("access_token", yqjkToken.getAccess_token());
        Map<String, String> requestBody = new HashMap<String, String>(8);
        requestBody.put("qqdwdm", yqjkQueryCondition.getQqdwdm());
        requestBody.put("qqdwmc", yqjkQueryCondition.getQqdwmc());
        requestBody.put("qqr", yqjkQueryCondition.getQqr());
        requestBody.put("qqrsfzhm", yqjkQueryCondition.getQqrsfzhm());
        requestBody.put("cxsy", yqjkQueryCondition.getCxsy());
        requestBody.put("xm", yqjkQueryCondition.getXm());
        requestBody.put("sfzhm", yqjkQueryCondition.getSfzhm());
        requestBody.put("fwlx", FWLX);
        logger.info("--------------------------------------------------------");
        logger.info("请求header信息:"+requestHeaders.toString());
        logger.info("请求参数信息:"+requestBody.toString());
        logger.info("--------------------------------------------------------");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(requestBody, requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apikenUrl, requestEntity, String.class);
        logger.info("请求返回结果:"+responseEntity.getBody());
        logger.info("--------------------------------------------------------");
        return responseEntity.getBody();
//        return MockData.getMockRMIEmpty(yqjkQueryCondition);
    }

    @Override
    public Map<String, String> getUserInfo(String userId) {
        return didsDao.getUserInfo(userId);
    }

    /**
     * 此方法串行化调用
     * 获取Token
     * @param yqjkParam
     * @return
     */
    private YqjkToken getYqjkToken(YqjkRMIParam yqjkParam) throws Exception{
        YqjkToken token = null;
        ValueOperations<String,YqjkToken> valueOperations = redisTemplate.opsForValue();
        if(valueOperations.get(TOKEN_KEY) == null){
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Content-Type", yqjkParam.getTokenContentType());
            Map<String, String> params = new HashMap<String, String>();
            params.put("client_id", yqjkParam.getClientId());
            params.put("client_secret",yqjkParam.getClientSecret());
            params.put("grant_type", yqjkParam.getGrantType());
            HttpEntity requestEntity = new HttpEntity(params, requestHeaders);
            logger.info("---------------------------开始获取token-----------------------------");
            String tokenJson = restTemplate.postForObject(yqjkParam.getTokenUrl(), requestEntity, String.class);
            System.out.println("获取到的token的值:"+tokenJson);
            logger.info("---------------------------结束获取token-----------------------------");
            token = JSON.parseObject(tokenJson, YqjkToken.class);
            //将token获取到的Token值缓存在Redis中,并设置过期时间,单位为秒
            valueOperations.set(TOKEN_KEY,token,token.getExpires_in() - 100, TimeUnit.SECONDS);
        }else{
            token = valueOperations.get(TOKEN_KEY);
        }
        return token;
    }

}

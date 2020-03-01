package com.dragonsoft.yqjk.aop;
import	java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dragonsoft.yqjk.dao.yqjk.IYqjkAuditLogDao;
import com.dragonsoft.yqjk.entity.YqjkQueryCondition;
import com.dragonsoft.yqjk.enumresources.RMIStatus;
import com.dragonsoft.yqjk.enumresources.YqInterfaceTypeEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 审计日志切面
 * @author ronin
 */
@Aspect
@Component
public class AuditLogAspect implements Ordered {

    @Autowired
    private IYqjkAuditLogDao yqjkAuditLogDao;

    private static Logger logger = LoggerFactory.getLogger(AuditLogAspect.class);

    /**默认远程调用发生异常时没有返回值,这里我们自定义一个默认的返回值*/
    private final String DEFAULT_YCDYYC_VALUE = "远程调用异常,无法获取返回值";

    /**
     * 定义切入点，切入点为com.example.demo.aop.AopController中的所有函数
     *通过@Pointcut注解声明频繁使用的切点表达式
     */
    @Pointcut("execution(public * com.dragonsoft.yqjk.service.YqjkService.getYqDataFromBu(..)))")
    public void getYqDataFromBuPointcut(){

    }

    /**
     * 在连接点执行之前执行时抛出异常的通知
     * @param joinPoint 连接点
     * @param e 调用失败时抛出的异常
     */
    @AfterThrowing(value="getYqDataFromBuPointcut()",throwing = "e")
    public void afterThrowingGetYqDataFromBu(JoinPoint joinPoint,Exception e){
        Object[] args = joinPoint.getArgs();
        YqjkQueryCondition yqjkQueryCondition = (YqjkQueryCondition)args[0];
        Map<String,Object> auditLogParams = new HashMap<String, Object> () ;
        //远程传入的调用参数
        auditLogParams.put("rmiParams",yqjkQueryCondition);
        //远程调用返回值
        auditLogParams.put("rmiReturnValue",DEFAULT_YCDYYC_VALUE);
        //远程调用状态吗
        auditLogParams.put("rmiStatusCode",RMIStatus.YCDYYC.getCode());
        yqjkAuditLogDao.auditLogGetYqDataFromBu(auditLogParams);
        String yqInterfaceType = "";
        for (YqInterfaceTypeEnum yqInterfaceTypeEnum : YqInterfaceTypeEnum.values()) {
            if(yqjkQueryCondition.getType().equals(yqInterfaceTypeEnum.getCode())){
                yqInterfaceType = yqInterfaceTypeEnum.getDesc();
            }
        }
        logger.info("调用yqDataFromBuAuditLog()时发生了异常,调用的接口类型:" + yqInterfaceType);
    }

    /**
     * 在连接点执行之后正常返回值的通知
     * @param joinPoint 连接点
     * @param jsonResult 调用成功时返回的json字符串
     */
    @AfterReturning(value="getYqDataFromBuPointcut()",returning = "jsonResult")
    public void afterReturningGetYqDataFromBu(JoinPoint joinPoint,String jsonResult){
        Object[] args = joinPoint.getArgs();
        YqjkQueryCondition yqjkQueryCondition = (YqjkQueryCondition)args[0];
        JSONObject jsonResultObj = (JSONObject)JSON.parse(jsonResult);
        Map<String,Object> auditLogParams = new HashMap<String, Object> () ;
        //远程传入的调用参数
        auditLogParams.put("rmiParams",yqjkQueryCondition);
        //远程调用返回值
        auditLogParams.put("rmiReturnValue",jsonResult);
        Object data = jsonResultObj.get("data");
        /**
         * 可能密切接触接口返回JSON格式:
         * 确诊和疑似病例接口返回JSON格式:
         */
        if(data instanceof JSONObject){
            if(((JSONObject) data).isEmpty()){
                //暂无比中结果:RMIStatus.ZWPPJL
                //远程调用状态吗
                auditLogParams.put("rmiStatusCode",RMIStatus.ZWPPJL.getCode());
                yqjkAuditLogDao.auditLogGetYqDataFromBu(auditLogParams);
            }else{
                //调用结果比中:RMIStatus.DYJGBZ
                auditLogParams.put("rmiStatusCode",RMIStatus.DYJGBZ.getCode());
                yqjkAuditLogDao.auditLogGetYqDataFromBu(auditLogParams);
            }
        }
        if(data instanceof JSONArray){
            if(((JSONArray) data).size() == 0){
                //暂无比中结果:RMIStatus.ZWPPJL
                //远程调用状态吗
                auditLogParams.put("rmiStatusCode",RMIStatus.ZWPPJL.getCode());
                yqjkAuditLogDao.auditLogGetYqDataFromBu(auditLogParams);
            }else{
                //调用结果比中:RMIStatus.DYJGBZ
                auditLogParams.put("rmiStatusCode",RMIStatus.DYJGBZ.getCode());
                yqjkAuditLogDao.auditLogGetYqDataFromBu(auditLogParams);
            }
        }
        String yqInterfaceType = "";
        for (YqInterfaceTypeEnum yqInterfaceTypeEnum : YqInterfaceTypeEnum.values()) {
            if(yqjkQueryCondition.getType().equals(yqInterfaceTypeEnum.getCode())){
                yqInterfaceType = yqInterfaceTypeEnum.getDesc();
            }
        }
        logger.info("调用yqDataFromBuAuditLog()正常,调用的接口类型:" + yqInterfaceType);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

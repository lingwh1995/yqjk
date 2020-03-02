package com.dragonsoft.yqjk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dragonsoft.yqjk.entity.ResponseMessage;
import com.dragonsoft.yqjk.entity.YqjkQueryCondition;
import com.dragonsoft.yqjk.enumresources.YqInterfaceTypeEnum;
import com.dragonsoft.yqjk.enumresources.YqjkInterfaceReturnEnum;
import com.dragonsoft.yqjk.service.IYqjkService;
import com.dragonsoft.yqjk.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 疫情监控Controller
 * @author ronin
 */
@Controller
public class YqjkController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IYqjkService yqjkService;

    /**批量查询时间间隔*/
    @Value("${yqjk.batchQueryTimeInterval}")
    private Integer batchQueryTimeInterval;

    /**批量查询刷新进度条时间间隔*/
    @Value("${yqjk.batchQueryRateRefreshTimeInterval}")
    private Integer batchQueryRateRefreshTimeInterval;

    /**在Redis进行缓存的时候,疫情监控模块的KEY的前缀*/
    private final String YQJK_GLOBAL_KEYPREFIX = "yqjk:";
    /**在Redis进行缓存的时候,疫情监控模块中缓存的查询条件数据的KEY前缀*/
    private final String YQJK_CONDITION_KEYPREFIX = "condition:";
    /**在Redis进行缓存的时候,疫情监控模块中缓存的导出进度的KEY前缀*/
    private final String YQJK_EXPORT_RATE_KEYPREFIX = "export_rate:";
    /**用户信息前缀*/
    private final String YQJK_USER_INFO_KEYPREFIX = "user_info:";
    private final String YQJK_EXPORT_RATE_KEY_BASE = YQJK_GLOBAL_KEYPREFIX + YQJK_EXPORT_RATE_KEYPREFIX ;
    private final String YQJK_CONDITION_KEY_BASE = YQJK_GLOBAL_KEYPREFIX + YQJK_CONDITION_KEYPREFIX ;
    private final String YQJK_USER_INFO_KEY_BASE = YQJK_GLOBAL_KEYPREFIX + YQJK_USER_INFO_KEYPREFIX;

    private static final Logger logger = LoggerFactory.getLogger(YqjkController.class);
    /**
     * 跳转到疫情监控列表展示页面
     * @return
     */
    @RequestMapping("/to-yqjk-list")
    public String toYqjkList(@RequestParam("userId") String userId,Model model,
                             HttpServletRequest request){
        Map<String, String> userInfo = (Map<String, String>)request.getSession().getAttribute(userId);
        //设置Redis中存储的用户信息3个小时后过期
        ValueOperations<String,Map<String,String>> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(YQJK_USER_INFO_KEY_BASE+userId,userInfo,1 * 360, TimeUnit.MINUTES);
        model.addAttribute("userId",userId);
        return "yqjk/yqjk-list";
    }

    /**
     * 跳转到批量下载页面
     * @return
     */
    @RequestMapping("/batch-download")
    public String batchDownload(@RequestParam("userId") String userId,Model model){
        model.addAttribute("userId", userId);
        return "yqjk/batch-download";
    }

    /**
     * 可能密切接触者查询服务
     * @return
     */
    @ResponseBody
    @RequestMapping("/knmqjcz-list")
    public String getKnmqjczList(YqjkQueryCondition yqjkQueryCondition){
        String responseMessage = null;
        JSONObject message = new JSONObject();
        try {
            ValueOperations<String,Map<String,String>> valueOperations = redisTemplate.opsForValue();
            String userInfoKey = YQJK_USER_INFO_KEY_BASE + yqjkQueryCondition.getUserId();
            Map<String,String> userInfo = valueOperations.get(userInfoKey);
            yqjkQueryCondition.setQqdwdm(userInfo.get("DWID"));
            yqjkQueryCondition.setQqdwmc(userInfo.get("DWMC"));
            yqjkQueryCondition.setQqr(userInfo.get("XM"));
            yqjkQueryCondition.setQqrsfzhm(userInfo.get("SFZH"));
            yqjkQueryCondition.setType(YqInterfaceTypeEnum.KNMQJCZ.getCode());
            responseMessage = yqjkService.getYqDataFromBu(yqjkQueryCondition);
        }catch(Exception e) {
            logger.info(e.getMessage());
            message.put("code",ResponseMessage.SESSION_TIMEOUT_CODE);
            responseMessage = message.toJSONString();
        }
//        responseMessage = MockData.mockKnmqjczList();
        return responseMessage;
    }

    /**
     * 确诊和疑似病例查询服务
     * @param yqjkQueryCondition 封装了查询条件是实体
     * @return
     */
    @ResponseBody
    @RequestMapping("/qzhysbl-list")
    public String getQzhysblList(YqjkQueryCondition yqjkQueryCondition){
        String responseMessage = null;
        JSONObject message = new JSONObject();
        try {
            ValueOperations<String,Map<String,String>> valueOperations = redisTemplate.opsForValue();
            String userInfoKey = YQJK_USER_INFO_KEY_BASE + yqjkQueryCondition.getUserId();
            Map<String,String> userInfo = valueOperations.get(userInfoKey);
            yqjkQueryCondition.setQqdwdm(userInfo.get("DWID"));
            yqjkQueryCondition.setQqdwmc(userInfo.get("DWMC"));
            yqjkQueryCondition.setQqr(userInfo.get("XM"));
            yqjkQueryCondition.setQqrsfzhm(userInfo.get("SFZH"));
            yqjkQueryCondition.setType(YqInterfaceTypeEnum.QZHYSBL.getCode());
            responseMessage = yqjkService.getYqDataFromBu(yqjkQueryCondition);
        } catch (Exception e) {
            logger.info(e.getMessage());
            message.put("code",ResponseMessage.SESSION_TIMEOUT_CODE);
            responseMessage = message.toJSONString();
        }
//        responseMessage = MockData.mockQzhysblList();
        return responseMessage;
    }

    /**
     * 功能描述：解析Excel接口
     * @param file MultipartFile
     * @return Object
     */
    @ResponseBody
    @RequestMapping(value = "/parse-excel",method = RequestMethod.POST)
    public ResponseMessage parseExcel(MultipartFile file, @RequestParam("identifier") String identifier) {
        ResponseMessage responseMessage = null;
        try{
            ValueOperations<String, List<Map<String, String>>> valueOperations = redisTemplate.opsForValue();
            if(StringUtils.isEmpty(identifier)){
                String randomIndentifier = CommonUtils.getUUID();
                String YQJK_CONDITION_KEY =  YQJK_CONDITION_KEY_BASE + randomIndentifier;
                //解析当前用户上传的Excel,并且将解析出来的查询数据缓存到Redis中
                List<Map<String, String>> conditions = ExcelOperator.importDataFromExcel(file);
                valueOperations.set(YQJK_CONDITION_KEY ,conditions,1 * 30, TimeUnit.MINUTES);
                Map<String,String> task = new HashMap<String,String>();
                task.put("identifier",YQJK_CONDITION_KEY);
                task.put("conditionSize", String.valueOf(conditions.size()));
                task.put("fileName",file.getOriginalFilename());
                task.put("fileSize", String.valueOf(file.getSize()));
                task.put("message","此Excel为用户初次上传!");
                task.put("batchQueryRateRefreshTimeInterval",batchQueryRateRefreshTimeInterval+"");
                responseMessage = ResponseMessage.success(JSON.toJSONString(task));
            }
        }catch(Exception e){
            responseMessage = ResponseMessage.fail(e.getMessage());
        }
        return responseMessage;
    }

    /**
     * 获取可能密切接触下载进度
     * @param identifier
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/export-excel-rate-knmqjcz",method = RequestMethod.GET)
    public String exportknmqjczRate(@RequestParam("identifier") String identifier) {
        String[] split = identifier.split(":");
        String YQJK_EXPORT_RATE_KEY_KNMQJCZ = YQJK_EXPORT_RATE_KEY_BASE + split[2]+":"+
                YqInterfaceTypeEnum.KNMQJCZ.getCode();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Integer currentRate = (Integer)valueOperations.get(YQJK_EXPORT_RATE_KEY_KNMQJCZ);
        return currentRate != null ? String.valueOf(currentRate) : String.valueOf(0);
    }

    /**
     * 获取确诊和疑似病例下载进度
     * @param identifier
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/export-excel-rate-qzhysbl",method = RequestMethod.GET)
    public String exportQzhysblRate(@RequestParam("identifier") String identifier) {
        String[] split = identifier.split(":");
        String YQJK_EXPORT_RATE_KEY_QZHYSBL = YQJK_EXPORT_RATE_KEY_BASE + split[2]+":"+
                YqInterfaceTypeEnum.QZHYSBL.getCode();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Integer currentRate = (Integer)valueOperations.get(YQJK_EXPORT_RATE_KEY_QZHYSBL);
        return currentRate != null ? String.valueOf(currentRate) : String.valueOf(0);
    }

    /**
     * 导出可能密切接触者
     * @param response
     * @param identifier
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/export-excel-knmqjcz",method = RequestMethod.GET)
    public void exportKnmqjczExcel(HttpServletResponse response,
                                              @RequestParam("identifier") String identifier,@RequestParam("userId") String userId){
        try {
            ExcelApi xlsExcelApi = new XlsExcelApi();
            String title = "SHEET-1";
            String fileName = YqInterfaceTypeEnum.KNMQJCZ.getFileName();
            List<Object[]> dataList = new ArrayList< Object [] >();
            String[] excelHeader = {"姓名","身份证号","查询事由","姓名","身份证号码","接触日期","接触类型","是否为可能密切接触者"};
            dataList.add(excelHeader);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            List<Map<String, String>> conditions = (List<Map<String, String>>)valueOperations.get(identifier);
            //导出进度的key
            String YQJK_EXPORT_RATE_KEY = YQJK_EXPORT_RATE_KEY_BASE + (identifier.split(":"))[2]+":"+
                    YqInterfaceTypeEnum.KNMQJCZ.getCode();
            for(int i=0;i<conditions.size(); i++){
                YqjkQueryCondition yqjkQueryCondition = new YqjkQueryCondition();
                //设置用户id
                yqjkQueryCondition.setUserId(userId);
                //拼接请求参数信息
                yqjkQueryCondition.setXm(conditions.get(i).get("xm"));
                yqjkQueryCondition.setSfzhm(conditions.get(i).get("sfzhm"));
                yqjkQueryCondition.setCxsy(conditions.get(i).get("cxsy"));
                //拼接用户信息
                String userInfoKey = YQJK_USER_INFO_KEY_BASE + userId;
                Map<String,String> userInfo = (Map<String,String>)valueOperations.get(userInfoKey);
                yqjkQueryCondition.setQqdwdm(userInfo.get("DWID"));
                yqjkQueryCondition.setQqdwmc(userInfo.get("DWMC"));
                yqjkQueryCondition.setQqr(userInfo.get("XM"));
                yqjkQueryCondition.setQqrsfzhm(userInfo.get("SFZH"));
                //拼接类型
                yqjkQueryCondition.setType(YqInterfaceTypeEnum.KNMQJCZ.getCode());
                logger.info("----------------------------------------------------------------------------------------------");
                logger.info("conditions:"+conditions);
                logger.info("yqjkQueryCondition:"+yqjkQueryCondition);
                JSONObject result = (JSONObject) JSON.parse(this.getKnmqjczList(yqjkQueryCondition));
                logger.info("this.getKnmqjczList(yqjkQueryCondition):"+JSON.toJSONString(result));
                logger.info("result:"+result);
                if(result.get("code").equals(YqjkInterfaceReturnEnum.CXCG.getCode())){
                    JSONArray users = (JSONArray)result.get("data");
                    logger.info("users:"+users.toJSONString());
                    if(users.size() >0) {
                        String[] excelRow = new String[excelHeader.length];
                        for (int j = 0; j < users.size(); j++) {
                            JSONObject user = (JSONObject) users.get(j);
                            excelRow[0] = yqjkQueryCondition.getXm();
                            excelRow[1] = yqjkQueryCondition.getSfzhm();
                            excelRow[2] = yqjkQueryCondition.getCxsy();
                            excelRow[3] = String.valueOf(user.get("xm")).equals("null") ? "" : String.valueOf(user.get("xm")).trim();
                            excelRow[4] = String.valueOf(user.get("sfzhm")).equals("null") ? "" : String.valueOf(user.get("sfzhm")).trim();
                            excelRow[5] = String.valueOf(user.get("jcrq")).equals("null") ? "" : String.valueOf(user.get("jcrq")).trim();
                            excelRow[6] = String.valueOf(user.get("jclx")).equals("null") ? "" : String.valueOf(user.get("jclx")).trim();
                            excelRow[7] = result.get("isContactPerson").equals("1") ? "是" : "否";
                        }
                        dataList.add(excelRow);
                    }else{
                        String[] emptyExcelBody = {"无匹配结果","-","-","-","-","-","-","-"};
                        dataList.add(emptyExcelBody);
                    }
                }
                valueOperations.set(YQJK_EXPORT_RATE_KEY,(i+1),1 *10,TimeUnit.MINUTES);
                //设置批量下载间隔时间
                Thread.sleep(batchQueryTimeInterval);
            }
            logger.info("dataList:"+dataList);
            logger.info("----------------------------------------------------------------------------------------------");
            xlsExcelApi.exportDataToExcel(title,dataList,fileName,response);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出确诊和疑似病例
     * @param response
     * @param identifier
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/export-excel-qzhysbl",method = RequestMethod.GET)
    public void exportExcel(HttpServletResponse response,
                                       @RequestParam("identifier") String identifier,@RequestParam("userId") String userId){
        try{
            ExcelApi xlsExcelApi = new XlsExcelApi();
            String title = "SHEET-1";
            String fileName = YqInterfaceTypeEnum.QZHYSBL.getFileName();
            List<Object[]> dataList = new ArrayList< Object [] >();
            ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
            List<Map<String, String>> conditions = (List<Map<String, String>>) valueOperations.get(identifier);
            //导出进度的key
            String YQJK_EXPORT_RATE_KEY = YQJK_EXPORT_RATE_KEY_BASE + (identifier.split(":"))[2]+":"+ YqInterfaceTypeEnum.QZHYSBL.getCode();
            String[] excelHeader = {"姓名","身份证号","查询事由","患者姓名","身份证号码","发病日期","确诊日期","病例类型"};
            dataList.add(excelHeader);
            for(int i=0;i<conditions.size();i++){
                YqjkQueryCondition yqjkQueryCondition = new YqjkQueryCondition();
                //设置用户id
                yqjkQueryCondition.setUserId(userId);
                //拼接请求参数信息
                yqjkQueryCondition.setXm(conditions.get(i).get("xm"));
                yqjkQueryCondition.setSfzhm(conditions.get(i).get("sfzhm"));
                yqjkQueryCondition.setCxsy(conditions.get(i).get("cxsy"));
                //拼接用户信息
                String userInfoKey = YQJK_USER_INFO_KEY_BASE + userId;
                Map<String,String> userInfo = (Map<String,String>)valueOperations.get(userInfoKey);
                yqjkQueryCondition.setQqdwdm(userInfo.get("DWID"));
                yqjkQueryCondition.setQqdwmc(userInfo.get("DWMC"));
                yqjkQueryCondition.setQqr(userInfo.get("XM"));
                yqjkQueryCondition.setQqrsfzhm(userInfo.get("SFZH"));
                //拼接类型
                yqjkQueryCondition.setType(YqInterfaceTypeEnum.QZHYSBL.getCode());
                logger.info("----------------------------------------------------------------------------------------------");
                logger.info("conditions:"+conditions);
                logger.info("yqjkQueryCondition:"+yqjkQueryCondition);
                JSONObject result = (JSONObject) JSON.parse(this.getQzhysblList(yqjkQueryCondition));
                logger.info("this.getQzhysblList(yqjkQueryCondition):"+JSON.toJSONString(result));
                logger.info("result:"+result);
                if(result.get("code").equals(YqjkInterfaceReturnEnum.CXCG.getCode())){
                    JSONObject user = (JSONObject)result.get("data");
                    logger.info("user:"+user.toJSONString());
                    if(user.size() > 0) {
                        String[] excelBody = {
                                yqjkQueryCondition.getXm(),
                                yqjkQueryCondition.getSfzhm(),
                                yqjkQueryCondition.getCxsy().trim(),
                                String.valueOf(user.get("xm")).equals("null") ? "" : String.valueOf(user.get("xm")).trim(),
                                String.valueOf(user.get("sfzhm")).equals("null") ? "" : String.valueOf(user.get("sfzhm")).trim(),
                                String.valueOf(user.get("fbsj")).equals("null") ? "" : String.valueOf(user.get("fbsj")).trim(),
                                String.valueOf(user.get("qzsj")).equals("null") ? "" : String.valueOf(user.get("qzsj")).trim(),
                                String.valueOf(user.get("bllx")).equals("null") ? "" : String.valueOf(user.get("bllx")).trim(),
                        };
                        dataList.add(excelBody);
                    }else{
                        String[] emptyExcelBody = {"无匹配结果","-","-","-","-","-","-","-"};
                        dataList.add(emptyExcelBody);
                    }
                }
                valueOperations.set(YQJK_EXPORT_RATE_KEY,(i+1),1 *10,TimeUnit.MINUTES);
                //设置批量下载间隔时间
                Thread.sleep(batchQueryTimeInterval);
            }
            logger.info("dataList:"+dataList);
            logger.info("----------------------------------------------------------------------------------------------");
            xlsExcelApi.exportDataToExcel(title,dataList,fileName,response);
        }catch (Exception e) {
           e.printStackTrace();
        }
    }

    /**
     * 清除Redis中缓存的用于统计下载进度的定时器
     * @return
     */
    @ResponseBody
    @RequestMapping("/clear-rate-data")
    public ResponseMessage clearQzhysblRateDataAtRedis(@RequestParam("identifier") String identifier, @RequestParam("type") String type){
        ResponseMessage responseMessage = null;
        try {
            String YQJK_EXPORT_RATE_KEY = YQJK_EXPORT_RATE_KEY_BASE + (identifier.split(":"))[2]+":"+type;
            redisTemplate.delete(YQJK_EXPORT_RATE_KEY);
            responseMessage = ResponseMessage.success("清除Redis中缓存的用于统计下载进度的定时器成功");
        } catch (Exception e) {
            responseMessage = ResponseMessage.success("清除Redis中缓存的用于统计下载进度的定时器失败");
            e.printStackTrace();
        }
        return responseMessage;
    }

}

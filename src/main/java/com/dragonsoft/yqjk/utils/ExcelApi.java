package com.dragonsoft.yqjk.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author ronin
 */
public interface ExcelApi {

    /**
     * 从用户上传的Excel中导入查询条件数据
     * @param file
     * @return
     */
    List<Map<String,String>> importDataFromExcel(MultipartFile file) throws Exception;

    /**
     * 把数据导出到Excel中
     * @param title Sheet名称
     * @param dataList 数据
     * @param fileName     文件名称
     * @param response
     */
    void exportDataToExcel(String title, List<Object[]> dataList, String fileName, HttpServletResponse response) throws Exception;
}

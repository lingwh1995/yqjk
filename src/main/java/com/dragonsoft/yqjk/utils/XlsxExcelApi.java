package com.dragonsoft.yqjk.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 操作后缀名为xlsx的Excel
 * @author ronin
 */
public class XlsxExcelApi implements ExcelApi {

    /**
     * TODO 目前为空实现
     * 从用户上传的Excel中导入查询条件数据,后缀为xlsx
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> importDataFromExcel(MultipartFile file) throws Exception {
        return null;
    }

    /**
     * 把数据导出到Excel中
     *
     * @param title        Sheet名称
     * @param dataList     数据
     * @param fileName     文件名称
     * @param response 流
     */
    @Override
    public void exportDataToExcel(String title, List<Object[]> dataList, String fileName, HttpServletResponse response) throws Exception {

    }
}

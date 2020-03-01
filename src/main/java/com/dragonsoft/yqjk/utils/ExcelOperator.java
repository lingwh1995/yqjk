package com.dragonsoft.yqjk.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author ronin
 */
public class ExcelOperator {

    /**excel2007以上版本的excel文件的后缀名*/
    private static final String XLS_SUFFIX = "xls";
    /**excel2003版本的excel文件的后缀名*/
    private static final String XLSX_SUFFIX = "xlsx";
    /**
     * 从用户上传的Excel中导入数据
     * @param file
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> importDataFromExcel(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!"xlsx".equals(suffix) && !"xls".equals(suffix)) {
            System.out.println("请传入excel文件");
        }
        ExcelApi excelApi = null;
        if (XLSX_SUFFIX.equals(suffix)) {
            excelApi = new XlsxExcelApi();
        }
        if (XLS_SUFFIX.equals(suffix)) {
            excelApi = new XlsExcelApi();
        }
        return excelApi.importDataFromExcel(file);
    }
}

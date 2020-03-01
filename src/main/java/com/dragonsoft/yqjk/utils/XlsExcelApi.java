package com.dragonsoft.yqjk.utils;

import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author ronin
 */
public class XlsExcelApi implements ExcelApi {

    /**
     * 从用户上传的Excel中导入查询条件数据,后缀为xls
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> importDataFromExcel(MultipartFile file) throws Exception {
        List<Map<String,String>> result = new ArrayList<Map<String,String>>();
        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = workbook.getSheetAt(0);
        for (int rowIndex = 1; rowIndex < sheet.getLastRowNum(); rowIndex++) {
            HashMap<String, String> map = new HashMap<String, String>();
            HSSFRow row = sheet.getRow(rowIndex);
            //整行都为空去掉
            if(row==null) {
                continue;
            }
            for (int cellIndex = 0; cellIndex < 3; cellIndex++) {
                HSSFCell cell = row.getCell(cellIndex);
                // 避免空指针异常
                if (cell == null) {
                    continue;
                }
                // 判断格式
                switch (cell.getCellTypeEnum()){
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        if(cellIndex == 1) {
                            cellValue = cell.getStringCellValue().
                                    replace("【", "")
                                    .replace("】", "").trim();
                        }
                        map.put(CommonUtils.getKey(cellIndex),cellValue);
                        break;
                    case NUMERIC:
                        DecimalFormat decimalFormat = new DecimalFormat("0");
                        map.put(CommonUtils.getKey(cellIndex),decimalFormat.format(cell.getNumericCellValue()));
                        break;
                    case FORMULA:
                        map.put(CommonUtils.getKey(cellIndex),String.valueOf(cell.getCellFormula()));
                        break;
                    case BOOLEAN:
                        map.put(CommonUtils.getKey(cellIndex), String.valueOf(cell.getBooleanCellValue()));
                        break;
                    case BLANK:
                        map.put(CommonUtils.getKey(cellIndex), " ");
                        break;
                    case ERROR:
                        map.put(CommonUtils.getKey(cellIndex), "非法字符");
                        break;
                    default:
                        map.put(CommonUtils.getKey(cellIndex), "未知类型");
                        break;
                }
                //判断必填项是否为空
                if(cellIndex == 0 || cellIndex == 1){
                    if(String.valueOf(map.get(CommonUtils.getKey(cellIndex))).trim().equals("null")){
                        StringBuilder message = new StringBuilder();
                        message.append("您导入的Excel文件"+file.getOriginalFilename()+"中第"+rowIndex+"行数据格式有误,");
                        if(cellIndex == 0){
                            message.append("姓名不能为空！");
                        }
                        if(cellIndex == 1){
                            message.append("身份证号不能为空！");
                        }
                        throw new RuntimeException(message.toString());
                    }
                }
                //判断身份证是否合法
                if(cellIndex == 1){
                    boolean isMatches = CommonUtils.isIDNumber(map.get(CommonUtils.getKey(cellIndex)));
                    if(! isMatches){
                        StringBuilder message = new StringBuilder();
                        message.append("您导入的Excel文件"+file.getOriginalFilename()+"中第"+rowIndex+"行数据格式有误,");
                        message.append("身份证号不合法！");
                        throw new RuntimeException(message.toString());
                    }
                }
            }
            result.add(map);
        }
        return result;
    }

    /**
     * 把数据导出到Excel中
     *
     * @param title  Sheet名称
     * @param dataList   数据
     * @param fileName     文件名称
     * @param response 流
     */
    @Override
    public void exportDataToExcel(String title, List<Object[]> dataList, String fileName, HttpServletResponse response) throws Exception {
        OutputStream out = null;
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(title);
            for(int i=0;i<dataList.size(); i++){
                HSSFRow HSSRows = sheet.createRow(i);
                Object[] HSSCols = dataList.get(i);
                for(int j=0;j<HSSCols.length; j++){
                    HSSFCell cell = HSSRows.createCell(j);
                    cell.setCellValue(String.valueOf(HSSCols[j]));
                }
            }
            autoColWidth(sheet,dataList.size());
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            out = response.getOutputStream();
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Excel表格自动适应宽度
     * @param sheet 要操作的sheet对象
     * @param rowSize 列的数目
     */
    private void autoColWidth(HSSFSheet sheet,int rowSize) {
        for (int colNum = 0; colNum < rowSize; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }
    }

}

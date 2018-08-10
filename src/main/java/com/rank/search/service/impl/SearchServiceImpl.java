package com.rank.search.service.impl;

import com.rank.search.entity.KeyWordRank;
import com.rank.search.util.DataFormatStatus;
import com.rank.search.service.SearchService;
import com.rank.search.util.HttpUtil;
import com.rank.search.util.JsoupHtmlParser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/8/10 16:13
 */
@Service
public class SearchServiceImpl implements SearchService {

    private static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private static String nodeFlag = "dayi.org.cn";

    private static String[] pages = {"1","2","3","4","5"}; //搜索前五页

    private static String[] rowName = {"关键词", "搜狗PC端排名", "搜狗移动端排名", "360移动端排名"};

    private static String rankSosoMUri = "http://m.sogou.com/web/search/ajax_query.jsp?keyword=%s&s_from=pagenext&p=%s&ie=utf8";

    private static String rankSosoWUri = "http://www.sogou.com/sogou?query=%s&page=%s&ie=utf8";

    private static String rank360Uri = "https://m.so.com/s?q=%s&srcg=home_next&src=own_pager_next&pn=%s";

    @Override
    public void rankSearch(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {

        List<String> keyWordList = getKeyWordFromFile(file);
        List<KeyWordRank> rankList = new ArrayList<>();
        if (null != keyWordList && keyWordList.size() > 0) {

            for (String keyWord : keyWordList) {

                Integer rankSom = runSpiderSOSOM(keyWord);
                Integer rankSow = runSpiderSOGOWeb(keyWord);
                Integer rank360 = runSpider360(keyWord);

                KeyWordRank keyWordRank = new KeyWordRank();
                keyWordRank.setKeyword(keyWord);
                keyWordRank.setRankSoso(rankSom);
                keyWordRank.setRankSosoW(rankSow);
                keyWordRank.setRank360(rank360);
                rankList.add(keyWordRank);
            }
        }
        //excel输出文件
        exportRankFile(rankList, request, response);
    }

    @Override
    public void downLoadModel(HttpServletRequest request, HttpServletResponse response){
        String fileName = "关键字上传模板.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("关键字");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("关键字(在此列添加关键字,请不要删除此列)");
        CellStyle cellStyle = cellStyle(workbook, 0);
        cell.setCellStyle(cellStyle);
        sheet.setColumnWidth(0, 20*800);
        try {
            exportWD(request, response, workbook, fileName);
        } catch (IOException e) {
            logger.info("文件下载失败");
            e.printStackTrace();
        }
    }

    /**
     * 导出搜索结果
     */
    private void exportRankFile(List<KeyWordRank> rankList, HttpServletRequest request, HttpServletResponse response) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("搜索引擎排名详情");
        //默认列宽
        sheet.setDefaultColumnWidth(20);
        //样式
        CellStyle headerStyle = cellStyle(workbook, 0);
        CellStyle textStyle = cellStyle(workbook, 1);

        //创建表头
        Row title = sheet.createRow(0);
        for (int i = 0; i < rowName.length; i++) {
            Cell cell = title.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(rowName[i]);
        }
        //内容填充
        for (int i = 0; i < rankList.size(); i++) {
            KeyWordRank keyWordRank = rankList.get(i);
            Row row = sheet.createRow(i + 1);
            Cell cell0 = row.createCell(0);
            cell0.setCellStyle(textStyle);
            cell0.setCellValue(keyWordRank.getKeyword());
            Cell cell1 = row.createCell(1);
            cell1.setCellStyle(textStyle);
            cell1.setCellValue(keyWordRank.getRankSosoW());
            Cell cell2 = row.createCell(2);
            cell2.setCellStyle(textStyle);
            cell2.setCellValue(keyWordRank.getRankSoso());
            Cell cell3 = row.createCell(3);
            cell3.setCellStyle(textStyle);
            cell3.setCellValue(keyWordRank.getRank360());
        }
        //导出数据
        try {
            exportWD(request, response, workbook, "搜索引擎排名详情.xlsx");
        } catch (IOException e) {
            logger.info("数据导出异常");
            e.printStackTrace();
        }
    }

    /**
     * m.soso.com
     */
    private Integer runSpiderSOSOM(String keyWord){
        logger.info("sosom搜索排行查询--开始");
        //指定分条的标识 class = vrResult
        List<String> sosoMClassNameList = new LinkedList<>();
        sosoMClassNameList.add(".vrResult");

        Integer rank = rankSearch(pages, rankSosoMUri, keyWord, sosoMClassNameList, nodeFlag);
        logger.info("sosoM搜索排行查询结果为 " + keyWord +" 排名: " + rank);
        logger.info("sosoM搜索排行查询--结束");
        return rank;
    }

    /**
     * www.sogo.com
     */
    private Integer runSpiderSOGOWeb(String keyWord) {
        logger.info("sosoW搜索排行查询--开始");
        //指定分条的标识 class = vrwrap
        List<String> sosoWClassNameList = new LinkedList<>();
        sosoWClassNameList.add(".vrwrap");

        Integer rank = rankSearch(pages, rankSosoWUri, keyWord, sosoWClassNameList, nodeFlag);
        logger.info("sosoW搜索排行查询结果为 " + keyWord +" 排名: " + rank);
        logger.info("sosoW搜索排行查询--结束");
        return rank;
    }

    /**
     * m.so.com
     */
    private Integer runSpider360(String keyWord) {
        logger.info("360搜索排行查询--开始");
        //指定分条的标识 class = g-card
        List<String> classNameList = new LinkedList<>();
        classNameList.add(".g-card");

        Integer rank = rankSearch(pages, rank360Uri, keyWord, classNameList, nodeFlag);
        logger.info("360搜索排行查询结果为 " + keyWord +" 排名: " + rank);
        logger.info("360搜索排行查询--结束");
        return rank;
    }

    /**
     * 排名搜索的具体实现
     * @param pages 分页码
     * @param rankUri 请求地址
     * @param keyWord 关键词
     * @param classNameList 分条关键字
     * @param nodeFlag dayi.org.cn
     * @return rank 关键字具体排名
     */
    private Integer rankSearch(String[] pages, String rankUri, String keyWord, List<String> classNameList, String nodeFlag){
        Integer rank = 0;
        Boolean flag = false;
        for (String pageNum : pages) {
            logger.info("当前请求页码: " + pageNum);
            logger.info("当前请求路径: " + (String.format(rankUri, keyWord, pageNum)));
            // String htmlSource = HttpUtil.getPageCode(String.format(rankUri, keyWord, pageNum), "utf-8");
            String htmlSource = HttpUtil.getPageCode(String.format(rankUri, keyWord, pageNum), "utf-8");

            logger.info("获取到搜索响应");
            List<String> nodeContents = JsoupHtmlParser.getNodeContentBySelector(htmlSource, classNameList, DataFormatStatus.TagAllContent, true);
            if (htmlSource.contains(nodeFlag)) {
                //包含此内容 查询确定排名
                logger.info("当前页面包含搜索关键词 dayi.org.cn ");
                for (int i = 1; i <= nodeContents.size(); i++) {
                    if (nodeContents.get(i - 1).contains(nodeFlag)) {
                        rank += i;
                        flag = true;
                        break;
                    }
                }
                break;
            } else {
                rank += nodeContents.size();
            }
        }
        if(!flag){
            rank = 0;
        }
        logger.info("关键词: " + keyWord + "搜索排名为: " + rank);
        return rank;
    }

    private List<String> getKeyWordFromFile(MultipartFile file){
        if(Objects.isNull(file)){
            logger.info("文件为空!!");
            return null;
        }
        List<String> keyWordList = new ArrayList<>();
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum < 1) {
                throw new RuntimeException("文件中没有数据");
            }
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue; // 跳过空行
                }
                Cell nameCell = row.getCell(0);
                keyWordList.add(nameCell.getStringCellValue().trim());
            }
        } catch (Exception e) {
            logger.info("错误的文件格式或文件内容异常");
            e.printStackTrace();
        }
        //存储数据
        return keyWordList;
    }

    /**
     * 获取excel 表头样式 或 内容样式 flag 0 表头
     */
    private CellStyle cellStyle(Workbook wb, Integer flag){
        CellStyle cellStyle  = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font cellFont = wb.createFont();
        cellFont.setFontName("微软雅黑");
        cellFont.setFontHeightInPoints((short) 10);
        if(Objects.equals(0, flag)){
            //标题
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            cellFont.setFontHeightInPoints((short) 12);
        }
        cellStyle.setFont(cellFont);
        return cellStyle;
    }

    /**
     * excel 文件导出
     */
    private void exportWD(HttpServletRequest request, HttpServletResponse response, Workbook wb, String filename) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        String userAgent = request.getHeader("User-Agent");
        byte[] bytes = userAgent.contains("MSIE") ? filename.getBytes(): filename.getBytes("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-disposition", "attachment;filename=" + new String(bytes, "ISO-8859-1"));
        response.flushBuffer();
        wb.write(out);
        out.close();
    }

}

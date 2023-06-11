package com.sophia.map.dao;

//import com.philosophy.base.util.NumericUtils;
//import com.philosophy.excel.utils.ExcelUtils;
//import com.sophia.map.entity.CompanyInfo;
//import com.sophia.map.entity.TaxInfo;
import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/*
 * @author lizhe
 * @email lizhe@bdstar.com
 * @create 2023/5/30
 */
@SpringBootTest
@Slf4j
public class LocationInfoDaoTestCase {
    @Resource
    private CompanyInfoDao companyInfoDao;

//    private final ExcelUtils excelUtils = new ExcelUtils();

    private final Path path = Paths.get("D:\\Workspace\\github\\location\\backend\\src\\test\\resources\\企业名单.xlsx");


//    private List<TaxInfo> generatorTaxInfo() {
//        List<TaxInfo> taxInfos = new LinkedList<>();
//        for (int i = 0; i < 3; i++) {
//            TaxInfo taxInfo = new TaxInfo();
//            taxInfo.setYear(Integer.valueOf("202" + i));
//            long taxRevenue = NumericUtils.randomLong(3000, 10000);
//            taxInfo.setTaxRevenue(taxRevenue);
//            Long salesRevenue = taxRevenue * NumericUtils.randomLong(10, 20);
//            taxInfo.setSalesRevenue(salesRevenue);
//            taxInfos.add(taxInfo);
//        }
//        return taxInfos;
//    }


    @Test
    public void TestInsert() throws IOException {
//        Workbook workbook = excelUtils.openWorkbook(path);
//        Sheet sheet = excelUtils.getSheet(workbook, "Sheet1", false);
//        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
//            Row row = sheet.getRow(i);
//            String taxId = excelUtils.getCellValue(row.getCell(0));
//            String taxName = excelUtils.getCellValue(row.getCell(1));
//            String address = excelUtils.getCellValue(row.getCell(2));
//            String longitude = excelUtils.getCellValue(row.getCell(3));
//            String latitude = excelUtils.getCellValue(row.getCell(4));
//            log.info("taxId {}, taxName {}, address {}, longitude {}, latitude {}", taxId, taxName, address, longitude, latitude);
//            CompanyInfo info = new CompanyInfo();
//            info.setSocialCreditCode(taxId);
//            info.setTaxPersonName(taxName);
//            info.setSupervisionUnit("国家税务总局");
//            info.setIndustryName("制造业");
//            info.setLongitude(Float.valueOf(longitude.trim()));
//            info.setLatitude(Float.valueOf(latitude.trim()));
//            info.setTaxInfos(generatorTaxInfo());
//            companyInfoDao.saveAndFlush(info);
//        }
//        excelUtils.close(workbook);

    }
}

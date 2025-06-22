package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.entitys.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {
    private final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);
    private final TenderRepository tenderRepository;

    private static final String[] COLUMN_HEADERS = {
            "ID Тендера",
            "Дата змін",
            "Назва організації",
            "Код ЄДРПОУ",
            "Область",
            "Місто",
            "Адреса",
            "Контактна особа",
            "Електронна пошта",
            "Телефон"
    };

    public ExcelExportService(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public void exportToExcel(String filePath) {
        List<TenderEntity> tenders = tenderRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Тендери");

            createStyledHeaderRow(sheet, workbook);
            fillDataRows(sheet, tenders);
            setupAutoFilter(sheet, tenders.size());
            autoSizeColumns(sheet);

            writeToFile(workbook, filePath);
            logger.info("Excel file created successfully: {}", filePath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to create Excel file", e);
        }
    }

    private void createStyledHeaderRow(Sheet sheet, Workbook workbook) {
        Row header = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int column = 0; column < COLUMN_HEADERS.length; column++) {
            Cell cell = header.createCell(column);
            cell.setCellValue(COLUMN_HEADERS[column]);
            cell.setCellStyle(headerStyle);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);

        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    /**
     * The setCellValue() method handles nulls automatically by creating blank cells,
     * making the NPE safety redundant
     */
    private void fillDataRows(Sheet sheet, List<TenderEntity> tenders) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        int rowNum = 1;

        for (TenderEntity tender : tenders) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(tender.getTenderId());
            row.createCell(1).setCellValue(formatter.format(tender.getDateModified()));

            ProcuringEntityEntity entity = tender.getProcuringEntity();
            row.createCell(2).setCellValue(entity.getName());
            row.createCell(3).setCellValue(entity.getIdentifierId());

            AddressEntity address = entity.getAddress();
            row.createCell(4).setCellValue(address.getRegion());
            row.createCell(5).setCellValue(address.getLocality());
            row.createCell(6).setCellValue(address.getStreetAddress());

            ContactPointEntity contact = entity.getContactPoint();
            row.createCell(7).setCellValue(contact.getName());
            row.createCell(8).setCellValue(contact.getEmail());
            row.createCell(9).setCellValue(contact.getPhone());
        }
    }

    private void setupAutoFilter(Sheet sheet, int dataRowCount) {
        if (dataRowCount > 0) {
            CellRangeAddress autoFilter = new CellRangeAddress(
                    0,
                    dataRowCount,
                    0,
                    COLUMN_HEADERS.length - 1
            );
            sheet.setAutoFilter(autoFilter);
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int column = 0; column < COLUMN_HEADERS.length; column++) {
            sheet.autoSizeColumn(column);
            int currentWidth = sheet.getColumnWidth(column);
            // Set minimum width to prevent too narrow columns
            int minWidth = 50;
            if (currentWidth < minWidth) {
                sheet.setColumnWidth(column, minWidth);
            }
        }
    }

    private void writeToFile(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
    }
}

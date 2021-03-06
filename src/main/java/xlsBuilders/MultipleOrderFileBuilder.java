package xlsBuilders;

import customExeptions.ExceedingLineLimitException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xlsBuilders.styles.CellStyle.getCellStyle;


public class MultipleOrderFileBuilder {
    private static final XSSFWorkbook workbook = new XSSFWorkbook();
    private Sheet sheet;
    private int totalRowsInSheet = 0;
    private int totalCellsInSheet = 0;

    private List<String> orderContentList;
    private List<String> clientInfoList;
    private List<String> clientAddressList;

    private static final int MAX_ROW_NUMBER = 1048576;
    private static final int MAX_CELL_NUMBER = 16384;
    private static final int MAX_CELL_LENGTH = 32767;

    public XSSFWorkbook buildWorkbook(String sheetName, List<String> headlines) throws ExceedingLineLimitException {
        createSheet(sheetName);
        setHeadlines(headlines);
        fillInfo(orderContentList, 1);
        return workbook;
    }

    public void createSheet(String sheetName) {
        // create list in workbook
        String name = WorkbookUtil.createSafeSheetName(sheetName);
        sheet = workbook.createSheet(name);
    }

    public void setHeadlines(List<String> sheetHeadlines) {
        Row row = sheet.createRow(0);
        for(int i = 0; i < sheetHeadlines.size(); i++) {
            row.createCell(i).setCellValue(sheetHeadlines.get(i));
        }
        // auto-size columns
        int lastCellNum = sheet.getRow(0).getLastCellNum(); //вернёт индекс последней ячейки (считает незаполненные)
        // подстраиваем колонки под текст
        for(int j=0; j<lastCellNum; j++) {
            sheet.autoSizeColumn(j);
        }
    }

    //fixme: числа читаются экселем как строки - в файле работать с ними как с числами невозможно
    public void fillInfo(List<String> info, int startRowNumber) throws ExceedingLineLimitException {
        // fixme: завершит ли метод работу выбрасыванием ошибки или всё-таки обернуть в if-else?
        // todo: обработать исключение - вернуть уведомление об этом как ответ сервера на request
        if(totalRowsInSheet > MAX_ROW_NUMBER) throw new ExceedingLineLimitException("Exceeded maximum row number");
        else {
            int rowNumber = startRowNumber;
            for(int i = 0; i < info.size(); i++) {
                Row row = sheet.createRow(rowNumber);
                totalRowsInSheet++;
                String[] strInInfo = info.get(i).split(";");
                int cellInRow = 0;
                for(String str : strInInfo) {
                    if(totalCellsInSheet > MAX_CELL_NUMBER) throw new ExceedingLineLimitException("Exceeded maximum cell number");
                    totalCellsInSheet++;
                    row.createCell(cellInRow).setCellValue(str.replaceAll("[\\[,\\]]", ""));
                    cellInRow++;
                }
                setStyle(row);
                rowNumber++;
            }
            // auto-size columns
            int lastCellNum = sheet.getRow(startRowNumber).getLastCellNum(); //вернёт индекс последней ячейки (считает незаполненные)
            // подстраиваем колонки под текст
            for(int j=0; j<lastCellNum; j++) {
                sheet.autoSizeColumn(j);
            }
        }
    }

    public void setStyle(Row row) {
        // применить стили для всех заполненных ячеек в строках
        XSSFCellStyle cellStyle = getCellStyle(workbook);
        for (Cell unstyledCell : row) {
            unstyledCell.setCellStyle(cellStyle);
        }
    }

    // устанавливаем содержимое - вводим данные
    public MultipleOrderFileBuilder orderContentList(List<Object> orderContentList) {
        this.orderContentList = convertObjectToStringList(orderContentList);
        return this;
    }

    public MultipleOrderFileBuilder clientInfoList(List<Object> clientInfoList) {
        this.clientInfoList = convertObjectToStringList(clientInfoList);
        return this;
    }

    public MultipleOrderFileBuilder clientAddressList(List<Object> clientAddressList) {
        this.clientAddressList = convertObjectToStringList(clientAddressList);
        return this;
    }

    private List<String> convertObjectToStringList(@org.jetbrains.annotations.NotNull List<Object> objectList) {
        List<String> stringList = new ArrayList<>();
        for (Object object : objectList) {
            // fixme: а этом этапе появляются [] ,
            String classFieldValues = (object.toString()).replaceAll("[\\[,\\]]", ""); // вот тут и юзается override toString()
            stringList = Arrays.asList(classFieldValues.split("\n"));
        }
        return stringList;
    }
}

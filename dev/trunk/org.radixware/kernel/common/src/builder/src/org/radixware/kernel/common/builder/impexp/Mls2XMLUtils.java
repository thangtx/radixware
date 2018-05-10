package org.radixware.kernel.common.builder.impexp;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.DateFormatConverter;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;

public class Mls2XMLUtils {
    final static int START_COLUMN       = 3;
    
    final static String HEADER_UUID     = "String UUID (Do not modify)";
    final static String HEADER_CONTEXT  = "Software context";
    final static String HEADER_REWIWER_COMMENT  = "Reviewer\'s comment";
    final static String HEADER_CREATOR  = "Created by";
    
    final static String META_LAYER = "Layer";
    final static String META_LANUAGES = "Languages";
    final static String META_EXPORT_BY = "Export by";
    final static String META_EXPORT_DATE = "Export date";
    
    final static String PATH_SEPORATOR = "-";
    final static String LANUAGES_SEPORATOR = ", ";
    final static String DATE_FORMAT = "dd MMM yyyy HH:mm:ss";
    final static SimpleDateFormat FORMMATER = new SimpleDateFormat(DATE_FORMAT);
    final static String EXCEL_DATE_FORMAT = DateFormatConverter.convert(Locale.US, DATE_FORMAT);
    
    final static String ORIGINAL_END = "_orig";
    
    public static String getValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)){
                return FORMMATER.format(cell.getDateCellValue());
            }
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            return cell.getRichStringCellValue().getString();
        }  
        return null;
    }
    
    public static Object getObjectValue(HSSFCell cell){
        if (cell != null) {
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                case Cell.CELL_TYPE_NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)){
                        return cell.getDateCellValue();
                    }
                    return cell.getNumericCellValue();
            }
        }  
        return null;
    }
    
    public static boolean isRowEmpty(HSSFRow row) {
        if (row == null){
            return true;
        }
        
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            HSSFCell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }
    
    public static Row findRow(HSSFSheet sheet, String cellContent) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
                        return row;
                    }
                }
            }
        }
        return null;
    }
    
    public static Cell findCell(HSSFSheet sheet, String cellContent) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
                        return cell;
                    }
                }
            }
        }
        return null;
    }
    
    public static Row findRowByColumn(HSSFSheet sheet, int column, String cellContent) {
        for (Row row : sheet) {
            Cell cell = row.getCell(column);
            if (cell != null){
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
                        return row;
                    }
                }
            }
        }
        return null;
    }

    public static Cell findCellByRow(Row row, String cellContent) {
        for (Cell cell : row) {
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
                        return cell;
                    }
                }
            }
        }
        return null;
    }

    public static List<EIsoLanguage> getLanguages(Branch branch, Layer layer, boolean isImport) {
        List<EIsoLanguage> languages = new LinkedList<>();
        if (!isImport || !layer.isReadOnly()){
            for (EIsoLanguage lang : layer.getLanguages()) {
                languages.add(lang);
            }
        }

        for (Layer l : branch.getLayers()) {
            if (l.isLocalizing() && l.getBaseLayerURIs().contains(layer.getURI()) && !l.isReadOnly()) {
                for (EIsoLanguage lang : l.getLanguages()) {
                    if (!languages.contains(lang)){
                        languages.add(lang);
                    }
                }
            }
        }
        
        return languages;
    }
    
    public static void aceptValue(IMultilingualStringDef string, EIsoLanguage language, String value, ImportInfo importInfo) {
        string.setValue(language, value);
        
        if (importInfo.isSetAgreed() || importInfo.isSetChecked()) {
            string.setChecked(language, false);
        }
        
        if (importInfo.isSetAgreed()) {
            string.setAgreed(language, true);
        }
    }
}

package org.radixware.kernel.common.builder.impexp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;

public class Xls2MlsImporter {

    Mls2XmlImportInfo info = null;
    Map<String, List<Definition>> cache;
    Branch branch;
    File file = null;

    public Xls2MlsImporter(Branch branch) {
        this.branch = branch;
        cache = new HashMap<>();
    }

    public void doImport(File file, final boolean isSetChecked, final boolean isSetAgreed) throws IOException {
        info = new Mls2XmlImportInfo();
        try (FileInputStream input = new FileInputStream(file)) {
            final HSSFWorkbook workbook = new HSSFWorkbook(input);

            for (int k = 0; k < workbook.getNumberOfSheets(); k++) {
                if (!workbook.isSheetHidden(k)){
                    HSSFSheet sheet = workbook.getSheetAt(k);
                   

                    if (sheet == null) {
                        info.setFormatProblem(file, "HSSFSheet (" + k + ") does not exist");
                        continue;
                    }
                    
                    Row startedRow = Mls2XMLUtils.findRow(sheet, Mls2XMLUtils.HEADER_UUID);

                    if (startedRow == null) {
                        info.setFormatProblem(file, "Header not found");
                        continue;
                    }
               
                    HSSFSheet orig = workbook.getSheet(sheet.getSheetName() + Mls2XMLUtils.ORIGINAL_END);
                    ImportInfo metaInfo = new ImportInfo();
                    metaInfo.setIsSetChecked(isSetChecked);
                    metaInfo.setIsSetAgreed(isSetAgreed);
                    
                    if (!importMeta(sheet, metaInfo, startedRow.getRowNum())) {
                        continue;
                    }

                    doImport(sheet, orig, startedRow, metaInfo);
                }
            }
        }
    }
    

    private boolean importMeta(HSSFSheet sheet, ImportInfo meta, int endRow) {
        HSSFRow row;
        for (int c = 0; c < endRow; c++) {
            row = sheet.getRow(c);

            if (Mls2XMLUtils.isRowEmpty(row)){
                continue;
            }
            //load metadata
            int col = Mls2XMLUtils.START_COLUMN + 1;
            String title = Mls2XMLUtils.getValue(row.getCell(col));
            col++;
            String value = Mls2XMLUtils.getValue(row.getCell(col));

            if (title != null && value != null) {
                switch (title) {
                    case Mls2XMLUtils.META_LAYER:
                        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(value);
                        if (m.find()){
                            Layer l = branch.getLayers().findByURI(m.group(1));
                            meta.setLayer(l);
                        }
                        break;
                    case Mls2XMLUtils.META_LANUAGES:
                        String[] langs = value.split(Mls2XMLUtils.LANUAGES_SEPORATOR);
                        List<EIsoLanguage> languages = new LinkedList<>();
                       
                        for (String lang: langs){
                            EIsoLanguage language = EIsoLanguage.valueOf(EIsoLanguage.class, lang.toUpperCase());
                            languages.add(language);
                        }
                        
                        if (!languages.isEmpty()){
                            meta.setLanguages(languages);
                        }
                        
                        break;
                    case Mls2XMLUtils.META_EXPORT_BY:
                        meta.setExportAuthor(value);
                        break;
                    case Mls2XMLUtils.META_EXPORT_DATE:
                        Object date = Mls2XMLUtils.getObjectValue(row.getCell(col));
                        if (date instanceof Date){
                            meta.setExportDate((Date) date);
                        }
                        break;
                    
                }
            }

        }
        return true;
    }

    private boolean doImport(HSSFSheet sheet, HSSFSheet orig, Row startedRow, ImportInfo importInfo) {

        Cell startCell = Mls2XMLUtils.findCellByRow(startedRow, Mls2XMLUtils.HEADER_UUID);
        if (startCell == null){
            info.setFormatProblem(file, "UUID cell not found");
            return false;
        }
        Cell contextCell = Mls2XMLUtils.findCellByRow(startedRow, Mls2XMLUtils.HEADER_CONTEXT);
        int contextCol = -1;
        if (contextCell != null){
            contextCol = contextCell.getColumnIndex();
        }
        
        int startCol = startCell.getColumnIndex();
        //load languages list
        Map<Integer, EIsoLanguage> languagesColumn = getLanguages(startedRow, startCol, importInfo);
        
        //load values
        for (int r = startedRow.getRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
            HSSFRow row = sheet.getRow(r);
            if (row == null) {
                continue;
            }

            int col = startCol;
            HSSFCell cell = row.getCell(col);
            if (cell == null) {
                continue;
            }

            //find string by id
            String path = cell.getStringCellValue();
            String[] paths = path.split(Mls2XMLUtils.PATH_SEPORATOR);

            IMultilingualStringDef string = null;
            final Id strId = Id.Factory.loadFrom(paths[paths.length - 1]);
            if (strId == null) {
                info.setLoadStringProblem(row, languagesColumn, paths[paths.length - 1]);
                continue;
            }

            for (int i = 0; i < paths.length - 1; i++) {
                final Id id = Id.Factory.loadFrom(paths[i]);

                if (id == null) {
                    continue;
                }

                List<Definition> defenitions = cache.get(id.toString());

                if (defenitions == null) {
                    defenitions = loadDefenitions(id, importInfo);
                }

                for (Definition def : defenitions) {
                    string = def.findLocalizedString(strId);
                    if (string != null) {
                        break;
                    }
                }

                if (string != null) {
                    break;
                }
            }

            if (string == null || !filterLanguages(importInfo, string, languagesColumn)) {
                info.setLoadStringProblem(row, languagesColumn, strId.toString());
                continue;
            }

            Row origRow = null;
            if (orig != null){
                origRow = Mls2XMLUtils.findRowByColumn(orig, Mls2XMLUtils.START_COLUMN, path);
            }
            
            ImportConflicts conflicts = new ImportConflicts(string, r + 1, 
                                                Mls2XMLUtils.getValue(contextCol > 0 ? row.getCell(contextCol) : null), importInfo);
            
            for (Integer i : languagesColumn.keySet()) {
                EIsoLanguage language = languagesColumn.get(i);
                String value = Mls2XMLUtils.getValue(row.getCell(i));
                if (value == null){
                    continue;
                }
                String origValue = null;
                if (origRow != null) {
                    origValue = Mls2XMLUtils.getValue(origRow.getCell(i));
                }
                
                if (orig != null) {
                    String currentValue = string.getValue(language);
                    if (origValue != null){
                        if (!(origValue.equals(currentValue) || origValue.isEmpty() && currentValue == null) && !value.equals(currentValue)) {
                            conflicts.add(new ImportConflict(conflicts, language, value, origValue));
                        } else {
                            Mls2XMLUtils.aceptValue(string, language, value, importInfo);
                        }   
                    } else {
                        if(!value.equals(currentValue)){
                            conflicts.add(new ImportConflict(conflicts, language, value, ""));
                        } else {
                            Mls2XMLUtils.aceptValue(string, language, value, importInfo);
                        }
                    }
                } else {
                    Mls2XMLUtils.aceptValue(string, language, value, importInfo);
                }
            }
            if (!conflicts.isEmpty()) {
                info.addImportConflicts(conflicts);
            }
        }
        return true;
    }

    public Mls2XmlImportInfo getImportInfo() {
        return info;
    }

    private List<Definition> loadDefenitions(final Id id, ImportInfo meta) {
        final List<Definition> defenitions = new ArrayList<>();
        IVisitor visitor = new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                Definition defenition = ((Definition) radixObject);
                defenitions.add(defenition);
            }
        }; 
        VisitorProvider provider = new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof Definition && ((Definition) radixObject).getId() == id;
            }
        };
        
        Layer layer = meta.getLayer();
        if (layer != null){
            layer.visit(visitor, provider);
        }
        
        if (defenitions.isEmpty()){
            branch.visit(visitor, provider);
        }
        
        if (!defenitions.isEmpty()) {
            cache.put(id.toString(), defenitions);
        }

        return defenitions;
    }
    
    private boolean filterLanguages(ImportInfo meta, IMultilingualStringDef sting, Map<Integer, EIsoLanguage> langs){
        Layer layer = meta.getLayer();
        
        if (layer == null){
            Definition def = (Definition) sting;
            layer = def.getLayer();
        }
        
        if (layer == null){
            return false;
        }
        
        List<EIsoLanguage> languages = Mls2XMLUtils.getLanguages(branch, layer, true);
        
        for (Iterator<Map.Entry<Integer, EIsoLanguage>> it = langs.entrySet().iterator(); it.hasNext();){
            Map.Entry<Integer, EIsoLanguage> entry = it.next();
            if (!languages.contains(entry.getValue())){
                it.remove();
            }
        }
        return true;
    }
    
    private Map<Integer, EIsoLanguage> getLanguages(Row headerRow, int startCol, ImportInfo meta){
        Map<Integer, EIsoLanguage> languagesColumn = new HashMap<>();
        List<EIsoLanguage> languages = meta.getLanguages();
        if (languages == null || languages.isEmpty()) {

            int cells = headerRow.getLastCellNum();

            int col = startCol + 1;

            for (; col < cells; col++) {
                Cell cell = headerRow.getCell(col);
                String value = cell.getStringCellValue();
                try {
                    EIsoLanguage language = EIsoLanguage.valueOf(EIsoLanguage.class, value.toUpperCase());
                    languagesColumn.put(col, language);
                } catch (IllegalArgumentException e) {
                    break;
                }
            }
        } else {
            for (EIsoLanguage language : languages) {
                Cell cell = Mls2XMLUtils.findCellByRow(headerRow, language.getName());
                if (cell != null){
                    languagesColumn.put(cell.getColumnIndex(), language);
                }
            }
        }
        
        return languagesColumn;
    }

}

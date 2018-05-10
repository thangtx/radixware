package org.radixware.kernel.common.builder.impexp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.environment.IMlStringBundle;

public class Mls2XmlImportInfo {
    public String CANNOT_LOAD_STRING = "Can not find string";
    private final List<String> problems = new ArrayList<>();
    private final List<ImportConflicts> conflicts = new ArrayList<>();
    
    public void setFormatProblem(File file){
        setFormatProblem(file, null);
    }
    
    public void setFormatProblem(File file, String msg){
        StringBuilder sb = new StringBuilder();
        sb.append("File ");
        sb.append(file.getPath());
        sb.append(" has wrong format");
        
        if (msg != null && !msg.isEmpty()){
            sb.append(". ");
            sb.append(msg);
        }
        
        addProblem(sb.toString());
    }
    
    public void setLoadStringProblem(HSSFRow row, Map<Integer, EIsoLanguage> languages, String strId) {
        StringBuilder sb = new StringBuilder();
        sb.append(CANNOT_LOAD_STRING);
        sb.append(" #");
        sb.append(strId);
        sb.append(". \n");
        sb.append("Sheet : ");
        sb.append(row.getSheet().getSheetName());
        sb.append(". Row Number: ");
        sb.append(row.getRowNum() + 1);
        sb.append("\n");
        for (Integer i : languages.keySet()) {
            String value = Mls2XMLUtils.getValue(row.getCell(i));
            if (value != null) {
                EIsoLanguage language = languages.get(i);
                sb.append(language.toString());
                sb.append(" : ");
                sb.append(value);
                sb.append("\n");
            }
        }
        
        addProblem(sb.toString());
    }
    
    void addProblem(String s){
        problems.add(s);
    }
    public List<String> getProblems() {
        return problems;
    }
    
    public List<ImportConflicts> getConflicts() {
        return conflicts;
    }
    
    public void addImportConflicts(ImportConflicts conf){
        conflicts.add(conf);
    }
    
}

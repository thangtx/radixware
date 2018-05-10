package org.radixware.kernel.common.builder.impexp;

import java.sql.Timestamp;
import java.util.Date;
import org.radixware.kernel.common.enums.EIsoLanguage;

public class ImportConflict {

    private final EIsoLanguage language;
    private final String importValue;
    private final String originalValue;
    private Boolean acceptImportValue = null;
    private final ImportConflicts container;
    

    public ImportConflict(ImportConflicts container, EIsoLanguage language, String importValue, String origValue) {
        this.language = language;
        this.importValue = importValue;
        this.originalValue = origValue;
        this.container = container;
    }

    public EIsoLanguage getLanguage() {
        return language;
    }

    public String getImportValue() {
        return importValue;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public ImportConflicts getContainer() {
        return container;
    }

    public Boolean getAcceptImportValue() {
        return acceptImportValue;
    }

    public void setAcceptImportValue(Boolean acceptImportValue) {
        this.acceptImportValue = acceptImportValue;
    }

    public String getHtmlImportValue(){
        return getHtmlValue(importValue);
    }
    
    public String getHtmlCurrentValue(){
        return getHtmlValue(container.getString().getValue(language));
    }
    
    private String getHtmlValue(String value){
        if (value == null){
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        if (value.indexOf("<html>") == 0) {
            sb.append("\u200B");
        }
        sb.append(value);
        return sb.toString();
    }
    
    public String getCurrentInformation(boolean isHtml) {
        StringBuilder sb = new StringBuilder();
        if (isHtml) {
            sb.append("<html>");
        }
        sb.append(language);
        sb.append(isHtml ? "<br>" : "\n");

        StringBuilder modifiedLine = new StringBuilder();
        Timestamp lastChangeTime = container.getString().getLastChangeTime(language);
        if (lastChangeTime != null) {
            modifiedLine.append(Mls2XMLUtils.FORMMATER.format(lastChangeTime));
        }
        String author = container.getString().getLastChangeAuthor(language);
        if (author != null && !author.isEmpty()) {
            modifiedLine.append(lastChangeTime != null && !author.isEmpty() ? " " : "");
            modifiedLine.append(author);
        }
        String modified = modifiedLine.toString();
        if (modified == null || modified.isEmpty()) {
            sb.append(isHtml ? "<br>" : "\n");
        } else {
            sb.append(modified);
        }
        if (isHtml) {
            sb.append("</html>");
        }
        return sb.toString();
    }

    public String getImportInformation(boolean isHtml) {
        StringBuilder sb = new StringBuilder();

        if (isHtml) {
            sb.append("<html>");
        }

        sb.append(language);
        sb.append(isHtml ? "<br>" : "\n");

        StringBuilder exportLine = new StringBuilder();
        ImportInfo importInfo = container.getImportInfo();
        Date exportTime = importInfo == null ? null : importInfo.getExportDate();
        if (exportTime != null) {
            exportLine.append(Mls2XMLUtils.FORMMATER.format(exportTime));
        }
        String author = importInfo == null ? null : importInfo.getExportAuthor();
        if (author != null && !author.isEmpty()) {
            exportLine.append(exportTime != null && !author.isEmpty() ? " " : "");
            exportLine.append(author);
        }
        String export = exportLine.toString();
        if (export == null || export.isEmpty()) {
            sb.append(isHtml ? "<br>" : "\n");
        } else {
            sb.append(export);
        }

        if (isHtml) {
            sb.append("</html>");
        }

        return sb.toString();
    }

}

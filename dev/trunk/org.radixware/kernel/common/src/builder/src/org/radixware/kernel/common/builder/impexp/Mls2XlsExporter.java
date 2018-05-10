/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.builder.impexp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author akrylov
 */
public class Mls2XlsExporter {

    private final Branch branch;
    private final Map<Layer, List<Module>> layer2Modules;
    private final Date dateFrom;
    private final Date dateTo;
    private final String userName;
    private final List<EMultilingualStringKind> kinds;

    public Mls2XlsExporter(Branch branch,  Map<Layer, List<Module>> layer2Modules, Date fromDate, Date toDate, String creatorName, List<EMultilingualStringKind> kinds) {
        this.branch = branch;
        this.layer2Modules = layer2Modules;
        this.dateFrom = fromDate;
        this.dateTo = toDate;
        this.userName = creatorName;
        this.kinds = kinds;
    }

    public void doExport(File file) throws IOException {
        final HSSFWorkbook workbook = new HSSFWorkbook();
        
        try {
        } finally {
            for (Layer layer : branch.getLayers().getInOrder()) {
                if (layer2Modules == null || layer2Modules.containsKey(layer)) {
                    doExport(workbook, branch, layer, layer2Modules == null ? null : layer2Modules.get(layer));
                }
            }
            int size = workbook.getNumberOfSheets();
            for (int i = 0; i < size; i++) {
                HSSFSheet spreadsheet = workbook.getSheetAt(i);  
                prepareOriginal(workbook, spreadsheet);
            }

            FileOutputStream output = new FileOutputStream(file);
            try {
                workbook.write(output);
            } finally {
                try {
                    output.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private void prepareOriginal(final HSSFWorkbook workbook, final HSSFSheet spreadsheet){
        String name = spreadsheet.getSheetName();
        final HSSFSheet originalSheet = workbook.cloneSheet(workbook.getSheetIndex(spreadsheet));
        int index = workbook.getSheetIndex(originalSheet);
        workbook.setSheetName(index, name + Mls2XMLUtils.ORIGINAL_END);
        workbook.setSheetHidden(index, true);
    }
    
    private void doExport(final HSSFWorkbook workbook, Branch branch, Layer layer, final List<Module> modules) {
        final String name = layer.getURI().replaceAll("\\.", "_");
        final HSSFSheet spreadsheet = workbook.createSheet(name);
        final List<ILocalizedDef.MultilingualStringInfo> infos = new ArrayList<>();
        final Context context = new Context(spreadsheet);
        final int startCol = Mls2XMLUtils.START_COLUMN;
        final List<EIsoLanguage> langs = Mls2XMLUtils.getLanguages(branch, layer, false);
        
        printMeta(workbook, context, layer, langs);

        //create hader
        int dataCol = startCol;
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        short border = 2;
        style.setBorderLeft(border);
        style.setBorderTop(border);
        style.setBorderBottom(border);
        style.setBorderRight(border);

        HSSFCell cell = context.getCell(dataCol);
        cell.setCellStyle(style);
        cell.setCellValue(Mls2XMLUtils.HEADER_UUID);
        
        for (EIsoLanguage lang : langs) {
            dataCol++;
            cell = context.getCell(dataCol);
            cell.setCellStyle(style);
            cell.setCellValue(lang.getName());
            spreadsheet.setColumnWidth(dataCol, 20000);
        }
        
        dataCol++;
        cell = context.getCell(dataCol);
        cell.setCellStyle(style);
        cell.setCellValue(Mls2XMLUtils.HEADER_CONTEXT);
        int contextCol = dataCol;
        
        dataCol++;
        cell = context.getCell(dataCol);
        cell.setCellStyle(style);
        cell.setCellValue(Mls2XMLUtils.HEADER_REWIWER_COMMENT);
        
        dataCol++;
        cell = context.getCell(dataCol);
        cell.setCellStyle(style);
        cell.setCellValue(Mls2XMLUtils.HEADER_CREATOR);

        context.nextRow();

        final HSSFCellStyle langStyle = workbook.createCellStyle();
        langStyle.setWrapText(true);
        
        for (int i = 0; i <= startCol; i++){
             spreadsheet.setColumnHidden(i, true);
        }
       
        final long from, to;
        if (dateFrom != null) {
            from = dateFrom.getTime();
        } else {
            from = -1;
        }
        if (dateTo != null) {
            to = dateTo.getTime();
        } else {
            to = -1;
        }
        List<Module> modulesList;
        if (modules == null){
            modulesList = new ArrayList<>(layer.getAds().getModules().list());
            modulesList.addAll(layer.getDds().getModules().list());
        } else {
            modulesList = new ArrayList<>(modules);
        }
        
        for (Module module : modulesList){
            module.visit(new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                ILocalizedDef localized = (ILocalizedDef) radixObject;
                infos.clear();
                localized.collectUsedMlStringIds(infos);
                for (ILocalizedDef.MultilingualStringInfo info : infos) {
                    IMultilingualStringDef stringDef = info.findString();
                    if (stringDef != null) {
                        boolean skip = true;
                        for (EIsoLanguage lang : langs) {
                           if (kinds != null && !kinds.contains(info.getKind())){
                               break;
                           }
                           boolean skipByTime = true;
                            if (from > 0 || to > 0) {
                                Date lct = stringDef.getLastChangeTime(lang);
                                if (lct != null) {
                                    long lmt = lct.getTime();
                                    if (from > 0) {
                                        if (to > 0) {
                                            if (lmt >= from && lmt <= to) {
                                                skipByTime = false;
                                            }
                                        } else {
                                            if (lmt >= from) {
                                                skipByTime = false;
                                            }
                                        }
                                    } else {
                                        if (to > 0) {
                                            if (lmt <= to) {
                                                skipByTime = false;
                                            }
                                        } else {
                                            skipByTime = false;
                                        }
                                    }
                                }
                            } else {
                                skipByTime = false;
                            }
                            if (!skipByTime) {
                                String lca = stringDef.getLastChangeAuthor(lang);
                                if (userName == null) {
                                    skip = false;
                                    break;
                                } else {
                                    if (lca != null) {
                                        if (userName.trim().toUpperCase().equals(lca.trim().toUpperCase())) {
                                            skip = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (skip) {
                            continue;
                        }

                        String contextLocation = radixObject.getDefinition().getQualifiedName();
                        int dataCol = startCol;

                        StringBuilder ids = new StringBuilder();
                        boolean first = true;
                        for (Id id : ((Definition) stringDef).getIdPath()) {
                            if (first) {
                                first = false;
                            } else {
                                ids.append(Mls2XMLUtils.PATH_SEPORATOR);
                            }
                            ids.append(id.toString());
                        }
                        
                        HSSFCell cell = context.getCell(dataCol);
                        cell.setCellValue(ids.toString());

                        for (EIsoLanguage lang : langs) {
                            dataCol++;
                            cell = context.getCell(dataCol);
                            String value = stringDef.getValue(lang);
                            cell.setCellValue(value == null ? "" : value);
                            cell.setCellStyle(langStyle);
                        }

                        dataCol++;
                        cell = context.getCell(dataCol);
                        cell.setCellValue(contextLocation);
                        dataCol++;
                        dataCol++;//REWIWER_COMMENT column
                        cell = context.getCell(dataCol);
                        cell.setCellValue(stringDef.getAuthor());
                        context.nextRow();
                    }
                }
            }
            
        }, new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof ILocalizedDef;
            }
        });
        }
        spreadsheet.autoSizeColumn(contextCol);
        spreadsheet.autoSizeColumn(contextCol + 1);
        spreadsheet.autoSizeColumn(contextCol + 2);
    }
    
    void printMeta(final HSSFWorkbook workbook, final Context context, Layer layer, List<EIsoLanguage> langs){
        int column = Mls2XMLUtils.START_COLUMN + 1;
        HSSFCell cell = context.getCell(column);
        cell.setCellValue(Mls2XMLUtils.META_LAYER);
        
        column++;
        cell = context.getCell(column);
        cell.setCellValue(layer.getName() + " (" + layer.getURI() + ")");
        context.nextRow();
        
        column = Mls2XMLUtils.START_COLUMN + 1; 
        cell = context.getCell(column);
        cell.setCellValue(Mls2XMLUtils.META_LANUAGES);
        
        column++;
        cell = context.getCell(column);
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (EIsoLanguage lang : langs) {
            sb.append(prefix);
            prefix = Mls2XMLUtils.LANUAGES_SEPORATOR;
            sb.append(lang.getName());
        }
        cell.setCellValue(sb.toString());
        context.nextRow();
        
        column = Mls2XMLUtils.START_COLUMN + 1;
        cell = context.getCell(column);
        cell.setCellValue(Mls2XMLUtils.META_EXPORT_BY);
        
        column++;
        cell = context.getCell(column);
        cell.setCellValue(System.getProperty("user.name"));
        context.nextRow();
        
        column = Mls2XMLUtils.START_COLUMN + 1;
        cell = context.getCell(column);
        cell.setCellValue(Mls2XMLUtils.META_EXPORT_DATE);
        column++;
        
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cell = context.getCell(column);
       
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        short df = dataFormat.getFormat(Mls2XMLUtils.EXCEL_DATE_FORMAT);
        style.setDataFormat(df);
        cell.setCellStyle(style);
        cell.setCellValue(new Date());
        context.nextRow();
        
        context.nextRow();
        context.nextRow();
        context.nextRow();
        
    }
    
    final class Context {
        final HSSFSheet spreadsheet;

        public Context(final HSSFSheet spreadsheet) {
            this.spreadsheet = spreadsheet;
        }
        
        int row = 0;

        HSSFCell getCell(int col) {
            HSSFRow row = spreadsheet.getRow(this.row);
            if (row == null) {
                row = spreadsheet.createRow(this.row);
            }
            HSSFCell cell = row.getCell(col);
            if (cell == null) {
                cell = row.createCell(col);
            }
            return cell;
        }

        void nextRow() {
            row++;
        }
    }

}

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
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author akrylov
 */
public class Mls2XlsExporter {

    private final Branch branch;
    private final Set<String> layers;
    private final Date dateFrom;
    private final Date dateTo;
    private final String userName;

    public Mls2XlsExporter(Branch branch, Set<String> layers, Date fromDate, Date toDate, String creatorName) {
        this.branch = branch;
        this.layers = layers;
        this.dateFrom = fromDate;
        this.dateTo = toDate;
        this.userName = creatorName;
    }

    public void doExport(File file) throws IOException {
        final HSSFWorkbook workbook = new HSSFWorkbook();
        
        try {
        } finally {
            for (Layer layer : branch.getLayers().getInOrder()) {
                if (layers == null || layers.contains(layer.getURI())) {
                    doExport(workbook, layer);
                }
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

    private void doExport(final HSSFWorkbook workbook, Layer layer) {
        final HSSFSheet spreadsheet = workbook.createSheet(layer.getName() + " (" + layer.getURI() + ")");
        final List<ILocalizedDef.MultilingualStringInfo> infos = new ArrayList<>();
        final class Context {

            int row = 3;

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
        final Context context = new Context();
        final int startCol = 3;
        //create hader
        final List<EIsoLanguage> langs = new LinkedList<>();
        for (EIsoLanguage lang : layer.getLanguages()) {
            langs.add(lang);
        }
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
        cell.setCellValue("String UUID (Do not modify)");
        dataCol++;
        cell = context.getCell(dataCol);
        cell.setCellStyle(style);
        cell.setCellValue("Context Description");
        dataCol++;
        cell = context.getCell(dataCol);
        cell.setCellStyle(style);
        cell.setCellValue("Author");

        for (EIsoLanguage lang : langs) {
            dataCol++;
            cell = context.getCell(dataCol);
            cell.setCellStyle(style);
            cell.setCellValue(lang.getName());
            spreadsheet.setColumnWidth(dataCol, 20000);
        }

        context.nextRow();

        final HSSFCellStyle langStyle = workbook.createCellStyle();
        langStyle.setWrapText(true);

        spreadsheet.setColumnHidden(startCol, true);
        spreadsheet.setColumnHidden(startCol + 1, true);
        spreadsheet.setColumnHidden(startCol + 2, true);
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

        layer.visit(new IVisitor() {

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
                            Date lct = stringDef.getLastChangeTime(lang);
                            if (lct == null) {
                                skip = false;
                                break;
                            }
                            boolean skipByTime = true;
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
                            if (!skipByTime) {
                                String lca = stringDef.getLastChangeAuthor(lang);
                                if (lca == null) {
                                    skip = false;
                                    break;
                                } else {
                                    if (userName == null) {
                                        skip = false;
                                        break;
                                    } else {
                                        if (userName.trim().toUpperCase().equals(lca.trim().toUpperCase())) {
                                            skip = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (skip) {
                            return;
                        }

                        String description = info.getContextDescription();
                        int dataCol = startCol;

                        StringBuilder ids = new StringBuilder();
                        boolean first = true;
                        for (Id id : ((Definition) stringDef).getIdPath()) {
                            if (first) {
                                first = false;
                            } else {
                                ids.append("-");
                            }
                            ids.append(id.toString());
                        }
                        HSSFCell cell = context.getCell(dataCol);
                        cell.setCellValue(ids.toString());
                        dataCol++;
                        cell = context.getCell(dataCol);
                        cell.setCellValue(description);
                        dataCol++;
                        cell = context.getCell(dataCol);
                        cell.setCellValue(stringDef.getAuthor());

                        for (EIsoLanguage lang : langs) {
                            dataCol++;
                            cell = context.getCell(dataCol);
                            String value = stringDef.getValue(lang);
                            cell.setCellValue(value == null ? "" : value);
                            cell.setCellStyle(langStyle);
                        }
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
}

/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.client.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.WorkbookUtil;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.types.PropertyValuesWriteOptions;
import org.radixware.kernel.common.client.utils.CsvWriter;
import org.radixware.kernel.common.client.utils.XlsxWriter;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.IKernelEnum;


public abstract class EntityObjectsWriter {
    
    public static class Factory{
        
        private Factory(){            
        }
        
        public static EntityObjectsWriter newCsvWriter(final File file,
                                                       final PropertyValuesWriteOptions writeOptions,
                                                       final CsvWriter.FormatOptions formatOptions,
                                                       final List<SelectorColumnModelItem> columns) 
                                                        throws FileNotFoundException, UnsupportedEncodingException{
            final CsvWriter csvWriter = 
                new CsvWriter(file, formatOptions, writeOptions.getColumnsToExport().size());
            final List<String> headers = new LinkedList<>();
            if (columns!=null && !columns.isEmpty()){
                for (SelectorColumnModelItem column: columns){
                    headers.add(column.getTitle());
                }
            }
            return new EntityObjectsCsvWriter(csvWriter, writeOptions, headers);
        }
        
        public static EntityObjectsWriter newXlsxWriter(final File file,
                                                       final PropertyValuesWriteOptions writeOptions,
                                                       final List<SelectorColumnModelItem> columns)
                                                       throws FileNotFoundException, InvalidFormatException{
            try {
                String sheetName = writeOptions.getGroupModelTitle() == null ? file.getName() : writeOptions.getGroupModelTitle();
                try {
                    WorkbookUtil.validateSheetName(sheetName);
                } catch (IllegalArgumentException ex) {
                    if (writeOptions.getGroupModelWindowtitle() != null) {
                        sheetName = writeOptions.getGroupModelWindowtitle();
                    }
                    try {
                        WorkbookUtil.validateSheetName(sheetName);
                    } catch (IllegalArgumentException e) {
                        sheetName = WorkbookUtil.createSafeSheetName(sheetName);
                    }
                }
                final XlsxWriter xslxWriter = new XlsxWriter(file, sheetName);
                final List<String> headers = new LinkedList<>();
                if (!columns.isEmpty()) {
                    for (SelectorColumnModelItem column: columns) {
                        headers.add(column.getTitle());
                    }
                }
                return new EntityObjectsXlsxWriter(xslxWriter, writeOptions, headers);
            } catch (IOException ex) {
                Logger.getLogger(EntityObjectsWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
    }
            
    public abstract boolean writeEntityObject(final EntityModel entityObject) throws IOException;
    public abstract void flush() throws IOException;
    public abstract void close() throws IOException;
    
    @SuppressWarnings("unchecked")
    static Object getUntypifiedValue(final Object typifiedValue, final EValType valType){
        if (typifiedValue instanceof IKernelEnum){
            return ((IKernelEnum)typifiedValue).getValue();
        }else if (typifiedValue instanceof Arr && !((Arr)typifiedValue).isEmpty()){
            final Arr untypifiedArr = newArrayInstance(valType, ((Arr)typifiedValue).size());
            for (Object arrItem: ((Arr)typifiedValue)){
                if (arrItem instanceof IKernelEnum){
                    untypifiedArr.add(((IKernelEnum)arrItem).getValue());
                }else{
                    untypifiedArr.add(arrItem);
                }
            }
            return untypifiedArr;
        }else{
            return typifiedValue;
        }
        
    }
    
        @SuppressWarnings("PMD.MissingBreakInSwitch")
    private static Arr newArrayInstance(final EValType valType, final int initialSize){
        switch(valType){
            case ARR_BIN:
            case BIN:                
                return new ArrBin(initialSize);
            case ARR_BOOL:
            case BOOL:
                return new ArrBool(initialSize);
            case ARR_CHAR:
            case CHAR:
                return new ArrChar(initialSize);
            case ARR_DATE_TIME:
            case DATE_TIME:
                return new ArrDateTime(initialSize);
            case ARR_INT:
            case INT:
                return new ArrInt(initialSize);
            case ARR_NUM:
            case NUM:
                return new ArrNum(initialSize);
            case ARR_STR:
            case STR:
                return new ArrStr(initialSize);
            default:
                return null;
        }
    } 
}

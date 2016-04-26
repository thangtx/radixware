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
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.types.PropertyValuesWriteOptions;
import org.radixware.kernel.common.client.utils.CsvWriter;


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
    }
            
    public abstract void writeEntityObject(final EntityModel entityObject) throws IOException;
    public abstract void flush() throws IOException;
    public abstract void close() throws IOException;
}

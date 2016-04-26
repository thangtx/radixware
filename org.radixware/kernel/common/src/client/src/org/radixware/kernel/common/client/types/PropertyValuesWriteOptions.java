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

package org.radixware.kernel.common.client.types;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.types.Id;


public final class PropertyValuesWriteOptions {
    
    public static enum ExportFormat{
        DISPLAYED_TEXT("asDisplayed"),
        PROPERTY_VALUE("value"),
        BOOLEAN_TO_NUMBER("bool2number"),
        BOOLEAN_TO_STRING("bool2str"),
        ENUM_TITLE("enumTitle"),
        BASE64("base64");
        
        private final String shortName;
        
        private ExportFormat(final String name){
            this.shortName = name;
        }
        
        public String asString(){
            return shortName;
        }
                
        public static ExportFormat getFromString(final String name){
            for (ExportFormat format: EnumSet.allOf(ExportFormat.class)){
                if (format.asString().equals(name)){
                    return format;
                }
            }
            return null;
        }
        
        public static String getDisplayName(final ExportFormat format, final MessageProvider mp){
            switch(format){
                case DISPLAYED_TEXT:
                    return mp.translate("ExplorerMessage", "Displayed text");
                case PROPERTY_VALUE:
                    return mp.translate("ExplorerMessage", "Value");
                case BOOLEAN_TO_NUMBER:
                    return mp.translate("ExplorerMessage", "Number \'0\' or \'1\'");
                case BOOLEAN_TO_STRING:
                    return mp.translate("ExplorerMessage", "String \'false\' or \'true\'");
                case ENUM_TITLE:
                    return mp.translate("ExplorerMessage", "Enumeration item description");
                case BASE64:
                    return mp.translate("ExplorerMessage", "Base64");
                default:
                    throw new IllegalArgumentException("Format "+format.name()+" is not supported");
            }
        }
        
    }
    
    private final List<Id> propertyIds = new LinkedList<>();
    private final Map<Id,ExportFormat> exportFormatByPropertyId = new HashMap<>();
    
    public PropertyValuesWriteOptions(final List<Id> columnIds){
        propertyIds.addAll(columnIds);
    }

    public List<Id> getColumnsToExport(){
        return Collections.unmodifiableList(propertyIds);
    }
    
    public void setColumntToExport(final List<Id> columnIds){
        propertyIds.clear();
        propertyIds.addAll(columnIds);
    }
    
    public ExportFormat getFormatForColumnt(final Id columnId){
        return exportFormatByPropertyId.get(columnId);
    }
    
    public void setFormatForColumn(final Id columnId, final ExportFormat format){
        exportFormatByPropertyId.put(columnId, format);
    }
    
    public void saveToConfig(final ClientSettings settings){
                
    }
    
    public static PropertyValuesWriteOptions loadFromConfig(final ClientSettings settings){
        return null;
    }
}

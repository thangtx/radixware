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
import java.util.TimeZone;
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
    
    public static enum TimeZoneFormat {
        SERVER_TIMEZONE("server"),
        CLIENT_TIMEZONE("client"),
        UTC("UTC"),
        ANOTHER("another");
        
        private final String shortName;
        
        private TimeZoneFormat(final String name){
            this.shortName = name;
        }
        
        public String asString(){
            return shortName;
        }
        
        public static TimeZoneFormat getFromString(final String name){
            for (TimeZoneFormat format: EnumSet.allOf(TimeZoneFormat.class)){
                if (format.asString().equals(name)){
                    return format;
                }
            }
            return null;
        }
        
        public static String getDisplayName(final TimeZoneFormat format, final MessageProvider mp){
            switch(format){
                case SERVER_TIMEZONE: 
                    return mp.translate("ExplorerMessage", "Server timezone");
                case CLIENT_TIMEZONE:
                    return mp.translate("ExplorerMessage", "Client timezone");
                case UTC:
                    return "UTC";
                case ANOTHER:
                    return mp.translate("ExplorerMessage", "Other timezone");
                default:
                    throw new IllegalArgumentException("Format "+format.name()+" is not supported");
            }
        }
    }
    
    private final List<Id> propertyIds = new LinkedList<>();
    private final Map<Id,ExportFormat> exportFormatByPropertyId = new HashMap<>();
    private final TimeZoneFormat timeZoneFormat;
    private boolean openFile;
    private TimeZone timeZone;
    private String groupModelTitle;
    private String groupModelWindowTitle;
    
    public PropertyValuesWriteOptions(final List<Id> columnIds){
        this(columnIds, null);
    }
    
    public PropertyValuesWriteOptions (final List<Id> columnIds, TimeZoneFormat timeZoneFormat) {
        propertyIds.addAll(columnIds);
        this.timeZoneFormat = timeZoneFormat;
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
    
    public TimeZoneFormat getTimeZoneFormat() {
        return timeZoneFormat;
    }
    
    public void setTimeZone(TimeZone timezone) {
        this.timeZone = timezone;
    }
    
    public TimeZone getTimeZone() {
        return timeZone;
    }
    
    public void setOpenFile(boolean openFile) {
        this.openFile = openFile;
    }
    
    public boolean getNeedToOpen() {
        return openFile;
    }
    
    public void setGroupModelTitle(String groupModelTitle) {
        this.groupModelTitle = groupModelTitle;
    }
    
    public String getGroupModelTitle() {
        return groupModelTitle;
    }
    
    public void setGroupModelWindowTitle(String groupModelWindowTitle) {
        this.groupModelTitle = groupModelTitle;
    }
    
    public String getGroupModelWindowtitle() {
        return groupModelWindowTitle;
    }
}

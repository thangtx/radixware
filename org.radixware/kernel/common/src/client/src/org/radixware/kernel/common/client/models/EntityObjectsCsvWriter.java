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

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.TimeZone;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.PropertyValuesWriteOptions;
import org.radixware.kernel.common.client.types.TimeZoneInfo;
import org.radixware.kernel.common.client.utils.CsvWriter;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.*;//NOPMD
import org.radixware.kernel.common.utils.Base64;


final class EntityObjectsCsvWriter extends EntityObjectsWriter{
    
    private final CsvWriter csvWriter;
    private final PropertyValuesWriteOptions options;
    private final List<String> fieldHeaders;
    private TimeZone serverTimeZone;
    private boolean serverTimeZoneInited;
    private boolean headerWrited;
    
    public EntityObjectsCsvWriter(final CsvWriter writer, final PropertyValuesWriteOptions writeOptions, final List<String> header){
        csvWriter = writer;
        options = writeOptions;
        fieldHeaders = header==null || header.isEmpty() ? null : Collections.unmodifiableList(header);
    }

    @Override
    public boolean writeEntityObject(final EntityModel entityObject) throws IOException {
        if (!headerWrited){
            if (fieldHeaders!=null){
                for (String header: fieldHeaders){
                    csvWriter.writeStringField(header);
                }
            }
            headerWrited = true;
        }
        final TimeZoneInfo srvTimeZone = entityObject.getEnvironment().getServerTimeZoneInfo();        
        final Timestamp timeToExport = new Timestamp(0);
        for (Id propertyId: options.getColumnsToExport()){
            final Property property = entityObject.getProperty(propertyId);
            final EValType valType = ValueConverter.serverValType2ClientValType(property.getType());
            final boolean isEnum = property.getDefinition().getConstSet()!=null;
            Object propertyValue = isEnum ? getUntypifiedValue(property.getValueObject(), valType) : property.getValueObject();            
            if (propertyValue==null){
                csvWriter.writeEmptyField();
            }else{
                if (propertyValue instanceof Timestamp){
                    final long time = ((Timestamp)propertyValue).getTime();
                    final int clientTimeZoneOffset = TimeZone.getDefault().getOffset(time);
                    final int serverTimeZoneOffset = getServerTimeZoneOffset(srvTimeZone, time);
                    if (clientTimeZoneOffset==serverTimeZoneOffset){
                        timeToExport.setTime(time);
                    }else{
                        final int delta = clientTimeZoneOffset - serverTimeZoneOffset;
                        timeToExport.setTime(time + delta);                        
                    }
                    propertyValue = timeToExport;
                }
                final PropertyValuesWriteOptions.ExportFormat format = options.getFormatForColumnt(propertyId);                
                switch (format){
                    case PROPERTY_VALUE:{
                        csvWriter.writeValue(propertyValue, valType);
                        break;
                    }case ENUM_TITLE:{                    
                        csvWriter.writeStringField(property.getEditMask().toStr(property.getEnvironment(), propertyValue));
                        break;
                    }case DISPLAYED_TEXT:{
                        String valueAsStr = property.getValueAsString();
                        final boolean cantEdit = property.isReadonly()
                                || (!property.hasOwnValue() && property.isValueDefined())
                                || property.isCustomEditOnly()
                                || !property.isEnabled()
                                || property.getEditPossibility() == EEditPossibility.PROGRAMMATICALLY;

                        if (cantEdit) {
                            valueAsStr = property.getOwner().getDisplayString(property.getId(), property.getValueObject(), valueAsStr, !property.hasOwnValue());
                        }
                        csvWriter.writeStringField(valueAsStr);
                        break;
                    }case BOOLEAN_TO_NUMBER:{
                        if (propertyValue instanceof Boolean){
                            if (((Boolean)propertyValue).booleanValue()){
                                csvWriter.writeSafeStringField("1");
                            }else{
                                csvWriter.writeSafeStringField("0");
                            }
                        }else{
                            throw new IllegalArgumentException("Boolean value expected for "+format.name()+" format");
                        }
                        break;
                    }case BOOLEAN_TO_STRING:{
                        if (propertyValue instanceof Boolean){
                            if (((Boolean)propertyValue).booleanValue()){
                                csvWriter.writeSafeStringField("true");
                            }else{
                                csvWriter.writeSafeStringField("false");
                            }
                        }else{
                            throw new IllegalArgumentException("Boolean value expected for "+format.name()+" format");
                        }
                        break;
                    }case BASE64:{
                        if (propertyValue instanceof Bin){
                            csvWriter.writeSafeStringField(Base64.encode(((Bin)propertyValue).get()));
                        }
                        final String valAsStr;
                        if (propertyValue instanceof XmlObject){
                            valAsStr = ((XmlObject)propertyValue).xmlText();
                        }else if (propertyValue instanceof String){
                            valAsStr = (String)propertyValue;
                        }else if (propertyValue instanceof IKernelStrEnum){
                            valAsStr = ((IKernelStrEnum)propertyValue).getValue();
                        }else{
                            throw new IllegalArgumentException("Failed to encode "+propertyValue.getClass().getName()+" to base64");
                        }
                        csvWriter.writeSafeStringField(Base64.encode(valAsStr.getBytes("UTF-8")));
                        break;
                    }default:{
                        throw new IllegalArgumentException("Format "+format.name()+" is not supported");
                    }
                    
                }
            }
        }
        return true;
    }
    
    private int getServerTimeZoneOffset(final TimeZoneInfo tzInfo, final long time){
        if (!serverTimeZoneInited){            
            final List<String> availableZones = Arrays.<String>asList(TimeZone.getAvailableIDs());
            if (availableZones.contains(tzInfo.getId())){
                serverTimeZone = TimeZone.getTimeZone(tzInfo.getId());
            }
            serverTimeZoneInited = true;
        }
        return serverTimeZone==null ? tzInfo.getOffsetMills() : serverTimeZone.getOffset(time);
    }
    
    public void flush() throws IOException{
        if (!csvWriter.flush()){
            throw new IOException();
        }
    }

    @Override
    public void close() throws IOException {
        if (!csvWriter.flush()){
            throw new IOException();
        }
        csvWriter.close();
    }
}

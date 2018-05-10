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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ExcelNumberOutOfBoundsException;
import org.radixware.kernel.common.client.exceptions.ExcelNumberPrecisionTooLargeException;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.PropertyValuesWriteOptions;
import org.radixware.kernel.common.client.types.TimeZoneInfo;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.utils.XlsxWriter;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Base64;

public class EntityObjectsXlsxWriter extends EntityObjectsWriter {

    private final XlsxWriter xlsxWriter;
    private final PropertyValuesWriteOptions options;
    private final List<String> fieldHeaders;
    private TimeZone serverTimeZone;
    private boolean serverTimeZoneInited;
    private boolean headerWrited;
    private final List<Integer> roundToColumns = new LinkedList<>();
    private final List<Integer> adjustColumns = new LinkedList<>();
    private int row = 0;

    public EntityObjectsXlsxWriter(final XlsxWriter writer, final PropertyValuesWriteOptions writeOptions, final List<String> header) {
        xlsxWriter = writer;
        options = writeOptions;
        fieldHeaders = header == null || header.isEmpty() ? null : Collections.unmodifiableList(header);
    }

    @Override
    public boolean writeEntityObject(EntityModel entityObject) throws IOException {
        if (!headerWrited) {
            if (fieldHeaders != null) {
                ClientSettings settings = entityObject.getEnvironment().getConfigStore();
                String alignment;
                settings.beginGroup(SettingNames.SYSTEM);
                settings.beginGroup(SettingNames.SELECTOR_GROUP);
                settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
                alignment = settings.readString(SettingNames.Selector.Common.TITLES_ALIGNMENT);
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
                xlsxWriter.writeHeaderFields(fieldHeaders, alignment);
                saveAdjustedColumns(entityObject);
                row++;
            }
            headerWrited = true;
        }
        int col = 0;
        for (Id propertyId : options.getColumnsToExport()) {
            final Property property = entityObject.getProperty(propertyId);
            IClientEnvironment env = entityObject.getEnvironment();
            final EValType valType = ValueConverter.serverValType2ClientValType(property.getType());
            final boolean isEnum = property.getDefinition().getConstSet() != null;
            Object propertyValue = isEnum ? getUntypifiedValue(property.getValueObject(), valType) : property.getValueObject();
            final PropertyValuesWriteOptions.ExportFormat format = options.getFormatForColumnt(propertyId);
            switch (format) {
                case PROPERTY_VALUE: {
                    if (valType == EValType.NUM) {
                        try {
                            xlsxWriter.checkNumber((BigDecimal) propertyValue);
                            xlsxWriter.writeNumValue((BigDecimal) propertyValue, row, col);
                        } catch (ExcelNumberOutOfBoundsException e) {
                            final String messageTemplate = env.getMessageProvider().translate("XlsxWriter", "Unable to export value %1$s from column %2$s because it exceeds the length specified by Excel limitation. Please change column format to \"Displayed Text\" and try again.");
                            env.messageError(env.getMessageProvider().translate("ExplorerDialog", "Unable to Export Value"), String.format(messageTemplate, propertyValue, fieldHeaders.get(col)));
                            return false;
                        } catch (ExcelNumberPrecisionTooLargeException e) {
                            if (!roundToColumns.contains(col)) {
                                final Set<EDialogButtonType> buttons = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO);
                                final String messageTemplate = env.getMessageProvider().translate("XlsxWriter", "The precision of the value %1$s from column %2$s is too high. Do you want to round the value?");
                                IMessageBox messageBox = env.newMessageBoxDialog(String.format(messageTemplate, propertyValue, fieldHeaders.get(col)), "Exception", EDialogIconType.QUESTION, buttons);
                                messageBox.setOptionText(env.getMessageProvider().translate("XlsxWriter", "Round all column values"));
                                if (messageBox.execMessageBox() == EDialogButtonType.YES) {
                                    if (messageBox.isOptionActivated()) {
                                        roundToColumns.add(col);
                                    }
                                    xlsxWriter.writeNumValue((BigDecimal) propertyValue, row, col);
                                } else {
                                    return false;
                                }
                            } else {
                                xlsxWriter.writeNumValue((BigDecimal) propertyValue, row, col);
                            }
                        }
                    } else if (valType == EValType.INT) {
                        try {
                            xlsxWriter.writeIntValue((Long) propertyValue, row, col);
                        } catch (ExcelNumberOutOfBoundsException ex) {
                            final String messageTemplate = env.getMessageProvider().translate("XlsxWriter", "Unable to export value %1$s from column %2$s because it exceeds the length specified by Excel limitation. Please change column format to \"Displayed Text\" and try again.");
                            env.messageError(env.getMessageProvider().translate("ExplorerDialog", "Unable to Export Value"), String.format(messageTemplate, propertyValue, fieldHeaders.get(col)));
                            return false;
                        }
                    } else if (valType == EValType.DATE_TIME) {
                        PropertyValuesWriteOptions.TimeZoneFormat timeZone = options.getTimeZoneFormat();
                        if (timeZone != null) {
                            String pattern = ((EditMaskDateTime) property.getEditMask()).getDisplayFormat(env);
                            final Timestamp timeToExport = new Timestamp(0);
                            if (propertyValue != null) {
                                final long time = ((Timestamp) propertyValue).getTime();
                                long deltaTime = countDeltaTime(time, env, timeZone);
                                timeToExport.setTime(time + deltaTime);
                                xlsxWriter.writeDateValue(timeToExport, pattern, env.getLocale(), row, col);
                            } else {
                                xlsxWriter.writeDateValue(null, pattern, env.getLocale(), row, col);
                            }
                        } else {
                            xlsxWriter.writeValue(propertyValue, valType, row, col);
                        }
                    } else {
                        xlsxWriter.writeValue(propertyValue, valType, row, col);
                    }
                    break;
                }
                case ENUM_TITLE: {
                    xlsxWriter.writeStringField(property.getEditMask().toStr(env, propertyValue), row, col);
                    break;
                }
                case DISPLAYED_TEXT: {
                    String valueAsStr = property.getValueAsString();
                    final boolean cantEdit = property.isReadonly()
                            || (!property.hasOwnValue() && property.isValueDefined())
                            || property.isCustomEditOnly()
                            || !property.isEnabled()
                            || property.getEditPossibility() == EEditPossibility.PROGRAMMATICALLY;

                    if (cantEdit) {
                        valueAsStr = property.getOwner().getDisplayString(property.getId(), property.getValueObject(), valueAsStr, !property.hasOwnValue());
                    }
                    xlsxWriter.writeStringField(valueAsStr, row, col);
                    break;
                }
                case BOOLEAN_TO_NUMBER: {
                    if (propertyValue == null || propertyValue instanceof Boolean) {
                        if (propertyValue != null) {
                            if (((Boolean) propertyValue).booleanValue()) {
                                xlsxWriter.writeValue(Long.valueOf(1), EValType.INT, row, col);
                            } else {
                                xlsxWriter.writeValue(Long.valueOf(0), EValType.INT, row, col);
                            }
                        } else {
                            xlsxWriter.writeValue(null, EValType.INT, row, col);
                        }
                    } else {
                        throw new IllegalArgumentException("Boolean value expected for " + format.name() + " format");
                    }
                    break;
                }
                case BOOLEAN_TO_STRING: {
                    if (propertyValue == null || propertyValue instanceof Boolean) {
                        if (propertyValue != null) {
                            if (((Boolean) propertyValue).booleanValue()) {
                                xlsxWriter.writeStringField("true", row, col);
                            } else {
                                xlsxWriter.writeStringField("false", row, col);
                            }
                        } else {
                            xlsxWriter.writeStringField(null, row, col);
                        }
                    } else {
                        throw new IllegalArgumentException("Boolean value expected for " + format.name() + " format");
                    }
                    break;
                }
                case BASE64: {
                    if (propertyValue != null) {
                        if (propertyValue instanceof Bin) {
                            xlsxWriter.writeStringField(Base64.encode(((Bin) propertyValue).get()), row, col);
                            break;
                        }
                        final String valAsStr;
                        if (propertyValue instanceof XmlObject) {
                            valAsStr = ((XmlObject) propertyValue).xmlText();
                        } else if (propertyValue instanceof String) {
                            valAsStr = (String) propertyValue;
                        } else if (propertyValue instanceof IKernelStrEnum) {
                            valAsStr = ((IKernelStrEnum) propertyValue).getValue();
                        } else {
                            throw new IllegalArgumentException("Failed to encode " + propertyValue.getClass().getName() + " to base64");
                        }
                        xlsxWriter.writeStringField(Base64.encode(valAsStr.getBytes("UTF-8")), row, col);
                    } else {
                        xlsxWriter.writeStringField(null, row, col);
                    }
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Format " + format.name() + " is not supported");
                }
            }
            col++;
        }
        row++;
        if (row % XlsxWriter.ROW_ACCESS_WINDOW_SIZE == 0) {
            for (Integer adjustedCol : adjustColumns) {
                xlsxWriter.countAdjustedColumnWidth(adjustedCol);
            }
        }
        return true;
    }

    private int getServerTimeZoneOffset(final TimeZoneInfo tzInfo, final long time) {
        if (!serverTimeZoneInited) {
            final List<String> availableZones = Arrays.<String>asList(TimeZone.getAvailableIDs());
            if (availableZones.contains(tzInfo.getId())) {
                serverTimeZone = TimeZone.getTimeZone(tzInfo.getId());
            }
            serverTimeZoneInited = true;
        }
        return serverTimeZone == null ? tzInfo.getOffsetMills() : serverTimeZone.getOffset(time);
    }

    @Override
    public void flush() throws IOException {
        xlsxWriter.flush();
    }

    @Override
    public void close() throws IOException {
        for (Integer col : adjustColumns) {
            xlsxWriter.countAdjustedColumnWidth(col);
            xlsxWriter.applyAdjustedColWidth();
        }
        xlsxWriter.close();
    }

    private void saveAdjustedColumns(EntityModel entityObject) {
        int cell = 0;
        for (Id propertyId : options.getColumnsToExport()) {
            final Property property = entityObject.getProperty(propertyId);
            final EValType valType = ValueConverter.serverValType2ClientValType(property.getType());
            final PropertyValuesWriteOptions.ExportFormat format = options.getFormatForColumnt(propertyId);
            if (valType.equals(EValType.DATE_TIME) || valType.equals(EValType.INT) || valType.equals(EValType.NUM)) {
                adjustColumns.add(cell);
            }
            cell++;
        }
    }

    private long countDeltaTime(long time, IClientEnvironment env, PropertyValuesWriteOptions.TimeZoneFormat timeZone) {
        long deltaTime = 0;
        final TimeZoneInfo srvTimeZone = env.getServerTimeZoneInfo();
        final int serverTimeZoneOffset = getServerTimeZoneOffset(srvTimeZone, time);
        switch (timeZone) {
            case SERVER_TIMEZONE:
                deltaTime = 0;
                break;
            case CLIENT_TIMEZONE:
                final int clientTimeZoneOffset = TimeZone.getDefault().getOffset(time);
                deltaTime = clientTimeZoneOffset - serverTimeZoneOffset;
                break;
            case UTC:
                final int utcTimeZoneOffset = TimeZone.getTimeZone("UTC").getOffset(time);
                deltaTime = utcTimeZoneOffset - serverTimeZoneOffset;
                break;
            case ANOTHER:
                TimeZone customTimeZone = options.getTimeZone();
                if (customTimeZone != null) {
                    final int customTimeZoneOffset = customTimeZone.getOffset(time);
                    deltaTime = customTimeZoneOffset - serverTimeZoneOffset;
                } else {
                    deltaTime = 0;
                }
        }
        return deltaTime;
    }
}

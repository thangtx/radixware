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
package org.radixware.kernel.server.meta.presentations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.enums.ETitleNullFormat;

import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.common.utils.TitleItemFormatter;

public final class RadEntityTitleFormatDef {

    public enum DefinitionContextType {

        ENTITY,
        PROPERTY,
        EDITOR_PRESENTATION
    }
    //Subclass

    //Public fields
    private final Id nullTitleId;
    private final List<TitleItem> format;
    private final Id ownerEntityId;
    private final DefinitionContextType contextType;

    public RadEntityTitleFormatDef(final Id ownerEntityId, final TitleItem[] format, final Id nullTitleId) {
        this(ownerEntityId, format, nullTitleId, null);
    }

    //Constructor
    public RadEntityTitleFormatDef(final Id ownerEntityId, final TitleItem[] format, final Id nullTitleId, DefinitionContextType contextType) {
        this.nullTitleId = nullTitleId;
        this.ownerEntityId = ownerEntityId;
        if (format != null) {
            this.format = Collections.unmodifiableList(Arrays.asList(format));
        } else {
            this.format = null; //format is not defined
        }
        this.contextType = contextType == null ? DefinitionContextType.ENTITY : contextType;
    }

    public final String getNullTitle(final Arte arte) {
        return MultilingualString.get(arte, ownerEntityId, nullTitleId);
    }

    public DefinitionContextType getDefinitionContextType() {
        return contextType;
    }

    public List<TitleItem> getFormat() {
        return format;
    }
    
    public static class TitleFormatItem {
        protected final Object formatIdOrValue;

        public TitleFormatItem(Id formatId) {
            this.formatIdOrValue = formatId;
        }
        
        public TitleFormatItem(String formatStr) {
            this.formatIdOrValue = formatStr;
        }
    }

    public final static class TitleItem extends TitleFormatItem{

        private final Id propId;
        private final ETitleNullFormat nullFormat; 
        private final TitleFormatItem nullFormatItem;
        private final Id ownerEntityId;
        
        public TitleItem(final Id ownerEntityId, final Id propId, final Id formatId) {
            super(formatId);
            this.propId = propId;
            this.ownerEntityId = ownerEntityId;
            this.nullFormat = ETitleNullFormat.SAME_AS_NOT_NULL;
            this.nullFormatItem = null;
        }
        
        public TitleItem(final Id ownerEntityId, final Id propId, final Id formatId, ETitleNullFormat format, TitleFormatItem item) {
            super(formatId);
            this.propId = propId;
            this.ownerEntityId = ownerEntityId;
            this.nullFormat = format;
            this.nullFormatItem = item;
        }

        public TitleItem(final Id ownerEntityId, final Id propId, final String formatStr, ETitleNullFormat format, TitleFormatItem item) {
            super(formatStr);
            this.propId = propId;
            this.ownerEntityId = ownerEntityId;
            this.nullFormat = format;
            this.nullFormatItem = item;
        }

        public TitleItem(final Id propId, final String formatStr) {
            super(formatStr);
            this.propId = propId;
            this.ownerEntityId = null;
            this.nullFormat = ETitleNullFormat.SAME_AS_NOT_NULL;
            this.nullFormatItem = null;
        }

        public String getFormat(final IRadixEnvironment arte) {
            return getFormat(arte, false);
        }
        
        public String getFormat(final IRadixEnvironment arte, boolean isNull) {
            if (isNull) {
                switch (nullFormat) {
                    case CUSTOM:
                        if (nullFormatItem != null) {
                            Object format = nullFormatItem.formatIdOrValue;
                            if (format instanceof String) {
                                return (String) format;
                            } else {
                                return MultilingualString.get(arte, getOwnerEntityId(), (Id) format);
                            }
                        }
                    case PROPERTY_NULL_TITLE:
                        return "{0}";
                    case EMPTY:
                        return "";
                    
                }
            }
            if (formatIdOrValue instanceof String) {
                return (String) formatIdOrValue;
            } else {
                return MultilingualString.get(arte, getOwnerEntityId(), (Id) formatIdOrValue);
            }
        }

        /**
         * @return the propId
         */
        public Id getPropId() {
            return propId;
        }

        /**
         * @return the ownerEntityId
         */
        public Id getOwnerEntityId() {
            return ownerEntityId;
        }

        public final String format(final IRadixEnvironment env, Object itemVal) {
            return format(env, itemVal, "");
        }
        
        public final String format(final IRadixEnvironment env, Object itemVal, String defaultValue) {
            boolean isNull = itemVal == null;
            final String pattern = getFormat(env, isNull);
            if (isNull) {
                switch (nullFormat) {
                    case PROPERTY_NULL_TITLE:
                          if (nullFormatItem != null) {
                            Object format = nullFormatItem.formatIdOrValue;
                            if (format instanceof Id) {
                                return MultilingualString.get(env, getOwnerEntityId(), (Id) format);
                            }
                        }
                    case EMPTY:
                        return TitleItemFormatter.format(pattern, "");
                    case SAME_AS_NOT_NULL:
                        return TitleItemFormatter.format(pattern, defaultValue);
                }
            }
            return TitleItemFormatter.format(pattern, itemVal);
        }
    }
}

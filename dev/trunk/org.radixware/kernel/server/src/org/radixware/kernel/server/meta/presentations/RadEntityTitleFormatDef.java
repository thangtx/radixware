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

    public final static class TitleItem {

        private final Id propId;
        private final Object formatIdOrValue;
        private final Id ownerEntityId;

        public TitleItem(final Id ownerEntityId, final Id propId, final Id formatId) {
            this.propId = propId;
            this.formatIdOrValue = formatId;
            this.ownerEntityId = ownerEntityId;
        }

        public TitleItem(final Id propId, final String formatStr) {
            this.propId = propId;
            this.formatIdOrValue = formatStr;
            this.ownerEntityId = null;
        }

        public String getFormat(final IRadixEnvironment arte) {
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
            final String pattern = getFormat(env);
            return TitleItemFormatter.format(pattern, itemVal);
        }
    }
}

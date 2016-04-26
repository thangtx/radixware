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

package org.radixware.kernel.server.meta.clazzes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;

public final class RadCursorRefFieldPropDef extends RadCursorFieldPropDef {

    private final Id destClassId;
    private final List<RefMapItem> refMap;
    private final List<RefMapItem> propMap;

//Constructor
    public RadCursorRefFieldPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final EValType valType,
            final Id destClassId,
            final RefMapItem[] refMap,
            final IRadPropAccessor accessor) {
        this(id, name, titleId, valType, destClassId, refMap, null, accessor);
    }

    public RadCursorRefFieldPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final EValType valType,
            final Id destClassId,
            final RefMapItem[] refMap,
            final RefMapItem[] propMap,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, valType, null, null, accessor);
        this.destClassId = destClassId;
        if (refMap == null || refMap.length == 0) {
            throw new WrongFormatError("Reference map is mandatory for cursor reference field", null);
        }
        this.refMap = Collections.unmodifiableList(Arrays.asList(refMap));
        this.propMap = propMap == null ? Collections.<RefMapItem>emptyList() : Collections.unmodifiableList(Arrays.asList(propMap));
    }

    public final Id getDestEntityId() {
        return getClassDef().getRelease().getClassEntityId(getDestClassId());
    }

    /**
     * @return the destClassId
     */
    public Id getDestClassId() {
        return destClassId;
    }

    /**
     * @return the refMap
     */
    public List<RefMapItem> getRefMap() {
        return refMap;
    }

    public List<RefMapItem> getOtherPropMap() {
        return propMap;
    }

    public static final class RefMapItem {

        private final Id cursorPropId;
        private final Id propId;

        public RefMapItem(final Id cursorPropId, final Id propId) {
            this.cursorPropId = cursorPropId;
            this.propId = propId;
        }

        /**
         * @return the cursorPropId
         */
        public Id getCursorPropId() {
            return cursorPropId;
        }

        /**
         * @return the propId
         */
        public Id getPropId() {
            return propId;
        }
    }
}

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

import org.apache.xmlbeans.XmlException;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.enums.EPaginationMethod;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.meta.RadTitledDef;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.xscml.SqmlDocument;

public final class RadSortingDef extends RadTitledDef {

    private final List<Item> orderBy;
    private final Sqml hint;
    private final EPaginationMethod paginationMethod;

    public RadSortingDef(
            final Id id,
            final Id ownerDefId,
            final String name,
            final Id titleId,
            final Item[] orderBy,
            final String hint) {
        this(id, ownerDefId, name, titleId, orderBy, hint, null);
    }
    
    //Constructor
    public RadSortingDef(
            final Id id,
            final Id ownerDefId,
            final String name,
            final Id titleId,
            final Item[] orderBy,
            final String hint,
            final String layerUri) {
        this(id, ownerDefId, name, titleId, orderBy, hint, layerUri, EPaginationMethod.ABSOLUTE);
    }

    //Constructor
    public RadSortingDef(
            final Id id,
            final Id ownerDefId,
            final String name,
            final Id titleId,
            final Item[] orderBy,
            final String hint,
            final String layerUri,
            final EPaginationMethod paginationMethod) {
        super(id, name, ownerDefId, titleId);
        if (orderBy != null) {
            this.orderBy = Collections.unmodifiableList(Arrays.asList(orderBy));
        } else {
            this.orderBy = Collections.emptyList();
        }

        final SqmlDocument expr;
        try {
            expr = hint == null || hint.length() == 0 ? null : SqmlDocument.Factory.parse(hint);
        } catch (XmlException e) {
            throw new WrongFormatError("Can't parse sorting \"" + name + "\" (#" + id + ") hint SQML: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        this.hint = expr != null ? Sqml.Factory.loadFrom("SrtHint", expr.getSqml()) : null;
        if (this.hint != null) {
            this.hint.setLayerUri(layerUri);
            this.hint.switchOnWriteProtection();
        }
        this.paginationMethod = paginationMethod;
    }

    /**
     * @return the orderBy
     */
    public List<Item> getOrderBy() {
        return orderBy;
    }

    public EPaginationMethod getPaginationMethod() {
        return paginationMethod;
    }
    
    /**
     * @return the hint
     */
    public Sqml getHint() {
        return hint;
    }

    public static final class Item {

        private final Id columnId;
        private final EOrder order;

        public Item(final Id columnId, final EOrder order) {
            this.columnId = columnId;
            this.order = order;
        }

        final public Id getColumnId() {
            return columnId;
        }

        final public EOrder getOrder() {
            return order;
        }
    }
}

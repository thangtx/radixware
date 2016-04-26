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

package org.radixware.kernel.common.client.editors.xmleditor.view;

import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.enums.EValType;


public final class XmlValueEditingOptions {

    private final boolean notNull;
    private final boolean readOnly;
    private final EValType valType;
    private final EditMask editMask;
    private final SchemaType schemaType;

    XmlValueEditingOptions(final EValType valType, 
                                   final EditMask editMask, 
                                   final boolean readOnly, 
                                   final boolean notNull,
                                   final SchemaType schemaType) {
        this.notNull = notNull;
        this.valType = valType;
        this.editMask = EditMask.newCopy(editMask);
        this.readOnly = readOnly;
        this.schemaType = schemaType;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public EValType getValType() {
        return valType;
    }

    public EditMask getEditMask() {
        return EditMask.newCopy(editMask);
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }    
}
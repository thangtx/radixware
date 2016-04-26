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

package org.radixware.kernel.designer.common.dialogs.components.valstreditor;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.BOOL;
import static org.radixware.kernel.common.enums.EValType.CHAR;
import static org.radixware.kernel.common.enums.EValType.DATE_TIME;
import static org.radixware.kernel.common.enums.EValType.INT;
import static org.radixware.kernel.common.enums.EValType.NUM;
import static org.radixware.kernel.common.enums.EValType.PARENT_REF;
import static org.radixware.kernel.common.enums.EValType.STR;


public class ValAsStrValueEditor extends ValAsStrEditor {

    private final EValType valType;
    private final ValueEditor valueEditor;

    public ValAsStrValueEditor(EValType valType, DdsTableDef targetTable) {
        this(valType, targetTable, null);
    }

    public ValAsStrValueEditor(EValType valType, DdsTableDef targetTable, String nullIndicator) {
        super(nullIndicator);
        this.valType = valType;
        switch (valType) {
            case BOOL:
                valueEditor = new BoolEditor(this);
                break;
            case STR:
                valueEditor = new StrEditor(this);
                break;
            case CHAR:
                valueEditor = new CharEditor(this);
                break;
            case DATE_TIME:
                valueEditor = new DateEditor(this);
                break;
            case INT:
                valueEditor = new IntEditor(this);
                break;
            case NUM:
                valueEditor = new NumEditor(this);
                break;
            case PARENT_REF:
                valueEditor = new ParentRefEditor(this, targetTable);
                break;
            default:
                valueEditor = new StrEditor(this);
                break;
        }
        valueEditor.setDefaultValue();
    }

    @Override
    public EValType getValType() {
        return valType;
    }

    @Override
    public ValueEditor getValueEditor() {
        return valueEditor;
    }
}
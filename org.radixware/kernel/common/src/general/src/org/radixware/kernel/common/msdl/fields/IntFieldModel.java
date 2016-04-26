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

package org.radixware.kernel.common.msdl.fields;

import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlUnitContext;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldInt;
import org.radixware.schemas.msdl.IntField;
import org.radixware.schemas.msdl.LenUnitDef;
import org.radixware.schemas.msdl.Structure;


public class IntFieldModel extends SimpleFieldModel {

    public IntFieldModel(MsdlField container, IntField field) {
        super(container, field);
    }

    @Override
    public IntField getField() {
        return (IntField) super.getField();
    }

    @Override
    public EFieldType getType() {
        return EFieldType.INT;
    }

    @Override
    public SmioField getParser() {
        if (parser == null) {
            parser = new SmioFieldInt(this);
        }
        return parser;
    }

    @Override
    public String getEncoding() {
        if (getField().isSetEncoding()) {
            return getField().getEncoding();
        } else {
            final String[] result = new String[]{null};
            iterateOverParents(true, new ParentAcceptor() {
                @Override
                public boolean accept(MsdlField field, Structure cur) {
                    if (cur.isSetDefaultIntNumEncoding()) {
                        result[0] = cur.getDefaultIntNumEncoding();
                        return false;
                    }
                    if ((isRootMsdlSchemeDirectChild() || field.getModel().isRootMsdlScheme()) && cur.isSetDefaultEncoding()) {
                        String encoding = cur.getDefaultEncoding();
                        if (isAcceptableEncoding(EEncoding.getInstance(encoding))) {
                            result[0] = encoding;
                            return false;
                        }
                    }
                    return true;
                }
            });
            return result[0];
        }
    }

    @Override
    public String getAlign() {
        for (Structure cur : getParentList(true)) {
            if (cur.isSetDefaultIntNumAlign()) {
                return cur.getDefaultIntNumAlign();
            }
            if (cur.isSetAlign()) {
                return cur.getAlign();
            }
        }
        return null;
    }
    
    @Override
    public String getUnit(boolean inclusive, MsdlUnitContext ctx) {
        if (ctx.getContextType() == MsdlUnitContext.EContext.FIXED_LEN) {
            return super.getUnit(inclusive, ctx);
        } else if (ctx.getContextType() == MsdlUnitContext.EContext.EMBEDDED_LEN) {
            for (Structure cur : getParentList(true)) {
                if (cur.isSetDefaultUnit()) {
                    return cur.getDefaultUnit();
                }
            }
            return LenUnitDef.CHAR.toString();
        }
        return null;
    }

    @Override
    public String getPadChar() {
        for (Structure cur : getParentList(true)) {
            if (cur.isSetDefaultIntNumPadChar()) {
                return cur.getDefaultIntNumPadChar();
            }
            if (cur.isSetPadChar()) {
                return cur.getPadChar();
            }
        }
        return null;
    }
}

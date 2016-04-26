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
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldBin;
import org.radixware.schemas.msdl.BinField;
import org.radixware.schemas.msdl.Structure;
import org.radixware.kernel.common.msdl.enums.EEncoding;


public class BinFieldModel extends SimpleFieldModel {

    public BinFieldModel(MsdlField container, BinField field) {
        super(container, field);
        encodingSet.add(EEncoding.NONE.getValue());
        encodingSet.add(EEncoding.BIN.getValue());
        encodingSet.add(EEncoding.HEX.getValue());
        encodingSet.add(EEncoding.HEXEBCDIC.getValue());
        encodingSet.add(EEncoding.DECIMAL.getValue());
    }

    @Override
    public String getEncoding(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultBinEncoding()) {
                return cur.getDefaultBinEncoding();
            } else {
                final String[] result = new String[]{null};
                iterateOverParents(true, new ParentAcceptor() {
                    @Override
                    public boolean accept(MsdlField field, Structure cur) {
                        if (cur.isSetDefaultBinEncoding()) {
                            result[0] = cur.getDefaultBinEncoding();
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
        return null;
    }

    @Override
    public EFieldType getType() {
        return EFieldType.BIN;
    }

    @Override
    public SmioField getParser() {
        if (parser == null) {
            parser = new SmioFieldBin(this);
        }
        return parser;
    }

    @Override
    public String getEncoding() {
        if (getField().isSetEncoding()) {
            return getField().getEncoding();
        } else {
            for (Structure cur : getParentList(true)) {
                if (cur.isSetDefaultBinEncoding()) {
                    return cur.getDefaultBinEncoding();
                }
                if (cur.isSetDefaultEncoding()) {
                    return cur.getDefaultEncoding();
                }
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
                if (cur.isSetDefaultBinUnit()) {
                    return cur.getDefaultBinUnit();
                }
                if (cur.isSetDefaultUnit()) {
                    return cur.getDefaultUnit();
                }
            }
            return null;
        }
        return null;
    }

    @Override
    public String getAlign() {
        for (Structure cur : getParentList(true)) {
            if (cur.isSetDefaultBinAlign()) {
                return cur.getDefaultBinAlign();
            }
            if (cur.isSetAlign()) {
                return cur.getAlign();
            }
        }
        return null;
    }

    @Override
    public String getPadChar() {
        for (Structure cur : getParentList(true)) {
            if (cur.isSetDefaultBinPadChar()) {
                return cur.getDefaultBinPadChar();
            }
            if (cur.isSetPadChar()) {
                return cur.getPadChar();
            }
        }
        return null;
    }

    @Override
    public byte[] getPadBin() {//NOPMD
        for (Structure cur : getParentList(true)) {
            if (cur.isSetDefaultBinPad()) {
                return cur.getDefaultBinPad();
            }
            if (cur.isSetPadBin()) {
                return cur.getPadBin();
            }
        }
        return null;
    }
}

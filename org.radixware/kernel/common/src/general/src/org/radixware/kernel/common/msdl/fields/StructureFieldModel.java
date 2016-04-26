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

import org.radixware.kernel.common.msdl.EFieldsFormat;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.MsdlStructureHeaderFields;
import org.radixware.kernel.common.msdl.MsdlStructureFields;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.structure.SmioFieldStructure;
import org.radixware.schemas.msdl.LenUnitDef;
import org.radixware.schemas.msdl.Structure;
import org.radixware.schemas.msdl.StructureField;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptor;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptorsList;
import java.util.ArrayList;
import org.radixware.kernel.common.msdl.MsdlUnitContext;


public class StructureFieldModel extends TemplateInstanceFieldModel {

    private MsdlStructureHeaderFields headerFields;
    private final MsdlStructureFields fields;

    public StructureFieldModel(MsdlField container, StructureField field) {
        super(container, field);

        if (getStructureType() == EFieldsFormat.BITMAP) {
            headerFields = new MsdlStructureHeaderFields();
            add(headerFields);
            headerFields.open(field.getStructure());
            getStructure().getHeaderFieldList().clear();
        } else {
            headerFields = null;
        }
        fields = new MsdlStructureFields();
        add(fields);
        fields.open(field.getStructure());
        getStructure().getFieldList().clear();
    }

    public final Structure getStructure() {
        return getField().getStructure();
    }

    @Override
    public StructureField getField() {
        return (StructureField) super.getField();
    }

    @Override
    public StructureField getFullField() {
        EFieldsFormat type = getStructureType();
        StructureField sf = getField();
        Structure structure = sf.getStructure();
        structure.getHeaderFieldList().clear();
        structure.getFieldList().clear();
        if (type == EFieldsFormat.BITMAP) {
            for (MsdlField cur : new ArrayList<>(headerFields.list())) {
                structure.getHeaderFieldList().add(cur.getFullField());
            }
        }

        for (MsdlStructureField cur : new ArrayList<>(fields.list())) {
           // if (!cur.getModel().getField().isSetAbstract() || !cur.getModel().getField().getAbstract().booleanValue()) {
                structure.getFieldList().add(cur.getFullField());
           // }
        }

        return sf;
    }

    public final EFieldsFormat getStructureType() {
        Structure structure = getField().getStructure();
        if (structure.isSetDbf()) {
            return EFieldsFormat.DBF;
        } else {
            if (structure.isSetBitmap()) {
                return EFieldsFormat.BITMAP;
            } else {
                if (structure.isSetFieldNaming()) {
                    if (structure.getFieldNaming().isSetPiece()) {
                        return EFieldsFormat.FIELD_NAMING;
                    } else {
                        return EFieldsFormat.BERTLV;
                    }
                } else {
                    return EFieldsFormat.SEQUENTALL_ORDER;
                }
            }
        }
    }

    public void setStructureType(EFieldsFormat format) {
        Structure structure = getStructure();
        if (format != EFieldsFormat.BITMAP && headerFields != null) {
            remove(headerFields);
            headerFields = null;
        }
        switch (format) {
            case BITMAP:
                if (structure.isSetDbf()) {
                    structure.unsetDbf();
                }
                if (structure.isSetFieldNaming()) {
                    structure.unsetFieldNaming();
                }
                if (!structure.isSetBitmap()) {
                    structure.addNewBitmap().setEncoding("Bin");
                    headerFields = new MsdlStructureHeaderFields();
                    add(0, headerFields);
                    headerFields.open(structure);
                }
                break;
            case FIELD_NAMING:
                if (structure.isSetDbf()) {
                    structure.unsetDbf();
                }

                if (structure.isSetBitmap()) {
                    structure.unsetBitmap();
                    getMsdlField().bitmapSetChanged();
                }
                if (!structure.isSetFieldNaming()) {
                    structure.addNewFieldNaming();
                }
                if (!structure.getFieldNaming().isSetPiece()) {
                    if (structure.getFieldNaming().isSetBerTLV()) {
                        structure.getFieldNaming().unsetBerTLV();
                    }
                    structure.getFieldNaming().addNewPiece();
                }
                break;
            case SEQUENTALL_ORDER:
                if (structure.isSetDbf()) {
                    structure.unsetDbf();
                }
                if (structure.isSetFieldNaming()) {
                    structure.unsetFieldNaming();
                }
                if (structure.isSetBitmap()) {
                    structure.unsetBitmap();
                    getMsdlField().bitmapSetChanged();
                }
                break;
            case BERTLV:
                if (structure.isSetDbf()) {
                    structure.unsetDbf();
                }
                if (structure.isSetBitmap()) {
                    structure.unsetBitmap();
                    getMsdlField().bitmapSetChanged();
                }
                if (!structure.isSetFieldNaming()) {
                    structure.addNewFieldNaming();
                }
                if (!structure.getFieldNaming().isSetBerTLV()) {
                    if (structure.getFieldNaming().isSetPiece()) {
                        structure.getFieldNaming().unsetPiece();
                    }
                    structure.getFieldNaming().addNewBerTLV();
                }
                break;
            case DBF:
                if (structure.isSetBitmap()) {
                    structure.unsetBitmap();
                    getMsdlField().bitmapSetChanged();
                }
                if (structure.isSetFieldNaming()) {
                    structure.unsetFieldNaming();
                }
                if (!structure.isSetDbf()) {
                    structure.addNewDbf();
                }
                break;
            default:
                break;
        }
    }

    public MsdlStructureHeaderFields getHeaderFields() {
        return headerFields;
    }

    public MsdlStructureFields getFields() {
        return fields;
    }

    @Override
    public MsdlFieldDescriptorsList getFieldDescriptorList() {
        MsdlFieldDescriptorsList ret = new MsdlFieldDescriptorsList();
        for (MsdlStructureField f : fields) {
            ret.add(new MsdlFieldDescriptor(f));
        }
        storeTemplaFields(ret);
        return ret;
    }

    @Override
    public EFieldType getType() {
        return EFieldType.STRUCTURE;
    }

    @Override
    public SmioField getParser() {
        if (parser == null) {
            parser = new SmioFieldStructure(this);
        }
        return parser;
    }
    
    @Override
    public String getUnit(boolean inclusive, MsdlUnitContext ctx) {
        if (ctx.getContextType() == MsdlUnitContext.EContext.FIXED_LEN) {
            return super.getUnit(inclusive, ctx);
        } else if (ctx.getContextType() == MsdlUnitContext.EContext.EMBEDDED_LEN) {
            return LenUnitDef.BYTE.toString();
        }
        return null;
    }

    @Override
    public void clearParser() {
        super.clearParser();
        if (headerFields != null) {
            for (MsdlField cur : headerFields) {
                cur.getFieldModel().clearParser();
            }
        }
        for (MsdlField cur : fields) {
            cur.getFieldModel().clearParser();
        }
    }
}

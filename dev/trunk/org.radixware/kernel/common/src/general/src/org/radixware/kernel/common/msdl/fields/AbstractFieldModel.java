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

import com.linuxense.javadbf.DBFField;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.MsdlStructureFields;
import org.radixware.kernel.common.msdl.MsdlUnitContext;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.schemas.msdl.AnyField;
import org.radixware.schemas.msdl.BCHField;
import org.radixware.schemas.msdl.BinField;
import org.radixware.schemas.msdl.BooleanField;
import org.radixware.schemas.msdl.ChoiceField;
import org.radixware.schemas.msdl.DateTimeField;
import org.radixware.schemas.msdl.Field;
import org.radixware.schemas.msdl.IntField;
import org.radixware.schemas.msdl.NumField;
import org.radixware.schemas.msdl.SequenceField;
import org.radixware.schemas.msdl.StrField;
import org.radixware.schemas.msdl.Structure;
import org.radixware.schemas.msdl.StructureField;


public abstract class AbstractFieldModel extends RadixObjects<RadixObject> {

    public final static class Factory {

        public static AbstractFieldModel newInstance(MsdlField container, AnyField anyField) {
            AbstractFieldModel model = null;
            if (anyField.isSetBCH()) {
                model = new BCHFieldModel(container, anyField.getBCH());
            }
            if (anyField.isSetBin()) {
                model = new BinFieldModel(container, anyField.getBin());
            }
            if (anyField.isSetChoice()) {
                model = new ChoiceFieldModel(container, anyField.getChoice());
            }
            if (anyField.isSetDateTime()) {
                model = new DateTimeFieldModel(container, anyField.getDateTime());
            }
            if (anyField.isSetInt()) {
                model = new IntFieldModel(container, anyField.getInt());
            }
            if (anyField.isSetNum()) {
                model = new NumFieldModel(container, anyField.getNum());
            }
            if (anyField.isSetSequence()) {
                model = new SequenceFieldModel(container, anyField.getSequence());
            }
            if (anyField.isSetStr()) {
                model = new StrFieldModel(container, anyField.getStr());
            }
            if (anyField.isSetStructure()) {
                model = new StructureFieldModel(container, anyField.getStructure());
            }

            if (anyField.isSetBoolean()) {
                model = new BooleanFieldModel(container, anyField.getBoolean());
            }
            return model;
        }

        public static AbstractFieldModel newInstance(MsdlField container, EFieldType type, Field field) {
            AbstractFieldModel model = null;
            switch (type) {
                case BCH:
                    return new BCHFieldModel(container, (BCHField) field);
                case BIN:
                    return new BinFieldModel(container, (BinField) field);
                case CHOICE:
                    return new ChoiceFieldModel(container, (ChoiceField) field);
                case DATETIME:
                    return new DateTimeFieldModel(container, (DateTimeField) field);
                case INT:
                    return new IntFieldModel(container, (IntField) field);
                case NUM:
                    return new NumFieldModel(container, (NumField) field);
                case SEQUENCE:
                    return new SequenceFieldModel(container, (SequenceField) field);
                case STR:
                    return new StrFieldModel(container, (StrField) field);
                case STRUCTURE:
                    return new StructureFieldModel(container, (StructureField) field);
                case BOOLEAN:
                    return new BooleanFieldModel(container, (BooleanField) field);
            }
            return model;
        }
    }
    private Field field = null;
    protected SmioField parser = null;

    public Field getFullField() {        
        return field;
    }

    public RootMsdlScheme getRootMsdlScheme() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof RootMsdlScheme) {
                return (RootMsdlScheme) owner;
            }
        }
        return null;
    }

    public AbstractFieldModel(MsdlField container, Field field) {
        super(field.getName());
        this.field = (Field) field.copy();
        setContainer(container);
    }

    public Field getField() {
        return field;
    }

    public DBFField getDBFField() {
        return null;
    }

    public MsdlField getMsdlField() {
        RadixObject cur = this;
        while (!(cur instanceof MsdlField)) {
            cur = cur.getContainer();
        }
        return (MsdlField) cur;
    }

    @Override
    public boolean setName(String name) {
        if (super.setName(name)) {
            getField().setName(name);
            return true;
        }
        return false;
    }

    protected List<Structure> getParentList(boolean inclusive) {
        final List<Structure> list = new LinkedList<>();
        iterateOverParents(inclusive, new ParentAcceptor() {
            @Override
            public boolean accept(MsdlField model, Structure structure) {
                list.add(structure);
                return true;
            }
        });
        return list;
    }

    protected interface ParentAcceptor {

        public boolean accept(MsdlField field, Structure structure);
    }

    protected void iterateOverParents(boolean inclusive, ParentAcceptor acceptor) {        
        RadixObject cur = getContainer();
        if (!inclusive) {
            if (cur instanceof RootMsdlScheme) {
                Structure fps = getRootMsdlScheme().getFictiveParentStructure();
                if (fps != null) {
                    acceptor.accept((RootMsdlScheme) cur, fps);
                    
                }
                return;
            }
            cur = cur.getContainer();
        }
        while (true) {
            if (cur instanceof RootMsdlScheme) {
                if (!acceptor.accept((RootMsdlScheme) cur, ((StructureFieldModel) ((MsdlField) cur).getFieldModel()).getStructure())) {
                    return;
                }                
                break;
            }
            if (cur instanceof MsdlField && ((MsdlField) cur).getFieldModel() instanceof StructureFieldModel) {
                if (!acceptor.accept((MsdlField) cur, ((StructureFieldModel) ((MsdlField) cur).getFieldModel()).getStructure())) {
                    return;
                }
            }
            if (cur == null) {
                break;
            }
            cur = cur.getContainer();
        }
        RootMsdlScheme rootScheme = getRootMsdlScheme();
        if (rootScheme != null) {
            Structure fps = rootScheme.getFictiveParentStructure();
            if (fps != null) {
                acceptor.accept(rootScheme, fps);
            }
        }
    }

    public byte[] getStartSeparator(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetStartSeparator()) {
                return cur.getStartSeparator();
            }
        }
        return null;
    }
    
    public String getStartSeparatorViewType(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetStartSeparatorViewType()) {
                return cur.getStartSeparatorViewType();
            }
        }
        return null;
    }

    public byte[] getEndSeparator(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetEndSeparator()) {
                return cur.getEndSeparator();
            }
        }
        return null;
    }
    
    public String getEndSeparatorViewType(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetEndSeparatorViewType()) {
                return cur.getEndSeparatorViewType();
            }
        }
        return null;
    }

    public byte[] getShield(boolean inclusive) {
        List<Structure> arr = getParentList(inclusive);
        for (Structure cur : arr) {
            if (cur.isSetShield()) {
                return cur.getShield();
            }
        }
        return null;
    }
    
    public String getShieldViewType(boolean inclusive) {
        List<Structure> arr = getParentList(inclusive);
        for (Structure cur : arr) {
            if (cur.isSetShieldViewType()) {
                return cur.getShieldViewType();
            }
        }
        return null;
    }

    public String getShieldedFormat(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetShieldedFormat()) {
                return cur.getShieldedFormat();
            }
        }
        return null;
    }

    public byte[] getPadBin(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetPadBin()) {
                return cur.getPadBin();
            }
        }
        return null;
    }

    public byte[] getBinPad(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultBinPad()) {
                return cur.getDefaultBinPad();
            }
        }
        return null;
    }
    
    public String getBinPadViewType(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultBinPadViewType()) {
                return cur.getDefaultBinPadViewType();
            }
        }
        return null;
    }

    public byte[] getItemSeparator(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultItemSeparator()) {
                return cur.getDefaultItemSeparator();
            }
        }
        return null;
    }
    
    public String getItemSeparatorViewType(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultItemSeparatorViewType()) {
                return cur.getDefaultItemSeparatorViewType();
            }
        }
        return null;
    }

    public Boolean getSelfInclusive(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetIsSelfInclusive()) {
                return cur.getIsSelfInclusive();
            }
        }
        return null;
    }

    public byte[] getNullIndicator(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultNullIndicator()) {
                return cur.getDefaultNullIndicator();
            }
        }
        return null;
    }
    
    public String getNullIndicatorViewType(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultNullIndicatorViewType()) {
                return cur.getDefaultNullIndicatorViewType();
            }
        }
        return null;
    }
    
    public String getUnit(boolean inclusive, MsdlUnitContext ctx) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultUnit()) {
                return cur.getDefaultUnit();
            }
        }
        return null;
    }

    public String getUnit(boolean inclusive) {
        return getUnit(inclusive, new MsdlUnitContext(MsdlUnitContext.EContext.FIXED_LEN));
    }

    public String getCharSetType(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultCharSetType()) {
                return cur.getDefaultCharSetType();
            }
        }
        return null;
    }

    public String getCharSetType() {
        return getCharSetType(true);
    }

    public String getCharSetExp(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultCharSetType()) {
                return cur.getDefaultCharSetExp();
            }
        }
        return null;
    }

    public String getCharSetExp() {
        return getCharSetExp(true);
    }
    
    public String getBinUnit(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultBinUnit()) {
                return cur.getDefaultBinUnit();
            }
        }
        return null;
    }

    public String getAlign(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetAlign()) {
                return cur.getAlign();
            }
        }
        return null;
    }

    public String getAlign() {
        for (Structure cur : getParentList(true)) {
            if (cur.isSetAlign()) {
                return cur.getAlign();
            }
        }
        return null;
    }

    public String getPadChar(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetPadChar()) {
                return cur.getPadChar();
            }
        }
        return null;
    }

    public String getPadChar() {
        for (Structure cur : getParentList(true)) {
            if (cur.isSetPadChar()) {
                return cur.getPadChar();
            }
        }
        return null;
    }

    public byte[] getPadBin() {
        for (Structure cur : getParentList(true)) {
            if (cur.isSetPadBin()) {
                return cur.getPadBin();
            }
        }
        return null;
    }
    
    public String getPadViewType(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetPadViewType()) {
                return cur.getPadViewType();
            }
        }
        return null;
    }

    public String getBCHPad(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultBCHPadChar()) {
                return cur.getDefaultBCHPadChar();
            }
        }
        return null;
    }

    public String getStrPadChar(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultStrPadChar()) {
                return cur.getDefaultStrPadChar();
            }
        }
        return null;
    }

    public String getIntNumPadChar(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultIntNumPadChar()) {
                return cur.getDefaultIntNumPadChar();
            }
        }
        return null;
    }

    public String getDateTimeFormat(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultDateTimeFormat()) {
                return cur.getDefaultDateTimeFormat();
            }
        }
        return null;
    }

    public String getDateTimePattern(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultDateTimePattern()) {
                return cur.getDefaultDateTimePattern();
            }
        }
        return null;
    }

    public String getIntNumAlign(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultIntNumAlign()) {
                return cur.getDefaultIntNumAlign();
            }
        }
        return getAlign(inclusive);
    }

    public String getStrAlign(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultStrAlign()) {
                return cur.getDefaultStrAlign();
            }
        }
        return getAlign(inclusive);
    }

    public String getBinAlign(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultBinAlign()) {
                return cur.getDefaultBinAlign();
            }
        }
        return getAlign(inclusive);
    }

    public String getBCHAlign(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultBCHAlign()) {
                return cur.getDefaultBCHAlign();
            }
        }
        return getAlign(inclusive);
    }

    public String getEncoding(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultEncoding()) {
                return cur.getDefaultEncoding();
            }
        }
        return null;
    }

    public String getEncoding() {
        for (Structure cur : getParentList(true)) {
            if (cur.isSetDefaultEncoding()) {
                return cur.getDefaultEncoding();
            }
        }
        return null;
    }

    public String getBinEncoding(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultBinEncoding()) {
                return cur.getDefaultBinEncoding();
            }
        }
        return getEncoding(inclusive);
    }

    public String getIntEncoding(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultIntNumEncoding()) {
                return cur.getDefaultIntNumEncoding();
            }
        }
        return getEncoding(inclusive);
    }

    public String getDateTimeEncoding(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultDateTimeEncoding()) {
                return cur.getDefaultDateTimeEncoding();
            }
        }
        return getEncoding(inclusive);
    }

    public String getStrEncoding(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultStrEncoding()) {
                return cur.getDefaultStrEncoding();
            }
        }
        return getEncoding(inclusive);
    }

    public Character getPlusSign(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultPlusSign()) {
                return cur.getDefaultPlusSign();
            }
        }
        return null;
    }

    public Character getMinusSign(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultMinusSign()) {
                return cur.getDefaultMinusSign();
            }
        }
        return null;
    }

    public Character getFractionalPoint(boolean inclusive) {
        for (Structure cur : getParentList(inclusive)) {
            if (cur.isSetDefaultFractionalPoint()) {
                return cur.getDefaultFractionalPoint();
            }
        }
        return null;
    }

    public abstract EFieldType getType();

    public abstract SmioField getParser();

    public void clearParser() {
        parser = null;
    }

    public void check(RadixObject source, IProblemHandler handler) {
        //getParser().check(source, handler);
    }
    
    public void setModified() {
        setModified(false); 
    }

    public void setModified(boolean fireChangeEvent) {
        clearParser();
        RadixObject tmp = this;
        while (tmp != null) {
            if (tmp instanceof AbstractFieldModel) {
                ((AbstractFieldModel) tmp).clearParser();
            }
            if (tmp instanceof RootMsdlScheme) {
                break;
            }
            tmp = tmp.getContainer();
        }
        getMsdlField().setModified(fireChangeEvent);
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

    public boolean isRootMsdlSchemeDirectChild() {
        int step = 0;
        boolean yes = false;
        RadixObject cur = getContainer();
        while (step < 4) {
            if (step == 0 && cur instanceof MsdlStructureField) {
                ++step;
            } else if (step == 1 && cur instanceof MsdlStructureFields) {
                ++step;
            } else if (step == 2 && cur instanceof StructureFieldModel) {
                ++step;
            } else if (step == 3 && cur instanceof RootMsdlScheme) {
                yes = true;
                break;
            } else {
                break;
            }
            cur = cur.getContainer();
        }
        return yes;
    }

    public boolean isRootMsdlScheme() {
        int step = 1;
        boolean yes = false;
        RadixObject cur = getContainer();
        if (cur instanceof RootMsdlScheme) {
            return true;
        }
        while (step < 4) {
            if (step == 1 && cur instanceof MsdlStructureFields) {
                ++step;
            } else if (step == 2 && cur instanceof StructureFieldModel) {
                ++step;
            } else if (step == 3 && cur instanceof RootMsdlScheme) {
                yes = true;
                break;
            } else {
                break;
            }
            cur = cur.getContainer();
        }
        return yes;
    }
}

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
package org.radixware.kernel.common.msdl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.radixware.kernel.common.build.xbeans.IXBeansChangeListener;
import org.radixware.kernel.common.build.xbeans.XBeansChangeEvent;
import org.radixware.kernel.common.enums.EMsdlTimeZoneType;
import org.radixware.schemas.msdl.AnyField;
import org.radixware.schemas.msdl.IntField;
import org.radixware.schemas.msdl.Structure;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 *
 * @author npopov
 */
class FictiveStructureDelegate implements Structure {

    private boolean isForcedUseMode = false;
    private final Structure xFictive;
    private final Structure xRoot;

    public FictiveStructureDelegate(Structure xFictive, Structure xRoot, boolean isForcedUse) {
        if (xRoot == null || xFictive == null) {
            throw new IllegalArgumentException("Parameter is null: " + xRoot == null ? "'RootStructure'" : "'FictiveStructure'");
        }
        this.xRoot = xRoot;
        this.xFictive = xFictive;
        this.isForcedUseMode = isForcedUse;
    }

    public void setIsForcedUseMode(boolean isForcedUseMode) {
        this.isForcedUseMode = isForcedUseMode;
    }
    
    private <T> T nvl(T o1, T o2) {
        if (isForcedUseMode) {
            return o1 != null ? o1 : o2;
        }
        return o1;
    }

    private boolean nvl(boolean b1, boolean b2) {
        if (isForcedUseMode) {
            return b1 ? b1 : b2;
        }
        return b1;
    }
    
    //***START METHODS USED IN PARENT STRUCTURE***
    @Override
    public byte[] getStartSeparator() {
        return nvl(xFictive.getStartSeparator(), xRoot.getStartSeparator());
    }

    @Override
    public boolean isSetStartSeparator() {
        return nvl(xFictive.isSetStartSeparator(), xRoot.isSetStartSeparator());
    }

    @Override
    public byte[] getEndSeparator() {
        return nvl(xFictive.getEndSeparator(), xRoot.getEndSeparator());
    }

    @Override
    public boolean isSetEndSeparator() {
        return nvl(xFictive.isSetEndSeparator(), xRoot.isSetEndSeparator());
    }

    @Override
    public byte[] getShield() {
        return nvl(xFictive.getShield(), xRoot.getShield());
    }

    @Override
    public boolean isSetShield() {
        return nvl(xFictive.isSetShield(), xRoot.isSetShield());
    }

    @Override
    public String getShieldedFormat() {
        return nvl(xFictive.getShieldedFormat(), xRoot.getShieldedFormat());
    }

    @Override
    public boolean isSetShieldedFormat() {
        return nvl(xFictive.isSetShieldedFormat(), xRoot.isSetShieldedFormat());
    }

    @Override
    public byte[] getPadBin() {
        return nvl(xFictive.getPadBin(), xRoot.getPadBin());
    }

    @Override
    public boolean isSetPadBin() {
        return nvl(xFictive.isSetPadBin(), xRoot.isSetPadBin());
    }

    @Override
    public String getDefaultBinPadChar() {
        return nvl(xFictive.getDefaultBinPadChar(), xRoot.getDefaultBinPadChar());
    }

    @Override
    public boolean isSetDefaultBinPadChar() {
        return nvl(xFictive.isSetDefaultBinPadChar(), xRoot.isSetDefaultBinPadChar());
    }

    @Override
    public byte[] getDefaultItemSeparator() {
        return nvl(xFictive.getDefaultItemSeparator(), xRoot.getDefaultItemSeparator());
    }

    @Override
    public boolean isSetDefaultItemSeparator() {
        return nvl(xFictive.isSetDefaultItemSeparator(), xRoot.isSetDefaultItemSeparator());
    }

    @Override
    public Boolean getIsSelfInclusive() {
        return nvl(xFictive.getIsSelfInclusive(), xRoot.getIsSelfInclusive());
    }

    @Override
    public boolean isSetIsSelfInclusive() {
        return nvl(xFictive.isSetIsSelfInclusive(), xRoot.isSetIsSelfInclusive());
    }

    @Override
    public byte[] getDefaultNullIndicator() {
        return nvl(xFictive.getDefaultNullIndicator(), xRoot.getDefaultNullIndicator());
    }

    @Override
    public boolean isSetDefaultNullIndicator() {
        return nvl(xFictive.isSetDefaultNullIndicator(), xRoot.isSetDefaultNullIndicator());
    }

    @Override
    public String getDefaultNullIndicatorChar() {
        return nvl(xFictive.getDefaultNullIndicatorChar(), xRoot.getDefaultNullIndicatorChar());
    }

    @Override
    public boolean isSetDefaultNullIndicatorChar() {
        return nvl(xFictive.isSetDefaultNullIndicatorChar(), xRoot.isSetDefaultNullIndicatorChar());
    }

    @Override
    public String getDefaultUnit() {
        return nvl(xFictive.getDefaultUnit(), xRoot.getDefaultUnit());
    }

    @Override
    public boolean isSetDefaultUnit() {
        return nvl(xFictive.isSetDefaultUnit(), xRoot.isSetDefaultUnit());
    }

    @Override
    public String getDefaultNullIndicatorUnit() {
        return nvl(xFictive.getDefaultNullIndicatorUnit(), xRoot.getDefaultNullIndicatorUnit());
    }

    @Override
    public boolean isSetDefaultNullIndicatorUnit() {
        return nvl(xFictive.isSetDefaultNullIndicatorUnit(), xRoot.isSetDefaultNullIndicatorUnit());
    }

    @Override
    public String getDefaultCharSetType() {
        return nvl(xFictive.getDefaultCharSetType(), xRoot.getDefaultCharSetType());
    }

    @Override
    public boolean isSetDefaultCharSetType() {
        return nvl(xFictive.isSetDefaultCharSetType(), xRoot.isSetDefaultCharSetType());
    }

    @Override
    public String getDefaultXmlBadCharAction() {
        return nvl(xFictive.getDefaultXmlBadCharAction(), xRoot.getDefaultXmlBadCharAction());
    }

    @Override
    public boolean isSetDefaultXmlBadCharAction() {
        return nvl(xFictive.isSetDefaultXmlBadCharAction(), xRoot.isSetDefaultXmlBadCharAction());
    }

    @Override
    public String getDefaultBinUnit() {
        return nvl(xFictive.getDefaultBinUnit(), xRoot.getDefaultBinUnit());
    }

    @Override
    public boolean isSetDefaultBinUnit() {
        return nvl(xFictive.isSetDefaultBinUnit(), xRoot.isSetDefaultBinUnit());
    }

    @Override
    public String getAlign() {
        return nvl(xFictive.getAlign(), xRoot.getAlign());
    }

    @Override
    public boolean isSetAlign() {
        return nvl(xFictive.isSetAlign(), xRoot.isSetAlign());
    }

    @Override
    public String getPadChar() {
        return nvl(xFictive.getPadChar(), xRoot.getPadChar());
    }

    @Override
    public boolean isSetPadChar() {
        return nvl(xFictive.isSetPadChar(), xRoot.isSetPadChar());
    }

    @Override
    public boolean isSetDefaultBCHPadChar() {
        return nvl(xFictive.isSetDefaultBCHPadChar(), xRoot.isSetDefaultBCHPadChar());
    }

    @Override
    public void setDefaultBCHPadChar(String string) {
        xFictive.setDefaultBCHPadChar(string);
    }

    @Override
    public String getDefaultStrPadChar() {
        return nvl(xFictive.getDefaultStrPadChar(), xRoot.getDefaultStrPadChar());
    }

    @Override
    public boolean isSetDefaultStrPadChar() {
        return nvl(xFictive.isSetDefaultStrPadChar(), xRoot.isSetDefaultStrPadChar());
    }

    @Override
    public String getDefaultIntNumPadChar() {
        return nvl(xFictive.getDefaultIntNumPadChar(), xRoot.getDefaultIntNumPadChar());
    }

    @Override
    public boolean isSetDefaultIntNumPadChar() {
        return nvl(xFictive.isSetDefaultIntNumPadChar(), xRoot.isSetDefaultIntNumPadChar());
    }

    @Override
    public String getDefaultDateTimeFormat() {
        return nvl(xFictive.getDefaultDateTimeFormat(), xRoot.getDefaultDateTimeFormat());
    }

    @Override
    public boolean isSetDefaultDateTimeFormat() {
        return nvl(xFictive.isSetDefaultDateTimeFormat(), xRoot.isSetDefaultDateTimeFormat());
    }

    @Override
    public String getDefaultDateTimePattern() {
        return nvl(xFictive.getDefaultDateTimePattern(), xRoot.getDefaultDateTimePattern());
    }

    @Override
    public boolean isSetDefaultDateTimePattern() {
        return nvl(xFictive.isSetDefaultDateTimePattern(), xRoot.isSetDefaultDateTimePattern());
    }

    @Override
    public EMsdlTimeZoneType getDefaultTimeZoneType() {
        return nvl(xFictive.getDefaultTimeZoneType(), xRoot.getDefaultTimeZoneType());
    }

    @Override
    public boolean isSetDefaultTimeZoneType() {
        return nvl(xFictive.isSetDefaultTimeZoneType(), xRoot.isSetDefaultTimeZoneType());
    }

    @Override
    public String getSpecifiedTimeZoneId() {
        return nvl(xFictive.getSpecifiedTimeZoneId(), xRoot.getSpecifiedTimeZoneId());
    }

    @Override
    public boolean isSetSpecifiedTimeZoneId() {
        return nvl(xFictive.isSetSpecifiedTimeZoneId(), xRoot.isSetSpecifiedTimeZoneId());
    }

    @Override
    public Boolean getDefaultDateLenientParse() {
        return nvl(xFictive.getDefaultDateLenientParse(), xRoot.getDefaultDateLenientParse());
    }

    @Override
    public boolean isSetDefaultDateLenientParse() {
        return nvl(xFictive.isSetDefaultDateLenientParse(), xRoot.isSetDefaultDateLenientParse());
    }

    @Override
    public String getDefaultIntNumAlign() {
        return nvl(xFictive.getDefaultIntNumAlign(), xRoot.getDefaultIntNumAlign());
    }

    @Override
    public boolean isSetDefaultIntNumAlign() {
        return nvl(xFictive.isSetDefaultIntNumAlign(), xRoot.isSetDefaultIntNumAlign());
    }

    @Override
    public String getDefaultStrAlign() {
        return nvl(xFictive.getDefaultStrAlign(), xRoot.getDefaultStrAlign());
    }

    @Override
    public boolean isSetDefaultStrAlign() {
        return nvl(xFictive.isSetDefaultStrAlign(), xRoot.isSetDefaultStrAlign());
    }

    @Override
    public String getDefaultBinAlign() {
        return nvl(xFictive.getDefaultBinAlign(), xRoot.getDefaultBinAlign());
    }

    @Override
    public boolean isSetDefaultBinAlign() {
        return nvl(xFictive.isSetDefaultBinAlign(), xRoot.isSetDefaultBinAlign());
    }

    @Override
    public String getDefaultBCHAlign() {
        return nvl(xFictive.getDefaultBCHAlign(), xRoot.getDefaultBCHAlign());
    }

    @Override
    public boolean isSetDefaultBCHAlign() {
        return nvl(xFictive.isSetDefaultBCHAlign(), xRoot.isSetDefaultBCHAlign());
    }

    @Override
    public String getDefaultEncoding() {
        return nvl(xFictive.getDefaultEncoding(), xRoot.getDefaultEncoding());
    }

    @Override
    public boolean isSetDefaultEncoding() {
        return nvl(xFictive.isSetDefaultEncoding(), xRoot.isSetDefaultEncoding());
    }

    @Override
    public String getDefaultBinEncoding() {
        return nvl(xFictive.getDefaultBinEncoding(), xRoot.getDefaultBinEncoding());
    }

    @Override
    public boolean isSetDefaultBinEncoding() {
        return nvl(xFictive.isSetDefaultBinEncoding(), xRoot.isSetDefaultBinEncoding());
    }

    @Override
    public String getDefaultIntNumEncoding() {
        return nvl(xFictive.getDefaultIntNumEncoding(), xRoot.getDefaultIntNumEncoding());
    }

    @Override
    public boolean isSetDefaultIntNumEncoding() {
        return nvl(xFictive.isSetDefaultIntNumEncoding(), xRoot.isSetDefaultIntNumEncoding());
    }

    @Override
    public String getDefaultDateTimeEncoding() {
        return nvl(xFictive.getDefaultDateTimeEncoding(), xRoot.getDefaultDateTimeEncoding());
    }

    @Override
    public boolean isSetDefaultDateTimeEncoding() {
        return nvl(xFictive.isSetDefaultDateTimeEncoding(), xRoot.isSetDefaultDateTimeEncoding());
    }

    @Override
    public String getDefaultStrEncoding() {
        return nvl(xFictive.getDefaultStrEncoding(), xRoot.getDefaultStrEncoding());
    }

    @Override
    public boolean isSetDefaultStrEncoding() {
        return nvl(xFictive.isSetDefaultStrEncoding(), xRoot.isSetDefaultStrEncoding());
    }

    @Override
    public Character getDefaultPlusSign() {
        return nvl(xFictive.getDefaultPlusSign(), xRoot.getDefaultPlusSign());
    }

    @Override
    public boolean isSetDefaultPlusSign() {
        return nvl(xFictive.isSetDefaultPlusSign(), xRoot.isSetDefaultPlusSign());
    }

    @Override
    public Character getDefaultMinusSign() {
        return nvl(xFictive.getDefaultMinusSign(), xRoot.getDefaultMinusSign());
    }

    @Override
    public boolean isSetDefaultMinusSign() {
        return nvl(xFictive.isSetDefaultMinusSign(), xRoot.isSetDefaultMinusSign());
    }

    @Override
    public Character getDefaultFractionalPoint() {
        return nvl(xFictive.getDefaultFractionalPoint(), xRoot.getDefaultFractionalPoint());
    }

    @Override
    public boolean isSetDefaultFractionalPoint() {
        return nvl(xFictive.isSetDefaultFractionalPoint(), xRoot.isSetDefaultFractionalPoint());
    }
    //***END METHODS USED IN PARENT STRUCTURE***

    @Override
    public IntField getDefaultLengthFormat() {
        return xFictive.getDefaultLengthFormat();
    }

    @Override
    public boolean isSetDefaultLengthFormat() {
        return xFictive.isSetDefaultLengthFormat();
    }

    @Override
    public void setDefaultLengthFormat(IntField i) {
        xFictive.setDefaultLengthFormat(i);
    }

    @Override
    public IntField addNewDefaultLengthFormat() {
        return xFictive.addNewDefaultLengthFormat();
    }

    @Override
    public IntField ensureDefaultLengthFormat() {
        return xFictive.ensureDefaultLengthFormat();
    }

    @Override
    public void unsetDefaultLengthFormat() {
        xFictive.unsetDefaultLengthFormat();
    }

    @Override
    public Dbf getDbf() {
        return xFictive.getDbf();
    }

    @Override
    public boolean isSetDbf() {
        return xFictive.isSetDbf();
    }

    @Override
    public void setDbf(Dbf dbf) {
        xFictive.setDbf(dbf);
    }

    @Override
    public Dbf addNewDbf() {
        return xFictive.addNewDbf();
    }

    @Override
    public Dbf ensureDbf() {
        return xFictive.ensureDbf();
    }

    @Override
    public void unsetDbf() {
        xFictive.unsetDbf();
    }

    @Override
    public FieldNaming getFieldNaming() {
        return xFictive.getFieldNaming();
    }

    @Override
    public boolean isSetFieldNaming() {
        return xFictive.isSetFieldNaming();
    }

    @Override
    public void setFieldNaming(FieldNaming fn) {
        xFictive.setFieldNaming(fn);
    }

    @Override
    public FieldNaming addNewFieldNaming() {
        return xFictive.addNewFieldNaming();
    }

    @Override
    public FieldNaming ensureFieldNaming() {
        return xFictive.ensureFieldNaming();
    }

    @Override
    public void unsetFieldNaming() {
        xFictive.unsetFieldNaming();
    }

    @Override
    public List<AnyField> getHeaderFieldList() {
        return xFictive.getHeaderFieldList();
    }

    @Override
    public AnyField[] getHeaderFieldArray() {
        return xFictive.getHeaderFieldArray();
    }

    @Override
    public AnyField getHeaderFieldArray(int i) {
        return xFictive.getHeaderFieldArray(i);
    }

    @Override
    public int sizeOfHeaderFieldArray() {
        return xFictive.sizeOfHeaderFieldArray();
    }

    @Override
    public void setHeaderFieldArray(AnyField[] afs) {
        xFictive.setHeaderFieldArray(afs);
    }

    @Override
    public void assignHeaderFieldList(List<AnyField> list) {
        xFictive.assignHeaderFieldList(list);
    }

    @Override
    public void setHeaderFieldArray(int i, AnyField af) {
        xFictive.setHeaderFieldArray(i, af);
    }

    @Override
    public AnyField insertNewHeaderField(int i) {
        return xFictive.insertNewHeaderField(i);
    }

    @Override
    public AnyField addNewHeaderField() {
        return xFictive.addNewHeaderField();
    }

    @Override
    public void removeHeaderField(int i) {
        xFictive.removeHeaderField(i);
    }

    @Override
    public Bitmap getBitmap() {
        return xFictive.getBitmap();
    }

    @Override
    public boolean isSetBitmap() {
        return xFictive.isSetBitmap();
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        xFictive.setBitmap(bitmap);
    }

    @Override
    public Bitmap addNewBitmap() {
        return xFictive.addNewBitmap();
    }

    @Override
    public Bitmap ensureBitmap() {
        return xFictive.ensureBitmap();
    }

    @Override
    public void unsetBitmap() {
        xFictive.unsetBitmap();
    }

    @Override
    public List<Field> getFieldList() {
        return xFictive.getFieldList();
    }

    @Override
    public Field[] getFieldArray() {
        return xFictive.getFieldArray();
    }

    @Override
    public Field getFieldArray(int i) {
        return xFictive.getFieldArray(i);
    }

    @Override
    public int sizeOfFieldArray() {
        return xFictive.sizeOfFieldArray();
    }

    @Override
    public void setFieldArray(Field[] fields) {
        xFictive.setFieldArray(fields);
    }

    @Override
    public void assignFieldList(List<Field> list) {
        xFictive.assignFieldList(list);
    }

    @Override
    public void setFieldArray(int i, Field field) {
        xFictive.setFieldArray(i, field);
    }

    @Override
    public Field insertNewField(int i) {
        return xFictive.insertNewField(i);
    }

    @Override
    public Field addNewField() {
        return xFictive.addNewField();
    }

    @Override
    public void removeField(int i) {
        xFictive.removeField(i);
    }

    @Override
    public void setIsSelfInclusive(Boolean bln) {
        xFictive.setIsSelfInclusive(bln);
    }

    @Override
    public void setPadChar(String string) {
        xFictive.setPadChar(string);
    }

    @Override
    public void setPadBin(byte[] bytes) {
        xFictive.setPadBin(bytes);
    }

    @Override
    public String getPadViewType() {
        return xFictive.getPadViewType();
    }

    @Override
    public boolean isSetPadViewType() {
        return xFictive.isSetPadViewType();
    }

    @Override
    public void setPadViewType(String string) {
        xFictive.setPadViewType(string);
    }

    @Override
    public void setAlign(String string) {
        xFictive.setAlign(string);
    }

    @Override
    public Boolean getTrimToLengthIfExceed() {
        return xFictive.getTrimToLengthIfExceed();
    }

    @Override
    public boolean isSetTrimToLengthIfExceed() {
        return xFictive.isSetTrimToLengthIfExceed();
    }

    @Override
    public void setTrimToLengthIfExceed(Boolean bln) {
        xFictive.setTrimToLengthIfExceed(bln);
    }

    @Override
    public void setStartSeparator(byte[] bytes) {
        xFictive.setStartSeparator(bytes);
    }

    @Override
    public String getStartSeparatorViewType() {
        return xFictive.getStartSeparatorViewType();
    }

    @Override
    public boolean isSetStartSeparatorViewType() {
        return xFictive.isSetStartSeparatorViewType();
    }

    @Override
    public void setStartSeparatorViewType(String string) {
        xFictive.setStartSeparatorViewType(string);
    }

    @Override
    public void setEndSeparator(byte[] bytes) {
        xFictive.setEndSeparator(bytes);
    }

    @Override
    public String getEndSeparatorViewType() {
        return xFictive.getEndSeparatorViewType();
    }

    @Override
    public boolean isSetEndSeparatorViewType() {
        return xFictive.isSetEndSeparatorViewType();
    }

    @Override
    public void setEndSeparatorViewType(String string) {
        xFictive.setEndSeparatorViewType(string);
    }

    @Override
    public void setShield(byte[] bytes) {
        xFictive.setShield(bytes);
    }

    @Override
    public String getShieldViewType() {
        return xFictive.getShieldViewType();
    }

    @Override
    public boolean isSetShieldViewType() {
        return xFictive.isSetShieldViewType();
    }

    @Override
    public void setShieldViewType(String string) {
        xFictive.setShieldViewType(string);
    }

    @Override
    public void setShieldedFormat(String string) {
        xFictive.setShieldedFormat(string);
    }

    @Override
    public String getDefaultBCHPadChar() {
        return xFictive.getDefaultBCHPadChar();
    }

    @Override
    public void setDefaultBCHAlign(String string) {
        xFictive.setDefaultBCHAlign(string);
    }

    @Override
    public void setDefaultItemSeparator(byte[] bytes) {
        xFictive.setDefaultItemSeparator(bytes);
    }

    @Override
    public String getDefaultItemSeparatorViewType() {
        return xFictive.getDefaultItemSeparatorViewType();
    }

    @Override
    public boolean isSetDefaultItemSeparatorViewType() {
        return xFictive.isSetDefaultItemSeparatorViewType();
    }

    @Override
    public void setDefaultItemSeparatorViewType(String string) {
        xFictive.setDefaultItemSeparatorViewType(string);
    }

    @Override
    public void setDefaultDateTimePattern(String string) {
        xFictive.setDefaultDateTimePattern(string);
    }

    @Override
    public void setDefaultDateTimeFormat(String string) {
        xFictive.setDefaultDateTimeFormat(string);
    }

    @Override
    public void setDefaultDateTimeEncoding(String string) {
        xFictive.setDefaultDateTimeEncoding(string);
    }

    @Override
    public void setDefaultDateLenientParse(Boolean bln) {
        xFictive.setDefaultDateLenientParse(bln);
    }

    @Override
    public void setDefaultTimeZoneType(EMsdlTimeZoneType emtzt) {
        xFictive.setDefaultTimeZoneType(emtzt);
    }

    @Override
    public void setSpecifiedTimeZoneId(String string) {
        xFictive.setSpecifiedTimeZoneId(string);
    }

    @Override
    public void setDefaultNullIndicator(byte[] bytes) {
        xFictive.setDefaultNullIndicator(bytes);
    }

    @Override
    public void setDefaultNullIndicatorChar(String string) {
        xFictive.setDefaultNullIndicatorChar(string);
    }

    @Override
    public void setDefaultNullIndicatorUnit(String string) {
        xFictive.setDefaultNullIndicatorUnit(string);
    }

    @Override
    public String getDefaultNullIndicatorViewType() {
        return xFictive.getDefaultNullIndicatorViewType();
    }

    @Override
    public boolean isSetDefaultNullIndicatorViewType() {
        return xFictive.isSetDefaultNullIndicatorViewType();
    }

    @Override
    public void setDefaultNullIndicatorViewType(String string) {
        xFictive.setDefaultNullIndicatorViewType(string);
    }

    @Override
    public void setDefaultPlusSign(Character chrctr) {
        xFictive.setDefaultPlusSign(chrctr);
    }

    @Override
    public void setDefaultMinusSign(Character chrctr) {
        xFictive.setDefaultMinusSign(chrctr);
    }

    @Override
    public void setDefaultFractionalPoint(Character chrctr) {
        xFictive.setDefaultFractionalPoint(chrctr);
    }

    @Override
    public void setDefaultStrEncoding(String string) {
        xFictive.setDefaultStrEncoding(string);
    }

    @Override
    public void setDefaultStrAlign(String string) {
        xFictive.setDefaultStrAlign(string);
    }

    @Override
    public void setDefaultIntNumAlign(String string) {
        xFictive.setDefaultIntNumAlign(string);
    }

    @Override
    public void setDefaultBinAlign(String string) {
        xFictive.setDefaultBinAlign(string);
    }

    @Override
    public void setDefaultBinEncoding(String string) {
        xFictive.setDefaultBinEncoding(string);
    }

    @Override
    public void setDefaultStrPadChar(String string) {
        xFictive.setDefaultStrPadChar(string);
    }

    @Override
    public void setDefaultIntNumPadChar(String string) {
        xFictive.setDefaultIntNumPadChar(string);
    }

    @Override
    public void setDefaultIntNumEncoding(String string) {
        xFictive.setDefaultIntNumEncoding(string);
    }

    @Override
    public void setDefaultBinPadChar(String string) {
        xFictive.setDefaultBinPadChar(string);
    }

    @Override
    public byte[] getDefaultBinPad() {
        return xFictive.getDefaultBinPad();
    }

    @Override
    public boolean isSetDefaultBinPad() {
        return xFictive.isSetDefaultBinPad();
    }

    @Override
    public void setDefaultBinPad(byte[] bytes) {
        xFictive.setDefaultBinPad(bytes);
    }

    @Override
    public String getDefaultBinPadViewType() {
        return xFictive.getDefaultBinPadViewType();
    }

    @Override
    public boolean isSetDefaultBinPadViewType() {
        return xFictive.isSetDefaultBinPadViewType();
    }

    @Override
    public void setDefaultBinPadViewType(String string) {
        xFictive.setDefaultBinPadViewType(string);
    }

    @Override
    public void setDefaultBinUnit(String string) {
        xFictive.setDefaultBinUnit(string);
    }

    @Override
    public void setDefaultUnit(String string) {
        xFictive.setDefaultUnit(string);
    }

    @Override
    public void setDefaultEncoding(String string) {
        xFictive.setDefaultEncoding(string);
    }

    @Override
    public void setDefaultCharSetType(String string) {
        xFictive.setDefaultCharSetType(string);
    }

    @Override
    public String getDefaultCharSetExp() {
        return xFictive.getDefaultCharSetExp();
    }

    @Override
    public boolean isSetDefaultCharSetExp() {
        return xFictive.isSetDefaultCharSetExp();
    }

    @Override
    public void setDefaultCharSetExp(String string) {
        xFictive.setDefaultCharSetExp(string);
    }

    @Override
    public void setDefaultXmlBadCharAction(String string) {
        xFictive.setDefaultXmlBadCharAction(string);
    }

    @Override
    public String getFieldSeparatorUnit() {
        return xFictive.getFieldSeparatorUnit();
    }

    @Override
    public boolean isSetFieldSeparatorUnit() {
        return xFictive.isSetFieldSeparatorUnit();
    }

    @Override
    public void setFieldSeparatorUnit(String string) {
        xFictive.setFieldSeparatorUnit(string);
    }

    @Override
    public byte[] getFieldSeparatorStart() {
        return xFictive.getFieldSeparatorStart();
    }

    @Override
    public boolean isSetFieldSeparatorStart() {
        return xFictive.isSetFieldSeparatorStart();
    }

    @Override
    public void setFieldSeparatorStart(byte[] bytes) {
        xFictive.setFieldSeparatorStart(bytes);
    }

    @Override
    public byte[] getFieldSeparator() {
        return xFictive.getFieldSeparator();
    }

    @Override
    public boolean isSetFieldSeparator() {
        return xFictive.isSetFieldSeparator();
    }

    @Override
    public void setFieldSeparator(byte[] bytes) {
        xFictive.setFieldSeparator(bytes);
    }

    @Override
    public String getFieldSeparatorStartChar() {
        return xFictive.getFieldSeparatorStartChar();
    }

    @Override
    public boolean isSetFieldSeparatorStartChar() {
        return xFictive.isSetFieldSeparatorStartChar();
    }

    @Override
    public void setFieldSeparatorStartChar(String string) {
        xFictive.setFieldSeparatorStartChar(string);
    }

    @Override
    public String getFieldSeparatorChar() {
        return xFictive.getFieldSeparatorChar();
    }

    @Override
    public boolean isSetFieldSeparatorChar() {
        return xFictive.isSetFieldSeparatorChar();
    }

    @Override
    public void setFieldSeparatorChar(String string) {
        xFictive.setFieldSeparatorChar(string);
    }

    @Override
    public String getFieldSeparatorViewType() {
        return xFictive.getFieldSeparatorViewType();
    }

    @Override
    public boolean isSetFieldSeparatorViewType() {
        return xFictive.isSetFieldSeparatorViewType();
    }

    @Override
    public void setFieldSeparatorViewType(String string) {
        xFictive.setFieldSeparatorViewType(string);
    }

    @Override
    public SchemaType schemaType() {
        return xFictive.schemaType();
    }

    @Override
    public boolean validate() {
        return xFictive.validate();
    }

    @Override
    public boolean validate(XmlOptions options) {
        return xFictive.validate(options);
    }

    @Override
    public XmlObject[] selectPath(String path) {
        return xFictive.selectPath(path);
    }

    @Override
    public XmlObject[] selectPath(String path, XmlOptions options) {
        return xFictive.selectPath(path, options);
    }

    @Override
    public XmlObject[] execQuery(String query) {
        return xFictive.execQuery(query);
    }

    @Override
    public XmlObject[] execQuery(String query, XmlOptions options) {
        return xFictive.execQuery(query, options);
    }

    @Override
    public XmlObject changeType(SchemaType newType) {
        return xFictive.changeType(newType);
    }

    @Override
    public XmlObject substitute(QName newName, SchemaType newType) {
        return xFictive.substitute(newName, newType);
    }

    @Override
    public boolean isNil() {
        return xFictive.isNil();
    }

    @Override
    public void setNil() {
        xFictive.setNil();
    }

    @Override
    public boolean isImmutable() {
        return xFictive.isImmutable();
    }

    @Override
    public XmlObject set(XmlObject srcObj) {
        return xFictive.set(srcObj);
    }

    @Override
    public XmlObject copy() {
        return xFictive.copy();
    }

    @Override
    public boolean valueEquals(XmlObject obj) {
        return xFictive.valueEquals(obj);
    }

    @Override
    public int valueHashCode() {
        return xFictive.valueHashCode();
    }

    @Override
    public int compareTo(Object obj) {
        return xFictive.compareTo(obj);
    }

    @Override
    public int compareValue(XmlObject obj) {
        return xFictive.compareValue(obj);
    }

    @Override
    public XmlObject[] selectChildren(QName elementName) {
        return xFictive.selectChildren(elementName);
    }

    @Override
    public XmlObject[] selectChildren(String elementUri, String elementLocalName) {
        return xFictive.selectChildren(elementUri, elementLocalName);
    }

    @Override
    public XmlObject[] selectChildren(QNameSet elementNameSet) {
        return xFictive.selectChildren(elementNameSet);
    }

    @Override
    public XmlObject selectAttribute(QName attributeName) {
        return xFictive.selectAttribute(attributeName);
    }

    @Override
    public XmlObject selectAttribute(String attributeUri, String attributeLocalName) {
        return xFictive.selectAttribute(attributeUri, attributeLocalName);
    }

    @Override
    public XmlObject[] selectAttributes(QNameSet attributeNameSet) {
        return xFictive.selectAttributes(attributeNameSet);
    }

    @Override
    public Object monitor() {
        return xFictive.monitor();
    }

    @Override
    public XmlDocumentProperties documentProperties() {
        return xFictive.documentProperties();
    }

    @Override
    public XmlCursor newCursor() {
        return xFictive.newCursor();
    }

    @Override
    public XMLInputStream newXMLInputStream() {
        return xFictive.newXMLInputStream();
    }

    @Override
    public XMLStreamReader newXMLStreamReader() {
        return xFictive.newXMLStreamReader();
    }

    @Override
    public String xmlText() {
        return xFictive.xmlText();
    }

    @Override
    public InputStream newInputStream() {
        return xFictive.newInputStream();
    }

    @Override
    public Reader newReader() {
        return xFictive.newReader();
    }

    @Override
    public Node newDomNode() {
        return xFictive.newDomNode();
    }

    @Override
    public Node getDomNode() {
        return xFictive.getDomNode();
    }

    @Override
    public void save(ContentHandler ch, LexicalHandler lh) throws SAXException {
        xFictive.save(ch, lh);
    }

    @Override
    public void save(File file) throws IOException {
        xFictive.save(file);
    }

    @Override
    public void save(OutputStream os) throws IOException {
        xFictive.save(os);
    }

    @Override
    public void save(Writer w) throws IOException {
        xFictive.save(w);
    }

    @Override
    public XMLInputStream newXMLInputStream(XmlOptions options) {
        return xFictive.newXMLInputStream(options);
    }

    @Override
    public XMLStreamReader newXMLStreamReader(XmlOptions options) {
        return xFictive.newXMLStreamReader(options);
    }

    @Override
    public String xmlText(XmlOptions options) {
        return xFictive.xmlText(options);
    }

    @Override
    public InputStream newInputStream(XmlOptions options) {
        return xFictive.newInputStream(options);
    }

    @Override
    public Reader newReader(XmlOptions options) {
        return xFictive.newReader(options);
    }

    @Override
    public Node newDomNode(XmlOptions options) {
        return xFictive.newDomNode(options);
    }

    @Override
    public void save(ContentHandler ch, LexicalHandler lh, XmlOptions options) throws SAXException {
        xFictive.save(ch, lh, options);
    }

    @Override
    public void save(File file, XmlOptions options) throws IOException {
        xFictive.save(file, options);
    }

    @Override
    public void save(OutputStream os, XmlOptions options) throws IOException {
        xFictive.save(os, options);
    }

    @Override
    public void save(Writer w, XmlOptions options) throws IOException {
        xFictive.save(w, options);
    }

    @Override
    public void dump() {
        xFictive.dump();
    }

    @Override
    public void fireXBeansChangeEvent(XBeansChangeEvent event) {
        xFictive.fireXBeansChangeEvent(event);
    }

    @Override
    public boolean beforeXBeansChangeEvent(XBeansChangeEvent event) {
        return xFictive.beforeXBeansChangeEvent(event);
    }

    @Override
    public void addXBeansChangeListener(IXBeansChangeListener changeListener) {
        xFictive.addXBeansChangeListener(changeListener);
    }

    @Override
    public void removeXBeansChangeListener(IXBeansChangeListener changeListener) {
        xFictive.removeXBeansChangeListener(changeListener);
    }

    @Override
    public boolean hasXBeansChangeListeners() {
        return xFictive.hasXBeansChangeListeners();
    }

    @Override
    public int hashCode() {
        return xFictive.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return xFictive.equals(obj);
    }

    @Override
    public String toString() {
        return xFictive.toString();
    }

}

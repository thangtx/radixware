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
package org.radixware.kernel.common.msdl.fields.parser.structure;

import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlStructureHeaderFields;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.EFieldsFormat;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.fields.IntFieldModel;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldInt;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.IFieldList;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.PlainFieldList;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.SeparatedFieldList;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPiece;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPieceSeparated;
import org.radixware.kernel.common.msdl.MsdlStructureFields;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptor;
import org.radixware.kernel.common.msdl.fields.extras.MsdlFieldDescriptorsList;
import org.radixware.kernel.common.msdl.fields.parser.ParseUtil;
import org.radixware.kernel.common.msdl.fields.parser.SmioCoder;
import org.radixware.schemas.msdl.SeparatedDef;
import org.radixware.schemas.msdl.Structure;
import org.radixware.schemas.msdl.Structure.FieldNaming;
import org.radixware.schemas.msdl.StructureField;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldSimple;
import org.radixware.kernel.common.msdl.fields.parser.piece.extras.BERManipulator;
import org.radixware.schemas.types.BinHex;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.schemas.msdl.*;
import org.radixware.schemas.types.Int;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPieceFixedLen;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import org.radixware.schemas.types.Str;

public final class SmioFieldStructure extends SmioField {

    private void doMerge(SmioField f, XmlObject obj, ExtByteBuffer out, boolean force) throws SmioException {
        SmioFieldStructure ss = null;
        if (f.getField().isSetAbstract() && f.getField().getAbstract().booleanValue()) {
            //the field is abstract - do nothing
        } else {
            try {
                if (f instanceof SmioFieldStructure) {
                    ss = (SmioFieldStructure) f;
                    if (!ss.mergeForcedly && force) {
                        ss.mergeForcedly = true;
                    }
                }
                ByteBuffer field = f.merge(obj);
                fieldList.mergeField(out, field);
            } finally {
                if (ss != null) {
                    if (force && ss.mergeForcedly) {
                        ss.mergeForcedly = false;
                    }
                }
            }
        }
        f.used = true;
    }

    private boolean mustBeMerged(XmlObject obj, SmioField f) {
        return fieldExists(obj, f) || f.getField().getIsRequired() || mergeForcedly;
    }

    private boolean sequnentialFieldCanBeMerged(MsdlField cur) {
        SmioField f = cur.getFieldModel().getParser();
        if (f instanceof SmioFieldSimple) {
            SmioFieldSimple sf = (SmioFieldSimple) f;
            XmlObject dv = sf.getDefaultVal();
            byte[] zeroIndicator = sf.getNullIndicator();

            boolean hasPad = cur.getFieldModel().getPadBin() != null || cur.getFieldModel().getPadChar() != null;
            if (zeroIndicator == null) {
                zeroIndicator = cur.getFieldModel().getNullIndicator(true);
            }
            if ((dv == null && zeroIndicator == null && !hasPad)
                    && !f.getField().getIsRequired()) {
                return false;
            }
        }
        return true;
    }

    private void mergeBerField(Structure.Field sf, BERManipulator manip, SmioField f, XmlObject obj, ExtByteBuffer out) throws SmioException {
        ExtByteBuffer content = new ExtByteBuffer();
        if (sf.isSetExtId()) {
            manip.setFieldName(content, sf.getExtId());
        } else {
            manip.setFieldName(content, f.getModel().getName());
        }
        ByteBuffer field = f.merge(obj);
        f.used = true;
        content.extPut(manip.lengthBytes(field.limit()));
        content.extPut(field);
        if (getField().getStructure().getFieldNaming().getBerTLV().isSetEncoding()) {
            String encoding = getField().getStructure().getFieldNaming().getBerTLV().getEncoding();
            if (encoding != null) {
                if (encoding.equals(EncodingDef.HEX.toString())) {
                    String asciiStr = Hex.encode(content.getByteBuffer());
                    content = new ExtByteBuffer();
                    content.extPut(ByteBuffer.wrap(asciiStr.getBytes()));
                } else if (encoding.equals(EncodingDef.HEX_EBCDIC.toString())) {
                    String asciiStr = Hex.encode(content.getByteBuffer());
                    SmioCoder coder = new SmioCoder(encoding);
                    content = new ExtByteBuffer();
                    content.extPut(ByteBuffer.wrap(coder.encode(asciiStr)));
                }
            }
        }
        fieldList.mergeField(out, content.flip());
    }

    private void mergeNamedField(ExtByteBuffer fieldBuf, SmioField f, XmlObject obj) throws SmioException {
        if (getModel().isTemplateInstance()) {
            fieldBuf.extPut(pieceNaming.merge(ByteBuffer.wrap(getModel().getFieldDescriptorList().getIdByField(f))));
        } else {
            fieldBuf.extPut(pieceNaming.merge(ByteBuffer.wrap(fieldNamesContainer.getExtId(f))));
        }
        if (pieceNaming instanceof SmioPieceSeparated) {
            SmioPieceSeparated sp = (SmioPieceSeparated) pieceNaming;
            Byte ch = sp.getShield();
            //для экранирования значения поля используются те же настройки что для экранирования имени
            //если имя separated, то настройки экранирования наследуются из Formating Defaults / Separated
            if (ch != null) {
                fieldBuf.shieldedList = sp.getShieldeList();
                fieldBuf.shield = ch;
                fieldBuf.isHex = sp.getShieldedIsHex();
            }
        }
        fieldBuf.extPut(f.merge(obj));
    }

    /**
     * Merge named field, filling field list properly
     *
     * @param fieldBuf - where this member will be merged
     * @param out - fieldBuf + merged field list member
     * @param f - field being merged
     * @param obj - object to extract mergeable information from
     * @throws SmioException
     */
    private void mergeEntireNamedField(ExtByteBuffer fieldBuf, ExtByteBuffer out, SmioField f, XmlObject obj) throws SmioException {
        mergeNamedField(fieldBuf, f, obj);
        fieldList.mergeField(out, fieldBuf.flip());
    }

    private boolean bothHaveSeparatedFieldList(SmioField f) {
        SmioFieldStructure sf = (SmioFieldStructure) f;
        return (sf.fieldList instanceof SeparatedFieldList) && (fieldList instanceof SeparatedFieldList);
    }

    private boolean bothHaveSameShield(SeparatedFieldList currentSFL, SeparatedFieldList childFieldSFL) {
        return (currentSFL.getShield() == null && childFieldSFL.getShield() == null)
                || (currentSFL.getShield() != null && childFieldSFL.getShield() != null
                && currentSFL.getShield().equals(childFieldSFL.getShield()));
    }

    private boolean bothHaveSameSepparatorAndShield(SmioField f) {
        SmioFieldStructure sf = (SmioFieldStructure) f;
        SeparatedFieldList childFieldSFL = (SeparatedFieldList) sf.fieldList;
        SeparatedFieldList currentSFL = (SeparatedFieldList) fieldList;
        return childFieldSFL.getSeparator() == currentSFL.getSeparator()
                && bothHaveSameShield(currentSFL, childFieldSFL);
    }

    private void parseSequentialField(SmioField f, IDataSource ids, XmlObject obj) throws SmioException, IOException {
        if (f instanceof SmioFieldStructure
                && bothHaveSeparatedFieldList(f)
                && bothHaveSameSepparatorAndShield(f)) {
            f.parse(obj, ids, false);
            f.used = true;
        }
        if (!f.used) {
            IDataSource idsField = fieldList.parseField(ids);
            f.parse(obj, idsField);
            if (fieldList instanceof SeparatedFieldList && idsField.available() > 0) {
                throw new SmioException("Incorrect length", f.getModel().getName());
            }
            f.used = true;
        }
    }

    private void parseEmptySequentialFieldList(IDataSource ids) throws SmioException, IOException {
        //RADIX-7146
        IDataSource idsField = fieldList.parseField(ids);
        getPiece().parse(idsField);
    }

    private void parseSingleNamedField(IDataSource ff, SmioField f, XmlObject obj) throws SmioException, IOException {
        if (pieceNaming instanceof SmioPieceSeparated) {
            SmioPieceSeparated sp = (SmioPieceSeparated) pieceNaming;
            Byte sh = sp.getShield();
            if (sh != null) {
                ff.setShieldedList(sp.getShieldeList());
                ff.setShield(sh);
                ff.setShieldIsHex(sp.getShieldedIsHex());
            }
        }
        f.parse(obj, ff);
        f.used = true;
    }

    private void omitUnknownNamedField(IDataSource ff, BinArr arr) throws SmioError, LogConfigurationException, IOException, SmioException {
        //omit field, that is not explicitly speficified
        if (getField().getStructure().isSetDefaultLengthFormat()) {
            SmioFieldInt omitLen = new SmioFieldInt(new IntFieldModel(getModel().getMsdlField(), getField().getStructure().getDefaultLengthFormat()));
            Int i = Int.Factory.newInstance();
            omitLen.parse(i, ff);
            byte[] omitted = ff.get(i.getBigIntegerValue().intValue());
            String omittedName = Hex.encode(arr.data);
            String omittedBytes = Hex.encode(omitted);

            LogFactory.getLog(SmioFieldStructure.class).debug(String.format("Unknown field: %s\nRaw data: %s", omittedName, omittedBytes));
        } else if (!(getPiece() instanceof SmioPieceSeparated)) {
            //If parent structure (e.g. this structure) has Separated field format,
            //unknown field can successfully be consumed without any warnings
            LogFactory.getLog(SmioFieldStructure.class).debug(String.format("Uknown field with raw name %s in structure %s", Hex.encode(arr.data), getField().getName()));
        }
    }

    private byte[] parseSingleBitmapField(MsdlField cur, boolean[] bitmap, IDataSource ids, XmlObject obj) throws IOException, SmioException {
        byte[] bitmapPortion = null;
        int idx = bitmapBlocks.getIdxByName(cur.getName());

        if (bitmapBlocks.getBlocksCount() > 2 && !bitmapBlocks.isIndexWithinProcessedBlocks(idx) && bitmapBlocks.hasPendingBlocks()) {
            //parse another bitmap portion, than continue
            bitmapPortion = bitmapBlocks.parse(ids, bitmap);
        }

        if (bitmap[idx]) {
            if (ids.available() <= 0) {
                String fn = cur.getModel().getMsdlField().getQualifiedName();
                throw new SmioException("Not enough data for field: '" + fn + "'");
            }
            parseSequentialField(cur.getFieldModel().getParser(), ids, obj);
        }

        return bitmapPortion;
    }

    private void mergeSeqentialField(MsdlField cur, XmlObject obj, Queue<SmioField> passed, ExtByteBuffer out) throws SmioException {
        if (cur.getFieldModel().getField().isSetAbstract() && (cur.getFieldModel().getField().getAbstract().booleanValue())) {
            SmioField f = cur.getFieldModel().getParser();
            f.used = true;
            passed.add(f);
            return;
        }
        if (!sequnentialFieldCanBeMerged(cur) && !fieldExists(obj, cur.getFieldModel().getParser())) {
            throw new SmioException("No value, default value and null indicator specified for optional field", cur.getName());
        }

        SmioField f = cur.getFieldModel().getParser();
        boolean must = mustBeMerged(obj, f);
        if (must) {
            Iterator<SmioField> it = passed.iterator();
            while (it.hasNext()) {
                SmioField ff = it.next();
                doMerge(ff, obj, out, true);
                it.remove();
            }
            doMerge(f, obj, out, false);
        } else {
            passed.add(f);
        }
    }

    private boolean checkFieldsUsed() {
        boolean fUsed = false;
        if (getModel().isTemplateInstance()) {
            for (MsdlFieldDescriptor d : getModel().getFieldDescriptorList()) {
                if (wasFieldUser(d.getMsdlField())) {
                    fUsed = true;
                    break;
                }
            }
        } else {
            for (MsdlField cur : getModel().getFields()) {
                if (wasFieldUser(cur)) {
                    fUsed = true;
                    break;
                }
            }
        }
        return fUsed;
    }

    private byte[] parseSingleBitmapFieldAndPossiblyBitmap(MsdlField cur, boolean[] bitmap, IDataSource ids, XmlObject obj, byte[] byteBitmap) throws SmioException, IOException {
        byte[] portion = parseSingleBitmapField(cur, bitmap, ids, obj);
        return appendBitmapPortion(byteBitmap, portion);
    }

    private class UnionContainer {

        public XmlObject container = null;
        public XmlCursor containerCursor = null;

        public UnionContainer(String Namespace, String elementName) {
            container = XmlObject.Factory.newInstance();
            containerCursor = container.newCursor();
            containerCursor.toStartDoc();
            containerCursor.toNextToken();
            containerCursor.beginElement(elementName, Namespace);
        }
    }

    private void mergeSingleNamedField(XmlObject obj, SmioField f, ExtByteBuffer out) throws SmioException {
        if (mustBeMerged(obj, f)) {
            ExtByteBuffer fieldBuf = new ExtByteBuffer();
            if (f.getField().getIsFieldUnion() != null && f.getField().getIsFieldUnion().booleanValue()) {
                XmlObject[] needed = obj.selectChildren(namespace, f.getField().getName());

                for (XmlObject n : needed) {
                    UnionContainer uc = new UnionContainer(namespace, f.getField().getName());

                    XmlCursor contentsCursor = n.newCursor();
                    contentsCursor.toFirstChild();
                    contentsCursor.copyXml(uc.containerCursor);
                    mergeEntireNamedField(fieldBuf, out, f, uc.container);

                    while (contentsCursor.toNextSibling()) {
                        fieldBuf = new ExtByteBuffer();
                        uc = new UnionContainer(namespace, f.getField().getName());
                        contentsCursor.copyXml(uc.containerCursor);
                        mergeEntireNamedField(fieldBuf, out, f, uc.container);
                    }
                }
            } else {
                mergeNamedField(fieldBuf, f, obj);
                fieldList.mergeField(out, fieldBuf.flip());
            }
            f.used = true;
        }
    }
    private SmioPiece pieceNaming;
    private FieldNamesContainer fieldNamesContainer = new FieldNamesContainer();
    private BitmapBlocks bitmapBlocks;
    private IFieldList fieldList;
    private EFieldsFormat structureType;
    private boolean mergeForcedly = false;

    public EFieldsFormat getStructureType() {
        return structureType;
    }

    public SmioFieldStructure(StructureFieldModel model) throws SmioError {
        super(model);
        structureType = EFieldsFormat.SEQUENTALL_ORDER;
        try {
            bitmapBlocks = null;
            if (getField().getStructure().isSetBitmap()) {
                bitmapBlocks = new BitmapBlocks(model);
                structureType = EFieldsFormat.BITMAP;
            }

            pieceNaming = null;

            if (getField().getStructure().isSetFieldNaming()) {
                FieldNaming fn = getField().getStructure().getFieldNaming();
                if (fn.isSetBerTLV()) {
                    structureType = EFieldsFormat.BERTLV;
                } else {
                    structureType = EFieldsFormat.FIELD_NAMING;
                    pieceNaming = SmioPiece.Factory.newInstance(this, fn.getPiece());
                    for (MsdlStructureField cur : getModel().getFields()) {
                        fieldNamesContainer.add(cur);
                    }
                }
            }
            Byte shield = null;
            byte[] shieldArr = getModel().getShield(true);
            if (shieldArr != null && shieldArr.length > 0) {
                shield = shieldArr[0];
            }
            Byte fieldSeparator = null;
            if (((StructureField) getModel().getField()).getStructure().isSetFieldSeparator() && ((StructureField) getModel().getField()).getStructure().getFieldSeparator().length > 0) {
                fieldSeparator = ((StructureField) getModel().getField()).getStructure().getFieldSeparator()[0];
            }
            String format = getModel().getShieldedFormat(true);
            boolean ff = false;
            if (format != null) {
                ff = SeparatedDef.ShieldedFormat.Enum.forString(format) == SeparatedDef.ShieldedFormat.HEX;
            }
            if (fieldSeparator == null) {
                fieldList = new PlainFieldList();
            } else {
                fieldList = new SeparatedFieldList(fieldSeparator, shield);
            }
        } catch (SmioError e) {
            throw new SmioError(initError, e, getModel().getName());
        }
    }

    @Override
    public StructureFieldModel getModel() {
        return (StructureFieldModel) super.getModel();
    }

    @Override
    public StructureField getField() {
        return (StructureField) super.getField();
    }

    private boolean fieldExists(XmlObject obj, SmioField field) {
        //boolean res = obj.selectPath("declare namespace s='" + namespace + "' s:" + field.getModel().getName()).length > 0;
        final QName qname = new QName(namespace, field.getModel().getName());
        boolean res = obj.selectChildren(qname).length > 0;
        return res;
    }

    private SmioField findFieldByName(String name) {
        for (MsdlField cur : getModel().getFields()) {
            if (cur.getName().compareTo(name) == 0) {
                return cur.getFieldModel().getParser();
            }
        }

        if (getModel().isTemplateInstance()) {
            for (MsdlFieldDescriptor d : getModel().getFieldDescriptorList()) {
                if (d.getMsdlField().getName().compareTo(name) == 0) {
                    return d.getMsdlField().getFieldModel().getParser();
                }
            }
        }
        return null;
    }

    @Override
    public void parseField(XmlObject obj, IDataSource ids, boolean containsOddEl) throws SmioException, IOException {
        clearFieldUsed();
        switch (structureType) {
            case BITMAP:
                MsdlStructureHeaderFields header = getModel().getHeaderFields();
                if (header != null) {
                    for (MsdlField cur : header) {
                        SmioField f = cur.getFieldModel().getParser();
                        f.parse(obj, ids);
                    }
                }
//                boolean[] bitmap = new boolean[((bitmapBlocks.size + 7)/8)*8];
                boolean[] bitmap = new boolean[bitmapBlocks.size];
                Arrays.fill(bitmap, false);
                bitmapBlocks.reset();

                BinHex bh = BinHex.Factory.newInstance();
                byte[] byteBitmap = bitmapBlocks.parse(ids, bitmap);

                XmlObject b = obj.selectAttribute(namespace, "Bitmap");
                if (b == null) {
                    XmlCursor c = obj.newCursor();
                    c.toNextToken();
                    c.insertAttribute("Bitmap", namespace);
                    c.dispose();
                    b = obj.selectAttribute(namespace, "Bitmap");
                }

                for (int i = bitmapBlocks.hasBitmapIsContinue() ? 1 : 0; i < bitmap.length; i++) {
                    final int idx = i + 1;
                    final String name = "F" + idx;
                    final MsdlField cur = getModel().isTemplateInstance()
                            ? getModel().getFieldDescriptorList().getMsdlFieldByName(name)
                            : getModel().getFields().get(name);
                    if (bitmap[i] && cur == null) {
                        throw new SmioException("Unsupported field #" + idx + " defined in bitmap", name);
                    }
                }

                if (getModel().isTemplateInstance()) {
                    for (MsdlFieldDescriptor curDescr : getModel().getFieldDescriptorList()) {
                        final MsdlField cur = curDescr.getMsdlField();
                        byteBitmap = parseSingleBitmapFieldAndPossiblyBitmap(cur, bitmap, ids, obj, byteBitmap);
                    }
                } else {
                    for (MsdlField cur : getModel().getFields()) {
                        if (cur.getFieldModel().getField() instanceof StructureField) {
                            StructureField sf = (StructureField) cur.getFieldModel().getField();
                            if (sf.isSetAbstract() && sf.getAbstract() == Boolean.TRUE) {
                                continue;
                            }
                        }
                        try {
                            byteBitmap = parseSingleBitmapFieldAndPossiblyBitmap(cur, bitmap, ids, obj, byteBitmap);
                        } catch (NumberFormatException ex) {
                            throw ex;
                        }
                    }
                }

                bh.setByteArrayValue(byteBitmap);
                b.set(bh);

                break;
            case SEQUENTALL_ORDER:
                if (getModel().isTemplateInstance()) {
                    MsdlFieldDescriptorsList dl = getModel().getFieldDescriptorList();
                    if (!dl.isEmpty()) {
                        for (MsdlFieldDescriptor d : dl) {
                            SmioField f = d.getMsdlField().getFieldModel().getParser();
                            parseSequentialField(f, ids, obj);
                        }
                    } else {
                        parseEmptySequentialFieldList(ids);
                    }
                } else {
                    MsdlStructureFields fields = getModel().getFields();
                    if (!fields.isEmpty()) {
                        for (MsdlField cur : getModel().getFields()) {
                            SmioField f = cur.getFieldModel().getParser();
                            if (ids.available() <= 0 && !f.getField().getIsRequired()) {
                                break;
                            }
                            parseSequentialField(f, ids, obj);
                        }
                    } else {
                        parseEmptySequentialFieldList(ids);
                    }
                }
                break;
            case DBF:
                break;
            case BERTLV:
                while (ids.available() > 0) {
                    BERManipulator manip = new BERManipulator();
                    DataSourceByteBuffer rIds = new DataSourceByteBuffer(ids.getByteBuffer());
                    if (getField().getStructure().getFieldNaming().getBerTLV().isSetEncoding()) {
                        String encoding = getField().getStructure().getFieldNaming().getBerTLV().getEncoding();
                        if (encoding != null) {
                            if (encoding.equals(EncodingDef.HEX.toString())) {
                                String bytesStr = new String(ids.getAll());
                                byte[] decoded = Hex.decode(bytesStr);
                                rIds = new DataSourceByteBuffer(decoded);
                            } else if (encoding.equals(EncodingDef.HEX_EBCDIC.toString())) {
                                String bytesStr = new String(ids.getAll(), "Cp1047");
                                byte[] decoded = Hex.decode(bytesStr);
                                rIds = new DataSourceByteBuffer(decoded);
                            }
                        }
                    }
                    while (rIds.available() > 0) {
                        String fieldName = manip.getIdentifier(rIds).toUpperCase();
                        SmioField f = findFieldByName(fieldName);
                        int len = (int) manip.getLength(rIds);
                        DataSourceByteBuffer dsbf = new DataSourceByteBuffer(rIds.get(len));
                        if (f == null) {
                            continue;
                        }
                        f.parse(obj, dsbf);
                        f.used = true;
                    }
                }
                break;
            case FIELD_NAMING:
                while (ids.available() > 0) {
                    IDataSource ff = fieldList.parseField(ids);
                    IDataSource d = pieceNaming.parse(ff);
                    BinArr arr = new BinArr(d.getAll());
                    SmioField f = fieldNamesContainer.getField(arr);
                    if (f != null) {
                        parseSingleNamedField(ff, f, obj);
                    } else if (getModel().isTemplateInstance()) {
                        SmioField tf = getModel().getFieldDescriptorList().getFieldById(arr);
                        if (tf != null) {
                            parseSingleNamedField(ff, tf, obj);
                        } else {
                            omitUnknownNamedField(ff, arr);
                        }
                    } else {
                        omitUnknownNamedField(ff, arr);
                    }

                }
                break;
        }
        checkRequired();
    }

    private boolean wasFieldUser(MsdlField cur) {
        return cur.getFieldModel().getParser().used;
    }

    @Override
    public ByteBuffer mergeField(XmlObject obj) throws SmioException {
        clearFieldUsed();
        ExtByteBuffer out = new ExtByteBuffer();
        switch (structureType) {
            case BITMAP:
                MsdlStructureHeaderFields header = getModel().getHeaderFields();
                if (header != null) {
                    for (MsdlField cur : getModel().getHeaderFields()) {
                        ByteBuffer f = cur.getFieldModel().getParser().merge(obj);
                        out.extPut(f);
                    }
                }
                boolean[] bitmap = new boolean[bitmapBlocks.size];
                for (MsdlField cur : getModel().getFields()) {
                    if (cur.getFieldModel().getField() instanceof StructureField) {
                        StructureField sf = (StructureField) cur.getFieldModel().getField();
                        if (sf.isSetAbstract() && sf.getAbstract() == Boolean.TRUE) {
                            continue;
                        }
                    }
                    bitmap[bitmapBlocks.getIdxByName(cur.getName())] = fieldExists(obj, cur.getFieldModel().getParser());
                }
                bitmapBlocks.reset();

                try {
                    bitmapBlocks.merge(out, bitmap);
                } catch (IOException ex) {
                    LogFactory.getLog(SmioFieldStructure.class).error(ex);
                }
                for (MsdlField cur : getModel().getFields()) {
                    if (cur.getFieldModel().getField() instanceof StructureField) {
                        StructureField sf = (StructureField) cur.getFieldModel().getField();
                        if (sf.isSetAbstract() && sf.getAbstract() == Boolean.TRUE) {
                            continue;
                        }
                    }

                    SmioField f = cur.getFieldModel().getParser();

                    if (bitmapBlocks.mustMergeNextBitmapBlock(bitmapBlocks.getIdxByName(cur.getName()))) {
                        try {
                            bitmapBlocks.merge(out, bitmap);
                        } catch (IOException ex) {
                            LogFactory.getLog(SmioFieldStructure.class).error(ex);
                        }
                    }

                    if (mustBeMerged(obj, f)) {
                        doMerge(f, obj, out, false);
                    }
                }
                break;
            case SEQUENTALL_ORDER:
                Queue<SmioField> passed = new LinkedList<>();
                if (getModel().isTemplateInstance()) {
                    for (MsdlFieldDescriptor d : getModel().getFieldDescriptorList()) {
                        mergeSeqentialField(d.getMsdlField(), obj, passed, out);
                    }
                } else {
                    for (MsdlField cur : getModel().getFields()) {
                        mergeSeqentialField(cur, obj, passed, out);
                    }
                }

                break;
            case BERTLV:
                BERManipulator manip = new BERManipulator();
                for (MsdlField cur : getModel().getFields()) {
                    SmioField f = cur.getFieldModel().getParser();
                    if (mustBeMerged(obj, f)) {
                        Structure.Field sf = ((MsdlStructureField) (f.getModel()).getMsdlField()).getField();
                        if (f.getField().isSetIsFieldArray1()) {
                            XmlObject arr[] = obj.selectChildren(namespace, f.getField().getName());
                            if (arr.length > 1) {
                                String name = f.getField().getName();
                                for (XmlObject child : arr) {
                                    XmlObject wrapper = XmlObject.Factory.newInstance();
                                    XmlCursor cursor = wrapper.newCursor();
                                    cursor.toNextToken();
                                    cursor.beginElement(name, namespace);
                                    XmlCursor cCur = child.newCursor();
                                    cCur.toNextToken();
                                    cursor.insertChars(cCur.getTextValue());
                                    mergeBerField(sf, manip, f, wrapper, out);
                                    cCur.dispose();
                                    cursor.dispose();
                                }
                            } else {
                                mergeBerField(sf, manip, f, obj, out);
                            }
                        } else {
                            mergeBerField(sf, manip, f, obj, out);
                        }
                    }
                }
                break;
            case FIELD_NAMING:
                if (getModel().isTemplateInstance()) {
                    for (MsdlFieldDescriptor d : getModel().getFieldDescriptorList()) {
                        SmioField f = d.getMsdlField().getFieldModel().getParser();
                        mergeSingleNamedField(obj, f, out);
                    }
                } else {
                    for (MsdlField cur : getModel().getFields()) {
                        SmioField f = cur.getFieldModel().getParser();
                        mergeSingleNamedField(obj, f, out);
                    }
                }
                break;
        }

        if (fieldList instanceof SeparatedFieldList) {
            boolean fUsed = checkFieldsUsed();

            if (fUsed) {
                ByteBuffer bf = out.getByteBuffer();
                bf.position(bf.position() - 1);
            }
        }
        checkRequired();
        return out.flip();
    }

    public void clearFieldUsed() {
        for (MsdlField cur : getModel().getFields()) {
            cur.getFieldModel().getParser().used = false;
        }

        if (getModel().isTemplateInstance()) {
            for (MsdlFieldDescriptor d : getModel().getFieldDescriptorList()) {
                d.getMsdlField().getFieldModel().getParser().used = false;
            }
        }
    }

    public void checkRequired() throws SmioException {
        if (getModel().isTemplateInstance()) {
            for (MsdlFieldDescriptor d : getModel().getFieldDescriptorList()) {
                SmioField f = d.getMsdlField().getFieldModel().getParser();
                if (f.getField().getIsRequired() && !f.used) {
                    throw new SmioException("Required field is missing", d.getMsdlField().getName());
                }
            }
        } else {
            for (MsdlField cur : getModel().getFields()) {
                SmioField f = cur.getFieldModel().getParser();
                if (f.getField().getIsRequired() && !f.used) {
                    throw new SmioException("Required field is missing", cur.getName());
                }
            }
        }
    }

    public boolean parseAsDbf(XmlObject obj, DBFReader reader) throws SmioException, IOException {
        Object objects[] = reader.nextRecord();
        if (objects == null) {
            return false;
        }
        int i = 0;
        for (MsdlField cur : getModel().getFields()) {
            SmioField f = cur.getFieldModel().getParser();
            f.readAsDbfObject(obj, objects[i]);
            i++;
        }
        return true;
    }

    public void mergeAsDbf(XmlObject obj, DBFWriter writer) throws SmioException, IOException {
        Object objects[] = new Object[getModel().getFields().size()];
        int i = 0;
        for (MsdlField cur : getModel().getFields()) {
            SmioField f = cur.getFieldModel().getParser();
            objects[i] = f.writeAsDbfObject(obj);
            i++;
        }
        writer.addRecord(objects);
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
        try {
            if (this.getStructureType() == EFieldsFormat.BITMAP) {
                for (MsdlField cur : getModel().getFields()) {
                    String fn = cur.getName();
                    if (fn.charAt(0) == 'F' && fn.length() >= 2) {
                        try {
                            Integer.decode(fn.substring(1));
                        } catch (NumberFormatException ex) {
                            handler.accept(RadixProblem.Factory.newError(cur, "MSDL Field '" + source.getQualifiedName() + "' error: 'Wrong field name (name format must be: F<integer_field_number>)'"));
                        }
                    }
                }
            }
            if (structureType == EFieldsFormat.SEQUENTALL_ORDER) {
                for (MsdlField cur : getModel().getFields()) {
                    if (!sequnentialFieldCanBeMerged(cur)) {
                        handler.accept(RadixProblem.Factory.newWarning(source, "No default value and null indicator specified for optional field: " + cur.getName()));
                    }
                }
            }
            if (getStructureType() == EFieldsFormat.FIELD_NAMING && pieceNaming instanceof SmioPieceFixedLen) {
                int namingLen = ((SmioPieceFixedLen) pieceNaming).getLen();
                for (MsdlStructureField cur : getModel().getFields()) {
                    Structure.Field sf = (Structure.Field) cur.getField();
                    if (!sf.isSetExtId()) {
                        if (cur.getModel().getField().getName().length() != namingLen) {
                            handler.accept(RadixProblem.Factory.newWarning(source, "Implicit field tag cannot have length different, than specified explicit length of naming field: " + cur.getName()));
                        }
                    }
                }
            }

            byte[] separator = getField().getStructure().getFieldSeparator();
            if (separator != null && separator.length > 1) {
                handler.accept(RadixProblem.Factory.newWarning(source, "Field separator, specified in MSDL schema is more than one byte. Only first byte will be used."));
            }
        } catch (Throwable ex) {
            handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "error: '" + ex.getMessage() + "'"));
        }
    }

    @Override
    protected ByteBuffer getFieldRowData(XmlObject obj) throws SmioException {
        XmlObject arr[] = obj.selectChildren(namespace, elementName);
        XmlObject field = null;
        if (arr.length == 0) {
            if (mergeForcedly) {
                field = XmlObject.Factory.newInstance();
            } else {
                throw new SmioException("Xml element doesn't exist");
            }
        } else {
            field = arr[0];
        }
        return mergeField(field);
    }

    private byte[] appendBitmapPortion(byte[] alreadyProcessed, byte[] newPortion) {
        if (newPortion != null) {
            return ParseUtil.concatenateArrays(alreadyProcessed, newPortion);
        }
        return alreadyProcessed;
    }
}

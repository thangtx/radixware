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

package org.radixware.kernel.common.msdl.fields.parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.EFieldsFormat;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlStructureFields;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPiece;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPieceEmbeddedLen;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPieceFixedLen;
import org.radixware.kernel.common.msdl.fields.parser.piece.extras.BERManipulator;
import org.radixware.kernel.common.msdl.fields.parser.piece.extras.PadRemover;
import org.radixware.kernel.common.msdl.fields.parser.piece.extras.SmioPiecePadded;
import org.radixware.schemas.msdl.AlignDef;
import org.radixware.schemas.msdl.Field;
import org.radixware.schemas.msdl.IntField;

public abstract class SmioField {

    private AbstractFieldModel model;
    private SmioPiece piece;
    public boolean used;
    protected static final String initError = "Can't initialize field";
    protected static final String readError = "Can't read field";
    protected static final String writeError = "Can't write field";
    protected static final String preprocessorFunctionExecutionError = "Preprocessor function execution error";
    private Method parseMethod, mergeMethod;
    protected String namespace;
    protected String elementName;
    public int fieldByteLen;

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object compare) {
        if (compare instanceof SmioField) {
            return model.getName().equalsIgnoreCase(((SmioField) compare).getModel().getName());
        } else {
            return false;
        }
    }

    public Field getField() {
        return getModel().getField();
    }

    @Override
    public int hashCode() {
        return model.getName().hashCode();
    }

    public AbstractFieldModel getModel() {
        return model;
    }

    public SmioPiece getPiece() {
        piece = null;
        piece = SmioPiece.Factory.newInstance(this, model.getField());
        return piece;
    }

    public SmioField(AbstractFieldModel model) throws SmioError {
        try {
            this.model = model;
            elementName = model.getName();
            if (model.getContainer() instanceof RootMsdlScheme) {
                elementName = "MessageInstance";
            }

            piece = null;
            namespace = model.getRootMsdlScheme().getNamespace();
            Class preprocessorClass = model.getRootMsdlScheme().getPreprocessorClass();
            if (preprocessorClass != null) {
                if (getField().getParseFunctionName() != null) {
                    parseMethod = preprocessorClass.getMethod(getField().getParseFunctionName(), new Class[]{byte[].class});
                    if (parseMethod != null) {
                        parseMethod.setAccessible(true);
                    }
                }
                if (getField().getMergeFunctionName() != null) {
                    mergeMethod = preprocessorClass.getMethod(getField().getMergeFunctionName(), new Class[]{byte[].class});
                    if (mergeMethod != null) {
                        mergeMethod.setAccessible(true);
                    }
                }
            }

        } catch (SmioException | NoSuchMethodException | SecurityException e) {
            throw new SmioError(initError, e, model.getName());
        }
    }

    public byte[] executeParseFunction(byte[] from) throws SmioException {
        if (parseMethod != null) {
            try {
                return (byte[]) parseMethod.invoke(parseMethod, new Object[]{from});
            } catch (IllegalAccessException ex) {
                throw new SmioException(preprocessorFunctionExecutionError, ex);
            } catch (IllegalArgumentException ex) {
                throw new SmioException(preprocessorFunctionExecutionError, ex);
            } catch (InvocationTargetException ex) {
                throw new SmioException(preprocessorFunctionExecutionError, ex);
            }
        }
        return from;
    }

    public byte[] executeMergeFunction(byte[] from) throws SmioException {
        if (mergeMethod != null) {
            try {
                return (byte[]) mergeMethod.invoke(mergeMethod, new Object[]{from});
            } catch (IllegalAccessException ex) {
                throw new SmioException(preprocessorFunctionExecutionError, ex);
            } catch (IllegalArgumentException ex) {
                throw new SmioException(preprocessorFunctionExecutionError, ex);
            } catch (InvocationTargetException ex) {
                throw new SmioException(preprocessorFunctionExecutionError, ex);
            }
        }
        return from;
    }

    protected boolean isNull(XmlObject obj) {
        if (obj == null) {
            return true;
        }
        XmlCursor c = obj.newCursor();
        if (c.getTextValue().equals("")) {
            return true;
        }
        c.dispose();
        return obj.isNil();
    }

    private XmlObject getOrCreateChild(XmlObject obj) {
        XmlObject children[] = obj.selectChildren(namespace, elementName);
        final XmlObject ret ;
        if (children.length == 0) { //insert new
            insertNewCHild(obj);
            ret = obj.selectChildren(namespace, elementName)[0];
        } else if (getField().getIsFieldArray1() != null && getField().getIsFieldArray1().booleanValue()) { //an array
            insertNewCHild(obj);
            children = obj.selectChildren(namespace, elementName);
            ret = children[children.length - 1];
        } else {
            ret = children[0];
        }
        return ret;
    }

    private void insertNewCHild(XmlObject obj) {
        XmlCursor c = obj.newCursor();
        c.toEndToken();
        c.insertElement(elementName, namespace);
        c.dispose();
    }

    public XmlObject createNewChild(XmlObject obj) {
        XmlCursor c = obj.newCursor();
        if (c.currentTokenType() == XmlCursor.TokenType.STARTDOC) {
            c.toNextToken();
        } else {
            c.toEndToken();
        }
        c.insertElement(elementName, namespace);
        c.dispose();
        XmlObject o[] = obj.selectChildren(namespace, elementName);
        return o[o.length - 1];
    }

    protected void setXmlObjectToNil(XmlObject obj) {
        obj.setNil();
    }
    
     private void parseLocal(XmlObject obj, IDataSource ids) throws SmioException, IOException {
         parseLocal(obj, ids, true);
     }

    private void parseLocal(XmlObject obj, IDataSource ids, boolean prependRemaining) throws SmioException, IOException {
        IDataSource pids = null;
        SmioPiece p = null;
        if ((this.getField().getIsRequired() == null || !this.getField().getIsRequired().booleanValue()) && (ids.available() == 0)) {
            pids = ids;
        } else {
            p = getPiece();
            pids = p.parse(ids);
            if (parseMethod != null && pids instanceof DataSourceByteBuffer) {
                DataSourceByteBuffer dsbf = (DataSourceByteBuffer) pids;
                pids = new DataSourceByteBuffer(executeParseFunction(dsbf.getAll()));
            }
        }
        fieldByteLen = pids.available();
        XmlObject child = getOrCreateChild(obj);
        if (pids.available() == 0) {
            setXmlObjectToNil(child);
        } else {
            boolean containsOddEl = false;
            if (p instanceof SmioPieceEmbeddedLen) {
                containsOddEl = ((SmioPieceEmbeddedLen) p).isOddElementNeeded();
            }
            parseField(child, pids, containsOddEl);
            if (prependRemaining && pids.available() > 0 && pids.getPosition() > 0) {
                ids.prepend(pids);
            } else if (!prependRemaining) {
                ByteBuffer buf = ids.getByteBuffer();
                buf.position(buf.position() - pids.available());
            }
        }
    }
    
    public void parse(XmlObject obj, IDataSource ids) throws SmioException, IOException {
        parse(obj, ids, true);
    }

    public void parse(XmlObject obj, IDataSource ids, boolean prependRemaining) throws SmioException, IOException {
        try {
            if (!model.getField().isSetAbstract() || model.getField().getAbstract() == Boolean.FALSE) {
                parseLocal(obj, ids, prependRemaining);
            }
        } catch (SmioException ex) {
            String fName = ex.getFieldName();
            if (!(model.getContainer() instanceof RootMsdlScheme)) {
                fName = model.getName() + "." + fName;
            }
            throw new SmioException("Parse error (field name: '" + fName + "')", ex, fName);
        } catch (Throwable ex) {
            throw new SmioException("Parse error (field name: '" + model.getName() + "')", ex, model.getName());
        }
    }

    protected ByteBuffer getFieldRowData(XmlObject obj) throws SmioException {
        XmlObject arr[] = obj.selectChildren(namespace, elementName);
        if (arr.length == 0) {
            throw new SmioException("Xml element doesn't exist");
        }
        XmlObject field = arr[0];
        ByteBuffer row = mergeField(field);
        return row;
    }

    public ByteBuffer merge(XmlObject obj) throws SmioException {
        try {
            ByteBuffer row;
            if (obj.isNil()) {
                row = ByteBuffer.wrap(new byte[0]);
            } else {
                row = getFieldRowData(obj);
            }
            if (mergeMethod != null) {
                row = ByteBuffer.wrap(executeMergeFunction(ParseUtil.extractByteBufferContent(row)));
            }
            ByteBuffer formatted = getPiece().merge(row);
            if (this instanceof SmioFieldSigned) {
                SmioFieldSigned signed = (SmioFieldSigned) this;
                if (signed.sign != null) {
                    ExtByteBuffer withSign = new ExtByteBuffer();
                    if (getPiece() instanceof SmioPieceFixedLen) {
                        withSign.extPut(signed.sign);

                        //if the field is aligned to right, than we have to throw
                        //the first byte out
                        if (((SmioPiecePadded) getPiece()).getAlign() == AlignDef.RIGHT) {
                            formatted.get();
                        }

                        withSign.extPut(formatted);

                        //y.snegirev now we need to remove padding from withSign
                        DataSourceByteBuffer bfPadded = new DataSourceByteBuffer(withSign.flip());
                        SmioPieceFixedLen p = (SmioPieceFixedLen) getPiece();
                        DataSourceByteBuffer unpadded = PadRemover.removePadding(p, bfPadded, p.getLen());
                        formatted = unpadded.getByteBuffer();
                    } else {
                        withSign.extPut(signed.sign);
                        withSign.extPut(formatted);
                        formatted = withSign.getByteBuffer();
                        formatted.flip();
                    }
                }
            }
            return formatted;
        } catch (SmioException ex) {
            String fName = ex.getFieldName();
            if (!(model.getContainer() instanceof RootMsdlScheme)) {
                fName = model.getName() + "." + fName;
            }
            throw new SmioException("Merge error (field name: '" + fName + "')", ex, fName);
        } catch (Throwable ex) {
            throw new SmioException("Merge error (field name: '" + model.getName() + "')", ex, model.getName());
        }
    }

    public abstract void parseField(XmlObject obj, IDataSource ids, boolean containsOddElement) throws SmioException, IOException;

    public abstract ByteBuffer mergeField(XmlObject obj) throws SmioException;

    public void readAsDbfObject(XmlObject obj, Object object) throws SmioException, IOException {
        throw new SmioError("Wrong field type");
    }

    public Object writeAsDbfObject(final XmlObject obj) throws SmioException, IOException {
        throw new SmioError("Wrong field type");
    }

    public int getMinFieldLen() {
        return 0;
    }

    /**
     * Get length of structured field's children (RADIX-5561). At the moment,
     * supports FIXED_LEN fields only!
     *
     * @return length of structured field's children
     */
    public int getChildrenLen() {
        int ret = 0;
        if (getPiece() instanceof SmioPieceFixedLen) {
            int childrenLen = 0;
            AbstractFieldModel fieldModel = getModel();
            if (fieldModel.getMsdlField().hasChildren()
                    && fieldModel instanceof StructureFieldModel) {
                StructureFieldModel sfm = (StructureFieldModel) fieldModel;
                childrenLen = countStructureFieldLength(sfm.getFields());
            } else if (getField() instanceof IntField) {
                childrenLen = 1; //Int field 
            }
            if (childrenLen > 0) {
                ret = childrenLen;
            }
        }
        return ret;

    }

    private int countStructureFieldLength(MsdlStructureFields fields) {
        int ret = 0;
        for (MsdlField f : fields) {
            SmioPiece pp = f.getFieldModel().getParser().getPiece();
            if (pp instanceof SmioPieceFixedLen/*
                     * || piece instanceof SmioPieceEmbeddedLen
                     */) {
                int len = ((SmioPieceFixedLen) pp).getLen();
                if (f.getFullField().isSetInt() && !f.getFullField().isSetStructure() /*
                         * && !f.getField().isNil()
                         */ && len == 0) {
                    len = 1;
                }
                ret += len;
            }
        }
        return ret;
    }

    public void check(RadixObject source, IProblemHandler handler) {
        try {
            getPiece().check(source, handler);
            MsdlField parent = getModel().getMsdlField().getParentMsdlField();
            if (parent != null && parent.getType() == EFieldType.STRUCTURE) {
                StructureFieldModel m = (StructureFieldModel) parent.getFieldModel();
                EFieldsFormat fieldFormat = m.getStructureType();
                if (fieldFormat == EFieldsFormat.BERTLV || fieldFormat == EFieldsFormat.FIELD_NAMING) {
                    if (fieldFormat == EFieldsFormat.BERTLV) {
                        try {
                            new BERManipulator().getTagValue(getField().getName());
                        } catch (SmioException e) {
                            handler.accept(RadixProblem.Factory.newWarning(getModel().getMsdlField(), "Incorrect BerTLV field name: " + getField().getName()));
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            handler.accept(RadixProblem.Factory.newError(source, ex.getMessage()));
        }
    }

    public boolean getIsBCH() {
        return false;
    }

    public boolean getIsBSD() throws SmioException {
        return false;
    }

    public boolean getIsHex() throws SmioException {
        return false;
    }

    public boolean needOddElementLen() {
        return false;
    }
}

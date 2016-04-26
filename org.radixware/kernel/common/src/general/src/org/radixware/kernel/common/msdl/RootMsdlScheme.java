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

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFWriter;
import java.io.IOException;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.msdl.fields.parser.structure.SmioFieldStructure;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.msdl.Message;
import org.radixware.schemas.msdl.Structure;


public class RootMsdlScheme extends MsdlField {

    private Structure fictiveParentStructure = null;
    private String classTargetNameSpace = null;
    private String converterClassGuid = null;
    private final Class preprocessorClass;

    public RootMsdlScheme(Message from) {
        super(from);
        classTargetNameSpace = from.getClassTargetNamespace();
        converterClassGuid = from.getConvertorClassGUID();
        preprocessorClass = null;
//        if (converterClassGuid != null) {
//            throw new IllegalStateException("Preprocessor class will be unavailable: missing ADS classloader reference");
//        }
    }

    public RootMsdlScheme(ClassLoader adsClassLoader, Message from) throws SmioException {
        super(from);
        classTargetNameSpace = from.getClassTargetNamespace();
        converterClassGuid = from.getConvertorClassGUID();
        Class clazz = null;
        if (converterClassGuid != null) {
            Id id = Id.Factory.loadFrom(converterClassGuid);
            try {
                clazz = ((IRadixClassLoader) adsClassLoader).loadExecutableClassById(id);
            } catch (ClassNotFoundException ex) {
                throw new SmioException("Preprocessor class not found", ex);
            }
        }
        preprocessorClass = clazz;
    }

    public Structure getFictiveParentStructure() {
        return fictiveParentStructure;
    }

    public StructureFieldModel getStructureFieldModel() {
        return (StructureFieldModel) getModel();
    }

    public void setPreprocessorClassGuid(String guid) {
        converterClassGuid = guid;
    }

    public String getPreprocessorClassGuid() {
        return converterClassGuid;
    }

    public Class getPreprocessorClass() throws SmioException {
        return preprocessorClass;
    }

    public String getNamespace() {
        return classTargetNameSpace;
    }

    public void setNamespace(String namespace) {
        classTargetNameSpace = namespace;
        setModified();
    }

    public Message getMessage() {
        Message message = Message.Factory.newInstance();
        message.set(getModel().getFullField());
        message.setClassTargetNamespace(classTargetNameSpace);
        message.setConvertorClassGUID(converterClassGuid);
        return message;
    }

    public void setFictiveParentStructure(Structure fictiveParentStructure) {
        this.fictiveParentStructure = fictiveParentStructure;
    }

    public boolean isDbf() {
        return getStructureFieldModel().getStructure().isSetDbf();
    }

    public ComboBoxModel getFieldList(boolean isDbf) {
        DefaultComboBoxModel res = new DefaultComboBoxModel();
        if (isDbf) {
            res.addElement(EFieldType.STR);
            res.addElement(EFieldType.NUM);
            res.addElement(EFieldType.DATETIME);
            res.addElement(EFieldType.BOOLEAN);
        } else {
            res.addElement(EFieldType.STR);
            res.addElement(EFieldType.NUM);
            res.addElement(EFieldType.DATETIME);
            res.addElement(EFieldType.BCH);
            res.addElement(EFieldType.BIN);
            res.addElement(EFieldType.INT);
            res.addElement(EFieldType.SEQUENCE);
            res.addElement(EFieldType.CHOICE);
            res.addElement(EFieldType.STRUCTURE);
        }
        return res;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
    }

    public boolean readDbfRecord(DBFReader reader, XmlObject object) throws IOException, SmioException {
        final SmioFieldStructure parser = (SmioFieldStructure) (getStructureFieldModel().getParser());
        return parser.parseAsDbf(object, reader);
    }

    public void writeDbfRecord(DBFWriter writer, XmlObject object) throws IOException, SmioException {
        ((SmioFieldStructure) (getStructureFieldModel().getParser())).mergeAsDbf(object, writer);
    }

    public DBFField[] getDBFFields() {
        return getStructureFieldModel().getFields().getDBFFields();
    }

    @Override
    public MsdlField getParentMsdlField() {
        return null;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

    @Override
    public String getQualifiedName() {
        RadixObject container = getContainer();
        if (container != null) {
            return container.getQualifiedName();
        }
        return super.getQualifiedName();
    }
}

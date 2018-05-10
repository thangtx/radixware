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
import java.util.EnumSet;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.common.msdl.fields.parser.ISmioParserFactory;
import org.radixware.kernel.common.msdl.fields.parser.SmioParserFactory;
import org.radixware.kernel.common.msdl.fields.parser.structure.SmioFieldStructure;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.msdl.Message;
import org.radixware.schemas.msdl.Structure;


public class RootMsdlScheme extends MsdlField {

    private Structure fictiveParentStructure = null;
    private String classTargetNameSpace = null;
    private String converterClassGuid = null;
    private boolean forcedUseFictiveParentStructure;
    private IMsdlPreprocessorAccess preprocessorAccess;
    private final ISmioParserFactory parserFactory;
    private final IRadixTrace trace;
    
    public static enum EFictiveStructParams {
        FORCED_USE, MERGE_WITH_ROOT_STRUCTURE
    }
    
    public RootMsdlScheme(Message from) {
        super(from);
        classTargetNameSpace = from.getClassTargetNamespace();
        converterClassGuid = from.getConvertorClassGUID();
        parserFactory = initParserFactory();
        preprocessorAccess = null;
        this.trace = IRadixTrace.Lookup.findInstance(null);
    }

    public RootMsdlScheme(ClassLoader adsClassLoader, Message from) throws SmioException {
        super(from);
        classTargetNameSpace = from.getClassTargetNamespace();
        converterClassGuid = from.getConvertorClassGUID();
        parserFactory = initParserFactory();
        Class clazz = null;
        if (converterClassGuid != null) {
            Id id = Id.Factory.loadFrom(converterClassGuid);
            try {
                clazz = ((IRadixClassLoader) adsClassLoader).loadExecutableClassById(id);
            } catch (ClassNotFoundException ex) {
                throw new SmioException("Preprocessor class not found", ex);
            }
        }
        if (clazz != null) {
            preprocessorAccess = new MsdlPreprocessorAccessDefault(clazz);
        }
        this.trace = IRadixTrace.Lookup.findInstance(null);
    }
    
    public IRadixTrace getTrace() {
        return trace;
    }
    
    private ISmioParserFactory initParserFactory() {
        return new SmioParserFactory();
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

    public void setPreprocessorAccess(IMsdlPreprocessorAccess preprocessorAccess) {
        this.preprocessorAccess = preprocessorAccess;
    }
    
    public IMsdlPreprocessorAccess getPreprocessorAccess() {
        return preprocessorAccess;
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
    
    public void setFictiveParentStructure(Structure fictiveParentStructure, EnumSet<EFictiveStructParams> params) {
        if (params != null) {
            forcedUseFictiveParentStructure = params.contains(EFictiveStructParams.FORCED_USE);
            if (params.contains(EFictiveStructParams.MERGE_WITH_ROOT_STRUCTURE)) {
                this.fictiveParentStructure = new FictiveStructureDelegate(fictiveParentStructure, ((StructureFieldModel) getFieldModel()).getStructure(), forcedUseFictiveParentStructure);
            } else {
                this.fictiveParentStructure = fictiveParentStructure;    
            }
        } else {
            this.fictiveParentStructure = fictiveParentStructure;
        }
        getFieldModel().clearParser();
    }

    public void setFictiveParentStructure(Structure fictiveParentStructure) {
        setFictiveParentStructure(fictiveParentStructure, null);
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
        return true;
    }

    @Override
    public String getQualifiedName() {
        RadixObject container = getContainer();
        if (container != null) {
            return container.getQualifiedName();
        }
        return super.getQualifiedName();
    }
    
    public void setForcedUseFictiveParentStructure(boolean forcedUseFictiveParentStructure) {
        if (this.forcedUseFictiveParentStructure == forcedUseFictiveParentStructure) {
            return;
        }
        this.forcedUseFictiveParentStructure = forcedUseFictiveParentStructure;
        if (fictiveParentStructure instanceof FictiveStructureDelegate) {
            final FictiveStructureDelegate xDelegate = (FictiveStructureDelegate) fictiveParentStructure;
            xDelegate.setIsForcedUseMode(forcedUseFictiveParentStructure);
        }
        getFieldModel().clearParser();
    }

    public boolean isFictiveStructureMergedWithRoot() {
        return fictiveParentStructure instanceof FictiveStructureDelegate;
    }
    
    public boolean isForcedUseFictiveParentStructure() {
        return forcedUseFictiveParentStructure;
    }
    
    public ISmioParserFactory getParserFactory() {
        return parserFactory;
    }
}

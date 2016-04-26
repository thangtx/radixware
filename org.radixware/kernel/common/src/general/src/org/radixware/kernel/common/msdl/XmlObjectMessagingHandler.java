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

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;

import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.ISchemeSearcher;
import org.radixware.kernel.common.msdl.fields.parser.ParseUtil;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceInputStream;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.msdl.Structure;

public final class XmlObjectMessagingHandler {
    
    static public DBFWriter openDbfFileWriter(XmlObject obj) throws SmioException {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        try {
            DBFWriter writer = new DBFWriter();
            writer.setFields(def.getDBFFields());
            return writer;
        } catch (DBFException ex) {
            throw new SmioException("Can't create DBFWriter", ex);
        }
    }
    
    static public void writeNextRecordToDbf(XmlObject obj, DBFWriter writer) throws IOException, SmioException {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.writeDbfRecord(writer, obj);
    }
    
    static public void closeDbfFile(XmlObject obj, DBFWriter writer) throws SmioException {
        try {
            writer.write();
        } catch (DBFException ex) {
            throw new SmioException("Can't close dbf", ex);
        }
    }
    
    static public DBFReader openDbfReader(XmlObject obj, InputStream stream) throws SmioException {
        try {
            return new DBFReader(stream);
        } catch (DBFException ex) {
            throw new SmioException("Can't create DBFReader", ex);
        }
    }
    
    static public boolean readNextRecordFromDbf(XmlObject obj, DBFReader reader) throws IOException, SmioException {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.readDbfRecord(reader, obj);
        return false;
    }
    
    static public void readStructuredMessageFromInput(XmlObject obj, InputStream inp) throws IOException, SmioException, SmioError {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(null);
        SmioField sf = def.getFieldModel().getParser();
        IDataSource ds = new DataSourceInputStream(inp);
        sf.parse(obj, ds);
    }
    
    static public void readStructuredMessageFromInputWithClear(XmlObject obj, InputStream inp) throws IOException, SmioException, SmioError {
        clearXml(obj);
        readStructuredMessageFromInput(obj, inp);
    }
    
    static public void readStructuredMessage(XmlObject obj, final byte[] inp) throws SmioException, SmioError {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(null);
        SmioField sf = def.getFieldModel().getParser();
        IDataSource ds = new DataSourceByteBuffer(inp);
        try {
            sf.parse(obj, ds);
        } catch (IOException ex) {
            Logger.getLogger(XmlObjectMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static public void readStructuredMessageWithClear(XmlObject obj, final byte[] inp) throws SmioException, SmioError {
        clearXml(obj);
        readStructuredMessage(obj, inp);
    }
    
    static public void writeStructuredMessageToOutput(XmlObject obj, OutputStream out) throws IOException, SmioError, SmioException {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(null);
        SmioField sf = def.getFieldModel().getParser();
        ByteBuffer res = sf.merge(obj);
        out.write(ParseUtil.extractByteBufferContent(res));
    }
    
    private static final class TemplateSchemeSearcher implements ISchemeSearcher {
        
        public static class SchemeInternalVisitor implements IVisitor {
            
            public AbstractFieldModel target = null;
            
            @Override
            public void accept(RadixObject radixObject) {
                if (radixObject instanceof MsdlField) {
                    target = ((MsdlField) radixObject).getFieldModel();
                } else if (radixObject instanceof AbstractFieldModel) {
                    target = (AbstractFieldModel) radixObject;
                }
            }
        }
        
        public static class SchemeInternalVisitorProvider extends VisitorProvider {
            
            String path = "/";
            RootMsdlScheme context = null;
            EFieldType type = EFieldType.STRUCTURE;
            
            public SchemeInternalVisitorProvider(String path, RootMsdlScheme context, EFieldType type) {
                this.path = path;
                this.context = context;
                this.type = type;
            }
            
            @Override
            public boolean isTarget(RadixObject radixObject) {
                boolean res = false;
                if (path == null) {
                    return false;
                }
                String[] componentsArr = path.split("/");
                List<String> components = new LinkedList<>();
                for (String s : componentsArr) {
                    if (!s.isEmpty()) {
                        components.add(s);
                    }
                }
                
                ListIterator<String> it = components.listIterator(components.size());
                RadixObject cur = radixObject;
                boolean fail = false;
                while (it.hasPrevious()) {
                    String component = it.previous();
                    if (component.equals(cur.getName())) {
                        cur = cur.getContainer();
                    } else {
                        fail = true;
                        break;
                    }
                }
                res = !fail;
                return res;
            }
            
            @Override
            public boolean isContainer(RadixObject object) {
                boolean isMsdlField = object instanceof MsdlField;
                boolean isFieldModel = object instanceof AbstractFieldModel;
                boolean isFieldContainer = object instanceof MsdlStructureFields || object instanceof MsdlVariantFields;
                
                if (isMsdlField) {
                    MsdlField f = (MsdlField) object;
                    return f.getFieldModel().getType() == type;
                }
                if (isFieldModel) {
                    return ((AbstractFieldModel) object).getType() == type;
                }
                if (isFieldContainer) {
                    return true;
                }
                
                return false;
            }
        }
        private final RootMsdlScheme context;
        
        private final Id contextId;
        private final ClassLoader cl;
        
        TemplateSchemeSearcher(XmlObject obj) {
            cl = obj.getClass().getClassLoader();
            contextId = getIdFromXmlObject(obj);
            context = getRootMsdlSchemeById(contextId, cl);
            if (context != null) {
                context.setSchemeSearcher(this);
            }
        }
        
        @Override
        public AbstractFieldModel findField(Id templateSchemeId, String templateSchemePath, EFieldType type) {
            RootMsdlScheme scheme;
            if (templateSchemeId == contextId) {
                scheme = context;
            } else {
                scheme = getRootMsdlSchemeById(contextId, cl);
            }
            if (scheme == null) {
                return null;
            }
            SchemeInternalVisitor visitor = new SchemeInternalVisitor();
            scheme.visit(visitor, new SchemeInternalVisitorProvider(templateSchemePath, context, type));
            return visitor.target;
        }
        
        RootMsdlScheme getContext() {
            return context;
        }
        
    }
    
    static public byte[] writeStructuredMessage(XmlObject obj) throws SmioError, SmioException {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(null);
        SmioField sf = def.getFieldModel().getParser();
        ByteBuffer res = sf.merge(obj);
        return ParseUtil.extractByteBufferContent(res);
    }
    
    static public void readStructuredMessageFromInputParam(XmlObject obj, InputStream inp, Structure defaults) throws IOException, SmioException, SmioError {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(defaults);
        SmioField sf = def.getFieldModel().getParser();
        IDataSource ids = new DataSourceInputStream(inp);
        sf.parse(obj, ids);
    }
    
    static public void readStructuredMessageParam(XmlObject obj, byte[] inp, Structure defaults) throws SmioException, SmioError {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(defaults);
        SmioField sf = def.getFieldModel().getParser();
        IDataSource ids = new DataSourceByteBuffer(inp);
        try {
            sf.parse(obj, ids);
        } catch (IOException ex) {
            Logger.getLogger(XmlObjectMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static public void readStructuredMessageFromInputParamWithClear(XmlObject obj, InputStream inp, Structure defaults) throws IOException, SmioException, SmioError {
        clearXml(obj);
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(defaults);
        SmioField sf = def.getFieldModel().getParser();
        IDataSource ids = new DataSourceInputStream(inp);
        try {
            sf.parse(obj, ids);
        } catch (IOException ex) {
            Logger.getLogger(XmlObjectMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static public void readStructuredMessageParamWithClear(XmlObject obj, byte[] inp, Structure defaults) throws SmioException, SmioError {
        clearXml(obj);
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(defaults);
        SmioField sf = def.getFieldModel().getParser();
        IDataSource ids = new DataSourceByteBuffer(inp);
        try {
            sf.parse(obj, ids);
        } catch (IOException ex) {
            Logger.getLogger(XmlObjectMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static public void writeStructuredMessageToOutputParam(XmlObject obj, OutputStream out, Structure defaults) throws IOException, SmioError, SmioException {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(defaults);
        SmioField sf = def.getFieldModel().getParser();
        ByteBuffer res = sf.merge(obj);
        out.write(ParseUtil.extractByteBufferContent(res));
    }
    
    static public byte[] writeStructuredMessageParam(XmlObject obj, Structure defaults) throws SmioError, SmioException {
        RootMsdlScheme def = getRootMsdlScheme(obj);
        def.setFictiveParentStructure(defaults);
        SmioField sf = def.getFieldModel().getParser();
        ByteBuffer res = sf.merge(obj);
        return ParseUtil.extractByteBufferContent(res);
    }
    
    static public void writeStructuredMessageSeparator(XmlObject obj, OutputStream out) throws IOException, SmioError {
        out.write(messageSeparator.getBytes(FileUtils.XML_ENCODING));
    }
    
    static public byte[] writeStructuredMessageSeparator(XmlObject obj) throws SmioError {
        try {
            return messageSeparator.getBytes(FileUtils.XML_ENCODING);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(XmlObjectMessagingHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new SmioError("Unable to get separator bytes", ex);
        }
    }
    
    static public String getAnnotation(XmlObject xmlObject) {
        XmlObject me = getMessageInstance(xmlObject);
        if (me == null) {
            addNewMessageInstance(xmlObject);
        }
        XmlObject root = xmlObject.selectPath("*")[0];
        SchemaType st = root.schemaType();
        SchemaAnnotation a = st.getAnnotation();
        XmlObject xo = a.getUserInformation()[0];
        XmlCursor cursor = xo.newCursor();
        String value = cursor.getTextValue();
        cursor.dispose();
        return value;
    }
    
    static public RootMsdlScheme getRootMsdlScheme(XmlObject obj) {
        TemplateSchemeSearcher searcher = new TemplateSchemeSearcher(obj);
        return searcher.getContext();
    }
    
    private static Id getIdFromXmlObject(XmlObject obj) {
        String idstr = getAnnotation(obj);
        if (idstr == null) {
            return null;
        }
        return Id.Factory.loadFrom(idstr);
    }
    
    private static RootMsdlScheme getRootMsdlSchemeById(Id id, ClassLoader cl) {
        final IRadixClassLoader radixLoader = (IRadixClassLoader) cl;
        final IRadixEnvironment environment = radixLoader.getEnvironment();
        return environment.getDefManager().getMsdlScheme(id);
    }
    static private final String messageSeparator = "\n";
    
    static public MsdlField getField(XmlObject obj, String name) {
        return getRootMsdlScheme(obj).getStructureFieldModel().getFields().get(name);
    }
    
    static private XmlObject getMessageInstance(XmlObject obj) {
        Method m = null;
        try {
            m = obj.getClass().getMethod("getMessageInstance", new Class[]{});
        } catch (SecurityException | NoSuchMethodException e) {
            Logger.getLogger(XmlObjectMessagingHandler.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        XmlObject me = null;
        try {
            if (m != null) {
                me = (XmlObject) m.invoke(obj, new Object[]{});
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            Logger.getLogger(XmlObjectMessagingHandler.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return me;
    }
    
    static public void clearXml(XmlObject obj) {
        XmlObject me = getMessageInstance(obj);
        if (me != null) {
            me.set(XmlObject.Factory.newInstance());
        }
    }
    
    static private void addNewMessageInstance(XmlObject obj) {
        Method m = null;
        try {
            m = obj.getClass().getMethod("addNewMessageInstance", new Class[]{});
        } catch (SecurityException | NoSuchMethodException e) {
            Logger.getLogger(XmlObjectMessagingHandler.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        try {
            if (m != null) {
                m.invoke(obj, new Object[]{});
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            Logger.getLogger(XmlObjectMessagingHandler.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }
}

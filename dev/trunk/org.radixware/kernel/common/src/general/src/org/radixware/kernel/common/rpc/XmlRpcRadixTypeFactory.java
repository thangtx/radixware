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

package org.radixware.kernel.common.rpc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import org.apache.ws.commons.util.NamespaceContextImpl;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.parser.AtomicParser;
import org.apache.xmlrpc.parser.ByteArrayParser;
import org.apache.xmlrpc.parser.TypeParser;
import org.apache.xmlrpc.serializer.SerializableSerializer;
import org.apache.xmlrpc.serializer.StringSerializer;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class XmlRpcRadixTypeFactory extends TypeFactoryImpl {
    
    private static String XML_OBJECT_TAG_NAME = "xmlobject";
    
    private static class XmlObjectSerializer extends StringSerializer{

        @Override
        public void write(ContentHandler pHandler, Object pObject) throws SAXException {
            if (pObject instanceof XmlObject){
                this.write(pHandler, "ex:"+XML_OBJECT_TAG_NAME, ((XmlObject)pObject).xmlText());
            }
            else{
                super.write(pHandler, pObject);
            }
        }
    }
    
    private static class XmlObjectParser extends AtomicParser{

        @Override
        protected void setResult(String string) throws SAXException {
            if (string==null){
                setResult((Object)null);
            }
            else{
                try {
                    setResult((Object)XmlObject.Factory.parse(string));
                } catch (XmlException ex) {
                    throw new SAXParseException("Cannot convert to XmlObject", null, ex);
                }                
            }
        }
    }
    
    private static class RadixSerializableParser extends ByteArrayParser {
        
        private final ClassLoader classLoader;
        
        public RadixSerializableParser(final ClassLoader adsClassLoader){
           super();
           classLoader = adsClassLoader;
        }
        
        @Override
	public Object getResult() throws XmlRpcException {
		try {
                    byte[] res = (byte[]) super.getResult();
                    ByteArrayInputStream bais = new ByteArrayInputStream(res);
                    ObjectInputStream ois = new ObjectInputStream(bais){
                        @Override
                        protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
                            return Class.forName(osc.getName(), false, classLoader);
                        }                            
                    };
                    return ois.readObject();
		} catch (IOException e) {
			throw new XmlRpcException("Failed to read result object: " + e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new XmlRpcException("Failed to load class for result object: " + e.getMessage(), e);
		}
	}

    }    

    
    private final XmlObjectSerializer xmlObjSerializer = new XmlObjectSerializer();
    private final XmlObjectParser xmlObjParser = new XmlObjectParser();
    private final RadixSerializableParser serializableParser;
    
    public XmlRpcRadixTypeFactory(final XmlRpcController controller, final ClassLoader adsClassLoader){
        super(controller);    
        serializableParser = new RadixSerializableParser(adsClassLoader);
    }

    @Override
    public TypeParser getParser(final XmlRpcStreamConfig pConfig, final NamespaceContextImpl pContext, final String pURI, String pLocalName) {
        if (XML_OBJECT_TAG_NAME.equals(pLocalName)){
            return xmlObjParser;
        }
        else if (SerializableSerializer.SERIALIZABLE_TAG.equals(pLocalName)){
            return serializableParser;
        }
        return super.getParser(pConfig, pContext, pURI, pLocalName);
    }

    @Override
    public TypeSerializer getSerializer(final XmlRpcStreamConfig pConfig, final Object pObject) throws SAXException {
        if (pObject instanceof XmlObject){
            return xmlObjSerializer;
        }
        return super.getSerializer(pConfig, pObject);
    }
    
}

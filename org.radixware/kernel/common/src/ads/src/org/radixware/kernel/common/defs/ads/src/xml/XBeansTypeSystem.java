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
package org.radixware.kernel.common.defs.ads.src.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.impl.schema.SchemaTypeImpl;
import org.apache.xmlbeans.impl.schema.AdsStscUtils;
import org.radixware.kernel.common.build.xbeans.XbeansSchemaCodePrinter;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.XmlInterfaceType;
import org.radixware.schemas.adsdef.XmlTypeList;

public class XBeansTypeSystem {

    private String interfacePackageName;
    private String implementationPackageName;
    private final List<XBeansInterface> interfaces = new LinkedList<>();
    private final List<XBeansClass> classes = new LinkedList<>();
    private final AbstractXmlDefinition context;

    public XBeansTypeSystem(AbstractXmlDefinition context) {
        this.context = context;
    }

    public XBeansTypeSystem(AbstractXmlDefinition context, XmlTypeList typeList) {
        this.context = context;
        interfacePackageName = typeList.getInterfacePackageName();
        implementationPackageName = typeList.getImplementationPackageName();
        for (XmlInterfaceType xInt : typeList.getInterfaceList()) {
            final XBeansInterface iface = new XBeansInterface(null);
            iface.loadFrom(xInt);
            interfaces.add(iface);
        }
    }

    public XBeansTypeSystem(AbstractXmlDefinition context, SchemaTypeSystem[] sts) {
        this.context = context;
        buildTypesFromTs(sts);
    }

    private void buildTypesFromTs(SchemaTypeSystem[] systems) {
        List<SchemaType> types = new ArrayList();

        for (SchemaTypeSystem system : systems) {
            types.addAll(Arrays.asList(system.globalTypes()));

            types.addAll(Arrays.asList(system.documentTypes()));

            types.addAll(Arrays.asList(system.attributeTypes()));
        }

        final XbeansSchemaCodePrinter printer = new XBeansDefCodePrinter(context, new XBeansDefJavaAcceptor(this));

        final String targetNamespace = context.getTargetNamespace();
        for (SchemaType type : types) {

            if (type.isBuiltinType()) {
                continue;
            }

            final String typeNs = AdsStscUtils.getTypeNamespace(type);

            if (typeNs == null) {
                continue;
            }

            if (!Utils.equals(typeNs, targetNamespace)) {
                continue;
            }

            final String fjn = type.getFullJavaName();
            if (fjn == null) {
                continue;
            }
            try {
                printer.printType(new NullWriter(), type);
                printer.printTypeImpl(new NullWriter(), type);
            } catch (IOException exception) {
                Logger.getLogger(XBeansTypeSystem.class.getName()).log(Level.FINE, exception.getMessage(), exception);
            }
        }
    }

    private static class NullWriter extends Writer {

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }

    public AbstractXmlDefinition getContext() {
        return context;
    }

    public Collection<String> getTypeList() {
        ArrayList<String> list = new ArrayList<>();
        for (XBeansInterface iface : interfaces) {
            iface.getTypeList(list);
        }
        return list;
    }

    public void addInterface(XBeansInterface iface) {
        interfaces.add(iface);
    }

    public void addInterface(XBeansClass clazz) {
        classes.add(clazz);
    }

    public String getImplementationPackageName() {
        return implementationPackageName;
    }

    public void setImplementationPackageName(String implementationPackageName) {
        this.implementationPackageName = implementationPackageName;
    }

    public String getInterfacePackageName() {
        return interfacePackageName;
    }

    public void setInterfacePackageName(String interfacePackageName) {
        this.interfacePackageName = interfacePackageName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(interfacePackageName).append(";\n\n");
        for (XBeansInterface iface : interfaces) {
            builder.append(iface.toString());
            builder.append("\n\n");
        }
        builder.append("\n\npackage ").append(interfacePackageName).append(";\n\n");
        for (XBeansClass clazz : classes) {
            builder.append(clazz.toString());
            builder.append("\n\n");
        }
        return builder.toString();
    }

    public XBeansType findTypeOrInterface(String name) {
        return findTypeOrInterface(name, null);
    }

    public XBeansType findTypeOrInterface(String name, List<String> tracePath) {
        String names[] = name.split("\\.");
        if (names.length == 0) {
            return null;
        }

        int loolFrom = 0;
        loop:
        while (true) {
            if (tracePath != null) {
                tracePath.clear();
            }
            for (XBeansInterface iface : interfaces) {
                if (iface.getName().equals(names[loolFrom])) {
                    if (tracePath != null) {
                        tracePath.add(iface.getName());
                    }
                    XBeansType res = iface;
                    for (int i = loolFrom + 1; i < names.length; i++) {
                        res = res.findInnerType(names[i]);
                        if (res == null) {
                            //dept search fails. it could be an msdl top level type
                            if (loolFrom == 0 && context.getDefinitionType() == EDefType.MSDL_SCHEME && "MessageType".equals(names[0])) {
                                loolFrom = 1;
                                continue loop;
                            }
                            if (tracePath != null) {
                                tracePath.clear();
                            }
                            return null;
                        } else {
                            if (tracePath != null) {
                                tracePath.add(names[i]);
                            }
                        }
                    }
                    return res;
                }
            }
            if (tracePath != null) {
                tracePath.clear();
            }
            return null;
        }
    }

    public XBeansInterface findInterface(String name) {
        for (XBeansInterface iface : interfaces) {
            if (iface.getName().equals(name)) {
                return iface;
            }
        }
        return null;
    }

    public List<XBeansInterface> getInterfaceList() {
        return Collections.unmodifiableList(interfaces);
    }

    public XBeansClass findClass(String name) {
        for (XBeansClass iface : classes) {
            if (iface.getName().equals(name)) {
                return iface;
            }
        }
        return null;
    }

    public List<XBeansClass> getClassList() {
        return Collections.unmodifiableList(classes);
    }

    public void appendTo(XmlTypeList xDef) {
        xDef.setInterfacePackageName(interfacePackageName);
        xDef.setImplementationPackageName(implementationPackageName);
        for (XBeansInterface iface : interfaces) {
            iface.appendTo(xDef.addNewInterface());
        }
    }

    public XBeansInterface findType(String name) {
        for (XBeansInterface iface : interfaces) {
            if (iface.getName().equals(name)) {
                return iface;
            }
        }
        return null;
    }
}

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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.InterfaceExtension.MethodSignature;


public class XBeansClass {

    public static class ExtensionMethod {

        private final String handler;
        private final String methodName;
        private final String[] excTypes;
        private final String[] argTypes;
        private final String returnType;

        public ExtensionMethod(String handler, MethodSignature sign) {
            this.handler = handler;
            this.methodName = sign.getName();
            this.excTypes = sign.getExceptionTypes();
            this.returnType = sign.getReturnType();
            this.argTypes = sign.getParameterTypes();
        }
    }
    private List<XBeansClass> inners = new LinkedList<XBeansClass>();
    private List<XBeansClassProp> props = new LinkedList<XBeansClassProp>();
    private List<ExtensionMethod> extensionMethods = new LinkedList<ExtensionMethod>();
    private final String name;
    private final boolean isInner;
    private String baseClass;
    private String[] interfaces;
    private boolean hasConstructor;

    public XBeansClass(String shortName, boolean isInner) {
        this.name = shortName;
        this.isInner = isInner;
    }

    public List<ExtensionMethod> getExtensionMethods() {
        return Collections.unmodifiableList(extensionMethods);
    }

    public List<XBeansClass> getInnerClasses() {
        return Collections.unmodifiableList(inners);
    }

    public List<XBeansClassProp> getProperties() {
        return Collections.unmodifiableList(props);
    }

    public void addInnerClass(XBeansClass clazz) {
        this.inners.add(clazz);
    }

    public void addProperty(XBeansClassProp prop) {
        this.props.add(prop);
    }

    public String getName() {
        return name;
    }

    public String getBaseClass() {
        return baseClass;
    }

    void setBaseClass(String baseClass) {
        this.baseClass = baseClass;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }

    public boolean hasConstructor() {
        return hasConstructor;
    }

    public void setHasConstructor(boolean hasConstructor) {
        this.hasConstructor = hasConstructor;
    }

    public void addExtensionMethod(ExtensionMethod method) {
        this.extensionMethods.add(method);
    }
}

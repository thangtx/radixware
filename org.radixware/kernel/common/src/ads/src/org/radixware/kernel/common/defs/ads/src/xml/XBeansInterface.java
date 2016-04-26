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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.XmlInterfaceType;


public class XBeansInterface implements XBeansType {

    public static abstract class Content {

        protected final XBeansInterface iface;

        public Content(XBeansInterface iface) {
            this.iface = iface;
        }

        public abstract void appendTo(XmlInterfaceType xDef);

        public abstract void loadFrom(XmlInterfaceType xDef);
    }

    public static class SimpleContent extends Content implements XBeansType {

        public static class EnumField {

            private final String name;
            private final String value;
            private XBeansInterface iface;

            public EnumField(String name, String value) {
                this.name = name;
                this.value = value == null ? null : value.trim();
            }

            public String getName() {
                return name;
            }

            public String getQName() {
                return iface.getName() + "." + getName();
            }

            public String getValue() {
                return value;
            }
        }

        public static class EnumerationElement {

            private final String name;
            private final String value;
            private final int intValue;
            private XBeansInterface iface;

            public EnumerationElement(String name, String value, int intValue) {
                this.name = name;
                this.value = value == null ? null : value.trim();
                this.intValue = intValue;
            }

            public int getIntValue() {
                return intValue;
            }

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }

            public String getQName() {
                return iface.getName() + "." + getName();
            }
        }
        private String enumClass;
        private boolean tableDefined;
        private List<EnumField> enumFields = new LinkedList<EnumField>();
        private List<EnumerationElement> elements = new LinkedList<EnumerationElement>();
        private List<String> indexFields = new LinkedList<String>();

        public SimpleContent(XBeansInterface iface, String enumClass) {
            super(iface);
            this.enumClass = enumClass;
        }

        private SimpleContent(XBeansInterface iface) {
            super(iface);
        }

        @Override
        public void appendTo(XmlInterfaceType xDef) {
            XmlInterfaceType.Enumeration xEnum = xDef.addNewEnumeration();
            xEnum.setEnumClassName(enumClass);
            if (tableDefined) {
                xEnum.setTableDefined(true);

            }
            for (String index : indexFields) {
                xEnum.addIndexField(index);
            }
            for (EnumField f : enumFields) {
                XmlInterfaceType.Enumeration.EnumField xF = xEnum.addNewEnumField();
                xF.setName(f.name);
                xF.setValue(f.value);
            }
            for (EnumerationElement e : elements) {
                XmlInterfaceType.Enumeration.EnumElement xE = xEnum.addNewEnumElement();
                xE.setName(e.name);
                xE.setValue(e.value);
                xE.setIntValue1(e.intValue);
            }
        }

        @Override
        public void loadFrom(XmlInterfaceType xDef) {
            XmlInterfaceType.Enumeration xEnum = xDef.getEnumeration();
            enumClass = xEnum.getEnumClassName();
            tableDefined = xEnum.getTableDefined();

            for (String index : xEnum.getIndexFieldList()) {
                indexFields.add(index);
            }
            for (XmlInterfaceType.Enumeration.EnumField xF : xEnum.getEnumFieldList()) {
                enumFields.add(new EnumField(xF.getName(), xF.getValue()));
            }
            for (XmlInterfaceType.Enumeration.EnumElement xE : xEnum.getEnumElementList()) {
                elements.add(new EnumerationElement(xE.getName(), xE.getValue(), xE.getIntValue1()));
            }
        }

        public boolean isTableDefined() {
            return tableDefined;
        }

        public void setTableDefined(boolean tableDefined) {
            this.tableDefined = tableDefined;
        }

        @Override
        public boolean isDocumentType() {
            return false;
        }

        @Override
        public XBeansType findInnerType(String name) {
            return null;
        }

        public void addEnumField(EnumField constant) {
            enumFields.add(constant);
            constant.iface = iface;
        }

        public void addIndexField(String name) {
            indexFields.add(name);
        }

        public void addElement(EnumerationElement e) {
            elements.add(e);
            e.iface = iface;
        }

        public List<EnumField> getFieldList() {
            return Collections.unmodifiableList(enumFields);
        }

        public EnumField findFiledByName(String name) {
            for (EnumField e : enumFields) {
                if (e.name.equals(name)) {
                    return e;
                }
            }
            return null;
        }

        public boolean isIndexExists(String name) {
            return indexFields.contains(name);
        }

        public List<String> getIndexList() {
            return Collections.unmodifiableList(indexFields);
        }

        public List<EnumerationElement> getElementList() {
            return Collections.unmodifiableList(elements);
        }

        public EnumerationElement findElementByName(String name) {
            for (EnumerationElement e : elements) {
                if (e.name.equals(name)) {
                    return e;
                }
            }
            return null;
        }

        public String getEnumClass() {
            return enumClass;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("  enumeration ").
                    append(enumClass).
                    append("\n  {\n  fields:\n");

            for (EnumField f : enumFields) {
                builder.append("  ").append(f.name).append(" = ").append(f.value).append(";\n");
            }
            builder.append("  indices:\n");
            for (String i : indexFields) {
                builder.append("  ").append(i).append(";\n");
            }
            builder.append("  elements:\n");
            for (EnumerationElement e : elements) {
                builder.append("  ").append(e.name).append(" = ").append(e.value).append(", index = ").append(e.intValue).append(";\n");
            }
            builder.append("\n  }\n");
            return builder.toString();
        }
    }

    public static class ComplexContent extends Content {

        private final List<XBeansIfaceProp> props = new LinkedList<XBeansIfaceProp>();

        public ComplexContent(XBeansInterface iface) {
            super(iface);
        }

        public void addProperty(XBeansIfaceProp prop) {
            props.add(prop);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (XBeansIfaceProp prop : props) {
                builder.append("  ").append(prop.toString()).append("\n");
            }
            return builder.toString();
        }

        public List<XBeansIfaceProp> getPropertyList() {
            return Collections.unmodifiableList(props);
        }

        public XBeansIfaceProp findProperty(String name) {
            for (XBeansIfaceProp p : props) {
                if (p.getName().equals(name)) {
                    return p;
                }
            }
            return null;
        }

        @Override
        public void appendTo(XmlInterfaceType xDef) {
            XmlInterfaceType.Properties xProps = xDef.addNewProperties();
            for (XBeansIfaceProp prop : props) {
                prop.appendTo(xProps.addNewProperty());
            }
        }

        @Override
        public void loadFrom(XmlInterfaceType xDef) {
            XmlInterfaceType.Properties xProps = xDef.getProperties();
            for (XmlInterfaceType.Properties.Property xProp : xProps.getPropertyList()) {
                XBeansIfaceProp prop = new XBeansIfaceProp(iface);
                prop.loadFrom(xProp);
                props.add(prop);
            }
        }
    }

    public static class FactoryInfo implements XBeansType {

        private final boolean isFullFactory;
        private final String factoryName;
        private final boolean isAbstractTypeFactory;
        private final boolean isSimpleTypeFactory;

        private static FactoryInfo loadFrom(XmlInterfaceType.Factory1 xDef) {
            if (xDef == null) {
                return null;
            }
            return new FactoryInfo(xDef.isSetIsFull() ? xDef.getIsFull() : false, xDef.getName(), xDef.isSetIsAbstractTypeFactory() ? xDef.getIsAbstractTypeFactory() : false, xDef.isSetIsSimpleTypeFactory() ? xDef.getIsSimpleTypeFactory() : false);
        }

        public FactoryInfo(boolean isFullFactory, String factoryName, boolean isAbstractTypeFactory, boolean isSimpleTypeFactory) {
            this.isFullFactory = isFullFactory;
            this.factoryName = factoryName;
            this.isAbstractTypeFactory = isAbstractTypeFactory;
            this.isSimpleTypeFactory = isSimpleTypeFactory;
        }

        public String getFactoryName() {
            return factoryName;
        }

        public boolean isAbstractTypeFactory() {
            return isAbstractTypeFactory;
        }

        public boolean isFullFactory() {
            return isFullFactory;
        }

        public boolean isSimpleTypeFactory() {
            return isSimpleTypeFactory;
        }

        public void appendTo(XmlInterfaceType.Factory1 xDef) {
            xDef.setName(factoryName);
            if (isFullFactory) {
                xDef.setIsFull(true);
            }
            if (isAbstractTypeFactory) {
                xDef.setIsAbstractTypeFactory(true);
            }
            if (isSimpleTypeFactory) {
                xDef.setIsSimpleTypeFactory(true);
            }
        }

        @Override
        public XBeansType findInnerType(String name) {
            return null;
        }

        @Override
        public boolean isDocumentType() {
            return false;
        }
    }
    private String name;
    private String baseInterface;
    private String[] extInterfaces;
    private boolean isDeprecated;
    private Content content = null;
    private final List<XBeansInterface> inners = new LinkedList<XBeansInterface>();
    private boolean hasDecimalAccessor = false;
    private int decimalAccType = -1;
    private boolean hasObjectAccessor;
    private String listAccessorWildcard = null;
    private XBeansInterface ownerInterface;
    private FactoryInfo factoryInfo = null;
    private boolean isDocType;
    private XBeansTypeSystem typeSystem;

    public XBeansInterface(XBeansTypeSystem typeSystem, XBeansInterface ownerInterface, String name, String baseInterface, String[] extInterfaces, boolean isDeprecated, boolean isDocType) {
        this.name = name;
        this.baseInterface = baseInterface;
        this.extInterfaces = extInterfaces;
        this.isDeprecated = isDeprecated;
        this.ownerInterface = ownerInterface;
        this.isDocType = isDocType;
        this.typeSystem = typeSystem;
    }

    XBeansInterface(XBeansInterface ownerIface) {
        this.ownerInterface = ownerIface;
    }

    public Content getContent() {
        return content;
    }

    public XBeansTypeSystem getOwnerTypeSystem() {
        if (typeSystem == null) {
            if (ownerInterface == null) {
                return null;
            } else {
                return ownerInterface.getOwnerTypeSystem();
            }
        } else {
            return typeSystem;
        }
    }

    public XBeansInterface findBaseInterface() {
        if (baseInterface == null) {
            return null;
        } else {
            int schemeIdIndex = baseInterface.lastIndexOf("." + EDefinitionIdPrefix.XML_SCHEME.getValue());
            if (schemeIdIndex < 0) {
                schemeIdIndex = baseInterface.lastIndexOf("." + EDefinitionIdPrefix.MSDL_SCHEME.getValue());
            }
            if (schemeIdIndex < 0) {
                return null;
            }

            int colonIndex = baseInterface.indexOf(".", schemeIdIndex + 1);

            if (colonIndex < 0) {
                return null;
            }
            String schemeName = baseInterface.substring(0, colonIndex);
            int dotIndex = schemeName.lastIndexOf(".");
            if (dotIndex < 0) {
                return null;
            }
            String schemeIdCandidate = schemeName.substring(dotIndex + 1);
            XBeansTypeSystem ts = getOwnerTypeSystem();
            if (ts == null) {
                return null;
            }
            AbstractXmlDefinition def = ts.getContext();
            if (def == null) {
                return null;
            }
            Id schemeId = Id.Factory.loadFrom(schemeIdCandidate);
            if (schemeId != def.getId()) {
                DefinitionSearcher<AdsDefinition> searcher = AdsSearcher.Factory.newAdsDefinitionSearcher(def);
                AdsDefinition target = searcher.findById(schemeId).get();
                if (target instanceof AbstractXmlDefinition) {
                    ts = ((AbstractXmlDefinition) target).getSchemaTypeSystem();
                    if (ts == null) {
                        return null;
                    }
                }
            }
            String ifaceName = baseInterface.substring(colonIndex + 1);
            return ts.findInterface(ifaceName);
        }

    }

    @Override
    public boolean isDocumentType() {
        return isDocType;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public void addInnerInterface(XBeansInterface iface) {
        inners.add(iface);
    }

    public List<XBeansInterface> getInnerInterfaces() {
        return Collections.unmodifiableList(inners);
    }

    public XBeansInterface findInnerInterface(String name) {

        for (XBeansInterface iface : inners) {
            if (iface.getName().equals(name)) {
                return iface;
            }
        }
        return null;
    }

    @Override
    public XBeansType findInnerType(String name) {
        if ("Factory".equals(name)) {
            return factoryInfo;
        }
        if ("Enum".equals(name)) {
            if (content instanceof SimpleContent) {
                return (SimpleContent) content;
            }
        }
        for (XBeansInterface iface : inners) {
            if (iface.getName().equals(name)) {
                return iface;
            }
        }
        XBeansInterface base = findBaseInterface();
        if (base != null) {
            return base.findInnerType(name);
        }
        return null;
    }

    public int getDecimalAccType() {
        return decimalAccType;
    }

    public void setDecimalAccType(int decimalAccType) {
        this.decimalAccType = decimalAccType;
        this.hasDecimalAccessor = true;
    }

    public boolean hasDecimalAccessor() {
        return hasDecimalAccessor;
    }

    public boolean hasListAccessor() {
        return listAccessorWildcard != null;
    }

    public void setListAccessorWildcard(String wildcard) {
        this.listAccessorWildcard = wildcard;
    }

    public boolean hasObjectAccessor() {
        return hasObjectAccessor;
    }

    public void setHasObjectAccessor(boolean hasObjectAccessor) {
        this.hasObjectAccessor = hasObjectAccessor;
    }

    public FactoryInfo getFactoryInfo() {
        return factoryInfo;
    }

    public void setFactoryInfo(FactoryInfo factoryInfo) {
        this.factoryInfo = factoryInfo;
    }

    public String getBaseInterface() {
        return baseInterface;
    }

    public String[] getExtInterfaces() {
        return extInterfaces;
    }

    public String getName() {
        return name;
    }

    public String getQName() {
        if (ownerInterface == null) {
            return name;
        } else {
            return ownerInterface.getQName() + "." + name;
        }
    }

    public XBeansInterface getOwnerInterface() {
        return ownerInterface;
    }

    public boolean isIsDeprecated() {
        return isDeprecated;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("interface ").
                append(name).
                append(" extends ").append(baseInterface);
        for (String ext : extInterfaces) {
            builder.append(", ");
            builder.append(ext);
        }
        builder.append("\n{\n");
        if (content != null) {
            builder.append(content.toString());
        }
        builder.append("}");
        return builder.toString();
    }

    public void appendTo(XmlInterfaceType xDef) {
        xDef.setName(name);
        xDef.setBaseInterface(baseInterface);
        if (extInterfaces != null && extInterfaces.length > 0) {
            xDef.setOtherInterfaces(Arrays.asList(extInterfaces));
        }
        if (isDocType) {
            xDef.setIsDocument(true);
        }
        if (hasDecimalAccessor) {
            xDef.setDecimalAccType(decimalAccType);
        }
        if (hasObjectAccessor) {
            xDef.setHasObjectAccessor(true);
        }
        if (listAccessorWildcard != null) {
            xDef.setListAccessorWildcard(listAccessorWildcard);
        }
        if (isDeprecated) {
            xDef.setIsDeprecated(true);
        }
        if (factoryInfo != null) {
            factoryInfo.appendTo(xDef.addNewFactory());
        }
        Content c = getContent();
        if (c != null) {
            c.appendTo(xDef);
        }
        for (XBeansInterface inner : inners) {
            inner.appendTo(xDef.addNewInterface());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFrom(XmlInterfaceType xDef) {
        name = xDef.getName();
        baseInterface = xDef.getBaseInterface();
        if (xDef.isSetIsDocument()) {
            isDocType = xDef.getIsDocument();
        }
        if (xDef.isSetOtherInterfaces()) {
            extInterfaces = (String[]) xDef.getOtherInterfaces().toArray(new String[xDef.getOtherInterfaces().size()]);
        }
        if (xDef.isSetDecimalAccType()) {
            hasDecimalAccessor = true;
            decimalAccType = xDef.getDecimalAccType();
        }

        hasObjectAccessor = xDef.getHasObjectAccessor();
        if (xDef.isSetListAccessorWildcard()) {
            listAccessorWildcard = xDef.getListAccessorWildcard();
        }

        isDeprecated = xDef.getIsDeprecated();

        factoryInfo = FactoryInfo.loadFrom(xDef.getFactory());

        if (xDef.isSetProperties()) {
            content = new ComplexContent(this);
        } else if (xDef.isSetEnumeration()) {
            content = new SimpleContent(this);
        }
        if (content != null) {
            content.loadFrom(xDef);
        }
        for (XmlInterfaceType xInt : xDef.getInterfaceList()) {
            XBeansInterface iface = new XBeansInterface(this);
            iface.loadFrom(xInt);
            inners.add(iface);
        }
    }

    void getTypeList(Collection<String> collection) {
        getTypeList(collection, null);
    }

    private void getTypeList(Collection<String> collection, String prefix) {
        String newPrefix = prefix == null ? getName() : prefix + "." + getName();
        collection.add(newPrefix);
        if (factoryInfo != null) {
            collection.add(newPrefix + ".Factory");
        }
        for (XBeansInterface inner : inners) {
            inner.getTypeList(collection, newPrefix);
        }
    }
}

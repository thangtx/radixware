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

import org.radixware.schemas.adsdef.XmlInterfaceType;
import org.radixware.schemas.adsdef.XmlTypeList;


public class XBeansIfaceProp {

    private String name;
    private boolean isDeprecated;
    private boolean isAttr;
    private String type;
    private String xtype;
    private boolean hasSingletonSetter;
    private boolean hasSingletonGetter;
    private boolean hasSingletonSetterXml;
    private boolean hasSingletonGetterXml;
    private boolean hasSingletonNullCheck;
    private boolean hasSingletonCreator;
    private boolean hasSingletonUnset;
    private boolean hasOptionalUnset;
    private boolean hasOptionalExistanceCheck;
    private boolean hasSeveralPropListGetter;
    private boolean hasSeveralPropArrayGetter;
    private boolean hasSeveralPropArraySetter;
    private boolean hasSeveralPropArrayElementGetter;
    private boolean hasSeveralPropArrayElementSetter;
    private boolean hasSeveralPropArrayElementSetNull;
    private boolean hasSeveralPropListXmlGetter;
    private String severalPropListGetterWrappedType;
    private boolean hasSeveralPropArrayXmlGetter;
    private boolean hasSeveralPropArrayXmlSetter;
    private boolean hasSeveralPropArrayElementXmlGetter;
    private boolean hasSeveralPropArrayElementXmlSetter;
    private boolean hasSeveralPropArrayElementInsertion;
    private boolean hasSeveralPropArrayElementAddition;
    private boolean hasSeveralPropArrayNewElementInsertion;
    private boolean hasSeveralPropArrayNewElementAddition;
    private boolean hasSeveralPropArrayElementRemoving;
    private boolean hasSeveralPropNullCheck;
    private boolean hasSeveralPropSizeAccess;
    private boolean hasSeveralPropListAssignment;
    private XBeansInterface iface;

    public XBeansIfaceProp(XBeansInterface iface, String name, boolean isDeprecated, boolean isAttr, String type, String xtype) {
        this(iface);
        this.name = name;
        this.isDeprecated = isDeprecated;
        this.isAttr = isAttr;
        this.type = type;
        this.xtype = xtype;
    }

    XBeansIfaceProp(XBeansInterface iface) {
        this.iface = iface;
    }

    public void appendTo(XmlInterfaceType.Properties.Property xDef) {
        xDef.setName(name);
        if (isDeprecated) {
            xDef.setIsDeprecated(isDeprecated);
        }
        if (isAttr) {
            xDef.setIsAttr(isAttr);
        }
        xDef.setType(type);
        xDef.setXmlType(xtype);
        if (hasSingletonSetter) {
            xDef.setSinSet(true);
        }
        if (hasSingletonGetter) {
            xDef.setSinGet(true);
        }
        if (hasSingletonSetterXml) {
            xDef.setSinSetXml(true);
        }
        if (hasSingletonGetterXml) {
            xDef.setSinGetXml(true);
        }
        if (hasSingletonNullCheck) {
            xDef.setSinNullCheck(true);
        }
        if (hasSingletonCreator) {
            xDef.setSinNew(true);
        }
        if (hasSingletonUnset) {
            xDef.setSinUnset(true);
        }
        if (hasOptionalUnset) {
            xDef.setOptUnset(true);
        }
        if (hasOptionalExistanceCheck) {
            xDef.setOptExistCheck(true);
        }
        if (hasSeveralPropListGetter) {
            xDef.setSevPropListGet(true);
            if (severalPropListGetterWrappedType != null) {
                xDef.setSevPropListGetType(severalPropListGetterWrappedType);
            }
        }

        if (hasSeveralPropListAssignment) {
            xDef.setSevPropListAssing(true);
        }
        if (hasSeveralPropArrayGetter) {
            xDef.setSevPropArrGet(true);
        }
        if (hasSeveralPropArraySetter) {
            xDef.setSevPropArrSet(true);
        }
        if (hasSeveralPropArrayElementGetter) {
            xDef.setSevPropArrItemGet(true);
        }
        if (hasSeveralPropArrayElementSetter) {
            xDef.setSevPropArrItemSet(true);
        }
        if (hasSeveralPropArrayElementSetNull) {
            xDef.setSevPropArrItemSetNull(true);
        }
        if (hasSeveralPropListXmlGetter) {
            xDef.setSevPropListXmlGet(true);
        }
        if (hasSeveralPropArrayXmlGetter) {
            xDef.setSevPropArrXmlGet(true);
        }
        if (hasSeveralPropArrayXmlSetter) {
            xDef.setSevPropArrXmlSet(true);
        }
        if (hasSeveralPropArrayElementXmlGetter) {
            xDef.setSevPropArrItemXmlGet(true);
        }
        if (hasSeveralPropArrayElementXmlSetter) {
            xDef.setSevPropArrItemXmlSet(true);
        }
        if (hasSeveralPropArrayElementInsertion) {
            xDef.setSevPropArrItemIns(true);
        }
        if (hasSeveralPropArrayElementAddition) {
            xDef.setSevPropArrItemAdd(true);
        }
        if (hasSeveralPropArrayNewElementInsertion) {
            xDef.setSevPropArrNewItemIns(true);
        }
        if (hasSeveralPropArrayNewElementAddition) {
            xDef.setSevPropArrNewItemAdd(true);
        }
        if (hasSeveralPropArrayElementRemoving) {
            xDef.setSevPropArrItemDel(true);
        }
        if (hasSeveralPropNullCheck) {
            xDef.setSevPropNullCheck(true);
        }
        if (hasSeveralPropSizeAccess) {
            xDef.setSevPropSize(true);
        }
    }

    public void loadFrom(XmlInterfaceType.Properties.Property xDef) {
        name = xDef.getName();
        type = xDef.getType();
        xtype = xDef.getXmlType();
        isDeprecated = xDef.getIsDeprecated();
        isAttr = xDef.getIsAttr();

        hasSingletonSetter = xDef.getSinSet();
        hasSingletonGetter = xDef.getSinGet();
        hasSingletonSetterXml =
                xDef.getSinSetXml();
        hasSingletonGetterXml =
                xDef.getSinGetXml();
        hasSingletonNullCheck =
                xDef.getSinNullCheck();
        hasSingletonCreator =
                xDef.getSinNew();
        hasSingletonUnset =
                xDef.getSinUnset();
        hasOptionalUnset =
                xDef.getOptUnset();
        hasOptionalExistanceCheck =
                xDef.getOptExistCheck();
        hasSeveralPropListGetter =
                xDef.getSevPropListGet();
        hasSeveralPropArrayGetter =
                xDef.getSevPropArrGet();
        hasSeveralPropArraySetter =
                xDef.getSevPropArrSet();
        hasSeveralPropArrayElementGetter =
                xDef.getSevPropArrItemGet();
        hasSeveralPropArrayElementSetter =
                xDef.getSevPropArrItemSet();
        hasSeveralPropArrayElementSetNull =
                xDef.getSevPropArrItemSetNull();
        hasSeveralPropListXmlGetter =
                xDef.getSevPropListXmlGet();
        hasSeveralPropArrayXmlGetter =
                xDef.getSevPropArrXmlGet();
        hasSeveralPropArrayXmlSetter =
                xDef.getSevPropArrXmlSet();
        hasSeveralPropArrayElementXmlGetter =
                xDef.getSevPropArrItemXmlGet();
        hasSeveralPropArrayElementXmlSetter =
                xDef.getSevPropArrItemXmlSet();
        hasSeveralPropArrayElementInsertion =
                xDef.getSevPropArrItemIns();
        hasSeveralPropArrayElementAddition =
                xDef.getSevPropArrItemAdd();
        hasSeveralPropArrayNewElementInsertion =
                xDef.getSevPropArrNewItemIns();
        hasSeveralPropArrayNewElementAddition =
                xDef.getSevPropArrNewItemAdd();
        hasSeveralPropArrayElementRemoving =
                xDef.getSevPropArrItemDel();
        hasSeveralPropNullCheck =
                xDef.getSevPropNullCheck();
        hasSeveralPropSizeAccess =
                xDef.getSevPropSize();
        hasSeveralPropListAssignment = xDef.getSevPropListAssing();
        severalPropListGetterWrappedType = xDef.getSevPropListGetType();

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("property ").
                append(type).
                append("(").
                append(xtype).
                append(") ").
                append(name);
        if (isDeprecated) {
            builder.append(" deprecated");
        }
        if (isAttr) {
            builder.append(" attribute");
        }
        if (hasSingletonSetter) {
            builder.append(" singletonWrite");
        }
        if (hasSingletonGetter) {
            builder.append(" singletonRead");
        }
        if (hasSingletonSetterXml) {
            builder.append(" singletonXmlWrite");
        }
        if (hasSingletonGetterXml) {
            builder.append(" singletonXmlRead");
        }
        if (hasSingletonNullCheck) {
            builder.append(" singletonNullCheck");
        }
        if (hasSingletonCreator) {
            builder.append(" singletonCreator");
        }
        if (hasSingletonUnset) {
            builder.append(" singletonUnset");
        }
        if (hasOptionalUnset) {
            builder.append(" optionalUnset");
        }
        if (hasOptionalExistanceCheck) {
            builder.append(" optionalExistanceCheck");
        }
        if (hasSeveralPropListGetter) {
            builder.append(" listRead");
        }
        if (hasSeveralPropListAssignment) {
            builder.append(" listAssign");
        }
        if (hasSeveralPropArrayGetter) {
            builder.append(" arrayRead");
        }
        if (hasSeveralPropArraySetter) {
            builder.append(" arrayWrite");
        }
        if (hasSeveralPropArrayElementGetter) {
            builder.append(" arrayElementRead");
        }
        if (hasSeveralPropArrayElementSetter) {
            builder.append(" arrayElementWrite");
        }
        if (hasSeveralPropArrayElementSetNull) {
            builder.append(" arrayElementReset");
        }
        if (hasSeveralPropListXmlGetter) {
            builder.append(" listXmlRead");
        }
        if (hasSeveralPropArrayXmlGetter) {
            builder.append(" arrayXmlRead");
        }
        if (hasSeveralPropArrayXmlSetter) {
            builder.append(" arrayXmlWrite");
        }
        if (hasSeveralPropArrayElementXmlGetter) {
            builder.append(" arrayElementXmlRead");
        }
        if (hasSeveralPropArrayElementXmlSetter) {
            builder.append(" arrayElementXmlWrite");
        }
        if (hasSeveralPropArrayElementInsertion) {
            builder.append(" arrayElementInsert");
        }
        if (hasSeveralPropArrayElementAddition) {
            builder.append(" arrayElementAdd");
        }
        if (hasSeveralPropArrayNewElementInsertion) {
            builder.append(" newElementInsert");
        }
        if (hasSeveralPropArrayNewElementAddition) {
            builder.append(" newElementAdd");
        }
        if (hasSeveralPropArrayElementRemoving) {
            builder.append(" elementRemove");
        }
        if (hasSeveralPropNullCheck) {
            builder.append(" arrayNullCheck");
        }
        if (hasSeveralPropSizeAccess) {
            builder.append(" sizeAccess");
        }


        return builder.toString();
    }

    public boolean hasSingletonGetter() {
        return hasSingletonGetter;
    }

    public void setHasSingletonGetter(boolean hasSingletonGetter) {
        this.hasSingletonGetter = hasSingletonGetter;
    }

    public boolean hasSingletonSetter() {
        return hasSingletonSetter;
    }

    public void setHasSingletonSetter(boolean hasSingletoneSetter) {
        this.hasSingletonSetter = hasSingletoneSetter;
    }

    public boolean hasSingletonGetterXml() {
        return hasSingletonGetterXml;
    }

    public void setHasSingletonGetterXml(boolean hasSingletonGetter) {
        this.hasSingletonGetterXml = hasSingletonGetter;
    }

    public boolean hasSingletonSetterXml() {
        return hasSingletonSetterXml;
    }

    public void setHasSingletonSetterXml(boolean hasSingletoneSetter) {
        this.hasSingletonSetterXml = hasSingletoneSetter;
    }

    public boolean hasSingletonCreator() {
        return hasSingletonCreator;
    }

    public void setHasSingletonCreator(boolean hasSingletonCreator) {
        this.hasSingletonCreator = hasSingletonCreator;
    }

    public boolean hasSingletonNullCheck() {
        return hasSingletonNullCheck;
    }

    public void setHasSingletonNullCheck(boolean hasSingletonNullCheck) {
        this.hasSingletonNullCheck = hasSingletonNullCheck;
    }

    public boolean hasSingletonUnset() {
        return hasSingletonUnset;
    }

    public void setHasSingletonUnset(boolean hasSingletonUnset) {
        this.hasSingletonUnset = hasSingletonUnset;
    }

    public boolean hasOptionalExistanceCheck() {
        return hasOptionalExistanceCheck;
    }

    public void setHasOptionalExistanceCheck(boolean hasOptionalExistanceCheck) {
        this.hasOptionalExistanceCheck = hasOptionalExistanceCheck;
    }

    public boolean hasOptionalUnset() {
        return hasOptionalUnset;
    }

    public void setHasOptionalUnset(boolean hasOptionalUnset) {
        this.hasOptionalUnset = hasOptionalUnset;
    }

    public boolean hasSeveralPropArrayElementAddition() {
        return hasSeveralPropArrayElementAddition;
    }

    public void setHasSeveralPropArrayElementAddition(boolean hasSeveralPropArrayElementAddition) {
        this.hasSeveralPropArrayElementAddition = hasSeveralPropArrayElementAddition;
    }

    public boolean hasSeveralPropArrayElementGetter() {
        return hasSeveralPropArrayElementGetter;
    }

    public void setHasSeveralPropArrayElementGetter(boolean hasSeveralPropArrayElementGetter) {
        this.hasSeveralPropArrayElementGetter = hasSeveralPropArrayElementGetter;
    }

    public boolean hasSeveralPropArrayElementInsertion() {
        return hasSeveralPropArrayElementInsertion;
    }

    public void setHasSeveralPropArrayElementInsertion(boolean hasSeveralPropArrayElementInsertion) {
        this.hasSeveralPropArrayElementInsertion = hasSeveralPropArrayElementInsertion;
    }

    public boolean hasSeveralPropArrayElementRemoving() {
        return hasSeveralPropArrayElementRemoving;
    }

    public void setHasSeveralPropArrayElementRemoving(boolean hasSeveralPropArrayElementRemoving) {
        this.hasSeveralPropArrayElementRemoving = hasSeveralPropArrayElementRemoving;
    }

    public boolean hasSeveralPropArrayElementSetNull() {
        return hasSeveralPropArrayElementSetNull;
    }

    public void setHasSeveralPropArrayElementSetNull(boolean hasSeveralPropArrayElementSetNull) {
        this.hasSeveralPropArrayElementSetNull = hasSeveralPropArrayElementSetNull;
    }

    public boolean hasSeveralPropArrayElementSetter() {
        return hasSeveralPropArrayElementSetter;
    }

    public void setHasSeveralPropArrayElementSetter(boolean hasSeveralPropArrayElementSetter) {
        this.hasSeveralPropArrayElementSetter = hasSeveralPropArrayElementSetter;
    }

    public boolean hasSeveralPropArrayElementXmlGetter() {
        return hasSeveralPropArrayElementXmlGetter;
    }

    public void setHasSeveralPropArrayElementXmlGetter(boolean hasSeveralPropArrayElementXmlGetter) {
        this.hasSeveralPropArrayElementXmlGetter = hasSeveralPropArrayElementXmlGetter;
    }

    public boolean hasSeveralPropArrayElementXmlSetter() {
        return hasSeveralPropArrayElementXmlSetter;
    }

    public void setHasSeveralPropArrayElementXmlSetter(boolean hasSeveralPropArrayElementXmlSetter) {
        this.hasSeveralPropArrayElementXmlSetter = hasSeveralPropArrayElementXmlSetter;
    }

    public boolean hasSeveralPropArrayGetter() {
        return hasSeveralPropArrayGetter;
    }

    public void setHasSeveralPropArrayGetter(boolean hasSeveralPropArrayGetter) {
        this.hasSeveralPropArrayGetter = hasSeveralPropArrayGetter;
    }

    public boolean hasSeveralPropArrayNewElementAddition() {
        return hasSeveralPropArrayNewElementAddition;
    }

    public void setHasSeveralPropArrayNewElementAddition(boolean hasSeveralPropArrayNewElementAddition) {
        this.hasSeveralPropArrayNewElementAddition = hasSeveralPropArrayNewElementAddition;
    }

    public boolean hasSeveralPropArrayNewElementInsertion() {
        return hasSeveralPropArrayNewElementInsertion;
    }

    public void setHasSeveralPropArrayNewElementInsertion(boolean hasSeveralPropArrayNewElementInsertion) {
        this.hasSeveralPropArrayNewElementInsertion = hasSeveralPropArrayNewElementInsertion;
    }

    public boolean hasSeveralPropArraySetter() {
        return hasSeveralPropArraySetter;
    }

    public void setHasSeveralPropArraySetter(boolean hasSeveralPropArraySetter) {
        this.hasSeveralPropArraySetter = hasSeveralPropArraySetter;
    }

    public boolean hasSeveralPropArrayXmlGetter() {
        return hasSeveralPropArrayXmlGetter;
    }

    public void setHasSeveralPropArrayXmlGetter(boolean hasSeveralPropArrayXmlGetter) {
        this.hasSeveralPropArrayXmlGetter = hasSeveralPropArrayXmlGetter;
    }

    public boolean hasSeveralPropArrayXmlSetter() {
        return hasSeveralPropArrayXmlSetter;
    }

    public void setHasSeveralPropArrayXmlSetter(boolean hasSeveralPropArrayXmlSetter) {
        this.hasSeveralPropArrayXmlSetter = hasSeveralPropArrayXmlSetter;
    }

    public boolean hasSeveralPropListXmlGetter() {
        return hasSeveralPropListXmlGetter;
    }

    public void setHasSeveralPropListXmlGetter(boolean hasSeveralPropListXmlGetter) {
        this.hasSeveralPropListXmlGetter = hasSeveralPropListXmlGetter;
    }

    public boolean hasSeveralPropNullCheck() {
        return hasSeveralPropNullCheck;
    }

    public void setHasSeveralPropNullCheck(boolean hasSeveralPropNullCheck) {
        this.hasSeveralPropNullCheck = hasSeveralPropNullCheck;
    }

    public boolean hasSeveralPropSizeAccess() {
        return hasSeveralPropSizeAccess;
    }

    public void setHasSeveralPropSizeAccess(boolean hasSeveralPropSizeAccess) {
        this.hasSeveralPropSizeAccess = hasSeveralPropSizeAccess;
    }

    public boolean hasSeveralPropListGetter() {
        return hasSeveralPropListGetter;
    }

    public void setHasSeveralPropListGetter(boolean hasSeveralPropListGetter, String wrappedType) {
        this.hasSeveralPropListGetter = hasSeveralPropListGetter;
        this.severalPropListGetterWrappedType = wrappedType;
    }

    public String getSeveralPropListGetterWrappedType() {
        return severalPropListGetterWrappedType;
    }

    public boolean hasSeveralPropListAssignment() {
        return hasSeveralPropListAssignment;
    }

    public void setHasSeveralPropListAssignment(boolean hasSeveralPropListAssignment) {
        this.hasSeveralPropListAssignment = hasSeveralPropListAssignment;
    }

    public boolean isAttr() {
        return isAttr;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public String getName() {
        return name;
    }

    public String getQName() {
        return iface.getQName() + "." + name;
    }

    public String getType() {
        return type;
    }

    public String getXType() {
        return xtype;
    }
}

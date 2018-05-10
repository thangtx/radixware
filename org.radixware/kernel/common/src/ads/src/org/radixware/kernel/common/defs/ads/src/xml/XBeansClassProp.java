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


public class XBeansClassProp {

    private final String name;
    private final String type;
    private final String xtype;
    private final boolean isAttr;
    private boolean hasSingletonPropGetter;
    private boolean hasSingletonPropXmlGetter;
    private boolean hasSingletonPropNullCheck;
    private boolean hasOptionalPropExistanceCheck;
    private boolean hasSeveralPropArrayAccess;
    private boolean hasSeveralPropArrayElementAccess;
    private boolean hasSeveralPropArrayXmlGetter;
    private boolean hasSeveralPropArrayElementXmlGetter;
    private boolean hasSeveralPropNullCheck;
    private boolean hasSeveralPropSizeAccess;
    private boolean hasListGetter15GetList;
    private boolean hasSingletonPropSetter;
    private boolean hasSingletonPropXmlSetter;
    private boolean hasSingletonPropCreation;
    private boolean hasSingletonPropSetNull;
    private boolean hasOptionalPropUnset;
    private boolean hasSeveralPropArraySetter;
    private boolean hasSeveralPropArrayElementSetter;
    private boolean hasSeveralPropArrayXmlSetter;
    private boolean hasSeveralPropArrayElementXmlSetter;
    private boolean hasSeveralPropSetNull;
    private boolean hasSeveralPropElementInsertion;
    private boolean hasSeveralPropElementAddition;
    private boolean hasSeveralPropNewElementInsertion;
    private boolean hasSeveralPropNewElementAddition;
    private boolean hasSeveralPropElementRemove;
    private boolean hasSeveralPropListAssignment;
    private boolean hasSingletonSetterDateTimeWithTimezone;
    private boolean hasSingletonGetterDateTimeWithTimezone;
    private boolean hasListGetter15GetListDateTimeWithTimezone;

    public XBeansClassProp(String name, String type, String xtype, boolean isAttr) {
        this.name = name;
        this.type = type;
        this.xtype = xtype;
        this.isAttr = isAttr;
    }

    public boolean hasListGetter15GetList() {
        return hasListGetter15GetList;
    }

    public void setHasListGetter15GetList(boolean hasListGetter15GetList) {
        this.hasListGetter15GetList = hasListGetter15GetList;
    }

    public boolean hasOptionalPropExistanceCheck() {
        return hasOptionalPropExistanceCheck;
    }

    public void setHasOptionalPropExistanceCheck(boolean hasOptionalPropExistanceCheck) {
        this.hasOptionalPropExistanceCheck = hasOptionalPropExistanceCheck;
    }

    public boolean hasOptionalPropUnset() {
        return hasOptionalPropUnset;
    }

    public void setHasOptionalPropUnset(boolean hasOptionalPropUnset) {
        this.hasOptionalPropUnset = hasOptionalPropUnset;
    }

    public boolean hasSeveralPropArrayAccess() {
        return hasSeveralPropArrayAccess;
    }

    public void setHasSeveralPropArrayAccess(boolean hasSeveralPropArrayAccess) {
        this.hasSeveralPropArrayAccess = hasSeveralPropArrayAccess;
    }

    public boolean hasSeveralPropArrayElementAccess() {
        return hasSeveralPropArrayElementAccess;
    }

    public void setHasSeveralPropArrayElementAccess(boolean hasSeveralPropArrayElementAccess) {
        this.hasSeveralPropArrayElementAccess = hasSeveralPropArrayElementAccess;
    }

    public boolean hasSeveralPropListAssignment() {
        return hasSeveralPropListAssignment;
    }

    public void setHasSeveralPropListAssignment(boolean hasSeveralPropListAssignment) {
        this.hasSeveralPropListAssignment = hasSeveralPropListAssignment;
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

    public boolean hasSeveralPropElementAddition() {
        return hasSeveralPropElementAddition;
    }

    public void setHasSeveralPropElementAddition(boolean hasSeveralPropElementAddition) {
        this.hasSeveralPropElementAddition = hasSeveralPropElementAddition;
    }

    public boolean hasSeveralPropElementInsertion() {
        return hasSeveralPropElementInsertion;
    }

    public void setHasSeveralPropElementInsertion(boolean hasSeveralPropElementInsertion) {
        this.hasSeveralPropElementInsertion = hasSeveralPropElementInsertion;
    }

    public boolean hasSeveralPropElementRemove() {
        return hasSeveralPropElementRemove;
    }

    public void setHasSeveralPropElementRemove(boolean hasSeveralPropElementRemove) {
        this.hasSeveralPropElementRemove = hasSeveralPropElementRemove;
    }

    public boolean hasSeveralPropNewElementAddition() {
        return hasSeveralPropNewElementAddition;
    }

    public void setHasSeveralPropNewElementAddition(boolean hasSeveralPropNewElementAddition) {
        this.hasSeveralPropNewElementAddition = hasSeveralPropNewElementAddition;
    }

    public boolean hasSeveralPropNewElementInsertion() {
        return hasSeveralPropNewElementInsertion;
    }

    public void setHasSeveralPropNewElementInsertion(boolean hasSeveralPropNewElementInsertion) {
        this.hasSeveralPropNewElementInsertion = hasSeveralPropNewElementInsertion;
    }

    public boolean hasSeveralPropNullCheck() {
        return hasSeveralPropNullCheck;
    }

    public void setHasSeveralPropNullCheck(boolean hasSeveralPropNullCheck) {
        this.hasSeveralPropNullCheck = hasSeveralPropNullCheck;
    }

    public boolean hasSeveralPropSetNull() {
        return hasSeveralPropSetNull;
    }

    public void setHasSeveralPropSetNull(boolean hasSeveralPropSetNull) {
        this.hasSeveralPropSetNull = hasSeveralPropSetNull;
    }

    public boolean hasSeveralPropSizeAccess() {
        return hasSeveralPropSizeAccess;
    }

    public void setHasSeveralPropSizeAccess(boolean hasSeveralPropSizeAccess) {
        this.hasSeveralPropSizeAccess = hasSeveralPropSizeAccess;
    }

    public boolean hasSingletonPropCreation() {
        return hasSingletonPropCreation;
    }

    public void setHasSingletonPropCreation(boolean hasSingletonPropCreation) {
        this.hasSingletonPropCreation = hasSingletonPropCreation;
    }

    public boolean hasSingletonPropGetter() {
        return hasSingletonPropGetter;
    }

    public void setHasSingletonPropGetter(boolean hasSingletonPropGetter) {
        this.hasSingletonPropGetter = hasSingletonPropGetter;
    }

    public boolean hasSingletonPropNullCheck() {
        return hasSingletonPropNullCheck;
    }

    public void setHasSingletonPropNullCheck(boolean hasSingletonPropNullCheck) {
        this.hasSingletonPropNullCheck = hasSingletonPropNullCheck;
    }

    public boolean hasSingletonPropSetNull() {
        return hasSingletonPropSetNull;
    }

    public void setHasSingletonPropSetNull(boolean hasSingletonPropSetNull) {
        this.hasSingletonPropSetNull = hasSingletonPropSetNull;
    }

    public boolean hasSingletonPropSetter() {
        return hasSingletonPropSetter;
    }

    public void setHasSingletonPropSetter(boolean hasSingletonPropSetter) {
        this.hasSingletonPropSetter = hasSingletonPropSetter;
    }

    public boolean hasSingletonPropXmlGetter() {
        return hasSingletonPropXmlGetter;
    }

    public void setHasSingletonPropXmlGetter(boolean hasSingletonPropXmlGetter) {
        this.hasSingletonPropXmlGetter = hasSingletonPropXmlGetter;
    }

    public boolean hasSingletonPropXmlSetter() {
        return hasSingletonPropXmlSetter;
    }

    public void setHasSingletonPropXmlSetter(boolean hasSingletonPropXmlSetter) {
        this.hasSingletonPropXmlSetter = hasSingletonPropXmlSetter;
    }
    
    public boolean hasSingletonGetterDateTimeWithTimezone() {
        return hasSingletonGetterDateTimeWithTimezone;
    }

    public void setHasSingletonGetterDateTimeWithTimezone(boolean hasSingletonDateTimeWithTimezoneGetter) {
        this.hasSingletonGetterDateTimeWithTimezone = hasSingletonDateTimeWithTimezoneGetter;
    }

    public boolean hasSingletonSetterDateTimeWithTimezone() {
        return hasSingletonSetterDateTimeWithTimezone;
    }

    public void setHasSingletonSetterDateTimeWithTimezone(boolean hasSingletonDateTimeWithTimezoneSetter) {
        this.hasSingletonSetterDateTimeWithTimezone = hasSingletonDateTimeWithTimezoneSetter;
    }

    public boolean hasListGetter15GetListDateTimeWithTimezone() {
        return this.hasListGetter15GetListDateTimeWithTimezone;
    }
    
    public void setHasListGetter15GetListDateTimeWithTimezone(boolean hasListGetter15GetListDateTimeWithTimezone) {
        this.hasListGetter15GetListDateTimeWithTimezone = hasListGetter15GetListDateTimeWithTimezone;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getXmlType() {
        return xtype;
    }

    public boolean isAttribute() {
        return isAttr;
    }
}

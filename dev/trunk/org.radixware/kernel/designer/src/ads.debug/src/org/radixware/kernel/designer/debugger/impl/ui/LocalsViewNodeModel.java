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

package org.radixware.kernel.designer.debugger.impl.ui;

import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.radixware.kernel.designer.debugger.impl.ArrayElement;
import org.radixware.kernel.designer.debugger.impl.ArrayReferenceWrapper;
import org.radixware.kernel.designer.debugger.impl.FieldWrapper;
import org.radixware.kernel.designer.debugger.impl.LocalVariableWrapper;
import org.radixware.kernel.designer.debugger.impl.PropertyWrapper;
import org.radixware.kernel.designer.debugger.impl.ThisReferenceWrapper;
import org.radixware.kernel.designer.debugger.impl.VariableWrapper;


public class LocalsViewNodeModel extends AbstractNodeModel {

    public static final String FIELD =
            "org/netbeans/modules/debugger/resources/watchesView/Field";
    public static final String LOCAL =
            "org/netbeans/modules/debugger/resources/localsView/LocalVariable";
    public static final String FIXED_WATCH =
            "org/netbeans/modules/debugger/resources/watchesView/FixedWatch";
    public static final String STATIC_FIELD =
            "org/netbeans/modules/debugger/resources/watchesView/StaticField";
    public static final String SUPER =
            "org/netbeans/modules/debugger/resources/watchesView/SuperVariable";
    public static final String STATIC =
            "org/netbeans/modules/debugger/resources/watchesView/SuperVariable";

    @Override
    public String getDisplayName(Object node) throws UnknownTypeException {
        if (node instanceof ThisReferenceWrapper) {
            return "this";
        } else if (node instanceof VariableWrapper) {
            return ((VariableWrapper) node).getDisplayName();
        } else if (node instanceof LocalsViewTreeModel.FieldGroup) {
            return ((LocalsViewTreeModel.FieldGroup) node).getDisplayName();
        } else if (node instanceof LocalsViewTreeModel.PropertiesGroup) {
            return ((LocalsViewTreeModel.PropertiesGroup) node).getDisplayName();
        } else if (node instanceof ArrayReferenceWrapper.ValueRange) {
            return ((ArrayReferenceWrapper.ValueRange) node).getDisplayName();
        } else {
            return "";
        }
    }

    @Override
    public String getIconBase(Object node) throws UnknownTypeException {
        if (node instanceof ThisReferenceWrapper) {
            return LOCAL;
        } else if (node instanceof ArrayElement) {
            return LOCAL;
        } else if (node instanceof ArrayReferenceWrapper.ValueRange) {
            return SUPER;
        } else if (node instanceof LocalVariableWrapper) {
            return LOCAL;
        } else if (node instanceof FieldWrapper) {
            return FIELD;
        } else if (node instanceof PropertyWrapper) {
            return FIELD;
        } else if (node instanceof LocalsViewTreeModel.FieldGroup) {
            return STATIC;
        } else if (node instanceof LocalsViewTreeModel.PropertiesGroup) {
            return STATIC;
        } else {
            return null;
        }
    }

    @Override
    public String getShortDescription(Object node) throws UnknownTypeException {
        return "";
    }
}

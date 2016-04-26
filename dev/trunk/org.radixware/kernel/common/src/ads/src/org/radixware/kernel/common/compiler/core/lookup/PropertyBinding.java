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

package org.radixware.kernel.common.compiler.core.lookup;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.eclipse.jdt.internal.compiler.lookup.AdsPropertyDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import static org.eclipse.jdt.internal.compiler.lookup.Binding.NO_PARAMETERS;
import static org.eclipse.jdt.internal.compiler.lookup.Binding.NO_TYPES;
import org.eclipse.jdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;


public class PropertyBinding extends FieldBinding {

    private MethodBinding getter;
    private MethodBinding setter;
    public final AdsPropertyDef property;
    public AdsPropertyDeclaration propertyDeclaration;

    public PropertyBinding(ReferenceBinding declaringClass, AdsPropertyDeclaration property) {
        this.declaringClass = declaringClass;
        this.property = property.getProperty();
        this.name = property.name;
        this.propertyDeclaration = property;
        this.modifiers = property.modifiers;
    }

    public PropertyBinding(ReferenceBinding declaringClass, char[] name, MethodBinding getter, MethodBinding setter) {
        this.declaringClass = declaringClass;
        this.name = name;
        this.property = null;
        this.type = getter.returnType;
        this.modifiers = getter.modifiers;
        this.getter = getter;
        this.setter = setter;
    }

    public MethodBinding getGetter() {
        if (getter == null && propertyDeclaration != null) {
            MethodBinding[] methods = declaringClass.getMethods(CharOperation.concat("get".toCharArray(), name));
            for (MethodBinding method : methods) {
                if (method.parameters == NO_PARAMETERS && method.returnType == this.type) {
                    getter = method;
                    break;
                }
            }
        }
        if (getter == null) {
            getter = new ProblemMethodBinding(CharOperation.concat("get".toCharArray(), name), NO_TYPES, ProblemReasons.NotFound);
        }
        return getter;
    }

    public MethodBinding getSetter() {
        if (setter == null && propertyDeclaration != null) {
            MethodBinding[] methods = declaringClass.getMethods(CharOperation.concat("set".toCharArray(), name));
            for (MethodBinding method : methods) {
                if (method.parameters != null && method.parameters.length == 1 && method.parameters[0] == this.type && method.returnType == BaseTypeBinding.VOID) {
                    setter = method;
                    break;
                }
            }
        }
        if (setter == null) {
            setter = new ProblemMethodBinding(CharOperation.concat("set".toCharArray(), name), NO_TYPES, ProblemReasons.NotFound);
        }
        return setter;
    }

    public boolean isAdsProperty() {
        return property != null;
    }

    public void createGetterAndSetter() {
    }
}

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

package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.radixware.kernel.common.defs.Definition;


public class AdsNestedTypeBinding extends NestedTypeBinding {

    public final Definition definition;

    public AdsNestedTypeBinding(char[][] compoundName, AdsDefinitionScope scope, SourceTypeBinding enclosingType) {
        super(compoundName, scope, enclosingType);
        definition = scope.definition;
    }

    void checkSyntheticArgsAndFields() {
        if (isStatic()) {
            return;
        }
        if (isInterface()) {
            return;
        }
        this.addSyntheticArgumentAndField(this.enclosingType);
    }
    /* Answer the receiver's constant pool name.
     *
     * NOTE: This method should only be used during/after code gen.
     */
    @Override
    public char[] constantPoolName() /* java/lang/Object */ {
        if (this.constantPoolName != null) {
            return this.constantPoolName;
        }
        return this.constantPoolName = CharOperation.concat(enclosingType().constantPoolName(), this.sourceName, '$');
    }

    /**
     * @see
     * org.eclipse.jdt.internal.compiler.lookup.Binding#initializeDeprecatedAnnotationTagBits()
     */
    @Override
    public void initializeDeprecatedAnnotationTagBits() {
        if ((this.tagBits & TagBits.DeprecatedAnnotationResolved) == 0) {
            super.initializeDeprecatedAnnotationTagBits();
            if ((this.tagBits & TagBits.AnnotationDeprecated) == 0) {
                // check enclosing type
                ReferenceBinding enclosing;
                if (((enclosing = enclosingType()).tagBits & TagBits.DeprecatedAnnotationResolved) == 0) {
                    enclosing.initializeDeprecatedAnnotationTagBits();
                }
                if (enclosing.isViewedAsDeprecated()) {
                    this.modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
                }
            }
        }
    }

    public String toString() {
        return "Member type : " + new String(sourceName()) + " " + super.toString(); //$NON-NLS-2$ //$NON-NLS-1$
    }

    @Override
    public FieldBinding getField(char[] fieldName, boolean needResolve) {
        FieldBinding result = super.getField(fieldName, needResolve); //To change body of generated methods, choose Tools | Templates.
        return result;
    }
}

/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.core.compiler.CharOperation;

/**
 *
 * @author akrylov
 */
public class AdsRawTypeBinding extends RawTypeBinding {

    public AdsRawTypeBinding(ReferenceBinding type, ReferenceBinding enclosingType, LookupEnvironment environment) {
        super(type, enclosingType, environment);
    }

    @Override
    public char[] readableName() /*java.lang.Object,  p.X<T> */ {
        char[] readableName;
        if (isMemberType()) {
            readableName = CharOperation.concat(enclosingType().readableName(), this.sourceName, '.');
        } else {
            final ReferenceBinding parameterizedTyp = actualType();
            if (parameterizedTyp instanceof AdsBinaryTypeBinding && ((AdsBinaryTypeBinding) parameterizedTyp).getDefinition() != null) {
                readableName = ("`" + ((AdsBinaryTypeBinding) parameterizedTyp).getDefinition().getQualifiedName() + "`").toCharArray();
            } else {
                readableName = CharOperation.concatWith(actualType().compoundName, '.');
            }
        }
        return readableName;
    }

    /**
     * @see org.eclipse.jdt.internal.compiler.lookup.Binding#shortReadableName()
     */
    public char[] shortReadableName() /*Object*/ {
        char[] shortReadableName;
        if (isMemberType()) {
            shortReadableName = CharOperation.concat(enclosingType().shortReadableName(), this.sourceName, '.');
        } else {
            final ReferenceBinding parameterizedTyp = actualType();
            if (parameterizedTyp instanceof AdsBinaryTypeBinding && ((AdsBinaryTypeBinding) parameterizedTyp).getDefinition() != null) {
                shortReadableName = ("`" + ((AdsBinaryTypeBinding) parameterizedTyp).getDefinition().getQualifiedName() + "`").toCharArray();
            } else {
                shortReadableName = parameterizedTyp.sourceName;
            }
        }
        return shortReadableName;
    }
}

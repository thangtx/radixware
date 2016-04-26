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
public class AdsParameterizedTypeBinding extends ParameterizedTypeBinding {

    public AdsParameterizedTypeBinding(ReferenceBinding type, TypeBinding[] arguments, ReferenceBinding enclosingType, LookupEnvironment environment) {
        super(type, arguments, enclosingType, environment);
    }

    @Override
    public char[] readableName() {
        final StringBuilder nameBuffer = new StringBuilder();
        if (isMemberType()) {
            nameBuffer.append(CharOperation.concat(enclosingType().readableName(), sourceName, '.'));
        } else {
            final ReferenceBinding genericType = genericType();
            if (genericType instanceof AdsBinaryTypeBinding && ((AdsBinaryTypeBinding) genericType).getDefinition() != null) {
                nameBuffer.append(genericType.readableName());
            } else {
                nameBuffer.append(CharOperation.concatWith(genericType.compoundName, '.'));
            }
        }
        if (arguments != null) {
            nameBuffer.append('<');
            for (int i = 0, length = arguments.length; i < length; i++) {
                if (i > 0) {
                    nameBuffer.append(',');
                }
                nameBuffer.append(arguments[i].readableName());
            }
            nameBuffer.append('>');
        }
        int nameLength = nameBuffer.length();
        char[] shortReadableName = new char[nameLength];
        nameBuffer.getChars(0, nameLength, shortReadableName, 0);
        return shortReadableName;
    }

    @Override
    public char[] shortReadableName() {
        final StringBuffer nameBuffer = new StringBuffer(10);
        if (isMemberType()) {
            nameBuffer.append(CharOperation.concat(enclosingType().shortReadableName(), this.sourceName, '.'));
        } else {
            final ReferenceBinding genericType = genericType();
            if (genericType instanceof AdsBinaryTypeBinding && ((AdsBinaryTypeBinding) genericType).getDefinition() != null) {
                nameBuffer.append(genericType.readableName());
            } else {
                nameBuffer.append(CharOperation.concatWith(genericType.compoundName, '.'));
            }
        }
        if (this.arguments != null) {
            nameBuffer.append('<');
            for (int i = 0, length = this.arguments.length; i < length; i++) {
                if (i > 0) {
                    nameBuffer.append(',');
                }
                nameBuffer.append(this.arguments[i].shortReadableName());
            }
            nameBuffer.append('>');
        }
        int nameLength = nameBuffer.length();
        char[] shortReadableName = new char[nameLength];
        nameBuffer.getChars(0, nameLength, shortReadableName, 0);
        return shortReadableName;
    }
}

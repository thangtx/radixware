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
package org.radixware.kernel.common.compiler.core.ast;

import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;

public class JMLParameterizedSingleTypeReference extends ParameterizedSingleTypeReference {

    private static TypeReference[] convertTypeArguments(TypeReference[] args, RadixObjectLocator locator) {
        if (args == null) {
            return null;
        }
        TypeReference[] result = new TypeReference[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = Java2JmlConverter.convertTypeReference(args[i], locator);
        }
        return result;
    }

    public JMLParameterizedSingleTypeReference(ParameterizedSingleTypeReference src, RadixObjectLocator locator) {
        super(src.token, convertTypeArguments(src.typeArguments, locator), src.dimensions(), 0);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.statementEnd = src.statementEnd;
        this.originalSourceEnd = this.sourceEnd;
    }
}

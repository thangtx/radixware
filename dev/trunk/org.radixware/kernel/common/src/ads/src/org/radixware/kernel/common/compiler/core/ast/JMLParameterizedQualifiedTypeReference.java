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

import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;

public class JMLParameterizedQualifiedTypeReference extends ParameterizedQualifiedTypeReference {

    private static TypeReference[][] convertTypeArguments(TypeReference[][] args, RadixObjectLocator locator) {
        if (args == null) {
            return null;
        }
        TypeReference[][] result = new TypeReference[args.length][];
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                result[i] = new TypeReference[args[i].length];
                for (int a = 0; a < args[i].length; a++) {
                    result[i][a] = Java2JmlConverter.convertTypeReference(args[i][a], locator);
                }
            }

        }
        return result;
    }

    public JMLParameterizedQualifiedTypeReference(ParameterizedQualifiedTypeReference src, RadixObjectLocator locator) {
        super(src.tokens, convertTypeArguments(src.typeArguments, locator), src.dimensions(), src.sourcePositions);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.statementEnd = src.statementEnd;
    }
}

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

package org.eclipse.jdt.internal.compiler.ast;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class AdsParameterizedQualifiedTypeReference extends ParameterizedQualifiedTypeReference {

    private static char[][] tokenize(Definition referenceContext, AdsTypeDeclaration[] tokens, char[][] charTokens) {
        char[][] result = new char[tokens.length][];
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] != null) {
                result[i] = tokens[i].getQualifiedName(referenceContext).toCharArray();
            } else {
                result[i] = charTokens[i];
            }
        }
        return result;
    }

    public AdsParameterizedQualifiedTypeReference(Definition referenceContext, ParameterizedQualifiedTypeReference src, TypeReference[][] arguments, AdsTypeDeclaration[] tokens, char[][] charTokens) {
        super(tokenize(referenceContext, tokens, charTokens), arguments, src.dimensions, src.sourcePositions);
    }
}

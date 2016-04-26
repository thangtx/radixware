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

import org.eclipse.jdt.internal.codeassist.complete.CompletionOnSingleNameReference;


public class JMLCompletionOnSingleNameReference extends CompletionOnSingleNameReference {

    public JMLCompletionOnSingleNameReference(CompletionOnSingleNameReference src) {
        super(src.token, 0, src.isInsideAnnotationAttribute);
        this.sourceStart = src.sourceStart;
        this.sourceEnd = src.sourceEnd;
        this.statementEnd = src.statementEnd;
        this.possibleKeywords = src.possibleKeywords;
        this.canBeExplicitConstructor = src.canBeExplicitConstructor;

    }
}

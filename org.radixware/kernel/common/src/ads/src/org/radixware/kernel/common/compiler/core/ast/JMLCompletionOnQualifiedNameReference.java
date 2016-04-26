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

import org.eclipse.jdt.internal.codeassist.complete.CompletionNodeFound;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnQualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

public class JMLCompletionOnQualifiedNameReference extends CompletionOnQualifiedNameReference {

    public JMLCompletionOnQualifiedNameReference(CompletionOnQualifiedNameReference src) {
        super(src.tokens, src.completionIdentifier, src.sourcePositions, src.isInsideAnnotationAttribute);
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {
        NameReference ref;
        if (this.tokens.length == 1) {
            ref = new JMLSingleNameReference(this.tokens[0], this.sourceStart, this.sourceEnd);
        } else {
            ref = new JMLQualifiedNameReference(this, null);
        }
        this.binding = ref.resolveType(scope);
        if (binding != null && binding.isValidBinding()) {
            throw new CompletionNodeFound(this, binding, scope);
        } else {
            throw new CompletionNodeFound();
        }

    }
}

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
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.radixware.kernel.common.defs.Definition;

public class AdsProblemReferenceBinding extends ProblemReferenceBinding {

    private Definition definition;

    public AdsProblemReferenceBinding(AdsMissingBinaryType missingType, int problemReason) {
        super(missingType.compoundName, null, problemReason);
        this.tagBits |= TagBits.IsBinaryBinding | TagBits.HierarchyHasProblems | TagBits.HasMissingType;
        this.definition = missingType.referencedDef;
        modifiers |= ClassFileConstants.AccPublic;
        this.closestMatch = this;
        this.tagBits |= TagBits.IsBinaryBinding;
    }

    @Override
    public char[] readableName() {
        return ("`" + definition.getQualifiedName() + "`").toCharArray();
    }

    @Override
    public char[] shortReadableName() {
        return ("`" + definition.getQualifiedName() + "`").toCharArray();
    }
}

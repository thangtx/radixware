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

import org.radixware.kernel.common.compiler.CompilerUtils;
import org.radixware.kernel.common.defs.Definition;


public class AdsMissingTypeBinding extends MissingTypeBinding {

    private Definition definition;

    public AdsMissingTypeBinding(Definition referencedDef, PackageBinding packageBinding, char[][] compoundName, LookupEnvironment environment) {
        super(packageBinding, compoundName, environment);
        this.definition = referencedDef;
    }

    @Override
    public char[] readableName() {
        return CompilerUtils.getTypeDisplayName(definition, compoundName).toCharArray();
    }

    @Override
    public char[] shortReadableName() {
        return CompilerUtils.getTypeDisplayName(definition, compoundName).toCharArray();
    }
}

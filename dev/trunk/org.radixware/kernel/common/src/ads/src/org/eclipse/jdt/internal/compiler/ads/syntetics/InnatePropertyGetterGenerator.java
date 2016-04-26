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

package org.eclipse.jdt.internal.compiler.ads.syntetics;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.radixware.kernel.common.defs.Definition;


class InnatePropertyGetterGenerator extends PersistentPropertyGetterGenerator {

    public static final char[] ACCESSOR = "getNativeProp".toCharArray();

    public InnatePropertyGetterGenerator(Definition referenceContext, CompilationResult result, ReferenceContext context) {
        super(referenceContext, result, context);
    }

    @Override
    protected char[] getNativeAccessorSelector() {
        return ACCESSOR;
    }
}
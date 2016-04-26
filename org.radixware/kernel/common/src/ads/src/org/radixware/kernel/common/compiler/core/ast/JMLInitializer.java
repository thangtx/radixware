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

import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;


public class JMLInitializer extends Initializer {

    private boolean wasResolved = false;

    public JMLInitializer(Initializer src) {
        super(src.block, src.modifiers);
    }

    @Override
    public void resolve(MethodScope scope) {
        if (wasResolved) {
            return;
        }
        super.resolve(scope); //To change body of generated methods, choose Tools | Templates.
    }
}

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

import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMemberAccess;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.compiler.core.ast.Java2JmlConverter;


public class JMLCompletionOnMemberAccess extends CompletionOnMemberAccess {

    public JMLCompletionOnMemberAccess(CompletionOnMemberAccess src) {
        super(src.token, src.nameSourcePosition, src.isInsideAnnotation);
        this.receiver = src.receiver;
    }

    @Override
    public TypeBinding resolveType(BlockScope scope) {        
        return super.resolveType(scope); //To change body of generated methods, choose Tools | Templates.
    }
}

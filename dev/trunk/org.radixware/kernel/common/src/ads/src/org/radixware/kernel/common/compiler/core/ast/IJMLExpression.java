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

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.RadixObject;


public interface IJMLExpression {

    public class RadixObjectLocation {

        public final RadixObject radixObject;
        public int offset;
        public int length;

        public RadixObjectLocation(RadixObject radixObject) {
            this.radixObject = radixObject;
        }

        public RadixObjectLocation(RadixObject radixObject, int offset, int len) {
            this.radixObject = radixObject;
            this.offset = offset;
            this.length = len;
        }
    }

    RadixObjectLocation getRadixObject(int index);

    Expression getSubstitution(BlockScope scope);

    Constant resolveCase(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement);

    Constant resolveCaseOriginal(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement);
}

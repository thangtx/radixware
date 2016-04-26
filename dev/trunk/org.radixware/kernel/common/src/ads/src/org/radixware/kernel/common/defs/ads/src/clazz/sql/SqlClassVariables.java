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

package org.radixware.kernel.common.defs.ads.src.clazz.sql;

import org.eclipse.jdt.internal.compiler.ast.ReenterableSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;


public class SqlClassVariables {

    public static final char[] LOOP_VAR = "$i".toCharArray();
    public static final char[] SIZE_VAR = "$size".toCharArray();
    public static final char[] PARAM_CNT_VAR = "$paramCnt".toCharArray();
    public static final char[] STMT_VAR = "$statement".toCharArray();
    public static final char[] APPEND_METHOD_NAME = "append".toCharArray();
    public static final char[] GET_METHOD_NAME = "get".toCharArray();

    public static SingleNameReference nameRef(char[] name) {
        return new ReenterableSingleNameReference(name);
    }

    public static SingleNameReference nameRef(String name) {
        return nameRef(name.toCharArray());
    }

    public static SingleNameReference loopVarRef() {
        return nameRef(LOOP_VAR);
    }

    public static SingleNameReference sizeVarRef() {
        return nameRef(SIZE_VAR);
    }

    public static SingleNameReference paramCntVarRef() {
        return nameRef(PARAM_CNT_VAR);
    }

    public static SingleNameReference stmtVarRef() {
        return nameRef(STMT_VAR);
    }
}

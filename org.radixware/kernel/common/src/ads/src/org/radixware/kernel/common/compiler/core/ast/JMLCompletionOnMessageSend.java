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

import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMessageSend;
import org.eclipse.jdt.internal.compiler.impl.Constant;


public class JMLCompletionOnMessageSend extends CompletionOnMessageSend {

    public JMLCompletionOnMessageSend(CompletionOnMessageSend src) {
        super();
        this.typeArguments = src.typeArguments;
        this.selector = src.selector;
        this.receiver = src.receiver;
        this.arguments = src.arguments;
        this.nameSourcePosition = src.nameSourcePosition;
        this.sourceEnd = src.sourceEnd;
        this.sourceStart = src.sourceStart;
        this.constant = Constant.NotAConstant;
        this.bits = src.bits;
    }
}

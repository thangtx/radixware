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
package org.radixware.kernel.common.svn.utils;

import org.radixware.kernel.common.enums.EEventSeverity;

/**
 *
 * @author npopov
 */
public interface IClassCheckProblem {
    
    EEventSeverity getSeverity();

    int getLine();
    
    String getMethodName();
    
    String getClassName();

    String getType();
    
    String getSignature();
    
    /**
     * Optional method, default return: "type : signature"
     * @return signature in human-readable format
     */
    String getPrettySignature();

    String getMessage();

    Throwable getException();
}

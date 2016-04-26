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

package org.radixware.kernel.designer.debugger.impl.ui;

import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.radixware.kernel.designer.debugger.impl.CallStackFrame;


public class CallStackNodeModel extends AbstractNodeModel {

    public static final String CALL_STACK =
            "org/netbeans/modules/debugger/resources/callStackView/NonCurrentFrame";
    public static final String CURRENT_CALL_STACK =
            "org/netbeans/modules/debugger/resources/callStackView/CurrentFrame";

    @Override
    public String getDisplayName(Object node) throws UnknownTypeException {
        if (node instanceof CallStackFrame) {
            CallStackFrame frame = (CallStackFrame) node;
            if (frame.isCurrent()) {
                return "<html><b>" + frame.getDisplayName().replace("<", "&lt;").replace(">", "&gt;") + "</b></html>";
            } else {
                return frame.getDisplayName();
            }
        } else {
            return node.toString();
        }
    }

    @Override
    public String getIconBase(Object node) throws UnknownTypeException {
        if (node instanceof CallStackFrame) {
            CallStackFrame frame = (CallStackFrame) node;
            if (frame.isCurrent()) {
                return CURRENT_CALL_STACK;
            } else {
                return CALL_STACK;
            }
        } else {
            return CALL_STACK;
        }
    }

    @Override
    public String getShortDescription(Object node) throws UnknownTypeException {
        return "";
    }
}

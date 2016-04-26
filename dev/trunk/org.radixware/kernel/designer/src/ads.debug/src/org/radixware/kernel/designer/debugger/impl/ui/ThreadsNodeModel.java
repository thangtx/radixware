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

import java.util.LinkedList;
import java.util.List;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.radixware.kernel.designer.debugger.impl.ThreadGroupWrapper;
import org.radixware.kernel.designer.debugger.impl.ThreadWrapper;


public class ThreadsNodeModel extends AbstractNodeModel {

    public static final String CURRENT_THREAD =
            "org/netbeans/modules/debugger/resources/threadsView/CurrentThread"; // NOI18N
    public static final String RUNNING_THREAD =
            "org/netbeans/modules/debugger/resources/threadsView/RunningThread"; // NOI18N
    public static final String SUSPENDED_THREAD =
            "org/netbeans/modules/debugger/resources/threadsView/SuspendedThread"; // NOI18N
    public static final String THREAD_GROUP =
            "org/netbeans/modules/debugger/resources/threadsView/ThreadGroup"; // NOI18N
    public static final String CURRENT_THREAD_GROUP =
            "org/netbeans/modules/debugger/resources/threadsView/CurrentThreadGroup"; // NOI18N

    @Override
    public String getDisplayName(Object node) throws UnknownTypeException {
        if (node instanceof ThreadWrapper) {
            ThreadWrapper w = (ThreadWrapper) node;
            if (w.isCurrent()) {
                return "<html><b>" + w.getDisplayName() + "<b></html>";
            } else {
                return w.getDisplayName();
            }
        } else if (node instanceof ThreadGroupWrapper) {
            ThreadGroupWrapper tgw = (ThreadGroupWrapper) node;
            if (tgw.isCurrentThreadGroup()) {
                return "<html><b>" + tgw.getDisplayName() + "</b></html>";
            } else {
                return tgw.getDisplayName();
            }

        } else {
            return node.toString();
        }
    }

    @Override
    public String getIconBase(Object node) throws UnknownTypeException {
        if (node instanceof ThreadWrapper) {
            ThreadWrapper wrapper = (ThreadWrapper) node;
            if (wrapper.isCurrent()) {
                return CURRENT_THREAD;
            } else {
                if (wrapper.isRunning()) {
                    return RUNNING_THREAD;
                } else {
                    return SUSPENDED_THREAD;
                }
            }
        } else if (node instanceof ThreadGroupWrapper) {
            ThreadGroupWrapper tgw = (ThreadGroupWrapper) node;
            if (tgw.isCurrentThreadGroup()) {
                return CURRENT_THREAD_GROUP;
            } else {
                return THREAD_GROUP;
            }
        } else {
            return node.toString();
        }
    }

    @Override
    public String getShortDescription(Object node) throws UnknownTypeException {
        return "";
    }
}

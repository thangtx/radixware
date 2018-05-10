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

package org.radixware.kernel.designer.common.dialogs.stack;

import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public class StackTraceParserTopComponent extends TopComponent {

    private static final String PREFERRED_ID = "StackTraceParserTopComponent";
    private final StackTraceList traceList;

    private StackTraceParserTopComponent() {
        super();
        setName("Stack Trace");
        setToolTipText("Stack Trace");
        setIcon(RadixWareIcons.CHECK.STACK.getImage());
        this.setLayout(new BorderLayout());
        traceList = new StackTraceList();
        traceList.addListner();
        add(new StackTraceParserToolBar(traceList), BorderLayout.NORTH);
        add(traceList, BorderLayout.CENTER);
        List<Branch> branches = new ArrayList<>(RadixFileUtil.getOpenedBranches());
        if (!branches.isEmpty()) {
            traceList.setBranch(branches.get(0));
        }
    }
    private static StackTraceParserTopComponent instance = null;

    public static synchronized StackTraceParserTopComponent getDefault() {
        if (instance == null) {
            instance = new StackTraceParserTopComponent();
        }
        return instance;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public static synchronized StackTraceParserTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(StackTraceParserTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof StackTraceParserTopComponent) {
            return (StackTraceParserTopComponent) win;
        }
        Logger.getLogger(StackTraceParserTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return StackTraceParserTopComponent.getDefault();
        }
    }    

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_ALWAYS;
    }
}

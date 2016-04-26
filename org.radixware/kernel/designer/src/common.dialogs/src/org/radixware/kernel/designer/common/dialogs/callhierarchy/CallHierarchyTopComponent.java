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

package org.radixware.kernel.designer.common.dialogs.callhierarchy;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.logging.Logger;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.UtilTabbedTopComponent;


@TopComponent.Description(preferredID = "CallHierarchyTopComponent", persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = false)
final class CallHierarchyTopComponent extends UtilTabbedTopComponent<AdsMethodDef> {

    public static synchronized CallHierarchyTopComponent findInstance() {
        TopComponent window = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (window == null) {
            Logger.getLogger(CallHierarchyTopComponent.class.getName()).warning(
                "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (window instanceof CallHierarchyTopComponent) {
            return (CallHierarchyTopComponent) window;
        }
        Logger.getLogger(CallHierarchyTopComponent.class.getName()).warning(
            "There seem to be multiple components with the '" + PREFERRED_ID
            + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    public static synchronized CallHierarchyTopComponent getDefault() {
        if (instance == null) {
            instance = new CallHierarchyTopComponent();
        }
        return instance;
    }

    private static final String PREFERRED_ID = "CallHierarchyTopComponent";
    private static CallHierarchyTopComponent instance;

    private static final String NAME = "Call Hierarchy";
    private static final int MAX_NAME_LEN = 20;

    public CallHierarchyTopComponent() {
        super();

        setIcon(RadixWareIcons.EDIT.SEARCH.getImage());
        setDisplayName(NAME);
        setToolTipText(NAME);
    }

    @Override
    protected String formatName(String name) {
        if (name != null && name.length() > MAX_NAME_LEN) {
            return name.substring(0, MAX_NAME_LEN - 3) + "...";
        }
        return name;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setName(NAME);
        setToolTipText(NAME);
    }

    @Override
    public CallHierarchyPanel createPanel(AdsMethodDef object) {
        return new CallHierarchyPanel();
    }

    @Override
    protected String getDefaultTitle() {
        return NAME;
    }
}

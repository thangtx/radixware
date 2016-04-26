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

package org.radixware.kernel.designer.common.dialogs.hierarchy;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.logging.Logger;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;


public class AdsClassHierarchyViewTopComponent extends TopComponent {

    private static final String PREFERRED_ID = "AdsClassHierarchyViewTopComponent";
    private static AdsClassHierarchyViewTopComponent instance;
    private AdsClassHierarchyViewPanel panel;

    public AdsClassHierarchyViewTopComponent() {
        setLayout(new BorderLayout());

    }

    public void setClassDef(AdsClassDef classDef) {
        if (panel != null) {
            remove(panel);
        }
        panel = new AdsClassHierarchyViewPanel(classDef, false);
        add(panel, BorderLayout.CENTER);
        setName("Hierarchy of \"" + classDef.getName() + "\"");
    }

    @Override
    public void requestActive() {
        super.requestActive();
        if (panel != null) {
            panel.requestFocusInWindow();
        }
    }

    /**
     * Obtain the FindUsagesResults instance. Never call {@link #getDefault} directly!
     */
    public static synchronized AdsClassHierarchyViewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(AdsClassHierarchyViewTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof AdsClassHierarchyViewTopComponent) {
            return (AdsClassHierarchyViewTopComponent) win;
        }
        Logger.getLogger(AdsClassHierarchyViewTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    public static synchronized AdsClassHierarchyViewTopComponent getDefault() {
        if (instance == null) {
            instance = new AdsClassHierarchyViewTopComponent();
        }
        return instance;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setName("Class Hierarchy");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
}

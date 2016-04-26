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


package org.radixware.kernel.designer.ads.editors.refactoring;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.logging.Logger;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


@TopComponent.Description(preferredID = "RefactoringTopComponent", persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = false,position = 199)
public class RefactoringTopComponent extends TopComponent {

    private static RefactoringTopComponent REFACTORING_TOP_COMPONENT = null;
    private static String PREFERRED_ID = "RefactoringTopComponent";

    public static synchronized RefactoringTopComponent findInstance() {
        TopComponent window = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (window == null) {
            Logger.getLogger(RefactoringTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (window instanceof RefactoringTopComponent) {
            return (RefactoringTopComponent) window;
        }
        Logger.getLogger(RefactoringTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }
    
    public static synchronized RefactoringTopComponent getDefault() {
        if (REFACTORING_TOP_COMPONENT == null) {
            REFACTORING_TOP_COMPONENT = new RefactoringTopComponent();
        }
        return REFACTORING_TOP_COMPONENT;
    }
    
    private final ChangesPanel changesPanel = new ChangesPanel();
    

    public RefactoringTopComponent() {
        setLayout(new BorderLayout());

        add(changesPanel, BorderLayout.CENTER);
        setDisplayName("Refactoring result");
    }

    public void open(RefactoringResult object) {
        changesPanel.open(object);
        this.open();
        this.requestActive();
    }
    
    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public Image getIcon() {
        return RadixWareDesignerIcon.EDIT.FIX.getImage();
    }
}

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

package org.radixware.kernel.designer.common.dialogs.check;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.text.DefaultEditorKit;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class CheckResultsTopComponent extends TopComponent {

    private static CheckResultsTopComponent instance;
    private static final String PREFERRED_ID = "CheckResultsTopComponent";
    private final CheckResultsToolbar toolbar;
    private final CheckResultsTree tree;
    

    private CheckResultsTopComponent() {
        super();

        setName("Problems");
        setToolTipText("Problems");
        setIcon(RadixWareIcons.CHECK.ERRORS.getImage());

        setLayout(new BorderLayout());

        tree = new CheckResultsTree();
        add(tree, BorderLayout.CENTER);

        toolbar = new CheckResultsToolbar(tree);
        add(toolbar, BorderLayout.NORTH);

        tree.setFilter(toolbar);

        getActionMap().put(DefaultEditorKit.copyAction, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tree.copyToClipboard();
            }
        });
    }

    public static synchronized CheckResultsTopComponent getDefault() {
        if (instance == null) {
            instance = new CheckResultsTopComponent();
        }
        return instance;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public static synchronized CheckResultsTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(CheckResultsTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof CheckResultsTopComponent) {
            return (CheckResultsTopComponent) win;
        }
        Logger.getLogger(CheckResultsTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
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
            return CheckResultsTopComponent.getDefault();
        }
    }

    public boolean isEmpty() {
        return tree.getRoot().isEmpty();
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_ALWAYS;
    }
}

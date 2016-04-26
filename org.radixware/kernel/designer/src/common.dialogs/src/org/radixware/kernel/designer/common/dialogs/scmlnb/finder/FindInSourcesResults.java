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

package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.text.DefaultEditorKit;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.designer.common.dialogs.results.ResultsToolbar;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class FindInSourcesResults extends TopComponent {

    private static FindInSourcesResults instance;
    private static final String PREFERRED_ID = "FindTextInScmlResults";
    private final ResultsToolbar toolbar;
    private final FindInSourcesResultsTree tree;

    protected FindInSourcesResults() {
        super();

        setName(NbBundle.getMessage(FindInSourcesResults.class, "CTL_FindTextInScmlResults"));
        setToolTipText(NbBundle.getMessage(FindInSourcesResults.class, "HINT_FindTextInScmlResults"));
        setIcon(RadixWareIcons.TREE.DEPENDENCIES.getImage());

        setLayout(new BorderLayout());

        tree = new FindInSourcesResultsTree();
        add(tree, BorderLayout.CENTER);

        toolbar = new ResultsToolbar(tree);
        add(toolbar, BorderLayout.NORTH);

        tree.setFilter(toolbar);

        getActionMap().put(DefaultEditorKit.copyAction, new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                tree.copyToClipboard();
            }
        });
    }

    public static synchronized FindInSourcesResults getDefault() {
        if (instance == null) {
            instance = new FindInSourcesResults();
        }
        return instance;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    /**
     * Obtain the FindInSourcesResults instance. Never call {@link #getDefault} directly!
     */
    public static synchronized FindInSourcesResults findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(FindInSourcesResults.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof FindInSourcesResults) {
            return (FindInSourcesResults) win;
        }
        Logger.getLogger(FindInSourcesResults.class.getName()).warning(
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
            return FindInSourcesResults.getDefault();
        }
    }

    public void clear() {
        tree.clear();
    }

    public boolean isEmpty() {
        return tree.getRoot().isEmpty();
    }

    public void add(final IOccurrence patternOccurance) {
        tree.add(patternOccurance);
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_ALWAYS;
    }
}

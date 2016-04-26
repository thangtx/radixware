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

package org.radixware.kernel.designer.common.dialogs.results;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.components.FilteredTreeView;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;


public abstract class ResultsTree extends FilteredTreeView {

    public static class RadixObjectItem extends FilteredTreeView.Item {

        public RadixObjectItem(RadixObject radixObject) {
            super(radixObject);

            final String qualifiedName = radixObject.getQualifiedName();
            final Icon icon = radixObject.getIcon().getIcon();

            setDisplayName(qualifiedName);
            setIcon(icon);
        }

        public RadixObject getRadixObject() {
            return (RadixObject) getUserObject();
        }

        @Override
        public String getToolTipText() {
            return getRadixObject().getToolTip();
        }
    }

    public ResultsTree() {
        super();
        addMouseListener(mouseListener);
    }
    //
    private final MouseListener mouseListener = new MouseAdapter() {

        private static final int DOUBLE_CLICK = 2;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == DOUBLE_CLICK) {
                goToLastSelectedObject();
            }
        }
    };

    public abstract void goToLastSelectedObject();

    public void findUsagesOfLastSelectedObject() {
    }

    public boolean canFindUsagesForSelectedObject() {
        return false;
    }

    public Action[] getCustomActions(){
        return null;
    }

    // do not unite onFilterChanged and setFilter, because onFilterChanged called before filter was initialized.
    protected abstract void onFilterChanged();

    protected static RadixObject getDisplayedObject(RadixObject source) {
        final Definition def = source.getDefinition();
        if (def != null) {
            return def;
        }
        return source;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return new ResultsPopupMenu(this);
    }

    public void copyToClipboard() {
        final Node[] selection = getSelectedNodes();
        if (selection == null || selection.length == 0) {
            return;
        }

        final StringBuilder sb = new StringBuilder();
        boolean added = false;
        final int len = selection.length;

        for (int i = 0; i < len; i++) {
            final Node node = selection[i];
            if (added) {
                sb.append('\n');
            }

            if (len > 1) {
                if (node.isLeaf()) {
                    sb.append("* ");
                } else {
                    if (added) {
                        sb.append("\n");
                    }
                }
            }

            final Object userObject = node.getUserObject();

            if (userObject instanceof RadixObject) {
                final RadixObject radixObject = (RadixObject) userObject;
                final String text = radixObject.getTypeTitle() + " '" + radixObject.getQualifiedName() + "'";
                sb.append(text);
            } else {
                final String text = node.getDisplayName();
                sb.append(text);
            }

            added = true;
        }

        final String text = sb.toString();
        if (text != null && !text.isEmpty()) {
            ClipboardUtils.copyToClipboard(text);
        }
    }

    public void clear() {
        getRoot().clear();
    }

    protected static boolean isSatisfiedToRoot(final RadixObject radixObject, final Collection<? extends RadixObject> rootObjects) {
        if (rootObjects == null || rootObjects.isEmpty()) {
            return true;
        }
        for (RadixObject root : rootObjects) {
            if (root.isParentOf(radixObject) || root == radixObject) {
                return true;
            }
        }
        return false;
    }
}

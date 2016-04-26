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


package org.radixware.kernel.designer.api.editors.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.api.ApiEditorManager;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


final class LinkLable extends JLabel {

    private final LinkedString string;
    private final List<ActionListener> listeners = new ArrayList<>();
    private final WeakReference<RadixObject> object;
    private RadixObject selectedObject;

    LinkLable(final RadixObject object, final LinkedString string) {
        this.string = string;
        this.object = new WeakReference<>(object);

        setText(string.toHtml());


        final JPopupMenu menu = new JPopupMenu();
        menu.add(new AbstractAction("Select in projects") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final RadixObject ref = selectedObject != null ? selectedObject : LinkLable.this.object.get();
                selectedObject = null;
                if (ref != null && ref.isInBranch()) {
                    NodesManager.selectInProjects(ref);
                }
            }
        });
        menu.add(new AbstractAction("Go to object") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final RadixObject ref = selectedObject != null ? selectedObject : LinkLable.this.object.get();
                selectedObject = null;
                if (ref != null && ref.isInBranch()) {
                    EditorsManager.getDefault().open(ref);
                }
            }
        });
        menu.add(new AbstractAction("Open in new window") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final RadixObject ref = selectedObject != null ? selectedObject : LinkLable.this.object.get();
                selectedObject = null;
                if (ref != null && ref.isInBranch()) {
                    ApiEditorManager.create().open(ref);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showMenu(e, getRef(e));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showMenu(e, getRef(e));
            }

            public boolean showMenu(MouseEvent e, final RadixObject ref) {
                if (e.isPopupTrigger()) {
                    selectedObject = ref;

                    menu.show(LinkLable.this, e.getX(), e.getY());
                    return true;
                }
                return false;
            }

            @Override
            public void mouseClicked(MouseEvent e) {       
                if (e.getButton() == MouseEvent.BUTTON1) {
                    final RadixObject ref = getRef(e);
                    if (ref != null) {
                        e.consume();
                        open(ref, e.isControlDown());
                    } else {
                        fireAction();
                    }
                }
            }

            private RadixObject getRef(MouseEvent e) {
                return string.getRef(e.getX() - getHorizontalTextPosition() - getIconTextGap(), e.getY(), LinkLable.this);
            }
        });

    }

    public void open(final RadixObject ref, final boolean newBrowser) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (newBrowser) {
                    ApiEditorManager.create().open(ref);
                } else {
                    ApiEditorManager.find(LinkLable.this).open(ref);
                }
            }
        });
    }

    private void fireAction() {
        for (final ActionListener actionListener : listeners) {
            actionListener.actionPerformed(new ActionEvent(this, 0, "click"));
        }
    }

    public void addActionListener(final ActionListener listener) {
        listeners.add(listener);
    }

    public void removeActionListener(final ActionListener listener) {
        listeners.remove(listener);
    }
}

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
package org.radixware.kernel.designer.common.editors;

import java.awt.EventQueue;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;
import org.openide.util.Mutex;
import org.openide.util.WeakListeners;
import org.openide.util.datatransfer.ClipboardEvent;
import org.openide.util.datatransfer.ClipboardListener;
import org.openide.util.datatransfer.ExClipboard;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

class EditorActions implements ClipboardListener, Runnable {

    private final RadixObjectTopComponent tc;
    private final CopyActionImpl copyActionImpl;
    private final CutActionImpl cutActionImpl;
    private final PasteActionImpl pasteActionImpl;
    private final DeleteActionImpl deleteActionImpl;

    public EditorActions(RadixObjectTopComponent tc) {
        this.tc = tc;

        ActionMap actionMap = tc.getActionMap();

        this.copyActionImpl = new CopyActionImpl();
        this.cutActionImpl = new CutActionImpl();
        this.pasteActionImpl = new PasteActionImpl();
        this.deleteActionImpl = new DeleteActionImpl();

        actionMap.put(DefaultEditorKit.copyAction, copyActionImpl);
        actionMap.put(DefaultEditorKit.cutAction, cutActionImpl);
        actionMap.put(DefaultEditorKit.pasteAction, pasteActionImpl);
        actionMap.put("delete", deleteActionImpl);

        copyActionImpl.setEnabled(false);    // required, because the internal variable 'enabled' is true in the action by default
        cutActionImpl.setEnabled(false);     //  and the following setEnabled(true) will not be applied.
        pasteActionImpl.setEnabled(false);   //    IMHO, shortcoming in swing action mapping.
        deleteActionImpl.setEnabled(false);

        Clipboard clipboard = ClipboardUtils.getClipboard();
        if (clipboard instanceof ExClipboard) {
            ExClipboard exClipboard = (ExClipboard) clipboard;
            exClipboard.addClipboardListener(
                    WeakListeners.create(ClipboardListener.class, this, exClipboard));
        }
    }

    private RadixObject getEditorRoot() {
        return tc.getEditorRoot();
    }

    public final void updateActions() {
        synchronized (this) {
            copyActionImpl.updateEnabled();
            cutActionImpl.updateEnabled();
            pasteActionImpl.updateEnabled();
            deleteActionImpl.updateEnabled();
        }
    }

    @Override
    public void clipboardChanged(ClipboardEvent ev) {
        if (!ev.isConsumed()) {
            Mutex.EVENT.readAccess(this);
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            pasteActionImpl.updateEnabled();
        }
    }

    private static abstract class RadixObjectsAction extends AbstractAction {

        protected abstract boolean calcEnabled();

        public final void updateEnabled() {
            boolean isEnabled = calcEnabled();
            setEnabled(isEnabled);
        }

        @Override
        public final boolean isEnabled() {
            updateEnabled();
            return super.isEnabled();
        }

        protected abstract void performAction();

        @Override
        public final void actionPerformed(ActionEvent e) {
            if (EventQueue.isDispatchThread()) {
                performAction();
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        performAction();
                    }
                });
            }
        }
    }

    private class CutActionImpl extends RadixObjectsAction implements Runnable {

        public CutActionImpl() {
            super();
        }

        @Override
        protected boolean calcEnabled() {
            final List<RadixObject> selectedObjects = tc.getSelectedObjects();
            for (RadixObject selectedObject : selectedObjects) {
                if (!getEditorRoot().isParentOf(selectedObject)) {
                    return false;
                }
            }
            return ClipboardUtils.canCut(selectedObjects);
        }

        @Override
        public void run() {
            final List<RadixObject> selectedObjects = tc.getSelectedObjects();
            ClipboardUtils.cutToClipboard(selectedObjects);
        }

        @Override
        public void performAction() {
            RadixMutex.writeAccess(this);
        }
    }

    private class CopyActionImpl extends RadixObjectsAction implements Runnable {

        public CopyActionImpl() {
            super();
        }

        @Override
        protected boolean calcEnabled() {
            final List<RadixObject> selectedObjects = tc.getSelectedObjects();
            return ClipboardUtils.canCopy(selectedObjects);
        }

        @Override
        public void run() {
            final List<RadixObject> selectedObjects = tc.getSelectedObjects();
            ClipboardUtils.copyToClipboard(selectedObjects);
        }

        @Override
        public void performAction() {
            RadixMutex.readAccess(this);
        }
    }

    private class PasteActionImpl extends RadixObjectsAction implements Runnable {

        public PasteActionImpl() {
            super();
        }

        public RadixObject getDestination() {
            final List<RadixObject> selectedObjects = tc.getSelectedObjects();
            if (selectedObjects.size() != 1) {
                return null;
            }

            RadixObject destination = selectedObjects.get(0);
            return destination;
        }

        @Override
        protected boolean calcEnabled() {
            final RadixObject destination = getDestination();
            if (getEditorRoot() == null) {
                return false;
            }
            if (destination==null || !getEditorRoot().isParentOf(destination)) {
                return false;
            }
            if (destination != null) {
                return ClipboardUtils.canPaste(destination, new ClipboardUtils.AskDuplicationResolver());
            } else {
                return false;
            }
        }

        @Override
        public void run() {
            final RadixObject destination = getDestination();
            if (destination != null) {
                ClipboardUtils.paste(destination, new ClipboardUtils.AskDuplicationResolver());
            }
        }

        @Override
        public void performAction() {
            RadixMutex.writeAccess(this);
        }
    }

    private class DeleteActionImpl extends RadixObjectsAction {

        public DeleteActionImpl() {
            super();
        }

        @Override
        protected boolean calcEnabled() {
            final List<RadixObject> selectedObjects = tc.getSelectedObjects();
            if (selectedObjects.isEmpty()) {
                return false;
            }
            for (RadixObject selectedObject : selectedObjects) {
                if (!getEditorRoot().isParentOf(selectedObject)) {
                    return false;
                }
                if (!selectedObject.canDelete()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void performAction() {
            final List<RadixObject> selectedObjects = tc.getSelectedObjects();
            DialogUtils.deleteObjects(selectedObjects);
        }
    }
}

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

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RemovedEvent;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.general.displaying.*;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

/**
 * TopComponent that displays RadixObjectEditor
 *
 */
public class RadixObjectTopComponent extends TopComponent {

    private final RadixObjectEditor editor;
    private final RadixObject radixObject;
    private final EditorTitlePanel editorTitlePanel;
    private final EditorActions editorActions;
    private boolean editorOpening;
    IconSupport iconSupport;

    public RadixObjectTopComponent(RadixObjectEditor editor) {
        super();
        this.editor = editor;
        this.radixObject = editor.getRadixObject();

        radixObject.getRemoveSupport().addEventListener(removeListener);

        this.iconSupport = IconSupportsManager.newInstance(radixObject);
        this.iconSupport.addEventListener(iconChangeListener);
        updateIcon();

        this.htmlNameSupport = HtmlNameSupportsManager.newInstance(radixObject);
        this.htmlNameSupport.addEventListener(htmlNameChangeListener);

        editor.getSelectionSupport().addEventListener(selectionChangeListener);
        editorActions = new EditorActions(this);

        this.setLayout(new BorderLayout());
        this.editorTitlePanel = new EditorTitlePanel();
        editorTitlePanel.open(radixObject);
        this.add(editorTitlePanel, BorderLayout.NORTH);
        this.add(editor, BorderLayout.CENTER);
    }

    RadixObject getEditorRoot() {
        return editor.getRadixObject();
    }

    private void updateIcon() {
        RadixIcon icon = iconSupport.getIcon();
        if (icon != null) {
            final Image image = icon.getImage();
            this.setIcon(image);
        } else {
            this.setIcon(null);
        }
    }
    private final IIconChangeListener iconChangeListener = new IIconChangeListener() {
        @Override
        public void onEvent(IconChangedEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateIcon();
                }
            });
        }
    };
    private volatile boolean isRadixObjectRemoved = false;
    private final RadixObject.IRemoveListener removeListener = new RadixObject.IRemoveListener() {
        @Override
        public void onEvent(RemovedEvent e) {
            isRadixObjectRemoved = true;
            if (SwingUtilities.isEventDispatchThread()) {
                RadixObjectTopComponent.this.close();
            } else {
                // called from updater
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        RadixObjectTopComponent.this.close();
                    }
                });
            }
        }
    };
    private final HtmlNameSupport htmlNameSupport;
    private final IRadixEventListener htmlNameChangeListener = new IRadixEventListener() {
        @Override
        public void onEvent(RadixEvent arg0) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateHtmlDisplayName();
                }
            });
        }
    };

    private void updateHtmlDisplayName() {
        final String newHtmlDisplayName = htmlNameSupport.getEditorHtmlName();
        RadixObjectTopComponent.this.setHtmlDisplayName(newHtmlDisplayName);
    }

    /**
     * Final - register HtmlNameSupport
     */
    @Override
    public final String getHtmlDisplayName() {
        return htmlNameSupport.getEditorHtmlName();
    }

    // for debug
    @Override
    public String getDisplayName() {
        return htmlNameSupport.getEditorDisplayName();
    }

    @Override
    protected String preferredID() {
        return this.getClass().getName();
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

    @Override
    public String getToolTipText() {
        return radixObject.getToolTip();
    }

    public boolean open(OpenInfo openInfo) {
        editorOpening = true;
        open();
        requestActive();
        editor.requestFocus();
        editor.open(openInfo);
        updateActiveNodes();
        EditorsRegistry.onOpened(radixObject);
        editorOpening = false;
        return true;
    }

    public RadixObjectEditor getEditor() {
        return editor;
    }

    private void setTopComponentVisibleByName(final String name, boolean visible) {
        final TopComponent tc = WindowManager.getDefault().findTopComponent(name);
        if (tc != null) {
            if (visible) {
                if (!tc.isOpened()) {
                    tc.open();
                }
            } else {
                if (tc.isOpened()) {
                    tc.close();
                }
            }
        }
    }

    public void update() {
        boolean closeInsteadOfUpdate = false;
        final RadixObject obj = this.editor.getRadixObject();
        if (obj != null) {
            final Branch branch = obj.getBranch();
            if (branch == null || branch.isClosing()) {
                closeInsteadOfUpdate = true;
            }
        } else {
            closeInsteadOfUpdate = true;
        }
        if (closeInsteadOfUpdate) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            });
        } else {
            updateHtmlDisplayName();
            editor.update();
            editorTitlePanel.update();
            updateActiveNodes();
        }
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();

        editor.onActivate();
        RadixMutex.readAccess(new Runnable() {
            @Override
            public void run() {
                RadixFileUtil.setSelectedInFileObject(radixObject); // hack for 'Select in Project'
                if (!editorOpening && !isRadixObjectRemoved) {
                    update();
                    editor.requestFocus();
                }
            }
        });
    }
    
    private boolean ignoreFileStatus (RadixObject selectedObject) {
        Module module = selectedObject.getModule();
        if (module instanceof AdsModule) {
            AdsModule ads = (AdsModule) module;
            return ads.isUserModule();
        }
        return false;
    }

    
    private void updateActiveNodes() {
        final List<RadixObject> selectedObjects = editor.getSelectedObjects();
        final Node[] activeNodes = new Node[selectedObjects.size()];
        for (int i = 0; i < selectedObjects.size(); i++) {
            final RadixObject selectedObject = selectedObjects.get(i);
            final Node node = NodesManager.findOrCreateNode(getEditorRoot(), selectedObject);
            if (!ignoreFileStatus(selectedObject)) {
                node.getHtmlDisplayName(); // TODO: FIXME: ugly, refactoring required, called to calc file objects (see RafixObjectNode.getHtmlDisplayName()) RADIX-4178, RADIX-3619
            }
            activeNodes[i] = node;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            setActivatedNodes(activeNodes);
            editorActions.updateActions();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setActivatedNodes(activeNodes);
                    editorActions.updateActions();
                }
            });
        }

    }
    private final IRadixEventListener selectionChangeListener = new IRadixEventListener() {
        @Override
        public void onEvent(RadixEvent e) {
            updateActiveNodes();
        }
    };

    public List<RadixObject> getSelectedObjects() {
        return editor.getSelectedObjects();
    }

    @Override
    public Action[] getActions() {
        final Action[] baseActions = super.getActions();

        final List<Action> actions = new ArrayList<>();
        actions.addAll(Arrays.asList(baseActions));
        actions.add(null);

        final Node node = NodesManager.findOrCreateNode(radixObject);
        if (node != null) {
            final Action[] nodeActions = node.getActions(true /*
             * for external purposes
             */);
            actions.addAll(Arrays.asList(nodeActions));
        }

        return actions.toArray(new Action[0]);
    }

    @Override
    protected void componentHidden() {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (editor != null) {
                    editor.onHidden();
                }
            }
        });
    }

    @Override
    protected void componentShowing() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (editor != null) {
                    editor.onShown();
                }
            }
        });

    }

    @Override
    protected void componentClosed() {
        super.componentClosed();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                editor.onClosed();

                final Branch branch = radixObject.getBranch();
                if (branch != null && !branch.isClosing()) {
                    EditorsRegistry.onClosed(radixObject);
                }

                closedSupport.fireEvent(new RadixEvent());
            }
        });
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();

        setActivatedNodes(null);
    }

    @Override
    public boolean canClose() {
        if (!super.canClose()) {
            return false;
        }
        if (!editor.canClose()) {
            return false;
        }
        return true;
    }
    private final RadixEventSource closedSupport = new RadixEventSource();

    public synchronized void removeClosedListener(IRadixEventListener listener) {
        closedSupport.removeEventListener(listener);
    }

    public synchronized void addClosedListener(IRadixEventListener listener) {
        closedSupport.addEventListener(listener);
    }

    @Override
    public Lookup getLookup() {
        Lookup editorLookup = editor.getLookup();
        if (editorLookup == null) {
            return super.getLookup();
        } else {
            Lookup lookup = super.getLookup();

            Lookup proxy = new ProxyLookup(editorLookup, lookup);
            return proxy;
        }
    }
}

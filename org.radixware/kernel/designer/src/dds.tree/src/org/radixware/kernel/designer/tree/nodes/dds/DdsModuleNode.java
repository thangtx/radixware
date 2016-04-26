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

package org.radixware.kernel.designer.tree.nodes.dds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.rights.SystemTablesBuilder;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.tree.actions.RadixdocAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsModuleCaptureAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsModuleActualizeRightSystemObjectsAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsModuleCancelCaptureAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsModuleReloadAction;

/**
 * Node of designer tree for DdsModule.
 *
 */
public class DdsModuleNode extends RadixObjectNode {

    private static class ChildFactory extends org.openide.nodes.ChildFactory<Object> {

        private final DdsModule ddsModule;
        private final IRadixEventListener modelListenerForChilds = new IRadixEventListener() {
            @Override
            public void onEvent(RadixEvent e) {
                ChildFactory.this.refresh(false);
            }
        };

        private ChildFactory(DdsModule ddsModule) {
            this.ddsModule = ddsModule;
        }

        @Override
        protected boolean createKeys(List<Object> itemsKeys) {
            itemsKeys.add(new Object());
            return true;
        }

        @Override
        protected Node[] createNodesForKey(Object key) {
            DdsModelDef model = null;
            ddsModule.getModelManager().getModelSupport().removeEventListener(modelListenerForChilds);
            try {
                model = ddsModule.getModelManager().getModel();
            } catch (final IOException error) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.messageError(error);
                    }
                });

                return new Node[0];
            } finally {
                ddsModule.getModelManager().getModelSupport().addEventListener(modelListenerForChilds);
            }

            final List<Node> nodes = new ArrayList<Node>();

            nodes.add(NodesManager.findOrCreateNode(model));
            nodes.add(NodesManager.findOrCreateNode(model.getDbAttributes()));
            nodes.add(NodesManager.findOrCreateNode(model.getBeginScript()));
            nodes.add(NodesManager.findOrCreateNode(model.getEndScript()));
            nodes.add(NodesManager.findOrCreateNode(model.getPackages()));
            nodes.add(NodesManager.findOrCreateNode(model.getTypes()));
            nodes.add(NodesManager.findOrCreateNode(model.getColumnTemplates()));
            nodes.add(NodesManager.findOrCreateNode(model.getAccessPartitionFamilies()));

            return nodes.toArray(new Node[0]);
        }
    }
    private final IRadixEventListener modelListener = new IRadixEventListener() {
        @Override
        public void onEvent(RadixEvent e) {
            if (SwingUtilities.isEventDispatchThread()) {
                updateLookupContent();
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateLookupContent();
                    }
                });
            }
        }
    };
    private final IRadixEventListener isTestListener = new IRadixEventListener() {
        @Override
        public void onEvent(RadixEvent e) {
            if (SwingUtilities.isEventDispatchThread()) {
                updateIcon();
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateIcon();
                    }
                });
            }
        }
    };

    public final DdsModule getModule() {
        return (DdsModule) getRadixObject();
    }
    private final DdsModuleCaptureAction.Cookie captureCookie;
    private final DdsModuleCancelCaptureAction.Cookie cancelCaptureCookie;
    private final DdsModuleActualizeRightSystemObjectsAction.Cookie actualizeRightSystemObjectsCoookie;

    protected DdsModuleNode(DdsModule ddsModule) {
        super(ddsModule, Children.create(new ChildFactory(ddsModule), true));

        final DdsSegment segment = ddsModule.getSegment();

        this.captureCookie = new DdsModuleCaptureAction.Cookie(ddsModule);
        this.cancelCaptureCookie = new DdsModuleCancelCaptureAction.Cookie(ddsModule);

        if (SystemTablesBuilder.DRC_MODULE_ID.equals(ddsModule.getId())) {
            this.actualizeRightSystemObjectsCoookie = new DdsModuleActualizeRightSystemObjectsAction.Cookie(ddsModule);
        } else {
            this.actualizeRightSystemObjectsCoookie = null;
        }

        ddsModule.addIsTestListener(isTestListener);

        ddsModule.getModelManager().getModelSupport().addEventListener(modelListener);
        updateLookupContent();

        final DdsModuleReloadAction.Cookie reloadCookie = new DdsModuleReloadAction.Cookie(ddsModule);
        addCookie(reloadCookie);
        
        addCookie(new RadixdocAction.RadixdocCookie(ddsModule));
    }

    private void updateLookupContent() {

        removeCookie(captureCookie);
        removeCookie(cancelCaptureCookie);
        if (actualizeRightSystemObjectsCoookie != null) {
            removeCookie(actualizeRightSystemObjectsCoookie);
        }

        final DdsModule module = getModule();
        final DdsModelManager modelManager = module.getModelManager();

        if (!module.isReadOnly() && modelManager.isInitialized()) {
            final DdsModelDef fixedModel = modelManager.getFixedModelIfLoaded();
            final DdsModelDef modifiedModel = modelManager.getModifiedModelIfLoaded();

            if (fixedModel != null && (modifiedModel == null || !modifiedModel.getModifierInfo().isOwn())) {
                addCookie(captureCookie);
            }

            if (modifiedModel != null) {
                addCookie(cancelCaptureCookie);
            }

            if (modifiedModel != null && modifiedModel.getModifierInfo().isOwn()) {
                if (actualizeRightSystemObjectsCoookie != null) {
                    addCookie(actualizeRightSystemObjectsCoookie);
                }
            }
        }
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        actions.add(SystemAction.get(DdsModuleReloadAction.class));
        actions.add(SystemAction.get(DdsModuleCaptureAction.class));
        actions.add(SystemAction.get(DdsModuleCancelCaptureAction.class));

        if (this.actualizeRightSystemObjectsCoookie != null) {
            actions.add(SystemAction.get(DdsModuleActualizeRightSystemObjectsAction.class));
        }
        
        actions.add(SystemAction.get(RadixdocAction.class));
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<DdsModule> {

        @Override
        public RadixObjectNode newInstance(DdsModule ddsModule) {
            return new DdsModuleNode(ddsModule);
        }
    }
}

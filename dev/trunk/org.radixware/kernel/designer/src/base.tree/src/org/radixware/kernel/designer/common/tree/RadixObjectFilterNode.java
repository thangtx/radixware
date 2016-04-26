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

package org.radixware.kernel.designer.common.tree;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.text.TextAction;
import org.openide.actions.EditAction;
import org.openide.cookies.EditCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeMemberEvent;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.utils.ManageableLookupListener;
import org.radixware.kernel.designer.common.general.nodes.hide.Hidable;
import org.radixware.kernel.designer.common.general.nodes.hide.HideSettings;
import org.radixware.kernel.designer.common.general.nodes.hide.Restorable;


public class RadixObjectFilterNode extends FilterNode {

    private final HidableChildren children;
    private final Action preferredAction;

    public RadixObjectFilterNode(Node original) {
        this(original, null);
    }

    public RadixObjectFilterNode(Node original, Action preferredAction) {
        this(original, new HidableChildren(original), preferredAction);
    }

    private RadixObjectFilterNode(Node original, HidableChildren children, Action preferredAction) {
        super(original, children);
        this.children = children;
        this.preferredAction = preferredAction;
        original.addNodeListener(new org.openide.nodes.NodeAdapter() {

            @Override
            public void childrenAdded(NodeMemberEvent ev) {
                updateChildren(true);
            }

            @Override
            public void childrenRemoved(NodeMemberEvent ev) {
                updateChildren(true);
            }
        });
        updateChildren(false);
        getLookup().lookup(Node.class);
    }

    @Override
    public Action getPreferredAction() {
        if (preferredAction != null) {
            return preferredAction;
        }
        return super.getPreferredAction();
    }

    private void updateChildren(boolean fullUpdate) {
        if (RadixObjectFilterNode.this.getOriginal().getChildren() == Children.LEAF) {
            if (getChildren() != Children.LEAF) {
                setChildren(Children.LEAF);
            }
        } else {
            if (getChildren() == Children.LEAF) {
                setChildren(children);
            }
        }
        if (fullUpdate) {
            children.update();
        }

    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] superActions = super.getActions(context);
        //do not show ManageHiddenChildren action in layer node and leaf nodes
        if (getChildren() != Children.LEAF && !(getLookup().lookup(RadixObject.class) instanceof Layer)) {
            Action[] actions = new Action[superActions.length + 1];
            System.arraycopy(superActions, 0, actions, 0, superActions.length);
            actions[actions.length - 1] = new ManageHiddenNodes((HidableChildren) getChildren());
            return actions;
        }
        return superActions;

    }

    static class ManageHiddenNodes extends AbstractRadixAction {

        private static final String MANAGE_HIDDEN_NODES = "manage-hidden-nodes";
        private final HidableChildren children;

        public ManageHiddenNodes(HidableChildren children) {
            super(MANAGE_HIDDEN_NODES);
            this.children = children;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            children.openHiddenNodesEditor();
        }
    }

    static final class HidableChildren extends FilterNode.Children {

        private HiddenNodesNode hiddenNodesNode;

        // used for stored references to lookupResult
        java.util.Map<Node, Lookup.Result<Hidable>> lookupResults = new WeakHashMap<>();
        private final ManageableLookupListener sharedListener = new ManageableLookupListener() {
            @Override
            protected void resultChangedImpl(LookupEvent ev) {
                update();
            }
        };

        public HidableChildren(Node or) {
            super(or);
        }

        private Node getRestoreNode() {
            if (hiddenNodesNode == null) {
                hiddenNodesNode = new HiddenNodesNode(this);
            }
            return hiddenNodesNode;
        }

        public void openHiddenNodesEditor() {
            getRestoreNode().getCookie(EditCookie.class).edit();
        }

        public void update() {
            /*
             * WARNING it is impossible to call methods getNode(true) or
             * getNodesCount(true), because it interrupt current thread.
             *
             */
            try {
                sharedListener.disable();
                Collection<Node> visibleNodes = getVisibleNodes(true);

                if (visibleNodes == null) {
                    return;
                }
                if (original.getChildren().getNodesCount() > visibleNodes.size()) {
                    visibleNodes.add(getRestoreNode());
                }
                setKeys(visibleNodes);
            } finally {
                sharedListener.restore();
            }
        }

        public List<Node> getVisibleNodes(boolean fromOriginal) {
            if (!isInitialized()) {
                this.getNodesCount();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
                return null;
            }
            List<Node> visibleNodes = new ArrayList<>();
            for (Node node : fromOriginal ? original.getChildren().getNodes() : getNodes()) {
                if (!isHidden(node)) {
                    visibleNodes.add(node);
                }
            }
            return visibleNodes;
        }

        @Override
        protected void addNotify() {
            super.addNotify();
            update();
        }

        public Node getOriginal() {
            return original;
        }

        public void restoreAll() {
            try {
                sharedListener.disable();
                for (Node node : original.getChildren().getNodes()) {
                    if (isHidden(node)) {
                        final Restorable restorable = node.getLookup().lookup(Restorable.class);
                        if (restorable != null) {
                            restorable.restore();
                        }
                    }
                }
                update();
            } finally {
                sharedListener.restore();
            }
        }

        public List<Node> getHiddenNodes(boolean fromOriginal) {
            if (!isInitialized()) {
                this.getNodesCount();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
            List<Node> hiddenNodes = new ArrayList<>();
            for (Node node : fromOriginal ? original.getChildren().getNodes() : getNodes()) {
                if (isHidden(node)) {
                    hiddenNodes.add(node);
                }
            }
            return hiddenNodes;
        }

        private boolean isHidden(Node node) {
            return node.getLookup().lookup(Restorable.class) != null;
        }

        @Override
        protected Node copyNode(Node node) {
            if (node == hiddenNodesNode) {
                return node;
            }
            Lookup.Result<Hidable> result = node.getLookup().lookupResult(Hidable.class);

            // save reference
            lookupResults.put(node, result);

            result.allInstances();
            result.addLookupListener(sharedListener);
            if (!HideSettings.hasHidableChildren(node)) {
                return new FilterNode(node);
            } else {
                return new RadixObjectFilterNode(node);
            }
        }
    }

    private static class HiddenNodesNode extends AbstractNode {

        private final HidableChildren childrenImpl;
        InstanceContent instanceContent;
        private HiddenNodesEditor editor = null;

        public HiddenNodesNode(HidableChildren childrenImpl) {
            this(childrenImpl, new InstanceContent());
        }

        public HiddenNodesNode(HidableChildren childrenImpl, InstanceContent instanceContent) {
            super(Children.LEAF, new AbstractLookup(instanceContent));
            this.childrenImpl = childrenImpl;

            instanceContent.add(new EditCookie() {
                @Override
                public void edit() {
                    if (editor == null) {
                        editor = new HiddenNodesEditor();
                    }
                    editor.setHidableChildren(HiddenNodesNode.this.childrenImpl);
                    editor.open();
                    editor.requestActive();
                }
            });

            instanceContent.add(childrenImpl);
            instanceContent.add(this);

            setDisplayName("Hidden Nodes");
        }

        @Override
        public Action[] getActions(boolean context) {
            return TextAction.augmentList(super.getActions(context), new Action[]{new RestoreAction(), SystemAction.get(EditAction.class)});
        }

        private class RestoreAction extends AbstractRadixAction {

            public RestoreAction() {
                super("restore-hidden-nodes");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                childrenImpl.restoreAll();
            }
        }

        @Override
        public Action getPreferredAction() {
            return SystemAction.get(EditAction.class);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}

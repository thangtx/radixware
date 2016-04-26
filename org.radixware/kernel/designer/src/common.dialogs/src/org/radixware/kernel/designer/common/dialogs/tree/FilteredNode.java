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

package org.radixware.kernel.designer.common.dialogs.tree;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.WidgetAdapter;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;


class FilteredNode extends FilterNode {

    private final Action defaultAction;

    public FilteredNode(final Node original, final IAcceptor<Node> acceptor, Action defaultAction) {
        super(original, new FilteredChildren(original, acceptor, defaultAction));
        this.defaultAction = defaultAction;
    }

    @Override
    public Action[] getActions(boolean context) {
        ICustomFilterActionsProvider provider = getLookup().lookup(ICustomFilterActionsProvider.class);
        if (provider != null) {
            return provider.getActions().toArray(new Action[]{});
        }
        return new Action[]{new GoToObjectAction(this), DeleteObjectAction.getInstance()};
    }

    @Override
    public Action getPreferredAction() {
        if (defaultAction != null) {
            return defaultAction;
        }
        ICustomFilterActionsProvider provider = getLookup().lookup(ICustomFilterActionsProvider.class);
        if (provider != null) {
            return provider.getPrefferedAction();
        }
        return new GoToObjectAction(this);
    }

    private static class GoToObjectAction extends AbstractRadixAction {

        private final Node node;

        public GoToObjectAction(Node node) {
            super("go-to-object");
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RadixObject radixObject = node.getLookup().lookup(RadixObject.class);
            if (radixObject != null) {
                DialogUtils.goToObject(radixObject);
            }
        }
    }

    private static class DeleteObjectAction extends NodeAction {

        private final static DeleteObjectAction ACTION = new DeleteObjectAction();

        public static Action getInstance() {
            return ACTION;
        }

        public DeleteObjectAction() {
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));
        }

        @Override
        protected boolean enable(Node[] activatedNodes) {

            if (activatedNodes.length > 0) {
                return isEnabled(activatedNodes[0].getLookup().lookup(RadixObject.class));
            }
            return false;
        }

        @Override
        protected void performAction(Node[] activatedNodes) {
            if (activatedNodes.length > 0) {
                perform(activatedNodes[0].getLookup().lookup(RadixObject.class));
            }
        }

        @Override
        public HelpCtx getHelpCtx() {
            return HelpCtx.DEFAULT_HELP;
        }

        @Override
        public String getName() {
            return "Delete Object";
        }

        @Override
        protected boolean asynchronous() {
            return false;
        }

        private void perform(RadixObject radixObject) {
            if (radixObject != null) {
                DialogUtils.deleteObject(radixObject);
            }
        }

        private boolean isEnabled(RadixObject radixObject) {

            if (radixObject instanceof AdsUIItemDef) {
                final Definition owner = radixObject.getOwnerDefinition();

                if (owner instanceof AdsUIItemDef) {
                    final WidgetAdapter ownerAdapter = new WidgetAdapter((AdsUIItemDef) owner);

                    switch (ownerAdapter.getClassName()) {
                        case AdsMetaInfo.STACKED_WIDGET_CLASS:
                        case AdsMetaInfo.TAB_WIDGET_CLASS:
                        case AdsMetaInfo.RWT_TAB_SET:
                            return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    static class FilteredChildren extends FilterNode.Children {

        private final transient IAcceptor<Node> acceptor;
        private final Action defaultAction;

        public FilteredChildren(final Node owner, final IAcceptor<Node> acceptor, Action defaultAction) {
            super(owner);
            this.acceptor = acceptor;
            this.defaultAction = defaultAction;
        }

        @Override
        protected Node copyNode(final Node node) {
            return new FilteredNode(node, acceptor, defaultAction);
        }

        @Override
        protected Node[] createNodes(final Node key) {
            final List<Node> result = new ArrayList<>();
            for (Node node : super.createNodes(key)) {

                if (acceptor.accept(key)) {
                    //((FilteredNavigatorNode) node).as
                    result.add(node);
                }
            }
            return result.toArray(new Node[result.size()]);
        }
    }

}

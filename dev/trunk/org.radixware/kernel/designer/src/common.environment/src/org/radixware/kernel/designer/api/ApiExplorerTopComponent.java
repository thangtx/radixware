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

package org.radixware.kernel.designer.api;

import org.openide.windows.TopComponent;


//@TopComponent.Description(
//        preferredID = "ApiExplorerTopComponent",
//        //iconBase="SET/PATH/TO/ICON/HERE",
//        persistenceType = TopComponent.PERSISTENCE_NEVER)
//@TopComponent.Registration(mode = "explorer", openAtStartup = false)
//@ActionID(category = "Window", id = "org.radixware.kernel.designer.api.ApiExplorerTopComponent")
//@ActionReference(path = "Menu/Window" /*, position = 333 */)
//@TopComponent.OpenActionRegistration(
//        displayName = "#ApiExplorerTopComponent_DisplayName",
//        preferredID = "ApiExplorerTopComponent")
//@Messages({
//    "ApiExplorerTopComponent_DisplayName=ApiExplorer"
//})
public class ApiExplorerTopComponent extends TopComponent {

//    private static class FilteredTree extends FilteredBeanTreeView {
//
//        public FilteredTree(IFilterFactory filterFactory, Action defaultAction) {
//            super(filterFactory, defaultAction);
//        }
//
//        private void expandAll() {
//            Node root = manager.getRootContext();
//            if (root != null && !root.isLeaf()) {
//
//                Stack<Node> stack = new Stack<>();
//                stack.push(root);
//
//                while (!stack.isEmpty()) {
//
//                    Node current = stack.pop();
//                    boolean expand = true;
//
//                    for (final Node node : current.getChildren().getNodes(true)) {
//                        if (!node.isLeaf()) {
//                            stack.push(node);
//                            expand = false;
//                        }
//                    }
//                    if (expand) {
//                        treeView.expandNode(current);
//                    }
//                }
//            }
//        }
//
//        @Override
//        protected void filterNodes(IAcceptor<Node> acceptor, boolean expandTree) {
//            manager.setRootContext(createNode(getRootNode(), acceptor, defaultAction));
//
//            if (expandTree) {
//                expandAll();
//            }
//        }
//
//        @Override
//        protected Node createNode(Node original, IAcceptor<Node> acceptor, Action defaultAction) {
//            return new ApiNode(original, acceptor, defaultAction);
//        }
//    }
//
//    private static class ApiFilter extends NameFilter {
//
//        public ApiFilter() {
//        }
//
//        @Override
//        public IAcceptor<Node> getNodeAcceptor() {
//            final IAcceptor<Node> nodeAcceptor = super.getNodeAcceptor();
//
//            return new IAcceptor<Node>() {
//                @Override
//                public boolean accept(Node candidate) {
//                    final RadixObject object = candidate.getLookup().lookup(RadixObject.class);
//
//                    if (object != null) {
//                        return accept(object) && nodeAcceptor.accept(candidate);
//                    }
//                    return false;
//                }
//
//                boolean accept(RadixObject object) {
//                    if (object instanceof IAccessible) {
//                        IAccessible accessible = (IAccessible) object;
//
//                        if (!accessible.getAccessFlags().isPublic()
//                                && !accessible.getAccessFlags().isProtected()) {
//                            return false;
//                        }
//                    }
//
//                    if (object instanceof Definition) {
//                        if (!((Definition) object).isPublished()) {
//                            return false;
//                        }
//                    }
//                    return true;
//                }
//            };
//        }
//
//        @Override
//        public Object getComponentPosition() {
//            return BorderLayout.PAGE_START;
//        }
//
//        @Override
//        public boolean expandTree() {
//            return !getSearchPattern().isEmpty();
//        }
//    }
//
//    private static final class ApiNode extends FilteredNode {
//
//        private static final class ApiNodeChildrenFactory extends ChildrenFactory {
//
//            @Override
//            public org.openide.nodes.Children createChildren(Node original, IAcceptor<Node> acceptor, Action defaultAction) {
////                if (original.getChildren().getNodesCount(true) == 0) {
////                    return FilteredChildren.LEAF;
////                }
//                return new ApiNodeChildren(original, acceptor, defaultAction);
//            }
//        }
//
//        private static final class ApiNodeChildren extends FilteredChildren {
//
//            public ApiNodeChildren(Node owner, IAcceptor<Node> acceptor, Action defaultAction) {
//                super(owner, acceptor, defaultAction);
//            }
//
//            @Override
//            protected Node copyNode(Node original, IAcceptor<Node> acceptor, Action defaultAction) {
//                return new ApiNode(original, acceptor, defaultAction);
//            }
//        }
//
//        private static class GoToApiAction extends AbstractRadixAction {
//
//            private final Node node;
//
//            public GoToApiAction(Node node) {
//                super("view-api");
//                this.node = node;
//            }
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                RadixObject radixObject = node.getLookup().lookup(RadixObject.class);
//                if (radixObject != null) {
//                    ApiEditorManager.getDefault().open(radixObject);
//                }
//            }
//        }
//
//        private static final ApiNodeChildrenFactory FACTORY = new ApiNodeChildrenFactory();
//
//        public ApiNode(Node original, IAcceptor<Node> acceptor, Action defaultAction) {
//            super(FACTORY, original, acceptor, defaultAction);
//        }
//
//        @Override
//        public Action[] getActions(boolean context) {
//            return new Action[]{new GoToApiAction(this)};
//        }
//
//        @Override
//        public Action getPreferredAction() {
//            return new GoToApiAction(this);
//        }
//    }
//
//    private final FilteredBeanTreeView treeView;
//    private Branch branch;
//
//    public ApiExplorerTopComponent() {
//        this(null);
//    }
//
//    public ApiExplorerTopComponent(Lookup lookup) {
//        super(lookup);
//
//        treeView = new FilteredTree(new IFilterFactory() {
//            @Override
//            public INavigatorFilter createFilter(RadixObject radixObject) {
//                return new ApiFilter();
//            }
//        }, null) {
//            @Override
//            protected Node createNode(Node original, IAcceptor<Node> acceptor, Action defaultAction) {
//                return new ApiNode(original, acceptor, defaultAction);
//            }
//        };
//
//        setLayout(new BorderLayout());
//        add(treeView, BorderLayout.CENTER);
//    }
//
//    @Override
//    public String getDisplayName() {
//        return Bundle.ApiExplorerTopComponent_DisplayName();
//    }
//
//    @Override
//    protected void componentOpened() {
//        final Collection<Branch> openedBranches = RadixFileUtil.getOpenedBranches();
//        this.branch = openedBranches.iterator().next();
//
//        updateTree();
//    }
//
//    private void updateTree() {
//        treeView.setRootNode(NodesManager.findOrCreateNode(branch));
//    }
}

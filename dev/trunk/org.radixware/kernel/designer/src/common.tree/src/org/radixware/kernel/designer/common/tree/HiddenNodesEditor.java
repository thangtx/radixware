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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.NodeAdapter;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.PropertySupport;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.utils.ManageableLookupListener;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.EditorTitlePanel;
import org.radixware.kernel.designer.common.general.editors.statistics.spi.IRadixObjectUsageCounter;
import org.radixware.kernel.designer.common.general.nodes.hide.Hidable;
import org.radixware.kernel.designer.common.general.nodes.hide.Restorable;
import org.radixware.kernel.designer.common.tree.actions.HideUnmodifiedModulesAction;


public class HiddenNodesEditor extends TopComponent {

    private final ListView hiddenListView = new ListView();
    private final ExplorerPanel hiddenPanel = new ExplorerPanel();
    private ListRootNode hiddenRootNode = new ListRootNode();
    private ListRootNode visibleRootNode = new ListRootNode();
    private final ExplorerPanel visiblePanel = new ExplorerPanel();
    private final ListView visibleListView = new ListView();
//    private final TreeTableView visibleTableView = new TreeTableView();
    private final JPanel buttonsPanel = new JPanel();
    private final JButton btHide = new JButton(RadixWareIcons.ARROW.LEFT.getIcon());
    private final JButton btRestore = new JButton(RadixWareIcons.ARROW.RIGHT.getIcon());
    private final JButton btHideUnmodified = new JButton("Hide Unmodified");
    private final JButton btRestoreUnmodified = new JButton("Restore Unmodified");
    private final ChangeListener unmodifiedListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (children != null) {
                updateLists();
            }
            
        }
    };
    private final JButton btMarkUnused = new JButton("Mark Unused");
    private final JButton btResetStatistics = new JButton("Reset Statistics");
    JPanel statisticsPanel = new JPanel();
    private RadixObjectFilterNode.HidableChildren children;
    private final Set<Lookup.Result<Hidable>> lookupResults = new HashSet<Lookup.Result<Hidable>>();
//    private final JLabel lbHeader = new JLabel("");
//    private final JPanel headerPanel = new JPanel(new BorderLayout());
    private final EditorTitlePanel editorTitlePanel = new EditorTitlePanel();
    private final ManageableLookupListener sharedListener = new ManageableLookupListener() {

        @Override
        protected void resultChangedImpl(LookupEvent ev) {
            updateLists();
        }
    };
    private final HideUnmodifiedChildrenAction hideUnmodifiedChildrenAction;
    private final RestoreUnmodifiedChildrenAction restoreUnmodifiedChildrenAction;
    /**
     * Creates new form HiddenNodesEditor
     */
    public HiddenNodesEditor() {
        initComponents();
        setName("Hidden Nodes");
        setLayout(new MigLayout("fill", "[fill][grow 0][fill]", "[fill]"));

        add(editorTitlePanel, "dock north");

        hiddenPanel.setSize(visiblePanel.getSize());

        add(hiddenPanel, "gaptop 8, gapleft 8, gapbottom 8");
        add(buttonsPanel, "gaptop push, gapbottom push");
        add(visiblePanel, "gaptop 8, gapright 8, gapbottom 8");

        hiddenPanel.setLayout(new BorderLayout(0, 4));
        hiddenPanel.getExplorerManager().setRootContext(hiddenRootNode);
        hiddenPanel.add(hiddenListView, BorderLayout.CENTER);
        hiddenPanel.add(new JLabel(NbBundle.getMessage(HiddenNodesEditor.class, "lbl-hidden-nodes")), BorderLayout.NORTH);
        hiddenListView.setBorder(BorderFactory.createEtchedBorder());
        hideUnmodifiedChildrenAction = SystemAction.get(HideUnmodifiedChildrenAction.class);
        btHideUnmodified.setAction(hideUnmodifiedChildrenAction);
        restoreUnmodifiedChildrenAction = SystemAction.get(RestoreUnmodifiedChildrenAction.class);
        btRestoreUnmodified.setAction(restoreUnmodifiedChildrenAction);
        statisticsPanel.setLayout(new MigLayout("align right"));
        statisticsPanel.add(btHideUnmodified);
        statisticsPanel.add(btRestoreUnmodified);
        statisticsPanel.add(btMarkUnused);
        statisticsPanel.add(btResetStatistics);

        visiblePanel.setLayout(new BorderLayout(0, 4));
        visiblePanel.getExplorerManager().setRootContext(visibleRootNode);
        visiblePanel.add(visibleListView, BorderLayout.CENTER);
        visiblePanel.add(statisticsPanel, BorderLayout.SOUTH);
//        visiblePanel.add(visibleTableView, BorderLayout.CENTER);
        visiblePanel.add(new JLabel(NbBundle.getMessage(HiddenNodesEditor.class, "lbl-visible-nodes")), BorderLayout.NORTH);
        visibleListView.setBorder(BorderFactory.createEtchedBorder());

//        visibleTableView.setProperties(new Property[]{new PropertySupport<Object>("UsageCount", Object.class, "Usage Count", "Number of times when user opened editor of this object or it's child", false, false) {
//
//                @Override
//                public Object getValue() throws IllegalAccessException, InvocationTargetException {
//                    throw new UnsupportedOperationException("Not supported yet.");
//                }
//
//                @Override
//                public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//                    throw new UnsupportedOperationException("Not supported yet.");
//                }
//            }});

        visiblePanel.setPreferredSize(hiddenPanel.getPreferredSize());
        visiblePanel.setSize(hiddenPanel.getSize());

        buttonsPanel.setLayout(new MigLayout());
        buttonsPanel.add(btRestore, "wrap");
        buttonsPanel.add(btHide);

        btHide.setToolTipText(NbBundle.getMessage(HiddenNodesEditor.class, "ttp-hide-selected"));
        btHide.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                hideSelectedNodes();
            }
        });

        btRestore.setToolTipText(NbBundle.getMessage(HiddenNodesEditor.class, "ttp-restore-selected"));
        btRestore.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                restoreSelectedNodes();
            }
        });

        btMarkUnused.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int totalCount = 0;

                Map<Node, Integer> usageMap = new HashMap<Node, Integer>();

                for (Node node : visiblePanel.getExplorerManager().getExploredContext().getChildren().getNodes()) {
                    int cnt = Lookup.getDefault().lookup(IRadixObjectUsageCounter.class).getUsageCount(node.getLookup().lookup(RadixObject.class));
                    usageMap.put(node, cnt);
                    totalCount += cnt;
                }

                for (Node node : hiddenPanel.getExplorerManager().getExploredContext().getChildren().getNodes()) {
                    int cnt = Lookup.getDefault().lookup(IRadixObjectUsageCounter.class).getUsageCount(node.getLookup().lookup(RadixObject.class));
                    totalCount += cnt;
                }

                long fivePercent = Math.round(Math.floor(totalCount * 0.05));

                List<Node> selectedNodes = new ArrayList<Node>();
                for (Entry<Node, Integer> entry : usageMap.entrySet()) {
                    if (entry.getValue() < fivePercent) {
                        selectedNodes.add(entry.getKey());
                    }
                }
                if (selectedNodes.isEmpty()) {
                    DialogUtils.messageInformation(NbBundle.getMessage(HiddenNodesEditor.class, "not-enough-statistics"));
                } else {
                    try {
                        visiblePanel.getExplorerManager().setSelectedNodes(selectedNodes.toArray(new Node[]{}));
                    } catch (PropertyVetoException ex) {
                        DialogUtils.messageError(ex);
                    }
                }
            }
        });

        btResetStatistics.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IRadixObjectUsageCounter counter = Lookup.getDefault().lookup(IRadixObjectUsageCounter.class);
                for (Node node : visiblePanel.getExplorerManager().getRootContext().getChildren().getNodes()) {
                    counter.resetData(node.getLookup().lookup(RadixObject.class));
                }
                for (Node node : hiddenPanel.getExplorerManager().getRootContext().getChildren().getNodes()) {
                    counter.resetData(node.getLookup().lookup(RadixObject.class));
                }
            }
        });
        
    }

    private void hideSelectedNodes() {
        try {
            sharedListener.disable();
            for (Node node : visiblePanel.getExplorerManager().getSelectedNodes()) {
                Hidable hidable = node.getLookup().lookup(Hidable.class);
                if (hidable != null) {
                    hidable.hide();
                } else {
                    throw new IllegalStateException("No Hidable in " + node + " lookup");
                }
            }
            updateLists();
        } finally {
            sharedListener.restore();
        }
    }

    private void restoreSelectedNodes() {
        try {
            sharedListener.disable();
            for (Node node : hiddenPanel.getExplorerManager().getSelectedNodes()) {
                Restorable restorable = node.getLookup().lookup(Restorable.class);
                if (restorable != null) {
                    restorable.restore();
                } else {
                    throw new IllegalStateException("No Restorable in " + node + " lookup");
                }
            }
            updateLists();
        } finally {
            sharedListener.restore();
        }
    }

    void setHidableChildren(RadixObjectFilterNode.HidableChildren children) {
        this.children = children;

        editorTitlePanel.open(children.getOriginal().getLookup().lookup(RadixObject.class));
        setName("Hidden Children of \"" + children.getOriginal().getLookup().lookup(RadixObject.class).getQualifiedName() + "\"");

        updateLists();

        for (Node node : children.getOriginal().getChildren().getNodes()) {
            Lookup.Result<Hidable> result = node.getLookup().lookupResult(Hidable.class);
            lookupResults.add(result);
            result.addLookupListener(sharedListener);
        }

        hideUnmodifiedChildrenAction.setChildren(children);
        restoreUnmodifiedChildrenAction.setChildren(children);
        children.getOriginal().addNodeListener(new NodeAdapter() {

            @Override
            public void childrenAdded(NodeMemberEvent ev) {
                updateLists();
            }

            @Override
            public void childrenRemoved(NodeMemberEvent ev) {
                updateLists();
            }

            @Override
            public void nodeDestroyed(NodeEvent ev) {
                close();
            }
        });
    }

    private void updateLists() {
        hiddenRootNode.setNodes(children.getHiddenNodes(true));
        visibleRootNode.setNodes(children.getVisibleNodes(true));
    }

    private static final class ExplorerPanel extends JPanel implements ExplorerManager.Provider {

        private final transient ExplorerManager explorerManager = new ExplorerManager();

        public ExplorerPanel() {
        }

        @Override
        public ExplorerManager getExplorerManager() {
            return explorerManager;
        }
    }

    private static class ListRootNode extends AbstractNode {

        private final ListRootChildren listRootChildren;

        public ListRootNode() {
            this(new ListRootChildren());
        }

        private ListRootNode(ListRootChildren children) {
            super(children);
            this.listRootChildren = children;
        }

        public void setNodes(Collection<Node> nodes) {
            listRootChildren.setNodes(nodes);
        }

        private static class ListRootChildren extends Children.Keys<Node> {

            public void setNodes(Collection<Node> nodes) {
                setKeys(nodes);
            }

            @Override
            protected Node[] createNodes(Node key) {
                return new Node[]{new ListFilterNode(key)};
            }
        }
    }

    private static class ListFilterNode extends FilterNode {

        private final PropertySet propertySet;

        public ListFilterNode(Node original) {
            super(original, Children.LEAF);
            propertySet = new PropertySet("UsageCount", "UsageCount", "UsageCount") {

                private Property<Integer> usageProperty = new PropertySupport<Integer>("UsageCount", Integer.class, "Usage Count", "Usage Count", true, false) {

                    @Override
                    public Integer getValue() throws IllegalAccessException, InvocationTargetException {
                        return Lookup.getDefault().lookup(IRadixObjectUsageCounter.class).getUsageCount(getLookup().lookup(RadixObject.class));
                    }

                    @Override
                    public void setValue(Integer val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        throw new IllegalAccessException();
                    }
                };

                @Override
                public Property<?>[] getProperties() {
                    return new Property[]{usageProperty};
                }
            };

        }

        @Override
        public PropertySet[] getPropertySets() {
            return new PropertySet[]{propertySet};
        }

        @Override
        public Action getPreferredAction() {
            if (getLookup().lookup(Restorable.class) != null) {
                return new RestoreAction(this);
            }
            if (getLookup().lookup(Hidable.class) != null) {
                return new HideAction(this);
            }
            return null;
        }

        @Override
        public Action[] getActions(boolean context) {
            if (getLookup().lookup(Restorable.class) != null) {
                return new Action[]{new RestoreAction(this)};
            }
            if (getLookup().lookup(Hidable.class) != null) {
                return new Action[]{new HideAction(this)};
            }
            return new Action[]{};
        }

        private class RestoreAction extends AbstractRadixAction {

            private final Node node;
            private static final String RESTORE_ACTION = "restore-action";

            public RestoreAction(Node node) {
                super(RESTORE_ACTION);
                this.node = node;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Restorable restorable = node.getLookup().lookup(Restorable.class);
                if (restorable != null) {
                    restorable.restore();
                } else {
                    throw new IllegalStateException("No restorable in node lookup");
                }
            }
        }

        private class HideAction extends AbstractRadixAction {

            private final Node node;
            private static final String HIDE_ACTION = "hide-action";

            public HideAction(Node node) {
                super(HIDE_ACTION);
                this.node = node;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Hidable hidable = node.getLookup().lookup(Hidable.class);
                if (hidable != null) {
                    hidable.hide(false);
                } else {
                    throw new IllegalStateException("No restorable in node lookup");
                }
            }
        }
    }

    @Override
    protected void componentClosed() {
        super.componentClosed();
        if (restoreUnmodifiedChildrenAction != null) {
            restoreUnmodifiedChildrenAction.removeChangeListener(unmodifiedListener);
        }
        if (hideUnmodifiedChildrenAction != null) {
            hideUnmodifiedChildrenAction.removeChangeListener(unmodifiedListener);
        }
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();
        if (restoreUnmodifiedChildrenAction != null) {
            restoreUnmodifiedChildrenAction.addChangeListener(unmodifiedListener);
        }
        if (hideUnmodifiedChildrenAction != null) {
            hideUnmodifiedChildrenAction.addChangeListener(unmodifiedListener);
        }
    }
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

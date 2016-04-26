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

package org.radixware.kernel.designer.common.dialogs.dependency;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.TreeViewPanel;
import org.radixware.kernel.designer.common.dialogs.components.UtilTabbedTopComponent;


final class ModuleDependenciesPanel extends UtilTabbedTopComponent.UtilTabPanel<Module> {

    private boolean right = true;
    private boolean list = false;
    private TreeViewPanel treeViewPanel;
    private ModuleDependencyGraph dependencyGraph;
    private FilterPanel filterPanel;

    public ModuleDependenciesPanel() {
        this(new TreeViewPanel());
    }

    private ModuleDependenciesPanel(TreeViewPanel treeViewPanel) {
        super(treeViewPanel);
        this.treeViewPanel = treeViewPanel;

        final JPanel contentPanel = getContentPanel();
        contentPanel.setLayout(new CardLayout());

        treeViewPanel.getTreeView().setRootVisible(false);
    }
    final ModuleDependencyGraph.IFilter filter = new ModuleDependencyGraph.IFilter() {

        @Override
        public boolean accept(OrientedGraph.Node node) {

            if (filterPanel.getPattern() == null) {
                return true;
            }
            return filterPanel.getPattern().matcher(node.getName()).matches();
        }
    };

    private List<ModuleDependencyGraph.Node> filterNodes(List<ModuleDependencyGraph.Node> nodes) {

        final List<ModuleDependencyGraph.Node> targets = new ArrayList<>();
        for (final OrientedGraph.Node node : nodes) {
            if (filter.accept(node)) {
                targets.add(node);
            }
        }
        return filterPanel.getPattern() == null ? null : targets;
    }

    private void expandWay() {
        Node[] selectedNodes = treeViewPanel.getExplorerManager().getSelectedNodes();

        if (selectedNodes != null && selectedNodes.length == 1) {
            Node current = selectedNodes[0];

            while (!current.isLeaf()) {
                treeViewPanel.getTreeView().expandNode(current);
                if (current.getChildren().getNodesCount() > 0) {
                    current = current.getChildren().getNodeAt(0);
                } else {
                 //   System.out.println("test");
                    break;
                }
            }
            treeViewPanel.getTreeView().expandNode(current);
        }
    }

    @Override
    public void update() {

        if (getObject() != null) {
            dependencyGraph = new ModuleDependencyGraph(getObject().getLayer());
            dependencyGraph.build();

            final ModuleDependencyGraph.Node start = dependencyGraph.findNode(getObject());
            final Node root;

            if (right) {
                if (list) {
                    root = new ModuleTreeNode(Children.create(
                            new AllModuleDependenciesChildFactory(start, filterNodes(start.getAllOutNodes())), false), getObject());
                } else {
                    root = new ModuleTreeNode(Children.create(
                            new ModuleDependenciesChildFactory(start, null, filterNodes(start.getAllOutNodes())), false), getObject());
                }

            } else {
                if (list) {
                    root = new ModuleTreeNode(Children.create(
                            new AllDependentModulesItemChildFactory(start, filterNodes(start.getAllInNodes())), false), getObject());
                } else {
                    root = new ModuleTreeNode(Children.create(
                            new DependentModulesChildFactory(start, null, filterNodes(start.getAllInNodes())), false), getObject());
                }
            }
            treeViewPanel.getExplorerManager().setRootContext(root);
        }
    }

    @Override
    public String getTitle() {
        if (getObject() != null) {
            return getObject().getQualifiedName();
        }
        return NbBundle.getMessage(ModuleDependenciesPanel.class, "ModuleDependenciesPanel.title");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public JToolBar createToolBar() {
        final JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);

        final JButton update = new JButton(RadixWareIcons.ARROW.CIRCLE.getIcon(13, 13));
        update.setFocusable(false);
        update.setToolTipText(NbBundle.getMessage(ModuleDependenciesPanel.class, "refresh.tooltip.text"));
        update.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        toolBar.add(update);

        final JButton expand = new JButton(RadixWareDesignerIcon.EDIT.LINK.getIcon(13, 13));
        expand.setFocusable(false);
        expand.setToolTipText(NbBundle.getMessage(ModuleDependenciesPanel.class, "expand.tooltip.text"));
        expand.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                expandWay();
            }
        });
        toolBar.add(expand);

        final JToggleButton dependencies = new JToggleButton(RadixWareIcons.TREE.DEPENDENCIES.getIcon(13, 13));
        dependencies.setFocusable(false);
        dependencies.setToolTipText(NbBundle.getMessage(ModuleDependenciesPanel.class, "dependencies.tooltip.text"));
        dependencies.setSelected(true);
        toolBar.add(dependencies);

        final JToggleButton dependents = new JToggleButton(RadixWareIcons.TREE.DEPENDENT.getIcon(13, 13));
        dependents.setFocusable(false);
        dependents.setToolTipText(NbBundle.getMessage(ModuleDependenciesPanel.class, "dependents.tooltip.text"));
        toolBar.add(dependents);

        final JToggleButton dependentList = new JToggleButton(AdsDefinitionIcon.WIDGETS.LIST.getIcon(13, 13));
        dependentList.setFocusable(false);
        dependentList.setToolTipText(NbBundle.getMessage(ModuleDependenciesPanel.class, "dependentList.tooltip.text"));
        toolBar.add(dependentList);

        dependents.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (right) {
                    right = false;
                    update();
                }

                dependents.setSelected(true);
                dependencies.setSelected(false);
            }
        });

        dependencies.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!right) {
                    right = true;
                    update();
                }

                dependencies.setSelected(true);
                dependents.setSelected(false);
            }
        });

        dependentList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                list = dependentList.isSelected();
                update();
            }
        });

        filterPanel = new FilterPanel();
        filterPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                update();
            }
        });
        toolBar.addSeparator();
        toolBar.add(filterPanel);

        return toolBar;
    }

    static class FilterPanel extends JPanel {

        private JTextField txtFilter = new JTextField();
        private Pattern pattern = null;

        public FilterPanel() {

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            txtFilter.setColumns(20);

            add(new JLabel("Filter: "));
            add(txtFilter);

            txtFilter.setToolTipText(NbBundle.getMessage(ModuleDependenciesPanel.class, "FilterPanel.tooltip.text"));
            txtFilter.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear-filter");
            txtFilter.getActionMap().put("clear-filter", new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    txtFilter.setText("");
                }
            });
        }

        Pattern getPattern() {
            return pattern;
        }

        String getRegExpStr() {
            final String text = txtFilter.getText();
            if (!text.isEmpty()) {
                final String regExp = text.trim().replaceAll("\\*", ".*");

                return regExp + (text.charAt(text.length() - 1) == ' ' ? "" : ".*");
            }

            return null;
        }

        private void updatePattern() {
            final String regExpStr = getRegExpStr();

            try {
                pattern = regExpStr == null ? null : Pattern.compile(regExpStr, Pattern.CASE_INSENSITIVE);
            } catch (PatternSyntaxException exception) {
                pattern = Pattern.compile("");
            }
        }

        void addChangeListener(final ChangeListener listener) {
            txtFilter.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updatePattern();
                    listener.stateChanged(new ChangeEvent(txtFilter));
                }
            });
        }
    }

}

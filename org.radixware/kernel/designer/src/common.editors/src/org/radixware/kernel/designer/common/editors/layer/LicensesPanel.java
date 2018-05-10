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
package org.radixware.kernel.designer.common.editors.layer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringEscapeUtils;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.jml.JmlTagCheckLicense;
import org.radixware.kernel.common.jml.JmlTagReadLicense;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Layer.License;
import org.radixware.kernel.common.repository.LayerUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Pair;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.radixdoc.RadixdocSelectModulesPanel;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.FindInSources;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.IFindInSourcesCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.IFinder;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.IOccurrence;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.ScmlLocation;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.TagTextFactory;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.editors.DefaultTagTextFactory;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class LicensesPanel extends JPanel {

    private final JTree tree;
    private boolean isReadOnly = false;

    public LicensesPanel() {
        this(null, null);
    }

    public LicensesPanel(final ActionListener doubleClickListener, final ActionListener selectListener) {
        this(doubleClickListener, selectListener, false);
    }

    private LicensesPanel(final ActionListener doubleClickListener, final ActionListener selectListener, final boolean selectOnly) {
        final JScrollPane pane = new JScrollPane();
        setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);
        tree = new JTree();
        pane.setViewportView(tree);
        tree.setToggleClickCount(0);

        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                if (value instanceof DefaultMutableTreeNode && ((DefaultMutableTreeNode) value).getUserObject() instanceof License) {
                    final License license = (License) ((DefaultMutableTreeNode) value).getUserObject();
                    final StringBuilder sb = new StringBuilder();
                    if (license.getDependencies() != null && license.getDependencies().size() > 0) {
                        sb.append("   [depends on ");
                        sb.append(license.getDependencies().get(0));
                        final int listedCount = 1;
                        if (license.getDependencies().size() > listedCount) {
                            sb.append(" and ");
                            sb.append(license.getDependencies().size() - listedCount);
                            sb.append(" more");
                        }
                        sb.append("]");
                    }
                    String newVal = "<html>" + StringEscapeUtils.escapeHtml(license.getOwnName()) + "<b>" + StringEscapeUtils.escapeHtml(sb.toString()) + "</b></html>";
                    setText(newVal);

                    setIcon(RadixWareIcons.LICENSES.LICENSE.getIcon());
                }
                return this;
            }
        });
        final JButton removeButton = new JButton("Remove", RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        removeButton.setToolTipText("Remove Selected License");
        final ActionListener removeActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final TreePath selPath = tree.getSelectionPath();
                if (selPath != null && selPath.getPathCount() > 2) {
                    if (DialogUtils.messageConfirmation("Do you want to remove '" + ((License) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject()).getOwnName() + "'?")) {
                        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                        model.removeNodeFromParent((DefaultMutableTreeNode) selPath.getLastPathComponent());
                    }
                }
            }
        };
        removeButton.addActionListener(removeActionListener);
        removeButton.setHorizontalAlignment(SwingConstants.LEFT);

        final JButton addButton = new JButton("Add", RadixWareDesignerIcon.CREATE.ADD.getIcon());
        addButton.setToolTipText("Add Nested License");
        final ActionListener addNestedActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final TreePath selPath = tree.getSelectionPath();
                if (selPath != null) {
                    final String newLicenseName = DialogUtils.inputBox("License name");
                    if (newLicenseName != null && !newLicenseName.isEmpty()) {
                        final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                        final License parentLicense = (License) parentNode.getUserObject();
                        ((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode(new License(newLicenseName, parentLicense.getFullName(), null, null, null, parentLicense.getLayer())), parentNode, parentNode.getChildCount());
                        tree.expandPath(selPath);
                    }

                }
            }
        };
        addButton.addActionListener(addNestedActionListener);
        addButton.setHorizontalAlignment(SwingConstants.LEFT);

        final JButton editButton = new JButton("Dependencies", RadixWareDesignerIcon.EDIT.EDIT.getIcon());
        editButton.setToolTipText("Edit License Dependencies");
        final ActionListener showDepActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final License selectedLicense = getSelectedLicense();
                if (selectedLicense != null) {
                    final License editedLicense = runDependenciesEditor(selectedLicense, isReadOnly, LicensesPanel.this);
                    if (editedLicense != null) {
                        ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).setUserObject(editedLicense);
                        tree.getModel().valueForPathChanged(tree.getSelectionPath(), editedLicense);
                    }
                }
            }
        };
        editButton.addActionListener(showDepActionListener);
        editButton.setHorizontalAlignment(SwingConstants.LEFT);

        final JButton findUsagesButton = new JButton("Find Usages", RadixWareDesignerIcon.TREE.DEPENDENCIES.getIcon());
        findUsagesButton.setToolTipText("Find Usages of the License");
        final ActionListener findUsagesActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final License selectedLicensee = getSelectedLicense();
                if (selectedLicensee != null) {
                    findUsages(selectedLicensee);
                }
            }
        };
        findUsagesButton.addActionListener(findUsagesActionListener);
        findUsagesButton.setHorizontalAlignment(SwingConstants.LEFT);

        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                final TreePath selPath = tree.getSelectionPath();

                boolean canAdd = false;

                if (selPath != null) {
                    final Layer layer = getLayerForNode((DefaultMutableTreeNode) selPath.getLastPathComponent());
                    canAdd = layer != null && !layer.isReadOnly();
                }

                findUsagesButton.setEnabled(selPath != null);
                addButton.setEnabled(!isReadOnly && canAdd);
                final boolean isUserLicense = canAdd && selPath.getPathCount() > 2;
                removeButton.setEnabled(!isReadOnly && isUserLicense);
                editButton.setEnabled(isUserLicense);

                if (selectListener != null) {
                    selectListener.actionPerformed(new SelectLicenseEvent(tree, (getLicense((DefaultMutableTreeNode) selPath.getLastPathComponent()))));
                }
            }
        });

        final MouseListener ml = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1 && e.getClickCount() == 2) {
                    if (doubleClickListener != null) {
                        doubleClickListener.actionPerformed(new ActionEvent(tree, -1, getLicenseFullname(selPath)));
                    } else {
                        if (tree.isExpanded(selPath)) {
                            tree.collapsePath(selPath);
                        } else {
                            tree.expandPath(selPath);
                        }
                    }
                    e.consume();
                }
                if (!selectOnly && selRow != -1 && e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e)) {
                    tree.setSelectionRow(selRow);

                    final JPopupMenu popupMenu = new JPopupMenu();

                    final JMenuItem addItem = new JMenuItem("Add Nested License");
                    addItem.addActionListener(addNestedActionListener);
                    addItem.setEnabled(addButton.isEnabled());
                    popupMenu.add(addItem);

                    final JMenuItem removeItem = new JMenuItem("Remove License");
                    removeItem.addActionListener(removeActionListener);
                    removeItem.setEnabled(removeButton.isEnabled());
                    popupMenu.add(removeItem);

                    final JMenuItem editItem = new JMenuItem("Edit Dependencies");
                    editItem.addActionListener(showDepActionListener);
                    editItem.setEnabled(editButton.isEnabled());
                    popupMenu.add(editItem);

                    final JMenuItem findUsagesItem = new JMenuItem("Find Usages");
                    findUsagesItem.addActionListener(findUsagesActionListener);
                    findUsagesItem.setEnabled(findUsagesButton.isEnabled());
                    popupMenu.add(findUsagesItem);

                    popupMenu.show(tree, e.getX(), e.getY());
                }
            }
        };
        tree.addMouseListener(ml);

        JPanel buttonPanel = new JPanel(new MigLayout());
        buttonPanel.add(addButton, "growx, wrap");
        buttonPanel.add(removeButton, "growx, wrap");
        buttonPanel.add(editButton, "growx, wrap");
        buttonPanel.add(findUsagesButton, "growx, wrap");

        if (!selectOnly) {
            add(buttonPanel, BorderLayout.EAST);
        }

        removeButton.setEnabled(false);
        addButton.setEnabled(false);
        editButton.setEnabled(false);
        findUsagesButton.setEnabled(false);

        tree.setRootVisible(false);
    }

    private void findUsages(final License license) {
        final IFindInSourcesCfg cfg = new IFindInSourcesCfg() {
            private final IFinder finder = new LicenseUsagesFinder(license);
            private final IAcceptor<RadixObject> acceptor = new IAcceptor<RadixObject>() {
                @Override
                public boolean accept(RadixObject candidate) {
                    return finder.accept(candidate);
                }
            };

            @Override
            public IFinder getFinder(RadixObject radixObject) {
                return finder;
            }

            @Override
            public List<RadixObject> getRoots() {
                return Collections.<RadixObject>singletonList(license.getLayer().getBranch());
            }

            @Override
            public IAcceptor<RadixObject> getAcceptor() {
                return acceptor;
            }
        };
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                FindInSources.find(cfg);
            }
        });

    }

    private Layer getLayerForNode(final DefaultMutableTreeNode node) {
        if (node != null && node.getUserObject() instanceof License) {
            return ((License) node.getUserObject()).getLayer();
        }
        return null;
    }

    public void open(License rootLicense, final boolean readOnly) {
        open(Collections.singletonList(rootLicense), readOnly);
    }

    private void open(List<License> rootLicenses, final boolean readOnly) {
        if (rootLicenses == null) {
            return;
        }
        tree.setModel(buildLicenseTreeModel(rootLicenses));
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            tree.expandPath(new TreePath(((DefaultMutableTreeNode) rootNode.getChildAt(i)).getPath()));
        }
        isReadOnly = readOnly;
    }

    public License getSelectedLicense() {
        final TreePath selPath = tree.getSelectionPath();
        if (selPath != null && selPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
            final Object selectedUserObject = ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();
            if (selectedUserObject instanceof License) {
                return (License) selectedUserObject;
            }
        }
        return null;
    }

    private String getLicenseFullname(final TreePath path) {
        if (path == null) {
            return null;
        }
        return ((License) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject()).getFullName();

    }

    public void apply() {
        if (!isReadOnly) {
            final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
            for (int i = 0; i < rootNode.getChildCount(); i++) {
                final DefaultMutableTreeNode layerNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
                final License layerLicense = getLicense(layerNode);
                if (!layerLicense.getLayer().isReadOnly()) {
                    layerLicense.getLayer().setLicenses(layerLicense);
                }
            }
        }
    }

    License getLicense(DefaultMutableTreeNode node) {
        final List<License> childLicenses = new ArrayList<>();
        for (int i = 0; i < node.getChildCount(); i++) {
            childLicenses.add(getLicense((DefaultMutableTreeNode) node.getChildAt(i)));
        }
        return new License(((License) node.getUserObject()).getOwnName(), ((License) node.getUserObject()).getParentFullName(), childLicenses, ((License) node.getUserObject()).getDependencies(), ((License) node.getUserObject()).getRequiredModules(), ((License) node.getUserObject()).getLayer());
    }

    JTree getLicenseTree() {
        return tree;
    }

    private TreeModel buildLicenseTreeModel(final List<License> licenses) {
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        for (License license : licenses) {
            addChild(root, license);

        }
        return new DefaultTreeModel(root);
    }

    private void addChild(DefaultMutableTreeNode parentNode, License license) {
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(license);
        if (license.getChildren() != null) {
            for (License childLicense : license.getChildren()) {
                addChild(node, childLicense);
            }
        }
        parentNode.add(node);

    }

    private License runDependenciesEditor(final License license, final boolean readOnly, final LicensesPanel parent) {
        DependenciesPanel depPanel = new DependenciesPanel(license, readOnly, parent);
        final ModalDisplayer md = new ModalDisplayer(depPanel, "Edit License");
        if (md.showModal()) {
            return new License(license.getOwnName(), license.getParentFullName(), license.getChildren(), depPanel.getDependencies(), depPanel.getRequiredModules(), license.getLayer());
        }
        return null;
    }

    public static License selectLicense(final List<Layer> layers, boolean readOnly) {
        if (layers == null || layers.isEmpty()) {
            return null;
        }
        final List<License> licenseRoots = new ArrayList<>();
        for (Layer layer : layers) {
            licenseRoots.add(layer.getLicenses());
        }
        return selectLicense(licenseRoots, readOnly, false, null);
    }

    static License selectLicense(final List<License> licenseRoots, boolean readOnly, boolean selectOnly, final IAcceptor<License> selectableAcceptor) {
        final JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new MigLayout("fill", "[grow]", "[grow]"));
        final ModalDisplayer md = new ModalDisplayer(contentPanel, "Select License");
        md.getDialogDescriptor().setValid(false);
        final ActionListener doubleClickListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (md.getDialogDescriptor().isValid()) {
                    md.close(true);
                }
            }
        };
        final ActionListener selectListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectLicenseEvent selectEvent = (SelectLicenseEvent) e;
                md.getDialogDescriptor().setValid(selectEvent.getLicense() != null && selectableAcceptor == null ? true : selectableAcceptor.accept(selectEvent.getLicense()));
            }
        };
        final LicensesPanel licensesPanel = new LicensesPanel(doubleClickListener, selectListener, selectOnly);
        licensesPanel.open(licenseRoots, readOnly);
        contentPanel.add(licensesPanel, "grow");
        if (md.showModal()) {
            licensesPanel.apply();
            return licensesPanel.getSelectedLicense();
        } else {
            return null;
        }
    }

    private static class LicenseUsagesFinder implements IFinder {

        private final License license;

        public LicenseUsagesFinder(final Layer.License license) {
            this.license = license;
        }

        @Override
        public boolean accept(RadixObject radixObject) {
            return radixObject instanceof Scml;
        }

        @Override
        public List<IOccurrence> list(final RadixObject radixObject) {
            if (radixObject == null) {
                return Collections.emptyList();
            }

            final Scml scml = (Scml) radixObject;

            final List<IOccurrence> occurences = new ArrayList<>();
            int scmlOffset = 0;
            int line = 1;
            int column = 1;
            final TagTextFactory tagTextFactory = new DefaultTagTextFactory();
            for (Scml.Item item : scml.getItems()) {
                if (item instanceof Scml.Tag) {
                    final String tagText = tagTextFactory.getText((Scml.Tag) item);
                    if (item instanceof JmlTagReadLicense || item instanceof JmlTagCheckLicense) {
                        boolean matched = false;
                        if (item instanceof JmlTagReadLicense) {
                            if (license.getFullName().equals(((JmlTagReadLicense) item).getLicense())) {
                                matched = true;
                            }
                        }
                        if (item instanceof JmlTagCheckLicense) {
                            if (license.getFullName().equals(((JmlTagCheckLicense) item).getLicense())) {
                                matched = true;
                            }
                        }
                        if (matched) {

                            final ScmlLocation scmlLocation = new ScmlLocation(scml, scmlOffset, 0);
                            final String prefix = line + ":" + column + " : ";
                            occurences.add(new IOccurrence() {
                                @Override
                                public String getDisplayText() {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("<html>");
                                    sb.append(StringEscapeUtils.escapeHtml(prefix));
                                    sb.append("<b>");
                                    sb.append(StringEscapeUtils.escapeHtml(tagText));
                                    sb.append("</b>");
                                    sb.append("</html>");
                                    return sb.toString();
                                }

                                @Override
                                public RadixObject getOwnerObject() {
                                    return scml.getDefinition();
                                }

                                @Override
                                public void goToObject() {
                                    EditorsManager.getDefault().open(scmlLocation.getScml(), new OpenInfo(scmlLocation.getScml(), Lookups.fixed(scmlLocation)));
                                }
                            });
                        }
                    }
                    scmlOffset++;

                    column += tagText.length();
                } else {
                    final String text = ((Scml.Text) item).getText();
                    scmlOffset += text.length();
                    line += text.length() - text.replace("\n", "").length();
                    int lastIndexOfLr = text.lastIndexOf('\n');
                    if (lastIndexOfLr != -1) {
                        column = text.length() - lastIndexOfLr;
                    } else {
                        column += text.length();
                    }
                }
            }
            return occurences;
        }
    }

    private static class SelectLicenseEvent extends ActionEvent {

        private final License license;

        public SelectLicenseEvent(final Object source, final License license) {
            super(source, -1, license.getFullName());
            this.license = license;
        }

        public License getLicense() {
            return license;
        }
    }
}

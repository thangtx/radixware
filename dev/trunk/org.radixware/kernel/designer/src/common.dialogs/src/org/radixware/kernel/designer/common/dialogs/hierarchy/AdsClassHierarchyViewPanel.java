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

package org.radixware.kernel.designer.common.dialogs.hierarchy;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;

import org.radixware.kernel.designer.common.dialogs.tree.FilteredBeanTreeView;
import org.radixware.kernel.designer.common.dialogs.tree.NameFilterFactory;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class AdsClassHierarchyViewPanel extends JPanel {

    private final FilteredBeanTreeView treeView;
    private final JToggleButton btSubtype;
    private final JToggleButton btSupertype;
    private final JButton btClose;
    private Node subtypeRoot;
    private Node supertypeRoot;
    private final AdsClassDef classDef;
    private static RequestProcessor rp = new RequestProcessor("Hierarchy Calculator", 1);

    public AdsClassHierarchyViewPanel(final AdsClassDef classDef, boolean showCloseButton) {
        this.classDef = classDef;
        treeView = new FilteredBeanTreeView(new NameFilterFactory(), new SelectInTreeAction());

        btSubtype = new JToggleButton("Subtype");
        btSubtype.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btSubtype.setSelected(true);
                btSupertype.setSelected(false);
                treeView.setRootNode(subtypeRoot);
            }
        });

        btSupertype = new JToggleButton("Supertype");
        btSupertype.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btSupertype.setSelected(true);
                btSubtype.setSelected(false);
                treeView.setRootNode(supertypeRoot);
            }
        });

        btClose = new JButton("Close");

        setLayout(new MigLayout("fill", "", "[grow][shrink]"));

        add(treeView, "grow, span, wrap");
        add(btSubtype, "split");
        add(btSupertype, "gapright push");
        if (showCloseButton) {
            add(btClose);
        }
        calculateNodes();

    }

    public AdsClassDef getClassDef() {
        return classDef;
    }

    private class SelectInTreeAction extends AbstractRadixAction {

        public SelectInTreeAction() {
            super("go-to-object");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Node[] selectedNodes = treeView.getExplorerManager().getSelectedNodes();
            if (selectedNodes == null || selectedNodes.length == 0) {
                return;
            }
            Node node = selectedNodes[0];
            if (node == null) {
                return;
            }
            btClose.doClick();
            RadixObject radixObject = node.getLookup().lookup(RadixObject.class);
            if (radixObject != null) {
                DialogUtils.goToObject(radixObject);
            }
        }
    }

    private void calculateNodes() {
        btSubtype.setEnabled(false);
        btSupertype.setEnabled(false);

        Node nodeWait = new AbstractNode(Children.LEAF);
        nodeWait.setDisplayName("Please wait...");
        treeView.setRootNode(nodeWait);

        rp.post(new Runnable() {

            @Override
            public void run() {
                subtypeRoot = createSubtypeRoot(classDef);
                supertypeRoot = createSupertypeRoot(classDef);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        btSubtype.setEnabled(true);
                        btSupertype.setEnabled(true);
                        btSubtype.setSelected(true);
                        treeView.setRootNode(subtypeRoot);
                        revalidate();
                    }
                });
            }
        });
    }

    private Node createSubtypeRoot(AdsClassDef classDef) {
        Inheritance.ClassHierarchySupport csh = new Inheritance.ClassHierarchySupport();
        Collection<AdsClassDef> subtypes = new ArrayList<AdsClassDef>();
        subtypes.addAll(csh.findDirectSubclasses(classDef));
        subtypes.addAll(csh.findDirectImplementations(classDef));
        SubtypeNode subTypeNode = new SubtypeNode(classDef, csh, subtypes != null && !subtypes.isEmpty());

        Node node = subTypeNode;
        AdsClassDef rootClassDef = InheritanceUtilities.getSuperOrOverwrittenClass(classDef);
        while (rootClassDef != null) {
            node = new PredefinedNode(rootClassDef, node);
            rootClassDef = InheritanceUtilities.getSuperOrOverwrittenClass(rootClassDef);
        }

        return node;
    }

    private Node createSupertypeRoot(AdsClassDef classDef) {
        return new SupertypeNode(classDef, InheritanceUtilities.getSuperOrOverwrittenClass(classDef) != null || classDef.getInheritance().getInerfaceRefList(EScope.LOCAL).size() > 0);
    }

    public void showModal() {

        final DialogDescriptor dd = new DialogDescriptor(this, "Class Hierarchy");
        dd.setModal(true);
        dd.setOptions(new Object[]{});
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        btClose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btClose.removeActionListener(this);
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        dialog.pack();
        dialog.setVisible(true);
    }
}

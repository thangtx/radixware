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
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.netbeans.spi.navigator.NavigatorPanel;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.resources.RadixWareIcons;

import org.radixware.kernel.designer.common.dialogs.tree.AbstractFiter;
import org.radixware.kernel.designer.common.dialogs.tree.INavigatorFilter;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.dialogs.tree.FilteredBeanTreeView;
import org.radixware.kernel.designer.common.dialogs.tree.IFilterFactory;
import org.radixware.kernel.designer.common.dialogs.tree.NameFilter;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;
import org.radixware.kernel.designer.common.dialogs.utils.RadixNbEditorUtils;


public class RadixNavigatorPanel extends JPanel implements NavigatorPanel {

    private final FilteredBeanTreeView treeView = new FilteredBeanTreeView();

    public RadixNavigatorPanel() {
        setLayout(new BorderLayout());
        add(treeView, BorderLayout.CENTER);
    }

    @Override
    public String getDisplayName() {
        return "Navigator";
    }

    @Override
    public String getDisplayHint() {
        return "Navigator";
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
    private static final Lookup.Template<RadixObject> lookupTemplate = new Lookup.Template<RadixObject>(RadixObject.class);
    private Lookup.Result<RadixObject> curLookupResult = null;
    private final LookupListener lookupResultListener = new LookupListener() {

        @Override
        public void resultChanged(LookupEvent ev) {
            updateTree();
        }
    };

    @Override
    public void panelActivated(Lookup context) {
        curLookupResult = context.lookup(lookupTemplate);
        updateTree();
        curLookupResult.addLookupListener(lookupResultListener);
    }

    @Override
    public void panelDeactivated() {
        if (curLookupResult != null) {
            curLookupResult.removeLookupListener(lookupResultListener);
            curLookupResult = null;
        }
    }

    @Override
    public Lookup getLookup() {
        return treeView.getLookup();
    }

    private static Definition findSaveable(RadixObject def) {
        for (RadixObject cur = def; cur != null; cur = cur.getContainer()) {
            if (cur instanceof Definition && cur.isSaveable()) {
                return (Definition) cur;
            }
        }
        return null;
    }

    protected Definition getDelegateDefinition(RadixObject def) {
        return findSaveable(def); // default
    }

    private void updateTree() {
        final RadixObject rootObject = calculateRootObject();
        if (rootObject != null) {
            treeView.setRootNode(NodesManager.findOrCreateNode(rootObject));
        } else {
            treeView.setRootNode(null);
        }
    }

    private RadixObject calculateRootObject() {
        if (curLookupResult == null) { // component deactivated
            return null;
        }

        final Collection<? extends RadixObject> selectedDefs = curLookupResult.allInstances();
        if (selectedDefs.isEmpty()) {
            return null;
        }

        final RadixObject firstSelectedDef = selectedDefs.iterator().next();
        final Definition top = getDelegateDefinition(firstSelectedDef);

        return top;
    }

    private static class FilterFactoryImpl implements IFilterFactory {

        private static class ClassNavigatorFilter extends AbstractFiter {

            private final NameFilter nameFilter = new NameFilter();
            private final JPanel filterPanel = new JPanel();
            private final JToggleButton btNonPublic = new JToggleButton(RadixWareIcons.SECURITY.KEY.getIcon());
            private final JToggleButton btFields = new JToggleButton(AdsDefinitionIcon.Property.PROPERTY_INNATE.getIcon());
            private ItemListener notifyListener = new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                        notifyListeners();
                    }
                }
            };

            public ClassNavigatorFilter() {
                nameFilter.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        notifyListeners();
                    }
                });
                filterPanel.setLayout(new MigLayout("fill, ins 0", "[fill][][]", ""));
                filterPanel.add(nameFilter.getComponent(), "grow, push");

                btFields.setSelected(true);
                btFields.setToolTipText(NbBundle.getMessage(ClassNavigatorFilter.class, "bt-show-fields-tooltip"));
                btFields.addItemListener(notifyListener);

                btNonPublic.setSelected(true);
                btNonPublic.setToolTipText(NbBundle.getMessage(ClassNavigatorFilter.class, "bt-show-non-public-tooltip"));
                btNonPublic.addItemListener(notifyListener);

                RadixNbEditorUtils.processToolbarButton(btFields);
                RadixNbEditorUtils.processToolbarButton(btNonPublic);

                filterPanel.add(btNonPublic);
                filterPanel.add(btFields);
            }

            @Override
            public IAcceptor<Node> getNodeAcceptor() {
                return new IAcceptor<Node>() {

                    private final boolean showNonPublic = btNonPublic.isSelected();
                    private final boolean showFileds = btFields.isSelected();
                    private final IAcceptor<Node> nameAcceptor = nameFilter.getNodeAcceptor();

                    @Override
                    public boolean accept(final Node candidate) {
                        if (!nameAcceptor.accept(candidate)) {
                            return false;
                        }
                        final RadixObject radixObject = candidate.getLookup().lookup(RadixObject.class);
                        if (radixObject == null) {
                            return true;
                        }
                        if (radixObject instanceof AdsPropertyDef) {
                            if (showFileds) {
                                final AdsPropertyDef propDef = (AdsPropertyDef) radixObject;
                                return (propDef.getAccessFlags().isPublic() || propDef.getAccessFlags().isPublished());
                            } else {
                                return false;
                            }
                        }
                        if (radixObject instanceof AdsMethodDef) {
                            if (showNonPublic) {
                                return true;
                            } else {
                                return ((AdsMethodDef) radixObject).getAccessMode() == EAccess.PUBLIC;
                            }

                        }
                        return false;
                    }
                };
            }

            @Override
            public Component getComponent() {
                return filterPanel;
            }

            @Override
            public Object getComponentPosition() {
                return BorderLayout.NORTH;
            }
        }

        @Override
        public INavigatorFilter createFilter(RadixObject ro) {
            if (ro instanceof AdsClassDef) {
                return new ClassNavigatorFilter();
            }

            return new NameFilter();
        }
    }
}

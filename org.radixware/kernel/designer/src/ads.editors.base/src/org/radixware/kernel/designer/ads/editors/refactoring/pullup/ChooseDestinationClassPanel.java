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

/*
 * ChooseDestinationClass.java
 *
 * Created on Jan 12, 2012, 11:23:05 AM
 */
package org.radixware.kernel.designer.ads.editors.refactoring.pullup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.explorer.ExplorerManager;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.designer.ads.editors.refactoring.components.CardPanel;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreePanel.DefaultFilter;


final class ChooseDestinationClassPanel extends CardPanel {

    private static final String HIERARCHY_CARD = "Hierarchy";
    private final ClassHierarchyTreePanel hierarchyTreePanel = new ClassHierarchyTreePanel();
    private PullUpSteps.PullUpSettings setting;

    public ChooseDestinationClassPanel(PullUpSteps.PullUpSettings setting) {
        this.setting = setting;
        hierarchyTreePanel.addPropertyChangeListener(ExplorerManager.PROP_SELECTED_NODES, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(PROP_NAME_CHANGES, evt.getOldValue(), evt.getNewValue());
            }
        });

        add(hierarchyTreePanel, HIERARCHY_CARD);
    }
    boolean isOpened = false;

    public void open(AdsClassDef context, AdsClassDef selected) {
        if (context != null && !isOpened) {

            isOpened = true;

            hierarchyTreePanel.open(context, new DefaultFilter<RadixObject>() {
                @Override
                public boolean acceptNode(RadixObject node) {
                    if (node instanceof AdsClassDef) {
                        AdsDefinition def = setting.getDefinition();
                        if (def instanceof AdsPropertyDef) {
                            AdsPropertyDef prop = (AdsPropertyDef) def;
                            if (prop.getNature() == EPropNature.INNATE || prop.getNature() == EPropNature.USER) {
                                return node instanceof AdsEntityObjectClassDef;
                            }
                        }

                        return (!AdsTransparence.isTransparent((AdsClassDef) node)
                                || AdsTransparence.isTransparent((AdsClassDef) node, true)) && !node.isReadOnly();
                    }
                    return false;
                }
            });
            if (hierarchyTreePanel.getExplorerManager().getRootContext().getChildren().getNodesCount(true) == 0) {
                String message = NbBundle.getMessage(ChooseDestinationClassPanel.class, "ChooseDestinationClassPanel.lblStatus.EmptyHierarchy");
                setStatus(message);
            } else {
                showCard(HIERARCHY_CARD);
            }
        }
    }

    public AdsClassDef getSelectedClass() {
        return hierarchyTreePanel.getSelectedItem();
    }
}

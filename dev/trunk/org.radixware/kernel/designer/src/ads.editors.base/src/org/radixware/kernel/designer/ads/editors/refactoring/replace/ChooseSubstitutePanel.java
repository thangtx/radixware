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
package org.radixware.kernel.designer.ads.editors.refactoring.replace;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import org.openide.explorer.ExplorerManager;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.designer.ads.editors.refactoring.components.CardPanel;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreePanel.DefaultFilter;


final class ChooseSubstitutePanel extends CardPanel {
    private static final String HIERARCHY_CARD = "Hierarchy";

    private final MembersTreePanel hierarchyTreePanel = new MembersTreePanel();
    private boolean isOpened = false;
    
    public ChooseSubstitutePanel() {
        
        hierarchyTreePanel.addPropertyChangeListener(ExplorerManager.PROP_SELECTED_NODES, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(PROP_NAME_CHANGES, evt.getOldValue(), evt.getNewValue());
            }
        });
        
        add(hierarchyTreePanel, HIERARCHY_CARD);
    }


    public void open(final AdsDefinition context, final AdsDefinition selected) {
        if (context != null && !isOpened) {

            isOpened = true;

            hierarchyTreePanel.open(context, new DefaultFilter<RadixObject>() {

                @Override
                public boolean acceptNode(RadixObject node) {
                    if (node instanceof AdsDefinition) {
                        if (node instanceof AdsMethodDef) {
                            if (((AdsMethodDef)node).isConstructor()) {
                                return false;
                            }
                        }
                        return !Objects.equals(context.getId(), ((AdsDefinition)node).getId());
                    }
                    return false;
                }
            });
            
            if (selected != null) {
                hierarchyTreePanel.selectNodeBy(selected);
            }
            
            if (hierarchyTreePanel.getExplorerManager().getRootContext().getChildren().getNodesCount(true) == 0) {
                String message = NbBundle.getMessage(ChooseSubstitutePanel.class, "ChooseSubstitutePanel.lblStatus.EmptyHierarchy");
                setStatus(message);
            } else {
                showCard(HIERARCHY_CARD);
            }
        }
    }

    public AdsDefinition getSelectedSubstitute() {
        return hierarchyTreePanel.getSelectedItem();
    }
}

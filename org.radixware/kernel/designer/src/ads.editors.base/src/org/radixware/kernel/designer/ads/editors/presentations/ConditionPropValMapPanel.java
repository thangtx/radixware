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

package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.VerticalLayout;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.common.Prop2ValueMap;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.PropListValEditorPanel;


public class ConditionPropValMapPanel extends JPanel {

    private JPanel buttonsPanel = new JPanel();
    private Prop2ValueMap propValMap;
    private JButton addPropertyButton;
    private JButton removePropertyButton;
    private PropListValEditorPanel propListPanel;
    private boolean isReadOnly;

    public ConditionPropValMapPanel() {
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        layout.setHgap(10);
        propListPanel = new PropListValEditorPanel() {
            @Override
            protected void onSelectionChanged() {
                updateButtonsState();
            }

            @Override
            protected void onValueChanged(Id id, ValAsStr val) {
                Prop2ValueMap.Prop2ValMapItem item = propValMap.getItems().findItemByPropId(id);
                if (item != null) {
                    item.setValue(val);
                }
            }
        };

        add(propListPanel, BorderLayout.CENTER);
        propListPanel.setPreferredSize(new Dimension(100, 100));
        add(buttonsPanel, BorderLayout.EAST);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
        addPropertyButton = new JButton("Add");
        addPropertyButton.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13));
        addPropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProperty();
            }
        });
        removePropertyButton = new JButton("Remove");
        removePropertyButton.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        removePropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProperty();
            }
        });
        VerticalLayout vl = new VerticalLayout();
        vl.setGap(6);
        buttonsPanel.setLayout(vl);
        buttonsPanel.add(addPropertyButton);
        buttonsPanel.add(removePropertyButton);

    }

    private void addProperty() {
        AdsClassDef spr = propValMap.findPropertiesProvider();
        if (spr != null) {
            List<AdsPropertyDef> props = spr.getProperties().get(ExtendableDefinitions.EScope.ALL, new IFilter<AdsPropertyDef>() {
                @Override
                public boolean isTarget(AdsPropertyDef radixObject) {
                    return propValMap.getItems().findItemByPropId(radixObject.getId()) == null && propValMap.isPropertySuitableForMap(radixObject);
                }
            });
            ChooseDefinitionCfg config = ChooseDefinitionCfg.Factory.newInstance(props);
            List<Definition> selection = ChooseDefinition.chooseDefinitions(config);
            if (selection != null && !selection.isEmpty()) {
                for (Definition def : selection) {
                    AdsPropertyDef prop = (AdsPropertyDef) def;
                    propValMap.getItems().add(new Prop2ValueMap.Prop2ValMapItem(prop));
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        }

    }

    private void removeProperty() {
        Id id = propListPanel.getSelectedId();
        Prop2ValueMap.Prop2ValMapItem currentItem = propValMap.getItems().findItemByPropId(id);
        if (currentItem != null) {
            propValMap.getItems().remove(currentItem);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }

    private void updateButtonsState() {
        addPropertyButton.setEnabled(!propValMap.isReadOnly() && !isReadOnly);
        removePropertyButton.setEnabled(!propValMap.isReadOnly() && propListPanel.getSelectedId() != null && !isReadOnly);
    }

    public void open(Prop2ValueMap propValMap) {
        this.propValMap = propValMap;
    }

    private Map<Id, ValAsStr> createPropValMap() {
        Map<Id, ValAsStr> map = new HashMap<>();
        for (Prop2ValueMap.Prop2ValMapItem item : propValMap.getItems()) {
            map.put(item.getPropertyId(), item.getValue());
        }
        return map;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public boolean setReadOnly(boolean isReadOnly) {
        if (this.isReadOnly != isReadOnly) {
            this.isReadOnly = isReadOnly;
            update();
            return true;
        }
        return false;
    }

    public void update() {
        this.propListPanel.setReadOnly(propValMap.isReadOnly() || isReadOnly);
        this.propListPanel.open(propValMap.findPropertiesProvider(), propValMap.getItems().getPropIds(), createPropValMap());
    }
}

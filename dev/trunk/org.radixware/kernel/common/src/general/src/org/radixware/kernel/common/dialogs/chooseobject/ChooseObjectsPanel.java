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
package org.radixware.kernel.common.dialogs.chooseobject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author dlastochkin
 */
public class ChooseObjectsPanel extends JPanel {

    private final SelectableObjectsPanel selectablePanel;
    private final SelectedObjectsPanel selectedPanel;

    private JButton selectBtn;
    private JButton deselectBtn;
    
    private JButton selectAllBtn;
    private JButton deselectAllBtn;

    public ChooseObjectsPanel(Collection<ISelectableObject> objects, String objectsTypeTitle) {
        super();

        selectablePanel = new SelectableObjectsPanel(objects, objectsTypeTitle);
        selectedPanel = new SelectedObjectsPanel(objectsTypeTitle);

        initComponents();
    }

    private void initComponents() {
        selectablePanel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveObject(selectablePanel, selectedPanel);
            }
        });

        selectedPanel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveObject(selectedPanel, selectablePanel);
            }
        });

        selectBtn = new JButton();
        selectBtn.setText(">");
//        selectBtn.setIcon(new RadixIcon(SELECT_BTN_ICON_FILE).getIcon());
        selectBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveObjects(selectablePanel, selectedPanel);
            }
        });
        
        deselectBtn = new JButton();
        deselectBtn.setText("<");
//        deselectBtn.setIcon(new RadixIcon(DESELECT_BTN_ICON_FILE).getIcon());
        deselectBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveObjects(selectedPanel, selectablePanel);
            }
        });
        
        selectAllBtn = new JButton();
        selectAllBtn.setText(">>");
//        selectAllBtn.setIcon(new RadixIcon(SELECT_ALL_BTN_ICON_FILE).getIcon());
        selectAllBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveAllObjects(selectablePanel, selectedPanel);
            }
        });
        
        deselectAllBtn = new JButton();
        deselectAllBtn.setText("<<");
//        deselectAllBtn.setIcon(new RadixIcon(DESELECT_ALL_BTN_ICON_FILE).getIcon());
        deselectAllBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveAllObjects(selectedPanel, selectablePanel);
            }
        });
        
        selectedPanel.setMinimumSize(selectablePanel.getMinimumSize());
        
        JPanel buttonsPanel = new JPanel(new MigLayout("fill", "[shrink]", "[shrink][shrink][shrink][grow][shrink][shrink][shrink]"));
        buttonsPanel.add(selectBtn, "cell 0 1, grow");
        buttonsPanel.add(selectAllBtn, "cell 0 2, shrink");
        buttonsPanel.add(new JPanel(), "cell 0 3, grow");
        buttonsPanel.add(deselectBtn, "cell 0 4, grow");
        buttonsPanel.add(deselectAllBtn, "cell 0 5, shrink");
        
        this.setLayout(new MigLayout("fill", "[grow][shrink][grow]", "[grow]"));//"[grow][shrink][shrink][grow][shrink][shrink][grow]"));
        this.add(selectablePanel, "grow");
        this.add(buttonsPanel, "shrink");
        this.add(selectedPanel, "grow");        
    }

    private void moveObject(BaseSelectableObjectsPanel source, BaseSelectableObjectsPanel dest) {
        dest.addObject(source.getSelectedObject());
        source.removeSelectedObject();

        source.update();
        dest.update();
    }
    
    private void moveObjects(BaseSelectableObjectsPanel source, BaseSelectableObjectsPanel dest) {
        dest.addObjects(source.getSelectedObjects());
        source.removeSelectedObjects();

        source.update();
        dest.update();
    }
    
    private void moveAllObjects(BaseSelectableObjectsPanel source, BaseSelectableObjectsPanel dest) {
        dest.addObjects(source.getAllObjects());
        source.removeAllObjects();

        source.update();
        dest.update();
    }
    
    public void update() {
        selectablePanel.update();
        selectedPanel.update();
    }
    
    public Collection<ISelectableObject> getSelectedObjects() {
        return selectedPanel.getAllObjects();
    }
    
}

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

package org.radixware.kernel.designer.ads.editors.clazz.sql.panels;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.EValType;


public class ValTypePanel extends JPanel {

    private int rowsCount;
    private int columnsCount;
    private Set<EValType> valTypes;
    private ButtonGroup typesGroup;
    private EValType selectedType;
    private Map<EValType, AbstractButton> valType2button = new HashMap<EValType, AbstractButton>();

    public ValTypePanel(Set<EValType> valTypes, int rowsCount, int columnsCount) {
        if (rowsCount <= 0 || columnsCount <= 0) {
            int typeCount = valTypes.size();
            rowsCount = (int) Math.ceil(Math.sqrt((double) typeCount));
            columnsCount = typeCount / rowsCount;
            if (columnsCount * rowsCount < typeCount) {
                columnsCount++;
            }
        }
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
        this.valTypes = valTypes;

        setLayout(new GridLayout(rowsCount, columnsCount, 5, 5));
        typesGroup = new ButtonGroup();
        Iterator<EValType> iterator = valTypes.iterator();
        while (iterator.hasNext()) {
            final EValType valType = iterator.next();
            final JRadioButton bt = new JRadioButton(valType.getName());
            bt.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    if (bt.isSelected()) {
                        selectedType = valType;
                    }
                }
            });
            typesGroup.add(bt);
            valType2button.put(valType, bt);
            add(bt);
        }

        setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(ValTypePanel.class, "msg-valtype")));
    }

    public EValType getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(EValType selectedType) {
        AbstractButton bt = valType2button.get(selectedType);
        if (bt != null) {
            bt.setSelected(true);
        } else {
            if (!valType2button.isEmpty()) {
                valType2button.values().iterator().next().setSelected(true);
            }
        }
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public Set<EValType> getValTypes() {
        return valTypes;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (AbstractButton bt : valType2button.values()) {
            bt.setEnabled(enabled);
        }
    }

    public ValTypePanel(Set<EValType> valTypes) {
        this(valTypes, -1, -1);
    }
}

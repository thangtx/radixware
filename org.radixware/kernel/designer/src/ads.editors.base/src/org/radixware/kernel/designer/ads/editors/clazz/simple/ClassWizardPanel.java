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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionPanel;


abstract class ClassWizardPanel extends JPanel implements ChangeListener {

    protected ChooseDefinitionPanel chooseDefinitionPanel;
    protected AdsClassDef resultClassObj;
    protected AdsClassDef context; //"context class" for which we search super class
    //private TypeArguments typeArguments;//typeArguments of "context" class
    protected JTable table;
    protected ChooseDefinitionCfg chooseDefinitionCfg;
    protected boolean isSpecifySuperClassArgs;
    protected JCheckBox checkBox;

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public ClassWizardPanel(AdsClassDef context) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.context = context;
        //typeArguments = context.getTypeArguments();

        chooseDefinitionPanel = new ChooseDefinitionPanel();
        chooseDefinitionPanel.setAlignmentX(0.0f);
        chooseDefinitionPanel.setBorder(BorderFactory.createEtchedBorder());
        this.add(chooseDefinitionPanel);

        this.add(Box.createRigidArea(new Dimension(0, 12)));

        checkBox = new JCheckBox("Specify arguments");
        checkBox.setBorder(null);
        checkBox.setAlignmentX(0.0f);
        checkBox.setSelected(false);
        isSpecifySuperClassArgs = false;
        checkBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                final boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                if (!selected && table.isEditing()) {
                    table.getCellEditor().stopCellEditing();
                }

                table.setEnabled(selected);
                if (table.getModel() instanceof TypeArgumentsTableModel){
                    ((TypeArgumentsTableModel) table.getModel()).setShowType(selected);
                }
            }
        });

        this.add(checkBox);

        this.add(Box.createRigidArea(new Dimension(0, 12)));

        final JPanel argumentsPanel = new JPanel();
        argumentsPanel.setLayout(new BoxLayout(argumentsPanel, BoxLayout.Y_AXIS));
        argumentsPanel.setAlignmentX(0.0f);
        table = new JTable();
        table.setAlignmentX(0.0f);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setEnabled(false);
        argumentsPanel.add(new JScrollPane(table));
        this.add(argumentsPanel);

        initConfig();

        assert (chooseDefinitionCfg != null);
        chooseDefinitionPanel.open(chooseDefinitionCfg);
        chooseDefinitionPanel.addChangeListener(this);
    }

    abstract protected void initConfig();

    protected void onSelectedClassChanged() {
        //do nothing
    }

    public boolean isComplete() {
        return resultClassObj != null && ((TypeArgumentsTableModel) table.getModel()).isComplete();
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        checkBox.setSelected(false);

        final Definition selectedDef = chooseDefinitionPanel.getSelected();
        if (selectedDef != null) {
            assert (selectedDef instanceof AdsClassDef);
            resultClassObj = (AdsClassDef) selectedDef;
            onSelectedClassChanged();
            changeSupport.fireChange();
        }
    }

    public AdsClassDef getResultClassObj() {
        return resultClassObj;
    }

    public AdsTypeDeclaration getResultAdsTypeDeclaration() {
        if (checkBox.isSelected() && !resultClassObj.getTypeArguments().getArgumentList().isEmpty()) {
            return AdsTypeDeclaration.Factory.newInstance(resultClassObj).toGenericType(((TypeArgumentsTableModel) table.getModel()).getCreatedTypeArgs());
        } else {
            return AdsTypeDeclaration.Factory.newInstance(resultClassObj);
        }
    }
}

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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.AdsFieldParameterDef;
import org.radixware.kernel.designer.common.dialogs.components.TypeEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;


final class AdsEnumClassParameterEditorPanel extends JPanel implements IReadyStatable {

    private NameEditorComponent nameEditor;
    private InitialValueEditorComponent initValueEditor;
    private TypeEditorComponent typeEditor;
    private AdsFieldParameterDef param;
    private StateManager stateManager;
    private ReadyStateAdapter readyStateAdapter;
    private AdsEnumClassDef context;

    private void initComponents() {

        setLayout(new GridBagLayout());

        final JLabel lblParamName = new JLabel();
        lblParamName.setText(NbBundle.getMessage(AdsEnumClassParameterEditorPanel.class, "AdsEnumClassAddParametrPanel.lblParamName.text"));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(lblParamName, c);

        final JLabel lblParamType = new JLabel();
        lblParamType.setText(NbBundle.getMessage(AdsEnumClassParameterEditorPanel.class, "AdsEnumClassAddParametrPanel.lblParamType.text"));
        c.gridy = 1;
        add(lblParamType, c);

        final JLabel lblParamInitVal = new JLabel();
        lblParamInitVal.setText(NbBundle.getMessage(AdsEnumClassParameterEditorPanel.class, "AdsEnumClassAddParametrPanel.lblParamInitVal.text"));
        c.gridy = 2;
        add(lblParamInitVal, c);

        nameEditor = new NameEditorComponent();
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4, 4, 4, 4);
        add(nameEditor.getEditorComponent(), c);

        typeEditor = new TypeEditorComponent();
        c.gridy = 1;
        add(typeEditor.getEditorComponent(), c);

        typeEditor.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChanged(ValueChangeEvent e) {
                param.getValue().setType(typeEditor.getValue());
                initValueEditor.open(param.getValue().getValueController());
                updateUI();
            }
        });


        initValueEditor = new InitialValueEditorComponent();
        c.gridy = 2;
        add(initValueEditor.getEditorComponent(), c);

        final StateDisplayer stateDisplayer = new StateDisplayer();
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.LINE_START | GridBagConstraints.PAGE_START;
        c.insets = new Insets(10, 4, 4, 4);
        add(stateDisplayer, c);
        IStateDisplayer.Locator.register(stateDisplayer, this);

        setBorder(BorderFactory.createEmptyBorder(10, 4, 0, 4));
    }

    public AdsEnumClassParameterEditorPanel(AdsEnumClassDef def, AdsFieldParameterDef parameter) {
        super();

        assert def != null && parameter != null : "null context";

        initComponents();

        context = def;

        readyStateAdapter = new ReadyStateAdapter(false);
        stateManager = new StateManager(this);

        ValueChangeListener checkReadyState = new ValueChangeListener() {

            @Override
            public void valueChanged(ValueChangeEvent e) {
                checkReadyState();
            }
        };

        nameEditor.addValueChangeListener(checkReadyState);
        typeEditor.addValueChangeListener(checkReadyState);

        param = parameter;
        nameEditor.open(new NameEditorComponent.NamedContext() {

            @Override
            public String getName() {
                return param.getName();
            }

            @Override
            public boolean isValidName(String name) {
                return true;
            }

            @Override
            public void setName(String name) {
                param.setName(name);
            }
        });

        typeEditor.open(param, param.getValue());
        initValueEditor.open(param.getValue().getValueController());

        checkReadyState();
    }

    public AdsFieldParameterDef getParameter() {
        try {
            nameEditor.commit();
            param.getValue().setInitValue(initValueEditor.getValue());
            param.getValue().setType(typeEditor.getValue());
            return param;
        } catch (Exception e) {
            Logger.getLogger(AdsEnumClassParameterEditorPanel.class.getName()).log(
                    Level.WARNING, "Falied to create new 'AdsEnumClassFieldParamDef'", e);
            return null;
        }
    }

    @Override
    public boolean getReadyState() {
        return readyStateAdapter.getReadyState();
    }

    public void checkReadyState() {
        String errMessage = null, name = nameEditor.getValue().toString();
        if (name.isEmpty()) {
            errMessage = NbBundle.getMessage(AdsEnumClassParameterEditorPanel.class, "AdsEnumClassDefStateManager-EmptyNameMessage");
        } else if (!AdsEnumClassStructureEditorPanel.isUniqueName(context.getFieldStruct().get(EScope.LOCAL_AND_OVERWRITE), name, (RadixObject) null)) {
            errMessage = NbBundle.getMessage(AdsEnumClassParameterEditorPanel.class, "AdsEnumClassDefStateManager-DuplicateParameterName");
        } else if (!typeEditor.isSetValue()) {
            errMessage = NbBundle.getMessage(AdsEnumClassParameterEditorPanel.class, "AdsEnumClassDefStateManager-UndefinedTypeMessage");
        }

        if (errMessage != null) {
            stateManager.error(errMessage);
            readyStateAdapter.setReadyState(false);
        } else {
            stateManager.ok();
            readyStateAdapter.setReadyState(true);
        }
    }

    @Override
    public ChangeSupport getReadyStateChangeSupport() {
        return readyStateAdapter.getReadyStateChangeSupport();
    }
}

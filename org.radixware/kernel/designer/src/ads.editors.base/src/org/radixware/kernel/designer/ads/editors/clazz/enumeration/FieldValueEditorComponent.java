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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.IValueController;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


final class FieldValueEditorComponent extends InitialValueEditorComponent {

    private final JButton cmdDefault = new JButton();

    public FieldValueEditorComponent() {
        super();

        cmdDefault.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isDefault()) {
                    final String message = NbBundle.getMessage(FieldValueEditorComponent.class, "FieldValueEditorComponent.DefaultDialogMessage");
                    final boolean confirm = DialogUtils.messageConfirmation(message);
                    if (confirm) {
                        resetToDefault(true);
                    }
                } else {
                    resetToDefault(false);
                }
            }
        });

        add(cmdDefault);

        addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChanged(ValueChangeEvent e) {
                if (isDefault()) {
                    updateButton();
                }
            }
        });
    }

    @Override
    public void open(IValueController context) {
        assert context instanceof AdsEnumClassFieldDef.FieldValueController : "Context must be instance of AdsEnumClassFieldDef.FieldValueController";

        if (context instanceof AdsEnumClassFieldDef.FieldValueController) {
            this.context = (AdsEnumClassFieldDef.FieldValueController) context;

            resetToDefault(isDefault());
        }
    }

    @Override
    public AdsValAsStr getValue() {
        if (isDefault()) {
            return null;
        } else {
            return editor.getValue();
        }
    }

    @Override
    public void commit() {
        editor.commit();
    }

    public boolean isDefault() {
        return !getContext().getContextDefinition().containsLocal(getContext().getContextParameter().getId());
    }

    private boolean isSetInitValue() {
        return getContext().getContextParameter().getValue().isInitialValueSet();
    }

    private void updateButton() {
        if (isDefault()) {
            cmdDefault.setIcon(RadixWareIcons.CHECK.SET.getIcon());
            cmdDefault.setToolTipText(NbBundle.getMessage(FieldValueEditorComponent.class, "FieldValuesEditorComponent.UnsetDefaultButton.Text"));
        } else {
            cmdDefault.setIcon(RadixWareIcons.CHECK.UNSET.getIcon());
            cmdDefault.setToolTipText(NbBundle.getMessage(FieldValueEditorComponent.class, "FieldValuesEditorComponent.SetDefaultButton.Text"));
        }
    }

    private void resetToDefault(boolean toDefault) {

        cmdDefault.setVisible(isSetInitValue());

        if (toDefault) {
            getContext().getContextDefinition().removeByParam(getContext().getContextParameter());
        } else if (isDefault()) {
            getContext().setValue(AdsValAsStr.Factory.newCopy(getContext().getContextParameter().getValue().getInitialValue(), getContext().getContextDefinition()));
        }

        updateButton();
        editor.open(context);
        editor.setEnabled(!toDefault || !isSetInitValue());
        updateUI();
    }

    private AdsEnumClassFieldDef.FieldValueController getContext() {
        return (AdsEnumClassFieldDef.FieldValueController) context;
    }
}

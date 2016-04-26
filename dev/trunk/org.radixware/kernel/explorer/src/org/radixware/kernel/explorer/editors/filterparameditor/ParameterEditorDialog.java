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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QLayout.SizeConstraint;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.views.IParameterEditorDialog;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class ParameterEditorDialog extends ExplorerDialog implements IParameterEditorDialog{

    final private ParameterEditorWidget editor;
    final private ISqmlParameter parameter;

    public ParameterEditorDialog(IClientEnvironment environment, final ISqmlParameter parameter,
            final ISqmlTableDef context,
            final List<String> restrictedNames,
            final boolean isReadonly,
            final QWidget parent) {
        super(environment, parent, null);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        this.parameter = parameter;
        final boolean readonly = isReadonly || !(parameter instanceof ISqmlModifiableParameter);
        if (!readonly) {
            ((ISqmlModifiableParameter) parameter).saveState();
        }
        editor = new ParameterEditorWidget(getEnvironment(), context, readonly, this);
        setupUi();
        editor.open(parameter, restrictedNames);
        final String title;
        if (isReadonly) {
            title = Application.translate("SqmlEditor", "Viewing condition parameter '%s'");
        } else {
            title = Application.translate("SqmlEditor", "Editing condition parameter '%s'");
        }
        setWindowTitle(String.format(title, parameter.getTitle()));
        setWindowIcon(ExplorerIcon.getQIcon(parameter.getIcon()));
    }

    private void setupUi() {
        layout().addWidget(editor);
        final EnumSet<EDialogButtonType> buttons;
        if (editor.isReadonly() && !parameter.canHavePersistentValue()) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        addButtons(buttons, true);
        if (parameter.getBasePropertyId() == null) {
            layout().setSizeConstraint(SizeConstraint.SetFixedSize);
        }        
    }

    @Override
    public void done(int result) {
        if (!editor.isReadonly() || parameter.canHavePersistentValue()) {
            if (result == QDialog.DialogCode.Accepted.value()
                    && !editor.writeAttributes(parameter)) {
                return;
            } else if (result == QDialog.DialogCode.Rejected.value() && (parameter instanceof ISqmlModifiableParameter)) {
                ((ISqmlModifiableParameter) parameter).restoreState();
            }
        }
        super.done(result);
    }
}

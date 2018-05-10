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

package org.radixware.kernel.explorer.editors.editmask;

import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.widgets.IEditMaskEditorDialog;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class EditMaskEditorDialog extends ExplorerDialog implements IEditMaskEditorDialog {
    private final EditMaskEditorWidget widget;
    private boolean readOnly = false;
    
    public EditMaskEditorDialog(final IClientEnvironment environment, final IWidget parent, final EValType valType) {
        super(environment, (QWidget)parent);
        widget = new EditMaskEditorWidget(environment, this, valType);
        setUpUi(environment.getMessageProvider());
    }
    
    public EditMaskEditorDialog(final IClientEnvironment environment, final IWidget parent, final RadEnumPresentationDef enumDef) {
        super(environment, (QWidget)parent);
        widget = new EditMaskEditorWidget(environment, this, enumDef);
        setUpUi(environment.getMessageProvider());
    }
    
    public EditMaskEditorDialog(final IClientEnvironment environment, final IWidget parent, final EEditMaskType maskType) {
        super(environment, (QWidget)parent);
        widget = new EditMaskEditorWidget(environment, this, maskType);
        setUpUi(environment.getMessageProvider());
    }
    
    private void setUpUi(final MessageProvider msgProvider) {
        setWindowTitle( msgProvider.translate("EditMask", "Edit Mask Editor") );
        setWindowIcon( ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT) );
        layout().addWidget(widget);
        
        addButton(EDialogButtonType.OK);
        addButton(EDialogButtonType.CANCEL);
        acceptButtonClick.connect(this, "onAccept()");
        rejectButtonClick.connect(this, "reject()");
    }
    
    private void redrawUi() {
        widget.setReadOnly(readOnly);
        clearButtons();
        if(readOnly) {
            addButton(EDialogButtonType.CLOSE);
        } else {
            addButton(EDialogButtonType.OK);
            addButton(EDialogButtonType.CANCEL);
        }
        acceptButtonClick.connect(this, "onAccept()");
        rejectButtonClick.connect(this, "reject()");        
    }
    
    @Override
    public EditMask getEditMask() {
        return widget.getEditMask();
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        widget.setEditMask(editMask);
    }

    @Override
    public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        widget.setHiddenOptions(options);
    }

    @Override
    public void setVisibleOptions(final EnumSet<EEditMaskOption> options) {
        widget.setVisibleOptions(options);
    }

    @Override
    public void setEnabledOptions(final EnumSet<EEditMaskOption> options) {
        widget.setEnabledOptions(options);
    }

    @Override
    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        widget.setDisabledOptions(options);
    }

    @Override
    public boolean checkOptions() {
        return widget.isDefaultEditMaskUsed() || widget.checkOptions();
    }
    
    @SuppressWarnings("unused")
    private void onAccept() {
        if(checkOptions()) {
            accept();
        }
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        if(this.readOnly != readOnly) {
            this.readOnly = readOnly;
            redrawUi();
        }
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }
    
}

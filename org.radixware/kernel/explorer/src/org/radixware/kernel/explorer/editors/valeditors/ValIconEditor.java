/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.gui.QBoxLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.SelectImageDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;

public class ValIconEditor extends ValEditor<Id> {

    private final QPushButton iconedButton = new QPushButton();
    private final static int SPACING = 4;
    SelectImageDialog selectImgDlg;
    
    public ValIconEditor(IClientEnvironment environment, QWidget parent, boolean mandatory, boolean readOnly) {
        super(environment, parent, new EditMaskNone(), mandatory, readOnly);
        this.setFrame(false);
        getLineEdit().setReadOnly(true);
        getLineEdit().setVisible(false);
        clearBtn.setVisible(false);
        ((QBoxLayout) layout()).insertWidget(0, iconedButton);
        iconedButton.setText("...");
        ((QBoxLayout) layout()).setSpacing(SPACING);
        iconedButton.clicked.connect(this, "onClicked()");
    }

    @SuppressWarnings("unused")
    private void onClicked() {
        selectImgDlg = new SelectImageDialog(getEnvironment(), null);
        selectImgDlg.accepted.connect(this, "getSelectedIcon()");
        selectImgDlg.execDialog();
    }
    
    private void getSelectedIcon() {
        Id iconId = selectImgDlg.getIconId();
        iconedButton.setIcon(ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(iconId)));
        super.setValue(iconId);
    }

    @Override
    public void setValue(Id value) {
        super.setValue(value); 
        if (value == null) {
            iconedButton.setText("...");
            iconedButton.setIcon(null);
        } else {
            iconedButton.setText(null);
            iconedButton.setIcon(ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(value)));
        }
    }
    
}

/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.dialogs.pkcs11;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QButtonGroup;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.common.client.utils.Pkcs11Token;

public class SelectPkcs11TokenDialog extends ExplorerDialog {
    
    private final QButtonGroup buttons = new QButtonGroup(this);
    private final List<Pkcs11Token> tokens = new LinkedList<>();
    private Pkcs11Token selectedToken;

    public SelectPkcs11TokenDialog(final IClientEnvironment environment, final List<Pkcs11Token> tokens, final QWidget parentWidget){
        super(environment,parentWidget);
        this.tokens.addAll(tokens);
        setupUi();
    }
        
    private void setupUi(){
        setWindowTitle(getEnvironment().getMessageProvider().translate("PKCS11", "Select HSM"));
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
        final QVBoxLayout vltMain = new QVBoxLayout();
        final String title = getEnvironment().getMessageProvider().translate("PKCS11", "Found devices:");
        final QGroupBox gbDevices = new QGroupBox(title,this);        
        gbDevices.setLayout(vltMain);
        
        for (int i=0; i<tokens.size(); i++) {
            final Pkcs11Token token = tokens.get(i);
            String tokenTitle = "";
            if (token.getManufacturerID()!=null){
                tokenTitle = token.getManufacturerID().trim();
            }
            if (token.getModel()!=null){
                tokenTitle = tokenTitle.isEmpty() ? token.getModel() : tokenTitle+" "+token.getModel().trim();//NOPMD
            }
            final QRadioButton rbDevice = new QRadioButton(tokenTitle,this);//NOPMD
            buttons.addButton(rbDevice, i+1);
            rbDevice.setObjectName(tokenTitle);            
            vltMain.addWidget(rbDevice);
            if (i==0){
                rbDevice.setChecked(true);
            }
        }
        vltMain.addStretch();
        layout().addWidget(gbDevices);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        setFixedSize(sizeHint());        
    }

    @Override
    public void done(final int result) {
        if (result== QDialog.DialogCode.Accepted.value()){
            selectedToken = tokens.get(buttons.checkedId()-1);
        }else{
            selectedToken = null;
        }
        super.done(result);
    }
    
    public Pkcs11Token getSelectedToken(){
        return selectedToken;
    }
    
    public static Pkcs11Token selectToken(final IClientEnvironment environment, final String libPath, final QWidget parent){
        return selectToken(environment, libPath, parent,false);
    }
    
    public static Pkcs11Token selectToken(final IClientEnvironment environment, final String libPath, final QWidget parent, final boolean forcedlyShowDialog){
        final List<Pkcs11Token> tokens = Pkcs11Token.enumTokens(environment, libPath, true);
        if (tokens.isEmpty()){
            final String title = environment.getMessageProvider().translate("PKCS11", "Sorry. No Devices Found");
            final String message = environment.getMessageProvider().translate("PKCS11", "No HSM devices were found");
            environment.messageInformation(title, message);
            return null;
        }else if (tokens.size()==1 && !forcedlyShowDialog){
            return tokens.get(0);
        }else{
            final SelectPkcs11TokenDialog dialog = new SelectPkcs11TokenDialog(environment, tokens, parent);
            return dialog.exec()==QDialog.DialogCode.Accepted.value() ? dialog.getSelectedToken() : null;
        }
    }    
}

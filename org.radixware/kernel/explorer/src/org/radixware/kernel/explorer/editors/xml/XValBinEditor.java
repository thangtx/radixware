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

package org.radixware.kernel.explorer.editors.xml;

import com.trolltech.qt.gui.QButtonGroup;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QRegExpValidator;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValBinEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValListEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.ValueConverter;


class XValBinEditor extends ValBinEditor{    
    
    private final static class SelectCharsetDialog extends ExplorerDialog{
        
        private final static List<String> OFTEN_USED = 
            Arrays.asList("UTF-8","UTF-16","UTF-16BE","UTF-16LE");
        
        private Charset charset;
        private final ValListEditor charsetEditor;
        
        public SelectCharsetDialog(final IClientEnvironment environment, final QWidget parent, final Charset initialValue){
            super(environment,parent);
            charset = initialValue;
            charsetEditor = new ValListEditor(environment, this, new EditMaskList(), true, false);
            setupUi(environment);
        }
        
        private void setupUi(final IClientEnvironment environment){
            final MessageProvider mp = environment.getMessageProvider();
            final Locale locale = environment.getLocale();
            setWindowTitle(mp.translate("XmlEditor", "Select Charset"));
            final QGridLayout layout = new QGridLayout();
            final QButtonGroup bgCharset = new QButtonGroup(this);            
            
            final QRadioButton rbBinary = new QRadioButton(mp.translate("XmlEditor", "Binary data"), this);
            layout.addWidget(rbBinary, 0, 0);
            bgCharset.addButton(rbBinary,0);
            if (charset==null){
                rbBinary.setChecked(true);
            }
            
            for (String charsetName: OFTEN_USED){
                final QRadioButton rbCharset = 
                    new QRadioButton(Charset.forName(charsetName).displayName(locale), this);//NOPMD                
                bgCharset.addButton(rbCharset,layout.rowCount());
                layout.addWidget(rbCharset, layout.rowCount(), 0);
                if (charset!=null && charsetName.equals(charset.name())){
                    rbCharset.setChecked(true);
                }
            }
            
            final QRadioButton rbOther = new QRadioButton(mp.translate("XmlEditor", "Other:"), this);            
            bgCharset.addButton(rbOther,layout.rowCount());
            layout.addWidget(rbOther, layout.rowCount(), 0);

            final EditMaskList editMask = new EditMaskList();
            final Map<String,Charset> charsets = Charset.availableCharsets();            
            String charsetName;
            for(Map.Entry<String,Charset> entry: charsets.entrySet()){
                charsetName = entry.getKey();
                if (!OFTEN_USED.contains(charsetName)){
                    editMask.addItem(entry.getValue().displayName(locale), charsetName);
                }
            }
            charsetEditor.setEditMask(editMask);
            if (charset==null || OFTEN_USED.contains(charset.name())){
                charsetEditor.setEnabled(false);
            }else{
                charsetEditor.setValue(charset.name());
                rbOther.setChecked(true);
            }
            layout.addWidget(charsetEditor, layout.rowCount()-1, 1);
            
            dialogLayout().addLayout(layout);
            addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
            
            rbOther.clicked.connect(charsetEditor,"setEnabled(boolean)");
            charsetEditor.valueChanged.connect(this,"onCharsetChanged()");
            bgCharset.buttonIdClicked.connect(this,"onButtonClicked(int)");            
        }
        
        @SuppressWarnings("unused")
        private void onCharsetChanged(){
            charset = Charset.forName((String)charsetEditor.getValue());
            getButton(EDialogButtonType.OK).setEnabled(charset!=null);
        }
        
        @SuppressWarnings("unused")
        private void onButtonClicked(final int buttonId){
            if (buttonId==0){
                charset = null;
                getButton(EDialogButtonType.OK).setEnabled(true);
            }
            else if (buttonId>OFTEN_USED.size()){
                if (charsetEditor.getValue()==null){
                    charset = null;
                }else{
                    charset = Charset.forName((String)charsetEditor.getValue());
                }
                getButton(EDialogButtonType.OK).setEnabled(charset!=null);
            }else{
                charset = Charset.forName(OFTEN_USED.get(buttonId-1));
                getButton(EDialogButtonType.OK).setEnabled(true);
            }            
        }
        
        public Charset getCharset(){
            return charset;
        }
    
    }
    
    public final Signal1<Charset> charsetChanged = new Signal1<>();            
    private Charset charset;
    
    public XValBinEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskNone editMask, final boolean mandatory, final boolean readOnly) {
        super(environment,parent,editMask,mandatory,readOnly);
        setupUI();
    }
    
    public XValBinEditor(final IClientEnvironment environment, final QWidget parent){
        super(environment,parent);
        setupUI();
    }
    
    public void setCharset(final Charset newCharset){
        if (charset!=newCharset){
            charset = newCharset;
            if (charset==null){
                getLineEdit().setValidator(new QRegExpValidator(HEX_VALUE_PATTERN, this));
            }else{
                getLineEdit().setValidator(null);
            }
            setOnlyText(convertBinToText(getValue(), getLineEdit().hasFocus()), null);
            charsetChanged.emit(charset);
        }
    }
    
    private void setupUI(){
        final QToolButton btn = new QToolButton();
        final Icon icon = getEnvironment().getApplication().getImageManager().loadIcon("classpath:images/charset.svg");
        btn.setIcon(ExplorerIcon.getQIcon(icon));
        btn.clicked.connect(this,"onSelectCharsetClick()");
        addButton(btn);
    }
    
    @Override
    protected void onTextEdited(final String newText) {
        final Bin newValue;
        try {
            newValue = convertText2Bin(newText);
        } catch (IllegalArgumentError error) {
            return;
        }
        if (getEditMask().validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE) {
            setOnlyValue(newValue);
        }
    }

    @Override
    protected void onFocusIn() {
        if (!isReadOnly()) {
            setOnlyText(convertBinToText(getValue(), true), null);
        }
        if (getLineEdit() != null) {
            getLineEdit().selectAll();
            updateValueMarkers(false);
            repaint();
        }
    }
    
    @Override
    protected String getStringToShow(final Object value) {
        return convertBinToText((Bin)value, false);
    }
    
    private Bin convertText2Bin(final String text) {
        if (text==null || text.isEmpty()){
            return null;
        }
        if (charset==null){
            final String hexadecimalString = text.replace(" ", "");
            if (hexadecimalString.isEmpty()) {
                return null;
            }
            return ValueConverter.hexadecimalString2Bin(hexadecimalString, null);
        }else{
            return Bin.wrap(text.getBytes(charset));
        }
    }
    
    private String convertBinToText(final Bin bin, final boolean focused){
        if (getEditMask().isSpecialValue(bin)){
            return focused ? "" : getEditMask().toStr(getEnvironment(), value);
        }else{
            if (charset==null){
                return ValueConverter.arrByte2Str(bin.get(), " ");
            }else{
                final byte[] data = bin.get();
                if (data.length==0){
                    return "";
                }else{
                    return new String(data, charset);
                }
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void onSelectCharsetClick(){
        final SelectCharsetDialog dialog = new SelectCharsetDialog(getEnvironment(),this,charset);
        if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
            setCharset(dialog.getCharset());
        }
    }
}

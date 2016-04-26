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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionTabV3;
import com.trolltech.qt.gui.QStylePainter;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.env.UserDefinitionSettings;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.jml.JmlTagEventCode;
import org.radixware.kernel.common.jml.JmlTagLocalizedString;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.monitoring.tree.UnitsTree;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;


public class LocalizedStr_Dialog extends ExplorerDialog {

    private JmlTagLocalizedString tag;
    private AdsMultilingualStringDef mlStringdef;
    private boolean isReadOnly;
    private boolean isObject = false;
    private Map<EIsoLanguage, QTextEdit> editorsToLangs;
    private AdsUserFuncDef userFunc;
    private QCheckBox cbIsObject;
    private final MyTabWidget tab;

    public LocalizedStr_Dialog(final XscmlEditor parent, final JmlTagLocalizedString tag, final String dialogTitle, final AdsUserFuncDef userFunc) {
        super(parent.getEnvironment(), parent, dialogTitle/*environment.getMessageProvider().translate("SqmlEditor", "Edit Localized String")*/);
        this.tag = tag;
        if (tag != null) {
            this.mlStringdef = tag.findLocalizedString(tag.getStringId());
            isObject = tag.getType() == JmlTagLocalizedString.EType.OBJECT;
        }
        this.userFunc = userFunc;
        this.isReadOnly = parent.isReadOnly();
        this.setWindowTitle(dialogTitle);
        accepted.connect(this, "setLocalizedStrVals()");
        tab = new MyTabWidget();
        createUi();
    }
    
    private void tabChanged(final Integer index){
        final String langValue=tab.tabWhatsThis(index);
        try{
            final EIsoLanguage lang=EIsoLanguage.getForValue(langValue);
            final QTextEdit textEditor=editorsToLangs.get(lang);            
            if(!textEditor.toPlainText().isEmpty()){
                final QTextCursor tc=textEditor.textCursor();
                tc.setPosition(textEditor.toPlainText().length());
                textEditor.setTextCursor(tc);
            }
            textEditor.setFocus();
        }catch(NoConstItemWithSuchValueError ex){ 
            Logger.getLogger(UnitsTree.class.getName()).log(Level.SEVERE, null, ex);
        }                
    }

    private void createUi() {
        tab.setTabPosition(QTabWidget.TabPosition.West);
        final List<EIsoLanguage> langs = new UserDefinitionSettings(getEnvironment()).getSupportedLanguages();
        editorsToLangs = new HashMap<>();
        for (EIsoLanguage lang : langs) {
            final QWidget w = new QWidget();
            final QVBoxLayout layout = new QVBoxLayout();
            layout.setMargin(0);
            final QTextEdit textEdit = new QTextEdit(w);
            if (mlStringdef != null) {
                textEdit.setText(mlStringdef.getValue(lang));
            }            
            layout.addWidget(textEdit);
            w.setLayout(layout);            
            tab.addTab(w, lang.getName());   
            tab.setTabWhatsThis(tab.count()-1,lang.getValue());
            editorsToLangs.put(lang, textEdit);
        }
        tab.currentChanged.connect(this, "tabChanged(Integer)");
        final EnumSet<EDialogButtonType> buttons;
        if (isReadOnly) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
            tab.setEnabled(false);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        
        this.dialogLayout().addWidget(tab);
        
        if (tag instanceof JmlTagEventCode == false) {
            cbIsObject = new QCheckBox(getEnvironment().getMessageProvider().translate(
                    "JmlEditor", "Tag value is string set (IMultilingualString instance)"), this);
            cbIsObject.setChecked(isObject);
            cbIsObject.toggled.connect(this, "onIsObjectToggled(boolean)");
            this.dialogLayout().addWidget(cbIsObject);
        }
             
        addButtons(buttons, true);   
        tabChanged(0);
    }
    
    @SuppressWarnings("unused")
    private void onIsObjectToggled(boolean checked) {
        isObject = checked;
    }

    public AdsMultilingualStringDef getLocalizedString() {
        if (mlStringdef == null) {
            mlStringdef = AdsMultilingualStringDef.Factory.newInstance();
            userFunc.findLocalizingBundle().getStrings().getLocal().add(mlStringdef);
        }
        return mlStringdef;
    }
    
    public JmlTagLocalizedString getTag() {
        if (tag == null) {
            tag = JmlTagLocalizedString.Factory.newInstance(getLocalizedString());
        }
        JmlTagLocalizedString.EType type = isObject ? JmlTagLocalizedString.EType.OBJECT : 
                                                      JmlTagLocalizedString.EType.SIMPLE;
        tag.setType(type);
        return tag;
    }

    @SuppressWarnings("unused")
    private void setLocalizedStrVals() {
        for (EIsoLanguage lang : editorsToLangs.keySet()) {
            getLocalizedString().setValue(lang, editorsToLangs.get(lang).toPlainText());
        }
    }

    class MyTabWidget extends QTabWidget {

        MyTabWidget() {
            MyTabBar t = new MyTabBar();
            this.setTabBar(t);
        }
    }

    class MyTabBar extends QTabBar {

        //MyTabBar() {
        //}

        @Override
        protected void paintEvent(final QPaintEvent qpe) {
            //super.paintEvent(qpe); //To change body of generated methods, choose Tools | Templates.
            final QStylePainter p = new QStylePainter(this);
            final QPainter painter = new QPainter(this);
            for (int index = 0; index < count(); index++) {
                final QStyleOptionTabV3 tab = new QStyleOptionTabV3();
                initStyleOption(tab, index);
                tab.setText("");
                p.drawControl(QStyle.ControlElement.CE_TabBarTab, tab);

                final QRect tabrect = tabRect(index);
                tabrect.adjust(0, 8, 0, -8);
                painter.drawText(tabrect, /*Qt.Orientation.Vertical.value() |*/ Qt.AlignmentFlag.AlignCenter.value() | Qt.AlignmentFlag.AlignVCenter.value(), tabText(index));
            }
            painter.end();
            p.end();

        }

        @Override
        protected QSize tabSizeHint(final int s) {
            return new QSize(80, 28);
        }
    }
}

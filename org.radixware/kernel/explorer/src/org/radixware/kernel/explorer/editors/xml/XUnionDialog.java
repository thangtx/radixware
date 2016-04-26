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

import java.util.List;

import org.apache.xmlbeans.SchemaType;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QButtonGroup;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QDialogButtonBox.StandardButton;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;

class XUnionDialog extends QDialog {

    private final Ui_XUnionDialog ud = new Ui_XUnionDialog();
    private final IClientEnvironment environment;
    private QGroupBox radios = null;
    private QButtonGroup bg = null;
    private XRadio current = null;
    private XRadio previous = null;
    private QStackedWidget editors = null;
    private SchemaType[] types = null;
    private SchemaType currentType = null;
    private String strValue = null;

    XUnionDialog(final IClientEnvironment environment, final SchemaType[] members, final SchemaType ct, final String v, final QWidget parent, final boolean readonly) {
        super(parent);
        this.environment = environment;
        types = members;
        currentType = ct;
        strValue = v;
        setup();
        editors.setEnabled(!readonly);
        radios.setEnabled(!readonly);
    }

    private void setup() {
        ud.setupUi(this);
        this.setWindowIcon(ExplorerIcon.getQIcon(XmlEditor.Icons.UNION));
        this.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        this.setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);
        this.setWindowModality(Qt.WindowModality.ApplicationModal);
        QGridLayout vbox = new QGridLayout(this);

        radios = new QGroupBox(Application.translate("XmlEditor", "Types"));
        editors = new QStackedWidget();
        QVBoxLayout buts = new QVBoxLayout();
        QLayout edit = editors.layout();
        bg = new QButtonGroup();
        for (SchemaType t : types) {
            addRadioForType(t, edit, buts, bg);
        }
        radios.setLayout(buts);
        vbox.addWidget(radios);
        vbox.addWidget(editors);

        XRadio b = (XRadio) bg.buttons().get(0);
        if (currentType == null) {
            editors.setCurrentIndex(0);
        } else {
            boolean stop = false;
            int i = 0;
            String cName = getTypeName(currentType);
            while (i <= bg.buttons().size() - 1 && !stop) {
                XRadio i_but = (XRadio) bg.buttons().get(i);
                String tName = getTypeName(i_but.getType());
                if (tName.equals(cName)) {
                    stop = true;
                } else {
                    i++;
                }
            }
            b = (XRadio) bg.buttons().get(i);
            editors.setCurrentIndex(i);
        }
        b.setChecked(true);
        current = b;
        bg.buttonClicked.connect(this, "onRadioClicked(QAbstractButton)");

        QDialogButtonBox mainbuttons = new QDialogButtonBox(this);
        setupButtonBox(mainbuttons);
        vbox.addWidget(mainbuttons);
    }

    @SuppressWarnings("unused")
    private void onValueChanged(Object v) {
        strValue = v != null ? v.toString() : "";
    }

    @SuppressWarnings("unused")
    private void onRadioClicked(QAbstractButton r) {
        if (!r.text().equals(current.text())) {
            XRadio xr = (XRadio) r;
            List<QObject> buttons = radios.children();
            boolean stop = false;
            int i = 0;
            while (i <= buttons.size() - 1
                    && !stop) {
                if (buttons.get(i) instanceof XRadio) {
                    XRadio i_but = (XRadio) buttons.get(i);
                    if (i_but.text().equals(xr.text())) {
                        stop = true;
                    } else {
                        i++;
                    }
                } else {
                    i++;
                }
            }
            if (stop == true) {
                ValEditor current_ed = (ValEditor) editors.currentWidget();
                String v = current_ed.getValue() != null ? current_ed.getValue().toString() : "";
                try {
                    XEditorTools.setProperValueToEditor(xr.getEditor(), v);
                    editors.setCurrentIndex(i - 1);
                    current = xr;
                    currentType = xr.getType();
                } catch (IllegalArgumentException excep) {
                    Application.messageWarning(Application.translate("XmlEditor", "Type mismatch"), excep.toString());
                    previous.setChecked(true);
                }
            }
        }
    }

    private void setupButtonBox(QDialogButtonBox box) {
        QPushButton b;
        b = box.addButton(StandardButton.Ok);
        b.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_OK));
        b.clicked.connect(this, "onOKClicked()");

        b = box.addButton(StandardButton.Cancel);
        b.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_CANCEL));
        b.clicked.connect(this, "onCancelClicked()");

        box.setVisible(true);
    }

    @SuppressWarnings("unused")
    private void onOKClicked() {
        this.accepted.emit();
        close();
    }

    @SuppressWarnings("unused")
    private void onCancelClicked() {
        this.rejected.emit();
        close();
    }

    private void addRadioForType(SchemaType t, QLayout e, QLayout l, QButtonGroup bg) {
        XRadio item = new XRadio(t, this);
        l.addWidget(item);
        item.getEditor().valueChanged.connect(this, "onValueChanged(Object)");
        item.getEditor().setVisible(false);
        e.addWidget(item.getEditor());
        bg.addButton(item);
    }

    public String getCurrentValue() {
        return strValue;
    }

    public SchemaType getCurrentMember() {
        return currentType;
    }

    public void setPreviousRadio(XRadio p) {
        previous = p;
    }

    public static String getTypeName(SchemaType t) {
        String name = "";
        if (t.getName() != null) {
            name = t.getName().getLocalPart();
        } else {
            SchemaType base = t.getBaseType();
            name = base.getName().getLocalPart();
        }
        return name;
    }

    private class XRadio extends QRadioButton {

        private XUnionDialog owner = null;
        private ValEditor editor = null;
        private SchemaType type = null;

        public ValEditor getEditor() {
            return editor;
        }

        public SchemaType getType() {
            return type;
        }

        @SuppressWarnings("unused")
        private void onToggled(boolean t) {
            if (!t) {
                owner.setPreviousRadio(this);
            }
        }

        public XRadio(SchemaType t, XUnionDialog o) {
            owner = o;
            type = t;
            String text = XUnionDialog.getTypeName(t);
            this.setText(text);
            editor = XEditorTools.getRelevantEditor(environment, t);
            try {
                XEditorTools.setProperValueToEditor(editor, strValue);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            this.toggled.connect(this, "onToggled(boolean)");
        }
    }
}

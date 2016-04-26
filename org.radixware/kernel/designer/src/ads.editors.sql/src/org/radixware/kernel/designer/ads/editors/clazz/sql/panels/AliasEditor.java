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

package org.radixware.kernel.designer.ads.editors.clazz.sql.panels;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ProxyModalOpenInfo;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;
import org.radixware.kernel.designer.common.dialogs.utils.ModalObjectEditor;


public class AliasEditor extends ModalObjectEditor<String> {

    private String newAlias;
    private JTextField textField;
    private final String windowTitle;

    public AliasEditor() {
        this(NbBundle.getMessage(AliasEditor.class, "msg-table-alias"), NbBundle.getMessage(AliasEditor.class, "msg-change-table-alias"));
    }

    public AliasEditor(String labelString, String windowTitle) {
        textField = new JTextField();
        int h = textField.getFont().getSize();
        setMinimumSize(new Dimension(200, h + 10));
        setPreferredSize(new Dimension(200, h + 10));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        JLabel label = new JLabel(labelString);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(label);
        textField.setMaximumSize(new Dimension(Short.MAX_VALUE, h + 10));
        add(textField);
        this.windowTitle = windowTitle;
    }

    @Override
    protected String getTitle() {
        return windowTitle;
    }

    @Override
    protected void applyChanges() {
        newAlias = textField.getText();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        textField.setEditable(!readOnly);
    }

    @Override
    protected void afterOpen() {
        textField.setText(getObject());
        newAlias = getObject();
    }

    public String getAlias() {
        return newAlias;
    }

    public void setAlias(final String alias) {
        textField.setText(alias);
    }

    @Override
    public void requestFocus() {
        textField.requestFocus();
    }

    /**
     * Return non-null string if OK was pressed, false otherwise
     *
     * @param alias - initial text
     * @param labelText - text for label near the JTextField
     * @param windowTitle
     * @return
     */
    public static String editAlias(String alias, String labelText, String windowTitle) {
        AliasEditor aliasEditor = new AliasEditor(labelText, windowTitle);
        aliasEditor.open(alias, EditorOpenInfo.DEFAULT_EDITABLE);
        if (aliasEditor.showModal()) {
            return aliasEditor.getAlias();
        }
        return null;
    }
}

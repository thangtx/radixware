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

/*
 * 9/20/11 5:30 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.designer.common.dialogs.components.values.ContextableValueEditorPanel;
import org.radixware.kernel.designer.common.editors.jml.JmlEditor;


final class JmlValAsStrEditorPanel extends ContextableValueEditorPanel<Jml, Jml> {

    private JmlEditor editor;
    private boolean editable;

    public JmlValAsStrEditorPanel(Jml jml) {
        super();

        editable = true;
        open(jml);
    }

    @Override
    public Jml getValue() {
        return editor.getSource();
    }

    @Override
    public final void open(Jml context) {
        setLayout(new BorderLayout());
        editor = new JmlEditor();
        editor.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(editor, BorderLayout.CENTER);
        editor.open(context);
    }

    @Override
    public boolean isSetValue() {
        return true;
    }

    @Override
    public void commit() {
    }

    @Override
    public boolean isValueChanged() {
        return false;
    }

    @Override
    public void setValue(Jml value) {
    }

    @Override
    public boolean isOpened() {
        return getValue() != null;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public void update() {
    }

    @Override
    public void open(Jml context, Jml currValue) {
        open(context);
    }

    @Override
    public Jml getNullValue() {
        return null;
    }
}

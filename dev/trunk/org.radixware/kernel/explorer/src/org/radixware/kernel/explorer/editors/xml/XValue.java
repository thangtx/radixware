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

import org.w3c.dom.Node;

import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;

class XValue extends XTreeElement {

    public static final String COMMENT = "Comment";
    public static final String PROCINST = "Processing instruction";
    private Node cpOwner = null;

    public XValue(final TreeWindow tw, final Node cp) {
        super(tw);
        cpOwner = cp;
        final ValEditor editor = XEditorTools.getRelevantEditor(tw.getEnvironment(), null);
        XEditorTools.setProperValueToEditor(editor, cp.getNodeValue());
        editor.changeStateForGrid();
        editor.valueChanged.connect(this, "changeValue(Object)");
        setEditor(editor);

        QFont f = new QFont();
        f.setItalic(true);
        setFont(0, f);
        if (cp.getNodeType() == Node.COMMENT_NODE) {
            setText(0, Application.translate("XmlEditor", COMMENT));
            setForeground(0, new QBrush(QColor.darkGreen));
        }
        if (cp.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
            setText(0, Application.translate("XmlEditor", PROCINST));
            setForeground(0, new QBrush(QColor.darkBlue));
        }
    }

    @Override
    public boolean isExternalTypeSystem() {
        return false;
    }

    @Override
    public SchemaType getSchemaType() {
        return node.schemaType();
    }

    @SuppressWarnings("unused")
    protected void changeValue(Object value) {
        String v = (String) getEditor().getValue();
        if (cpOwner != null) {
            cpOwner.setNodeValue(v);
            applyChanges();
            tw.setModified(true);
        }
    } 

    @Override
    protected Node getNodeForXPath() {
        return cpOwner;
    }

    public String getTextValue() {
        return cpOwner != null ? cpOwner.getNodeValue() : null;
    }
}

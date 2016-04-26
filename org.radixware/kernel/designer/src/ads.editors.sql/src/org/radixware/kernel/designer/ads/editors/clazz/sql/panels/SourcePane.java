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

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;


public class SourcePane extends JEditorPane {

    public SourcePane() {
        setEditorKit(new HTMLEditorKit());
        setEditable(false);
        setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(SourcePane.class, "msg-source")));
    }

    public void setSource(DdsColumnDef column) {
        StringBuilder source = new StringBuilder();
        if (column != null) {
            source.append("<html>");
            source.append("<b>Table:</b>\t");
            source.append(column.getOwnerDefinition().getName());
            source.append("<br>");
            source.append("<b>Column:</b>\t");
            source.append(column.getName());
            source.append("<br>");
            source.append("<b>Value Type:</b>\t");
            source.append(column.getValType().getName());
            source.append("</html>");
        } else {
            source.append("<html><b>null</b></html>");
        }
        setText(source.toString());
    }
}

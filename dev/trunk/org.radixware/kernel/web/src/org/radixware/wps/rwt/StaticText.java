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
package org.radixware.wps.rwt;

import java.awt.Color;
import org.radixware.kernel.common.html.Div;

public class StaticText extends UIObject {

    private TableContainer textContainer;

    public StaticText() {
        super(new Div());
        html.setCss("overflow", "auto");
        this.textContainer = new TableContainer();
        this.textContainer.html.setCss("width", "100%");
        this.html.add(textContainer.html);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        } else {
            return textContainer.findObjectByHtmlId(id);
        }
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        textContainer.visit(visitor);
    }

    public void setText(String text) {
        textContainer.clearRows();
        append(text, null);
    }

    public void append(String text, Color textColor) {
        if (text == null) {
            return;
        }
        String[] rows = text.split("\n");
        for (String line : rows) {
            TableContainer.Row row = textContainer.addRow();
            final Label label = new Label(line, true);
            label.setForeground(textColor);
            label.setTextWrapDisabled(true);
            row.addCell().add(label);
        }
    }

    public void appendButton(ButtonBase button) {
        TableContainer.Row row = textContainer.addRow();
        row.addCell().add(button);
        button.html.setCss("float", "right");
        button.html.setCss("padding-right", "10px");
    }

    public String getText() {
        StringBuilder result = new StringBuilder();
        for (TableContainer.Row row : textContainer.getRows()) {
            Label l = (Label) row.getCells().get(0).getChildren().get(0);
            if (result.length() == 0) {
                result.append(l.getText());
            } else {
                result.append("\n").append(l.getText());
            }
        }
        return result.toString();
    }
    
    public int getLinesCount(){
        return textContainer.getRows().size();
    }

    @Override
    public void setSizePolicy(final SizePolicy hPolicy, final SizePolicy vPolicy) {
        super.setSizePolicy(hPolicy, vPolicy); 
        textContainer.setSizePolicy(hPolicy, vPolicy);
    }

    @Override
    public void setVSizePolicy(final SizePolicy vSizePolicy) {
        super.setVSizePolicy(vSizePolicy);
        textContainer.setVSizePolicy(vSizePolicy);
    }

    @Override
    public void setHSizePolicy(final SizePolicy hSizePolicy) {
        super.setHSizePolicy(hSizePolicy);
        textContainer.setHSizePolicy(hSizePolicy);
    }
    
    
}

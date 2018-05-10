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
import org.radixware.kernel.common.html.Html;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.widgets.IButton;


public class CheckBox extends ButtonBase {

    private Html checkBox = new Html("input");
    private boolean selected = false;

    public interface SelectionStateListener {

        public void onSelectionChange(CheckBox cb);
    }
    private final SelectionStateListener defStateListener = new SelectionStateListener() {

        @Override
        public void onSelectionChange(CheckBox cb) {
            List<SelectionStateListener> lss;
            synchronized (listeners) {
                lss = new ArrayList<>(listeners);
            }
            for (SelectionStateListener l : lss) {
                l.onSelectionChange(cb);
            }
        }
    };
    private final List<SelectionStateListener> listeners = new LinkedList<>();
    private Html row;
    private Html checkCell, textCell;

    public CheckBox() {
        super(new Html("table"));
        row = new Html("tr");
        html.add(row);
        checkCell = new Html("td");
        textCell = new Html("td");
        textCell.setCss("width", "100%");
        row.add(checkCell);
        row.add(textCell);
        html.setCss("overflow", "hidden");
        checkCell.add(checkBox);
        html.layout("$RWT.checkBox.layout");
        setSelected(selected);
        setDefaultClassName("rwt-ui-element-text");
        setInputType("checkbox");
        addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                setSelected(!selected);
            }
        });
    }

    public final void setInputType(String type){
        checkBox.setAttr("type", type);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject o = super.findObjectByHtmlId(id);
        if (o != null) {
            return o;
        } else if (checkBox.getId().equals(id)) {
            return this;
        } else {
            return null;
        }
    }

    public boolean isSelected() {
        return checkBox.getAttr("checked") != null;
    }

    public void setSelected(boolean selected) {
        if (selected != this.selected) {
            if (selected) {
                checkBox.setAttr("checked", "checked");
            } else {
                checkBox.setAttr("checked", null);
            }
            this.selected = selected;
            defStateListener.onSelectionChange(this);
        }
    }

    public void addSelectionStateListener(SelectionStateListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeSelectionStateListener(SelectionStateListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    @Override
    protected Html createLabelElement() {
        Html le = super.createLabelElement();
        //  le.setCss("white-space", "nowrap");
        return le;
    }

    @Override
    protected Html getContentElement() {
        return textCell;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        if (isEnabled) {
            checkBox.setAttr("disabled", null);
            checkBox.removeClass("ui-state-disabled");
        } else {
            checkBox.setAttr("disabled", true);
            checkBox.addClass("ui-state-disabled");
        }
    }

    @Override
    public void setForeground(final Color c) {
        super.setForeground(c);
        checkBox.setCss("color", c == null ? null : color2Str(c));
    }
    
    
}

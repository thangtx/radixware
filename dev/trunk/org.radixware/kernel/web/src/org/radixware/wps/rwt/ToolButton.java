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

import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.views.RwtAction.IActionPresenter;


public class ToolButton extends ButtonBase implements IActionPresenter, IToolButton {

    private Action action;

    public ToolButton() {
        this(new Div());
    }

    public ToolButton(Action action) {
        this();
        this.action = action;
        actionStateChanged(action);
        addClickHandler(new ClickHandler() {

            @Override
            public void onClick(IButton source) {
                ToolButton.this.action.trigger();
            }
        });
    }

    protected ToolButton(Html element) {
        super(element);
        getContentElement().setCss("width", "100%");
        getContentElement().setCss("height", "100%");
        html.addClass("rwt-tool-button");
        html.setAttr("tabIndex","-1");
        html.layout("$RWT.toolButton.layout");
        html.setAttr("rwt_f_minsize", "$RWT.toolButton.size");
        setHeight(25);
        setPreferredHeight(25);
        html.setCss("float", "left");
        html.markAsChoosable();
    }

    @Override
    protected Html createLabelElement() {
        Html le = new Html("a");
        le.setAttr("href", "#");
        //le.setCss("float", "left");
        le.setCss("white-space", "nowrap");
        return le;
    }

    @Override
    public final void actionStateChanged(Action a) {
        this.setIcon(a.getIcon());
        this.setToolTip(a.getToolTip());
        this.setVisible(a.isVisible());
        this.setEnabled(a.isEnabled());
        this.setText(a.getText());
    }

    public Action getAction() {
        return action;
    }
    private ToolButtonPopupMode popupMode;
    private boolean autoRaise;

    @Override
    public ToolButtonPopupMode getPopupMode() {
        return popupMode;
    }

    @Override
    public boolean isAutoRaise() {
        return autoRaise;
    }

    @Override
    public void setAutoRaise(boolean isAutoRaise) {
        autoRaise = isAutoRaise;
    }

    @Override
    public void setPopupMode(ToolButtonPopupMode mode) {
        popupMode = mode;
    }
}

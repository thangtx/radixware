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

package org.radixware.wps;

import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.UIObject;



class ProgressHandleView extends UIObject {

    private Label label = new Label("");
    private PushButton cancelButton = null;

    public ProgressHandleView() {
        super(new Div());
        html.addClass("rwt-progress-handle");
        this.html.add(label.getHtml());
        Html content = new Div();
        Html marker = new Div();
        this.html.add(content);
        content.add(marker);
        content.setCss("float", "top");
        content.setCss("position", "relative");
        content.setCss("border", "solid 1px black");
        content.setCss("overflow", "hidden");
        content.setCss("height", "16px");
        content.setCss("padding", "0px");
        content.setCss("margin", "0px");
        content.addClass("ui-corner-all");
        marker.setCss("position", "absolute");
        marker.setCss("padding", "0px");
        marker.addClass("ui-corner-all");
        marker.setCss("float", "none");
        marker.setCss("margin", "0px");
        //marker.setCss("background-color", "red");
        marker.addClass("rwt-progress-bar");
        content.addClass("rwt-progress-bar-background");
        setVCoverage(100);
        setHeight(35);
        getHtml().setCss("float", "top");
        getHtml().layout("$RWT.dialog.progressHandle.layout");
    }
    private IProgressHandle currentHandle;

    void accept(IProgressHandle ph) {
        setText(ph.getText());
        setTitle(ph.getTitle());
        setValue(ph.getValue());
        setMaximumValue(ph.getMaximumValue());
        setCancellable(ph.canCancel());
        setCancellable(false);
        this.currentHandle = ph;
    }

    public void setTitle(String title) {
        html.setInnerText(title);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void setMaximumValue(int value) {
        if (value <= 0) {
            getHtml().setAttr("amount", null);
        } else {
            getHtml().setAttr("amount", value);
        }
    }

    public void setCancellable(boolean isCancellable) {
        if (!isCancellable) {
            if (cancelButton != null) {
                this.html.remove(cancelButton.getHtml());
                cancelButton = null;
            }
        } else {
            if (cancelButton == null) {
                cancelButton = new PushButton(getEnvironment().getMessageProvider().translate("MessageBox", IMessageBox.StandardButton.getTitle(EDialogButtonType.CANCEL, getEnvironment())));
                this.html.add(cancelButton.getHtml());
                cancelButton.setParent(this);
                cancelButton.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(IButton source) {
                        cancelProgress();
                    }
                });
            }
        }
    }

    private void cancelProgress() {
        if (currentHandle != null && currentHandle.canCancel()) {
            currentHandle.cancel();
            if (cancelButton != null) {
                cancelButton.setEnabled(false);
            }
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        if (cancelButton != null) {
            UIObject result = cancelButton.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
        }
        return super.findObjectByHtmlId(id);
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        if (cancelButton != null) {
            cancelButton.visit(visitor);
        }
        label.visit(visitor);
    }

    public void setValue(int value) {

        getHtml().setAttr("current", value);

    }
}

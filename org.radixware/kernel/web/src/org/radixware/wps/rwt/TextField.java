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
import java.util.Map;
import org.radixware.kernel.common.html.CssDistributor;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.ICssStyledItem;
import org.radixware.kernel.common.utils.Utils;
import static org.radixware.wps.rwt.Alignment.RIGHT;
import org.radixware.wps.text.ECssPropertyName;
import org.radixware.wps.text.WpsTextOptions;


public class TextField extends AbstractTextField {

    private static enum EAttrName {

        InputType("inputType"),
        MaxDigitsCount("maxDigitsCount"),
        IsPassword("isPassword");
        private final String attrName;

        private EAttrName(final String attrName) {
            this.attrName = attrName;
        }

        public String getAttrName() {
            return attrName;
        }
    }
    protected Html input = new Html("input");
    private final CssDistributor textOptionsDistributor;
    private boolean isEnabled = true;

    public TextField() {
        this("");
    }

    public TextField(String text) {
        super(new Div());
        textOptionsDistributor = new CssDistributor(input, input, html);
        html.setCss("margin-right", "3px");
        html.setCss("vertical-align", "middle");
        Html container = new Div();
        container.setCss("padding-right", "3px");
        this.html.add(container);
        container.add(input);
        container.setCss("vertical-align", "middle");
        input.setCss(Html.CSSRule.WIDTH, "100%");
        input.setCss(Html.CSSRule.HEIGHT, "100%");
        input.setCss("vertical-align", "middle");
        input.addClass("rwt-ui-element");
        html.addClass("rwt-text-field");
        html.layout("$RWT.textField.layout");

        if (text != null && !text.isEmpty()) {
            setText(text);
        }

        input.setAttr("onfocus", "$RWT.textField.focusIn");
        input.setAttr("onblur", "$RWT.textField.focusOut");
        input.setAttr("onkeypress", "$RWT.textField.keyPress");
        input.setAttr("onpaste", "$RWT.textField.pasteText");
        input.setAttr("oncut", "$RWT.textField.cutText");

        input.setAttr("onclick", "$RWT.textField.onCursorPositionChanged");
        input.setAttr("onselect", "$RWT.textField.onCursorPositionChanged");
        input.setAttr("onmousedown", "$RWT.textField.onCursorPositionChanged");

        input.setAttr("onkeydown", "$RWT.textField.keyDown");
        input.setAttr("ondrop", "$RWT.textField.drop");
        input.setAttr("ondragover", "$RWT.textField.dragOver");
        input.setAttr("ondragenter", "$RWT.textField.dragEnter");
        input.setAttr("ondragleave", "$RWT.textField.dragLeave");
        InputFormat.Factory.freeInputFormat().writeToHtml(html);
        super.setClientHandler("requestFocus", "$RWT.textField.requestFocus");
    }

    public void applyTextOptions(final WpsTextOptions options) {
        if (options != null) {
            final Map<ECssPropertyName, String> css = options.getCssPropertyValues();
            html.setCss(ECssPropertyName.BACKGROUND_COLOR.getPropertyName(), css.get(ECssPropertyName.BACKGROUND_COLOR));
            input.getParent().setCss(ECssPropertyName.BACKGROUND_COLOR.getPropertyName(), css.get(ECssPropertyName.BACKGROUND_COLOR));
            for (Map.Entry<ECssPropertyName, String> entry : css.entrySet()) {
                input.setCss(entry.getKey().getPropertyName(), entry.getValue());
            }
        }
    }

    public Alignment getTextAlign() {
        String val = input.getCss("text-align");
        if ("right".equals(val)) {
            return Alignment.RIGHT;
        } else if ("center".equals(val)) {
            return Alignment.CENTER;
        } else {
            return Alignment.LEFT;
        }
    }

    public void setTextAlign(Alignment alignment) {
        if (alignment == null) {
            return;
        }
        switch (alignment) {
            case LEFT:
                input.setCss("text-align", null);
                break;
            case RIGHT:
                input.setCss("text-align", "right");
                break;
            case CENTER:
                input.setCss("text-align", "center");
                break;
        }
    }

    public void setClientOnFocusHandler(final String method_name) {
        input.setAttr("rwt_f_focusIn", method_name);
    }

    public void setClientOnBlurHandler(final String method_name) {
        input.setAttr("rwt_f_focusOut", method_name);
    }

    @Override
    protected void updateTextInHtml(String text) {
        this.input.setAttr("value", text);
    }

    public final void editText(final String text) {
        this.input.setAttr("value", text);
    }

    @Override
    protected final String getTextFromHtml() {
        return this.input.getAttr("value");
    }

    public boolean isPassword() {
        return "true".equals(this.input.getAttr(EAttrName.IsPassword.getAttrName()));
    }

    public void setPassword(boolean isPassword) {
        this.html.setAttr(EAttrName.IsPassword.getAttrName(), isPassword ? "true" : null);
    }

    public final void setMaxLength(int maxLength) {
        input.setAttr("maxLength", maxLength);
    }

    public final void setInputFormat(final InputFormat inputFormat) {
        inputFormat.writeToHtml(html);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        return input.getId().equals(id) ? this : null;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        if (isEnabled) {
            input.setAttr("disabled", null);
            input.removeClass("ui-state-disabled");
        } else {
            input.setAttr("disabled", true);
            input.addClass("ui-state-disabled");
        }
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean isReadOnly() {
        return Utils.equals(input.getAttr("readonly"), "true");
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if (readOnly) {
            input.setAttr("readonly", "true");
        } else {
            input.setAttr("readonly", null);
        }
    }

    @Override
    public void setClientHandler(String customEventName, String code) {
        super.setClientHandler(input, customEventName, code);
    }

    @Override
    protected ICssStyledItem getBackgroundHolder() {
        return textOptionsDistributor;
    }

    @Override
    protected ICssStyledItem getForegroundHolder() {
        return textOptionsDistributor;
    }

    @Override
    protected ICssStyledItem getFontOptionsHolder() {
        return textOptionsDistributor;
    }
}

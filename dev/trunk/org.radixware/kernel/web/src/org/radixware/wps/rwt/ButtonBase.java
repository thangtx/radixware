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
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.wps.icons.WpsIcon;
import static org.radixware.wps.rwt.UIObject.color2Str;

public abstract class ButtonBase extends UIObject implements IButton {

    private int iconWidth = 20;
    private int iconHeight = 20;

    public ButtonBase(Html html) {
        super(html);
        this.content = html;
    }
    private Html content;

    protected ButtonBase(Html html, Html content) {
        super(html);
        if (content != null) {
            html.add(content);
            this.content = content;
        }
    }

    public String getText() {
        if (labelElement != null) {
            return labelElement.getInnerText();
        } else {
            return "";
        }
    }

    protected Html getContentElement() {
        return content;
    }

    public void setText(String text) {
        if (text == null || text.isEmpty()) {
            if (labelElement != null) {
                getContentElement().remove(labelElement);
                labelElement = null;
            }
        } else {
            if (labelElement == null) {
                labelElement = createLabelElement();
                labelElement.addClass("rwt-button-label");
                if (textColor != null) {
                    labelElement.setCss("color", textColor == null ? null : color2Str(textColor));
                }
                if (labelElement != getContentElement()) {
                    getContentElement().add(labelElement);
                }
            }
            labelElement.setInnerText(text.replace("&", ""));
            if (labelElement.getCss("display") == null) {
                labelElement.setCss("display", "inline");
            }
        }
        updateIconPadding();
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/button.js"};
    }

    private void setUpDomClick(boolean set) {
        if (set) {
            html.setAttr("onclick", "default");
        } else {
            html.setAttr("onclick", null);
        }
    }

    private class DefaultClickHandler implements ClickHandler {

        private final List<ClickHandler> handlers = new LinkedList<>();

        @Override
        public void onClick(IButton button) {
            if (isEnabled()) {
                synchronized (handlers) {
                    for (ClickHandler handler : handlers) {
                        handler.onClick(button);
                    }
                }
            }
        }

        void add(ClickHandler handler) {
            synchronized (handlers) {
                if (!handlers.contains(handler)) {
                    handlers.add(handler);
                    setUpDomClick(true);
                }
            }
        }

        void remove(ClickHandler handler) {
            synchronized (handlers) {
                handlers.remove(handler);
                if (handlers.isEmpty()) {
                    setUpDomClick(false);
                }
            }
        }

        void clear() {
            synchronized (handlers) {
                handlers.clear();
                setUpDomClick(false);
            }
        }
    }
    private DefaultClickHandler defaultHandler;
    private WpsIcon icon;
    private Html iconElement;
    private Html labelElement;
    private Color textColor;

    @Override
    public void addClickHandler(ClickHandler handler) {
        if (handler != null) {
            synchronized (this) {
                if (defaultHandler == null) {
                    defaultHandler = new DefaultClickHandler();
                }
            }
            defaultHandler.add(handler);
        }
    }

    @Override
    public void removeClickHandler(ClickHandler handler) {
        synchronized (this) {
            if (defaultHandler != null) {
                defaultHandler.remove(handler);
            }
        }
    }

    @Override
    public void clearClickHandlers() {
        synchronized (this) {
            if (defaultHandler != null) {
                defaultHandler.clear();
            }
        }
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
            click();
        } else {
            super.processAction(actionName, actionParam);
        }
    }

    public void click() {
        if (isEnabled()) {
            defaultHandler.onClick(this);
        }
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public String getTitle() {
        return getText();
    }

    @Override
    public void setIcon(Icon icon) {
        if (icon instanceof WpsIcon) {

            this.icon = (WpsIcon) icon;
            if (iconElement != null) {
                iconElement.setAttr("src", null);
            }
            updateIconSettings();
//            iconElement.setAttr("src", this.icon.getURI(this));
//
//            iconElement.setCss("height", getIconHeight() + "px");
//            iconElement.setCss("width", getIconWidth() + "px");
//            iconElement.setAttr("height", getIconHeight() + "px");
//            iconElement.setAttr("width", getIconWidth() + "px");
            iconElement.setCss("vertical-align", "middle");
            updateIconPadding();
        } else if (iconElement != null) {
            getContentElement().remove(iconElement);
            iconElement = null;
            html.setCss("min-height", null);
        }
    }

    private void updateIconPadding() {
        if (iconElement != null) {
            final boolean hasText
                    = labelElement != null && labelElement.getInnerText() != null && !labelElement.getInnerText().isEmpty();
            iconElement.setCss("padding-right", hasText ? "4px" : null);
            iconElement.setCss("display", hasText ? "inline" : "block");
        }
    }

    private void updateIconSettings() {
        if (icon != null) {
            if (iconElement == null) {
                iconElement = new Html("img");
                iconElement.setAttr("onmousedown", "$RWT.defaultMousedown");
                //iconElement.layout("$RWT.buttonIcon.layout");                
                getContentElement().add(0, iconElement);
            }
            if (iconElement.getAttr("src") == null) {
                iconElement.setAttr("src", this.icon.getURI(this));
            }
            iconElement.setCss("height", iconHeight + "px");
            iconElement.setCss("width", iconWidth + "px");
            iconElement.setAttr("height", iconHeight);
            iconElement.setAttr("width", iconWidth);
        }
    }

    protected Html createLabelElement() {
        Html html = new Html("a");
        html.addClass("rwt-button-label");
        html.setCss("vertical-align", "middle");
        if (textColor != null) {
            html.setCss("color", color2Str(textColor));
        }
        return html;
    }

    protected final int getIconWidth() {
        return iconWidth;
    }

    protected final int getIconHeight() {
        return iconHeight;
    }

    public void setIconHeight(int iconHeight) {
        if (this.iconHeight != iconHeight) {
            this.iconHeight = iconHeight;
            updateIconSettings();
        }
    }

    public void setIconWidth(int iconWidth) {
        if (this.iconWidth != iconWidth) {
            this.iconWidth = iconWidth;
            updateIconSettings();
        }
    }

    public void setIconSize(int width, int height) {
        if (this.iconWidth != width || this.iconHeight != height) {
            this.iconHeight = height;
            this.iconWidth = width;
            updateIconSettings();
        }
    }

    @Override
    public void setTitle(String text) {
        setText(text);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        } else {
            if (getContentElement().getId().equals(id)) {
                return this;
            } else if (iconElement != null && iconElement.getId().equals(id)) {
                return this;
            } else if (labelElement != null && labelElement.getId().equals(id)) {
                return this;
            } else {
                return null;
            }
        }
    }

    @Override
    public void addAction(Action action) {
    }

    public boolean isTextWrapDisabled() {
        return "nowrap".equals(html.getCss("white-space"));
    }

    public void setTextWrapDisabled(boolean disable) {
        html.setCss("white-space", disable ? "nowrap" : null);
    }

    @Override
    public void setForeground(final Color c) {
        if (labelElement != null) {
            labelElement.setCss("color", c == null ? null : color2Str(c));
        }
        textColor = c;
    }

    @Override
    public Color getForeground() {
        return textColor;
    }
}

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
import java.util.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox.StandardButton;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialogWithStandardButtons;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.WrongFormatException;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Table;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.icons.images.WsIcons;

public class Dialog extends UIObject implements IDialogWithStandardButtons {

    private Html header = new Div();
    private Html title = new Html("label");
    private String defaultAcceptAction;
    private String defaultRejectAction;
    private IClientEnvironment environment;
    private final EventSupport dialogEventSupport = new EventSupport(this);
    private boolean buttonBoxVisible = true;
    private DialogGeometry currentGeometry;
    private String key = "";
    private boolean isResizable = true;
    private boolean internalResize;

    private final static class Body extends Container {

        private final Container innerBody = new Container();//imitation of padding=2

        public Body(Dialog parent) {
            super();
            setParent(parent);
            html.setCss("overflow", "hidden");
            html.setCss("width", "100%");
            html.setCss("position", "relative");
            html.setCss("display", "block");
            html.setAttr("role", "outerBody");

            innerBody.getHtml().setCss("width", "100%");
            innerBody.getHtml().setCss("height", "100%");
            innerBody.getHtml().setAttr("role", "innerBody");
            innerBody.setTop(2);
            innerBody.setLeft(2);
            add(innerBody);
        }

        @Override
        public void add(final UIObject child) {
            if (child == innerBody) {
                super.add(child);
            } else {
                innerBody.add(child);
            }
        }

        @Override
        public void add(final int index, final UIObject child) {
            if (child == innerBody) {
                super.add(index, child);
            } else {
                innerBody.add(index, child);
            }
        }

        @Override
        public void clear() {
            innerBody.clear();
        }

        @Override
        public UIObject findObjectByHtmlId(final String id) {
            return innerBody.findObjectByHtmlId(id);
        }

        @Override
        public List<UIObject> getChildren() {
            return innerBody.getChildren();
        }

        @Override
        public void remove(final UIObject child) {
            innerBody.remove(child);
        }

        @Override
        public void visit(final Visitor visitor) {
            innerBody.visit(visitor);
        }
    }
    public static final String CLOSE_ACTION_CLOSE = "Close";
    public static final String CLOSE_ACTION_YES = "Yes";
    public static final String CLOSE_ACTION_NO = "No";
    public static final String CLOSE_ACTION_OK = "Ok";
    public static final String CLOSE_ACTION_CANCEL = "Cancel";

    public interface ApplyActionHandler {

        public void apply(String action, Object data);
    }

    public interface CloseActionHandler {

        public boolean canClose(String action, Object data);

        public void closed(String action, Object data);
    }

    public String getEscapeAction() {
        return defaultRejectAction;
    }

    public void setEscapeAction(final String action) {
        defaultRejectAction = action == null ? null : action.replace("&", "");
        html.setAttr("escapeAction", defaultRejectAction);
    }

    public void setEscapeAction(final EDialogButtonType type) {
        setEscapeAction(type == null ? null : StandardButton.getTitle(type, getEnvironment()));
    }

    public void setEscapeAction(final StandardButton button) {
        setEscapeAction(button == null ? null : button.getTitle(getEnvironment()));
    }

    public String getDefaultAction() {
        return defaultAcceptAction;
    }

    public void setDefaultAction(final String action) {
        defaultAcceptAction = action == null ? null : action.replace("&", "");
        html.setAttr("defaultAction", defaultAcceptAction);
    }

    public void setDefaultAction(final EDialogButtonType type) {
        setDefaultAction(type == null ? null : StandardButton.getTitle(type, getEnvironment()));
    }

    public void setDefaultAction(final StandardButton button) {
        setDefaultAction(button == null ? null : button.getTitle(getEnvironment()));
    }

    protected void setupDefaultButtons(Set<EDialogButtonType> buttons) {
        if (buttons == null) {
            return;
        }
        if (getEscapeAction() == null) {
            if (buttons.size() == 1) {
                EDialogButtonType defaultButton = buttons.iterator().next();
                setEscapeAction(defaultButton);
            } else {
                EDialogButtonType[] candidatesByPryority = new EDialogButtonType[]{
                    EDialogButtonType.CANCEL,
                    EDialogButtonType.NO,
                    EDialogButtonType.CLOSE,
                    EDialogButtonType.NO_TO_ALL};
                for (EDialogButtonType candidate : candidatesByPryority) {
                    if (buttons.contains(candidate)) {
                        setEscapeAction(candidate);
                        break;
                    }
                }
            }
        }
        if (getDefaultAction() == null) {
            if (buttons.size() == 1) {
                EDialogButtonType defaultButton = buttons.iterator().next();
                setDefaultAction(defaultButton);
            } else {
                EDialogButtonType[] candidatesByPryority = new EDialogButtonType[]{
                    EDialogButtonType.OK,
                    EDialogButtonType.YES,
                    EDialogButtonType.APPLY,
                    EDialogButtonType.YES_TO_ALL};
                for (EDialogButtonType candidate : candidatesByPryority) {
                    if (buttons.contains(candidate)) {
                        setDefaultAction(candidate);
                        break;
                    }
                }
            }
        }
    }

    private class Buttons extends AbstractContainer {

        private final Map<Object, IPushButton> buttonsByData = new HashMap<>();

        public Buttons(Dialog parent) {
            super(new Div());
            setParent(parent);
            html.setCss("width", "100%");
            html.setCss("position", "relative");
            html.setAttr("role", "buttons");
            html.setCss("display", "block");
            this.html.addClass("ui-corner-bottom");
            html.removeClass("rwt-ui-background");
            setHeight(30);
        }

        public void addCloseAction(String name, final DialogResult result) {
            addCloseAction(name, result, null, null, null, false);
        }

        public void addCloseAction(IPushButton button, final DialogResult result) {
            addCloseAction(button.getObjectName(), button, result, null, null, null, false);
        }

        public void addCloseAction(final String name, IPushButton button, final DialogResult result, final CloseActionHandler handler, final ApplyActionHandler applyHandler, final Object data, boolean custom) {
            final boolean exitOnClick;
            switch (result) {
                case REJECTED:
                    if (defaultRejectAction == null) {
                        setEscapeAction(name);
                    }
                    exitOnClick = true;
                    break;
                case ACCEPTED:
                    if (defaultAcceptAction == null) {
                        setDefaultAction(name);
                    }
                    exitOnClick = true;
                    break;
                case APPLY:
                case NONE:
                    exitOnClick = false;
                    break;
                default:
                    exitOnClick = false;

            }
            button.addClickHandler(new IButton.ClickHandler() {
                @Override
                public void onClick(IButton button) {
                    if (exitOnClick) {
                        if (handler != null) {
                            if (!handler.canClose(name, data)) {
                                return;
                            }

                        }
                        if (Dialog.this.closeImpl(name, result)) {
                            if (handler != null) {
                                handler.closed(name, data);
                            }
                        }
                    } else {
                        if (handler instanceof ApplyActionHandler) {
                            ((ApplyActionHandler) handler).apply(name, data);
                        }

                        if (applyHandler != null) {
                            applyHandler.apply(name, data);
                        }
                        dialogButtonAction(name);
                    }
                }
            });

            ((ButtonBase) button).html.setAttr("action", name == null ? null : name.replace("&", ""));
            if (custom && result == DialogResult.APPLY) {
                ((ButtonBase) button).html.setCss("float", "left");
            } else {
                ((ButtonBase) button).html.setCss("float", "right");
            }
            ((ButtonBase) button).html.setCss("position", "relative");
            add(0, (ButtonBase) button);
            if (data != null) {
                buttonsByData.put(data, button);
            }
            if (getEscapeAction() == null && (CLOSE_ACTION_CANCEL.equals(name) || CLOSE_ACTION_CLOSE.equals(name))) {
                setEscapeAction(name);
            }
            if (button.isDefault()) {
                setDefaultAction(name);
            }
            if (button.getObjectName()==null || button.getObjectName().isEmpty()){
                button.setObjectName("rx_dlg_btn_"+name);
            }
            if (buttonBoxVisible) {
                showButtonBox();
            }
        }

        @Override
        public void clear() {
            super.clear();
            for (IPushButton button: buttonsByData.values()){
                if (button instanceof UIObject){
                    remove((UIObject) button);
                }
            }
            buttonsByData.clear();
        }

        public void removeCloseActionWithData(final Object data) {
            if (buttonsByData.containsKey(data)) {
                remove((UIObject) buttonsByData.get(data));
                buttonsByData.remove(data);
            }
        }

        public IPushButton getCloseActionByData(final Object data) {
            return buttonsByData.get(data);
        }

        public IPushButton getCloseActionByTitle(final String title) {
            final String match = title.replace("&", "");
            for (UIObject obj : getChildren()) {
                if (obj instanceof IPushButton) {
                    String action = obj.getHtml().getAttr("action");
                    if (Utils.equals(match, action)) {
                        return (IPushButton) obj;
                    }
                }
            }
            return null;
        }

        public IPushButton getCloseActionByObjectName(final String objectName) {
            for (IPushButton button : buttonsByData.values()) {
                if (objectName.equals(button.getObjectName())) {
                    return button;
                }
            }
            return null;
        }

        public PushButton addCloseAction(final String name, final DialogResult result, final CloseActionHandler handler, final ApplyActionHandler applyHandler, final Object data, boolean custom) {
            PushButton pb = new PushButton(name);
            pb.setTextWrapDisabled(true);
            addCloseAction(name.replace("&", ""), pb, result, handler, applyHandler, data, custom);
            return pb;
        }

        public boolean isActionEnabled(String action) {
            for (UIObject obj : getChildren()) {
                if (obj.getHtml().getAttr("action").equals(action)) {
                    return obj.isEnabled();
                }
            }
            return false;
        }

        public void setActionEnabled(String action, boolean enabled) {
            for (UIObject obj : getChildren()) {
                if (obj.getHtml().getAttr("action").equals(action)) {
                    obj.setEnabled(enabled);
                    break;
                }
            }
        }

        public boolean isActionVisible(String action) {
            for (UIObject obj : getChildren()) {
                if (obj.getHtml().getAttr("action").equals(action)) {
                    return obj.isVisible();
                }
            }
            return false;
        }

        public void setActionVisible(String action, boolean visible) {
            for (UIObject obj : getChildren()) {
                if (obj.getHtml().getAttr("action").equals(action)) {
                    obj.setVisible(visible);
                    break;
                }
            }
        }
    }
    private Body contentPanel = new Body(this);
    private Buttons buttons = new Buttons(this);
    private RwtMenuBar menuBar = new RwtMenuBar(this);
    private DialogResult result = DialogResult.REJECTED;
    private IDialogDisplayer displayer;
    private boolean wasClosed = false;
    private boolean isModalExecuting;
    private Html seHandle;
    private Html handleBar;
    private ButtonBase headerCloseButton = new ButtonBase(new Html("div")) {
    };

    {
        headerCloseButton.html.markAsChoosable();
        headerCloseButton.setIcon(WsIcons.WINDOW_CLOSE);
        headerCloseButton.setWidth(17);
        headerCloseButton.setHeight(17);
        headerCloseButton.setIconSize(16, 16);
        headerCloseButton.html.setCss("float", "right");
        headerCloseButton.html.setCss("cursor", "pointer");
        headerCloseButton.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                Dialog.this.close(DialogResult.REJECTED);
            }
        });

    }

    public interface CloseButtonListener {

        public void onClose(EDialogButtonType button, DialogResult result);
    }

    public interface BeforeCloseButtonListener {

        public boolean beforeClose(EDialogButtonType button, DialogResult result);
    }

    public interface ApplyButtonListener {

        public void onApply(EDialogButtonType button);
    }

    public interface CustomActionListener {

        public void onAction(String action);
    }

    private static class StandardButtonsHandler implements CloseActionHandler, ApplyActionHandler {

        private final List<CloseButtonListener> listeners = new LinkedList<>();
        private final List<BeforeCloseButtonListener> beforeCloseListeners = new LinkedList<>();
        private final List<ApplyButtonListener> applyListeners = new LinkedList<>();

        @Override
        public boolean canClose(String action, Object data) {
            if (data instanceof EDialogButtonType) {
                EDialogButtonType b = (EDialogButtonType) data;
                List<BeforeCloseButtonListener> list;
                synchronized (beforeCloseListeners) {
                    list = new ArrayList<>(beforeCloseListeners);
                }
                for (BeforeCloseButtonListener l : list) {
                    if (!l.beforeClose(b, StandardButton.getDialogResult(b))) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public void closed(String action, Object data) {
            if (data instanceof EDialogButtonType) {
                EDialogButtonType b = (EDialogButtonType) data;
                List<CloseButtonListener> list;
                synchronized (listeners) {
                    list = new ArrayList<>(listeners);
                }
                for (CloseButtonListener l : list) {
                    l.onClose(b, StandardButton.getDialogResult(b));
                }
            }
        }

        public void addDialogButtonListener(CloseButtonListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        public void removeDialogButtonListener(CloseButtonListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }

        public void addBeforeDialogButtonListener(BeforeCloseButtonListener l) {
            synchronized (beforeCloseListeners) {
                if (!beforeCloseListeners.contains(l)) {
                    beforeCloseListeners.add(l);
                }
            }
        }

        public void removeBeforeDialogButtonListener(BeforeCloseButtonListener l) {
            synchronized (beforeCloseListeners) {
                beforeCloseListeners.remove(l);
            }
        }

        public void addApplyButtonListener(ApplyButtonListener l) {
            synchronized (applyListeners) {
                if (!applyListeners.contains(l)) {
                    applyListeners.add(l);
                }
            }
        }

        public void removeApplyButtonListener(ApplyButtonListener l) {
            synchronized (applyListeners) {
                applyListeners.remove(l);
            }
        }

        @Override
        public void apply(String action, Object data) {
            if (data instanceof EDialogButtonType) {
                EDialogButtonType b = (EDialogButtonType) data;
                List<ApplyButtonListener> list;
                synchronized (applyListeners) {
                    list = new ArrayList<>(applyListeners);
                }
                for (ApplyButtonListener l : list) {
                    l.onApply(b);
                }
            }
        }
    };

    private static class CustomActionHandler implements ApplyActionHandler {

        private final List<CustomActionListener> listeners = new LinkedList<>();

        @Override
        public void apply(String action, Object data) {
            final List<CustomActionListener> ls;
            synchronized (listeners) {
                ls = new ArrayList<>(listeners);
            }
            for (CustomActionListener l : ls) {
                l.onAction(action);
            }
        }

        public void addCustomActionListener(CustomActionListener listener) {
            synchronized (listeners) {
                if (!listeners.contains(listener)) {
                    listeners.add(listener);
                }
            }
        }

        public void removeCustomActionListener(CustomActionListener listener) {
            synchronized (listeners) {
                listeners.remove(listener);
            }
        }
    }
    private StandardButtonsHandler standardButtonHandler = null;
    private CustomActionHandler customActionHandler = null;

    private CustomActionHandler getCustomActionHandler() {
        synchronized (this) {
            if (customActionHandler == null) {
                customActionHandler = new CustomActionHandler();
            }
        }
        return customActionHandler;
    }

    private StandardButtonsHandler getStandardButtonsHandler() {
        synchronized (this) {
            if (standardButtonHandler == null) {
                standardButtonHandler = new StandardButtonsHandler();
            }
            return standardButtonHandler;
        }
    }

    public AbstractContainer getBottomContainer() {
        return buttons;
    }

    public void setStandardButtons(Set<EDialogButtonType> buttons) {

        List<EDialogButtonType> list = new ArrayList<>(buttons);
        Collections.sort(list, new Comparator<EDialogButtonType>() {
            @Override
            public int compare(EDialogButtonType arg0, EDialogButtonType arg1) {
                return arg0.ordinal() == arg1.ordinal() ? 0 : arg0.ordinal() > arg1.ordinal() ? 1 : -1;
            }
        });

        if (buttons == null || buttons.isEmpty() || (buttons.size() == 1 && buttons.contains(EDialogButtonType.NO_BUTTON))) {
            hideButtonBox();
        } else {
            for (EDialogButtonType button : buttons) {
                if (button != EDialogButtonType.NO_BUTTON) {
                    addCloseAction(button);
                }
            }
        }
    }

    public void setButtonBoxVisible(final boolean isVisible) {
        if (buttonBoxVisible != isVisible) {
            buttonBoxVisible = isVisible;
            if (!isVisible) {
                hideButtonBox();
            } else {
                showButtonBox();
            }
        }
    }

    private void showButtonBox() {
        if (buttons.getHtml().containsClass("rwt-dialog-hide-element")) {
            final List<UIObject> list = buttons.getChildren();
            if (!list.isEmpty()) {
                for (UIObject btn : list) {
                    if (btn instanceof PushButton) {
                        buttons.getHtml().add(btn.getHtml());
                    }
                }
            }
            buttons.getHtml().removeClass("rwt-dialog-hide-element");
        }
    }

    private void hideButtonBox() {
        buttons.getHtml().addClass("rwt-dialog-hide-element");
        final List<UIObject> list = buttons.getChildren();
        for (UIObject btn : list) {
            if (btn instanceof PushButton) {
                buttons.getHtml().remove(btn.getHtml());
            }
        }
    }

    public boolean isButtonBoxVisible() {
        return buttonBoxVisible;
    }

    public void setActionEnabled(EDialogButtonType button, boolean enabled) {
        final String action = StandardButton.getTitle(button, getEnvironment()).replace("&", "");
        setActionEnabled(action, enabled);
    }

    public boolean isActionEnabled(EDialogButtonType button) {
        final String action = StandardButton.getTitle(button, getEnvironment()).replace("&", "");
        return isActionEnabled(action);
    }

    public void setActionVisible(EDialogButtonType button, boolean visible) {
        final String action = StandardButton.getTitle(button, getEnvironment()).replace("&", "");
        setActionVisible(action, visible);
    }

    public boolean isActionVisible(EDialogButtonType button) {
        final String action = StandardButton.getTitle(button, getEnvironment()).replace("&", "");
        return isActionVisible(action);
    }

    public void addCloseButtonListener(CloseButtonListener l) {
        getStandardButtonsHandler().addDialogButtonListener(l);
    }

    public void removeCloseButtonListener(CloseButtonListener l) {
        getStandardButtonsHandler().removeDialogButtonListener(l);
    }

    public void addBeforeCloseListener(BeforeCloseButtonListener l) {
        getStandardButtonsHandler().addBeforeDialogButtonListener(l);
    }

    public void removeBeforeCloseListener(BeforeCloseButtonListener l) {
        getStandardButtonsHandler().removeBeforeDialogButtonListener(l);
    }

    public void addApplyButtonListener(ApplyButtonListener l) {
        getStandardButtonsHandler().addApplyButtonListener(l);
    }

    public void removeApplyButtonListener(ApplyButtonListener l) {
        getStandardButtonsHandler().removeApplyButtonListener(l);
    }

    public Dialog(IClientEnvironment env, String title) {
        this(((WpsEnvironment) env).getDialogDisplayer(), title);
        environment = env;
    }

    public Dialog(IClientEnvironment env, String title, boolean restoreGeometry) {
        this(((WpsEnvironment) env).getDialogDisplayer(), title, restoreGeometry);
        environment = env;
    }

    public Dialog(String title) {
        this(getEnvironmentStatic(), title);
    }

    public Dialog() {
        this("");
    }

    public Dialog(IDialogDisplayer displayer, String title) {
        this(displayer, title, true);
    }

    public Dialog(IDialogDisplayer displayer, String title, boolean restoreGeometry) {
        this(displayer, title, restoreGeometry, null);
    }

    public Dialog(IDialogDisplayer displayer, String title, boolean restoreGeometry, DialogGeometry defaultGeometry) {
        super(new Div());
        this.environment = getEnvironmentStatic();
        this.html.addClass("rwt-ui-shadow");
        this.html.setCss("position", "absolute");
        this.displayer = displayer;
        this.html.add(header);
        this.header.setAttr("role", "title");
        this.html.addClass("rwt-dialog");
        this.html.addClass("ui-corner-all");
        this.title.setInnerText(title);
        this.title.setCss("white-space", "nowrap");
        this.title.setCss("padding-top", "2px");
        this.title.setCss("cursor", "move");
        this.header.setCss("text-align", "center");
        this.header.setCss("vertical-align", "middle");
        //this.header.setCss("margin-top", "3px");
        this.header.setCss("overflow", "hidden");
        this.header.addClass("rwt-dialog-header");
        this.header.setCss("cursor", "move");
        this.header.addClass("ui-corner-top");

        Html table = new Table();
        Table.Row row = new Table.Row();
        table.add(row);
        Table.DataCell cell = new Table.DataCell();
        row.add(cell);
        cell.setCss("width", "100%");
        cell.setCss("vertical-aling", "middle");
        cell.add(this.title);
        this.title.setCss("display", "block");

        cell = new Table.DataCell();
        cell.add(headerCloseButton.getHtml());
        row.add(cell);

        this.header.add(table);
        menuBar.setVisible(false);
        menuBar.html.setAttr("role", "menuBar");
        menuBar.setParent(this);
        this.html.add(menuBar.html);

        this.html.add(contentPanel.html);
        this.contentPanel.html.addClass("rwt-dialog-body");
        this.html.add(buttons.html);

        html.layout("$RWT.dialog._layout");
        handleBar = new Div();
        handleBar.setCss("height", "7px");
        handleBar.setAttr("role", "handle-bar");
        handleBar.setCss("width", "100%");
        seHandle = new Div();
        seHandle.setAttr("role", "se-handle");
        seHandle.setCss("cursor", "se-resize");
        seHandle.setCss("width", "5px");
        seHandle.setCss("height", "5px");
        seHandle.addClass("ui-corner-bottom");

        handleBar.add(seHandle);
        this.html.add(handleBar);
        if (defaultGeometry == null) {
            defaultGeometry = new DialogGeometry(300, 200, 0, 0);
        }
        setDialogGeometry(defaultGeometry);
        if (restoreGeometry) {
            this.key = title != null ? SettingNames.SYSTEM + "/" + title + "/geometry" : SettingNames.SYSTEM + "/" + getClass().getSimpleName() + "/geometry";
        }
        if (!key.isEmpty()) {
            restoreGeometryFromConfig(key);
        }
    }

    protected void saveGeometryToConfig() {
        if (key != null && !key.isEmpty() && getDialogGeometry().isValid()) {
            ((WpsSettings) getEnvironment().getConfigStore()).writeString(key, getDialogGeometry().asStr());
        }
    }

    /*В дочернем классе методы setWidth и setHeight вызовутся после восстановелния геометрии из конфига, 
     поэтому эти методы необхожимо перекрыть с учетом новой геометрии*/
    @Override
    public void setWidth(final int w) {
        if (internalResize) {
            if (getDialogGeometry() != null && !key.isEmpty() && getEnvironmentStatic().getConfigStore().contains(key)) {
                int storedWidth = getDialogGeometry().getWidth();
                int maxWidth = getMaxWidth();
                int minWidth = getMinimumWidth();
                if (maxWidth > 0 && storedWidth >= maxWidth) {
                    super.setWidth(maxWidth);
                    return;
                }
                if (minWidth > 0 && storedWidth <= minWidth) {
                    super.setWidth(minWidth);
                    return;
                }
                super.setWidth(storedWidth <= 0 ? (w <= 0 ? 300 : w) : storedWidth);
            } else {
                super.setWidth(w <= 0 ? 300 : w);
            }
        } else {
            setDialogGeometry(currentGeometry.getResized(w, currentGeometry.height));
        }
    }

    protected IPushButton findCloseActionByTitle(String title) {
        return buttons.getCloseActionByTitle(title);
    }

    @Override
    public void setHeight(final int h) {
        if (internalResize) {
            if (getDialogGeometry() != null && !key.isEmpty() && getEnvironmentStatic().getConfigStore().contains(key)) {
                int storedHeight = getDialogGeometry().getHeight();
                int maxHeight = getMaxHeight();
                int minHeight = getMinimumHeight();
                if (maxHeight > 0 && storedHeight >= maxHeight) {
                    super.setHeight(maxHeight);
                    return;
                }
                if (minHeight > 0 && storedHeight <= minHeight) {
                    super.setHeight(minHeight);
                    return;
                }
                super.setHeight(storedHeight <= 0 ? (h <= 0 ? 200 : h) : storedHeight);
            } else {
                super.setHeight(h <= 0 ? 200 : h);
            }
        } else {
            setDialogGeometry(currentGeometry.getResized(currentGeometry.width, h));
        }
    }

    protected final void restoreGeometryFromConfig(final String configKey) {
        final WpsSettings settings = (WpsSettings) environment.getConfigStore();
        if (settings.contains(configKey)) {
            final String newGeometry = settings.readString(configKey);
            if (newGeometry != null && !newGeometry.isEmpty()) {
                final DialogGeometry newDialogGeometry;
                try {
                    newDialogGeometry = DialogGeometry.parseFromStr(newGeometry);
                } catch (WrongFormatException e) {
                    environment.getTracer().error(e);
                    return;
                }
                if (newDialogGeometry.isValid()
                    && newDialogGeometry.getTop()>=0 
                    && newDialogGeometry.getLeft()>=0){
                    if (isResizable()) {
                        setDialogGeometry(newDialogGeometry);
                    } else {
                        setDialogGeometry(newDialogGeometry.getResized(getWidth(), getHeight()));
                    }
                }
            }
        }
    }

    public void addCloseAction(String name, DialogResult result) {
        buttons.addCloseAction(name, result);
    }

    public void addCloseAction(EDialogButtonType buttonType, DialogResult result) {
        buttons.addCloseAction(StandardButton.getTitle(buttonType, getEnvironment()), result);
    }

    public void addCloseAction(IPushButton closeButton, DialogResult result) {
        buttons.addCloseAction(closeButton, result);
    }

    public void addCloseAction(IPushButton closeButton, DialogResult result, CloseActionHandler handler, Object data) {
        buttons.addCloseAction(closeButton.getObjectName(), closeButton, result, handler, null, data, false);
    }

    public PushButton addCloseAction(EDialogButtonType button, CloseActionHandler handler) {
        final PushButton btn
                = buttons.addCloseAction(StandardButton.getTitle(button, getEnvironment()), StandardButton.getDialogResult(button), handler, null, button, false);
        if (StandardButton.getIcon(button) != null) {
            btn.setIcon(getEnvironment().getApplication().getImageManager().getIcon(StandardButton.getIcon(button)));
        }
        return btn;
    }

    public PushButton addCloseAction(EDialogButtonType button) {
        return addCloseAction(button, getStandardButtonsHandler());
    }

    public PushButton addCloseAction(String name, DialogResult result, CloseActionHandler handler, Object data) {
        return buttons.addCloseAction(name, result, handler, null, data, false);
    }

    public PushButton addCustomAction(String name) {
        return buttons.addCloseAction(name, DialogResult.APPLY, null, getCustomActionHandler(), null, true);
    }

    public PushButton addCustomAction(String name, Alignment alignment) {
        return buttons.addCloseAction(name, alignment == Alignment.LEFT ? DialogResult.APPLY : DialogResult.NONE, null, getCustomActionHandler(), null, true);
    }

    public void addCustomActionListener(CustomActionListener listener) {
        getCustomActionHandler().addCustomActionListener(listener);
    }

    public void removeCustomActionListener(CustomActionListener listener) {
        getCustomActionHandler().removeCustomActionListener(listener);
    }

    public void clearCloseActions() {
        buttons.clear();
    }

    public void add(UIObject obj) {
        contentPanel.add(obj);
    }

    public void remove(UIObject obj) {
        contentPanel.remove(obj);
    }

    public boolean isActionEnabled(String action) {
        return buttons.isActionEnabled(action);
    }

    public void setActionEnabled(String action, boolean enabled) {
        buttons.setActionEnabled(action, enabled);
    }

    public boolean isActionVisible(String action) {
        return buttons.isActionVisible(action);
    }

    public void setActionVisible(String action, boolean enabled) {
        buttons.setActionVisible(action, enabled);
    }

    protected void dialogButtonAction(String name) {
    }

    //------------------- IDialog implementation --------------------
    @Override
    public final String getWidowTitle() {
        return title.getInnerText();
    }

    @Override
    public final void setWindowTitle(String title) {
        this.title.setInnerText(title);
    }

    @Override
    public Icon getWindowIcon() {
        return null;
    }

    @Override
    public void setWindowIcon(Icon icon) {
    }

    @Override
    public DialogResult execDialog() {
        isModalExecuting = true;
        try {
            return execDialog(displayer.getRootPanel());
        } finally {
            isModalExecuting = false;
        }
    }

    public boolean wasClosed() {
        return wasClosed;
    }

    @Override
    public RootPanel findRoot() {
        return displayer.getRootPanel();
    }

    public boolean isNotModalExec() {
        return html.containsClass("rwt-ui-dialog-no-overlay");
    }

    @Override
    public DialogResult execDialog(final IWidget parentWidget) {
        RootPanel rootPanel = null;
        if (parentWidget instanceof RootPanel){
            rootPanel = (RootPanel)parentWidget;
        }else if (parentWidget instanceof UIObject){
            rootPanel =  ((UIObject)parentWidget).findRoot();
            if (rootPanel==null){
                rootPanel = displayer.getRootPanel();
            }
        }
        if (rootPanel==null){
            final IClientEnvironment environment = getEnvironment();
            if (environment==null){
                final String message = "Unable to show dialog \'%1$s\': application main window was not found";
                throw new IllegalUsageError(String.format(message, Dialog.class.getSimpleName()));
            }else{
                final String message = 
                    environment.getMessageProvider().translate("TraceMessage", "Unable to show dialog \'%1$s\': application main window was not found");
                environment.getTracer().error(String.format(message, Dialog.class.getSimpleName()));
                return DialogResult.REJECTED;
            }
        }else{
            rootPanel.add(this);
            html.removeClass("rwt-ui-dialog-no-overlay");
            html.removeClass("rwt-ui-dialog-show");
            unsetClose();
            isModalExecuting = true;
            try {
                return displayer.execModal(this);
            } finally {
                isModalExecuting = false;
            }
        }
    }

    @Override
    public IPushButton addButton(EDialogButtonType button) {
        return addCloseAction(button);
    }

    public RwtMenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBarVisible(boolean visible) {
        menuBar.setVisible(visible);
        menuBar.setTop(22);
        if (visible) {
            header.setCss("margin-bottom", "22px");
        } else {
            header.setCss("margin-bottom", "0px");
        }
    }

    @Override
    public void clearButtons() {
        clearCloseActions();
    }

    @Override
    public IPushButton getButton(EDialogButtonType button) {
        return buttons.getCloseActionByData(button);
    }

    public IPushButton findButtonByObjectName(final String objectName) {
        return buttons.getCloseActionByObjectName(objectName);
    }

    @Override
    public void removeButton(EDialogButtonType button) {
        buttons.removeCloseActionWithData(button);
    }

    protected void unsetClose() {
        wasClosed = false;
        result = null;
    }

    public void close(DialogResult result) {
        String action;
        switch (result) {
            case ACCEPTED:
                action = defaultAcceptAction;
                break;
            case REJECTED:
                action = defaultRejectAction;
                break;
            default:
                return;
        }
        closeImpl(action, result);
    }

    private boolean closeImpl(String closeAction, DialogResult result) {
        saveGeometryToConfig();
        if (wasClosed) {
            return true;
        }
        DialogResult alternateResult = onClose(closeAction, result);
        if (alternateResult == null) {
            return false;
        }
        wasClosed = true;
        this.result = alternateResult;

        if (getParent() != null) {
            ((AbstractContainer) getParent()).remove(this);
        }
        return true;
    }

    protected DialogResult onClose(String action, DialogResult actionResult) {
        return actionResult;
    }

    @Override
    public void acceptDialog() {
        close(DialogResult.ACCEPTED);
    }

    @Override
    public void rejectDialog() {
        close(DialogResult.REJECTED);
    }

    @Override
    public DialogResult getDialogResult() {
        return result;
    }

    @Override
    public EventSupport getEventSupport() {
        return dialogEventSupport;
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        obj = buttons.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        obj = contentPanel.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        obj = menuBar.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        return headerCloseButton.findObjectByHtmlId(id);

    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        this.contentPanel.visit(visitor);
        this.buttons.visit(visitor);
        this.headerCloseButton.visit(visitor);
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (!isVisible() && isModalExecuting) {
            close(DialogResult.REJECTED);
        }
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/dialog.js"};
    }

    @Override
    protected String[] clientCssRequired() {
        return new String[]{"org/radixware/wps/rwt/dialog.css"};
    }

    public void showDialog() {
        showDialog(findRoot());
    }

    public void showDialog(IWidget parentWidget) {

        if (parentWidget instanceof AbstractContainer) {
            ((AbstractContainer) parentWidget).add(this);
            wasClosed = false;
            result = null;
            html.addClass("rwt-ui-dialog-no-overlay");
            html.addClass("rwt-ui-dialog-show");
            displayer.showModal(this);
        }
    }

//    public void showModal() {
//        showModal(findRoot());
//    }
    public void showModal(IWidget parentWidget) {

        if (parentWidget instanceof AbstractContainer) {
            ((AbstractContainer) parentWidget).add(this);
            wasClosed = false;
            result = null;
            html.removeClass("rwt-ui-dialog-no-overlay");
            html.addClass("rwt-ui-dialog-show");
            displayer.showModal(this);
        }
    }

    protected void updateDialog() {
        displayer.showModal(this);
    }

    public boolean isAutoHeight() {
        return this.contentPanel.getHtml().getAttr("isAutoHeight") != null;
    }

    public void setAutoHeight(boolean isAutoHeight) {
        if (isAutoHeight) {
            this.contentPanel.getHtml().setAttr("isAutoHeight", true);
        } else {
            this.contentPanel.getHtml().setAttr("isAutoHeight", null);
        }
    }

    public void setAdjustSizeEnabled(boolean isAdjustSizeEnabled) {
        html.setAttr("isAdjustSizeEnabled", isAdjustSizeEnabled);
    }

    public boolean isAutoWidth() {
        return this.contentPanel.getHtml().getBooleanAttr("isAutoWidth");
    }

    public void setAutoWidth(boolean isAutoHeight) {
        if (isAutoHeight) {
            this.contentPanel.getHtml().setAttr("isAutoWidth", true);
        } else {
            this.contentPanel.getHtml().setAttr("isAutoWidth", null);
        }
    }

    public void setResizable(boolean isResizable) {
        this.isResizable = isResizable;
        if (isResizable) {
            getHtml().setAttr("isResizable", null);
        } else {
            getHtml().setAttr("isResizable", false);
        }
    }

    public boolean isResizable() {
        return isResizable;
    }

    public int getMaxHeight() {
        return this.contentPanel.getHtml().getIntegerAttr("maxHeight", -1);
    }

    public void setMaxHeight(int mh) {
        if (mh >= 0) {
            this.contentPanel.getHtml().setAttr("maxHeight", mh);
        } else {
            this.contentPanel.getHtml().setAttr("maxHeight", null);
        }
    }

    public int getMaxWidth() {
        return this.contentPanel.getHtml().getIntegerAttr("maxWidth", -1);
    }

    public void setMaxWidth(int mh) {
        if (mh >= 0) {
            this.contentPanel.getHtml().setAttr("maxWidth", mh);
        } else {
            this.contentPanel.getHtml().setAttr("maxWidth", null);
        }
    }

    public final DialogGeometry getDialogGeometry() {
        return currentGeometry.clone();
    }

    public final void setDialogGeometry(final DialogGeometry geometry) {
        if (geometry.isValid()) {
            if (isResizable() || currentGeometry.sameSize(geometry)) {
                this.currentGeometry = geometry.clone();
            } else {
                this.currentGeometry
                        = new DialogGeometry(currentGeometry.getWidth(), currentGeometry.getHeight(), geometry.getLeft(), geometry.getTop());
            }
            checkSizeBounds();
            applyGeometry(currentGeometry);
        }
    }

    private void checkSizeBounds() {
        int width = currentGeometry.getWidth();
        if (getMinimumWidth() > 0) {
            width = Math.max(width, getMinimumWidth());
        }
        if (getMaxWidth() > 0) {
            width = Math.min(width, getMaxWidth());
        }
        int height = currentGeometry.getHeight();
        if (getMinimumHeight() > 0) {
            height = Math.max(height, getMinimumHeight());
        }
        if (getMaxHeight() > 0) {
            height = Math.min(height, getMaxHeight());
        }
        if (width != currentGeometry.width || height != currentGeometry.height) {
            currentGeometry = currentGeometry.getResized(width, height);
        }
    }

    private void applyGeometry(final DialogGeometry geometry) {
        internalResize = true;
        try {
            if (!isAutoWidth()) {
                setWidth(geometry.getWidth());
            }
            if (!isAutoHeight()) {
                setHeight(geometry.getHeight());
            }
        } finally {
            internalResize = false;
        }
        this.setTop(geometry.getTop());
        this.setLeft(geometry.getLeft());
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if ("dlg_pos".equals(actionName) && !actionParam.isEmpty()) {
            int t = Integer.parseInt(actionParam.substring(actionParam.indexOf("t:") + 2, actionParam.indexOf("l:")));
            int l = Integer.parseInt(actionParam.substring(actionParam.indexOf("l:") + 2));
            //updating current state
            getHtml().setCss("top", t + "px");
            getHtml().setCss("left", l + "px");
            //calc new state            
            setDialogGeometry(getDialogGeometry().getMoved(l, t));
        } else if ("dlg_size".equals(actionName) && !actionParam.isEmpty() && isResizable()) {
            int w = Integer.parseInt(actionParam.substring(actionParam.indexOf("w:") + 2, actionParam.indexOf("h:")));
            int h = Integer.parseInt(actionParam.substring(actionParam.indexOf("h:") + 2));
            //updating current state
            getHtml().setCss(Html.CSSRule.WIDTH, w + "px");
            getHtml().setCss(Html.CSSRule.HEIGHT, h + "px");
            //calc new state
            setDialogGeometry(getDialogGeometry().getResized(w, h));
        }
        super.processAction(actionName, actionParam);
    }

    public final static class DialogGeometry implements IRwtGeometry, Cloneable {

        private final int width;
        private final int height;
        private final int top;
        private final int left;

        public DialogGeometry(final int width, final int height, final int left, final int top) {
            this.width = width;
            this.height = height;
            this.top = top;
            this.left = left;
        }

        public DialogGeometry() {
            width = -1;
            height = -1;
            left = 0;
            top = 0;
        }

        public DialogGeometry(final DialogGeometry copy) {
            width = copy.width;
            height = copy.height;
            left = copy.left;
            top = copy.top;
        }

        @Override
        @SuppressWarnings({"CloneDeclaresCloneNotSupported", "CloneDoesntCallSuperClone"})
        protected DialogGeometry clone() {
            return new DialogGeometry(this);
        }

        public boolean isValid() {
            return width > 0 && height > 0;
        }

        public DialogGeometry getResized(final int width, final int height) {
            return new DialogGeometry(width, height, this.left, this.top);
        }

        public DialogGeometry getMoved(final int left, final int top) {
            return new DialogGeometry(this.width, this.height, left, top);
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public int getLeft() {
            return left;
        }

        @Override
        public int getTop() {
            return top;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 73 * hash + this.width;
            hash = 73 * hash + this.height;
            hash = 73 * hash + this.top;
            hash = 73 * hash + this.left;
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DialogGeometry other = (DialogGeometry) obj;
            if (this.width != other.width) {
                return false;
            }
            if (this.height != other.height) {
                return false;
            }
            if (this.top != other.top) {
                return false;
            }
            return this.left == other.left;
        }

        @Override
        public String toString() {
            final StringBuilder optionsStr = new StringBuilder(32);
            optionsStr.append("width=");
            optionsStr.append(getWidth());
            optionsStr.append(", height=");
            optionsStr.append(getHeight());
            optionsStr.append(", left=");
            optionsStr.append(getLeft());
            optionsStr.append(", top=");
            optionsStr.append(getTop());
            return optionsStr.toString();
        }

        public boolean sameSize(final DialogGeometry other) {
            return other.width == this.width && other.height == this.height;
        }

        public boolean samePosition(final DialogGeometry other) {
            return other.left == this.left && other.top == this.top;
        }

        @Override
        public String asStr() {
            final StringBuilder optionsStr = new StringBuilder();
            if (getWidth() >= 0) {
                optionsStr.append(getWidth());
            }
            optionsStr.append(';');
            if (getHeight() >= 0) {
                optionsStr.append(getHeight());
            }
            optionsStr.append(';');

            if (getTop() >= 0) {
                optionsStr.append(getTop());
            }
            optionsStr.append(';');
            if (getLeft() >= 0) {
                optionsStr.append(getLeft());
            }
            return optionsStr.toString();
        }

        public static DialogGeometry parseFromStr(final String geometryAsStr) throws WrongFormatException {
            String[] settingsGeometry = geometryAsStr.split(";");
            if (settingsGeometry.length > 0 && settingsGeometry.length <= 4) {
                final int[] values = {-1,-1,-1,-1};
                for (int i=0; i<settingsGeometry.length; i++){
                    try{
                        values[i] = Integer.parseInt(settingsGeometry[i]);
                    }catch(NumberFormatException e){
                        throw new WrongFormatException("Failed to parse dialog geometry from \'" + geometryAsStr + "\' string", e);
                    }
                }
                return new DialogGeometry(values[0], values[1], values[3], values[2]);
            } else {
                throw new WrongFormatException("Failed to parse dialog geometry from \'" + geometryAsStr + "\' string");
            }
        }
    }

    @Override
    public IClientEnvironment getEnvironment() {
        if (environment == null) {
            if (displayer != null) {
                environment = displayer.getEnvironment();
            }
            if (environment == null) {
                environment = getEnvironmentStatic();
            }
        }
        return environment;
    }
}

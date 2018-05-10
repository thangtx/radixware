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
package org.radixware.wps.views.widgetsarea;

import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaItemPresenter;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.GridBoxContainer;
import org.radixware.wps.rwt.HorizontalBox;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.views.RwtAction;

public class ItemWidget extends Container implements IWidgetAreaItemPresenter {

    public interface EditFinishedListener {

        public void onEditFinished(int left, int top, int width, int height);
    }

    private final TabWidgetContent ownerTab;
    private final VerticalBoxContainer mainLayout = new VerticalBoxContainer();
    private final Label label;
    private final HorizontalBox toolButtonsContainer = new HorizontalBox();
    private final RwtAction resizeAction;
    private final RwtAction removeAction;
    private final VerticalBoxContainer contentLayoutWidget = new VerticalBoxContainer();
    private EditFinishedListener editFinishedListener;

    private final static class AdminPanelIcons extends ClientIcon {

        private AdminPanelIcons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon MOVE_ITEM = new AdminPanelIcons("classpath:images/move.svg");
        public static final ClientIcon ADD_TAB = new AdminPanelIcons("classpath:images/addEmpty.svg");
    }

    ItemWidget(final TabWidgetContent ownerTab, final IClientEnvironment environment, String title) {
        this.ownerTab = ownerTab;
        GridBoxContainer titleLayout = new GridBoxContainer(1, 2);
        getHtml().addClass("rwt-dash-widget");
        getHtml().setCss("box-sizing", "border-box");
        label = new Label(title);
        label.getHtml().setCss("line-height", "20px");
        label.setHeight(20);
        label.getHtml().addClass("rwt-no-text-selection");
        label.getHtml().setCss("margin-left", "3px");
        label.setFont(label.getFont().getBold());
        mainLayout.getHtml().setCss("width", "100%");
        mainLayout.getHtml().setCss("height", "100%");
        getHtml().setCss("border", "1px solid black");
        titleLayout.setHeight(20);
        Html table = titleLayout.getHtml().children().get(0);
        table.setCss("border-collapse", "collapse");
        table.setCss("border", "0px");
        titleLayout.add(label, 0, 0);
        toolButtonsContainer.setHeight(20);
        toolButtonsContainer.getHtml().setCss("background-color", "transparent");
        titleLayout.add(toolButtonsContainer, 0, 1);
        titleLayout.setStretchColumn(0);
        titleLayout.getHtml().setCss("border-bottom", "1px solid black");
        titleLayout.setRowHeightByContent(0);
        MessageProvider mp = environment.getMessageProvider();
        removeAction = new RwtAction(environment, ClientIcon.Dialog.BUTTON_CANCEL, null);
        removeAction.setToolTip(mp.translate("WidgetsArea", "Remove"));
        resizeAction = new RwtAction(environment, AdminPanelIcons.MOVE_ITEM, null);
        resizeAction.setToolTip(mp.translate("WidgetsArea", "Resize"));
        resizeAction.addActionListener(new Action.ActionListener() {

            @Override
            public void triggered(Action action) {
                getHtml().setAttr("resize-widget", null);
                getHtml().setAttr("resize-widget", true);
            }
        });
        mainLayout.add(titleLayout);
        contentLayoutWidget.getHtml().setCss("overflow", "hidden");
        mainLayout.add(contentLayoutWidget);
        mainLayout.setAutoSize(contentLayoutWidget, true);
        add(mainLayout);
    }

    @Override
    public void setBounds(int gridTop, int gridLeft, int gridWidth, int gridHeight) {
        ownerTab.setBounds(this, gridTop, gridLeft, gridWidth, gridHeight);
    }

    @Override
    public void setContent(IWidget widget) {
        contentLayoutWidget.add((UIObject) widget);
    }

    @Override
    public void setTitle(String title) {
        label.setText(title);
    }

    private void refreshToolButtonsContainerWidth() {
        List<UIObject> toolButtonList = toolButtonsContainer.getChildren();
        int size = toolButtonList.isEmpty() ? 0 : toolButtonList.size();
        toolButtonsContainer.setWidth(size * 22);
    }

    @Override
    public void addAction(Action action) {
        if (action != null) {
            ToolButton toolButton = createToolButton(action);
            if (action.equals(resizeAction)) {
                toolButton.getHtml().addClass("resize-button");
                ItemWidget.this.getHtml().setAttr("isResizable", true);
            }
            toolButtonsContainer.add(toolButton);
            refreshToolButtonsContainerWidth();
        }
    }

    @Override
    public void addAction(int pos, Action action) {
        if (action != null) {
            toolButtonsContainer.add(pos, createToolButton(action));
            refreshToolButtonsContainerWidth();
        }
    }

    @Override
    public void removeAction(Action action) {
        for (UIObject obj : toolButtonsContainer.getChildren()) {
            if (action != null && obj instanceof ToolButton && action.equals(((ToolButton) obj).getAction())) {
                if (action.equals(resizeAction)) {
                    ItemWidget.this.getHtml().setAttr("isResizable", false);
                }
                toolButtonsContainer.remove(obj);
                List<UIObject> toolButtonList = toolButtonsContainer.getChildren();
                int size = toolButtonList.isEmpty() ? 0 : toolButtonList.size();
                toolButtonsContainer.setWidth(size * 22);
                break;
            }
        }
    }

    @Override
    public Action getRemoveAction() {
        return removeAction;
    }

    @Override
    public Action getResizeAction() {
        return resizeAction;
    }

    private ToolButton createToolButton(Action action) {
        ToolButton btn = new ToolButton(action);
        btn.setIconSize(11, 11);
        btn.setWidth(14);
        btn.setHeight(14);
        btn.getHtml().setCss("margin-top", "3px");
        btn.getHtml().setCss("margin-bottom", "3px");
        btn.getHtml().removeClass("rwt-tool-button");
        return btn;
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        } else {
            return mainLayout.findObjectByHtmlId(id);
        }
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if (actionName != null && !actionName.isEmpty() && actionName.equals("dashWidgetResize")) {
            String[] args = actionParam.split(";");
            editFinishedListener.onEditFinished(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        } else {
            super.processAction(actionName, actionParam);
        }
    }
    
    void setEditFinishedListener(EditFinishedListener listener) {
        this.editFinishedListener = listener;
    }
}

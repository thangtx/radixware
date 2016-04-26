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

package org.radixware.kernel.explorer.editors.monitoring;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.editors.monitoring.tree.TreeItem;
import org.radixware.kernel.explorer.editors.monitoring.tree.UnitsTree.UpdateContextEventPanelEvent;
import org.radixware.kernel.explorer.views.selector.StandardSelector;


public class EventLogPanel {

    private final QWidget mainWidget;
    private final UnitsWidget parent;
    private GroupModel eventLogWithContextModel;

    public EventLogPanel(final UnitsWidget parent) {
        this.parent = parent;
        mainWidget = new QWidget(parent);

    }

    public void open(final GroupModel eventLogWithContextModel) {
        try {
            final QVBoxLayout mainLayout = new QVBoxLayout();
            mainLayout.setMargin(0);
            mainWidget.setLayout(mainLayout);
            mainWidget.setUpdatesEnabled(false);
            this.eventLogWithContextModel = eventLogWithContextModel;
        } finally {
            mainWidget.setUpdatesEnabled(true);
        }
    }

    public void update(final TreeItem item) {
        QApplication.postEvent(parent.getTree(), new UpdateContextEventPanelEvent(item)); //updateContextTab(item);          
    }

    public void updateContextTab(final TreeItem item) {
        if (eventLogWithContextModel.getView() == null) {
            createContextEventsPanel(item);
        } else {
            if (eventLogWithContextModel != null/* && index==EVENT_LOG_CONTEXT_TAB*/) {
                try {
                    eventLogWithContextModel.getProperty(parent.getIdsGetter().getPropId(UnitsWidget.EPropertyName.PARAM_CONTEXT_ID)).setValueObject(item.getId().toString());
                    eventLogWithContextModel.getProperty(parent.getIdsGetter().getPropId(UnitsWidget.EPropertyName.PARAM_CONTEXT_TYPE)).setValueObject(item.getContextType().getValue());
                    eventLogWithContextModel.reread();
                } catch (ServiceClientException ex) {
                    eventLogWithContextModel.showException(ex);
                }
            }
        }
    }

    private void createContextEventsPanel(final TreeItem item) {
        if (eventLogWithContextModel != null) {
            final StandardSelector selector = (StandardSelector) eventLogWithContextModel.createView();
            eventLogWithContextModel.getProperty(parent.getIdsGetter().getPropId(UnitsWidget.EPropertyName.PARAM_CONTEXT_ID)).setValueObject(item.getId().toString());
            eventLogWithContextModel.getProperty(parent.getIdsGetter().getPropId(UnitsWidget.EPropertyName.PARAM_CONTEXT_TYPE)).setValueObject(item.getContextType().getValue());
            selector.open(eventLogWithContextModel);
            mainWidget.layout().addWidget(selector);
        }
    }

    public QWidget getWidget() {
        return mainWidget;
    }

    public void setVisible(final boolean isVisible) {
        mainWidget.setVisible(isVisible);
    }

    public boolean isVisible() {
        return mainWidget.isVisible();
    }
}

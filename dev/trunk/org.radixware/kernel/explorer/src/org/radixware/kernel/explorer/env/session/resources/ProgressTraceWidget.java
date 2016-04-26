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

package org.radixware.kernel.explorer.env.session.resources;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchNameError;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.explorer.env.ExplorerIcon;


class ProgressTraceWidget extends QTreeWidget {

    private final static int HEIGHT = 200;
    private final static int WIDTH = 350;
    private QTreeWidgetItem currentProgress = null;
    private final IClientEnvironment environment;

    public ProgressTraceWidget(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        this.environment = environment;
        setColumnCount(1);
        setHeaderHidden(true);
        setSelectionMode(SelectionMode.NoSelection);
        setMinimumSize(WIDTH, HEIGHT);
    }

    public void traceStartProgress(final String name) {
        final String itemText = String.format(environment.getMessageProvider().translate("TraceMessage", "Process \"%s\" was started"), name);
        final QTreeWidgetItem item = 
            createTreeItem(EEventSeverity.DEBUG, itemText, EEventSource.EXPLORER.getValue(), currentProgress);
        if (currentProgress == null) {
            addTopLevelItem(item);
        }
        currentProgress = item;
        scrollToItem(item);
        resizeColumnToContents(0);
    }

    public void traceFinishProgress(final String name) {
        final QTreeWidgetItem parent = currentProgress == null ? null : currentProgress.parent();
        final String itemText = String.format(environment.getMessageProvider().translate("TraceMessage", "Process \"%s\" was finished"), name);
        final QTreeWidgetItem item = 
            createTreeItem(EEventSeverity.DEBUG, itemText, EEventSource.EXPLORER.getValue(), currentProgress);        
        if (parent == null) {
            addTopLevelItem(item);
        }
        currentProgress = parent;
        scrollToItem(item);
        resizeColumnToContents(0);
    }

    public void add(final org.radixware.schemas.eas.Trace trace) {
        if (trace != null && trace.getItemList() != null && !trace.getItemList().isEmpty()) {            
            QTreeWidgetItem treeItem = null;
            for (org.radixware.schemas.eas.Trace.Item item : trace.getItemList()) {                
                treeItem = createTreeItem(item, currentProgress);
                if (treeItem!=null && currentProgress == null) {
                    addTopLevelItem(treeItem);
                }
            }
            if (treeItem != null) {
                scrollToItem(treeItem);
                resizeColumnToContents(0);
            }
        }
    }

    private QTreeWidgetItem createTreeItem(final org.radixware.schemas.eas.Trace.Item traceItem, final QTreeWidgetItem parent) {
        final String itemText;
        try{
            itemText = XmlUtils.parseSafeXmlString(traceItem.getStringValue());
        }catch(RuntimeException exception){
            final String messageTemplate = 
                environment.getMessageProvider().translate("ExplorerError", "Can't parse trace message \'%s\':\n%s");
            final String message = String.format(messageTemplate, traceItem.getStringValue(), exception.toString());
            environment.getTracer().error(message);
            return null;
        }
        EEventSeverity severity;
        try{
            severity= EEventSeverity.getForName(traceItem.getLevel().toString());
        }catch(NoConstItemWithSuchNameError error){
            severity = EEventSeverity.DEBUG;
        }
        final String source = EEventSource.ARTE.getValue();
        return createTreeItem(severity, itemText, source, parent);
    }
    
    private QTreeWidgetItem createTreeItem(final EEventSeverity severity, final String message, final String source, final QTreeWidgetItem parent){
        final QTreeWidgetItem item;
        if (parent == null) {
            item = new QTreeWidgetItem(this);
        } else {
            item = new QTreeWidgetItem(parent);
            parent.addChild(item);
            parent.setExpanded(true);
        }
        
        item.setFlags(Qt.ItemFlag.ItemIsEnabled);
        item.setText(0, prepare(message));
        item.setIcon(0, getIconForEventSeverity(severity));
        environment.getTracer().put(severity, message, source);
        return item;        
    }

    private static String prepare(final String str) {
        return String.valueOf(str).replace("\n", " ");
    }

    private QIcon getIconForEventSeverity(final EEventSeverity severity) {
        return ExplorerIcon.getQIcon(ExplorerIcon.TraceLevel.findEventSeverityIcon(severity, environment));
    }

    public void deleteAll() {
        currentProgress = null;
        clear();
    }
}

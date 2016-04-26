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

package org.radixware.kernel.explorer.tester.tests;

import com.trolltech.qt.gui.QIcon;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;
import org.radixware.kernel.explorer.tester.TesterConstants;

import org.radixware.kernel.explorer.views.Editor;
import org.radixware.kernel.explorer.views.IExplorerView;
import org.radixware.kernel.explorer.views.StandardParagraphEditor;
import org.radixware.kernel.explorer.views.selector.Selector;


public class TestResult {

    public String type;
    public String path;
    public String title;
    public QIcon icon;
    public boolean isForEntityModel = false;
    private ArrayList<TestResultEvent> log = new ArrayList<>();
    private ArrayList<ExplorerTraceItem> traceLog = new ArrayList<>();
    private boolean exceptionsOccuried = false;
    private boolean hasWarnings = false;
    private boolean hasInfoMessages = false;
    public String operation = "";
    public String result = "";
    public String time = "";
    private final IClientEnvironment environment;

    public TestResult(IClientEnvironment environment, Object testObject) {
        this.isForEntityModel = testObject instanceof EntityModel;
        if (environment==null)
            throw new IllegalArgumentException("environment cant be null");
        this.environment = environment;
        this.type = getType(testObject);
        this.path = getPath(testObject);
        this.icon = getIcon(testObject);
        this.title = getTitle(testObject);
    }

    public IClientEnvironment getenvironment() {
        return environment;
    }

    public void writeTraceEventToLog(ExplorerTraceItem event) {
        if (event != null) {
            traceLog.add(event);
        }
    }

    public List<ExplorerTraceItem> getTraceEventsLog() {
        return this.traceLog;
    }

    public void writeEventToLog(TestResultEvent event) {
        if (event != null) {
            log.add(event);
        }
    }

    public List<TestResultEvent> getEventsLog() {
        return log;
    }

    public void setHasErrors() {
        this.exceptionsOccuried = true;
    }

    public boolean hasErrors() {
        return exceptionsOccuried;
    }

    public void setHasWarnings() {
        this.hasWarnings = true;
    }

    public boolean hasWarnings() {
        return hasWarnings;
    }

    public void setHasInfoMessages() {
        this.hasInfoMessages = true;
    }

    public boolean hasInfoMessages() {
        return hasInfoMessages;
    }

    private String getType(Object testObject) {
        if (testObject != null) {
            if (testObject instanceof IExplorerTreeNode) {
                IExplorerTreeNode node = (IExplorerTreeNode) testObject;
                IExplorerItemView view = node.getView();
                if (view != null) {
                    if (view.isParagraphView()) {
                        return TesterConstants.OBJ_PARAGRAPH.getTitle();
                    }
                    if (view.isGroupView()) {
                        return TesterConstants.OBJ_SELECTOR.getTitle();
                    }
                    if (view.isEntityView()) {
                        return TesterConstants.OBJ_EDITOR.getTitle();
                    }
                }
            } else if (testObject instanceof Editor || testObject instanceof EntityModel) {
                return TesterConstants.OBJ_EDITOR.getTitle();
            } else if (testObject instanceof Selector || testObject instanceof GroupModel) {
                return TesterConstants.OBJ_SELECTOR.getTitle();
            } else if (testObject instanceof EditorPageModelItem) {
                return TesterConstants.OBJ_PAGE.getTitle();
            } else if (testObject instanceof StandardParagraphEditor
                    || testObject.getClass().getSimpleName().startsWith(EDefinitionIdPrefix.CUSTOM_PARAG_EDITOR.getValue())) {
                return TesterConstants.OBJ_PARAGRAPH.getTitle();
            }
        }
        return TesterConstants.OBJ_UNDEFINED.getTitle();
    }

    private String getTitle(Object testObject) {
        if (testObject != null) {
            if (testObject instanceof IExplorerTreeNode) {
                return ((IExplorerTreeNode) testObject).getView().getTitle();
            } else if (testObject instanceof IExplorerView) {
                if (((IExplorerView) testObject).getModel() != null) {
                    return ((IExplorerView) testObject).getModel().getTitle();
                }
            } else if (testObject instanceof EditorPageModelItem) {
                return ((EditorPageModelItem) testObject).getTitle();
            } else if (testObject instanceof Model) {
                return ((Model) testObject).getTitle();
            }
        }
        return TesterConstants.OBJ_UNDEFINED.getTitle();
    }

    private QIcon getIcon(Object testObject) {
        if (testObject != null) {
            if (testObject instanceof IExplorerTreeNode) {
                return (QIcon) ((IExplorerTreeNode) testObject).getView().getIcon();
            } else if (testObject instanceof IExplorerView) {
                if (((IExplorerView) testObject).getModel() != null) {
                    return (QIcon) ((IExplorerView) testObject).getModel().getIcon();
                }
            } else if (testObject instanceof EditorPageModelItem) {
                return (QIcon) ((EditorPageModelItem) testObject).getIcon();
            } else if (testObject instanceof Model) {
                return (QIcon) ((Model) testObject).getIcon();
            }
        }
        return null;
    }

    private String getPath(Object testObject) {
        if (testObject != null) {
            if (testObject instanceof IExplorerTreeNode) {
                return ((IExplorerTreeNode) testObject).getPath();
            } //            else if(testObject instanceof Model){
            //                final ExplorerItemView itemView = ((Model)testObject).getExplorerItemView();
            //
            //            }
            else {
                return "";
            }
        }
        return TesterConstants.OBJ_UNDEFINED.getTitle();
    }
}

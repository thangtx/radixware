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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.EntityEditorDialog;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.dialogs.SelectInstantiatableClassDialog;
import org.radixware.kernel.explorer.tester.TesterConstants;
import org.radixware.kernel.explorer.tester.TesterEngine;
import org.radixware.kernel.explorer.tester.TesterEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.providers.CreationSelectDialogTestProvider;
import org.radixware.kernel.explorer.tester.providers.RestrictedForInsertionsEditorsTests;
import org.radixware.kernel.explorer.tester.providers.TestsProvider;
import org.radixware.kernel.explorer.utils.DialogWatcher;
import org.radixware.kernel.explorer.views.Dialog;
import org.radixware.kernel.explorer.views.selector.Selector;


public class CreationDialogTest implements ITest {

    private boolean interrupted = false;
    private Selector selector;
    private final Id classId;
    private final String classTitle;
    private final CreationDialogTestResult testResult;
    private EntityModel modelToTest;
    private Map<Id, String> classIds2TitlesForTest;

    public CreationDialogTest(final Selector selector, final Id classId, final String title) {
        this.selector = selector;
        this.classId = classId;
        this.classTitle = title;
        this.testResult = new CreationDialogTestResult(selector);
        this.testResult.operation = TesterConstants.TEST_CREATION_DIALOG.getTitle();
    }

    @Override
    public TestsProvider createChildTestsProvider(final TestsProvider parentProvider, final TestsOptions options) {
        if (this.modelToTest != null) {
            List<EntityModel> list = new ArrayList<>();
            list.add(modelToTest);
            return new RestrictedForInsertionsEditorsTests(parentProvider.getEnvironment(), parentProvider, list);
        } else if (classIds2TitlesForTest != null && !classIds2TitlesForTest.isEmpty()) {
            return new CreationSelectDialogTestProvider(parentProvider.getEnvironment(), parentProvider, selector, classIds2TitlesForTest);
        }
        return null;
    }

    private class CreationDialogHandler implements DialogWatcher.IDialogHandler {

        private EntityModel model;
        private Map<Id, String> id2title;
        private boolean isCustomDialog = false;

        @Override
        public boolean dialogAccepted(final QWidget dialog) {
            return (ExplorerDialog.class.isInstance(dialog)
                    || SelectInstantiatableClassDialog.class.isInstance(dialog)
                    || org.radixware.kernel.explorer.views.Dialog.class.isInstance(dialog))
                    && !selector.getEnvironment().getEasSession().isBusy();
        }

        @Override
        public void doAction(final QWidget dialog) {
            if (dialog instanceof EntityEditorDialog) {
                final EntityEditorDialog entityDialog = (EntityEditorDialog) dialog;
                this.model = entityDialog.getEntityModel();
                entityDialog.reject();
            } else if (dialog instanceof SelectInstantiatableClassDialog) {
                final SelectInstantiatableClassDialog selectDialog = (SelectInstantiatableClassDialog) dialog;
                processSelectDialog(selectDialog);
            } else if (dialog instanceof org.radixware.kernel.explorer.views.Dialog) {
                final org.radixware.kernel.explorer.views.Dialog customDialog = (Dialog) dialog;
                this.isCustomDialog = true;
                customDialog.reject();
            } else if (dialog instanceof ExplorerDialog) {
                final ExplorerDialog customDialog = (ExplorerDialog) dialog;
                this.isCustomDialog = true;
                customDialog.reject();
            }
        }

        private void processSelectDialog(final SelectInstantiatableClassDialog selectDialog) {
            boolean stop = false;
            List<QObject> children = selectDialog.children();
            int i = 0, size = children.size();

            QTreeWidget tree = null;
            while (!stop && i < size) {
                QObject child = children.get(i);
                if (child instanceof QTreeWidget) {
                    tree = (QTreeWidget) child;
                    stop = true;
                } else {
                    i++;
                }
            }

            if (tree != null) {
                this.id2title = new HashMap<>();
                for (int t = 0, tSize = tree.topLevelItemCount(); t < tSize; t++) {
                    final QTreeWidgetItem item = tree.topLevelItem(t);
                    final InstantiatableClass instClass = (InstantiatableClass)item.data(0, Qt.ItemDataRole.UserRole);
                    if (instClass!=null && instClass.getId() != null) {
                        this.id2title.put(instClass.getId(), instClass.getTitle());
                    }
                    this.id2title.putAll(collectInstantiatableClassesIds(tree.topLevelItem(t)));
                }
            }

            selectDialog.reject();
        }

        private Map<Id, String> collectInstantiatableClassesIds(final QTreeWidgetItem item) {
            Map<Id, String> result = new HashMap<>();
            if (item.childCount() > 0) {
                for (int i = 0, size = item.childCount(); i < size; i++) {
                    final QTreeWidgetItem childItem = item.child(i);
                    final InstantiatableClass instClass = (InstantiatableClass)childItem.data(0, Qt.ItemDataRole.UserRole);
                    if (instClass!=null && instClass.getId() != null) {
                        result.put(instClass.getId(), instClass.getTitle());
                    }                    
                    result.putAll(collectInstantiatableClassesIds(childItem));
                }
            }
            return result;
        }

        EntityModel getOpenedModel() {
            return this.model;
        }

        Map<Id, String> getClassId2Titles() {
            return this.id2title;
        }

        boolean isCustomDialog() {
            return this.isCustomDialog;
        }
    }

    public void execute(final TestsOptions options, final TesterEnvironment env) {
        try {
            GroupModel group = selector.getGroupModel();
            if (group != null) {
                if (!group.getRestrictions().getIsCreateRestricted()) {

                    if (interrupted) {
                        return;
                    }

                    if (classId != null) {
                        this.testResult.operation = classTitle != null && !classTitle.isEmpty() ? classTitle : classId.toString();
                        this.testResult.title = this.testResult.operation;

                        this.modelToTest = group.openCreatingEntity(classId, null, null);
                        if (this.modelToTest == null) {
                            this.testResult.result = TesterConstants.RESULT_CREATIONDIALOG_NOENTITYMODEL.getTitle();
                        }
                    } else {
                        DialogWatcher watcher = DialogWatcher.getInstance();
                        CreationDialogHandler handler = new CreationDialogHandler();
                        watcher.invokeLater(handler);

                        try {
                            if (interrupted) {
                                return;
                            }

                            selector.create();
                        } finally {
                            watcher.stopWatcher();

                            if (handler.getOpenedModel() != null) {
                                this.modelToTest = handler.getOpenedModel();
                            } else if (handler.getClassId2Titles() != null) {
                                this.testResult.operation = TesterConstants.TEST_CREATION_SELECT_DIALOG.getTitle();
                                this.classIds2TitlesForTest = handler.getClassId2Titles();
                            } else if (handler.isCustomDialog()) {
                                this.testResult.result = TesterConstants.RESULT_CREATIONDIALOG_CUSTOM_OPENED.getTitle();
                            } else {
                                this.testResult.result = TesterConstants.RESULT_CREATIONDIALOG_NODIALOG.getTitle();
                            }
                        }
                    }

                } else {
                    this.testResult.result = TesterConstants.RESULT_CREATIONDIALOG_RESTRICTED.getTitle();
                }
            } else {
                this.testResult.result = TesterConstants.RESULT_CREATIONDIALOG_NOGROUPMODEL.getTitle();
            }
        } catch (Throwable ex) {
            TesterEngine.processThrowable(selector.getEnvironment(), ex, testResult);
        } finally {
            selector = null;//for GC
        }
    }

    @Override
    public TestResult getTestResult() {
        return this.testResult;
    }

    @Override
    public void interrupt() {
        this.interrupted = true;
        selector.getEnvironment().getEasSession().breakRequest();
    }
}

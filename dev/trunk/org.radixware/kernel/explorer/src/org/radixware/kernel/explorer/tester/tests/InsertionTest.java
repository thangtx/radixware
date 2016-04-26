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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.tester.TesterConstants;
import org.radixware.kernel.explorer.tester.TesterEngine;
import org.radixware.kernel.explorer.tester.TesterEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.providers.RestrictedForInsertionsEditorsTests;
import org.radixware.kernel.explorer.tester.providers.TestsProvider;

import org.radixware.kernel.explorer.views.selector.Selector;


public class InsertionTest implements ITest {

    private Selector selector;
    private InsertionTestResult testResult;
    private boolean interrupted = false;

    public InsertionTest(Selector selector) {
        this.selector = selector;
        testResult = new InsertionTestResult(selector.getEnvironment(), selector);
        testResult.operation = TesterConstants.TEST_INSERTIONS.getTitle();
    }
    private List<EntityModel> forExternalTesting;

    @Override
    public TestsProvider createChildTestsProvider(TestsProvider parentProvider, TestsOptions options) {
        if (forExternalTesting != null && !forExternalTesting.isEmpty()) {
            return new RestrictedForInsertionsEditorsTests(selector.getEnvironment(), parentProvider, forExternalTesting);
        }
        return null;
    }

    @Override
    public void execute(TestsOptions options, TesterEnvironment env) {
        try {
            GroupModel group = selector.getGroupModel();
            if (group != null) {
                if (group.isEmpty() || group.getEntitiesCount() == 0) {
                    testResult.result = TesterConstants.RESULT_EMPTY_SELECTOR.getTitle();
                } else {
                    long inserts = options.inserts;

                    if (inserts > group.getEntitiesCount() || inserts == -1) {
                        inserts = group.getEntitiesCount();
                    }

                    if (interrupted) {
                        return;
                    }

                    Set<Id> usedClasses = new HashSet<Id>();
                    int madeInserts = 0;

                    int i = 0;
                    boolean stop = false;
                    final int size = group.getEntitiesCount();

                    if (forExternalTesting == null) {
                        forExternalTesting = new ArrayList<EntityModel>();
                    } else {
                        forExternalTesting.clear();
                    }

                    boolean containsRecursive = false;
                    selector.getActions().getInsertAction().isEnabled();
                    if (!group.getRestrictions().getIsInsertIntoTreeRestricted()) {
                        while (!stop && i < size && madeInserts < inserts) {
                            EntityModel model = group.getEntity(i);

                            if (interrupted) {
                                return;
                            }

                            Id classId = model.getClassPresentationDef().getId();
                            if (!usedClasses.contains(classId)) {
                                if (!isRecursiveEntity(group, model)) {
                                    selector.setCurrentEntity(model);
                                    usedClasses.add(classId);
                                    testResult.addInsertedClass(model.getClassPresentationDef());
                                    selector.insertEntity();
                                    madeInserts++;
                                } else {
                                    if (!containsRecursive) {
                                        containsRecursive = true;
                                    }
                                }
                            }
                            if (madeInserts == inserts) {
                                stop = true;
                            } else {
                                i++;
                            }
                        }

                        if (madeInserts < inserts && !stop) {
                            EntityModel current = selector.getCurrentEntity();
                            int c = 0;
                            stop = false;
                            while (!stop && c < size) {
                                if (group.getEntity(c).equals(current)) {
                                    stop = true;
                                    c++;
                                } else {
                                    c++;
                                }
                            }

                            while (c < size && madeInserts < inserts) {
                                if (interrupted) {
                                    return;
                                }

                                EntityModel model = group.getEntity(c);
                                if (!isRecursiveEntity(group, model)) {
                                    selector.setCurrentEntity(model);
                                    selector.insertEntity();
                                    madeInserts++;
                                } else {
                                    if (!containsRecursive) {
                                        containsRecursive = true;
                                    }
                                }
                                c++;
                            }
                        }
                    }

                    if (madeInserts > 0) {
                        if (!containsRecursive) {
                            testResult.result = TesterConstants.RESULT_INSERTION_SCS.getTitle();
                        } else {
                            testResult.result = TesterConstants.RESULT_INSERTION_RECURSIVE.getTitle();
                        }
                    } else {
                        testResult.result = TesterConstants.RESULT_INSERTION_RESTRICTED.getTitle();
                        int e = 0;
                        while (e < inserts && e < group.getEntitiesCount()) {
                            forExternalTesting.add(group.getEntity(e));
                            e++;
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            TesterEngine.processThrowable(selector.getEnvironment(), ex, testResult);
        }
    }

    private boolean isRecursiveEntity(GroupModel group, EntityModel model) {
        final List<EntityModel> parentEntities = ((ExplorerItemView) group.findNearestExplorerItemView()).getParentEntityModels();

        if (parentEntities.size() > 0) {
            int i = 0;
            EntityModel holderModel = parentEntities.get(i);

            while (i < parentEntities.size()) {
                Pid groupPid = holderModel.getPid();
                if (groupPid != null && groupPid.equals(model.getPid())) {
                    return true;
                } else {
                    i++;
                    if (i < parentEntities.size()) {
                        holderModel = parentEntities.get(i);
                    }
                }
            }
        }

        return false;
    }

    @Override
    public TestResult getTestResult() {
        return testResult;
    }

    @Override
    public void interrupt() {
        interrupted = true;
        selector.getEnvironment().getEasSession().breakRequest();
    }
}

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

package org.radixware.kernel.designer.ads.editors.refactoring.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.ads.editors.refactoring.IRefactoringSteps;
import org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringSteps;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfg;
import org.radixware.kernel.designer.common.dialogs.usages.UsagesFinder;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;


public final class ObjectUsagesStep extends RefactoringSteps.RefactoringStep<ObjectUsagesPanel> {

    public interface IFilter {

        boolean accept(RadixObject object);

        boolean subSearch(RadixObject object);

        boolean isCheckable(RadixObject object, boolean isLeaf);

        boolean isSelected(RadixObject object, boolean isLeaf);
    }

    public interface IObjectUsagesSettings extends RefactoringSteps.ISettings {

        Map<RadixObject, List<RadixObject>> getSelectedUsages();

        Map<RadixObject, List<RadixObject>> getAllUsages();

        void setUsages(Map<RadixObject, List<RadixObject>> all, Map<RadixObject, List<RadixObject>> selected);

        Definition getDefinition();

        IFilter getUsagesFilter();
    }

    public interface IObjectUsagesStep extends IRefactoringSteps {

        @Override
        IObjectUsagesSettings getSettings();
    }
    private IObjectUsagesStep refactoringSteps;
    private volatile boolean isReady = false;
    private final boolean requireUsages;
    private Map<RadixObject, List<RadixObject>> allUsages;
    private final String title;

    public ObjectUsagesStep(IObjectUsagesStep refactoringSteps, boolean requireUsages, String title) {
        this.refactoringSteps = refactoringSteps;
        this.requireUsages = requireUsages;
        this.title = title != null ? title : "Usages";
    }

    private class FindUsagesOperation implements Callable<Void> {

        @Override
        public synchronized Void call() {
            final ProgressHandle createHandle = ProgressHandleFactory.createHandle("Find usages...");
            final Map<RadixObject, List<RadixObject>> result = new HashMap<>();
            final Queue<Definition> queue = new LinkedList<>();
            final Set<RadixObject> used = new HashSet<>();
            queue.add(refactoringSteps.getSettings().getDefinition());
            try {
                createHandle.start();

                while (!queue.isEmpty()) {
                    final Definition curr = queue.poll();
                    final UsagesFinder finder = new UsagesFinder(new FindUsagesCfg(curr));
                    final Map<RadixObject, List<RadixObject>> usages = finder.search();

                    final IFilter usagesFilter = refactoringSteps.getSettings().getUsagesFilter();

                    used.add(curr);
                    
                    for (final Map.Entry<RadixObject, List<RadixObject>> entry : usages.entrySet()) {
                        if (usagesFilter.accept(entry.getKey())) {
                            result.put(entry.getKey(), entry.getValue());
                            for (final RadixObject obj : entry.getValue()) {
                                if (obj instanceof Definition && !used.contains(obj) && usagesFilter.subSearch(obj)) {
                                    queue.add((Definition) obj);
                                }
                            }
                        }
                    }
                }

                allUsages = result;
            } finally {
                createHandle.finish();
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    getVisualPanel().open(result, refactoringSteps.getSettings().getUsagesFilter());
                    isReady = true;
                    fireChange();
                }
            });
            return null;
        }
    }
    private final FindUsagesOperation findUsagesOperation = new FindUsagesOperation();
    private Future<?> findUsagesTask;

    @Override
    public IObjectUsagesSettings getSettings() {
        return refactoringSteps.getSettings();
    }

    @Override
    public String getDisplayName() {
        return title;
    }

    @Override
    protected ObjectUsagesPanel createVisualPanel() {
        ObjectUsagesPanel methodUsagesPanel = new ObjectUsagesPanel();
        methodUsagesPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fireChange();
            }
        });
        return methodUsagesPanel;
    }

    @Override
    public boolean isFinishiable() {
        return false;
    }

    @Override
    public boolean hasNextStep() {
        return true;
    }

    @Override
    public WizardSteps.Step createNextStep() {
        return new RefactoringStatusStep();
    }

    @Override
    public void open(Object settings) {
        refactoringSteps.getSettings().setUsages(null, null);
        final ObjectUsagesPanel visualPanel = getVisualPanel();
        visualPanel.open();
        if (findUsagesTask != null) {
            findUsagesTask.cancel(true);
        }
        findUsagesTask = refactoringSteps.getExecutor().submit(findUsagesOperation);
    }

    @Override
    public void apply() {
        if (findUsagesTask != null) {
            findUsagesTask.cancel(true);
        }

        refactoringSteps.getSettings().setUsages(allUsages, getVisualPanel().getSelectedDefinitions());
    }

    @Override
    public void cancel(Object settings) {
        if (findUsagesTask != null) {
            findUsagesTask.cancel(true);
        }
    }

    @Override
    protected OperationStatus check() {
        if (allUsages != null) {
            final OperationStatus status = new OperationStatus();

            loop:
            for (final List<RadixObject> objects : allUsages.values()) {
                for (final RadixObject obj : objects) {
                    if (obj.isReadOnly()) {
                        status.addEvent(new OperationStatus.Event(OperationStatus.EEventType.WARNING, "Usages contains readonly objects"));
                        break loop;
                    }
                }
            }

            return status;
        }

        return OperationStatus.OK;
    }

    @Override
    protected boolean isReady() {
        return isReady && (!requireUsages || isUsed());
    }

    public boolean isUsed() {
        final Map<RadixObject, List<RadixObject>> usages = getVisualPanel().getSelectedDefinitions();
        if (usages != null && !usages.isEmpty()) {
            for (List<RadixObject> i : usages.values()) {
                if (!i.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}

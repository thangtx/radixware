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
package org.radixware.kernel.designer.common.dialogs.check;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.check.ProblemAnnotationFactory.SpellingAnnotation;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.check.RadixProblemRegistry.ChangedEvent;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.common.components.FilteredTreeView;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.designer.common.dialogs.results.ResultsTree;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsages;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfg;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfgPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.check.spelling.UserDictionaryManager;

class CheckResultsTree extends ResultsTree {

    private static final Color ERROR_COLOR = Color.RED;
    private static final Color WARNING_COLOR = new Color(128, 0, 0);

    private class Head extends RadixObjectItem {

        public Head(RadixObject radixObject) {
            super(radixObject);
        }
    }

    private class ProblemItem extends FilteredTreeView.Item {

        public ProblemItem(RadixProblem problem) {
            super(problem);

            final String message = problem.getMessage();
            final boolean error = problem.getSeverity() == RadixProblem.ESeverity.ERROR;
            final EEventSeverity severity = error ? EEventSeverity.ERROR : EEventSeverity.WARNING;
            final Icon icon = RadixObjectIcon.getForSeverity(severity).getIcon();
            final Color color = error ? ERROR_COLOR : WARNING_COLOR;

            setDisplayName(message);
            setIcon(icon);
            setColor(color);
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        public RadixProblem getProblem() {
            return (RadixProblem) super.getUserObject();
        }

        public Head getHead() {
            return (Head) super.getParent();
        }

        public boolean isWarning() {
            return getProblem().getSeverity() == RadixProblem.ESeverity.WARNING;
        }

        public boolean isError() {
            return getProblem().getSeverity() == RadixProblem.ESeverity.ERROR;
        }
    }
    private RadixProblemRegistry.IChangeListener problemRegistryChangeListener = new RadixProblemRegistry.IChangeListener() {
        @Override
        public void onEvent(final ChangedEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    switch (e.getChangeType()) {
                        case ADDED:
                            for (RadixProblem problem : e.getChangedProblems()) {
                                add(problem);
                            }
                            break;
                        case CLEARED:
                            clear();
                            break;
                        case REMOVED:
                            for (RadixProblem problem : e.getChangedProblems()) {
                                remove(problem);
                            }
                            break;
                    }
                }
            });
        }
    };
    private ICheckResultsFilter filter = null;
    private int errorsCount = 0;
    private int warningsCount = 0;
    private int displayedErrorsCount = 0;
    private int displayedWarningsCount = 0;

    public CheckResultsTree() {
        super();

        // fill initial problems
        final Set<RadixProblem> problems = RadixProblemRegistry.getDefault().getAllProblemSet();
        for (RadixProblem problem : problems) {
            add(problem);
        }

        // start listen problems
        RadixProblemRegistry.getDefault().addChangeListener(problemRegistryChangeListener);
    }

    private void add(RadixProblem p) {
        final RadixObject source = p.getSource();
        final RadixObject headObject = getDisplayedObject(source);

        Head head = (Head) findItemByUserObject(headObject);
        final boolean needAddToTree;
        if (head == null) {
            head = new Head(headObject);
            needAddToTree = true;
        } else {
            needAddToTree = false;
        }

        final ProblemItem problemItem = new ProblemItem(p);
        head.add(problemItem);
        applyFilter(head);

        if (needAddToTree) {
            getRoot().add(head);
        }

        final boolean isDisplay = problemItem.isVisible() && head.isVisible();

        // counters
        adjustCounters(problemItem.isError(), isDisplay, 1);
    }

    private void remove(RadixProblem problem) {
        final ProblemItem problemItem = (ProblemItem) findItemByUserObject(problem);
        if (problemItem == null) {
            return;
        }

        final Head head = problemItem.getHead();

        final boolean isDisplay = problemItem.isVisible() && head.isVisible();

        problemItem.remove();

        if (head.isEmpty()) {
            if (problem.getSource().isInBranch()) {
                head.setVisible(false);
            } else {
                head.remove();
            }
        }

        // counters
        adjustCounters(problemItem.isError(), isDisplay, -1);
    }

    private void adjustCounters(boolean err, boolean display, int delta) {

        if (err) {
            errorsCount += delta;
            if (display) {
                displayedErrorsCount += delta;
            }
        } else {
            warningsCount += delta;
            if (display) {
                displayedWarningsCount += delta;
            }
        }
        onCountersChanged();
    }

    private void applyFilter(Head head) {
        final String pattern = (filter != null ? filter.getText().toLowerCase() : "");
        final boolean warnings = filter == null || filter.isWarningsDisplayed();
        final boolean errors = filter == null || filter.isErrorsDisplayed();
        final Collection<? extends RadixObject> rootObjects = (filter != null ? filter.getRootObjects() : null);

        final RadixObject radixObject = head.getRadixObject();
        final boolean headSatisfiedToRoots = isSatisfiedToRoot(radixObject, rootObjects);
        if (!headSatisfiedToRoots) {
            head.setVisible(false);
            return;
        }

        boolean headSatistiedToText = pattern.isEmpty() || head.getDisplayName().toLowerCase().contains(pattern);

        boolean someItemDisplayed = false;

        for (Item child : new ArrayList<>(head.getChildren())) {
            final ProblemItem problemItem = (ProblemItem) child;
            if (!warnings && problemItem.isWarning()) {
                problemItem.setVisible(false);
                continue;
            }
            if (!errors && problemItem.isError()) {
                problemItem.setVisible(false);
                continue;
            }

            final boolean itemSatistiedToText = (!pattern.isEmpty() && headSatistiedToText) || (pattern.isEmpty() || problemItem.getDisplayName().toLowerCase().contains(pattern));

            problemItem.setVisible(itemSatistiedToText);

            if (itemSatistiedToText) {
                someItemDisplayed = true;
            }
        }

        head.setVisible(someItemDisplayed);
    }

    protected void setFilter(ICheckResultsFilter filter) {
        if (this.filter != filter) {
            this.filter = filter;
            onFilterChanged();
        }
    }

    @Override
    protected void onFilterChanged() {
        for (final Item child : getRootChildren()) {
            final Head head = (Head) child;
            applyFilter(head);
        }

        updateCounters();
    }

    private List<Item> getRootChildren() {
        return new ArrayList<>(getRoot().getChildren());
    }

    private void updateCounters() {
        int newDisplayedErrorsCount = 0;
        int newDisplayedWarningsCount = 0;

        for (final Item child : getRootChildren()) {
            if (child.isVisible()) {
                for (final Item subChild : new ArrayList<>(child.getChildren())) {
                    final ProblemItem problemItem = (ProblemItem) subChild;
                    if (problemItem.isVisible()) {
                        if (problemItem.isError()) {
                            newDisplayedErrorsCount++;
                        } else {
                            newDisplayedWarningsCount++;
                        }
                    }
                }
            }
        }

        if (displayedErrorsCount != newDisplayedErrorsCount || displayedWarningsCount != newDisplayedWarningsCount) {
            displayedErrorsCount = newDisplayedErrorsCount;
            displayedWarningsCount = newDisplayedWarningsCount;
            onCountersChanged();
        }
    }
    private RadixEventSource countersSupport = new RadixEventSource();

    public void onCountersChanged() {
        countersSupport.fireEvent(new RadixEvent());
    }

    public void addCountersChangeListener(IRadixEventListener l) {
        countersSupport.addEventListener(l);
    }

    public void removeCountersChangeListener(IRadixEventListener l) {
        countersSupport.addEventListener(l);
    }

    public int getDisplayedErrorsCount() {
        return displayedErrorsCount;
    }

    public int getDisplayedWarningsCount() {
        return displayedWarningsCount;
    }

    public int getErrorsCount() {
        return errorsCount;
    }

    public int getWarningsCount() {
        return warningsCount;
    }

    @Override
    public void clear() {
        if (getRoot().isEmpty()) {
            return;
        }
        getRoot().clear();
        errorsCount = 0;
        warningsCount = 0;
        displayedErrorsCount = 0;
        displayedWarningsCount = 0;
        onCountersChanged();
    }

    @Override
    public void goToLastSelectedObject() {
        final Object userObject = getLastSelectedUserObject();
        if (userObject instanceof RadixObject) {
            final RadixObject radixObject = (RadixObject) userObject;
            DialogUtils.goToObject(radixObject);
        } else if (userObject instanceof RadixProblem) {
            final RadixProblem problem = (RadixProblem) userObject;
            final RadixObject source = problem.getSource();
            final OpenInfo openInfo = new OpenInfo(source, Lookups.fixed(problem));
            DialogUtils.goToObject(source, openInfo);
        }
    }

    @Override
    public void findUsagesOfLastSelectedObject() {
        final Object userObject = getLastSelectedUserObject();
        if (userObject instanceof Definition) {
            final Definition def = (Definition) userObject;
            final FindUsagesCfg cfg = FindUsagesCfgPanel.askCfg(def);
            if (cfg != null) {
                FindUsages.search(cfg);
            }
        } else if (userObject instanceof RadixProblem) {
            final RadixProblem problem = (RadixProblem) userObject;
            final RadixObject source = problem.getSource();
            if (source instanceof Definition) {
                final Definition def = (Definition) source;
                final FindUsagesCfg cfg = FindUsagesCfgPanel.askCfg(def);
                if (cfg != null) {
                    FindUsages.search(cfg);
                }
            }
        }
    }

    private RadixProblem getCurrentProblem() {
        final Node[] selection = getSelectedNodes();
        if (selection == null || selection.length == 0) {
            return null;
        } else {
            Object obj = selection[0].getUserObject();
            if (obj instanceof RadixProblem) {
                return (RadixProblem) obj;
            } else {
                return null;
            }
        }
    }

    @Override
    public Action[] getCustomActions() {
        RadixProblem p = getCurrentProblem();
        if (p != null) {
            int code = p.getCode();
            List<Action> actions = new LinkedList<>();

            if (p.getKind() == RadixProblem.EProblemKind.SPELLING) {
                actions.add(new AddToUserDictionaryAction(p));
            }

            if (p.getSeverity() == RadixProblem.ESeverity.WARNING) {
                if (code != RadixProblem.Codes.UNKNOWN) {
                    RadixObject src = p.getSource();
                    if (src != null) {

                        RadixProblem.ProblemFixSupport fs = src.getProblemFixSupport();
                        if (fs != null) {
                            List<RadixProblem.ProblemFixSupport.Fix> fixes = new LinkedList<RadixProblem.ProblemFixSupport.Fix>();
                            if (fs.canFix(code, fixes)) {
                                for (RadixProblem.ProblemFixSupport.Fix fix : fixes) {
                                    actions.add(new FixProblemAction(fix));
                                }
                            }
                        }
                        RadixProblem.WarningSuppressionSupport support = src.getWarningSuppressionSupport(true);
                        if (support != null && support.canSuppressWarning(code)) {
                            if (!actions.isEmpty()) {
                                actions.add(null);
                            }
                            actions.add(new SuppressWarningAction(src, code));
                        }

                        if (!actions.isEmpty()) {
                            return actions.toArray(new Action[]{});
                        }
                    }
                } else {
                    RadixObject src = p.getSource();
                    if (src != null) {
                        if (p.getNativeCode() > 0) {
                            actions.add(new SuppressCompilerWarningAction(src, p.getNativeCode()));
                        }
                    }
                    if (!actions.isEmpty()) {
                        return actions.toArray(new Action[]{});
                    }
                }
            }
        }
        return null;
    }

    private static final class AddToUserDictionaryAction extends AbstractAction {

        static final String ACTION_NAME = NbBundle.getMessage(CheckResultsTree.class, "AddToUserDictionaryAction.Name");
        RadixProblem problem;

        public AddToUserDictionaryAction(RadixProblem problem) {
            super(ACTION_NAME, RadixWareIcons.CREATE.ADD.getIcon());
            this.problem = problem;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SpellingAnnotation annotation = (SpellingAnnotation) problem.getAnnotation();
            UserDictionaryManager.addWord(annotation.getWord(), "", problem.getSource().getLayer(), annotation.getLanguage());
        }
    }

    private final class SuppressWarningAction extends AbstractAction {

        private final RadixObject src;
        private final int code;

        public SuppressWarningAction(RadixObject src, int code) {
            super("Suppress warning: \'" + RadixProblem.getProblemDescription(src, code) + "\'", RadixWareIcons.EVENT_LOG.getForSeverity(EEventSeverity.WARNING).getIcon());
            this.src = src;
            this.code = code;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Module m = src.getModule();
            if (m != null && m instanceof DdsModule) {
                final DdsModelDef modifiedModel = ((DdsModule) m).getModelManager().getModifiedModelIfLoaded();
                if (modifiedModel == null || !modifiedModel.getModifierInfo().isOwn()) {
                    DialogUtils.messageInformation("Please capture DDS module '" + m.getQualifiedName() + "' to suppress this warning");
                    return;
                }
            }
            RadixProblem.WarningSuppressionSupport s = src.getWarningSuppressionSupport(true);
            if (s != null && s.canSuppressWarning(code)) {
                s.suppressWarnings(code, true);
            }
        }
    }

    private final class SuppressCompilerWarningAction extends AbstractAction {

        private final RadixObject src;
        private final int code;

        public SuppressCompilerWarningAction(RadixObject src, int code) {
            super("Suppress compiler warning");
            this.src = src;
            this.code = code;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AdsDefinition def = getOwnerAdsDefinition();
            if (def == null) {
                DialogUtils.messageError("Unable suppress compiler warning because no owner ADS definition found");
            }
            def.addCompilerWarning(code);
        }

        private AdsDefinition getOwnerAdsDefinition() {
            Definition def = src.getDefinition();
            while (def != null) {
                if (def instanceof AdsDefinition) {
                    return (AdsDefinition) def;
                }
                def = def.getOwnerDefinition();
            }
            return null;
        }
    }

    private final class FixProblemAction extends AbstractAction {

        private final RadixProblem.ProblemFixSupport.Fix fix;

        public FixProblemAction(RadixProblem.ProblemFixSupport.Fix fix) {
            super(fix.getDescription(), RadixWareIcons.CHECK.CHECK.getIcon());
            this.fix = fix;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fix.fix();
        }
    }
}

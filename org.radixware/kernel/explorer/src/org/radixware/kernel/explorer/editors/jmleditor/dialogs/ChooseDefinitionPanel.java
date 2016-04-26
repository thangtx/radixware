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
package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.QThread;
import com.trolltech.qt.gui.QShowEvent;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import static org.radixware.kernel.explorer.editors.jmleditor.dialogs.BaseChoosePanel.LOAD_ITEM_COUNT;
import org.radixware.kernel.explorer.env.Application;

public class ChooseDefinitionPanel extends BaseChoosePanel {

    private Collection<DefInfo> allowedDefinitions;
    protected AdsUserFuncDef userFunc;
    private Set<EDefType> templList;
    private MyRunnable task;
    private QThread thread;
    private boolean complete = false;
    private boolean wasShown = false;
    private String findedText = null;
    private Lookup.IDefInfoFilter defFilter;
    public Signal0 onDataReadySignal = new Signal0();

    public ChooseDefinitionPanel(final IChooseDefFromList parent, final Collection<DefInfo> allowedDefinitions, final AdsUserFuncDef userFunc, Set<EDefType> templList, final boolean isDbEntity, final boolean isParentRef) {
        this(parent, allowedDefinitions, userFunc, templList, isDbEntity, isParentRef, null, null);
    }
    
    public ChooseDefinitionPanel(final IChooseDefFromList parent, Collection<DefInfo> allowedDefinitions, final AdsUserFuncDef userFunc, Set<EDefType> templList, final boolean isDbEntity, final boolean isParentRef, final IFilter<AdsDefinition> filter, final Lookup.IDefInfoFilter defFilter) {
        this(parent, allowedDefinitions, userFunc, templList, isDbEntity, false, isParentRef, filter, defFilter);
    }

    public ChooseDefinitionPanel(final IChooseDefFromList parent, Collection<DefInfo> allowedDefinitions, final AdsUserFuncDef userFunc, Set<EDefType> templList, final boolean isDbEntity, final boolean isForParamBinding, final boolean isParentRef, final IFilter<AdsDefinition> filter, final Lookup.IDefInfoFilter defFilter) {
        super(parent);
        this.allowedDefinitions = allowedDefinitions;
        this.userFunc = userFunc;
        this.templList = templList;
        this.defFilter = defFilter;
        createListUi();
        this.setEnabled(false);
        task = new MyRunnable(false, isDbEntity, isForParamBinding, isParentRef, filter, defFilter);
        thread = new QThread(task);
    }

    @Override
    protected void showEvent(final QShowEvent e) {
        super.showEvent(e);
        if ((!wasShown) && (e.type() == QShowEvent.Type.Show)) {
            wasShown = true;
            if (allowedDefinitions == null) {
                thread.finished.connect(this, "onDataReady()");
                thread.start();
            } else {
                fillList(allowedDefinitions);
            }
        } else if (wasShown) {
            setFocusToFilter();
        }
    }

    public void closeTread() {
        setVisibleForList(false);
        if (thread.isAlive()) {
            thread.finished.disconnect();
            thread.interrupt();
        }
    }

    @SuppressWarnings("unused")
    private void onDataReady() {
        fillList(task.getDefList());
        complete = true;
        onDataReadySignal.emit();
    }

    public boolean isComplete() {
        return complete;
    }

    public void setDefFilter(final Lookup.IDefInfoFilter defFilter) {
        this.defFilter = defFilter;
        doFilterDefList(true, findedText);
    }

    private class MyRunnable implements Runnable {

        private boolean needExplorerClass;
        private boolean isDbEntity;
        private boolean isForParamBinding;
        private boolean isParentRef;
        private Collection<DefInfo> definitions;
        private IFilter<AdsDefinition> filter;
        private Lookup.IDefInfoFilter defFilter;

        MyRunnable(final boolean needExplorerClass, final boolean isDbEntity, final boolean isForParamBinding, final boolean isParentRef, final IFilter<AdsDefinition> filter, final Lookup.IDefInfoFilter defFilter) {
            this.needExplorerClass = needExplorerClass;
            this.isDbEntity = isDbEntity;
            this.isForParamBinding = isForParamBinding;
            this.isParentRef = isParentRef;
            this.filter = filter;
            this.defFilter = defFilter;
        }

        private List<DefInfo> getDefs(final Set<EDefType> templList, final boolean needExplorerClass, final boolean isForObject, final boolean isForParamBinding, final boolean isParentRef) {
            final Collection<DefInfo> defs = Lookup.listTopLevelDefinitions(userFunc, templList);
            final List<DefInfo> res = new ArrayList<>(defs.size() / 2);

            for (DefInfo def : defs) {
                if (!checkRestrictions(def, isForObject, isForParamBinding, isParentRef)) {
                    continue;
                }
                boolean matches = true;

                if (!needExplorerClass) {
                    matches = (def.getEnvironment() == ERuntimeEnvironmentType.COMMON) || (def.getEnvironment() == ERuntimeEnvironmentType.SERVER);
                }
                if (matches) {
                    if (defFilter != null) {
                        if (!defFilter.isTarget(def)) {
                            continue;
                        }
                    }
                    if (filter != null) {
                        AdsDefinition adsdef = def.getDefinition();
                        if (adsdef != null) {
                            if (filter.isTarget(adsdef)) {
                                res.add(def);
                            }
                        } else {
                            res.add(def);
                        }
                    } else {
                        res.add(def);
                    }
                }
            }
            Collections.sort(res, new Comparator<DefInfo>() {
                @Override
                public int compare(DefInfo o1, DefInfo o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return res;
        }

        private boolean checkRestrictions(final DefInfo info, final boolean isForObject, final boolean isForParamBinding, final boolean isParentRef) {
            if (isForObject) {
                EnumSet<EDefinitionIdPrefix> defTypes = EnumSet.of(EDefinitionIdPrefix.ADS_ENTITY_CLASS);
                if(isForParamBinding) {
                    defTypes.add(EDefinitionIdPrefix.ADS_APPLICATION_CLASS);
                }
                final Id[] path = info.getPath();
                if (defTypes.contains(path[0].getPrefix())) {
                    final AdsDefinition def = Lookup.findTopLevelDefinition(userFunc, path[0]);
                    if (def != null) {
                        final RadClassPresentationDef classDef = Application.getInstance().getDefManager().getClassPresentationDef(path[0]);
                        final RadSelectorPresentationDef selPresDef = classDef.getDefaultSelectorPresentation();
                        if (selPresDef != null) {
                            final Restrictions selectorRestrictions = selPresDef.getRestrictions();
                            if (!selectorRestrictions.getIsContextlessUsageRestricted()) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            } else if (isParentRef) {
                final Id[] path = info.getPath();
                return path[0].getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS || path[0].getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS;
            }
            return true;
        }

        @Override
        public void run() {
            try {
                if (allowedDefinitions == null) {
                    definitions = getDefs(templList, needExplorerClass, isDbEntity, isForParamBinding, isParentRef);
                }
            } catch (Exception ex) {
                Logger.getLogger(ChooseObjectDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public Collection<DefInfo> getDefList() {
            return definitions;
        }
    }

    protected void fillList(final Collection<DefInfo> allowedDefinitions) {
        setVisibleForList(true);
        if (allowedDefinitions != null) {
            update(allowedDefinitions);
        }
        this.setEnabled(true);
        setFocusToFilter();
    }

    private void update(final Collection<DefInfo> allowedDefinitions) {
        this.allowedDefinitions = allowedDefinitions;
        setDefinitionList(LOAD_ITEM_COUNT);
    }

    protected void doFilterDefList(boolean newStart, String inputString) {
        if (allowedDefinitions == null) {
            return;
        }
        setLoadedDefsCount(LOAD_ITEM_COUNT);
        final LinkedList<DefInfo> defList = new LinkedList<>();
        if (!newStart && findedText != null && inputString.startsWith(findedText)) {
            defList.addAll(getDefList());
        } else {
            defList.addAll(allowedDefinitions);
        }

        final boolean doCheckName = inputString != null && (!"".equals(inputString)) && (!"*".equals(inputString));
        for (int i = 0; i < defList.size(); i++) {
            if ((doCheckName && !checkName(inputString, defList.get(i).getName())) || (defFilter != null && !defFilter.isTarget(defList.get(i)))) {
                defList.remove(i);
                i--;
            }
        }
        updateModelForDef(defList);
    }

    @Override
    protected void findTextChanged(final String s) {
        doFilterDefList(false, s);
        findedText = s;
    }

    @Override
    protected BaseModelList createModelList(final int count, Collection<DefInfo> definitionList) {
        if (definitionList == null) {
            definitionList = allowedDefinitions;
        }
        final ListModel dm = new ListModel(null);
        final List<DefInfo> defList = new ArrayList<>();
        if (definitionList != null) {
            int index = 0;
            for (DefInfo def : definitionList) {
                if (index >= count) {
                    break;
                }
                defList.add(def);
                index++;
            }
            if (index >= definitionList.size()) {
                setLoadedDefsCount(-1);
            }
        }
        dm.setDefList(defList);
        return dm;
    }
}

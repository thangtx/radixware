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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.ads.AdsSqmlEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.SelectorPresentationDefinition;


public class SelectorAddons extends RadixObject {

    private boolean isAnyBaseFilterEnabled;
    private boolean isAnyBaseSortingEnabled;
    private boolean isCustomFilterEnabled;
    private boolean isCustomSortingEnabled;
    private boolean filterIsObligatory;
    
    private Id defaultFilterId;
    private Id defaultSortingId;
    final AdsSqmlEnvironment env = AdsSqmlEnvironment.Factory.newInstance(this);
    private final Sqml defaultHint = new Hint();
    private List<Id> enabledFilters;
    private List<Id> enabledSortings;
    
    private final class Hint extends Sqml {

        public Hint() {
            super(SelectorAddons.this);
            setEnvironment(env);
        }
    }

    SelectorAddons(AdsSelectorPresentationDef owner, SelectorPresentationDefinition.Addons xDef) {
        setContainer(owner);
        this.isAnyBaseFilterEnabled = xDef.getAnyBaseFilterEnabled();
        this.isAnyBaseSortingEnabled = xDef.getAnyBaseSortingEnabled();
        this.isCustomFilterEnabled = xDef.getCustomFilterEnabled();
        this.isCustomSortingEnabled = xDef.getCustomSortingEnabled();
        this.defaultFilterId = xDef.getDefaultFilterId();
        this.defaultHint.loadFrom(xDef.getDefaultHint());
        //this.autoExpand = xDef.getAutoExpand();
        this.defaultSortingId = xDef.getDefaultSortingId();

        if (xDef.getEnabledFilterIds() != null && !xDef.getEnabledFilterIds().isEmpty()) {
            this.enabledFilters = new ArrayList<Id>(xDef.getEnabledFilterIds());

        } else {
            this.enabledFilters = null;
        }

        if (xDef.getEnabledSortingIds() != null && !xDef.getEnabledSortingIds().isEmpty()) {
            this.enabledSortings = new ArrayList<Id>(xDef.getEnabledSortingIds());
        } else {
            this.enabledSortings = null;
        }
        xDef.getEnabledSortingIds();
        this.filterIsObligatory = xDef.getFilterIsObligatory();        
    }

    SelectorAddons(AdsSelectorPresentationDef owner) {
        setContainer(owner);
        this.isAnyBaseFilterEnabled = false;
        this.isAnyBaseSortingEnabled = false;
        this.isCustomFilterEnabled = false;
        this.isCustomSortingEnabled = false;
        this.defaultFilterId = null;
        this.defaultSortingId = null;
        this.enabledFilters = null;
        this.enabledSortings = null;
        this.filterIsObligatory = false;
    }

    public void appendTo(SelectorPresentationDefinition.Addons xDef) {
        xDef.setAnyBaseFilterEnabled(this.isAnyBaseFilterEnabled);
        xDef.setAnyBaseSortingEnabled(this.isAnyBaseSortingEnabled);
        xDef.setCustomFilterEnabled(this.isCustomFilterEnabled);
        xDef.setCustomSortingEnabled(this.isCustomSortingEnabled);
        xDef.setFilterIsObligatory(this.filterIsObligatory);
        
        if (this.defaultFilterId != null) {
            xDef.setDefaultFilterId(this.defaultFilterId);
        }
        this.defaultHint.appendTo(xDef.addNewDefaultHint());

        if (this.defaultSortingId != null) {
            xDef.setDefaultSortingId(this.defaultSortingId);
        }
        if (this.enabledFilters != null && !this.enabledFilters.isEmpty()) {
            xDef.setEnabledFilterIds(this.enabledFilters);
        }
        if (this.enabledSortings != null && !this.enabledSortings.isEmpty()) {
            xDef.setEnabledSortingIds(this.enabledSortings);
        }        
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.defaultHint.visit(visitor, provider);
    }

    public Id getDefaultFilterId() {
        return defaultFilterId;
    }

    public AdsFilterDef findDefaultFilter() {
        return findFilterById(defaultFilterId);
    }

    public AdsFilterDef findFilterById(Id id) {
        if (id == null) {
            return null;
        } else {
            return getOwnerSelectorPresentation().getOwnerClass().getPresentations().getFilters().findById(id, EScope.ALL).get();
        }
    }

    public void setDefaultFilterId(Id defaultFilterId) {
        this.defaultFilterId = defaultFilterId;
        setEditState(EEditState.MODIFIED);
    }

    public Sqml getDefaultHint() {
        return defaultHint;
    }

    public Id getDefaultSortingId() {
        return defaultSortingId;
    }

    public AdsSortingDef findDefaultSorting() {
        return findSortingById(defaultSortingId);
    }

    public AdsSortingDef findSortingById(Id id) {
        if (id == null) {
            return null;
        } else {
            return getOwnerSelectorPresentation().getOwnerClass().getPresentations().getSortings().findById(id, EScope.ALL).get();
        }
    }

    public void setDefaultSortingId(Id defaultSortingId) {
        this.defaultSortingId = defaultSortingId;
        setEditState(EEditState.MODIFIED);
    }

    public List<Id> getEnabledFilterIds() {
        if (enabledFilters == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<Id>(enabledFilters);
        }
    }

    public boolean addEnabledFilterId(Id id) {
        if (this.enabledFilters == null || !this.enabledFilters.contains(id)) {
            if (this.enabledFilters == null) {
                this.enabledFilters = new ArrayList<Id>();
            }
            if (this.enabledFilters.add(id)) {
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean removeEnabledFilterId(Id id) {
        if (this.enabledFilters == null) {
            return false;
        }
        if (this.enabledFilters.contains(id)) {
            if (this.enabledFilters.remove(id)) {
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<Id> getEnabledSortingIds() {
        if (enabledSortings == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<Id>(enabledSortings);
        }
    }

    public boolean addEnabledSortingId(Id id) {
        if (this.enabledSortings == null || !this.enabledSortings.contains(id)) {
            if (this.enabledSortings == null) {
                this.enabledSortings = new ArrayList<Id>();
            }
            if (this.enabledSortings.add(id)) {
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean removeEnabledSortingId(Id id) {
        if (this.enabledSortings == null) {
            return false;
        }
        if (this.enabledSortings.contains(id)) {
            if (this.enabledSortings.remove(id)) {
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isFilterObligatory() {
        return filterIsObligatory;
    }

    public void setFilterIsObligatory(boolean filterIsObligatory) {
        this.filterIsObligatory = filterIsObligatory;
        setEditState(EEditState.MODIFIED);
    }

    public boolean isAnyBaseFilterEnabled() {
        return isAnyBaseFilterEnabled;
    }

    public void setAnyBaseFilterEnabled(boolean isAnyBaseFilterEnabled) {
        this.isAnyBaseFilterEnabled = isAnyBaseFilterEnabled;
        setEditState(EEditState.MODIFIED);
    }

    public boolean isAnyBaseSortingEnabled() {
        return isAnyBaseSortingEnabled;
    }

    public void setAnyBaseSortingEnabled(boolean isAnyBaseSortingEnabled) {
        this.isAnyBaseSortingEnabled = isAnyBaseSortingEnabled;
        setEditState(EEditState.MODIFIED);
    }

    public boolean isCustomFilterEnabled() {
        return isCustomFilterEnabled;
    }

    public void setCustomFilterEnabled(boolean isCustomFilterEnabled) {
        this.isCustomFilterEnabled = isCustomFilterEnabled;
        setEditState(EEditState.MODIFIED);
    }

    public boolean isCustomSortingEnabled() {
        return isCustomSortingEnabled;
    }

    public void setCustomSortingEnabled(boolean isCustomSortingEnabled) {
        this.isCustomSortingEnabled = isCustomSortingEnabled;
        setEditState(EEditState.MODIFIED);
    }

    public AdsSelectorPresentationDef getOwnerSelectorPresentation() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsSelectorPresentationDef) {
                return (AdsSelectorPresentationDef) owner;
            }
        }
        return null;
    }

    public AdsEntityObjectClassDef getOwnerClass() {
        AdsSelectorPresentationDef sp = getOwnerSelectorPresentation();
        if (sp != null) {
            return sp.getOwnerClass();
        } else {
            return null;
        }
    }

    EntityObjectPresentations getOwnerPresentations() {
        AdsEntityObjectClassDef clazz = getOwnerClass();
        return clazz == null ? null : clazz.getPresentations();
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        AdsFilterDef f = findDefaultFilter();
        if (f != null) {
            list.add(f);
        }
        AdsSortingDef s = findDefaultSorting();
        if (s != null) {
            list.add(s);
        }
        EntityObjectPresentations prs = null;
        if (isAnyBaseFilterEnabled()) {
            prs = getOwnerPresentations();
            if (prs != null) {
                list.addAll(prs.getFilters().get(EScope.ALL));
            }
        } else {
            if (enabledFilters != null) {
                for (Id id : enabledFilters) {
                    f = findFilterById(id);
                    if (f != null) {
                        list.add(f);
                    }
                }
            }
        }
        if (isAnyBaseSortingEnabled()) {
            if (prs == null) {
                prs = getOwnerPresentations();
            }
            if (prs != null) {
                list.addAll(prs.getSortings().get(EScope.ALL));
            }
        } else {
            if (enabledSortings != null) {
                for (Id id : enabledSortings) {
                    s = findSortingById(id);
                    if (s != null) {
                        list.add(s);
                    }
                }
            }
        }
    }
}

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

package org.eclipse.jdt.internal.compiler.ads.syntetics;

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EPropNature;


public class PropertyGeneratorFlowInfo {

    private final AdsPropertyDef def;
    private AdsPropertyDef ovr$;
    private boolean noOvr$ = false;
    private boolean isInitializerWritten;
    private AdsPropertyDef rootProp;
    private boolean isGetter;
    private final boolean isRefine;

    public PropertyGeneratorFlowInfo(AdsPropertyDef def, boolean isGetter) {
        this.def = def;
        this.isGetter = isGetter;
        rootProp = findRootBaseProperty(def);
        isInitializerWritten = rootProp != def;
        isRefine = calcRefine(def);
    }

    public PropertyGeneratorFlowInfo(PropertyGeneratorFlowInfo src, boolean isGetter) {
        this.def = src.def;
        this.isGetter = isGetter;
        this.rootProp = src.rootProp;
        this.isInitializerWritten = src.isInitializerWritten;
        this.isRefine = src.isRefine;
    }

    public AdsPropertyDef getPropertyDef() {
        return def;
    }

    public boolean isGetter() {
        return isGetter;
    }

    protected boolean canRefine() {
        return def.getNature() == EPropNature.DYNAMIC;
    }

    private boolean equalsPropertyType(AdsPropertyDef curr, AdsPropertyDef condidate) {
        return AdsTypeDeclaration.equals(curr,curr.getValue().getType(), condidate.getValue().getType());
    }

    private boolean calcRefine(AdsPropertyDef currProp) {
        if (!canRefine()) {
            return false;
        }

        SearchResult<AdsPropertyDef> overriddenResult = currProp.getHierarchy().findOverridden();
        while (!overriddenResult.isEmpty()) {
            final AdsPropertyDef curr = overriddenResult.get();
            if (!equalsPropertyType(curr, def)) {
                return true;
            }
            overriddenResult = curr.getHierarchy().findOverridden();
        }
        return false;
    }

    protected static AdsPropertyDef findRootBaseProperty(AdsPropertyDef currProp) {
        if (currProp instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) currProp).isTemporary()) {
            return currProp;
        }
        SearchResult<AdsPropertyDef> overriddenResult = currProp.getHierarchy().findOverridden();
        while (!overriddenResult.isEmpty()) {
            final AdsPropertyDef curr = overriddenResult.get();
            if (!curr.getAccessFlags().isAbstract()) {
                return curr;
            }
            overriddenResult = curr.getHierarchy().findOverridden();
        }
        return currProp;
    }

    public boolean isWriteHidden() {
        final AdsPropertyDef over = findOverriddenProp();
        return !isInitializerWritten && (over == null || over.getAccessFlags().isAbstract());
    }

    protected AdsPropertyDef findOverriddenProp() {
        synchronized (this) {
            if (noOvr$) {
                return null;
            }
            if (ovr$ == null) {
                if ((def.getContainer() instanceof IModelPublishableProperty)) {
                    noOvr$ = true;
                } else {
                    ovr$ = def.getHierarchy().findOverridden().get();
                    if (ovr$ == null) {
                        noOvr$ = true;
                    }
                }
            }
            return ovr$;
        }
    }
    private AdsPropertyDef.Getter getterInstance;
    private boolean noGetter = false;

    protected AdsPropertyDef.Getter findGetter() {
        if (noGetter) {
            return null;
        }
        if (getterInstance == null) {
            ExtendableDefinitions.EScope scope;
            if (def.getOwnerDef() instanceof AdsPropertyDef) {//def is temporary property presentation
                scope = ExtendableDefinitions.EScope.LOCAL;
            } else {
                scope = ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE;
            }
            getterInstance = def.findGetter(scope).get();
            if (getterInstance == null) {
                noGetter = true;
            }
        }
        return getterInstance;
    }

    protected final boolean isRefine() {
        return isRefine;
    }
}

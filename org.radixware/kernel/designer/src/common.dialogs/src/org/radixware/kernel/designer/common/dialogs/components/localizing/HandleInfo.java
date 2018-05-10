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

package org.radixware.kernel.designer.common.dialogs.components.localizing;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.types.Id;


public abstract class HandleInfo {

    private boolean isDeleted = false;
    private IMultilingualStringDef adsMultilingualStringDef = null;

    /**
     * Supply Ads Definition.
     *
     */
    public abstract Definition getAdsDefinition();

    /**
     * Supply definition's title id if it is used in LocalizingPaneList as
     * initial title id value, otherwise supply null.
     *
     */
    public abstract Id getTitleId();

    public IMultilingualStringDef getAdsMultilingualStringDef() {

//        if (isDeleted) {
//            return null;
//        }
//
//        if (adsMultilingualStringDef != null) {
//            return adsMultilingualStringDef;
//        } else {
            ILocalizingBundleDef bundle = getAdsDefinition().findExistingLocalizingBundle();
            return bundle != null ? (IMultilingualStringDef) bundle.getStrings().findById(getTitleId(), EScope.LOCAL_AND_OVERWRITE).get() : null;
//        }
    }

    public void setAdsMultilingualStringDef(IMultilingualStringDef adsMultilingualStringDef) {

//        isDeleted = false;
//        this.adsMultilingualStringDef = adsMultilingualStringDef;

        addAdsMultilingualStringDef(adsMultilingualStringDef);
        onAdsMultilingualStringDefChange(adsMultilingualStringDef);
        onSetString(adsMultilingualStringDef);
    }

    protected void addAdsMultilingualStringDef(IMultilingualStringDef adsMultilingualStringDef) {
        getAdsDefinition().findLocalizingBundle().getStrings().getLocal().add((RadixObject) adsMultilingualStringDef);
        ILocalizingBundleDef.version.incrementAndGet();
    }

    public boolean createAdsMultilingualStringDef() {
        if (getAdsMultilingualStringDef() == null) {
            final ILocalizingBundleDef bundle = getAdsDefinition().findLocalizingBundle();
            if (bundle == null) {
                return false;
            }
            setAdsMultilingualStringDef(bundle.createString(getLocalizedStringKind()));
            return true;
        }
        return false;
    }

    public void removeAdsMultilingualStringDef() {

//        isDeleted = true;
//        if (adsMultilingualStringDef != null) {
//            final ILocalizingBundleDef bundle = getAdsDefinition().findExistingLocalizingBundle();
//            if (bundle != null) {
//                bundle.getStrings().getLocal().remove((RadixObject) adsMultilingualStringDef);
//            }
//            adsMultilingualStringDef = null;
//        }
        onAdsMultilingualStringDefChange(null);
        onRemoveString();
        ILocalizingBundleDef.version.incrementAndGet();
    }

    /**
     * Handle id's change event in derived classes. Example:
     * <tt>AdsMultilingualStringDef adsMultilingualStringDef =
     * getAdsMultilingualStringDef(); if (adsMultilingualStringDef != null) {
     * ((AdsTitledDefinition)
     * getAdsDefinition()).setTitleId(adsMultilingualStringDef.getId()); } else
     * { ((AdsTitledDefinition) getAdsDefinition()).setTitleId(null); }</tt>
     */
    protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
    }

    /*
     *
     * Handle language's value change event in derived classes, for example:
     * getAdsMultilingualStringDef().setValue(language, newStringValue);
     *
     */
    protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
    }

    /*
     * Handle single language's value change event in derived classes
     */
    protected void onSingleLanguagePatternChange(String newStringValue) {
    }

    /*
     *
     * Handle languages change event in derived classes,
     */
    protected void onLanguagesChange() {
    }

    /*
     * Marks whether we can add/remove current bundle (i.e. whether checkbox
     * might be editable)
     *
     */
    protected boolean isBundleResettable() {
        return true;
    }

    protected void onRemoveString() {
    }

    protected void onSetString(IMultilingualStringDef multilingualStringDef) {
    }

    public ELocalizedStringKind getLocalizedStringKind() {
        return ELocalizedStringKind.SIMPLE;
    }
}

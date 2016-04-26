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

package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import java.util.Map;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;


public class ChartEditorHandleInfo extends HandleInfo {

    private final AdsDefinition report;
    private final Map<Id, IMultilingualStringDef> mlStrings;

    ChartEditorHandleInfo(final AdsDefinition report,final Map<Id, IMultilingualStringDef> mlStrings) {
        this.mlStrings = mlStrings;
        this.report = report;
    }

    @Override
    public AdsDefinition getAdsDefinition() {
        return report;
    }

    /* @Override
     public Id getTitleId() {
     return axis!=null ? axis.getTitleId():null;
     }*/
    @Override
    public IMultilingualStringDef getAdsMultilingualStringDef() {
        IMultilingualStringDef res = super.getAdsMultilingualStringDef();
        if (res == null && mlStrings != null) {
            res = mlStrings.get(getTitleId());
        }
        return res;
    }

    @Override
    protected void addAdsMultilingualStringDef(final IMultilingualStringDef adsMultilingualStringDef) {
        if (adsMultilingualStringDef != null) {
            if (mlStrings == null) {
                super.addAdsMultilingualStringDef(adsMultilingualStringDef);                
            } else {
                mlStrings.put(adsMultilingualStringDef.getId(), adsMultilingualStringDef);
            }
        }
    }

    @Override
    public void removeAdsMultilingualStringDef() {
        final IMultilingualStringDef stringDef = this.getAdsMultilingualStringDef();
        if (stringDef != null && mlStrings != null && mlStrings.containsKey(stringDef.getId())) {
            mlStrings.remove(stringDef.getId());
        }
        super.removeAdsMultilingualStringDef();
    }

    @Override
    protected void onLanguagesPatternChange(final EIsoLanguage language,final String newStringValue) {
        getAdsMultilingualStringDef().setValue(language, newStringValue);
    }

    @Override
    public Id getTitleId() {
        return null;
    }
}

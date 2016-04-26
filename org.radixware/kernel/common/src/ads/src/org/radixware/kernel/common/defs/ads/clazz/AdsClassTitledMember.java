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

package org.radixware.kernel.common.defs.ads.clazz;

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.ITitledDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.DescribedAdsDefinition;
import org.radixware.schemas.adsdef.TitledAdsDefinition;


public abstract class AdsClassTitledMember extends AdsClassMember implements ITitledDefinition {

    protected Id titleId;

    protected AdsClassTitledMember(TitledAdsDefinition xDef) {
        super(xDef);
        this.titleId = xDef.getTitleId();
    }

    protected AdsClassTitledMember(DescribedAdsDefinition xDef) {
        super(xDef);
        this.titleId = null;
    }

    protected AdsClassTitledMember(Id id, String name, Id titleId) {
        super(id, name);
        this.titleId = titleId;
    }

    public String getTitle(EIsoLanguage language) {
        return getLocalizedStringValue(language, titleId);
    }

    public boolean setTitle(EIsoLanguage language, String title) {
        this.titleId = setLocalizedStringValue(language, titleId, title);
        return titleId != null;
    }

    @Override
    public Id getTitleId() {
        return titleId;
    }

    @Override
    public void setTitleId(Id id) {
        titleId = id;
        setEditState(EEditState.MODIFIED);
    }

    public void appendTo(TitledAdsDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (titleId != null) {
            xDef.setTitleId(titleId);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (titleId != null) {
            AdsMultilingualStringDef string = findLocalizedString(titleId);
            if (string != null) {
                list.add(string);
            }
        }
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        super.collectUsedMlStringIds(ids);
        ids.add(new MultilingualStringInfo(AdsClassTitledMember.this) {

            @Override
            public Id getId() {
                return titleId;
            }

            @Override
            public void updateId(Id newId) {
                titleId = newId;
                setEditState(EEditState.MODIFIED);
            }

            @Override
            public EAccess getAccess() {
                return AdsClassTitledMember.this.getAccessMode();
            }

            @Override
            public String getContextDescription() {
                return getTypeTitle().concat(" Title");
            }

            @Override
            public boolean isPublished() {
                return AdsClassTitledMember.this.isPublished();
            }
        });
    }
}

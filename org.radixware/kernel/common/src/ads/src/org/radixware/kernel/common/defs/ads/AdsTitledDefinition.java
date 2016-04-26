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

package org.radixware.kernel.common.defs.ads;

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.schemas.adsdef.TitledAdsDefinition;

/**
 * Abstract definition with multilingual title
 */
public abstract class AdsTitledDefinition extends AdsDefinition implements ITitledDefinition {

    private Id titleId = null;

    protected AdsTitledDefinition(Id id, String name, Id titleId) {
        super(id, name);
        this.titleId = titleId;
    }

    protected AdsTitledDefinition(TitledAdsDefinition xDef) {
        super(xDef);
        this.titleId = xDef.getTitleId();
    }

    protected AdsTitledDefinition(Id id, TitledAdsDefinition xDef) {
        super(id, xDef);
        this.titleId = xDef.getTitleId();
    }

    protected void appendTo(TitledAdsDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (saveMode != ESaveMode.USAGE) {
            if (this.titleId != null && isTitleSupported()) {
                xDef.setTitleId(this.titleId);
            }
        }
    }

    @Override
    public String getTitle(EIsoLanguage language) {
        return getLocalizedStringValue(language, titleId);
    }

    @Override
    public boolean setTitle(EIsoLanguage language, String title) {
        this.titleId = setLocalizedStringValue(language, titleId, title);
        return titleId != null;
    }

    /**
     * Returns id of multilingual string containing definition title
     */
    @Override
    public Id getTitleId() {
        if (isTitleSupported()) {
            return titleId;
        } else {
            return null;
        }
    }

    @Override
    public void setTitleId(Id id) {
        this.titleId = id;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (titleId != null && isTitleSupported()) {
            AdsMultilingualStringDef string = findLocalizedString(titleId);
            if (string != null) {
                list.add(string);
            }
        }
    }

    public boolean isTitleSupported() {
        return true;
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        super.collectUsedMlStringIds(ids);
        if (titleId != null && isTitleSupported()) {
            ids.add(new MultilingualStringInfo(AdsTitledDefinition.this) {

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
                    return AdsTitledDefinition.this.getAccessMode();
                }

                @Override
                public String getContextDescription() {
                    return getTypeTitle().concat(" Title");
                }

                @Override
                public boolean isPublished() {
                    return AdsTitledDefinition.this.isPublished();
                }
            });
        }
    }
}

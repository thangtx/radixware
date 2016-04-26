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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.Collection;

import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class AdsEventCodePropertyDef extends AdsDynamicPropertyDef implements ILocalizedDef {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AdsEventCodePropertyDef newInstance() {
            AdsEventCodePropertyDef p = new AdsEventCodePropertyDef("newEventCodeProperty");
            p.getAccessFlags().setStatic(true);
            return p;
        }

        public static AdsDynamicPropertyDef newTemporaryInstance(final AdsPropertyGroup context) {
            final AdsEventCodePropertyDef prop = newInstance();
            prop.setContainer(context);
            return prop;
        }
    }
    private Id eventId;

    private AdsEventCodePropertyDef(EDefinitionIdPrefix prefix, String name) {
        super(prefix, name);
        this.value.setType(AdsTypeDeclaration.Factory.newInstance(EValType.STR));

    }

    public AdsEventCodePropertyDef(AdsEventCodePropertyDef source, boolean forOverride) {
        super(source, forOverride);
        this.value.setType(AdsTypeDeclaration.Factory.newInstance(EValType.STR));
    }

    public AdsEventCodePropertyDef(Id id, String name) {
        super(id, name);
        this.value.setType(AdsTypeDeclaration.Factory.newInstance(EValType.STR));
    }

    public AdsEventCodePropertyDef(String name) {
        super(name);
        this.value.setType(AdsTypeDeclaration.Factory.newInstance(EValType.STR));
    }

    public AdsEventCodePropertyDef(AbstractPropertyDefinition xProp) {
        super(null,xProp);
        this.eventId = xProp.getEventStringId();
    }

    @Override
    public ServerPresentationSupport getPresentationSupport() {
        return null;
    }

    @Override
    public boolean isConst() {
        return true;
    }

    public Id getEventId() {
        return eventId;
    }

    public void setEventId(Id id) {
        this.eventId = id;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        xDef.setEventStringId(eventId);
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        super.collectUsedMlStringIds(ids);
        if (eventId != null) {
            ids.add(new MultilingualStringInfo(this) {

                @Override
                public String getContextDescription() {
                    return "Event code property event description";
                }

                @Override
                public Id getId() {
                    return eventId;
                }

                @Override
                public EAccess getAccess() {
                    return EAccess.PUBLIC;
                }

                @Override
                public void updateId(Id newId) {
                    AdsEventCodePropertyDef.this.eventId = newId;
                }

                @Override
                public boolean isPublished() {
                    return true;
                }

                @Override
                public EMultilingualStringKind getKind() {
                    return EMultilingualStringKind.EVENT_CODE;
                }
            });
        }
    }

    public AdsEventCodeDef findEventCode() {
        AdsMultilingualStringDef string = findLocalizedString(eventId);
        if (string instanceof AdsEventCodeDef) {
            return (AdsEventCodeDef) string;
        } else {
            return null;
        }
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.EVENT_CODE;
    }

    @Override
    protected void appendDetailsToolTip(StringBuilder sb) {
        AdsEventCodeDef ec = findEventCode();
        if (ec != null) {
            ec.appendAdditionalToolTip(sb);
//            for (EIsoLanguage l : ec.getLanguages()) {
//                sb.append("<br><b>").append(l.getName()).append(" message:</b><br>");
//                sb.append(ec.getValue(l).replace("<", "&lt;").replace(">", "&gt;"));
//            }
        }
    }
}

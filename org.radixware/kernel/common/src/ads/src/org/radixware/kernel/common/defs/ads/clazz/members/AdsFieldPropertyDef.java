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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.scml.CommentsAnalizer;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class AdsFieldPropertyDef extends AdsServerSidePropertyDef {

    public static final class Factory {

        private Factory() {
        }

        public static AdsFieldPropertyDef newInstance(final String name) {
            return new AdsFieldPropertyDef(name);
        }

        public static AdsFieldPropertyDef newTemporaryInstance(final RadixObject container) {
            final AdsFieldPropertyDef prop = new AdsFieldPropertyDef("newFieldProperty");
            prop.setContainer(container);
            return prop;
        }
    }

    AdsFieldPropertyDef(final AbstractPropertyDefinition xProp) {
        super(xProp);
    }

    AdsFieldPropertyDef(final String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CURSOR_FIELD_PROP), name);
    }

    AdsFieldPropertyDef(final Id id, final String name) {
        super(id, name);
    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
    }

    @Override
    protected AdsPropertyDef createOvr(final boolean forOverride) {
        return null;
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.FIELD;
    }

    /**
     * @return index of field in SQL (>0 if exist, 0 if absent or commented).
     */
    public int calcIndex() {
        int index = 1;
        final AdsClassDef ownerClass = getOwnerClass();
        if (ownerClass instanceof AdsSqlClassDef) {
            final CommentsAnalizer commentsAnalizer = CommentsAnalizer.Factory.newSqlCommentsAnalizer();
            final AdsSqlClassDef sqlClass = (AdsSqlClassDef) ownerClass;
            for (Scml.Item item : sqlClass.getSqml().getItems()) {
                if (item instanceof Scml.Text) {
                    final Scml.Text textItem = (Scml.Text) item;
                    commentsAnalizer.process(textItem.getText());
                } else if (item instanceof PropSqlNameTag) {
                    if (!commentsAnalizer.isInComment()) {
                        final PropSqlNameTag tag = (PropSqlNameTag) item;
                        if (tag.getOwnerType() == PropSqlNameTag.EOwnerType.THIS) {
                            if (Utils.equals(getId(), tag.getPropId())) {
                                return index;
                            } else {
                                index++;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    public boolean isTransfereble() {
        return false;
    }

    @Override
    public ServerPresentationSupport getPresentationSupport() {
        return null;
    }

    @Override
    public boolean isConst() {
        final AdsClassDef adsClass = getOwnerClass();
        if (adsClass != null && adsClass.getClassDefType() == EClassType.SQL_CURSOR) {
            final AdsCursorClassDef cursor = (AdsCursorClassDef) adsClass;
            return cursor.isDbReadOnly();
        }

        return super.isConst();
    }
}

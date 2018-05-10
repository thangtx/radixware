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

package org.radixware.kernel.common.builder.check.ads.ui;

import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.common.ReleaseUtils;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsUIPropertyChecker extends RadixObjectChecker<AdsUIProperty> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsUIProperty.class;
    }

    @Override
    public void check(AdsUIProperty property, IProblemHandler problemHandler) {
        super.check(property, problemHandler);
        if (property instanceof AdsUIProperty.EmbeddedWidgetOpenParams) {
            AdsUIProperty.EmbeddedWidgetOpenParams prop = (AdsUIProperty.EmbeddedWidgetOpenParams) property;
            if (prop.getExplorerItemId() != null) {
                AdsExplorerItemDef ei = prop.findExplorerItem();
                if (ei == null) {
                    error(prop, problemHandler, "Can not find referenced explorer item: #" + prop.getExplorerItemId());
                } else {
                    if (prop instanceof AdsUIProperty.EmbeddedEditorOpenParamsProperty) {
                        if (!(ei instanceof AdsParentRefExplorerItemDef)) {
                            error(prop, problemHandler, "Referenced explorer item must be parent reference");
                        }
                    } else {
                        if (!(ei instanceof AdsEntityExplorerItemDef || ei instanceof AdsChildRefExplorerItemDef)) {
                            error(prop, problemHandler, "Referenced explorer item must be entity or child reference");
                        }
                    }
                }
            }
            if (prop.getPropertyId() != null) {
                AdsPropertyDef p = prop.findProperty();
                if (p == null) {
                    error(prop, problemHandler, "Can not find referenced property: #" + prop.getPropertyId());
                } else {
                    if (!p.getValue().getType().isBasedOn(EValType.PARENT_REF) && !p.getValue().getType().isBasedOn(EValType.OBJECT)) {
                        error(prop, problemHandler, "Referenced property must be parent reference or object property");
                    }
                }
                ReleaseUtils.checkExprationRelease(property, p, problemHandler);
            }
            if (property instanceof AdsUIProperty.EmbeddedEditorOpenParamsProperty) {
                AdsUIProperty.EmbeddedEditorOpenParamsProperty prop2 = (AdsUIProperty.EmbeddedEditorOpenParamsProperty) property;
                if (prop2.getClassId() != null) {
                    if (prop2.findClass() == null) {
                        error(prop, problemHandler, "Can not find referenced class: #" + prop2.getClassId());
                    } else {
                        if (prop2.findEditorPresentation() == null) {
                            error(prop, problemHandler, "Can not find referenced editor presentation: #" + prop2.getEditorPresentationId());
                        }
                    }
                }
            }
        } else if (property instanceof AdsUIProperty.EditorPageRefProperty) {
            AdsUIProperty.EditorPageRefProperty prop = (AdsUIProperty.EditorPageRefProperty) property;
            if (prop.getEditorPageId() != null) {
                if (prop.findEditorPage() == null) {
                    error(property, problemHandler, "Can not find referenced editor page: #" + prop.getEditorPageId());
                }
            }
        } else if (property instanceof AdsUIProperty.PropertyRefProperty) {
            AdsUIProperty.PropertyRefProperty prop = (AdsUIProperty.PropertyRefProperty) property;
            if (prop.getPropertyId() != null) {
                if (prop.findProperty() == null) {
                    error(property, problemHandler, "Can not find referenced property: #" + prop.getPropertyId());
                }
            }
        } else if (property instanceof AdsUIProperty.CommandRefProperty) {
            AdsUIProperty.CommandRefProperty prop = (AdsUIProperty.CommandRefProperty) property;
            if (prop.getCommandId() != null) {
                if (prop.findCommand() == null) {
                    error(property, problemHandler, "Can not find referenced command: #" + prop.getCommandId());
                }
            }
        } else if (property instanceof AdsUIProperty.LocalizedStringRefProperty) {
            AdsUIProperty.LocalizedStringRefProperty prop = (AdsUIProperty.LocalizedStringRefProperty) property;
            if (prop.getStringId() != null) {
                if (prop.findLocalizedString() == null) {
                    error(property, problemHandler, "Can not find referenced multilingual string: #" + prop.getStringId());
                }
            }
        } else if (property instanceof AdsUIProperty.ImageProperty) {
            AdsUIProperty.ImageProperty prop = (AdsUIProperty.ImageProperty) property;
            if (prop.getImageId() != null) {
                if (prop.findImage() == null) {
                    error(property, problemHandler, "Can not find referenced image: #" + prop.getImageId());
                }
            }
        } else if (property instanceof AdsUIProperty.StringProperty && "buddy".equals(property.getName())) {
            AdsUIProperty.StringProperty prop = (AdsUIProperty.StringProperty) property;
            if (prop.value != null && !prop.value.isEmpty()) {
                Id id = Id.Factory.loadFrom(prop.value);
                AdsUIItemDef widget = AdsUIUtil.getUiDef(prop).getWidget().findWidgetById(id);
                if (widget == null) {
                    error(property, problemHandler, "Can not find referenced buddy widget: #" + id);
                }
            }
        }
    }
}

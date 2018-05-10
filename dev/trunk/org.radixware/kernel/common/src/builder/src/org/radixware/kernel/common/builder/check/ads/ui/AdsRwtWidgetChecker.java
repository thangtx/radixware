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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.builder.check.common.DefinitionChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.TypeDeclarationProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetProperties;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtUIDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsRwtWidgetChecker<T extends AdsRwtWidgetDef> extends DefinitionChecker<T> {

//    private void checkCommandToolButton(T widget, IProblemHandler problemHandler) {
//
//        if (AdsMetaInfo.COMMAND_TOOL_BUTTON_CLASS.equals(widget.getClassName())) {
//            if (widget.getProperties().getByName("command") == null) {
//                warning(widget, problemHandler, "Command not set");
//            }
//        }
//    }
//
//    private void checkCommandPushButton(T widget, IProblemHandler problemHandler) {
//
//        if (AdsMetaInfo.COMMAND_PUSH_BUTTON_CLASS.equals(widget.getClassName())) {
//            if (widget.getProperties().getByName("command") == null) {
//                warning(widget, problemHandler, "Command not set");
//            }
//        }
//    }
//
//    private void checkValEditors(T widget, IProblemHandler problemHandler) {
//
//        if (AdsMetaInfo.VAL_CONST_SET_EDITOR_CLASS.equals(widget.getClassName())) {
//            if (widget.getProperties().getByName("enumRef") == null) {
//                warning(widget, problemHandler, "Property enumRef not set");
//            }
//        }
//    }
    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsRwtWidgetDef.class;
    }

//    @Override
//    protected boolean isAutoGeneratedName() {
//        return true;
//    }
    @Override
    public void check(T widget, IProblemHandler problemHandler) {
        super.check(widget, problemHandler);
        if (AdsUIUtil.isCustomWidget(widget)) {
            Id id = Id.Factory.loadFrom(widget.getClassName());
            if (null == AdsSearcher.Factory.newAdsDefinitionSearcher(widget).findById(id)) {
                error(widget, problemHandler, "Custom widget definition not found, id = #" + id);
                return;
            }
            // check signals

            AdsRwtUIDef ui = (AdsRwtUIDef)AdsUIUtil.getUiDef(widget);
            for (AdsUIConnection c : ui.getConnections()) {
                if (c.getSenderId() != widget.getId() || c.getSignalId() == null) {
                    continue;
                }
                AdsUISignalDef sig = c.getUISignal();
//                if (sig == null) {
//                    AdsMethodDef m = c.getSlot();
//                    error(widget, problemHandler, "Unable to find custom signal <" + c.getSignalName() + ">, delete event handler \"" + (m != null ? m.getName() : "<???>") + "\"");
//                }
                if (sig == null) {
                    warning(widget, problemHandler, "Unable to find custom signal #" + c.getSignalName() + ", reference deleted");
                    c.delete();
                }
            }
            // check properties
            Set<String> names = new HashSet<String>();
            String clazz = widget.getClassName();
            while (clazz != null) {
                List<AdsUIProperty> props = AdsMetaInfo.getProps(clazz, widget);
                if (props != null) {
                    for (AdsUIProperty p : props) {
                        names.add(p.getName());
                    }
                }
                clazz = AdsMetaInfo.getExtends(clazz, widget);
            }
            for (AdsUIProperty p : widget.getProperties()) {
                if (!names.contains(p.getName())) {
                    warning(widget, problemHandler, "Unable to find property \"" + p.getName() + "\", reference deleted");
                    p.delete();
                }
            }
        }

        widgetCheck(widget, problemHandler);

//        checkCommandToolButton(widget, problemHandler);
//        checkCommandPushButton(widget, problemHandler);
//        checkValEditors(widget, problemHandler);
    }

    private void widgetCheck(T widget, IProblemHandler problemHandler) {
        switch (widget.getClassName()) {
            case AdsMetaInfo.RWT_VAL_ENUM_EDITOR:
                AdsUIProperty enumerationProp = AdsUIUtil.getUiProperty(widget, "enumeration");

                if (enumerationProp instanceof AdsUIProperty.EnumRefProperty) {
                    if (((AdsUIProperty.EnumRefProperty) enumerationProp).findEnum()!=null)
                    break;
                }
                error(widget, problemHandler, "Value of property 'Enumeration' should be set");
                break;
            case AdsMetaInfo.RWT_VAL_LIST_EDITOR:
                final AdsUIProperty itemType = AdsUIUtil.getUiProperty(widget, "itemType");

                if (itemType instanceof TypeDeclarationProperty) {

                    if (!AdsTypeDeclaration.UNDEFINED.equals(((TypeDeclarationProperty) itemType).getType())) {
                        break;
                    }
                }
                error(widget, problemHandler, "Value of property 'ItemType' should be set");
                break;
            case AdsMetaInfo.RWT_EDITOR_PAGE:
                AdsUIProperty.EditorPageRefProperty prop = (AdsUIProperty.EditorPageRefProperty) widget.getProperties().getByName(AdsWidgetProperties.EDITOR_PAGE);
                if (prop == null || prop.getEditorPageId() == null) {
                    error(widget, problemHandler, "Page model for editor page is not defined");
                }
            default:
            // ...
        }
    }
}

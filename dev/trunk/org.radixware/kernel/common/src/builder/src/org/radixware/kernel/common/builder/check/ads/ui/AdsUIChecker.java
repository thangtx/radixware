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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.ui.AdsUIDef;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;


@RadixObjectCheckerRegistration
public class AdsUIChecker extends AdsDefinitionChecker<AdsUIDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsUIDef.class;
    }

    @Override
    public void check(final AdsUIDef ui, final IProblemHandler problemHandler) {
        super.check(ui, problemHandler);

        ui.getWidget().visit(new IVisitor() {
            @Override
            public void accept(RadixObject object) {
                if (object instanceof AdsWidgetDef) {
                    AdsWidgetDef widget = (AdsWidgetDef) object;
                    if (AdsUIUtil.isCustomWidget(widget)) {
                        if (AdsMetaInfo.getCustomUI(widget) == null){
                            error(ui, problemHandler, "Custom widget #" + widget.getClassName() + " not found");
                        }
                    }
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

//        if (ui instanceof AdsCustomEditorDef) {
//            AdsCustomEditorDef editor = (AdsCustomEditorDef) ui;
//            if (editor.getClassId() == null) {
//                error(ui, problemHandler, "Custom editor is not associated with class. Possibly this custom editor view is unused");
//            } else {
//                if (editor.findClass() == null) {
//                    error(ui, problemHandler, "Can not find class using this custom editor view");
//                } else if (editor.findEditorPresentation() == null) {
//                    error(ui, problemHandler, "Can not find editor presentation using this custom editor view");
//                }
//            }
//        } else if (ui instanceof AdsCustomSelectorDef) {
//            AdsCustomSelectorDef selector = (AdsCustomSelectorDef) ui;
//            if (selector.getClassId() == null) {
//                error(ui, problemHandler, "Custom selector is not associated with class. Possibly this custom selector view is unused");
//            } else {
//                if (selector.findClass() == null) {
//                    error(ui, problemHandler, "Can not find class using this custom selector view");
//                } else if (selector.findSelectorPresentation() == null) {
//                    error(ui, problemHandler, "Can not find selector presentation using this selector");
//                }
//            }
//        } else if (ui instanceof AdsCustomPageEditorDef) {
//
//            AdsCustomPageEditorDef page = (AdsCustomPageEditorDef) ui;
//            if (page.getClassId() == null) {
//                error(ui, problemHandler, "Custom page editor is not associated with class. Possibly this custom page editor view is unused");
//            } else {
//                if (page.findClass() == null) {
//                    error(ui, problemHandler, "Can not find class using this custom page editor view");
//                } else if (page.findEditorPage() == null) {
//                    error(ui, problemHandler, "Can not find editor presentation using this custom page editor view");
//                }
//            }
//        } else if (ui instanceof AdsCustomFormDialogDef) {
//            AdsCustomFormDialogDef form = (AdsCustomFormDialogDef) ui;
//            if (form.getFormId() == null) {
//                error(ui, problemHandler, "Custom form dialog is not associated with class. Possibly this custom form dialog view is unused");
//            } else {
//                if (form.findClass() == null) {
//                    error(ui, problemHandler, "Can not find form using this custom form dialog view");
//                }
//            }
//        } else if (ui instanceof AdsCustomParagEditorDef) {
//            AdsCustomParagEditorDef pe = (AdsCustomParagEditorDef) ui;
//            if (pe.getParagraphOwnerId() == null) {
//                error(ui, problemHandler, "Custom paragraph editor is not associated with any of allowed top level defintions. Possibly this custom paragraph editor view is unused");
//            } else {
//                AdsDefinition def = pe.findParagraphOwner();
//                if (def instanceof AdsEntityObjectClassDef) {
//                    if (pe.getParagraphImmediateOwnerId() == null) {
//                        error(ui, problemHandler, "Custom paragraph editor presentation information is absent");
//                    } else {
//                        AdsDefinition io = pe.findParagraphImmediateOwner();
//                        if (io == null) {
//                            error(ui, problemHandler, MessageFormat.format("Can not find editor presentation containing edited paragraph in class {0}", def.getQualifiedName()));
//                        } else {
//                            if (pe.findParagraph() == null) {
//                                error(ui, problemHandler, MessageFormat.format("Can not find edited paragraph in editor presentation {0}", io.getQualifiedName()));
//                            }
//                        }
//                    }
//                } else if (def instanceof AdsParagraphExplorerItemDef) {
//                    if (pe.findParagraph() == null) {
//                        error(ui, problemHandler, MessageFormat.format("Can not find edited paragraph in explorer root {0}", def.getQualifiedName()));
//                    }
//                } else {
//                    error(ui, problemHandler, "Can not find class or explorer root containig editing paragraph");
//                }
//            }
//        }
    }
}

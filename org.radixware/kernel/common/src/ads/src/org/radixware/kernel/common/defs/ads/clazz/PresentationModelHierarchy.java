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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsFilterModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsDialogModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.ICustomDialog;


class PresentationModelHierarchy extends AdsDefinition.Hierarchy<AdsClassDef> {

    public PresentationModelHierarchy(AdsModelClassDef object) {
        super(object);
    }

    @Override
    public SearchResult<AdsClassDef> findOverridden() {
        return SearchResult.empty();
    }

    @Override
    public SearchResult<AdsClassDef> findOverwritten() {
        List<AdsClassDef> list = new LinkedList<>();
        collectAllOverwritten(list);
        if (list.isEmpty()) {
            return SearchResult.empty();
        } else {
            return SearchResult.<AdsClassDef>list(list);
        }
    }

    @SuppressWarnings("unchecked")
    private void collectAllOverwritten(List<AdsClassDef> collection) {
        switch (this.object.getClassDefType()) {
            case ENTITY_MODEL: {
                AdsEditorPresentationDef epr = ((AdsEntityModelClassDef) object).getOwnerEditorPresentation();
                List<AdsEditorPresentationDef> ovrs = new LinkedList<>();
                epr.getHierarchy().findOverwritten().save(ovrs);
                for (AdsEditorPresentationDef ovr : ovrs) {
                    AdsModelClassDef clazz = ovr.getModel();//findFinalModel();
                    if (clazz != null && !collection.contains(clazz)) {
                        collection.add(clazz);
                    }
                }
            }
            break;
            case GROUP_MODEL: {
                AdsSelectorPresentationDef epr = ((AdsGroupModelClassDef) object).getOwnerSelectorPresentation();
                List<AdsSelectorPresentationDef> ovrs = new LinkedList<>();
                epr.getHierarchy().findOverwritten().save(ovrs);
                for (AdsSelectorPresentationDef ovr : ovrs) {
                    AdsModelClassDef clazz = ovr.getModel();//findFinalModel();
                    if (clazz != null && !collection.contains(clazz)) {
                        collection.add(clazz);
                    }
                }
            }
            break;
            case FORM_MODEL: {
                AdsFormHandlerClassDef form = ((AdsFormModelClassDef) object).getOwnerClass();
                List<AdsClassDef> ovrs = new LinkedList<>();
                form.getHierarchy().findOverwritten().save(ovrs);
                for (AdsClassDef ovr : ovrs) {

                    AdsModelClassDef clazz = ((AdsFormHandlerClassDef) ovr).getPresentations().getModel();
                    if (clazz != null && !collection.contains(clazz)) {
                        collection.add(clazz);
                    }
                }
            }
            break;
            case REPORT_MODEL: {
                AdsReportClassDef form = ((AdsReportModelClassDef) object).getOwnerClass();
                List<AdsClassDef> ovrs = new LinkedList<>();
                form.getHierarchy().findOverwritten().save(ovrs);
                for (AdsClassDef ovr : ovrs) {

                    AdsModelClassDef clazz = ((AdsReportClassDef) ovr).getPresentations().getModel();
                    if (clazz != null && !collection.contains(clazz)) {
                        collection.add(clazz);
                    }
                }
            }
            break;
            case FILTER_MODEL: {
                AdsFilterDef form = ((AdsFilterModelClassDef) object).getOwnerFilterDef();


                List<AdsFilterDef> ovrs = new LinkedList<>();
                form.<AdsFilterDef>getHierarchy().findOverwritten().save(ovrs);
                for (AdsFilterDef ovr : ovrs) {

                    AdsModelClassDef clazz = ovr.getModel();
                    if (clazz != null && !collection.contains(clazz)) {
                        collection.add(clazz);
                    }
                }
            }
            break;
            case DIALOG_MODEL:
            case PROP_EDITOR_MODEL:
            case CUSTOM_WDGET_MODEL: {
                IModelClassOwner ui = ((AdsDialogModelClassDef) object).getOwnerDialog();
                List<AdsDefinition> ovrs = new LinkedList<>();
                ((AdsDefinition) ui).getHierarchy().findOverwritten().save(ovrs);
                for (AdsDefinition ovr : ovrs) {
                    if (ovr instanceof ICustomDialog) {
                        AdsModelClassDef clazz = ((ICustomDialog) ovr).getModelClass();
                        if (clazz != null && !collection.contains(clazz)) {
                            collection.add(clazz);
                        }
                    }
                }

            }
            break;

            case PARAGRAPH_MODEL: {
                AdsParagraphExplorerItemDef p = ((AdsParagraphModelClassDef) object).getOwnerParagraph();
                List<AdsExplorerItemDef> ovrs = new LinkedList<>();
                p.getHierarchy().findOverwritten().save(ovrs);
                for (AdsDefinition ovr : ovrs) {
                    if (ovr instanceof AdsParagraphExplorerItemDef) {
                        AdsModelClassDef clazz = ((AdsParagraphExplorerItemDef) ovr).getModel();
                        if (clazz != null && !collection.contains(clazz)) {
                            collection.add(clazz);
                        }
                    }
                }
            }
            break;
        }
    }
}

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
package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef.EditorPresentations.PresentationInfo;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.CreatePresentationsList;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.SelectorAddons;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.types.Id;

@RadixObjectCheckerRegistration
public class AdsSelectorPresentationChecker extends PresentationChecker<AdsSelectorPresentationDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsSelectorPresentationDef.class;
    }

    @Override
    public void check(AdsSelectorPresentationDef presentation, IProblemHandler problemHandler) {
        super.check(presentation, problemHandler);
        boolean isFinal = presentation.isFinal();

        if (presentation.getBasePresentationId() != null) {
            AdsSelectorPresentationDef basePresentation = presentation.findBaseSelectorPresentation().get(AdsDefinitionChecker.<AdsSelectorPresentationDef>getSearchDuplicatesChecker(presentation, problemHandler));
            if (basePresentation == null) {
                error(presentation, problemHandler, "Base selector presentation not found");
            } else {
                AdsUtils.checkAccessibility(presentation, basePresentation, false, problemHandler);
                CheckUtils.checkExportedApiDatails(presentation, basePresentation, problemHandler);

                if (basePresentation.isFinal()) {
                    error(presentation, problemHandler, "Presentation " + basePresentation.getQualifiedName() + " is final");
                }
                if (basePresentation.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && presentation.getClientEnvironment() != basePresentation.getClientEnvironment()) {
                    error(presentation, problemHandler, "Client environment of presentation (" + presentation.getClientEnvironment().getName() + ") does not match to base it's base presentation client environment (" + basePresentation.getClientEnvironment().getName() + ")");
                }
            }
        }

        if (presentation.getCreationClassCatalogId() != null) {
            AdsClassCatalogDef cc = presentation.findCreationClassCatalog().get(AdsDefinitionChecker.<AdsClassCatalogDef>getSearchDuplicatesChecker(presentation, problemHandler));
            if (cc == null) {
                error(presentation, problemHandler, "Creation class catalog not found: #" + presentation.getCreationClassCatalogId());
            } else {
                AdsUtils.checkAccessibility(presentation, cc, false, problemHandler);
                CheckUtils.checkExportedApiDatails(presentation, cc, problemHandler);
            }
        } else {  
            
        }
        List<AdsEditorPresentationDef> accChecked = new ArrayList<>();
        List<AdsEditorPresentationDef> allUsedPresentations = new LinkedList<>();
        if (!presentation.getCreatePresentationsList().isEmpty()) {
            List<CreatePresentationsList.PresentationRef> refs = presentation.getCreatePresentationsList().getPresentationRefs();
            boolean canCreate = !presentation.getRestrictions(false).isDenied(ERestriction.CREATE);

            for (CreatePresentationsList.PresentationRef ref : refs) {
                AdsEditorPresentationDef epr = ref.findEditorPresentation();
                if (epr == null) {
                    if (canCreate) {
                        error(presentation, problemHandler, "Editor presentation for creation not found: #" + ref.getPresentationId().toString());
                    }
                } else {
                    allUsedPresentations.add(epr);
                    if (!accChecked.contains(epr)) {
                        AdsUtils.checkAccessibility(presentation, epr, false, problemHandler);
                        CheckUtils.checkCreationEditorPresentation(presentation, epr, problemHandler);
                        accChecked.add(epr);
                    }
                    //CheckUtils.checkExportedApiDatails(presentation, epr, problemHandler);
                    if (epr.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && presentation.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && presentation.getClientEnvironment() != epr.getClientEnvironment()) {
                        error(presentation, problemHandler, "Client environment  (" + epr.getClientEnvironment().getName() + ") of editor presentation " + epr.getQualifiedName() + " from creation presentation list does not match to presentation client environment (" + presentation.getClientEnvironment().getName() + ")");
                    }
                }
            }
        } else {
            if (!presentation.getRestrictions(false).isDenied(ERestriction.CREATE)) {
                warning(presentation, problemHandler, "No editor presentation for creation specified");
            }
        }

        SelectorAddons addons = presentation.getAddons();

        List<Id> enabledSortingIds = addons.getEnabledSortingIds();
        if (addons.getDefaultSortingId() != null) {
            AdsSortingDef s = addons.findDefaultSorting();
            if (s == null) {
                error(presentation, problemHandler, "Default sorting not found");
            } else {
                if (!addons.isAnyBaseSortingEnabled() && !enabledSortingIds.contains(s.getId())) {
                    error(presentation, problemHandler, "Sorting " + s.getQualifiedName() + " is used as default sorting but not available");
                }
                AdsUtils.checkAccessibility(presentation, s, false, problemHandler);

                CheckUtils.checkExportedApiDatails(presentation, s, problemHandler);
            }
        }
//        if (addons.isCustomSortingEnabled()) {
//            DdsTableDef table = presentation.getOwnerClass().findTable();
//            if (table instanceof DdsViewDef) {
//                error(presentation, problemHandler, "Custom sortings are not allowed for view-based presentations");
//            }
//        }

        List<Id> enabledFilterIds = addons.getEnabledFilterIds();
        if (addons.getDefaultFilterId() != null) {
            AdsFilterDef f = addons.findDefaultFilter();
            if (f == null) {
                error(presentation, problemHandler, "Default filter not found");
            } else {
                if (!addons.isAnyBaseFilterEnabled() && !enabledFilterIds.contains(f.getId())) {
                    error(presentation, problemHandler, "Filter " + f.getQualifiedName() + " is used as default filter but not available");
                }
                AdsUtils.checkAccessibility(presentation, f, false, problemHandler);
                CheckUtils.checkExportedApiDatails(presentation, f, problemHandler);
            }
        } else {
            if (addons.isFilterObligatory()) {
                error(presentation, problemHandler, "Default filter must be specified");
            }
        }

        int counter = 1;
        for (Id id : enabledFilterIds) {
            AdsFilterDef f = addons.findFilterById(id);
            if (f == null) {
                error(presentation, problemHandler, MessageFormat.format("Cannot find filter referenced from item #{0} of enabled filter list", counter));
            } else {
//                if (f.getDefaultSortingId() != null) {                    
//                    if (!f.isAnyBaseSortingEnabled() && !f.getEnabledSortings().getEnabledSortingIds().contains(f.getDefaultSortingId())) {
//                        AdsSortingDef s = f.findDefaultSorting();
//                        String name = s == null ? "#" + f.getDefaultSortingId().toString() : s.getQualifiedName();
//                        warning(f, problemHandler, "Default sorting " + name + " of filter " + f.getQualifiedName() + " is not enabled in filter");
//                    }
//                }

                AdsUtils.checkAccessibility(presentation, f, false, problemHandler);
                if (!isFinal) {
                    CheckUtils.checkExportedApiDatails(presentation, f, problemHandler);
                }
            }
            counter++;
        }

        counter = 1;
        for (Id id : enabledSortingIds) {
            AdsSortingDef s = addons.findSortingById(id);
            if (s == null) {
                error(presentation, problemHandler, MessageFormat.format("Cannot find sorting referenced from item #{0} of enabled sorting list", counter));
            } else {
                AdsUtils.checkAccessibility(presentation, s, false, problemHandler);
                CheckUtils.checkExportedApiDatails(presentation, s, problemHandler);
            }
            counter++;
        }

        RadixObjects<AdsSelectorPresentationDef.SelectorColumn> columns = presentation.getColumns();

        if (columns.isEmpty()) {
            warning(presentation, problemHandler, "Empty list of columns in selector");
        }

        counter = 1;
        Set<AdsPropertyDef> usedProps = new HashSet<>();
        for (AdsSelectorPresentationDef.SelectorColumn c : columns) {

            AdsPropertyDef prop = c.findProperty();
            if (prop == null) {
                error(c, problemHandler, MessageFormat.format("Cannot find property referenced from selector column #{0}", counter));
            } else {
                if (usedProps.contains(prop)) {
                    error(c, problemHandler, MessageFormat.format("Duplicated property #{0} in selector columns list", prop.getQualifiedName(presentation)));
                }
                usedProps.add(prop);
                AdsUtils.checkAccessibility(presentation, prop, false, problemHandler);

                CheckUtils.checkExportedApiDatails(c, prop, problemHandler);

                AdsPropertyDef tp = prop;
                while (tp != null && tp.getNature() == EPropNature.PARENT_PROP) {
                    AdsParentPropertyDef pp = (AdsParentPropertyDef) tp;
                    tp = pp.getParentInfo().findOriginalProperty();
                }
                if (tp == null) {
                    error(c, problemHandler, MessageFormat.format("Cannot determine final property referenced from selector column #{0}", counter));
                }

                if (prop instanceof IAdsPresentableProperty) {
                    ServerPresentationSupport support = ((IAdsPresentableProperty) prop).getPresentationSupport();
                    if (support != null && support.getPresentation() != null && support.getPresentation().isPresentable()) {
                        if (!support.getPresentation().isTitleInherited() && c.getVisibility() != ESelectorColumnVisibility.NEVER) {
                            CheckUtils.titleShouldBeDefined(prop, support.getPresentation().getTitleId(), problemHandler, getHistory());
                        }
                        if (!c.isWarningSuppressed(AdsDefinitionProblems.LAZY_PROPS_IN_SELECTOR_COLUMNS)) {
                            if (support.getPresentation().getEditOptions().isReadSeparately()) {
                                problemHandler.accept(RadixProblem.Factory.newWarning(presentation, AdsDefinitionProblems.LAZY_PROPS_IN_SELECTOR_COLUMNS, c.getQualifiedName(), prop.getQualifiedName()));
                            }
                        }
                    } else {
                        error(c, problemHandler, "Property without presentation attributes in selector column list: " + prop.getQualifiedName());
                    }
                } else {
                    error(c, problemHandler, "Non-presentation property in selector column list: " + prop.getQualifiedName());
                }
            }
            counter++;
        }

        List<PresentationInfo> eprInfos = presentation.getEditorPresentations().getPresentationInfos();
        if (eprInfos.isEmpty()) {
            error(presentation.getEditorPresentations(), problemHandler, "No editor presentations selected");
        } else {
            int webEprCount = 0;
            int commonEprCount = 0;
            int explorerEprCount = 0;

            for (PresentationInfo info : eprInfos) {
                AdsEditorPresentationDef epr = info.findPresentation();
                if (epr == null) {
                    error(presentation.getEditorPresentations(), problemHandler, "Editor presentation not found: #" + info.getId());
                } else {
                    if (!allUsedPresentations.contains(epr)) {
                        allUsedPresentations.add(epr);
                    }
                    if (!accChecked.contains(epr)) {
                        AdsUtils.checkAccessibility(presentation, epr, false, problemHandler);
                    }
                    if (epr.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && presentation.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && presentation.getClientEnvironment() != epr.getClientEnvironment()) {
                        error(presentation, problemHandler, "Client environment  (" + epr.getClientEnvironment().getName() + ") of editor presentation " + epr.getQualifiedName() + " from editor presentation list does not match to presentation client environment (" + presentation.getClientEnvironment().getName() + ")");
                    }
                    AdsEntityModelClassDef model = (AdsEntityModelClassDef) epr.findFinalModel();
                    if (model != null) {
                        switch (model.getClientEnvironment()) {
                            case COMMON_CLIENT:
                                commonEprCount++;
                                break;
                            case EXPLORER:
                                explorerEprCount++;
                                break;
                            case WEB:
                                webEprCount++;
                                break;
                        }
                    } else {
                        if (presentation.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                            commonEprCount++;
                        }
                    }
                    // CheckUtils.checkExportedApiDatails(presentation, epr, problemHandler);
                }
            }
            switch (presentation.getClientEnvironment()) {
                case COMMON_CLIENT:
                    if (explorerEprCount > 0) {
                        if (webEprCount == 0 && commonEprCount == 0) {
                            error(presentation, problemHandler, "No editor presentation with Web or Client-Common model allowed for Client-Common selector presentation");
                        }
                    }
                    if (webEprCount > 0) {
                        if (explorerEprCount == 0 && commonEprCount == 0) {
                            error(presentation, problemHandler, "No editor presentation with Explorer or Client-Common model allowed for Client-Common selector presentation");
                        }
                    }
                    break;
            }
        }

        if (presentation.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
            boolean isWebCustomViewUsed = presentation.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB);
            boolean isExplorerCustomViewUsed = presentation.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER);

            if (isWebCustomViewUsed || isExplorerCustomViewUsed) {
                if (!isWebCustomViewUsed && !presentation.isWarningSuppressed(AdsDefinitionProblems.MISSING_WEB_CUSTOM_VIEW)) {
                    warning(presentation, problemHandler, AdsDefinitionProblems.MISSING_WEB_CUSTOM_VIEW);
                }
                if (!isExplorerCustomViewUsed && !presentation.isWarningSuppressed(AdsDefinitionProblems.MISSING_EXPLORER_CUSTOM_VIEW)) {
                    warning(presentation, problemHandler, AdsDefinitionProblems.MISSING_EXPLORER_CUSTOM_VIEW);
                }
            }
        }

        selectorColumnsChecker(presentation, problemHandler);
    }

    private void selectorColumnsChecker(AdsSelectorPresentationDef presentation, IProblemHandler problemHandler) {

        for (final AdsSelectorPresentationDef.SelectorColumn selectorColumn : presentation.getColumns()) {
            final AdsPropertyDef property = selectorColumn.findProperty();

            if (property != null) {
                switch (property.getNature()) {
                    case EVENT_CODE:
                        error(presentation, problemHandler, "Selector can't contain EVENT_CODE property");
                }
            }
        }
    }
}

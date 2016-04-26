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

import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPropertyPresentationAttributes;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsEditorPresentationChecker extends PresentationChecker<AdsEditorPresentationDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsEditorPresentationDef.class;
    }

    @Override
    public void check(final AdsEditorPresentationDef presentation, final IProblemHandler problemHandler) {
        super.check(presentation, problemHandler);

        AdsEntityObjectClassDef ownerClass = presentation.getOwnerClass();
        if (ownerClass.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && ownerClass.getClientEnvironment() != presentation.getClientEnvironment()) {
            error(presentation, problemHandler, "Client environment of presentation (" + presentation.getClientEnvironment().getName() + " ) does not match to it's owner class client environment (" + ownerClass.getClientEnvironment().getName() + ")");
        }
        AdsEditorPresentationDef accessibilityChecked = null;
        if (presentation.getBasePresentationId() != null) {
            //SearchResult.GetAdvisor<AdsEditorPresentationDef > advisor =
            AdsEditorPresentationDef epr = presentation.findBaseEditorPresentation().get(AdsDefinitionChecker.<AdsEditorPresentationDef>getSearchDuplicatesChecker(presentation, problemHandler));
            if (epr == null) {
                error(presentation, problemHandler, "Base editor presentation not found");
            } else {
                AdsUtils.checkAccessibility(presentation, epr, false, problemHandler);
                CheckUtils.checkExportedApiDatails(presentation, epr, problemHandler);
                accessibilityChecked = epr;
                if (epr.isFinal()) {
                    error(presentation, problemHandler, "Presentation " + epr.getQualifiedName() + " is final");
                }
                if (epr.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && presentation.getClientEnvironment() != epr.getClientEnvironment()) {
                    error(presentation, problemHandler, "Client environment of presentation (" + presentation.getClientEnvironment().getName() + ") does not match to base it's base presentation client environment (" + epr.getClientEnvironment().getName() + ")");
                }
            }
        }
        if (presentation.getReplacedEditorPresentationId() != null) {
            AdsEditorPresentationDef epr = presentation.findReplacedEditorPresentation().get(AdsDefinitionChecker.<AdsEditorPresentationDef>getSearchDuplicatesChecker(presentation, problemHandler));
            if (epr == null) {
                error(presentation, problemHandler, "Replaced editor presentation not found");
            } else {
                ERuntimeEnvironmentType repEnv = epr.getClientEnvironment();

                if (repEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                    if (presentation.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && presentation.getClientEnvironment() != repEnv) {
                        error(presentation, problemHandler, "Client environment of replaced editor presentation " + epr.getQualifiedName() + " (" + repEnv.getName() + ") is incompatible with presentation client environment (" + presentation.getClientEnvironment().getName() + ")");
                    }


                }

                if (accessibilityChecked != epr) {
                    AdsUtils.checkAccessibility(presentation, epr, false, problemHandler);
                }

                if (presentation.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT) {
                    AdsEditorPresentationDef replacedPathItem = epr;
                    while (replacedPathItem != null) {
                        repEnv = replacedPathItem.getClientEnvironment();
                        if (repEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                            if (replacedPathItem == epr) {
                                error(presentation, problemHandler, "Replacing Client-Common presentation with " + presentation.getClientEnvironment().getName() + " presentation may cause problems on opening selector");
                            } else {
                                error(presentation, problemHandler, "Indirectly replacing Client-Common presentation with " + presentation.getClientEnvironment().getName() + " presentation may cause problems on opening selector");
                            }
                            break;
                        }
                        replacedPathItem = replacedPathItem.findReplacedEditorPresentation().get();
                    }
                }
                //CheckUtils.checkExportedApiDatails(presentation, epr, problemHandler);
            }
        }

        if (presentation.isRightsInheritanceModeInherited()) {
            AdsEditorPresentationDef rightsInheritanceModeDefiner = presentation.findRightsInheritanceDefinePresentation().get();
            if (rightsInheritanceModeDefiner == null || rightsInheritanceModeDefiner == presentation) {
                error(presentation, problemHandler, "Rights inheritance mode is inherited, but base of inhertiance can not be found");
            }
        } else {
            switch (presentation.getRightInheritanceMode()) {
                case FROM_DEFINED:
                    if (presentation.getRightsSourceEditorPresentationId() == null) {
                        error(presentation, problemHandler, "Editor presentation for rights inheritance must be specified");
                    } else {
                        AdsEditorPresentationDef rs = presentation.findRightSourceEditorPresentation();
                        if (rs == null) {
                            error(presentation, problemHandler, "Can not find editor presentation for rights inheritance: #" + presentation.getRightsSourceEditorPresentationId());
                        }
                    }
                    break;
                case FROM_REPLACED:
                    if (presentation.getReplacedEditorPresentationId() == null) {
                        error(presentation, problemHandler, "Rights are inherited from replaced presentation but no replaced editor presentation specified");
                    }
                    break;
            }
        }
//        final ForbiddenProps fps = fp == null ? presentation.getForbiddenProperties() : fp;

        checkPropertyPresentationAttributes(presentation, problemHandler);

        EditorPages editorPages = presentation.getEditorPages();
        final boolean isEditorPagesInherited = presentation.isEditorPagesInherited();
        final ERuntimeEnvironmentType eprClientEnv = presentation.getClientEnvironment();

        if (editorPages != null) {
            editorPages.getOrder().visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {

                    EditorPages.OrderedPage page = (EditorPages.OrderedPage) radixObject;
                    AdsEditorPageDef editorPage = page.findPage();
                    if (editorPage != null) {
                        ERuntimeEnvironmentType effectiveContainerEnv = editorPage.getClientEnvironment();
                        if (eprClientEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                            if (eprClientEnv != effectiveContainerEnv && effectiveContainerEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                                error(page, problemHandler, "Client environment of editor page " + editorPage.getQualifiedName() + " (" + editorPage.getClientEnvironment().getName() + ") is incompatible with client environment of context editor presentation " + presentation.getQualifiedName() + " (" + eprClientEnv.getName() + ")");
                            }
                            effectiveContainerEnv = eprClientEnv;
                        }
                        if (!isEditorPagesInherited) {
                            AdsUtils.checkAccessibility(presentation, editorPage, false, problemHandler);
                        }
                        //CheckUtils.checkExportedApiDatails(presentation, editorPage, problemHandler);

                        final PropertyUsageSupport support = editorPage.getUsedProperties();
                        for (PropertyUsageSupport.PropertyRef ref : support.get()) {

                            final Id id = ref.getPropertyId();

//                            PropertyUsage usage = fps.findIfForbidden(id);
                            AdsDefinition prop = null;
//                            if (usage != null) {
//                                prop = usage.findProperty();
//                                if (prop != null) {
//                                    error(presentation, problemHandler, "Property " + prop.getQualifiedName() + " from editor page " + editorPage.getQualifiedName() + " is forbidden in context of this presentation");
//                                } else {
//                                    error(presentation, problemHandler, "Property #" + usage.getPropertyId() + " from editor page " + editorPage.getQualifiedName() + " is forbidden in context of this presentation");
//                                }
//                            }
                            if (prop == null) {
                                prop = ref.findProperty();

                            }
                            if (prop instanceof IAdsPresentableProperty) {
                                final ServerPresentationSupport presentationSupport = ((IAdsPresentableProperty) prop).getPresentationSupport();
                                if (presentationSupport != null) {
                                    EditOptionsChecker.checkPresentationOptionsUsedInClientPart(AdsEditorPresentationChecker.this, editorPage, effectiveContainerEnv, prop, presentationSupport, problemHandler);
                                }
                            }
                        }
                    } else {
                        //another chaer make it clean
                    }
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof EditorPages.OrderedPage;
                }
            });

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
        }
    }

    private void checkPropertyPresentationAttributes(final AdsEditorPresentationDef presentation, final IProblemHandler problemHandler) {
        final SearchResult<AdsEditorPresentationDef> overwritten = presentation.getHierarchy().findOverwritten();
        if (overwritten.isMultiple()) {
            if (presentation.isPropertyPresentationAttributesInherited()) {
                error(presentation, problemHandler, "Diamond overwritten detected, property presentation attributes must be overwritten");
            }
        }

        final AdsEditorPresentationDef.PropertyPresentationAttributesCollection attributesCollection = presentation.getPropertyPresentationAttributesCollection();
        final Set<Id> used = new HashSet<>();
        for (final AdsPropertyPresentationAttributes restriction : attributesCollection) {
            final Id propertyId = restriction.getPropertyId();

            if (propertyId != null && used.contains(propertyId)) {
                final AdsPropertyDef property = restriction.findProperty();
                error(presentation, problemHandler,
                        "Duplication presentation attributes for property " + (property != null ? property.getQualifiedName() : ("#" + propertyId)));
            }

            if (restriction.getTitleId() != null) {
                if (restriction.findTitle() == null) {
                    error(presentation, problemHandler, "Title #" + restriction.getTitleId() + " referenced from property presentation attributes not found");
                }
            }

            if (propertyId == null) {
                error(presentation, problemHandler, "Wrong property presentation attributes: property id not specified");
            } else {

                used.add(propertyId);

                final AdsPropertyDef property = restriction.findProperty();
                if (property == null) {
                    error(presentation, problemHandler, "Property #" + restriction.getPropertyId() + " referenced from property presentation attributes not found");
                } else {
                    AdsUtils.checkAccessibility(presentation, property, false, problemHandler);
                    CheckUtils.checkExportedApiDatails(presentation, property, problemHandler);
                    checkPresentableProperty(presentation, property, problemHandler);
                }
            }
        }
    }

    private void checkPresentableProperty(final AdsEditorPresentationDef presentation, AdsPropertyDef property, final IProblemHandler problemHandler) {
        if (property instanceof IAdsPresentableProperty) {
            final IAdsPresentableProperty presentableProperty = (IAdsPresentableProperty) property;

            if (presentableProperty.getPresentationSupport() != null
                    && presentableProperty.getPresentationSupport().getPresentation().isPresentable()) {
                return;
            }
        }
        error(presentation, problemHandler, "Property #" + property.getQualifiedName() + " referenced from property presentation attributes is not presentable");
    }
}

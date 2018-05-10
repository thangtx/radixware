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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityGroupPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ReportPresentations;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

@RadixObjectCheckerRegistration
public class ClassPresentationsChecker<T extends ClassPresentations> extends RadixObjectChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return ClassPresentations.class;
    }

    @Override
    public void check(T prs, IProblemHandler problemHandler) {
        super.check(prs, problemHandler);
        if (prs instanceof EntityPresentations) {
            checkEntityClassPresentations((EntityPresentations) prs, problemHandler);
        } else if (prs instanceof EntityObjectPresentations) {
            checkEntityObjectClassPresentations((EntityObjectPresentations) prs, problemHandler);
        } else if (prs instanceof EntityGroupPresentations) {
            checkEntityGroupClassPresentations((EntityGroupPresentations) prs, problemHandler);
        } else if (prs instanceof ReportPresentations) {
            checkReportClassPresentations((ReportPresentations) prs, problemHandler);
        } else if (prs instanceof AbstractFormPresentations) {
            checkFormHandlerClassPresentations((AbstractFormPresentations) prs, problemHandler);
        }
    }

    private void checkEntityObjectClassPresentations(EntityObjectPresentations prs, IProblemHandler problemHandler) {
        Map<Id, AdsEditorPresentationDef> replaceMap = new HashMap<Id, AdsEditorPresentationDef>();
        for (AdsEditorPresentationDef p : prs.getEditorPresentations().get(EScope.LOCAL_AND_OVERWRITE)) {
            if (p.getReplacedEditorPresentationId() != null) {
                AdsEditorPresentationDef def = replaceMap.get(p.getReplacedEditorPresentationId());
                if (def != null) {
                    AdsEditorPresentationDef replaced = prs.getEditorPresentations().findById(p.getReplacedEditorPresentationId(), EScope.ALL).get();
                    String presName = replaced == null ? "#" + p.getReplacedEditorPresentationId() : p.getQualifiedName();
                    error(prs, problemHandler, "Editor presentation " + presName + " can not be replaced more than one time in one class, but two replacers found: " + p.getQualifiedName() + " and " + def.getQualifiedName());
                } else {
                    replaceMap.put(p.getReplacedEditorPresentationId(), p);
                }
            }
        }
        if (prs.getObjectTitleId() != null) {
            CheckUtils.checkMLStringId(prs.findOwnerTitleDefinition(), prs.getObjectTitleId(), problemHandler, "singular title");
        }

    }

    private void checkEntityClassPresentations(EntityPresentations prs, IProblemHandler problemHandler) {
        checkEntityObjectClassPresentations(prs, problemHandler);
        if (prs.getDefaultSelectorPresentationId() != null) {
            AdsSelectorPresentationDef spr = prs.findDefaultSelectorPresentation();
            if (spr == null) {
                error(prs, problemHandler, "Default selector presentation not found #" + prs.getDefaultSelectorPresentationId().toString());
            } else {
                AdsUtils.checkAccessibility(prs, spr, false, problemHandler);
                CheckUtils.checkExportedApiDatails(prs, spr, problemHandler);
            }
        }
        if (prs.getEntityTitleId() != null) {
            CheckUtils.checkMLStringId(prs.getOwnerClass(), prs.getEntityTitleId(), problemHandler, "plural title");
        }

        if (prs.getIconId() != null) {
            CheckUtils.checkIconId(prs.getOwnerClass(), prs.getIconId(), problemHandler, "entity icon");
        }
    }

    private void checkEntityGroupClassPresentations(EntityGroupPresentations prs, IProblemHandler problemHandler) {
    }

    private void checkFormHandlerClassPresentations(AbstractFormPresentations prs, IProblemHandler problemHandler) {
        if (!prs.getOwnerClass().getAccessFlags().isAbstract()) {

            if (prs.getOwnerClass().getClassDefType() == EClassType.FORM_HANDLER) {
                if (!prs.getInheritanceMask().contains(EPresentationAttrInheritance.TITLE)) {
                    AdsFormHandlerClassDef form = (AdsFormHandlerClassDef) prs.getOwnerClass();
                    boolean hasTitle = form.getTitleId() != null;
                    if (!hasTitle) {
                        while (form != null) {
                            final AdsType type = form.getInheritance().getSuperClassRef().resolve(prs.getOwnerClass()).get();
                            if (type instanceof AdsClassType && ((AdsClassType) type).getSource() instanceof AdsFormHandlerClassDef) {
                                form = (AdsFormHandlerClassDef) ((AdsClassType) type).getSource();
                                hasTitle = form.getTitleId() != null;
                                if (hasTitle) {
                                    break;
                                }
                            }else{
                                break;
                            }
                        }
                        if (!hasTitle) {
                            CheckUtils.titleShouldBeDefined(prs.getOwnerClass(), prs.getOwnerClass().getTitleId(), problemHandler, getHistory());
                        }
                    }
                }
            } else {
                CheckUtils.titleShouldBeDefined(prs.getOwnerClass(), prs.getOwnerClass().getTitleId(), problemHandler, getHistory());
            }

        }
        checkAbstractFormPresentations(prs, problemHandler);
    }

    private void checkReportClassPresentations(ReportPresentations prs, IProblemHandler problemHandler) {
        if (!((AdsReportClassDef) prs.getOwnerClass()).isSubreport()) {
            CheckUtils.titleShouldBeDefined(prs.getOwnerClass(), prs.getOwnerClass().getTitleId(), problemHandler, getHistory());
        }
        checkAbstractFormPresentations(prs, problemHandler);
    }

    private void checkAbstractFormPresentations(AbstractFormPresentations prs, IProblemHandler problemHandler) {
        List<Id> commandsOrder = prs.getCommandsOrder();
        CheckUtils.checkIconId(prs.getOwnerClass(), prs.getIconId(), problemHandler, "icon");
        for (Id id : commandsOrder) {
            AdsCommandDef command = prs.getCommands().findById(id, EScope.ALL).get();
            if (command == null) {
                problemHandler.accept(RadixProblem.Factory.newError(prs, "Unknown command in command order list: #" + id));
            } else {
                AdsUtils.checkAccessibility(prs, command, false, problemHandler);
                CheckUtils.checkExportedApiDatails(prs, command, problemHandler);
            }
        }
        if (prs.getOwnerClass().getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
            boolean isWebCustomViewUsed = prs.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB);
            boolean isExplorerCustomViewUsed = prs.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER);

            if (isWebCustomViewUsed || isExplorerCustomViewUsed) {
                if (!isWebCustomViewUsed && !prs.getOwnerClass().isWarningSuppressed(AdsDefinitionProblems.MISSING_WEB_CUSTOM_VIEW)) {
                    warning(prs.getOwnerClass(), problemHandler, AdsDefinitionProblems.MISSING_WEB_CUSTOM_VIEW);
                }
                if (!isExplorerCustomViewUsed && !prs.getOwnerClass().isWarningSuppressed(AdsDefinitionProblems.MISSING_EXPLORER_CUSTOM_VIEW)) {
                    warning(prs.getOwnerClass(), problemHandler, AdsDefinitionProblems.MISSING_EXPLORER_CUSTOM_VIEW);
                }
            }
        }
    }
}

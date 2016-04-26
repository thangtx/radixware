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

package org.radixware.kernel.common.builder.check.ads;

import java.text.MessageFormat;

import org.radixware.kernel.common.builder.check.common.DefinitionChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.IClientDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.Utils;


public abstract class AdsDefinitionChecker<T extends AdsDefinition> extends DefinitionChecker<T> {

    public AdsDefinitionChecker() {
    }

    public static <T> SearchResult.GetAdvisor<T> getSearchDuplicatesChecker(final RadixObject context, final IProblemHandler problemHandler) {
        return new SearchResult.GetAdvisor<T>() {

            private String getName(T getResult) {
                String name;
                if (getResult instanceof RadixObject) {
                    name = ((RadixObject) getResult).getQualifiedName();
                } else if (getResult instanceof AdsType) {
                    name = ((AdsType) getResult).getQualifiedName(context);
                } else {
                    name = getResult.toString();
                }
                return name;
            }

            @Override
            public T advise(SearchResult<T> result, T getResult) {
                if (result.isMultiple()) {
                    StringBuilder builder = new StringBuilder();

                    builder.append("Reference to ").append(getName(getResult)).append(" is ambigous. Candidates are:  ");
                    boolean first = true;
                    for (T t : result.all()) {
                        if (first) {
                            first = false;
                        } else {
                            builder.append(", ");
                        }
                        builder.append(getName(t));
                    }
                    problemHandler.accept(RadixProblem.Factory.newError(context, builder.toString()));
                }
                return getResult;
            }
        };
    }

    @Override
    public void check(T definition, IProblemHandler problemHandler) {
        super.check(definition, problemHandler);

        if (definition.isIdInherited()) {
            AdsEnumItemDef src = definition.findIdSourceItem();
            if (src == null) {
                AdsEnumDef e = definition.findIdSourceEnum();
                if (e == null) {
                    error(definition, problemHandler, "Id source enumeration can not be found: #" + definition.getIdSourceEnumId());
                } else {
                    error(definition, problemHandler, "Id source enumeration item #" + definition.getIdSourceItemId() + " can not be found in enumeration " + e.getQualifiedName());
                }
            } else {
                AdsEnumDef e = src.getOwnerEnum();
                if (!e.isIdEnum()) {
                    error(definition, problemHandler, "Enumeration " + e.getQualifiedName() + " can not be used as Id source");
                }
                String idAsStr = definition.getId().toString();
                Object enumValue = src.getValue().toObject(EValType.STR);
                if (enumValue instanceof String) {
                    if (!Utils.equals(idAsStr, enumValue)) {
                        error(definition, problemHandler, "Definition Id, inherited from " + src.getQualifiedName() + " does not match to value of " + src.getQualifiedName());
                    }
                }
            }
        }
        if (definition instanceof IProfileable) {
            IProfileable prof = (IProfileable) definition;
            AdsProfileSupport support = prof.getProfileSupport();
            if (support != null && support.isProfiled()) {
                AdsEnumDef ts = support.findTimingSections();
                if (ts == null) {
                    error(definition, problemHandler, "Timing sections definitions are not available (Enumeration, based on ETimingSection can not be found).");
                } else {
                    String enumValue = support.getTimingSectionId();
                    AdsEnumItemDef item = ts.getItems().findByValue(enumValue, EScope.ALL);
                    if (item == null) {
                        error(definition, problemHandler, "Can not find timing section for id=\"" + enumValue.toString() + "\" (No item with such value in enumeration " + ts.getQualifiedName());
                    }
                }
            }
        }
        if (definition.canChangeAccessMode()) {
            if (definition.getAccessMode().isLess(definition.getMinimumAccess())) {
                error(definition, problemHandler, "Definition accessibility is less than minimum allowed. Should be at least " + definition.getMinimumAccess().getName());
            }
        }
        CheckUtils.checkDomains(definition, problemHandler);

        if (definition instanceof IOverwritable) {
            AdsDefinition.Hierarchy<T> h = definition.<T>getHierarchy();
            boolean checkForOvr = true;
            if (h.isDefaultHierarchy()) {
                if (!definition.isTopLevelDefinition()) {
                    checkForOvr = false;
                }
            }
            if (checkForOvr) {
            //    SearchResult.GetAdvisor<T> dupDetector = getSearchDuplicatesChecker(definition, problemHandler);
                T ovr = h.findOverwritten().get(/*dupDetector*/);
                if (ovr != null) {
                    
                    if (!definition.isWarningSuppressed(AdsDefinitionProblems.DO_NOT_MATCH_THE_NAME_OF_OVERWRITTEN)) {
                        if (!Utils.equals(definition.getName(), ovr.getName())) {
                            if (definition instanceof AdsRoleDef){
                                warning(definition, problemHandler, AdsDefinitionProblems.DO_NOT_MATCH_THE_NAME_OF_OVERWRITTEN, ovr.getQualifiedName());
                            }
                            else{
                                warning(definition, problemHandler, MessageFormat.format("Name does not match the name of overwritten {0}", ovr.getQualifiedName()));
                            }
                        }
                    }
                    Layer definitionLayer = definition.getModule().getSegment().getLayer();

                    /*if (!definitionLayer.isReadOnly() && !definitionLayer.isTrunkLayer()) {
                        error(definition, problemHandler, "Overwrite is not allowed for definitions from layer " + definitionLayer.getName());
                    }*/
                    AdsUtils.checkAccessibility(definition, ovr, false, problemHandler);
                    boolean invalidOvr = false;
//                    if (ovr.isFinal()) {
//                        problemHandler.accept(RadixProblem.Factory.newError(AdsDefinitionProblems.OVERWRITE_FINAL_DEFINITION, definition, ovr.getQualifiedName()));
//                        invalidOvr = true;
//                    }
                    if (ovr instanceof IOverwritable) {
                        if (!((IOverwritable) ovr).allowOverwrite()) {
                            problemHandler.accept(RadixProblem.Factory.newError(AdsDefinitionProblems.OVERWRITE_NOT_ALLOWED, definition, ovr.getQualifiedName()));
                            invalidOvr = true;
                        }
                    }
                    if (!invalidOvr) {
                        if (!((IOverwritable) definition).isOverwrite()) {
                            problemHandler.accept(RadixProblem.Factory.newWarning(AdsDefinitionProblems.MISSING_OVERWRITE_FLAG, definition, ovr.getQualifiedName()));
                        }
                    }
                    if (definition instanceof IClientDefinition && ovr instanceof IClientDefinition) {
                        ERuntimeEnvironmentType src = ((IClientDefinition) ovr).getClientEnvironment();
                        ERuntimeEnvironmentType cur = ((IClientDefinition) definition).getClientEnvironment();
                        if (src != cur) {
                            error(definition, problemHandler, "Wrong client environment. Must be " + src.getName() + ", same with client environment of overwritten " + ovr.getQualifiedName());
                        }
                    }
                } else {
                    if (((IOverwritable) definition).isOverwrite()) {
                        problemHandler.accept(RadixProblem.Factory.newError(AdsDefinitionProblems.INVALID_OVERWRITE_FLAG, definition));
                    }
                }
            }
        }
//        checkDescription(definition, problemHandler);
    }

    private void checkDescription(T definition, IProblemHandler problemHandler) {
        if (definition.getDescriptionId() == null) {
            final String oldDescription = definition.getDescription();

            if (oldDescription != null && !oldDescription.isEmpty()) {
                warning(definition, problemHandler, "Description of definition may be localized");
            }
        }
    }
}

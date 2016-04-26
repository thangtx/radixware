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

import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


public abstract class PresentationChecker<T extends AdsPresentationDef> extends AdsDefinitionChecker<T> {

    protected static final String exportErrorFixHint = "Presentation should be \"final\"";

    @Override
    public void check(final T presentation, final IProblemHandler problemHandler) {
        super.check(presentation, problemHandler);

        for (final EPresentationAttrInheritance e : EPresentationAttrInheritance.values()) {
            if (presentation.getInheritanceMask().contains(e)) {
                SearchResult<AdsPresentationDef> result = (SearchResult<AdsPresentationDef>) presentation.findAttributeOwner(e);
                result.get(new SearchResult.GetAdvisor<AdsPresentationDef>() {

                    @Override
                    public AdsPresentationDef advise(SearchResult<AdsPresentationDef> result, AdsPresentationDef getResult) {
                        if (result.isMultiple()) {
                            StringBuilder builder = new StringBuilder();

                            builder.append("Inherited presentation attribute ").append(e.getTitle()).append(" is ambigous. Candidates are found in:  ");
                            boolean first = true;
                            for (AdsPresentationDef t : result.all()) {
                                if (first) {
                                    first = false;
                                } else {
                                    builder.append(", ");
                                }
                                builder.append(t.getQualifiedName());
                            }
                            problemHandler.accept(RadixProblem.Factory.newError(presentation, builder.toString()));
                        }
                        return getResult;
                    }
                });
            }
        }


        if (!presentation.isIconInherited()) {
            CheckUtils.checkIconId(presentation, presentation.getIconId(), problemHandler, "icon");
        }
        if (!presentation.isTitleInherited()) {
            CheckUtils.checkMLStringId(presentation, presentation.getTitleId(), problemHandler, "title");
        }
        DefinitionSearcher<AdsContextlessCommandDef> clcSearcer = AdsSearcher.Factory.newAdsContextlessCommandSearcher(presentation.getModule());
        ExtendableDefinitions<AdsScopeCommandDef> commandSet = presentation.getCommandsLookup();

        if (commandSet != null) {
            if (!presentation.isRestrictionsInherited()) {
                for (Id id : presentation.getRestrictions().getEnabledCommandIds()) {
                    AdsCommandDef cmd = null;
                    if (id.getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
                        cmd = clcSearcer.findById(id).get();
                    } else {
                        cmd = commandSet.findById(id, EScope.ALL).get();
                    }
                    if (cmd == null) {
                        error(presentation.getRestrictions(), problemHandler, "Unknown command #" + id + " in allowed commands list");
                    } else {
                        AdsUtils.checkAccessibility(presentation, cmd, false, problemHandler);
                        CheckUtils.checkExportedApiDatails(presentation, cmd, problemHandler);
                    }
                }
            }
        }

        ERuntimeEnvironmentType env = presentation.getClientEnvironment();
        if (env != ERuntimeEnvironmentType.COMMON_CLIENT) {
            ICustomViewable cv = (ICustomViewable) presentation;
            if (env == ERuntimeEnvironmentType.WEB && cv.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                error(presentation, problemHandler, "Web presentation must not define custom view for Explorer");
            }
            if (env == ERuntimeEnvironmentType.EXPLORER && cv.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                error(presentation, problemHandler, "Explorer presentation must not define custom view for Web");
            }
        }
        if (!presentation.isUseDefaultModel()) {
            if (!presentation.isOwnModelAllowed()) {
                error(presentation, problemHandler, "Own model is not allowed for this presentation. Default model must be used");
            }
        }
    }
}
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

package org.radixware.kernel.common.builder.check.ads.exploreritem;

import java.util.List;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsParentRefExplorerItemChecker extends AdsExplorerItemChecker<AdsParentRefExplorerItemDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsParentRefExplorerItemDef.class;
    }

    @Override
    public void check(AdsParentRefExplorerItemDef explorerItem, IProblemHandler problemHandler) {
        super.check(explorerItem, problemHandler);

        if (explorerItem.findParentReference() == null) {
            error(explorerItem, problemHandler, "Can not find parent reference: #" + explorerItem.getParentReferenceId());
        }

        if (!explorerItem.isReadOnly()) {//See RADIX-1833
            AdsEntityObjectClassDef referencedClass = explorerItem.findReferencedEntityClass();

            final ERuntimeEnvironmentType env = explorerItem.getClientEnvironment();

            if (referencedClass == null) {
                error(explorerItem, problemHandler, "Can not find referenced class: #" + explorerItem.getClassId());
            } else {
                if (referencedClass.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && referencedClass.getClientEnvironment() != env) {
                    error(explorerItem, problemHandler, "Class " + referencedClass.getQualifiedName() + " is not available for client environment " + env.getName());
                }

                AdsUtils.checkAccessibility(explorerItem, referencedClass, false, problemHandler);
                CheckUtils.checkExportedApiDatails(explorerItem, referencedClass, problemHandler);
                List<Id> eprIds = explorerItem.getEditorPresentationIds();

                for (Id id : eprIds) {
                    AdsEditorPresentationDef epr = referencedClass.getPresentations().getEditorPresentations().findById(id, EScope.ALL).get();
                    if (epr == null) {
                        error(explorerItem, problemHandler, "Unknown editor presentation in editor presentation list: #" + id);
                    } else {
                        if (epr.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && epr.getClientEnvironment() != env) {
                            error(explorerItem, problemHandler, "Editor presentation " + epr.getQualifiedName() + " is not available for client environment " + env.getName());
                        }

                        AdsUtils.checkAccessibility(explorerItem, epr, false, problemHandler);
                        CheckUtils.checkExportedApiDatails(explorerItem, epr, problemHandler);
                    }
                }
            }
        }
    }
}

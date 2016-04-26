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

import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

public abstract class AdsSelectorExplorerItemChecker<T extends AdsSelectorExplorerItemDef> extends AdsExplorerItemChecker<T> {

    @Override
    public void check(T explorerItem, IProblemHandler problemHandler) {
        super.check(explorerItem, problemHandler);
        if (!explorerItem.isReadOnly()) {//See RADIX-1833
            if (explorerItem.getCreationClassCatalogId() != null) {
                AdsClassCatalogDef cc = explorerItem.findCreationClassCatalog().get(AdsDefinitionChecker.<AdsClassCatalogDef>getSearchDuplicatesChecker(explorerItem, problemHandler));
                if (cc == null) {
                    error(explorerItem, problemHandler, "Creation class catalog not found: #" + explorerItem.getCreationClassCatalogId());
                } else {
                    AdsUtils.checkAccessibility(explorerItem, cc, false, problemHandler);
                    CheckUtils.checkExportedApiDatails(explorerItem, cc, problemHandler);
                }
            }
//            } else {
//                if (!explorerItem.getRestrictions().isDenied(ERestriction.CREATE) && !explorerItem.isClassCatalogInherited()) {
//                    AdsEntityObjectClassDef clazz = explorerItem.findReferencedEntityClass();
//                    if (clazz != null) {
//                        AdsUtils.checkAccessibility(explorerItem, clazz, false, problemHandler);
//                        CheckUtils.checkExportedApiDatails(explorerItem, clazz, problemHandler);
//                        DdsTableDef table = clazz.findTable(explorerItem);
//                        if (table != null && table.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES)) {
//                            warning(explorerItem, problemHandler, "Creation class catalog not specified");
//                        }
//                    }
//                }
//            }
            AdsEntityObjectClassDef referencedClass = explorerItem.findReferencedEntityClass();

            if (referencedClass == null) {
                error(explorerItem, problemHandler, "Can not find referenced class: #" + explorerItem.getClassId());
            } else {
                if (referencedClass.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && referencedClass.getClientEnvironment() != explorerItem.getClientEnvironment()) {

                    error(explorerItem, problemHandler, "Class " + referencedClass.getQualifiedName() + " is not available for client environment " + explorerItem.getClientEnvironment().getName());

                }

                AdsUtils.checkAccessibility(explorerItem, referencedClass, false, problemHandler);
                CheckUtils.checkExportedApiDatails(explorerItem, referencedClass, problemHandler);
            }
            if (explorerItem.getSelectorPresentationId() != null) {
                AdsSelectorPresentationDef spr = explorerItem.findReferencedSelectorPresentation().get(AdsDefinitionChecker.<AdsSelectorPresentationDef>getSearchDuplicatesChecker(explorerItem, problemHandler));
                if (spr == null) {
                    error(explorerItem, problemHandler, "Selector presentation not found: #" + explorerItem.getSelectorPresentationId());
                } else {
                    AdsUtils.checkAccessibility(explorerItem, spr, false, problemHandler);
                    CheckUtils.checkExportedApiDatails(explorerItem, spr, problemHandler);
                    CheckUtils.checkSelectorPresentationCreationOptions(explorerItem, explorerItem.getRestrictions(), spr, problemHandler);

                    ERuntimeEnvironmentType explorerItemEnv = explorerItem.getClientEnvironment();
                    ERuntimeEnvironmentType sprEnv = spr.getClientEnvironment();

                    if (sprEnv != ERuntimeEnvironmentType.COMMON_CLIENT && explorerItemEnv != sprEnv) {
                        StringBuilder envMess = new StringBuilder();
                        if (explorerItemEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                            if (sprEnv == ERuntimeEnvironmentType.WEB) {
                                envMess.append(
                                        ERuntimeEnvironmentType.EXPLORER.getName());
                            } else {
                                envMess.append(
                                        ERuntimeEnvironmentType.WEB.getName());
                            }
                        } else {
                            envMess.append(
                                    explorerItemEnv.getName());
                        }

                        error(explorerItem, problemHandler, "Selector presentation " + spr.getQualifiedName() + " will not be available for client environment " + envMess);
                    }

                    if (explorerItemEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {

                        ERuntimeEnvironmentType selectorPresentationClientEnvByEprs = spr.getClientEnvironmentByEditorPresentations();

                        if (selectorPresentationClientEnvByEprs != ERuntimeEnvironmentType.COMMON_CLIENT) {
                            warning(explorerItem, problemHandler, "Explorer item client environment (" + explorerItemEnv.getName() + ") does not match to actual client environment (computed by it's editor presentations) )of referenced presentation (" + selectorPresentationClientEnvByEprs.getName() + ")");
                        }
                    }

                }
            } else {
                error(explorerItem, problemHandler, "Selector presentation not specified");
            }
        }
    }
}

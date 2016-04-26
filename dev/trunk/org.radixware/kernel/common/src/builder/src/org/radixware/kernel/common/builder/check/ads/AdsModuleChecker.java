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

import java.io.File;
import java.util.List;
import org.radixware.kernel.common.builder.check.ads.clazz.AdsClassChecker;
import org.radixware.kernel.common.builder.check.common.ModuleChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.module.MetaInfServicesCatalog;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.fs.IRepositoryDefinition;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsModuleChecker extends ModuleChecker<AdsModule> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsModule.class;
    }

    @Override
    public void check(AdsModule module, IProblemHandler problemHandler) {
        super.check(module, problemHandler);

        final File[] imageDefFiles = module.getImages().collectImageDefFiles();
        if (imageDefFiles != null) {
            for (File imageDefFile : imageDefFiles) {
                final Id id = AdsDefinition.fileName2DefinitionId(imageDefFile);
                if (module.getImages().getById(id) == null) {
                    error(module, problemHandler, "Image #" + id + " not loaded");
                }
            }
        }

        final IRepositoryDefinition[] defRepositories = module.getRepository().listDefinitions();
        if (defRepositories != null) {
            for (IRepositoryDefinition defRepository : defRepositories) {
                //final File file = defRepository.getFile();
                final Id id = defRepository.getId();

                if (module.getDefinitions().findById(id) == null) {
                    error(module, problemHandler, "Definition #" + id + " not loaded");
                }
            }
        }

        if (module.getCompanionModuleId() != null) {
            AdsModule companion = module.findCompanionModule();
            if (companion == null) {
                error(module, problemHandler, "Companion module #" + module.getCompanionModuleId().toString() + " not found");
            }
        }

        checkMetaInfServices(module, problemHandler);

        if (!module.isUnderConstruction()) {
            if (module.isUnderConstruction(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE)) {
                error(module, problemHandler, "Module should be under construction, because overwritten module is under construction");
            }
        }

        if (!module.isUnderConstruction()) {
            checkUnderConstructionDependencies(module, problemHandler);
        }
    }

    private void checkUnderConstructionDependencies(final AdsModule module, final IProblemHandler problemHandler) {
        for (final Dependences.Dependence dependence : module.getDependences()) {
            final List<Module> dependenceModule = dependence.findDependenceModule(module);

            if (!dependenceModule.isEmpty()) {
                for (final Module mdl : dependenceModule) {
                    if (mdl instanceof AdsModule) {
                        if (((AdsModule) mdl).isUnderConstruction()) {
                            error(module, problemHandler, "Module depends from module '" + mdl.getQualifiedName() + "' that is in 'under construction' state");
                            break;
                        }
                    }
                }
            }
        }
    }

    private void checkMetaInfServices(AdsModule module, IProblemHandler problemHandler) {

        for (final MetaInfServicesCatalog.Service service : module.getServicesCatalog().getAllServices()) {
            Definition definition = service.getInterfaceIdPath().resolve(module).get();

            if (definition instanceof AdsClassDef) {

                final AdsClassDef serviceClass = (AdsClassDef) definition;
                for (final AdsPath path : service.getAllImplementations()) {
                    definition = path.resolve(module).get();

                    if (definition instanceof AdsClassDef) {
                        final AdsClassDef implementsClass = (AdsClassDef) definition;

                        switch (implementsClass.getClassDefType()) {
                            case DYNAMIC:
                                break;
                            default:
                                error(implementsClass, problemHandler, "The service implementation class must have dynamic type");
                        }

                        final AdsMethodDef defaultConstructor = AdsClassChecker.fingDefaultConstructor(implementsClass);

                        // check default constructor
                        if (implementsClass.hasConstructors() && (defaultConstructor == null || !defaultConstructor.getAccessFlags().isPublic())) {
                            error(implementsClass, problemHandler, "The service implementation class must have public default constructor without parameters");
                        }

                        // check environment
                        if (!ERuntimeEnvironmentType.compatibility(
                                implementsClass.getUsageEnvironment(), serviceClass.getUsageEnvironment())) {
                            error(module, problemHandler, "The service interface and implementation class have incompatible usage environments");
                        }

                        // check modifiers
                        if (implementsClass.getAccessFlags().isAbstract()) {
                            error(implementsClass, problemHandler, "The service implementation class must be concrete");
                        }

                    } else {
                        error(module, problemHandler, "Service implementation must be class");
                    }
                }
            } else {
                error(module, problemHandler, "Service must be interface");
            }
        }
    }
}
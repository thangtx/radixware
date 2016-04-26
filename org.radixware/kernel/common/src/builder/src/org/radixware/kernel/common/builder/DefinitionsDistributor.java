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

package org.radixware.kernel.common.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.ITitledDefinition;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentDeletionOptions;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.product.Definitions;
import org.radixware.schemas.product.Definitions.Definition;
import org.radixware.schemas.product.Classes;

import org.radixware.schemas.product.DefinitionsDocument;
import org.radixware.schemas.product.JavaClasses;
import org.radixware.schemas.product.UserReference;


public class DefinitionsDistributor {

    private final IBuildEnvironment buildEnv;
    private final BuildActionExecutor.EBuildActionType actionType;

    public DefinitionsDistributor(IBuildEnvironment buildEnv, BuildActionExecutor.EBuildActionType actionType) {
        this.buildEnv = buildEnv;
        this.actionType = actionType;
    }

    public void execute(Collection<AdsModule> modules) {
        processModules(modules);
    }

    public static void makeDefinitionsXml(AdsModule module, IBuildEnvironment buildEnv, BuildActionExecutor.EBuildActionType actionType) {
        DefinitionsDistributor distributor = new DefinitionsDistributor(buildEnv, actionType);
        distributor.execute(Collections.singleton(module));
    }

    private Classes getClasses(Definitions.Definition xDef, ERuntimeEnvironmentType sc) {
        Classes classes = null;
        switch (sc) {
            case SERVER:
                classes = xDef.getServerClasses();
                if (classes == null) {
                    classes = xDef.addNewServerClasses();
                }
                break;
            case COMMON:
                classes = xDef.getCommonClasses();
                if (classes == null) {
                    classes = xDef.addNewCommonClasses();
                }
                break;
            case EXPLORER:
                classes = xDef.getExplorerClasses();
                if (classes == null) {
                    classes = xDef.addNewExplorerClasses();
                }
                break;
            case WEB:
                classes = xDef.getWebClasses();
                if (classes == null) {
                    classes = xDef.addNewWebClasses();
                }
                break;
            case COMMON_CLIENT:
                classes = xDef.getCommonClientClasses();
                if (classes == null) {
                    classes = xDef.addNewCommonClientClasses();
                }
                break;

        }
        return classes;
    }

    private JavaClasses getJavaClasses(Definitions.Definition xDef, ERuntimeEnvironmentType sc, JavaSourceSupport.CodeType codeType) {
        final Classes classes = getClasses(xDef, sc);

        if (codeType == JavaSourceSupport.CodeType.META) {
            JavaClasses result = classes.getMeta();
            if (result == null) {
                result = classes.addNewMeta();
            }
            return result;
        } else {
            JavaClasses result = classes.getExecutable();
            if (result == null) {
                result = classes.addNewExecutable();
            }
            return result;
        }

    }

    private void cleanUpAdsModule(final AdsModule module) {
        buildEnv.getMutex().readAccess(new Runnable() {
            @Override
            public void run() {
                File metaDir = module.getDefinitions().getSourceDir(AdsDefinition.ESaveMode.API);
                if (metaDir != null && metaDir.exists()) {
                    FileUtils.deleteDirectory(metaDir);
                }
                File moduleDir = module.getDirectory();
                if (moduleDir != null) {
                    File indexFile = new File(moduleDir, FileUtils.DEFINITIONS_XML_FILE_NAME);
                    FileUtils.deleteFile(indexFile);
                }
            }
        });
    }

    private boolean wasCancelled() {
        if (buildEnv.getFlowLogger().getCancellable() != null) {
            return buildEnv.getFlowLogger().getCancellable().wasCancelled();
        } else {
            return false;
        }
    }

    private void updateAdsModules(Collection<AdsModule> modules) {
        final IProgressHandle handle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Distribute " + modules.size() + " modules...", new Cancellable() {
            @Override
            public boolean cancel() {
                if (buildEnv.getFlowLogger().getCancellable() != null) {
                    return buildEnv.getFlowLogger().getCancellable().cancel();
                } else {
                    return false;
                }
            }

            @Override
            public boolean wasCancelled() {
                return DefinitionsDistributor.this.wasCancelled();
            }
        });
        try {         
            handle.start(modules.size());


            List<AdsModule> processing = new ArrayList<>(modules.size());
            for (final AdsModule module : modules) {
                if (wasCancelled()) {
                    return;
                }
                if (module.isReadOnly() || module.getDirectory() == null) {
                    continue;
                }
                processing.add(module);

            }
            if (processing.isEmpty()) {
                return;
            }
            final int[] i = {0};
            final CountDownLatch waiter = new CountDownLatch(processing.size());
            final ExecutorService operationThreadPool = Executors.newFixedThreadPool(5);


            for (final AdsModule module : processing) {
                operationThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (wasCancelled()) {
                                return;
                            }
                            buildEnv.getMutex().readAccess(new ModuleProcessor(module));
                        } finally {
                            waiter.countDown();
                            try {
                                handle.progress(i[0]++);
                            } catch (Throwable e) {
                                Logger.getLogger(DefinitionsDistributor.class.getName()).log(Level.FINE, "Progress handle error", e);
                            }
                        }
                    }
                });
            }
            try {
                waiter.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } finally {
                operationThreadPool.shutdown();
            }
        } finally {
            handle.finish();
        }
    }

    private class ModuleProcessor implements Runnable {

        private final AdsModule module;

        public ModuleProcessor(AdsModule module) {
            this.module = module;
        }

        @Override
        public void run() {

            File indexFile = new File(module.getDirectory(), FileUtils.DEFINITIONS_XML_FILE_NAME);
            DefinitionsDocument xDoc = DefinitionsDocument.Factory.newInstance();

            final Definitions xDefs = xDoc.addNewDefinitions();
            if (module.getJavaSourceSupport().isSeparateFilesRequired(ERuntimeEnvironmentType.SERVER)) {
                xDefs.setServerFactory(module.getFactoryClassName());
            }
            final List<Definition> xDefList = new ArrayList<>(module.getDefinitions().size() * 2);
            module.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    final AdsDefinition def = (AdsDefinition) radixObject;
                    final Definition xDef = Definition.Factory.newInstance();
                    xDefList.add(xDef);

                    if (def.getDefinitionType() == EDefType.DOMAIN) {
                        xDef.setPath(Collections.singletonList(def.getId()));
                    } else {
                        xDef.setPath(Arrays.asList(def.getIdPath()));
                    }

                    xDef.setName(def.getQualifiedName(def.getModule()));
                    xDef.setType(def.getDefinitionType());
                    xDef.setEnvironment(def.getUsageEnvironment());

                    if (def instanceof ITitledDefinition) {
                        Id titleId = ((ITitledDefinition) def).getTitleId();
                        if (titleId != null) {
                            xDef.setTitleId(titleId);
                        }
                    }

                    if (def instanceof IXmlDefinition) {
                        xDef.setXmlTargetNamespace(((IXmlDefinition) def).getTargetNamespace());
                        xDef.setDefFileName(((IXmlDefinition) def).getJavaSourceSupport().getContentResourceName());
                    } else if (def instanceof IJavaSource) {

                        IJavaSource source = (IJavaSource) def;
                        JavaSourceSupport support = source.getJavaSourceSupport();

                        for (ERuntimeEnvironmentType sc : support.getSupportedEnvironments()) {
                            JavaClasses jc;
                            Set<JavaSourceSupport.CodeType> cts = support.getSeparateFileTypes(sc);
                            if ((cts == null || !cts.contains(JavaSourceSupport.CodeType.EXCUTABLE)) && def instanceof IPlatformClassPublisher && def.getUsageEnvironment() == sc) {
                                IPlatformClassPublisher.IPlatformClassPublishingSupport pps = ((IPlatformClassPublisher) def).getPlatformClassPublishingSupport();
                                if (pps != null && pps.isPlatformClassPublisher() && !pps.isExtendablePublishing()) {
                                    jc = getJavaClasses(xDef, sc, JavaSourceSupport.CodeType.EXCUTABLE);
                                    JavaClasses.Class xClass = jc.addNewClass1();
                                    xClass.setName(pps.getPlatformClassName());
                                }
                            }
                            if (cts != null || def.getDefinitionType() == EDefType.DOMAIN) {
                                if (cts != null) {
                                    for (JavaSourceSupport.CodeType ct : cts) {
                                        jc = getJavaClasses(xDef, sc, ct);
                                        char[][] className = support.getMainClassName(JavaSourceSupport.UsagePurpose.getPurpose(sc, ct));
                                        JavaClasses.Class xClass = jc.addNewClass1();
                                        xClass.setName(new String(CharOperations.merge(className, '.')));
                                    }
                                } else {
                                    jc = getJavaClasses(xDef, sc, JavaSourceSupport.CodeType.META);
                                    char[][] className = support.getMainClassName(JavaSourceSupport.UsagePurpose.getPurpose(sc, JavaSourceSupport.CodeType.META));
                                    JavaClasses.Class xClass = jc.addNewClass1();
                                    xClass.setName(new String(CharOperations.merge(className, '.')));
                                }
                            }
                        }
                    }
                    if (def instanceof AdsClassDef) {
                        AdsClassDef clazz = (AdsClassDef) def;
                        xDef.setUploadMode(clazz.getRuntimeUploadMode());
                        if (clazz.getClassDefType() != EClassType.INTERFACE) {
                            AdsTypeDeclaration decl = clazz.getInheritance().getSuperClassRef();
                            if (decl != null && decl.getTypeId() == EValType.USER_CLASS) {
                                getClasses(xDef, clazz.getUsageEnvironment()).addNewExtends().setPath(decl.getPath().asList());
                            }
                        }
                        List<AdsTypeDeclaration> impls = clazz.getInheritance().getInerfaceRefList(EScope.LOCAL_AND_OVERWRITE);

                        if (!impls.isEmpty()) {
                            Classes classes = getClasses(xDef, clazz.getUsageEnvironment());
                            for (AdsTypeDeclaration impl : impls) {
                                classes.addNewImplements().setPath(impl.getPath().asList());
                            }
                        }

                        final Definition.UserReferences userReferences = xDef.addNewUserReferences();

                        for (AdsPropertyDef propertyDef : clazz.getProperties().get(EScope.LOCAL)) {
                            if (propertyDef instanceof AdsUserPropertyDef) {
                                final AdsTypeDeclaration typeDeclaration = propertyDef.getValue().getType();
                                if (typeDeclaration != null) {
                                    if (typeDeclaration.getTypeId() == EValType.PARENT_REF || typeDeclaration.getTypeId() == EValType.ARR_REF) {
                                        final AdsType type = typeDeclaration.resolve(propertyDef).get();
                                        if (type instanceof AdsDefinitionType) {
                                            final org.radixware.kernel.common.defs.Definition referencedDef = ((AdsDefinitionType) type).getSource();
                                            if (referencedDef instanceof AdsEntityObjectClassDef) {
                                                final UserReference ref = userReferences.addNewReference();
                                                ref.setTableId(((AdsEntityObjectClassDef) referencedDef).getEntityId());
                                                ref.setType(typeDeclaration.getTypeId());
                                                ref.setUserPropId(propertyDef.getId());
                                                final ParentDeletionOptions options = ((AdsUserPropertyDef) propertyDef).getParentDeletionOptions();
                                                if (options != null) {
                                                    ref.setDeleteMode(options.getDeleteMode());
                                                    ref.setConfirmationRequired(options.isConfirmationRequired());
                                                } else {
                                                    ref.setDeleteMode(EDeleteMode.NONE);
                                                    ref.setConfirmationRequired(false);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    if (def instanceof AdsLocalizingBundleDef) {
                        AdsLocalizingBundleDef bundle = (AdsLocalizingBundleDef) def;
                        final List<AdsMultilingualStringDef> strings = bundle.getStrings().get(EScope.LOCAL);
                        if (strings != null) {
                            final Definition.EventCodes eventCodes = xDef.addNewEventCodes();
                            for (AdsMultilingualStringDef string : strings) {
                                if (string instanceof AdsEventCodeDef) {
                                    final AdsEventCodeDef codeDef = (AdsEventCodeDef) string;
                                    final Definition.EventCodes.EventCode xCode = eventCodes.addNewEventCode();
                                    xCode.setId(codeDef.getId());
                                    if (codeDef.getEventSeverity() != null) {
                                        xCode.setEventSeverity(codeDef.getEventSeverity().getValue());
                                    } else {
                                        xCode.setEventSeverity(EEventSeverity.DEBUG.getValue());
                                    }
                                    xCode.setEventSource(codeDef.getEventSource());
                                }
                            }
                        }
                    }
                    if (def instanceof AdsDomainDef) {
                        if (!def.isTopLevelDefinition()) {
                            xDef.setDomains(Collections.singletonList(((AdsDomainDef) def).getOwnerDomain().getId()));
                        }
                    } else {
                        List<Id> domainIds = Arrays.asList(def.getDomains().getDomaindIds());
                        if (!domainIds.isEmpty()) {
                            xDef.setDomains(domainIds);
                        }
                    }
                }
            },
                    new AdsVisitorProvider() {
                @Override
                public boolean isTarget(final RadixObject object) {
                    if (object instanceof AdsDefinition) {
                        final AdsDefinition def = ((AdsDefinition) object);
                        final EDefType defType = def.getDefinitionType();
                        switch (defType) {
                            case CLASS:
                            case CONTEXTLESS_COMMAND:
                            case PARAGRAPH:
                            case ROLE:
                            case DATA_SEGMENT:
                            case ENUMERATION:
                            case XML_SCHEME:
                            case MSDL_SCHEME:
                            case LOCALIZING_BUNDLE:
                            case EDITOR_PRESENTATION:
                            case SELECTOR_PRESENTATION:
                            case CUSTOM_DIALOG:
                            case CUSTOM_PROP_EDITOR:
                            case CUSTOM_PARAG_EDITOR:
                            case CUSTOM_PAGE_EDITOR:
                            case CUSTOM_EDITOR:
                            case CUSTOM_SELECTOR:
                            case CUSTOM_FORM_EDITOR:
                            case CUSTOM_REPORT_EDITOR:
                            case CUSTOM_FILTER_DIALOG:
                            case DOMAIN:
                                return true;
                            default:
                                return false;
                        }
                    } else {
                        return false;
                    }
                }
            });

            final String imagePathPrefix = new String(CharOperations.merge(JavaSourceSupport.getPackageNameComponents(module, JavaSourceSupport.UsagePurpose.COMMON_EXECUTABLE), '/'));
            for (AdsImageDef image : module.getImages()) {
                if (image.getImageFile() != null) {
                    final Definition xDef = Definition.Factory.newInstance();
                    xDefList.add(xDef);
                    xDef.setPath(Arrays.asList(image.getIdPath()));
                    xDef.setType(EDefType.IMAGE);
                    xDef.setDefFileName(imagePathPrefix + "/" + image.getImageFile().getName());
                }
            }
            xDefs.setDefinitionArray(xDefList.toArray(new Definition[xDefList.size()]));
            try {
                final boolean exist = indexFile.exists();
                XmlUtils.saveXmlPrettyNoLock(xDoc, indexFile);
                if (exist) {
                    buildEnv.getFlowLogger().message("[dist] Definitions index file updated: " + indexFile.getAbsolutePath());
                } else {
                    buildEnv.getFlowLogger().message("[dist] Definitions index file created: " + indexFile.getAbsolutePath());
                }
            } catch (IOException ex) {
                buildEnv.getBuildProblemHandler().accept(RadixProblem.Factory.newError(module, "Can not create definition index file: " + ex.getMessage()));
            }
        }
    }

    private void cleanUpAdsModules(Collection<AdsModule> modules) {
        final IProgressHandle handle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Cleanup distribution...");
        try {

            handle.start(modules.size());


            List<AdsModule> processing = new ArrayList(modules.size());
            for (AdsModule module : modules) {
                if (module.isReadOnly()) {
                    continue;
                }
                processing.add(module);
            }
            if (processing.isEmpty()) {
                return;
            }
            final int[] i = {0};
            final CountDownLatch waiter = new CountDownLatch(processing.size());
            final ExecutorService operationThreadPool = Executors.newFixedThreadPool(3);
            try {
                for (final AdsModule module : processing) {
                    operationThreadPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                cleanUpAdsModule(module);
                            } catch (Throwable e) {
                                Logger.getLogger(DefinitionsDistributor.class.getName()).log(Level.SEVERE, "Error on module cleamup", e);
                            } finally {
                                waiter.countDown();
                                try {
                                    handle.progress(i[0]++);
                                } catch (Throwable e) {
                                    Logger.getLogger(DefinitionsDistributor.class.getName()).log(Level.FINE, "Error on module cleamup", e);
                                }
                            }

                        }
                    });
                }
                try {
                    waiter.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            } finally {
                operationThreadPool.shutdown();
            }
        } finally {
            handle.finish();
        }

    }

    private void processModules(Collection<AdsModule> modules) {

        if (actionType == BuildActionExecutor.EBuildActionType.CLEAN || actionType == BuildActionExecutor.EBuildActionType.CLEAN_AND_BUILD) {
            cleanUpAdsModules(modules);
            if (actionType == BuildActionExecutor.EBuildActionType.CLEAN) {
                return;
            }
        }
        updateAdsModules(modules);
    }
}

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

import java.util.*;
import org.radixware.kernel.common.builder.api.IBuildDisplayer;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.builder.check.common.Checker;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEmbeddedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.IClassInclusive;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.defs.uds.files.UdsDirectory;
import org.radixware.kernel.common.defs.uds.files.UdsFile;
import org.radixware.kernel.common.defs.uds.files.UdsFileRadixObjects;
import org.radixware.kernel.common.defs.uds.files.UdsXmlFile;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.uds.UdsSegment;
import org.radixware.kernel.common.types.Id;

class ContextChecker {

    class ContextInfo {

        Set<Definition> targets;
        List<Module> processedModules;
        List<Definition> checkFailedDefinitions;

        public ContextInfo(Set<Definition> targets, List<Module> processedModules, List<Definition> checkFailedDefinitions) {
            this.targets = targets;
            this.checkFailedDefinitions = checkFailedDefinitions;
            this.processedModules = processedModules;
        }
    }
    private boolean skipCheck = false;

    public ContextChecker(boolean skipCheck) {
        this.skipCheck = skipCheck;
    }

    private boolean isCancelled(IBuildEnvironment buildEnv) {
        Cancellable c = buildEnv.getFlowLogger().getCancellable();
        return c != null && c.wasCancelled();
    }

    Set<Layer> processedLayers = new HashSet<>();

    ContextInfo determineTargets(BuildActionExecutor.EBuildActionType actionType,
            RadixObject contexts[], IBuildEnvironment buildEnv, final boolean suppressProblemsClear) {
        IBuildDisplayer buildDisplayer = buildEnv.getBuildDisplayer();
        IProgressHandle handle = buildDisplayer.getProgressHandleFactory().createHandle("Check...", buildEnv.getFlowLogger().getCancellable());
        try {
            PlatformLib.enableClassCaching(true);
            handle.start();

            ArrayList<Definition> result = new ArrayList<>();
            final ArrayList<Module> exclude = new ArrayList<>();

            for (RadixObject context : contexts) {
                if (context instanceof AdsDefinition) {

                    search:
                    if (context instanceof AdsEntityModelClassDef) {
                        AdsEditorPresentationDef epr = ((AdsEntityModelClassDef) context).getOwnerEditorPresentation();
                        if (epr != null) {
                            result.add(epr);
                        }
                    } else if (context instanceof AdsGroupModelClassDef) {
                        AdsSelectorPresentationDef spr = ((AdsGroupModelClassDef) context).getOwnerSelectorPresentation();
                        if (spr != null) {
                            result.add(spr);
                        }
                    } else {
                        if (context instanceof AdsClassDef) {
                            AdsClassDef cls = (AdsClassDef) context;
                            if (cls instanceof AdsEmbeddedClassDef) {
                                IClassInclusive hostObject = ((AdsEmbeddedClassDef) cls).getHostObject();
                                if (hostObject instanceof IAdsClassMember) {
                                    cls = ((IAdsClassMember) hostObject).getOwnerClass();
                                } else {
                                    break search;
                                }
                            }

                            if (cls.isNested()) {
                                result.add(cls.getTopLevelEnclosingClass());
                                break search;
                            } else if (cls != context) {
                                result.add(cls);
                                break search;
                            }
                        }

                        result.add((AdsDefinition) context);
                    }

                    AdsModule module = (AdsModule) context.getModule();
                    if (!suppressProblemsClear) {
                        resetModuleChecks(module, module.getSegment());
                    }
                    processedModules.add(module);
                    exclude.add(module);
                } else if (context instanceof UdsDefinition) {
                    result.add((UdsDefinition) context);
                    UdsModule module = (UdsModule) context.getModule();
                    if (!suppressProblemsClear) {
                        resetModuleChecks(module, module.getSegment());
                    }
                    processedModules.add(module);
                    exclude.add(module);
                } else if (context instanceof UdsFile) {
                    UdsModule module = (UdsModule) context.getModule();
                    if (!suppressProblemsClear) {
                        resetModuleChecks(module, module.getSegment());
                    }
                    processedModules.add(module);
                    exclude.add(module);
                    processUdsFile(module, (UdsFile) context, result, buildEnv);
                } if (context instanceof UdsFileRadixObjects) {
                    Module module = ((UdsFileRadixObjects) context).getModule();
                    if (!suppressProblemsClear) {
                        resetModuleChecks(module, module.getSegment());
                    }
                    processedModules.add(module);
                    exclude.add(module);
                    processUdsFileRadixObjects((UdsFileRadixObjects) context, result, buildEnv);
                } else if (context instanceof AdsModule) {
                    AdsModule module = (AdsModule) context;
                    if (!suppressProblemsClear) {
                        resetModuleChecks(module, module.getSegment());
                    }
                    processedModules.add(module);
                    processModule(module, result, buildEnv);
                } else if (context instanceof UdsModule) {
                    UdsModule module = (UdsModule) context;
                    if (!suppressProblemsClear) {
                        resetModuleChecks(module, module.getSegment());
                    }
                    processedModules.add(module);
                    processModule(module, result, buildEnv);
                } else if (context instanceof AdsSegment) {
                    processSegment((AdsSegment) context, result, handle, buildEnv);
                } else if (context instanceof UdsSegment) {
                    processSegment((UdsSegment) context, result, handle, buildEnv);
                } else if (context instanceof Layer) {
                    if (!suppressProblemsClear) {
                        resetModuleChecks((Layer) context, buildEnv);
                    }
                    processLayer((Layer) context, result, handle, buildEnv);
                } else if (context instanceof Branch) {
                    List<Layer> layers = ((Branch) context).getLayers().getInOrder();
                    if (!suppressProblemsClear) {
                        for (Layer l : layers) {
                            resetModuleChecks(l, buildEnv);
                        }
                    }
                    for (Layer l : layers) {
                        processLayer(l, result, handle, buildEnv);
                    }
                } 
                if (isCancelled(buildEnv)) {
                    return null;
                }
            }
            if (isCancelled(buildEnv)) {
                return null;
            }

            //c.prepareCheck();
            Set<Definition> checkedDefinitions = new HashSet<>();
            List<Definition> checkFailedDefinitions = new ArrayList<>();
            if (actionType != BuildActionExecutor.EBuildActionType.CLEAN && !skipCheck) {
                Checker c = new Checker(buildEnv.getBuildProblemHandler(), buildEnv.getCheckOptions());
                int checkSize = result.size() + processedModules.size() + processedLayers.size();
                handle.switchToDeterminate(checkSize);
                int count = 0;
                int stepSize = checkSize > 100 ? checkSize / 100 : 1;
                for (Layer l : processedLayers){
                    if (isCancelled(buildEnv)) {
                        return null;
                    }
                    if (!l.isReadOnly()){
                        c.checkRadixObject(l);
                    }
                    count++;
                    if (count % stepSize == 0) {
                        handle.progress("Check", count);
                    }
                }
                for (Module m : processedModules) {
                    if (isCancelled(buildEnv)) {
                        return null;
                    }
                    if (!result.contains(m) && !exclude.contains(m)) {
                        c.checkRadixObject(m);
                    }
                    count++;
                    if (count % stepSize == 0) {
                        handle.progress("Check", count);
                    }
                }
                for (Definition def : result) {
                    if (isCancelled(buildEnv)) {
                        return null;
                    }
                    if (c.check(Collections.singletonList(def))) {
                        //   buildEnv.getFlowLogger().message("[check] Build target found: " + def.getQualifiedName() + " (" + def.getId() + ")");
                        checkedDefinitions.add(def);
                    } else {
                        checkFailedDefinitions.add(def);
                    }
                    count++;
                    if (count % stepSize == 0) {
                        handle.progress("Check", count);
                    }

                }
            } else {
                checkedDefinitions.addAll(result);
            }
            //c.completeCheck();

            if (isCancelled(buildEnv)) {
                return null;
            }
            buildEnv.getFlowLogger().message("[check] " + checkedDefinitions.size() + " definition(s) added to build list");

            if (buildEnv.getActionType() == BuildActionExecutor.EBuildActionType.COMPILE_SINGLE) {
                Set<AdsDefinition> bundles = new HashSet<>();
                for (Definition def : checkedDefinitions) {
                    if (def instanceof AdsDefinition) {
                        AdsDefinition adsdef = (AdsDefinition) def;
                        if (adsdef.isTopLevelDefinition()) {
                            AdsLocalizingBundleDef bundle = adsdef.findExistingLocalizingBundle();
                            if (bundle != null && !bundles.contains(bundle) && !checkedDefinitions.contains(bundle)) {
                                bundles.add(bundle);
                            }
                        }
                    }
                }
                if (!bundles.isEmpty()) {
                    checkedDefinitions.addAll(bundles);
                    buildEnv.getFlowLogger().message("[check] " + bundles.size() + " string bundle(s) added to build list");
                }
            }

//            if (actionType != BuildActionExecutor.EBuildActionType.CLEAN) {
////                final HashSet<Segment> segments = new HashSet<Segment>();
////                RadixProblemRegistry.getDefault().clear(processedModules);
////                for (AdsModule module : processedModules) {
////                    c.checkRadixObject(module);
////                    segments.add(module.getSegment());
////                }
////                RadixProblemRegistry.getDefault().clear(segments);
////                for (Segment s : segments) {
////                    c.checkRadixObject(s);
////                }
//            }
            return new ContextInfo(checkedDefinitions, /*
                     * layers,
                     */ processedModules, checkFailedDefinitions);
        } finally {
            PlatformLib.enableClassCaching(false);
            handle.finish();
        }
    }
    private final HashMap<Id, Definition> defsIndex = new HashMap<>();
    private final HashMap<Id, Module> modulesIndex = new HashMap<>();
    private final ArrayList<Module> processedModules = new ArrayList<>();

    private void resetModuleChecks(Layer layer, IBuildEnvironment buildEnv) {
        if (!layer.isReadOnly()) {
            AdsSegment segment = (AdsSegment) layer.getAds();
            RadixProblemRegistry.getDefault().clear(Collections.singleton(segment));
            for (AdsModule module : segment.getModules()) {
                resetModuleChecks(module, null);
                if (isCancelled(buildEnv)) {
                    return;
                }
            }
            RadixProblemRegistry.getDefault().clear(Collections.singleton(layer));
        }
    }

    private void resetModuleChecks(Module module, Segment segment) {
        if (!module.isReadOnly()) {
            if (segment != null) {
                RadixProblemRegistry.getDefault().clear(Collections.singleton(segment));
            }
            RadixProblemRegistry.getDefault().clear(Collections.singleton(module));
        }
    }

    private void processModule(AdsModule module, List<Definition> result, IBuildEnvironment buildEnv) {
        AdsModule m = module;
        boolean isLocaleLayer = m.getLayer().isLocalizing();
        defsIndex.clear();
        while (m != null) {
            if (isCancelled(buildEnv)) {
                return;
            }
            if (!m.isReadOnly()) {
                if (!isLocaleLayer) {
                    final AdsLocalizingBundleDef moduleBundle = module.findExistingLocalizingBundle();
                    if (moduleBundle != null) {
                        if (!defsIndex.containsKey(moduleBundle.getId())) {
                            defsIndex.put(moduleBundle.getId(), moduleBundle);
                        }
                    }
                    for (AdsDefinition def : m.getDefinitions()) {
                        if (!defsIndex.containsKey(def.getId())) {
                            defsIndex.put(def.getId(), def);
                        }
                        if (def.getDefinitionType() != EDefType.LOCALIZING_BUNDLE) {
                            AdsLocalizingBundleDef bundle = def.findExistingLocalizingBundle();
                            if (bundle != null) {
                                if (!defsIndex.containsKey(bundle.getId())) {
                                    defsIndex.put(bundle.getId(), bundle);
                                }
                            }
                        }
                    }
                    if (module instanceof UdsModule) {
                        UdsModule udsModule = (UdsModule) module;
                        for (RadixObject udsFile : udsModule.getUdsFiles()) {
                            processUdsFile(udsModule, udsFile, result, buildEnv);
                        }
                    }
                }
                if (isCancelled(buildEnv)) {
                    return;
                }
            }
            m = m.findOverwritten();
        }
        result.addAll(defsIndex.values());
    }

    private void processModule(UdsModule module, List<Definition> result, IBuildEnvironment buildEnv) {
        defsIndex.clear();

        if (isCancelled(buildEnv)) {
            return;
        }
        if (!module.isReadOnly()) {
            for (AdsDefinition def : module.getDefinitions()) {
                if (!defsIndex.containsKey(def.getId())) {
                    defsIndex.put(def.getId(), def);
                }

            }
            for (RadixObject udsFile: module.getUdsFiles()){
                processUdsFile(module, udsFile, result, buildEnv);
            }
            if (isCancelled(buildEnv)) {
                return;
            }
        }

        result.addAll(defsIndex.values());
    }
    
    private void processUdsFile(UdsModule module, RadixObject file, List<Definition> result, IBuildEnvironment buildEnv) {

        if (isCancelled(buildEnv)) {
            return;
        }
        
        if (module.isReadOnly()){
            return;
        }
        
        if (file instanceof Definition){
            result.add((Definition) file);
        }
        
        if (file instanceof UdsDirectory){
            UdsDirectory directory = (UdsDirectory) file;
            for (RadixObject f : directory.getFiles()){
                processUdsFile(module, f, result, buildEnv);
            }
        }
        if (file instanceof UdsXmlFile) {
            UdsXmlFile xmlFile = (UdsXmlFile) file;
            processUdsFileRadixObjects(xmlFile.getUdsDefinitions(), result, buildEnv);
        }
        if (file instanceof UdsFileRadixObjects) {
            processUdsFileRadixObjects((UdsFileRadixObjects) file, result, buildEnv);
        }
    }
    
    private void processUdsFileRadixObjects(UdsFileRadixObjects objects, List<Definition> result, IBuildEnvironment buildEnv) {
        if (isCancelled(buildEnv)) {
            return;
        }
        for (RadixObject radixObject : objects) {
            if (radixObject instanceof Definition) {
                result.add((Definition) radixObject);
                if (isCancelled(buildEnv)) {
                    return;
                }
            } else if (radixObject instanceof UdsFileRadixObjects) {
                processUdsFileRadixObjects((UdsFileRadixObjects) radixObject, result, buildEnv);
            }
        }
    }

    private void processLayer(Layer layer, List<Definition> result, IProgressHandle handle, IBuildEnvironment buildEnv) {

        modulesIndex.clear();
        if (isCancelled(buildEnv)) {
            return;
        }
        if (!layer.isReadOnly()) {
            processedLayers.add(layer);
            AdsSegment segment = (AdsSegment) layer.getAds();
            for (AdsModule module : segment.getModules()) {
                if (buildEnv.getActionType() == BuildActionExecutor.EBuildActionType.RELEASE && module.isUnderConstruction()) {
                    continue;
                }
                processedModules.add(module);
                if (!modulesIndex.containsKey(module.getId())) {
                    modulesIndex.put(module.getId(), module);
                }
            }
            if (buildEnv.getBuildOptions().isBuildUds()) {
                UdsSegment uds = (UdsSegment) layer.getUds();
                for (AdsModule module : uds.getModules()) {
                    processedModules.add(module);
                    if (!modulesIndex.containsKey(module.getId())) {
                        modulesIndex.put(module.getId(), module);
                    }
                }
                if (isCancelled(buildEnv)) {
                    return;
                }
            }
        }

        int count = modulesIndex.size();
        int current = 0;
        handle.switchToDeterminate(count);

        for (Module module : modulesIndex.values()) {
            if (module instanceof AdsModule) {
                processModule((AdsModule) module, result, buildEnv);
            } else {
                processModule((UdsModule) module, result, buildEnv);
            }
            handle.progress(current++);
        }
    }

    private void processSegment(Segment segment, List<Definition> result, IProgressHandle handle, IBuildEnvironment buildEnv) {
        modulesIndex.clear();
        if (isCancelled(buildEnv)) {
            return;
        }
        if (!segment.isReadOnly()) {
            for (Object m : segment.getModules()) {
                Module module = (Module) m;
                processedModules.add(module);
                if (!modulesIndex.containsKey(module.getId())) {
                    modulesIndex.put(module.getId(), module);
                }
            }

            if (isCancelled(buildEnv)) {
                return;
            }
        }

        int count = modulesIndex.size();
        int current = 0;
        handle.switchToDeterminate(count);

        for (Module module : modulesIndex.values()) {
            if (module instanceof AdsModule) {
                processModule((AdsModule) module, result, buildEnv);
            } else {
                processModule((UdsModule) module, result, buildEnv);
            }
            handle.progress(current++);
        }
    }
}

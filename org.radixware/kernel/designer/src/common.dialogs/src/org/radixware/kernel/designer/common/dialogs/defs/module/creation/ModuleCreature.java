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

package org.radixware.kernel.designer.common.dialogs.defs.module.creation;

import java.io.File;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.builder.DefinitionsDistributor;
import org.radixware.kernel.common.builder.DirectoryFileChecker;
import org.radixware.kernel.common.builder.SqmlDistributor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.resources.icons.RadixIcon;

import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.NamedRadixObjectCreature;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public class ModuleCreature extends NamedRadixObjectCreature<Module> {
    
    private boolean overwrite = false;
    private Module moduleForOverwrite = null;
    private final Segment context;
    
    @SuppressWarnings("unchecked")
    public ModuleCreature(Segment context) {
        super(context.getModules(), "Module");
        this.context = context;
    }
    
    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {
            @Override
            public CreatureSetupStep createFirstStep() {
                return new ModuleCreatureWizardStep1(ModuleCreature.this);
            }
            
            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Module createInstance(final String name) {
        if (overwrite) {
            final Module instance = context.getModules().overwrite(moduleForOverwrite);
            instance.setName(name);
            if (instance instanceof DdsModule) {
                afterAppend(instance);
            }
            return instance;
        } else {
            if (context.getType() == ERepositorySegmentType.ADS) {
                return AdsModule.Factory.getDefault().newInstance(name);
            } else if (context.getType() == ERepositorySegmentType.UDS) {
                return UdsModule.Factory.getDefault().newInstance(name);
            } else {
                return DdsModule.Factory.getDefault().newInstance(name);
            }
        }
    }
    
    @Override
    public String getDescription() {
        return NbBundle.getMessage(ModuleCreature.class, "CreatureDescription");
    }
    
    @Override
    public boolean isEnabled() {
        return !context.isReadOnly();
    }
    
    @Override
    public RadixIcon getIcon() {
        switch (context.getType()) {
            case ADS: return AdsDefinitionIcon.MODULE;
            case UDS: return UdsDefinitionIcon.MODULE;
            default:
            return DdsDefinitionIcon.MODULE;
        }
    }
    
    public Segment getSegment() {
        return context;
    }
    
    public boolean isOverwrite() {
        return overwrite;
    }
    
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }
    
    public Module getModuleForOverwrite() {
        return moduleForOverwrite;
    }
    
    public void setModuleForOverwrite(Module moduleForOverwrite) {
        this.moduleForOverwrite = moduleForOverwrite;
    }

//    private static void svnAdd(Module module) {
//        final Branch branch = module.getSegment().getLayer().getBranch();
//        try {
//            final RadixSvnUtils svn = RadixSvnUtils.Factory.newInstance(branch);
//            if (svn != null) {
//                svn.addDirectory(module.getDirectory()); // recurs
//                //svn.addFile(module.getFile());
//                svn.setIgnorePatterns(module.getDirectory(), Arrays.asList(
//                        AdsModule.BUILD_DIR_NAME,
//                        AdsModule.BINARIES_DIR_NAME,
//                        AdsModule.STRIP_SOURCES_DIR_NAME,
//                        FileUtils.DIRECTORY_XML_FILE_NAME,
//                        FileUtils.DEFINITIONS_XML_FILE_NAME));
//            }
//        } catch (SVNClientException error) {
//            DialogUtils.messageError(error);
//        }
//    }
    @Override
    public void afterAppend(Module module) {
        if (module instanceof AdsModule) {
            // make directory.xml
            DirectoryFileChecker checker = DirectoryFileChecker.Factory.newInstance(module, new DesignerBuildEnvironment(false, EBuildActionType.UPDATE_DIST));
            checker.update();

            // make definitions.xml
            AdsModule adsModule = (AdsModule) module;
            final DesignerBuildEnvironment buildEnvironment = new DesignerBuildEnvironment(false, EBuildActionType.UPDATE_DIST);
                    
            DefinitionsDistributor.makeDefinitionsXml(adsModule, buildEnvironment, EBuildActionType.UPDATE_DIST);
            SqmlDistributor.makeDefinitionsXml(adsModule, buildEnvironment, EBuildActionType.UPDATE_DIST);
            // add svn:ignore dirs
            final File dir = module.getDirectory();
            final FileObject dirFo = RadixFileUtil.toFileObject(dir);
            try {
                dirFo.createFolder(AdsModule.BUILD_DIR_NAME);
                dirFo.createFolder(AdsModule.BINARIES_DIR_NAME);
                dirFo.createFolder(AdsModule.STRIP_SOURCES_DIR_NAME);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (module instanceof DdsModule) {
            try {
                ((DdsModule) module).getModelManager().switchModelToModificationState();
            } catch (IOException ex) {
                DialogUtils.messageError(ex);
            }
        }
    }
}

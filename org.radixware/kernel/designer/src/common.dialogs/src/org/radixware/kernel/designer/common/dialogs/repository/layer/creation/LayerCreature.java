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

package org.radixware.kernel.designer.common.dialogs.repository.layer.creation;

import java.util.ArrayList;
import java.util.List;
import org.openide.util.Cancellable;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.builder.DirectoryFileChecker;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class LayerCreature extends Creature<Layer> {

    Branch branch;
    protected List<EIsoLanguage> languages;
    protected Layer prevLayer;
    protected String copyright, title, URI, name;
    protected boolean isLocalizing;

    @Override
    public String getDisplayName() {
        return "Layer";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public Layer createInstance() {
        return Layer.Factory.newInstance();
    }

    public LayerCreature(Branch branch) {
        super(branch.getLayers());
        this.branch = branch;
        final Layer baseLayer = branch.getBaseDevelopmentLayer();
        if (baseLayer != null) {
            this.languages = baseLayer.getLanguages();
            this.copyright = baseLayer.getCopyright();
            this.title = baseLayer.getTitle();
            this.prevLayer = baseLayer;
        } else {
            this.languages = new ArrayList<>();
            this.languages.add(EIsoLanguage.ENGLISH);
            this.copyright = "";
            this.title = "New Layer";
            List<Layer> tops = branch.getLayers().findTop();
            if (!tops.isEmpty()) {
                this.prevLayer = tops.get(0);
            } else {
                this.prevLayer = null;
            }
        }
        this.isLocalizing=false;
        this.URI = "";
        this.name = "newLayer";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean afterCreate(Layer layer) {
        layer.setName(name);
        layer.setTitle(title);
        layer.setCopyright(copyright);
        layer.setURI(URI);
        layer.setLanguages(languages);
        layer.addBaseLayer(prevLayer);
        layer.setIsLocalizing(isLocalizing);        
        return true;
    }
//
//    private static void svnAdd(Layer layer) {
//        final Branch branch = layer.getBranch();
//        try {
//            final RadixSvnUtils svn = RadixSvnUtils.Factory.newInstance(branch);
//            if (svn != null) {
//                svn.addDirectory(layer.getDirectory()); // recursively
//                //svn.addFile(layer.getFile());
//                //svn.addFile(layer.getAds().getDirectory());
//                //svn.addFile(layer.getDds().getDirectory());
//                svn.setIgnorePatterns(layer.getAds().getDirectory(), Collections.singletonList(FileUtils.DIRECTORY_XML_FILE_NAME));
//            }
//        } catch (SVNClientException error) {
//            DialogUtils.messageError(error);
//        }
//    }
    
    private static final class Cancel implements Cancellable {

        boolean canceled = false;

        @Override
        public boolean cancel() {
            canceled = true;
            return false;
        }
    }

    @Override
    public void afterAppend(Layer layer) {
        DirectoryFileChecker checker = DirectoryFileChecker.Factory.newInstance(layer, new DesignerBuildEnvironment(false, EBuildActionType.UPDATE_DIST));
        checker.update();
        /*if(isLocalizing){     
            final Cancel cancel = new Cancel();
            ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Create localizing layer...", cancel);
            try{
                progressHandle.start(prevLayer.getAds().getModules().size());
                int cnt=0;
                for(Module module : prevLayer.getAds().getModules()){                
                   AdsModule localizingModule=AdsModule.Factory.getDefault().overwrite((AdsModule)module);
                   localizingModule.setName(module.getName());
                   for(AdsDefinition def:localizingModule.getDefinitions()){
                      def.setUploadMode(AdsDefinition.ESaveMode.API);
                   }
                   layer.getAds().getModules().add(localizingModule);

                   try {
                        for(AdsDefinition def:((AdsModule)module).getDefinitions()){                   
                             AdsLocalizingBundleDef newMlbundle=localizingModule.getDefinitions().findLocalizingBundleDef(def,false); 
                             if(newMlbundle!=null)
                                newMlbundle.save();                       
                        }
                        layer.save();
                   }catch (IOException ex) {
                       Exceptions.printStackTrace(ex);
                   }
                   progressHandle.progress(cnt++);
                }
            }finally{
                progressHandle.finish();
            }
        }*/
        //svnAdd(layer);
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public boolean hasWizard() {
                return true;
            }

            @Override
            public CreatureSetupStep createFirstStep() {
                return new LayerCreatureWizardStep1();
            }
        };
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }
    
    public void setIsLocalizing(boolean isLocalizing) {
        this.isLocalizing=isLocalizing;
        //if(isLocalizing)
        //    this.URI = prevLayer.getURI()+Layer.LOCALE_LAYER_SUFFIX+languages.get(0).getValue();
    }

    public void setLanguages(List<EIsoLanguage> languages) {
        this.languages = new ArrayList<>(languages);
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.LAYER;
    }
}

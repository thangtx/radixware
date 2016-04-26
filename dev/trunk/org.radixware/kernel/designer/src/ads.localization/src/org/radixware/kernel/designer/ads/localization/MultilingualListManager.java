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

package org.radixware.kernel.designer.ads.localization;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.dds.DdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.ads.localization.prompt.PhrasesAndGuessesPanel;
import org.radixware.kernel.designer.ads.localization.prompt.Prompt;
import org.radixware.kernel.designer.common.dialogs.build.ProgressHandleFactoryDelegate;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public class MultilingualListManager {
    final MultilingualEditor editor;
    private List<EIsoLanguage> sourceLangs;
    private List<EIsoLanguage> translLangs;
    
    private Map<IMultilingualStringDef, RowString> editMlStrings;
    private List<Definition> selectedDefs;
    final MultilingualEditorProgressHandle progressHandle;
    

    public MultilingualListManager(MultilingualEditor editor) {
        editMlStrings=new HashMap<>();
        this.editor = editor;
        progressHandle = new MultilingualEditorProgressHandle(editor);
    }

    public void  updateMlStrings(List<EIsoLanguage> sourceLangs,List<EIsoLanguage> translLangs,final Map<Layer,List<Module>>  selectedLayers, final Cancellable cancellable) {
        clearStrings();
        this.sourceLangs=sourceLangs;
        this.translLangs=translLangs;
        
        new Thread(new Runnable() {
                @Override
                public void run() {
                        synchronized(MultilingualListManager.this){
                             try {
                                progressHandle.startProgress(cancellable);
                                if (showSelectedDefs()) {
                                    getMlStringsFromSelectedDef(selectedLayers, cancellable);
                                } else {
                                    getMlStrings(selectedLayers, cancellable);
                                }
                            } finally {
                                progressHandle.finishProgress();
                            }
                        }
                }
            }).start();
        
    }

    public boolean isInProcess(){
        return progressHandle.isInProcess();
    }
    
    public void getMlStrings(Map<Layer,List<Module>>  selectedLayers, final Cancellable cancellable) {
            editor.clearPromptList();
            if(selectedLayers == null){
                Map<Layer,List<Module>> allLayers = new HashMap<>();
                Collection<Branch> openBranches = RadixFileUtil.getOpenedBranches();
                for(Branch branch : openBranches){             
                    for(Layer layer:branch.getLayers().list())
                        allLayers.put(layer, null);
                    
                    if (cancellable.wasCancelled()){
                        return;
                    }
                    findInLayers(allLayers, cancellable); 
                }
            }else{
                if (cancellable.wasCancelled()){
                    return;
                }
                findInLayers(selectedLayers, cancellable);
            }
    }

    @SuppressWarnings("unchecked")
    private void findInLayers(final Map<Layer,List<Module>> layers, final Cancellable cancellable) {
        for (final Layer layer : layers.keySet()) {
            if ((layer.isInBranch() && !layer.isLocalizing())/*&&(isLayerHasLangs(layer, sourceLangs))&&(isLayerHasLangs(layer, translLangs))*/) {
                final List<Module> selectedModules = layers.get(layer);
                List<Module> modules = new ArrayList<>(layer.getAds().getModules().list());
                modules.addAll(layer.getDds().getModules().list());
                
                if (cancellable.wasCancelled()) {
                    return;
                }
                
                getStringsFromAdsAndDds(modules, selectedModules, layer, cancellable);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void getStringsFromAdsAndDds(final List<Module> modules,List<Module> selectedModules,final Layer layer, final Cancellable cancellable){
        for(Module m:modules){ 
            if(selectedModules==null || selectedModules.isEmpty() || selectedModules.contains(m))
               m.visitChildren(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        ILocalizingBundleDef bundle=((ILocalizingBundleDef)radixObject);
                        editor.firePropertyChange(MultilingualEditor.LOADING_STRINGS, new Object(), addString(bundle, layer, cancellable));
                    }
               }, new VisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        if (cancellable.wasCancelled()) {
                            return false;
                        }
                        return radixObject instanceof AdsLocalizingBundleDef || radixObject instanceof DdsLocalizingBundleDef;
                    }
               });
          }
    }

    public void getMlStringsFromSelectedDef(final Map<Layer, List<Module>> selectedLayers,final Cancellable cancellable) {
        for (Definition def : selectedDefs) {
            ILocalizingBundleDef<? extends IMultilingualStringDef> mlBundle = def.findExistingLocalizingBundle();
            if (mlBundle != null) {
                Layer layer = def.getModule().getSegment().getLayer();
                if (selectedLayers == null || selectedLayers.keySet().contains(layer)) {
                    if (cancellable.wasCancelled()){
                        return;       
                    }
                    editor.firePropertyChange(MultilingualEditor.LOADING_STRINGS, new Object(), addString(mlBundle, layer, cancellable));
                }
            }
        }
    }
    
    private List<RowString> addString(final ILocalizingBundleDef bundle, final Layer layer, final Cancellable cancellable){
        ArrayList<RowString> addStirng = new ArrayList<>();
            Definitions<? extends IMultilingualStringDef> list = bundle.getStrings().getLocal();
            for (IMultilingualStringDef mlstring : list) {
                boolean isVersionChanged = false;
                if (layer.isLocalizing()) {
                    IMultilingualStringDef ovwrwr = (IMultilingualStringDef) mlstring.findOverwritten().get();
                    if (ovwrwr != null) {
                        isVersionChanged = ovwrwr.getVersion() != mlstring.getVersion();
                    }
                }
                RowString row = new RowString(mlstring, isVersionChanged);
                if (row.isRowUsed()) {
                    if (!showSelectedDefs()){
                        for (EIsoLanguage lang : sourceLangs) {
                            if (row.hasCheckedTranslation(translLangs) && (!row.needsCheck(lang)) && (!row.isEmpty(sourceLangs, translLangs))) {
                                editor.createPrompt(mlstring);
                            }
                        }
                    }
                    if (editMlStrings.get(mlstring) != null) {
                        row.setWasEdit(true);
                    }
                    
                    if (cancellable.wasCancelled()){
                        break;       
                    }
                    
                    addStirng.add(row);
            }
        }
        return addStirng;
    }
    
    private void clearStrings(){
        synchronized(this){
            editor.firePropertyChange(MultilingualEditor.CLEAR_STRINGS, false, true);
        }
    }

     public void addEditedStringList(RowString s) {
        for (IMultilingualStringDef string : s.getMlStrings()){
            editMlStrings.put(string, s);
        }
     }

    public boolean showSelectedDefs() {
        return selectedDefs != null && !selectedDefs.isEmpty();
    }

     public void setSelectedDefs(final List<Definition> defs) {
        if(selectedDefs==null){
            selectedDefs=new ArrayList<>();
        }else{
            selectedDefs.clear();
        }
        
        if (defs != null){
            selectedDefs.addAll(defs);
        }
     }
 
    public void saveStings(){
        for (RowString str : editMlStrings.values()){
            if(str.getWasEdit()){
                try {
                    str.save();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        editMlStrings.clear();
    }
    
}

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

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;


public class RowString {

    private boolean isVersionChanged = false;
    private List<IMultilingualStringDef> mlStrings;
    private RadixObject contextDef;
    private String strContext = null;
    private ILocalizedDef.MultilingualStringInfo mlStringInfo;
    private boolean wasEdit = false;
    private final static int iconSize = 16;
    private final Icon needTranslateIcon = RadixWareIcons.MLSTRING_EDITOR.TRANSLATION_NOT_CHECKED.getIcon(iconSize, iconSize);
    private final Icon translateIcon = RadixWareIcons.DIALOG.OK.getIcon(iconSize, iconSize);
    private final Icon needTranslateDisabledIcon = RadixWareIcons.MLSTRING_EDITOR.TRANSLATION_NOT_CHECKED_DISABLED.getIcon(iconSize, iconSize);
    private final Icon translateDisabledIcon = RadixWareIcons.MLSTRING_EDITOR.TRANSLAT_DISABLED.getIcon(iconSize, iconSize);
    private final Icon sourceNotCheckedIcon = RadixWareIcons.MLSTRING_EDITOR.UNCHECKED_SOURCE.getIcon(iconSize, iconSize);

    public RowString(IMultilingualStringDef mlString, boolean isVersionChanged) {
        this(mlString);
        this.isVersionChanged = isVersionChanged;
    }

    public RowString(IMultilingualStringDef mlString) {
        this.mlStrings = new ArrayList<>();
        this.mlStrings.add(mlString);
    }

    public RadixObject getContextDef() {
        return contextDef;
    }

    public boolean isVersionChanged() {
        return isVersionChanged;
    }
    
    public boolean isRowUsed(){
        Definition definition = getTopLevelDef();
        if (definition != null && mlStringInfo == null) {
            definition.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof ILocalizedDef) {
                            ILocalizedDef ld = (ILocalizedDef) radixObject;
                            final ArrayList<ILocalizedDef.MultilingualStringInfo> infos = new ArrayList<>();
                            ld.collectUsedMlStringIds(infos);
                            for (ILocalizedDef.MultilingualStringInfo info : infos) {
                                if ((info != null) && (info.getId() != null) && (info.getId().equals(mlStrings.get(0).getId()))) {
                                    contextDef = radixObject;
                                    mlStringInfo = info;
                                }
                            }
                        }
                    }
                }, new VisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return radixObject instanceof ILocalizedDef;
                    }
                });
        }
        return mlStringInfo != null;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private Definition getTopLevelDef() {
        Definition topLevelDef;
        IMultilingualStringDef mlString = mlStrings.get(0);
        ILocalizingBundleDef ownerBundle = mlString.getOwnerBundle();
        topLevelDef = ownerBundle.findBundleOwner();
        if (topLevelDef == null) {
            SearchResult<Definition> searchResult = ownerBundle.findOverwrittenFor((Definition)ownerBundle);
            if (!searchResult.isEmpty() && searchResult.get() instanceof ILocalizingBundleDef) {
                topLevelDef = ((ILocalizingBundleDef) searchResult.get()).findBundleOwner();
            }
        }
        return topLevelDef;
    }

    @SuppressWarnings("rawtypes")
    public String getStrContext() {
        if (strContext == null) {
            Definition topLevelDef;
            if (contextDef == null) {
                topLevelDef = getTopLevelDef();
            } else {
                IMultilingualStringDef mlString = mlStrings.get(0);
                ILocalizingBundleDef ownerBundle = mlString.getOwnerBundle();
                topLevelDef = ownerBundle.findBundleOwner();
            }
            return topLevelDef == null ? "" : createContext(topLevelDef);
        } else {
            return strContext;
        }
    }

    public boolean isContextPublished() {
        if (isRowUsed()) {
            return mlStringInfo.isPublished();
        }
        return false;
    }

    public EMultilingualStringKind getMultilingualStringKind() {
        if (isRowUsed()) {
            return mlStringInfo.getKind();
        }
        return null;
    }
    

    private String createContext(Definition topLevelDef) {
        final StringBuilder sb = new StringBuilder();
        
        if (contextDef == null) {
            contextDef = topLevelDef;
        }
        String link = "<a href=\"1\" title=\"Click for going to context\">";
        sb.append("<html><b>");
        if (mlStringInfo != null) {
            sb.append(mlStringInfo.getContextDescription());
        } else {
            sb.append(contextDef.getTypeTitle());
        }
        String name = contextDef.getName();
        
        if (!name.isEmpty()){
            sb.append(": ");
            sb.append(' ');
            sb.append(link);
            sb.append(name);
            sb.append("</a>");
        }
        
        sb.append("</b>");
        //if (def.isInBranch()) { // otherwise, appendAdditionalToolTip can throw NullPointerException.
        //    def.appendAdditionalToolTip(sb);
        //}
        String description = contextDef.getDescription();
        final RadixObject ownerForQualifiedName = contextDef.getOwnerForQualifedName();
        if (ownerForQualifiedName != null) {
            sb.append("<br><i>Location: </i>");
            if (name.isEmpty()) {
                sb.append(link);
                sb.append(ownerForQualifiedName.getQualifiedName());
                sb.append("</a>");
            } else {
                sb.append(ownerForQualifiedName.getQualifiedName());
            }
        }

        if ((topLevelDef != null) && (!topLevelDef.equals(contextDef))) {
            sb.append("<br><i>Owner: </i>");
            sb.append(topLevelDef.getTypeTitle());
            sb.append(" ");
            sb.append(topLevelDef.getName());

            description = topLevelDef.getDescription();
            if (description != null && !description.isEmpty()) {
                sb.append("<br><i>Owner description: </i>");
                sb.append(description);
            }
        }
        
        sb.append("</html>");
        return sb.toString();
    }

    public String getComment() {
        return "";
    }

    public boolean isNeedTranslate(EIsoLanguage lang) {
        if (contextDef == null){
             if ((contextDef = getTopLevelDef()) == null) return false;
        }
        
        Layer layer= contextDef.getLayer();
        if (layer == null){
            return false;
        }
        
        if (layer.getLanguages().contains(lang)){
            return true;
        }
        
        for(Layer l:layer.getBranch().getLayers()){
            if(l.isLocalizing() && 
                    l.getBaseLayerURIs().contains(layer.getURI()) 
                    && l.getLanguages().contains(lang)){
                return true;
            }
        }
        
        return false;
    }

    public boolean needsCheck(List<EIsoLanguage> langs) {
        for (EIsoLanguage lang : langs) {
            if (needsCheck(lang)) {
                return true;
            }
        }
        return false;
    }

    public boolean needsCheck(EIsoLanguage lang) {
        for (IMultilingualStringDef mlString : mlStrings) {
            if (mlString.getLanguages().contains(lang)) {
                return mlString.getNeedsCheck(lang);
            }
        }
        return true;
    }

    /* public String getValue(EIsoLanguage lang) {
     for(AdsMultilingualStringDef mlString:mlStrings){
     if(mlString.getLayer().getLanguages().contains(lang))
     return mlString.getValue(lang);
     //if(val!=null){
     //    return val;
     //}
     }    
     AdsMultilingualStringDef mlString=mlStrings.get(0);
     if(mlString.getLayer().isLocalizing()){
     AdsMultilingualStringDef overwrittenMlString=mlString.getHierarchy().findOverwritten().get();
     if(overwrittenMlString!=null){
     return overwrittenMlString.getValue(lang);
     }
     }else{
     for(Layer layer: mlString.getBranch().getLayers()){
     if(layer.isLocalizing() && !layer.getBaseLayerURIs().isEmpty() &&
     layer.getBaseLayerURIs().contains(mlString.getLayer().getURI()) && layer.getLanguages().contains(lang)){
     AdsModule module=mlStrings.get(0).getModule();
     AdsModule localizingModule= (AdsModule)layer.getAds().getModules().findById(module.getId());
     if(localizingModule==null){
     localizingModule=AdsModule.Factory.getDefault().overwrite(module);
     localizingModule.setName(module.getName());
     for(AdsDefinition def:localizingModule.getDefinitions()){
     def.setUploadMode(AdsDefinition.ESaveMode.API);
     }
     layer.getAds().getModules().add(localizingModule);
     }

     try {
     AdsDefinition def=module.getDefinitions().findById(mlString.getOwnerBundle().getBundleOwnerId());
     AdsLocalizingBundleDef newMlbundle=localizingModule.getDefinitions().findLocalizingBundleDef(def,true); 
     if(newMlbundle!=null){
     newMlbundle.save();
     AdsMultilingualStringDef string=newMlbundle.getStrings().findById(mlString.getId(), ExtendableDefinitions.EScope.LOCAL).get();
     mlStrings.add(string);
     return string.getValue(lang);
     }
     }catch (IOException ex) {
     Exceptions.printStackTrace(ex);
     }                  
     }
     } 
     }
     return null;
     }*/
    public String getValue(EIsoLanguage lang) {
        for (IMultilingualStringDef mlString : mlStrings) {
            if (mlString.getLanguages().contains(lang)) {
                return mlString.getValue(lang);
            }
            //if(val!=null){
            //    return val;
            //}
        }
        return "";
        /*AdsMultilingualStringDef mlString=mlStrings.get(0);
         if(mlString.getLayer().isLocalizing()){
         AdsMultilingualStringDef overwrittenMlString=mlString.getHierarchy().findOverwritten().get();
         if(overwrittenMlString!=null){
         return overwrittenMlString.getValue(lang);
         }
         }else{
         for(Layer layer: mlString.getBranch().getLayers()){
         if(layer.isLocalizing() && !layer.getBaseLayerURIs().isEmpty() &&
         layer.getBaseLayerURIs().contains(mlString.getLayer().getURI()) && layer.getLanguages().contains(lang)){
         AdsModule module=mlString.getModule();
         AdsModule localizingModule= (AdsModule)layer.getAds().getModules().findById(module.getId());
         if(localizingModule==null){
         localizingModule=AdsModule.Factory.getDefault().overwrite(module);
         localizingModule.setName(module.getName());
         //for(AdsDefinition def:localizingModule.getDefinitions()){
         //   def.setUploadMode(AdsDefinition.ESaveMode.API);
         //}                      
         layer.getAds().getModules().add(localizingModule);
         //localizingModule.getDefinitions().overwrite(mlString.getOwnerBundle());
         }
         try {                      
         //AdsDefinition def=module.getDefinitions().findById(mlString.getOwnerBundle().getBundleOwnerId());
         AdsDefinition newDef=localizingModule.getDefinitions().findById(mlString.getOwnerBundle().getBundleOwnerId());
         if(newDef==null){
         AdsDefinition def=module.getDefinitions().findById(mlString.getOwnerBundle().getBundleOwnerId());
         newDef=localizingModule.getDefinitions().overwrite(def);
         }
         AdsLocalizingBundleDef newMlbundle=localizingModule.getDefinitions().findLocalizingBundleDef(newDef,true);                       
         if(newMlbundle!=null){
         newMlbundle.save();
         AdsMultilingualStringDef string=newMlbundle.getStrings().findById(mlString.getId(), ExtendableDefinitions.EScope.LOCAL).get();
         if(string==null){
         string=newMlbundle.getStrings().overwrite(mlString);
         }
         //if(!mlStrings.contains(string)){
         mlStrings.add(string);
         String val=mlString.getValue(lang);
         if(val!=null)
         return string.getValue(lang);
         //}
         }
         }catch (IOException ex) {
         Exceptions.printStackTrace(ex);
         }                  
         }
         } 
         }
         return null;*/
    }

    public boolean setValue(EIsoLanguage lang, String val) {
        if (!mlStrings.isEmpty()) {
            return mlStrings.get(0).setValue(lang, val);
        }
        return false;
    }

    public void setNeedCheck(EIsoLanguage lang, boolean needChecked) {
        for (IMultilingualStringDef mlString : mlStrings) {
            if (mlString.getLanguages().contains(lang)) {
                mlString.setChecked(lang, needChecked);
                ((RadixObject)mlString).setEditState(EEditState.MODIFIED);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        }
    }

    public Icon getIcon(List<EIsoLanguage> sourceLangs, List<EIsoLanguage> translLangs) {
        if ((!isSrcLangsInTranslList(sourceLangs, translLangs)) && (!hasCheckedTranslation(sourceLangs))) {
            return sourceNotCheckedIcon;
        }
        if (needsCheck(translLangs)) {
            return needTranslateIcon;
        }
        return translateIcon;
    }

    public boolean isSrcLangsInTranslList(List<EIsoLanguage> sourceLangs, List<EIsoLanguage> translLangs) {
        for (EIsoLanguage lang : sourceLangs) {
            if (!translLangs.contains(lang)) {
                return false;
            }
        }
        return true;
    }

    public Icon getIcon(EIsoLanguage lang, List<EIsoLanguage> sourceLangs) {
        if (getCanChangeStatus(lang, sourceLangs)) {
            if (needsCheck(lang)) {
                return needTranslateIcon;
            }
            return translateIcon;
        } else {
            if (needsCheck(lang)) {
                return needTranslateDisabledIcon;
            }
            return translateDisabledIcon;
        }
    }

    public String getToolTip(List<EIsoLanguage> sourceLangs, List<EIsoLanguage> translLangs) {
        if ((!isSrcLangsInTranslList(sourceLangs, translLangs)) && (!hasCheckedTranslation(sourceLangs))) {
            return NbBundle.getMessage(RowString.class, "SRC_NOT_CHECKED");
        }
        if (needsCheck(translLangs)) {
            return NbBundle.getMessage(RowString.class, "TRANSLATION_NOT_CHAECKED");
        }
        return NbBundle.getMessage(RowString.class, "MLS_IS_CHECKED");
    }

    public String getToolTip(EIsoLanguage lang, List<EIsoLanguage> sourceLangs) {
        if (getCanChangeStatus(lang,sourceLangs)) {
            return NbBundle.getMessage(RowString.class, "CHANGE_STATUS");
        } else {
            return NbBundle.getMessage(RowString.class, "CANT_CHANGE_STATUS");
        }
    }

    public boolean getCanChangeStatus(EIsoLanguage lang, List<EIsoLanguage> sourceLangs) {
        if ((sourceLangs.contains(lang)) || (this.hasCheckedTranslation(sourceLangs))) {
            return true;
        }
        return false;
    }

    public boolean isSimple() {
        return mlStrings.get(0).getDefinitionType() != EDefType.MULTILINGUAL_EVENT_CODE;
    }

    public boolean isEventCode() {
        return mlStrings.get(0).getDefinitionType() == EDefType.MULTILINGUAL_EVENT_CODE;
    }

    public boolean isEmpty(List<EIsoLanguage> sourceLangs, List<EIsoLanguage> translLangs) {
        for (IMultilingualStringDef mlString : mlStrings) {
            for (EIsoLanguage lang : sourceLangs) {
                String value = mlString.getValue(lang);
                if (value == null || value.isEmpty()) {
                    return true;
                }
            }
            for (EIsoLanguage lang : translLangs) {
                String value = mlString.getValue(lang);
                if (value == null || value.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isStrInComment() {
        if (isRowUsed()){
            return mlStringInfo.isInComment();
        }
        return false;
    }

    public boolean hasCheckedTranslation(List<EIsoLanguage> langs) {
        for (EIsoLanguage lang : langs) {
            if (!needsCheck(lang)) {
                return true;
            }
        }
        return false;
    }

    public List<IMultilingualStringDef> getMlStrings() {
        return mlStrings;
    }

    public IMultilingualStringDef getMlString(EIsoLanguage lang) {
        for (IMultilingualStringDef mlString : mlStrings) {
            if (mlString.getLanguages().contains(lang)) {
                return mlString;
            }
        }
        return null;
    }

    public String getLastChangeAuthor(EIsoLanguage lang) {
        for (IMultilingualStringDef mlString : mlStrings) {
            if (mlString.getLanguages().contains(lang)) {
                return mlString.getLastChangeAuthor(lang);
            }
        }
        return null;
    }

    public Timestamp getLastChangeTime(EIsoLanguage lang) {
        for (IMultilingualStringDef mlString : mlStrings) {
            if (mlString.getLanguages().contains(lang)) {
                return mlString.getLastChangeTime(lang);
            }
        }
        return null;
    }
    
    public Timestamp getChangeStatusTime(EIsoLanguage lang) {
        for (IMultilingualStringDef mlString : mlStrings) {
            if (mlString.getLanguages().contains(lang)) {
                return mlString.getTimeChangeOfStatus(lang);
            }
        }
        
        return null;
    }

    public String getChangeStatusAuthor(EIsoLanguage lang) {
        for (IMultilingualStringDef mlString : mlStrings) {
            if (mlString.getLanguages().contains(lang)) {
                return mlString.getAuthorChangeOfStatus(lang);
            }
        }
        
        return null;
    }
    
    public boolean isStatusChanged(final List<EIsoLanguage> sourceLangs,final List<EIsoLanguage> translLangs){
        for (EIsoLanguage lang : sourceLangs) {
                String value = getChangeStatusAuthor(lang);
                if (value != null && !value.isEmpty()) {
                    return true;
                }
            }
        for (EIsoLanguage lang : translLangs) {
            String value = getChangeStatusAuthor(lang);
                if (value != null && !value.isEmpty()) {
                    return true;
            }
        }
        return false;
    }
    
    public String getAuthor() {
        return mlStrings.isEmpty() ? null : mlStrings.get(0).getAuthor();
    }

    public String getCreationDateStr() {
        long creationDateMs = mlStrings.get(0).getCreationDate();
        if (creationDateMs != -1) {
            Timestamp creationDate = new Timestamp(creationDateMs);
            DateFormat dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            String sTimeFrom = dateTimeFormat.format(creationDate);
            return mlStrings.isEmpty() ? "" : sTimeFrom;
        }
        return null;
    }

    public Timestamp getCreationDate() {
        long creationDateMs = mlStrings.get(0).getCreationDate();
        return new Timestamp(creationDateMs);
    }

    public void save() throws IOException {
        for (IMultilingualStringDef mlString : mlStrings) {
            if(mlString.getOwnerBundle() instanceof AdsLocalizingBundleDef)
                ((AdsLocalizingBundleDef)mlString.getOwnerBundle()).save();
        }
        setWasEdit(false);
    }

    public void setWasEdit(boolean wasEdit) {
        this.wasEdit = wasEdit;
    }

    public boolean getWasEdit() {
        return wasEdit;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RowString)) {
            return false;
        }
        if (((RowString) o).getMlStrings().size() == mlStrings.size()) {
            for (IMultilingualStringDef mlString : ((RowString) o).getMlStrings()) {
                if (!mlStrings.contains(mlString)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.mlStrings != null ? this.mlStrings.hashCode() : 0);
        return hash;
    }
}

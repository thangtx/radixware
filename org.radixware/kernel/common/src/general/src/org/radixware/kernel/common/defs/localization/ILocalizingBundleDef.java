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

package org.radixware.kernel.common.defs.localization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;


public interface ILocalizingBundleDef<T extends Definition & IMultilingualStringDef> {
    public static AtomicLong version = new AtomicLong(0);
    public static Authors authors = new Authors();
    
    public class Authors {
        private final SortedSet<String> authors = new TreeSet<>();
        
        public SortedSet<String> getAuthors() {
            return Collections.unmodifiableSortedSet(authors);
        }
        
        public void addAuthor(String name){
            if (name != null) {
                authors.add(name.toLowerCase());
            }
        }
        
    }

    public static abstract class Strings<T extends Definition & IMultilingualStringDef> extends ExtendableDefinitions<T> {

        public Strings(Definition owner) {
            super(owner);
        }

        private ILocalizingBundleDef<T> getOwnerBundle() {
            return (ILocalizingBundleDef<T>) getContainer();
        }
        
        protected void appendTo(LocalizingBundleDefinition xDef, boolean apiMode, EIsoLanguage lang) {
            if (!getLocal().isEmpty()) {
                List<IMultilingualStringDef> listToSave = getUsedStrings(EScope.LOCAL, apiMode);
                Collections.sort(listToSave, new Comparator<IMultilingualStringDef>() {
                    @Override
                    public int compare(IMultilingualStringDef o1, IMultilingualStringDef o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                });
                for (IMultilingualStringDef string : listToSave) {
                    string.appendTo(xDef.addNewString(), lang);
                }
            }
        }
        
        protected java.util.Set<Id> getUsedStringsIds(EScope scope, boolean apiMode){
            java.util.Set<Id> usedMlStringIds = null;
            Definition def = getOwnerBundle().findBundleOwner();
            while (def != null) {
                final Collection<ILocalizedDef.MultilingualStringInfo> stringInfos = new ArrayList<>();
                def.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        ILocalizedDef def = (ILocalizedDef) radixObject;
                        def.collectUsedMlStringIds(stringInfos);
                    }
                }, new VisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return radixObject instanceof ILocalizedDef;
                    }
                });
                if (!stringInfos.isEmpty()) {
                    if (usedMlStringIds == null) {
                        usedMlStringIds = new HashSet<>();
                    }
                    if (apiMode) {
                        for (ILocalizedDef.MultilingualStringInfo info : stringInfos) {
                            EAccess access = info.getAccess();
                            if (info.isPublished() || (access != EAccess.DEFAULT && access != EAccess.PRIVATE)) {
                                usedMlStringIds.add(info.getId());
                            }
                        }
                    } else {
                        for (ILocalizedDef.MultilingualStringInfo info : stringInfos) {
                            usedMlStringIds.add(info.getId());
                        }
                    }
                }
                if (scope == EScope.LOCAL) {
                    break;
                }
                def = (Definition) getOwnerBundle().findOverwrittenFor(def).get();
            }
            
            if (usedMlStringIds == null || usedMlStringIds.isEmpty()){
                usedMlStringIds = null;
            }
            
            return usedMlStringIds;
        }

        protected List<IMultilingualStringDef> getUsedStrings(EScope scope, boolean apiMode) {

            boolean saveAll = false;

            Branch branch = getBranch();
            Layer layer = getLayer();
            if (branch == null || layer == null) {
                saveAll = true;
            } else if (layer.isReadOnly()) {
                for (Layer l : branch.getLayers()) {
                    if (!l.isReadOnly() && l.isLocalizing() && l.getBaseLayerURIs().contains(layer.getURI())) {
                        saveAll = true;
                    }
                }
            }
            java.util.Set<Id> usedMlStringIds = getUsedStringsIds(scope, apiMode);
            
            List<IMultilingualStringDef> listToSave = new LinkedList<>();
            if (usedMlStringIds != null){
                for (IMultilingualStringDef string : get(scope)) {
                    if (!saveAll && !usedMlStringIds.contains(string.getId())) {//ignore unused strings
                        Module m = string.getModule();
                        if ((m == null || m == getModule()) && string.findOverwritten().get() == null) {//does not ignore strings from overwritten modules
                            continue;
                        }
                    }
                    listToSave.add(string);
                }
            }  else {
                if (saveAll){
                    for (IMultilingualStringDef string : get(scope)) {
                        listToSave.add(string);
                    }
                }
            }
            return listToSave;
        }

        public void loadFrom(LocalizingBundleDefinition xDef) {
            for (final LocalizedString s : xDef.getStringList()) {
                final IMultilingualStringDef multilingualString = loadString(s);
                if (multilingualString != null) {
                    IMultilingualStringDef m = getLocal().findById(multilingualString.getId());
                    if (m != null) {
                        if (!multilingualString.getLanguages().isEmpty()) {
                            EIsoLanguage language = multilingualString.getLanguages().iterator().next();
                            m.loadValue(multilingualString, language);
                        }
                    } else {
                        getLocal().add((T) multilingualString);
                    }
                }
            }
        }

        protected abstract IMultilingualStringDef loadString(LocalizedString xDef);

        public List<EIsoLanguage> getLanguages() {
            EnumSet<EIsoLanguage> res = EnumSet.noneOf(EIsoLanguage.class);
            for (IMultilingualStringDef s : getAll(EScope.LOCAL)) {
                res.addAll(s.getLanguages());
            }
            return new ArrayList<>(res);

        }
        
        public void updateStringsByLanguages(){
            Layer layer = getLayer();
            if (layer != null){
                Set<EIsoLanguage> languages = new HashSet(layer.getLanguages());
                Branch branch = layer.getBranch();
                
                if (branch == null) {
                    return;
                }
                
                for (Layer l : branch.getLayers()){
                    if (l.isLocalizing() && l.getBaseLayerURIs().contains(layer.getURI())) {
                        languages.addAll(l.getLanguages());
                    }
                }
                for (IMultilingualStringDef s : getAll(EScope.LOCAL)) {
                    s.updateLanguages(languages);
                } 
            }
        }
    }

    Strings<T> getStrings();

    Definition findBundleOwner();

    SearchResult findOverwrittenFor(Definition def);

    public IMultilingualStringDef createString(ELocalizedStringKind kind);
    
    public Id getId();
    
    public List<EIsoLanguage> getLanguages();
}

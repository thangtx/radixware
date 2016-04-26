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

package org.radixware.kernel.common.client.types;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public final class ExplorerRoot {

    final static private String SETTING_NAME = "explorerRoots";
    private static ExplorerRoot def;
    private final Id id;
    private final String title;
    private final String description;
    private final Id iconId;
    private final IClientEnvironment environment;
    private List<Id> visibleExplorerItems;
    private long visibleExplorerItemsVersion = -1;

    private ExplorerRoot(final IClientEnvironment environment, final Id id, final String title, final String desc, final Id iconId) {
        if (id == null) {
            throw new NullPointerException();
        }
        this.id = id;
        this.title = title != null && !title.isEmpty() ? title : id.toString();
        this.description = desc;
        this.iconId = iconId;
        this.environment = environment;
    }
    
    private ExplorerRoot(final IClientEnvironment environment, final Id id, final String title, final String desc){
        this(environment, id,title,desc,null);
    }
    
    private ExplorerRoot(final IClientEnvironment environment, final Id id, final String title, final Id iconId){
        this(environment, id,title,null,iconId);
    }

    @Override
    public boolean equals(final Object other) {
        return (other instanceof ExplorerRoot)
                && ((ExplorerRoot) other).id.equals(id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    public Id getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    
    public Id getIconId(){
        return iconId;
    }
    
    public List<Id> getVisibleExplorerItems() throws ServiceClientException, InterruptedException{
        if (visibleExplorerItems==null || 
            visibleExplorerItemsVersion!=environment.getDefManager().getAdsVersion().getNumber()){
            visibleExplorerItems = environment.getEasSession().listVisibleExplorerItems(id);
            visibleExplorerItemsVersion=environment.getDefManager().getAdsVersion().getNumber();
        }
        return visibleExplorerItems;        
    }
    
    public RadParagraphDef getParagraphDef(){
        return environment.getDefManager().getParagraphDef(id);
    }

    public static ExplorerRoot getDefault(final IClientEnvironment environment) {
        if (def == null && RunParams.getExplorerRootId() != null) {
            def = new ExplorerRoot(environment, RunParams.getExplorerRootId(), "", "");
        }
        return def;
    }

    public static List<ExplorerRoot> loadFromResponse(final org.radixware.schemas.eas.CreateSessionRs.ExplorerRoots explorerRoots, final IClientEnvironment environment) {
        final List<ExplorerRoot> roots = new LinkedList<>();
        if (explorerRoots != null && explorerRoots.getItemList() != null) {
            for (org.radixware.schemas.eas.CreateSessionRs.ExplorerRoots.Item xRoot : explorerRoots.getItemList()) {
                if (xRoot.getId() != null && !xRoot.getHidden()) {
                    try{
                        environment.getDefManager().getRepository().getMetaClassNameByDefId(xRoot.getId());
                    }
                    catch(DefinitionError error){
                        final String message = 
                            environment.getMessageProvider().translate("TraceMessage" , "Explorer root '%1s' (#%2s) was not found - ignoring");
                        environment.getTracer().debug(String.format(message,xRoot.getTitle(), xRoot.getId()));
                        continue;
                    }
                    final ExplorerRoot root = 
                        new ExplorerRoot(environment, xRoot.getId(), xRoot.getTitle(), xRoot.getDescription());
                    if (xRoot.isSetVisibleExplorerItems() && xRoot.getVisibleExplorerItems().getItemList()!=null){
                        root.visibleExplorerItems = new ArrayList<>();
                        root.visibleExplorerItemsVersion = environment.getDefManager().getAdsVersion().getNumber();
                        for (org.radixware.schemas.eas.Definition ei: xRoot.getVisibleExplorerItems().getItemList()){
                            root.visibleExplorerItems.add(ei.getId());
                        }
                    }
                    roots.add(root);
                }
            }
        }
        return roots;
    }
    
    public static List<ExplorerRoot> loadFromDefManager(final IClientEnvironment environment){
        final List<ExplorerRoot> roots = new LinkedList<>();        
        for (RadParagraphDef paragraphDef: environment.getDefManager().getExplorerRoots()){
            if (!paragraphDef.isHidden()){
                roots.add(new ExplorerRoot(environment, paragraphDef.getId(), paragraphDef.getTitle(), paragraphDef.getIconId()));
            }
        }
        return roots;
    }

    public static List<ExplorerRoot> loadFromConfig(final ClientSettings settings, final IClientEnvironment environment) {
        final List<ExplorerRoot> roots = new LinkedList<>();
        final int count = settings.beginReadArray(ExplorerRoot.SETTING_NAME);
        try {
            Id rootId;
            for (int j = 0; j < count; j++) {
                settings.setArrayIndex(j);
                rootId = Id.Factory.loadFrom(settings.readString("id", null));
                if (rootId != null) {
                    roots.add(new ExplorerRoot(environment, rootId,
                            (String) settings.value("title", null),
                            (String) settings.value("describtion", null)));
                }
            }
        } finally {
            settings.endArray();
        }
        return roots;
    }

    public static void storeToConfig(final ClientSettings settings, final List<ExplorerRoot> roots) {
        settings.beginWriteArray(SETTING_NAME);
        try {
            for (int i = 0; i < roots.size(); ++i) {
                settings.setArrayIndex(i);
                settings.writeString("id", roots.get(i).id.toString());
                if (roots.get(i).title != null) {
                    settings.writeString("title", roots.get(i).title);
                }
                if (roots.get(i).description != null) {
                    settings.writeString("title", roots.get(i).description);
                }
            }
        } finally {
            settings.endArray();
        }
    }
}

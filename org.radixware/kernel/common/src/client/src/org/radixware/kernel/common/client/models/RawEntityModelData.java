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

package org.radixware.kernel.common.client.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.Actions;
import org.radixware.schemas.eas.ExplorerItemList;
import org.radixware.schemas.eas.Object;


public final class RawEntityModelData {
    
    public static final class ServerRestrictions{
        
        private static final ServerRestrictions EMPTY = new ServerRestrictions();
        
        private EnumSet<ERestriction> disabledActions;
        private List<Id> disabledCommands;
        
        private ServerRestrictions(List<Actions.Item> xml){
            disabledActions = EnumSet.noneOf(ERestriction.class);
            disabledCommands = new ArrayList<>();
            for (org.radixware.schemas.eas.Actions.Item item : xml) {
                if (item.getType() == org.radixware.schemas.eas.ActionTypeEnum.DELETE) {
                    disabledActions.add(ERestriction.DELETE);
                } else if (item.getType() == org.radixware.schemas.eas.ActionTypeEnum.UPDATE) {
                    disabledActions.add(ERestriction.UPDATE);
                } else if (item.getType() == org.radixware.schemas.eas.ActionTypeEnum.VIEW){
                    disabledActions.add(ERestriction.VIEW);
                }
                else if (item.getType() == org.radixware.schemas.eas.ActionTypeEnum.COMMAND) {
                    disabledCommands.add(item.getId());
                }
            }
        }
        
        private ServerRestrictions(){
            disabledActions = null;
            disabledCommands = null;
        }

        public EnumSet<ERestriction> getDisabledActions() {
            if (disabledActions==null){
                return EnumSet.noneOf(ERestriction.class);
            }else{
                return EnumSet.copyOf(disabledActions);
            }
        }

        public List<Id> getDisabledCommands() {
            if (disabledCommands==null){
                return Collections.emptyList();
            }else{
                return Collections.unmodifiableList(disabledCommands);
            }
        }
       
        public boolean isEmpty(){
            return disabledActions==null && disabledCommands==null;
        }
        
        static ServerRestrictions parse(List<Actions.Item> xml){
            return xml==null ? EMPTY : new ServerRestrictions(xml);
        }
    }
    
    public static final class EnabledEditorPages{  
        
        private final static EnabledEditorPages ALL_PAGES = new EnabledEditorPages();
        
        private final boolean allPages;
        private final Collection<Id> enabledPages;
        
        private EnabledEditorPages(final Collection<Id> pages){
            allPages = false;
            enabledPages = pages;
        }
        
        private EnabledEditorPages(){
            allPages = true;
            enabledPages = null;
        }

        public boolean isAllPagesEnabled() {
            return allPages;
        }

        public Collection<Id> getEnabledPages() {
            if (enabledPages==null){
                return Collections.emptyList();
            }else{
                return Collections.unmodifiableCollection(enabledPages);
            }
        }        
        
        static EnabledEditorPages parse(org.radixware.schemas.eas.EditorPages xml){
            if (xml==null){
                return null;
            }else if (xml.getAll()){
                return ALL_PAGES;
            }else{
                final Collection<Id> pages;
                if (xml.getItemList()!=null && !xml.getItemList().isEmpty()){
                    pages = new LinkedList<>();
                    for (org.radixware.schemas.eas.EditorPages.Item item: xml.getItemList()){
                        pages.add(item.getId());
                    }                    
                }else{
                    pages = null;
                }
                return new EnabledEditorPages(pages);
            }
            
        }
    }
    
    public static final class ChildExplorerItemInfo {

        private final Id id;
        private final Id editorPresentationId;
        private final Id classId;
        private final String objectPidAsStr;
        private final String objectTitle;    

        private ChildExplorerItemInfo(ExplorerItemList.Item xml){
            id = xml.getId();
            if (xml.isSetObject() && xml.getObject().getPID()!=null){
                classId = xml.getObject().getPresentation().getClassId();
                editorPresentationId = xml.getObject().getPresentation().getId();
                objectPidAsStr = xml.getObject().getPID();
                objectTitle = xml.getObject().getTitle();
            }else{
                editorPresentationId = null;
                classId = null;
                objectPidAsStr = null;
                objectTitle = null;
            }
        }

        public Id getId() {
            return id;
        }

        public Id getEditorPresentationId() {
            return editorPresentationId;
        }

        public Id getClassId() {
            return classId;
        }

        public String getParentObjectPidAsStr() {
            return objectPidAsStr;
        }

        public String getParentObjectTitle() {
            return objectTitle;
        }

        public boolean isParentObject(){
            return objectPidAsStr!=null;
        }

        public static Collection<ChildExplorerItemInfo> parse(ExplorerItemList xml){
            if (xml==null || xml.getItemList()==null){
                return null;
            }else{
                final List<ChildExplorerItemInfo> result = new LinkedList<>();
                for (ExplorerItemList.Item item: xml.getItemList()){
                    result.add(new ChildExplorerItemInfo(item));
                }
                return result;
            }            
        }
    }
    
    private final org.radixware.schemas.eas.Object data;
    private final ServerRestrictions restrictions;
    private final EnabledEditorPages editorPages;
    private final ESelectorRowStyle rowStyle;
    private final Collection<ChildExplorerItemInfo> childExplorerItems;
    
    public RawEntityModelData(final org.radixware.schemas.eas.Object object,
                              final List<org.radixware.schemas.eas.Actions.Item> serverRestrictions,
                              final org.radixware.schemas.eas.EditorPages enabledEditorPages,
                              final ExplorerItemList accessibleExplorerItemList,
                              final ESelectorRowStyle selectorRowStyle){
        data = object==null ? null : (org.radixware.schemas.eas.PresentableObject)object.copy();
        restrictions = serverRestrictions==null ? ServerRestrictions.EMPTY : new ServerRestrictions(serverRestrictions);
        editorPages = EnabledEditorPages.parse(enabledEditorPages);
        childExplorerItems = ChildExplorerItemInfo.parse(accessibleExplorerItemList);
        rowStyle = selectorRowStyle;
    }
    
    public RawEntityModelData(final org.radixware.schemas.eas.Object object,
                              final List<org.radixware.schemas.eas.Actions.Item> serverRestrictions){
        data = object==null ? null : (org.radixware.schemas.eas.PresentableObject)object.copy();
        restrictions = serverRestrictions==null ? ServerRestrictions.EMPTY : new ServerRestrictions(serverRestrictions);
        editorPages = null;
        childExplorerItems = null;
        rowStyle = object.getRowStyle();
    }
    
    public RawEntityModelData(final org.radixware.schemas.eas.ReadRs readRs){
        this(readRs.getData());
    }
    
    public RawEntityModelData(final org.radixware.schemas.eas.PresentableObject xmlObject){
        data = (org.radixware.schemas.eas.PresentableObject)xmlObject.copy();
        if (xmlObject.getDisabledActions()==null || xmlObject.getDisabledActions().getItemList()==null){
            restrictions = ServerRestrictions.EMPTY;
        }else{
            restrictions = new ServerRestrictions(xmlObject.getDisabledActions().getItemList());
        }        
        editorPages = EnabledEditorPages.parse(xmlObject.getEnabledEditorPages());
        childExplorerItems = ChildExplorerItemInfo.parse(xmlObject.getAccessibleExplorerItems());
        rowStyle = xmlObject.getRowStyle();        
    }

    public Object getEntityObjectData() {
        return data==null ? null : (org.radixware.schemas.eas.Object)data.copy();
    }

    public ServerRestrictions getRestrictions() {
        return restrictions;
    }

    public EnabledEditorPages getEnabledEditorPages() {
        return editorPages;
    }
    
    public ESelectorRowStyle getSelectorRowStyle() {
        return rowStyle;
    }    
    
    public Collection<ChildExplorerItemInfo> getAccessibleChildExplorerItems(){
        if (childExplorerItems==null){
            return null;
        }else{
            return Collections.unmodifiableCollection(childExplorerItems);
        }
    }
}

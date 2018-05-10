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

package org.radixware.kernel.server.arte.services.eas;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.DefManager;
import org.radixware.kernel.server.arte.Trace;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.types.EntityGroup;


public final class InstantiatableClassesCollector {
    
    private InstantiatableClassesCollector(){        
    }
    
    static void collectInstantiatableClasses(final RadClassPresentationDef classPresDef, 
                                             final EntityGroup entGrp,
                                             final SessionRequest.PresentationOptions presOptions,
                                             org.radixware.schemas.eas.InstantiatableClasses classesXml){
        final DefManager defManager = entGrp.getArte().getDefManager();
        final List<EntityGroup.ClassCatalogItem> classes = new LinkedList<>();
        final Id classCatalogId = presOptions.getClassCatalogId();
        final IRadRefPropertyDef contextProperty = presOptions.context == null ? null : presOptions.context.getContextRefProperty();
        final List<Id> roleIds = presOptions.getContextCurUserApplicableRoleIds();
        if (classCatalogId != null) {
            Class<?> valClass = null;
            final RadClassPresentationDef.ClassCatalog classCatalog;
            if (contextProperty != null) {
                valClass = defManager.getClass(contextProperty.getDestinationClassId());
                classCatalog = defManager.getClassDef(contextProperty.getDestinationClassId()).getPresentation().getClassCatalogById(classCatalogId);
            } else {
                classCatalog = classPresDef.getClassCatalogById(classCatalogId);
            }
            final List<Id> creationPresentationIds = presOptions.context.getCreationEditorPresentationIds();
            for (RadClassPresentationDef.ClassCatalog.ItemPresentation item : classCatalog.getPresentation()) {
                if (item.getClassId() == null) {
                    classes.add(new EntityGroup.ClassCatalogItem(item.getLevel(), item.getClassId(), item.getTitle(), item.getId()));
                } else if (canLoadClass(defManager, item.getClassId(), entGrp.getArte().getTrace()) &&
                        (valClass == null || valClass.isAssignableFrom(defManager.getClass(item.getClassId()))) && //отсеем классы по типу
                        curUserCanCreate(defManager.getClassDef(item.getClassId()), creationPresentationIds, roleIds) //отсеем классы, на инстанцирование которых нет прав
                        ) {
                    classes.add(new EntityGroup.ClassCatalogItem(item.getLevel(), item.getClassId(), getItemTitle(item, defManager), item.getId()));
                }
            }
        } else if (contextProperty != null 
                      && contextProperty.getDestinationClassId() != null
                      && canLoadClass(defManager, contextProperty.getDestinationClassId(), entGrp.getArte().getTrace())
                      ) {
            //DBP-1630 if class catalog is not defined for a user property then let's try to create object of the property very class
            classes.add(new EntityGroup.ClassCatalogItem(1, contextProperty.getDestinationClassId(), null));
        }
        
        entGrp.onListInstantiableClasses(classCatalogId, classes);

        EntityGroup.ClassCatalogItem item;
        org.radixware.schemas.eas.InstantiatableClasses.Item itemXml;
        for (int i = 0; i < classes.size(); i++) {
            item = classes.get(i);
            //отсеем пустые группы
            if (item.getClassId() != null
                    || ((i + 1) < classes.size() && classes.get(i + 1).getLevel() == (item.getLevel() + 1))) {
                itemXml = classesXml.addNewItem();
                if (item.getClassId() != null) {
                    itemXml.addNewClass1().setId(item.getClassId());
                }
                itemXml.setTitle(item.getTitle());
                itemXml.setLevel(item.getLevel());
                itemXml.setId(item.getItemId());
            }
        }
    }
    
    private static boolean canLoadClass(final DefManager defManager, final Id classId, final Trace trace){
        try{
            defManager.getClassDef(classId);
            defManager.getClass(classId);
            return true;
        }catch(DefinitionError error){
            final List<String> words = new LinkedList<>();
            words.add(classId.toString());
            words.add(ExceptionTextFormatter.exceptionStackToString(error));
            trace.put(Messages.MLS_ID_EAS_ERR_ON_LOAD_INST_CLASS, words);
            return false;
        }
    }
    
    private static String getItemTitle(final RadClassPresentationDef.ClassCatalog.ItemPresentation item, final DefManager defManager){
        final String itemTitle = item.getTitle();
        if (itemTitle==null || itemTitle.isEmpty()){
            if (item.getClassId()==null){
                return item.getId().toString();
            }else{
                final RadClassDef classDef = defManager.getClassDef(item.getClassId());
                return classDef.getName();
            }
        }else{
            return itemTitle;
        }
    }
    
    private static boolean curUserCanCreate(final RadClassDef classDef, final List<Id> creationEditorPresentationIds, final List<Id> roleIds) {        
        final RadClassPresentationDef classPres = classDef.getPresentation();
        for (Id creationEditorPresentationId : creationEditorPresentationIds) {
            final Id presId = classPres.getActualPresentationId(creationEditorPresentationId);            
            final RadEditorPresentationDef pres;
            try{
                pres = classPres.getEditorPresentationById(presId);
            }catch(DefinitionNotFoundError error){
                return false;//Если презентация создания не нашлась в контексте данного элемента каталога классов, 
                            //то нужно пробовать другие элементы
            }
            if (!pres.getTotalRestrictions(roleIds).getIsCreateRestricted()) {
                return true;
            }
        }
        return false;
    }
    
    
}

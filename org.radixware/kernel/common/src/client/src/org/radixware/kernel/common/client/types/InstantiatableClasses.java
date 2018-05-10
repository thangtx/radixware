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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.ISelectInstantiatableClassDialog;
import org.radixware.kernel.common.client.exceptions.NoInstantiatableClassesException;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.ListInstantiatableClassesRs;


public class InstantiatableClasses {

    public static List<InstantiatableClass> getClasses(final IClientEnvironment environment, final Id entityId, final org.radixware.schemas.eas.Context context) throws ServiceClientException, InterruptedException {
        final ListInstantiatableClassesRs classList = environment.getEasSession().listInstantiatableClasses(entityId, context);
        return parseClasses(classList.getClasses(), environment);
    }
    
    public static List<InstantiatableClass> parseClasses(final org.radixware.schemas.eas.InstantiatableClasses xmlClasses,
                                                         final IClientEnvironment environment){
        final List<InstantiatableClass> classes = new LinkedList<>();
        if (xmlClasses != null) {
            final List<org.radixware.schemas.eas.InstantiatableClasses.Item> items = xmlClasses.getItemList();
            InstantiatableClass current = null;
            for (org.radixware.schemas.eas.InstantiatableClasses.Item item : items) {
                while (current != null && current.getLevel() >= item.getLevel()) {
                    current = current.getParentClass();
                }
                if (current != null) {
                    if (item.getClass1() != null) {
                        current = current.addDerivedClass(item.getClass1().getId(), item.getTitle(), item.getId());
                    } else {
                        current = current.addDerivedClass(null, item.getTitle(), item.getId());
                    }
                } else {
                    if (item.getClass1() == null) {
                        current = new InstantiatableClass(null, item.getTitle(), item.getId());                        
                    } else {
                        current = new InstantiatableClass(item.getClass1().getId(), item.getTitle(), item.getId());
                    }
                    classes.add(current);
                }
            }
        }
        return classes;        
    }

    public static InstantiatableClass selectClass(final IClientEnvironment environment, 
                                                  final IWidget parentWidget, 
                                                  final List<InstantiatableClass> classes,
                                                  final String configPrefix,
                                                  final boolean sortByTitles) throws NoInstantiatableClassesException {
        final List<InstantiatableClass> selectedClasses = 
                selectClassesImpl(environment, parentWidget, classes, configPrefix, sortByTitles, false);
        if (selectedClasses==null || selectedClasses.isEmpty()){
            return null;
        }else{
            return selectedClasses.get(0);
        }
    }
    
    public static List<InstantiatableClass> selectClasses(final IClientEnvironment environment, 
                                                  final IWidget parentWidget, 
                                                  final List<InstantiatableClass> classes,
                                                  final String configPrefix,
                                                  final boolean sortByTitles) throws NoInstantiatableClassesException {
        return selectClassesImpl(environment, parentWidget, classes, configPrefix, sortByTitles, false);
    }
    
    public static List<InstantiatableClass> selectClasses(final IClientEnvironment environment, 
                                                  final IWidget parentWidget, 
                                                  final List<InstantiatableClass> classes,
                                                  final String configPrefix,
                                                  final boolean sortByTitles,
                                                  final boolean isMultiselectEnabled) throws NoInstantiatableClassesException {
        return selectClassesImpl(environment, parentWidget, classes, configPrefix, sortByTitles, isMultiselectEnabled);
    }    
    
    public static InstantiatableClass autoSelectClass(final List<InstantiatableClass> classes){
        if (classes==null || classes.isEmpty()){
            return null;
        }else{
            InstantiatableClass resultClass = null;
            int classCount = 0;            
            for (InstantiatableClass c : classes) {
                classCount += getNotAbstractClassesCount(c);
                if (classCount > 1) {
                    break;
                }
                if (!c.isAbstractClass()) {
                    resultClass = c;
                    classCount++;
                } else if (classCount == 1 && resultClass == null) {
                    resultClass = findFirstNotAbstractClass(c);
                }
            }
            return classCount==1 ? resultClass : null;
        }    
    }
    
    private static List<InstantiatableClass> selectClassesImpl(final IClientEnvironment environment, 
                                                  final IWidget parentWidget, 
                                                  final List<InstantiatableClass> classes,
                                                  final String configPrefix,
                                                  final boolean sortByTitles,
                                                  final boolean multipleSelection) throws NoInstantiatableClassesException {
        if (classes != null && !classes.isEmpty()) {
            final InstantiatableClass resultClass = autoSelectClass(classes);

            if (resultClass != null) {
                return Collections.singletonList(resultClass);
            }

            final ISelectInstantiatableClassDialog dialog = 
                environment.getApplication().getDialogFactory().newInstantiatableClassDialog(environment, parentWidget, classes, configPrefix, sortByTitles, multipleSelection);
            environment.getProgressHandleManager().blockProgress();
            try {
                if (dialog.execDialog() == DialogResult.ACCEPTED) {
                    if (multipleSelection){
                        return dialog.getSelectedClasses();
                    }else{
                        return Collections.singletonList(dialog.getCurrentClass());
                    }
                } else {
                    return null;
                }
            } finally {
                environment.getProgressHandleManager().unblockProgress();
            }
        }
        throw new NoInstantiatableClassesException(environment.getMessageProvider());
    }    

    private static InstantiatableClass findFirstNotAbstractClass(InstantiatableClass clazz) {
        InstantiatableClass c;
        for (int i = 0; i < clazz.getDerivedClassesCount(); i++) {
            c = clazz.getDerivedClassByIdx(i);
            if (!c.isAbstractClass()) {
                return c;
            }
            c = findFirstNotAbstractClass(c);
            if (c != null) {
                return c;
            }
        }
        return null;
    }

    private static int getNotAbstractClassesCount(InstantiatableClass clazz) {
        int result = 0;
        InstantiatableClass c;
        for (int i = clazz.getDerivedClassesCount() - 1; i >= 0; i--) {
            c = clazz.getDerivedClassByIdx(i);
            if (!c.isAbstractClass()) {
                result++;
            }
            if (c.getDerivedClassesCount() > 0) {
                result += getNotAbstractClassesCount(c);
            }
        }
        return result;
    }
}

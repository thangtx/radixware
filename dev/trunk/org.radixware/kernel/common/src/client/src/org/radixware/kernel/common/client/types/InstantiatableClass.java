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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.types.Id;


public final class InstantiatableClass implements Cloneable{
    
    private interface InstantiatableClassFinder{
        boolean isTarget(InstantiatableClass c);
    }
            
    private final static class ClassFinderByTitle implements InstantiatableClassFinder{
        
        private final String classTitle;
        
        public ClassFinderByTitle(final String title){
            classTitle = title;
        }

        @Override
        public boolean isTarget(final InstantiatableClass c) {
            return Objects.equals(classTitle, c.getTitle());
        }        
    }
    
    private final static class ClassFinderById implements InstantiatableClassFinder{
        
        private final Id classId;
        
        public ClassFinderById(final Id id){
            classId = id;
        }

        @Override
        public boolean isTarget(final InstantiatableClass c) {
            return Objects.equals(classId, c.getId());
        }        
    }    
    
    private final Id classId;
    private final Id itemId;
    private int level = 1;
    private String title;
    private InstantiatableClass superClass;
    private List<InstantiatableClass> derivedClasses;
    
    public InstantiatableClass(final Id classId, final String title){
        this(classId, title, null);
    }
    
    public InstantiatableClass(final Id classId, final String title, final Id itemId){
        this.classId = classId;
        this.title = title;
        this.itemId = itemId;
    }    

    public Id getId() {
        return classId;
    }
    
    public Id getItemId(){
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
    
    private void setParentClass(final InstantiatableClass parentClass){
        level = parentClass==null ? 1 : parentClass.level + 1;
        superClass = parentClass;
    }

    public void addDerivedClass(final InstantiatableClass derivedClass) {
        insertDerivedClass(derivedClasses==null ? 0 : derivedClasses.size(), derivedClass);
    }
    
    public InstantiatableClass addDerivedClass(final Id classId, final String title) {
        return addDerivedClass(classId, title, null);
    }    
    
    public InstantiatableClass addDerivedClass(final Id classId, final String title, final Id itemId) {
        final InstantiatableClass derivedClass = new InstantiatableClass(classId, title, itemId);
        addDerivedClass(derivedClass);
        return derivedClass;
    }        
    
    public void insertDerivedClass(final int idx, final InstantiatableClass derivedClass) {
        if (derivedClass!=null){
            if (derivedClasses==null){
                derivedClasses = new ArrayList<>();
            }            
            derivedClass.setParentClass(this);         
            derivedClasses.add(idx, derivedClass);
        }        
    }

    public void insertDerivedClass(final int idx, final Id classId, final String title) {
        insertDerivedClass(idx, classId, title, null);
    }
    
    public void insertDerivedClass(final int idx, final Id classId, final String title, final Id itemId) {
        insertDerivedClass(idx, new InstantiatableClass(classId, title, itemId));
    }    

    public boolean isAbstractClass() {
        return classId==null;
    }
    
    public InstantiatableClass getDerivedClassByIdx(final int idx) {
        if (derivedClasses==null){
            throw new IndexOutOfBoundsException("size=0, index="+idx);
        }else{
            return derivedClasses.get(idx);
        }        
    }
    
    public List<InstantiatableClass> getDerivedClasses(){
        if (derivedClasses==null){
            return Collections.emptyList();
        }else{
            return Collections.unmodifiableList(derivedClasses);
        }
    }

    public InstantiatableClass findDerivedClassByTitle(final String title) {
        return findDerivedClass(new ClassFinderByTitle(title));
    }
    
    public InstantiatableClass findDerivedClassById(final Id id) {
        return findDerivedClass(new ClassFinderById(id));        
    }
    
    private InstantiatableClass findDerivedClass(final InstantiatableClassFinder finder){
        if (derivedClasses!=null){
            for (InstantiatableClass derivedClass: derivedClasses){
                if (finder.isTarget(derivedClass)){
                    return derivedClass;
                }
            }
        }
        return null;        
    }

    public InstantiatableClass findDerivedClassByTitleRecursively(final String title) {
        return findDerivedClassRecursively(new ClassFinderByTitle(title));
    }

    public InstantiatableClass findDerivedClassByIdRecursively(final Id id) {
        return findDerivedClassRecursively(new ClassFinderById(id));
    }        
    
    private InstantiatableClass findDerivedClassRecursively(final InstantiatableClassFinder finder){
        if (derivedClasses!=null && !derivedClasses.isEmpty()){
            final Stack<InstantiatableClass> stack = new Stack<>();
            stack.addAll(derivedClasses);
            InstantiatableClass derivedClass;
            while(!stack.isEmpty()){
                derivedClass = stack.pop();
                if (finder.isTarget(derivedClass)){
                    return derivedClass;
                }
                for (int i=derivedClass.getDerivedClassesCount()-1; i>=0; i--){
                    stack.add(derivedClass.getDerivedClassByIdx(i));
                }
            }
        }
        return null;        
    }

    public int getDerivedClassesCount() {
        return derivedClasses==null ? 0 : derivedClasses.size();
    }    

    public InstantiatableClass getParentClass() {
        return superClass;
    }

    public void removeDerivedClass(final InstantiatableClass derivedClass) {
        if (derivedClasses!=null && derivedClass!=null){
            derivedClasses.remove(derivedClass);
            derivedClass.setParentClass(null);            
            if (derivedClasses.isEmpty()){
                derivedClasses = null;
            }
        }
    }

    public InstantiatableClass takeDerivedClass(final int idx) {
        if (derivedClasses==null){
            throw new IndexOutOfBoundsException("size=0, index="+idx);
        }
        final InstantiatableClass derivedClass = derivedClasses.remove(idx);
        derivedClass.setParentClass(null);
        if (derivedClasses.isEmpty()){
            derivedClasses = null;
        }
        return derivedClass;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public InstantiatableClass clone() {
        final InstantiatableClass clone = new InstantiatableClass(classId, title, itemId);
        clone.level = level;
        if (derivedClasses!=null){
            for (InstantiatableClass derivedClass: derivedClasses){
                clone.addDerivedClass(derivedClass.clone());
            }
        }
        return clone;
    }
    
    public void writeToSettings(final ClientSettings settings){
        if (itemId!=null){
            settings.writeId("itemId", itemId);
        }
        if (classId!=null){
            settings.writeId("classId", classId);
        }
        settings.writeInteger("level", level);
        settings.writeString("title", title);
    }
    
    public boolean theSameAs(final InstantiatableClass other){
        if (getItemId()!=null && Objects.equals(getItemId(), other.getItemId())){
            return true;
        }
        if (getLevel()!=other.getLevel()){
            return false;
        }
        if (getId()!=null && Objects.equals(getId(), other.getId())){
            return true;
        }
        if (getId()!=null || other.getId()!=null){
            return false;
        }
        return Objects.equals(getTitle(), other.getTitle());
    }
    
    public boolean containsIn(final Collection<InstantiatableClass> classes){
        if (classes!=null && !classes.isEmpty()){
            for (InstantiatableClass cl: classes){
                if (theSameAs(cl)){
                    return true;
                }
            }
        }
        return false;
    }    
    
    public static InstantiatableClass readFromSettings(final ClientSettings settings){
        final Id itemId = settings.readId("itemId");
        final Id classId = settings.readId("classId");
        final int level = settings.readInteger("level", 1);
        final String title = settings.readString("title");
        final InstantiatableClass result = new InstantiatableClass(classId, title, itemId);
        result.level = level;
        return result;
    }
    
    public static List<InstantiatableClass> compact(final List<InstantiatableClass> classes){
        final List<InstantiatableClass> result = new ArrayList<>();        
        List<InstantiatableClass> compactedDerivedClasses;        
        List<InstantiatableClass> derivedClasses;
        for (InstantiatableClass cls: classes){
            compactedDerivedClasses = null;            
            if (cls.getDerivedClassesCount()>0){
                derivedClasses = new ArrayList<>();
                for (int i=0; i<cls.getDerivedClassesCount(); i++){
                    derivedClasses.add(cls.getDerivedClassByIdx(i));
                }
                compactedDerivedClasses = compact(derivedClasses);
            }
            if (!cls.isAbstractClass() || (compactedDerivedClasses!=null && !compactedDerivedClasses.isEmpty())){
                final InstantiatableClass classInfo = new InstantiatableClass(cls.getId(), cls.getTitle(), cls.getItemId());
                if (compactedDerivedClasses!=null){
                    for (InstantiatableClass derivedClass: compactedDerivedClasses){
                        classInfo.addDerivedClass(derivedClass);
                    }
                }
                result.add(classInfo);
            }
        }
        return result;
    }
    
    public static void writeToSettings(final ClientSettings settings, final String arrayName, final Collection<InstantiatableClass> classes){
        settings.beginWriteArray(arrayName);
        try{
            int counter = 0;
            for (InstantiatableClass cl: classes){
                settings.setArrayIndex(counter);
                cl.writeToSettings(settings);
                counter++;
            }
        }finally{
            settings.endArray();
        }
    }
    
    public static List<InstantiatableClass> readFromSettings(final ClientSettings settings, final String arrayName, final List<InstantiatableClass> readTo){
        final List<InstantiatableClass> result = readTo==null ? new LinkedList<InstantiatableClass>() : readTo;
        final int count = settings.beginReadArray(arrayName);
        try{
            for (int i=0; i<count; i++){
                settings.setArrayIndex(i);
                result.add(readFromSettings(settings));
            }
        }finally{
            settings.endArray();
        }
        return result;
    }
    
    public static List<InstantiatableClass> sortByTitles(final List<InstantiatableClass> classes){
        final List<InstantiatableClass> result = new ArrayList<>(classes);
        Collections.sort(result, new Comparator<InstantiatableClass>(){
            @Override
            public int compare(final InstantiatableClass o1, final InstantiatableClass o2) {
                if (o1.getTitle()==null || o1.getTitle().isEmpty()){
                    if (o2.getTitle()==null || o2.getTitle().isEmpty()){
                        return 0;
                    }else{
                        return -1;
                    }
                }else  if (o2.getTitle()==null || o2.getTitle().isEmpty()){
                    return 1;
                }else {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            }            
        });
        return result;
    }
}
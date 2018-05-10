/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;


public class PriorityArray<T> implements Set<T>{
    
    public final static int DEFAULT_PRIORITY = 0;
    
    private final static class ItemIndex implements Comparable<ItemIndex>{                
        
        private final long priority;
        private final int index;
        
        public ItemIndex(final long priority, final int index){
            this.priority = priority;
            this.index = index;
        }
        
        public ItemIndex(final int index){
            this(DEFAULT_PRIORITY,index);
        }

        public long getPriority() {
            return priority;
        }

        public int getIndex() {
            return index;
        }                

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + (int) (this.priority ^ (this.priority >>> 32));
            hash = 37 * hash + this.index;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ItemIndex other = (ItemIndex) obj;
            if (this.priority != other.priority) {
                return false;
            }
            if (this.index != other.index) {
                return false;
            }
            return true;
        }                

        @Override
        public int compareTo(final ItemIndex o) {
            int result = Long.compare(o.priority, priority);
            if (result==0){
                result = Integer.compare(index, o.index);
            }
            return result;
        }
        
        public ItemIndex createNextIndex(){
            if (index==Integer.MAX_VALUE){
                throw new IllegalStateException("No more indexes");
            }
            return new ItemIndex(priority, index+1);
        }
        
        public ItemIndex createPreviousIndex(){
            if (index==0){
                throw new IllegalStateException("No previous index");
            }            
            return new ItemIndex(priority, index-1);
        }
    }        
    
    private final static long LOWEST_PRIORITY = (long)Integer.MIN_VALUE - 1l;
    private final static long HIGHEST_PRIORITY = (long)Integer.MAX_VALUE + 1l;
            
    private final TreeMap<ItemIndex,T>  items = new TreeMap<>();
    private final TreeMap<Long,List<ItemIndex>> indexByPriority = new TreeMap<>();
    private final boolean isNullAcceptable;
    private final boolean isDuplicatesAcceptable;
    
    public PriorityArray(final boolean isNullAcceptable, final boolean isDuplicatesAcceptable){
        this.isNullAcceptable = isNullAcceptable;
        this.isDuplicatesAcceptable = isDuplicatesAcceptable;
    }
    
    public PriorityArray(){
        this(true,true);
    }
    
    public PriorityArray(final PriorityArray<T> copy){
        isNullAcceptable = copy.isNullAcceptable;
        isDuplicatesAcceptable = copy.isDuplicatesAcceptable;
        items.putAll(copy.items);
        for (Map.Entry<Long,List<ItemIndex>> entry: copy.indexByPriority.entrySet()){
            indexByPriority.put(entry.getKey(), new LinkedList<>(entry.getValue()));
        }        
    }
    
    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return items.containsValue(o);
    }

    @Override
    public Iterator<T> iterator() {
        return items.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return items.values().toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return items.values().toArray(a);
    }
    
    private ItemIndex nextIndex(final long priority){
        List<ItemIndex> indexes = indexByPriority.get(priority);        
        final ItemIndex nextIndex;
        if (indexes==null){
            indexes = new LinkedList<>();
            indexByPriority.put(priority, indexes);
            nextIndex = new ItemIndex(priority, 0);            
        }else {
            final ItemIndex lastIndex = indexes.get(indexes.size()-1);
            nextIndex = lastIndex.createNextIndex();
        }
        indexes.add(nextIndex);
        return nextIndex;
    }
    
    public int addWithHighestPriority(final T e){
        if (!isNullAcceptable && e==null){
            throw new NullPointerException();
        }
        if (!isDuplicatesAcceptable && items.containsValue(e)){
            return -1;
        }
        final ItemIndex itemIndex = new ItemIndex(HIGHEST_PRIORITY, 0);
        List<ItemIndex> indexes = indexByPriority.get(HIGHEST_PRIORITY);        
        if (indexes==null){
            indexes = new LinkedList<>();
            indexByPriority.put(HIGHEST_PRIORITY, indexes);
            indexes.add(itemIndex);
        }else{
            indexes.add(0, itemIndex);
            reindexItems(HIGHEST_PRIORITY, true);
        }
        items.put(itemIndex, e);
        return 0;
    }
    
    public int addWithLowestPriority(final T e){
        if (!isNullAcceptable && e==null){
            throw new NullPointerException();
        }
        if (!isDuplicatesAcceptable && items.containsValue(e)){
            return -1;
        }
        items.put(nextIndex(LOWEST_PRIORITY), e);
        return items.size()-1;
    }
    
    public int addWithPriority(final T e, final int priority){
        if (!isNullAcceptable && e==null){
            throw new NullPointerException();
        }
        if (!isDuplicatesAcceptable && items.containsValue(e)){
            return -1;
        }
        final Set<Long> priorities = indexByPriority.tailMap(Long.valueOf(priority), false).keySet();
        int index = 0;
        for (long priorityKey: priorities){
            index+=indexByPriority.get(priorityKey).size();
        }
        List<ItemIndex> itemIndexes = indexByPriority.get(Long.valueOf(priority));
        if (itemIndexes==null){
            itemIndexes = new LinkedList<>();
            indexByPriority.put(Long.valueOf(priority), itemIndexes);
        }
        final ItemIndex itemIndex = new ItemIndex(priority, itemIndexes.size());
        itemIndexes.add(itemIndex);
        items.put(itemIndex, e);
        return index+itemIndexes.size()-1;
    }

    @Override
    public boolean add(final T e) {
        if (!isNullAcceptable && e==null){
            throw new NullPointerException();
        }
        if (!isDuplicatesAcceptable && items.containsValue(e)){
            return false;
        }
        items.put(nextIndex(DEFAULT_PRIORITY), e);
        return true;
    }
    
    public T get(final int index){
        if (index<0){
            throw new IndexOutOfBoundsException("index must not be negative ");
        }else if (index>=items.size()){
            throw new IndexOutOfBoundsException("index = "+String.valueOf(index)+", count = "+String.valueOf(items.size()));
        }
        int i=0;
        for (ItemIndex itemIndex: items.keySet()){
            if (i==index){
                return items.get(itemIndex);
            }
            i++;
        }
        throw new IndexOutOfBoundsException("index = "+String.valueOf(index)+", count = "+String.valueOf(items.size()));
    }
    
    public int indexOf(final T e){
        return indexOf(e,0);
    }
    
    public int indexOf(final T e, final int startFrom){
        int i=0;
        for (T value: items.values()){
            if (Objects.equals(value, e) && i>=startFrom){
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public boolean remove(final Object o) {
        final List<ItemIndex> itemIndexes = new LinkedList<>();
        for (Map.Entry<ItemIndex,T> entry: items.entrySet()){
            if (Objects.equals(entry.getValue(), o)){
                itemIndexes.add(entry.getKey());
            }
        }
        if (itemIndexes.isEmpty()){
            return false;
        }
        final List<Long> priorities = new LinkedList<>();
        for (ItemIndex itemIndex: itemIndexes){
            items.remove(itemIndex);
            long priority = itemIndex.getPriority();
            indexByPriority.get(priority).remove(itemIndex);
            if (!priorities.contains(priority)){
                priorities.add(priority);
            }
        }
        for (long priority: priorities){
            reindexItems(priority,false);
        }
        return true;
    }
    
    private void reindexItems(final long priority, final boolean reverse){
        final List<ItemIndex> indexes = indexByPriority.get(priority);
        if (indexes.isEmpty()){
            indexByPriority.remove(priority);
        }else{
            ItemIndex currentItemIndex, newItemIndex;
            T value;
            final int startIndex = reverse ? indexes.size()-1 : 0;
            final int endIndex = reverse ? -1 : indexes.size();
            final int delta = reverse ? -1 : 1;
            for (int i=startIndex; i!=endIndex; i+=delta){
                currentItemIndex = indexes.get(i);
                if (currentItemIndex.getIndex()!=i){
                    value = items.remove(currentItemIndex);
                    newItemIndex = new ItemIndex(priority, i);
                    indexes.set(i, newItemIndex);
                    items.put(newItemIndex, value);
                }
            }
        }
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        for (Object value: c){
            if (!contains(value)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        boolean result = false;
        for (T value: c){
            if (add(value)){
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        final List<ItemIndex> itemIndexes = new LinkedList<>();
        for (Map.Entry<ItemIndex,T> entry: items.entrySet()){
            if (!c.contains(entry.getValue())){
                itemIndexes.add(entry.getKey());
            }
        } 
        if (itemIndexes.isEmpty()){
            return false;
        }
        final List<Long> priorities = new LinkedList<>();
        for (ItemIndex itemIndex: itemIndexes){
            items.remove(itemIndex);
            long priority = itemIndex.getPriority();
            indexByPriority.get(priority).remove(itemIndex);
            if (!priorities.contains(priority)){
                priorities.add(priority);
            }
        }
        for (long priority: priorities){
            reindexItems(priority,false);
        }
        return true;        
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        boolean result = false;
        for (Object value: c){
            if (remove(value)){
                result = true;
            }
        }
        return result;
    }

    @Override
    public void clear() {
        items.clear();
        indexByPriority.clear();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.items);
        hash = 71 * hash + (this.isNullAcceptable ? 1 : 0);
        hash = 71 * hash + (this.isDuplicatesAcceptable ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PriorityArray<?> other = (PriorityArray<?>) obj;
        if (!Objects.equals(this.items, other.items)) {
            return false;
        }
        if (this.isNullAcceptable != other.isNullAcceptable) {
            return false;
        }
        if (this.isDuplicatesAcceptable != other.isDuplicatesAcceptable) {
            return false;
        }
        return true;
    }    

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new PriorityArray<>(this);
    }    
}

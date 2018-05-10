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

package org.radixware.kernel.common.client.trace;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;

public abstract class AbstractTraceBuffer<T extends IClientTraceItem> {

    public interface TraceBufferListener<T extends IClientTraceItem> {

        public void newItemInBuffer(T traceItem);

        public void maxSeverityChanged(EEventSeverity eventSeverity);

        public void cleared();
    }


    private final ArrayList<T> items = new ArrayList<>();
    private int maxSize = -1;
    private boolean quiet = false;
    private EEventSeverity maxSeverity = EEventSeverity.NONE;
    private AbstractTraceBuffer<T> mergeWith;
    private final Object semaphore = new Object();
    private final List<TraceBufferListener<T>> listeners = new LinkedList<>();
    private final TraceBufferListener<T> listenerCache = new TraceBufferListener<T>() {

        @Override
        public void newItemInBuffer(T traceItem) {
            synchronized (listeners) {
                final List<TraceBufferListener<T>> savedListeners = new LinkedList<>(listeners);
                for (TraceBufferListener<T> l : savedListeners) {
                    l.newItemInBuffer(traceItem);
                }
            }
        }

        @Override
        public void maxSeverityChanged(EEventSeverity eventSeverity) {
            synchronized (listeners) {
                final List<TraceBufferListener<T>> savedListeners = new LinkedList<>(listeners);
                for (TraceBufferListener l : savedListeners) {
                    l.maxSeverityChanged(eventSeverity);
                }
            }
        }

        @Override
        public void cleared() {
            synchronized (listeners) {
                final List<TraceBufferListener<T>> savedListeners = new LinkedList<>(listeners);
                for (TraceBufferListener l : savedListeners) {
                    l.cleared();
                }
            }
        }
    };

    public void addTraceBufferListener(TraceBufferListener<T> l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeTraceBufferListener(TraceBufferListener<T> l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    
    void clearTraceBufferListeners(){
        synchronized (listeners) {
            listeners.clear();
        }
    }
    
    public void put(final EEventSeverity severity,final Object[] parts){
        final T item = createTraceItem((String) parts[4], severity, (String) parts[3], ((Date) parts[2]).getTime());
        put(item);
    }
        
    public void put(final EEventSeverity severity, final String message, final String source){
        put(createTraceItem(message, severity, source, System.currentTimeMillis()));
    }
    
    protected abstract T createTraceItem(final String message, final EEventSeverity severity, final String source, final long timeMillis);
    
    protected abstract T copyTraceItem(T source);

    private void put(final T item) {
        synchronized (semaphore){
            final EEventSeverity removedItemSeverity;
            if (maxSize > 0 && items.size() >= maxSize) {
                removedItemSeverity = items.get(0).getSeverity();
                afterRemoveTraceItem(items.remove(0));
            }else{
                removedItemSeverity = null;
            }
            if (maxSeverity == EEventSeverity.NONE || maxSeverity.getValue() < item.getSeverity().getValue()) {
                maxSeverity = item.getSeverity();
                if (!quiet) {
                    listenerCache.maxSeverityChanged(maxSeverity);
                }
            }
            items.add(item);
            afterPutTraceItem(item);
            if (!quiet && removedItemSeverity!=null && maxSeverity.getValue()<=removedItemSeverity.getValue()){
                EEventSeverity newMaxSeverity = null;
                for (T traceItem: asList()){
                    if (traceItem.getSeverity()!=null && traceItem.getSeverity()!=EEventSeverity.NONE){
                        if (traceItem.getSeverity().getValue()>=removedItemSeverity.getValue()){
                            newMaxSeverity = null;
                            break;
                        }
                        if (newMaxSeverity==null || newMaxSeverity.getValue()<traceItem.getSeverity().getValue()){
                            newMaxSeverity = traceItem.getSeverity();
                        }
                    }
                }
                if (newMaxSeverity!=null){
                    maxSeverity = newMaxSeverity;
                    listenerCache.maxSeverityChanged(maxSeverity);
                }
            }
            if (!quiet) {
                listenerCache.newItemInBuffer(item);
            }
        }
    }

    public void add(final AbstractTraceBuffer<T> buffer) {
        synchronized (semaphore){
            for (T item : buffer.asList()) {
                put(item);
            }
        }
    }

    public void startMerge(final AbstractTraceBuffer<T> buffer) {
        if (buffer != null) {
            synchronized (buffer.semaphore){            
                if (buffer.items.size() > 1) {
                    T item;
                    for (int i = 0; i < buffer.items.size() - 2; i++) {
                        item = buffer.items.get(i);
                        put(copyTraceItem(item));
                    }
                }
            }
            synchronized(semaphore){
                mergeWith = buffer;
            }
        }
    }

    public void finishMerge() {
        synchronized (semaphore){
            if (mergeWith!=null){
                synchronized (mergeWith.semaphore){
                    if (!mergeWith.items.isEmpty()) {
                        final int itemsCount = mergeWith.items.size();
                        for (int i = Math.max(itemsCount-2, 0); i<itemsCount; i++){
                            final T item = mergeWith.items.get(i);
                            put(copyTraceItem(item));
                        }
                    }
                }
                mergeWith = null;
            }
        }
    }

    public AbstractTraceBuffer<T> getMergeWith() {
        return mergeWith;
    }

    public List<T> asList() {
        synchronized (semaphore){
            return new ArrayList<>(items);            
        }
    }
    
    public boolean isEmpty(){
        synchronized (semaphore){
            return items.isEmpty();
        }
    }

    public void clear() {
        synchronized (semaphore){
            items.clear();
            afterClearTraceItems();
            if (!quiet) {
                listenerCache.cleared();
            }
            maxSeverity = EEventSeverity.NONE;
            if (!quiet) {
                listenerCache.maxSeverityChanged(maxSeverity);
            }
        }
    }

    public EEventSeverity getMaxSeverity() {
        return maxSeverity;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int newMaxSize) {
        synchronized (semaphore){
            if (newMaxSize > 0) {
                while (items.size() > newMaxSize) {
                    afterRemoveTraceItem(items.remove(0));
                }
            }
            maxSize = newMaxSize;
        }
    }

    public void beQuiet() {
        quiet = true;
    }

    public void allowSignals() {
        quiet = false;
    }
    
    protected void afterPutTraceItem(final T traceItem){
        
    }
    
    protected void afterRemoveTraceItem(final T traceItem){
        
    }
    
    protected void afterClearTraceItems(){        
    }
}

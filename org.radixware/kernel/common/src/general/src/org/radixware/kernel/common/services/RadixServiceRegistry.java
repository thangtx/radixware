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

package org.radixware.kernel.common.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;


public class RadixServiceRegistry {

    private final static RadixServiceRegistry defaultRegistry = new RadixServiceRegistry();
    //private final ServiceLoader<IRadixService> radixServiceLoader = ServiceLoader.load(IRadixService.class);
    private final ConcurrentHashMap<Class, FutureTask> class2serviceLoader = new ConcurrentHashMap<Class, FutureTask>();

    protected RadixServiceRegistry() {
    }

    public <T extends Object> Iterator<T> iterator(final Class<T> clazz) {
        FutureTask<RadixServiceLoader<T>> ft = new FutureTask<RadixServiceLoader<T>>(new Callable<RadixServiceLoader<T>>() {
            @Override
            public RadixServiceLoader call() throws Exception {
                return new RadixServiceLoader(clazz);
            }
        });
        final FutureTask<RadixServiceLoader<T>> f = class2serviceLoader.putIfAbsent(clazz, ft);
        boolean isCalculatingThread = false;//flag that indicates if current thread will calculate appropriate RadixServiceLoader
        if (f != null) { //other thread is already calculating or has calculated
            ft = f; //let's wait (in get())
        } else { // it's a first thread wich requested the srvice 
            ft.run(); //let's calculate on current thread
            isCalculatingThread = true;
        }
        try {
            return ft.get().iterator();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();//because interrupted flag had been reseted by calling Thread.interrupted() in FutureTask.get()
            throw new RadixError("Service loading thread is interrupted" + (isCalculatingThread ? " while calculating RadixServiceLoader" : " while waiting for another thread to calculate RadixServiceLoader"), ex);
        } catch (ExecutionException ex) {
            throw new RadixError("Service loading error: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
        }
    }

    private static class RadixServiceLoader<T extends Object> implements Iterable<T> {

        private final List<T> javaServiceList;
        private final List<T> radixServiceList;

        public RadixServiceLoader(final Class<T> clazz) {
            javaServiceList = createJavaServiceList(clazz);
            radixServiceList = createRadixServiceList(clazz);
        }

        private List<T> createJavaServiceList(final Class<T> clazz) {
            final ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
            final List<T> servicesFromJavaLoader = new ArrayList<T>();
            final Iterator<T> javaServiceIterator = serviceLoader.iterator();
            while (javaServiceIterator.hasNext()) {
                servicesFromJavaLoader.add(javaServiceIterator.next());
            }
            return Collections.unmodifiableList(servicesFromJavaLoader);
        }

        private List<T> createRadixServiceList(final Class<T> clazz) {
            if (IRadixService.class.isAssignableFrom(clazz)) {
                final ServiceLoader<IRadixService> radixServiceLoader = ServiceLoader.load(IRadixService.class);

                final List<T> list = new LinkedList<T>();
                final Iterator<IRadixService> iterator = radixServiceLoader.iterator();

                while (iterator.hasNext()) {
                    try {
                        final T radixService = (T) iterator.next();
                        list.add(radixService);
                    } catch (ClassCastException ex) {
                        Logger.getLogger(RadixServiceRegistry.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }

                return Collections.unmodifiableList(list);
            } else {
                return Collections.emptyList();
            }
        }

        private class RadixServiceIterator implements Iterator<T> {

            private final Iterator<T> javaIterator = javaServiceList.iterator();
            private final Iterator<T> radixIterator = radixServiceList.iterator();

            @Override
            public boolean hasNext() {
                return javaIterator.hasNext() || radixIterator.hasNext();
            }

            @Override
            public T next() {
                if (javaIterator.hasNext()) {
                    return javaIterator.next();
                }
                if (radixIterator.hasNext()) {
                    return radixIterator.next();
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("This is Read-Only iterator");
            }
        }

        @Override
        public Iterator<T> iterator() {
            return new RadixServiceIterator();
        }
    }

    public static RadixServiceRegistry getDefault() {
        return defaultRegistry;
    }
}

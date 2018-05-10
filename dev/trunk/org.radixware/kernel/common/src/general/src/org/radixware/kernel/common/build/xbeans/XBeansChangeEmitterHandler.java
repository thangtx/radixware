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

package org.radixware.kernel.common.build.xbeans;

import java.util.*;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.PrePostExtension;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;


public class XBeansChangeEmitterHandler {

    public interface ListenersStorage {

        void addListener(XmlObject obj, IXBeansChangeListener listener);

        void removeListener(XmlObject obj, IXBeansChangeListener listener);

        Collection<IXBeansChangeListener> getListeners(XmlObject obj);

        boolean hasListeners(XmlObject obj);
    }

    public interface ListenersStorageProvider {

        ListenersStorage getListenersStorage();
    }

    public static void fireXBeansChangeEvent(XmlObject xo, XBeansChangeEvent event) {
        final ListenersStorage storage = getStorage();
        if (storage != null) {
            final Collection<IXBeansChangeListener> listeners = storage.getListeners(xo);
            if (listeners != null) {
                for (IXBeansChangeListener l : listeners) {
                    l.xBeansChange(event);
                }
            }
        }
    }

    public static boolean beforeXBeansChangeEvent(XmlObject xo, XBeansChangeEvent event) {
        final ListenersStorage storage = getStorage();
        if (storage != null) {
            final Collection<IXBeansChangeListener> listeners = storage.getListeners(xo);
            if (listeners != null) {
                for (IXBeansChangeListener l : listeners) {
                    if (!l.beforeXBeansChange(event)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static ListenersStorage getStorage() {
        final Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ListenersStorageProvider) {
            return ((ListenersStorageProvider) currentThread).getListenersStorage();
        }
        return null;
    }

    public static void addXBeansChangeListener(XmlObject xo, IXBeansChangeListener xbeansChangeListener) {
        ListenersStorage storage = getStorage();
        if (storage != null) {
            storage.addListener(xo, xbeansChangeListener);
        }
    }

    public static void removeXBeansChangeListener(XmlObject xo, IXBeansChangeListener xbeansChangeListener) {
        ListenersStorage storage = getStorage();
        if (storage != null) {
            storage.removeListener(xo, xbeansChangeListener);
        }
    }

    public static boolean hasXBeansChangeListeners(XmlObject xo) {
        ListenersStorage storage = getStorage();
        if (storage != null) {
            return storage.hasListeners(xo);
        } else {
            return false;
        }

    }
    //private static final Map<Integer, Object> oldValues = new HashMap<>();

    private static class ChangeInfo {

        private final Object newValue;
        private final XBeansChangeEvent.Action action;

        public ChangeInfo(Object newValue, XBeansChangeEvent.Action action) {
            this.newValue = newValue;
            this.action = action;
        }

        private static ChangeInfo getInfo(int opType, XmlObject xo, QName propertyName, boolean isAttr, int index, boolean post) {
            XBeansChangeEvent.Action action = XBeansChangeEvent.Action.UPDATE;
            XmlObject newXO = null;
            if (isAttr) {
                if (post) {
                    newXO = xo.selectAttribute(propertyName);
                }
            } else {
                switch (opType) {
                    case PrePostExtension.OPERATION_SET:
                        if (post) {
                            newXO = getChildXO(xo, propertyName, index);
                        }
                        break;
                    case PrePostExtension.OPERATION_INSERT:
                        if (post) {
                            XmlObject[] children = xo.selectChildren(propertyName);

                            // on an insert, it is always the last element
                            newXO = children[children.length - 1];
                        }
                        action = XBeansChangeEvent.Action.CREATE;
                        break;
                    case PrePostExtension.OPERATION_REMOVE:
                        action = XBeansChangeEvent.Action.DELETE;
                        break;
                    default:
                        break;
                }
            }
            if (post) {
                return new ChangeInfo(newXO, action);
            } else {
                return new ChangeInfo(null, action);
            }

        }
    }

    public static boolean preSet(int opType, XmlObject xo, QName propertyName, boolean isAttr, int index) {
        IXBeansChangeEmitter emitter = (IXBeansChangeEmitter) xo;

        ChangeInfo info = ChangeInfo.getInfo(opType, xo, propertyName, isAttr, index, false);

        XBeansChangeEvent cause = new XBeansChangeEvent(propertyName, emitter, info.newValue, info.action, index);
        if (emitter.hasXBeansChangeListeners() && !emitter.beforeXBeansChangeEvent(cause)) {
            return false;
        }

        XmlCursor cursor = xo.newCursor();
        XmlObject current = xo;
        try {
            while (current != null) {
                cursor.toParent();
                XmlObject next = cursor.getObject();
                if (current == next) {
                    current = null;
                } else {
                    current = next;
                    if (current instanceof IXBeansChangeEmitter) {
                        emitter = (IXBeansChangeEmitter) current;
                        if (emitter.hasXBeansChangeListeners()) {
                            cause = new XBeansChangeEvent(emitter, cause);
                            if (!emitter.beforeXBeansChangeEvent(cause)) {
                                return false;
                            }
                        }
                    }
                }
            }
        } finally {
            cursor.dispose();
        }

        return true;
    }

    public static boolean postSet(int opType, XmlObject xo, QName propertyName, boolean isAttr, int index) {

        IXBeansChangeEmitter emitter = (IXBeansChangeEmitter) xo;
        ChangeInfo info = ChangeInfo.getInfo(opType, xo, propertyName, isAttr, index, true);
        XBeansChangeEvent cause = new XBeansChangeEvent(propertyName, emitter, info.newValue, info.action, index);
        if (emitter.hasXBeansChangeListeners()) {
            emitter.fireXBeansChangeEvent(cause);
        }

        XmlCursor cursor = xo.newCursor();
        XmlObject current = xo;
        try {
            while (current != null) {
                cursor.toParent();
                XmlObject next = cursor.getObject();
                if (current == next) {
                    current = null;
                } else {
                    current = next;
                    if (current instanceof IXBeansChangeEmitter) {
                        emitter = (IXBeansChangeEmitter) current;
                        if (emitter.hasXBeansChangeListeners()) {
                            cause = new XBeansChangeEvent(emitter, cause);
                            emitter.fireXBeansChangeEvent(cause);
                        }
                    }
                }
            }
        } finally {
            cursor.dispose();
        }

        return true;

    }

    private static XmlObject getChildXO(XmlObject xo, QName propertyName, int index) {
        XmlObject childXO = null;
        XmlObject[] values = xo.selectChildren(propertyName);
        if (values.length != 0) {
            if (index == -1) {
                childXO = values[0];
            } else if (index < values.length) {
                childXO = values[index];
            }
        }
        return childXO;
    }

//    private static XmlObject getParent(XmlObject xo) {
//        XmlCursor cursor = xo.newCursor();
//        cursor.toParent();
//        XmlObject parentXbean = cursor.getObject();
//        cursor.dispose();
//        return parentXbean == xo ? null : parentXbean;
//    }

//    private static int hashCode(XmlObject xo, QName propertyName, boolean isAttr, int index) {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + index;
//        result = prime * result + (isAttr ? 1231 : 1237);
//        result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
//        result = prime * result + ((xo == null) ? 0 : xo.hashCode());
//        return result;
//    }
}

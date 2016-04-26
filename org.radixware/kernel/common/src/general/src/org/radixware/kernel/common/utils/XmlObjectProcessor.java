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

package org.radixware.kernel.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.soap.Node;
import org.radixware.kernel.common.exceptions.AppError;

public class XmlObjectProcessor {

    public static final XmlObject getXmlObjectFirstChild(final XmlObject input) {
        if (input == null) {
            return null;
        }
        final XmlCursor c = input.newCursor();
        try {
            if (c.toFirstChild()) {
                return c.getObject();
            } else {
                return null;
            }
        } finally {
            c.dispose();
        }
    }

    public static final boolean hasChild(final XmlObject input) {
        if (input == null) {
            return false;
        }
        final XmlCursor c = input.newCursor();
        try {
            return c.toFirstChild();
        } finally {
            c.dispose();
        }
    }

    public static XmlObject castToXmlClass(final ClassLoader classLoader, final XmlObject obj, final Class castTo) {
        return cast(classLoader, obj, (Class<? extends XmlObject>) castTo);
    }

    public static <T extends XmlObject> T cast(final ClassLoader classLoader, final XmlObject obj, final Class<T> castTo) {
        if (obj != null) {
            if (!castTo.isInstance(obj)) {
                final String factoryClassName = castTo.getName() + "$Factory";

                final Class<?> c;
                try {
                    c = classLoader.loadClass(factoryClassName);
                } catch (ClassNotFoundException e) {
                    throw new AppError("Unable to find factory class for casting: " + factoryClassName, e);
                }
                final Method m;
                try {
                    m = c.getMethod("parse", new Class<?>[]{org.w3c.dom.Node.class});
                } catch (SecurityException e) {
                    throw new AppError("Unable to get factory class method for casting: " + factoryClassName + ".parse( org.w3c.dom.Node )", e);
                } catch (NoSuchMethodException e) {
                    throw new AppError("Unable to find factory class method for casting: " + factoryClassName + ".parse( org.w3c.dom.Node )", e);
                }

                try {
                    return (T) m.invoke(null, new Object[]{obj.getDomNode()});
                } catch (IllegalArgumentException e) {
                    throw new AppError("Unable to cast - internal error: illegal arguments", e);
                } catch (IllegalAccessException e) {
                    throw new AppError("Unable to cast - internal error: illegal access", e);
                } catch (InvocationTargetException e) {
                    Throwable ex = e;
                    if (e.getTargetException() != null) {
                        ex = e.getTargetException();
                    }
                    throw new AppError("Can't parse message" + (ex.getMessage() != null ? " :" + ex.getMessage() : ""), ex);
                }
            } else {
                return (T) obj;
            }
        }
        return null;
    }

    //yremizov
        public static final XmlObject toSeparateXml(final XmlObject xml) {
        if (xml == null || xml.isNil() || xml.getDomNode().getNodeType() == Node.DOCUMENT_NODE) {
            return xml;
        }
        final XmlCursor c = xml.newCursor();
        try {
            return XmlObject.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + c.xmlText());
        } catch (XmlException e) {
            return xml;
        } finally {
            c.dispose();
        }
    }

    //yremizov
    public static final XmlObject createNewInstance(final ClassLoader classLoader, final Class document) {
        final String factoryClassName = document.getName() + "$Factory";
        final Class<?> c;
        try {
            c = classLoader.loadClass(factoryClassName);
        } catch (ClassNotFoundException e) {
            throw new AppError("Unable to find factory class for casting: " + factoryClassName, e);
        }

        final Method m;
        try {
            m = c.getMethod("newInstance", new Class<?>[]{});
        } catch (SecurityException e) {
            throw new AppError("Unable to get factory class method for casting: " + factoryClassName + ".parse( org.w3c.dom.Node )", e);
        } catch (NoSuchMethodException e) {
            throw new AppError("Unable to find factory class method for casting: " + factoryClassName + ".parse( org.w3c.dom.Node )", e);
        }

        try {
            return (org.apache.xmlbeans.XmlObject) m.invoke(null, new Object[]{});
        } catch (IllegalArgumentException e) {
            throw new AppError("Unable to cast - internal error: illegal arguments", e);
        } catch (IllegalAccessException e) {
            throw new AppError("Unable to cast - internal error: illegal access", e);
        } catch (InvocationTargetException e) {
            Throwable ex = e;
            if (e.getTargetException() != null) {
                ex = e.getTargetException();
            }
            throw new AppError("Exception on invoke \"newInstance\" method: " + ex.getMessage(), ex);
        }

    }

    //yremizov
    static public ArrayList<XmlError> validate(final XmlObject xml) {
        if (xml != null) {
            final XmlOptions validateOptions = new XmlOptions();
            final ArrayList<XmlError> errorList = new ArrayList<XmlError>();
            validateOptions.setErrorListener(errorList);
            if (!xml.validate(validateOptions)) {
                return errorList;
            }
        }
        return null;
    }

    public static final XmlObject getAppCommandInputData(final ClassLoader classLoader, final XmlObject input, final Class castTo) {
        if (input != null && castTo != null) {
            return castToXmlClass(classLoader, getXmlObjectFirstChild(input), castTo);
        } else {
            return null;
        }
    }
}

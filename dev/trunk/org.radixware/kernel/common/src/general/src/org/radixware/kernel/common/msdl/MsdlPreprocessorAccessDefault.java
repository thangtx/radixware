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
package org.radixware.kernel.common.msdl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.radixware.kernel.common.exceptions.SmioException;

/**
 *
 * @author npopov
 */
class MsdlPreprocessorAccessDefault implements IMsdlPreprocessorAccess {

    private final Class preprocessorClass;

    public MsdlPreprocessorAccessDefault(Class clazz) {
        this.preprocessorClass = clazz;
    }

    @Override
    public byte[] read(String methodName, byte[] arg) throws SmioException {
        try {
            return (byte[]) invokeMethod(methodName, arg);
        } catch (Throwable ex) {
            throw new SmioException("Error on execute read method of preprocessor", ex);
        }
    }

    @Override
    public byte[] write(String methodName, byte[] arg) throws SmioException {
        try {
            return (byte[]) invokeMethod(methodName, arg);
        } catch (Throwable ex) {
            throw new SmioException("Error on execute write method of preprocessor", ex);
        }
    }

    @Override
    public String select(String methodName, byte[] arg) throws SmioException {
        try {
            return (String) invokeMethod(methodName, arg);
        } catch (Throwable ex) {
            throw new SmioException("Error on execute select method of preprocessor", ex);
        }
    }

    private Object invokeMethod(String methodName, byte[] arg) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (methodName == null) {
            throw new NullPointerException("Method name is null");
        }
        Method method = preprocessorClass.getMethod(methodName, new Class[]{byte[].class});
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(method, new Object[]{arg});
    }
}

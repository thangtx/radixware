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

package org.radixware.ws.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.utils.FileUtils;


public class UploadStore {

    private static File storageDir;

    public static boolean init() {
        try {
            storageDir = File.createTempFile("rdx_upload", "store");
            storageDir.delete();
            storageDir.mkdirs();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    private static final Map<String, String> id2uuid = new HashMap<String, String>();
    private static final Map<String, Object> waiters = new HashMap<String, Object>();

    public static File createFile(String srcName) {
        UUID uuid = UUID.randomUUID();
        File file = new File(storageDir, uuid.toString());
        return file;
    }

    public static Object notifyUploaded(String srcId, File file) {
        synchronized (waiters) {
            Object waiter = waiters.get(srcId);
            if (waiter != null) {
                notifyWaiter(srcId, waiter, file);
                return waiter;
            } else {
                synchronized (id2uuid) {
                    id2uuid.put(srcId, file.getName());
                }
                return null;
            }
        }
    }
    
    public static void notifyUploadFailed(final String srcId, final Exception exception){
        synchronized (waiters) {
            Object waiter = waiters.get(srcId);
            if (waiter != null) {
                notifyWaiter(srcId, waiter, exception);
            }
        }        
    }

    public static void registerWaiter(String srcId, Object waiter) {
        String uuid = null;
        synchronized (id2uuid) {
            uuid = id2uuid.get(srcId);
        }
        if (uuid != null) {

            File file = new File(storageDir, uuid);
            if (file.exists()) {
                notifyWaiter(srcId, waiter, file);
            } else {
                notifyWaiter(srcId, waiter, new FileNotFoundException(srcId));
            }
        } else {
            synchronized (waiters) {
                waiters.put(srcId, waiter);
            }
        }
    }

    public static void removeFile(String descriptor) {
        File file = new File(storageDir, descriptor);
        if (file.exists()) {
            try {
                FileUtils.removeRecursively(file);
            } catch (RadixLoaderException ex) {
            }
        }
    }

    private static void notifyWaiter(String srcId, Object waiter, File file) {
        synchronized (id2uuid) {
            id2uuid.remove(srcId);
        }

        try {
            Method m = waiter.getClass().getMethod("upload", File.class);
            m.invoke(waiter, file);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(UploadStore.class.getName()).log(Level.SEVERE, "Upload Exception", ex);

        }
    }

    private static void notifyWaiter(String srcId, Object waiter, Exception e) {
        synchronized (id2uuid) {
            id2uuid.remove(srcId);
        }
        try {
            Method m = waiter.getClass().getMethod("fail", Exception.class);
            try {
                m.invoke(waiter, e);
            } catch (IllegalAccessException ex) {
            } catch (IllegalArgumentException ex) {
            } catch (InvocationTargetException ex) {
            }
        } catch (NoSuchMethodException ex) {
        } catch (SecurityException ex) {
        }
    }

    public static void dispose() {
        if (storageDir != null) {
            try {
                FileUtils.removeRecursively(storageDir);
            } catch (RadixLoaderException ex) {
            }
        }
        waiters.clear();
        id2uuid.clear();
    }

    public static File getRepository() {
        return storageDir;
    }
}

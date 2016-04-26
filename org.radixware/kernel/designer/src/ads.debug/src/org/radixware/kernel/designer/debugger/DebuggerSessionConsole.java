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

package org.radixware.kernel.designer.debugger;

import com.sun.jdi.VirtualMachine;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;


class DebuggerSessionConsole {

    private final InputOutput io;
    private static RadixDebuggerConsole instance;
    private static final Lock errlock = new ReentrantLock();
    private static final Lock outlock = new ReentrantLock();

    private static class IOSCache {

        static class IOInfo {

            volatile boolean captured = false;
            private WeakReference<InputOutput> io = null;
            private String displayName;

            IOInfo(String displayName) {
                this.displayName = displayName;
            }

            boolean capture() {
                if (captured || getInOut() == null) {
                    return false;
                }
                captured = true;
                return true;
            }

            void free() {
                captured = false;
            }

            InputOutput getInOut() {
                synchronized (this) {
                    if (io == null) {
                        io = new WeakReference<InputOutput>(IOProvider.getDefault().getIO(displayName, null));
                    }
                    return io.get();
                }
            }
        }
        private static final Map<String, IOInfo> infos = new HashMap<String, IOInfo>();

        public static IOInfo getIO(String name) {
            synchronized (infos) {
                String lookupName = name;
                int index = 1;
                while (true) {
                    IOInfo info = infos.get(lookupName);
                    if (info != null) {
                        if (info.getInOut() == null) {
                            infos.remove(lookupName);
                        } else {
                            if (info.capture()) {
                                return info;
                            } else {
                                index++;
                                lookupName = name + " " + String.valueOf(index);
                            }
                        }
                    } else {
                        info = new IOInfo(lookupName);
                        infos.put(lookupName, info);
                        info.capture();
                        return info;
                    }
                }
            }
        }
    }

    private class StreamReader implements Runnable {

        private final InputStream stream;
        private final OutputWriter out;
        private final Lock lock;

        public StreamReader(InputStream stream, OutputWriter out, Lock lock) {
            this.stream = stream;
            this.out = out;
            this.lock = lock;

        }

        @Override
        public void run() {
            boolean locked = false;
            try {
                locked = lock.tryLock();
                byte[] buffer = new byte[512];
                while (true) {
                    try {
                        int count = stream.read(buffer);
                        if (count >= 0) {
                            out.append(new String(buffer, 0, count));
                            out.flush();
                        } else {
                            if (isDetached) {
                                return;
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }

                }
            } finally {
                if (locked) {
                    lock.unlock();
                }
            }
        }
    }
    private final StreamReader errReader;
    private final StreamReader outReader;
    private final ExecutorService service;
    private volatile boolean isDetached = false;
    private IOSCache.IOInfo ioInfo;

    DebuggerSessionConsole(String sessionName, VirtualMachine vm) {
        ioInfo = IOSCache.getIO(sessionName);
        io = ioInfo.getInOut();
        try {
            io.getOut().reset();
            io.getErr().reset();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }


        errReader = new StreamReader(vm.process().getErrorStream(), io.getErr(), errlock);
        outReader = new StreamReader(vm.process().getInputStream(), io.getOut(), outlock);

        service = Executors.newFixedThreadPool(2);
        synchronized (this) {
            service.submit(errReader);
            service.submit(outReader);
        }

        io.select();
    }

    public InputOutput getIO() {
        return io;
    }

    public void detach() {
        synchronized (this) {
            ioInfo.free();
            service.shutdown();
            isDetached = true;
            try {
                errlock.lock();
                outlock.lock();
                io.getOut().println();
                try {
                    IOColorLines.println(io, "User program finished", Color.BLUE);
                } catch (IOException ex) {
                    io.getOut().println("User program finished");
                }
            } finally {
                errlock.unlock();
                outlock.unlock();
            }
        }
    }
}

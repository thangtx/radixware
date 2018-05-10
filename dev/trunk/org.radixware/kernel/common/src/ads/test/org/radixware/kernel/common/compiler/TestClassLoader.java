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

package org.radixware.kernel.common.compiler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;
import org.radixware.kernel.common.utils.Utils;


class TestClassLoader extends ClassLoader {

    private final ClassFile[] classFiles;
    private final File branchDir;

    public TestClassLoader(File branchDir, ClassFile[] classFiles) {  //NOPMD      
        this.classFiles = classFiles;
        this.branchDir = branchDir;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        final Class c = findLoadedClass(name);
        if (c != null) {
            return c;
        }
        for (ClassFile cf : classFiles) {
            final String className = String.valueOf(CharOperation.concatWith(cf.getCompoundName(), '.'));
            if (Utils.equals(name, className)) {
                return defineClass(className, cf.getBytes(), 0, cf.getBytes().length);
            }
        }
        try {
            final byte[] bytes = lookup(branchDir, name);
            if (bytes != null) {
                return defineClass(name, bytes, 0, bytes.length);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
        return super.loadClass(name); //To change body of generated methods, choose Tools | Templates.
    }

    public void resolve(Class cl) {
        super.resolveClass(cl);
        try {
            cl.getDeclaredConstructors();
            cl.getDeclaredFields();
            cl.getDeclaredMethods();
            cl.getDeclaredClasses();
        } catch (Throwable e) {
            throw  e;
        }
    }

    private byte[] lookup(File dir, String className) throws IOException {
        if (dir.isDirectory()) {
            final File[] jars = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".jar");
                }
            });
            for (File jar : jars) {
                final JarFileDataProvider provider = JarFileDataProvider.getInstance(jar);
                final String entryName = className.replace(".", "/") + ".class";
                if (provider.entryExists(entryName)) {
                    return provider.getEntryData(entryName);
                }
            }
            final File[] subdirs = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
            for (File file : subdirs) {
                final byte[] result = lookup(file, className);
                if (result != null) {
                    return result;
                }
            }
            return null;
        } else {
            return null;
        }
    }
}

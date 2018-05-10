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
package org.radixware.kernel.common.svn.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.ClassReader;

/**
 *
 * @author npopov
 */
class UserFuncClassLoader extends ClassLoader {

    private final List<PatchClassFileLinkageVerifier.ClassInfo> classFiles;
    private final Set<String> notFoundClasses;
    private final ClassLoader branchClassLoader;
    private final NoizyVerifierEx verifier;
    private final int id;

    public UserFuncClassLoader(int id, ClassLoader branchClassLoader, NoizyVerifierEx verifier, List<PatchClassFileLinkageVerifier.ClassInfo> names, Set<String> notFoundClasses) {
        super(null);
        this.id = id;
        this.notFoundClasses = notFoundClasses;
        this.classFiles = names;
        this.branchClassLoader = branchClassLoader;
        this.verifier = verifier;
    }

    public boolean verify() {
        //1. Define all classes
        for (PatchClassFileLinkageVerifier.ClassInfo info : classFiles) {
            try {
                info.clazz = defineClass(info.name, info.bytes, 0, info.bytes.length);
            } catch (ClassFormatError | NoClassDefFoundError err) {
                verifier.error(info.name + " : " + err.getClass().getSimpleName() + " : " + err.getMessage());
            }
        }

        //2. Check all classes
        final StringBuilder sb = new StringBuilder();
        for (PatchClassFileLinkageVerifier.ClassInfo info : classFiles) {
            try (InputStream is = new ByteArrayInputStream(info.bytes)) {
                ClassReader cr = new ClassReader(is);
                ClassCheckVisitor cv = new ClassCheckVisitor(info.name, info.clazz, notFoundClasses, this, verifier);
                cr.accept(cv, 0);
                
                for (IClassCheckProblem p : cv.getProblems()) {
                    final String loc = String.format("[location: '%d).java:%d']", id, p.getLine());
                    sb.append(loc);
                    sb.append(" : ").append(p.getPrettySignature()).append(" : ");
                    if (p.getMessage() != null) {
                        sb.append(p.getMessage());
                    } else {
                        sb.append(p.getException().getClass().getSimpleName()).append(": ").append(p.getException().getMessage());
                    }
                    verifier.message(p.getSeverity(), sb.toString());
                    sb.setLength(0);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                //See: MANIMALSNIFFER-9
                verifier.error("Found corrupted class file: " + info.name);
            } catch (Exception ex) {
                verifier.error(info.name + " : " + ex.getClass().getSimpleName() + " : " + ex.getMessage());
            }
        }
        return true;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (PatchClassFileLinkageVerifier.ClassInfo info : classFiles) {
            if (info.name.equals(name)) {
                return info.clazz;
            }
        }
        final Class<?> c = branchClassLoader.loadClass(name);
        if (c != null) {
            return c;
        }
        throw new ClassNotFoundException(name);
    }
}

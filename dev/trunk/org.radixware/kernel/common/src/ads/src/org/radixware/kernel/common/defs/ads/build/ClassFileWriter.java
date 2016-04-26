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

package org.radixware.kernel.common.defs.ads.build;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.compiler.CompilerUtils;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport.EKind;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


final class ClassFileWriter extends JavaFileSupport.FileWriter {

    private final ClassFile[] classFiles;
    private final IJavaSource sourceRoot;

    ClassFileWriter(IJavaSource sourceRoot, ClassFile[] classFiles) {
        this.classFiles = classFiles;
        this.sourceRoot = sourceRoot;
    }

    @Override
    public boolean write(final File packagesRoot, final UsagePurpose purpose, final boolean force, IProblemHandler problemHandler, final Collection<File> writtenFiles) throws IOException {
        for (int i = 0; i < classFiles.length; i++) {
            final byte[] bytes = classFiles[i].getBytes();
            if (bytes != null && bytes.length > 0) {
                final char[][] names = classFiles[i].getCompoundName();
                File packageDir = packagesRoot;
                for (int p = 0; p < names.length - 1; p++) {
                    packageDir = new File(packageDir, String.valueOf(names[p]));
                    if (!packageDir.exists()) {
                        if (!packageDir.mkdirs()) {
                            return false;
                        }
                    }
                }
                final File binary = new File(packageDir, String.valueOf(names[names.length - 1]) + SuffixConstants.SUFFIX_STRING_class);
                CompilerUtils.writeBytes(binary, bytes);
                writtenFiles.add(binary);
            }
        }
        return true;
    }

    @Override
    public boolean canWrite(ERuntimeEnvironmentType env) {
        return true;
    }

    @Override
    public EKind getKind() {
        return EKind.SOURCE;
    }

    @Override
    public IJavaSource getJavaSource() {
        return sourceRoot;
    }

    @Override
    public boolean deletePackagesRootOnFail() {
        return false;
    }
}
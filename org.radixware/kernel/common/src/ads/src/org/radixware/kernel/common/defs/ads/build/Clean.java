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
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.module.IJavaModule;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.FileUtils;


public class Clean extends JavaAction {

    private int count;

    public Clean(IFlowLogger flowLogger) {
        super(flowLogger);
    }

    public <T extends Module & IJavaModule> void cleanAll(T module, IProblemHandler handler) {
        if (module.isReadOnly()) {
            return;
        }
        final File binDir = JavaFileSupport.getBinDir(module);
        doClean(module, binDir, handler);
        final File dir = JavaFileSupport.getSourceDir(module, ERuntimeEnvironmentType.COMMON);
        final File srcDir = dir.getParentFile();
        doClean(module, srcDir, handler);

        final File radixdoc = new File(module.getDirectory(), RadixdocConventions.RADIXDOC_ZIP_FILE);
        doClean(module, radixdoc, handler);
        
        final File radixdocXml = new File(module.getDirectory(), RadixdocConventions.RADIXDOC_XML_FILE);
        doClean(module, radixdocXml, handler);
    }

    public void report() {
        flowLogger.message("[clean] deleted " + count + " directories and files");
    }

    public <T extends Module & IJavaModule> void clean(T module, ERuntimeEnvironmentType env, IProblemHandler handler) {
        if (module.isReadOnly()) {
            return;
        }
        File dir = JavaFileSupport.getCompiledBinaryFile(module, env);
        doClean(module, dir, handler);
        dir = dir.getParentFile();
        if (dir.isDirectory()) {
            String[] files = dir.list();
            if (files == null || files.length == 0) {
                deleteEntry(dir);
            }
        }
        dir = JavaFileSupport.getSourceDir(module, env);
        doClean(module, dir, handler);
        dir = dir.getParentFile();
        if (dir.isDirectory()) {
            String[] files = dir.list();
            if (files == null || files.length == 0) {
                deleteEntry(dir);
            }
        }
    }

    private void deleteEntry(File file) {
        if (file.isDirectory()) {
            FileUtils.deleteDirectory(file);
            count++;
        } else {
            FileUtils.deleteFile(file);
            count++;
        }
    }

    private <T extends Module & IJavaModule> void doClean(T module, File file, IProblemHandler handler) {
        if (file != null && file.exists()) {
            deleteEntry(file);
            if (file.exists()) {
                handler.accept(RadixProblem.Factory.newError(module, "Unable to delete file: " + file.getAbsolutePath()));
            }
        }
    }

    @Override
    public String getDisplayName() {
        return "Clean";
    }
}

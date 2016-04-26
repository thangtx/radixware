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


package org.radixware.kernel.designer.tree.actions.dds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.radixware.kernel.common.repository.dds.DdsDatabaseScripts;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.subversion.util.RadixSvnUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

/**
 * RADIX-6977. Updater for directory structure of scripts. Remove folder for
 * concrete DB (oracel).
 *

 */
final class DdsScriptUpdater {

    private final DdsDatabaseScripts dbScripts;
    private List<File> updateSvnStatusfiles;
    
    public DdsScriptUpdater(DdsDatabaseScripts dbScripts) {
        this.dbScripts = dbScripts;
        
        updateSvnStatusfiles = new ArrayList<>();
    }

    public static boolean isUpgradeRequired(DdsDatabaseScripts dbScripts) {
        final DdsScripts scripts = dbScripts.getOwnerScripts();
        if (scripts != null) {
            final File scriptsDir = scripts.getDirectory();
            if (scriptsDir != null) {
                final String dirName = dbScripts.getDirectoryName();
                final File dbScript = new File(scriptsDir, dirName);

                return dbScript.exists();
            }
        }
        return false;
    }

    private Map<String, File> dirDump(String pref, File dir) {
        final Map<String, File> files = new LinkedHashMap<>();

        for (final File file : dir.listFiles()) {
            files.put(pref + file.getName(), file);
            if (file.isDirectory()) {
                final Map<String, File> subDir = dirDump(pref + file.getName() + "/", file);
                files.putAll(subDir);
            }
        }

        return files;
    }

    public void upgrade() {

        Map<String, File> actualDump = new LinkedHashMap<>();
        Map<String, File> oldDump = null;

        final List<File> removeDir = new ArrayList<>();

        for (final File file : dbScripts.getDirectory().listFiles()) {
            if (file.isDirectory()) {
                if (file.getName().equals("oracle")) {
                    oldDump = dirDump("", file);
                    removeDir.add(file);
                } else {
                    actualDump.put(file.getName(), file);
                    final Map<String, File> files = dirDump(file.getName() + "/", file);
                    actualDump.putAll(files);
                }
            } else {
                actualDump.put(file.getName(), file);
            }
        }

        if (oldDump == null) {
            return;
        }

        for (final Map.Entry<String, File> entry : oldDump.entrySet()) {
            if (!actualDump.containsKey(entry.getKey())) {
                try {
                    final File actual = new File(dbScripts.getDirectory(), entry.getKey());
                    final File old = entry.getValue();

                    if (old.isDirectory()) {
                        FileUtils.mkDirs(actual);
                        removeDir.add(old);
                    } else {
                        FileUtils.copyFile(old, actual);
                        final FileObject fileObject = RadixFileUtil.toFileObject(old);
                        fileObject.delete();
                    }
                    
                    updateSvnStatusfiles.add(old);
                    updateSvnStatusfiles.add(actual);
                } catch (IOException | SecurityException ex) {
                    Logger.getLogger(DdsScriptUpdater.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        Collections.reverse(removeDir);

        for (final File dir : removeDir) {
            final String[] files = dir.list();
            if (files == null || files.length == 0) {
                try {
                    final FileObject fileObject = RadixFileUtil.toFileObject(dir);
                    fileObject.delete();
                } catch (IOException | SecurityException e) {
                    //...
                }
            }
        }
        final FileObject fileObject = RadixFileUtil.toFileObject(dbScripts.getDirectory());
        fileObject.refresh();
        
        try {
            RadixSvnUtils.refreshStatus(updateSvnStatusfiles.toArray(new File[0]));
            RadixSvnUtils.refreshStatus(dbScripts.getDirectory());
        } catch (Exception e) {
            Logger.getLogger(DdsScriptUpdater.class.getName()).log(Level.WARNING, "Error on update svn status", e);
        }
    }
}

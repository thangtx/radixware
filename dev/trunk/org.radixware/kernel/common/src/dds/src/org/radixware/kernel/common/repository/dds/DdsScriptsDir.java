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

package org.radixware.kernel.common.repository.dds;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;

/**
 * Metainformation about 'pre', 'post' and 'upgrade' folder.
 *
 */
public abstract class DdsScriptsDir extends RadixObjects<DdsScript> implements IDirectoryRadixObject {

    protected DdsScriptsDir(final DdsDatabaseScripts ownerDatabaseScripts) {
        super(ownerDatabaseScripts);
        upload();//NOPMD
    }

    protected abstract String getDirectoryName();

    public DdsDatabaseScripts getOwnerDatabaseScripts() {
        return (DdsDatabaseScripts) getContainer();
    }

//    @Override
//    public File getDirectory() {
//        final DdsDatabaseScripts databaseScripts = getOwnerDatabaseScripts();
//        if (databaseScripts != null) {
//            final File databaseScriptsDir = databaseScripts.getDirectory();
//            if (databaseScriptsDir != null) {
//                final String directoryName = getDirectoryName();
//                return new File(databaseScriptsDir, directoryName);
//            }
//        }
//        return null;
//    }
    @Override
    public File getDirectory() {
        final DdsDatabaseScripts databaseScripts = getOwnerDatabaseScripts();
        if (databaseScripts != null) {
            File scriptsDir = databaseScripts.findFile(getDirectoryName());
            if (scriptsDir == null) {
                final File databaseScriptsDir = databaseScripts.getDirectory();
                if (databaseScriptsDir != null) {
                    scriptsDir = new File(databaseScriptsDir, getDirectoryName());
                }
            }

            return scriptsDir;
        }
        return null;
    }

    @Override
    public File getFile() {
        return getDirectory();
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    public final File[] collectFiles() {
        final File dir = getDirectory();
        if (dir == null) {
            return null; // loaded from input stream or not in branch
        } else {
            return dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(final File pathname) {
                    final String name = pathname.getName();
                    return name != null && name.endsWith(".sql");
                }
            });
        }

    }

    public DdsScript addFromFile(final File sqlFile) {
        final String fileNameWithoutExt = FileUtils.getFileBaseName(sqlFile);
        final DdsScript script = DdsScript.Factory.newPostPreUpdateScript(fileNameWithoutExt);
        add(script);
        return script;
    }

    private void upload() {
        final File[] sqlFiles = collectFiles();

        if (sqlFiles != null) {
            for (File sqlFile : sqlFiles) {
                addFromFile(sqlFile);
            }
        }
    }

    protected DdsScript findScriptByFileBaseName(String fileBaseName) {
        for (DdsScript script : this) {
            final String scriptFileBaseName = script.getFileBaseName();
            if (Utils.equals(scriptFileBaseName, fileBaseName)) {
                return script;
            }
        }
        return null;
    }

    public DdsScript findLastByNumberedName(final boolean reverse) {
        DdsScript result = null;
        long max = 0;

        for (DdsScript script : this) {
            try {
                final DdsUpdateInfo updateInfo = getOwnerDatabaseScripts().getOwnerScripts().getUpdatesInfo().findByUpdateFileName(script.getFileBaseName());
                if (updateInfo == null) {
                    continue;
                }
                
                final long number = Long.parseLong(script.getName());
                if (result == null || number > max) {
                    result = script;
                    max = number;
                }
            } catch (NumberFormatException e) {
                Logger.getLogger(DdsScriptsDir.class.getName()).log(Level.FINE, "Unexpected file name " + script.getName() + " in scripts dir", e);
            }

        }
        return result;
    }

    public DdsScript addNew(final boolean reverse) {
        final DdsScript lastScript = findLastByNumberedName(reverse);
        final long nextNumber;

        if (lastScript != null) {
            final long number = Long.parseLong(lastScript.getName());
            nextNumber = number + 1;
        } else {
            nextNumber = 1;
        }

        final String newName = String.valueOf(nextNumber);
        final DdsScript newScript = DdsScript.Factory.newPostPreUpdateScript(newName);
        add(newScript);

        return newScript;
    }

    @Override
    public String getTypesTitle() {
        return super.getTypeTitle(); // prevent scriptses
    }

    /**
     * Metainformation about 'upgrade' folder.
     */
    private static class UpgradeScripts extends DdsScriptsDir {

        protected UpgradeScripts(DdsDatabaseScripts ownerDatabaseScripts) {
            super(ownerDatabaseScripts);
        }

        @Override
        protected String getDirectoryName() {
            return "upgrades";
        }

        @Override
        public String getName() {
            return "Upgrades";
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.SQL_UPGRADE_SCRIPTS;
        }
    }

    /**
     * Metainformation about 'pre' folder.
     */
    private static class PreScripts extends DdsScriptsDir {

        protected PreScripts(DdsDatabaseScripts ownerDatabaseScripts) {
            super(ownerDatabaseScripts);
        }

        @Override
        protected String getDirectoryName() {
            return "pre";
        }

        @Override
        public String getName() {
            return "Pre";
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.SQL_PRE_SCRIPTS;
        }
    }

    /**
     * Metainformation about 'post' folder.
     */
    private static class PostScripts extends DdsScriptsDir {

        protected PostScripts(DdsDatabaseScripts ownerDatabaseScripts) {
            super(ownerDatabaseScripts);
        }

        @Override
        protected String getDirectoryName() {
            return "post";
        }

        @Override
        public String getName() {
            return "Post";
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.SQL_POST_SCRIPTS;
        }
    }

    static class Factory {

        private Factory() {
        }

        static DdsScriptsDir newPreScriptsDir(DdsDatabaseScripts ownerDatabaseScripts) {
            return new PreScripts(ownerDatabaseScripts);
        }

        static DdsScriptsDir newPostScriptsDir(DdsDatabaseScripts ownerDatabaseScripts) {
            return new PostScripts(ownerDatabaseScripts);
        }

        static DdsScriptsDir newUpgradeScriptsDir(DdsDatabaseScripts ownerDatabaseScripts) {
            return new UpgradeScripts(ownerDatabaseScripts);
        }
    }
}

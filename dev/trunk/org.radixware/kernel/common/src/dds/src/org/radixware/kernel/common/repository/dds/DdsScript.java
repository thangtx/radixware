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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo.BaseLayerInfo;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.FileUtils;

/**
 * Metainformation about SQL script file (install, upgrade, pre, post).
 * DdsSegment.Scripts.Oracle.Create DdsSegment.Scripts.Oracle.Install
 * DdsSegment.Scripts.Oracle.Post[].Script
 * DdsSegment.Scripts.Oracle.Pre[].Script
 * DdsSegment.Scripts.Oracle.Upgrade[].Script
 *
 */
public class DdsScript extends RadixObject {

    private final String fileNameWithoutExtension;

    protected DdsScript(String fileNameWithoutExtension) {
        super();
        this.fileNameWithoutExtension = fileNameWithoutExtension;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public RadixIcon getIcon() {
        if (getOwnerDatabaseScripts() != null && getOwnerDatabaseScripts().getOwnerScripts() != null) {
            DdsScripts.UpdatesInfo upInfo = getOwnerDatabaseScripts().getOwnerScripts().getUpdatesInfo();
            for (DdsUpdateInfo info : upInfo) {
                if (info.findScript() == this) {
                    if (info.isReverse()) {
                        return DdsDefinitionIcon.SQL_REVERSE_SCRIPT;
                    } else {
                        break;
                    }
                }
            }
        }

        return DdsDefinitionIcon.SQL_SCRIPT;
    }

    public String getFileBaseName() {
        return fileNameWithoutExtension + ".sql";
    }

    protected String getFileNameWithoutExtension() {
        return fileNameWithoutExtension;
    }

    protected void onDelete() {
    }

    @Override
    public boolean delete() {
        final File file = getFile();
        if (file != null) {
            try {
                FileUtils.deleteFileExt(file);
            } catch (IOException cause) {
                throw new RadixObjectError("Unable to delete script.", this, cause);
            }
        }

        onDelete();

        return super.delete();
    }

    public DdsDatabaseScripts getOwnerDatabaseScripts() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof DdsDatabaseScripts) {
                return (DdsDatabaseScripts) owner;
            }
        }
        return null;
    }

    public String getContent() throws IOException {
        final File file = getFile();
        assert file != null;

        final String result = FileUtils.readTextFile(file, FileUtils.SQL_ENCODING);
        return result;
    }

    public void setContent(final String content) throws IOException {
        final File file = getFile();
        assert file != null;

//        final File dir = file.getParentFile();
        //FileUtils.mkDirs(dir);

        FileUtils.writeString(file, content, FileUtils.SQL_ENCODING);
    }

    @Override
    public File getFile() {
        final DdsDatabaseScripts databaseScripts = getOwnerDatabaseScripts();
        if (databaseScripts != null) {
            File installDir = databaseScripts.findFile("install");

            if (installDir == null) {
                final File databaseScriptsDir = databaseScripts.getDirectory();
                if (databaseScriptsDir != null) {
                    installDir = new File(databaseScriptsDir, "install");
                }
            }
            if (installDir != null) {
                final String fileBaseName = getFileBaseName();
                return new File(installDir, fileBaseName);
            }
        }
        return null;
    }
    public static final String SCRIPT_TYPE_TITLE = "Script";
    public static final String SCRIPT_TYPES_TITLE = "Scripts";

    @Override
    public String getTypeTitle() {
        return SCRIPT_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return SCRIPT_TYPES_TITLE;
    }

    /**
     * Metainformation about user creation SQL file ('0.sql').
     */
    private static class CreateScript extends DdsScript {

        public CreateScript(DdsDatabaseScripts ownerDatabaseScripts) {
            super("0");
            setContainer(ownerDatabaseScripts);
        }

        @Override
        public String getName() {
            return "Create";
        }
//        @Override
//        protected File getFileToWrite() {
//            final DdsDatabaseScripts databaseScripts = getOwnerDatabaseScripts();
//            if (databaseScripts != null) {
//                final File databaseScriptsDir = databaseScripts.getDirectory();
//                if (databaseScriptsDir != null) {
//                    final File installDir = new File(databaseScriptsDir, "install");
//                    final String fileBaseName = getFileBaseName();
//                    return new File(installDir, fileBaseName);
//                }
//            }
//            return null;
//        }
    }

    /**
     * Metainformation about installation SQL file ('1.sql').
     */
    private static class InstallScript extends DdsScript {

        protected InstallScript(DdsDatabaseScripts ownerDatabaseScripts) {
            super("1");
            setContainer(ownerDatabaseScripts);
        }

        @Override
        public String getName() {
            return "Install";
        }
    }

    private static class PrePostUpdateScript extends DdsScript {

        protected PrePostUpdateScript(final String fileNameWithoutExtension) {
            super(fileNameWithoutExtension);
        }

        /**
         * @return file name without extension
         */
        @Override
        public String getName() {
            return getFileNameWithoutExtension();
        }

        public DdsScriptsDir getOwnerScriptsDir() {
            return (DdsScriptsDir) getContainer();
        }

        @Override
        public File getFile() {
            final DdsScriptsDir scriptsDir = getOwnerScriptsDir();
            if (scriptsDir != null) {
                final File dir = scriptsDir.getDirectory();
                if (dir != null) {
                    final String fileBaseName = getFileBaseName();
                    return new File(dir, fileBaseName);
                }
            }
            return null;
        }

        private Layer getOwnerLayer() {
            for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
                if (container instanceof Layer) {
                    return (Layer) container;
                }
            }
            return null;
        }

        public DdsScript findPrevious() {
            final long thisVersion;
            try {
                thisVersion = Long.parseLong(getName());
            } catch (NumberFormatException ex) {
                return null;
            }

            final DdsScriptsDir scriptsDir = getOwnerScriptsDir();
            if (scriptsDir == null) {
                return null;
            }

            DdsScript result = null;
            long maxVersion = 0;
            for (DdsScript siblink : scriptsDir) {
                try {
                    long version = Long.parseLong(siblink.getName());
                    if (version > maxVersion && version < thisVersion) {
                        maxVersion = version;
                        result = siblink;
                    }
                } catch (NumberFormatException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            return result;
        }

        @Override
        protected void onDelete() {
            // fix current scripts.xml - remove all references to current file
            final DdsDatabaseScripts dbScripts = getOwnerDatabaseScripts();
            if (dbScripts != null) {
                final DdsScripts scripts = dbScripts.getOwnerScripts();
                for (DdsUpdateInfo updateInfo : scripts.getUpdatesInfo()) {
                    if (updateInfo.findScript() == this) {
                        updateInfo.delete();
                        try {
                            scripts.save();
                        } catch (IOException cause) {
                            throw new RadixObjectError("Unable to save scripts.xml", scripts, cause);
                        }
                    }
                }
            }

            // fix upper scripts.xml - move all references to previous file.
            final Layer curLayer = getOwnerLayer();
            final Branch branch = (curLayer != null ? curLayer.getBranch() : null);
            if (branch != null) {
                for (Layer layer : branch.getLayers().getTopsFirst()) {
                    final DdsSegment segment = (DdsSegment) layer.getDds();
                    final DdsScripts scripts = segment.getScripts();
                    for (DdsUpdateInfo updateInfo : scripts.getUpdatesInfo()) {
                        for (BaseLayerInfo baseLayerInfo : updateInfo.getBaseLayersInfo()) {
                            if (baseLayerInfo.findLastUpdateScript() == this) {
                                final DdsScript previous = findPrevious();
                                if (previous != null) {
                                    baseLayerInfo.setLastUpdateFileName(previous.getFile().getName());
                                } else {
                                    baseLayerInfo.delete();
                                }
                                try {
                                    scripts.save();
                                } catch (IOException cause) {
                                    throw new RadixObjectError("Unable to save scripts.xml", scripts, cause);
                                }
                            }
                        }
                    }
                }
            }
        }
        // RADIX-1539  disable delete if layer above refferes to this script
        // commented by RADIX-1974
//        @Override
//        public boolean canDelete() {
//            if (!super.canDelete()) {
//                return false;
//            }
//
//            final Layer curLayer = getOwnerLayer();
//            if (curLayer == null) {
//                return true;
//            }
//
//            final Branch branch = curLayer.getBranch();
//            if (branch == null) {
//                return true;
//            }
//
//            for (Layer layer = branch.getLayers().findTop(); layer != null && layer != curLayer; layer = layer.findPrevLayer()) {
//                final DdsSegment segment = (DdsSegment) layer.getDds();
//                final DdsScripts scripts = segment.getScripts();
//                if (scripts.isReffersTo(this)) {
//                    return false;
//                }
//            }
//
//            return true;
//        }
    }

    public static final class Factory {

        private Factory() {
        }

        static DdsScript newCreateScript(DdsDatabaseScripts ownerDatabaseScripts) {
            return new CreateScript(ownerDatabaseScripts);
        }

        static DdsScript newInstallScript(DdsDatabaseScripts ownerDatabaseScripts) {
            return new InstallScript(ownerDatabaseScripts);
        }

        public static DdsScript newPostPreUpdateScript(final String fileNameWithoutExtension) {
            return new PrePostUpdateScript(fileNameWithoutExtension);
        }
    }
}

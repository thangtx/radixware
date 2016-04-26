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
import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.resources.icons.RadixIcon;

/**
 * Metainformation about scripts folder in DDS segment for concrete database
 * (oracle, ...).
 *
 */
public abstract class DdsDatabaseScripts extends RadixObject implements IDirectoryRadixObject {

    protected DdsDatabaseScripts(DdsScripts ownerScripts) {
        super();
        setContainer(ownerScripts);
    }

    public DdsScripts getOwnerScripts() {
        return (DdsScripts) getContainer();
    }

    public abstract String getDirectoryName();

    @Override
    public File getDirectory() {
        final DdsScripts scripts = getOwnerScripts();
        if (scripts != null) {
            final File scriptsDir = scripts.getDirectory();
            return scriptsDir;
//            if (scriptsDir != null) {
//                final String dirName = getDirectoryName();
//                return new File(scriptsDir, dirName);
//            }
        }
        return null;
    }

    @Override
    public File getFile() {
        return getDirectory();
    }

    /**
     * Finds specified file in various locations, dependent on script version
     * format. Start search from new format.
     *
     * @param name Name of specified file
     * @return found file or null.
     */
    File findFile(String name) {

        final File[] directories = getDirectories();

        for (final File dir : directories) {
            final File file = new File(dir, name);

            if (file.exists()) {
                return file;
            }
        }

        return null;
    }

    private File[] getDirectories() {

        final DdsScripts scripts = getOwnerScripts();
        if (scripts != null) {

            final File scriptsDir = scripts.getDirectory();
            if (scriptsDir != null) {
                final String dirName = getDirectoryName();
                final File dbScripts = new File(scriptsDir, dirName);

                if (dbScripts.exists()) {
                    return new File[]{scriptsDir, dbScripts};
                } else {
                    return new File[]{scriptsDir};
                }
            }
        }
        return new File[0];
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
    private final DdsScript createScript = DdsScript.Factory.newCreateScript(this);

    public DdsScript getCreateScript() {
        return createScript;
    }
    private final DdsScript installScript = DdsScript.Factory.newInstallScript(this);

    public DdsScript getInstallScript() {
        return installScript;
    }
    private DdsScriptsDir upgradeScripts = null;

    public DdsScriptsDir getUpgradeScripts() {
        synchronized (this) {
            if (upgradeScripts == null) {
                upgradeScripts = DdsScriptsDir.Factory.newUpgradeScriptsDir(this);
            }
            return upgradeScripts;
        }
    }
    private DdsScriptsDir preScripts = null;

    public DdsScriptsDir getPreScripts() {
        synchronized (this) {
            if (preScripts == null) {
                preScripts = DdsScriptsDir.Factory.newPreScriptsDir(this);
            }

            return preScripts;
        }
    }
    private DdsScriptsDir postScripts = null;

    public DdsScriptsDir getPostScripts() {
        synchronized (this) {
            if (postScripts == null) {
                postScripts = DdsScriptsDir.Factory.newPostScriptsDir(this);
            }

            return postScripts;
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getCreateScript().visit(visitor, provider);
        getInstallScript().visit(visitor, provider);
        getPreScripts().visit(visitor, provider);
        getPostScripts().visit(visitor, provider);
        getUpgradeScripts().visit(visitor, provider);
    }

    /**
     * Metainformation about Oracle scripts folder.
     */
    private static class OracleScripts extends DdsDatabaseScripts {

        protected OracleScripts(final DdsScripts ownerScripts) {
            super(ownerScripts);
        }

        @Override
        public String getName() {
            return "Scripts";
        }

        @Override
        public String getDirectoryName() {
            return "oracle";
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.ORACLE;
        }
    }

    static class Factory {

        static DdsDatabaseScripts newOracleScripts(final DdsScripts ownerScripts) {
            return new OracleScripts(ownerScripts);
        }
    }
}

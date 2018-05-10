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
package org.radixware.kernel.common.builder.release;

import java.io.IOException;
import java.util.logging.Level;
import org.radixware.kernel.common.builder.release.ReleaseUtils.ReleaseComponentManager;

import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.ISvnFSClient;

class ReleaseFlow {

    public ReleaseFlow(ReleaseSettings settings) {
        this.settings = settings;
    }
    /**
     * Flags for test purpose only
     */
    boolean prepareScripts = true;
    boolean prepareBinaies = true;
    boolean releaseSrc = true;
    boolean releaseSrcSrc = true;
    private SVNRepositoryAdapter repository;
    private long revision = 0;
    private final ReleaseSettings settings;
    private byte[] releaseDescriptionFileContent;
    private SVNRepositoryAdapter.Editor editor = null;
    String sourceBranchUrl;
    String targetReleaseUrl;
    private ReleaseUtils.ReleaseComponentManager componentManager = null;

    public ReleaseSettings getSettings() {
        return settings;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public SVNRepositoryAdapter getRepository() {
        return repository;
    }

    public void setRepository(SVNRepositoryAdapter repository) {
        this.repository = repository;
    }

    public byte[] getReleaseDescriptionFileContent() {
        return releaseDescriptionFileContent;
    }

    public void setReleaseDescriptionFileContent(byte[] releaseDescriptionFileContent) {
        this.releaseDescriptionFileContent = releaseDescriptionFileContent;
    }

    public boolean isPatchRelease() {
        return settings.isPatchRelease();
    }

    public SVNRepositoryAdapter.Editor getEditor() throws RadixSvnException {
        if (editor == null) {
            editor = repository.createEditor("Release version " + settings.getNumber() + " (release based on revision " + revision + ")");
        }
        return editor;
    }

    public void cancel() {
        if (editor != null) {
            try {
                editor.cancel();
            } catch (RadixSvnException ex) {
                java.util.logging.Logger.getLogger(ReleaseFlow.class.getName()).log(Level.SEVERE, "Error on cancel changes", ex);
            }

            editor = null;
        }
    }

    public boolean commit(ISvnFSClient clientManager) {
        if (editor != null) {
            try {
                boolean done = false;
                while (!done) {
                    try {
                        settings.getLogger().message("Commiting changes...");
                        editor.commit();
                        done = true;
                    } catch (RadixSvnException e) {
                        if (e.isIOError()) {
                            if (!settings.getLogger().confirmation("Error on release commit: " + e.getMessage() + "\nTry again?")) {
                                throw e;
                            }
                        } else {
                            throw e;
                        }
                    }
                }
                settings.getLogger().message("Updating repository...");
                clientManager.update(settings.getBranch().getDirectory(), clientManager.getHeadRevision(), true);
                //clientManager.getUpdateClient().doUpdate(settings.getBranch().getDirectory(), SVNRevision.HEAD, SVNDepth.INFINITY, false, false);
                try {
                    settings.getBranch().reloadDescription();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(ReleaseFlow.class.getName()).log(Level.SEVERE, "Error on reload branch after release", ex);
                }
                settings.getLogger().success();
            } catch (RadixSvnException e) {
                e.printStackTrace();
                cancel();
                return settings.getLogger().fatal(new RadixError("Unable to commit changes", e));
            }
        }
        return true;
    }

    ReleaseComponentManager getComponentManager() {
        return componentManager;
    }

    void setComponentManager(ReleaseComponentManager componentManager) {
        this.componentManager = componentManager;
    }
}

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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.zip.ZipFile;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

public abstract class UpgradeCompatibilityVerifier implements NoizyVerifier {

    private final SVNRepositoryAdapter repository;
    private final String branchPath;
    private final ZipFile upgradeFile;
    private final String upgradeFilePath;
    private final boolean skipDevelopmentLayers;
    private final String predefinedBaseDevLayerURI;

    public UpgradeCompatibilityVerifier(SVNRepositoryAdapter repository, String branchPath, ZipFile upgradeFile, String baseDevLayerURI, boolean skipDevelopmentLayers) {
        this.repository = repository;
        this.branchPath = branchPath;
        this.upgradeFile = upgradeFile;
        this.skipDevelopmentLayers = skipDevelopmentLayers;
        this.upgradeFilePath = null;
        this.predefinedBaseDevLayerURI = baseDevLayerURI;
    }

    public UpgradeCompatibilityVerifier(SVNRepositoryAdapter repository, String branchPath, ZipFile upgradeFile, boolean skipDevelopmentLayers) {
        this.repository = repository;
        this.branchPath = branchPath;
        this.upgradeFile = upgradeFile;
        this.skipDevelopmentLayers = skipDevelopmentLayers;
        this.upgradeFilePath = null;
        this.predefinedBaseDevLayerURI = Utils.NO_BASE_DEVELOPMENT_LAYER_SPECIFIED;
    }

    public UpgradeCompatibilityVerifier(SVNRepositoryAdapter repository, String branchPath, String upgradeFilePath, boolean skipDevelopmentLayers) {
        this.repository = repository;
        this.branchPath = branchPath;
        this.upgradeFilePath = upgradeFilePath;
        this.skipDevelopmentLayers = skipDevelopmentLayers;
        this.upgradeFile = null;
        this.predefinedBaseDevLayerURI = Utils.NO_BASE_DEVELOPMENT_LAYER_SPECIFIED;
    }

    public static interface IClassLinkageProblemHandler {

        public boolean canIgnoreProblem(final Class c, final String problem);
    }

    private IClassLinkageProblemHandler classLinkageProblemHandler = null;

    final public void setClassLinkageProblemHandler(IClassLinkageProblemHandler classLinkageProblemHandler) {
        this.classLinkageProblemHandler = classLinkageProblemHandler;
    }

    private ICancellable cancellableHandler = null;

    final public void setCancellableHandler(ICancellable cancellableHandler) {
        this.cancellableHandler = cancellableHandler;
    }

    @Override
    public boolean verify() {
        BranchHolder holder = new BranchHolder(this, skipDevelopmentLayers);
        try {
            if (!holder.initialize(repository, branchPath, upgradeFile, upgradeFilePath, predefinedBaseDevLayerURI, skipDevelopmentLayers)) {
                return false;
            }
            message("Checking Usages-API compatibility...");
            Usages2APIVerifier apiVerifier = createUsages2APIVerifier();
            if (!apiVerifier.verifyExternalHolder(holder)) {
                return false;
            }
            message("Checking binary compatibility...");
            PatchClassFileLinkageVerifier cfVerifier = createCFVerifier();

            return cfVerifier.verifyExternalHolder(holder);
        } finally {
            holder.finish();
        }
    }

    private Usages2APIVerifier createUsages2APIVerifier() {
        return new Usages2APIVerifier(repository, branchPath, upgradeFile, upgradeFilePath, skipDevelopmentLayers, predefinedBaseDevLayerURI) {

            @Override
            public boolean cancel() {
                if (cancellableHandler != null) {
                    return cancellableHandler.cancel();
                }
                return super.cancel();
            }

            @Override
            public boolean wasCancelled() {
                if (cancellableHandler != null) {
                    return cancellableHandler.wasCancelled();
                }
                return super.wasCancelled();
            }

            @Override
            public void error(Exception e) {
                UpgradeCompatibilityVerifier.this.error(e);
            }

            @Override
            public void error(String message) {
                UpgradeCompatibilityVerifier.this.error(message);
            }

            @Override
            public void message(String message) {
                UpgradeCompatibilityVerifier.this.message(message);
            }
        };
    }

    PatchClassFileLinkageVerifier createCFVerifier() {
        return new PatchClassFileLinkageVerifier(repository, branchPath, upgradeFile, upgradeFilePath, skipDevelopmentLayers, predefinedBaseDevLayerURI, false) {

            @Override
            protected boolean canIgnoreProblem(final Class c, final String problem) {
                if (classLinkageProblemHandler != null) {
                    return classLinkageProblemHandler.canIgnoreProblem(c, problem);
                }
                return super.canIgnoreProblem(c, problem);
            }

            @Override
            public boolean cancel() {
                if (cancellableHandler != null) {
                    return cancellableHandler.cancel();
                }
                return super.cancel();
            }

            @Override
            public boolean wasCancelled() {
                if (cancellableHandler != null) {
                    return cancellableHandler.wasCancelled();
                }
                return super.wasCancelled();
            }

            @Override
            public void error(Exception e) {
                UpgradeCompatibilityVerifier.this.error(e);
            }

            @Override
            public void error(String message) {
                UpgradeCompatibilityVerifier.this.error(message);
            }

            @Override
            public void message(String message) {
                UpgradeCompatibilityVerifier.this.message(message);
            }
        };
    }

    public boolean verifyUserFunctions(Connection c) {
        try {
            message("Verifing user functions at " + c.getMetaData().getURL());
        } catch (SQLException ex) {
            //ignore
        }
        BranchHolder holder = new BranchHolder(this, skipDevelopmentLayers);
        try {
            if (!holder.initialize(repository, branchPath, upgradeFile, upgradeFilePath, predefinedBaseDevLayerURI, skipDevelopmentLayers)) {
                return false;
            }
            message("Checking Usages-API compatibility...");
            Usages2APIVerifier verifier = createUsages2APIVerifier();
            if (!verifier.verifyUserFunctions(c, holder)) {
                return false;
            }
            message("Checking binary compatibility...");
            PatchClassFileLinkageVerifier cfVerifier = createCFVerifier();
            return cfVerifier.verifyUserFunctions(c, holder);
        } finally {
            holder.finish();
        }

    }
}

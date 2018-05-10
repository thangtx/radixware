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
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.openide.windows.InputOutput;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.subversion.util.SvnBridge;

public class DdsModuleCancelCaptureAction extends CookieAction {

    public static class Cookie implements Node.Cookie, Runnable {

        private DdsModule module;
        private final boolean quiet;

        public Cookie(DdsModule module) {
            this.module = module;
            this.quiet = false;
        }
        
        public Cookie(DdsModule module, boolean quiet) {
            this.module = module;
            this.quiet = quiet;
        }

        @Override
        public void run() {
            if (!module.isInBranch()) {
                return; // was deleted
            }

            final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("DDS Structure Capture Cancellation");
            progressHandle.start();

            try {
                progressHandle.progress("Initializing");

                final DdsModelDef modifiedModel = module.getModelManager().getModifiedModel();
                if (modifiedModel != null) {
                    final DdsModelDef.ModifierInfo modifierInfo = modifiedModel.getModifierInfo();
                    final String editor = String.valueOf(modifierInfo.getEditor());
                    final String station = String.valueOf(modifierInfo.getStation());
                    String message = "DDS module '" + module.getName() + "' is captured by '" + editor + "' on '" + station + "'.\nCancel capture?";
                    if (modifiedModel.getEditState() != EEditState.NONE) {
                        message += "\nALL UNSAVED CHANGES WILL BE LOST!";
                    }
                    if (quiet || DialogUtils.messageConfirmation(message)) {
                        progressHandle.progress("Cancel capture");

                        if (!cancelCaptureImpl()) {
                            return;
                        }

                        module.getModelManager().cancelCapture();
                        NodesManager.updateOpenedEditors();
                    }
                }
            } catch (IOException | RadixSvnException | NoSuchAlgorithmException cause) {
                DefinitionError error = new DefinitionError("Unable to cancel capture of DDS module.", module, cause);
                DialogUtils.messageError(error);
            } finally {
                progressHandle.finish();
            }
        }

        public void cancelCapture() {
            RadixMutex.writeAccess(this);
        }

        private boolean cancelCaptureImpl() throws RadixSvnException, ISvnFSClient.SvnFsClientException, IOException, NoSuchAlgorithmException {

            final ISvnFSClient client = SvnBridge.getClientAdapter(module.getDirectory());
            final SVNRepositoryAdapter repository = DdsModuleCaptureAction.Cookie.getRepository(client, module);
            
            boolean isNewModule = SVN.isUnversionedSvnStatus(client, module.getDirectory());

            if (isNewModule){
                module.delete();
                return false;
            }
            
            if (repository == null) {
                return false;
            }
            try {
                final String moduleUrl;
                try {
                    moduleUrl = SVN.getFileUrl(client, module.getDirectory());
                } catch (ISvnFSClient.SvnFsClientException e) {
//                    if (e.getErrorMessage() != null && e.getErrorMessage().getErrorCode() == SVNErrorCode.WC_PATH_NOT_FOUND) {
//                        //module was not commited
//                        return true;
//                    }
                    return false;
                }
                String repoPath = repository.getLocalPath();
                String repositoryPath = SvnPath.append(repository.getRepositoryRoot(), repoPath);
                final String path = SvnPath.getRelativePath(repositoryPath,
                        org.radixware.kernel.common.svn.client.SvnPath.append(moduleUrl, DdsModelManager.MODIFIED_MODEL_XML_FILE_NAME));

                final boolean fileExists = SVN.isFileExists(repository, path);

                if (!fileExists) {
                    return true;
                }

                SVNRepositoryAdapter.Editor editor;
                try {
                    final SVNRepositoryAdapter.Editor testEditor = repository.createEditor("Check write access");
                    testEditor.cancel();

                    final String message = String.format("[internal] Cancel capture module %s by %s", module.getName(), System.getProperty("user.name"));

                    editor = repository.createEditor(message);
                } catch (RadixSvnException ex) {
                    final String message = "Write access to '" + repository.getLocation() + "' not exist";
                    Logger.getLogger(DdsModuleCaptureAction.class.getName()).log(Level.INFO, message, ex);
                    DialogUtils.messageError(message);
                    return false;
                }

                SVN.revert(client, new File(module.getDirectory(), DdsModelManager.MODIFIED_MODEL_XML_FILE_NAME));
                final File sqmlDefsFile = new File(module.getDirectory(), FileUtils.SQML_DEFINITIONS_XML_FILE_NAME);
                try {
                    if (sqmlDefsFile.isFile()) {
                        SVN.revert(client, sqmlDefsFile);
                    }
                } catch (ISvnFSClient.SvnFsClientException ex) {
                    Logger.getLogger(DdsModuleCaptureAction.class.getName()).log(Level.INFO, 
                            "Could not revert " + FileUtils.SQML_DEFINITIONS_XML_FILE_NAME, ex);
                }
                // SVN.getDirLatestRevision(repository)
                String entryPath = SvnPath.append(repoPath, path);
                editor.deleteEntry(entryPath, -1);
                editor.commit();

                SVN.update(client, module.getDirectory());

                return true;
            } finally {
                repository.close();
            }
        }
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        for (Node node : nodes) {
            Cookie cookie = node.getCookie(DdsModuleCancelCaptureAction.Cookie.class);
            if (cookie != null) {
                cookie.cancelCapture();
            }
        }
    }

    @Override
    public String getName() {
        return "Cancel Capture"; // TODO: translate
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true; // to prevent deadlock, because tree can be recreated
    }
}

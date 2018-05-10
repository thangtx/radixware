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
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.AuditTriggersUpdater;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.translate.SqmlTranslator;
import org.radixware.kernel.common.svn.FinalSvnEditor;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.SvnAuthType;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.designer.subversion.util.RadixSvnUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.subversion.util.SvnBridge;
import org.tigris.subversion.svnclientadapter.SVNClientException;

public class DdsModuleCaptureAction extends CookieAction {

    public static class Cookie implements Node.Cookie, Runnable {

        private DdsModule module;
        private final boolean recapture;
        private Status status = Status.NONE;
        public enum Status {
            NONE, COMPLITE, RECAPTURE, LOCALE_DELETE, IS_NOT_UP_TO_DATE
        }

        public Cookie(DdsModule module) {
            this.module = module;
            this.recapture = true;
        }

        public Cookie(DdsModule module, boolean recapture) {
            this.module = module;
            this.recapture = recapture;
        }
        
        private static void actualizeAllSqml(DdsModelDef module) {
            final SqmlTranslator sqmlTranslator = SqmlTranslator.Factory.newInstance();

            module.visitAll(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (radixObject instanceof Sqml) {
                        final Sqml sqml = (Sqml) radixObject;
                        if (!sqml.isReadOnly()) {// for example, auto-created triggers
                            sqmlTranslator.actualize(sqml);
                        }
                    } else if (radixObject instanceof DdsTableDef) {
                        // Закоментари актуализацию скриптов аудита, т.к. она модифицирует таблицу, даже если не было изменений.
                        // Раскоментарил, т.к. было исправлено
                        final DdsTableDef table = (DdsTableDef) radixObject;
                        AuditTriggersUpdater.update(table);
                    }
                }
            });
        }

        @Override
        public void run() {
            if (!module.isInBranch()) {
                return; // was deleted
            }
            if (module.getBranch().getType() == ERepositoryBranchType.PATCH) {
                DialogUtils.messageError("Database structure changes are not allowed in patches");
                return;
            }

            final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("DDS Structure Capturing");
            progressHandle.start();

            try {
                progressHandle.progress("Initializing");

                DdsModelDef modifiedModel = module.getModelManager().getModifiedModel();
                if (modifiedModel != null) {
                    DdsModelDef.ModifierInfo modifierInfo = modifiedModel.getModifierInfo();
                    if (!modifierInfo.isOwn()) {
                        final String editor = String.valueOf(modifierInfo.getEditor());
                        final String station = String.valueOf(modifierInfo.getStation());
                        final String message = "DDS module '" + module.getName() + "' is already captured by '" + editor + "' on '" + station + "'.\nRecapture?";
                        if (!recapture || !DialogUtils.messageConfirmation(message)) {
                            status = Status.RECAPTURE;
                            return;
                        }
                    }
                }

                progressHandle.progress("Check versioning status");
                final DdsSegment segment = module.getSegment();
                if (segment == null) {
                    return;
                }
                final Branch branch = module.getBranch();
                RadixSvnUtils radixSvnUtils = RadixSvnUtils.Factory.newInstance(branch);

                final File segmentDir = segment.getDirectory();
                if (radixSvnUtils != null && !radixSvnUtils.isUpToDate(segmentDir)) {
                    DialogUtils.messageError("Unable to capture DDS module '" + module.getName() + "'\n"
                            + "because it's owner DDS segment versioning status is not up to date.");
                    status = Status.IS_NOT_UP_TO_DATE;
                    return;
                }

                progressHandle.progress("Capturing");

                // создание model_modified.xml на Svn сервере
                if (!switchModelToModificationStateImpl()) {
                    return;
                }

                module.getModelManager().switchModelToModificationState();
                modifiedModel = module.getModelManager().getModifiedModel();

                actualizeAllSqml(modifiedModel);
                NodesManager.updateOpenedEditors();
                status = Status.COMPLITE;
            } catch (IOException | SVNClientException | RadixSvnException | NoSuchAlgorithmException cause) {
                DefinitionError error = new DefinitionError("Unable to capture DDS module.", module, cause);
                DialogUtils.messageError(error);
            } finally {
                progressHandle.finish();
            }
        }

        private boolean switchModelToModificationStateImpl() throws ISvnFSClient.SvnFsClientException, RadixSvnException, IOException, NoSuchAlgorithmException {

            final ISvnFSClient client = SvnBridge.getClientAdapter(module.getDirectory());
            final SVNRepositoryAdapter repository = getRepository(client, module);

            if (repository == null) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        DialogUtils.messageError("Unable to connect to Subversion repository. Check your auhentication options and try later");
                    }
                });
                return false;
            }
            try {

                final String moduleUrl;
                try {
                    moduleUrl = SVN.getFileUrl(client, module.getDirectory());
                    if (moduleUrl == null) {//this means module was not committed to subversion yet
                        return true;
                    }
                } catch (ISvnFSClient.SvnFsClientException e) {
//                    if (e.getErrorMessage() != null && e.getErrorMessage().getErrorCode() == SVNErrorCode.WC_PATH_NOT_FOUND) {
//                        //module was not commited
//                        return true;
//                    }
                    return false;
                }
                final String path = SvnPath.getRelativePath(repository.getLocation(),
                        SvnPath.append(moduleUrl, DdsModelManager.MODIFIED_MODEL_XML_FILE_NAME));

                final boolean fileExists = SVN.isFileExists(repository, path);

                final File localFile = new File(module.getDirectory(), DdsModelManager.MODIFIED_MODEL_XML_FILE_NAME);

                if (!localFile.exists() && fileExists) {
                    final String message = String.format("File %s exists in svn repository, but locally has been deleted."
                            + " If continue, remote file will be replaced by new locally file. Continue?", DdsModelManager.MODIFIED_MODEL_XML_FILE_NAME);
                    if (!recapture || !DialogUtils.messageConfirmation(message)) {
                        status = Status.LOCALE_DELETE;
                        return false;
                    }
                }

                FinalSvnEditor editor;
                try {
                    final FinalSvnEditor testEditor = new FinalSvnEditor(repository, "Check write access");
                    testEditor.abort();

                    final String message = fileExists ? String.format("Recapture module %s by %s", module.getName(), System.getProperty("user.name")) : String.format("Capture module %s by %s", module.getName(), System.getProperty("user.name"));

                    editor = new FinalSvnEditor(repository, message);
                } catch (RadixSvnException ex) {
                    final String message = "Write access to '" + repository.getLocation() + "' not exist";
                    Logger.getLogger(DdsModuleCaptureAction.class.getName()).log(Level.INFO, message, ex);
                    DialogUtils.messageError(message);
                    return false;
                }

                final byte[] modelModificationState = module.getModelManager().getModelModificationState();
                if (fileExists) {
                    editor.modifyFile(path, modelModificationState);
                } else {
                    editor.appendFile(path, modelModificationState);
                }
                editor.commit();

                SVN.update(client, module.getDirectory());

                return true;
            } finally {
                repository.close();
            }
        }

        public void switchToStructureModificationState() {
            RadixMutex.writeAccess(this);
        }

        static SVNRepositoryAdapter getRepository(ISvnFSClient client, DdsModule module) throws RadixSvnException {
            final String userName = SVN.SVNPreferences.getUserName();
            final SvnAuthType authType = SVN.SVNPreferences.getAuthType();
            final String sshKeyFile = SVN.SVNPreferences.getSSHKeyFilePath();

            final File branchDir = module.getBranch().getDirectory();

            try {
                return SVNRepositoryAdapter.Factory.newInstance(client, branchDir, userName, authType, sshKeyFile);
            } catch (RadixSvnException ex) {
                if (ex.isAuthenticationCancelled()) {
                    Logger.getLogger(DdsModuleCaptureAction.class.getName()).info("SVN authentication cancelled by user");
                    return null;
                } else {
                    throw ex;
                }

            }
        }

        public Status getStatus() {
            return status;
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
            final Cookie cookie = node.getCookie(DdsModuleCaptureAction.Cookie.class);
            if (cookie != null) {
                cookie.switchToStructureModificationState();
            }
        }
    }

    @Override
    public String getName() {
        return "Capture Structure"; // TODO: translate
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true; // to prevent deadlock because tree can be recreated
    }
}

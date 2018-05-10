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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.dialogs.IDialogStyler;
import org.radixware.kernel.common.enums.EReleaseStatus;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.utils.ReleaseVerifier;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.common.ReleaseUtils;

public class ReleaseSettings {

    public static void enableDbDependentScriptsMode() {
        ReleaseVerifier.enableDbDependentScriptsMode();
    }

    public static boolean isDbDependentScriptMode() {
        return ReleaseVerifier.isDbDependentScriptMode();
    }
    public boolean TEST_MODE = false;
    private EReleaseStatus status = EReleaseStatus.NEW;
    private String number;
    private String prevNumber;
    private final Branch sourceBranch;
    private IFlowLogger logger;
    private IBuildEnvironment buildEnvironment;
    private String userName = org.radixware.kernel.common.svn.utils.SVN.SVNPreferences.getUserName();
    private boolean performCleanAndBuild = true;
    private boolean verifyRelease = true;
    private boolean generateRadixdoc = true;
    private boolean isPatch = false;
    private String clientUri = "";
    private ESvnAuthType authType = SVN.getByAuthType(org.radixware.kernel.common.svn.utils.SVN.SVNPreferences.getAuthType());
    private String sshKeyFile = org.radixware.kernel.common.svn.utils.SVN.SVNPreferences.getSSHKeyFilePath();
    private Map<String, List<Id>> layer2ModuleId = new HashMap<>();
    private IDialogStyler dialogStyler;
    private ISvnFSClient svnClientAdapter;
    private IScriptDialog scriptDialog;

    public ReleaseSettings(Branch b, IFlowLogger logger, IBuildEnvironment buildEnv, boolean isPatch, ISvnFSClient svnClientAdapter) {
        this.sourceBranch = b;
        this.isPatch = isPatch;
        this.svnClientAdapter = svnClientAdapter;
        String prevNumberStr = b.getLastReleaseVersion();

        int[] prevVersion;
        int[] newVersion;

        if (prevNumberStr == null) {
            if (b.getType() == ERepositoryBranchType.OFFSHOOT || b.getType() == ERepositoryBranchType.PATCH || isPatch) {
                prevVersion = ReleaseUtils.parseVersionStr(b.getBaseReleaseVersion());
                newVersion = Arrays.copyOf(prevVersion, prevVersion.length + 1);
                newVersion[prevVersion.length] = 1;
            } else {
                prevVersion = new int[0];
                newVersion = new int[]{1, 0};
            }
        } else {
            if (isPatch) {
                prevVersion = ReleaseUtils.parseVersionStr(prevNumberStr);
                newVersion = Arrays.copyOf(prevVersion, prevVersion.length + 1);
                newVersion[prevVersion.length] = 1;
            } else {
                prevVersion = ReleaseUtils.parseVersionStr(prevNumberStr);
                if (prevVersion.length > 0) {
                    newVersion = Arrays.copyOf(prevVersion, prevVersion.length);
                    newVersion[newVersion.length - 1]++;
                } else {
                    newVersion = new int[]{1, 0};
                }
            }
        }
        this.prevNumber = ReleaseUtils.mergeVersionStr(prevVersion, isPatch);
        this.number = ReleaseUtils.mergeVersionStr(newVersion, isPatch);
        this.logger = logger;
        this.buildEnvironment = buildEnv;
    }

    public ESvnAuthType getAuthType() {
        return authType;
    }

    public void setAuthType(ESvnAuthType authType) {
        this.authType = authType;
    }

    public ISvnFSClient getSvnClientAdapter() {
        return svnClientAdapter;
    }

    public String getSSHKeyFile() {
        return sshKeyFile;
    }

//    public void setSSHKeyFile(String keyFile) {
//        this.sshKeyFile = keyFile;
//    }
    public String getUserName() {
        return userName;
    }

    public List<Id> getPatchModules(String layerURI) {
        return layer2ModuleId.get(layerURI);
    }

    Map<String, List<Id>> getPatchLayer2PatchModuleMap() {
        return layer2ModuleId;
    }

    public void setPatchModules(Map<String, List<Id>> moduleIdByLayer) {
        if (moduleIdByLayer != null && !moduleIdByLayer.isEmpty()) {
            layer2ModuleId.putAll(moduleIdByLayer);
        } else {
            layer2ModuleId.clear();
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public IFlowLogger getLogger() {
        return logger;
    }

    public String getNumber() {
        return number;
    }

    public String getPatchNumber() {
        if (isPatchRelease()) {
            int index = getNumber().indexOf("-");
            if (index > 0) {
                return getNumber().substring(index + 1);
            } else {
                return "";
            }
        } else {
            return null;
        }
    }

    public Branch getBranch() {
        return sourceBranch;
    }

    public String getClientURI() {
        return clientUri;
    }

    public void setClientURI(String uri) {
        clientUri = uri;
    }

    public boolean isPatchRelease() {
        return isPatch;
    }

    public boolean setNumber(String number) {
        if (!ReleaseUtils.isValidReleaseName(number, isPatchRelease())) {
            return false;
        } else {
            this.number = number;
            return true;
        }
    }

    public IBuildEnvironment getBuildEnvironment() {
        return buildEnvironment;
    }

    public String getPrevNumber() {
        return prevNumber;
    }

    public EReleaseStatus getStatus() {
        return status;
    }

    public void setStatus(EReleaseStatus st) {
        status = st;
    }

    public boolean performCleanAndBuild() {
        return performCleanAndBuild;
    }

    public void setPerformCleanAndBuild(boolean performCleanAndBuild) {
        this.performCleanAndBuild = performCleanAndBuild;
    }

    public boolean verifyRelease() {
        return verifyRelease;
    }

    public IDialogStyler getDialogStyler() {
        return dialogStyler;
    }

    public void setDialogStyler(IDialogStyler dialogStyler) {
        this.dialogStyler = dialogStyler;
    }

    public void setVerifyRelease(boolean verifyRelease) {
        this.verifyRelease = verifyRelease;
    }

    public boolean isGenerateRadixdoc() {
        return generateRadixdoc;
    }

    public void setGenerateRadixdoc(boolean generateRadixdoc) {
        this.generateRadixdoc = generateRadixdoc;
    }

    public IScriptDialog getScriptDialog() {
        return scriptDialog;
    }

    public void setScriptDialog(IScriptDialog scriptDialog) {
        this.scriptDialog = scriptDialog;
    }
}

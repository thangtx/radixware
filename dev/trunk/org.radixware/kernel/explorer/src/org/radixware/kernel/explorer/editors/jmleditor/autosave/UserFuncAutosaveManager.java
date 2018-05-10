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
package org.radixware.kernel.explorer.editors.jmleditor.autosave;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.adsdef.UserFuncSourceVersions;
import org.radixware.schemas.jmlEditorSettings.FuncEntry;
import org.radixware.schemas.jmlEditorSettings.UserFuncAutosaveData;
import org.radixware.schemas.jmlEditorSettings.UserFuncAutosaveDataDocument;

/**
 *
 * @author npopov
 */
public class UserFuncAutosaveManager {

    private final IClientEnvironment env;
    private final UserFuncAutosaveDataDocument xSettings;
    private final Map<Pid, FuncEntry> func2Versions = new HashMap<>();
    private final PriorityQueue<UfVersion> allVersions = new PriorityQueue<>();

    private boolean isAutosaveEnabled = true;
    private int saveIntervalSec = 20;
    private int maxStoredVersionsPerFunc = 10;
    private int maxStoredVersionsAll = 100;

    private static final String settingsKey = SettingNames.SYSTEM + "/" + "user_func_editor_autosave";
    private static final Map<IClientEnvironment, UserFuncAutosaveManager> env2Manager = new WeakHashMap<>();
    private static final XmlOptions XML_OPTIONS;
    static {
        XML_OPTIONS = new XmlOptions();
        XML_OPTIONS.setSaveNamespacesFirst();
        XML_OPTIONS.setSaveAggressiveNamespaces();
        XML_OPTIONS.setUseDefaultNamespace();
    }
 

    private UserFuncAutosaveManager(IClientEnvironment env) {
        this.env = env;

        UserFuncAutosaveDataDocument xml = loadConfig();
        if (xml != null) {
            xSettings = xml;
            if (xSettings.getUserFuncAutosaveData() != null) {
                UserFuncAutosaveData xData = xSettings.getUserFuncAutosaveData();
                Iterator<FuncEntry> iter = xData.getFuncEntryList().iterator();
                while (iter.hasNext()) {
                    FuncEntry xEntry = iter.next();
                    if (xEntry.getPid() == null) {
                        iter.remove();
                    } else {
                        func2Versions.put(Pid.fromStr(xEntry.getPid()), xEntry);
                        for (UserFuncSourceVersions.SourceVersion xVersion : xEntry.getVersions().getSourceVersionList()) {
                            allVersions.add(new UfVersion(xVersion));
                        }
                    }
                }
                if (xData.isSetIsAutosaveEnabled()) {
                    isAutosaveEnabled = xData.getIsAutosaveEnabled();
                }
                if (xData.isSetSaveIntervalSec()) {
                    saveIntervalSec = xData.getSaveIntervalSec().intValue();
                }
                if (xData.isSetMaxStoredVersionsForFunc()) {
                    maxStoredVersionsPerFunc = xData.getMaxStoredVersionsForFunc().intValue();
                }
                if (xData.isSetMaxStoredVersionsAll()) {
                    maxStoredVersionsAll = xData.getMaxStoredVersionsAll().intValue();
                }
            }

        } else {
            xSettings = UserFuncAutosaveDataDocument.Factory.newInstance();
        }
    }

    public static final UserFuncAutosaveManager getInstance(IClientEnvironment env) {
        UserFuncAutosaveManager instance = env2Manager.get(env);
        if (instance == null) {
            instance = new UserFuncAutosaveManager(env);
            env2Manager.put(env, instance);
        }
        return instance;
    }

    public void addVersion(Pid ufPid, Jml versionJml) {
        if (ufPid == null || versionJml == null) {
            throw new NullPointerException("User function pid or jml is null");
        }
        FuncEntry xFuncEntry = func2Versions.get(ufPid);
        int nextVersionNum = 0;
        UserFuncSourceVersions.SourceVersion xVersion = null;
        if (xFuncEntry == null) {
            xFuncEntry = xSettings.ensureUserFuncAutosaveData().addNewFuncEntry();
            xFuncEntry.setPid(ufPid.toStr());
            func2Versions.put(ufPid, xFuncEntry);
        } else {
            if (xFuncEntry.getVersions().sizeOfSourceVersionArray() > 0) {
                UserFuncSourceVersions.SourceVersion lastVer = xFuncEntry.
                        getVersions().getSourceVersionArray(
                                xFuncEntry.getVersions().sizeOfSourceVersionArray() - 1);
                xVersion = isSameSrc(versionJml, lastVer) ? lastVer : null;
                String[] numAsStr = lastVer.getName().split("#");
                nextVersionNum = Integer.parseInt(numAsStr[1]) + 1;
            }
        }
        
        if (xVersion == null) {
            xVersion = xFuncEntry.ensureVersions().addNewSourceVersion();
            versionJml.appendTo(xVersion, AdsDefinition.ESaveMode.NORMAL);
        } else {
            removeFromAllVersions(xVersion);
        }
        Calendar c = Calendar.getInstance();
        xVersion.setLastUpdateTime(c);
        xVersion.setName("Version#" + nextVersionNum);
        xVersion.setLastUpdateUserName(env.getUserName());
        allVersions.add(new UfVersion(xVersion));
        
        checkLimits(xFuncEntry);

        storeConfing();
    }
    
    public boolean removeVersion(Pid ufPid, String versionName) {
        FuncEntry xFuncEntry = func2Versions.get(ufPid);
        if (xFuncEntry != null && xFuncEntry.getVersions() != null) {
            for (int i = 0; i < xFuncEntry.getVersions().sizeOfSourceVersionArray(); i++) {
                UserFuncSourceVersions.SourceVersion xVer = xFuncEntry.getVersions().getSourceVersionArray(i);
                if (xVer.getName().equals(versionName)) {
                    xFuncEntry.getVersions().removeSourceVersion(i);
                    removeFromAllVersions(xVer);
                    storeConfing();
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isSameSrc(Jml newVerJml, UserFuncSourceVersions.SourceVersion xLastVer) {
        final Jml lastVerJml = Jml.Factory.loadFrom(null, xLastVer, "temporary_jml");
        return newVerJml.toString().equals(lastVerJml.toString());
    }

    private UserFuncAutosaveDataDocument loadConfig() {
        final String xSettingsBase64 = env.getConfigStore().readString(settingsKey);
        UserFuncAutosaveDataDocument xml = null;
        if (xSettingsBase64 != null && !xSettingsBase64.isEmpty()) {
            try (ByteArrayInputStream is = new ByteArrayInputStream(Base64.decode(xSettingsBase64));
                    ZipInputStream zis = new ZipInputStream(is)) {
                zis.getNextEntry();
                String unzipedStr = new String(FileUtils.readBinaryStream(zis), StandardCharsets.UTF_8);
                xml = UserFuncAutosaveDataDocument.Factory.parse(unzipedStr);
            } catch (XmlException | IOException ex) {
                Logger.getLogger(UserFuncAutosaveManager.class.getName()).log(Level.SEVERE,
                        "Error on load user function auto-save data", ex);
            }
        }
        return xml;
    }

    private void storeConfing() {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(out)) {
            zip.putNextEntry(new ZipEntry(settingsKey));
            xSettings.save(zip, XML_OPTIONS);
            zip.closeEntry();
            String base64Str = Base64.encode(out.toByteArray());
            env.getConfigStore().writeString(settingsKey, base64Str);
        } catch (IOException ex) {
            Logger.getLogger(UserFuncAutosaveManager.class.getName()).log(Level.SEVERE,
                    "Error on store user function auto-save data", ex);
        }
    }
    
    private void removeFromAllVersions(UserFuncSourceVersions.SourceVersion xVerToRemove) {
        Iterator<UfVersion> iter = allVersions.iterator();
        while (iter.hasNext()) {
            if (iter.next().xVersion == xVerToRemove) {
                iter.remove();
                break;
            }
        }
    }

    private void checkLimits(FuncEntry xFuncEntry) {
        while (xFuncEntry.getVersions().sizeOfSourceVersionArray() > maxStoredVersionsPerFunc) {
            final UserFuncSourceVersions.SourceVersion oldestVer = xFuncEntry.getVersions().getSourceVersionArray(0);
            removeFromAllVersions(oldestVer);
            xFuncEntry.getVersions().removeSourceVersion(0);
        }

        while (allVersions.size() > maxStoredVersionsAll) {
            XmlCursor cursor = allVersions.poll().xVersion.newCursor();
            cursor.removeXml();
            cursor.dispose();
        }
    }

    public static void reset() {
        env2Manager.clear();
    }

    public List<UserFuncSourceVersions.SourceVersion> getVersions(Pid pid) {
        if (func2Versions.containsKey(pid)) {
            UserFuncSourceVersions versions = (UserFuncSourceVersions) func2Versions.get(pid).getVersions().copy();
            return versions.getSourceVersionList();
        }
        return Collections.emptyList();
    }

    public Map<String, UserFuncSourceVersions.SourceVersion> getVersionsMap(Pid pid) {
        HashMap<String, UserFuncSourceVersions.SourceVersion> result = new HashMap<>();
        for (UserFuncSourceVersions.SourceVersion ver : getVersions(pid)) {
            result.put(ver.getName(), ver);
        }
        return result;
    }

    public int getAllVersionsCnt() {
        return allVersions.size();
    }

    public UserFuncAutosaveData getAutosaveData() {
        return xSettings.getUserFuncAutosaveData();
    }

    public void setOptions(boolean isAutosaveEnabled, int intervalSec, int maxFuncVersions, int totalVersions) {
        this.isAutosaveEnabled = isAutosaveEnabled;
        saveIntervalSec = intervalSec;
        maxStoredVersionsPerFunc = maxFuncVersions;
        maxStoredVersionsAll = totalVersions;
        
        xSettings.ensureUserFuncAutosaveData().setIsAutosaveEnabled(isAutosaveEnabled);
        xSettings.ensureUserFuncAutosaveData().setSaveIntervalSec(Long.valueOf(intervalSec));
        xSettings.ensureUserFuncAutosaveData().setMaxStoredVersionsForFunc(Long.valueOf(maxFuncVersions));
        xSettings.ensureUserFuncAutosaveData().setMaxStoredVersionsAll(Long.valueOf(totalVersions));
        
        storeConfing();
    }
    
    public boolean isAutosaveEnabled() {
        return isAutosaveEnabled;
    }

    public int getSaveIntervalSec() {
        return saveIntervalSec;
    }

    public int getMaxStoredVersionsForFunc() {
        return maxStoredVersionsPerFunc;
    }

    public int getMaxStoredVersionsAll() {
        return maxStoredVersionsAll;
    }

    private static final class UfVersion implements Comparable<UfVersion> {

        private final long creationDate;
        private final UserFuncSourceVersions.SourceVersion xVersion;

        public UfVersion(UserFuncSourceVersions.SourceVersion xVersion) {
            this.creationDate = xVersion.getLastUpdateTime().getTimeInMillis();
            this.xVersion = xVersion;
        }
        
        @Override
        public int compareTo(UfVersion o) {
            return Long.compare(creationDate, o.creationDate);
        }

        @Override
        public String toString() {
            return xVersion.getName();
        }
    }

}

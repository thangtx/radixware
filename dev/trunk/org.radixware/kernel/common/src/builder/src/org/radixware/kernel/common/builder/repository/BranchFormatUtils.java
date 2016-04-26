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
package org.radixware.kernel.common.builder.repository;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.utils.FileUtils;

public class BranchFormatUtils {

    public static final void upgrade(Branch branch, ISvnFSClient client) {
        try {
            //System.out.println("START UPGRADE");
            //System.out.println("FormatVersion="+branch.getFormatVersion());
            if (branch.getFormatVersion() == 0) {
                branch.visitChildren(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                    }
                }, new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        if (radixObject instanceof AdsLocalizingBundleDef) {
                            return true;
                        }
                        return false;
                    }
                });
                final SVNRepositoryAdapter repo = SVNRepositoryAdapter.Factory.newInstance(client, branch.getFile(), SVN.SVNPreferences.getUserName(), org.radixware.kernel.common.svn.utils.SVN.SVNPreferences.getAuthType(), SVN.SVNPreferences.getSSHKeyFilePath());
                try {
                    List<File> files = new ArrayList<>();
                    for (Layer layer : branch.getLayers()) {
                        //System.out.println("Layer: "+layer.getName()+"("+layer.getURI()+")");
                        if (!layer.isLocalizing() && !layer.isReadOnly()) {
                            //System.out.println(layer.getDirectory().getAbsolutePath()+" was added");
                            files.add(layer.getDirectory());
                            /*for (Module module : layer.getAds().getModules()) {
                             AdsModule ads = (AdsModule) module;                                  
                             files.add(ads.getDirectory());
                             }*/
                        }
                    }

                    //System.out.println("SVN update (files count="+files.size()+")");
                    SVN.update(client, files.toArray(new File[files.size()]));
                } finally {
                    if (repo != null) {
                        repo.close();
                    }
                }
                //System.out.println("upgradeToVersion_1()");
                upgradeToVersion_1(branch);
                //System.out.println("upgradeToVersion_2()");
                upgradeToVersion_2(branch, client);
            } else if (branch.getFormatVersion() == 1) {
                upgradeToVersion_2(branch, client);
            }
        } catch (RadixSvnException | AuthenticationCancelledException ex) {
            //System.out.println("SVNException | AuthenticationCancelledException ex");
            //System.out.println("branch.getFile()="+branch.getFile().getAbsolutePath()+" ," + SVN.SVNPreferences.getUserName()+", "+SVN.SVNPreferences.getAuthType().getValue()+" ,"+ SVN.SVNPreferences.getSSHKeyFilePath());
            Logger.getLogger(BranchFormatUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static final boolean upgradeRequired(Branch branch) {
        //System.out.println("IS UPGRADE REQUIRED");
        //System.out.println("FormatVersion="+branch.getFormatVersion());
        if (branch.getFormatVersion() < 2) {

            Layer layer = branch.getLayers().findByURI("org.radixware");
            //System.out.println("org.radixware="+layer);
            if (layer == null) {
                return false;
            } else {
                String releaseNumber = layer.getReleaseNumber();
                //System.out.println("ReleaseNumber="+releaseNumber);
                if (releaseNumber == null) {
                    //System.out.println("ReleaseNumber=null  -> return  false");
                    return false;
                } else {
                    String[] parts = releaseNumber.split("\\.");
                    if (parts.length < 3) {
                        //System.out.println("parts.length < 3  -> return  false");
                        return false;
                    } else {
                        String lastMajorVersion = parts[2];
                        try {
                            int value = Integer.parseInt(lastMajorVersion);
                            if (value < 21) {
                                //System.out.println("lastMajorVersion < 21  -> return false");
                                return false;
                            } else {
                                //System.out.println("return true");
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            //System.out.println("NumberFormatException ->  return false");
                            return false;
                        }
                    }
                }
            }
        } else {
            //System.out.println("branch.getFormatVersion() >= 2 ->  return false");
            return false;
        }
    }

    public static final void upgradeToVersion_1(Branch branch) {
        try {
            // lock.lock();      
            //System.out.println("branch.getLayers().size()="+branch.getLayers().size());
            for (Layer layer : branch.getLayers()) {
                //System.out.println(layer.getName());
                if (!layer.isLocalizing() && !layer.isReadOnly()) {
                    //System.out.println(layer.getName()+" save");
                    layer.visitChildren(new IVisitor() {
                        @Override
                        public void accept(RadixObject radixObject) {
                            try {
                                radixObject.save();
                            } catch (IOException cause) {
                                throw new RadixObjectError("Unable to save localizing bundle.", radixObject, cause);
                            }
                        }
                    }, new AdsVisitorProvider() {
                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            if (radixObject instanceof AdsLocalizingBundleDef) {
                                return true;
                            }
                            return false;
                        }
                    });
                }
            }
            //System.out.println("setFormatVersion(1)");
            branch.setFormatVersion(1);
            branch.save();
            //CommitAction.commit("Изменение структуры хранения многоязыковых строк", new Context(project.getProjectDirectory()), false) ;
        } catch (IOException ex) {
            throw new RadixObjectError("Unable to save branch.", branch, ex);
            //} finally {
            //    lock.unlock();
        }
    }

    public static final void upgradeToVersion_2(Branch branch, ISvnFSClient client) throws ISvnFSClient.SvnFsClientException, RadixSvnException, AuthenticationCancelledException {

        class DeleteInfo {

            final String path;
            final long revision;

            public DeleteInfo(String path, long revision) {
                this.path = path;
                this.revision = revision;
            }
        }
        final SVNRepositoryAdapter repo = SVNRepositoryAdapter.Factory.newInstance(client, branch.getFile(), SVN.SVNPreferences.getUserName(), org.radixware.kernel.common.svn.utils.SVN.SVNPreferences.getAuthType(), SVN.SVNPreferences.getSSHKeyFilePath());
        try {
            long revision = -1;
            String branchUrl = null;
            try {
                ISvnFSClient.ISvnStatus status = client.getSingleStatus(branch.getDirectory());
                revision = status.getRevision().getNumber();
                branchUrl = status.getUrlString();
            } catch (ISvnFSClient.SvnFsClientException ex) {
                return;
            }
            String rootUrl = null;

            rootUrl = repo.getRepositoryRoot();
            final Map<String, File> addInfo = new HashMap<>();
            String branchPath = SvnPath.getRelativePath(rootUrl, branchUrl);
            final List<DeleteInfo> deleteInfos = new LinkedList<>();
            List<File> files = new ArrayList<>();
            for (Layer layer : branch.getLayers()) {
                if (!layer.isLocalizing() && !layer.isReadOnly()) {
                    for (Module module : layer.getAds().getModules()) {
                        AdsModule ads = (AdsModule) module;
                        File srcDir = new File(ads.getDirectory(), "src");
                        File localeDir = new File(ads.getDirectory(), AdsModule.LOCALE_DIR_NAME);
                        if (srcDir.exists()) {
                            File[] mlbs = srcDir.listFiles(new FileFilter() {
                                @Override
                                public boolean accept(File pathname) {
                                    return pathname.getName().startsWith("mlb") && pathname.getName().endsWith(".xml");
                                }
                            });
                            if (mlbs != null) {
                                for (File file : mlbs) {
                                    FileUtils.deleteFile(file);

                                }
                            }

                            final String path = SvnPath.append(branchPath, (FileUtils.getRelativePath(branch.getDirectory(), srcDir)).replace(File.separator, "/"));
                            if (SVN.isExists(repo, path)) {
                                repo.getDir(path, -1, new SVNRepositoryAdapter.EntryHandler() {

                                    @Override
                                    public void accept(SvnEntry entry) throws RadixSvnException {
                                        String name = entry.getName();
                                        if (name.startsWith("mlb") && name.endsWith(".xml")) {
                                            deleteInfos.add(new DeleteInfo(SvnPath.append(path, entry.getName()), entry.getRevision()));
                                        }
                                    }
                                });
                            }
                            if (localeDir.exists()) {
                                String addPath = SvnPath.append(branchPath, FileUtils.getRelativePath(branch.getDirectory(), localeDir));
                                addPath = addPath.replace(File.separator, "/");
                                if (!SVN.isExists(repo, addPath)) {
                                    addInfo.put(addPath, localeDir);
                                    files.add(module.getDirectory());
                                }
                            }
                        }
                    }
                }
            }
            if (!deleteInfos.isEmpty() || !addInfo.isEmpty()) {
                SVNRepositoryAdapter.Editor editor = repo.createEditor("Removing obsolete localization information and add new");
                for (DeleteInfo info : deleteInfos) {
                    editor.deleteEntry(info.path, info.revision);
                }
                addLocaleDirsToSvn(branch, branchPath, editor, addInfo);
                saveBranch(branch, branchPath, editor);
                editor.commit();

                for (String path : addInfo.keySet()) {
                    File localeDir = addInfo.get(path);
                    FileUtils.deleteDirectory(localeDir);
                }
                SVN.update(client, files.toArray(new File[files.size()]));
            } else {
                branch.setFormatVersion(2);
                try {
                    branch.save();
                } catch (IOException ex) {
                    Logger.getLogger(BranchFormatUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } finally {
            if (repo != null) {
                repo.close();
            }
        }
    }

    private static void addLocaleDirsToSvn(Branch branch, String branchPath, SVNRepositoryAdapter.Editor editor, final Map<String, File> addInfo) throws RadixSvnException {
        for (String path : addInfo.keySet()) {
            File localeDir = addInfo.get(path);
            editor.appendDir(path);
            for (int i = 0; i < localeDir.listFiles().length; i++) {
                File langDir = localeDir.listFiles()[i];
                try {
                    if (langDir.isDirectory() && EIsoLanguage.getForValue(langDir.getName()) != null) {
                        String addPath = SvnPath.append(branchPath, (FileUtils.getRelativePath(branch.getDirectory(), langDir)).replace(File.separator, "/"));
                        editor.appendDir(addPath);
                        for (int j = 0; j < langDir.listFiles().length; j++) {
                            File file = langDir.listFiles()[j];
                            if (file.getName().startsWith("mlb") && file.getName().endsWith(".xml")) {
                                addPath = SvnPath.append(branchPath, (FileUtils.getRelativePath(branch.getDirectory(), file)).replace(File.separator, "/"));
                                addPath = addPath.replace(File.separator, "/");
                                try {
                                    FileInputStream data = new FileInputStream(file);
                                    editor.appendFile(addPath, data);
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(BranchFormatUtils.class.getName()).log(Level.SEVERE, null, ex);
                                    continue;
                                }
                            }
                        }
                        editor.closeDir();
                    }
                } catch (NoConstItemWithSuchValueError ex) {
                    continue;
                }
            }
            editor.closeDir();
        }
    }

    private static void saveBranch(Branch branch, String branchPath, SVNRepositoryAdapter.Editor editor) throws RadixSvnException {
        try {
            branch.setFormatVersion(2);
            branch.save();
            final String path = SvnPath.append(branchPath, (FileUtils.getRelativePath(branch.getDirectory(), branch.getFile())).replace(File.separator, "/"));
            FileInputStream data = new FileInputStream(branch.getFile());
            editor.modifyFile(path, data);
        } catch (IOException ex) {
            Logger.getLogger(BranchFormatUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

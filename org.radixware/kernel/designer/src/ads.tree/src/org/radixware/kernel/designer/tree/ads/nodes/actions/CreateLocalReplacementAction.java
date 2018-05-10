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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.apache.xmlbeans.XmlException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import static org.radixware.kernel.common.build.directory.DirectoryFileSigner.calcFileDigest;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.SvnAuthType;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.common.utils.ReleaseUtilsCommon.ReleaseStringComporator;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.InputOutputPrinter;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.subversion.util.SvnBridge;
import org.radixware.schemas.product.Directory;
import org.radixware.schemas.product.DirectoryDocument;

public class CreateLocalReplacementAction extends CookieAction {
    
    public static class CreateLocalReplacementCookie implements Node.Cookie {

        private final Module module;

        public CreateLocalReplacementCookie(Module module) {
            this.module = module;
        }

        private final void generate() {
            
        }
    }

    public CreateLocalReplacementAction() {
        io = new InputOutputPrinter("Create local replacement zip");
    }
    
    private final InputOutputPrinter io;

    @Override
    protected int mode() {
        return MODE_ALL;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{
            CreateLocalReplacementCookie.class
        };
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        try {
            io.printlnInfo("Start operation");
            if (BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null) {
                final ArrayList<Module> contexts = new ArrayList<>();
                for (Node node : activatedNodes) {
                    final AbstractBuildAction.BuildCookie buildCookie = node.getLookup().lookup(AbstractBuildAction.BuildCookie.class);
                    if (buildCookie != null) {
                        RadixObject obj = buildCookie.getRadixObject();
                        if (obj != null && obj instanceof Module) {
                            contexts.add((Module) obj);
                        }
                    }
                }
                
                
                generate(contexts);
            }
            io.printlnInfo("Operation finish");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public String getName() {
        return "Create Local Replacement Zip";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isEnabled() {
        return BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null && super.isEnabled();
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }
    
    private boolean collectFile(final File branchDir, final List<Module> moduleDirDiffs, final List<String> paths, Reference<String> version) throws IOException, RadixSvnException {
        
        ISvnFSClient client = SvnBridge.getClientAdapter(branchDir);
        final String userName = SVN.SVNPreferences.getUserName();
        final SvnAuthType authType = SVN.SVNPreferences.getAuthType();
        final String sshKeyFile = SVN.SVNPreferences.getSSHKeyFilePath();

        final File dir = Files.createTempDirectory("LocalReplacementTmp").toFile();
        try {
            String branchUrl = SVN.getFileUrl(client, branchDir);
            SVNRepositoryAdapter repository =  SVNRepositoryAdapter.Factory.newRootInstance(branchUrl, userName, authType, sshKeyFile);
            long revision = repository.getLatestRevision();
            io.println("Will use repository revision number " + revision);
            final String rootUrl = repository.getRepositoryRoot();
            String basisUrl = SvnPath.getRelativePath(rootUrl, SvnPath.removeTail(SvnPath.removeTail(branchUrl)));
            String releasesUrl = SvnPath.append(basisUrl, "releases");
            final ArrayList<String> releases = new ArrayList<>();
            repository.getDir(releasesUrl, revision, new SVNRepositoryAdapter.EntryHandler() {
                @Override
                public void accept(SvnEntry entry) throws RadixSvnException {
                    if (entry.getKind() == SvnEntry.Kind.DIRECTORY) {
                        final String name = entry.getName();

                        if (!releases.contains(name)) {
                            releases.add(entry.getName());
                        }
                    }
                }
            });
            Collections.sort(releases, new ReleaseStringComporator());
            Collections.reverse(releases);
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("fillx"));
            JComboBox<String> box = new JComboBox<>(releases.toArray(new String[releases.size()]));
            panel.add(box, "growx");
            ModalDisplayer md = new ModalDisplayer(panel, "Replacement Release Version");
            if (!md.showModal()) {
                io.printlnError("Cancelled by user");
                return false;
            }
            version.set((String) box.getSelectedItem());
            for (Module m : moduleDirDiffs) {
                File currentModuleDir = m.getDirectory();
                String mUrl = SVN.getFileUrl(client, currentModuleDir);
                String moduleUrl = SvnPath.getRelativePath(branchUrl, mUrl);
                StringBuilder sb = new StringBuilder(releasesUrl);
                sb.append("/").append(version.get()).append("/").append(moduleUrl).append("/");
                final String modulePath = sb.toString();
                sb.append(FileUtils.DIRECTORY_XML_FILE_NAME);
                final String moduleDirIndex = sb.toString();
                final boolean fileExists = SVN.isFileExists(repository, moduleDirIndex);
                if (fileExists) {
                    final File temp = File.createTempFile("tmp", m.getName(), dir);
                    SVN.getFile(repository, moduleDirIndex, revision, temp);
                    DirectoryDocument xDoc;
                    try {
                        xDoc = DirectoryDocument.Factory.parse(temp);
                        if (xDoc.getDirectory().getFileGroups() != null) {
                            for (Directory.FileGroups.FileGroup xGroup : xDoc.getDirectory().getFileGroups().getFileGroupList()) {
                                for (Directory.FileGroups.FileGroup.File xFile : xGroup.getFileList()) {
                                    if (xFile.getName().contains(FileNames.SRC_DIR + "/") && Directory.FileGroups.FileGroup.GroupType.ADS_COMMON.equals(xGroup.getGroupType())){
                                        continue;
                                    }
                                    final byte[] storedDigest = xFile.getDigest();
                                    File fsFile = new File(currentModuleDir, xFile.getName());
                                    if (fsFile.exists()) {
                                        final byte[] fileDigest = calcFileDigest(fsFile);
                                        if (!Arrays.equals(storedDigest, fileDigest)) {
                                            paths.add(moduleUrl + "/" + xFile.getName());
                                        }
                                    }
                                }
                            }
                        }
                    } catch (XmlException ex) {
                        printError(Level.WARNING, "Unable to parse directory index file " + moduleDirIndex, ex);
                        return false;
                    } catch (NoSuchAlgorithmException ex) {
                        return false;
                    }
                }
                
            }
            
        } catch (RadixSvnException ex) {
            if (ex.isAuthenticationCancelled()) {
                io.printlnError("SVN authentication cancelled by user");
                 return false;
            } else {
                throw ex;
            }

        } finally {
            FileUtils.deleteFileOrDirectory(dir);
        }
        return true;
    }    
    
    private boolean generate(final List<Module> modules) throws IOException {
        if (modules.isEmpty()) {
            return false;
        }

        io.println("Compiling...");
        final DesignerBuildEnvironment buildEnvironment = new DesignerBuildEnvironment(false, BuildActionExecutor.EBuildActionType.CLEAN_AND_BUILD) {

            @Override
            public BuildOptions getBuildOptions() {
                synchronized (this) {
                    if (configuredOptions == null) {
                        configuredOptions = BuildOptions.Factory.newInstance();
                        configuredOptions.setEnvironment(EnumSet.allOf(ERuntimeEnvironmentType.class));
                        configuredOptions.setMultythread(true);
                    }
                    return configuredOptions;
                }
            }

        };
        final BuildActionExecutor executor = new BuildActionExecutor(buildEnvironment);
        
        BuildOptions options = executor.execute(modules.toArray(new Module[modules.size()]), buildEnvironment.getActionType(), null, true);
        if (options == null || executor.wasErrors()) {
            io.printlnError("Build failed");
            return false;
        }
        
        ArrayList<String> paths = new ArrayList<>();
        Reference<String> currentVersion = new Reference<>();
        Branch b = modules.get(0).getBranch();
        final File branchDir = b.getDirectory();
        if (branchDir == null) {
            return false;
        }
        io.select();
        io.println("Find modified files...");
        try {
            if (!collectFile(branchDir, modules, paths, currentVersion)){
                return false;
            }
        } catch (RadixSvnException ex) {
            String message = ex.getMessage();
            message = message == null? "There is some SVN problem" : message;
            printError(Level.SEVERE, message, ex);
            return false;
        }
        if (paths.isEmpty()) {
            io.println("There are no modified files");
            return true;
        }
        CreateLocalReplacementPanel panel = new CreateLocalReplacementPanel();
        panel.open(paths, currentVersion.get());
        StateAbstractDialog md = new StateAbstractDialog(panel, "Replacement Release Version") {};
        if (!md.showModal()) {
            io.printlnError("Cancelled by user");
            return false;
        }
        io.println("Create ZIP file...");
        File outFile = panel.getFile();
        List<String> resultPath = panel.getPaths();
        String version = panel.getVersion();
        String comment = panel.getComment();
        
        if (resultPath.isEmpty()) {
            io.println("There are no selected files");
            return true;
        }
        
        try (FileOutputStream baos = new FileOutputStream(outFile)){
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (String path : resultPath) {
                    File file = new File(branchDir, path);
                    if (file.exists() && !file.isDirectory()) {
                        writeFile(file, path, zos);
                    }
                }
                ZipEntry ze = new ZipEntry("replacement.cfg");
                zos.putNextEntry(ze);
                String cfg = createCfgFile(version, comment);
                byte[] data = cfg.getBytes();
                zos.write(data, 0, data.length);
                zos.closeEntry();
            }
        }
        return true;
    }
    
    private void writeFile(File file, String name, ZipOutputStream zos) throws FileNotFoundException, IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry ze = new ZipEntry(name);
            zos.putNextEntry(ze);
            FileUtils.copyStream(fis, zos);
            zos.closeEntry();
        }
    }
    
    private String createCfgFile(String version, String comment) {
        StringBuilder sb = new StringBuilder();
        if (version != null && !version.isEmpty()) {
            sb.append("compatibleVersions=").append(version).append("\n");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd-hh:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = dateFormat.format(new Date());
        sb.append("creationDate=").append(date).append("\n");
        if (comment != null && !comment.isEmpty()) {
            sb.append("comment=").append(comment);
        }
        return sb.toString();
    }
    
    private void printError(Level level, String message, Exception ex) throws IOException {
        io.printlnError(message);
        Logger.getLogger(CreateLocalReplacementAction.class.getName()).log(level, message, ex);
    }

}

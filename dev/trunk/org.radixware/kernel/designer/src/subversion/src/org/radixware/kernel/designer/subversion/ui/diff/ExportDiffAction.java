/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2009 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * May 2013: adapted to work with RadixWare file-based entities by Compass Plus Limited.
 */
package org.radixware.kernel.designer.subversion.ui.diff;

//import org.netbeans.spi.diff.DiffVisualizer;
import org.netbeans.modules.diff.builtin.visualizer.TextDiffVisualizer;
import org.netbeans.modules.subversion.FileInformation;
import org.netbeans.modules.subversion.Subversion;
import org.netbeans.modules.subversion.SvnModuleConfig;
import org.netbeans.modules.subversion.client.SvnProgressSupport;
import org.netbeans.modules.subversion.util.Context;
import org.netbeans.modules.subversion.util.SvnUtils;
import org.netbeans.modules.subversion.ui.actions.ContextAction;
import org.netbeans.modules.versioning.util.Utils;
import org.netbeans.api.diff.Difference;
import org.netbeans.spi.diff.DiffProvider;
import org.openide.windows.TopComponent;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.NbBundle;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import org.openide.nodes.Node;
import org.openide.awt.StatusDisplayer;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.modules.subversion.client.SvnClientExceptionHandler;
import org.netbeans.modules.proxy.Base64Encoder;
import org.netbeans.modules.versioning.util.ExportDiffSupport;
import org.tigris.subversion.svnclientadapter.SVNClientException;

/**
 * Exports diff to file:
 *
 * <ul>
 * <li>for components that implements {@link DiffSetupSource} interface exports
 * actually displayed diff.
 *
 * <li>for DataNodes <b>local</b> differencies between the current working copy
 * and BASE repository version.
 * </ul>
 */
public class ExportDiffAction extends ContextAction {

    private static final int enabledForStatus =
            FileInformation.STATUS_VERSIONED_MERGE
            | FileInformation.STATUS_VERSIONED_MODIFIEDLOCALLY
            | FileInformation.STATUS_VERSIONED_DELETEDLOCALLY
            | FileInformation.STATUS_VERSIONED_REMOVEDLOCALLY
            | FileInformation.STATUS_NOTVERSIONED_NEWLOCALLY
            | FileInformation.STATUS_VERSIONED_ADDEDLOCALLY;

    protected String getBaseName(Node[] activatedNodes) {
        return "CTL_MenuItem_ExportDiff";  // NOI18N
    }

    /**
     * First look for DiffSetupSource name then for super (context name).
     */
    @Override
    public String getName() {
        TopComponent activated = TopComponent.getRegistry().getActivated();
        if (activated instanceof DiffSetupSource) {
            String setupName = ((DiffSetupSource) activated).getSetupDisplayName();
            if (setupName != null) {
                return NbBundle.getMessage(this.getClass(), getBaseName(getActivatedNodes()) + "_Context", // NOI18N
                        setupName);
            }
        }
        return super.getName();
    }

    @Override
    public boolean enable(Node[] nodes) {
        Context ctx = getCachedContext(nodes);
        if (!Subversion.getInstance().getStatusCache().containsFiles(ctx, enabledForStatus, true)) {
            return false;
        }
        TopComponent activated = TopComponent.getRegistry().getActivated();
        if (activated instanceof DiffSetupSource) {
            return true;
        }
        return super.enable(nodes) && Lookup.getDefault().lookup(DiffProvider.class) != null;
    }

    @Override
    protected void performContextAction(Node[] nodes) {
        performContextAction(nodes, false);
    }

    void performContextAction(final Node[] nodes, final boolean singleDiffSetup) {
        // reevaluate fast enablement logic guess

        if (!Subversion.getInstance().checkClientAvailable()) {
            return;
        }

        boolean noop;
        Context context = getContext(nodes);
        TopComponent activated = TopComponent.getRegistry().getActivated();
        if (activated instanceof DiffSetupSource) {
            noop = ((DiffSetupSource) activated).getSetups().isEmpty();
        } else {
            noop = !Subversion.getInstance().getStatusCache().containsFiles(context, FileInformation.STATUS_LOCAL_CHANGE, true);
        }
        if (noop) {
            NotifyDescriptor msg = new NotifyDescriptor.Message(NbBundle.getMessage(ExportDiffAction.class, "BK3001"), NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(msg);
            return;
        }

        ExportDiffSupport exportDiffSupport = new ExportDiffSupport(context.getRootFiles(), SvnModuleConfig.getDefault().getPreferences()) {
            @Override
            public void writeDiffFile(final File toFile) {
                RequestProcessor rp = Subversion.getInstance().getRequestProcessor();
                SvnProgressSupport ps = new SvnProgressSupport() {
                    protected void perform() {
                        async(this, nodes, toFile, singleDiffSetup);
                    }
                };
                ps.start(rp, null, getRunningName(nodes)).waitFinished();
            }
        };
        exportDiffSupport.export();
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    private void async(SvnProgressSupport progress, Node[] nodes, File destination, boolean singleDiffSetup) {
        boolean success = false;
        OutputStream out = null;
        int exportedFiles = 0;
        try {

            // prepare setups and common parent - root

            File root;
            List<Setup> setups;

            TopComponent activated = TopComponent.getRegistry().getActivated();
            if (activated instanceof DiffSetupSource) {
                if (!singleDiffSetup) {
                    setups = new ArrayList<Setup>(((DiffSetupSource) activated).getSetups());
                } else {
                    if (nodes.length > 0 && nodes[0] instanceof DiffNode) {
                        setups = new ArrayList<Setup>(Collections.singletonList(((DiffNode) nodes[0]).getSetup()));
                    } else {
                        return;
                    }
                }
                List<File> setupFiles = new ArrayList<File>(setups.size());
                for (Iterator i = setups.iterator(); i.hasNext();) {
                    Setup setup = (Setup) i.next();
                    setupFiles.add(setup.getBaseFile());
                }
                root = getCommonParent(setupFiles.toArray(new File[setupFiles.size()]));
            } else {
                Context context = getContext(nodes);
                File[] files = SvnUtils.getModifiedFiles(context, FileInformation.STATUS_LOCAL_CHANGE);
                root = getCommonParent(context.getRootFiles());
                setups = new ArrayList<Setup>(files.length);
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    Setup setup = new Setup(file, null, Setup.DIFFTYPE_LOCAL);
                    setups.add(setup);
                }
            }
            if (root == null) {
                NotifyDescriptor nd = new NotifyDescriptor(
                        NbBundle.getMessage(ExportDiffAction.class, "MSG_BadSelection_Prompt"),
                        NbBundle.getMessage(ExportDiffAction.class, "MSG_BadSelection_Title"),
                        NotifyDescriptor.DEFAULT_OPTION, NotifyDescriptor.ERROR_MESSAGE, null, null);
                DialogDisplayer.getDefault().notify(nd);
                return;
            }

            String sep = System.getProperty("line.separator"); // NOI18N
            out = new BufferedOutputStream(new FileOutputStream(destination));
            // Used by PatchAction as MAGIC to detect right encoding
            out.write(("# This patch file was generated by NetBeans IDE" + sep).getBytes("utf8"));  // NOI18N
            out.write(("# Following Index: paths are relative to: " + root.getAbsolutePath() + sep).getBytes("utf8"));  // NOI18N
            out.write(("# This patch can be applied using context Tools: Patch action on respective folder." + sep).getBytes("utf8"));  // NOI18N
            out.write(("# It uses platform neutral UTF-8 encoding and \\n newlines." + sep).getBytes("utf8"));  // NOI18N
            out.write(("# Above lines and this line are ignored by the patching process." + sep).getBytes("utf8"));  // NOI18N


            Collections.sort(setups, new Comparator<Setup>() {
                public int compare(Setup o1, Setup o2) {
                    return o1.getBaseFile().compareTo(o2.getBaseFile());
                }
            });
            Iterator<Setup> it = setups.iterator();
            int i = 0;
            while (it.hasNext()) {
                Setup setup = it.next();
                File file = setup.getBaseFile();
                if (file.isDirectory()) {
                    continue;
                }
                try {
                    progress.setRepositoryRoot(SvnUtils.getRepositoryRootUrl(file));
                } catch (SVNClientException ex) {
                    SvnClientExceptionHandler.notifyException(ex, true, true);
                    return;
                }
                progress.setDisplayName(file.getName());

                String index = "Index: ";   // NOI18N
                String rootPath = root.getAbsolutePath();
                String filePath = file.getAbsolutePath();
                String relativePath = filePath;
                if (filePath.startsWith(rootPath)) {
                    relativePath = filePath.substring(rootPath.length() + 1).replace(File.separatorChar, '/');
                    index += relativePath + sep;
                    out.write(index.getBytes("utf8")); // NOI18N
                }
                exportDiff(setup, relativePath, out);
                i++;
            }

            exportedFiles = i;
            success = true;
        } catch (IOException ex) {
            Subversion.LOG.log(Level.INFO, NbBundle.getMessage(ExportDiffAction.class, "BK3003"), ex);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            if (success) {
                StatusDisplayer.getDefault().setStatusText(NbBundle.getMessage(ExportDiffAction.class, "BK3004", new Integer(exportedFiles)));
                if (exportedFiles == 0) {
                    destination.delete();
                } else {
                    Utils.openFile(destination);
                }
            } else {
                destination.delete();
            }

        }
    }

    private static File getCommonParent(File[] files) {
        File root = files[0];
        if (!root.exists() || root.isFile()) {
            root = root.getParentFile();
        }
        for (int i = 1; i < files.length; i++) {
            root = Utils.getCommonParent(root, files[i]);
            if (root == null) {
                return null;
            }
        }
        return root;
    }

    /**
     * Writes contextual diff into given stream.
     */
    private void exportDiff(Setup setup, String relativePath, OutputStream out) throws IOException {
        setup.initSources();
        DiffProvider diff = (DiffProvider) Lookup.getDefault().lookup(DiffProvider.class);

        Reader r1 = null;
        Reader r2 = null;
        Difference[] differences;

        try {
            r1 = setup.getFirstSource().createReader();
            if (r1 == null) {
                r1 = new StringReader("");  // NOI18N
            }
            r2 = setup.getSecondSource().createReader();
            if (r2 == null) {
                r2 = new StringReader("");  // NOI18N
            }
            differences = diff.computeDiff(r1, r2);
        } finally {
            if (r1 != null) {
                try {
                    r1.close();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            if (r2 != null) {
                try {
                    r2.close();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }

        File file = setup.getBaseFile();
        try {
            InputStream is;
            if (!SvnUtils.getMimeType(file).startsWith("text/") && differences.length == 0) {
                // assume the file is binary
                is = new ByteArrayInputStream(exportBinaryFile(file).getBytes("utf8"));  // NOI18N
            } else {
                r1 = setup.getFirstSource().createReader();
                if (r1 == null) {
                    r1 = new StringReader(""); // NOI18N
                }
                r2 = setup.getSecondSource().createReader();
                if (r2 == null) {
                    r2 = new StringReader(""); // NOI18N
                }
                TextDiffVisualizer.TextDiffInfo info = new TextDiffVisualizer.TextDiffInfo(
                        relativePath + " " + setup.getFirstSource().getTitle(), // NOI18N
                        relativePath + " " + setup.getSecondSource().getTitle(), // NOI18N
                        null,
                        null,
                        r1,
                        r2,
                        differences);
                info.setContextMode(true, 3);
                String diffText = TextDiffVisualizer.differenceToUnifiedDiffText(info);
                is = new ByteArrayInputStream(diffText.getBytes("utf8"));  // NOI18N
            }
            while (true) {
                int i = is.read();
                if (i == -1) {
                    break;
                }
                out.write(i);
            }
        } finally {
            if (r1 != null) {
                try {
                    r1.close();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            if (r2 != null) {
                try {
                    r2.close();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }

    private String exportBinaryFile(File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder((int) file.length());
        if (file.canRead()) {
            Utils.copyStreamsCloseAll(baos, new FileInputStream(file));
        }
        sb.append("MIME: application/octet-stream; encoding: Base64; length: " + (file.canRead() ? file.length() : -1)); // NOI18N
        sb.append(System.getProperty("line.separator")); // NOI18N
        sb.append(Base64Encoder.encode(baos.toByteArray(), true));
        sb.append(System.getProperty("line.separator")); // NOI18N
        return sb.toString();
    }
}

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
package org.radixware.kernel.designer.environment.project;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.modules.project.ui.ProjectUtilities;
import org.openide.LifecycleManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.radixware.kernel.designer.environment.files.FileOperationsImpl;
import org.openide.modules.ModuleInstall;
import org.openide.util.RequestProcessor;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.trace.TraceDebug;
import org.radixware.kernel.common.utils.FileOperations;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.merge.MergeEngineProvider;
import org.radixware.kernel.designer.environment.merge.AdsMergeChangesOptions;
import org.radixware.kernel.designer.environment.merge.MergeUtils;

public class Installer extends ModuleInstall {

    private static File findProjectDir() {
        for (File dir = RadixFileUtil.getUserDir(); dir != null; dir = dir.getParentFile()) {
            if (Branch.isBranchDir(dir)) {
                return dir;
            }
        }
        return null;
    }

    private static Project findProject(final File projectDir) {
        final FileObject fo = RadixFileUtil.toFileObject(projectDir);
        try {
            final Project project = ProjectManager.getDefault().findProject(fo);
            return project;
        } catch (IOException ex) {
            DialogUtils.messageError(ex);
            return null;
        }
    }

    private static void openAndExpandProject(final Project project) {
        if (!OpenProjects.getDefault().isProjectOpen(project)) {
            OpenProjects.getDefault().open(new Project[]{project}, false /* open sub projects */, true /*assinch*/);
        }
        ProjectUtilities.selectAndExpandProject(project);

    }

    private void openProject() {
        final File projectDir = findProjectDir();
        if (projectDir != null) {
            final Project project = findProject(projectDir);
            if (project != null) {
                openAndExpandProject(project);
            }
        }
    }

    @Override
    public void restored() {
        System.setProperty("org.radixware.svn.client.type", "radix");
        //RadixPropertyEditorsRegistry.getInstance(); // commented, because deprecated

        if (TraceDebug.isEnabledTraceDebug()) {
            if (System.out != null) {
                System.out.println("Radix trace debug is enabled. Trace directory \'" + TraceDebug.getTraceDirectoryPath() + "\'");
            }
        }

//        if (!SVNRepositoryAdapter.Factory.useRadixClient(true)) {
//            System.out.println("Failed to force RadixWare svn client usage. Anoter option is specified explicitely");
//        }
        FileOperations.register(new FileOperationsImpl());

        MergeEngineProvider.register(new MergeEngineProvider.MergeEngine() {

            @Override
            public void doWithDefinitions(List<Definition> list) throws Exception {
                MergeUtils.doWithDefinitions(list);
            }

            @Override
            public void doWithFiles(List<File> list) throws Exception {
                MergeUtils.doWithFiles(list);
            }

            @Override
            public boolean isAdsSelected(List<File> list) {
                return MergeUtils.isSelectedAds(list);
            }

            @Override
            public void freeSVNRepositoryOptions() {
                AdsMergeChangesOptions.freeSVNRepositoryOptions();
            }

        });

        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                final List<Project> inspectors = new LinkedList<Project>();
                try {
                    Future<Project[]> pft = OpenProjects.getDefault().openProjects();
                    Project[] projects = null;

                    projects = pft.get();

                    if (projects != null) {
                        for (Project p : projects) {
                            if (p instanceof InspectorProject) {
                                inspectors.add(p);
                            }
                        }
                    }
                    if (!inspectors.isEmpty()) {
                        OpenProjects.getDefault().close(inspectors.toArray(new Project[inspectors.size()]));
                    }
                } catch (final InterruptedException ex) {
                    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtils.messageError(ex);
                        }
                    });

                } catch (final ExecutionException ex) {
                    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtils.messageError(ex);
                        }
                    });
                }
                WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                    @Override
                    public void run() {
                        //open default project if no projects are opened
                        if (OpenProjects.getDefault().getOpenProjects().length == 0) {
                            openProject();
                        }
                    }
                });
            }
        });

        if (!RadixFileUtil.isDebugMode()) {
            removeProjectMenu(); // RADIX-3279
        }

        System.setProperty("netbeans.indexing.noFileRefresh", "true"); // RADIX-3581
    }

    private void removeProjectMenu() {
        final FileObject root = FileUtil.getConfigRoot();
        final FileObject fo = root.getFileObject("Menu/File");
        for (FileObject child : fo.getChildren()) {
            try {
                final Object attrValue = child.getAttribute("position");
                if (attrValue != null) {
                    Long l = Long.valueOf(attrValue.toString());
                    if (l < 1500) { // File/Save
                        child.delete(); // only clean and build will revert previous state
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static final void inspect(File projectDir) {
        LifecycleManager.getDefault().saveAll();
        if (projectDir != null) {
            final Project project = findProject(projectDir);
            if (project != null) {
                OpenProjects.getDefault().close(OpenProjects.getDefault().getOpenProjects());
                openAndExpandProject(project);
            }
        }
    }
}

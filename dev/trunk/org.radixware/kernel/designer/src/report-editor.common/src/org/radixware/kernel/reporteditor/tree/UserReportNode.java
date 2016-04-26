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
package org.radixware.kernel.reporteditor.tree;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.actions.EditAction;
import org.openide.actions.SaveAction;
import org.openide.cookies.EditCookie;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import static org.openide.nodes.Children.LEAF;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersion;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction;
import org.radixware.kernel.designer.ads.build.actions.CompileDefinitionAction;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;
import org.radixware.kernel.reporteditor.repository.UserReportSaveCookie;

public class UserReportNode extends AbstractNode {

    private static class UserReportEditCookie implements EditCookie {

        private final UserReport report;

        public UserReportEditCookie(UserReport report) {
            this.report = report;
        }

        @Override
        public void edit() {
            this.report.openEditor();
        }
    }

    private static final class VersionKey {

        private final ReportVersion version;
        private final AdsUserReportClassDef clazz;

        public VersionKey(ReportVersion version, AdsUserReportClassDef clazz) {
            this.version = version;
            this.clazz = clazz;
        }

        @Override
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                return true;
            }
            if (obj instanceof VersionKey) {
                VersionKey other = (VersionKey) obj;
                return other.version == version && other.clazz == this.clazz;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + Objects.hashCode(this.version);
            hash = 59 * hash + Objects.hashCode(this.clazz);
            return hash;
        }
    }

    private static final class ReportNodeChildren extends Children.Keys<VersionKey> {

        private final Id reportId;
        private ChangeListener versionsListener;
        private static final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                final Thread t = new Thread(r);
                t.setName("RadixWare ReportNodeChildren Report Versions Getter Thread");
                return t;
            }
        });

        public ReportNodeChildren(Id reportId) {
            this.reportId = reportId;
            setKeys(new VersionKey[]{new VersionKey(null, null)});
        }

        private UserReport getReport() {
            return UserExtensionManager.getInstance().getUserReports().findReportById(reportId);
        }

        @Override
        protected Node[] createNodes(VersionKey key) {
            if (key.version == null) {
                Node node = new AbstractNode(LEAF);
                node.setDisplayName("Loading...");
                RequestProcessor.submit(new Runnable() {
                    @Override
                    public void run() {
                        updateKeys();
                    }
                });
                return new Node[]{node};
            } else {
                ReportVersionNode node = new ReportVersionNode(key.version, key.clazz);
                return new Node[]{node};
            }

        }

        private void updateKeys() {

            Callable<java.util.List<ReportVersion>> listGetter = new Callable<java.util.List<ReportVersion>>() {
                @Override
                public java.util.List<ReportVersion> call() throws Exception {
                    UserReport report = getReport();
                    if (report == null) {
                        return Collections.emptyList();
                    }
                    return report.getVersions().listVisible();
                }
            };

            final Future<java.util.List<ReportVersion>> task = executor.submit(listGetter);

            
            try {
                updateKeysImpl(task.get());
            } catch (InterruptedException | ExecutionException ex) {
            } finally {
                synchronized (this) {
                    UserReport report = getReport();
                    if (versionsListener == null && report != null) {
                        versionsListener = new ChangeListener() {
                            @Override
                            public void stateChanged(ChangeEvent e) {
                                RequestProcessor.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateKeys();
                                    }
                                });
                            }
                        };
                        report.getVersions().addChangeListener(versionsListener);
                    }
                }
            }
        }

        private void updateKeysImpl(java.util.List<ReportVersion> visible) {
            final java.util.List<VersionKey> newKeys = new ArrayList<>();

            if (visible.isEmpty()) {
                setKeys(Collections.<VersionKey>emptyList());
                return;
            }

            for (ReportVersion v : visible) {
                AdsUserReportClassDef clazz = v.findReportDefinition();
                if (clazz != null) {
                    newKeys.add(new VersionKey(v, clazz));
                }
            }
            Collections.sort(newKeys, new Comparator<VersionKey>() {
                @Override
                public int compare(VersionKey o1, VersionKey o2) {
                    return o1.version.getVersion() == o2.version.getVersion() ? 0 : o1.version.getVersion() > o2.version.getVersion() ? 1 : -1;
                }
            });

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setKeys(newKeys);
                }
            });

        }
    }
    private final InstanceContent lookupContent;
    private final UserReportEditCookie editCookie;
    private final DeleteReportAction.Cookie deleteCookie;
    private final UninstallReportAction.Cookie uninstallCookie;
    private final RenameReportAction.Cookie renameCookie;
    private final SaveCookie saveCookie;
    private final AddNewVersionAction.Cookie addVersionCookie;
    private final ExportReportAction.Cookie exportCookie;
    private final ImportReportVersionAction.Cookie importCookie;
    private final CopyReportAction.Cookie copyCookie;
    private final Id reportId;

    public UserReportNode(UserReport report) {
        this(report, new InstanceContent());
    }

    public UserReportNode(final UserReport report, InstanceContent lookupContent) {
        super(new ReportNodeChildren(report.getId()), new AbstractLookup(lookupContent));
        this.reportId = report.getId();
        this.lookupContent = lookupContent;
        this.lookupContent.add(UserExtensionManager.getInstance());
        setDisplayName(report.getName());

        addCookie(editCookie = new UserReportEditCookie(report));
        addCookie(uninstallCookie = new UninstallReportAction.Cookie(report));
        addCookie(deleteCookie = new DeleteReportAction.Cookie(report));
        addCookie(renameCookie = new RenameReportAction.Cookie(report));
        addCookie(saveCookie = new UserReportSaveCookie(report));
        addCookie(addVersionCookie = new AddNewVersionAction.Cookie(report));
        addCookie(exportCookie = new ExportReportAction.Cookie(report));
        addCookie(importCookie = new ImportReportVersionAction.Cookie(report));
        addCookie(copyCookie = new CopyReportAction.Cookie(report));
        addCookie(new AbstractBuildAction.CompileCookie(
                new AbstractBuildAction.BuildCookie.RadixObjectLookupDelegate() {
                    @Override
                    public RadixObject getRadixObject() {
                        return report.getVersions().getCurrent().findReportDefinition();
                    }
                }));
        report.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("name".equals(evt.getPropertyName())) {
                    setDisplayName(report.getName());
                    fireNameChange((String) evt.getOldValue(), (String) evt.getNewValue());
                }
            }
        });
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(EditAction.class);
    }

    public UserReport getUserReport() {
        return UserExtensionManager.getInstance().getUserReports().findReportById(reportId);
    }

    @Override
    public Image getIcon(int type) {
        return AdsDefinitionIcon.CLASS_USER_REPORT.getImage();
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    private void addCookie(Cookie c) {
        this.lookupContent.add(c);
    }

    @Override
    public SystemAction[] getActions(boolean context) {
        return new SystemAction[]{
            SystemAction.get(RenameReportAction.class),
            SystemAction.get(EditAction.class),
            SystemAction.get(SaveAction.class),
            null,
            SystemAction.get(AddNewVersionAction.class),
            null,
            SystemAction.get(ImportReportVersionAction.class),
            SystemAction.get(ExportReportAction.class),
            null,
            SystemAction.get(UninstallReportAction.class),
            SystemAction.get(DeleteReportAction.class),
            SystemAction.get(CopyReportAction.class),
            SystemAction.get(CompileDefinitionAction.class)
        };
    }
}

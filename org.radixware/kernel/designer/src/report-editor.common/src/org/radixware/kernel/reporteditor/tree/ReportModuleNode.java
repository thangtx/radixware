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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.Action;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.userreport.repository.UserReport.ReportVersion;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;
import org.radixware.schemas.adsdef.AdsUserReportDefinitionDocument;
import org.radixware.schemas.adsdef.UserReportDefinitionType;

public class ReportModuleNode extends RadixObjectNode {

    private static final class ReportsModuleChildren extends Children.Keys<UserReport> {

        private final ReportsModule module;
        private ReentrantLock createLock = new ReentrantLock();
        private ReentrantLock updateLock = new ReentrantLock();
        private final PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("report-list".equals(evt.getPropertyName())) {
                    if (module.getContainer() != null) {
                        updateKeys();
                    } else {
                        UserExtensionManager.getInstance().getUserReports().removePropertyChangeListener(listener);
                    }
                }
            }
        };

        public ReportsModuleChildren(ReportsModule module) {
            this.module = module;
            updateKeys();
            UserExtensionManager.getInstance().getUserReports().addPropertyChangeListener(listener);
        }

        @Override
        protected Node[] createNodes(UserReport t) {
            if (t == null) {
                return new Node[0];
            } else {
                createLock.lock();
                try {
                    UserReportNode node = new UserReportNode(t);//UserExtensionManager.getInstance().findOrCreateNode(t);
                    return new Node[]{node};
                } finally {
                    createLock.unlock();
                }
            }
        }

        private void updateKeys() {
            Callable<List<UserReport>> listGetter = new Callable<List<UserReport>>() {
                @Override
                public List<UserReport> call() throws Exception {
                    return new ArrayList<>(module.getReports());
                }
            };

            final Future<List<UserReport>> task = RequestProcessor.submit(listGetter);

            if (!task.isDone()) {
                RequestProcessor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            updateKeysImpl(task.get());
                        } catch (InterruptedException | ExecutionException ex) {
                        }
                    }
                });
            } else {
                try {
                    updateKeysImpl(task.get());
                } catch (InterruptedException | ExecutionException ex) {
                }
            }
        }

        private void updateKeysImpl(List<UserReport> reports) {
            updateLock.lock();
            try {

                Collections.sort(reports, new Comparator<UserReport>() {
                    @Override
                    public int compare(UserReport o1, UserReport o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                setKeys(reports);
            } finally {
                updateLock.unlock();
            }
        }
    }
    private final AddNewReportAction.Cookie addNewReportCookie;
    private final ImportReportAction.Cookie importReportCookie;
    private final ExportAllReportsAction.Cookie exportAllCookie;

    public ReportModuleNode(ReportsModule module) {
        super(module, new ReportsModuleChildren(module));
        addNewReportCookie = new AddNewReportAction.Cookie(module);
        addCookie(addNewReportCookie);
        addCookie(importReportCookie = new ImportReportAction.Cookie(module));
        addCookie(exportAllCookie = new ExportAllReportsAction.Cookie(module));
        getLookupContent().add(UserExtensionManager.getInstance());
    }

    @Override
    protected void addPrimaryCustomActions(List<Action> actions) {
        actions.add(SystemAction.get(AddNewReportAction.class));
        actions.add(SystemAction.get(ImportReportAction.class));
        actions.add(null);

    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(ExportAllReportsAction.class)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        UserExtensionManager.getInstance().removeCopiedReportInfo();
        return super.clipboardCopy();
    }

    @Override
    protected void createPasteTypes(final Transferable transferable, final List<PasteType> pasteTypes) {
        if (UserExtensionManager.getInstance().getCopiedReportInfo() != null) {
            pasteTypes.add(new PastTypeUserReport(UserExtensionManager.getInstance().getCopiedReportInfo()));
            return;
        }
        final List<PasteType> localPasteTypes = new LinkedList<>();
        super.createPasteTypes(transferable, localPasteTypes);
        if (!localPasteTypes.isEmpty()) {
            pasteTypes.addAll(localPasteTypes);
            //return;
        } else {
            if (transferable != null) {
                NodeTransfer.Paste paste = NodeTransfer.findPaste(transferable);
                Node node = NodeTransfer.node(transferable, NodeTransfer.CLIPBOARD_COPY);
                if (node instanceof UserReportNode) {
                    final UserReport report = ((UserReportNode) node).getUserReport();
                    if (report != null) {
                        pasteTypes.add(new PasteType() {
                            @Override
                            public Transferable paste() throws IOException {
                                final Transferable[] result = new Transferable[]{null};
                                final CountDownLatch lock = new CountDownLatch(1);
                                ProgressUtils.showProgressDialogAndRun(new Runnable() {
                                    @Override
                                    public void run() {
                                        ReportsModule currentModule = (ReportsModule) ReportModuleNode.this.getRadixObject();
                                        try {
                                            result[0] = new UserReportTransferable(report.moveTo(currentModule));
                                        } catch (IOException ex) {
                                            Exceptions.printStackTrace(ex);
                                        } finally {
                                            lock.countDown();
                                        }
                                    }
                                }, "Moving report...");
                                try {
                                    lock.await();
                                } catch (InterruptedException ex) {
                                }
                                return result[0];
                            }
                        });
                    }
                }
            }
        }
    }

    class PastTypeUserReport extends PasteType {

        private List<UserExtensionManager.CopiedReportInfo> copiedReportInfo;

        PastTypeUserReport(List<UserExtensionManager.CopiedReportInfo> copiedReportInfo) {
            this.copiedReportInfo = copiedReportInfo;

        }

        @Override
        public Transferable paste() throws IOException {
            final Transferable[] result = new Transferable[]{null};
            final CountDownLatch lock = new CountDownLatch(1);
            ProgressUtils.showProgressDialogAndRun(new Runnable() {
                @Override
                public void run() {
                    if (copiedReportInfo != null) {
                        try {
                            UserExtensionManager.CopiedReportInfo currentInfo = null;
                            for (UserExtensionManager.CopiedReportInfo ci : copiedReportInfo) {
                                if (ci.isCurrent()) {
                                    currentInfo = ci;
                                    break;
                                }
                            }
                            if (currentInfo != null) {
                                AdsReportClassDef newReportDef = (AdsReportClassDef) currentInfo.getCopiedReport().getClipboardSupport().duplicate();
                                String reportName = currentInfo.getCopiedReport().getName();
                                int index = reportName.indexOf('.');
                                if (index > 0) {
                                    reportName = reportName.substring(0, index);
                                }
                                newReportDef.setName(reportName + "(Copy)");
                                final UserReport userreport = UserReport.create((ReportsModule) getClipboardPresentation(), newReportDef);

                                for (UserExtensionManager.CopiedReportInfo ci : copiedReportInfo) {
                                    AdsUserReportDefinitionDocument xDoc = AdsUserReportDefinitionDocument.Factory.newInstance();
                                    UserReportDefinitionType xDef = xDoc.addNewAdsUserReportDefinition();
                                    newReportDef = (AdsReportClassDef) ci.getCopiedReport().getClipboardSupport().duplicate();
                                    newReportDef.appendTo(xDef.addNewReport(), AdsDefinition.ESaveMode.NORMAL);
                                    AdsLocalizingBundleDef sb = ci.getStrings();
                                    if (sb != null) {
                                        sb.appendTo(xDef.addNewStrings(), AdsDefinition.ESaveMode.NORMAL);
                                    }
                                    ReportVersion v = userreport.getVersions().addNewVersion(xDef);
                                    v.setVisible(ci.isVisible());
                                    if (ci.isCurrent()) {
                                        UserExtensionManager.getInstance().makeCurrent(null, true, v, userreport);
                                    }
                                }
                                userreport.getVersions().removeVersion(userreport.getVersions().list().get(0));
                                result[0] = new UserReportTransferable(userreport);
                            }
                            //
                            //SwingUtilities.invokeLater(new Runnable() {

                            //    @Override
                            //   public void run() {
                            //       DialogUtils.goToObject(userreport.getVersions().getCurrent().findReportDefinition());
                            //   }
                            //});
                            //} catch (TransformerConfigurationException ex) {
                            //    Exceptions.printStackTrace(ex);
                            // } catch (TransformerException ex) {
                            //    Exceptions.printStackTrace(ex);
                            //} catch (IOException ex) {
                            //    Exceptions.printStackTrace(ex);
                            //}
                        } finally {
                            //UserExtensionManager.getInstance().removeCopiedReportInfo();
                            lock.countDown();
                        }
                    }
                }
            }, "Past report...");
            try {
                lock.await();
            } catch (InterruptedException ex) {
            }
            return result[0];
        }
    }

    public static class UserReportTransferable implements Transferable {

        private final UserReport report;

        public UserReportTransferable(UserReport report) {
            this.report = report;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{new UserReportDataFlawor(report)};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor instanceof UserReportDataFlawor;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor instanceof UserReportDataFlawor) {
                return ((UserReportDataFlawor) flavor).report;
            } else {
                return null;
            }
        }
    }

    public static class UserReportDataFlawor extends DataFlavor {

        private final UserReport report;

        public UserReportDataFlawor(UserReport report) {
            this.report = report;
        }
    }
}

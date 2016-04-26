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

import java.util.concurrent.CountDownLatch;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import static org.openide.util.actions.CookieAction.MODE_EXACTLY_ONE;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;
import org.radixware.schemas.adsdef.AdsUserReportDefinitionDocument;
import org.radixware.schemas.adsdef.UserReportDefinitionType;


public class CopyReportAction  extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private final UserReport report;

        public Cookie(final UserReport report) {
            this.report = report;
        }

        public void execute() {
            /*java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            try {
                report.exportReport(out, null);
                AdsUserReportExchangeDocument xDoc=AdsUserReportExchangeDocument.Factory.parse(out.toString());
                xDoc.getAdsUserReportExchange().setId(Id.Factory.newInstance(EDefinitionIdPrefix.USER_DEFINED_REPORT));
                xDoc.getAdsUserReportExchange().setName(xDoc.getAdsUserReportExchange().getName()+"Copy");               
                UserExtensionManager.getInstance().setCopiedReport(xDoc);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (XmlException ex) {
                Exceptions.printStackTrace(ex);
            }*/
            
            if (report != null ) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                            final CountDownLatch lock = new CountDownLatch(1);
                            //StringBuilder sb=new StringBuilder(reports.size() > 1 ? "Copy reports " : "Copy report ");
                           // for(UserReport report:reports){
                            //    sb.append(report.getName()).append(", ");
                           // }
                            
                            ProgressUtils.showProgressDialogAndRun(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        for(UserReport.ReportVersion version:report.getVersions().list()){                                            
                                            final AdsUserReportClassDef reportDef=version.findReportDefinition();
                                            final AdsLocalizingBundleDef sb = reportDef.findExistingLocalizingBundle();
                                            //AdsReportClassDef newReportDef=(AdsReportClassDef)reportDef.getClipboardSupport().duplicate();                                           
                                            //if(version.isCurrent())
                                            //     newReportDef.setName(report.getName()+"(Copy)");
                                            UserExtensionManager.getInstance().addCopiedReportInfo(reportDef, version.isVisible(), version.isCurrent(),sb);                                            
                                        }
                                        /*newReportDef.setName(report.getName()+"(Copy)");        

                                        List<AdsUserReportDefinitionDocument> xDocs = new ArrayList<>();
                                        AdsUserReportDefinitionDocument xDoc=versionToXml(report.getVersions().getCurrent());
                                       // if(xDoc!=null)
                                       //     xDocs.add(xDoc);
                                        for(UserReport.ReportVersion version:report.getVersions().list()){
                                            //if(!version.isCurrent()){
                                                xDoc = versionToXml(version);
                                                if(xDoc!=null)
                                                    xDocs.add(xDoc);
                                            //}
                                        }*/
                                        
                                    } catch (RadixError e) {
                                        DialogUtils.messageError(e);
                                    } finally {
                                        lock.countDown();
                                    }
                                }
                                
                               private AdsUserReportDefinitionDocument versionToXml(final UserReport.ReportVersion version){
                                    final AdsUserReportClassDef report = version.findReportDefinition();
                                    AdsUserReportDefinitionDocument xDoc=null;
                                    if (report != null) { 
                                        xDoc = AdsUserReportDefinitionDocument.Factory.newInstance();
                                        final UserReportDefinitionType xDef=xDoc.addNewAdsUserReportDefinition();
                                        report.appendTo(xDef.addNewReport(), AdsDefinition.ESaveMode.NORMAL);
                                        // xDef.getReport().setName("Version #" + version.getOrder() + " (" + version + ")");
                                        final AdsLocalizingBundleDef sb = report.findExistingLocalizingBundle();
                                        if (sb != null) {
                                            sb.appendTo(xDef.addNewStrings(), AdsDefinition.ESaveMode.NORMAL);
                                        }                                        
                                    }
                                    return xDoc;
                               }
                                
                            },  "Copy report " + report.getName());
                            try {
                                lock.await();
                            } catch (InterruptedException ex) {
                            }
                            //if (deletedPubs.size() > 0) {
                            //    DialogUtils.messageInformation(deletedPubs.size() + " publications of report " + report.getName() + " were deleted");
                            //}
                        //}
                    }
                });
            }

        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Class<?>[] cookieClasses() {
        return new Class[]{CopyReportAction.Cookie.class};
    }

    @Override
    protected void performAction(final Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            final CopyReportAction.Cookie c = n.getCookie(CopyReportAction.Cookie.class);
            if (c != null) {
                c.execute();
            }
        }
    }

    @Override
    public String getName() {
        return "Copy Report...";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}


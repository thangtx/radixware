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

//import java.io.IOException;
//import java.text.MessageFormat;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import javax.mail.MessagingException;
//import javax.swing.JPanel;
//import org.radixware.kernel.common.builder.compare.APIComparator;
//import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
//import org.radixware.kernel.common.enums.EIsoLanguage;
//import org.radixware.kernel.common.enums.ENotificationEventType;
//import org.radixware.kernel.common.mail.SettingsForSendMail;
//import org.radixware.kernel.common.repository.Layer;
//import org.radixware.kernel.common.svn.notification.NotificationConfig;
//import org.radixware.kernel.common.svn.notification.NotificationEvent;
//import org.radixware.kernel.common.svn.notification.NotificationSubscriber;
//import org.radixware.kernel.common.svn.notification.messaging.Formatter;
//import org.radixware.kernel.common.svn.notification.messaging.changelist.ChangeList;
//import org.tmatesoft.svn.core.SVNException;


class Notifier{
}
//extends org.radixware.kernel.common.svn.notification.messaging.Notifier {
//
//    private final ReleaseFlow flow;
//    private List<NotificationEvent> releaseToTestEvents = null;
//    private String sender = null;
//    private String subject;
//    private String body;
//    private boolean noMail = false;
//
//    private static Map<String, String> createSettings(ReleaseFlow flow) {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(Formatter.RELEASE, flow.getSettings().isPatchRelease() ? flow.getSettings().getBranch().getBaseReleaseVersion() : flow.getSettings().getNumber());
//        map.put(Formatter.TIME, MessageFormat.format("{0,date, yyyy.MM.dd HH:mm:ss z}", System.currentTimeMillis()));
//        map.put(Formatter.USER, System.getProperty("user.name"));
//
//        return map;
//    }
//
//    Notifier(ReleaseFlow flow) {
//        super(new Formatter(createSettings(flow)));
//        this.flow = flow;
//    }
//
//    boolean prepare(Set<EIsoLanguage> langList,/* Map<EIsoLanguage, String> comments, */String branchPath, String clientUti) {
//        flow.getSettings().getLogger().message("Reading notification config data");
//        try {
//            NotificationConfig config = NotificationConfig.Factory.loadFrom(flow.getRepository(), branchPath, clientUti);
//            this.releaseToTestEvents = config.getEvents().getEventsByType(flow.isPatchRelease() ? ENotificationEventType.PATCH_PREPARED : ENotificationEventType.RELEASE_TO_TEST);
//            if (releaseToTestEvents == null || releaseToTestEvents.isEmpty()) {
//                noMail = true;
//                if (!flow.getSettings().getLogger().recoverableError("No events found in notification config. Email notification will not be available")) {
//                    if (flow.getSettings().TEST_MODE) {
//                        return true;
//                    }
//                    return false;
//                }
//
//            } else {
//                SettingsForSendMail settings = SettingsForSendMail.getInstance();
//                if (settings.getDefaultSenderAddress() == null || settings.getDefaultSenderAddress().isEmpty()) {
//                    noMail = true;
//                    if (!flow.getSettings().getLogger().recoverableError("Sender email address is not specified. Email notification will not be available")) {
//
//                        if (flow.getSettings().TEST_MODE) {
//                            return true;
//                        }
//                        return false;
//                    }
//                } else {
//                    sender = settings.getDefaultSenderAddress();
//                }
//                subject = releaseToTestEvents.get(0).getSubjectTemplate();
//                if (subject == null || subject.isEmpty()) {
//                    subject = "Release " + flow.getSettings().getNumber();
//                }
//                body = releaseToTestEvents.get(0).getBodyTemplate();
////                StringBuilder allComments = new StringBuilder();
////                if (langList != null) {
////                    for (EIsoLanguage language : langList) {
////                        String comment = comments.get(language);
////                        if (comment != null && !comment.isEmpty()) {
////                            allComments.append("\n\n");
////                            allComments.append(comment);
////                        }
////                    }
////                }
////                formatter.setProperty(Formatter.CHANGES, allComments.toString());
////                if (flow.getSettings().isPatchRelease() && flow.getSettings().getClientURI() != null) {
////                    formatter.setProperty(Formatter.CLIENT_NAME, flow.getSettings().getClientURI());
////                    formatter.setProperty(Formatter.PATCH_NO, flow.getSettings().getPatchNumber());
////                }
//            }
//            return true;
//        } catch (SVNException ex) {
//            noMail = true;
//            if (!flow.getSettings().getLogger().recoverableError("Problems with reading of notification configuration")) {
//                if (flow.getSettings().TEST_MODE) {
//                    return true;
//                }
//                return false;
//            } else {
//                return true;
//            }
//        }
//    }
//
//    boolean execute(Map<Layer, ChangeList> comments, Map<Layer, APIComparator.Result.LayerResults> apiChanges) {
//        if (noMail) {
//            return true;
//        }
//        Map<String, String> layerComments = new HashMap<String, String>();
//        if (comments != null) {
//            for (Map.Entry<Layer, ChangeList> e : comments.entrySet()) {
//                String fileChanges = e.getValue().getFileChangesHtml();
//                if (!fileChanges.isEmpty()) {
//                    layerComments.put("Changes in " + e.getKey().getName() + "(" + e.getKey().getURI() + ").html", fileChanges);
//                }
//            }
//        }
//        if (apiChanges != null) {
//            for (Map.Entry<Layer, APIComparator.Result.LayerResults> e : apiChanges.entrySet()) {
//                String report = e.getValue().generateHtmlReport();
//                if (!report.isEmpty()) {
//                    layerComments.put("API Changes in " + e.getKey().getName() + "(" + e.getKey().getURI() + ").html", report);
//                }
//            }
//        }
//
//        final List<NotificationSubscriber> subscribers = new LinkedList<NotificationSubscriber>();
//        for (NotificationEvent e : releaseToTestEvents) {
//            subscribers.addAll(e.getSubscribers().list());
//        }
//        //----------------------------------- message send -----------------------------------------
//        try {
//            try {
//                if (!flow.getSettings().TEST_MODE) {//do not send any mail in test mode
//                    super.notify(sender, subject, body, layerComments, subscribers);
//                }
//            } catch (MessagingException ex) {
//                return flow.getSettings().getLogger().fatal(ex.getMessage());
//            } catch (AuthenticationCancelledException ex) {
//                return false;
//            }
//        } catch (IOException ex) {
//            return flow.getSettings().getLogger().fatal(ex.getMessage());
//        }
//        return true;
//    }
//
//    @Override
//    public boolean configureMessage(JPanel configPanel) {
//        return flow.getSettings().getLogger().showMessageEditor(configPanel, "Accept notification message");
//    }
//
//    @Override
//    public void reportProgress(String message) {
//        flow.getSettings().getLogger().message(message);
//    }
//}

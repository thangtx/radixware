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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroups;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.editors.RadixObjectModalEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


class AdsReportFormEditor extends RadixObjectModalEditor<AdsReportForm> {

    private enum ETab {PAGE, BANDS, GROUPS};

    private final PagePanel pagePanel;
    private final BandsPanel bandsPanel;
    private final GroupsPanel groupsPanel;
//    private static ETab tab = ETab.PAGE;
 //   private volatile boolean init = false;

    /** Creates new form ReportFormEditor */
    protected AdsReportFormEditor(final AdsReportForm form) {
        super(form);
        //init = true;
        initComponents();

        pagePanel = new PagePanel(form);
        bandsPanel = new BandsPanel(form);
        groupsPanel = new GroupsPanel(form);
        tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_PAGE"),
                AdsDefinitionIcon.REPORT_FORM.getIcon(), pagePanel);
        tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_BANDS"),
                AdsDefinitionIcon.REPORT_BANDS.getIcon(), bandsPanel);
        tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_GROUPS"),
                AdsDefinitionIcon.REPORT_GROUP.getIcon(), groupsPanel);

//        switch (tab) {
//            case PAGE:
//                tabbedPane.setSelectedComponent(pagePanel);
//                break;
//            case BANDS:
//                tabbedPane.setSelectedComponent(bandsPanel);
//                break;
//            case GROUPS:
//                tabbedPane.setSelectedComponent(groupsPanel);
//                break;
//        }

        this.setComplete(true);
        //init = false;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public String getTitle() {
        return getRadixObject().getTypeTitle() + " Editor";
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();

        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
//        if (init)
//            return;
//        if (tabbedPane.getSelectedComponent() == pagePanel) {
//            tab = ETab.PAGE;
//        } else if (tabbedPane.getSelectedComponent() == bandsPanel) {
//            tab = ETab.BANDS;
//        } else if (tabbedPane.getSelectedComponent() == groupsPanel) {
//            tab = ETab.GROUPS;
//        }
    }//GEN-LAST:event_tabbedPaneStateChanged

    @Override
    public boolean open(final OpenInfo openInfo) {
        final RadixObject obj = openInfo.getTarget();
        if (obj instanceof AdsReportForm) {
            tabbedPane.setSelectedComponent(pagePanel);
        } else if (obj instanceof AdsReportBand) {
            tabbedPane.setSelectedComponent(bandsPanel);
        } else if (obj instanceof AdsReportGroups) {
            tabbedPane.setSelectedComponent(groupsPanel);
        }
        return super.open(openInfo);
    }

    @Override
    protected void apply() {
//        pagePanel.apply();
//        bandsPanel.apply();
//        groupsPanel.apply();
    }

    @Override
    public void update() {
        //
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsReportForm> {

        @Override
        public RadixObjectEditor<AdsReportForm> newInstance(final AdsReportForm form) {
            return new AdsReportFormEditor(form);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

}
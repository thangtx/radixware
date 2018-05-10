/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EReportBandType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;

/**
 *
 * @author akrylov
 */
public class BandCountEditor extends javax.swing.JPanel {

    private AdsReportForm.Bands bands;
    private EReportBandType bandType = EReportBandType.TITLE;

    /**
     * Creates new form BandCountEditor
     */
    public BandCountEditor() {
        initComponents();
        btAddBand.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        btAddBand.setText("");
        btRemoveBand.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        btRemoveBand.setText("");
        setBandType(bandType);
    }

    public EReportBandType getBandType() {
        return bandType;
    }

    public final void setBandType(EReportBandType bandType) {
        this.bandType = bandType;
        updateTitle();
    }

    private void updateTitle() {
        String title;
        switch (bandType) {
            case PAGE_HEADER:
                title = "Page Header";
                break;
            case TITLE:
                title = "Title";
                break;
            case COLUMN_HEADER:
                title = "Columns Header";
                break;
            case DETAIL:
                title = "Details";
                break;
            case SUMMARY:
                title = "Summary";
                break;
            case PAGE_FOOTER:
                title = "Page Footer";
                break;
            default:
                throw new IllegalStateException();
        }
        lblBandKindTitle.setText(title);
    }

    public void open(AdsReportForm form) {
        switch (bandType) {
            case PAGE_HEADER:
                this.bands = form.getPageHeaderBands();
                break;
            case TITLE:
                this.bands = form.getTitleBands();
                break;
            case COLUMN_HEADER:
                this.bands = form.getColumnHeaderBands();
                break;
            case DETAIL:
                this.bands = form.getDetailBands();
                break;
            case SUMMARY:
                this.bands = form.getSummaryBands();
                break;
            case PAGE_FOOTER:
                this.bands = form.getPageFooterBands();
                break;
            default:
                throw new IllegalStateException();
        }

        
        btAddBand.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsReportBand band = AdsReportBand.createNewStyleBand();
                bands.add(band);
                updateEditorState();
            }
        });
        btRemoveBand.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!bands.isEmpty()) {
                    bands.remove(bands.get(bands.size() - 1));
                    updateEditorState();
                }
            }
        });
        updateEditorState();
    }
    public void update(){
        updateEditorState();
    }

    private void updateEditorState() {
        btAddBand.setEnabled(!bands.isReadOnly());
        btRemoveBand.setEnabled(!bands.isReadOnly() && !bands.isEmpty());
        lblBandCount.setText(String.valueOf(bands.size()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblBandKindTitle = new javax.swing.JLabel();
        btAddBand = new javax.swing.JButton();
        lblBandCount = new javax.swing.JLabel();
        btRemoveBand = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(lblBandKindTitle, org.openide.util.NbBundle.getMessage(BandCountEditor.class, "BandCountEditor.lblBandKindTitle.text")); // NOI18N
        lblBandKindTitle.setMinimumSize(new java.awt.Dimension(100, 19));
        lblBandKindTitle.setPreferredSize(new java.awt.Dimension(100, 19));

        org.openide.awt.Mnemonics.setLocalizedText(btAddBand, org.openide.util.NbBundle.getMessage(BandCountEditor.class, "BandCountEditor.btAddBand.text")); // NOI18N
        btAddBand.setMinimumSize(new java.awt.Dimension(30, 31));
        btAddBand.setPreferredSize(new java.awt.Dimension(30, 31));

        lblBandCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(lblBandCount, org.openide.util.NbBundle.getMessage(BandCountEditor.class, "BandCountEditor.lblBandCount.text")); // NOI18N
        lblBandCount.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblBandCount.setMinimumSize(new java.awt.Dimension(50, 19));
        lblBandCount.setPreferredSize(new java.awt.Dimension(50, 19));

        org.openide.awt.Mnemonics.setLocalizedText(btRemoveBand, org.openide.util.NbBundle.getMessage(BandCountEditor.class, "BandCountEditor.btRemoveBand.text")); // NOI18N
        btRemoveBand.setMinimumSize(new java.awt.Dimension(30, 31));
        btRemoveBand.setPreferredSize(new java.awt.Dimension(30, 31));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBandKindTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btAddBand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBandCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemoveBand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btAddBand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblBandKindTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblBandCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btRemoveBand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddBand;
    private javax.swing.JButton btRemoveBand;
    private javax.swing.JLabel lblBandCount;
    private javax.swing.JLabel lblBandKindTitle;
    // End of variables declaration//GEN-END:variables
}

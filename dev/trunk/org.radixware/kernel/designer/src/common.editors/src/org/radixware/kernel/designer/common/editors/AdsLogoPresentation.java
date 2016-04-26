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

package org.radixware.kernel.designer.common.editors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.EventListenerList;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.editors.module.images.ChooseImagesDialog;


public class AdsLogoPresentation extends javax.swing.JPanel {

    private AdsDefinition definition;
    private RadixIcon radixIcon;
    private Id imageId;

    /** Creates new form AdsImagePresentation */
    public AdsLogoPresentation() {
        initComponents();
        ActionListener browseListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseImagesDialog dialog = ChooseImagesDialog.getInstanceFor(definition.getModule());
                if (dialog.chooseImage() && dialog.getSelectedImageId() != null) {
                    Id newImageId = dialog.getSelectedImageId();
                    AdsImageDef imageDef = AdsSearcher.Factory.newImageSearcher(definition).findById(newImageId).get();
                    if (imageDef != null) {
                        radixIcon = imageDef.getIcon();
                        imageId = newImageId;
                        logoHandler.setImageIcon(radixIcon != null ? radixIcon.getOriginalImage() : null);
                        getLogoIdChangeSupport().fireEvent(new LogoIdChangeEvent(newImageId));
                    } else {
                        imageId = null;
                        logoHandler.setImageIcon(null);
                    }
                    logoHandler.repaint();
                    scroll.updateUI();
                    clearButton.setEnabled(imageId != null); 
                }
            }
        };
        browseBtn.addActionListener(browseListener);
        ActionListener clearListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                imageId = null;
                radixIcon = null;
                getLogoIdChangeSupport().fireEvent(new LogoIdChangeEvent(null));
                logoHandler.setImageIcon(null);
                logoHandler.repaint();
                clearButton.setEnabled(false);
            }

        };
        clearButton.addActionListener(clearListener); 
    }

    public void open(AdsDefinition definition, Id imageId) {
        this.definition = definition;
        if (imageId != null) {
            AdsImageDef imageDef = AdsSearcher.Factory.newImageSearcher(definition).findById(imageId).get();
            RadixIcon ri = imageDef != null ? imageDef.getIcon() : null;
            if (ri != null) {
                this.radixIcon = ri;
            } else {
                this.radixIcon = null;
            }
        } else {
            this.radixIcon = null;
        }
        this.imageId = imageId;
        update();
    }

    public void update() {
        boolean hasId = imageId != null;
        clearButton.setEnabled(hasId);
        if (hasId) {
            AdsImageDef imageDef = definition.getModule().getImages().findById(imageId);
            RadixIcon ri = imageDef != null ? imageDef.getIcon() : null;
            if (ri != null) {
                this.radixIcon = ri;
                logoHandler.setImageIcon(radixIcon.getOriginalImage());
            } else {
                logoHandler.setImageIcon(null);
            }
        } else {
            if (radixIcon != null) {
                logoHandler.setImageIcon(radixIcon.getOriginalImage());
            } else {
                radixIcon = null;
            }
        }
    }

    public void setReadonly(boolean readonly) {
        logoHandler.setEnabled(!readonly);
        browseBtn.setEnabled(!readonly);
    }

    public boolean isReadonly() {
        return !logoHandler.isEnabled();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        browseBtn = new javax.swing.JButton();
        scroll = new javax.swing.JScrollPane();
        logoHandler = new org.radixware.kernel.designer.common.editors.AdsLogoViewer();
        clearButton = new javax.swing.JButton();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsLogoPresentation.class, "LogoPresentation-Label")); // NOI18N

        browseBtn.setText(org.openide.util.NbBundle.getMessage(AdsLogoPresentation.class, "LogoPresentation-Btn")); // NOI18N

        scroll.setViewportView(logoHandler);

        clearButton.setText(org.openide.util.NbBundle.getMessage(AdsLogoPresentation.class, "ClearMenuTip")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(380, 380, 380))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clearButton)
                    .addComponent(browseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {browseBtn, clearButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(browseBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton)
                        .addContainerGap())
                    .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseBtn;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel jLabel1;
    private org.radixware.kernel.designer.common.editors.AdsLogoViewer logoHandler;
    private javax.swing.JScrollPane scroll;
    // End of variables declaration//GEN-END:variables
    private LogoIdChangeSupport idChangeSupport;

    public static class LogoIdChangeEvent extends RadixEvent {

        public Id logoId;

        public LogoIdChangeEvent(Id logoId) {
            this.logoId = logoId;
        }
    }

    public interface LogoIdStateChangeListener extends IRadixEventListener<LogoIdChangeEvent> {
    }

    public static class LogoIdChangeSupport extends RadixEventSource<LogoIdStateChangeListener, LogoIdChangeEvent> {

        private EventListenerList listeners = new EventListenerList();

        @Override
        public synchronized void addEventListener(LogoIdStateChangeListener listener) {
            listeners.add(LogoIdStateChangeListener.class, listener);
        }

        @Override
        public synchronized void removeEventListener(LogoIdStateChangeListener listener) {
            listeners.remove(LogoIdStateChangeListener.class, listener);
        }

        @Override
        public void fireEvent(LogoIdChangeEvent event) {
            Object[] l = listeners.getListeners(LogoIdStateChangeListener.class);
            for (int i = 0; i <= l.length - 1; i++) {
                if (event != null) {
                    ((LogoIdStateChangeListener) l[i]).onEvent(event);
                }
            }
        }
    }

    public synchronized LogoIdChangeSupport getLogoIdChangeSupport() {
        if (idChangeSupport == null) {
            idChangeSupport = new LogoIdChangeSupport();
        }
        return idChangeSupport;
    }
}

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

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.editors.module.images.ChooseImagesDialog;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class AdsDefinitionIconPresentation extends javax.swing.JPanel {

    private static final RadixIcon NOICON = RadixWareIcons.EDIT.NO_ICON;
    private static String NO_ICON_TIP = NbBundle.getMessage(AdsDefinitionIconPresentation.class, "IconPresentation-NoIconToolTip");
    private AdsDefinition definition;
    private RadixIcon icon;
    private Id iconId;

    /** Creates new form IconViewer */
    public AdsDefinitionIconPresentation() {
        initComponents();
        final JMenuItem clearItem = new JMenuItem(NbBundle.getMessage(AdsDefinitionIconPresentation.class, "ClearMenuTip"));
        clearItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                iconId = null;
                icon = null;
                getIconIdChangeSupport().fireEvent(new IconIdChangeEvent(iconId));
                setToolTipText(NO_ICON_TIP);
                iconHandler.setIcon(NOICON.getIcon()); 
            }

        });
        JPopupMenu menu = new JPopupMenu();
        menu.add(clearItem);
        menu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                clearItem.setEnabled(iconHandler.isEnabled() && iconId != null);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        this.iconHandler.setComponentPopupMenu(menu);
    }

    public void open(AdsDefinition definition, Id iconId) {
        this.definition = definition;
        if (iconId != null) {
            AdsImageDef imageDef = AdsSearcher.Factory.newImageSearcher(definition).findById(iconId).get();
            RadixIcon ri = imageDef != null ? imageDef.getIcon() : null;
            if (ri != null) {
                this.icon = ri;
            } else {
                this.icon = NOICON;
            }
        } else {
            this.icon = NOICON;
        }
        this.iconId = iconId;
        update();
    }

    public void update() {
        if (iconId != null) {
            AdsImageDef imageDef = AdsSearcher.Factory.newImageSearcher(definition).findById(iconId).get();
            RadixIcon ri = imageDef != null ? imageDef.getIcon() : null;
            if (ri != null) {
                setToolTipText(imageDef.getToolTip());
                iconHandler.setIcon(ri.getIcon());
            } else {
                iconHandler.setIcon(icon != null ? icon.getIcon() : NOICON.getIcon());
                if (icon.equals(NOICON)) {
                    setToolTipText(NbBundle.getMessage(AdsDefinitionIconPresentation.class, "IconPresentation-NotFound"));
                }
            }
        } else {
            icon = NOICON;
            iconHandler.setIcon(NOICON.getIcon());
            setToolTipText(NO_ICON_TIP);
        }
    }

    public void setReadonly(boolean readonly) {
        iconHandler.setEnabled(!readonly);
    }

    public boolean isReadonly() {
        return !iconHandler.isEnabled();
    }

    @Override
    public Dimension getPreferredSize() {
        if (getBorder() != null) {
            Border iconBorder = getBorder();
            Dimension iconPreferred = iconHandler.getPreferredSize();
            if (iconBorder instanceof ComponentTitledBorder) {
                ComponentTitledBorder asTitledBorder = (ComponentTitledBorder) iconBorder;
                int bw = asTitledBorder.getComponentWidth();
                if (bw > -1 && (bw + asTitledBorder.getOffset()) > iconPreferred.width) {
                    final int edge = bw + 2 * asTitledBorder.getOffset();
                    Dimension result = new Dimension(edge, edge);
                    return result;
                } else {
                    return iconPreferred;
                }
            } else {
                Insets inset = getBorder().getBorderInsets(iconHandler);
                int r = iconPreferred.width;
                if (inset != null) {
                    return new Dimension(r + inset.right, r + inset.top);
                }
                return iconPreferred;
            }
        }
        return super.getPreferredSize();
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (preferredSize != null) {
            int r = preferredSize.width;
            iconHandler.setPreferredSize(new Dimension(r, r));
            if (getBorder() != null) {
                Insets inset = getBorder().getBorderInsets(iconHandler);
                if (inset != null) {
                    super.setPreferredSize(new Dimension(r + inset.right, r + inset.top));
                }
            }
        } else {
            super.setPreferredSize(preferredSize);
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        if (maximumSize != null) {
            int r = maximumSize.width;
            iconHandler.setMaximumSize(new Dimension(r, r));
            if (getBorder() != null) {
                Insets inset = getBorder().getBorderInsets(iconHandler);
                if (inset != null) {
                    super.setMaximumSize(new Dimension(r + inset.right, r + inset.top));
                }
            }
        } else {
            super.setMaximumSize(maximumSize);
        }
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        if (minimumSize != null) {
            int r = minimumSize.width;
            iconHandler.setMinimumSize(new Dimension(r, r));
            if (getBorder() != null) {
                Insets inset = getBorder().getBorderInsets(iconHandler);
                if (inset != null) {
                    super.setMinimumSize(new Dimension(r + inset.right, r + inset.top));
                }
            }
        } else {
            super.setMinimumSize(minimumSize);
        }
    }

    @Override
    public int getBaseline(int width, int height) {
        return iconHandler.getBaseline(width, height);
    }

    @Override
    public boolean requestFocusInWindow() {
        return iconHandler.requestFocusInWindow();
    }

    @Override
    public void setToolTipText(String text) {
        if (text != null) {
            iconHandler.setToolTipText(text);
        }
        super.setToolTipText(text);
    }

    public boolean chooseNewIcon(){
        ChooseImagesDialog dialog = ChooseImagesDialog.getInstanceFor(definition.getModule());
        if (dialog.chooseImage() && dialog.getSelectedImageId() != null) {
            AdsImageDef imageDef = dialog.getSelectedAdsImageDef();
            if (imageDef != null) {
                if (definition != null){
                    AdsModule newIconOwner = imageDef.getModule();
                    boolean contains = definition.getModule().getDependences().contains(newIconOwner);
                    if (!contains && !newIconOwner.equals(definition.getModule())){
                        definition.getModule().getDependences().add(newIconOwner); 
                    }
                }
                icon = imageDef.getIcon();
                iconId = imageDef.getId();//newImageId;
                iconHandler.setIcon(icon != null ? icon.getIcon() : NOICON.getIcon());
                getIconIdChangeSupport().fireEvent(new IconIdChangeEvent(imageDef.getId()/*newImageId*/));
            } else {
                iconId = null;
                iconHandler.setIcon(NOICON.getIcon());
            }
            return true;
        }
        return false;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        iconHandler = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setMinimumSize(new java.awt.Dimension(51, 51));
        setPreferredSize(new java.awt.Dimension(51, 51));

        iconHandler.setMargin(new java.awt.Insets(14, 14, 14, 14));
        iconHandler.setMaximumSize(new java.awt.Dimension(32767, 32767));
        iconHandler.setMinimumSize(new java.awt.Dimension(51, 51));
        iconHandler.setPreferredSize(new java.awt.Dimension(51, 51));
        iconHandler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iconHandlerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(iconHandler, javax.swing.GroupLayout.PREFERRED_SIZE, 47, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(iconHandler, javax.swing.GroupLayout.PREFERRED_SIZE, 47, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void iconHandlerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iconHandlerActionPerformed
        chooseNewIcon();
}//GEN-LAST:event_iconHandlerActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton iconHandler;
    // End of variables declaration//GEN-END:variables
    private IconIdChangeSupport idChangeSupport;

    public static class IconIdChangeEvent extends RadixEvent {

        public Id iconId;

        public IconIdChangeEvent(Id iconId) {
            this.iconId = iconId;
        }
    }

    public interface IconIdStateChangeListener extends IRadixEventListener<IconIdChangeEvent> {
    }

    public static class IconIdChangeSupport extends RadixEventSource<IconIdStateChangeListener, IconIdChangeEvent> {

        private EventListenerList listeners = new EventListenerList();

        @Override
        public synchronized void addEventListener(IconIdStateChangeListener listener) {
            listeners.add(IconIdStateChangeListener.class, listener);
        }

        @Override
        public synchronized void removeEventListener(IconIdStateChangeListener listener) {
            listeners.remove(IconIdStateChangeListener.class, listener);
        }

        @Override
        public void fireEvent(IconIdChangeEvent event) {
            Object[] l = listeners.getListeners(IconIdStateChangeListener.class);
            for (int i = 0; i <= l.length - 1; i++) {
                if (event != null) {
                    ((IconIdStateChangeListener) l[i]).onEvent(event);
                }
            }
        }
    }

    public synchronized IconIdChangeSupport getIconIdChangeSupport() {
        if (idChangeSupport == null) {
            idChangeSupport = new IconIdChangeSupport();
        }
        return idChangeSupport;
    }
}

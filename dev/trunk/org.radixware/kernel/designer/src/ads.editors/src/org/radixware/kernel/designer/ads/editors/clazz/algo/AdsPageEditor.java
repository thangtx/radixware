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

package org.radixware.kernel.designer.ads.editors.clazz.algo;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.netbeans.spi.palette.PaletteController;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponentGroup;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette.Palette;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.widget.BaseWidget;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsPageEditor extends RadixObjectEditor<AdsPage> {

    private GraphSceneImpl scene = null;

    public AdsPageEditor(AdsPage page) {
        super(page);
        initComponents();
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsPage> {

        @Override
        public IRadixObjectEditor<AdsPage> newInstance(AdsPage page) {
            return new AdsPageEditor(page);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public AdsPage getPage() {
        return getRadixObject();
    }
    private final InstanceContent lookupContent = new InstanceContent();
    private final Lookup lookup = new AbstractLookup(lookupContent);

    @Override
    public Lookup getLookup() {
        AdsPage obj = getRadixObject();
        if (obj.getContainer() != null) {
            PaletteController palette = lookup.lookup(PaletteController.class);
            if (obj.isReadOnly()) {
                if (palette != null) {
                    lookupContent.remove(palette);
                }
            } else {
                if (palette == null) {
                    lookupContent.add(Palette.PALETTE);
                } 
                Palette.updatePalette(obj);
            }
        }
//        setSheet(createSheet());
        return lookup;
    }

    @Override
    public boolean open(OpenInfo info) {
        final AdsPage page = getPage();

        if (scene == null) {
            setLayout(new BorderLayout());

            scene = new GraphSceneImpl(this);
            JComponent myView = scene.createView();

            JScrollPane shapePane = new JScrollPane(myView);
            add(shapePane, BorderLayout.CENTER);

            TapPanel tap = new TapPanel();
            tap.setExpanded(false);
            tap.setOrientation(TapPanel.DOWN);
            JLabel label = new JLabel("");
            label.setBorder(new EmptyBorder(0, 5, 5, 0));
            tap.add(label);
            tap.add(scene.createSatelliteView());
            add(tap, BorderLayout.SOUTH);

            addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent e) {
                    scene.getView().requestFocus();
                }

                @Override
                public void focusLost(FocusEvent e) {
                }
            });
        } else {
            scene.clear();
        }

        scene.setPage(page);

        RadixObject target = (info == null) ? null : info.getTarget();
        if (target != null) {
            if (target instanceof AdsBaseObject || target instanceof AdsEdge) {
                scene.Focus(target);
            } else {
                RadixObject context = target;
                do {
                    target = target.getContainer();
                } while (target != null && !(target instanceof AdsBaseObject) && !(target instanceof AdsEdge));
                if (target != null) {
                    scene.Focus(target, context);
                } else {
                    notifySelectionChanged(Collections.singletonList(page));
                }
            }
        } else {
            notifySelectionChanged(Collections.singletonList(page));
        }

        updateUI();
        return true;
    }

    @Override
    public void notifySelectionChanged(final List<? extends RadixObject> newSelectedObjects) {
        for (RadixObject obj : newSelectedObjects) {
            if (obj instanceof AdsPage) {
                ((AdsPage) obj).fireNodeUpdate();
            }
            if (obj instanceof AdsBaseObject) {
                ((AdsBaseObject) obj).fireNodeUpdate();
            }
            if (obj instanceof AdsEdge) {
                ((AdsEdge) obj).fireNodeUpdate();
            }
        }
        super.notifySelectionChanged(newSelectedObjects);
    }

    @Override
    public void update() {
        assert scene != null;
        for (AdsBaseObject node : scene.getNodes()) {
            BaseWidget widget = (BaseWidget) scene.findWidget(node);
            widget.sync();
        }
        scene.validate();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                scene.updateSelection();
            }
        });
    }

    @Override
    public boolean isShowProperties() {
        return true;
    }

    @Override
    public void onShown() {
        TopComponentGroup group = WindowManager.getDefault().findTopComponentGroup("uidesigner");
        if (group != null) {
            group.open();
        }
    }

    private void closeWindowsGroup() {
        TopComponentGroup group = WindowManager.getDefault().findTopComponentGroup("uidesigner");
        if (group != null) {
            group.close();
        }
    }

    @Override
    public void onClosed() {
        closeWindowsGroup();
    }

    @Override
    public void onHidden() {
        closeWindowsGroup();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

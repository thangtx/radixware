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

package org.radixware.kernel.designer.ads.editors.clazz.report;//GEN-LINE:initComponents

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.spi.palette.PaletteController;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette.AdsReportPalette;
import org.radixware.kernel.designer.ads.editors.clazz.report.preview.PreviewSettingsPanel;
import org.radixware.kernel.designer.ads.editors.clazz.report.preview.ReportPreviewActionsProvider;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassBodyPanel;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsReportClassEditor extends RadixObjectEditor<AdsReportClassDef> {

    private final AdsSqlClassBodyPanel sqmlPanel;
    private final AdsReportClassGeneralPanel generalPanel;
    private final DesignPanel designPanel;
    private final PreviewSettingsPanel previewPanel;
    private final IRadixEventListener<RadixEvent> selectionListener = new IRadixEventListener<RadixEvent>() {

        @Override
        public void onEvent(final RadixEvent e) {
            fireSelectionChanged();
        }
    };
    private final ChangeListener tabChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(final ChangeEvent e) {
            update();
            fireSelectionChanged();
        }
    };

    /**
     * Creates new form AdsReportClassDefEditor
     */
    protected AdsReportClassEditor(final AdsReportClassDef report) {
        super(report);
        initComponents();

        generalPanel = new AdsReportClassGeneralPanel(report);
        designPanel = new DesignPanel(report);
        sqmlPanel = new AdsSqlClassBodyPanel();
        previewPanel = new PreviewSettingsPanel(report);

        tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_GENERAL"),
                report.getIcon().getIcon(), generalPanel);
        tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_SQML"),
                AdsDefinitionIcon.CLASS_CURSOR.getIcon(), sqmlPanel);
        tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_DESIGN"),
                AdsDefinitionIcon.REPORT_FORM.getIcon(), designPanel);
        tabbedPane.addTab(NbBundle.getBundle(AdsReportClassEditor.class).getString("TAB_PREVIEW"),
                RadixWareDesignerIcon.REPORT.PREVIEW.getIcon(), previewPanel);

        tabbedPane.addChangeListener(tabChangeListener);
        designPanel.addSelectionListener(selectionListener);

        tabbedPane.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(final FocusEvent e) {
                if (tabbedPane.getSelectedComponent() == designPanel) {
                    designPanel.requestFocus();
                }
            }

            @Override
            public void focusLost(final FocusEvent e) {
                //
            }
        });
        
        generalPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                previewPanel.update();
            }
        });
        
        InputMap inputMap = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), ReportPreviewActionsProvider.PREVIEW_ACTION_NAME);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), ReportPreviewActionsProvider.COMPILE_AND_PREVIEW_ACTION_NAME);
        
        ActionMap actionMap = this.getActionMap();
        actionMap.put(ReportPreviewActionsProvider.PREVIEW_ACTION_NAME, ReportPreviewActionsProvider.getPreviewAction(report));
        actionMap.put(ReportPreviewActionsProvider.COMPILE_AND_PREVIEW_ACTION_NAME, ReportPreviewActionsProvider.getCompileAndPreviewAction(report));
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsReportClassDef> {

        @Override
        public RadixObjectEditor<AdsReportClassDef> newInstance(final AdsReportClassDef report) {
            return new AdsReportClassEditor(report);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>                        

    @Override
    public boolean open(final OpenInfo openInfo) {
        final AdsReportClassDef report = getReportClassDef();

        tabbedPane.removeChangeListener(tabChangeListener);

        for (RadixObject target = openInfo.getTarget(); target != null; target = target.getContainer()) {
            if (target == report.getForm()) {
                tabbedPane.setSelectedComponent(designPanel);
                break;
            }
            if (target == report.getSqml() || target == report.getUsedTables()) {
                tabbedPane.setSelectedComponent(sqmlPanel);
                break;
            }
        }

        tabbedPane.addChangeListener(tabChangeListener);

        generalPanel.open(openInfo);
        sqmlPanel.open(report, null);
        designPanel.open(openInfo);
        return super.open(openInfo);
    }

    public AdsReportClassDef getReportClassDef() {
        return getRadixObject();
    }

    @Override
    public void update() {
        if (tabbedPane.getSelectedComponent() == generalPanel) {
            generalPanel.update();
        } else if (tabbedPane.getSelectedComponent() == sqmlPanel) {
            sqmlPanel.update();
        } else if (tabbedPane.getSelectedComponent() == designPanel) {
            designPanel.update();
        } else if (tabbedPane.getSelectedComponent() == previewPanel) {
            previewPanel.update();
        }
    }

    private void fireSelectionChanged() {
        List<? extends RadixObject> selectedObjects;

        if (tabbedPane.getSelectedComponent() == designPanel) {
            selectedObjects = designPanel.getSelectedObjects();
        } else {
            final AdsReportClassDef report = getReportClassDef();
            selectedObjects = Collections.singletonList(report);
        }

        notifySelectionChanged(selectedObjects);
    }
    private final InstanceContent lookupContent = new InstanceContent();
    private final Lookup lookup = new AbstractLookup(lookupContent);

    @Override
    public Lookup getLookup() {
        final RadixObject obj = getRadixObject();
        if (obj.getContainer() != null) {
            final PaletteController palette = lookup.lookup(PaletteController.class);
            if (obj.isReadOnly()) {
                if (palette != null) {
                    lookupContent.remove(palette);
                }
            } else {
                if (palette == null) {
                    lookupContent.add(AdsReportPalette.getController(obj));
                }
            }
        }
//        setSheet(createSheet());
        return lookup;
    }
    // Variables declaration - do not modify                     
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration                   
}

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.changelog.ChangeLogEditor;
import org.radixware.kernel.designer.ads.editors.clazz.simple.SuperClassHierarchyPanel;
import org.radixware.kernel.designer.common.dialogs.components.DefaultFormSizePanel;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

class AdsReportClassGeneralPanel extends JPanel {

    private final AdsReportColumnsExportPanel csvExportPanel;
    private final AdsReportColumnsExportPanel xlsxExportPanel;
    private final AdsReportColumnsPanel columnsPanel;
    private final AdsReportClassDef report;
    private final DescriptionPanel descriptionEditor;
    private final ChangeLogEditor changeLogEditor;
    private final LocalizingEditorPanel titleEditor;
    private final HandleInfo handleInfo;
    private final JLabel contextParamTitle;
    private final JCheckBox cbUseDesktopCustomView;
    private final JCheckBox cbUseWebCustomView;
    private final DefaultFormSizePanel defaultSizePanel = new DefaultFormSizePanel();
    private final DefinitionLinkEditPanel cntxParamEditor;
    private final ChangeListener changeListener = new ChangeListener() {
        @Override
        public void stateChanged(final ChangeEvent e) {
            report.setContextParameterId(cntxParamEditor.getDefinitionId());
        }
    };
    private final JCheckBox cbExportTxt;
    private final JCheckBox cbIsSubreport;
    //private final AccessEditPanel accessEditPanel;
    private final SuperClassHierarchyPanel superClassPanel;
    //Added for notification report editor on changing the list of report export formats on preview panel
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public AdsReportClassGeneralPanel(final AdsReportClassDef report) {
        super(new BorderLayout()/*
         * new MigLayout("fillx, wrap")
         */);
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        final JPanel content = new JPanel(new MigLayout("fillx"));
        final JScrollPane scroller = new JScrollPane();
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setViewportView(mainContent);
        mainContent.add(content, BorderLayout.CENTER);
        JPanel persistentHeader = new JPanel(new MigLayout("fillx, wrap"));
        mainContent.add(persistentHeader, BorderLayout.NORTH);

        this.add(scroller);
        //scroller.setLayout(new MigLayout("fillx, wrap"));
        //this.getContentPane().add(scroller, BorderLayout.CENTER);

        this.report = report;
        this.descriptionEditor = new DescriptionPanel();
        this.changeLogEditor = new ChangeLogEditor(this.report);
        this.titleEditor = new LocalizingEditorPanel();
        this.cbUseDesktopCustomView = new JCheckBox("Use custom view (Desktop)");
        this.cbUseWebCustomView = new JCheckBox("Use custom view (Web)");
        this.cbIsSubreport = new JCheckBox("Subreport");

        this.handleInfo = new HandleInfo() {
            @Override
            public Id getTitleId() {
                return AdsReportClassGeneralPanel.this.report.getTitleId();
            }

            @Override
            public AdsDefinition getAdsDefinition() {
                return AdsReportClassGeneralPanel.this.report;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (multilingualStringDef == null) {
                    AdsReportClassGeneralPanel.this.report.setTitleId(null);
                } else {
                    AdsReportClassGeneralPanel.this.report.setTitleId(multilingualStringDef.getId());
                }
            }

            @Override
            protected void onLanguagesPatternChange(final EIsoLanguage language, final String newStringValue) {
                if (getAdsMultilingualStringDef() != null) {
                    getAdsMultilingualStringDef().setValue(language, newStringValue);
                }
            }
        };

        this.cbUseDesktopCustomView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ae) {
                final boolean useCustomView = cbUseDesktopCustomView.isSelected();
                if (useCustomView || DialogUtils.messageConfirmation("Delete desktop custom view?")) {
                    AdsReportClassGeneralPanel.this.report.getPresentations().getCustomViewSupport().setUseCustomView(ERuntimeEnvironmentType.EXPLORER, useCustomView);
                }
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        updateVisibility();
                    }
                });
            }
        });
        this.cbUseWebCustomView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ae) {
                final boolean useCustomView = cbUseWebCustomView.isSelected();
                if (useCustomView || DialogUtils.messageConfirmation("Delete web custom view ?")) {
                    AdsReportClassGeneralPanel.this.report.getPresentations().getCustomViewSupport().setUseCustomView(ERuntimeEnvironmentType.WEB, useCustomView);
                }
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        updateVisibility();
                    }
                });
            }
        });
        this.cbIsSubreport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final boolean isSubreport = cbIsSubreport.isSelected();
                AdsReportClassGeneralPanel.this.report.setIsSubreport(isSubreport);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        });

        this.cntxParamEditor = new DefinitionLinkEditPanel();
        cntxParamEditor.setComboMode();
        //this.accessEditPanel = new AccessEditPanel();
        this.superClassPanel = new SuperClassHierarchyPanel();

        persistentHeader.add(titleEditor, "growx");
        persistentHeader.add(descriptionEditor, "growx");

        persistentHeader.add(ChangeLogEditor.wrapPanelWithLabel(changeLogEditor), "growx");

        if (BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null) {
            content.add(cbUseDesktopCustomView);
            content.add(cbUseWebCustomView);
        }

        content.add(contextParamTitle = new JLabel("Context parameter:"), "newline");

        content.add(cntxParamEditor, "growx, newline, sx 2");

        persistentHeader.add(cbIsSubreport);
        //this.add(accessEditPanel);
        persistentHeader.add(superClassPanel, "growx");

        columnsPanel = new AdsReportColumnsPanel();
        persistentHeader.add(columnsPanel, "growx");
        
        cbExportTxt = new JCheckBox("Export to TXT");
        cbExportTxt.setSelected(this.report.getForm().isSupportsTxt());
        persistentHeader.add(cbExportTxt, "growx");
        cbExportTxt.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                AdsReportClassGeneralPanel.this.report.getForm().setTextModeAccepted(cbExportTxt.isSelected());
            }
        });

        csvExportPanel = new AdsReportColumnsExportPanel(EReportExportFormat.CSV);
        persistentHeader.add(csvExportPanel, "growx");
        csvExportPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeSupport.fireChange();
            }
        });

        xlsxExportPanel = new AdsReportColumnsExportPanel(EReportExportFormat.XLSX);
        persistentHeader.add(xlsxExportPanel, "growx");
        xlsxExportPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeSupport.fireChange();
            }
        });

        Border border = defaultSizePanel.getBorder();
        if (border instanceof TitledBorder) {
            ((TitledBorder) border).setTitle("Default size of report parameter dialog box");
        }
        content.add(defaultSizePanel, "growx,newline, sx 2");
    }

    public void open(final OpenInfo openInfo) {
        titleEditor.open(handleInfo);
        descriptionEditor.open(report);

        cbUseDesktopCustomView.setSelected(report.getPresentations().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER));
        cbUseDesktopCustomView.setEnabled(!report.isReadOnly());

        cbUseWebCustomView.setSelected(report.getPresentations().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB));
        cbUseWebCustomView.setEnabled(!report.isReadOnly());

        cbIsSubreport.setSelected(report.isSubreport());
        cbIsSubreport.setEnabled(!report.isReadOnly());

        //accessEditPanel.open(report);
        superClassPanel.open(report);
        columnsPanel.open(report, openInfo);
        csvExportPanel.open(report, openInfo);
        xlsxExportPanel.open(report, openInfo);

        defaultSizePanel.open(report.getPresentations());
        updateContextParameterEditor();
        changeLogEditor.update();
        updateVisibility();
    }

    private void updateContextParameterEditor() {
        final IFilter<AdsPropertyDef> filter = AdsVisitorProviders.newReportContextParameterFilter();
        final Collection<AdsPropertyDef> props = report.getProperties().get(EScope.ALL, filter);
        cntxParamEditor.removeChangeListener(changeListener);
        cntxParamEditor.setComboBoxValues(props, true);
        cntxParamEditor.open(report.findContextParameter(), report.getContextParameterId());
        cntxParamEditor.addChangeListener(changeListener);
        cntxParamEditor.setEnabled(!report.isReadOnly());
    }

    private void updateVisibility() {
        cbUseWebCustomView.setForeground(Color.black);
        cbUseDesktopCustomView.setForeground(Color.black);
        if (report.isSubreport()) {
            if (report.getPresentations().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                cbUseDesktopCustomView.setVisible(true);
                cbUseDesktopCustomView.setForeground(Color.red);
            } else {
                cbUseDesktopCustomView.setVisible(false);
            }
            cbUseWebCustomView.setVisible(false);
            if (report.getPresentations().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                cbUseWebCustomView.setVisible(true);
                cbUseWebCustomView.setForeground(Color.red);
            } else {
                cbUseWebCustomView.setVisible(false);
            }
            defaultSizePanel.setVisible(false);
            cntxParamEditor.setVisible(false);
            contextParamTitle.setVisible(false);
        } else {
            cbUseDesktopCustomView.setVisible(true);
            cbUseWebCustomView.setVisible(true);
            defaultSizePanel.setVisible(true);
            cntxParamEditor.setVisible(true);
            contextParamTitle.setVisible(true);
        }
    }

    public void update() {
        titleEditor.update(handleInfo);
        descriptionEditor.update();
        superClassPanel.update();
        changeLogEditor.update();

        cbUseDesktopCustomView.setSelected(report.getPresentations().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER));
        cbUseDesktopCustomView.setEnabled(!report.isReadOnly());

        cbUseWebCustomView.setSelected(report.getPresentations().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB));
        cbUseWebCustomView.setEnabled(!report.isReadOnly());

        cbIsSubreport.setSelected(report.isSubreport());
        cbIsSubreport.setEnabled(!report.isReadOnly());

        defaultSizePanel.update();
        updateContextParameterEditor();
        updateVisibility();
    }

    public final void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public final void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }
}

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

package org.radixware.kernel.designer.ads.localization.source;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.*;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.builder.impexp.Mls2XlsExporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.localization.AdsPhraseBookDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.localization.NavigationToolBarUi;
import org.radixware.kernel.designer.ads.localization.RowString;
import org.radixware.kernel.designer.ads.localization.dialog.FiltersDialog;
import org.radixware.kernel.designer.ads.localization.dialog.StatisticsPanel;
import org.radixware.kernel.designer.ads.localization.dialog.ViewHtmlDialog;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public class ToolBarUi {
    private FilterSettings filterSettings;
    private javax.swing.JToolBar toolBar;
    private MlsTablePanel panel;
    private JButton btnLoadStrings;
    private JButton btnСhooseDefs;
    private JButton btnChangeLangs;
    private JButton btnStatistics;
    private JButton btnAddToPhraseBook;
    private JButton btnViewHtml;
    private JButton xlsExporer;
    private JToggleButton btnShowFilters;
    private JTextField textSearch;
    private NavigationToolBarUi navigateToolBarUi;

    public ToolBarUi(final MlsTablePanel panel) {
        this.toolBar = panel.getToolBar();
        this.toolBar.setFloatable(false);
        this.panel = panel;
        createToolBarUi();
        navigateToolBarUi = new NavigationToolBarUi(toolBar, panel);
        createFilterTollBar();
        filterSettings = panel.getFilterSettings();
        //toolBarUi.setVisible(false);
    }

    private void createFilterTollBar() {

        final JLabel lbSearch = new JLabel(NbBundle.getMessage(ToolBarUi.class, "SEARCH"));
        toolBar.add(lbSearch);
        textSearch = new JTextField();
        textSearch.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(final CaretEvent e) {
                final String str = textSearch.getText();
                textSearch.setToolTipText(str);
                filterSettings.setSerchText(str);
                panel.updateFilter(filterSettings);
            }
        });
        textSearch.setColumns(20);
        toolBar.add(textSearch);
        textSearch.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent event) {
                panel.setFocusOnTranslation(false);
            }

            @Override
            public void focusLost(final FocusEvent event) {
                panel.setFocusOnTranslation(true);
            }
        });

        final Icon icon = RadixWareIcons.CHECK.FILTER_BY_OBJECT.getIcon(20);
        final Action actShowFilters = new AbstractAction("ShowFilters", icon) {
            @Override
            public void actionPerformed(final ActionEvent event) {
               showFilters();
            }
        };
        btnShowFilters = new JToggleButton(actShowFilters);
        setButton(btnShowFilters, NbBundle.getMessage(ToolBarUi.class, "SHOW_MORE_FILTERS"));
        btnShowFilters.setSelected(false);
    }
    
    public void update() {
        btnShowFilters.setSelected(false);
        filterSettings.init();
        textSearch.setText("");
    }

    private void createToolBarUi() {
        Icon icon = RadixWareIcons.MLSTRING_EDITOR.LOAD.getIcon(20);
        final Action actLoadStrings = new AbstractAction("Load Strings", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                panel.reloadMlStrings();
                update();
            }
        };
        
        icon = RadixWareDesignerIcon.EDIT.EDIT.getIcon(20);
        final Action actViewXLSExporer = new AbstractAction("XLS Explorer", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                Map<Layer, List<Module>> selected = panel.getLayersAndModules();
                final Mls2XlsOptionsPanel optionsPanel = new Mls2XlsOptionsPanel(selected.keySet());
                ModalDisplayer options = new ModalDisplayer(optionsPanel, "Options");
                if (options.showModal()) {
                    optionsPanel.save();
                    ProgressUtils.showProgressDialogAndRun(new Runnable() {

                        @Override
                        public void run() {
                            Mls2XlsExporter mls2XlsExporter =  new Mls2XlsExporter(optionsPanel.getBranch(), optionsPanel.getSelectedLayers(), optionsPanel.getFromDate(), optionsPanel.getToDate(), optionsPanel.getLastModifiedAuthor());
                            try {
                                mls2XlsExporter.doExport(optionsPanel.getOutputFile());
                            } catch (IOException ex) {
                                DialogUtils.messageError(ex);
                            }
                            
                        }
                    }, "Export Strings...");
                }
            }
        };
        
        icon = RadixWareIcons.MLSTRING_EDITOR.CHOOSE_DEFS.getIcon(20);
        Action actChoose = new AbstractAction("Select Definition", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                LayersVisitor layersVisitor = new LayersVisitor();
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(layersVisitor, new GoToDefinitionProvider());
                List<Definition> definitions = ChooseDefinition.chooseDefinitions(cfg);

                final List<Definition> defs = new ArrayList<>();
                if (definitions != null) {
                    for (Definition definition : definitions) {
                        defs.add(definition);
                    }
                    panel.setSelectedDefs(definitions);
                }
            }
        };
        icon = RadixWareIcons.MLSTRING_EDITOR.STATISTICS.getIcon(20);
        final Action actStatistics = new AbstractAction("Statistics", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                showStatistics();
            }
        };
        icon = RadixWareIcons.MLSTRING_EDITOR.ADD_TO_PHRASE_BOOK.getIcon(20);
        final Action actAddToPhraseBook = new AbstractAction("AddToPhraseBook", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                JPopupMenu menu = createBtnAddToPhraseBookMenu();
                menu.show(btnAddToPhraseBook, 0, btnAddToPhraseBook.getHeight());
            }
        };

        icon = RadixWareIcons.MLSTRING_EDITOR.VIEW_HTML.getIcon(20);
        final Action actViewHtml = new AbstractAction("ViewHtml", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                showHtml();
            }
        };

        String tooltip = NbBundle.getMessage(ToolBarUi.class, "LOAD_STRINGS");
        btnLoadStrings = new JButton(actLoadStrings);
        setButton(btnLoadStrings, tooltip);
        btnLoadStrings.setBackground(Color.white);

        tooltip = NbBundle.getMessage(ToolBarUi.class, "CHOOSE_DEFINITION");
        btnСhooseDefs = new JButton(actChoose);
        setButton(btnСhooseDefs, tooltip);


        final Action actChangeLangs = new AbstractAction("ChangeLangs", icon) {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                panel.changeLangs();
            }
        };    
        btnChangeLangs = new JButton(actChangeLangs);
        icon = RadixWareIcons.MLSTRING_EDITOR.CHOOSE_LANGS.getIcon(20);
        btnChangeLangs.setIcon(icon);
        tooltip = NbBundle.getMessage(ToolBarUi.class, "CHANGE_LANGUAGES");
        setButton(btnChangeLangs, tooltip);

        tooltip = NbBundle.getMessage(ToolBarUi.class, "SHOW_STATISTICS");
        btnStatistics = new JButton(actStatistics);
        setButton(btnStatistics, tooltip);

        toolBar.addSeparator();

        btnAddToPhraseBook = new JButton(actAddToPhraseBook);
        tooltip = NbBundle.getMessage(ToolBarUi.class, "ADD_TO_PHRASE_BOOK");
        setButton(btnAddToPhraseBook, tooltip);
        btnAddToPhraseBook.setEnabled(false);

        btnViewHtml = new JButton(actViewHtml);
        tooltip = NbBundle.getMessage(ToolBarUi.class, "VIEW_HTML");
        setButton(btnViewHtml, tooltip);
        btnViewHtml.setEnabled(false);
        
        xlsExporer = new JButton(actViewXLSExporer);
        tooltip = NbBundle.getMessage(ToolBarUi.class, "EXPORT_XLS");
        setButton(xlsExporer, tooltip);
        toolBar.addSeparator();
    }

    private void setButton(final AbstractButton btn, final String toolTip) {
        btn.setText(null);
        btn.setFocusable(false);
        btn.setToolTipText(toolTip);
        btn.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(btn);
    }

    private void showStatistics() {
        final StatisticsPanel p = new StatisticsPanel(panel.getRowStrings(), filterSettings.isEmpty()? null : panel.getFiltredRowStrings(), panel.getSourceLags(), panel.getTranslatedLags());
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        final ModalDisplayer md = new ModalDisplayer(p, "Statistics");
        md.showModal();
    }

    private void showHtml() {
        final ViewHtmlDialog p = new ViewHtmlDialog(panel.getCurrentRowString(), panel.getSourceLags(), panel.getTranslatedLags());
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        final ModalDisplayer md = new ModalDisplayer(p, "View Html") {
           
        };
        md.showModal();
    }

    private void showFilters() {
        final FiltersDialog dialog = new FiltersDialog();
        dialog.open(filterSettings, panel.getSourceLags(), panel.getTranslatedLags(), panel.getSelectedLayers());
        dialog.check();
        dialog.setBorder(new EmptyBorder(10, 10, 0, 10));
        StateAbstractDialog md = new StateAbstractDialog(dialog, "Filters") {
            @Override
            protected void apply() {}
        };
        
        if (md.showModal()) {
            panel.changeLayers(dialog.getSelectedLayers());
            panel.updateFilter(dialog.getNewFilterSettings());           
        }

        btnShowFilters.setSelected(!filterSettings.isEmpty() && (filterSettings.getSerchText() == null || filterSettings.getSerchText().isEmpty()));
    }
    
    public void setStatisticEnabled(final boolean enabled){
        btnStatistics.setEnabled(enabled);
    }

    public void setReadOnly(final boolean readOnly) {
        //btnChangeLangs.setEnabled(!readOnly);
        //setStatisticEnabled(!readOnly);
        navigateToolBarUi.setReadOnly(readOnly);
    }

    private class LayersVisitor extends RadixObject {

        @Override
        public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
            final Map<Layer, List<Module>> layers = panel.getLayersAndModules();
            if (layers == null) {
                final Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
                for (Branch branch : branches) {
                    branch.visit(visitor, provider);
                }
            } else {
                for (Layer layer : layers.keySet()) {
                    final List<Module> modules = layers.get(layer);
                    if (modules == null) {
                        //if(!layer.isReadOnly())
                        layer.visit(visitor, provider);
                    } else {
                        for (Module module : modules) {
                            //if(!module.isReadOnly())
                            module.visit(visitor, provider);
                        }
                    }
                }
            }
            super.visitChildren(visitor, provider);
        }
    }

    class GoToDefinitionProvider extends VisitorProvider {

        @Override
        public boolean isTarget(final RadixObject radixObject) {
            if (radixObject instanceof Definition) {
                final Definition definition = (Definition) radixObject;
                
                if ((definition instanceof AdsDefinition)
                        && (((AdsDefinition) radixObject).getDefinitionType() != EDefType.IMAGE)
                        && (((AdsDefinition) radixObject).getDefinitionType() != EDefType.LOCALIZING_BUNDLE)
                        && ((AdsDefinition) radixObject).getDefinitionType() != EDefType.XML_SCHEME){
                    return ((AdsDefinition) radixObject).isTopLevelDefinition();
                }
            }
            return false;
        }
    }

    private JPopupMenu createBtnAddToPhraseBookMenu() {
        final JPopupMenu menu = new JPopupMenu();
        final List<AdsPhraseBookDef> openedPhraseBook = panel.getOpenedPhraseBook();
        for (final AdsPhraseBookDef phraseBook : openedPhraseBook) {
            final JMenuItem item = new JMenuItem();
            item.setName(phraseBook.getName());
            item.setText(phraseBook.getName());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    panel.addCurMlStringToPhraseBook(phraseBook);
                }
            });
            menu.add(item);
        }
        return menu;
    }

    public void canAddStringToPhraseBook(final boolean enabled) {
        btnAddToPhraseBook.setEnabled(enabled);
    }

    public void canViewHtml(boolean enabled) {
        btnViewHtml.setEnabled(enabled);
    }
}

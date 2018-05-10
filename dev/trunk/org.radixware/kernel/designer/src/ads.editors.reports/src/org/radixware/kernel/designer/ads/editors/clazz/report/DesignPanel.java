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
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EReportLayout;
import org.radixware.kernel.common.enums.EReportTextFormat;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.ads.editors.clazz.report.ArrowColorButton.ColorChangeEvent;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdjustHeightAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdjustWidthAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportBandsEditAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportFormEditAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportFormRedoAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportFormUndoAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportGridLayoutAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportGroupsEditAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportHLayoutAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportMarginAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportTreeAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportVLayoutAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AdsReportVisibilityAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AlignBottomAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AlignByHeightAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AlignByWidthAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AlignCenterAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AlignLeftAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AlignMiddleAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AlignRightAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.AlignTopAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.ArrangeByHorizontalAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.ArrangeByVerticalAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.BackgroundAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.BorderAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.ClipContentAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.FontAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.GridSettingsEditAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.HAlignCenterAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.HAlignJustifyAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.HAlignLeftAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.HAlignRightAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.LineSpaicingAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.NoAdjustAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.NoClipContentAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.NoSnapEdgesAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.NoWrapWordAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.SnapBottomEdgeAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.SpanTopEdgeAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.TextFormatAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.action.WrapWordAction;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportAbstractSelectableWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportBandWidget;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportBaseContainer;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagram;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormDiagramOptions;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormUndoRedo;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.IReportCellContainer;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette.AdsReportPaletteRootNode;
import org.radixware.kernel.designer.ads.editors.clazz.report.navigator.ReportNavigator;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

class DesignPanel extends javax.swing.JPanel {

    private final AdsReportFormDiagram diagram;
//    private final ArrowColorButton.ColorChangeEventListener bgListener;
    private final ArrowColorButton.ColorChangeEventListener fgListener;
    private final JComboBox<EIsoLanguage> langComboBox = new JComboBox<>();
    private final JComboBox<AdsReportForm.Mode> modeComboBox = new JComboBox<>();
    private final AdsReportFormUndoRedo undoRedo;
    private final AdsReportFormUndoAction undoAction;
    private final AdsReportFormRedoAction redoAction;
    private JButton hLayoutBtn;
    private JButton vLayoutBtn;
    private JButton gridLayoutBtn;
    private JSplitPane jSplitPane = new JSplitPane();

    public interface OnModeChangeListener {

        void onModeChanged(AdsReportForm.Mode newMode);
    }
    private final List<OnModeChangeListener> modeChangeListeners = new LinkedList<>();

    public void addModeChangeListener(OnModeChangeListener listener) {
        synchronized (modeChangeListeners) {
            if (!modeChangeListeners.contains(listener)) {
                modeChangeListeners.add(listener);
            }
        }
    }

    public void fireModeChange(AdsReportForm.Mode newMode) {
        final List<OnModeChangeListener> list;
        synchronized (modeChangeListeners) {
            list = new ArrayList<>(modeChangeListeners);
        }
        for (OnModeChangeListener l : list) {
            l.onModeChanged(newMode);
        }
        AdsReportPaletteRootNode.fireModeChange(newMode);
    }

    private final AdsReportClassDef report;
    private final ActionListener langComboBoxActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            final EIsoLanguage language = (EIsoLanguage) langComboBox.getSelectedItem();
            diagram.setLanguage(language);
            AdsReportFormDiagramOptions.getDefault().setDefaultLanguage(language);
            diagram.update();
            diagram.repaint();
        }
    };
    private final ActionListener modeComboBoxActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            final AdsReportForm.Mode mode = (AdsReportForm.Mode) modeComboBox.getSelectedItem();
            diagram.setMode(mode);
            fireModeChange(mode);
            //AdsReportFormDiagramOptions.getDefault().setDefaultLanguage(language);
            diagram.update();
            diagram.repaint();
        }
    };

    private final IRadixEventListener<RadixEvent> selectionListener = new IRadixEventListener<RadixEvent>() {

        @Override
        public void onEvent(RadixEvent e) {
            IReportCellContainer widgetContainer = null;
            List<AdsReportAbstractSelectableWidget> selectedWidgets = diagram.getSelectedWidgets();
            if (selectedWidgets.size() == 1){
                AdsReportAbstractSelectableWidget selectedWidget = selectedWidgets.get(0);
                if (selectedWidget instanceof AdsReportBandWidget || selectedWidget instanceof AdsReportBaseContainer){
                    widgetContainer = (IReportCellContainer) selectedWidget;
                }
            }
            updateLayoutBtns(widgetContainer);
        }

    };

    private void updateLayoutBtns(IReportCellContainer widgetContainer) {
        if (widgetContainer != null && widgetContainer.getReportWidgetContainer() != null) {
            EReportLayout layout = widgetContainer.getReportWidgetContainer().getLayout();
            switch (layout) {
                case GRID:
                    gridLayoutBtn.setSelected(true);
                    hLayoutBtn.setSelected(false);
                    vLayoutBtn.setSelected(false);
                    break;
                case HORIZONTAL:
                    hLayoutBtn.setSelected(true);
                    gridLayoutBtn.setSelected(false);
                    vLayoutBtn.setSelected(false);
                    break;
                case VERTICAL:
                    vLayoutBtn.setSelected(true);
                    gridLayoutBtn.setSelected(false);
                    hLayoutBtn.setSelected(false);
                    break;
                case FREE:
                    vLayoutBtn.setSelected(false);
                    gridLayoutBtn.setSelected(false);
                    hLayoutBtn.setSelected(false);
                    break;
            }
            setLayoutBtnsEnabled(true);
        } else {
            setLayoutBtnsEnabled(false);
        }
    }

    private void setLayoutBtnsEnabled(boolean enabled) {
        gridLayoutBtn.setEnabled(enabled);
        hLayoutBtn.setEnabled(enabled);
        vLayoutBtn.setEnabled(enabled);
    }
    /**
     * Creates new form AdsReportClassDefEditor
     */
    public DesignPanel(AdsReportClassDef report) {
        this.report = report;
        final AdsReportForm form = report.getForm();

        undoRedo = new AdsReportFormUndoRedo(form);
        undoAction = new AdsReportFormUndoAction(undoRedo);
        redoAction = new AdsReportFormRedoAction(undoRedo);
        diagram = new AdsReportFormDiagram(form, undoRedo);
        diagram.addSelectionListener(selectionListener);
//        bgListener = new ArrowColorButton.ColorChangeEventListener() {
//
//            @Override
//            public void onEvent(ColorChangeEvent e) {
//                for (RadixObject obj : diagram.getSelectedObjects()) {
//                    if (obj instanceof AdsReportCell) {
//                        AdsReportCell cell = (AdsReportCell) obj;
//                        cell.setBgColorInherited(false);
//                        cell.setBgColor(e.getColor());
//                    }
//                }
//            }
//        };
        fgListener = new ArrowColorButton.ColorChangeEventListener() {

            @Override
            public void onEvent(ColorChangeEvent e) {
                for (RadixObject obj : diagram.getSelectedObjects()) {
                    if (obj instanceof AdsReportCell) {
                        AdsReportCell cell = (AdsReportCell) obj;
                        cell.setFgColorInherited(false);
                        cell.setFgColor(e.getColor());
                    }
                }
            }
        };

        setLayout(new MultiBorderLayout());
        jSplitPane.setDividerLocation(150);
        final ReportNavigator navigator = new ReportNavigator();
        AdsReportTreeAction reportTreeAction = new AdsReportTreeAction(diagram, jSplitPane, navigator);
        //JPanel toolBarPanel=new JPanel();

        //toolBarPanel.setLayout(/*new FlowLayout()*/new MultiBorderLayout(0,0)/*new WrappedLayout(WrappedLayout.LEFT, 1, 1)*/);
        add(getTopToolBar(form, reportTreeAction), BorderLayout.NORTH);
        add(getEditToolBar(), BorderLayout.NORTH);
        //add(toolBarPanel,BorderLayout.NORTH);
//        add(getLeftToolBar(), BorderLayout.WEST);
        
        final JScrollPane scroll = new JScrollPane(diagram);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        jSplitPane.setRightComponent(scroll);
        add(jSplitPane, BorderLayout.CENTER);

        InputMap inputMap = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "undo");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "redo");

        ActionMap actionMap = this.getActionMap();
        actionMap.put("undo", undoAction);
        actionMap.put("redo", redoAction);

        diagram.setFocusable(true);
        AdsReportPaletteRootNode.fireModeChange(form.getMode());
    }

    private JToolBar getEditToolBar() {
        final JToolBar toolBar = new javax.swing.JToolBar();
        toolBar.setFloatable(true);
        toolBar.setRollover(true);
        toolBar.add(getVisibilityButton());
        toolBar.add(getAlignButton());
        toolBar.add(getHAlignButton());
        toolBar.add(getSnapEdgesButton());
        toolBar.add(getAdjustButton());
        toolBar.addSeparator();
        toolBar.add(getButtonForAction(new LineSpaicingAction(diagram), false, "Line Spacing"));
        toolBar.add(getWrapWordButton());
        toolBar.add(getClipContentButton());
        toolBar.add(getTextFormatButton());
        toolBar.add(getButtonForAction(new AdsReportMarginAction(diagram), false, "Margins"));
        toolBar.addSeparator();
//        toolBar.add(getVAlignButton());
        toolBar.add(getButtonForAction(new FontAction(diagram), false, "Font"));
        toolBar.add(getFgColorButton());
        toolBar.add(getButtonForAction(new BackgroundAction(diagram), true, "Background Color"));
        toolBar.addSeparator();
        toolBar.add(getButtonForAction(new BorderAction(diagram), false, "Borders"));
        return toolBar;
    }

    private JToolBar getTopToolBar(AdsReportForm form, final AdsReportTreeAction reportTreeAction) {
        final JToolBar toolBar = new javax.swing.JToolBar();
        toolBar.setFloatable(true);
        toolBar.setRollover(true);

        final JToggleButton button = new JToggleButton(reportTreeAction);
        button.setSelected(true);
        reportTreeAction.actionPerformed(new ActionEvent(button, 0, ""));
        toolBar.add(button);
        toolBar.addSeparator();
        
        toolBar.add(getButtonForAction(new AdsReportFormEditAction(form), true, ""));
        toolBar.add(getButtonForAction(new AdsReportBandsEditAction(form), true, ""));
        toolBar.add(getButtonForAction(new AdsReportGroupsEditAction(form), true, ""));
        toolBar.add(getButtonForAction(new GridSettingsEditAction(form), true, ""));
        toolBar.addSeparator();
        toolBar.add(new ScaleComboBox(this));
        toolBar.addSeparator();

        langComboBox.setToolTipText("Language");
        langComboBox.setMaximumSize(new Dimension(100, 24));
        toolBar.add(langComboBox);

        modeComboBox.setToolTipText("Mode");
        modeComboBox.setMaximumSize(new Dimension(100, 24));
        toolBar.add(modeComboBox);
//        modeComboBox.setVisible(false);

        toolBar.addSeparator();
        toolBar.add(getButtonForAction(undoAction, true, ""));
        toolBar.add(getButtonForAction(redoAction, true, ""));

        /* toolBar.addSeparator();
         toolBar.add(getAlignButton());
         toolBar.add(getHAlignButton());
         toolBar.add(getSnapEdgesButton());
         toolBar.add(getAdjustButton());
         toolBar.add(getWrapWordButton());
         toolBar.add(getClipContentButton());
         //        toolBar.add(getVAlignButton());
         toolBar.add(getBgColorButton());
         toolBar.add(getFgColorButton());
         toolBar.add(getBrColorButton());
         toolBar.add(getBorderButton());*/
        AdsReportHLayoutAction hLayoutAction = new AdsReportHLayoutAction(diagram);
        AdsReportVLayoutAction vLayoutAction = new AdsReportVLayoutAction(diagram);
        AdsReportGridLayoutAction gridLayoutAction = new AdsReportGridLayoutAction(diagram);
        hLayoutBtn = getButtonForAction(hLayoutAction, false, "");
        vLayoutBtn = getButtonForAction(vLayoutAction, false, "");
        gridLayoutBtn = getButtonForAction(gridLayoutAction, false, "");

        toolBar.addSeparator();
        toolBar.add(hLayoutBtn);
        toolBar.add(vLayoutBtn);
        toolBar.add(gridLayoutBtn);
        return toolBar;
    }

    private JButton getButtonForAction(final Action action, final boolean isEnabledInTextMode, String tooltip) {
        final JButton button = new JButton(action);
        button.setText("");
        button.setBorderPainted(false);
        button.setMargin(new Insets(2, 2, 2, 2));
        button.setFocusable(false);
        button.setToolTipText(tooltip);
        addModeChangeListener(new OnModeChangeListener() {

            @Override
            public void onModeChanged(AdsReportForm.Mode newMode) {
                button.setEnabled(action.isEnabled() && (isEnabledInTextMode || newMode != AdsReportForm.Mode.TEXT));
            }
        });
        return button;
    }

    /* private JToolBar getLeftToolBar() {
     final JToolBar toolBar = new javax.swing.JToolBar();
     toolBar.setOrientation(JToolBar.VERTICAL);
     toolBar.setFloatable(false);
     toolBar.setRollover(true);

     toolBar.add(getAlignButton());
     toolBar.add(getHAlignButton());
     toolBar.add(getVAlignButton());
     toolBar.add(getBgColorButton());
     toolBar.add(getFgColorButton());
     toolBar.add(getBrColorButton());
     toolBar.add(getBorderButton());

     return toolBar;
     }*/
    private JButton getAlignButton() {
        DropDownButton button = new DropDownButton();
        button.addAction(new AlignLeftAction(diagram));
        button.addAction(new AlignRightAction(diagram));
        button.addAction(new AlignTopAction(diagram));
        button.addAction(new AlignBottomAction(diagram));
        button.addSeparator();
        button.addAction(new AlignByWidthAction(diagram));
        button.addAction(new AlignByHeightAction(diagram));
        button.addSeparator();
        button.addAction(new AlignCenterAction(diagram));
        button.addAction(new AlignMiddleAction(diagram));
        button.addSeparator();
        button.addAction(new ArrangeByHorizontalAction(diagram));
        button.addAction(new ArrangeByVerticalAction(diagram));
        button.getButton().setToolTipText("Align Cells");
        return button.getButton();
    }

    private JButton getHAlignButton() {
        DropDownButton button = new DropDownButton();
        button.addAction(new HAlignLeftAction(diagram));
        button.addAction(new HAlignCenterAction(diagram));
        button.addAction(new HAlignRightAction(diagram));
        button.addAction(new HAlignJustifyAction(diagram));
        button.getButton().setToolTipText("Align Text Horizontally");
        return button.getButton();
    }

    private JButton getSnapEdgesButton() {
        DropDownButton button = new DropDownButton();
        /*JCheckBoxMenuItem spanTopEdgeItem=new JCheckBoxMenuItem();
         SpanTopEdgeAction spanTopEdgeAction=new SpanTopEdgeAction(diagram,spanTopEdgeItem);
         spanTopEdgeItem.setAction(spanTopEdgeAction);
         button.addMenuItem(spanTopEdgeItem);
        
         JCheckBoxMenuItem spanBottomEdgeItem=new JCheckBoxMenuItem();
         SnapBottomEdgeAction spanBottomEdgeAction=new SnapBottomEdgeAction(diagram,spanBottomEdgeItem);
         spanBottomEdgeItem.setAction(spanBottomEdgeAction);
         button.addMenuItem(spanBottomEdgeItem);*/

        button.addAction(new SpanTopEdgeAction(diagram));
        button.addAction(new SnapBottomEdgeAction(diagram));
        button.addAction(new NoSnapEdgesAction(diagram));
        button.getButton().setToolTipText("Snap Edges");
        return button.getButton();
    }

    private JButton getAdjustButton() {
        DropDownButton button = new DropDownButton();

        /*JCheckBoxMenuItem adjustHeightItem=new JCheckBoxMenuItem();
         AdjustHeightAction adjustHeightAct=new AdjustHeightAction(diagram,adjustHeightItem);
         adjustHeightItem.setAction(adjustHeightAct);
         button.addMenuItem(adjustHeightItem);
        
         JCheckBoxMenuItem adjustWidthItem=new JCheckBoxMenuItem();
         AdjustWidthAction adjustWidthAct=new AdjustWidthAction(diagram,adjustWidthItem);
         adjustWidthItem.setAction(adjustWidthAct);
         button.addMenuItem(adjustWidthItem);*/
        button.addAction(new AdjustHeightAction(diagram));
        button.addAction(new AdjustWidthAction(diagram));
        button.addAction(new NoAdjustAction(diagram));
        button.getButton().setToolTipText("Adjust Height/Width");
        return button.getButton();
    }

    private JButton getWrapWordButton() {
        DropDownButton button = new DropDownButton();
        button.addAction(new WrapWordAction(diagram));
        button.addAction(new NoWrapWordAction(diagram));
        button.getButton().setToolTipText("Wrap Word");
        return button.getButton();
    }

    private JButton getClipContentButton() {
        DropDownButton button = new DropDownButton();
        button.addAction(new ClipContentAction(diagram));
        button.addAction(new NoClipContentAction(diagram));
        button.getButton().setToolTipText("Clip Content");
        return button.getButton();
    }
    
    private JButton getTextFormatButton() {
        DropDownButton button = new DropDownButton();
        for (EReportTextFormat format : EReportTextFormat.values()){
            button.addAction(new TextFormatAction(diagram, format));
        }
        button.getButton().setToolTipText("Text format");
        return button.getButton();
    }
    
    private JButton getVisibilityButton() {
        DropDownButton button = new DropDownButton();
        button.setIcon(RadixWareIcons.MLSTRING_EDITOR.SHOW_EMPTY_STRINGS.getIcon());
        button.addAction(new AdsReportVisibilityAction(diagram, null));
        for (AdsReportForm.Mode mode : AdsReportForm.Mode.values()){
            button.addAction(new AdsReportVisibilityAction(diagram, mode));
        }
        button.getButton().setToolTipText("Visibility");
        return button.getButton();
    }

    /* private JButton getVAlignButton() {
     DropDownButton button = new DropDownButton();
     button.addAction(new VAlignTopAction(diagram));
     button.addAction(new VAlignCenterAction(diagram));
     button.addAction(new VAlignBottomAction(diagram));
     button.addAction(new VAlignAdjustAction(diagram));
     button.getButton().setToolTipText("Vertical Text Align");
     return button.getButton();
     }*/
    private JButton getFgColorButton() {
        final ArrowColorButton button = new ArrowColorButton(
                (ImageIcon) RadixWareIcons.EDIT.FOREGROUND.getIcon(), Color.BLACK);
        button.addColorChangeEventListener(fgListener);
        button.setToolTipText("Text Color");
        addModeChangeListener(new OnModeChangeListener() {

            @Override
            public void onModeChanged(AdsReportForm.Mode newMode) {
                button.setEnabled(newMode != AdsReportForm.Mode.TEXT);
            }
        });
        return button;
    }

    
    public void open(OpenInfo openInfo) {
        diagram.open(openInfo);
    }

    public void update() {
        langComboBox.removeActionListener(langComboBoxActionListener);
        final List<EIsoLanguage> supportedLanguages = report.getModule().getSegment().getLayer().getLanguages();
        EIsoLanguage arrLangs[] = new EIsoLanguage[supportedLanguages.size()];
        arrLangs = supportedLanguages.toArray(arrLangs);
        langComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(arrLangs));
        if (!supportedLanguages.contains(diagram.getLanguage())) {
            if (supportedLanguages.isEmpty()) {
                diagram.setLanguage(EIsoLanguage.ENGLISH);
            } else {
                diagram.setLanguage(supportedLanguages.iterator().next());
            }
            AdsReportFormDiagramOptions.getDefault().setDefaultLanguage(diagram.getLanguage());
        }
        langComboBox.setSelectedItem(diagram.getLanguage());
        langComboBox.addActionListener(langComboBoxActionListener);

        modeComboBox.removeActionListener(modeComboBoxActionListener);
        DefaultComboBoxModel<AdsReportForm.Mode> comboBoxModel = new DefaultComboBoxModel();
        comboBoxModel.addElement(AdsReportForm.Mode.GRAPHICS);
        final AdsReportForm form = report.getForm();
        if (form != null) {
            if (form.isSupportsTxt()) {
                comboBoxModel.addElement(AdsReportForm.Mode.TEXT);
            }
             if (diagram.getMode() != form.getMode()) {
                diagram.setMode(form.getMode());
                fireModeChange(form.getMode());
            }
        }
        modeComboBox.setModel(comboBoxModel);
        modeComboBox.setSelectedItem(diagram.getMode());
        modeComboBox.addActionListener(modeComboBoxActionListener);
        
        diagram.update();
    }

    public List<RadixObject> getSelectedObjects() {
        return diagram.getSelectedObjects();
    }

    public void removeSelectionListener(IRadixEventListener<RadixEvent> listener) {
        diagram.removeSelectionListener(listener);
    }

    public void addSelectionListener(IRadixEventListener<RadixEvent> listener) {
        diagram.addSelectionListener(listener);
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        diagram.requestFocus();
    }
}

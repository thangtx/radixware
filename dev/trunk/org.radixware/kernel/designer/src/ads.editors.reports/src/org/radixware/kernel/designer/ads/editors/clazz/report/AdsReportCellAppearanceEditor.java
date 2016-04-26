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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EImageScaleType;
import org.radixware.kernel.common.enums.EReportBorderStyle;
import org.radixware.kernel.common.enums.EReportCellHAlign;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportCellVAlign;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportWidgetUtils;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.MmUtils;
import org.radixware.kernel.designer.common.dialogs.components.BigDecimalSpinnerModel;
import org.radixware.kernel.designer.common.dialogs.components.CheckedBigDecimalSpinnerEditor;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;

class AdsReportCellAppearanceEditor extends JPanel implements ChangeListener {

    @Override
    public void stateChanged(final ChangeEvent e) {
        updateFont();
    }

    /*private class FontComboBoxModel extends DefaultComboBoxModel {

     private final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

     @Override
     public int getSize() {
     return fonts.length;
     }

     @Override
     public Object getElementAt(int index) {
     return fonts[index];
     }
     }*/
    private volatile boolean updating = false;
    private EReportCellHAlign hAlign;
    private EReportCellVAlign vAlign;
    private final AdsReportCell appearance;
    private EImageScaleType scaleType = EImageScaleType.CROP;
    private boolean ignoreZebra;

    private final AdsReportFontPanel innerFontPanel;

    /**
     * Creates new form AdsReportCellAppearanceEditor
     */
    @SuppressWarnings("deprecation")
    protected AdsReportCellAppearanceEditor(final AdsReportCell appearance) {
        super();
        this.appearance = appearance;
        initComponents();

        jLabel9.setVisible(false);
        borderSpinner.setVisible(false);

        northWestToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.LEFT_TOP.getIcon());
        northToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.CENTER_TOP.getIcon());
        northEastToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.RIGHT_TOP.getIcon());
        northAdjustToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.JUSTIFY_TOP.getIcon());
        westToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.LEFT_MIDDLE.getIcon());
        centerToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.CENTER_MIDDLE.getIcon());
        eastToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.RIGHT_MIDDLE.getIcon());
        centerAdjustToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.JUSTIFY_CENTER.getIcon());
        southWestToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.LEFT_BOTTOM.getIcon());
        southToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.CENTER_BOTTOM.getIcon());
        southEastToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.RIGHT_BOTTOM.getIcon());
        southAdjustToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.JUSTIFY_BOTTOM.getIcon());

        imgNorthWestToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.LEFT_TOP.getIcon());
        imgNorthToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.CENTER_TOP.getIcon());
        imgNorthEastToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.RIGHT_TOP.getIcon());
        imgWestToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.LEFT_MIDDLE.getIcon());
        imgCenterToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.CENTER_MIDDLE.getIcon());
        imgEastToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.RIGHT_MIDDLE.getIcon());
        imgSouthWestToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.LEFT_BOTTOM.getIcon());
        imgSouthToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.CENTER_BOTTOM.getIcon());
        imgSouthEastToggleButton.setIcon(RadixWareIcons.ALIGN.TEXT.RIGHT_BOTTOM.getIcon());

        iconButton.setIcon(RadixWareIcons.DIALOG.ALL.getIcon());
        iconButton.setText("");

        ComponentTitledBorder border = new ComponentTitledBorder(useBandFontCheckBox, fontPanel, new TitledBorder(""));
        fontPanel.setBorder(border);

        if (appearance.getOwnerForm().getMode() == AdsReportForm.Mode.GRAPHICS) {

            topSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            topSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(topSpinner));
            leftSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            leftSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(leftSpinner));
            widthSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            widthSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(widthSpinner));
            heightSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            heightSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(heightSpinner));

            topMarginSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            topMarginSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(topMarginSpinner));
            leftMarginSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            leftMarginSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(leftMarginSpinner));
            bottomMarginSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            bottomMarginSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(bottomMarginSpinner));
            rightMarginSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            rightMarginSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(rightMarginSpinner));

        } else {
            lblTop.setText(lblTop.getText().replace("mm.", "row"));
            lblHeight.setText(lblHeight.getText().replace("mm.", "rows"));
            lblLeft.setText(lblLeft.getText().replace("mm.", "col"));
            lblWidth.setText(lblWidth.getText().replace("mm.", "cols"));

            lbTopMargin.setText(lbTopMargin.getText().replace("mm.", "rows"));
            lbBottomMargin.setText(lbBottomMargin.getText().replace("mm.", "rows"));
            lbLeftMargin.setText(lbLeftMargin.getText().replace("mm.", "cols"));
            lbRightMargin.setText(lbRightMargin.getText().replace("mm.", "cols"));

            topSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            topSpinner.setEditor(new CheckedNumberSpinnerEditor(topSpinner));
            leftSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            leftSpinner.setEditor(new CheckedNumberSpinnerEditor(leftSpinner));
            widthSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            widthSpinner.setEditor(new CheckedNumberSpinnerEditor(widthSpinner));
            heightSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            heightSpinner.setEditor(new CheckedNumberSpinnerEditor(heightSpinner));

            topMarginSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            topMarginSpinner.setEditor(new CheckedNumberSpinnerEditor(topMarginSpinner));
            leftMarginSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            leftMarginSpinner.setEditor(new CheckedNumberSpinnerEditor(leftMarginSpinner));
            bottomMarginSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            bottomMarginSpinner.setEditor(new CheckedNumberSpinnerEditor(bottomMarginSpinner));
            rightMarginSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            rightMarginSpinner.setEditor(new CheckedNumberSpinnerEditor(rightMarginSpinner));
        }
        lineSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
        lineSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(lineSpinner));
        borderSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(50), BigDecimal.valueOf(0.1)));
        borderSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(borderSpinner));
        innerFontPanel = new AdsReportFontPanel(appearance.getFont(), "Font");
        innerFontPanel.changeSupport.addChangeListener(this);
        useBandFontCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                //if (useBandFontCheckBox.isSelected()) {
                //    AdsReportBand ownerBand = AdsReportCellAppearanceEditor.this.appearance.getOwnerBand();
                //    if (ownerBand != null) {

                //    }
                //}
                updateEnableState();
                apply();
                updateFont();
            }
        });
        fontPanel.setLayout(new BorderLayout());
        fontPanel.add(innerFontPanel, BorderLayout.NORTH);
        fontPanel.updateUI();

        snapTopBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (!updating) {
                    final boolean isSelected = snapTopBox.isSelected();
                    AdsReportCellAppearanceEditor.this.appearance.setSnapTopEdge(isSelected);
                }
            }
        });

        snapBottomBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (!updating) {
                    final boolean isSelected = snapBottomBox.isSelected();
                    AdsReportCellAppearanceEditor.this.appearance.setSnapBottomEdge(isSelected);
                }
            }
        });
        final ActionListener modeListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    if (cbShowInTextMode.isSelected()) {
                        appearance.setPreferredMode(AdsReportForm.Mode.TEXT);
                    } else if (cbShowInGraphicalMode.isSelected()) {
                        appearance.setPreferredMode(AdsReportForm.Mode.GRAPHICS);
                    } else {
                        appearance.setPreferredMode(null);
                    }
                }
            }
        };
        cbShowAlways.addActionListener(modeListener);
        cbShowInTextMode.addActionListener(modeListener);
        cbShowInGraphicalMode.addActionListener(modeListener);

        setupInitialValues();
    }

    private void setupInitialValues() {
        updating = true;
        if (appearance.getOwnerForm().getMode() == AdsReportForm.Mode.GRAPHICS) {
            topSpinner.setValue(BigDecimal.valueOf(appearance.getTopMm()));
            leftSpinner.setValue(BigDecimal.valueOf(appearance.getLeftMm()));
            widthSpinner.setValue(BigDecimal.valueOf(appearance.getWidthMm()));
            heightSpinner.setValue(BigDecimal.valueOf(appearance.getHeightMm()));
            topMarginSpinner.setValue(BigDecimal.valueOf(appearance.getMarginTopMm()));
            bottomMarginSpinner.setValue(BigDecimal.valueOf(appearance.getMarginBottomMm()));
            leftMarginSpinner.setValue(BigDecimal.valueOf(appearance.getMarginLeftMm()));
            rightMarginSpinner.setValue(BigDecimal.valueOf(appearance.getMarginRightMm()));

        } else {
            topSpinner.setValue(appearance.getTopRow());
            leftSpinner.setValue(appearance.getLeftColumn());
            widthSpinner.setValue(appearance.getWidthCols());
            heightSpinner.setValue(appearance.getHeightRows());
            topMarginSpinner.setValue(appearance.getMarginTopRows());
            bottomMarginSpinner.setValue(appearance.getMarginBottomRows());
            leftMarginSpinner.setValue(appearance.getMarginLeftCols());
            rightMarginSpinner.setValue(appearance.getMarginRightCols());
        }
        ignoreZebra = appearance.isIgnoreAltBgColor();
        if (appearance.getCellType() == EReportCellType.DB_IMAGE) {
            scaleType = ((AdsReportDbImageCell) appearance).getScaleType();
            ((CardLayout) jPanel5.getLayout()).show(jPanel5, "card2");
            switch (scaleType) {
                case CROP:
                    imgModeCropImageBtn.setSelected(true);
                    break;
                case FIT_TO_CONTAINER:
                    imgModeFitImageBtn.setSelected(true);
                    break;
                case SCALE_TO_CONTAINER:
                    imgModeScaleImageBtn.setSelected(true);
                    break;
                case RESIZE_CONTAINER:
                    imgModeResizeCellBtn.setSelected(true);
                    break;
            }
        } else {
            ((CardLayout) jPanel5.getLayout()).show(jPanel5, "card1");
        }
        inheritBgCheckBox.setSelected(appearance.isBgColorInherited());
        bgButton.setColor(appearance.getBgColor());
        samplePanel.setBackground(appearance.getBgColor());
        useBandFontCheckBox.setSelected(appearance.isFontInherited());
        innerFontPanel.setFont(appearance.getFont(), true);
        updateFont();
        lineSpinner.setValue(BigDecimal.valueOf(appearance.getLineSpacingMm()));
        wrapCheckBox.setSelected(appearance.isWrapWord());
        clipCheckBox.setSelected(appearance.isClipContent());
        inheritFgCheckBox.setSelected(appearance.isFgColorInherited());
        fgButton.setColor(appearance.getFgColor());
        sampleLabel.setForeground(appearance.getFgColor());
        hAlign = appearance.getHAlign();
        vAlign = adjustCheckBox.isSelected() ? EReportCellVAlign.TOP : appearance.getVAlign();
        adjustCheckBox.setSelected(appearance.isAdjustHeight() || vAlign == EReportCellVAlign.ADJUST);
        cbAdjustWidth.setSelected(appearance.isAdjustWidth());
        cbShowAlways.setSelected(appearance.getPreferredMode() == null);
        cbShowInGraphicalMode.setSelected(appearance.getPreferredMode() == AdsReportForm.Mode.GRAPHICS);
        cbShowInTextMode.setSelected(appearance.getPreferredMode() == AdsReportForm.Mode.TEXT);
        updateAlignment();
        switch (appearance.getBorder().getStyle()) {
            case SOLID:
                solidRadioButton.setSelected(true);
                break;
            case DASHED:
                dashedRadioButton.setSelected(true);
                break;
            case DOTTED:
                dottedRadioButton.setSelected(true);
                break;
        }
        topCheckBox.setSelected(appearance.getBorder().isOnTop());
        leftCheckBox.setSelected(appearance.getBorder().isOnLeft());
        rightCheckBox.setSelected(appearance.getBorder().isOnRight());
        bottomCheckBox.setSelected(appearance.getBorder().isOnBottom());
        colorButton.setColor(appearance.getBorder().getColor());
        borderSpinner.setValue(BigDecimal.valueOf(appearance.getBorder().getThicknessMm()));

        snapTopBox.setSelected(appearance.isSnapTopEdge());
        snapBottomBox.setSelected(appearance.isSnapBottomEdge());
        chIgnoreZebra.setSelected(ignoreZebra);
        updateEnableState();
        updateBorder();

        updating = false;
    }

    private void updateEnableState() {
        boolean isTextMode = appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS;
        boolean enabled = !appearance.isReadOnly();
        heightSpinner.setEnabled(enabled);
        topSpinner.setEnabled(enabled);
        leftSpinner.setEnabled(enabled);
        widthSpinner.setEnabled(enabled);
        inheritBgCheckBox.setEnabled(enabled);
        bgButton.setEnabled(enabled && !inheritBgCheckBox.isSelected() && !isTextMode);
        useBandFontCheckBox.setEnabled(enabled && !isTextMode);
        innerFontPanel.setPanelEnabled(enabled && !useBandFontCheckBox.isSelected());
        topMarginSpinner.setEnabled(enabled);
        leftMarginSpinner.setEnabled(enabled);
        bottomMarginSpinner.setEnabled(enabled);
        rightMarginSpinner.setEnabled(enabled);
        lineSpinner.setEnabled(enabled && !isTextMode);
        wrapCheckBox.setEnabled(enabled);
        clipCheckBox.setEnabled(enabled);
        inheritFgCheckBox.setEnabled(enabled && !isTextMode);
        fgButton.setEnabled(enabled && !inheritFgCheckBox.isSelected() && !isTextMode);
        final boolean isChart = appearance.getCellType() == EReportCellType.CHART;
        adjustCheckBox.setEnabled(enabled && !isChart);
        cbAdjustWidth.setEnabled(enabled && !isChart);
        northEastToggleButton.setEnabled(enabled && !isChart);
        northToggleButton.setEnabled(enabled && !isChart);
        northWestToggleButton.setEnabled(enabled && !isChart);
        northAdjustToggleButton.setEnabled(enabled && !isChart);
        westToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ !isChart);
        centerToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ !isChart);
        eastToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ !isChart);
        centerAdjustToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ !isChart);
        southEastToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ !isChart);
        southToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ !isChart);
        southWestToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ !isChart);
        southAdjustToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ !isChart);

        topCheckBox.setEnabled(enabled);
        leftCheckBox.setEnabled(enabled);
        rightCheckBox.setEnabled(enabled);
        bottomCheckBox.setEnabled(enabled);
        final boolean enableBorder = enabled && !isTextMode && (topCheckBox.isSelected() || leftCheckBox.isSelected()
                || bottomCheckBox.isSelected() || rightCheckBox.isSelected());
        solidRadioButton.setEnabled(enableBorder);
        dashedRadioButton.setEnabled(enableBorder);
        dottedRadioButton.setEnabled(enableBorder);
        colorButton.setEnabled(enableBorder);
        borderSpinner.setEnabled(enabled);

        snapTopBox.setEnabled(enabled);
        snapBottomBox.setEnabled(enabled);
        cbShowAlways.setEnabled(enabled);
        cbShowInTextMode.setEnabled(enabled);
        cbShowInGraphicalMode.setEnabled(enabled);

        boolean isImageAlignEnabled = scaleType == EImageScaleType.FIT_TO_CONTAINER || scaleType == EImageScaleType.CROP || scaleType == EImageScaleType.RESIZE_CONTAINER && enabled;
        imgCenterToggleButton.setEnabled(isImageAlignEnabled);
        imgEastToggleButton.setEnabled(isImageAlignEnabled);
        imgNorthEastToggleButton.setEnabled(isImageAlignEnabled);
        imgNorthWestToggleButton.setEnabled(isImageAlignEnabled);
        imgSouthEastToggleButton.setEnabled(isImageAlignEnabled);
        imgSouthToggleButton.setEnabled(isImageAlignEnabled);
        imgSouthWestToggleButton.setEnabled(isImageAlignEnabled);
        imgWestToggleButton.setEnabled(isImageAlignEnabled);
        imgNorthToggleButton.setEnabled(isImageAlignEnabled);
        chIgnoreZebra.setEnabled(enabled && !isTextMode);
    }

    private void updateAlignment() {
        if (vAlign == EReportCellVAlign.TOP) {
            if (hAlign == EReportCellHAlign.LEFT) {
                northWestToggleButton.setSelected(true);
                imgNorthWestToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            } else if (hAlign == EReportCellHAlign.CENTER) {
                northToggleButton.setSelected(true);
                imgNorthToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            } else if (hAlign == EReportCellHAlign.RIGHT) {
                northEastToggleButton.setSelected(true);
                imgNorthEastToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            } else if (hAlign == EReportCellHAlign.JUSTIFY) {
                northAdjustToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            }
            sampleLabel.setVerticalAlignment(SwingConstants.TOP);
        } else if (vAlign == EReportCellVAlign.MIDDLE) {
            if (hAlign == EReportCellHAlign.LEFT) {
                westToggleButton.setSelected(true);
                imgWestToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            } else if (hAlign == EReportCellHAlign.CENTER) {
                centerToggleButton.setSelected(true);
                imgCenterToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            } else if (hAlign == EReportCellHAlign.RIGHT) {
                eastToggleButton.setSelected(true);
                imgEastToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            } else if (hAlign == EReportCellHAlign.JUSTIFY) {
                centerAdjustToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            }
            sampleLabel.setVerticalAlignment(SwingConstants.CENTER);
        } else if (vAlign == EReportCellVAlign.BOTTOM) {
            if (hAlign == EReportCellHAlign.LEFT) {
                southWestToggleButton.setSelected(true);
                imgSouthWestToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            } else if (hAlign == EReportCellHAlign.CENTER) {
                southToggleButton.setSelected(true);
                imgSouthToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            } else if (hAlign == EReportCellHAlign.RIGHT) {
                southEastToggleButton.setSelected(true);
                imgSouthEastToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            } else if (hAlign == EReportCellHAlign.JUSTIFY) {
                southAdjustToggleButton.setSelected(true);
                sampleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            }
            sampleLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        }
    }

    private void updateFont() {
        final AdsReportAbstractAppearance.Font reportFont = appearance.getFont();
        final Font font = AdsReportWidgetUtils.reportFont2JavaFont(reportFont, sampleLabel);
        sampleLabel.setFont(font);
//        String name = fontComboBox.getSelectedItem() != null ? fontComboBox.getSelectedItem().toString() : "Arial";
//        sampleLabel.setFont(new Font(name,
//                (boldButton.isSelected() ? Font.BOLD : 0) |
//                (italicButton.isSelected() ? Font.ITALIC : 0),
//                (int) ((BigDecimal) fontSizeSpinner.getValue()).doubleValue()));
    }

    private void updateBorder() {
        final double ww = ((BigDecimal) borderSpinner.getValue()).doubleValue();
        int w = MmUtils.mm2px(ww);
        if (w == 0 && ww > 0) {
            w = 1;
        }
        samplePanel.setBorder(new MatteBorder(
                topCheckBox.isSelected() ? w : 0,
                leftCheckBox.isSelected() ? w : 0,
                bottomCheckBox.isSelected() ? w : 0,
                rightCheckBox.isSelected() ? w : 0,
                colorButton.getColor()));
        final Insets ins = samplePanel.getInsets();
        if (dashedRadioButton.isSelected()) {
            samplePanel.setBorder(new DashedBorder(
                    topCheckBox.isSelected() ? w : 0,
                    leftCheckBox.isSelected() ? w : 0,
                    bottomCheckBox.isSelected() ? w : 0,
                    rightCheckBox.isSelected() ? w : 0,
                    colorButton.getColor(),
                    Math.max(w * 2, 10), ins));
        } else if (dottedRadioButton.isSelected()) {
            samplePanel.setBorder(new DashedBorder(
                    topCheckBox.isSelected() ? w : 0,
                    leftCheckBox.isSelected() ? w : 0,
                    bottomCheckBox.isSelected() ? w : 0,
                    rightCheckBox.isSelected() ? w : 0,
                    colorButton.getColor(),
                    Math.max(w, 1), ins));
        }
        final boolean readonly = appearance.isReadOnly();
        final boolean enable = !readonly && (topCheckBox.isSelected() || leftCheckBox.isSelected()
                || bottomCheckBox.isSelected() || rightCheckBox.isSelected());
        boolean isTextMode = appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS;
        colorButton.setEnabled(enable && !isTextMode);
        solidRadioButton.setEnabled(enable && !isTextMode);
        dottedRadioButton.setEnabled(enable && !isTextMode);
        dashedRadioButton.setEnabled(enable && !isTextMode);

        topCheckBox.setEnabled(!readonly && !isTextMode);
        bottomCheckBox.setEnabled(!readonly && !isTextMode);
        leftCheckBox.setEnabled(!readonly && !isTextMode);
        rightCheckBox.setEnabled(!readonly && !isTextMode);
        iconButton.setEnabled(!readonly && !isTextMode);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        samplePanel = new javax.swing.JPanel();
        sampleLabel = new javax.swing.JLabel();
        inheritBgCheckBox = new javax.swing.JCheckBox();
        bgButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();
        jPanel10 = new javax.swing.JPanel();
        lblLeft = new javax.swing.JLabel();
        leftSpinner = new javax.swing.JSpinner();
        lblTop = new javax.swing.JLabel();
        topSpinner = new javax.swing.JSpinner();
        jPanel11 = new javax.swing.JPanel();
        lblHeight = new javax.swing.JLabel();
        heightSpinner = new javax.swing.JSpinner();
        lblWidth = new javax.swing.JLabel();
        widthSpinner = new javax.swing.JSpinner();
        chIgnoreZebra = new javax.swing.JCheckBox();
        fontPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lineSpinner = new javax.swing.JSpinner();
        wrapCheckBox = new javax.swing.JCheckBox();
        clipCheckBox = new javax.swing.JCheckBox();
        inheritFgCheckBox = new javax.swing.JCheckBox();
        fgButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        adjustCheckBox = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        northWestToggleButton = new javax.swing.JToggleButton();
        centerToggleButton = new javax.swing.JToggleButton();
        northToggleButton = new javax.swing.JToggleButton();
        southToggleButton = new javax.swing.JToggleButton();
        northEastToggleButton = new javax.swing.JToggleButton();
        southWestToggleButton = new javax.swing.JToggleButton();
        eastToggleButton = new javax.swing.JToggleButton();
        westToggleButton = new javax.swing.JToggleButton();
        southEastToggleButton = new javax.swing.JToggleButton();
        northAdjustToggleButton = new javax.swing.JToggleButton();
        centerAdjustToggleButton = new javax.swing.JToggleButton();
        southAdjustToggleButton = new javax.swing.JToggleButton();
        snapTopBox = new javax.swing.JCheckBox();
        snapBottomBox = new javax.swing.JCheckBox();
        cbAdjustWidth = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        imgModeFitImageBtn = new javax.swing.JToggleButton();
        imgModeScaleImageBtn = new javax.swing.JToggleButton();
        imgModeResizeCellBtn = new javax.swing.JToggleButton();
        imgModeCropImageBtn = new javax.swing.JToggleButton();
        jPanel7 = new javax.swing.JPanel();
        imgNorthWestToggleButton = new javax.swing.JToggleButton();
        imgCenterToggleButton = new javax.swing.JToggleButton();
        imgNorthToggleButton = new javax.swing.JToggleButton();
        imgSouthToggleButton = new javax.swing.JToggleButton();
        imgNorthEastToggleButton = new javax.swing.JToggleButton();
        imgSouthWestToggleButton = new javax.swing.JToggleButton();
        imgEastToggleButton = new javax.swing.JToggleButton();
        imgWestToggleButton = new javax.swing.JToggleButton();
        imgSouthEastToggleButton = new javax.swing.JToggleButton();
        borderPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        leftCheckBox = new javax.swing.JCheckBox();
        iconButton = new javax.swing.JButton();
        rightCheckBox = new javax.swing.JCheckBox();
        topCheckBox = new javax.swing.JCheckBox();
        bottomCheckBox = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        borderSpinner = new javax.swing.JSpinner();
        colorButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();
        solidRadioButton = new javax.swing.JRadioButton();
        dashedRadioButton = new javax.swing.JRadioButton();
        dottedRadioButton = new javax.swing.JRadioButton();
        textMarginsPanel = new javax.swing.JPanel();
        middlePanel1 = new javax.swing.JPanel();
        topMarginSpinner = new javax.swing.JSpinner();
        lbTopMargin = new javax.swing.JLabel();
        lbBottomMargin = new javax.swing.JLabel();
        leftMarginSpinner = new javax.swing.JSpinner();
        lbLeftMargin = new javax.swing.JLabel();
        lbRightMargin = new javax.swing.JLabel();
        rightMarginSpinner = new javax.swing.JSpinner();
        iconPanel1 = new javax.swing.JPanel();
        bottomMarginSpinner = new javax.swing.JSpinner();
        pModeSupport = new javax.swing.JPanel();
        cbShowAlways = new javax.swing.JRadioButton();
        cbShowInTextMode = new javax.swing.JRadioButton();
        cbShowInGraphicalMode = new javax.swing.JRadioButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        samplePanel.setBackground(new java.awt.Color(255, 255, 255));
        samplePanel.setPreferredSize(new java.awt.Dimension(224, 80));

        sampleLabel.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.sampleLabel.text")); // NOI18N

        javax.swing.GroupLayout samplePanelLayout = new javax.swing.GroupLayout(samplePanel);
        samplePanel.setLayout(samplePanelLayout);
        samplePanelLayout.setHorizontalGroup(
            samplePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sampleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        samplePanelLayout.setVerticalGroup(
            samplePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sampleLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        inheritBgCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.inheritBgCheckBox.text")); // NOI18N
        inheritBgCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                inheritBgCheckBoxItemStateChanged(evt);
            }
        });

        bgButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.bgButton.text")); // NOI18N
        bgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bgButtonActionPerformed(evt);
            }
        });

        lblLeft.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.lblLeft.text")); // NOI18N

        leftSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        leftSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                leftSpinnerStateChanged(evt);
            }
        });

        lblTop.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.lblTop.text")); // NOI18N

        topSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        topSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                topSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTop)
            .addComponent(topSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblLeft)
            .addComponent(leftSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(lblTop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(topSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblLeft)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leftSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblHeight.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.lblHeight.text")); // NOI18N

        heightSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        heightSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                heightSpinnerStateChanged(evt);
            }
        });

        lblWidth.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.lblWidth.text")); // NOI18N

        widthSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        widthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                widthSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHeight)
            .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblWidth)
            .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(lblHeight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblWidth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        chIgnoreZebra.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.chIgnoreZebra.text")); // NOI18N
        chIgnoreZebra.setToolTipText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.chIgnoreZebra.toolTipText")); // NOI18N
        chIgnoreZebra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chIgnoreZebraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(inheritBgCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bgButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chIgnoreZebra)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(samplePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(samplePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bgButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inheritBgCheckBox)
                            .addComponent(chIgnoreZebra))))
                .addContainerGap())
        );

        fontPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.fontPanel.border.title"))); // NOI18N
        fontPanel.setName(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.fontPanel.name")); // NOI18N

        javax.swing.GroupLayout fontPanelLayout = new javax.swing.GroupLayout(fontPanel);
        fontPanel.setLayout(fontPanelLayout);
        fontPanelLayout.setHorizontalGroup(
            fontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        fontPanelLayout.setVerticalGroup(
            fontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.jPanel4.border.title"))); // NOI18N

        jLabel8.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.jLabel8.text")); // NOI18N

        lineSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                lineSpinnerStateChanged(evt);
            }
        });

        wrapCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.wrapCheckBox.text")); // NOI18N
        wrapCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wrapCheckBoxActionPerformed(evt);
            }
        });

        clipCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.clipCheckBox.text")); // NOI18N
        clipCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clipCheckBoxActionPerformed(evt);
            }
        });

        inheritFgCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.inheritFgCheckBox.text")); // NOI18N
        inheritFgCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                inheritFgCheckBoxItemStateChanged(evt);
            }
        });

        fgButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.fgButton.text")); // NOI18N
        fgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fgButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(inheritFgCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fgButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lineSpinner))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(wrapCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(clipCheckBox)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lineSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wrapCheckBox)
                    .addComponent(clipCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inheritFgCheckBox)
                    .addComponent(fgButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.jPanel5.border.title"))); // NOI18N
        jPanel5.setLayout(new java.awt.CardLayout());

        adjustCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.adjustCheckBox.text")); // NOI18N
        adjustCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                adjustCheckBoxItemStateChanged(evt);
            }
        });

        jPanel6.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(northWestToggleButton);
        northWestToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.northWestToggleButton.text")); // NOI18N
        northWestToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        northWestToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                northWestToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(northWestToggleButton, gridBagConstraints);

        buttonGroup1.add(centerToggleButton);
        centerToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.centerToggleButton.text")); // NOI18N
        centerToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        centerToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                centerToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(centerToggleButton, gridBagConstraints);

        buttonGroup1.add(northToggleButton);
        northToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.northToggleButton.text")); // NOI18N
        northToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        northToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                northToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(northToggleButton, gridBagConstraints);

        buttonGroup1.add(southToggleButton);
        southToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.southToggleButton.text")); // NOI18N
        southToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        southToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                southToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(southToggleButton, gridBagConstraints);

        buttonGroup1.add(northEastToggleButton);
        northEastToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.northEastToggleButton.text")); // NOI18N
        northEastToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        northEastToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                northEastToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(northEastToggleButton, gridBagConstraints);

        buttonGroup1.add(southWestToggleButton);
        southWestToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.southWestToggleButton.text")); // NOI18N
        southWestToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        southWestToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                southWestToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(southWestToggleButton, gridBagConstraints);

        buttonGroup1.add(eastToggleButton);
        eastToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.eastToggleButton.text")); // NOI18N
        eastToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        eastToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eastToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(eastToggleButton, gridBagConstraints);

        buttonGroup1.add(westToggleButton);
        westToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.westToggleButton.text")); // NOI18N
        westToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        westToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                westToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(westToggleButton, gridBagConstraints);

        buttonGroup1.add(southEastToggleButton);
        southEastToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.southEastToggleButton.text")); // NOI18N
        southEastToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        southEastToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                southEastToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(southEastToggleButton, gridBagConstraints);

        buttonGroup1.add(northAdjustToggleButton);
        northAdjustToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        northAdjustToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                northAdjustToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(northAdjustToggleButton, gridBagConstraints);

        buttonGroup1.add(centerAdjustToggleButton);
        centerAdjustToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        centerAdjustToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                centerAdjustToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(centerAdjustToggleButton, gridBagConstraints);

        buttonGroup1.add(southAdjustToggleButton);
        southAdjustToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        southAdjustToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                southAdjustToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(southAdjustToggleButton, gridBagConstraints);

        snapTopBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor-SnapToTop")); // NOI18N

        snapBottomBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor-SnapToBottom")); // NOI18N

        cbAdjustWidth.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.cbAdjustWidth.text")); // NOI18N
        cbAdjustWidth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbAdjustWidthItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(adjustCheckBox)
                    .addComponent(cbAdjustWidth))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(snapBottomBox)
                    .addComponent(snapTopBox))
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adjustCheckBox)
                    .addComponent(snapTopBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbAdjustWidth)
                    .addComponent(snapBottomBox))
                .addContainerGap(189, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(56, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jPanel5.add(jPanel2, "card1");

        buttonGroup5.add(imgModeFitImageBtn);
        imgModeFitImageBtn.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgModeFitImageBtn.text")); // NOI18N
        imgModeFitImageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgModeFitImageBtnActionPerformed(evt);
            }
        });

        buttonGroup5.add(imgModeScaleImageBtn);
        imgModeScaleImageBtn.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgModeScaleImageBtn.text")); // NOI18N
        imgModeScaleImageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgModeScaleImageBtnActionPerformed(evt);
            }
        });

        buttonGroup5.add(imgModeResizeCellBtn);
        imgModeResizeCellBtn.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgModeResizeCellBtn.text")); // NOI18N
        imgModeResizeCellBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgModeResizeCellBtnActionPerformed(evt);
            }
        });

        buttonGroup5.add(imgModeCropImageBtn);
        imgModeCropImageBtn.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgModeCropImageBtn.text")); // NOI18N
        imgModeCropImageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgModeCropImageBtnActionPerformed(evt);
            }
        });

        jPanel7.setLayout(new java.awt.GridBagLayout());

        buttonGroup4.add(imgNorthWestToggleButton);
        imgNorthWestToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgNorthWestToggleButton.text")); // NOI18N
        imgNorthWestToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        imgNorthWestToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgNorthWestToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(imgNorthWestToggleButton, gridBagConstraints);

        buttonGroup4.add(imgCenterToggleButton);
        imgCenterToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgCenterToggleButton.text")); // NOI18N
        imgCenterToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        imgCenterToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgCenterToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(imgCenterToggleButton, gridBagConstraints);

        buttonGroup4.add(imgNorthToggleButton);
        imgNorthToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgNorthToggleButton.text")); // NOI18N
        imgNorthToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        imgNorthToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgNorthToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(imgNorthToggleButton, gridBagConstraints);

        buttonGroup4.add(imgSouthToggleButton);
        imgSouthToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgSouthToggleButton.text")); // NOI18N
        imgSouthToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        imgSouthToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgSouthToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(imgSouthToggleButton, gridBagConstraints);

        buttonGroup4.add(imgNorthEastToggleButton);
        imgNorthEastToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgNorthEastToggleButton.text")); // NOI18N
        imgNorthEastToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        imgNorthEastToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgNorthEastToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(imgNorthEastToggleButton, gridBagConstraints);

        buttonGroup4.add(imgSouthWestToggleButton);
        imgSouthWestToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgSouthWestToggleButton.text")); // NOI18N
        imgSouthWestToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        imgSouthWestToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgSouthWestToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(imgSouthWestToggleButton, gridBagConstraints);

        buttonGroup4.add(imgEastToggleButton);
        imgEastToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgEastToggleButton.text")); // NOI18N
        imgEastToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        imgEastToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgEastToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(imgEastToggleButton, gridBagConstraints);

        buttonGroup4.add(imgWestToggleButton);
        imgWestToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgWestToggleButton.text")); // NOI18N
        imgWestToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        imgWestToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgWestToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(imgWestToggleButton, gridBagConstraints);

        buttonGroup4.add(imgSouthEastToggleButton);
        imgSouthEastToggleButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.imgSouthEastToggleButton.text")); // NOI18N
        imgSouthEastToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        imgSouthEastToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgSouthEastToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(imgSouthEastToggleButton, gridBagConstraints);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(imgModeScaleImageBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(imgModeCropImageBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imgModeFitImageBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(imgModeResizeCellBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imgModeScaleImageBtn)
                    .addComponent(imgModeResizeCellBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imgModeCropImageBtn)
                    .addComponent(imgModeFitImageBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.add(jPanel3, "card2");

        borderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.borderPanel.border.title"))); // NOI18N

        jPanel8.setLayout(new java.awt.GridBagLayout());

        leftCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.leftCheckBox.text")); // NOI18N
        leftCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        leftCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                leftCheckBoxItemStateChanged(evt);
            }
        });

        iconButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.iconButton.text")); // NOI18N
        iconButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iconButtonActionPerformed(evt);
            }
        });

        rightCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.rightCheckBox.text")); // NOI18N
        rightCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rightCheckBoxItemStateChanged(evt);
            }
        });

        topCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.topCheckBox.text")); // NOI18N
        topCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                topCheckBoxItemStateChanged(evt);
            }
        });

        bottomCheckBox.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.bottomCheckBox.text")); // NOI18N
        bottomCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                bottomCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(leftCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bottomCheckBox)
                    .addComponent(topCheckBox)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(iconButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rightCheckBox))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(topCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(leftCheckBox)
                    .addComponent(iconButton)
                    .addComponent(rightCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomCheckBox))
        );

        jPanel8.add(jPanel9, new java.awt.GridBagConstraints());

        jLabel9.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.jLabel9.text")); // NOI18N

        borderSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                borderSpinnerStateChanged(evt);
            }
        });

        colorButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.colorButton.text")); // NOI18N
        colorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(solidRadioButton);
        solidRadioButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.solidRadioButton.text")); // NOI18N
        solidRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solidRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(dashedRadioButton);
        dashedRadioButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.dashedRadioButton.text")); // NOI18N
        dashedRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashedRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(dottedRadioButton);
        dottedRadioButton.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.dottedRadioButton.text")); // NOI18N
        dottedRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dottedRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout borderPanelLayout = new javax.swing.GroupLayout(borderPanel);
        borderPanel.setLayout(borderPanelLayout);
        borderPanelLayout.setHorizontalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(solidRadioButton)
                    .addComponent(dashedRadioButton)
                    .addComponent(dottedRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(borderSpinner)
                    .addComponent(colorButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        borderPanelLayout.setVerticalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borderPanelLayout.createSequentialGroup()
                .addGroup(borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, borderPanelLayout.createSequentialGroup()
                        .addComponent(solidRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dashedRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dottedRadioButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, borderPanelLayout.createSequentialGroup()
                        .addComponent(colorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(borderSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        textMarginsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.textMarginsPanel.border.title"))); // NOI18N
        textMarginsPanel.setLayout(new java.awt.GridBagLayout());

        topMarginSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        topMarginSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                topMarginSpinnerStateChanged(evt);
            }
        });

        lbTopMargin.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.lbTopMargin.text")); // NOI18N

        lbBottomMargin.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.lbBottomMargin.text")); // NOI18N

        leftMarginSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        leftMarginSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                leftMarginSpinnerStateChanged(evt);
            }
        });

        lbLeftMargin.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.lbLeftMargin.text")); // NOI18N

        lbRightMargin.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.lbRightMargin.text")); // NOI18N

        rightMarginSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        rightMarginSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rightMarginSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout iconPanel1Layout = new javax.swing.GroupLayout(iconPanel1);
        iconPanel1.setLayout(iconPanel1Layout);
        iconPanel1Layout.setHorizontalGroup(
            iconPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        iconPanel1Layout.setVerticalGroup(
            iconPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        bottomMarginSpinner.setPreferredSize(new java.awt.Dimension(64, 20));
        bottomMarginSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bottomMarginSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout middlePanel1Layout = new javax.swing.GroupLayout(middlePanel1);
        middlePanel1.setLayout(middlePanel1Layout);
        middlePanel1Layout.setHorizontalGroup(
            middlePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(middlePanel1Layout.createSequentialGroup()
                .addGroup(middlePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(middlePanel1Layout.createSequentialGroup()
                        .addComponent(lbLeftMargin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(leftMarginSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbTopMargin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(middlePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(topMarginSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(middlePanel1Layout.createSequentialGroup()
                        .addComponent(iconPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRightMargin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rightMarginSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(middlePanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(lbBottomMargin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomMarginSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        middlePanel1Layout.setVerticalGroup(
            middlePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(middlePanel1Layout.createSequentialGroup()
                .addGroup(middlePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(topMarginSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTopMargin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(middlePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(middlePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(leftMarginSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbLeftMargin))
                    .addGroup(middlePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbRightMargin)
                        .addComponent(rightMarginSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(iconPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(middlePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbBottomMargin)
                    .addComponent(bottomMarginSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        textMarginsPanel.add(middlePanel1, new java.awt.GridBagConstraints());

        pModeSupport.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.pModeSupport.border.title"))); // NOI18N

        buttonGroup3.add(cbShowAlways);
        cbShowAlways.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.cbShowAlways.text")); // NOI18N

        buttonGroup3.add(cbShowInTextMode);
        cbShowInTextMode.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.cbShowInTextMode.text")); // NOI18N

        buttonGroup3.add(cbShowInGraphicalMode);
        cbShowInGraphicalMode.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.cbShowInGraphicalMode.text")); // NOI18N

        javax.swing.GroupLayout pModeSupportLayout = new javax.swing.GroupLayout(pModeSupport);
        pModeSupport.setLayout(pModeSupportLayout);
        pModeSupportLayout.setHorizontalGroup(
            pModeSupportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pModeSupportLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbShowAlways)
                .addGap(26, 26, 26)
                .addComponent(cbShowInGraphicalMode)
                .addGap(34, 34, 34)
                .addComponent(cbShowInTextMode)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pModeSupportLayout.setVerticalGroup(
            pModeSupportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pModeSupportLayout.createSequentialGroup()
                .addGroup(pModeSupportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbShowAlways)
                    .addComponent(cbShowInTextMode)
                    .addComponent(cbShowInGraphicalMode))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fontPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textMarginsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(borderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pModeSupport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textMarginsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(borderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pModeSupport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        textMarginsPanel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.jPanel12.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void leftCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_leftCheckBoxItemStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
}//GEN-LAST:event_leftCheckBoxItemStateChanged

    private void rightCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rightCheckBoxItemStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
}//GEN-LAST:event_rightCheckBoxItemStateChanged

    private void topCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_topCheckBoxItemStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
}//GEN-LAST:event_topCheckBoxItemStateChanged

    private void bottomCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_bottomCheckBoxItemStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
}//GEN-LAST:event_bottomCheckBoxItemStateChanged

    private void borderSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_borderSpinnerStateChanged
        if (!updating) {
            updateBorder();
            apply();
        }
}//GEN-LAST:event_borderSpinnerStateChanged

    private void inheritBgCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_inheritBgCheckBoxItemStateChanged
        if (!updating) {
            apply();
            bgButton.setColor(appearance.getBgColor());
            samplePanel.setBackground(appearance.getBgColor());
            updateEnableState();
        }
    }//GEN-LAST:event_inheritBgCheckBoxItemStateChanged

    private void inheritFgCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_inheritFgCheckBoxItemStateChanged
        if (!updating) {
            apply();
            fgButton.setColor(appearance.getFgColor());
            sampleLabel.setForeground(appearance.getFgColor());
            updateEnableState();
        }
    }//GEN-LAST:event_inheritFgCheckBoxItemStateChanged

    private void adjustCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_adjustCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        // if (adjustCheckBox.isSelected()) {
        //vAlign = EReportCellVAlign.TOP;
        //     updateAlignment();
        // }
        updateEnableState();
        apply();
    }//GEN-LAST:event_adjustCheckBoxItemStateChanged

    private void northWestToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_northWestToggleButtonActionPerformed
        vAlign = EReportCellVAlign.TOP;
        hAlign = EReportCellHAlign.LEFT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_northWestToggleButtonActionPerformed

    private void northToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_northToggleButtonActionPerformed
        vAlign = EReportCellVAlign.TOP;
        hAlign = EReportCellHAlign.CENTER;
        updateAlignment();
        apply();
    }//GEN-LAST:event_northToggleButtonActionPerformed

    private void northEastToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_northEastToggleButtonActionPerformed
        vAlign = EReportCellVAlign.TOP;
        hAlign = EReportCellHAlign.RIGHT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_northEastToggleButtonActionPerformed

    private void westToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_westToggleButtonActionPerformed
        vAlign = EReportCellVAlign.MIDDLE;
        hAlign = EReportCellHAlign.LEFT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_westToggleButtonActionPerformed

    private void centerToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_centerToggleButtonActionPerformed
        vAlign = EReportCellVAlign.MIDDLE;
        hAlign = EReportCellHAlign.CENTER;
        updateAlignment();
        apply();
    }//GEN-LAST:event_centerToggleButtonActionPerformed

    private void eastToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eastToggleButtonActionPerformed
        vAlign = EReportCellVAlign.MIDDLE;
        hAlign = EReportCellHAlign.RIGHT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_eastToggleButtonActionPerformed

    private void southWestToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_southWestToggleButtonActionPerformed
        vAlign = EReportCellVAlign.BOTTOM;
        hAlign = EReportCellHAlign.LEFT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_southWestToggleButtonActionPerformed

    private void southToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_southToggleButtonActionPerformed
        vAlign = EReportCellVAlign.BOTTOM;
        hAlign = EReportCellHAlign.CENTER;
        updateAlignment();
        apply();
    }//GEN-LAST:event_southToggleButtonActionPerformed

    private void southEastToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_southEastToggleButtonActionPerformed
        vAlign = EReportCellVAlign.BOTTOM;
        hAlign = EReportCellHAlign.RIGHT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_southEastToggleButtonActionPerformed

    private void topSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_topSpinnerStateChanged
        apply();
    }//GEN-LAST:event_topSpinnerStateChanged

    private void leftSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_leftSpinnerStateChanged
        apply();
    }//GEN-LAST:event_leftSpinnerStateChanged

    private void widthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_widthSpinnerStateChanged
        apply();
    }//GEN-LAST:event_widthSpinnerStateChanged

    private void heightSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_heightSpinnerStateChanged
        apply();
    }//GEN-LAST:event_heightSpinnerStateChanged

    private void lineSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_lineSpinnerStateChanged
        apply();
    }//GEN-LAST:event_lineSpinnerStateChanged

    private void wrapCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wrapCheckBoxActionPerformed
        apply();
    }//GEN-LAST:event_wrapCheckBoxActionPerformed

    private void clipCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clipCheckBoxActionPerformed
        apply();
    }//GEN-LAST:event_clipCheckBoxActionPerformed

    private void iconButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iconButtonActionPerformed
        if (topCheckBox.isSelected() || leftCheckBox.isSelected()
                || rightCheckBox.isSelected() || bottomCheckBox.isSelected()) {
            topCheckBox.setSelected(false);
            leftCheckBox.setSelected(false);
            rightCheckBox.setSelected(false);
            bottomCheckBox.setSelected(false);
        } else {
            topCheckBox.setSelected(true);
            leftCheckBox.setSelected(true);
            rightCheckBox.setSelected(true);
            bottomCheckBox.setSelected(true);
        }
    }//GEN-LAST:event_iconButtonActionPerformed

    private void bgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bgButtonActionPerformed
        final Color clr = JColorChooser.showDialog(this, "Choose Background Color", bgButton.getColor());
        if (clr != null) {
            bgButton.setColor(clr);
            samplePanel.setBackground(clr);
            apply();
        }
    }//GEN-LAST:event_bgButtonActionPerformed

    private void fgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fgButtonActionPerformed
        final Color clr = JColorChooser.showDialog(this, "Choose Foreground Color", fgButton.getColor());
        if (clr != null) {
            fgButton.setColor(clr);
            sampleLabel.setForeground(clr);
            apply();
        }
    }//GEN-LAST:event_fgButtonActionPerformed

    private void colorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorButtonActionPerformed
        final Color clr = JColorChooser.showDialog(this, "Choose Border Color", colorButton.getColor());
        if (clr != null) {
            colorButton.setColor(clr);
            updateBorder();
            apply();
        }
    }//GEN-LAST:event_colorButtonActionPerformed

    private void solidRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solidRadioButtonActionPerformed
        if (!updating) {
            updateBorder();
            apply();
        }
}//GEN-LAST:event_solidRadioButtonActionPerformed

    private void dashedRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashedRadioButtonActionPerformed
        if (!updating) {
            updateBorder();
            apply();
        }
}//GEN-LAST:event_dashedRadioButtonActionPerformed

    private void dottedRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dottedRadioButtonActionPerformed
        if (!updating) {
            updateBorder();
            apply();
        }
}//GEN-LAST:event_dottedRadioButtonActionPerformed

    private void topMarginSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_topMarginSpinnerStateChanged
        if (!updating) {
            setSampleMergins();
            apply();
        }
    }//GEN-LAST:event_topMarginSpinnerStateChanged

    private void leftMarginSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_leftMarginSpinnerStateChanged
        if (!updating) {
            setSampleMergins();
            apply();
        }
    }//GEN-LAST:event_leftMarginSpinnerStateChanged

    private void rightMarginSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rightMarginSpinnerStateChanged
        if (!updating) {
            setSampleMergins();
            apply();
        }
    }//GEN-LAST:event_rightMarginSpinnerStateChanged

    private void bottomMarginSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bottomMarginSpinnerStateChanged
        if (!updating) {
            setSampleMergins();
            apply();
        }
    }//GEN-LAST:event_bottomMarginSpinnerStateChanged

    private void cbAdjustWidthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbAdjustWidthItemStateChanged
        if (updating) {
            return;
        }
        updateEnableState();
        apply();
    }//GEN-LAST:event_cbAdjustWidthItemStateChanged

    private void northAdjustToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_northAdjustToggleButtonActionPerformed
        vAlign = EReportCellVAlign.TOP;
        hAlign = EReportCellHAlign.JUSTIFY;
        updateAlignment();
        apply();
    }//GEN-LAST:event_northAdjustToggleButtonActionPerformed

    private void centerAdjustToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_centerAdjustToggleButtonActionPerformed
        vAlign = EReportCellVAlign.MIDDLE;
        hAlign = EReportCellHAlign.JUSTIFY;
        updateAlignment();
        apply();
    }//GEN-LAST:event_centerAdjustToggleButtonActionPerformed

    private void southAdjustToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_southAdjustToggleButtonActionPerformed
        vAlign = EReportCellVAlign.BOTTOM;
        hAlign = EReportCellHAlign.JUSTIFY;
        updateAlignment();
        apply();
    }//GEN-LAST:event_southAdjustToggleButtonActionPerformed

    private void imgNorthWestToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgNorthWestToggleButtonActionPerformed
        vAlign = EReportCellVAlign.TOP;
        hAlign = EReportCellHAlign.LEFT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_imgNorthWestToggleButtonActionPerformed

    private void imgNorthToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgNorthToggleButtonActionPerformed
        vAlign = EReportCellVAlign.TOP;
        hAlign = EReportCellHAlign.CENTER;
        updateAlignment();
        apply();
    }//GEN-LAST:event_imgNorthToggleButtonActionPerformed

    private void imgNorthEastToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgNorthEastToggleButtonActionPerformed
        vAlign = EReportCellVAlign.TOP;
        hAlign = EReportCellHAlign.RIGHT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_imgNorthEastToggleButtonActionPerformed

    private void imgWestToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgWestToggleButtonActionPerformed
        vAlign = EReportCellVAlign.MIDDLE;
        hAlign = EReportCellHAlign.LEFT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_imgWestToggleButtonActionPerformed

    private void imgSouthWestToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgSouthWestToggleButtonActionPerformed
        vAlign = EReportCellVAlign.BOTTOM;
        hAlign = EReportCellHAlign.LEFT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_imgSouthWestToggleButtonActionPerformed

    private void imgSouthToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgSouthToggleButtonActionPerformed
        vAlign = EReportCellVAlign.BOTTOM;
        hAlign = EReportCellHAlign.CENTER;
        updateAlignment();
        apply();
    }//GEN-LAST:event_imgSouthToggleButtonActionPerformed

    private void imgSouthEastToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgSouthEastToggleButtonActionPerformed
        vAlign = EReportCellVAlign.BOTTOM;
        hAlign = EReportCellHAlign.RIGHT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_imgSouthEastToggleButtonActionPerformed

    private void imgEastToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgEastToggleButtonActionPerformed
        vAlign = EReportCellVAlign.MIDDLE;
        hAlign = EReportCellHAlign.RIGHT;
        updateAlignment();
        apply();
    }//GEN-LAST:event_imgEastToggleButtonActionPerformed

    private void imgCenterToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgCenterToggleButtonActionPerformed
        vAlign = EReportCellVAlign.MIDDLE;
        hAlign = EReportCellHAlign.CENTER;
        updateAlignment();
        apply();
    }//GEN-LAST:event_imgCenterToggleButtonActionPerformed

    private void imgModeScaleImageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgModeScaleImageBtnActionPerformed
        if (imgModeScaleImageBtn.isSelected()) {
            scaleType = EImageScaleType.SCALE_TO_CONTAINER;
            apply();
            updateEnableState();
        }
    }//GEN-LAST:event_imgModeScaleImageBtnActionPerformed

    private void imgModeResizeCellBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgModeResizeCellBtnActionPerformed
        if (imgModeResizeCellBtn.isSelected()) {
            scaleType = EImageScaleType.RESIZE_CONTAINER;
            apply();
            updateEnableState();
        }
    }//GEN-LAST:event_imgModeResizeCellBtnActionPerformed

    private void imgModeCropImageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgModeCropImageBtnActionPerformed
        if (imgModeCropImageBtn.isSelected()) {
            scaleType = EImageScaleType.CROP;
            apply();
            updateEnableState();
        }
    }//GEN-LAST:event_imgModeCropImageBtnActionPerformed

    private void imgModeFitImageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgModeFitImageBtnActionPerformed
        if (imgModeFitImageBtn.isSelected()) {
            scaleType = EImageScaleType.FIT_TO_CONTAINER;
            apply();
            updateEnableState();
        }
    }//GEN-LAST:event_imgModeFitImageBtnActionPerformed

    private void chIgnoreZebraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chIgnoreZebraActionPerformed
        ignoreZebra = chIgnoreZebra.isSelected();
        apply();
        updateEnableState();
    }//GEN-LAST:event_chIgnoreZebraActionPerformed

    private void setSampleMergins() {
        final int top = MmUtils.mm2px(((BigDecimal) topMarginSpinner.getValue()).doubleValue());
        final int left = MmUtils.mm2px(((BigDecimal) leftMarginSpinner.getValue()).doubleValue());
        final int bottom = MmUtils.mm2px(((BigDecimal) bottomMarginSpinner.getValue()).doubleValue());
        final int right = MmUtils.mm2px(((BigDecimal) rightMarginSpinner.getValue()).doubleValue());
        final EmptyBorder border = new EmptyBorder(top, left, bottom, right);
        sampleLabel.setBorder(border);
    }

    private void apply() {
        if (updating) {
            return;
        }
        if (appearance.getOwnerForm().getMode() == AdsReportForm.Mode.GRAPHICS) {
            appearance.setHeightMm(((BigDecimal) heightSpinner.getValue()).doubleValue());
            appearance.setWidthMm(((BigDecimal) widthSpinner.getValue()).doubleValue());
            appearance.setTopMm(((BigDecimal) topSpinner.getValue()).doubleValue());
            appearance.setLeftMm(((BigDecimal) leftSpinner.getValue()).doubleValue());

            appearance.setMarginTopMm(((BigDecimal) topMarginSpinner.getValue()).doubleValue());
            appearance.setMarginLeftMm(((BigDecimal) leftMarginSpinner.getValue()).doubleValue());
            appearance.setMarginRightMm(((BigDecimal) rightMarginSpinner.getValue()).doubleValue());
            appearance.setMarginBottomMm(((BigDecimal) bottomMarginSpinner.getValue()).doubleValue());

        } else {
            appearance.setHeightRows(((Integer) heightSpinner.getValue()).intValue());
            appearance.setWidthCols(((Integer) widthSpinner.getValue()).intValue());
            appearance.setTopRow(((Integer) topSpinner.getValue()).intValue());
            appearance.setLeftColumn(((Integer) leftSpinner.getValue()).intValue());

            appearance.setMarginTopRows(((Integer) topMarginSpinner.getValue()).intValue());
            appearance.setMarginLeftCols(((Integer) leftMarginSpinner.getValue()).intValue());
            appearance.setMarginRightCols(((Integer) rightMarginSpinner.getValue()).intValue());
            appearance.setMarginBottomRows(((Integer) bottomMarginSpinner.getValue()).intValue());

        }

        if (imgModeCropImageBtn.isSelected()) {
            scaleType = EImageScaleType.CROP;
        } else if (imgModeFitImageBtn.isSelected()) {
            scaleType = EImageScaleType.FIT_TO_CONTAINER;
        } else if (imgModeResizeCellBtn.isSelected()) {
            scaleType = EImageScaleType.RESIZE_CONTAINER;
        } else if (imgModeScaleImageBtn.isSelected()) {
            scaleType = EImageScaleType.SCALE_TO_CONTAINER;
        }

        if (appearance.getCellType() == EReportCellType.DB_IMAGE) {
            ((AdsReportDbImageCell) appearance).setScaleType(scaleType);
        }

        appearance.setBgColorInherited(inheritBgCheckBox.isSelected());
        appearance.setBgColor(bgButton.getColor());
        appearance.setFontInherited(useBandFontCheckBox.isSelected());
        final AdsReportAbstractAppearance.Font reportFont = appearance.getFont();
        innerFontPanel.setFont(reportFont, useBandFontCheckBox.isSelected());
        if (!useBandFontCheckBox.isSelected()) {
            innerFontPanel.apply();
        }
        appearance.setIgnoreAltBgColor(ignoreZebra);
        appearance.setLineSpacingMm(((BigDecimal) lineSpinner.getValue()).doubleValue());
        appearance.setWrapWord(wrapCheckBox.isSelected());
        appearance.setClipContent(clipCheckBox.isSelected());
        appearance.setFgColorInherited(inheritFgCheckBox.isSelected());
        appearance.setFgColor(fgButton.getColor());
        appearance.setAdjustHeight(adjustCheckBox.isSelected());//!!!!!!
        appearance.setAdjustWidth(cbAdjustWidth.isSelected());
        //if (adjustCheckBox.isSelected()) {
        //appearance.setVAlign(EReportCellVAlign.ADJUST);
        //    appearance.setAdjustHeight(true);
        //} else {
        appearance.setVAlign(vAlign);
        //}
        appearance.setHAlign(hAlign);
        if (solidRadioButton.isSelected()) {
            appearance.getBorder().setStyle(EReportBorderStyle.SOLID);
        } else if (dashedRadioButton.isSelected()) {
            appearance.getBorder().setStyle(EReportBorderStyle.DASHED);
        } else if (dottedRadioButton.isSelected()) {
            appearance.getBorder().setStyle(EReportBorderStyle.DOTTED);
        }

        appearance.getBorder().setOnTop(topCheckBox.isSelected());
        appearance.getBorder().setOnLeft(leftCheckBox.isSelected());
        appearance.getBorder().setOnBottom(bottomCheckBox.isSelected());
        appearance.getBorder().setOnRight(rightCheckBox.isSelected());
        appearance.getBorder().setColor(colorButton.getColor());
        appearance.getBorder().setThicknessMm(((BigDecimal) borderSpinner.getValue()).doubleValue());
    }

    private final JCheckBox useBandFontCheckBox = new JCheckBox("Use band font");

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox adjustCheckBox;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton bgButton;
    private javax.swing.JPanel borderPanel;
    private javax.swing.JSpinner borderSpinner;
    private javax.swing.JCheckBox bottomCheckBox;
    private javax.swing.JSpinner bottomMarginSpinner;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JCheckBox cbAdjustWidth;
    private javax.swing.JRadioButton cbShowAlways;
    private javax.swing.JRadioButton cbShowInGraphicalMode;
    private javax.swing.JRadioButton cbShowInTextMode;
    private javax.swing.JToggleButton centerAdjustToggleButton;
    private javax.swing.JToggleButton centerToggleButton;
    private javax.swing.JCheckBox chIgnoreZebra;
    private javax.swing.JCheckBox clipCheckBox;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton colorButton;
    private javax.swing.JRadioButton dashedRadioButton;
    private javax.swing.JRadioButton dottedRadioButton;
    private javax.swing.JToggleButton eastToggleButton;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton fgButton;
    private javax.swing.JPanel fontPanel;
    private javax.swing.JSpinner heightSpinner;
    private javax.swing.JButton iconButton;
    private javax.swing.JPanel iconPanel1;
    private javax.swing.JToggleButton imgCenterToggleButton;
    private javax.swing.JToggleButton imgEastToggleButton;
    private javax.swing.JToggleButton imgModeCropImageBtn;
    private javax.swing.JToggleButton imgModeFitImageBtn;
    private javax.swing.JToggleButton imgModeResizeCellBtn;
    private javax.swing.JToggleButton imgModeScaleImageBtn;
    private javax.swing.JToggleButton imgNorthEastToggleButton;
    private javax.swing.JToggleButton imgNorthToggleButton;
    private javax.swing.JToggleButton imgNorthWestToggleButton;
    private javax.swing.JToggleButton imgSouthEastToggleButton;
    private javax.swing.JToggleButton imgSouthToggleButton;
    private javax.swing.JToggleButton imgSouthWestToggleButton;
    private javax.swing.JToggleButton imgWestToggleButton;
    private javax.swing.JCheckBox inheritBgCheckBox;
    private javax.swing.JCheckBox inheritFgCheckBox;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lbBottomMargin;
    private javax.swing.JLabel lbLeftMargin;
    private javax.swing.JLabel lbRightMargin;
    private javax.swing.JLabel lbTopMargin;
    private javax.swing.JLabel lblHeight;
    private javax.swing.JLabel lblLeft;
    private javax.swing.JLabel lblTop;
    private javax.swing.JLabel lblWidth;
    private javax.swing.JCheckBox leftCheckBox;
    private javax.swing.JSpinner leftMarginSpinner;
    private javax.swing.JSpinner leftSpinner;
    private javax.swing.JSpinner lineSpinner;
    private javax.swing.JPanel middlePanel1;
    private javax.swing.JToggleButton northAdjustToggleButton;
    private javax.swing.JToggleButton northEastToggleButton;
    private javax.swing.JToggleButton northToggleButton;
    private javax.swing.JToggleButton northWestToggleButton;
    private javax.swing.JPanel pModeSupport;
    private javax.swing.JCheckBox rightCheckBox;
    private javax.swing.JSpinner rightMarginSpinner;
    private javax.swing.JLabel sampleLabel;
    private javax.swing.JPanel samplePanel;
    private javax.swing.JCheckBox snapBottomBox;
    private javax.swing.JCheckBox snapTopBox;
    private javax.swing.JRadioButton solidRadioButton;
    private javax.swing.JToggleButton southAdjustToggleButton;
    private javax.swing.JToggleButton southEastToggleButton;
    private javax.swing.JToggleButton southToggleButton;
    private javax.swing.JToggleButton southWestToggleButton;
    private javax.swing.JPanel textMarginsPanel;
    private javax.swing.JCheckBox topCheckBox;
    private javax.swing.JSpinner topMarginSpinner;
    private javax.swing.JSpinner topSpinner;
    private javax.swing.JToggleButton westToggleButton;
    private javax.swing.JSpinner widthSpinner;
    private javax.swing.JCheckBox wrapCheckBox;
    // End of variables declaration//GEN-END:variables

}

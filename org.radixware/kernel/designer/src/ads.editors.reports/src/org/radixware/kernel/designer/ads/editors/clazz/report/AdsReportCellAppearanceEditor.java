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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
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
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance.Border;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportDbImageCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.enums.EImageScaleType;
import org.radixware.kernel.common.enums.EReportBorderStyle;
import org.radixware.kernel.common.enums.EReportCellHAlign;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.enums.EReportCellVAlign;
import org.radixware.kernel.common.enums.EReportTextFormat;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportWidgetUtils;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.MmUtils;
import org.radixware.kernel.designer.ads.reports.AdsReportDialogsUtils;
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

    private final AdsReportFontPanel innerFontPanel;
    private final AdsReportBackgroundPanel innerBackgroundPanel;
    
    private final PropertyChangeListener backgroundListener;
    private final PropertyChangeListener lineSpacingListener;
    private final PropertyChangeListener borderSpacingListener;

    /**
     * Creates new form AdsReportCellAppearanceEditor
     */
    @SuppressWarnings("deprecation")
    protected AdsReportCellAppearanceEditor(final AdsReportCell appearance) {
        super();
        this.appearance = appearance;
        initComponents();


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

        ComponentTitledBorder border = new ComponentTitledBorder(useBandFontCheckBox, fontPanel, new TitledBorder(""));
        fontPanel.setBorder(border);
        marginPanel.setBorder(BorderFactory.createTitledBorder("Text margins"));
        ChangeListener marginListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setSampleMergins();
            }
        };
        
        marginPanel.addTopSpinnerChangeListener(marginListener);
        marginPanel.addLeftSpinnerChangeListener(marginListener);
        marginPanel.addBottomSpinnerChangeListener(marginListener);
        marginPanel.addRightSpinnerChangeListener(marginListener);

        if (appearance.getOwnerForm().getMode() == AdsReportForm.Mode.GRAPHICS) {

            topSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            topSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(topSpinner));
            leftSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            leftSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(leftSpinner));
            widthSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            widthSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(widthSpinner));
            heightSpinner.setModel(new BigDecimalSpinnerModel(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1000), BigDecimal.valueOf(1)));
            heightSpinner.setEditor(new CheckedBigDecimalSpinnerEditor(heightSpinner));

        } else {
            lblTop.setText(lblTop.getText().replace("mm.", "row"));
            lblHeight.setText(lblHeight.getText().replace("mm.", "rows"));
            lblLeft.setText(lblLeft.getText().replace("mm.", "col"));
            lblWidth.setText(lblWidth.getText().replace("mm.", "cols"));

            topSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            topSpinner.setEditor(new CheckedNumberSpinnerEditor(topSpinner));
            leftSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            leftSpinner.setEditor(new CheckedNumberSpinnerEditor(leftSpinner));
            widthSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            widthSpinner.setEditor(new CheckedNumberSpinnerEditor(widthSpinner));
            heightSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
            heightSpinner.setEditor(new CheckedNumberSpinnerEditor(heightSpinner));
        }

        
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
        innerBackgroundPanel = new AdsReportBackgroundPanel();
        backgroundListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()){
                    case AdsReportDialogsUtils.BACKGROUND_COLOR:
                    case AdsReportDialogsUtils.INHERIT_BACKROUND:
                        innerBackgroundPanel.apply(appearance);
                        samplePanel.setBackground(appearance.getBgColor());
                        break;
                    case AdsReportDialogsUtils.IGNORE_ZEBRA:                        
                            innerBackgroundPanel.apply(appearance);
                }
            }
        };
        backgroundPanel.add(innerBackgroundPanel, BorderLayout.NORTH);
        
        lineSpacingListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                adsReportLineSpacingPanel1.apply(appearance);
            }
        };
        
        borderSpacingListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                adsReportBorderPanel1.apply(appearance);
                updateBorder();
            }
        };
        setupInitialValues();
    }

    private void setupInitialValues() {
        updating = true;
        if (appearance.getOwnerForm().getMode() == AdsReportForm.Mode.GRAPHICS) {
            topSpinner.setValue(BigDecimal.valueOf(appearance.getTopMm()));
            leftSpinner.setValue(BigDecimal.valueOf(appearance.getLeftMm()));
            widthSpinner.setValue(BigDecimal.valueOf(appearance.getWidthMm()));
            heightSpinner.setValue(BigDecimal.valueOf(appearance.getHeightMm()));
            marginPanel.open(appearance.getMarginMm());

        } else {
            topSpinner.setValue(appearance.getTopRow());
            leftSpinner.setValue(appearance.getLeftColumn());
            widthSpinner.setValue(appearance.getWidthCols());
            heightSpinner.setValue(appearance.getHeightRows());
            marginPanel.open(appearance.getMarginTxt());
        }
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
        innerBackgroundPanel.open(appearance);
        samplePanel.setBackground(appearance.getBgColor());
        useBandFontCheckBox.setSelected(appearance.isFontInherited());
        innerFontPanel.setFont(appearance.getFont(), true);
        updateFont();
        adsReportLineSpacingPanel1.open(appearance);
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
        adsReportBorderPanel1.open(appearance.getBorder());
        snapTopBox.setSelected(appearance.isSnapTopEdge());
        snapBottomBox.setSelected(appearance.isSnapBottomEdge());
        updateEnableState();
        updateBorder();

        EReportTextFormat textFormat = appearance.getTextFormat();
        cbPlainFormat.setSelected(textFormat == EReportTextFormat.PLAIN);
        cbRichFormat.setSelected(textFormat == EReportTextFormat.RICH);
        cbUseSpacePadding.setSelected(appearance.isUseTxtPadding());
        if (appearance.isUseTxtPadding() && appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS) {
            northWestToggleButton.setSelected(true);
        }
        updating = false;
    }

    private void updateEnableState() {
        boolean isTextMode = appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS;
        boolean enabled = !appearance.isReadOnly();
        heightSpinner.setEnabled(enabled);
        topSpinner.setEnabled(enabled);
        leftSpinner.setEnabled(enabled);
        widthSpinner.setEnabled(enabled);
        innerBackgroundPanel.updateEnableState();
        useBandFontCheckBox.setEnabled(enabled && !isTextMode);
        innerFontPanel.setPanelEnabled(enabled && !isTextMode && !useBandFontCheckBox.isSelected());
        marginPanel.setEnabled(enabled);
        setEnabled(enabled && !isTextMode);
        wrapCheckBox.setEnabled(enabled);
        clipCheckBox.setEnabled(enabled);
        inheritFgCheckBox.setEnabled(enabled && !isTextMode);
        fgButton.setEnabled(enabled && !inheritFgCheckBox.isSelected() && !isTextMode);
        final boolean isChart = appearance.getCellType() == EReportCellType.CHART;
        final boolean isUseAlign = !isChart && (!isTextMode || appearance.isUseTxtPadding());
        adjustCheckBox.setEnabled(enabled && !isChart);
        cbAdjustWidth.setEnabled(enabled && !isChart);
        northEastToggleButton.setEnabled(enabled && isUseAlign);
        northToggleButton.setEnabled(enabled && isUseAlign);
        northWestToggleButton.setEnabled(enabled && isUseAlign);
        northAdjustToggleButton.setEnabled(enabled && isUseAlign);
        westToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ isUseAlign);
        centerToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ isUseAlign);
        eastToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ isUseAlign);
        centerAdjustToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ isUseAlign);
        southEastToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ isUseAlign);
        southToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ isUseAlign);
        southWestToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ isUseAlign);
        southAdjustToggleButton.setEnabled(enabled && /*!adjustCheckBox.isSelected()&&*/ isUseAlign);

        adsReportBorderPanel1.setEnabled(enabled && !isTextMode);
        snapTopBox.setEnabled(enabled);
        snapBottomBox.setEnabled(enabled);
        cbShowAlways.setEnabled(enabled);
        cbShowInTextMode.setEnabled(enabled);
        cbShowInGraphicalMode.setEnabled(enabled);
        
        cbPlainFormat.setEnabled(enabled);
        cbRichFormat.setEnabled(enabled);

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
        
        cbUseSpacePadding.setVisible(isTextMode);
        cbUseSpacePadding.setEnabled(enabled);
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
    }
    
    private javax.swing.border.Border getBorder( 
            double thicknessMm, EReportBorderStyle style, Color color, Insets ins,
            boolean onTop, boolean onLeft, boolean onBottom, boolean onRight) {
        if (!onTop && !onLeft && !onBottom && !onRight) {
            return null;
        }
        int w = MmUtils.mm2px(thicknessMm);
        if (w == 0) {
            w = 1;
        }
        int top = onTop ? w : 0;
        int left = onLeft ? w : 0;
        int bottom = onBottom ? w : 0;
        int right = onRight ? w : 0;
        Insets insets = new Insets(top + ins.top, left + ins.left, bottom + ins.bottom, right + ins.right);
        switch (style) {
            case DASHED:
                return new DashedBorder(
                        top,
                        left,
                        bottom,
                        right,
                        color,
                        Math.max(w * 2, 10), insets);
            case DOTTED:
                return new DashedBorder(
                        top,
                        left,
                        bottom,
                        right,
                        color,
                        Math.max(w, 1), insets);
            default:
                return new MatteBorder(
                        top,
                        left,
                        bottom,
                        right,
                        color);
        }
        
    }

    private void updateBorder() {
        Border border = appearance.getBorder();
        samplePanel.setBorder(null);
        final Insets ins = samplePanel.getInsets();
        javax.swing.border.Border topBorder = getBorder(border.getTopThicknessMm(), border.getTopStyle(), border.getTopColor(),
                ins, border.getOnTop(), false, false, false);
        javax.swing.border.Border leftBorder = getBorder(border.getLeftThicknessMm(), border.getLeftStyle(), border.getLeftColor(), 
                ins, false, border.getOnLeft(), false, false);
        javax.swing.border.Border bottomBorder = getBorder(border.getBottomThicknessMm(), border.getBottomStyle(), border.getBottomColor(), 
                ins, false, false, border.getOnBottom(), false);
        javax.swing.border.Border rightBorder = getBorder(border.getRightThicknessMm(), border.getRightStyle(), border.getRightColor(), 
                ins, false, false, false, border.getOnRight());
        
        javax.swing.border.Border compBorder = null;
        if (topBorder != null) {
            compBorder = topBorder;
        }
        if (leftBorder != null){
            if (compBorder != null){
                compBorder = BorderFactory.createCompoundBorder(leftBorder, compBorder);
            } else {
                compBorder = leftBorder;
            }
        }
        if (bottomBorder != null){
            if (compBorder != null){
                compBorder = BorderFactory.createCompoundBorder(bottomBorder, compBorder);
            } else {
                compBorder = bottomBorder;
            }
        }
        if (rightBorder != null){
            if (compBorder != null){
                compBorder = BorderFactory.createCompoundBorder(rightBorder, compBorder);
            } else {
                compBorder = rightBorder;
            }
        }
        
        samplePanel.setBorder(compBorder);
        boolean isTextMode = appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS;
        adsReportBorderPanel1.setEnabled(!appearance.isReadOnly() && !isTextMode);

    }
    private static final double MM2PTS_CONST = 25.4 / 72;

    protected static BigDecimal mm2pts(BigDecimal mm) {
        return BigDecimal.valueOf(Math.floor(10.0 * mm.doubleValue() / MM2PTS_CONST) / 10.0);
    }

    protected static BigDecimal pts2mm(BigDecimal pts) {
        return BigDecimal.valueOf(Math.floor(10.0 * pts.doubleValue() * MM2PTS_CONST) / 10.0);
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
        textFormatGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        samplePanel = new javax.swing.JPanel();
        sampleLabel = new javax.swing.JLabel();
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
        backgroundPanel = new javax.swing.JPanel();
        fontPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        wrapCheckBox = new javax.swing.JCheckBox();
        clipCheckBox = new javax.swing.JCheckBox();
        inheritFgCheckBox = new javax.swing.JCheckBox();
        fgButton = new org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton();
        jPanel12 = new javax.swing.JPanel();
        cbRichFormat = new javax.swing.JRadioButton();
        cbPlainFormat = new javax.swing.JRadioButton();
        adsReportLineSpacingPanel1 = new org.radixware.kernel.designer.ads.editors.clazz.report.AdsReportLineSpacingPanel();
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
        cbUseSpacePadding = new javax.swing.JCheckBox();
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
        marginPanel = new org.radixware.kernel.designer.ads.editors.clazz.report.MarginPanel();
        pModeSupport = new javax.swing.JPanel();
        cbShowAlways = new javax.swing.JRadioButton();
        cbShowInTextMode = new javax.swing.JRadioButton();
        cbShowInGraphicalMode = new javax.swing.JRadioButton();
        adsReportBorderPanel1 = new org.radixware.kernel.designer.ads.editors.clazz.report.appearance.AdsReportBorderPanel();

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

        backgroundPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(samplePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(samplePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backgroundPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
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
            .addGap(0, 51, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.jPanel4.border.title"))); // NOI18N

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

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.jPanel12.border.title"))); // NOI18N

        textFormatGroup.add(cbRichFormat);
        cbRichFormat.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.cbRichFormat.text")); // NOI18N
        cbRichFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRichFormatActionPerformed(evt);
            }
        });

        textFormatGroup.add(cbPlainFormat);
        cbPlainFormat.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.cbPlainFormat.text")); // NOI18N
        cbPlainFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPlainFormatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(cbRichFormat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbPlainFormat)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbRichFormat)
                    .addComponent(cbPlainFormat))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(inheritFgCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fgButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(wrapCheckBox)
                                .addGap(18, 18, 18)
                                .addComponent(clipCheckBox))
                            .addComponent(adsReportLineSpacingPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adsReportLineSpacingPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wrapCheckBox)
                    .addComponent(clipCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inheritFgCheckBox)
                    .addComponent(fgButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
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

        cbUseSpacePadding.setText(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.cbUseSpacePadding.text")); // NOI18N
        cbUseSpacePadding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbUseSpacePaddingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(adjustCheckBox)
                            .addComponent(cbAdjustWidth))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(snapBottomBox)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(snapTopBox)
                                .addGap(36, 36, 36)
                                .addComponent(cbUseSpacePadding)))
                        .addGap(0, 51, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adjustCheckBox)
                    .addComponent(snapTopBox)
                    .addComponent(cbUseSpacePadding))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbAdjustWidth)
                    .addComponent(snapBottomBox))
                .addGap(5, 5, 5)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                .addGap(10, 10, 10))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.add(jPanel3, "card2");

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
                .addContainerGap(26, Short.MAX_VALUE))
        );

        adsReportBorderPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsReportCellAppearanceEditor.class, "AdsReportCellAppearanceEditor.adsReportBorderPanel1.border.title"))); // NOI18N
        adsReportBorderPanel1.setMaximumSize(new java.awt.Dimension(435, 211));
        adsReportBorderPanel1.setMinimumSize(new java.awt.Dimension(435, 211));
        adsReportBorderPanel1.setPreferredSize(new java.awt.Dimension(435, 211));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fontPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pModeSupport, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(marginPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(adsReportBorderPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(marginPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(adsReportBorderPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(pModeSupport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents

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

    private void wrapCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wrapCheckBoxActionPerformed
        apply();
    }//GEN-LAST:event_wrapCheckBoxActionPerformed

    private void clipCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clipCheckBoxActionPerformed
        apply();
    }//GEN-LAST:event_clipCheckBoxActionPerformed

    private void fgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fgButtonActionPerformed
        final Color clr = JColorChooser.showDialog(this, "Choose Foreground Color", fgButton.getColor());
        if (clr != null) {
            fgButton.setColor(clr);
            sampleLabel.setForeground(clr);
            apply();
        }
    }//GEN-LAST:event_fgButtonActionPerformed

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

    private void cbRichFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRichFormatActionPerformed
        apply();
    }//GEN-LAST:event_cbRichFormatActionPerformed

    private void cbPlainFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPlainFormatActionPerformed
        apply();
    }//GEN-LAST:event_cbPlainFormatActionPerformed

    private void cbUseSpacePaddingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUseSpacePaddingActionPerformed
        apply();
        if (appearance.isUseTxtPadding() && appearance.getOwnerForm().getMode() != AdsReportForm.Mode.GRAPHICS) {
            northWestToggleButton.setSelected(true);
        }
        updateEnableState();      
    }//GEN-LAST:event_cbUseSpacePaddingActionPerformed

    private void setSampleMergins() {
        final int top;
        final int left;
        final int bottom;
        final int right;
        if (appearance.getOwnerForm().getMode() == AdsReportForm.Mode.GRAPHICS) {
            top = MmUtils.mm2px(marginPanel.getTopValue().doubleValue());
            left = MmUtils.mm2px(marginPanel.getLeftValue().doubleValue());
            bottom = MmUtils.mm2px(marginPanel.getBottomValue().doubleValue());
            right = MmUtils.mm2px(marginPanel.getRightValue().doubleValue());
        } else {
            top = marginPanel.getTopValue().intValue();
            left = marginPanel.getLeftValue().intValue();
            bottom = marginPanel.getBottomValue().intValue();
            right = marginPanel.getRightValue().intValue();
        }
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

        } else {
            appearance.setHeightRows(((Integer) heightSpinner.getValue()).intValue());
            appearance.setWidthCols(((Integer) widthSpinner.getValue()).intValue());
            appearance.setTopRow(((Integer) topSpinner.getValue()).intValue());
            appearance.setLeftColumn(((Integer) leftSpinner.getValue()).intValue());

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

        appearance.setFontInherited(useBandFontCheckBox.isSelected());
        final AdsReportAbstractAppearance.Font reportFont = appearance.getFont();
        innerFontPanel.setFont(reportFont, useBandFontCheckBox.isSelected());
        if (!useBandFontCheckBox.isSelected()) {
            innerFontPanel.apply();
        }
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
        
        if (cbPlainFormat.isSelected()){
            appearance.setTextFormat(EReportTextFormat.PLAIN);
        } else {
            appearance.setTextFormat(EReportTextFormat.RICH);
        }
        
        appearance.setUseTxtPadding(cbUseSpacePadding.isSelected());
    }

    private final JCheckBox useBandFontCheckBox = new JCheckBox("Use band font");

    @Override
    public void addNotify() {
        super.addNotify();
        innerBackgroundPanel.addPropertyChangeListener(backgroundListener);
        adsReportLineSpacingPanel1.addPropertyChangeListener(AdsReportDialogsUtils.LINE_SPACING, lineSpacingListener);
        adsReportBorderPanel1.addPropertyChangeListener(AdsReportDialogsUtils.BORDER_CHANGE, borderSpacingListener);
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        innerBackgroundPanel.removePropertyChangeListener(backgroundListener);
        adsReportLineSpacingPanel1.removePropertyChangeListener(AdsReportDialogsUtils.LINE_SPACING, lineSpacingListener);
        adsReportBorderPanel1.removePropertyChangeListener(AdsReportDialogsUtils.BORDER_CHANGE, borderSpacingListener);
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox adjustCheckBox;
    private org.radixware.kernel.designer.ads.editors.clazz.report.appearance.AdsReportBorderPanel adsReportBorderPanel1;
    private org.radixware.kernel.designer.ads.editors.clazz.report.AdsReportLineSpacingPanel adsReportLineSpacingPanel1;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JCheckBox cbAdjustWidth;
    private javax.swing.JRadioButton cbPlainFormat;
    private javax.swing.JRadioButton cbRichFormat;
    private javax.swing.JRadioButton cbShowAlways;
    private javax.swing.JRadioButton cbShowInGraphicalMode;
    private javax.swing.JRadioButton cbShowInTextMode;
    private javax.swing.JCheckBox cbUseSpacePadding;
    private javax.swing.JToggleButton centerAdjustToggleButton;
    private javax.swing.JToggleButton centerToggleButton;
    private javax.swing.JCheckBox clipCheckBox;
    private javax.swing.JToggleButton eastToggleButton;
    private org.radixware.kernel.designer.ads.editors.clazz.report.ColorButton fgButton;
    private javax.swing.JPanel fontPanel;
    private javax.swing.JSpinner heightSpinner;
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
    private javax.swing.JCheckBox inheritFgCheckBox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lblHeight;
    private javax.swing.JLabel lblLeft;
    private javax.swing.JLabel lblTop;
    private javax.swing.JLabel lblWidth;
    private javax.swing.JSpinner leftSpinner;
    private org.radixware.kernel.designer.ads.editors.clazz.report.MarginPanel marginPanel;
    private javax.swing.JToggleButton northAdjustToggleButton;
    private javax.swing.JToggleButton northEastToggleButton;
    private javax.swing.JToggleButton northToggleButton;
    private javax.swing.JToggleButton northWestToggleButton;
    private javax.swing.JPanel pModeSupport;
    private javax.swing.JLabel sampleLabel;
    private javax.swing.JPanel samplePanel;
    private javax.swing.JCheckBox snapBottomBox;
    private javax.swing.JCheckBox snapTopBox;
    private javax.swing.JToggleButton southAdjustToggleButton;
    private javax.swing.JToggleButton southEastToggleButton;
    private javax.swing.JToggleButton southToggleButton;
    private javax.swing.JToggleButton southWestToggleButton;
    private javax.swing.ButtonGroup textFormatGroup;
    private javax.swing.JSpinner topSpinner;
    private javax.swing.JToggleButton westToggleButton;
    private javax.swing.JSpinner widthSpinner;
    private javax.swing.JCheckBox wrapCheckBox;
    // End of variables declaration//GEN-END:variables

}

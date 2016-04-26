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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt.BrushStyle;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QLineF;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyle.SubElement;
import com.trolltech.qt.gui.QStyleOption;
import com.trolltech.qt.gui.QStyleOptionButton;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class TristateCheckBoxStyle extends WidgetUtils.CustomStyle {
    
    public static final int INDICATOR_SIZE = 13;
    
    private static final QRect ZERO_RECT = new QRect(0,0,0,0);

    public TristateCheckBoxStyle(final QWidget sourceWidget) {
        super(sourceWidget);
    }

    @Override
    public void drawControl(final QStyle.ControlElement element, final QStyleOption options, final QPainter p, final QWidget widget) {
        if (element == QStyle.ControlElement.CE_CheckBox && (options instanceof QStyleOptionButton)) {
            QStyleOptionButton subopt = new QStyleOptionButton((QStyleOptionButton) options);
            subopt.setRect(subElementRect(SubElement.SE_CheckBoxIndicator, options, widget));
            drawPrimitive(QStyle.PrimitiveElement.PE_IndicatorCheckBox, subopt, p, widget);
            subopt.setRect(subElementRect(SubElement.SE_CheckBoxContents, options, widget));
            drawControl(QStyle.ControlElement.CE_CheckBoxLabel, subopt, p, widget);
            if (subopt.state().isSet(StateFlag.State_HasFocus)) {
                QStyleOptionButton fropt = (QStyleOptionButton) options;
                fropt.setRect(subElementRect(SubElement.SE_CheckBoxFocusRect, options, widget));
                //drawPrimitive(QStyle.PrimitiveElement.PE_FrameFocusRect, fropt, p, widget);
            }
        } else {
            super.drawControl(element, options, p, widget);
        }
    }

    @Override
    public void drawPrimitive(final QStyle.PrimitiveElement primitive, final QStyleOption options, final QPainter painter, final QWidget widget) {
        if (primitive == QStyle.PrimitiveElement.PE_IndicatorCheckBox && widget instanceof QCheckBox) {
            final QCheckBox checkBox = ((QCheckBox) widget);
            final boolean highlighted = checkBox.hasFocus();
            drawCheckBox(painter, options, highlighted, checkBox.checkState());
        } else if (primitive != QStyle.PrimitiveElement.PE_FrameFocusRect) {
            super.drawPrimitive(primitive, options, painter, widget);
        }
    }

    @Override
    public int pixelMetric(PixelMetric element, QStyleOption option, QWidget widget) {
        switch (element){
            case PM_IndicatorHeight:
            case PM_IndicatorWidth:
                return INDICATOR_SIZE;
            default:
                return super.pixelMetric(element, option, widget);
        }        
    }

    @Override
    public QRect subElementRect(SubElement subElement, QStyleOption option, QWidget widget) {
        if (subElement==SubElement.SE_CheckBoxLayoutItem){
            return ZERO_RECT;
        }
        return super.subElementRect(subElement, option, widget);
    }
    
    

    public static void drawCheckBox(final QPainter painter,
            final QStyleOption options,
            final boolean highlighted,
            final CheckState state) {
        final QBrush fill;
        if (options.state().isSet(QStyle.StateFlag.State_NoChange)) {
            fill = new QBrush(options.palette().base().color(), BrushStyle.Dense4Pattern);
        } else if (options.state().isSet(QStyle.StateFlag.State_Sunken)) {
            fill = new QBrush(options.palette().button());
        } else if (options.state().isSet(QStyle.StateFlag.State_Enabled)) {
            fill = new QBrush(options.palette().base());
        } else {
            fill = options.palette().window();
        }
        painter.save();
        QRect rect = options.rect();
        painter.fillRect(rect, fill);
        final List<QLineF> lines = new ArrayList<QLineF>(9);
        if (state == CheckState.Checked) {
            //������ "�������"
            final int arrowSize = 2;
            painter.setPen(options.palette().text().color());
            int xx = rect.x() + 4,
                    yy = rect.y() + 6;
            for (int i = 0; i < 4; ++i) {
                lines.add(new QLineF(xx, yy, xx, yy + arrowSize));
                xx++;
                yy++;
            }
            yy -= 2;
            for (int i = 4; i < 9; ++i) {
                lines.add(new QLineF(xx, yy, xx, yy + arrowSize));
                ++xx;
                --yy;
            }
            painter.drawLinesF(lines);
        } else if (state == CheckState.PartiallyChecked) {
            //������ "���������"
            final QRect innerRect;
            if (highlighted) {
                innerRect = rect.adjusted(4, 4, -3, -3);
            } else {
                innerRect = rect.adjusted(5, 5, -3, -3);
            }

            painter.fillRect(innerRect, new QBrush(options.palette().mid()));
        }
        //������ �����:
        if (highlighted) {
            painter.setPen(options.palette().highlight().color());
        } else {
            painter.setPen(options.palette().dark().color());
        }
        painter.drawRect(rect.adjusted(0, 0, 0, -1));
        if (!highlighted) {
            //������ ���� �����
            painter.setPen(options.palette().shadow().color());
            painter.drawLine(rect.x() + 1, rect.y() + 1,
                    rect.x() + rect.width() - 1, rect.y() + 1);
            painter.drawLine(rect.x() + 1, rect.y() + 1,
                    rect.x() + 1, rect.y() + rect.height() - 1);
        }

        painter.restore();
    }
}

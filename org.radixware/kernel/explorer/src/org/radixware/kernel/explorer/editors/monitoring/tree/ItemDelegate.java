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
package org.radixware.kernel.explorer.editors.monitoring.tree;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QAbstractTextDocumentLayout_PaintContext;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyle.State;
import com.trolltech.qt.gui.QStyleOptionProgressBar;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QStyleOptionViewItemV4;
import com.trolltech.qt.gui.QStyledItemDelegate;
import com.trolltech.qt.gui.QTextDocument;
import java.text.DecimalFormat;
import java.util.List;

public class ItemDelegate extends QStyledItemDelegate {

    private QTextDocument document = new QTextDocument();
    private final int height = 22;
    private final int space = 5;
    private final int iconSize = 20;
    private final QColor error_val_color = QColor.darkRed;
    private final QColor warning_val_color = QColor.darkYellow;
    private final QColor val_color = QColor.blue;

    @Override
    public QSize sizeHint(QStyleOptionViewItem qsovi, QModelIndex index) {
        MetricData metricData = (MetricData) index.data(ItemDataRole.UserRole);
        if ((metricData != null) && (metricData.getType() == MetricData.Type.HTML)) {
            QStyleOptionViewItemV4 optionV4 = (QStyleOptionViewItemV4) qsovi;
            initStyleOption(optionV4, index);
            document.setHtml(optionV4.text());
            document.setTextWidth(optionV4.rect().width());
            return new QSize((int) document.idealWidth(), height);
        }
        QSize size = super.sizeHint(qsovi, index);
        size.setHeight(height);
        int width = metricData != null ? metricData.getIconList().size() * iconSize : 0;
        size.setWidth(size.width() + width);
        return size;
    }

    public ItemDelegate() {
        super();
    }

    @Override
    public void paint(QPainter painter, QStyleOptionViewItem option, QModelIndex index) {
        MetricData metricData = (MetricData) index.data(ItemDataRole.UserRole);
        if (metricData != null) {
            if (metricData.getType() == MetricData.Type.DIAGRAM) {
                paintDiagram(painter, option, metricData, index);
            } else if (metricData.getType() == MetricData.Type.HTML) {
                paintHtml(painter, option, index);
            } else if (metricData.getType() == MetricData.Type.DECORATIONS) {
                paintDecorations(painter, option, metricData, index);
            } else {
                paintText(painter, option, metricData, index);
            }
        } else {
            super.paint(painter, option, index);
        }
    }

    private void paintDiagram(QPainter painter, QStyleOptionViewItem option, MetricData metricData, QModelIndex index) {
        if (metricData.getVal() != null) {
            painter.save();
            QStyleOptionProgressBar opt = new QStyleOptionProgressBar();
            QRect progressRect = option.rect();
            opt.setRect(progressRect);
            opt.setMinimum(metricData.getMinVal());
            opt.setMaximum(metricData.getMaxVal());
            String text = null;
            if (metricData.getText() != null && !metricData.getText().isEmpty()) {
                text = metricData.getText();
            } else {
                DecimalFormat f = new DecimalFormat("0.0");
                text = /*metricData.getText();*/ f.format(metricData.getVal()) + "%";
            }
            int progress = metricData.getVal().intValue();
            progress = progress > metricData.getMaxVal() ? metricData.getMaxVal() : (progress < metricData.getMinVal() ? metricData.getMinVal() : progress);
            opt.setProgress(progress);
            QPalette p = opt.palette();
            QBrush b = opt.palette().highlight();
            if (metricData.getSeverityColor() != null) {
                b.setColor(metricData.getSeverityColor());
            } else {
                b.setColor(calcColor(metricData));
            }
            p.setBrush(QPalette.ColorRole.Highlight, b);
            opt.setPalette(p);

            opt.setText(text);
            opt.setTextAlignment(AlignmentFlag.AlignCenter);
            opt.setTextVisible(true);
            QApplication.style().drawControl(QStyle.ControlElement.CE_ProgressBar, opt, painter);
            painter.restore();
        } else {
            super.paint(painter, option, index);
        }
    }

    private QColor calcColor(MetricData metricData) {
        if (metricData.getRange() == MetricData.Range.ERROR) {
            return error_val_color;
        }
        if (metricData.getRange() == MetricData.Range.WARNING) {
            return warning_val_color;
        }
        return val_color;
    }

    private void paintText(QPainter painter, QStyleOptionViewItem option, MetricData metricData, QModelIndex index) {
        painter.save();
        if ((option.state().value() & QStyle.StateFlag.State_Selected.value()) > 0) {
            painter.fillRect(option.rect(), option.palette().highlight());
            QPalette.ColorGroup colorGroup = ((option.state().value() & QStyle.StateFlag.State_Active.value()) > 0)
                    ? QPalette.ColorGroup.Active : QPalette.ColorGroup.Inactive;
            QColor color = option.palette().color(colorGroup, QPalette.ColorRole.HighlightedText);
            painter.setPen(color);
        }

        QRect rect = option.rect();
        int x = rect.x();
        List<QIcon> icons = metricData.getIconList();
        for (int i = 0; i < icons.size(); i++) {
            QIcon icon = icons.get(i);
            if (icon != null) {
                QPixmap p = icons.get(i).pixmap(iconSize, iconSize);
                painter.drawPixmap(x, rect.y(), p);
                x += p.width();
            }
        }
        painter.setClipRect(rect);
        QRect textRect = new QRect(x + space, rect.y(), rect.width() + x + space, rect.height());
        //QStyleOptionViewItemV4 optionV4 =(QStyleOptionViewItemV4)option;
        //initStyleOption(optionV4,index);
        String text = (String) index.data(ItemDataRole.DisplayRole);
        painter.drawText(textRect, AlignmentFlag.AlignLeft.value() | AlignmentFlag.AlignVCenter.value(), text/*
         * optionV4.text()
         */);
        painter.restore();
    }

    private void paintDecorations(QPainter painter, QStyleOptionViewItem option, MetricData metricData, QModelIndex index) {
        painter.save();
        if ((option.state().value() & QStyle.StateFlag.State_Selected.value()) > 0) {
            painter.fillRect(option.rect(), option.palette().highlight());
            QPalette.ColorGroup colorGroup = ((option.state().value() & QStyle.StateFlag.State_Active.value()) > 0)
                    ? QPalette.ColorGroup.Active : QPalette.ColorGroup.Inactive;
            QColor color = option.palette().color(colorGroup, QPalette.ColorRole.HighlightedText);
            painter.setPen(color);
        }

        QColor backgroundColor = metricData.getSeverityColor();
        if (backgroundColor != null) {
            painter.fillRect(option.rect(), backgroundColor);
        }

        option.setDecorationPosition(com.trolltech.qt.gui.QStyleOptionViewItem.Position.Top);
        option.setDecorationAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(
                com.trolltech.qt.core.Qt.AlignmentFlag.AlignBottom,
                com.trolltech.qt.core.Qt.AlignmentFlag.AlignHCenter));

        final QRect rect = option.rect();
        int x = rect.x();
        painter.setClipRect(rect);
        final List<MonitoringTreeItemDecoration> decorations = metricData.getDecorations();
        if (decorations != null) {
            for (MonitoringTreeItemDecoration decoration : decorations) {
                QIcon icon = decoration.getIcon();
                if (icon != null) {
                    QPixmap p = icon.pixmap(iconSize, iconSize);
                    if (decorations.size() == 1 && decoration.getText() == null) {
                        int centerX = x + rect.width() / 2 - iconSize / 2;
                        int centerY = rect.y() + rect.height() / 2 - iconSize / 2;
                        painter.drawPixmap(centerX, centerY, p);
                    } else {
                        painter.drawPixmap(x, rect.y(), p);
                    }
                    x += p.width();
                    x += space;
                }

                final String text = decoration.getText();
                if (text != null) {
                    if (metricData.getTextFont() != null) {
                        painter.setFont(metricData.getTextFont());
                    }
                    final int textWidth = painter.fontMetrics().width(text);
                    final QRect textRect = new QRect(x, rect.y(), rect.width()/*textWidth*/, rect.height());
                    painter.drawText(textRect, metricData.getTextAlignment().value(), text);
                    x += textWidth;
                }
                x += 2 * space;
            }
        }

        painter.restore();
    }

    private void paintHtml(QPainter painter, QStyleOptionViewItem option, QModelIndex index) {
        QStyleOptionViewItemV4 optionV4 = (QStyleOptionViewItemV4) option;

        //boolean isInFocus=(option.state().value() & QStyle.StateFlag.State_HasFocus.value())>0;
        //prevent draw focus
        State state = optionV4.state();
        int val = option.state().value() & (~QStyle.StateFlag.State_HasFocus.value());
        state.setValue(val);
        optionV4.setState(state);

        initStyleOption(optionV4, index);
        document.setDefaultFont(option.font());
        document.setDocumentMargin(2);
        document.setHtml(optionV4.text());

        optionV4.setText("");
        QStyle style = optionV4.widget() != null ? optionV4.widget().style() : QApplication.style();
        style.drawControl(QStyle.ControlElement.CE_ItemViewItem, optionV4, painter);

        //highlight selected item text
        QAbstractTextDocumentLayout_PaintContext ctx = new QAbstractTextDocumentLayout_PaintContext();
        QPalette pal = optionV4.palette();
        if (/*
                 * isInFocus &&
                 */(optionV4.state().value() & QStyle.StateFlag.State_Selected.value()) > 0) {
            QPalette.ColorGroup colorGroup = ((option.state().value() & QStyle.StateFlag.State_Active.value()) > 0)
                    ? QPalette.ColorGroup.Active : QPalette.ColorGroup.Inactive;

            QColor color = optionV4.palette().color(colorGroup, QPalette.ColorRole.HighlightedText);
            pal.setColor(colorGroup, QPalette.ColorRole.Text, color);
        }
        ctx.setPalette(pal);

        painter.save();
        QRect textRect = style.subElementRect(QStyle.SubElement.SE_ItemViewItemText, optionV4);
        painter.setClipRect(textRect);
        painter.translate(textRect.topLeft());
        document.documentLayout().draw(painter, ctx);
        painter.restore();
    }
}

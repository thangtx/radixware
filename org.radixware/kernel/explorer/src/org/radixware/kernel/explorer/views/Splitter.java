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

package org.radixware.kernel.explorer.views;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBoxLayout;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QSplitter;
import com.trolltech.qt.gui.QSplitterHandle;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.widgets.ISplitter;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public class Splitter extends QSplitter implements ISplitter {

    private int curPos = -1;
    private int savePos = -1;
    final private String configPrefix;
    final private ClientSettings settings;
    final private double initialRatio;
    private List<SplitterCollapser> collapsers;

    private static class MoveHandlerEvent extends QEvent {

        final private int position;

        public MoveHandlerEvent(final int pos) {
            super(QEvent.Type.User);
            position = pos;
        }

        public int getPosition() {
            return position;
        }
    }

    private static class SetRatioEvent extends QEvent {

        final private double ratio;

        public SetRatioEvent(final double newRatio) {
            super(QEvent.Type.User);
            ratio = newRatio;
        }

        public double getRatio() {
            return ratio;
        }
    }

    private static class RestoreRatioEvent extends QEvent {

        public RestoreRatioEvent() {
            super(QEvent.Type.User);
        }
    }

    private static class Handle extends QSplitterHandle {

        final private QFrame frame = new QFrame(this);

        public Handle(Qt.Orientation orientation, QSplitter parent) {
            super(orientation, parent);

            final QBoxLayout layout;
            if (orientation == Qt.Orientation.Horizontal) {
                layout = new QHBoxLayout(this);
                //             frame.setMaximumHeight(2);
            } else {
                layout = new QVBoxLayout(this);
//                frame.setMaximumWidth(2);
            }
            setLayout(layout);
            frame.setFrameShape(QFrame.Shape.Box);
            frame.setFrameStyle(QFrame.Shadow.Raised.value());
            layout().addWidget(frame);
        }
        private boolean underCursor;

        @Override
        protected void enterEvent(QEvent event) {
            super.enterEvent(event);
            if (!underCursor) {
                underCursor = true;
                repaint();
            }
        }

        @Override
        protected void leaveEvent(QEvent arg__1) {
            underCursor = false;
            super.leaveEvent(arg__1);
            repaint();
        }

        @Override
        protected void paintEvent(QPaintEvent event) {
            final QPainter painter = new QPainter(this);
            painter.save();
            // hover appearance
            QBrush fillColor = palette().window();
            if (underCursor && isEnabled()) {
                fillColor = palette().toolTipBase();
            }

            painter.fillRect(event.rect(), fillColor);

            QColor grooveColor = WidgetUtils.mergedColors(palette().color(QPalette.ColorRole.Dark).lighter(110), palette().button().color(), 40);
            QColor gripShadow = grooveColor.darker(110);
            QPalette palette = palette();
            boolean vertical = orientation() != Qt.Orientation.Horizontal;
            QRect scrollBarSlider = event.rect();
            int gripMargin = 4;
            //draw grips
            if (vertical) {
                for (int i = -20; i < 20; i += 2) {
                    painter.setPen(new QPen(gripShadow, 1));
                    painter.drawLine(
                            new QPoint(scrollBarSlider.center().x() + i,
                            scrollBarSlider.top() + gripMargin),
                            new QPoint(scrollBarSlider.center().x() + i,
                            scrollBarSlider.bottom() - gripMargin));
                    painter.setPen(new QPen(palette.light(), 1));
                    painter.drawLine(
                            new QPoint(scrollBarSlider.center().x() + i + 1,
                            scrollBarSlider.top() + gripMargin),
                            new QPoint(scrollBarSlider.center().x() + i + 1,
                            scrollBarSlider.bottom() - gripMargin));
                }
            } else {
                for (int i = -20; i < 20; i += 2) {
                    painter.setPen(new QPen(gripShadow, 1));
                    painter.drawLine(
                            new QPoint(scrollBarSlider.left() + gripMargin,
                            scrollBarSlider.center().y() + i),
                            new QPoint(scrollBarSlider.right() - gripMargin,
                            scrollBarSlider.center().y() + i));
                    painter.setPen(new QPen(palette.light(), 1));
                    painter.drawLine(
                            new QPoint(scrollBarSlider.left() + gripMargin,
                            scrollBarSlider.center().y() + 1 + i),
                            new QPoint(scrollBarSlider.right() - gripMargin,
                            scrollBarSlider.center().y() + 1 + i));

                }
            }
            painter.restore();
        }
    }

    public Splitter(final QWidget parent, final ClientSettings settings, final String cfgPrefix, final double initialRatio) {
        super(parent);
        this.settings = settings;
        this.setHandleWidth(5);
        this.splitterMoved.connect(this, "wasMoved(int,int)");
        configPrefix = cfgPrefix;
        this.initialRatio = initialRatio;
        if (configPrefix != null && !configPrefix.isEmpty()) {
            QApplication.postEvent(this, new RestoreRatioEvent());
        }
    }

    public Splitter(final QWidget parent, final ClientSettings settings) {
        this(parent, settings, null, -1);
    }

    @SuppressWarnings("unused")
    private void wasMoved(int pos, int i) {
        //curPos = pos;
        if (handle(1) != null) {
            if (orientation().equals(com.trolltech.qt.core.Qt.Orientation.Vertical)) {
                curPos = handle(1).y();
            } else {
                curPos = handle(1).x();
            }
        }
    }

    public void moveToPosition(int pos) {
        moveSplitter(pos, 1);
    }

    public void moveToPositionLater(int pos) {
        QApplication.postEvent(this, new MoveHandlerEvent(pos));
    }

    public void setRatio(double ratio) {
        QApplication.postEvent(this, new SetRatioEvent(ratio));
    }

    @Override
    public int getCurrentPosition() {
        return curPos;
    }

    @Override
    public void saveCurrentPosition() {
        savePos = curPos;
    }

    @Override
    public void restorePosition() {
        if (savePos > 0 && curPos != savePos) {
            curPos = savePos;
            moveToPosition(savePos);
        }
    }

    public void saveRatioToConfig(final String key) {
        if (settings==null){
            throw new IllegalUsageError("Config store is undefined");
        }             
        final double ratio =
                (double) getCurrentPosition() / (orientation() == Qt.Orientation.Vertical ? height() : width());
        settings.writeDouble(key, ratio);
    }
    
    public void saveRatioToConfig() {
        final StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(configPrefix);
        keyBuilder.append("/");
        keyBuilder.append(SettingNames.SYSTEM);
        if (objectName() != null && !objectName().isEmpty()) {
            keyBuilder.append("/");
            keyBuilder.append(objectName());
        }
        keyBuilder.append("/splitterRatio");        
        saveRatioToConfig(keyBuilder.toString());
    }    

    public void restoreRatioFromConfig(final String key, final double defaultRatio) {
        setRatio(settings.readDouble(key, defaultRatio));
    }

    public void updatePosition() {
        if (orientation().equals(com.trolltech.qt.core.Qt.Orientation.Vertical)) {
            curPos = height() - widget(1).height() - handleWidth();
        } else {
            curPos = width() - widget(1).width() - handleWidth();
        }
    }

    @Override
    protected void customEvent(final QEvent customEvent) {
        if (isVisible() && customEvent instanceof SetRatioEvent) {
            final double ratio = ((SetRatioEvent) customEvent).getRatio();
            if (0 <= ratio && ratio <= 1) {
                final int pos;
                if (orientation() == Qt.Orientation.Vertical) {
                    pos = (int) (height() * ratio);
                } else {
                    pos = (int) (width() * ratio);
                }
                moveToPosition(pos);
            }
        } else if (customEvent instanceof MoveHandlerEvent) {
            final MoveHandlerEvent event = (MoveHandlerEvent) customEvent;
            moveToPosition(event.getPosition());
        } else if (customEvent instanceof RestoreRatioEvent) {
            final StringBuilder keyBuilder = new StringBuilder();
            keyBuilder.append(configPrefix);
            keyBuilder.append("/");
            keyBuilder.append(SettingNames.SYSTEM);
            if (objectName() != null && !objectName().isEmpty()) {
                keyBuilder.append("/");
                keyBuilder.append(objectName());
            }
            keyBuilder.append("/splitterRatio");
            restoreRatioFromConfig(keyBuilder.toString(), initialRatio);
        }
        super.customEvent(customEvent);
    }

    @Override
    public boolean isCollapsed(int idx) {
        if (orientation().equals(com.trolltech.qt.core.Qt.Orientation.Vertical)) {
            return curPos + handleWidth() >= height();
        } //return widget(idx).height()==0;
        else {
            return curPos + handleWidth() >= width();
        }
        //return widget(idx).width()==0;
    }

    @Override
    public void collapse(int idx) {        
        if (isCollapsible(idx)) {
            blockSignals(true);
            try {
                if (idx == 0) {
                    moveSplitter(0, 1);
                } else {
                    final int difference;

                    if (orientation().equals(com.trolltech.qt.core.Qt.Orientation.Vertical)) {
                        difference = parentWidget().height();
                    } else {
                        difference = parentWidget().width();
                    }

                    curPos = difference - handleWidth();
                    moveSplitter(difference, 1);

                }
            } finally {
                blockSignals(false);
            }
        }
    }

    @Override
    protected QSplitterHandle createHandle() {
        return new Handle(orientation(), this);
    }
    
    public final SplitterCollapser addCollapser(final int index){
        if (collapsers==null){
            collapsers = new LinkedList<>();
        }else{
            for (SplitterCollapser collapser: collapsers){
                if (collapser.getIndex()==index){
                    return collapser;
                }
            }
        }
        final SplitterCollapser collapser = new SplitterCollapser(this, index);
        collapsers.add(collapser);
        return collapser;
    }
    
    public final SplitterCollapser getCollapser(final int index){
        if (collapsers!=null){
            for (SplitterCollapser collapser: collapsers){
                if (collapser.getIndex()==index){
                    return collapser;
                }
            }            
        }
        return null;
    }
    
    public final void removeCollapser(final int index){
        removeCollapser(getCollapser(index));
    }
    
    public final void removeCollapser(final SplitterCollapser collapser){
        if (collapser!=null && collapsers!=null && collapsers.contains(collapser)){
            collapser.setParent(null);
            collapser.close();
            collapser.hide();
            collapsers.remove(collapser);
            if (collapsers.isEmpty()){
                collapsers = null;
            }            
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (collapsers!=null){
            for (int i =collapsers.size()-1; i>=0; i--){
                removeCollapser(collapsers.get(i));
            }
        }
        super.closeEvent(event);
    }
    
    
    
}

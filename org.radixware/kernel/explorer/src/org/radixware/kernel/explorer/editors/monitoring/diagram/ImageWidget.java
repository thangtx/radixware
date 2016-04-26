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

package org.radixware.kernel.explorer.editors.monitoring.diagram;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPixmap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;


public class ImageWidget extends QLabel {

    /* @Override
     public boolean event(QEvent qevent) {
     if(qevent.type()==QEvent.Type.Wheel){
     QWheelEvent scrollEvent=(QWheelEvent)qevent;
     int numSteps=-1;
     if(scrollEvent.delta()<0)
     numSteps=1;

     parent.resizeValueRange(numSteps);
     }else if(qevent.type()==QEvent.Type.MouseButtonDblClick){
     parent.setAutoValueRange();
     }
            
     return super.event(qevent);
        
     }*/
    private static final class ImageBuffer extends OutputStream {

        public ImageBuffer() {
            super();
        }
        private final QByteArray memoryBuffer = new QByteArray();

        @Override
        public void write(final int b) throws IOException {
            memoryBuffer.append((byte) b);
        }

        public QPixmap getQPixmap() {
            final QPixmap pixmap = new QPixmap();
            pixmap.loadFromData(memoryBuffer, "PNG");
            return pixmap;
        }
    }
    private final DiagramWidget parent;

    public ImageWidget(final DiagramWidget parent, final BufferedImage image) throws IOException {
        super(parent);
        this.parent = parent;
        setMargin(0);
        setContentsMargins(0, 0, 0, 0);
        setAlignment(AlignmentFlag.AlignRight);
        setPixmap(getQPixmap(image));
    }

    public void setImage(final BufferedImage image) throws IOException {
        setPixmap(getQPixmap(image));
    }

    private static QPixmap getQPixmap(final BufferedImage awtImage) throws IOException {
        final ImageBuffer buffer = new ImageBuffer();
        ImageIO.write(awtImage, "PNG", buffer);
        return buffer.getQPixmap();
    }
}
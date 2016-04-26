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

package org.radixware.kernel.explorer.inspector;

import com.compassplus.schemas.inspector.ObjectInfo;
import com.compassplus.schemas.inspectorWsdl.ObjectInfoRqDocument;
import com.compassplus.schemas.inspectorWsdl.ObjectInfoRsDocument;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;


public class InspectorImplementation {

    public InspectorImplementation() {
    }

    public ObjectInfoRsDocument getObjectInfo(final ObjectInfoRqDocument rq) {
        final ObjectInfoRsDocument rs = ObjectInfoRsDocument.Factory.newInstance();
        rs.addNewObjectInfoRs();
        if (rq != null && rq.getObjectInfoRq() != null) {
            if (rq.getObjectInfoRq().isSetPosition()) {
                QApplication.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        final QPoint globalPoint = new QPoint(rq.getObjectInfoRq().getPosition().getAbsoluteX().intValue(), rq.getObjectInfoRq().getPosition().getAbsoluteY().intValue());
                        for (QWidget widget : QApplication.allWidgets()) {
                            if (widget.parentWidget()!= null && widget.isVisible() && widget.childAt(widget.mapFromGlobal(globalPoint)) == null && widget.geometry().contains(widget.parentWidget().mapFromGlobal(globalPoint))) {
                                fillRs(rs, widget);
                                break;
                            }
                        }
                    }
                });
            }
        }
        return rs;
    }

    private void fillRs(ObjectInfoRsDocument rsDoc, QWidget widget) {
        final ObjectInfo info = rsDoc.ensureObjectInfoRs().ensureObjectInfo();

        info.setId(widget.getClass().getSimpleName());

        info.setWidth(BigInteger.valueOf(widget.size().width()));
        info.setHeight(BigInteger.valueOf(widget.size().height()));

        final QPoint globalTopLeft = widget.mapToGlobal(new QPoint(0, 0));

        info.setAbsoluteTopLeftX(BigInteger.valueOf(globalTopLeft.x()));
        info.setAbsoluteTopLeftY(BigInteger.valueOf(globalTopLeft.y()));
    }
}

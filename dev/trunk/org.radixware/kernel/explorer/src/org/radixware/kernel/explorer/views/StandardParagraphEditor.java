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

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.DefinitionError;

public final class StandardParagraphEditor extends ParagraphEditor {

    private QIcon icon = null;

    public StandardParagraphEditor(IClientEnvironment environment) {
        super(environment);
    }

    @Override
    public void open(Model model_) {
        super.open(model_);
        try {
            icon = (QIcon) getParagraphModel().getParagraphDef().getLogo();            
        } catch (DefinitionError err) {
            final String msg = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot load logo for paragraph %s");
            getEnvironment().getTracer().error(String.format(msg, getParagraphModel().getDefinition().toString()), err);
            getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, getParagraphModel().getDefinition().toString(), err.getMessage()) + "\n" + err.getMessage(),
                    EEventSource.EXPLORER);
        }
        opened.emit(this);
    }

    @Override
    protected void paintEvent(QPaintEvent event) {
        super.paintEvent(event);
        if (icon != null) {
            final QPainter painter = new QPainter();
            final Alignment alignment = new Alignment();
            alignment.set(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter);
            painter.begin(this);
            icon.paint(painter, this.rect(), alignment);
            painter.end();
        }
    }
}

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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import org.radixware.kernel.common.client.env.ClientSettings;


public interface IExplorerSettings extends ClientSettings{

    /////////////////////write methods
    void writeQFont(final String key, final QFont font);
    void writeQPoint(final String key, final QPoint point);
    void writeQAlignmentFlag(final String key, final Qt.AlignmentFlag alignmentFlag);
    void writeQSize(final String key, final QSize size);
    void writeQColor(final String key, final QColor color);
    void writeQByteArray(final String key, final QByteArray array);

    /////////////////////read methods
    QFont readQFont(final String key);
    QPoint readQPoint(final String key);
    Qt.AlignmentFlag readAlignmentFlag(final String key);
    QSize readQSize(final String key);
    QColor readQColor(final String key);
    QByteArray readQByteArray(final String key);
}

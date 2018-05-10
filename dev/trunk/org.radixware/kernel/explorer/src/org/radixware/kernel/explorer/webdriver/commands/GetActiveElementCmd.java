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
package org.radixware.kernel.explorer.webdriver.commands;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public class GetActiveElementCmd extends WindowCmd {

    @Override
    public JSONObject execute(WebDrvSession session, WebDrvUri uri, JSONObject parameter) throws WebDrvException {
        QWidget w = QApplication.focusWidget();

        if (w == null) {
            throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_ELEMENT);
        }

        if (w instanceof QAbstractItemView) {
            QAbstractItemView aiv = (QAbstractItemView) w;
            QModelIndex i = aiv.currentIndex();
            if (i == null) {
                throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_ELEMENT);
            }
            UIElementReference re = session.getUIElements().getCellReference(aiv, i, false);
            return re.asJson();
        }
        return session.getUIElements().getWidgetReference(w).asJson();
    }

    @Override
    public String getName() {
        return "active";
    }

    @Override
    public String getGroupName() {
        return "element";
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.GET;
    }
}

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

import com.trolltech.qt.core.QBuffer;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QPixmap;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.explorer.webdriver.WebDriverUserPromptManager;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionCommandResult;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;

abstract class GuiCmd implements IWebDrvSessionCommand{

    @Override
    public final boolean isGuiCommand() {
        return true;
    }    

    @Override
    public boolean isInputActionCommand() {
        return false;
    }

    @Override
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        WebDriverUserPromptManager.getInstance().handleUserPrompt(session.getCapabilities().getUnhandledPromptBehaviour());
        return null;
    }
	
	@SuppressWarnings("unchecked")
	protected static JSONObject qRectToJSON(QRect qrect) {
        final JSONObject jsonRect = new JSONObject();
        if (!qrect.isValid() || qrect.isNull() || qrect.isEmpty()){
            jsonRect.put("x", 0);
            jsonRect.put("y", 0);
            jsonRect.put("width", 0);
            jsonRect.put("height", 0);
        }else{
            jsonRect.put("x", qrect.x());
            jsonRect.put("y", qrect.y());
            jsonRect.put("width", qrect.width());
            jsonRect.put("height", qrect.height());
        }
		return jsonRect;
	}
	
	protected static JSONObject qPixmapToJSON(QPixmap pixmap) {
        final QBuffer imageBuffer = new QBuffer();
        pixmap.save(imageBuffer, "png");
        final QByteArray imageData = imageBuffer.data();
        final byte[] pngData = new byte[imageData.length()];
        for (int i=pngData.length-1; i>=0; i--){
            pngData[i] = imageData.at(i);
        }
        imageBuffer.dispose();
        return new WebDrvSessionCommandResult(Base64.encode(pngData));
	}
}

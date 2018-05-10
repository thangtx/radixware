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

package org.radixware.kernel.explorer.webdriver.input;

import com.trolltech.qt.core.Qt;
import org.radixware.kernel.explorer.webdriver.WebDrvServer;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class PointerDownAction extends PointerButtonAction {
    
    public PointerDownAction(final PointerInputSource inputSource, final int button){
        super(inputSource, button);
    }

    @Override
    public void dispatch(final WebDrvSession session, int tickDuration, long tickStart) throws WebDrvException {
        final PointerInputSourceState state = getInputSource().getState();
        Qt.MouseButton btn = getButton();
        if (btn!=null && !state.isPressed(getButton())){
            WebDrvServer.postGlobalMouseMove(state.getX(), state.getY());
            WebDrvServer.postMousePress(btn);
            state.setButton(btn);
            session.getInputSourceManager().addCancelAction(new PointerUpAction(getInputSource(), btn.value()));
        }
    }
}

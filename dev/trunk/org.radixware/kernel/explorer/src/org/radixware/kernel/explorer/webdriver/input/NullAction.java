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

import org.radixware.kernel.explorer.webdriver.WebDrvSession;

final class NullAction<T extends InputSource> extends InputAction<T> implements IPauseAction{
    
    private final int duration;
    
    public NullAction(final T inputSource, final Integer duration){
        super(inputSource);
        this.duration = duration==null ? 0 : duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void dispatch(final WebDrvSession session, int tickDuration, long tickStart) {        
    }
}

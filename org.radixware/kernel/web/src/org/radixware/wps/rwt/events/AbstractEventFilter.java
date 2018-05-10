/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.wps.rwt.events;

public abstract class AbstractEventFilter<T extends AbstractEventFilter> {
    private final EHtmlEventType type;
    public EHtmlEventType getType() {
        return type;
    }
    
    public AbstractEventFilter(EHtmlEventType type) {
        this.type = type;
    }
    public abstract String toJSONString();
    public abstract boolean merge(T eventFilter);
    public abstract boolean unmerge(T eventFilter);
}

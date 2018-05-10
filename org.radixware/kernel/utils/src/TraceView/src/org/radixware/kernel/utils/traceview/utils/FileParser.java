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
package org.radixware.kernel.utils.traceview.utils;

import org.radixware.kernel.utils.traceview.window.TablePanel;

public class FileParser implements Runnable {

    private final TablePanel tab;
    private final ContextForParsing context;

    public FileParser(TablePanel tab, ContextForParsing context) {
        this.tab = tab;
        this.context = context;
    }

    @Override
    public void run() {
        context.parseFile();
        tab.setData(context.getData());
    }
}

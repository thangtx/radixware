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

package org.radixware.kernel.server.trace;

import java.util.ResourceBundle;



final class Messages {
	private Messages(){};

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.trace.mess.messages");

        FILE_TRACE_OPTIONS_CHANGED = bundle.getString("FILE_TRACE_OPTIONS_CHANGED");
        GUI_TRACE_PROFILE_OVERRIDEN = bundle.getString("GUI_TRACE_PROFILE_OVERRIDEN");
    }
    static final String FILE_TRACE_OPTIONS_CHANGED;
    static final String GUI_TRACE_PROFILE_OVERRIDEN;
    static final String MLS_ID_GUI_TRACE_PROFILE_OVERRIDEN = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsMJBDFXXIX5DMRFJTSOCE7ADYKQ";// MLS GUI trace profile is localy overriden: "%1"
}

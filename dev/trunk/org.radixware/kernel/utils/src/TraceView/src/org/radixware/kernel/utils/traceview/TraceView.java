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
package org.radixware.kernel.utils.traceview;

import org.radixware.kernel.utils.traceview.utils.ParseArguments;
import org.radixware.kernel.utils.traceview.utils.TraceViewUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.radixware.kernel.utils.traceview.console.ConsoleMode;
import org.radixware.kernel.utils.traceview.window.WindowMode;

public class TraceView {
    
    private static final Logger logger = Logger.getLogger(TraceView.class.getName());
    
    public static void main(final String[] args) {
        final ParseArguments arguments = new ParseArguments(args);
        if(arguments.getOperatingMode() == ParseArguments.EResultCode.WINDOW) {
            SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        WindowMode window = new WindowMode(arguments);
                    }
                });
        } else if(arguments.getOperatingMode() == ParseArguments.EResultCode.CONSOLE) {
            ConsoleMode console = new ConsoleMode(arguments);
        } else if(arguments.getOperatingMode() == ParseArguments.EResultCode.HELP) {
            TraceViewUtils.printHelp(arguments.getOptions());
        } else {
            logger.log(Level.SEVERE, "Error on parse arguments:{0}", arguments.getParseException());
        }
    }

}





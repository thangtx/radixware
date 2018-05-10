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
package org.radixware.kernel.designer.ads.common.radixdoc;

import java.awt.Color;
import java.io.IOException;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.radixdoc.IDocLogger;
import org.radixware.kernel.designer.common.dialogs.utils.InputOutputPrinter;

public class DocLogger implements IDocLogger {

    private final InputOutputPrinter ioPrinter = new InputOutputPrinter("Export XML Schemas", false);

    public DocLogger() {        
        try {
            ioPrinter.reset();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void put(EEventSeverity severity, String message) {
        try {
            ioPrinter.select();           

            switch (severity) {
                case WARNING:
                    ioPrinter.printlnWarning(message);
                    break;
                case ERROR:
                case ALARM:
                    ioPrinter.printlnError(message);
                    break;
                case EVENT:                    
                    ioPrinter.println(message, Color.GREEN.brighter());
                    break;
                default:
                    ioPrinter.println(message);
            }           
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
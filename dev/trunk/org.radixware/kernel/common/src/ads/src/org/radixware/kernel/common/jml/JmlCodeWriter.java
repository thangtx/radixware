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

package org.radixware.kernel.common.jml;

import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;


import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.CommentsAnalizer;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.scml.Scml.Text;
import org.radixware.kernel.common.scml.ScmlProcessor;


class JmlCodeWriter extends CodeWriter {

    public static final char[] IL_MARKER = "/*$il$*/".toCharArray();
    public static final char[] IL_MARKER_TEXT = "$il$".toCharArray();

    private class JmlCodeProcessor extends ScmlProcessor {

        private CodePrinter printer;
        private int itemIdx = 0;

        JmlCodeProcessor(CodePrinter printer) {
            this.printer = printer;
        }

        @Override
        protected void processTag(Tag tag) {
            if (tag instanceof Jml.Tag) {
                WriterUtils.enterHumanUnreadableBlock(printer);
                printer.print(Jml.Tag.TAG_LEAD_MARKER);
                printer.print(itemIdx);
                printer.print(Jml.Tag.TAG_MARKER_TAIL);
                WriterUtils.leaveHumanUnreadableBlock(printer);
                writeCode(printer, (Jml.Tag) tag, jml.getOwnerDef());
                WriterUtils.enterHumanUnreadableBlock(printer);
                printer.print(Jml.Tag.TAG_TAIL_MARKER);
                printer.print(itemIdx);
                printer.print(Jml.Tag.TAG_MARKER_TAIL);
                WriterUtils.leaveHumanUnreadableBlock(printer);
                itemIdx++;
            } else {
                printer.printError();
            }
        }

        @Override
        protected void processTagInComment(Tag tag) {
            itemIdx++;
        }

        @Override
        protected void processText(Text text) {
            final CodePrinter.Monitor m = text.getSourceMonitor();
            if (m != null) {
                printer.activateMonitor(m);
            }
//            String[] strings = text.getText().split("\n");
//
//            for (int i = 0; i < strings.length; i++) {
//                printer.print(strings[i]);
//                if (i < strings.length - 1) {
//                    printer.println();
//                    printer.print(IL_MARKER);
//                }
//            }
            final RadixObjectLocator locator = (RadixObjectLocator) printer.getProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME);
            if (locator != null) {
                final RadixObjectLocator.RadixObjectData data = locator.start(text);
                printer.print(text.getText());
                data.commit();
            } else {
                printer.print(text.getText());
            }

            itemIdx++;
        }

        @Override
        protected CommentsAnalizer getCommentsAnalizer() {
            return CommentsAnalizer.Factory.newJavaCommentsAnalizer();
        }
    }
    private Jml jml;

    public JmlCodeWriter(JavaSourceSupport support, Jml jml, UsagePurpose purpose) {
        super(support, purpose);
        this.jml = jml;
    }

    public Jml getJml() {
        return jml;
    }

    @Override
    public boolean writeCode(CodePrinter printer) {
        WriterUtils.enterHumanUnreadableBlock(printer);
        printer.print(Jml.Tag.TAG_LEAD_MARKER);
        printer.print(-1);
        printer.print(Jml.Tag.TAG_MARKER_TAIL);
        printer.print(Jml.Tag.TAG_TAIL_MARKER);
        printer.print(-1);
        printer.print(Jml.Tag.TAG_MARKER_TAIL);
       WriterUtils.leaveHumanUnreadableBlock(printer);
        new JmlCodeProcessor(printer).process(jml);
        return true;
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        //do not use
    }
}

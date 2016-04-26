/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.support.OffsetsBag;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;

/**
 *
 * @author akrylov
 */
public class MarkOccurrencesHighlighter implements CaretListener {

    private static final AttributeSet defaultColors
            = AttributesUtilities.createImmutable(StyleConstants.Background,
                    new Color(236, 235, 163));
    private final OffsetsBag bag;
    private JTextComponent comp;

    public MarkOccurrencesHighlighter(HighlightsLayerFactory.Context context) {
        bag = new OffsetsBag(context.getDocument());
        comp = context.getComponent();
        comp.addCaretListener(this);
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        bag.clear();
        scheduleUpdate();
    }
    private RequestProcessor.Task task = null;
    private final static int DELAY = 1000;

    private void computeHighlite() {
        String selection = comp.getSelectedText();
        char[] text = comp.getText() == null ? new char[0] : comp.getText().replace("\r", "").toCharArray();
        if (selection == null) {

            int pos = comp.getCaret().getDot();

            if (pos >= 0 && pos < text.length) {
                int from = 0, to = -1;
                for (int start = pos - 1; start >= 0; start--) {
                    if (!Character.isJavaIdentifierPart(text[start])) {
                        from = start + 1;
                        break;
                    }
                }

                for (int start = from; start < text.length; start++) {
                    if (!Character.isJavaIdentifierPart(text[start])) {
                        to = start;
                        break;
                    }
                }
                if (from >= 0 && to > from) {
                    selection = String.valueOf(text, from, to - from);
                }
            }
        }
        if (selection != null) {
            int start = -1;
            int end = -1;
            for (int i = 0; i < text.length; i++) {
                char c = text[i];
                if (Character.isJavaIdentifierStart(c)) {
                    if (start < 0) {
                        start = i;
                    }
                } else {
                    if (Character.isJavaIdentifierPart(c)) {
                        if (start >= 0) {
                            continue;
                        }
                    }
                    if (start >= 0) {
                        end = i - 1;
                        int len = end - start + 1;
                        String token = String.valueOf(text, start, len);
                        if (token.equals(selection)) {
                            bag.addHighlight(start, start + len, defaultColors);
                        }
                        start = -1;
                    }
                }
            }

        }
    }
    private static MarkOccurrencesHighlighter activeWorker = null;
    private static final Object MUTEX = new Object();
    private static boolean started = false;

    private static void schedule(MarkOccurrencesHighlighter worker) {
        synchronized (MUTEX) {
            if (!started) {
                started = true;
                org.radixware.kernel.common.utils.RequestProcessor.submit(new Runnable() {

                    @Override
                    public void run() {
                        for (;;) {
                            MarkOccurrencesHighlighter worker = null;
                            synchronized (MUTEX) {
                                if (activeWorker == null) {
                                    try {
                                        MUTEX.wait();
                                    } catch (InterruptedException ex) {
                                        started = false;
                                        return;
                                    }
                                } else {
                                    worker = activeWorker;
                                }
                            }
                            if (worker != null) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    return;
                                }
                                synchronized (MUTEX) {
                                    if (activeWorker != worker) {
                                        continue;
                                    } else {
                                        activeWorker = null;
                                    }
                                }
                                worker.computeHighlite();
                            }
                        }
                    }
                });

            }
            activeWorker = worker;
            MUTEX.notifyAll();
        }
    }

    public void scheduleUpdate() {
        schedule(this);
    }

    public OffsetsBag getHighlightsBag() {
        return bag;
    }
}

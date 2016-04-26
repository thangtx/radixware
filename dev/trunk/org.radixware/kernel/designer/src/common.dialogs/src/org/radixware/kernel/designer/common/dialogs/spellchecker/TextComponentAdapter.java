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

package org.radixware.kernel.designer.common.dialogs.spellchecker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import org.radixware.kernel.designer.common.dialogs.spellchecker.Spellchecker.SpellcheckControl;
import org.radixware.kernel.utils.spellchecker.SimpleTokenizer;
import org.radixware.kernel.utils.spellchecker.Spellchecker;


class TextComponentAdapter implements PropertyChangeListener, DocumentListener, CaretListener, Runnable {

    private SpellcheckControl control;
    private Document document;
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    static TextComponentAdapter attach(SpellcheckControl control) {
        TextComponentAdapter adapter = new TextComponentAdapter(control);
        control.getTextComponent().putClientProperty(TextComponentAdapter.class, adapter);

        return adapter;
    }

    private TextComponentAdapter(SpellcheckControl control) {
        control.getTextComponent().addPropertyChangeListener(this);
        control.getTextComponent().addCaretListener(this);
        control.setUpdateCallback(this);
        this.control = control;
        document = control.getTextComponent().getDocument();
        document.addDocumentListener(this);
        documentUpdate();
    }

    @Override
    public void run() {
        documentUpdate();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (document != control.getTextComponent().getDocument()) {
            if (document != null) {
                document.removeDocumentListener(this);
            }
            document = control.getTextComponent().getDocument();
            document.addDocumentListener(this);
            doUpdateCurrentVisibleSpan();
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        documentUpdate();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        documentUpdate();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        documentUpdate();
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        //do nothing, until hint is not supported
    }

    private void doUpdateCurrentVisibleSpan() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                updateCurrentVisibleSpan();
                reschedule();
            }
        });
    }

    private void updateCurrentVisibleSpan() {
    }

    private void documentUpdate() {
        doUpdateCurrentVisibleSpan();
        cancel();
    }
    private final AtomicBoolean cancel = new AtomicBoolean();

    private void cancel() {
        cancel.set(true);
    }

    private void resume() {
        cancel.set(false);
    }

    private boolean isCancelled() {
        return cancel.get();
    }
    private Runnable checker = new Runnable() {

        @Override
        public void run() {
            process();
        }
    };

    private void reschedule() {
        cancel();
        executor.schedule(checker, 100, TimeUnit.MILLISECONDS);
    }

    private void process() {
        final List<SimpleTokenizer.Token> err = new LinkedList<>();
        try {
            resume();
            if (control.isSpellcheckEnabled()) {
                SimpleTokenizer tokenizer = new SimpleTokenizer(control.getTextComponent().getText());
                List<SimpleTokenizer.Token> tokens = tokenizer.tokenize();

                Spellchecker spellchecker = Spellchecker.getInstance(control.getLanguage(), control.getContext());
                for (SimpleTokenizer.Token t : tokens) {
                    final SimpleTokenizer.Token token = t;
                    if (spellchecker.check(t.text) == Spellchecker.Validity.INVALID) {
                        document.render(new Runnable() {

                            @Override
                            public void run() {
                                if (!isCancelled()) {
                                    err.add(token);
                                }
                            }
                        });
                    }
                }
            }
        } finally {
            if (!isCancelled()) {

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        if (isCancelled()) {
                            return;//NOPMD
                        }
                        document.render(new Runnable() {

                            @Override
                            public void run() {
                                if (isCancelled()) {
                                    return;//NOPMD
                                }
                                try {
                                    Highlighter h = control.getTextComponent().getHighlighter();

                                    if (h != null) {
                                        List<Object> oldTags = (List<Object>) control.getTextComponent().getClientProperty(ErrorHighlightPainter.class);

                                        if (oldTags != null) {
                                            for (Object tag : oldTags) {
                                                h.removeHighlight(tag);
                                            }
                                        }

                                        List<Object> newTags = new LinkedList<>();
                                        for (SimpleTokenizer.Token current : err) {
                                            newTags.add(h.addHighlight(current.start, current.start + current.length, new ErrorHighlightPainter()));
                                        }

                                        control.getTextComponent().putClientProperty(ErrorHighlightPainter.class, newTags);
                                    }
                                } catch (BadLocationException e) {
                                    //e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    private class ErrorHighlightPainter implements Highlighter.HighlightPainter {

        private ErrorHighlightPainter() {
        }

        @Override
        public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
            g.setColor(Color.RED);

            try {
                Rectangle start = control.getTextComponent().modelToView(p0);
                Rectangle end = control.getTextComponent().modelToView(p1);

                if (start.x < 0) {
                    return;
                }

                int waveLength = end.x + end.width - start.x;
                if (waveLength > 0) {
                    int[] wf = {0, 0, -1, -1};
                    int[] xArray = new int[waveLength + 1];
                    int[] yArray = new int[waveLength + 1];

                    int yBase = start.y + start.height - 2;
                    for (int i = 0; i <= waveLength; i++) {
                        xArray[i] = start.x + i;
                        yArray[i] = yBase + wf[xArray[i] % 4];
                    }
                    g.drawPolyline(xArray, yArray, waveLength);
                }
            } catch (BadLocationException e) {
                //  e.printStackTrace();
            }
        }
    }

}

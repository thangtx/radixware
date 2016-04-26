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

package org.radixware.kernel.designer.common.operation;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.radixware.kernel.common.components.ICancellable;

import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public abstract class BaseOperation extends Thread implements Cancellable, ICancellable {

    private final String title;
    private volatile ProgressHandle progress = null;
    private volatile boolean cancelled = false;
    private volatile BufferedReader in;
    private volatile PrintWriter out;
    private volatile PrintWriter err;
    private volatile boolean success = false;
    private volatile InputOutput io = null;
    // 

    public BaseOperation(String title) {
        this.title = title;

    }

    public ProgressHandle getProgress() {
        return progress;
    }

    public InputOutput getIO() {
        return io;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public PrintWriter getErr() {
        return err;
    }

    public void addOut(Writer newOut) {
        if (out == null) {
            out = new PrintWriter(newOut);
        } else {
            out = new PrintWriter(new DuplexWriter(out, newOut));
        }
    }

    public void addErr(Writer newErr) {
        if (err == null) {
            err = new PrintWriter(newErr);
        } else {
            err = new PrintWriter(new DuplexWriter(err, newErr));
        }
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public void run() {
        try {
            execute();
        } finally {
            onFinished();
        }
    }

    protected void onFinished() {
    }

    private void printLn(String mess, Color color) {
        try {
            IOColorLines.println(io, getTime() + " " + mess, color);
        } catch (IOException ex) {
            DialogUtils.messageError(ex);
        }
    }

    public String getTitle() {
        return title;
    }

    public void execute() {
        success = false;

        try {


            io = IOProvider.getDefault().getIO(title, false);

            out = new PrintWriter(new TimeWriter(io.getOut()));
            err = new PrintWriter(new TimeWriter(io.getErr()));
            in = new BufferedReader(io.getIn());

            printLn("Operation '" + title + "' started.", Color.BLUE.darker());

            addOut(new OutputStreamWriter(System.out));
            addErr(new OutputStreamWriter(System.err));

            progress = ProgressHandleFactory.createHandle(title, this);
            try {
                progress.start();

                try {
                    if (process()) {
                        printLn("Operation '" + title + "' finished.", Color.GREEN.darker().darker());
                        success = true;
                    } else if (wasCancelled()) {
                        getErr().println("Operation '" + title + "' cancelled by user.");
                    }
                } catch (Exception ex) {
                    final String error = ExceptionTextFormatter.throwableToString(ex);
                    getErr().println(error);
                    getErr().println("Operation '" + title + "' finished with an error.");
                }
            } finally {
                progress.finish();
            }
        } finally {
            Logger.getLogger(getClass().getName()).log(Level.FINE, "");
        }
    }

    @Override
    public boolean cancel() {
        cancelled = true;
        return true;
    }

    /**
     * Operation (void process()) must see this method periodically to return.
     *
     * @return
     */
    @Override
    public boolean wasCancelled() {
        return cancelled;
    }

    /**
     * @return true if operation completed successfully, false otherwise.
     * @throws Exception
     */
    protected abstract boolean process() throws Exception;

    private class DuplexWriter extends Writer {

        private final Writer writer1;
        private final Writer writer2;

        public DuplexWriter(Writer writer1, Writer writer2) {
            this.writer1 = writer1;
            this.writer2 = writer2;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            writer1.write(cbuf, off, len);
            writer1.flush();
            writer2.write(cbuf, off, len);
            writer2.flush();
        }

        @Override
        public void flush() throws IOException {
            writer1.flush();
            writer2.flush();
        }

        @Override
        public void close() throws IOException {
            writer1.close();
            writer2.close();
        }
    }
    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

    public static String getTime() {
        return timeFormatter.format(new Date());
    }

    public class TimeWriter extends Writer {

        private boolean needPrintTime = true;
        private final Writer out;

        public TimeWriter(Writer out) {
            super(out);
            this.out = out;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            for (int i = 0; i < len; i++) {
                char c = cbuf[i + off];
                if (needPrintTime) {
                    needPrintTime = false;
                    out.write(getTime());
                    out.write(" ");
                }
                if (c == '\n') {
                    needPrintTime = true;
                }
                out.write(cbuf, i + off, 1);
            }
        }

        @Override
        public void flush() throws IOException {
            out.flush();
        }

        @Override
        public void close() throws IOException {
            out.close();
        }
    }
}

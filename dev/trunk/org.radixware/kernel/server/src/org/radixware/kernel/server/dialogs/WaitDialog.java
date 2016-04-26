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

/*
 * WaitDialog.java
 *
 * Created on Nov 18, 2010, 5:18:32 PM
 */
package org.radixware.kernel.server.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.exceptions.IllegalUsageError;


public class WaitDialog extends javax.swing.JDialog {

    private static final Logger logger = Logger.getLogger("org.radixware.kernerl.server.dialogs.WaitDialog");

    {
        logger.setLevel(Level.OFF);
    }

    private enum EReason {

        CANCEL_PRESSED,
        WORK_DONE;
    }
    private final Lock waitDialogLock = new ReentrantLock();
    private volatile boolean disposeRequested = false;
    private volatile boolean cancelPressed = false;
    private final JDialog owner;

    /** Creates new form WaitDialog */
    public WaitDialog(JDialog owner) {
        super(owner, true);
        initComponents();
        this.owner = owner;
    }

    private JButton getCancelButton() {
        return btCancel;
    }

    private boolean isCancelPressed() {
        return cancelPressed;
    }

    private void showIfNecessary() {

        waitDialogLock.lock();
        logger.info("Wait dialog is going to appear");
        if (!disposeRequested) {
            pack();
            setLocationRelativeTo(owner);
            addWindowListener(new WindowAdapter() {

                @Override
                public void windowOpened(WindowEvent e) {
                    logger.info("Wait dialog appeared, release lock");
                    waitDialogLock.unlock();
                }
            });
            setVisible(true);
        } else {
            logger.info("Task to close wait dialog is already submitted. Release lock without showing dialog");
            waitDialogLock.unlock();
        }
    }

    private void hideDialog(EReason reason) {
        waitDialogLock.lock();
        try {
            if (!disposeRequested) {
                if (SwingUtilities.isEventDispatchThread()) {
                    logger.info("Dispose dialog from AWT");
                    dispose();
                } else {
                    logger.info("Posting runnable to close the dialog.");
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            dispose();
                        }
                    });
                }
                disposeRequested = true;
                if (reason == EReason.CANCEL_PRESSED) {
                    cancelPressed = true;
                }
            } else {
                logger.info("Dialog is already closed, do nothing");
            }
        } finally {
            waitDialogLock.unlock();
        }
    }

    static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.dialogs.mess.messages");

            PLEASE_WAIT = bundle.getString("PLEASE_WAIT");

        }
        static final String PLEASE_WAIT;
    }

    /**
     * Calculate given callable and show wait dialog if calculation takes more then {@code  waitBeforeShowMillis}.
     * Should be called from AWT only
     */
    public static <T> T invokeAndShow(final Callable<T> callable, final long waitBeforeShowMillis, final JDialog owner) throws Exception {
        if(!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalUsageError("This method must be called only from AWT");
        }
        final Exception[] executionException = new Exception[1];
        executionException[0] = null;
        final Object[] result = new Object[1];
        result[0] = null;
        final WaitDialog waitDialog = new WaitDialog(owner);
        waitDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    logger.info("Running task...");
                    Object res = callable.call();
                    logger.info("Task completed");
                    if (Thread.currentThread().isInterrupted()) {
                        logger.info("Thread is interrupted, return from runnable");
                        return;
                    }
                    result[0] = res;
                } catch (Exception ex) {
                    logger.info("Excpetion during task execution");
                    executionException[0] = ex;
                }
                logger.info("Close dialog, because task is finished");
                waitDialog.hideDialog(EReason.WORK_DONE);
            }
        };
        final Thread executionThread = new Thread(runnable);

        waitDialog.getCancelButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                executionThread.interrupt();
                waitDialog.hideDialog(EReason.CANCEL_PRESSED);
            }
        });
        executionThread.start();
        if (waitBeforeShowMillis > 0) {
            try {
                executionThread.join(waitBeforeShowMillis);
            } catch (InterruptedException ex) {
                throw new Exception(ex);//NOPMD
            }
        }

        waitDialog.showIfNecessary();

        if (waitDialog.isCancelPressed()) {
            return null;
        }
        if (executionException[0] != null) {
            throw executionException[0];
        }

        return (T) result[0];

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText(Messages.PLEASE_WAIT);

        btCancel.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(btCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addComponent(btCancel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}

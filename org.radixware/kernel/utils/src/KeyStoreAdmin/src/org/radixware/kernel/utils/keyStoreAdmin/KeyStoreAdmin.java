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
 * KeyStoreAdmin.java
 */

package org.radixware.kernel.utils.keyStoreAdmin;

import java.util.EventObject;
import java.util.concurrent.CountDownLatch;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class KeyStoreAdmin extends SingleFrameApplication {

    private enum OpenMode {DEFAULT, OPEN_FILE};

    private static String path;
    private static char[] password;
    private static OpenMode openMode = OpenMode.DEFAULT;
    private static final CountDownLatch returnFromMainLatch = new CountDownLatch(1);
    private static boolean ignoreExit;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        Thread.currentThread().setContextClassLoader(KeyStoreAdmin.class.getClassLoader());
        switch (openMode){
            case OPEN_FILE:
                show(new KeyStoreAdminView(this, path, password));
                break;
            case DEFAULT:
            default:
                show(new KeyStoreAdminView(this));
                break;
        }
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of KeyStoreAdminApp
     */
    public static KeyStoreAdmin getApplication() {
        return Application.getInstance(KeyStoreAdmin.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(KeyStoreAdmin.class, args);
        try {
            returnFromMainLatch.await();
        } catch (InterruptedException ex) {
            //do nothing
        }
    }

    @Override
    protected void shutdown() {
        super.shutdown();
        returnFromMainLatch.countDown();
    }

    @Override
    public void exit(final EventObject eo) {  
        if (!ignoreExit){
            super.exit(eo);
        }
        //RADIXMANAGER-179
    }


    public static void launchAndOpenFile(final String keystorePath, final char[] keystorePassword) {
        launchAndOpenFile(keystorePath, keystorePassword, false);
    }
    
    public static void launchAndOpenFile(final String keystorePath, final char[] keystorePassword, final boolean ignoreExit) {
        path = keystorePath;
        password = keystorePassword;
        openMode = OpenMode.OPEN_FILE;
        KeyStoreAdmin.ignoreExit = ignoreExit;

        launch(KeyStoreAdmin.class, new String[]{});
    }
}

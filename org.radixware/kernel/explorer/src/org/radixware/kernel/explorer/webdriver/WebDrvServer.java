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

package org.radixware.kernel.explorer.webdriver;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.net.httpserver.HttpServer;
import com.trolltech.qt.core.Qt;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.utils.QtJambiExecutor;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public final class WebDrvServer {
    
    private final static WebDrvServer INSTANCE = new WebDrvServer();
    
    private HttpServer httpServer;
    private IClientEnvironment environment;
    
    private WebDrvServer(){
    }
    
    public void start(final InetSocketAddress socketAddress, final Collection<QtJambiExecutor> executors, final IClientEnvironment environment
			, final String webDrvClients
	) throws IOException{
        if (httpServer==null){
            final HttpServer webDrvHttpServer = HttpServer.create();
            webDrvHttpServer.bind(socketAddress, 1);
			final WebDrvHttpHandler handler = WebDrvHttpHandler.getInstance();
			handler.appendWhiteListClients(webDrvClients);
            webDrvHttpServer.createContext("/", handler);
            WebDrvSessionsManager.getInstance().setExecutors(executors);
            this.environment = environment;
            webDrvHttpServer.start();
            httpServer = webDrvHttpServer;
        }
    }

    IClientEnvironment getEnvironment() {
        return environment;
    }
    
    public boolean started(){
        return httpServer!=null;
    }
    
    public void stop(){
        if (httpServer!=null){
            httpServer.stop(0);
            httpServer = null;
            String msg = Application.getInstance().getEnvironment()
                        .getMessageProvider()
                        .translate("WebDriver", "WebDriver stopped.");
            traceEvent(msg);
        }
    }
    
    public static WebDrvServer getInstance(){
        return INSTANCE;
    }
    
    private static Robot robot;
    private static boolean mustUseNativeSetCursorPos = false;
    private static Robot getRobot() throws WebDrvException {
        try {
            if(robot == null) {
                robot = new Robot();
                int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
                WebDrvServer.traceDebug("dpi="+Integer.toString(dpi));
                if(SystemTools.isWindows && dpi != 96) {
                    mustUseNativeSetCursorPos = true;
                    WebDrvServer.traceDebug("Robot is going to use native User32.dll SetCursorPos function.");
                }
            }
            return robot;
        }
        catch(Exception ex) {
            throw new WebDrvException(ex.getMessage(), ex);
        }
    }
    
    /**
     * Ткнуть левой кнопкой мышкой в точке (x,y) (координаты главного экрана).
     */
    public static void postGlobalClick(int x, int y) throws WebDrvException {
        postGlobalMouseMove(x, y);
        postClick();
    }

    /**
     * Передвинуть курсор мыши в точку (x,y) (координаты главного экрана).
     */
    public static void postGlobalMouseMove(int x, int y) throws WebDrvException {
        if(mustUseNativeSetCursorPos) {
            // 20180315 Котрачев. На винде, если установить масштаб != 100% (например 150%)
            // робот начинает мазать мимо.
            // Вызов winAPI вроде правильно работает.
            User32Library.INSTANCE.SetCursorPos(x, y);
        }
        else {
            getRobot().mouseMove(x, y);
        }
    }
    
    /**
     * Только для Винды.
     */
    private interface User32Library extends Library {
        User32Library INSTANCE = (User32Library) Native.loadLibrary(
            "User32", User32Library.class);
        boolean SetCursorPos(int x, int y);
    }

    /**
     * Клик левой кнопкой.
     */
    public static void postClick() throws WebDrvException {
        Robot r = getRobot();
        r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
    
    public static void postMousePress(Qt.MouseButton btn) throws WebDrvException {
        getRobot().mousePress(translateQtMouseToRobot(btn));
    }
    
    public static void postMouseRelease(Qt.MouseButton btn) throws WebDrvException {
        getRobot().mouseRelease(translateQtMouseToRobot(btn));
    }
    
    private static int translateQtMouseToRobot(Qt.MouseButton button) throws WebDrvException {
        if(button == Qt.MouseButton.LeftButton) return InputEvent.BUTTON1_DOWN_MASK;
        if(button == Qt.MouseButton.MidButton) return InputEvent.BUTTON2_DOWN_MASK;
        if(button == Qt.MouseButton.RightButton) return InputEvent.BUTTON3_DOWN_MASK;
        throw new WebDrvException(EWebDrvErrorCode.UNSUPPORTED_OPERATION, "Only LeftButton , RightButton and MidButton mouse buttons are supported.");
    }
    
    public static void traceEvent(String translatedMessage, Object... args) {
        String message = String.format(translatedMessage, args);
        trace(message, EEventSeverity.EVENT);
    }
    
    private static void trace(String message, EEventSeverity severity) {
        IClientEnvironment e = Application.getInstance().getEnvironment();
        e.getTracer().put(severity, message, EEventSource.WEB_DRIVER);
    }
    
    public static void traceDebug(String translatedMessage, Object... args) {
        String message = String.format(translatedMessage, args);
        trace(message, EEventSeverity.DEBUG);
    }
    
    static void traceError(String translatedMessage, Object... args) {
        String message = String.format(translatedMessage, args);
        trace(message, EEventSeverity.ERROR);
    }

}

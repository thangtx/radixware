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

package org.radixware.kernel.starter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;


public class StarterSplashScreen {

    private static final String SPLASH_IMAGE_PATH = "org/radixware/kernel/starter/resources/starter_splash.png";
    private static volatile SplashWindow splashWindow;
    private static volatile Frame frame;
    private static volatile Image image;
    private static volatile Thread statusUpdaterThread;
    //do not rename, used via reflection
    public static volatile WeakReference<Object> statusProviderRef;

    public static void show(final String className) {
        try {
            getSplashClass().getMethod("showImpl", String.class).invoke(null, className);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void setStatusProvider(final Object provider) {
        try {
            getSplashClass().getField("statusProviderRef").set(null, provider == null ? null : new WeakReference<>(provider));
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void hide() {
        try {
            getSplashClass().getMethod("hideImpl").invoke(null);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void showImpl(final String className) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createSplashWindow(className);
                    splashWindow.setLocationRelativeTo(null);
                    splashWindow.setVisible(true);
                    statusUpdaterThread = new Thread() {
                        private final SplashWindow window = splashWindow;
                        private volatile boolean updateScheduled = false;

                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted() && window.isVisible()) {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException ex) {
                                    return;
                                }
                                if (!updateScheduled) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (window.isVisible()) {
                                                window.requestRepaint();
                                            }
                                            updateScheduled = false;
                                        }
                                    });
                                    updateScheduled = true;
                                }
                            }
                        }
                    };
                    statusUpdaterThread.setDaemon(true);
                    statusUpdaterThread.start();
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }
            }
        });
    }

    private static void createSplashWindow(final String text) throws IOException {
        frame = new Frame();
        if (image == null) {
            final URL imageUrl = Thread.currentThread().getContextClassLoader().getResource(SPLASH_IMAGE_PATH);
            if (imageUrl != null) {
                image = ImageIO.read(imageUrl);
            } else {
                return;
            }
        }
        splashWindow = new SplashWindow(frame, image, text);
    }

    public static void hideImpl() {
        if (splashWindow != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    splashWindow.setVisible(false);
                    splashWindow.dispose();
                    frame.dispose();
                    statusUpdaterThread.interrupt();
                    splashWindow = null;
                    frame = null;
                    statusUpdaterThread = null;
                }
            });
        }
    }

    private static Class getSplashClass() {
        ClassLoader startersClassLoader = Starter.class.getClassLoader();
        while (RadixClassLoader.class.getName().equals(startersClassLoader.getClass().getName())) {
            startersClassLoader = startersClassLoader.getClass().getClassLoader();
        }
        try {
            return startersClassLoader.loadClass(StarterSplashScreen.class.getName());
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        return StarterSplashScreen.class;
    }

    private static final class SplashWindow extends Window {

        private static final int TITLE_TOP_LEFT_X = 158;
        private static final int TITLE_TOP_LEFT_Y = 150;
        private static final int TITLE_MAX_WIDTH = 180;
        private static final int TITLE_MAX_HEIGHT = 20;
        private static final int STATUS_TOP_LEFT_X = 2;
        private static final int STATUS_TOP_LEFT_Y = 170;
        private static final Color TEXT_COLOR = new Color(0x0075b4f7);
        private final Image image;
        private Image tmpImage;
        private Graphics tmpGraphics;
        private final String text;
        private Font titleFont;
        private Font statusFont;

        public SplashWindow(final Frame owner, final Image image, final String text) {
            super(owner);
            this.image = image;
            this.text = text;
            setSize(image.getWidth(owner), image.getHeight(owner));
        }

        @Override
        public void paint(Graphics g) {
            update(g);
        }

        @Override
        public void update(Graphics g) {
            if (tmpImage == null) {
                tmpImage = createImage(getWidth(), getHeight());
                tmpGraphics = tmpImage.getGraphics();
            }
            doPaint(tmpGraphics);
            g.drawImage(tmpImage, 0, 0, this);
        }

        private void doPaint(final Graphics g) {
            g.drawImage(image, 0, 0, this);
            drawText(g);
        }

        private void drawText(Graphics g) {
            if (titleFont == null) {
                final Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(text, g);
                final double textWidth = stringBounds.getWidth();
                final double textHeight = stringBounds.getHeight();
                final double scale = Math.min(TITLE_MAX_WIDTH / textWidth, TITLE_MAX_HEIGHT / textHeight);
                final AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
                titleFont = (g.getFont().deriveFont(transform));
                statusFont = g.getFont().deriveFont(4);
            }
            g.setColor(TEXT_COLOR);
            g.setFont(titleFont);
            int x_start = (int) (TITLE_TOP_LEFT_X + TITLE_MAX_WIDTH - g.getFontMetrics().getStringBounds(text, g).getWidth());
            int y_start = (int) (TITLE_TOP_LEFT_Y);
            g.drawString(text, x_start, y_start);
            final String status = getStatus();
            if (status != null) {
                g.setFont(statusFont);
                g.drawString(status, STATUS_TOP_LEFT_X, STATUS_TOP_LEFT_Y);
            }
        }

        public void requestRepaint() {
            repaint();
        }

        private String getStatus() {
            try {
                final Object statusProviderRefObj = getSplashClass().getField("statusProviderRef").get(null);
                if (statusProviderRefObj instanceof WeakReference) {
                    final Object statusProviderObj = ((WeakReference) statusProviderRefObj).get();
                    if (statusProviderObj != null) {
                        return statusProviderObj.getClass().getMethod("getStatus").invoke(statusProviderObj).toString();
                    }
                }
            } catch (Exception ex) {
                //ingore
            }
            return null;
        }
    }
}

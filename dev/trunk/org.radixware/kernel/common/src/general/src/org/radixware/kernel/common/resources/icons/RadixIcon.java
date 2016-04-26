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

package org.radixware.kernel.common.resources.icons;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.radixware.kernel.common.enums.ERadixIconType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.ThreadSafe;

/**
 * RadixWare icon.
 * Supports AWT image getter and Swing icon getter.
 * All AWT images and Swing icons with standard size (16x16 in RadixWare) are cashed.
 */
@ThreadSafe
public class RadixIcon {

    private List<Image> cachedImages = null;
    private Image cachedOriginalImage = null; //required to display images in editor in original size
    private List<Icon> cachedIcons = null;
    private boolean isErronerous = false;
    private final String name; // relative url of file path
    private final File file;

    protected RadixIcon(String name) {
        this.name = name;
        this.file = null;
    }

    public RadixIcon(File file) {
        this.name = file.getAbsolutePath();
        this.file = file;
    }
    
    private static final int STD_SIZE = 16;

    /**
     * Get AWT image with standard size (16x16).
     * @return image or null if unable to load at first call.
     * @throws RadixError if unable to load image first time.
     */
    public Image getImage() {
        return getImage(STD_SIZE, STD_SIZE);
    }

    /**
     * Get AWT image with scalled size to specified height.
     * @return image or null if unable to load at first call.
     * @throws RadixError if unable to load image first time.
     */
    public Image getImage(int height) {
        return getImage(height, height);
    }

    /**
     * Get scalled AWT image.
     * @return image or null if unable to load at first call.
     * @throws RadixError if unable to load image first time.
     */
    public Image getImage(int width, int height) {
        synchronized (this) {
            if (isErronerous) {
                return null;
            }
            if (cachedImages != null) {
                for (Image image : cachedImages) {
                    if (image.getWidth(null) == width && image.getHeight(null) == height) {
                        return image;
                    }
                }
            }

            try {
                final Image image;
                if (file != null) {
                    image = ImageLoader.loadImage(file, width, height);
                } else {
                    image = ImageLoader.loadImage(getResourceUri(), width, height);
                }
                if (cachedImages == null) {
                    cachedImages = new ArrayList<>();
                }
                cachedImages.add(image);
                return image;
            } catch (Exception cause) {
                isErronerous = true;
                //throw new RadixError("Unable to load image '" + name + "'.", cause);
                // RADIX-7129
                Logger.getLogger(RadixIcon.class.getName()).log(Level.INFO, "Unable to load image '" + name + "'.", cause);
                return null;
            }
        }
    }

    /**
     * Get Swing icon with standard size (16x16).
     * @return image or null if unable to load at first call.
     * @throws RadixError if unable to load image first time.
     */
    public Icon getIcon() {
        return getIcon(STD_SIZE, STD_SIZE);
    }

    /**
     * Get Swing icon with scalled size to specified height.
     * @return image or null if unable to load at first call.
     * @throws RadixError if unable to load image first time.
     */
    public Icon getIcon(int height) {
        return getIcon(height, height);
    }

    /**
     * Get Swing icon with scalled size to specified height.
     * @return image or null if unable to load at first call.
     * @throws RadixError if unable to load image first time.
     */
    public Icon getIcon(int width, int height) {
        synchronized (this) {
            if (cachedIcons != null) {
                for (Icon icon : cachedIcons) {
                    if (icon.getIconWidth() == width && icon.getIconHeight() == height) {
                        return icon;
                    }
                }
            }

            final Image image = getImage(width, height);

            if (image != null) {
                final Icon icon = new ImageIcon(image);
                if (cachedIcons == null) {
                    cachedIcons = new ArrayList<>();
                }
                cachedIcons.add(icon);
                return icon;
            } else {
                return null;
            }
        }
    }

    /**
     * Load image in its original size.
     * @return image or null if unable to load at first call.
     * @throws RadixError if unable to load image first time.
     */
    public Image getOriginalImage() {
        synchronized (this) {
            if (isErronerous) {
                return null;
            }
            if (getType() == ERadixIconType.SVG) {
                return getImage();
            }
            if (cachedOriginalImage != null) {
                return cachedOriginalImage;
            }
            try {
                if (file != null) {
                    cachedOriginalImage = ImageLoader.loadImage(file);
                } else {
                    cachedOriginalImage = ImageLoader.loadImage(getResourceUri());
                }
                return cachedOriginalImage;
            } catch (IOException cause) {
                isErronerous = true;
                //throw new RadixError("Unable to load image '" + name + "'", cause);
                // RADIX-7129
                Logger.getLogger(RadixIcon.class.getName()).log(Level.INFO, "Unable to load image '" + name + "'.", cause);
                return null;
            }

        }
    }

    private ERadixIconType getType() {
        return ImageLoader.getRadixIconType(name);
    }

    @Override
    public String toString() {
        return super.toString() + "; " + name;
    }

    public String getResourceUri(){
        return "org/radixware/kernel/common/resources/" + name;
    }
}

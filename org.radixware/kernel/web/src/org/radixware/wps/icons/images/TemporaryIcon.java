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
package org.radixware.wps.icons.images;

import java.awt.Dimension;
import org.radixware.wps.icons.WpsIcon;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.WpsEnvironment;

public class TemporaryIcon {

    public enum Format {

        PNG, GIF
    }

    private static BufferedImage loadFromFile(WpsIcon icon, boolean force, Dimension d, StringBuilder fileName) throws IOException {
        File file = icon.getImageFile(d.width, d.height);
        String n = icon.getImageFileName(d.width, d.height);
        if (file != null) {
            try {
                BufferedImage image = ImageIO.read(file);
                int svgi = n.indexOf(".svg");
                int pngi = n.indexOf(".png");
                int idx = svgi > 0 ? svgi : (pngi > 0 ? pngi : n.length());
                fileName.append(n.substring(0, idx)).append("_");
                return image;
            } catch (IOException ex) {
                if (!force) {
                    return loadFromFile(icon, true, d, fileName);
                }
                Logger.getLogger(TemporaryIcon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static WpsIcon createTempIcon(WpsEnvironment env, Dimension dim, Format format, final WpsIcon... icons) {
        if (icons.length <= 0) {
            throw new IllegalArgumentException("Cannot create temporary icon!");
        }
        String ext = setFormat(format);

        BufferedImage[] images = new BufferedImage[icons.length];
        StringBuilder fileName = new StringBuilder();
        for (int i = 0; i < icons.length; i++) {
            Dimension d = dim == null ? icons[i].getOriginalSize() : dim;
            try {
                images[i] = loadFromFile(icons[i], false, d, fileName);
            } catch (IOException ex) {
                Logger.getLogger(TemporaryIcon.class.getName()).log(Level.FINE, null, ex);
            }

        }
        Dimension dimension = new Dimension();
        images = filter(images, dimension);
        if (images.length == 0) {
            return null;
        }
        File file = createTempIconFile(images, fileName.toString(), ext, dimension.width, dimension.height);
        if (file != null && file.exists() && file.isFile()) {
            return new WpsIcon(env, file);
        } else {
            return null;
        }

    }

    public static WpsIcon createTempIcon(WpsEnvironment env, Format format, final Image... icons) {
        if (icons.length <= 0) {
            throw new IllegalArgumentException("Cannot create temporary icon.");
        }
        String ext = setFormat(format);
        String fileName = "";
        for (int i = 0; i < icons.length; i++) {
            fileName += icons[i].hashCode();
        }
        Dimension dim = new Dimension();
        BufferedImage imgs[] = convertToBufferedImage(icons, dim);
        File file = createTempIconFile(imgs, fileName, ext, dim.width, dim.height);
        if (file != null && file.exists() && file.isFile()) {
            return new WpsIcon(env, file);
        }
        return null;
    }

    private static BufferedImage[] convertToBufferedImage(Image[] icons, Dimension dim) {
        BufferedImage imgs[] = new BufferedImage[icons.length];
        int type = BufferedImage.TYPE_INT_ARGB;
        int width = 0;
        int height = 0;
        for (int i = 0; i < imgs.length; i++) {
            int w = icons[i].getWidth(null);
            int h = icons[i].getHeight(null);
            imgs[i] = new BufferedImage(w, h, type);
            Graphics2D g = imgs[i].createGraphics();
            g.drawImage(icons[i], null, null);
            width += w;
            height = Math.max(height, h);
        }
        dim.width = width;
        dim.height = height;
        return imgs;
    }

    private static File createTempIconFile(BufferedImage[] imgs, String fileName, String ext, int width, int height) {
        if (imgs == null || imgs.length <= 0 || width <= 0 || height <= 0) {
            return null;
        }

        BufferedImage tempBufIcon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = tempBufIcon.createGraphics();
        int x = 0;
        //draw images in one row
        for (int j = 0; j < imgs.length; j++) {
            x = j > 0 ? x + imgs[j - 1].getWidth() : 0;
            gr.drawImage(imgs[j], null, x, 0);
        }
        try {

            File f = RadixLoader.getInstance().createTempFile(fileName + ".svg." + ext);
            ImageIO.write(tempBufIcon, ext.toUpperCase(), f);
            f.deleteOnExit();
            return f;
        } catch (IOException ex) {
            Logger.getLogger(TemporaryIcon.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            gr.dispose();
        }
        return null;
    }

    private static String setFormat(Format format) {
        if (format == null) {
            return "png";
        }
        switch (format) {
            case GIF:
                return "gif";
            case PNG:
                return "png";

            default:
                throw new IllegalArgumentException("Unknown image format " + format.name());
        }
    }

    private static BufferedImage[] filter(BufferedImage[] imgs, Dimension dim) {
        int width = 0;
        int height = 0;
        List<BufferedImage> result = new LinkedList<>();
        for (int i = 0; i < imgs.length; i++) {
            if (imgs[i] != null) {
                width += imgs[i].getWidth();
                height = Math.max(imgs[i].getHeight(), height);
                result.add(imgs[i]);
            }
        }
        if (dim != null) {
            dim.width = width;
            dim.height = height;
        }
        return result.toArray(new BufferedImage[result.size()]);
    }
}

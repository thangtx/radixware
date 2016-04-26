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

package org.radixware.kernel.designer.common.dialogs;

import org.radixware.kernel.common.resources.icons.*;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;
import org.radixware.kernel.common.enums.ERadixIconType;
import org.radixware.kernel.common.exceptions.RadixError;


public class StdImageLoader implements IImageLoader {

    @Override
    public boolean isSupported(ERadixIconType type) {
        return type != ERadixIconType.SVG;
    }

    private static Image scale(Image image, int width, int height) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (w != width || h != height) {
            return image.getScaledInstance(width, height, 0);
        }
        return image;
    }

    @Override
    public Image loadImage(File file) throws IOException {
        assert file != null;

        try {
            final String path = file.getAbsolutePath();
            final ImageIcon imageIcon = new ImageIcon(path);
            if (imageIcon == null || imageIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
                throw new RadixError("Invalid image format.");
            }
            final Image image = imageIcon.getImage();
            return image;
        } catch (Exception cause) {
            throw new IOException("Unable to load image '" + String.valueOf(file.getAbsoluteFile()) + "'.", cause);
        }
    }

    @Override
    public Image loadImage(File file, int width, int height) throws IOException {
        final Image image = loadImage(file);
        return scale(image, width, height);
    }

    @Override
    public Image loadImage(String resourceUri) throws IOException {
        assert resourceUri != null;

        try {
            final URL url = RadixIcon.class.getClassLoader().getResource(resourceUri);
            final ImageIcon imageIcon = new ImageIcon(url);
            if (imageIcon == null || imageIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
                throw new RadixError("Invalid image format.");
            }
            final Image image = imageIcon.getImage();
            return image;
        } catch (Exception cause) {
            throw new IOException("Unable to load image '" + resourceUri + "'.", cause);
        }
    }

    @Override
    public Image loadImage(String resourceUri, int width, int height) throws IOException {
        final Image image = loadImage(resourceUri);
        return scale(image, width, height);
    }
}

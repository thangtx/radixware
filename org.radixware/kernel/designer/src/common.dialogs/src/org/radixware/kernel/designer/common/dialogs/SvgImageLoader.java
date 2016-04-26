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

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.radixware.kernel.common.enums.ERadixIconType;
import org.radixware.kernel.common.resources.icons.IImageLoader;
import org.radixware.kernel.common.resources.icons.RadixIcon;

/**
 * Loader of RadixIcon icon and image. Singleton.
 *
, akiliyevich.
 */
public class SvgImageLoader implements IImageLoader {

    public SvgImageLoader() {
    }

    @Override
    public boolean isSupported(ERadixIconType type) {
        return type == ERadixIconType.SVG;
    }

    @Override
    public Image loadImage(String resourceUri, int width, int height) throws IOException {
        InputStream inputStream = null;
        try {
            final URL url = RadixIcon.class.getClassLoader().getResource(resourceUri);
            if (url != null) {
                inputStream = url.openStream();
                final Image image = SvgImage.Factory.newInstance(inputStream, width, height);
                return image;
            } else {
                throw new IOException("Can not load image from " + resourceUri);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public Image loadImage(String resourceUri) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Image loadImage(File file, int width, int height) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            final Image image = SvgImage.Factory.newInstance(inputStream, width, height);
            return image;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public Image loadImage(File file) throws IOException {
        throw new UnsupportedOperationException();
    }
}

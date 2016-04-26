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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.radixware.kernel.common.enums.ERadixIconType;
import org.radixware.kernel.common.services.RadixServiceRegistry;


class ImageLoader {

    public static ERadixIconType getRadixIconType(String name) {
        final int pos = name.lastIndexOf('.');
        if (pos < 0) {
            return ERadixIconType.UNKNOWN;
        }
        final String ext = name.substring(pos + 1).toLowerCase();
        for (ERadixIconType type : ERadixIconType.values()) {
            if (type.getValue().equals(ext)) {
                return type;
            }
        }
        return ERadixIconType.UNKNOWN;
    }

    public static ERadixIconType getRadixIconType(File file) {
        final String name = file.getName();
        return getRadixIconType(name);
    }

    private static IImageLoader findImageLoader(ERadixIconType type) {
        final Iterator<IImageLoader> loaders = RadixServiceRegistry.getDefault().iterator(IImageLoader.class);
        if (loaders != null) {
            while (loaders.hasNext()) {
                final IImageLoader loader = loaders.next();
                if (loader.isSupported(type)) {
                    return loader;
                }
            }
        }
        return null;
    }

    private static final class StubImageLoader implements IImageLoader {

        private StubImageLoader() {
        }
        private static final StubImageLoader INSTANCE = new StubImageLoader();

        public static StubImageLoader getInstance() {
            return INSTANCE;
        }

        @Override
        public boolean isSupported(ERadixIconType type) {
            return true;
        }

        @Override
        public Image loadImage(String resourceUri, int width, int height) throws IOException {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public Image loadImage(String resourceUri) throws IOException {
            return new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public Image loadImage(File file, int width, int height) throws IOException {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public Image loadImage(File file) throws IOException {
            return new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        }
    }

    private static IImageLoader findImageLoader(String resourceUri) throws IOException {
        final ERadixIconType iconType = getRadixIconType(resourceUri);
        if (iconType != ERadixIconType.UNKNOWN) {
            final IImageLoader loader = findImageLoader(iconType);
            if (loader != null) {
                return loader;
            }
            return StubImageLoader.getInstance();
        }
        throw new IOException("Loader not found for '" + resourceUri + "'.");
    }

    private static IImageLoader findImageLoader(File file) throws IOException {
        final ERadixIconType iconType = getRadixIconType(file);
        if (iconType != ERadixIconType.UNKNOWN) {
            final IImageLoader loader = findImageLoader(iconType);
            if (loader != null) {
                return loader;
            }
        }
        throw new IOException("Loader not found for '" + file.getAbsolutePath() + "'.");
    }

    public static Image loadImage(String resourceUri, int width, int height) throws IOException {
        final IImageLoader loader = findImageLoader(resourceUri);
        return loader.loadImage(resourceUri, width, height);
    }

    public static Image loadImage(String resourceUri) throws IOException {
        final IImageLoader loader = findImageLoader(resourceUri);
        return loader.loadImage(resourceUri);
    }

    public static Image loadImage(File file, int width, int height) throws IOException {
        final IImageLoader loader = findImageLoader(file);
        return loader.loadImage(file, width, height);
    }

    public static Image loadImage(File file) throws IOException {
        final IImageLoader loader = findImageLoader(file);
        return loader.loadImage(file);
    }
}

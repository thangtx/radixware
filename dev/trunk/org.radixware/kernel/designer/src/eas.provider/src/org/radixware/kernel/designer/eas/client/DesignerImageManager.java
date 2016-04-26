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

package org.radixware.kernel.designer.eas.client;

import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ImageManager;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.types.Id;


class DesignerImageManager implements ImageManager {

    @Override
    public Icon loadIcon(String path) {
        return null;
    }

    @Override
    public Icon getIcon(ClientIcon icon) {
        return loadIcon(icon.fileName);
    }

    @Override
    public void clearCache(boolean clearAdsIconsOnly) {
    }

    @Override
    public Id findCachedIconId(Icon icon) {
        return null;
    }
//    private final static String CLASSPATH_PROTOCOL = "classpath:";
//    private final static String EXPLORER_DIR = "org/radixware/kernel/common/client/";
//    

    public DesignerImageManager() {
    }
//    private URL getImageUrl(final String fileName) {
//        final String actualFileName;
//        if (fileName.startsWith(CLASSPATH_PROTOCOL + "images/")
//                || fileName.startsWith(CLASSPATH_PROTOCOL + "/images/")
//                || fileName.startsWith(CLASSPATH_PROTOCOL + "//images/")) {
//            final int idx = fileName.indexOf("images/");
//            final String path = fileName.substring(idx);
//            actualFileName = EXPLORER_DIR + path;
//        } else if (fileName.startsWith(CLASSPATH_PROTOCOL)) {
//            actualFileName = fileName.substring(CLASSPATH_PROTOCOL.length());
//        } else {
//            actualFileName = fileName;
//        }
//
//        final ClassLoader loader;
//        if (app.getDefManager() != null
//                && app.getDefManager().getAdsVersion().getNumber() > -1
//                && RadixLoader.getInstance() != null
//                && app.getDefManager().getClassLoader() != null) {
//            loader = app.getDefManager().getClassLoader();
//        } else {
//            loader = Thread.currentThread().getContextClassLoader();
//        }
//        final URL imageUrl = loader.getResource(actualFileName);
//        if (imageUrl == null) {
//            throw new DefinitionError("File not found \"" + actualFileName + "\"");
//        }
//        return imageUrl;
//
//    }
//    public WpsIcon findCachedIcon(String filePath) {
//        return cache.findInCache(filePath);
//    }
}

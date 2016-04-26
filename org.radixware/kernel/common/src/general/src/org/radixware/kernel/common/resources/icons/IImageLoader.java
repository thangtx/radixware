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
import org.radixware.kernel.common.enums.ERadixIconType;


public interface IImageLoader {

    public Image loadImage(String resourceUri, int width, int height) throws IOException;

    public Image loadImage(String resourceUri) throws IOException;

    public Image loadImage(File file, int width, int height) throws IOException;

    public Image loadImage(File file) throws IOException;

    public boolean isSupported(ERadixIconType type);
}

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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.explorer.types.RdxIcon;


public final class ExplorerIcon extends ClientIcon {

    private ExplorerIcon(final String fileName) {
        super(fileName);
    }
    
    public static QIcon getQIcon(final ClientIcon popupIcon, final ClientIcon icon, final QWidget owner) {
        if (popupIcon==null || owner==null){
            return getQIcon(icon);
        }
        if (icon==null){
            return getQIcon(popupIcon);
        }
        return getQIcon(isNativePaintingStyle(owner) ? popupIcon : icon);
    }

    public static QIcon getQIcon(final ClientIcon icon) {
        if (icon == null) {
            return null;
        } else {
            final RdxIcon rdxIcon;
            if (icon.isSvg){
                rdxIcon = ((ImageManager) Application.getInstance().getImageManager()).loadSvgIcon(icon.fileName, QColor.transparent);
            }
            else{
                rdxIcon = ((ImageManager) Application.getInstance().getImageManager()).loadQIcon(icon.fileName);
            }
            return ImageManager.getQIcon(rdxIcon);
        }
    }

    public static QIcon getQIcon(final Icon icon) {
        return ImageManager.getQIcon(icon);
    }
    
    public static QIcon getQIcon(final Icon popupIcon, final Icon icon, final QWidget owner) {
        if (popupIcon==null || owner==null){
            return getQIcon(icon);
        }
        if (icon==null){
            return getQIcon(popupIcon);
        }
        return getQIcon(isNativePaintingStyle(owner) ? popupIcon : icon);
    }
    
    private static boolean isNativePaintingStyle(final QWidget owner){
        return "windowsvista".equals(owner.style().objectName()) || "windowsxp".equals(owner.style().objectName());
    }    
}
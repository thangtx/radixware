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

package org.radixware.kernel.common.client.text;

import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.ETextDecoration;


public interface IFontManager {
    IFont getDefaultFont();
    IFont getFont(String family, int pointSize, EFontWeight weight, EFontStyle style, EnumSet<ETextDecoration> decoration);
    IFont getFont(String family, int pointSize, EFontWeight weight, EFontStyle style);
    IFont getFont(String family, int pointSize, EFontWeight weight);
    IFont getFont(String family, int pointSize);
    IFont getFont(String family);
    IFont getFont(int pointSize, EFontWeight weight, EFontStyle style, EnumSet<ETextDecoration> decoration);
    IFont getFont(int pointSize, EFontWeight weight, EFontStyle style);
    IFont getFont(int pointSize, EFontWeight weight);
    IFont getFont(int pointSize);
    IFont getFont(EFontWeight weight, EFontStyle style, EnumSet<ETextDecoration> decoration);
    IFont getFont(EFontWeight weight, EFontStyle style);
    IFont getFont(EFontWeight weight);
    IFont getFont(EFontStyle style, EnumSet<ETextDecoration> decoration);
    IFont getFont(EFontStyle style);
    IFont getFontFromStr(final String fontAsStr);
}
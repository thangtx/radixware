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


public abstract class AbstractFontManager implements IFontManager{

    @Override
    public IFont getFont(String family, int pointSize, EFontWeight weight, EFontStyle style) {
        return getFont(family, pointSize, weight, style, null);
    }

    @Override
    public IFont getFont(String family, int pointSize, EFontWeight weight) {
        return getFont(family, pointSize, weight, null, null);
    }

    @Override
    public IFont getFont(String family, int pointSize) {
        return getFont(family, pointSize, null, null, null);
    }

    @Override
    public IFont getFont(String family) {
        return getFont(family, -1, null, null, null);
    }

    @Override
    public IFont getFont(int pointSize, EFontWeight weight, EFontStyle style, EnumSet<ETextDecoration> decoration) {
        return getFont(null, pointSize, weight, style, decoration);
    }

    @Override
    public IFont getFont(int pointSize, EFontWeight weight, EFontStyle style) {
        return getFont(null, pointSize, weight, style, null);
    }

    @Override
    public IFont getFont(int pointSize, EFontWeight weight) {
        return getFont(null, pointSize, weight, null, null);
    }

    @Override
    public IFont getFont(int pointSize) {
        return getFont(null, pointSize, null, null, null);
    }

    @Override
    public IFont getFont(EFontWeight weight, EFontStyle style, EnumSet<ETextDecoration> decoration) {
        return getFont(null, -1, weight, style, decoration);
    }

    @Override
    public IFont getFont(EFontWeight weight, EFontStyle style) {
        return getFont(null, -1, weight, style, null);
    }

    @Override
    public IFont getFont(EFontWeight weight) {
        return getFont(null, -1, weight, null, null);
    }

    @Override
    public IFont getFont(EFontStyle style, EnumSet<ETextDecoration> decoration) {
        return getFont(null, -1, null, style, decoration);
    }

    @Override
    public IFont getFont(EFontStyle style) {
        return getFont(null, -1, null, style, null);
    }   
}
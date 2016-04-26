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

import java.awt.Color;
import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextDecoration;


public interface ITextOptions {
    
    Color getForegroundColor();
    
    Color getBackgroundColor();    
    
    IFont getFont();
    
    ETextAlignment getAlignment();
    
    ITextOptions changeForegroundColor(Color color);
    
    ITextOptions changeBackgroundColor(Color color);
    
    ITextOptions changeAlignment(ETextAlignment alignment);
    
    ITextOptions changeFont(IFont font);
    
    ITextOptions changeFontFamily(String newFamily);
    
    ITextOptions changeFontSize(int size);
    
    ITextOptions changeFontWeight(EFontWeight weight);
    
    ITextOptions changeFontStyle(EFontStyle style);
    
    ITextOptions changeFontDecoration(EnumSet<ETextDecoration> decoration);
    
    ITextOptions increaseFontSize(int size);
    
    ITextOptions decreaseFontSize(int size);
    
    ITextOptions getWithBoldFont();
    
    ITextOptions getWithItalicFont();
    
    String asStr();
}

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


public interface IFont {

    String getFamily();
    
    int getSize();
    
    EFontWeight getWeight();
    
    EFontStyle  getStyle();
    
    EnumSet<ETextDecoration> getDecoration();
    
    boolean isItalic();
    
    boolean isBold();    
    
    IFont changeFamily(String newFamily);
    
    IFont changeSize(int size);
    
    IFont changeWeight(EFontWeight weight);
    
    IFont changeStyle(EFontStyle style);
    
    IFont changeDecoration(EnumSet<ETextDecoration> decoration);
    
    IFont increaseSize(int size);
    
    IFont decreaseSize(int size);
    
    IFont getBold();
    
    IFont getItalic();
    
    String asStr();//fixed format
}
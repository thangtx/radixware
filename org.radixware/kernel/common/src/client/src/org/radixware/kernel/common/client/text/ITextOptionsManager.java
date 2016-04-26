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
import org.radixware.kernel.common.client.enums.ETextAlignment;


public interface ITextOptionsManager {
    ITextOptions getOptions(IFont font, ETextAlignment alignment, Color foreground, Color background);
    ITextOptions getOptions(IFont font, ETextAlignment alignment, Color foreground);
    ITextOptions getOptions(IFont font, ETextAlignment alignment);    
    ITextOptions getOptions(IFont font, Color foreground, Color background);
    ITextOptions getOptions(IFont font, Color foreground);
    ITextOptions getOptions(IFont font);
    ITextOptions getOptions(Color foreground, Color background);
    ITextOptions getOptions(Color foreground);
    ITextOptions getOptions(ETextAlignment alignment);
    ITextOptions getOptions(ETextAlignment alignment, Color foreground, Color background);
    ITextOptions getOptions(ETextAlignment alignment, Color foreground);        
    ITextOptions merge(ITextOptions initial, ITextOptions addon);
    Character getPasswordCharacter();
}
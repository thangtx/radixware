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


public abstract class TextOptionsManager implements ITextOptionsManager{
    
    private final IFontManager fontManager;
    
    protected TextOptionsManager(final IFontManager fntMngr){
        this.fontManager = fntMngr;
    }
                    
    @Override
    public ITextOptions getOptions(final IFont font, final ETextAlignment alignment,final Color foreground) {
        return getOptions(font,alignment,foreground,null);
    }

    @Override
    public ITextOptions getOptions(final  IFont font, final  ETextAlignment alignment) {
        return getOptions(font,alignment,null,null);
    }

    @Override
    public ITextOptions getOptions(final  IFont font, final Color foreground, final  Color background) {
        return getOptions(font,null,foreground,background);
    }

    @Override
    public ITextOptions getOptions(final IFont font, final Color foreground) {
        return getOptions(font,null,foreground,null);
    }

    @Override
    public ITextOptions getOptions(final  IFont font) {
        return getOptions(font,null,null,null);
    }

    @Override
    public ITextOptions getOptions(final  Color foreground, final Color background) {
        return getOptions(null,null,foreground,background);
    }

    @Override
    public ITextOptions getOptions(final Color foreground) {
        return getOptions(null,null,foreground,null);
    }

    @Override
    public ITextOptions getOptions(final ETextAlignment alignment) {
        return getOptions(null,alignment,null,null);
    }

    @Override
    public ITextOptions getOptions(final ETextAlignment alignment, final Color foreground, final Color background) {
        return getOptions(null,alignment,foreground,background);
    }

    @Override
    public ITextOptions getOptions(final ETextAlignment alignment, final Color foreground) {
        return getOptions(null,alignment,foreground,null);
    }   

    @Override
    public ITextOptions merge(final ITextOptions initial, final ITextOptions addon) {
        if (addon==null || isEmptyOptions(addon)){
            return initial;
        }
        if (initial==null || isEmptyOptions(initial) || initial==addon){
            return addon;
        }
        final boolean wholeOverride = 
                (addon.getFont()!=null || initial.getFont()==null) &&
                (addon.getAlignment()!=null || initial.getAlignment()==null) &&
                (addon.getForegroundColor()!=null || initial.getForegroundColor()==null) &&
                (addon.getBackgroundColor()!=null || initial.getBackgroundColor()==null);
        if (wholeOverride){
            return addon;
        }
        final IFont font = 
               addon.getFont()==null ? initial.getFont() : addon.getFont();
        final ETextAlignment alignment = 
            addon.getAlignment()==null ? initial.getAlignment() : addon.getAlignment();
        final Color fg = 
            addon.getForegroundColor()==null ? initial.getForegroundColor() : addon.getForegroundColor();
        final Color bg = 
            addon.getBackgroundColor()==null ? initial.getBackgroundColor() : addon.getBackgroundColor();
        return getOptions(font, alignment, fg, bg);
    }
    
    protected static boolean isEmptyOptions(final ITextOptions options){
        return options.getFont()==null &&
               options.getAlignment()==null &&
               options.getForegroundColor()==null &&
               options.getBackgroundColor()==null;
    }   
}

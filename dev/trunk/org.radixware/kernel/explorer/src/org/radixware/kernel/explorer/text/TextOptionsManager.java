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

package org.radixware.kernel.explorer.text;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QWidget;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.text.IFont;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.explorer.env.Application;


public final class TextOptionsManager extends org.radixware.kernel.common.client.text.TextOptionsManager{
    
    private final static TextOptionsManager INSTANCE = new TextOptionsManager();
    private final static Map<String,Character> PWD_CHARACTER_BY_STYLE_NAME = new HashMap<>();
    
    private final Map<String,ExplorerTextOptions> cache = new HashMap<>();
    
    private TextOptionsManager(){
        super(ExplorerFontManager.getInstance());
    }

    @Override
    public ExplorerTextOptions getOptions(final IFont font, final ETextAlignment alignment, final Color foreground, final Color background) {
        final String key = TextOptions.getTextOptionsAsStr(font, alignment, foreground, background);
        ExplorerTextOptions options = cache.get(key);
        if (options==null){
            options = 
                new ExplorerTextOptions((ExplorerFont)font, alignment, foreground, background);
            cache.put(key, options);
        }
        return options;
    }
    
    @Override
    public Character getPasswordCharacter() {
        final String styleName = Application.getCurrentStyleName();
        Character pwdChar = PWD_CHARACTER_BY_STYLE_NAME.get(styleName);
        if (pwdChar==null){
            final QWidget widget = Application.getMainWindow()==null ? QApplication.desktop() : Application.getMainWindow();
            final int charCode = 
                QApplication.style().styleHint(QStyle.StyleHint.SH_LineEdit_PasswordCharacter,null,widget);
            final char[] arrChar = Character.toChars(charCode);
            pwdChar = Character.valueOf(arrChar==null || arrChar.length!=1 ? '*' : arrChar[0] );
            PWD_CHARACTER_BY_STYLE_NAME.put(styleName, pwdChar);
        }
        return pwdChar;
    }
    
    public static TextOptionsManager getInstance(){
        return INSTANCE;
    }
}
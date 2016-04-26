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

package org.radixware.kernel.designer.ads.localization.translation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.radixware.kernel.designer.ads.localization.prompt.Validation;


public class Highlight {
    //private final List<Integer[]> hightLightPos;
    //private javax.swing.JTextPane textPane;

    public static void hightlightTranslation(final javax.swing.JTextPane textPane) {
        final List<Integer[]> list = getHightlightPos(textPane.getText());
        final MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, Color.blue);
        final StyledDocument sdoc = textPane.getStyledDocument();
        sdoc.setCharacterAttributes(0, textPane.getText().length(), textPane.getStyle(StyleContext.DEFAULT_STYLE), false);
        for (Integer[] arr : list) {
            sdoc.setCharacterAttributes(arr[0], arr[1], attrs, false);
            // n=arr[0]+arr[1];
            //sdoc.setCharacterAttributes(n,n+1/*edTranslation.getText().length()*/, edTranslation.getStyle(StyleContext.DEFAULT_STYLE), false);
        }
    }

    private static List<Integer[]> getHightlightPos(final String str) {
        final List<Integer[]> hightLightPos = new ArrayList<>();
        if ((str != null) && (!str.equals(""))) {
            findPlaceMark(str, hightLightPos);
            findFormat(str, hightLightPos);
            findHtml(str, hightLightPos);
        }
        return hightLightPos;
    }

    //checking %1, %2, ..., %n
    private static void findPlaceMark(final String str, final List<Integer[]> hightLightPos) {
        final int len = str.length();
        int index = str.indexOf('%', 0);
        while ((index < len) && index != -1) {
            final String chr = str.substring(index, index + 1) + Validation.getNumb(str, index, len);
            final Integer[] arr = {index, chr.length()};
            hightLightPos.add(arr);
            index++;
        }
    }

    //checking message format
    private static boolean findFormat(final String str, final List<Integer[]> hightLightPos) {
        final int len = str.length();
        int index = str.indexOf('{', 0);
        while ((index + 1 < len) && index != -1) {
            //int count = index + 1;
            int count = str.indexOf('}', index + 1);
            if (count== -1) {
                break; 
            } else {               
                final String chr = str.substring(index, count + 1);
                if (Validation.isMsgFormat(str.substring(index + 1, count))) {
                    final Integer[] arr = {index, chr.length()};
                    hightLightPos.add(arr);
                }
            }
            index = str.indexOf('{', count+1);
        }
        return true;
    }

    //checking html
    private static void findHtml(final String str, final List<Integer[]> hightLightPos) {
        if (!str.trim().startsWith("<html>")) {
            return;
        }
        int index = 0, count;
        final int len = str.length(); 
        index = str.indexOf('<', index);
        while ((index < len) && index != -1) {
            count = str.indexOf('>', index);
            if ((index < len) && count != -1) {
                final String chr = str.substring(index, count + 1);
                final Integer[] arr = {index, chr.length()};
                hightLightPos.add(arr);
            } else {
                break;
            }
            index = str.indexOf('<', count+1);
        }
    }
}

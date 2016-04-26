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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.UIManager;
import org.openide.util.ImageUtilities;


class IconWithArrow implements Icon {

    private static final String ARROW_IMAGE_NAME = "org/openide/awt/resources/arrow.png"; //NOI18N

    private final Icon orig;
    private final Icon arrow = ImageUtilities.loadImageIcon(ARROW_IMAGE_NAME, false);
    private final boolean paintRollOver;

    private static final int GAP = 6;

    /** Creates a new instance of IconWithArrow */
    public IconWithArrow(final Icon orig,final boolean paintRollOver ) {
        this.orig = orig;
        this.paintRollOver = paintRollOver;
    }

    @Override
    public void paintIcon(final Component c,final Graphics g,final int x,final int y ) {
        final int height = getIconHeight();
        orig.paintIcon( c, g, x, y+(height-orig.getIconHeight())/2 );

        arrow.paintIcon( c, g, x+GAP+orig.getIconWidth(), y+(height-arrow.getIconHeight())/2 );

        if( paintRollOver ) {
            Color brighter = UIManager.getColor( "controlHighlight" ); //NOI18N
            Color darker = UIManager.getColor( "controlShadow" ); //NOI18N
            if( null == brighter || null == darker ) {
                brighter = c.getBackground().brighter();
                darker = c.getBackground().darker();
            }
            if( null != brighter && null != darker ) {
                g.setColor( brighter );
                g.drawLine( x+orig.getIconWidth()+1, y,
                            x+orig.getIconWidth()+1, y+getIconHeight() );
                g.setColor( darker );
                g.drawLine( x+orig.getIconWidth()+2, y,
                            x+orig.getIconWidth()+2, y+getIconHeight() );
            }
        }
    }

    @Override
    public int getIconWidth() {
        return orig.getIconWidth() + GAP + arrow.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return Math.max( orig.getIconHeight(), arrow.getIconHeight() );
    }

    public static int getArrowAreaWidth() {
        return GAP/2 + 5;
    }
}

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
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import org.openide.util.ImageUtilities;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;

/**
 * JButton with a small arrow that displays JColorChooser dialog when clicked.
 *
 */
class ArrowColorButton extends JButton {

    public class ColorChangeEvent extends RadixEvent {

        private final Color color;

        public ColorChangeEvent(final Color clr) {
            color = clr;
        }

        public Color getColor() {
            return color;
        }

    }

    public interface ColorChangeEventListener extends IRadixEventListener<ColorChangeEvent> {

    }

    private final RadixEventSource<ColorChangeEventListener, ColorChangeEvent> source =
            new RadixEventSource<>();

    public void addColorChangeEventListener(final ColorChangeEventListener listener) {
        source.addEventListener(listener);
    }

    protected class ColorIcon extends ImageIcon {

        private final Color color;

        public ColorIcon(final Icon icon,final Color color) {
            super(((ImageIcon)icon).getImage());
            this.color = color;
        }

        @Override
        public synchronized void paintIcon(final Component c,final Graphics g,final int x,final int y) {
            super.paintIcon(c, g, x, y);
            g.setColor(color);
            g.fillRect(x, y + this.getIconHeight() - 4, this.getIconWidth(), 4);
        }

    }

    private boolean mouseInArrowArea = false;

    private final Map<String,Icon> regIcons = new HashMap<>( 5 );
    private final Map<String,Icon> arrowIcons = new HashMap<>( 5 );

    private static final String ICON_NORMAL = "normal"; //NOI18N
    private static final String ICON_PRESSED = "pressed"; //NOI18N
    private static final String ICON_ROLLOVER = "rollover"; //NOI18N
    private static final String ICON_ROLLOVER_SELECTED = "rolloverSelected"; //NOI18N
    private static final String ICON_SELECTED = "selected"; //NOI18N
    private static final String ICON_DISABLED = "disabled"; //NOI18N
    private static final String ICON_DISABLED_SELECTED = "disabledSelected"; //NOI18N

    private static final String ICON_ROLLOVER_LINE = "rolloverLine"; //NOI18N
    private static final String ICON_ROLLOVER_SELECTED_LINE = "rolloverSelectedLine"; //NOI18N

    private Color  color;
    private final Icon initIcon;

    /** Creates a new instance of MenuToggleButton */
    public ArrowColorButton( ImageIcon icon,final Color clr ) {
        assert null != icon;
        assert null != clr;

        color = clr;
        initIcon = icon;
        icon = new ColorIcon(icon, color);

        setIcon(icon);

        resetIcons();

        addMouseMotionListener( new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e ) {
                mouseInArrowArea = isInArrowArea( e.getPoint() );
                updateRollover( _getRolloverIcon(), _getRolloverSelectedIcon() );
            }
        });

        addMouseListener( new MouseAdapter() {
            private boolean popupMenuOperation = false;

            @Override
            public void mousePressed(final MouseEvent e ) {
                popupMenuOperation = false;
                
                if ( getModel() instanceof Model ) {
                    final Model model = (Model) getModel();
                    if ( !model._isPressed() ) {
                        if( isInArrowArea( e.getPoint() ) && isEnabled() ) {
                            model._press();

                            final JColorChooser colorChooser = new JColorChooser(color);
                            final Color tmp = JColorChooser.showDialog(ArrowColorButton.this, "Choose Color", color);
                            if (tmp != null) {
                                color = tmp;
                                setIcon(new ColorIcon(initIcon, color));
                                resetIcons();
                                source.fireEvent(new ColorChangeEvent(color));
                            }
                            if( getModel() instanceof Model ) {
                                ((Model)getModel())._release();
                            }

                            popupMenuOperation = true;
                        }
                    } else {
                        model._release();
                        popupMenuOperation = true;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // If we done something with the popup menu, we should consume
                // the event, otherwise the button's action will be triggered.
                if (popupMenuOperation) {
                    popupMenuOperation = false;
                    e.consume();
                }
            }

            @Override
            public void mouseEntered(final MouseEvent e ) {
                mouseInArrowArea = isInArrowArea( e.getPoint() );
                updateRollover( _getRolloverIcon(), _getRolloverSelectedIcon() );
            }

            @Override
            public void mouseExited(final MouseEvent e ) {
                mouseInArrowArea = false;
                updateRollover( _getRolloverIcon(), _getRolloverSelectedIcon() );
            }
        });

        setModel( new Model() );

        setBorderPainted(false);
        setMargin(new Insets(2, 2, 2, 2));
        setFocusable(false);
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                source.fireEvent(new ColorChangeEvent(color));
            }
        });

    }

    private void updateRollover( Icon rollover, Icon rolloverSelected ) {
//        rollover = new ColorIcon(rollover, color);
//        rolloverSelected = new ColorIcon(rolloverSelected, color);
        super.setRolloverIcon( rollover );
        super.setRolloverSelectedIcon( rolloverSelected );
    }

    private void resetIcons() {
        Icon icon = regIcons.get( ICON_NORMAL );
        if( null != icon )
            setIcon( icon );

        icon = regIcons.get( ICON_PRESSED );
        if( null != icon )
            setPressedIcon( icon );

        icon = regIcons.get( ICON_ROLLOVER );
        if( null != icon )
            setRolloverIcon( icon );

        icon = regIcons.get( ICON_ROLLOVER_SELECTED );
        if( null != icon )
            setRolloverSelectedIcon( icon );

        icon = regIcons.get( ICON_SELECTED );
        if( null != icon )
            setSelectedIcon( icon );

        icon = regIcons.get( ICON_DISABLED );
        if( null != icon )
            setDisabledIcon( icon );

        icon = regIcons.get( ICON_DISABLED_SELECTED );
        if( null != icon )
            setDisabledSelectedIcon( icon );
    }

    private Icon _getRolloverIcon() {
        Icon icon =  arrowIcons.get( mouseInArrowArea ? ICON_ROLLOVER : ICON_ROLLOVER_LINE );
        if( null == icon ) {
            Icon orig = regIcons.get( ICON_ROLLOVER );
            if( null == orig )
                orig = regIcons.get( ICON_NORMAL );
            icon = new IconWithArrow( orig, !mouseInArrowArea );
            arrowIcons.put( mouseInArrowArea ? ICON_ROLLOVER : ICON_ROLLOVER_LINE, icon );
        }
        return icon;
    }

    private Icon _getRolloverSelectedIcon() {
        Icon icon = arrowIcons.get( mouseInArrowArea ? ICON_ROLLOVER_SELECTED : ICON_ROLLOVER_SELECTED_LINE );
        if( null == icon ) {
            Icon orig = regIcons.get( ICON_ROLLOVER_SELECTED );
            if( null == orig )
                orig = regIcons.get( ICON_ROLLOVER );
            if( null == orig )
                orig = regIcons.get( ICON_NORMAL );
            icon = new IconWithArrow( orig, !mouseInArrowArea );
            arrowIcons.put( mouseInArrowArea ? ICON_ROLLOVER_SELECTED : ICON_ROLLOVER_SELECTED_LINE, icon );
        }
        return icon;
    }

    private boolean isInArrowArea( Point p ) {
        return p.getLocation().x >= getWidth() - IconWithArrow.getArrowAreaWidth() - getInsets().right;
    }

    @Override
    public void setIcon(Icon icon) {
        assert null != icon;
        icon = new ColorIcon(icon, color);
        Icon arrow = updateIcons( icon, ICON_NORMAL );
        arrowIcons.remove( ICON_ROLLOVER_LINE );
        arrowIcons.remove( ICON_ROLLOVER_SELECTED_LINE );
        arrowIcons.remove( ICON_ROLLOVER );
        arrowIcons.remove( ICON_ROLLOVER_SELECTED );
        super.setIcon( arrow );
    }

    private Icon updateIcons( Icon orig, String iconType ) {
        Icon arrow = null;
        if( null == orig ) {
            regIcons.remove( iconType );
            arrowIcons.remove( iconType );
        } else {
            orig = new ColorIcon(orig, color);
            regIcons.put( iconType, orig );
            arrow = new ImageIcon(ImageUtilities.icon2Image(new IconWithArrow( orig, false )));
            arrowIcons.put( iconType, arrow );
        }
        return arrow;
    }

    @Override
    public void setPressedIcon(Icon icon) {
        icon = new ColorIcon(icon, color);
        Icon arrow = updateIcons( icon, ICON_PRESSED );
        super.setPressedIcon( arrow );
    }

    @Override
    public void setSelectedIcon(Icon icon) {
        icon = new ColorIcon(icon, color);
        Icon arrow = updateIcons( icon, ICON_SELECTED );
        super.setSelectedIcon( arrow );
    }

    @Override
    public void setRolloverIcon(Icon icon) {
        icon = new ColorIcon(icon, color);
        Icon arrow = updateIcons( icon, ICON_ROLLOVER );
        arrowIcons.remove( ICON_ROLLOVER_LINE );
        arrowIcons.remove( ICON_ROLLOVER_SELECTED_LINE );
        super.setRolloverIcon( arrow );
    }

    @Override
    public void setRolloverSelectedIcon(Icon icon) {
        icon = new ColorIcon(icon, color);
        Icon arrow = updateIcons( icon, ICON_ROLLOVER_SELECTED );
        arrowIcons.remove( ICON_ROLLOVER_SELECTED_LINE );
        super.setRolloverSelectedIcon( arrow );
    }

    @Override
    public void setDisabledIcon(Icon icon) {
        icon = new ColorIcon(icon, color);
        Icon arrow = updateIcons( icon, ICON_DISABLED );
        super.setDisabledIcon( arrow );
    }

    @Override
    public void setDisabledSelectedIcon(Icon icon) {
        icon = new ColorIcon(icon, color);
        Icon arrow = updateIcons( icon, ICON_DISABLED_SELECTED );
        super.setDisabledSelectedIcon( arrow );
    }


    private class Model extends DefaultButtonModel {
        private boolean _pressed = false;

        @Override
        public void setPressed(final boolean b) {
            if( mouseInArrowArea || _pressed )
                return;
            super.setPressed( b );
        }

        public void _press() {
            if((isPressed()) || !isEnabled()) {
                return;
            }

            stateMask |= PRESSED + ARMED;

            fireStateChanged();
            _pressed = true;
        }

        public void _release() {
            _pressed = false;
            mouseInArrowArea = false;
            setArmed( false );
            setPressed( false );
            setRollover( false );
            setSelected( false );
        }

        public boolean _isPressed() {
            return _pressed;
        }

        @Override
        protected void fireStateChanged() {
            if( _pressed )
                return;
            super.fireStateChanged();
        }

        @Override
        public void setArmed(boolean b) {
            if( _pressed )
                return;
            super.setArmed(b);
        }

        @Override
        public void setEnabled(boolean b) {
            if( _pressed )
                return;
            super.setEnabled(b);
        }

        @Override
        public void setSelected(boolean b) {
            if( _pressed )
                return;
            super.setSelected(b);
        }

        @Override
        public void setRollover(boolean b) {
            if( _pressed )
                return;
            super.setRollover(b);
        }
    }

    
}
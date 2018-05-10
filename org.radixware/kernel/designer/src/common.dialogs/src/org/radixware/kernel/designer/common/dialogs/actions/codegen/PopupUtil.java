/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.common.dialogs.actions.codegen;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.openide.windows.WindowManager;

public final class PopupUtil  {
    
    // private static MyFocusListener mfl = new MyFocusListener();
    
    private static final String CLOSE_KEY = "CloseKey"; //NOI18N
    private static final Action CLOSE_ACTION = new CloseAction();
    private static final KeyStroke ESC_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ); 
        
    private static final String POPUP_NAME = "popupComponent"; //NOI18N
    private static JDialog popupWindow;
    private static Window owner;
    private static final HideAWTListener hideListener = new HideAWTListener();
    
    // Singleton
    private PopupUtil() {
    }
    
    
    public static void showPopup( JComponent content, Window parent, int x, int y, boolean undecorated, int altHeight ) {
        if (popupWindow != null ) {
            return; // Content already showing
        }
                           
        Toolkit.getDefaultToolkit().addAWTEventListener(hideListener, AWTEvent.MOUSE_EVENT_MASK);
        
        // NOT using PopupFactory
        // 1. on linux, creates mediumweight popup taht doesn't refresh behind visible glasspane
        // 2. on mac, needs an owner frame otherwise hiding tooltip also hides the popup. (linux requires no owner frame to force heavyweight)
        // 3. the created window is not focusable window
        owner = parent;
        popupWindow = new JDialog( parent );
        popupWindow.setName( POPUP_NAME );
        popupWindow.setUndecorated(undecorated);
        popupWindow.getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( ESC_KEY_STROKE, CLOSE_KEY );
        popupWindow.getRootPane().getActionMap().put( CLOSE_KEY, CLOSE_ACTION );
	
	//set a11y
	String a11yName = content.getAccessibleContext().getAccessibleName();
	if(a11yName != null && !a11yName.equals(""))
	    popupWindow.getAccessibleContext().setAccessibleName(a11yName);
	String a11yDesc = content.getAccessibleContext().getAccessibleDescription();
	if(a11yDesc != null && !a11yDesc.equals(""))
	    popupWindow.getAccessibleContext().setAccessibleDescription(a11yDesc);
	    
        popupWindow.getContentPane().add(content);                      
                
        getMainWindow().addWindowStateListener(hideListener);
        getMainWindow().addComponentListener(hideListener);
        resizePopup();
        
        if (x != (-1)) {
            Point p = fitToScreen( x, y, altHeight );
            Rectangle screen = org.openide.util.Utilities.getUsableScreenBounds();
            if (p.y < screen.y) {
                int yAdjustment = screen.y - p.y;
                p.y += yAdjustment;
                popupWindow.setSize(popupWindow.getWidth(), popupWindow.getHeight() - yAdjustment);
            }
            popupWindow.setLocation(p.x, p.y);
        }
        
        popupWindow.setVisible( true );
        content.requestFocus();
        content.requestFocusInWindow();
    }
    
    public static void hidePopup() {
        if (popupWindow != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(hideListener);
            
            popupWindow.setVisible( false );
            popupWindow.dispose();
        }
        getMainWindow().removeWindowStateListener(hideListener);
        getMainWindow().removeComponentListener(hideListener);
        popupWindow = null;
        owner = null;
    }

    
    private static void resizePopup() {
        popupWindow.pack();
        Point point = new Point(0,0);
        SwingUtilities.convertPointToScreen(point, getMainWindow());
        popupWindow.setLocation( point.x + (getMainWindow().getWidth() - popupWindow.getWidth()) / 2, 
                                 point.y + (getMainWindow().getHeight() - popupWindow.getHeight()) / 3);
    }
    
    private static final int X_INSET = 10;
    private static final int Y_INSET = X_INSET;
    
    private static Point fitToScreen( int x, int y, int altHeight ) {
        
        Rectangle screen = org.openide.util.Utilities.getUsableScreenBounds();
                
        Point p = new Point( x, y );
        
        // Adjust the x postition if necessary
        if ( ( p.x + popupWindow.getWidth() ) > ( screen.x + screen.width - X_INSET ) ) {
            p.x = screen.x + screen.width - X_INSET - popupWindow.getWidth(); 
        }
        
        // Adjust the y position if necessary
        if ( ( p.y + popupWindow.getHeight() ) > ( screen.y + screen.height - X_INSET ) ) {
            p.y = p.y - popupWindow.getHeight() - altHeight;
        }
        
        return p;     
    }

    
    private static Window getMainWindow() {
        return owner != null ? owner : WindowManager.getDefault().getMainWindow();
    }
    
    // Innerclasses ------------------------------------------------------------
    
    private static class HideAWTListener extends ComponentAdapter implements  AWTEventListener, WindowStateListener {
        
        @Override
        public void eventDispatched(java.awt.AWTEvent aWTEvent) {
            if (aWTEvent instanceof MouseEvent) {
                MouseEvent mv = (MouseEvent)aWTEvent;
                if (mv.getID() == MouseEvent.MOUSE_CLICKED && mv.getClickCount() > 0) {
                    if (! (aWTEvent.getSource() instanceof Component)) {
                        hidePopup();
                        return;
                    }
                    
                    Component comp = (Component)aWTEvent.getSource();
                    Container par = SwingUtilities.getAncestorNamed(POPUP_NAME, comp); //NOI18N
                    if ( par == null ) {
                        hidePopup();
                    }
                }
            }
        }

        @Override
        public void windowStateChanged(WindowEvent windowEvent) {
            if (popupWindow != null ) {
                int oldState = windowEvent.getOldState();
                int newState = windowEvent.getNewState();
            
                if (((oldState & Frame.ICONIFIED) == 0) &&
                    ((newState & Frame.ICONIFIED) == Frame.ICONIFIED)) {
                    hidePopup();
                }
            }

        }
        
        @Override
        public void componentResized(ComponentEvent evt) {
            if (popupWindow != null) {
                resizePopup();
            }
        }
        
        @Override
        public void componentMoved(ComponentEvent evt) {
            if (popupWindow!= null) {
                resizePopup();
            }
        }        
        
    }
    
    private static class MyFocusListener implements FocusListener {
        
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
        }

        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
        }
                        
    }
    
    private static class CloseAction extends AbstractAction {
        
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            hidePopup();
        }
                
                
    }
    
}
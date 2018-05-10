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

package org.radixware.kernel.explorer.webdriver;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QWidget;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class WebDrvKeyEvents {
    
    private final static Qt.KeyboardModifiers NO_MODIFIERS = new Qt.KeyboardModifiers(Qt.KeyboardModifier.NoModifier);
    
    private final static boolean ENABLE_ONE_KEY_RELEASE = System.getProperty("org.radixware.kernel.explorer.webdriver.enable-one-key-release")!=null;

    private final static Map<Qt.Key, Qt.KeyboardModifier> MODIFIER_BY_KEY = new EnumMap<>(Qt.Key.class);
    static {
        MODIFIER_BY_KEY.put(Qt.Key.Key_Alt, Qt.KeyboardModifier.AltModifier);
        MODIFIER_BY_KEY.put(Qt.Key.Key_Control, Qt.KeyboardModifier.ControlModifier);
        MODIFIER_BY_KEY.put(Qt.Key.Key_Shift, Qt.KeyboardModifier.ShiftModifier);
        MODIFIER_BY_KEY.put(Qt.Key.Key_Meta, Qt.KeyboardModifier.MetaModifier);
    }
        
    private final static List<Qt.Key> SPECIAL_WEBDRIVER_KEYS = Arrays.asList(
        Qt.Key.Key_unknown,             //\uE000
        Qt.Key.Key_Cancel,              //\uE001
        Qt.Key.Key_Help,                //\uE002
        Qt.Key.Key_Backspace,           //\uE003
        Qt.Key.Key_Tab,                 //\uE004
        Qt.Key.Key_Clear,               //\uE005
        Qt.Key.Key_Return,              //\uE006
        Qt.Key.Key_Enter,               //\uE007
        Qt.Key.Key_Shift,               //\uE008
        Qt.Key.Key_Control,             //\uE009
        Qt.Key.Key_Alt,                 //\uE00A
        Qt.Key.Key_Pause,               //\uE00B
        Qt.Key.Key_Escape,              //\uE00C
        Qt.Key.Key_Space,               //\uE00D
        Qt.Key.Key_PageUp,              //\uE00E
        Qt.Key.Key_PageDown,            //\uE00F
        Qt.Key.Key_End,                 //\uE010
        Qt.Key.Key_Home,                //\uE011
        Qt.Key.Key_Left,                //\uE012
        Qt.Key.Key_Up,                  //\uE013
        Qt.Key.Key_Right,               //\uE014
        Qt.Key.Key_Down,                //\uE015
        Qt.Key.Key_Insert,              //\uE016
        Qt.Key.Key_Delete,              //\uE017
        Qt.Key.Key_Semicolon,           //\uE018
        Qt.Key.Key_Equal,               //\uE019
        Qt.Key.Key_0,                   //\uE01A
        Qt.Key.Key_1,                   //\uE01B
        Qt.Key.Key_2,                   //\uE01C
        Qt.Key.Key_3,                   //\uE01D
        Qt.Key.Key_4,                   //\uE01E
        Qt.Key.Key_5,                   //\uE01F
        Qt.Key.Key_6,                   //\uE020
        Qt.Key.Key_7,                   //\uE021
        Qt.Key.Key_8,                   //\uE022
        Qt.Key.Key_9,                   //\uE023
        Qt.Key.Key_Asterisk,            //\uE024
        Qt.Key.Key_Plus,                //\uE025
        Qt.Key.Key_Comma,               //\uE026
        Qt.Key.Key_Minus,               //\uE027
        Qt.Key.Key_Period,              //\uE028
        Qt.Key.Key_Slash,               //\uE029
        Qt.Key.Key_F1,                  //\uE031
        Qt.Key.Key_F2,                  //\uE032
        Qt.Key.Key_F3,                  //\uE033
        Qt.Key.Key_F4,                  //\uE034
        Qt.Key.Key_F5,                  //\uE035
        Qt.Key.Key_F6,                  //\uE036
        Qt.Key.Key_F7,                  //\uE037
        Qt.Key.Key_F8,                  //\uE038
        Qt.Key.Key_F9,                  //\uE039
        Qt.Key.Key_F10,                 //\uE03A
        Qt.Key.Key_F11,                 //\uE03B
        Qt.Key.Key_F12,                 //\uE03C
        Qt.Key.Key_Meta,                //\uE03D
        Qt.Key.Key_Zenkaku_Hankaku,     //\uE040
        Qt.Key.Key_Shift,/*right*/      //\uE050
        Qt.Key.Key_Control,/*right*/    //\uE051
        Qt.Key.Key_Alt,/*right*/        //\uE052
        Qt.Key.Key_Meta,/*right*/       //\uE053
        Qt.Key.Key_PageUp,              //\uE054
        Qt.Key.Key_PageDown,            //\uE055
        Qt.Key.Key_End,                 //\uE056
        Qt.Key.Key_Home,                //\uE057
        Qt.Key.Key_Left,                //\uE058
        Qt.Key.Key_Up,                  //\uE059
        Qt.Key.Key_Right,               //\uE05A
        Qt.Key.Key_Down,                //\uE05B
        Qt.Key.Key_Insert,              //\uE05C
        Qt.Key.Key_Delete               //\uE05D
    );
    
    private final static char NULL_KEY = '\uE000';
            
    private final List<QKeyEvent> keyEvents = new LinkedList<>();
        
    private WebDrvKeyEvents(){
    }
    
    public static Qt.Key charCode2QKey(final char c){        
        final int charCode = (int)c;
        final int special_key_index = charCode - (int)NULL_KEY;
        if (special_key_index>=0 && special_key_index<SPECIAL_WEBDRIVER_KEYS.size()){
            return SPECIAL_WEBDRIVER_KEYS.get(special_key_index);
        }
        final int upperCaseCharCode = Character.toUpperCase(charCode);
        try{
            return Qt.Key.resolve(upperCaseCharCode);
        }catch(com.trolltech.qt.QNoSuchEnumValueException exception){
            
        }
        try{
            return Qt.Key.resolve(charCode);
        }catch(com.trolltech.qt.QNoSuchEnumValueException exception){
            
        }
        try{
            return Qt.Key.resolve(upperCaseCharCode+0x01000000);
        }catch(com.trolltech.qt.QNoSuchEnumValueException exception){
            
        }
        try{
            return Qt.Key.resolve(Character.toLowerCase(charCode)+0x01000000);
        }catch(com.trolltech.qt.QNoSuchEnumValueException exception){
            
        }
        return Qt.Key.Key_unknown;
    }
    
    public static Qt.KeyboardModifiers keys2modifiers(EnumSet<Qt.Key> keys){
        if (keys==null || keys.isEmpty()){
            return NO_MODIFIERS;
        }
        final Qt.KeyboardModifiers modifiers = new Qt.KeyboardModifiers(Qt.KeyboardModifier.NoModifier);
        for (Qt.Key key: keys){
            if (MODIFIER_BY_KEY.containsKey(key)){
                modifiers.set(MODIFIER_BY_KEY.get(key));
            }
        }
        return modifiers;
    }
    
    public static Qt.KeyboardModifier getModifierForKey(final Qt.Key key){
        return MODIFIER_BY_KEY.get(key);
    }
    
    public static boolean isPrintableChar(final char c){
        final Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
                c != KeyEvent.CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    public static WebDrvKeyEvents parse(final String keyEventsAsStr){        
        final WebDrvKeyEvents instance = new WebDrvKeyEvents();
        final EnumSet<Qt.Key> modifiers = EnumSet.noneOf(Qt.Key.class);
        char current_key;
        String key_symbol;
        boolean repeatPress, repeatRelease, generateKeyRelease;
        for (int i=0,count=keyEventsAsStr.length(); i<count; i++){
            
            current_key = keyEventsAsStr.charAt(i);
            
            if (current_key==NULL_KEY){                
                for (Qt.Key modifier: modifiers){
                    instance.addReleaseEvent(modifier);
                }
                modifiers.clear();       
            }else if (current_key!='\r'/*skip this key*/) {
                final Qt.Key qKey = charCode2QKey(current_key);
                if (MODIFIER_BY_KEY.containsKey(qKey)){
                    if (modifiers.contains(qKey)){
                        modifiers.remove(qKey);
                        instance.addReleaseEvent(qKey, modifiers);
                    }else{
                        modifiers.add(qKey);                        
                        instance.addPressEvent(qKey, modifiers);
                    }
                }else{
                    repeatPress = i > 0 && current_key == keyEventsAsStr.charAt(i-1);
                    repeatRelease = i < count -1 && current_key == keyEventsAsStr.charAt(i+1);
                    generateKeyRelease = !ENABLE_ONE_KEY_RELEASE || !repeatRelease;
                    key_symbol = isPrintableChar(current_key) ? String.valueOf(current_key) : null;
                    instance.addPressEvent(qKey, modifiers, key_symbol, repeatPress);
                    if (generateKeyRelease){
                        instance.addReleaseEvent(qKey, modifiers, key_symbol, repeatRelease);
                    }
                }
            }
        }
        for (Qt.Key modifier: modifiers){
            instance.addReleaseEvent(modifier);
        }        
        return instance;
    }
    
    public void post(final QWidget targetWidget){
        for (QKeyEvent event: keyEvents){
            QApplication.postEvent(targetWidget, event);
        }
    }
    
    private void addReleaseEvent(final Qt.Key key){
        addReleaseEvent(key, null);
    }
    
    private void addReleaseEvent(final Qt.Key key, final EnumSet<Qt.Key> modifiers){
        keyEvents.add(new QKeyEvent(QEvent.Type.KeyRelease, key.value(), keys2modifiers(modifiers)));
    }
        
    private void addPressEvent(final Qt.Key key, final EnumSet<Qt.Key> modifiers){
        keyEvents.add(new QKeyEvent(QEvent.Type.KeyPress, key.value(), keys2modifiers(modifiers)));
    }    
    
    private void addPressEvent(final Qt.Key key, final EnumSet<Qt.Key> modifiers, final String text, final boolean autoRep){
        keyEvents.add(new QKeyEvent(QEvent.Type.KeyPress, key.value(), keys2modifiers(modifiers), text, autoRep));
    }
    
    private void addReleaseEvent(final Qt.Key key, final EnumSet<Qt.Key> modifiers, final String text, final boolean autoRep){
        keyEvents.add(new QKeyEvent(QEvent.Type.KeyRelease, key.value(), keys2modifiers(modifiers), text, autoRep));
    }    
}

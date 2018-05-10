/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.wps.rwt.events;

import java.util.EnumSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;

public class HtmlEvent {

    public final static class Factory {
        
        private Factory(){            
        }

        public static HtmlEvent parseHtmlEventFromJsonString(final String jsonString) {
            if (jsonString==null || jsonString.isEmpty()){
                return null;
            }            
            
            final Map map;
            try {
                map = (Map)new JSONParser().parse(jsonString);
            } catch (ParseException ex) {                
                Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, "Failed to parse html event \'"+jsonString+"\'", ex);
                return null;
            }
            
            final String type = (String)map.get("type");
            if (type == null) {
                Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, "Failed to parse html event \'"+jsonString+"\'. Event type was not defined");
                return null;
            }
            final EHtmlEventType eventType;
            try{
                eventType = EHtmlEventType.valueOf(type.toUpperCase());
            }catch(IllegalArgumentException ex){
                Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, "Failed to parse html event \'"+jsonString+"\'", ex);
                return null;                
            }
            
            final int button = ((Long) map.get("button")).intValue();
            final String params = (String) map.get("params");
            final EnumSet<EKeyboardModifier> modifiers = getModifiers(map);
            
            switch(eventType){
                case CLICK:
                    return new ClickHtmlEvent(button, modifiers);
                case DOUBLECLICK:
                    return new DoubleClickHtmlEvent(button, modifiers);
                case MOUSEDOWN:
                    return new MouseButtonPressHtmlEvent(button, modifiers);
                case KEYUP:
                    return new KeyUpHtmlEvent(button, modifiers);
                case KEYDOWN:
                    return new KeyDownHtmlEvent(button, modifiers);
                case CONTEXTMENU:
                    return new ContextMenuHtmlEvent(button, modifiers, params);
                default:
                    Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, "Failed to parse html event \'"+jsonString+"\' Unsupported event type: \'"+eventType+"\'");
                    return null;
            }            
        }

        private static EnumSet<EKeyboardModifier> getModifiers(Map map) {
            final Object modifier = map.get("modifier");
            if (modifier instanceof JSONArray) {
                final JSONArray jArray = (JSONArray) modifier;
                final EnumSet<EKeyboardModifier> modifierList = EnumSet.noneOf(EKeyboardModifier.class);
                for (Object jArr : jArray) {
                    modifierList.add(EKeyboardModifier.valueOf(jArr.toString()));
                }
                return modifierList;
            } else {
                return null;
            }
        }
    }
}

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

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.enums.EKeyEvent;


public class InputEventFilter<T extends InputEventFilter> extends AbstractEventFilter<T> {

    private final List<ButtonAndModifiers> buttonAndModifiers = new LinkedList<>();

    public InputEventFilter(final EKeyboardModifier modifier, final EHtmlEventType type) {
        super(type);
        final EnumSet<EKeyboardModifier> modifiers = 
            modifier==null ? null : EnumSet.<EKeyboardModifier>of(modifier);
        buttonAndModifiers.add(new ButtonAndModifiers(1, modifiers));
    }

    public InputEventFilter(final EKeyEvent button, final EKeyboardModifier modifier, final EHtmlEventType type) {
        super(type);
        final EnumSet<EKeyboardModifier> modifiers = 
            modifier==null ? null : EnumSet.<EKeyboardModifier>of(modifier);
        buttonAndModifiers.add(new ButtonAndModifiers(button.getValue().intValue(), modifiers));
    }

    public InputEventFilter(final EnumSet<EKeyboardModifier> modifiers, final EHtmlEventType type) {
        super(type);
        buttonAndModifiers.add(new ButtonAndModifiers(1, modifiers));
    }

    public InputEventFilter(final EKeyEvent button, final EnumSet<EKeyboardModifier> modifiers, final EHtmlEventType type) {
        super(type);
        buttonAndModifiers.add(new ButtonAndModifiers(button.getValue().intValue(), modifiers));
    }

    public InputEventFilter(final EKeyEvent button, final EHtmlEventType type) {
        super(type);
        buttonAndModifiers.add(new ButtonAndModifiers(button.getValue().intValue(), null));
    }
    
    final List<ButtonAndModifiers> getButtonsAndModifiers(){
        return Collections.unmodifiableList(buttonAndModifiers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean merge(final T eventFilter) {
        boolean isChange = false;        
        final List<ButtonAndModifiers> params = eventFilter.getButtonsAndModifiers();
        for (ButtonAndModifiers btnAndMdfStruct : params) {
            if (!this.buttonAndModifiers.contains(btnAndMdfStruct)) {
                this.buttonAndModifiers.add(btnAndMdfStruct);
                isChange = true;
            }
        }
        return isChange;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean unmerge(final T eventFilter) {
        boolean isChange = false;
        final List<ButtonAndModifiers> params = eventFilter.getButtonsAndModifiers();
        for (ButtonAndModifiers btnAndMdfStruct : params) {
            if (this.buttonAndModifiers.contains(btnAndMdfStruct)) {
                this.buttonAndModifiers.remove(btnAndMdfStruct);
                isChange = true;
            }
        }
        return isChange;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toJSONString() {
        JSONArray jsonArr = new JSONArray();
        if (buttonAndModifiers != null && !buttonAndModifiers.isEmpty()) {
            JSONObject jsonObj;
            for (ButtonAndModifiers struct : buttonAndModifiers) {
                jsonObj = new JSONObject();
                jsonObj.put("button", struct.getButton());
                if (struct.getModifiers() != null && !struct.getModifiers().isEmpty()) {
                    final JSONArray modifiersArr = new JSONArray();
                    for (EKeyboardModifier keyboardModifier : struct.getModifiers()) {
                        if (keyboardModifier==EKeyboardModifier.ANY){
                            modifiersArr.clear();
                            modifiersArr.add(keyboardModifier.toString());
                            break;
                        }else{
                            modifiersArr.add(keyboardModifier.toString());
                        }
                    }
                    jsonObj.put("modifier", modifiersArr);
                } else {
                    jsonObj.put("modifier", null);
                }
                jsonArr.add(jsonObj);
            }
        }
        return jsonArr.toJSONString();
    }
}
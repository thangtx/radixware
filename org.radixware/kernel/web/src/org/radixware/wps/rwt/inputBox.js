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
$RWT.inputBox = {
    requestFocus: function(ib) {

        if (ib) {
            var tf = $RWT.inputBox.findInput(ib);

            if (tf) {
                $(tf).focus();
            }
        }
    },
    requestFocusTextArea: function(ib) {

        if (ib) {
            var tf = $RWT.inputBox._findTextArea(ib);

            if (tf) {
                $(tf).focus();
            }
        }
    },
    layout: function(box) {
        var table = WpsUtils.findChildByLocalName(box, 'table');
        if (table) {
            var h = $(box).innerHeight();
            var th = $(table).outerHeight();
            if (h > th) {
                $(table).css('height', th + "px");
            }
//             $(table).css('margin-top',diff/2);

        }
        box.maxSpinUpStepCount = box.getAttribute("maxSpinUpStepCount") == null ? 0 : parseInt(box.getAttribute("maxSpinUpStepCount"));
        box.maxSpinDownStepCount = box.getAttribute("maxSpinDownStepCount") == null ? 0 : parseInt(box.getAttribute("maxSpinDownStepCount"));
        var textField = $RWT.inputBox._findTextField(box);
        if (textField && textField.getAttribute("inputType") == "Int") {
            $(box).bind('keydown', 'up', eval("$RWT.inputBox.keyUp"));
            $(box).bind('keydown', 'down', eval("$RWT.inputBox.keyDown"));
            $(box).bind('keydown', 'pageup', eval("$RWT.inputBox.keyPageUp"));
            $(box).bind('keydown', 'pagedown', eval("$RWT.inputBox.keyPageDown"));
        }
        else if (textField && textField.getAttribute("inputType") == "Bool") {
            $(box).css('width', $(box).outerHeight() + 'px');
            $(box).bind('keydown', 'space', eval("$RWT.inputBox.keySpace"));
        } else if (textField && box.getAttribute("inputHandlers") != null) {
            var handlers = eval(box.getAttribute("inputHandlers"));
            var i;
            for (i in handlers) {
                var hotKey = handlers[i];
                if (hotKey == "wheel") {
                    box.handleMouseWheel = true;
                }
                else {
                    var handler =
                            new Function("event", "if (!$RWT.inputBox._eventWasCancelled(event)){$RWT.inputBox._cancelEvent(event); $RWT.actions.event(this,\'hotkey\',\'" + hotKey + "\');}");
                    $(box).bind('keydown', hotKey, handler);
                }
            }
        }
        $(box).bind('keydown', 'ctrl+space', eval("$RWT.inputBox.clearValue"));
    },
    setValue: function(box, value) {
        var input = $RWT.inputBox.findInput(box);
        box.setAttribute("focusedtext", value);
        input.value = value;
    },
    setBlockValueEvents: function(box, block) {
        var textField = $RWT.inputBox._findTextField(box);
        textField.eventsBlocked = block;
    },
    setChangeValueHandler: function(box, handler) {
        box.changeValueHandler = handler;
    },
    getValue: function(box) {
        var input = $RWT.inputBox.findInput(box);
        return input.value;
    },
    _findTextFieldByButton: function(button) {
        var tr = button.parentNode.parentNode;
        var path = ['td', 'div', 'div', 'input'];
        var res = tr;
        var result;
        for (var i in path) {
            res = WpsUtils.findChildByLocalName(res, path[i]);
            if (!res)
                return null;
            result = res;
        }
        return result;
    },
    _findTextFieldCell: function(box) {
        var result;
        var path = ['table', 'tbody', 'tr', 'td'];
        var res = box;
        for (var i in path) {
            res = WpsUtils.findChildByLocalName(res, path[i]);
            if (!res)
                return null;
            result = res;
        }
        if (result != null && result.className != null && result.className.indexOf('rwt-value-icon') > 0) {
            return WpsUtils.findNextSibling(result, 'td');
        }
        if (result != null && result.className != null && result.className.indexOf('rwt-value-label') > 0) {
            return WpsUtils.findNextSibling(result, 'td');
        }
        return result;
    }
    ,
    _findTextField: function(box) {
        var result;
        var path = ['table', 'tbody', 'tr', 'td', 'div'];
        var res = box;
        for (var i in path) {
            res = WpsUtils.findChildByLocalName(res, path[i]);
            if (!res)
                return null;
            result = res;
            if (path[i] === 'td' && result.className != null && result.className.indexOf('rwt-value-icon') > 0) {
                res = WpsUtils.findNextSibling(result, 'td');
                result = res;
            }
            if (path[i] === 'td' && result.className != null && result.className.indexOf('rwt-value-label') > 0) {
                res = WpsUtils.findNextSibling(result, 'td');
                result = res;
            }
        }
        return result;
    }
    ,
    findInput: function(box) {
        var result;
        var path = ['table', 'tbody', 'tr', 'td', 'div', 'div', 'input'];
        var res = box;
        for (var i in path) {
            res = WpsUtils.findChildByLocalName(res, path[i]);
            if (!res)
                return null;
            result = res;
            if (path[i] === 'td' && result.className != null && result.className.indexOf('rwt-value-icon') > 0) {
                result = WpsUtils.findNextSibling(result, 'td');
                res = result;
            }
            if (path[i] === 'td' && result.className != null && result.className.indexOf('rwt-value-label') > 0) {
                result = WpsUtils.findNextSibling(result, 'td');
                res = result;
            }
        }
        return result;
    },
    _findTextArea: function(box) {
        var result;
        var path = ['table', 'tbody', 'tr', 'td', 'div', 'div', 'textarea'];
        var res = box;
        for (var i in path) {
            res = WpsUtils.findChildByLocalName(res, path[i]);
            if (!res)
                return null;
            result = res;
            if (path[i] === 'td' && result.className != null && result.className.indexOf('rwt-value-icon') > 0) {
                result = WpsUtils.findNextSibling(result, 'td');
                res = result;
            }
            if (path[i] === 'td' && result.className != null && result.className.indexOf('rwt-value-label') > 0) {
                result = WpsUtils.findNextSibling(result, 'td');
                res = result;
            }
        }
        return result;
    },
    textFieldFocusIn: function() {

        var inputBox = WpsUtils.findParentByClassName(this, "rwt-input-box");//upper div
        if (!this.readOnly) {
            var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");//first inner div
            if (!textField) {
                var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
                if (e && e.getAttribute("name") == "propTextEditor") {
                    textField = e;
                }
            }
            var focusedtext;
            var isvalid;
            if (inputBox) {
                focusedtext = inputBox.getAttribute("focusedtext");
                if (focusedtext) {
                    focusedtext = focusedtext.replace(/<br\s*[\/]?>/gi, "\n");
                }
                isvalid = inputBox.getAttribute("isvalid");
            }
            var value_is_null = textField.getAttribute("value_is_null");
            if (!inputBox.hasFocus
                    || focusedtext != inputBox.focusedText
                    || value_is_null != inputBox.value_is_null
                    || isvalid != inputBox.isvalid
                    ) {
                if (focusedtext != null && focusedtext != this.value) {
                    inputBox.unfocusedtext = this.value;
                    this.value = focusedtext;
                }
                else {
                    inputBox.unfocusedtext = null;
                }
                if (value_is_null == "true") {
                    inputBox.wasNullValue = true;
                    if (!inputBox.oldColor) {
                        inputBox.oldColor = $(this).css('color');
                    }
                    if ($(this).hasClass("rwt-undefined-value")) {
                        $(this).removeClass("rwt-undefined-value");
                    }
                    if (textField.getAttribute("editcolor") != null) {
                        $(this).css("color", textField.getAttribute("editcolor"));
                    } else {
                        //request for edit mode color
                        setTimeout(function() {
                            $RWT.actions.event(inputBox, 'wasNullValue', 'true')
                        }, 0);
                    }
                } else {
                    inputBox.wasNullValue = false;
                }
                if (isvalid == "false") {
                    inputBox.wasInvalid = true;
                    $(this).removeClass("rwt-invalid-value");
                    $(textField).removeClass("rwt-invalid-value");
                }
                else {
                    inputBox.wasInvalid = false;
                }
                inputBox.focusedText = focusedtext;
                inputBox.value_is_null = value_is_null;
                inputBox.isvalid = isvalid;
            }
        }
        inputBox.hasFocus = true;
        inputBox.setAttribute("focused", "true");
    },
    textFieldFocusOut: function() {
        var inputBox = WpsUtils.findParentByClassName(this, "rwt-input-box");
        var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
        if (!textField) {
            var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
            if (e && e.getAttribute("name") == "propTextEditor")
                textField = e;
        }
        if ($(this).attr("value") != textField.oldText && !textField.getAttribute("value_is_null")) {
            textField.wasChanged = true;
            $(this).attr("value", this.value);

            textField.getAttribute("value_is_null", (!textField.oldText || textField.oldText == ''));
            inputBox.value_is_null = (!textField.oldText || textField.oldText == '');
        }

        if (textField.wasChanged != null && !textField.wasChanged) {
            if (inputBox.unfocusedtext != null) {
                this.value = inputBox.unfocusedtext;
            }
            if (inputBox.wasInvalid != null && inputBox.wasInvalid) {
                $(this).addClass("rwt-invalid-value");
                $(textField).addClass("rwt-invalid-value");
            }
            if (inputBox.value_is_null != null && inputBox.value_is_null) {
                if (!$(this).hasClass("rwt-undefined-value")) {
                    $(this).addClass("rwt-undefined-value");
                }
                if (inputBox.oldColor != null && inputBox.oldColor) {
                    $(this).css('color', inputBox.oldColor);
                }
                else {
                    setTimeout(function() {
                        $RWT.actions.event(inputBox, 'wasNullValue', 'false')
                    }, 0);//focus out
                }
            }
        }
        inputBox.hasFocus = false;
        inputBox.setAttribute("focused", "false");
        if (inputBox.changeValueHandler != null) {
            inputBox.changeValueHandler(inputBox, this.value);
        }
    },
    keyUp: function(event) {
        $RWT.inputBox._changeValueOnHotKey(this, event, 'get-next', 1);
    },
    keyDown: function(event) {
        $RWT.inputBox._changeValueOnHotKey(this, event, 'get-prev', -1);
    },
    keyPageUp: function(event) {
        $RWT.inputBox._changeValueOnHotKey(this, event, 'get-next10', 10);
    },
    keyPageDown: function(event) {
        $RWT.inputBox._changeValueOnHotKey(this, event, 'get-prev10', -10);
    },
    mousewheel: function(event) {
        var input = $RWT.inputBox.findInput(this);
        if (input != null && !input.readOnly && !input.disabled) {
            var delta = 0;
            if (event.wheelDelta) { /* IE/Opera. */
                delta = event.wheelDelta / 120;
            } else if (event.detail) { /** Mozilla case. */
                /** In Mozilla, sign of delta is different than in IE.
                 * Also, delta is multiple of 3.
                 */
                delta = -event.detail / 3;
            }
            /* Basically, delta is now positive if wheel was scrolled up,
             * and negative, if wheel was scrolled down.
             */
            delta = delta > 0 ? 1 : -1;
            if (!$RWT.inputBox._changeValueOnWheel(this, event, delta) && this.handleMouseWheel == true) {
                $RWT.actions.event(this, 'mousewheel', String(delta));
            }
        }
    },
    dblclick: function(event) {
        if (event.target && event.target.tagName == "INPUT") {
            var result;
            var path = ['table', 'tbody', 'tr'];
            var res = this;
            for (var i in path) {
                res = WpsUtils.findChildByLocalName(res, path[i]);
                if (!res)
                    return null;
                result = res;
            }
            if (result) {
                WpsUtils.iterateNames(result, 'td', function(td) {
                    var button = WpsUtils.findFirstChild(td);
                    if (button && button.className &&
                            button.className.indexOf('rwt-tool-button') > 0 && button.className.indexOf('rwt-clear-button') < 0 && button.className.indexOf('rwt-input-box-spin') < 0) {
                        if ($(button).is(":visible") && !button.attributes.disabled && button.onclick) {
                            button.onclick();
                        }
                    }
                });
            }
        }
    },
    checkBoxClick: function(event) {
        var input = $RWT.inputBox.findInput(this);
        var textField = $RWT.inputBox._findTextField(this);
        if (textField.inputType === "Bool") {
            event.preventDefault ? event.preventDefault() : event.returnValue = false;
            if (!input.readOnly && !input.disabled) {
                $RWT.inputBox._changeCheckBoxValue(this, textField, input);
            }
        }
    },
    keySpace: function(event) {
        var wasPrevented = (event.isDefaultPrevented && event.isDefaultPrevented()) || event.returnValue == false;
        if (!wasPrevented) {
            var input = $RWT.inputBox.findInput(this);
            var isModifier = event.altKey || event.ctrlKey || event.metaKey || event.shiftKey;
            if (input && !isModifier && !input.readOnly && !input.disabled) {
                $RWT.inputBox._cancelEvent(event);
                var textField = $RWT.inputBox._findTextField(this);
                $RWT.inputBox._changeCheckBoxValue(this, textField, input);
            }
        }
    },
    clearValue: function(event) {
        var wasPrevented = (event.isDefaultPrevented && event.isDefaultPrevented()) || event.returnValue == false;
        if (!wasPrevented) {
            var input = $RWT.inputBox.findInput(this);
            if (this.getAttribute("is_clearable") == "true" && !input.readOnly && !input.disabled) {
                input.blur();
                $RWT.inputBox._cancelEvent(event);
                $RWT.actions.event(this, 'clear', '');
            }
        }
    },
    _changeCheckBoxValue: function(box, textField, input) {
        if (input.value == "\u25FC") {//null
            $RWT.actions.event(input, 'change', "true");
        }
        else if (input.value == " ") {
            var canBeNull = box.getAttribute("is_clearable") == "true";
            $RWT.actions.event(input, 'change', canBeNull ? "" : "true");
        }
        else {
            $RWT.actions.event(input, 'change', "false");
        }
    },
    _changeValueOnHotKey: function(box, event, action, delta) {
        var wasPrevented = (event.isDefaultPrevented && event.isDefaultPrevented()) || event.returnValue == false;
        if (!wasPrevented) {
            var input = $RWT.inputBox.findInput(box);
            var isModifier = event.altKey || event.ctrlKey || event.metaKey || event.shiftKey;
            if (input && !isModifier && !input.readOnly && !input.disabled) {
                if ((delta > 0 && delta <= box.maxSpinUpStepCount) || (delta < 0 && -delta <= box.maxSpinDownStepCount)) {
                    $RWT.inputBox._cancelEvent(event);
                    $RWT.actions.event(box, action, input.value);
                }
            }
        }
    },
    _cancelEvent: function(event) {
        $RWT.events.cancelEvent(event);
    },
    _eventWasCancelled: function(event) {
        if (event.isDefaultPrevented && event.isDefaultPrevented()) {
            return true;
        }
        if (event.isPropagationStopped && event.isPropagationStopped()) {
            return true;
        }
        return false;
    },
    _changeValueOnWheel: function(box, event, delta) {
        if ((delta > 0 && delta <= box.maxSpinUpStepCount) || (delta < 0 && -delta <= box.maxSpinDownStepCount)) {
            if (event.shiftKey) {
                delta *= 10;
            }
            if (box.hasFocus) {
                var input = $RWT.inputBox.findInput(box);
                $RWT.inputBox._findTextField(box).wasChanged = true;
                if (delta > 0) {
                    $RWT.actions.event(box, event.shiftKey ? 'get-next10' : 'get-next', input.value);
                }
                else {
                    $RWT.actions.event(box, event.shiftKey ? 'get-prev10' : 'get-prev', input.value);
                }
            }
            else {
                $RWT.actions.event(box, 'increment', String(delta));
            }
            return true;
        }
        return false;
    }
}

$RWT.textField = {
    layout: function(tf) {
        var div = WpsUtils.findChildByLocalName(tf, 'div');
        var input = WpsUtils.findChildByLocalName(div, 'input');
        if (input != null && input.hidden) {
            return;
        }
        input.disabled = !WpsUtils.isEnabled(input);
        if (div) {
            var h = $(tf).innerHeight();
            if (input) {
                var ih = $(input).outerHeight();
                var diff = h - ih;
                $(input).css('position', 'relative');
                $(input).css('height', ih);
                $(input).css('top', diff / 2);
                $(input).css('padding-top', 0);
                $(input).css('padding-bottom', 0);
            }
            $(div).css('height', h);
        }
        tf.inputType = tf.getAttribute("inputtype") == null ? "Text" : tf.getAttribute("inputtype");
        tf.isPassword = tf.getAttribute("isPassword") == 'true';
        tf.initialText = tf.getAttribute("initialtext");
        if (tf.initialText != null && tf.initialText == input.value) {
            tf.inModificationState = false;
        }
        tf.repeatStartModification = tf.getAttribute("repeatStartModification") == 'true';
        if (tf.inputType == "Int") {
            tf.maxDigitsCount = tf.getAttribute("maxDigitsCount") == null ? 0 : parseInt(tf.getAttribute("maxDigitsCount"));
            tf.canBeNegative = tf.getAttribute("canBeNegative") == null ? true : tf.getAttribute("canBeNegative") == "true";
            tf.canBePositive = tf.getAttribute("canBePositive") == null ? true : tf.getAttribute("canBePositive") == "true";
            tf.radix = tf.getAttribute("radix") == null ? 10 : parseInt(tf.getAttribute("radix"));
        }
        else if (tf.inputType == "Num") {
            tf.precision = tf.getAttribute("precision") == null ? -1 : parseInt(tf.getAttribute("precision"));
            tf.isIntegerNumber = tf.getAttribute("isIntegerNumber") == 'true';
            tf.radix = tf.getAttribute("radix") == null ? 10 : parseInt(tf.getAttribute("radix"));
            var symbols = {plusSign: tf.getAttribute("plusSign"),
                minusSign: tf.getAttribute("minusSign"),
                decimalPoint: tf.getAttribute("decimalDelimeter"),
                triadDelimeter: tf.getAttribute("triadDelimeter")};
            var restrictions = {canBeNegative: tf.getAttribute("canBeNegative") == null ? true : tf.getAttribute("canBeNegative") == "true",
                canBePositive: tf.getAttribute("canBePositive") == null ? true : tf.getAttribute("canBePositive") == "true",
                maxIntDigitsCount: tf.getAttribute("maxIntDigitsCount") == null ? null : parseInt(tf.getAttribute("maxIntDigitsCount"))
            };
            tf.symbols = symbols;
            tf.restrictions = restrictions;
        }
        else if (tf.inputType == "Bool") {
            //$(tf).width(tf.parentNode.innerWidth-2);
            //$(tf).height(tf.parentNode.innerHeight-2);
            $(tf).css("margin-top", "0px");
            $(tf).css("margin-bottom", "0px");
            $(tf).css("margin-right", "0px");
            $(tf).css("margin-left", "2px");
            $(tf).css("padding", "0px");
            $(tf).css("width", "14px");
            $(tf).css("height", "18px");
            $(tf).css("vertical-align", "top");
            $(div).css("margin", "0px");
            $(div).css("padding", "0px");
            $(div).css("width", "14px");
            $(div).css("height", "18px");
            $(div).css("vertical-align", "top");
            $(input).css("text-align", "center");
            $(input).css("font-size", "14px");
            $(input).css("padding", "0px");
            $(input).css("top", "0px");//safari bug fix
            
            if ($RWT.BrowserDetect.browser == 'Safari') {
                $(input).css("margin-top", "2px");
                $(input).css("margin-left", "-1px");
            }
            else {
                $(input).css("margin-top", "0px");
                $(input).css("margin-left", "0px");
            }
            $(input).css("margin-right", "0px");
            $(input).css("margin-bottom", "0px");
            $(input).css("vertical-align", "top");
            $(input).css("width", "14px");
            $(input).css("height", "16px");
            $(input).css("cursor", "default");
            input.tabIndex = -1;
            tf.canBeNull = tf.getAttribute("canBeNull") == null ? false : tf.getAttribute("canBeNull") == "true";
        }
        else if (tf.inputType == "Mask") {
            if (tf.inputMask != tf.getAttribute("pattern")) {
                tf.inputMask = tf.getAttribute("pattern");
                var maskData = $RWT.inputMaskUtils.parse(tf.inputMask);
                input.tokens = maskData.tokens;
                input.blankChar = maskData.blankChar;
                input.maxlength = maskData.tokens.length;
            }
        }
        if (tf.isPassword && input.type == 'text' && tf.getAttribute("value_is_null") != 'true') {
            $RWT.textField.__changeInputTypeToPassword(input);
        }
        input.rwt_f_finishEdit = $RWT.textField.rwt_f_finishEdit;
    }, //c = f.valHooks[e.nodeName.toLowerCase()] || f.valHooks[e.type];
    clientTextChange: function(e) {
        $RWT.actions.client_event(this, 'change', $(this).val());
    },
    textChange: function() {
        $RWT.actions.event(this, 'change', $(this).val());
    },
    drop: function(event) {
        var insertingText = event.dataTransfer.getData('Text');
        var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
        if (!textField) {
            var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
            if (e && e.getAttribute("name") == "propTextEditor")
                textField = e;
        }
        if (textField != null && insertingText != null && insertingText.length > 0) {
            var valueChanged = false;
            if (textField.inputType == 'Mask') {
                var curPos = $RWT.textField.caret($(this));
                $RWT.textField._processPasteWithMask(this, curPos, this.value, insertingText);
                valueChanged = true;
            }
            else if (textField.inputType == 'Bin' && $RWT.parseUtils.isValidHexString(insertingText)) {
                this.value = $RWT.textField._calcResultText(this, insertingText);
                valueChanged = true;
            } else if (textField.inputType == 'Num') {
                var curPos = $RWT.textField.caret($(this));
                var resultText = $RWT.textField._calcResultText(this, insertingText);
                var formattedText = $RWT.parseUtils.formatNumber(resultText,
                        textField.isIntegerNumber,
                        textField.radix,
                        textField.symbols,
                        textField.restrictions);
                if (formattedText !== null) {
                    this.value = formattedText;
                    var caretPos = curPos.begin + insertingText.length;
                    caretPos = $RWT.parseUtils.calcNewCursorPosition(resultText, formattedText, caretPos,
                            textField.isIntegerNumber, false, textField.symbols);
                    if (caretPos < formattedText.length - 1) {
                        $RWT.textField.caret($(this), caretPos);
                    }
                    valueChanged = true
                }
            } else {
                var resultText = $RWT.textField._calcResultText(this, insertingText);
                if ($RWT.textField._validate(textField, resultText)) {
                    this.value = resultText;
                    valueChanged = true;
                }
            }
            if (valueChanged) {
                $RWT.textField._onChangeValue(this);
            }
        }
        $RWT.events.cancelEvent(event);
    },
    dragEnter: function(event) {
        var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
        if (!textField) {
            var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
            if (e && e.getAttribute("name") == "propTextEditor")
                textField = e;
        }
        if (textField.inputType == 'Bool') {
            event.preventDefault ? event.preventDefault() : event.returnValue = false;
            return false;
        }
        $(this).focus();
    },
    dragLeave: function() {
        $(this).blur();
    },
    dragOver: function(event) {
        // the dragover event needs to be canceled to allow firing the drop event
        event.preventDefault ? event.preventDefault() : event.returnValue = false;
        var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
        if (!textField) {
            var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
            if (e && e.getAttribute("name") == "propTextEditor")
                textField = e;
        }
        if (textField.inputType == 'Bool') {
            return false;
        }
        $(this).focus();
    },
    focusIn: function(event) {
        var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
        if (!textField) {
            var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
            if (e && e.getAttribute("name") == "propTextEditor") {
                textField = e;
            }
        }
        if (textField.ignoreFocusIn == true) {//call during startModification or discardModification event                      
            textField.ignoreFocusIn = false;
            $RWT.registerFocuseEvent(event);
            return;
        }
        if (textField.isPassword && this.type == 'text') {
            var input = this;
            $RWT.textField.__changeInputTypeToPassword(input).focus();
            return;
        }
        if (this.rwt_f_focusIn) {
            this.rwt_f_focusIn();
        }
        if (!this.readOnly) {
            textField.oldText = $(this).val();
            textField.isValid = true;
            textField.lastChange = null;
            if (textField.inputType != 'Bool') {
                attrUpdate(this, 'oninput', '$RWT.textField._onInput');
                if ($RWT.BrowserDetect.browser == 'Explorer') {
                    attrUpdate(this, 'onkeyup', '$RWT.textField._onKeyUp');
                }
            }
            if (textField.inputType == "Mask") {
                $RWT.inputMaskUtils.updateCursorPosition(this, true);
            }
        }
        $RWT.registerFocuseEvent(event);
    },
    __changeInputTypeToPassword: function(input) {
        var pwdInput = input.cloneNode(true);
        pwdInput.type = 'password';

        pwdInput.rwt_f_focusIn = input.rwt_f_focusIn;
        pwdInput.originalInput = input;
        input.onblur = null;
        input.hidden = true;
        $(input).unbind('blur').hide().val("");
        $(input.parentNode).append(pwdInput);
        input.pwdInput = pwdInput;

        attrUpdate(pwdInput, 'onfocus', '$RWT.textField.focusIn');
        attrUpdate(pwdInput, 'onblur', '$RWT.textField.focusOut');
        attrUpdate(pwdInput, 'onkeypress', '$RWT.textField.keyPress');
        //attrUpdate(pwdInput, 'onpaste', '$RWT.textField.pasteText');
        attrUpdate(pwdInput, 'oncut', '$RWT.textField.cutText');
        attrUpdate(pwdInput, 'oninput', '$RWT.textField._onInput');
        attrUpdate(pwdInput, 'onclick', '$RWT.textField.onCursorPositionChanged');
        attrUpdate(pwdInput, 'onselect', '$RWT.textField.onCursorPositionChanged');
        attrUpdate(pwdInput, 'onmousedown', '$RWT.textField.onCursorPositionChanged');
        attrUpdate(pwdInput, 'onkeydown', '$RWT.textField.keyDown');
        attrUpdate(pwdInput, 'onkeyup', '$RWT.textField._onKeyUp');
        pwdInput.rwt_f_finishEdit = input.rwt_f_finishEdit;

        input.rwt_f_onChangeValue = $RWT.textField.rwt_f_onChangeValue;
        return pwdInput;
    },
    rwt_f_onChangeValue: function(node, newValue) {
        if (node.pwdInput != null) {
            node.pwdInput.value = newValue;
        }
    },
    rwt_f_finishEdit: function(input, discard) {
        var textField = WpsUtils.findParentByClassName(input, "rwt-text-field");
        if (!textField) {
            var e = WpsUtils.findParentByClassName(input, "rwt-text-area");
            if (e && e.getAttribute("name") == "propTextEditor")
                textField = e;
        }
        if (textField == null) {
            return;//one more call for password input
        }
        var inputValue = null;
        if (!input.readOnly) {
            attrUpdate(input, 'oninput', null);
            attrUpdate(input, 'onkeyup', null);
            inputValue = input.value;
            if (textField.oldText != null) {
                if (discard != null && discard == true) {
                    input.value = textField.oldText;
                    textField.wasChanged = false;
                }
                else if ((textField.inputType == 'Int' || textField.inputType == 'Num') && ($(input).val() === '-' || $(input).val() === '.')) {
                    input.value = textField.oldText;
                    textField.wasChanged = false;
                }
                else if (textField.wasChanged == false || textField.wasChanged == null) {
                    textField.wasChanged = !(textField.oldText === $(input).val());
                }
            }
        }
        if (input.type == 'password' && input.originalInput != null) {
            var originalInput = input.originalInput;
            var pwd = $(input).val();
            var wasChanged = textField.wasChanged != null && textField.wasChanged;
            originalInput.wasChanged = textField.wasChanged;
            if (textField.getAttribute("value_is_null") == 'true' || wasChanged) {
                originalInput.onblur = input.onblur;
                $(originalInput).bind('blur', $RWT.textField.focusOut);
                $(input).hide().remove();
                if (originalInput.rwt_f_focusOut) {
                    originalInput.rwt_f_focusOut();
                }
                originalInput.pwdInput = null;
                attrUpdate(originalInput, 'onchange', null);
                originalInput.hidden = false;
                $(originalInput).show();
            }
            if (wasChanged) {
                if (originalInput.rwt_f_onchange) {
                    originalInput.rwt_f_onchange(originalInput);
                }
                if (textField.eventsBlocked == null || !textField.eventsBlocked) {
                    $RWT.actions.event(originalInput, 'change', pwd);
                }

            }
        }
        else {
            if (input.rwt_f_focusOut && (discard == null)) {
                input.rwt_f_focusOut();
                if ($(input).attr("value") != inputValue && !textField.getAttribute("value_is_null")) {
                    textField.wasChanged = true;
                    $(input).attr("value", inputValue);
                    textField.getAttribute("value_is_null", (!inputValue || inputValue == ''));
                }
            }
            if (textField.wasChanged != null && textField.wasChanged) {
                if (input.rwt_f_onchange) {
                    input.rwt_f_onchange(input);
                }
                if (textField.eventsBlocked == null || !textField.eventsBlocked) {
                    var newValue = $(input).val();
                    //optimization: prevent double processing of the same change event
                    if (textField.lastChange == null || !(textField.lastChange === newValue)) {
                        textField.lastChange = newValue;//textField.olValue can be updated only on focus-in event
                        $RWT.actions.event(input, 'change', newValue);
                    }
                }
            } else {
                var canceledChanges = textField.inModificationState == true
                        && textField.oldText != null
                        && (textField.initialText == null || textField.initialText == textField.oldText);
                if (canceledChanges) {
                    $RWT.actions.event(input, 'discardModification');
                }
                textField.oldText = null;
            }
        }
        if (textField.initialText == null || textField.initialText == inputValue) {
            textField.inModificationState = false;
        }
        textField.ignoreFocusIn = false;
        textField.wasChanged = null;
    },
    focusOut: function() {
        $RWT.textField.rwt_f_finishEdit(this);
    },
    _onInput: function(event) {
        $RWT.textField._onChangeValue(this);
    },
    _onChangeValue: function(input, newValue) {
        var textField = WpsUtils.findParentByClassName(input, "rwt-text-field");
        if (textField != null) {
            var initialText = textField.initialText == null ? textField.oldText : textField.initialText;
            var newText = newValue == null ? input.value : newValue;
            if (initialText != null) {
                if ((textField.inModificationState != true/*null or false*/ || textField.repeatStartModification==true) && initialText != newText) {
                    textField.inModificationState = true;
                    textField.ignoreFocusIn = true;
                    textField.repeatStartModification = false;
                    var t = setTimeout(function() {
                        $RWT.actions.event(input, 'startModification', newText);
                    }, 0);
                } else if (textField.inModificationState == true && initialText == newText) {
                    textField.inModificationState = false;
                    textField.ignoreFocusIn = true;
                    $RWT.actions.event(input, 'discardModification');
                }
            }
        }
    },
    keyPress: function(event) {
        var isModifier = event.altKey || event.ctrlKey || event.metaKey;
        if (!isModifier && !this.readOnly) {
            var charCode = null;
            if (event.which == null) {
                charCode = event.keyCode; // Old IE
            }
            else if (event.which != 0 && event.charCode != 0) {
                charCode = event.which; // All others
            }
            else {
                // special key
            }
            if (charCode != null) {
                var ch = String.fromCharCode(charCode);
                var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
                if (!textField) {
                    var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
                    if (e && e.getAttribute("name") == "propTextEditor")
                        textField = e;
                }
                var isValid = true;
                var valueChanged = false;
                if (textField.inputType === 'Mask') {
                    var pos = $RWT.textField.caret($(this));
                    var result =
                            $RWT.inputMaskUtils.putChar(ch, pos.begin, pos.end, this.value, this.tokens, this.blankChar, false);
                    this.value = result.newString;
                    if (result.pos > -1) {
                        $RWT.inputMaskUtils.moveCursorToRight(this, result.pos);
                    }
                    else {
                        $RWT.inputMaskUtils.moveCursorToRight(this, pos.begin - 1);
                    }
                    valueChanged = true;
                    isValid = false;//do not process event
                } else if (textField.inputType === 'Num') {
                    $RWT.events.cancelEvent(event);
                    var curPos = $RWT.textField.caret($(this));
                    var resultText = $RWT.textField._calcResultText(this, ch);
                    var formattedText = $RWT.parseUtils.formatNumber(resultText,
                            textField.isIntegerNumber,
                            textField.radix,
                            textField.symbols,
                            textField.restrictions);
                    if (formattedText !== null) {
                        this.value = formattedText;
                        var caretPos = $RWT.parseUtils.calcNewCursorPosition(resultText, formattedText, curPos.begin + 1,
                                textField.isIntegerNumber, false, textField.symbols);
                        if (caretPos < formattedText.length) {
                            $RWT.textField.caret($(this), caretPos);
                        }
                        valueChanged = true;
                    }
                } else if (textField.inputType === 'Bin') {
                    isValid = $RWT.parseUtils.isValidHexString(ch);
                }
                else {
                    var resultText = $RWT.textField._calcResultText(this, ch);
                    isValid = (resultText === '-' && textField.canBeNegative)
                            || $RWT.textField._validate(textField, resultText);
                }
                if (!isValid) {
                    $RWT.events.cancelEvent(event);
                }
                if (valueChanged) {
                    $RWT.textField._onChangeValue(this);
                }
            }
        }
    },
    keyDown: function(event) {
        var ESCAPE_KEYCODE = 27;
        var ENTER_KEYCODE = 13;
        var RIGHT_ARROW_KEYCODE = 39;
        var END_KEYCODE = 35;
        var DOWN_ARROW_KEYCODE = 40;
        var BACKSPACE_KEYCODE = 8;
        var DELETE_KEYCODE = 46;
        if (!this.readOnly) {
            var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
            if (!textField) {
                var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
                if (e && e.getAttribute("name") == "propTextEditor")
                    textField = e;
            }
            var keyCode = event.which;
            var valueChanged = false;
            if (keyCode === ESCAPE_KEYCODE) {
                $RWT.textField.rwt_f_finishEdit(this, true);
                this.blur();
            } else if (keyCode === ENTER_KEYCODE) {
                $RWT.textField.rwt_f_finishEdit(this, false);
                this.blur();
            } else if (textField.inputType === 'Mask') {
                if (keyCode === RIGHT_ARROW_KEYCODE) {
                    var curPos = $RWT.textField.caret($(this));
                    var maxPos = $RWT.inputMaskUtils.getPositionOfFirstBlankCharInDisplayString(this.tokens, this.blankChar, this.value);
                    if (maxPos === -1)
                        maxPos = this.tokens.length;
                    var pos = curPos.end;
                    if (event.ctrlKey) {
                        var i;
                        var wasSeparator = false;
                        for (i = curPos.end + 1; i <= maxPos && i < this.tokens.length; i++) {
                            if (this.tokens[i].isSeparator) {
                                wasSeparator = true;
                            }
                            else if (wasSeparator) {
                                pos = i;
                                break;
                            }
                        }
                    }
                    else if (curPos.end < maxPos) {
                        pos = curPos.end + 1;
                    }
                    if (pos > curPos.end) {
                        if (event.shiftKey) {
                            $RWT.textField.caret($(this), curPos.begin, pos);
                        }
                        else {
                            $RWT.textField.caret($(this), pos);
                        }
                    }
                    $RWT.events.cancelEvent(event);
                }
                else if (keyCode === END_KEYCODE || keyCode === DOWN_ARROW_KEYCODE) {//End or Down
                    var curPos = $RWT.textField.caret($(this));
                    var pos =
                            $RWT.inputMaskUtils.getPositionOfFirstBlankCharInDisplayString(this.tokens, this.blankChar, this.value);
                    if (pos === -1)
                        pos = this.tokens.length;
                    if (curPos.end < pos) {
                        if (event.shiftKey) {
                            $RWT.textField.caret($(this), curPos.begin, pos);
                        }
                        else {
                            $RWT.textField.caret($(this), pos);
                        }
                    }
                    $RWT.events.cancelEvent(event);
                }
                else if (keyCode === BACKSPACE_KEYCODE) {//backspace
                    var curPos = $RWT.textField.caret($(this));
                    if (curPos.end > curPos.begin) {
                        var result =
                                $RWT.inputMaskUtils.putChar(this.blankChar, curPos.begin, curPos.end, this.value, this.tokens, this.blankChar, true);
                        this.value = result.newString;
                        var maxPos =
                                $RWT.inputMaskUtils.getPositionOfFirstBlankCharInDisplayString(this.tokens, this.blankChar, this.value);
                        if (maxPos === -1)
                            maxPos = this.tokens.length;
                        $RWT.textField.caret($(this), curPos.begin > maxPos ? maxPos : curPos.begin);
                        valueChanged = true;
                    }
                    else {
                        var i;
                        for (i = curPos.end - 1; i >= 0 && i < this.tokens.length; i--) {
                            if (!this.tokens[i].isSeparator) {
                                var result =
                                        $RWT.inputMaskUtils.putChar(this.blankChar, i, i, this.value, this.tokens, this.blankChar, true);
                                this.value = result.newString;
                                $RWT.textField.caret($(this), i);
                                valueChanged = true;
                                break;
                            }
                        }
                    }
                    $RWT.events.cancelEvent(event);
                }
                else if (keyCode === DELETE_KEYCODE) {//delete
                    var curPos = $RWT.textField.caret($(this));
                    if (curPos.end > curPos.begin) {
                        var result =
                                $RWT.inputMaskUtils.putChar(this.blankChar, curPos.begin, curPos.end, this.value, this.tokens, this.blankChar, true);
                        this.value = result.newString;
                        var maxPos =
                                $RWT.inputMaskUtils.getPositionOfFirstBlankCharInDisplayString(this.tokens, this.blankChar, this.value);
                        if (maxPos === -1)
                            maxPos = this.tokens.length;
                        $RWT.textField.caret($(this), curPos.begin > maxPos ? maxPos : curPos.begin);
                        valueChanged = true;
                    }
                    else {
                        var i;
                        for (i = curPos.end; i < this.tokens.length; i++) {
                            if (!this.tokens[i].isSeparator) {
                                var result =
                                        $RWT.inputMaskUtils.putChar(this.blankChar, i, i, this.value, this.tokens, this.blankChar, true);
                                this.value = result.newString;
                                var maxPos =
                                        $RWT.inputMaskUtils.getPositionOfFirstBlankCharInDisplayString(this.tokens, this.blankChar, this.value);
                                if (maxPos === -1)
                                    maxPos = this.tokens.length;
                                $RWT.textField.caret($(this), i > maxPos ? maxPos : i);
                                valueChanged = true;
                                break;
                            }
                        }
                    }
                    $RWT.events.cancelEvent(event);
                }
            } else if (textField.inputType === 'Num') {
                if (keyCode === BACKSPACE_KEYCODE || keyCode === DELETE_KEYCODE) {
                    var curPos = $RWT.textField.caret($(this));
                    var forcedChangeOnBackspace = false;
                    var removeFromPos = curPos.begin;
                    var removeToPos = curPos.end;
                    if (keyCode === BACKSPACE_KEYCODE) {
                        if (curPos.begin === curPos.end) {
                            removeFromPos--;
                        }
                        if (textField.symbols.triadDelimeter !== null &&
                                (curPos.end - curPos.begin) < 2 &&
                                this.value[removeFromPos] === textField.symbols.triadDelimeter) {
                            removeFromPos--;
                            forcedChangeOnBackspace = true;
                        }

                    } else {
                        if (curPos.begin === curPos.end) {
                            removeToPos++;
                        }
                        if (textField.symbols.triadDelimeter !== null &&
                                (curPos.end - curPos.begin) < 2 &&
                                this.value[curPos.begin] === textField.symbols.triadDelimeter) {
                            removeToPos++;
                        }
                    }
                    var resultText =
                            this.value.substring(0, removeFromPos) + this.value.substring(removeToPos, this.value.length);
                    var formattedText = $RWT.parseUtils.formatNumber(resultText,
                            textField.isIntegerNumber,
                            textField.radix,
                            textField.symbols,
                            null);
                    this.value = formattedText;
                    var caretPos = $RWT.parseUtils.calcNewCursorPosition(resultText, formattedText, removeFromPos,
                            textField.isIntegerNumber, forcedChangeOnBackspace, textField.symbols);
                    if (caretPos < formattedText.length) {
                        $RWT.textField.caret($(this), caretPos);
                    }
                    $RWT.events.cancelEvent(event);
                    valueChanged = true;
                }
            }
            if (valueChanged) {
                $RWT.textField._onChangeValue(this);
            }
        }
    },
    _onKeyUp: function(event) {
        var BACKSPACE_KEYCODE = 8;
        var DELETE_KEYCODE = 46;
        if (!this.readOnly && (event.which === BACKSPACE_KEYCODE || event.which === DELETE_KEYCODE)) {
            $RWT.textField._onChangeValue(this);
        }
    },
    onCursorPositionChanged: function(e) {
        var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
        /*if ((!(window.ActiveXObject) && "ActiveXObject" in window) || navigator.appVersion.indexOf("MSIE 10") !== -1) {
         $RWT.textField._normalizeSelection(textField);
         }*/
        if (!textField) {
            var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
            if (e && e.getAttribute("name") == "propTextEditor")
                textField = e;
        }
        if (textField.inputType === 'Mask') {
            $RWT.inputMaskUtils.updateCursorPosition(this, false);
        }
    },
    cutText: function(event) {
        if (!this.readOnly) {
            var curPos = $RWT.textField.caret($(this));
            if (curPos.end > curPos.begin) {
                var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
                if (!textField) {
                    var e = WpsUtils.findParentByClassName(this, "rwt-text-area");
                    if (e && e.getAttribute("name") == "propTextEditor")
                        textField = e;
                }
                var selectedText = this.value.substring(curPos.begin, curPos.end);
                if (textField.inputType === 'Mask') {
                    var newString = this.value.substring(0, curPos.begin);
                    var i;
                    for (i = curPos.begin; i < curPos.end && i < this.value.length && i < this.tokens.length; i++) {
                        if (this.tokens[i].isSeparator) {
                            newString += this.tokens[i].maskChar;
                        } else {
                            newString += this.blankChar;
                        }
                    }
                    newString += this.value.substring(curPos.end, this.value.length);
                    this.value = newString;
                    var maxPos =
                            $RWT.inputMaskUtils.getPositionOfFirstBlankCharInDisplayString(this.tokens, this.blankChar, this.value);
                    if (maxPos === -1)
                        maxPos = this.tokens.length;
                    $RWT.textField.caret($(this), curPos.begin > maxPos ? maxPos : curPos.begin);
                    $RWT.textField._copyToClipboard(event, selectedText);
                    $RWT.textField._onChangeValue(this);
                } else if (textField.inputType === 'Num') {
                    if (textField.symbols.triadDelimeter !== null &&
                            (curPos.end - curPos.begin) === 1 &&
                            this.value[curPos.begin] === textField.symbols.triadDelimeter) {
                        $RWT.textField._copyToClipboard(event, textField.symbols.triadDelimeter);
                    } else {
                        var resultText =
                                this.value.substring(0, curPos.begin) + this.value.substring(curPos.end, this.value.length);
                        var formattedText = $RWT.parseUtils.formatNumber(resultText,
                                textField.isIntegerNumber,
                                textField.radix,
                                textField.symbols,
                                null);
                        this.value = formattedText;
                        var caretPos = $RWT.parseUtils.calcNewCursorPosition(resultText, formattedText, curPos.begin,
                                textField.isIntegerNumber, false, textField.symbols);
                        if (caretPos < formattedText.length) {
                            $RWT.textField.caret($(this), caretPos);
                        }
                        $RWT.textField._copyToClipboard(event, selectedText);
                    }
                    $RWT.textField._onChangeValue(this);
                }
                else if (textField.inputType !== 'Bool') {
                    var resultText =
                            this.value.substring(0, curPos.begin) + this.value.substring(curPos.end, this.value.length);
                    $RWT.textField._onChangeValue(this, resultText);
                }
            }
        }
    },
    _copyToClipboard: function(event, text) {
        if (window.clipboardData) {//IE
            window.clipboardData.setData("Text", text);
        }
        else if (event && event.clipboardData) {//webkit browser
            event.clipboardData.setData("Text", text);
        }
        $RWT.events.cancelEvent(event);
    },
    pasteText: function(event) {
        if (!this.readOnly) {
            var insertingText;
            if (window.clipboardData) {//IE
                insertingText = window.clipboardData.getData("Text");
                if (insertingText == null) {
                    $RWT.events.cancelEvent(event);
                }
            }
            else if (event && event.clipboardData) {//webkit browser
                insertingText = event.clipboardData.getData("Text");
                if (insertingText == null) {
                    $RWT.events.cancelEvent(event);
                }
            }
            var textField = WpsUtils.findParentByClassName(this, "rwt-text-field");
            if (insertingText != null) {
                var isValid = true;
                var valueChanged = false;
                if (textField.inputType == 'Mask') {
                    var curPos = $RWT.textField.caret($(this));
                    $RWT.textField._processPasteWithMask(this, curPos, this.value, insertingText);
                    valueChanged = true;
                    isValid = false;//prevent default
                }
                else if (textField.inputType == 'Bin') {
                    isValid = $RWT.parseUtils.isValidHexString(insertingText);
                } else if (textField.inputType == 'Num') {
                    var curPos = $RWT.textField.caret($(this));
                    var resultText = $RWT.textField._calcResultText(this, insertingText);
                    var formattedText = $RWT.parseUtils.formatNumber(resultText,
                            textField.isIntegerNumber,
                            textField.radix,
                            textField.symbols,
                            textField.restrictions);
                    if (formattedText !== null) {
                        this.value = formattedText;
                        var caretPos = curPos.begin + insertingText.length;
                        caretPos = $RWT.parseUtils.calcNewCursorPosition(resultText, formattedText, caretPos,
                                textField.isIntegerNumber, false, textField.symbols);
                        if (caretPos < formattedText.length - 1) {
                            $RWT.textField.caret($(this), caretPos);
                        }
                        valueChanged = true
                    }
                    isValid = false;//prevent default
                } else {
                    var resultText = $RWT.textField._calcResultText(this, insertingText);
                    isValid = $RWT.textField._validate(textField, resultText);
                }
                if (!isValid) {
                    $RWT.events.cancelEvent(event);
                }
                if (valueChanged) {
                    $RWT.textField._onChangeValue(this);
                }
            }
            else {//FireFox                
                var curPos = $RWT.textField.caret($(this));
                var input = this;
                var originalText = input.value;
                var afterPaste = function() {
                    if (textField.inputType == 'Mask') {
                        var insertingLen =
                                input.value.length - (originalText.length - (curPos.end - curPos.begin));
                        var insertingText = insertingLen > 0 ? input.value.substr(curPos.begin, insertingLen) : input.value;
                        $RWT.textField._processPasteWithMask(input, curPos, originalText, insertingText);
                    } else if (textField.inputType == 'Num') {
                        var curPos = $RWT.textField.caret($(this));
                        var formattedText = $RWT.parseUtils.formatNumber(this.value,
                                textField.isIntegerNumber,
                                textField.radix,
                                textField.symbols,
                                textField.restrictions);
                        if (formattedText !== null) {
                            caretPos = $RWT.parseUtils.calcNewCursorPosition(this.value, formattedText, curPos.end,
                                    textField.isIntegerNumber, false, textField.symbols);
                            this.value = formattedText;
                            if (caretPos < formattedText.length - 1) {
                                $RWT.textField.caret($(this), caretPos);
                            }
                        } else {
                            input.value = originalText;
                        }
                    } else {
                        var isValid = true;
                        if (textField.inputType == 'Bin') {
                            isValid = $RWT.parseUtils.isValidHexString(input.value);
                        }
                        else {
                            isValid = $RWT.textField._validate(textField, input.value);
                        }
                        if (!isValid) {
                            input.value = originalText;
                        }
                    }
                }
                setTimeout(afterPaste, 0);
            }
        }
    },
    _processPasteWithMask: function(input, curPos, originalText, insertingText) {
        var maskedText = $RWT.inputMaskUtils.apply(input.tokens, input.blankChar, insertingText, curPos.begin, originalText);
        var resultString = originalText.substring(0, curPos.begin) + maskedText;
        if (resultString.length <= curPos.end) {
            resultString += $RWT.inputMaskUtils.buildClearString(input.tokens, input.blankChar, resultString.length, curPos.end);
        }
        var pos = resultString.length < curPos.end - 1 ? curPos.end - 1 : resultString.length;
        if (resultString.length < originalText.length) {
            resultString += originalText.substring(resultString.length);
        }
        input.value = resultString;
        var maxPos =
                $RWT.inputMaskUtils.getPositionOfFirstBlankCharInDisplayString(input.tokens, input.blankChar, input.value);
        if (pos < maxPos || maxPos < 0) {
            $RWT.inputMaskUtils.moveCursorToRight(input, pos);
        }
        else {
            $RWT.textField.caret($(input), maxPos);
        }
    },
    _validate: function(tf, text) {
        if (tf.inputType == 'Int') {
            return $RWT.parseUtils.isValidInteger(text, tf.canBePositive, tf.canBeNegative, tf.radix) &&
                    (tf.maxDigitsCount == 0 || tf.maxDigitsCount >= $RWT.parseUtils.getDigitsCount(text));
        }
        else if (tf.inputType == 'Num') {
            return $RWT.parseUtils.isValidDecimal(text, tf.canBePositive, tf.canBeNegative, tf.decimalDelimeter)
                    && (tf.precision == -1 || tf.precision >= $RWT.parseUtils.getFractionDigitsCount(text));
        }
        else if (tf.inputType == 'Bin') {
            return $RWT.parseUtils.isValidHexString(text);
        }
        else if (tf.inputType == 'Bool') {
            return false;
        }
        return true;
    },
    _calcResultText: function(inputElement, insertingText) {
        var currentText = inputElement.value;
        if (inputElement.selectionStart != null && inputElement.selectionEnd != null) {
            return currentText.slice(0, inputElement.selectionStart) + insertingText + currentText.slice(inputElement.selectionEnd);
        }
        else {
            return currentText;
        }
    },
    _normalizeSelection: function(element) {//For IE
        if (document.selection) {
            // The current selection
            var range = document.selection.createRange();
            // We'll use this as a 'dummy'
            var stored_range = range.duplicate();
            // Select all text
            stored_range.moveToElementText(element);
            // Now move 'dummy' end point to end point of original range
            stored_range.setEndPoint('EndToEnd', range);
            // Now we can calculate start and end points
            element.selectionStart = stored_range.text.length - range.text.length;
            element.selectionEnd = element.selectionStart + range.text.length;
        }
    },
    caret: function(input, begin, end) {
        if (input.length == 0)
            return;
        if (typeof begin == 'number') {
            end = (typeof end == 'number') ? end : begin;
            return input.each(function() {
                if (this.setSelectionRange) {
                    this.setSelectionRange(begin, end);
                } else if (this.createTextRange) {
                    var range = this.createTextRange();
                    range.collapse(true);
                    range.moveEnd('character', end);
                    range.moveStart('character', begin);
                    range.select();
                }
            });
        } else {
            if (input[0].setSelectionRange) {
                begin = input[0].selectionStart;
                end = input[0].selectionEnd;
            } else if (document.selection && document.selection.createRange) {
                var range = document.selection.createRange();
                begin = 0 - range.duplicate().moveStart('character', -100000);
                end = begin + range.text.length;
            }
            return {
                begin: begin,
                end: end
            };
        }
    },
    _findInput: function(tf) {
        var div = WpsUtils.findChildByLocalName(tf, 'div');
        var e = WpsUtils.findChildByLocalName(div, 'input');
        if (e)
            return e;
        else
            return  WpsUtils.findChildByLocalName(div, 'textArea');
    },
    requestFocus: function(tf) {
        if (tf) {
            var input = $RWT.textField._findInput(tf);
            if (input) {
                $(input).focus();
                setTimeout(function(){$(input).focus();}, 100);
            }
        }
    },
    requestFocusTextArea: function(ta) {
        if (ta) {
            var editor = $RWT.textField._findTextArea(ta);
            if (editor) {
                $(editor).focus();
                setTimeout(function(){$(editor).focus();}, 100);
            }
        }

    }

}
$RWT.textField.layout.ignoreChildren = true;

$RWT.parseUtils = {
    isDigit: function(charCode, radix) {
        if (48 <= charCode && charCode <= 57) {//0-9
            return radix > charCode - 48;
        }
        else if (97 <= charCode && charCode <= 122) {//a-z
            return radix > charCode - 87;
        }
        else if (65 <= charCode && charCode <= 90) {//A-Z
            return radix > charCode - 55;
        }
        return false;
    },
    isValidInteger: function(integerAsStr, canBePositive, canBeNegative, radix) {
        var i = 0;
        if (integerAsStr.charAt(i) == "-") {
            if (canBeNegative && integerAsStr.length > 1) {
                i++;
            }
            else {
                return false;
            }
        }
        for (; i < integerAsStr.length; i++) {
            if (!$RWT.parseUtils.isDigit(integerAsStr.charCodeAt(i), radix)) {
                return false;
            }
        }
        return true;
    },
    isValidDecimal: function(decimalAsStr, canBePositive, canBeNegative, decimalDelimeter) {
        var i = 0;
        if (decimalAsStr.charAt(i) == "-") {
            if (canBeNegative && decimalAsStr.length > 1) {
                i++;
            }
            else {
                return false;
            }
        }
        var wasPoint = false;
        for (; i < decimalAsStr.length; i++) {
            if (decimalAsStr.charAt(i) === '.' || decimalAsStr.charAt(i) === decimalDelimeter) {
                if (wasPoint) {
                    return false;
                }
                else {
                    wasPoint = true;
                }
            }
            else if (!$RWT.parseUtils.isDigit(decimalAsStr.charCodeAt(i), 10)) {
                return false;
            }
        }
        return true;
    },
    isValidHexString: function(binAsStr) {
        var i;
        for (i = 0; i < binAsStr.length; i++) {
            if (binAsStr.charAt(i) != ' ' && !$RWT.parseUtils.isDigit(binAsStr.charCodeAt(i), 16)) {
                return false
            }
        }
        return true;
    },
    getFractionDigitsCount: function(text) {
        var i = text.indexOf('.');
        return i >= 0 ? text.length - i - 1 : 0;
    },
    getDigitsCount: function(text) {
        var i, digitsCount = 0;
        for (i = 0; i < text.length; i++) {
            if (text.charAt(i) == '0') {
                if (digitsCount > 0)
                    digitsCount++;
            }
            else if (text.charAt(i) != '-') {
                digitsCount++;
            }
        }
        return digitsCount;
    },
    formatNumber: function(inputText, isInteger, radix, symbols, restrictions) {
        var START_STATE = 0;
        var SIGN_STATE = 1;
        var INTEGER_PART_STATE = 2;
        var FRACTION_PART_STATE = 3;
        var formattedString = '';
        var i, c, curChar;
        var int_digits_counter = 0;
        var last_digit_char_pos = -1;
        var curState = START_STATE;

        for (i = 0, c = inputText.length; i < c; i++) {
            curChar = inputText.charAt(i);
            if (curChar === symbols.plusSign || curChar === symbols.minusSign) {
                if (curState === START_STATE) {
                    if ((restrictions !== null && restrictions.canBeNegative && curChar === symbols.minusSign) ||
                            (restrictions !== null && restrictions.canBePositive && curChar === symbols.plusSign)
                            ) {
                        formattedString = curChar;
                        curState = SIGN_STATE;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else if (curChar === symbols.decimalPoint || curChar === '.') {
                if (isInteger || curState === FRACTION_PART_STATE) {
                    return null;
                } else if (curState === SIGN_STATE || curState === START_STATE) {
                    formattedString += '0';
                }
                last_digit_char_pos = formattedString.length - 1;
                formattedString += symbols.decimalPoint === null ? curChar : symbols.decimalPoint;
                curState = FRACTION_PART_STATE;
            } else if ($RWT.parseUtils.isDigit(inputText.charCodeAt(i), radix)) {
                if (curState !== FRACTION_PART_STATE && curState !== INTEGER_PART_STATE) {
                    curState = INTEGER_PART_STATE;
                    int_digits_counter = 1;
                } else if (curState === INTEGER_PART_STATE) {
                    if (restrictions !== null && restrictions.maxIntDigitsCount !== null && int_digits_counter >= restrictions.maxIntDigitsCount) {
                        return null;
                    }
                    int_digits_counter++;
                }
                formattedString += curChar;
            } else if (curChar !== symbols.triadDelimeter) {
                return null;
            }
        }
        if (last_digit_char_pos === -1 && curState === INTEGER_PART_STATE) {
            last_digit_char_pos = formattedString.length - 1;
        }
        if (symbols.triadDelimeter !== null && int_digits_counter > 3 && last_digit_char_pos > 0) {
            var delimeters_count = Math.floor(int_digits_counter / 3) - (int_digits_counter % 3 === 0 ? 1 : 0);
            var delim_pos = last_digit_char_pos - 2;
            for (i = 1; i <= delimeters_count; i++) {
                formattedString =
                        formattedString.substring(0, delim_pos) + symbols.triadDelimeter + formattedString.substring(delim_pos, formattedString.length);
                delim_pos -= 3;
            }
        }
        return formattedString;
    },
    calcNewCursorPosition: function(inputText, formattedText, curPos, isInteger, forcedChangeOnBackspace, symbols) {
        var absolutePosition = 0;
        if (symbols.triadDelimeter !== null) {
            var i;
            for (i = 0; i < curPos && i < inputText.length; i++) {
                if (inputText[i] != symbols.triadDelimeter) {
                    absolutePosition++;
                }
            }
        } else {
            absolutePosition = curPos;
        }
        var leadingDecimalDelimeter = false;
        if (!isInteger) {
            var plusCharacter = symbols.plusSign;
            var minusCharacter = symbols.minusSign;
            var i;
            for (i = curPos - 1; i >= 0; i--) {
                if (symbols.decimalPoint == inputText[i] || inputText[i] == '.') {
                    leadingDecimalDelimeter = true;
                } else if (inputText[i] != plusCharacter && inputText[i] != minusCharacter) {
                    leadingDecimalDelimeter = false;
                    break;
                }
            }
        }
        var newCursorPosition = 0;
        if (symbols.triadDelimeter !== null) {
            var triadDelimeter = symbols.triadDelimeter;
            var isCursorAfterDoubleDelimeter = curPos > 1 &&
                    inputText[curPos - 1] == triadDelimeter &&
                    inputText[curPos - 2] == triadDelimeter;
            var i, j;
            for (i = absolutePosition, j = 0; i > 0 && j < formattedText.length; j++) {
                if (formattedText[j] != triadDelimeter) {
                    i--;
                }
                newCursorPosition++;
            }
            if ((isCursorAfterDoubleDelimeter || forcedChangeOnBackspace) &&
                    newCursorPosition < formattedText.length &&
                    formattedText[newCursorPosition] == triadDelimeter) {
                newCursorPosition++;
            }
        } else {
            newCursorPosition = curPos;
        }

        if (leadingDecimalDelimeter) {
            newCursorPosition++;
        }
        return newCursorPosition;
    }
}
$RWT.textArea = {
    layout: function(textAreaDiv) {
        var textArea = WpsUtils.findChildByLocalName(textAreaDiv,"textarea");
        if (textArea){
            var newCursorPosition = textAreaDiv.getAttribute("cursorPos");
            if (newCursorPosition){
                $RWT.textField.caret($(textArea),parseInt(newCursorPosition));
                textAreaDiv.removeAttribute("cursorPos");
            }
        }
    },
    clientTextChange: function(tf) {
        $RWT.actions.client_event(tf, 'change', $(tf).val());
    },
    textChange: function(tf) {
        $RWT.actions.event(tf, 'change', $(tf).val());
    },
    drop: function(event) {
        var insertingText = event.dataTransfer.getData('Text');
        var textArea = WpsUtils.findParentByClassName(this, "rwt-text-area");
        if (textArea != null && insertingText != null && insertingText.length > 0) {
            var resultText = $RWT.textField._calcResultText(this, insertingText);
            this.value = resultText;
            $RWT.textArea._onChangeValue(this);
        }
        $RWT.events.cancelEvent(event);
    },
    dragEnter: function(event) {
        $(this).focus();
    },
    dragLeave: function() {
        $(this).blur();
    },
    dragOver: function(event) {
        // the dragover event needs to be canceled to allow firing the drop event
        $RWT.events.cancelEvent(event);
        $(this).focus();
    },
    focusIn: function(event) {
        var textArea = WpsUtils.findParentByClassName(this, "rwt-text-area");
        if (textArea.ignoreFocusIn == true) {//call during startModification or discardModification event                      
            textArea.ignoreFocusIn = false;
            $RWT.registerFocuseEvent(event);
            return;
        }
        if (this.rwt_f_focusIn) {
            this.rwt_f_focusIn();
        }
        if (!this.readOnly) {
            textArea.oldText = $(this).val();
            attrUpdate(this, 'oninput', '$RWT.textArea._onInput');
            if ($RWT.BrowserDetect.browser == 'Explorer') {
                attrUpdate(this, 'onkeyup', '$RWT.textArea._onKeyUp');
                attrUpdate(this, 'oncut', '$RWT.textArea._onCutText');
            }
        }
        $RWT.registerFocuseEvent(event);
    },
    finishEdit: function(input) {
        var textArea = WpsUtils.findParentByClassName(input, "rwt-text-area");
        var inputValue = null;
        if (!input.readOnly) {
            attrUpdate(input, 'oninput', null);
            attrUpdate(input, 'onkeyup', null);
            attrUpdate(input, 'oncut', null);
            inputValue = input.value;
            if (textArea.oldText != null) {
                textArea.wasChanged = !(textArea.oldText === $(input).val());
            }
        }
        if (input.rwt_f_focusOut) {
            input.rwt_f_focusOut();
        }
        if (textArea.wasChanged != null && textArea.wasChanged) {
            if (input.rwt_f_onchange) {
                input.rwt_f_onchange(input);
            }
            $RWT.textArea.textChange(input);
        } else {
            var canceledChanges = textArea.inModificationState == true
                    && textArea.oldText != null
                    && (textArea.initialText == null || textArea.initialText == textArea.oldText);
            if (canceledChanges) {
                $RWT.actions.event(input, 'discardModification');
            }
            textArea.oldText = null;
        }
        if (textArea.initialText == null || textArea.initialText == inputValue) {
            textArea.inModificationState = false;
        }
        textArea.ignoreFocusIn = false;
        textArea.wasChanged = false;
    },
    focusOut: function() {
        $RWT.textArea.finishEdit(this);
    },
    getSelectedText: function(){
        var textAreaId = this.getAttribute("textAreaId");
        var selfId = this.getAttribute("id");
        if (textAreaId && selfId){
            var objects = $('#' + textAreaId);
            var textArea = objects && objects.length>0 ? objects[0] : null;
            if (textArea){
                var selectedText;
                // IE version
                if (document.selection != undefined){
                    textArea.focus();
                    var sel = document.selection.createRange();
                    selectedText = sel==null ? "" : sel.text;
                }
                // Mozilla version
                else if (textArea.selectionStart != undefined){                  
                  var startPos = textArea.selectionStart;
                  var endPos = textArea.selectionEnd;
                  selectedText = textArea.value.substring(startPos, endPos);
                }
                $RWT.actions.event(textArea, 'updateSelectedText', selectedText+'#'+selfId);
            }
        }
    },    
    getCursorPosition: function(){
        var textAreaId = this.getAttribute("textAreaId");
        var selfId = this.getAttribute("id");
        if (textAreaId && selfId){
            var objects = $('#' + textAreaId);
            if (objects && objects.length>0){
                var textArea = objects[0];                
                var cursor = $RWT.textField.caret(objects);
                if (cursor!=null && cursor.begin!=null && cursor.end!=null){
                    $RWT.textArea.finishEdit(textArea);
                    $RWT.actions.event(textArea, 'updateCursorPosition', cursor.begin.toString()+"-"+cursor.end.toString()+'#'+selfId);
                }
            }
        }
    },    
    _onInput: function(event) {
        $RWT.textArea._onChangeValue(this);
    },
    _onChangeValue: function(input, newValue) {
        var textArea = WpsUtils.findParentByClassName(input, "rwt-text-area");
        if (textArea != null) {
            var initialText = textArea.initialText == null ? textArea.oldText : textArea.initialText;
            var newText = newValue == null ? input.value : newValue;
            if (initialText != null) {
                if ((textArea.inModificationState != true/*null or false*/|| textArea.repeatStartModification==true) && initialText != newText) {
                    textArea.inModificationState = true;
                    textArea.ignoreFocusIn = true;
                    textArea.repeatStartModification=false;
                    $RWT.actions.event(input, 'startModification', newText);
                } else if (textArea.inModificationState == true && initialText == newText) {
                    textArea.inModificationState = false;
                    textArea.ignoreFocusIn = true;
                    $RWT.actions.event(input, 'discardModification');
                }
            }
        }
    },
    _onKeyUp: function(event) {
        var BACKSPACE_KEYCODE = 8;
        var DELETE_KEYCODE = 46;
        if (!this.readOnly && (event.which === BACKSPACE_KEYCODE || event.which === DELETE_KEYCODE)) {
            $RWT.textArea._onChangeValue(this);
        }
    },
    _onCutText: function(event) {
        var curPos = $RWT.textField.caret($(this));
        if (curPos.end > curPos.begin) {
            var resultText =
                    this.value.substring(0, curPos.begin) + this.value.substring(curPos.end, this.value.length);
            $RWT.textArea._onChangeValue(this, resultText);
        }
    }
}
$RWT.tooltip = {
    _positiontip: function(tooltip, geometry, e) {
        var iebody = (document.compatMode && document.compatMode != "BackCompat") ? document.documentElement : document.body
        var scrollTop = window.pageYOffset ? window.pageYOffset : iebody.scrollTop
        var docwidth = (window.innerWidth) ? window.innerWidth - 15 : iebody.clientWidth - 15
        var docheight = (window.innerHeight) ? window.innerHeight - 18 : iebody.clientHeight - 15
        var tipx = docwidth - geometry.offsetx
        var tipy = geometry.offsety + geometry.h
        tipy = (tipy - scrollTop > docheight) ? tipy - geometry.h : tipy
        $(tooltip).css('right', tipx + 'px').css('top', tipy + 'px');
    },
    _findtip: function(owner) {
        if (owner != null && owner.getAttribute != null && owner.getAttribute("tooltip_id") != null) {
            var tooltips = $('#' + $(owner).attr('tooltip_id'));
            if (tooltips && tooltips.length > 0) {
                return tooltips[0];
            }
        }
        return null;
    },
    onMouseOver: function(e) {
        var tooltip = $RWT.tooltip._findtip(this);
        if (tooltip != null) {
            var geometry = {
                w: this.offsetWidth,
                h: this.offsetHeight,
                offsetx: $(this).offset().left,
                offsety: $(this).offset().top
            } //store anchor dimensions
            tooltip.geometry = {
                w: tooltip.offsetWidth,
                h: tooltip.offsetHeight
            }
            $RWT.tooltip._positiontip(tooltip, geometry, e);
            $(tooltip).css("zIndex", WpsUtils.getNextZIndex(this)).css("position", "absolute").show('slow');
        }
    },
    onMouseOut: function(e) {
        var tooltip = $RWT.tooltip._findtip(this);
        if (tooltip != null) {
            $(tooltip).hide();
        }
    }
}
$RWT.inputMaskUtils = {
    __letter: /[\u0041-\u005A\u0061-\u007A\u00AA\u00B5\u00BA\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02C1\u02C6-\u02D1\u02E0-\u02E4\u02EC\u02EE\u0370-\u0374\u0376\u0377\u037A-\u037D\u0386\u0388-\u038A\u038C\u038E-\u03A1\u03A3-\u03F5\u03F7-\u0481\u048A-\u0525\u0531-\u0556\u0559\u0561-\u0587\u05D0-\u05EA\u05F0-\u05F2\u0621-\u064A\u066E\u066F\u0671-\u06D3\u06D5\u06E5\u06E6\u06EE\u06EF\u06FA-\u06FC\u06FF\u0710\u0712-\u072F\u074D-\u07A5\u07B1\u07CA-\u07EA\u07F4\u07F5\u07FA\u0800-\u0815\u081A\u0824\u0828\u0904-\u0939\u093D\u0950\u0958-\u0961\u0971\u0972\u0979-\u097F\u0985-\u098C\u098F\u0990\u0993-\u09A8\u09AA-\u09B0\u09B2\u09B6-\u09B9\u09BD\u09CE\u09DC\u09DD\u09DF-\u09E1\u09F0\u09F1\u0A05-\u0A0A\u0A0F\u0A10\u0A13-\u0A28\u0A2A-\u0A30\u0A32\u0A33\u0A35\u0A36\u0A38\u0A39\u0A59-\u0A5C\u0A5E\u0A72-\u0A74\u0A85-\u0A8D\u0A8F-\u0A91\u0A93-\u0AA8\u0AAA-\u0AB0\u0AB2\u0AB3\u0AB5-\u0AB9\u0ABD\u0AD0\u0AE0\u0AE1\u0B05-\u0B0C\u0B0F\u0B10\u0B13-\u0B28\u0B2A-\u0B30\u0B32\u0B33\u0B35-\u0B39\u0B3D\u0B5C\u0B5D\u0B5F-\u0B61\u0B71\u0B83\u0B85-\u0B8A\u0B8E-\u0B90\u0B92-\u0B95\u0B99\u0B9A\u0B9C\u0B9E\u0B9F\u0BA3\u0BA4\u0BA8-\u0BAA\u0BAE-\u0BB9\u0BD0\u0C05-\u0C0C\u0C0E-\u0C10\u0C12-\u0C28\u0C2A-\u0C33\u0C35-\u0C39\u0C3D\u0C58\u0C59\u0C60\u0C61\u0C85-\u0C8C\u0C8E-\u0C90\u0C92-\u0CA8\u0CAA-\u0CB3\u0CB5-\u0CB9\u0CBD\u0CDE\u0CE0\u0CE1\u0D05-\u0D0C\u0D0E-\u0D10\u0D12-\u0D28\u0D2A-\u0D39\u0D3D\u0D60\u0D61\u0D7A-\u0D7F\u0D85-\u0D96\u0D9A-\u0DB1\u0DB3-\u0DBB\u0DBD\u0DC0-\u0DC6\u0E01-\u0E30\u0E32\u0E33\u0E40-\u0E46\u0E81\u0E82\u0E84\u0E87\u0E88\u0E8A\u0E8D\u0E94-\u0E97\u0E99-\u0E9F\u0EA1-\u0EA3\u0EA5\u0EA7\u0EAA\u0EAB\u0EAD-\u0EB0\u0EB2\u0EB3\u0EBD\u0EC0-\u0EC4\u0EC6\u0EDC\u0EDD\u0F00\u0F40-\u0F47\u0F49-\u0F6C\u0F88-\u0F8B\u1000-\u102A\u103F\u1050-\u1055\u105A-\u105D\u1061\u1065\u1066\u106E-\u1070\u1075-\u1081\u108E\u10A0-\u10C5\u10D0-\u10FA\u10FC\u1100-\u1248\u124A-\u124D\u1250-\u1256\u1258\u125A-\u125D\u1260-\u1288\u128A-\u128D\u1290-\u12B0\u12B2-\u12B5\u12B8-\u12BE\u12C0\u12C2-\u12C5\u12C8-\u12D6\u12D8-\u1310\u1312-\u1315\u1318-\u135A\u1380-\u138F\u13A0-\u13F4\u1401-\u166C\u166F-\u167F\u1681-\u169A\u16A0-\u16EA\u1700-\u170C\u170E-\u1711\u1720-\u1731\u1740-\u1751\u1760-\u176C\u176E-\u1770\u1780-\u17B3\u17D7\u17DC\u1820-\u1877\u1880-\u18A8\u18AA\u18B0-\u18F5\u1900-\u191C\u1950-\u196D\u1970-\u1974\u1980-\u19AB\u19C1-\u19C7\u1A00-\u1A16\u1A20-\u1A54\u1AA7\u1B05-\u1B33\u1B45-\u1B4B\u1B83-\u1BA0\u1BAE\u1BAF\u1C00-\u1C23\u1C4D-\u1C4F\u1C5A-\u1C7D\u1CE9-\u1CEC\u1CEE-\u1CF1\u1D00-\u1DBF\u1E00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D\u1F50-\u1F57\u1F59\u1F5B\u1F5D\u1F5F-\u1F7D\u1F80-\u1FB4\u1FB6-\u1FBC\u1FBE\u1FC2-\u1FC4\u1FC6-\u1FCC\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFC\u2071\u207F\u2090-\u2094\u2102\u2107\u210A-\u2113\u2115\u2119-\u211D\u2124\u2126\u2128\u212A-\u212D\u212F-\u2139\u213C-\u213F\u2145-\u2149\u214E\u2183\u2184\u2C00-\u2C2E\u2C30-\u2C5E\u2C60-\u2CE4\u2CEB-\u2CEE\u2D00-\u2D25\u2D30-\u2D65\u2D6F\u2D80-\u2D96\u2DA0-\u2DA6\u2DA8-\u2DAE\u2DB0-\u2DB6\u2DB8-\u2DBE\u2DC0-\u2DC6\u2DC8-\u2DCE\u2DD0-\u2DD6\u2DD8-\u2DDE\u2E2F\u3005\u3006\u3031-\u3035\u303B\u303C\u3041-\u3096\u309D-\u309F\u30A1-\u30FA\u30FC-\u30FF\u3105-\u312D\u3131-\u318E\u31A0-\u31B7\u31F0-\u31FF\u3400-\u4DB5\u4E00-\u9FCB\uA000-\uA48C\uA4D0-\uA4FD\uA500-\uA60C\uA610-\uA61F\uA62A\uA62B\uA640-\uA65F\uA662-\uA66E\uA67F-\uA697\uA6A0-\uA6E5\uA717-\uA71F\uA722-\uA788\uA78B\uA78C\uA7FB-\uA801\uA803-\uA805\uA807-\uA80A\uA80C-\uA822\uA840-\uA873\uA882-\uA8B3\uA8F2-\uA8F7\uA8FB\uA90A-\uA925\uA930-\uA946\uA960-\uA97C\uA984-\uA9B2\uA9CF\uAA00-\uAA28\uAA40-\uAA42\uAA44-\uAA4B\uAA60-\uAA76\uAA7A\uAA80-\uAAAF\uAAB1\uAAB5\uAAB6\uAAB9-\uAABD\uAAC0\uAAC2\uAADB-\uAADD\uABC0-\uABE2\uAC00-\uD7A3\uD7B0-\uD7C6\uD7CB-\uD7FB\uF900-\uFA2D\uFA30-\uFA6D\uFA70-\uFAD9\uFB00-\uFB06\uFB13-\uFB17\uFB1D\uFB1F-\uFB28\uFB2A-\uFB36\uFB38-\uFB3C\uFB3E\uFB40\uFB41\uFB43\uFB44\uFB46-\uFBB1\uFBD3-\uFD3D\uFD50-\uFD8F\uFD92-\uFDC7\uFDF0-\uFDFB\uFE70-\uFE74\uFE76-\uFEFC\uFF21-\uFF3A\uFF41-\uFF5A\uFF66-\uFFBE\uFFC2-\uFFC7\uFFCA-\uFFCF\uFFD2-\uFFD7\uFFDA-\uFFDC]/,
    __letterOrDigit: /[\u0041-\u005A\u0061-\u007A\u00AA\u00B5\u00BA\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02C1\u02C6-\u02D1\u02E0-\u02E4\u02EC\u02EE\u0370-\u0374\u0376\u0377\u037A-\u037D\u0386\u0388-\u038A\u038C\u038E-\u03A1\u03A3-\u03F5\u03F7-\u0481\u048A-\u0525\u0531-\u0556\u0559\u0561-\u0587\u05D0-\u05EA\u05F0-\u05F2\u0621-\u064A\u066E\u066F\u0671-\u06D3\u06D5\u06E5\u06E6\u06EE\u06EF\u06FA-\u06FC\u06FF\u0710\u0712-\u072F\u074D-\u07A5\u07B1\u07CA-\u07EA\u07F4\u07F5\u07FA\u0800-\u0815\u081A\u0824\u0828\u0904-\u0939\u093D\u0950\u0958-\u0961\u0971\u0972\u0979-\u097F\u0985-\u098C\u098F\u0990\u0993-\u09A8\u09AA-\u09B0\u09B2\u09B6-\u09B9\u09BD\u09CE\u09DC\u09DD\u09DF-\u09E1\u09F0\u09F1\u0A05-\u0A0A\u0A0F\u0A10\u0A13-\u0A28\u0A2A-\u0A30\u0A32\u0A33\u0A35\u0A36\u0A38\u0A39\u0A59-\u0A5C\u0A5E\u0A72-\u0A74\u0A85-\u0A8D\u0A8F-\u0A91\u0A93-\u0AA8\u0AAA-\u0AB0\u0AB2\u0AB3\u0AB5-\u0AB9\u0ABD\u0AD0\u0AE0\u0AE1\u0B05-\u0B0C\u0B0F\u0B10\u0B13-\u0B28\u0B2A-\u0B30\u0B32\u0B33\u0B35-\u0B39\u0B3D\u0B5C\u0B5D\u0B5F-\u0B61\u0B71\u0B83\u0B85-\u0B8A\u0B8E-\u0B90\u0B92-\u0B95\u0B99\u0B9A\u0B9C\u0B9E\u0B9F\u0BA3\u0BA4\u0BA8-\u0BAA\u0BAE-\u0BB9\u0BD0\u0C05-\u0C0C\u0C0E-\u0C10\u0C12-\u0C28\u0C2A-\u0C33\u0C35-\u0C39\u0C3D\u0C58\u0C59\u0C60\u0C61\u0C85-\u0C8C\u0C8E-\u0C90\u0C92-\u0CA8\u0CAA-\u0CB3\u0CB5-\u0CB9\u0CBD\u0CDE\u0CE0\u0CE1\u0D05-\u0D0C\u0D0E-\u0D10\u0D12-\u0D28\u0D2A-\u0D39\u0D3D\u0D60\u0D61\u0D7A-\u0D7F\u0D85-\u0D96\u0D9A-\u0DB1\u0DB3-\u0DBB\u0DBD\u0DC0-\u0DC6\u0E01-\u0E30\u0E32\u0E33\u0E40-\u0E46\u0E81\u0E82\u0E84\u0E87\u0E88\u0E8A\u0E8D\u0E94-\u0E97\u0E99-\u0E9F\u0EA1-\u0EA3\u0EA5\u0EA7\u0EAA\u0EAB\u0EAD-\u0EB0\u0EB2\u0EB3\u0EBD\u0EC0-\u0EC4\u0EC6\u0EDC\u0EDD\u0F00\u0F40-\u0F47\u0F49-\u0F6C\u0F88-\u0F8B\u1000-\u102A\u103F\u1050-\u1055\u105A-\u105D\u1061\u1065\u1066\u106E-\u1070\u1075-\u1081\u108E\u10A0-\u10C5\u10D0-\u10FA\u10FC\u1100-\u1248\u124A-\u124D\u1250-\u1256\u1258\u125A-\u125D\u1260-\u1288\u128A-\u128D\u1290-\u12B0\u12B2-\u12B5\u12B8-\u12BE\u12C0\u12C2-\u12C5\u12C8-\u12D6\u12D8-\u1310\u1312-\u1315\u1318-\u135A\u1380-\u138F\u13A0-\u13F4\u1401-\u166C\u166F-\u167F\u1681-\u169A\u16A0-\u16EA\u1700-\u170C\u170E-\u1711\u1720-\u1731\u1740-\u1751\u1760-\u176C\u176E-\u1770\u1780-\u17B3\u17D7\u17DC\u1820-\u1877\u1880-\u18A8\u18AA\u18B0-\u18F5\u1900-\u191C\u1950-\u196D\u1970-\u1974\u1980-\u19AB\u19C1-\u19C7\u1A00-\u1A16\u1A20-\u1A54\u1AA7\u1B05-\u1B33\u1B45-\u1B4B\u1B83-\u1BA0\u1BAE\u1BAF\u1C00-\u1C23\u1C4D-\u1C4F\u1C5A-\u1C7D\u1CE9-\u1CEC\u1CEE-\u1CF1\u1D00-\u1DBF\u1E00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D\u1F50-\u1F57\u1F59\u1F5B\u1F5D\u1F5F-\u1F7D\u1F80-\u1FB4\u1FB6-\u1FBC\u1FBE\u1FC2-\u1FC4\u1FC6-\u1FCC\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFC\u2071\u207F\u2090-\u2094\u2102\u2107\u210A-\u2113\u2115\u2119-\u211D\u2124\u2126\u2128\u212A-\u212D\u212F-\u2139\u213C-\u213F\u2145-\u2149\u214E\u2183\u2184\u2C00-\u2C2E\u2C30-\u2C5E\u2C60-\u2CE4\u2CEB-\u2CEE\u2D00-\u2D25\u2D30-\u2D65\u2D6F\u2D80-\u2D96\u2DA0-\u2DA6\u2DA8-\u2DAE\u2DB0-\u2DB6\u2DB8-\u2DBE\u2DC0-\u2DC6\u2DC8-\u2DCE\u2DD0-\u2DD6\u2DD8-\u2DDE\u2E2F\u3005\u3006\u3031-\u3035\u303B\u303C\u3041-\u3096\u309D-\u309F\u30A1-\u30FA\u30FC-\u30FF\u3105-\u312D\u3131-\u318E\u31A0-\u31B7\u31F0-\u31FF\u3400-\u4DB5\u4E00-\u9FCB\uA000-\uA48C\uA4D0-\uA4FD\uA500-\uA60C\uA610-\uA61F\uA62A\uA62B\uA640-\uA65F\uA662-\uA66E\uA67F-\uA697\uA6A0-\uA6E5\uA717-\uA71F\uA722-\uA788\uA78B\uA78C\uA7FB-\uA801\uA803-\uA805\uA807-\uA80A\uA80C-\uA822\uA840-\uA873\uA882-\uA8B3\uA8F2-\uA8F7\uA8FB\uA90A-\uA925\uA930-\uA946\uA960-\uA97C\uA984-\uA9B2\uA9CF\uAA00-\uAA28\uAA40-\uAA42\uAA44-\uAA4B\uAA60-\uAA76\uAA7A\uAA80-\uAAAF\uAAB1\uAAB5\uAAB6\uAAB9-\uAABD\uAAC0\uAAC2\uAADB-\uAADD\uABC0-\uABE2\uAC00-\uD7A3\uD7B0-\uD7C6\uD7CB-\uD7FB\uF900-\uFA2D\uFA30-\uFA6D\uFA70-\uFAD9\uFB00-\uFB06\uFB13-\uFB17\uFB1D\uFB1F-\uFB28\uFB2A-\uFB36\uFB38-\uFB3C\uFB3E\uFB40\uFB41\uFB43\uFB44\uFB46-\uFBB1\uFBD3-\uFD3D\uFD50-\uFD8F\uFD92-\uFDC7\uFDF0-\uFDFB\uFE70-\uFE74\uFE76-\uFEFC\uFF21-\uFF3A\uFF41-\uFF5A\uFF66-\uFFBE\uFFC2-\uFFC7\uFFCA-\uFFCF\uFFD2-\uFFD7\uFFDA-\uFFDC\d]/,
    __printable: /[^\u0000-\u001F\u007F-\u009F\u0378\u0379\u037F-\u0383\u038B\u038D\u03A2\u0526-\u0530\u0557\u0558\u0560\u0588\u058B-\u0590\u05C8-\u05CF\u05EB-\u05EF\u05F5-\u05FF\u0604\u0605\u061C\u061D\u0620\u065F\u070E\u074B\u074C\u07B2-\u07BF\u07FB-\u07FF\u082E\u082F\u083F-\u08FF\u093A\u093B\u094F\u0956\u0957\u0973-\u0978\u0980\u0984\u098D\u098E\u0991\u0992\u09A9\u09B1\u09B3-\u09B5\u09BA\u09BB\u09C5\u09C6\u09C9\u09CA\u09CF-\u09D6\u09D8-\u09DB\u09DE\u09E4\u09E5\u09FC-\u0A00\u0A04\u0A0B-\u0A0E\u0A11\u0A12\u0A29\u0A31\u0A34\u0A37\u0A3A\u0A3B\u0A3D\u0A43-\u0A46\u0A49\u0A4A\u0A4E-\u0A50\u0A52-\u0A58\u0A5D\u0A5F-\u0A65\u0A76-\u0A80\u0A84\u0A8E\u0A92\u0AA9\u0AB1\u0AB4\u0ABA\u0ABB\u0AC6\u0ACA\u0ACE\u0ACF\u0AD1-\u0ADF\u0AE4\u0AE5\u0AF0\u0AF2-\u0B00\u0B04\u0B0D\u0B0E\u0B11\u0B12\u0B29\u0B31\u0B34\u0B3A\u0B3B\u0B45\u0B46\u0B49\u0B4A\u0B4E-\u0B55\u0B58-\u0B5B\u0B5E\u0B64\u0B65\u0B72-\u0B81\u0B84\u0B8B-\u0B8D\u0B91\u0B96-\u0B98\u0B9B\u0B9D\u0BA0-\u0BA2\u0BA5-\u0BA7\u0BAB-\u0BAD\u0BBA-\u0BBD\u0BC3-\u0BC5\u0BC9\u0BCE\u0BCF\u0BD1-\u0BD6\u0BD8-\u0BE5\u0BFB-\u0C00\u0C04\u0C0D\u0C11\u0C29\u0C34\u0C3A-\u0C3C\u0C45\u0C49\u0C4E-\u0C54\u0C57\u0C5A-\u0C5F\u0C64\u0C65\u0C70-\u0C77\u0C80\u0C81\u0C84\u0C8D\u0C91\u0CA9\u0CB4\u0CBA\u0CBB\u0CC5\u0CC9\u0CCE-\u0CD4\u0CD7-\u0CDD\u0CDF\u0CE4\u0CE5\u0CF0\u0CF3-\u0D01\u0D04\u0D0D\u0D11\u0D29\u0D3A-\u0D3C\u0D45\u0D49\u0D4E-\u0D56\u0D58-\u0D5F\u0D64\u0D65\u0D76-\u0D78\u0D80\u0D81\u0D84\u0D97-\u0D99\u0DB2\u0DBC\u0DBE\u0DBF\u0DC7-\u0DC9\u0DCB-\u0DCE\u0DD5\u0DD7\u0DE0-\u0DF1\u0DF5-\u0E00\u0E3B-\u0E3E\u0E5C-\u0E80\u0E83\u0E85\u0E86\u0E89\u0E8B\u0E8C\u0E8E-\u0E93\u0E98\u0EA0\u0EA4\u0EA6\u0EA8\u0EA9\u0EAC\u0EBA\u0EBE\u0EBF\u0EC5\u0EC7\u0ECE\u0ECF\u0EDA\u0EDB\u0EDE-\u0EFF\u0F48\u0F6D-\u0F70\u0F8C-\u0F8F\u0F98\u0FBD\u0FCD\u0FD9-\u0FFF\u10C6-\u10CF\u10FD-\u10FF\u1249\u124E\u124F\u1257\u1259\u125E\u125F\u1289\u128E\u128F\u12B1\u12B6\u12B7\u12BF\u12C1\u12C6\u12C7\u12D7\u1311\u1316\u1317\u135B-\u135E\u137D-\u137F\u139A-\u139F\u13F5-\u13FF\u169D-\u169F\u16F1-\u16FF\u170D\u1715-\u171F\u1737-\u173F\u1754-\u175F\u176D\u1771\u1774-\u177F\u17DE\u17DF\u17EA-\u17EF\u17FA-\u17FF\u180F\u181A-\u181F\u1878-\u187F\u18AB-\u18AF\u18F6-\u18FF\u191D-\u191F\u192C-\u192F\u193C-\u193F\u1941-\u1943\u196E\u196F\u1975-\u197F\u19AC-\u19AF\u19CA-\u19CF\u19DB-\u19DD\u1A1C\u1A1D\u1A5F\u1A7D\u1A7E\u1A8A-\u1A8F\u1A9A-\u1A9F\u1AAE-\u1AFF\u1B4C-\u1B4F\u1B7D-\u1B7F\u1BAB-\u1BAD\u1BBA-\u1BFF\u1C38-\u1C3A\u1C4A-\u1C4C\u1C80-\u1CCF\u1CF3-\u1CFF\u1DE7-\u1DFC\u1F16\u1F17\u1F1E\u1F1F\u1F46\u1F47\u1F4E\u1F4F\u1F58\u1F5A\u1F5C\u1F5E\u1F7E\u1F7F\u1FB5\u1FC5\u1FD4\u1FD5\u1FDC\u1FF0\u1FF1\u1FF5\u1FFF\u2065-\u2069\u2072\u2073\u208F\u2095-\u209F\u20B9-\u20CF\u20F1-\u20FF\u218A-\u218F\u23E9-\u23FF\u2427-\u243F\u244B-\u245F\u26CE\u26E2\u26E4-\u26E7\u2700\u2705\u270A\u270B\u2728\u274C\u274E\u2753-\u2755\u275F\u2760\u2795-\u2797\u27B0\u27BF\u27CB\u27CD-\u27CF\u2B4D-\u2B4F\u2B5A-\u2BFF\u2C2F\u2C5F\u2CF2-\u2CF8\u2D26-\u2D2F\u2D66-\u2D6E\u2D70-\u2D7F\u2D97-\u2D9F\u2DA7\u2DAF\u2DB7\u2DBF\u2DC7\u2DCF\u2DD7\u2DDF\u2E32-\u2E7F\u2E9A\u2EF4-\u2EFF\u2FD6-\u2FEF\u2FFC-\u2FFF\u3040\u3097\u3098\u3100-\u3104\u312E-\u3130\u318F\u31B8-\u31BF\u31E4-\u31EF\u321F\u32FF\u4DB6-\u4DBF\u9FCC-\u9FFF\uA48D-\uA48F\uA4C7-\uA4CF\uA62C-\uA63F\uA660\uA661\uA674-\uA67B\uA698-\uA69F\uA6F8-\uA6FF\uA78D-\uA7FA\uA82C-\uA82F\uA83A-\uA83F\uA878-\uA87F\uA8C5-\uA8CD\uA8DA-\uA8DF\uA8FC-\uA8FF\uA954-\uA95E\uA97D-\uA97F\uA9CE\uA9DA-\uA9DD\uA9E0-\uA9FF\uAA37-\uAA3F\uAA4E\uAA4F\uAA5A\uAA5B\uAA7C-\uAA7F\uAAC3-\uAADA\uAAE0-\uABBF\uABEE\uABEF\uABFA-\uABFF\uD7A4-\uD7AF\uD7C7-\uD7CA\uD7FC-\uD7FF\uFA2E\uFA2F\uFA6E\uFA6F\uFADA-\uFAFF\uFB07-\uFB12\uFB18-\uFB1C\uFB37\uFB3D\uFB3F\uFB42\uFB45\uFBB2-\uFBD2\uFD40-\uFD4F\uFD90\uFD91\uFDC8-\uFDEF\uFDFE\uFDFF\uFE1A-\uFE1F\uFE27-\uFE2F\uFE53\uFE67\uFE6C-\uFE6F\uFE75\uFEFD\uFEFE\uFF00\uFFBF-\uFFC1\uFFC8\uFFC9\uFFD0\uFFD1\uFFD8\uFFD9\uFFDD-\uFFDF\uFFE7\uFFEF-\uFFF8\uFFFE\uFFFF\u2007]/,
    parse: function(pattern) {
        var delimiter = pattern.indexOf(";");

        var mask;
        var blankChar;
        if (delimiter == -1) {
            blankChar = ' ';
            mask = pattern;
        } else {
            mask = pattern.substring(0, delimiter);
            blankChar = delimiter + 1 < pattern.length ? pattern.charAt(delimiter + 1) : ' ';
        }

        var c = "";
        var separator = false;
        var escape = false;
        var caseMode = 0;
        var i;
        var matchFunc;
        var tokens = [];

        for (i in mask) {
            c = mask.charAt(i);
            if (escape) {
                separator = true;
                matchFunc = function(inputChar, maskChar) {
                    return inputChar == maskChar;
                }
                tokens.push({
                    maskChar: c,
                    isSeparator: separator,
                    caseMode: caseMode,
                    accept: matchFunc
                });
                escape = false;
            } else if (c == '<') {
                caseMode = -1;
            } else if (c == '>') {
                caseMode = 1;
            } else if (c == '!') {
                caseMode = 0;
            } else if (c != "[" && c != "]" && c != "{" && c != "}") {
                switch (c) {
                    case 'A':
                        matchFunc = function(inputChar) {
                            return $RWT.inputMaskUtils.__letter.test(inputChar);
                        }
                        separator = false;
                        break;
                    case 'a':
                        matchFunc = function(inputChar, maskChar) {
                            return inputChar == maskChar || $RWT.inputMaskUtils.__letter.test(inputChar);
                        }
                        separator = false;
                        break;
                    case 'N':
                        matchFunc = function(inputChar) {
                            return $RWT.inputMaskUtils.__letterOrDigit.test(inputChar);
                        }
                        separator = false;
                        break;
                    case 'n':
                        matchFunc = function(inputChar, maskChar) {
                            return inputChar == maskChar || $RWT.inputMaskUtils.__letterOrDigit.test(inputChar);
                        }
                        separator = false;
                        break;
                    case 'X':
                        matchFunc = function(inputChar) {
                            return $RWT.inputMaskUtils.__printable.test(inputChar);
                        }
                        separator = false;
                        break;
                    case 'x':
                        matchFunc = function(inputChar, maskChar) {
                            return inputChar == maskChar || $RWT.inputMaskUtils.__printable.test(inputChar);
                        }
                        separator = false;
                        break;
                    case '9':
                        matchFunc = function(inputChar) {
                            return $RWT.parseUtils.isDigit(inputChar.charCodeAt(0), 10);
                        }
                        separator = false;
                        break;
                    case '0':
                        matchFunc = function(inputChar, maskChar) {
                            return inputChar == maskChar || $RWT.parseUtils.isDigit(inputChar.charCodeAt(0), 10);
                        }
                        separator = false;
                        break;
                    case 'D':
                        matchFunc = function(inputChar) {
                            return $RWT.parseUtils.isDigit(inputChar.charCodeAt(0), 10) && inputChar != '0';
                        }
                        separator = false;
                        break;
                    case 'd':
                        matchFunc = function(inputChar, maskChar) {
                            return inputChar == maskChar || $RWT.parseUtils.isDigit(inputChar.charCodeAt(0), 10);
                        }
                        separator = false;
                        break;
                    case '#':
                        matchFunc = function(inputChar, maskChar) {
                            return inputChar == maskChar || $RWT.parseUtils.isDigit(inputChar.charCodeAt(0), 10) || inputChar == '+' || inputChar == '-';
                        }
                        separator = false;
                        break;
                    case 'H':
                        matchFunc = function(inputChar) {
                            return $RWT.parseUtils.isDigit(inputChar.charCodeAt(0), 16);
                        }
                        separator = false;
                        break;
                    case 'h':
                        matchFunc = function(inputChar, maskChar) {
                            return inputChar == maskChar || $RWT.parseUtils.isDigit(inputChar.charCodeAt(0), 16);
                        }
                        separator = false;
                        break;
                    case 'B':
                        matchFunc = function(inputChar) {
                            return inputChar == '0' || inputChar == '1';
                        }
                        separator = false;
                        break;
                    case 'b':
                        matchFunc = function(inputChar, maskChar) {
                            return inputChar == maskChar || inputChar == '0' || inputChar == '1';
                        }
                        separator = false;
                        break;
                    case '\\':
                        escape = true;
                        separator = true;
                        break;
                    default:
                        matchFunc = function(inputChar, maskChar) {
                            return inputChar == maskChar;
                        }
                        separator = true;
                        break;
                }

                if (!escape) {
                    tokens.push({
                        maskChar: c,
                        isSeparator: separator,
                        caseMode: caseMode,
                        accept: matchFunc
                    });
                }
            }
        }
        return {
            tokens: tokens,
            blankChar: blankChar
        };
    },
    putChar: function(inputChar, beginPos, endPos, currentString, tokens, blankChar, force) {
        var i;
        var newString = "";
        for (i = 0; i < currentString.length; i++) {
            if (i < tokens.length) {
                if (i < beginPos) {
                    newString += currentString.charAt(i);
                }
                else if (tokens[i].isSeparator) {
                    newString += tokens[i].maskChar;
                }
                else if (tokens[i].accept(inputChar, blankChar) || force) {
                    if (tokens[i].caseMode < 0) {
                        newString += inputChar.toLowerCase();
                    }
                    else if (tokens[i].caseMode > 0) {
                        newString += inputChar.toUpperCase();
                    }
                    else {
                        newString += inputChar;
                    }
                    if (endPos > beginPos) {
                        var j;
                        for (j = i + 1; j < endPos && j < tokens.length; j++) {
                            newString += tokens[j].isSeparator ? tokens[j].maskChar : blankChar;
                        }
                    }
                    if (newString.length < currentString.length) {
                        newString += currentString.substring(newString.length);
                    }
                    return {
                        newString: newString,
                        pos: i
                    };
                }
                else {
                    if (endPos == beginPos) {
                        return {
                            newString: currentString,
                            pos: -1
                        };//Can't put
                    }
                    else if (i < endPos) {
                        newString += blankChar;
                    }
                    else {
                        newString += currentString.substring(i);
                        return {
                            newString: newString,
                            pos: -1
                        };//Can't put
                    }
                }
            }
        }
        return {
            newString: newString,
            pos: -1
        };
    },
    apply: function(tokens, blankChar, sourceString, startIndex, currentString) {

        var resultString = "";
        var i, j = 0;
        for (i = startIndex; i < tokens.length && j < sourceString.length; ) {
            if (tokens[i].isSeparator) {
                resultString += tokens[i].maskChar;
                if (sourceString.charAt(j) == tokens[i].maskChar) {
                    j++;
                }
                i++;
            }
            else {
                if (tokens[i].accept(sourceString.charAt(j))) {
                    switch (tokens[i].caseMode) {
                        case 1:
                            resultString += sourceString.charAt(j).toUpperCase();
                            break;
                        case -1:
                            resultString += sourceString.charAt(j).toLowerCase();
                            break;
                        default:
                            resultString += sourceString.charAt(j)
                    }
                    i++;
                }
                else {
                    // search for separator first                    
                    var pos = $RWT.inputMaskUtils._findSeparatorPosition(tokens, i, sourceString.charAt(j));
                    if (pos > -1) {
                        //example: For pattern "9999-9999-9999;_" and input string "33-22-33" 
                        //result must be 33__-22__-33__
                        if (sourceString.length != 1 || i == 0 ||
                                (i > 0 && (!tokens[i - 1].isSeparator || tokens[i - 1].maskChar != sourceString.charAt(j)))
                                ) {
                            resultString += currentString.substring(i, pos + 1);
                            i = pos + 1;
                        }
                    }
                    else {// search for valid significant if not
                        var pos = $RWT.inputMaskUtils._findSignificantCharacterPosition(tokens, i, sourceString.charAt(j));
                        if (pos > 0) {
                            //example: For pattern "9999>AAAA9999;_" and input string "33bb22" 
                            //result must be 33__BB__22__
                            if (i < pos - 1) {
                                resultString += currentString.substring(i, pos);
                            }
                            switch (tokens[i].caseMode) {
                                case 1:
                                    resultString += sourceString.charAt(j).toUpperCase();
                                    break;
                                case -1:
                                    resultString += sourceString.charAt(j).toLowerCase();
                                    break;
                                default:
                                    resultString += sourceString.charAt(j)
                            }
                            i = pos + 1; // updates i to find + 1                            
                        }
                    }
                }
                j++;
            }
        }
        return resultString;
    },
    buildClearString: function(tokens, blankChar, startPos, endPos) {
        var resultString = "";
        var i;
        for (i = startPos; i <= endPos && i < tokens.length; i++) {
            resultString += tokens[i].isSeparator ? tokens[i].maskChar : blankChar;
        }
        return resultString;
    },
    _findSeparatorPosition: function(tokens, pos, searchChar) {
        if (pos >= tokens.length || pos < 0)
            return -1;

        var i;
        for (i = pos; i < tokens.length; i++) {
            if (tokens[i].isSeparator && tokens[i].maskChar == searchChar) {
                return i;
            }
        }

        return -1;
    },
    _findSignificantCharacterPosition: function(tokens, pos, searchChar) {
        if (pos >= tokens.length || pos < 0)
            return -1;

        var i;
        for (i = pos; i < tokens.length; i++) {
            if (!tokens[i].isSeparator && tokens[i].accept(searchChar)) {
                return i;
            }
        }
        return -1;
    },
    getPositionOfFirstBlankCharInDisplayString: function(tokens, blankChar, displayString) {
        var blankCharPosition = -1;
        var i;
        for (i = displayString.length - 1; i >= 0; i--) {
            if (i < tokens.length && !tokens[i].isSeparator) {
                if (displayString.charAt(i) == blankChar) {
                    blankCharPosition = i;
                }
                else {
                    return blankCharPosition;
                }
            }
        }
        return blankCharPosition;
    },
    updateCursorPosition: function(input, onfocus) {
        var curPos = $RWT.textField.caret($(input));
        var pos = $RWT.inputMaskUtils.getPositionOfFirstBlankCharInDisplayString(input.tokens, input.blankChar, input.value);
        if ((curPos.begin > pos && pos > -1) || onfocus) {
            var moveCaret = function() {
                if (pos > -1) {
                    $RWT.textField.caret($(input), pos);
                }
                else {
                    $RWT.textField.caret($(input), 0, $(input).val().length);
                }
            };
            if (onfocus) {
                if ($.browser) {//for jquery<1.9 compability
                    ($.browser.msie ? moveCaret : function() {
                        setTimeout(moveCaret, 0);
                    })();
                }
                else if ($.support) {
                    ($.support.msie ? moveCaret : function() {
                        setTimeout(moveCaret, 0);
                    })();
                }
            }
            else {
                moveCaret();
            }
        }
    },
    moveCursorToRight: function(input, beginPos) {
        var pos;
        for (pos = beginPos + 1; pos < input.tokens.length; pos++) {
            if (!input.tokens[pos].isSeparator) {
                var moveCaret = function() {
                    $RWT.textField.caret($(input), pos);
                };
                if ($.browser) {//for jquery<1.9 compability
                    ($.browser.msie ? moveCaret : function() {
                        setTimeout(moveCaret, 0);
                    })();
                    return;
                } else if ($.support) {
                    ($.support.msie ? moveCaret : function() {
                        setTimeout(moveCaret, 0);
                    })();
                    return;
                }
            }
        }
    }
}
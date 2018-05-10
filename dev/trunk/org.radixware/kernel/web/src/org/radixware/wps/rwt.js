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
var request_url = "explorer.html";
if (!document.ELEMENT_NODE) {
    document.ELEMENT_NODE = 1;
    document.ATTRIBUTE_NODE = 2;
    document.TEXT_NODE = 3;
    document.CDATA_SECTION_NODE = 4;
    document.ENTITY_REFERENCE_NODE = 5;
    document.ENTITY_NODE = 6;
    document.PROCESSING_INSTRUCTION_NODE = 7;
    document.COMMENT_NODE = 8;
    document.DOCUMENT_NODE = 9;
    document.DOCUMENT_TYPE_NODE = 10;
    document.DOCUMENT_FRAGMENT_NODE = 11;
    document.NOTATION_NODE = 12;
}

$RWT = {
    DEBUG: false,
    __console_log: function (message) {
        if ($RWT.DEBUG === true && console && console.log)
            console.log(message);
    }
};

$RWT._importNode = function (node, allChildren, update) {
    function importNode(parent, node, allChildren, queue) {
        switch (node.nodeType) {
            case document.ELEMENT_NODE:
                var nodeName = node.nodeName;
                var nodeNameLength = nodeName.length;
                var nodeTextInBase64 = nodeName.indexOf('__in_base64__', nodeNameLength - 13/*13='__in_base64__'.length*/) !== -1;
                if (nodeTextInBase64) {
                    nodeName = nodeName.substr(0, nodeNameLength - 13);
                }
                var processAttribute = function (node, attribute) {
                    var name = attribute.name;
                    var value = attribute.value;
                    var nameLength = name.length;
                    var attrValueInBase64 = name.indexOf('__in_base64__', nameLength - 13/*13='__in_base64__'.length*/) !== -1;
                    if (attrValueInBase64) {
                        name = name.substr(0, nameLength - 13);
                        value = $RWT.utils.base64.decodeWithUTF(value);
                    }
                    if (name === 'style') {
                        node.style.cssText = value;
                    } else if (name === 'class') {
                        node.className = value;
                    } else {
                        if (name == "value" || name == "focusedtext" || name == "initialtext") {
                            var regex = /<br\s*[\/]?>/gi;
                            value = value.replace(regex, "\n");
                        }
                        attrUpdate(node, name, value);
                    }
                };
                var newNode = document.createElement(nodeName);
                if (nodeTextInBase64) {
                    newNode.__rwt_inner_text_in_base64__ = true;
                }
                var i, il;
                if (node && node.attributes && node.attributes.length > 0) {
                    for (i = 0, il = node.attributes.length; i < il; i++) {
                        processAttribute(newNode, node.attributes[i]);
                    }
                }

                else if (update && update.attributes && update.attributes.length > 0) {
                    //var updateNodes = update;
                    for (i = 0, il = update.attributes.length; i < il; i++) {
                        processAttribute(newNode, update.attributes[i]);
                    }
                }

                if (allChildren && node.childNodes && node.childNodes.length > 0) {
                    for (i = 0, il = node.childNodes.length; i < il; i++) {
                        queue.push(node.childNodes[i]);
                    }
                }
                return newNode;
                break;
            case document.TEXT_NODE:
            case document.CDATA_SECTION_NODE:
            case document.COMMENT_NODE:
                if (parent != null && parent.className.indexOf('rwt-html-display-component') >= 0) {
                    var htmltext = $RWT.utils.base64.decodeWithUTF(node.nodeValue);
                    try {
                        var stub = $(htmltext);
                        if (stub == null || stub.length == 0) {
                            return document.createTextNode(htmltext);
                        } else {
                            parent.innerHTML = htmltext;
                            return null;
                        }
                    } catch (e) {
                        return document.createTextNode(htmltext);
                    }
                } else {
                    var htmltext;
                    if (parent != null && parent.__rwt_inner_text_in_base64__ === true) {
                        htmltext = $RWT.utils.base64.decodeWithUTF(node.nodeValue);
                    } else {
                        htmltext = node.nodeValue;
                    }
                    return document.createTextNode(htmltext);
                }
                break;
        }
    }
    var children = [];
    var newNode = importNode(null, node, allChildren, children);
    if ($RWT._importNode._all)
        delete $RWT._importNode._all;
    $RWT._importNode._all = [];
    $RWT._importNode._all.push({
        'n': newNode,
        'c': children
    });
    while ($RWT._importNode._all.length > 0) {
        var orig = $RWT._importNode._all[0].c;
        var parent = $RWT._importNode._all[0].n;
        $RWT._importNode._all.splice(0, 1);
        for (var c = 0; c < orig.length; c++) {
            var childChildren = [];
            var child = importNode(parent, orig[c], allChildren, childChildren);
            if (child != null) {
                parent.appendChild(child);
                if (childChildren.length > 0) {
                    $RWT._importNode._all.push({
                        'n': child,
                        'c': childChildren
                    });
                }
            }
        }
        delete orig;
    }
    delete $RWT._importNode._all;
    return newNode;
}


document._findNodeById = function (id) {
    var node = document.getElementById(id);
    return node;
}

function attrUpdate(node, name, value) {
    if (node == null)
        return;
    var res;
    if (name === 'onclick') {
        if (value === 'default') {
            node.onclick = $RWT.handleClick;
        } else
            node.onclick = eval(value);

    } else if (name === 'onmousedown' ||
            name === 'ondblclick' ||
            name === 'onkeydown' ||
            name === 'onkeyup' ||
            name === 'onkeypress' ||
            name === 'onpaste' ||
            name === 'oncut' ||
            name === 'onscroll' ||
            name === 'onchange' ||
            name === 'oninput' ||
            name === 'ondragend' ||
            name === 'ondragover' ||
            name === 'ondragenter' ||
            name === 'ondragleave' ||
            name === 'ondrop' ||
            name === 'oncontextmenu' ||
            name === 'onmouseover' ||
            name === 'onmouseout' ||
            name === 'onmouseenter' ||
            name === 'onmouseleave') {

        node[name] = eval(value);
    }
    else if (name === 'onmousewheel') {
        node[name] = eval(value);
        if (node.addEventListener) {//fix for FF
            node.addEventListener('DOMMouseScroll', eval(value), false);
        }
    } else if (name == 'onfocus') {
        res = eval(value);
        $(node).focus(res);
    } else if (name == 'checked') {
        node.setAttribute(name, value);
        node.checked = true;
    } else if (name == 'onblur') {
        res = eval(value);
        $(node).blur(res);
    } else if (name.indexOf("rwt_f_") == 0) {
        node[name] = eval(value);
    } else {
        if ((name === 'title' || name === 'shortcutIcon') && WpsUtils.getNodeLocalName(node).toLowerCase() == 'body') {
            var header = WpsUtils.findChildByLocalName(document.documentElement, 'head');
            if (header) {
                if (name === 'title') {
                    var title = WpsUtils.findChildByLocalName(header, 'title');
                    if (title) {
                        try {
                            WpsUtils.text(title, value);
                        } catch (e) {
                            //ignore error in IE
                        }
                    }   
                } else {
                    var link = null;
                    WpsUtils.iterateNames(header, 'link', function (l) {
                        var a = l.getAttribute('rel');
                        if (a !== null && a.toLowerCase() === 'shortcut icon') {
                            link = l;
                            return true;
                        }
                    });
                    if (link !== null) {
                        $(link).remove();
                    }
                    WpsUtils.appendNamedElement(header, 'link', null, {
                        'rel': 'shortcut icon',
                        'href': value,
                        'type': 'image/png'
                    });
                }
            }
        } else {
            node.setAttribute(name, value);
            if (name === 'value') {
                try {
                    node.value = value;
                    if (node.rwt_f_onChangeValue != null) {
                        node.rwt_f_onChangeValue(node, value);
                    }
                } catch (e) {

                }
            }
        }
    }
}

$RWT.FilteredEvent = {
    process: function(obj,e){        
        if (e.type == "keydown" || e.type == "keyup" || e.type == "click" || e.type == "dblclick" || e.type == "mousedown" || e.type == "contextmenu") {
            var filters = JSON.parse($(obj).attr("on" + e.type + "params"));            
            for (var i in filters) {
                if ($RWT.FilteredEvent._checkEventFilterCondition(filters[i], e)) {
                    $RWT.events.cancelEvent(e);
                    if (e.type == "contextmenu") {
                        $RWT.FilteredEvent.sendEvent(obj, e, e.clientY + "," + e.clientX, null);
                    } else {
                        $RWT.FilteredEvent.sendEvent(obj, e, null, null);
                    }
                    return;
                }
            }
        }
    },
    sendEvent: function(obj, event, params, callback){
        var eventParams = $RWT.FilteredEvent._writeEventPamsToJSONString(event, params);        
        $RWT.actions.event(obj, "filteredevent", eventParams, callback);
    },
    _getEventButton: function(event){
        switch (event.type){
            case "keydown":
            case "keyup":
                return event.keyCode;
            case "click":
            case "dblclick":
            case "mousedown":
            case "contextmenu":    
                return event.which;
            default:
                return null;
        }
    },
    _writeEventPamsToJSONString: function(event, params) {
        var button = $RWT.FilteredEvent._getEventButton(event);
        var modifiers = $RWT.FilteredEvent._getKeyboardModifiers(event);
        return JSON.stringify({modifier: modifiers==null ? "null" : modifiers, button: button, type: event.type, params: params});
    },
    _checkModifiers: function(modifiers, event) {
        var ctrlButton = false;
        var shiftButton = false;
        var altButton = false;
        var metaButton = false;
        if (modifiers!=null){
            for (j in modifiers) {
                var modif = modifiers[j];
                switch (modif) {
                    case("ALT") :
                        altButton = true;
                        break;
                    case("SHIFT") :
                        shiftButton = true;
                        break;
                    case("CTRL") :
                        ctrlButton = true;
                        break;
                    case("META") :
                        metaButton = true;
                        break;
                    case("ANY") :
                        return true;
                }
            }
        }
        return ctrlButton == event.ctrlKey 
               && shiftButton == event.shiftKey 
               && altButton == event.altKey 
               && metaButton == event.metaKey;
    },
    _checkEventFilterCondition: function (condition, event) {
        var eventButton = $RWT.FilteredEvent._getEventButton(event);
        return parseInt(condition.button) === eventButton 
               && $RWT.FilteredEvent._checkModifiers(condition.modifier, event);
    },
    _getKeyboardModifiers: function (event) {
        var modifiers = [];
        if (event.ctrlKey) {
            modifiers.push("CTRL");
        }
        if (event.shiftKey) {
            modifiers.push("SHIFT");
        }
        if (event.altKey) {
            modifiers.push("ALT");
        }
        if (event.metaKey) {
            modifiers.push("META");
        }
        return modifiers;
    }   
}

$RWT.onFilteredEvent = function (event) {
   $RWT.FilteredEvent.process(this, event);
}

$RWT.makeHttpRequest = function () {
    try
    {
        // Firefox, Opera 8.0+, Safari      
        return new XMLHttpRequest();
    }
    catch (e)
    {    // Internet Explorer      
        try
        {
            return new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e)
        {
            try
            {
                return new ActiveXObject("Microsoft.XMLHTTP");
            }
            catch (e)
            {
                alert("Your browser does not support AJAX!");
                return false;
            }
        }
    }
}


function rdx_ajax(requestData, callback, priority, isTimerEvent) {
    try {
        if (!$RWT._rqq) {
            $RWT._rqq = [];
        }
        var rq = {
            data: requestData,
            cb: callback,
            processing: false,
            isTimerEvent: isTimerEvent
        };
        if (priority === 'hight' && $RWT._rqq.length > 0) {
            if ($RWT._rqq[0].processing === true) {
                $RWT._rqq.splice(1, 0, rq);
            } else {
                $RWT._rqq.unshift(rq);
            }
        } else {
            $RWT._rqq.push(rq);
        }
        if ($RWT._rqq[0] == rq) {
            $RWT.ajax.sendRequest(rq);            
        }
    } catch (e) {
        if (console && console.log) {
            console.log(e);
        }
    }
}


function spliceRq(rq) {
    try {
        if ($RWT._rqq) {
            if ($RWT._rqq[0] == rq.rwt_rq) {
                $RWT._rqq.splice(0, 1);
            } else {
                if (console && console.log) {
                    console.log('wrong request sequence');
                }
            }
            if ($RWT._rqq.length > 0) {
                $RWT.ajax.sendRequest($RWT._rqq[0]);
            }
        }
    } catch (e) {
        if (console && console.log) {
            console.log(e);
        }
    }
}

function resetRqs() {
    try {
        delete $RWT._rqq;
    } catch (e) {
        if (console && console.log) {
            console.log(e);
        }
    }
}

function get_client_locale() {
    if (navigator.userLanguage) // Explorer
        return navigator.userLanguage;
    else if (navigator.language) // FF
        return navigator.language;
    else
        return "en";
}

$RWT.ajax = {
    _alertError: function(e){
        var text = e.name + "\n" + e.message + "\n" + e.description;
        for (var txt in e) {
            try {
                text += "\n" + txt + ': ' + e[txt];
            } catch (ee) {

            }
        }
        text += '\n';
        if (e.stack) {
            text += e.stack;
        } else {
            var currentFunction = arguments.callee.caller;
            while (currentFunction) {
                text += currentFunction.name + '\n'
                currentFunction = currentFunction.caller;
            }
        }
        alert(text);
    },
    _loadScript: function(scriptName, view, flow, rootId, rq, callback) {

        var head = document.getElementsByTagName('head')[0];
        var src = 'rwtext/' + scriptName;
        var ex = null;
        WpsUtils.iterateNames(head, 'script', function (s) {
            if ($(s).attr('src') == src) {
                ex = s;
                return true;
            }
        });
        if (ex != null) {
            //script exists
            return false;
        }
        var script = document.createElement('script');
        script.flow = flow;
        script.type = 'text/javascript';
        script.rwt_callback = callback;
        script.rwt_rq = rq;
        script.counter = 0;
        script.rwt_done = function (e) {

            this.counter++;
            this.flow.script_count--;

            if (this.flow.script_count == 0) {
                $RWT.ajax._doProcessResponse(view, this.rwt_rq, this.rwt_callback, rq);
            }
        };

        if (window.ActiveXObject) {//ie
            script.onreadystatechange = function () {
                if (this.readyState == 'complete' || this.readyState == 4) {
                    script.rwt_done();
                }
            }
        } else {
            script.onload = script.rwt_done;
        }

        if (navigator && navigator.appName == 'Microsoft Internet Explorer') {
            script.onreadystatechange = function () {
                if (script.readyState == 'loaded' || script.readyState == 'complete') {
                    this.rwt_done();
                }
            };
        }

        script.src = src;

        head.appendChild(script);
        return true;
    },
    _loadCss: function(scriptName, view, rootId) {
        var head = document.getElementsByTagName('head')[0];
        var href = 'rwtext/' + scriptName;
        var ex = null;
        WpsUtils.iterateNames(head, 'link', function (l) {
            if ($(l).attr('href') === href) {
                ex = l;
                return true;
            }
        });
        if (ex != null)
            return;
        var link = document.createElement('link');
        link.rel = 'stylesheet';
        link.type = 'text/css';
        link.href = href;
        head.appendChild(link);
    },
    _saveLayouts: function(update, layouts) {
        WpsUtils.iterateNames(update, 'rwt-layout', function (l) {
            var id = l.getAttribute('id');
            var func = l.getAttribute('func');
            if (id && func) {
                layouts.push({
                    lid: id,
                    lfunc: func
                });
            }
        });
    },
    _applyLayouts:  function(layouts) {
        for (var i = 0; i < layouts.length; i++) {
            var linfo = layouts[i];
            var node = $('#' + linfo.lid);
            if (node && node.length > 0) {
                try {
                    node[0].rwt_layout = eval(linfo.lfunc);
                } catch (e) {
                    alert("Unable to resolve layout function:" + linfo.lfunc);
                }
            }
        }
    },
    _updateObject: function(id, updates, layouts) {
        var update = updates[id];

        if (update != null) {
            try {
                var updateKind = update.getAttribute('type');

                if (updateKind === 'init') {
                    var element = WpsUtils.findFirstChild(update);
                    if (WpsUtils.getNodeLocalName(element).toLowerCase() === 'body') {//only body should be added directly

                        var body = $RWT._importNode(element, true, update);
                        var exBody = document.getElementsByTagName('body');
                        if (exBody.length > 0) {
                            for (var i = 0; i < exBody.length; i++) {
                                exBody[i].parentNode.removeChild(exBody[i]);
                            }
                        }
                        document.documentElement.appendChild(body);
                        exBody = document.getElementsByTagName('body');
                    }
                } else if (updateKind === 'update') {
                    var node = document._findNodeById(id);
                    if (node != null) {
                        var u_remove = [];
                        var u_add = [];

                        WpsUtils.iterateAll(update, function (e) {
                            var partName = WpsUtils.getNodeLocalName(e);
                            if ('style-change' === partName) {
                                var type = e.getAttribute('type');
                                if (type === 'remove') {
                                    $(node).css(e.getAttribute('name'), '');
                                } else {
                                    $(node).css(e.getAttribute('name'), e.getAttribute('value'));
                                }
                            } else if ('class-change' === partName) {
                                node.className = e.getAttribute('class');
                            } else if ('attr-change' === partName) {
                                var value = e.getAttribute('value');
                                if (e.getAttribute('name') == "value") {
                                    var regex = /<br\s*[\/]?>/gi;
                                    value = value.replace(regex, "\n");
                                }

                                attrUpdate(node, e.getAttribute('name'), value);
                            } else if ('attr-change-base64' === partName) {
                                var value = $RWT.utils.base64.decodeWithUTF(e.getAttribute('value'));
                                attrUpdate(node, e.getAttribute('name'), value);
                            } else if ('attr-remove' === partName) {
                                var aname = e.getAttribute('name');
                                node[aname] = null;
                                node.removeAttribute(aname);
                                if (aname == 'checked') {
                                    node.checked = false;
                                }
                            } else if ('text-change' === partName) {
                                if (node.className && node.className.indexOf('rwt-html-display-component') >= 0) {
                                    var htmltext = $RWT.utils.base64.decodeWithUTF(WpsUtils.text(e));
                                    try {
                                        var stub = $(htmltext);
                                        if (stub == null || stub.length == 0) {
                                            WpsUtils.text(node, htmltext);
                                        } else {
                                            node.innerHTML = htmltext;                                            
                                        }
                                    } catch (e) {
                                        WpsUtils.text(node, htmltext);
                                    }                                                                        
                                } else {
                                    WpsUtils.text(node, WpsUtils.text(e));
                                }
                            } else if ('text-change-base64' === partName) {
                                var htmltext = $RWT.utils.base64.decodeWithUTF(WpsUtils.text(e));
                                WpsUtils.text(node, htmltext);
                            } else if ('child-add' === partName) {
                                u_add.push(e);
                            } else if ('child-remove' === partName) {
                                u_remove.push(e);
                            }
                        });
                        for (var a = 0; a < u_remove.length; a++) {
                            var e = u_remove[a];
                            childId = e.getAttribute('id');

                            var childNode = null;
                            WpsUtils.iterateAll(node, function (c) {
                                if (c.getAttribute('id') === childId) {
                                    childNode = c;
                                    return true;
                                } else
                                    return false;
                            });
                            if (childNode == null) {
                                childNode = document._findNodeById(childId);
                            }

                            if (childNode) {

                                if (childNode.bodyId) {
                                    var b = $('#' + childNode.bodyId);
                                    if (b && b.length > 0) {
                                        var parent = b[0].parentNode;
                                        if (parent) {
                                            parent.removeChild(b[0]);
                                        }
                                    }
                                }
                                if (childNode.parentNode != null) {
                                    childNode.parentNode.removeChild(childNode);
                                }
                                if (childNode.ui_dialog) {
                                    $RWT.dialog.close(childNode);
                                }
                            }
                        }
                        u_add.sort(function (o1, o2) {
                            var pos1 = parseInt(o1.getAttribute('flow'));
                            var pos2 = parseInt(o2.getAttribute('flow'));
                            return pos1 == pos2 ? 0 : (pos1 > pos2 ? 1 : -1);
                        });
                        for (a = 0; a < u_add.length; a++) {
                            e = u_add[a];
                            var childId = e.getAttribute('id');
                            var pos = parseInt(e.getAttribute('flow'));
                            if (childId) {
                                var childXml = updates[childId];
                                if (childXml && childXml.getAttribute('type') === 'init') {
                                    var childHtml = $RWT._importNode(WpsUtils.findFirstChild(childXml), true, null);

                                    if (childHtml) {
                                        var instead = null;
                                        var cc = -1;
                                        for (var r = node.firstChild; r != null; r = r.nextSibling) {
                                            if (r.nodeType == 1) {
                                                cc++;
                                                if (cc == pos) {
                                                    instead = r;
                                                    break;
                                                }
                                            }
                                        }
                                        if (instead != null) {
                                            node.insertBefore(childHtml, instead)
                                        }
                                        else
                                            node.appendChild(childHtml);
                                    }
                                    $RWT.ajax._saveLayouts(childXml, layouts);
                                    updates[childId] = null;
                                }
                            }
                        }
                    }
                }
                $RWT.ajax._saveLayouts(update, layouts);
            } catch (e) {
                $RWT.ajax._alertError(e);
            }
            finally {
                updates[id] = null;
            }
        }
    },
    _checkFocus:  function(view) {
        var focusRequest = WpsUtils.findChildByLocalName(view, 'focus');
        var focusTarget = null;
        if (focusRequest) {
            var focusId = focusRequest.getAttribute('id');
            if (focusId) {
                var rqData = $('#' + focusId);
                if (rqData.length > 0) {
                    focusTarget = rqData[0];
                }
            }
        }

        if (!focusTarget)
            focusTarget = $RWT.currentFocusLayer().target;

        if (focusTarget && focusTarget.parentNode) {
            if (focusTarget.rwt_f_requestFocus) {
                focusTarget.rwt_f_requestFocus(focusTarget);
            } else {
                if (focusTarget.ownerDocument.activeElement !== focusTarget) {
                    $(focusTarget).focus();
                    $RWT._registerFocuseEvent(focusTarget);
                }
            }
        }
    },
    _doProcessResponse:  function(view, rqId, callback, srcrq) {
        try {

            var updates = {}
            var bodyId;


            WpsUtils.iterateNames(view, 'update-element', function (e) {
                var body = WpsUtils.findChildByLocalName(e, 'body');
                if (body) {
                    bodyId = e.getAttribute('id');
                }
                updates[e.getAttribute('id')] = e;
            });
            try {
                var layouts = [];

                var uc = 0;
                if (bodyId != null) {//we should start from body
                    $RWT.ajax._updateObject(bodyId, updates, layouts);
                    uc++;
                }
                for (var id in updates) {
                    $RWT.ajax._updateObject(id, updates, layouts);
                    uc++;
                }
            } finally {
                $RWT.ajax._applyLayouts(layouts);
            }

            if (uc > 0) {


                var rr_rs = $RWT.layoutDocument(rqId);
                $RWT._ignoreFocusCheck = false;

                if (bodyId != null) {
                    if (!rr_rs[bodyId]) {
                        rr_rs[bodyId] = 'default';
                    }
                }
                if (!srcrq.rwt_rq.isTimerEvent) {
                    $RWT.ajax._checkFocus(view);
                }

                if (rr_rs && !WpsUtils.isEmpty(rr_rs)) {
                    $RWT.notifyRendered(rr_rs, rqId);
                }
            } else {
                $RWT._ignoreFocusCheck = false;
                if (!srcrq.rwt_rq.isTimerEvent) {
                    $RWT.ajax._checkFocus(view);
                }
            }
            var downloadSet = WpsUtils.findChildByLocalName(view, 'transmissions');
            if (downloadSet) {
                WpsUtils.iterateNames(downloadSet, 'transmission', function (t) {
                    var name = $(t).attr('name');
                    var url = $(t).attr('url');
                    var src = $(t).attr('src');
                    var save = $(t).attr('save');
                    if (url) {
                        if (src) {
                            $RWT.files.upload(src, url);
                        } else {
                            if (save == 'true') {
                                $RWT.files.downloadAndSave(name ? name : '', url);
                            } else {
                                $RWT.files.download(name ? name : '', url);
                            }
                        }
                    }
                });
            }
            if (callback) {
                try {
                    callback();
                } catch (e) {
                    //ignore
                }

            }

        } finally {
            $RWT.ajax._destroyWaitBox();
            spliceRq(srcrq);
        }
    },
    _processResponse: function(xml, rqId, callback, rq) {
        var isWaitBoxUpdate = false;
        try {
            if (xml == null || xml.documentElement == null) {
                if (rq != null && rq.responseText != null) {
                    var message =
                            "Failed to parse response. Request=\"" + rq.responseURL + "\" resposeText=\"" + rq.responseText + "\"";
                    WpsUtils.print(message);
                }
                spliceRq(rq);
                if (callback) {
                    callback();
                }
                return;
            } else if (rq != null && rq.responseText != null && rq.responseText == "<disposed/>") {
                return;
            }
            var view = xml.documentElement;
            this._rqToken = view.getAttribute('token');
            if (WpsUtils.getNodeLocalName(view) == 'redirect') {
                var url_string = window.location.href;
                if (url_string.indexOf("?") > 0) {
                    var url = url_string;
                    var newUrl = url_string.split('?')[0];
                    if (newUrl != url) {
                        try{ //fix for Edge error window
                            window.location.replace(newUrl);
                        } catch (e) {
                           
                        }
                        return;
                    }
                }
            } if (WpsUtils.getNodeLocalName(view) == 'timer') {
                $RWT.timer.processResponse(view, callback);
                spliceRq(rq);
                return;
            }
            else if (WpsUtils.getNodeLocalName(view) == 'waitbox') {
                isWaitBoxUpdate = true;
                $RWT.waitWindow.processResponse(view);
                spliceRq(rq);
                if (callback) {
                    callback();
                }
                return;
            } else if (WpsUtils.getNodeLocalName(view) == 'textsigner') {
                $RWT.signer.processResponse(view);
                spliceRq(rq);
                if (callback) {
                    callback();
                }
                return;
            } else if (WpsUtils.getNodeLocalName(view) == 'jsinvoke') {
                $RWT.jsInvoker.processResponse(view);
                spliceRq(rq);
                if (callback) {
                    callback();
                }
                return;
            }
            var rootId = view.getAttribute('root');
            if (WpsUtils.getNodeLocalName(view) == 'failure') {
                var reason = $(view).attr('reason');
                if (reason == 'timeout') {
                    try {
                        window.onunload = null;
                        location.reload(0);
                    } catch (e) {

                    }
                    return;
                }
                var body = WpsUtils.findChildByLocalName(view, 'body');
                if (body) {
                    body = $RWT._importNode(body, true, null);
                    $('body').remove();
                    document.documentElement.appendChild(body);
                }
                spliceRq(rq);
                if (callback) {
                    callback();
                }
                return;
            }

            var scripts = new Array();

            var cssSet = WpsUtils.findChildByLocalName(view, 'styles');
            if (cssSet) {
                WpsUtils.iterateNames(cssSet, 'style', function (e) {
                    var text = WpsUtils.getText(e);
                    if (text) {
                        $RWT.ajax._loadCss(text, view, rootId);
                    }
                });
            }

            var cookiesSet = WpsUtils.findChildByLocalName(view, 'cookies');
            if (cookiesSet) {
                WpsUtils.iterateNames(cookiesSet, 'cookie', function (e) {
                    var name = e.getAttribute('name');
                    var value = e.getAttribute('value');
                    if (name != 'paramsId') {
                        $RWT.cookies.eraseCookie(name);
                        $RWT.cookies.createCookie(name, value, 60);
                    }
                });
            }


            var scriptSet = WpsUtils.findChildByLocalName(view, 'scripts');
            var scriptFlow = {
                script_count: 0
            };

            if (scriptSet) {
                WpsUtils.iterateNames(scriptSet, 'script', function (e) {
                    var text = WpsUtils.getText(e);
                    if (text) {
                        scripts[scriptFlow.script_count] = {
                            t: 'j',
                            v: text
                        };
                        scriptFlow.script_count++;
                    }

                });
            }

            if (scriptFlow.script_count === 0) {
                $RWT.ajax._doProcessResponse(view, rqId, callback, rq);
            } else {
                var someScriptIsNew = false;
                for (var idx in scripts) {
                    var obj = scripts[idx];
                    if (obj.t === 'j') {
                        if ($RWT.ajax._loadScript(obj.v, view, scriptFlow, rootId, rq, callback)) {
                            someScriptIsNew = true;
                        }
                    }
                }
                if (someScriptIsNew == false) {
                    $RWT.ajax._doProcessResponse(view, rqId, callback, rq);
                }
            }

            var timerCommand = WpsUtils.findChildByLocalName(view, 'object-timer');


            if (timerCommand) {
                var command = timerCommand.getAttribute('command');
                if (command === 'start') {
                    $RWT.timer.startUITicks();
                } else if (command === 'stop') {
                    $RWT.timer.stopUITicks();
                }
            }
        } catch (e) {
            $RWT.ajax._alertError(e);
        } finally {
            if (!isWaitBoxUpdate) {
                $RWT.ajax._destroyWaitBox();
            }
        }
    },
    _createWaitBox: function() {
        var box = $('#wps_sys_waitbox');
        if (box == null || box.length == 0) {
            if ($('body').attr('id')) {
                $('body').append('<div id="wps_sys_waitbox" style="position:absolute;width:100%;height:100%;background-color:black;opacity:0;left:0px;top:0px;z-index:99999997"/>');
                box = $('#wps_sys_waitbox');
            }
        }
        var textbox = $('#wps_sys_waitbox_text');
        if (textbox == null || textbox.length == 0) {
            $('body').append('<table id="wps_sys_waitbox_text" class="ui-corner-all rwt-wait-box-gradient rwt-ui-shadow" style="display:none;color:white;position:absolute;z-index:99999999;">' +
                    '<tr>' +
                    '<td>' +
                    '<img style="padding:10px;"src="images/ajax-loader.gif">' +
                    '</td>' +
                    '<td id="wps_sys_waitbox_text_cell" style="display:none">' +
                    '<table style="padding-right:20px">' +
                    '<tr>' +
                    '<td>' +
                    '<label id="wps_sys_waitbox_text_label" style="color:black; padding-right:10px;padding-left:10px;">Opening Selector...</label>' +
                    '</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td id="wps_sys_waitbox_text_progress_cell" style="border:solid 1px black;display:none; ">' +
                    '<div id="wps_sys_waitbox_text_progress" style="color:black; font-size:5px; border:solid 1px black; display:block;height: 20px float:bottom"></div>' +
                    '</td>' +                    
                    '</tr>' +                    
                    '</table>' +
                    '</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td id="wps_sys_waitbox_text_buttons_cell" colspan="2" style="display:none;">' +
                    '<div id="wps_sys_waitbox_text_buttons" style="height: 24px; text-align: center"></div>' +
                    '</td>' +                    
                    '</tr>' +                     
                    '</table>');
        }
        if (box.css('display') == 'none') {
            box.stop();
            box.css('opacity', 0);
            box.css('display', '');


            textbox = $('#wps_sys_waitbox_text');
            var s = WpsUtils.getBrowserWindowSize();
            var w = textbox.outerWidth();
            var h = textbox.outerHeight();
            textbox.css('left', (s.width / 2 - w / 2) + 'px').css('top', (s.height / 2 - h / 2) + 'px');

            setTimeout(function () {
                if (box.css('display') == 'none') {
                    return;
                }
                box.animate({
                    'opacity': 0.3
                }, {
                    duration: 600,
                    easing: 'linear',
                    step: function () {
                        if ($(this).css('display') == 'none') {
                            $(this).stop();
                        } else {
                            if (parseFloat($(this).css('opacity')) > 0.1) {
                                $('#wps_sys_waitbox_text').css('display', '');
                            }
                        }
                    },
                    complete: function () {

                    }
                });
            }, 1000);
        }        
    },
    _destroyWaitBox: function() {
        $('#wps_sys_waitbox').stop().css('opacity', 0)
                .css('display', 'none');
        $('#wps_sys_waitbox_text').css('display', 'none');
        $('#wps_sys_waitbox_text_label').text('');
        $('#wps_sys_waitbox_text_cell').css('display', 'none');
        $('#wps_sys_waitbox_buttons_cell').css('display', 'none');
    },
    _processResponseAsync: function(rs) {
        $RWT.ajax._processResponse(rs.responseXML, rs.rwt_id, rs.rwt_callbackFunc, rs);
    },
    _processBadStatus: function(rs){
        var xml = rs.responseXML;
        var xmlDoc = xml==null ? null : xml.documentElement;
        var token = xmlDoc==null ? null : xmlDoc.getAttribute('token');
        if (token!=null) {
            this._rqToken = token;
        }
    },
    sendRequest: function(rq_r, async) {
        var requestData = rq_r.data, callback = rq_r.cb;
        $RWT._ignoreFocusCheck = true;
        if (!$RWT._clientLocale) {
            $RWT._clientLocale = get_client_locale();
        }
        var rqId = Date.now();
        try {
            var rq = $RWT.makeHttpRequest();

            rq.rwt_rq = rq_r;
            var body = $('body');
            var rootId = null;
            if (body.length > 0) {
                rootId = body.attr('id');

                var rootNotFound = (rootId === '' || rootId == null);
                if (rootNotFound) {

                    $('body').attr('id', rootId);
                    document.body.id = rootId;
                    $('body').attr('name', rootId);//for ie (for getAttribute('id') method could return name)
                    document.body.name = rootId;//for ie
                }
            }
            var addr = request_url + '?root=' + (rootId != null ? rootId : '');
            if (this._rqToken!=null){
                addr += "&token=" + this._rqToken;
            }
            if (rootId == null) {
                addr += "&browser_locale=" + $RWT._clientLocale;
            }

            if (requestData != null) {
                addr += '&' + requestData;
            }

            addr += '&_rq_id_=' + rqId;

            rq.rwt_callbackFunc = callback;
            rq.rwt_id = rqId;
            rq.rwt_rq.processing = true;
            var persistentCookie = null;
            var asyncReq = async == null ? true : async;
            rq.onreadystatechange = function () {
                if (this.readyState == 1) {
                    $RWT.ajax._createWaitBox();
                } else if (this.readyState == 2) {

                } else if (this.readyState == 4) {
                    if (persistentCookie) {
                        for (var i = 0; i < persistentCookie.length; i++) {
                            if (persistentCookie[i].indexOf('paramsId') == -1) { 
                                var date = new Date();
                                date.setTime(date.getTime() + (30 * 24 * 60 * 60 * 1000));
                                var expires = "; expires=" + date.toGMTString();
                                document.cookie = persistentCookie[i] + expires + "; path=/";
                            }
                        }
                    }
                    try {
                        if (this.status == 200 || this.status == 'complete') {
                            $RWT.__console_log('AJAX  response received');
                            $RWT.ajax._processResponseAsync(this);
                        } else {//401 Unauthorized or server is not respnding
                            $RWT.ajax._processBadStatus(this);
                            resetRqs();
                            $RWT.ajax._destroyWaitBox();
                        }
                    } finally {
                    }
                }

            };

            rq.open('GET', addr, asyncReq);
            rq.setRequestHeader('Content-Type', 'text/xml');
            if (rq.overrideMimeType)
                rq.overrideMimeType('text/xml');
            $RWT.__console_log('AJAX request sent');
            persistentCookie = $RWT.cookies.filter(['JSESSIONID', 'll', 'ln', 'ls', 'paramsId']);
            rq.send(null);
        } catch (e) {
            $RWT.ajax._alertError(e);
        }
        return rqId;        
    }
};

function rdx_ajax_impl(rq_r, async) {
    return $RWT.ajax.sendRequest(rq_r, async);
}

$RWT.cookies = {
    filter: function (exceptions) {
        var cur = document.cookie;
        var all = cur.split(';');
        for (var i = 0; i < all.length; i++) {
            var found = false;
            for (var j = 0; j < exceptions.length; j++) {
                var match = exceptions[j] + "=";

                var test = all[i];

                if (test[0] === ' ')
                    test = test.substring(1);

                if (test.length >= match.length && test.substring(0, match.length) === match) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                var nameEnd = test.indexOf('=');
                if (nameEnd > 0) {
                    $RWT.cookies.eraseCookie(test.substring(0, nameEnd));
                }
            }
        }

        return all;
    },
    createCookie: function (name, value, days) {
        var expires = '';
        if (days) {
            var date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toGMTString();
        }
        document.cookie = name + "=" + $RWT.utils.base64.encodeWithUTF(value) + expires + "; path=/";
    },
    readCookie: function (name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ')
                c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) == 0) {
                var val = c.substring(nameEQ.length, c.length);
                if (val) {
                    try {
                        return $RWT.utils.base64.decodeWithUTF(val);
                    } catch (e) {
                        return null;
                    }
                } else
                    return null;
            }
        }
        return null;
    },
    eraseCookie: function (name) {
        $RWT.cookies.createCookie(name, "", -1);
    }
}

$RWT._killSession = function (ev) {
    var e = ev || window.event;
    // For IE and Firefox
    if (e) {
        e.returnValue = 'Leaving the page';
    }
    resetRqs();
    try {
        $RWT.ajax.sendRequest({data: 'dispose'}, false);
    } catch (e) {
        //ignore
    }
    return 'Leaving the page'
}
$RWT._initSession = function () {
    function get_browser() {
        var N = navigator.appName, ua = navigator.userAgent, tem;
        var M = ua.match(/(opera|chrome|safari|firefox|msie)\/?\s*(\.?\d+(\.\d+)*)/i);
        if (M && (tem = ua.match(/version\/([\.\d]+)/i)) != null)
            M[2] = tem[1];
        M = M ? [M[1], M[2]] : [N, navigator.appVersion, '-?'];
        return M[0];
    }
    function get_browser_version() {
        var N = navigator.appName, ua = navigator.userAgent, tem;
        var M = ua.match(/(opera|chrome|safari|firefox|msie)\/?\s*(\.?\d+(\.\d+)*)/i);
        if (M && (tem = ua.match(/version\/([\.\d]+)/i)) != null)
            M[2] = tem[1];
        M = M ? [M[1], M[2]] : [N, navigator.appVersion, '-?'];
        return M[1];
    }

    var browser = get_browser().toLowerCase();
    var browser_version = get_browser_version().toLowerCase();

    if (browser === "msie") {
        var numbers = browser_version.split('.');
        var showError = false;
        if (numbers.length < 1)
            showError = true;
        else {
            try {
                var v = parseInt(numbers[0]);
                if (v < 9) {
                    showError = true;
                }
            } catch (e) {
                showError = true;
            }
        }
        if (showError) {
            var message;
            if (get_client_locale() === 'ru') {
                message = "Для работы приложения необходим Internet Explorer версии 9.0 или выше";
            } else {
                message = "Application requires Internet Explorer version 9.0 or higher";
            }
            $('body').addClass('failure-content')
            $('body').html('<div class ="header">' +
                    (get_client_locale() === 'ru' ? 'Ошибка при инициализации приложения' : 'Application initialization failure')
                    + '</div>' +
                    '<div style="width:100%; height:100%; overflow:auto">' +
                    '    <table class="failure-details">' +
                    '        <tbody>' +
                    '            <tr><td>' +
                    message +
                    '             </td></tr>' +
                    '        </tbody>' +
                    '    </table>' +
                    '</div>');
            return;
        }
    }
    $('body').removeClass('failure-content');
    resetRqs();
    var userName = $RWT.cookies.readCookie('ln');
    var stationName = $RWT.cookies.readCookie('ls');
    var lang = $RWT.cookies.readCookie('ll');
    var initNodes = $RWT.cookies.readCookie('initNodes');
    var ainfo = 'ln=';
    if (userName)
        ainfo += userName;
    ainfo += ',ls=';
    if (stationName)
        ainfo += stationName;
    ainfo += ',ll=';
    if (lang)
        ainfo += lang;
    ainfo += ',initNodes=';
    if (initNodes)
        ainfo += initNodes;

    ainfo += ',browser='
    if (browser)
        ainfo += browser;
    ainfo += ',browser_version='
    if (browser_version)
        ainfo += browser_version;

    $RWT.actions.event_id('root', 'init', ainfo);

};
window.onunload = $RWT._killSession;

$RWT.actions = {
    event: function (element, actionName, params, callback) {
        $RWT.events._event(element, '_ae', '_a=' + actionName, params, callback);
    },
    event_id: function (elementid, actionName, params, callback) {
        $RWT.events._event_id(elementid, '_ae', '_a=' + actionName, params, callback);
    },
    client_event: function (element, actionName, params, callback) {
        $RWT.events._client_event(element, '_ae', '_a=' + actionName, params, callback);
    }
}
$RWT.events = {
    deffered_changes: {},
    _event: function (element, eventName, details, params, callback) {
        //        $RWT.events._client_event(element,eventName,details,params);        
        //        var q='';
        //        for(var key in $RWT.events.deffered_changes){
        //            var elementCache = $RWT.events.deffered_changes[key];                        
        //            for(var ev in elementCache){
        //                if(q!=''){
        //                    q += '&';
        //                }
        //                q += '_w_e='+ev+'^_wid=' + key;
        //                var event=elementCache[ev];
        //                for(var d in event){
        //                    if(d!=0){//anonimous event;
        //                        q+='^'+d;
        //                    }
        //                    var ap=event[d] ;
        //                    if(ap){
        //                        q += '^_p='+$RWT.utils.base64.encode(ap);    
        //                    }
        //                }                
        //            }
        //        }
        //        $RWT.events.deffered_changes=null;
        //        rdx_ajax(q);
        var priority;
        if (element == 'wait-box' || element == 'timer' || element == 'textsigner') {
            priority = 'hight';
        }
        var elementId = typeof element === 'string' || element instanceof String ? element : element.id;
        $RWT.events._event_id(elementId, eventName, details, params, callback, priority);
    },
    _event_id: function (elementid, eventName, details, params, callback, priority) {
        $RWT.events._client_event_id(elementid, eventName, details, params);
        var q = '';
        for (var key in $RWT.events.deffered_changes) {
            var elementCache = $RWT.events.deffered_changes[key];
            for (var ev in elementCache) {
                if (q != '') {
                    q += '&';
                }
                q += '_w_e=' + ev + '^_wid=' + key;
                var event = elementCache[ev];
                for (var d in event) {
                    if (d != 0) {//anonimous event;
                        q += '^' + d;
                    }
                    var ap = event[d];
                    if (ap) {
                        q += '^_p=' + $RWT.utils.base64.encodeWithUTF(ap);
                    }
                }
            }
        }
        $RWT.events.deffered_changes = null;
        rdx_ajax(encodeURIComponent(q), callback, priority, eventName === "timer_tick");
    },
    //client event function;
    //puts element event into event cache
    //will send to server with
    //first interactable event follows
    _client_event: function (element, eventName, details, params) {
        //        if($RWT.events.deffered_changes){
        //            var elementCache = $RWT.events.deffered_changes[element.id];
        //        }else{
        //            $RWT.events.deffered_changes = {}
        //        }
        //        if(!elementCache){
        //            elementCache = {};
        //            $RWT.events.deffered_changes[element.id] = elementCache;
        //        }
        //        var detailSet=elementCache[eventName];
        //        if(!detailSet){
        //            detailSet = {};
        //            elementCache[eventName]=detailSet;
        //        }
        //        var detailInfo = details?details:0;
        //        detailSet[detailInfo]=params;        
        var elementId = typeof element === 'string' || element instanceof String ? element : element.id;
        $RWT.events._client_event_id(elementId, eventName, details, params);
    },
    _client_event_id: function (elementid, eventName, details, params) {
        if ($RWT.events.deffered_changes) {
            var elementCache = $RWT.events.deffered_changes[elementid];
        } else {
            $RWT.events.deffered_changes = {}
        }
        if (!elementCache) {
            elementCache = {};
            $RWT.events.deffered_changes[elementid] = elementCache;
        }
        var detailSet = elementCache[eventName];
        if (!detailSet) {
            detailSet = {};
            elementCache[eventName] = detailSet;
        }
        var detailInfo = details ? details : 0;
        detailSet[detailInfo] = params;
    },
    uiTick: function () {
        $RWT.events._event_id('root', "timer_tick", null, null, function () {
            $RWT.__console_log('UI tick sent');
            $RWT.timer.enableUITicks();
        });
    },
    focused: function (element) {
        $RWT.events._event(element, "_wfc");
    },
    defocused: function (element) {
        $RWT.events._event(element, "_wfl");
    },
    focusedEvent: function () {
        $RWT.events.focused(this);
    },
    defocusedEvent: function () {
        $RWT.events.defocused(this);
    },
    cancelEvent: function (event) {
        if (event.preventDefault)
            event.preventDefault();
        if (event.stopPropagation)
            event.stopPropagation();
        event.returnValue = false;
    },
    getButtonsMask: function (event) {
        var SHIFT_MASK = 1;
        var CTRL_MASK = 1 << 1;
        var META_MASK = 1 << 2;
        var ALT_MASK = 1 << 3;
        var LEFT_MOUSE_BUTTON_MASK = 1 << 10;
        var MIDDLE_MOUSE_BUTTON_MASK = 1 << 11;
        var RIGHT_MOUSE_BUTTON_MASK = 1 << 12;
        var buttonsMask = 0;
        if (event.shiftKey == 1) {
            buttonsMask += SHIFT_MASK;
        }
        if (event.ctrlKey == 1) {
            buttonsMask += CTRL_MASK;
        }
        if (event.metaKey == 1) {
            buttonsMask += META_MASK;
        }
        if (event.altKey == 1) {
            buttonsMask += ALT_MASK;
        }
        if (event.button == 0) {
            buttonsMask += LEFT_MOUSE_BUTTON_MASK;
        }
        if (event.button == 1) {
            buttonsMask += MIDDLE_MOUSE_BUTTON_MASK;
        }
        if (event.button == 2) {
            buttonsMask += RIGHT_MOUSE_BUTTON_MASK;
        }
        return buttonsMask;
    }
}

$RWT._focusCache = [];
$RWT._focusCurrent = -1;
$RWT.addFocusLayer = function () {
    $RWT._focusCache.push({});
    $RWT._focusCurrent++;
};

$RWT.currentFocusLayer = function () {
    if ($RWT._focusCurrent < 0)
        $RWT.addFocusLayer();
    return $RWT._focusCache[$RWT._focusCurrent];
};
$RWT.closeFocusLayer = function () {
    if ($RWT._focusCurrent > 0) {
        $RWT._focusCache.pop();
        $RWT._focusCurrent--;
    }
};

window.onfocus = function (e) {
    $RWT.registerFocuseEvent(e);
}


$RWT.registerFocuseEvent = function (e) {
    //  if($RWT._ignoreFocusCheck==true)
    //      return;
    if (e) {
        $RWT._registerFocuseEvent(e.target);
    }
}

$RWT._registerFocuseEvent = function(t) {
    if (t && t.id && t.id.indexOf("wf_") == 0) {
        var tabIndex = t.getAttribute("tabindex");
        if (tabIndex === null || isNaN(tabIndex) || Number(tabIndex) > -1) {
            $RWT.currentFocusLayer().target = t;
        }
    }   
}

$RWT.clearFocusTarget = function () {
    $RWT.currentFocusLayer().target = null;
}

$RWT.timer = {
    _onTimeout: function () {
        if ($RWT._rqq == null || $RWT._rqq.length == 0) {
            $RWT.actions.event('timer', 'timeout');
        }
    },
    processResponse: function (xml, callback) {

        var command = xml.getAttribute('command');
        if (command == 'start') {
            if ($RWT.timer.instance == null) {
                $RWT.timer.instance = setInterval($RWT.timer._onTimeout, 500);
            }
        } else if (command == 'stop') {
            if ($RWT.timer.instance != null) {
                clearInterval($RWT.timer.instance);
            }
            $RWT.timer.instance = null;
        }
        if (callback) {
            callback();
        }
        $RWT.actions.event('timer', 'updated');

    },
    _onUITick: function () {
        try {
            if ($RWT.timer.uiTicks!=null) {
                $RWT.__console_log('UI tick generated');

                if ($RWT.timer.uiTickFree === true) {
                    $RWT.__console_log('UI tick accepted');
                    if ($RWT._rqq == null || $RWT._rqq.length == 0) {
                        $RWT.timer.uiTickFree = false;
                        $RWT.__console_log('UI tick ready to submit');
                        $RWT.events.uiTick();
                        $RWT.__console_log('UI tick performed');
                    } else {
                        $RWT.__console_log('UI tick rejected: request queue is not empty');
                    }
                } else {
                    $RWT.__console_log('UI tick rejected: ticks disabled');
                }                
                $RWT.timer.currentTickId = setTimeout($RWT.timer._onUITick, 2000);
            }
        } catch (e) {
            $RWT.__console_log('Exception on tick processing: ' + e.toString());
        }
    },
    startUITicks: function () {
        if ($RWT.timer.uiTicks == null && $RWT.timer.currentTickId==null) {
            $RWT.timer.uiTicks = 1;
            $RWT.timer.currentTickId = setTimeout($RWT.timer._onUITick, 2000);
            $RWT.__console_log('UI timer started');
            $RWT.timer.enableUITicks();
        }
    },
    stopUITicks: function () {
        $RWT.timer.uiTicks = null;
        $RWT.timer.uiTickFree = false;
        if ($RWT.timer.currentTickId!=null){
            clearTimeout($RWT.timer.currentTickId);
            $RWT.timer.currentTickId = null;
        }        
        $RWT.__console_log('UI timer stopped');
    },
    enableUITicks: function () {
        if ($RWT.timer.uiTicks != null){
            $RWT.timer.uiTickFree = true;
            $RWT.__console_log('UI ticking enabled');
        }
    }
}
$RWT.waitWindow = {
    show: function () {
        var window = $RWT.waitWindow._window;
        if (window == null || window.parentNode == null) {
            window = $RWT.waitWindow._createWindow();
            $('body').append(window.overlay);
            $('body').append(window);
            $RWT.waitWindow._window = window;
        }

        $RWT.waitWindow.updatePositition();

        $(window.overlay).css('display', '');
        $(window.overlay).css('opacity', '0');
        var testValue = new Date().getTime();
        window.startTime = testValue;
        window.isHidden = false;
        $(window).css('display', 'none').animate(
                {
                    'opacity': 1
                }, {
            duration: 200,
            easing: 'linear',
            step: function () {
                if (this.startTime != testValue || this.isHidden) {
                    $(this).stop();
                }
            },
            complete: function () {
                if (this.startTime == testValue && !this.isHidden) {
                    $(this).css('display', '');
                }
            }
        }
        );
    },
    updatePositition: function () {
        var window = $RWT.waitWindow._window;
        if (window != null) {
            var s = WpsUtils.getBrowserWindowSize();
            var w = $(window).outerWidth();
            var h = $(window).outerHeight();
            $(window).css('left', (s.width / 2 - w / 2) + 'px').css('top', (s.height / 2 - h / 2) + 'px');
        }
    },
    hide: function () {
        var window = $RWT.waitWindow._window;
        if (window != null) {
            var _setHidden = function (window) {
                window.isHidden = true;
                $(window).css('display', 'none');
                $(window.overlay).css('display', 'none');
                //$RWT.waitWindow._window = null;
                //                $(window).remove();
                //                $(window.overlay).remove();
            }
            //if($RWT.dialog!= null && $RWT.dialog.hasDialogs($('body'))){                
            _setHidden(this);
            return;
            //            }else{
            //                $(window).animate({
            //                    'opacity':0
            //                },{
            //                    duration:50,
            //                    complete:function(){
            //                        _setHidden(this);
            //                    }                
            //                });
            //            }
        }
    },
    update: function (text, title, value, maxValue) {
        var window = $RWT.waitWindow._window;
        if (window != null) {
            WpsUtils.text(window.titleLabel, title == null ? '' : title);
            WpsUtils.text(window.progressLabel, text == null ? '' : text);
            var animator;
            var isDeterminate = maxValue != null;
            if (window.animator == null || window.isDeterminate != isDeterminate) {
                if (!isDeterminate) {
                    animator = {
                        _update: function () {
                            var container = this.loop.parentNode;
                            var h = $(container).innerHeight();
                            var w = $(container).innerWidth();
                            $(this.loop).height(h).width(w / 10);
                            var sw = $(this.loop).outerWidth();

                            return w - sw;
                        },
                        toEnd: function () {
                            var pos = this._update();
                            $(this.loop).animate({
                                'left': pos + 'px'
                            }, {
                                step: function () {

                                    if (this.window.isDeterminate || this.window.parentNode == null || $(this.window).css('display') == 'none') {
                                        $(this).stop();
                                    }
                                },
                                duration: 3000,
                                easing: 'linear',
                                complete: function () {
                                    if (!this.window.isDeterminate) {
                                        this.animator.toStart();
                                    }
                                }
                            });
                        },
                        toStart: function () {
                            this._update();
                            $(this.loop).animate({
                                'left': 0 + 'px'
                            }, {
                                step: function () {
                                    if (this.window.isDeterminate || this.window.parentNode == null || $(this.window).css('display') == 'none') {
                                        $(this).stop();
                                    }
                                },
                                duration: 3000,
                                easing: 'linear',
                                complete: function () {
                                    if (!this.window.isDeterminate) {
                                        this.animator.toEnd();
                                    }
                                }
                            });
                        },
                        update: function () {
                            //                                if($RWT.dialog.progressHandle._lastUndState){
                            //                                    var container = this.loop.parentNode;                                    
                            //                                    var w = $(container).innerWidth();   
                            //                                    var pos = w * $RWT.dialog.progressHandle._lastUndState;
                            //                                    if(pos > 0 && pos < w){
                            //                                        $(this.loop).css('left',pos+'px');
                            //                                    }
                            //                                }
                            //if($RWT.dialog.progressHandle._lastUndDir==0){                                    
                            this.toEnd();
                            //                                }else{
                            //                                    this.toStart();
                            //                                }

                        }
                    }
                } else {
                    animator = {
                        update: function (cur, total) {
                            var container = this.loop.parentNode;
                            var h = $(container).innerHeight();
                            var w = $(container).innerWidth();
                            if (total) {
                                var t = parseFloat(total);
                                var c = cur ? parseFloat(cur) : 0;
                                var sw;
                                if (c > t || t == 0) {
                                    sw = w;
                                } else {
                                    sw = w * (c / t)
                                }
                                $(this.loop).css('left', '0').width(sw).height(h);
                            }
                        }
                    }
                }
                window.isDeterminate = isDeterminate;
                animator.loop = window.marker;
                animator.loop.animator = animator;
                window.animator = animator;
                animator.loop.handle = window.indicator;
                if (!window.isDeterminate) {
                    animator.update();
                }
            }
            if (window.animator && window.isDeterminate) {
                window.animator.update(value, maxValue);
            }
        }
    },
    _createWindow: function () {
        var overlay = WpsUtils.createDocumentElement('div', {
            'style': 'width:100%;height:100%;top:0;left:0;display:none;position:absolute;background-color:black;opacity:0.2;z-index:99999997',
            'id': 'wait-box-overlay',
            'tabIndex': 1
        });
        var window = WpsUtils.createDocumentElement('div', {
            'id': 'wait-box',
            'style': 'width:300px;height:75px;display:none;background-color:black;position:absolute;z-index:99999999;border:solid 1px black;'
        });
        overlay.window = window;
        window.header = WpsUtils.createDocumentElement('div', {
            'style': 'height:20px;width:100%;backgound-color:black;overflow:hidden'
        });
        $(window.header).addClass('rwt-dialog-header')
        window.titleLabel = WpsUtils.createDocumentElement('label', {
            'style': 'color:black;padding-top:3px; display:block'
        }, 'Please wait...');

        window.progressLabel = WpsUtils.createDocumentElement('label', {
            'style': 'color:black; top: 5px; left: 5px; position:absolute; display:block; font-family:arial; font-size:10px;'
        }, 'Please wait...');



        window.body = WpsUtils.createDocumentElement('div', {
            'style': 'width:297px;height:52px;top: 21px; left:2px;position:absolute;display:block;'
        });
        $(window.body).addClass('rwt-dialog-body');
        $(window).addClass('rwt-dialog');
        $(window).addClass('ui-corner-all');
        $(window).addClass('rwt-ui-shadow');
        $(window.header).addClass('ui-corner-top');
        window.appendChild(window.body);
        //$(window).addClass('ui-corner-all');
        //overlay.appendChild(window);                
        window.overlay = overlay;
        window.appendChild(window.header);
        var center = WpsUtils.createDocumentElement('center');
        window.header.appendChild(center);
        center.appendChild(window.titleLabel);

        window.indicator = WpsUtils.createDocumentElement('div', {
            'style': 'top:25px;left:8px;width:280px;height:17px;border:solid 1px;white; display:block; position: absolute;border:solid 1px black;'
        });
        window.body.appendChild(window.progressLabel);
        window.body.appendChild(window.indicator);
        window.marker = WpsUtils.createDocumentElement('div', {
            'style': 'left:0px;width:16x;height:20px;background-color:red; display:block;position:absolute'
        });
        window.indicator.appendChild(window.marker);
        $(window.indicator).addClass('rwt-progress-bar-background');

        $(window.marker).addClass('rwt-progress-bar');
        window.marker.window = window;
        return window;
    },
    _setTextLabel: function(text){
        var box = $('#wps_sys_waitbox_text_label');
        $(box).text(text);
        $('#wps_sys_waitbox_text_cell').css('display', '');        
    },
    _updateText: function(xml){
        var text = xml.getAttribute('text');
        if (text != null && text != '') {
            $RWT.waitWindow._setTextLabel(text);
        } else {
            $('#wps_sys_waitbox_text_cell').css('display', '');
            $('#wps_sys_waitbox_text_label').text('');
        }
    },
    _updateProgressBar: function(xml){
        var max_val = xml.getAttribute('max-value');
        if (max_val != null) {
            max_val = parseFloat(xml.getAttribute('max-value'));

            $("#wps_sys_waitbox_text_progress_cell").css('display', '');
            var val = xml.getAttribute('value');
            if (val != null)
                val = parseFloat(val);
            else
                val = 0;

            var w = (val / max_val) * 100;
            if (w > 100)
                w = 100;
            $("#wps_sys_waitbox_text_progress").css('display', '').css('width', w + '%');

        } else {
            $("#wps_sys_waitbox_text_progress_cell").css('display', 'none');
        }        
    },
    _updateButtons: function(xml){
        if (xml.getAttribute('buttonsPresent')=='true'){
            $("#wps_sys_waitbox_text_buttons_cell").css('display', '');
            var buttons = WpsUtils.findChildByLocalName(xml, 'buttons');
            if (buttons){
                var update = WpsUtils.findChildByLocalName(buttons,'update-element');
                if (update){
                    var buttonBoxXml = WpsUtils.findChildByLocalName(update,'div');                        
                    if (buttonBoxXml){
                        var buttonBox = $RWT.waitWindow._getButtons();
                        var oldButtons = [];
                        WpsUtils.iterateNames(buttonBox, 'button', function (button) {
                            oldButtons.push(button);
                        });
                        var i=0;
                        for (;i<oldButtons.length; i++){
                            buttonBox.removeChild(oldButtons[i]);
                        }                        
                        i = 0;
                        WpsUtils.iterateNames(buttonBoxXml, 'button', function(buttonXml){
                            var button = $RWT._importNode(buttonXml, true, null);
                            if (button){
                                buttonBox.appendChild(button);
                                if (i>0){
                                    $(button).css('margin-left','10px');
                                }
                                i++;
                            }
                        });
                    }
                }
            }
        }else{
            $("#wps_sys_waitbox_text_buttons_cell").css('display', 'none');
        }        
    },
    _updateSize: function(){
        box = $('#wps_sys_waitbox_text');
        var s = WpsUtils.getBrowserWindowSize();
        var w = $(box).outerWidth();
        var h = $(box).outerHeight();
        $(box).css('left', (s.width / 2 - w / 2) + 'px').css('top', (s.height / 2 - h / 2) + 'px');
    },
    _update: function(xml){
        $RWT.waitWindow._updateText(xml);
        $RWT.waitWindow._updateProgressBar(xml);
        $RWT.waitWindow._updateButtons(xml);
        $RWT.waitWindow._updateSize();
        var forcedShow = xml.getAttribute('forced');
        if (forcedShow != null && forcedShow === 'true') {
            box.css('display', '');                
        }        
    },
    _getButtons: function(){
        return document._findNodeById("wps_sys_waitbox_text_buttons");
    },
    _clearPressedButton: function(){        
        $RWT.waitWindow._getButtons().pressedButton = null;
    },
    _getPressedButton: function(){
        var buttonId = $RWT.waitWindow._getButtons().pressedButton;
        return buttonId!=null && document._findNodeById(buttonId)!=null ? buttonId : null;
    },
    processResponse: function (xml) {
        var command = xml.getAttribute('command');
        if (command == 'ping') {
            var buttonId = $RWT.waitWindow._getPressedButton();
            if (buttonId==null){
                $RWT.actions.event('wait-box', 'updated');
            }else{
                $RWT.actions.event('wait-box', 'button', buttonId);
            }            
        } else if (command == 'show') {            
            $RWT.waitWindow._update(xml);
            $RWT.waitWindow._clearPressedButton();
            $RWT.actions.event('wait-box', 'updated');
        } else if (command == 'hide') {
            $RWT.waitWindow._clearPressedButton();
            $RWT.actions.event('wait-box', 'updated');
        } else if (command == 'update') {
            var buttonId = $RWT.waitWindow._getPressedButton();
            if (buttonId==null){
                $RWT.waitWindow._update(xml);
                $RWT.actions.event('wait-box', 'updated');
            }else{
                $RWT.actions.event('wait-box', 'button', buttonId);
            }            
        }
    },
    onButtonClick: function(){
        $RWT.waitWindow._getButtons().pressedButton = this.id;
        var buttonBox = $RWT.waitWindow._getButtons();
        WpsUtils.iterateNames(buttonBox, 'button', function (button) {
            button.disabled = true;
            $(button).addClass('ui-state-disabled');            
        });
        var text = this.getAttribute('cancel-progress-text');
        if (text!=null && text!=''){
            $RWT.waitWindow._setTextLabel(text);            
        }
    }
}

$RWT.signer = {
    _findCertificateByHash: function (certThumbprint)
    {
        // CAPICOM constants
        var CAPICOM_STORE_ACTIVEX_OBJECT_NAME = "CAPICOM.Store";
        var CAPICOM_SIGNER_ACTIVEX_OBJECT_NAME = "CAPICOM.Signer";
        var CAPICOM_CURRENT_USER_STORE_NAME = "My";
        var CAPICOM_CURRENT_USER_STORE = 2;
        var CAPICOM_STORE_OPEN_READ_ONLY = 0;
        var CAPICOM_CERTIFICATE_FIND_SHA1_HASH = 0;
        var CAPICOM_CERTIFICATE_FIND_EXTENDED_PROPERTY = 6;
        var CAPICOM_CERTIFICATE_FIND_TIME_VALID = 9;
        var CAPICOM_CERTIFICATE_FIND_KEY_USAGE = 12;

        // instantiate the CAPICOM objects
        var MyStore = new ActiveXObject(CAPICOM_STORE_ACTIVEX_OBJECT_NAME);
        // open the current users personal certificate store
        MyStore.Open(CAPICOM_CURRENT_USER_STORE, CAPICOM_CURRENT_USER_STORE_NAME, CAPICOM_STORE_OPEN_READ_ONLY);

        // find all of the certificates that have the specified hash
        var FilteredCertificates = MyStore.Certificates.Find(CAPICOM_CERTIFICATE_FIND_SHA1_HASH, certThumbprint);
        if (FilteredCertificates.Count < 1) {
            // Clean Up
            MyStore = null;
            FilteredCertificates = null;
            return null;
        } else {
            var Signer = new ActiveXObject(CAPICOM_SIGNER_ACTIVEX_OBJECT_NAME);
            Signer.Certificate = FilteredCertificates.Item(1);
            // Clean Up
            MyStore = null;
            FilteredCertificates = null;
            return Signer;
        }
    },
    _sign_IE: function (text, certThumbprint)
    {
        // CAPICOM constants

        var CAPICOM_SIGNED_DATA_ACTIVEX_OBJECT_NAME = "CAPICOM.SignedData";
        var CAPICOM_ATTRIBUTE_ACTIVEX_OBJECT_NAME = "CAPICOM.Attribute";
        var CAPICOM_AUTHENTICATED_ATTRIBUTE_SIGNING_TIME = 0;
        var CAPICOM_ENCODE_BASE64 = 0;
        // instantiate the CAPICOM objects
        var SignedData = new ActiveXObject(CAPICOM_SIGNED_DATA_ACTIVEX_OBJECT_NAME);
        var TimeAttribute = new ActiveXObject(CAPICOM_ATTRIBUTE_ACTIVEX_OBJECT_NAME);


        // Set the data that we want to sign
        SignedData.Content = text;
        var Signer = $RWT.signer._findCertificateByHash(certThumbprint);
        if (Signer == null) {
            return "error:noMatchingCert";
        }

        // Set the time in which we are applying the signature
        var Today = new Date();
        TimeAttribute.Name = CAPICOM_AUTHENTICATED_ATTRIBUTE_SIGNING_TIME;
        TimeAttribute.Value = Today.getVarDate();
        Today = null;
        Signer.AuthenticatedAttributes.Add(TimeAttribute);

        // Do the Sign operation
        return SignedData.Sign(Signer, true, CAPICOM_ENCODE_BASE64);

    },
    _sign_NS: function (text, dn)
    {
        return window.crypto.signText(text, "auto", dn);
    },
    _signDigest: function (text, dn, thumbprint)
    {
        if (window.crypto && window.crypto.signText)
            return $RWT.signer._sign_NS(text, dn);
        else if (window.ActiveXObject != null)
            return $RWT.signer._sign_IE(text, thumbprint);
        else
            return "unsupported";
    },
    _sendError: function (error) {
        $RWT.actions.event('textSigner', 'error', error);
    },
    processResponse: function (xml) {
        var CAPICOM_E_CANCELLED = -2138568446;
        var TEXT_TO_SIGN_TAG_NAME = "Text";
        var CERTIFICATE_DN_TAG_NAME = "CertificateDN";
        var CERTIFICATE_THUMBPRINT_TAG_NAME = "CertificateThumbprint";
        var FF_NO_MATCHING_CERT_ERROR = "error:noMatchingCert";
        var FF_USER_CANCEL_ERROR = "error:userCancel";
        var FF_INTERNAL_ERROR = "error:internalError";
        var RWT_ELEMENT_NAME = "textsigner";
        var RWT_USER_CANCEL_ERROR = "canceled";
        var RWT_NO_MATCHING_CERT_ERROR = "noMatchingCert";
        var RWT_INTERNAL_ERROR = "internal";
        var RWT_UNSUPPORTED_OPERATION_ERROR = "unsupported";

        var text = xml.getElementsByTagName(TEXT_TO_SIGN_TAG_NAME)[0].childNodes[0].nodeValue;
        var dn = xml.getElementsByTagName(CERTIFICATE_DN_TAG_NAME)[0].childNodes[0].nodeValue;
        var thumbprint =
                xml.getElementsByTagName(CERTIFICATE_THUMBPRINT_TAG_NAME)[0].childNodes[0].nodeValue;
        var sign;
        try {
            sign = $RWT.signer._signDigest(text, dn, thumbprint);
        } catch (e) {
            if (e.number == CAPICOM_E_CANCELLED)
            {
                $RWT.signer._sendError(RWT_USER_CANCEL_ERROR);
            } else {
                $RWT.signer._sendError(e.description);
            }
            return;
        }
        switch (sign) {
            case FF_NO_MATCHING_CERT_ERROR:
                $RWT.signer._sendError(RWT_NO_MATCHING_CERT_ERROR);
                break;
            case FF_USER_CANCEL_ERROR:
                $RWT.signer._sendError(RWT_USER_CANCEL_ERROR);
                break;
            case FF_INTERNAL_ERROR:
                $RWT.signer._sendError(RWT_INTERNAL_ERROR);
                break;
            case RWT_UNSUPPORTED_OPERATION_ERROR:
                $RWT.signer._sendError(RWT_UNSUPPORTED_OPERATION_ERROR);
                break;
            default:
                $RWT.actions.event(RWT_ELEMENT_NAME, 'sign', sign);
        }
    }
}

$RWT.jsInvoker = {
    processResponse: function (xml) {
        var NODE_ID_TAG_NAME = "NodeId";
        var METHOD_NAME_TAG_NAME = "MethodName";
        var METHOD_PARAM_TAG_NAME = "MethodParam";

        var nodeId = xml.getElementsByTagName(NODE_ID_TAG_NAME)[0].childNodes[0].nodeValue;
        var methodName = xml.getElementsByTagName(METHOD_NAME_TAG_NAME)[0].childNodes[0].nodeValue;
        var methodParam = null;
        if (xml.getElementsByTagName(METHOD_PARAM_TAG_NAME) != null
                && xml.getElementsByTagName(METHOD_PARAM_TAG_NAME)[0] != null) {
            methodParam = xml.getElementsByTagName(METHOD_PARAM_TAG_NAME)[0].childNodes[0].nodeValue;
        }
        var node = document._findNodeById(nodeId);
        if (node == null) {
            $RWT.jsInvoker._sendError('instance with id \'' + nodeId + '\' was not found');
        } else if (node[methodName] == null) {
            $RWT.jsInvoker._sendError('method \'' + methodName + '\' was not found');
        }
        var result = null;
        try {
            if (methodParam == null) {
                result = node[methodName]();
            } else {
                result = node[methodName](methodParam);
            }
        } catch (e) {
            $RWT.jsInvoker._sendError(e.description);
        }
        return;
    },
    _sendError: function (error) {
        $RWT.actions.event('jsInvoker', 'error', error);
    },
}
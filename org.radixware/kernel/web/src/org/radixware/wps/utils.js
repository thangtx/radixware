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
WpsUtils = {
    '_scrollBarSize' : {'width' : null, 'height' : null}
}

WpsUtils.findChildByLocalName = function(element, childName) {
    for (var r = element.firstChild; r != null; r = r.nextSibling) {
        if (r.nodeType == 1 && WpsUtils.getNodeLocalName(r) == childName) {
            return r;
        }
    }
    return null;
}

WpsUtils.findChildByClassName = function(element, className) {
    for (var r = element.firstChild; r != null; r = r.nextSibling) {
        if (r.nodeType == 1 && r.className!=null && r.className.indexOf(className) >= 0) {
            return r;
        }
    }
    return null;
}

WpsUtils.findFirstChild = function(element) {
    for (var r = element.firstChild; r != null; r = r.nextSibling) {
        if (r.nodeType == 1) {
            return r;
        }
    }
    return null;
}

WpsUtils.findParentById = function(element, parentId) {
    for (var r = element.parentNode; r != null; r = r.parentNode) {
        if (r.nodeType == 1 && r.id && r.id == parentId) {
            return r;
        }
    }
    return null;
}

WpsUtils.findParentByClassName = function(element, className) {
    for (var r = element.parentNode; r != null; r = r.parentNode) {
        if (r.nodeType == 1 && r.className != null && r.className.indexOf(className) > 0) {
            return r;
        }
    }
    return null;
}

WpsUtils.isEnabled = function(element) {
    for (var r = element.parentNode; r != null; r = r.parentNode) {
        if (r.nodeType == 1 && r.getAttribute('disabled') == 'true') {
            return r.disabled;
        }
    }
    return true;
}

WpsUtils.findPrevSibling = function(element, name) {
    var prev = element.previousSibling;
    while (prev) {
        if (prev.nodeType == 1 && (name == null || WpsUtils.getNodeLocalName(prev) === name)) {
            return prev;
        }
        prev = prev.previousSibling;
    }
    return null;
}
WpsUtils.findNextSibling = function(element, name) {
    var next = element.nextSibling;
    while (next) {
        if (next.nodeType == 1 && (name == null || WpsUtils.getNodeLocalName(next) === name)) {
            return next;
        }
        next = next.nextSibling;
    }
    return null;
}

WpsUtils.iterateNames = function(element, childName, func) {
    for (var r = element.firstChild; r != null; r = r.nextSibling) {
        if (r.nodeType == 1 && WpsUtils.getNodeLocalName(r) == childName) {
            if (func(r) === true) {
                return true;
            }
        }
    }
    return false;
}
WpsUtils.getText = function(element) {
    for (var r = element.firstChild; r != null; r = r.nextSibling) {
        if (r.nodeType == 3) {
            return r.nodeValue;
        }
    }
    return null;
}
WpsUtils.iterateAll = function(element, func) {
    for (var r = element.firstChild; r != null; r = r.nextSibling) {
        if (r.nodeType == 1) {
            if (func(r) === true) {
                break;
            }
        }
    }
}

WpsUtils.text = function(element, tv) {
    var tn = null;
    for (var r = element.firstChild; r != null; r = r.nextSibling) {
        if (r.nodeType == document.TEXT_NODE) {
            tn = r;
            break;
        }
    }
    if (tv) {
        var ntn = document.createTextNode(tv);
        if (tn)
            element.replaceChild(ntn, tn);
        else
            element.appendChild(ntn);
        return tv;
    } else {
        if (tn) {
            return tn.nodeValue;
        } else
            return null
    }
}


WpsUtils.xmlText = function(element) {
    var serializer = new XMLSerializer();
    return serializer.serializeToString(element);
}

WpsUtils.appendNamedElement = function(parent, child, before, attributes) {
    var childElement = WpsUtils.createDocumentElement(child, attributes);
    if (before) {
        parent.insertBefore(childElement, before)
    } else {
        parent.appendChild(childElement);
    }
    return childElement;
}


WpsUtils.getNodeLocalName = function(node) {
    return (node.localName ? node.localName : (node.baseName ? node.baseName : node.tagName)).toLowerCase();
}

WpsUtils.createDocumentElement = function(type, attributes, value) {

    var element = null;
    // Try the IE way; this fails on standards-compliant browsers
    try {
        var elementText = '<' + type;
        if (attributes) {
            for (var a in attributes) {
                elementText += ' ' + a + '="' + attributes[a] + '"';
            }
        }

        if (value != null) {
            elementText += '>' + value + '</' + type + '>';
        } else
            elementText += '/>';
        element = document.createElement(elementText);
    } catch (e) {
    }
    if (!element || element.nodeName != type.toUpperCase()) {
        element = document.createElement(type);
        if (attributes) {
            for (var a in attributes) {
                element.setAttribute(a, attributes[a]);
            }
        }
        if (value != null) {
            $(element).text(value);
        }
    }
    return element;


}


WpsUtils.getBrowserWindowSize = function() {
    var winW, winH;
    if (document.body && document.body.offsetWidth) {
        winW = document.body.offsetWidth;
        winH = document.body.offsetHeight;
    }
    if (document.compatMode == 'CSS1Compat' &&
            document.documentElement &&
            document.documentElement.offsetWidth) {
        winW = document.documentElement.offsetWidth;
        winH = document.documentElement.offsetHeight;
    }
    if (window.innerWidth && window.innerHeight) {
        winW = window.innerWidth;
        winH = window.innerHeight;
    }
    return {
        'width': winW,
        'height': winH
    }
}


WpsUtils.size = function(o) {
    if (o == null)
        return 0;
    var c = 0;
    for (var e in o) {
        c++;
    }
    return c;
}
WpsUtils.isInTree = function(o) {
    while (o) {
        if (o.parentNode == null)
            return false;
        if (o.parentNode == document.documentElement) {
            return true;
        }
        o = o.parentNode;
    }
}

WpsUtils.isEmpty = function(map) {
    for (var key in map) {
        if (map.hasOwnProperty(key)) {
            return false;
        }
    }
    return true;
}

WpsUtils.getCoords = function(elem) {
    var box = elem.getBoundingClientRect();

    var body = document.body;
    var docElem = document.documentElement;

    var scrollTop = window.pageYOffset || docElem.scrollTop || body.scrollTop;
    var scrollLeft = window.pageXOffset || docElem.scrollLeft || body.scrollLeft;

    var clientTop = docElem.clientTop || body.clientTop || 0;
    var clientLeft = docElem.clientLeft || body.clientLeft || 0;

    var top = box.top + scrollTop - clientTop;
    var left = box.left + scrollLeft - clientLeft;

    return {top: Math.round(top), left: Math.round(left)};
}

WpsUtils.fixEvent = function(e) {
    e = e || window.event;
    if (!e.target)
        e.target = e.srcElement;
    if (e.pageX == null && e.clientX != null) { // если нет pageX..
        var html = document.documentElement;
        var body = document.body;

        e.pageX = e.clientX + (html.scrollLeft || body && body.scrollLeft || 0);
        e.pageX -= html.clientLeft || 0;

        e.pageY = e.clientY + (html.scrollTop || body && body.scrollTop || 0);
        e.pageY -= html.clientTop || 0;
    }

    if (!e.which && e.button) {
        e.which = e.button & 1 ? 1 : (e.button & 2 ? 3 : (e.button & 4 ? 2 : 0))
    }
    return e;
},
        $RWT.utils = {
    base64: {
        // private property
        _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
        // public method for encoding
        encode: function(input) {
            return $.base64.encode(input);
        },
        encodeWithUTF: function(input) {
            var output = "";
            var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            var i = 0;

            input = $RWT.utils.base64._utf8_encode(input);

            while (i < input.length) {

                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output +
                        $RWT.utils.base64._keyStr.charAt(enc1) + $RWT.utils.base64._keyStr.charAt(enc2) +
                        $RWT.utils.base64._keyStr.charAt(enc3) + $RWT.utils.base64._keyStr.charAt(enc4);

            }
            return output;
        },
        // public method for decoding
        decode: function(input) {
            return $.base64.decode(input);
        },
        decodeWithUTF: function(input) {
            if (input == null)
                return null;
            if (input == "")
                return "";
            var output = "";
            var chr1, chr2, chr3;
            var enc1, enc2, enc3, enc4;
            var i = 0;

            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            while (i < input.length) {

                enc1 = $RWT.utils.base64._keyStr.indexOf(input.charAt(i++));
                enc2 = $RWT.utils.base64._keyStr.indexOf(input.charAt(i++));
                enc3 = $RWT.utils.base64._keyStr.indexOf(input.charAt(i++));
                enc4 = $RWT.utils.base64._keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }

            }

            output = $RWT.utils.base64._utf8_decode(output);
            return output;
        },
        //,

        // private method for UTF-8 encoding
        _utf8_encode: function(string) {
            string = string.replace(/\r\n/g, "\n");
            var utftext = "";

            for (var n = 0; n < string.length; n++) {

                var c = string.charCodeAt(n);

                if (c < 128) {
                    utftext += String.fromCharCode(c);
                }
                else if ((c > 127) && (c < 2048)) {
                    utftext += String.fromCharCode((c >> 6) | 192);
                    utftext += String.fromCharCode((c & 63) | 128);
                }
                else {
                    utftext += String.fromCharCode((c >> 12) | 224);
                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                    utftext += String.fromCharCode((c & 63) | 128);
                }

            }

            return utftext;
        },
        //        // private method for UTF-8 decoding
        _utf8_decode: function(utftext) {
            var string = "";
            var i = 0;
            var c = 0, c1 = 0, c2 = 0;

            while (i < utftext.length) {

                c = utftext.charCodeAt(i);

                if (c < 128) {
                    string += String.fromCharCode(c);
                    i++;
                }
                else if ((c > 191) && (c < 224)) {
                    c2 = utftext.charCodeAt(i + 1);
                    string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                    i += 2;
                }
                else {
                    c2 = utftext.charCodeAt(i + 1);
                    var c3 = utftext.charCodeAt(i + 2);
                    string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                    i += 3;
                }

            }
            return string;
        }
    }
}

WpsUtils.getNextZIndex = function(node) {
    var parent = node.parentNode;
    var max = 0;
    while (parent) {
        try {
            if (parent.id != 'wait-box-overlay' && parent.id != 'wait-box') {
                var zIndex = parseInt($(parent).css('zIndex'), 10);
                if (zIndex && zIndex > max) {
                    max = zIndex;
                }
            }
        } catch (e) {
        }
        parent = parent.parentNode;
        if (parent == document)
            break;
    }
    return max + 3;    
}

WpsUtils.print = function(message){
    if (window.console!=null && console.log!=null){
        console.log(message);
    }
}

WpsUtils.printStackTrace = function(error){
    var stack = null;
    if (error==null){
        var e = new Error();
        if (e.stack!=null){
            stack = e.stack.split('\n').slice(2).join('\n');//delete first and second line
        }
    }else if (typeof error === 'string' || error instanceof String){
        var e = new Error();
        if (e.stack!=null){
            stack = error+'\n'+e.stack.split('\n').slice(2).join('\n');
        }else{
            stack = error;
        }
    }else{
        stack = error.stack;
        if (stack==null){
            stack = error.description;
        }
    }
    if (stack!=null){
        WpsUtils.print(stack);
    }
}

WpsUtils.getScrollbarWidth = function() {
    if (WpsUtils._scrollBarSize.width!=null){
        return WpsUtils._scrollBarSize.width;
    }
    var outer = document.createElement("div");
    outer.style.visibility = "hidden";
    outer.style.width = "100px";
    outer.style.msOverflowStyle = "scrollbar"; // needed for WinJS apps

    document.body.appendChild(outer);

    var widthNoScroll = outer.offsetWidth;
    // force scrollbars
    outer.style.overflow = "scroll";

    // add innerdiv
    var inner = document.createElement("div");
    inner.style.width = "100%";
    outer.appendChild(inner);        

    var widthWithScroll = inner.offsetWidth;

    // remove divs
    outer.parentNode.removeChild(outer);

    WpsUtils._scrollBarSize.width = widthNoScroll - widthWithScroll;
    return WpsUtils._scrollBarSize.width;
}

WpsUtils.getScrollbarHeight = function() {
    if (WpsUtils._scrollBarSize.height!=null){
        return WpsUtils._scrollBarSize.height;
    }
    var outer = document.createElement("div");
    outer.style.visibility = "hidden";
    outer.style.height = "100px";
    outer.style.msOverflowStyle = "scrollbar"; // needed for WinJS apps

    document.body.appendChild(outer);

    var heightNoScroll = outer.offsetWidth;
    // force scrollbars
    outer.style.overflow = "scroll";

    // add innerdiv
    var inner = document.createElement("div");
    inner.style.height = "100%";
    outer.appendChild(inner);        

    var heightWithScroll = inner.offsetWidth;

    // remove divs
    outer.parentNode.removeChild(outer);

    WpsUtils._scrollBarSize.height = heightNoScroll - heightWithScroll;
    return WpsUtils._scrollBarSize.height;
}

WpsUtils.getZoomFactor = function() {
    if ($RWT.BrowserDetect.browser ==  'Chrome' || $RWT.BrowserDetect.browser == 'Safari'){
        return window.outerWidth / window.innerWidth;
    }else if ($RWT.BrowserDetect.browser ==  'Explorer'){
        return screen.deviceXDPI / screen.logicalXDPI;
    }else {//Firefox
        return window.devicePixelRatio;
    }
}

$RWT.BrowserDetect = {
    init: function() {
        this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
        this.version = this.searchVersion(navigator.userAgent)
                || this.searchVersion(navigator.appVersion)
                || "an unknown version";
        this.OS = this.searchString(this.dataOS) || "an unknown OS";
    },
    searchString: function(data) {
        for (var i = 0; i < data.length; i++) {
            var dataString = data[i].string;
            var dataProp = data[i].prop;
            this.versionSearchString = data[i].versionSearch || data[i].identity;
            if (dataString) {
                if (dataString.indexOf(data[i].subString) != -1)
                    return data[i].identity;
            }
            else if (dataProp)
                return data[i].identity;
        }
    },
    searchVersion: function(dataString) {
        var index = dataString.indexOf(this.versionSearchString);
        if (index == -1)
            return;
        return parseFloat(dataString.substring(index + this.versionSearchString.length + 1));
    },
    dataBrowser: [
        {
            string: navigator.userAgent,
            subString: "Chrome",
            identity: "Chrome"
        },
        {
            string: navigator.userAgent,
            subString: "OmniWeb",
            versionSearch: "OmniWeb/",
            identity: "OmniWeb"
        },
        {
            string: navigator.vendor,
            subString: "Apple",
            identity: "Safari",
            versionSearch: "Version"
        },
        {
            prop: window.opera,
            identity: "Opera",
            versionSearch: "Version"
        },
        {
            string: navigator.vendor,
            subString: "iCab",
            identity: "iCab"
        },
        {
            string: navigator.vendor,
            subString: "KDE",
            identity: "Konqueror"
        },
        {
            string: navigator.userAgent,
            subString: "Firefox",
            identity: "Firefox"
        },
        {
            string: navigator.vendor,
            subString: "Camino",
            identity: "Camino"
        },
        {// for newer Netscapes (6+)
            string: navigator.userAgent,
            subString: "Netscape",
            identity: "Netscape"
        },
        {
            string: navigator.userAgent,
            subString: "MSIE",
            identity: "Explorer",
            versionSearch: "MSIE"
        },
        {
            string: navigator.userAgent,
            subString: "Gecko",
            identity: "Mozilla",
            versionSearch: "rv"
        },
        {// for older Netscapes (4-)
            string: navigator.userAgent,
            subString: "Mozilla",
            identity: "Netscape",
            versionSearch: "Mozilla"
        }
    ],
    dataOS: [
        {
            string: navigator.platform,
            subString: "Win",
            identity: "Windows"
        },
        {
            string: navigator.platform,
            subString: "Mac",
            identity: "Mac"
        },
        {
            string: navigator.userAgent,
            subString: "iPhone",
            identity: "iPhone/iPod"
        },
        {
            string: navigator.platform,
            subString: "Linux",
            identity: "Linux"
        }
    ]

};
$RWT.BrowserDetect.init();
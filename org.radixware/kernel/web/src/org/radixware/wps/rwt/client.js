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
$RWT.env = {
    oldVersion: {
        layout: function (label) {
            var size = WpsUtils.getBrowserWindowSize();
            var w = $(label).outerWidth();
            $(label).css('position', 'absolute').css('left', size.width - w - 1 + 'px').css('top', 0).css('color', 'red');
            if (!label.animated) {
                label.onclick = function () {
                    $RWT.actions.event(this, 'update-version');
                }
                label.animated = true;
                label.nextColor = 'black';
                label.animate_continue = function (l) {
                    var opacity = $(l).css('opacity');

                    if (opacity == '0') {
                        opacity = '1';
                    } else
                        opacity = '0';
                    var opts = {
                        'opacity': opacity
                    }
                    $(l).animate(opts, {
                        duration: 1000,
                        easing: 'linear',
                        step: function () {
                            if (this.parentNode == null) {
                                $(this).stop();
                            }
                        },
                        complete: function () {
                            if (this.parentNode == null) {
                                $(this).stop();
                            }
                            this.animate_continue(this);
                        }
                    });
                }
                label.animate_continue(label);

            }
        }
    }

}

$RWT.defaultMousedown = function (event) {
    if (event.preventDefault)
        event.preventDefault();
    return false;
}


$RWT._findId = function (el) {
    var id = el.getAttribute('id');
    while (!id) {
        el = el.parentNode;
        if (!el)
            break;
        id = el.getAttribute('id');
    }
    return id;
},
        $RWT._ae = function (el, ev, params) {
            $RWT._pae(el, ev, params);
        },
        $RWT._aeid = function (id, ev) {
            $RWT._paeid(id, ev, null);
        },
        $RWT._pae = function (el, ev, p) {
            var id = $RWT._findId(el);
            if (id == null) {
                alert("Action event without context");
                return;
            }
            $RWT._paeid(id, ev, p);
        },
        $RWT._paeid = function (id, ev, p) {
            var rd = '_w_e=_ae^_wid=' + id + '^_a=' + ev;
            if (p) {
                rd += '^_ap=' + p;
            }
            rdx_ajax(rd);
        },
        $RWT._re = function _(el, ev) {
            var rd = "_w_e=_re^_wid=" + el.id;
            rdx_ajax(rd);
        },
        $RWT.finishEdit = function () {
            var activeElement = null
            if (document.activeElement != null && document.activeElement.rwt_f_finishEdit != null) {
                activeElement = document.activeElement;
                document.activeElement.rwt_f_finishEdit(activeElement);
            }
            if ($RWT.currentFocusLayer().target != null && $RWT.currentFocusLayer().target != activeElement && $RWT.currentFocusLayer().target.rwt_f_finishEdit != null) {
                $RWT.currentFocusLayer().target.rwt_f_finishEdit($RWT.currentFocusLayer().target);
            }
        },
        $RWT.handleClick = function (e) {
            $RWT._ce(e);
            $RWT.finishEdit();
            $(this).focus();
            $RWT.actions.event(this, 'click');
        },
        $RWT.layoutDocument = function (rqId, noreply) {
            if (!$RWT._layoutNode) {
                var containerFunc = function (c, rr_rs, rqId) {
                    if (c) {
                        $RWT.container.updateAnchors(c, rr_rs, rqId);
                    }
                }
                // $RWT._layoutContainer = containerFunc;
                var recursiveFunc = function (n, rr_rs, rqId) {
                    try {
                        if ($RWT._layout_set) {
                            if (n.id && $RWT._layout_set[n.id] == n)
                                return;
                            $RWT._layout_set[n.id] = n;
                        }

                        var hidden = false;
                        if ($(n).css('display') == 'none')
                            hidden = true;
                        var skipChildren = false;
                        if (n.rwt_layout) {
                            if (hidden) {
                                if (n.rwt_layout.ignoreHiddenState == true) {
                                    n.rwt_layout(n, rr_rs, rqId);
                                } else
                                    return;
                            } else {
                                n.rwt_layout(n, rr_rs, rqId);
                            }
                            if (n.rwt_layout.ignoreChildren == true) {
                                skipChildren = true;
                            }
                        } else {//default container layout;
                            if (hidden)
                                return;
                            containerFunc(n, rr_rs, rqId);
                            skipChildren = true;
                        }
                        if (!skipChildren) {
                            try {
                                WpsUtils.iterateAll(n, function (c) {
                                    recursiveFunc(c, rr_rs, rqId);
                                });
                            } catch (e) {
                                alert(e);
                                WpsUtils.printStackTrace(e);
                            }
                        }
                        if (n.rwt_rnd_refine) {
                            var res = n.rwt_rnd_refine(n, rqId);
                            if (res && rr_rs) {
                                rr_rs[n.id] = res;
                            }
                        }
                    } catch (e) {
                        alert("1" + e);
                        WpsUtils.printStackTrace(e);
                    }
                }

                $RWT._layoutNode = recursiveFunc;
            }

            $RWT._layout_set = {};

            try {
                var rr_rs = {};
                //        if($RWT.dialog){
                //            var top = $RWT.dialog.top();
                //            if(top && top.parentNode){
                //                $RWT._layoutNode(top,rr_rs,rqId);    
                //                return noreply?null:rr_rs;
                //            }
                //        }    
                var body = $('body')[0];
                var dialogMode = false;
                var dialogCount = 0;
                if ($RWT.dialog) {
                    var list = $RWT.dialog.listDialogs(body);
                    for (var i = 0; i < list.length; i++) {
                        dialogCount++;
                        var dlg = list[i];
                        if (!$RWT.dialog.inited(dlg)) {
                            $RWT._layoutNode(dlg, rr_rs, rqId);
                            dialogMode = true;
                        }
                    }
                    if (!dialogMode) {
                        var top = $RWT.dialog.top();
                        if (top) {
                            $RWT._layoutNode(top, rr_rs, rqId);
                            dialogMode = true;
                        }
                    }
                }


                if (!dialogMode || dialogCount == 1) {
                    $RWT._layoutNode(body, rr_rs, rqId); //apply layouts for all web-content
                } else {
                    var mc = $RWT.menuContainer.container(body);
                    if (mc[0]) {
                        $RWT.menuContainer.layout(mc[0]);
                    }
                    var pps = $RWT.dropDown.listPopups(body);
                    for (i = 0; i < pps.length; i++) {
                        $RWT._layoutNode(pps[i], rr_rs, rqId);
                    }
                }
                return noreply ? null : rr_rs;
            } finally {
                delete $RWT._layout_set;
                var toolTip = $RWT.toolTip.toolTipValue;
                if ($RWT.toolTip.timerId != null) { //for processing esc button click
                    clearTimeout($RWT.toolTip.timerId);
                }
                $RWT.toolTip.timeStamp = null;
                var toolTipNode = document.getElementById($(toolTip).attr('id'));
                if (document != null && document.body != null && toolTip != null && toolTipNode != null) {
                    var toolTipParentId = $(toolTip).attr('relativeElemId');
                    var toolTipParent = document.getElementById(toolTipParentId);
                    if (toolTipParent == null) {
                        document.body.removeChild(toolTip);
                        $RWT.toolTip.toolTipValue = null;
                    }    
                } else {
                    $RWT.toolTip.toolTipValue = null;
                }
            }


            return noreply ? null : rr_rs;
        },
        $RWT._layout = function (object, rr_rs, rqId) {
            $(object).css('margin', '0px');
            $(object).css('padding', '0px');
            $(object).css('top', 0);
            $(object).css('left', 0);
            $(object).css('width', $(window).width() - 1 + 'px');
            $(object).css('height', $(window).height() - 1 + 'px');
            if (!window.rwt_root) {
                window.rwt_root = object;
                window.rwt_root.std_resize = window.onresize;

                window.onresize = function () {

                    if (window.rwt_root) {
                        if (window.rwt_root.std_resize) {
                            window.rwt_root.std_resize();
                        }
                        var rr_rs = $RWT.layoutDocument();
                        if (rr_rs) {
                            $RWT.notifyRendered(rr_rs);
                        }
                    }
                }
            }
            $RWT.container.updateAnchors(object, rr_rs, rqId);
        },
        $RWT._layout.ignoreChildren = true;
$RWT._ce = function (e) {
    if (e) {
        if (e) {
            if (e.cancelBubble != null) {
                e.cancelBubble = true;
            } else {
                e.stopPropagation();
            }
        }
    }
},
        $RWT._removeNonDigits = /[^-\d\.]/g,
        $RWT.notifyRendered = function (rr_rs, rqId) {
            var rd = "_w_e=_re";
            if (rr_rs) {
                var string = '';
                for (var desc in rr_rs) {
                    var val = rr_rs[desc];
                    if (val) {
                        string += '&' + desc + '=' + val;
                    }
                }
            }
            if (string && string != '') {
                rd += string;
                return rdx_ajax(rd);
            }
        }

$RWT.toolButton = {
    _mouseDown: function () {
        $(this).addClass('rwt-tool-button-pressed');
        if (this.old_onmouseDown) {
            this.old_onmouseDown();
        }
    },
    _mouseUp: function () {
        $(this).removeClass('rwt-tool-button-pressed');
        if (this.old_onmouseUp) {
            this.old_onmouseUp();
        }
    },
    _mouseOut: function () {
        $(this).removeClass('rwt-tool-button-pressed');
        if (this.old_onmouseOut) {
            this.old_onmouseOut();
        }
    },
    layout: function (button) {

        if (button.onmousedown != $RWT.toolButton._mouseDown) {
            button.old_onmouseDown = button.onmousedown;
            button.onmousedown = $RWT.toolButton._mouseDown;
        }
        if (button.onmouseup != $RWT.toolButton._mouseUp) {
            button.old_onmouseUp = button.onmouseup;
            button.onmouseup = $RWT.toolButton._mouseUp;
        }
        if (button.onmouseout != $RWT.toolButton._mouseOut) {
            button.old_onmouseOut = button.onmouseout;
            button.onmouseout = $RWT.toolButton._mouseOut;
        }
        
        var img = WpsUtils.findChildByLocalName(button, 'img');
        var label = $RWT.toolButton._findLabel(button);
        var btn = WpsUtils.findChildByClassName(button, 'rwt-tool-button');
        var tableContainer = WpsUtils.findChildByClassName(button, "tableContainer");
        
        if ($(button).hasClass('rwt-tool-button-special')) {
            if (tableContainer == null) {
                tableContainer = document.createElement("div");
                $(tableContainer).addClass("tableContainer");
                $(tableContainer).css("display", "table");
                $(img).css("display", "inline-block");
                $(label).css("display", "table-cell").css("vertical-align", "bottom");
                if (img != null) {
                    tableContainer.appendChild(img);
                }
                if (label != null) {
                    tableContainer.appendChild(label);
                }
                if (btn != null) {
                    tableContainer.appendChild(btn);
                }
                button.appendChild(tableContainer);
            }
        }
        var size = $RWT.toolButton.size(button);
       
        if ($(button).outerWidth() < size.w && !$(button).hasClass('rwt-tool-button-special')) {
            $(button).width(size.w);
        }
        if ($(button).outerHeight() < size.h) {
            $(button).height(size.h);
        }
        if (!button.onmouseover) {
            button.onmouseover = $RWT.toolButton.mouseIn;
        }
        if (!button.onmouseout) {
            button.onmouseout = $RWT.toolButton.mouseOut;
        }


        var diff;
        if (img) {

            var h = $(img).outerHeight();
            var bh = $(button).innerHeight();
            diff = (bh - h) / 2;
            $(img).css('margin-top', diff).css('margin-bottom', diff);
            if (label) {
                $(img).css('margin-left', 3);
            } else {
                $(img).css('margin-left', 5);
            }

        }
        if (label) {

            h = $(label).outerHeight();
            bh = $(button).innerHeight();
            diff = (bh - h) / 2;

            $(label).css('margin-top', diff).css('margin-bottom', diff);

            if (img) {
                $(label).css('margin-left', 2);
            } else
                $(label).css('margin-left', 5);
        }
    },
    _findLabel: function (button) {
        var l = WpsUtils.findChildByClassName(button, 'button-label')
        if (!l)
            l = WpsUtils.findChildByLocalName(button, 'label');
        return l;
    },
    size: function (button, img_, label_) {
        var img = img_ ? img_ : WpsUtils.findChildByLocalName(button, 'img');
        var label = label_ ? label_ : $RWT.toolButton._findLabel(button);
        var btn = WpsUtils.findChildByLocalName(button, 'button');
        var imgPopUp = WpsUtils.findChildByClassName(button, 'popup-icon');
        var size = 10;
        var hsize = 0;
        if (img) {
            size += $(img).outerWidth();
            var oh = $(img).outerHeight();
            if (oh > hsize)
                hsize = oh;
        }
        if (label) {
            size += $(label).outerWidth();
            oh = $(img).outerHeight();
            if (oh > hsize)
                hsize = oh;
        }
        if (btn) {
            size += $(btn).outerWidth(true);
            if(!label) {
                $(btn).css('bottom', $(button).height()+'px');
                size = 37; //icon + btn size
            }
        }
        if ($(button).hasClass('rwt-tool-button-special')) {
           size.w = 30; //tableContainer width
        }
        if (imgPopUp) {
            size += $(imgPopUp).outerWidth(true);
            if(!label) {
                $(imgPopUp).css('position', 'relative');
            }
        }
        return {
            w: size,
            h: hsize
        };
    }
}

//$RWT.buttonIcon={
//    layout:function(img){
//        if(img){
//            var w = $(img).css('width');
//            if(w){
//                var index = w.indexOf('px');
//                if(index>0){
//                    var val = parseInt(w.substring(0, index));
//                    val++;
//                    $(img).css('width',val+'px');
//                    val--;
//                    $(img).css('width',val+'px');
//                }
//            }
//            
//        }
//    }    
//}
//$RWT.buttonIcon.layout.ignoreChildren=true;
$RWT.toolButton.layout.ignoreChildren = true;
$RWT.toolBar = {
    getTable: function (toolBar) {
        var content = WpsUtils.findChildByLocalName(toolBar, 'table');
        if (!content) {
            content = WpsUtils.findChildByLocalName(toolBar, 'center');
            if (content) {
                content = WpsUtils.findChildByLocalName(content, 'table');
            }
        }
        return content;
    },
    minsize: function (toolBar) {
        var content = $RWT.toolBar.getTable(toolBar);
        if (content) {
            var body = WpsUtils.findChildByLocalName(content, 'tbody');
            if (body) {
                var rows = [];
                WpsUtils.iterateNames(body, 'tr', function (tr) {
                    rows.push(tr);
                });

                if (rows.length > 0) {
                    var w = 0, h = 0
                    if (rows.length == 1) {//horizontal                        
                        WpsUtils.iterateNames(rows[0], 'td', function (td) {
                            var button = WpsUtils.findFirstChild(td);
                            if (button) {
                                if ($(button).css('display') != 'none') {
                                    var size = $RWT.toolButton.size(button);
                                    w += size.w;
                                    if (size.h > h)
                                        h = size.h;
                                }
                            }
                        });
                    } else {//vertical


                        for (var i = 0; i < rows.length; i++) {
                            var td = WpsUtils.findFirstChild(rows[i]);
                            if (td) {
                                var button = WpsUtils.findFirstChild(td);
                                if (button) {
                                    if ($(button).css('display') != 'none') {
                                        var size = $RWT.toolButton.size(button);
                                        h += size.h;
                                        if (w < size.w)
                                            w = size.w;
                                    }

                                }
                            }
                        }
                    }
                    return {
                        w: w + 5,
                        h: h
                    };
                }
                return null;
            }
            return null;
        }
        return null;
    },
    layout: function (toolBar) {
        var content = $RWT.toolBar.getTable(toolBar);
        if (content) {

            var body = WpsUtils.findChildByLocalName(content, 'tbody');
            if (body) {
                var rows = [];
                WpsUtils.iterateNames(body, 'tr', function (tr) {
                    rows.push(tr);
                });
                var totalSize = 0;
                if (rows.length > 0) {
                    if (rows.length == 1) {//horizontal
                        WpsUtils.iterateNames(rows[0], 'td', function (td) {
                            var button = WpsUtils.findFirstChild(td);
                            if (button) {
                                if ($(button).css('display') != 'none') {
                                    $(td).css('display', '');
                                    var size = $RWT.toolButton.size(button);
                                    if ($(button).hasClass('rwt-tool-button-special')) {
                                        size.w = 30; 
                                    }
                                    $(button).width(size.w);
                                    $(td).width($(button).outerWidth() + 1);
                                    totalSize += $(button).outerWidth();
                                }
                                else {
                                    $(td).css('display', 'none');
                                }
                            }
                        });
                    } else {//vertical
                        var maxWidth = 0;
                        var buttons = [];
                        for (var i = 0; i < rows.length; i++) {
                            var td = WpsUtils.findFirstChild(rows[i]);
                            if (td) {
                                var button = WpsUtils.findFirstChild(td);
                                if (button) {
                                    if ($(button).css('display') != 'none') {
                                        var size = $RWT.toolButton.size(button);
                                        if (maxWidth < size.w)
                                            maxWidth = size.w;
                                        var spacing = 4;
                                        $(button).height(size.h + spacing);
                                        $(button.parentNode).height($(button).outerHeight() + spacing);
                                        buttons.push(button);
                                    } else {
                                        $(td).css('display', 'none');
                                    }
                                }
                            }
                        }
                        for (i = 0; i < buttons.length; i++) {
                            $(buttons[i]).width(maxWidth);
                            $(buttons[i].parentNode).width($(buttons[i]).outerWidth());
                        }
                        totalSize = maxWidth;
                        $(toolBar).height($(content).outerHeight());
                    }
                }
                //  $(toolBar).width($(content).outerWidth()+10);        
            }
        }
    }
}

$RWT.toolTip = {
    layout: function(toolTip) {
        if ($(toolTip).attr('relativeElemId') == null) {
            var parentObj = document.getElementById(toolTip.parentNode.id);
            $(toolTip).attr('relativeElemId', parentObj.id);
            parentObj.toolTip = toolTip;
            parentObj.addEventListener('mouseenter', mouseEnterFunc,false);
            parentObj.addEventListener('mouseleave', mouseLeaveFunc,false);
        }
          
        function mouseEnterFunc() {
            $RWT.toolTip.toolTipValue = this.toolTip;
            if ($RWT.toolTip.timeStamp == null || (new Date().getTime() - $RWT.toolTip.timeStamp > 5000)) {
                $RWT.toolTip.timerId = setTimeout(showToolTip, 1000);
            } else {
                $RWT.toolTip.timerId = setTimeout(showToolTip, 500);
            }
            
        }
        
        function mouseLeaveFunc() {
            if ($RWT.toolTip.toolTipValue != null) {
                $(this.toolTip).css('visibility', 'hidden');
                $(this.toolTip).css('display', 'none');
                this.appendChild(this.toolTip);
                $RWT.toolTip.toolTipValue = null;      
            }
            if ($RWT.toolTip.timerId) {
                clearTimeout($RWT.toolTip.timerId);
            }
        }
          
        function showToolTip() {
            try {
                var toolTip = $RWT.toolTip.toolTipValue;
                if (toolTip != null) {
                    $RWT.toolTip.timeStamp = new Date().getTime();
                    document.body.appendChild(toolTip);
                    $(toolTip).css('visibility', 'visible');
                    $(toolTip).css('display', 'inline');

                    var he = $('#' + $(toolTip).attr('relativeElemId'));
                    if (!he)
                        return;

                    var tf_pos = $(he).offset();
                    var size = WpsUtils.getBrowserWindowSize();
                    var maxheight = size.height - tf_pos.top - $(he).outerHeight() - 20;
                        if (maxheight < $($(toolTip).children()[0]).height()){
                            $(toolTip).offset({
                               top: tf_pos.top - $($(toolTip).children()[0]).height(),
                               left: tf_pos.left
                            });
                        } else {
                            $(toolTip).offset({
                                top: tf_pos.top + $(he).outerHeight(),
                                left: tf_pos.left
                            });
                        }
                    var minwidth = 0;
                    var zIndex = WpsUtils.getNextZIndex(document.getElementById($(toolTip).attr('relativeElemId')));
                    $(toolTip)
                            .css("zIndex", zIndex) 
                            .css("position", "absolute")
                            .css("min-width", minwidth + 'px')
                }
            } catch (e) {
                alert(e);
                WpsUtils.printStackTrace(e);
            }
          }
    },
    toolTipValue : null,
    timerId : null,
    timeStamp : null
};

$RWT.choosable = {
    mouseIn: function () {
        /*var t = $(this);
         if (!$(this).attr('savecolor')) {
         var cs = this.style.backgroundColor ? this.style.backgroundColor : $(this).css('background-color');
         $(this).attr('savecolor', cs);
         }
         $(this).css('background-color', '');
         if (t.attr('disabled') == true || t.hasClass('ui-state-disabled')) {
         return;
         } else {
         $(this).addClass('rwt-ui-choosable-pointed');
         }
         if ($(this).is('tr')) {
         WpsUtils.iterateNames(this, 'td', function (td) {
         var c = td.style.backgroundColor;//$(td).css('background-color');
         if (c && !$(td).hasClass("rwt-ui-choosable-pointed") && !$(td).hasClass('rwt-ui-selected-item')) {
         $(td).attr('savecolor', c);
         $(td).css('background-color', '');
         $(td).addClass('rwt-ui-choosable-pointed');
         }
         }
         );
         }*/
    },
    mouseOut: function () {
        /*   $(this).removeClass("rwt-ui-choosable-pointed");
         $(this).css('background-color', $(this).attr('savecolor'));
         
         if ($(this).is('tr')) {
         WpsUtils.iterateNames(this, 'td', function (td) {
         $(td).removeClass('rwt-ui-choosable-pointed');
         var c = $(td).attr('savecolor');
         if (c && !$(td).hasClass("rwt-ui-choosable-pointed") && !$(td).hasClass('rwt-ui-selected-item')) {
         $(td).css('background-color', c);
         }
         });
         }*/
    }
};

$RWT.listBox = {
    item: {
        keyDown: function (e) {
            switch (e.keyCode) {//escape
                case 38://up
                    var prev = this;
                    do {
                        prev = $RWT.listBox.item._prevItem(prev);
                    } while (prev && !$(prev).hasClass("rwt-ui-choosable-pointed"));
                    if (prev && prev.onclick) {
                        $(prev).focus();
                        prev.onclick();
                    }
                    break;
                case 40://dn
                    var next = this;
                    do {
                        next = $RWT.listBox.item._nextItem(next);
                    } while (next && !$(next).hasClass("rwt-ui-choosable-pointed"))
                    if (next && next.onclick) {
                        $(next).focus();
                        next.onclick();
                    }
                    break;
            }
        },
        _prevItem: function (item) {
            var li = item.parentNode;
            if (li) {
                var prev = WpsUtils.findPrevSibling(li, 'li');
                if (prev) {
                    return  WpsUtils.findFirstChild(prev);
                }
            }
            return null;
        },
        dblclick: function () {
            $RWT.actions.event(this, 'dblclick');
        },
        _nextItem: function (item) {
            var li = item.parentNode;
            if (li) {
                var prev = WpsUtils.findNextSibling(li, 'li');
                if (prev) {
                    return  WpsUtils.findFirstChild(prev);
                }
            }
            return null;
        }
    },
    layout: function (list) {
        list.waitingData = false;
        $RWT.listBox.afterLoadData(list);
    },
    afterLoadData: function (list) {
        var content = WpsUtils.findChildByLocalName(list, 'ul');
        if ($RWT.listBox._shouldReadMore(list) && !list.waitingData) {
            var myHeight = Math.round($(list).innerHeight() + list.scrollTop);
            if (myHeight >= content.offsetHeight) {
                var rr_rs = {};
                rr_rs[list.id] = 'm';
                list.waitingData = true;
                $RWT.notifyRendered(rr_rs);
            }
        }
    },
    syncScroll: function (e) {
        $RWT.listBox.afterLoadData(this);
    },
    _shouldReadMore: function (list) {
        return $(list).attr("more-rows") === 'true';
    }
}
$RWT.listBox.layout.ignoreChildren = true;
$RWT.container = {
    childById: function (c, id) {
        var res = null;
        WpsUtils.iterateAll(c, function (co) {
            if (co.id == id) {
                res = co;
                return true;
            }
        });
        return res;
    },
    anchor: function (c, obj, options, register) {
        if (c && obj) {
            if (options) {
                if (!c.anchors)
                    c.anchors = {};
                c.anchors[obj.id] = options;
                $(c).resize($RWT.container.resizeEvent);
            } else {
                if (c.anchors) {
                    delete c.anchors[obj.id];
                }
            }
        }
    },
    resizeEvent: function () {
        $RWT.container.updateAnchors(this);
    },
    updateAnchors: function (c, rr_rs, rqId) {

        if (!c.className || (typeof (c.className.indexOf) !== 'undefined' && c.className.indexOf('rwt-container') < 0)) {
            WpsUtils.iterateAll(c, function (d) {
                try {
                    $RWT._layoutNode(d, rr_rs, rqId);
                } catch (e) {
                    alert(e);
                    WpsUtils.printStackTrace(e);
                }
            });
            return;
        }

        if (!$RWT.container.applyHorizontalAnchor) {
            $RWT.container.applyHorizontalAnchor = function (container, ac, processed, all, flow) {
                if (flow[ac.target.id] != null)
                    return false;
                else
                    flow[ac.target.id] = ac;
                try {
                    var totalW = $(container).innerWidth();

                    var width = null;
                    var loc = $(ac.target).position();
                    var posx = null
                    if (ac.left) {
                        if (ac.left.ref) {
                            if (processed[ac.left.ref] == null && container.anchors[ac.left.ref] != null) {
                                var tac = all[ac.left.ref];
                                if (tac) {
                                    if (!$RWT.container.applyHorizontalAnchor(container, tac, processed, all, flow)) {
                                        return false;
                                    }
                                } else
                                    return false;
                            }
                            var refObj = null;
                            if (processed[ac.left.ref] != null)
                                refObj = processed[ac.left.ref];
                            else
                                refObj = $RWT.container.childById(container, ac.left.ref);
                            if (refObj != null) {
                                var refL = $(refObj).position();
                                var refW = $(refObj).outerWidth();
                                posx = refL.left + refW * ac.left.part + ac.left.offset;
                            }
                            else
                                return false;
                        } else {
                            posx = totalW * ac.left.part + ac.left.offset;
                        }
                    }
                    if (ac.right) {
                        if (ac.right.ref) {
                            if (processed[ac.right.ref] == null && container.anchors[ac.right.ref] != null) {
                                tac = all[ac.right.ref];
                                if (tac) {
                                    if (!$RWT.container.applyHorizontalAnchor(container, tac, processed, all, flow)) {
                                        return false;
                                    }
                                } else
                                    return false;
                            }
                            if (processed[ac.right.ref] != null)
                                refObj = processed[ac.right.ref];
                            else
                                refObj = $RWT.container.childById(container, ac.right.ref);
                            if (refObj != null) {
                                refL = $(refObj).position();
                                refW = $(refObj).outerWidth();
                                var dest = refL.left + refW * ac.right.part + ac.right.offset;
                                if (posx) {
                                    width = dest - posx;
                                } else {
                                    width = dest - loc.left;
                                }
                            } else
                                return false;
                        } else {
                            dest = totalW * ac.right.part + ac.right.offset - (container.scrolledY == true ? WpsUtils.getScrollbarWidth() : 0);
                            if (posx) {
                                width = dest - posx;
                            } else {
                                width = dest - loc.left;
                            }
                        }
                    }
                    if (posx) {
                        $(ac.target).css('left', posx);
                        $(ac.target).css('position', 'absolute');
                    }
                    if (width) {
                        $(ac.target).width(width);
                    }
                    return true;
                } finally {
                    flow[ac.target.id] = null;
                }
            }
        }

        if (!$RWT.container.applyVerticalAnchor) {
            $RWT.container.applyVerticalAnchor = function (container, ac, processed, all, flow) {
                if (flow[ac.target.id] != null)
                    return false;
                else
                    flow[ac.target.id] = ac;
                try {
                    var totalH = $(container).innerHeight();
                    var loc = $(ac.target).position();
                    var refObj = null;
                    var tac = null;
                    var dest = null;
                    var refL = null;
                    var posy = null;
                    var height = null;
                    if (ac.top) {
                        if (ac.top.ref) {
                            if (processed[ac.top.ref] == null && container.anchors[ac.top.ref] != null) {
                                tac = all[ac.top.ref];
                                if (tac) {
                                    if (!$RWT.container.applyVerticalAnchor(container, tac, processed, all, flow)) {
                                        return false;
                                    }
                                } else
                                    return false;
                            }
                            if (processed[ac.top.ref] != null)
                                refObj = processed[ac.top.ref];
                            else
                                refObj = $RWT.container.childById(container, ac.top.ref);
                            if (refObj != null) {
                                refL = $(refObj).position();
                                var refH = $(refObj).outerHeight();
                                posy = refL.top + refH * ac.top.part + ac.top.offset;
                            } else
                                return false;
                        } else
                            posy = totalH * ac.top.part + ac.top.offset;
                    }
                    if (ac.bottom) {
                        if (ac.bottom.ref) {
                            if (processed[ac.bottom.ref] == null && container.anchors[ac.bottom.ref] != null) {
                                tac = all[ac.bottom.ref];
                                if (tac) {
                                    if (!$RWT.container.applyVerticalAnchor(container, tac, processed, all, flow)) {
                                        return false;
                                    }
                                }
                                else
                                    return false;
                            }
                            if (processed[ac.bottom.ref] != null)
                                refObj = processed[ac.bottom.ref];
                            else
                                refObj = $RWT.container.childById(container, ac.bottom.ref);
                            if (refObj != null) {
                                refL = $(refObj).position();
                                refH = $(refObj).outerHeight();
                                dest = refL.top + refH * ac.bottom.part + ac.bottom.offset;
                                if (posy) {
                                    height = dest - posy;
                                } else {
                                    height = dest - loc.top;
                                }
                            } else
                                return false;
                        } else {
                            dest = totalH * ac.bottom.part + ac.bottom.offset - (container.scrolledX == true ? WpsUtils.getScrollbarHeight() : 0);
                            if (posy) {
                                height = dest - posy;
                            } else {
                                height = dest - loc.top;
                            }
                        }
                    }

                    if (posy) {
                        $(ac.target).css('top', posy);
                        $(ac.target).css('position', 'absolute');
                    }
                    if (height) {
                        $(ac.target).height(height);
                    }
                    return true;
                }
                finally {
                    flow[ac.target.id] = null;
                }
            }
        }
        var result = [];

        WpsUtils.iterateAll(c, function (d) {
            if (d.className && typeof (d.className.indexOf) !== 'undefined' && d.className.indexOf('rwt-ui-auto-height') >= 0 && d.rwt_f_adjustHeight != null) {
                d.rwt_f_adjustHeight(d);
            }
            result.push(d)
            var anc = $(d).attr("anchor");
            if (anc) {
                $(d).attr("anchor", null);
                var ancs = anc.split('|');
                var options = null;
                for (var i = 0; i < ancs.length; i++) {
                    var opts = ancs[i].split(':');
                    if (opts.length == 2) {
                        var aname = opts[0];
                        var values = opts[1].split(',');
                        if (values.length >= 2) {
                            if (!options)
                                options = {};
                            options[aname] = {
                                part: parseFloat(values[0]),
                                offset: parseInt(values[1])
                            };
                            if (values.length == 3) {
                                options[aname].ref = values[2];
                            }
                        }
                    }
                }
                if (options) {
                    options.target = d;
                    $RWT.container.anchor(c, d, options);
                } else {
                    $RWT.container.anchor(c, d, null);
                }
            }
        });
        if (c.anchors) {
            //  var of = $(c).css('overflow');
            $(c).css('overflow', 'none');
            $(c).css('overflow-x', 'none');
            $(c).css('overflow-y', 'none');
            try {
                var rerun = {};
                var all = {}

                for (var id in c.anchors) {
                    var options = c.anchors[id];
                    var obj = options.target;
                    if (!obj)
                        continue;
                    var ac = {};
                    ac.target = obj;

                    for (var a in options) {
                        if (a == 't') {
                            ac.top = options[a];
                            all[obj.id] = ac;
                        } else if (a == 'l') {
                            ac.left = options[a];
                            all[obj.id] = ac;
                        } else if (a == 'r') {
                            ac.right = options[a];
                            all[obj.id] = ac;
                        } else if (a == 'b') {
                            ac.bottom = options[a];
                            all[obj.id] = ac;
                        }
                    }

                }

                var processed = {};
                for (id in all) {
                    var flow = {};
                    ac = all[id];
                    if ($RWT.container.applyHorizontalAnchor(c, ac, processed, all, flow)) {
                        processed[ac.target.id] = ac.target;
                    }
                    else {
                        rerun[ac.target.id] = ac;
                    }
                }
                var modified = true;
                while (modified == true) {
                    modified = false;
                    for (id in rerun) {
                        ac = rerun[id];
                        flow = {};
                        if ($RWT.container.applyHorizontalAnchor(c, ac, processed, all, flow)) {
                            delete rerun[id];
                            modified = true;
                        }
                    }
                }


                processed = {};
                for (id in all) {
                    flow = {};
                    ac = all[id];
                    if ($RWT.container.applyVerticalAnchor(c, ac, processed, all, flow)) {
                        processed[ac.target.id] = ac.target;
                    } else {
                        rerun[ac.target.id] = ac;
                    }
                }
                modified = true;
                while (modified == true) {
                    modified = false;
                    for (id in rerun) {
                        ac = rerun[id];
                        flow = {};
                        if ($RWT.container.applyVerticalAnchor(c, ac, processed, all, flow)) {
                            delete rerun[id];
                            modified = true;
                        }
                    }
                }
                delete all;
            } finally {
                //                if(of !='hidden')
                //                    $(c).css('overflow',of)
            }
        }
        for (var i = 0; i < result.length; i++) {
            try {
                var child = result[i];
                $RWT._layoutNode(child, rr_rs, rqId);
            } catch (e) {
                alert(e);
                WpsUtils.printStackTrace(e);
            }
        }

    }
}
$RWT.dropDown = {
    keyDown: function (e) {
        switch (e.keyCode) {//escape
            case 27:
            {
                var dropDownElement = this.dropDown == null ? this : this.dropDown;
                if (dropDownElement.waitingData !== true) {
                    var closeDropDown = function () {
                        $RWT.dropDown.cancel(dropDownElement);
                    };
                    setTimeout(closeDropDown, 0);
                }
                break;
            }
//            default:
//                $RWT.listBox.keyDown.call(this,e);
        }
    },
    blur: function () {
        var dropDownElement = this.dropDown == null ? this : this.dropDown;
        if (dropDownElement.waitingData !== true) {
            var closeDropDown = function () {
                $RWT.dropDown.cancel(dropDownElement);
            };
            //if dropDown element has its own blur handler then it must be processed before closing
            if (!dropDownElement.onMenu) {
                setTimeout(closeDropDown, 0);
            }
        }
    },
    closeAll: function () {
        var widgets = $('.rwt-drop-down');
        var i;
        for (i = 0; i < widgets.length; i++) {
            if (widgets[i].isShown) {
                $RWT.dropDown._getActiveElement(widgets[i]).blur();
            }
        }
    },
    listPopups: function (body) {
        var res = [];
        WpsUtils.iterateNames(body, 'div', function (d) {
            if (d.className && d.className.indexOf('rwt-drop-down') >= 0) {
                res.push(d);
            }
        });
        return res;
    },
    cancel: function (obj) {
        var ib = $RWT.dropDown._getHandlerElem(obj);
        if (ib != null) {
            if (this._getRelativeElem(obj) === "menuBarButton") {
               $RWT.menu.beforeClose(obj);
            }
            $RWT.actions.event(ib, 'close-drop-down', '');
        }
        //$RWT.events.defocused(obj);
        if ($(obj).attr('id') == $RWT.dropDownButton.openedMenuId){
            $RWT.dropDownButton.openedMenuId = null;
        }
        if (obj.isShown) {
            obj.isShown = false;
        }
        $(obj).hide().remove();
    },
    _maxHeight: function (dropDown) {
        var pos = $(dropDown).offset();
        var size = $(document).height();
        return size - pos.top - 20;
    },
    _getActiveElement: function (dropDown) {
        if (dropDown.getAttribute('active_element') == null) {
            return dropDown;
        }
        else {
            var result = $($(dropDown).attr('active_element'), dropDown)[0];
            result.dropDown = dropDown;
            return result;
        }
    },
    _getHandlerElem: function (dropDown) {
        var ib = $('#' + $(dropDown).attr('handler_id'));
        return  ib && ib.length > 0 ? ib[0] : null;
    },
    _getRelativeElem: function (dropDown) {
        var re = $(dropDown).attr('relativeElem');
        return  re;
    },
    layout: function (dropDown, rr_rs, rqId, coordinates) {
        if (dropDown.isShown) {
            var dropDownElement = $RWT.dropDown._getActiveElement(dropDown);
            if (dropDownElement.waitingData === true) {
                dropDownElement.waitingData = false;
                dropDownElement.focus();
            } else {
                dropDown.isShown = false;
                $RWT.dropDown._getActiveElement(dropDown).blur();
            }
        } else {
            try {
                var he = $RWT.dropDown._getHandlerElem(dropDown);
                var displayMode = $RWT.dropDown._getRelativeElem(dropDown);
                var relativeElem, tf_pos, ib_pos;
                relativeElem = he == null ? null : $('#' + $(he).attr('relativeElemId'));
                if (!relativeElem && !coordinates) {
                    return;
                } else if (coordinates) {
                    var coordArray = coordinates.split(',');
                    var coordinatesOffset = {top: +coordArray[0], left: +coordArray[1]};
                    tf_pos = ib_pos = coordinatesOffset;
                } else {
                    tf_pos = $(relativeElem).offset();
                    ib_pos = $(he).offset();
                }
                
                var size = WpsUtils.getBrowserWindowSize();
                
                var maxheight = size.height - tf_pos.top - (coordinates ? 0 : $(relativeElem).outerHeight()) - 20;
                var maxwidth = size.width - tf_pos.left - (coordinates ? 0 : $(relativeElem).outerWidth()) - 20;
                
                if (maxheight < 60 && tf_pos.top > 60) {//try to place at top
                    maxheight = tf_pos.top - 20;
                    $(dropDown).css("bottom", size.height - tf_pos.top + 'px');
                    if (maxwidth < 60 && tf_pos.left > 60 && displayMode !== 'textFieldCell') {//try to place at left
                        maxwidth = tf_pos.left - 20;
                        $(dropDown).css("right", size.width - tf_pos.left - (coordinates == null ? $(relativeElem).outerWidth() : 0) + "px");
                    } else {
                        $(dropDown).css("left", ib_pos.left + 'px');
                    }
                } else if (maxwidth < 60 && tf_pos.left > 60 && displayMode !== 'textFieldCell') {
                    maxwidth = tf_pos.left - 20;
                    $(dropDown).css("right", size.width - tf_pos.left - (coordinates == null ? $(relativeElem).outerWidth() : 0) + "px")
                            .css("top", tf_pos.top + (coordinates == null ? $(relativeElem).outerHeight() : 0));
                } else {
                    if (coordinates) {
                        $(dropDown).offset({
                           top: tf_pos.top,
                           left: tf_pos.left
                        });
                    } else {
                        $(dropDown).offset({
                            top: tf_pos.top + $(relativeElem).outerHeight(),
                            left: tf_pos.left
                        });
                    }
                    maxwidth = size.width - ib_pos.left - 20;
                }
                
                var minwidth;
                if (displayMode === 'textFieldCell') {
                    minwidth = $(relativeElem).offset().left - ib_pos.left + $(relativeElem).outerWidth();
                } else {
                    if ($(dropDown).parent().attr('minwidth') == 'true') {
                        var leftBorderWidth = $(dropDown).css("border-left-width");
                        var rightBorderWidth = $(dropDown).css("border-right-width");
                        minwidth = $(he).outerWidth() - (leftBorderWidth ? parseFloat(leftBorderWidth) : 0) - (rightBorderWidth ? parseFloat(rightBorderWidth) : 0);
                    } else {
                        minwidth = 0;
                    }
                }

                $RWT.clearFocusTarget();

                var activeElement = $RWT.dropDown._getActiveElement(dropDown);
                activeElement.isBlur = true;
                $(activeElement).blur($RWT.dropDown.blur);
                $(dropDown)
                        .css("zIndex", WpsUtils.getNextZIndex(he))
                        .css("position", "absolute")
                        .css("max-height", maxheight + 'px')
                        .css("max-width", maxwidth + 'px')
                        .css("min-width", minwidth + 'px')
                        .attr("tabindex", 1)
                        .keydown($RWT.dropDown.keyDown)
                        .show("fast", function () {
                            if (this.clientHeight < this.scrollHeight) {
                                $(this).css("overflow-y", "scroll");
                            }
                            if (this.clientWidth < this.scrollWidth) {
                                $(this).css("overflow-x", "scroll");
                            }
                            $(activeElement).focus();
                            this.isShown = true;
                        });
            } catch (e) {
                alert(e);
                WpsUtils.printStackTrace(e);
            }
        }
    }
}
$RWT.dropDown.layout.ignoreHiddenState = true;
$RWT.menuBarButton = {
    onmousedown: function () {
            $RWT.actions.event(this, "openMenu");
    },
    onmouseenter: function () {
        $(this).addClass("rwt-ui-choosable-pointed-blue");
        $(this).siblings().removeClass("rwt-ui-choosable-pointed-blue");
        var menuBar = WpsUtils.findParentByClassName(this, "rwt-menu-bar");
        var relativeElementId = menuBar.getAttribute("relativeelemid");
        var relativeElement = document.getElementById(relativeElementId);
        var prevMenu = document.getElementById((relativeElement).getAttribute('menuId'));
        $(prevMenu).off('blur');
        prevMenu.isBlur = false;
        $RWT.subMenu.closeSubMenu(prevMenu);
        $(prevMenu.selectedElement).removeClass("rwt-ui-choosable-pointed-blue");
        if (relativeElementId !== this.id) {
            $(prevMenu).hide();
            $(prevMenu).attr('activeMenu', 'false');
            var curMenu = document.getElementById($(this).attr('menuId'));
            $(curMenu).attr('activeMenu', 'true');
            menuBar.setAttribute("relativeelemid", this.id);
            $(curMenu).show(); //focus should bind only on visible objects
            $RWT.dropDown.layout(curMenu, null, null);
            $(curMenu).focus();
        }
    },
    onmouseleave: function () {
        var curMenu = document.getElementById($(this).attr('menuId'));
        if (!curMenu.isBlur) {
            curMenu.isBlur = true;
            $(curMenu).blur($RWT.dropDown.blur);
        }
    }
};
$RWT.dropDownButton = {
    onmousedown : function () {
        $(this).on('click', function() {
           return false;
        });
        $RWT.dropDownButton.openMenu(this);
        return false;
    },
    openMenu: function(button) {
        var newMenuId = $(button).attr('menuid');
        var prevMenu = document.getElementById($RWT.dropDownButton.openedMenuId);
        if ($RWT.dropDownButton.openedMenuId) {
            if ($RWT.dropDownButton.openedMenuId == newMenuId && prevMenu) {
                $RWT.dropDown.cancel(document.getElementById(newMenuId));
            } else {   
                if (prevMenu) {
                    $RWT.dropDown.cancel(prevMenu);
                }
                $RWT.actions.event(button, "expose");
                $RWT.dropDownButton.openedMenuId = newMenuId;
            }
        } else {
            $RWT.actions.event(button, "expose");
            $RWT.dropDownButton.openedMenuId = newMenuId;
        }
    }
};

$RWT.dropDownButton.openedMenuId;

$RWT.delayedPopupBtn = {
    onmousedown : function () {
        timeRemaining = 200;
        this.wasMouseUp =  false;
        $RWT.delayedPopupBtn.obj = this;
        $(this).on('mouseup', function() {
            if (this.wasMouseUp == false) {
                this.wasMouseUp = true;
            }
        });
        this.onclick = function() {
              return false;
        };
        
        if ($(this).attr('relativeelemid') == null) {
            $RWT.delayedPopupBtn.interval = setInterval(function(){
                var obj = $RWT.delayedPopupBtn.obj;
                if (timeRemaining > 0 && obj.wasMouseUp == false) {
                    timeRemaining -= 1;
                } else if (timeRemaining > 0 && obj.wasMouseUp == true) {
                    $RWT.actions.event(obj, 'click');
                    clearInterval($RWT.delayedPopupBtn.interval);
                } else if (timeRemaining <= 0) {
                    clearInterval($RWT.delayedPopupBtn.interval);
                    $RWT.dropDownButton.openMenu(obj);
                } 
            }, 1, this);
        } else {
            var menu = document.getElementById($(this).attr('menuid'));
            menu.isShown = true;
            $RWT.dropDown.cancel(menu);
            this.wasMouseUp = false;
        }
    },
    interval : null,
    obj : null
};
$RWT.menuButton = {
    mouseenter: function () {
        var parentMenu = WpsUtils.findParentByClassName(this, "rwt-menu");
        parentMenu.onMenu = true;
        $RWT.subMenu.closeSubMenu(parentMenu);
        $(this).siblings().removeClass("rwt-ui-choosable-pointed-blue");
        $(this).removeClass("rwt-ui-choosable-pointed");
        if ($(this).hasClass("rwt-menu-item")) {
            $(this).addClass("rwt-ui-choosable-pointed-blue");
            var subMenu = document.getElementById($(this).attr("menuId"));
            parentMenu.openedMenu = subMenu;
            $(parentMenu).off('blur');
            parentMenu.isBlur = false;
            $RWT.subMenu.layout(subMenu, parentMenu, this);
            $(subMenu).off('blur');
            subMenu.isBlur = false;
            subMenu.parentMenu = parentMenu;
        } else {
            if ($(parentMenu).attr('firstLevelMenu') == 'true' && !parentMenu.isBlur) {
                $(parentMenu).blur($RWT.dropDown.blur);
                parentMenu.isBlur = true;
            } else if (!parentMenu.isBlur){
                $(parentMenu).blur($RWT.subMenu.blur);
                parentMenu.isBlur = true;
            }
            $(this).addClass("rwt-ui-choosable-pointed-blue-hover");
            $(parentMenu).focus();
        }
    },
    mouseleave: function () {
        if ($(this).hasClass("rwt-menu-item")) {
            var subMenu = document.getElementById($(this).attr("menuId"));
            if (!subMenu.isBlur) {
                $(subMenu).blur($RWT.subMenu.blur);
                subMenu.isBlur = true;
            }
            $(subMenu).focus();
        } 
        WpsUtils.findParentByClassName(this, "rwt-menu").onMenu = false;
    }
};
$RWT.subMenu = {
    layout: function (subMenu, parentMenu, menuButton) {
        var minwidth = 0;
        var btn_pos = $(menuButton).offset();
        $(subMenu).show();
        var size = WpsUtils.getBrowserWindowSize();
        var maxheight = size.height - btn_pos.top - 20;
        if (maxheight < 60 && btn_pos.top > 60) {//try to place at top
            maxheight = btn_pos.top - 20;
            $(subMenu).css("bottom", size.height - btn_pos.top + 'px').css("left", btn_pos.left + $(menuButton).outerWidth() + 'px');
        } else {
            $(subMenu).offset({
                top: btn_pos.top,
                left: btn_pos.left + $(menuButton).outerWidth()
            });
        }
        var maxheight = 1000;
        var maxwidth = 1000;
        $(subMenu).blur($RWT.subMenu.blur);
        $(subMenu)
                .css("zIndex", parseInt($(parentMenu).css('zIndex'), 10)) 
                .css("position", "absolute")
                .css("max-height", maxheight + 'px')
                .css("max-width", maxwidth + 'px')
                .css("min-width", minwidth + 'px')
                .attr("tabindex", 1)
                .keydown($RWT.dropDown.keyDown)
                .show("fast", function () {
                    $(subMenu).focus();
                    this.isShown = true;
                });
    },
    blur: function () {
        if (!this.onMenu) {
            $RWT.actions.event(this, 'close-sub-menu', '');
        }
    }
};
$RWT.subMenu.closeSubMenu = function (menu) {
    if (menu.openedMenu) {
        var openedMenu = menu.openedMenu;
        $RWT.subMenu.closeSubMenu(openedMenu);
        $(openedMenu).off("blur");
        $(openedMenu).hide();
        var liChild = openedMenu.selectedElement;
        if (openedMenu.selectedElement != undefined) {
            $(liChild).removeClass("rwt-ui-choosable-pointed-blue");
            menu.element = null;
        }
        menu.openedMenu = null;
    }
};
$RWT.menu = {
    beforeClose : function(obj) {
        var menuBar = $("#" + $(obj).attr('handler_id'));
        var curMenu = $("#" + $(menuBar).attr('relativeelemid'));
        curMenu.off('mouseenter');
        $(curMenu).siblings().off('mouseenter');
        $(curMenu).off('mouseleave');
        $(curMenu).siblings().off('mouseleave');
    },
    itemClick: function() {
        if ($(this).attr('disabled') != 'disabled') {
            var index = $(this).index();
            var menu = WpsUtils.findParentByClassName(this, "rwt-menu");
            for (var parentMenu = menu; parentMenu != null; parentMenu = parentMenu.parentMenu) {
                $(parentMenu).hide();
                var handlerElem = $(parentMenu).attr("handler_id"); //menuBar menu check
                if (handlerElem) {
                    var relativeElem = $(document.getElementById(handlerElem)).attr("relativeelemid");
                    if (relativeElem) {
                        $(document.getElementById(relativeElem)).removeClass("rwt-ui-choosable-pointed-blue");
                    }
                }
            }
            $RWT.actions.event(menu, 'menuItemClicked', String(index));
        }
    }
};

$RWT.getAdjustSize = function(elem) {
    if ($(elem).css('display') != 'none') {
        $RWT._layout_set[elem.id] = null;
        var adjustSize, adjustWidth, adjustHeight, final, adjustSizeChild;
        if (elem.calcAdjustSize != null) {        
            adjustSize = elem.calcAdjustSize();
            if (adjustSize != null) {
                adjustWidth = adjustSize.width == null ? 0 : adjustSize.width;
                adjustHeight = adjustSize.height == null ? 0 : adjustSize.height;
                final = adjustSize.final;
            }
        }
        if (!final) {
            WpsUtils.iterateAll(elem, function(c){
                if ($(c).css('display') != 'none') {
                    var adjustSize = $RWT.getAdjustSize(c);
                    if (adjustSizeChild==null){
                        adjustSizeChild = adjustSize;
                    }else{
                        if (adjustSize.width > adjustSizeChild.width) {
                            adjustSizeChild.width = adjustSize.width;
                        }
                        if (adjustSizeChild.height == null) {
                            adjustSizeChild.height = adjustSize.height;
                        } else {
                            adjustSizeChild.height += (adjustSize.height == null ? 0 : adjustSize.height);
                        }
                    }
                }
            });
        }
        //merge adjustSizeChild and adjustSize
        var finalWidth;
        if (adjustWidth == null) {
            finalWidth = adjustSizeChild == null ? null : adjustSizeChild.width;
        } else if (adjustSizeChild != null && adjustSizeChild.width != null) {
            finalWidth = adjustWidth + adjustSizeChild.width;
        } else {
            finalWidth = adjustWidth;
        }
        var finalHeight;
        if (adjustHeight == null) {
            finalHeight = adjustSizeChild == null ? null : adjustSizeChild.height;
        } else if (adjustSizeChild != null && adjustSizeChild.height != null) {
            finalHeight = adjustHeight + adjustSizeChild.height;
        } else {
            finalHeight = adjustHeight;
        }
    }
    return {"width": finalWidth, "height": finalHeight};
}

$RWT.menuContainer = {
    layout: function (container) {
        WpsUtils.iterateNames(container, 'div', function (div) {
            var isIE = (navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true);
            if (isIE) {
                     $(div).find("a").attr("unselectable", "on");
                }
            if ($(div).attr('activeMenu') == 'true' && $(div).attr('firstLevelMenu') == 'true') {
                if ($(div).attr('coordinates')) {
                    $RWT.dropDown.layout(div, null, null, $(div).attr('coordinates'));
                } else {
                    $RWT.dropDown.layout(div, null, null, null);
                }
            } else {
                $(div).hide();
            }
        });
    },
    container: function (body) {
        var res = [];
        WpsUtils.iterateNames(body, 'div', function (d) {
            if (d.className && d.className.indexOf('rwt-menu-container') >= 0) {
                res.push(d);
            }
        });
        return res;
    }
};
$RWT.traceTrayItem = {
    layout: function (traceTrayItem) {
        if ($(traceTrayItem).attr("blink") == "true") {
            if ($RWT.traceTrayItem.id == null) {
                $RWT.traceTrayItem.traceTrayItem = traceTrayItem;
                $RWT.traceTrayItem.blink();
            }
        } else {
            clearInterval($RWT.traceTrayItem.id);
            $RWT.traceTrayItem.id = null;
            $($(traceTrayItem).children()[0]).css('visibility', 'visible');
        }
    },
    blink: function () {
        $RWT.traceTrayItem.id = setInterval(function () {
            var img = $($RWT.traceTrayItem.traceTrayItem).children()[0];
            if ($(img).css('visibility') == 'visible') {
                $(img).css('visibility', 'hidden');
            } else {
                $(img).css('visibility', 'visible');
            }
        }, 500);
    }
};
$RWT.groupBox = {
    layout: function (gb) {
        var adjustMode = $(gb).attr('adjustMode');
        WpsUtils.iterateNames(gb, 'div', function (div) {
            if ($(div).attr('role') == 'group') {
                    WpsUtils.iterateAll(div, function (d) {
                            if (d.className && typeof (d.className.indexOf) !== 'undefined' && d.className.indexOf('rwt-ui-auto-height') >= 0 && d.rwt_f_adjustHeight != null) {
                                d.rwt_f_adjustHeight(d);
                            }
                        }
                    );
                    if (adjustMode == 'heightByContent'){
                        $(gb).height($(div).outerHeight() + 22);
                    }else if (adjustMode == 'contentByHeight'){
                        $(div).height($(gb).innerHeight() - 23);
                    }
            }
        });
    }
}
$RWT.waitDialog = {
    _id: 'rwt-wait-dialog',
    _ov_id: 'rwt-wait-overlay',
    _counter: 0,
    layout: function (overlay) {
        $(overlay).css('zIndex', '999999');
        var wait = WpsUtils.findFirstChild(overlay);
        if (wait != null) {
            var label = WpsUtils.findChildByLocalName(wait, 'label');
            var minw, minh
            if (label) {
                minw = $(label).outerWidth();
                minh = $(label).outerHeight();
            }
            else {
                minw = 100;
                minh = 50;
            }
            if (minw < 100)
                minw = 100;
            if (minh < 50)
                minh = 50;

            var screenSize = WpsUtils.getBrowserWindowSize();

            $(wait)
                    .width(minw)
                    .height(minh)
                    .css('top', (screenSize.height / 2 - minh / 2) + 'px')
                    .css('left', (screenSize.width / 2 - minw / 2) + 'px');
        }
        //        $(overlay).animate({
        //            opacity: 0.5 
        //        },2000,function(){
        //          //  alert('complete');
        //        });
    },
    show: function () {
        var wait = $('#' + $RWT.waitDialog._id);
        if (!wait || wait.length == 0 || $RWT.waitDialog._counter == 0) {
            wait = $('<div id="' + $RWT.waitDialog._id + '">Please Wait...</div>');
            wait.css('width', '100px');
            wait.css('height', '60px');
            wait.css('color', 'white');
            wait.css('vertical-align', 'middle');
            wait.css('text-align', 'center');
            wait.css('position', 'absolute');
            wait.addClass('ui-corner-all');
            var screenSize = WpsUtils.getBrowserWindowSize();
            wait.css('top', (screenSize.height / 2 - 30) + 'px');
            wait.css('left', (screenSize.width / 2 - 50) + 'px');
            wait.css('display', 'block');

            wait.css('zIndex', 999999);
            wait.css('opacity', 0);
            wait.css('background-color', 'black');
            var overlay = $('#' + $RWT.waitDialog._ov_id);
            if (!overlay || overlay.length == 0) {
                overlay = $('<div id="' + $RWT.waitDialog._ov_id + '" class="ui-widget-overlay">');
                overlay.css('zIndex', '999998');
                $('body').append(overlay);
            }
            $('body').append(wait);

        }
        $RWT.waitDialog._counter++;
    },
    hide: function () {

        if ($RWT.waitDialog._counter > 0) {
            $RWT.waitDialog._counter--;
            if ($RWT.waitDialog._counter == 0) {
                $('#' + $RWT.waitDialog._id).remove();
                $('#' + $RWT.waitDialog._ov_id).remove();

            }
        }
    }
}
$RWT.checkBox = {
    layout: function (cb) {
        var pos = 0;
        var h = $(cb).innerHeight();
        var hh = h / 2;
        WpsUtils.iterateAll(cb, function (c) {
            var h = $(c).outerHeight();
            $(c).css('left', pos + 'px')
                    //.css('position', 'absolute')
                    .css('top', (hh - h / 2) + 'px');
            pos = $(c).outerWidth() + 3;
        });
    },
    selectChange: function (event) {
        var checkedAttrValue = this.checked ? "true" : "false";
        $RWT.actions.event(this, 'onclick', checkedAttrValue);
    }
};
$RWT.slider = {
    layout: function () {
        function calcualteValue(slider, left, step, thumb) {
            var retVal = 0;
            if (left >= 0) {
                var V = slider.offsetWidth - 12;
                var min = parseInt($(slider).attr('min'));
                var max = parseInt($(slider).attr('max'));
                var value = 0;
                if (V) {
                    var vv = parseInt((left * (max - min) + V * min) / V);
                    var minV = vv - vv % step;
                    var maxV;
                    if (minV <= min) {
                        minV = min;
                    }
                    maxV = minV + parseInt(step) > max ? max : minV + parseInt(step);
                    value = step ? (Math.abs(maxV - vv) > step / 2 ? minV : maxV) : vv;
                }
                retVal = value;

            }
            if (thumb) {
                setValue(slider, thumb, min, max, retVal);
            }
            return retVal;
        }

        function setValue(slider, thumb, min, max, initValue) {
            var V = slider.offsetWidth - 12;
            var init = parseInt(initValue);
            var minV = parseInt(min)
            var maxV = parseInt(max)
            if (init >= minV && init <= maxV) {
                var newleft = ((init - minV) * V) / (maxV - minV);
                thumb.style.left = newleft + 'px';
            }
        }

        function setInitialValue(slider, thumb, min, max, initValue) {
            slider.initValSet = true;
            setValue(slider, thumb, min, max, initValue);
        }
        var slider = $(this).find('div.rwt-ui-slider')[0];
        if (slider) {
            var thumb = WpsUtils.findFirstChild(slider);
            if (thumb) {
                var initVal = $(slider).attr('value');
                var max = $(slider).attr('max');
                var min = $(slider).attr('min');
                var stepAttr = $(slider).attr('step');
                var step = stepAttr && stepAttr > 1 ? (stepAttr >= Math.abs(max) ? Math.abs(max - min) : stepAttr) : 1;
                if (!slider.initValSet && initVal != min) {
                    setInitialValue(slider, thumb, min, max, initVal);
                }

                thumb.ondragstart = function () {
                    return false;
                };
                thumb.onmousedown = function (e) {
                    e = WpsUtils.fixEvent(e);
                    var self = this;
                    $(self).addClass('rwt-ui-selected-item');
                    var thumbCoords = WpsUtils.getCoords(thumb);
                    var shiftX = e.pageX - thumbCoords.left;
                    //var shiftY = e.pageY - thumbCoords.top;//no need cause thumb moves horizontally only

                    var sliderCoords = WpsUtils.getCoords(slider);

                    var val = calcualteValue(slider, e.pageX - shiftX - sliderCoords.left, step, this);
                    //тултип со значением
                    $(self).html(
                            '<div class="rwt-slider-tooltip rwt-slider-tooltip-top rwt-slider-tip"><div class="rwt-slider-tooltip-arrow"></div><div class="rwt-slider-tooltip-inner">' +
                            val + '</div></div>'
                            );
                    document.onmousemove = function (e) {
                        e = WpsUtils.fixEvent(e);
                        var newLeft = e.pageX - shiftX - sliderCoords.left;
                        if (newLeft < 0) {//курсор вышел за левую границу слайдера
                            newLeft = 0;
                        }
                        var rightEdge = slider.offsetWidth - thumb.offsetWidth;
                        var value;

                        if (newLeft > rightEdge) {//курсор вышел за правую границу слайдера
                            newLeft = rightEdge;
                        }

                        value = calcualteValue(slider, newLeft, step, self);
                        $(self).html(
                                '<div class="rwt-slider-tooltip rwt-slider-tooltip-top rwt-slider-tip"><div class="rwt-slider-tooltip-arrow"></div><div class="rwt-slider-tooltip-inner">' +
                                value + '</div></div>'
                                );
                        //self.style.left = newLeft + 'px';
                    };

                    document.onmouseup = function () {
                        document.onmousemove = document.onmouseup = null;
                        $(self).removeClass('rwt-ui-selected-item');
                        $(self).html(""); //убираем тултип

                        var value = calcualteValue(slider, (self.style.left).replace('px', ''), step, self);
                        slider.setAttribute("value", value);
                        if (value)
                            $RWT.actions.event(slider.parentNode, 'valueChanged', String(value));
                    };
                    return false;
                };
                thumb.onmouseup = function (e) {
                    $(this).html("");
                };
            }
        }
    }
};
$RWT.altColorWidget = {
    layout: function (container) {
        var input, colorDiv;
        var i = $(container).find('div.rwt-ui-slider');
        var c = $(container).find("[role = 'alterColor']");
        if (i && c) {
            input = i[0];
            colorDiv = c[0];
        }
        if (input && colorDiv) {
            var value = input.getAttribute("value") / 100; //init
            $(colorDiv).css("opacity", value); //init
            function slide(e) {
                if (input && colorDiv) {
                    var v = input.getAttribute("value") / 100;
                    $(colorDiv).css("opacity", v);
                }
            }
            //input.onmouseup = slide;
        }
    }
};
$RWT.panel = {
    layout: function (panel) {
        var table = WpsUtils.findChildByLocalName(panel, 'table');
        if (table) {
            var tbody = WpsUtils.findChildByLocalName(table, 'tbody');
            if (tbody) {
                var rows = [];
                var rowCount = 0;
                WpsUtils.iterateNames(tbody, 'tr', function (tr) {
                    rows.push(tr);
                    rowCount++;
                });
                if (rowCount > 0) {
                    var totalH = $(panel).innerHeight() - 4;

                    var totalW = $(panel).innerWidth() - 4;
                    $(table).width(totalW).height(totalH);
                    var unsizedRows = [];
                    for (var i = 0; i < rowCount; i++) {

                        var cols = [];
                        var hfitcols = [];
                        var vfitcols = [];
                        var colsCount = 0;
                        WpsUtils.iterateNames(rows[i], 'td', function (td) {
                            if ($(td).attr('hfit') == 'true') {
                                hfitcols.push(td);
                                td.hfit = true;
                            }
                            if ($(td).attr('vfit') == 'true') {
                                vfitcols.push(td);
                                td.vfit = true;
                            }
                            cols.push(td);
                            colsCount++;
                        });
                        if (colsCount > 0) {
                            var fitW = 0;

                            for (var j = 0; j < hfitcols.length; j++) {
                                var td = hfitcols[j];
                                var child = WpsUtils.findFirstChild(td);
                                if (child) {
                                    // var w = $(child).outerWidth()+2;
                                    $(td).width(1);

                                    //fitW += w;

                                }
                            }
                            var maxFitH = 0;
                            for (j = 0; j < vfitcols.length; j++) {
                                td = vfitcols[j];
                                child = WpsUtils.findFirstChild(td);
                                if (child) {
                                    var h = $(child).outerHeight() + 2;
                                    $(td).height(h);
                                    if (maxFitH < h)
                                        maxFitH = h;
                                }
                            }

                            var realWidth = totalW - fitW;
                            var colWidth = realWidth / colsCount;
                            for (j = 0; j < colsCount; j++) {
                                if (!$(cols[j]).attr('colspan')) {
                                    if (!cols[j].hfit) {
                                        //$(cols[j]).css('width',colWidth+'px');
                                        $(cols[j]).css('width', '100%');
                                    }
                                }
                            }
                        }
                        if (vfitcols.length > 0 && maxFitH > 0) {
                            $(rows[i]).css('height', maxFitH + 'px');
                            if (unsizedRows.height)
                                unsizedRows.height += maxFitH;
                            else
                                unsizedRows.height = maxFitH;
                        } else {
                            unsizedRows.push(rows[i]);
                        }
                    }
                    if (unsizedRows.length > 0) {
                        var rowHeight = (totalH - (unsizedRows.height ? unsizedRows.height : 0)) / unsizedRows.length;
                        for (i = 0; i < unsizedRows.length; i++) {
                            $(unsizedRows[i]).css('height', rowHeight + 'px');
                        }
                    }
                }
            }
        }
    }
}
$RWT.filterParameters = {
    keyDown: function (event) {
        if (event.keyCode == 13) {
            $RWT._ce(event);
            $RWT.finishEdit();
            if (event.target != null && event.target.blur != null) {
                event.target.blur();
            }
            $RWT.actions.event(this, 'apply-filter', '');
            if (event.target != null && event.target.focus != null) {
                event.target.focus();
            }
        }
    }
}

$RWT.fileInput = {
    layout: function (fi) {
        var input = WpsUtils.findChildByLocalName(fi, 'input');
        if (input) {
            $(input).change($RWT.files.processFileInput());
        }
    }
}
$RWT.files = {
    _registry: {},
    download: function (name, url) {
        window.open('rwtext/file/' + url, null, "menubar=no,location=no,resizable=1,scrollbars=1,status=no,width=800,height=600");
    },
    downloadAndSave: function (name, url) {
        var wnd = window.open('rwtext/file/' + url, null, "menubar=no,location=no,resizable=1,scrollbars=1,status=no,width=800,height=600");
    },
    processFileChoice: function () {
        if (this.value != null && this.value != "") {
            var contextId = this.getAttribute('contextId');
            if (contextId != null) {
                var eventParam;
                if (this.files != null && this.files[0] != null) {
                    var file = this.files[0];
                    if (file.size != null && !isNaN(file.size)) {
                        eventParam = file.size + ' ' + file.name;
                    } else {
                        eventParam = '-1 ' + file.name;
                    }
                } else {//IE
                    eventParam = '-1 ' + this.value.match(/[^\/\\]+$/);
                }
                var eventAcceptor = document._findNodeById(contextId);
                if (eventAcceptor != null) {
                    this.contextId = contextId;
                    $RWT.actions.event(eventAcceptor, 'file-selected', eventParam);
                    $RWT.files._registry[contextId] = this;
                }
            }
        }
    },
    processFileInput: function () {
        if (this.value != null && this.value != "") {
            if (this.initiatorId != null) {
                var eventParam;
                if (this.files != null && this.files[0] != null) {
                    var file = this.files[0];
                    if (file.size != null && !isNaN(file.size)) {
                        eventParam = file.size + ' ' + file.name;
                    } else {
                        eventParam = '-1 ' + file.name;
                    }
                } else {//IE
                    eventParam = '-1 ' + this.value.match(/[^\/\\]+$/);
                }
                var eventAcceptor = document._findNodeById(this.initiatorId);
                if (eventAcceptor != null) {
                    $RWT.actions.event(eventAcceptor, 'file-selected', eventParam);
                    $RWT.files._registry[this.initiatorId] = this;
                }
            } else {//legacy way
                $RWT.actions.event(this, 'file-selected', this.value);
                $RWT.files._registry[this.id] = this;
            }
        }
    },
    upload: function (srcId, uuid) {
        var fileInput = $RWT.files._registry[srcId];
        var uploadStarted = false;
        try {
            if (fileInput.contextId == null) {
                uploadStarted = $RWT.files._fileUpload(uuid, fileInput);
            } else {
                uploadStarted = $RWT.files._fileUploadNew(uuid, fileInput);
            }
        } catch (e) {
            $RWT.actions.event_id(srcId, 'upload-rejected', e.description);
            return;
        }
        if (uploadStarted) {
            $RWT.actions.event_id(srcId, 'upload-started');
        } else {
            $RWT.actions.event_id(srcId, 'upload-rejected');
        }
    },
    _clearIframe: function (iframe) {
        if (iframe) {
            $(iframe).remove();
        }
    },
    _fileUploadNew: function (id, file) {
        file.name = id;
        var form = $('<form id=' + id + ' action="file-upload.html" method="POST" enctype="multipart/form-data" style="display: none;" target=' + id + '-iframe></form>');
        var iframe = $('<iframe name=' + id + '-iframe id=' + id + '-iframe style="display: none;" ></iframe>');
        iframe.append(form);
        $('body').append(iframe);
        iframe[0].onload = function () {
            var iframe = this;
            if (iframe != null) {
                $RWT.files._clearIframe(iframe);
            }
        }
        form.append(file);
        form.submit();
        return true;
    },
    _fileUpload: function (id, file) {
        var form = file.parentNode;
        if (form != null) {
            //            WpsUtils.iterateAll(form,function (e){
            //                if(e.getAttribute('user-role')=='submit-button'){
            //                    e.click();
            //                }
            //            })
            //  form.submit();
            var iframe = $('<iframe name=' + form.id + '-iframe id=' + form.id + '-iframe style="display: none;" ></iframe>');
            $(form).attr('target', form.id + '-iframe');
            $('body').append(iframe);
            iframe[0].onload = function () {
                var iframe = this;
                if (iframe != null) {
                    $RWT.files._clearIframe(iframe);
                }
            }
            //test form parents (for IE)
            var p = form.parentNode;
            var insert = form.document == null;
            if (!insert) {
                do {
                    if (p == document) {
                        break;
                    }
                    if (p == null) {
                        insert = true;
                        break;
                    }
                    p = p.parentNode;

                } while (true);
            }
            if (insert) {
                iframe.append(form);
            }
            form.submit();
            return true;
        }
        return false;
    }
}
$RWT.arrayEditor = {
    layout: function (arrayEditorTable) {
        var isDefined = arrayEditorTable.getAttribute('isdefined') == 'true';
        if (arrayEditorTable.clientHeight < arrayEditorTable.scrollHeight && isDefined) {
            $(arrayEditorTable).css("overflow-y", "scroll");
        }
        else {
            $(arrayEditorTable).css("overflow-y", "hidden");
        }
    },
    _findArrayEditor: function (node) {
        var arrayEditorTable = WpsUtils.findParentByClassName(node, 'rwt-array-editor-table');
        return arrayEditorTable == null ? null : arrayEditorTable.parentNode.parentNode;
    },
    _getArrayEditorTable: function (arrayEditor) {
        var horizontalLayout = WpsUtils.findChildByLocalName(arrayEditor, "div");
        return horizontalLayout == null ? null : WpsUtils.findChildByLocalName(horizontalLayout, "div");
    },
    _focusCell: function (node) {
        if (node != null && node.className != null && node.className.indexOf('rwt-array-editor-value-cell') > 0) {
            node.focus();
        }
    },
    onCellClick: function (event) {
        if (this.getAttribute('row') != null) {
            var arrayEditorTable = WpsUtils.findParentByClassName(this, 'rwt-array-editor-table');
            var row = this.getAttribute('row');
            var editing_row = arrayEditorTable.getAttribute('editing') == null ? -1 : parseInt(arrayEditorTable.getAttribute('editing'));
            if (row != editing_row) {
                var arrayEditor = $RWT.arrayEditor._findArrayEditor(this);
                if (arrayEditor != null) {
                    $RWT.actions.event(arrayEditor, 'click', String(row));
                    event.preventDefault ? event.preventDefault() : event.returnValue = false;
                }
            }
        }
    },
    _finishEdit: function (node) {
        if (node != null && node.nodeName == 'INPUT') {
            node.blur();
        }
    },
    _inEditingMode: function (node) {
        return node != null && node.nodeName == 'INPUT';
    },
    onKeyDown: function (event) {
        var isModifier = event.altKey || event.ctrlKey || event.metaKey;
        //var arrayEditor = $RWT.arrayEditor._findArrayEditor(this);
        var arrayEditor = this;
        if (arrayEditor != null) {
            var arrayEditorTable = $RWT.arrayEditor._getArrayEditorTable(arrayEditor);
            if (arrayEditorTable != null) {
                var isDefined = arrayEditorTable.getAttribute('isdefined') == 'true';
                var isEditing = arrayEditorTable.getAttribute('editing') != null;
                var processed = false;
                if (event.keyCode == 38 && isDefined && !isModifier && !isEditing) {//up
                    $RWT.actions.event(arrayEditor, 'up');
                    processed = true;
                }
                else if (event.keyCode == 40 && isDefined && !isModifier && !isEditing) {//down
                    $RWT.actions.event(arrayEditor, 'down');
                    processed = true;
                }
                if (event.keyCode == 36 && isDefined && !isModifier && !isEditing) {//home
                    $RWT.actions.event(arrayEditor, 'home');
                    processed = true;
                }
                else if (event.keyCode == 35 && isDefined && !isModifier && !isEditing) {//end
                    $RWT.actions.event(arrayEditor, 'end');
                    processed = true;
                }
                else if (arrayEditor.getAttribute('readonly') == 'false') {
                    if (event.keyCode == 45 && !isModifier && !isEditing) {//insert
                        $RWT.arrayEditor._finishEdit(event.target);
                        $RWT.actions.event(arrayEditor, 'add');
                        processed = true;
                    }
                    else if (isDefined) {
                        switch (event.keyCode) {
                            case 38://up
                                if (event.ctrlKey && !isEditing) {
                                    $RWT.arrayEditor._finishEdit(event.target);
                                    $RWT.actions.event(arrayEditor, 'move-up');
                                    processed = true;
                                }
                                break;
                            case 40://down
                                if (event.ctrlKey && !isEditing) {
                                    $RWT.arrayEditor._finishEdit(event.target);
                                    $RWT.actions.event(arrayEditor, 'move-down');
                                    processed = true;
                                }
                                break;
                            case 36://home
                                if (event.ctrlKey && !isEditing) {
                                    $RWT.arrayEditor._finishEdit(event.target);
                                    $RWT.actions.event(arrayEditor, 'move-home');
                                    processed = true;
                                }
                                break;
                            case 35://end
                                if (event.ctrlKey && !isEditing) {
                                    $RWT.arrayEditor._finishEdit(event.target);
                                    $RWT.actions.event(arrayEditor, 'move-end');
                                    processed = true;
                                }
                                break;
                            case 46://del
                                if (!isModifier && !isEditing) {
                                    $RWT.actions.event(arrayEditor, 'del');
                                    processed = true;
                                }
                                else if (event.ctrlKey && !isEditing) {
                                    $RWT.actions.event(arrayEditor, 'clear');
                                    processed = true;
                                }
                                break;
                            case 113://F2
                                if (!isEditing) {
                                    $RWT.actions.event(arrayEditor, 'edit');
                                    processed = true;
                                }
                                break;
                            case 13://enter
                                if (event.ctrlKey) {
                                    $RWT.actions.event(arrayEditor, 'edit');
                                    processed = true;
                                }
                                else if (isEditing) {
                                    if (event.target != null && event.target.blur != null) {
                                        event.target.blur();
                                    }
                                    $RWT.actions.event(arrayEditor, 'finishedit');
                                    processed = true;
                                }
                                break;
                            case 27://Esc
                                if (isEditing) {
                                    $RWT.actions.event(arrayEditor, 'canceledit');
                                    processed = true;
                                }
                                break;
                            case 32://space
                                if (event.ctrlKey) {
                                    $RWT.actions.event(arrayEditor, 'null');
                                    processed = true;
                                }
                                break;

                        }
                    }
                }
                if (processed) {
                    event.preventDefault ? event.preventDefault() : event.returnValue = false;
                }
            }
        }
    },
    onKeyPress: function (event) {
        var isModifier = event.altKey || event.ctrlKey || event.metaKey;
        var arrayEditor = $RWT.arrayEditor._findArrayEditor(this);
        if (!isModifier && arrayEditor != null && arrayEditor.getAttribute('readonly') == 'false') {
            var arrayEditorTable = $RWT.arrayEditor._getArrayEditorTable(arrayEditor);
            if (arrayEditorTable != null && arrayEditorTable.getAttribute('editing') == null) {
                var charCode = null;
                if (event.which == null) {
                    charCode = event.keyCode; // Old IE
                }
                else if (event.which != 0 && event.charCode != 0) {
                    charCode = event.which; // All others
                }
                else {
                }
                if (charCode != null) {
                    $RWT.actions.event(arrayEditor, 'edit', String.fromCharCode(charCode));
                    event.preventDefault ? event.preventDefault() : event.returnValue = false;
                }
            }
        }
    },
    onDblClick: function (event) {
        var arrayEditor = this.parentNode == null || this.parentNode.parentNode ? null : this.parentNode.parentNode.parentNode;
        if (arrayEditor != null && arrayEditor.getAttribute('readonly') == 'true' && arrayEditor.getAttribute('disabled') == null) {
            $RWT.actions.event(arrayEditor, 'dblclick');
            event.preventDefault ? event.preventDefault() : event.returnValue = false;
        }
    }
}
$RWT.verticalBox = {
    layout: function (container) {
        var autosize = $(container).attr("autoresize");
        if (autosize != null) {
            autosize = autosize.split(',');
        }
        var padding = $(container).attr("internal-padding");
        if (padding != null)
            padding = parseInt(padding, 10);
        if (padding == NaN) {
            padding = null;
        }
        var components = {};

        WpsUtils.iterateAll(container, function (c) {
            if ($(c).css('display') != 'none') {
                components[c.id] = c;
                c.vbox_auto = false;
            }
        });
        if (autosize) {
            for (var i = 0; i < autosize.length; i++) {
                var c = components[autosize[i]];
                if (c) {
                    c.vbox_auto = true;
                }
            }
        }


        for (cid in components) {
            c = components[cid];
            var jqobj = $(c);
            if (!c.vbox_auto) {
                if (c.height_css != null) {
                    jqobj.css('height', c.height_css + 'px');
                }
            }
        }

        var not_auto_h = 0;
        var auto_count = 0;
        var total_count = 0;
        for (var cid in components) {
            c = components[cid];
            if (!c.vbox_auto) {
                not_auto_h += $(c).outerHeight();
            } else {
                auto_count++;
            }
            total_count++;
        }
        var height = $(container).innerHeight();
        if (padding != null) {
            height -= (total_count + 1) * padding;
        }
        var diff = height - not_auto_h;
        var ah = 0;
        var posy = padding == null ? 0 : padding;

        if (auto_count > 0) {
            ah = Math.floor(diff / auto_count);
        }
        var starty = posy;
        for (cid in components) {
            c = components[cid];
            jqobj = $(c);
            if (c.vbox_auto) {
                if (c.height_css == null) {
                    c.height_css = jqobj.outerHeight();
                }
                jqobj.height(ah - (jqobj.outerHeight() - jqobj.innerHeight()));
            }
            jqobj.css('position', 'absolute')
                    .css('left', '0')
                    .css('width', '100%')
                    .css('top', posy + 'px');
            posy = jqobj.position().top + jqobj.outerHeight();
            if (padding != null) {
                posy += padding;
            }
        }

        if (auto_count == 0) {
            var space = posy - starty;
            var align = $(container).attr('valign');
            if (align == 'center') {
                posy = (height - space) / 2;
            } else if (align == 'bottom') {
                posy = height - space;
                if (padding != null)
                    posy -= padding;
            } else
                return;
            for (cid in components) {
                c = components[cid];
                jqobj = $(c);
                jqobj.css('top', posy + 'px');
                posy = jqobj.position().top + jqobj.outerHeight();
                if (padding != null) {
                    posy += padding;
                }
            }
        }

    }
}
$RWT.horizontalBox = {
    layout: function (container) {
        var autosize = $(container).attr("autoresize");
        if (autosize != null) {
            autosize = autosize.split(',');
        }
        var padding = $(container).attr("internal-padding");
        if (padding != null)
            padding = parseInt(padding, 10);
        if (padding == NaN) {
            padding = null;
        }
        var components = {};

        WpsUtils.iterateAll(container, function (c) {
            if ($(c).css('display') != 'none') {
                components[c.id] = c;
                c.vbox_auto = false;
            }
        });
        if (autosize) {
            for (var i = 0; i < autosize.length; i++) {
                var c = components[autosize[i]];
                if (c) {
                    c.vbox_auto = true;
                }
            }
        }


        for (cid in components) {
            c = components[cid];
            var jqobj = $(c);
            if (!c.vbox_auto) {
                if (c.width_css != null) {
                    jqobj.css('width', c.width_css + 'px');
                }
            }
        }

        var not_auto_w = 0;
        var auto_count = 0;
        var total_count = 0;
        for (var cid in components) {
            c = components[cid];
            if (!c.vbox_auto) {
                not_auto_w += $(c).outerWidth();
            } else {
                auto_count++;
            }
            total_count++;
        }
        var width = $(container).innerWidth();
        if (padding != null) {
            width -= (total_count + 1) * padding;
        }
        var diff = width - not_auto_w;
        var aw = 0;
        if (auto_count > 0) {
            aw = Math.floor(diff / auto_count);
        }
        var posx = padding != null ? padding : 0;
        var startx = posx;
        for (cid in components) {
            c = components[cid];
            jqobj = $(c);
            if (c.vbox_auto) {
                if (c.width_css == null) {
                    c.width_css = jqobj.outerWidth();
                }
                jqobj.width(aw - (jqobj.outerWidth() - jqobj.innerWidth()));
            }
            jqobj.css('position', 'absolute')
                    .css('top', '0')
                    .css('height', '100%')
                    .css('left', posx + 'px');

            /*if (jqobj.hasClass('rwt-input-box') || (WpsUtils.findFirstChild(c) && WpsUtils.findFirstChild(c).className.indexOf('rwt-input-box')>0)){
             jqobj.css('top', 'calc(50% - 10px)');//выравниваем элементы контейнера по середине
             }*/

            posx = jqobj.position().left + jqobj.outerWidth();
            if (padding != null)
                posx += padding;
        }
        if (auto_count == 0) {
            var space = posx - startx;
            var align = $(container).attr('halign');
            if (align == 'center') {
                posx = (width - space) / 2;
            } else if (align == 'right') {
                posx = width - space;
                if (padding != null)
                    posx -= padding;
            } else {
                return;
            }
            for (cid in components) {
                c = components[cid];
                jqobj = $(c);
                jqobj.css('left', posx + 'px');
                posx = jqobj.position().left + jqobj.outerWidth();
                if (padding != null) {
                    posx += padding;
                }
            }

        }
        if (container.sci_data_$) {
            container.sci_data_$ = null;
        } else {
            container.sci_data_$ = 1;
            if (width != $(container).innerWidth()) {
                $RWT.horizontalBox.layout(container);
            }
        }
    }
}
$RWT.calendarWidget = {
    layout: function (calendar) {
        var yearEditor = WpsUtils.findFirstChild(calendar);
        var selectedDatesAsStr = calendar.getAttribute("selectedDates");
        calendar.arrSelectedDates = selectedDatesAsStr == null ? [] : eval(selectedDatesAsStr);
        var isDisabled = calendar.className != null && calendar.className.indexOf('ui-state-disabled') > 0;
        var updateCalendar = function () {
            if (!isDisabled) {
                $RWT.calendarWidget._initYearEditor(calendar, yearEditor);
            }
        }
        var flashDate = function () {
            $RWT.calendarWidget._flashDate(calendar);
        }
        var firstDay = calendar.getAttribute("firstDay");
        firstDay = firstDay == null || isNaN(parseInt(firstDay)) ? 0 : parseInt(firstDay);
        var options = {
            changeMonth: !isDisabled,
            showOn: 'button',
            disabled: isDisabled,
            showOtherMonths: true,
            selectOtherMonths: !isDisabled,
            firstDay: firstDay,
            onChangeMonthYear: function (year, month) {
                var curDate = $(calendar).datepicker("getDate");
                if (curDate != null && (curDate.getFullYear() != year || curDate.getMonth() != (month - 1))) {
                    var validDate = $RWT.calendarWidget._calcNearestValidDate(year, month - 1, curDate.getDate());
                    $(calendar).datepicker("setDate", validDate);
                    flashDate();
                }
            },
            beforeShowDay: function (date) {
                if (date != null && this.arrSelectedDates != null) {
                    var time = date.getTime();
                    var index = this.arrSelectedDates.indexOf(time);
                    if (index >= 0) {
                        return [true, 'ui-datepicker-selected-day', ''];
                    } else {
                        return [true, '', ''];
                    }
                }
                else {
                    return [true, '', ''];
                }
            }
        };
        $(calendar).datepicker(options);
        if (calendar.inited == null) {
            $(calendar).datepicker("option", "onSelect", flashDate);

            $RWT.inputBox.setBlockValueEvents(yearEditor, true);
            yearEditor.changeValueHandler = $RWT.calendarWidget._onYearChange;
            yearEditor.calendar = calendar;
            $(yearEditor).bind('keydown', $RWT.calendarWidget._onKeyDownInYearEditor);
            $(yearEditor).bind('mousewheel', $RWT.calendarWidget._onMouseWheelInYearEditor);
            calendar.inited = true;
        }
        calendar.blockUpdates = true;
        var minDate = new Date();
        if (calendar.getAttribute("minvalue") != null) {
            minDate.setTime(parseInt(calendar.getAttribute("minvalue")));
        }
        else {
            minDate.setHours(0, 0, 0);
            minDate.setFullYear(1586, 0, 1);
        }
        $(calendar).datepicker("option", "minDate", minDate);
        calendar.minDate = minDate;

        var maxDate = new Date();
        if (calendar.getAttribute("maxvalue") != null) {
            maxDate.setTime(parseInt(calendar.getAttribute("maxvalue")));
        }
        else {
            maxDate.setHours(0, 0, 0);
            maxDate.setFullYear(9999, 11, 31);
        }
        $(calendar).datepicker("option", "maxDate", maxDate);
        calendar.maxDate = maxDate;

        if (calendar.getAttribute("currentvalue") != null) {
            var curDate = new Date();
            curDate.setTime(parseInt(calendar.getAttribute("currentvalue")));
            $(calendar).datepicker("setDate", curDate);
        }
        calendar.blockUpdates = false;
        setTimeout(updateCalendar, 0);
    },
    _flashDate: function (calendar) {
        var yearEditor = WpsUtils.findFirstChild(calendar);
        var isDisabled = calendar.className != null && calendar.className.indexOf('ui-state-disabled') > 0;
        var updateCalendar = function () {
            if (!isDisabled) {
                $RWT.calendarWidget._initYearEditor(calendar, yearEditor);
            }
        }
        if (calendar.inited && !calendar.blockUpdates) {
            var curDate = $(calendar).datepicker("getDate");
            var timeWithOffset = curDate.getTime();
            var timeOffset = curDate.getTimezoneOffset() * 60 * 1000;
            var curTime = timeWithOffset - timeOffset;
            var srvTime =
                    calendar.getAttribute("currentvalue") == null ? 0 : parseInt(calendar.getAttribute("currentvalue"));

            if (curTime != srvTime) {
                $RWT.actions.event(calendar, 'change', curTime.toString());
            }
            else {
                setTimeout(updateCalendar, 0);
            }
        }
    },
    _updateInnerGemoetry: function (calendar) {
        var children = $(calendar).find('.ui-datepicker-header');
        if (children != null && children.length == 1) {
            $(children[0]).width($(calendar).innerWidth() - 7.5);
        }
        children = $(calendar).find('.ui-datepicker-calendar');
        if (children != null && children.length == 1) {
            $(children[0]).width($(calendar).innerWidth() - 6);
        }
    },
    _initYearEditor: function (calendar, yearEditor) {
        var children = $(calendar).find('.ui-datepicker-year');
        if (children != null && children.length == 1) {
            var lbYear = children[0];
            $(lbYear).bind('click', $RWT.calendarWidget._onLbYearClick);
            lbYear.editor = yearEditor;
            lbYear.calendar = calendar;
            yearEditor.lbYear = lbYear;
        }
    },
    _updateYearEditorGeometry: function (calendar, lbYear, yearEditor) {
        $(yearEditor).css('width', $(lbYear).outerWidth() + 20 + "px")
                .css('zIndex', $(lbYear).zIndex() + 1)
                .css('left', ($(calendar).position().left - calendar.offsetLeft + $(lbYear).position().left + 10) + "px")
                .css('top', ($(calendar).position().top - calendar.offsetTop + $(lbYear).position().top + 1) + "px")
                .css('position', 'absolute');
    },
    _fixMonthFieldWidth: function (calendar) {
        var children = $(calendar).find('.ui-datepicker-month');
        if (children != null && children.length == 1) {
            var lbMonth = children[0];
            $(lbMonth).css('width', "51%");
        }
    },
    _onKeyDownInYearEditor: function (event) {
        var isModifier = event.altKey || event.ctrlKey || event.metaKey;
        var keyCode = event.which;
        if (!isModifier) {
            if (keyCode == 13) {
                $RWT.calendarWidget._onYearChange(this, $RWT.inputBox.getValue(this));
                event.preventDefault ? event.preventDefault() : event.returnValue = false;
            }
            else if (keyCode == 27) {
                $(this).hide();
                event.preventDefault ? event.preventDefault() : event.returnValue = false;
            }
            else {
                var value = parseInt($RWT.inputBox.getValue(this));
                if (!isNaN(value)) {
                    switch (keyCode) {
                        case 38: //up
                            value++;
                            break;
                        case 40: //down
                            value--;
                            break;
                        case 33://pageup
                            value += 10;
                            break;
                        case 34://pagedown
                            value -= 10;
                            break;
                        default:
                            return;
                    }
                    if ($RWT.calendarWidget._checkYearRange(this.calendar, value)) {
                        $RWT.inputBox.setValue(this, value);
                    }
                    event.preventDefault ? event.preventDefault() : event.returnValue = false;
                }
            }
        }
    },
    _onMouseWheelInYearEditor: function (event) {
        var value = parseInt($RWT.inputBox.getValue(this));
        if (!isNaN(value)) {
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
            if (event.shiftKey) {
                delta *= 10;
            }
            value += delta;
            if ($RWT.calendarWidget._checkYearRange(this.calendar, value)) {
                $RWT.inputBox.setValue(this, value);
            }
        }
    },
    _onYearChange: function (editor, newValue) {
        $(editor).hide();
        if (typeof newValue == 'string') {
            newValue = parseInt(newValue)
        }
        if (!isNaN(newValue)) {
            var curDate = $(editor.calendar).datepicker("getDate");
            var minYear = editor.calendar.minDate == null ? 1586 : editor.calendar.minDate.getFullYear();
            var maxYear = editor.calendar.minDate == null ? 9999 : editor.calendar.maxDate.getFullYear();
            if (curDate != null && curDate.getFullYear() != newValue && $RWT.calendarWidget._checkYearRange(editor.calendar, newValue)) {
                var validDate =
                        $RWT.calendarWidget._calcNearestValidDate(newValue, curDate.getMonth(), curDate.getDate());
                $(editor.calendar).datepicker("setDate", validDate);
                $RWT.calendarWidget._flashDate(editor.calendar);
            }
        }
    },
    _checkYearRange: function (calendar, year) {
        var minYear = calendar.minDate == null ? 1586 : calendar.minDate.getFullYear();
        var maxYear = calendar.minDate == null ? 9999 : calendar.maxDate.getFullYear();
        return minYear <= year && maxYear >= year;
    },
    _calcNearestValidDate: function (year, month, day) {
        var result = new Date();
        result.setHours(0, 0, 0, 0);
        result.setFullYear(year, month, day);
        while (result.getDate() != day && day > 28) {
            day--;
            result.setFullYear(year, month, day);
        }
        return result;
    },
    _onLbYearClick: function (event) {
        if (this.editor != null) {
            $RWT.calendarWidget._updateYearEditorGeometry(this.calendar, this, this.editor);
            $RWT.inputBox.setValue(this.editor, $(this).text());
            var input = $RWT.inputBox.findInput(this.editor);
            $(this.editor).show();
            $(input).focus()
        }
    }
}


$RWT.buttonBox = {
    layout: function (buttonBox) {
        var ih = $(buttonBox).innerHeight();
        var centers = [];
        var centerLen = 0;
        WpsUtils.iterateAll(buttonBox, function (button) {
            var h = $(button).outerHeight();
            var top = (ih - h) / 2 - 1;
            $(button).css('top', top + 'px');
            if ($(button).css('float') !== 'right' && $(button).css('float') !== 'left') {
                centers.push(button);
                centerLen += $(button).outerWidth();
            }
        });
        if (centers.length > 0) {
            var indent = 3;
            var totalLen = centerLen + indent * (centers.length - 1);
            var start = $(buttonBox).innerWidth() / 2 - totalLen / 2;

            for (var c = 0; c < centers.length; c++) {
                $(centers[c]).css('left', start + "px").css('position', 'absolute');
                start += $(centers[c]).outerWidth() + indent;
            }
        }
    }
}
//ColorPicker.java
$RWT.picker = {
    install: function (input) {
        $RWT.colorpicker.install(input);
    },
    processColorChange: function (event) {
        $RWT.actions.event(this, 'change', this.value);
    }
}

$RWT.gridBoxContainer = {
    layout: function(obj) {
        var jsonStringFixed = $(obj).children().attr('fixedcolls');
        if (jsonStringFixed) {
            var map = JSON.parse(jsonStringFixed);
            $RWT.gridBoxContainer.iterateRows(document.getElementById($(obj).children().attr('id')), function(tr) {
                Object.keys(map).forEach(function(key) { 
                    var cell = $(tr).children()[key];
                    if ($(cell).attr('colspan') == '1' && $(cell).children() != null) {
                        $(cell).css('width', map[key]);
                        $($(cell).children()).css('min-width', map[key]);
                        var cellObject = $($(cell).children()).children();
                        if ($(cellObject).css('max-width') == 'none' || $(cellObject).css('max-width') == null || parseInt($(cellObject).css('max-width')) > parseInt(map[key])) {
                            $(cellObject).css('max-width', map[key]);
                        }
                    }
                });
            });
            obj.calcAdjustSize = function() {
                var width, height;
                var dt = WpsUtils.findChildByLocalName(obj, 'table');
                if (dt) {
                    if ($(dt.parentNode).attr('isAdjustWidth') == 'true') {
                        width = $(dt).width() - $(dt.parentNode).width();
                    }
                    if ($(dt.parentNode).attr('isAdjustHeight') == 'true') {
                        height = $(dt).height() - $(dt.parentNode).height();
                    }
                }
                return {"width" : (width > 0 ? width : 0), "height" : (height > 0 ? height : 0), "final" : true};
            }   
        }
        
        var expandCol = $(obj).children().attr("expandedcol");
        if (expandCol != null) {
        $RWT.gridLayout._iterateRows(document.getElementById($(obj).children().attr('id')), function(tr) {
            var cell = $(tr).children()[expandCol];
            if ($(cell).attr('colspan') == '1' && $(cell).children() != null) {
                $(cell).css('width', "100%");
            }
        });
        }
        
        var expandRow = $(obj).children().attr("expandedrow");
        if (expandRow != null) {
            var tr = $(obj).children().children().children()[expandRow];
            $(tr).children().each(function() {
                if ($(this).attr("rowspan") == 1 && $(this).children() != null) {
                    $(this).css("height", "100%");
                    $(this).children().first().css("height", "100%");
                }
            });
        }
    },
    iterateRows: function(dt, f) {
        var b = WpsUtils.findChildByLocalName(dt, 'tbody');
        if (b) {
            if (WpsUtils.iterateNames(b, 'tr', f) === true) {
                return;
            }
        }
    }
}

$RWT.messageBox = {
    layout: function(dialog, rr_rs, rqId) {
        if (dialog.layout_complete == null) {
            dialog.layout_complete = true;
            var outerBody;
            WpsUtils.iterateAll(dialog, function(c) {
                if ($(c).attr('role') == 'outerBody') {
                    outerBody = c;
                }
            });        
            var gridBoxContainer = $(outerBody).find(".gridBox")[0];
            var table = $(gridBoxContainer).children()[0];
            $(table).parent().removeClass("gridBox");
            _$RWT_DLG_OBJ._layout(dialog, rr_rs, rqId);
            //Задание диалогу максимально допустимых размеров и вычисление оптимальных размеров таблицы
            var innerBody = $(outerBody).children()[0];
            var windowSize = WpsUtils.getBrowserWindowSize();
            var maxWidth = windowSize.width * 0.66;
            var maxHeight = windowSize.height * 0.66;
            $(dialog).height(maxHeight);
            $(dialog).width(maxWidth);       
            _$RWT_DLG_OBJ._afterLayout(dialog, rqId); 
            _$RWT_DLG_OBJ._layout(dialog, rr_rs, rqId);
            $(gridBoxContainer).css("width", "100%");
            $(gridBoxContainer).css("height", "100%");
            $(gridBoxContainer).css("min-width", $(table).outerWidth() + 1); 
            $(gridBoxContainer).css("min-height", $(table).outerHeight() + 1);
            //Уменьшение диалога и корректировка его размеров
            $(dialog).width(200);
            $(dialog).height(110);
            _$RWT_DLG_OBJ._layout(dialog, rr_rs, rqId);
            var innerBodyDiffWidth = $(gridBoxContainer).width() - $(innerBody).width() + 1;
            var isMaxVertical = false;
            var isMaxHorizontal = false;
            if ($(dialog).width() + innerBodyDiffWidth > maxWidth) {
                innerBodyDiffWidth = maxWidth - $(dialog).width();
                $(innerBody).css("overflow", "auto");
                isMaxHorizontal = true;
            }
            var innerBodyDiffHeight = $(gridBoxContainer).height() - $(innerBody).height() + 2;
            if ($(dialog).height() + innerBodyDiffHeight > maxHeight) {
                innerBodyDiffHeight = maxHeight - $(dialog).height();
                $(innerBody).css("overflow", "auto")
                isMaxVertical = true;
            }
            
            var w = $(dialog).width(); 
            if (innerBodyDiffWidth > 0 ) {
                if (isMaxVertical) {
                    if ($(dialog).width() + innerBodyDiffWidth + WpsUtils.getScrollbarWidth() > maxWidth) {
                        $(dialog).width(maxWidth);
                        isMaxHorizontal = true;
                    } else {
                        $(dialog).width($(dialog).width() + innerBodyDiffWidth + WpsUtils.getScrollbarWidth());
                    }
                } else {
                    $(dialog).width(w + innerBodyDiffWidth);
                }
            } 
            if (innerBodyDiffHeight > 0) { 
                if (isMaxHorizontal) {
                    if ($(dialog).height() + innerBodyDiffHeight + WpsUtils.getScrollbarHeight() > maxHeight) {
                        $(dialog).height(maxHeight);
                    } else {
                        $(dialog).height($(dialog).height() + innerBodyDiffHeight + WpsUtils.getScrollbarHeight() + 1);
                    }
                } else {
                    $(dialog).height($(dialog).height() + innerBodyDiffHeight);
                }
            }
            $(gridBoxContainer).height($(table).height() + 1);
            $(gridBoxContainer).width($(table).width() + 1);
            _$RWT_DLG_OBJ._afterLayout(dialog, rqId); 
            _$RWT_DLG_OBJ._layout(dialog, rr_rs, rqId); //afterLayout не вызовет _layout диалога так как у него задан layout $RWT.messageBox.layout и вызовется именно он
            $(innerBody).get(0).style.overflow = "hidden"; //fix for Google Chrome bag with not updating scrolls
            setTimeout(function(){
                $(innerBody).get(0).style.overflow = "auto";    
            },0); 
            $(dialog).css('left', windowSize.width / 2 - $(dialog).outerWidth() / 2).css('top', windowSize.height / 2 - $(dialog).outerHeight() / 2);
            dialog.rwt_rnd_refine = null;
        }
    }
}

$RWT.gridLayout = {//common layout methods for $RWT.grid (grid.js) and $RWT.tree (tree.js)
    doLayout: function (grid, stickToRight, widthSetter) {
        var cts = $RWT.gridLayout.splitToComponents(grid);
        var ml = $RWT.gridLayout._getTreeMaxLevel(cts);
        var hc = $RWT.gridLayout._getHHeaderCells(cts.h, ml);
        grid.widthSetter = widthSetter;
        $RWT.gridLayout._setupHHeaderHeight(grid, cts, hc);
        var dt = $RWT.gridLayout.getDataTable(cts.b);
        var stretchIdx = $RWT.gridLayout._detectSizePolicy(grid, hc, stickToRight);
        cts.h.stretchIdx = stretchIdx;
        $RWT.gridLayout._setupHeaderBorders(hc, stretchIdx < 0);
        $RWT.gridLayout._calcContentWidths(grid, dt, hc);
        if (!grid.rwt_inited) {
            grid.rwt_inited = $RWT.gridLayout._doInitialColumnsLayout(grid, cts, dt, hc) > 0;
        } else {
            $RWT.gridLayout._repeatColumnsLayout(grid, cts, dt, hc);
            if (grid.sizePolicyChanged==true){
                $RWT.gridLayout._sendResizeEvent(cts, hc, -1);
            }
        }
        var horizontalScrolling;
        if ( dt.clientWidth - cts.b.clientWidth <= 1) {
            $(cts.b).css('overflow-x', 'hidden');
            $(cts.b).css('overflow-y', 'auto');
            horizontalScrolling = false;
        } else {
            $(cts.b).css('overflow', 'auto');
            horizontalScrolling = true;
        }
        $RWT.gridLayout._setupFocusFrame(cts.b);
        var editorId = grid.getAttribute('editor');
        if (editorId != null) {
            var editor = $('#' + editorId);
            if (editor != null && editor.length > 0) {
                editor.width($(editor[0].parentNode).innerWidth());
                $RWT._layoutNode(editor[0]);
            }
        }
        $RWT.gridLayout._setupVerticalHeaderHeight(grid, cts, horizontalScrolling);
        grid.calcAdjustSize = function() {
            var width, height;
            if (dt) {
                if ($(dt.parentNode).attr('isAdjustWidth') == 'true') {
                    width = $(dt).width() - $(dt.parentNode).width();
                }
                if ($(dt.parentNode).attr('isAdjustHeight') == 'true') {
                    height = $(dt).height() - $(dt.parentNode).height();
                }
            }
            return {"width" : (width > 0 ? width : 0), "height" : (height > 0 ? height : 0), "final" : true};
        }
        var editor = $RWT.gridLayout._findFilterEditor(grid);
        if (editor!=null){
            $RWT.gridLayout._initFilterEditor(editor, grid);
        } 
        if ($(grid).attr('scrolledToNode') != null) {
                var scrolledToNode = document.getElementById($(grid).attr('scrolledToNode'));
                var forward = $(grid).attr('isForward');
                scrolledToNode.scrollIntoView(forward==='false');
                $(grid).removeAttr("scrolledToNode");
                $(grid).removeAttr("forward");
        }
    },
    splitToComponents: function (object) {
        var v, h, b;
        var classifyComponentInOuterContainer = function (c) {
            if ($(c).hasClass('rwt-grid-header-panel header')) {
                h = c;
                return false;
            } else if ($(c).hasClass('rwt-grid-data-panel')) {
                b = c;
                return false;
            } else {
                return true;
            }
        };
        WpsUtils.iterateNames(object, 'div', function (c) {
            if ($(c).hasClass('rwt-grid-vertical-header-panel')) {
                v = c;
            } else if ($(c).hasClass('rwt-grid-outer-container')) {
                WpsUtils.iterateNames(c, 'div', classifyComponentInOuterContainer);
                return true;
            }
            return false;
        });
        return {
            'v': v,
            'h': h,
            'b': b
        };
    },
    getDataTable: function (b) {//get data table
        return WpsUtils.findChildByLocalName(b, 'table');
    },
    _getHeaderTable: function(h){
        return WpsUtils.findChildByLocalName(h, 'table');
    },
    _setupFocusFrame: function (bodyContainer) {
        var zIndex = WpsUtils.getNextZIndex(bodyContainer);
        var rowFrame = $RWT.gridLayout._parseFrame(bodyContainer, bodyContainer.getAttribute('rowFrame'));
        if (rowFrame != null) {
            rowFrame.zIndex = zIndex - 1;
        }
        var cellFrame = $RWT.gridLayout._parseFrame(bodyContainer, bodyContainer.getAttribute('cellFrame'));
        if (cellFrame != null) {
            cellFrame.zIndex = zIndex;
        }
        $RWT.gridLayout._drawFocusFrames(rowFrame, cellFrame);
        bodyContainer.rowFrame = rowFrame;
        bodyContainer.cellFrame = cellFrame;
    },
    _parseFrame: function (bodyContainer, frameAttrValue) {
        if (frameAttrValue != null) {
            var frame;
            try {
                frame = JSON.parse(frameAttrValue);
            } catch (e) {
                WpsUtils.printStackTrace(e);
                return null;
            }
            var leftLine = $RWT.gridLayout._findFrameLine(bodyContainer, frame.left);
            var rightLine = $RWT.gridLayout._findFrameLine(bodyContainer, frame.right);
            var topLine = $RWT.gridLayout._findFrameLine(bodyContainer, frame.top);
            var bottomLine = $RWT.gridLayout._findFrameLine(bodyContainer, frame.bottom);
            if (leftLine == null || rightLine == null || topLine == null || bottomLine == null) {
                return null;
            }
            frame.left = $(leftLine);
            frame.right = $(rightLine);
            frame.top = $(topLine);
            frame.bottom = $(bottomLine);
            var dt = $RWT.gridLayout.getDataTable(bodyContainer);
            var row = frame.row === 'null' ? null : $RWT.gridLayout._findRowById(dt, frame.row);
            frame.row = row;
            if (row == null) {
                frame.cell = null;
            } else {
                var cellIndex = parseInt(frame.cell);
                if (isNaN(cellIndex) || cellIndex < 0) {
                    frame.cell = null;
                } else {
                    frame.cell = $RWT.gridLayout._getCellByIndex(frame.row, cellIndex);
                }
            }
            return frame;
        }
        return null;
    },
    _findFrameLine: function (bodyContainer, frameLineId) {
        var frameLine = null;
        WpsUtils.iterateNames(bodyContainer, 'hr', function (element) {
            if (element.getAttribute('id') === frameLineId) {
                frameLine = element;
                return true;
            }
        });
        return frameLine;
    },
    _drawFocusFrames: function (rowFrame, cellFrame) {
        if (rowFrame != null) {
            $RWT.gridLayout._setFocusFrameToElement(rowFrame.row, rowFrame);
        }
        if (cellFrame != null) {
            $RWT.gridLayout._setFocusFrameToElement(cellFrame.cell, cellFrame);
        }
    },
    _setFocusFrameToElement: function (element, frame) {
        if (element == null) {
            frame.left.css('display', 'none');
            frame.right.css('display', 'none');
            frame.top.css('display', 'none');
            frame.bottom.css('display', 'none');
        } else {
            var geometry = $RWT.gridLayout._getGeometry(element);
            geometry.left += frame.offsetLeft;
            geometry.h -= parseInt(frame.top.css('border-top-width'));
            //geometry.h -= parseInt(frame.bottom.css('border-bottom-width'));
            geometry.w -= parseInt(frame.left.css('border-left-width'));
            if ($RWT.BrowserDetect.browser == 'Firefox') {
                geometry.w -= parseInt(frame.right.css('border-right-width'));
            }
            geometry.w -= frame.offsetLeft;
            $RWT.gridLayout._setFrameGeometry(frame, geometry);
            $RWT.gridLayout._setupFrameLine(frame.left, frame.zIndex);
            $RWT.gridLayout._setupFrameLine(frame.right, frame.zIndex);
            $RWT.gridLayout._setupFrameLine(frame.top, frame.zIndex);
            $RWT.gridLayout._setupFrameLine(frame.bottom, frame.zIndex);
        }
    },
    _setupFrameLine: function (frameLine, zIndex) {
        frameLine.css('display', '').css('zIndex', zIndex).css('position', 'absolute');
    },
    _getGeometry: function (element) {
        return {
            w: element.offsetWidth,
            h: element.offsetHeight,
            left: element.offsetLeft,
            top: element.offsetTop
        };
    },
    _setFrameGeometry: function (frame, geometry) {
        frame.left.css('left', geometry.left + 'px')
                .css('top', geometry.top + 'px')
                .css('height', geometry.h + 'px');
        frame.right.css('left', (geometry.left + geometry.w) + 'px')
                .css('top', geometry.top + 'px')
                .css('height', geometry.h + 'px');
        frame.top.css('left', geometry.left + 'px')
                .css('top', geometry.top + 'px')
                .css('width', geometry.w + 'px');
        frame.bottom.css('left', geometry.left + 'px')
                .css('top', (geometry.h + geometry.top) + 'px')
                .css('width', geometry.w + 'px');
    },
    _getHHeaderCells: function (header, firstColumnAddintionalWidth) {
        var hr = $RWT.gridLayout._getHeadRow(header);
        var cells = {};
        cells.count = 0;
        $RWT.gridLayout._iterateRowCells(hr, function (td) {
            cells[cells.count] = td; //WpsUtils.findChildByLocalName(td,'div');            
            cells.count++;
            if (cells.count == 1 && firstColumnAddintionalWidth != null) {
                td.minWidth = $RWT.gridLayout._minSize(td) + firstColumnAddintionalWidth;
            } else {
                td.minWidth = $RWT.gridLayout._minSize(td);
            }
        });
        return cells;
    },
    _getVHeaderCells: function (header) {
        var cells = {};
        cells.count = 0;
        var cornerCell = WpsUtils.findFirstChild(header);
        var tableContainer = cornerCell == null ? null : WpsUtils.findNextSibling(cornerCell);
        if (tableContainer) {
            var table = WpsUtils.findChildByLocalName(tableContainer, 'table');
            if (table) {
                var t = WpsUtils.findChildByLocalName(table, 'thead');
                WpsUtils.iterateNames(t, 'tr', function (tr) {
                    var td = WpsUtils.findChildByLocalName(tr, 'td');
                    cells[cells.count] = td;
                    cells.count++;
                    return false;
                });
            }
        }
        return cells;
    },
    _getVHeaderRows: function (header) {
        var rows = {};
        rows.count = 0;
        var cornerCell = WpsUtils.findFirstChild(header);
        var tableContainer = cornerCell == null ? null : WpsUtils.findNextSibling(cornerCell);
        if (tableContainer) {
            var table = WpsUtils.findChildByLocalName(tableContainer, 'table');
            if (table) {
                var t = WpsUtils.findChildByLocalName(table, 'thead');
                WpsUtils.iterateNames(t, 'tr', function (tr) {                    
                    rows[rows.count] = tr;
                    rows.count++;
                    return false;
                });
            }
        }
        return rows;
    },    
    _getCornerCell: function (verticalHeader) {
        return WpsUtils.findChildByLocalName(verticalHeader, 'div');
    },
    _getInitialWidth: function (td) {
        var fw = $(td).attr('rwt_initialWidth');
        return fw == null ? null : parseInt(fw, 10);
    },
    _detectSizePolicy: function (grid, hc, stickToRight) {
        var i;
        var lastIdx = -1;
        var stretchIdx = -1;
        var td;
        grid.sizePolicyChanged = false;
        for (i = 0; i < hc.count; i++) {
            td = hc[i];
            if ('stretch' === td.sizePolicy || 'weak_stretch' === td.sizePolicy ) {
                if (td.wasManualResize === true){
                    td.wasManualResize = false;
                }else{
                    td.columnWidth = null; //drop width that was calculated automatically
                }
            }
            if (td.fixedWidth == null) {
                var prevSizePolicy = td.sizePolicy;
                td.sizePolicy = $(td).attr('policySize');
                if (prevSizePolicy!=td.sizePolicy){
                    if ('manual' === prevSizePolicy){
                        td.columnWidth = null;
                    }
                    grid.sizePolicyChanged = true;
                }
                if ('stretch' === td.sizePolicy || 'weak_stretch' === td.sizePolicy) {
                    stretchIdx = i;
                }
                if ('content' !== td.sizePolicy || 'weak_content' !== td.sizePolicy ) {
                    lastIdx = i;
                }
            } else {
                td.sizePolicy = 'fixed';
            }
        }
        if (stickToRight && stretchIdx < 0 && lastIdx >= 0) {
            stretchIdx = lastIdx;
            hc[stretchIdx].sizePolicy = 'stretch';
        }
        if (stretchIdx >= 0) {
            hc[stretchIdx].columnWidth = null;
        }
        return stretchIdx;
    },
    _setupHHeaderHeight: function (grid, cts, hc) {
        var headerCellTitle;
        var headerCellHeight;
        var maxHeaderCellHeight = -1;
        for (var i = 0; i < hc.count; i++) {
            headerCellTitle = $RWT.gridLayout._getHeaderCellTitle(hc[i]);
            headerCellHeight = $(headerCellTitle).outerHeight();
            if (headerCellHeight > maxHeaderCellHeight) {
                maxHeaderCellHeight = headerCellHeight;
            }
        }
        var headerHeight = maxHeaderCellHeight + 6;
        var cc;
        for (var i = 0; i < hc.count; i++) {
            cc = $($RWT.gridLayout._getHeaderCellComponent(hc[i]));
            $(cc).height(headerHeight);
        }
        var h = $(grid).innerHeight();
        if ($(cts.h).css('display') != 'none') {
            $(cts.h).height(headerHeight);
            $(cts.b).height(h - $(cts.h).outerHeight() - 2);
        } else {
            $(cts.h).height(0);
            $(cts.b).height(h - 2);
        }
        if (cts.v != null) {
            var cornerCell = $RWT.gridLayout._getCornerCell(cts.v);
            var padding = parseInt($(cornerCell).css('padding-top'));
            $(cornerCell).height(headerHeight - padding);
        }
    },
    _setupVerticalHeaderHeight: function (grid, cts, horizontalScrolling) {
        if (cts.v != null) {
            //setup vertical header height
            if (horizontalScrolling && (cts.b.scrollWidth > cts.b.clientWidth)) {//horizontal scrollbar is visible
                $(cts.v).height(grid.clientHeight - WpsUtils.getScrollbarHeight() - cts.v.offsetTop);
            } else {
                $(cts.v).height(grid.clientHeight - cts.v.offsetTop);
            }
        }
    },
    _setupHeaderBorders: function (hc, isLastColumnHasRightBorder) {
        var lastIdx = hc.count - 1;
        var cell;
        var td;
        var prevColumnSizePolicy;
        var resizeColumnIndex;
        var prevResizeColumnIndex;
        for (var i = 0; i < hc.count; i++) {
            td = hc[i];
            cell = $RWT.gridLayout._getHeaderCellComponent(td);
            resizeColumnIndex = cell.getAttribute('resizingColumnIdx') == null ? -1 : parseInt(cell.getAttribute('resizingColumnIdx'));
            if (td.borderWidth == null) {
                td.borderWidth = $(td).outerWidth() - $(td).innerWidth();
            }
            var leftSizer = $RWT.gridLayout._getHeaderCellLeftSizer(td);
            if (leftSizer) {
                if (i == 0) {
                    $(leftSizer).css('display', 'none');
                } else {
                    $(leftSizer).css('display', 'fixed' === prevColumnSizePolicy ? 'none' : '');
                    leftSizer.resizeColumnIndex = prevResizeColumnIndex;
                }
            }
            var rightSizer = $RWT.gridLayout._getHeaderCellRightSizer(td);
            if (i == lastIdx && !isLastColumnHasRightBorder) {
                if (rightSizer) {
                    $(rightSizer).css('display', 'none');
                }
                $(td).css('border-right', 'none');
            } else {
                if (rightSizer) {
                    $(rightSizer).css('display', 'fixed' === td.sizePolicy ? 'none' : '');
                    rightSizer.resizeColumnIndex = resizeColumnIndex;
                }
                $(td).css('border-right', '');
            }
            prevColumnSizePolicy = td.sizePolicy;
            prevResizeColumnIndex = resizeColumnIndex;
        }
    },
    _calcStretchColumnWidth: function (cts, hc) {
        var stretchIdx = cts.h.stretchIdx;
        if (stretchIdx >= 0) {
            var td = hc[stretchIdx];
            var autoSizeCell = $RWT.gridLayout._getHeaderCellComponent(td);
            var w = cts.b.clientWidth;
            if ($RWT.BrowserDetect.browser == 'Firefox') {
                w -= 1;
            }
            for (var i=0,c=hc.count; i<c; i++){
                if (i!=stretchIdx){
                    var headerCell = hc[i];
                    if ($(headerCell).css('display')!=='none' && headerCell.columnWidth!=null){
                        w -= headerCell.columnWidth;
                    }
                }
            }
            
            td.columnWidth = Math.max(w, td.minWidth);
            $(autoSizeCell).width(td.columnWidth - td.borderWidth);
        }
    },
    _calcContentWidths: function (grid, dt, hc) {
        for (var i = 0; i < hc.count; i++) {
            hc[i].contentWidth = hc[i].minWidth;
        }
        var firstRow;
        var widthSetter = widthSetter == null ? $RWT.gridLayout._defaultCellWidthSetter : widthSetter;
        var tbody = WpsUtils.findFirstChild(dt);
        if (tbody != null) {
            WpsUtils.iterateNames(tbody, 'tr', function (tr) {
                if ($(tr).css('display') == 'none') {
                    return;
                }
                if ($RWT.gridLayout._isSpannedRow(tr)) {
                    return;
                }
                var i = 0;
                $RWT.gridLayout._iterateRowCells(tr, function (td) {
                    var byContent = 'content' === hc[i].sizePolicy || 'weak_content' === hc[i].sizePolicy;
                    if (byContent && td.wasResized != null && td.wasResized) {
                        widthSetter(grid, i, td, null);                        
                        td.wasResized = false;
                    }
                    var content = WpsUtils.findChildByLocalName(td, 'div');
                    if (content != null) {
                        var innerContent = WpsUtils.findFirstChild(content);
                        if (innerContent!=null && innerContent.getAttribute('nolayout')!='true'){
                            $RWT._layoutNode(content);
                        }
                    }
                    i++;
                });
                if (firstRow == null) {
                    firstRow = tr;
                }
            });
        }
        if (firstRow != null) {
            var i = 0;
            var zoomFactor =WpsUtils.getZoomFactor();
            var isBordered = $RWT.gridLayout._isDataTableBordered(dt);
            var zoomEnabled = zoomFactor==null || zoomFactor <= 0.999 || zoomFactor >= 1.001;
            var invalidateSizeCache = false;       
            if (zoomEnabled && (zoomFactor==null || grid.lastDprWidth!=zoomFactor)){
                invalidateSizeCache= true;
                grid.lastDprWidth = zoomFactor;
            }
            $RWT.gridLayout._iterateRowCells(firstRow, function (td) {                
                if ('content' === hc[i].sizePolicy || 'weak_content' === hc[i].sizePolicy) {
                    var columnWidth = td.clientWidth;
                    if (zoomEnabled || $RWT.BrowserDetect.browser ==  'Explorer'){
                        invalidateSizeCache = invalidateSizeCache || td.cachedWidth==null || td.widthCacheKey!=columnWidth;
                        if (invalidateSizeCache){
                            //calc exact cell width (it may be non-integer)
                            hc[i].contentWidth = parseFloat(window.getComputedStyle(td).width) + (isBordered ? hc[i].borderWidth : 0);
                            td.widthCacheKey = columnWidth;
                            td.cachedWidth = hc[i].contentWidth;
                        }else{
                            hc[i].contentWidth = td.cachedWidth;
                        }
                    }else{
                        hc[i].contentWidth = columnWidth + (isBordered ? hc[i].borderWidth : 0);
                    }
                } else {
                    hc[i].contentWidth = $(td).outerWidth();
                }
                i++;
            });
        }
    },
    _defaultCellWidthSetter: function (grid, columnIndex, cell, width) {
        var content = WpsUtils.findChildByLocalName(cell, 'div');
        if (width == null) {
            $(content).width('100%');
        } else {
            $(content).width(width);
        }
    },
    _applyColumnsWidthToContent: function (grid, table, cts, hc) {
        var isBordered = $RWT.gridLayout._isDataTableBordered(table);
        var rowCount = 0;
        var width;
        var widthSetter = grid.widthSetter == null ? $RWT.gridLayout._defaultCellWidthSetter : grid.widthSetter;        
        var vHeaderRows = cts.v == null ? null : $RWT.gridLayout._getVHeaderRows(cts.v);
        var verticalHeaderVisible = vHeaderRows != null;
        $RWT.gridLayout._iterateRows(table, function (tr) {
            var i = 0;
            var spanned = $RWT.gridLayout._isSpannedRow(tr);
            $RWT.gridLayout._iterateRowCells(tr, function (td) {
                if (isBordered) {
                    $(td).addClass("rwt-grid-row-cell");
                }
                else {
                    $(td).removeClass("rwt-grid-row-cell");
                }
                if (spanned) {
                    var cw = 0;
                    for (var j = 0; j < hc.count; j++) {
                        cw += hc[i + j].columnWidth - (isBordered ? hc[i + j].borderWidth : 0);
                    }
                    var cc = WpsUtils.findChildByLocalName(td, 'div');
                    $(cc).width(cw);
                }
                else {
                    var byContent = 'content' === hc[i].sizePolicy || 'weak_content' === hc[i].sizePolicy;
                    if (byContent && hc[i].columnWidth == hc[i].contentWidth) {
                        if (td.wasResized != null && td.wasResized) {
                            widthSetter(grid, i, td, null);
                            td.wasResized = false;
                            td.lastSettedWidth = null;
                        }
                    } else {
                        width = hc[i].columnWidth - (isBordered ? hc[i].borderWidth : 0);
                        if (td.lastSettedWidth!=width){
                            widthSetter(grid, i, td, width);
                            td.lastSettedWidth = width;
                        }
                        td.wasResized = !byContent;
                    }
                }
                i++;
            });
            if (i>0){
                if (verticalHeaderVisible && vHeaderRows.count > rowCount) {
                    var headerRow = vHeaderRows[rowCount];
                    if (tr.clientHeight!=headerRow.lastRowHeight){
                        var rowHeight = $(tr).outerHeight();
                        $(tr).height(rowHeight);
                        $(headerRow).height(rowHeight);
                        headerRow.lastRowHeight = tr.clientHeight;
                    }
                }
                rowCount++;
            }
        });
        return rowCount;
    },
    _initHeaderCell: function (grid, hc) {
        var leftSizer = $RWT.gridLayout._getHeaderCellLeftSizer(hc);
        if (leftSizer != null) {
            $RWT.gridLayout._initSizer(grid, leftSizer);
        }
        var rightSizer = $RWT.gridLayout._getHeaderCellRightSizer(hc);
        if (rightSizer != null) {
            $RWT.gridLayout._initSizer(grid, rightSizer);
        }
        hc.inited = true;
    },
    _initSizer: function (grid, sizer) {
        if (!sizer.inited) {
            sizer.grid = grid;            
            $(sizer).draggable({
                axis: ("x"),
                delay: 200,
                distance: 1,
                containment: grid,
                appendTo: grid,
                zIndex: 3,
                helper: "clone",
                start: function (e, ui) {
                    if ("true" === $(this).attr('resizable')) {
                        this.rwt_sp = ui.position.left;
                        $(ui.helper).css('background-color', 'black');
                        $(ui.helper).css('height', $(this.grid).innerHeight() + 'px');
                        $(ui.helper).css('float', '');
                        $(ui.helper).css('top', '0');
                    }
                },
                stop: function (e, ui) {
                    if ("true" === $(this).attr('resizable')) {
                        var increment = ui.position.left - this.rwt_sp;
                        var grid = this.grid;
                        var cts = $RWT.gridLayout.splitToComponents(grid);
                        var ml = $RWT.gridLayout._getTreeMaxLevel(cts);
                        var hc = $RWT.gridLayout._getHHeaderCells(cts.h, ml);
                        var resizeColIndex = this.resizeColumnIndex;
                        if (resizeColIndex > -1 && resizeColIndex < hc.count) {
                            var resizingColumn = hc[resizeColIndex];
                            var sign = cts.h.stretchIdx > -1 && resizeColIndex > cts.h.stretchIdx ? -1 : 1;
                            var newSize = $(resizingColumn).outerWidth() + sign * increment;
                            resizingColumn.columnWidth = Math.max(newSize, resizingColumn.minWidth);
                            var cell = $RWT.gridLayout._getHeaderCellComponent(resizingColumn);
                            $(cell).width(resizingColumn.columnWidth - resizingColumn.borderWidth);
                            cell.parentNode.wasManualResize = true;
                            $RWT.gridLayout._afterResizeColumn(grid, cts, hc, resizeColIndex);
                        }
                    }
                }
            });
            $(sizer).dblclick(function(){
                var grid = this.grid;
                var cts = $RWT.gridLayout.splitToComponents(grid);
                var ml = $RWT.gridLayout._getTreeMaxLevel(cts);
                var hc = $RWT.gridLayout._getHHeaderCells(cts.h, ml);
                var resizeColIndex = this.resizeColumnIndex;
                if (resizeColIndex > -1 && resizeColIndex < hc.count) {
                    var resizingColumn = hc[resizeColIndex];
                    var canResizeByContent = resizingColumn.getAttribute('canresizebycontent');
                    if ('true' === canResizeByContent){
                        $RWT.actions.event(resizingColumn, 'resizeByContent', null);  
                    }
                }
            });
            $(sizer).click(function(e){
                if (e.stopPropagation)
                    e.stopPropagation();                
            });
            sizer.inited = true;
        }
    },
    _doInitialColumnsLayout: function (grid, cts, dt, hc) {
        var td;
        var cell;
        for (var i = 0; i < hc.count; i++) {
            td = hc[i];
            if (!td.inited) {
                $RWT.gridLayout._initHeaderCell(grid, td);
            }
            if ('content' === td.sizePolicy || 'weak_content' === td.sizePolicy) {
                td.columnWidth = Math.max(td.contentWidth, td.minWidth);
            } else {
                var fs = $RWT.gridLayout._getFixedWidth(td);
                var is;
                if (fs == null) {
                    is = $RWT.gridLayout._getInitialWidth(td);
                    if (is == null) {
                        is = 100;
                    }
                } else {
                    is = fs;
                }
                var ms = td.minWidth == null ? 0 : td.minWidth;
                td.columnWidth = Math.max(is, ms);
            }
            cell = WpsUtils.findChildByLocalName(td, 'div');
            $(cell).width(td.columnWidth - td.borderWidth);
        }
        $RWT.gridLayout._calcStretchColumnWidth(cts, hc);
        var rowCount = $RWT.gridLayout._applyColumnsWidthToContent(grid, dt, cts, hc);
        return rowCount;
    },
    _repeatColumnsLayout: function (grid, cts, dt, hc) {
        for (var i = 0; i < hc.count; i++) {
            var td = hc[i];
            var cell = $RWT.gridLayout._getHeaderCellComponent(td);
            var byContent = 'content' === td.sizePolicy || 'weak_content' === td.sizePolicy;
            var stretch = 'stretch' === td.sizePolicy || 'weak_stretch' === td.sizePolicy;
            var prevWidth = td.columnWidth;
            if (byContent) {
                if (prevWidth) {
                    td.columnWidth = Math.max(prevWidth, td.contentWidth);
                } else {
                    td.columnWidth = Math.max(td.contentWidth, td.minWidth);
                }
            } else {
                if (td.fixedWidth == null) {
                    if (td.inited) {
                        if (prevWidth == null && !stretch) {
                            td.columnWidth = td.minWidth;
                        }
                    } else {
                        $RWT.gridLayout._initHeaderCell(grid, td);
                        td.columnWidth = td.minWidth;
                    }
                } else {
                    td.columnWidth = td.fixedWidth;
                }
            }
            if (!byContent) {
                if (td.columnWidth < td.minWidth) {
                    td.columnWidth = td.minWidth;
                }
            }
            $(cell).width(td.columnWidth - td.borderWidth);
        }
        $RWT.gridLayout._calcStretchColumnWidth(cts, hc);
        $RWT.gridLayout._applyColumnsWidthToContent(grid, dt, cts, hc);
    },
    _getHeadRow: function (h) {//get header row
        var table = WpsUtils.findChildByLocalName(h, 'table');
        if (table) {
            var t = WpsUtils.findChildByLocalName(table, 'thead');
            if (t == null) {
                t = WpsUtils.findChildByLocalName(table, 'tbody');
            }
            return t == null ? null : WpsUtils.findChildByLocalName(t, 'tr');
        }
        return null;
    },
    _getFixedWidth: function (td) {
        var fw = $(td).attr('rwt_fixedWidth');
        return fw == null ? null : parseInt(fw, 10);
    },
    _minSize: function (cell) {
        var fw = $RWT.gridLayout._getFixedWidth(cell);
        if (fw != null) {
            cell.fixedWidth = fw;
            return fw;
        } else {
            cell.fixedWidth = null;
            var title = $RWT.gridLayout._getHeaderCellTitle(cell);
            var titleWidth;
            if (title == null) {
                titleWidth = 0;
            } else {
                //Width of label can be changed when changing width of outer cell. So cache it here
                if (title.prevWidth == null || title.prevText !== title.textContent) {
                    titleWidth = $(title).outerWidth();
                    title.prevWidth = titleWidth;
                    title.prevText = title.textContent;
                } else {
                    titleWidth = title.prevWidth;
                }
            }
            var cellContent = WpsUtils.findChildByLocalName(cell, 'div');
            var cellWidth = titleWidth;
            if (cellContent!=null){
                WpsUtils.iterateAll(cellContent, function (n) {
                    if (n!=title && $(n)!=null){
                        cellWidth+=$(n).outerWidth();
                    }
                });        
            }
            //var sizer = $RWT.gridLayout._getHeaderCellRightSizer(cell);            
            //return titleWidth + $(sizer).outerWidth() * 2 + 20;
            return cellWidth + 20;
        }
    },
    _getHeaderCellTitle: function (td) {
        var div = WpsUtils.findChildByLocalName(td, 'div');
        if (div)
            return WpsUtils.findChildByLocalName(div, 'label');
        else
            return null;
    },
    _getHeaderCellRightSizer: function (td) {
        var div = WpsUtils.findChildByLocalName(td, 'div');
        if (div)
            return WpsUtils.findChildByClassName(div, 'header-right-handle');
        else
            return null;
    },
    _getHeaderCellLeftSizer: function (td) {
        var div = WpsUtils.findChildByLocalName(td, 'div');
        if (div)
            return WpsUtils.findChildByClassName(div, 'header-left-handle');
        else
            return null;
    },
    _getHeaderCellComponent: function (td) {
        return WpsUtils.findChildByLocalName(td, 'div');
    },
    _getDataBody: function (dt) {
        return WpsUtils.findChildByLocalName(dt, 'tbody');
    },
    _isDataTableBordered: function (dt) {
        var attr = $(dt).attr("showborder");
        return attr != null && 'true' === attr;
    },
    _iterateRows: function (dt, f) {
        var b = $RWT.gridLayout._getDataBody(dt);
        if (b) {
            if (WpsUtils.iterateNames(b, 'tr', function(tr) {
                return $(tr).css('display') == 'none' ? false : f(tr);
            }) === true) {
                return;
            }
        }        
    },
    _iterateRowCells: function (tr, f) {
        WpsUtils.iterateNames(tr, 'td', function (td) {
            return $(td).css('display') == 'none' ? false : f(td);
        });
    },
    _findRowById: function (dt, id) {
        var row;
        $RWT.gridLayout._iterateRows(dt, function (tr) {
            if (tr.getAttribute('id') === id) {
                row = tr;
                return true;
            }
        });
        return row;
    },
    _getCellByIndex: function (tr, index) {
        var i = 0;
        var cell;
        $RWT.gridLayout._iterateRowCells(tr, function (td) {
            if (i === index) {
                cell = td;
                return true;
            }
            i++;
        });
        return cell;
    },
    _isSpannedRow: function (tr) {
        var firstCell = WpsUtils.findFirstChild(tr);
        return firstCell != null && firstCell.getAttribute('colSpan') != null;
    },
    _afterResizeColumn: function (grid, cts, hc, resizeColIndex) {
        var dt = $RWT.gridLayout.getDataTable(cts.b);
        var widthSetter = grid.widthSetter == null ? $RWT.gridLayout._defaultCellWidthSetter : grid.widthSetter;
        $RWT.gridLayout._iterateRows(dt, function (tr) {
            var i = 0;
            if ($RWT.gridLayout._isSpannedRow(tr)) {
                return;
            }
            $RWT.gridLayout._iterateRowCells(tr, function (td) {
                if (i == resizeColIndex) {
                    widthSetter(grid, i, td, (hc[i].columnWidth - hc[i].borderWidth));
                    td.wasResized = true;
                }
                i++;
            });
        });
        if (cts.h.stretchIdx!=resizeColIndex){
            $RWT.gridLayout._resizeStretchColumn(grid, cts, hc);
        }
        $RWT.gridLayout._sendResizeEvent(cts, hc, resizeColIndex);
        $RWT.gridLayout._drawFocusFrames(cts.b.rowFrame, cts.b.cellFrame);
        $RWT.gridLayout._setupVerticalHeaderHeight(grid, cts);
    },
    _sendResizeEvent: function(cts, hc, resizeColIndex){
        var widths = '';
        var td;
        var sizePolicy;
        for (var i = 0; i < hc.count; i++) {
            td = hc[i];
            sizePolicy = i==resizeColIndex ? 'manual' : td.sizePolicy;
            widths += $(td).attr('ccid') + ':' + Math.round(td.columnWidth) + ',' + sizePolicy + ';';            
        }
        $RWT.actions.event(WpsUtils.findFirstChild(cts.h), 'resizeEvent', widths);
    },
    _resizeStretchColumn: function (grid, cts, hc) {
        $RWT.gridLayout._calcStretchColumnWidth(cts, hc);
        var dt = $RWT.gridLayout.getDataTable(cts.b);
        var widthSetter = grid.widthSetter == null ? $RWT.gridLayout._defaultCellWidthSetter : grid.widthSetter;
        var stretchIdx = cts.h.stretchIdx;
        $RWT.gridLayout._iterateRows(dt, function (tr) {
            var i = 0;
            if ($RWT.gridLayout._isSpannedRow(tr)) {
                return;
            }
            $RWT.gridLayout._iterateRowCells(tr, function (td) {
                if (i == stretchIdx) {
                    widthSetter(grid, i, td, (hc[i].columnWidth - hc[i].borderWidth));
                    td.wasResized = true;
                }
                i++;
            });
        });
    },
    _getTreeMaxLevel: function (cts) {
        var ml = cts.b.getAttribute('max-level');
        return ml == null ? 0 : parseInt(ml, 10);
    },
    _initFilterEditor: function(editor, grid){
        var input = $RWT.inputBox.findInput(editor);
        if (input!=null){
            $(input).keyup(function() {
                                $RWT.delay(function(){
                                        $RWT.actions.event(grid, 'filter', input.value);
                                       }, 500 );
            });
        }
    },
    _findFilterEditor: function(grid){
        var filterEditorId = grid.getAttribute('filtereditor');
        if ( filterEditorId!=null ){
            var editors = $('#' + filterEditorId);
            if (editors != null && editors.length > 0) {
                return editors[0];
            }
        }
        return null;
    },
    _syncScroll: function(dataTable) {
        var outerContainer = dataTable.parentNode;
        var grid = outerContainer.parentNode;

        var horizontalHeader = WpsUtils.findChildByLocalName(outerContainer, 'div');
        var table = $RWT.gridLayout._getHeaderTable(horizontalHeader);
        $(table).css('left', - dataTable.scrollLeft + 'px');

        var verticalHeader = WpsUtils.findChildByLocalName(grid, 'div');
        if ($(verticalHeader).hasClass('rwt-grid-vertical-header-panel')){
            var cornerWidget = WpsUtils.findChildByLocalName(verticalHeader, 'div');
            var tableContainer = cornerWidget==null ? null : WpsUtils.findNextSibling(cornerWidget, 'div');
            if (tableContainer){
                table = $RWT.gridLayout._getHeaderTable(tableContainer);
                $(table).css('top', - dataTable.scrollTop + 'px');
            }
        }
    },
    onVerticalHeaderCellClick: function(event){
        if (event.button == 0) {
            var vHeaderCell = event.target==null ? null : WpsUtils.findParentByClassName(event.target,'rwt-grid-row-header');
            if (vHeaderCell!=null){        
                $RWT.actions.event(vHeaderCell, 'click', $RWT.events.getButtonsMask(event).toString());
            }      
        }
    },
    onVerticalHeaderCellDblClick: function(event){
        if (event.button == 0) {
            var vHeaderCell = event.target==null ? null : WpsUtils.findParentByClassName(event.target,'rwt-grid-row-header');
            if (vHeaderCell!=null){
                $RWT.actions.event(vHeaderCell, 'dblclick', $RWT.events.getButtonsMask(event).toString());
            }
        }
    }
}

$RWT.propEditor = {
    requestFocus: function (pe) {
        var c = WpsUtils.findFirstChild(pe, 'div');
        if (c) {
            if (c.rwt_f_requestFocus) {
                c.rwt_f_requestFocus(c);
            } else {
                $(c).focus();
            }
        }
    }
}

$RWT.delay = (function () {
    var timer = 0;
    return function (callback, ms) {
        clearTimeout(timer);
        timer = setTimeout(callback, ms);
    };
})();
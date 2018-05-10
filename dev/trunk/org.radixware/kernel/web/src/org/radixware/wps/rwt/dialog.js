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
_$RWT_DLG_OBJ = {
    _ce: function(e) {
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
    decHeight: function(div) {
        var contentHeight = $(div).outerHeight();
        WpsUtils.iterateAll(div, function(c) {
            var h = $(c).outerHeight();
            if (h > contentHeight) {
                contentHeight = h;
            }
        });

        return contentHeight;
    },
    _layoutImpl: function(object, rr_rs, rqId) {
        if (_$RWT_DLG_OBJ.isFree(object)) {
            if (rr_rs != null)
                rr_rs[object.id] = 'ping';
        }
        var w = $(object).innerWidth();
        var header, menuBar, outerBody, innerBody, buttons, handleBar;
        var updateWidths = function(w) {
            if (buttons) {
                $(buttons).width(w - 7).css('left', '2px');
            }
            if (outerBody) {
                $(outerBody).width(w - 4).css('left', '2px');
                $(innerBody).width(w - 8);
            }
        }
        WpsUtils.iterateNames(object, 'div', function(div) {
            var role = $(div).attr('role');
            if (role === 'buttons') {
                buttons = div;
            } else if (role === 'outerBody') {
                outerBody = div;
                innerBody = WpsUtils.findFirstChild(outerBody);
            } else if ('title' === role) {
                header = div;
            } else if (role === 'handle-bar') {
                handleBar = div;
            } else if (role === 'menuBar') {
                menuBar = div;
            }
        });
        if (header) {
            $(header).height(23);
            header.calcAdjustSize = function() {
                var adjustWidth;
                if ($(header).width() < $(WpsUtils.findFirstChild(header)).width()) {
                    adjustWidth = $(WpsUtils.findFirstChild(header)).width() - $(header).width();
                }
                return {"width" : adjustWidth, "height": null};
            };
        }
        if (buttons) {
            var tbs = 0;
            var cnt = 0;
            WpsUtils.iterateAll(buttons, function(button) {
                //var left = $(c).position().left;
                if ($(button).css('display') != 'none') {
                    if (button.rwt_layout)
                        button.rwt_layout(button);
                    else {
                        var outerWidth = 0;
                        WpsUtils.iterateAll(button, function(buttonItem) {
                            outerWidth += $(buttonItem).outerWidth();
                        });
                        $(button).width(outerWidth + 20);
                    }
                    tbs += $(button).outerWidth();
                    cnt++;
                }
            });
            var existingMW = $(object).css('min-width');
            if (existingMW) {
                var idx = existingMW.indexOf('px');
                if (idx > 0) {
                    existingMW = parseInt(existingMW.substring(0, idx));
                } else
                    existingMW = null;
            }
            var newMW = tbs + (cnt * 5) + 7;
            if (existingMW != null && existingMW <= newMW) {
                $(object).css('min-width', newMW + 'px');
            }
        }
        //debugger;
        updateWidths($(object).innerWidth());
        var isAh = $(outerBody).attr("isAutoHeight");
        if (isAh)
            isAh = isAh === 'true';
        var maxHeight = $(outerBody).attr("maxHeight");
        if (maxHeight)
            maxHeight = parseInt(maxHeight, 10);

        var isAw = $(outerBody).attr("isAutoWidth");
        if (isAw)
            isAw = isAw === 'true';
        var maxWidth = $(outerBody).attr("maxWidth");
        if (maxWidth)
            maxWidth = parseInt(maxWidth, 10);

        var buttons_list = [];
        WpsUtils.iterateAll(buttons, function(bt) {
            if ($(bt).css('display') != 'none') {
                buttons_list.push(bt);
            }
        });
        if (buttons_list.length == 0)
            $(buttons).height(2);
        else {
            $(buttons).height(28);
        }
        var bh = $(buttons).innerHeight();

        for (var i = 0; i < buttons_list.length; i++) {
            var bt = $(buttons_list);
            var bth = bt.outerHeight();
            bt.css('top', (bh / 2 - bth / 2) + 'px');
        }

        if (handleBar) {
            $(handleBar).height(3);
            var seHandle = null;

            WpsUtils.iterateAll(handleBar, function(div) {
                var role = $(div).attr('role');
                if (role == 'se-handle') {
                    seHandle = div;
                    return true;
                }
            });

            if (seHandle) {
                $(seHandle).css('float', 'right');
                var isResizable = $(object).attr("isresizable");
                if (isResizable == 'false') {
                    $(seHandle).css('display', 'none');
                }
            }
        }

        var dec = 0;
        if (seHandle && $(seHandle).css('display') != 'none')
            dec = 4;//$(seHandle).outerHeight();

        var headerHeight = 24;
        if ($(menuBar).css("display") != "none") {
            headerHeight += $(menuBar).height();
        }
        if (isAh && !object.wasSized) {
            var cw = 0;
            var max = 0;
            var usemax = true;

            WpsUtils.iterateAll(innerBody, function(c) {
                $RWT._layoutNode(c);
                tableItem = $(c);
                if ($(c).css('position') == 'absolute' || $(c).css('position') == 'relative') {
                    cw += $(c).outerHeight();
                    max = Math.max(_$RWT_DLG_OBJ._calcSuspectedBottomBorder(c), max);
                } else {
                    cw += $(c).outerHeight();
                    usemax = false;
                }
            });
            if (cw > 0 || (usemax && max > 0)) {
                var use = usemax ? max : cw;
                var inners = headerHeight + $(buttons).outerHeight() + dec;
                var dlgHeight = inners + use + 4;//4 = difference between outer and inner body
                if (maxHeight && maxHeight < dlgHeight) {
                    dlgHeight = maxHeight;
                }

                $(object).height(dlgHeight);
                var outerBodyHeight = dlgHeight - inners;
                $(outerBody).height(outerBodyHeight);
                $(innerBody).css('max-height', outerBodyHeight - 4 + 'px');
                $(innerBody).height(outerBodyHeight - 4);

                var mh = $(object).css('min-height');
                if (mh == null || mh == '' || mh == '0px') {
                    $(object).css('min-height', $(object).outerHeight() + 'px');
                }
            }
        } else {
            var h = $(object).innerHeight();
            outerBodyHeight = h - headerHeight - $(buttons).outerHeight() - dec;
            $(outerBody).height(outerBodyHeight);
            $(innerBody).css('max-height', outerBodyHeight - 4 + 'px');
            $(innerBody).height(outerBodyHeight - 4);
        }

        if (isAw && !object.wasSized) {
            cw = 0;
            WpsUtils.iterateAll(innerBody, function(c) {
                $RWT._layoutNode(c);
                cw = Math.max(_$RWT_DLG_OBJ._calcSuspectedRightBorder(c), cw);
                if (maxWidth && cw > maxWidth) {
                    cw = maxWidth;
                    return true;
                }
                else
                    return false;
            });
            var addintionalWidthAttr = $(object).attr('addintional-width');
            var addintionalWidth = addintionalWidthAttr==null ? 0 : parseInt(addintionalWidthAttr);
            if (cw > 0 && (object.prevCw==null || (object.prevCw+addintionalWidth)!=cw)) {
                var diff = $(object).innerWidth() - $(innerBody).innerWidth();
                object.prevCw = cw;
                var preferredWidth = cw + diff + addintionalWidth;
                $(object).width(preferredWidth);
                updateWidths($(object).innerWidth());
            }
        }

        object.rwt_rnd_refine = _$RWT_DLG_OBJ._afterLayout;

        header.window = object;

        var windowSize = WpsUtils.getBrowserWindowSize();
        var w2 = $(object).outerWidth();
        var h2 = $(object).outerHeight();
        if (w2 > windowSize.width) {
            $(object).width(windowSize.width - 10);
            updateWidths($(object).innerWidth());
        }
        if (h2 > windowSize.height) {
            $(object).height(windowSize.height - 10);
        }

        //$(object).css('left',size.width/2 - w2/2).css('top',size.height/2 - h2/2);

        if (!object.wasMoved) {
            if (object.offsetTop && object.offsetLeft) {
                var top = object.offsetTop >= 0 ? (object.offsetTop >= windowSize.height ? windowSize.height - h2 : object.offsetTop) : 0;
                var left = object.offsetLeft >= 0 ? (object.offsetLeft >= windowSize.width ? windowSize.width - w2 : object.offsetLeft) : 0;
                $(object).css('left', left).css('top', top);
            } else {
                $(object).css('left', windowSize.width / 2 - w2 / 2).css('top', windowSize.height / 2 - h2 / 2);
            }
        }
        return {
            'sizer': seHandle,
            'header': header
        };
    },
    _afterLayout: function(dialog, rqId){
        var outerBody, innerBody;
        WpsUtils.iterateNames(dialog, 'div', function(div) {
            var role = $(div).attr('role');
            if (role === 'outerBody') {
                outerBody = div;
                innerBody = WpsUtils.findFirstChild(outerBody);
            }
        });

        if (dialog.needToResize !== false && $(dialog).attr('isAdjustSizeEnabled') !== 'false') {
            var maxCalcWidth = 0;
            var maxCalcHeight = 0;

            WpsUtils.iterateAll(dialog, function(c) {
                if ($(c).css('display') != 'none') {
                    var adjustSize = $RWT.getAdjustSize(c);
                    if (adjustSize.width != null && adjustSize.width > maxCalcWidth) {
                        maxCalcWidth = adjustSize.width;
                    }
                    if (adjustSize.height != null) {
                        maxCalcHeight += adjustSize.height;
                    }
                }
            });
            var windowSize = WpsUtils.getBrowserWindowSize();
            var maxWidth = windowSize.width * 0.66;
            var maxHeight = windowSize.height * 0.66;
            var dialogWidth = $(dialog).width();
            var dialogHeight = $(dialog).height();
            var calcWidth = dialogWidth + maxCalcWidth;
            var calcHeight = dialogHeight + maxCalcHeight;
            if (calcWidth>dialogWidth) {
                if (calcWidth <= maxWidth) {
                    $(dialog).width(calcWidth);
                } else {
                    $(dialog).width(maxWidth);
                }
            }
            if (calcHeight>dialogHeight) {
                if (calcHeight <= maxHeight) {
                    $(dialog).height(calcHeight);
                } else {
                     $(dialog).height(maxHeight);
                }
            }
            $RWT._layout_set[dialog.id] = null;
            dialog.needToResize = false;
            $RWT._layoutNode(dialog, null, null);
            dialog.needToResize = null;
        }

        var childWidths =0;
        var childHeights  =0;
        var childBottomBorder = 0;
        var childRightBorder = 0;
                WpsUtils.iterateAll(innerBody, function(c) {
                childHeights +=$(c).outerHeight(true);
                if ($(c).css("position") == "absolute") {
                    var b = $(c).position().top + $(c).outerHeight(true);
                    var r = $(c).position().left + $(c).outerWidth(true);
                    if (b > childBottomBorder) {
                        childBottomBorder = b;
                    }
                    if (r > childRightBorder) {
                        childRightBorder = r;
                    }
                }
            childWidths += $(c).outerWidth(true);
        });
        
        if (childHeights < childBottomBorder) {
            childHeights = childBottomBorder;
        }
        if (childWidths < childRightBorder) {
            childWidths = childRightBorder;
        }

        if (childWidths > 0){
            if (childWidths - $(innerBody).outerWidth() > 4 && $(innerBody).css("overflow-x") != "auto"){
                if ($(innerBody).css("overflow-y") != "auto") { //w3.org says that if one overflow is set to scroll and other is visible than others value will be auto
                    innerBody.refresh = true;
                }
                $(innerBody).css("overflow-x", "auto");
                innerBody.scrolledX = true;
                innerBody.scrollUpdated = true;
            }
            else if (childWidths - $(innerBody).outerWidth() <= 4 && $(innerBody).css("overflow-x") != "hidden") {
                $(innerBody).css("overflow-x", "hidden");
                innerBody.scrolledX = false;
                innerBody.scrollUpdated = true;
            }
        if (childHeights > 0) {
            innerBody.maxChildBottomBorder = 0;
            if (childHeights - $(innerBody).outerHeight() > 4 && ($(innerBody).css("overflow-y") != "auto" || innerBody.refresh == true)) {
                innerBody.refresh = null;
                $(innerBody).css("overflow-y", "auto");
                innerBody.scrolledY = true;
                innerBody.scrollUpdated = true;
            }
            else if (childHeights - $(innerBody).outerHeight() <= 4 && $(innerBody).css("overflow-y") != "hidden") {
                $(innerBody).css("overflow-y", "hidden");
                innerBody.scrolledY = false;
                innerBody.scrollUpdated = true;
            }
        }
        if (innerBody.scrollUpdated == true) {
            innerBody.scrollUpdated = null;
            if ($RWT._layout_set != null) {
                $RWT._layout_set[innerBody.id] = null;
                $RWT._layout_set[outerBody.id] = null;
                WpsUtils.iterateAll(innerBody, function (c) {
                    $RWT._layout_set[c.id] = null;
                    _$RWT_DLG_OBJ._removeFromLayoutSetAllDescendants(c);
                })
            }
            $RWT.container.updateAnchors(dialog);
        }
    }
    },
    
    _removeFromLayoutSetAllDescendants : function (node) {
        for (var i = 0; i < node.childNodes.length; i++) {
          var child = node.childNodes[i];
          _$RWT_DLG_OBJ._removeFromLayoutSetAllDescendants(child);
          $RWT._layout_set[child.id] = null;
        }
    },
    _calcSuspectedBottomBorder: function(item) {
        var anchorOptions = _$RWT_DLG_OBJ._getAnchorOptions(item);
        var topMargin = $(item).position().top;
        var bottomMargin = 0;
        if (anchorOptions != null) {
            for (var a in anchorOptions) {
                if (a == 't') {
                    topMargin = anchorOptions[a].offset;
                } else if (a == 'b' && anchorOptions[a].part == 1) {
                    bottomMargin = -anchorOptions[a].offset;
                }
            }
        }
        return topMargin + $(item).outerHeight() + bottomMargin;
    },
    _calcSuspectedRightBorder: function(item) {
        var anchorOptions = _$RWT_DLG_OBJ._getAnchorOptions(item);
        var leftMargin = $(item).position().left;
        var rightMargin = 0;
        if (anchorOptions != null) {
            for (var a in anchorOptions) {
                if (a == 'l') {
                    leftMargin = anchorOptions[a].offset;
                } else if (a == 'r' && anchorOptions[a].part == 1) {
                    rightMargin = -anchorOptions[a].offset;
                }
            }
        }
        return leftMargin + $(item).outerWidth() + rightMargin;
    },
    _getAnchorOptions: function(item) {
        if (item.ancOptions) {
            return item.ancOptions;
        }
        var anc = $(item).attr("anchor");
        if (anc) {
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
            item.ancOptions = options;
            return options;
        }
        return null;
    },
    _escapeHandler: function(e, dialog) {
        $RWT._ce(e);
        var b = _$RWT_DLG_OBJ._findButtonForAction(dialog, dialog.escapeAction);
        if (b && b.onclick) {
            $RWT.finishEdit();
            $(b).focus();
            b.onclick();
        }
    },
    _defaultHandler: function(e, dialog) {
        $RWT._ce(e);
        var b = _$RWT_DLG_OBJ._findButtonForAction(dialog, dialog.defaultAction);
        if (b && b.onclick) {
            $RWT.finishEdit();
            $(b).focus();
            b.onclick();
        }
    },
    _closeKeyHandler: function(e) {
        if (e && (e.defaultPrevented == null || e.defaultPrevented == false) && (e.returnValue == null || e.returnValue == true)) {
            if (e.keyCode == 13) {
                _$RWT_DLG_OBJ._defaultHandler(e, this);
            }
            else if (e.keyCode == 27) {
                _$RWT_DLG_OBJ._escapeHandler(e, this);
            }
            else if (e.keyCode === 9) {//tab
                var cell = _$RWT_DLG_OBJ._findEditorCell();

                if (cell) {
                    if ($(cell).attr("tabindex") === 1) {//if first cell with edit mask
                        $RWT.grid.cell.onClick(cell, function() {
                            $(cell).focus();
                            var table = $(this).find("div.rwt-grid>table");
                            $RWT.actions.event(table, "key", e.keyCode.toString(), function() {
                                var cc = WpsUtils.findChildByLocalName(cell, 'div');//container
                                if (cc) {
                                    cc = WpsUtils.findChildByLocalName(cc, 'div');
                                    if (cc) {
                                        if (cc.rwt_f_requestFocus) {
                                            cc.rwt_f_requestFocus(cc);
                                        }
                                    }
                                }
                            });
                        });
                    }
                }
            }
        }
    },
    _findEditorCell: function() {
        var cell = $("div.rwt-grid>table td.rwt-grid-row-cell.editor-cell:eq(0)");
        if (cell.length > 0) {
            return cell[0];
        }
        else
        {
            return null;
        }
    },
    _findButtonForAction: function(object, action) {
        var buttonBox = _$RWT_DLG_OBJ._findButtonBox(object);

        if (buttonBox) {
            var b = null;
            WpsUtils.iterateAll(buttonBox, function(bt) {
                if ($(bt).attr("action") === action) {
                    b = bt;
                    return true;
                }
            });
            return  b;
        } else
            return null;
    },
    _findButtonBox: function(object) {
        var buttonBox = null;
        WpsUtils.iterateNames(object, 'div', function(div) {
            var role = $(div).attr('role');
            if (role === 'buttons') {
                buttonBox = div;
                return true;
            }
        });
        return buttonBox;
    },
    _minSize: function(object) {
        var w = 0, h = 0;
        WpsUtils.iterateAll(object, function(div) {
            var role = $(div).attr('role');
            if (role !== 'outerBody') {
                if (role === 'buttons') {
                    WpsUtils.iterateAll(div, function(bt) {
                        if ($(w).css('display' != 'none')) {
                            w += $(bt).outerWidth();
                        }
                    });
                }
                h += $(div).outerHeight();
            }
        });
        return {
            'width': w,
            'height': h
        };
    },
    top: function() {
        if (_$RWT_DLG_OBJ.overlay && _$RWT_DLG_OBJ.overlay._dialogs && _$RWT_DLG_OBJ.overlay._dialogs.length > 0) {
            return _$RWT_DLG_OBJ.overlay._dialogs[_$RWT_DLG_OBJ.overlay._dialogs.length - 1];
        } else {
            return null;
        }
    },
    inited: function(dialog) {
        if (!_$RWT_DLG_OBJ.overlay) {
            return false;
        }
        if (!_$RWT_DLG_OBJ.overlay._dialogs) {
            return false;
        }
        if (_$RWT_DLG_OBJ.overlay._dialogs.length == 0) {
            return false;
        }
        for (var i = 0; i < _$RWT_DLG_OBJ.overlay._dialogs.length; i++) {
            if (_$RWT_DLG_OBJ.overlay._dialogs[i] == dialog) {
                return true;  
            }
        }
        return false;
    },
    listDialogs: function(body) {
        var list = [];
        WpsUtils.iterateNames(body, 'div', function(d) {
            if (d.className && d.className.indexOf('rwt-dialog') >= 0 && d.id != 'wait-box' && d.id != 'wait-box-overlay' && !_$RWT_DLG_OBJ.isAsync(d))
                list.push(d);
        });
        return list;
    },
    hasDialogs: function(body) {
        var result = false;
        WpsUtils.iterateNames(body, 'div', function(d) {
            if (d.className && d.className.indexOf('rwt-dialog') >= 0 && d.id != 'wait-box' && d.id != 'wait-box-overlay' && !_$RWT_DLG_OBJ.isAsync(d)) {
                result = true;
                return true;
            }


        });
        return result;
    },
    isAsync: function(object) {
        if (object._rwt_async == null) {

            if (object.className && object.className.indexOf('rwt-ui-dialog-no-overlay') >= 0) {
                object._rwt_async = true;
            } else {
                object._rwt_async = false;
            }
        }
        return object._rwt_async;
    },
    isFree: function(object) {
        if (object._rwt_free == null) {

            if (object.className && object.className.indexOf('rwt-ui-dialog-show') >= 0) {
                object._rwt_free = true;
            } else {
                object._rwt_free = false;
            }
        }
        return object._rwt_free;
    },
    _async_registry: []
    ,
    _layout: function(object, rr_rs, rqId) {
        try {
            if (object.ui_dialog) {
                _$RWT_DLG_OBJ._layoutImpl(object, rr_rs, rqId);
            } else {
                if (_$RWT_DLG_OBJ.isAsync(object)) {
                    _$RWT_DLG_OBJ._async_registry.push(object);
                    _$RWT_DLG_OBJ._setMostTop(object);
                }
                $(object).css('opacity', 0);
                object.ui_dialog = true;
                if ($(object).attr('cb') === 'true') {
                    object.rwt_rnd_refine = function() {
                        return 'cb';
                    }
                }
                _$RWT_DLG_OBJ._createOverlay(object);
                $(object).attr('tabindex', 1);

                var escapeAction = $(object).attr('escapeaction');
                if (escapeAction != null) {
                    object.escapeAction = escapeAction;
                    object.onkeydown = _$RWT_DLG_OBJ._closeKeyHandler;
                }

                var defaultAction = $(object).attr('defaultaction');
                if (defaultAction != null) {
                    object.defaultAction = defaultAction;
                    object.onkeydown = _$RWT_DLG_OBJ._closeKeyHandler;
                }

                var handles = _$RWT_DLG_OBJ._layoutImpl(object, rr_rs, rqId);
                var size = WpsUtils.getBrowserWindowSize();
                var w = $(object).outerWidth();
                var h = $(object).outerHeight();

                if (object.offsetTop >= 0 && object.offsetLeft >= 0) {
                    var top = object.offsetTop >= 0 ? (object.offsetTop + h >= size.height ? size.height - h : object.offsetTop) : 0;
                    var left = object.offsetLeft >= 0 ? (object.offsetLeft + w >= size.width ? size.width - w : object.offsetLeft) : 0;
                    $(object).css('left', left).css('top', top);
                } else {
                    $(object).css('left', size.width / 2 - w / 2).css('top', size.height / 2 - h / 2);
                }

                $(object).draggable({
                    //cancel:".ui-dialog-content, .ui-dialog-titlebar-close",
                    //handle:".ui-dialog-titlebar",
                    handle: handles.header,
                    containment: "document",
                    start: function(e, ui) {
                        if (_$RWT_DLG_OBJ.isAsync(this)) {
                            _$RWT_DLG_OBJ._setMostTop(this);
                        }
                        if ($RWT.dropDown != null) {
                            $RWT.dropDown.closeAll();
                        }
                    },
                    stop: function(e, ui) {
                        //_$RWT_DLG_OBJ._layoutImpl(this.object);
                        var top = parseInt(this.offsetTop ? this.offsetTop : 0);
                        var left = parseInt(this.offsetLeft ? this.offsetLeft : 0);
                        var dlg_pos = 't:' + top + 'l:' + left;
                        $RWT.actions.event(this, 'dlg_pos', dlg_pos);
                        this.wasMoved = true;
                    }
                });

                if (handles.sizer) {
                    handles.sizer.object = object;

                    $(handles.sizer).draggable({
                        containment: "document",
                        helper: "clone",
                        start: function(e, ui) {
                            this.basew = $(this.object).outerWidth();
                            this.baseh = $(this.object).outerHeight();
                            var ms = _$RWT_DLG_OBJ._minSize(this.object);
                            this.minw = ms.width;
                            this.minh = ms.height;
                            this.object.wasMoved = true;
                            this.object.wasSized = true;
                        },
                        drag: function(e, ui) {
                            var dx = ui.position.left - ui.originalPosition.left;
                            var dy = ui.position.top - ui.originalPosition.top;
                            var newWidth = this.basew + dx;
                            if (newWidth < this.minw) {
                                newWidth = this.minw;
                                ui.position.left = ui.originalPosition.left + (newWidth - this.basew);
                            }
                            var newHeight = this.baseh + dy;
                            if (newHeight < this.minh) {
                                newHeight = this.minh;
                                ui.position.top = ui.originalPosition.top + (newHeight - this.baseh);
                            }
                            this.object.wasMoved = true;
                            $(this.object).width(newWidth);
                            $(this.object).height(newHeight);
                            this.object.needToResize = false;
                            _$RWT_DLG_OBJ._layoutImpl(this.object);
                        },
                        stop: function(e, ui) {
                            $RWT._layoutNode(this.object);
                            var w = parseInt($(this.object).outerWidth());
                            var h = parseInt($(this.object).outerHeight());
                            var dlg_size = 'w:' + w + 'h:' + h;
                            this.object.needToResize = false;
                            $RWT.actions.event(this.object, "dlg_size", dlg_size);
                        }
                    });
                }
                $(object).animate({
                    'opacity': 1
                }, {
                    duration: 200,
                    complete: function() {
                        if (!$(this).hasClass("rwt-ui-dialog-no-overlay")) {
                            this._focusedElement = document.activeElement;
                        }
                        $(this).focus();
                    }
                });
            }
        } catch (e) {
            alert(e);
        }
    },
    _getZIndex: function(object) {
        var parent = object.parentNode;
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
    },
    _createOverlay: function(object) {

        _$RWT_DLG_OBJ.overlay.initialize(object);
    },
    overlay: {
        _object: null,
        initialize: function(dialog) {
            if (_$RWT_DLG_OBJ.isAsync(dialog)) {
                return;
            }
            $RWT.addFocusLayer();
            var zIndex = WpsUtils.getNextZIndex(dialog);
            if (_$RWT_DLG_OBJ.overlay._object) {
                var list = _$RWT_DLG_OBJ.listDialogs(dialog.parentNode);
                for (var i = 0; i < list.length; i++) {
                    if (list[i] == dialog)
                        continue;
                    var z = parseInt($((list[i])).css('zIndex'), 10);

                    if (z != NaN && z > zIndex) {
                        zIndex = z;
                    }
                }
            } else {
                _$RWT_DLG_OBJ.overlay._object = $('<div id="' + $RWT.dialog._ov_id + '" class="ui-widget-overlay">');
                $('body').append(_$RWT_DLG_OBJ.overlay._object);
                //$('outerBody').append(_$RWT_DLG_OBJ.overlay._object);
                _$RWT_DLG_OBJ.overlay._dialogZIndex = zIndex + 1;
            }
            _$RWT_DLG_OBJ.overlay._object.css('zIndex', zIndex + 1);
            $(dialog).css('zIndex', zIndex + 2);
            _$RWT_DLG_OBJ.overlay._dialogs.push(dialog);
        },
        close: function(dialog) {
            if (dialog) {
                if (_$RWT_DLG_OBJ.isAsync(dialog)) {
                    return;
                }
                var len = _$RWT_DLG_OBJ.overlay._dialogs.length;
                if (len == 0)
                    return;

                var last = _$RWT_DLG_OBJ.overlay._dialogs[len - 1];
                if (last == dialog) {
                    _$RWT_DLG_OBJ.overlay._dialogs.pop();
                } else {
                    for (var i = 0; i < len; i++) {
                        if (_$RWT_DLG_OBJ.overlay._dialogs[i] == dialog) {
                            _$RWT_DLG_OBJ.overlay._dialogs.splice(i, 1);
                        }
                    }
                }
                if (_$RWT_DLG_OBJ.overlay._dialogs.length == 0) {
                    _$RWT_DLG_OBJ.overlay._object.remove();
                    _$RWT_DLG_OBJ.overlay._object = null;
                } else {
                    //var current = _$RWT_DLG_OBJ.overlay._dialogs[_$RWT_DLG_OBJ.overlay._dialogs.length-1];
                    //var zIndex = $(_$RWT_DLG_OBJ.overlay._object).css('zIndex');
                    last = _$RWT_DLG_OBJ.overlay._dialogs[_$RWT_DLG_OBJ.overlay._dialogs.length - 1];
                    _$RWT_DLG_OBJ.overlay._object.css('zIndex', $(last).css('zIndex') - 1);
                }
                var rr_rs = {};
                rr_rs[dialog.id] = 'closed';
                $RWT.closeFocusLayer();
                $RWT.notifyRendered(rr_rs);
                if (dialog._focusedElement != null) {
                    dialog._focusedElement.focus();
                }
                //var rr_rs =$RWT.layoutDocument();            
                //                if(!rr_rs[dialog.id])
                //                    rr_rs[dialog.id] = 'closed';            
                //                if(rr_rs){                //                
                //                    $RWT.notifyRendered(rr_rs);
                //                }
            }
        },
        _dialogs: []
    },
    _setMostTop: function(target) {
        for (var i = 0; i < _$RWT_DLG_OBJ._async_registry.length; i++) {
            var dialog = _$RWT_DLG_OBJ._async_registry[i];
            $(dialog).css('zIndex', '');

        }
        $(target).css('zIndex', WpsUtils.getNextZIndex(target)+1);

    },
    close: function(object) {
        if (_$RWT_DLG_OBJ.isAsync(object)) {
            for (var i = 0; i < _$RWT_DLG_OBJ._async_registry.length; i++) {
                if (_$RWT_DLG_OBJ._async_registry[i] == object) {
                    _$RWT_DLG_OBJ._async_registry.splice(i, 1);
                    break;
                }
            }
        }
        _$RWT_DLG_OBJ.overlay.close(object);
    },
    rwt_f_resize: function(node) {
    },
    //            var dialogButtons = {};
    //            var dialogBody =null;
    //            var dialogTitle= '';            
    //            WpsUtils.iterateNames(object,'div',function(div){
    //                var role = $(div).attr('role');
    //                if(role==='buttons')  {
    //                    WpsUtils.iterateNames(div,'span',function(span){
    //                        dialogButtons[$(span).text()]=function(){
    //                            if(object.ui_dialog){
    //                                this.rwt_button_id=span.id;                            
    //                                $(this).dialog('close');
    //                            }
    //                        };
    //                    });
    //                }else if(role==='title'){
    //                    dialogTitle = $(div).text();
    //                }else if(role==='outerBody'){
    //                    dialogBody = div;
    //                }                
    //            });
    //        
    //            if(dialogBody){
    //                var h = $(object).outerHeight();
    //                var w = $(object).outerWidth();
    //                object.bodyId = dialogBody.id;
    //                object.ui_dialog = $(dialogBody).dialog({
    //                    beforeClose:_$RWT_DLG_OBJ._beforeClose,
    //                    autoOpen:false,
    //                    close:function(){                       
    //                        alert(this.rwt_button_id)
    //                        return true;
    //                    },
    //                    title:dialogTitle, 
    //                    buttons:dialogButtons,
    //                    modal: true,
    //                    height:h,
    //                    width:w
    //                });     
    //                
    //                $(dialogBody).dialog('open');
    //                
    //                
    //                
    //                object.ui_dialog_body = dialogBody;
    //            }

    _beforeClose: function(e) {
        if (this.rwt_button_id) {
            var id = this.rwt_button_id;
            this.rwt_button_id = null;
            $RWT._aeid(id, 'click');
        }
        return false;
    },
    download: function(name, url) {
        window.open('rwtext/file/' + url, null, "menubar=no,location=no,resizable=no,scrollbars=no,status=no");
    },
    downloadAndSave: function(name, url) {
        var wnd = window.open('rwtext/file/' + url, null, "menubar=no,location=no,resizable=no,scrollbars=no,status=no");
    },
    progressHandle: {
        _lastUndState: null,
        _lastUndDir: 0,
        _curState: function(loop) {

            var container = loop.parentNode;
            var w = $(container).innerWidth();
            if (w == 0)
                return null;
            return $(loop).position().left / w;
        },
        _minHeight: function(handle) {
            var h = 0;
            var label = WpsUtils.findChildByLocalName(handle, 'label');
            if (label)
                h += $(label).outerHeight() + 3;
            var content = WpsUtils.findChildByLocalName(handle, 'div');
            if (content) {
                h += $(content).outerHeight() + 3;

            }
            var button = WpsUtils.findChildByLocalName(handle, 'button');
            if (button)
                h += $(button).outerHeight() + 3;
            return h;
        },
        layout: function(handle) {
            var content = WpsUtils.findChildByLocalName(handle, 'div');
            $(handle).height($RWT.dialog.progressHandle._minHeight(handle));
            if (content) {
                var ow = $(handle).outerWidth();
                $(content).width(ow - 10);
                $(content).css('left', '5px');

                $(content).css('position', 'absolute');
                var label = WpsUtils.findChildByLocalName(handle, 'label');
                if (label) {
                    $(label).css('left', '5px');
                    $(label).css('top', '3px');
                    $(label).css('position', 'absolute');
                    $(content).css('top', 3 + $(label).outerHeight() + 3 + 'px');
                }
                var button = WpsUtils.findChildByLocalName(handle, 'button');
                if (button) {
                    var bw = $(button).outerWidth();
                    $(button).css('left', (ow / 2 - bw / 2) + 'px');
                    $(button).css('top', 3 + $(label).outerHeight() + 3 + $(content).outerHeight() + 3 + 'px');
                    $(button).css('position', 'absolute');
                }
                var loop = WpsUtils.findChildByLocalName(content, 'div');
                var isDeterminate = $(loop.handle).attr('amount');

                if (loop.handle == null || isDeterminate != loop.isDeterminate) {
                    loop.isDeterminate = isDeterminate;
                    var animator = null;
                    if (loop.isDeterminate) {
                        animator = {
                            update: function() {
                                var total = $(loop.handle).attr('amount');
                                var cur = $(loop.handle).attr('current');
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
                                    $(loop).css('left', '0').width(sw).height(h);
                                    $RWT.actions.event(loop.handle, 'progress');
                                }
                            }
                        }
                    } else {
                        animator = {
                            _update: function() {
                                var container = this.loop.parentNode;
                                var h = $(container).innerHeight();
                                var w = $(container).innerWidth();
                                $(loop).height(h).width(w / 10);
                                var sw = $(this.loop).outerWidth();

                                return w - sw;
                            },
                            toEnd: function() {
                                var pos = this._update();
                                $(this.loop).animate({
                                    'left': pos + 'px'
                                }, {
                                    step: function() {
                                        $RWT.dialog.progressHandle._lastUndDir = 0;
                                        var state = $RWT.dialog.progressHandle._curState(this);
                                        if (state != null)
                                            $RWT.dialog.progressHandle._lastUndState = state;
                                        if (this.isDeterminate) {
                                            $(this).stop();
                                        }
                                    },
                                    duration: 3000,
                                    easing: 'linear',
                                    complete: function() {
                                        if (!this.isDeterminate) {
                                            this.animator.toStart();
                                        }
                                    }
                                });
                            },
                            toStart: function() {
                                this._update();
                                $(this.loop).animate({
                                    'left': 0 + 'px'
                                }, {
                                    step: function() {
                                        $RWT.dialog.progressHandle._lastUndDir = 1;
                                        var state = $RWT.dialog.progressHandle._curState(this);
                                        if (state != null)
                                            $RWT.dialog.progressHandle._lastUndState = state;
                                        if (this.isDeterminate) {
                                            $(this).stop();
                                        }
                                    },
                                    duration: 3000,
                                    easing: 'linear',
                                    complete: function() {
                                        if (!this.isDeterminate) {
                                            this.animator.toEnd();
                                        }
                                    }
                                });
                            },
                            update: function() {
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

                                $RWT.actions.event(loop.handle, 'progress');
                            }
                        };
                    }
                    loop.animator = animator;
                    loop.handle = handle;
                    animator.loop = loop;
                    if (!loop.isDeterminate) {
                        loop.animator.update();
                    }
                }
                if (loop.isDeterminate && loop.animator) {
                    loop.animator.update();
                }
            }
        }
    }
};

$RWT.dialog = _$RWT_DLG_OBJ;



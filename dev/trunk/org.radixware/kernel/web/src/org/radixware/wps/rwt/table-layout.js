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
$_RWT_TABLE_LAYOUT_OBJ = {
    _layout: function (layout) {
        var b = WpsUtils.findChildByLocalName(layout, 'tbody');
        //     var layoutSize = $(layout).innerHeight();
        if (b) {
            var unsizedRows = {};
            unsizedRows.count = 0;
            var size = 0;
            WpsUtils.iterateNames(b, 'tr', function (tr) {
                var min = $_RWT_TABLE_LAYOUT_OBJ._rowMinHeight(tr);
                if (min != 0) {
                    //$(tr).css('height',min+'px');
                    tr.style.height = min + 'px';
                    WpsUtils.iterateNames(tr, 'td', function (td) {
                        td.style.height = min + 'px';
                    });
                    size += min;
                } else {
                    tr.style.height = null;
                    //$(tr).css('height','');
                    WpsUtils.iterateNames(tr, 'td', function (td) {
                        td.style.height = '100%';
                    });
                    unsizedRows[unsizedRows.count++] = tr;
                }
                WpsUtils.iterateAll(tr, function (td) {
                    if (td.getAttribute("expandContent") == 'true') {
                        $_RWT_TABLE_LAYOUT_OBJ._expandContent(td);
                    }
                });
            });
            //            var sizePerRow = (layoutSize-size)/unsizedRows.count;
            //            for(var i=0;i<unsizedRows.count;i++){
            //                $(unsizedRows[i]).css('height',sizePerRow+'px');
            //                WpsUtils.iterateNames(unsizedRows[i],'td',function(c){
            //                    $(c).css('height',sizePerRow+'px');
            //                });                
            //            }
            // grab elements as array, rather than as NodeList
//            $($(layout).parent()).attr("onscroll", "runOnScroll()")
            if ($(layout).attr("scrolledToRow") >= 0) {
                var scrolledToRow = $(b.children[$(layout).attr("scrolledToRow")]);
                var divWidth = $($(layout).parent()).height() - WpsUtils.getScrollbarHeight();
                var divScrollTop = $(layout).parent().scrollTop();
                var scrollToLength = 0;
                if (scrolledToRow.position().top > divWidth) {
                    scrollToLength = scrolledToRow.position().top + divScrollTop - divWidth + scrolledToRow.height();
                } else if (scrolledToRow.position().top < 0){
                    scrollToLength = scrolledToRow.position().top + divScrollTop; 
                }

                if (scrollToLength > 0) {
                    $(layout).parent().scrollTop(scrollToLength);
                    $($($(scrolledToRow).children("td:first")).children("div:first")).focus(function (e) {
                        $RWT.currentFocusLayer().target = e.target;
                    });
                }
                $(layout).removeAttr("scrolledToRow");
            }
        }
        layout.rwt_layout = $_RWT_TABLE_LAYOUT_OBJ._layout;
    },
    _rowMinHeight: function (tr) {
        var min = 0;
        WpsUtils.iterateNames(tr, 'td', function (td) {
            var h = $_RWT_TABLE_LAYOUT_OBJ._cellMinHeight(td);
            if (h > min) {
                min = h;
            }
        });
        return min;
    },
    _cellMinHeight: function (td) {
        var min = 0;
        WpsUtils.iterateAll(td, function (c) {

            if (c.style.height && c.style.height.indexOf('px') > 0) {
                if ($(c).outerHeight() > min) {
                    min = $(c).outerHeight();
                }
            }
        });
        return min;
    },
    _expandContent: function (td) {
        var th = $(td).innerHeight();
        var hs = {};
        hs.count = 0;
        hs.fc_count = 0;
        hs.fixed = 0;
        WpsUtils.iterateAll(td, function (content) {
            if (content.style && content.style.height) {
                if (content.style.height.indexOf('px') > 0) {//preferred
                    var h = $(content).outerHeight();
                    if (content.rwt_f_minsize) {
                        var minh = content.rwt_f_minsize(content).h;
                        if (minh > h) {
                            h = minh;
                        }
                    }
                    hs[hs.count] = {
                        c: content,
                        height: h
                    };
                    hs.fixed += h;
                    hs.fc_count++;
                }
                else if (content.style.height.indexOf('%') > 0) {//expanding
                    hs[hs.count] = {
                        c: content,
                        height: null
                    };
                }
            } else {//minimum-expanding
                var h;
                if (content.rwt_f_minsize)
                    h = content.rwt_f_minsize(content).h;
                else
                    h = $(content).outerHeight();
                hs[hs.count] = {
                    c: content,
                    height: h
                };
                hs.fixed += h;
                hs.fc_count++;
            }
            hs.count++;
        });
        if (hs.fixed > th && hs.fc_count > 0) {
            var diff = (hs.fixed - th) / hs.fc_count;
            for (var i = 0; i < hs.count; i++) {
                if (hs[i].height != null) {
                    hs[i].height -= diff;
                }
            }
        }
        var exp = {};
        exp.count = 0;
        for (i = 0; i < hs.count; i++) {
            if (hs[i].height != null) {
                hs[i].c.style.height = hs[i].height + 'px';
            } else {
                exp[exp.count] = hs[i].c;
                exp.count++;
            }
        }
        if (exp.count > 0) {
            var eh = (th - hs.fixed) / exp.count;
            for (i = 0; i < exp.count; i++) {
                exp[i].style.height = eh + 'px';
            }
        }
    },
    layoutSplitter: function (splitter, rr_rs, rqId, targetCell, ignoreCheckRatio) {
        var THIS = $RWT.table_layout;
        var parentNode = splitter.parentNode;
        var w = $(parentNode).innerWidth() - 1;
        var h = $(parentNode).innerHeight() - 1;
        var rows = [];
        var body = WpsUtils.findChildByLocalName(splitter, 'tbody');
        WpsUtils.iterateNames(body, 'tr', function (tr) {
            if ($(tr).css('display') != 'none') {
                rows.push(tr);
            }
        });
        var ratios = [];
        if (rows.length > 1 || $(splitter.parentNode).attr('orientation') == 'vertical') {//vertical            
            if (splitter.inited != 'v') {
                var inc = 1 / rows.length;
                var ratio = 0;
                for (var i = 0; i < rows.length; i++) {
                    var component = THIS._splitterComponentForRow(rows[i]);
                    var attrVal = $(component).attr('ratio');
                    if (attrVal) {
                        ratio = parseFloat(attrVal);
                    } else {
                        if (ratio > 1)
                            ratio = 1;
                        ratio += inc;
                    }
                    rows[i].ratio = ratio;
                    ratios.push(ratio);
                }
                splitter.inited = 'v';
            } else {
                for (i = 0; i < rows.length; i++) {
                    ratio = rows[i].ratio;
                    component = THIS._splitterComponentForRow(rows[i]);
                    //if(ratio==null ){                        

                    attrVal = $(component).attr('ratio');
                    if (attrVal) {
                        ratio = parseFloat(attrVal);
                    }
                    // }                                           
                    ratios.push(ratio);
                }
                var prev = 0;
                ratios[0] = 0;
                for (i = 0; i < rows.length; i++) {
                    ratio = ratios[i];
                    if (ratio == null) {
                        if (i < rows.length - 1) {
                            ratio = (ratios[i + 1] - prev) / 2;
                        } else
                            ratio = 1;
                    }
                    if (i > 0) {
                        if (ratio <= ratios[i - 1]) {
                            component = THIS._splitterComponentForRow(rows[i]);
                            attrVal = $(component).attr('ratio');
                            if (attrVal) {
                                ratio = parseFloat(attrVal);
                            }
                            if (ratio == null || ratio <= ratios[i - 1]) {
                                if (i < ratios.length - 1) {
                                    ratio = (ratios[i + 1] + ratios[i - 1]) / 2;
                                } else
                                    ratio = 1;
                            }
                        }
                    }
                    rows[i].ratio = ratio;
                    ratios[i] = ratio;
                    prev = ratio;
                }
            }


            for (i = 0; i < rows.length; i++) {
                var col = WpsUtils.findFirstChild(rows[i]);
                component = THIS._splitterComponentForCell(col)
                $(component).width(w);
                var iw = $(component).innerWidth();
                var elems = THIS._components(component);
                var span = elems.handle;
                var selfRatio = ratios[i];
                if (i == 0) {
                    $(span).css('display', 'none');
                    if ($(elems.container).css('display') == 'none' && component != targetCell) {//component is collapsed                                            
                        $(component).height(0);
                        $(elems.container).height(0);
                    } else {
                        //look for next non collapsed element
                        var dec = 0;
                        var nextRatio = 1;
                        for (var r = i + 1; r < rows.length; r++) {
                            var next = THIS._splitterComponentForRow(rows[r]);
                            var nelems = THIS._components(next);
                            if ($(nelems.container).css('display') == 'none' && next != targetCell) {//collapsed
                                $(nelems.handle).height(5);
                                $(next).height($(nelems.handle).outerHeight());
                                dec += $(next).outerHeight();
                            } else {
                                nextRatio = ratios[r];
                                break;
                            }
                        }
                        var part;
                        if (rows.length == 1) {
                            part = h - dec;
                        } else {
                            part = (nextRatio - selfRatio) * h - dec;
                        }
                        $(component).height(part);
                        var ch = $(component).innerHeight() - dec;
                        if ($(span).css('display') != 'none') {
                            ch -= $(span).outerHeight();
                        }
                        ch -= 1;
                        $(elems.container)
                                .height(ch)
                                .width(iw - 1);
                    }
                } else {
                    $(span)
                            .css('display', '')
                            .height(5)
                            .width(iw - 2);
                    if ($(elems.container).css('display') == 'none' && component != targetCell) {//component is collapsed                                            

                        $(component).height($(span).outerHeight());
                    } else {
                        //look for next non collapsed element
                        dec = 0;
                        nextRatio = 1;
                        for (r = i + 1; r < rows.length; r++) {
                            next = THIS._splitterComponentForRow(rows[r]);
                            nelems = THIS._components(next);
                            if ($(nelems.container).css('display') == 'none' && next != targetCell) {//collapsed
                                $(nelems.handle).height(5);
                                $(next).height($(nelems.handle).outerHeight());
                                dec += $(next).outerHeight();
                            } else {
                                nextRatio = ratios[r];
                                break;
                            }
                        }

                        part = (nextRatio - selfRatio) * h - dec;
                        $(component).height(part);
                        ch = $(component).innerHeight() - dec;
                        if ($(span).css('display') != 'none') {
                            ch -= $(span).outerHeight();
                        }
                        ch -= 1;
                        $(elems.container)
                                .height(ch - 1)
                                .width(iw - 1);
                    }
                }
                var content = WpsUtils.findFirstChild(elems.container);
                if (content != null) {
                    var ciw = $(elems.container).innerWidth();
                    var cih = $(elems.container).innerHeight();
                    $(content).css('position', 'relative').
                            css('width', ciw + 'px').css('height', cih + 'px').css('top', 0).css('left', 0);
                }



                //                
                //                var nextRatio = i+1>=rows.length?1:ratios[i+1];
                //                
                //                var part = (nextRatio-selfRatio) * h;
                //                var dec = i==0?0:5;
                //                $(component).height(part);                 
                //                if(i == 0)
                //                    $(span).css('display','none');
                //                else{
                //                    $(span).css('display','')                        
                //                    .height(dec)
                //                    .width(iw-2);
                //                    dec = $(span).outerHeight();
                //                }               
                //                var container = elems.container;
                //                var ch = $(component).innerHeight()-dec;
                //                $(container)
                //                .height(ch-1)
                //                .width(iw-1);
                $RWT._layoutNode(component, rr_rs, rqId);
                if (i > 0 && span.inited != 'v') {
                    span.inited = 'v';
                    $(span).draggable({
                        axis: ("y"),
                        delay: 200,
                        distance: 1,
                        containment: splitter.parentNode,
                        zIndex: 3,
                        helper: "clone",
                        drag: function (e, ui) {
                            var table = THIS._splitterTable(this);
                            if (table) {
                                var diff = ui.position.top - ui.originalPosition.top;
                                var td = THIS._splitterCell(this);
                                var currentSize = $(td).innerHeight();
                                var newSize = currentSize - diff;
                                if (newSize < 5) {
                                    diff = 5 - newSize;
                                    ui.position.top -= diff;
                                    return true;
                                }

                                var prev = THIS._prevCellForRow(this, true);
                                if (prev) {
                                    currentSize = $(prev).innerHeight();
                                    newSize = currentSize + diff;
                                    if (newSize < 5) {
                                        diff = 5 - newSize;
                                        ui.position.top += diff;
                                        return true;
                                    }
                                }
                                return true;
                            } else
                                return false;
                        },
                        stop: function (e, ui) {
                            var table = THIS._splitterTable(this);
                            if (table) {
                                var tr = THIS._splitterRow(this);
                                if (tr) {
                                    var top = ui.position.top; // - $(table.parentNode).position().top;
                                    tr.ratio = top / $(table.parentNode).innerHeight();
                                    var component = THIS._splitterComponentForRow(tr);
                                    if (component) {
                                        $RWT.actions.event(component, 'up-r', tr.ratio.toString(), function () {
                                            THIS._checkCollapsed(table, true);
                                        });
                                    }
                                    //THIS.layoutSplitter(table,null,null,THIS._splitterComponentForRow(tr));     


                                }
                            }
                        }
                    });
                }
            }
            splitter.calcAdjustSize = function() {
                var finalHeight, finalWidth;
                for (i = 0; i < rows.length; i++) {
                    var col = WpsUtils.findFirstChild(rows[i]);
                    component = THIS._splitterComponentForCell(col);
                    var adjustSizeNew = $RWT.getAdjustSize(component);
                    if (adjustSizeNew != null) {
                        var adjustHeight = adjustSizeNew.height;
                        if (adjustHeight != null) {
                            finalHeight = adjustHeight + adjustHeight * (ratios[i])/(1 - ratios[i]) + (finalHeight == null ? 0 : finalHeight);
                        }
                        if (finalWidth == null) {
                            finalWidth = adjustSizeNew.width;
                        } else if (adjustSizeNew.width > finalWidth) {
                            finalWidth = adjustSizeNew.width;
                        }
                    }
                }
                return {"width" : finalWidth, "height" : finalHeight, "final" : true};
            };
        } else if (rows.length == 1) {//horizontal
            var cols = [];
            WpsUtils.iterateNames(rows[0], 'td', function (td) {
                if ($(td).css('display') != 'none')
                    cols.push(td);
            });
            if (cols.length > 0) {
                var updateRatios = function (cols, ratios) {
                    var inc = 1 / cols.length;
                    var ratio = 0;
                    var delta;
                    for (i = 0; i < cols.length; i++) {
                        component = THIS._splitterComponentForCell(cols[i]);
                        attrVal = $(component).attr('ratio');
                        if (attrVal) {
                            ratio = parseFloat(attrVal);
                        } else {
                            if (ratio > 1)
                                ratio = 1;
                            ratio += inc;
                        }
                        cols[i].ratio = ratio;
                        component.sv_ratio = ratio;
                        ratios.push(ratio);
                    }
                }
                if (splitter.inited != 'h') {
                    updateRatios(cols, ratios);
                    splitter.inited = 'h';
                } else {
                    updateRatios(cols, ratios);
                }

                for (i = 0; i < cols.length; i++) {
                    component = THIS._splitterComponentForCell(cols[i])
                    $(component).height(h);
                    var ih = $(component).innerHeight();
                    elems = THIS._components(component);
                    span = elems.handle;
                    dec = i == 0 ? 0 : 5;
                    selfRatio = ratios[i];
                    nextRatio = i + 1 >= cols.length ? 1 : ratios[i + 1];
                    part = cols.length == 1 ? w : (nextRatio - selfRatio) * w;
                    //automatically computed part

                    $(component).width(part);
                    if (i == 0)
                        $(span).css('display', 'none');
                    else {
                        $(span).css('display', '')
                                .width(dec)
                                .height(ih - 2);
                        dec = $(span).outerWidth();
                    }

                    var container = elems.container;
                    var cw = $(component).innerWidth() - dec - 1;
                    $(container).width(cw - 1).height(ih - 1);
                    content = WpsUtils.findFirstChild(container);
                    if (content != null) {
                        ciw = $(container).innerWidth();
                        cih = $(container).innerHeight();
                        $(content).css('position', 'relative').
                                css('width', ciw - 4 + 'px').css('height', cih - 3 + 'px').css('top', 1).css('left', 2);
                    }
                    $RWT._layoutNode(component, rr_rs, rqId);
                    if (i > 0 && span.inited != 'h') {
                        span.inited = 'h';
                        $(span).draggable({
                            axis: ("x"),
                            delay: 200,
                            distance: 1,
                            containment: splitter.parentNode,
                            zIndex: 3,
                            helper: "clone",
                            drag: function (e, ui) {
                                var table = THIS._splitterTable(this);
                                if (table) {
                                    var diff = ui.position.left - ui.originalPosition.left;
                                    var td = THIS._splitterCell(this);
                                    var currentSize = $(td).innerWidth();
                                    var newSize = currentSize - diff;
                                    if (newSize < 5) {
                                        diff = 5 - newSize;
                                        ui.position.left -= diff;
                                        return true;
                                    }

                                    var prev = THIS._prevCell(this);
                                    if (prev) {
                                        currentSize = $(prev).innerWidth();
                                        newSize = currentSize + diff;
                                        if (newSize < 5) {
                                            diff = 5 - newSize;
                                            ui.position.left += diff;
                                            return true;
                                        }
                                    }
                                    return true;
                                } else
                                    return false;
                            },
                            stop: function (e, ui) {
                                var table = THIS._splitterTable(this);
                                if (table) {
                                    var td = THIS._splitterCell(this);
                                    if (td) {
                                        var left = ui.position.left; // - $(table.parentNode).position().left;
                                        td.ratio = left / $(table.parentNode).innerWidth();
                                        var component = THIS._splitterComponentForCell(td);
                                        if (component) {
                                            $RWT.actions.event(component, 'up-r', td.ratio.toString(), function () {
                                                THIS._checkCollapsed(table, false);
                                            });
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
            splitter.calcAdjustSize = function() {
                var finalHeight, finalWidth;
                for (i = 0; i < cols.length; i++) {
                    component = THIS._splitterComponentForCell(cols[i]);
                    var adjustSizeNew = $RWT.getAdjustSize(component);
                    if (adjustSizeNew != null) {
                        var adjustWidth = adjustSizeNew.width;
                        if (adjustWidth != null) {
                            finalWidth = adjustWidth + adjustWidth * (ratios[i])/(1 - ratios[i]) + (finalWidth == null ? 0 : finalWidth);
                        }
                        if (finalHeight == null) {
                            finalHeight = adjustSizeNew.height;
                        } else if (adjustSizeNew.height > finalHeight) {
                            finalHeight = adjustSizeNew.height;
                        }
                    }
                }
                return {"width" : finalWidth, "height" : finalHeight, "final" : true};
            };
        }
},
    _splitterTable: function (separator) {
        var p = separator.parentNode;
        while (p) {
            if (WpsUtils.getNodeLocalName(p) == 'table') {
                return p;
            }
            p = p.parentNode;
        }
        return null;
    },
    _nextCell: function (separator) {
        var p = separator.parentNode;
        while (p) {
            if (WpsUtils.getNodeLocalName(p) == 'td') {
                return WpsUtils.findNextSibling(p, 'td');
            }
            p = p.parentNode;
        }
        return null;
    },
    _prevCell: function (separator) {
        var p = separator.parentNode;
        while (p) {
            if (WpsUtils.getNodeLocalName(p) == 'td') {
                return WpsUtils.findPrevSibling(p, 'td');
            }
            p = p.parentNode;
        }
        return null;
    },
    _checkCollapsed: function (table, vertical) {
        var body = WpsUtils.findChildByLocalName(table, 'tbody');
        if (vertical) {

            WpsUtils.iterateNames(body, 'tr', function (tr) {
                if ($(tr).css('display') != 'none') {
                    var cell = WpsUtils.findChildByLocalName(tr, 'td');
                    if (cell) {
                        var component = WpsUtils.findChildByLocalName(cell, 'div');
                        var elems = $RWT.table_layout._components(component);
                        if ($(component).innerHeight() <= $(elems.handle).outerHeight()) {
                            if ($(elems.container).css('display') != 'none') {
                                $RWT.actions.event(component, "collapsed", "true");
                            }
                        } else {
                            if ($(elems.container).css('display') == 'none') {
                                $RWT.actions.event(component, "collapsed", "false");
                            }
                        }
                    }
                }
            });
        } else {
            WpsUtils.iterateNames(body, 'tr', function (tr) {
                WpsUtils.iterateNames(tr, 'td', function (td) {
                    if ($(td).css('display') != 'none') {
                        var component = WpsUtils.findChildByLocalName(td, 'div');
                        var elems = $RWT.table_layout._components(component);
                        if ($(component).innerWidth() <= $(elems.handle).outerWidth()) {
                            if ($(elems.container).css('display') != 'none') {
                                $RWT.actions.event(component, "collapsed", "true");
                            }
                        }
                        else {
                            if ($(elems.container).css('display') == 'none') {
                                $RWT.actions.event(component, "collapsed", "false");
                            }
                        }
                    }
                });
                return true;
            });
        }

    },
    _prevCellForRow: function (separator, nonCollapsed) {
        var p = separator.parentNode;
        while (p) {
            if (WpsUtils.getNodeLocalName(p) == 'tr') {
                var prevRow = WpsUtils.findPrevSibling(p, 'tr');
                while (prevRow) {
                    if ($(prevRow).css('display') != 'none') {
                        if (nonCollapsed) {
                            var component = $RWT.table_layout._splitterComponentForRow(prevRow);
                            if (component) {
                                var elems = $RWT.table_layout._components(component);
                                if ($(elems.container).css('display') != 'none') {
                                    return prevRow;
                                }
                            }
                        } else {
                            return WpsUtils.findFirstChild(prevRow);
                        }
                    }
                    prevRow = WpsUtils.findPrevSibling(prevRow, 'tr');
                }
                return null;
            }
            p = p.parentNode;
        }
        return null;
    },
    _splitterCell: function (separator) {
        var p = separator.parentNode;
        while (p) {
            if (WpsUtils.getNodeLocalName(p) == 'td') {
                return p;
            }
            p = p.parentNode;
        }
        return null;
    },
    _splitterRow: function (separator) {
        var p = separator.parentNode;
        while (p) {
            if (WpsUtils.getNodeLocalName(p) == 'tr') {
                return p;
            }
            p = p.parentNode;
        }
        return null;
    },
    _splitterComponentForRow: function (row) {
        var cell = WpsUtils.findChildByLocalName(row, 'td');
        if (cell)
            return WpsUtils.findFirstChild(cell);
        else
            return null;
    },
    _splitterComponentForCell: function (cell) {
        return WpsUtils.findFirstChild(cell);
    },
    _components: function (cell) {
        var result = {};
        WpsUtils.iterateAll(cell, function (div) {
            if (result.handle) {
                result.container = div;
                return true;
            }
            else
                result.handle = div;
            return false;
        }


        );
        return result;
    }
}
$RWT.componentGrid = {
    _min_height: function (grid) {
        var table = WpsUtils.findFirstChild(grid);
        if (table) {
            return $(table).outerHeight() + 5;
        } else
            return 100;
    },
    layout: function (grid) {
        $RWT.componentGrid.adjustHeight(grid);
        var table = WpsUtils.findFirstChild(grid);
        if (table) {
            $RWT.componentGrid._iterateRows(table, function (tr) {
                var max_content_height = 0;
                var property_groups = [];                
                $RWT.componentGrid._iterateRowCells(tr, function (td) {
                    var cellContent = WpsUtils.findFirstChild(td);
                    var space = $(grid).attr('spacing');
                    if (cellContent!=null && cellContent.tagName == "LABEL") {
                        if (Number(space) <= 0) {
                            space = null;
                            $(grid).attr('spacing', null);
                            $(td).css('padding-right', 0);
                        } else {
                            $(td).css('padding-right', space);
                        }
                    }
                });
                for (var i = 0; i < property_groups.length; i++) {
                    $(property_groups[i]).height(max_content_height);
                }
            });
        }
        grid.calcAdjustSize = function() {
            var table = WpsUtils.findFirstChild(grid);
            if (table) {
                $RWT._layout_set[table.id] = null;
                var adjustWidth = 0;
                var width = 0;
                var height = 0;
                if ($(grid).attr('isAdjustWidth') == 'true') {
                    var tbody = WpsUtils.findFirstChild(table);
                    if (tbody != null) {
                        $RWT._layout_set[tbody.id] = null; 
                    }
                    $RWT.componentGrid._iterateRows(table, function (tr) {
                        var rowAdjustWidth = 0;
                        $RWT._layout_set[tr.id] = null;
                        
                        function concatInputBoxWidth(elem) {
                            $RWT._layout_set[elem.id] = null;
                            if ($(elem).css('display') != 'none') {
                                var inputBox = WpsUtils.findFirstChild(elem);
                                if (inputBox && $(inputBox).css('display') != 'none') {                                
                                    $RWT._layout_set[inputBox.id] = null;
                                    if ($(inputBox).hasClass("rwt-input-box") && !$(inputBox).hasClass("rwt-check-box")) {
                                        var textArea = $(inputBox).find("td")[0];
                                        if ($(textArea).hasClass('rwt-value-icon')) {
                                            textArea = $(inputBox).find("td")[1];
                                        }
                                        var propEditorDiff = $(WpsUtils.findFirstChild(inputBox)).width() - $(inputBox).width(); //diff between propEditor and it's inner table
                                        propEditorDiff = propEditorDiff > 0 ? propEditorDiff : 0;
                                        if (textArea != null && ($(textArea).width() - propEditorDiff) < 80) {
                                            rowAdjustWidth += (80 - $(textArea).width() + propEditorDiff);
                                            recFunc(textArea);
                                        }
                                    }
                                }
                            }
                        }
                        
                        function recFunc(elem) {
                            $RWT._layout_set[elem.id] = null;
                            WpsUtils.iterateAll(elem, function(c) {
                                recFunc(c);
                            });
                        }
                        
                        $RWT.componentGrid._iterateRowCells(tr, function (td) {
                            $RWT._layout_set[td.id] = null;
                            var cellContent = WpsUtils.findFirstChild(td);
                            if (cellContent != null) {    
                                concatInputBoxWidth(cellContent);
                            }
                        });
                        if (rowAdjustWidth > adjustWidth) {
                            adjustWidth = rowAdjustWidth;
                        }
                    });
                } 
                var propertiesGrid = table.parentNode;
                if ($(grid).attr('isAdjustWidth') == 'true') {
                    width = $(table).width() - $(propertiesGrid).width();
                }
                if ($(grid).attr('isAdjustHeight') == 'true') {
                    height = $(table).height() - $(propertiesGrid).height();
                }
                return {"width" : width > 0 ? width + adjustWidth : adjustWidth, "height" : height > 0 ? height : null, "final" : true};
            }
            else return {"width" : null, "height" : null, "final" : true};
        }
    },
    _iterateRows: function (table, f) {//iterate header column cells
        var tbody = $RWT.componentGrid._getDataBody(table);
        if (tbody) {
            if (WpsUtils.iterateNames(tbody, 'tr', f) === true) {
                return;
            }
        }
    },
    _iterateRowCells: function (tr, f) {
        WpsUtils.iterateNames(tr, 'td', function (td) {
            if ($(td).css('display') != 'none') {
                return f(td) === true;
            }
        });
    },    
    _getDataBody: function (table) {
        return WpsUtils.findChildByLocalName(table, 'tbody');
    },    
    adjustHeight: function (grid) {
        if (!grid.rwt_min_height) {
            grid.rwt_min_height = $RWT.componentGrid._min_height;
        }
        if ($(grid).attr('adjustHeight') == 'true') {
            $(grid).height($RWT.componentGrid._min_height(grid));
        }
    }
}

$RWT.propertiesGrid = {
    BORDER_SPACING: 2,
    CELL_PADDING: 1,
    LABEL_CELL_LEFT_PADDING: 3,
    GROUP_BOX_BORDER_WIDTH: 1,
    
    layout: function (grid) {
        $RWT.componentGrid.layout(grid);
        var table = WpsUtils.findFirstChild(grid);
        if (table) {
            table.innerTables = [];
            table.columnsCount = 0;            
            $RWT.propertiesGrid._iterateRows(table, function (tr) {
                var max_content_height = 0;
                var property_groups = [];
                var column = 0;
                tr.containsSpanCell = false;
                $RWT.componentGrid._iterateRowCells(tr, function (td) {
                    var cellContent = WpsUtils.findFirstChild(td);
                    var spanAttr = td.getAttribute('colspan');
                    var colSpan = spanAttr!=null;
                    if (colSpan){
                        tr.containsSpanCell = true;
                    }
                    td.role = td.getAttribute('role');
                    td.span = spanAttr==null ? 0 : parseInt(spanAttr) || 0;
                    if (cellContent!=null 
                        && cellContent.nodeType == 1
                        && td.role == 'group'){
                        if ($(cellContent).attr('adjustMode')=='heightByContent'){
                            $RWT.groupBox.layout(cellContent);
                            var height = $(cellContent).innerHeight();
                            if (max_content_height<height){
                                max_content_height = height;
                            }
                        }else{
                            property_groups.push(cellContent);
                        }
                        var innerTableId = td.getAttribute('innertable');
                        if (innerTableId!=null){
                            var elements = $('#' + innerTableId);
                            var innerTable = elements == null || elements.length != 1 ? null : elements[0];
                            if (innerTable!=null){                                    
                                innerTable.externalSize = td.span;
                                if (innerTable.externalSize>1){
                                    innerTable.holderStartColumn = column;
                                    innerTable.holderEndColumn = 
                                        Math.min(column+innerTable.externalSize,table.columnsCount) - 1;
                                    innerTable.holderTable = table;
                                    var arrTables  = table.innerTables[column];
                                    if (arrTables==null){
                                        arrTables = [];
                                        table.innerTables[column] = arrTables;
                                    }
                                    arrTables.push(innerTable);
                                }
                            }
                        }                        
                    }
                    column++;
                });
                for (var i = 0; i < property_groups.length; i++) {
                    $(property_groups[i]).height(max_content_height);
                }
                table.columnsCount = Math.max(table.columnsCount, column);
            });
            if (table.holderTable!=null || table.innerTables.length>0){
                if (table.holderStartColumn!=null){
                    $RWT.propertiesGrid._buildColumnsMap(table);
                }
                $(table).attr('table-layout','fixed');//manually set width to columns
                table.columnsPadding = [];//left padding for label cell and right padding for editor cell
                for (var i = 0; i < table.columnsCount; i++) {
                    if (i % 2 == 0){
                        table.columnsPadding[i] = $RWT.propertiesGrid.LABEL_CELL_LEFT_PADDING;
                    }else{
                        table.columnsPadding[i] = $RWT.propertiesGrid.CELL_PADDING;
                    }
                }
                table.columns = [];
                var column = 0;
                WpsUtils.iterateNames(table, 'col', function(col){
                    table.columns[column] = col;
                    column++;
                    return false;
                });
                grid.rwt_rnd_refine = $RWT.propertiesGrid._afterLayout;
            }
        }
    },
    _buildColumnsMap: function(table){
        //mapping column index from outer table to column index from inner table
        table.columnsMap = [];
        if (table.externalSize==table.columnsCount 
            && (table.holderTable.columnsCount<=table.holderStartColumn+table.externalSize)){                
            for (var i=0; i<table.holderTable.columnsCount; i++){
                if (i<table.holderStartColumn || i >= (table.holderStartColumn + table.externalSize)){
                    table.columnsMap[i] = -1;
                }else{
                    table.columnsMap[i] = i - table.holderStartColumn;
                }
            }
        }else{
            for (var i=0; i<table.holderTable.columnsCount; i++){
                table.columnsMap[i] = i==table.holderStartColumn ? 0 : -1;
            }
        }        
    },
    _iterateRows: function (table, f) {//iterate header column cells
        var tbody = WpsUtils.findChildByLocalName(table, 'tbody');
        if (tbody) {
            if (WpsUtils.iterateNames(tbody, 'tr', f) === true) {
                return;
            }
        }
    },
    _iterateRowCells: function (tr, f) {
        WpsUtils.iterateNames(tr, 'td', function (td) {
            if ($(td).css('display') != 'none') {
                return f(td) === true;
            }
        });
    },
    _iterateTableCells: function (table, f){
        var column = 0;
        $RWT.propertiesGrid._iterateRows(table, function (tr) {
            column = 0;
            $RWT.propertiesGrid._iterateRowCells(tr, function (td) {
                f(tr, column, td);
                column++;
            });
        });
    },
    _iterateInnerTables: function (table, f) {
        for (var column=0; column<table.columnsCount; column++){
            var arrTables = table.innerTables[column];
            if (arrTables!=null){
                var innerTable;
                for (var i=0; i<arrTables.length; i++){
                    innerTable = arrTables[i];
                    if (innerTable!=null){
                        f(innerTable);
                    }
                }
            }
        }
    },
    _afterLayout: function(grid){
        var table = WpsUtils.findFirstChild(grid);
        if (table) {
            $RWT.propertiesGrid._calcColumnsWidth(table);
            if (table.holderStartColumn==null){//topmost table
                $RWT.propertiesGrid._applyColumnsWidth(table, null);
            }
        }
        return null;
    },
    _calcColumnsWidth: function(table){
        var columnsWidth = [];
        for (var column=0; column<table.columnsCount; column++){
            columnsWidth[column] = -1;
        }
        
        $RWT.propertiesGrid._iterateTableCells(table, function(tr, column, td){
            if (column % 2 == 0 && 'label'==td.role){
                columnsWidth[column] = Math.max(columnsWidth[column], $RWT.propertiesGrid._getCellWidth(td));
            }            
        });
             
        var maxInnerTablesRightPadding = [];
        for (var column=0; column<table.columnsCount; column++){
            maxInnerTablesRightPadding[column] = -1;
        }
        
        var innerTableOffset = 
            $RWT.propertiesGrid.BORDER_SPACING + $RWT.propertiesGrid.CELL_PADDING + $RWT.propertiesGrid.GROUP_BOX_BORDER_WIDTH;
            
        for (var column=0; column<table.columnsCount; column++){
            var arrTables = table.innerTables[column];            
            if (arrTables!=null){
                var innerTable;
                var maxLeftPadding = 0;
                for (var i=0; i<arrTables.length; i++){
                    innerTable = arrTables[i];
                    if (innerTable!=null){
                        var innerTableColumnsWidth = $RWT.propertiesGrid._unmapColumnsWidth(table, innerTable);
                        for (var j=0; j<table.columnsCount; j++){
                            columnsWidth[j] = Math.max(columnsWidth[j], innerTableColumnsWidth[j]);
                        }
                        if (innerTable.columnsCount>0){
                            maxLeftPadding = Math.max(maxLeftPadding, innerTable.columnsPadding[0]);
                            var endColumn = innerTable.holderEndColumn;
                            var rightPadding = innerTable.columnsPadding[innerTable.columnsCount-1];
                            maxInnerTablesRightPadding[endColumn] = 
                                Math.max(maxInnerTablesRightPadding[endColumn], rightPadding);
                        }
                    }
                }
                table.columnsPadding[column] = maxLeftPadding + innerTableOffset;
            }
        }
        
        for (var column=1; column<table.columnsCount; column+=2){
            if (maxInnerTablesRightPadding[column]>0){
                table.columnsPadding[column]=maxInnerTablesRightPadding[column]+innerTableOffset;
            }
        }
                
        table.columnsWidth = columnsWidth;
        return columnsWidth;
    },
    _applyColumnsWidth: function(table, outerColumnsWidth){
        
        if (outerColumnsWidth!=null){
            for (var column=0; column<table.columnsCount; column+=2){
                table.columnsWidth[column] = Math.max(table.columnsWidth[column], outerColumnsWidth[column]);
            }
        }
        
        var editorsWidth = table.parentNode.clientWidth;
        for (var column=0; column<table.columnsCount; column++){
            if (column % 2==0){
                editorsWidth -= table.columnsWidth[column];
            }            
            editorsWidth -= (table.columnsPadding[column]  + $RWT.propertiesGrid.CELL_PADDING);            
        }
        
        editorsWidth-=$RWT.propertiesGrid.BORDER_SPACING*(table.columnsCount+1);
        
        var editorsCount = table.columnsCount/2;
        var editorWidth = editorsWidth/editorsCount;
        
        for (var column=1; column<table.columnsCount; column+=2){
            table.columnsWidth[column] = editorWidth;
        }
        
        for (var column=0; column<table.columnsCount; column++){
            var width = table.columnsWidth[column] + table.columnsPadding[column]+$RWT.propertiesGrid.CELL_PADDING;
            if (table.columns[column].alignedWidth!==width){
                table.columns[column].alignedWidth = width;
                $(table.columns[column]).width(width);
            }
        }
        
        $RWT.propertiesGrid._iterateTableCells(table, function(tr, column, td){
            var cellContent = WpsUtils.findFirstChild(td);
            if (cellContent!=null && cellContent.nodeType == 1){
                if ('label'==td.role){
                    var padding = table.columnsPadding[column];
                    $RWT.propertiesGrid._setCellPadding(td, padding, -1);
                }else if ('editor'==td.role){
                    var padding;
                    if (td.span>1){
                        padding = table.columnsPadding[column+td.span-1];
                    }else{
                        padding = table.columnsPadding[column];
                    }
                    var width = tr.containsSpanCell==false ? table.columnsWidth[column] : -1;
                    $RWT.propertiesGrid._setCellPadding(td, -1, padding);
                }
            }
        });
        
        $RWT.propertiesGrid._iterateInnerTables(table, function(innerTable){
            var mappedColumnsWidth = $RWT.propertiesGrid._mapColumnsWidth(table, innerTable);
            $RWT.propertiesGrid._applyColumnsWidth(innerTable, mappedColumnsWidth);
        });        
    },
    _mapColumnsWidth: function(outerTable, innerTable){
        var mappedColumnsWidth = [];
        for (var column=0; column<innerTable.columnsCount; column++){
            mappedColumnsWidth[column] = -1;
        }
        var innerColumnIndex;        
        for (var column=0; column<outerTable.columnsCount; column++){           
            innerColumnIndex = innerTable.columnsMap[column];
            if (innerColumnIndex>=0){
                mappedColumnsWidth[innerColumnIndex] = outerTable.columnsWidth[column];
            }
        }
        return mappedColumnsWidth;
    },
    _unmapColumnsWidth: function(outerTable, innerTable){
        var columnsWidth = [];
        var innerColumnIndex;
        for (var column=0; column<outerTable.columnsCount; column++){
            innerColumnIndex = innerTable.columnsMap[column];
            if (innerColumnIndex>=0){
                columnsWidth[column] = innerTable.columnsWidth[innerColumnIndex];
            }else{
                columnsWidth[column] = -1;
            }
        }
        return columnsWidth;
    },    
    _getCellWidth: function(td){
        if (td==null){
            return -1;
        }else{
            var cellContent = WpsUtils.findFirstChild(td);            
            if (cellContent!=null && cellContent.nodeType == 1){
                return cellContent.offsetWidth;
            }else{
                return -1;
            }
        }
    },
    _setCellPadding: function(td, paddingLeft, paddingRight){
        if (paddingLeft>=0 && td.alignedPaddingLeft!=paddingLeft){
            td.alignedPaddingLeft = paddingLeft;
            $(td).css("padding-left",paddingLeft+'px');
        }
        if (paddingRight>=0 && td.alignedPaddingRight!=paddingRight){
            td.alignedPaddingRight = paddingRight;
            if (paddingRight==1){
                $(td).css("padding-right",null);
            }else{
                $(td).css("padding-right",paddingRight+'px');
            }
        }
    }
}

$RWT.table_layout = $_RWT_TABLE_LAYOUT_OBJ;
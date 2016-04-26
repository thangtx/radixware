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
                        var part = (nextRatio - selfRatio) * h - dec;
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
                                    var top = ui.position.top;// - $(table.parentNode).position().top;
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

                    part = (nextRatio - selfRatio) * w;
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
                                        var left = ui.position.left;// - $(table.parentNode).position().left;
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

$RWT.table_layout = $_RWT_TABLE_LAYOUT_OBJ;
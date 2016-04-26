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
$_GF = _rwt_grid_flow = {
    _getDataTable: function(b) {//get data table
        return WpsUtils.findChildByLocalName(b, 'table');
    },
    _getHeaderTable: function(h) {//get data table
        return WpsUtils.findChildByLocalName(h, 'table');
    },
    _getDataCols: function(dt) {
        var cs = {};
        cs.c = 0;
        WpsUtils.iterateNames(dt, 'col', function(c) {
            cs[cs.c] = c;
            cs.c++;
        });
        return cs;
    },
    _getDataBody: function(dt) {
        return WpsUtils.findChildByLocalName(dt, 'tbody');
    },
    _shadeEvenRows: function(table) {
        var opAttr = table.getAttribute("alpha");
        $(table).find('tr').removeAttr("even");
        if (opAttr > 0) {
            $(table).find('tr:even').attr('even', true);
            $_GF._iterateRows(table, function(tr) {
                if ($(tr).attr("even")) {
                    $(tr).each(function(i, row) {
                        $_GF._iterateRowCells(row, function(cell) {
                            var color = $(cell).css('background-color');
                            var sc = $(cell).attr('savecolor');
                            if (!$(cell).hasClass('rwt-ui-selected-item') && color != sc/* && !$(cell).attr('colspan')>1*/) {
                                if (color) {
                                    var c;
                                    if (color.indexOf('a') > -1) {
                                        if (!$(cell).attr('colspan')) {
                                            c = color.replace(/[^,]+(?=\))/, opAttr / 100);
                                        }
                                        else {
                                            c = color.replace(/[^,]+(?=\))/, 0);//temporary bf
                                        }
                                    } else {
                                        c = color.replace(')', ', ' + opAttr / 100 + ')').replace('rgb', 'rgba');
                                    }
                                    if (color != c)
                                        $(cell).css('background-color', c);
                                }
                            }
                        });
                    });
                }
            });
        }
    },
    _iterateRows: function(dt, f) {//iterate header column cells
        var b = $_GF._getDataBody(dt);
        if (b) {
            if (WpsUtils.iterateNames(b, 'tr', f) === true) {
                return;
            }
        }
    },
    _iterateRowCells: function(tr, f) {
        WpsUtils.iterateNames(tr, 'td', function(td) {
            if ($(td).css('display') != 'none') {
                if (f(td) === true) {
                    return  true;
                } else
                    return false;
            }
        });
    },
    _nextRow: function(row) {
        return WpsUtils.findNextSibling(row, 'tr');
    },
    _prevRow: function(row) {
        return WpsUtils.findPrevSibling(row, 'tr');
    },
    _currentRow: function(cell) {
        return cell == null ? null : cell.parentNode;
    },
    _cellInNextRow: function(cell) {
        var row = $RWT.grid._currentRow(cell);
        if (row) {
            var index = $RWT.grid._cellIndex(cell);
            if (index >= 0) {
                var next = $RWT.grid._nextRow(row);
                if (next) {
                    return $RWT.grid._cellByIndex(next, index);
                }
            }
        }
        return null;
    },
    _cellInPrevRow: function(cell) {
        var row = $RWT.grid._currentRow(cell);
        if (row) {
            var index = $RWT.grid._cellIndex(cell);
            if (index >= 0) {
                var prev = $RWT.grid._prevRow(row);
                if (prev) {
                    return $RWT.grid._cellByIndex(prev, index);
                }
            }
        }
        return null;
    },
    _nextCell: function(cell) {
        var row = $RWT.grid._currentRow(cell);
        if (row) {
            var index = $RWT.grid._cellIndex(cell);
            return $RWT.grid._cellByIndex(row, index + 1);
        }
        return null;
    },
    _prevCell: function(cell) {
        var row = $RWT.grid._currentRow(cell);
        if (row) {
            var index = $RWT.grid._cellIndex(cell);
            if (index > 0) {
                return $RWT.grid._cellByIndex(row, index - 1);
            }

        }
        return null;
    },
    _cellIndex: function(cell) {
        var row = $RWT.grid._currentRow(cell);
        if (row) {
            var index = 0;
            WpsUtils.iterateNames(row, 'td', function(c) {
                if ($(c).css('display') != 'none') {
                    if (c == cell) {
                        return true;
                    } else {
                        index++;
                        return false;
                    }
                }
            });
            return index;
        }
        return -1;
    },
    onHeaderClick: function(event) {
        if (event.button == 0) {
            $RWT.actions.event(this, 'click', $RWT.events.getButtonsMask(event).toString());
        }
    },
    onContextMenu: function(event) {
        $RWT.events.cancelEvent(event);
        $RWT.actions.event(this, 'click', $RWT.events.getButtonsMask(event).toString());
        return false;
    },
    _cellByIndex: function(row, idx) {
        if (row) {
            var index = 0;
            var cell = null;
            WpsUtils.iterateNames(row, 'td', function(c) {
                if ($(c).css('display') != 'none') {
                    if (index == idx) {
                        cell = c;
                        return true;
                    }
                    index++;
                    return false;
                }

            });
            return cell;
        }
        return null;
    },
    _syncScroll: function(e) {
        var h = WpsUtils.findChildByLocalName(this.parentNode, 'div');
        var table = $_GF._getHeaderTable(h);
        $(table).css('left', -this.scrollLeft + 'px');
        //h.scrollLeft = this.scrollLeft;   

        $_GF._dataScroll(this);
    },
    _layout: function(grid, rr_rs, rqId) {
        var stickToRight = $(grid).attr('stick') === '1';
        grid.blockScroll = true;
        try{
            $RWT.gridLayout.doLayout(grid,stickToRight,null);//see client.js
            grid.rwt_rnd_refine = $_GF._sizeCheck;
        }finally{
            grid.blockScroll = false;
        }
    },    
    _dataScroll: function(table) {
        if ($_GF._shouldReadMore(table.parentNode) && 
            (table.parentNode.blockScroll==null || !table.parentNode.blockScroll)) {
            var dt = $_GF._getDataTable(table);
            if (dt) {
                var myHeight = Math.round($(table).innerHeight() + table.scrollTop);
                if (myHeight >= dt.offsetHeight) {
                    var rr_rs = {};
                    rr_rs[table.parentNode.id] = 'm';
                    table.parentNode.blockScroll = true
                    $RWT.notifyRendered(rr_rs);
                }
            }
        }
    },
    _shouldReadMore: function(grid) {
        return $(grid).attr("more-rows") === 'true';
    },
    _sizeCheck: function(grid) {
        if ($_GF._shouldReadMore(grid)) {
            var cts = $RWT.gridLayout.splitToComponents(grid);
            if (!cts)
                return null;
            var dt = $RWT.gridLayout.getDataTable(cts.b);
            if (!dt)
                return null;
            if (cts.b.offsetHeight > dt.offsetHeight) {
                return 'm';
            } else
                return null;
        } else
            return null;
    },
    mouseIn: function() {
        $(this).addClass('rwt-grid-data-row-active');
    },
    mouseOut: function() {
        $(this).removeClass('rwt-grid-data-row-active');
    },
    cell: {
        click: function(e) {
            var node = this;
            $RWT.grid.cell.onClick(this, function() {
                if (node.className && node.className.indexOf("editor-cell") >= 0) {
                    var cc = WpsUtils.findChildByLocalName(node, 'div');//container
                    if (cc) {
                        cc = WpsUtils.findChildByLocalName(cc, 'div');//editor
                        if (cc) {
                            if (cc.rwt_f_requestFocus) {
                                cc.rwt_f_requestFocus(cc);
                            } else {
                                $(cc).focus();
                            }
                        }
                    }
                }
            });
        },
        dblclick: function(e) {
            if (!this.className || this.className.indexOf("editor-cell") < 0) {
                $RWT.actions.event(this, 'dblclick', null, null);
            }
        },
        onClick: function(cell, callback) {
            if (!cell.className || cell.className.indexOf("editor-cell") < 0) {
                $(cell).focus();
                $RWT.actions.event(cell, 'click', null, callback);
            }
            else {
                $RWT.actions.event(cell, 'click', null, callback);
            }
        },
        keyDown: function(e) {
            if (e) {
                switch (e.keyCode) {
                    case 27://esc
                        if (this.className.indexOf("editor-cell") >= 0 && this !== document.activeElement) {
                            //do not apply changes, close editor
                            e.preventDefault();
                            $(this).focus();
                            $RWT.actions.event(this, "key", e.keyCode.toString(), function() {

                            });
                            break;
                        }
                        else {
                            $(this).focus();
                            break;//close dialog
                        }

                    case 113://F2
                    case 13://enter

                        e.preventDefault();
                        var node = this;
                        if (this === document.activeElement) {
                            $RWT.grid.cell.onClick(node, function() {
                                if (node.className && node.className.indexOf("editor-cell") >= 0) {
                                    $RWT.actions.event(this, "key", e.keyCode.toString(), function() {
                                        if (node.className && node.className.indexOf("editor-cell") >= 0) {
                                            var cc = WpsUtils.findChildByLocalName(node, 'div');//container
                                            if (cc) {
                                                cc = WpsUtils.findChildByLocalName(cc, 'div');
                                                if (cc) {
                                                    if (document.activeElement === node)
                                                    {
                                                        if (cc.rwt_f_requestFocus) {
                                                            cc.rwt_f_requestFocus(cc);
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            $(node).focus();
                                        }
                                    });
                                }
                            });
                        }
                        else {
                            $(this).focus();
                            $RWT.actions.event(this, "key", e.keyCode.toString(), function() {
                            });
                        }//second enter => close editor
                        break;

                    case 9://tab
                        e.preventDefault();
                        var node = this;
                        var currRow = $RWT.grid._currentRow(node);
                        var nextRow;
                        var cell;

                        if (!e.shiftKey) {//tabulation
                            cell = $RWT.grid._nextCell(node);
                            nextRow = $RWT.grid._nextRow(currRow);
                            if (!cell) {
                                var firstCell = $RWT.grid._cellByIndex(nextRow, 0);
                            }
                            else {
                                firstCell = cell;
                            }
                        }
                        else {//reverse tabulation (shift+tab)
                            cell = $RWT.grid._prevCell(node);
                            nextRow = $RWT.grid._prevRow(currRow);
                            if (!cell) {
                                firstCell = nextRow.lastElementChild;
                            }
                            else
                                firstCell = cell;
                        }

                        $RWT.actions.event(firstCell, 'click', null, null);
                        $RWT.grid.cell.onClick(firstCell, function() {
                            $RWT.actions.event(firstCell, "key", e.keyCode.toString(), function() {
                                $(firstCell).focus();
                            });
                        });
                        break;

                    case 37://left

                        if (this.className.indexOf("editor-cell") >= 0 && this !== document.activeElement) {
                            break;
                        }
                        else {
                            e.preventDefault();
                            var next = this.previousElementSibling;
                            if (next)
                                $RWT.grid.cell.onClick(next, function() {
                                    $(next).focus();
                                });
                        }
                        break;

                    case 39://right

                        if (this.className.indexOf("editor-cell") >= 0 && this !== document.activeElement) {
                            break;
                        }
                        else {
                            e.preventDefault();
                            var next = this.nextElementSibling;
                            if (next)
                                $RWT.grid.cell.onClick(next, function() {
                                    $(next).focus();
                                });
                        }
                        break;

                    case 40://down

                        if (this.className.indexOf("editor-cell") >= 0 && this !== document.activeElement) {
                            break;
                        }
                        else {
                            e.preventDefault();
                            var next = $RWT.grid._cellInNextRow(this);
                            if (next)
                            {
                                $RWT.grid.cell.onClick(next, function() {
                                    $(next).focus();
                                });
                            }
                        }
                        break;

                    case 38://up

                        if (this.className.indexOf("editor-cell") >= 0 && this !== document.activeElement) {
                            break;
                        }
                        else {
                            e.preventDefault();
                            var next = $RWT.grid._cellInPrevRow(this);
                            if (next)
                            {
                                $RWT.grid.cell.onClick(next, function() {
                                    $(next).focus();
                                });
                            }
                        }
                        break;

                    case 35://end
                    case 36://home                        
                        e.preventDefault();
                        $RWT._ce(e);
                        break;
                    default:
                        $RWT._ce(e);
                }
            }
        }
    }
}
$_GF._layout.ignoreChildren = true;
$RWT.grid = $_GF; 
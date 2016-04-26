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
$RWT.tree = {
    mouseIn: function() {
        $(this).addClass('rwt-tree-node-active');
    },
    mouseOut: function() {
        $(this).removeClass('rwt-tree-node-active');
    },
    onLeafClick: function() {
        if (this.className != null &&
                this.className.indexOf('rwt-tree-node-indicator') < 0 &&
                this.className.indexOf('editor-cell') < 0) {
            $(this).focus();
            $RWT.actions.event(this, 'click');
        }
    },
    onLeafDblClick: function() {
        if (this.className != null &&
                this.className.indexOf('rwt-tree-node-indicator') < 0 &&
                this.className.indexOf('editor-cell') < 0) {
            $(this).focus();
            $RWT.actions.event(this, 'dblclick');
        }
    },
    onHeaderClick: function(event) {
        $RWT.actions.event(this, 'click', $RWT.events.getButtonsMask(event).toString());
    },
    onContextMenu: function(event) {
        $RWT.events.cancelEvent(event);
        $RWT.actions.event(this, 'click', $RWT.events.getButtonsMask(event).toString());
        return false;
    },
    _setCellWidth: function(grid,columnIndex,cell,width){
        cell.tree = grid;
        var w = width;
        var editorOrRenderer;
        if (columnIndex==0){
            var table = WpsUtils.findChildByLocalName(cell, 'table');
            var tbody = WpsUtils.findChildByLocalName(table, 'tbody');
            var tr = WpsUtils.findChildByLocalName(tbody, 'tr');
            var cell1 = WpsUtils.findChildByLocalName(tr, 'td');
            var cell2 = WpsUtils.findNextSibling(cell1, 'td');
            editorOrRenderer = WpsUtils.findChildByLocalName(cell2, 'div');
            if (width!=null)
                w -= $(cell1).outerWidth();
        }else{
            editorOrRenderer = WpsUtils.findChildByLocalName(cell, 'div');
        }
        if (w==null){
            $(editorOrRenderer).css('width','100%');
        }else{
            $(editorOrRenderer).width(w);
        }
    },
    _shadeEvenRows: function(table) {
        var opAttr = table.getAttribute("alpha");
        var tbody = WpsUtils.findFirstChild(table);
        $(table).find('tr:even:even').attr('even', 'true');

        if (tbody && opAttr > 0) {
            WpsUtils.iterateNames(tbody, 'tr', function(tr) {
                if ($(tr).attr("even") == 'true') {
                    WpsUtils.iterateNames(tr, 'td', function(cell) {
                        var color = cell.style.backgroundColor;
                        var sc = $(cell).attr('savecolor');
                        if (color != sc) {
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
                                    c = color.replace(')', ',' + opAttr / 100 + ')').replace('rgb', 'rgba');
                                }
                                cell.style.backgroundColor = c;
                            }
                        }
                    });
                }
            }
            );
        }
    },
    _syncScroll: function(e) {
        var h = WpsUtils.findChildByLocalName(this.parentNode, 'div');
        var table = WpsUtils.findChildByLocalName(h, 'table');
        $(table).css('left', -this.scrollLeft + 'px');
    },
    layout: function(tree) {
        $RWT.gridLayout.doLayout(tree, true, $RWT.tree._setCellWidth);//see client.js
        var editor = $RWT.tree._findFilterEditor(tree);
        if (editor!=null){
            $RWT.tree._initFilterEditor(editor, tree);
        }        
    },
    _initFilterEditor: function(editor, tree){
        var input = $RWT.inputBox.findInput(editor);
        if (input!=null){
            $(input).keyup(function() {
                                $RWT.delay(function(){
                                        $RWT.actions.event(tree, 'filter', input.value);
                                       }, 500 );
            });
        }
    },
    _findFilterEditor: function(tree){
        var filterEditorId = tree.getAttribute('filtereditor');
        if ( filterEditorId!=null ){
            var editors = $('#' + filterEditorId);
            if (editors != null && editors.length > 0) {
                return editors[0];
            }
        }
        return null;
    },
    _findEditor: function(tree) {
        var editorId = tree.getAttribute('editor');
        if (editorId != null) {
            var editor = $('#' + editorId);
            if (editor != null && editor.length > 0) {
                return editor[0];
            }
        }
        return null;
    },
    node: {
        keyDown: function(e) {
            switch (e.keyCode) {
                case 27:
                    var node = this;
                    var code = e.keyCode;
                    var cc = $RWT.tree._findEditor(node.tree);
                    if (cc == null)
                        return;

                case 13:
                    if (e)
                        e.preventDefault();
                    $(this).focus();

                    $RWT.actions.event(this, "key", e.keyCode.toString(), function() {
                        if (code == 13 && node.className && node.className.indexOf("editor-cell") >= 0) {

                            var cc = $RWT.tree._findEditor(node.tree);
                            if (cc != null) {
                                if (cc.rwt_f_requestFocus) {
                                    cc.rwt_f_requestFocus(cc);
                                } else {
                                    $(cc).focus();
                                }
                                return;
                            } else
                                $(node).focus();
                        }
                    });
                    break;
                case 37://left
                    if (e)
                        e.preventDefault();
                    var prev = $RWT.tree.node._prevCell(this);
                    if (prev != null && prev.onclick) {
                        prev.onclick();
                        $(prev).focus();
                    }
                    break;
                case 38://up;
                    if (e)
                        e.preventDefault();
                    var colIndex = $RWT.tree.node._colIndex(this);
                    prev = $RWT.tree.node._prevItem(this, colIndex);

                    if (prev == null)
                        prev = $RWT.tree.node._parentItem(this, colIndex);
                    if (prev != null && prev.onclick) {
                        prev.onclick();
                        $(prev).focus();
                    }
                    break;
                case 39://right
                    if (e)
                        e.preventDefault();
                    var next = $RWT.tree.node._nextCell(this);
                    if (next != null && next.onclick) {
                        next.onclick();
                        $(next).focus();
                    }
                    break;
                case 40://down;
                    if (e)
                        e.preventDefault();
                    colIndex = $RWT.tree.node._colIndex(this);
                    next = $RWT.tree.node._nextItem(this, colIndex);
                    if (next == null) {
                        prev = $RWT.tree.node._parentItem(this);
                        if (prev) {
                            next = $RWT.tree.node._nextItem(prev, colIndex);
                        }
                    }
                    if (next != null && next.onclick) {
                        next.onclick();
                        $(next).focus();
                    }
                    break;
            }
        },
        _nextItem: function(node, colIndex) {
            var ownR = node.parentNode;
            var pid = ownR.getAttribute('pid');

            var nextTr = WpsUtils.findNextSibling(ownR, 'tr');
            while (nextTr != null) {

                if ($(nextTr).css('display') != 'none') {
                    return $RWT.tree.node._cellAtCol(nextTr, colIndex);
                }
                ownR = nextTr;
                nextTr = WpsUtils.findNextSibling(ownTr, 'tr');
            }
            return null;
        },
        _nextCell: function(node) {
            return WpsUtils.findNextSibling(node, 'td');
        },
        _prevCell: function(node) {
            return WpsUtils.findPrevSibling(node, 'td');
        },
        _colIndex: function(node) {
            var index = 0;
            var prev = WpsUtils.findPrevSibling(node, 'td');
            while (prev != null) {
                index++;
                prev = WpsUtils.findPrevSibling(prev, 'td');
            }
            return index;
        },
        _prevItem: function(node, colIndex) {
            var tr = node.parentNode;
            if (tr == null)
                return null;
            var prevTr = WpsUtils.findPrevSibling(tr, 'tr');
            while (prevTr != null) {
                if ($(prevTr).css('display') != 'none') {
                    return $RWT.tree.node._cellAtCol(prevTr, colIndex);
                } else {
                    tr = prevTr;
                    prevTr = WpsUtils.findPrevSibling(tr, 'tr');
                }
            }
            return null;
        },
        _cellAtCol: function(row, colIndex) {
            if (colIndex == null || colIndex == 0) {
                return WpsUtils.findFirstChild(row);
            } else {
                var first = WpsUtils.findFirstChild(row);
                var index = 0;
                var next = first;
                var prev = first;
                while (index < colIndex) {
                    next = $RWT.tree.node._nextCell(next);
                    index++;
                }
                return next;
            }
        },
        _parentItem: function(node, colIndex) {
            var parent = $('#' + node.parentNode.getAttribute('pid'));
            if (parent.length > 0) {
                return $RWT.tree.node._cellAtCol(parent[0], colIndex);
            } else
                return null;
        }
    }

}
$RWT.tree.layout.ignoreChildren = false;



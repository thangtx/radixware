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

$RWT.widgetsarea = {
}

$RWT.widgetsarea.tab = {
    layout: function (tab) {
        if (tab.oldWidth != $(tab).outerWidth() || tab.oldHeight != $(tab).outerHeight()) {
            var gridDiv = $(tab).parent().parent().find(".grid")[0];
            if (gridDiv != null) {
                $(gridDiv).remove();
            }
            tab.oldWidth = $(tab).outerWidth();
            tab.oldHeight = $(tab).outerHeight();
        }
        var tabWidth = $(tab).width();
        var tabHeight = $(tab).height();
        var cellsHCount = tab.getAttribute("cellsHCount");
        var cellsVCount = tab.getAttribute("cellsVCount");
        var deltaX = Math.floor(tabWidth / cellsVCount);
        var deltaY = Math.floor(tabHeight / cellsHCount);
        var resizeWidget;
        for (var i = 0; i < tab.children.length; i++) {
            childWidgetGridTop = tab.children[i].getAttribute("gridTop");
            childWidgetGridLeft = tab.children[i].getAttribute("gridLeft");
            $(tab.children[i]).css("left", Math.round(childWidgetGridLeft * deltaX));
            $(tab.children[i]).css("width", (Math.round(deltaX * tab.children[i].getAttribute("gridWidth")) + 1));
            $(tab.children[i]).css("top", Math.round(deltaY * childWidgetGridTop));
            $(tab.children[i]).css("height", Math.round(deltaY * tab.children[i].getAttribute("gridHeight")) + 1);
            var title = $(tab.children[i]).find('td').first();
            $($(title).children(":first")).css("min-height", "20px");
            if (title != null) {
                var titleLbl = $(title.find('label'));
                if (!tab.children[i].isInited) {
                    $(titleLbl).mousedown($RWT.widgetsarea.tab.dashWidgetDragPartMouseDown);
                    tab.children[i].isInited = true;
                }
                var buttons = $($(tab.children[i]).find("td")[0]).next();
                if (buttons != null) {
                    $($(buttons).children(":first")).css("min-height", "20px");
                    $(titleLbl).width($(tab.children[i]).width() - buttons.width() - 3);
                }
            }
            if ($(tab.children[i]).attr("resize-widget") == "true") {
                resizeWidget = $(tab.children[i]);
                $(tab.children[i]).attr("resize-widget", null);
            }
        }
        if (resizeWidget != null) {
            $RWT.widgetsarea.tab.dashWidgetDragPartMouseDown(null, resizeWidget[0]);
        }
    },
    dashWidgetDragPartMouseDown: function (mouseEvent, resizeWidget) {
        if (mouseEvent == null && resizeWidget == null || mouseEvent != null && mouseEvent.which !== 1) {
            return;
        }
        var EStartDragPos = {TOP_LEFT: 0, TOP: 1, TOP_RIGHT: 2, RIGHT: 3, BOTTOM_RIGHT: 4, BOTTOM: 5, BOTTOM_LEFT: 6, LEFT: 7, MOVE: 8};
        var widget = resizeWidget != null ? resizeWidget : $(this).closest(".rwt-dash-widget")[0];
        var tabWidget = $(widget).parent();
        var cellsHCount = Number(tabWidget.attr("cellsHCount"));
        var cellsVCount = Number(tabWidget.attr("cellsVCount"));
        var DASH_WIDGET_MIN_WIDTH = 2;
        var DASH_WIDGET_MIN_HEIGHT = 2;
        var offsets = $(widget).offset();
        var frameBorderWidthCss = 1;
        var tabLeft = tabWidget.offset().left, tabTop = tabWidget.offset().top;
        var gridDiv = $(tabWidget).parent().parent().find(".grid")[0];
        var startPos = EStartDragPos.MOVE;
        var isDropAccepted = true;

        var wasLeftButtonPressed, isQuickEditModeOn;
        if (resizeWidget) {
            wasLeftButtonPressed = isQuickEditModeOn = false;
        } else {
            wasLeftButtonPressed = isQuickEditModeOn = true;
        }
        //Double click (two click's in 300ms)
        //activate long time edit mode.
        var timeBetweenTwoClicksInDblClickMills = 300;
        if ((widget.getAttribute("lastClickTime") !== null && new Date().getTime() - widget.getAttribute("lastClickTime") < timeBetweenTwoClicksInDblClickMills) && (resizeWidget || $(widget).attr("isResizable") == "true")) {
            isQuickEditModeOn = false;
            widget.setAttribute("lastClickTime", null);
        } else {
            widget.setAttribute("lastClickTime", new Date().getTime());
        }

        if (!gridDiv) {
            gridDiv = document.createElement('div');
            gridDiv.style.position = "absolute";
            $(gridDiv).css("left", "5px");
            $(gridDiv).css("top", "25px");
            gridDiv.style.width = ($($(widget).parent().parent().parent()).width() - 10) + "px";
            gridDiv.style.height = ($($(widget).parent().parent().parent()).height() - 10) + "px"; //toolbar + 5px space
            deltaX = Math.floor($(gridDiv).width() / cellsHCount); //Округлить шаг в меньшую сторону
            deltaY = Math.floor($(gridDiv).height() / cellsVCount);
            diffX = $(gridDiv).width() % cellsHCount == 0 ? 0 : 1;
            diffY = $(gridDiv).height() % cellsVCount == 0 ? 0 : 1;
            gridDiv.style.zIndex = 1;
            gridDiv.style.visibility = "hidden";
            $(gridDiv).addClass('grid');

            //Creating html table for grid.
            var tableHtml = "<table style=\"height: auto; width: 100%;\"><colgroup>";
            for (var i = 0; i < cellsHCount + diffX; i++) {
                tableHtml += "<col style=\"width: " + deltaX + "px\">"
            }
            tableHtml += "</colgroup>"
            for (var row = 0; row < cellsVCount + diffY; row++) {
                tableHtml += "<tr style=\"height: " + deltaY + "px\">";
                for (var col = 0; col < cellsHCount + diffX; col++) {
                    tableHtml += "<td class=\"rwt-solid-border\"></td>";
                }
                tableHtml += "</tr>";
            }

            tableHtml += "</table>";
            gridDiv.innerHTML = tableHtml;
            var tableGrid = gridDiv.firstChild;
            $(tableGrid).css(
                    {
                        width: "100%",
                        height: "100%",
                        "table-layout": "fixed",
                        "border-collapse": "collapse"
                    });
            $(gridDiv).append(tableGrid);
            $(widget).parent().parent().parent().append(gridDiv);
            $(gridDiv).css("opacity", 0.5);
        } else {
            var tableGrid = gridDiv.firstChild;
        }
        var resizeFrameDiv = document.createElement('div');
        var resizeFrame = document.createElement('div');
        resizeFrame.style.width = "100%";
        resizeFrame.style.height = "100%";
        resizeFrame.style.border = frameBorderWidthCss + "px solid gray";
        resizeFrameDiv.appendChild(resizeFrame);
        resizeFrameDiv.style.position = "absolute";
        resizeFrameDiv.style.left = (offsets.left - 1) + "px";
        resizeFrameDiv.style.top = (offsets.top - 1) + "px";
        $(resizeFrameDiv).css("padding", "4px");
        $(resizeFrameDiv).css("box-sizing", "border-box");
        resizeFrameDiv.style.width = $(widget).outerWidth() + "px";
        resizeFrameDiv.style.height = $(widget).outerHeight() + "px";
        resizeFrameDiv.style.zIndex = 9999;
        resizeFrameDiv.style["background-color"] = "rgba(0,0,0,0)";                     //If element is empty z-index don't work in IE.
        if (mouseEvent != null) {
            resizeFrameDiv.setAttribute("dragShiftLeft", mouseEvent.pageX - offsets.left);
            resizeFrameDiv.setAttribute("dragShiftTop", mouseEvent.pageY - offsets.top);
        }

        //Creating 8 anchors divs on resizable frame.  
        var anchorDivSize = 5, anchorDivs = [];
        for (var i = 0; i < 8; i++) {
            var anchorDiv = document.createElement('div');
            anchorDiv.style.position = "absolute";
            anchorDiv.style.width = anchorDivSize + "px";
            anchorDiv.style.height = anchorDivSize + "px";
            anchorDiv.style.border = frameBorderWidthCss + "px solid gray";
            $(anchorDiv).css("background-color", "#f5f7f9");
            anchorDiv.className = "frame-anchor-div";
            anchorDivs.push(anchorDiv);
        }
        //Positioning anchor divs according to resizable frame coordinates.
        function locateAnchorDivs() {
            var widgetWidth = $(resizeFrameDiv).outerWidth(),
                    widgetHeight = $(resizeFrameDiv).outerHeight(),
                    leftAndTopSide = "1px";
            midWidth = (widgetWidth - anchorDivSize) / 2 + "px",
                    midHeight = (widgetHeight - anchorDivSize) / 2 + "px",
                    rightSide = (widgetWidth - anchorDivSize - 1) + "px",
                    bottomSide = (widgetHeight - anchorDivSize - 1) + "px";
            anchorDivs[EStartDragPos.TOP_LEFT].style.left = leftAndTopSide;
            anchorDivs[EStartDragPos.TOP_LEFT].style.top = leftAndTopSide;
            anchorDivs[EStartDragPos.TOP].style.left = midWidth;
            anchorDivs[EStartDragPos.TOP].style.top = leftAndTopSide;
            anchorDivs[EStartDragPos.TOP_RIGHT].style.left = rightSide;
            anchorDivs[EStartDragPos.TOP_RIGHT].style.top = leftAndTopSide;
            anchorDivs[EStartDragPos.RIGHT].style.left = rightSide;
            anchorDivs[EStartDragPos.RIGHT].style.top = midHeight;
            anchorDivs[EStartDragPos.BOTTOM_RIGHT].style.left = rightSide;
            anchorDivs[EStartDragPos.BOTTOM_RIGHT].style.top = bottomSide;
            anchorDivs[EStartDragPos.BOTTOM].style.left = midWidth;
            anchorDivs[EStartDragPos.BOTTOM].style.top = bottomSide;
            anchorDivs[EStartDragPos.BOTTOM_LEFT].style.left = leftAndTopSide;
            anchorDivs[EStartDragPos.BOTTOM_LEFT].style.top = bottomSide;
            anchorDivs[EStartDragPos.LEFT].style.left = leftAndTopSide;
            anchorDivs[EStartDragPos.LEFT].style.top = midHeight;
        }
        locateAnchorDivs();
        for (var i = 0; i < 8; i++) {
            $(resizeFrameDiv).append(anchorDivs[i]);
        }

        document.body.appendChild(resizeFrameDiv);

        $(gridDiv).offset($(tabWidget).offset());
        $(gridDiv).css('visibility', 'visible');

        //Fix problem with event.which property.
        //On most browsers except Chrome, event.which = 1 
        //if left button pressed AND if nothing is pressed.
        //Function set e.which = 0, if nothing is pressed. 
        function tweakMouseMoveEvent(e) {
            // If left button is not set, set which to 0
            // This indicates no buttons pressed
            if (e.which === 1 && !wasLeftButtonPressed) {
                e.which = 0;
            }
        }

        $(document).bind('mousemove.dash', function (event) {
            tweakMouseMoveEvent(event);
            if (event.which !== 1) {
                //If left button isn't pressed - tracking cursor position and 
                //change cursor shape on frame anchors.
                resizeFrameDiv.setAttribute("dragShiftLeft", event.pageX - $(widget).offset().left);
                resizeFrameDiv.setAttribute("dragShiftTop", event.pageY - $(widget).offset().top);
                var borderInteractiveAreaWidth = 4;
                var cursorShape = "move";
                if (Math.abs($(resizeFrameDiv).offset().left - event.pageX) <= borderInteractiveAreaWidth) { //LEFT SIDE 
                    if (Math.abs($(resizeFrameDiv).offset().top - event.pageY) <= borderInteractiveAreaWidth) {
                        startPos = EStartDragPos.TOP_LEFT;
                        cursorShape = "nw-resize";
                    } else if (Math.abs(($(resizeFrameDiv).offset().top + $(resizeFrameDiv).outerHeight()) - event.pageY) <= borderInteractiveAreaWidth) {
                        startPos = EStartDragPos.BOTTOM_LEFT;
                        cursorShape = "ne-resize";
                    } else {
                        startPos = EStartDragPos.LEFT;
                        cursorShape = "w-resize";
                    }
                } else if (Math.abs(($(resizeFrameDiv).offset().left + $(resizeFrameDiv).outerWidth()) - event.pageX) <= borderInteractiveAreaWidth) { //RIGHT SIDE 
                    if (Math.abs($(resizeFrameDiv).offset().top - event.pageY) <= borderInteractiveAreaWidth) {
                        startPos = EStartDragPos.TOP_RIGHT;
                        cursorShape = "ne-resize";
                    } else if (Math.abs(($(resizeFrameDiv).offset().top + $(resizeFrameDiv).outerHeight()) - event.pageY) <= borderInteractiveAreaWidth) {
                        startPos = EStartDragPos.BOTTOM_RIGHT;
                        cursorShape = "nw-resize";
                    } else {
                        startPos = EStartDragPos.RIGHT;
                        cursorShape = "w-resize";
                    }
                } else if (Math.abs($(resizeFrameDiv).offset().top - event.pageY) <= borderInteractiveAreaWidth) {
                    startPos = EStartDragPos.TOP;
                    cursorShape = "n-resize";
                } else if (Math.abs(($(resizeFrameDiv).offset().top + $(resizeFrameDiv).outerHeight()) - event.pageY) <= borderInteractiveAreaWidth) {
                    startPos = EStartDragPos.BOTTOM;
                    cursorShape = "n-resize";
                } else {
                    startPos = EStartDragPos.MOVE;
                    cursorShape = "move";
                }
                $(resizeFrameDiv).css("cursor", cursorShape);
                return;
            }

            //Dragging with pressed left button
            //1. Calculate new position for temp frame and apply it.
            var newPosTop = $(resizeFrameDiv).position.top,
                    newPosLeft = $(resizeFrameDiv).position.left,
                    newWidth = $(resizeFrameDiv).outerWidth(),
                    newHeight = $(resizeFrameDiv).outerHeight(),
                    cursorX = event.pageX >= tabLeft ? event.pageX : tabLeft,
                    cursorY = event.pageY >= tabTop ? event.pageY : tabTop;
            if (cursorX > tabLeft + Math.floor(tabWidget.width() / cellsHCount) * cellsHCount) {
                cursorX = tabLeft + Math.floor(tabWidget.width() / cellsHCount) * cellsHCount;
            }
            if (cursorY > tabTop + Math.floor(tabWidget.height() / cellsVCount) * cellsVCount) {
                cursorY = tabTop + Math.floor(tabWidget.height() / cellsVCount) * cellsVCount;
            }

            var cellWidth = Math.floor(tabWidget.width() / cellsHCount);
            var cellHeight = Math.floor(tabWidget.height() / cellsVCount);
            var minWidthPx = Math.round(DASH_WIDGET_MIN_WIDTH * cellWidth);
            var minHeightPx = Math.round(DASH_WIDGET_MIN_HEIGHT * cellHeight);
            resizeFrameDiv.startPos = startPos;
            switch (startPos) {
                case EStartDragPos.MOVE :
                    newPosLeft = (cursorX - resizeFrameDiv.getAttribute("dragShiftLeft"));
                    newPosTop = (cursorY - resizeFrameDiv.getAttribute("dragShiftTop"));

                    newPosLeft = newPosLeft >= tabLeft ? newPosLeft : tabLeft;
                    newPosTop = newPosTop >= tabTop ? newPosTop : tabTop;
                    var maxPosX = tabLeft + Math.floor(tabWidget.width() / cellsHCount) * cellsHCount - $(resizeFrameDiv).outerWidth() - 1;
                    var maxPosY = tabTop + Math.floor(tabWidget.height() / cellsVCount) * cellsVCount - $(resizeFrameDiv).outerHeight() - 1;
                    newPosLeft = newPosLeft <= maxPosX ? newPosLeft : maxPosX;
                    newPosTop = newPosTop <= maxPosY ? newPosTop : maxPosY;
                    break;
                case EStartDragPos.TOP_LEFT :
                    newPosLeft = cursorX;
                    newPosTop = cursorY;
                    newHeight = $(resizeFrameDiv).outerHeight() + ($(resizeFrameDiv).position().top - newPosTop);
                    newWidth = $(resizeFrameDiv).outerWidth() + ($(resizeFrameDiv).position().left - newPosLeft);
                    break;
                case EStartDragPos.TOP :
                    newPosTop = cursorY;
                    newHeight = $(resizeFrameDiv).outerHeight() + ($(resizeFrameDiv).position().top - newPosTop);
                    break;
                case EStartDragPos.TOP_RIGHT :
                    newPosTop = cursorY;
                    newHeight = $(resizeFrameDiv).outerHeight() + ($(resizeFrameDiv).position().top - newPosTop);
                    newWidth = $(resizeFrameDiv).outerWidth() + (cursorX - ($(resizeFrameDiv).position().left + $(resizeFrameDiv).width()));
                    break;
                case EStartDragPos.RIGHT :
                    newWidth = cursorX - $(resizeFrameDiv).position().left;
                    break;
                case EStartDragPos.BOTTOM_RIGHT :
                    newHeight = $(resizeFrameDiv).height() + (cursorY - ($(resizeFrameDiv).position().top + $(resizeFrameDiv).height()));
                    newWidth = $(resizeFrameDiv).width() + (cursorX - ($(resizeFrameDiv).position().left + $(resizeFrameDiv).width()));
                    break;
                case EStartDragPos.BOTTOM :
                    newHeight = cursorY - $(resizeFrameDiv).position().top;
                    break;
                case EStartDragPos.BOTTOM_LEFT :
                    newPosLeft = cursorX;
                    newWidth = $(resizeFrameDiv).outerWidth() + ($(resizeFrameDiv).position().left - newPosLeft);
                    newHeight = cursorY - $(resizeFrameDiv).position().top;
                    break;
                case EStartDragPos.LEFT :
                    newPosLeft = cursorX;
                    tmpWidth = $(resizeFrameDiv).outerWidth() + ($(resizeFrameDiv).position().left - newPosLeft);
                    if (tmpWidth >= minWidthPx) {
                        newPosLeft = cursorX;
                    }
                    newWidth = $(resizeFrameDiv).outerWidth() + ($(resizeFrameDiv).position().left - newPosLeft);
                    break;
            }

            if (Math.round(newWidth) >= minWidthPx) {
                $(resizeFrameDiv).css({left: newPosLeft + "px", width: (newWidth) + "px"});
            }
            if (Math.round(newHeight) >= minHeightPx) {
                $(resizeFrameDiv).css({top: newPosTop + "px", height: (newHeight) + "px"});
            }
            locateAnchorDivs();

            //2. Redraw background grid.
            gridDiv.style.visibility = "visible";
            newPosLeft = $(resizeFrameDiv).position().left - tabLeft;
            newPosTop = $(resizeFrameDiv).position().top - tabTop;

            var frameWidgetRect = {
                left: (startPos == EStartDragPos.TOP_LEFT || startPos == EStartDragPos.BOTTOM_LEFT || startPos == EStartDragPos.LEFT) ?
                        $RWT.widgetsarea.tab.excludedRound(newPosLeft / cellWidth) : Math.round(newPosLeft / cellWidth),
                top: (startPos == EStartDragPos.TOP_LEFT || startPos == EStartDragPos.TOP_RIGHT || startPos == EStartDragPos.TOP) ?
                        $RWT.widgetsarea.tab.excludedRound(newPosTop / cellHeight) : Math.round(newPosTop / cellHeight),
                width: Math.round($(resizeFrameDiv).outerWidth() / cellWidth),
                height: Math.round($(resizeFrameDiv).outerHeight() / cellHeight),
                contains: function (rect) {
                    return (this.top + this.height > rect.top) &&
                            (this.top < rect.top + rect.height) &&
                            (this.left < rect.left + rect.width) &&
                            (this.left + this.width > rect.left);
                }
            };
            //Define frame rectangle color. 
            //If new frame position isn't free color is LightPink, otherwise - PaleGreen.
            var frameRectColor = "PaleGreen";
            isDropAccepted = true;
            for (var i = 0; i < $(widget).siblings().length; i++) {
                var childWidget = $(widget).siblings().eq(i);
                if ($(childWidget).is(gridDiv)) {
                    continue;
                }
                var childWidgetRect = {
                    left: Math.round($(childWidget).position().left / cellWidth),
                    top: Math.round($(childWidget).position().top / cellHeight),
                    width: Math.round($(childWidget).width() / cellWidth),
                    height: Math.round($(childWidget).height() / cellHeight)
                };
                if (frameWidgetRect.contains(childWidgetRect)) {
                    frameRectColor = "LightPink";
                    isDropAccepted = false;
                    break;
                }
            }

            //Paint table cells with defined color.   
            var rows = tableGrid.childNodes[1].childNodes;
            var cellRect = {left: 0, top: 0, width: 1, height: 1};
            var colored = "background-color: " + frameRectColor + ";";
            var gray = "";
            for (var row = 0; row < cellsVCount; row++) {
                var currentRowCells = rows[row].childNodes;
                for (var col = 0; col < cellsHCount; col++) {
                    cellRect.left = col;
                    cellRect.top = row;
                    if (frameWidgetRect.contains(cellRect)) {
                        currentRowCells[col].setAttribute("style", colored);
                        currentRowCells[col].colored = true;
                    } else {
                        if (currentRowCells[col].colored != null && currentRowCells[col].colored == true) {
                            currentRowCells[col].setAttribute("style", gray);
                            currentRowCells[col].colored = false;
                        }
                    }
                }
            }
            return false;
        });

        function closeEditMode() {
            this.isEditingModeOn = false;
            $(document).unbind("mousemove.dash");
            $(document).unbind("mouseup.dash");
            $(document).unbind("mousedown.dash");
            $(document).unbind("keydown.dash");
            document.body.removeChild(resizeFrameDiv);
            $(gridDiv).css('visibility', 'hidden');
        }

        $(document).bind("mouseup.dash", function (event) {
            if (event.which === 1) {
                wasLeftButtonPressed = false;
            }
            if (isDropAccepted) {
                var cellWidth = Math.floor($(tabWidget).outerWidth() / cellsHCount);
                var cellHeight = Math.floor($(tabWidget).outerHeight() / cellsVCount);
                var newPosLeft = $(resizeFrameDiv).position().left - tabLeft;
                var newPosTop = $(resizeFrameDiv).position().top - tabTop;
                //New coordinates in cells.
                var newLocX = (startPos == EStartDragPos.TOP_LEFT || startPos == EStartDragPos.BOTTOM_LEFT || startPos == EStartDragPos.LEFT) ?
                        $RWT.widgetsarea.tab.excludedRound(newPosLeft / cellWidth) : Math.round(newPosLeft / cellWidth);
                var newLocY = (startPos == EStartDragPos.TOP_LEFT || startPos == EStartDragPos.TOP_RIGHT || startPos == EStartDragPos.TOP) ?
                        $RWT.widgetsarea.tab.excludedRound(newPosTop / cellHeight) : Math.round(newPosTop / cellHeight);
                var newWidth = Math.round($(resizeFrameDiv).outerWidth() / cellWidth);
                var newHeight = Math.round($(resizeFrameDiv).outerHeight() / cellHeight);
                resizeFrameDiv.style.left = (tabLeft + Math.round(cellWidth * newLocX) - 1) + "px";
                resizeFrameDiv.style.top = (tabTop + cellHeight * newLocY - 1) + "px";
                resizeFrameDiv.style.width = (Math.round(newWidth * cellWidth) + 1) + "px";
                resizeFrameDiv.style.height = (Math.round(newHeight * cellHeight) + 1) + "px";
                $RWT.actions.event(widget, "dashWidgetResize", newLocX + ";" + newLocY + ";" + newWidth + ";" + newHeight, null);
            } else {
                resizeFrameDiv.style.left = ($(widget).offset().left - 1) + "px";
                resizeFrameDiv.style.top = ($(widget).offset().top - 1) + "px";
                resizeFrameDiv.style.width = ($(widget).outerWidth()) + "px";
                resizeFrameDiv.style.height = ($(widget).outerHeight()) + "px";
            }
            locateAnchorDivs();
            var rows = tableGrid.childNodes[1].childNodes;
            for (var row = 0; row < cellsVCount; row++) {
                var currentRowCells = rows[row].childNodes;
                for (var col = 0; col < cellsHCount; col++) {
                    if (currentRowCells[col].colored) {
                        currentRowCells[col].setAttribute("style", "");
                        currentRowCells[col].colored = false;
                    }
                }
            }
            if (isQuickEditModeOn) {
                closeEditMode();
            }
        });

        $(document).bind('mousedown.dash', function (event) {
            if (event.which !== 1) {
                return;
            }
            wasLeftButtonPressed = true;
            var elemUnderCursor = document.elementFromPoint(event.clientX, event.clientY);
            if (elemUnderCursor !== resizeFrameDiv && elemUnderCursor !== resizeFrameDiv.children[0] && elemUnderCursor.className !== "frame-anchor-div") {
                closeEditMode();
            }
            return false;
        });

        $(document).bind('keydown.dash', function (event) {
            if (event.which !== 13 && event.which !== 27) {//Keys to close edit mode: ENTER/RETURN(13), ESC(27).
                return;
            }
            closeEditMode();
            return false;
        });
        return false;
    },
    excludedRound: function (number) {
        fract = number % 1;
        return fract > 0.5 ? Math.round(number) : Math.floor(number);
    }
}

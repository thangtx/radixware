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
$RWT.dashboard = {
    layout: function (target) {
        if (target && target.getAttribute("dash-initied") !== "true") {
            if ($(target).find(".dash-widget-draggable-part")) {
                $(target).find(".dash-widget-draggable-part").mousedown($RWT.dashboard.dashWidgetDragPartMouseDown);
                $(target).find(".rwt-dash-widget-move-button").click($RWT.dashboard.dashWidgetDragPartMouseDown);
            }
            target.rwt_min_height = function () {
                var tabHeader = $(".rwt-ui-tab-layout-header");
                var headerVisible = $(tabHeader).css("display") != "none";
                var headerH = headerVisible ? $(tabHeader).height() : 0;
                var tabWidget = $(".rwt-root-dash-widget-content-wrapper");
                return tabWidget.height() - headerH - 5;
            }

            var contentWrapper = $(target).find(".rwt-dash-widget-content-wrapper");
            if (contentWrapper !== null) {
                var left = $(target).find(".dash-widget-inner-content-filler").offset().left;
                var top = $(target).find(".dash-widget-inner-content-filler").offset().top;
                contentWrapper.css("position", "fixed");
                contentWrapper.css("left", left);
                contentWrapper.css("top", top);
                contentWrapper.height($(target).height() - $(target).find(".dash-widget-header").height() - 1);
                contentWrapper.width($(target).width() - 1);
            }

            target.setAttribute("dash-initied", "true");
        }
    },
    dashWidgetDragPartMouseDown: function (mouseEvent) {
        rootContainer = $(".rwt-root-dash-widget");
        if (mouseEvent.which !== 1 || rootContainer.attr("isEditingModeOn") === "true") {
            return;
        }
        rootContainer.attr("isEditingModeOn", "true");

        var widget = $(this).closest(".rwt-dash-widget")[0];
        var EStartDragPos = {TOP_LEFT: 0, TOP: 1, TOP_RIGHT: 2, RIGHT: 3, BOTTOM_RIGHT: 4, BOTTOM: 5, BOTTOM_LEFT: 6, LEFT: 7, MOVE: 8},
        startPos = EStartDragPos.MOVE,
                rootContainer = $(".rwt-root-dash-widget"),
                DASH_CELLS_CNT = rootContainer.attr("DASH_CELL_CNT"),
                DASH_WIDGET_MIN_WIDTH = rootContainer.attr("DASH_WIDGET_MIN_WIDTH"),
                DASH_WIDGET_MIN_HEIGHT = rootContainer.attr("DASH_WIDGET_MIN_HEIGHT"),
                backgroundTableColor = rootContainer.css("background-color"),
                isDropAccepted = true,
                offsets = $(widget).offset(),
                frameBorderWidthCss = 2;
        var wasLeftButtonPressed, isQuickEditModeOn;
        wasLeftButtonPressed = isQuickEditModeOn = $(this).hasClass("rwt-dash-widget-move-button") ? false : true;

        //Double click (two click's in 300ms)
        //activate long time edit mode.
        var timeBetweenTwoClicksInDblClickMills = 300;
        if (widget.getAttribute("lastClickTime") !== null && new Date().getTime() - widget.getAttribute("lastClickTime") < timeBetweenTwoClicksInDblClickMills) {
            isQuickEditModeOn = false;
            widget.setAttribute("lastClickTime", null);
        } else {
            widget.setAttribute("lastClickTime", new Date().getTime());
        }

        //Creating frame for resizing and dragging widget. 
        var resizeFrameDiv = document.createElement('div');
        resizeFrameDiv.style.position = "absolute";
        resizeFrameDiv.style.left = offsets.left + "px";
        resizeFrameDiv.style.top = offsets.top + "px";
        resizeFrameDiv.style.width = $(widget).width() + "px";
        resizeFrameDiv.style.height = $(widget).height() + "px";
        resizeFrameDiv.style.zIndex = 9999;
        resizeFrameDiv.style.border = frameBorderWidthCss + "px solid gray";
        resizeFrameDiv.style["background-color"] = "rgba(0,0,0,0)";                     //If element is empty z-index don't work in IE.
        resizeFrameDiv.setAttribute("dragShiftLeft", mouseEvent.pageX - offsets.left);
        resizeFrameDiv.setAttribute("dragShiftTop", mouseEvent.pageY - offsets.top);
        document.body.appendChild(resizeFrameDiv);

        //Creating 8 anchors divs on resizable frame.  
        var anchorDivSize = 5, anchorDivs = [];
        for (var i = 0; i < 8; i++) {
            var anchorDiv = document.createElement('div');
            anchorDiv.style.position = "absolute";
            anchorDiv.style.width = anchorDivSize + "px";
            anchorDiv.style.height = anchorDivSize + "px";
            anchorDiv.style.border = frameBorderWidthCss + "px solid gray";
            anchorDiv.className = "frame-anchor-div";
            anchorDivs.push(anchorDiv);
        }
        //Positioning anchor divs according to resizable frame coordinates.
        function locateAnchorDivs() {
            var widgetWidth = $(resizeFrameDiv).width(),
                    widgetHeight = $(resizeFrameDiv).height(),
                    leftAndTopSide = -frameBorderWidthCss + "px",
                    midWidth = (widgetWidth - anchorDivSize) / 2 + "px",
                    midHeight = (widgetHeight - anchorDivSize) / 2 + "px",
                    rightSide = (widgetWidth - anchorDivSize - frameBorderWidthCss) + "px",
                    bottomSide = (widgetHeight - anchorDivSize - frameBorderWidthCss) + "px";
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

        //Creating background grid div with html table.
        var tabWidget = $(widget).parent();
        var tabLeft = tabWidget.offset().left, tabTop = tabWidget.offset().top;
        var gridDiv = document.createElement('div');
        gridDiv.style.position = "absolute";
        gridDiv.style.left = 0 + "px";
        gridDiv.style.top = 0 + "px";
        gridDiv.style.width = tabWidget.width() + "px";
        gridDiv.style.height = tabWidget.height() + "px";
        gridDiv.style.zIndex = 1;
        gridDiv.style.visibility = "hidden";

        //Creating html table for grid.
        var tableHtml = "<table>";
        for (var row = 0; row < DASH_CELLS_CNT; row++) {
            tableHtml += "<tr>";
            for (var col = 0; col < DASH_CELLS_CNT; col++) {
                tableHtml += "<td></td>";
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
        $(tableGrid).find('td').css({
            border: "1px dashed black"
        });
        $(gridDiv).append(tableGrid);
        $(widget).parent().append(gridDiv);


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
                    } else if (Math.abs(($(resizeFrameDiv).offset().top + $(resizeFrameDiv).height()) - event.pageY) <= borderInteractiveAreaWidth) {
                        startPos = EStartDragPos.BOTTOM_LEFT;
                        cursorShape = "ne-resize";
                    } else {
                        startPos = EStartDragPos.LEFT;
                        cursorShape = "w-resize";
                    }
                } else if (Math.abs(($(resizeFrameDiv).offset().left + $(resizeFrameDiv).width()) - event.pageX) <= borderInteractiveAreaWidth) { //RIGHT SIDE 
                    if (Math.abs($(resizeFrameDiv).offset().top - event.pageY) <= borderInteractiveAreaWidth) {
                        startPos = EStartDragPos.TOP_RIGHT;
                        cursorShape = "ne-resize";
                    } else if (Math.abs(($(resizeFrameDiv).offset().top + $(resizeFrameDiv).height()) - event.pageY) <= borderInteractiveAreaWidth) {
                        startPos = EStartDragPos.BOTTOM_RIGHT;
                        cursorShape = "nw-resize";
                    } else {
                        startPos = EStartDragPos.RIGHT;
                        cursorShape = "w-resize";
                    }
                } else if (Math.abs($(resizeFrameDiv).offset().top - event.pageY) <= borderInteractiveAreaWidth) {
                    startPos = EStartDragPos.TOP;
                    cursorShape = "n-resize";
                } else if (Math.abs(($(resizeFrameDiv).offset().top + $(resizeFrameDiv).height()) - event.pageY) <= borderInteractiveAreaWidth) {
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
                    newWidth = $(resizeFrameDiv).width(),
                    newHeight = $(resizeFrameDiv).height(),
                    cursorX = event.pageX >= tabLeft ? event.pageX : tabLeft,
                    cursorY = event.pageY >= tabTop ? event.pageY : tabTop;
            cursorX = cursorX <= tabLeft + tabWidget.width() ? cursorX : tabLeft + tabWidget.width();
            cursorY = cursorY <= tabTop + tabWidget.height() ? cursorY : tabTop + tabWidget.height();

            switch (startPos) {
                case EStartDragPos.MOVE :
                    newPosLeft = (cursorX - resizeFrameDiv.getAttribute("dragShiftLeft"));
                    newPosTop = (cursorY - resizeFrameDiv.getAttribute("dragShiftTop"));

                    newPosLeft = newPosLeft >= tabLeft ? newPosLeft : tabLeft;
                    newPosTop = newPosTop >= tabTop ? newPosTop : tabTop;
                    var maxPosX = tabLeft + tabWidget.width() - $(resizeFrameDiv).width();
                    var maxPosY = tabTop + tabWidget.height() - $(resizeFrameDiv).height();
                    newPosLeft = newPosLeft <= maxPosX ? newPosLeft : maxPosX;
                    newPosTop = newPosTop <= maxPosY ? newPosTop : maxPosY;
                    break;
                case EStartDragPos.TOP_LEFT :
                    newPosLeft = cursorX;
                    newPosTop = cursorY;
                    newHeight = $(resizeFrameDiv).height() + ($(resizeFrameDiv).position().top - newPosTop);
                    newWidth = $(resizeFrameDiv).width() + ($(resizeFrameDiv).position().left - newPosLeft);
                    break;
                case EStartDragPos.TOP :
                    newPosTop = cursorY;
                    newHeight = $(resizeFrameDiv).height() + ($(resizeFrameDiv).position().top - newPosTop);
                    break;
                case EStartDragPos.TOP_RIGHT :
                    newPosTop = cursorY;
                    newHeight = $(resizeFrameDiv).height() + ($(resizeFrameDiv).position().top - newPosTop);
                    newWidth = $(resizeFrameDiv).width() + (cursorX - ($(resizeFrameDiv).position().left + $(resizeFrameDiv).width()));
                    break;
                case EStartDragPos.RIGHT :
                    newWidth = $(resizeFrameDiv).width() + (cursorX - ($(resizeFrameDiv).position().left + $(resizeFrameDiv).width()));
                    break;
                case EStartDragPos.BOTTOM_RIGHT :
                    newHeight = $(resizeFrameDiv).height() + (cursorY - ($(resizeFrameDiv).position().top + $(resizeFrameDiv).height()));
                    newWidth = $(resizeFrameDiv).width() + (cursorX - ($(resizeFrameDiv).position().left + $(resizeFrameDiv).width()));
                    break;
                case EStartDragPos.BOTTOM :
                    newHeight = $(resizeFrameDiv).height() + (cursorY - ($(resizeFrameDiv).position().top + $(resizeFrameDiv).height()));
                    break;
                case EStartDragPos.BOTTOM_LEFT :
                    newPosLeft = cursorX;
                    newWidth = $(resizeFrameDiv).width() + ($(resizeFrameDiv).position().left - newPosLeft);
                    newHeight = $(resizeFrameDiv).height() + (cursorY - ($(resizeFrameDiv).position().top + $(resizeFrameDiv).height()));
                    break;
                case EStartDragPos.LEFT :
                    newPosLeft = cursorX;
                    newWidth = $(resizeFrameDiv).width() + ($(resizeFrameDiv).position().left - newPosLeft);
                    break;
            }
            var cellWidth = tabWidget.width() / DASH_CELLS_CNT;
            var cellHeight = tabWidget.height() / DASH_CELLS_CNT;
            var minWidthPx = Math.round(DASH_WIDGET_MIN_WIDTH * cellWidth);
            var minHeightPx = Math.round(DASH_WIDGET_MIN_HEIGHT * cellHeight);
            if (Math.round(newWidth) >= minWidthPx && Math.round(newHeight) >= minHeightPx) {
                $(resizeFrameDiv).css({top: newPosTop + "px", left: newPosLeft + "px", width: newWidth + "px", height: newHeight + "px"});
                locateAnchorDivs();
            }

            //2. Redraw background grid.
            gridDiv.style.visibility = "visible";
            newPosLeft = $(resizeFrameDiv).position().left - tabLeft;
            newPosTop = $(resizeFrameDiv).position().top - tabTop;

            var frameWidgetRect = {
                left: Math.round(newPosLeft / cellWidth),
                top: Math.round(newPosTop / cellHeight),
                width: Math.round($(resizeFrameDiv).width() / cellWidth),
                height: Math.round($(resizeFrameDiv).height() / cellHeight),
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
            var rows = $(tableGrid.childNodes[0]).children();
            var cellRect = {left: 0, top: 0, width: 1, height: 1};
            for (var row = 0; row < DASH_CELLS_CNT; row++) {
                for (var col = 0; col < DASH_CELLS_CNT; col++) {
                    cellRect.left = col;
                    cellRect.top = row;
                    if (frameWidgetRect.contains(cellRect)) {
                        rows.eq(row).children().eq(col).css("background-color", frameRectColor);
                    } else {
                        rows.eq(row).children().eq(col).css("background-color", backgroundTableColor);
                    }
                }
            }
            return false;
        });

        function closeEditMode() {
            rootContainer.attr("isEditingModeOn", "false");
            $(document).unbind("mousemove.dash");
            $(document).unbind("mouseup.dash");
            $(document).unbind("mousedown.dash");
            $(document).unbind("keydown.dash");
            document.body.removeChild(resizeFrameDiv);
            $(gridDiv).remove();
        }

        $(document).bind('mouseup.dash', function (event) {
            if (event.which === 1) {
                wasLeftButtonPressed = false;
            }

            gridDiv.style.visibility = "hidden";
            if (isDropAccepted) {
                //Translate form coordinates in px to abs values in dashboard cells.
                //Temp vars.
                var cellWidth = tabWidget.width() / DASH_CELLS_CNT;
                var cellHeight = tabWidget.height() / DASH_CELLS_CNT;
                var newPosLeft = $(resizeFrameDiv).position().left - tabLeft;
                var newPosTop = $(resizeFrameDiv).position().top - tabTop;
                //New coordinates in cells.
                var newLocX = Math.round(newPosLeft / cellWidth);
                var newLocY = Math.round(newPosTop / cellHeight);
                var newWidth = Math.round(resizeFrameDiv.offsetWidth / cellWidth);
                var newHeight = Math.round(resizeFrameDiv.offsetHeight / cellHeight);
                $RWT.actions.event(widget, "dashWidgetResize", "locX=" + newLocX + ";locY=" + newLocY + ";width=" + newWidth + ";height=" + newHeight, null);

                resizeFrameDiv.style.left = (tabLeft + cellWidth * newLocX) + "px";
                resizeFrameDiv.style.top = (tabTop + cellHeight * newLocY) + "px";
                resizeFrameDiv.style.width = (newWidth * cellWidth) + "px";
                resizeFrameDiv.style.height = (newHeight * cellHeight) + "px";
            } else {
                resizeFrameDiv.style.left = $(widget).offset().left + "px";
                resizeFrameDiv.style.top = $(widget).offset().top + "px";
                resizeFrameDiv.style.width = $(widget).width() + "px";
                resizeFrameDiv.style.height = $(widget).height() + "px";
            }
            locateAnchorDivs();
            if (isQuickEditModeOn) {
                closeEditMode();
            }

            return false;
        });

        $(document).bind('mousedown.dash', function (event) {
            if (event.which !== 1) {
                return;
            }
            wasLeftButtonPressed = true;
            var elemUnderCursor = document.elementFromPoint(event.clientX, event.clientY);
            if (elemUnderCursor !== resizeFrameDiv && elemUnderCursor.className !== "frame-anchor-div") {
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
    diagram: {
        layout: function (target) {
            if (target.getAttribute("DiagRs") !== null && (target.getAttribute("BrowserUpdateTime") == null || target.getAttribute("BrowserUpdateTime") !== target.getAttribute("LastUpdateTime"))) {
                target.setAttribute("BrowserUpdateTime", target.getAttribute("LastUpdateTime"));
                var diagRs = JSON.parse(target.getAttribute("DiagRs"));

                //Preparing data series for output in graphic
                if (diagRs.HistoryRs) {
                    var histRs = diagRs.HistoryRs;
                    var sourceData = histRs.Records;
                    var data;
                    var eventDiagramSeriesName = "eventLineData";
                    var pointDiagramSeriesName = "pointLineData";
                    var statAvgDiagramSeriesName = "avgLineData";
                    var statBordersDiagramSeriesName = "bordersData";
                    var highErrorBorderName = "highErrorBorder";
                    var lowErrorBorderName = "lowErrorBorder";
                    var highWarningBorderName = "highWarningBorder";
                    var lowWarningBorderName = "lowWarningBorder";

                    switch (target.getAttribute("DiagramType")) {
                        case "Event":
                            {
                                var eventLineData = [];
                                if (sourceData !== null && sourceData.length !== 0) {
                                    eventLineData.push({x: +histRs.TimeFrom, y: sourceData[0].BegVal});
                                    for (var i = 0; i < sourceData.length; i++) {
                                        var tmp = sourceData[i];
                                        eventLineData.push({x: tmp.BegTime, y: tmp.BegVal});
                                        eventLineData.push({x: tmp.BegTime, y: tmp.EndVal});
                                    }
                                    eventLineData.push({x: +histRs.TimeTo, y: sourceData[sourceData.length - 1].EndVal});
                                } else {
                                    var tmp = histRs.PrevRecord;
                                    eventLineData.push({x: histRs.TimeFrom, y: tmp.EndVal});
                                    eventLineData.push({x: histRs.TimeTo, y: tmp.EndVal});
                                }
                                data = [
                                    {className: eventDiagramSeriesName, name: eventDiagramSeriesName, data: eventLineData}
                                ];
                            }
                            break;
                        case "Point":
                            {
                                var pointLineData = [];
                                for (var i = 0; i < sourceData.length; i++) {
                                    var tmp = sourceData[i];
                                    pointLineData.push({x: tmp.EndTime, y: tmp.EndVal});
                                }
                                data = [
                                    {className: pointDiagramSeriesName, name: pointDiagramSeriesName, data: pointLineData}
                                ];
                            }

                            break;
                        case "Stat":
                            {
                                var avgLineData = [];
                                var bordersData = [];
                                for (var i = 0; i < sourceData.length; i++) {
                                    var tmp = sourceData[i];
                                    var time = tmp.BegTime + (tmp.EndTime - tmp.BegTime) / 2;
                                    avgLineData.push({x: time, y: tmp.AvgVal});
                                    bordersData.push({x: time, y: tmp.MinVal});
                                    bordersData.push({x: time, y: tmp.MaxVal});
                                    bordersData.push(null);
                                }

                                data = [
                                    {className: statAvgDiagramSeriesName, name: statAvgDiagramSeriesName, data: avgLineData},
                                    {className: statBordersDiagramSeriesName, name: statBordersDiagramSeriesName, data: bordersData}
                                ];
                            }
                            break;
                        case "None" :
                            alert("Type of diagram not defined");
                    }

                    var startBorderLineCoord = histRs.TimeFrom;
                    var endBorderLineCoord = histRs.TimeTo;
                    if (histRs.ValueRanges) {
                        if (histRs.ValueRanges.HighErrorVal) {
                            data.push({
                                className: highErrorBorderName,
                                name: highErrorBorderName,
                                data: [{x: startBorderLineCoord, y: histRs.ValueRanges.HighErrorVal}, {x: endBorderLineCoord, y: histRs.ValueRanges.HighErrorVal}]
                            });
                            if (histRs.ValueRanges.HighWarnVal) {
                                data.push({
                                    className: highWarningBorderName,
                                    name: highWarningBorderName,
                                    data: [{x: startBorderLineCoord, y: histRs.ValueRanges.HighWarnVal}, {x: endBorderLineCoord, y: histRs.ValueRanges.HighWarnVal}]
                                });
                            }
                        }
                        if (histRs.ValueRanges.LowErrorVal) {
                            data.push({
                                className: lowErrorBorderName,
                                name: lowErrorBorderName,
                                data: [{x: startBorderLineCoord, y: histRs.ValueRanges.LowErrorVal}, {x: endBorderLineCoord, y: histRs.ValueRanges.LowErrorVal}]
                            });
                            if (histRs.ValueRanges.LowWarnVal) {
                                data.push({
                                    className: lowWarningBorderName,
                                    name: lowWarningBorderName,
                                    data: [{x: startBorderLineCoord, y: histRs.ValueRanges.LowWarnVal}, {x: endBorderLineCoord, y: histRs.ValueRanges.LowWarnVal}]
                                });
                            }
                        }
                    }

                    //Converting timestamp to Date string
                    var millisInSecond = 1000;
                    var millisInMinute = millisInSecond * 60;
                    var millisInHour = millisInMinute * 60;
                    var millisInDay = millisInHour * 24;
                    var millisInMonth = millisInDay * 30;
                    var millisInYear = millisInMonth * 12;
                    var diagramTimeIntervalInMillis = histRs.TimeTo - histRs.TimeFrom;
                    var formatDateStrLabel;
                    if (diagramTimeIntervalInMillis <= millisInMinute) {
                        formatDateStrLabel = '%S';
                    } else if (diagramTimeIntervalInMillis <= millisInHour) {
                        formatDateStrLabel = '%M:%S';
                    } else if (diagramTimeIntervalInMillis <= millisInDay) {
                        formatDateStrLabel = '%H:%M';
                    } else if (diagramTimeIntervalInMillis <= millisInMonth) {
                        formatDateStrLabel = '%b %d';
                    } else if (diagramTimeIntervalInMillis <= millisInYear) {
                        formatDateStrLabel = '%b %d';
                    } else {
                        formatDateStrLabel = '%d-%m-%y';
                    }
                    var timeOptions = {
                        timezone: "browser",
                        timeformat: formatDateStrLabel,
                        twelveHourClock: false,
                        monthNames: null
                    };
                    var timestamp2DateStrFnc = function (timestampVal, index) {
                        if (index === 0 || timestampVal >= histRs.TimeTo) {
                            return " ";
                        }
                        return Chartist.TimeAxisUtils.getTickLabel(timestampVal, timeOptions);
                    };

                    //Used for auto-range function
                    var minValue = null, maxValue = null;
                    minValue = +target.getAttribute("MinValue");
                    maxValue = +target.getAttribute("MaxValue");
                    var optionsAxisY;
                    if (minValue !== null && maxValue !== null) {
                        optionsAxisY = {type: Chartist.AutoScaleAxis, high: maxValue, low: minValue};
                    } else {
                        optionsAxisY = {type: Chartist.AutoScaleAxis};
                    }

                    var options = {
                        series: {
                            //will be filled lower
                        },
                        axisX: {
                            type: Chartist.AutoScaleAxis,
                            onlyInteger: true,
                            low: histRs.TimeFrom,
                            high: histRs.TimeTo,
                            isTimeAxis: true,
                            labelInterpolationFnc: timestamp2DateStrFnc,
                            scaleMinSpace: 32
                        },
                        axisY: optionsAxisY
                    };
                    var seriesOption = {lineSmooth: false};
                    options.series[eventDiagramSeriesName] = seriesOption;
                    options.series[pointDiagramSeriesName] = seriesOption;
                    options.series[statAvgDiagramSeriesName] = seriesOption;
                    options.series[statBordersDiagramSeriesName] = seriesOption;
                    options.series[highErrorBorderName] = {
                        lineSmooth: false,
                        showLine: false,
                        showPoint: false,
                        showArea: true,
                        areaBase: 99999
                    };
                    options.series[lowErrorBorderName] = {
                        lineSmooth: false,
                        showLine: false,
                        showPoint: false,
                        showArea: true,
                        areaBase: -99999
                    };
                    options.series[highWarningBorderName] = {
                        lineSmooth: false,
                        showLine: false,
                        showPoint: false,
                        showArea: true,
                        areaBase: 99999
                    };
                    options.series[lowWarningBorderName] = {
                        lineSmooth: false,
                        showLine: false,
                        showPoint: false,
                        showArea: true,
                        areaBase: -99999
                    };

                    var dataForChartist = {series: data};
                    var chart = new Chartist.Line(target, dataForChartist, options);

                    //Change type of point for borders series to rectangles.            
                    var halfOfRectWidth = 2.5;
                    var halfOfRectHeight = 0.6;
                    chart.on('draw', function (data) {
                        if (data.type === 'point' && data.series.name === statBordersDiagramSeriesName) {
                            var rect = new Chartist.Svg('path', {
                                d: ['M',
                                    data.x - halfOfRectWidth,
                                    data.y - halfOfRectHeight,
                                    'L',
                                    data.x + halfOfRectWidth,
                                    data.y - halfOfRectHeight,
                                    'L',
                                    data.x + halfOfRectWidth,
                                    data.y + halfOfRectHeight,
                                    'L',
                                    data.x - halfOfRectWidth,
                                    data.y + halfOfRectHeight,
                                    'z'].join(' '),
                                style: 'fill-opacity: 1'
                            }, 'ct-point');
                            data.element.replace(rect);
                        }
                    });


                } else if (diagRs.CorrellationRs) {
                    var corrRs = diagRs.CorrellationRs;
                    var sourceData = corrRs.Records;
                    var dataForChartist = {series: [], labels: []};
                    for (var i = 0; i < sourceData.length; i++) {
                        dataForChartist.labels.push(sourceData[i].Record.Title);
                        dataForChartist.series.push(sourceData[i].Record.Value);
                    }

                    var diagramDiv = $(target).children(".rwt-dash-corr-diagram-container");
                    var options = {showLabel: false};
                    if (target.getAttribute("diagram-initied") !== "true") {
                        var legendDiv = $(target).children(".rwt-dash-diagram-legend");
                        var pieLegendList = legendDiv.add("ul");
                        pieLegendList.css(
                                {
                                    "font-family": "sans - serif",
                                    li: {
                                        position: "relative",
                                        "padding-left": "1.3em",
                                        "margin-bottom": "0.3em",
                                        "list-style-type": "none"
                                    },
                                    "li::before": {
                                        height: "1em",
                                        width: "1em",
                                        position: "absolute",
                                        top: "0.1em",
                                        left: "0",
                                        content: '',
                                        "border-radius": "1em",
                                    }
                                });
                        var seriesColors = [
                            "#d70206",
                            "#f05b4f",
                            "#f4c63d",
                            "#d17905",
                            "#453d3f",
                            "#59922b",
                            "#0544d3",
                            "#6b0392",
                            "#f05b4f",
                            "#dda458",
                            "#eacf7d",
                            "#86797d",
                            "#b2c326",
                            "#6188e2",
                            "#a748ca"
                        ];
                        $.each(dataForChartist.labels, function (i, val) {
                            var listItem = $('<li />')
                                    .addClass('ct-series-' + i)
                                    .html('<strong>' + val + '</strong>')
                                    .css({"word-wrap": "break-word", color: seriesColors[i]})
                                    .appendTo(pieLegendList);
                        });

                        diagramDiv.height($(target).height() - legendDiv.height() - $(target).find(".dash-widget-header").height() - 1);
                        diagramDiv.width($(target).width());
                        new Chartist.Pie(diagramDiv[0], dataForChartist, options);

                        target.setAttribute("diagram-initied", "true");
                    } else {
                        target.diagram = new Chartist.Pie(diagramDiv[0], dataForChartist, options);
                    }
                }
            }
        }
    },
    progressbar: {
        layout: function (target) {
            function setUncertainBehavior() {
                $(target).children().first().css("transition-duration", "0s");
                $(target).children().first().addClass("rwt-uncertain-behavior-progress-bar");

                var magic = target.offsetLeft;  //HERE COMES THE MAGIC - reading this prop forces delay we need
                $(target).children().first().css("width", "20px");
                magic = target.offsetLeft;

                target.setAttribute("UNCERTAIN_BEHAVIOR", "true");
            }

            function removeUncertainBehavior() {
                $(target).children().first().removeClass("rwt-uncertain-behavior-progress-bar");
                $(target).children().first().css("left", "1px");
                target.setAttribute("UNCERTAIN_BEHAVIOR", "false");
            }

            if (target.getAttribute("PROGRESSBAR_VALUE") !== null && target.getAttribute("PROGRESSBAR_MAX_VALUE") !== null) {
                var progressValue = (+target.getAttribute("PROGRESSBAR_VALUE"));
                var progressValueMax = (+target.getAttribute("PROGRESSBAR_MAX_VALUE")) * 1000;
                var layoutUpdateTime_ms = 2000;
                var isStop = false;

                if (target.getAttribute("STOP_UNCERTAIN_BEHAVIOR_tmp") === null) {
                    target.setAttribute("STOP_UNCERTAIN_BEHAVIOR_tmp", 1);
                }
                
                if (target.getAttribute("UNCERTAIN_BEHAVIOR") === null) {
                    target.setAttribute("UNCERTAIN_BEHAVIOR", "false");
                }
                
                if (target.getAttribute("STOP_UNCERTAIN_BEHAVIOR") !== null) {
                    isStop = target.getAttribute("STOP_UNCERTAIN_BEHAVIOR") !== target.getAttribute("STOP_UNCERTAIN_BEHAVIOR_tmp");
                    if(isStop) {
                        target.setAttribute("STOP_UNCERTAIN_BEHAVIOR_tmp", target.getAttribute("STOP_UNCERTAIN_BEHAVIOR"));
                    }
                }
                
                if (target.getAttribute("UNCERTAIN_BEHAVIOR") !== null && target.getAttribute("UNCERTAIN_BEHAVIOR") === "true" && isStop) {
                    removeUncertainBehavior();
                    
                    var magic = target.offsetLeft;  //HERE COMES THE MAGIC - reading this prop forces delay we need
                    $(target).children().first().css("width", "100px");
                    magic = target.offsetLeft;
                }
                
                if (target.getAttribute("UNCERTAIN_BEHAVIOR") !== null && target.getAttribute("UNCERTAIN_BEHAVIOR") === "false") {
                    if ((progressValueMax - progressValue) > layoutUpdateTime_ms) {
                        $(target).children().first().css("transition-duration", layoutUpdateTime_ms + "ms");
                        $(target).children().first().css("width", Math.floor(100 - ((progressValue + layoutUpdateTime_ms) / progressValueMax) * 100) + "px");
                    } else if ((progressValueMax - progressValue) === layoutUpdateTime_ms) {
                        $(target).children().first().css("transition-duration", layoutUpdateTime_ms + "ms");
                        $(target).children().first().css("width", Math.floor(100 - ((progressValue + layoutUpdateTime_ms) / progressValueMax) * 100) + "px");
                        window.setTimeout(setUncertainBehavior, layoutUpdateTime_ms);
                    } else if ((progressValueMax - progressValue) < layoutUpdateTime_ms && (progressValueMax - progressValue) > 0) {
                        $(target).children().first().css("transition-duration", (progressValueMax - progressValue) + "ms");
                        $(target).children().first().css("width", "0px");
                        window.setTimeout(setUncertainBehavior, progressValueMax - progressValue);
                    }
                }
            }
        }
    }
};
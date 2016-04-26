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
            target.setAttribute("dash-initied", "true");
        }
        var contentWrapper = $(target).find(".rwt-dash-widget-content-wrapper");
        if (contentWrapper !== null) {
            var left = $(target).find(".dash-widget-inner-content-filler").offset().left;
            var top = $(target).find(".dash-widget-inner-content-filler").offset().top;
            contentWrapper.css("position", "fixed");
            contentWrapper.css("left", left);
            contentWrapper.css("top", top);
            contentWrapper.height($(target).height() - $(target).find(".dash-widget-header").height() - 1);
            contentWrapper.width($(target).width());
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
                    switch (target.getAttribute("DiagramType")) {
                        case "Event":
                            {
                                var eventLineData = [];
                                if (sourceData !== null && sourceData.length !== 0) {
                                    eventLineData.push([+histRs.TimeFrom, sourceData[0].BegVal]);
                                    for (var i = 0; i < sourceData.length; i++) {
                                        var tmp = sourceData[i];
                                        eventLineData.push([tmp.BegTime, tmp.BegVal]);
                                        eventLineData.push([tmp.BegTime, tmp.EndVal]);
                                    }
                                    eventLineData.push([+histRs.TimeTo, sourceData[sourceData.length - 1].EndVal]);
                                } else {
                                    var tmp = histRs.PrevRecord;
                                    eventLineData.push([histRs.TimeFrom, tmp.EndVal]);
                                    eventLineData.push([histRs.TimeTo, tmp.EndVal]);
                                }
                                data = [{
                                        color: "blue",
                                        data: eventLineData,
                                        lines: {show: true}
                                    }
                                ];
                            }
                            break;
                        case "Point":
                            {
                                var pointLineData = [];
                                for (var i = 0; i < sourceData.length; i++) {
                                    var tmp = sourceData[i];
                                    pointLineData.push([tmp.EndTime, tmp.EndVal]);
                                }
                                data = [{
                                        color: "blue",
                                        data: pointLineData,
                                        lines: {show: true},
                                        points: {
                                            show: true,
                                            radius: 2,
                                            fillColor: "blue",
                                            fill: true
                                        }
                                    }
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
                                    avgLineData.push([time, tmp.AvgVal]);
                                    bordersData.push([time, tmp.MinVal]);
                                    bordersData.push([time, tmp.MaxVal]);
                                    bordersData.push(null);
                                }

                                //Preparing args for print graphic
                                data = [
                                    {
                                        color: "gray",
                                        data: bordersData,
                                        lines: {show: true, lineWidth: 1},
                                        points: {
                                            show: true,
                                            radius: 3,
                                            symbol: function cross(ctx, x, y, radius, shadow) {
                                                ctx.moveTo(x - radius, y);
                                                ctx.lineTo(x + radius, y);
                                            },
                                            fillColor: "gray",
                                            fill: true
                                        }
                                    },
                                    {
                                        color: "blue",
                                        data: avgLineData,
                                        lines: {show: true, lineWidth: 2}
                                    }
                                ];
                            }
                            break;
                        case "None" :
                            alert("Type of diagram not defined");
                    }
                    var markings = [];
                    if (histRs.ValueRanges != null) {
                        if (histRs.ValueRanges.HighErrorVal != null) {
                            markings.push({color: "rgba(255, 50, 50, 0.3)", yaxis: {from: histRs.ValueRanges.HighErrorVal}});

                            if (histRs.ValueRanges.HighWarnVal != null) {
                                markings.push({color: "rgba(255, 200, 100, 0.3)", yaxis: {from: histRs.ValueRanges.HighWarnVal, to: histRs.ValueRanges.HighErrorVal}});
                            }
                        }
                        if (histRs.ValueRanges.LowErrorVal != null) {
                            markings.push({color: "rgba(255, 50, 50, 0.3)", yaxis: {to: histRs.ValueRanges.LowErrorVal}});
                            if (histRs.ValueRanges.LowWarnVal != null) {
                                markings.push({color: "rgba(255, 200, 100, 0.3)", yaxis: {from: histRs.ValueRanges.LowErrorVal, to: histRs.ValueRanges.LowWarnVal}});
                            }
                        }
                    }

                    var minValue = null, maxValue = null;
                    if (target.getAttribute("IsAutoRange") === "false") {
                        minValue = +target.getAttribute("MinValue");
                        maxValue = +target.getAttribute("MaxValue");
                        if (minValue >= maxValue) {
                            minValue = maxValue = null;
                        }
                    }
                    var options =
                            {
                                xaxis:
                                        {
                                            axisLabelUseCanvas: true,
                                            axisLabelPadding: 5,
                                            axisLabelFontSizePixels: 12,
                                            axisLabel: target.getAttribute("X-AxisTitle"),
                                            mode: "time",
                                            timezone: "browser",
                                            autoscaleMargin: 0,
                                            min: histRs.TimeFrom,
                                            max: histRs.TimeTo
                                        },
                                yaxis:
                                        {
                                            axisLabelUseCanvas: true,
                                            axisLabelPadding: 5,
                                            axisLabelFontSizePixels: 12,
                                            axisLabel: target.getAttribute("Y-AxisTitle"),
                                            min: minValue,
                                            max: maxValue
                                        },
                                grid:
                                        {
                                            backgroundColor: "#fff",
                                            markings: markings
                                        }
                            };
                }

                $.plot(target, data, options);
            }
        }
    }
};
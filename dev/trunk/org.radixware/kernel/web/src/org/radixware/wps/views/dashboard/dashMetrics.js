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
    diagram: {
        layout: function (target) {
            if (target.getAttribute("DiagRs") !== null && (target.getAttribute("BrowserUpdateTime") == null || target.getAttribute("BrowserUpdateTime") !== target.getAttribute("LastUpdateTime"))) {
                target.setAttribute("BrowserUpdateTime", target.getAttribute("LastUpdateTime"));
                var diagRs = JSON.parse(target.getAttribute("DiagRs"));

                //Preparing data for output in graphic
                if (diagRs.HistoryRs) {
                    var histRs = diagRs.HistoryRs;
                    var sourceData = histRs.Records;
                    var data = [];
                    var type;
                    var eventSeriesColor = [];
                    switch (target.getAttribute("DiagramType")) {
                        case "Event":
                            {
                                var partData = [];
                                if (sourceData !== null && sourceData.length !== 0) {
                                    partData.push({x: new Date(+histRs.TimeFrom), y: sourceData[0].BegVal});
                                    partData.push({x: new Date(sourceData[0].EndTime), y: sourceData[0].BegVal});
                                    data.push(partData);
                                    partData = [];
                                    eventSeriesColor.push('blue');
                                    for (var i = 0; i < sourceData.length; i++) {
                                        var tmp = sourceData[i];
                                        var nextVal = (i + 1) > sourceData.length - 1 ? sourceData[i].BegVal : sourceData[i+1].BegVal;
                                        var nextDate = (i + 1) > sourceData.length - 1 ? new Date(+histRs.TimeTo) : new Date(sourceData[i+1].EndTime);
                                        partData.push({x: new Date(tmp.EndTime), y: tmp.BegVal});
                                        partData.push({x: new Date(tmp.EndTime), y: tmp.EndVal});
                                        if (nextVal != tmp.EndVal) {
                                            partData.push({x: nextDate, y: tmp.EndVal});
                                            data.push(partData);
                                            partData = [];
                                            eventSeriesColor.push('blue');
                                        }
                                    }
                                    data.push(partData);
                                } else {
                                    var tmp = histRs.PrevRecord;
                                    d3.select(target).selectAll('.mg-line2').remove();
                                    data.push({x: new Date(+histRs.TimeFrom), y: tmp.EndVal});
                                    data.push({x: new Date(+histRs.TimeTo), y: tmp.EndVal});
                                }
                                type = 'line';
                            }
                            break;
                        case "Point":
                            {
                                for (var i = 0; i < sourceData.length; i++) {
                                    var tmp = sourceData[i];
                                    data.push({x: new Date(tmp.EndTime), y: tmp.EndVal});
                                }
                            }
                            type = 'point';
                            break;
                        case "Stat":
                            {
                                for (var i = 0; i < sourceData.length; i++) {
                                    var tmp = sourceData[i];
                                    var time = tmp.BegTime + (tmp.EndTime - tmp.BegTime) / 2;
                                    data.push({x: new Date(time), y: tmp.AvgVal, l: tmp.MinVal, u: tmp.MaxVal});
                                }
                                type = 'line';
                            }
                            break;
                        case "None" :
                            alert("Type of diagram not defined");
                    }

                    var startBorderLineCoord = histRs.TimeFrom;
                    var endBorderLineCoord = histRs.TimeTo;


                    //Used for auto-range function
                    var minValue = null, maxValue = null;
                    minValue = +target.getAttribute("MinValue");
                    maxValue = +target.getAttribute("MaxValue");

                    var nullData = false;
                    if (data == null || data.length === 0) {
                        data.push([{x: new Date(+startBorderLineCoord), y: 0}]);
                        minValue = 0;
                        maxValue = 1;
                        nullData = true;
                    }
                    if (maxValue == minValue && minValue == 0) {
                        maxValue = 1;
                    }
                    var xaxCountVal = Math.round(($(target).width() - 100) / 70);
                    var yaxCountVal = Math.round(($(target).height() - 50) / 15);
                    var yax_format_func = function (x) {
                        return +(+x).toFixed(3) + "";
                    };
                    
                    MG.data_graphic({
                        data: data,
                        transition_on_update: false,
                        min_y: minValue,
                        max_y: maxValue,
                        chart_type: type,
                        full_width: true,
                        full_height: true,
                        interpolate: d3.curveLinear,
                        area: false,
                        aggregate_rollover: true,
                        show_secondary_x_label: true,
                        x_extended_ticks: true,
                        y_extended_ticks: true,
                        min_x: new Date(+startBorderLineCoord),
                        max_x: new Date(+endBorderLineCoord),
                        right: 5,
                        left: 90,
                        top: 10,
                        yax_format: yax_format_func,
                        show_confidence_band: ['l', 'u'],
                        target: target,
                        x_accessor: 'x',
                        y_accessor: 'y',
                        x_sort: false,
                        colors: eventSeriesColor.length == 0 ? ['blue'] : eventSeriesColor,
                        xax_count: xaxCountVal,
                        yax_count: yaxCountVal,
                        show_rollover_text: false,
                        x_label: target.getAttribute("x-axistitle"),
                        y_label: target.getAttribute("y-axistitle"),
                        x_axis_date_autoType: true, 
                        point_size: nullData ? 0 : 2.5
                    });

                    $(target).css('background-color', 'white');
                    if (nullData == true) {
                        d3.select(target).selectAll('.mg-points').selectAll('*').remove();
                    }
                } else if (diagRs.CorrellationRs) {
                    //Not supported yet
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
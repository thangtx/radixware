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
$_RWT_TAB_LAYOUT = {
    _layout: function(tab) {
        var tabHeader, tabArea, scroller, left, right;
        function paintScroller() {
            var liSumWidth = 0;
            var currentLi = null;
            WpsUtils.iterateNames(tabHeader, "li", function(li) {
                liSumWidth += $(li).outerWidth(true);
                if (li.className.indexOf("rwt-ui-tab-layout-tab-active") > 0) {
                    currentLi = li;
                }
            });
            var needScroller = liSumWidth - tabArea.offsetWidth >= 0;
            if (needScroller && tabHeader.childElementCount > 1) {//ie scrollerWidht fix
                $(scroller).css("display", "inline");
            }
            if (!needScroller || tabHeader.childElementCount <= 1) {
                $(scroller).css("display", "none");
                $(tabHeader).css("margin-left", "0px");
            }
            updateButtonState();
        }

        function mouseScrollingEvent(e) {
            var delta = e.wheelDelta ? e.wheelDelta : e.deltaY;
            var firstLi = $(tabHeader).find("li:first-child")[0];
            var lastLi = $(tabHeader).find("li:last-child")[0];
            animateHeaderSlide(delta, firstLi, lastLi);
        }

        function updateButtonState() {
            if (left && right) {
                var firstLi = $(tabHeader).find("li:first-child")[0];
                var lastLi = $(tabHeader).find("li:last-child")[0];

                var lastEdge = parseInt(lastLi.offsetLeft) + parseInt(lastLi.offsetWidth);
                var areaWidth = tabArea.offsetWidth;
                var c = areaWidth - lastEdge;

                //left button
                if ((firstLi.offsetLeft >= 0)) {
                    if (left.className.indexOf('ui-state-disabled') <= 0) {
                        $(left).addClass("ui-state-disabled");
                    }
                }
                else {
                    if (left.className.indexOf('ui-state-disabled') > 0) {
                        $(left).removeClass("ui-state-disabled");
                    }
                }

                //right button
                if (c > 40) {
                    if (right.className.indexOf('ui-state-disabled') <= 0) {
                        $(right).addClass("ui-state-disabled");
                    }
                }
                else {
                    if (right.className.indexOf('ui-state-disabled') > 0) {
                        $(right).removeClass("ui-state-disabled");
                    }
                }
            }
        }

        function animateHeaderSlide(delta, first, last) {
            var currentMargin = parseInt($(tabHeader).css("margin-left"), 10);
            var m = delta < 0 ? (currentMargin - 50) : (currentMargin + 50);
            var opts = {
                'margin-left': m + 'px'
            };
            $(tabHeader).animate(
                    opts, {
                queue: false,
                duration: 250,
                easing: "linear",
                step: function() {
                    if (delta > 0) {//slide to right
                        if ((first.offsetLeft >= 0)) {
                            $(tabHeader).stop();
                        }
                    }
                    else {
                        var lastEdge = parseInt(last.offsetLeft) + parseInt(last.offsetWidth);
                        var areaWidth = tabArea.offsetWidth;
                        var c = areaWidth - lastEdge;
                        if (c > 40) {//40px==scroll buttons total width
                            $(tabHeader).stop();
                        }
                    }
                    updateButtonState();
                },
            });
        }

        WpsUtils.iterateAll(tab, function(div) {
            var role = $(div).attr('role');
            if (role === 'header') {
                tabHeader = div;
            } else if (role === 'tabs') {
                tabArea = div;
            }
            if (tabHeader && tabArea)
                return true;
            else
                return false;
        });
        WpsUtils.iterateAll(tab, function(div) {
            var role = $(div).attr('role');
            if (role === 'scroller') {
                scroller = div;
            }
            if (scroller) {
                return true;
            }
            else
                return false;
        });
        if (tabArea && tabHeader) {
            if ($(tab).attr('adjustHeight') == 'true') {
                $RWT._layoutNode(tabArea);
                var ch = null;
                WpsUtils.iterateAll(tabArea, function(t) {
                    if ($(t).css('display') != 'none') {
                        var content = WpsUtils.findFirstChild(t);
                        while (content) {
                            if (content.rwt_min_height) {
                                ch = content.rwt_min_height(content);
                                break;
                            }
                            content = WpsUtils.findFirstChild(content);
                        }
                        if (ch == null) {
                            ch = $(t).outerHeight();
                        }
                        return true;
                    }
                    return false;
                });
            }
            var tabWBorder = $(tabArea).outerWidth(true) - $(tabArea).innerWidth();
            var tabHBorder = $(tabArea).outerHeight(true) - $(tabArea).innerHeight();
            var headerVisible = $(tabHeader).css("display") != "none";
            var headerH = headerVisible ? $(tabHeader).outerHeight(true) : 0;
            if (ch) {
                $(tab).height(headerH + ch + tabHBorder + 5);
            }
            var totalButtonsWidth = 0;
            var lastW;
            if (headerVisible) {
                WpsUtils.iterateAll(tabHeader, function(button) {
                    var size = $RWT.toolButton.size(button);
                    if (size) {
                        $(button).width(size.w);
                        lastW = $(button).outerWidth();
                        totalButtonsWidth += size.w;
                    }
                });
            }
            tabArea.style.width = ($(tab).innerWidth() - tabWBorder - 1) + 'px';
            tabArea.style.height = ($(tab).innerHeight() - tabHBorder - headerH) + 'px';
            if (scroller) {
                var result;
                var path = ['table', 'tbody', 'tr'];
                var firstLi = $(tabHeader).find("li:first-child")[0];
                var lastLi = $(tabHeader).find("li:last-child")[0];
                if (!firstLi || !lastLi) {
                    return;
                }
                var res = scroller;
                for (var i in path) {
                    res = WpsUtils.findChildByLocalName(res, path[i]);
                    if (!res)
                        return null;
                    result = res;
                }

                if (result/* && $(scroller).is(":visible")*/) {
                    WpsUtils.iterateAll(result, function(td) {
                        var button = WpsUtils.findFirstChild(td);
                        if (button) {
                            var direction = $(button).attr('scroll');
                            if (direction) {
                                if (direction == 'left') {
                                    left = button;
                                }
                                if (direction == 'right') {
                                    right = button;
                                }
                            }
                        }
                    });
                    if (left && right) {
                        if (!left.onmousedown)
                            left.onmousedown = function() {
                                var x = setInterval(function() {
                                    $(tabHeader).stop();
                                    animateHeaderSlide(1, firstLi, lastLi);
                                },
                                        200);
                                document.onmouseup = left.onmouseup = function() {
                                    clearInterval(x);
                                }
                            };
                        if (!left.onclick)
                            left.onclick = function() {
                                $(tabHeader).stop();
                                if (left.className.indexOf("ui-state-disabled") <= 0)
                                    $(tabHeader).animate({"margin-left": "+=50px"}, {duration: 250, step: function() {
                                            if ((firstLi.offsetLeft >= 0)) {
                                                $(tabHeader).stop();
                                            }
                                            updateButtonState();
                                        }
                                    });
                            }

                        if (!right.onmousedown)
                            right.onmousedown = function() {
                                var x = setInterval(function() {
                                    $(tabHeader).stop();
                                    animateHeaderSlide(-1, firstLi, lastLi);
                                },
                                        200);
                                document.onmouseup = right.onmouseup = function() {
                                    clearInterval(x);
                                }
                            };

                        if (!right.onclick)
                            right.onclick = function() {
                                $(tabHeader).stop();
                                if (right.className.indexOf("ui-state-disabled") <= 0)
                                    $(tabHeader).animate({"margin-left": "-=50px"}, {duration: 250, step: function() {
                                            var lastEdge = parseInt(lastLi.offsetLeft) + parseInt(lastLi.offsetWidth);
                                            var areaWidth = tabArea.offsetWidth;
                                            var c = areaWidth - lastEdge;
                                            if (c > 40) {
                                                $(tabHeader).stop();
                                            }
                                            updateButtonState();
                                        }
                                    });
                            }
                    }
                    paintScroller();
                }

                if (tabHeader.addEventListener) {
                    if ('onwheel' in document) {
                        // IE9+, FF17+
                        tabHeader.addEventListener("wheel", mouseScrollingEvent, false);
                    } else if ('onmousewheel' in document) {
                        // ÑƒÑ�Ñ‚Ð°Ñ€ÐµÐ²ÑˆÐ¸Ð¹ Ð²Ð°Ñ€Ð¸Ð°Ð½Ñ‚ Ñ�Ð¾Ð±Ñ‹Ñ‚Ð¸Ñ�
                        tabHeader.addEventListener("mousewheel", mouseScrollingEvent, false);
                    } else {
                        // 3.5 <= Firefox < 17, Ð±Ð¾Ð»ÐµÐµ Ñ�Ñ‚Ð°Ñ€Ð¾Ðµ Ñ�Ð¾Ð±Ñ‹Ñ‚Ð¸Ðµ DOMMouseScroll Ð¿Ñ€Ð¾Ð¿ÑƒÑ�Ñ‚Ð¸Ð¼
                        tabHeader.addEventListener("MozMousePixelScroll", mouseScrollingEvent, false);
                    }
                }
            }
        }
    }
};
$RWT.tab_layout = $_RWT_TAB_LAYOUT;
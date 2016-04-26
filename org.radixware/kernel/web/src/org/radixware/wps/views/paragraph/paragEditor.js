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
$RWT.paragEditor={
    layout:function(pe){
        var logo = null;
        WpsUtils.iterateNames(pe,'img',function(d){
            if($(d).attr('role')=='logo') {
                logo=d;
                return true;
            }
            return false;
        });
        if(logo){
            var w = $(pe).innerWidth();
            var h = $(pe).innerHeight();
            var lw = $(logo).outerWidth();
            var lh = $(logo).outerHeight();
            $(logo)
            .css('position','absolute')
            .css('left',(w/2-lw/2) + 'px')
            .css('top',(h/2-lh/2) + 'px');
        }
    }
}


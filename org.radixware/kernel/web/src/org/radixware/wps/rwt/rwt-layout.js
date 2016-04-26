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
$_RWT_VBOX={        
    
    }

$RWT.box_layout={
    _preferredWidth:function(node){
        if(node.rwt_f_pref_w){
            return node.rwt_f_pref_w(node);
        }else
            return $(node).outerWidth();
    },
    _preferredHeight:function(node){
        if(node.rwt_f_pref_h){
            return node.rwt_f_pref_h(node);
        }else
            return $(node).outerHeight();
    }        
}
$RWT.vertical_layout={
    minsize:function(layout){
        var minHeight=0;
        var minWidth=0;
        WpsUtils.iterateAll(layout,function(proxy){
            var child = WpsUtils.findFirstChild(proxy);
            if(child.rwt_f_minsize){
                var ms = child.rwt_f_minsize(child);
                if(ms){
                    if(ms.w > minWidth)
                        minWidth = ms.w;
                    minHeight += ms.h;
                }
            }
        });
        return {
            w:minWidth,
            h:minHeight
        };
    },
    layout:function(layout){
        var parent = layout.parentNode;
        var th = $(layout).innerHeight();
        var tw = $(layout).innerWidth();
        var hs={};
        hs.count=0;
        hs.fc_count=0;
        hs.fixed=0;
        if(th > 0){
            WpsUtils.iterateAll(layout,function(proxy){
                var child = WpsUtils.findFirstChild(proxy);
                if($(child).css('display')=='none'){
                    $(proxy).css('display','none');
                    return false;
                }else{
                    $(proxy).css('display','');
                }
                if(child.style && child.style.height){
                    if(child.style.height.indexOf('px')>0){//preferred
                        var h =$(child).outerHeight(); 
                        hs[hs.count]={
                            pr:proxy,
                            h:h
                        };
                        hs.fixed+=h;
                        hs.fc_count++;
                    }else if(child.style.height.indexOf('%')>0){//expanding
                        hs[hs.count]={
                            pr:proxy,
                            h:null
                        };
                    }
                }else{//minimum-expanding
                    h =$(child).outerHeight(); 
                    hs[hs.count]={
                        pr:proxy,
                        h:h
                    };
                    hs.fixed+=h;                        
                    hs.fc_count++;
                }                    
                
                hs.count++;
            });
            if(hs.fixed > th && hs.fc_count>0){
                var diff = (hs.fixed - th)/hs.fc_count;
                for(var i = 0; i < hs.count;i++){
                    if(hs[i].h!=null){
                        hs[i].h-=diff;
                    }
                }
            }
            var exp={};
            exp.count=0;
            for(i = 0; i < hs.count;i++){
                if(hs[i].h!=null){
                    hs[i].pr.style.width = '100%';
                    hs[i].pr.style.height = hs[i].h+ 'px';                                                
                }else{
                    exp[exp.count] = hs[i].pr;
                    exp.count++;
                }
            }
            if(exp.count > 0){
                var eh = (th - hs.fixed)/exp.count;
                for(i = 0; i < exp.count;i++){
                    exp[i].style.width = '100%';
                    exp[i].style.height = eh+ 'px';                                                
                }
            }                                
        }else{            
        }
    }
};
$RWT.horizontal_layout={
    layout:function(layout){
        var parent = layout.parentNode;        
        var tw = $(layout).innerWidth();
        var hs={};
        hs.count=0;
        hs.fc_count=0;
        hs.fixed=0;
        if(tw > 0){
            WpsUtils.iterateAll(layout,function(proxy){
                var child = WpsUtils.findFirstChild(proxy);
                if($(child).css('display')=='none'){
                    $(proxy).css('display','none');
                    return false;
                }else{
                    $(proxy).css('display','');
                }
                if(child.style && child.style.width){
                    
                    
                    if(child.style.width.indexOf('px')>0){//preferred
                        var w =$(child).outerWidth(); 
                        if(child.rwt_f_minsize){
                            var minw=child.rwt_f_minsize(child).w;
                            if(minw>w){
                                w = minw;
                            }
                        }                        
                        hs[hs.count]={
                            pr:proxy,
                            h:w
                        };
                        hs.fixed+=w;
                        hs.fc_count++;
                    }else if(child.style.width.indexOf('%')>0){//expanding
                        hs[hs.count]={
                            pr:proxy,
                            h:null
                        };
                    }
                }else{//minimum-expanding                    
                    if(child.rwt_f_minsize)
                        w=child.rwt_f_minsize(child).w;
                    else
                        w =$(child).outerWidth(); 
                    hs[hs.count]={
                        pr:proxy,
                        h:w
                    };
                    hs.fixed+=w;                        
                    hs.fc_count++;
                }                    
                
                hs.count++;
            });
            if(hs.fixed > tw && hs.fc_count>0){
                var diff = (hs.fixed - tw)/hs.fc_count;
                for(var i = 0; i < hs.count;i++){
                    if(hs[i].h!=null){
                        hs[i].h-=diff;
                    }
                }
            }
            var exp={};
            exp.count=0;
            for(i = 0; i < hs.count;i++){
                if(hs[i].h!=null){
                    hs[i].pr.style.height = '100%';
                    hs[i].pr.style.width = hs[i].h+ 'px';                                                
                }else{
                    exp[exp.count] = hs[i].pr;
                    exp.count++;
                }
            }
            if(exp.count > 0){
                var eh = (tw - hs.fixed)/exp.count;
                for(i = 0; i < exp.count;i++){
                    exp[i].style.height= '100%';
                    exp[i].style.width  = eh+ 'px';                                                
                }
            }
                               
        }else{            
        }
    }
};

$RWT.grid_layout={
    cell:{
        layout:function(cell){            
            if($(cell).attr("hfit")=="true"){
                var w =0;
                var cellx = $(cell).position().left 
                WpsUtils.iterateAll(cell,function(obj){
                    var $obj=$(obj);
                    var right = $obj.position().left -cellx + $obj.outerWidth();
                    if(right > w){
                        w = right;
                    }
                });
                $(cell.parentNode).width(w);
            }
            if($(cell).attr("vfit")=="true"){
                //var celly = $(cell).position().top 
                var h =0;
                WpsUtils.iterateAll(cell,function(obj){
                    var $obj=$(obj);
                    var bottom = /*$obj.position().top*/ + $obj.outerHeight();
                    if(bottom > h){
                        h = bottom;
                    }
                });
                $(cell.parentNode).height(h);
            }
        }
    } 
}


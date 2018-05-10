/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.rwt;

import org.radixware.kernel.common.html.Html;


public final class Banner extends UIObject{
    
    private final static String DEFAULT_BANNER_HEIGHT="100px";
    private final static String DEFAULT_BANNER_FRAME_STYLE="border: none; width: 100%;";    
    
    public static class Options{
        
        private final String dirPath;
        private final String fileName;
        private final String style;
        private final String height;

        public Options(final String dirPath, final String fileName, final String style, final String height) {
            this.dirPath = dirPath;
            this.fileName = fileName;
            this.style = style;
            this.height = height;
        }

        public String getDirPath() {
            return dirPath;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFrameStyle() {
            return style;
        }

        public String getFrameHeight() {
            return height;
        }
    }
        
    public Banner(final Options options){
        super(new Html("iframe"));
        getHtml().setAttr("src", "bannerDir/"+options.getFileName());
        final String style = options.getFrameStyle();
        if (style!=null && !style.isEmpty()){
            getHtml().setAttr("style", style);
        }else{
            String height = options.getFrameHeight();
            if (height==null || height.isEmpty()){
                height = DEFAULT_BANNER_HEIGHT;
            }
            getHtml().setAttr("style", DEFAULT_BANNER_FRAME_STYLE+"; height: "+height+";");
        }      
    }   
}

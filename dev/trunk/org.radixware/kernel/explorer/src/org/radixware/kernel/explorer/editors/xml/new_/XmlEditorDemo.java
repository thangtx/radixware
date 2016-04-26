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

package org.radixware.kernel.explorer.editors.xml.new_;

import com.trolltech.qt.gui.QApplication;
import java.io.File;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.env.Application;

public class XmlEditorDemo{
    
    public XmlEditorDemo(){
        super();
    }

    public static void main(String[] args) throws Exception {        
        System.setProperty("com.trolltech.qt.native-library-loader-override", "org.radixware.kernel.explorer.Explorer$QtJambiNativeLibraryLoader");        
        QApplication.initialize(args);
        final Application application = Application.newInstance(null);
        XmlEditor xmlEditor = new XmlEditor(application.getEnvironment(), FileUtils.readTextFile(new File(""), "UTF-8"));
        xmlEditor.setGeometry(0, 0, 1350, 900);

        xmlEditor.show();
        QApplication.execStatic();
    }    
}

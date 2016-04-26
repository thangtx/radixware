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

package org.radixware.wps.rwt.uploading;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.RwtAction;


public class LoadFileAction extends RwtAction{
    
    private IUploadedDataReader reader;
    private FileUploader uploader;
    private final IClientEnvironment environment;
    
    public LoadFileAction(final IClientEnvironment env, final IUploadedDataReader reader) {
        super(env,ClientIcon.CommonOperations.OPEN);
        environment = env;
        this.reader = reader;
    }

    public LoadFileAction(final IClientEnvironment env, final ClientIcon icon, final IUploadedDataReader reader) {
        super(env, icon);
        environment = env;
        this.reader = reader;
    }       

    @Override
    public void addActionPresenter(final IActionPresenter p) {
        super.addActionPresenter(p);
        if (p instanceof UIObject){
            final UIObject contextObject = ((UIObject)p);
            uploader = new FileUploader(environment, contextObject, reader);
            reader = null;
        }
    }
    
    public FileUploader getUploader(){
        return uploader;
    }
}

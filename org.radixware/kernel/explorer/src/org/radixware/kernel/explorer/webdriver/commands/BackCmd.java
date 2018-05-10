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

package org.radixware.kernel.explorer.webdriver.commands;

import org.json.simple.JSONObject;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class BackCmd extends NavigationCmd {

    @Override
    public String getName() {
        return "back";
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.POST;
    }

	@Override
	public JSONObject execute(WebDrvSession session, WebDrvUri uri, JSONObject parameter) throws WebDrvException {
        super.execute(session, uri, parameter);
        
		if(!this.isMainWindowOnTop())
		{
			throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE, "Main window is hovered by some other modal window. Close all modal windows then click \"Back\" button.");
		}
		Action a = this.getMainTree(session).getActions().getGoBackAction();
		
		if(a.isEnabled())
		{
			a.trigger();
		}
		return null;
	}
}
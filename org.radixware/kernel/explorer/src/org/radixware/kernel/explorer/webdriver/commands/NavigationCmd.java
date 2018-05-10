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

package org.radixware.kernel.explorer.webdriver.commands;

import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import static org.radixware.kernel.explorer.webdriver.commands.WindowCmd.getTopLevelWindow;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;


abstract class NavigationCmd extends GuiCmd{

    @Override
    public boolean isInputActionCommand() {
        return true;
    }    
    
    /**
     * Возвращает дерево главного окна, а если его нет - бросает сиключение EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE.
     */
    protected IExplorerTree getMainTree(WebDrvSession session) throws WebDrvException {
        IExplorerTree t = session.getEnvironment().getTreeManager().getCurrentTree();
        if(t == null) {
            throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE, "Main window tree is empty because RW Explorer is not connected to RW Server. Make RW Explorer connect to RW Server first.");
        }
        return t;
    }    
    
	/**
	 * Возвращает true если главное окно в данный момент не перекрыто модальными окнами, иначе возвращает false.
	 */
	protected boolean isMainWindowOnTop() throws WebDrvException {
		return getTopLevelWindow() == Application.getMainWindow();
	}    

}

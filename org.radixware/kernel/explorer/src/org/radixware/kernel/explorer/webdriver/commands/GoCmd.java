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

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public final class GoCmd extends NavigationCmd {

    @Override
    public String getName() {
        return "url";
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
        
		final WebDrvUIElementsManager manager = session.getUIElements();
		
		if(!this.isMainWindowOnTop()) {
			throw new WebDrvException(EWebDrvErrorCode.ELEMENT_NOT_INTERACTABLE, "Main window is hovered by some other modal window. Close all modal windows then navigate.");
		}
		JSONObject jo = super.execute(session, uri, parameter); // handlePrompt
		if(jo != null) return jo;
		String pathToGo = (String)parameter.get("url");
		if(pathToGo == null || pathToGo.isEmpty()) {
			throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "url must be a valid absolute path in main window tree. For example 'Entire System/System Settings/Schedules/Calendars/Absolute Calendar: 21) Каждые 2 минуты/Calendar Items'.");
		}
        IExplorerTree tree = getMainTree(session);
        if(!gotoPath(tree, pathToGo)) {
            throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_ELEMENT);
        }
        return null;

	}
	
    /**
     * В дереве tree переходит к узлу pathToGo раскрывая по пути узлы. Напрмер "Entire System/System Settings/System Instances"
     */
    public static boolean gotoPath(IExplorerTree tree, String pathToGo) {
        List<IExplorerTreeNode> current = tree.getRootNodes();
		List<IExplorerTreeNode> path = new ArrayList<IExplorerTreeNode>();
		for(String item : pathToGo.split("/")) {
			IExplorerTreeNode selected = selectNode(item, current);
			if(selected == null) return false;//throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_ELEMENT);
			path.add(selected);
			current = selected.getChildNodes();
		}
		// путь найден, теперь ракрываю узлы
		for(IExplorerTreeNode node : path) {
			node.getView().expand();
		}
		if(path.size() > 0) {
			path.get(path.size() - 1).getView().setCurrent();
            return true;
		}
        return false;
    }
    
	/**
	 * Вовзращает первый узел из списка list, у которого текст или идетификатор равен refer, или возвращает null.
	 */
	private static IExplorerTreeNode selectNode(String refer, List<IExplorerTreeNode> list) {
		for(IExplorerTreeNode node : list) {
			if(!node.isValid()) continue;
			if(node.getView().getTitle().equalsIgnoreCase(refer)) return node;

			if(node.getView().isChoosenObject()) {
				if(node.getView().getChoosenEntityInfo().pid.toStr().equalsIgnoreCase(refer)) return node;
			}
			else {
				if(node.getView().getExplorerItemId().toString().equalsIgnoreCase(refer)) return node;
			}
		}
		return null;
	}

}

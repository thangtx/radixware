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

package org.radixware.kernel.explorer.editors.xml;

import java.util.ArrayList;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;

public class TableEditError extends QTableWidgetItem {

	private XmlError error = null;
	private TreeWindow tw = null;
	
	public TableEditError (XmlError e,TreeWindow t) {
		error = e;
		tw = t;
	}
	
	public void toErrorLocation(){
		XmlCursor location = error.getCursorLocation();
		QTreeWidget tree = tw.getTree();
		if (location.currentTokenType().intValue() == TokenType.INT_STARTDOC)
    		location.toFirstChild();
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		if (location.getDomNode().getNodeType()==Node.ATTRIBUTE_NODE){
			int atrs = 1;
			while (location.currentTokenType().intValue()!=TokenType.INT_START){
				location.toPrevToken();
				if (location.currentTokenType().intValue()==TokenType.INT_ATTR)
					atrs++;
			}
			indexes.add(atrs-1);
		}

		XmlObject x = location.getObject();

        if (x != null){
    		while (x.getDomNode().getParentNode().getNodeType() != Node.DOCUMENT_NODE){
	    		ArrayList<XmlObject> attrs = XElementTools.getCurrentAttributes(XElementTools.getParentNode(x));
		    	int a = 0;
			    if (attrs!=null)
    				a = attrs.size();
	    		indexes.add(getNodeIndex(x.getDomNode(),x.getDomNode().getParentNode())+a);
		    	x = XElementTools.getParentNode(x);
		    }
        }
		boolean stop=false;
		int t = 0;
		while (!stop){
			if (tree.topLevelItem(t) instanceof XTreeTag){
				indexes.add(t);
				stop=true;
			}else{
				t++;
			}
		}
		moveToErrorNode(tree,indexes);
	}
	
	private int getNodeIndex(Node x,Node parent){
		NodeList childs = parent.getChildNodes();
		for (int i=0;i<=childs.getLength()-1;i++){
			Node i_child = childs.item(i);//NodeList.item(i) returns null if corresponding node is invalid
			if (i_child!=null && i_child.equals(x)){
				return i;
			}
		}
		return 0;
	}
	
	private void moveToErrorNode(QTreeWidget tree,ArrayList<Integer> indexes){
		int i = indexes.size()-1;
		QTreeWidgetItem item = tree.topLevelItem(indexes.get(i));
		item.setExpanded(true);
		i--;
		while (i >= 0){
			item = item.child(indexes.get(i));
			item.setExpanded(true);
			i--;
		}
		if (tree.selectedItems().size() != 0)
			tree.selectedItems().get(0).setSelected(false);
		tree.setCurrentItem(item);
		tree.setFocus();
		item.setSelected(true);
	}
	
	public XmlError getError(){
		return error;
	}
	
}

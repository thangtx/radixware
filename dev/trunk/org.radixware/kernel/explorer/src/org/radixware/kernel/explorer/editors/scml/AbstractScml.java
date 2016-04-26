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

package org.radixware.kernel.explorer.editors.scml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.scml.Scml;


public abstract class AbstractScml implements IScml {

    private final List<IScmlItem> items;

    public AbstractScml() {
        items = new LinkedList<IScmlItem>();
    }

    @Override
    public List<IScmlItem> getItems() {
        return items;
    }

    @Override
    public void add(IScmlItem item) {
        items.add(item);
    }

    @Override
    public void add(int position, IScmlItem item) {
        items.add(position, item);
    }

    @Override
    public void add(int position, int offset, IScmlItem item) {
        List<IScmlItem> newItems = new ArrayList<IScmlItem>();
        newItems.add(item);
        add(position, offset, newItems);
    }

    @Override
    public void add(int position, int offset, List<IScmlItem> newItems) {
        IScmlItem preItem = getItemByOffset(position, offset, true);
        IScmlItem postItem = getItemByOffset(position, offset, false);

        items.remove(position);
        items.add(position, preItem);
        for (IScmlItem item : newItems) {
            position++;
            items.add(position, item);
        }
        items.add(position + 1, postItem);

    }

    @Override
    public IScml getCopy() {
        IScml newScml = newInstance();
        for (int i = 0; i < items.size(); i++) {
            newScml.add(items.get(i).getCopy());
        }
        return newScml;
    }

    @Override
    public IScml getCopy(int from, int to) {
        IScml newScml = newInstance();
        for (int i = from; i < to; i++) {
            newScml.add(items.get(i).getCopy());
        }
        return newScml;
    }

    @Override
    public IScml getCopy(int from, int fromOffset, int to, int toOffset) {
        IScml newScml = newInstance();

        IScmlItem itemFrom = getItemByOffset(from, fromOffset, false);
        IScmlItem itemTo = getItemByOffset(to, toOffset, true);

        newScml.add(itemFrom);
        for (int i = from + 1; i < to; i++) {
            newScml.add(items.get(i).getCopy());
        }
        newScml.add(itemTo);

        return newScml;
    }

    private IScmlItem getItemByOffset(int position, int offset, boolean isBeforeOffset) {
        IScmlItem item = items.get(position);
        if (item.isText()) {
            int start = isBeforeOffset ? 0 : offset;
            int end = isBeforeOffset ? offset : ((ScmlText) item).getText().length();
            return item.getCopy(start, end);
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public abstract XmlObject saveToXml();

    @Override
    public abstract void loadFromXml(XmlObject xmlObject) throws IOException;

    public boolean loadFromStr(String str) {
        try {
            XmlObject xmlObj = XmlObject.Factory.parse(str);
            loadFromXml(xmlObj);
            return true;
        } catch (XmlException e) {//Text is not valud xml text,try to append it as plain text item
            loadPlainText(str);
            return true;
        } catch (IOException e) {//Text is valid XML but not suitable for this kind of Scml. This case must be handled in loadFromXml
            return false;
        }
    }

    protected void loadPlainText(String str) {
        getItems().clear();
        add(new ScmlText(str));
    }

    public abstract IScml newInstance();
}

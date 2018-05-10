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

package org.radixware.wps.rwt;

import org.radixware.kernel.common.html.Html;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.html.Div;


public class AbstractContainer extends UIObject {

    private List<UIObject> children;

    public AbstractContainer() {
        super(new Div());
        html.addClass("rwt-ui-background");
    }

    public AbstractContainer(Html html) {
        super(html);
        html.addClass("rwt-ui-background");
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        if (children != null) {
            for (UIObject obj : new ArrayList<>(children)) {
                obj.visit(visitor);
            }
        }
    }

    public void add(UIObject child) {
        add(-1, child);
    }

    public void add(int index, UIObject child) {
        if (child == null) {
            return;
        }
        if (children == null) {
            children = new LinkedList<>();
        }
        if (index < 0 || index >= children.size()) {
            children.add(child);
        } else {
            children.add(index, child);
        }
        child.setParent(this);
        if (child instanceof Dialog) {
            RootPanel root = findRoot();
            if (root != null) {
                root.getHtml().add(child.getHtml());
            }
        } else {
            this.getHtml().add(index, child.getHtml());
        }
    }

    public List<UIObject> getChildren() {
        if (children == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(children);
        }
    }

    public void remove(UIObject child) {
        if (children != null) {

            if (children.remove(child)) {
                this.getHtml().remove(child.getHtml());
                if (child instanceof Dialog) {
                    RootPanel root = findRoot();
                    if (root != null) {
                        root.getHtml().remove(child.getHtml());
                    }
                }
                child.setParent(null);
            }
        }
    }

    public void clear() {
        if (children != null) {
            List<UIObject> cls = new ArrayList<>(children);
            for (UIObject c : cls) {
                remove(c);
            }
            children = null;
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        } else {
            if (children == null) {
                return null;
            } else {
                for (UIObject obj : children) {
                    result = obj.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
                return null;
            }
        }
    }
}

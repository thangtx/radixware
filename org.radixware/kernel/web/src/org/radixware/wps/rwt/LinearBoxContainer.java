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

import org.radixware.kernel.common.utils.Utils;


public abstract class LinearBoxContainer extends AbstractContainer {

    protected LinearBoxContainer(final String layoutClassName) {
        html.layout("$RWT." + layoutClassName + ".layout");
    }

    public int getPadding() {
        String val = html.getAttr("internal-padding");
        if (val == null) {
            return 0;
        } else {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    public void setPadding(int padding) {
        if (padding <= 0) {
            html.setAttr("internal-padding", null);
        } else {
            html.setAttr("internal-padding", padding);
        }
    }

    public boolean isAutoSize(UIObject obj) {
        if (obj == null) {
            return false;
        }
        String attr = html.getAttr("autoresize");
        if (attr == null || attr.isEmpty()) {
            return false;
        } else {
            return attr.indexOf(obj.getHtmlId()) >= 0;
        }
    }
    private boolean isAutoAutoSize = false;

    public void setAutoSizeChildren(boolean set) {
        isAutoAutoSize = set;
        for (UIObject obj : getChildren()) {
            setAutoSize(obj, set);
        }
    }

    public boolean isAutoSizeChildren() {
        return isAutoAutoSize;
    }

    @Override
    public void add(UIObject child) {
        super.add(child); //To change body of generated methods, choose Tools | Templates.
        if (isAutoAutoSize) {
            setAutoSize(child, true);
        }
    }

    @Override
    public void add(int index, UIObject child) {
        super.add(index, child); //To change body of generated methods, choose Tools | Templates.
        if (isAutoAutoSize) {
            setAutoSize(child, true);
        }
    }

    @Override
    public void remove(UIObject child) {
        setAutoSize(child, false);
        super.remove(child);
    }

    public void setAutoSize(UIObject obj, boolean set) {
        if (getChildren().contains(obj)) {
            String attr = html.getAttr("autoresize");
            if (attr != null) {
                String[] items = attr.split(",");
                if (set) {
                    String id = obj.getHtmlId();
                    for (int i = 0; i < items.length; i++) {
                        if (Utils.equals(items[i], id)) {
                            return;
                        }
                    }
                    if (attr.isEmpty()) {
                        attr = id;
                    } else {
                        attr += "," + id;
                    }
                } else {
                    String id = obj.getHtmlId();
                    int dropIndex = -1;
                    for (int i = 0; i < items.length; i++) {
                        if (Utils.equals(items[i], id)) {
                            dropIndex = i;
                            break;
                        }
                    }
                    if (dropIndex == -1) {
                        return;
                    }
                    if (items.length == 1) {
                        attr = null;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        boolean first = true;
                        for (int i = 0; i < items.length; i++) {
                            if (i == dropIndex) {
                                continue;
                            }
                            if (first) {
                                first = false;
                            } else {
                                sb.append(",");
                            }
                            sb.append(items[i]);
                        }
                        attr = sb.toString();
                    }
                }
            } else {
                if (set) {
                    attr = obj.getHtmlId();
                }
            }
            if (attr != null && attr.isEmpty()) {
                attr = null;
            }
            html.setAttr("autoresize", attr);
        }
    }

    public abstract void setAlignment(Alignment a);

    public abstract Alignment getAlignment();
}

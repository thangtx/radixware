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

package org.radixware.kernel.common.html;

import java.util.Collections;
import java.util.List;

public final class TextNode extends Html{
    private String text;
    TextNode(String text) {
        super("");
        
        this.text = text;
    }

    @Override
    public void setContextMenuEnabled(boolean enabled) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void removeChoosableMarker() {
    }

    @Override
    public void markAsChoosable() {
    }

    @Override
    public List<String> getClasses() {
        return Collections.emptyList();
    }

    @Override
    public void removeClass(String clazz) {
    }

    @Override
    public boolean containsClass(String clazz) {
        return false;
    }

    @Override
    public void addClass(String clazz) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void setInnerText(String text) {
        this.text = text;
    }

    @Override
    public String getInnerText() {
        return text; 
    }

    @Override
    public void setAttr(String name, int value) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void setAttr(String name, String value) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void setAttr(String name, float value) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void setAttr(String name, boolean value) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public String getStyle() {
        return null;
    }

    @Override
    public String toString(boolean prettyPrint, boolean printId, int indent, boolean isRoot) {
        return text;
    }

    @Override
    public String toString(boolean prettyPrint, boolean printId, int indent, boolean isRoot, boolean closeEmptyTags) {
        return text;
    }

    @Override
    public String toString(boolean closeEmptyTags) {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void setFocusSencitive(boolean sence) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void layout(String type) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public String getCss(String name) {
        return "";
    }

    @Override
    public void setCss(String name, String value) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void resetAttrs() {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void resetCss() {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void setCss(String name, int value) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void setCss(String name, boolean value) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void clear() {
        text = "";
    }

    @Override
    public int indexOfChild(Html html) {
        return -1;
    }

    @Override
    public Html getChildAt(int index) {
        return null;
    }

    @Override
    public int childCount() {
        return 0;
    }

    @Override
    public List<Html> children() {
        return Collections.emptyList();
    }

    @Override
    public void remove(Html html) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void add(int index, Html html) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public void add(Html html) {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    protected Html createCopy() {
        throw new UnsupportedOperationException("This operation is not supported for text node");
    }

    @Override
    public <T> void setUserObject(T userObject) {
        super.setUserObject(userObject); 
    }

    @Override
    public <T> T getUserObject() {
        return super.getUserObject(); 
    }

    @Override
    public String getElementName() {
        return "";
    }
}

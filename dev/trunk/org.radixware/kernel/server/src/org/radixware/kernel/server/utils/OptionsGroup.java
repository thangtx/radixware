/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Class to represent tree like hierarchy of options as string in form of:
 * <pre>
 * group_name {
 *    opt: optval;
 *    subgroup_name {
 *          supbopt: suboptval;
 *    }
 * }
 * </pre>
 */
public class OptionsGroup {

    private static final int DEFAULT_INDENT_STEP = 4;
    private final List<Entry> entries = new ArrayList<>();
    private final String name;

    public OptionsGroup(final String name) {
        this.name = name;
    }

    public OptionsGroup() {
        this(null);
    }

    public String getName() {
        return name;
    }

    public Iterable<Entry> entries() {
        return new Iterable<Entry>(){@Override public Iterator<Entry> iterator() {return entries.iterator();}};
    }

    public boolean containsItem(final String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            throw new IllegalArgumentException("Item name can't be null");
        }
        else if (itemName.indexOf('.') != -1) {
            final Object item = getItemValue(itemName.substring(0,itemName.indexOf('.')));
            
            if ((item != null) && (item instanceof OptionsGroup)) {
                return ((OptionsGroup)item).containsItem(itemName.substring(itemName.indexOf('.')+1));
            }
            else {
                return false;
            }
        }
        else {
            for (Entry item : entries()) {
                if (itemName.equals(item.getName())) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public Object getItemValue(final String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            throw new IllegalArgumentException("Item name can't be null");
        }
        else if (itemName.indexOf('.') != -1) {
            final Object item = getItemValue(itemName.substring(0,itemName.indexOf('.')));
            
            if ((item != null) && (item instanceof OptionsGroup)) {
                return ((OptionsGroup)item).getItemValue(itemName.substring(itemName.indexOf('.')+1));
            }
            else {
                return null;
            }
        }
        else {
            for (Entry item : entries()) {
                if (itemName.equals(item.getName())) {
                    return item.getValue();
                }
            }
            return null;
        }
    }
    
    public OptionsGroup add(String name, final Object value) {
        entries.add(new Entry(name, value));
        return this;
    }
    
    public OptionsGroup addGroup(final String name) {
        final OptionsGroup group = new OptionsGroup(name);
        entries.add(new Entry(null, group));
        return group;
    }

    @Override
    public String toString() {
        return toString(0, DEFAULT_INDENT_STEP);
    }

    public String toString(int startIndent, int indentStep) {
        return toString(startIndent, indentStep, null);
    }

    public String toString(int startIndent, int indentStep, String explicitName) {
        final String indentStr = getIndendStr(startIndent);
        final String internalIndentStr = indentStr + getIndendStr(indentStep);
        final StringBuilder sb = new StringBuilder();
        sb.append(indentStr);
        String nameStr = explicitName == null ? name : explicitName;
        if (nameStr != null) {
            sb.append(nameStr).append(" ");
        }
        sb.append("{");
        if (!entries.isEmpty()) {
            sb.append("\n");
            for (Entry entry : entries()) {
                if (entry.value instanceof OptionsGroup) {
                    sb.append(((OptionsGroup) entry.value).toString(startIndent + indentStep, indentStep, name)).append(";\n");
                } else {
                    sb.append(internalIndentStr).append(entry.name).append(": ").append(entry.value).append(";\n");
                }
            }
        }
        sb.append(indentStr).append("}");
        return sb.toString();
    }

    private String getIndendStr(final int indent) {
        final StringBuilder sb = new StringBuilder(indent);
        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.entries);
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OptionsGroup other = (OptionsGroup) obj;
        if (!Objects.equals(this.entries, other.entries)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public static class Entry {

        private final String name;
        private final Object value;

        public Entry(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }

    }

}

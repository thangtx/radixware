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

package org.radixware.kernel.designer.eas.client;

import java.awt.Dimension;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.types.Id;


public class DesignerConfigStore implements ClientSettings {

    @Override
    public boolean contains(String string) {
        return false;
    }

    @Override
    public void writeBoolean(String string, boolean bln) {
    }

    @Override
    public void writeInteger(String string, int i) {
    }

    @Override
    public void writeString(String string, String string1) {
    }

    @Override
    public void writeId(String string, Id id) {
    }

    @Override
    public void writePid(String string, Pid pid) {
    }

    @Override
    public void writeDouble(String string, double d) {
    }

    @Override
    public void setValue(String string, Object o) {
    }

    @Override
    public boolean readBoolean(String string, boolean bln) {
        return bln;
    }

    @Override
    public boolean readBoolean(String string) {
        return false;
    }

    @Override
    public int readInteger(String string, int i) {
        return i;
    }

    @Override
    public int readInteger(String string) {
        return 0;
    }

    @Override
    public double readDouble(String string, double d) {
        return d;
    }

    @Override
    public double readDouble(String string) {
        return 0;
    }

    @Override
    public String readString(String string) {
        return "";
    }

    @Override
    public String readString(String string, String string1) {
        return string1;
    }

    @Override
    public Id readId(String string) {
        return null;
    }

    @Override
    public Pid readPid(String string) {
        return null;
    }

    @Override
    public Object value(String string) {
        return null;
    }

    @Override
    public Object value(String string, Object o) {
        return o;
    }

    @Override
    public List<String> allKeys() {
        return Collections.emptyList();
    }

    @Override
    public void beginGroup(String string) {
    }

    @Override
    public void setConfigProfile(String string) {
    }

    @Override
    public int beginReadArray(String string) {
        return 0;
    }

    @Override
    public void beginWriteArray(String string) {
    }

    @Override
    public void beginWriteArray(String string, int i) {
    }

    @Override
    public List<String> childGroups() {
        return Collections.emptyList();
    }

    @Override
    public List<String> childKeys() {
        return Collections.emptyList();
    }

    @Override
    public void clear() {
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public void endArray() {
    }

    @Override
    public void endGroup() {
    }

    @Override
    public void endAllGroups() {
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public void sync() {
    }

    @Override
    public void remove(String string) {
    }

    @Override
    public void setArrayIndex(int i) {
    }

    @Override
    public ITextOptions readTextOptions(String string) {
        return null;
    }

    @Override
    public void writeTextOptions(String string, ITextOptions ito) {        
    }
       

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public Dimension readSize(String string) {
        return null;
    }
}

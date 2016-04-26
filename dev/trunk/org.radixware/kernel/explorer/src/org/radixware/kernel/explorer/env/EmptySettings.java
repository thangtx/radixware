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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import java.awt.Dimension;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


class EmptySettings implements IExplorerSettings {

    private static final EmptySettings INSTANCE = new EmptySettings();

    private EmptySettings() {
    }

    public static EmptySettings getInstance() {
        return INSTANCE;
    }

    @Override
    public void writeQFont(String key, QFont font) {
    }

    @Override
    public void writeQPoint(String key, QPoint point) {
    }

    @Override
    public void writeQAlignmentFlag(String key, AlignmentFlag alignmentFlag) {
    }

    @Override
    public void writeQSize(String key, QSize size) {
    }

    @Override
    public void writeQColor(String key, QColor color) {
    }

    @Override
    public void writeQByteArray(String key, QByteArray array) {
    }

    @Override
    public QFont readQFont(String key) {
        return null;
    }

    @Override
    public QPoint readQPoint(String key) {
        return null;
    }

    @Override
    public AlignmentFlag readAlignmentFlag(String key) {
        return null;
    }

    @Override
    public QSize readQSize(String key) {
        return null;
    }

    @Override
    public QColor readQColor(String key) {
        return null;
    }

    @Override
    public QByteArray readQByteArray(String key) {
        return null;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public void writeBoolean(String key, boolean b) {
    }

    @Override
    public void writeInteger(String key, int i) {
    }

    @Override
    public void writeString(String key, String s) {
    }

    @Override
    public void writeId(String key, Id id) {
    }

    @Override
    public void writePid(String key, Pid pid) {
    }

    @Override
    public void writeDouble(String key, double d) {
    }

    @Override
    public void setValue(String key, Object value) {
    }

    @Override
    public boolean readBoolean(String key, boolean defaultBoolean) {
        return defaultBoolean;
    }

    @Override
    public boolean readBoolean(String key) {
        return false;
    }

    @Override
    public int readInteger(String key, int defaultInteger) {
        return defaultInteger;
    }

    @Override
    public int readInteger(String key) {
        return 0;
    }

    @Override
    public double readDouble(String key, double defaultDouble) {
        return defaultDouble;
    }

    @Override
    public double readDouble(String key) {
        return Double.NaN;
    }

    @Override
    public String readString(String key) {
        return null;
    }

    @Override
    public String readString(String key, String defaultString) {
        return defaultString;
    }

    @Override
    public Id readId(String key) {
        return null;
    }

    @Override
    public Pid readPid(String key) {
        return null;
    }

    @Override
    public Object value(String key) {
        return null;
    }

    @Override
    public Object value(String key, Object defaultValue) {
        return defaultValue;
    }

    @Override
    public List<String> allKeys() {
        return Collections.<String>emptyList();
    }

    @Override
    public void beginGroup(String group) {
    }

    @Override
    public void setConfigProfile(String profile) {
    }

    @Override
    public int beginReadArray(String array) {
        return -1;
    }

    @Override
    public void beginWriteArray(String array) {
    }

    @Override
    public void beginWriteArray(String array, int i) {
    }

    @Override
    public List<String> childGroups() {
        return Collections.<String>emptyList();
    }

    @Override
    public List<String> childKeys() {
        return Collections.<String>emptyList();
    }

    @Override
    public void clear() {
    }

    @Override
    public String group() {
        return null;
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
        return false;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public void sync() {
    }

    @Override
    public void remove(String group) {
    }

    @Override
    public void setArrayIndex(int i) {
    }

    @Override
    public ITextOptions readTextOptions(String path) {
        return ExplorerTextOptions.EMPTY;
    }

    @Override
    public void writeTextOptions(String key, ITextOptions textSettings) {        
    }
        
    @Override
    public String getDescription() {
        return "Empty settings";
    }

    @Override
    public Dimension readSize(String key) {
        return null;
    }
}

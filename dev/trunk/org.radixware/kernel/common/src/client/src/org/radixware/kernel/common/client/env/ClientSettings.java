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

package org.radixware.kernel.common.client.env;

import java.awt.Dimension;
import java.util.List;
import org.radixware.kernel.common.client.text.ITextOptions;

import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.types.Id;

/**
 * The ExplorerSettings class provides persistent explorer settings.
 * <p>
 */
public interface ClientSettings {

    public static final String CONFIG_VERSION = "configversion";
    public static final int configVersion = 3;
    public static final int defaultConfigVersion = 22;
    public static final String DEFAULT_SETTINGS_FILE_NAME = "defaults.ini";    

    public boolean contains(final String key);
    /////////////////////write methods

    public void writeBoolean(final String key, final boolean b);

    public void writeInteger(final String key, final int i);

    public void writeString(final String key, final String s);

    public void writeId(final String key, final Id id);

    public void writePid(final String key, final Pid pid);

    public void writeDouble(final String key, final double d);

    public void setValue(final String key, final Object value);

    public boolean readBoolean(final String key, final boolean defaultBoolean);

    public boolean readBoolean(final String key);

    public int readInteger(final String key, final int defaultInteger);

    public int readInteger(final String key);

    public double readDouble(final String key, final double defaultDouble);

    public double readDouble(final String key);

    public String readString(final String key);

    public String readString(final String key, final String defaultString);

    public Id readId(final String key);

    public Pid readPid(final String key);

    public Object value(final String key);

    public Object value(final String key, final Object defaultValue);

    //////// QSettings interface
    public List<String> allKeys();

    public void beginGroup(final String group);

    public void setConfigProfile(final String profile);

    public int beginReadArray(final String array);

    public void beginWriteArray(final String array);

    public void beginWriteArray(final String array, final int i);

    public List<String> childGroups();

    public List<String> childKeys();

    public void clear();

    public String group();

    public void endArray();

    public void endGroup();

    public void endAllGroups();

    public boolean isWritable();

    public boolean isInvalid();

    public void sync();

    public void remove(final String group);

    public void setArrayIndex(int i);
    
    public ITextOptions readTextOptions(final String path);
    
    public void writeTextOptions(final String key, ITextOptions textSettings);

    public String getDescription();

    public Dimension readSize(final String key);
}

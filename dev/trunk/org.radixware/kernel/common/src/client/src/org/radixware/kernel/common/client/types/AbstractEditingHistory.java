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

package org.radixware.kernel.common.client.types;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;


public abstract class AbstractEditingHistory implements IEditingHistory {

    protected final static int MAX_SIZE = 30;
    private final static String SETTING = "editingHistory";
    private final static String SETTING_ENTRY = "entry";
    protected final transient List<String> valuesAsStr = new LinkedList<String>();
    private final IClientEnvironment environment;
    private final ClientSettings clientSettings;
    private final String settingPath;
    
    public AbstractEditingHistory(final IClientEnvironment environment, final String settingPath) {
        this.environment = environment;
        this.clientSettings = environment.getConfigStore();
        this.settingPath = settingPath;
    }
    
    protected void init(final String settingPath) {
        clientSettings.beginGroup(settingPath);
        final int arraySize = clientSettings.beginReadArray(SETTING);
        try {
            for(int i = arraySize - 1; i >= 0; i--) {
                clientSettings.setArrayIndex(i);
                final String currentEntry = clientSettings.readString(SETTING_ENTRY);
                addEntry(currentEntry);
            }
        } catch (Exception e) {
            environment.getTracer().error(e);
        } finally {
            clientSettings.endArray();
            clientSettings.endGroup();
        }
    }
    
    @Override
    public void addEntry(final String entry) {
        if(entry != null && !entry.isEmpty()) {
            if(valuesAsStr.contains(entry)) {
                valuesAsStr.remove(entry);
            } else if(valuesAsStr.size() >= MAX_SIZE) {
                valuesAsStr.remove(getSize() - 1);
            }
            valuesAsStr.add(0, entry);
        }
    }

    @Override
    public void clear() {
        clientSettings.beginGroup(settingPath);
        clientSettings.remove(SETTING);
        clientSettings.endGroup();
        valuesAsStr.clear();
    }

    @Override
    public void flush() throws EditingHistoryException {
        if(!valuesAsStr.isEmpty()) {
            clientSettings.beginGroup(settingPath);
            clientSettings.beginWriteArray(SETTING);
            try {
                int index = 0;
                for(String s : valuesAsStr) {
                    clientSettings.setArrayIndex(index++);
                    clientSettings.writeString(SETTING_ENTRY, s);
                }
            } catch(Exception e) {
                environment.getTracer().error(e);
                final String errorMsg = environment.getMessageProvider().translate("EditingHistory", "Couldn't write the editing history of %s on disk");
                throw new EditingHistoryException(String.format(errorMsg, settingPath), e);
            } finally {
                clientSettings.endArray();
                clientSettings.endGroup();
            }
            
        }
    }

    @Override
    public String getEntry(final int row) {
        return valuesAsStr.get(row);
    }

    @Override
    public int getSize() {
        return valuesAsStr.size();
    }

    @Override
    public List<String> getEntries() {
        return Collections.unmodifiableList(valuesAsStr);
    }

    @Override
    public final boolean isEmpty() {
        return valuesAsStr.isEmpty();
    }
}

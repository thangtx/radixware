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

package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.lookup.AdsCommandsLookupSupport;


class CommandsListModel extends AbstractListModel {

    private static Map<Id, AdsCommandDef> commandsIdMap = new HashMap<Id, AdsCommandDef>();
    private LinkedList<CommandItem> commandsList;

    CommandsListModel(List<Id> commands, AdsCommandsLookupSupport context) {
        commandsList = new LinkedList<CommandItem>();//new TreeSet<CommandItem>(CommandItem.ITEM_NAME_COMPARATOR);
        if (context != null && commands.size() > 0) {
            Collection<AdsCommandDef> allCommands = context.getAvailableCommandList();
            for (Id i : commands) {
                commandsList.add(new CommandItem(i, getCommandById(allCommands, i)));
            }
        }
    }

    private AdsCommandDef getCommandById(Collection<AdsCommandDef> all, Id id) {
        AdsCommandDef result = commandsIdMap.get(id);
        if (result == null) {
            for (AdsCommandDef c : all) {
                if (c.getId().toString().equals(id.toString())) {
                    commandsIdMap.put(id, c);
                    return c;
                }
            }
        }
        return result;
    }

    CommandItem getCommandItem(int index) {
        return (CommandItem) getElementAt(index);
    }

    @Override
    public Object getElementAt(int index) {
        return commandsList.get(index);
    }

    @Override
    public int getSize() {
        return commandsList.size();
    }

    void removeCommands(Collection<CommandItem> items) {
        int size = commandsList.size();
        commandsList.removeAll(items);
        fireContentsChanged(this, 0, size);
    }

    void addCommand(CommandItem item) {
        if (!commandsList.contains(item)) {
            int size = commandsList.size();
            commandsList.add(item);
            fireContentsChanged(this, 0, size);
        }
    }

    void moveUp(Object[] items, int[] indexes) {
        final int size = commandsList.size();
        final int length = items.length;
        if (length > 1) {
            for (int i = 0, s = items.length - 1; i <= s; i++) {
                commandsList.remove((CommandItem) items[i]);
                indexes[i] = indexes[i] - 1;
            }
            for (int i = 0, s = items.length - 1; i <= s; i++) {
                commandsList.add(indexes[i], (CommandItem) items[i]);
            }
        } else {
            commandsList.remove((CommandItem) items[0]);
            commandsList.add(indexes[0] - 1, (CommandItem) items[0]); 
        }
        fireContentsChanged(this, 0, size);
    }

    void moveDown(Object[] items, int[] indexes) {
        final int size = commandsList.size();
        final int length = items.length;
        if (length > 1) {
            for (int i = 0, s = items.length - 1; i <= s; i++) {
                commandsList.remove((CommandItem) items[i]);
                indexes[i] = indexes[i] + 1;
            }
            for (int i = 0, s = items.length - 1; i <= s; i++) {
                commandsList.add(indexes[i], (CommandItem) items[i]);
            }
        } else {
            commandsList.remove((CommandItem) items[0]);
            commandsList.add(indexes[0] + 1, (CommandItem) items[0]);
        }
        fireContentsChanged(this, 0, size);
    }
}

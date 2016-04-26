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

import java.text.Collator;
import java.util.Comparator;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.types.Id;


public class CommandItem /*implements Comparable<CommandItem>*/{

//        static final Comparator<CommandItem> ITEM_NAME_COMPARATOR;
//
//        static {
//            ITEM_NAME_COMPARATOR = new Comparator<CommandItem>(){
//
//                @Override
//                public int compare(CommandItem o1, CommandItem o2) {
//                    int result = Collator.getInstance().compare(o1.toString(), o2.toString());
//                    return result != 0 ? result : o1.compareTo(o2);
//                }
//
//            };
//        }

        Id id;
        AdsCommandDef command;

        CommandItem(Id id, AdsCommandDef context){
            this.id = id;
            this.command = context;
        }

//        @Override
//        public int compareTo(CommandItem o) {
//            return getIdStr().compareTo(o.getIdStr());
//        }

        @Override
        public String toString() {
            return command != null ? command.getName() : id.toString();
        }

        public String getIdStr(){
            return id.toString();
        }
}

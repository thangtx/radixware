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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.util.Comparator;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.resources.RadixWareIcons;


class RadixPlatformClassListItem implements Comparable<RadixPlatformClassListItem> {

    static public final Comparator<RadixPlatformClassListItem> PLATFORM_CLASS_COMPARATOR;
    static {
        PLATFORM_CLASS_COMPARATOR = new Comparator<RadixPlatformClassListItem>(){

            @Override
            public int compare(RadixPlatformClassListItem o1, RadixPlatformClassListItem o2) {
                int result = o1.compareTo(o2);
                if (result == 0){
                    result = o1.getFullName().compareTo(o2.getFullName());
                }
                return result;
            }

        };
    }

    static final RadixIcon CLASS_ICON = RadixWareIcons.JAVA.CLASS;
    static final RadixIcon SUBCLASS_ICON = RadixWareIcons.DIALOG.OK;
    static final RadixIcon PACK_ICON = RadixWareIcons.JAVA.PACKAGE;
    static final RadixIcon NONE_ICON = RadixWareIcons.METHOD.VOID;

    private String fullName;
    private String ownerName;
    private String shortName;
    private RadixIcon icon;
    private RadixIcon ownerIcon;

    RadixPlatformClassListItem(String fullName){
        this.fullName = fullName;
        int dot = -1;
        if (fullName != null){
            dot = fullName.lastIndexOf(".");
            if (dot != -1){
                ownerName = fullName.substring(0, dot);
                shortName = fullName.substring(dot+1, fullName.length());
                int ownerDot = ownerName.lastIndexOf(".");
                boolean stop = false;
                while (ownerDot != -1 && !stop){
                    String sub = ownerName.substring(ownerDot+1, ownerName.length());
                    if (beginsFromCapital(sub)){
                        shortName = sub + "." + shortName;
                        ownerName = ownerName.substring(0, ownerDot);
                        ownerDot = ownerName.lastIndexOf(".");
                    } else {
                        stop = true;
                    }
                }
                if (shortName.contains("."))
                    icon = SUBCLASS_ICON;
            } else {
                if (beginsFromCapital(fullName)){
                    ownerName = "<Not defined>";
                    shortName = fullName;
                }else {
                    ownerName = fullName;
                    shortName = "<Not defined>";
                }
            }
            icon = CLASS_ICON;
            ownerIcon = PACK_ICON;
        } else {
            this.fullName = "<Not defined>";
            ownerName = "<Not defined>";
            shortName = "<Not defined>";
            icon = NONE_ICON;
            ownerIcon = NONE_ICON;
        }
    }

    private boolean beginsFromCapital(String str){
//        return str.matches("[A-Z].*");
        if(str == null || str.isEmpty()) {
            return false;
        }
        return Character.isUpperCase(str.charAt(0));
    }

    RadixIcon getIcon(){
        return icon;
    }

    RadixIcon getOwnerIcon(){
        return ownerIcon;
    }

    @Override
    public String toString() {
        return shortName;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public String getFullName(){
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public int compareTo(RadixPlatformClassListItem o) {
        int result = toString().compareTo(o.toString());
        return result;
    }

}

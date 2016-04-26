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

package org.radixware.kernel.common.svn;


public enum RadixIssueComitType  {

    MAJOR_IMPROVEMENT("Major improvement", "++"),
    MINOR_IMPROVEMENT("Minor improvement", "+"),
    CHANGE("Change", "*"),
    DELETION("Deletion", "-"),
    BUG_FIX("Bug fix", "#"),
    INCOMPLETE("Incomplete", ".i"),    
    REFACTORING("Refactoring", ".r"),
    INTERNAL_IMPROVEMENT("Internal improvement", ".ii"),
    INTERNAL_BUG_FIX("Internal bugfix", ".ib")
    //MERGE("Merge", ".sm")
    ;
    

//    '++' - Major improvement
//    '+' - Minor improvement
//    '*' - Change
//    '-' - Deletion
//    '#' - Bug fix
//    '.i' - Incomplete
//    '.r' - Refactoring
//    '.ii' - Internal improvement
//    '.ib' - Internal bugfix
//    '.sm' - Merge

    
    private final String title;
    private final String value;

    private RadixIssueComitType(final String title, final String value) {
        this.title = title;
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return title;
    }

    
         
    public String getName() {
        return title;
    }
 
 
}

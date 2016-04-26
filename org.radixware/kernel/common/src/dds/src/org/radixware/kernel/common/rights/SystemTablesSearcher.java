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

package org.radixware.kernel.common.rights;

import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProvider;
import org.radixware.kernel.common.types.Id;


public class SystemTablesSearcher {

    public static final Id USER2ROLE_ID = Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4");
    //  tbl42K4K2TTGLNRDHRZABQAQH3XQ4
    public static final Id USERGROUP2ROLE_ID = Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4");
    public static final Id USER2USERGROUP_ID = Id.Factory.loadFrom("tblDYWJCJTTGLNRDHRZABQAQH3XQ4");
    public static final Id SEQ_USER2ROLE_ID = Id.Factory.loadFrom("sqnGYUQGTO7F3OBDHZTABQAQH3XQ4");
    //private static final Id SEQ_USERGROUP2ROLE_ID = Id.Factory.loadFrom("sqnMRHLHY66F3OBDHZTABQAQH3XQ4");
    public static final Id PKG_DRC_ID = Id.Factory.loadFrom("pkgYQZTLHT2YDORDIE4ABQAQH3XQ4");
    private DdsTableDef clerk2Role = null;
    private DdsTableDef clerkGroup2Role = null;
    private DdsTableDef clerk2ClerkGroup = null;
    private DdsSequenceDef seq_User2Role = null;
    private DdsPackageDef pkg_drc = null;

    DdsPackageDef getPackageDrc() {
        return pkg_drc;
    }

    DdsSequenceDef getUser2RoleSeq() {
        return seq_User2Role;
    }

    DdsTableDef getClerk2Role() {
        return clerk2Role;
    }

    DdsTableDef getClerkGroup2Role() {
        return clerkGroup2Role;
    }

    DdsTableDef getClerk2ClerkGroup() {
        return clerk2ClerkGroup;
    }

    int getIndexOverriteUser2Role() {
        return sysVisitorProvider.indexOverriteUser2Role;
    }

    int getIndexOverriteUserGroup2Role() {
        return sysVisitorProvider.indexOverriteUserGroup2Role;
    }

    IVisitor getNoneVisitor() {
        return noneVisitor;
    }
    private IVisitor noneVisitor;
    private SystemTablesVisitorProvider sysVisitorProvider;

    public SystemTablesSearcher(DdsModule module, boolean searchSeq, boolean searchPackage, boolean searchUser2UserGroup) {
        sysVisitorProvider = new SystemTablesVisitorProvider(searchSeq, searchPackage, searchUser2UserGroup);
        noneVisitor = new IVisitor() {
            @Override
            public void accept(RadixObject object) {
                //do nothing !!!
            }
        };
        //clerk2Role = clerkGroup2Role = null;        
        while (module != null) {
            module.visitChildren(noneVisitor, sysVisitorProvider);
            module = module.findOverwritten();
        }
    }

    public class SystemTablesVisitorProvider extends DdsVisitorProvider {

        private boolean searchSeq;
        private boolean searchPackage;
        private boolean searchUser2UserGroup;
        private int indexOverriteUser2UserGroup;
        private int indexOverriteUser2Role;
        private int indexOverriteUserGroup2Role;

        private SystemTablesVisitorProvider(
                boolean searchSeq,
                boolean searchPackage,
                boolean searchUser2UserGroup) {
            this.searchSeq = searchSeq;
            this.searchPackage = searchPackage;
            this.searchUser2UserGroup = searchUser2UserGroup;
            indexOverriteUser2UserGroup = -1;
            indexOverriteUser2Role = -1;
            indexOverriteUserGroup2Role = -1;

        }

        @Override
        public boolean isTarget(RadixObject obj) {
            if (obj instanceof DdsTableDef) {


                DdsTableDef table = (DdsTableDef) obj;
                {


                    if (table.getId().equals(USER2ROLE_ID)) {
                        DdsTableDef templateTable = table;
                        int newIndex = 0;
                        for (;; newIndex++) {
                            templateTable = templateTable.findOverwritten();
                            if (templateTable == null) {
                                break;
                            }
                        }
                        if (newIndex > indexOverriteUser2Role) {

                            indexOverriteUser2Role = newIndex;
                            clerk2Role = table;
                        }
                        //DdsTableDef templateTable = table.findOverwritten();
                        //templateTable.
                        return false;
                    }
                    if (table.getId().equals(USERGROUP2ROLE_ID)) {
                        DdsTableDef templateTable = table;
                        int newIndex = 0;
                        for (;; newIndex++) {
                            templateTable = templateTable.findOverwritten();
                            if (templateTable == null) {
                                break;
                            }
                        }
                        if (newIndex > indexOverriteUserGroup2Role) {

                            indexOverriteUserGroup2Role = newIndex;
                            clerkGroup2Role = table;
                        }
                        return false;
                    }
                    if (searchUser2UserGroup && table.getId().equals(USER2USERGROUP_ID)) {
                        //templateTable.
                        //clerk2ClerkGroup = table;
                        DdsTableDef templateTable = table;
                        int newIndex = 0;
                        for (;; newIndex++) {
                            templateTable = table.findOverwritten();
                            if (templateTable == null) {
                                break;
                            }
                        }
                        if (newIndex > indexOverriteUser2UserGroup) {
                            indexOverriteUser2UserGroup = newIndex;
                            clerk2ClerkGroup = table;
                        }
                        return false;
                    }
                }


                return false;
            }
            if (searchSeq && obj instanceof DdsSequenceDef) {
                DdsSequenceDef seq = (DdsSequenceDef) obj;
                if (seq.getId().equals(SEQ_USER2ROLE_ID)) {
                    seq_User2Role = seq;
                    return false;
                }
            }

            if (searchPackage && obj instanceof DdsPackageDef) {
                DdsPackageDef pkg = (DdsPackageDef) obj;
                if (pkg.getId().equals(PKG_DRC_ID)) {
                    pkg_drc = pkg;
                    return false;
                }
            }
            return false;
        }
    }
}
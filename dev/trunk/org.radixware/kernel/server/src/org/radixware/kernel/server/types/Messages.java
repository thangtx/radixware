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

package org.radixware.kernel.server.types;

import org.radixware.kernel.common.types.Id;


public final class Messages {   
    public static final Id MLS_OWNER_ID = Id.Factory.loadFrom("adcWVZ2HLBXWNBZLFZT4EKNYASGKM");
    
    //PresentationEntityAdapter
    public static final Id MLS_ID_CONFIRM_TO_CLEAR_REFS_TO_OBJECT = Id.Factory.loadFrom("mls57IZ3IRS25C7XF4KYB46AGNUSM");//"Do you want to clear child references to this object: %1?"
    public static final Id MLS_ID_CONFIRM_TO_DELETE_SUBOBJECT = Id.Factory.loadFrom("mlsCWATYS6G4NHQDMP5ATUSND7G6E");//"Do you want to delete subobject: %1?"   
    public static final Id MLS_ID_CANT_DELETE_USED_OBJECT = Id.Factory.loadFrom("mls4MQJUCQPB5C2LJYOOCKLMSKW4Q");//"Can't delete the object because it is used by %1 object."
    
    //Calculation of editor presentation
    static final String MLS_ID_EAS_ED_PRES_CALCULATION = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsC5XX2UP6ORF4DBKBPTYBBQWKT4"; //Calculating editor presentation for object of %1 (#%2) class with \'%3\' identifier.\n%4
    static final Id MLS_ID_EAS_USING_ALL_ED_PRES = Id.Factory.loadFrom("mlsTBXPWEWR3RBXNGJZKSYF2ARDQA");//Desired editor presentations was not defined. Using all presentation defined in class.
    static final Id MLS_ID_EAS_DESIRED_ED_PRESS = Id.Factory.loadFrom("mlsQQM73R6LCVA3XMMWWDWGHHUX5U");//Desired editor presentations:
    static final Id MLS_ID_EAS_DESIRED_ED_PRES = Id.Factory.loadFrom("mlsUIDH7VTYT5AMVM6WWCFYILISVM");//Desired editor presentation:
    static final Id MLS_ID_EAS_PRES_NOT_FOUND = Id.Factory.loadFrom("mlsVYLPYNTS3ZEYTFTM63JACJLEYU");//presentation #%1$s was not found
    static final Id MLS_ID_EAS_NO_ED_PRES = Id.Factory.loadFrom("mlsCOJH77MOVBH2VEHFCYLQU7CVIQ");//There are no accessible editor presentations found.
    static final Id MLS_ID_EAS_NO_ED_PRES_TO_CHOOSE = Id.Factory.loadFrom("mlsRQOBM3YYVZCDFLBETNIS6O5ARI");//No editor presentations provided to choose from.
    static final Id MLS_ID_EAS_PRES_INCOMPATIBLE = Id.Factory.loadFrom("mlsXLWYBKT4VZGNTEMRVIWML2FVRI");//Presentation %1$s has incompatible runtime environment.
    static final Id MLS_ID_EAS_EMPTY_RESULT = Id.Factory.loadFrom("mlsGMBEUH57NRFFFN6RUO72G4UTUI");//Result editor presentations list is empty.
    static final Id MLS_ID_EAS_RESULT_ED_PRES_LIST = Id.Factory.loadFrom("mlsCG44FCMBCVDCBMB7ABETF46P3U");//Result editor presentations list:
    static final Id MLS_ID_EAS_ED_PRES_NOT_CHOSEN = Id.Factory.loadFrom("mlsNWEA5KVJHRGHPNO2BTKJLCNIIE");//Editor presentation was not chosen.
    static final Id MLS_ID_EAS_CHOSEN_ED_PRES = Id.Factory.loadFrom("mls2RARUSS6PBA7JFFYLE4Q3NLU5Q");//Chosen editor presentation: %1$s.
    static final Id MLS_ID_EAS_CANT_ACCESS_PRES_ADD = Id.Factory.loadFrom("mlsXCS2G4OAIVDUDDLJGCY36TRK6Y");//Access to object in presentation %1$s was restricted by PresentationEntityAdapter.
    static final Id MLS_ID_EAS_CANT_ACCESS_PRES = Id.Factory.loadFrom("mlsQDPZP4Z3WRB7TCFMKHQKGTWWPA");//Current user has insufficient rights to access object in editor presentation %1$s.
    static final Id MLS_ID_EAS_CANT_VIEW_PRES_ADD = Id.Factory.loadFrom("mls45WGWUKPENDUHNG66OS32EERCA");//Current user has no access to view object in editor presentation %1$s (restricted in PresentationEntityAdapter).    
    static final Id MLS_ID_EAS_CANT_VIEW_PRES = Id.Factory.loadFrom("mlsMMO7SLO5K5A2ZBGXOUG36GE7YQ");//Current user has no access to view object in editor presentation %1$s.
    static final Id MLS_ID_EAS_CANT_MODIFY_PRES_ADD = Id.Factory.loadFrom("mlsWOCIPZG2XFCODJ6U64KQATOF4U");//Current user has no access to modify object in editor presentation %1$s (restricted in PresentationEntityAdapter).
    static final Id MLS_ID_EAS_CANT_MODIFY_PRES = Id.Factory.loadFrom("mlsOPIOQVZ7QFFLVHKI5GXIQV5R4Y");//Current user has no access to modify object in editor presentation %1$s.
    static final Id MLS_ID_EAS_PRES_ALLOWED_ACTIONS = Id.Factory.loadFrom("mls3N7ND3WETNC57DSEFI3LZDVTUE");//Actions that current user can perform in editor presentation %1$s: %2$s.
    static final Id MLS_ID_EAS_PRES_RESTRICTED_ACTIONS = Id.Factory.loadFrom("mlsTE3UWD5235EG5MREUI6RZ2M5VA");//Actions that restricted in PresentationEntityAdapter: %1$s.
    static final Id MLS_ID_EAS_PRES_REPLACED_WITH_PATH = Id.Factory.loadFrom("mlsWDRMJUNIKZELNEKYCR4BHS7AWE");//Presentation %1$s was replaced by %2$s (%3$s).
    static final Id MLS_ID_EAS_PRES_REPLACED = Id.Factory.loadFrom("mlsW6UPSTTILNFLLMJV7B63GXPWRA");//Presentation %1$s was replaced by %2$s.
    static final Id MLS_ID_EAS_PRES_REPLACED_IN_HANDLER = Id.Factory.loadFrom("mlsX6ZOYSBCBVHNXCWHWFLWK5L6BY");//Presentation %1$s was replaced by %2$s in onCalcEditorPresentation event handler.
}

/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
 
package org.radixware.kernel.utils.wia;

import java.util.EnumSet;

public enum EWiaItemType {

	WIA_ITEM_TYPE_FREE(0x00000000l),
	WIA_ITEM_TYPE_IMAGE(0x00000001l),
	WIA_ITEM_TYPE_FILE(0x00000002l),
	WIA_ITEM_TYPE_FOLDER(0x00000004l),
	WIA_ITEM_TYPE_ROOT(0x00000008l),
	WIA_ITEM_TYPE_ANALYZE(0x00000010l),
	WIA_ITEM_TYPE_AUDIO(0x00000020l),
	WIA_ITEM_TYPE_DEVICE(0x00000040l),
	WIA_ITEM_TYPE_DELETED(0x00000080l),
	WIA_ITEM_TYPE_DISCONNECTED(0x00000100l),
	WIA_ITEM_TYPE_HPANORAMA(0x00000200l),
	WIA_ITEM_TYPE_VPANORAMA(0x00000400l),
	WIA_ITEM_TYPE_BURST(0x00000800l),
	WIA_ITEM_TYPE_STORAGE(0x00001000l),
	WIA_ITEM_TYPE_TRANSFER(0x00002000l),
	WIA_ITEM_TYPE_GENERATED(0x00004000l),
	WIA_ITEM_TYPE_HAS_ATTACHMENTS(0x00008000l),
	WIA_ITEM_TYPE_VIDEO(0x00010000l),
	WIA_ITEM_TYPE_REMOVED(0x80000000l),
	WIA_ITEM_TYPE_DOCUMENT(0x00040000l),
	WIA_ITEM_TYPE_PROGRAMMABLE_DATA_SOURCE(0x00080000l),
	WIA_ITEM_TYPE_MASK(0x800FFFFFl);
	
	private final Long value;
	
	private EWiaItemType(){
		value = null;
	}
	
	private EWiaItemType(final long val){
		value = val;
	}
	
	public Long getValue(){
		return value;
	}
	
    public static EnumSet<EWiaItemType> getFromBitMask(final long mask) {
		final EnumSet<EWiaItemType> result = EnumSet.noneOf(EWiaItemType.class);
		
        for (EWiaItemType type : EWiaItemType.values()) {		
			if ((mask & type.value.longValue()) != 0L) {
				result.add(type);
            }
        }
		
        return result;
    }
}
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

import java.util.Collection;
import java.util.HashSet;

import java.util.Set;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;

public class ModifiableRestrictions{
	private long bitMask;
	private Set<Id> enabledCommandIds;
	private Set<Id> enabledChildIds;
	private Set<Id> enabledPageIds;


	public ModifiableRestrictions(final Restrictions restrictions) {
		bitMask = restrictions.getBitMask();
        enabledCommandIds = restrictions.getEnabledCommandIds() == null ? null : new HashSet<Id>(restrictions.getEnabledCommandIds());
        enabledChildIds = restrictions.getEnabledChildIds() == null ? null : new HashSet<Id>(restrictions.getEnabledChildIds());
        enabledPageIds = restrictions.getEnabledPageIds() == null ? null : new HashSet<Id>(restrictions.getEnabledPageIds());
	}

	public void setAnyCommandRestricted(final boolean flag) {
		if (flag) {
			bitMask = bitMask | ERestriction.ANY_COMMAND.getValue().longValue();
		} else {
			bitMask = bitMask & (~ERestriction.ANY_COMMAND.getValue().longValue());
		}
	}

        public void setAnyChildRestricted(final boolean flag) {
		if (flag) {
			bitMask = bitMask | ERestriction.ANY_CHILD.getValue().longValue();
		} else {
			bitMask = bitMask & (~ERestriction.ANY_CHILD.getValue().longValue());
		}
	}

	public void addEnabledCommand(final Id cmdId) {
		if (enabledCommandIds == null) {
			enabledCommandIds = new HashSet<Id>();
		}
		if (!enabledCommandIds.contains(cmdId)) {
			enabledCommandIds.add(cmdId);
		}
	}

	public void addEnabledChild(final Id childId) {
		if (enabledChildIds == null) {
			enabledChildIds = new HashSet<Id>();
		}
		if (!enabledChildIds.contains(childId)) {
			enabledChildIds.add(childId);
		}
	}
	public void addEnabledPage(final Id pageId) {
		if (enabledPageIds == null) {
			enabledPageIds = new HashSet<Id>();
		}
		if (!enabledPageIds.contains(pageId)) {
			enabledPageIds.add(pageId);
		}
	}
        
    public void setContextlessUsageRestricted(final boolean flag) {
		if (flag) {
			bitMask = bitMask | ERestriction.CONTEXTLESS_USAGE.getValue().longValue();
		} else {
			bitMask = bitMask & (~ERestriction.CONTEXTLESS_USAGE.getValue().longValue());
		}
    }

//	public void removeEnabledCommand(final Id cmdId) {
//		if (enabledCommandIds != null) {
//			enabledCommandIds.remove(cmdId);
//		}
//	}
//
//	public void setCopyRestricted(final boolean flag) {
//		if (flag) {
//			bitMask = bitMask | ERestriction.COPY.getValue().longValue();
//		} else {
//			bitMask = bitMask & (~ERestriction.COPY.getValue().longValue());
//		}
//	}
//
//	public void setCreateRestricted(final boolean flag) {
//		if (flag) {
//			bitMask = bitMask | ERestriction.CREATE.getValue().longValue();
//		} else {
//			bitMask = bitMask & (~ERestriction.CREATE.getValue().longValue());
//		}
//	}
//
//	public void setDeleteRestricted(final boolean flag) {
//		if (flag) {
//			bitMask = bitMask | ERestriction.DELETE.getValue().longValue();
//		} else {
//			bitMask = bitMask & (~ERestriction.DELETE.getValue().longValue());
//		}
//	}
//
//	public void setDeleteAllRestricted(final boolean flag) {
//		if (flag) {
//			bitMask = bitMask | ERestriction.DELETE_ALL.getValue().longValue();
//		} else {
//			bitMask = bitMask & (~ERestriction.DELETE_ALL.getValue().longValue());
//		}
//	}
//
//	public void setMoveRestricted(final boolean flag) {
//		if (flag) {
//			bitMask = bitMask | ERestriction.MOVE.getValue().longValue();
//		} else {
//			bitMask = bitMask & (~ERestriction.MOVE.getValue().longValue());
//		}
//	}
//
//	public void setTransferInRestricted(final boolean flag) {
//		if (flag) {
//			bitMask = bitMask | ERestriction.TRANSFER_IN.getValue().longValue();
//		} else {
//			bitMask = bitMask & (~ERestriction.TRANSFER_IN.getValue().longValue());
//		}
//	}
//
//	public void setTransferOutRestricted(final boolean flag) {
//		if (flag) {
//			bitMask = bitMask | ERestriction.TRANSFER_OUT.getValue().longValue();
//		} else {
//			bitMask = bitMask & (~ERestriction.TRANSFER_OUT.getValue().longValue());
//		}
//	}
//
//	public void setTransferOutAllRestricted(final boolean flag) {
//		if (flag) {
//			bitMask = bitMask | ERestriction.TRANSFER_OUT_ALL.getValue().longValue();
//		} else {
//			bitMask = bitMask & (~ERestriction.TRANSFER_OUT_ALL.getValue().longValue());
//		}
//	}
//
//	public void setUpdateRestricted(final boolean flag) {
//		if (flag) {
//			bitMask = bitMask | ERestriction.UPDATE.getValue().longValue();
//		} else {
//			bitMask = bitMask & (~ERestriction.UPDATE.getValue().longValue());
//		}
//	}

	public void add(final Restrictions restrictions) {
		if ((restrictions.getBitMask() & ERestriction.ANY_COMMAND.getValue().longValue()) != 0) {// commands restricted in restrictions
			if (restrictions.getEnabledCommandIds() == null || restrictions.getEnabledCommandIds().isEmpty()) //all commands restricted in restrictions
			{
				enabledCommandIds = null;
			} else if ((bitMask & ERestriction.ANY_COMMAND.getValue().longValue()) == 0) { // all commands aren't restriced in this, do copy of restrictions.enabledCommandIds
				enabledCommandIds = new HashSet<Id>(restrictions.getEnabledCommandIds());
			} else { // commands restricted in this and in restrictions, do "and" operation on this.enabledCommandIds and restrictions.enabledCommandIds  
				if (enabledCommandIds != null) {
                    enabledCommandIds.retainAll(restrictions.getEnabledCommandIds());
				}
			}
		}
		if ((restrictions.getBitMask() & ERestriction.ANY_CHILD.getValue().longValue()) != 0) {// children restricted in restrictions
			if (restrictions.getEnabledChildIds() == null || restrictions.getEnabledChildIds().isEmpty()) //all children restricted in restrictions
			{
				enabledChildIds = null;
			} else if ((bitMask & ERestriction.ANY_CHILD.getValue().longValue()) == 0) { // all children aren't restriced in this, do copy of restrictions.enabledChildIds
				enabledChildIds = new HashSet<Id>(restrictions.getEnabledChildIds());
			} else { // commands restricted in this and in restrictions, do "and" operation on this.enabledChildIds and restrictions.enabledChilndIds
				if (enabledChildIds != null) {
                    enabledChildIds.retainAll(restrictions.getEnabledChildIds());
				}
			}
		}
		if ((restrictions.getBitMask() & ERestriction.ANY_PAGES.getValue().longValue()) != 0) {// pages restricted in restrictions
			if (restrictions.getEnabledPageIds() == null || restrictions.getEnabledPageIds().isEmpty()) //all page restricted in restrictions
			{
				enabledPageIds = null;
			} else if ((bitMask & ERestriction.ANY_PAGES.getValue().longValue()) == 0) { // all pages aren't restriced in this, do copy of restrictions.enabledPageIds
				enabledPageIds = new HashSet<Id>(restrictions.getEnabledPageIds());
			} else { // pages restricted in this and in restrictions, do "and" operation on this.enabledPageIds and restrictions.enabledPageIds
				if (enabledPageIds != null) {
                    enabledPageIds.retainAll(restrictions.getEnabledPageIds());
				}
			}
		}                
		bitMask = bitMask | restrictions.getBitMask();
	}

	public void and(final Restrictions restrictions) {
		bitMask = bitMask & restrictions.getBitMask();
		if ((bitMask & ERestriction.ANY_COMMAND.getValue().longValue()) == 0) {
			enabledCommandIds = null;// isn't needed anymore
		} else if (restrictions.getEnabledCommandIds() != null) {
			for (Id cmdId : restrictions.getEnabledCommandIds()) {
				addEnabledCommand(cmdId);
			}
		}
		if ((bitMask & ERestriction.ANY_CHILD.getValue().longValue()) == 0) {
			enabledChildIds = null;// isn't needed anymore
		} else if (restrictions.getEnabledChildIds() != null) {
			for (Id cmdId : restrictions.getEnabledChildIds()) {
				addEnabledChild(cmdId);
			}
		}
                
		if ((bitMask & ERestriction.ANY_PAGES.getValue().longValue()) == 0) {
			enabledPageIds = null;// isn't needed anymore
		} else if (restrictions.getEnabledPageIds() != null) {
			for (Id cmdId : restrictions.getEnabledPageIds()) {
				addEnabledPage(cmdId);
			}
		}                
	}

    public long getBitMask() {
        return bitMask;
    }

    public Collection<Id> getEnabledCommandIds() {
        return enabledCommandIds;
    }

    public Collection<Id> getEnabledChildIds() {
        return enabledChildIds;
    }

    public Collection<Id> getEnabledPageIds() {
        return enabledPageIds;
    }

}

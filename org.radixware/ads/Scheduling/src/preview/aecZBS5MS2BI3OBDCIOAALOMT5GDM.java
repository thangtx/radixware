
/* Radix::Scheduling::IntervalSchedule - Server Executable*/

/*Radix::Scheduling::IntervalSchedule-Entity Class*/

package org.radixware.ads.Scheduling.server;

import java.util.Calendar;
import java.util.GregorianCalendar;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule")
public final published class IntervalSchedule  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return IntervalSchedule_mi.rdxMeta;}

	/*Radix::Scheduling::IntervalSchedule:Nested classes-Nested Classes*/

	/*Radix::Scheduling::IntervalSchedule:CfgObjectLookupAdvizor-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:CfgObjectLookupAdvizor")
	private static class CfgObjectLookupAdvizor  implements org.radixware.ads.CfgManagement.server.ICfgObjectLookupAdvizor,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return IntervalSchedule_mi.rdxMeta_adcXFYD4MKKUBDEXPZ2MYKLM4QU6E;}

		/*Radix::Scheduling::IntervalSchedule:CfgObjectLookupAdvizor:Nested classes-Nested Classes*/

		/*Radix::Scheduling::IntervalSchedule:CfgObjectLookupAdvizor:Properties-Properties*/





























		/*Radix::Scheduling::IntervalSchedule:CfgObjectLookupAdvizor:Methods-Methods*/

		/*Radix::Scheduling::IntervalSchedule:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid")
		public published  java.util.List<org.radixware.ads.Types.server.Entity> getCfgObjectsByExtGuid (Str extGuid, boolean considerContext, org.radixware.ads.Types.server.Entity context) {
			if (extGuid == null)
			    return java.util.Collections.emptyList();

			final java.util.ArrayList<Types::Entity> objects = new java.util.ArrayList<Types::Entity>(1);
			final Scheduling.Db::IntervalScheduleByGuidCursor cur = Scheduling.Db::IntervalScheduleByGuidCursor.open(extGuid);
			try {
			    while (cur.next()) {
			        objects.add(cur.intervalSchedule);
			    }
			} finally {
			    cur.close();
			}
			return objects;
		}


	}

	/*Radix::Scheduling::IntervalSchedule:Properties-Properties*/

	/*Radix::Scheduling::IntervalSchedule:lastUpdateUser-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateUser")
	public published  Str getLastUpdateUser() {
		return lastUpdateUser;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateUser")
	public published   void setLastUpdateUser(Str val) {
		lastUpdateUser = val;
	}

	/*Radix::Scheduling::IntervalSchedule:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::Scheduling::IntervalSchedule:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateTime")
	public published  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateTime")
	public published   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::Scheduling::IntervalSchedule:guid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:guid")
	public published  Str getGuid() {
		return guid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:guid")
	public published   void setGuid(Str val) {
		guid = val;
	}

	/*Radix::Scheduling::IntervalSchedule:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::Scheduling::IntervalSchedule:notes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:notes")
	public published  Str getNotes() {
		return notes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:notes")
	public published   void setNotes(Str val) {
		notes = val;
	}

	/*Radix::Scheduling::IntervalSchedule:caching-Dynamic Property*/



	protected org.radixware.ads.Utils.server.Caching caching=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:caching")
	private final  org.radixware.ads.Utils.server.Caching getCaching() {

		if(internal[caching] == null){
		    internal[caching] = new  Caching(this, Common::CachingPeriod:RecheckPeriod.getValue().intValue(), Common::CachingPeriod:KeepPeriod_30.getValue().intValue());
		}

		return internal[caching];
	}

	/*Radix::Scheduling::IntervalSchedule:itemIds-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrInt itemIds=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:itemIds")
	private final  org.radixware.kernel.common.types.ArrInt getItemIds() {
		return itemIds;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:itemIds")
	private final   void setItemIds(org.radixware.kernel.common.types.ArrInt val) {
		itemIds = val;
	}

	/*Radix::Scheduling::IntervalSchedule:lastUpdateTimeGetter-Dynamic Property*/



	protected org.radixware.ads.Utils.server.ILastUpdateTimeGetter lastUpdateTimeGetter=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateTimeGetter")
	private final  org.radixware.ads.Utils.server.ILastUpdateTimeGetter getLastUpdateTimeGetter() {

		if (internal[lastUpdateTimeGetter] == null) {
		    internal[lastUpdateTimeGetter] = new LastUpdateTimeGetterById(dbName[Radix::Scheduling::IntervalSchd], dbName[lastUpdateTime], dbName[id], id);
		}
		return internal[lastUpdateTimeGetter];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateTimeGetter")
	private final   void setLastUpdateTimeGetter(org.radixware.ads.Utils.server.ILastUpdateTimeGetter val) {
		lastUpdateTimeGetter = val;
	}

	/*Radix::Scheduling::IntervalSchedule:rid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:rid")
	public published  Str getRid() {
		return rid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:rid")
	public published   void setRid(Str val) {
		rid = val;
	}





















































































	/*Radix::Scheduling::IntervalSchedule:Methods-Methods*/

	/*Radix::Scheduling::IntervalSchedule:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		if(guid == null)
		  guid = Arte::Arte.generateGuid();
		else
		  if(src != null && ((IntervalSchedule)src).guid == guid)
		    guid = Arte::Arte.generateGuid();
	}

	/*Radix::Scheduling::IntervalSchedule:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		updated();        
		return true;
	}

	/*Radix::Scheduling::IntervalSchedule:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:beforeUpdate")
	protected published  boolean beforeUpdate () {
		updated();
		return true;
	}

	/*Radix::Scheduling::IntervalSchedule:getSeconds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:getSeconds")
	private final  int getSeconds (java.util.Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60
			+ calendar.get(Calendar.MINUTE) * 60
			+ calendar.get(Calendar.SECOND);

	}

	/*Radix::Scheduling::IntervalSchedule:loadByGuid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:loadByGuid")
	public static published  org.radixware.ads.Scheduling.server.IntervalSchedule loadByGuid (Str guid) {
		if(guid == null)
		  return null;

		Scheduling.Db::IntervalScheduleByGuidCursor cur = Scheduling.Db::IntervalScheduleByGuidCursor.open(guid);
		try{
		  if(cur.next())
		    return cur.intervalSchedule;
		}finally{
		  cur.close();
		}

		return null;
	}

	/*Radix::Scheduling::IntervalSchedule:prevFinishTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:prevFinishTime")
	public published  java.sql.Timestamp prevFinishTime (java.sql.Timestamp dateTime) {
		if(dateTime == null)
			return null;

		refreshCache();

		final Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(dateTime.getTime());
		final int currentSeconds = getSeconds(calendar);
		final DateTime yesterday = getAnotherDay(calendar, -1);

		DateTime maxDate = null;
		int maxSeconds = -1;

		for(Int id : itemIds)
		{
			IntervalScheduleItem item = IntervalScheduleItem.loadByPK(id, id, false);
			int end = item.endTime.intValue() * 60;
			final boolean isToday = currentSeconds > end;
			final DateTime date = getDate(item, -1, dateTime, yesterday, isToday);

			if(date == null)
				continue;

			int compareDates = maxDate != null ? maxDate.compareTo(date) : -1;

			if(compareDates < 0
					|| compareDates == 0 && maxSeconds < end)
			{
				maxSeconds = end;
				maxDate = date;
			}

		}

		return makeResult(maxDate, maxSeconds);

	}

	/*Radix::Scheduling::IntervalSchedule:nextFinishTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:nextFinishTime")
	public published  java.sql.Timestamp nextFinishTime (java.sql.Timestamp dateTime) {
		if(dateTime == null)
			return null;

		refreshCache();

		final Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(dateTime.getTime());
		final int currentSeconds = getSeconds(calendar);
		final DateTime tomorrow = getAnotherDay(calendar, 1);

		DateTime minDate = null;
		int minSeconds = -1;

		for(Int id : itemIds)
		{
			IntervalScheduleItem item = IntervalScheduleItem.loadByPK(id, id, false);
			int end = item.endTime.intValue() * 60;
			final boolean isToday = currentSeconds < end;
			final DateTime date = getDate(item, 1, dateTime, tomorrow, isToday);

			if(date == null)
				continue;

			int compareDates = minDate != null ? minDate.compareTo(date) : 1;
			if(compareDates > 0 ||
					compareDates == 0 && (minSeconds == -1 || end < minSeconds))
			{
				minSeconds = end;
				minDate = date;
			}

		}

		return makeResult(minDate, minSeconds);

	}

	/*Radix::Scheduling::IntervalSchedule:getAnotherDay-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:getAnotherDay")
	private final  java.sql.Timestamp getAnotherDay (java.util.Calendar calendar, int dir) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_YEAR, dir > 0 ? 1 : -1);

		return new DateTime(calendar.getTimeInMillis());
	}

	/*Radix::Scheduling::IntervalSchedule:isIn-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:isIn")
	public published  boolean isIn (java.sql.Timestamp dateTime) {
		if(dateTime == null)
			return false;

		refreshCache();

		final Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(dateTime.getTime());
		final int currentSeconds = getSeconds(calendar);
		for(Int id : itemIds)
		{
			IntervalScheduleItem item = IntervalScheduleItem.loadByPK(id, id, false);
			if(item.calendar != null)
			{
				Calendar.Absolute absCal = (Calendar.Absolute)item.calendar;
				if(!absCal.isIn(dateTime))
					continue;
			}
		  int start = item.startTime.intValue() * 60;
		  int end = item.endTime.intValue() * 60;
		  if(currentSeconds >= start && (end == 0 || currentSeconds <= end))
		    return true;
		}

		return false;

	}

	/*Radix::Scheduling::IntervalSchedule:nextStartTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:nextStartTime")
	public published  java.sql.Timestamp nextStartTime (java.sql.Timestamp dateTime) {
		if(dateTime == null)
			return null;

		refreshCache();

		final Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(dateTime.getTime());
		final int currentSeconds = getSeconds(calendar);
		final DateTime tomorrow = getAnotherDay(calendar, 1);

		DateTime minDate = null;
		int minSeconds = -1;

		for(Int id : itemIds)
		{
			IntervalScheduleItem item = IntervalScheduleItem.loadByPK(id, id, false);
			int start = item.startTime.intValue() * 60;
			final boolean isToday = currentSeconds < start;
			final DateTime date = getDate(item, 1, dateTime, tomorrow, isToday);
			
			if(date == null)
				continue;

			int compareDates = minDate != null ? minDate.compareTo(date) : 1;
			if(compareDates > 0 ||
					compareDates == 0 && (minSeconds == -1 || start < minSeconds))
			{
				minSeconds = start;
				minDate = date;
			}

		}

		return makeResult(minDate, minSeconds);

	}

	/*Radix::Scheduling::IntervalSchedule:updated-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:updated")
	  void updated () {
		if (rid != null) {
		    IntervalSchedule o = loadByRid(rid);
		    if (o != null && o != this)
		        throw new InvalidEasRequestClientFault("Duplicated RID");
		}
		lastUpdateTime = Arte::Arte.getCurrentTime();
		lastUpdateUser = Arte::Arte.getUserName();

	}

	/*Radix::Scheduling::IntervalSchedule:refreshCache-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:refreshCache")
	private final  boolean refreshCache () {
		final boolean needRefresh = caching.refresh(lastUpdateTimeGetter);
		if (needRefresh || itemIds == null) {
		    itemIds = new ArrInt();
		    Scheduling.Db::IntervalScheduleItemsCursor cur = Scheduling.Db::IntervalScheduleItemsCursor.open(id);
		    try {
		        while (cur.next()) {
		            itemIds.add(cur.id);
		            IntervalScheduleItem item = IntervalScheduleItem.loadByPK(id, cur.id, false);
		            /*
		             Нужно перечитать элемент, даже если он в кэше. Для этого loadByPK(..., FALSE) и read
		             */
		            item.read();
		            item.keepInCache(Common::CachingPeriod:KeepPeriod.getValue());
		        }
		    } finally {
		        cur.close();
		    }
		} else {
		    for (Int id : itemIds) {
		        IntervalScheduleItem item = IntervalScheduleItem.loadByPK(id, id, false);
		        item.keepInCache(Common::CachingPeriod:KeepPeriod_30.getValue());
		    }
		}

		return needRefresh;

	}

	/*Radix::Scheduling::IntervalSchedule:prevStartTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:prevStartTime")
	public published  java.sql.Timestamp prevStartTime (java.sql.Timestamp dateTime) {
		if(dateTime == null)
			return null;

		refreshCache();

		final Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(dateTime.getTime());
		final int currentSeconds = getSeconds(calendar);
		final DateTime yesterday = getAnotherDay(calendar, -1);

		DateTime maxDate = null;
		int maxSeconds = -1;

		for(Int id : itemIds)
		{
			IntervalScheduleItem item = IntervalScheduleItem.loadByPK(id, id, false);
			int start = item.startTime.intValue() * 60;
			final boolean isToday = currentSeconds > start;
			final DateTime date = getDate(item, -1, dateTime, yesterday, isToday);

			if(date == null)
				continue;

			int compareDates = maxDate != null ? maxDate.compareTo(date) : -1;

			if(compareDates < 0 ||
					compareDates == 0 && maxSeconds < start)
			{
				maxSeconds = start;
				maxDate = date;
			}
		}

		return makeResult(maxDate, maxSeconds);

	}

	/*Radix::Scheduling::IntervalSchedule:getDate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:getDate")
	private final  java.sql.Timestamp getDate (org.radixware.ads.Scheduling.server.IntervalScheduleItem item, int dir, java.sql.Timestamp today, java.sql.Timestamp anotherDay, boolean checkToday) {
		if(item.calendar == null)
			return checkToday ? today : anotherDay;
		else
		{
			Calendar.Absolute absCal = (Calendar.Absolute)item.calendar;
			if(checkToday && absCal.isIn(today))
				return today;
			else
			{
				if(dir > 0)
					return absCal.next(today);
				else
					return absCal.prev(today);
			}
		}

	}

	/*Radix::Scheduling::IntervalSchedule:makeResult-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:makeResult")
	private final  java.sql.Timestamp makeResult (java.sql.Timestamp date, int seconds) {
		if(date == null || seconds < 0)
			return null;
			
		int hour = seconds / (60*60);
		int min = (seconds / 60) % 60;
		int sec = seconds % 60;

		final Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		return new DateTime(calendar.getTimeInMillis());
	}

	/*Radix::Scheduling::IntervalSchedule:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:loadByPidStr")
	public static published  org.radixware.ads.Scheduling.server.IntervalSchedule loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),pidAsStr);
		try{
		return (
		org.radixware.ads.Scheduling.server.IntervalSchedule) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Scheduling::IntervalSchedule:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:loadByPK")
	public static published  org.radixware.ads.Scheduling.server.IntervalSchedule loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),pkValsMap);
		try{
		return (
		org.radixware.ads.Scheduling.server.IntervalSchedule) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Scheduling::IntervalSchedule:create-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:create")
	 static  org.radixware.ads.Scheduling.server.IntervalSchedule create (Str guid) {
		IntervalSchedule obj = new IntervalSchedule();
		obj.init();
		obj.guid = guid;
		return obj;
	}

	/*Radix::Scheduling::IntervalSchedule:deleteAllItems-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:deleteAllItems")
	private final  void deleteAllItems () {
		if (!isInDatabase(false))
		    return;

		itemIds = null; //force reread
		refreshCache();

		for (Int id : itemIds) {
		    IntervalScheduleItem item = IntervalScheduleItem.loadByPK(id, id, false);
		    if (item != null)
		        item.delete();
		}

		itemIds.clear();
	}

	/*Radix::Scheduling::IntervalSchedule:exportAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:exportAll")
	 static  org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalScheduleGroupDocument exportAll (org.radixware.ads.CfgManagement.server.CfgExportData data, java.util.Iterator<org.radixware.ads.Scheduling.server.IntervalSchedule> iter) {
		ImpExpXsd:IntervalScheduleGroupDocument groupDoc = ImpExpXsd:IntervalScheduleGroupDocument.Factory.newInstance();
		groupDoc.addNewIntervalScheduleGroup();

		if (iter == null) {
		    Scheduling.Db::IntervalSchedulesCursor c = Scheduling.Db::IntervalSchedulesCursor.open();
		    iter = new EntityCursorIterator(c, idof[Scheduling.Db::IntervalSchedulesCursor:schedule]);
		}



		try {
		    while (iter.hasNext()) {
		        IntervalSchedule intSched = iter.next();
		        CfgExportData singleData = new CfgExportData();
		        ImpExpXsd:IntervalScheduleDocument singleDoc = intSched.exportThis(singleData);
		        if (data != null) 
		            data.children.add(singleData);
		        groupDoc.IntervalScheduleGroup.IntervalList.add(singleDoc.IntervalSchedule);
		    }
		} finally {
		    EntityCursorIterator.closeIterator(iter);
		}
		    
		if (data != null) {
		    data.itemClassId = idof[CfgItem.IntervalScheduleGroup];
		    data.data = null;
		    data.fileContent = groupDoc;
		}
		return groupDoc;

	}

	/*Radix::Scheduling::IntervalSchedule:exportThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:exportThis")
	  org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalScheduleDocument exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		refreshCache();

		ImpExpXsd:IntervalScheduleDocument doc = ImpExpXsd:IntervalScheduleDocument.Factory.newInstance();
		ImpExpXsd:IntervalSchedule xml = doc.addNewIntervalSchedule();
		xml.Id = id;
		xml.Title = calcTitle();
		xml.Guid = guid;
		xml.Rid = rid;
		xml.Title = title;
		xml.Notes = notes;
		data.data = doc.copy();

		for (Int id : itemIds) {
		    IntervalScheduleItem item = IntervalScheduleItem.loadByPK(id, id, false);
		    CfgManagement::CfgExportData d = new CfgExportData();
		    xml.ItemList.add(item.exportThis(d).IntervalItem);
		    data.children.add(d);
		}

		if (data != null) {
		    data.itemClassId = idof[CfgItem.IntervalScheduleSingle];
		    data.object = this;
		    data.objectRid = guid;
		    data.fileContent = doc;
		}

		return doc;

	}

	/*Radix::Scheduling::IntervalSchedule:importAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:importAll")
	 static  void importAll (org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalScheduleGroup xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null)
		    return;

		for (ImpExpXsd:IntervalSchedule c : xml.IntervalList) {
		    importOne(c, helper);
		    if (helper.wasCancelled())
		        break;
		}

	}

	/*Radix::Scheduling::IntervalSchedule:importOne-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:importOne")
	 static  org.radixware.ads.Scheduling.server.IntervalSchedule importOne (org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalSchedule xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null)
		    return null;

		IntervalSchedule obj = IntervalSchedule.loadByGuid(xml.Guid);
		if (obj == null) {
		    obj = create(xml.Guid);
		    obj.importThis(xml, helper);
		} else
		    switch (helper.getActionIfObjExists(obj)) {
		        case UPDATE: 
		            obj.importThis(xml, helper);
		            break;
		        case NEW:
		            obj = create(Arte::Arte.generateGuid());
		            obj.importThis(xml, helper);
		            break;
		        case CANCELL: 
		            helper.reportObjectCancelled(obj);
		            break;
		    }

		return obj;

	}

	/*Radix::Scheduling::IntervalSchedule:importThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:importThis")
	private final  void importThis (org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalSchedule xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		deleteAllItems(); 

		updateFromXml(xml, helper);

		for (ImpExpXsd:IntervalItem xItem : xml.ItemList) 
		    IntervalScheduleItem.createAndImport(this, xItem, null, helper);

	}

	/*Radix::Scheduling::IntervalSchedule:updateFromCfgItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:updateFromCfgItem")
	  void updateFromCfgItem (org.radixware.ads.Scheduling.server.CfgItem.IntervalScheduleSingle cfg, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		//удалить имеющиеся элементы, они будут пересозданы
		deleteAllItems(); 

		updateFromXml(cfg.myData.IntervalSchedule, helper);

	}

	/*Radix::Scheduling::IntervalSchedule:updateFromXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:updateFromXml")
	private final  void updateFromXml (org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalSchedule xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		title = xml.Title;
		notes = xml.Notes;
		rid = helper.importRid(this, loadByRid(xml.Rid), xml.Rid);

		helper.createOrUpdateAndReport(this);

	}

	/*Radix::Scheduling::IntervalSchedule:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:onCommand_Import")
	  org.radixware.schemas.types.StrDocument onCommand_Import (org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalScheduleDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		CfgManagement::CfgImportHelper.Interactive helper = new CfgImportHelper.Interactive(false, true);
		importThis(input.IntervalSchedule, helper);
		return helper.getResultsAsHtmlStr();

	}

	/*Radix::Scheduling::IntervalSchedule:getCfgReferenceExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:getCfgReferenceExtGuid")
	public published  Str getCfgReferenceExtGuid () {
		return guid;
	}

	/*Radix::Scheduling::IntervalSchedule:getCfgReferencePropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:getCfgReferencePropId")
	public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
		return idof[IntervalSchedule:guid];
	}

	/*Radix::Scheduling::IntervalSchedule:loadByRid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:loadByRid")
	public static published  org.radixware.ads.Scheduling.server.IntervalSchedule loadByRid (Str rid) {
		if (rid == null) {
		    return null;
		}

		try (FindIntervalSchedByRidCur cur = FindIntervalSchedByRidCur.open(rid)) {
		    if (cur.next()) {
		        return cur.sched;
		    }
		}
		return null;
	}



	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdLAAGCXHVLRHUZE5A3SFO3XJOQQ){
			org.radixware.schemas.types.StrDocument result = onCommand_Import((org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalScheduleDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalScheduleDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::IntervalSchedule - Server Meta*/

/*Radix::Scheduling::IntervalSchedule-Entity Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class IntervalSchedule_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),"IntervalSchedule",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK3NFEV24OXOBDFKUAAMPGXUWTQ"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Scheduling::IntervalSchedule:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
							/*Owner Class Name*/
							"IntervalSchedule",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK3NFEV24OXOBDFKUAAMPGXUWTQ"),
							/*Property presentations*/

							/*Radix::Scheduling::IntervalSchedule:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::IntervalSchedule:lastUpdateUser:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DCWZJCBI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::IntervalSchedule:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::IntervalSchedule:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKCIYPFSBI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::IntervalSchedule:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::IntervalSchedule:notes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZBVMTD2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::IntervalSchedule:rid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWMQHAB6WVDX7FGU27A6QZ5B2E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::IntervalSchedule:CalcIntervals-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd42QC6MA5LXOBDCJGAALOMT5GDM"),"CalcIntervals",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::IntervalSchedule:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHF2DSLEDZZGFFHBMVEO2HV77MI"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::IntervalSchedule:Import-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLAAGCXHVLRHUZE5A3SFO3XJOQQ"),"Import",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Scheduling::IntervalSchedule:Id-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBOAQBFDND3PBDFHQABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),"Id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSAQBFDND3PBDFHQABIFNQAABA"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>index(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:IndexDbName TableId=\"tblZBS5MS2BI3OBDCIOAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql>) </xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::IntervalSchedule:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM"),
									10418,
									null,

									/*Radix::Scheduling::IntervalSchedule:Edit:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Scheduling::IntervalSchedule:Edit:IntervalScheduleItem-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJYB36RSJJLOBDCISAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecARTOV5KBI3OBDCIOAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr5KBTCHCJJLOBDCISAALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refSBROEBSCI3OBDCIOAALOMT5GDM"),
													null,
													null)
										}
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::IntervalSchedule:Create (Base)-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM"),
									"Create (Base)",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Scheduling::IntervalSchedule:Create (Base):Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd42QC6MA5LXOBDCJGAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHF2DSLEDZZGFFHBMVEO2HV77MI")},null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Scheduling::IntervalSchedule:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOK2W6XKHJLOBDCISAALOMT5GDM"),"General",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZBVMTD2BI3OBDCIOAALOMT5GDM"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOK2W6XKHJLOBDCISAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Scheduling::IntervalSchedule:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDNFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Scheduling::IntervalSchedule:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::IntervalSchedule:lastUpdateUser-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DCWZJCBI3OBDCIOAALOMT5GDM"),"lastUpdateUser",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXNFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::IntervalSchedule:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLTNFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::IntervalSchedule:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKCIYPFSBI3OBDCIOAALOMT5GDM"),"lastUpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPNFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::IntervalSchedule:guid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLKOV4ATWBFGENJOB6DZNQK3BRA"),"guid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::IntervalSchedule:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLNFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::IntervalSchedule:notes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZBVMTD2BI3OBDCIOAALOMT5GDM"),"notes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLHNFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::IntervalSchedule:caching-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLGS2B4IWTFD4DCB42XM643WWJ4"),"caching",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::IntervalSchedule:itemIds-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMQBC76ZZPJB3TK7LL7MANR4QNY"),"itemIds",null,org.radixware.kernel.common.enums.EValType.ARR_INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::IntervalSchedule:lastUpdateTimeGetter-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3YLUM6HQB5A3JFBGVFS7MU2W2Q"),"lastUpdateTimeGetter",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::IntervalSchedule:rid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWMQHAB6WVDX7FGU27A6QZ5B2E"),"rid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2Z2LRMHHTJFJZORUPW3AS665DI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::IntervalSchedule:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr32HJN2FXUBC65CSJ4QZY6EH7HU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLYHYDNWDKZGMHFSSVYD7UHI5W4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUHIOEJJZFRE2FBZQZ6CT5DJ5LU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2NF6463HCBDZNIVECJWOYTZS2Q"),"getSeconds",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("calendar",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6DA7F4E67RBATDH3ZHPALMSOYE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2T2UF7USQRAPJDUJFXLL72HOKY"),"loadByGuid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBHNJLEUO35A4JACSHQSIXMHI44"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UIXIRAQJPOBDCITAALOMT5GDM"),"prevFinishTime",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dateTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD3RVRW2YA5EQVFMAMUVIIXKXUE"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2V2CQBX7JLOBDCITAALOMT5GDM"),"nextFinishTime",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dateTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSFUVBX5HCFBANKNTJWQWUYV6HA"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAJF374ZL6RDZJFX6WBDOEDQRXU"),"getAnotherDay",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("calendar",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGOITN5V2RRG2FGZ4IC5VN6HFRY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dir",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4G7P534D3BDNNNCSWZ44YKPGI4"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHQX4YD2MJLOBDCISAALOMT5GDM"),"isIn",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dateTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4T5GLNSBOBESNFPLU4E55TOUTQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthK7OHMD7QJLOBDCITAALOMT5GDM"),"nextStartTime",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dateTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM3YPF7QSFBFJBLQWFT6M4I7IOI"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthND5UV6CHJLOBDCISAALOMT5GDM"),"updated",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRHJKMOWZEJGYPIYPUX4I7V55CA"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRHQWQCYRJPOBDCITAALOMT5GDM"),"prevStartTime",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dateTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr26MSP4Q5H5HRVNG4DAB5E2SWPI"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW6GJK3UZCRA2XIMQBHWAA6ZOSY"),"getDate",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("item",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMHOVQZEHIZEJPH6FTPUE4GQXXI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dir",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN4NH7DBYR5BTJFFBOAVU4MI4YI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("today",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA2HUMGH2MJH2ZM7C7Y7MEVZDTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("anotherDay",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBO6NOMAKQVECVB6LXKBS4ZQBVM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkToday",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6Y5ZSOHXF5AWZCCYZMWR4EJ7NA"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZRJFMXZSP5CUVJL7QKVQ3RDQ7M"),"makeResult",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKH44NROOD5CCXANNG54IN425VU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("seconds",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOCPBB5XIIREDDM5JHQDAG5J2WI"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDHYHOUSBI3OBDCIOAALOMT5GDM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOM5EOKNP45E3LBSXL4V5YMAHYE"),"create",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRKD4DQJZ45BCBCWQDGOHYJNCVY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNMTARXBAZFCTXLG3ULURHFYFMU"),"deleteAllItems",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI7TXKLOSNVAQBFLQILNZHLO5HE"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHIUOCZCCPVCM7PND72AEPILZQY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ3U2EBW7TRFGNFD7JKRREH2RLE"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM5CEIQQ3GVCOLOKXPYUJI22BYQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBDT6TQJ4CFG7JJJXJRDJTMSKF4"),"importAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXP2362VB6BBQLOLBU576X4IGOY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4IH7PFGFPZC63EKEA5AUGXYGYM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKUAA4QI3EZDWTEUNTXQXUUMJKQ"),"importOne",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXP2362VB6BBQLOLBU576X4IGOY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJEYP75KPQNHAFKOSQEC37HSTLA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOQC5VBLBAJAT5BRVLFDAG66SHE"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA3PFGYJVIFCCJKRJOV2LBXYUFY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCFV4CG67MRADJIHTVKD7K6HAIQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTU4AKVMF2JB3BLADDPRRLAGVTY"),"updateFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAMRIOYK4ZGAJIMTDCHQBGSUZ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM3QGJEVRRJEMBH6JE2REXJTHPI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPOIYBMIAMRHRRHNLNDSJSGO7A4"),"updateFromXml",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA3PFGYJVIFCCJKRJOV2LBXYUFY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIRPWCYHUXZF6LLP4JMFJH5WJXQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdLAAGCXHVLRHUZE5A3SFO3XJOQQ"),"onCommand_Import",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5EZRXENWSZACHOQEVR4JGJXZGI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZIBZBMXFARCIBICON5N63NMFXM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6BOM3R46UFASPMFKVINC7CTNBY"),"loadByRid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWTCQT6HZEZE4VDSMINSMWHCVAE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcXFYD4MKKUBDEXPZ2MYKLM4QU6E = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcXFYD4MKKUBDEXPZ2MYKLM4QU6E"),"CfgObjectLookupAdvizor",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,
						null,
						null,
						null,

						/*Radix::Scheduling::IntervalSchedule:CfgObjectLookupAdvizor:Properties-Properties*/
						null,

						/*Radix::Scheduling::IntervalSchedule:CfgObjectLookupAdvizor:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJPAOFWUPW5GIJKM3O7SE3KNB7Q"),"getCfgObjectsByExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRMGSXPBIKJF6DNQJ2EZFR6ANEU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2DEJV7HQ3FDQNAOXAODK2WLD2M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6UEGOUUQJFA37D5KOW7U22PHDY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::IntervalSchedule - Desktop Executable*/

/*Radix::Scheduling::IntervalSchedule-Entity Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule")
public interface IntervalSchedule {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.Scheduling.explorer.IntervalSchedule.IntervalSchedule_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Scheduling.explorer.IntervalSchedule.IntervalSchedule_DefaultModel )  super.getEntity(i);}
	}































































	/*Radix::Scheduling::IntervalSchedule:lastUpdateUser:lastUpdateUser-Presentation Property*/


	public class LastUpdateUser extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUser(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateUser:lastUpdateUser")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateUser:lastUpdateUser")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUser getLastUpdateUser();
	/*Radix::Scheduling::IntervalSchedule:id:id-Presentation Property*/


	public class Id extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Id(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Scheduling::IntervalSchedule:lastUpdateTime:lastUpdateTime-Presentation Property*/


	public class LastUpdateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastUpdateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::Scheduling::IntervalSchedule:title:title-Presentation Property*/


	public class Title extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Title(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Scheduling::IntervalSchedule:rid:rid-Presentation Property*/


	public class Rid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Rid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:rid:rid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:rid:rid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Rid getRid();
	/*Radix::Scheduling::IntervalSchedule:notes:notes-Presentation Property*/


	public class Notes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Notes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	public static class CalcIntervals extends org.radixware.kernel.common.client.models.items.Command{
		protected CalcIntervals(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalScheduleDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}



}

/* Radix::Scheduling::IntervalSchedule - Desktop Meta*/

/*Radix::Scheduling::IntervalSchedule-Entity Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class IntervalSchedule_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::IntervalSchedule:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
			"Radix::Scheduling::IntervalSchedule",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgQJDNIH3TDA3SIV74SB3DQEUM6G5LFAIA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOK2W6XKHJLOBDCISAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK3NFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDNFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK3NFEV24OXOBDFKUAAMPGXUWTQ"),0,

			/*Radix::Scheduling::IntervalSchedule:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::IntervalSchedule:lastUpdateUser:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DCWZJCBI3OBDCIOAALOMT5GDM"),
						"lastUpdateUser",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:lastUpdateUser:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLTNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKCIYPFSBI3OBDCIOAALOMT5GDM"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZBVMTD2BI3OBDCIOAALOMT5GDM"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLHNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:rid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWMQHAB6WVDX7FGU27A6QZ5B2E"),
						"rid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2Z2LRMHHTJFJZORUPW3AS665DI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:rid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::IntervalSchedule:CalcIntervals-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd42QC6MA5LXOBDCJGAALOMT5GDM"),
						"CalcIntervals",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHNFEV24OXOBDFKUAAMPGXUWTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgKNNPRFVPT5ZUBCLMIQZXIIA2KL6DGGUU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhSY27CMJTRZH4NKFRFSH3ICSLV4"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::IntervalSchedule:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHF2DSLEDZZGFFHBMVEO2HV77MI"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIYRMHSBPSFDVPCAVGFBDPFOUDU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVORNT5ZFORBEHLSYMJNUWJK6II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhVFZP72BDPND3VA5B5FGIWFC7SU"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::IntervalSchedule:Import-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLAAGCXHVLRHUZE5A3SFO3XJOQQ"),
						"Import",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDYUWBTPSDJGLFN3BAV2DZX67NE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMUCCZVMA4JC4NMTVVEGYXEXU5Q"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Scheduling::IntervalSchedule:Id-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBOAQBFDND3PBDFHQABIFNQAABA"),
						"Id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSAQBFDND3PBDFHQABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refUCVUG5C5I3OBDCIOAALOMT5GDM"),"IntervalSchd=>User (lastUpdateUser=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DCWZJCBI3OBDCIOAALOMT5GDM")},new String[]{"lastUpdateUser"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOK2W6XKHJLOBDCISAALOMT5GDM")},
			false,true,false);
}

/* Radix::Scheduling::IntervalSchedule - Web Executable*/

/*Radix::Scheduling::IntervalSchedule-Entity Class*/

package org.radixware.ads.Scheduling.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule")
public interface IntervalSchedule {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.Scheduling.web.IntervalSchedule.IntervalSchedule_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Scheduling.web.IntervalSchedule.IntervalSchedule_DefaultModel )  super.getEntity(i);}
	}































































	/*Radix::Scheduling::IntervalSchedule:lastUpdateUser:lastUpdateUser-Presentation Property*/


	public class LastUpdateUser extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUser(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateUser:lastUpdateUser")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateUser:lastUpdateUser")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUser getLastUpdateUser();
	/*Radix::Scheduling::IntervalSchedule:id:id-Presentation Property*/


	public class Id extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Id(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Scheduling::IntervalSchedule:lastUpdateTime:lastUpdateTime-Presentation Property*/


	public class LastUpdateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastUpdateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::Scheduling::IntervalSchedule:title:title-Presentation Property*/


	public class Title extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Title(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Scheduling::IntervalSchedule:rid:rid-Presentation Property*/


	public class Rid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Rid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:rid:rid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:rid:rid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Rid getRid();
	/*Radix::Scheduling::IntervalSchedule:notes:notes-Presentation Property*/


	public class Notes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Notes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	public static class CalcIntervals extends org.radixware.kernel.common.client.models.items.Command{
		protected CalcIntervals(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.Scheduling.common.ImpExpXsd.IntervalScheduleDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}



}

/* Radix::Scheduling::IntervalSchedule - Web Meta*/

/*Radix::Scheduling::IntervalSchedule-Entity Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class IntervalSchedule_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::IntervalSchedule:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
			"Radix::Scheduling::IntervalSchedule",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgQJDNIH3TDA3SIV74SB3DQEUM6G5LFAIA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOK2W6XKHJLOBDCISAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK3NFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDNFEV24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK3NFEV24OXOBDFKUAAMPGXUWTQ"),0,

			/*Radix::Scheduling::IntervalSchedule:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::IntervalSchedule:lastUpdateUser:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DCWZJCBI3OBDCIOAALOMT5GDM"),
						"lastUpdateUser",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:lastUpdateUser:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLTNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKCIYPFSBI3OBDCIOAALOMT5GDM"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZBVMTD2BI3OBDCIOAALOMT5GDM"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLHNFEV24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::IntervalSchedule:rid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWMQHAB6WVDX7FGU27A6QZ5B2E"),
						"rid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2Z2LRMHHTJFJZORUPW3AS665DI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::IntervalSchedule:rid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::IntervalSchedule:CalcIntervals-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd42QC6MA5LXOBDCJGAALOMT5GDM"),
						"CalcIntervals",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHNFEV24OXOBDFKUAAMPGXUWTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgKNNPRFVPT5ZUBCLMIQZXIIA2KL6DGGUU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhSY27CMJTRZH4NKFRFSH3ICSLV4"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Scheduling::IntervalSchedule:Id-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBOAQBFDND3PBDFHQABIFNQAABA"),
						"Id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSAQBFDND3PBDFHQABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refUCVUG5C5I3OBDCIOAALOMT5GDM"),"IntervalSchd=>User (lastUpdateUser=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DCWZJCBI3OBDCIOAALOMT5GDM")},new String[]{"lastUpdateUser"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOK2W6XKHJLOBDCISAALOMT5GDM")},
			false,true,false);
}

/* Radix::Scheduling::IntervalSchedule:Edit - Desktop Meta*/

/*Radix::Scheduling::IntervalSchedule:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),
	null,
	null,

	/*Radix::Scheduling::IntervalSchedule:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Scheduling::IntervalSchedule:Edit:Items-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7O5HJLGRJ7OBDCIYAALOMT5GDM"),"Items",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3NFEV24OXOBDFKUAAMPGXUWTQ"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJYB36RSJJLOBDCISAALOMT5GDM"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN4NEHPQPVLOBDCLSAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7O5HJLGRJ7OBDCIYAALOMT5GDM"))}
	,

	/*Radix::Scheduling::IntervalSchedule:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Scheduling::IntervalSchedule:Edit:IntervalScheduleItem-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJYB36RSJJLOBDCISAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecARTOV5KBI3OBDCIOAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr5KBTCHCJJLOBDCISAALOMT5GDM"),
					32,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	10418,0,0,null);
}
/* Radix::Scheduling::IntervalSchedule:Edit - Web Meta*/

/*Radix::Scheduling::IntervalSchedule:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),
	null,
	null,

	/*Radix::Scheduling::IntervalSchedule:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Scheduling::IntervalSchedule:Edit:Items-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7O5HJLGRJ7OBDCIYAALOMT5GDM"),"Items",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3NFEV24OXOBDFKUAAMPGXUWTQ"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJYB36RSJJLOBDCISAALOMT5GDM"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN4NEHPQPVLOBDCLSAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7O5HJLGRJ7OBDCIYAALOMT5GDM"))}
	,

	/*Radix::Scheduling::IntervalSchedule:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Scheduling::IntervalSchedule:Edit:IntervalScheduleItem-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJYB36RSJJLOBDCISAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecARTOV5KBI3OBDCIOAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr5KBTCHCJJLOBDCISAALOMT5GDM"),
					32,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	10418,0,0,null);
}
/* Radix::Scheduling::IntervalSchedule:Edit:Model - Desktop Executable*/

/*Radix::Scheduling::IntervalSchedule:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Scheduling.explorer.IntervalSchedule.IntervalSchedule_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::IntervalSchedule:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::IntervalSchedule:Edit:Model:Properties-Properties*/

	/*Radix::Scheduling::IntervalSchedule:Edit:Model:Methods-Methods*/

	/*Radix::Scheduling::IntervalSchedule:Edit:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:Edit:Model:onCommand_Import")
	protected  void onCommand_Import (org.radixware.ads.Scheduling.explorer.IntervalSchedule.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpXsd:IntervalScheduleDocument input = ImpExpXsd:IntervalScheduleDocument.Factory.parse(file);
		    Common.Dlg::ClientUtils.viewImportResult(command.send(input));
		    if (getView() != null)
		        getView().reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}
	public final class Import extends org.radixware.ads.Scheduling.explorer.IntervalSchedule.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}













}

/* Radix::Scheduling::IntervalSchedule:Edit:Model - Desktop Meta*/

/*Radix::Scheduling::IntervalSchedule:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemDBH5KY2HJLOBDCISAALOMT5GDM"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::IntervalSchedule:Edit:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Scheduling::IntervalSchedule:Edit:Model - Web Executable*/

/*Radix::Scheduling::IntervalSchedule:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.web;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Scheduling.web.IntervalSchedule.IntervalSchedule_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::IntervalSchedule:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::IntervalSchedule:Edit:Model:Properties-Properties*/

	/*Radix::Scheduling::IntervalSchedule:Edit:Model:Methods-Methods*/


}

/* Radix::Scheduling::IntervalSchedule:Edit:Model - Web Meta*/

/*Radix::Scheduling::IntervalSchedule:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemDBH5KY2HJLOBDCISAALOMT5GDM"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::IntervalSchedule:Edit:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Scheduling::IntervalSchedule:Create (Base) - Desktop Meta*/

/*Radix::Scheduling::IntervalSchedule:Create (Base)-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Create (Base)_mi{
	private static final class Create (Base)_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create (Base)_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM"),
			"Create (Base)",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),
			null,
			null,

			/*Radix::Scheduling::IntervalSchedule:Create (Base):Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Scheduling::IntervalSchedule:Create (Base):Main-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN4NEHPQPVLOBDCLSAALOMT5GDM"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO7S2C3IQVLOBDCLSAALOMT5GDM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZBVMTD2BI3OBDCIOAALOMT5GDM"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DCWZJCBI3OBDCIOAALOMT5GDM"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKCIYPFSBI3OBDCIOAALOMT5GDM"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWMQHAB6WVDX7FGU27A6QZ5B2E"),0,1,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN4NEHPQPVLOBDCLSAALOMT5GDM"))}
			,

			/*Radix::Scheduling::IntervalSchedule:Create (Base):Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.IntervalSchedule.IntervalSchedule_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create (Base)_DEF(); 
;
}
/* Radix::Scheduling::IntervalSchedule:Create (Base) - Web Meta*/

/*Radix::Scheduling::IntervalSchedule:Create (Base)-Editor Presentation*/

package org.radixware.ads.Scheduling.web;
public final class Create (Base)_mi{
	private static final class Create (Base)_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create (Base)_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM"),
			"Create (Base)",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),
			null,
			null,

			/*Radix::Scheduling::IntervalSchedule:Create (Base):Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Scheduling::IntervalSchedule:Create (Base):Main-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN4NEHPQPVLOBDCLSAALOMT5GDM"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO7S2C3IQVLOBDCLSAALOMT5GDM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZBVMTD2BI3OBDCIOAALOMT5GDM"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DCWZJCBI3OBDCIOAALOMT5GDM"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKCIYPFSBI3OBDCIOAALOMT5GDM"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWMQHAB6WVDX7FGU27A6QZ5B2E"),0,1,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN4NEHPQPVLOBDCLSAALOMT5GDM"))}
			,

			/*Radix::Scheduling::IntervalSchedule:Create (Base):Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.web.IntervalSchedule.IntervalSchedule_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create (Base)_DEF(); 
;
}
/* Radix::Scheduling::IntervalSchedule:General - Desktop Meta*/

/*Radix::Scheduling::IntervalSchedule:General-Selector Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOK2W6XKHJLOBDCISAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),
		null,
		null,
		null,
		null,
		true,
		null,
		null,
		false,
		true,
		null,
		32,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZBVMTD2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::Scheduling::IntervalSchedule:General - Web Meta*/

/*Radix::Scheduling::IntervalSchedule:General-Selector Presentation*/

package org.radixware.ads.Scheduling.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOK2W6XKHJLOBDCISAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),
		null,
		null,
		null,
		null,
		true,
		null,
		null,
		false,
		true,
		null,
		32,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCE4RLQPVLOBDCLSAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDJZRCCBI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZBVMTD2BI3OBDCIOAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::Scheduling::IntervalSchedule:General:Model - Desktop Executable*/

/*Radix::Scheduling::IntervalSchedule:General:Model-Group Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:General:Model")
public class General:Model  extends org.radixware.ads.Scheduling.explorer.IntervalSchedule.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::IntervalSchedule:General:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::IntervalSchedule:General:Model:Properties-Properties*/

	/*Radix::Scheduling::IntervalSchedule:General:Model:Methods-Methods*/

	/*Radix::Scheduling::IntervalSchedule:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:General:Model:onCommand_Import")
	private final  void onCommand_Import (org.radixware.ads.Scheduling.explorer.IntervalScheduleGroup.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpXsd:IntervalScheduleGroupDocument xDoc;
		    try {
		        xDoc = ImpExpXsd:IntervalScheduleGroupDocument.Factory.parse(file);
		    } catch (Exceptions::XmlException ex) {
		        ImpExpXsd:IntervalScheduleDocument x = ImpExpXsd:IntervalScheduleDocument.Factory.parse(file);
		        xDoc = ImpExpXsd:IntervalScheduleGroupDocument.Factory.newInstance();
		        xDoc.addNewIntervalScheduleGroup().IntervalList.add(x.IntervalSchedule);
		    }
		    Common.Dlg::ClientUtils.viewImportResult(command.send(xDoc));
		    reread();
		} catch (Exceptions::Exception e) {
		    showException(e);
		}


	}
	public final class Import extends org.radixware.ads.Scheduling.explorer.IntervalScheduleGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}













}

/* Radix::Scheduling::IntervalSchedule:General:Model - Desktop Meta*/

/*Radix::Scheduling::IntervalSchedule:General:Model-Group Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmOK2W6XKHJLOBDCISAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::IntervalSchedule:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Scheduling::IntervalSchedule:General:Model - Web Executable*/

/*Radix::Scheduling::IntervalSchedule:General:Model-Group Model Class*/

package org.radixware.ads.Scheduling.web;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::IntervalSchedule:General:Model")
public class General:Model  extends org.radixware.ads.Scheduling.web.IntervalSchedule.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::IntervalSchedule:General:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::IntervalSchedule:General:Model:Properties-Properties*/

	/*Radix::Scheduling::IntervalSchedule:General:Model:Methods-Methods*/


}

/* Radix::Scheduling::IntervalSchedule:General:Model - Web Meta*/

/*Radix::Scheduling::IntervalSchedule:General:Model-Group Model Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmOK2W6XKHJLOBDCISAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::IntervalSchedule:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Scheduling::IntervalSchedule - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class IntervalSchedule - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ссылочный ид.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reference ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2Z2LRMHHTJFJZORUPW3AS665DI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По ид.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSAQBFDND3PBDFHQABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDYUWBTPSDJGLFN3BAV2DZX67NE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIYRMHSBPSFDVPCAVGFBDPFOUDU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Расписание интервалов");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Interval Schedule");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK3NFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элементы");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Items");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3NFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Расписания интервалов");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Interval Schedules");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDNFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Примечания");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notes");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLHNFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLNFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней модификации");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPNFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLTNFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор последней модификации");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXNFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не определен>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDNFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Рассчитать интервалы на период");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Calculate Intervals for Period");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHNFEV24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO7S2C3IQVLOBDCLSAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/Scheduling"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(IntervalSchedule - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecZBS5MS2BI3OBDCIOAALOMT5GDM"),"IntervalSchedule - Localizing Bundle",$$$items$$$);
}

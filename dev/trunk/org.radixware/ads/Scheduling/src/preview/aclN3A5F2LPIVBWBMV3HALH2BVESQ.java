
/* Radix::Scheduling::Calendar.Relative - Server Executable*/

/*Radix::Scheduling::Calendar.Relative-Application Class*/

package org.radixware.ads.Scheduling.server;

import java.util.HashMap;
import java.util.Calendar;
import java.util.SortedSet;
import java.util.TreeSet;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative")
public published class Calendar.Relative  extends org.radixware.ads.Scheduling.server.Calendar  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private final class CacheItem{
	  private SortedSet<DateTime> dates;
	  private Common::Period dateRange;
	}

	static final private int START_DATES_CACHE_LIMIT = 100;
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Calendar.Relative_mi.rdxMeta;}

	/*Radix::Scheduling::Calendar.Relative:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Calendar.Relative:Properties-Properties*/

	/*Radix::Scheduling::Calendar.Relative:cache-Dynamic Property*/



	protected java.util.Map<java.sql.Timestamp,CacheItem> cache=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:cache")
	private final  java.util.Map<java.sql.Timestamp,CacheItem> getCache() {
		return cache;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:cache")
	private final   void setCache(java.util.Map<java.sql.Timestamp,CacheItem> val) {
		cache = val;
	}



































	/*Radix::Scheduling::Calendar.Relative:Methods-Methods*/

	/*Radix::Scheduling::Calendar.Relative:nextNotIn-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:nextNotIn")
	public published  java.sql.Timestamp nextNotIn (java.sql.Timestamp startDate, java.sql.Timestamp date) {
		Calendar dateCalendar = Utils::Timing.getCalendarTruncatedToDay(date);
		if(dateCalendar == null)
			return null;

		Calendar startDateCalendar = Utils::Timing.getCalendarTruncatedToDay(startDate);
		if(startDateCalendar == null)
			return null;

		final DateTime trunkedStartDate = new DateTime(startDateCalendar.getTimeInMillis());
		for(int i=0; i<1000; i++)
		{
			dateCalendar.add(Calendar.DAY_OF_YEAR, 1);
			DateTime d = new DateTime(dateCalendar.getTimeInMillis());
			if(!isInTruncatedDate(trunkedStartDate, d))
				return d;
		}

		return null;

	}

	/*Radix::Scheduling::Calendar.Relative:prev-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:prev")
	public published  java.sql.Timestamp prev (java.sql.Timestamp startDate, java.sql.Timestamp date) {
		final Calendar dateCalendar = Utils::Timing.getCalendarTruncatedToDay(date);
		if (dateCalendar == null) {
		    return null;
		}

		final Calendar startDateCalendar = Utils::Timing.getCalendarTruncatedToDay(startDate);
		if (startDateCalendar == null) {
		    return null;
		}

		final DateTime trunkedDate = new DateTime(dateCalendar.getTimeInMillis());
		final DateTime trunkedStartDate = new DateTime(startDateCalendar.getTimeInMillis());

		DateTime dateForCalc = trunkedDate;
		for (int i = 0; i < 10; i++) {
		    extendCachedPeriod(trunkedStartDate, dateForCalc);
		    if (!cache.containsKey(trunkedStartDate))
		        continue;

		    final CacheItem item = cache.get(trunkedStartDate);
		    final SortedSet<DateTime> head = item.dates.headSet(trunkedDate);
		    if (head.size() > 0) {
		        return head.last();
		    }
		    
		    //если в вычисленном периоде не оказалось следующей даты, пытаемся вычислить еще
		    dateCalendar.setTimeInMillis(item.dateRange.begin.getTime());
		    dateCalendar.add(Calendar.DAY_OF_YEAR, -1);
		    dateForCalc = new DateTime(dateCalendar.getTimeInMillis());
		}

		return null;

	}

	/*Radix::Scheduling::Calendar.Relative:firstInPeriod-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:firstInPeriod")
	public published  java.sql.Timestamp firstInPeriod (java.sql.Timestamp startDate, org.radixware.ads.Common.server.Period period) {
		if(period == null)
			return null;

		if(period.begin == null)
		  throw new AppError("Period begin not defined");

		if(isIn(startDate, period.begin))
			return period.begin;

		DateTime next = next(startDate, period.begin);
		if(next == null || (period.end != null && period.end.before(next)))
			return null;

		return next;

	}

	/*Radix::Scheduling::Calendar.Relative:isInCalculation-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:isInCalculation")
	private final  boolean isInCalculation (java.util.Calendar startDate, java.util.Calendar date) {
		if(itemIds.size() == 0)
			return false;


		boolean flag = false;

		for(Int id : itemIds){
			CalendarItem item = CalendarItem.loadByPK(id, false);
			if(item instanceof CalendarItem.Relative){
				CalendarItem.Relative relItem = (CalendarItem.Relative)item;
				flag = (flag == false && relItem.isIncluded(startDate, date))
					|| (flag == true && !relItem.isExcluded(startDate, date));
			}
		}

		return flag;

	}

	/*Radix::Scheduling::Calendar.Relative:extendTo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:extendTo")
	private final  java.sql.Timestamp extendTo (Direction dir, java.util.Calendar dateCalendar, java.util.Calendar startDateCalendar, java.util.SortedSet<java.sql.Timestamp> dates, org.radixware.ads.Common.server.Period period) {
		/*
		  Расширить переданный период в какую-либо сторону, отсчитывая от даты, переданной в
		  dateCalendar.
		  Возвращается новая граница периода.
		  
		  dates != null - расчитанные даты сюда
		  dateCalendar != null, содержит дату, от которой отсчет
		*/

		//проверка параметров
		if(dateCalendar == null || dates == null)
			throw new AppError("Incorrect parameters: start date or result dates");

		//отсчет ведется от этой даты
		final DateTime theDate = new DateTime(dateCalendar.getTimeInMillis());
		DateTime res = null;

		final int signum = dir == Direction.ToPast ? -1 : 1;

		//новая возможная граница периода
		dateCalendar.add(Calendar.DAY_OF_YEAR, PERIOD_CACHE_LIMIT*signum);
		DateTime curDay = new DateTime(dateCalendar.getTimeInMillis());

		//если возможная граница периода за границами текущего периода
		if(period == null
				|| (dir == Direction.ToPast && curDay.before(period.begin))
				|| (dir == Direction.ToFuture && curDay.after(period.end)))
		{
			res = curDay;
			
			//рассчитать интервал - от новой границы до старой границы, либо до даты, от которой ведется отсчет
			DateTime edge = period == null ? theDate : (dir == Direction.ToPast ? period.begin : period.end);
			
			//перебор дней, входящих в интервал
			while((dir == Direction.ToPast && !curDay.after(edge)
						|| (dir == Direction.ToFuture && curDay.after(edge))))
			{
				if(isInCalculation(startDateCalendar, dateCalendar))
					dates.add(curDay);
				dateCalendar.add(Calendar.DAY_OF_YEAR, -signum);
				curDay = new DateTime(dateCalendar.getTimeInMillis());
			}
		}
		else{
		  //если нет, возвращаем старую границу периода
			res = dir == Direction.ToPast ? period.begin : period.end;
		}

		return res;

	}

	/*Radix::Scheduling::Calendar.Relative:extendCachedPeriod-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:extendCachedPeriod")
	private final  void extendCachedPeriod (java.sql.Timestamp trunkedStartDate, java.sql.Timestamp trunkedDate) {
		/*
		  Расширить список дней, описанных этим календарем.
		  Рассчитанный период включает [date-PERIOD_CACHE_LIMIT ... date+PERIOD_CACHE_LIMIT]

		  trunkedDate - not null, trunked to day
		  trunkedStartDate - not null, trunked to day
		*/

		refreshCache();

		CacheItem cacheItem = cache.get(trunkedStartDate);
		Common::Period cachedPeriod = cacheItem == null ? null : cacheItem.dateRange;
		SortedSet<DateTime> cachedDates = cacheItem == null ? new TreeSet<DateTime>() : cacheItem.dates;

		if(trunkedDate == null || itemIds.size() == 0
				|| (cacheItem != null && cachedPeriod.contains(trunkedDate)))
			return;


		final Calendar dateCalendar = Utils::Timing.getCalendarTruncatedToDay(trunkedDate);
		final Calendar startDateCalendar = Utils::Timing.getCalendarTruncatedToDay(trunkedStartDate);

		//расширяем влево
		final DateTime periodBegin = extendTo(Direction.ToPast, dateCalendar, startDateCalendar, cachedDates, cachedPeriod);

		dateCalendar.setTimeInMillis(trunkedDate.getTime());

		//расширяем вправо
		final DateTime periodEnd = extendTo(Direction.ToFuture, dateCalendar, startDateCalendar, cachedDates, cachedPeriod);

		//чистим кэш
		if(cache.size() >= START_DATES_CACHE_LIMIT){
			ArrDateTime removedStartDates = new ArrDateTime();
			java.util.Iterator <DateTime> iter = cache.keySet().iterator();
			for(int i = START_DATES_CACHE_LIMIT-1; i<cache.size(); i++){
				removedStartDates.add(iter.next());
			}
			for(DateTime sd : removedStartDates)
				cache.remove(sd);
		}

		//добавляем в кэш
		cachedPeriod = new Period(periodBegin, periodEnd);
		cacheItem = new CacheItem();
		cacheItem.dates = cachedDates;
		cacheItem.dateRange = cachedPeriod;
		cache.put(trunkedStartDate, cacheItem);

		truncCachedPeriod(trunkedStartDate, trunkedDate);
	}

	/*Radix::Scheduling::Calendar.Relative:isExcluded-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:isExcluded")
	  boolean isExcluded (java.sql.Timestamp startDate, java.sql.Timestamp date) {
		Calendar dateCalendar = Utils::Timing.getCalendarTruncatedToDay(date);
		if(dateCalendar == null)
			return false;

		Calendar startDateCalendar = Utils::Timing.getCalendarTruncatedToDay(startDate);
		if(startDateCalendar == null)
			return false;

		final DateTime d = new DateTime(dateCalendar.getTimeInMillis());
		final DateTime sd = new DateTime(startDateCalendar.getTimeInMillis());

		return isInTruncatedDate(sd, d);
	}

	/*Radix::Scheduling::Calendar.Relative:getDateDetails-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:getDateDetails")
	  org.radixware.ads.Scheduling.common.CommandsXsd.GetDateDetailsRsDocument getDateDetails (java.sql.Timestamp date, java.sql.Timestamp startDate) {
		refreshCache();

		if(itemIds.size() == 0)
			return null;

		Calendar dateCalendar = Utils::Timing.getCalendarTruncatedToDay(date);
		Calendar startDateCalendar = Utils::Timing.getCalendarTruncatedToDay(startDate);
		if(dateCalendar == null || startDateCalendar == null)
			return null;

		CommandsXsd:GetDateDetailsRsDocument doc = CommandsXsd:GetDateDetailsRsDocument.Factory.newInstance();
		CommandsXsd:GetDateDetailsRsDocument.GetDateDetailsRs rs = doc.addNewGetDateDetailsRs();
		for(Int id : itemIds)
		{
			CalendarItem item = CalendarItem.loadByPK(id, false);
			if(item instanceof CalendarItem.Relative)
			{
				CalendarItem.Relative relItem = (CalendarItem.Relative)item;
				if(relItem.isIncluded(startDateCalendar, dateCalendar) || relItem.isExcluded(startDateCalendar, dateCalendar))
				{
					CommandsXsd:GetDateDetailsRsDocument.GetDateDetailsRs.Element el = rs.addNewElement();
					el.Oper = item.oper;
					el.Title = item.calcTitle();
				}
			}
		}

		return doc;

	}

	/*Radix::Scheduling::Calendar.Relative:prevNotIn-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:prevNotIn")
	public published  java.sql.Timestamp prevNotIn (java.sql.Timestamp startDate, java.sql.Timestamp date) {
		Calendar dateCalendar = Utils::Timing.getCalendarTruncatedToDay(date);
		if(dateCalendar == null)
			return null;

		Calendar startDateCalendar = Utils::Timing.getCalendarTruncatedToDay(startDate);
		if(startDateCalendar == null)
			return null;

		final DateTime trunkedStartDate = new DateTime(startDateCalendar.getTimeInMillis());
		for(int i=0; i<1000; i++)
		{
			dateCalendar.add(Calendar.DAY_OF_YEAR, -1);
			DateTime d = new DateTime(dateCalendar.getTimeInMillis());
			if(!isInTruncatedDate(trunkedStartDate, d))
				return d;
		}

		return null;

	}

	/*Radix::Scheduling::Calendar.Relative:isInTruncatedDate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:isInTruncatedDate")
	private final  boolean isInTruncatedDate (java.sql.Timestamp truncatedStartDate, java.sql.Timestamp truncatedDate) {
		//trunkedStartDate - not null, trunked to day
		//trunkedDate - not null, trunked to day

		extendCachedPeriod(truncatedStartDate, truncatedDate);
		CacheItem item = cache.get(truncatedStartDate);
		if(item == null)
		  return false;

		return item.dates.contains(truncatedDate);
	}

	/*Radix::Scheduling::Calendar.Relative:next-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:next")
	public published  java.sql.Timestamp next (java.sql.Timestamp startDate, java.sql.Timestamp date) {
		final Calendar dateCalendar = Utils::Timing.getCalendarTruncatedToDay(date);
		if(dateCalendar == null)
			return null;

		final Calendar startDateCalendar = Utils::Timing.getCalendarTruncatedToDay(startDate);
		if(startDateCalendar == null)
			return null;

		final DateTime trunkedDate = new DateTime(dateCalendar.getTimeInMillis());
		final DateTime trunkedStartDate = new DateTime(startDateCalendar.getTimeInMillis());

		DateTime dateForCalc = trunkedDate;
		for(int i=0; i<10; i++)
		{
			extendCachedPeriod(trunkedStartDate, dateForCalc);
			if(!cache.containsKey(trunkedStartDate))
				continue;

			final CacheItem item = cache.get(trunkedStartDate);
			final SortedSet<DateTime> tail = item.dates.tailSet(trunkedDate);
			for(final DateTime d : tail){
				if(d.after(trunkedDate))
					return d;
			}
			
			//если в вычисленном периоде не оказалось следующей даты, пытаемся вычислить еще
			dateCalendar.setTimeInMillis(item.dateRange.end.getTime());
			dateCalendar.add(Calendar.DAY_OF_YEAR, 1);
			dateForCalc = new DateTime(dateCalendar.getTimeInMillis());
		}

		return null;

	}

	/*Radix::Scheduling::Calendar.Relative:lastInPeriod-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:lastInPeriod")
	public published  java.sql.Timestamp lastInPeriod (java.sql.Timestamp startDate, org.radixware.ads.Common.server.Period period) {
		if(period == null)
			return null;

		if(period.end == null)
			throw new AppError("Period end not defined");

		if(isIn(startDate, period.begin))
			return period.end;

		DateTime prev = prev(startDate, period.end);
		if(prev == null || (period.begin != null && period.begin.after(prev)))
			return null;

		return prev;

	}

	/*Radix::Scheduling::Calendar.Relative:isIn-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:isIn")
	public published  boolean isIn (java.sql.Timestamp startDate, java.sql.Timestamp date) {
		Calendar dateCalendar = Utils::Timing.getCalendarTruncatedToDay(date);
		if(dateCalendar == null)
			return false;

		Calendar startDateCalendar = Utils::Timing.getCalendarTruncatedToDay(startDate);
		if(startDateCalendar == null)
			return false;

		final DateTime d = new DateTime(dateCalendar.getTimeInMillis());
		final DateTime sd = new DateTime(startDateCalendar.getTimeInMillis());

		return isInTruncatedDate(sd, d);
	}

	/*Radix::Scheduling::Calendar.Relative:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:refreshCache")
	  boolean refreshCache () {
		if(super.refreshCache()){
		    cache = new HashMap<DateTime, CacheItem>();
		    return true;
		}

		return false;

	}

	/*Radix::Scheduling::Calendar.Relative:truncCachedPeriod-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:truncCachedPeriod")
	  void truncCachedPeriod (java.sql.Timestamp truncatedStartDate, java.sql.Timestamp truncatedDate) {
		/*
		 * Расширили кэшированный период дат.
		 * Теперь нужно сократить количество записей в кэше до указанного предела.
		 */

		CacheItem cacheItem = cache.get(truncatedStartDate);
		Common::Period cachedPeriod = cacheItem.dateRange;
		SortedSet<DateTime> cachedDates = cacheItem.dates;

		while(cachedDates.size() <= MAX_CACHE_SIZE)
		    return;

		java.util.SortedSet<DateTime> head = cachedDates.headSet(truncatedDate);
		java.util.SortedSet<DateTime> tail = cachedDates.tailSet(truncatedDate);

		int h = head.size();
		int t = tail.size();

		/*
		 * Нужно, чтобы h+t <= MAX_CACHE_SIZE
		 */
		while(h+t > MAX_CACHE_SIZE) {
		    if(t >= h) {
		        tail.remove(tail.last());
		        t--;
		    }
		    if(h >= t) {
		        head.remove(head.first());
		        h--;
		    }
		}

		cachedPeriod = new Period(cachedDates.first(), cachedDates.last());
		cacheItem.dateRange = cachedPeriod;
	}

	/*Radix::Scheduling::Calendar.Relative:calcDates-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:calcDates")
	public published  org.radixware.kernel.common.types.ArrDateTime calcDates (java.sql.Timestamp from, java.sql.Timestamp to, java.sql.Timestamp startDate) {
		Calendar fromCalendar = Utils::Timing.getCalendarTruncatedToDay(from);
		Calendar toCalendar = Utils::Timing.getCalendarTruncatedToDay(to);
		Calendar startCalendar = Utils::Timing.getCalendarTruncatedToDay(startDate);

		if(fromCalendar == null || toCalendar == null || startCalendar == null)
			return null;

		DateTime trunkedFrom = new DateTime(fromCalendar.getTimeInMillis());
		DateTime trunkedTo = new DateTime(toCalendar.getTimeInMillis());
		DateTime trunkedStart = new DateTime(startCalendar.getTimeInMillis());

		{
			DateTime d = new DateTime(fromCalendar.getTimeInMillis());
			extendCachedPeriod(trunkedStart, d);
			do{
				fromCalendar.add(Calendar.DAY_OF_YEAR, PERIOD_CACHE_LIMIT);
				d = new DateTime(fromCalendar.getTimeInMillis());
				extendCachedPeriod(trunkedStart, d);
			}while(fromCalendar.before(toCalendar));
		}


		CacheItem item = cache.get(trunkedStart);
		if(item != null)
		{
			ArrDateTime res = new ArrDateTime();
			res.addAll(item.dates.tailSet(trunkedFrom).headSet(trunkedTo));
			if(item.dates.contains(trunkedTo))
				res.add(trunkedTo);
			return res;
		}


		return null;

	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::Calendar.Relative - Server Meta*/

/*Radix::Scheduling::Calendar.Relative-Application Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Calendar.Relative_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),"Calendar.Relative",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYOJWRYOUNEXHEP3V5PP6HRU3E"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Scheduling::Calendar.Relative:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),
							/*Owner Class Name*/
							"Calendar.Relative",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYOJWRYOUNEXHEP3V5PP6HRU3E"),
							/*Property presentations*/

							/*Radix::Scheduling::Calendar.Relative:Properties-Properties*/
							null,
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Calendar.Relative:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXZ76YJVY7VEZNBWINZUEN37U7E"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXMZEW6K3I3OBDCIOAALOMT5GDM"),
									15538,
									null,

									/*Radix::Scheduling::Calendar.Relative:Edit:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Scheduling::Calendar.Relative:Edit:CalendarItem-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiYWBE7D3ULTOBDCJFAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecCRD53OZ5I3OBDCIOAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr3SF2T5XN55HBRFQBS4QDIEFCOA"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refALG64NJ6I3OBDCIOAALOMT5GDM"),
													null,
													null)
										}
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXMZEW6K3I3OBDCIOAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXZ76YJVY7VEZNBWINZUEN37U7E")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Scheduling::Calendar.Relative:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("ecc7OXRUQUMQZE77FGHGE5WGENMKI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),null,100.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHC6VVBZ4I3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHC6VVBZ4I3OBDCIOAALOMT5GDM"),

						/*Radix::Scheduling::Calendar.Relative:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::Calendar.Relative:cache-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd37HINRCLDRAG7DCOKBPG2T2KBI"),"cache",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::Calendar.Relative:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCOFOB25IHRAABFZIRMNMRQ57RY"),"nextNotIn",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFY6RJJNCTVAKVF6LZQQZXFNFTI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr27KEXWKWKFHULFJQIWBF7J7RQQ"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCSBO4RZCJBFF7AMAM4MAOXLKP4"),"prev",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBRN2XTF6UJC3XC3OZXAJDPV6TM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHLC23Q7FZZAFRKCMFHWTRTAHCA"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCVYB2GRGJREHDIM623OQPHKLGI"),"firstInPeriod",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWUVUEJJPUZARVM2KAYDUMNSMNA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("period",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV7EIPB6QPRFKFJRB6FRZ6OPORE"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFGLUHBSXAZHMTGEWKZKSSRNPOA"),"isInCalculation",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTABYCLWXURFORC66Q2VWK7PM5A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGLKXGJW4JNFS7D3RH3MCTHKZWA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJG5SWYKC55FIPCNJDPIYUGPSGQ"),"extendTo",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dir",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWT5OSIO2ANH6LHVKPCT6PL6LZE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dateCalendar",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRPEN6KDX5ZHUPGAOMTULN35JBY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDateCalendar",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYSA55DJNTFAILD7TMIWZYRA2HI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dates",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprILX46TZER5E4XGNY36L65ODOHU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("period",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE3HAXDILJBCLNGFG63MCPFGAHM"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJVK7FXIZEJFKXH637FORQWHT4U"),"extendCachedPeriod",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("trunkedStartDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ2XJY5BQBBBARP5WRMSX4ZWGRA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("trunkedDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLMPOFCAPMZD5XC4ZDTM7CWMWQQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM6YM2FHZAZAKDEJCHJRHNLLSQM"),"isExcluded",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4QYS5F3DRBEZBONWI4OKLYPOGU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD2HDMKIIJFBXLLF5C5DJO2PXPA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNLBAYGLMXZB7HL7S6GK2CQ6V3I"),"getDateDetails",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA2UBT5SCPFAYLP7OGR7CTXKSGM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7AHJ7M66NNF2BAWPO6ERI3Y37A"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPG23I4MSJNGFTGF4JINPRLMTRM"),"prevNotIn",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYCVSU7QBEBEWJBO5L3LTT6ACE4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPKHHWXVYIFH4RFG4YDZKRPUDEM"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPJXQRDPCHZDSTOAZE7Y2H3NEDM"),"isInTruncatedDate",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("truncatedStartDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5Z3K7BDVEVCPJH4BHS4KF6RAKA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("truncatedDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprARK2KTK57JHSHP6KUTY6QYXMQA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRXA3CUO2BVE7XKAOA72H5SEFVY"),"next",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprABJ5AEUVVZAB5EWFAEBXUICWHY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUOMRDTLTWNGJ5ITYT26HRRALY4"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSWVBXETATVE4DL4MA7WKMKCGU4"),"lastInPeriod",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUMXQS5ZP6ZBV7IHK3A3JDQJIMA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("period",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5MNGO5OQ6NHRLK3LFEPYIIVHUM"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZMRPIBJJ4ZEWPJ7LCRB2XJV55A"),"isIn",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHVUHUDLVEFBTRCWEKLTZJIOOSI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("date",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNOB2P7JYYVG6ZLAFJNUC3U3C6A"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY4ZBEWSU7FFSPIBEWQP53YY64A"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6ZTBNTKRZZCWVJZ4D3UWRN3XKE"),"truncCachedPeriod",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("truncatedStartDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVXKXL77WAZEETPSAVODWRC75BA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("truncatedDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKA3ADLM5O5EW3MTXPQSJZ2YNE4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRMDEQ6KX7BEAXPA5PKI6CZ5ESA"),"calcDates",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("from",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNBJAICM4RFAEBHIFTDIZIINGMA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("to",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLETR3CTESFGNPJXTPCBTKJWTWI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("startDate",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE2HQNCPTZFDP7MX7UB3T4W5RUU"))
								},org.radixware.kernel.common.enums.EValType.ARR_DATE_TIME)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::Calendar.Relative - Desktop Executable*/

/*Radix::Scheduling::Calendar.Relative-Application Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative")
public interface Calendar.Relative   extends org.radixware.ads.Scheduling.explorer.Calendar  {













































}

/* Radix::Scheduling::Calendar.Relative - Desktop Meta*/

/*Radix::Scheduling::Calendar.Relative-Application Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Calendar.Relative_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Calendar.Relative:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),
			"Radix::Scheduling::Calendar.Relative",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHC6VVBZ4I3OBDCIOAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYOJWRYOUNEXHEP3V5PP6HRU3E"),null,null,0,

			/*Radix::Scheduling::Calendar.Relative:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXZ76YJVY7VEZNBWINZUEN37U7E")},
			true,true,false);
}

/* Radix::Scheduling::Calendar.Relative - Web Executable*/

/*Radix::Scheduling::Calendar.Relative-Application Class*/

package org.radixware.ads.Scheduling.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative")
public interface Calendar.Relative   extends org.radixware.ads.Scheduling.web.Calendar  {













































}

/* Radix::Scheduling::Calendar.Relative - Web Meta*/

/*Radix::Scheduling::Calendar.Relative-Application Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Calendar.Relative_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Calendar.Relative:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),
			"Radix::Scheduling::Calendar.Relative",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHC6VVBZ4I3OBDCIOAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYOJWRYOUNEXHEP3V5PP6HRU3E"),null,null,0,

			/*Radix::Scheduling::Calendar.Relative:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXZ76YJVY7VEZNBWINZUEN37U7E")},
			true,true,false);
}

/* Radix::Scheduling::Calendar.Relative:Edit - Desktop Meta*/

/*Radix::Scheduling::Calendar.Relative:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXZ76YJVY7VEZNBWINZUEN37U7E"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXMZEW6K3I3OBDCIOAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHC6VVBZ4I3OBDCIOAALOMT5GDM"),
	null,
	null,
	null,
	null,

	/*Radix::Scheduling::Calendar.Relative:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Scheduling::Calendar.Relative:Edit:CalendarItem-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiYWBE7D3ULTOBDCJFAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecCRD53OZ5I3OBDCIOAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr3SF2T5XN55HBRFQBS4QDIEFCOA"),
					32,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	15538,0,0);
}
/* Radix::Scheduling::Calendar.Relative:Edit - Web Meta*/

/*Radix::Scheduling::Calendar.Relative:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXZ76YJVY7VEZNBWINZUEN37U7E"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXMZEW6K3I3OBDCIOAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHC6VVBZ4I3OBDCIOAALOMT5GDM"),
	null,
	null,
	null,
	null,

	/*Radix::Scheduling::Calendar.Relative:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Scheduling::Calendar.Relative:Edit:CalendarItem-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiYWBE7D3ULTOBDCJFAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aclN3A5F2LPIVBWBMV3HALH2BVESQ"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecCRD53OZ5I3OBDCIOAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr3SF2T5XN55HBRFQBS4QDIEFCOA"),
					32,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	15538,0,0);
}
/* Radix::Scheduling::Calendar.Relative:Edit:Model - Desktop Executable*/

/*Radix::Scheduling::Calendar.Relative:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Scheduling.explorer.Calendar.Relative.Calendar.Relative_DefaultModel.eprXMZEW6K3I3OBDCIOAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::Calendar.Relative:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Calendar.Relative:Edit:Model:Properties-Properties*/

	/*Radix::Scheduling::Calendar.Relative:Edit:Model:Methods-Methods*/

	/*Radix::Scheduling::Calendar.Relative:Edit:Model:initWidgets-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:Edit:Model:initWidgets")
	protected  void initWidgets (com.trolltech.qt.gui.QWidget parent) {
		super.initWidgets(parent);

		thisCalendarStartDateEditorDesktop.setDisabled(false);
		thisCalendarStartDateEditorDesktop.setValue(new DateTime(System.currentTimeMillis()));

	}


}

/* Radix::Scheduling::Calendar.Relative:Edit:Model - Desktop Meta*/

/*Radix::Scheduling::Calendar.Relative:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXZ76YJVY7VEZNBWINZUEN37U7E"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXMZEW6K3I3OBDCIOAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Calendar.Relative:Edit:Model:Properties-Properties*/
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

/* Radix::Scheduling::Calendar.Relative:Edit:Model - Web Executable*/

/*Radix::Scheduling::Calendar.Relative:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Scheduling.web.Calendar.Relative.Calendar.Relative_DefaultModel.eprXMZEW6K3I3OBDCIOAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::Calendar.Relative:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Calendar.Relative:Edit:Model:Properties-Properties*/

	/*Radix::Scheduling::Calendar.Relative:Edit:Model:Methods-Methods*/

	/*Radix::Scheduling::Calendar.Relative:Edit:Model:initWidgetsWeb-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Calendar.Relative:Edit:Model:initWidgetsWeb")
	protected  void initWidgetsWeb () {
		super.initWidgetsWeb();

		((Web.Widgets::UIObject)thisCalendarStartDateEditorWeb.ValEditor).setEnabled(true);
		thisCalendarStartDateEditorWeb.Value = new DateTime(System.currentTimeMillis());
	}


}

/* Radix::Scheduling::Calendar.Relative:Edit:Model - Web Meta*/

/*Radix::Scheduling::Calendar.Relative:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXZ76YJVY7VEZNBWINZUEN37U7E"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXMZEW6K3I3OBDCIOAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Calendar.Relative:Edit:Model:Properties-Properties*/
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

/* Radix::Scheduling::Calendar.Relative - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Calendar.Relative - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period begin not defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Начало периода не задано");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4LTLMOGCWBEVHNQIWQCPYIDTPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Relative Calendar");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Относительный календарь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYOJWRYOUNEXHEP3V5PP6HRU3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Incorrect parameters: start date or result dates");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Некорректные параметры: дата отчета или рассчитанные даты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMPVGYX7C5DHBPKOCQFRPKTNQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period end not defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конец периода не задан");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJRGINNKVVASDMAW7R3FAPY3AI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Calendar.Relative - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclN3A5F2LPIVBWBMV3HALH2BVESQ"),"Calendar.Relative - Localizing Bundle",$$$items$$$);
}


/* Radix::Reports::ReportPub - Server Executable*/

/*Radix::Reports::ReportPub-Entity Class*/

package org.radixware.ads.Reports.server;

import org.radixware.kernel.common.repository.*;
import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub")
public abstract published class ReportPub  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportPub_mi.rdxMeta;}

	/*Radix::Reports::ReportPub:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportPub:Properties-Properties*/

	/*Radix::Reports::ReportPub:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::Reports::ReportPub:description-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:description")
	public published  Str getDescription() {
		return description;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:description")
	public published   void setDescription(Str val) {
		description = val;
	}

	/*Radix::Reports::ReportPub:format-Column-Based Property*/









			
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:format")
	@Deprecated
	public published  org.radixware.kernel.common.enums.EReportExportFormat getFormat() {
		return format;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:format")
	@Deprecated
	public published   void setFormat(org.radixware.kernel.common.enums.EReportExportFormat val) {
		format = val;
	}

	/*Radix::Reports::ReportPub:inheritedPubId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubId")
	public published  Int getInheritedPubId() {
		return inheritedPubId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubId")
	public published   void setInheritedPubId(Int val) {
		inheritedPubId = val;
	}

	/*Radix::Reports::ReportPub:isEnabled-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isEnabled")
	public published  Bool getIsEnabled() {
		return isEnabled;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isEnabled")
	public published   void setIsEnabled(Bool val) {
		isEnabled = val;
	}

	/*Radix::Reports::ReportPub:listId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:listId")
	public published  Int getListId() {
		return listId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:listId")
	public published   void setListId(Int val) {
		listId = val;
	}

	/*Radix::Reports::ReportPub:paramBinding-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:paramBinding")
	public published  java.sql.Clob getParamBinding() {
		return paramBinding;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:paramBinding")
	public published   void setParamBinding(java.sql.Clob val) {
		paramBinding = val;
	}

	/*Radix::Reports::ReportPub:parentTopicId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopicId")
	public published  Int getParentTopicId() {
		return parentTopicId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopicId")
	public published   void setParentTopicId(Int val) {
		parentTopicId = val;
	}

	/*Radix::Reports::ReportPub:printOnly-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:printOnly")
	public published  Bool getPrintOnly() {
		return printOnly;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:printOnly")
	public published   void setPrintOnly(Bool val) {
		printOnly = val;
	}

	/*Radix::Reports::ReportPub:reportClassGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassGuid")
	public published  Str getReportClassGuid() {
		return reportClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassGuid")
	public published   void setReportClassGuid(Str val) {
		reportClassGuid = val;
	}

	/*Radix::Reports::ReportPub:seq-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:seq")
	public published  Int getSeq() {
		return seq;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:seq")
	public published   void setSeq(Int val) {
		seq = val;
	}

	/*Radix::Reports::ReportPub:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::Reports::ReportPub:userRoleGuids-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoleGuids")
	public published  org.radixware.kernel.common.types.ArrStr getUserRoleGuids() {
		return userRoleGuids;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoleGuids")
	public published   void setUserRoleGuids(org.radixware.kernel.common.types.ArrStr val) {
		userRoleGuids = val;
	}

	/*Radix::Reports::ReportPub:reportClassTitle-Dynamic Property*/



	protected Str reportClassTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassTitle")
	public published  Str getReportClassTitle() {

		try {    
		   Str title = Meta::Utils.getDefinitionTitle(this.reportClassGuid);/*(this.);*/
		   if (title != null)
		        return title;
		    title = Meta::Utils.getDefinitionName(this.reportClassGuid);
		    if (title != null)
		        return title;
		    if (this.reportClassGuid != null)
		        return this.reportClassGuid.toString();
		    return null;
		} catch (org.radixware.kernel.common.exceptions.DefinitionNotFoundError e) {
		    if (this.reportClassGuid != null)
		        return this.reportClassGuid.toString();
		    return "<report not found>";
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassTitle")
	public published   void setReportClassTitle(Str val) {
		reportClassTitle = val;
	}

	/*Radix::Reports::ReportPub:finalTitle-Dynamic Property*/



	protected Str finalTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:finalTitle")
	public published  Str getFinalTitle() {

		if (inheritedPub!=null)
		    return inheritedPub.finalTitle;
		else if (title!=null && !title.isEmpty())
		    return title;
		else
		    return reportClassTitle;

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:finalTitle")
	public published   void setFinalTitle(Str val) {
		finalTitle = val;
	}

	/*Radix::Reports::ReportPub:userRoles-Dynamic Property*/



	protected Str userRoles=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoles")
	public published  Str getUserRoles() {

		return Meta::Utils.getRoleArrTitle(userRoleGuids);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoles")
	public published   void setUserRoles(Str val) {
		userRoles = val;
	}

	/*Radix::Reports::ReportPub:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::Reports::ReportPub:inheritedPub-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPub")
	public published  org.radixware.ads.Reports.server.ReportPub getInheritedPub() {
		return inheritedPub;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPub")
	public published   void setInheritedPub(org.radixware.ads.Reports.server.ReportPub val) {
		inheritedPub = val;
	}

	/*Radix::Reports::ReportPub:inheritedPubLocation-Dynamic Property*/



	protected Str inheritedPubLocation=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubLocation")
	public published  Str getInheritedPubLocation() {

		if (inheritedPub!=null) {
		    final ReportPubList pubList = inheritedPub.reportPubList;
		    final Types::Entity pubContext = pubList.getPubContext();
		    if (pubContext!=null)
		        return pubContext.getClassDefinitionTitle() + ": " + pubContext.calcTitle();
		}
		return null;

	}

	/*Radix::Reports::ReportPub:reportPubList-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportPubList")
	public published  org.radixware.ads.Reports.server.ReportPubList getReportPubList() {
		return reportPubList;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportPubList")
	public published   void setReportPubList(org.radixware.ads.Reports.server.ReportPubList val) {
		reportPubList = val;
	}

	/*Radix::Reports::ReportPub:contextObjectClassGuid-Dynamic Property*/



	protected Str contextObjectClassGuid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextObjectClassGuid")
	public published  Str getContextObjectClassGuid() {

		final Types::Id contextObjectClassId = getContextObjectClassId();
		return contextObjectClassId!=null ? contextObjectClassId.toString() : null;
	}

	/*Radix::Reports::ReportPub:reportFullClassName-Dynamic Property*/



	protected Str reportFullClassName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportFullClassName")
	public published  Str getReportFullClassName() {

		return reportClassGuid == null ? null : reportClassTitle + " (" + reportClassName + ")";
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportFullClassName")
	public published   void setReportFullClassName(Str val) {
		reportFullClassName = val;
	}

	/*Radix::Reports::ReportPub:isForCustomer-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isForCustomer")
	public published  Bool getIsForCustomer() {
		return isForCustomer;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isForCustomer")
	public published   void setIsForCustomer(Bool val) {
		isForCustomer = val;
	}

	/*Radix::Reports::ReportPub:parentTopic-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopic")
	public  org.radixware.ads.Reports.server.ReportPubTopic getParentTopic() {
		return parentTopic;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopic")
	public   void setParentTopic(org.radixware.ads.Reports.server.ReportPubTopic val) {
		parentTopic = val;
	}

	/*Radix::Reports::ReportPub:pubContextPid-Dynamic Property*/



	protected Str pubContextPid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextPid")
	public  Str getPubContextPid() {

		if(reportPubList!=null && reportPubList.getPubContext()!=null)
		    return reportPubList.getPubContext().getPid().toString();
		return null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextPid")
	public   void setPubContextPid(Str val) {
		pubContextPid = val;
	}

	/*Radix::Reports::ReportPub:contextTitle-Dynamic Property*/



	protected Str contextTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextTitle")
	public  Str getContextTitle() {

		if(reportPubList!=null && reportPubList.getPubContext()!=null)
		    return reportPubList.getPubContext().calcTitle();
		return null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextTitle")
	public   void setContextTitle(Str val) {
		contextTitle = val;
	}

	/*Radix::Reports::ReportPub:pubContextClassGuid-Dynamic Property*/



	protected Str pubContextClassGuid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextClassGuid")
	public  Str getPubContextClassGuid() {

		if (reportPubList != null && reportPubList.getPubContext() != null)
		    return reportPubList.getPubContext().RadMeta.Id.toString();
		else
		    return null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextClassGuid")
	public   void setPubContextClassGuid(Str val) {
		pubContextClassGuid = val;
	}

	/*Radix::Reports::ReportPub:internalSupportedFormats-Dynamic Property*/



	protected java.util.Set<org.radixware.kernel.common.enums.EReportExportFormat> internalSupportedFormats=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:internalSupportedFormats")
	public  java.util.Set<org.radixware.kernel.common.enums.EReportExportFormat> getInternalSupportedFormats() {

		try {    
		    Meta::ClassDef clazz = Arte::Arte.getDefManager().getClassDef(Types::Id.Factory.loadFrom(this.reportClassGuid));
		    if (clazz != null && clazz.getPresentation() != null) {
		        java.util.Set<ReportExportFormat> set = clazz.getPresentation().SupportedReportFormats;
		        return set == null ? Collections.<ReportExportFormat>emptySet(): set;
		    } else {
		        return Collections.emptySet();
		    }
		} catch (Exceptions::Exception e) {
		    Arte::Trace.debug(Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:ArteReports);
		    return Collections.emptySet();
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:internalSupportedFormats")
	public   void setInternalSupportedFormats(java.util.Set<org.radixware.kernel.common.enums.EReportExportFormat> val) {
		internalSupportedFormats = val;
	}

	/*Radix::Reports::ReportPub:encoding-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:encoding")
	public published  Str getEncoding() {
		return encoding;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:encoding")
	public published   void setEncoding(Str val) {
		encoding = val;
	}

	/*Radix::Reports::ReportPub:isMultyFile-Dynamic Property*/



	protected Bool isMultyFile=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isMultyFile")
	public  Bool getIsMultyFile() {

		try {

		    Meta::ClassDef clazz = Arte::Arte.getDefManager().getClassDef(Types::Id.Factory.loadFrom(this.reportClassGuid));
		    if (clazz != null && clazz.getPresentation() != null) {
		        return clazz.getPresentation().isMultiFileReport();
		    } else {
		        return false;
		    }
		} catch (Exceptions::Exception e) {
		    Arte::Trace.debug(Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:ArteReports);
		    return false;
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isMultyFile")
	public   void setIsMultyFile(Bool val) {
		isMultyFile = val;
	}

	/*Radix::Reports::ReportPub:areTextBasedFormatsSupported-Dynamic Property*/



	protected Bool areTextBasedFormatsSupported=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:areTextBasedFormatsSupported")
	public  Bool getAreTextBasedFormatsSupported() {

		return internalSupportedFormats != null && (internalSupportedFormats.contains(ReportExportFormat:CSV)||internalSupportedFormats.contains(ReportExportFormat:TXT));
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:areTextBasedFormatsSupported")
	public   void setAreTextBasedFormatsSupported(Bool val) {
		areTextBasedFormatsSupported = val;
	}

	/*Radix::Reports::ReportPub:allowedFormats-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:allowedFormats")
	public published  org.radixware.ads.Reports.common.ReportExportFormat.Arr getAllowedFormats() {
		return allowedFormats;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:allowedFormats")
	public published   void setAllowedFormats(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
		allowedFormats = val;
	}

	/*Radix::Reports::ReportPub:supportedFormats-Dynamic Property*/



	protected org.radixware.ads.Reports.common.ReportExportFormat.Arr supportedFormats=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:supportedFormats")
	public published  org.radixware.ads.Reports.common.ReportExportFormat.Arr getSupportedFormats() {

		return new Arr<ReportExportFormat>(internalSupportedFormats);
	}

	/*Radix::Reports::ReportPub:exportFormats-Dynamic Property*/



	protected org.radixware.ads.Reports.common.ReportExportFormat.Arr exportFormats=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:exportFormats")
	public published  org.radixware.ads.Reports.common.ReportExportFormat.Arr getExportFormats() {

		Arr<ReportExportFormat> result = new Arr<ReportExportFormat>();
		for(ReportExportFormat f : internalSupportedFormats){
		    if(allowedFormats == null || allowedFormats.contains(f)){
		        result.add(f);
		    }
		}
		return result;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:exportFormats")
	public published   void setExportFormats(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
		exportFormats = val;
	}

	/*Radix::Reports::ReportPub:calcPdfSecurityOptions-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:calcPdfSecurityOptions")
	public published  org.radixware.ads.Reports.server.UserFunc.CalcPdfSecurityOptions getCalcPdfSecurityOptions() {
		return calcPdfSecurityOptions;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:calcPdfSecurityOptions")
	public published   void setCalcPdfSecurityOptions(org.radixware.ads.Reports.server.UserFunc.CalcPdfSecurityOptions val) {
		calcPdfSecurityOptions = val;
	}

	/*Radix::Reports::ReportPub:maxResultSetCacheSizeKb-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:maxResultSetCacheSizeKb")
	public published  Int getMaxResultSetCacheSizeKb() {
		return maxResultSetCacheSizeKb;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:maxResultSetCacheSizeKb")
	public published   void setMaxResultSetCacheSizeKb(Int val) {
		maxResultSetCacheSizeKb = val;
	}

	/*Radix::Reports::ReportPub:systemRef-Dynamic Property*/



	protected org.radixware.ads.System.server.System systemRef=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:systemRef")
	public  org.radixware.ads.System.server.System getSystemRef() {

		return System::System.loadByPK(1, true);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:systemRef")
	public   void setSystemRef(org.radixware.ads.System.server.System val) {
		systemRef = val;
	}

	/*Radix::Reports::ReportPub:reportClassName-Dynamic Property*/



	protected Str reportClassName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassName")
	public published  Str getReportClassName() {

		Str className = "";

		boolean titleCompleted = false;
		try{
		    if (reportClassGuid != null && reportClassGuid.startsWith(org.radixware.kernel.common.enums.EDefinitionIdPrefix.USER_DEFINED_REPORT.Value)) {
		        Reports.User::UserReport report = Reports.User::UserReport.loadByPK(reportClassGuid, false);
		        if (report != null) {
		            Reports.User::UserReportModule module = report.module;
		            if (module != null) {
		                className += "User-Defined Reports" + "::" + module.title + "::" + report.name;
		                titleCompleted = true;
		            }
		        }
		    }
		    if (!titleCompleted) {
		        final List<Layer> layers = getDdsMeta().Branch.getLayers().list();
		        for (int i = layers.size() - 1; i >= 0; i--) {
		            final Layer layer = layers.get(i);
		            for (org.radixware.kernel.common.defs.ads.module.AdsModule module : ((org.radixware.kernel.common.repository.ads.AdsSegment) layer.getAds()).getModules()) {
		                final List<Types::Id> reportClassIds = module.getDefinitionIdsByIdPrefix(Meta::DefinitionIdPrefix:REPORT);
		                if (reportClassIds.contains(Types::Id.Factory.loadFrom(reportClassGuid))) {              
		                    className += layer.Name + "::" + module.Name + "::" + Meta::Utils.getDefinitionName(this.reportClassGuid);
		                }
		            }
		        }
		    }
		}catch(Exceptions::EntityObjectNotExistsError ex){
		    //.("Report class #"+  + " not found", ); 
		}catch( Exceptions::DefinitionNotFoundError ex){  
		   //.("Report class #"+  + " not found", ); 
		}

		return className;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassName")
	public published   void setReportClassName(Str val) {
		reportClassName = val;
	}

	/*Radix::Reports::ReportPub:columnsSettings-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:columnsSettings")
	public  java.sql.Clob getColumnsSettings() {
		return columnsSettings;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:columnsSettings")
	public   void setColumnsSettings(java.sql.Clob val) {
		columnsSettings = val;
	}

	/*Radix::Reports::ReportPub:inheritedPubReportClassName-Dynamic Property*/



	protected Str inheritedPubReportClassName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassName")
	public published  Str getInheritedPubReportClassName() {

		return inheritedPub == null ? null : inheritedPub.reportClassName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassName")
	public published   void setInheritedPubReportClassName(Str val) {
		inheritedPubReportClassName = val;
	}

	/*Radix::Reports::ReportPub:inheritedPubReportClassGuid-Dynamic Property*/



	protected Str inheritedPubReportClassGuid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassGuid")
	public published  Str getInheritedPubReportClassGuid() {

		if (inheritedPub != null) {
		    return inheritedPub.reportClassGuid != null ? inheritedPub.reportClassGuid : inheritedPub.inheritedPubReportClassGuid;
		}
		return null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassGuid")
	public published   void setInheritedPubReportClassGuid(Str val) {
		inheritedPubReportClassGuid = val;
	}







































































































































































































































































	/*Radix::Reports::ReportPub:Methods-Methods*/

	/*Radix::Reports::ReportPub:check-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:check")
	public abstract published  boolean check (org.radixware.ads.Types.server.Entity contextObject, boolean isPreExecCheck);

	/*Radix::Reports::ReportPub:checkSuitability-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:checkSuitability")
	protected  boolean checkSuitability (org.radixware.ads.Types.server.Entity contextObject) {
		final Types::Report report;
		String contextInfo = getContextInfo(contextObject);

		try {
		    report = ReportsServerUtils.instantiateReportByClassId(Types::Id.Factory.loadFrom(this.reportClassGuid));
		} catch (Throwable ex) {
		    Arte::Trace.warning(
		            "Report publication #" + id + contextInfo + " skipped: "
		            + "unable to instantiate report class #" + String.valueOf(this.reportClassGuid) + ": "
		            + ex.getMessage(),
		            Arte::EventSource:ArteReports);
		    return false;
		}

		if (contextObject != null) {
		    final Types::Id contextObjectClassId = report.getContextClassId();
		    if (contextObjectClassId != null) {
		        final Class<?> cl = Arte::Arte.getDefManager().getClass(contextObjectClassId);
		        if (!cl.isInstance(contextObject)) {
		            Arte::Trace.debug(
		                    "Report publication #" + id + " skipped: context object compatibility failed: "
		                    + ReportsServerUtils.getClassInfo(contextObject)
		                    + " is not instance of "
		                    + ReportsServerUtils.getClassInfo(contextObjectClassId) + ".",
		                    Arte::EventSource:ArteReports);
		            return false;
		        }
		    }
		}

		ReportsXsd:ParametersBindingType parametersBinding = getParametersBinding();
		java.util.Map<Types::Id, Object> paramId2Value = new java.util.HashMap<Types::Id, Object>();
		if (report.ContextParameterId != null) {
		    paramId2Value.put(report.ContextParameterId, contextObject);
		}

		if (parametersBinding != null) {
		    java.util.List<ReportsXsd:ParametersBindingType.ParameterBinding> paramBindings = parametersBinding.getParameterBindingList();
		    if (paramBindings != null) {
		        for (ReportsXsd:ParametersBindingType.ParameterBinding paramBinding : paramBindings) {
		            final Types::Id parameterId = paramBinding.ParameterId;
		            if (parameterId != report.ContextParameterId) {
		                try {
		                    final Object paramValue = ReportsServerUtils.loadParamFromXml(report, parameterId, paramBinding.Value);
		                    paramId2Value.put(parameterId, paramValue);
		                    //System.out.println("parameterId = "+parameterId+"; paramValue="+paramValue);
		                } catch (Exceptions::DefinitionError er) {
		                    String reportContextInfo = "";
		                    if (contextObject != null) {
		                        Meta::ClassDef cl = Arte::Arte.getInstance().DefManager.getClassDef(Types::Id.Factory.loadFrom(contextObjectClassGuid));
		                        reportContextInfo = " Report context " + cl.getTitle() + " with title \"" + contextObject.calcTitle() + "\"";
		                    }
		                    Arte::Trace.warning("Parameter #" + parameterId + " not found in report publication " + title + " (#" + id + "). To fix recreate report publication." + reportContextInfo, Arte::EventSource:ArteReports);
		                    //System.out.println("Parameter #"+ parameterId + " not found in report publication " +  +" (#"++"). To fix recreate report publication."+contextInfo);
		                } catch (Exceptions::Throwable er) {
		                    Arte::Trace.warning("Parameter #" + parameterId + " value not bounded in report publication #" + id + ": " + er.getMessage(), Arte::EventSource:ArteReports);
		                }
		            }
		        }
		    }
		}

		report.assignParamValues(paramId2Value);

		if (!report.checkSuitability()) {
		    Arte::Trace.debug("Report publication #" + id + contextInfo + " skipped: report suitability condition failed.", Arte::EventSource:ArteReports);
		    return false;
		}

		return true;
	}

	/*Radix::Reports::ReportPub:getParametersBinding-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:getParametersBinding")
	public published  org.radixware.schemas.reports.ParametersBindingType getParametersBinding () {
		if (paramBinding==null){
		    return null;
		}

		// parse clob
		ReportsXsd:ParametersBindingDocument doc = null;

		try {
		    final java.io.Reader reader = paramBinding.getCharacterStream(1, paramBinding.length());
		    try{
		        doc = ReportsXsd:ParametersBindingDocument.Factory.parse(reader);
		    }finally {
		        reader.close();
		    }
		} catch(Exceptions::SQLException ex) {
		    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:Arte);
		} catch(Exceptions::IOException ex) {
		    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:Arte);
		} catch(org.apache.xmlbeans.XmlException ex) {
		    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:Arte);
		}

		if (doc!=null)
		    return doc.ParametersBinding;
		else
		    return null;

	}

	/*Radix::Reports::ReportPub:getContextObjectClassId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:getContextObjectClassId")
	public abstract published  org.radixware.kernel.common.types.Id getContextObjectClassId ();

	/*Radix::Reports::ReportPub:checkAccess-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:checkAccess")
	@Deprecated
	private static  void checkAccess (java.util.Collection<java.lang.Object> objects) {
		for (Object obj : objects) {
		    if (obj instanceof Types::Entity) {
		        final Types::Entity entity = (Types::Entity) obj;
		        checkAccess(entity);
		    } else if (obj instanceof org.radixware.kernel.server.types.ArrEntity) {
		        final org.radixware.kernel.server.types.ArrEntity<?> arrEntity = (org.radixware.kernel.server.types.ArrEntity<?>) obj;
		        for (int i=0; i<arrEntity.size(); i++){
		            checkAccess(arrEntity.get(i));
		        }
		    }
		}

	}

	/*Radix::Reports::ReportPub:checkAccess-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:checkAccess")
	@Deprecated
	private static  void checkAccess (org.radixware.kernel.server.types.Entity entity) {
		if (entity != null && !Arte::Arte.getInstance().Rights.getCurUserCanAccess(entity)) {
		    final String pid = entity.getPid().toString();
		    final String className = entity.getRadMeta().getName();
		    throw new ServiceProcessClientFault(
		            Utils::IEasFaultReasonEnum.ACCESS_VIOLATION.toString(),
		            String.format("Attempt to access restricted object: class '%s', PID='%s'", className, pid),
		            null, null);
		}

	}

	/*Radix::Reports::ReportPub:generateReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:generateReport")
	protected  void generateReport (org.radixware.ads.Types.server.Entity requiredContextObject, org.radixware.schemas.reports.GenerateReportRqDocument input) {
		if (!isEnabled.booleanValue()) {
		    throw new IllegalUsageError("Unable to generate a report: publication prohibited.");
		}
		final Types::Id reportClassId = Types::Id.Factory.loadFrom(this.reportClassGuid);
		final Types::Report report = ReportsServerUtils.instantiateReportByClassId(reportClassId);

		final ReportsXsd:GenerateReportRqDocument.GenerateReportRq request = input.GenerateReportRq;
		final java.util.HashMap<Types::Id, Object> paramId2Value = ReportsServerUtils.loadParamsFromXml(report, request.ParamValues.ItemList);
		final Types::Id contextParameterId = report.ContextParameterId;
		final Object contextObject = (contextParameterId != null ? paramId2Value.get(report.ContextParameterId) : null);
		if (contextParameterId != null && contextObject == null) {
		    throw new IllegalUsageError("Unable to generate a report: context parameter not defined.");
		}

		if (contextParameterId != null && !(contextObject instanceof Types::Entity)) {
		    throw new IllegalUsageError("Unable to generate a report: context parameter must be a reference to the entity.");
		}

		final Types::Entity context = (Types::Entity) contextObject;

		if (requiredContextObject != null && requiredContextObject != context) {
		    throw new IllegalUsageError("Unable to generate a report: the specified context object and the expected one do not match.");
		}

		// check context
		if (!check(context, true)) {
		    throw new IllegalUsageError("Unable to generate a report: publication condition has not been met.");
		}

		// check role
		if (userRoleGuids != null) {
		    ArrStr chiefAdminRole = new ArrStr();
		    chiefAdminRole.add(idof[Arte::SuperAdmin].toString());
		    if (!Acs::AcsUtils.isCurUserHasRole(userRoleGuids) && !Acs::AcsUtils.isCurUserHasRole(chiefAdminRole)) {
		        throw new IllegalUsageError("Unable to generate a report: access denied.");
		    }
		}

		// check final params
		if (!checkFinalParams(report, paramId2Value, (contextObject instanceof Types::Entity) ? (Types::Entity) contextObject : null)) {
		    throw new IllegalUsageError("Unable to generate a report: the current parameter value and the required one do not match.");
		}

		// check parameter values in publication for suitability to report
		report.assignParamValues(paramId2Value);
		if (!report.checkSuitability()) {
		    throw new IllegalUsageError("Unable to generate a report: report condition has not been met.");
		}

		final String fileName = request.File;

		/* AK 10.12.2012 слишком строгое ограничение
		 // check access to all entities in parameters
		 (paramId2Value.values());
		 */
		// open stream to expolorer
		ReportExportParameters parameters;
		ReportExportFormat format = null;

		if (input.GenerateReportRq.Type == null) {
		    format = ReportExportFormat:PDF;
		} else {
		    Str type = input.GenerateReportRq.Type;

		    if (ReportExportFormat:XML.Value == type) {
		        format = ReportExportFormat:XML;
		    } else if (ReportExportFormat:CSV.Value == type) {
		        format = ReportExportFormat:CSV;
		    } else if (ReportExportFormat:XLS.Value == type) {
		        format = ReportExportFormat:XLS;
		    } else if (ReportExportFormat:XLSX.Value == type) {
		        format = ReportExportFormat:XLSX;
		    } else if (ReportExportFormat:TXT.Value == type) {
		        format = ReportExportFormat:TXT;
		    }
		}

		if (report.isMultyFile() || input.GenerateReportRq.ExportFormats != null) {
		    if (input.GenerateReportRq.ExportFormats == null && format == null) {
		        return;        
		    } else {
		        if (input.GenerateReportRq.ExportFormats != null) {
		            format = null;
		        }
		    }

		    InteractiveReportFileController controller = format != null
		            ? new InteractiveReportFileController(fileName, format)
		            : new InteractiveReportFileController(fileName, getFormat2ExportPathMap(input.getGenerateReportRq().FormatDirectories), input.GenerateReportRq.DefaultFileName);


		    parameters = new ReportExportParameters(controller);
		    
		    if (input.GenerateReportRq.ExportFormats != null) {         
		            List<ReportExportFormat> exportFormats = input.GenerateReportRq.ExportFormats.getExportFormatList();
		            if (exportFormats.contains(ReportExportFormat:PDF)) {
		                if (calcPdfSecurityOptions != null) {
		                    PdfSecurityOptions securityParams = calcPdfSecurityOptions.calcPdfSecurityOption(paramId2Value, context);
		                    report.setPdfSecurityOptions(securityParams);
		                }
		            }

		            parameters.setExportFormats(exportFormats);
		            parameters.setMaxResultSetCacheSizeKb(maxResultSetCacheSizeKb.intValue());
		            parameters.setEncoding(encoding);            
		    }
		} else {
		    if (format == null) {
		        return;
		    }
		    
		    Client.Resources::FileOutResource fout = ReportsServerUtils.openFileOutResource(fileName);
		    parameters = new ReportExportParameters(fout);   
		}

		parameters.setParamId2Value(paramId2Value);

		if (format != null) {
		    parameters.setExportFormat(format);

		    switch (format) {
		        case ReportExportFormat:CSV:
		        case ReportExportFormat:TXT:
		            parameters.setEncoding(encoding);
		            break;
		        case ReportExportFormat:PDF:
		            if (calcPdfSecurityOptions != null) {
		                PdfSecurityOptions securityParams = calcPdfSecurityOptions.calcPdfSecurityOption(paramId2Value, context);
		                report.setPdfSecurityOptions(securityParams);
		            }
		            break;
		        default:
		            break;
		    }    
		}

		// xColumnsDocument = .Factory.newInstance();
		// xColumnsSettings = xColumnsDocument.addNewColumnSettings();
		// xVisibleColumns = xColumnsSettings.addNewVisibleColumns();
		//xVisibleColumns.ColumnIdList.add(.("rpcHBTRSVA355CZ3FS7H72275RSNU"));  // id
		//xVisibleColumns.ColumnIdList.add(.("rpcDNY7WKPMQJCB3LDSBH2SNSZZOY"));  // title
		//xVisibleColumns.ColumnIdList.add(.("rpcAKZHRMXXQNFJREPWM3H6NGD2NQ"));  // started
		//xVisibleColumns.ColumnIdList.add(.("rpcVX3NTMLPNFGR7BL5E7O34KKMSA"));  // instance count

		//parameters.(xColumnsSettings);
		parameters.setColumnSettings(getColumnsSettings());

		try {
		    String startReportDebugMessage = "Report started. Format: " + (
		                                            parameters.getExportFormat() != null ? 
		                                            parameters.getExportFormat().toString() : 
		                                            parameters.getExportFormats().toString()
		                                        );
		                                        
		    Arte::Trace.debug(startReportDebugMessage, Arte::EventSource:ArteReports);
		    report.export(parameters);    
		} finally { 
		    Arte::Trace.debug("Report finished", Arte::EventSource:ArteReports);
		    
		    try {
		        if (parameters.getController() != null) {        
		            org.radixware.kernel.common.utils.FileUtils.deleteDirectory(parameters.getController().getDirectory());
		        } else {
		            parameters.getStream().close();
		        }
		    } catch (Exceptions::IOException ex) {
		        Arte::Trace.warning("Unable to close the file output resource", Arte::EventSource:ArteReports);
		    } catch (ReportGenerationException ex) {
		        Arte::Trace.warning("Unable to delete the temporary files of the report", Arte::EventSource:ArteReports);
		    }
		}
	}

	/*Radix::Reports::ReportPub:checkFinalParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:checkFinalParams")
	public  boolean checkFinalParams (org.radixware.ads.Types.server.Report report, java.util.HashMap<org.radixware.kernel.common.types.Id,java.lang.Object> typedParams, org.radixware.ads.Types.server.Entity contextObject) {
		ReportsXsd:ParametersBindingType parametersBinding = getParametersBinding();
		if (parametersBinding == null) {
		    return true;
		}
		final java.util.List<ReportsXsd:ParametersBindingType.ParameterBinding> paramBindings = parametersBinding.getParameterBindingList();
		if (paramBindings == null) {
		    return true;
		}

		for (ReportsXsd:ParametersBindingType.ParameterBinding paramBinding : paramBindings) {
		    if (paramBinding.Final) {
		        final Types::Id parameterId = paramBinding.ParameterId;
		        try {
		            final Object expectedParamValue = ReportsServerUtils.loadParamFromXml(report, parameterId, paramBinding.Value);
		            final Object actualParamValue = typedParams.get(parameterId);
		            if (!org.radixware.kernel.common.utils.Utils.equals(expectedParamValue, actualParamValue)) {
		                return false;
		            }
		        } catch (Exceptions::DefinitionError er) {
		            String contextInfo = "Report:\n";
		            contextInfo += "        Class id  " + report.getId();
		            contextInfo += "      Class name  " + report.getRadMeta().getName();
		            contextInfo += "     Class title  " + report.getRadMeta().getTitle();
		            contextInfo += "  Pub class name  " + this.getRadMeta().getName();
		            contextInfo += " Pub class title  " + this.getRadMeta().getTitle();
		            contextInfo += "       Pub title  " + this.calcTitle();
		            contextInfo += "          Pub id  " + this.id;

		            contextInfo += "Report context:\n";
		            if (contextObject == null) {
		                contextInfo += "     No context";
		            } else {

		                Meta::ClassDef cl = contextObject.getRadMeta();
		                contextInfo += "     Class name   " + cl.getName();
		                contextInfo += "    Class title   " + cl.getTitle();
		                contextInfo += "   Object title   " + contextObject.calcTitle();
		            }

		            Arte::Trace.warning("Parameter #" + parameterId + " not found in report publication\n" + contextInfo, Arte::EventSource:ArteReports);            
		        }
		    }
		}

		return true;

	}

	/*Radix::Reports::ReportPub:exportReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:exportReport")
	@Deprecated
	protected  void exportReport (org.radixware.ads.Types.server.Entity requiredContextObject, org.radixware.schemas.reports.GenerateReportRqDocument input) {
		//old code of this function differ from generateReport code only by calling checkAccess on parameters
		generateReport(requiredContextObject,input);
	}

	/*Radix::Reports::ReportPub:onCommand_BeforeInputParams-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:onCommand_BeforeInputParams")
	public  org.radixware.schemas.reports.GenerateReportRqDocument onCommand_BeforeInputParams (org.radixware.schemas.reports.GenerateReportRqDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		final Types::Id reportClassId = Types::Id.Factory.loadFrom(this.reportClassGuid);
		final Types::Report report = ReportsServerUtils.instantiateReportByClassId(reportClassId);

		final ReportsXsd:GenerateReportRqDocument.GenerateReportRq request = input.GenerateReportRq;
		final java.util.HashMap<Types::Id, Object> paramId2Value = ReportsServerUtils.loadParamsFromXml(report, request.ParamValues.ItemList);

		report.assignParamValues(paramId2Value);

		report.beforeInputParams();

		ReportsXsd:GenerateReportRqDocument rsDoc = (ReportsXsd:GenerateReportRqDocument) input.copy();

		for (Arte::EasXsd:Property p : rsDoc.GenerateReportRq.ParamValues.ItemList) {
		    Meta::PropDef def = report.getRadMeta().getPropById(p.Id);
		    Object val = report.getProp(p.Id);
		    org.radixware.kernel.server.arte.services.eas.EasValueConverter.objVal2EasPropXmlVal(val, null, def.getValType(), p);
		}

		return rsDoc;
	}

	/*Radix::Reports::ReportPub:onCommand_ExportReport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:onCommand_ExportReport")
	public  void onCommand_ExportReport (org.radixware.schemas.reports.GenerateReportRqDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		exportReport(null /*context*/, input);
	}

	/*Radix::Reports::ReportPub:getContextInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:getContextInfo")
	private final  Str getContextInfo (org.radixware.ads.Types.server.Entity contextObject) {
		String reportPubListContextInfo = "";
		if (reportPubList.pubContextClassGuid != null) {
		    if ((reportPubList.pubContextClassGuid.startsWith(Meta::DefinitionIdPrefix:ADS_APPLICATION_CLASS.getValue())
		            || reportPubList.pubContextClassGuid.startsWith(Meta::DefinitionIdPrefix:ADS_ENTITY_CLASS.getValue()))
		            && reportPubList.contextPid != null) {
		        Types::Id pubListContextId = Types::Id.Factory.loadFrom(reportPubList.pubContextClassGuid);
		        
		        Meta::ClassDef pubListContextClass = Arte::Arte.getInstance().getDefManager().getClassDef(pubListContextId);        
		        Types::Entity pubListContextEntity = Types::Entity.load(pubListContextClass.getEntityId(), reportPubList.contextPid);
		        if (pubListContextEntity != null) {
		            reportPubListContextInfo = " (publication context \"" + pubListContextClass.getTitle() + " / " + pubListContextEntity.calcTitle() +"\"";
		        }
		    } else {       
		       reportPubListContextInfo = " (publication context #" + reportPubList.pubContextClassGuid;
		    }    
		}

		String contextInfo = reportPubListContextInfo.isEmpty() ? "" : ")";
		if (contextObject != null) {
		    Meta::ClassDef cl = Arte::Arte.getInstance().DefManager.getClassDef(Types::Id.Factory.loadFrom(contextObjectClassGuid));
		    String infoStart = reportPubListContextInfo.isEmpty() ? " (" : ", ";
		    contextInfo =  infoStart + "report context \"" + cl.getTitle() + "\" with title \"" + contextObject.calcTitle() + "\")";
		}

		return reportPubListContextInfo + contextInfo;
	}

	/*Radix::Reports::ReportPub:getFormat2ExportPathMap-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:getFormat2ExportPathMap")
	public  java.util.Map<org.radixware.kernel.common.enums.EReportExportFormat,Str> getFormat2ExportPathMap (org.radixware.schemas.reports.GenerateReportRqDocument.GenerateReportRq.FormatDirectories formatDirectories) {
		if (formatDirectories == null) {
		    return null;
		}

		java.util.Map<ReportExportFormat, Str> result = new java.util.HashMap<ReportExportFormat, Str>();
		for (ReportsXsd:GenerateReportRqDocument.GenerateReportRq.FormatDirectories.FormatDirectory formatDirectory : formatDirectories.getFormatDirectoryList()) {
		    result.put(formatDirectory.Format, formatDirectory.Path);
		}

		return result;
	}

	/*Radix::Reports::ReportPub:getColumnsSettings-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:getColumnsSettings")
	private final  org.radixware.schemas.reports.ColumnSettings getColumnsSettings () {
		if (columnsSettings == null){
		    return null;
		}

		// parse clob
		ReportsXsd:ColumnSettingsDocument doc = null;

		try {
		    final java.io.Reader reader = columnsSettings.getCharacterStream(1, columnsSettings.length());
		    try{
		        doc = ReportsXsd:ColumnSettingsDocument.Factory.parse(reader);
		    }finally {
		        reader.close();
		    }
		} catch(Exceptions::SQLException ex) {
		    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:Arte);
		} catch(Exceptions::IOException ex) {
		    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:Arte);
		} catch(org.apache.xmlbeans.XmlException ex) {
		    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:Arte);
		}

		return doc == null ? null : doc.ColumnSettings;

	}




	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmd2YYVB5QGYNCQ3CWFFTY2EYZZMU){
			org.radixware.schemas.reports.GenerateReportRqDocument result = onCommand_BeforeInputParams((org.radixware.schemas.reports.GenerateReportRqDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.reports.GenerateReportRqDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmd7XQZT2UPTJFX3KEDPFDV5WQM2A){
			onCommand_ExportReport((org.radixware.schemas.reports.GenerateReportRqDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.reports.GenerateReportRqDocument.class),newPropValsById);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Reports::ReportPub - Server Meta*/

/*Radix::Reports::ReportPub-Entity Class*/

package org.radixware.ads.Reports.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPub_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),"ReportPub",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG53YVNHFNHZZPJTDWK3EJWEMQ"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Reports::ReportPub:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
							/*Owner Class Name*/
							"ReportPub",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG53YVNHFNHZZPJTDWK3EJWEMQ"),
							/*Property presentations*/

							/*Radix::Reports::ReportPub:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Reports::ReportPub:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:description:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCCJD67UNVD4LM7B3IKHJPER5Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:format:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7FRUCC2LRVCFDN26X3OA73GNIY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:inheritedPubId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BK2KKPDYJCOFIAHL23AZ4XOCE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:isEnabled:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLOAH3AWYW5DLBHJSUCBMFDJEHY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:listId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXFVM3NF5JBVPM44EQV6TLOEB4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:paramBinding:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWN7EWE5K45C4HNELVSOXTYA2RM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:parentTopicId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNK3L4L6SJFGZBFKG64XTIGTBM"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:printOnly:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX5QBHA4M3NABRLT52FMFOWQVYQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:reportClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQV2RHG7LRHZVJ2KHGLJI2HVEQ"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:seq:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU7SJ5KBINF27AV7GLWZLCIO4E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:userRoleGuids:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFIB7BM27NH67GFPFCDMY7Y7OQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:reportClassTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXDBOLEH76RBMLLMYVGBLB3AKS4"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:finalTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV4UEKFQHKNGRVCLOX4V33D4NQY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:userRoles:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5NFR5GNJNA7BMUCQG7VVJHGGQ"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:classGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2GB4IHLRWVAHTCYF3F2KNK2WBU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:inheritedPub:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPFZICKAKZCNXCW7XNDNUCERJM"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Reports::ReportPub:inheritedPubLocation:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3CGHHRLW55CQJNQJXE6DBUPWQY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:reportPubList:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXFJFNCRMKBDMZID5HZLAXCIOZY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Reports::ReportPub:contextObjectClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF2L5IFQC5NB6ZJDNZYEPVN4A3A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:reportFullClassName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:isForCustomer:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVP3HR4T4VRDJXE2SQKI3CUOSJI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:parentTopic:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMZ7FVBTVMBBURC66IPV3VL53WI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Reports::ReportPub:pubContextPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6ZTLI62DFAU3IVMRXRS2RP4O4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:contextTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKVBCP4A4NBENTENHZNSR2CHMTQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:pubContextClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLBBRZHEYFFWRBZCNQXVDGZIVE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:encoding:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2IHMCWS4PBBZLGPMBLXPQVCACM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:isMultyFile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU62DMLZ7VBHRZI4XKH3QBOGE44"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:areTextBasedFormatsSupported:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPCQMGHIZIRCRJHRVPCEXLMA3KQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:allowedFormats:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFPX3ORINZ5ADVJBS6BMJPBRPLA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:supportedFormats:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX2LTASUESJFR5BOELWAWGLC6RE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:exportFormats:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU46DA6EHUVCP3NLRNW3JMBDUKY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:calcPdfSecurityOptions:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXBSUGY7HRNDEJJYTZBOJ7BGNR4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:maxResultSetCacheSizeKb:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPS2XXJEY6ZHUXMSQRBWX5CKMNU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:reportClassName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3NQ35UT3PZFMTC56PJMYAHOZN4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:columnsSettings:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KG2NQHYBFGGJLWG2GKI4A6APE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:inheritedPubReportClassName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPMYIAKHDBBPHHJB5COTEL5NHA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports::ReportPub:inheritedPubReportClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYCGMKKZQYNAUNPB2IBHKW2FHM4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports::ReportPub:ExportReport-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd7XQZT2UPTJFX3KEDPFDV5WQM2A"),"ExportReport",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports::ReportPub:ChooseReport-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2EVERXXOYND7JCLVEEVPOJGOIY"),"ChooseReport",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports::ReportPub:BeforeInputParams-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2YYVB5QGYNCQ3CWFFTY2EYZZMU"),"BeforeInputParams",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Reports::ReportPub:bySeq-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQ64NKOZ4VVFUTGRIGEUCIFMBLE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),"bySeq",null,new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Reports::ReportPub:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Reports::ReportPub:General:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Reports::ReportPub:Common-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E"),"Common",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV4UEKFQHKNGRVCLOX4V33D4NQY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BK2KKPDYJCOFIAHL23AZ4XOCE"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNK3L4L6SJFGZBFKG64XTIGTBM"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXFVM3NF5JBVPM44EQV6TLOEB4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3NQ35UT3PZFMTC56PJMYAHOZN4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQV2RHG7LRHZVJ2KHGLJI2HVEQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPMYIAKHDBBPHHJB5COTEL5NHA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYCGMKKZQYNAUNPB2IBHKW2FHM4"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQ64NKOZ4VVFUTGRIGEUCIFMBLE"),true,null,false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262160,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Reports::ReportPub:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV4UEKFQHKNGRVCLOX4V33D4NQY"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Reports::ReportPub:Empty-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccGSR7XTLSRNGDLOCDMYLQTHYRIQ"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
									}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Reports::ReportPub:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Reports::ReportPub:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJYMCSU7D2ZHIVC2U3Y2XCI7NVI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:description-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCCJD67UNVD4LM7B3IKHJPER5Y"),"description",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7DUY6Z2LYBCEPMLL6AFXLPVLU4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:format-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7FRUCC2LRVCFDN26X3OA73GNIY"),"format",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6IW7MDKVIVHFHLIHFPSEIJ46QI"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("application/pdf")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:inheritedPubId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BK2KKPDYJCOFIAHL23AZ4XOCE"),"inheritedPubId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:isEnabled-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLOAH3AWYW5DLBHJSUCBMFDJEHY"),"isEnabled",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS7UECLSNVDSDHT3O77MHCV4RY"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:listId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXFVM3NF5JBVPM44EQV6TLOEB4"),"listId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:paramBinding-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWN7EWE5K45C4HNELVSOXTYA2RM"),"paramBinding",null,org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:parentTopicId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNK3L4L6SJFGZBFKG64XTIGTBM"),"parentTopicId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:printOnly-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX5QBHA4M3NABRLT52FMFOWQVYQ"),"printOnly",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZLDF446Y6NGAXNG5BM4R3GU52Q"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:reportClassGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQV2RHG7LRHZVJ2KHGLJI2HVEQ"),"reportClassGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3TKQH37GMNDAPLUQDIUXNEVVAY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:seq-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),"seq",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ74K6773TBDGFGGUTJIGOR6GOA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU7SJ5KBINF27AV7GLWZLCIO4E"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSRLBLESA5BH3KQ55ATNMQOOLA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:userRoleGuids-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFIB7BM27NH67GFPFCDMY7Y7OQ"),"userRoleGuids",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:reportClassTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXDBOLEH76RBMLLMYVGBLB3AKS4"),"reportClassTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJNY6FL7LFEXLFZMAXXVZH4MRQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:finalTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV4UEKFQHKNGRVCLOX4V33D4NQY"),"finalTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5XHSYWNCTZAFNK57WOHRSYRVVY"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:userRoles-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5NFR5GNJNA7BMUCQG7VVJHGGQ"),"userRoles",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETZLOQWHDNARHJ5XEL6FHFON7Q"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2GB4IHLRWVAHTCYF3F2KNK2WBU"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:inheritedPub-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPFZICKAKZCNXCW7XNDNUCERJM"),"inheritedPub",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LNAYJZ5XRGA5OYEMUVJFO6KRQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refBOVOJ7JCA5DABMRYFMKTL2IMRQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:inheritedPubLocation-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3CGHHRLW55CQJNQJXE6DBUPWQY"),"inheritedPubLocation",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWP6FTGCPWVAIPMIYXUHAF3ZAUY"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:reportPubList-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXFJFNCRMKBDMZID5HZLAXCIOZY"),"reportPubList",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5FBPZNPVQRBRNMIZLHIHVIGUN4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refJLB3T4LDYNGENHZH4FUXEMEU7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:contextObjectClassGuid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF2L5IFQC5NB6ZJDNZYEPVN4A3A"),"contextObjectClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:reportFullClassName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E"),"reportFullClassName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2HVIHI3M4FBAVGXFSEU5S46LFI"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:isForCustomer-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVP3HR4T4VRDJXE2SQKI3CUOSJI"),"isForCustomer",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBAMHIDMXBEXTJ2S5B5XZKIQMU"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:parentTopic-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMZ7FVBTVMBBURC66IPV3VL53WI"),"parentTopic",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGGLFUEWYE5BL7MFRKF3V7O5PYM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref33FTUNJWBNG7XILKNQF3VKWVRA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec34F7JPGYHJEJHBCE7ISQKJN3IY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:pubContextPid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6ZTLI62DFAU3IVMRXRS2RP4O4"),"pubContextPid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:contextTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKVBCP4A4NBENTENHZNSR2CHMTQ"),"contextTitle",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:pubContextClassGuid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLBBRZHEYFFWRBZCNQXVDGZIVE"),"pubContextClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:internalSupportedFormats-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4QTADNCJHVFGRPB3BTC6VROHXI"),"internalSupportedFormats",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:encoding-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2IHMCWS4PBBZLGPMBLXPQVCACM"),"encoding",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls23SA6GQDEREUZKJ7Q3N3Z7XXMY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:isMultyFile-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU62DMLZ7VBHRZI4XKH3QBOGE44"),"isMultyFile",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:areTextBasedFormatsSupported-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPCQMGHIZIRCRJHRVPCEXLMA3KQ"),"areTextBasedFormatsSupported",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:allowedFormats-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFPX3ORINZ5ADVJBS6BMJPBRPLA"),"allowedFormats",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCN764DRY3FA2BHEH66DMQIBWQQ"),org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:supportedFormats-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX2LTASUESJFR5BOELWAWGLC6RE"),"supportedFormats",null,org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:exportFormats-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU46DA6EHUVCP3NLRNW3JMBDUKY"),"exportFormats",null,org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:calcPdfSecurityOptions-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXBSUGY7HRNDEJJYTZBOJ7BGNR4"),"calcPdfSecurityOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQEZ5JHCRGBENXMP3GT6NFZJQUM"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5CE6DSMOGVFXZHH2RMLQBDM4II"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:maxResultSetCacheSizeKb-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPS2XXJEY6ZHUXMSQRBWX5CKMNU"),"maxResultSetCacheSizeKb",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGGWFKKFPFBFXJRGZI76YYZUW4"),org.radixware.kernel.common.enums.EValType.INT,null,true,null,new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUOVUDYRSKFBNLO3262U25PADLI")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5TXYWQQLNBKPDAZKGEQRQTLK4"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_ALWAYS,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:systemRef-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUOVUDYRSKFBNLO3262U25PADLI"),"systemRef",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:reportClassName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3NQ35UT3PZFMTC56PJMYAHOZN4"),"reportClassName",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:columnsSettings-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KG2NQHYBFGGJLWG2GKI4A6APE"),"columnsSettings",null,org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:inheritedPubReportClassName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPMYIAKHDBBPHHJB5COTEL5NHA"),"inheritedPubReportClassName",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports::ReportPub:inheritedPubReportClassGuid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYCGMKKZQYNAUNPB2IBHKW2FHM4"),"inheritedPubReportClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Reports::ReportPub:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHR2SSO3ZF5D5FC5YCVP3AP5YPA"),"check",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextObject",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVPDI3VZM35CTHGQIPFVC3T45AM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isPreExecCheck",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB36A7NMB4VFVVAMVKK6RW7ZVSE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQOBYNROIBNBXTD2TQXKIXVUVA4"),"checkSuitability",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextObject",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJPQRLMCMLFD47A6MICECC5LQUA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO5T7GUGBFFFQDLQWS4SO4IXHHY"),"getParametersBinding",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthU2QN6PMEORASHGRBEXZDXVR2JI"),"getContextObjectClassId",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWKNP6TAXCVGEVNOKPFN5QJBB4I"),"checkAccess",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objects",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRQJNZ4URDZBAXLE7KXP5JQLCMQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXLPVBRKGZBS7DBSZ4KVTPVS7U"),"checkAccess",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOQGWAYIUPREILOIB3ZX7ET7EUE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAK3L7LWYAVC5DIXCO5HLBYKAQE"),"generateReport",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("requiredContextObject",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR7XCP73KSVGW3NLMQBVZ3FDSMQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRLJECX2YIFBDZM5VIJRY76JK5A"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2ZA6WJHBAZGS3LN6EUFCOD32WA"),"checkFinalParams",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDX22T5WPDFD7HCFI65X4PFIHJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("typedParams",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTVUBKKF4MVHBXPT3H7GIDNCR4Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextObject",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHX6STCO4ZND75AIFIXL33SMA6Q"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB6X7IL4ICNBDNMRX7AP26M5SSI"),"exportReport",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("requiredContextObject",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLX54JJDFZRHZJJNAJ7FLN76MJY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLNOQ24OCAZHT3D5UTX6XTIAO5A"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd2YYVB5QGYNCQ3CWFFTY2EYZZMU"),"onCommand_BeforeInputParams",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYP3QE4HX4JEWJN2TXCOZEXXJXM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRSYAVYFKGVGOPIFAAPFJPVCXI4"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd7XQZT2UPTJFX3KEDPFDV5WQM2A"),"onCommand_ExportReport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7FRG2C2X2BFXXK6SYYKG5NZTNE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQPDHFEPKS5DDJOK4MF67D5HLZI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthP3WVA6RLWBAWPEJZX2RBPUD3NE"),"getContextInfo",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextObject",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWUQCCCUJGNEMTOR6JJXGFXGUVM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEBGC2USDC5FR5PT32ZS5ZNL5VE"),"getFormat2ExportPathMap",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("formatDirectories",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGKGIQBHP4FE4FLQNNTM42PQV4Q"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWEW5AO4EEZFKLAANTUEI2SXHBQ"),"getColumnsSettings",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::Reports::ReportPub - Desktop Executable*/

/*Radix::Reports::ReportPub-Entity Class*/

package org.radixware.ads.Reports.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub")
public interface ReportPub {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Reports.explorer.ReportPub.ReportPub_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Reports.explorer.ReportPub.ReportPub_DefaultModel )  super.getEntity(i);}
	}




































































































































































































































	/*Radix::Reports::ReportPub:inheritedPubId:inheritedPubId-Presentation Property*/


	public class InheritedPubId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InheritedPubId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubId:inheritedPubId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubId:inheritedPubId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InheritedPubId getInheritedPubId();
	/*Radix::Reports::ReportPub:classGuid:classGuid-Presentation Property*/


	public class ClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::Reports::ReportPub:columnsSettings:columnsSettings-Presentation Property*/


	public class ColumnsSettings extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public ColumnsSettings(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:columnsSettings:columnsSettings")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:columnsSettings:columnsSettings")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ColumnsSettings getColumnsSettings();
	/*Radix::Reports::ReportPub:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Reports::ReportPub:format:format-Presentation Property*/


	public class Format extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Format(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Deprecated
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EReportExportFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EReportExportFormat ? (org.radixware.kernel.common.enums.EReportExportFormat)x : org.radixware.kernel.common.enums.EReportExportFormat.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Deprecated
		@Override
		public Class<org.radixware.kernel.common.enums.EReportExportFormat> getValClass(){
			return org.radixware.kernel.common.enums.EReportExportFormat.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EReportExportFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EReportExportFormat ? (org.radixware.kernel.common.enums.EReportExportFormat)x : org.radixware.kernel.common.enums.EReportExportFormat.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}






				














		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:format:format")
		public  org.radixware.kernel.common.enums.EReportExportFormat getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:format:format")
		public   void setValue(org.radixware.kernel.common.enums.EReportExportFormat val) {
			Value = val;
		}
	}
	public Format getFormat();
	/*Radix::Reports::ReportPub:reportClassGuid:reportClassGuid-Presentation Property*/


	public class ReportClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassGuid:reportClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassGuid:reportClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportClassGuid getReportClassGuid();
	/*Radix::Reports::ReportPub:userRoleGuids:userRoleGuids-Presentation Property*/


	public class UserRoleGuids extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public UserRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoleGuids:userRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoleGuids:userRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public UserRoleGuids getUserRoleGuids();
	/*Radix::Reports::ReportPub:inheritedPub:inheritedPub-Presentation Property*/


	public class InheritedPub extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public InheritedPub(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Reports.explorer.ReportPub.ReportPub_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Reports.explorer.ReportPub.ReportPub_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Reports.explorer.ReportPub.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Reports.explorer.ReportPub.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPub:inheritedPub")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPub:inheritedPub")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public InheritedPub getInheritedPub();
	/*Radix::Reports::ReportPub:seq:seq-Presentation Property*/


	public class Seq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Seq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:seq:seq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:seq:seq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Seq getSeq();
	/*Radix::Reports::ReportPub:isEnabled:isEnabled-Presentation Property*/


	public class IsEnabled extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsEnabled(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isEnabled:isEnabled")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isEnabled:isEnabled")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsEnabled getIsEnabled();
	/*Radix::Reports::ReportPub:parentTopic:parentTopic-Presentation Property*/


	public class ParentTopic extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ParentTopic(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Reports.explorer.ReportPubTopic.ReportPubTopic_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Reports.explorer.ReportPubTopic.ReportPubTopic_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Reports.explorer.ReportPubTopic.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Reports.explorer.ReportPubTopic.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopic:parentTopic")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopic:parentTopic")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ParentTopic getParentTopic();
	/*Radix::Reports::ReportPub:description:description-Presentation Property*/


	public class Description extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Description(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::Reports::ReportPub:parentTopicId:parentTopicId-Presentation Property*/


	public class ParentTopicId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ParentTopicId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopicId:parentTopicId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopicId:parentTopicId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParentTopicId getParentTopicId();
	/*Radix::Reports::ReportPub:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb-Presentation Property*/


	public class MaxResultSetCacheSizeKb extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxResultSetCacheSizeKb(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxResultSetCacheSizeKb getMaxResultSetCacheSizeKb();
	/*Radix::Reports::ReportPub:listId:listId-Presentation Property*/


	public class ListId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ListId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:listId:listId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:listId:listId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ListId getListId();
	/*Radix::Reports::ReportPub:isForCustomer:isForCustomer-Presentation Property*/


	public class IsForCustomer extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsForCustomer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isForCustomer:isForCustomer")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isForCustomer:isForCustomer")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsForCustomer getIsForCustomer();
	/*Radix::Reports::ReportPub:paramBinding:paramBinding-Presentation Property*/


	public class ParamBinding extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public ParamBinding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:paramBinding:paramBinding")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:paramBinding:paramBinding")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ParamBinding getParamBinding();
	/*Radix::Reports::ReportPub:printOnly:printOnly-Presentation Property*/


	public class PrintOnly extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PrintOnly(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:printOnly:printOnly")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:printOnly:printOnly")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PrintOnly getPrintOnly();
	/*Radix::Reports::ReportPub:reportPubList:reportPubList-Presentation Property*/


	public class ReportPubList extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ReportPubList(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Reports.explorer.ReportPubList.ReportPubList_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Reports.explorer.ReportPubList.ReportPubList_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Reports.explorer.ReportPubList.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Reports.explorer.ReportPubList.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportPubList:reportPubList")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportPubList:reportPubList")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ReportPubList getReportPubList();
	/*Radix::Reports::ReportPub:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Reports::ReportPub:inheritedPubLocation:inheritedPubLocation-Presentation Property*/


	public class InheritedPubLocation extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public InheritedPubLocation(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubLocation:inheritedPubLocation")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubLocation:inheritedPubLocation")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InheritedPubLocation getInheritedPubLocation();
	/*Radix::Reports::ReportPub:reportClassName:reportClassName-Presentation Property*/


	public class ReportClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassName:reportClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassName:reportClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportClassName getReportClassName();
	/*Radix::Reports::ReportPub:contextObjectClassGuid:contextObjectClassGuid-Presentation Property*/


	public class ContextObjectClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ContextObjectClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextObjectClassGuid:contextObjectClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextObjectClassGuid:contextObjectClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ContextObjectClassGuid getContextObjectClassGuid();
	/*Radix::Reports::ReportPub:reportFullClassName:reportFullClassName-Presentation Property*/


	public class ReportFullClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportFullClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportFullClassName:reportFullClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportFullClassName:reportFullClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportFullClassName getReportFullClassName();
	/*Radix::Reports::ReportPub:pubContextClassGuid:pubContextClassGuid-Presentation Property*/


	public class PubContextClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PubContextClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextClassGuid:pubContextClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextClassGuid:pubContextClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PubContextClassGuid getPubContextClassGuid();
	/*Radix::Reports::ReportPub:contextTitle:contextTitle-Presentation Property*/


	public class ContextTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ContextTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextTitle:contextTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextTitle:contextTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ContextTitle getContextTitle();
	/*Radix::Reports::ReportPub:areTextBasedFormatsSupported:areTextBasedFormatsSupported-Presentation Property*/


	public class AreTextBasedFormatsSupported extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AreTextBasedFormatsSupported(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:areTextBasedFormatsSupported:areTextBasedFormatsSupported")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:areTextBasedFormatsSupported:areTextBasedFormatsSupported")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AreTextBasedFormatsSupported getAreTextBasedFormatsSupported();
	/*Radix::Reports::ReportPub:userRoles:userRoles-Presentation Property*/


	public class UserRoles extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UserRoles(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoles:userRoles")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoles:userRoles")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UserRoles getUserRoles();
	/*Radix::Reports::ReportPub:exportFormats:exportFormats-Presentation Property*/


	public class ExportFormats extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ExportFormats(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Reports.common.ReportExportFormat.Arr> getValClass(){
			return org.radixware.ads.Reports.common.ReportExportFormat.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:exportFormats:exportFormats")
		public  org.radixware.ads.Reports.common.ReportExportFormat.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:exportFormats:exportFormats")
		public   void setValue(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
			Value = val;
		}
	}
	public ExportFormats getExportFormats();
	/*Radix::Reports::ReportPub:isMultyFile:isMultyFile-Presentation Property*/


	public class IsMultyFile extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsMultyFile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isMultyFile:isMultyFile")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isMultyFile:isMultyFile")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsMultyFile getIsMultyFile();
	/*Radix::Reports::ReportPub:finalTitle:finalTitle-Presentation Property*/


	public class FinalTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FinalTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:finalTitle:finalTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:finalTitle:finalTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FinalTitle getFinalTitle();
	/*Radix::Reports::ReportPub:supportedFormats:supportedFormats-Presentation Property*/


	public class SupportedFormats extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public SupportedFormats(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Reports.common.ReportExportFormat.Arr> getValClass(){
			return org.radixware.ads.Reports.common.ReportExportFormat.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:supportedFormats:supportedFormats")
		public  org.radixware.ads.Reports.common.ReportExportFormat.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:supportedFormats:supportedFormats")
		public   void setValue(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
			Value = val;
		}
	}
	public SupportedFormats getSupportedFormats();
	/*Radix::Reports::ReportPub:reportClassTitle:reportClassTitle-Presentation Property*/


	public class ReportClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassTitle:reportClassTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassTitle:reportClassTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportClassTitle getReportClassTitle();
	/*Radix::Reports::ReportPub:inheritedPubReportClassName:inheritedPubReportClassName-Presentation Property*/


	public class InheritedPubReportClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public InheritedPubReportClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassName:inheritedPubReportClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassName:inheritedPubReportClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InheritedPubReportClassName getInheritedPubReportClassName();
	/*Radix::Reports::ReportPub:inheritedPubReportClassGuid:inheritedPubReportClassGuid-Presentation Property*/


	public class InheritedPubReportClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public InheritedPubReportClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassGuid:inheritedPubReportClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassGuid:inheritedPubReportClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InheritedPubReportClassGuid getInheritedPubReportClassGuid();
	/*Radix::Reports::ReportPub:pubContextPid:pubContextPid-Presentation Property*/


	public class PubContextPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PubContextPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextPid:pubContextPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextPid:pubContextPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PubContextPid getPubContextPid();
	/*Radix::Reports::ReportPub:encoding:encoding-Presentation Property*/


	public class Encoding extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Encoding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:encoding:encoding")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:encoding:encoding")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Encoding getEncoding();
	/*Radix::Reports::ReportPub:allowedFormats:allowedFormats-Presentation Property*/


	public class AllowedFormats extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public AllowedFormats(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Reports.common.ReportExportFormat.Arr> getValClass(){
			return org.radixware.ads.Reports.common.ReportExportFormat.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:allowedFormats:allowedFormats")
		public  org.radixware.ads.Reports.common.ReportExportFormat.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:allowedFormats:allowedFormats")
		public   void setValue(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
			Value = val;
		}
	}
	public AllowedFormats getAllowedFormats();
	/*Radix::Reports::ReportPub:calcPdfSecurityOptions:calcPdfSecurityOptions-Presentation Property*/


	public class CalcPdfSecurityOptions extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public CalcPdfSecurityOptions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:calcPdfSecurityOptions:calcPdfSecurityOptions")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:calcPdfSecurityOptions:calcPdfSecurityOptions")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public CalcPdfSecurityOptions getCalcPdfSecurityOptions();
	public static class ChooseReport extends org.radixware.kernel.common.client.models.items.Command{
		protected ChooseReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
			processResponse(response, propertyId);
		}

	}

	public static class BeforeInputParams extends org.radixware.kernel.common.client.models.items.Command{
		protected BeforeInputParams(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.reports.GenerateReportRqDocument send(org.radixware.schemas.reports.GenerateReportRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.reports.GenerateReportRqDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.reports.GenerateReportRqDocument.class);
		}

	}

	public static class ExportReport extends org.radixware.kernel.common.client.models.items.Command{
		protected ExportReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.reports.GenerateReportRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}



}

/* Radix::Reports::ReportPub - Desktop Meta*/

/*Radix::Reports::ReportPub-Entity Class*/

package org.radixware.ads.Reports.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPub_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports::ReportPub:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
			"Radix::Reports::ReportPub",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZV433DCBJRFDTFK7PCI7ZJURGU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG53YVNHFNHZZPJTDWK3EJWEMQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR6SCZBID3BATLNJAEEMGFMIWK4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG53YVNHFNHZZPJTDWK3EJWEMQ"),0,

			/*Radix::Reports::ReportPub:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Reports::ReportPub:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJYMCSU7D2ZHIVC2U3Y2XCI7NVI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCCJD67UNVD4LM7B3IKHJPER5Y"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7DUY6Z2LYBCEPMLL6AFXLPVLU4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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

						/*Radix::Reports::ReportPub:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:format:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7FRUCC2LRVCFDN26X3OA73GNIY"),
						"format",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6IW7MDKVIVHFHLIHFPSEIJ46QI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("application/pdf"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:format:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL63KG5SUZRCGBAIMS6WUX5IHAY")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Reports::ReportPub:inheritedPubId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BK2KKPDYJCOFIAHL23AZ4XOCE"),
						"inheritedPubId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:inheritedPubId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:isEnabled:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLOAH3AWYW5DLBHJSUCBMFDJEHY"),
						"isEnabled",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS7UECLSNVDSDHT3O77MHCV4RY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:isEnabled:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:listId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXFVM3NF5JBVPM44EQV6TLOEB4"),
						"listId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:listId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:paramBinding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWN7EWE5K45C4HNELVSOXTYA2RM"),
						"paramBinding",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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

						/*Radix::Reports::ReportPub:paramBinding:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:parentTopicId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNK3L4L6SJFGZBFKG64XTIGTBM"),
						"parentTopicId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:parentTopicId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:printOnly:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX5QBHA4M3NABRLT52FMFOWQVYQ"),
						"printOnly",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZLDF446Y6NGAXNG5BM4R3GU52Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:printOnly:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQV2RHG7LRHZVJ2KHGLJI2HVEQ"),
						"reportClassGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3TKQH37GMNDAPLUQDIUXNEVVAY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:reportClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:seq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),
						"seq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ74K6773TBDGFGGUTJIGOR6GOA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:seq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU7SJ5KBINF27AV7GLWZLCIO4E"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSRLBLESA5BH3KQ55ATNMQOOLA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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

						/*Radix::Reports::ReportPub:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSZBTRRARNB2VHTV44D7QIFHT4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:userRoleGuids:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFIB7BM27NH67GFPFCDMY7Y7OQ"),
						"userRoleGuids",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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

						/*Radix::Reports::ReportPub:userRoleGuids:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportClassTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXDBOLEH76RBMLLMYVGBLB3AKS4"),
						"reportClassTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJNY6FL7LFEXLFZMAXXVZH4MRQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:reportClassTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:finalTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV4UEKFQHKNGRVCLOX4V33D4NQY"),
						"finalTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5XHSYWNCTZAFNK57WOHRSYRVVY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:finalTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:userRoles:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5NFR5GNJNA7BMUCQG7VVJHGGQ"),
						"userRoles",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETZLOQWHDNARHJ5XEL6FHFON7Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeVBBSP3NKG5HVVODE7MOX4U7DWI"),
						true,

						/*Radix::Reports::ReportPub:userRoles:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJTWNFMUOOZCUVBFYPRIXVONOW4"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2GB4IHLRWVAHTCYF3F2KNK2WBU"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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

						/*Radix::Reports::ReportPub:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:inheritedPub:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPFZICKAKZCNXCW7XNDNUCERJM"),
						"inheritedPub",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LNAYJZ5XRGA5OYEMUVJFO6KRQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},
						null,
						null,
						262143,
						262143,false),

					/*Radix::Reports::ReportPub:inheritedPubLocation:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3CGHHRLW55CQJNQJXE6DBUPWQY"),
						"inheritedPubLocation",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWP6FTGCPWVAIPMIYXUHAF3ZAUY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:inheritedPubLocation:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJFQKO3FO3FHUBKWSTGU46Q7PEI"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportPubList:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXFJFNCRMKBDMZID5HZLAXCIOZY"),
						"reportPubList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5FBPZNPVQRBRNMIZLHIHVIGUN4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ5T72ARKKBHHNBHOH3LPATQA6M"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::Reports::ReportPub:contextObjectClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF2L5IFQC5NB6ZJDNZYEPVN4A3A"),
						"contextObjectClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:contextObjectClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportFullClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E"),
						"reportFullClassName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2HVIHI3M4FBAVGXFSEU5S46LFI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:reportFullClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:isForCustomer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVP3HR4T4VRDJXE2SQKI3CUOSJI"),
						"isForCustomer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBAMHIDMXBEXTJ2S5B5XZKIQMU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:isForCustomer:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:parentTopic:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMZ7FVBTVMBBURC66IPV3VL53WI"),
						"parentTopic",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec34F7JPGYHJEJHBCE7ISQKJN3IY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl34F7JPGYHJEJHBCE7ISQKJN3IY"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Reports::ReportPub:pubContextPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6ZTLI62DFAU3IVMRXRS2RP4O4"),
						"pubContextPid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:pubContextPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:contextTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKVBCP4A4NBENTENHZNSR2CHMTQ"),
						"contextTitle",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:contextTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:pubContextClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLBBRZHEYFFWRBZCNQXVDGZIVE"),
						"pubContextClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:pubContextClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:encoding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2IHMCWS4PBBZLGPMBLXPQVCACM"),
						"encoding",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls23SA6GQDEREUZKJ7Q3N3Z7XXMY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::Reports::ReportPub:encoding:PropertyPresentation:Edit Options:-Edit Mask List*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskList(),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQXOMLCULBGLFHC5ZME2ENHYQ4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:isMultyFile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU62DMLZ7VBHRZI4XKH3QBOGE44"),
						"isMultyFile",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:isMultyFile:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:areTextBasedFormatsSupported:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPCQMGHIZIRCRJHRVPCEXLMA3KQ"),
						"areTextBasedFormatsSupported",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:areTextBasedFormatsSupported:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:allowedFormats:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFPX3ORINZ5ADVJBS6BMJPBRPLA"),
						"allowedFormats",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCN764DRY3FA2BHEH66DMQIBWQQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:allowedFormats:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDLK7ULKYSZGRPJBJEK4JP5A6KE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBKRJKNUS7FAATAMABOKVMGWD7Y"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:supportedFormats:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX2LTASUESJFR5BOELWAWGLC6RE"),
						"supportedFormats",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),
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

						/*Radix::Reports::ReportPub:supportedFormats:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:exportFormats:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU46DA6EHUVCP3NLRNW3JMBDUKY"),
						"exportFormats",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),
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

						/*Radix::Reports::ReportPub:exportFormats:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:calcPdfSecurityOptions:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXBSUGY7HRNDEJJYTZBOJ7BGNR4"),
						"calcPdfSecurityOptions",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5CE6DSMOGVFXZHH2RMLQBDM4II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::Reports::ReportPub:maxResultSetCacheSizeKb:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPS2XXJEY6ZHUXMSQRBWX5CKMNU"),
						"maxResultSetCacheSizeKb",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGGWFKKFPFBFXJRGZI76YYZUW4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						true,
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

						/*Radix::Reports::ReportPub:maxResultSetCacheSizeKb:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3NQ35UT3PZFMTC56PJMYAHOZN4"),
						"reportClassName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:reportClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:columnsSettings:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KG2NQHYBFGGJLWG2GKI4A6APE"),
						"columnsSettings",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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

						/*Radix::Reports::ReportPub:columnsSettings:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:inheritedPubReportClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPMYIAKHDBBPHHJB5COTEL5NHA"),
						"inheritedPubReportClassName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:inheritedPubReportClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:inheritedPubReportClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYCGMKKZQYNAUNPB2IBHKW2FHM4"),
						"inheritedPubReportClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:inheritedPubReportClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports::ReportPub:ExportReport-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd7XQZT2UPTJFX3KEDPFDV5WQM2A"),
						"ExportReport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPYRBNJJB35C2BDIMQLGZW6G5SE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3627K6GYUFGXFJYWMVCBQAPHUA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports::ReportPub:ChooseReport-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2EVERXXOYND7JCLVEEVPOJGOIY"),
						"ChooseReport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMG2PRIV4HRHTTPE222LNCJCZI4"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports::ReportPub:BeforeInputParams-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2YYVB5QGYNCQ3CWFFTY2EYZZMU"),
						"BeforeInputParams",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3627K6GYUFGXFJYWMVCBQAPHUA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
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
					/*Radix::Reports::ReportPub:bySeq-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQ64NKOZ4VVFUTGRIGEUCIFMBLE"),
						"bySeq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref33FTUNJWBNG7XILKNQF3VKWVRA"),"ReportPub=>ReportPubTopic (parentTopicId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl34F7JPGYHJEJHBCE7ISQKJN3IY"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNK3L4L6SJFGZBFKG64XTIGTBM")},new String[]{"parentTopicId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colOLKKTSVUOJHDTGFXOHQDX2Q46Y")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refBOVOJ7JCA5DABMRYFMKTL2IMRQ"),"ReportPub=>ReportPub (inheritedPubId=>Id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BK2KKPDYJCOFIAHL23AZ4XOCE")},new String[]{"inheritedPubId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ")},new String[]{"Id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refJLB3T4LDYNGENHZH4FUXEMEU7E"),"ReportPub=>ReportPubList (listId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ5T72ARKKBHHNBHOH3LPATQA6M"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXFVM3NF5JBVPM44EQV6TLOEB4")},new String[]{"listId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E")},
			true,false,false);
}

/* Radix::Reports::ReportPub - Web Executable*/

/*Radix::Reports::ReportPub-Entity Class*/

package org.radixware.ads.Reports.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub")
public interface ReportPub {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Reports.web.ReportPub.ReportPub_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Reports.web.ReportPub.ReportPub_DefaultModel )  super.getEntity(i);}
	}































































































































































































































	/*Radix::Reports::ReportPub:inheritedPubId:inheritedPubId-Presentation Property*/


	public class InheritedPubId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InheritedPubId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubId:inheritedPubId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubId:inheritedPubId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InheritedPubId getInheritedPubId();
	/*Radix::Reports::ReportPub:classGuid:classGuid-Presentation Property*/


	public class ClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::Reports::ReportPub:columnsSettings:columnsSettings-Presentation Property*/


	public class ColumnsSettings extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public ColumnsSettings(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:columnsSettings:columnsSettings")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:columnsSettings:columnsSettings")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ColumnsSettings getColumnsSettings();
	/*Radix::Reports::ReportPub:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Reports::ReportPub:format:format-Presentation Property*/


	public class Format extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Format(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Deprecated
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EReportExportFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EReportExportFormat ? (org.radixware.kernel.common.enums.EReportExportFormat)x : org.radixware.kernel.common.enums.EReportExportFormat.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Deprecated
		@Override
		public Class<org.radixware.kernel.common.enums.EReportExportFormat> getValClass(){
			return org.radixware.kernel.common.enums.EReportExportFormat.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EReportExportFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EReportExportFormat ? (org.radixware.kernel.common.enums.EReportExportFormat)x : org.radixware.kernel.common.enums.EReportExportFormat.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}






				














		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:format:format")
		public  org.radixware.kernel.common.enums.EReportExportFormat getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:format:format")
		public   void setValue(org.radixware.kernel.common.enums.EReportExportFormat val) {
			Value = val;
		}
	}
	public Format getFormat();
	/*Radix::Reports::ReportPub:reportClassGuid:reportClassGuid-Presentation Property*/


	public class ReportClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassGuid:reportClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassGuid:reportClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportClassGuid getReportClassGuid();
	/*Radix::Reports::ReportPub:userRoleGuids:userRoleGuids-Presentation Property*/


	public class UserRoleGuids extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public UserRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoleGuids:userRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoleGuids:userRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public UserRoleGuids getUserRoleGuids();
	/*Radix::Reports::ReportPub:inheritedPub:inheritedPub-Presentation Property*/


	public class InheritedPub extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public InheritedPub(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Reports.web.ReportPub.ReportPub_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Reports.web.ReportPub.ReportPub_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Reports.web.ReportPub.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Reports.web.ReportPub.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPub:inheritedPub")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPub:inheritedPub")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public InheritedPub getInheritedPub();
	/*Radix::Reports::ReportPub:seq:seq-Presentation Property*/


	public class Seq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Seq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:seq:seq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:seq:seq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Seq getSeq();
	/*Radix::Reports::ReportPub:isEnabled:isEnabled-Presentation Property*/


	public class IsEnabled extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsEnabled(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isEnabled:isEnabled")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isEnabled:isEnabled")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsEnabled getIsEnabled();
	/*Radix::Reports::ReportPub:parentTopic:parentTopic-Presentation Property*/


	public class ParentTopic extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ParentTopic(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Reports.web.ReportPubTopic.ReportPubTopic_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Reports.web.ReportPubTopic.ReportPubTopic_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Reports.web.ReportPubTopic.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Reports.web.ReportPubTopic.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopic:parentTopic")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopic:parentTopic")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ParentTopic getParentTopic();
	/*Radix::Reports::ReportPub:description:description-Presentation Property*/


	public class Description extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Description(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::Reports::ReportPub:parentTopicId:parentTopicId-Presentation Property*/


	public class ParentTopicId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ParentTopicId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopicId:parentTopicId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:parentTopicId:parentTopicId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParentTopicId getParentTopicId();
	/*Radix::Reports::ReportPub:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb-Presentation Property*/


	public class MaxResultSetCacheSizeKb extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxResultSetCacheSizeKb(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxResultSetCacheSizeKb getMaxResultSetCacheSizeKb();
	/*Radix::Reports::ReportPub:listId:listId-Presentation Property*/


	public class ListId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ListId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:listId:listId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:listId:listId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ListId getListId();
	/*Radix::Reports::ReportPub:isForCustomer:isForCustomer-Presentation Property*/


	public class IsForCustomer extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsForCustomer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isForCustomer:isForCustomer")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isForCustomer:isForCustomer")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsForCustomer getIsForCustomer();
	/*Radix::Reports::ReportPub:paramBinding:paramBinding-Presentation Property*/


	public class ParamBinding extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public ParamBinding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:paramBinding:paramBinding")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:paramBinding:paramBinding")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ParamBinding getParamBinding();
	/*Radix::Reports::ReportPub:printOnly:printOnly-Presentation Property*/


	public class PrintOnly extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PrintOnly(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:printOnly:printOnly")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:printOnly:printOnly")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PrintOnly getPrintOnly();
	/*Radix::Reports::ReportPub:reportPubList:reportPubList-Presentation Property*/


	public class ReportPubList extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ReportPubList(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Reports.web.ReportPubList.ReportPubList_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Reports.web.ReportPubList.ReportPubList_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Reports.web.ReportPubList.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Reports.web.ReportPubList.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportPubList:reportPubList")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportPubList:reportPubList")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ReportPubList getReportPubList();
	/*Radix::Reports::ReportPub:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Reports::ReportPub:inheritedPubLocation:inheritedPubLocation-Presentation Property*/


	public class InheritedPubLocation extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public InheritedPubLocation(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubLocation:inheritedPubLocation")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubLocation:inheritedPubLocation")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InheritedPubLocation getInheritedPubLocation();
	/*Radix::Reports::ReportPub:reportClassName:reportClassName-Presentation Property*/


	public class ReportClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassName:reportClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassName:reportClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportClassName getReportClassName();
	/*Radix::Reports::ReportPub:contextObjectClassGuid:contextObjectClassGuid-Presentation Property*/


	public class ContextObjectClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ContextObjectClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextObjectClassGuid:contextObjectClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextObjectClassGuid:contextObjectClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ContextObjectClassGuid getContextObjectClassGuid();
	/*Radix::Reports::ReportPub:reportFullClassName:reportFullClassName-Presentation Property*/


	public class ReportFullClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportFullClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportFullClassName:reportFullClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportFullClassName:reportFullClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportFullClassName getReportFullClassName();
	/*Radix::Reports::ReportPub:pubContextClassGuid:pubContextClassGuid-Presentation Property*/


	public class PubContextClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PubContextClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextClassGuid:pubContextClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextClassGuid:pubContextClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PubContextClassGuid getPubContextClassGuid();
	/*Radix::Reports::ReportPub:contextTitle:contextTitle-Presentation Property*/


	public class ContextTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ContextTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextTitle:contextTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:contextTitle:contextTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ContextTitle getContextTitle();
	/*Radix::Reports::ReportPub:areTextBasedFormatsSupported:areTextBasedFormatsSupported-Presentation Property*/


	public class AreTextBasedFormatsSupported extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AreTextBasedFormatsSupported(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:areTextBasedFormatsSupported:areTextBasedFormatsSupported")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:areTextBasedFormatsSupported:areTextBasedFormatsSupported")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AreTextBasedFormatsSupported getAreTextBasedFormatsSupported();
	/*Radix::Reports::ReportPub:userRoles:userRoles-Presentation Property*/


	public class UserRoles extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UserRoles(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoles:userRoles")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:userRoles:userRoles")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UserRoles getUserRoles();
	/*Radix::Reports::ReportPub:exportFormats:exportFormats-Presentation Property*/


	public class ExportFormats extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ExportFormats(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Reports.common.ReportExportFormat.Arr> getValClass(){
			return org.radixware.ads.Reports.common.ReportExportFormat.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:exportFormats:exportFormats")
		public  org.radixware.ads.Reports.common.ReportExportFormat.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:exportFormats:exportFormats")
		public   void setValue(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
			Value = val;
		}
	}
	public ExportFormats getExportFormats();
	/*Radix::Reports::ReportPub:isMultyFile:isMultyFile-Presentation Property*/


	public class IsMultyFile extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsMultyFile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isMultyFile:isMultyFile")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:isMultyFile:isMultyFile")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsMultyFile getIsMultyFile();
	/*Radix::Reports::ReportPub:finalTitle:finalTitle-Presentation Property*/


	public class FinalTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FinalTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:finalTitle:finalTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:finalTitle:finalTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FinalTitle getFinalTitle();
	/*Radix::Reports::ReportPub:supportedFormats:supportedFormats-Presentation Property*/


	public class SupportedFormats extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public SupportedFormats(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Reports.common.ReportExportFormat.Arr> getValClass(){
			return org.radixware.ads.Reports.common.ReportExportFormat.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:supportedFormats:supportedFormats")
		public  org.radixware.ads.Reports.common.ReportExportFormat.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:supportedFormats:supportedFormats")
		public   void setValue(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
			Value = val;
		}
	}
	public SupportedFormats getSupportedFormats();
	/*Radix::Reports::ReportPub:reportClassTitle:reportClassTitle-Presentation Property*/


	public class ReportClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassTitle:reportClassTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:reportClassTitle:reportClassTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportClassTitle getReportClassTitle();
	/*Radix::Reports::ReportPub:inheritedPubReportClassName:inheritedPubReportClassName-Presentation Property*/


	public class InheritedPubReportClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public InheritedPubReportClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassName:inheritedPubReportClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassName:inheritedPubReportClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InheritedPubReportClassName getInheritedPubReportClassName();
	/*Radix::Reports::ReportPub:inheritedPubReportClassGuid:inheritedPubReportClassGuid-Presentation Property*/


	public class InheritedPubReportClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public InheritedPubReportClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassGuid:inheritedPubReportClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:inheritedPubReportClassGuid:inheritedPubReportClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InheritedPubReportClassGuid getInheritedPubReportClassGuid();
	/*Radix::Reports::ReportPub:pubContextPid:pubContextPid-Presentation Property*/


	public class PubContextPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PubContextPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextPid:pubContextPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:pubContextPid:pubContextPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PubContextPid getPubContextPid();
	/*Radix::Reports::ReportPub:encoding:encoding-Presentation Property*/


	public class Encoding extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Encoding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:encoding:encoding")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:encoding:encoding")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Encoding getEncoding();
	/*Radix::Reports::ReportPub:allowedFormats:allowedFormats-Presentation Property*/


	public class AllowedFormats extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public AllowedFormats(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Reports.common.ReportExportFormat.Arr> getValClass(){
			return org.radixware.ads.Reports.common.ReportExportFormat.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:allowedFormats:allowedFormats")
		public  org.radixware.ads.Reports.common.ReportExportFormat.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:allowedFormats:allowedFormats")
		public   void setValue(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {
			Value = val;
		}
	}
	public AllowedFormats getAllowedFormats();
	public static class ChooseReport extends org.radixware.kernel.common.client.models.items.Command{
		protected ChooseReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
			processResponse(response, propertyId);
		}

	}

	public static class BeforeInputParams extends org.radixware.kernel.common.client.models.items.Command{
		protected BeforeInputParams(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.reports.GenerateReportRqDocument send(org.radixware.schemas.reports.GenerateReportRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.reports.GenerateReportRqDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.reports.GenerateReportRqDocument.class);
		}

	}

	public static class ExportReport extends org.radixware.kernel.common.client.models.items.Command{
		protected ExportReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.reports.GenerateReportRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}



}

/* Radix::Reports::ReportPub - Web Meta*/

/*Radix::Reports::ReportPub-Entity Class*/

package org.radixware.ads.Reports.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPub_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports::ReportPub:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
			"Radix::Reports::ReportPub",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZV433DCBJRFDTFK7PCI7ZJURGU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG53YVNHFNHZZPJTDWK3EJWEMQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR6SCZBID3BATLNJAEEMGFMIWK4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG53YVNHFNHZZPJTDWK3EJWEMQ"),0,

			/*Radix::Reports::ReportPub:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Reports::ReportPub:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJYMCSU7D2ZHIVC2U3Y2XCI7NVI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCCJD67UNVD4LM7B3IKHJPER5Y"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7DUY6Z2LYBCEPMLL6AFXLPVLU4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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

						/*Radix::Reports::ReportPub:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:format:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7FRUCC2LRVCFDN26X3OA73GNIY"),
						"format",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6IW7MDKVIVHFHLIHFPSEIJ46QI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("application/pdf"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:format:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL63KG5SUZRCGBAIMS6WUX5IHAY")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Reports::ReportPub:inheritedPubId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BK2KKPDYJCOFIAHL23AZ4XOCE"),
						"inheritedPubId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:inheritedPubId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:isEnabled:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLOAH3AWYW5DLBHJSUCBMFDJEHY"),
						"isEnabled",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS7UECLSNVDSDHT3O77MHCV4RY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:isEnabled:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:listId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXFVM3NF5JBVPM44EQV6TLOEB4"),
						"listId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:listId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:paramBinding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWN7EWE5K45C4HNELVSOXTYA2RM"),
						"paramBinding",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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

						/*Radix::Reports::ReportPub:paramBinding:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:parentTopicId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNK3L4L6SJFGZBFKG64XTIGTBM"),
						"parentTopicId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:parentTopicId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:printOnly:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX5QBHA4M3NABRLT52FMFOWQVYQ"),
						"printOnly",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZLDF446Y6NGAXNG5BM4R3GU52Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:printOnly:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQV2RHG7LRHZVJ2KHGLJI2HVEQ"),
						"reportClassGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3TKQH37GMNDAPLUQDIUXNEVVAY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:reportClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:seq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),
						"seq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ74K6773TBDGFGGUTJIGOR6GOA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:seq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU7SJ5KBINF27AV7GLWZLCIO4E"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSRLBLESA5BH3KQ55ATNMQOOLA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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

						/*Radix::Reports::ReportPub:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSZBTRRARNB2VHTV44D7QIFHT4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:userRoleGuids:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFIB7BM27NH67GFPFCDMY7Y7OQ"),
						"userRoleGuids",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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

						/*Radix::Reports::ReportPub:userRoleGuids:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportClassTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXDBOLEH76RBMLLMYVGBLB3AKS4"),
						"reportClassTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJNY6FL7LFEXLFZMAXXVZH4MRQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:reportClassTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:finalTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV4UEKFQHKNGRVCLOX4V33D4NQY"),
						"finalTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5XHSYWNCTZAFNK57WOHRSYRVVY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:finalTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:userRoles:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5NFR5GNJNA7BMUCQG7VVJHGGQ"),
						"userRoles",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETZLOQWHDNARHJ5XEL6FHFON7Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpe7J5XAWOKZFHQTPMFLHATY3NMWQ"),
						true,

						/*Radix::Reports::ReportPub:userRoles:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJTWNFMUOOZCUVBFYPRIXVONOW4"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2GB4IHLRWVAHTCYF3F2KNK2WBU"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
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

						/*Radix::Reports::ReportPub:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:inheritedPub:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPFZICKAKZCNXCW7XNDNUCERJM"),
						"inheritedPub",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LNAYJZ5XRGA5OYEMUVJFO6KRQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},
						null,
						null,
						262143,
						262143,false),

					/*Radix::Reports::ReportPub:inheritedPubLocation:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3CGHHRLW55CQJNQJXE6DBUPWQY"),
						"inheritedPubLocation",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWP6FTGCPWVAIPMIYXUHAF3ZAUY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:inheritedPubLocation:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJFQKO3FO3FHUBKWSTGU46Q7PEI"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportPubList:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXFJFNCRMKBDMZID5HZLAXCIOZY"),
						"reportPubList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5FBPZNPVQRBRNMIZLHIHVIGUN4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ5T72ARKKBHHNBHOH3LPATQA6M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ5T72ARKKBHHNBHOH3LPATQA6M"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::Reports::ReportPub:contextObjectClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF2L5IFQC5NB6ZJDNZYEPVN4A3A"),
						"contextObjectClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:contextObjectClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportFullClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E"),
						"reportFullClassName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2HVIHI3M4FBAVGXFSEU5S46LFI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:reportFullClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:isForCustomer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVP3HR4T4VRDJXE2SQKI3CUOSJI"),
						"isForCustomer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBAMHIDMXBEXTJ2S5B5XZKIQMU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:isForCustomer:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:parentTopic:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMZ7FVBTVMBBURC66IPV3VL53WI"),
						"parentTopic",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec34F7JPGYHJEJHBCE7ISQKJN3IY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl34F7JPGYHJEJHBCE7ISQKJN3IY"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Reports::ReportPub:pubContextPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6ZTLI62DFAU3IVMRXRS2RP4O4"),
						"pubContextPid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:pubContextPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:contextTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKVBCP4A4NBENTENHZNSR2CHMTQ"),
						"contextTitle",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:contextTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:pubContextClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLBBRZHEYFFWRBZCNQXVDGZIVE"),
						"pubContextClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:pubContextClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:encoding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2IHMCWS4PBBZLGPMBLXPQVCACM"),
						"encoding",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls23SA6GQDEREUZKJ7Q3N3Z7XXMY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::Reports::ReportPub:encoding:PropertyPresentation:Edit Options:-Edit Mask List*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskList(),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQXOMLCULBGLFHC5ZME2ENHYQ4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:isMultyFile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU62DMLZ7VBHRZI4XKH3QBOGE44"),
						"isMultyFile",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:isMultyFile:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:areTextBasedFormatsSupported:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPCQMGHIZIRCRJHRVPCEXLMA3KQ"),
						"areTextBasedFormatsSupported",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:areTextBasedFormatsSupported:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:allowedFormats:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFPX3ORINZ5ADVJBS6BMJPBRPLA"),
						"allowedFormats",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCN764DRY3FA2BHEH66DMQIBWQQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Reports::ReportPub:allowedFormats:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDLK7ULKYSZGRPJBJEK4JP5A6KE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBKRJKNUS7FAATAMABOKVMGWD7Y"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:supportedFormats:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX2LTASUESJFR5BOELWAWGLC6RE"),
						"supportedFormats",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),
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

						/*Radix::Reports::ReportPub:supportedFormats:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:exportFormats:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU46DA6EHUVCP3NLRNW3JMBDUKY"),
						"exportFormats",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),
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

						/*Radix::Reports::ReportPub:exportFormats:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:calcPdfSecurityOptions:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXBSUGY7HRNDEJJYTZBOJ7BGNR4"),
						"calcPdfSecurityOptions",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5CE6DSMOGVFXZHH2RMLQBDM4II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false),

					/*Radix::Reports::ReportPub:maxResultSetCacheSizeKb:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPS2XXJEY6ZHUXMSQRBWX5CKMNU"),
						"maxResultSetCacheSizeKb",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGGWFKKFPFBFXJRGZI76YYZUW4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						true,
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

						/*Radix::Reports::ReportPub:maxResultSetCacheSizeKb:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:reportClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3NQ35UT3PZFMTC56PJMYAHOZN4"),
						"reportClassName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:reportClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:columnsSettings:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KG2NQHYBFGGJLWG2GKI4A6APE"),
						"columnsSettings",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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

						/*Radix::Reports::ReportPub:columnsSettings:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:inheritedPubReportClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPMYIAKHDBBPHHJB5COTEL5NHA"),
						"inheritedPubReportClassName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:inheritedPubReportClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports::ReportPub:inheritedPubReportClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYCGMKKZQYNAUNPB2IBHKW2FHM4"),
						"inheritedPubReportClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::Reports::ReportPub:inheritedPubReportClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports::ReportPub:ExportReport-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd7XQZT2UPTJFX3KEDPFDV5WQM2A"),
						"ExportReport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPYRBNJJB35C2BDIMQLGZW6G5SE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3627K6GYUFGXFJYWMVCBQAPHUA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports::ReportPub:BeforeInputParams-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2YYVB5QGYNCQ3CWFFTY2EYZZMU"),
						"BeforeInputParams",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3627K6GYUFGXFJYWMVCBQAPHUA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
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
					/*Radix::Reports::ReportPub:bySeq-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQ64NKOZ4VVFUTGRIGEUCIFMBLE"),
						"bySeq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref33FTUNJWBNG7XILKNQF3VKWVRA"),"ReportPub=>ReportPubTopic (parentTopicId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl34F7JPGYHJEJHBCE7ISQKJN3IY"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNK3L4L6SJFGZBFKG64XTIGTBM")},new String[]{"parentTopicId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colOLKKTSVUOJHDTGFXOHQDX2Q46Y")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refBOVOJ7JCA5DABMRYFMKTL2IMRQ"),"ReportPub=>ReportPub (inheritedPubId=>Id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BK2KKPDYJCOFIAHL23AZ4XOCE")},new String[]{"inheritedPubId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ")},new String[]{"Id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refJLB3T4LDYNGENHZH4FUXEMEU7E"),"ReportPub=>ReportPubList (listId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ5T72ARKKBHHNBHOH3LPATQA6M"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXFVM3NF5JBVPM44EQV6TLOEB4")},new String[]{"listId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ3B6R3LVVBGDZFTHVOYGQXIGW4")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E")},
			true,false,false);
}

/* Radix::Reports::ReportPub:General - Desktop Meta*/

/*Radix::Reports::ReportPub:General-Editor Presentation*/

package org.radixware.ads.Reports.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
	null,
	null,

	/*Radix::Reports::ReportPub:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Reports::ReportPub:General:Common-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgA4JVIX47OFEH5NN2N6O7WB7R6I"),"Common",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDVUTAJD2K5BCVHSQQ4B5TUWU5Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZV433DCBJRFDTFK7PCI7ZJURGU"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU7SJ5KBINF27AV7GLWZLCIO4E"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCCJD67UNVD4LM7B3IKHJPER5Y"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLOAH3AWYW5DLBHJSUCBMFDJEHY"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX5QBHA4M3NABRLT52FMFOWQVYQ"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5NFR5GNJNA7BMUCQG7VVJHGGQ"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPFZICKAKZCNXCW7XNDNUCERJM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3CGHHRLW55CQJNQJXE6DBUPWQY"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVP3HR4T4VRDJXE2SQKI3CUOSJI"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2IHMCWS4PBBZLGPMBLXPQVCACM"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFPX3ORINZ5ADVJBS6BMJPBRPLA"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXBSUGY7HRNDEJJYTZBOJ7BGNR4"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPS2XXJEY6ZHUXMSQRBWX5CKMNU"),0,14,1,false,false)
			},null),

			/*Radix::Reports::ReportPub:General:Parameters-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3E4TYYDJNJCVHGUXVRIA3SM6VQ"),"Parameters",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR7RU2BLUYBCNZO7HDPSMXRTPB4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgQENRUP5P6VATVFFTBZ5Y6O6KEU"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepQU46UOTNERCRTE7LVIKJQCKAYE")),

			/*Radix::Reports::ReportPub:General:Columns-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2STT5XKWHBFA5EYRDCDJB3QP3Y"),"Columns",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJPLQJINJSRGQHLBUZ5OTBTV7JU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgOFJRBBHDTBFYVLDYSQI4VHGH3E"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cep3WIKTYFHJRFWPCGXX2CECFA25M"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgA4JVIX47OFEH5NN2N6O7WB7R6I")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3E4TYYDJNJCVHGUXVRIA3SM6VQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2STT5XKWHBFA5EYRDCDJB3QP3Y"))}
	,

	/*Radix::Reports::ReportPub:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Reports::ReportPub:General - Web Meta*/

/*Radix::Reports::ReportPub:General-Editor Presentation*/

package org.radixware.ads.Reports.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
	null,
	null,

	/*Radix::Reports::ReportPub:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Reports::ReportPub:General:Common-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgA4JVIX47OFEH5NN2N6O7WB7R6I"),"Common",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDVUTAJD2K5BCVHSQQ4B5TUWU5Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZV433DCBJRFDTFK7PCI7ZJURGU"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU7SJ5KBINF27AV7GLWZLCIO4E"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCCJD67UNVD4LM7B3IKHJPER5Y"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLOAH3AWYW5DLBHJSUCBMFDJEHY"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX5QBHA4M3NABRLT52FMFOWQVYQ"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ5NFR5GNJNA7BMUCQG7VVJHGGQ"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPFZICKAKZCNXCW7XNDNUCERJM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3CGHHRLW55CQJNQJXE6DBUPWQY"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD67H7DF35E6DHJIDDOFGPQD4E"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVP3HR4T4VRDJXE2SQKI3CUOSJI"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2IHMCWS4PBBZLGPMBLXPQVCACM"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFPX3ORINZ5ADVJBS6BMJPBRPLA"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruXBSUGY7HRNDEJJYTZBOJ7BGNR4"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPS2XXJEY6ZHUXMSQRBWX5CKMNU"),0,14,1,false,false)
			},null),

			/*Radix::Reports::ReportPub:General:Parameters-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3E4TYYDJNJCVHGUXVRIA3SM6VQ"),"Parameters",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR7RU2BLUYBCNZO7HDPSMXRTPB4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgQENRUP5P6VATVFFTBZ5Y6O6KEU"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cep47I4OF2MRNB77ELTKDY2K6OQOA"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgA4JVIX47OFEH5NN2N6O7WB7R6I")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3E4TYYDJNJCVHGUXVRIA3SM6VQ"))}
	,

	/*Radix::Reports::ReportPub:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Reports::ReportPub:General:Model - Desktop Executable*/

/*Radix::Reports::ReportPub:General:Model-Entity Model Class*/

package org.radixware.ads.Reports.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model")
public class General:Model  extends org.radixware.ads.Reports.explorer.ReportPub.ReportPub_DefaultModel implements org.radixware.ads.Acs.common_client.IArrRoleHolder  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Reports::ReportPub:General:Model:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportPub:General:Model:Properties-Properties*/

	/*Radix::Reports::ReportPub:General:Model:reportClassId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id reportClassId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:reportClassId")
	public published  org.radixware.kernel.common.types.Id getReportClassId() {

		return Types::Id.Factory.loadFrom(reportClassGuid.Value);

	}

	/*Radix::Reports::ReportPub:General:Model:docForExport-Dynamic Property*/



	protected org.radixware.schemas.reports.GenerateReportRqDocument docForExport=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:docForExport")
	public  org.radixware.schemas.reports.GenerateReportRqDocument getDocForExport() {
		return docForExport;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:docForExport")
	public   void setDocForExport(org.radixware.schemas.reports.GenerateReportRqDocument val) {
		docForExport = val;
	}

	/*Radix::Reports::ReportPub:General:Model:allowedFormats-Presentation Property*/




	public class AllowedFormats extends org.radixware.ads.Reports.explorer.ReportPub.pruFPX3ORINZ5ADVJBS6BMJPBRPLA{
		public AllowedFormats(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Reports.common.ReportExportFormat.Arr> getValClass(){
			return org.radixware.ads.Reports.common.ReportExportFormat.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:allowedFormats")
		public  org.radixware.ads.Reports.common.ReportExportFormat.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:allowedFormats")
		public   void setValue(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {

			internal[allowedFormats] = val;
			setVisibility();
		}
	}
	public AllowedFormats getAllowedFormats(){return (AllowedFormats)getProperty(pruFPX3ORINZ5ADVJBS6BMJPBRPLA);}

	/*Radix::Reports::ReportPub:General:Model:reportFullClassName-Presentation Property*/




	public class ReportFullClassName extends org.radixware.ads.Reports.explorer.ReportPub.prdFD67H7DF35E6DHJIDDOFGPQD4E{
		public ReportFullClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:reportFullClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:reportFullClassName")
		public   void setValue(Str val) {

			boolean isRemoveColumnsSettings = !java.util.Objects.equals(internal[reportFullClassName], val);

			internal[reportFullClassName] = val;
			//if (val == null) {
			//    ().(false);
			//} else {
			//    ().(true);
			//}
			updateColumnsPageVisibility();

			columnsSettings.Value = isRemoveColumnsSettings ? null : columnsSettings.Value;
		}
	}
	public ReportFullClassName getReportFullClassName(){return (ReportFullClassName)getProperty(prdFD67H7DF35E6DHJIDDOFGPQD4E);}










	/*Radix::Reports::ReportPub:General:Model:Methods-Methods*/

	/*Radix::Reports::ReportPub:General:Model:chooseReport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:chooseReport")
	protected  void chooseReport (org.radixware.ads.Reports.explorer.ReportPub.ChooseReport command, org.radixware.kernel.common.types.Id propertyId) {
		final ChooseReportClassDialog dlg = new ChooseReportClassDialog(Environment);
		final ChooseReportClassDialog:Model model = (ChooseReportClassDialog:Model)dlg.getModel();
		model.selectedClassId = Types::Id.Factory.loadFrom(this.reportClassGuid.Value);
		model.contextObjectClassId = Types::Id.Factory.loadFrom(this.contextObjectClassGuid.Value);
		model.checkContextClassId = true;

		if (dlg.exec() == Explorer.Qt.Types::QDialog.DialogCode.Accepted.value()) {
		    this.reportClassGuid.setValueObject(model.selectedClassId.toString());
		    this.reportClassTitle.setValueObject(model.selectedClassTitle);
		    String fullName=model.selectedClassTitle+ " ("+model.selectedClassFullName+")";
		    this.reportFullClassName.setValueObject(fullName);
		    updateParametersBinding();
		    updateColumnsSettings();
		}

	}

	/*Radix::Reports::ReportPub:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();

		final boolean isInherited = this.inheritedPubId.Value != null;
		// ArrayList - concurent modification exception otherwise
		final java.util.List<Explorer.Models.Properties::Property> props = new java.util.ArrayList<Explorer.Models.Properties::Property>(this.getActiveProperties());
		for (Explorer.Models.Properties::Property prop : props) {
		    if (prop == this.id || prop == this.seq) {
		        // nothing
		    } else if (prop == this.inheritedPub || prop == this.inheritedPubLocation) {
		        prop.setVisible(isInherited);
		    } else if (prop == this.allowedFormats) {
		        prop.setVisible(!isNew() && !isInherited);
		    } else {
		        prop.setVisible(!isInherited);
		    }
		}

		reportClassTitle.setMandatory(!isInherited); // RADIX-4471
		try {
		    this.getEditorPage(idof[ReportPub:General:Parameters]).setVisible(!isInherited);
		} catch (Explorer.Exceptions::NoDefinitionWithSuchIdError e) {
		    return;//illegal usage
		}

		updateEncodingsList();
		updateAllowedFormats();
		setVisibility();
		/* презентации в производных классах перекрываются и функции добавляются явно
		final String CONDITION = "condition";
		if (!isInherited && () && this.().isPropertyDefExistsByName(CONDITION)) {
		    final  conditionPropId = this.().(CONDITION).getId();
		    final  editorPageId = ;
		    this.(editorPageId).(conditionPropId);
		}
		*/
	}

	/*Radix::Reports::ReportPub:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		updateParametersBinding();
		updateColumnsSettings();
	}

	/*Radix::Reports::ReportPub:General:Model:onCommand_ExportReport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:onCommand_ExportReport")
	public  void onCommand_ExportReport (org.radixware.ads.Reports.explorer.ReportPub.ExportReport command) {
		try{
		  ReportsXsd:GenerateReportRqDocument xIn = docForExport;
		  command.send(xIn);
		}catch(Exceptions::InterruptedException  e){
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::Reports::ReportPub:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (getEditorPage(idof[ReportPub:General:Parameters]).getView()!=null) {
		    ReportParametersBindingWidget editor = ReportPub:General:Parameters:View:EditorPageView:object;
		    ReportParametersBindingWidget:Model model = editor.Model;
		    model.finishEdit();
		}
		return super.beforeUpdate();
	}

	/*Radix::Reports::ReportPub:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (getEditorPage(idof[ReportPub:General:Parameters]).getView()!=null) {
		    ReportParametersBindingWidget editor = ReportPub:General:Parameters:View:EditorPageView:object;
		    ReportParametersBindingWidget:Model model = editor.Model;
		    model.finishEdit();
		}

		// заполнение bind для фиксации порядка параметров, который используется в Radix::Reports::ReportPubList:buildTreeListForCustomer
		if (paramBinding.Value == null && reportClassId != null) {
		    ReportsXsd:ParametersBindingDocument bind = ReportsXsd:ParametersBindingDocument.Factory.newInstance();
		    bind.addNewParametersBinding();
		    final Explorer.Meta::ReportPresentationDef reportPresentationDef = Environment.DefManager.getReportPresentationDef(reportClassId);
		    final java.util.List<Types::Id> propIds = reportPresentationDef.getPropertyIds();
		    for (Types::Id propId : propIds) {
		        final Explorer.Meta::PropertyDef propDef = reportPresentationDef.getPropertyDefById(propId);
		        if (propDef.getNature() == Meta::PropNature:SqlClassParameter && propId != reportPresentationDef.ContextParameterId) {
		            ReportsXsd:ParametersBindingType.ParameterBinding b = bind.ParametersBinding.addNewParameterBinding();
		            b.ParameterId = propId;
		            b.Restriction = ReportParameterRestriction:NONE;
		            b.Type = ReportParameterBindingType:Value;
		            if (propDef.getInitialVal() != null) {
		                Client.Utils::ValueConverter.objVal2EasPropXmlVal(propDef.getInitialVal().toObject(propDef.getType()), propDef.getType(), b.addNewValue());
		            }
		        }
		    }
		    paramBinding.Value = bind.xmlText();
		}



		if (inheritedPubId != null && inheritedPubId.Value != null) {
		    reportFullClassName.setMandatory(false);
		}

		return super.beforeCreate();
	}

	/*Radix::Reports::ReportPub:General:Model:createTmpPdfFile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:createTmpPdfFile")
	private final  java.io.File createTmpPdfFile (boolean isDir) throws java.io.IOException {
		final java.io.File tempFile;
		if (isDir) {
		    tempFile = java.io.File.createTempFile("Report-", "Dir");
		    tempFile.delete();
		    tempFile.mkdirs();
		    tempFile.deleteOnExit();
		} else {
		    tempFile = java.io.File.createTempFile("Report-", ".pdf");
		    tempFile.deleteOnExit();    
		}

		return tempFile;
	}

	/*Radix::Reports::ReportPub:General:Model:canSafelyClean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:canSafelyClean")
	public published  boolean canSafelyClean (org.radixware.kernel.common.client.models.CleanModelController controller) {
		if (getEditorPage(idof[ReportPub:General:Parameters]).getView()!=null) {
		    ReportParametersBindingWidget editor = ReportPub:General:Parameters:View:EditorPageView:object;
		    ReportParametersBindingWidget:Model model = editor.Model;
		    model.canSafelyClean(controller);    
		}
		return super.canSafelyClean(controller);
	}

	/*Radix::Reports::ReportPub:General:Model:inputReportParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:inputReportParams")
	public  boolean inputReportParams (org.radixware.kernel.common.client.models.EntityModel contextEntityModel, org.radixware.schemas.reports.GenerateReportRqDocument.GenerateReportRq request, org.radixware.kernel.common.client.models.EntityModel parentEntityModel) {
		Explorer.Models::ReportParamDialogModel paramDialogModel
		        = Explorer.Models::ReportParamDialogModel.create(reportClassId, this, parentEntityModel);

		final Explorer.Meta::ReportPresentationDef reportPresentationDef = paramDialogModel.getReportPresentationDef();
		final java.util.List<Types::Id> propIds = reportPresentationDef.getPropertyIds();
		boolean paramExists = false;

		ReportsXsd:ParametersBindingDocument parametersBindingDoc = null;
		if (paramBinding.Value != null) {
		    try {
		        parametersBindingDoc = ReportsXsd:ParametersBindingDocument.Factory.parse(paramBinding.Value);
		    } catch (Exceptions::Exception ex) {
		        Environment.processException(ex);
		        return false;
		    }
		}

		for (Types::Id propId : propIds) {
		    final Explorer.Meta::PropertyDef propDef = reportPresentationDef.getPropertyDefById(propId);
		    if (propDef.getNature() == Meta::PropNature:SqlClassParameter) {
		        final Explorer.Models.Properties::Property param = paramDialogModel.getProperty(propId);
		        boolean isFinal = false;
		        boolean isVisible = true;
		        try {
		            if (param.Id == reportPresentationDef.ContextParameterId && contextEntityModel != null) {
		                paramDialogModel.setReportContextModel(contextEntityModel);
		                Explorer.Models.Properties::PropertyValue propVal = new PropertyValue(propDef, new Reference(contextEntityModel.getPid(), contextEntityModel.getTitle()));
		                param.setServerValue(propVal);
		                isFinal = true;
		            } else {
		                final ReportsXsd:ParametersBindingType.ParameterBinding parameterBinding = ReportsExplorerUtils.findParameterBindingById(parametersBindingDoc, propId);
		                if (parameterBinding != null) {
		                    Object value = Explorer.Utils::PropUtils.getEasPropValue(param, parameterBinding.Value);
		                    if (param instanceof Explorer.Models.Properties::PropertyArrRef) {
		                        value = ((Explorer.Models.Properties::PropertyArrRef) param).updateTitles((Explorer.Types::ArrReference) value);
		                    } else if (param instanceof Explorer.Models.Properties::PropertyRef) {
		                       /* org.radixware.kernel.common.client.meta.RadParentRefPropertyDef pDef = (org.radixware.kernel.common.client.meta.RadParentRefPropertyDef) param.();
		                         tableId = pDef.();
		                        final  presentationId;
		                        if (pDef.() == null) {
		                            presentationId = null;
		                        } else {
		                            presentationId = pDef.().getId();
		                        }
		                        final  titlesProvider
		                                = new (param.(), tableId, presentationId);
		                        titlesProvider.(() value);
		                        final  titles = titlesProvider.getTitles();
		                        value = titles.((() value).());*/
		                        value = ((Explorer.Models.Properties::PropertyRef) param).updateTitle((Explorer.Types::Reference) value);
		                    }
		                    Explorer.Models.Properties::PropertyValue propVal = new PropertyValue(propDef, value);
		                    param.setServerValue(propVal);
		                    //.(param, parameterBinding.Value);                   
		                    if (param.Id == reportPresentationDef.ContextParameterId) {
		                        Explorer.Meta::ParentRefPropertyDef propParentRef = (Explorer.Meta::ParentRefPropertyDef) param.getDefinition();
		                        Explorer.Types::Reference ref = (Explorer.Types::Reference) param.getValueObject();
		                        Explorer.Meta::PresentationClassDef classDef = getEnvironment().getDefManager().getClassPresentationDef(propParentRef.getReferencedClassId());
		                        EntityModel model = Explorer.Models::EntityModel.openContextlessModel(getEnvironment(), ref.getPid(), propParentRef.getReferencedClassId(), classDef.getEditorPresentationIds());
		                        paramDialogModel.setReportContextModel(model);
		                    }
		                    if (parameterBinding.isSetFinal()) {
		                        isFinal = parameterBinding.Final;
		                    } else if (parameterBinding.isSetRestriction()) {
		                        isFinal = parameterBinding.Restriction == ReportParameterRestriction:CONST;
		                        isVisible = !(parameterBinding.Restriction == ReportParameterRestriction:HIDDEN);
		                    }
		                }
		            }
		        } catch (ClassCastException ex) {
		            final String message = Str.format("Type of '%s' parameter value does not match the one specified in parameters binding....", param.getTitle());
		            Environment.messageWarning(message);
		        } catch (Exceptions::InterruptedException ex) {
		        } catch (Exceptions::ServiceClientException ex) {
		            Environment.processException(ex);
		        }

		        param.setReadonly(isFinal);
		        param.setVisible(isVisible);
		        if (!isFinal && isVisible)
		            paramExists = true;
		    }
		}

		if (paramExists)
		    try {
		        ReportsXsd:GenerateReportRqDocument rqDoc = ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		        ReportsXsd:GenerateReportRqDocument.GenerateReportRq rqReport = rqDoc.addNewGenerateReportRq();
		        rqReport.setParamValues(paramDialogModel.writeProperties());
		        Xml out = getEnvironment().EasSession.executeCommand(this, null, idof[ReportPub:BeforeInputParams], null, rqDoc).Output;
		        ReportsXsd:GenerateReportRqDocument rs = ReportsXsd:GenerateReportRqDocument.Factory.parse(out.DomNode.FirstChild);
		        paramDialogModel.readProperties(rs.GenerateReportRq.ParamValues);
		    } catch (Exceptions::Exception ex) {
		        Environment.processException(ex);
		        return false;
		    }

		if (!paramExists || paramDialogModel.execDialog() == org.radixware.kernel.common.client.views.IDialog.DialogResult.ACCEPTED) {
		    request.setParamValues(paramDialogModel.writeProperties());
		    return true;
		} else
		    return false;


	}

	/*Radix::Reports::ReportPub:General:Model:setVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:setVisibility")
	public  void setVisibility () {
		if (allowedFormats.Value != null) {
		    calcPdfSecurityOptions.setVisible(allowedFormats.Value.contains(ReportExportFormat:PDF));
		    encoding.setVisible(allowedFormats.Value.contains(ReportExportFormat:TXT) || allowedFormats.Value.contains(ReportExportFormat:CSV));
		} else {
		    encoding.setVisible(areTextBasedFormatsSupported.Value == Boolean.TRUE);
		    calcPdfSecurityOptions.setVisible(supportedFormats.Value != null && (supportedFormats.Value.contains(ReportExportFormat:PDF)));
		}

		updateColumnsPageVisibility();
	}

	/*Radix::Reports::ReportPub:General:Model:suggestFileName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:suggestFileName")
	public  Str suggestFileName () {
		final Explorer.EditMask::EditMaskDateTime mask = new EditMaskDateTime("dd-MM-yy_HH-mm", null, null);
		if (reportClassTitle.getValue()!=null && !reportClassTitle.getValue().isEmpty()){
		    return reportClassTitle.getValue()+"_"+mask.toStr(getEnvironment(), getEnvironment().getCurrentServerTime());
		}else if (title.getValue()!=null && !title.getValue().isEmpty()){
		    return title.getValue()+"_"+mask.toStr(getEnvironment(), getEnvironment().getCurrentServerTime());
		}else{
		    return "report"+"_"+mask.toStr(getEnvironment(), getEnvironment().getCurrentServerTime());
		}
	}

	/*Radix::Reports::ReportPub:General:Model:updateAllowedFormats-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateAllowedFormats")
	public  void updateAllowedFormats () {
		Explorer.EditMask::EditMaskConstSet editMask = (Explorer.EditMask::EditMaskConstSet)allowedFormats.getEditMask();


		Explorer.Meta::EnumItems items = editMask.getRadEnumPresentationDef(getApplication()).getEmptyItems();
		for(ReportExportFormat f : supportedFormats.Value){
		    items.addItem(f);
		}

		editMask.setItems(items);



	}

	/*Radix::Reports::ReportPub:General:Model:updateEncodingsList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateEncodingsList")
	public  void updateEncodingsList () {
		Explorer.EditMask::EditMaskList editMask = (Explorer.EditMask::EditMaskList)encoding.getEditMask();

		final java.util.Map<String, java.nio.charset.Charset> allCharsets = java.nio.charset.Charset.availableCharsets();

		editMask.clearItems();
		for (java.util.Map.Entry<String, java.nio.charset.Charset> entry : allCharsets.entrySet()) {
		    editMask.addItem(entry.getValue().displayName(), entry.getKey());     
		}



	}

	/*Radix::Reports::ReportPub:General:Model:exportReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:exportReport")
	public published  void exportReport (org.radixware.kernel.common.client.models.items.Command exportReportCmd, Str fileName, org.radixware.kernel.common.enums.EReportExportFormat type) {
		try {
		    final Explorer.Models::EntityModel parentModel = (Explorer.Models::EntityModel) exportReportCmd.Owner;
		    final Explorer.Models::EntityModel contextEntityModel
		            = (exportReportCmd.Id
		            != idof[ReportPub:ExportReport]
		                    ? parentModel
		                    : null);

		    final ReportsXsd:GenerateReportRqDocument generateReportRequestDoc = ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		    final ReportsXsd:GenerateReportRqDocument.GenerateReportRq xGenerateReport = generateReportRequestDoc.addNewGenerateReportRq();

		    xGenerateReport.File = fileName;
		    xGenerateReport.Type = type.Value;

		    if (!inputReportParams(contextEntityModel, xGenerateReport, parentModel)) {
		        return;
		    }

		    final java.io.File currFile = new java.io.File(fileName);
		    currFile.deleteOnExit();
		    xGenerateReport.setFile(currFile.getAbsolutePath());
		    xGenerateReport.setReportPubPid(this.getPid().toString());

		    Environment.EasSession.executeCommand(exportReportCmd.Owner, null, exportReportCmd.Id, null, generateReportRequestDoc);
		    Environment.getTracer().debug("Report saved in '" + currFile.getAbsolutePath() + "'");

		    if (Environment.getApplication().RuntimeEnvironmentType == org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB) {
		        try {
		            java.io.File file = new java.io.File(fileName);
		            Common::MimeType mt = Utils::Mime.getMimeTypeForExt(org.radixware.kernel.common.utils.FileUtils.getFileExt(file));
		            String mimeType = mt == null ? "" : mt.Value;
		            ReportsExplorerUtils.printOrOpenFile(Environment, currFile, false, mimeType, suggestFileName(), true);
		        } catch (Exceptions::Throwable ex) {
		            Environment.messageException("Error", "Unable to open generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + fileName + "'.", ex);
		        }
		    }

		} catch (Exceptions::InterruptedException ex) {
		    return; // cancelled
		} catch (Exceptions::Exception ex) {
		    Environment.processException(ex);
		}

	}

	/*Radix::Reports::ReportPub:General:Model:exportToFile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:exportToFile")
	public  void exportToFile (org.radixware.schemas.reports.GenerateReportRqDocument doc) {
		docForExport = doc;
		 this.getCommand(idof[ReportPub:ExportReport]).execute();
	}

	/*Radix::Reports::ReportPub:General:Model:generateReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:generateReport")
	public published  void generateReport (org.radixware.kernel.common.client.models.items.Command generateReportCmd, Str fileName, org.radixware.kernel.common.enums.EReportExportFormat exportType) {
		try {
		    final Explorer.Models::EntityModel parentEntityModel = (Explorer.Models::EntityModel) generateReportCmd.Owner;
		    final Explorer.Models::EntityModel contextEntityModel
		            = (generateReportCmd.Id != idof[ReportPub.Contextless:GenerateReport]
		                    ? parentEntityModel
		                    : null);

		    final ReportsXsd:GenerateReportRqDocument generateReportRequestDoc = ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		    final ReportsXsd:GenerateReportRqDocument.GenerateReportRq xGenerateReport = generateReportRequestDoc.addNewGenerateReportRq();

		    if (!inputReportParams(contextEntityModel, xGenerateReport, parentEntityModel)) {
		        return;
		    }

		    xGenerateReport.setReportPubPid(this.getPid().toString());

		    if (fileName != null && exportType != null) {
		        xGenerateReport.File = fileName;
		        if (exportType != ReportExportFormat:PDF) {//server consider null as PDF
		            xGenerateReport.Type = exportType.Value;
		        }
		        Environment.EasSession.executeCommand(generateReportCmd.Owner, null, generateReportCmd.Id, null, generateReportRequestDoc);
		        if (Environment.getApplication().RuntimeEnvironmentType == org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB) {
		            try {

		                Common::MimeType mt = Utils::Mime.getMimeTypeForExt(org.radixware.kernel.common.utils.FileUtils.getFileExt(new java.io.File(fileName)));
		                String mimeType = mt == null ? "" : mt.Value;
		                ReportsExplorerUtils.printOrOpenFile(Environment, new java.io.File(fileName), false, mimeType, suggestFileName(), true);
		            } catch (Exceptions::Throwable ex) {
		                Environment.messageException("Error", "Unable to open generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + fileName + "'.", ex);
		            }
		        }
		    } else {
		        final java.io.File tempFile = createTmpPdfFile(isMultyFile.Value == Boolean.TRUE);
		        if (isMultyFile.Value == Boolean.TRUE) {
		            xGenerateReport.setFile(tempFile.getAbsolutePath());
		        }

		        xGenerateReport.setFile(tempFile.AbsolutePath);

		        Environment.EasSession.executeCommand(generateReportCmd.Owner, null, generateReportCmd.Id, null, generateReportRequestDoc);
		        Environment.getTracer().debug("Report saved in '" + tempFile.AbsolutePath + "'");

		        boolean print = this.printOnly.Value.booleanValue();
		        try {
		            Str mimeType;
		            if (exportType != null) {
		                switch (exportType) {
		                    case ReportExportFormat:CSV:
		                        mimeType = Common::MimeType:CSV.Value;
		                        break;
		                    case ReportExportFormat:XML:
		                        mimeType = Common::MimeType:XML.Value;
		                        break;
		                    case ReportExportFormat:PDF:
		                        mimeType = Common::MimeType:Pdf.Value;
		                        break;
		                    case ReportExportFormat:XLS:
		                        mimeType = Common::MimeType:MsOfficeSpreadsheet.Value;
		                        break;
		                    case ReportExportFormat:XLSX:
		                        mimeType = Common::MimeType:MsOfficeSpreadsheetX.Value;
		                        break;
		                    default:
		                        mimeType = Common::MimeType:PlainText.Value;
		                }
		            } else {
		                mimeType = Common::MimeType:Pdf.Value;
		            }
		            ReportsExplorerUtils.printOrOpenFile(Environment, tempFile, print, mimeType, suggestFileName(), true);
		        } catch (Exceptions::Throwable ex) {
		            Environment.messageException("Error", "Unable to " + (print ? "print" : "open") + " generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + tempFile.getAbsolutePath() + "'.", ex);
		        }

		    }
		} catch (Exceptions::InterruptedException ex) {
		    return; // cancelled
		} catch (Exceptions::Exception ex) {
		    Environment.processException(ex);
		}

	}

	/*Radix::Reports::ReportPub:General:Model:generateReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:generateReport")
	public published  boolean generateReport (org.radixware.kernel.common.client.models.items.Command generateReportCmd, Str exportDirectory, Str defaultFileName, org.radixware.ads.Reports.common.ReportExportFormat.Arr exportFormats, boolean isOpenPdf) {
		try {
		    final Explorer.Models::EntityModel parentEntityModel = (Explorer.Models::EntityModel) generateReportCmd.Owner;
		    final Explorer.Models::EntityModel contextEntityModel
		            = (generateReportCmd.Id != idof[ReportPub.Contextless:GenerateReport]
		                    ? parentEntityModel
		                    : null);

		    final ReportsXsd:GenerateReportRqDocument generateReportRequestDoc = ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		    final ReportsXsd:GenerateReportRqDocument.GenerateReportRq xGenerateReport = generateReportRequestDoc.addNewGenerateReportRq();

		    if (!inputReportParams(contextEntityModel, xGenerateReport, parentEntityModel)) {
		        return false;
		    }

		    xGenerateReport.setReportPubPid(this.getPid().toString());

		    if (exportDirectory != null && exportFormats != null) {
		        xGenerateReport.File = exportDirectory;
		        xGenerateReport.DefaultFileName = defaultFileName;

		        ReportsXsd:GenerateReportRqDocument.GenerateReportRq.FormatDirectories xFormatDirectories = xGenerateReport.addNewFormatDirectories();
		        ReportsXsd:GenerateReportRqDocument.GenerateReportRq.FormatDirectories.FormatDirectory xFormatDirectory = xFormatDirectories.addNewFormatDirectory();

		        java.io.File tmpPdfDir = createTmpPdfFile(true);

		        xFormatDirectory.Format = ReportExportFormat:PDF;
		        xFormatDirectory.Path = tmpPdfDir.getPath();

		        boolean wasPdf = true;
		        if (isOpenPdf && !exportFormats.contains(ReportExportFormat:PDF)) {
		            exportFormats.add(ReportExportFormat:PDF);
		            wasPdf = false;
		        }

		        ReportsXsd:GenerateReportRqDocument.GenerateReportRq.ExportFormats xExportFormats = xGenerateReport.addNewExportFormats();
		        xExportFormats.getExportFormatList().addAll(exportFormats);

		        Environment.EasSession.executeCommand(generateReportCmd.Owner, null, generateReportCmd.Id, null, generateReportRequestDoc);

		        boolean isOnWeb = Environment.getApplication().RuntimeEnvironmentType == org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB;

		        java.io.File openFile;
		        if (tmpPdfDir.listFiles() != null && tmpPdfDir.listFiles().length > 0) {
		            if (wasPdf) {
		                if (!isMultyFile.Value.booleanValue()) {
		                    openFile = new java.io.File(exportDirectory, tmpPdfDir.listFiles()[0].getName());
		                    java.nio.file.Files.move(tmpPdfDir.listFiles()[0].toPath(), openFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		                } else {
		                    for (java.io.File pdfFile : tmpPdfDir.listFiles()) {
		                        openFile = new java.io.File(exportDirectory, pdfFile.getName());
		                        java.nio.file.Files.move(tmpPdfDir.listFiles()[0].toPath(), openFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		                    }
		                    openFile = new java.io.File(exportDirectory);
		                }
		                org.radixware.kernel.common.utils.FileUtils.deleteDirectory(tmpPdfDir);
		            } else {
		                if (!isMultyFile.Value.booleanValue()) {
		                    openFile = tmpPdfDir.listFiles()[0];
		                } else {
		                    openFile = tmpPdfDir;
		                }
		            }
		        } else {
		            org.radixware.kernel.common.utils.FileUtils.deleteDirectory(tmpPdfDir);
		            openFile = new java.io.File(exportDirectory);
		        }

		        if (isOnWeb || isOpenPdf) {
		            try {
		                Common::MimeType mt = Utils::Mime.getMimeTypeForExt(exportFormats.get(0) == ReportExportFormat:XLS ? "xls" : exportFormats.get(0).getExt());
		                String mimeType = mt == null ? "" : mt.Value;

		                if (isOnWeb) {
		                    java.io.File exportDir = new java.io.File(exportDirectory);
		                    if (exportDir.listFiles() != null && exportDir.listFiles().length == 1) {
		                        openFile = exportDir.listFiles()[0];
		                    } else if (exportDir.listFiles() != null) {
		                        openFile = exportDir;
		                    }
		                }

		                ReportsExplorerUtils.printOrOpenFile(Environment, openFile, false, mimeType, defaultFileName == null ? suggestFileName() : defaultFileName, true);
		            } catch (Exceptions::Throwable ex) {
		                Environment.messageException("Error", "Unable to open generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + exportDirectory + "'.", ex);
		            }
		        }
		    } else {
		        final java.io.File tempFile = createTmpPdfFile(isMultyFile.Value == Boolean.TRUE);
		        if (isMultyFile.Value == Boolean.TRUE) {
		            xGenerateReport.setFile(tempFile.getAbsolutePath());
		        }
		        xGenerateReport.setFile(tempFile.AbsolutePath);

		        Environment.EasSession.executeCommand(generateReportCmd.Owner, null, generateReportCmd.Id, null, generateReportRequestDoc);
		        Environment.getTracer().debug("Report saved in '" + tempFile.AbsolutePath + "'");

		        boolean print = this.printOnly.Value.booleanValue();
		        try {
		            Str mimeType;
		            if (exportFormats != null) {
		                switch (exportFormats.get(0)) {
		                    case ReportExportFormat:CSV:
		                        mimeType = Common::MimeType:CSV.Value;
		                        break;
		                    case ReportExportFormat:XML:
		                        mimeType = Common::MimeType:XML.Value;
		                        break;
		                    case ReportExportFormat:PDF:
		                        mimeType = Common::MimeType:Pdf.Value;
		                        break;
		                    case ReportExportFormat:XLS:
		                        mimeType = Common::MimeType:MsOfficeSpreadsheet.Value;
		                        break;
		                    case ReportExportFormat:XLSX:
		                        mimeType = Common::MimeType:MsOfficeSpreadsheetX.Value;
		                        break;
		                    default:
		                        mimeType = Common::MimeType:PlainText.Value;
		                }
		            } else {
		                mimeType = Common::MimeType:Pdf.Value;
		            }
		            ReportsExplorerUtils.printOrOpenFile(Environment, tempFile, print, mimeType, suggestFileName(), true);
		        } catch (Exceptions::Throwable ex) {
		            Environment.messageException("Error", "Unable to " + (print ? "print" : "open") + " generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + tempFile.getAbsolutePath() + "'.", ex);
		            return false;
		        }

		    }
		} catch (Exceptions::InterruptedException ex) {
		    return false; // cancelled
		} catch (Exceptions::Exception ex) {
		    Environment.processException(ex);
		    return false;
		}

		return true;

	}

	/*Radix::Reports::ReportPub:General:Model:getRolesIdsProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:getRolesIdsProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyArrStr getRolesIdsProp (org.radixware.kernel.common.types.Id roleTitlePropId) {
		return userRoleGuids;

	}

	/*Radix::Reports::ReportPub:General:Model:getRolesTitlesProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:getRolesTitlesProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRolesTitlesProp (org.radixware.kernel.common.types.Id editedRolePropId) {
		return userRoles;
	}

	/*Radix::Reports::ReportPub:General:Model:saveReportAsXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:saveReportAsXml")
	public published  void saveReportAsXml (org.radixware.kernel.common.client.models.items.Command saveReportAsXmlCmd, Str fileName) {
		try {
		    final Explorer.Models::EntityModel parentModel=(Explorer.Models::EntityModel) saveReportAsXmlCmd.Owner;
		    final Explorer.Models::EntityModel contextEntityModel =
		            (saveReportAsXmlCmd.Id != idof[ReportPub.Contextless:GenerateReport]
		            ? parentModel
		            : null);
		    
		    final ReportsXsd:GenerateReportRqDocument generateReportRequestDoc = ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		    final ReportsXsd:GenerateReportRqDocument.GenerateReportRq xGenerateReport = generateReportRequestDoc.addNewGenerateReportRq();

		    if (!inputReportParams(contextEntityModel, xGenerateReport, parentModel)) {
		        return;
		    }

		    //final String fileExt = this..Value.getExt();    
		    final java.io.File file = new java.io.File(fileName);    
		    file.deleteOnExit();
		    xGenerateReport.setFile(file.getAbsolutePath());
		    xGenerateReport.setReportPubPid(this.getPid().toString());

		    Environment.EasSession.executeCommand(saveReportAsXmlCmd.Owner, null, saveReportAsXmlCmd.Id, null, generateReportRequestDoc);
		    Explorer.Utils::Trace.debug(getEnvironment(), "Report saved in '" + file.getAbsolutePath() + "'");

		    boolean print = this.printOnly.Value.booleanValue();
		    try {
		        if (print) {
		            java.awt.Desktop.getDesktop().print(file);
		        } else {
		            java.awt.Desktop.getDesktop().open(file);
		        }
		    } catch (Exceptions::Throwable ex) {
		        Explorer.Env::Application.messageException("Error", "Unable to " + (print ? "print" : "open") + " generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + file.getAbsolutePath() + "'.", ex);
		    }
		} catch (Exceptions::InterruptedException ex) {
		    return; // cancelled
		} catch (Exceptions::Exception ex) {
		    Explorer.Env::Application.processException(ex);
		}

	}

	/*Radix::Reports::ReportPub:General:Model:updateParametersBinding-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateParametersBinding")
	public  void updateParametersBinding () {
		if (getEditorPage(idof[ReportPub:General:Parameters]).getView() == null) {
		    return; // not opened yet.
		}
		ReportParametersBindingWidget editor = ReportPub:General:Parameters:View:EditorPageView:object;
		ReportParametersBindingWidget:Model model = editor.Model;
		model.parameters = new java.util.ArrayList<Explorer.Models.Properties::Property>();
		model.parametersBindingDoc = null;
		model.contextParameterId = null;

		if (reportClassId != null) {
		    final Explorer.Meta::ReportPresentationDef reportPresentationDef = Environment.DefManager.getReportPresentationDef(reportClassId);
		    final java.util.List<Types::Id> propIds = reportPresentationDef.getPropertyIds();

		    final Explorer.Models::Model reportModel = reportPresentationDef.createModel(new ReportParametersContext(this, this));
		    for (Types::Id propId : propIds) {
		        final Explorer.Meta::PropertyDef propDef = reportPresentationDef.getPropertyDefById(propId);
		        if (propDef.getNature() == Meta::PropNature:SqlClassParameter) {
		            Explorer.Models.Properties::Property param = reportModel.getProperty(propId);
		            if (propId.equals(reportPresentationDef.ContextParameterId)) {

		                if (contextObjectClassGuid.Value != null && pubContextPid.Value != null && pubContextClassGuid.Value != null) {
		                    Types::Id contextObjectClassId = Types::Id.Factory.loadFrom(contextObjectClassGuid.Value);
		                    Types::Id pubContextClassId = Types::Id.Factory.loadFrom(pubContextClassGuid.Value);

		                    if (contextObjectClassId == pubContextClassId || getEnvironment().getDefManager().getRepository().isClassExtends(pubContextClassId, contextObjectClassId)) {
		                        try {
		                            Explorer.Types::Pid pid = new Pid(contextObjectClassId, pubContextPid.Value, getEnvironment().getDefManager());
		                            Explorer.Types::Reference ref = new Reference(pid, contextTitle.Value);
		                            try {
		                                param.setValueObject(ref);
		                            } catch (org.radixware.kernel.common.client.errors.ParentRefSetterError e) {
		                                //().(e); ignore
		                            }
		                        } catch (IllegalArgumentException e) {
		                            getEnvironment().processException(e);
		                        }
		                    }
		                }
		            }
		            model.parameters.add(param);
		        }
		    }

		    if (paramBinding.Value != null) {
		        try {
		            model.parametersBindingDoc = ReportsXsd:ParametersBindingDocument.Factory.parse(paramBinding.Value);
		        } catch (org.apache.xmlbeans.XmlException ex) {
		            Explorer.Env::Application.processException(ex);
		        }
		    }

		    if (this.contextObjectClassGuid.Value != null) {
		        model.contextParameterId = reportPresentationDef.ContextParameterId;
		    }
		}

		model.init();

	}

	/*Radix::Reports::ReportPub:General:Model:updateColumnsSettings-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateColumnsSettings")
	public  void updateColumnsSettings () {
		if (!isEditorPageExists(idof[ReportPub:General:Columns]) || getEditorPage(idof[ReportPub:General:Columns]).getView() == null || !getEditorPage(idof[ReportPub:General:Columns]).isVisible()) {
		    return;
		}

		ReportColumnsSettingWidget editor = ReportPub:General:Columns:View:ReportColumnsView:columnsSettingsEditor;
		ReportColumnsSettingWidget:Model model = editor.Model;

		if (reportClassId != null) {
		    model.reportId = this.reportClassId;

		    if (columnsSettings.Value != null && !columnsSettings.Value.isEmpty()) {
		        try {
		            ReportsXsd:ColumnSettingsDocument xColumnSettingsDocument = ReportsXsd:ColumnSettingsDocument.Factory.parse(columnsSettings.Value);
		            model.columnsSettingsDocument = xColumnSettingsDocument;
		        } catch (Exceptions::Exception ex) {
		            Environment.processException(ex);            
		        }
		    } else {
		        model.columnsSettingsDocument = null;
		    }
		}

		model.init();
	}

	/*Radix::Reports::ReportPub:General:Model:EditorPageView_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:EditorPageView_opened")
	public  void EditorPageView_opened (com.trolltech.qt.gui.QWidget widget) {
		updateParametersBinding();
	}

	/*Radix::Reports::ReportPub:General:Model:ReportColumnsView_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:ReportColumnsView_opened")
	public  void ReportColumnsView_opened (com.trolltech.qt.gui.QWidget widget) {
		updateColumnsSettings();
	}

	/*Radix::Reports::ReportPub:General:Model:columnsSettingsEditor_changed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:columnsSettingsEditor_changed")
	public  void columnsSettingsEditor_changed () {
		ReportColumnsSettingWidget editor = ReportPub:General:Columns:View:ReportColumnsView:columnsSettingsEditor;
		ReportColumnsSettingWidget:Model model = editor.Model;
		columnsSettings.Value = (model.columnsSettingsDocument != null ? model.columnsSettingsDocument.xmlText() : null);
	}

	/*Radix::Reports::ReportPub:General:Model:paramBindingEditor_changed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:paramBindingEditor_changed")
	public  void paramBindingEditor_changed () {
		ReportParametersBindingWidget editor = ReportPub:General:Parameters:View:EditorPageView:object;
		ReportParametersBindingWidget:Model model = editor.Model;
		paramBinding.Value = (model.parametersBindingDoc != null ? model.parametersBindingDoc.xmlText() : null);

	}

	/*Radix::Reports::ReportPub:General:Model:updateColumnsPageVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateColumnsPageVisibility")
	private final  void updateColumnsPageVisibility () {
		if (!isEditorPageExists(idof[ReportPub:General:Columns])) {
		    return;
		}

		if (reportClassGuid.Value == null) {
		    getEditorPage(idof[ReportPub:General:Columns]).setVisible(false);
		    return;
		}

		Arr<ReportExportFormat> formats;
		if (allowedFormats.Value != null) {
		    formats = allowedFormats.Value;
		} else if (!supportedFormats.Value.isEmpty()) {
		    formats = supportedFormats.Value;
		} else {
		    formats = new Arr<ReportExportFormat>();
		    formats.add(ReportExportFormat:PDF);
		}

		if (!formats.contains(ReportExportFormat:PDF)) {
		    if (formats.contains(ReportExportFormat:XLSX) || formats.contains(ReportExportFormat:CSV)) {
		        getEditorPage(idof[ReportPub:General:Columns]).setVisible(true);
		    } else {
		        getEditorPage(idof[ReportPub:General:Columns]).setVisible(false);
		    }
		} else if (ReportsExplorerUtils.isAnyReportCellAssotiateToColumn(getEnvironment(), reportClassId)) {
		    getEditorPage(idof[ReportPub:General:Columns]).setVisible(true);
		} else {
		    getEditorPage(idof[ReportPub:General:Columns]).setVisible(false);
		}
	}

	/*Radix::Reports::ReportPub:General:Model:cancelChanges-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:cancelChanges")
	public published  void cancelChanges () {
		super.cancelChanges();
		setVisibility();
	}
	public final class ChooseReport extends org.radixware.ads.Reports.explorer.ReportPub.ChooseReport{
		protected ChooseReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ChooseReport( this, propertyId );
		}

	}

	public final class ExportReport extends org.radixware.ads.Reports.explorer.ReportPub.ExportReport{
		protected ExportReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ExportReport( this );
		}

	}

























}

/* Radix::Reports::ReportPub:General:Model - Desktop Meta*/

/*Radix::Reports::ReportPub:General:Model-Entity Model Class*/

package org.radixware.ads.Reports.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFP5NARHOA5E7LNMKWHBCS5Y3FM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Reports::ReportPub:General:Model:Properties-Properties*/
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

/* Radix::Reports::ReportPub:General:Model - Web Executable*/

/*Radix::Reports::ReportPub:General:Model-Entity Model Class*/

package org.radixware.ads.Reports.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model")
public class General:Model  extends org.radixware.ads.Reports.web.ReportPub.ReportPub_DefaultModel implements org.radixware.ads.Acs.common_client.IArrRoleHolder  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Reports::ReportPub:General:Model:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportPub:General:Model:Properties-Properties*/

	/*Radix::Reports::ReportPub:General:Model:reportClassId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id reportClassId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:reportClassId")
	public published  org.radixware.kernel.common.types.Id getReportClassId() {

		return Types::Id.Factory.loadFrom(reportClassGuid.Value);

	}

	/*Radix::Reports::ReportPub:General:Model:docForExport-Dynamic Property*/



	protected org.radixware.schemas.reports.GenerateReportRqDocument docForExport=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:docForExport")
	public  org.radixware.schemas.reports.GenerateReportRqDocument getDocForExport() {
		return docForExport;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:docForExport")
	public   void setDocForExport(org.radixware.schemas.reports.GenerateReportRqDocument val) {
		docForExport = val;
	}

	/*Radix::Reports::ReportPub:General:Model:allowedFormats-Presentation Property*/




	public class AllowedFormats extends org.radixware.ads.Reports.web.ReportPub.pruFPX3ORINZ5ADVJBS6BMJPBRPLA{
		public AllowedFormats(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Reports.common.ReportExportFormat.Arr> getValClass(){
			return org.radixware.ads.Reports.common.ReportExportFormat.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Reports.common.ReportExportFormat.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Reports.common.ReportExportFormat.Arr ? (org.radixware.ads.Reports.common.ReportExportFormat.Arr)x : new org.radixware.ads.Reports.common.ReportExportFormat.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:allowedFormats")
		public  org.radixware.ads.Reports.common.ReportExportFormat.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:allowedFormats")
		public   void setValue(org.radixware.ads.Reports.common.ReportExportFormat.Arr val) {

			internal[allowedFormats] = val;
			setVisibility();
		}
	}
	public AllowedFormats getAllowedFormats(){return (AllowedFormats)getProperty(pruFPX3ORINZ5ADVJBS6BMJPBRPLA);}

	/*Radix::Reports::ReportPub:General:Model:reportFullClassName-Presentation Property*/




	public class ReportFullClassName extends org.radixware.ads.Reports.web.ReportPub.prdFD67H7DF35E6DHJIDDOFGPQD4E{
		public ReportFullClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:reportFullClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:reportFullClassName")
		public   void setValue(Str val) {

			boolean isRemoveColumnsSettings = !java.util.Objects.equals(internal[reportFullClassName], val);

			internal[reportFullClassName] = val;
			//if (val == null) {
			//    ().(false);
			//} else {
			//    ().(true);
			//}
			updateColumnsPageVisibility();

			columnsSettings.Value = isRemoveColumnsSettings ? null : columnsSettings.Value;
		}
	}
	public ReportFullClassName getReportFullClassName(){return (ReportFullClassName)getProperty(prdFD67H7DF35E6DHJIDDOFGPQD4E);}










	/*Radix::Reports::ReportPub:General:Model:Methods-Methods*/

	/*Radix::Reports::ReportPub:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();

		final boolean isInherited = this.inheritedPubId.Value != null;
		// ArrayList - concurent modification exception otherwise
		final java.util.List<Explorer.Models.Properties::Property> props = new java.util.ArrayList<Explorer.Models.Properties::Property>(this.getActiveProperties());
		for (Explorer.Models.Properties::Property prop : props) {
		    if (prop == this.id || prop == this.seq) {
		        // nothing
		    } else if (prop == this.inheritedPub || prop == this.inheritedPubLocation) {
		        prop.setVisible(isInherited);
		    } else if (prop == this.allowedFormats) {
		        prop.setVisible(!isNew() && !isInherited);
		    } else {
		        prop.setVisible(!isInherited);
		    }
		}

		reportClassTitle.setMandatory(!isInherited); // RADIX-4471
		try {
		    this.getEditorPage(idof[ReportPub:General:Parameters]).setVisible(!isInherited);
		} catch (Explorer.Exceptions::NoDefinitionWithSuchIdError e) {
		    return;//illegal usage
		}

		updateEncodingsList();
		updateAllowedFormats();
		setVisibility();
		/* презентации в производных классах перекрываются и функции добавляются явно
		final String CONDITION = "condition";
		if (!isInherited && () && this.().isPropertyDefExistsByName(CONDITION)) {
		    final  conditionPropId = this.().(CONDITION).getId();
		    final  editorPageId = ;
		    this.(editorPageId).(conditionPropId);
		}
		*/
	}

	/*Radix::Reports::ReportPub:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		updateParametersBinding();
		updateColumnsSettings();
	}

	/*Radix::Reports::ReportPub:General:Model:onCommand_ExportReport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:onCommand_ExportReport")
	public  void onCommand_ExportReport (org.radixware.ads.Reports.web.ReportPub.ExportReport command) {
		try{
		  ReportsXsd:GenerateReportRqDocument xIn = docForExport;
		  command.send(xIn);
		}catch(Exceptions::InterruptedException  e){
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::Reports::ReportPub:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		try {
		    if (getEditorPage(idof[ReportPub:General:Parameters]).getView() != null) {
		        ReportParametersBindingWidgetWeb editor = ReportPub:General:Parameters:WebView:widget:paramBindingEditor;
		        ReportParametersBindingWidgetWeb:Model model = editor.Model;
		        model.finishEdit();
		    }
		} catch (Explorer.Exceptions::NoDefinitionWithSuchIdError e) {
		    //illegal usage
		}
		return super.beforeUpdate();
	}

	/*Radix::Reports::ReportPub:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		try {
		    if (getEditorPage(idof[ReportPub:General:Parameters]).getView() != null) {
		        ReportParametersBindingWidgetWeb editor = ReportPub:General:Parameters:WebView:widget:paramBindingEditor;
		        ReportParametersBindingWidgetWeb:Model model = editor.Model;
		        model.finishEdit();
		    }
		} catch (Explorer.Exceptions::NoDefinitionWithSuchIdError e) {
		    //illegal usage
		}

		// заполнение bind для фиксации порядка параметров, который используется в Radix::Reports::ReportPubList:buildTreeListForCustomer
		if (paramBinding.Value == null && reportClassId != null) {
		    ReportsXsd:ParametersBindingDocument bind = ReportsXsd:ParametersBindingDocument.Factory.newInstance();
		    bind.addNewParametersBinding();
		    final Explorer.Meta::ReportPresentationDef reportPresentationDef = Environment.DefManager.getReportPresentationDef(reportClassId);
		    final java.util.List<Types::Id> propIds = reportPresentationDef.getPropertyIds();
		    for (Types::Id propId : propIds) {
		        final Explorer.Meta::PropertyDef propDef = reportPresentationDef.getPropertyDefById(propId);
		        if (propDef.getNature() == Meta::PropNature:SqlClassParameter && propId != reportPresentationDef.ContextParameterId) {
		            ReportsXsd:ParametersBindingType.ParameterBinding b = bind.ParametersBinding.addNewParameterBinding();
		            b.ParameterId = propId;
		            b.Restriction = ReportParameterRestriction:NONE;
		            b.Type = ReportParameterBindingType:Value;
		            if (propDef.getInitialVal() != null) {
		                Client.Utils::ValueConverter.objVal2EasPropXmlVal(propDef.getInitialVal().toObject(propDef.getType()), propDef.getType(), b.addNewValue());
		            }
		        }
		    }
		    paramBinding.Value = bind.xmlText();
		}



		if (inheritedPubId != null && inheritedPubId.Value != null) {
		    reportFullClassName.setMandatory(false);
		}

		return super.beforeCreate();
	}

	/*Radix::Reports::ReportPub:General:Model:createTmpPdfFile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:createTmpPdfFile")
	private final  java.io.File createTmpPdfFile (boolean isDir) throws java.io.IOException {
		final java.io.File tempFile;
		if (isDir) {
		    tempFile = java.io.File.createTempFile("Report-", "Dir");
		    tempFile.delete();
		    tempFile.mkdirs();
		    tempFile.deleteOnExit();
		} else {
		    tempFile = java.io.File.createTempFile("Report-", ".pdf");
		    tempFile.deleteOnExit();    
		}

		return tempFile;
	}

	/*Radix::Reports::ReportPub:General:Model:canSafelyClean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:canSafelyClean")
	public published  boolean canSafelyClean (org.radixware.kernel.common.client.models.CleanModelController controller) {
		try {
		    if (getEditorPage(idof[ReportPub:General:Parameters]).getView() != null) {
		        ReportParametersBindingWidgetWeb editor = ReportPub:General:Parameters:WebView:widget:paramBindingEditor;
		        ReportParametersBindingWidgetWeb:Model model = editor.Model;
		        model.canSafelyClean(controller);
		    }
		} catch (Explorer.Exceptions::NoDefinitionWithSuchIdError e) {
		  //illegal usage
		}
		return super.canSafelyClean(controller);
	}

	/*Radix::Reports::ReportPub:General:Model:inputReportParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:inputReportParams")
	public  boolean inputReportParams (org.radixware.kernel.common.client.models.EntityModel contextEntityModel, org.radixware.schemas.reports.GenerateReportRqDocument.GenerateReportRq request, org.radixware.kernel.common.client.models.EntityModel parentEntityModel) {
		Explorer.Models::ReportParamDialogModel paramDialogModel
		        = Explorer.Models::ReportParamDialogModel.create(reportClassId, this, parentEntityModel);

		final Explorer.Meta::ReportPresentationDef reportPresentationDef = paramDialogModel.getReportPresentationDef();
		final java.util.List<Types::Id> propIds = reportPresentationDef.getPropertyIds();
		boolean paramExists = false;

		ReportsXsd:ParametersBindingDocument parametersBindingDoc = null;
		if (paramBinding.Value != null) {
		    try {
		        parametersBindingDoc = ReportsXsd:ParametersBindingDocument.Factory.parse(paramBinding.Value);
		    } catch (Exceptions::Exception ex) {
		        Environment.processException(ex);
		        return false;
		    }
		}

		for (Types::Id propId : propIds) {
		    final Explorer.Meta::PropertyDef propDef = reportPresentationDef.getPropertyDefById(propId);
		    if (propDef.getNature() == Meta::PropNature:SqlClassParameter) {
		        final Explorer.Models.Properties::Property param = paramDialogModel.getProperty(propId);
		        boolean isFinal = false;
		        boolean isVisible = true;
		        try {
		            if (param.Id == reportPresentationDef.ContextParameterId && contextEntityModel != null) {
		                paramDialogModel.setReportContextModel(contextEntityModel);
		                Explorer.Models.Properties::PropertyValue propVal = new PropertyValue(propDef, new Reference(contextEntityModel.getPid(), contextEntityModel.getTitle()));
		                param.setServerValue(propVal);
		                isFinal = true;
		            } else {
		                final ReportsXsd:ParametersBindingType.ParameterBinding parameterBinding = ReportsExplorerUtils.findParameterBindingById(parametersBindingDoc, propId);
		                if (parameterBinding != null) {
		                    Object value = Explorer.Utils::PropUtils.getEasPropValue(param, parameterBinding.Value);
		                    if (param instanceof Explorer.Models.Properties::PropertyArrRef) {
		                        value = ((Explorer.Models.Properties::PropertyArrRef) param).updateTitles((Explorer.Types::ArrReference) value);
		                    } else if (param instanceof Explorer.Models.Properties::PropertyRef) {
		                       /* org.radixware.kernel.common.client.meta.RadParentRefPropertyDef pDef = (org.radixware.kernel.common.client.meta.RadParentRefPropertyDef) param.();
		                         tableId = pDef.();
		                        final  presentationId;
		                        if (pDef.() == null) {
		                            presentationId = null;
		                        } else {
		                            presentationId = pDef.().getId();
		                        }
		                        final  titlesProvider
		                                = new (param.(), tableId, presentationId);
		                        titlesProvider.(() value);
		                        final  titles = titlesProvider.getTitles();
		                        value = titles.((() value).());*/
		                        value = ((Explorer.Models.Properties::PropertyRef) param).updateTitle((Explorer.Types::Reference) value);
		                    }
		                    Explorer.Models.Properties::PropertyValue propVal = new PropertyValue(propDef, value);
		                    param.setServerValue(propVal);
		                    //.(param, parameterBinding.Value);                   
		                    if (param.Id == reportPresentationDef.ContextParameterId) {
		                        Explorer.Meta::ParentRefPropertyDef propParentRef = (Explorer.Meta::ParentRefPropertyDef) param.getDefinition();
		                        Explorer.Types::Reference ref = (Explorer.Types::Reference) param.getValueObject();
		                        Explorer.Meta::PresentationClassDef classDef = getEnvironment().getDefManager().getClassPresentationDef(propParentRef.getReferencedClassId());
		                        EntityModel model = Explorer.Models::EntityModel.openContextlessModel(getEnvironment(), ref.getPid(), propParentRef.getReferencedClassId(), classDef.getEditorPresentationIds());
		                        paramDialogModel.setReportContextModel(model);
		                    }
		                    if (parameterBinding.isSetFinal()) {
		                        isFinal = parameterBinding.Final;
		                    } else if (parameterBinding.isSetRestriction()) {
		                        isFinal = parameterBinding.Restriction == ReportParameterRestriction:CONST;
		                        isVisible = !(parameterBinding.Restriction == ReportParameterRestriction:HIDDEN);
		                    }
		                }
		            }
		        } catch (ClassCastException ex) {
		            final String message = Str.format("Type of '%s' parameter value does not match the one specified in parameters binding....", param.getTitle());
		            Environment.messageWarning(message);
		        } catch (Exceptions::InterruptedException ex) {
		        } catch (Exceptions::ServiceClientException ex) {
		            Environment.processException(ex);
		        }

		        param.setReadonly(isFinal);
		        param.setVisible(isVisible);
		        if (!isFinal && isVisible)
		            paramExists = true;
		    }
		}

		if (paramExists)
		    try {
		        ReportsXsd:GenerateReportRqDocument rqDoc = ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		        ReportsXsd:GenerateReportRqDocument.GenerateReportRq rqReport = rqDoc.addNewGenerateReportRq();
		        rqReport.setParamValues(paramDialogModel.writeProperties());
		        Xml out = getEnvironment().EasSession.executeCommand(this, null, idof[ReportPub:BeforeInputParams], null, rqDoc).Output;
		        ReportsXsd:GenerateReportRqDocument rs = ReportsXsd:GenerateReportRqDocument.Factory.parse(out.DomNode.FirstChild);
		        paramDialogModel.readProperties(rs.GenerateReportRq.ParamValues);
		    } catch (Exceptions::Exception ex) {
		        Environment.processException(ex);
		        return false;
		    }

		if (!paramExists || paramDialogModel.execDialog() == org.radixware.kernel.common.client.views.IDialog.DialogResult.ACCEPTED) {
		    request.setParamValues(paramDialogModel.writeProperties());
		    return true;
		} else
		    return false;


	}

	/*Radix::Reports::ReportPub:General:Model:setVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:setVisibility")
	public  void setVisibility () {
		if (allowedFormats.Value != null){
		     encoding.setVisible(allowedFormats.Value.contains(ReportExportFormat:TXT) || allowedFormats.Value.contains(ReportExportFormat:CSV));
		} else {
		    encoding.setVisible(areTextBasedFormatsSupported.Value == Boolean.TRUE);
		}

		updateColumnsPageVisibility();
	}

	/*Radix::Reports::ReportPub:General:Model:suggestFileName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:suggestFileName")
	public  Str suggestFileName () {
		final Explorer.EditMask::EditMaskDateTime mask = new EditMaskDateTime("dd-MM-yy_HH-mm", null, null);
		if (reportClassTitle.getValue()!=null && !reportClassTitle.getValue().isEmpty()){
		    return reportClassTitle.getValue()+"_"+mask.toStr(getEnvironment(), getEnvironment().getCurrentServerTime());
		}else if (title.getValue()!=null && !title.getValue().isEmpty()){
		    return title.getValue()+"_"+mask.toStr(getEnvironment(), getEnvironment().getCurrentServerTime());
		}else{
		    return "report"+"_"+mask.toStr(getEnvironment(), getEnvironment().getCurrentServerTime());
		}
	}

	/*Radix::Reports::ReportPub:General:Model:updateAllowedFormats-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateAllowedFormats")
	public  void updateAllowedFormats () {
		Explorer.EditMask::EditMaskConstSet editMask = (Explorer.EditMask::EditMaskConstSet)allowedFormats.getEditMask();


		Explorer.Meta::EnumItems items = editMask.getRadEnumPresentationDef(getApplication()).getEmptyItems();
		for(ReportExportFormat f : supportedFormats.Value){
		    items.addItem(f);
		}

		editMask.setItems(items);



	}

	/*Radix::Reports::ReportPub:General:Model:updateEncodingsList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateEncodingsList")
	public  void updateEncodingsList () {
		Explorer.EditMask::EditMaskList editMask = (Explorer.EditMask::EditMaskList)encoding.getEditMask();

		final java.util.Map<String, java.nio.charset.Charset> allCharsets = java.nio.charset.Charset.availableCharsets();

		editMask.clearItems();
		for (java.util.Map.Entry<String, java.nio.charset.Charset> entry : allCharsets.entrySet()) {
		    editMask.addItem(entry.getValue().displayName(), entry.getKey());     
		}



	}

	/*Radix::Reports::ReportPub:General:Model:exportReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:exportReport")
	public published  void exportReport (org.radixware.kernel.common.client.models.items.Command exportReportCmd, Str fileName, org.radixware.kernel.common.enums.EReportExportFormat type) {
		try {
		    final Explorer.Models::EntityModel parentModel = (Explorer.Models::EntityModel) exportReportCmd.Owner;
		    final Explorer.Models::EntityModel contextEntityModel
		            = (exportReportCmd.Id
		            != idof[ReportPub:ExportReport]
		                    ? parentModel
		                    : null);

		    final ReportsXsd:GenerateReportRqDocument generateReportRequestDoc = ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		    final ReportsXsd:GenerateReportRqDocument.GenerateReportRq xGenerateReport = generateReportRequestDoc.addNewGenerateReportRq();

		    xGenerateReport.File = fileName;
		    xGenerateReport.Type = type.Value;

		    if (!inputReportParams(contextEntityModel, xGenerateReport, parentModel)) {
		        return;
		    }

		    final java.io.File currFile = new java.io.File(fileName);
		    currFile.deleteOnExit();
		    xGenerateReport.setFile(currFile.getAbsolutePath());
		    xGenerateReport.setReportPubPid(this.getPid().toString());

		    Environment.EasSession.executeCommand(exportReportCmd.Owner, null, exportReportCmd.Id, null, generateReportRequestDoc);
		    Environment.getTracer().debug("Report saved in '" + currFile.getAbsolutePath() + "'");

		    if (Environment.getApplication().RuntimeEnvironmentType == org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB) {
		        try {
		            java.io.File file = new java.io.File(fileName);
		            Common::MimeType mt = Utils::Mime.getMimeTypeForExt(org.radixware.kernel.common.utils.FileUtils.getFileExt(file));
		            String mimeType = mt == null ? "" : mt.Value;
		            ReportsExplorerUtils.printOrOpenFile(Environment, currFile, false, mimeType, suggestFileName(), true);
		        } catch (Exceptions::Throwable ex) {
		            Environment.messageException("Error", "Unable to open generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + fileName + "'.", ex);
		        }
		    }

		} catch (Exceptions::InterruptedException ex) {
		    return; // cancelled
		} catch (Exceptions::Exception ex) {
		    Environment.processException(ex);
		}

	}

	/*Radix::Reports::ReportPub:General:Model:generateReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:generateReport")
	public published  void generateReport (org.radixware.kernel.common.client.models.items.Command generateReportCmd, Str fileName, org.radixware.kernel.common.enums.EReportExportFormat exportType) {
		try {
		    final Explorer.Models::EntityModel parentEntityModel = (Explorer.Models::EntityModel) generateReportCmd.Owner;
		    final Explorer.Models::EntityModel contextEntityModel
		            = (generateReportCmd.Id != idof[ReportPub.Contextless:GenerateReport]
		                    ? parentEntityModel
		                    : null);

		    final ReportsXsd:GenerateReportRqDocument generateReportRequestDoc = ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		    final ReportsXsd:GenerateReportRqDocument.GenerateReportRq xGenerateReport = generateReportRequestDoc.addNewGenerateReportRq();

		    if (!inputReportParams(contextEntityModel, xGenerateReport, parentEntityModel)) {
		        return;
		    }

		    xGenerateReport.setReportPubPid(this.getPid().toString());

		    if (fileName != null && exportType != null) {
		        xGenerateReport.File = fileName;
		        if (exportType != ReportExportFormat:PDF) {//server consider null as PDF
		            xGenerateReport.Type = exportType.Value;
		        }
		        Environment.EasSession.executeCommand(generateReportCmd.Owner, null, generateReportCmd.Id, null, generateReportRequestDoc);
		        if (Environment.getApplication().RuntimeEnvironmentType == org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB) {
		            try {

		                Common::MimeType mt = Utils::Mime.getMimeTypeForExt(org.radixware.kernel.common.utils.FileUtils.getFileExt(new java.io.File(fileName)));
		                String mimeType = mt == null ? "" : mt.Value;
		                ReportsExplorerUtils.printOrOpenFile(Environment, new java.io.File(fileName), false, mimeType, suggestFileName(), true);
		            } catch (Exceptions::Throwable ex) {
		                Environment.messageException("Error", "Unable to open generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + fileName + "'.", ex);
		            }
		        }
		    } else {
		        final java.io.File tempFile = createTmpPdfFile(isMultyFile.Value == Boolean.TRUE);
		        if (isMultyFile.Value == Boolean.TRUE) {
		            xGenerateReport.setFile(tempFile.getAbsolutePath());
		        }

		        xGenerateReport.setFile(tempFile.AbsolutePath);

		        Environment.EasSession.executeCommand(generateReportCmd.Owner, null, generateReportCmd.Id, null, generateReportRequestDoc);
		        Environment.getTracer().debug("Report saved in '" + tempFile.AbsolutePath + "'");

		        boolean print = this.printOnly.Value.booleanValue();
		        try {
		            Str mimeType;
		            if (exportType != null) {
		                switch (exportType) {
		                    case ReportExportFormat:CSV:
		                        mimeType = Common::MimeType:CSV.Value;
		                        break;
		                    case ReportExportFormat:XML:
		                        mimeType = Common::MimeType:XML.Value;
		                        break;
		                    case ReportExportFormat:PDF:
		                        mimeType = Common::MimeType:Pdf.Value;
		                        break;
		                    case ReportExportFormat:XLS:
		                        mimeType = Common::MimeType:MsOfficeSpreadsheet.Value;
		                        break;
		                    case ReportExportFormat:XLSX:
		                        mimeType = Common::MimeType:MsOfficeSpreadsheetX.Value;
		                        break;
		                    default:
		                        mimeType = Common::MimeType:PlainText.Value;
		                }
		            } else {
		                mimeType = Common::MimeType:Pdf.Value;
		            }
		            ReportsExplorerUtils.printOrOpenFile(Environment, tempFile, print, mimeType, suggestFileName(), true);
		        } catch (Exceptions::Throwable ex) {
		            Environment.messageException("Error", "Unable to " + (print ? "print" : "open") + " generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + tempFile.getAbsolutePath() + "'.", ex);
		        }

		    }
		} catch (Exceptions::InterruptedException ex) {
		    return; // cancelled
		} catch (Exceptions::Exception ex) {
		    Environment.processException(ex);
		}

	}

	/*Radix::Reports::ReportPub:General:Model:generateReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:generateReport")
	public published  boolean generateReport (org.radixware.kernel.common.client.models.items.Command generateReportCmd, Str exportDirectory, Str defaultFileName, org.radixware.ads.Reports.common.ReportExportFormat.Arr exportFormats, boolean isOpenPdf) {
		try {
		    final Explorer.Models::EntityModel parentEntityModel = (Explorer.Models::EntityModel) generateReportCmd.Owner;
		    final Explorer.Models::EntityModel contextEntityModel
		            = (generateReportCmd.Id != idof[ReportPub.Contextless:GenerateReport]
		                    ? parentEntityModel
		                    : null);

		    final ReportsXsd:GenerateReportRqDocument generateReportRequestDoc = ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		    final ReportsXsd:GenerateReportRqDocument.GenerateReportRq xGenerateReport = generateReportRequestDoc.addNewGenerateReportRq();

		    if (!inputReportParams(contextEntityModel, xGenerateReport, parentEntityModel)) {
		        return false;
		    }

		    xGenerateReport.setReportPubPid(this.getPid().toString());

		    if (exportDirectory != null && exportFormats != null) {
		        xGenerateReport.File = exportDirectory;
		        xGenerateReport.DefaultFileName = defaultFileName;

		        ReportsXsd:GenerateReportRqDocument.GenerateReportRq.FormatDirectories xFormatDirectories = xGenerateReport.addNewFormatDirectories();
		        ReportsXsd:GenerateReportRqDocument.GenerateReportRq.FormatDirectories.FormatDirectory xFormatDirectory = xFormatDirectories.addNewFormatDirectory();

		        java.io.File tmpPdfDir = createTmpPdfFile(true);

		        xFormatDirectory.Format = ReportExportFormat:PDF;
		        xFormatDirectory.Path = tmpPdfDir.getPath();

		        boolean wasPdf = true;
		        if (isOpenPdf && !exportFormats.contains(ReportExportFormat:PDF)) {
		            exportFormats.add(ReportExportFormat:PDF);
		            wasPdf = false;
		        }

		        ReportsXsd:GenerateReportRqDocument.GenerateReportRq.ExportFormats xExportFormats = xGenerateReport.addNewExportFormats();
		        xExportFormats.getExportFormatList().addAll(exportFormats);

		        Environment.EasSession.executeCommand(generateReportCmd.Owner, null, generateReportCmd.Id, null, generateReportRequestDoc);

		        boolean isOnWeb = Environment.getApplication().RuntimeEnvironmentType == org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB;

		        java.io.File openFile;
		        if (tmpPdfDir.listFiles() != null && tmpPdfDir.listFiles().length > 0) {
		            if (wasPdf) {
		                if (!isMultyFile.Value.booleanValue()) {
		                    openFile = new java.io.File(exportDirectory, tmpPdfDir.listFiles()[0].getName());
		                    java.nio.file.Files.move(tmpPdfDir.listFiles()[0].toPath(), openFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		                } else {
		                    for (java.io.File pdfFile : tmpPdfDir.listFiles()) {
		                        openFile = new java.io.File(exportDirectory, pdfFile.getName());
		                        java.nio.file.Files.move(tmpPdfDir.listFiles()[0].toPath(), openFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		                    }
		                    openFile = new java.io.File(exportDirectory);
		                }
		                org.radixware.kernel.common.utils.FileUtils.deleteDirectory(tmpPdfDir);
		            } else {
		                if (!isMultyFile.Value.booleanValue()) {
		                    openFile = tmpPdfDir.listFiles()[0];
		                } else {
		                    openFile = tmpPdfDir;
		                }
		            }
		        } else {
		            org.radixware.kernel.common.utils.FileUtils.deleteDirectory(tmpPdfDir);
		            openFile = new java.io.File(exportDirectory);
		        }

		        if (isOnWeb || isOpenPdf) {
		            try {
		                Common::MimeType mt = Utils::Mime.getMimeTypeForExt(exportFormats.get(0) == ReportExportFormat:XLS ? "xls" : exportFormats.get(0).getExt());
		                String mimeType = mt == null ? "" : mt.Value;

		                if (isOnWeb) {
		                    java.io.File exportDir = new java.io.File(exportDirectory);
		                    if (exportDir.listFiles() != null && exportDir.listFiles().length == 1) {
		                        openFile = exportDir.listFiles()[0];
		                    } else if (exportDir.listFiles() != null) {
		                        openFile = exportDir;
		                    }
		                }

		                ReportsExplorerUtils.printOrOpenFile(Environment, openFile, false, mimeType, defaultFileName == null ? suggestFileName() : defaultFileName, true);
		            } catch (Exceptions::Throwable ex) {
		                Environment.messageException("Error", "Unable to open generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + exportDirectory + "'.", ex);
		            }
		        }
		    } else {
		        final java.io.File tempFile = createTmpPdfFile(isMultyFile.Value == Boolean.TRUE);
		        if (isMultyFile.Value == Boolean.TRUE) {
		            xGenerateReport.setFile(tempFile.getAbsolutePath());
		        }
		        xGenerateReport.setFile(tempFile.AbsolutePath);

		        Environment.EasSession.executeCommand(generateReportCmd.Owner, null, generateReportCmd.Id, null, generateReportRequestDoc);
		        Environment.getTracer().debug("Report saved in '" + tempFile.AbsolutePath + "'");

		        boolean print = this.printOnly.Value.booleanValue();
		        try {
		            Str mimeType;
		            if (exportFormats != null) {
		                switch (exportFormats.get(0)) {
		                    case ReportExportFormat:CSV:
		                        mimeType = Common::MimeType:CSV.Value;
		                        break;
		                    case ReportExportFormat:XML:
		                        mimeType = Common::MimeType:XML.Value;
		                        break;
		                    case ReportExportFormat:PDF:
		                        mimeType = Common::MimeType:Pdf.Value;
		                        break;
		                    case ReportExportFormat:XLS:
		                        mimeType = Common::MimeType:MsOfficeSpreadsheet.Value;
		                        break;
		                    case ReportExportFormat:XLSX:
		                        mimeType = Common::MimeType:MsOfficeSpreadsheetX.Value;
		                        break;
		                    default:
		                        mimeType = Common::MimeType:PlainText.Value;
		                }
		            } else {
		                mimeType = Common::MimeType:Pdf.Value;
		            }
		            ReportsExplorerUtils.printOrOpenFile(Environment, tempFile, print, mimeType, suggestFileName(), true);
		        } catch (Exceptions::Throwable ex) {
		            Environment.messageException("Error", "Unable to " + (print ? "print" : "open") + " generated report.\nCause: " + ex.getLocalizedMessage() + "\nReport saved in '" + tempFile.getAbsolutePath() + "'.", ex);
		            return false;
		        }

		    }
		} catch (Exceptions::InterruptedException ex) {
		    return false; // cancelled
		} catch (Exceptions::Exception ex) {
		    Environment.processException(ex);
		    return false;
		}

		return true;

	}

	/*Radix::Reports::ReportPub:General:Model:getRolesIdsProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:getRolesIdsProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyArrStr getRolesIdsProp (org.radixware.kernel.common.types.Id roleTitlePropId) {
		return userRoleGuids;

	}

	/*Radix::Reports::ReportPub:General:Model:getRolesTitlesProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:getRolesTitlesProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRolesTitlesProp (org.radixware.kernel.common.types.Id editedRolePropId) {
		return userRoles;
	}

	/*Radix::Reports::ReportPub:General:Model:updateParametersBinding-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateParametersBinding")
	public  void updateParametersBinding () {
		try {
		    if (getEditorPage(idof[ReportPub:General:Parameters]).getView() == null) {
		        return; // not opened yet.
		    }
		} catch (Explorer.Exceptions::NoDefinitionWithSuchIdError e) {
		    return;//illegal usage
		}

		ReportParametersBindingWidgetWeb editor = ReportPub:General:Parameters:WebView:widget:paramBindingEditor;
		ReportParametersBindingWidgetWeb:Model model = editor.Model;
		model.parameters = new java.util.ArrayList<Explorer.Models.Properties::Property>();
		model.parametersBindingDoc = null;
		model.contextParameterId = null;

		if (reportClassId != null) {
		    final Explorer.Meta::ReportPresentationDef reportPresentationDef = Environment.DefManager.getReportPresentationDef(reportClassId);
		    final java.util.List<Types::Id> propIds = reportPresentationDef.getPropertyIds();

		    final Explorer.Models::Model reportModel = reportPresentationDef.createModel(new ReportParametersContext(this,this));
		    for (Types::Id propId : propIds) {
		        final Explorer.Meta::PropertyDef propDef = reportPresentationDef.getPropertyDefById(propId);
		        if (propDef.getNature() == Meta::PropNature:SqlClassParameter) {
		            Explorer.Models.Properties::Property param = reportModel.getProperty(propId);
		            model.parameters.add(param);
		        }
		    }

		    if (paramBinding.Value != null) {
		        try {
		            model.parametersBindingDoc = ReportsXsd:ParametersBindingDocument.Factory.parse(paramBinding.Value);
		        } catch (org.apache.xmlbeans.XmlException ex) {
		            Environment.processException(ex);
		        }
		    }

		    if (this.contextObjectClassGuid.Value != null) {
		        model.contextParameterId = reportPresentationDef.ContextParameterId;
		    }
		}
		model.init();

	}

	/*Radix::Reports::ReportPub:General:Model:updateColumnsSettings-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateColumnsSettings")
	public  void updateColumnsSettings () {
		// columns settings editor is not available for web
	}

	/*Radix::Reports::ReportPub:General:Model:parametersBinding_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:parametersBinding_opened")
	public  void parametersBinding_opened (org.radixware.kernel.common.client.views.IEditorPageView view) {
		//();
	}

	/*Radix::Reports::ReportPub:General:Model:updateColumnsPageVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:updateColumnsPageVisibility")
	private final  void updateColumnsPageVisibility () {
		if (!isEditorPageExists(idof[ReportPub:General:Columns])) {
		    return;
		}

		if (reportClassGuid.Value == null) {
		    getEditorPage(idof[ReportPub:General:Columns]).setVisible(false);
		    return;
		}

		Arr<ReportExportFormat> formats;
		if (allowedFormats.Value != null) {
		    formats = allowedFormats.Value;
		} else if (!supportedFormats.Value.isEmpty()) {
		    formats = supportedFormats.Value;
		} else {
		    formats = new Arr<ReportExportFormat>();
		    formats.add(ReportExportFormat:PDF);
		}

		if (!formats.contains(ReportExportFormat:PDF)) {
		    if (formats.contains(ReportExportFormat:XLSX) || formats.contains(ReportExportFormat:CSV)) {
		        getEditorPage(idof[ReportPub:General:Columns]).setVisible(true);
		    } else {
		        getEditorPage(idof[ReportPub:General:Columns]).setVisible(false);
		    }
		} else if (ReportsExplorerUtils.isAnyReportCellAssotiateToColumn(getEnvironment(), reportClassId)) {
		    getEditorPage(idof[ReportPub:General:Columns]).setVisible(true);
		} else {
		    getEditorPage(idof[ReportPub:General:Columns]).setVisible(false);
		}
	}

	/*Radix::Reports::ReportPub:General:Model:cancelChanges-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Model:cancelChanges")
	public published  void cancelChanges () {
		super.cancelChanges();
		setVisibility();
	}
	public final class ExportReport extends org.radixware.ads.Reports.web.ReportPub.ExportReport{
		protected ExportReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ExportReport( this );
		}

	}


























}

/* Radix::Reports::ReportPub:General:Model - Web Meta*/

/*Radix::Reports::ReportPub:General:Model-Entity Model Class*/

package org.radixware.ads.Reports.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFP5NARHOA5E7LNMKWHBCS5Y3FM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Reports::ReportPub:General:Model:Properties-Properties*/
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

/* Radix::Reports::ReportPub:General:Parameters:View - Desktop Executable*/

/*Radix::Reports::ReportPub:General:Parameters:View-Custom Page Editor for Desktop*/

package org.radixware.ads.Reports.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Parameters:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View EditorPageView;
	public View getEditorPageView(){ return EditorPageView;}
	public org.radixware.ads.Reports.explorer.ReportParametersBindingWidget object;
	public org.radixware.ads.Reports.explorer.ReportParametersBindingWidget getObject(){ return object;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		EditorPageView = this;
		EditorPageView.setObjectName("EditorPageView");
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 499, 382));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QVBoxLayout $layout1 = new com.trolltech.qt.gui.QVBoxLayout(EditorPageView);
		$layout1.setObjectName("verticalLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setSpacing(6);
		object = new org.radixware.ads.Reports.explorer.ReportParametersBindingWidget(model.getEnvironment(),EditorPageView);
		object.open(object.getModel());
		object.setObjectName("object");
		object.setFont(DEFAULT_FONT);
		object.sig7EBQC42NXNDJ3K6W6TW4T57VJA.connect(model, "mthJMKUDSWDHNA4XJ3NTHMYFOOZIU()");
		$layout1.addWidget(object);
		EditorPageView.opened.connect(model, "mthCRTU2HUS5JB47ACVJ4QXTQR4MA(com.trolltech.qt.gui.QWidget)");
		opened.emit(this);
	}
	public final org.radixware.ads.Reports.explorer.General:Model getModel() {
		return (org.radixware.ads.Reports.explorer.General:Model) super.getModel();
	}

}

/* Radix::Reports::ReportPub:General:Parameters:WebView - Web Executable*/

/*Radix::Reports::ReportPub:General:Parameters:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.Reports.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Parameters:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public org.radixware.ads.Reports.web.ReportParametersBindingWidgetWeb paramBindingEditor;
	public org.radixware.ads.Reports.web.ReportParametersBindingWidgetWeb getParamBindingEditor(){ return  paramBindingEditor;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::Reports::ReportPub:General:Parameters:WebView:widget ==============
		this.widget = this;
		widget.setWidth(558);
		widget.setHeight(457);
		//============ Radix::Reports::ReportPub:General:Parameters:WebView:widget:paramBindingEditor ==============
		this.paramBindingEditor = new org.radixware.ads.Reports.web.ReportParametersBindingWidgetWeb(Environment);
		paramBindingEditor.setTop(1);
		paramBindingEditor.setLeft(1);
		paramBindingEditor.setWidth(556);
		paramBindingEditor.setHeight(455);
		paramBindingEditor.setObjectName("paramBindingEditor");
		this.widget.add(this.paramBindingEditor);
		paramBindingEditor.getAnchors().setBottom(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,-1));
		paramBindingEditor.getAnchors().setRight(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,-1));
		this.wdgKNJY3G453JGVFDL5HNWLBFVTDU.open();
		this.widget.addEditorPageListener(new org.radixware.wps.views.editor.EditorPageView.EditorPageOpenHandler(){
			public void opened(org.radixware.kernel.common.client.views.IEditorPageView p0){
				((org.radixware.ads.Reports.web.General:Model)getModel()).parametersBinding_opened(p0);
			}
		});
		fireOpened();
	}
}

/* Radix::Reports::ReportPub:General:Columns:View - Desktop Executable*/

/*Radix::Reports::ReportPub:General:Columns:View-Custom Page Editor for Desktop*/

package org.radixware.ads.Reports.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPub:General:Columns:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View ReportColumnsView;
	public View getReportColumnsView(){ return ReportColumnsView;}
	public org.radixware.ads.Reports.explorer.ReportColumnsSettingWidget columnsSettingsEditor;
	public org.radixware.ads.Reports.explorer.ReportColumnsSettingWidget getColumnsSettingsEditor(){ return columnsSettingsEditor;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		ReportColumnsView = this;
		ReportColumnsView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 600, 400));
		ReportColumnsView.setObjectName("ReportColumnsView");
		ReportColumnsView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QGridLayout $layout1 = new com.trolltech.qt.gui.QGridLayout(ReportColumnsView);
		$layout1.setObjectName("gridLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setHorizontalSpacing(6);
		$layout1.setVerticalSpacing(6);
		columnsSettingsEditor = new org.radixware.ads.Reports.explorer.ReportColumnsSettingWidget(model.getEnvironment(),ReportColumnsView);
		columnsSettingsEditor.open(columnsSettingsEditor.getModel());
		columnsSettingsEditor.setGeometry(new com.trolltech.qt.core.QRect(144, 22, 200, 200));
		columnsSettingsEditor.setObjectName("columnsSettingsEditor");
		columnsSettingsEditor.setFont(DEFAULT_FONT);
		columnsSettingsEditor.sigSMYKYBQUQJBRTM5YRYMLSTIISI.connect(model, "mth2XRIODHW5RAIXAPAHGJ4NPJX3Y()");
		$layout1.addWidget(columnsSettingsEditor, 0, 0, 1, 1);
		ReportColumnsView.opened.connect(model, "mthSEISCDO6YZDH5O3PD26EGMYTMU(com.trolltech.qt.gui.QWidget)");
		opened.emit(this);
	}
	public final org.radixware.ads.Reports.explorer.General:Model getModel() {
		return (org.radixware.ads.Reports.explorer.General:Model) super.getModel();
	}

}

/* Radix::Reports::ReportPub:Common - Desktop Meta*/

/*Radix::Reports::ReportPub:Common-Selector Presentation*/

package org.radixware.ads.Reports.explorer;
public final class Common_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Common_mi();
	private Common_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E"),
		"Common",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQ64NKOZ4VVFUTGRIGEUCIFMBLE"),
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		262160,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV4UEKFQHKNGRVCLOX4V33D4NQY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BK2KKPDYJCOFIAHL23AZ4XOCE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNK3L4L6SJFGZBFKG64XTIGTBM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXFVM3NF5JBVPM44EQV6TLOEB4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3NQ35UT3PZFMTC56PJMYAHOZN4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQV2RHG7LRHZVJ2KHGLJI2HVEQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPMYIAKHDBBPHHJB5COTEL5NHA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYCGMKKZQYNAUNPB2IBHKW2FHM4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Reports.explorer.ReportPub.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Reports::ReportPub:Common - Web Meta*/

/*Radix::Reports::ReportPub:Common-Selector Presentation*/

package org.radixware.ads.Reports.web;
public final class Common_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Common_mi();
	private Common_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYYUTYV7EYRF4TEAHGYK5SWGV6E"),
		"Common",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecUNC67IF5OBCX7NOPNLQOCUG374"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblUNC67IF5OBCX7NOPNLQOCUG374"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQ64NKOZ4VVFUTGRIGEUCIFMBLE"),
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		262160,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFP5NARHOA5E7LNMKWHBCS5Y3FM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5Z77DJW25NE3NOMCZ6G7JSIGSQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKRIJCTPH4BBTJACPINFNMGHFOU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV4UEKFQHKNGRVCLOX4V33D4NQY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BK2KKPDYJCOFIAHL23AZ4XOCE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNK3L4L6SJFGZBFKG64XTIGTBM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXFVM3NF5JBVPM44EQV6TLOEB4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3NQ35UT3PZFMTC56PJMYAHOZN4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQV2RHG7LRHZVJ2KHGLJI2HVEQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPMYIAKHDBBPHHJB5COTEL5NHA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYCGMKKZQYNAUNPB2IBHKW2FHM4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Reports.web.ReportPub.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Reports::ReportPub - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPub - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Encoding (for CSV and TXT)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка (для CSV или TXT)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls23SA6GQDEREUZKJ7Q3N3Z7XXMY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2HVIHI3M4FBAVGXFSEU5S46LFI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3TKQH37GMNDAPLUQDIUXNEVVAY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<report not found>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<отчет не найден>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4JO6LWKAX5EEJAD6JHMAG2X4JA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report publication list");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список публикаций отчетов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5FBPZNPVQRBRNMIZLHIHVIGUN4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to generate a report: access denied.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно выполнить публикацию отчета: в доступе отказано.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5G75KRPWIFD7TKQ3P7Y55IPVGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5XHSYWNCTZAFNK57WOHRSYRVVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Format");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6IW7MDKVIVHFHLIHFPSEIJ46QI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6L4VVCKNVRGY5PQPQ7XZOVGRDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report file encoding (for CSV and TXT). If not defined, the system encoding is used. ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка текстового представления отчета (применяется для форматов txt и csv). Если не задано - используется системная кодировка ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TCHPRWEL5G5BE5NUJ6AM5WJDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to close the file output resource");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось закрыть ресурс вывода файлов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6VQK6CB3CNHJLB7NSXFTXRC3QE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7DUY6Z2LYBCEPMLL6AFXLPVLU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inherited publication");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Наследуемая публикация");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LNAYJZ5XRGA5OYEMUVJFO6KRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to generate a report: the current parameter value and the required one do not match.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно выполнить публикацию отчета: актуальное и требуемое значение финального параметра не совпадают.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAZMY34H53FARJBIDOY47QSMCZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report Publication");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Публикация отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG53YVNHFNHZZPJTDWK3EJWEMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<no one>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBKRJKNUS7FAATAMABOKVMGWD7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Available export formats");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Доступные форматы выгрузки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCN764DRY3FA2BHEH66DMQIBWQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<all of supported>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<все поддерживаемые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDLK7ULKYSZGRPJBJEK4JP5A6KE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"use system encoding");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"использовать кодировку по-умолчанию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQXOMLCULBGLFHC5ZME2ENHYQ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Publication");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Публикация");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDVUTAJD2K5BCVHSQQ4B5TUWU5Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETZLOQWHDNARHJ5XEL6FHFON7Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to generate a report: context parameter must be a reference to the entity.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно выполнить публикацию отчета: контекстный параметр должен быть ссылкой на сущность.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEY5LSEE42FFUNBJNA5CDTUQ2RA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Reports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательские отчеты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6AVERDWSZHODGFVCWA45QI6PY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to generate a report: context parameter not defined.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно выполнить публикацию отчета: контекстный параметр не задан.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6NSRPE5OFC33JCGF5LOSRBBHE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Index");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Индекс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ74K6773TBDGFGGUTJIGOR6GOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<common reports>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<общие отчеты>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJFQKO3FO3FHUBKWSTGU46Q7PEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Column Visibility Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки видимости колонок");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJPLQJINJSRGQHLBUZ5OTBTV7JU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unrestricted access>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неограниченный доступ>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJTWNFMUOOZCUVBFYPRIXVONOW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJYMCSU7D2ZHIVC2U3Y2XCI7NVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выбор отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMG2PRIV4HRHTTPE222LNCJCZI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to access restricted object: class \'%s\', PID=\'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка доступа к запрещенному объекту: класс  \'%s\', PID=\'%s\'.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPEK4UFYEIJGMPFIO3YVIOLXLFY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<use report name>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<использовать название отчета>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSZBTRRARNB2VHTV44D7QIFHT4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPYRBNJJB35C2BDIMQLGZW6G5SE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum size of report results cache (KB)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальный размер кеша результатов выполнения отчета (Кб)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGGWFKKFPFBFXJRGZI76YYZUW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report Publications");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Публикации отчетов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR6SCZBID3BATLNJAEEMGFMIWK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report Parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR7RU2BLUYBCNZO7HDPSMXRTPB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to delete the temporary files of the report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось удалить временные файлы отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTIBVWRTMXRFOHDDQ4EXXHIXWSY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJNY6FL7LFEXLFZMAXXVZH4MRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to generate a report: publication condition has not been met.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно выполнить публикацию отчета: условие публикации не выполнилось.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUT5XYLVYBZCRRF3ZUWL7FFZ5WI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type of \'%s\' parameter value does not match the one specified in parameters binding.\nUpdate the report publication.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип значения параметра \'%s\' не соответствует заданному в привязке параметров.\nАктуализируйте публикацию отчета.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVAELURRFKZATZNZLD3VKW5SVZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enabled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разрешен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVS7UECLSNVDSDHT3O77MHCV4RY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accessible for customers");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Доступен для клиентов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBAMHIDMXBEXTJ2S5B5XZKIQMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to generate a report: the specified context object and the expected one do not match.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно выполнить публикацию отчета: введенный и ожидаемый контекстные объекты не совпадают.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBSCRUQSFZHBHKZ2NDP7RJAFMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to generate a report: publication prohibited.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно выполнить публикацию отчета: публикация запрещена.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGD4YTLMMRBKPDVOQ5XCBFVDBM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Location");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Расположение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWP6FTGCPWVAIPMIYXUHAF3ZAUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to generate a report: report condition has not been met.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно выполнить публикацию отчета: условие отчета не выполнилось.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ732NLPPJNDCNPNHORYANYQHQ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Print only");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Только печать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZLDF446Y6NGAXNG5BM4R3GU52Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSRLBLESA5BH3KQ55ATNMQOOLA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ReportPub - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecUNC67IF5OBCX7NOPNLQOCUG374"),"ReportPub - Localizing Bundle",$$$items$$$);
}

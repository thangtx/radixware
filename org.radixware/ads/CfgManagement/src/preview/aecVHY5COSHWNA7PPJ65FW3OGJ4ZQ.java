
/* Radix::CfgManagement::ChangeLog - Server Executable*/

/*Radix::CfgManagement::ChangeLog-Entity Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog")
public final published class ChangeLog  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.IExportableUserProp,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	final String NO_REVISIONS_STR = "<no revisions>";
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ChangeLog_mi.rdxMeta;}

	/*Radix::CfgManagement::ChangeLog:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::ChangeLog:ComparisonResult-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult")
	private static class ComparisonResult  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ChangeLog_mi.rdxMeta_adcJ7XAYNOPZBHDVK4ZNWNKZCSOW4;}

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:Properties-Properties*/

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:message-Dynamic Property*/



		protected org.radixware.kernel.common.types.ArrStr message=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:message")
		 final  org.radixware.kernel.common.types.ArrStr getMessage() {
			return message;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:message")
		 final   void setMessage(org.radixware.kernel.common.types.ArrStr val) {
			message = val;
		}

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:result-Dynamic Property*/



		protected int result=0;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:result")
		 final  int getResult() {
			return result;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:result")
		 final   void setResult(int val) {
			result = val;
		}

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:thisRevFoundInImport-Dynamic Property*/



		protected boolean thisRevFoundInImport=false;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:thisRevFoundInImport")
		private final  boolean getThisRevFoundInImport() {
			return thisRevFoundInImport;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:thisRevFoundInImport")
		private final   void setThisRevFoundInImport(boolean val) {
			thisRevFoundInImport = val;
		}

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:thisRev-Dynamic Property*/



		protected org.radixware.ads.CfgManagement.server.Revision thisRev=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:thisRev")
		private final  org.radixware.ads.CfgManagement.server.Revision getThisRev() {
			return thisRev;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:thisRev")
		private final   void setThisRev(org.radixware.ads.CfgManagement.server.Revision val) {
			thisRev = val;
		}

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:otherRev-Dynamic Property*/



		protected org.radixware.schemas.commondef.ChangeLogItem otherRev=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:otherRev")
		private final  org.radixware.schemas.commondef.ChangeLogItem getOtherRev() {
			return otherRev;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:otherRev")
		private final   void setOtherRev(org.radixware.schemas.commondef.ChangeLogItem val) {
			otherRev = val;
		}



























































		/*Radix::CfgManagement::ChangeLog:ComparisonResult:Methods-Methods*/

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:thereIsConflict-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:thereIsConflict")
		public  boolean thereIsConflict () {
			return thisRev != null && !thisRevFoundInImport;
		}

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:ComparisonResult-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:ComparisonResult")
		public  ComparisonResult (org.radixware.ads.CfgManagement.server.Revision thisRev, org.radixware.schemas.commondef.ChangeLogItem otherRev, boolean thisRevFoundInImport, int result, org.radixware.kernel.common.types.ArrStr message) {
			result = result;
			message = message;
			thisRevFoundInImport = thisRevFoundInImport;
			thisRev = thisRev;
			otherRev = otherRev;
		}

		/*Radix::CfgManagement::ChangeLog:ComparisonResult:getMessage-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:ComparisonResult:getMessage")
		public  Str[] getMessage (org.radixware.ads.Types.server.Entity owner) {
			if (message == null) {
			    return null;
			}

			final Str[] messageStr;
			if (owner instanceof UserFunc::UserFunc) {
			    messageStr = new Str[message.size() + 1];
			    messageStr[0] = ((UserFunc::UserFunc) owner).path;
			    int i = 1;
			    for (Str mess : message) {
			        messageStr[i++] = mess;
			    }
			} else {
			    messageStr = new Str[message.size()];
			    message.toArray(messageStr);
			}
			return messageStr;
		}


	}

	/*Radix::CfgManagement::ChangeLog:Properties-Properties*/

	/*Radix::CfgManagement::ChangeLog:comments-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:comments")
	public published  Str getComments() {
		return comments;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:comments")
	public published   void setComments(Str val) {
		comments = val;
	}

	/*Radix::CfgManagement::ChangeLog:localNotes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:localNotes")
	public published  Str getLocalNotes() {
		return localNotes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:localNotes")
	public published   void setLocalNotes(Str val) {
		localNotes = val;
	}

	/*Radix::CfgManagement::ChangeLog:upDefId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:upDefId")
	public published  Str getUpDefId() {
		return upDefId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:upDefId")
	public published   void setUpDefId(Str val) {
		upDefId = val;
	}

	/*Radix::CfgManagement::ChangeLog:upOwnerEntityId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:upOwnerEntityId")
	public published  Str getUpOwnerEntityId() {
		return upOwnerEntityId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:upOwnerEntityId")
	public published   void setUpOwnerEntityId(Str val) {
		upOwnerEntityId = val;
	}

	/*Radix::CfgManagement::ChangeLog:upOwnerPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:upOwnerPid")
	public published  Str getUpOwnerPid() {
		return upOwnerPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:upOwnerPid")
	public published   void setUpOwnerPid(Str val) {
		upOwnerPid = val;
	}

	/*Radix::CfgManagement::ChangeLog:lastRevisionId-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionId")
	public published  Int getLastRevisionId() {
		return lastRevisionId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionId")
	public published   void setLastRevisionId(Int val) {
		lastRevisionId = val;
	}

	/*Radix::CfgManagement::ChangeLog:lastRevision-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.Revision lastRevision=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevision")
	  org.radixware.ads.CfgManagement.server.Revision getLastRevision() {

		if (internal[lastRevision] != null) {
		    return internal[lastRevision];
		}
		if (lastRevisionId == null) {
		    return null;
		}

		internal[lastRevision] = Revision.loadByPK(upDefId, upOwnerEntityId, upOwnerPid, lastRevisionId, true);

		return internal[lastRevision];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevision")
	   void setLastRevision(org.radixware.ads.CfgManagement.server.Revision val) {
		lastRevision = val;
	}

	/*Radix::CfgManagement::ChangeLog:lastRevisionAuthor-Dynamic Property*/



	protected Str lastRevisionAuthor=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionAuthor")
	private final  Str getLastRevisionAuthor() {

		return lastRevision != null ? lastRevision.author : null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionAuthor")
	private final   void setLastRevisionAuthor(Str val) {
		lastRevisionAuthor = val;
	}

	/*Radix::CfgManagement::ChangeLog:lastRevisionTime-Dynamic Property*/



	protected java.sql.Timestamp lastRevisionTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionTime")
	private final  java.sql.Timestamp getLastRevisionTime() {

		return lastRevision != null ? lastRevision.time : null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionTime")
	private final   void setLastRevisionTime(java.sql.Timestamp val) {
		lastRevisionTime = val;
	}

	/*Radix::CfgManagement::ChangeLog:lastRevisionSeq-Dynamic Property*/



	protected Int lastRevisionSeq=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionSeq")
	  Int getLastRevisionSeq() {

		return lastRevision != null ? lastRevision.seq : null;
	}

	/*Radix::CfgManagement::ChangeLog:revisionFromEditorCreate-Dynamic Property*/



	protected org.radixware.schemas.commondef.ChangeLogItemDocument revisionFromEditorCreate=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:revisionFromEditorCreate")
	private final  org.radixware.schemas.commondef.ChangeLogItemDocument getRevisionFromEditorCreate() {
		return revisionFromEditorCreate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:revisionFromEditorCreate")
	private final   void setRevisionFromEditorCreate(org.radixware.schemas.commondef.ChangeLogItemDocument val) {
		revisionFromEditorCreate = val;
	}



























































































	/*Radix::CfgManagement::ChangeLog:Methods-Methods*/

	/*Radix::CfgManagement::ChangeLog:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:loadByPK")
	public static published  org.radixware.ads.CfgManagement.server.ChangeLog loadByPK (Str upDefId, Str upOwnerEntityId, Str upOwnerPid, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(9);
			if(upDefId==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYZPLJDVNTRBOZGTGEVP7OFHURE"),upDefId);
			if(upOwnerEntityId==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDNQQV5SM4VAWTCKEOAEDTUXAMI"),upOwnerEntityId);
			if(upOwnerPid==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRGHQSIYJKJALXJACD42AGVY2VI"),upOwnerPid);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),pkValsMap);
		try{
		return (
		org.radixware.ads.CfgManagement.server.ChangeLog) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::CfgManagement::ChangeLog:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:loadByPidStr")
	public static published  org.radixware.ads.CfgManagement.server.ChangeLog loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),pidAsStr);
		try{
		return (
		org.radixware.ads.CfgManagement.server.ChangeLog) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::CfgManagement::ChangeLog:onCalcTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:onCalcTitle")
	protected published  Str onCalcTitle (Str title) {
		if (title != null && (title.isEmpty() || "null".equals(title))) {
		    return NO_REVISIONS_STR;
		}
		return super.onCalcTitle(title);
	}

	/*Radix::CfgManagement::ChangeLog:export-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:export")
	public published  org.apache.xmlbeans.XmlObject export () {
		return export(false);
	}

	/*Radix::CfgManagement::ChangeLog:import-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:import")
	public published  org.radixware.ads.Types.server.Entity import (org.radixware.ads.Types.server.Entity owner, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		return new ChangeLog().import(owner, owner, propId, xml, helper);
	}

	/*Radix::CfgManagement::ChangeLog:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:export")
	public published  org.radixware.schemas.commondef.ChangeLog export () {
		return export(false);
	}

	/*Radix::CfgManagement::ChangeLog:import-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:import")
	public static published  org.radixware.ads.CfgManagement.server.ChangeLog import (org.radixware.ads.Types.server.Entity owner, org.radixware.kernel.common.types.Id propId, boolean isSet, org.radixware.schemas.commondef.ChangeLog xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		return import(owner, owner, propId, isSet, xml, helper);
	}

	/*Radix::CfgManagement::ChangeLog:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:export")
	public  org.radixware.schemas.commondef.ChangeLog export (boolean exportLocals) {
		Types::CommonDefXsd:ChangeLog xChangeLog = Types::CommonDefXsd:ChangeLog.Factory.newInstance();
		xChangeLog.Comments = comments;
		if (exportLocals) {
		    xChangeLog.LocalNotes = localNotes;
		}

		ChangeLogRevisionsCursor cursor = ChangeLogRevisionsCursor.open(this);
		try {
		    while (cursor.next()) {
		        xChangeLog.addNewChangeLogItem().set(cursor.rev.exportXml(exportLocals));
		    }
		} finally {
		    cursor.close();
		}

		return xChangeLog;
	}

	/*Radix::CfgManagement::ChangeLog:import-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:import")
	public static  org.radixware.ads.CfgManagement.server.ChangeLog import (org.radixware.ads.Types.server.Entity owner, org.radixware.ads.Types.server.Entity existingChangelogOwner, org.radixware.kernel.common.types.Id propId, boolean isSet, org.radixware.schemas.commondef.ChangeLog xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (owner == null || propId == null) {
		    String errMsg = "Error on import ChangeLog: Owner or propId is null";
		    if (helper != null) {
		        helper.reportWarnings("ChangeLog", errMsg);
		    }
		    Arte::Trace.error(Arte::Trace.exceptionStackToString(new IllegalArgumentException(errMsg)), Arte::EventSource:AppCfgPackage);
		    return null;
		}

		if (!isSet) {
		    owner.setPropHasOwnVal(propId, false);
		    return null;
		} else if (xml == null || xml.isNil()) {
		    owner.setProp(propId, null);
		    return null;
		}

		return (ChangeLog) new ChangeLog().import(owner, existingChangelogOwner, propId, xml, helper);
	}

	/*Radix::CfgManagement::ChangeLog:import-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:import")
	private final  org.radixware.ads.Types.server.Entity import (org.radixware.ads.Types.server.Entity owner, org.radixware.ads.Types.server.Entity existingChangelogOwner, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null || xml.isNil() || owner == null || propId == null) {
		    String errMsg = "Xml or owner or propId is null";
		    if (helper != null) {
		        helper.reportWarnings(this, errMsg);
		    }
		    Arte::Trace.error("Error on import changelog \n"
		            + Arte::Trace.exceptionStackToString(new IllegalArgumentException(errMsg)) + "\n" + xml, Arte::EventSource:AppCfgPackage);
		    return null;
		}

		try {
		    Types::CommonDefXsd:ChangeLog xChangeLog = Types::CommonDefXsd:ChangeLog.Factory.parse(xml.newReader());
		    final ChangeLog existingLog;
		    if (existingChangelogOwner != null) {
		        existingLog = (ChangeLog) existingChangelogOwner.getProp(propId);
		    } else {
		        existingLog = null;
		    }
		    if (existingLog != null) {
		        ChangeLog.ComparisonResult result = existingLog.compareChangelogRevisions(xChangeLog);
		        if (result.thereIsConflict() && helper != null) {
		            helper.reportWarnings(helper.getContextItem() != null ? helper.getContextItem() : owner,
		                    result.getMessage(owner));
		        }

		//In case of UserReportVersion owner != existingChangelogOwner,
		        //in other cases we must delete existingLog
		        if (owner == existingChangelogOwner) {
		            cleanupExistingChangeLog(owner, existingLog, helper);
		        }
		    }
		    if (!isInited()) {
		        init();
		    }

		    comments = xChangeLog.Comments;
		    localNotes = xChangeLog.LocalNotes;

		    owner.setProp(propId, this);

		    Revision lastRev = null;
		    for (Types::CommonDefXsd:ChangeLogItem xRev : xChangeLog.getChangeLogItemList()) {
		        final Revision rev = Revision.importRevision(this, xRev);
		        if (rev.kind == ChangelogItemKind:MODIFY && (lastRev == null || rev.seq.intValue() > lastRev.seq.intValue())) {
		            lastRev = rev;
		        }
		    }

		    if (helper != null) {
		        if (lastRev != null && helper.getChangeLogImportHelper() != null) {
		            final String lastRevTitle = lastRev.calcTitle();
		            helper.getChangeLogImportHelper().registerChangeLogLastRevMess(owner.getPid(), lastRevTitle);
		            if (owner instanceof IChangeLogOwner) {
		                final IChangeLogOwner cOwner = (IChangeLogOwner) owner;
		                final java.util.List<Types::Entity> addOwners = cOwner.getAdditionalOwners();
		                if (addOwners != null) {
		                    for (Types::Entity addOwner : addOwners) {
		                        if (addOwner != null) {
		                            helper.getChangeLogImportHelper().registerChangeLogLastRevMess(addOwner.getPid(), lastRevTitle);
		                        }
		                    }
		                }
		            }
		        }

		        if (helper.getContextPacket() != null) {
		            Revision.addImportRevision(this, helper.getContextPacket());
		        }
		    }

		} catch (Exceptions::IOException | Exceptions::XmlException ex) {
		    if (helper != null) {
		        helper.reportWarnings(this, "Error on import changelog:" + ex.getMessage());
		    }
		    Arte::Trace.error("Error on import changelog: \n" + Arte::Trace.exceptionStackToString(ex),
		            Arte::EventSource:AppCfgPackage);
		    return null;
		}

		return this;
	}

	/*Radix::CfgManagement::ChangeLog:compareChangelogRevisions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:compareChangelogRevisions")
	private final  org.radixware.ads.CfgManagement.server.ChangeLog.ComparisonResult compareChangelogRevisions (org.radixware.schemas.commondef.ChangeLog xOther) {
		boolean thisRevisionFoundInImport = false;
		int result = 0;
		final ArrStr message = new ArrStr();

		final java.util.List<Types::CommonDefXsd:ChangeLogItem> xOtherRevs = xOther.ChangeLogItemList;
		Types::CommonDefXsd:ChangeLogItem xOtherLastRev = null;
		if (xOtherRevs != null) {
		    int maxRevisionNum = -1;
		    for (int index = xOtherRevs.size() - 1; index >= 0; index--) {
		        Types::CommonDefXsd:ChangeLogItem xRev = xOtherRevs.get(index);
		        if (Revision.getKind(xRev) == ChangelogItemKind:MODIFY && xRev.RevisionNumber.intValue() > maxRevisionNum) {
		            xOtherLastRev = xRev;
		            maxRevisionNum = xRev.RevisionNumber.intValue();
		        }
		    }
		}
		final Revision thisLastRev = lastRevision != null ? lastRevision : null;

		if (thisLastRev == null && xOtherLastRev == null) {
		    result = 0;
		} else if (thisLastRev == null) {
		    result = -1;
		} else if (xOtherLastRev == null) {
		    result = 1;
		} else {
		    for (int index = xOtherRevs.size() - 1; index >= 0; index--) {
		        Types::CommonDefXsd:ChangeLogItem xRev = xOtherRevs.get(index);
		        if (thisLastRev.seq == xRev.RevisionNumber && thisLastRev.time.Time == xRev.Date.Time) {
		            thisRevisionFoundInImport = true;
		            result = index == xOtherRevs.size() - 1 ? 0 : -1;
		            break;
		        }
		    }
		    if (!thisRevisionFoundInImport) {
		        result = Int.compare(thisLastRev.time.Time, xOtherLastRev.Date.Time);
		    }
		}

		if (result == 0) {
		    message.add("Changelogs are equal");
		} else {
		    final String conflictMsg
		            = "Change log conflict, latest revision available in this system is absent in the change log being imported:";
		    message.add(conflictMsg);
		    String thisLastRevStr = thisLastRev != null
		            ? thisLastRev.calcTitle() : NO_REVISIONS_STR;
		    message.add("Latest revision in this system" + " : " + thisLastRevStr);
		    String otherLastRevStr = xOtherLastRev != null
		            ? Revision.getRevisionTitle(xOtherLastRev) : NO_REVISIONS_STR;
		    message.add("Latest revision in the source system" + " : " + otherLastRevStr);
		}
		return new ChangeLog.ComparisonResult(thisLastRev, xOtherLastRev, thisRevisionFoundInImport, result, message);
	}

	/*Radix::CfgManagement::ChangeLog:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);

		if (src != null) {
		    try (ChangeLogRevisionsCursor revCursor = ChangeLogRevisionsCursor.open((ChangeLog) src)) {
		        while (revCursor.next()) {
		            Revision.importRevision(this, revCursor.rev.exportXml(true));
		        }
		    }
		} else if (revisionFromEditorCreate != null) {
		    Revision.importRevision(this, revisionFromEditorCreate.ChangeLogItem);
		    revisionFromEditorCreate = null;
		}
	}

	/*Radix::CfgManagement::ChangeLog:cleanupExistingChangeLog-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:cleanupExistingChangeLog")
	private final  void cleanupExistingChangeLog (org.radixware.ads.Types.server.Entity ownerFromImport, org.radixware.ads.CfgManagement.server.ChangeLog log, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (log != null) {
		    final boolean thereArePreviouslyImportedRevisions = discardAllNewRevisions(log);
		    if (thereArePreviouslyImportedRevisions && helper != null) {
		        helper.reportWarnings(helper.getContextItem() != null ? helper.getContextItem() : ownerFromImport,
		                "Change log was imported more than once. Probably configuration package contains more than one instance of this object.");
		    }
		    if (!log.isNewObject()) {
		        log.delete();
		    } else {
		        log.discard();
		    }
		}
	}

	/*Radix::CfgManagement::ChangeLog:discardAllNewRevisions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:discardAllNewRevisions")
	private final  boolean discardAllNewRevisions (org.radixware.ads.CfgManagement.server.ChangeLog log) {
		final ChangeLog logParam = log;
		final boolean[] thereAreDiscardedRevisions = new boolean[1];
		Arte::Arte.getInstance().getCache().visitAllNewEntities(null, new Arte::IEntityVisitor() {
		    @Override
		    public void visit(org.radixware.kernel.server.types.Entity e) {
		        if (e instanceof Revision) {
		            final Revision rev = (Revision) e;
		            if (logParam.upDefId.equals(rev.upDefId)
		                    && logParam.upOwnerPid.equals(rev.upOwnerPid)
		                    && logParam.upOwnerEntityId.equals(rev.upOwnerEntityId)) {
		                e.discard();
		                thereAreDiscardedRevisions[0] = true;
		            }
		        }
		    }
		});
		return thereAreDiscardedRevisions[0];
	}

	/*Radix::CfgManagement::ChangeLog:getLastRevisionDate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:getLastRevisionDate")
	public  java.sql.Timestamp getLastRevisionDate () {
		return lastRevisionTime;
	}

	/*Radix::CfgManagement::ChangeLog:getLastRevisionTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:getLastRevisionTitle")
	public  Str getLastRevisionTitle (boolean shortFormat) {
		if (lastRevision != null) {
		    if (shortFormat) {
		        return ChangelogUtils.getPresentationString(lastRevisionSeq, lastRevisionTime.getTime(), null, null, null);
		    } else {
		        return calcTitle();
		    }
		} else {
		    return null;
		}
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::CfgManagement::ChangeLog - Server Meta*/

/*Radix::CfgManagement::ChangeLog-Entity Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ChangeLog_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),"ChangeLog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::CfgManagement::ChangeLog:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
							/*Owner Class Name*/
							"ChangeLog",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),
							/*Property presentations*/

							/*Radix::CfgManagement::ChangeLog:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::CfgManagement::ChangeLog:comments:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::ChangeLog:localNotes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::ChangeLog:lastRevision:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ46UVHNVWNERDD6IULEXEEBUTM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::ChangeLog:lastRevisionAuthor:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEJA7RAR5YBDNXNHKL7QRNVKG6U"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::ChangeLog:lastRevisionTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU67IR74T3JBO7FPAXAF5NPEPBQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::ChangeLog:lastRevisionSeq:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCE2UEVQLLFCOLFACCXZPS263HM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::ChangeLog:revisionFromEditorCreate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDTKMYWB3ZH35NRLGDLU7D6LSE"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::ChangeLog:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::CfgManagement::ChangeLog:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::CfgManagement::ChangeLog:General:Revision-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiINMCKSU3UVEUZNYZISIWVITSVQ"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refHZB6WVXTNVB73FJVURPGTGNCJY"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::ChangeLog:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U"),
									36018,
									null,

									/*Radix::CfgManagement::ChangeLog:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::CfgManagement::ChangeLog:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOQUZHHDWJRFQTFMXTTNFHZTCAU"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOQUZHHDWJRFQTFMXTTNFHZTCAU"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::CfgManagement::ChangeLog:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ46UVHNVWNERDD6IULEXEEBUTM"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28672,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::CfgManagement::ChangeLog:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::ChangeLog:comments-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI"),"comments",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIK2PJUFFQRCXDGQGHBMQAHQ4JQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:localNotes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4"),"localNotes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVFZ2745PF5B23AFMNEQAFBGTFI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:upDefId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYZPLJDVNTRBOZGTGEVP7OFHURE"),"upDefId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:upOwnerEntityId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDNQQV5SM4VAWTCKEOAEDTUXAMI"),"upOwnerEntityId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:upOwnerPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRGHQSIYJKJALXJACD42AGVY2VI"),"upOwnerPid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:lastRevisionId-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCR5WNKD4ZBBNTDIY5MQTCOPOX4"),"lastRevisionId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMWWN2O635AU7BWLXTCQPAOP7U"),org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\" PropId=\"colZQUNS4MKIBCGXE7CSG6PA7ROPU\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>\nfrom </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\"/></xsc:Item><xsc:Item><xsc:Sql>\nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\" PropId=\"colHZU5LSFUFRC23MSA5WTUAAB2YE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ\" PropId=\"colYZPLJDVNTRBOZGTGEVP7OFHURE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\" PropId=\"colZKMFHS6KSBDZFDQSL6XFKD5EZE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ\" PropId=\"colDNQQV5SM4VAWTCKEOAEDTUXAMI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\" PropId=\"colOJDV73QKYVG4PGCAC3UY2G6F74\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ\" PropId=\"colRGHQSIYJKJALXJACD42AGVY2VI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\" PropId=\"colSGRNYKKFMZHS3JWOVWIAQUPNF4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> IN (\n    select max(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\" PropId=\"colSGRNYKKFMZHS3JWOVWIAQUPNF4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>)\n    from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\"/></xsc:Item><xsc:Item><xsc:Sql> \n    where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\" PropId=\"colHZU5LSFUFRC23MSA5WTUAAB2YE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ\" PropId=\"colYZPLJDVNTRBOZGTGEVP7OFHURE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\" PropId=\"colZKMFHS6KSBDZFDQSL6XFKD5EZE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ\" PropId=\"colDNQQV5SM4VAWTCKEOAEDTUXAMI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFBB5BWCUZZHEDICOSYNTHZ3ZNE\" PropId=\"colOJDV73QKYVG4PGCAC3UY2G6F74\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ\" PropId=\"colRGHQSIYJKJALXJACD42AGVY2VI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\n)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:lastRevision-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ46UVHNVWNERDD6IULEXEEBUTM"),"lastRevision",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4JAOZQLUBEHZEE2XKARMPKUII"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:lastRevisionAuthor-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEJA7RAR5YBDNXNHKL7QRNVKG6U"),"lastRevisionAuthor",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6CMLRW5A65DFJO3WVEIW326U4M"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:lastRevisionTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU67IR74T3JBO7FPAXAF5NPEPBQ"),"lastRevisionTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSTVYULHMBFTFKDQY5GCJMP2TU"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:lastRevisionSeq-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCE2UEVQLLFCOLFACCXZPS263HM"),"lastRevisionSeq",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKHQDVERLBC77FMZ6ZETVA5MAE"),org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::ChangeLog:revisionFromEditorCreate-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDTKMYWB3ZH35NRLGDLU7D6LSE"),"revisionFromEditorCreate",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::ChangeLog:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upDefId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYZPLJDVNTRBOZGTGEVP7OFHURE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upOwnerEntityId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDNQQV5SM4VAWTCKEOAEDTUXAMI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upOwnerPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRGHQSIYJKJALXJACD42AGVY2VI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4D6ME6VX45BYVNJXZPBTAA7KP4"),"onCalcTitle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNFA3EOZ6MNA25E7OQBNR4NRXHE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6ASZA2R43BF6NL63G55ZHO5NWE"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEEYKJAYPIZCBHNPAUKGYP4KUUQ"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW6TVNAHJMJG2BD5NSRMZPAIJI4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLNHOVZQDQJBKXG5RJ6FZHGCDLY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3UF5XNL66VFCFBNIG54R4TUTTM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIXJJ5EMOMZGCZEKXGF2CWVLSAM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWMATQMDUIRCLHJ35ENASQ6DXNM"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBPZISEBADNC5XAEHQHRBIJJRGY"),"import",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLO4QT7O6SRHWXCNIEA57USSONQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHW5KC3ZWFJBZ7DPTZ6USTTMCVI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isSet",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMPYDANBVDFBPJA4HWTPGHR37PM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKRI6CH4NRFBHLORVU7YPSNXZII")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBOYTXAH3P5HI7METM4TYB66DSY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVBDRPV2H7FEWVK7YUWSZQPLG2M"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportLocals",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNTK42FJWWVARXJTHWTOTZ3I56E"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLAQBOTJ32JEJ7F3AQNKOMUXT34"),"import",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLO4QT7O6SRHWXCNIEA57USSONQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("existingChangelogOwner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWXSAVBF4BBGRE27OASA7EDB4M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHW5KC3ZWFJBZ7DPTZ6USTTMCVI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isSet",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMPYDANBVDFBPJA4HWTPGHR37PM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKRI6CH4NRFBHLORVU7YPSNXZII")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBOYTXAH3P5HI7METM4TYB66DSY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGFX54ACAH5AQTFHYRYZIVNSGJY"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW6TVNAHJMJG2BD5NSRMZPAIJI4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("existingChangelogOwner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBMOS72ARZRHUNKGIL2NFTKZV4M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLNHOVZQDQJBKXG5RJ6FZHGCDLY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3UF5XNL66VFCFBNIG54R4TUTTM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIXJJ5EMOMZGCZEKXGF2CWVLSAM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMF6ZSJBQ7RGNBF62XJSNAC4Z3A"),"compareChangelogRevisions",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xOther",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKE6VO5HVPRH5HLQ4YO7YQMXDX4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEZXHPQGHXRETDNCT3UU3MX24RA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVLCUFHJAJBA2JGABNM4D7RR2GU"),"cleanupExistingChangeLog",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ownerFromImport",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCLRZLZWAQ5ABVDIOWS2WKEMDHI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("log",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLVSI7K33HJAAPIFYYUQEBBJU2A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7FRBVJJ4BVCULES6MGYKVNNNPM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCI64GA4E4RA43GGNNZISONHHME"),"discardAllNewRevisions",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("log",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSQJ4XNUEKJEEJLN236TFESMXBE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTNS6D2K2IBCAFLGHSPPYX5UJL4"),"getLastRevisionDate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7BQTF7JPJRBB3JJDYL2VSH5CLM"),"getLastRevisionTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("shortFormat",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHDGVGXZTZRDDLBLDISE7SQBH6Y"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcJ7XAYNOPZBHDVK4ZNWNKZCSOW4 = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcJ7XAYNOPZBHDVK4ZNWNKZCSOW4"),"ComparisonResult",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,
						null,
						null,
						null,

						/*Radix::CfgManagement::ChangeLog:ComparisonResult:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::ChangeLog:ComparisonResult:message-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBPUHXKOGXBGZXKPCUZDQPLG3G4"),"message",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,null),

								/*Radix::CfgManagement::ChangeLog:ComparisonResult:result-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCP3CVSCX7RHLTO6LED5MBUPPM4"),"result",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,null),

								/*Radix::CfgManagement::ChangeLog:ComparisonResult:thisRevFoundInImport-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR2DMSNSM3ZDQ5E4XXL3QUUBMF4"),"thisRevFoundInImport",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,null),

								/*Radix::CfgManagement::ChangeLog:ComparisonResult:thisRev-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ6ZGQLPG3JBLXNSMW536IV6YDA"),"thisRev",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),null),

								/*Radix::CfgManagement::ChangeLog:ComparisonResult:otherRev-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGGDRLH3DWJFMPC7AZLHUO24REA"),"otherRev",null,org.radixware.kernel.common.enums.EValType.XML,null,null,null)
						},

						/*Radix::CfgManagement::ChangeLog:ComparisonResult:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO4NR4YHABRAK7FILUDJUAOISNY"),"thereIsConflict",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2RHEM6FJLVE2JBH5K7A3MQC6C4"),"ComparisonResult",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("thisRev",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPDQNW6XR5JENJJ3VIDUMCPZOHA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("otherRev",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNSSHHRJ42JA4XKJS5JLDLWESQ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("thisRevFoundInImport",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr33HISLWE3BDMLAAHJEBBOCICIA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("result",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLGFDSDD74JD53IRNK2RMXPRCEY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDA6KAPBMQRBO7AKRGKAQJA7IXM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2Q5HKSWRHFCFBL6WESBCT3HVNQ"),"getMessage",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYAA47L467JCDXKBWGN73QJYELE"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::CfgManagement::ChangeLog - Desktop Executable*/

/*Radix::CfgManagement::ChangeLog-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog")
public interface ChangeLog {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel )  super.getEntity(i);}
	}

























































	/*Radix::CfgManagement::ChangeLog:comments:comments-Presentation Property*/


	public class Comments extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Comments(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:comments:comments")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:comments:comments")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Comments getComments();
	/*Radix::CfgManagement::ChangeLog:localNotes:localNotes-Presentation Property*/


	public class LocalNotes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LocalNotes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:localNotes:localNotes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:localNotes:localNotes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LocalNotes getLocalNotes();
	/*Radix::CfgManagement::ChangeLog:lastRevisionSeq:lastRevisionSeq-Presentation Property*/


	public class LastRevisionSeq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LastRevisionSeq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionSeq:lastRevisionSeq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionSeq:lastRevisionSeq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LastRevisionSeq getLastRevisionSeq();
	/*Radix::CfgManagement::ChangeLog:lastRevisionAuthor:lastRevisionAuthor-Presentation Property*/


	public class LastRevisionAuthor extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastRevisionAuthor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionAuthor:lastRevisionAuthor")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionAuthor:lastRevisionAuthor")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastRevisionAuthor getLastRevisionAuthor();
	/*Radix::CfgManagement::ChangeLog:lastRevision:lastRevision-Presentation Property*/


	public class LastRevision extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public LastRevision(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.Revision.Revision_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.Revision.Revision_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.Revision.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.CfgManagement.explorer.Revision.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevision:lastRevision")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevision:lastRevision")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public LastRevision getLastRevision();
	/*Radix::CfgManagement::ChangeLog:revisionFromEditorCreate:revisionFromEditorCreate-Presentation Property*/


	public class RevisionFromEditorCreate extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public RevisionFromEditorCreate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.commondef.ChangeLogItemDocument> getValClass(){
			return org.radixware.schemas.commondef.ChangeLogItemDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.commondef.ChangeLogItemDocument dummy = x == null ? null : (org.radixware.schemas.commondef.ChangeLogItemDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.commondef.ChangeLogItemDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:revisionFromEditorCreate:revisionFromEditorCreate")
		public  org.radixware.schemas.commondef.ChangeLogItemDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:revisionFromEditorCreate:revisionFromEditorCreate")
		public   void setValue(org.radixware.schemas.commondef.ChangeLogItemDocument val) {
			Value = val;
		}
	}
	public RevisionFromEditorCreate getRevisionFromEditorCreate();
	/*Radix::CfgManagement::ChangeLog:lastRevisionTime:lastRevisionTime-Presentation Property*/


	public class LastRevisionTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastRevisionTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionTime:lastRevisionTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionTime:lastRevisionTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastRevisionTime getLastRevisionTime();


}

/* Radix::CfgManagement::ChangeLog - Desktop Meta*/

/*Radix::CfgManagement::ChangeLog-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ChangeLog_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::ChangeLog:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
			"Radix::CfgManagement::ChangeLog",
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOQUZHHDWJRFQTFMXTTNFHZTCAU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRBVCFS3GNHPPI3EMU5LMPJGOU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),28672,

			/*Radix::CfgManagement::ChangeLog:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::CfgManagement::ChangeLog:comments:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI"),
						"comments",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIK2PJUFFQRCXDGQGHBMQAHQ4JQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
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

						/*Radix::CfgManagement::ChangeLog:comments:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:localNotes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4"),
						"localNotes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVFZ2745PF5B23AFMNEQAFBGTFI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
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

						/*Radix::CfgManagement::ChangeLog:localNotes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:lastRevision:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ46UVHNVWNERDD6IULEXEEBUTM"),
						"lastRevision",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
						133693439,
						133693439,false),

					/*Radix::CfgManagement::ChangeLog:lastRevisionAuthor:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEJA7RAR5YBDNXNHKL7QRNVKG6U"),
						"lastRevisionAuthor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6CMLRW5A65DFJO3WVEIW326U4M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
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

						/*Radix::CfgManagement::ChangeLog:lastRevisionAuthor:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:lastRevisionTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU67IR74T3JBO7FPAXAF5NPEPBQ"),
						"lastRevisionTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSTVYULHMBFTFKDQY5GCJMP2TU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::CfgManagement::ChangeLog:lastRevisionTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:lastRevisionSeq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCE2UEVQLLFCOLFACCXZPS263HM"),
						"lastRevisionSeq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKHQDVERLBC77FMZ6ZETVA5MAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::CfgManagement::ChangeLog:lastRevisionSeq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:revisionFromEditorCreate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDTKMYWB3ZH35NRLGDLU7D6LSE"),
						"revisionFromEditorCreate",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refDSVLIJJASRC2JF2ITJB2ILWBUE"),"ChangeLog=>UpValRef (upDefId=>defId, upOwnerEntityId=>ownerEntityId, upOwnerPid=>ownerPid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZVJUSFASRDNBDCBEABIFNQAABA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colYZPLJDVNTRBOZGTGEVP7OFHURE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colDNQQV5SM4VAWTCKEOAEDTUXAMI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colRGHQSIYJKJALXJACD42AGVY2VI")},new String[]{"upDefId","upOwnerEntityId","upOwnerPid"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BJUSFASRDNBDCBEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJUSFASRDNBDCBEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JJUSFASRDNBDCBEABIFNQAABA")},new String[]{"defId","ownerEntityId","ownerPid"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOQUZHHDWJRFQTFMXTTNFHZTCAU")},
			false,false,false);
}

/* Radix::CfgManagement::ChangeLog - Web Executable*/

/*Radix::CfgManagement::ChangeLog-Entity Class*/

package org.radixware.ads.CfgManagement.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog")
public interface ChangeLog {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.CfgManagement.web.ChangeLog.ChangeLog_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.CfgManagement.web.ChangeLog.ChangeLog_DefaultModel )  super.getEntity(i);}
	}

























































	/*Radix::CfgManagement::ChangeLog:comments:comments-Presentation Property*/


	public class Comments extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Comments(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:comments:comments")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:comments:comments")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Comments getComments();
	/*Radix::CfgManagement::ChangeLog:localNotes:localNotes-Presentation Property*/


	public class LocalNotes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LocalNotes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:localNotes:localNotes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:localNotes:localNotes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LocalNotes getLocalNotes();
	/*Radix::CfgManagement::ChangeLog:lastRevisionSeq:lastRevisionSeq-Presentation Property*/


	public class LastRevisionSeq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LastRevisionSeq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionSeq:lastRevisionSeq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionSeq:lastRevisionSeq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LastRevisionSeq getLastRevisionSeq();
	/*Radix::CfgManagement::ChangeLog:lastRevisionAuthor:lastRevisionAuthor-Presentation Property*/


	public class LastRevisionAuthor extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastRevisionAuthor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionAuthor:lastRevisionAuthor")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionAuthor:lastRevisionAuthor")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastRevisionAuthor getLastRevisionAuthor();
	/*Radix::CfgManagement::ChangeLog:lastRevision:lastRevision-Presentation Property*/


	public class LastRevision extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public LastRevision(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.web.Revision.Revision_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.web.Revision.Revision_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.web.Revision.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.CfgManagement.web.Revision.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevision:lastRevision")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevision:lastRevision")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public LastRevision getLastRevision();
	/*Radix::CfgManagement::ChangeLog:revisionFromEditorCreate:revisionFromEditorCreate-Presentation Property*/


	public class RevisionFromEditorCreate extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public RevisionFromEditorCreate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.commondef.ChangeLogItemDocument> getValClass(){
			return org.radixware.schemas.commondef.ChangeLogItemDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.commondef.ChangeLogItemDocument dummy = x == null ? null : (org.radixware.schemas.commondef.ChangeLogItemDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.commondef.ChangeLogItemDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:revisionFromEditorCreate:revisionFromEditorCreate")
		public  org.radixware.schemas.commondef.ChangeLogItemDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:revisionFromEditorCreate:revisionFromEditorCreate")
		public   void setValue(org.radixware.schemas.commondef.ChangeLogItemDocument val) {
			Value = val;
		}
	}
	public RevisionFromEditorCreate getRevisionFromEditorCreate();
	/*Radix::CfgManagement::ChangeLog:lastRevisionTime:lastRevisionTime-Presentation Property*/


	public class LastRevisionTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastRevisionTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionTime:lastRevisionTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:lastRevisionTime:lastRevisionTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastRevisionTime getLastRevisionTime();


}

/* Radix::CfgManagement::ChangeLog - Web Meta*/

/*Radix::CfgManagement::ChangeLog-Entity Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ChangeLog_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::ChangeLog:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
			"Radix::CfgManagement::ChangeLog",
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOQUZHHDWJRFQTFMXTTNFHZTCAU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRBVCFS3GNHPPI3EMU5LMPJGOU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),28672,

			/*Radix::CfgManagement::ChangeLog:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::CfgManagement::ChangeLog:comments:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI"),
						"comments",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIK2PJUFFQRCXDGQGHBMQAHQ4JQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
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

						/*Radix::CfgManagement::ChangeLog:comments:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:localNotes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4"),
						"localNotes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVFZ2745PF5B23AFMNEQAFBGTFI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
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

						/*Radix::CfgManagement::ChangeLog:localNotes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:lastRevision:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ46UVHNVWNERDD6IULEXEEBUTM"),
						"lastRevision",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
						133693439,
						133693439,false),

					/*Radix::CfgManagement::ChangeLog:lastRevisionAuthor:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEJA7RAR5YBDNXNHKL7QRNVKG6U"),
						"lastRevisionAuthor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6CMLRW5A65DFJO3WVEIW326U4M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
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

						/*Radix::CfgManagement::ChangeLog:lastRevisionAuthor:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:lastRevisionTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU67IR74T3JBO7FPAXAF5NPEPBQ"),
						"lastRevisionTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSTVYULHMBFTFKDQY5GCJMP2TU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::CfgManagement::ChangeLog:lastRevisionTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:lastRevisionSeq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCE2UEVQLLFCOLFACCXZPS263HM"),
						"lastRevisionSeq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKHQDVERLBC77FMZ6ZETVA5MAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::CfgManagement::ChangeLog:lastRevisionSeq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::ChangeLog:revisionFromEditorCreate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDTKMYWB3ZH35NRLGDLU7D6LSE"),
						"revisionFromEditorCreate",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refDSVLIJJASRC2JF2ITJB2ILWBUE"),"ChangeLog=>UpValRef (upDefId=>defId, upOwnerEntityId=>ownerEntityId, upOwnerPid=>ownerPid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZVJUSFASRDNBDCBEABIFNQAABA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colYZPLJDVNTRBOZGTGEVP7OFHURE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colDNQQV5SM4VAWTCKEOAEDTUXAMI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colRGHQSIYJKJALXJACD42AGVY2VI")},new String[]{"upDefId","upOwnerEntityId","upOwnerPid"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BJUSFASRDNBDCBEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJUSFASRDNBDCBEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JJUSFASRDNBDCBEABIFNQAABA")},new String[]{"defId","ownerEntityId","ownerPid"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOQUZHHDWJRFQTFMXTTNFHZTCAU")},
			false,false,false);
}

/* Radix::CfgManagement::ChangeLog:General - Desktop Meta*/

/*Radix::CfgManagement::ChangeLog:General-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
	null,
	null,

	/*Radix::CfgManagement::ChangeLog:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::ChangeLog:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNU6TVQJORNANLCYQ4ZWMWJRHWY"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWTXBOXADUVEEFD6S3UZNJ6YZ54"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEJA7RAR5YBDNXNHKL7QRNVKG6U"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU67IR74T3JBO7FPAXAF5NPEPBQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCE2UEVQLLFCOLFACCXZPS263HM"),0,0,1,false,false)
			},null),

			/*Radix::CfgManagement::ChangeLog:General:Revisions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVV3R7EYDNJENJMRFH6V5U2YKUM"),"Revisions",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWDKRWN232JAJHAVKOLGHH32BBY"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiINMCKSU3UVEUZNYZISIWVITSVQ"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNU6TVQJORNANLCYQ4ZWMWJRHWY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVV3R7EYDNJENJMRFH6V5U2YKUM"))}
	,

	/*Radix::CfgManagement::ChangeLog:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::CfgManagement::ChangeLog:General:Revision-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiINMCKSU3UVEUZNYZISIWVITSVQ"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::CfgManagement::ChangeLog:General - Web Meta*/

/*Radix::CfgManagement::ChangeLog:General-Editor Presentation*/

package org.radixware.ads.CfgManagement.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
	null,
	null,

	/*Radix::CfgManagement::ChangeLog:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::ChangeLog:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNU6TVQJORNANLCYQ4ZWMWJRHWY"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWTXBOXADUVEEFD6S3UZNJ6YZ54"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEJA7RAR5YBDNXNHKL7QRNVKG6U"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU67IR74T3JBO7FPAXAF5NPEPBQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCE2UEVQLLFCOLFACCXZPS263HM"),0,0,1,false,false)
			},null),

			/*Radix::CfgManagement::ChangeLog:General:Revisions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVV3R7EYDNJENJMRFH6V5U2YKUM"),"Revisions",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWDKRWN232JAJHAVKOLGHH32BBY"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiINMCKSU3UVEUZNYZISIWVITSVQ"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNU6TVQJORNANLCYQ4ZWMWJRHWY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVV3R7EYDNJENJMRFH6V5U2YKUM"))}
	,

	/*Radix::CfgManagement::ChangeLog:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::CfgManagement::ChangeLog:General:Revision-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiINMCKSU3UVEUZNYZISIWVITSVQ"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7KE3MVJCXBEMVBY2IOQ27CH3FU"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::CfgManagement::ChangeLog:General:Model - Desktop Executable*/

/*Radix::CfgManagement::ChangeLog:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:General:Model")
public class General:Model  extends org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::ChangeLog:General:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::ChangeLog:General:Model:Properties-Properties*/

	/*Radix::CfgManagement::ChangeLog:General:Model:Methods-Methods*/


}

/* Radix::CfgManagement::ChangeLog:General:Model - Desktop Meta*/

/*Radix::CfgManagement::ChangeLog:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemVRZPY6RJKBHWFLK46ATZLXFW3U"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::ChangeLog:General:Model:Properties-Properties*/
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

/* Radix::CfgManagement::ChangeLog:General:Model - Web Executable*/

/*Radix::CfgManagement::ChangeLog:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:General:Model")
public class General:Model  extends org.radixware.ads.CfgManagement.web.ChangeLog.ChangeLog_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::ChangeLog:General:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::ChangeLog:General:Model:Properties-Properties*/

	/*Radix::CfgManagement::ChangeLog:General:Model:Methods-Methods*/


}

/* Radix::CfgManagement::ChangeLog:General:Model - Web Meta*/

/*Radix::CfgManagement::ChangeLog:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemVRZPY6RJKBHWFLK46ATZLXFW3U"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::ChangeLog:General:Model:Properties-Properties*/
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

/* Radix::CfgManagement::ChangeLog:Create - Desktop Meta*/

/*Radix::CfgManagement::ChangeLog:Create-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
	null,
	null,

	/*Radix::CfgManagement::ChangeLog:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::ChangeLog:Create:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNU6TVQJORNANLCYQ4ZWMWJRHWY"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepM5Q7MYLM4ZDV5C5EUKMPAWO3FA"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNU6TVQJORNANLCYQ4ZWMWJRHWY"))}
	,

	/*Radix::CfgManagement::ChangeLog:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36018,0,0);
}
/* Radix::CfgManagement::ChangeLog:Create - Web Meta*/

/*Radix::CfgManagement::ChangeLog:Create-Editor Presentation*/

package org.radixware.ads.CfgManagement.web;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
	null,
	null,

	/*Radix::CfgManagement::ChangeLog:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::ChangeLog:Create:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNU6TVQJORNANLCYQ4ZWMWJRHWY"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepHAEPRHXOCFBWRN3GVQC3ZYD4JU"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNU6TVQJORNANLCYQ4ZWMWJRHWY"))}
	,

	/*Radix::CfgManagement::ChangeLog:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36018,0,0);
}
/* Radix::CfgManagement::ChangeLog:Create:Model - Desktop Executable*/

/*Radix::CfgManagement::ChangeLog:Create:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:Create:Model")
public class Create:Model  extends org.radixware.ads.CfgManagement.explorer.General:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::ChangeLog:Create:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::ChangeLog:Create:Model:Properties-Properties*/

	/*Radix::CfgManagement::ChangeLog:Create:Model:Methods-Methods*/

	/*Radix::CfgManagement::ChangeLog:Create:Model:afterActivateEditorPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:Create:Model:afterActivateEditorPage")
	public published  void afterActivateEditorPage (org.radixware.kernel.common.types.Id pageId) {
		super.afterActivateEditorPage(pageId);

		if (idof[ChangeLog:Create:General] == pageId && revisionFromEditorCreate.Value != null) {
		    Revision rev = (Revision) ChangeLog:Create:General:View:EditorPageView:groupBox:embeddedEditor.getModel();
		    DesktopUtils.importRevisionFromXml(rev, revisionFromEditorCreate.Value.ChangeLogItem);
		}
	}

	/*Radix::CfgManagement::ChangeLog:Create:Model:finishEdit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:Create:Model:finishEdit")
	public published  void finishEdit () {
		super.finishEdit();

		Revision rev = (Revision) ChangeLog:Create:General:View:EditorPageView:groupBox:embeddedEditor.getModel();
		Types::CommonDefXsd:ChangeLogItemDocument xRev = Types::CommonDefXsd:ChangeLogItemDocument.Factory.newInstance();
		xRev.setChangeLogItem(DesktopUtils.exportRevision(rev, true));
		revisionFromEditorCreate.Value = xRev;
	}







}

/* Radix::CfgManagement::ChangeLog:Create:Model - Desktop Meta*/

/*Radix::CfgManagement::ChangeLog:Create:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem6HHEKF43V5BUPOUOE7WW7JP2PU"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemVRZPY6RJKBHWFLK46ATZLXFW3U"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::ChangeLog:Create:Model:Properties-Properties*/
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

/* Radix::CfgManagement::ChangeLog:Create:Model - Web Executable*/

/*Radix::CfgManagement::ChangeLog:Create:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:Create:Model")
public class Create:Model  extends org.radixware.ads.CfgManagement.web.General:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::ChangeLog:Create:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::ChangeLog:Create:Model:Properties-Properties*/

	/*Radix::CfgManagement::ChangeLog:Create:Model:Methods-Methods*/

	/*Radix::CfgManagement::ChangeLog:Create:Model:afterActivateEditorPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:Create:Model:afterActivateEditorPage")
	public published  void afterActivateEditorPage (org.radixware.kernel.common.types.Id pageId) {
		super.afterActivateEditorPage(pageId);

	}

	/*Radix::CfgManagement::ChangeLog:Create:Model:finishEdit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:Create:Model:finishEdit")
	public published  void finishEdit () {
		super.finishEdit();
	}







}

/* Radix::CfgManagement::ChangeLog:Create:Model - Web Meta*/

/*Radix::CfgManagement::ChangeLog:Create:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem6HHEKF43V5BUPOUOE7WW7JP2PU"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemVRZPY6RJKBHWFLK46ATZLXFW3U"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::ChangeLog:Create:Model:Properties-Properties*/
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

/* Radix::CfgManagement::ChangeLog:Create:General:View - Desktop Executable*/

/*Radix::CfgManagement::ChangeLog:Create:General:View-Custom Page Editor for Desktop*/

package org.radixware.ads.CfgManagement.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:Create:General:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View EditorPageView;
	public View getEditorPageView(){ return EditorPageView;}
	public org.radixware.kernel.explorer.widgets.PropLabel commentsPropLabel;
	public org.radixware.kernel.explorer.widgets.PropLabel getCommentsPropLabel(){ return commentsPropLabel;}
	public org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor commentsPropEditor;
	public org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor getCommentsPropEditor(){ return commentsPropEditor;}
	public org.radixware.kernel.explorer.widgets.PropLabel localNotesPropLabel;
	public org.radixware.kernel.explorer.widgets.PropLabel getLocalNotesPropLabel(){ return localNotesPropLabel;}
	public org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor localNotesPropEditor;
	public org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor getLocalNotesPropEditor(){ return localNotesPropEditor;}
	public com.trolltech.qt.gui.QGroupBox groupBox;
	public com.trolltech.qt.gui.QGroupBox getGroupBox(){ return groupBox;}
	public org.radixware.kernel.explorer.widgets.EmbeddedEditor embeddedEditor;
	public org.radixware.kernel.explorer.widgets.EmbeddedEditor getEmbeddedEditor(){ return embeddedEditor;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		EditorPageView = this;
		EditorPageView.setObjectName("EditorPageView");
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 600, 400));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QGridLayout $layout1 = new com.trolltech.qt.gui.QGridLayout(EditorPageView);
		$layout1.setObjectName("gridLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setHorizontalSpacing(6);
		$layout1.setVerticalSpacing(6);
		commentsPropLabel = new org.radixware.kernel.explorer.widgets.PropLabel(EditorPageView);
		commentsPropLabel.setObjectName("commentsPropLabel");
		commentsPropLabel.setProperty(model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI")));
		commentsPropLabel.setObjectName("commentsPropLabel");
		commentsPropLabel.setSizePolicy(new com.trolltech.qt.gui.QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Maximum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed));
		commentsPropLabel.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 100, 20));
		$layout1.addWidget(commentsPropLabel, 0, 0, 1, 1);
		commentsPropEditor = (org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor)model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI")).createPropertyEditor();
		commentsPropEditor.setParent(EditorPageView);
		commentsPropEditor.bind();
		commentsPropEditor.setObjectName("commentsPropEditor");
		commentsPropEditor.setObjectName("commentsPropEditor");
		commentsPropEditor.setSizePolicy(new com.trolltech.qt.gui.QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed));
		commentsPropEditor.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 100, 20));
		$layout1.addWidget(commentsPropEditor, 0, 1, 1, 1);
		localNotesPropLabel = new org.radixware.kernel.explorer.widgets.PropLabel(EditorPageView);
		localNotesPropLabel.setObjectName("localNotesPropLabel");
		localNotesPropLabel.setProperty(model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4")));
		localNotesPropLabel.setObjectName("localNotesPropLabel");
		localNotesPropLabel.setSizePolicy(new com.trolltech.qt.gui.QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Maximum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed));
		localNotesPropLabel.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 100, 20));
		$layout1.addWidget(localNotesPropLabel, 1, 0, 1, 1);
		localNotesPropEditor = (org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor)model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4")).createPropertyEditor();
		localNotesPropEditor.setParent(EditorPageView);
		localNotesPropEditor.bind();
		localNotesPropEditor.setObjectName("localNotesPropEditor");
		localNotesPropEditor.setObjectName("localNotesPropEditor");
		localNotesPropEditor.setSizePolicy(new com.trolltech.qt.gui.QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed));
		localNotesPropEditor.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 100, 20));
		$layout1.addWidget(localNotesPropEditor, 1, 1, 1, 1);
		com.trolltech.qt.gui.QSpacerItem $spacer1 = new com.trolltech.qt.gui.QSpacerItem(20, 40, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);
		$layout1.addItem($spacer1, 3, 0, 1, 1);
		groupBox = new com.trolltech.qt.gui.QGroupBox(EditorPageView);
		groupBox.setObjectName("groupBox");
		groupBox.setTitle(org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(View.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEILDD3JE4VCM5GUXOEPQAIVKJY")));
		groupBox.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QVBoxLayout $layout2 = new com.trolltech.qt.gui.QVBoxLayout(groupBox);
		$layout2.setObjectName("verticalLayout");
		$layout2.setContentsMargins(1, 1, 1, 1);
		$layout2.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout2.setSpacing(6);
		embeddedEditor = new org.radixware.kernel.explorer.widgets.EmbeddedEditor(model.getEnvironment(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFBB5BWCUZZHEDICOSYNTHZ3ZNE"), org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOJBSAEAAPZCHTMSSKK6XCMO2XI"), null);
		embeddedEditor.setParent(groupBox);
		embeddedEditor.setObjectName("embeddedEditor");
		embeddedEditor.setSynchronizedWithParentView(true);
		embeddedEditor.setSizePolicy(new com.trolltech.qt.gui.QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Preferred, com.trolltech.qt.gui.QSizePolicy.Policy.Maximum));
		embeddedEditor.setFont(DEFAULT_FONT);
		embeddedEditor.bind();
		$layout2.addWidget(embeddedEditor);
		$layout1.addWidget(groupBox, 2, 0, 1, 2);
		opened.emit(this);
	}
	public final org.radixware.ads.CfgManagement.explorer.Create:Model getModel() {
		return (org.radixware.ads.CfgManagement.explorer.Create:Model) super.getModel();
	}

}

/* Radix::CfgManagement::ChangeLog:Create:General:WebView - Web Executable*/

/*Radix::CfgManagement::ChangeLog:Create:General:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.CfgManagement.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ChangeLog:Create:General:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public org.radixware.wps.views.editor.PropertiesGrid widget;
	public org.radixware.wps.views.editor.PropertiesGrid getWidget(){ return  widget;}
	public org.radixware.wps.views.editor.property.AbstractPropEditor commentsWidget;
	public org.radixware.wps.views.editor.property.AbstractPropEditor getCommentsWidget(){ return  commentsWidget;}
	public org.radixware.wps.views.editor.property.AbstractPropEditor localNotesWidget;
	public org.radixware.wps.views.editor.property.AbstractPropEditor getLocalNotesWidget(){ return  localNotesWidget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::CfgManagement::ChangeLog:Create:General:WebView:widget ==============
		this.widget = this;
		widget.setWidth(600);
		widget.setHeight(400);
		//============ Radix::CfgManagement::ChangeLog:Create:General:WebView:widget:widget ==============
		this.widget = new org.radixware.wps.views.editor.PropertiesGrid();
		widget.setObjectName("widget");
		this.widget.add(this.widget);
		//============ Radix::CfgManagement::ChangeLog:Create:General:WebView:widget:widget:commentsWidget ==============
		this.commentsWidget = this.widget.addProperty(model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI")),0,0,1,true,false);
		commentsWidget.setObjectName("commentsWidget");
		commentsWidget.setObjectName("commentsWidget");
		commentsWidget.setHeight(20);
		//============ Radix::CfgManagement::ChangeLog:Create:General:WebView:widget:widget:localNotesWidget ==============
		this.localNotesWidget = this.widget.addProperty(model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4")),0,1,1,true,false);
		localNotesWidget.setObjectName("localNotesWidget");
		localNotesWidget.setObjectName("localNotesWidget");
		localNotesWidget.setHeight(20);
		widget.getAnchors().setTop(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setLeft(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setBottom(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		widget.getAnchors().setRight(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		this.wdgZADERSWEONH4BN3RCV2Y3ZA3KI.bind();
		this.wdgGFIOA6WTKJEM7BUZGMNIEDDXWE.bind();
		this.wdgI2XNQ3UXG5EYFFFG7KCOLK347I.bind();
		fireOpened();
	}
}

/* Radix::CfgManagement::ChangeLog:General - Desktop Meta*/

/*Radix::CfgManagement::ChangeLog:General-Selector Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOQUZHHDWJRFQTFMXTTNFHZTCAU"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.explorer.ChangeLog.DefaultGroupModel(userSession,this);
	}
}
/* Radix::CfgManagement::ChangeLog:General - Web Meta*/

/*Radix::CfgManagement::ChangeLog:General-Selector Presentation*/

package org.radixware.ads.CfgManagement.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOQUZHHDWJRFQTFMXTTNFHZTCAU"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKVXCQA3IHRH5FGGABFJK3CS6FI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMR7O6HVK7VHNXK56NUE43I2MK4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.web.ChangeLog.DefaultGroupModel(userSession,this);
	}
}
/* Radix::CfgManagement::ChangeLog - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ChangeLog - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last revision updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор последней ревизии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6CMLRW5A65DFJO3WVEIW326U4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Change log was imported more than once. Probably configuration package contains more than one instance of this object.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Журнал событий был импортирован более одного раза. Вероятно, пакет конфигурации содержит более одного экземпляра текущего объекта.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OHK5NSBBNABFGKJ4A5F274Z7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Latest revision in this system");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последняя ревизия этой системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCARV2ESUTZCODOQRNM2NWX5CO4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"First Revision");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Первая ревизия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEILDD3JE4VCM5GUXOEPQAIVKJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Change log");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Журнал изменений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Changelogs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Журналы изменений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRBVCFS3GNHPPI3EMU5LMPJGOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Comment");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Комментарии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIK2PJUFFQRCXDGQGHBMQAHQ4JQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last revision number");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Номер последней ревизии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMWWN2O635AU7BWLXTCQPAOP7U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Latest revision in the source system");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последняя ревизия системы-источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKAHWPOANDJEAJPV4TDUOGFRCE4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Change log");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Лог изменений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOB2W3ZQ575ATJKTPYOGIWCJYSI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last revision updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дата последней ревизии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSTVYULHMBFTFKDQY5GCJMP2TU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Changelogs are equal");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Журналы событий равны");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRS5I6R2AXBFMDB4CQ2DBS3C42Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<no revisions>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет ревизий>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUOTBAP3IG5BYJASZIMHTFFCKKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Local notes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Локальные заметки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVFZ2745PF5B23AFMNEQAFBGTFI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Revisions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ревизии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWDKRWN232JAJHAVKOLGHH32BBY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWTXBOXADUVEEFD6S3UZNJ6YZ54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last revision number");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Номер последней ревизии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKHQDVERLBC77FMZ6ZETVA5MAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Change log conflict, latest revision available in this system is absent in the change log being imported:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конфликт Журналов событий, последняя ревизия данной системы отсутствует в импортируемом Журнале событий:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZL5FQQQLMVGHRHBEM7V73XDZXU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ChangeLog - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),"ChangeLog - Localizing Bundle",$$$items$$$);
}

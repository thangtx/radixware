
/* Radix::CfgManagement::CfgImportHelper.Base - Server Executable*/

/*Radix::CfgManagement::CfgImportHelper.Base-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;




@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base")
public abstract published class CfgImportHelper.Base  implements org.radixware.ads.CfgManagement.server.ICfgImportHelper,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgImportHelper.Base_mi.rdxMeta;}

	/*Radix::CfgManagement::CfgImportHelper.Base:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec")
	 static class ResultRec  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgImportHelper.Base_mi.rdxMeta_adcVDTJPUJICREKZMJHVBLETZLIJI;}

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:Properties-Properties*/

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objTitle-Dynamic Property*/



		protected Str objTitle=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objTitle")
		  Str getObjTitle() {
			return objTitle;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objTitle")
		   void setObjTitle(Str val) {
			objTitle = val;
		}

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:kind-Dynamic Property*/



		protected org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultKind kind=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:kind")
		  org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultKind getKind() {
			return kind;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:kind")
		   void setKind(org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultKind val) {
			kind = val;
		}

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:warnings-Dynamic Property*/



		protected java.util.List<Str> warnings=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:warnings")
		  java.util.List<Str> getWarnings() {
			return warnings;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:warnings")
		   void setWarnings(java.util.List<Str> val) {
			warnings = val;
		}

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:newExtIdGenerated-Dynamic Property*/



		protected boolean newExtIdGenerated=false;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:newExtIdGenerated")
		public  boolean getNewExtIdGenerated() {
			return newExtIdGenerated;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:newExtIdGenerated")
		public   void setNewExtIdGenerated(boolean val) {
			newExtIdGenerated = val;
		}

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objPid-Dynamic Property*/



		protected org.radixware.kernel.server.types.Pid objPid=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objPid")
		  org.radixware.kernel.server.types.Pid getObjPid() {
			return objPid;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objPid")
		   void setObjPid(org.radixware.kernel.server.types.Pid val) {
			objPid = val;
		}

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objExtGuid-Dynamic Property*/



		protected Str objExtGuid=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objExtGuid")
		  Str getObjExtGuid() {
			return objExtGuid;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objExtGuid")
		   void setObjExtGuid(Str val) {
			objExtGuid = val;
		}

































































		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:Methods-Methods*/

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:getMessage-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:getMessage")
		public  Str getMessage (boolean isFullMessage) {
			switch (kind) {
			    case ResultKind.NEW:
			        return newExtIdGenerated
			                ? "Object with the same ID already exists. Object with new ID created"
			                : "New object created";
			    case ResultKind.UPDATED:
			        return "Updated";
			    case ResultKind.DELETED:
			        return "Deleted";
			    case ResultKind.CANCELLED:
			        return "Import canceled";
			    case ResultKind.WARNINGS:
			    case ResultKind.PROPS_NOT_SET:
			        final String begMessage = kind == ResultKind.PROPS_NOT_SET
			                    ? "Properties not set:" : "Warnings:";
			        if (isFullMessage) {
			            final java.lang.StringBuilder mess = new java.lang.StringBuilder();
			            mess.append(begMessage);
			            for (Str p : warnings) {
			                if (!p.isEmpty()) {
			                    mess.append(' ').append(p);
			                    final char lastChar = p.charAt(p.length() - 1);
			                    final boolean appendSemicolon = !isPunctuation(lastChar);
			                    if (appendSemicolon) {
			                        mess.append(';');
			                    }
			                }
			            }
			            if (isPunctuation(mess.charAt(mess.length() - 1))) {
			                mess.setCharAt(mess.length() - 1, '.');
			            }
			            return mess.toString();
			        } else {
			            return begMessage;
			        }
			    default:
			        throw new IllegalArgumentException("Unknown item kind: " + kind);
			}
		}

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:ResultRec-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:ResultRec")
		public  ResultRec (Str extGuid, org.radixware.kernel.server.types.Pid objPid, Str objTitle, org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultKind kind, java.util.List<Str> warnings) {
			objExtGuid = extGuid;
			objPid = objPid;
			objTitle = objTitle;
			kind = kind;
			warnings = warnings;
		}

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:isPunctuation-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultRec:isPunctuation")
		private final  boolean isPunctuation (char ch) {
			return ch == '.' || ch == ',' || ch == ':' || ch == ';';
		}


	}

	/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind-Server Enum Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ResultKind")
	public enum ResultKind  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:Items-Fields*/

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:UPDATED-Enum Class Field*/

		aefXMDHFT4DZBB6LJDMCHY6APBNSI,

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:NEW-Enum Class Field*/

		aefYDZZWLPGHRAGDHFUEBVWBMDM4Y,

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:CANCELLED-Enum Class Field*/

		aefJ4RQPE5MRVAPVGYP6DNVZ6DGOQ,

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:WARNINGS-Enum Class Field*/

		aef5GLGXHTC2BFOHMNKWHJRI42LGI,

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:PROPS_NOT_SET-Enum Class Field*/

		aefWLD35SMHFNEUZE26YYDTWM77SY,

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:DELETED-Enum Class Field*/

		aef27D6MMVHFNGJBCJHQILQVHHL3M;



		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgImportHelper.Base_mi.rdxMeta_aetK3SPY2LE4VBNRB5547FV2QCQ5Y;}

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:Properties-Properties*/





























		/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:Methods-Methods*/


	}

	/*Radix::CfgManagement::CfgImportHelper.Base:ChangeLogImportHelperImpl-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ChangeLogImportHelperImpl")
	private final class ChangeLogImportHelperImpl  implements org.radixware.ads.CfgManagement.server.ICfgChangeLogImportHelper  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgImportHelper.Base_mi.rdxMeta_adcIU4DAPZVHVDEZDXX3QLWX63QGA;}

		/*Radix::CfgManagement::CfgImportHelper.Base:ChangeLogImportHelperImpl:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgImportHelper.Base:ChangeLogImportHelperImpl:Properties-Properties*/

		/*Radix::CfgManagement::CfgImportHelper.Base:ChangeLogImportHelperImpl:Methods-Methods*/

		/*Radix::CfgManagement::CfgImportHelper.Base:ChangeLogImportHelperImpl:registerChangeLogLastRevMess-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ChangeLogImportHelperImpl:registerChangeLogLastRevMess")
		public published  void registerChangeLogLastRevMess (org.radixware.kernel.server.types.Pid ownerPid, Str mess) {
			CfgImportHelper.Base.this.changelogLastRevMessByOwnerPid.put(ownerPid, mess);
		}


	}

	/*Radix::CfgManagement::CfgImportHelper.Base:Properties-Properties*/

	/*Radix::CfgManagement::CfgImportHelper.Base:results-Dynamic Property*/



	protected java.util.List<org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultRec> results=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:results")
	private final  java.util.List<org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultRec> getResults() {
		return results;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:results")
	private final   void setResults(java.util.List<org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultRec> val) {
		results = val;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:settings-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.common.ICfgImportSettings settings=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:settings")
	private final  org.radixware.ads.CfgManagement.common.ICfgImportSettings getSettings() {
		return settings;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:settings")
	private final   void setSettings(org.radixware.ads.CfgManagement.common.ICfgImportSettings val) {
		settings = val;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:contextItem-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.CfgItem contextItem=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:contextItem")
	private final  org.radixware.ads.CfgManagement.server.CfgItem getContextItem() {
		return contextItem;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:contextItem")
	private final   void setContextItem(org.radixware.ads.CfgManagement.server.CfgItem val) {
		contextItem = val;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:contextPacket-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.CfgPacket contextPacket=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:contextPacket")
	private final  org.radixware.ads.CfgManagement.server.CfgPacket getContextPacket() {
		return contextPacket;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:contextPacket")
	private final   void setContextPacket(org.radixware.ads.CfgManagement.server.CfgPacket val) {
		contextPacket = val;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:ufHelper-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.CfgUserFuncImportHelper ufHelper=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ufHelper")
	private final  org.radixware.ads.CfgManagement.server.CfgUserFuncImportHelper getUfHelper() {
		return ufHelper;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:ufHelper")
	private final   void setUfHelper(org.radixware.ads.CfgManagement.server.CfgUserFuncImportHelper val) {
		ufHelper = val;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:defaultActionIfObjExists-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.ICfgImportHelper.Action defaultActionIfObjExists=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:defaultActionIfObjExists")
	private final  org.radixware.ads.CfgManagement.server.ICfgImportHelper.Action getDefaultActionIfObjExists() {
		return defaultActionIfObjExists;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:defaultActionIfObjExists")
	private final   void setDefaultActionIfObjExists(org.radixware.ads.CfgManagement.server.ICfgImportHelper.Action val) {
		defaultActionIfObjExists = val;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:progressDlg-Dynamic Property*/



	protected org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process progressDlg=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:progressDlg")
	private final  org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process getProgressDlg() {
		return progressDlg;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:progressDlg")
	private final   void setProgressDlg(org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process val) {
		progressDlg = val;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:changelogLastRevMessByOwnerPid-Dynamic Property*/



	protected java.util.Map<org.radixware.kernel.server.types.Pid,Str> changelogLastRevMessByOwnerPid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:changelogLastRevMessByOwnerPid")
	private final  java.util.Map<org.radixware.kernel.server.types.Pid,Str> getChangelogLastRevMessByOwnerPid() {
		return changelogLastRevMessByOwnerPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:changelogLastRevMessByOwnerPid")
	private final   void setChangelogLastRevMessByOwnerPid(java.util.Map<org.radixware.kernel.server.types.Pid,Str> val) {
		changelogLastRevMessByOwnerPid = val;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:changeLogHelper-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.ICfgChangeLogImportHelper changeLogHelper=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:changeLogHelper")
	private final  org.radixware.ads.CfgManagement.server.ICfgChangeLogImportHelper getChangeLogHelper() {
		return changeLogHelper;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:changeLogHelper")
	private final   void setChangeLogHelper(org.radixware.ads.CfgManagement.server.ICfgChangeLogImportHelper val) {
		changeLogHelper = val;
	}



















































































	/*Radix::CfgManagement::CfgImportHelper.Base:Methods-Methods*/

	/*Radix::CfgManagement::CfgImportHelper.Base:getResultsAsHtml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getResultsAsHtml")
	public published  org.radixware.kernel.common.html.Html getResultsAsHtml () {
		if (results.isEmpty())
		    return null;

		Utils::HtmlTable table = getResultsTable(false);
		if (table == null)
		    return null;

		Utils::Html head = new Html("head");
		Utils::Html meta = new Html("meta");
		meta.setAttr("content", "text/html; charset=utf-8");
		meta.setAttr("http-equiv", "Content-Type");
		head.add(meta);
		Utils::Html body = new Html("body");
		body.add(table);
		Utils::Html html = new Html("html");
		html.add(head);
		html.add(body);
		return html;

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getResultForm-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getResultForm")
	public published  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest getResultForm () {
		return new FormHandlerNextDialogsRequest(
		    new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Information, Client.Resources::DialogButtonType:Close, null, getResultsAsHtml().toString(), null),
		    null);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportNewExtIdGenerated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportNewExtIdGenerated")
	public published  void reportNewExtIdGenerated () {
		for (int i = results.size() - 1; i >= 0; i--) {
		    ResultRec r = results.get(i);
		    if (r.kind == ResultKind.NEW) {
		        r.newExtIdGenerated = true;
		        return;
		    }
		}

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectUpdated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectUpdated")
	public published  void reportObjectUpdated (Str objectTitle) {
		registerRec(ResultKind.UPDATED, null, objectTitle);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectCanceled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectCanceled")
	public published  void reportObjectCanceled (Str objectTitle) {
		registerRec(ResultKind.CANCELLED, null, objectTitle);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectCreated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectCreated")
	public published  void reportObjectCreated (Str objectTitle) {
		registerRec(ResultKind.NEW, null, objectTitle);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportPropsNotSet-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportPropsNotSet")
	public published  void reportPropsNotSet (Str objectTitle, Str... props) {
		registreWarning(ResultKind.PROPS_NOT_SET, null, objectTitle, props);

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:CfgImportHelper.Base-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:CfgImportHelper.Base")
	public  CfgImportHelper.Base () {
		results = new java.util.ArrayList<ResultRec>();
		changelogLastRevMessByOwnerPid = new java.util.HashMap<>();
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getResultsAsHtmlStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getResultsAsHtmlStr")
	public published  org.radixware.schemas.types.StrDocument getResultsAsHtmlStr () {
		Utils::Html h = getResultsAsHtml();
		if (h == null)
		    return null;
		Arte::TypesXsd:StrDocument res = Arte::TypesXsd:StrDocument.Factory.newInstance();
		res.Str = h.toString();
		return res;

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportWarnings-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportWarnings")
	public published  void reportWarnings (Str objectTitle, Str... warnings) {
		registreWarning(ResultKind.WARNINGS, null, objectTitle, warnings);

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getResultsTable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getResultsTable")
	public published  org.radixware.kernel.common.html.Table getResultsTable (boolean problemsOnly) {
		if (results.isEmpty())
		    return null;

		Utils::HtmlTable table = CfgCommonUtils.createHtmlTable();
		for (ResultRec r : results) {
		    if (problemsOnly && (r.kind == ResultKind.NEW || r.kind == ResultKind.UPDATED || r.kind == ResultKind.DELETED))
		        continue;
		    Utils::HtmlTable.Row tr = table.addRow();
		    
		    final Utils::Html titleCell = tr.addCell();
		    Utils::HtmlTable titleTable = new HtmlTable();
		    titleTable.addRow().addCell().setInnerText(r.objTitle);
		    
		    final Utils::Html extGuidCell = titleTable.addRow().addCell(); 
		    extGuidCell.setCss("padding-left", "20px");
		    extGuidCell.setCss("color", "gray");
		    extGuidCell.setInnerText("Ext Guid: " + (r.objExtGuid != null ? r.objExtGuid : "<not defined>"));
		    
		    final String lastChangelogRevMess = changelogLastRevMessByOwnerPid.get(r.objPid);
		    if (lastChangelogRevMess != null) {
		        final Utils::Html changelogCell = titleTable.addRow().addCell(); 
		        changelogCell.setCss("padding-left", "20px");
		        changelogCell.setCss("color", "gray");
		        changelogCell.setInnerText(lastChangelogRevMess);
		    }
		    
		    titleCell.add(titleTable);
		    
		    final String message = r.getMessage(false);
		    switch (r.kind) {
		        case ResultKind.NEW: {
		            Utils::Html c = tr.addCell();
		            Utils::Html h = new Html("font");
		            h.setAttr("color", "GREEN");
		            h.setInnerText(message);
		            c.add(h);
		            break;
		        }
		        case ResultKind.UPDATED: {
		            Utils::Html c = tr.addCell();
		            Utils::Html h = new Html("font");
		            h.setAttr("color", "BLUE");
		            h.setInnerText(message);
		            c.add(h);
		            break;
		        }
		        case ResultKind.DELETED: {
		            Utils::Html c = tr.addCell();
		            Utils::Html h = new Html("font");
		            h.setAttr("color", "GRAY");
		            h.setInnerText(message);
		            c.add(h);
		            break;
		        }
		        case ResultKind.CANCELLED:
		            tr.addCell().setInnerText(message);
		            break;
		        case ResultKind.WARNINGS:
		        case ResultKind.PROPS_NOT_SET: {
		            Utils::Html c = tr.addCell();
		            Utils::Html h = new Html("font");
		            h.setAttr("color", "RED");
		            h.setInnerText(message);
		            c.add(h);
		            Utils::HtmlTable pt = new HtmlTable();
		            for (Str p : r.warnings) {
		                Utils::Html с = pt.addRow().addCell();
		                с.setInnerText(p);
		            }
		            c.add(pt);
		            break;
		        }
		    }
		}
		return table;

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:createOrUpdateAndReport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:createOrUpdateAndReport")
	public published  void createOrUpdateAndReport (org.radixware.ads.Types.server.Entity obj) {
		if (obj.isInDatabase(false)) {
		    obj.update();
		    reportObjectUpdated(obj);
		} else {
		    obj.create();
		    reportObjectCreated(obj);
		}
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectDeleted-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectDeleted")
	public published  void reportObjectDeleted (Str objectTitle) {
		registerRec(ResultKind.DELETED, null, objectTitle);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectCancelled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectCancelled")
	public published  void reportObjectCancelled (org.radixware.ads.Types.server.Entity object) {
		registerRec(ResultKind.CANCELLED, object, calcObjTitle(object));
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectCreated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectCreated")
	public published  void reportObjectCreated (org.radixware.ads.Types.server.Entity object) {
		registerRec(ResultKind.NEW, object, calcObjTitle(object));
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectDeleted-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectDeleted")
	public published  void reportObjectDeleted (org.radixware.ads.Types.server.Entity object) {
		registerRec(ResultKind.DELETED, object, calcObjTitle(object));
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectUpdated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectUpdated")
	public published  void reportObjectUpdated (org.radixware.ads.Types.server.Entity object) {
		registerRec(ResultKind.UPDATED, object, calcObjTitle(object));
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportPropsNotSet-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportPropsNotSet")
	public published  void reportPropsNotSet (org.radixware.ads.Types.server.Entity object, Str... props) {
		registreWarning(ResultKind.PROPS_NOT_SET, object, calcObjTitle(object), props);

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportWarnings-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportWarnings")
	public published  void reportWarnings (org.radixware.ads.Types.server.Entity object, Str... warnings) {
		registreWarning(ResultKind.WARNINGS, object, calcObjTitle(object), warnings);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:calcObjTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:calcObjTitle")
	public published  Str calcObjTitle (org.radixware.ads.Types.server.Entity obj) {
		return ImpExpUtils.calcObjTitle(obj);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getActionIfObjExists-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getActionIfObjExists")
	public published  org.radixware.ads.CfgManagement.server.ICfgImportHelper.Action getActionIfObjExists (org.radixware.ads.Types.server.Entity object) {
		if (defaultActionIfObjExists != null) {
		    return defaultActionIfObjExists;
		}
		return getActionIfObjExists(calcObjTitle(object));

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportIfRefNotFound-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportIfRefNotFound")
	public published  void reportIfRefNotFound (org.radixware.ads.Types.server.Entity object, org.radixware.kernel.common.types.Id propId, Str extRef) {
		if (extRef == null || object.getProp(propId) != null) 
		    return;
		reportPropsNotSet(object, propId);

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:clearUnsupportedImportedRef-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:clearUnsupportedImportedRef")
	public published  void clearUnsupportedImportedRef (org.radixware.ads.Types.server.Entity object, org.radixware.kernel.common.types.Id propId) {
		if (object.getPropHasOwnVal(propId) && object.getProp(propId) != null) {
		    object.setProp(propId, null); 
		    reportPropsNotSet(object, propId);
		}

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportPropsNotSet-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportPropsNotSet")
	public published  void reportPropsNotSet (org.radixware.ads.Types.server.Entity object, org.radixware.kernel.common.types.Id propId) {
		reportPropsNotSet(object, null, propId);

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:checkIfRefNotFound-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:checkIfRefNotFound")
	public published  void checkIfRefNotFound (org.radixware.ads.Types.server.Entity object, org.radixware.kernel.common.types.Id propId, Str extRef) throws org.radixware.kernel.common.exceptions.AppError {
		checkIfRefNotFound(object, null, propId, extRef);

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:checkIfRefNotFound-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:checkIfRefNotFound")
	public published  void checkIfRefNotFound (org.radixware.ads.Types.server.Entity object, Str objectTitle, org.radixware.kernel.common.types.Id propId, Str extRef) throws org.radixware.kernel.common.exceptions.AppError {
		if (extRef == null || object.getProp(propId) != null)
		    return;

		if (objectTitle == null)
		    objectTitle = calcObjTitle(object);

		boolean notNull = false;
		Meta::ClassDef classDef = object.getClassDefinition();
		try {
		    Meta::PropDef propDef =  classDef.getPropById(propId);
		    if (propDef instanceof org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef) {
		        org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef refDef = (org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef) propDef;
		        for (org.radixware.kernel.common.defs.dds.DdsReferenceDef.ColumnsInfoItem it: refDef.Reference.ColumnsInfo.list()) {
		            Types::Id childColId = it.getChildColumnId();
		            org.radixware.kernel.common.defs.dds.DdsColumnDef colDef = classDef.TableDef.Columns.getById(childColId, org.radixware.kernel.common.defs.ExtendableDefinitions.EScope.LOCAL);
		            if (colDef != null && colDef.isNotNull()) {
		                notNull = true;
		                break;
		            }
		        }
		    }
		} catch (Exceptions::DefinitionNotFoundError e) {
		    ;
		}

		if (notNull)
		    exceptionIfRefNotFound(object, objectTitle, propId, extRef);
		else
		    reportIfRefNotFound(object, objectTitle, propId, extRef);

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:exceptionIfRefNotFound-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:exceptionIfRefNotFound")
	public published  void exceptionIfRefNotFound (org.radixware.ads.Types.server.Entity object, org.radixware.kernel.common.types.Id propId, Str extRef) throws org.radixware.kernel.common.exceptions.AppError {
		exceptionIfRefNotFound(object, null, propId, extRef);

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:exceptionIfRefNotFound-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:exceptionIfRefNotFound")
	public published  void exceptionIfRefNotFound (org.radixware.ads.Types.server.Entity object, Str objectTitle, org.radixware.kernel.common.types.Id propId, Str extRef) throws org.radixware.kernel.common.exceptions.AppError {
		if (extRef == null || object.getProp(propId) != null) 
		    return;
		Str mess = Str.format("Failed to set the link '%s' for '%s' by reference '%s'", calcPropTitle(object, propId), objectTitle == null ? calcObjTitle(object) : objectTitle, extRef); 
		throw new AppError(mess);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportIfRefNotFound-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportIfRefNotFound")
	public published  void reportIfRefNotFound (org.radixware.ads.Types.server.Entity object, Str objectTitle, org.radixware.kernel.common.types.Id propId, Str extRef) {
		if (extRef == null || object.getProp(propId) != null) 
		    return;
		reportPropsNotSet(object, objectTitle, propId);

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportPropsNotSet-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportPropsNotSet")
	public published  void reportPropsNotSet (org.radixware.ads.Types.server.Entity object, Str objectTitle, org.radixware.kernel.common.types.Id propId) {
		registreWarning(ResultKind.PROPS_NOT_SET, object, objectTitle == null ? calcObjTitle(object) : objectTitle, calcPropTitle(object, propId));
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:calcPropTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:calcPropTitle")
	protected published  Str calcPropTitle (org.radixware.ads.Types.server.Entity object, org.radixware.kernel.common.types.Id propId) {
		Meta::ClassDef classDef = object.getClassDefinition();
		Meta::PropDef propDef = classDef.getPropById(propId);
		return propDef.getTitle();

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:registreWarning-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:registreWarning")
	private final  void registreWarning (org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultKind kind, org.radixware.ads.Types.server.Entity object, Str objectTitle, Str... warnings) {
		if (warnings == null)
		    return;
		for (int i = results.size() - 1; i >= 0; i--) {
		    ResultRec r = results.get(i);
		    if (r.kind == kind && r.objTitle == objectTitle) {
		        java.util.Collections.addAll(r.warnings, warnings);
		        return;
		    }
		}

		java.util.List<Str> warnList = new java.util.ArrayList<Str>(warnings.length);
		java.util.Collections.addAll(warnList, warnings);
		if (object != null) {
		    results.add(new ResultRec(ImpExpUtils.getExtGuid(object), object.getPid(), objectTitle, kind, warnList));
		} else {
		    results.add(new ResultRec(null, null, objectTitle, kind, warnList));
		}
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:toTrace-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:toTrace")
	public published  void toTrace () {
		if (results.isEmpty())
		    return;

		final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		for (ResultRec r : results) {
		    sb.append("Configuration of object '").append(r.objTitle);
		    sb.append("' (ExtGuid: ").append(r.objExtGuid != null ? r.objExtGuid : "<not defined>").append(")");
		    if (changelogLastRevMessByOwnerPid.get(r.objPid) != null) {
		        sb.append(" (").append(changelogLastRevMessByOwnerPid.get(r.objPid)).append("): ");
		    } else {
		        sb.append(": ");
		    }
		    sb.append(r.getMessage(true));
		    if (r.kind == ResultKind.WARNINGS || r.kind == ResultKind.PROPS_NOT_SET) {
		        Arte::Trace.warning(sb.toString(), Arte::EventSource:AppCfgPackage);
		    } else {
		        Arte::Trace.event(sb.toString(), Arte::EventSource:AppCfgPackage);
		    }
		    sb.setLength(0);
		}

	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getContextItem-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getContextItem")
	public published  org.radixware.ads.CfgManagement.server.CfgItem getContextItem () {
		return contextItem;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getContextPacket-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getContextPacket")
	public published  org.radixware.ads.CfgManagement.server.CfgPacket getContextPacket () {
		return contextPacket;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getImportSettings-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getImportSettings")
	public published  org.radixware.ads.CfgManagement.common.ICfgImportSettings getImportSettings () {
		return settings;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:setImportSettings-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:setImportSettings")
	public published  void setImportSettings (org.radixware.ads.CfgManagement.common.ICfgImportSettings settings) {
		settings = settings;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:setContextItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:setContextItem")
	  void setContextItem (org.radixware.ads.CfgManagement.server.CfgItem item) {
		this.contextItem = item;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:setContextPacket-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:setContextPacket")
	  void setContextPacket (org.radixware.ads.CfgManagement.server.CfgPacket packet) {
		this.contextPacket = packet;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getUserFuncImportHelper-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getUserFuncImportHelper")
	public published  org.radixware.ads.CfgManagement.server.ICfgUserFuncImportHelper getUserFuncImportHelper () {
		if (ufHelper == null) {
		    ufHelper = new CfgUserFuncImportHelper(this);
		}
		return ufHelper;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:finalizeImport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:finalizeImport")
	public published  void finalizeImport () {
		if (ufHelper != null) {
		    ufHelper.compile(progressDlg);
		}
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:setDefaultActionIfObjExists-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:setDefaultActionIfObjExists")
	public published  void setDefaultActionIfObjExists (org.radixware.ads.CfgManagement.server.ICfgImportHelper.Action action) {
		defaultActionIfObjExists = action;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getObjsCountByKind-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getObjsCountByKind")
	  int getObjsCountByKind (org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultKind kind) {
		int counter = 0;
		for (CfgImportHelper.Base.ResultRec rec : results) {
		    if (rec.kind == kind) {
		        counter++;
		    }
		}
		return counter;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:createLoadProcessProgressBar-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:createLoadProcessProgressBar")
	  org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process createLoadProcessProgressBar (Str progressCaption) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ResourceUsageException,org.radixware.kernel.common.exceptions.ResourceUsageTimeout {
		if (progressCaption == null) {
		    return null;
		}
		try {
		    progressDlg = new ProgressDialogResource(Arte::Arte.getInstance(), progressCaption, false).startNewProcess("", true, true);
		    return progressDlg;
		} catch (Exceptions::InterruptedException | Exceptions::ResourceUsageException | Exceptions::ResourceUsageTimeout ex) {
		    throw ex;
		}
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportRidUniquenessViolation-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportRidUniquenessViolation")
	public published  void reportRidUniquenessViolation (org.radixware.ads.Types.server.Entity thisObj, org.radixware.ads.Types.server.Entity otherObj, Str rid) {
		final String otherObjTitle = otherObj != null ? calcObjTitle(otherObj) : "";
		registreWarning(ResultKind.WARNINGS, thisObj, calcObjTitle(thisObj),
		        Str.format("Reference Id uniqueness violation: target database already contains object %s with RID: '%s'. Reference Id of imported object will be reset to null.",
		                otherObjTitle, rid));
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:importRid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:importRid")
	public published  Str importRid (org.radixware.ads.Types.server.Entity thisObj, org.radixware.ads.Types.server.Entity otherObj, Str rid) {
		if (otherObj == null || otherObj == thisObj) {
		    return rid;
		} else {
		    reportRidUniquenessViolation(thisObj, otherObj, rid);
		    return null;
		}
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:importArrRef-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:importArrRef")
	public published  void importArrRef (org.radixware.ads.Types.server.Entity owner, org.radixware.kernel.common.types.Id propId, boolean isInherited, java.util.List<Str> guids, java.util.List<? extends org.radixware.ads.Types.server.Entity> refs) {
		if (isInherited) {
		    owner.setPropHasOwnVal(propId, false);
		} else {
		    owner.setProp(propId, refs);
		    if (guids == null) {
		        if (refs != null) {
		            reportWarnings(owner, "Internal error: guids is null, but refs not null");
		        }
		        return;
		    }
		    if (refs == null) {
		        reportPropsNotSet(owner, propId);
		        return;
		    }

		    final java.util.List<String> warnings = new java.util.ArrayList<>();
		    final java.util.Iterator<String> it1 = guids.iterator();
		    final java.util.Iterator<? extends Types::Entity> it2 = refs.iterator();

		    while (it1.hasNext()) {
		        final String guid = it1.next();
		        final Types::Entity ref = it2.next();

		        if (guid != null && ref == null) {
		            warnings.add(guid);
		        }
		    }

		    if (!warnings.isEmpty()) {
		        reportWarnings(owner, String.format(
		            "Value of the property \"%s\" has not been fully imported, elements with the following ExtGuid not found:",
		            owner.getClassDefinition().getPropById(propId).getTitle()
		        ));
		        reportWarnings(owner, warnings.toArray(new String[warnings.size()]));
		    }
		}
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:importRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:importRoles")
	public published  void importRoles (org.radixware.ads.Types.server.Entity owner, org.radixware.kernel.common.types.Id propId, boolean isInherited, java.util.List<Str> guids) {
		if (isInherited) {
		    owner.setPropHasOwnVal(propId, false);
		} else {
		    final ArrStr value = guids == null ? null : new ArrStr();
		    if (guids != null) {
		        final java.util.List<String> warnings = new java.util.ArrayList<>();
		        Arte::Arte.getDefManager().updateAppRolesCache();
		        for (String guid : guids) {
		            if (Meta::Utils.doCheckRole(Types::Id.Factory.loadFrom(guid))) {
		                value.add(guid);
		            } else {
		                warnings.add(guid);
		            }
		        }

		        if (!warnings.isEmpty()) {
		            reportWarnings(owner, String.format(
		                "Value of the property \"%s\" has not been fully imported, the following roles not found:",
		                owner.getClassDefinition().getPropById(propId).getTitle()
		            ));
		            reportWarnings(owner, warnings.toArray(new String[warnings.size()]));
		        }
		    }
		    owner.setProp(propId, value);
		}
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:registerRec-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:registerRec")
	private final  void registerRec (org.radixware.ads.CfgManagement.server.CfgImportHelper.Base.ResultKind kind, org.radixware.ads.Types.server.Entity obj, Str objTitle) {
		if (obj != null) {
		    results.add(new CfgImportHelper.Base.ResultRec(ImpExpUtils.getExtGuid(obj), obj.getPid(), objTitle, kind, null));
		} else {
		    results.add(new CfgImportHelper.Base.ResultRec(null, null, objTitle, kind, null));
		}
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:getChangeLogImportHelper-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:getChangeLogImportHelper")
	public published  org.radixware.ads.CfgManagement.server.ICfgChangeLogImportHelper getChangeLogImportHelper () {
		if (changeLogHelper == null) {
		    changeLogHelper = new CfgImportHelper.Base.ChangeLogImportHelperImpl();
		}
		return changeLogHelper;
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectCreated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectCreated")
	public published  void reportObjectCreated (org.radixware.ads.Types.server.Entity object, Str objectTitle) {
		registerRec(ResultKind.NEW, object, objectTitle);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportObjectUpdated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportObjectUpdated")
	public published  void reportObjectUpdated (org.radixware.ads.Types.server.Entity object, Str objectTitle) {
		registerRec(ResultKind.UPDATED, object, objectTitle);
	}

	/*Radix::CfgManagement::CfgImportHelper.Base:reportWarnings-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgImportHelper.Base:reportWarnings")
	public published  void reportWarnings (org.radixware.ads.Types.server.Entity object, Str objectTitle, Str... warnings) {
		registreWarning(ResultKind.WARNINGS, object, objectTitle, warnings);
	}


}

/* Radix::CfgManagement::CfgImportHelper.Base - Server Meta*/

/*Radix::CfgManagement::CfgImportHelper.Base-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgImportHelper.Base_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcQRT3DVIK6ZGTFCMBS46VE73IT4"),"CfgImportHelper.Base",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::CfgManagement::CfgImportHelper.Base:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgImportHelper.Base:results-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQETDJSD2WJBGJIQ2BMB2LNF3CM"),"results",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgImportHelper.Base:settings-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd74N6EJXLJJBLVPXX53N54XLU3Q"),"settings",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgImportHelper.Base:contextItem-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJF6ITCOLZBAUBE7N6WMRRBNTPE"),"contextItem",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgImportHelper.Base:contextPacket-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUZM75SQSX5EYNPFIP5AZLO3MNE"),"contextPacket",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgImportHelper.Base:ufHelper-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXDLGOATZ7ZGHXAO5ERD6ADLVP4"),"ufHelper",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgImportHelper.Base:defaultActionIfObjExists-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVK7ZRQZRSZH7VBC5S5Z5E2NM5Y"),"defaultActionIfObjExists",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgImportHelper.Base:progressDlg-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdW4JS7V6JUJDNFC3GWVS6QX5TDU"),"progressDlg",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgImportHelper.Base:changelogLastRevMessByOwnerPid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHWLYBC3RSFDI3FD3ERZLBMRK6I"),"changelogLastRevMessByOwnerPid",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgImportHelper.Base:changeLogHelper-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRSAFL27CV5F2JAZDFFVHTO3OIU"),"changeLogHelper",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::CfgImportHelper.Base:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI42BRXHR6ZDORCHPCAWOTGUVDU"),"getResultsAsHtml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJQP4RBNLDVGIBGUR2K6WJGC6RI"),"getResultForm",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4BCVYDCWENDZVJXEC6VKPDJ4OY"),"reportNewExtIdGenerated",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAOYAWV4KE5FVVGDLXQB42QFURM"),"reportObjectUpdated",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU3JDTLQZQRAUDNXUQNQLFXHRBI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUTLZYDZIKJDPZOQBQ6BJ773X4Y"),"reportObjectCanceled",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJDYY7NHS3FBKVN2T5WLJ6CL4HA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ7M4FMHWZJDNFCKCSYDSXJSHYQ"),"reportObjectCreated",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZQNP2BDKLZC3DALJ4QXDQZ5YNI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCXFA6PYRJBDGLKBGMQHXM37NEI"),"reportPropsNotSet",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTZ4K6NCGLBCHHA64Q35NPAIXQU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("props",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIWO3BPY5OFEPTLDLVTUTAQTENE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2VYZAMFYFREDJOUVDT4CBXGCUE"),"CfgImportHelper.Base",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLK52XSZFHZBCLIGUAS5CPEQ2AE"),"getResultsAsHtmlStr",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGFBWZF6UKBAN5A7ZZIMPGOQUDU"),"reportWarnings",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTU26TYX6XRDZZADMQCUQG546WI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("warnings",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFUJHTWHJ3VAP3PERGSHM763JXA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5XSTW5OXBJA5JBGIUSMPRUAFDA"),"getResultsTable",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemsOnly",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr652LG4RNJBAE7NI2T6F7GXH7IE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVHPOUV7Y4ZAQDLNT3S7A6GZWQU"),"createOrUpdateAndReport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQTILOUSEGRBHDG35Y66DE2W3DI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXV5F5YZDGFCLJDD2VVOMGMNGUI"),"reportObjectDeleted",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4FEJGCBXEBDFROBHQO64LDY5PQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFATUBWO3Y5GGFJXF4TWGIGFTIE"),"reportObjectCancelled",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSSAHC7ZI7BBRBOSJ5DQL376JBE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ3NETH2TUNAODBTD7YKIAWZNYI"),"reportObjectCreated",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6XURPKHVX5B5ZHUGAIFQGG42WA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPS4GTDD6XZBRXDUFNLC5ZFTZCQ"),"reportObjectDeleted",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIWU7FZKE7NGWRICAA7C6TWSWQI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUSPSKJ2SHRBA7AJSHBW6RTU6X4"),"reportObjectUpdated",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVQ2KIKUIE5EWRM3OQ5GGTLAPSY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYREB6CKPZZAQHEBTVDPGCOMFUU"),"reportPropsNotSet",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4EZ2BYHPNNGSNEEUIAP5IC4H2U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("props",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUKF2ZUF6PFBFFNZZBP5JMMSSWI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6COBQOSWOVGU7LUO3PIKETNRXE"),"reportWarnings",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCU4O6MQQVJDXVCLMAINZKOJ5XM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("warnings",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFQPIRM3U7NAYLJNWTF4MDKPNIE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMJXLQKFP2NCK7K2NFJAAJ6J7MA"),"calcObjTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZXHAUA2Y5HGHESZNJVGOMGKJA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQZTXWZJKAZHILBSEQIWTHOSMUA"),"getActionIfObjExists",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWBCZ6SGEKBBRXPVEPFSHIWUHGY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthE76WDLOGARH35AMA6ESRFHVKTA"),"reportIfRefNotFound",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprX4ZTWSLRYJDIVB46OBBX7O22U4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN3ONDODCIZF2PMQYBBHLMPRNAY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extRef",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFOIZKBVUCZF5TE5DPJJDV5TSBE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZBXGDJSPTVBD5EK4FTRY24KYSQ"),"clearUnsupportedImportedRef",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZGCWEURKVRBQZMFF573C44KXOE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVJWWE7YETZHQDMQ3I4Z7UVFVCA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKGUNG5GP5VHVDIIY5KIZXTKCIA"),"reportPropsNotSet",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGHEJZBXPRBA3LKFDU3H6HPFKY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU6JRTEJWQNBMBFLSJGXZBGDZEE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNEJWG2R45RBEZPQPIVEB5VZEOU"),"checkIfRefNotFound",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWWAUVENDSBGPHCQEH624NFWFXY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW3JADBLAL5CKDDNGZITHOXOKAM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extRef",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4P2PWIREJZB7JAFDZP6IMHGROU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6ZR34BIPIBBSFAAWKDPZYLWSLI"),"checkIfRefNotFound",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOMXCUMSBEBHT5PB33SGJ5JV2CQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBWJFMDVRVBCX5PN2ZSERZEFGSY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprO6464Z4ADNGY5CGLDSBOPRLHQQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extRef",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDL77CKPPHBHGHPPQUZJU5B3SN4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTPLYMFLRXNCNFGMLV5T4ZLPCN4"),"exceptionIfRefNotFound",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJDETLEXQ4FGBNJPJDPOJXYBLJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3TAXVDDFJVEFPOLSOWHPCUZ5RU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extRef",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRYYKGZA4BNHPDMT4WLB2W6YZQA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMR4FWKYDLVHEPNGGW4BXBKCTKY"),"exceptionIfRefNotFound",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7VOSM3NXBVH5DKK3KQMQZG6YTY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEBGVSHXCOZGPTDPK4Z7J5ETIQE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD62BTA4XTFGWJEOUDBTRZT22TY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extRef",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRE5P7V73ZBEDNDO5O65NKO7H7E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVUMOLWDHNFES5NMSQAHCOJPSAU"),"reportIfRefNotFound",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAPZTLLC3HFCEPDTJ4TWMC7DB4I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLZY74M24NRD6REICOUVC5EOM4M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPTEFO33CZNDONNOCEAIFBMHMIM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extRef",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDBU7FTSEYFHLBLBJEN5HXKDCDQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVM3TITIE75HB3DEOZL642DOWGY"),"reportPropsNotSet",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2BHI7CDQ2NCEZJEYAMKC6AWOYA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5JFWLJBFO5EGVK2VM7NTULU25M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprX5CWZEKPXFC4XOMGIDSZE26BTY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthT5SEV2XU3VBJ7BUF2RFICWIBPU"),"calcPropTitle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2BHI7CDQ2NCEZJEYAMKC6AWOYA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprX5CWZEKPXFC4XOMGIDSZE26BTY"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIZNAHBPOTBAPJA6M3GDA7PAMOY"),"registreWarning",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("kind",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWVEIPJMNDCZDSTUWNZIAMC3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr63CWND4JPVG7PJTAFRN3SGC77Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYSKUWN5NCBCDTH3Y66QRK35MTA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("warnings",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUHHJGPOOMBC2FK33C6BOC3RKYQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRQNXWVAPANHZPBXO4BVCAMPWPA"),"toTrace",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTF3B4FUHBFEEZD7MKXEZJFCMAA"),"getContextItem",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSTNDKBAJ6JFM3CGFRKI3SV7CO4"),"getContextPacket",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVQB7HZD5EVA4NHKSRJAR5425DY"),"getImportSettings",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthF5QKT6IE3VDZNA3CYQ7SVIZGFQ"),"setImportSettings",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("settings",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5ZCYWATHNBAB7H4FTSEIGLUJGE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGTW2Q4ZTUNFHXK6R6KZCUZ7SZM"),"setContextItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("item",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYBFMLIBCPRF5TBKSI7MZ72MJXU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3PMVQZ5BHFC5ZFPKOMUJH7TWBQ"),"setContextPacket",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("packet",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM2GCG3K7DNFIJFLFFTYQZOAXV4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYGPQPGZLOZHKZJNV76IERZN45Y"),"getUserFuncImportHelper",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIZXQL5ITJNFOTMS5GFD4OQFL6E"),"finalizeImport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSGPUOVVXAZEI5OXVMUAGEYUREM"),"setDefaultActionIfObjExists",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("action",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQCKA3NYACFE5TIVJTGVYLUI4ZA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWEYK7KBDGJGOPJ4SFFAXB2MODU"),"getObjsCountByKind",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("kind",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLHQK7PRMA5AQDAHESQWJSV65VI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM2XEXR2P5VE5LG6DUK3DISWYPE"),"createLoadProcessProgressBar",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("progressCaption",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIMCRYFMY5BHS3ED3CII5VPVSJM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVDVVCYRQ55DWNDVOZ7FIGJJJQQ"),"reportRidUniquenessViolation",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("thisObj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD5HM43VUHJC73NWTW6AIJZCSPA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("otherObj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2VXILS6K2NFHLEPEESXHI232ZQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6EUOD4W5BJBXFC4CMJK6U3NJC4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYEFJTEIJ2RGLFH75XLGM7Y3D3Q"),"importRid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("thisObj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPAPSD3S7MJB6DF6B5KYJQ7HIPM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("otherObj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUK4O4M4J3VBOZCSQPOSXCP25T4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCEXOLAZ6ORGZ3DL6YMXVHFLKFU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLS7J67BUHZBFTNWS5RQMM34TEA"),"importArrRef",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBIVOVYGAIFF5ZBWXXBRVNZPFGU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOBGAVRTX35CH5FIRCMVZSZXUPU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isInherited",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFCFN64DH5ZCVVDFUGQEPNSQVZM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB43EKO7KVJE3TMUKU3TOQFITJQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("refs",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNVSP73FQVBCZDNTNEFF6U2JIKI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHTDVASTBUVHNTAVZAXK3XRDVBQ"),"importRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7B62FZXF6BCNPJ4FF4SRZ6YIBM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGOQK6UPNRVE2NL4PE2G5QKCPPA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isInherited",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ33LPRTXOJG4NIHODNRU3EOB5A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNA6ZPV3U5BHA5NGWHXB5YZ3GAA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7SHKFFPTNZCG7JOLYZ5JCR3W3Q"),"registerRec",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("kind",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4DTBWK2UIJC53HMAJJJCUOHPEI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprELAUUQR4EFHBRMMAEKALZAJQKM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEVETHIUOWJASNLSPQNLUVVVC7M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWFGCEQZ6SRBVNPDML4FW6RCLXU"),"getChangeLogImportHelper",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMM2ORZUN3RBUXKB737E4VKAKZY"),"reportObjectCreated",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3532MZDOLZAALJP237CG65GBNI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7CD2RMQSTBFZZJJW5URBUESTIU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2I24QZ23MJAHFCVZTSKP7U3UPE"),"reportObjectUpdated",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprURKLX3SXAVCM5FP3NHPGLBA7EE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOHQH3RFWKVEB7FF2QK6P2IWV64"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3ZGA42NJLBAWLMXQEFL4B2NK5Q"),"reportWarnings",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV3UOVN46QNCRXNC5NWUWFKRSKA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objectTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSFH6OBH7SNGBNO5XIV4JGZKQ24")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("warnings",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV6GHRGKRJRFXFB3646HJYLPQWM"))
								},null)
						},
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcVDTJPUJICREKZMJHVBLETZLIJI = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcVDTJPUJICREKZMJHVBLETZLIJI"),"ResultRec",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKD4DDIM36JDA5ID3MQK73MW7O4"),"objTitle",null,org.radixware.kernel.common.enums.EValType.STR,null,null,null),

								/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:kind-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQRKOTQYQMFAVFLNAGXUBMQCYAU"),"kind",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,null),

								/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:warnings-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHHGK3R4ZVFDDZINP5ZNUVRF5WY"),"warnings",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,null),

								/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:newExtIdGenerated-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRFQAQDK5IJDFREBAUDSKKE73VM"),"newExtIdGenerated",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,null),

								/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objPid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX7ESC7N5X5CNRFPPWPTYZ6GWSE"),"objPid",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,null),

								/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:objExtGuid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdN3TRS3W7EZAUNDSBMPGSINCAPE"),"objExtGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,null)
						},

						/*Radix::CfgManagement::CfgImportHelper.Base:ResultRec:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCVJ7NGNUMBE3VMHN4HMJORCPTM"),"getMessage",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isFullMessage",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYRUNVZEZHFDGNK2GBTGL6UTWV4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7I25RJHABZACHI2EVCHFSVWB6A"),"ResultRec",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6UCKHMJNWFCJTKELCBL67ZGL6U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objPid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVA4L62YVQZCXBB4QAJM34RFZ4A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5NQB375KZZFT7AZJE7ZFDNHOCU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("kind",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTLSJQLV2VBFVXHHSJDY5OKG5RE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("warnings",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFBU43Y5HWFFBPFNTVPE3B3U5DI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOBJBU24E6FAHTJ4VLETG3YUKTQ"),"isPunctuation",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ch",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHYRRJQ27XFGTTBJ54HGJ4BCFPI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_aetK3SPY2LE4VBNRB5547FV2QCQ5Y = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aetK3SPY2LE4VBNRB5547FV2QCQ5Y"),"ResultKind",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:Properties-Properties*/
						null,

						/*Radix::CfgManagement::CfgImportHelper.Base:ResultKind:Methods-Methods*/
						null,
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcIU4DAPZVHVDEZDXX3QLWX63QGA = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcIU4DAPZVHVDEZDXX3QLWX63QGA"),"ChangeLogImportHelperImpl",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::CfgManagement::CfgImportHelper.Base:ChangeLogImportHelperImpl:Properties-Properties*/
						null,

						/*Radix::CfgManagement::CfgImportHelper.Base:ChangeLogImportHelperImpl:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCRPDEX3FFZEMVMZL5JJVDQM2M4"),"registerChangeLogLastRevMess",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ownerPid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA25SU4T2RVAEHAT6IMKUPZ47BQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQFQNFSTWV5AKNNYK5KCRO7KKSQ"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::CfgManagement::CfgImportHelper.Base - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgImportHelper.Base - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обновлен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PUC4W54HZFWXAXXPI2QAEXCCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Object with the same ID already exists. Object with new ID created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект с таким же ид. уже существует. Создан объект с новым  ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IC3M6KDZBE5NI32GE6A7QEZUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to set the link \'%s\' for \'%s\' by reference \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось установить связь \'%s\' для \'%s\' по ссылке \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls67J7BWI7ZJDIBBFSLQH24XDEKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check if property set and throw exception");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6V5G6VQBJBCSXJZMMCK3WNZYAQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Properties not set:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не установленные свойства:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls72BZNFYSGFFTRK2CIJZL3RTBOE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Clear reference property and report warning");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAOE3FIF3LRGJRJP2NRH4SDWXMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check if property set and report warning or throw exception (if the property is mandatory)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBFGAHB4D2BBIBM2APKYIW4G22A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check if property set and report warning or throw exception (if the property is mandatory)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCV24PFUU3BAXVC4X52IWU6SN3U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"This method should somehow inform the user that a new object with the specified header has been successfully created.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Данный метод должен как-то сообщить пользователю, что новый объект с данным заголовком был успешно создан.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCXNSEDGPE5BTZMWCXRAGV6ATDI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"This method should somehow inform the user that a new object with the specified header has been successfully created.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Данный метод должен как-то сообщить пользователю, что новый объект с данным заголовком был успешно создан.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBTPIPVHOZBZZM5J4HBTKWNCZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Warnings:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Предупреждения:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFLG73ARM6BBCVIFUOS2QXKSULI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGNENEIUU6NCBHK4DYMJQU4PLQY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check if property set and report warning");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFVKAR4RJZAORO7QDTFW44KEQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reference Id uniqueness violation: target database already contains object %s with RID: \'%s\'. Reference Id of imported object will be reset to null.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нарушение уникальности ссылочного идентификатора: целевая база данных уже содержит объект %s со значением RID: \'%s\'. Ссылочный идентификатор импортированного объекта будет сброшен в null.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIOBLROS4AFGI5O5RGTTOOGNM4A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import canceled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт отменен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ3RZYXRAKJHMZL4BWJSPQJFYXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New object created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создан новый объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKEWBVSFW2NGVNAU3WN7PGF7PT4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Value of the property \"%s\" has not been fully imported, elements with the following ExtGuid not found:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение свойства \"%s\" импортировано не полностью, не найдены элементы со следующими ExtGuid:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO5QZM3MXGFEGTCKHD5K7LKTS7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Value of the property \"%s\" has not been fully imported, the following roles not found:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение свойства \"%s\" импортировано не полностью, не найдены роли:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMODK4NGL5AUNHLDD65R5UOLEM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration of object \'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Применение конфигурации к объекту \'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRDS3RN2XDBDEDAIZLVEQL6DHMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Deleted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удален");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRMJAPLB6XVEBFKWCNDRS4YAZQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check if property set and report warning");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUEEOBPUD6JCWJD6JHHF43NSK74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check if property set and throw exception");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWE4GCH6BOJFDBDCW2LBZ52POEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYQBD5ZN3ZNCJNF55BVQSFU4IMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgImportHelper.Base - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcQRT3DVIK6ZGTFCMBS46VE73IT4"),"CfgImportHelper.Base - Localizing Bundle",$$$items$$$);
}

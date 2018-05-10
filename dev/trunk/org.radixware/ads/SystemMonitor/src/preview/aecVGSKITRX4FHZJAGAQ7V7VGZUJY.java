
/* Radix::SystemMonitor::DashConfig - Server Executable*/

/*Radix::SystemMonitor::DashConfig-Entity Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig")
public final published class DashConfig  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return DashConfig_mi.rdxMeta;}

	/*Radix::SystemMonitor::DashConfig:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:DashboardCacheEntry")
	public class DashboardCacheEntry  implements org.radixware.kernel.server.instance.ICachedUserObject  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return DashConfig_mi.rdxMeta_adcMQHOW5W57RBOXOB3SM7PFJCUQE;}

		/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:Nested classes-Nested Classes*/

		/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:Properties-Properties*/

		/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:xDashDoc-Dynamic Property*/

		protected org.radixware.ads.SystemMonitor.common.DashboardXsd.DashboardDocument xDashDoc=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:DashboardCacheEntry:xDashDoc")
		public  org.radixware.ads.SystemMonitor.common.DashboardXsd.DashboardDocument getXDashDoc() {
			return xDashDoc;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:DashboardCacheEntry:xDashDoc")
		public   void setXDashDoc(org.radixware.ads.SystemMonitor.common.DashboardXsd.DashboardDocument val) {
			xDashDoc = val;
		}

		/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:lastUpdateTime-Dynamic Property*/

		protected long lastUpdateTime=0;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:DashboardCacheEntry:lastUpdateTime")
		public  long getLastUpdateTime() {
			return lastUpdateTime;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:DashboardCacheEntry:lastUpdateTime")
		public   void setLastUpdateTime(long val) {
			lastUpdateTime = val;
		}

		/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:Methods-Methods*/

		/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:release-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:DashboardCacheEntry:release")
		public published  void release () {
			//do nothing
		}

		/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:DashboardCacheEntry-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:DashboardCacheEntry:DashboardCacheEntry")
		public  DashboardCacheEntry (org.radixware.ads.SystemMonitor.common.DashboardXsd.DashboardDocument xDashDoc, long lastUpdateTime) {
			xDashDoc = xDashDoc;
			lastUpdateTime = lastUpdateTime;
		}


	}

	/*Radix::SystemMonitor::DashConfig:Properties-Properties*/

	/*Radix::SystemMonitor::DashConfig:guid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:guid")
	public  Str getGuid() {
		return guid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:guid")
	public   void setGuid(Str val) {
		guid = val;
	}

	/*Radix::SystemMonitor::DashConfig:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:title")
	public  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:title")
	public   void setTitle(Str val) {
		title = val;
	}

	/*Radix::SystemMonitor::DashConfig:xmlContent-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:xmlContent")
	public  java.sql.Clob getXmlContent() {
		return xmlContent;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:xmlContent")
	public   void setXmlContent(java.sql.Clob val) {
		xmlContent = val;
	}

	/*Radix::SystemMonitor::DashConfig:lastUpdateUser-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateUser")
	public  Str getLastUpdateUser() {
		return lastUpdateUser;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateUser")
	public   void setLastUpdateUser(Str val) {
		lastUpdateUser = val;
	}

	/*Radix::SystemMonitor::DashConfig:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateTime")
	public  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateTime")
	public   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::SystemMonitor::DashConfig:storeInCacheDurationSec-Dynamic Property*/



	protected int storeInCacheDurationSec=600;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:storeInCacheDurationSec")
	private final  int getStoreInCacheDurationSec() {
		return storeInCacheDurationSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:storeInCacheDurationSec")
	private final   void setStoreInCacheDurationSec(int val) {
		storeInCacheDurationSec = val;
	}

































































	/*Radix::SystemMonitor::DashConfig:Methods-Methods*/

	/*Radix::SystemMonitor::DashConfig:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:loadByPidStr")
	public static  org.radixware.ads.SystemMonitor.server.DashConfig loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),pidAsStr);
		try{
		return (
		org.radixware.ads.SystemMonitor.server.DashConfig) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::SystemMonitor::DashConfig:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:loadByPK")
	public static  org.radixware.ads.SystemMonitor.server.DashConfig loadByPK (Str guid, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(guid==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),guid);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),pkValsMap);
		try{
		return (
		org.radixware.ads.SystemMonitor.server.DashConfig) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::SystemMonitor::DashConfig:getCachedDashDocXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:getCachedDashDocXml")
	public  org.radixware.ads.SystemMonitor.common.DashboardXsd.DashboardDocument getCachedDashDocXml () {
		final String cacheKey = "Dashboard~" + guid;
		DashboardCacheEntry cacheEntry = (DashboardCacheEntry) Arte::Arte.getUserCache().get(cacheKey);
		if (cacheEntry != null && cacheEntry.lastUpdateTime == lastUpdateTime.Time) {
		    return cacheEntry.xDashDoc;
		} else {
		    try {
		        DashboardXsd:DashboardDocument xDashboardDoc = DashboardXsd:DashboardDocument.Factory.parse(xmlContent.getCharacterStream());
		        Arte::Arte.getUserCache().put(
		                cacheKey,
		                new DashboardCacheEntry(xDashboardDoc, lastUpdateTime.Time),
		                storeInCacheDurationSec
		        );
		        return xDashboardDoc;
		    } catch (Exceptions::Exception ex) {
		        Arte::Trace.error(Arte::Trace.exceptionStackToString(ex), Arte::EventSource:SystemMonitoring);
		    }
		}
		return null;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::DashConfig - Server Meta*/

/*Radix::SystemMonitor::DashConfig-Entity Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DashConfig_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),"DashConfig",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX24SICCPQNDG5BUNUR6D3MJVVM"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::SystemMonitor::DashConfig:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
							/*Owner Class Name*/
							"DashConfig",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX24SICCPQNDG5BUNUR6D3MJVVM"),
							/*Property presentations*/

							/*Radix::SystemMonitor::DashConfig:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::DashConfig:guid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::DashConfig:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::DashConfig:xmlContent:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXTE7QVLZ3FGCFDGLAXN7EWNN54"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::DashConfig:lastUpdateUser:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::DashConfig:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::SystemMonitor::DashConfig:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::SystemMonitor::DashConfig:General:Children-Explorer Items*/
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
									/*Radix::SystemMonitor::DashConfig:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXTE7QVLZ3FGCFDGLAXN7EWNN54"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SystemMonitor::DashConfig:WithoutXmlContent-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPXXAJTTI25FEBDAS2XVEWSLNUA"),"WithoutXmlContent",org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),16560,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SystemMonitor::DashConfig:ForPartitionsGroup-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYTMICZFELFBFTBKR3SIYEWREGQ"),"ForPartitionsGroup",org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),16571,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),true)
									},null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},null,null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::SystemMonitor::DashConfig:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::SystemMonitor::DashConfig:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::DashConfig:guid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),"guid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3VMUBOPSH5DU7GIL5YUZD5DMI4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::DashConfig:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFW3DSYQKFDDVETRYFWPEWYBZU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::DashConfig:xmlContent-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXTE7QVLZ3FGCFDGLAXN7EWNN54"),"xmlContent",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5GHBZGTBNBTDOFKOQK6RZ4CPY"),org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::DashConfig:lastUpdateUser-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),"lastUpdateUser",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD36QI6DIZVE73H5MT5HZXR3D6A"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::DashConfig:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),"lastUpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ47RUGDGZBEQTC45BMJSTPZKIQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::DashConfig:storeInCacheDurationSec-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGZCEXBNWYFFIBJ5KCV76K2FN3U"),"storeInCacheDurationSec",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::DashConfig:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQU77X4CEHNCMPMBTEGPP2OQ3IY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIPJCMVQKRBGI7AD3BO4MYZCUKY"),"getCachedDashDocXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.OWN,null,new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea[]{

								new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea(new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea.Partition[]{

										new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea.Partition(org.radixware.kernel.common.types.Id.Factory.loadFrom("apf4PQ4U65VK5HFVJ32XCUORBKRJM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"))
								})
						},false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcMQHOW5W57RBOXOB3SM7PFJCUQE = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcMQHOW5W57RBOXOB3SM7PFJCUQE"),"DashboardCacheEntry",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,
						null,
						null,
						null,

						/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:xDashDoc-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ4BW3YI2NFGNTHVWHEYGX56KKE"),"xDashDoc",null,org.radixware.kernel.common.enums.EValType.XML,null,null,null),

								/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:lastUpdateTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPURP77PITZA4TKFFUH7ERM3IKI"),"lastUpdateTime",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,null)
						},

						/*Radix::SystemMonitor::DashConfig:DashboardCacheEntry:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPVPRWFCHNNAQ3DIGR6QDW642FE"),"release",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEBZBNXGTNNBCDATOBRIT4AGPI4"),"DashboardCacheEntry",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xDashDoc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSAG2VDQ73VDJLAIBWZUAI2LQY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lastUpdateTime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV4S522BYSNEJPPLM4RWI4CRTUA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::DashConfig - Desktop Executable*/

/*Radix::SystemMonitor::DashConfig-Entity Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig")
public interface DashConfig {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.SystemMonitor.explorer.DashConfig.DashConfig_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.SystemMonitor.explorer.DashConfig.DashConfig_DefaultModel )  super.getEntity(i);}
	}















































	/*Radix::SystemMonitor::DashConfig:lastUpdateUser:lastUpdateUser-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateUser:lastUpdateUser")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateUser:lastUpdateUser")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUser getLastUpdateUser();
	/*Radix::SystemMonitor::DashConfig:lastUpdateTime:lastUpdateTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::SystemMonitor::DashConfig:guid:guid-Presentation Property*/


	public class Guid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Guid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::SystemMonitor::DashConfig:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::SystemMonitor::DashConfig:xmlContent:xmlContent-Presentation Property*/


	public class XmlContent extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public XmlContent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:xmlContent:xmlContent")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:xmlContent:xmlContent")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public XmlContent getXmlContent();


}

/* Radix::SystemMonitor::DashConfig - Desktop Meta*/

/*Radix::SystemMonitor::DashConfig-Entity Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DashConfig_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::DashConfig:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
			"Radix::SystemMonitor::DashConfig",
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX24SICCPQNDG5BUNUR6D3MJVVM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBJR3SBGZRHLBL47TM7P2SIVWQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX24SICCPQNDG5BUNUR6D3MJVVM"),0,

			/*Radix::SystemMonitor::DashConfig:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::DashConfig:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),
						"guid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3VMUBOPSH5DU7GIL5YUZD5DMI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::DashConfig:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::DashConfig:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFW3DSYQKFDDVETRYFWPEWYBZU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::DashConfig:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,300,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::DashConfig:xmlContent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXTE7QVLZ3FGCFDGLAXN7EWNN54"),
						"xmlContent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5GHBZGTBNBTDOFKOQK6RZ4CPY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
						org.radixware.kernel.common.enums.EValType.CLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						true,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::DashConfig:xmlContent:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::DashConfig:lastUpdateUser:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),
						"lastUpdateUser",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD36QI6DIZVE73H5MT5HZXR3D6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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

						/*Radix::SystemMonitor::DashConfig:lastUpdateUser:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::DashConfig:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ47RUGDGZBEQTC45BMJSTPZKIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::DashConfig:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPXXAJTTI25FEBDAS2XVEWSLNUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYTMICZFELFBFTBKR3SIYEWREGQ")},
			false,false,false);
}

/* Radix::SystemMonitor::DashConfig - Web Executable*/

/*Radix::SystemMonitor::DashConfig-Entity Class*/

package org.radixware.ads.SystemMonitor.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig")
public interface DashConfig {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.SystemMonitor.web.DashConfig.DashConfig_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.SystemMonitor.web.DashConfig.DashConfig_DefaultModel )  super.getEntity(i);}
	}















































	/*Radix::SystemMonitor::DashConfig:lastUpdateUser:lastUpdateUser-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateUser:lastUpdateUser")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateUser:lastUpdateUser")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUser getLastUpdateUser();
	/*Radix::SystemMonitor::DashConfig:lastUpdateTime:lastUpdateTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::SystemMonitor::DashConfig:guid:guid-Presentation Property*/


	public class Guid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Guid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::SystemMonitor::DashConfig:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::SystemMonitor::DashConfig:xmlContent:xmlContent-Presentation Property*/


	public class XmlContent extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public XmlContent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:xmlContent:xmlContent")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::DashConfig:xmlContent:xmlContent")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public XmlContent getXmlContent();


}

/* Radix::SystemMonitor::DashConfig - Web Meta*/

/*Radix::SystemMonitor::DashConfig-Entity Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DashConfig_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::DashConfig:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
			"Radix::SystemMonitor::DashConfig",
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX24SICCPQNDG5BUNUR6D3MJVVM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBJR3SBGZRHLBL47TM7P2SIVWQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX24SICCPQNDG5BUNUR6D3MJVVM"),0,

			/*Radix::SystemMonitor::DashConfig:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::DashConfig:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),
						"guid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3VMUBOPSH5DU7GIL5YUZD5DMI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::DashConfig:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::DashConfig:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFW3DSYQKFDDVETRYFWPEWYBZU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::DashConfig:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,300,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::DashConfig:xmlContent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXTE7QVLZ3FGCFDGLAXN7EWNN54"),
						"xmlContent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5GHBZGTBNBTDOFKOQK6RZ4CPY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
						org.radixware.kernel.common.enums.EValType.CLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						true,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::DashConfig:xmlContent:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::DashConfig:lastUpdateUser:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),
						"lastUpdateUser",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD36QI6DIZVE73H5MT5HZXR3D6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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

						/*Radix::SystemMonitor::DashConfig:lastUpdateUser:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::DashConfig:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ47RUGDGZBEQTC45BMJSTPZKIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::DashConfig:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPXXAJTTI25FEBDAS2XVEWSLNUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYTMICZFELFBFTBKR3SIYEWREGQ")},
			false,false,false);
}

/* Radix::SystemMonitor::DashConfig:General - Desktop Meta*/

/*Radix::SystemMonitor::DashConfig:General-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
			null,
			null,

			/*Radix::SystemMonitor::DashConfig:General:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::SystemMonitor::DashConfig:General:GeneralPage-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgACUCWVG3YND45MWRVBVFVNGOM4"),"GeneralPage",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),0,0,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgACUCWVG3YND45MWRVBVFVNGOM4"))}
			,

			/*Radix::SystemMonitor::DashConfig:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.DashConfig.DashConfig_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::SystemMonitor::DashConfig:General - Web Meta*/

/*Radix::SystemMonitor::DashConfig:General-Editor Presentation*/

package org.radixware.ads.SystemMonitor.web;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
			null,
			null,

			/*Radix::SystemMonitor::DashConfig:General:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::SystemMonitor::DashConfig:General:GeneralPage-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgACUCWVG3YND45MWRVBVFVNGOM4"),"GeneralPage",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),0,0,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgACUCWVG3YND45MWRVBVFVNGOM4"))}
			,

			/*Radix::SystemMonitor::DashConfig:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.web.DashConfig.DashConfig_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::SystemMonitor::DashConfig:General - Desktop Meta*/

/*Radix::SystemMonitor::DashConfig:General-Selector Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		true,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGX6ZWGYBRA5NL5I6VB6XVEQCU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBTUQEM7CVG23IIHYZCPEWXMWA")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXTE7QVLZ3FGCFDGLAXN7EWNN54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIZ42C7525EIFIPH3BXCJKMJWA")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.DashConfig.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::DashConfig:General - Web Meta*/

/*Radix::SystemMonitor::DashConfig:General-Selector Presentation*/

package org.radixware.ads.SystemMonitor.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		true,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGX6ZWGYBRA5NL5I6VB6XVEQCU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBTUQEM7CVG23IIHYZCPEWXMWA")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXTE7QVLZ3FGCFDGLAXN7EWNN54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIZ42C7525EIFIPH3BXCJKMJWA")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.web.DashConfig.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::DashConfig:WithoutXmlContent - Desktop Meta*/

/*Radix::SystemMonitor::DashConfig:WithoutXmlContent-Selector Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class WithoutXmlContent_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new WithoutXmlContent_mi();
	private WithoutXmlContent_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPXXAJTTI25FEBDAS2XVEWSLNUA"),
		"WithoutXmlContent",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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
		0,null,
		16560,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ4TMNSTCYJAKPNRTPXKEIAZI44")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF73O7PPGKRCTHBN7Z74F5GGNPU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.DashConfig.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::DashConfig:WithoutXmlContent - Web Meta*/

/*Radix::SystemMonitor::DashConfig:WithoutXmlContent-Selector Presentation*/

package org.radixware.ads.SystemMonitor.web;
public final class WithoutXmlContent_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new WithoutXmlContent_mi();
	private WithoutXmlContent_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPXXAJTTI25FEBDAS2XVEWSLNUA"),
		"WithoutXmlContent",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
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
		0,null,
		16560,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ4TMNSTCYJAKPNRTPXKEIAZI44")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF73O7PPGKRCTHBN7Z74F5GGNPU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.web.DashConfig.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::DashConfig:ForPartitionsGroup - Desktop Meta*/

/*Radix::SystemMonitor::DashConfig:ForPartitionsGroup-Selector Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class ForPartitionsGroup_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForPartitionsGroup_mi();
	private ForPartitionsGroup_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYTMICZFELFBFTBKR3SIYEWREGQ"),
		"ForPartitionsGroup",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		null,
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16571,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZQJ3SLTOBARRHQGEJY7JZOZPU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.DashConfig.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::DashConfig:ForPartitionsGroup - Web Meta*/

/*Radix::SystemMonitor::DashConfig:ForPartitionsGroup-Selector Presentation*/

package org.radixware.ads.SystemMonitor.web;
public final class ForPartitionsGroup_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForPartitionsGroup_mi();
	private ForPartitionsGroup_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprYTMICZFELFBFTBKR3SIYEWREGQ"),
		"ForPartitionsGroup",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
		null,
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16571,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprYEHWQLSO2ZC6HN23VV65FTZYWM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQVV4T5O6B5COJMBD3EZL53BF3A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZQJ3SLTOBARRHQGEJY7JZOZPU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBD47EI456VGBLLZ5AUVGEZFKOU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JC6467ZUBDORDSTW5SLDQMFVI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.web.DashConfig.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::DashConfig - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DashConfig - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Guid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Guid");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3VMUBOPSH5DU7GIL5YUZD5DMI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Releases the resources of the given cached user object.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Высвобождение ресурсов, занятых данным кешированным пользовательским объектом.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAV64OITKVNBJBE3QMVV7SYTVTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заголовок");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBTUQEM7CVG23IIHYZCPEWXMWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь совершивший последнее обновление");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD36QI6DIZVE73H5MT5HZXR3D6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заголовок");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF73O7PPGKRCTHBN7Z74F5GGNPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Xml Content");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Xml Content");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIZ42C7525EIFIPH3BXCJKMJWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последнего обновления");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ47RUGDGZBEQTC45BMJSTPZKIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Guid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Guid");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGX6ZWGYBRA5NL5I6VB6XVEQCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заголовок");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZQJ3SLTOBARRHQGEJY7JZOZPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Guid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Guid");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ4TMNSTCYJAKPNRTPXKEIAZI44"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Xml Content");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Xml");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5GHBZGTBNBTDOFKOQK6RZ4CPY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dashboard configurations");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конфигурации приборной панели");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBJR3SBGZRHLBL47TM7P2SIVWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заголовок");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFW3DSYQKFDDVETRYFWPEWYBZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dashboard Configuration");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конфигурация приборной панели");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX24SICCPQNDG5BUNUR6D3MJVVM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(DashConfig - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecVGSKITRX4FHZJAGAQ7V7VGZUJY"),"DashConfig - Localizing Bundle",$$$items$$$);
}

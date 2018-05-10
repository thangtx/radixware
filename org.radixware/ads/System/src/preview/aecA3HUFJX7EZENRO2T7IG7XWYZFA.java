
/* Radix::System::EventSeverity - Server Executable*/

/*Radix::System::EventSeverity-Entity Class*/

package org.radixware.ads.System.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity")
public final published class EventSeverity  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private Meta::EventCode2EventParams reference=null;
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EventSeverity_mi.rdxMeta;}

	/*Radix::System::EventSeverity:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:Properties-Properties*/

	/*Radix::System::EventSeverity:eventCode-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCode")
	public  Str getEventCode() {
		return eventCode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCode")
	public   void setEventCode(Str val) {
		eventCode = val;
	}

	/*Radix::System::EventSeverity:eventSeverity-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSeverity")
	public  Int getEventSeverity() {
		return eventSeverity;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSeverity")
	public   void setEventSeverity(Int val) {
		eventSeverity = val;
	}

	/*Radix::System::EventSeverity:eventMessage-Dynamic Property*/



	protected Str eventMessage=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventMessage")
	public  Str getEventMessage() {

		if (eventParams == null && eventCode != null) {
		    return Utils::MessageFormatter.format("Event code \"{0}\" not found", eventCode);
		}

		String result = Arte::Arte.getDefManager().getEventTitleByCode(eventCode, Arte::Arte.getClientLanguage());
		if (result == null) {
		    return "";
		} else
		    return result;

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventMessage")
	public   void setEventMessage(Str val) {
		eventMessage = val;
	}

	/*Radix::System::EventSeverity:eventSource-Dynamic Property*/



	protected Str eventSource=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSource")
	public published  Str getEventSource() {

		Meta::EventCode2EventParams params = eventParams;

		if (params != null) {
		    return params.eventSource;
		}

		return null;

		//if ( != null) {
		//    String result = .().getEventSourceByCode();
		//    if (result == null) {
		//        return null;
		//    } else
		//        return result;
		//}else {
		//    return  null;
		//}

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSource")
	public published   void setEventSource(Str val) {
		eventSource = val;
	}

	/*Radix::System::EventSeverity:originalSeverity-Dynamic Property*/



	protected org.radixware.kernel.common.enums.EEventSeverity originalSeverity=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:originalSeverity")
	public published  org.radixware.kernel.common.enums.EEventSeverity getOriginalSeverity() {

		Meta::EventCode2EventParams params = eventParams;

		if(params!=null){
		    return params.eventSeverity;
		}

		return null;
		//if( != null){
		//    return  .().getEventSeverityByCode();    
		//}else{
		//    return null;
		//}


		/*if (== null &&  != null) {
		    try {
		         = .();
		    } catch (org.radixware.kernel.common.exceptions.WrongFormatError e) {
		         = null;
		    }
		}

		return ;*/
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:originalSeverity")
	public published   void setOriginalSeverity(org.radixware.kernel.common.enums.EEventSeverity val) {
		originalSeverity = val;
	}

	/*Radix::System::EventSeverity:severity-Dynamic Property*/



	protected org.radixware.kernel.common.enums.EEventSeverity severity=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:severity")
	public published  org.radixware.kernel.common.enums.EEventSeverity getSeverity() {

		return eventSeverity!=null ? Arte::EventSeverity.getForValue(eventSeverity) : null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:severity")
	public published   void setSeverity(org.radixware.kernel.common.enums.EEventSeverity val) {

		eventSeverity=val.Value;

	}

	/*Radix::System::EventSeverity:eventCodeTitle-Dynamic Property*/



	protected Str eventCodeTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCodeTitle")
	public  Str getEventCodeTitle() {

		if (internal[eventCodeTitle]== null && eventCode != null) {
		    try {
		        internal[eventCodeTitle] = Meta::Utils.getEventTitleByCode(eventCode);
		    } catch (org.radixware.kernel.common.exceptions.WrongFormatError e) {
		        internal[eventCodeTitle] = e.getMessage();
		    }
		}
		return internal[eventCodeTitle];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCodeTitle")
	public   void setEventCodeTitle(Str val) {
		eventCodeTitle = val;
	}

	/*Radix::System::EventSeverity:eventParams-Dynamic Property*/



	protected org.radixware.ads.Meta.server.EventCode2EventParams eventParams=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventParams")
	public  org.radixware.ads.Meta.server.EventCode2EventParams getEventParams() {

		if(reference == null && this.eventCode != null){
		    reference = Meta::EventCode2EventParams.loadByPK(this.eventCode,Arte::Arte.getVersion(),true);
		}
		return reference;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventParams")
	public   void setEventParams(org.radixware.ads.Meta.server.EventCode2EventParams val) {
		eventParams = val;
	}

	/*Radix::System::EventSeverity:source-Dynamic Property*/



	protected org.radixware.ads.Arte.common.EventSource source=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:source")
	public  org.radixware.ads.Arte.common.EventSource getSource() {

		if (internal[source] == null) {
		    try {
		        internal[source] = Meta::Utils.getEventSourceByCode(eventCode);
		    } catch (org.radixware.kernel.common.exceptions.WrongFormatError e) {
		        internal[source] = null;
		    }
		}

		return internal[source];
	}















































































	/*Radix::System::EventSeverity:Methods-Methods*/


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::System::EventSeverity - Server Meta*/

/*Radix::System::EventSeverity-Entity Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSeverity_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),"EventSeverity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHV4MUFBT6FBT5EUIZFQTK4PBK4"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::System::EventSeverity:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
							/*Owner Class Name*/
							"EventSeverity",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHV4MUFBT6FBT5EUIZFQTK4PBK4"),
							/*Property presentations*/

							/*Radix::System::EventSeverity:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::System::EventSeverity:eventCode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7CBA7KQU55FFJCODUNPCIH5MZE"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventSeverity:eventSeverity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5OCVEPMSFHUZC5GJJX7WUNEFI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventSeverity:eventMessage:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFBFIIRQYZ5BQHHB5I36KILK5YE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventSeverity:eventSource:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventSeverity:originalSeverity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventSeverity:severity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventSeverity:eventCodeTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDYM6W5ES5BDA5OWHDQFDPNVWBE"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventSeverity:eventParams:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEPHGMVODL5EFVCKXGMPG6GVFW4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::EventSeverity:source:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd52GDRSSBBNBY3KVS3THIG7J27A"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::System::EventSeverity:Severity-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),"Severity",null,new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5OCVEPMSFHUZC5GJJX7WUNEFI"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventSeverity:Source-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltPGO4JWJBYVDH3GRWJS6NPVQEEU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),"Source",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AH37G5EINCPVCJLC65AWHTRZI"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Parameter ParamId=\"prmSXIY7X7SXVHBPL4IZ7H5CTBXBY\"/></xsc:Item><xsc:Item><xsc:Sql> in (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colTBQMLGFG5NFX3J4GNHTGJ4Z35U\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\"/></xsc:Item><xsc:Item><xsc:Sql>\nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colWIFTDY55GZHMVFNTUWMJFMFHSM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecA3HUFJX7EZENRO2T7IG7XWYZFA\" PropId=\"col7CBA7KQU55FFJCODUNPCIH5MZE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),true,null,false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventSeverity:Severity-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3HRUV3UCMFA5LJ4BEN3LA75PKY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),"Severity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKI6SK7ELZG4BHA5JDQKY3QVJ4"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Parameter ParamId=\"prm6NNOXLLAQ5HJDL3D362WFX7CMQ\"/></xsc:Item><xsc:Item><xsc:Sql> is null or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm6NNOXLLAQ5HJDL3D362WFX7CMQ\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecA3HUFJX7EZENRO2T7IG7XWYZFA\" PropId=\"colK5OCVEPMSFHUZC5GJJX7WUNEFI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),true,null,false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventSeverity:OriginSeverity-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltJT6TD4Q6UJDCHD6AYNRW36JALI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),"OriginSeverity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYYJAQZGJPFHAPHOM4QLGW3MYQA"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Parameter ParamId=\"prmJDB3C7OK2NA4LKF3RUDUV473PU\"/></xsc:Item><xsc:Item><xsc:Sql> in (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colXMA7PRAFMNEM7LT4GY4Z4MAXFQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\"/></xsc:Item><xsc:Item><xsc:Sql>\nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colWIFTDY55GZHMVFNTUWMJFMFHSM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecA3HUFJX7EZENRO2T7IG7XWYZFA\" PropId=\"col7CBA7KQU55FFJCODUNPCIH5MZE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),true,null,false,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::EventSeverity:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::System::EventSeverity:Create:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::EventSeverity:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRQ4ZHIMKENEFZFXEXN77X3LTRE"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI"),
									36018,
									null,

									/*Radix::System::EventSeverity:Edit:Children-Explorer Items*/
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
									/*Radix::System::EventSeverity:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprCQ4M24HWBRGGFHDLJTS3VHATJA"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFBFIIRQYZ5BQHHB5I36KILK5YE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,true,null,false,null,true,null,false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRQ4ZHIMKENEFZFXEXN77X3LTRE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null)
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::System::EventSeverity:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),null,null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::System::EventSeverity:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::System::EventSeverity:eventCode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7CBA7KQU55FFJCODUNPCIH5MZE"),"eventCode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2GIXD4DMARG2RMIVENCJOFGKUM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventSeverity:eventSeverity-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5OCVEPMSFHUZC5GJJX7WUNEFI"),"eventSeverity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7YVL7YNXAZEP7GN4BDDHCTHJ5U"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventSeverity:eventMessage-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFBFIIRQYZ5BQHHB5I36KILK5YE"),"eventMessage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHTX4SNL5VGHXIWMONEEWXG5PY"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventSeverity:eventSource-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),"eventSource",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNMMPUFLT3FFRLNIU47HIPFA35Q"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventSeverity:originalSeverity-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),"originalSeverity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYGYOPYQ7FE6BKOR56FC54ORIM"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventSeverity:severity-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),"severity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAFZEBIKUURHIBLGMC5CHYH6TNM"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventSeverity:eventCodeTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDYM6W5ES5BDA5OWHDQFDPNVWBE"),"eventCodeTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFPMWPZY2BCWFGGDKUGW5BKO2E"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventSeverity:eventParams-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEPHGMVODL5EFVCKXGMPG6GVFW4"),"eventParams",null,org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQEGQZYZRSJD57JKZRV5NJY6U3I"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventSeverity:source-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd52GDRSSBBNBY3KVS3THIG7J27A"),"source",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYCLQHJZP3JHBRJ6CB5VDCDJOWM"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::System::EventSeverity:Methods-Methods*/
						null,
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::System::EventSeverity - Desktop Executable*/

/*Radix::System::EventSeverity-Entity Class*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity")
public interface EventSeverity {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.System.explorer.EventSeverity.EventSeverity_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.explorer.EventSeverity.EventSeverity_DefaultModel )  super.getEntity(i);}
	}



































































	/*Radix::System::EventSeverity:eventCode:eventCode-Presentation Property*/


	public class EventCode extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventCode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCode:eventCode")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCode:eventCode")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventCode getEventCode();
	/*Radix::System::EventSeverity:eventSeverity:eventSeverity-Presentation Property*/


	public class EventSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EventSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSeverity:eventSeverity")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSeverity:eventSeverity")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EventSeverity getEventSeverity();
	/*Radix::System::EventSeverity:source:source-Presentation Property*/


	public class Source extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventSource> getValClass(){
			return org.radixware.ads.Arte.common.EventSource.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:source:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:source:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource();
	/*Radix::System::EventSeverity:eventCodeTitle:eventCodeTitle-Presentation Property*/


	public class EventCodeTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventCodeTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCodeTitle:eventCodeTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCodeTitle:eventCodeTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventCodeTitle getEventCodeTitle();
	/*Radix::System::EventSeverity:eventParams:eventParams-Presentation Property*/


	public class EventParams extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public EventParams(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Meta.explorer.EventCode2EventParams.EventCode2EventParams_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Meta.explorer.EventCode2EventParams.EventCode2EventParams_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Meta.explorer.EventCode2EventParams.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Meta.explorer.EventCode2EventParams.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventParams:eventParams")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventParams:eventParams")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public EventParams getEventParams();
	/*Radix::System::EventSeverity:eventMessage:eventMessage-Presentation Property*/


	public class EventMessage extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventMessage(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventMessage:eventMessage")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventMessage:eventMessage")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventMessage getEventMessage();
	/*Radix::System::EventSeverity:severity:severity-Presentation Property*/


	public class Severity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Severity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:severity:severity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:severity:severity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public Severity getSeverity();
	/*Radix::System::EventSeverity:originalSeverity:originalSeverity-Presentation Property*/


	public class OriginalSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OriginalSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:originalSeverity:originalSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:originalSeverity:originalSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public OriginalSeverity getOriginalSeverity();
	/*Radix::System::EventSeverity:eventSource:eventSource-Presentation Property*/


	public class EventSource extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventSource(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSource:eventSource")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSource:eventSource")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventSource getEventSource();


}

/* Radix::System::EventSeverity - Desktop Meta*/

/*Radix::System::EventSeverity-Entity Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSeverity_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::EventSeverity:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
			"Radix::System::EventSeverity",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZK4L5MHDOEIH64ZER4MR46XGGIGCJ2WH"),
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHV4MUFBT6FBT5EUIZFQTK4PBK4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIORHPSACLRBM5NKFGXL7VMEAQY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHV4MUFBT6FBT5EUIZFQTK4PBK4"),0,

			/*Radix::System::EventSeverity:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::EventSeverity:eventCode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7CBA7KQU55FFJCODUNPCIH5MZE"),
						"eventCode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2GIXD4DMARG2RMIVENCJOFGKUM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeJPPYKYT4CVBFXJTNYI5YCA4IQQ"),
						true,

						/*Radix::System::EventSeverity:eventCode:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5OCVEPMSFHUZC5GJJX7WUNEFI"),
						"eventSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7YVL7YNXAZEP7GN4BDDHCTHJ5U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::EventSeverity:eventSeverity:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventMessage:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFBFIIRQYZ5BQHHB5I36KILK5YE"),
						"eventMessage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHTX4SNL5VGHXIWMONEEWXG5PY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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

						/*Radix::System::EventSeverity:eventMessage:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventSource:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),
						"eventSource",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNMMPUFLT3FFRLNIU47HIPFA35Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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

						/*Radix::System::EventSeverity:eventSource:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:originalSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),
						"originalSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYGYOPYQ7FE6BKOR56FC54ORIM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::System::EventSeverity:originalSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:severity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),
						"severity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAFZEBIKUURHIBLGMC5CHYH6TNM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::System::EventSeverity:severity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventCodeTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDYM6W5ES5BDA5OWHDQFDPNVWBE"),
						"eventCodeTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFPMWPZY2BCWFGGDKUGW5BKO2E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeJPPYKYT4CVBFXJTNYI5YCA4IQQ"),
						true,

						/*Radix::System::EventSeverity:eventCodeTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventParams:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEPHGMVODL5EFVCKXGMPG6GVFW4"),
						"eventParams",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQEGQZYZRSJD57JKZRV5NJY6U3I"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQEGQZYZRSJD57JKZRV5NJY6U3I"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::System::EventSeverity:source:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd52GDRSSBBNBY3KVS3THIG7J27A"),
						"source",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYCLQHJZP3JHBRJ6CB5VDCDJOWM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeNFVGJDG7QVAQXGMXNLZTX6CQRY"),
						true,

						/*Radix::System::EventSeverity:source:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7EPPYDJQJDOHERIUFAZ25XQBA"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventSeverity:Source-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltPGO4JWJBYVDH3GRWJS6NPVQEEU"),
						"Source",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AH37G5EINCPVCJLC65AWHTRZI"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Parameter ParamId=\"prmSXIY7X7SXVHBPL4IZ7H5CTBXBY\"/></xsc:Item><xsc:Item><xsc:Sql> in (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colTBQMLGFG5NFX3J4GNHTGJ4Z35U\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\"/></xsc:Item><xsc:Item><xsc:Sql>\nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colWIFTDY55GZHMVFNTUWMJFMFHSM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecA3HUFJX7EZENRO2T7IG7XWYZFA\" PropId=\"col7CBA7KQU55FFJCODUNPCIH5MZE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSXIY7X7SXVHBPL4IZ7H5CTBXBY"),
								"pSource",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSZ6ZYG6JZEAJPBDOHWLEYNGPU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::System::EventSeverity:Source:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventSeverity:Severity-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3HRUV3UCMFA5LJ4BEN3LA75PKY"),
						"Severity",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKI6SK7ELZG4BHA5JDQKY3QVJ4"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Parameter ParamId=\"prm6NNOXLLAQ5HJDL3D362WFX7CMQ\"/></xsc:Item><xsc:Item><xsc:Sql> is null or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm6NNOXLLAQ5HJDL3D362WFX7CMQ\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecA3HUFJX7EZENRO2T7IG7XWYZFA\" PropId=\"colK5OCVEPMSFHUZC5GJJX7WUNEFI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6NNOXLLAQ5HJDL3D362WFX7CMQ"),
								"pSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAZ225VX4RGRTOXFF7Z5FUI6HI"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::System::EventSeverity:Severity:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventSeverity:OriginSeverity-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltJT6TD4Q6UJDCHD6AYNRW36JALI"),
						"OriginSeverity",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYYJAQZGJPFHAPHOM4QLGW3MYQA"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Parameter ParamId=\"prmJDB3C7OK2NA4LKF3RUDUV473PU\"/></xsc:Item><xsc:Item><xsc:Sql> in (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colXMA7PRAFMNEM7LT4GY4Z4MAXFQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\"/></xsc:Item><xsc:Item><xsc:Sql>\nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colWIFTDY55GZHMVFNTUWMJFMFHSM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecA3HUFJX7EZENRO2T7IG7XWYZFA\" PropId=\"col7CBA7KQU55FFJCODUNPCIH5MZE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmJDB3C7OK2NA4LKF3RUDUV473PU"),
								"pOriginSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV6HCFJIVD5ACVBS3ANJMZME4RM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::System::EventSeverity:OriginSeverity:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::EventSeverity:Severity-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),
						"Severity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5OCVEPMSFHUZC5GJJX7WUNEFI"),
								false)
						})
			},
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRQ4ZHIMKENEFZFXEXN77X3LTRE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprCQ4M24HWBRGGFHDLJTS3VHATJA")},
			false,false,false);
}

/* Radix::System::EventSeverity - Web Executable*/

/*Radix::System::EventSeverity-Entity Class*/

package org.radixware.ads.System.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity")
public interface EventSeverity {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.System.web.EventSeverity.EventSeverity_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.web.EventSeverity.EventSeverity_DefaultModel )  super.getEntity(i);}
	}



































































	/*Radix::System::EventSeverity:eventCode:eventCode-Presentation Property*/


	public class EventCode extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventCode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCode:eventCode")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCode:eventCode")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventCode getEventCode();
	/*Radix::System::EventSeverity:eventSeverity:eventSeverity-Presentation Property*/


	public class EventSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EventSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSeverity:eventSeverity")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSeverity:eventSeverity")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EventSeverity getEventSeverity();
	/*Radix::System::EventSeverity:source:source-Presentation Property*/


	public class Source extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventSource> getValClass(){
			return org.radixware.ads.Arte.common.EventSource.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:source:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:source:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource();
	/*Radix::System::EventSeverity:eventCodeTitle:eventCodeTitle-Presentation Property*/


	public class EventCodeTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventCodeTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCodeTitle:eventCodeTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventCodeTitle:eventCodeTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventCodeTitle getEventCodeTitle();
	/*Radix::System::EventSeverity:eventParams:eventParams-Presentation Property*/


	public class EventParams extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public EventParams(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Meta.web.EventCode2EventParams.EventCode2EventParams_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Meta.web.EventCode2EventParams.EventCode2EventParams_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Meta.web.EventCode2EventParams.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Meta.web.EventCode2EventParams.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventParams:eventParams")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventParams:eventParams")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public EventParams getEventParams();
	/*Radix::System::EventSeverity:eventMessage:eventMessage-Presentation Property*/


	public class EventMessage extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventMessage(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventMessage:eventMessage")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventMessage:eventMessage")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventMessage getEventMessage();
	/*Radix::System::EventSeverity:severity:severity-Presentation Property*/


	public class Severity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Severity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:severity:severity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:severity:severity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public Severity getSeverity();
	/*Radix::System::EventSeverity:originalSeverity:originalSeverity-Presentation Property*/


	public class OriginalSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OriginalSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:originalSeverity:originalSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:originalSeverity:originalSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public OriginalSeverity getOriginalSeverity();
	/*Radix::System::EventSeverity:eventSource:eventSource-Presentation Property*/


	public class EventSource extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventSource(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSource:eventSource")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:eventSource:eventSource")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventSource getEventSource();


}

/* Radix::System::EventSeverity - Web Meta*/

/*Radix::System::EventSeverity-Entity Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSeverity_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::EventSeverity:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
			"Radix::System::EventSeverity",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZK4L5MHDOEIH64ZER4MR46XGGIGCJ2WH"),
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHV4MUFBT6FBT5EUIZFQTK4PBK4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIORHPSACLRBM5NKFGXL7VMEAQY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHV4MUFBT6FBT5EUIZFQTK4PBK4"),0,

			/*Radix::System::EventSeverity:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::EventSeverity:eventCode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7CBA7KQU55FFJCODUNPCIH5MZE"),
						"eventCode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2GIXD4DMARG2RMIVENCJOFGKUM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeJR45J5TXOJBNJJEDT2CFPLSC3I"),
						true,

						/*Radix::System::EventSeverity:eventCode:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5OCVEPMSFHUZC5GJJX7WUNEFI"),
						"eventSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7YVL7YNXAZEP7GN4BDDHCTHJ5U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::EventSeverity:eventSeverity:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventMessage:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFBFIIRQYZ5BQHHB5I36KILK5YE"),
						"eventMessage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHTX4SNL5VGHXIWMONEEWXG5PY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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

						/*Radix::System::EventSeverity:eventMessage:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventSource:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),
						"eventSource",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNMMPUFLT3FFRLNIU47HIPFA35Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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

						/*Radix::System::EventSeverity:eventSource:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:originalSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),
						"originalSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYGYOPYQ7FE6BKOR56FC54ORIM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::System::EventSeverity:originalSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:severity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),
						"severity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAFZEBIKUURHIBLGMC5CHYH6TNM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::System::EventSeverity:severity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventCodeTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDYM6W5ES5BDA5OWHDQFDPNVWBE"),
						"eventCodeTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFPMWPZY2BCWFGGDKUGW5BKO2E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeJR45J5TXOJBNJJEDT2CFPLSC3I"),
						true,

						/*Radix::System::EventSeverity:eventCodeTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventSeverity:eventParams:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEPHGMVODL5EFVCKXGMPG6GVFW4"),
						"eventParams",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQEGQZYZRSJD57JKZRV5NJY6U3I"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQEGQZYZRSJD57JKZRV5NJY6U3I"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::System::EventSeverity:source:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd52GDRSSBBNBY3KVS3THIG7J27A"),
						"source",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYCLQHJZP3JHBRJ6CB5VDCDJOWM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeZR4KNFFFEJGF3ARHZC76NI3WXA"),
						true,

						/*Radix::System::EventSeverity:source:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7EPPYDJQJDOHERIUFAZ25XQBA"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventSeverity:Source-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltPGO4JWJBYVDH3GRWJS6NPVQEEU"),
						"Source",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AH37G5EINCPVCJLC65AWHTRZI"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Parameter ParamId=\"prmSXIY7X7SXVHBPL4IZ7H5CTBXBY\"/></xsc:Item><xsc:Item><xsc:Sql> in (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colTBQMLGFG5NFX3J4GNHTGJ4Z35U\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\"/></xsc:Item><xsc:Item><xsc:Sql>\nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colWIFTDY55GZHMVFNTUWMJFMFHSM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecA3HUFJX7EZENRO2T7IG7XWYZFA\" PropId=\"col7CBA7KQU55FFJCODUNPCIH5MZE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSXIY7X7SXVHBPL4IZ7H5CTBXBY"),
								"pSource",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSZ6ZYG6JZEAJPBDOHWLEYNGPU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::System::EventSeverity:Source:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventSeverity:Severity-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3HRUV3UCMFA5LJ4BEN3LA75PKY"),
						"Severity",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKI6SK7ELZG4BHA5JDQKY3QVJ4"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Parameter ParamId=\"prm6NNOXLLAQ5HJDL3D362WFX7CMQ\"/></xsc:Item><xsc:Item><xsc:Sql> is null or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm6NNOXLLAQ5HJDL3D362WFX7CMQ\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecA3HUFJX7EZENRO2T7IG7XWYZFA\" PropId=\"colK5OCVEPMSFHUZC5GJJX7WUNEFI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6NNOXLLAQ5HJDL3D362WFX7CMQ"),
								"pSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAZ225VX4RGRTOXFF7Z5FUI6HI"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::System::EventSeverity:Severity:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventSeverity:OriginSeverity-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltJT6TD4Q6UJDCHD6AYNRW36JALI"),
						"OriginSeverity",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYYJAQZGJPFHAPHOM4QLGW3MYQA"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Parameter ParamId=\"prmJDB3C7OK2NA4LKF3RUDUV473PU\"/></xsc:Item><xsc:Item><xsc:Sql> in (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colXMA7PRAFMNEM7LT4GY4Z4MAXFQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\"/></xsc:Item><xsc:Item><xsc:Sql>\nwhere </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblQEGQZYZRSJD57JKZRV5NJY6U3I\" PropId=\"colWIFTDY55GZHMVFNTUWMJFMFHSM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecA3HUFJX7EZENRO2T7IG7XWYZFA\" PropId=\"col7CBA7KQU55FFJCODUNPCIH5MZE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmJDB3C7OK2NA4LKF3RUDUV473PU"),
								"pOriginSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV6HCFJIVD5ACVBS3ANJMZME4RM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::System::EventSeverity:OriginSeverity:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::EventSeverity:Severity-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtCNKFA65L7BDYPH7ZFT3BORDX6E"),
						"Severity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5OCVEPMSFHUZC5GJJX7WUNEFI"),
								false)
						})
			},
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRQ4ZHIMKENEFZFXEXN77X3LTRE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprCQ4M24HWBRGGFHDLJTS3VHATJA")},
			false,false,false);
}

/* Radix::System::EventSeverity:Create - Desktop Meta*/

/*Radix::System::EventSeverity:Create-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
	null,
	null,

	/*Radix::System::EventSeverity:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::EventSeverity:Create:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg43T2SWH3QVF73JPS7S55KBB4EM"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDYM6W5ES5BDA5OWHDQFDPNVWBE"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg43T2SWH3QVF73JPS7S55KBB4EM"))}
	,

	/*Radix::System::EventSeverity:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::System::EventSeverity:Create - Web Meta*/

/*Radix::System::EventSeverity:Create-Editor Presentation*/

package org.radixware.ads.System.web;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
	null,
	null,

	/*Radix::System::EventSeverity:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::EventSeverity:Create:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg43T2SWH3QVF73JPS7S55KBB4EM"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDYM6W5ES5BDA5OWHDQFDPNVWBE"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg43T2SWH3QVF73JPS7S55KBB4EM"))}
	,

	/*Radix::System::EventSeverity:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::System::EventSeverity:Create:Model - Desktop Executable*/

/*Radix::System::EventSeverity:Create:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model")
public class Create:Model  extends org.radixware.ads.System.explorer.EventSeverity.EventSeverity_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:Create:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:Create:Model:Properties-Properties*/

	/*Radix::System::EventSeverity:Create:Model:severity-Presentation Property*/




	public class Severity extends org.radixware.ads.System.explorer.EventSeverity.prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ{
		public Severity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:severity")
		public published  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:severity")
		public published   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {

			internal[severity]=val;
			eventSeverity.setValueObject(val.Value);
		}
	}
	public Severity getSeverity(){return (Severity)getProperty(prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ);}

	/*Radix::System::EventSeverity:Create:Model:source-Presentation Property*/




	public class Source extends org.radixware.ads.System.explorer.EventSeverity.prd52GDRSSBBNBY3KVS3THIG7J27A{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventSource> getValClass(){
			return org.radixware.ads.Arte.common.EventSource.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource(){return (Source)getProperty(prd52GDRSSBBNBY3KVS3THIG7J27A);}










	/*Radix::System::EventSeverity:Create:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:Create:Model:afterClosePropEditorDialog-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:afterClosePropEditorDialog")
	public published  org.radixware.kernel.common.client.views.IDialog.DialogResult afterClosePropEditorDialog (org.radixware.kernel.common.client.models.items.properties.Property property, org.radixware.kernel.common.client.models.PropEditorModel propertyEditorModel, org.radixware.kernel.common.client.views.IDialog.DialogResult result) {
		Client.Views::DialogResult res=super.afterClosePropEditorDialog(property, propertyEditorModel, result);
		if (property.getId()==idof[EventSeverity:eventCodeTitle] && res==Client.Views::DialogResult.ACCEPTED ){
		    Common.Dlg::EventCodeChoiceDlg:Model model = (Common.Dlg::EventCodeChoiceDlg:Model)propertyEditorModel;
		    originalSeverity.setValueObject(model.resEventSeverity);
		    eventSource.setValueObject(model.resEventSource.Value);
		    //.setValueObject(propertyEditorModel.().());
		    eventCodeTitle.setValueObject(model.resTitle);

		   //  excluded = Environment.DefManager.getEnumPresentationDef().();
		   // excluded.(model.);
		   // (().EditMask).(excluded);
		}
		setupEditor();
		return res;
	}

	/*Radix::System::EventSeverity:Create:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		setupEditor();
	}

	/*Radix::System::EventSeverity:Create:Model:setupEditor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:setupEditor")
	public  void setupEditor () {
		Explorer.Meta::EnumItems excluded = Environment.DefManager.getEnumPresentationDef(idof[Arte::EventSeverity]).getEmptyItems();
		if (originalSeverity.Value != null) {
		    excluded.addItem(originalSeverity.Value);
		}
		if (eventSource.Value != null && eventSource.Value.startsWith(Arte::EventSource:APP_AUDIT.Value)) {
		    excluded.addItem(Arte::EventSeverity:Debug);
		}

		((Explorer.EditMask::EditMaskConstSet) severity.EditMask).setExcludedItems(excluded);
	}

	/*Radix::System::EventSeverity:Create:Model:afterPrepareCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:afterPrepareCreate")
	protected published  boolean afterPrepareCreate () {

		if(super.afterPrepareCreate()){
		    setupEditor();
		    return true;
		}else
		    return false;
	}

	/*Radix::System::EventSeverity:Create:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if(propertyId == idof[EventSeverity:eventSource] && propertyValue instanceof String){
		    try{
		    return Arte::EventSource.getForValue((String)propertyValue).getTitle(getEnvironment().getLanguage());
		    }catch(Exceptions::NoConstItemWithSuchValueError e){
		        return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
		    }
		}

		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::System::EventSeverity:Create:Model:beforeOpenPropEditorDialog-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:beforeOpenPropEditorDialog")
	public published  void beforeOpenPropEditorDialog (org.radixware.kernel.common.client.models.items.properties.Property property, org.radixware.kernel.common.client.models.PropEditorModel propEditorModel) {
		// --------------------- explorer --------------------- 
		super.beforeOpenPropEditorDialog(property, propEditorModel);
		if(propEditorModel instanceof Common.Dlg::EventCodeChoiceDlg:Model){
		    if(property.Id == idof[EventSeverity:eventCodeTitle]) {
		        Explorer.Context::PropEditorContext newContext = 
		                new PropEditorContext( eventCode, 0);
		        propEditorModel.setContext(newContext);
		    }
		    Common.Dlg::EventCodeChoiceDlg:Model model  = ((Common.Dlg::EventCodeChoiceDlg:Model)propEditorModel);
		    model.isForRedefine=true;
		    model.parEventSourceProp = source;
		}// --------------------- web --------------------- 

	}


}

/* Radix::System::EventSeverity:Create:Model - Desktop Meta*/

/*Radix::System::EventSeverity:Create:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemDVK72KVUHVH5PNN3EV7CD2VNEI"),
						"Create:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:Create:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity:Create:Model - Web Executable*/

/*Radix::System::EventSeverity:Create:Model-Entity Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model")
public class Create:Model  extends org.radixware.ads.System.web.EventSeverity.EventSeverity_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:Create:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:Create:Model:Properties-Properties*/

	/*Radix::System::EventSeverity:Create:Model:severity-Presentation Property*/




	public class Severity extends org.radixware.ads.System.web.EventSeverity.prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ{
		public Severity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:severity")
		public published  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:severity")
		public published   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {

			internal[severity]=val;
			eventSeverity.setValueObject(val.Value);
		}
	}
	public Severity getSeverity(){return (Severity)getProperty(prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ);}

	/*Radix::System::EventSeverity:Create:Model:source-Presentation Property*/




	public class Source extends org.radixware.ads.System.web.EventSeverity.prd52GDRSSBBNBY3KVS3THIG7J27A{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventSource> getValClass(){
			return org.radixware.ads.Arte.common.EventSource.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource(){return (Source)getProperty(prd52GDRSSBBNBY3KVS3THIG7J27A);}










	/*Radix::System::EventSeverity:Create:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:Create:Model:afterClosePropEditorDialog-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:afterClosePropEditorDialog")
	public published  org.radixware.kernel.common.client.views.IDialog.DialogResult afterClosePropEditorDialog (org.radixware.kernel.common.client.models.items.properties.Property property, org.radixware.kernel.common.client.models.PropEditorModel propertyEditorModel, org.radixware.kernel.common.client.views.IDialog.DialogResult result) {
		Client.Views::DialogResult res=super.afterClosePropEditorDialog(property, propertyEditorModel, result);
		if (property.getId()==idof[EventSeverity:eventCodeTitle] && res==Client.Views::DialogResult.ACCEPTED ){   
		    Common.Dlg::EventCodeChoiceDlgWeb:Model model = (Common.Dlg::EventCodeChoiceDlgWeb:Model)propertyEditorModel;
		    originalSeverity.setValueObject(model.resEventSeverity);
		    eventSource.setValueObject(model.resEventSource.Value);
		    //.setValueObject(propertyEditorModel.().());
		    eventCodeTitle.setValueObject(model.resTitle);

		   //  excluded = Environment.DefManager.getEnumPresentationDef().();
		   // excluded.(model.);
		   // (().EditMask).(excluded);
		}
		setupEditor();
		return res;
	}

	/*Radix::System::EventSeverity:Create:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		setupEditor();
	}

	/*Radix::System::EventSeverity:Create:Model:setupEditor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:setupEditor")
	public  void setupEditor () {
		Explorer.Meta::EnumItems excluded = Environment.DefManager.getEnumPresentationDef(idof[Arte::EventSeverity]).getEmptyItems();
		if (originalSeverity.Value != null) {
		    excluded.addItem(originalSeverity.Value);
		}
		if (eventSource.Value != null && eventSource.Value.startsWith(Arte::EventSource:APP_AUDIT.Value)) {
		    excluded.addItem(Arte::EventSeverity:Debug);
		}

		((Explorer.EditMask::EditMaskConstSet) severity.EditMask).setExcludedItems(excluded);
	}

	/*Radix::System::EventSeverity:Create:Model:afterPrepareCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:afterPrepareCreate")
	protected published  boolean afterPrepareCreate () {

		if(super.afterPrepareCreate()){
		    setupEditor();
		    return true;
		}else
		    return false;
	}

	/*Radix::System::EventSeverity:Create:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if(propertyId == idof[EventSeverity:eventSource] && propertyValue instanceof String){
		    try{
		    return Arte::EventSource.getForValue((String)propertyValue).getTitle(getEnvironment().getLanguage());
		    }catch(Exceptions::NoConstItemWithSuchValueError e){
		        return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
		    }
		}

		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::System::EventSeverity:Create:Model:beforeOpenPropEditorDialog-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Create:Model:beforeOpenPropEditorDialog")
	public published  void beforeOpenPropEditorDialog (org.radixware.kernel.common.client.models.items.properties.Property property, org.radixware.kernel.common.client.models.PropEditorModel propEditorModel) {
		super.beforeOpenPropEditorDialog(property, propEditorModel);
		if(propEditorModel instanceof Common.Dlg::EventCodeChoiceDlgWeb:Model){
		    if(property.Id == idof[EventSeverity:eventCodeTitle]) {
		        Explorer.Context::PropEditorContext newContext = 
		                new PropEditorContext( eventCode, 0);
		        propEditorModel.setContext(newContext);
		    }
		    Common.Dlg::EventCodeChoiceDlgWeb:Model model  = ((Common.Dlg::EventCodeChoiceDlgWeb:Model)propEditorModel);
		    model.isForRedefine=true;
		    model.parEventSourceProp = source;
		}
	}


}

/* Radix::System::EventSeverity:Create:Model - Web Meta*/

/*Radix::System::EventSeverity:Create:Model-Entity Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemDVK72KVUHVH5PNN3EV7CD2VNEI"),
						"Create:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:Create:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity:Edit - Desktop Meta*/

/*Radix::System::EventSeverity:Edit-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRQ4ZHIMKENEFZFXEXN77X3LTRE"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
	null,
	null,

	/*Radix::System::EventSeverity:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::EventSeverity:Edit:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBI7A43DU5NCMBNT67EYMDZ4YTA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFBFIIRQYZ5BQHHB5I36KILK5YE"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDYM6W5ES5BDA5OWHDQFDPNVWBE"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBI7A43DU5NCMBNT67EYMDZ4YTA"))}
	,

	/*Radix::System::EventSeverity:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36018,0,0);
}
/* Radix::System::EventSeverity:Edit - Web Meta*/

/*Radix::System::EventSeverity:Edit-Editor Presentation*/

package org.radixware.ads.System.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRQ4ZHIMKENEFZFXEXN77X3LTRE"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
	null,
	null,

	/*Radix::System::EventSeverity:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::EventSeverity:Edit:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBI7A43DU5NCMBNT67EYMDZ4YTA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFBFIIRQYZ5BQHHB5I36KILK5YE"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDYM6W5ES5BDA5OWHDQFDPNVWBE"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBI7A43DU5NCMBNT67EYMDZ4YTA"))}
	,

	/*Radix::System::EventSeverity:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36018,0,0);
}
/* Radix::System::EventSeverity:Edit:Model - Desktop Executable*/

/*Radix::System::EventSeverity:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Edit:Model")
public class Edit:Model  extends org.radixware.ads.System.explorer.Create:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:Edit:Model:Properties-Properties*/

	/*Radix::System::EventSeverity:Edit:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:Edit:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Edit:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		eventCodeTitle.setCanOpenPropEditorDialog(false);
	}


}

/* Radix::System::EventSeverity:Edit:Model - Desktop Meta*/

/*Radix::System::EventSeverity:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemRQ4ZHIMKENEFZFXEXN77X3LTRE"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemDVK72KVUHVH5PNN3EV7CD2VNEI"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:Edit:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity:Edit:Model - Web Executable*/

/*Radix::System::EventSeverity:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Edit:Model")
public class Edit:Model  extends org.radixware.ads.System.web.Create:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:Edit:Model:Properties-Properties*/

	/*Radix::System::EventSeverity:Edit:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:Edit:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Edit:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		eventCodeTitle.setCanOpenPropEditorDialog(false);
	}


}

/* Radix::System::EventSeverity:Edit:Model - Web Meta*/

/*Radix::System::EventSeverity:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemRQ4ZHIMKENEFZFXEXN77X3LTRE"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemDVK72KVUHVH5PNN3EV7CD2VNEI"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:Edit:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity:General - Desktop Meta*/

/*Radix::System::EventSeverity:General-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprCQ4M24HWBRGGFHDLJTS3VHATJA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
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
		0,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRQ4ZHIMKENEFZFXEXN77X3LTRE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFBFIIRQYZ5BQHHB5I36KILK5YE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.EventSeverity.DefaultGroupModel(userSession,this);
	}
}
/* Radix::System::EventSeverity:General - Web Meta*/

/*Radix::System::EventSeverity:General-Selector Presentation*/

package org.radixware.ads.System.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprCQ4M24HWBRGGFHDLJTS3VHATJA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA3HUFJX7EZENRO2T7IG7XWYZFA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
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
		0,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDVK72KVUHVH5PNN3EV7CD2VNEI")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRQ4ZHIMKENEFZFXEXN77X3LTRE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZG4NUQZTAVAA5NOYJK5LLMFOKQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFBFIIRQYZ5BQHHB5I36KILK5YE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFPLCT7XWVRCY5AC5ZZU4MUNLCQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRG6NMQNHCZHCXBC3R6KAVKGKKA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.EventSeverity.DefaultGroupModel(userSession,this);
	}
}
/* Radix::System::EventSeverity:Source:Model - Desktop Executable*/

/*Radix::System::EventSeverity:Source:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Source:Model")
public class Source:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Source:Model_mi.rdxMeta; }



	public Source:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:Source:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:Source:Model:Properties-Properties*/








	/*Radix::System::EventSeverity:Source:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:Source:pSource:pSource-Presentation Property*/




	public class PSource extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PSource(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventSource> getValClass(){
			return org.radixware.ads.Arte.common.EventSource.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Source:pSource:pSource")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Source:pSource:pSource")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public PSource getPSource(){return (PSource)getProperty(prmSXIY7X7SXVHBPL4IZ7H5CTBXBY);}


}

/* Radix::System::EventSeverity:Source:Model - Desktop Meta*/

/*Radix::System::EventSeverity:Source:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Source:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcPGO4JWJBYVDH3GRWJS6NPVQEEU"),
						"Source:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:Source:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity:Source:Model - Web Executable*/

/*Radix::System::EventSeverity:Source:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Source:Model")
public class Source:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Source:Model_mi.rdxMeta; }



	public Source:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:Source:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:Source:Model:Properties-Properties*/








	/*Radix::System::EventSeverity:Source:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:Source:pSource:pSource-Presentation Property*/




	public class PSource extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PSource(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventSource> getValClass(){
			return org.radixware.ads.Arte.common.EventSource.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Source:pSource:pSource")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Source:pSource:pSource")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public PSource getPSource(){return (PSource)getProperty(prmSXIY7X7SXVHBPL4IZ7H5CTBXBY);}


}

/* Radix::System::EventSeverity:Source:Model - Web Meta*/

/*Radix::System::EventSeverity:Source:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Source:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcPGO4JWJBYVDH3GRWJS6NPVQEEU"),
						"Source:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:Source:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity:Severity:Model - Desktop Executable*/

/*Radix::System::EventSeverity:Severity:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Severity:Model")
public class Severity:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Severity:Model_mi.rdxMeta; }



	public Severity:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:Severity:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:Severity:Model:Properties-Properties*/








	/*Radix::System::EventSeverity:Severity:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:Severity:pSeverity:pSeverity-Presentation Property*/




	public class PSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Severity:pSeverity:pSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Severity:pSeverity:pSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public PSeverity getPSeverity(){return (PSeverity)getProperty(prm6NNOXLLAQ5HJDL3D362WFX7CMQ);}


}

/* Radix::System::EventSeverity:Severity:Model - Desktop Meta*/

/*Radix::System::EventSeverity:Severity:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Severity:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmc3HRUV3UCMFA5LJ4BEN3LA75PKY"),
						"Severity:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:Severity:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity:Severity:Model - Web Executable*/

/*Radix::System::EventSeverity:Severity:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Severity:Model")
public class Severity:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Severity:Model_mi.rdxMeta; }



	public Severity:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:Severity:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:Severity:Model:Properties-Properties*/








	/*Radix::System::EventSeverity:Severity:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:Severity:pSeverity:pSeverity-Presentation Property*/




	public class PSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Severity:pSeverity:pSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:Severity:pSeverity:pSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public PSeverity getPSeverity(){return (PSeverity)getProperty(prm6NNOXLLAQ5HJDL3D362WFX7CMQ);}


}

/* Radix::System::EventSeverity:Severity:Model - Web Meta*/

/*Radix::System::EventSeverity:Severity:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Severity:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmc3HRUV3UCMFA5LJ4BEN3LA75PKY"),
						"Severity:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:Severity:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity:OriginSeverity:Model - Desktop Executable*/

/*Radix::System::EventSeverity:OriginSeverity:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:OriginSeverity:Model")
public class OriginSeverity:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return OriginSeverity:Model_mi.rdxMeta; }



	public OriginSeverity:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:OriginSeverity:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:OriginSeverity:Model:Properties-Properties*/








	/*Radix::System::EventSeverity:OriginSeverity:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:OriginSeverity:pOriginSeverity:pOriginSeverity-Presentation Property*/




	public class POriginSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public POriginSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:OriginSeverity:pOriginSeverity:pOriginSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:OriginSeverity:pOriginSeverity:pOriginSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public POriginSeverity getPOriginSeverity(){return (POriginSeverity)getProperty(prmJDB3C7OK2NA4LKF3RUDUV473PU);}


}

/* Radix::System::EventSeverity:OriginSeverity:Model - Desktop Meta*/

/*Radix::System::EventSeverity:OriginSeverity:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class OriginSeverity:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcJT6TD4Q6UJDCHD6AYNRW36JALI"),
						"OriginSeverity:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:OriginSeverity:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity:OriginSeverity:Model - Web Executable*/

/*Radix::System::EventSeverity:OriginSeverity:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:OriginSeverity:Model")
public class OriginSeverity:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return OriginSeverity:Model_mi.rdxMeta; }



	public OriginSeverity:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventSeverity:OriginSeverity:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverity:OriginSeverity:Model:Properties-Properties*/








	/*Radix::System::EventSeverity:OriginSeverity:Model:Methods-Methods*/

	/*Radix::System::EventSeverity:OriginSeverity:pOriginSeverity:pOriginSeverity-Presentation Property*/




	public class POriginSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public POriginSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:OriginSeverity:pOriginSeverity:pOriginSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverity:OriginSeverity:pOriginSeverity:pOriginSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public POriginSeverity getPOriginSeverity(){return (POriginSeverity)getProperty(prmJDB3C7OK2NA4LKF3RUDUV473PU);}


}

/* Radix::System::EventSeverity:OriginSeverity:Model - Web Meta*/

/*Radix::System::EventSeverity:OriginSeverity:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class OriginSeverity:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcJT6TD4Q6UJDCHD6AYNRW36JALI"),
						"OriginSeverity:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventSeverity:OriginSeverity:Model:Properties-Properties*/
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

/* Radix::System::EventSeverity - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSeverity - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Code");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2GIXD4DMARG2RMIVENCJOFGKUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код события \"{0}\" не найден");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event code \"{0}\" not found");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2STFPF2OJREY5DJEERMP765XJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По источнику");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By source");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AH37G5EINCPVCJLC65AWHTRZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень события");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event severity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7YVL7YNXAZEP7GN4BDDHCTHJ5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень серьезности");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event severity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAFZEBIKUURHIBLGMC5CHYH6TNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Серьёзность");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Severity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAZ225VX4RGRTOXFF7Z5FUI6HI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень серьезности события");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event Severity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHV4MUFBT6FBT5EUIZFQTK4PBK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровни серьезности событий");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event Severities");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIORHPSACLRBM5NKFGXL7VMEAQY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNMMPUFLT3FFRLNIU47HIPFA35Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Code");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFPMWPZY2BCWFGGDKUGW5BKO2E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По уровню серьезности");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By severity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKI6SK7ELZG4BHA5JDQKY3QVJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исходный уровень серьезности");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Original severity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYGYOPYQ7FE6BKOR56FC54ORIM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исходная серьёзность");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Origin severity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV6HCFJIVD5ACVBS3ANJMZME4RM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<источник события>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<event source>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7EPPYDJQJDOHERIUFAZ25XQBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Событие");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event message");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHTX4SNL5VGHXIWMONEEWXG5PY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSZ6ZYG6JZEAJPBDOHWLEYNGPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYCLQHJZP3JHBRJ6CB5VDCDJOWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По исходному уровню серьезности");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By origin severity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYYJAQZGJPFHAPHOM4QLGW3MYQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\System"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EventSeverity - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecA3HUFJX7EZENRO2T7IG7XWYZFA"),"EventSeverity - Localizing Bundle",$$$items$$$);
}

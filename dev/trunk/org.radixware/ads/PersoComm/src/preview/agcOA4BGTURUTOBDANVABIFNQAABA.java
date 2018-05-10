
/* Radix::PersoComm::SentMessageGroup - Server Executable*/

/*Radix::PersoComm::SentMessageGroup-Entity Group Class*/

package org.radixware.ads.PersoComm.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup")
public class SentMessageGroup  extends org.radixware.ads.Types.server.EntityGroup<org.radixware.ads.PersoComm.server.SentMessage>  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return SentMessageGroup_mi.rdxMeta;}

	public SentMessageGroup(boolean isContextWrapper){super(isContextWrapper);}
	/*Radix::PersoComm::SentMessageGroup:Nested classes-Nested Classes*/

	/*Radix::PersoComm::SentMessageGroup:Properties-Properties*/

	/*Radix::PersoComm::SentMessageGroup:needReplace-Entity Group Parameter*/



	protected Bool needReplace=(Bool)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("0",org.radixware.kernel.common.enums.EValType.BOOL);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:needReplace")
	public  Bool getNeedReplace() {
		return needReplace;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:needReplace")
	public   void setNeedReplace(Bool val) {
		needReplace = val;
	}

	/*Radix::PersoComm::SentMessageGroup:newChannel-Entity Group Parameter*/



	protected Int newChannel=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newChannel")
	public  Int getNewChannel() {
		return newChannel;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newChannel")
	public   void setNewChannel(Int val) {
		newChannel = val;
	}

	/*Radix::PersoComm::SentMessageGroup:newDestinationAddress-Entity Group Parameter*/



	protected Str newDestinationAddress=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDestinationAddress")
	public  Str getNewDestinationAddress() {
		return newDestinationAddress;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDestinationAddress")
	public   void setNewDestinationAddress(Str val) {
		newDestinationAddress = val;
	}

	/*Radix::PersoComm::SentMessageGroup:newDueTime-Entity Group Parameter*/



	protected java.sql.Timestamp newDueTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDueTime")
	public  java.sql.Timestamp getNewDueTime() {
		return newDueTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDueTime")
	public   void setNewDueTime(java.sql.Timestamp val) {
		newDueTime = val;
	}

	/*Radix::PersoComm::SentMessageGroup:oldChannel-Entity Group Parameter*/



	protected Str oldChannel=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldChannel")
	public  Str getOldChannel() {
		return oldChannel;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldChannel")
	public   void setOldChannel(Str val) {
		oldChannel = val;
	}

	/*Radix::PersoComm::SentMessageGroup:oldDestinationAddress-Entity Group Parameter*/



	protected Str oldDestinationAddress=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldDestinationAddress")
	public  Str getOldDestinationAddress() {
		return oldDestinationAddress;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldDestinationAddress")
	public   void setOldDestinationAddress(Str val) {
		oldDestinationAddress = val;
	}

	/*Radix::PersoComm::SentMessageGroup:needReplaceStatic-Dynamic Property*/



	protected static Bool needReplaceStatic=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:needReplaceStatic")
	public static  Bool getNeedReplaceStatic() {
		return needReplaceStatic;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:needReplaceStatic")
	public static   void setNeedReplaceStatic(Bool val) {
		needReplaceStatic = val;
	}

	/*Radix::PersoComm::SentMessageGroup:newChannelStatic-Dynamic Property*/



	protected static Int newChannelStatic=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newChannelStatic")
	public static published  Int getNewChannelStatic() {
		return newChannelStatic;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newChannelStatic")
	public static published   void setNewChannelStatic(Int val) {
		newChannelStatic = val;
	}

	/*Radix::PersoComm::SentMessageGroup:newDestinationAddressStatic-Dynamic Property*/



	protected static Str newDestinationAddressStatic=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDestinationAddressStatic")
	public static published  Str getNewDestinationAddressStatic() {
		return newDestinationAddressStatic;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDestinationAddressStatic")
	public static published   void setNewDestinationAddressStatic(Str val) {
		newDestinationAddressStatic = val;
	}

	/*Radix::PersoComm::SentMessageGroup:newDueTimeStatic-Dynamic Property*/



	protected static java.sql.Timestamp newDueTimeStatic=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDueTimeStatic")
	public static published  java.sql.Timestamp getNewDueTimeStatic() {
		return newDueTimeStatic;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDueTimeStatic")
	public static published   void setNewDueTimeStatic(java.sql.Timestamp val) {
		newDueTimeStatic = val;
	}

	/*Radix::PersoComm::SentMessageGroup:oldChannelStatic-Dynamic Property*/



	protected static Str oldChannelStatic=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldChannelStatic")
	public static published  Str getOldChannelStatic() {
		return oldChannelStatic;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldChannelStatic")
	public static published   void setOldChannelStatic(Str val) {
		oldChannelStatic = val;
	}

	/*Radix::PersoComm::SentMessageGroup:oldDestinationAddressStatic-Dynamic Property*/



	protected static Str oldDestinationAddressStatic=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldDestinationAddressStatic")
	public static published  Str getOldDestinationAddressStatic() {
		return oldDestinationAddressStatic;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldDestinationAddressStatic")
	public static published   void setOldDestinationAddressStatic(Str val) {
		oldDestinationAddressStatic = val;
	}





































































































	/*Radix::PersoComm::SentMessageGroup:Methods-Methods*/

	/*Radix::PersoComm::SentMessageGroup:publishParameters-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:publishParameters")
	private final  void publishParameters () {
		needReplaceStatic = this.needReplace;
		newChannelStatic = this.newChannel;
		newDestinationAddressStatic = this.newDestinationAddress;
		newDueTimeStatic = this.newDueTime;
		oldChannelStatic = this.oldChannel;
		oldDestinationAddressStatic = this.oldDestinationAddress;

	}

	/*Radix::PersoComm::SentMessageGroup:getEntityFilter-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:getEntityFilter")
	public published  org.radixware.kernel.server.types.EntityGroup.EntityFilter<org.radixware.ads.PersoComm.server.SentMessage> getEntityFilter () {
		publishParameters();
		return super.getEntityFilter();
	}

	/*Radix::PersoComm::SentMessageGroup:iterator-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:iterator")
	public published  java.util.Iterator<org.radixware.ads.PersoComm.server.SentMessage> iterator () {
		publishParameters();
		return super.iterator();
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.apache.xmlbeans.XmlObject input, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,input,output);
	}


}

/* Radix::PersoComm::SentMessageGroup - Server Meta*/

/*Radix::PersoComm::SentMessageGroup-Entity Group Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SentMessageGroup_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),"SentMessageGroup",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY_GROUP,

						/*Radix::PersoComm::SentMessageGroup:Presentations-Entity Group Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
							/*Owner Class Name*/
							"SentMessageGroup",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::PersoComm::SentMessageGroup:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::PersoComm::SentMessageGroup:needReplace:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpZPVAQNVZ6ZDMNGIE2TONU4DGN4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessageGroup:newChannel:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpBCGHBBXGQ5EHVBZX3WEM7SN2J4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessageGroup:newDestinationAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpCGVHXUT7GBG6DJFMHIYKYJESIA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessageGroup:newDueTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpZZXMOBMPTBBZNI4ADXXRWDLDFU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessageGroup:oldChannel:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpIA2K5ICLERFDDHDMKXOOCSDRQU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessageGroup:oldDestinationAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpADZWSYA755CZHBIHTIOO5GVYRI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::PersoComm::SentMessageGroup:BulkMessageChanges-Group Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJBJMGJVEXNH5XJ2HEWA4A4W5EA"),"BulkMessageChanges",org.radixware.kernel.common.enums.ECommandScope.GROUP,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							null,
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntityGroup_______________"),

						/*Radix::PersoComm::SentMessageGroup:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::SentMessageGroup:needReplace-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpZPVAQNVZ6ZDMNGIE2TONU4DGN4"),"needReplace",null,org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:newChannel-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpBCGHBBXGQ5EHVBZX3WEM7SN2J4"),"newChannel",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:newDestinationAddress-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpCGVHXUT7GBG6DJFMHIYKYJESIA"),"newDestinationAddress",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:newDueTime-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpZZXMOBMPTBBZNI4ADXXRWDLDFU"),"newDueTime",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:oldChannel-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpIA2K5ICLERFDDHDMKXOOCSDRQU"),"oldChannel",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:oldDestinationAddress-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpADZWSYA755CZHBIHTIOO5GVYRI"),"oldDestinationAddress",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:needReplaceStatic-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFOKQC6ALQVHO5PXW2EMYP6ABFU"),"needReplaceStatic",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:newChannelStatic-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK33NGWJWH5FDZIMT2UVZG3BJKA"),"newChannelStatic",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4XEHQQPS2BCNDNKAQVXTZ63HNQ"),org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:newDestinationAddressStatic-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLZVMJNTHPFEE5PYAB7FEGBMXBM"),"newDestinationAddressStatic",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR7YDGJ452RFIVDPPZ34V3HVW6U"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:newDueTimeStatic-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5YVGMT3WHVGD3CTDL5WEK4NUHU"),"newDueTimeStatic",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQFXDJUKKHVBAPCUVEALXNUQWPA"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:oldChannelStatic-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR6IWZWJWBVBFBCOSIYB273OXJ4"),"oldChannelStatic",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4Z2ZKZ2YZVDFJIS6SEENR3P3SM"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessageGroup:oldDestinationAddressStatic-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOTRF3LD3LVDANP2EGQPDMCCD4M"),"oldDestinationAddressStatic",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsASAV3MNI2BH5VJO2OTN3FO2W64"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::SentMessageGroup:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCSNR5JFUUFBZPICQE3IFKAJBPY"),"publishParameters",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN2WJ3HALEFAHNLEC33L7YMY5VI"),"getEntityFilter",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH7N6UFUIO5E6XBVWQXTNWXKLOE"),"iterator",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::PersoComm::SentMessageGroup - Desktop Executable*/

/*Radix::PersoComm::SentMessageGroup-Entity Group Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup")
public class SentMessageGroup {



	public static org.radixware.kernel.common.client.models.items.Command createCommand(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){
		@SuppressWarnings("unused")
		org.radixware.kernel.common.types.Id commandId = def.getId();
		if(cmdJBJMGJVEXNH5XJ2HEWA4A4W5EA == commandId) return new SentMessageGroup.BulkMessageChanges(model,def);
		else return null;
	}

	public static class BulkMessageChanges extends org.radixware.kernel.common.client.models.items.Command{
		protected BulkMessageChanges(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::PersoComm::SentMessageGroup - Desktop Meta*/

/*Radix::PersoComm::SentMessageGroup-Entity Group Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SentMessageGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadGroupHandlerDef rdxMeta = 
	/*Radix::PersoComm::SentMessageGroup:Presentations-Entity Group Presentations*/
	new org.radixware.kernel.common.client.meta.RadGroupHandlerDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::SentMessageGroup:BulkMessageChanges-Group Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJBJMGJVEXNH5XJ2HEWA4A4W5EA"),
						"BulkMessageChanges",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFWMXY6ZRFDJLNFS7JWCTYZ2EA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgUHTOGOZAWMJOJ2DCGZTFQEE4DRDRZTTU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhZSJZQT4E4RDFZPDOPTB4UPJHLU"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.GROUP,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},

			/*Radix::PersoComm::SentMessageGroup:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::SentMessageGroup:needReplace:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpZPVAQNVZ6ZDMNGIE2TONU4DGN4"),
						"needReplace",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessageGroup:needReplace:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessageGroup:newChannel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpBCGHBBXGQ5EHVBZX3WEM7SN2J4"),
						"newChannel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::PersoComm::SentMessageGroup:newChannel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessageGroup:newDestinationAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpCGVHXUT7GBG6DJFMHIYKYJESIA"),
						"newDestinationAddress",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::PersoComm::SentMessageGroup:newDestinationAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessageGroup:newDueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpZZXMOBMPTBBZNI4ADXXRWDLDFU"),
						"newDueTime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::PersoComm::SentMessageGroup:newDueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessageGroup:oldChannel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpIA2K5ICLERFDDHDMKXOOCSDRQU"),
						"oldChannel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::PersoComm::SentMessageGroup:oldChannel:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessageGroup:oldDestinationAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpADZWSYA755CZHBIHTIOO5GVYRI"),
						"oldDestinationAddress",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::PersoComm::SentMessageGroup:oldDestinationAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			});
	}

	/* Radix::PersoComm::SentMessageGroup - Web Executable*/

	/*Radix::PersoComm::SentMessageGroup-Entity Group Class*/

	package org.radixware.ads.PersoComm.web;

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup")
	public class SentMessageGroup {



		public static org.radixware.kernel.common.client.models.items.Command createCommand(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){
			@SuppressWarnings("unused")
			org.radixware.kernel.common.types.Id commandId = def.getId();
			if(cmdJBJMGJVEXNH5XJ2HEWA4A4W5EA == commandId) return new SentMessageGroup.BulkMessageChanges(model,def);
			else return null;
		}

		public static class BulkMessageChanges extends org.radixware.kernel.common.client.models.items.Command{
			protected BulkMessageChanges(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

		}



	}

	/* Radix::PersoComm::SentMessageGroup - Web Meta*/

	/*Radix::PersoComm::SentMessageGroup-Entity Group Class*/

	package org.radixware.ads.PersoComm.web;
	@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
	public final class SentMessageGroup_mi{
		public static final org.radixware.kernel.common.client.meta.RadGroupHandlerDef rdxMeta = 
		/*Radix::PersoComm::SentMessageGroup:Presentations-Entity Group Presentations*/
		new org.radixware.kernel.common.client.meta.RadGroupHandlerDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
				org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
				new org.radixware.kernel.common.client.meta.RadCommandDef[]{
						new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
						/*Radix::PersoComm::SentMessageGroup:BulkMessageChanges-Group Command*/

							org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJBJMGJVEXNH5XJ2HEWA4A4W5EA"),
							"BulkMessageChanges",
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFWMXY6ZRFDJLNFS7JWCTYZ2EA"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("imgUHTOGOZAWMJOJ2DCGZTFQEE4DRDRZTTU"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("afhZSJZQT4E4RDFZPDOPTB4UPJHLU"),
							org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
							true,
							false,
							false,
							org.radixware.kernel.common.enums.ECommandScope.GROUP,
							null,
							org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
				},

				/*Radix::PersoComm::SentMessageGroup:Properties-Properties*/
				new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

						/*Radix::PersoComm::SentMessageGroup:needReplace:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpZPVAQNVZ6ZDMNGIE2TONU4DGN4"),
							"needReplace",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
							org.radixware.kernel.common.enums.EValType.BOOL,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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
							false,false,
							false,
							false,
							null,
							false,

							/*Radix::PersoComm::SentMessageGroup:needReplace:PropertyPresentation:Edit Options:-Edit Mask Bool*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

						/*Radix::PersoComm::SentMessageGroup:newChannel:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpBCGHBBXGQ5EHVBZX3WEM7SN2J4"),
							"newChannel",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
							org.radixware.kernel.common.enums.EValType.INT,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::PersoComm::SentMessageGroup:newChannel:PropertyPresentation:Edit Options:-Edit Mask Int*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

						/*Radix::PersoComm::SentMessageGroup:newDestinationAddress:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpCGVHXUT7GBG6DJFMHIYKYJESIA"),
							"newDestinationAddress",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
							org.radixware.kernel.common.enums.EValType.STR,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::PersoComm::SentMessageGroup:newDestinationAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

						/*Radix::PersoComm::SentMessageGroup:newDueTime:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpZZXMOBMPTBBZNI4ADXXRWDLDFU"),
							"newDueTime",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
							org.radixware.kernel.common.enums.EValType.DATE_TIME,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::PersoComm::SentMessageGroup:newDueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

						/*Radix::PersoComm::SentMessageGroup:oldChannel:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpIA2K5ICLERFDDHDMKXOOCSDRQU"),
							"oldChannel",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
							org.radixware.kernel.common.enums.EValType.STR,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::PersoComm::SentMessageGroup:oldChannel:PropertyPresentation:Edit Options:-Edit Mask Str*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

						/*Radix::PersoComm::SentMessageGroup:oldDestinationAddress:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpADZWSYA755CZHBIHTIOO5GVYRI"),
							"oldDestinationAddress",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcOA4BGTURUTOBDANVABIFNQAABA"),
							org.radixware.kernel.common.enums.EValType.STR,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::PersoComm::SentMessageGroup:oldDestinationAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
				});
		}

		/* Radix::PersoComm::SentMessageGroup - Localizing Bundle */
		package org.radixware.ads.PersoComm.common;
		@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
		public final class SentMessageGroup - Localizing Bundle_mi{
			@SuppressWarnings("unused")
			private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
			static{
				loadStrings1();
			}

			@SuppressWarnings("unused")
			private static void loadStrings1(){
				java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New channel number");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый номер канала");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4XEHQQPS2BCNDNKAQVXTZ63HNQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Old channel number");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прежний номер канала");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4YRBRRBJPRDYRDWFGBO43RGRAA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Оld channel number");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прежний номер канала");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4Z2ZKZ2YZVDFJIS6SEENR3P3SM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Old destination address");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прежний адрес приемника");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5NGSZBSSBRAIBO4K63NOMAXPQI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Old destination address");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прежний адрес приемника");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsASAV3MNI2BH5VJO2OTN3FO2W64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Edit All Undelivered Messages");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Редактировать все недоставленные сообщения");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFWMXY6ZRFDJLNFS7JWCTYZ2EA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Replaces the message parameters in bulk");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Команда для массовой замены параметров сообщения");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFF7WJQ4YBAXFFVWXA4Y3ULL3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New channel number");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый номер канала");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJN5BSPA5VD2NB7F75W72ZZDYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New destination address");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый адрес приемника");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJM2VUNML4FEF7M7NPRWGLE4WVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New Due Time");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый срок передачи");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQFXDJUKKHVBAPCUVEALXNUQWPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New destination address");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый адрес приемника");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR7YDGJ452RFIVDPPZ34V3HVW6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New due time");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый срок передачи");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYWMZAYXH5ZH4XPELMU2TCKJSEU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
			}

			public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(SentMessageGroup - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbagcOA4BGTURUTOBDANVABIFNQAABA"),"SentMessageGroup - Localizing Bundle",$$$items$$$);
		}

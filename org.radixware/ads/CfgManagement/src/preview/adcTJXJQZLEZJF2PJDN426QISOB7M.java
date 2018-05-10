
/* Radix::CfgManagement::CfgSettingsProviderForActualize - Server Executable*/

/*Radix::CfgManagement::CfgSettingsProviderForActualize-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize")
 class CfgSettingsProviderForActualize  implements org.radixware.ads.CfgManagement.server.ICfgSettingsProvider,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgSettingsProviderForActualize_mi.rdxMeta;}

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:Properties-Properties*/

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:itemId2ActualizeFlag-Dynamic Property*/



	protected java.util.Map<Int,Bool> itemId2ActualizeFlag=new java.util.HashMap<Int, Bool>();;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:itemId2ActualizeFlag")
	private final  java.util.Map<Int,Bool> getItemId2ActualizeFlag() {
		return itemId2ActualizeFlag;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:itemId2ActualizeFlag")
	private final   void setItemId2ActualizeFlag(java.util.Map<Int,Bool> val) {
		itemId2ActualizeFlag = val;
	}

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:isItemCreationAllowed-Dynamic Property*/



	protected boolean isItemCreationAllowed=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:isItemCreationAllowed")
	private final  boolean getIsItemCreationAllowed() {
		return isItemCreationAllowed;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:isItemCreationAllowed")
	private final   void setIsItemCreationAllowed(boolean val) {
		isItemCreationAllowed = val;
	}

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:curItem-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.CfgItem curItem=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:curItem")
	  org.radixware.ads.CfgManagement.server.CfgItem getCurItem() {
		return curItem;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:curItem")
	   void setCurItem(org.radixware.ads.CfgManagement.server.CfgItem val) {
		curItem = val;
	}















































	/*Radix::CfgManagement::CfgSettingsProviderForActualize:Methods-Methods*/

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:setItemActualizeStatus-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:setItemActualizeStatus")
	public  void setItemActualizeStatus (Int itemId, boolean wasActualized) {
		itemId2ActualizeFlag.put(itemId, wasActualized);
	}

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:wasItemActualized-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:wasItemActualized")
	public  boolean wasItemActualized (Int itemId) {
		final Bool wasActualized = itemId2ActualizeFlag.get(itemId);
		return wasActualized != null ? wasActualized.booleanValue() : false;
	}

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:isItemsCreationAllowed-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:isItemsCreationAllowed")
	public  boolean isItemsCreationAllowed () {
		return isItemCreationAllowed;
	}

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:getSettings-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:getSettings")
	public published  org.radixware.ads.CfgManagement.common.ImpExpXsd.SettingsDocument getSettings (org.radixware.ads.CfgManagement.server.CfgItem item) {
		return null;
	}

	/*Radix::CfgManagement::CfgSettingsProviderForActualize:CfgSettingsProviderForActualize-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgSettingsProviderForActualize:CfgSettingsProviderForActualize")
	public  CfgSettingsProviderForActualize (boolean isItemsCreationAllowed) {
		isItemCreationAllowed = isItemsCreationAllowed;
	}


}

/* Radix::CfgManagement::CfgSettingsProviderForActualize - Server Meta*/

/*Radix::CfgManagement::CfgSettingsProviderForActualize-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgSettingsProviderForActualize_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcTJXJQZLEZJF2PJDN426QISOB7M"),"CfgSettingsProviderForActualize",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::CfgManagement::CfgSettingsProviderForActualize:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgSettingsProviderForActualize:itemId2ActualizeFlag-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWF2KM4KK4NFOFARP6RJYRC3VCM"),"itemId2ActualizeFlag",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgSettingsProviderForActualize:isItemCreationAllowed-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRDJTXIQIZFGBXNF5CDJKE4C7NQ"),"isItemCreationAllowed",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgSettingsProviderForActualize:curItem-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNQ7AZ6B7I5DEDKRHP6OYTGTKMU"),"curItem",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::CfgSettingsProviderForActualize:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5KRKFDOL6ZB6VB6YWT2646AOHU"),"setItemActualizeStatus",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("itemId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD7EU4BIRRFHSDOT7MLAEDH6ABY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("wasActualized",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGE6R4OFLPJFZ3BWKXP5QLBSHAM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth57SFNF4UCZAGXGVUKX7W7WZMQI"),"wasItemActualized",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("itemId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5Y2B42KTNJDZ3B5P4SO5DSOCQA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2WHGE4XCPRCLXANJWTPI5BD6ZI"),"isItemsCreationAllowed",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKJOZGPZTNJEZTMSBPUKWZWUGFM"),"getSettings",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("item",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5S6D44DHFFEXTOQWRVCDF3PW6M"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOWVKKF243FHBNEPQDFWXJLKC34"),"CfgSettingsProviderForActualize",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isItemsCreationAllowed",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTSMBMOWLNJCG5K47ELOV5RLVVE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

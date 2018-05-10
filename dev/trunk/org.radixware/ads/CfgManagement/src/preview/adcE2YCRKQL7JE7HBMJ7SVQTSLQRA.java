
/* Radix::CfgManagement::CfgExportData - Server Executable*/

/*Radix::CfgManagement::CfgExportData-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData")
public final published class CfgExportData  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgExportData_mi.rdxMeta;}

	/*Radix::CfgManagement::CfgExportData:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgExportData:Properties-Properties*/

	/*Radix::CfgManagement::CfgExportData:object-Dynamic Property*/



	protected org.radixware.ads.Types.server.Entity object=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:object")
	public published  org.radixware.ads.Types.server.Entity getObject() {
		return object;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:object")
	public published   void setObject(org.radixware.ads.Types.server.Entity val) {
		object = val;
	}

	/*Radix::CfgManagement::CfgExportData:itemClassId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id itemClassId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:itemClassId")
	public published  org.radixware.kernel.common.types.Id getItemClassId() {
		return itemClassId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:itemClassId")
	public published   void setItemClassId(org.radixware.kernel.common.types.Id val) {
		itemClassId = val;
	}

	/*Radix::CfgManagement::CfgExportData:data-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject data=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:data")
	public published  org.apache.xmlbeans.XmlObject getData() {
		return data;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:data")
	public published   void setData(org.apache.xmlbeans.XmlObject val) {
		data = val;
	}

	/*Radix::CfgManagement::CfgExportData:fileContent-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject fileContent=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:fileContent")
	public published  org.apache.xmlbeans.XmlObject getFileContent() {
		return fileContent;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:fileContent")
	public published   void setFileContent(org.apache.xmlbeans.XmlObject val) {
		fileContent = val;
	}

	/*Radix::CfgManagement::CfgExportData:children-Dynamic Property*/



	protected java.util.List<org.radixware.ads.CfgManagement.server.CfgExportData> children=new java.util.ArrayList<CfgExportData>();











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:children")
	public published  java.util.List<org.radixware.ads.CfgManagement.server.CfgExportData> getChildren() {
		return children;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:children")
	public published   void setChildren(java.util.List<org.radixware.ads.CfgManagement.server.CfgExportData> val) {
		children = val;
	}

	/*Radix::CfgManagement::CfgExportData:objectRid-Dynamic Property*/



	protected Str objectRid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:objectRid")
	public published  Str getObjectRid() {
		return objectRid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:objectRid")
	public published   void setObjectRid(Str val) {
		objectRid = val;
	}

































































	/*Radix::CfgManagement::CfgExportData:Methods-Methods*/

	/*Radix::CfgManagement::CfgExportData:CfgExportData-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:CfgExportData")
	public published  CfgExportData () {

	}

	/*Radix::CfgManagement::CfgExportData:CfgExportData-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:CfgExportData")
	public published  CfgExportData (org.radixware.ads.Types.server.Entity object) {
		object = object;


	}

	/*Radix::CfgManagement::CfgExportData:CfgExportData-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:CfgExportData")
	public published  CfgExportData (org.radixware.ads.Types.server.Entity object, Str rid, org.radixware.kernel.common.types.Id itemClassId, org.apache.xmlbeans.XmlObject data) {
		object = object;
		objectRid = rid;
		itemClassId = itemClassId;
		data = data;

	}

	/*Radix::CfgManagement::CfgExportData:getExtGuid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgExportData:getExtGuid")
	public published  Str getExtGuid () {
		return ImpExpUtils.getExtGuid(object);
	}


}

/* Radix::CfgManagement::CfgExportData - Server Meta*/

/*Radix::CfgManagement::CfgExportData-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgExportData_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcE2YCRKQL7JE7HBMJ7SVQTSLQRA"),"CfgExportData",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::CfgManagement::CfgExportData:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgExportData:object-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLVEOXBUEYBDEFJLZDCYUZFOZSY"),"object",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgExportData:itemClassId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJLPCPCN4ENBYFP3VVXHCMETKQI"),"itemClassId",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgExportData:data-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTHDSUUUK6RCDLJB5GNXMX7F4PM"),"data",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgExportData:fileContent-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHTE6WGDJ35FFJGQAKSHOWDQWXY"),"fileContent",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgExportData:children-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCTAMF5IVBVDFLACES33SCZPA74"),"children",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgExportData:objectRid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEWO7AKREINBOPGFPBU3KW6LM3Y"),"objectRid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::CfgExportData:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTUPNAPASTJFQLIL3RVUGKTV5AM"),"CfgExportData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXKSVXXJ2YRDFLGEW4YXTUZZYLQ"),"CfgExportData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG3WWVE4MFZCSNJSN2Y3DUIFHIU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6SKCDX3ZZRGNVL4M3ECZI2M54E"),"CfgExportData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("object",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG3WWVE4MFZCSNJSN2Y3DUIFHIU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKJXPCDZCTFHQVDZ6TEHMCSNUQ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("itemClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJZNCQ24POFBOPIG5UOTM7C5C6E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGFKROREKYNACNIOYO35IVRTCMA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ7W5QLY3PBHHTK3WHD7GPLXKPE"),"getExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}


/* Radix::UserFunc::UserFuncAdapter - Server Executable*/

/*Radix::UserFunc::UserFuncAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.UserFunc.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncAdapter")
public class UserFuncAdapter  extends org.radixware.kernel.server.types.PresentationEntityAdapter<org.radixware.ads.UserFunc.server.UserFunc>  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return UserFuncAdapter_mi.rdxMeta;}

	public UserFuncAdapter(org.radixware.kernel.server.types.Entity e){super(e);}

	/*Radix::UserFunc::UserFuncAdapter:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFuncAdapter:Properties-Properties*/





























	/*Radix::UserFunc::UserFuncAdapter:Methods-Methods*/

	/*Radix::UserFunc::UserFuncAdapter:isCommandDisabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncAdapter:isCommandDisabled")
	public published  boolean isCommandDisabled (org.radixware.kernel.common.types.Id cmdId) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException {
		if(cmdId==idof[UserFunc:MoveToLibrary]){
		    return idof[System::LibUserFunc].toString() == this.Entity.upOwnerEntityId;
		}

		return super.isCommandDisabled(cmdId);
	}

	/*Radix::UserFunc::UserFuncAdapter:calcSelectorRowStyle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncAdapter:calcSelectorRowStyle")
	public published  org.radixware.kernel.common.enums.ESelectorRowStyle calcSelectorRowStyle () {
		UserFunc e = getEntity();
		if (e.isValid != null && e.isValid.booleanValue()) {
		    return super.calcSelectorRowStyle();
		} else {
		    return Explorer.Env::SelectorStyle:VeryBad;
		}


	}

	/*Radix::UserFunc::UserFuncAdapter:setProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncAdapter:setProp")
	public published  void setProp (org.radixware.kernel.common.types.Id id, java.lang.Object val) {
		if (idof[UserFunc:isOwnerWasNotCreated]==id && val==Bool.TRUE){
		    final Str ownerPid = getContextOwnerPid();
		    if (ownerPid!=null){
		        getEntity().upOwnerPid = ownerPid;
		    }
		}
		if (idof[UserFunc:upOwnerPid]==id){
		    //RADIX-13426 Do not allow to modify upOwnerPid from client side
		    return;
		}
		super.setProp(id, val);
	}

	/*Radix::UserFunc::UserFuncAdapter:getContextOwnerPid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncAdapter:getContextOwnerPid")
	private final  Str getContextOwnerPid () {
		if (getPresentationContext() instanceof Types.Context::EntityPropertyPresentationContext){
		    final Types.Context::EntityPropertyPresentationContext context = (Types.Context::EntityPropertyPresentationContext)getPresentationContext();        
		    if (context.getChildEntity().getRadMeta().getPropById(context.getPropertyId()).getValType()==Meta::ValType:Object){
		        return context.getChildEntity().getPid().toString();
		    }
		}
		return null;
	}

	/*Radix::UserFunc::UserFuncAdapter:doUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncAdapter:doUpdate")
	protected published  void doUpdate () {
		getEntity().checkUserRights(true);

		super.doUpdate();
	}

	/*Radix::UserFunc::UserFuncAdapter:doCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncAdapter:doCreate")
	protected published  org.radixware.kernel.server.types.Entity doCreate (org.radixware.kernel.server.types.Entity src) {
		getEntity().checkUserRights(false);

		return super.doCreate(src);
	}

	/*Radix::UserFunc::UserFuncAdapter:createAsUserPropertyValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFuncAdapter:createAsUserPropertyValue")
	public published  void createAsUserPropertyValue (org.radixware.kernel.server.types.PresentationEntityAdapter ownerEntityAdapter, org.radixware.kernel.common.types.Id propertyId) {
		getEntity().checkUserRights(false);

		super.createAsUserPropertyValue(ownerEntityAdapter, propertyId);
	}


}

/* Radix::UserFunc::UserFuncAdapter - Server Meta*/

/*Radix::UserFunc::UserFuncAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.UserFunc.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFuncAdapter_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("apaJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"UserFuncAdapter",null,

						org.radixware.kernel.common.enums.EClassType.PRESENTATION_ENTITY_ADAPTER,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,

						/*Radix::UserFunc::UserFuncAdapter:Properties-Properties*/
						null,

						/*Radix::UserFunc::UserFuncAdapter:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ4QY527H2FFCVCSSDEH6EXQJAA"),"isCommandDisabled",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cmdId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYS7MCGZQK5AH5C7Z57GXTHQ2TI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM23NRT7ZAJGWFDZSVWALEMS2QI"),"calcSelectorRowStyle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth733NK3HNF5B3DB4PEGRDMWJYZ4"),"setProp",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTKCT4MW73FDC7J23TR3KOV4T3E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("val",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDZM65GEMSFFZXJC2MEL3VJH74Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPVKLRJ66G5FJVNVQO4MUJK3Q5M"),"getContextOwnerPid",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEDZCPWDTMJE7TEFPR5PLXXEDQA"),"doUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY72O3IGBUZES7N4LG6CMZSH5NI"),"doCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7BU6R4AVJRBFLONFLVXCWAMQSU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ223PZLDQFFCVJQZR7HIJK3ICI"),"createAsUserPropertyValue",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ownerEntityAdapter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMM5PZIZMTVEYBJ4ALM2K6PJA4Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propertyId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWXA5PR3VH5CZBB5573FBMQSASA"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::UserFunc::UserFuncAdapter - Localizing Bundle */
package org.radixware.ads.UserFunc.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFuncAdapter - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(UserFuncAdapter - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbapaJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"UserFuncAdapter - Localizing Bundle",$$$items$$$);
}


/* Radix::System::SystemAdapter - Server Executable*/

/*Radix::System::SystemAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.System.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::SystemAdapter")
 class SystemAdapter  extends org.radixware.kernel.server.types.PresentationEntityAdapter<org.radixware.ads.System.server.System>  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return SystemAdapter_mi.rdxMeta;}

	public SystemAdapter(org.radixware.kernel.server.types.Entity e){super(e);}

	/*Radix::System::SystemAdapter:Nested classes-Nested Classes*/

	/*Radix::System::SystemAdapter:Properties-Properties*/





























	/*Radix::System::SystemAdapter:Methods-Methods*/

	/*Radix::System::SystemAdapter:isCommandDisabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::SystemAdapter:isCommandDisabled")
	public published  boolean isCommandDisabled (org.radixware.kernel.common.types.Id cmdId) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException {
		final boolean isThisSys = (getEntity().id == 1);
		if (cmdId == idof[System:ClearTracedSensitiveData]) {
		    return !isThisSys; 
		} else if (cmdId == idof[System:ExportManifest]) {
		    return !isThisSys; 
		}else if (cmdId == idof[System:ImportManifest]) {
		    return isThisSys; 
		}
		return super.isCommandDisabled(cmdId);
	}

	/*Radix::System::SystemAdapter:doUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::SystemAdapter:doUpdate")
	protected published  void doUpdate () {
		getEntity().checkValues();

		super.doUpdate();
	}


}

/* Radix::System::SystemAdapter - Server Meta*/

/*Radix::System::SystemAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SystemAdapter_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("apaX5TD7JDVVHWDBROXAAIT4AGD7E"),"SystemAdapter",null,

						org.radixware.kernel.common.enums.EClassType.PRESENTATION_ENTITY_ADAPTER,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),
						null,

						/*Radix::System::SystemAdapter:Properties-Properties*/
						null,

						/*Radix::System::SystemAdapter:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ4QY527H2FFCVCSSDEH6EXQJAA"),"isCommandDisabled",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cmdId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQJA5AAV36BBPRDVN3AJXCG7DDE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEDZCPWDTMJE7TEFPR5PLXXEDQA"),"doUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null)
						},
						null,
						null,null,null,false);
}

/* Radix::System::SystemAdapter - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SystemAdapter - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(SystemAdapter - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbapaX5TD7JDVVHWDBROXAAIT4AGD7E"),"SystemAdapter - Localizing Bundle",$$$items$$$);
}

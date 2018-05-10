
/* Radix::CfgManagement::CfgPacketAdapter - Server Executable*/

/*Radix::CfgManagement::CfgPacketAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacketAdapter")
public class CfgPacketAdapter  extends org.radixware.kernel.server.types.PresentationEntityAdapter<org.radixware.ads.CfgManagement.server.CfgPacket>  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgPacketAdapter_mi.rdxMeta;}

	public CfgPacketAdapter(org.radixware.kernel.server.types.Entity e){super(e);}

	/*Radix::CfgManagement::CfgPacketAdapter:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgPacketAdapter:Properties-Properties*/





























	/*Radix::CfgManagement::CfgPacketAdapter:Methods-Methods*/

	/*Radix::CfgManagement::CfgPacketAdapter:setProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacketAdapter:setProp",line=58)
	public published  void setProp (org.radixware.kernel.common.types.Id id, java.lang.Object val) {
		if (id == idof[CfgPacket:pkgState]) {
		    
		}

		super.setProp(id, val);
	}


}

/* Radix::CfgManagement::CfgPacketAdapter - Server Meta*/

/*Radix::CfgManagement::CfgPacketAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacketAdapter_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("apaWMN3P5J5ONCFPLJVGBFAKZXZXU"),"CfgPacketAdapter",null,

						org.radixware.kernel.common.enums.EClassType.PRESENTATION_ENTITY_ADAPTER,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),
						null,

						/*Radix::CfgManagement::CfgPacketAdapter:Properties-Properties*/
						null,

						/*Radix::CfgManagement::CfgPacketAdapter:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth733NK3HNF5B3DB4PEGRDMWJYZ4"),"setProp",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFUK2DM5RDNFB5GQLZ53PXECE6Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("val",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD4JDPPVYZBEA5EXDQ37YYBFIEY"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::CfgManagement::CfgPacketAdapter - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacketAdapter - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgPacketAdapter - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbapaWMN3P5J5ONCFPLJVGBFAKZXZXU"),"CfgPacketAdapter - Localizing Bundle",$$$items$$$);
}

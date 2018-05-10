
/* Radix::System::EventSeverityAdapter - Server Executable*/

/*Radix::System::EventSeverityAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.System.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverityAdapter")
public class EventSeverityAdapter  extends org.radixware.kernel.server.types.PresentationEntityAdapter<org.radixware.ads.System.server.EventSeverity>  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EventSeverityAdapter_mi.rdxMeta;}

	public EventSeverityAdapter(org.radixware.kernel.server.types.Entity e){super(e);}

	/*Radix::System::EventSeverityAdapter:Nested classes-Nested Classes*/

	/*Radix::System::EventSeverityAdapter:Properties-Properties*/





























	/*Radix::System::EventSeverityAdapter:Methods-Methods*/

	/*Radix::System::EventSeverityAdapter:calcSelectorRowStyle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventSeverityAdapter:calcSelectorRowStyle")
	public published  org.radixware.kernel.common.enums.ESelectorRowStyle calcSelectorRowStyle () {
		if (getEntity().eventParams == null) {
		    return Explorer.Env::SelectorStyle:Bad;
		} else {
		    return super.calcSelectorRowStyle();
		}
	}


}

/* Radix::System::EventSeverityAdapter - Server Meta*/

/*Radix::System::EventSeverityAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSeverityAdapter_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("apaA3HUFJX7EZENRO2T7IG7XWYZFA"),"EventSeverityAdapter",null,

						org.radixware.kernel.common.enums.EClassType.PRESENTATION_ENTITY_ADAPTER,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblA3HUFJX7EZENRO2T7IG7XWYZFA"),
						null,

						/*Radix::System::EventSeverityAdapter:Properties-Properties*/
						null,

						/*Radix::System::EventSeverityAdapter:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM23NRT7ZAJGWFDZSVWALEMS2QI"),"calcSelectorRowStyle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::System::EventSeverityAdapter - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSeverityAdapter - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EventSeverityAdapter - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbapaA3HUFJX7EZENRO2T7IG7XWYZFA"),"EventSeverityAdapter - Localizing Bundle",$$$items$$$);
}

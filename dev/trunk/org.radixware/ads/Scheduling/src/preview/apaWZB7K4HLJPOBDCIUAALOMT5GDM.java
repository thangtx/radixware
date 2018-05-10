
/* Radix::Scheduling::TaskAdapter - Server Executable*/

/*Radix::Scheduling::TaskAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskAdapter")
public class TaskAdapter  extends org.radixware.kernel.server.types.PresentationEntityAdapter<org.radixware.ads.Scheduling.server.Task>  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return TaskAdapter_mi.rdxMeta;}

	public TaskAdapter(org.radixware.kernel.server.types.Entity e){super(e);}

	/*Radix::Scheduling::TaskAdapter:Nested classes-Nested Classes*/

	/*Radix::Scheduling::TaskAdapter:Properties-Properties*/





























	/*Radix::Scheduling::TaskAdapter:Methods-Methods*/

	/*Radix::Scheduling::TaskAdapter:doInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskAdapter:doInit")
	protected published  void doInit (org.radixware.kernel.server.types.PropValHandlersByIdMap initialPropValsById, org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		if (src != null && this.getPresentationContext() != null && this.getPresentationContext() instanceof Types.Context::RootExplorerItemPresentationContext) {
		    if (this.getEntity() != null) {
		        this.getEntity().unitId = null;
		    }
		}

		super.doInit(initialPropValsById, src, phase);
	}

	/*Radix::Scheduling::TaskAdapter:getProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskAdapter:getProp")
	public published  java.lang.Object getProp (org.radixware.kernel.common.types.Id id) {
		if (id == idof[Task:hasChildren]) {
		    final org.radixware.kernel.server.types.EntityGroup group = this.PresentationContext.getEntityGroup();
		    if (group != null && group.getProp(idof[TaskGroup:taskToMoveId]) != null) {
		        return super.getProp(idof[Task:hasChildrenOnlyDirs]);
		    }
		}

		return super.getProp(id);
	}


}

/* Radix::Scheduling::TaskAdapter - Server Meta*/

/*Radix::Scheduling::TaskAdapter-Presentation Entity Adapter Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TaskAdapter_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("apaWZB7K4HLJPOBDCIUAALOMT5GDM"),"TaskAdapter",null,

						org.radixware.kernel.common.enums.EClassType.PRESENTATION_ENTITY_ADAPTER,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						null,

						/*Radix::Scheduling::TaskAdapter:Properties-Properties*/
						null,

						/*Radix::Scheduling::TaskAdapter:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDVPR662MUFCB7O6X425DBXV2E4"),"doInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("initialPropValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBTGNRIJBLFCOPB4SUNLMNN4NOE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZNDBTR44ZDLFH6CQJTW4GB26M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTP22J3LGTZDKXB4562LS7SHWK4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAGX4ROZ7ORGMXOXCORRZJUIWOY"),"getProp",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCPDK3RVBUFG6LDSO3TS22KYGAQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::TaskAdapter - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TaskAdapter - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(TaskAdapter - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbapaWZB7K4HLJPOBDCIUAALOMT5GDM"),"TaskAdapter - Localizing Bundle",$$$items$$$);
}


/* Radix::System::AADCLocksDialogUtils - Server Executable*/

/*Radix::System::AADCLocksDialogUtils-Server Dynamic Class*/

package org.radixware.ads.System.server;

import org.radixware.kernel.server.instance.aadc.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::AADCLocksDialogUtils")
public class AADCLocksDialogUtils  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return AADCLocksDialogUtils_mi.rdxMeta;}

	/*Radix::System::AADCLocksDialogUtils:Nested classes-Nested Classes*/

	/*Radix::System::AADCLocksDialogUtils:Properties-Properties*/





























	/*Radix::System::AADCLocksDialogUtils:Methods-Methods*/

	/*Radix::System::AADCLocksDialogUtils:getTestAADCLockCacheKeyList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::AADCLocksDialogUtils:getTestAADCLockCacheKeyList")
	public static  java.util.List<org.radixware.kernel.server.instance.aadc.LockCacheKey> getTestAADCLockCacheKeyList () {
		java.util.List<LockCacheKey> result = new java.util.ArrayList<LockCacheKey>();

		for (int i = 1; i <= /*20000*/1000; i++) {
		    Types::Pid pid = new Pid(Arte::Arte.getInstance(), idof[Scheduling::Task], idof[Scheduling::Task:id], i);
		    byte lockType = (byte) (i % 10);
		    
		    LockCacheKey key = new LockCacheKey(pid, lockType);
		    result.add(key);
		}

		for (int i = 1; i <= /*8000*/1000; i++) {
		    Types::Pid pid = new Pid(Arte::Arte.getInstance(), idof[System::Instance], idof[Instance:id], i);
		    byte lockType = (byte) (i % 10);
		    
		    LockCacheKey key = new LockCacheKey(pid, lockType);
		    result.add(key);
		}

		for (int i = 1; i <= 1000; i++) {
		    Types::Pid pid = new Pid(Arte::Arte.getInstance(), idof[System::Unit], idof[Unit:id], i);
		    byte lockType = (byte) (i % 10);
		    
		    LockCacheKey key = new LockCacheKey(pid, lockType);
		    result.add(key);
		}

		return result;
	}

	/*Radix::System::AADCLocksDialogUtils:getStaticsticsEntryByObject-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::AADCLocksDialogUtils:getStaticsticsEntryByObject")
	public static  org.radixware.ads.System.common.AADCLocksListXsd.AADCLocksStatistics.Entry getStaticsticsEntryByObject (java.util.List<org.radixware.ads.System.common.AADCLocksListXsd.AADCLocksStatistics.Entry> statisticsEntries, Str objType, int ownerMember) {
		for (AADCLocksListXsd:AADCLocksStatistics.Entry entry : statisticsEntries) {
		    if (objType.equals(entry.ObjectType) && Str.valueOf(ownerMember).equals(entry.OwnerMember)) {
		        return entry;
		    }
		}

		return null;
	}


}

/* Radix::System::AADCLocksDialogUtils - Server Meta*/

/*Radix::System::AADCLocksDialogUtils-Server Dynamic Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AADCLocksDialogUtils_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcPQCFGYDX7BFMXKLSYPB2IKPXEU"),"AADCLocksDialogUtils",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::System::AADCLocksDialogUtils:Properties-Properties*/
						null,

						/*Radix::System::AADCLocksDialogUtils:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6RGGADCF6NFNPMP5N673B4EKBY"),"getTestAADCLockCacheKeyList",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCSH466APQJCIPPCVYWEAFWXT4M"),"getStaticsticsEntryByObject",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("statisticsEntries",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRP3STLNLDVBFBDHME35FWXQNT4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objType",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQX43EM3IB5BKFEBCT3KOBXW6DE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ownerMember",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOMLUCFILJJHMHESC6P437RVHJY"))
								},org.radixware.kernel.common.enums.EValType.XML)
						},
						null,
						null,null,null,false);
}

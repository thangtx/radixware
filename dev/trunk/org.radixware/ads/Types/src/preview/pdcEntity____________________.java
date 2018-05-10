
/* Radix::Types::Entity - Server Executable*/

/*Radix::Types::Entity-Server Dynamic Class*/

package org.radixware.ads.Types.server;

//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;        
//import org.radixware.kernel.server.types.Entity;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity")
public abstract published class Entity  extends org.radixware.kernel.server.types.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	public void copyPropVal(final org.radixware.kernel.server.meta.clazzes.RadPropDef prop, final org.radixware.kernel.server.types.Entity src) {
	//#dirty hack
	if (src instanceof CfgManagement::ICfgReferencedObject) {
	    Id extGuidPropId = ((CfgManagement::ICfgReferencedObject) src).getCfgReferencePropId();
	    if (java.util.Objects.equals(prop.getId(), extGuidPropId)) {
	        return;
	    }
	}
	super.copyPropVal(prop, src);
	//#dirty hack
	}

	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Entity_mi.rdxMeta;}

	/*Radix::Types::Entity:Nested classes-Nested Classes*/

	/*Radix::Types::Entity:Properties-Properties*/





























	/*Radix::Types::Entity:Methods-Methods*/

	/*Radix::Types::Entity:afterCreate-Platform Method Wrapper*/


	/*Radix::Types::Entity:afterDelete-Platform Method Wrapper*/


	/*Radix::Types::Entity:afterInit-Platform Method Wrapper*/


	/*Radix::Types::Entity:afterRead-Platform Method Wrapper*/


	/*Radix::Types::Entity:afterRollbackToSavepoint-Platform Method Wrapper*/


	/*Radix::Types::Entity:afterUpdate-Platform Method Wrapper*/


	/*Radix::Types::Entity:afterUpdatePropObject-Platform Method Wrapper*/


	/*Radix::Types::Entity:beforeAutoUpdate-Platform Method Wrapper*/


	/*Radix::Types::Entity:beforeCreate-Platform Method Wrapper*/


	/*Radix::Types::Entity:beforeDelete-Platform Method Wrapper*/


	/*Radix::Types::Entity:beforeInit-Platform Method Wrapper*/


	/*Radix::Types::Entity:beforeRollbackToSavepoint-Platform Method Wrapper*/


	/*Radix::Types::Entity:beforeUpdate-Platform Method Wrapper*/


	/*Radix::Types::Entity:create-Platform Method Wrapper*/


	/*Radix::Types::Entity:delete-Platform Method Wrapper*/


	/*Radix::Types::Entity:discard-Platform Method Wrapper*/


	/*Radix::Types::Entity:init-Platform Method Wrapper*/


	/*Radix::Types::Entity:keepInCache-Platform Method Wrapper*/


	/*Radix::Types::Entity:getClassDefinitionId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:getClassDefinitionId")
	public published  org.radixware.kernel.common.types.Id getClassDefinitionId () {
		return getRadMeta().getId();
	}

	/*Radix::Types::Entity:read-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:read")
	public published  void read () {
		read(org.radixware.kernel.common.enums.EEntityLockMode.NONE,null); 

		//read(???,null);
		//read(false,null);
	}

	/*Radix::Types::Entity:load-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:load")
	public static published  org.radixware.ads.Types.server.Entity load (org.radixware.kernel.common.types.Id entityId, Str pidStr) {
		try {
		    return Arte::Arte.getEntityObject(new Pid(Arte::Arte.getInstance(), entityId, pidStr));    
		} catch(Exceptions::EntityObjectNotExistsError e) {
		    return null;
		}
	}

	/*Radix::Types::Entity:create-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:create")
	public published  void create () {
		create(null);
	}

	/*Radix::Types::Entity:modifyUserPropList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:modifyUserPropList")
	public published  void modifyUserPropList (org.radixware.ads.Common.common.CommonXsd.UserProps userProps) {
		for (Common::CommonXsd:UserProp up : userProps.UserPropList) {
		    if (up.OwnVal == true)
		        this.setPropAsStr(up.getId(), up.SafeValue == null ? up.Value : up.SafeValue);
		    else
		        this.setPropHasOwnVal(up.getId(), false);
		}

	}

	/*Radix::Types::Entity:readAndLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:readAndLock")
	public published  void readAndLock (Int waitTime, boolean sessional) {
		read(sessional ? org.radixware.kernel.common.enums.EEntityLockMode.SESSION : org.radixware.kernel.common.enums.EEntityLockMode.TRANSACTION, waitTime);

	}

	/*Radix::Types::Entity:lock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:lock")
	public published  void lock (Int waitTime, boolean sessional) {
		lock(sessional ? org.radixware.kernel.common.enums.EEntityLockMode.SESSION : org.radixware.kernel.common.enums.EEntityLockMode.TRANSACTION, waitTime);

	}

	/*Radix::Types::Entity:getPid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:getPid")
	public final published  org.radixware.kernel.server.types.Pid getPid () {
		return getPid();

	}

	/*Radix::Types::Entity:readUserPropList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:readUserPropList")
	public published  java.util.List<org.radixware.ads.Common.common.CommonXsd.UserProp> readUserPropList (org.radixware.ads.Common.common.CommonXsd.UserProps rqUserProps) {
		java.util.List< Common::CommonXsd:UserProp> userProps = new java.util.ArrayList< Common::CommonXsd:UserProp>();

		for (Common::CommonXsd:UserProp rqProp : rqUserProps.UserPropList) {
		    Common::CommonXsd:UserProp rsProp = (Common::CommonXsd:UserProp) rqProp.copy();
		    rsProp.Value = this.getPropAsStr(rqProp.Id);
		    rsProp.SafeValue = rsProp.Value;

		    userProps.add(rsProp);
		}

		return userProps;

	}

	/*Radix::Types::Entity:getClassDefinitionTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:getClassDefinitionTitle")
	public published  Str getClassDefinitionTitle () {
		return getRadMeta().getTitle();

	}

	/*Radix::Types::Entity:init-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:init")
	public published  void init () {
		init(null, null, EntityInitializationPhase:PROGRAMMED_CREATION);
	}

	/*Radix::Types::Entity:getClassDefinitionName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:getClassDefinitionName")
	public published  Str getClassDefinitionName () {
		return getArte().getDefManager().getClassDef(getRadMeta().getId()).getName();
	}

	/*Radix::Types::Entity:calcTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:calcTitle")
	public published  Str calcTitle () {
		return calcTitle();
	}

	/*Radix::Types::Entity:rereadAndLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:rereadAndLock")
	public published  void rereadAndLock (Int waitTime, boolean sessional) {
		reread(sessional ? org.radixware.kernel.common.enums.EEntityLockMode.SESSION : org.radixware.kernel.common.enums.EEntityLockMode.TRANSACTION, waitTime);

	}

	/*Radix::Types::Entity:load-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:load")
	public static published  org.radixware.ads.Types.server.Entity load (org.radixware.kernel.server.types.Pid pid) {
		try {
		    return Arte::Arte.getEntityObject(pid);
		} catch(Exceptions::EntityObjectNotExistsError e) {
		    return null;
		}
	}

	/*Radix::Types::Entity:getPropAsStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:getPropAsStr")
	public published  Str getPropAsStr (org.radixware.kernel.common.types.Id propId) {
		org.radixware.kernel.server.meta.clazzes.RadPropDef dacProp = getRadMeta().getPropById(propId);

		return org.radixware.kernel.server.utils.SrvValAsStr.toStr(getArte(), getProp(propId), dacProp.getValType()); 

	}

	/*Radix::Types::Entity:releaseSessionalLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:releaseSessionalLock")
	public published  void releaseSessionalLock () {
		releaseSessionLock();
	}

	/*Radix::Types::Entity:reread-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:reread")
	public published  void reread () {
		//reread( false, null );
		reread( org.radixware.kernel.common.enums.EEntityLockMode.NONE, null );
	}

	/*Radix::Types::Entity:setPropAsStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:setPropAsStr")
	public published  void setPropAsStr (org.radixware.kernel.common.types.Id propId, Str valAsStr) {
		org.radixware.kernel.server.meta.clazzes.RadPropDef dacProp = getRadMeta().getPropById(propId);

		final Object val = org.radixware.kernel.server.utils.SrvValAsStr.fromStr( getArte(), valAsStr, dacProp.getValType()) ;

		if (val instanceof Pid){//RADIX-5484
		    setProp(propId, Arte::Arte.getEntityObject((Pid)val));
		} else {
		    setProp(propId, val);
		}
	}

	/*Radix::Types::Entity:getEntityDefinitionId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:getEntityDefinitionId")
	public published  org.radixware.kernel.common.types.Id getEntityDefinitionId () {
		return getEntityId();
	}

	/*Radix::Types::Entity:onCalcEditorPresentation-Platform Method Wrapper*/


	/*Radix::Types::Entity:onCalcTitle-Platform Method Wrapper*/


	/*Radix::Types::Entity:execCommand-Platform Method Wrapper*/


	/*Radix::Types::Entity:onSetSavepoint-Platform Method Wrapper*/


	/*Radix::Types::Entity:setIsAutoUpdateEnabled-Platform Method Wrapper*/


	/*Radix::Types::Entity:setPropHasOwnVal-Platform Method Wrapper*/


	/*Radix::Types::Entity:update-Platform Method Wrapper*/


	/*Radix::Types::Entity:getDdsMeta-Platform Method Wrapper*/


	/*Radix::Types::Entity:getPresentationMeta-Platform Method Wrapper*/


	/*Radix::Types::Entity:getProp-Platform Method Wrapper*/


	/*Radix::Types::Entity:purgePropCache-Platform Method Wrapper*/


	/*Radix::Types::Entity:setProp-Platform Method Wrapper*/


	/*Radix::Types::Entity:setProps-Platform Method Wrapper*/


	/*Radix::Types::Entity:getCurUserApplicableRoleIds-Platform Method Wrapper*/


	/*Radix::Types::Entity:isCurUserHasRole-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isCurUserHasRole")
	public published  boolean isCurUserHasRole (org.radixware.kernel.common.types.Id roleId) {


		java.util.List<Id> l = getCurUserApplicableRoleIds();

		return l.contains(roleId) || l.contains(idof[Arte::SuperAdmin]);
	}

	/*Radix::Types::Entity:isCurUserHasRole-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isCurUserHasRole")
	public published  boolean isCurUserHasRole (Str roleGuid) {
		return isCurUserHasRole(Id.Factory.loadFrom(roleGuid));
	}

	/*Radix::Types::Entity:isCurUserHasAnyRole-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isCurUserHasAnyRole")
	public published  boolean isCurUserHasAnyRole (java.util.Collection<org.radixware.kernel.common.types.Id> roleGuids) {
		if (roleGuids != null)  {
		    if (roleGuids.size()==0)
		        return isCurUserHasRole(idof[Arte::SuperAdmin]);
		    for (Id roleGuid : roleGuids) {
		        if (isCurUserHasRole(roleGuid)){
		            return true;
		        }
		    }
		}
		return false;
	}

	/*Radix::Types::Entity:isCurUserHasAnyRole-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isCurUserHasAnyRole")
	public published  boolean isCurUserHasAnyRole (java.util.Collection<Str> roleGuids) {
		if (roleGuids != null)  {
		    if (roleGuids.size()==0)
		        return isCurUserHasRole(idof[Arte::SuperAdmin]);
		    for (String roleGuid : roleGuids) {
		        if (isCurUserHasRole(roleGuid)){
		            return true;
		        }
		    }
		}
		return false;
	}

	/*Radix::Types::Entity:init-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:init")
	public published  void init (org.radixware.kernel.server.types.PropValHandlersByIdMap initPropValsById, org.radixware.ads.Types.server.Entity src) {
		init(initPropValsById, src, EntityInitializationPhase:PROGRAMMED_CREATION);

	}

	/*Radix::Types::Entity:onCalcParentTitle-Platform Method Wrapper*/


	/*Radix::Types::Entity:isDiscarded-Platform Method Wrapper*/


	/*Radix::Types::Entity:beforeUpdatePropObject-Platform Method Wrapper*/


	/*Radix::Types::Entity:onAutonomousStoreAfterRollback-Platform Method Wrapper*/


	/*Radix::Types::Entity:isLocked-Platform Method Wrapper*/


	/*Radix::Types::Entity:isCreatedAfterSavePoint-Platform Method Wrapper*/


	/*Radix::Types::Entity:isModified-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isModified")
	public published  boolean isModified () {
		return isModified();
	}

	/*Radix::Types::Entity:isLocked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isLocked")
	public published  boolean isLocked (boolean sessional) {
		return isLocked(sessional ? org.radixware.kernel.common.enums.EEntityLockMode.SESSION : org.radixware.kernel.common.enums.EEntityLockMode.TRANSACTION);
	}

	/*Radix::Types::Entity:isInDatabase-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isInDatabase")
	public published  boolean isInDatabase (boolean doSelect) {
		return isInDatabase(doSelect);
	}

	/*Radix::Types::Entity:purgePropCacheOnRollback-Platform Method Wrapper*/


	/*Radix::Types::Entity:isInited-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isInited")
	public published  boolean isInited () {
		return isInited();
	}

	/*Radix::Types::Entity:isDeleted-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isDeleted")
	public published  boolean isDeleted () {
		return isDeleted();
	}

	/*Radix::Types::Entity:isNewObject-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isNewObject")
	public published  boolean isNewObject () {
		return isNewObject();
	}

	/*Radix::Types::Entity:getClassDefinition-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:getClassDefinition")
	public published  org.radixware.kernel.server.meta.clazzes.RadClassDef getClassDefinition () {
		return getArte().getDefManager().getClassDef(getRadMeta().getId());
	}

	/*Radix::Types::Entity:afterCommit-Platform Method Wrapper*/


	/*Radix::Types::Entity:getAcsCoordinatesAsString-Platform Method Wrapper*/


	/*Radix::Types::Entity:isPersistentPropModified-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:isPersistentPropModified")
	public published  boolean isPersistentPropModified (org.radixware.kernel.common.types.Id propId) {
		return isPersistentPropModified(propId);
	}

	/*Radix::Types::Entity:setPropHasOwnVal-Platform Method Wrapper*/


	/*Radix::Types::Entity:getPropHasOwnVal-Platform Method Wrapper*/


	/*Radix::Types::Entity:copyPropVal-Platform Method Wrapper*/


	/*Radix::Types::Entity:markAsDeleted-Platform Method Wrapper*/


	/*Radix::Types::Entity:setPropHasOwnValAsStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Entity:setPropHasOwnValAsStr")
	public published  void setPropHasOwnValAsStr (org.radixware.kernel.common.types.Id propId, boolean x, Str valAsStr) {
		org.radixware.kernel.server.meta.clazzes.RadPropDef dacProp = getRadMeta().getPropById(propId);

		Object val = org.radixware.kernel.server.utils.SrvValAsStr.fromStr( getArte(), valAsStr, dacProp.getValType()) ;
		if (val instanceof Pid){//RADIX-5484
		    val = Arte::Arte.getEntityObject((Pid)val);
		}
		setPropHasOwnVal(propId, x, val);


	}

	/*Radix::Types::Entity:isAfterCommitRequired-Platform Method Wrapper*/


	/*Radix::Types::Entity:calcDescriptiveTitle-Platform Method Wrapper*/


	/*Radix::Types::Entity:getDescriptiveOwner-Platform Method Wrapper*/



}

/* Radix::Types::Entity - Server Meta*/

/*Radix::Types::Entity-Server Dynamic Class*/

package org.radixware.ads.Types.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Entity_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),"Entity",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Types::Entity:Properties-Properties*/
						null,

						/*Radix::Types::Entity:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK7A6YNBNHZDK3E7UBYFBHMM3FY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UCNHZSLQVGXPG7Z4SJQJPPSAY"),"afterDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYBM42RGIJNFVHAVUCD2MUQ7LDI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2FOZBHF6EBFFDKIACTMINAWRBM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCVICCNQ5IJE25PKGOLZVB6Y54E"),"afterRead",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNJ3W3ED77ZD5PHLKMOOVAUZ4WE"),"afterRollbackToSavepoint",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepointId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRGZTPTFCGJAF7NGXYFMMAD5WIM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W3TRDKMDRCGFJYNWSP72FQKSE"),"afterUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAZ2HAURWXVB53HEDENC67YLQAU"),"afterUpdatePropObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJVB7OOLAWFEHRN4U2UDHICY7MY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVal",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZAEUKYXRRBB5FDLBRFKG2NL5TA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBXCPIF3MHVDEFOSW67XOM5LQAY"),"beforeAutoUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("arg0",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH4F65YLVWRGOLAZ7TSW3VR3YLA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2BFR2MGVIFBJRB27WWRNA4D2UA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGZDY76GQDVEENNFB33JRO5ADCU"),"beforeInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("initPropValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCDI6ZVMJ4JGAJL4MZFSJG35DY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ6BDV6KH4JHFNE6MDKPWHZCNXM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFIIA3HDN4BGGJKIACGU2OWOWBQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSBXPM6BYERBBLMYMFEVJQ7TRIE"),"beforeRollbackToSavepoint",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepointId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMPR6IPDRBFCRHAPBDO4P6LF52M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI3D7RBRK4FCBXGQGYBCWPHX6VM"),"create",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP5VBEE4JT5E2TATX7YXR53BY7A"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4WCFC7ABDRBJTDPVIKETNLSXHM"),"delete",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBYOD4SHFVRFN5NST6R46OR4B7M"),"discard",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSA7W2J3SYZGY5FPBMGO3BW627U"),"init",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("initPropValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2PNQX7RBJVCBNM44OR4VEEDWGA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPNFEXBQEEZHCND3OE2J7RQNWXY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDQGAHTYBLFGD7NGFX5SOYVP4RM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZXGNZ6V2G5DYXN4ADVHZWN2C24"),"keepInCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("maxAgeInSeconds",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4SDCDZ6G7JFJRIT6BX5AE22FEI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3VQXRGQW6PNRDA2JAAMPGXSZKU"),"getClassDefinitionId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4RMKITY25DNRDOL4ABIFNQAABA"),"read",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5RIUNTEIFDORDM2AABIFNQAABA"),"load",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entityId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRJIMAW7RSNF4VBDJF4KJNPW2PU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMA422ATNBJGBLEJKCENETSYHGY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7X446DFQSTNRDDXBABIFNQAAAE"),"create",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDOE5EJA2LPPBDC5PABIFNQAABA"),"modifyUserPropList",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("userProps",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXBBMT5LHN5HVPMVBESFZ2CWB2Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFC5UU2I25DNRDOL4ABIFNQAABA"),"readAndLock",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("waitTime",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7P7CK44FMVDAVGDRXRW52LJEB4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sessional",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUJEB4SMBPRAHXLVGH7ABF2PC4A"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH452JGF3QLOBDBU2AAN7YHKUNI"),"lock",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("waitTime",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBDG6RKXKTVCNFMCTPA5RAL3UIU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sessional",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprABSOPZHCD5DRTLDSPNYOJQOOYM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ66JVZJF57NRDIMQAAMPGXSZKU"),"getPid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJUPTNA7DLPPBDC5QABIFNQAABA"),"readUserPropList",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rqUserProps",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU24CZNCUBRC2JME47ZPLYLPL4U"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJW53TRDZATOBDA26AAMPGXSZKU"),"getClassDefinitionTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMUQ4NQFMSTNRDDXBABIFNQAAAE"),"init",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNAAWAWQX6PNRDA2JAAMPGXSZKU"),"getClassDefinitionName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQCGEEZOUAHOBDA26AAMPGXSZKU"),"calcTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQN7LVIA74TNRDC7HAAMPGXSZKU"),"rereadAndLock",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("waitTime",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4AEJGK7365FLRAPY5KAFLNDFWY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sessional",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr64GFWWRAMZGDJOIZ7JCFNF3Y54"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTJMTUS3Y6HNRDIMQAAMPGXSZKU"),"load",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprX2XH3F7G3BAP3EBVFW7QC3TEMI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTOCEDADAFLPBDLXAAAN7YHKUNI"),"getPropAsStr",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5ICSGG5ZG5GCLOCEGQ4ZH5J46Q"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVAYKNE3RB3OBDNTPAAMPGXSZKU"),"releaseSessionalLock",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYXHXCUYRXTNRDCJ3ABIFNQAABA"),"reread",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ2YBPSBDODORDJIYABIFNQAABA"),"setPropAsStr",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOYKW6UTUD5CD3HHEXGIVZ2E7J4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("valAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLIBSZ37PTFHGRFJWOBVHCQ2WOA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ4HH5VYW6PNRDA2JAAMPGXSZKU"),"getEntityDefinitionId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZJ672NR72ZEPJP2YHQ4KTL2VWY"),"onCalcEditorPresentation",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("presentationId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFX4OIHIUS5GGBFY5S3EQLRIDP4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4D6ME6VX45BYVNJXZPBTAA7KP4"),"onCalcTitle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLMUWH6DNB5CY7HQRMHYA6SOXIA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVQQNGEVQJBFIPIIJALJND4AQOY"),"execCommand",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cmdId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAQKOKGM6CRBI5G3QMXIUJHXTY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXG6EDW6NBJBA5HSP4E6EDKXMG4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRLEVP4IS3FDQFPMMBA4MHQYTW4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2HIHVCCQ7BGEPFTVS56V5J2Z7Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("output",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWKQUBHIAHRBVXIRS2SQDHR3UT4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNO5SF2TKZBYLK72NE4ZL3MS6I"),"onSetSavepoint",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepointId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3LP3KCIB3JGETEX3EQLMHRJCYM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVGODV43DM5ARDDXWLZ2HCHRAPY"),"setIsAutoUpdateEnabled",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isEnabled",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXQTBAX6T5VB2BK6JUZ5HA2PIFM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAVNGZFNZJFEM5F264QAIRSOHGE"),"setPropHasOwnVal",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPIHKPMWG7NGVVL3A3FCAQASURE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("x",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSVWKMNGINRAANHH3IZT4TNIZJI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5A5TVHUCNVHFXIT7FDJ75MILHE"),"update",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVPP424BCMBA5RALD2S7CGTZJ3I"),"getDdsMeta",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthT7R5Q7D74ZA3NAKLGIGWEN5UYE"),"getPresentationMeta",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthP2EWJ2PSAZGJVESQKCRACMBWKI"),"getProp",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUGJFOUBEHFCIXF643XF44GL4SI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYI2EJSKBMZEWJF2HWO5JJHUSTQ"),"purgePropCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth37BJGQIJQZAMXIPNDIDMU45MJM"),"setProp",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5XZCNIEO7NCMJOTZ74YAKQY744")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("x",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVWKZU74YZNAOBLHIDICNOC3AQI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEYS5WP6Y6NCGRHV55VXQTV6SLU"),"setProps",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYZCP6G6CJNAGBFT4FC6F722GWM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPNPHJW3TBJBIVP4LH7MOAT2LRM"),"getCurUserApplicableRoleIds",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUQQDNINZQPORDMNEABIFNQAABA"),"isCurUserHasRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roleId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN6KFJVTIWJAHJA5BN5IL6UPYNU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYBR653Q2JNBH3FJB3NYQDQHA4I"),"isCurUserHasRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roleGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUJHXCHGCXRAHXEBTMY4NXI5EZE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQM2TESGIL5A2NIBESHWPW6RI5A"),"isCurUserHasAnyRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roleGuids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMMTYJMUZPNCL7OKUH34SRKMYTQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4J27D4EEXVEHNGEXYQ32YM2I2M"),"isCurUserHasAnyRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roleGuids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5MBVMNMLM5ADZK5ZOQPWBMRDCE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUTPF5BWBTFC35IDCO6WZHM3DB4"),"init",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("initPropValsById",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEZ4FQNMOFBHOZESGYSFRZSDBLI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI55CMBGB2FCSPFVBBH3ZXMRPYI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth23RIMT3QYRALDCOPINDYKRUC5I"),"onCalcParentTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ptPropId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMDOCUZRWNFER7OGVW6K5CAPDLU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parent",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLJY2WXKT3RBLNEL4X4L5P7WGNM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parentTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYH3LWGSWYND7DHJCEWIADGE5RI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPOB2MIQLR5FBHODMKA2AWCPQQM"),"isDiscarded",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIKSUJAGWQBHKFPQPE53AC7YD7Y"),"beforeUpdatePropObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYLR5WJO74FGATLLY574Y4QTH4M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVal",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC5NP3R2ZIRHE3JQ2C7OOD2Q2OA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3FB6HQRDY5A3JC2WXXCBSC6V5A"),"onAutonomousStoreAfterRollback",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savePointId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4B4GAWVPGJB2ZPXSD6OJ62MX4E"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZC3AQRPUYRB5FL7FB4A7JGE46U"),"isLocked",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lockMode",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKZD36TR3JNEKTBPYHMDG5DNBSI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXIWIUQAVFJFVXG4AEIGDKCBN2U"),"isCreatedAfterSavePoint",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("spId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQVRC6UKJWBFY5AFHPKCTONW65E"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthE23L423YALPBDA2PABIFNQAABA"),"isModified",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4A25XVWOKTPBDNOBAAN7YHKUNI"),"isLocked",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sessional",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTIVKIFF2FFBW7J4W63CCOZIAFU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOPU3AAQWZPNRDB64AALOMT5GDM"),"isInDatabase",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("doSelect",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLFXWA3UKDJC77M2O4L66GSJYCI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIC7IDL6YHFFYPBBNNPYVLE2K6E"),"purgePropCacheOnRollback",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepointId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ4PIJGKWUBCSJGAYCHZY4O5TGY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY7OUFFEVQBDJ3OJXQ45DW3TODU"),"isInited",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3ZCIEHV3MNDJHGQV64AG7HSIPE"),"isDeleted",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB6B4DAPRVJA5PIUOPJUE5VMLX4"),"isNewObject",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPMV4LNLNNNHOBCBBDCS2P7CISA"),"getClassDefinition",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMJ36OUASKZBYZBMQG6QJNNKLAM"),"afterCommit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isNewObject",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDWP2GKZEHNGVBEFLJZGWEGKUX4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLWKHHJQDHFFDBGVTSCDT6NEIHI"),"getAcsCoordinatesAsString",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAAZKIGKVHVA4JF5MNQBV4B7VLM"),"isPersistentPropModified",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI4MBYHM345FA7FKIH2YJGKVLXQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWDCPS7A65NCT7KQDF4OZ62EB5U"),"setPropHasOwnVal",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE2P7YQDPFNBXJIUDID3SWC3KLM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("x",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMVVXO45RURG5FB25FI6IQE5HR4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("val",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM6ZUK6VJN5CDBLRYBC7W7JND64"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRPTTJ54JTJBUVN6OEHHDOAOXTQ"),"getPropHasOwnVal",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA45CC3M3U5CAHHLNDIZSOGDKNQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2ID6LLB4ORG75JFEH45HYYK75U"),"copyPropVal",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prop",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWU3DVEFJ4FCAVGCILF5JX4Q74Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD7Y3NPD2VRHNTMUZAVILIKCLSE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVOCVGFPORBCZ5DTQ5NFRWXDMXQ"),"markAsDeleted",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPHNDFCSTMZG3ZJLTPNKQ56PLXA"),"setPropHasOwnValAsStr",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOYKW6UTUD5CD3HHEXGIVZ2E7J4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("x",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3CDF7MBVJVCGNHYGFLVANSDTH4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("valAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLIBSZ37PTFHGRFJWOBVHCQ2WOA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6PD7UT74UREYRPSXKLYRTUPPFI"),"isAfterCommitRequired",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY6QJBWGAGREARHD4UFEMARAZHI"),"calcDescriptiveTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTX7AFQWDNRDV5JJBAXUEYH43II"),"getDescriptiveOwner",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Types::Entity - Localizing Bundle */
package org.radixware.ads.Types.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Entity - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
		loadStrings2();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки что объект удален из кэша ARTE вызовом метода discard() или delete()");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2BVTPATF5FCZJEEMSTDZ4XZCCA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2IONR7PLJNF2NA2NGZZPR6UJCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Presentation definition for the given entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дефиниция презентации данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JRJZ33OXJHSDA3HH6XDML3W6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the presentation being used");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор используемой презентации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2KWX3TC6DJCTXGOVZILWXA6XIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки, что значение хранимого в БД свойства модифицировано, но еще не сохранено в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2OTIFGGAEJCNJFTVSN2QTYHR6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the DB table definition for the given entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения дефиниции таблицы БД данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PRJAGAUZJGZ3KIOJUZPOP3LTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после обновления (но не вставки) в БД другого объекта, принадлежащего данному. Предназначен для перекрытия в классах-потомках для реализации дополнительных действий или проверок после обновления определенных свойств.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XKLL77QKVCYHGXHFSMCXPRJZY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object containing the data of the user-defined entity properties being modified");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, содержащий данные модифицируемых пользовательских свойств сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls37HQ4TL7FVEEDKPKNCG2WJBOOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the entity class definition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор дефиниции класса сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3BH5M2IDG5GNJIUP3YPKADJ2QY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод блокировки объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YQLQYKVEZB2ZCKV5KRTOLQ2TY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns descriptive (logical) owner of this object, see Entity.getDescriptiveTitle()");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает описательного (логического) владельца данного объекта. См. Entity.getDescriptiveOwner()");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls44UT2VW6JNF5VE6EJ2I7IN57CA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение флага \"собственное значение\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AI4TRECONC2HPSAP6TNWFTV2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sets the property value for the given object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод установки значения свойства данного объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4GBCIRYMYBFLNED456GKC3BHGQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый перед помещением объекта в БД (insert). Предназначен для перекрытия в классах-потомках для реализации каких-либо дополнительных действий или проверок перед вставкой объекта в БД.\nВ случае если данный метод возвращает false - процесс вставки объекта в БД прерывается, объект удаляется из кэша ARTE (см. discard)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4NF3FW3UTZA35LNHUDE25Z3S4U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения свойства данного объекта по идентификатору");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4O74GYTMLBFN7H6IYTUERHQ6WM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сущность-источник (в случае если данный объект является копией другого объекта, в противном случае - null)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4UQ7SU7CHNEWHJMX5A5D77IGH4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый перед вставкой или обновлением данного объекта во время выполнения автоматического обновления БД. Автоматическое обновление БД выполняется при коммите транзакции, либо при вызове метода Arte::updateDatabase()\nДанный обработчик вызывается перед вызовом beforeCreate/beforeUpdate\nЕсли данный обработчик возвращает false - автоматическое обновление данного объекта не выполняется\n");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls52P6HPP3K5H5VLAOU7436QAQCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity instance. If the instance is not found, returns null.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экземпляр сущности. Если экземпляр не найден - возвращает null");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5IWOTYBFYVA2VIWIKE3JTBSUUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5LHOQBPDFJCRLEKP7VISC7GNGQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки что текущий оператор имеет указанную роль и эта роль применима к данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls66RUFW6L3ZBH3B2KX2WQJ2AIK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки, что объект содержит модифицированные свойства, значения которых еще не сохранены в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6EY6SPFEMJFGRMPT3Q4T2CPJMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод копирования значения свойства из объекта-источника в данный объект. Вызывается для каждого копируемого свойства при создании сущности-копии, обеспечивает копирование сложных объектов. Может быть перекрыт в классе-потомке с целью изменения логики копирования, например если необходимо исключить копирование каких-либо отдельных свойств при создании сущности-копии.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6GEM3SYKMBCZNO6V674JXJZWGA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список идентификаторов применимых ролей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6HLM4D2JFNEKFHNHPYHANGVEGU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод сброса значений всех свойств объекта в кэше ARTE и последующего чтения объекта из БД.\nЭквивалентен вызову пары методов (но выполняется за один запрос к БД):<br>\n<code>\nthis.purgePropCache();<br>\nthis.read();\n</code>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6LVCQA7FIZGC3K45IBYZCULURM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор свойства над которым выполняется команда (для команд выполняемых над отдельными свойствами), null если команда выполняется над сущностью в целом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6MDIV7TSQREKZHT5NUBZ72YGWI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после обновления объекта в БД. Предназначен для перекрытия в классах-потомках для реализации каких-либо дополнительных действий или проверок после обновления объекта в БД.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6NRKQA4CQ5F2FILHPKVQGGF2FI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод загрузки экземпляра сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TFDSZOY3VBKFBH4YJ6BQ5NYFI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод чтения свойств объекта из БД. В случае если объект не существует в БД - генерирует исключение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XALT7GGVJCXTM35SH2HSNI2MM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сущность-источник (в случае если данный объект является копией другого объекта, в противном случае - null)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XG5POXCG5EZJNAVTWH5P772II"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property being defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор устанавливаемого свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls74LADWENGFHH3JTAW4S7MEGA2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время ожидания блокировки в секундах. Если объект не может быть заблокирован на протяжении этого времени - генерирует исключение LockTimeoutError\nВ случае null - ожидание блокировки будет неограничено по времени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7OYJSPXOUFF2DH4VQHP275F7JY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the role being checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор проверяемой роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TWWR7654JE6LBL6DJTE7YDO6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заполняемые при инициализации значения свойств объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7Y73CFGCIBGEPBVVZJFTXARCHI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливаемое значение. В случае если x==false (то есть свойство не имеет собственного значения) - данный параметр игнорируется");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA2M3R4CDBVF5DOCDEVGTADKQYQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Object initialization phase");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Фаза инициализации объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAC7IAAAX7RF6PGHY622UO64DWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAO3PSL6IE5HGZBOJYL2MIFW5WQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Флаг, указывающий вид блокировки\ntrue - блокировка устанавливается на протяжении сессии\nfalse - блокировка устанавливается на протяжении транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAO6746XTAFF67LFWAGDBQZST6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the presentation definition for the given entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения дефиниции презентации данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAYXHWKDM75EKVM5SUKVEXURUE4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Value being set");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливаемое значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB65I5Q7BSFGJZIF7W7P6CU3JB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод-обработчик события расчета названия объекта. Вызывается в процессе выполнения метода calcTitle.\nМожет перекрываться в классах-потомках для модификации результата.\nРеализация базового класса возвращает переданный параметр без каких-либо модификаций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBARUVRMSP5DKBCEHQCAGOQR7VU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сущность-источник (в случае если данный объект является копией другого объекта, в противном случае - null)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBEYLSPEGOZBEZPHRJNHO3W6PBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение флага \"собственное значение\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBFRBDBRQU5EWTBD7WXDSBH5CF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event processing result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат обработки события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBNDBSB22DVGYPJI5VE63F2T3NM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"List of IDs for the roles being checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список идентификаторов проверяемых ролей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBO6ZAL5ZZZDUJOULGOFXVJL6FM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Initialization phase");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Фаза инициализации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBP5ADFOTPRGMRLK5AVBCSF3UPI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Definition of the entity class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дефиниция класса сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBQC5QQHKUFAEZE5CUIO4V23WOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the name of the entity class definition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения имени дефиниции класса сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBUHNHR644FAJPFRXASRO7UX5TA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the list of role identifiers for the current user to be applied to this entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения списка идентификаторов ролей текущего пользователя, которые применимы к данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBVQMYQO4QVB2ZN4Z33BBQCOXW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the role being checked presented as a string");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор проверяемой роли в виде строки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZJDTN5K5JFLTLZW7WGH5MLTZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property being checked.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор проверяемого свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC2YF4CKMNNEWTB7653KW6LC7AA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод указания что данный объект должен храниться в кэше ARTE на протяжении указанного времени.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHM5ZOITP5EDRHCEAPA6IH4GRA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event processing result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат обработки события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCIDK7J3ABZA5DEZWSCLBY4B3NA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор дефиниции таблицы БД данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCJ32VTZU7JAZJJEJ7MDZ5JQHWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the description of the entity class definition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения названия дефиниции класса сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCX2FZ3SNDZDKDI4ATXS4YFEMIE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Object name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCZVZ4LBWT5FXNFCWZ3ZAPDO4BY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the savepoint being checked.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор проверяемой точки отката");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD7H6X25FIBCIXJQCIA5E2YCOZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<p>Класс, представляющий абстрактную запись в БД, является базовым классом для всех Entity-классов системы. Обеспечивает базовый функционал взаимодействия с БД и кэшем Arte. Основные возможности, предоставляемые Entity:</p>\n<ol>\n<li>Получение экземпляра из БД по его первичному ключу (PID).</li>\n<li>Программное создание нового экземпляра.</li>\n<li>Сохранение новой или обновление данных существующей записи в БД.</li>\n<li>Блокировка записи в БД.</li>\n<li>Удаление записи из БД.</li>\n</ol>\n<p>Жизненный цикл Entity выглядит следующим образом:</p>\n<ol>\n<li>Создание java-объекта (final Entity entity=new EntityClass()), где EntityClass - конкретный класс, потомок Entity.</li>\n<li>Инициализация (entity.init()). На этой фазе срабатывают обработчики beforeInit и afterInit. После инициализации объект попадает в кэш Arte, откуда может быть записан в БД. При инициализации может указываться объект-источник, свойства которого копируются в соответствующие свойства инициализируемого объекта (это используется, в частности, при копировании объектов в Radixware Explorer).</li>\n<li>Запись нового объекта в БД (entity.create()). При этом срабатывают обработчики beforeCreate и afterCreate. Может вызываться как явно, так и неявно в процессе исполнения Arte.updateDatabase(), либо Arte.commit(). При неявном вызове дополнительно срабатывает обработчик beforeAutoUpdate.</li>\n<li>В случае, если запись объекта в БД не требуется (например, вследствие какой-либо ошибки), чтобы избежать автоматического создания объекта он может быть сброшен из кэша при помощи метода entity.discard().</li>\n<li>Запись обновлений объекта в БД (entity.update()). При этом срабатывают обработчики beforeUpdate и afterUpdate. Может вызываться как явно, так и неявно в процессе исполнения Arte.updateDatabase(), либо Arte.commit(). При неявном вызове дополнительно срабатывает обработчик beforeAutoUpdate.</li>\n<li>Удаление объекта из БД (entity.delete()). При этом срабатывают обработчики beforeDelete и afterDelete.</li>\n</ol>\n<p>\nПолучение объекта из БД может быть выполнено при помощи методов loadByPK и loadByPid. Данные методы не определены в entity и генерируются RadixWare Designer для каждого конкретного entity-класса в зависимости от первичного ключа таблицы в которой хранится данная сущность.<br>\nПримеры: <code>public static EntityClass loadByPid(Str pid, boolean checkExistance), public static EntityClass loadByPK(Int id, boolean checkExistance)</code><br>\nПоследний параметр указывает способ обращения к БД. В случае, если <code>checkExistance==false</code>, методы loadByPid и loadByPK гарантированно не выполняют запроса в БД. Если объект с таким первичным ключом находится в кэше - он возвращается из кэша. Если объект не находится в кэше - создается и возвращается \"заглушка\" с указанным первичным ключом. Таким образом, метод, вызванный с <code>checkExistance==false</code> не возвращает null. При обращении к свойствам этого объекта он будет прочитан из БД (будет выполнен запрос), что может привести к исключению (например если объекта нет в БД).<br>\nВ случае если <code>checkExistance==true</code>, методы loadByPid и loadByPK гарантируют что возвращенный объект был как минимум один раз прочитан из БД. Если в кэше не находится объекта, либо находится \"заглушка\", то выполняется чтение объекта из БД. В случае если чтение завершается неудачно, то метод возвращает null. Параметр <code>checkExistance==true</code> гарантирует что объект был прочитан из БД, но не гарантирует что объект продолжает существовать к моменту вызова метода (он может быть возвращен из кэша в то время как соответствующая запись в БД удалена параллельным процессом). Если требуется гарантировать существование объекта - следует воспользоваться функцией блокировки (<code>entity.lock(), entity.rereadAndLock()</code>). При чтении объекта (либо в результате обращения к свойству, либо в результате работы метода loadByPid/loadByPK с параметром checkExistance==true) вызывается обработчик afterRead.\n</p>\n<p>Сущности обычно существуют в кэше ARTE на протяжении сессии. Однако, для объекта можно установить более длительный срок хранения вызовом метода keepInCache</p>\n<p>Сущность может быть создана путем копирования другой сущности (подходящего класса), в этом случае в методы init и create передается параметр src - сущность-источник. Экземпляр сущности источника, передаваемый в методы init и create должен быть одним и тем же. Значения свойств новой сущности берутся из свойств сущности-источника, кроме случая когда они заданы явно в обработчике события beforeInit или в параметре initPropValsById метода init. В этом случае явно заданные значения имеют приоритет перед значениями из сущности-источника.</p>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDACEHBPQZRABNP6WBIPN6BNIQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Первичный ключ данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDDHRLSNVGBAWDJRPMTMIOQHUFE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод удаления сущности из кэша ARTE, обычно применяется к объектам, которые были инициализированы, но еще не сохранены в БД. Объект продолжает существовать в оперативной памяти, однако обращение к любому из хранящихся в БД свойств такого объекта приведет к исключению IllegalUsageError.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDG6K2ZVYIFFBTKIVDVU2NZD5SQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Value being set as a string (see ValAsStr)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливаемое значение в строковом виде (см. ValAsStr)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQ4BVVQQQZDBDKAXZCN22W54UQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property being checked.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор проверяемого свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQ4OKW6QBNE4RGUQZXZWLL3BOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sets the property value for the given object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод установки значения свойства данного объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDSSTIAEIYJD7XJLAGP5P3RXKZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Флаг, указывающий необходимость запроса в БД.\nЕсли false - проверяется состояние объекта в кэше ARTE. Существование объекта в БД не гарантируется, даже то что такой объект когда-либо существовал.\nЕсли true - выполняется запрос в БД. В этом случае гарантируется проверка реального существования объекта в БД на момент выполнения данного метода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDVI7FTYBOVD63PKVOCI34FXVVA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Loads the entity instance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод загрузки экземпляра сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDYJYNWSHSRCA5EFN54WRCBFFAQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"List of IDs presented as strings for the roles being checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список идентификаторов проверяемых ролей в строковом виде");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE332NHNXQZF27JAFFLAJD2KMAM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object filled with the output data of the command");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, заполняемый выходными данными команды");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEE3NR72F7ZBRRODAVKORGOVOWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки, что данный объект помещен в БД после установки указанной точки отката");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJUISV5GFZGH7MLLDSONT5MBQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод снятия сессионной блокировки, ранее установленной методом lock (readAndLock, rereadAndLock) с параметром sessional=true");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEN5XAORHRFEW3DFHZHHZ7NEPEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Primary key of the entity being loaded as a string");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Первичный ключ загружаемой сущности в виде строки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEOPWE3AFXBFAFBKEUXFMJOVO4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Executes the command on the entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод исполнения команды над сущностью");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEQRYKMZSORB6PI7HUEVN2HJATA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event processing result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат обработки события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFD7TF5E3BZEYRGNHNLTY4D7AME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки что текущий оператор имеет любую из ролей, содержащихся в списке и эта роль применима к данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFE7GXAVIAJAENHPHCYRX4QWOV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после удаления объекта из БД. Предназначен для перекрытия в классах-потомках для реализации каких-либо дополнительных действий или проверок после удаления объекта из БД.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFJP2HCWA7JFZ7NLADPFF74ZBJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Definition of the property being copied");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дефиниция копируемого свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFOCB6XU3LJEL7EG33FKSXBUYDI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Flag value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение флага");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFPOGO5DP4FGT7H2UOIOC2P2ZJE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод установки флага, указывающего что свойство имеет собственное (неунаследованное) значение, а также собственного значения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFQH3X3LQNNBEHCUNBUNU3XINUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFSQPNAZ2W5GUFMAPOLLFPJ7UOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the parent object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название родительского объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWOKXYX24VDSRGGMTARADS22RQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод инициализации объекта. Эквивалентен вызову this.init(initPropValsById, src, EntityInitializationPhase:PROGRAMMED_CREATION)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG5HFPMI275BMTHKULIQ5RBBGXQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property being requested");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор запрашиваемого свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGBOMVK674VDXREKFVMWWOMJBIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Первичный ключ загружаемой сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGBQ37GXCFVHEFCSYSLIRN3JNL4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод установки флага, указывающего что свойство имеет собственное (неунаследованное) значение, а также собственного значения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGC65IDAPS5ENZBSQANGQOFPULU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Object name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGEZU2LHM45E4ZKTDPEQAGJCWXU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип проверяемой блокировки: true - сессионная, false - транзакционная");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFCBI76JYFAANGLA2VCCXXO4RM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод установки флага, указывающего что свойство имеет собственное (неунаследованное) значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGRS6C7YNLBEQZA62T2XYI5SWSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object containing the input data of the command");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, содержащий входные данные команды");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGSQYMNPGXFBKLDIJAAF7TJGUHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый перед удалением объекта из БД (delete). Предназначен для перекрытия в классах-потомках для реализации каких-либо дополнительных действий или проверок перед удалением объекта из БД.\nВ случае если данный метод возвращает false - процесс удаления объекта прерывается");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGWJXPOMLJJEE7MSACDFJKBDQBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик диалога для отображения оператору (в случае интерактивной команды), null если отображение диалога не требуется (неинтерактивная команда)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH46BVJW3YRGQHGJNMHNAZDHSOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод создания объекта в БД (insert). Эквивалентен вызову this.create(null);");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH5ZEHU46OFGM5GEALSRSTDFSRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод сброса значений всех свойств объекта в кэше ARTE. Вызывается при откате транзакции до точки отката.\nМожет перекрываться в классах потомках для реализации дополнительной логики. Реализация базового класса эквивалентна purgePropCache()");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH7KD6DRPKVHJXLBEJTMZYKXIWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения идентификатора дефиниции таблицы БД данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFFYGBUIGRC3DEWRN24AZQG4BE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the value of the entity property by identifier. The value is returned as a string.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения значения свойства данного объекта по идентификатору. Значение возвращается в виде строки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIOQY7QIKAJCFJAMVHSS5EYOU74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод блокировки объекта и последующего чтения его свойств.\nЭквивалентен вызову пары методов:<br>\n<code>\nthis.lock(waitTime, sessional);<br>\nthis.read();\n</code>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIRR43RWOMFHY5MOWCUTJ44CGZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Updated object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обновленный объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJDU5T6ZFEZGIDKOTEACGCY6FTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Savepoint ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор устанавливаемой точки отката");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJHBJVS4TIJHYJO464I2OD6LIAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJKDBGLHQGRGZNDE6UZ7XA6UYKQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод создания объекта в БД (insert). Если задан объект-источник, то копирует значения свойств, которым еще не заданы значения из объекта-источника.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJKRKUIVCCVDPHDKPWMKWSZG6TY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the definition of the entity class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения дефиниции класса сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJNMC7DRCDFBKTEGEADM6WOG7PA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Flag value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение флага");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJOFJGY773ZBETMGJ6QKCP5HVRM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после отката транзакции. Предназначен для сохранения каких-либо автономных (то есть не подверженных откату) данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJONNMIL5FJBMRHZD62UZ3MBODU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод установки флага, указывающего что автоматическое обновление БД разрешено. Если данный флаг не установлен - то данный объект не будет автоматически записываться в БД в процессе автоматического обновления");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJX7AHNU4EJAKBGVEK2HMKEA4WQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	@SuppressWarnings("unused")
	private static void loadStrings2(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Updated  object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обновленный объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKDJ6QJOAIJGAVMMCNYJJBZTFCA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки что данный объект заблокирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKLUYBRRJ3JGKRA5ORZUWLW6KRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый при выборе презентации сущности. Может скорректировать выбор презентации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL4MDH2BOQBAD5NVZXMQQ77C4DY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Продолжительность хранения объекта в кэше в секундах. Если 0 или null - объект хранится в кэше на протяжении жизни версии в ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL674I6FI3JGABBHV6C2JR2QJFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLECAT3E275DWVEIQPZKAKOMVXQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения объекта, хранящего первичный ключ данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLHRCTMXDF5BP5AM4ZYNL7KVYFM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат обработки события. false - сохранение автономных данных не требуется, true - автономные данные сохранены");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLMUQU22AJ5DTNC5JJTZ6AN4F6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Рассчитанное название объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPOLD655AZHDHLDGE5SQHP2Z3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки, что данный объект создан в кэше ARTE, но еще не сохранен в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLWZIEMMFZZHTJFWXXOE5GXBJZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description of the entity class definition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название дефиниции класса сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM3CMZOCZHJEQ5GF3XH4H7ERQNU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после помещения объекта в БД (insert). Предназначен для перекрытия в классах-потомках для реализации каких-либо дополнительных действий или проверок после вставки объекта в БД.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM7IBL4VKDJDIVHR3VORR2GRHHI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNFKRAIJUBC27FDIXLW5W54SXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property referring to the updated object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор свойства, ссылающегося на обновленный объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMY2OQEO7C5FGXD3B2PANM24DFQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый перед инициализацией объекта. Предназначен для перекрытия в классах-потомках для реализации каких-либо действий или проверок перед инициализацией объекта.\nЕсли данный метод возвращает false - процесс инициализации объекта прерывается.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN33A25RF4FALFIGOKPUSODX324"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заполняемые при инициализации значения свойств объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN3CJSSSUYRA5REQTAGIRI22FFM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сущность-источник (в случае если данный объект является копией другого объекта, в противном случае - null)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNEMCWZSCHJBJRJNLTZ63UF5QM4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Identifier of the savepoint to which the transaction is rolled back");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор точки отката на которую производится откат");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFD6E2LX6NFZXASSI2GQO4VBBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод массовой установки значений свойств объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVXP5ZLIKREORFBLG6YAB5BS7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Property value as a string (see ValAsStr)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение свойства в виде строки (см. ValAsStr)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNY24OZYBCNBSJCHOKQLMC7L4G4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время ожидания блокировки в секундах. Если объект не может быть заблокирован на протяжении этого времени - генерирует исключение LockTimeoutError\nВ случае null - ожидание блокировки будет неограничено по времени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYHW266ITRFSLNFDX6NGZU7OSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO54YDTFLJRAT5BSKU5D536VANU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сущность откуда копируется значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6Y5AFOSAZGTBFLUYX5AXWN5DU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки что данный объект заблокирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsODQ2FITIR5FR5KIIEX3GAJRWBY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после явного (методом read/reread) или неявного (при обращении к свойству незагруженного объекта) чтения объекта из БД. Предназначен для перекрытия в классах потомках для реализации каких-либо дополнительных действий или проверок после чтения объекта.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOGWG6DPOPVBG3LPE5JOMQVMYQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таблица, содержащая значения устанавливаемых свойств в виде \"идентификатор свойства - значение свойства\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKY6R4DNEJCBFPTS2ZM2VLZ5AQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый перед обновлением (но не вставкой) в БД другого объекта, принадлежащего данному. Предназначен для перекрытия в классах-потомках для реализации дополнительных действий или проверок перед обновлением определенных свойств.\nВ случае если данный метод возвращает false - процесс обновления объекта прерывается.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOLAXWK2QDBFWPAXZ6Z3KLE66LA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns descriptive title of the object (a title including titles of all descriptive owners)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает описательный заголовок объекта (заголовок, включающий в себя заголовки всех описательных владельцев)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOLZA3GMDYNH23HWZEKO4BQIPRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сущность-источник (в случае если данный объект является копией другого объекта, в противном случае - null)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOUCEN4XLMVBFNFTBX5JL2AS4BI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после коммита транзакции, выполнившей над данным объектом какую-либо модификацию в БД. Не вызывается если объект был удален.\nПредназначен для перекрытия в классах-потомках для выполнения каких-либо дополнительных действий после коммита транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOYUEUCMEOVECXPUWTPDKBXDJTA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the selected presentation");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор выбранной презентации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZLPBLORXBGHHFTPVN7D7RZNHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPD3ZCVLU2NCLVOARGOZBDVDG2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPPFOD5B4SFF4VGTGF45BDYT6UM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Identifier of the savepoint to which the transaction is rolled back");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор точки отката на которую производится откат");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPUDRZVWNDVER5OL4JKHTVKRT7I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the ID of the entity class definition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения идентификатора дефиниции класса сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ36QNGVCKFEOTMAFKE6F3VWYOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сущность-источник (в случае если данный объект является копией другого объекта, в противном случае - null)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ7SQECOZI5EAPFV4ZKGD65H6DI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event processing result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат обработки события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDEKBYUM4NBQPEIFALK7PMBBBQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQHSHC6VADVD3HCO4RRIDK2OGMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект, содержащий начальные значения свойств объекта, которые будут установлены в процессе инициализации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQHZXYVRXPRBGBLEGEZ2BCR5NUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки, что объект сохранен в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQPQ5OFNO65ELRMONNBMTTEEWOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event processing result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат обработки события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQQFALKOR3FAAJIO7AOWWG3FWL4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property being set");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор устанавливаемого свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQU436M7YPBCLDJZFENERZBTJGQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Флаг, указывающий вид блокировки\ntrue - блокировка устанавливается на протяжении сессии\nfalse - блокировка устанавливается на протяжении транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQZ5DFJEBUNGA3FERYNFN4W4TBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод расчета названия родительского объекта. Используется для корректировки заголовков свойств типа ParentRef, отображаемых в редакторе данного объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5AKFUJRNBHZDJLQ7BDGU6WYWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки что текущий оператор имеет указанную роль и эта роль применима к данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5WF2VGUPJE4RAUE73QN4CROHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод блокировки объекта и последующего перечитывания его свойств.\nЭквивалентен вызову пары методов (но выполняется за один запрос к БД):<br>\n<code>\nthis.lock(waitTime, sessional);<br>\nthis.reread();\n</code>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRATMGQVCWREVLIROS4VSYNH56U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reason for automatic DB update");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Причина автоматического обновления БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCAC3LJHP5D5XLO5AEPNGRMNVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки что текущий оператор имеет любую из ролей, содержащихся в списке и эта роль применима к данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRK6EH6MO7VCKRCWMHBHE73TUF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Value being set as a string (see ValAsStr)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливаемое значение в строковом виде (см. ValAsStr)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKIRISBVPRCENIVUMWLJWZDMJA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после отката транзакции на точку отката. Вызывается для объектов, затронутых транзакцией. Предназначен для перекрытия в классах-потомках для реализации каких-либо действий после отката транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSBNJAFZRKBA5ZKI3YEMMRZMY74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дефиниция таблицы БД данной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSCNOSNR6BRCH3AOL4C6ER6OGJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the entity being loaded");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор загружаемой сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSCWEEINZVVDSDF3NCL7DPERDTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод расчета названия объекта в соответствии с форматом, указанным в дефиниции класса.\nПо окончании расчета вызывает метод onCalcTitle(title), который может модифицировать название произвольным образом и возвращает его результат.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSD4MIVK2HBA7PB5GLN4KTZDZKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property referring to the parent object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор свойства, ссылающегося на родительский объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSJFCEWE5QZASTHJY3BRXXV6DPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property being set");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор устанавливаемого свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSR5ZEASTNJAPBH7OIHTCZQRWRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор точки отката транзакции на которую произошел откат");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSVVHIW4QNZEIXHSGLYNGA546OI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Флаг, указывающий был ли объект создан в рамках транзакции, либо существовал ранее.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSWEZP2CYFZG53OUDYBUBK6VXSU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the command being executed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор исполняемой команды");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSWRRZHS3LZHR7MNDKOVCOPNXM4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSWVILSPSINENVC4DB5RMDVDB4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод сброса значений всех свойств объекта в кэше ARTE.\nПоследующее обращение к любому из свойств вызовет чтение объекта из БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTLT7S7TJE5ENRMYIUIRITBC32M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод инициализации объекта.\nИнициализация объекта происходит в несколько этапов:\n<ol>\n<li>Вызывается обработчик события beforeInit(initPropValsById, src, phase). В случае, если beforeInit возвращает false - процесс инициализации прерывается, объект остается в неинициализированном состоянии</li>\n<li>Заполняются значения свойств, переданных в параметре initPropValsById (если initPropValById!=null)</li>\n<li>Заполняются свойства, значения которых берутся из последовательностей (согласно определению DDS сущности)</li>\n<li>Если src!=null, то копируются значения свойств из сущности источника с помощью вызова метода copyPropVal</li>\n<li>Устанавливаются значения свойств по умолчанию (для тех свойств у которых заданы значения свойств по умолчанию)</li>\n<lli>Для свойств, который наследуют значения из родительского объекта и не имеют собственного значения - включается режим наследования</li>\n<li>Сущность помечается как инициализированная</li>\n<li>Вызывается обработчик события afterInit</li>\n</ol>\nЭтапы 3-6 выполняются только для свойств, значения которых еще не заданы к этому моменту, то есть, если свойство получило значение при выполнении обработчика beforeInit, то значение считается заданным и оно не будет перезаписано значением из сущности-источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTNYH75IBLZEBJDOHJKJWSB2WBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity instance. If the instance is not found, returns null.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экземпляр сущности. Если экземпляр не найден - возвращает null");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTP77IOP4FNDADFIK2IKAAGPI7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTPF53NCGVRHKLPJUQFMVUVTUMY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property for which the flag is set");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор свойства, которому устанавливается флаг");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTUTWMOWEB5EPJPZUWN5DSIGL5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сущность-источник (в случае если данный объект является копией другого объекта, в противном случае - null)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTXCMA4A45BFJNLR373A67LOQ2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU4JTPSECSFE2JKWZKPO6VIDAGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки, что указанное свойство имеет собственное (неунаследованное) значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU6KLWV4URRBC3GIDXY4V5IBESU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the entity class definition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя дефиниции класса сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUCXFD5FGHNC7DHGBXWULE3UK6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый перед откатом транзакции на точку отката. Вызывается для объектов, затронутых транзакцией. Предназначен для перекрытия в классах-потомках для реализации каких-либо действий перед откатом транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUE7LGQ3ZUJFPDPYSKYY55MDAQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event listener called after the object is initialized");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после инициализации объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKVXTKLEJRHR5H773Z3HTBFHB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUVDFXKZBTJG63GZNRKLKQ7JNN4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV4VTTBJBCBCARKVE5ESKLPSXEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор точки отката до которой производится откат");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVTVJ4CCEDRC4PBXOMVFVQBW2LE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время ожидания блокировки в секундах. Если объект не может быть заблокирован на протяжении этого времени - генерирует исключение LockTimeoutError\nВ случае null - ожидание блокировки будет неограничено по времени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW4K4OIB4QFAPXFA5ARXSMXFXDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый перед обновлением объекта в БД (update). Предназначен для перекрытия в классах-потомках для реализации каких-либо дополнительных действий или проверок перед записью объекта в БД.\nВ случае если данный метод возвращает false - процесс обновления объекта в БД прерывается");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7XR67FSAZDCNLJUTA5E24JEIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property for which the flag is set");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор свойства, которому устанавливается флаг");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGVEGBQ33ZGX3N2HOSQC44H3OE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификаторы колонок, используемых для разграничения прав доступа в виде строки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKLDVEINQVAYNOPX6N5YX23BZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки, что данный объект инициализирован методом init()");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWZA6WH6QO5AI5JAID7USW65OKM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the property referring to the updated object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор свойства, ссылающегося на обновленный объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX2RPVTTDANBB5PIOZRN56HRA2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значения свойств объекта, установленные в редакторе перед выполнением команды (но не сохраненные в самом объекте)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXGQXLJOPMJDLBN6EECRI2FSZRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Property value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXN44EVUVCFHIXD4YMIYYFR5CF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор запрашиваемого свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXQMKTQYXIZFCVGENOE7HDEKJ7Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод модификации пользовательских свойств сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSXL4R34YRCB7ODYS4LLJFYAX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event processing result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат обработки события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY2GMIHL64ZCYVLNK6HQ2N6ODTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип проверяемой блокировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYGTMEEHQWZA4ZDRQOLOVAFF3YE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название объекта, рассчитанное стандартным алгоритмом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYMF2IC2EJZDODAVLWFBXLHIZKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод обновления объекта в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYTZW2RUHIBFSJEDLGRAUSHAFFQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод удаления сущности из БД. При удалении сущности она также удаляется из кэша ARTE, однако сам объект продолжает существовать в оперативной памяти. Обращение к любому из хранящихся в БД свойств такого объекта приведет к исключению EntityObjectNotExistsError.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIBK5T5RXREUHN5KEN4H7DDSLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод инициализации объекта. Эквивалентен вызову this.init(null, null, EntityInitializationPhase:PROGRAMMED_CREATION)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZN2TSEE4Y5DV3EYTRGMCSOTVAI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки, что объект удален из БД вызовом метода delete()");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSFNUE4DOZBCRBLHOEK7YN7UOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Флаг, указывающий вид блокировки\ntrue - блокировка устанавливается на протяжении сессии\nfalse - блокировка устанавливается на протяжении транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSQV66JFRFAW3ENGD4ZJOJFQZY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event listener called when setting the savepoint");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый при установке точки отката транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZYRJ4LAQLZGA7B25PLS6WPH6XE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parent object");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Родительский объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZZH43HXEFJBJ5AMRWO3MHDG2ZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Entity - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbpdcEntity____________________"),"Entity - Localizing Bundle",$$$items$$$);
}

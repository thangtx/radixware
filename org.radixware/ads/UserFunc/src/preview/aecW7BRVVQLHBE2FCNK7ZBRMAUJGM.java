
/* Radix::UserFunc::LibUserFunc - Server Executable*/

/*Radix::UserFunc::LibUserFunc-Entity Class*/

package org.radixware.ads.UserFunc.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc")
public final published class LibUserFunc  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return LibUserFunc_mi.rdxMeta;}

	/*Radix::UserFunc::LibUserFunc:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:Properties-Properties*/

	/*Radix::UserFunc::LibUserFunc:libName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:libName")
	public published  Str getLibName() {
		return libName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:libName")
	public published   void setLibName(Str val) {
		libName = val;
	}

	/*Radix::UserFunc::LibUserFunc:guid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:guid")
	public published  Str getGuid() {
		return guid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:guid")
	public published   void setGuid(Str val) {
		guid = val;
	}

	/*Radix::UserFunc::LibUserFunc:ownerLib-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ownerLib")
	public published  org.radixware.ads.UserFunc.server.UserFuncLib getOwnerLib() {
		return ownerLib;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ownerLib")
	public published   void setOwnerLib(org.radixware.ads.UserFunc.server.UserFuncLib val) {
		ownerLib = val;
	}

	/*Radix::UserFunc::LibUserFunc:profile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profile")
	public published  Str getProfile() {
		return profile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profile")
	public published   void setProfile(Str val) {

		if(val != null && !val.equals(internal[profile])) {
		    internal[profile] = val;
		    profileHtml = calcProfileHtml();
		}
	}

	/*Radix::UserFunc::LibUserFunc:description-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:description")
	public published  Str getDescription() {
		return description;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:description")
	public published   void setDescription(Str val) {
		description = val;
	}

	/*Radix::UserFunc::LibUserFunc:name-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:name")
	public published  Str getName() {
		return name;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:name")
	public published   void setName(Str val) {
		name = val;
	}

	/*Radix::UserFunc::LibUserFunc:caching-Dynamic Property*/



	protected org.radixware.ads.Utils.server.Caching caching=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:caching")
	private final  org.radixware.ads.Utils.server.Caching getCaching() {

		if(internal[caching]==null)
		    internal[caching]= new Caching(this);
		return internal[caching];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:caching")
	private final   void setCaching(org.radixware.ads.Utils.server.Caching val) {
		caching = val;
	}

	/*Radix::UserFunc::LibUserFunc:lastUpdateTimeGetter-Dynamic Property*/



	protected org.radixware.ads.Utils.server.ILastUpdateTimeGetter lastUpdateTimeGetter=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:lastUpdateTimeGetter")
	public  org.radixware.ads.Utils.server.ILastUpdateTimeGetter getLastUpdateTimeGetter() {

		if (internal[lastUpdateTimeGetter] == null) {

		    internal[lastUpdateTimeGetter] = new Utils::ILastUpdateTimeGetter() {

		        public DateTime getLastUpdateTime() {
		            final GetLibUserFuncLastUpdateTimeCursor cursor = GetLibUserFuncLastUpdateTimeCursor.open(LibUserFunc.this.libName);
		            try {
		                if (cursor.next()) {
		                    return cursor.lastUpdateTime;
		                } else {
		                    return null;
		                }
		            } finally {
		                cursor.close();
		            }
		        }
		    };
		}
		return internal[lastUpdateTimeGetter];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:lastUpdateTimeGetter")
	public   void setLastUpdateTimeGetter(org.radixware.ads.Utils.server.ILastUpdateTimeGetter val) {
		lastUpdateTimeGetter = val;
	}

	/*Radix::UserFunc::LibUserFunc:usedLibraryFunctions-Dynamic Property*/



	protected org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.UserFunc.server.LibUserFunc> usedLibraryFunctions=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:usedLibraryFunctions")
	public published  org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.UserFunc.server.LibUserFunc> getUsedLibraryFunctions() {

		if(upUserFunc != null){
		    return upUserFunc.usedLibraryFunctions;
		}else{
		    return new org.radixware.kernel.server.types.ArrEntity<>(getArte());
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:usedLibraryFunctions")
	public published   void setUsedLibraryFunctions(org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.UserFunc.server.LibUserFunc> val) {
		usedLibraryFunctions = val;
	}

	/*Radix::UserFunc::LibUserFunc:ufResultType-Dynamic Property*/



	protected org.radixware.schemas.xscml.TypeDeclarationDocument ufResultType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ufResultType")
	public  org.radixware.schemas.xscml.TypeDeclarationDocument getUfResultType() {

		if (upUserFunc != null) {
		    return upUserFunc.resultType;
		} else {
		    return null;
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ufResultType")
	public   void setUfResultType(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
		ufResultType = val;
	}

	/*Radix::UserFunc::LibUserFunc:isFreeForm-Dynamic Property*/



	protected Bool isFreeForm=(Bool)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("1",org.radixware.kernel.common.enums.EValType.BOOL);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isFreeForm")
	public  Bool getIsFreeForm() {
		return isFreeForm;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isFreeForm")
	public   void setIsFreeForm(Bool val) {
		isFreeForm = val;
	}

	/*Radix::UserFunc::LibUserFunc:upUserFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFunc")
	public published  org.radixware.ads.UserFunc.server.UserFunc getUpUserFunc() {
		return upUserFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFunc")
	public published   void setUpUserFunc(org.radixware.ads.UserFunc.server.UserFunc val) {
		upUserFunc = val;
	}

	/*Radix::UserFunc::LibUserFunc:isUfDefined-Dynamic Property*/



	protected Bool isUfDefined=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isUfDefined")
	public published  Bool getIsUfDefined() {

		return upUserFunc!=null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isUfDefined")
	public published   void setIsUfDefined(Bool val) {
		isUfDefined = val;
	}

	/*Radix::UserFunc::LibUserFunc:presentableDescription-Dynamic Property*/



	protected Str presentableDescription=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:presentableDescription")
	public published  Str getPresentableDescription() {

		if (upUserFunc != null && (upUserFunc.description != null || upUserFunc.lastChangelogRevisionDate != null)) {
		    return upUserFunc.getDescriptionWithChangelogInfo(upUserFunc.description);
		} else
		    return description;
	}

	/*Radix::UserFunc::LibUserFunc:newUserFuncClassId-Dynamic Property*/



	protected Str newUserFuncClassId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:newUserFuncClassId")
	public  Str getNewUserFuncClassId() {
		return newUserFuncClassId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:newUserFuncClassId")
	public   void setNewUserFuncClassId(Str val) {
		newUserFuncClassId = val;
	}

	/*Radix::UserFunc::LibUserFunc:dummyRefOnFunction-Dynamic Property*/



	protected org.radixware.ads.UserFunc.server.UserFunc dummyRefOnFunction=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:dummyRefOnFunction")
	public  org.radixware.ads.UserFunc.server.UserFunc getDummyRefOnFunction() {
		return dummyRefOnFunction;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:dummyRefOnFunction")
	public   void setDummyRefOnFunction(org.radixware.ads.UserFunc.server.UserFunc val) {
		dummyRefOnFunction = val;
	}

	/*Radix::UserFunc::LibUserFunc:upUserFuncClassId-Dynamic Property*/



	protected Str upUserFuncClassId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFuncClassId")
	public  Str getUpUserFuncClassId() {

		if (upUserFunc != null) {
		    return upUserFunc.getClassDefinitionId().toString();
		}
		return null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFuncClassId")
	public   void setUpUserFuncClassId(Str val) {
		upUserFuncClassId = val;
	}

	/*Radix::UserFunc::LibUserFunc:fullProfile-Dynamic Property*/



	protected Str fullProfile=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:fullProfile")
	public  Str getFullProfile() {

		String profile = profile;
		if (profile == null) {
		    return "<unknown>";
		}
		int index = profile.indexOf(" ");
		if (index > 0 && index+1<profile.length()) {
		    return profile.substring(0, index) + " " + libName + "::" + profile.substring(index+1);
		} else
		    return profile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:fullProfile")
	public   void setFullProfile(Str val) {
		fullProfile = val;
	}

	/*Radix::UserFunc::LibUserFunc:profileHtml-Dynamic Property*/



	protected Str profileHtml=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profileHtml")
	private final  Str getProfileHtml() {

		if (internal[profileHtml] != null) {
		    return internal[profileHtml];
		}
		internal[profileHtml] = calcProfileHtml();
		return internal[profileHtml];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profileHtml")
	private final   void setProfileHtml(Str val) {
		profileHtml = val;
	}

	/*Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle-Dynamic Property*/



	protected Str userFuncChangeLogTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle")
	private final  Str getUserFuncChangeLogTitle() {

		if (internal[userFuncChangeLogTitle] != null) {
		    return internal[userFuncChangeLogTitle];
		}
		if (upUserFunc != null && upUserFunc.changeLog != null) {
		    internal[userFuncChangeLogTitle] = upUserFunc.changeLog.calcTitle();
		} else {
		    internal[userFuncChangeLogTitle] = "<not defined>";
		}
		return internal[userFuncChangeLogTitle];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle")
	private final   void setUserFuncChangeLogTitle(Str val) {
		userFuncChangeLogTitle = val;
	}

	/*Radix::UserFunc::LibUserFunc:inheritedDescription-Dynamic Property*/



	protected Str inheritedDescription=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:inheritedDescription")
	private final  Str getInheritedDescription() {

		if (upUserFunc != null) {
		    return upUserFunc.inheritedDescription;
		}
		return null;
	}



















































































































































	/*Radix::UserFunc::LibUserFunc:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		if(this.guid == null){
		    this.guid = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:USER_FUNC_CLASS).toString().replace("usf","luf");
		}
		ownerLib.onUpdate();
		return super.beforeCreate(src);
	}

	/*Radix::UserFunc::LibUserFunc:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:beforeDelete")
	protected published  boolean beforeDelete () {
		ownerLib.onUpdate();
		return super.beforeDelete();

		            
		            
	}

	/*Radix::UserFunc::LibUserFunc:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:beforeUpdate")
	protected published  boolean beforeUpdate () {

		if(upUserFunc!=null){
		    description = upUserFunc.description;
		}
		ownerLib.onUpdate();
		return super.beforeUpdate();
	}

	/*Radix::UserFunc::LibUserFunc:LibUserFunc-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:LibUserFunc")
	public  LibUserFunc (Str libName, Str guid, Str classGuid) {
		this.libName = libName;
		this.guid = guid;
		this.newUserFuncClassId = classGuid;
	}

	/*Radix::UserFunc::LibUserFunc:LibUserFunc-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:LibUserFunc")
	public  LibUserFunc () {

	}

	/*Radix::UserFunc::LibUserFunc:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:loadByPK")
	public static published  org.radixware.ads.UserFunc.server.LibUserFunc loadByPK (Str guid, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(guid==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANG66BP4NZC55NQTVVF24BXAKI"),guid);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),pkValsMap);
		try{
		return (
		org.radixware.ads.UserFunc.server.LibUserFunc) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::UserFunc::LibUserFunc:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:loadByPidStr")
	public static published  org.radixware.ads.UserFunc.server.LibUserFunc loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),pidAsStr);
		try{
		return (
		org.radixware.ads.UserFunc.server.LibUserFunc) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::UserFunc::LibUserFunc:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:export")
	  org.radixware.ads.UserFunc.common.ImpExpXsd.LibUserFuncDocument export () {
		final ImpExpXsd:LibUserFuncDocument xDoc = ImpExpXsd:LibUserFuncDocument.Factory.newInstance();
		final ImpExpXsd:LibUserFunc item = xDoc.addNewLibUserFunc();
		item.Id = this.guid;
		item.Name = this.name != null ? this.name : "";
		if (this.upUserFunc != null)
		    item.UserFunc = this.upUserFunc.export();
		item.Description = this.description;
		item.LibName = this.libName;
		item.Profile = this.profile;
		return xDoc;
	}

	/*Radix::UserFunc::LibUserFunc:importNew-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:importNew")
	 static  org.radixware.ads.UserFunc.server.LibUserFunc importNew (Str libName, org.radixware.ads.UserFunc.common.ImpExpXsd.LibUserFunc xmlUserFunc) {
		final LibUserFunc function = create(libName, xmlUserFunc.Id, xmlUserFunc.UserFunc.ClassGUID);
		importData(function, xmlUserFunc, null);
		function.create();
		return function;

	}

	/*Radix::UserFunc::LibUserFunc:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		LibUserFunc srcFunc = null;
		if (src instanceof LibUserFunc) {
		    srcFunc = (LibUserFunc) src;
		}

		if (upUserFunc == null) {
		    UserFunc uf;
		    if (newUserFuncClassId != null) {
		        uf = (UserFunc) Arte::Arte.getInstance().newObject(Types::Id.Factory.loadFrom(newUserFuncClassId));
		    } else {
		        uf = new UserFunc.FreeForm();
		    }
		    //System.out.println(uf.getClass().getName());

		    uf.init();

		    uf.upDefId = idof[LibUserFunc:upUserFunc].toString();
		    uf.upOwnerEntityId = idof[System::LibUserFunc].toString();
		    uf.upOwnerPid = this.Pid.toString();

		    Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xDoc = uf.javaSrc;
		    org.radixware.schemas.adsdef.UserFuncProfile prof = xDoc.AdsUserFuncDefinition.addNewUserFuncProfile();
		    prof.MethodName = name;
		    uf.javaSrc = xDoc;

		    upUserFunc = uf;
		    uf.description = description;
		}

		if (srcFunc != null) {
		    profile = srcFunc.profile.replaceFirst(srcFunc.name, name);
		} else {
		    if (profile == null || profile.isEmpty()) {
		        if (upUserFunc != null && upUserFunc.classGuid != idof[UserFunc.FreeForm].toString()) {
		            profile = upUserFunc.displayProfile;
		        } else {
		            profile = "void " + name + "()";
		        }
		    }
		}

		update();
		super.afterCreate(src);
	}

	/*Radix::UserFunc::LibUserFunc:afterUpdatePropObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:afterUpdatePropObject")
	protected published  void afterUpdatePropObject (org.radixware.kernel.common.types.Id propId, org.radixware.kernel.server.types.Entity propVal) {
		ownerLib.onUpdate();

		if(upUserFunc!=null){
		    profile = upUserFunc.displayProfile;
		    update();
		}

		super.afterUpdatePropObject(propId, propVal);


	}

	/*Radix::UserFunc::LibUserFunc:getLibParamStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getLibParamStr")
	public published  Str getLibParamStr (Str paramName) {
		return ownerLib.getParamStr(paramName);
	}

	/*Radix::UserFunc::LibUserFunc:getLibParamInt-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getLibParamInt")
	public published  Int getLibParamInt (Str paramName) {
		return ownerLib.getParamInt(paramName);
	}

	/*Radix::UserFunc::LibUserFunc:getLibParamNum-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getLibParamNum")
	public published  Num getLibParamNum (Str paramName) {
		return ownerLib.getParamNum(paramName);
	}

	/*Radix::UserFunc::LibUserFunc:getLibParamDateTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getLibParamDateTime")
	public published  java.sql.Timestamp getLibParamDateTime (Str paramName) {
		return ownerLib.getParamDateTime(paramName);
	}

	/*Radix::UserFunc::LibUserFunc:getLibParamChar-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getLibParamChar")
	public published  Character getLibParamChar (Str paramName) {
		return ownerLib.getParamChar(paramName);
	}

	/*Radix::UserFunc::LibUserFunc:getLibParamBool-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getLibParamBool")
	public published  Bool getLibParamBool (Str paramName) {
		return ownerLib.getParamBool(paramName);
	}

	/*Radix::UserFunc::LibUserFunc:beforeInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:beforeInit")
	protected published  boolean beforeInit (org.radixware.kernel.server.types.PropValHandlersByIdMap initPropValsById, org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		guid = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:LIB_USERFUNC_PREFIX).toString();

		return super.beforeInit(initPropValsById, src, phase);
	}

	/*Radix::UserFunc::LibUserFunc:refreshCache-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:refreshCache")
	  void refreshCache () {
		if (ownerLib == null)
		    return;

		boolean isOk = caching.refresh(this.lastUpdateTimeGetter);
		if (isOk) {
		    resetCache();
		}
	}

	/*Radix::UserFunc::LibUserFunc:resetCache-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:resetCache")
	private final  void resetCache () {

	}

	/*Radix::UserFunc::LibUserFunc:call-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:call")
	public published  java.lang.Object call (java.util.Map<Str,java.lang.Object> params) {
		if(upUserFunc!=null){
		    try{
		        return upUserFunc.invoke(params);    
		    }catch(Exceptions::Exception e){
		        throw new AppError(this.libName + "::" + this.guid + " " + "Invocation exception",e);
		    }
		}else 
		    throw new AppError(this.libName + "::" + this.guid + " " + "Function not defined");
	}

	/*Radix::UserFunc::LibUserFunc:call-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:call")
	public published  java.lang.Object call (java.lang.Object... params) throws java.lang.Exception {
		if(upUserFunc!=null){
		    try{
		        return upUserFunc.invoke(params);    
		    }catch(Exceptions::Exception e){
		        //TODO: add information about library function
		        throw e;
		      //  throw new (this. + "::" + this. + " " + ,e);
		    }
		}else 
		    throw new AppError(this.libName + "::" + this.guid + " " + "Function not defined");
	}

	/*Radix::UserFunc::LibUserFunc:getXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getXml")
	public static  org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition getXml (org.radixware.kernel.common.types.Id libUserFuncId) {
		if (libUserFuncId == null) {
		    return null;
		}
		return getXml(libUserFuncId.toString());
	}

	/*Radix::UserFunc::LibUserFunc:getXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getXml")
	public static  org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition getXml (Str libUserFuncId) {
		 try {
		    if (libUserFuncId == null)
		        return null;

		    LibUserFuncHeadersCursor cursor = LibUserFuncHeadersCursor.open(libUserFuncId);
		    try {
		        if (cursor.next()) {
		            try {
		                return getXml(cursor.libUserFunc, cursor.ufLibName);
		            } catch (Exceptions::Throwable e) {
		                Arte::Trace.warning(Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
		            }
		        }
		    } finally {
		        cursor.close();
		    }
		} catch (Exceptions::DatabaseError e) {
		    Arte::Trace.warning(Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
		}
		return null;
	}

	/*Radix::UserFunc::LibUserFunc:getXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getXml")
	public static  org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition getXml (org.radixware.ads.UserFunc.server.LibUserFunc libFunc, Str libName) {
		if (libFunc != null && libFunc.upUserFunc != null) {
		    UserFunc userFunc = libFunc.upUserFunc;
		    Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xDoc = userFunc.javaSrc;
		    if (xDoc != null && xDoc.AdsUserFuncDefinition != null && xDoc.AdsUserFuncDefinition.UserFuncProfile != null) {
		        if (xDoc.AdsUserFuncDefinition.UserFuncProfile.LibName == null) {
		            //(RADIX-10658) 
		            //If context of request is new user func entity, ARTE transaction is readonly
		            //that why we must create copy to set library name.
		            Meta::AdsDefXsd:AdsUserFuncDefinitionDocument copy = Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.Factory.newInstance();
		            copy.addNewAdsUserFuncDefinition().set(xDoc.AdsUserFuncDefinition);
		            copy.AdsUserFuncDefinition.UserFuncProfile.LibName = libName;
		            xDoc = copy;
		        }
		    } else if (xDoc != null) {
		        Meta::AdsDefXsd:AdsUserFuncDefinitionDocument copy = Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.Factory.newInstance();
		        copy.addNewAdsUserFuncDefinition().set(xDoc.AdsUserFuncDefinition);
		        copy.ensureAdsUserFuncDefinition().ensureUserFuncProfile().LibName = libName;
		        copy.AdsUserFuncDefinition.UserFuncProfile.MethodName = libFunc.name;
		        xDoc = copy;
		    }
		    return xDoc != null ? xDoc.AdsUserFuncDefinition : null;
		}
		return null;
	}

	/*Radix::UserFunc::LibUserFunc:calcProfileHtml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:calcProfileHtml")
	private final  Str calcProfileHtml () {
		final String profileHtml = profile;
		if (profileHtml == null || profileHtml.isEmpty()) {
		    return profileHtml;
		}

		try {

		    StringBuilder sb = new java.lang.StringBuilder();
		    int sepIndex = profileHtml.indexOf(' ');
		    int openIndex = profileHtml.indexOf('(');
		    int closeIndex = profileHtml.indexOf(')');

		    sb.append(profileHtml.substring(0, sepIndex));
		    sb.append("<font color=\"#0066ff\"><b>");
		    sb.append(profileHtml.substring(sepIndex, openIndex));
		    sb.append("</b></font>");
		    sb.append("(");
		    String args = profileHtml.substring(openIndex + 1, closeIndex);
		    if (!args.isEmpty()) {
		        //Parse generic arguments, if found replace them with mark _GEN_PARAM#_
		        java.util.Map<String, String> generics = null;
		        int genericIndex = 0;
		        int genericStartPos = 0;
		        int angleBracketsCounter = 0;
		        String argsWithoutGenerics = new String(args);
		        String key;
		        for (int curPos = 0; curPos < args.length(); curPos++) {
		            char ch = args.charAt(curPos);
		            switch (ch) {
		                case '<':
		                    if (++angleBracketsCounter == 1) {
		                        genericStartPos = curPos;
		                    }
		                    break;
		                case '>':
		                    if (--angleBracketsCounter == 0) {
		                        if (generics == null) {
		                            generics = new java.util.HashMap<String, String>();
		                        }
		                        key = "_GEN_PAR#" + genericIndex++ + "_";
		                        String generic = args.substring(genericStartPos, curPos + 1);
		                        generics.put(key, generic);
		                        argsWithoutGenerics = argsWithoutGenerics.replaceFirst(generic, key);
		                    }
		                    break;
		            }
		        }

		        //build arguments string without generics
		        java.lang.StringBuilder sbArgs = new java.lang.StringBuilder();
		        String[] parts = argsWithoutGenerics.split(",");
		        for (String part : parts) {
		            String[] arg = part.trim().split(" ");
		            sbArgs.append(arg[0]).append(" ").append("<font color=\"#d3730b\">").append(arg[1]).append("</font>").append(", ");
		        }
		        sbArgs.setLength(sbArgs.length() - 2);

		        //Return generics into string
		        String resArgs = sbArgs.toString();
		        if (generics != null) {
		            for (java.util.Map.Entry<String, String> gen : generics.entrySet()) {
		                resArgs = resArgs.replaceFirst(gen.Key, "&lt;" + gen.Value.substring(1, gen.Value.length() - 1) + "&gt; ");
		            }
		        }

		        sb.append(resArgs);
		    } else {
		        sb.append(" ");
		    }
		    sb.append(profileHtml.substring(closeIndex));

		    return sb.toString();

		} catch (Exception ex) {
		    return org.apache.commons.lang.StringEscapeUtils.escapeHtml(profileHtml);
		}
	}

	/*Radix::UserFunc::LibUserFunc:importOne-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:importOne")
	 static  org.radixware.ads.UserFunc.server.LibUserFunc importOne (Str libName, org.radixware.ads.UserFunc.common.ImpExpXsd.LibUserFunc xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null)
		    return null;

		Str guid = xml.Id;
		final Str classGuid = xml.UserFunc.ClassGUID;
		LibUserFunc obj = LibUserFunc.loadByPK(guid, true);
		if (obj == null) {
		    obj = create(libName, guid, classGuid);
		    obj.importThis(xml, helper);
		} else {
		    if (!libName.equals(obj.libName)) {
		        helper.reportWarnings(obj, Str.format("Function was moved from library '%s' to library '%s'", obj.libName, libName));
		        obj.libName = libName;
		        obj.importThis(xml, helper);
		    } else {
		        if (helper instanceof CfgManagement::CfgImportHelper.Interactive) {
		            switch (helper.getActionIfObjExists(obj)) {
		                case UPDATE:
		                    obj.importThis(xml, helper);
		                    break;
		                case NEW:
		                    guid = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:LIB_USERFUNC_PREFIX).toString();
		                    obj = create(libName, guid, classGuid);
		                    obj.importThis(xml, helper);
		                    break;
		                case CANCELL:
		                    helper.reportObjectCancelled(obj);
		                    return null;
		            }
		        } else {
		            obj.importThis(xml, helper);
		        }
		    }
		}
		return obj;
	}

	/*Radix::UserFunc::LibUserFunc:importThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:importThis")
	  void importThis (org.radixware.ads.UserFunc.common.ImpExpXsd.LibUserFunc xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		String profile = xml.UserFunc.Profile;
		if (profile == null) {
		    profile = xml.Profile != null ? xml.Profile : xml.Name;
		}
		profile = profile;
		helper.createOrUpdateAndReport(this);
		importData(this, xml, helper);
	}

	/*Radix::UserFunc::LibUserFunc:exportThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:exportThis")
	  org.radixware.ads.UserFunc.server.LibUserFunc exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		data.itemClassId = idof[CfgItem.LibUserFuncSingle];
		data.object = this;
		ImpExpXsd:LibUserFuncDocument s = export();
		data.data = s;
		data.fileContent = s;
		return null;
	}

	/*Radix::UserFunc::LibUserFunc:createFreeForm-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:createFreeForm")
	public static  org.radixware.ads.UserFunc.server.LibUserFunc createFreeForm (Str libName, Str guid) {
		return create(libName, guid, null);
	}

	/*Radix::UserFunc::LibUserFunc:importAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:importAll")
	 static  void importAll (Str libName, org.radixware.ads.UserFunc.common.ImpExpXsd.Functions xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null || libName == null) {
		    return;
		}

		if (helper.getUserFuncImportHelper() != null) {
		    helper.getUserFuncImportHelper().setCompileDeferred(true);
		}
		try {
		    for (ImpExpXsd:LibUserFunc xFunc : xml.FunctionList) {
		        importOne(libName, xFunc, helper);
		    }
		} finally {
		    //If we not in Package context, we should invoke
		    //finalizeImport explicitly to compile deferred functions.
		    if (helper.getContextPacket() == null) {
		        helper.finalizeImport();
		        if (helper.getUserFuncImportHelper() != null) {
		            helper.getUserFuncImportHelper().setCompileDeferred(false);
		        }
		    }
		}
	}

	/*Radix::UserFunc::LibUserFunc:replaceFromCfgItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:replaceFromCfgItem")
	  void replaceFromCfgItem (org.radixware.ads.UserFunc.server.CfgItem.LibUserFuncSingle cfg, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (cfg.libraryRef.getRefVal() instanceof UserFuncLib) {
		    libName = ((UserFuncLib) cfg.libraryRef.getRefVal()).name;
		    importThis(cfg.myData.LibUserFunc, helper);
		} else {
		    Arte::Trace.error("Library for import not defined", Arte::EventSource:UserFunc);
		    helper.reportWarnings(cfg.myData.LibUserFunc.Name, "Library for import library function is not defined. Configure Library function configuration item reference to define library.");
		}
	}

	/*Radix::UserFunc::LibUserFunc:importData-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:importData")
	private static  void importData (org.radixware.ads.UserFunc.server.LibUserFunc function, org.radixware.ads.UserFunc.common.ImpExpXsd.LibUserFunc xmlUserFunc, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		function.name = xmlUserFunc.Name;
		function.upUserFunc.importThis(function, idof[LibUserFunc:upUserFunc], true, xmlUserFunc.UserFunc, helper);
		function.profile = function.upUserFunc.displayProfile;
		function.upUserFunc.update(); //forced update inner user func to create changelog before revisions
		function.description = xmlUserFunc.Description;
		function.update();
	}

	/*Radix::UserFunc::LibUserFunc:getUsedByLibraryFunctions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getUsedByLibraryFunctions")
	  org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.UserFunc.server.UserFunc> getUsedByLibraryFunctions (boolean filterSameLibrary) {
		final Types::EntityGroup<UserFunc> userFuncGroup = (Types::EntityGroup<UserFunc>) Arte::Arte.getInstance().getGroupHander(idof[System::UserFunc]);

		final org.radixware.kernel.server.meta.clazzes.RadClassDef libUfClass
		        = Arte::Arte.getDefManager().getClassDef(idof[LibUserFunc]);
		final org.radixware.kernel.server.meta.clazzes.RadClassDef ufClass
		        = Arte::Arte.getDefManager().getClassDef(idof[UserFunc]);
		final Types::EntityGroup.TreeContext context
		        = new Types::EntityGroup.TreeContext(ufClass,
		                libUfClass.getPresentation().getEditorPresentationById(idof[LibUserFunc:Usages]).
		                findChildExplorerItemById(idof[LibUserFunc:Usages:UsageOccurences]),
		                getPid());
		userFuncGroup.set(
		        context,
		        ufClass.getPresentation().getSelectorPresentationById(idof[UserFunc:Usages]),
		        null, null, null, null);

		ArrParentRef result = new ArrParentRef(Arte::Arte.getInstance());
		for (UserFunc userFunc : userFuncGroup) {
		    if (filterSameLibrary && userFunc.ownerLibFunc != null && userFunc.ownerLibFunc.libName.equals(libName)) {
		        continue;
		    }
		    result.add(userFunc);
		}
		return result;
	}

	/*Radix::UserFunc::LibUserFunc:create-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:create")
	public static  org.radixware.ads.UserFunc.server.LibUserFunc create (Str libName, Str guid, Str classGuid) {
		LibUserFunc luf = new LibUserFunc();
		luf.init();
		luf.libName = libName;
		luf.guid = guid;
		luf.newUserFuncClassId = classGuid;
		return luf;
	}

	/*Radix::UserFunc::LibUserFunc:getCfgReferenceExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getCfgReferenceExtGuid")
	public published  Str getCfgReferenceExtGuid () {
		return guid;
	}

	/*Radix::UserFunc::LibUserFunc:getCfgReferencePropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:getCfgReferencePropId")
	public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
		return idof[LibUserFunc:guid];
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::UserFunc::LibUserFunc - Server Meta*/

/*Radix::UserFunc::LibUserFunc-Entity Class*/

package org.radixware.ads.UserFunc.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class LibUserFunc_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),"LibUserFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::UserFunc::LibUserFunc:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
							/*Owner Class Name*/
							"LibUserFunc",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),
							/*Property presentations*/

							/*Radix::UserFunc::LibUserFunc:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::UserFunc::LibUserFunc:libName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:guid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANG66BP4NZC55NQTVVF24BXAKI"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:ownerLib:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CYBJAQYWFHSXIHCIP7AIYR7UE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::UserFunc::LibUserFunc:profile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLE6DB2PKDNF3ZEYP6CEJ32LQ5A"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:description:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2Y5RGHJO7FCS7FCGIUIPTJKDOY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:name:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:usedLibraryFunctions:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOMVOK77W2JHITKSK7UEJMDFYLA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::UserFunc::LibUserFunc:ufResultType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7375XZFYGZBGXOQRLJVUM2NJ4Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:isFreeForm:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKPGMTEIGM5FGLFLPINTNWBZUBY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:upUserFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSVH5HTN7GNAR7MTQFFWAZRZ2YI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("eccZMCDFRDL4DOBDIEAAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGWQBF5FKIVGRFHCOVCDXBLS3TQ")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:isUfDefined:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBLVNIK4D7ZE6DMXAGRPU4NMRLE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:presentableDescription:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6WWXYGVTRG5ZG6RV7IRPKFABI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:newUserFuncClassId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHDT7IIQ4MRFKXFCF4NW3VDBYOU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:dummyRefOnFunction:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWGU27MQJWJAKBOINDEXTMIQDAY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJXA4VMNLWBH6DPNJHIXMNHXBHA"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(3670014,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:upUserFuncClassId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJOQLCFYHPVCJBAPPJPJQTEZ6LE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:fullProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR5NQJDMGMZCIJPONVCWH6BXKMI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:profileHtml:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6727FJZMCJFR5FYY7JFOFBQGWU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3YAZN37THRBPXECTUCJPALVOLY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::LibUserFunc:inheritedDescription:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQYKMA3PMCBBZ7IYKIUZURJT6OI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::UserFunc::LibUserFunc:Name-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDNJEZP4F4BHFZLFZAXTJEN3EJU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),"Name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKESTNV27K5DDVNG43H3C3F3UEM"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::UserFunc::LibUserFunc:LibName-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEVSMT45QRVES3NEE3VHLUYNQYU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),"LibName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6PGLQ4VQU5BONHWGE5V6RVWNWA"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::UserFunc::LibUserFunc:Name-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltLQIK54RHDND75HB2KPXPBAQBJE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),"Name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJNBJRHQ4DBEB3JNI2MOOUZOZHY"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecW7BRVVQLHBE2FCNK7ZBRMAUJGM\" PropId=\"colANG66BP4NZC55NQTVVF24BXAKI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm3IVN4DSMB5D2LDWUOEMLA4GB4I\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\'</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDNJEZP4F4BHFZLFZAXTJEN3EJU"),true,null,false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::UserFunc::LibUserFunc:LibName-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltJSIUQHZ2ZFGJZDN2EV6AYOYDPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),"LibName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPKTSC7QHCZHLLC76SX4BNUZQCY"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecW7BRVVQLHBE2FCNK7ZBRMAUJGM\" PropId=\"colPWEV3UOPXJD5NLVOWUIJNNQ4XA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmF3NJX5RQJVHALIFIQ2USHTEY4M\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\'</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEVSMT45QRVES3NEE3VHLUYNQYU"),true,null,false,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::UserFunc::LibUserFunc:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::UserFunc::LibUserFunc:General:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::UserFunc::LibUserFunc:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQV34OZIMOZEQRNJXWAAH6CZBR4"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::UserFunc::LibUserFunc:Create:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::UserFunc::LibUserFunc:Create:UserFunc-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiT5JS2JED3JDOFJDF57LKL2WJPM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJXA4VMNLWBH6DPNJHIXMNHXBHA"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::UserFunc::LibUserFunc:Usages-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr74SXZSVAHBF4NEXED3ZRFZ7XFU"),
									"Usages",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::UserFunc::LibUserFunc:Usages:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::UserFunc::LibUserFunc:Usages:UsageOccurences-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiOETFCOWDWZEMRP33J5N2KVK22U"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHMDW6GTMKJC47FL5GNCYGTMKQU"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>dbms_lob.instr(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colS3ULKYQUEZAYBJR55WPBI77FQE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, to_clob(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecW7BRVVQLHBE2FCNK7ZBRMAUJGM\" PropId=\"colANG66BP4NZC55NQTVVF24BXAKI\" Owner=\"PARENT\"/></xsc:Item><xsc:Item><xsc:Sql>)) > 0\n\n</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::UserFunc::LibUserFunc:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQYKMA3PMCBBZ7IYKIUZURJT6OI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6727FJZMCJFR5FYY7JFOFBQGWU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6WWXYGVTRG5ZG6RV7IRPKFABI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3YAZN37THRBPXECTUCJPALVOLY"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDNJEZP4F4BHFZLFZAXTJEN3EJU"),true,null,false,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQV34OZIMOZEQRNJXWAAH6CZBR4")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::UserFunc::LibUserFunc:Filtered-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2Z3PQIOYMBE2FM2DX74KBB7D7A"),"Filtered",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE"),16561,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQYKMA3PMCBBZ7IYKIUZURJT6OI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7375XZFYGZBGXOQRLJVUM2NJ4Y"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLE6DB2PKDNF3ZEYP6CEJ32LQ5A"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6WWXYGVTRG5ZG6RV7IRPKFABI"),true)
									},null,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEVSMT45QRVES3NEE3VHLUYNQYU"),true,null,false,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQV34OZIMOZEQRNJXWAAH6CZBR4")},null,null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::UserFunc::LibUserFunc:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR5NQJDMGMZCIJPONVCWH6BXKMI"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::UserFunc::LibUserFunc:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::UserFunc::LibUserFunc:libName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA"),"libName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FSVEJ5XW5BOXCOS67BBZJQW5U"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:guid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANG66BP4NZC55NQTVVF24BXAKI"),"guid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMLH6FE5SJCVHBBU5S4PEPMV7I"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:ownerLib-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CYBJAQYWFHSXIHCIP7AIYR7UE"),"ownerLib",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6KOSNNTGVHQXPFKIUG2GQKRBM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref4BWIIPWSI5D6ZE6SQREHBZJ7VY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAM6HEX43SFCQTMG23G7JHOBCMA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:profile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLE6DB2PKDNF3ZEYP6CEJ32LQ5A"),"profile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLU5SMV324VFVRA7UP7CKFKWUDE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:description-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2Y5RGHJO7FCS7FCGIUIPTJKDOY"),"description",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:name-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),"name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXD45OLATONAVXPFBP65UADWZ5A"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:caching-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGKOT7F2RXRFIHKLGBG3LZWUBHY"),"caching",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:lastUpdateTimeGetter-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZY7IRHPQO5ETJBM6HQ4SEW3FCI"),"lastUpdateTimeGetter",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:usedLibraryFunctions-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOMVOK77W2JHITKSK7UEJMDFYLA"),"usedLibraryFunctions",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),org.radixware.kernel.common.enums.EValType.ARR_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:ufResultType-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7375XZFYGZBGXOQRLJVUM2NJ4Y"),"ufResultType",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:isFreeForm-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKPGMTEIGM5FGLFLPINTNWBZUBY"),"isFreeForm",null,org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:upUserFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSVH5HTN7GNAR7MTQFFWAZRZ2YI"),"upUserFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:isUfDefined-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBLVNIK4D7ZE6DMXAGRPU4NMRLE"),"isUfDefined",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:presentableDescription-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6WWXYGVTRG5ZG6RV7IRPKFABI"),"presentableDescription",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBF7KKVZANAQFGHRB2NUU4COYI"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:newUserFuncClassId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHDT7IIQ4MRFKXFCF4NW3VDBYOU"),"newUserFuncClassId",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:dummyRefOnFunction-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWGU27MQJWJAKBOINDEXTMIQDAY"),"dummyRefOnFunction",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:upUserFuncClassId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJOQLCFYHPVCJBAPPJPJQTEZ6LE"),"upUserFuncClassId",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:fullProfile-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR5NQJDMGMZCIJPONVCWH6BXKMI"),"fullProfile",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:profileHtml-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6727FJZMCJFR5FYY7JFOFBQGWU"),"profileHtml",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTBT4L5P5N5GZNJBRA5DQBKODSE"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3YAZN37THRBPXECTUCJPALVOLY"),"userFuncChangeLogTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYHA74WS4BHVBIPMDUOYZ5Z6KE"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::LibUserFunc:inheritedDescription-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQYKMA3PMCBBZ7IYKIUZURJT6OI"),"inheritedDescription",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::UserFunc::LibUserFunc:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKVKCZHA5AJDMTOD4JQTDKEG4IE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDBARDSEJNVGBBNM267JRNHOJ2U"),"LibUserFunc",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEO3K5KGP7NHB7MCAJFV7Z56NYA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIRM234H7EZAJTFQ33OIMIFWGLQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5SXLSY4MVZA4XLUAXRYWXLJU6Y"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthX6667G7WGJEQ3KO7W2ECWPV6AA"),"LibUserFunc",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJIKGEDUJAZBEPDQ2H4JKCUYABU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGP557SALWFDR5PZWJXQMUJN6FM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5UXKE6ODFZEDHCQ4CARSVINZXA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCUEDISLA45DWHCWJDMM47OT2CQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBNDGL57CSRBY5FYVTXXEQ52CSA"),"export",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJJ43HKUC4BCBLBR7MA3EDURIUU"),"importNew",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBIGDPRP4OFDIVD3R6Y3DGGY2JY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xmlUserFunc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5WRJWDEJBNEXJCS56PWI2NBT54"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPRYOJ3RCH5ELZEJPBW2YH7RU5I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAZ2HAURWXVB53HEDENC67YLQAU"),"afterUpdatePropObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSQSSKLTHXVBZTF3KOQGLKJQRKM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVal",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAHMDYL5KN5CXTPLDKFDQVAQ6KU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHDPZZLJBHNDYVAV7WX2NGVUUMU"),"getLibParamStr",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSICQUAQ4JBHZHDI2E5HD5VXEXE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD3V3QP2TAVBC3CKA2GM3CJSWRE"),"getLibParamInt",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM7PCEXZFKREU3KJGOMK6YETQKA"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYRY2GXWJ4VA6NO7GSTVJ34ZZ7M"),"getLibParamNum",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVIHIO6BNPVBKLJPDXNJVIHUS3A"))
								},org.radixware.kernel.common.enums.EValType.NUM),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIZCQP7P62FFSJIBN6QUCROORQE"),"getLibParamDateTime",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprASJOFU3XPVGEDJZUDPJ4IYIU24"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2M2UIQC74VHQHNBJPCUXKMT3SQ"),"getLibParamChar",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK7WKPZGY2FCB3E3AHJLIQSMAZ4"))
								},org.radixware.kernel.common.enums.EValType.CHAR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthE4OQGAKQU5DQDHOP7EVD7N4TEA"),"getLibParamBool",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNBRFOX7RKRDUVB7K2XSI2ZY5YA"))
								},org.radixware.kernel.common.enums.EValType.BOOL),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGZDY76GQDVEENNFB33JRO5ADCU"),"beforeInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("initPropValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6FCWV3WOZNAMRCTQJJJLK2XJYY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJODYNAXHNRFVTHJ6D2ZW4TOTNA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGCESQUUKBRDRREOAFKTYK53UN4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYTK3M6D6LBC2JHPDLUYCRKGPZE"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGBI37CJQQBCPVNEKPGKP54ZV3Y"),"resetCache",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPHOTGU3LBZFKDNAT4KRH2VSAZM"),"call",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprENKQC7FSCRB63P35FCGBOS6AU4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth56DIPEYPZZHK3KEUJ3LAIOCDKY"),"call",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKV2YCK5DDZHI3HARN7Y4YUPGZA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ5SKDTYDVZDCPEXA3W3A7QLMHM"),"getXml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libUserFuncId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprITEUJMESL5DGRGWZIYEZXAWDXM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGYAAVWCRQNGKLNFHIRS6KNWLZQ"),"getXml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libUserFuncId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA2AKWTFAXZDH3ODRJFDBMUF6O4"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWN7RL5KLCJBJ3MP6XKQP5BEKDQ"),"getXml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libFunc",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7JD3SQFDFFDQJBVJ5QPWYP3S2I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSVIWNJFQPJDBRPCBDQMRKJ2QQU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6Z3BTEYPKFBNLMEHJDX2IQT5NY"),"calcProfileHtml",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRO6PYBIUIBBSLKTJBKVPVXUGOQ"),"importOne",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ5NVQNBIQFBRNDPR47VOMJTDMM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ3YEPNEEYNG2RH3NQZYPGQVYSU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDUFF4ULL6FDEPE5ELEYS4HMZO4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthETI55FF54RGOVMBQOCCPZSK2SM"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ3YEPNEEYNG2RH3NQZYPGQVYSU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDUFF4ULL6FDEPE5ELEYS4HMZO4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGHDBDOWGUBERDKVVQF3RU5WSPY"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYNLSO3DJM5BNXFLKWZS2Z3FK7E"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthE3ESZLXOSZENPPEQKIIN27OMFQ"),"createFreeForm",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr62DSXSQFU5A2DHHLEX6PYAPMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI2J33P7K4ZBSRLNUIFSDTFVQYU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPUPVPMEKABHE3PYQOX6F7LJ52Y"),"importAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr24HWCV2TX5AGNEZELETN4TC46Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTXBXWMXPJFAHJMR6TDULEDBLMU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSEZJO56KHNFNRAB5SZ36ONL7K4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIWB3FCCY4BC7BIR3X64Y5FIF2A"),"replaceFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCA4BVSUPUBG7VNKMFMIMEDRHVU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFRDH4HWTANFULECBHZ35EPI7W4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthE5QSF5M7YZGHRERMHC7NDDJI6E"),"importData",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("function",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUYBAZ7Y67ZFSTL7SERLIAEH5LM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xmlUserFunc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr63QSRJHDTJAEXEGJROHUKBJDWI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprELJNL7O3LFE65PQIQKHW5C6I3Q"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKRFZLP3MI5EBZGYCAB6Z5XTERQ"),"getUsedByLibraryFunctions",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("filterSameLibrary",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3QJ23BE35VEMNLZT3Y5UW3TKZU"))
								},org.radixware.kernel.common.enums.EValType.ARR_REF),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6X2TROPAJNER5K3HNHPZSA554U"),"create",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr62DSXSQFU5A2DHHLEX6PYAPMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI2J33P7K4ZBSRLNUIFSDTFVQYU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU2LZVWZUPZA7PMPLUXD6ZRAOFU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::UserFunc::LibUserFunc - Desktop Executable*/

/*Radix::UserFunc::LibUserFunc-Entity Class*/

package org.radixware.ads.UserFunc.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc")
public interface LibUserFunc {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::UserFunc::LibUserFuncGroup:allowedLibNames:allowedLibNames-Presentation Property*/




		public class AllowedLibNames extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
			public AllowedLibNames(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:allowedLibNames:allowedLibNames")
			public  org.radixware.kernel.common.types.ArrStr getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:allowedLibNames:allowedLibNames")
			public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
				Value = val;
			}
		}
		public AllowedLibNames getAllowedLibNames(){return (AllowedLibNames)getProperty(pgpVSECH6NEYVDUJENE5QYPQJ5VZY);}

		/*Radix::UserFunc::LibUserFuncGroup:xReturnType:xReturnType-Presentation Property*/




		public class XReturnType extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
			public XReturnType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Class<org.radixware.schemas.xscml.TypeDeclarationDocument> getValClass(){
				return org.radixware.schemas.xscml.TypeDeclarationDocument.class;
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				org.radixware.schemas.xscml.TypeDeclarationDocument dummy = x == null ? null : (org.radixware.schemas.xscml.TypeDeclarationDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.xscml.TypeDeclarationDocument.class);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:xReturnType:xReturnType")
			public  org.radixware.schemas.xscml.TypeDeclarationDocument getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:xReturnType:xReturnType")
			public   void setValue(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
				Value = val;
			}
		}
		public XReturnType getXReturnType(){return (XReturnType)getProperty(pgpHITCWSTPHVAQ5BAZANDX5U7K2A);}

		/*Radix::UserFunc::LibUserFuncGroup:libFuncInEditorGuid:libFuncInEditorGuid-Presentation Property*/




		public class LibFuncInEditorGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public LibFuncInEditorGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Str dummy = ((Str)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:libFuncInEditorGuid:libFuncInEditorGuid")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:libFuncInEditorGuid:libFuncInEditorGuid")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public LibFuncInEditorGuid getLibFuncInEditorGuid(){return (LibFuncInEditorGuid)getProperty(pgpY2I2NSMUOVERXPWPT2D5QVH5MU);}











		public org.radixware.ads.UserFunc.explorer.LibUserFunc.LibUserFunc_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.UserFunc.explorer.LibUserFunc.LibUserFunc_DefaultModel )  super.getEntity(i);}
	}





















































































































	/*Radix::UserFunc::LibUserFunc:description:description-Presentation Property*/


	public class Description extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Description(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::UserFunc::LibUserFunc:name:name-Presentation Property*/


	public class Name extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Name(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::UserFunc::LibUserFunc:ownerLib:ownerLib-Presentation Property*/


	public class OwnerLib extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public OwnerLib(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFuncLib.UserFuncLib_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFuncLib.UserFuncLib_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFuncLib.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.UserFunc.explorer.UserFuncLib.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ownerLib:ownerLib")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ownerLib:ownerLib")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public OwnerLib getOwnerLib();
	/*Radix::UserFunc::LibUserFunc:guid:guid-Presentation Property*/


	public class Guid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Guid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::UserFunc::LibUserFunc:profile:profile-Presentation Property*/


	public class Profile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Profile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profile:profile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profile:profile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Profile getProfile();
	/*Radix::UserFunc::LibUserFunc:libName:libName-Presentation Property*/


	public class LibName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LibName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:libName:libName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:libName:libName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LibName getLibName();
	/*Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:userFuncChangeLogTitle-Presentation Property*/


	public class UserFuncChangeLogTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UserFuncChangeLogTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:userFuncChangeLogTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:userFuncChangeLogTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UserFuncChangeLogTitle getUserFuncChangeLogTitle();
	/*Radix::UserFunc::LibUserFunc:profileHtml:profileHtml-Presentation Property*/


	public class ProfileHtml extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ProfileHtml(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profileHtml:profileHtml")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profileHtml:profileHtml")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ProfileHtml getProfileHtml();
	/*Radix::UserFunc::LibUserFunc:ufResultType:ufResultType-Presentation Property*/


	public class UfResultType extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public UfResultType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.xscml.TypeDeclarationDocument> getValClass(){
			return org.radixware.schemas.xscml.TypeDeclarationDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.xscml.TypeDeclarationDocument dummy = x == null ? null : (org.radixware.schemas.xscml.TypeDeclarationDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.xscml.TypeDeclarationDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ufResultType:ufResultType")
		public  org.radixware.schemas.xscml.TypeDeclarationDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ufResultType:ufResultType")
		public   void setValue(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
			Value = val;
		}
	}
	public UfResultType getUfResultType();
	/*Radix::UserFunc::LibUserFunc:isUfDefined:isUfDefined-Presentation Property*/


	public class IsUfDefined extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsUfDefined(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isUfDefined:isUfDefined")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isUfDefined:isUfDefined")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsUfDefined getIsUfDefined();
	/*Radix::UserFunc::LibUserFunc:newUserFuncClassId:newUserFuncClassId-Presentation Property*/


	public class NewUserFuncClassId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public NewUserFuncClassId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:newUserFuncClassId:newUserFuncClassId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:newUserFuncClassId:newUserFuncClassId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public NewUserFuncClassId getNewUserFuncClassId();
	/*Radix::UserFunc::LibUserFunc:upUserFuncClassId:upUserFuncClassId-Presentation Property*/


	public class UpUserFuncClassId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UpUserFuncClassId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFuncClassId:upUserFuncClassId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFuncClassId:upUserFuncClassId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UpUserFuncClassId getUpUserFuncClassId();
	/*Radix::UserFunc::LibUserFunc:isFreeForm:isFreeForm-Presentation Property*/


	public class IsFreeForm extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsFreeForm(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isFreeForm:isFreeForm")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isFreeForm:isFreeForm")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsFreeForm getIsFreeForm();
	/*Radix::UserFunc::LibUserFunc:usedLibraryFunctions:usedLibraryFunctions-Presentation Property*/


	public class UsedLibraryFunctions extends org.radixware.kernel.common.client.models.items.properties.PropertyArrRef{
		public UsedLibraryFunctions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.ArrRef dummy = ((org.radixware.kernel.common.client.types.ArrRef)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:usedLibraryFunctions:usedLibraryFunctions")
		public  org.radixware.kernel.common.client.types.ArrRef getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:usedLibraryFunctions:usedLibraryFunctions")
		public   void setValue(org.radixware.kernel.common.client.types.ArrRef val) {
			Value = val;
		}
	}
	public UsedLibraryFunctions getUsedLibraryFunctions();
	/*Radix::UserFunc::LibUserFunc:inheritedDescription:inheritedDescription-Presentation Property*/


	public class InheritedDescription extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public InheritedDescription(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:inheritedDescription:inheritedDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:inheritedDescription:inheritedDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InheritedDescription getInheritedDescription();
	/*Radix::UserFunc::LibUserFunc:fullProfile:fullProfile-Presentation Property*/


	public class FullProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FullProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:fullProfile:fullProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:fullProfile:fullProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FullProfile getFullProfile();
	/*Radix::UserFunc::LibUserFunc:dummyRefOnFunction:dummyRefOnFunction-Presentation Property*/


	public class DummyRefOnFunction extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public DummyRefOnFunction(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:dummyRefOnFunction:dummyRefOnFunction")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:dummyRefOnFunction:dummyRefOnFunction")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public DummyRefOnFunction getDummyRefOnFunction();
	/*Radix::UserFunc::LibUserFunc:presentableDescription:presentableDescription-Presentation Property*/


	public class PresentableDescription extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PresentableDescription(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:presentableDescription:presentableDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:presentableDescription:presentableDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PresentableDescription getPresentableDescription();
	/*Radix::UserFunc::LibUserFunc:upUserFunc:upUserFunc-Presentation Property*/


	public class UpUserFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public UpUserFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFunc:upUserFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFunc:upUserFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public UpUserFunc getUpUserFunc();


}

/* Radix::UserFunc::LibUserFunc - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc-Entity Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class LibUserFunc_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::UserFunc::LibUserFunc:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
			"Radix::UserFunc::LibUserFunc",
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2257BYG35CTNM7NBWSJGH6254"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),0,

			/*Radix::UserFunc::LibUserFunc:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::UserFunc::LibUserFunc:libName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA"),
						"libName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FSVEJ5XW5BOXCOS67BBZJQW5U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:libName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANG66BP4NZC55NQTVVF24BXAKI"),
						"guid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMLH6FE5SJCVHBBU5S4PEPMV7I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,300,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:ownerLib:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CYBJAQYWFHSXIHCIP7AIYR7UE"),
						"ownerLib",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6KOSNNTGVHQXPFKIUG2GQKRBM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAM6HEX43SFCQTMG23G7JHOBCMA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAM6HEX43SFCQTMG23G7JHOBCMA"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::UserFunc::LibUserFunc:profile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLE6DB2PKDNF3ZEYP6CEJ32LQ5A"),
						"profile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLU5SMV324VFVRA7UP7CKFKWUDE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:profile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2Y5RGHJO7FCS7FCGIUIPTJKDOY"),
						"description",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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

						/*Radix::UserFunc::LibUserFunc:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXD45OLATONAVXPFBP65UADWZ5A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr(org.radixware.kernel.common.client.meta.mask.validators.ValidatorsFactory.createRegExpValidator("[a-zA-Z_$][a-zA-Z0-9_$]*",false),false,300,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:usedLibraryFunctions:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOMVOK77W2JHITKSK7UEJMDFYLA"),
						"usedLibraryFunctions",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.ARR_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						21,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::UserFunc::LibUserFunc:ufResultType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7375XZFYGZBGXOQRLJVUM2NJ4Y"),
						"ufResultType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.XML,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:isFreeForm:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKPGMTEIGM5FGLFLPINTNWBZUBY"),
						"isFreeForm",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:isFreeForm:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:upUserFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSVH5HTN7GNAR7MTQFFWAZRZ2YI"),
						"upUserFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGWQBF5FKIVGRFHCOVCDXBLS3TQ")},
						null,
						0L,
						0L,false,false),

					/*Radix::UserFunc::LibUserFunc:isUfDefined:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBLVNIK4D7ZE6DMXAGRPU4NMRLE"),
						"isUfDefined",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:isUfDefined:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:presentableDescription:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6WWXYGVTRG5ZG6RV7IRPKFABI"),
						"presentableDescription",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBF7KKVZANAQFGHRB2NUU4COYI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:presentableDescription:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:newUserFuncClassId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHDT7IIQ4MRFKXFCF4NW3VDBYOU"),
						"newUserFuncClassId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:newUserFuncClassId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:dummyRefOnFunction:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWGU27MQJWJAKBOINDEXTMIQDAY"),
						"dummyRefOnFunction",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						5,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJXA4VMNLWBH6DPNJHIXMNHXBHA"),
						3670014,
						133693439,false),

					/*Radix::UserFunc::LibUserFunc:upUserFuncClassId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJOQLCFYHPVCJBAPPJPJQTEZ6LE"),
						"upUserFuncClassId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:upUserFuncClassId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:fullProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR5NQJDMGMZCIJPONVCWH6BXKMI"),
						"fullProfile",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:fullProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:profileHtml:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6727FJZMCJFR5FYY7JFOFBQGWU"),
						"profileHtml",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTBT4L5P5N5GZNJBRA5DQBKODSE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:profileHtml:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3YAZN37THRBPXECTUCJPALVOLY"),
						"userFuncChangeLogTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYHA74WS4BHVBIPMDUOYZ5Z6KE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:inheritedDescription:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQYKMA3PMCBBZ7IYKIUZURJT6OI"),
						"inheritedDescription",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:inheritedDescription:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::UserFunc::LibUserFunc:Name-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltLQIK54RHDND75HB2KPXPBAQBJE"),
						"Name",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJNBJRHQ4DBEB3JNI2MOOUZOZHY"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecW7BRVVQLHBE2FCNK7ZBRMAUJGM\" PropId=\"colANG66BP4NZC55NQTVVF24BXAKI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm3IVN4DSMB5D2LDWUOEMLA4GB4I\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\'</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDNJEZP4F4BHFZLFZAXTJEN3EJU"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm3IVN4DSMB5D2LDWUOEMLA4GB4I"),
								"name",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXY256XIXCBFDFGHR5LXSPA4IQE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::UserFunc::LibUserFunc:Name:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::UserFunc::LibUserFunc:LibName-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltJSIUQHZ2ZFGJZDN2EV6AYOYDPE"),
						"LibName",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPKTSC7QHCZHLLC76SX4BNUZQCY"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecW7BRVVQLHBE2FCNK7ZBRMAUJGM\" PropId=\"colPWEV3UOPXJD5NLVOWUIJNNQ4XA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmF3NJX5RQJVHALIFIQ2USHTEY4M\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\'</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEVSMT45QRVES3NEE3VHLUYNQYU"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmF3NJX5RQJVHALIFIQ2USHTEY4M"),
								"libraryName",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW6XBB2PRWZEQHBUPLXTIHWNLQM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::UserFunc::LibUserFunc:LibName:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::UserFunc::LibUserFunc:Name-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDNJEZP4F4BHFZLFZAXTJEN3EJU"),
						"Name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKESTNV27K5DDVNG43H3C3F3UEM"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::UserFunc::LibUserFunc:LibName-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEVSMT45QRVES3NEE3VHLUYNQYU"),
						"LibName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6PGLQ4VQU5BONHWGE5V6RVWNWA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref4BWIIPWSI5D6ZE6SQREHBZJ7VY"),"LibUserFunc=>UserFuncLib (libName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAM6HEX43SFCQTMG23G7JHOBCMA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA")},new String[]{"libName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colLSUHVNJK2FEMPFDMOHXBTSONXA")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQV34OZIMOZEQRNJXWAAH6CZBR4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr74SXZSVAHBF4NEXED3ZRFZ7XFU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2Z3PQIOYMBE2FM2DX74KBB7D7A")},
			false,false,false);
}

/* Radix::UserFunc::LibUserFunc - Web Executable*/

/*Radix::UserFunc::LibUserFunc-Entity Class*/

package org.radixware.ads.UserFunc.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc")
public interface LibUserFunc {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::UserFunc::LibUserFuncGroup:allowedLibNames:allowedLibNames-Presentation Property*/




		public class AllowedLibNames extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
			public AllowedLibNames(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:allowedLibNames:allowedLibNames")
			public  org.radixware.kernel.common.types.ArrStr getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:allowedLibNames:allowedLibNames")
			public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
				Value = val;
			}
		}
		public AllowedLibNames getAllowedLibNames(){return (AllowedLibNames)getProperty(pgpVSECH6NEYVDUJENE5QYPQJ5VZY);}

		/*Radix::UserFunc::LibUserFuncGroup:xReturnType:xReturnType-Presentation Property*/




		public class XReturnType extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
			public XReturnType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Class<org.radixware.schemas.xscml.TypeDeclarationDocument> getValClass(){
				return org.radixware.schemas.xscml.TypeDeclarationDocument.class;
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				org.radixware.schemas.xscml.TypeDeclarationDocument dummy = x == null ? null : (org.radixware.schemas.xscml.TypeDeclarationDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.xscml.TypeDeclarationDocument.class);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:xReturnType:xReturnType")
			public  org.radixware.schemas.xscml.TypeDeclarationDocument getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:xReturnType:xReturnType")
			public   void setValue(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
				Value = val;
			}
		}
		public XReturnType getXReturnType(){return (XReturnType)getProperty(pgpHITCWSTPHVAQ5BAZANDX5U7K2A);}

		/*Radix::UserFunc::LibUserFuncGroup:libFuncInEditorGuid:libFuncInEditorGuid-Presentation Property*/




		public class LibFuncInEditorGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public LibFuncInEditorGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Str dummy = ((Str)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:libFuncInEditorGuid:libFuncInEditorGuid")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFuncGroup:libFuncInEditorGuid:libFuncInEditorGuid")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public LibFuncInEditorGuid getLibFuncInEditorGuid(){return (LibFuncInEditorGuid)getProperty(pgpY2I2NSMUOVERXPWPT2D5QVH5MU);}











		public org.radixware.ads.UserFunc.web.LibUserFunc.LibUserFunc_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.UserFunc.web.LibUserFunc.LibUserFunc_DefaultModel )  super.getEntity(i);}
	}











































































































	/*Radix::UserFunc::LibUserFunc:description:description-Presentation Property*/


	public class Description extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Description(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::UserFunc::LibUserFunc:name:name-Presentation Property*/


	public class Name extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Name(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::UserFunc::LibUserFunc:ownerLib:ownerLib-Presentation Property*/


	public class OwnerLib extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public OwnerLib(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.web.UserFuncLib.UserFuncLib_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.web.UserFuncLib.UserFuncLib_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.UserFunc.web.UserFuncLib.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.UserFunc.web.UserFuncLib.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ownerLib:ownerLib")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ownerLib:ownerLib")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public OwnerLib getOwnerLib();
	/*Radix::UserFunc::LibUserFunc:guid:guid-Presentation Property*/


	public class Guid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Guid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::UserFunc::LibUserFunc:profile:profile-Presentation Property*/


	public class Profile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Profile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profile:profile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profile:profile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Profile getProfile();
	/*Radix::UserFunc::LibUserFunc:libName:libName-Presentation Property*/


	public class LibName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LibName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:libName:libName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:libName:libName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LibName getLibName();
	/*Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:userFuncChangeLogTitle-Presentation Property*/


	public class UserFuncChangeLogTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UserFuncChangeLogTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:userFuncChangeLogTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:userFuncChangeLogTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UserFuncChangeLogTitle getUserFuncChangeLogTitle();
	/*Radix::UserFunc::LibUserFunc:profileHtml:profileHtml-Presentation Property*/


	public class ProfileHtml extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ProfileHtml(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profileHtml:profileHtml")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:profileHtml:profileHtml")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ProfileHtml getProfileHtml();
	/*Radix::UserFunc::LibUserFunc:ufResultType:ufResultType-Presentation Property*/


	public class UfResultType extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public UfResultType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.xscml.TypeDeclarationDocument> getValClass(){
			return org.radixware.schemas.xscml.TypeDeclarationDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.xscml.TypeDeclarationDocument dummy = x == null ? null : (org.radixware.schemas.xscml.TypeDeclarationDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.xscml.TypeDeclarationDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ufResultType:ufResultType")
		public  org.radixware.schemas.xscml.TypeDeclarationDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:ufResultType:ufResultType")
		public   void setValue(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
			Value = val;
		}
	}
	public UfResultType getUfResultType();
	/*Radix::UserFunc::LibUserFunc:isUfDefined:isUfDefined-Presentation Property*/


	public class IsUfDefined extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsUfDefined(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isUfDefined:isUfDefined")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isUfDefined:isUfDefined")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsUfDefined getIsUfDefined();
	/*Radix::UserFunc::LibUserFunc:newUserFuncClassId:newUserFuncClassId-Presentation Property*/


	public class NewUserFuncClassId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public NewUserFuncClassId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:newUserFuncClassId:newUserFuncClassId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:newUserFuncClassId:newUserFuncClassId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public NewUserFuncClassId getNewUserFuncClassId();
	/*Radix::UserFunc::LibUserFunc:upUserFuncClassId:upUserFuncClassId-Presentation Property*/


	public class UpUserFuncClassId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UpUserFuncClassId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFuncClassId:upUserFuncClassId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:upUserFuncClassId:upUserFuncClassId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UpUserFuncClassId getUpUserFuncClassId();
	/*Radix::UserFunc::LibUserFunc:isFreeForm:isFreeForm-Presentation Property*/


	public class IsFreeForm extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsFreeForm(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isFreeForm:isFreeForm")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:isFreeForm:isFreeForm")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsFreeForm getIsFreeForm();
	/*Radix::UserFunc::LibUserFunc:usedLibraryFunctions:usedLibraryFunctions-Presentation Property*/


	public class UsedLibraryFunctions extends org.radixware.kernel.common.client.models.items.properties.PropertyArrRef{
		public UsedLibraryFunctions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.ArrRef dummy = ((org.radixware.kernel.common.client.types.ArrRef)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:usedLibraryFunctions:usedLibraryFunctions")
		public  org.radixware.kernel.common.client.types.ArrRef getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:usedLibraryFunctions:usedLibraryFunctions")
		public   void setValue(org.radixware.kernel.common.client.types.ArrRef val) {
			Value = val;
		}
	}
	public UsedLibraryFunctions getUsedLibraryFunctions();
	/*Radix::UserFunc::LibUserFunc:inheritedDescription:inheritedDescription-Presentation Property*/


	public class InheritedDescription extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public InheritedDescription(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:inheritedDescription:inheritedDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:inheritedDescription:inheritedDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InheritedDescription getInheritedDescription();
	/*Radix::UserFunc::LibUserFunc:fullProfile:fullProfile-Presentation Property*/


	public class FullProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FullProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:fullProfile:fullProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:fullProfile:fullProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FullProfile getFullProfile();
	/*Radix::UserFunc::LibUserFunc:presentableDescription:presentableDescription-Presentation Property*/


	public class PresentableDescription extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PresentableDescription(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:presentableDescription:presentableDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:presentableDescription:presentableDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PresentableDescription getPresentableDescription();


}

/* Radix::UserFunc::LibUserFunc - Web Meta*/

/*Radix::UserFunc::LibUserFunc-Entity Class*/

package org.radixware.ads.UserFunc.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class LibUserFunc_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::UserFunc::LibUserFunc:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
			"Radix::UserFunc::LibUserFunc",
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2257BYG35CTNM7NBWSJGH6254"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),0,

			/*Radix::UserFunc::LibUserFunc:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::UserFunc::LibUserFunc:libName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA"),
						"libName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FSVEJ5XW5BOXCOS67BBZJQW5U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:libName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANG66BP4NZC55NQTVVF24BXAKI"),
						"guid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMLH6FE5SJCVHBBU5S4PEPMV7I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,300,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:ownerLib:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CYBJAQYWFHSXIHCIP7AIYR7UE"),
						"ownerLib",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6KOSNNTGVHQXPFKIUG2GQKRBM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAM6HEX43SFCQTMG23G7JHOBCMA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAM6HEX43SFCQTMG23G7JHOBCMA"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::UserFunc::LibUserFunc:profile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLE6DB2PKDNF3ZEYP6CEJ32LQ5A"),
						"profile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLU5SMV324VFVRA7UP7CKFKWUDE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:profile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2Y5RGHJO7FCS7FCGIUIPTJKDOY"),
						"description",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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

						/*Radix::UserFunc::LibUserFunc:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXD45OLATONAVXPFBP65UADWZ5A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr(org.radixware.kernel.common.client.meta.mask.validators.ValidatorsFactory.createRegExpValidator("[a-zA-Z_$][a-zA-Z0-9_$]*",false),false,300,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:usedLibraryFunctions:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOMVOK77W2JHITKSK7UEJMDFYLA"),
						"usedLibraryFunctions",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.ARR_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						21,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::UserFunc::LibUserFunc:ufResultType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7375XZFYGZBGXOQRLJVUM2NJ4Y"),
						"ufResultType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.XML,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:isFreeForm:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKPGMTEIGM5FGLFLPINTNWBZUBY"),
						"isFreeForm",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:isFreeForm:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:upUserFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSVH5HTN7GNAR7MTQFFWAZRZ2YI"),
						"upUserFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false),

					/*Radix::UserFunc::LibUserFunc:isUfDefined:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBLVNIK4D7ZE6DMXAGRPU4NMRLE"),
						"isUfDefined",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:isUfDefined:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:presentableDescription:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6WWXYGVTRG5ZG6RV7IRPKFABI"),
						"presentableDescription",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBF7KKVZANAQFGHRB2NUU4COYI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:presentableDescription:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:newUserFuncClassId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHDT7IIQ4MRFKXFCF4NW3VDBYOU"),
						"newUserFuncClassId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:newUserFuncClassId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:dummyRefOnFunction:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWGU27MQJWJAKBOINDEXTMIQDAY"),
						"dummyRefOnFunction",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						5,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						3670014,
						133693439,false),

					/*Radix::UserFunc::LibUserFunc:upUserFuncClassId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJOQLCFYHPVCJBAPPJPJQTEZ6LE"),
						"upUserFuncClassId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:upUserFuncClassId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:fullProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR5NQJDMGMZCIJPONVCWH6BXKMI"),
						"fullProfile",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:fullProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:profileHtml:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6727FJZMCJFR5FYY7JFOFBQGWU"),
						"profileHtml",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTBT4L5P5N5GZNJBRA5DQBKODSE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:profileHtml:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3YAZN37THRBPXECTUCJPALVOLY"),
						"userFuncChangeLogTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYHA74WS4BHVBIPMDUOYZ5Z6KE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::LibUserFunc:userFuncChangeLogTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::LibUserFunc:inheritedDescription:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQYKMA3PMCBBZ7IYKIUZURJT6OI"),
						"inheritedDescription",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::UserFunc::LibUserFunc:inheritedDescription:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::UserFunc::LibUserFunc:Name-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltLQIK54RHDND75HB2KPXPBAQBJE"),
						"Name",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJNBJRHQ4DBEB3JNI2MOOUZOZHY"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecW7BRVVQLHBE2FCNK7ZBRMAUJGM\" PropId=\"colANG66BP4NZC55NQTVVF24BXAKI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm3IVN4DSMB5D2LDWUOEMLA4GB4I\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\'</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDNJEZP4F4BHFZLFZAXTJEN3EJU"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm3IVN4DSMB5D2LDWUOEMLA4GB4I"),
								"name",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXY256XIXCBFDFGHR5LXSPA4IQE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::UserFunc::LibUserFunc:Name:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::UserFunc::LibUserFunc:LibName-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltJSIUQHZ2ZFGJZDN2EV6AYOYDPE"),
						"LibName",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPKTSC7QHCZHLLC76SX4BNUZQCY"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecW7BRVVQLHBE2FCNK7ZBRMAUJGM\" PropId=\"colPWEV3UOPXJD5NLVOWUIJNNQ4XA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmF3NJX5RQJVHALIFIQ2USHTEY4M\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\'</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEVSMT45QRVES3NEE3VHLUYNQYU"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmF3NJX5RQJVHALIFIQ2USHTEY4M"),
								"libraryName",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW6XBB2PRWZEQHBUPLXTIHWNLQM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::UserFunc::LibUserFunc:LibName:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::UserFunc::LibUserFunc:Name-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDNJEZP4F4BHFZLFZAXTJEN3EJU"),
						"Name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKESTNV27K5DDVNG43H3C3F3UEM"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::UserFunc::LibUserFunc:LibName-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEVSMT45QRVES3NEE3VHLUYNQYU"),
						"LibName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6PGLQ4VQU5BONHWGE5V6RVWNWA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref4BWIIPWSI5D6ZE6SQREHBZJ7VY"),"LibUserFunc=>UserFuncLib (libName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAM6HEX43SFCQTMG23G7JHOBCMA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA")},new String[]{"libName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colLSUHVNJK2FEMPFDMOHXBTSONXA")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr74SXZSVAHBF4NEXED3ZRFZ7XFU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE")},
			false,false,false);
}

/* Radix::UserFunc::LibUserFunc:General - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:General-Editor Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
	null,
	null,

	/*Radix::UserFunc::LibUserFunc:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::UserFunc::LibUserFunc:General:ufEditor-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOS6GSFFAOFBG5LXXYIU54ZS5FU"),"ufEditor",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMGDX2DRPZGTDBA4PERCVTNULU"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepJBIWON5CHNFP7IR6ZGP3QYYYJE"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOS6GSFFAOFBG5LXXYIU54ZS5FU"))}
	,

	/*Radix::UserFunc::LibUserFunc:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	32,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::UserFunc::LibUserFunc:General - Web Meta*/

/*Radix::UserFunc::LibUserFunc:General-Editor Presentation*/

package org.radixware.ads.UserFunc.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
	null,
	null,

	/*Radix::UserFunc::LibUserFunc:General:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::UserFunc::LibUserFunc:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	32,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::UserFunc::LibUserFunc:General:Model - Desktop Executable*/

/*Radix::UserFunc::LibUserFunc:General:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model")
public class General:Model  extends org.radixware.ads.UserFunc.explorer.LibUserFunc.LibUserFunc_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::UserFunc::LibUserFunc:General:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:General:Model:Properties-Properties*/

	/*Radix::UserFunc::LibUserFunc:General:Model:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:General:Model:afterChangePropertyObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:afterChangePropertyObject")
	public published  void afterChangePropertyObject (org.radixware.kernel.common.client.models.items.properties.PropertyObject property) {
		super.afterChangePropertyObject(property);
		if(property.getId().equals(idof[LibUserFunc:upUserFunc]) /*&& (property.()!=null instanceof )*/){
		   embeddedEditor_entityUpdated();
		   try{
		        UserFunc uf=(UserFunc)upUserFunc.openEntityModel();
		        if(!(upUserFunc instanceof UserFunc.FreeForm) && uf.methodId.Value!=null){
		            Types::Id methodId= Types::Id.Factory.loadFrom(uf.methodId.Value);
		            Types::Id classGuid= Types::Id.Factory.loadFrom(uf.classGuid.Value);
		            //if(methodId!=null){
		            try{
		                org.radixware.kernel.common.repository.Branch branch =getEnvironment().getDefManager().getRepository().Branch;// .().Release.Repository.getBranch();
		                org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef method = org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.findMethod(branch, classGuid,methodId);
		                if(method != null){
		                    profile.Value = method.Profile.getNameForSelector(name.Value);
		                    update();
		                }
		            }catch(Exceptions::IOException ex){            
		            }catch(Explorer.Exceptions::ModelException ex){
		            }
		        }
		    }catch(InterruptedException ex){
		    }catch(Exceptions::ServiceClientException ex){
		    }
		}

	}

	/*Radix::UserFunc::LibUserFunc:General:Model:EditorPageView_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:EditorPageView_opened")
	public  void EditorPageView_opened (com.trolltech.qt.gui.QWidget widget) {
		boolean ufIsSet = upUserFunc.Value != null;
		if (LibUserFunc:General:ufEditor:View:EditorPageView:embeddedEditor.isOpened()) {
		    LibUserFunc:General:ufEditor:View:EditorPageView:embeddedEditor.getView().setToolBarHidden(true);
		    LibUserFunc:General:ufEditor:View:EditorPageView:embeddedEditor.getView().setMenuHidden(true);
		}
		com.trolltech.qt.gui.QHBoxLayout layout = (com.trolltech.qt.gui.QHBoxLayout) LibUserFunc:General:ufEditor:View:EditorPageView.layout().findChild(com.trolltech.qt.gui.QHBoxLayout.class, "horizontalLayout");
		if (layout != null) {
		    LibUserFunc:General:ufEditor:View:EditorPageView.layout().setAlignment(layout, com.trolltech.qt.core.Qt.AlignmentFlag.AlignTop);
		}
		upUserFunc.setVisible(!ufIsSet);
		LibUserFunc:General:ufEditor:View:EditorPageView:embeddedEditor.setVisible(ufIsSet);


	}

	/*Radix::UserFunc::LibUserFunc:General:Model:embeddedEditor_entityUpdated-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:embeddedEditor_entityUpdated")
	public  void embeddedEditor_entityUpdated () {
		boolean isUfDefined = upUserFunc.Value != null;
		LibUserFunc:General:ufEditor:View:EditorPageView:embeddedEditor.setVisible(isUfDefined);
		upUserFunc.setVisible(!isUfDefined);
		try {
		    update();
		} catch (Exception e) {
		    getEnvironment().processException(e);
		}

	}

	/*Radix::UserFunc::LibUserFunc:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		// --------------------- explorer --------------------- 
		super.beforeOpenView();

		((Client.Views::IEditor) View).setCommandBarHidden(true);
		((Client.Views::IEditor) View).setToolBarHidden(true);
		((Client.Views::IEditor) View).setMenuHidden(true);


	}

	/*Radix::UserFunc::LibUserFunc:General:Model:canSafelyClean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:canSafelyClean")
	public published  boolean canSafelyClean (org.radixware.kernel.common.client.models.CleanModelController controller) {
		if (!super.canSafelyClean(controller)) {
		    return false;
		}
		if (LibUserFunc:General:ufEditor:View:EditorPageView:embeddedEditor != null) {
		    return LibUserFunc:General:ufEditor:View:EditorPageView:embeddedEditor.getModel().canSafelyClean(controller);
		}
		return true;
	}

	/*Radix::UserFunc::LibUserFunc:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		presentableDescription.getEditMask().setNoValueStr(inheritedDescription.Value);
	}










}

/* Radix::UserFunc::LibUserFunc:General:Model - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:General:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemCZX6P4BJUBBZRDPUQ3P74DQJXE"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc:General:Model - Web Executable*/

/*Radix::UserFunc::LibUserFunc:General:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model")
public class General:Model  extends org.radixware.ads.UserFunc.web.LibUserFunc.LibUserFunc_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::UserFunc::LibUserFunc:General:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:General:Model:Properties-Properties*/

	/*Radix::UserFunc::LibUserFunc:General:Model:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:General:Model:afterChangePropertyObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:afterChangePropertyObject")
	public published  void afterChangePropertyObject (org.radixware.kernel.common.client.models.items.properties.PropertyObject property) {
		super.afterChangePropertyObject(property);
	}

	/*Radix::UserFunc::LibUserFunc:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		// --------------------- explorer --------------------- 
		super.beforeOpenView();

		((Client.Views::IEditor) View).setCommandBarHidden(true);
		((Client.Views::IEditor) View).setToolBarHidden(true);
		((Client.Views::IEditor) View).setMenuHidden(true);


	}

	/*Radix::UserFunc::LibUserFunc:General:Model:canSafelyClean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:canSafelyClean")
	public published  boolean canSafelyClean (org.radixware.kernel.common.client.models.CleanModelController controller) {
		return super.canSafelyClean(controller);
	}

	/*Radix::UserFunc::LibUserFunc:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		presentableDescription.getEditMask().setNoValueStr(inheritedDescription.Value);
	}










}

/* Radix::UserFunc::LibUserFunc:General:Model - Web Meta*/

/*Radix::UserFunc::LibUserFunc:General:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemCZX6P4BJUBBZRDPUQ3P74DQJXE"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc:General:ufEditor:View - Desktop Executable*/

/*Radix::UserFunc::LibUserFunc:General:ufEditor:View-Custom Page Editor for Desktop*/

package org.radixware.ads.UserFunc.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:ufEditor:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View EditorPageView;
	public View getEditorPageView(){ return EditorPageView;}
	public org.radixware.kernel.explorer.widgets.PropLabel upUserFuncPropTitle;
	public org.radixware.kernel.explorer.widgets.PropLabel getUpUserFuncPropTitle(){ return upUserFuncPropTitle;}
	public org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor upUserFuncPropEditor;
	public org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor getUpUserFuncPropEditor(){ return upUserFuncPropEditor;}
	public org.radixware.kernel.explorer.widgets.EmbeddedEditor embeddedEditor;
	public org.radixware.kernel.explorer.widgets.EmbeddedEditor getEmbeddedEditor(){ return embeddedEditor;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		EditorPageView = this;
		EditorPageView.setObjectName("EditorPageView");
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 689, 299));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QVBoxLayout $layout1 = new com.trolltech.qt.gui.QVBoxLayout(EditorPageView);
		$layout1.setObjectName("verticalLayout");
		$layout1.setContentsMargins(0, 0, 0, 0);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setSpacing(6);
		final com.trolltech.qt.gui.QHBoxLayout $layout2 = new com.trolltech.qt.gui.QHBoxLayout();
		$layout2.setObjectName("horizontalLayout");
		$layout2.setContentsMargins(0, 0, 0, 0);
		$layout2.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout2.setSpacing(6);
		upUserFuncPropTitle = new org.radixware.kernel.explorer.widgets.PropLabel(EditorPageView);
		upUserFuncPropTitle.setGeometry(new com.trolltech.qt.core.QRect(167, 31, 120, 20));
		upUserFuncPropTitle.setObjectName("upUserFuncPropTitle");
		upUserFuncPropTitle.setSizePolicy(new com.trolltech.qt.gui.QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Maximum, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred));
		upUserFuncPropTitle.setProperty(model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSVH5HTN7GNAR7MTQFFWAZRZ2YI")));
		$layout2.addWidget(upUserFuncPropTitle);
		upUserFuncPropEditor = (org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor)model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSVH5HTN7GNAR7MTQFFWAZRZ2YI")).createPropertyEditor();
		upUserFuncPropEditor.setParent(EditorPageView);
		upUserFuncPropEditor.bind();
		upUserFuncPropEditor.setGeometry(new com.trolltech.qt.core.QRect(607, 33, 100, 25));
		upUserFuncPropEditor.setObjectName("upUserFuncPropEditor");
		upUserFuncPropEditor.setSizePolicy(new com.trolltech.qt.gui.QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred));
		$layout2.addWidget(upUserFuncPropEditor);
		$layout1.addLayout($layout2);
		embeddedEditor = new org.radixware.kernel.explorer.widgets.EmbeddedEditor(model.getEnvironment(),(org.radixware.kernel.common.client.models.items.properties.PropertyReference)getModel().getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSVH5HTN7GNAR7MTQFFWAZRZ2YI")));
		embeddedEditor.setParent(EditorPageView);
		embeddedEditor.setGeometry(new com.trolltech.qt.core.QRect(147, 34, 200, 200));
		embeddedEditor.setObjectName("embeddedEditor");
		embeddedEditor.setFont(DEFAULT_FONT);
		embeddedEditor.bind();
		embeddedEditor.entityUpdated.connect(model, "mth2SUBHYEXZRDOLJTEW4UZOTVJCI()");
		$layout1.addWidget(embeddedEditor);
		EditorPageView.opened.connect(model, "mthBDTBGZFFX5G63DPJ4GTH3LGLWY(com.trolltech.qt.gui.QWidget)");
		opened.emit(this);
	}
	public final org.radixware.ads.UserFunc.explorer.General:Model getModel() {
		return (org.radixware.ads.UserFunc.explorer.General:Model) super.getModel();
	}

}

/* Radix::UserFunc::LibUserFunc:Create - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:Create-Editor Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQV34OZIMOZEQRNJXWAAH6CZBR4"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
	null,
	null,

	/*Radix::UserFunc::LibUserFunc:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::UserFunc::LibUserFunc:Create:Initialization-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGCFZQTDZFVHADFIRPCFN4TPENA"),"Initialization",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsONSFLFTKRRFBPEKJPYRVOIHP5A"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGCFZQTDZFVHADFIRPCFN4TPENA"))}
	,

	/*Radix::UserFunc::LibUserFunc:Create:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::UserFunc::LibUserFunc:Create:UserFunc-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiT5JS2JED3JDOFJDF57LKL2WJPM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJXA4VMNLWBH6DPNJHIXMNHXBHA"),
					0,
					null,
					16560,false)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::UserFunc::LibUserFunc:Create:Model - Desktop Executable*/

/*Radix::UserFunc::LibUserFunc:Create:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Create:Model")
public class Create:Model  extends org.radixware.ads.UserFunc.explorer.LibUserFunc.LibUserFunc_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::UserFunc::LibUserFunc:Create:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:Create:Model:Properties-Properties*/

	/*Radix::UserFunc::LibUserFunc:Create:Model:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:Create:Model:afterPrepareCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Create:Model:afterPrepareCreate")
	protected published  boolean afterPrepareCreate () {
		try {
		    if (getSrcPid() != null) {// Copy object created
		        newUserFuncClassId.Value = upUserFuncClassId.Value;
		        return super.afterPrepareCreate();
		    }

		    Explorer.Models::GroupModel dummyGroup = this.dummyRefOnFunction.openGroupModel();//  () this.();

		//((), , );
		// context = new (dummyGroup);
		    final Types::Id entityId = idof[System::UserFunc];
		    final java.util.List<Client.Types::InstantiatableClass> classes = org.radixware.kernel.common.client.types.InstantiatableClasses.getClasses(getEnvironment(), entityId, (dummyGroup.getContext()).toXml());

		    if (classes.size() == 1) {

		        final Client.Types::InstantiatableClass cl = classes.get(0);
		        newUserFuncClassId.Value = cl.getId().toString();

		    } else if (!classes.isEmpty()) {        
		        final Client.Types::InstantiatableClass cl = 
		            org.radixware.kernel.common.client.types.InstantiatableClasses.selectClass(getEnvironment(), 
		                                                                                       getEnvironment().getMainWindow(), 
		                                                                                       classes, 
		                                                                                       getDefinition().getId().toString(),
		                                                                                       dummyGroup.getSelectorPresentationDef().autoSortInstantiatableClasses());
		        if (cl == null) {

		            return false;
		        }
		        newUserFuncClassId.Value = cl.getId().toString();
		    } else {
		        newUserFuncClassId.Value = idof[UserFunc.FreeForm].toString();
		    }

		} catch (Exceptions::InterruptedException ex) {
		    ex.printStackTrace();
		} catch (Exceptions::ServiceClientException ex) {
		//ignore. create regular library function
		    ex.printStackTrace();
		}

		return super.afterPrepareCreate();
	}

	/*Radix::UserFunc::LibUserFunc:Create:Model:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Create:Model:afterCreate")
	protected published  void afterCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		super.afterCreate();
		//System.out.println(1);
		//
		//try {
		//     dummyGroup = () this.();
		//
		////((), , );
		//// context = new (dummyGroup);
		//    System.out.println(2);
		//
		//    System.out.println(3);
		//    final  entityId = ;
		//    final java.util.List<> classes = org.radixware.kernel.common.client.types.InstantiatableClasses.getClasses(getEnvironment(), entityId, (dummyGroup.()).toXml());
		//    System.out.println(4);
		//    if (classes.size() == 1) {
		//        System.out.println(5);
		//        final  cl = classes.get(0);
		//        .Value = cl.getId().();
		//        System.out.println(6);
		//    } else if (!classes.isEmpty()) {
		//        System.out.println(7);
		//        final  cl = org.radixware.kernel.common.client.types.InstantiatableClasses.selectClass(getEnvironment(), getEnvironment().getMainWindow(), classes);
		//        if (cl == null) {
		//            System.out.println(8);
		//            return;
		//        }
		//        .Value = cl.getId().();
		//    } else {
		//        .Value = .();
		//    }
		//
		//} catch ( ex) {
		//    ex.();
		//} catch ( ex) {
		////ignore. create regular library function
		//    ex.();
		//}
		//
		//System.out.println(9);
		//

	}










}

/* Radix::UserFunc::LibUserFunc:Create:Model - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:Create:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemQV34OZIMOZEQRNJXWAAH6CZBR4"),
						"Create:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:Create:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc:Usages - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:Usages-Editor Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class Usages_mi{
	private static final class Usages_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Usages_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr74SXZSVAHBF4NEXED3ZRFZ7XFU"),
			"Usages",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
			null,
			null,

			/*Radix::UserFunc::LibUserFunc:Usages:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::UserFunc::LibUserFunc:Usages:UsagesList-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7636AFPNRJCUZHKTIBA3FRVM2I"),"UsagesList",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiOETFCOWDWZEMRP33J5N2KVK22U"))
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7636AFPNRJCUZHKTIBA3FRVM2I"))}
			,

			/*Radix::UserFunc::LibUserFunc:Usages:Children-Explorer Items*/
				new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

						/*Radix::UserFunc::LibUserFunc:Usages:UsageOccurences-Entity Explorer Item*/

						new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiOETFCOWDWZEMRP33J5N2KVK22U"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDEU5R4ZYKFCH5DZCFTWY7Z2ZEA"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHMDW6GTMKJC47FL5GNCYGTMKQU"),
							38,
							null,
							16432,false)
				}
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.UserFunc.explorer.LibUserFunc.LibUserFunc_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Usages_DEF(); 
;
}
/* Radix::UserFunc::LibUserFunc:Usages - Web Meta*/

/*Radix::UserFunc::LibUserFunc:Usages-Editor Presentation*/

package org.radixware.ads.UserFunc.web;
public final class Usages_mi{
	private static final class Usages_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Usages_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr74SXZSVAHBF4NEXED3ZRFZ7XFU"),
			"Usages",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
			null,
			null,

			/*Radix::UserFunc::LibUserFunc:Usages:Editor Pages-Editor Presentation Pages*/
			null,
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
			}
			,

			/*Radix::UserFunc::LibUserFunc:Usages:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.UserFunc.web.LibUserFunc.LibUserFunc_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Usages_DEF(); 
;
}
/* Radix::UserFunc::LibUserFunc:General - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:General-Selector Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDNJEZP4F4BHFZLFZAXTJEN3EJU"),
		null,
		false,
		true,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQV34OZIMOZEQRNJXWAAH6CZBR4")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQYKMA3PMCBBZ7IYKIUZURJT6OI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6727FJZMCJFR5FYY7JFOFBQGWU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6WWXYGVTRG5ZG6RV7IRPKFABI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3YAZN37THRBPXECTUCJPALVOLY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::UserFunc::LibUserFunc:General - Web Meta*/

/*Radix::UserFunc::LibUserFunc:General-Selector Presentation*/

package org.radixware.ads.UserFunc.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDNJEZP4F4BHFZLFZAXTJEN3EJU"),
		null,
		false,
		true,
		null,
		32,
		null,
		144,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQYKMA3PMCBBZ7IYKIUZURJT6OI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6727FJZMCJFR5FYY7JFOFBQGWU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6WWXYGVTRG5ZG6RV7IRPKFABI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3YAZN37THRBPXECTUCJPALVOLY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::UserFunc::LibUserFunc:General:Model - Desktop Executable*/

/*Radix::UserFunc::LibUserFunc:General:Model-Group Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model")
public class General:Model  extends org.radixware.ads.UserFunc.explorer.LibUserFunc.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	@Override
	@SuppressWarnings("deprecation")
	public org.radixware.kernel.common.client.views.IView createView() { return new 
	org.radixware.ads.UserFunc.explorer.View(getEnvironment());}
	@Override
	public org.radixware.kernel.common.types.Id getCustomViewId() {return org.radixware.kernel.common.types.Id.Factory.loadFrom("cesWXA5LXLZCRC4RFVEXUS5UTDGPE");}


	/*Radix::UserFunc::LibUserFunc:General:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate-Desktop Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate")
	public static final class ProfileSelectorItemDelegate  extends org.radixware.kernel.explorer.widgets.selector.SelectorWidgetItemDelegate  {



		/*Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:Nested classes-Nested Classes*/

		/*Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:Properties-Properties*/

		/*Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:profileDoc-Dynamic Property*/



		protected com.trolltech.qt.gui.QTextDocument profileDoc=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:profileDoc")
		private final  com.trolltech.qt.gui.QTextDocument getProfileDoc() {
			return profileDoc;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:profileDoc")
		private final   void setProfileDoc(com.trolltech.qt.gui.QTextDocument val) {
			profileDoc = val;
		}

		/*Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:Methods-Methods*/

		/*Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:paint-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:paint")
		public published  void paint (com.trolltech.qt.gui.QPainter painter, com.trolltech.qt.gui.QStyleOptionViewItem option, com.trolltech.qt.core.QModelIndex index) {
			if (isProfileColumn(index.data(com.trolltech.qt.core.Qt.ItemDataRole.UserRole))) {
			    painter.save();
			    try {
			        profileDoc.setDefaultFont(option.font());

			        profileDoc.setHtml((String) index.data(com.trolltech.qt.core.Qt.ItemDataRole.DisplayRole));

			        drawBackground(painter, option, index);
			        drawFocusFrame(painter, option, index);
			        
			        painter.setClipRect(option.rect());
			        painter.translate(option.rect().topLeft());
			        profileDoc.drawContents(painter);
			    } finally {
			        painter.restore();
			    }
			} else {
			    super.paint(painter, option, index);
			}
		}

		/*Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:ProfileSelectorItemDelegate-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:ProfileSelectorItemDelegate")
		public  ProfileSelectorItemDelegate (org.radixware.kernel.explorer.widgets.selector.SelectorGrid parent) {
			super(parent);
			profileDoc = new com.trolltech.qt.gui.QTextDocument();
		}

		/*Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:createEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:createEditor")
		public published  com.trolltech.qt.gui.QWidget createEditor (com.trolltech.qt.gui.QWidget parent, com.trolltech.qt.gui.QStyleOptionViewItem option, com.trolltech.qt.core.QModelIndex index) {
			if(isProfileColumn(index.data(com.trolltech.qt.core.Qt.ItemDataRole.UserRole))) {
			    return null;
			}
			return super.createEditor(parent, option, index);
		}

		/*Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:isProfileColumn-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:ProfileSelectorItemDelegate:isProfileColumn")
		private final  boolean isProfileColumn (java.lang.Object columnData) {
			return columnData instanceof Explorer.Models.Properties::Property
			        && idof[LibUserFunc:profileHtml].equals(((Explorer.Models.Properties::Property) columnData).getId());
		}


	}

	/*Radix::UserFunc::LibUserFunc:General:Model:Properties-Properties*/

	/*Radix::UserFunc::LibUserFunc:General:Model:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:General:Model:Selector_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:Selector_opened")
	public  void Selector_opened (com.trolltech.qt.gui.QWidget widget) {
		Explorer.Views.Wraps::SelectorModel model = new SelectorGridModel(this);
		Explorer.Widgets::SelectorGrid selectorGrid = new SelectorGrid(LibUserFunc:General:View:Selector, model);

		ProfileSelectorItemDelegate delegate = new ProfileSelectorItemDelegate(selectorGrid);
		selectorGrid.setItemDelegate(delegate);

		com.trolltech.qt.gui.QVBoxLayout layout = new com.trolltech.qt.gui.QVBoxLayout();
		layout.setMargin(0);
		widget.setLayout(layout);
		widget.layout().addWidget(selectorGrid);

		getGroupView().setSelectorWidget(selectorGrid);
	}

	/*Radix::UserFunc::LibUserFunc:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:onCommand_Import")
	protected  void onCommand_Import (org.radixware.ads.UserFunc.explorer.LibUserFuncGroup.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());

		if (file == null)
		    return;

		try {
		    ImpExpXsd:LibUserFuncGroupDocument request = ImpExpXsd:LibUserFuncGroupDocument.Factory.newInstance();
		    request.addNewLibUserFuncGroup();
		    ImpExpXsd:LibUserFuncGroupDocument xLibFuncs = ImpExpXsd:LibUserFuncGroupDocument.Factory.parse(file);
		    LibUserFunc.ImportExporerDialog selectFuncsDialog = new LibUserFunc.ImportExporerDialog(Environment);
		    selectFuncsDialog.Model.setLibFuncXml(xLibFuncs.LibUserFuncGroup);
		    if (selectFuncsDialog.execDialog() == org.radixware.kernel.common.client.views.IDialog.DialogResult.ACCEPTED) {
		        ArrStr selectedFuncs = selectFuncsDialog.Model.getSelectedFuncGuids();
		        if (selectedFuncs.isEmpty()) {
		            return;
		        }
		        for (String guid : selectedFuncs) {
		            for (ImpExpXsd:LibUserFunc xFunc : xLibFuncs.LibUserFuncGroup.FunctionList) {
		                if (xFunc.Id.equals(guid)) {
		                    request.LibUserFuncGroup.addNewFunction().set(xFunc);
		                }
		            }
		        }
		    } else {
		        return;
		    }
		    
		    UserFuncLib ownerLib = (UserFuncLib) findOwnerByClass(UserFuncLib.class);
		    if (ownerLib != null) {
		        request.LibUserFuncGroup.LibName = ownerLib.name.Value;
		    } else {
		        return;
		    }

		    Common.Dlg::ClientUtils.viewImportResult(command.send(request));
		    if (getView() != null) {
		        getView().reread();
		    }
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}

	/*Radix::UserFunc::LibUserFunc:General:Model:onCommand_Export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model:onCommand_Export")
	protected  void onCommand_Export (org.radixware.ads.UserFunc.explorer.LibUserFuncGroup.Export command) {
		try {
		    Arte::TypesXsd:MapStrStrDocument xFuncList = null;
		    Client.Views::ISelector selectorView = (Client.Views::ISelector) getView();
		    Explorer.Models::GroupModel model = (Explorer.Models::GroupModel) selectorView.getModel();
		    xFuncList = Arte::TypesXsd:MapStrStrDocument.Factory.newInstance();
		    xFuncList.addNewMapStrStr();
		    if (!model.getSelection().getSelectedObjects().isEmpty()) {
		        for (Explorer.Types::Pid pid : model.getSelection().getSelectedObjects()) {
		            xFuncList.MapStrStr.addNewEntry().Key = pid.toString();
		        }
		    } else {
		        Explorer.Models::EntityModel currEntity = model.getGroupView().getCurrentEntity();
		        if (currEntity != null) {
		            xFuncList.MapStrStr.addNewEntry().Key = currEntity.getPid().toString();
		        }
		    }

		    if (xFuncList != null && !xFuncList.MapStrStr.EntryList.isEmpty()) {
		        command.send(xFuncList);
		    }
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}
	public final class Import extends org.radixware.ads.UserFunc.explorer.LibUserFuncGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

	public final class Export extends org.radixware.ads.UserFunc.explorer.LibUserFuncGroup.Export{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Export( this );
		}

	}




























}

/* Radix::UserFunc::LibUserFunc:General:Model - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:General:Model-Group Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmWXA5LXLZCRC4RFVEXUS5UTDGPE"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc:General:Model - Web Executable*/

/*Radix::UserFunc::LibUserFunc:General:Model-Group Model Class*/

package org.radixware.ads.UserFunc.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:Model")
public class General:Model  extends org.radixware.ads.UserFunc.web.LibUserFunc.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::UserFunc::LibUserFunc:General:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:General:Model:Properties-Properties*/

	/*Radix::UserFunc::LibUserFunc:General:Model:Methods-Methods*/















}

/* Radix::UserFunc::LibUserFunc:General:Model - Web Meta*/

/*Radix::UserFunc::LibUserFunc:General:Model-Group Model Class*/

package org.radixware.ads.UserFunc.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmWXA5LXLZCRC4RFVEXUS5UTDGPE"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc:General:View - Desktop Executable*/

/*Radix::UserFunc::LibUserFunc:General:View-Custom Selector for Desktop*/

package org.radixware.ads.UserFunc.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:General:View")
public class View extends org.radixware.kernel.explorer.views.selector.Selector {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View Selector;
	public View getSelector(){ return Selector;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession) {
		super(userSession);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		Selector = this;
		Selector.setObjectName("Selector");
		Selector.setFont(DEFAULT_FONT);
		Selector.opened.connect(model, "mth2XACXVL2BZCFZC7QJKGKHTOEMQ(com.trolltech.qt.gui.QWidget)");
		opened.emit(this.content);
	}
	public final org.radixware.ads.UserFunc.explorer.General:Model getModel() {
		return (org.radixware.ads.UserFunc.explorer.General:Model) super.getModel();
	}

}

/* Radix::UserFunc::LibUserFunc:Filtered - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:Filtered-Selector Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class Filtered_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Filtered_mi();
	private Filtered_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2Z3PQIOYMBE2FM2DX74KBB7D7A"),
		"Filtered",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWXA5LXLZCRC4RFVEXUS5UTDGPE"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEVSMT45QRVES3NEE3VHLUYNQYU"),
		null,
		false,
		true,
		null,
		0,null,
		16561,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQV34OZIMOZEQRNJXWAAH6CZBR4")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQYKMA3PMCBBZ7IYKIUZURJT6OI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7375XZFYGZBGXOQRLJVUM2NJ4Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPWEV3UOPXJD5NLVOWUIJNNQ4XA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA3K7ORHRIRH4TDJ6L2VIGMANGI")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLE6DB2PKDNF3ZEYP6CEJ32LQ5A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ6WWXYGVTRG5ZG6RV7IRPKFABI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::UserFunc::LibUserFunc:Filtered:Model - Desktop Executable*/

/*Radix::UserFunc::LibUserFunc:Filtered:Model-Group Model Class*/

package org.radixware.ads.UserFunc.explorer;

//import  org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Filtered:Model")
public class Filtered:Model  extends org.radixware.ads.UserFunc.explorer.General:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Filtered:Model_mi.rdxMeta; }



	public Filtered:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::UserFunc::LibUserFunc:Filtered:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:Filtered:Model:Properties-Properties*/

	/*Radix::UserFunc::LibUserFunc:Filtered:Model:returnType-Dynamic Property*/



	protected org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration returnType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Filtered:Model:returnType")
	public  org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration getReturnType() {
		return returnType;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Filtered:Model:returnType")
	public   void setReturnType(org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration val) {
		returnType = val;
	}

	/*Radix::UserFunc::LibUserFunc:Filtered:Model:allowedLibNames2PipelineId-Dynamic Property*/



	protected java.util.Map<Str,Str> allowedLibNames2PipelineId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Filtered:Model:allowedLibNames2PipelineId")
	public  java.util.Map<Str,Str> getAllowedLibNames2PipelineId() {
		return allowedLibNames2PipelineId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Filtered:Model:allowedLibNames2PipelineId")
	public   void setAllowedLibNames2PipelineId(java.util.Map<Str,Str> val) {
		allowedLibNames2PipelineId = val;
	}






	/*Radix::UserFunc::LibUserFunc:Filtered:Model:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:Filtered:Model:beforeReadFirstPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Filtered:Model:beforeReadFirstPage")
	protected published  void beforeReadFirstPage () {
		//Filter algorithm see: ()
		UserFunc ownerUserFunc = (UserFunc) findOwnerByClass(UserFunc.class);
		if (ownerUserFunc != null) {
		    allowedLibNames2PipelineId = LibUserFuncUtils.getAllowedLibNamesByPipelineId(Environment, ownerUserFunc.ownerPipelineId.Value);
		    if (allowedLibNames2PipelineId != null) {
		        ArrStr libs = new ArrStr();
		        for (String libName : allowedLibNames2PipelineId.keySet()) {
		            libs.add(libName);
		        }
		        allowedLibNames.setValueObject(libs);
		    }
		}

		if (returnType != null) {
		    Meta::XscmlXsd:TypeDeclarationDocument xRetType = Meta::XscmlXsd:TypeDeclarationDocument.Factory.newInstance();
		    returnType.appendTo(xRetType.addNewTypeDeclaration());
		    xReturnType.setValueObject(xRetType);
		}
		LibUserFunc ownerLibFunc = (LibUserFunc) findOwnerByClass(LibUserFunc.class);
		libFuncInEditorGuid.setValueObject(ownerLibFunc != null ? ownerLibFunc.guid.Value : null);

		super.beforeReadFirstPage();
	}















}

/* Radix::UserFunc::LibUserFunc:Filtered:Model - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:Filtered:Model-Group Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Filtered:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm2Z3PQIOYMBE2FM2DX74KBB7D7A"),
						"Filtered:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmWXA5LXLZCRC4RFVEXUS5UTDGPE"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:Filtered:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc:Name:Model - Desktop Executable*/

/*Radix::UserFunc::LibUserFunc:Name:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Name:Model")
public class Name:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Name:Model_mi.rdxMeta; }



	public Name:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::UserFunc::LibUserFunc:Name:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:Name:Model:Properties-Properties*/








	/*Radix::UserFunc::LibUserFunc:Name:Model:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:Name:Model:beforeApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Name:Model:beforeApply")
	public published  boolean beforeApply () {
		return super.beforeApply();
	}

	/*Radix::UserFunc::LibUserFunc:Name:name:name-Presentation Property*/




	public class Name extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Name(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Name:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Name:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName(){return (Name)getProperty(prm3IVN4DSMB5D2LDWUOEMLA4GB4I);}














}

/* Radix::UserFunc::LibUserFunc:Name:Model - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:Name:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Name:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcLQIK54RHDND75HB2KPXPBAQBJE"),
						"Name:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:Name:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc:Name:Model - Web Executable*/

/*Radix::UserFunc::LibUserFunc:Name:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Name:Model")
public class Name:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Name:Model_mi.rdxMeta; }



	public Name:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::UserFunc::LibUserFunc:Name:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:Name:Model:Properties-Properties*/








	/*Radix::UserFunc::LibUserFunc:Name:Model:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:Name:Model:beforeApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Name:Model:beforeApply")
	public published  boolean beforeApply () {
		return super.beforeApply();
	}

	/*Radix::UserFunc::LibUserFunc:Name:name:name-Presentation Property*/




	public class Name extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Name(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Name:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:Name:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName(){return (Name)getProperty(prm3IVN4DSMB5D2LDWUOEMLA4GB4I);}














}

/* Radix::UserFunc::LibUserFunc:Name:Model - Web Meta*/

/*Radix::UserFunc::LibUserFunc:Name:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Name:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcLQIK54RHDND75HB2KPXPBAQBJE"),
						"Name:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:Name:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc:LibName:Model - Desktop Executable*/

/*Radix::UserFunc::LibUserFunc:LibName:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:LibName:Model")
public class LibName:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return LibName:Model_mi.rdxMeta; }



	public LibName:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::UserFunc::LibUserFunc:LibName:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:LibName:Model:Properties-Properties*/








	/*Radix::UserFunc::LibUserFunc:LibName:Model:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:LibName:libraryName:libraryName-Presentation Property*/




	public class LibraryName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LibraryName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:LibName:libraryName:libraryName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:LibName:libraryName:libraryName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LibraryName getLibraryName(){return (LibraryName)getProperty(prmF3NJX5RQJVHALIFIQ2USHTEY4M);}














}

/* Radix::UserFunc::LibUserFunc:LibName:Model - Desktop Meta*/

/*Radix::UserFunc::LibUserFunc:LibName:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class LibName:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcJSIUQHZ2ZFGJZDN2EV6AYOYDPE"),
						"LibName:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:LibName:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc:LibName:Model - Web Executable*/

/*Radix::UserFunc::LibUserFunc:LibName:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:LibName:Model")
public class LibName:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return LibName:Model_mi.rdxMeta; }



	public LibName:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::UserFunc::LibUserFunc:LibName:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::LibUserFunc:LibName:Model:Properties-Properties*/








	/*Radix::UserFunc::LibUserFunc:LibName:Model:Methods-Methods*/

	/*Radix::UserFunc::LibUserFunc:LibName:libraryName:libraryName-Presentation Property*/




	public class LibraryName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LibraryName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:LibName:libraryName:libraryName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::LibUserFunc:LibName:libraryName:libraryName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LibraryName getLibraryName(){return (LibraryName)getProperty(prmF3NJX5RQJVHALIFIQ2USHTEY4M);}














}

/* Radix::UserFunc::LibUserFunc:LibName:Model - Web Meta*/

/*Radix::UserFunc::LibUserFunc:LibName:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class LibName:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcJSIUQHZ2ZFGJZDN2EV6AYOYDPE"),
						"LibName:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::LibUserFunc:LibName:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::UserFunc::LibUserFunc - Localizing Bundle */
package org.radixware.ads.UserFunc.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class LibUserFunc - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"returns list of library functions, used by current function");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4DXFQANVV5CQTHS3QKB2C4YWYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Function not defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция не определена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OG4I2MYTZA5FOVYQEVDJJXC2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Library for import library function is not defined. Configure Library function configuration item reference to define library.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Библиотека для импорта библиотечной функции не задана. Задайте ссылку на библиотеку в редакторе конфигурационного элемента.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls55AZCBFMJJE37L52CZVFNBFOCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By Library");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По библиотеке");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6PGLQ4VQU5BONHWGE5V6RVWNWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Library Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя библиотеки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FSVEJ5XW5BOXCOS67BBZJQW5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Library");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Библиотека");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA3K7ORHRIRH4TDJ6L2VIGMANGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Usages");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Места использования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDEU5R4ZYKFCH5DZCFTWY7Z2ZEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Library Functions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Библиотечные функции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2257BYG35CTNM7NBWSJGH6254"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Function was moved from library \'%s\' to library \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция была перенесена из библиотеки \'%s\' в библиотеку \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBGLSA5TE5GJ3KCLHARQLZXPNE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI7BADWVLARFPTAG33JBJSBLTEM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJNBJRHQ4DBEB3JNI2MOOUZOZHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По имени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKESTNV27K5DDVNG43H3C3F3UEM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLU5SMV324VFVRA7UP7CKFKWUDE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Function not defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция не определена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVXDPKPF5NEGNAZ5PJUYUJ244A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Latest Revision");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последняя ревизия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYHA74WS4BHVBIPMDUOYZ5Z6KE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Library");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Библиотека");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6KOSNNTGVHQXPFKIUG2GQKRBM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBF7KKVZANAQFGHRB2NUU4COYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Initial parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Начальные параметры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsONSFLFTKRRFBPEKJPYRVOIHP5A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invocation exception");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при выполнении");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOO33ARZY5RE23JCFY2RFMVDNRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Library Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя библиотеки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPKTSC7QHCZHLLC76SX4BNUZQCY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMGDX2DRPZGTDBA4PERCVTNULU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMLH6FE5SJCVHBBU5S4PEPMV7I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Profile of the user-defined library function with html formatting. Used in the General selector.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль библиотечной функций с html-форматированием. Используется в селекторе General.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBQONNCJHFAXBEFWDDWAHURYFE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTBT4L5P5N5GZNJBRA5DQBKODSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Library Function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Библиотечная функция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Library Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя библиотеки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW6XBB2PRWZEQHBUPLXTIHWNLQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invocation exception");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при выполнении");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWEFOS4TURRAGHKGR3EG776FS4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXD45OLATONAVXPFBP65UADWZ5A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Function Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя функции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXY256XIXCBFDFGHR5LXSPA4IQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(LibUserFunc - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),"LibUserFunc - Localizing Bundle",$$$items$$$);
}

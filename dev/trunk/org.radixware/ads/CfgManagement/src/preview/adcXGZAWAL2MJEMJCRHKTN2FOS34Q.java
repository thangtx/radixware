
/* Radix::CfgManagement::ImpExpUtils - Server Executable*/

/*Radix::CfgManagement::ImpExpUtils-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils")
public published class ImpExpUtils  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ImpExpUtils_mi.rdxMeta;}

	/*Radix::CfgManagement::ImpExpUtils:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::ImpExpUtils:Properties-Properties*/





























	/*Radix::CfgManagement::ImpExpUtils:Methods-Methods*/

	/*Radix::CfgManagement::ImpExpUtils:exportEntity-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils:exportEntity")
	public static published  void exportEntity (org.radixware.ads.Common.common.CommonXsd.ImpExpEntity xEntity, org.radixware.ads.Types.server.Entity entity, boolean isRoot, java.util.List<org.radixware.kernel.common.types.Id> ignoreProps) {
		xEntity.ClassId = entity.getClassDefinitionId();
		final java.util.List<Meta::PropDef> sortedProps = new java.util.ArrayList<Meta::PropDef>(entity.getRadMeta().getProps());
		final Java.Lang::Comparator<Meta::PropDef> comparator = new Java.Lang::Comparator<Meta::PropDef>() {
		    public int compare(Meta::PropDef p1, Meta::PropDef p2) {
		        return p1.Id.toString().compareTo(p2.Id.toString());
		    }
		};
		java.util.Collections.sort(sortedProps, comparator);

		for (Meta::PropDef p : sortedProps) {
		    if (!(p instanceof Meta::UserPropDef) || p.isDeprecated() || ignoreProps != null && ignoreProps.contains(p.Id))
		        continue;
		    final Meta::UserPropDef propDef = (Meta::UserPropDef) p;
		    Common::CommonXsd:UserProp xProp = xEntity.ensureUserProps().addNewUserProp();
		    xProp.Id = propDef.Id;
		    xProp.OwnVal =  isRoot || entity.getPropHasOwnVal(p.Id);
		    if (xProp.OwnVal == false)
		        continue;
		    if (propDef.getValType() == Meta::ValType:Object) {
		        Object propObject = entity.getProp(propDef.Id);
		        if (propObject == null)
		            continue;
		        if (propObject instanceof UserFunc::UserFunc) {
		            Common::CommonXsd:UserFunc xFunc = xProp.addNewFunc();
		            final UserFunc::UserFunc func = (UserFunc::UserFunc) propObject;
		            Common::CommonXsd:UserFunc x = func.export();
		            xFunc.set(x);
		        } else if (propObject instanceof IExportableUserProp) {
		            Common::CommonXsd:UserProp.Obj xObj = xProp.addNewObj();
		            final IExportableUserProp exportable = (IExportableUserProp) propObject;
		            xObj.set(exportable.export());
		            xObj.ClassId = Types::Id.Factory.loadFrom(propObject.Class.SimpleName);
		        } else
		            throw new AppError("Failed to export the object: user-defined property # cannot be exported" + propDef.Id);
		    } else {
		        xProp.SafeValue = entity.getPropAsStr(propDef.Id);
		    }
		}
	}

	/*Radix::CfgManagement::ImpExpUtils:importProps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils:importProps")
	public static published  void importProps (org.radixware.ads.Common.common.CommonXsd.ImpExpEntity xEntity, org.radixware.ads.Types.server.Entity entity, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper, boolean considerContext, org.radixware.ads.Types.server.Entity context) {
		if (xEntity.UserProps == null)
		    return;

		UserFunc::UserFunc.UserFuncImporter ufImporter = new UserFunc::UserFunc.UserFuncImporter();
		ufImporter.setHelper(helper);
		ufImporter.setContext(considerContext, context);

		for (Common::CommonXsd:UserProp xProp : xEntity.UserProps.UserPropList) {
		    try {
		        if (xProp.OwnVal == false)
		            entity.setPropHasOwnVal(xProp.Id, false);
		        else {
		            final Meta::PropDef propDef = entity.getRadMeta().getPropById(xProp.Id);
		            if (propDef.ValType == Meta::ValType:ParentRef) {
		                final Types::Pid pid = (Types::Pid) org.radixware.kernel.server.utils.SrvValAsStr.fromStr(Arte::Arte.getInstance(), xProp.isSetSafeValue() ? xProp.SafeValue : xProp.Value, propDef.getValType());
		                if (pid != null)
		                    entity.setProp(xProp.Id, Types::Entity.load(pid));
		                else
		                    entity.setProp(xProp.Id, null);
		            } else if (propDef.ValType == Meta::ValType:Object) {
		                if (xProp.Func != null) {
		                    ufImporter.setPropParams(entity, xProp.Id, true);
		                    ufImporter.setUserFuncXml(xProp.Func);
		                    ufImporter.import();
		                } else if (xProp.Obj != null) {
		                    Types::Entity empty = (Types::Entity) Arte::Arte.getDefManager().newClassInstance(xProp.Obj.ClassId, null);
		                    empty.setIsAutoUpdateEnabled(false); //???
		                    ((IExportableUserProp) empty).import(entity, xProp.Id, xProp.Obj, helper);
		                } else
		                    entity.setProp(xProp.Id, null);
		            } else {
		                entity.setPropAsStr(xProp.Id, xProp.isSetSafeValue() ? xProp.SafeValue : xProp.Value);
		            }
		        }
		    } catch (Exceptions::DefinitionNotFoundError ex) {
		        final String mes = "Error importing the property" + ": " + ex.toString();
		        if (helper != null) {
		            helper.reportWarnings(entity, mes);
		        } else {
		            Arte::Trace.error(calcObjTitle(entity) + ": " + mes, Arte::EventSource:AppCfgPackage);
		        }
		    }
		}
	}

	/*Radix::CfgManagement::ImpExpUtils:importProps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils:importProps")
	public static published  void importProps (org.radixware.ads.Common.common.CommonXsd.ImpExpEntity xEntity, org.radixware.ads.Types.server.Entity entity, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		importProps(xEntity, entity, helper, false, null);
	}

	/*Radix::CfgManagement::ImpExpUtils:checkImportedRef-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils:checkImportedRef")
	@Deprecated
	public static published  void checkImportedRef (org.radixware.ads.Types.server.Entity entity, org.radixware.kernel.common.types.Id propId, java.lang.Object extRef, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (extRef != null && entity.getProp(propId) == null) {
		    Str propTitle = entity.getClassDefinition().getPropById(propId).getTitle();
		    helper.reportPropsNotSet(entity, propTitle);
		}
	}

	/*Radix::CfgManagement::ImpExpUtils:clearUnsupportedImportedRef-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils:clearUnsupportedImportedRef")
	@Deprecated
	public static published  void clearUnsupportedImportedRef (org.radixware.ads.Types.server.Entity entity, org.radixware.kernel.common.types.Id propId, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (entity.getPropHasOwnVal(propId)) {
		    entity.setProp(propId, null); 
		    Str propTitle = entity.getClassDefinition().getPropById(propId).getTitle();
		    helper.reportPropsNotSet(entity, propTitle);
		}

	}

	/*Radix::CfgManagement::ImpExpUtils:writeToFile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils:writeToFile")
	public static published  void writeToFile (org.apache.xmlbeans.XmlObject xml, Str fileName) {
		Client.Resources::FileOutResource file = null;
		try {
		    file = new FileOutResource(Arte::Arte.getInstance(),
		            fileName,
		            Client.Resources::FileOpenMode:TruncateOrCreate,
		            Client.Resources::FileOpenShareMode:Write);
		    /*
		     * setSavePrettyPrint удаляет некоторые символы. Для форматирования 
		     * используется свой класс.
		     * */
		    org.radixware.kernel.common.utils.XmlFormatter.save(xml, file);
		    file.flush();
		} catch (Exceptions::Exception e) {
		    throw new InvalidEasRequestClientFault("Unable to write the file", e, "");
		} finally {
		    try {
		        if (file != null)
		            file.close();
		    } catch (java.io.IOException e) {
		        ;
		    }
		}
	}

	/*Radix::CfgManagement::ImpExpUtils:calcObjTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils:calcObjTitle")
	public static published  Str calcObjTitle (org.radixware.ads.Types.server.Entity obj) {
		return obj.getClassDefinitionTitle() + " '" + obj.calcTitle() + "'";
	}

	/*Radix::CfgManagement::ImpExpUtils:createCfgLookupAdvizor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils:createCfgLookupAdvizor")
	public static  org.radixware.ads.CfgManagement.server.ICfgObjectLookupAdvizor createCfgLookupAdvizor (org.radixware.kernel.common.types.Id entityId) throws java.lang.Exception {
		java.lang.Class<?> dbObjectCls = Arte::Arte.getClassById(entityId);

		if (entityId.getPrefix() == Meta::DefinitionIdPrefix:ADS_APPLICATION_CLASS) {
		    while (!dbObjectCls.getSimpleName().startsWith(Meta::DefinitionIdPrefix:ADS_ENTITY_CLASS.getValue())) {
		        dbObjectCls = dbObjectCls.getSuperclass();
		    }
		}

		java.lang.Class<?> advizorCls = null;
		for (java.lang.Class<?> innerCls : dbObjectCls.getDeclaredClasses()) {
		    if (!innerCls.isMemberClass() || !ICfgObjectLookupAdvizor.class.isAssignableFrom(innerCls)) {
		        continue;
		    } else {
		        advizorCls = innerCls;
		        break;
		    }
		}
		if (advizorCls == null) {
		    throw new Exception(
		            Str.format("Entity class %s does not contains CfgLookupAdvizor implementation.", entityId));
		}

		final java.lang.reflect.Constructor<?> con;
		try {
		    con = advizorCls.getDeclaredConstructor(new Class<?>[]{});
		    con.setAccessible(true);
		} catch (java.lang.NoSuchMethodException ex) {
		    throw new Exception(Str.format("Can not instantiate lookup advisor class for entity %s", entityId), ex);
		}

		try {
		    return (ICfgObjectLookupAdvizor) con.newInstance(new Object[]{});
		} catch (Exception ex) {
		    throw new Exception(Str.format("Can not instantiate lookup advisor class for entity %s", entityId), ex);
		}
	}

	/*Radix::CfgManagement::ImpExpUtils:getExtGuid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::ImpExpUtils:getExtGuid")
	public static published  Str getExtGuid (org.radixware.ads.Types.server.Entity obj) {
		return obj instanceof ICfgReferencedObject ? ((ICfgReferencedObject) obj).getCfgReferenceExtGuid() : null;
	}


}

/* Radix::CfgManagement::ImpExpUtils - Server Meta*/

/*Radix::CfgManagement::ImpExpUtils-Server Dynamic Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ImpExpUtils_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcXGZAWAL2MJEMJCRHKTN2FOS34Q"),"ImpExpUtils",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::CfgManagement::ImpExpUtils:Properties-Properties*/
						null,

						/*Radix::CfgManagement::ImpExpUtils:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3ST4O6LUQJGBZOCMBX4OZ24MI4"),"exportEntity",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xEntity",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC25YH2KXVZFN3FKNQINR2PGOWI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFZ7RGJ2VWNGOBKRNCCMQ4QISJQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isRoot",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3AVZ6263SJD53KECORFFHTQDGQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ignoreProps",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr37CXFJ7PBRFEBGKB6KTL64TXXM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6DEET7H4PRC47KVBRTVMNTGLNQ"),"importProps",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xEntity",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4RFFFZQEFNHFDKTUNXOCFT2CVQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYQM7EC6XSRCI3HWTNKBXK5EOSM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNTMECILW4FE65EZSJQTGVA57CE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2VYRCBYQ7FCHTE4LGAZKMBFJN4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUWMOAWQ4FFCULKBSHCBBTI47LU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6G6B4OLHCFBXLJU4UUSNQCYADI"),"importProps",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xEntity",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4RFFFZQEFNHFDKTUNXOCFT2CVQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYQM7EC6XSRCI3HWTNKBXK5EOSM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNTMECILW4FE65EZSJQTGVA57CE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWBRVCEZVGFFGFEOQKOHOZF6WYU"),"checkImportedRef",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG2AQXVFVYVDRBDONQC24PQCH4E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI7MLKYWSONCBFODNBXBRJF6VDY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extRef",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPSQI3EOXU5ADFBLTLR3KKFC2FU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUNOBRERCNJBDHIMCGVXTXBAC7M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth74ZVCNO5RFGAHDCCZCMJNPB7AE"),"clearUnsupportedImportedRef",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKEVFMUFF5NATBKEFWDCWJL2KFM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI7MLKYWSONCBFODNBXBRJF6VDY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUNOBRERCNJBDHIMCGVXTXBAC7M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXRWMISCIY5ECTEVH6DISRVE3U4"),"writeToFile",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTS5PTHOAXRGLJMK7OCDLL5GW74")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKPQVT25KUVDWBIGAVYUTKUVB6I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFWMOSTPENBE5JO6A6A4IO7JP2Q"),"calcObjTitle",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZXHAUA2Y5HGHESZNJVGOMGKJA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCPAA433M7BDZBAEM2JYAXBIYVQ"),"createCfgLookupAdvizor",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entityId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2MCASU6QBRFC3EIRAHCHSWHLYE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH3YZ6LOEGRF23J2AOGBPJNZIU4"),"getExtGuid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJPQTDEWYTNBILL46CKBAZGVKR4"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::CfgManagement::ImpExpUtils - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ImpExpUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check if property set and report warning");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DF27NHNYZFFLN4QXXQBZ6BARM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to write the file");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось записать файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7EDNQKTAINF6FH67B5FUQPQUXM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to export the object: user-defined property # cannot be exported");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось экспортировать объект - невозможно экспортировать пользовательское свойство #");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDCD6VJ3ILFGFZFVWQZGUA2L5CE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error importing the property");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка импорта свойства");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUW2PONDJJJEKNPPYOK3ASY55IY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Clear reference property and report warning");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZX7QQ5XHSZFD3E4KIMKGUKSYVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ImpExpUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcXGZAWAL2MJEMJCRHKTN2FOS34Q"),"ImpExpUtils - Localizing Bundle",$$$items$$$);
}

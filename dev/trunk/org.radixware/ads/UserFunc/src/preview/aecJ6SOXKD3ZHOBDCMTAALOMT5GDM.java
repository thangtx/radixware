
/* Radix::UserFunc::UserFunc - Server Executable*/

/*Radix::UserFunc::UserFunc-Entity Class*/

package org.radixware.ads.UserFunc.server;

import java.lang.reflect.Method;
import org.radixware.kernel.common.types.Pid;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc")
public abstract published class UserFunc  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.UserFunc.server.IUserFuncImpExp,org.radixware.ads.CfgManagement.server.IChangeLogOwner,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private String profileTitle = null;
	private static final String UNDEFINED_METHOD_PROFILE_TITLE = "<source method not found>";
	private Meta::AdsDefXsd:ParameterDeclaration[] paramsOrderCache = null;

	private class ServerUserDefRequestor implements org.radixware.kernel.common.defs.ads.userfunc.UdsObserver.IUserDefRequestor {

	    public void readUserDefHeaders(final java.util.Set<Meta::DefType> defTypes, org.radixware.kernel.common.defs.ads.userfunc.UdsObserver.IUserDefReceiver recv) {
	        if (defTypes.contains(Meta::DefType:Class)) {
	            try {
	                UserReportHeadersCursor cursor = UserReportHeadersCursor.open();

	                while (cursor.next()) {
	                    recv.accept(Types::Id.Factory.loadFrom(cursor.reportId),
	                            cursor.reportName,
	                            cursor.moduleName,
	                            Types::Id.Factory.loadFrom(cursor.moduleId));
	                }
	            } catch (Exceptions::DatabaseError e) {
	                //ignore
	            }
	        }
	        if (defTypes.contains(Meta::DefType:Role)) {
	            try {
	                UserRoleHeadersCursor cursor = UserRoleHeadersCursor.open();

	                while (cursor.next()) {
	                    recv.accept(Types::Id.Factory.loadFrom(cursor.roleId),
	                            cursor.roleName,
	                            null,
	                            null);
	                }
	            } catch (Exceptions::DatabaseError e) {
	                //ignore
	            }
	        }
	        if (defTypes.contains(Meta::DefType:UserFunc)) {
	            try {
	                LibUserFuncHeadersCursor cursor = LibUserFuncHeadersCursor.open(null);
	                while (cursor.next()) {
	                    //if( cursor.!=null && cursor..!=null && cursor...!=null){
	                    Types::Id ufId = Types::Id.Factory.loadFrom(cursor.libUserFuncGuid);
	                    Types::Id libId = Types::Id.Factory.loadFrom(cursor.ufLibName);
	                    String strProfile = cursor.ufProfile;
	//                    try {
	////                        if (!(cursor.. instanceof ) && strProfile != null) {
	////                            int endIndex = strProfile.indexOf("(");
	////                            if (endIndex != -1) {
	////                                String s = strProfile.substring(0, endIndex);
	////                                int startIndex = s.lastIndexOf(" ");
	////                                s = strProfile.substring(0, startIndex + 1);
	////                                strProfile = s + ufId + strProfile.substring(endIndex);
	////                            }
	////                        }
	//                    } catch ( e) {
	//                        .(.(e), );
	//                    }
	                    recv.accept(ufId,
	                            strProfile == null ? ufId.toString() : strProfile,
	                            cursor.ufLibName,
	                            libId);
	                    //}
	                }
	            } catch (Exceptions::DatabaseError e) {
	                Arte::Trace.debug(Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	            }
	        }
	    }

	    public Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.AdsUserFuncDefinition getLibUserFuncXml(Types::Id libUserFuncId) {
	        return LibUserFunc.getXml(libUserFuncId);
	    }

	    public org.radixware.schemas.adsdef.ClassDefinition getClassDefXml(Types::Id reportId) {
	        org.radixware.schemas.adsdef.AdsDefinitionDocument xDoc = loadUserClassXml(reportId == null ? null : reportId.toString());
	        if (xDoc == null || xDoc.AdsDefinition == null) {
	            return null;
	        }
	        return xDoc.AdsDefinition.AdsClassDefinition;
	    }
	}

	private class ServerUdsObserver extends org.radixware.kernel.common.defs.ads.userfunc.UdsObserver {

	    public org.radixware.kernel.common.defs.ads.userfunc.UdsObserver.IUserDefRequestor getReportRequestor() {
	        return requestor != null ? requestor : (requestor = new ServerUserDefRequestor());
	    }
	    
	    @Override
	    public java.util.Map<Pid, Bool> checkEntitiesExistance(java.util.Set<Pid> pidsToCheck) {
	        return checkEntitiesExistance(pidsToCheck);
	    }
	}

	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return UserFunc_mi.rdxMeta;}

	/*Radix::UserFunc::UserFunc:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFunc:UserFuncImporter-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter")
	public static published class UserFuncImporter  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return UserFunc_mi.rdxMeta_adc6KN4L7WAD5HDRCOOQRMI3AN424;}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:Nested classes-Nested Classes*/

		/*Radix::UserFunc::UserFunc:UserFuncImporter:Properties-Properties*/

		/*Radix::UserFunc::UserFunc:UserFuncImporter:considerContext-Dynamic Property*/



		protected Bool considerContext=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:considerContext")
		private final  Bool getConsiderContext() {
			return considerContext;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:considerContext")
		private final   void setConsiderContext(Bool val) {
			considerContext = val;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:context-Dynamic Property*/



		protected org.radixware.ads.Types.server.Entity context=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:context")
		public  org.radixware.ads.Types.server.Entity getContext() {
			return context;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:context")
		public   void setContext(org.radixware.ads.Types.server.Entity val) {
			context = val;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:helper-Dynamic Property*/



		protected org.radixware.ads.CfgManagement.server.ICfgImportHelper helper=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:helper")
		private final  org.radixware.ads.CfgManagement.server.ICfgImportHelper getHelper() {
			return helper;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:helper")
		private final   void setHelper(org.radixware.ads.CfgManagement.server.ICfgImportHelper val) {
			helper = val;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:isSet-Dynamic Property*/



		protected boolean isSet=false;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:isSet")
		private final  boolean getIsSet() {
			return isSet;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:isSet")
		private final   void setIsSet(boolean val) {
			isSet = val;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:propId-Dynamic Property*/



		protected org.radixware.kernel.common.types.Id propId=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:propId")
		private final  org.radixware.kernel.common.types.Id getPropId() {
			return propId;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:propId")
		private final   void setPropId(org.radixware.kernel.common.types.Id val) {
			propId = val;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:propOwner-Dynamic Property*/



		protected org.radixware.ads.Types.server.Entity propOwner=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:propOwner")
		private final  org.radixware.ads.Types.server.Entity getPropOwner() {
			return propOwner;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:propOwner")
		private final   void setPropOwner(org.radixware.ads.Types.server.Entity val) {
			propOwner = val;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:xml-Dynamic Property*/



		protected org.radixware.ads.Common.common.CommonXsd.UserFunc xml=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:xml")
		private final  org.radixware.ads.Common.common.CommonXsd.UserFunc getXml() {
			return xml;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:xml")
		private final   void setXml(org.radixware.ads.Common.common.CommonXsd.UserFunc val) {
			xml = val;
		}







































































		/*Radix::UserFunc::UserFunc:UserFuncImporter:Methods-Methods*/

		/*Radix::UserFunc::UserFunc:UserFuncImporter:import-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:import")
		public  org.radixware.ads.UserFunc.server.UserFunc import () {
			return import(null);
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:setContext-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:setContext")
		public  void setContext (boolean considerContext, org.radixware.ads.Types.server.Entity context) {
			considerContext = considerContext;
			context = context;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:setHelper-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:setHelper")
		public  void setHelper (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
			helper = helper;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:setPropParams-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:setPropParams")
		public  void setPropParams (org.radixware.ads.Types.server.Entity obj, org.radixware.kernel.common.types.Id propId, boolean isSet) {
			propOwner = obj;
			propId = propId;
			isSet = isSet;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:setUserFuncXml-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:setUserFuncXml")
		public  void setUserFuncXml (org.radixware.ads.Common.common.CommonXsd.UserFunc xml) {
			xml = xml;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:reset-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:reset")
		public  void reset () {
			considerContext = null;
			context = null;
			helper = null;
			isSet = false;
			propId = null;
			propOwner = null;
			xml = null;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:UserFuncImporter-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:UserFuncImporter")
		public  UserFuncImporter () {

		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:UserFuncImporter-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:UserFuncImporter")
		public  UserFuncImporter (org.radixware.ads.Types.server.Entity obj, org.radixware.kernel.common.types.Id propId, boolean isSet, org.radixware.ads.Common.common.CommonXsd.UserFunc xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
			propOwner = obj;
			propId = propId;
			isSet = isSet;
			xml = xml;
			helper = helper;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:UserFuncImporter-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:UserFuncImporter")
		public  UserFuncImporter (org.radixware.ads.Common.common.CommonXsd.UserFunc xml) {
			xml = xml;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:import-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:import")
		  org.radixware.ads.UserFunc.server.UserFunc import (org.radixware.ads.UserFunc.server.UserFunc func) {
			final boolean importAsProperty = propOwner != null && propId != null;
			if (importAsProperty) {
			    if (!isSet) {
			        propOwner.setPropHasOwnVal(propId, false);
			        return null;
			    }
			    if (xml == null || xml.isNil()) {
			        propOwner.setProp(propId, null);
			        return null;
			    }

			    actualizeXmlUserPropInfo(xml, propOwner, propId);
			} else {
			    if (xml == null || xml.isNil()) {
			        return null;
			    }
			}

			if (func == null) {
			    if (importAsProperty && propOwner.getPropHasOwnVal(propId)) {
			        func = (UserFunc) propOwner.getProp(propId);
			    }
			    if (func == null) {
			        func = create(xml.ClassGUID);
			    }
			}

			func.upOwnerKnownFromImport = propOwner;
			final boolean userFuncWithFixedProfile = func instanceof UserFunc.FreeForm == false;
			if (userFuncWithFixedProfile && xml.isSetProfileVersion() && func.getCurrentVersion() != xml.ProfileVersion) {
			    //Если в файле импорта указана старая версия профиля, то
			    //ставим её и проверяем нашёлся ли метод для этой версии и
			    //совпал ли он методом из файла импорта.
			    //Если использовать старую версию профиля не удалось, то 
			    //ставим текущую и пишем предупреждение.
			    func.version = xml.ProfileVersion;
			    if (func.getMethodId() != xml.MethodId) {
			        func.version = func.getCurrentVersion();
			        func.reportImportWarnings(helper,
			                String.format(
			                        "System not support version %s of user function profile. Current profile version will be used instead: %s",
			                        xml.ProfileVersion, func.getDisplayProfile(true)));
			    }
			}

			func.import(xml, false, considerContext != null ? considerContext.booleanValue() : false,
			        context != null ? context : propOwner, helper);

			if (importAsProperty) {
			    propOwner.setProp(propId, func);
			}

			final boolean compileDeferred;
			if (helper != null && helper.getUserFuncImportHelper() != null) {
			    compileDeferred = helper.getUserFuncImportHelper().isCompileDeferred();
			} else {
			    compileDeferred = false;
			}
			if (!compileDeferred && helper != null) {
			    func.reportIsStateValid(helper);
			}

			return func;
		}

		/*Radix::UserFunc::UserFunc:UserFuncImporter:actualizeXmlUserPropInfo-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:UserFuncImporter:actualizeXmlUserPropInfo")
		private final  void actualizeXmlUserPropInfo (org.radixware.ads.Common.common.CommonXsd.UserFunc xml, org.radixware.ads.Types.server.Entity propOwner, org.radixware.kernel.common.types.Id propId) {
			if (xml.Binding != null && xml.OwnerClassId != null) {
			    actualizeUserFuncOwnerBinding(xml.Binding.ParamsBinding, Types::Id.Factory.loadFrom(xml.OwnerClassId), xml.OwnerPid, propOwner.getPid().toString());
			}

			xml.OwnerEntityId = propOwner.getEntityDefinitionId().toString();
			xml.OwnerClassId = propOwner.getClassDefinitionId().toString();
			xml.OwnerPid = propOwner.getPid().toString();
			xml.OwnerPropId = propId.toString();
		}


	}

	/*Radix::UserFunc::UserFunc:Properties-Properties*/

	/*Radix::UserFunc::UserFunc:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:classGuid")
	public final published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:classGuid")
	public final published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::UserFunc::UserFunc:userClassGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:userClassGuid")
	public published  Str getUserClassGuid() {
		return userClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:userClassGuid")
	public published   void setUserClassGuid(Str val) {
		userClassGuid = val;
	}

	/*Radix::UserFunc::UserFunc:upDefId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upDefId")
	public published  Str getUpDefId() {
		return upDefId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upDefId")
	public published   void setUpDefId(Str val) {
		upDefId = val;
	}

	/*Radix::UserFunc::UserFunc:upOwnerEntityId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerEntityId")
	public published  Str getUpOwnerEntityId() {
		return upOwnerEntityId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerEntityId")
	public published   void setUpOwnerEntityId(Str val) {
		upOwnerEntityId = val;
	}

	/*Radix::UserFunc::UserFunc:upOwnerPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerPid")
	public published  Str getUpOwnerPid() {
		return upOwnerPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerPid")
	public published   void setUpOwnerPid(Str val) {
		upOwnerPid = val;
	}

	/*Radix::UserFunc::UserFunc:lastUpdateUserName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastUpdateUserName")
	public published  Str getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastUpdateUserName")
	public published   void setLastUpdateUserName(Str val) {
		lastUpdateUserName = val;
	}

	/*Radix::UserFunc::UserFunc:javaSrc-Column-Based Property*/










































	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaSrc")
	public published  org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument getJavaSrc() {
		return javaSrc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaSrc")
	public published   void setJavaSrc(org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument val) {

		internal[javaSrc] = val;
		onJavaSrcChanged(val);
	}

	/*Radix::UserFunc::UserFunc:javaRuntime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaRuntime")
	public published  java.sql.Blob getJavaRuntime() {
		return javaRuntime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaRuntime")
	public published   void setJavaRuntime(java.sql.Blob val) {
		javaRuntime = val;
	}

	/*Radix::UserFunc::UserFunc:extSrc-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:extSrc")
	public published  java.sql.Clob getExtSrc() {
		return extSrc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:extSrc")
	public published   void setExtSrc(java.sql.Clob val) {

		internal[extSrc] = val;
		                        extSrcChanged();
	}

	/*Radix::UserFunc::UserFunc:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastUpdateTime")
	public published  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastUpdateTime")
	public published   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::UserFunc::UserFunc:methodId-Dynamic Property*/



	protected Str methodId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:methodId")
	public published  Str getMethodId() {

		Types::Id methodId = getMethodId();
		return methodId == null ? null : methodId.toString();
		                        
	}

	/*Radix::UserFunc::UserFunc:caching-Dynamic Property*/



	protected org.radixware.ads.Utils.server.Caching caching=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:caching")
	private final  org.radixware.ads.Utils.server.Caching getCaching() {

		if(internal[caching] == null)
		    internal[caching] = new Caching(this);
		return internal[caching];

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:caching")
	private final   void setCaching(org.radixware.ads.Utils.server.Caching val) {
		caching = val;
	}

	/*Radix::UserFunc::UserFunc:ownerTitle-Dynamic Property*/



	protected Str ownerTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerTitle")
	public published  Str getOwnerTitle() {

		try {
		    Types::Entity e = findOwner();
		    if (e != null) {
		        return e.getClassDefinitionTitle() + " '" + e.calcTitle() + "'";
		    } else {
		        return "<owner object not found, PID " +( upOwnerPid == null ? "" : upOwnerPid)+">";
		    }
		} catch (Exceptions::Throwable e) {
		    return "";
		}
	}

	/*Radix::UserFunc::UserFunc:description-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:description")
	public published  Str getDescription() {
		return description;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:description")
	public published   void setDescription(Str val) {
		description = val;
	}

	/*Radix::UserFunc::UserFunc:userFunc-Dynamic Property*/



	protected org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef userFunc=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:userFunc")
	private final  org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef getUserFunc() {
		return userFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:userFunc")
	private final   void setUserFunc(org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef val) {
		userFunc = val;
	}

	/*Radix::UserFunc::UserFunc:displayProfile-Dynamic Property*/



	protected Str displayProfile=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:displayProfile")
	public published  Str getDisplayProfile() {

		return getDisplayProfile();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:displayProfile")
	public published   void setDisplayProfile(Str val) {
		displayProfile = val;
	}

	/*Radix::UserFunc::UserFunc:version-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:version")
	private final  Int getVersion() {
		return version;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:version")
	private final   void setVersion(Int val) {
		version = val;
	}

	/*Radix::UserFunc::UserFunc:currentVersion-Dynamic Property*/



	protected Int currentVersion=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:currentVersion")
	public published  Int getCurrentVersion() {

		return getCurrentVersion();
	}

	/*Radix::UserFunc::UserFunc:ownerClassName-Dynamic Property*/



	protected Str ownerClassName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerClassName")
	public published  Str getOwnerClassName() {

		final String errorMessage="User-defined function owner does not exist";
		try{
		    Types::Entity e = findOwner();
		    if(e != null)
		        return e.RadMeta.Name;
		    else
		        return errorMessage;
		}catch(Exceptions::Throwable e){//will thrown if property is accessed during object creation
		    return "";
		}

	}

	/*Radix::UserFunc::UserFunc:isValid-Dynamic Property*/



	protected Bool isValid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isValid")
	public published  Bool getIsValid() {

		if (isLinkUsed != null && isLinkUsed.booleanValue()) {
		    if (linkedLibFunc == null || linkedLibFunc.upUserFunc == null) {
		        return false;
		    }else{
		        return linkedLibFunc.upUserFunc.isValid;
		    }
		}else{
		    return javaRuntime != null;
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isValid")
	public published   void setIsValid(Bool val) {
		isValid = val;
	}

	/*Radix::UserFunc::UserFunc:upOwnerClassId-Dynamic Property*/



	protected Str upOwnerClassId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerClassId")
	public  Str getUpOwnerClassId() {

		if (upOwnerKnownFromImport != null) {
		    return upOwnerKnownFromImport.getClassDefinitionId().toString();
		}
		try { 
		    Types::Entity e = findOwner();
		    if (e != null) {
		        Meta::ClassDef meta = e.getRadMeta();
		        return meta == null ? null : meta.getId().toString();
		    } else {
		        return null;
		    }
		} catch (org.radixware.kernel.common.exceptions.DefinitionNotFoundError e) {
		    return "";
		}
	}

	/*Radix::UserFunc::UserFunc:path-Dynamic Property*/



	protected Str path=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:path")
	public published  Str getPath() {

		return "User Defined Function [id=" + id  + "] '" + ownerTitle + " -> " + ownerPropertyName + "'";
	}

	/*Radix::UserFunc::UserFunc:ownerPropertyFullName-Dynamic Property*/



	protected Str ownerPropertyFullName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPropertyFullName")
	public published  Str getOwnerPropertyFullName() {

		try {
		    Types::Entity e = findOwner();
		    if (e != null) {
		        Meta::ClassDef meta = e.getRadMeta();
		        if (meta == null) {
		            return "";
		        } else {
		            Meta::PropDef prop = meta.getPropById(Types::Id.Factory.loadFrom(upDefId));
		            if (prop != null) {
		                return meta.Name + "." + prop.Name;
		            } else
		                return "";
		        }
		    } else {
		        return null;
		    }
		} catch (Exceptions::Throwable e) {
		    return "";
		}

	}

	/*Radix::UserFunc::UserFunc:ownerPropertyName-Dynamic Property*/



	protected Str ownerPropertyName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPropertyName")
	public published  Str getOwnerPropertyName() {

		try {
		    Types::Entity e = findOwner();
		    if (e != null) {
		        Meta::ClassDef meta = e.getRadMeta();
		        if (meta == null) {
		            return "";
		        } else {
		            Meta::PropDef prop = meta.getPropById(Types::Id.Factory.loadFrom(upDefId));
		            if (prop != null) {
		                return prop.Name;
		            } else
		                return "";
		        }
		    } else {
		        return null;
		    }
		} catch (Exceptions::Throwable e) {
		    return "";
		}

	}

	/*Radix::UserFunc::UserFunc:javaSrcVers-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaSrcVers")
	public published  java.sql.Clob getJavaSrcVers() {
		return javaSrcVers;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaSrcVers")
	public published   void setJavaSrcVers(java.sql.Clob val) {
		javaSrcVers = val;
	}

	/*Radix::UserFunc::UserFunc:editedSourceVersions-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr editedSourceVersions=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:editedSourceVersions")
	public  org.radixware.kernel.common.types.ArrStr getEditedSourceVersions() {
		return editedSourceVersions;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:editedSourceVersions")
	public   void setEditedSourceVersions(org.radixware.kernel.common.types.ArrStr val) {
		editedSourceVersions = val;
	}

	/*Radix::UserFunc::UserFunc:lastUpdateTimeGetter-Dynamic Property*/



	protected org.radixware.ads.Utils.server.ILastUpdateTimeGetter lastUpdateTimeGetter=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastUpdateTimeGetter")
	private final  org.radixware.ads.Utils.server.ILastUpdateTimeGetter getLastUpdateTimeGetter() {

		if (internal[lastUpdateTimeGetter] == null) {

		    internal[lastUpdateTimeGetter] = new Utils::ILastUpdateTimeGetter() {

		        public DateTime getLastUpdateTime() {
		            final GetUserFuncLastUpdateTimeCursor cursor = GetUserFuncLastUpdateTimeCursor.open(upDefId, upOwnerEntityId, upOwnerPid);
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

	/*Radix::UserFunc::UserFunc:isOwnerExist-Dynamic Property*/



	protected Bool isOwnerExist=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerExist")
	public  Bool getIsOwnerExist() {

		try {
		    Types::Entity e = findOwner();
		    if (e != null) {
		        return true;
		    }
		} catch (org.radixware.kernel.common.exceptions.DefinitionNotFoundError e) {
		    return false;
		}
		return false;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerExist")
	public   void setIsOwnerExist(Bool val) {
		isOwnerExist = val;
	}

	/*Radix::UserFunc::UserFunc:ownerJavaClassName-Dynamic Property*/



	protected Str ownerJavaClassName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerJavaClassName")
	public  Str getOwnerJavaClassName() {


		try{
		    Types::Entity e = findOwner();
		    if(e != null)
		        return e.Class.getName();
		    else
		        return null;
		}catch(Exceptions::Throwable e){//will thrown if property is accessed during object creation
		    return null;
		}

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerJavaClassName")
	public   void setOwnerJavaClassName(Str val) {
		ownerJavaClassName = val;
	}

	/*Radix::UserFunc::UserFunc:pathQuick-Dynamic Property*/



	protected Str pathQuick=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:pathQuick")
	public published  Str getPathQuick() {

		if (upOwnerEntityId == idof[LibUserFunc].toString()) {
		    try {
		        Types::Entity e = findOwner();
		        if (e instanceof LibUserFunc) {
		            return ((LibUserFunc) e).fullProfile;
		        }
		    } catch (Exceptions::Exception e) {
		        //ignore, return simple class name
		    }
		} 

		return  id  + ") " + ownerTitleQuick + " -> " + ownerPropertyName;
	}

	/*Radix::UserFunc::UserFunc:ownerTitleQuick-Dynamic Property*/



	protected Str ownerTitleQuick=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerTitleQuick")
	public published  Str getOwnerTitleQuick() {

		if (upOwnerEntityId == idof[LibUserFunc].toString()) {
		    try {
		        Types::Entity e = findOwner();
		        if (e instanceof LibUserFunc) {
		            return ((LibUserFunc) e).libName + "."  + ((LibUserFunc) e).name +  ("(PID=" + upOwnerPid + ")");
		        }
		    } catch (Exceptions::Exception e) {
		        //ignore, return simple class name
		    }
		}
		return ownerClassName + "(PID=" + upOwnerPid + ")";
	}

	/*Radix::UserFunc::UserFunc:libFuncGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:libFuncGuid")
	public published  Str getLibFuncGuid() {
		return libFuncGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:libFuncGuid")
	public published   void setLibFuncGuid(Str val) {
		libFuncGuid = val;
	}

	/*Radix::UserFunc::UserFunc:parameterNames-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr parameterNames=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:parameterNames")
	private final  org.radixware.kernel.common.types.ArrStr getParameterNames() {

		if(internal[parameterNames] == null){
		    internal[parameterNames] = readParameterNames();
		}
		return internal[parameterNames];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:parameterNames")
	private final   void setParameterNames(org.radixware.kernel.common.types.ArrStr val) {
		parameterNames = val;
	}

	/*Radix::UserFunc::UserFunc:usedDefinitions-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:usedDefinitions")
	public  java.sql.Clob getUsedDefinitions() {
		return usedDefinitions;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:usedDefinitions")
	public   void setUsedDefinitions(java.sql.Clob val) {
		usedDefinitions = val;
	}

	/*Radix::UserFunc::UserFunc:linkedLibFunc-Dynamic Property*/



	protected org.radixware.ads.UserFunc.server.LibUserFunc linkedLibFunc=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:linkedLibFunc")
	public  org.radixware.ads.UserFunc.server.LibUserFunc getLinkedLibFunc() {

		return LibUserFunc.loadByPK(libFuncGuid,true);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:linkedLibFunc")
	public   void setLinkedLibFunc(org.radixware.ads.UserFunc.server.LibUserFunc val) {

		if(val==null){
		    libFuncGuid=null;
		}else{
		    libFuncGuid=val.guid;
		}
	}

	/*Radix::UserFunc::UserFunc:paramBinding-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:paramBinding")
	public published  java.sql.Clob getParamBinding() {
		return paramBinding;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:paramBinding")
	public published   void setParamBinding(java.sql.Clob val) {

		if (forbidBindingXmlCopy) {
		    return;
		}
		internal[paramBinding] = val;
	}

	/*Radix::UserFunc::UserFunc:isLinkUsed-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isLinkUsed")
	public published  Bool getIsLinkUsed() {
		return isLinkUsed;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isLinkUsed")
	public published   void setIsLinkUsed(Bool val) {
		isLinkUsed = val;
	}

	/*Radix::UserFunc::UserFunc:ownerLibFunc-Dynamic Property*/



	protected org.radixware.ads.UserFunc.server.LibUserFunc ownerLibFunc=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerLibFunc")
	public  org.radixware.ads.UserFunc.server.LibUserFunc getOwnerLibFunc() {

		if (initOfDuplicatedObj != null && initOfDuplicatedObj == true) {
		    return null;
		}
		if(upOwnerEntityId == idof[System::LibUserFunc].toString()){
		    try{
		        return  LibUserFunc.loadByPidStr(this.upOwnerPid,false);
		    }catch(Exceptions::EntityObjectNotExistsError e){
		        //ignore
		    }
		}
		return null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerLibFunc")
	public   void setOwnerLibFunc(org.radixware.ads.UserFunc.server.LibUserFunc val) {
		ownerLibFunc = val;
	}

	/*Radix::UserFunc::UserFunc:paramsOrder-Dynamic Property*/



	protected org.radixware.schemas.adsdef.ParameterDeclaration[] paramsOrder=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:paramsOrder")
	private final  org.radixware.schemas.adsdef.ParameterDeclaration[] getParamsOrder() {

		if (paramsOrderCache == null) {
		    if (this.javaSrc != null && this.javaSrc.AdsUserFuncDefinition != null && this.javaSrc.AdsUserFuncDefinition.UserFuncProfile != null && this.javaSrc.AdsUserFuncDefinition.UserFuncProfile.Parameters != null) {
		        Meta::AdsDefXsd:ParameterDeclaration[] ids = new Meta::AdsDefXsd:ParameterDeclaration[this.javaSrc.AdsUserFuncDefinition.UserFuncProfile.Parameters.ParameterList.size()];
		        int index = 0;
		        Types::Id methodId = this.javaSrc.AdsUserFuncDefinition.MethodId;        
		        for (org.radixware.schemas.adsdef.ParameterDeclaration decl : this.javaSrc.AdsUserFuncDefinition.UserFuncProfile.Parameters.ParameterList) {
		            org.radixware.schemas.adsdef.ParameterDeclaration copy = org.radixware.schemas.adsdef.ParameterDeclaration.Factory.newInstance();
		            copy.set(decl);
		            if (copy.getId() == null && methodId != null) {
		                Types::Id tmp = Types::Id.Factory.changePrefix(methodId, Meta::DefinitionIdPrefix:ADS_METHOD_PARAMETER);
		                copy.setId(Types::Id.Factory.loadFrom(tmp.toString() + "_" + String.valueOf(index)));                                           
		            }           
		            ids[index] = copy;
		            index++;
		        }
		        paramsOrderCache = ids;
		    } else {
		        paramsOrderCache = new Meta::AdsDefXsd:ParameterDeclaration[0];
		    }
		}
		return paramsOrderCache;
	}

	/*Radix::UserFunc::UserFunc:resultType-Dynamic Property*/



	protected org.radixware.schemas.xscml.TypeDeclarationDocument resultType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:resultType")
	public  org.radixware.schemas.xscml.TypeDeclarationDocument getResultType() {

		if (internal[resultType] == null) {
		    Meta::XscmlXsd:TypeDeclarationDocument xDoc = null;
		    if (this.javaSrc != null && this.javaSrc.AdsUserFuncDefinition != null && this.javaSrc.AdsUserFuncDefinition.UserFuncProfile != null) {

		        if (this.javaSrc.AdsUserFuncDefinition.UserFuncProfile.ReturnType != null && !this.javaSrc.AdsUserFuncDefinition.UserFuncProfile.ReturnType.isNil()) {
		            xDoc = Meta::XscmlXsd:TypeDeclarationDocument.Factory.newInstance();
		            xDoc.TypeDeclaration = this.javaSrc.AdsUserFuncDefinition.UserFuncProfile.ReturnType;
		        }

		    }
		    internal[resultType] = xDoc;
		}
		return internal[resultType];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:resultType")
	public   void setResultType(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
		resultType = val;
	}

	/*Radix::UserFunc::UserFunc:usedLibraryFunctions-Dynamic Property*/



	protected org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.UserFunc.server.LibUserFunc> usedLibraryFunctions=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:usedLibraryFunctions")
	public published  org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.UserFunc.server.LibUserFunc> getUsedLibraryFunctions() {

		return getUsedLibraryFunctions();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:usedLibraryFunctions")
	public published   void setUsedLibraryFunctions(org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.UserFunc.server.LibUserFunc> val) {
		usedLibraryFunctions = val;
	}

	/*Radix::UserFunc::UserFunc:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:id")
	public  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:id")
	public   void setId(Int val) {
		id = val;
	}

	/*Radix::UserFunc::UserFunc:optimizerCache-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr optimizerCache=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:optimizerCache")
	private final  org.radixware.kernel.common.types.ArrStr getOptimizerCache() {

		if (internal[optimizerCache] != null) {
		    return internal[optimizerCache];
		}

		internal[optimizerCache] = new ArrStr();
		final java.util.Set<String> usedLibFuncs = new java.util.HashSet<>();
		final java.util.Set<String> usedDbEntities = new java.util.HashSet<>();

		//Search for lib functions and database entities usage in current user function source.
		for (Meta::XscmlXsd:JmlType.Item item : javaSrc.AdsUserFuncDefinition.Source.ItemList) {
		    if (item.isSetIdReference()) {
		        if (!item.IdReference.Path.isEmpty()) {
		            if (item.IdReference.Path.get(0).getPrefix() == Meta::DefinitionIdPrefix:LIB_USERFUNC_PREFIX) {
		                usedLibFuncs.add(item.IdReference.Path.get(0).toString());
		            }
		        }
		    } else if (item.isSetDbEntity()) {
		        usedDbEntities.add(item.DbEntity.EntityId + "~" + item.DbEntity.PidAsStr);
		    }
		}

		//Load all used library function xml-files.
		if (!usedLibFuncs.isEmpty()) {
		    LibUserFuncHeadersCursor cursor = LibUserFuncHeadersCursor.open(null);
		    try {
		        while (!usedLibFuncs.isEmpty() && cursor.next()) {
		            if (usedLibFuncs.contains(cursor.libUserFuncGuid) && cursor.libUserFunc.upUserFunc != null) {
		                Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.AdsUserFuncDefinition xDef = LibUserFunc.getXml(cursor.libUserFunc, cursor.ufLibName);
		                if (xDef != null) {
		                    Meta::AdsDefXsd:AdsUserFuncDefinitionDocument newDoc = Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.Factory.newInstance();
		                    newDoc.addNewAdsUserFuncDefinition().set(xDef);
		                    internal[optimizerCache].add(cursor.libUserFuncGuid + "=" + newDoc.xmlText());
		                }
		                usedLibFuncs.remove(cursor.libUserFuncGuid);
		            }
		        }
		    } catch (Exceptions::Exception e) {
		        Arte::Trace.warning("Error on load library functions xml from database.\n" + Arte::Trace.exceptionStackToString(e), Arte::EventSource:UserFunc);
		    } finally {
		        cursor.close();
		    }
		}

		//Load all used database entities titles.
		if (!usedDbEntities.isEmpty()) {
		    Types::Id entityId;
		    String entityPid;
		    String entityTitle;
		    final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		    for (String entityInfo : usedDbEntities) {
		        String[] idAndPid = entityInfo.split("~", 2);
		        entityId = Meta::Utils.getEntityIdByClassId(Types::Id.Factory.loadFrom(idAndPid[0]));
		        entityPid = idAndPid[1];
		        try {
		            Types::Entity e = Types::Entity.load(entityId, entityPid);
		            if (e != null) {
		                sb.setLength(0);
		                sb.append(entityId.toString()).append("~").append(idAndPid[1]).append("=").append(e.calcTitle());
		                internal[optimizerCache].add(sb.toString());
		            }
		        } catch (Exceptions::Throwable e) {
		            Arte::Trace.warning("Error on load database entity title.\n" 
		                + Arte::Trace.exceptionStackToString(e), Arte::EventSource:UserFunc);
		        }
		    }
		}
		return internal[optimizerCache];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:optimizerCache")
	private final   void setOptimizerCache(org.radixware.kernel.common.types.ArrStr val) {
		optimizerCache = val;
	}

	/*Radix::UserFunc::UserFunc:ownerPipelineId-Dynamic Property*/



	protected Int ownerPipelineId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPipelineId")
	public  Int getOwnerPipelineId() {

		return findOwnerPipelineId();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPipelineId")
	public   void setOwnerPipelineId(Int val) {
		ownerPipelineId = val;
	}

	/*Radix::UserFunc::UserFunc:ownerLibFuncName-Parent Property*/






















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerLibFuncName")
public  Str getOwnerLibFuncName() {
	return ownerLibFuncName;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerLibFuncName")
public   void setOwnerLibFuncName(Str val) {

	if (upOwnerPid != null && !upOwnerPid.isEmpty()) { //dirty hack for RADIX-12719
	    internal[ownerLibFuncName] = val;
	}
}

/*Radix::UserFunc::UserFunc:isOwnerCfgRef-Dynamic Property*/



protected Bool isOwnerCfgRef=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerCfgRef")
public  Bool getIsOwnerCfgRef() {

	return findOwner() instanceof CfgManagement::ICfgReferencedObject;
}

/*Radix::UserFunc::UserFunc:changeLog-User Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:changeLog")
public published  org.radixware.ads.CfgManagement.server.ChangeLog getChangeLog() {
	return changeLog;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:changeLog")
public published   void setChangeLog(org.radixware.ads.CfgManagement.server.ChangeLog val) {
	changeLog = val;
}

/*Radix::UserFunc::UserFunc:isOwnerWasNotCreated-Dynamic Property*/



protected Bool isOwnerWasNotCreated=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerWasNotCreated")
public published  Bool getIsOwnerWasNotCreated() {
	return isOwnerWasNotCreated;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerWasNotCreated")
public published   void setIsOwnerWasNotCreated(Bool val) {
	isOwnerWasNotCreated = val;
}

/*Radix::UserFunc::UserFunc:upOwnerKnownFromImport-Dynamic Property*/



protected org.radixware.ads.Types.server.Entity upOwnerKnownFromImport=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerKnownFromImport")
private final  org.radixware.ads.Types.server.Entity getUpOwnerKnownFromImport() {
	return upOwnerKnownFromImport;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerKnownFromImport")
private final   void setUpOwnerKnownFromImport(org.radixware.ads.Types.server.Entity val) {
	upOwnerKnownFromImport = val;
}

/*Radix::UserFunc::UserFunc:versionLockCount-Dynamic Property*/



protected int versionLockCount=0;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:versionLockCount")
protected  int getVersionLockCount() {
	return versionLockCount;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:versionLockCount")
protected   void setVersionLockCount(int val) {
	versionLockCount = val;
}

/*Radix::UserFunc::UserFunc:changeLogImportFromJmlEditor-Dynamic Property*/



protected org.radixware.schemas.commondef.ChangeLogDocument changeLogImportFromJmlEditor=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:changeLogImportFromJmlEditor")
  org.radixware.schemas.commondef.ChangeLogDocument getChangeLogImportFromJmlEditor() {
	return changeLogImportFromJmlEditor;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:changeLogImportFromJmlEditor")
   void setChangeLogImportFromJmlEditor(org.radixware.schemas.commondef.ChangeLogDocument val) {
	changeLogImportFromJmlEditor = val;
}

/*Radix::UserFunc::UserFunc:initOfDuplicatedObj-Dynamic Property*/



protected Bool initOfDuplicatedObj=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:initOfDuplicatedObj")
private final  Bool getInitOfDuplicatedObj() {
	return initOfDuplicatedObj;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:initOfDuplicatedObj")
private final   void setInitOfDuplicatedObj(Bool val) {
	initOfDuplicatedObj = val;
}

/*Radix::UserFunc::UserFunc:inheritedDescription-Dynamic Property*/



protected Str inheritedDescription=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:inheritedDescription")
  Str getInheritedDescription() {

	if (isLinkUsed != null && isLinkUsed.booleanValue() && linkedLibFunc != null) {
	    return linkedLibFunc.upUserFunc.getDescriptionWithChangelogInfo(linkedLibFunc.description);
	}
	return null;
}

/*Radix::UserFunc::UserFunc:logInvoke-Dynamic Property*/



protected boolean logInvoke=true;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:logInvoke")
public  boolean getLogInvoke() {
	return logInvoke;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:logInvoke")
public   void setLogInvoke(boolean val) {
	logInvoke = val;
}

/*Radix::UserFunc::UserFunc:lastChangelogRevisionDate-Dynamic Property*/



protected java.sql.Timestamp lastChangelogRevisionDate=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastChangelogRevisionDate")
  java.sql.Timestamp getLastChangelogRevisionDate() {

	if (changeLog != null) {
	    return changeLog.getLastRevisionDate();
	}
	return null;
}

/*Radix::UserFunc::UserFunc:descriptionForSelector-Dynamic Property*/



protected Str descriptionForSelector=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:descriptionForSelector")
private final  Str getDescriptionForSelector() {

	if (description != null || lastChangelogRevisionDate != null) {
	    return getDescriptionWithChangelogInfo(description);
	}
	return null;
}

/*Radix::UserFunc::UserFunc:PROPS_CONTROLLED_BY_ACCESS_SYSTEM-Dynamic Property*/



protected static java.util.Collection<org.radixware.kernel.common.types.Id> PROPS_CONTROLLED_BY_ACCESS_SYSTEM=java.util.Collections.unmodifiableSet(new java.util.HashSet<Types::Id>() {
    {
        this.add(idof[UserFunc:javaRuntime]);
        this.add(idof[UserFunc:javaSrc]);
    }
});;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:PROPS_CONTROLLED_BY_ACCESS_SYSTEM")
private static final  java.util.Collection<org.radixware.kernel.common.types.Id> getPROPS_CONTROLLED_BY_ACCESS_SYSTEM() {
	return PROPS_CONTROLLED_BY_ACCESS_SYSTEM;
}

/*Radix::UserFunc::UserFunc:forbidBindingXmlCopy-Dynamic Property*/



protected boolean forbidBindingXmlCopy=false;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:forbidBindingXmlCopy")
private final  boolean getForbidBindingXmlCopy() {
	return forbidBindingXmlCopy;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:forbidBindingXmlCopy")
private final   void setForbidBindingXmlCopy(boolean val) {
	forbidBindingXmlCopy = val;
}





















































































































































































































































































































/*Radix::UserFunc::UserFunc:Methods-Methods*/

/*Radix::UserFunc::UserFunc:invoke-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:invoke")
protected published  java.lang.Object invoke (java.lang.Object[] paramVals) throws java.lang.Exception {
	refreshCache();

	if (isLinkUsed.booleanValue() && linkedLibFunc != null) {
	    return invokeLinkedFunc(paramVals);
	} else {
	    if (javaRuntime == null)
	        throw new UserFuncError(this, "not compiled");

	    Method mth = findJavaMethod();
	    if (mth == null)
	        throw new UserFuncError(this, "wrong method ID (can't find method) " + getMethodId().toString());

	    Class<?>[] paramTypes = mth.getParameterTypes();
	    if (logInvoke && Arte::Trace.getMinSeverity() <= Arte::EventSeverity:Debug.getValue().longValue()) {
	        Arte::Trace.debug("Invoke " + getInvokeDescr(), Arte::EventSource:UserFunc);
	    }
	    try {
	        Types::Id id = Types::Id.Factory.loadFrom(userClassGuid);
	        try {
	            return invokeImpl(paramTypes, paramVals);
	        } catch (org.radixware.kernel.server.arte.AdsClassLoader.UserDefinitionNotFoundError e) {
	            if (e.DefinitionId == id) {
	                reread();
	                try {
	                    return invokeImpl(paramTypes, paramVals);
	                } catch (org.radixware.kernel.server.arte.AdsClassLoader.UserDefinitionNotFoundError e2) {
	                    throw new DefinitionNotFoundError(e2.DefinitionId, e2);
	                }
	            } else {
	                throw new DefinitionNotFoundError(e.DefinitionId, e);
	            }
	        }
	    } catch (Exceptions::Exception e) {
	        java.lang.StackTraceElement[] stack = e.getStackTrace();
	        java.lang.StackTraceElement[] stack2 = new java.lang.StackTraceElement[stack.length + 1];
	        System.arraycopy(stack, 0, stack2, 1, stack.length);

	        String className = "UDF '" + id + ") " + ownerTitleQuick + "'";
	        String fileName = "'" + ownerPropertyName + "'";

	        if (upOwnerEntityId == idof[System::LibUserFunc].toString() && ownerLibFunc != null) {
	            className = "UDF '" + id + ") " + ownerTitleQuick + "' " + ownerLibFunc.libName + "::" + ownerLibFunc.profile;
	            fileName = "";
	        }

	        stack2[0] = new java.lang.StackTraceElement(className, fileName, "", 0);

	        stack = stack2;
	        boolean removeFirstElement = false;
	        for (int i = 1; i < stack.length; i++) {
	            if (stack[i] != null && stack[i].getFileName() != null && stack[i].getFileName().equals(userClassGuid + ".java")) {
	                className = "UDF '" + id + ") " + ownerTitleQuick + "'";
	                fileName = "'" + ownerPropertyName + "' (" + stack[i].getMethodName() + ")";

	                if (upOwnerEntityId == idof[System::LibUserFunc].toString() && ownerLibFunc != null) {
	                    className = "UDF '" + id + ") " + ownerTitleQuick + "' " + ownerLibFunc.libName + "::" + ownerLibFunc.profile;
	                    fileName = "";
	                }

	                stack[i] = new java.lang.StackTraceElement(className, fileName,
	                        stack[i].getFileName(), stack[i].getLineNumber());
	                removeFirstElement = true;
	            }
	        }
	        if (removeFirstElement) {
	            stack2 = new java.lang.StackTraceElement[stack.length - 1];
	            System.arraycopy(stack, 1, stack2, 0, stack2.length);
	            stack = stack2;
	        }

	        e.setStackTrace(stack);
	        throw e;
	    }
	}

}

/*Radix::UserFunc::UserFunc:getMethodId-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getMethodId")
protected abstract published  org.radixware.kernel.common.types.Id getMethodId ();

/*Radix::UserFunc::UserFunc:refreshCache-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:refreshCache")
protected published  boolean refreshCache () {
	if (versionLockCount == 0) {
	    return caching.refresh(lastUpdateTimeGetter);
	}
	return false;
}

/*Radix::UserFunc::UserFunc:getDisplayName-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getDisplayName")
public published  Str getDisplayName () {
	return ownerTitle+ "->"+ getDisplayProfile();
}

/*Radix::UserFunc::UserFunc:compile-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:compile")
public published  void compile (org.radixware.kernel.common.check.IProblemHandler problemHandler) {
	compile(problemHandler,true,false);
}

/*Radix::UserFunc::UserFunc:findUserFunc-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:findUserFunc")
protected  org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef findUserFunc () {
	if (userFunc == null) {
	    org.radixware.kernel.common.repository.Branch branch = null;
	    try {
	        branch = Arte::Arte.getDefManager().ReleaseCache.Release.Repository.getBranch();
	    } catch (Exceptions::IOException e) {
	        return null;
	    }

	    Str classIdAsStr = classGuid;

	    if (classIdAsStr == null)
	        return null;

	    Types::Id methodId = getMethodId();

	    if (methodId == null/* && !(this instanceof )*/)
	        return null;

	    Types::Id classId = Types::Id.Factory.loadFrom(classIdAsStr);

	    Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xDoc = javaSrc;

	    Types::Id userClassGuidId = Types::Id.Factory.loadFrom(userClassGuid);
	    Types::Id ownerEntityId = Types::Id.Factory.loadFrom(upOwnerClassId);
	    userFunc = org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.lookup(branch, classId, methodId, userClassGuidId, ownerEntityId, upOwnerPid,
	            new AdsUserFuncDef.LibFuncNameGetter() {
	                public String getLibFuncName() {
	                    return ownerLibFuncName;
	                }
	            }, xDoc == null ? null : xDoc.AdsUserFuncDefinition, null, new ServerUdsObserver());

	}

	return userFunc;
}

/*Radix::UserFunc::UserFunc:writeClob-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:writeClob")
protected static  java.sql.Clob writeClob (org.apache.xmlbeans.XmlObject obj) {
	return writeClob(obj.xmlText());
}

/*Radix::UserFunc::UserFunc:writeClob-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:writeClob")
protected static  java.sql.Clob writeClob (Str obj) {
	Clob clob = Arte::Arte.createTemporaryClob();

	try{
	    clob.setString(1,obj);
	}catch(Exceptions::SQLException e){
	     Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e),Arte::EventSource:UserFunc);
	}

	return clob;
}

/*Radix::UserFunc::UserFunc:getCurrentVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getCurrentVersion")
public published  Int getCurrentVersion () {
	return 0;
}

/*Radix::UserFunc::UserFunc:getInitialText-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getInitialText")
protected published  Str getInitialText () {
	return "throw new UnsupportedOperationException(\"Not supported yet.\");";
}

/*Radix::UserFunc::UserFunc:afterInit-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:afterInit")
protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
	if (userClassGuid == null)
	    userClassGuid = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:USER_FUNC_CLASS).toString();

	if (src instanceof UserFunc) {
	    version = ((UserFunc) src).version;
	    if (org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.canSafelyCopyRuntime(this,javaSrc, ((UserFunc) src).javaRuntime))  {
	        javaRuntime = ((UserFunc) src).javaRuntime;
	        userClassGuid = ((UserFunc) src).userClassGuid;
	    } else {
	        javaRuntime = null;
	    }

	} else {
	    version = getCurrentVersion();
	    // = null;
	}

	super.afterInit(src, phase);
}

/*Radix::UserFunc::UserFunc:beforeCreate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:beforeCreate")
protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
	initOfDuplicatedObj = false;
	lastUpdateTime = new DateTime(System.currentTimeMillis());
	lastUpdateUserName = Arte::Arte.getUserName();
	appendToUsedDefinitionsList(libFuncGuid);

	if (isOwnerWasNotCreated==Bool.TRUE && upOwnerPid!=null){   
	    final Types::Entity owner = findOwner();
	    if (owner!=null){
	        if (javaSrcVers != null) {
	            try {
	                final String javaSrc = javaSrcVers.getSubString(1, (int) javaSrcVers.length());
	                final org.radixware.schemas.adsdef.UserFuncSourceVersions xSrcVersions = 
	                    org.radixware.schemas.adsdef.UserFuncSourceVersions.Factory.parse(javaSrc);
	                if (actualizeUpOwnerPid(xSrcVersions, owner)){
	                    try (java.io.StringWriter writer = new java.io.StringWriter()){
	                        xSrcVersions.save(writer);
	                        javaSrcVers = writeClob(writer.getBuffer().toString());
	                    }
	                }            
	            } catch (Exceptions::IOException e) {
	                Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	            } catch (Exceptions::XmlException e) {
	                Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	            } catch (Exceptions::SQLException e) {
	                Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	            }
	        }        
	        actualizeUpOwnerPid(javaSrc,owner);
	    }
	}

	if (src != null && upOwnerPid != null) {
	    Clob val = paramBinding;
	    if (val != null) {
	        try {
	            final UserFunc srcFunc = (UserFunc) src;
	            Reports::ReportsXsd:ParametersBindingDocument xDoc = Reports::ReportsXsd:ParametersBindingDocument.Factory.parse(val.CharacterStream);
	            if (actualizeUserFuncOwnerBinding(xDoc.ParametersBinding, Types::Id.Factory.loadFrom(upOwnerClassId), srcFunc.upOwnerPid, upOwnerPid)) {
	                final Clob newVal = Arte::Arte.createTemporaryClob();
	                try (java.io.Writer writer = newVal.setCharacterStream(1); java.io.Reader reader = xDoc.newReader()) {
	                    org.apache.commons.io.IOUtils.copy(reader, writer);
	                }
	                paramBinding = newVal;
	                forbidBindingXmlCopy = true;
	            }
	        }catch (Exceptions::IOException | Exceptions::SQLException | Exceptions::XmlException e) {
	            Arte::Trace.error("Error on actualize user func owner binding: " + Arte::Trace.exceptionStackToString(e), Arte::EventSource:UserFunc);
	        }
	    }
	}


	return super.beforeCreate(src);
}

/*Radix::UserFunc::UserFunc:beforeUpdate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:beforeUpdate")
protected published  boolean beforeUpdate () {
	long curTime = System.currentTimeMillis();
	lastUpdateTime = new DateTime(curTime);
	lastUpdateUserName = Arte::Arte.getUserName();
	// = ();
	profileTitle = null;
	java.util.Calendar cal = java.util.Calendar.getInstance();
	cal.setTimeInMillis(curTime);
	if (editedSourceVersions != null && javaSrcVers != null) {
	    Clob javaSrcVetrionsClob = javaSrcVers;
	    if (javaSrcVetrionsClob != null) {
	        try {
	            String src = javaSrcVers.getSubString(1, (int) javaSrcVers.length());
	            org.radixware.schemas.adsdef.UserFuncSourceVersions xSrcVersions = org.radixware.schemas.adsdef.UserFuncSourceVersions.Factory.parse(src);
	            if (xSrcVersions.SourceVersionList != null) {

	                for (Str sourceVersionName : editedSourceVersions) {
	                    for (org.radixware.schemas.adsdef.UserFuncSourceVersions.SourceVersion sourceVersion : xSrcVersions.SourceVersionList) {
	                        if (sourceVersion.Name.equals(sourceVersionName)) {
	                            sourceVersion.LastUpdateTime = cal;
	                            sourceVersion.LastUpdateUserName = lastUpdateUserName;
	                        }
	                    }
	                }
	                java.io.StringWriter writer = new java.io.StringWriter();
	                writer = new java.io.StringWriter();
	                xSrcVersions.save(writer);
	                javaSrcVers = writeClob(writer.getBuffer().toString());
	                writer.close();
	            }
	        } catch (Exceptions::IOException e) {
	            Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	        } catch (Exceptions::XmlException e) {
	            Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	        } catch (Exceptions::SQLException e) {
	            Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	        }
	    }
	    editedSourceVersions.clear();
	}

	if (javaSrc != null && javaSrc.AdsUserFuncDefinition != null) {
	    javaSrc.AdsUserFuncDefinition.LastUpdateUserName = lastUpdateUserName;
	    javaSrc.AdsUserFuncDefinition.LastUpdateTime = cal;
	}

	appendToUsedDefinitionsList(libFuncGuid);

	if (changeLogImportFromJmlEditor != null) {
	    try {
	        CfgManagement::ChangeLog.import(this, idof[UserFunc:changeLog], true,
	                changeLogImportFromJmlEditor.ChangeLog, null);
	    } finally {
	        changeLogImportFromJmlEditor = null;
	    }
	}

	return super.beforeUpdate();
}

/*Radix::UserFunc::UserFunc:compile-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:compile")
public  java.sql.Blob compile (org.radixware.ads.UserFunc.common.UserFuncCompiler compiler, org.radixware.kernel.common.check.IProblemHandler problemHandler, boolean resetRuntime, boolean fakeResult) {
	Blob blob = null;
	final org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef userFunc = findUserFunc();
	if (userFunc != null) {
	    try {
	        byte[] jarBytes = compiler.compile(userFunc, problemHandler, fakeResult);
	        if (!fakeResult) {
	            if (jarBytes != null || resetRuntime) {
	                try {
	                    if (jarBytes == null) {
	                        javaRuntime = null;
	                    } else {
	                        blob = Arte::Arte.createTemporaryBlob();
	                        blob.setBytes(1, jarBytes);
	                        javaRuntime = blob;
	                    }
	                    userClassGuid = userFunc.getId().toString();
	                } catch (Exceptions::SQLException e) {
	                    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	                }
	            }
	        }
	    } finally {
	        userFunc.resetCompileTimeCaches();
	    }
	}
	return blob;

}

/*Radix::UserFunc::UserFunc:afterUpdate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:afterUpdate")
protected published  void afterUpdate () {
	super.afterUpdate();

	if (ownerLibFunc != null) {
	    profileTitle = null;
	    findUserFunc();
	    ownerLibFunc.profile = displayProfile;
	    ownerLibFunc.update();
	}

	paramsOrderCache = null;
	upOwnerKnownFromImport = null;

	Arte::Trace.put(//RADIX-4401
	        eventCode["User '%1' modified user-defined function '%2'"],
	        Arte::Arte.getUserName(),
	        getDisplayName());


}

/*Radix::UserFunc::UserFunc:onCalcTitle-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:onCalcTitle")
protected published  Str onCalcTitle (Str title) {
	String res = title;
	if (description != null && !description.isEmpty()) {
	    res += description;
	} else {
	    if (isLinkUsed != null && isLinkUsed.booleanValue() && linkedLibFunc != null
	            && linkedLibFunc.description != null && !linkedLibFunc.description.isEmpty()) {
	        res += linkedLibFunc.description;
	        return linkedLibFunc.upUserFunc.getDescriptionWithChangelogInfo(res);
	    } else {
	        res += "<function defined>";
	    }
	}
	return UserFuncUtils.getDescriptionWithLastRevisionDate(res, lastChangelogRevisionDate);
}

/*Radix::UserFunc::UserFunc:compile-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:compile")
private final  void compile (org.radixware.kernel.common.check.IProblemHandler problemHandler, boolean resetRuntime, boolean fakeResult) {
	UserFuncCompiler compiler = new UserFuncCompiler();
	try {
	    compile(compiler, problemHandler, resetRuntime, fakeResult);
	} finally {
	    org.radixware.kernel.common.repository.Branch branch = null;
	    try {
	        branch = Arte::Arte.getDefManager().ReleaseCache.Release.Repository.getBranch();
	    } catch (Exceptions::IOException e) {
	    }
	    compiler.close(branch);
	}


}

/*Radix::UserFunc::UserFunc:extSrcChanged-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:extSrcChanged")
private final  void extSrcChanged () {

}

/*Radix::UserFunc::UserFunc:getDisplayProfile-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getDisplayProfile")
private final  Str getDisplayProfile () {
	if (profileTitle == null) {
	    try {
	        if (classGuid == null || (initOfDuplicatedObj != null && initOfDuplicatedObj == true)) {
	            profileTitle = UNDEFINED_METHOD_PROFILE_TITLE;
	        } else {
	            org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.Profile profile = null;
	            if (userFunc != null) {
	                profile = userFunc.findProfile();
	            } else {
	                if (idof[System::LibUserFunc].toString().equals(upOwnerEntityId) && this instanceof UserFunc.FreeForm) {
	                    LibUserFunc libFunc = (LibUserFunc) LibUserFunc.load(idof[System::LibUserFunc], upOwnerPid);
	                    profileTitle = libFunc.profile;
	                } else {
	                    org.radixware.kernel.common.repository.Branch branch;
	                    try {
	                        branch = Arte::Arte.getDefManager().ReleaseCache.Release.Repository.getBranch();
	                    } catch (Exceptions::IOException e) {
	                        Arte::Trace.warning("On update user function profile:\n" + Arte::Trace.exceptionStackToString(e), Arte::EventSource:UserFunc);
	                        profileTitle = UNDEFINED_METHOD_PROFILE_TITLE;
	                        return profileTitle;
	                    }
	                    
	                    Types::Id classGuid = Types::Id.Factory.loadFrom(classGuid);
	                    Types::Id methodId = getMethodId();
	                    if (methodId != null) {
	                        org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef method = org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.findMethod(branch, classGuid, methodId);
	                        if (method != null) {
	                            profile = method.Profile;
	                        }
	                    }
	                }
	            }

	            if (profileTitle == null) {
	                if (profile == null) {
	                    profileTitle = UNDEFINED_METHOD_PROFILE_TITLE;
	                } else {
	                    if (ownerLibFunc != null) {
	                        profileTitle = profile.getNameForSelector(ownerLibFunc.name);
	                    } else
	                        profileTitle = profile.getName();
	                }
	            }
	        }
	    } catch (Exceptions::Throwable e) {
	        Arte::Trace.warning("On update user function profile:\n" + Arte::Trace.exceptionStackToString(e), Arte::EventSource:UserFunc);
	        profileTitle = UNDEFINED_METHOD_PROFILE_TITLE;
	    }
	}
	return profileTitle;
}

/*Radix::UserFunc::UserFunc:baseTest-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:baseTest")
  void baseTest () {

}

/*Radix::UserFunc::UserFunc:export-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:export")
@Deprecated
public published  java.sql.Clob export () {
	Clob clob = null;
	try {
	    clob = Arte::Arte.getInstance().getDbConnection().createTemporaryClob();
	    java.io.OutputStream out = clob.setAsciiStream(1L);
	    org.radixware.kernel.common.utils.XmlFormatter.save(export(), out);
	    out.flush(); out.close();
	} catch (Exceptions::Exception e) {
	    Arte::Trace.error(e.getMessage(), Arte::EventSource:App);    
	}
	return clob;
}

/*Radix::UserFunc::UserFunc:import-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:import")
@Deprecated
public published  void import (java.sql.Clob clob) {
	if (clob == null)
	    return;

	try {
	    Common::CommonXsd:UserFunc xFunc = Common::CommonXsd:UserFunc.Factory.parse(clob.getCharacterStream());
	    import(xFunc, false, false, null, null);
	} catch (Exceptions::Exception e) {
	    Arte::Trace.error(e.getMessage(), Arte::EventSource:App);
	}
}

/*Radix::UserFunc::UserFunc:listUserDefinitions-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:listUserDefinitions")
  org.radixware.schemas.userfunccommands.ReadUserDefHeadersRsDocument listUserDefinitions (org.radixware.ads.Meta.common.DefType.Arr request) {
	java.util.Set<Meta::DefType> set = java.util.EnumSet.copyOf(request);

	ServerUserDefRequestor rq = new ServerUserDefRequestor();

	CommandsXsd:ReadUserDefHeadersRsDocument xDoc  = CommandsXsd:ReadUserDefHeadersRsDocument.Factory.newInstance();
	final CommandsXsd:ReadUserDefHeadersRsDocument.ReadUserDefHeadersRs xList = xDoc.addNewReadUserDefHeadersRs();
	rq.readUserDefHeaders(set, new org.radixware.kernel.common.defs.ads.userfunc.UdsObserver.IUserDefReceiver() {
	    public void accept(Types::Id id, String name, String moduleName, Types::Id moduleId) {
	        CommandsXsd:ReadUserDefHeadersRsDocument.ReadUserDefHeadersRs.DefInfo xInfo = xList.addNewDefInfo();
	        xInfo.Id = id;
	        xInfo.Name = name;
	        xInfo.ModuleName = moduleName;
	        xInfo.ModuleId = moduleId;        
	    }
	    
	  
	});

	return xDoc;
}

/*Radix::UserFunc::UserFunc:loadUserClassXml-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:loadUserClassXml")
public  org.radixware.schemas.adsdef.AdsDefinitionDocument loadUserClassXml (Str classId) {

	if (classId == null) {
	    return null;
	}

	try {
	    if (classId.startsWith("rpu")) {
	        Reports.User::UserReport report = Reports.User::UserReport.loadByPK(classId, false);
	        if (report == null) {
	            return null;
	        }
	        Reports.User::UserReportVersion currentVersion = report.currentVersion;;
	        if (currentVersion == null) {
	            return null;
	        }
	        org.radixware.schemas.adsdef.AdsUserReportDefinitionDocument reportSrc = currentVersion.reportSource;
	        if (reportSrc == null || reportSrc.AdsUserReportDefinition == null) {
	            return null;
	        }
	        org.radixware.schemas.adsdef.AdsDefinitionDocument xDoc = org.radixware.schemas.adsdef.AdsDefinitionDocument.Factory.newInstance();
	        final Types::Id reportId = Types::Id.Factory.loadFrom(report.guid);
	        xDoc.addNewAdsDefinition().AdsClassDefinition = Reports.User::AdsUserReportClassDef.
	                getReportDefWithCorrectIds(reportSrc.AdsUserReportDefinition, reportId, currentVersion.version.intValue(), true).Report;
	        xDoc.AdsDefinition.AdsClassDefinition.Name = report.moduleName + "::" + report.name;
	        xDoc.AdsDefinition.AdsClassDefinition.Description = "Version #" + report.versionOrder + " (" + report.version + ")" + Reports.User::AdsWrapperClassDef.END_OF_VERSION_INFO_MARKER + report.descirption;
	        return xDoc;
	    }
	} catch (Exceptions::EntityObjectNotExistsError e) {
	    return null;
	}
	return null;
}

/*Radix::UserFunc::UserFunc:loadUserFuncXml-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:loadUserFuncXml")
public  org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument loadUserFuncXml (Str libUfId) {
	Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xDoc = null;
	Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.AdsUserFuncDefinition xDef = LibUserFunc.getXml(libUfId);
	if (xDef != null) {
	    xDoc = Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.Factory.newInstance();
	    xDoc.addNewAdsUserFuncDefinition().set(xDef);
	}
	return xDoc;
}

/*Radix::UserFunc::UserFunc:readParameterNames-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:readParameterNames")
protected  org.radixware.kernel.common.types.ArrStr readParameterNames () {
	org.radixware.kernel.server.meta.clazzes.RadMethodDef method = RadMeta.getMethodById(getMethodId());
	ArrStr result = new ArrStr();

	for(org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter param : method.Params){
	    result.add(param.name);
	}

	return result;
}

/*Radix::UserFunc::UserFunc:invoke-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:invoke")
public  java.lang.Object invoke (java.util.Map<java.lang.String,java.lang.Object> params) throws java.lang.Exception {
	ArrStr paramNames = parameterNames;
	Object[] paramVals = new Object[paramNames.size()];
	int i = 0;
	for(String p : paramNames){
	    paramVals[i] = params.get(p);
	    i++;
	}

	return invoke(paramVals);
}

/*Radix::UserFunc::UserFunc:findJavaMethod-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:findJavaMethod")
public  java.lang.reflect.Method findJavaMethod () {
	return Meta::Utils.getMethod(this.Class, getMethodId());
}

/*Radix::UserFunc::UserFunc:beforeInit-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:beforeInit")
protected published  boolean beforeInit (org.radixware.kernel.server.types.PropValHandlersByIdMap initPropValsById, org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
	UserFunc srcFunc = null;
	if (src instanceof UserFunc) {
	    srcFunc = (UserFunc) src;
	    initOfDuplicatedObj = true;
	}

	javaSrc = getJavaSrcInitValue(srcFunc);
	if (getMethodId() != null) {
	    javaSrc.AdsUserFuncDefinition.MethodId = getMethodId();
	}
	if (src == null) { //Avoid modify copied src, RADIX-14764
	    javaSrc.AdsUserFuncDefinition.ClassId = this.getClassDefinitionId();
	}

	return super.beforeInit(initPropValsById, src, phase);
}

/*Radix::UserFunc::UserFunc:invokeImpl-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:invokeImpl")
private final  java.lang.Object invokeImpl (java.lang.Class<?>[] paramTypes, java.lang.Object[] paramVals) throws java.lang.Exception {
	java.lang.reflect.Field ownerField = null;
	try {
	    final Types::Id id = Types::Id.Factory.loadFrom(userClassGuid);
	    Class<?> clazz = Arte::Arte.getDefManager().getClass(id);
	    java.lang.reflect.Field field = clazz.getDeclaredField(String.valueOf(org.radixware.kernel.common.jml.JmlTagDbEntity.UF_OWNER_FIELD_NAME));
	    field.setAccessible(true);
	    field.set(null, findOwner());
	    ownerField = field;
	} catch (Exception ex) {
	}

	final java.lang.StringBuilder timingSectionSb = new java.lang.StringBuilder();
	    timingSectionSb
	            .append(findOwner().RadMeta.TableDef.getName())
	            .append('~')
	            .append(upOwnerPid)
	            .append('.')
	            .append(ownerPropertyName);
	final Str timingSection = timingSectionSb.toString();
	org.radixware.kernel.server.monitoring.ArteWaitStats beforeStats = null;
	try {

	    for (int i = 0; i < paramTypes.length; i++) {

	        Class<?> type = paramTypes[i];
	        Object value = paramVals[i];
	        if (Blob.class.isAssignableFrom(type) && value instanceof byte[]) {
	            Blob blob = Arte::Arte.createTemporaryBlob();
	            blob.setBytes(1, (byte[]) value);
	            paramVals[i] = blob;
	        }
	        if (byte[].class.isAssignableFrom(type) && value instanceof Blob) {
	            Blob blob = (Blob) value;
	            paramVals[i] = blob.getBytes(1, (int) blob.length());
	        }
	    }
	    
	    beforeStats = Arte::Arte.getInstance().getProfiler().WaitStatsSnapshot;
	    Arte::Arte.enterTimingSection(Profiler::TimingSection:RDX_ARTE_USER_FUNC_INVOKE, timingSection);
	    
	    return Arte::Arte.invokeByClassId(Types::Id.Factory.loadFrom(userClassGuid), getMethodId().toString(), paramTypes, paramVals);
	} finally {
	    Arte::Arte.leaveTimingSection(Profiler::TimingSection:RDX_ARTE_USER_FUNC_INVOKE, timingSection);
	    if (logInvoke && Arte::Trace.getMinSeverity() <= Arte::EventSeverity:Debug.getValue().longValue()) {
	        if (beforeStats != null) {
	            Arte::Trace.debug("Invoke " + getInvokeDescr() + " finished, duration: " +
	                Utils::ArteWaitStatsUtils.toStr(beforeStats), Arte::EventSource:UserFunc);
	        }
	    }
	    if (ownerField != null) {
	        ownerField.set(null, null);
	    }
	}
}

/*Radix::UserFunc::UserFunc:findOwner-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:findOwner")
public  org.radixware.ads.Types.server.Entity findOwner () {
	if (upOwnerKnownFromImport != null) {
	    return upOwnerKnownFromImport;
	}
	try {
	    return Types::Entity.load(Types::Id.Factory.loadFrom(upOwnerEntityId), upOwnerPid);
	} catch (Exceptions::Exception ex) {
	    return null;
	}
}

/*Radix::UserFunc::UserFunc:invokeLinkedFunc-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:invokeLinkedFunc")
public  java.lang.Object invokeLinkedFunc (java.lang.Object[] paramVals) throws java.lang.Exception {
	java.util.List<Object> result = new java.util.LinkedList<Object>();

	Reports::ReportsXsd:ParametersBindingDocument parametersBindingDoc = null;
	if (paramBinding != null) {
	    String src = paramBinding.getSubString(1, (int) paramBinding.length());
	    parametersBindingDoc = Reports::ReportsXsd:ParametersBindingDocument.Factory.parse(src);
	}

	Meta::AdsDefXsd:ParameterDeclaration[] paramIds = linkedLibFunc.upUserFunc.paramsOrder;

	for (int i = 0; i < paramIds.length; i++) {

	    Meta::AdsDefXsd:ParameterDeclaration p = paramIds[i];
	    Types::Id paramId = p.Id;
	    final Reports::ReportsXsd:ParametersBindingType.ParameterBinding parameterBinding = Reports::ReportsServerUtils.findParameterBindingById(parametersBindingDoc, paramId);
	    if (parameterBinding != null) {
	        if (parameterBinding.isSetValue()) {
	            if ((p.Type.TypeId == Meta::ValType:JavaClass || p.Type.TypeId == Meta::ValType:JavaType) && (p.Type.getExtStr() != null)) {
	                String strType = p.Type.getExtStr();

	                if (strType.equals("char")) {
	                    result.add(Character.valueOf(parameterBinding.Value.Str.charAt(0)));
	                } else if (strType.equals("java.lang.String")) {
	                    result.add(parameterBinding.Value.Str);
	                } else if (strType.equals("int") || strType.equals("java.lang.Integer")) {
	                    result.add(parameterBinding.Value.Int.intValue());
	                } else if (strType.equals("long") || strType.equals("java.lang.Long")) {
	                    result.add(parameterBinding.Value.Int.longValue());
	                } else if (strType.equals("short") || strType.equals("java.lang.Short")) {
	                    result.add(parameterBinding.Value.Int.shortValue());
	                } else if (strType.equals("float") || strType.equals("java.lang.Float")) {
	                    result.add(parameterBinding.Value.Num.floatValue());
	                } else if (strType.equals("double") || strType.equals("java.lang.Double")) {
	                    result.add(parameterBinding.Value.Num.doubleValue());
	                } else if (strType.equals("boolean") || strType.equals("java.lang.Boolean")) {
	                    result.add(parameterBinding.Value.Bool.booleanValue());
	                } else {
	                    throw new AppError("Unsupported type of parameter " + p.Name + ": " + strType + ". Invoke library function " + linkedLibFunc.fullProfile + " from UDF '" + ownerTitleQuick + "'.'" + ownerPropertyName + "'");
	                }
	            } else {
	                final Object parameterValue = org.radixware.kernel.common.client.utils.ValueConverter.easPropXmlVal2ObjVal(parameterBinding.Value, p.Type.TypeId, null);
	                result.add(parameterValue);
	            }
	        } else if (parameterBinding.isSetParameter()) {
	            Types::Id callerParameterId = parameterBinding.Parameter;
	            org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef adsUserFunc = findUserFunc();
	            if (adsUserFunc != null) {
	                org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef adsMethod = adsUserFunc.findMethod();
	                if (adsMethod != null) {
	                    org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodParameters adsParams = adsMethod.Profile.ParametersList;
	                    int index = -1;
	                    for (int j = 0; j < adsParams.size(); j++) {
	                        if (adsParams.get(j).Id.equals(callerParameterId)) {
	                            index = j;
	                            break;
	                        }
	                    }
	                    if (index >= 0 && index < paramVals.length) {
	                        result.add(paramVals[index]);
	                    } else {
	                        throw new AppError("Parameter with id #" + callerParameterId + " bound to library function parameter " + p.Name + " (#" + paramId + ") not found. Invoke library function " + linkedLibFunc.fullProfile + " from UDF '" + ownerTitleQuick + "'.'" + ownerPropertyName + "'");
	                    }
	                }
	            } else {
	                throw new AppError("Can not pass parameter with id #" + callerParameterId + " bound to library function parameter " + p.Name + " (#" + paramId + "), caller definition not found. Invoke library function " + linkedLibFunc.fullProfile + " from UDF '" + ownerTitleQuick + "'.'" + ownerPropertyName + "'");
	            }
	        } else if (parameterBinding.isSetExternalValue()) {
	            java.lang.Object prop = null;
	            if (parameterBinding.ExternalValue.isSetOwnerPID()) {
	                String sPid = parameterBinding.ExternalValue.OwnerPID;
	                Types::Id tableId = parameterBinding.ExternalValue.OwnerClassId;
	                if (tableId.getPrefix() == org.radixware.kernel.common.enums.EDefinitionIdPrefix.ADS_ENTITY_CLASS) {
	                    tableId = Types::Id.Factory.changePrefix(tableId, org.radixware.kernel.common.enums.EDefinitionIdPrefix.DDS_TABLE);
	                } else if (tableId.getPrefix() != Meta::DefinitionIdPrefix:DDS_TABLE) {
	                    Meta::ClassDef clazz = Arte::Arte.getInstance().DefManager.getClassDef(tableId);
	                    tableId = clazz.getEntityId();
	                }

	                Types::Pid pid = new Pid(Arte::Arte.getInstance(), tableId, sPid);
	                Types::Entity owner = Types::Entity.load(pid);
	                Types::Id propId = parameterBinding.ExternalValue.Value == null ? null : parameterBinding.ExternalValue.Value.Id;
	                if (propId != null) {
	                    prop = owner.getProp(parameterBinding.ExternalValue.Value.Id);
	                    result.add(prop);
	                } else {
	                    result.add(owner);
	                }
	            } else if (p.Type.Path != null && !p.Type.Path.isEmpty() && p.Type.Path.get(0).getPrefix() == Meta::DefinitionIdPrefix:ADS_ENUMERATION) {
	                Types::Id classId = p.Type.Path.get(0);
	                if (classId.getPrefix() == Meta::DefinitionIdPrefix:ADS_ENUMERATION) {
	                    Meta::EnumDef enumDef = Arte::Arte.getInstance().DefManager.getEnumDef(classId);
	                    if (enumDef != null) {
	                        prop = org.radixware.kernel.common.client.utils.ValueConverter.easPropXmlVal2ObjVal(parameterBinding.ExternalValue.Value, p.Type.TypeId, null);
	                        if (prop == null) {
	                            result.add(null);
	                        } else {
	                            if (p.Type.TypeId.isArrayType()) {
	                                Class<?> enumClass = ((org.radixware.kernel.server.arte.AdsClassLoader) getClass().getClassLoader()).loadMetaClassById(enumDef.Id);
	                                String arrClassName = enumClass.getName().substring(0, enumClass.getName().length() - 3) + "$" + "Arr";
	                                Class<?> arrClass = getClass().getClassLoader().loadClass(arrClassName);
	                                java.lang.reflect.Constructor<?> constructor = arrClass.getConstructor(prop.getClass());
	                                result.add(constructor.newInstance(prop));
	                            } else {
	                                Meta::EnumDef.Item enumItem = enumDef.getItemByVal((Comparable<?>) prop);
	                                if (enumItem != null) {
	                                    result.add(enumItem.Constant);
	                                } else {
	                                    throw new AppError("Enumeration item with value " + prop.toString() + " bound with parameter " + p.Name + " not found. Invoke library function " + linkedLibFunc.fullProfile + " from UDF '" + ownerTitleQuick + "'.'" + ownerPropertyName + "'");
	                                }
	                            }
	                        }
	                    } else {
	                        throw new AppError("Enumeration #" + classId + " bound with parameter " + p.Name + " not found. Invoke library function " + linkedLibFunc.fullProfile + " from UDF '" + ownerTitleQuick + "'.'" + ownerPropertyName + "'");
	                    }
	                }
	            } else {
	                if ((p.Type.TypeId == Meta::ValType:JavaClass || p.Type.TypeId == Meta::ValType:JavaType) && (p.Type.getExtStr() != null)) {
	                    String strType = p.Type.getExtStr();

	                    if (strType.equals("char")) {
	                        result.add(Character.valueOf(parameterBinding.ExternalValue.Value.Str.charAt(0)));
	                    } else if (strType.equals("java.lang.String")) {
	                        result.add(parameterBinding.ExternalValue.Value.Str);
	                    } else if (strType.equals("int") || strType.equals("java.lang.Integer")) {
	                        result.add(parameterBinding.ExternalValue.Value.Int.intValue());
	                    } else if (strType.equals("long") || strType.equals("java.lang.Long")) {
	                        result.add(parameterBinding.ExternalValue.Value.Int.longValue());
	                    } else if (strType.equals("short") || strType.equals("java.lang.Short")) {
	                        result.add(parameterBinding.ExternalValue.Value.Int.shortValue());
	                    } else if (strType.equals("float") || strType.equals("java.lang.Float")) {
	                        result.add(parameterBinding.ExternalValue.Value.Num.floatValue());
	                    } else if (strType.equals("double") || strType.equals("java.lang.Double")) {
	                        result.add(parameterBinding.ExternalValue.Value.Num.doubleValue());
	                    } else if (strType.equals("boolean") || strType.equals("java.lang.Boolean")) {
	                        result.add(parameterBinding.ExternalValue.Value.Bool.booleanValue());
	                    } else {
	                        throw new AppError("Unsupported type of parameter " + p.Name + ": " + strType + ". Invoke library function " + linkedLibFunc.fullProfile + " from UDF '" + ownerTitleQuick + "'.'" + ownerPropertyName + "'");
	                    }
	                } else {
	                    prop = org.radixware.kernel.common.client.utils.ValueConverter.easPropXmlVal2ObjVal(parameterBinding.ExternalValue.Value, p.Type.TypeId, null);
	                    result.add(prop);
	                }
	            }
	        } else {
	            result.add(null);
	        }
	    } else {
	        result.add(null);
	    }
	}

	linkedLibFunc.upUserFunc.logInvoke = this.logInvoke;
	return linkedLibFunc.upUserFunc.invoke(result.toArray());
}

/*Radix::UserFunc::UserFunc:diagnoseCompile-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:diagnoseCompile")
public published  boolean diagnoseCompile () {
	return diagnoseCompileImpl(null);

}

/*Radix::UserFunc::UserFunc:diagnoseCompileImpl-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:diagnoseCompileImpl")
protected published  boolean diagnoseCompileImpl (org.radixware.kernel.common.check.IProblemHandler problemHandler) {
	final boolean[] wasErrors = new boolean[]{false};

	final org.radixware.kernel.common.check.IProblemHandler handler = problemHandler;
	org.radixware.kernel.common.check.IProblemHandler diagnoseHandler = new org.radixware.kernel.common.check.IProblemHandler(){
	    public void accept(org.radixware.kernel.common.check.RadixProblem problem){
	        if(problem.Severity == org.radixware.kernel.common.check.RadixProblem.ESeverity.ERROR){
	            wasErrors[0] = true;
	        }
	        if(handler != null){
	            handler.accept(problem);
	        }
	    }
	};


	compile(diagnoseHandler,true,true);
	return wasErrors[0];
}

/*Radix::UserFunc::UserFunc:validateLinkedFuncParams-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:validateLinkedFuncParams")
public  void validateLinkedFuncParams (org.radixware.kernel.common.check.IProblemHandler problemPacker) throws java.lang.Throwable {
	if (libFuncGuid != null) {
	    AdsUserFuncDef uf = findUserFunc();
	    AdsUserFuncDef targetUf = linkedLibFunc == null || linkedLibFunc.upUserFunc == null ? null : linkedLibFunc.upUserFunc.findUserFunc();

	    //so we have a target, now check for loops in bindings
	    if (targetUf != null) {
	        try {
	            UserFunc current = linkedLibFunc.upUserFunc;
	            java.util.Set<String> pidsPath = new java.util.HashSet<String>();
	            pidsPath.add(this.Pid.toString());
	            pidsPath.add(current.Pid.toString());

	            while (current.libFuncGuid != null) {
	                if (current.linkedLibFunc == null) {
	                    break;
	                }
	                LibUserFunc funcObject = current.linkedLibFunc;
	                if(funcObject.upUserFunc==null){
	                    break;
	                }
	                Types::Pid ufPid = funcObject.upUserFunc.Pid;
	                if (pidsPath.contains(ufPid.toString())) {
	                    problemPacker.accept(org.radixware.kernel.common.check.RadixProblem.Factory.newError(targetUf, "Сyclic binding. Infinite recursion is possible"));                    
	                    break;
	                }
	                current = funcObject.upUserFunc;
	                if (current != null) {
	                    pidsPath.add(current.Pid.toString());
	                } else
	                    break;
	            }
	        } catch (Throwable e) {
	            //ignore
	        }
	    }

	    new ServerUserFuncBindingValidator().validateLinkedFuncParams(uf, targetUf, getPid().toString(), linkedLibFunc.upUserFunc.getPid().toString(), problemPacker, paramBinding == null ? null : paramBinding.getSubString(1, (int) paramBinding.length()));
	}
}

/*Radix::UserFunc::UserFunc:onCommand_MoveToLibrary-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:onCommand_MoveToLibrary")
public  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest onCommand_MoveToLibrary (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	MoveUserFuncToLibraryForm form = new MoveUserFuncToLibraryForm(null);
	form.source = this;
	return new FormHandlerNextDialogsRequest(null,form);

}

/*Radix::UserFunc::UserFunc:exportForExchange-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:exportForExchange")
public  org.radixware.schemas.udsdef.UserFunctionDefinition exportForExchange () {
	org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef userFunc = findUserFunc();

	if (userFunc != null) {
	    org.radixware.schemas.udsdef.UserFunctionDefinition xFunc = org.radixware.schemas.udsdef.UserFunctionDefinition.Factory.newInstance();
	    if (upOwnerClassId == idof[LibUserFunc].toString()) {
	        xFunc.setId(Types::Id.Factory.loadFrom(upOwnerPid));
	    } else {
	        xFunc.setId(userFunc.getId());
	    }
	    xFunc.setName(ownerPropertyFullName + " - " + ownerTitle);
	    if (description != null) {
	        xFunc.setDescription(description);
	    }
	    userFunc.Source.appendTo(xFunc.addNewSource(), org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode.NORMAL);
	    org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef bundle = userFunc.findExistingLocalizingBundle();
	    if (bundle != null) {
	        org.radixware.schemas.adsdef.LocalizingBundleDefinition xBundle = org.radixware.schemas.adsdef.LocalizingBundleDefinition.Factory.newInstance();
	        bundle.appendTo(xBundle, org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode.NORMAL);
	        xFunc.Strings = xBundle;
	    }
	    xFunc.setMethodId(Types::Id.Factory.loadFrom(methodId));
	    xFunc.setClassId(Types::Id.Factory.loadFrom(classGuid));
	    xFunc.setPropId(Types::Id.Factory.loadFrom(upDefId));
	    xFunc.setOwnerClassId(Types::Id.Factory.loadFrom(upOwnerClassId));

	    org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef method = userFunc.findMethod();
	    if (method != null) {
	        org.radixware.schemas.adsdef.UserFuncProfile xProfile = xFunc.addNewUserFuncProfile();
	        org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.Profile profile = method.Profile;
	        if (profile.ReturnValue != null && profile.ReturnValue.Type != null) {
	            profile.ReturnValue.Type.appendTo(xProfile.ensureReturnType());
	        }
	        xProfile.MethodName = method.Name;

	        if (profile.ParametersList != null && !profile.ParametersList.isEmpty()) {
	            org.radixware.schemas.adsdef.UserFuncProfile.Parameters xParams = xProfile.addNewParameters();
	            for (org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter p : profile.ParametersList) {
	                org.radixware.schemas.adsdef.ParameterDeclaration xPDecl = xParams.addNewParameter();
	                xPDecl.Name = p.Name;
	                if (p.Type != null) {
	                    p.Type.appendTo(xPDecl.addNewType());
	                }
	                xPDecl.Variable = p.isVariable();
	            }
	        }

	        if (profile.ThrowsList != null && !profile.ThrowsList.isEmpty()) {
	            org.radixware.schemas.adsdef.UserFuncProfile.ThrownExceptions xExs = xProfile.addNewThrownExceptions();
	            for (org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList.ThrowsListItem item : profile.ThrowsList) {
	                item.Exception.appendTo(xExs.addNewException());
	            }
	        }

	    }

	    return xFunc;
	} else
	    return null;


}

/*Radix::UserFunc::UserFunc:getUsedLibraryFunctions-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getUsedLibraryFunctions")
private final  org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.UserFunc.server.LibUserFunc> getUsedLibraryFunctions () {
	final Clob usedDefs = this.usedDefinitions;
	final org.radixware.kernel.server.types.ArrEntity<LibUserFunc> result = new org.radixware.kernel.server.types.ArrEntity<>(Arte::Arte.getInstance());
	if (usedDefs == null) {
	    return result;
	}
	try {
	    String usedDefsData = usedDefs.getSubString(1l, (int) usedDefs.length());
	    String[] ids = usedDefsData.split(" ");
	    for (String id : ids) {
	        LibUserFunc func = LibUserFunc.loadByPK(id, false);
	        if(func != null){
	            result.add(func);
	        }
	    }
	} catch (Exceptions::Throwable ex) {
	    Arte::Trace.error(ex.getMessage(),Arte::EventSource:UserFunc);
	}
	return result;
}

/*Radix::UserFunc::UserFunc:loadByPK-System Method*/

@SuppressWarnings("unused")
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:loadByPK")
public static published  org.radixware.ads.UserFunc.server.UserFunc loadByPK (Str upDefId, Str upOwnerEntityId, Str upOwnerPid, boolean checkExistance) {
final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(9);
		if(upDefId==null) return null;
		pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7QMAJND3ZHOBDCMTAALOMT5GDM"),upDefId);
		if(upOwnerEntityId==null) return null;
		pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UMAJND3ZHOBDCMTAALOMT5GDM"),upOwnerEntityId);
		if(upOwnerPid==null) return null;
		pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7YMAJND3ZHOBDCMTAALOMT5GDM"),upOwnerPid);
org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),pkValsMap);
	try{
	return (
	org.radixware.ads.UserFunc.server.UserFunc) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
return null;
}

}

/*Radix::UserFunc::UserFunc:loadByPidStr-System Method*/

@SuppressWarnings("unused")
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:loadByPidStr")
public static published  org.radixware.ads.UserFunc.server.UserFunc loadByPidStr (Str pidAsStr, boolean checkExistance) {
org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),pidAsStr);
	try{
	return (
	org.radixware.ads.UserFunc.server.UserFunc) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
return null;
}

}

/*Radix::UserFunc::UserFunc:import-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:import")
public static published  org.radixware.ads.UserFunc.server.UserFunc import (org.radixware.ads.Types.server.Entity obj, org.radixware.kernel.common.types.Id propId, boolean isSet, org.radixware.ads.Common.common.CommonXsd.UserFunc xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	return new UserFunc.UserFuncImporter(obj, propId, isSet, xml, helper).import();
}

/*Radix::UserFunc::UserFunc:findOwnerPipelineId-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:findOwnerPipelineId")
public  Int findOwnerPipelineId () {
	if (ownerLibFunc != null) {
	    return ownerLibFunc.ownerLib.pipelineId;
	}
	Types::Entity owner = findOwner();
	if (owner instanceof ServiceBus::Pipeline) {
	    ServiceBus::Pipeline pipeline = (ServiceBus::Pipeline) owner;
	    return pipeline.id;
	} else if (owner instanceof ServiceBus::PipelineNode) {
	    ServiceBus::PipelineNode node = (ServiceBus::PipelineNode) owner;
	    ServiceBus::Pipeline pipeline = (ServiceBus::Pipeline) node.getPipeline();
	    if (pipeline != null) {
	        return pipeline.id;
	    }
	}
	return null;
}

/*Radix::UserFunc::UserFunc:import-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:import")
@Deprecated
public static published  org.radixware.ads.UserFunc.server.UserFunc import (org.radixware.ads.Common.common.CommonXsd.UserFunc xml) {
	return new UserFunc.UserFuncImporter(xml).import();
}

/*Radix::UserFunc::UserFunc:export-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:export")
public published  org.radixware.ads.Common.common.CommonXsd.UserFunc export () {
	return export(javaSrc);
}

/*Radix::UserFunc::UserFunc:import-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:import")
protected final  void import (org.radixware.ads.Common.common.CommonXsd.UserFunc xml, boolean ignoreErrors, boolean considerContext, org.radixware.ads.Types.server.Entity context, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	if (xml == null)
	    return;

	description = xml.Description == null ? (xml.Title == null ? "" : xml.Title) : xml.Description;
	if (xml.OwnerPropId != null) {
	    upDefId = xml.OwnerPropId;
	}
	if (xml.OwnerEntityId != null) {
	    upOwnerEntityId = xml.OwnerEntityId;
	}

	if (xml.OwnerPid != null) {
	    upOwnerPid = xml.OwnerPid;
	}
	if (xml.ExtSrc != null)
	    extSrc = writeClob(xml.ExtSrc);

	if (xml.JavaSrc != null) {
	    if (shouldSaveSrcVersion()) {
	        saveCurSrcVersionBeforeImport(helper);
	    }
	    
	    org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument xDoc = org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.Factory.newInstance();
	    org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition xDef = xDoc.addNewAdsUserFuncDefinition();
	    xDef.Source = xml.JavaSrc;
	    xDef.Strings = xml.Strings;
	    xDef.MethodId = xml.MethodId;
	    xDef.ClassId = Types::Id.Factory.loadFrom(xml.ClassGUID);
	    if (xml.MethodProfile != null) {
	        org.radixware.schemas.adsdef.UserFuncProfile xProfile = xDef.addNewUserFuncProfile();
	        xProfile.MethodName = xml.MethodProfile.MethodName;
	        if (xml.MethodProfile.ReturnType != null) {
	            org.radixware.schemas.adsdef.UserFuncProfile.ReturnType xRet = xProfile.addNewReturnType();
	            xRet.Description = xml.MethodProfile.ReturnType.Description;
	            xRet.DescriptionId = xml.MethodProfile.ReturnType.DescriptionId;
	            xRet.Dimension = xml.MethodProfile.ReturnType.Dimension;
	            xRet.ExtStr = xml.MethodProfile.ReturnType.ExtStr;
	            if (xml.MethodProfile.ReturnType.TypeId != null)
	                xRet.TypeId = xml.MethodProfile.ReturnType.TypeId;
	            xRet.GenericArguments = xml.MethodProfile.ReturnType.GenericArguments;
	            xRet.IsArgumentType = xml.MethodProfile.ReturnType.IsArgumentType;
	            if (xml.MethodProfile.ReturnType.Path != null) {
	                xRet.Path = xml.MethodProfile.ReturnType.Path;
	            }
	        }
	        if (xml.MethodProfile.Parameters != null && xml.MethodProfile.Parameters.ParameterList != null) {
	            org.radixware.schemas.adsdef.UserFuncProfile.Parameters xParams = xProfile.addNewParameters();

	            for (org.radixware.schemas.xscml.JmlParameterDeclaration xDecl : xml.MethodProfile.Parameters.ParameterList) {
	                org.radixware.schemas.adsdef.ParameterDeclaration xParam = xParams.addNewParameter();
	                xParam.Name = xDecl.Name;
	                if (xDecl.Id != null) {
	                    xParam.Id = xDecl.Id;
	                } else {
	                    xParam.Id = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:ADS_METHOD_PARAMETER);
	                }
	                xParam.Description = xDecl.Description;
	                xParam.DescriptionId = xDecl.DescriptionId;
	                xParam.Type = xDecl.Type;
	                xParam.Variable = xDecl.Variable;
	            }
	        }
	        if (xml.MethodProfile.ThrownExceptions != null && xml.MethodProfile.ThrownExceptions.ExceptionList != null) {
	            org.radixware.schemas.adsdef.UserFuncProfile.ThrownExceptions xExcs = xProfile.addNewThrownExceptions();
	            for (org.radixware.schemas.xscml.JmlFuncProfile.ThrownExceptions.Exception xDecl : xml.MethodProfile.ThrownExceptions.ExceptionList) {
	                org.radixware.schemas.adsdef.UserFuncProfile.ThrownExceptions.Exception xEx = xExcs.addNewException();
	                xEx.Description = xDecl.Description;
	                xEx.DescriptionId = xDecl.DescriptionId;
	                xEx.Dimension = xDecl.Dimension;
	                xEx.ExtStr = xDecl.ExtStr;
	                if (xDecl.TypeId != null)
	                    xEx.TypeId = xDecl.TypeId;
	                xEx.GenericArguments = xDecl.GenericArguments;
	                xEx.IsArgumentType = xDecl.IsArgumentType;
	                if (xDecl.Path != null) {
	                    xEx.Path = xDecl.Path;
	                }

	            }
	        }
	    }
	    processDbTags(xDef.Source, considerContext, context, helper);
	    javaSrc = xDoc;
	}

	if (xml.Binding != null && xml.Binding.LibFunc != null) {
	    this.libFuncGuid = xml.Binding.LibFunc;
	    this.isLinkUsed = xml.Binding.Use;
	    if (xml.Binding.ParamsBinding != null) {
	        Reports::ReportsXsd:ParametersBindingDocument xDoc = Reports::ReportsXsd:ParametersBindingDocument.Factory.newInstance();
	        xDoc.ParametersBinding = xml.Binding.ParamsBinding;
	        this.paramBinding = writeClob(xDoc);
	    }
	} else {
	    this.libFuncGuid = null;
	    this.isLinkUsed = false;
	    this.paramBinding = null;
	}

	if (helper != null && helper.getUserFuncImportHelper() != null
	        && helper.getUserFuncImportHelper().isCompileDeferred()) {
	    helper.getUserFuncImportHelper().scheduleDeferredCompile(this);
	} else {
	    if (isLinkUsed == null || !isLinkUsed.booleanValue()) {
	        final boolean ignoreErrorsParam = ignoreErrors;
	        final UserFunc thisFunc = this;
	        org.radixware.kernel.common.check.IProblemHandler diagnoseHandler = new org.radixware.kernel.common.check.IProblemHandler() {
	            public void accept(org.radixware.kernel.common.check.RadixProblem p) {
	                if (!ignoreErrorsParam && p.Severity == org.radixware.kernel.common.check.RadixProblem.ESeverity.ERROR) {
	                    Arte::Trace.error(UserFunc.getCompilationErrorMessage(thisFunc, p), Arte::EventSource:UserFunc);
	                }
	            }
	        };
	        compile(diagnoseHandler);
	    }
	    actualizeUsedDefinitions();
	}

	if (xml.isSetChangeLog()) {
	    CfgManagement::ChangeLog.import(this, idof[UserFunc:changeLog], xml.isSetChangeLog(), xml.ChangeLog, helper);
	}
}

/*Radix::UserFunc::UserFunc:calcDbObjectsExtGuids-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:calcDbObjectsExtGuids")
private final  void calcDbObjectsExtGuids (org.radixware.schemas.xscml.JmlType jmlSrc) {
	for (Meta::XscmlXsd:JmlType.Item item : jmlSrc.ItemList) {
	    if (item.isSetDbEntity()) {
	        final Types::Id tableId;
	        if (item.DbEntity.EntityId.getPrefix() == Meta::DefinitionIdPrefix:ADS_APPLICATION_CLASS) {
	            tableId = Arte::Arte.getDefManager().getClassDef(item.DbEntity.EntityId).getTableDef().getId();
	        } else {
	            tableId = Types::Id.Factory.changePrefix(item.DbEntity.EntityId,
	                    Meta::DefinitionIdPrefix:DDS_TABLE);
	        }

	        try {
	            final org.radixware.kernel.server.types.Entity dbEntity
	                    = Arte::Arte.getInstance().getEntityObject(new Pid(
	                                    Arte::Arte.getInstance(),
	                                    tableId,
	                                    item.DbEntity.PidAsStr));

	            if (dbEntity instanceof CfgManagement::ICfgReferencedObject) {
	                item.DbEntity.ExtGuid
	                        = ((CfgManagement::ICfgReferencedObject) dbEntity).getCfgReferenceExtGuid();
	            }
	        } catch (Exceptions::Exception ex) {
	            Arte::Trace.warning(pathQuick + ": Error on calculate CfgReferenceExtGuid for: '"
	                    + tableId + "~" + item.DbEntity.PidAsStr
	                    + "'. Stack:\n" + Arte::Trace.exceptionStackToString(ex), Arte::EventSource:UserFunc);
	        }
	    }
	}
}

/*Radix::UserFunc::UserFunc:processDbTags-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:processDbTags")
private final  void processDbTags (org.radixware.schemas.xscml.JmlType jmlSrc, boolean considerContext, org.radixware.ads.Types.server.Entity context, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	java.util.Map<org.radixware.kernel.common.types.Pid, Types::Id> pids2EntityId = new java.util.HashMap<>();
	for (Meta::XscmlXsd:JmlType.Item item : jmlSrc.ItemList) {
	    if (!item.isSetDbEntity() || item.DbEntity.IsUFOwner) {
	        continue;
	    }
	    actualizeDbTagPidByExtGuid(item.DbEntity, context, considerContext, helper);
	    
	    Types::Id tableId = Meta::Utils.getEntityIdByClassId(item.DbEntity.EntityId);
	    org.radixware.kernel.common.types.Pid pid
	            = new org.radixware.kernel.common.types.Pid(tableId, item.DbEntity.PidAsStr);
	    pids2EntityId.put(pid, item.DbEntity.EntityId);
	}

	reportDbTagEntitiesNotExistsInDb(pids2EntityId, helper);
}

/*Radix::UserFunc::UserFunc:exportThis-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:exportThis")
  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data, org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument xUserFuncSrc) {
	data.itemClassId = idof[CfgItem.UserFuncSingle];
	data.object = this;
	Common::CommonXsd:UserFunc xml = export(xUserFuncSrc != null ? xUserFuncSrc : javaSrc);
	Common::CommonXsd:UserFuncDocument doc = Common::CommonXsd:UserFuncDocument.Factory.newInstance();
	doc.addNewUserFunc().set(xml);
	data.data = doc;
	data.fileContent = doc;
}

/*Radix::UserFunc::UserFunc:replaceFromCfgItem-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:replaceFromCfgItem")
  void replaceFromCfgItem (org.radixware.ads.UserFunc.server.CfgItem.UserFuncSingle cfg, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	Types::Id ufId = Types::Id.Factory.loadFrom(cfg.myData.UserFunc.OwnerPropId);
	importThis(cfg.propOwner, ufId, true, cfg.myData.UserFunc, helper);

	if (isNewObject()) {
	    cfg.propOwner.update();
	    helper.reportObjectCreated(this, path);
	} else {
	    update();
	    helper.reportObjectUpdated(this, path);
	}
}

/*Radix::UserFunc::UserFunc:resolveEntityByExtGuid-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:resolveEntityByExtGuid")
 static  java.util.List<org.radixware.ads.Types.server.Entity> resolveEntityByExtGuid (org.radixware.kernel.common.types.Id entityId, Str extGuid, boolean considerContext, org.radixware.ads.Types.server.Entity context) throws java.lang.Exception {
	final CfgManagement::ICfgObjectLookupAdvizor advizor = CfgManagement::ImpExpUtils.createCfgLookupAdvizor(entityId);
	if (advizor != null) {
	    try {
	        return advizor.getCfgObjectsByExtGuid(extGuid, considerContext, context);
	    } catch (Exceptions::Exception ex) {
	        final String mess = Str.format("Error on lookup entity %s by extGuid: %s", entityId.toString(), extGuid);
	        throw new Exception(mess, ex);
	    }
	}
	return java.util.Collections.emptyList();
}

/*Radix::UserFunc::UserFunc:importThis-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:importThis")
 final  void importThis (org.radixware.ads.Types.server.Entity owner, org.radixware.kernel.common.types.Id propId, boolean isSet, org.radixware.ads.Common.common.CommonXsd.UserFunc xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	new UserFunc.UserFuncImporter(owner, propId, isSet, xml, helper).import(this);
}

/*Radix::UserFunc::UserFunc:onCommand_Export-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:onCommand_Export")
public  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest onCommand_Export (org.radixware.schemas.types.MapStrStrDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	String fileName = null;
	Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xUserFuncDocFromEditor = null;
	for (org.radixware.schemas.types.MapStrStr.Entry e : input.MapStrStr.getEntryList()) {
	    if ("Filepath".equals(e.getKey())) {
	        fileName = e.getValue();
	    } else if ("UserFuncXmlFromEditorBase64".equals(e.getKey())) {
	        String userFuncXmlFromEditorBase64
	                = new String(Utils::Base64.decode(e.getValue()), java.nio.charset.StandardCharsets.UTF_8);
	        try {
	            xUserFuncDocFromEditor
	                    = Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.Factory.parse(userFuncXmlFromEditorBase64);
	        } catch (Exceptions::XmlException ex) {
	            Arte::Trace.debug(Arte::Trace.exceptionStackToString(ex), Arte::EventSource:UserFunc);
	        }
	    }
	}

	if (fileName != null && !fileName.isEmpty()) {
	    Common::CommonXsd:UserFunc xml = export(xUserFuncDocFromEditor);
	    Common::CommonXsd:UserFuncDocument doc = Common::CommonXsd:UserFuncDocument.Factory.newInstance();
	    doc.addNewUserFunc().set(xml);
	    CfgManagement::ImpExpUtils.writeToFile(doc, fileName);
	    return null;
	} else {
	    CfgExportForm.UserFuncSingle form = new CfgExportForm.UserFuncSingle(null);
	    form.xUserFuncSrc = xUserFuncDocFromEditor;
	    return new FormHandlerNextDialogsRequest(null, form);
	}
}

/*Radix::UserFunc::UserFunc:reportResolveWarnings-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:reportResolveWarnings")
 static  java.util.List<Str> reportResolveWarnings (java.util.List<org.radixware.ads.Types.server.Entity> objects, Str extGuid, org.radixware.kernel.common.types.Id classId) {
	if (objects == null || objects.size() != 1 || (objects.size() == 1 && objects.get(0) == null)) {
	    final java.util.List<String> msg = new java.util.ArrayList<String>();
	    String classTitle = "<undefined>";
	    try {
	        final org.radixware.kernel.server.meta.clazzes.RadClassDef classDef = Arte::Arte.getDefManager().getClassDef(classId);
	        if (classDef != null) {
	            classTitle = classDef.getTitle();
	        }
	    } catch (Exceptions::Exception ex) {
	        Arte::Trace.debug("Error on calc entity title:\n" + Arte::Trace.exceptionStackToString(ex), Arte::EventSource:UserFunc);
	    }

	    if (objects != null && objects.size() > 1) {
	        msg.add(String.format("Ambiguous link, following entities of type '%s':", classTitle));
	        for (Types::Entity obj : objects) {
	            msg.add(obj != null ? obj.calcTitle() : "null");
	        }
	        msg.add(String.format("has same ExtGuid: %s", extGuid));
	    } else {
	        msg.add(String.format("Entity of type '%s' with ExtGuid '%s' not found", classTitle, extGuid));
	    }

	    return msg;
	}

	return java.util.Collections.emptyList();
}

/*Radix::UserFunc::UserFunc:create-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:create")
 static  org.radixware.ads.UserFunc.server.UserFunc create (Str classGuid) {
	UserFunc func = (UserFunc)Arte::Arte.getInstance().newObject(Types::Id.Factory.loadFrom(classGuid));
	func.init();
	return func;
}

/*Radix::UserFunc::UserFunc:export-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:export")
  org.radixware.ads.Common.common.CommonXsd.UserFunc export (org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument xUserFuncSrc) {
	Common::CommonXsd:UserFunc userFunc = Common::CommonXsd:UserFunc.Factory.newInstance();
	userFunc.ClassGUID = classGuid;
	if (description != null) {
	    userFunc.Description = description;
	}
	userFunc.ProfileVersion = version;
	userFunc.OwnerPid = upOwnerPid;
	userFunc.OwnerEntityId = upOwnerEntityId;
	userFunc.OwnerClassId = upOwnerClassId;
	userFunc.OwnerPropId = upDefId;

	Types::Entity owner = findOwner();
	if (owner instanceof CfgManagement::ICfgReferencedObject) {
	    userFunc.OwnerExtGuid = ((CfgManagement::ICfgReferencedObject) owner).getCfgReferenceExtGuid();
	}

	if (extSrc != null) {
	    try {
	        String extSrc = extSrc.getSubString(1, (int) extSrc.length());
	        userFunc.ExtSrc = extSrc;//.(extSrc.getBytes());
	    } catch (Exceptions::SQLException e) {
	        Arte::Trace.put(Arte::EventSeverity:Error, Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	    }
	}
	if (libFuncGuid != null) {
	    Common::CommonXsd:UserFunc.Binding xBinding = userFunc.ensureBinding();
	    xBinding.LibFunc = libFuncGuid;
	    xBinding.Use = isLinkUsed == null ? false : isLinkUsed.booleanValue();


	    if (paramBinding != null) {
	        try {
	            String src = paramBinding.getSubString(1, (int) paramBinding.length());
	            Reports::ReportsXsd:ParametersBindingDocument parametersBindingDoc = Reports::ReportsXsd:ParametersBindingDocument.Factory.parse(src);
	            xBinding.ParamsBinding = parametersBindingDoc.ParametersBinding;
	        } catch (Exceptions::SQLException e) {
	            Arte::Trace.debug(Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	        } catch (Exceptions::XmlException e) {
	            Arte::Trace.debug(Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	        }
	    }
	}

	if (xUserFuncSrc != null) {
	    final Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xDoc;
	    if (javaSrc == xUserFuncSrc) {
	        //Should do copy of xml to not modify user function
	        xDoc = (Meta::AdsDefXsd:AdsUserFuncDefinitionDocument) xUserFuncSrc.copy();
	    } else {
	        xDoc = xUserFuncSrc;
	    }
	    
	    if (xDoc.AdsUserFuncDefinition != null) {
	        if (xDoc.AdsUserFuncDefinition.Source != null) {
	            Meta::XscmlXsd:JmlType jml = xDoc.AdsUserFuncDefinition.Source;
	            calcDbObjectsExtGuids(jml);
	            userFunc.JavaSrc = jml;
	        }

	        userFunc.Strings = xDoc.AdsUserFuncDefinition.Strings;

	        userFunc.MethodId = xDoc.AdsUserFuncDefinition.MethodId;
	        if (xDoc.AdsUserFuncDefinition.UserFuncProfile != null) {
	            org.radixware.schemas.adsdef.UserFuncProfile srcProfile = xDoc.AdsUserFuncDefinition.UserFuncProfile;
	            org.radixware.schemas.xscml.JmlFuncProfile xProfile = userFunc.addNewMethodProfile();
	            xProfile.MethodName = srcProfile.MethodName;
	            if (srcProfile.ReturnType != null) {
	                org.radixware.schemas.xscml.JmlFuncProfile.ReturnType xRet = xProfile.addNewReturnType();
	                xRet.Description = srcProfile.ReturnType.Description;
	                xRet.DescriptionId = srcProfile.ReturnType.DescriptionId;
	                xRet.Dimension = srcProfile.ReturnType.Dimension;
	                xRet.ExtStr = srcProfile.ReturnType.ExtStr;
	                if (srcProfile.ReturnType.TypeId != null)
	                    xRet.TypeId = srcProfile.ReturnType.TypeId;
	                xRet.GenericArguments = srcProfile.ReturnType.GenericArguments;
	                xRet.IsArgumentType = srcProfile.ReturnType.IsArgumentType;
	                if (srcProfile.ReturnType.Path != null) {
	                    xRet.Path = srcProfile.ReturnType.Path;
	                }
	            }
	            if (srcProfile.Parameters != null && srcProfile.Parameters.ParameterList != null) {
	                org.radixware.schemas.xscml.JmlFuncProfile.Parameters xParams = xProfile.addNewParameters();
	                for (org.radixware.schemas.adsdef.ParameterDeclaration xDecl : srcProfile.Parameters.ParameterList) {
	                    org.radixware.schemas.xscml.JmlParameterDeclaration xParam = xParams.addNewParameter();
	                    xParam.Name = xDecl.Name;
	                    if (xDecl.Id != null) {
	                        xParam.Id = xDecl.Id;
	                    } else {
	                        xParam.Id = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:ADS_METHOD_PARAMETER);
	                    }
	                    xParam.Description = xDecl.Description;
	                    xParam.DescriptionId = xDecl.DescriptionId;
	                    xParam.Type = xDecl.Type;
	                    xParam.Variable = xDecl.Variable;
	                }
	            }
	            if (srcProfile.ThrownExceptions != null && srcProfile.ThrownExceptions.ExceptionList != null) {
	                org.radixware.schemas.xscml.JmlFuncProfile.ThrownExceptions xExcs = xProfile.addNewThrownExceptions();
	                for (org.radixware.schemas.adsdef.UserFuncProfile.ThrownExceptions.Exception xDecl : srcProfile.ThrownExceptions.ExceptionList) {
	                    org.radixware.schemas.xscml.JmlFuncProfile.ThrownExceptions.Exception xEx = xExcs.addNewException();
	                    xEx.Description = xDecl.Description;
	                    xEx.DescriptionId = xDecl.DescriptionId;
	                    xEx.Dimension = xDecl.Dimension;
	                    xEx.ExtStr = xDecl.ExtStr;
	                    if (xDecl.TypeId != null) {
	                        xEx.TypeId = xDecl.TypeId;
	                    }
	                    xEx.GenericArguments = xDecl.GenericArguments;
	                    xEx.IsArgumentType = xDecl.IsArgumentType;
	                    if (xDecl.Path != null) {
	                        xEx.Path = xDecl.Path;
	                    }

	                }
	            }
	        }
	    }
	}
	userFunc.Profile = displayProfile;

	if (changeLog != null) {
	    userFunc.addNewChangeLog().set(changeLog.export());
	}
	return userFunc;

}

/*Radix::UserFunc::UserFunc:actualizeUpOwnerPid-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:actualizeUpOwnerPid")
private static  boolean actualizeUpOwnerPid (org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument xmlDoc, org.radixware.ads.Types.server.Entity owner) {
	if (xmlDoc!=null && xmlDoc.AdsUserFuncDefinition!=null && xmlDoc.AdsUserFuncDefinition.Source!=null){
	    return actualizeUpOwnerPid(xmlDoc.AdsUserFuncDefinition.Source.ItemList, owner);
	}
	return false;
}

/*Radix::UserFunc::UserFunc:actualizeUpOwnerPid-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:actualizeUpOwnerPid")
private static  boolean actualizeUpOwnerPid (java.util.List<org.radixware.schemas.xscml.JmlType.Item> jmlItemsList, org.radixware.ads.Types.server.Entity owner) {
	boolean wasChanges = false;
	if (jmlItemsList!=null){
	    for (Meta::XscmlXsd:JmlType.Item item : jmlItemsList) {
	        if (item.isSetDbEntity() && item.DbEntity.IsUFOwner && item.DbEntity.PidAsStr==null) {
	            item.DbEntity.PidAsStr = owner.getPid().toString();
	            item.DbEntity.Title = owner.calcTitle();
	            wasChanges = true;
	        }
	    }    
	}
	return wasChanges;
}

/*Radix::UserFunc::UserFunc:actualizeUpOwnerPid-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:actualizeUpOwnerPid")
private static  boolean actualizeUpOwnerPid (org.radixware.schemas.adsdef.UserFuncSourceVersions sourceVersions, org.radixware.ads.Types.server.Entity owner) {
	boolean wasChanges = false;
	if (sourceVersions!=null && sourceVersions.SourceVersionList != null) {
	    for (org.radixware.schemas.adsdef.UserFuncSourceVersions.SourceVersion sourceVersion : sourceVersions.SourceVersionList) {
	        if (actualizeUpOwnerPid(sourceVersion.ItemList, owner)){
	            wasChanges = true;
	        }
	    }
	}
	return wasChanges;
}

/*Radix::UserFunc::UserFunc:appendToUsedDefinitionsList-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:appendToUsedDefinitionsList")
private final  void appendToUsedDefinitionsList (Str usedWrappers) {
	if (usedWrappers != null) {
	    String asString;
	    try {
	        if (this.usedDefinitions != null) {
	            asString = this.usedDefinitions.getSubString(1, (int) this.usedDefinitions.length());
	            final java.util.Set<String> usedDefsSet = new java.util.HashSet<>();
	            for (String def : asString.split(" ")) {
	                usedDefsSet.add(def);
	            }
	            for (String def : usedWrappers.split(" ")) {
	                usedDefsSet.add(def);
	            }
	            final java.lang.StringBuilder sb = new java.lang.StringBuilder();
	            boolean first = true;
	            for (String def : usedDefsSet) {
	                if (first) {
	                    first = false;
	                } else {
	                    sb.append(" ");
	                }
	                sb.append(def);
	            }
	            asString = sb.toString();
	        } else {
	            asString = usedWrappers;
	        }
	        Clob clob = Arte::Arte.createTemporaryClob();
	        clob.setString(1, asString);
	        this.usedDefinitions = clob;
	    } catch (Exceptions::SQLException e) {
	        Arte::Trace.warning("Error on update used definitions list\n" + Arte::Trace.exceptionStackToString(e),
	                Arte::EventSource:UserFunc);
	    }
	}
}

/*Radix::UserFunc::UserFunc:afterCreate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:afterCreate")
protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
	super.afterCreate(src);
	upOwnerKnownFromImport = null;
}

/*Radix::UserFunc::UserFunc:actualizeUsedDefinitions-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:actualizeUsedDefinitions")
public  void actualizeUsedDefinitions () {
	//Actualize used definitions list.
	AdsUserFuncDef userFunc = findUserFunc();
	String usedWrappersAsStr = UserFuncUtils.usedDefinitionsListToStr(userFunc.UsedWrappers);
	if (usedWrappersAsStr != null) {
	    if (libFuncGuid != null && !usedWrappersAsStr.contains(libFuncGuid)) {
	        usedWrappersAsStr += " " + libFuncGuid;
	    }
	} else {
	    usedWrappersAsStr = libFuncGuid;
	}
	appendToUsedDefinitionsList(usedWrappersAsStr);

	new org.radixware.kernel.common.defs.ads.module.UsagesSupport(userFunc.getModule())
	        .saveUsages(userFunc, javaSrc.AdsUserFuncDefinition.addNewUsages());
}

/*Radix::UserFunc::UserFunc:getCompilationErrorMessage-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getCompilationErrorMessage")
public static  Str getCompilationErrorMessage (org.radixware.ads.UserFunc.server.UserFunc userFunc, org.radixware.kernel.common.check.RadixProblem problem) {
	final java.lang.StringBuilder msgBuilder = new java.lang.StringBuilder("Compilation error: '");
	msgBuilder.append(userFunc.path).append("'\n").append(problem.Source).append("\n").append(problem.getMessage());
	return msgBuilder.toString();
}

/*Radix::UserFunc::UserFunc:reportIsStateValid-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:reportIsStateValid")
public  void reportIsStateValid (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	if (helper == null) {
	    throw new java.lang.NullPointerException("Helper is null");
	}
	if (isValid != true) {
	    final java.lang.StringBuilder sb = new java.lang.StringBuilder("Invalid UDF");
	    if (isLinkUsed != null && isLinkUsed.booleanValue() && linkedLibFunc == null) {
	        sb.append(": ").append(String.format("linked library function ('%s') not found", libFuncGuid));
	    }
	    reportImportWarnings(helper, sb.toString());
	}
}

/*Radix::UserFunc::UserFunc:reportImportWarnings-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:reportImportWarnings")
  void reportImportWarnings (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper, Str... message) {
	if (helper != null) {
	    if (ownerLibFunc != null) {
	        helper.reportWarnings(ownerLibFunc, message);
	    } else {
	        helper.reportWarnings(this, path, message);
	    }
	} else {
	    Arte::Trace.warning(path + ": " + message, Arte::EventSource:UserFunc);
	}
}

/*Radix::UserFunc::UserFunc:checkEntitiesExistance-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:checkEntitiesExistance")
private final  java.util.Map<org.radixware.kernel.common.types.Pid,Bool> checkEntitiesExistance (java.util.Set<org.radixware.kernel.common.types.Pid> pidsToCheck) {
	java.util.Map<org.radixware.kernel.common.types.Pid, Bool> resultMap
	        = new java.util.HashMap<>(pidsToCheck.size());
	for (org.radixware.kernel.common.types.Pid pid : pidsToCheck) {
	    boolean isEntityExists = true;
	    try {
	        Types::Pid serverPid = new Pid(Arte::Arte.getInstance(), pid.TableId, pid.toString());
	        Arte::Arte.getInstance().getEntityObject(serverPid, null, true);
	    } catch (Exceptions::DefinitionNotFoundError | Exceptions::EntityObjectNotExistsError ex) {
	        isEntityExists = false;
	    }
	    resultMap.put(pid, isEntityExists);
	}
	return resultMap;
}

/*Radix::UserFunc::UserFunc:onCommand_CheckEntitiesExistance-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:onCommand_CheckEntitiesExistance")
private final  org.radixware.schemas.types.MapStrStrDocument onCommand_CheckEntitiesExistance (org.radixware.schemas.types.MapStrStrDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	java.util.Set<org.radixware.kernel.common.types.Pid> pidsToCheck = new java.util.HashSet<>();
	for (Arte::TypesXsd:MapStrStr.Entry e : input.MapStrStr.EntryList) {
	    pidsToCheck.add(org.radixware.kernel.common.types.Pid.fromStr(e.Key));
	}
	Arte::TypesXsd:MapStrStrDocument xRs = Arte::TypesXsd:MapStrStrDocument.Factory.newInstance();
	xRs.addNewMapStrStr();
	for (java.util.Map.Entry<org.radixware.kernel.common.types.Pid, Bool> pid2Existance
	        : checkEntitiesExistance(pidsToCheck).entrySet()) {
	    Arte::TypesXsd:MapStrStr.Entry e = xRs.MapStrStr.addNewEntry();
	    e.Key = pid2Existance.getKey().toStr();
	    e.Value = pid2Existance.getValue().toString();
	}
	return xRs;
}

/*Radix::UserFunc::UserFunc:reportDbTagEntitiesNotExistsInDb-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:reportDbTagEntitiesNotExistsInDb")
private final  void reportDbTagEntitiesNotExistsInDb (java.util.Map<org.radixware.kernel.common.types.Pid,org.radixware.kernel.common.types.Id> pids2EntityId, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	if (helper == null || pids2EntityId == null || pids2EntityId.isEmpty()) {
	    return;
	}

	java.util.Map<org.radixware.kernel.common.types.Pid, Bool> checkResult
	        = checkEntitiesExistance(pids2EntityId.keySet());
	java.util.List<Str> warningsList = null;
	for (java.util.Map.Entry<org.radixware.kernel.common.types.Pid, Bool> pid2Existance : checkResult.entrySet()) {
	    if (pid2Existance.getValue().booleanValue()) {
	        continue;
	    }
	    if (warningsList == null) {
	        warningsList = new java.util.ArrayList<Str>();
	        warningsList.add("The following objects used by the UDF have not been found in the database:");
	    }
	    
	    final org.radixware.kernel.common.types.Pid pid = pid2Existance.getKey();
	    final Types::Id entityId = pids2EntityId.get(pid);
	    String className;
	    try {
	        className = Arte::Arte.getInstance().DefManager.getClassDef(entityId).getTitle();
	    } catch (Exceptions::DefinitionNotFoundError ex) {
	        className = entityId.toString();
	    }
	    warningsList.add(String.format("'%s~%s'", className, pid.toString()));
	}
	if (warningsList != null) {
	    reportImportWarnings(helper, warningsList.toArray(new String[warningsList.size()]));
	}
}

/*Radix::UserFunc::UserFunc:actualizeDbTagPidByExtGuid-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:actualizeDbTagPidByExtGuid")
private final  void actualizeDbTagPidByExtGuid (org.radixware.schemas.xscml.JmlType.Item.DbEntity xDbTag, org.radixware.ads.Types.server.Entity context, boolean considerContext, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	if (xDbTag.getExtGuid() != null && !xDbTag.ExtGuid.isEmpty()) {
	    final java.util.List<Types::Entity> objects;
	    try {
	        objects = resolveEntityByExtGuid(xDbTag.EntityId, xDbTag.ExtGuid, considerContext, context);
	    } catch (Exceptions::Exception ex) {
	        final String causeStack = ex.getCause() != null ? ("\n\n" + Arte::Trace.exceptionStackToString(ex.getCause())) : ""; 
	        Arte::Trace.warning(Str.format("%s: %s %s", path, ex.getMessage(), causeStack), Arte::EventSource:AppCfgPackage);
	        if (helper != null)
	            reportImportWarnings(helper, ex.getMessage());
	        return;
	    }

	    final java.util.List<String> warnings = reportResolveWarnings(objects, xDbTag.ExtGuid, xDbTag.EntityId);
	    if (!warnings.isEmpty()) {
	        reportImportWarnings(helper, warnings.toArray(new String[warnings.size()]));
	    } else {
	        xDbTag.PidAsStr = objects.get(0).getPid().toString();
	    }
	}
}

/*Radix::UserFunc::UserFunc:getCurrentVersionMethodId-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getCurrentVersionMethodId")
private final  Str getCurrentVersionMethodId () {
	Int prevVal = version;
	try {
	    version = getCurrentVersion();
	    Types::Id currMethodId = getMethodId();
	    return currMethodId != null ? currMethodId.toString() : null;
	} finally {
	    version = prevVal;
	}
}

/*Radix::UserFunc::UserFunc:lockVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lockVersion")
public published  long lockVersion () {
	refreshCache();
	versionLockCount++;
	if (versionLockCount == 1) {
	    Arte::CachedUserObject obj = new Arte::CachedUserObject() {
	        public void release() {
	            versionLockCount = 0;
	        }
	    };
	    Arte::Arte.getUserCache().put(getLockReleaserCacheKey(), obj, 60);
	}
	return version == null ? 0 : version.longValue();
}

/*Radix::UserFunc::UserFunc:unlockVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:unlockVersion")
public published  void unlockVersion () {
	if (versionLockCount > 0) {
	    if (versionLockCount == 1) {
	        Arte::Arte.getUserCache().remove(getLockReleaserCacheKey());
	        versionLockCount = 0;
	    } else {
	        versionLockCount--;
	    }
	}
}

/*Radix::UserFunc::UserFunc:getLockReleaserCacheKey-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getLockReleaserCacheKey")
private final  Str getLockReleaserCacheKey () {
	return "UDFLockReleaser '" + this. toString() + "' @ " + System.identityHashCode(this);
}

/*Radix::UserFunc::UserFunc:copyPropVal-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:copyPropVal")
public published  void copyPropVal (org.radixware.kernel.server.meta.clazzes.RadPropDef prop, org.radixware.kernel.server.types.Entity src) {
	if (idof[UserFunc:initOfDuplicatedObj] == prop.getId() || idof[UserFunc:ownerLibFuncName] == prop.getId()) {
	    return;
	}
	super.copyPropVal(prop, src);
}

/*Radix::UserFunc::UserFunc:saveCurSrcVersionBeforeImport-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:saveCurSrcVersionBeforeImport")
private final  void saveCurSrcVersionBeforeImport (org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	final org.radixware.schemas.adsdef.UserFuncSourceVersions xVersions;
	if (javaSrcVers != null) {
	    try {
	        xVersions = org.radixware.schemas.adsdef.UserFuncSourceVersions.Factory.parse(javaSrcVers.getCharacterStream());
	    } catch (Exceptions::XmlException | Exceptions::IOException | Exceptions::SQLException ex) {
	        Arte::Trace.warning("Error on saving current version in history\n" + Arte::Trace.exceptionStackToString(ex),
	                Arte::EventSource:UserFunc);
	        return;
	    }
	} else {
	    xVersions = org.radixware.schemas.adsdef.UserFuncSourceVersions.Factory.newInstance();
	}

	final String importFrom = helper != null && helper.getContextPacket() != null
	        ? "'" + helper.getContextPacket().title + "'"
	        : "file";
	final String baseName = Str.format("Backup before importing from %s", importFrom);
	String newVersionName = baseName;
	int versionIndex = 2;
	while (true) {
	    boolean isNameUnique = true;
	    for (Meta::AdsDefXsd:UserFuncSourceVersions.SourceVersion v : xVersions.SourceVersionList) {
	        if (newVersionName.equals(v.Name)) {
	            isNameUnique = false;
	            newVersionName = baseName + "#" + versionIndex++;
	            break;
	        }
	    }
	    if (isNameUnique) {
	        break;
	    }
	}

	Meta::AdsDefXsd:UserFuncSourceVersions.SourceVersion xVersion = xVersions.addNewSourceVersion();
	xVersion.set(javaSrc.AdsUserFuncDefinition.Source);
	xVersion.LastUpdateUserName = Arte::Arte.getUserName();
	xVersion.LastUpdateTime = java.util.Calendar.getInstance();
	xVersion.Name = newVersionName;

	try (java.io.StringWriter writer = new java.io.StringWriter()) {
	    xVersions.save(writer);
	    Clob tmp = Arte::Arte.createTemporaryClob();
	    tmp.setString(1, writer.getBuffer().toString());
	    javaSrcVers = tmp;
	} catch (Exceptions::IOException | Exceptions::SQLException ex) {
	    Arte::Trace.warning("Error on saving current version in history\n" + Arte::Trace.exceptionStackToString(ex),
	            Arte::EventSource:UserFunc);
	}
}

/*Radix::UserFunc::UserFunc:shouldSaveSrcVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:shouldSaveSrcVersion")
private final  boolean shouldSaveSrcVersion () {
	try {
	    return !UserFuncUtils.compareUserFuncSrc(javaSrc, getJavaSrcInitValue(null));
	} catch (Exceptions::IOException | Exceptions::XmlException ex) {
	    Arte::Trace.debug("Error on parse user function source:\n" + Arte::Trace.exceptionStackToString(ex), Arte::EventSource:UserFunc);
	    return true;
	}
}

/*Radix::UserFunc::UserFunc:isOwnerContainsLookupAdvizor-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerContainsLookupAdvizor")
private final  Bool isOwnerContainsLookupAdvizor () {
	Types::Entity owner = findOwner();
	if (owner != null && owner instanceof CfgManagement::ICfgReferencedObject) {
	    try {
	        return CfgManagement::ImpExpUtils.createCfgLookupAdvizor(owner.getClassDefinitionId()) != null;
	    } catch (Exceptions::Exception ex) {
	        final String causeStack = ex.getCause() != null ? ("\n\n" + Arte::Trace.exceptionStackToString(ex.getCause())) : "";
	        Arte::Trace.warning(ex.getMessage() + causeStack, Arte::EventSource:UserFunc);
	    }
	}
	return false;
}

/*Radix::UserFunc::UserFunc:getJavaSrcInitValue-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getJavaSrcInitValue")
protected  org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument getJavaSrcInitValue (org.radixware.ads.UserFunc.server.UserFunc srcFunc) {
	Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xDoc = null;
	if (srcFunc != null && srcFunc.javaSrc != null) {
	    try (java.io.InputStream is = srcFunc.javaSrc.newInputStream()) {
	        xDoc = Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.Factory.parse(is); 
	    } catch (Exceptions::XmlException | Exceptions::IOException e) {
	        Arte::Trace.error("Error on copy user function source:\n" + Arte::Trace.exceptionStackToString(e), Arte::EventSource:UserFunc);
	    }
	}
	if (xDoc == null) {
	    xDoc = Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.Factory.newInstance();

	    String initialSource = getInitialText();
	    boolean addAsString = true;
	    try {
	        org.radixware.schemas.xscml.JmlType srcJml = org.radixware.schemas.xscml.JmlType.Factory.parse(initialSource);
	        if (srcJml != null) {
	            xDoc.addNewAdsUserFuncDefinition().Source = srcJml;
	            addAsString = false;
	        }
	    } catch (Exceptions::XmlException e) {
	    }
	    if (addAsString) {
	        xDoc.addNewAdsUserFuncDefinition().addNewSource().addNewItem().Java = initialSource;
	    }
	}

	return xDoc;
}

/*Radix::UserFunc::UserFunc:getInvokeDescr-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getInvokeDescr")
public  Str getInvokeDescr () {
	final Str descr;
	String revDescr = "";
	if (changeLog != null) {
	    revDescr = changeLog.getLastRevisionTitle(true);
	    if (revDescr != null) {
	        revDescr = ", Revision: \"" + revDescr + '"';
	    } else {
	        revDescr = "";
	    }
	}
	if (ownerLibFunc != null) {//library function{
	    final String libName = ownerLibFunc.libName;
	    final String funcName = ownerLibFunc.name;
	    final String guid = ownerLibFunc.guid;
	    descr = "UDL '" + id + ") " + libName + "::" + funcName + "' (" + guid + ")" + revDescr;
	} else {
	    descr = "UDF '" + pathQuick + "'" + revDescr;
	}

	return descr;

}

/*Radix::UserFunc::UserFunc:getDescriptionWithChangelogInfo-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getDescriptionWithChangelogInfo")
  Str getDescriptionWithChangelogInfo (Str descr) {
	return UserFuncUtils.getDescriptionWithLastRevisionDate(descr, lastChangelogRevisionDate);
}

/*Radix::UserFunc::UserFunc:getAdditionalOwners-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getAdditionalOwners")
public published  java.util.List<org.radixware.ads.Types.server.Entity> getAdditionalOwners () {
	if (this.ownerLibFunc != null) {
	    return java.util.Arrays.asList((Types::Entity) this.ownerLibFunc);
	}
	return null;
}

/*Radix::UserFunc::UserFunc:getDisplayProfile-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:getDisplayProfile")
private final  Str getDisplayProfile (boolean reread) {
	if (reread) {
	    profileTitle = null;
	}
	return getDisplayProfile();
}

/*Radix::UserFunc::UserFunc:checkUserRights-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:checkUserRights")
 final  void checkUserRights (boolean isUpdate) {
	if (Arte::Arte.getInstance().getRights().getCurUserCanAccess(org.radixware.kernel.common.enums.EDrcServerResource.USER_FUNC_DEV)) {
	    return;
	}

	boolean accessViolation = false;
	UserFunc src = (UserFunc) this.getInitSrc();
	if (src != null) {
	    if (javaSrc != null && src.javaSrc != null) {
	        try {
	            accessViolation = !UserFuncUtils.compareUserFuncSrc(javaSrc, src.javaSrc);
	        } catch (Exceptions::IOException | Exceptions::XmlException e) {
	            Arte::Trace.debug("Error on parse user function source.\n" + Arte::Trace.exceptionStackToString(e), Arte::EventSource:UserFunc);
	            accessViolation = true;
	        }
	    } else {
	        accessViolation = javaSrc != src.javaSrc;
	    }
	    if (!accessViolation) {
	        if (javaRuntime != null && src.javaRuntime != null) {
	            try (java.io.InputStream bis1 = javaRuntime.BinaryStream; java.io.InputStream bis2 = src.javaRuntime.BinaryStream;) {
	                accessViolation = !UserFuncUtils.compareStreams(bis1, bis2);
	            } catch (Exceptions::IOException | Exceptions::SQLException e) {
	                Arte::Trace.debug("Error on parse user function source.\n" + Arte::Trace.exceptionStackToString(e), Arte::EventSource:UserFunc);
	                accessViolation = true;
	            }
	        } else {
	            accessViolation = javaRuntime != src.javaRuntime;
	        }
	    }
	} else if (isUpdate) {
	    accessViolation = !java.util.Collections.disjoint(PROPS_CONTROLLED_BY_ACCESS_SYSTEM, getPersistentModifiedPropIds());
	} else {
	    //User without rights for user func development can only create function
	    //which linked to other function
	    try {
	        accessViolation = !(javaRuntime == null && UserFuncUtils.compareUserFuncSrc(javaSrc, getJavaSrcInitValue(null)));
	    } catch (Exceptions::IOException | Exceptions::XmlException ex) {
	        Arte::Trace.debug("Error on parse user function source.\n" + Arte::Trace.exceptionStackToString(ex), Arte::EventSource:UserFunc);
	        accessViolation = true;
	    }
	}

	if (accessViolation) {
	    final String mess = "Insufficient access rights to develop user-defined functions";
	    Arte::Trace.put(
	            "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsF5YDUAZQWJER7OWVASLO7NWSS4",
	            mess,
	            Arte::Arte.getUserName(),
	            Arte::Arte.getStationName() + " (" + Arte::Arte.getInstance().getArteSocket().getRemoteAddress() + ")");
	    throw new ServiceProcessClientFault(Arte::EasXsd:ExceptionEnum.ACCESS_VIOLATION.toString(), mess, null, null);
	}
}

/*Radix::UserFunc::UserFunc:onJavaSrcChanged-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:onJavaSrcChanged")
protected  void onJavaSrcChanged (org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument newVal) {
	resultType = null;
}

/*Radix::UserFunc::UserFunc:actualizeUserFuncOwnerBinding-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:actualizeUserFuncOwnerBinding")
private static  boolean actualizeUserFuncOwnerBinding (org.radixware.schemas.reports.ParametersBindingType binding, org.radixware.kernel.common.types.Id upOwnerClassId, Str upOwnerOldPid, Str upOwnerNewPid) {
	boolean wasChanged = false;
	if (binding != null) {
	    for (Reports::ReportsXsd:ParametersBindingType.ParameterBinding xPar : binding.ParameterBindingList) {
	        if (xPar.isSetExternalValue()) {
	            if (java.util.Objects.equals(xPar.getExternalValue().OwnerClassId, upOwnerClassId) 
	                    && java.util.Objects.equals(xPar.getExternalValue().OwnerPID, upOwnerOldPid)) {
	                xPar.ExternalValue.OwnerPID = upOwnerNewPid;
	                wasChanged = true;
	            }
	        }
	    }
	}
	return wasChanged;
}










@Override
public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
	if(cmdId == cmd2UFOL65XPNAZFBUXA35TKOZYCE){
		final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmd2UFOL65XPNAZFBUXA35TKOZYCE_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmd77QKAJRRVFDGBNIIXSH4UDQ6QY){
		final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmd77QKAJRRVFDGBNIIXSH4UDQ6QY_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdAJHTMHT6FJEUVC7PUDONQ3RZXQ){
		final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmdAJHTMHT6FJEUVC7PUDONQ3RZXQ_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdJSJIK2BKOFAP5DOLQIICYERBAQ){
		org.radixware.kernel.server.types.FormHandler.NextDialogsRequest result = onCommand_MoveToLibrary(newPropValsById);
	return result;
} else if(cmdId == cmdLTBI2UOZY5DPHIGICOHUX5II5M){
	final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmdLTBI2UOZY5DPHIGICOHUX5II5M_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
	if(result != null)
		output.set(result);
	return null;
} else if(cmdId == cmdPQTVYTIC2RHXZGE7NPGUP3OMOE){
	org.radixware.schemas.types.MapStrStrDocument result = onCommand_CheckEntitiesExistance((org.radixware.schemas.types.MapStrStrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.types.MapStrStrDocument.class),newPropValsById);
	if(result != null)
		output.set(result);
	return null;
} else if(cmdId == cmdUAJCOSP4CRGSHHKG75O2TOIS34){
	final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmdUAJCOSP4CRGSHHKG75O2TOIS34_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
	if(result != null)
		output.set(result);
	return null;
} else if(cmdId == cmdWSK3HNPKXNCLFFMSYQ7XZCGOKI){
	org.radixware.kernel.server.types.FormHandler.NextDialogsRequest result = onCommand_Export((org.radixware.schemas.types.MapStrStrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.types.MapStrStrDocument.class),newPropValsById);
return result;
} else 
	return super.execCommand(cmdId,propId,input,newPropValsById,output);
}
private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmd2UFOL65XPNAZFBUXA35TKOZYCE_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
	final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
		@Override
		protected  org.radixware.kernel.common.enums.EValType getReturnType(){
			return org.radixware.kernel.common.enums.EValType.XML;
		}
		protected  Object invokeImpl(Object[] arguments){
			final Str p0 = arguments[0] == null ? null : (Str)arguments[0];
			Object $res$ =org.radixware.ads.UserFunc.server.UserFunc.this.mth6QOKMIBNWBD6TC5ITSDDXZL7DA(p0);
			return $res$;
		}
	};
	return invoker.invoke();
}
private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmd77QKAJRRVFDGBNIIXSH4UDQ6QY_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
	final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
		@Override
		protected  org.radixware.kernel.common.enums.EValType getReturnType(){
			return org.radixware.kernel.common.enums.EValType.STR;
		}
		protected  Object invokeImpl(Object[] arguments){
			Object $res$ =org.radixware.ads.UserFunc.server.UserFunc.this.mthWOAXEQNQVJFYFNPLD5FYT6ONBY();
			return $res$;
		}
	};
	return invoker.invoke();
}
private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmdAJHTMHT6FJEUVC7PUDONQ3RZXQ_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
	final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
		@Override
		protected  org.radixware.kernel.common.enums.EValType getReturnType(){
			return org.radixware.kernel.common.enums.EValType.XML;
		}
		protected  Object invokeImpl(Object[] arguments){
			final Str p0 = arguments[0] == null ? null : (Str)arguments[0];
			Object $res$ =org.radixware.ads.UserFunc.server.UserFunc.this.mthHO4YGVHMJFFJNFDQU3YKNNBIBY(p0);
			return $res$;
		}
	};
	return invoker.invoke();
}
private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmdLTBI2UOZY5DPHIGICOHUX5II5M_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
	final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
		@Override
		protected  org.radixware.kernel.common.enums.EValType getReturnType(){
			return org.radixware.kernel.common.enums.EValType.BOOL;
		}
		protected  Object invokeImpl(Object[] arguments){
			Object $res$ =org.radixware.ads.UserFunc.server.UserFunc.this.mth7F3GMQH6QJFADENZFW7VP73AKM();
			return $res$;
		}
	};
	return invoker.invoke();
}
private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmdUAJCOSP4CRGSHHKG75O2TOIS34_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
	final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
		@Override
		protected  org.radixware.kernel.common.enums.EValType getReturnType(){
			return org.radixware.kernel.common.enums.EValType.XML;
		}
		protected  Object invokeImpl(Object[] arguments){
			final org.radixware.ads.Meta.common.DefType.Arr p0 = arguments[0] == null ? null : arguments[0] instanceof org.radixware.ads.Meta.common.DefType.Arr ? (org.radixware.ads.Meta.common.DefType.Arr) arguments[0] : new org.radixware.ads.Meta.common.DefType.Arr((org.radixware.kernel.common.types.ArrInt)arguments[0]);
			Object $res$ =org.radixware.ads.UserFunc.server.UserFunc.this.mth4BC72HPVYRAGHKULB3RDT3VJRI(p0);
			return $res$;
		}
	};
	return invoker.invoke();
}


}

/* Radix::UserFunc::UserFunc - Server Meta*/

/*Radix::UserFunc::UserFunc-Entity Class*/

package org.radixware.ads.UserFunc.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"UserFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::UserFunc::UserFunc:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
							/*Owner Class Name*/
							"UserFunc",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),
							/*Property presentations*/

							/*Radix::UserFunc::UserFunc:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::UserFunc::UserFunc:classGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2Q4DNYD3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:userClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2U4DNYD3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:upDefId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7QMAJND3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:upOwnerEntityId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UMAJND3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:upOwnerPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7YMAJND3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:lastUpdateUserName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCGFACL4ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:javaSrc:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTALTD5L3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:javaRuntime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTELTD5L3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:extSrc:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYB2NT733ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::UserFunc::UserFunc:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYF2NT733ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:methodId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKVOQE4YK7FBFTDFG42BCRRSZCI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:ownerTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQY3XHAZTHZB57GEKAHI6K23SEM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::UserFunc::UserFunc:description:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEY7LJLL77RDNLLC655BJGHGEL4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::UserFunc::UserFunc:displayProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLKXRGY3PCRBR3ED4HQEB25EODE"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:version:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZVDPX2IPBCM7FVCASFB3JHBYM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:currentVersion:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSPGLGCHLMZBXFIE4CJPZOMDCXY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:ownerClassName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ4TLAHYKT5ECVLDKAXYMT44BXY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:isValid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5HQW5K6ILZEC7LCIL66JEZNP7U"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:upOwnerClassId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEDT3H6EV4JF65HJPCQOZLCP2QQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:path:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYLLJTXZ45NHMLOCQFZSHR4PHOQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:ownerPropertyFullName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7MTIHMUG5ZHVNKMSTMAZAKOJOQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:ownerPropertyName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLVJEVPXKWVFVLIBJL7NHJNZJKU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:javaSrcVers:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOUPOLAY2WRH4DMCHP2YZEPZHRI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:editedSourceVersions:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGWV4ETLA5BH4BD32XHCBMRLQPM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:isOwnerExist:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHVASLNCB2JFJPLSJA3LATFOL2E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:ownerJavaClassName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL2DOXTGV5ZGK3P5VFGI5KNQAEE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:pathQuick:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDVV3C4FRCBA6THJPK4O2HIVOUA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:ownerTitleQuick:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA323IBYJANBAHG67SUX4DEUCYY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::UserFunc::UserFunc:libFuncGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col26N4VA2NARCLDDJPST4UQNDHIA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:parameterNames:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL7ZR7JQORFDHNKNPTGS7FKIFWI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:usedDefinitions:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS3ULKYQUEZAYBJR55WPBI77FQE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:linkedLibFunc:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXRTJJSPDT5F4ROOQGMN3K5MJDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2Z3PQIOYMBE2FM2DX74KBB7D7A"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:paramBinding:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7J2X7AEIJNE3HDAI4RQQWGWKN4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:isLinkUsed:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPNLNJ2WNRJCBPBR6JPI3RZ4TVE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:ownerLibFunc:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2WO2NG55AZFCBCB6XECNIMXEAE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr74SXZSVAHBF4NEXED3ZRFZ7XFU")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::UserFunc::UserFunc:resultType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2ALXV2PR4FBRJF4M77FXFVJVIA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:usedLibraryFunctions:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd25D66ZTBSNE3BDBEDPTQ5UMDKA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::UserFunc::UserFunc:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:optimizerCache:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT2MHPJYB2BG6POOUPZNQQQDKEY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:ownerPipelineId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ4J6Z23BTNDF5IAFKM6GNL3G6U"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:ownerLibFuncName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK4HEWCSIIVCBDGFGIPYANPGLD4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::UserFunc::UserFunc:isOwnerCfgRef:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7VDQ66LIFBFXTGM27NTY3OIGVQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:changeLog:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruOF2GYDTCGBGJ3EZV2QYRIC626I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:isOwnerWasNotCreated:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdILZKB5MVKNAITADIRBN2C2BEZU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:changeLogImportFromJmlEditor:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZHGKBYXPYBEN7LARKMVZVTJHVY"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:inheritedDescription:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPFPZWKHWJNFB5H4GA2XM6MYFE4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:lastChangelogRevisionDate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRLFSWEPMAZGSXHC4Q33JZ4V7VI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::UserFunc::UserFunc:descriptionForSelector:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP73O4GPGEBHW7O4YHOK5RDTGF4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::UserFunc::UserFunc:remoteCall_listUserDefinitions-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdUAJCOSP4CRGSHHKG75O2TOIS34"),"remoteCall_listUserDefinitions",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::UserFunc::UserFunc:remoteCall_loadUserClassXml-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2UFOL65XPNAZFBUXA35TKOZYCE"),"remoteCall_loadUserClassXml",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::UserFunc::UserFunc:remoteCall_loadUserFuncXml-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAJHTMHT6FJEUVC7PUDONQ3RZXQ"),"remoteCall_loadUserFuncXml",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::UserFunc::UserFunc:MoveToLibrary-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJSJIK2BKOFAP5DOLQIICYERBAQ"),"MoveToLibrary",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::UserFunc::UserFunc:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdWSK3HNPKXNCLFFMSYQ7XZCGOKI"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::UserFunc::UserFunc:CheckEntitiesExistance-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPQTVYTIC2RHXZGE7NPGUP3OMOE"),"CheckEntitiesExistance",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),true,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::UserFunc::UserFunc:remoteCall_getCurrentVersionMethodId-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd77QKAJRRVFDGBNIIXSH4UDQ6QY"),"remoteCall_getCurrentVersionMethodId",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::UserFunc::UserFunc:remoteCall_isOwnerContainsLookupAdvizor-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLTBI2UOZY5DPHIGICOHUX5II5M"),"remoteCall_isOwnerContainsLookupAdvizor",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::UserFunc::UserFunc:UpdateTime-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"UpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJFYGCCXBZAI7OWHG63SKXU3OY"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYF2NT733ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::UserFunc::UserFunc:Id-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtKLUDTYAFTBEOFFEXKZBNDARCNA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"Id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ4EBVNECQJH77P5MHNLQYEQLOA"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::UserFunc::UserFunc:Invalid-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltSSCZW2PBHBAIXEYY2GD3H5UVUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"Invalid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRTIXYU5XQZBVJPCK4OZI6JBNKM"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colTELTD5L3ZHOBDCMTAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),true,null,false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::UserFunc::UserFunc:Undefined-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltR2YI43ZJIJESJNSIB2DBS646JE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"Undefined",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXUGMTUUGNE6LLFEVUEY34AMAU"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colTALTD5L3ZHOBDCMTAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null \nOR \nextract(xmltype(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colTALTD5L3ZHOBDCMTAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>), \'//*[local-name()=\"Source\"]//*[local-name()=\"Java\"]/text()[normalize-space()]\') is null\n</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),true,null,false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::UserFunc::UserFunc:BySourceText-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("flt56W7TVKG35DX7C376TDH4KGQYY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"BySourceText",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZNAHTKFXBRBAFFOLDXF2NJP4H4"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colPNLNJ2WNRJCBPBR6JPI3RZ4TVE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=0 or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"col26N4VA2NARCLDDJPST4UQNDHIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null) and  dbms_lob.instr(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colTALTD5L3ZHOBDCMTAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, to_clob(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm26DWQSCW5VF37NOE7F7MHY7H7A\"/></xsc:Item><xsc:Item><xsc:Sql>)) > 0</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),true,null,false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::UserFunc::UserFunc:ById-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltYAHTHJU32BB67JJVNKDRDKFIMI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"ById",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCMXJLZWWJCLNF3YYF7BMLSSWU"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"col6V3CVGAHT5GRXAQ6VRLN3YVQGQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmGREQZOIXRFGW7GHYAT65QIUQ6M\"/></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtKLUDTYAFTBEOFFEXKZBNDARCNA"),true,null,false,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::UserFunc::UserFunc:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2048,
									null,

									/*Radix::UserFunc::UserFunc:General:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::UserFunc::UserFunc:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGWQBF5FKIVGRFHCOVCDXBLS3TQ"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::UserFunc::UserFunc:Create:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::UserFunc::UserFunc:Usages-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHMDW6GTMKJC47FL5GNCYGTMKQU"),"Usages",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPFPZWKHWJNFB5H4GA2XM6MYFE4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQY3XHAZTHZB57GEKAHI6K23SEM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLKXRGY3PCRBR3ED4HQEB25EODE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEY7LJLL77RDNLLC655BJGHGEL4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP73O4GPGEBHW7O4YHOK5RDTGF4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5HQW5K6ILZEC7LCIL66JEZNP7U"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYF2NT733ZHOBDCMTAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCGFACL4ZHOBDCMTAALOMT5GDM"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(18559,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::UserFunc::UserFunc:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DRA4YXDZNALTA4KT73WNRMYSQ"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPFPZWKHWJNFB5H4GA2XM6MYFE4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2ALXV2PR4FBRJF4M77FXFVJVIA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQY3XHAZTHZB57GEKAHI6K23SEM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ4TLAHYKT5ECVLDKAXYMT44BXY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLVJEVPXKWVFVLIBJL7NHJNZJKU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLKXRGY3PCRBR3ED4HQEB25EODE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5HQW5K6ILZEC7LCIL66JEZNP7U"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEY7LJLL77RDNLLC655BJGHGEL4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP73O4GPGEBHW7O4YHOK5RDTGF4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYF2NT733ZHOBDCMTAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCGFACL4ZHOBDCMTAALOMT5GDM"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(17467,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNTOBFKCWDNA7DCJMVLXTSYYEUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd6BI243J5TFAVVN3NA2I6TPTYUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdG3QHVWEBQBGNPJQV3RFULJWQNE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZYKIKMNUXFAJXKWYRPUKGT4LWE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAEPY27UUVJHVXC3BHUPA7QJSC4")},null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::UserFunc::UserFunc:CreateInLibrary-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJXA4VMNLWBH6DPNJHIXMNHXBHA"),"CreateInLibrary",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGWQBF5FKIVGRFHCOVCDXBLS3TQ")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXBL56RWC2NAPDMES66PQYJ74QM"))
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DRA4YXDZNALTA4KT73WNRMYSQ"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::UserFunc::UserFunc:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::UserFunc::UserFunc:All-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccZMCDFRDL4DOBDIEAAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
									}
									),

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::UserFunc::UserFunc:Library-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXBL56RWC2NAPDMES66PQYJ74QM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
									}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::UserFunc::UserFunc:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::UserFunc::UserFunc:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2Q4DNYD3ZHOBDCMTAALOMT5GDM"),"classGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ZRQY5J2HZDJJOX6KRXGTLD4XM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:userClassGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2U4DNYD3ZHOBDCMTAALOMT5GDM"),"userClassGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUV3IEIL7FCCPNWBF3O4BHG5FQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:upDefId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7QMAJND3ZHOBDCMTAALOMT5GDM"),"upDefId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUAWK5V3IZE2PD25UDIX3V6BIE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:upOwnerEntityId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UMAJND3ZHOBDCMTAALOMT5GDM"),"upOwnerEntityId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4N3LV2N2DRHFXLYRA4YYKT3LMQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:upOwnerPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7YMAJND3ZHOBDCMTAALOMT5GDM"),"upOwnerPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7LYHXK53FHL7AWV3I6STSEVR4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:lastUpdateUserName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCGFACL4ZHOBDCMTAALOMT5GDM"),"lastUpdateUserName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XD4EC4XZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:javaSrc-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTALTD5L3ZHOBDCMTAALOMT5GDM"),"javaSrc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZVMPR4UWZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.XML,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:javaRuntime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTELTD5L3ZHOBDCMTAALOMT5GDM"),"javaRuntime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSUXRA2ZYCVEUXFPRVQHFZPOIRE"),org.radixware.kernel.common.enums.EValType.BLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:extSrc-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYB2NT733ZHOBDCMTAALOMT5GDM"),"extSrc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCVWF5UUWZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYF2NT733ZHOBDCMTAALOMT5GDM"),"lastUpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBV63MAUXZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:methodId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKVOQE4YK7FBFTDFG42BCRRSZCI"),"methodId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJET4FUYTFDELDVA5FDANT7DUE"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:caching-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZZ4A46QO33OBDID5AALOMT5GDM"),"caching",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:ownerTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQY3XHAZTHZB57GEKAHI6K23SEM"),"ownerTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQKET7J6H2RHVPMW6OPVRQ5ZUFU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:description-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEY7LJLL77RDNLLC655BJGHGEL4"),"description",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGKNFFLGI2ZACHERCDHLX72BUAI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:userFunc-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2K5DN3ONERCMRFDHCINCC3DXMI"),"userFunc",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:displayProfile-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLKXRGY3PCRBR3ED4HQEB25EODE"),"displayProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDSKYHRHE3JDSDEC75Z3I6HHNWU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:version-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZVDPX2IPBCM7FVCASFB3JHBYM"),"version",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsML67B7EKHFE4DGNS6RTOX2NZNY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:currentVersion-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSPGLGCHLMZBXFIE4CJPZOMDCXY"),"currentVersion",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2X4FBYL2FVDJTL5JF775ZP4URM"),org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:ownerClassName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ4TLAHYKT5ECVLDKAXYMT44BXY"),"ownerClassName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls53PZY46IXFFORNNC64KWN6FW5A"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:isValid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5HQW5K6ILZEC7LCIL66JEZNP7U"),"isValid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls62NPHG2O6RAPZIP7IKPBG2BCI4"),org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:upOwnerClassId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEDT3H6EV4JF65HJPCQOZLCP2QQ"),"upOwnerClassId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOFA44NLVNJCA7DBT2VCDOM3A5Y"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:path-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYLLJTXZ45NHMLOCQFZSHR4PHOQ"),"path",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:ownerPropertyFullName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7MTIHMUG5ZHVNKMSTMAZAKOJOQ"),"ownerPropertyFullName",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:ownerPropertyName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLVJEVPXKWVFVLIBJL7NHJNZJKU"),"ownerPropertyName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY2G2XCQ53BH6ZCKSJVBJVFTGOA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:javaSrcVers-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOUPOLAY2WRH4DMCHP2YZEPZHRI"),"javaSrcVers",null,org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:editedSourceVersions-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGWV4ETLA5BH4BD32XHCBMRLQPM"),"editedSourceVersions",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:lastUpdateTimeGetter-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF7MJXVA7MRETTFN7JQ7RKZB5PY"),"lastUpdateTimeGetter",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:isOwnerExist-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHVASLNCB2JFJPLSJA3LATFOL2E"),"isOwnerExist",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:ownerJavaClassName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL2DOXTGV5ZGK3P5VFGI5KNQAEE"),"ownerJavaClassName",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:pathQuick-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDVV3C4FRCBA6THJPK4O2HIVOUA"),"pathQuick",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:ownerTitleQuick-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA323IBYJANBAHG67SUX4DEUCYY"),"ownerTitleQuick",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:libFuncGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col26N4VA2NARCLDDJPST4UQNDHIA"),"libFuncGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNGONIZIIEBC55PWT6HDFZUYDQI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:parameterNames-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL7ZR7JQORFDHNKNPTGS7FKIFWI"),"parameterNames",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:usedDefinitions-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS3ULKYQUEZAYBJR55WPBI77FQE"),"usedDefinitions",null,org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:linkedLibFunc-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXRTJJSPDT5F4ROOQGMN3K5MJDQ"),"linkedLibFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:paramBinding-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7J2X7AEIJNE3HDAI4RQQWGWKN4"),"paramBinding",null,org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:isLinkUsed-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPNLNJ2WNRJCBPBR6JPI3RZ4TVE"),"isLinkUsed",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPK4O44NEJ5ANBIL2CFSAIQBCZM"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:ownerLibFunc-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2WO2NG55AZFCBCB6XECNIMXEAE"),"ownerLibFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:paramsOrder-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTTBTQXWUX5E57GRPLMBFE4TSAM"),"paramsOrder",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:resultType-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2ALXV2PR4FBRJF4M77FXFVJVIA"),"resultType",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:usedLibraryFunctions-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd25D66ZTBSNE3BDBEDPTQ5UMDKA"),"usedLibraryFunctions",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWJB62JS7VA6JMU33ERLS62FQI"),org.radixware.kernel.common.enums.EValType.ARR_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBARCF67KSJAKRFXZWBPRBHYRM4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:optimizerCache-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT2MHPJYB2BG6POOUPZNQQQDKEY"),"optimizerCache",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:ownerPipelineId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ4J6Z23BTNDF5IAFKM6GNL3G6U"),"ownerPipelineId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:ownerLibFuncName-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK4HEWCSIIVCBDGFGIPYANPGLD4"),"ownerLibFuncName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXD45OLATONAVXPFBP65UADWZ5A"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2WO2NG55AZFCBCB6XECNIMXEAE")},org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:isOwnerCfgRef-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7VDQ66LIFBFXTGM27NTY3OIGVQ"),"isOwnerCfgRef",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:changeLog-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruOF2GYDTCGBGJ3EZV2QYRIC626I"),"changeLog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:isOwnerWasNotCreated-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdILZKB5MVKNAITADIRBN2C2BEZU"),"isOwnerWasNotCreated",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:upOwnerKnownFromImport-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA4TZTVDNXVB6ZIDI4HS7A3ISEY"),"upOwnerKnownFromImport",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:versionLockCount-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI36FQADHVNEOZJPWVLD7NCDCVA"),"versionLockCount",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:changeLogImportFromJmlEditor-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZHGKBYXPYBEN7LARKMVZVTJHVY"),"changeLogImportFromJmlEditor",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:initOfDuplicatedObj-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5EBXMZV5TNGCPIALU4RAGWGB6M"),"initOfDuplicatedObj",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:inheritedDescription-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPFPZWKHWJNFB5H4GA2XM6MYFE4"),"inheritedDescription",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:logInvoke-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd77PV6DO2FZC5JOX7BDLUKC75HY"),"logInvoke",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:lastChangelogRevisionDate-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRLFSWEPMAZGSXHC4Q33JZ4V7VI"),"lastChangelogRevisionDate",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:descriptionForSelector-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP73O4GPGEBHW7O4YHOK5RDTGF4"),"descriptionForSelector",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF4NDFLISBZHRVC5RVNOYO4R4RQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:PROPS_CONTROLLED_BY_ACCESS_SYSTEM-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLPWOD2BXTJDSJM2AX3OGQDIRYE"),"PROPS_CONTROLLED_BY_ACCESS_SYSTEM",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::UserFunc::UserFunc:forbidBindingXmlCopy-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2QZYKFGHQVGO5M35RTAZACSZPY"),"forbidBindingXmlCopy",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::UserFunc::UserFunc:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTPAFKDKW4TOBDIECAALOMT5GDM"),"invoke",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramVals",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7KQ364PPBREHDEXNO6ARUHPMXU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFOOX7DTCW5GA5CUK2VYOOVAC3Q"),"getMethodId",false,true,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6VMHJOAO33OBDID5AALOMT5GDM"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSVEAYKY6LJBQFLQKU5MMLABQOI"),"getDisplayName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSB64INNDIFF33GYI5MYIICR2WA"),"compile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemHandler",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr52M3C4QKVNDYRKTSZWANROV7YM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEVSZVRJMRGUNNVBPTPHVVNJNY"),"findUserFunc",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYNUHUII2UFB2ZD4724CV55IIFE"),"writeClob",true,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC536OAQNKRHZ5NUDNDSOFK3TKY"))
								},org.radixware.kernel.common.enums.EValType.CLOB),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth46ORU44NRREJRPAKLMMZB6LLAA"),"writeClob",true,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPQJAEDJ4SVBTBMBZ46T7AVZDWU"))
								},org.radixware.kernel.common.enums.EValType.CLOB),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBX5U2CVPRVBJHEJTWWCPF64GOY"),"getCurrentVersion",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG5PR53IMH5CKRP3RZZGMHIHWGA"),"getInitialText",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS7ABYGPNT5B6HFEPY6JVPFTTJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZV36FKS2BAQPNEBSLRFYAHR2Q"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA6Q7ARER2ZH77GLXAS4O6GXU7E"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW3C2BMJXMBFDVK5MO434UHDW5E"),"compile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("compiler",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQDQ5GM2SBRDQZFEWPBQCLNOUVI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemHandler",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7A3SE27LRBGOJHEKQFE46Z5GX4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resetRuntime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAYXYOEHBFFH5ZOJZOTLKKV5Y6I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fakeResult",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWCWM2QZ735DP7GS4EYATIJPKIY"))
								},org.radixware.kernel.common.enums.EValType.BLOB),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W3TRDKMDRCGFJYNWSP72FQKSE"),"afterUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4D6ME6VX45BYVNJXZPBTAA7KP4"),"onCalcTitle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVBOI6XTRJBB2VEUHX6LKNRX6ZY"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG6CUWVRSVJANJJAV4PB4Y2BIME"),"compile",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemHandler",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQRXBWQ3LNNGGZOIRNMLWNDYE3Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resetRuntime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFGUU5XM7PJE3BDB3KKBZV2AAWI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fakeResult",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJJMA2Q3OZ5FBFCF4K7BI37RG74"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6ERSQWLS3TOBDID3AALOMT5GDM"),"extSrcChanged",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH5ILCSETZHOBDCMTAALOMT5GDM"),"getDisplayProfile",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEMYVMA3RBVHUBFMCYMSXZCAFLY"),"baseTest",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD6XUMRSIBBFNXO5H3IBNGQ22YE"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.CLOB),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRSM3SI2PWNBZJCRX4UOWAQCGRE"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clob",org.radixware.kernel.common.enums.EValType.CLOB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDODCA5BYJZH6DIZM2G33ML7NHE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4BC72HPVYRAGHKULB3RDT3VJRI"),"listUserDefinitions",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("request",org.radixware.kernel.common.enums.EValType.ARR_INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE5XF6AHNJZA6RDXR35Q5CI56TU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6QOKMIBNWBD6TC5ITSDDXZL7DA"),"loadUserClassXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSU75A5WCBBAX3PN6I3NFC2764M"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHO4YGVHMJFFJNFDQU3YKNNBIBY"),"loadUserFuncXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("libUfId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6E2ZZBVTBVB3LLXR2RTXOPNK5Y"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJYIZEAJ7N5E4HO5IMVGSVLD7GE"),"readParameterNames",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.ARR_STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFARZIRBZ7NE4VJ7OLWJLBXZIEA"),"invoke",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6HDPO6SBL5GJNDTGTIAIKUSECE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPZUGQQ3A7BBRPNLPDEUDGOJRRA"),"findJavaMethod",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGZDY76GQDVEENNFB33JRO5ADCU"),"beforeInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("initPropValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW3WDQIFOBFGR7P6ERFKD5NWLWA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRNNE36LTJ5HAVC5O3AWOXYMO34")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJA2XQDNVCJBCHNUT2E2LE45DPQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBSYJJ3LXMJDEJFAZPSBJLMWVA4"),"invokeImpl",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramTypes",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSYZZ5PEARRDRHHCOUMNSCFFLF4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramVals",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCGGFJRPZENEQXAGYNULNNG23PU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIPN6XEMFTNANJCZRF3XHDG4OEE"),"findOwner",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHOE7GCVERBEMTMK7QMSRQFFOVM"),"invokeLinkedFunc",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramVals",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWZPOCPNBXFFGRMRRVDQIFEQEFI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS4HA7JHPTBDQ3HVT43OPWAD3II"),"diagnoseCompile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthL2VAYJU7NJDHPCF5CN3NPWFCEA"),"diagnoseCompileImpl",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemHandler",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTM64RQ2YUJCO5FWURXI6QAQGGI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3WMCTSIXFNG6FIEURCGZSAWWZE"),"validateLinkedFuncParams",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problemPacker",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTQWNADOWVVEW3KZZSV7VSVFJSU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdJSJIK2BKOFAP5DOLQIICYERBAQ"),"onCommand_MoveToLibrary",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVLYKSMWK7ZC3FA6Z67LBBXWBLU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRWKTUXDPJBE5TAUYFJXKQN27QQ"),"exportForExchange",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNZ6IUYGMBNDFLJPVWD4E57ZCUQ"),"getUsedLibraryFunctions",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.ARR_REF),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upDefId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7QMAJND3ZHOBDCMTAALOMT5GDM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upOwnerEntityId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7UMAJND3ZHOBDCMTAALOMT5GDM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upOwnerPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7YMAJND3ZHOBDCMTAALOMT5GDM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMPKTZGNQZVDO5PTCXXV7VDDFKM"),"import",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPSIDMSIHTNGOBLOXIAGIA3UYFQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLY77MYIYTRBS7E7K4S55YPONEQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isSet",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCVV6DD66Y5FDBHT7UVHPR43KHU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPUMUJGX63BA5VJA3NC53FKKJGM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIGV3QWJDFNAE3MFYJSOZSDOYHQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNTUG6GM5O5DU5OKUKZINIH55AM"),"findOwnerPipelineId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth76S7WFDECFGR5FWR4QV2UTDXEE"),"import",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYM7LYL22ERBCTDXH4NSV2M72L4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJGDXYXHI7NGQDCTDXGOYGL74YI"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPKKIJ4RT5RFKDAPNR6FV5DWHQ4"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAHHRDSNRYBA7XI4RMF37BHBKDE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ignoreErrors",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5AH5MPFNH5AWHEH7VARQJUQTWE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEBGBCLORYFF33GL7EFMZYKGD4M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4Y6NAQOLCVHRTICWCCJXGV73Y4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHTGUASQHEFE7DO354LIIMKFOPQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2642COJAA5E3NJ3SUNXEL4BFBI"),"calcDbObjectsExtGuids",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("jmlSrc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ7HMAFKQBBGT3ATI6P6R6NANAQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPD6HYUWPSJDIBNQCHEANO67R4M"),"processDbTags",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("jmlSrc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ7HMAFKQBBGT3ATI6P6R6NANAQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAPVZ6DUADZH5TE4JS3N6THT23E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZK3QJAXBQJB2TKZD3T2FUFJQSU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNNAALOTN7RCFVGFSSNX2XBTTRA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJZO4LRC5R5EKHE5HELHHNWYB2M"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYNLSO3DJM5BNXFLKWZS2Z3FK7E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xUserFuncSrc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA36FLMKF3VBF7LSTM2GT4HC3CI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVF5XZA7DVRAM7LGIVKME55SWLU"),"replaceFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC73DQYYRBFAWFOPU7CZVEPTI5M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE2ICNCKHVVFWRKVEOVMS2MCER4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTJ2UJAGVZRDYDF2LIZYDSTOYLM"),"resolveEntityByExtGuid",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entityId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMKQLIRN5XVAFZJU5FWAQYXYAGM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC6BVNH5IVVD4NCB65Y7USNKA5Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr36TXNBRKBBF3JFQIC4LN3LAJM4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNZVKJSVSG5A45C5ONPLGANAR4M"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO2YSHAGD2RFKZBP3ZE7FNWB6OE"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCJ7UXTJFUJCUFL75Y6ZTXEVODY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQVD6AP3P7VBJJLOT4IOQ5XJKXE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isSet",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHP3LXHQLKFEP7OEVYOTRK5AVYM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAHHRDSNRYBA7XI4RMF37BHBKDE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHTGUASQHEFE7DO354LIIMKFOPQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdWSK3HNPKXNCLFFMSYQ7XZCGOKI"),"onCommand_Export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPBBIZD7R5JFQHIRX6ZK26NIB7Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr44MC2KINB5BLXJMDIR7OT2QWCY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHCKNU57MAZFRRLSJEW6BCT5NSI"),"reportResolveWarnings",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("objects",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNNS3GQ5P4ZGBJCGKCUJVVAE77U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC6BVNH5IVVD4NCB65Y7USNKA5Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMI3Y24UFBRD4JDID4SXYPAZEUQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHC2Y6NG45BEMLMZ5MUYQYMA4NI"),"create",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLBCIFLB5ZFHG3PSHO6KGWI2EYE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ3HQ5OT4G5E45NWMJOGG4R3WCU"),"export",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xUserFuncSrc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHN56TS647JBLDPFLQUWRMVTIZQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth25C77BFEKRBHTPN7OMCPZW5FDU"),"actualizeUpOwnerPid",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xmlDoc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBPO2SB22KNCBLESP6JP7CRHFUU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZINMBQGKRHR7KA7ELET7QSGOI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCZLF7OBQ6VCINLTZUKXBJISWCU"),"actualizeUpOwnerPid",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("jmlItemsList",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTNTXRLLEL5EQ7OUXRKB72EJKYY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFOGQR5YXFVFXJO4273ELYVCDWM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNHUNBALTMZC6ZIVKYCIEYED3OY"),"actualizeUpOwnerPid",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sourceVersions",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSJBBTTM6GJFDVBTMGPQETKK7S4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("owner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr55ZM6KB4CNHXNAAHBJ4ZZCFQVQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7VSQ5EZG5VFWRCN4ML3XRSHKVA"),"appendToUsedDefinitionsList",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("usedWrappers",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVPOM3BN26VDSFF6GAOF3TF4DXU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH5KIOUM3NZE2DCBXHD6EXW4X3I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthX7XPNMOB7ZH5HEM4SOGAGA4N7A"),"actualizeUsedDefinitions",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN5W43F34FRGKNFS764PPDQF7WY"),"getCompilationErrorMessage",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("userFunc",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSMRMHJHPTFDTJLML7SJF4L5CA4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("problem",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprORPNKXG4FJDPHD2X22XDHEXWJQ"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth63BUKCYF3FA7LP4HZQMBCOS3FU"),"reportIsStateValid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRGJLATERGBBKLA22XBLXXYZZSI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7SW2XMLB5VGL3BOVIUJL22AHD4"),"reportImportWarnings",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAZH5ZC24VJDVDBBF6XZ4QWLF3U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAEBJ2C2UONE6NOG54MESW6T5RE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ45FW6GQNVB7PG6MBBPRS2JIQY"),"checkEntitiesExistance",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidsToCheck",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNPLB5222KFBZBGDWNWT7BBDJFU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdPQTVYTIC2RHXZGE7NPGUP3OMOE"),"onCommand_CheckEntitiesExistance",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIPQRAXRZ7JCDJNDTAG3HTZMY3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7Y73ZPJKPRGP7B6YGFO3OOPDKU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIU7T5CFLQRDGTGLPK4ZD37FYNA"),"reportDbTagEntitiesNotExistsInDb",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pids2EntityId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEMMHQETKAVH6ZIQWUJ6DL2BRCA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEL3M6LLSHZDJNP4JYLJ4PSDYII"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2SR4ZT5X35GWFMHWEIZIRNIFJE"),"actualizeDbTagPidByExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xDbTag",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHWEN6AS525FX3FBBLLPVG6OFII")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQODIUQ4CLZAY3CBG6V2EP57JIE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprURFTGWESNFFMBDQ7WKV4Z3VOVE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7TD2RA6FZRBRRBTERDNKWDO4N4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWOAXEQNQVJFYFNPLD5FYT6ONBY"),"getCurrentVersionMethodId",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4IMANV3AJFCFZNX2YJVLVBCXXI"),"lockVersion",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKBRYI4GTNFHDZP3VZSY5246NKA"),"unlockVersion",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSG5BEVBIFJCLNKF2MB7DUTHZRY"),"getLockReleaserCacheKey",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2ID6LLB4ORG75JFEH45HYYK75U"),"copyPropVal",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prop",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVW3CI7LDWZBY3FPG6ILYW4O2QY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOCJOBGU6J5EDTAYT47O4TBVX6M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO3RJ6GNVJJA5HOCK3UQHMJWHIA"),"saveCurSrcVersionBeforeImport",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBAF3W2NYTBEV7MV26G2JMREZHI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthENQF5POD4VFAZBFDZO2C7AD42I"),"shouldSaveSrcVersion",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7F3GMQH6QJFADENZFW7VP73AKM"),"isOwnerContainsLookupAdvizor",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.BOOL),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMVCXE2I4KJDFHF6U4ENLPBNXAY"),"getJavaSrcInitValue",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("srcFunc",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL5D4WDPMIZEE5NP57PBUM5PORY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYSUG2C4UZVDQHL3TXNV2CQ56ZA"),"getInvokeDescr",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJHWFPA6SFVCZ5IIHJ772S2ETBA"),"getDescriptionWithChangelogInfo",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("descr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWNDFXHAJPRB6XOUIPAKVVI5WUM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDZEF4S7FSNBBDAWZPJLOZG47K4"),"getAdditionalOwners",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7IJDDAYGA5GRRF7SUG22DOEZ4M"),"getDisplayProfile",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reread",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWDWJMBJN6NED3IQUQZFK57X6XM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMIG2SQ22QZHQNLXRCLCWTQEV7Q"),"checkUserRights",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isUpdate",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMACIJX3VU5HJ3D77QFEBMKX3KQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCL2DHGLXENEF5L65GUMSDJGR2Q"),"onJavaSrcChanged",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newVal",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3RPEIQ2W7BA3XLVMSUZ3Z6BEDQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4M2BMLDPNVH5BGGWIJI6MG35H4"),"actualizeUserFuncOwnerBinding",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("binding",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL2XDV22UTNGFFCHQIGCTDKV3PY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upOwnerClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2AZZBEERSNCNNGKOJLDXCPFWPI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upOwnerOldPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAPJV7IUTEVBEHEPSFELJYBN5MY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upOwnerNewPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUI2AYGOXMNCCJBTDIVKZ5J2D2E"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adc6KN4L7WAD5HDRCOOQRMI3AN424 = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adc6KN4L7WAD5HDRCOOQRMI3AN424"),"UserFuncImporter",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,
						null,
						null,
						null,

						/*Radix::UserFunc::UserFunc:UserFuncImporter:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::UserFunc::UserFunc:UserFuncImporter:considerContext-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFKPRP3P2GZHCDIMO5KQ5W5SQEU"),"considerContext",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,null),

								/*Radix::UserFunc::UserFunc:UserFuncImporter:context-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdC6OD3SUJZJGBTIX5PWHO7NOVDA"),"context",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,null),

								/*Radix::UserFunc::UserFunc:UserFuncImporter:helper-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd33T5ZMVBZBHVPCMAEDFG2BOX3I"),"helper",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,null),

								/*Radix::UserFunc::UserFunc:UserFuncImporter:isSet-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQOUGCKD5UZETJLZ4HZNFK4JZPM"),"isSet",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,null),

								/*Radix::UserFunc::UserFunc:UserFuncImporter:propId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBPCBCYJLTFABHLPLKPD2QUFRWM"),"propId",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,null),

								/*Radix::UserFunc::UserFunc:UserFuncImporter:propOwner-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7EB4R3OLLZBIRE76OF22Y6I2YY"),"propOwner",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,null),

								/*Radix::UserFunc::UserFunc:UserFuncImporter:xml-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4EQLJ2A4QBATPJSV3Y33B2OYQ4"),"xml",null,org.radixware.kernel.common.enums.EValType.XML,null,null,null)
						},

						/*Radix::UserFunc::UserFunc:UserFuncImporter:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS5N6UHZVNNFSRCWH6JAGE27LFQ"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMPWTCDQ7ZBDRLI6EKZWYZXKWNY"),"setContext",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEKWHC73DIBDFTGQWFY2TY32AMY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE544AAKCEZCMXKMO3IJ6TTHAGM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI3IM6O3QW5FV3GC4TAA4XDR2QY"),"setHelper",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU63UKY7ZRRC7JB4DMDXHO7E2DQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2JC42QQCVBC53CBR6VREC2RL34"),"setPropParams",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKPR3PU4AQZBFPGCDZZHMO66N6M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWBDGN244P5DPNHPBPB3DCAX5CA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isSet",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLGLTAP6HAVAC7FGSR2CZMKOCBY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLJYCU5FWHFHGBFSKM6TH4R3ORM"),"setUserFuncXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3ADOL7HSWZBUNO2LSXI62GRDNA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXZKVJVWVZNGGRAFHOHQRFH36FM"),"reset",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTY25OMSVTNHWFI2ZNYNFOUQCCE"),"UserFuncImporter",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth23D377QPPZD27G3DAFALLWFPSU"),"UserFuncImporter",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFWYYS5BQIRD5HK4NP3JIBLFJFA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLD235X3DNFDWVHXMV2N3V2H63M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isSet",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPXLXQHQHJJDGTG72I2QWD6XRM4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBFZAYCW6VVAPTLAAPJLGN4EWLU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQPKCJQQ4YNATXFRYVPT3IUFH7Y"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRRDMYAIZLNEHBOUP6JB5IKWURY"),"UserFuncImporter",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDC6QGLRDOBARPETSBRJDUKGBQY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQFGCBG2JKVAXLLFYHC5K7BKVKM"),"import",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("func",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKWM5HIGGGNCVBMHVZ3BNGBJ43U"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6H5S7KOK5NHF3BCXTCJOK4QJLM"),"actualizeXmlUserPropInfo",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6MHZLLPIFRGHVPLBYGDXF36EEA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propOwner",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMTESR433M5EQ5IUSJ36S3NR3HY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXZVO4FHQMVAKVLTPDDPOCOIJXI"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::UserFunc::UserFunc - Desktop Executable*/

/*Radix::UserFunc::UserFunc-Entity Class*/

package org.radixware.ads.UserFunc.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc")
public interface UserFunc {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel )  super.getEntity(i);}
	}
































































































































































































































































































	/*Radix::UserFunc::UserFunc:libFuncGuid:libFuncGuid-Presentation Property*/


	public class LibFuncGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LibFuncGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:libFuncGuid:libFuncGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:libFuncGuid:libFuncGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LibFuncGuid getLibFuncGuid();
	/*Radix::UserFunc::UserFunc:classGuid:classGuid-Presentation Property*/


	public class ClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::UserFunc::UserFunc:userClassGuid:userClassGuid-Presentation Property*/


	public class UserClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UserClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:userClassGuid:userClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:userClassGuid:userClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UserClassGuid getUserClassGuid();
	/*Radix::UserFunc::UserFunc:id:id-Presentation Property*/


	public class Id extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Id(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::UserFunc::UserFunc:paramBinding:paramBinding-Presentation Property*/


	public class ParamBinding extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public ParamBinding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:paramBinding:paramBinding")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:paramBinding:paramBinding")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ParamBinding getParamBinding();
	/*Radix::UserFunc::UserFunc:upDefId:upDefId-Presentation Property*/


	public class UpDefId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UpDefId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upDefId:upDefId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upDefId:upDefId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UpDefId getUpDefId();
	/*Radix::UserFunc::UserFunc:upOwnerEntityId:upOwnerEntityId-Presentation Property*/


	public class UpOwnerEntityId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UpOwnerEntityId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerEntityId:upOwnerEntityId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerEntityId:upOwnerEntityId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UpOwnerEntityId getUpOwnerEntityId();
	/*Radix::UserFunc::UserFunc:upOwnerPid:upOwnerPid-Presentation Property*/


	public class UpOwnerPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UpOwnerPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerPid:upOwnerPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerPid:upOwnerPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UpOwnerPid getUpOwnerPid();
	/*Radix::UserFunc::UserFunc:description:description-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::UserFunc::UserFunc:ownerLibFuncName:ownerLibFuncName-Presentation Property*/


	public class OwnerLibFuncName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OwnerLibFuncName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerLibFuncName:ownerLibFuncName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerLibFuncName:ownerLibFuncName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OwnerLibFuncName getOwnerLibFuncName();
	/*Radix::UserFunc::UserFunc:lastUpdateUserName:lastUpdateUserName-Presentation Property*/


	public class LastUpdateUserName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUserName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastUpdateUserName:lastUpdateUserName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastUpdateUserName:lastUpdateUserName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUserName getLastUpdateUserName();
	/*Radix::UserFunc::UserFunc:javaSrcVers:javaSrcVers-Presentation Property*/


	public class JavaSrcVers extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public JavaSrcVers(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaSrcVers:javaSrcVers")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaSrcVers:javaSrcVers")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JavaSrcVers getJavaSrcVers();
	/*Radix::UserFunc::UserFunc:isLinkUsed:isLinkUsed-Presentation Property*/


	public class IsLinkUsed extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsLinkUsed(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isLinkUsed:isLinkUsed")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isLinkUsed:isLinkUsed")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsLinkUsed getIsLinkUsed();
	/*Radix::UserFunc::UserFunc:version:version-Presentation Property*/


	public class Version extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Version(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:version:version")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:version:version")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Version getVersion();
	/*Radix::UserFunc::UserFunc:usedDefinitions:usedDefinitions-Presentation Property*/


	public class UsedDefinitions extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public UsedDefinitions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:usedDefinitions:usedDefinitions")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:usedDefinitions:usedDefinitions")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UsedDefinitions getUsedDefinitions();
	/*Radix::UserFunc::UserFunc:javaSrc:javaSrc-Presentation Property*/


	public class JavaSrc extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public JavaSrc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument> getValClass(){
			return org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument dummy = x == null ? null : (org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaSrc:javaSrc")
		public  org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaSrc:javaSrc")
		public   void setValue(org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument val) {
			Value = val;
		}
	}
	public JavaSrc getJavaSrc();
	/*Radix::UserFunc::UserFunc:javaRuntime:javaRuntime-Presentation Property*/


	public class JavaRuntime extends org.radixware.kernel.common.client.models.items.properties.PropertyBlob{
		public JavaRuntime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaRuntime:javaRuntime")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:javaRuntime:javaRuntime")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public JavaRuntime getJavaRuntime();
	/*Radix::UserFunc::UserFunc:extSrc:extSrc-Presentation Property*/


	public class ExtSrc extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public ExtSrc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:extSrc:extSrc")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:extSrc:extSrc")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtSrc getExtSrc();
	/*Radix::UserFunc::UserFunc:lastUpdateTime:lastUpdateTime-Presentation Property*/


	public class LastUpdateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastUpdateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::UserFunc::UserFunc:usedLibraryFunctions:usedLibraryFunctions-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:usedLibraryFunctions:usedLibraryFunctions")
		public  org.radixware.kernel.common.client.types.ArrRef getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:usedLibraryFunctions:usedLibraryFunctions")
		public   void setValue(org.radixware.kernel.common.client.types.ArrRef val) {
			Value = val;
		}
	}
	public UsedLibraryFunctions getUsedLibraryFunctions();
	/*Radix::UserFunc::UserFunc:resultType:resultType-Presentation Property*/


	public class ResultType extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ResultType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:resultType:resultType")
		public  org.radixware.schemas.xscml.TypeDeclarationDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:resultType:resultType")
		public   void setValue(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
			Value = val;
		}
	}
	public ResultType getResultType();
	/*Radix::UserFunc::UserFunc:ownerLibFunc:ownerLibFunc-Presentation Property*/


	public class OwnerLibFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public OwnerLibFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.LibUserFunc.LibUserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.LibUserFunc.LibUserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.LibUserFunc.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.UserFunc.explorer.LibUserFunc.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerLibFunc:ownerLibFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerLibFunc:ownerLibFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public OwnerLibFunc getOwnerLibFunc();
	/*Radix::UserFunc::UserFunc:isValid:isValid-Presentation Property*/


	public class IsValid extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsValid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isValid:isValid")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isValid:isValid")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsValid getIsValid();
	/*Radix::UserFunc::UserFunc:ownerPropertyFullName:ownerPropertyFullName-Presentation Property*/


	public class OwnerPropertyFullName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OwnerPropertyFullName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPropertyFullName:ownerPropertyFullName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPropertyFullName:ownerPropertyFullName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OwnerPropertyFullName getOwnerPropertyFullName();
	/*Radix::UserFunc::UserFunc:isOwnerCfgRef:isOwnerCfgRef-Presentation Property*/


	public class IsOwnerCfgRef extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsOwnerCfgRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerCfgRef:isOwnerCfgRef")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerCfgRef:isOwnerCfgRef")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsOwnerCfgRef getIsOwnerCfgRef();
	/*Radix::UserFunc::UserFunc:ownerTitleQuick:ownerTitleQuick-Presentation Property*/


	public class OwnerTitleQuick extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OwnerTitleQuick(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerTitleQuick:ownerTitleQuick")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerTitleQuick:ownerTitleQuick")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OwnerTitleQuick getOwnerTitleQuick();
	/*Radix::UserFunc::UserFunc:pathQuick:pathQuick-Presentation Property*/


	public class PathQuick extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PathQuick(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:pathQuick:pathQuick")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:pathQuick:pathQuick")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PathQuick getPathQuick();
	/*Radix::UserFunc::UserFunc:upOwnerClassId:upOwnerClassId-Presentation Property*/


	public class UpOwnerClassId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UpOwnerClassId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerClassId:upOwnerClassId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:upOwnerClassId:upOwnerClassId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UpOwnerClassId getUpOwnerClassId();
	/*Radix::UserFunc::UserFunc:editedSourceVersions:editedSourceVersions-Presentation Property*/


	public class EditedSourceVersions extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public EditedSourceVersions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:editedSourceVersions:editedSourceVersions")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:editedSourceVersions:editedSourceVersions")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public EditedSourceVersions getEditedSourceVersions();
	/*Radix::UserFunc::UserFunc:isOwnerExist:isOwnerExist-Presentation Property*/


	public class IsOwnerExist extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsOwnerExist(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerExist:isOwnerExist")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerExist:isOwnerExist")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsOwnerExist getIsOwnerExist();
	/*Radix::UserFunc::UserFunc:isOwnerWasNotCreated:isOwnerWasNotCreated-Presentation Property*/


	public class IsOwnerWasNotCreated extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsOwnerWasNotCreated(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerWasNotCreated:isOwnerWasNotCreated")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:isOwnerWasNotCreated:isOwnerWasNotCreated")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsOwnerWasNotCreated getIsOwnerWasNotCreated();
	/*Radix::UserFunc::UserFunc:methodId:methodId-Presentation Property*/


	public class MethodId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MethodId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:methodId:methodId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:methodId:methodId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MethodId getMethodId();
	/*Radix::UserFunc::UserFunc:ownerJavaClassName:ownerJavaClassName-Presentation Property*/


	public class OwnerJavaClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OwnerJavaClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerJavaClassName:ownerJavaClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerJavaClassName:ownerJavaClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OwnerJavaClassName getOwnerJavaClassName();
	/*Radix::UserFunc::UserFunc:parameterNames:parameterNames-Presentation Property*/


	public class ParameterNames extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ParameterNames(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:parameterNames:parameterNames")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:parameterNames:parameterNames")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ParameterNames getParameterNames();
	/*Radix::UserFunc::UserFunc:displayProfile:displayProfile-Presentation Property*/


	public class DisplayProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DisplayProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:displayProfile:displayProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:displayProfile:displayProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DisplayProfile getDisplayProfile();
	/*Radix::UserFunc::UserFunc:ownerPropertyName:ownerPropertyName-Presentation Property*/


	public class OwnerPropertyName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OwnerPropertyName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPropertyName:ownerPropertyName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPropertyName:ownerPropertyName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OwnerPropertyName getOwnerPropertyName();
	/*Radix::UserFunc::UserFunc:descriptionForSelector:descriptionForSelector-Presentation Property*/


	public class DescriptionForSelector extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DescriptionForSelector(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:descriptionForSelector:descriptionForSelector")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:descriptionForSelector:descriptionForSelector")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DescriptionForSelector getDescriptionForSelector();
	/*Radix::UserFunc::UserFunc:inheritedDescription:inheritedDescription-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:inheritedDescription:inheritedDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:inheritedDescription:inheritedDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InheritedDescription getInheritedDescription();
	/*Radix::UserFunc::UserFunc:ownerTitle:ownerTitle-Presentation Property*/


	public class OwnerTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OwnerTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerTitle:ownerTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerTitle:ownerTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OwnerTitle getOwnerTitle();
	/*Radix::UserFunc::UserFunc:lastChangelogRevisionDate:lastChangelogRevisionDate-Presentation Property*/


	public class LastChangelogRevisionDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastChangelogRevisionDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastChangelogRevisionDate:lastChangelogRevisionDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:lastChangelogRevisionDate:lastChangelogRevisionDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastChangelogRevisionDate getLastChangelogRevisionDate();
	/*Radix::UserFunc::UserFunc:currentVersion:currentVersion-Presentation Property*/


	public class CurrentVersion extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurrentVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:currentVersion:currentVersion")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:currentVersion:currentVersion")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurrentVersion getCurrentVersion();
	/*Radix::UserFunc::UserFunc:optimizerCache:optimizerCache-Presentation Property*/


	public class OptimizerCache extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public OptimizerCache(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:optimizerCache:optimizerCache")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:optimizerCache:optimizerCache")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public OptimizerCache getOptimizerCache();
	/*Radix::UserFunc::UserFunc:linkedLibFunc:linkedLibFunc-Presentation Property*/


	public class LinkedLibFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public LinkedLibFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.LibUserFunc.LibUserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.LibUserFunc.LibUserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.LibUserFunc.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.UserFunc.explorer.LibUserFunc.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:linkedLibFunc:linkedLibFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:linkedLibFunc:linkedLibFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public LinkedLibFunc getLinkedLibFunc();
	/*Radix::UserFunc::UserFunc:path:path-Presentation Property*/


	public class Path extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Path(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:path:path")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:path:path")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Path getPath();
	/*Radix::UserFunc::UserFunc:ownerPipelineId:ownerPipelineId-Presentation Property*/


	public class OwnerPipelineId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OwnerPipelineId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPipelineId:ownerPipelineId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerPipelineId:ownerPipelineId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OwnerPipelineId getOwnerPipelineId();
	/*Radix::UserFunc::UserFunc:ownerClassName:ownerClassName-Presentation Property*/


	public class OwnerClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OwnerClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerClassName:ownerClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ownerClassName:ownerClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OwnerClassName getOwnerClassName();
	/*Radix::UserFunc::UserFunc:changeLogImportFromJmlEditor:changeLogImportFromJmlEditor-Presentation Property*/


	public class ChangeLogImportFromJmlEditor extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ChangeLogImportFromJmlEditor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.commondef.ChangeLogDocument> getValClass(){
			return org.radixware.schemas.commondef.ChangeLogDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.commondef.ChangeLogDocument dummy = x == null ? null : (org.radixware.schemas.commondef.ChangeLogDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.commondef.ChangeLogDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:changeLogImportFromJmlEditor:changeLogImportFromJmlEditor")
		public  org.radixware.schemas.commondef.ChangeLogDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:changeLogImportFromJmlEditor:changeLogImportFromJmlEditor")
		public   void setValue(org.radixware.schemas.commondef.ChangeLogDocument val) {
			Value = val;
		}
	}
	public ChangeLogImportFromJmlEditor getChangeLogImportFromJmlEditor();
	/*Radix::UserFunc::UserFunc:changeLog:changeLog-Presentation Property*/


	public class ChangeLog extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ChangeLog(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:changeLog:changeLog")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:changeLog:changeLog")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLog getChangeLog();
	public static class RemoteCall_loadUserClassXml extends org.radixware.kernel.common.client.models.items.Command{
		protected RemoteCall_loadUserClassXml(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class RemoteCall_getCurrentVersionMethodId extends org.radixware.kernel.common.client.models.items.Command{
		protected RemoteCall_getCurrentVersionMethodId(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class RemoteCall_loadUserFuncXml extends org.radixware.kernel.common.client.models.items.Command{
		protected RemoteCall_loadUserFuncXml(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class MoveToLibrary extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveToLibrary(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class RemoteCall_isOwnerContainsLookupAdvizor extends org.radixware.kernel.common.client.models.items.Command{
		protected RemoteCall_isOwnerContainsLookupAdvizor(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class CheckEntitiesExistance extends org.radixware.kernel.common.client.models.items.Command{
		protected CheckEntitiesExistance(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.MapStrStrDocument send(org.radixware.schemas.types.MapStrStrDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.MapStrStrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.MapStrStrDocument.class);
		}

	}

	public static class RemoteCall_listUserDefinitions extends org.radixware.kernel.common.client.models.items.Command{
		protected RemoteCall_listUserDefinitions(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.types.MapStrStrDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}



}

/* Radix::UserFunc::UserFunc - Desktop Meta*/

/*Radix::UserFunc::UserFunc-Entity Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::UserFunc::UserFunc:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
			"Radix::UserFunc::UserFunc",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMZS5KGUHCVAM5ISGUXXBNE3QKY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DRA4YXDZNALTA4KT73WNRMYSQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKNZKVTMXZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),0,

			/*Radix::UserFunc::UserFunc:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::UserFunc::UserFunc:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2Q4DNYD3ZHOBDCMTAALOMT5GDM"),
						"classGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ZRQY5J2HZDJJOX6KRXGTLD4XM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,29,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:userClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2U4DNYD3ZHOBDCMTAALOMT5GDM"),
						"userClassGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUV3IEIL7FCCPNWBF3O4BHG5FQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:userClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:upDefId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7QMAJND3ZHOBDCMTAALOMT5GDM"),
						"upDefId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUAWK5V3IZE2PD25UDIX3V6BIE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:upDefId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:upOwnerEntityId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UMAJND3ZHOBDCMTAALOMT5GDM"),
						"upOwnerEntityId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4N3LV2N2DRHFXLYRA4YYKT3LMQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:upOwnerEntityId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:upOwnerPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7YMAJND3ZHOBDCMTAALOMT5GDM"),
						"upOwnerPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7LYHXK53FHL7AWV3I6STSEVR4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom(""),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:upOwnerPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:lastUpdateUserName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCGFACL4ZHOBDCMTAALOMT5GDM"),
						"lastUpdateUserName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XD4EC4XZHOBDCMTAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:lastUpdateUserName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:javaSrc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTALTD5L3ZHOBDCMTAALOMT5GDM"),
						"javaSrc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZVMPR4UWZHOBDCMTAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:javaRuntime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTELTD5L3ZHOBDCMTAALOMT5GDM"),
						"javaRuntime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSUXRA2ZYCVEUXFPRVQHFZPOIRE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						true,
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:extSrc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYB2NT733ZHOBDCMTAALOMT5GDM"),
						"extSrc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCVWF5UUWZHOBDCMTAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.CLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
						null,
						null,
						null,
						false,
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

						/*Radix::UserFunc::UserFunc:extSrc:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYF2NT733ZHOBDCMTAALOMT5GDM"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBV63MAUXZHOBDCMTAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:methodId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKVOQE4YK7FBFTDFG42BCRRSZCI"),
						"methodId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJET4FUYTFDELDVA5FDANT7DUE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:methodId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:ownerTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQY3XHAZTHZB57GEKAHI6K23SEM"),
						"ownerTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQKET7J6H2RHVPMW6OPVRQ5ZUFU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						0,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:ownerTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEY7LJLL77RDNLLC655BJGHGEL4"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGKNFFLGI2ZACHERCDHLX72BUAI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
						null,
						null,
						null,
						false,
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

						/*Radix::UserFunc::UserFunc:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:displayProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLKXRGY3PCRBR3ED4HQEB25EODE"),
						"displayProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDSKYHRHE3JDSDEC75Z3I6HHNWU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:displayProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:version:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZVDPX2IPBCM7FVCASFB3JHBYM"),
						"version",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsML67B7EKHFE4DGNS6RTOX2NZNY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:version:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:currentVersion:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSPGLGCHLMZBXFIE4CJPZOMDCXY"),
						"currentVersion",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2X4FBYL2FVDJTL5JF775ZP4URM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:currentVersion:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:ownerClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ4TLAHYKT5ECVLDKAXYMT44BXY"),
						"ownerClassName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls53PZY46IXFFORNNC64KWN6FW5A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:ownerClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:isValid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5HQW5K6ILZEC7LCIL66JEZNP7U"),
						"isValid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls62NPHG2O6RAPZIP7IKPBG2BCI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:isValid:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:upOwnerClassId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEDT3H6EV4JF65HJPCQOZLCP2QQ"),
						"upOwnerClassId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOFA44NLVNJCA7DBT2VCDOM3A5Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:upOwnerClassId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:path:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYLLJTXZ45NHMLOCQFZSHR4PHOQ"),
						"path",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:path:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:ownerPropertyFullName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7MTIHMUG5ZHVNKMSTMAZAKOJOQ"),
						"ownerPropertyFullName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:ownerPropertyFullName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:ownerPropertyName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLVJEVPXKWVFVLIBJL7NHJNZJKU"),
						"ownerPropertyName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY2G2XCQ53BH6ZCKSJVBJVFTGOA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:ownerPropertyName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:javaSrcVers:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOUPOLAY2WRH4DMCHP2YZEPZHRI"),
						"javaSrcVers",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:javaSrcVers:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:editedSourceVersions:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGWV4ETLA5BH4BD32XHCBMRLQPM"),
						"editedSourceVersions",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:editedSourceVersions:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:isOwnerExist:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHVASLNCB2JFJPLSJA3LATFOL2E"),
						"isOwnerExist",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:isOwnerExist:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:ownerJavaClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL2DOXTGV5ZGK3P5VFGI5KNQAEE"),
						"ownerJavaClassName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:ownerJavaClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:pathQuick:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDVV3C4FRCBA6THJPK4O2HIVOUA"),
						"pathQuick",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:pathQuick:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:ownerTitleQuick:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA323IBYJANBAHG67SUX4DEUCYY"),
						"ownerTitleQuick",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						0,
						null,
						null,
						null,
						false,
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

						/*Radix::UserFunc::UserFunc:ownerTitleQuick:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:libFuncGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col26N4VA2NARCLDDJPST4UQNDHIA"),
						"libFuncGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNGONIZIIEBC55PWT6HDFZUYDQI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:libFuncGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:parameterNames:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL7ZR7JQORFDHNKNPTGS7FKIFWI"),
						"parameterNames",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:parameterNames:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:usedDefinitions:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS3ULKYQUEZAYBJR55WPBI77FQE"),
						"usedDefinitions",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:usedDefinitions:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:linkedLibFunc:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXRTJJSPDT5F4ROOQGMN3K5MJDQ"),
						"linkedLibFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCZX6P4BJUBBZRDPUQ3P74DQJXE")},
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2Z3PQIOYMBE2FM2DX74KBB7D7A"),
						133693439,
						133693439,false),

					/*Radix::UserFunc::UserFunc:paramBinding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7J2X7AEIJNE3HDAI4RQQWGWKN4"),
						"paramBinding",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:paramBinding:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:isLinkUsed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPNLNJ2WNRJCBPBR6JPI3RZ4TVE"),
						"isLinkUsed",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPK4O44NEJ5ANBIL2CFSAIQBCZM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:isLinkUsed:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:ownerLibFunc:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2WO2NG55AZFCBCB6XECNIMXEAE"),
						"ownerLibFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						21,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr74SXZSVAHBF4NEXED3ZRFZ7XFU")},
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::UserFunc::UserFunc:resultType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2ALXV2PR4FBRJF4M77FXFVJVIA"),
						"resultType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:usedLibraryFunctions:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd25D66ZTBSNE3BDBEDPTQ5UMDKA"),
						"usedLibraryFunctions",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::UserFunc::UserFunc:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBARCF67KSJAKRFXZWBPRBHYRM4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD5BW5C77CNC2XLXQDTTPLCWR7M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:optimizerCache:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT2MHPJYB2BG6POOUPZNQQQDKEY"),
						"optimizerCache",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:optimizerCache:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:ownerPipelineId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ4J6Z23BTNDF5IAFKM6GNL3G6U"),
						"ownerPipelineId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:ownerPipelineId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:ownerLibFuncName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK4HEWCSIIVCBDGFGIPYANPGLD4"),
						"ownerLibFuncName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						63,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecW7BRVVQLHBE2FCNK7ZBRMAUJGM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ACZLKUIERHQXKMX5Z7X5URPAY"),
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:isOwnerCfgRef:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7VDQ66LIFBFXTGM27NTY3OIGVQ"),
						"isOwnerCfgRef",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:isOwnerCfgRef:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:changeLog:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruOF2GYDTCGBGJ3EZV2QYRIC626I"),
						"changeLog",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
						null,
						0L,
						0L,false,false),

					/*Radix::UserFunc::UserFunc:isOwnerWasNotCreated:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdILZKB5MVKNAITADIRBN2C2BEZU"),
						"isOwnerWasNotCreated",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:isOwnerWasNotCreated:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:changeLogImportFromJmlEditor:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZHGKBYXPYBEN7LARKMVZVTJHVY"),
						"changeLogImportFromJmlEditor",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:inheritedDescription:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPFPZWKHWJNFB5H4GA2XM6MYFE4"),
						"inheritedDescription",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:inheritedDescription:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:lastChangelogRevisionDate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRLFSWEPMAZGSXHC4Q33JZ4V7VI"),
						"lastChangelogRevisionDate",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:lastChangelogRevisionDate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::UserFunc::UserFunc:descriptionForSelector:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP73O4GPGEBHW7O4YHOK5RDTGF4"),
						"descriptionForSelector",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF4NDFLISBZHRVC5RVNOYO4R4RQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::UserFunc::UserFunc:descriptionForSelector:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::UserFunc::UserFunc:remoteCall_listUserDefinitions-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdUAJCOSP4CRGSHHKG75O2TOIS34"),
						"remoteCall_listUserDefinitions",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.RPC,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::UserFunc::UserFunc:remoteCall_loadUserClassXml-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2UFOL65XPNAZFBUXA35TKOZYCE"),
						"remoteCall_loadUserClassXml",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.RPC,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::UserFunc::UserFunc:remoteCall_loadUserFuncXml-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAJHTMHT6FJEUVC7PUDONQ3RZXQ"),
						"remoteCall_loadUserFuncXml",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.RPC,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::UserFunc::UserFunc:MoveToLibrary-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJSJIK2BKOFAP5DOLQIICYERBAQ"),
						"MoveToLibrary",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWTEHTIAQBRHVZFDONBUORXOZS4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img5XIED6NB7RGSBEN6KRYB2PXTSQFQPJTS"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::UserFunc::UserFunc:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdWSK3HNPKXNCLFFMSYQ7XZCGOKI"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::UserFunc::UserFunc:CheckEntitiesExistance-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPQTVYTIC2RHXZGE7NPGUP3OMOE"),
						"CheckEntitiesExistance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						true,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::UserFunc::UserFunc:remoteCall_getCurrentVersionMethodId-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd77QKAJRRVFDGBNIIXSH4UDQ6QY"),
						"remoteCall_getCurrentVersionMethodId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.RPC,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::UserFunc::UserFunc:remoteCall_isOwnerContainsLookupAdvizor-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLTBI2UOZY5DPHIGICOHUX5II5M"),
						"remoteCall_isOwnerContainsLookupAdvizor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.RPC,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::UserFunc::UserFunc:Invalid-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltSSCZW2PBHBAIXEYY2GD3H5UVUA"),
						"Invalid",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRTIXYU5XQZBVJPCK4OZI6JBNKM"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colTELTD5L3ZHOBDCMTAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),
						null,

						/*Radix::UserFunc::UserFunc:Invalid:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::UserFunc::UserFunc:Undefined-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltR2YI43ZJIJESJNSIB2DBS646JE"),
						"Undefined",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXUGMTUUGNE6LLFEVUEY34AMAU"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colTALTD5L3ZHOBDCMTAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null \nOR \nextract(xmltype(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colTALTD5L3ZHOBDCMTAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>), \'//*[local-name()=\"Source\"]//*[local-name()=\"Java\"]/text()[normalize-space()]\') is null\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),
						null,

						/*Radix::UserFunc::UserFunc:Undefined:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::UserFunc::UserFunc:BySourceText-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("flt56W7TVKG35DX7C376TDH4KGQYY"),
						"BySourceText",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZNAHTKFXBRBAFFOLDXF2NJP4H4"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colPNLNJ2WNRJCBPBR6JPI3RZ4TVE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=0 or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"col26N4VA2NARCLDDJPST4UQNDHIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null) and  dbms_lob.instr(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"colTALTD5L3ZHOBDCMTAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, to_clob(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm26DWQSCW5VF37NOE7F7MHY7H7A\"/></xsc:Item><xsc:Item><xsc:Sql>)) > 0</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm26DWQSCW5VF37NOE7F7MHY7H7A"),
								"snippet",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7WRENQIAR5EDTPAQD626WDWRZU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
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

						/*Radix::UserFunc::UserFunc:BySourceText:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::UserFunc::UserFunc:ById-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltYAHTHJU32BB67JJVNKDRDKFIMI"),
						"ById",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCMXJLZWWJCLNF3YYF7BMLSSWU"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblJ6SOXKD3ZHOBDCMTAALOMT5GDM\" PropId=\"col6V3CVGAHT5GRXAQ6VRLN3YVQGQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmGREQZOIXRFGW7GHYAT65QIUQ6M\"/></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtKLUDTYAFTBEOFFEXKZBNDARCNA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmGREQZOIXRFGW7GHYAT65QIUQ6M"),
								"idParam",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLEIFVQYTCRCR7O5LQRVK5NPKFA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::UserFunc::UserFunc:ById:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::UserFunc::UserFunc:UpdateTime-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),
						"UpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJFYGCCXBZAI7OWHG63SKXU3OY"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYF2NT733ZHOBDCMTAALOMT5GDM"),
								true)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::UserFunc::UserFunc:Id-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtKLUDTYAFTBEOFFEXKZBNDARCNA"),
						"Id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ4EBVNECQJH77P5MHNLQYEQLOA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refAEMQJND3ZHOBDCMTAALOMT5GDM"),"UserFunc=>UpValRef (upDefId=>defId, upOwnerEntityId=>ownerEntityId, upOwnerPid=>ownerPid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZVJUSFASRDNBDCBEABIFNQAABA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col7QMAJND3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UMAJND3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col7YMAJND3ZHOBDCMTAALOMT5GDM")},new String[]{"upDefId","upOwnerEntityId","upOwnerPid"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BJUSFASRDNBDCBEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJUSFASRDNBDCBEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JJUSFASRDNBDCBEABIFNQAABA")},new String[]{"defId","ownerEntityId","ownerPid"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refPFPNOHD4ZHOBDCMTAALOMT5GDM"),"UserFunc=>User (lastUpdateUserName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCGFACL4ZHOBDCMTAALOMT5GDM")},new String[]{"lastUpdateUserName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGWQBF5FKIVGRFHCOVCDXBLS3TQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHMDW6GTMKJC47FL5GNCYGTMKQU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DRA4YXDZNALTA4KT73WNRMYSQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJXA4VMNLWBH6DPNJHIXMNHXBHA")},
			true,true,true);
}

/* Radix::UserFunc::UserFunc - Web Meta*/

/*Radix::UserFunc::UserFunc-Entity Class*/

package org.radixware.ads.UserFunc.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::UserFunc::UserFunc:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
			"Radix::UserFunc::UserFunc",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKNZKVTMXZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,true);
}

/* Radix::UserFunc::UserFunc:General - Desktop Meta*/

/*Radix::UserFunc::UserFunc:General-Editor Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWUMB3KKA6ZA63APKRKZLUGG7RI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWEQPJUYLXCAGMHF4KKUK6VWQPOORINWN"),

	/*Radix::UserFunc::UserFunc:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::UserFunc::UserFunc:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGWV57X7UTNDE3HWQIAKKSKDBLM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFEVTY3TYBVC43OGRLRICVHGUQQ"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepVWJEQ7X5CBBTPCIWEUPOYPJCM4"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGWV57X7UTNDE3HWQIAKKSKDBLM"))}
	,

	/*Radix::UserFunc::UserFunc:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	32,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2048,0,0,null);
}
/* Radix::UserFunc::UserFunc:General:Model - Desktop Executable*/

/*Radix::UserFunc::UserFunc:General:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.explorer;

import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
                                

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model")
public class General:Model  extends org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }

	private boolean compileOnUpdate = true;

	class CompleterGetter implements java.util.concurrent.Callable<Bin> {

	    final AdsUserFuncDef uf;
	    final java.lang.Long storedVersion;
	    final java.lang.Long currentVersion;
	    final org.radixware.kernel.common.check.IProblemHandler problemHandler;

	    CompleterGetter(org.radixware.kernel.common.check.IProblemHandler problemHandler, AdsUserFuncDef uf, java.lang.Long storedVersion, java.lang.Long currentVersion) {
	        this.uf = uf;
	        this.storedVersion = storedVersion;
	        this.currentVersion = currentVersion;
	        this.problemHandler = problemHandler;
	    }

	    public Bin call() {
	        UserFuncCompiler compiler = new UserFuncCompiler();
	        try {
	            byte[] jarBytes = compiler.compile(uf, problemHandler, false);
	            if (jarBytes != null)
	                return new Bin(jarBytes);
	            else
	                return null;
	        } finally {
	            compiler.close(uf.Branch);
	        }
	    }
	};
	private org.radixware.kernel.explorer.utils.QtJambiExecutor executor = new org.radixware.kernel.explorer.utils.QtJambiExecutor();

	private class ClientUserDefRequestor implements org.radixware.kernel.common.defs.ads.userfunc.UdsObserver.IUserDefRequestor {

	    private final java.util.Set<String> failOnLoadLibFunctions = new java.util.HashSet<String>();

	    public void readUserDefHeaders(final java.util.Set<Meta::DefType> defTypes, org.radixware.kernel.common.defs.ads.userfunc.UdsObserver.IUserDefReceiver recv) {
	        final Arr<Meta::DefType> arrEnum = new Arr<Meta::DefType>(defTypes);
	        try {
	            java.util.concurrent.Callable<CommandsXsd:ReadUserDefHeadersRsDocument> call = new java.util.concurrent.Callable<CommandsXsd:ReadUserDefHeadersRsDocument>() {
	                public CommandsXsd:ReadUserDefHeadersRsDocument call() {
	                    try {
	                        return remoteCall_listUserDefinitions(arrEnum);
	                    } catch (Exceptions::ServiceClientException e) {
	                        return null;
	                    } catch (InterruptedException e) {
	                        return null;
	                    }
	                }
	            };
	            CommandsXsd:ReadUserDefHeadersRsDocument xDoc;
	            if (com.trolltech.qt.gui.QApplication.instance().thread() == Thread.currentThread()) {
	                try {
	                    xDoc = call.call();
	                } catch (Exception e) {
	                    xDoc = null;
	                }
	            } else {
	                xDoc = executor.invoke(call);
	            }

	            if (xDoc != null && xDoc.ReadUserDefHeadersRs != null && xDoc.ReadUserDefHeadersRs.DefInfoList != null) {
	                for (CommandsXsd:ReadUserDefHeadersRsDocument.ReadUserDefHeadersRs.DefInfo xInfo : xDoc.ReadUserDefHeadersRs.DefInfoList) {
	                    recv.accept(xInfo.Id, xInfo.Name, xInfo.ModuleName, xInfo.ModuleId);
	                }
	            }
	        } catch (java.util.concurrent.ExecutionException e) {
	        } catch (InterruptedException e) {
	        }

	    }

	    public org.radixware.schemas.adsdef.ClassDefinition getClassDefXml(final Types::Id reportId) {
	        if (reportId == null) {
	            return null;
	        }

	        try {
	            java.util.concurrent.Callable<org.radixware.schemas.adsdef.ClassDefinition> call = new java.util.concurrent.Callable<org.radixware.schemas.adsdef.ClassDefinition>() {
	                public org.radixware.schemas.adsdef.ClassDefinition call() {
	                    try {
	                        org.radixware.schemas.adsdef.AdsDefinitionDocument xDoc = remoteCall_loadUserClassXml(reportId.toString());
	                        if (xDoc == null || xDoc.AdsDefinition == null) {
	                            return null;
	                        }
	                        return xDoc.AdsDefinition.AdsClassDefinition;
	                    } catch (Exceptions::ServiceClientException e) {
	                        return null;
	                    } catch (InterruptedException e) {
	                        return null;
	                    }
	                }
	            };
	            if (com.trolltech.qt.gui.QApplication.instance().thread() == Thread.currentThread()) {
	                try {
	                    return call.call();
	                } catch (Exception e) {
	                    return null;
	                }
	            } else {
	                return executor.invoke(call);
	            }
	        } catch (java.util.concurrent.ExecutionException e) {
	        } catch (InterruptedException e) {
	        }
	        return null;
	    }

	    public AdsUserFuncDefinitionDocument.AdsUserFuncDefinition getLibUserFuncXml(final Types::Id libUserFuncId) {
	        if (libUserFuncId == null || failOnLoadLibFunctions.contains(libUserFuncId.toString())) {
	            return null;
	        }

	        String cachedValue = getObjectFromOptimizerCache(libUserFuncId.toString());
	        if (cachedValue != null) {
	            try {
	                Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xLibFuncDoc = Meta::AdsDefXsd:AdsUserFuncDefinitionDocument.Factory.parse(cachedValue);
	                return xLibFuncDoc.AdsUserFuncDefinition;
	            } catch (Exceptions::XmlException ex) {
	                //Fail on load library func from cache.
	            }
	        }

	        java.util.concurrent.Callable<AdsUserFuncDefinitionDocument> call = new java.util.concurrent.Callable<AdsUserFuncDefinitionDocument>() {
	            public AdsUserFuncDefinitionDocument call() {
	                try {
	                    return remoteCall_loadUserFuncXml(libUserFuncId.toString());
	                } catch (Exceptions::ServiceClientException ex) {
	                    return null;
	                } catch (Exceptions::InterruptedException ex) {
	                    return null;
	                }
	            }
	        };

	        try {
	            AdsUserFuncDefinitionDocument libFuncXml;
	            if (com.trolltech.qt.gui.QApplication.instance().thread() == Thread.currentThread()) {
	                libFuncXml = call.call();
	            } else {
	                libFuncXml = executor.invoke(call);
	            }

	            if (libFuncXml != null && libFuncXml.AdsUserFuncDefinition != null) {
	                return libFuncXml.AdsUserFuncDefinition;
	            } else {
	                failOnLoadLibFunctions.add(libUserFuncId.toString());
	            }
	        } catch (java.util.concurrent.ExecutionException ex) {

	        } catch (InterruptedException ex) {

	        } catch (Exceptions::Exception ex) {
	            Environment.getTracer().error("Error on load library function.", ex);
	        }
	        return null;
	    }
	}

	private class ClientUdsObserver extends org.radixware.kernel.common.defs.ads.userfunc.UdsObserver {

	    public org.radixware.kernel.common.defs.ads.userfunc.UdsObserver.IUserDefRequestor getReportRequestor() {
	        return requestor != null ? requestor : (requestor = new ClientUserDefRequestor());
	    }

	    @Override
	    public java.util.Map<org.radixware.kernel.common.types.Pid, Bool> checkEntitiesExistance(java.util.Set<org.radixware.kernel.common.types.Pid> pidsToCheck) {
	        Arte::TypesXsd:MapStrStrDocument xRq = Arte::TypesXsd:MapStrStrDocument.Factory.newInstance();
	        xRq.addNewMapStrStr();
	        for (org.radixware.kernel.common.types.Pid pid : pidsToCheck) {
	            xRq.MapStrStr.addNewEntry().Key = pid.toStr();
	        }
	        try {
	            Arte::EasXsd:CommandRs xRsDoc = Environment.getEasSession().executeCommand(UserFunc:General:Model.this, null, idof[UserFunc:CheckEntitiesExistance], null, xRq);
	            Arte::TypesXsd:MapStrStrDocument xRs = Arte::TypesXsd:MapStrStrDocument.Factory.parse(xRsDoc.Output.newInputStream());
	            java.util.Map<org.radixware.kernel.common.types.Pid, Bool> result = new java.util.HashMap<>(xRs.MapStrStr.EntryList.size());
	            for (Arte::TypesXsd:MapStrStr.Entry e : xRs.MapStrStr.EntryList) {
	                result.put(org.radixware.kernel.common.types.Pid.fromStr(e.Key), Bool.valueOf(e.Value));
	            }
	            return result;
	        } catch (Exceptions::InterruptedException | Exceptions::ServiceClientException | Exceptions::IOException | Exceptions::XmlException ex) {
	            Environment.getTracer().error(ex);
	            return java.util.Collections.emptyMap();
	        }
	    }
	}


	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	@Override
	@SuppressWarnings("deprecation")
	public org.radixware.kernel.common.client.views.IView createView() { return new 
	org.radixware.ads.UserFunc.explorer.View(getEnvironment());}
	@Override
	public org.radixware.kernel.common.types.Id getCustomViewId() {return org.radixware.kernel.common.types.Id.Factory.loadFrom("cee7IKMB24XZHOBDCMTAALOMT5GDM");}


	/*Radix::UserFunc::UserFunc:General:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFunc:General:Model:Properties-Properties*/

	/*Radix::UserFunc::UserFunc:General:Model:userFunc-Dynamic Property*/



	protected org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef userFunc=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:userFunc")
	  org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef getUserFunc() {
		return userFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:userFunc")
	   void setUserFunc(org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef val) {
		userFunc = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:description-Presentation Property*/




	public class Description extends org.radixware.ads.UserFunc.explorer.UserFunc.colEY7LJLL77RDNLLC655BJGHGEL4{
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

		/*Radix::UserFunc::UserFunc:General:Model:description:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::UserFunc::UserFunc:General:Model:description:Nested classes-Nested Classes*/

		/*Radix::UserFunc::UserFunc:General:Model:description:Properties-Properties*/

		/*Radix::UserFunc::UserFunc:General:Model:description:Methods-Methods*/

		/*Radix::UserFunc::UserFunc:General:Model:description:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:description:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			if (this.Owner.getContext() instanceof Explorer.Context::SelectorRowContext) {
			    return super.createPropertyEditor();
			} else{
			    
			    Explorer.Widgets::PropTextEditor editor = new PropTextEditor(this);
			    editor.setMaximumHeight(50);
			    editor.setMinimumWidth(0);
			    return editor;
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:description")
		public   void setValue(Str val) {

			internal[description] = val;
			if(userFunc!=null && internal[description]!=null && !internal[description].isEmpty()){
			    userFunc.Name = internal[description];
			}
		}
	}
	public Description getDescription(){return (Description)getProperty(colEY7LJLL77RDNLLC655BJGHGEL4);}

	/*Radix::UserFunc::UserFunc:General:Model:upDefId-Presentation Property*/




	public class UpDefId extends org.radixware.ads.UserFunc.explorer.UserFunc.col7QMAJND3ZHOBDCMTAALOMT5GDM{
		public UpDefId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:upDefId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:upDefId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UpDefId getUpDefId(){return (UpDefId)getProperty(col7QMAJND3ZHOBDCMTAALOMT5GDM);}

	/*Radix::UserFunc::UserFunc:General:Model:upOwnerClassId-Presentation Property*/




	public class UpOwnerClassId extends org.radixware.ads.UserFunc.explorer.UserFunc.prdEDT3H6EV4JF65HJPCQOZLCP2QQ{
		public UpOwnerClassId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:upOwnerClassId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:upOwnerClassId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UpOwnerClassId getUpOwnerClassId(){return (UpOwnerClassId)getProperty(prdEDT3H6EV4JF65HJPCQOZLCP2QQ);}

	/*Radix::UserFunc::UserFunc:General:Model:upOwnerPropertyName-Presentation Property*/




	public class UpOwnerPropertyName extends org.radixware.ads.UserFunc.explorer.UserFunc.prd7MTIHMUG5ZHVNKMSTMAZAKOJOQ{
		public UpOwnerPropertyName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:upOwnerPropertyName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:upOwnerPropertyName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UpOwnerPropertyName getUpOwnerPropertyName(){return (UpOwnerPropertyName)getProperty(prd7MTIHMUG5ZHVNKMSTMAZAKOJOQ);}

	/*Radix::UserFunc::UserFunc:General:Model:userFuncVersions-Dynamic Property*/



	protected java.util.Map<java.lang.String,org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef> userFuncVersions=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:userFuncVersions")
	public  java.util.Map<java.lang.String,org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef> getUserFuncVersions() {
		return userFuncVersions;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:userFuncVersions")
	public   void setUserFuncVersions(java.util.Map<java.lang.String,org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef> val) {
		userFuncVersions = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:sourceVersionName-Dynamic Property*/



	protected Str sourceVersionName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:sourceVersionName")
	public  Str getSourceVersionName() {
		return sourceVersionName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:sourceVersionName")
	public   void setSourceVersionName(Str val) {
		sourceVersionName = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:isEditorReadOnlyMode-Dynamic Property*/



	protected boolean isEditorReadOnlyMode=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:isEditorReadOnlyMode")
	public  boolean getIsEditorReadOnlyMode() {
		return isEditorReadOnlyMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:isEditorReadOnlyMode")
	public   void setIsEditorReadOnlyMode(boolean val) {
		isEditorReadOnlyMode = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:srcHash-Dynamic Property*/



	protected byte[] srcHash=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:srcHash")
	private final  byte[] getSrcHash() {
		return srcHash;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:srcHash")
	private final   void setSrcHash(byte[] val) {
		srcHash = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:methodId-Presentation Property*/




	public class MethodId extends org.radixware.ads.UserFunc.explorer.UserFunc.prdKVOQE4YK7FBFTDFG42BCRRSZCI{
		public MethodId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:methodId")
		public  Str getValue() {

			if (temporaryMethodId != null) {
			    return temporaryMethodId;
			}
			return internal[methodId];
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:methodId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MethodId getMethodId(){return (MethodId)getProperty(prdKVOQE4YK7FBFTDFG42BCRRSZCI);}

	/*Radix::UserFunc::UserFunc:General:Model:temporaryMethodId-Dynamic Property*/



	protected Str temporaryMethodId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:temporaryMethodId")
	  Str getTemporaryMethodId() {
		return temporaryMethodId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:temporaryMethodId")
	   void setTemporaryMethodId(Str val) {
		temporaryMethodId = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:changeLog-Presentation Property*/




	public class ChangeLog extends org.radixware.ads.UserFunc.explorer.UserFunc.pruOF2GYDTCGBGJ3EZV2QYRIC626I{
		public ChangeLog(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}

		/*Radix::UserFunc::UserFunc:General:Model:changeLog:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::UserFunc::UserFunc:General:Model:changeLog:Nested classes-Nested Classes*/

		/*Radix::UserFunc::UserFunc:General:Model:changeLog:Properties-Properties*/

		/*Radix::UserFunc::UserFunc:General:Model:changeLog:Methods-Methods*/

		/*Radix::UserFunc::UserFunc:General:Model:changeLog:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:changeLog:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			return new ChangeLogPropEditor(changeLog, UserFunc:General:Model.this);
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:changeLog")
		public final  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:changeLog")
		public final   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLog getChangeLog(){return (ChangeLog)getProperty(pruOF2GYDTCGBGJ3EZV2QYRIC626I);}

	/*Radix::UserFunc::UserFunc:General:Model:editorWidget-Dynamic Property*/



	protected org.radixware.ads.UserFunc.explorer.UserFuncEditorWidget editorWidget=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:editorWidget")
	public  org.radixware.ads.UserFunc.explorer.UserFuncEditorWidget getEditorWidget() {
		return editorWidget;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:editorWidget")
	public   void setEditorWidget(org.radixware.ads.UserFunc.explorer.UserFuncEditorWidget val) {
		editorWidget = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:postOpenHook-Dynamic Property*/



	protected org.radixware.kernel.explorer.editors.jmleditor.JmlEditor.IPostOpenAction postOpenHook=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:postOpenHook")
	public  org.radixware.kernel.explorer.editors.jmleditor.JmlEditor.IPostOpenAction getPostOpenHook() {
		return postOpenHook;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:postOpenHook")
	public   void setPostOpenHook(org.radixware.kernel.explorer.editors.jmleditor.JmlEditor.IPostOpenAction val) {
		postOpenHook = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:problemList-Dynamic Property*/



	protected org.radixware.kernel.explorer.editors.jmleditor.JmlProblemList problemList=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:problemList")
	public  org.radixware.kernel.explorer.editors.jmleditor.JmlProblemList getProblemList() {
		return problemList;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:problemList")
	public   void setProblemList(org.radixware.kernel.explorer.editors.jmleditor.JmlProblemList val) {
		problemList = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:userFuncLocator-Dynamic Property*/



	protected org.radixware.ads.UserFunc.explorer.EditorUserFuncLocator userFuncLocator=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:userFuncLocator")
	public  org.radixware.ads.UserFunc.explorer.EditorUserFuncLocator getUserFuncLocator() {

		if(internal[userFuncLocator] == null){    
		    internal[userFuncLocator] = new EditorUserFuncLocator(this,editorWidget.Model);
		}
		return internal[userFuncLocator];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:userFuncLocator")
	public   void setUserFuncLocator(org.radixware.ads.UserFunc.explorer.EditorUserFuncLocator val) {
		userFuncLocator = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:versionAccessor-Presentation Property*/




	public class VersionAccessor extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public VersionAccessor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:versionAccessor")
		public  Int getValue() {

			return version.Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:versionAccessor")
		public   void setValue(Int val) {

			version.Value = val;
		}
	}
	public VersionAccessor getVersionAccessor(){return (VersionAccessor)getProperty(prd4VZCA4B24BHM5MSOPZMUDBZ6BE);}

	/*Radix::UserFunc::UserFunc:General:Model:isCodeEditAccessible-Dynamic Property*/



	protected boolean isCodeEditAccessible=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:isCodeEditAccessible")
	  boolean getIsCodeEditAccessible() {

		return getEnvironment().isUserFuncDevelopmentAccessible();
	}




















	/*Radix::UserFunc::UserFunc:General:Model:Methods-Methods*/

	/*Radix::UserFunc::UserFunc:General:Model:create-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:create")
	public published  org.radixware.kernel.common.client.enums.EEntityCreationResult create () throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceClientException,org.radixware.kernel.common.client.exceptions.ModelException {
		try {
		    return super.create();
		} catch (ServiceCallFault e) {
		    Explorer.Exceptions::ModelException modelException = Explorer.Exceptions::ModelException.create(this, e);
		    if (modelException instanceof Explorer.Exceptions::InvalidPropertyValueException) {
		        if (idof[UserFunc:javaSrc].equals(((Explorer.Exceptions::InvalidPropertyValueException) modelException).getPropertyId()))
		            return Explorer.Models::EntityCreationResult:CANCELED_BY_CLIENT;
		    }
		    throw e;
		}

	}

	/*Radix::UserFunc::UserFunc:General:Model:update-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:update")
	public published  boolean update () throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceClientException,org.radixware.kernel.common.client.exceptions.ModelException {
		boolean res = true;
		 
		try {
		    if (res && super.update()) {        
		        return true;
		    } else
		        return false;
		} catch (ServiceCallFault e) {
		    Explorer.Exceptions::ModelException modelException = Explorer.Exceptions::ModelException.create(this, e);
		    if (modelException instanceof Explorer.Exceptions::InvalidPropertyValueException) {
		        if (idof[UserFunc:javaSrc].equals(((Explorer.Exceptions::InvalidPropertyValueException) modelException).getPropertyId()))
		            return false;
		    }
		    throw e;
		}

	}

	/*Radix::UserFunc::UserFunc:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:afterRead")
	protected published  void afterRead () {
		boolean reloadEditor = true;
		if (srcHash != null) {
		    AdsUserFuncDef uf = userFuncLocator.findUserFunc(id.Value.longValue(), sourceVersionName);
		    if (uf != null) {
		        byte[] newSrcHash = calcDigest(uf.getSource().toString());
		        if (java.util.Arrays.equals(srcHash, newSrcHash)) {
		            reloadEditor = false;
		        }
		    }
		    srcHash = null;
		}

		if (reloadEditor) {
		    userFunc = null;
		}
		super.afterRead();

		if (reloadEditor && editorWidget != null && getView() != null) {
		    UserFuncEditorWidget:Model widgetModel = editorWidget.Model;
		    widgetModel.open(this, userFuncLocator, problemList);
		}

		if (!isNew()) {
		    description.getEditMask().setNoValueStr(inheritedDescription.Value);
		}

		if (!isInSelectorRowContext()) {
		    changeLog.setEnabled(changeLogImportFromJmlEditor.Value == null);
		    changeLog.getEditMask().setNoValueStr(null);
		} else {
		    descriptionForSelector.getEditMask().setNoValueStr(inheritedDescription.Value);
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:compile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:compile")
	public  void compile (org.radixware.kernel.common.check.IProblemHandler problemHandler, Str sourceVersionName) {
		final Explorer.Env::TaskWaiter waiter = new TaskWaiter(Environment);
		waiter.setMessage("Compiling...");

		final AdsUserFuncDef userFunc = findUserFunc(sourceVersionName);
		if (userFunc != null) {
		    final java.util.concurrent.Callable<Bin> task = new CompleterGetter(problemHandler, userFunc, version.Value, currentVersion.Value);

		    final org.radixware.kernel.common.check.IProblemHandler phParam = problemHandler;
		    java.util.concurrent.Callable<Bin> call = new java.util.concurrent.Callable<Bin>() {
		        public Bin call() {
		            try {
		                syncUdsBuildPath(getEnvironment());
		                return (Bin) waiter.runAndWait(task);
		            } catch (Exception e) {
		                phParam.accept(org.radixware.kernel.common.check.RadixProblem.Factory.newError(userFunc,
		                        "Unexpected error occurred on function compilation. See details in trace."));
		                getEnvironment().getTracer().error("Error on compile user function", e);
		                return null;
		            }
		        }
		    };

		    try {

		        Bin jarBytes;
		        if (com.trolltech.qt.gui.QApplication.instance().thread() == Thread.currentThread()) {
		            try {
		                jarBytes = call.call();
		            } catch (Exception e) {
		                jarBytes = null;
		            }
		        } else {
		            jarBytes = executor.invoke(call);
		        }

		        if (sourceVersionName == null) {
		            javaRuntime.Value = jarBytes;
		            userClassGuid.Value = userFunc.getId().toString();
		        }
		    } catch (InterruptedException ex) {
		        showException(ex);
		    } catch (java.util.concurrent.ExecutionException ex) {
		        showException(ex);
		    } finally {
		        userFunc.resetCompileTimeCaches();
		        waiter.close();
		    }
		}


	}

	/*Radix::UserFunc::UserFunc:General:Model:findUserFunc-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:findUserFunc")
	protected  org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef findUserFunc (Str sourceVersionName) {

		if (sourceVersionName != null && userFuncVersions != null) {
		    //if(==null || .Value ==null || !.Value.contains( sourceVersionName)){
		    if (userFuncVersions.keySet().contains(sourceVersionName)) {
		        return userFuncVersions.get(sourceVersionName);
		    }
		//}
		}
		if (userFunc != null && sourceVersionName == null) {
		    return userFunc;
		}

		org.radixware.kernel.common.repository.Branch branch = null;
		try {
		    branch = Explorer.Env::Application.getDefManager().Repository.getBranch();
		} catch (Exceptions::IOException e) {
		    Explorer.Env::Application.processException(e);
		    return null;
		}

		Str classIdAsStr = classGuid.Value;

		if (classIdAsStr == null)
		    return null;

		Str methodIdAsStr = methodId.Value;

		Types::Id methodId = null;
		if (methodIdAsStr != null) {
		    methodId = Types::Id.Factory.loadFrom(methodIdAsStr);
		}
		Types::Id classId = Types::Id.Factory.loadFrom(classIdAsStr);

		org.radixware.schemas.adsdef.UserFuncSourceVersions xSrcVersionsDoc = null;

		Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xDoc = javaSrc.Value;
		try {
		    if (lastUpdateTime.Value != null) {
		        java.util.Calendar lastUpdateTime = java.util.Calendar.getInstance();
		        lastUpdateTime.setTime(lastUpdateTime.Value);
		        xDoc.AdsUserFuncDefinition.LastUpdateTime = lastUpdateTime;
		    }
		    xDoc.AdsUserFuncDefinition.LastUpdateUserName = lastUpdateUserName.Value;

		    if (javaSrcVers.Value != null) {
		        xSrcVersionsDoc = org.radixware.schemas.adsdef.UserFuncSourceVersions.Factory.parse(javaSrcVers.Value);
		        if (sourceVersionName != null && xSrcVersionsDoc.SourceVersionList != null) {
		            for (org.radixware.schemas.adsdef.UserFuncSourceVersions.SourceVersion sourceVersion : xSrcVersionsDoc.SourceVersionList) {
		                if (sourceVersion.Name.equals(sourceVersionName)) {
		                    xDoc.AdsUserFuncDefinition.Source.ItemList.clear();
		                    xDoc.AdsUserFuncDefinition.Source.ItemList.addAll(sourceVersion.ItemList);
		                    break;
		                }
		            }
		        }
		    }
		} catch (Exceptions::XmlException e) {
		    Explorer.Env::Application.processException(e);
		}
		Types::Id userClassGUIDId = Types::Id.Factory.loadFrom(userClassGuid.Value);

		Types::Id ownerEntityId;
		String ownerPid;
		if (isNew() && getContext() instanceof Explorer.Context::ObjectPropCreatingContext) {
		    Explorer.Context::ObjectPropCreatingContext ctx = (Explorer.Context::ObjectPropCreatingContext) getContext();
		    ownerEntityId = ctx.propOwner.getClassId();
		    ownerPid = ctx.propOwner.isNew() ? null : ctx.propOwner.getPid().toString();
		} else {
		    ownerEntityId = Types::Id.Factory.loadFrom(upOwnerClassId.Value);
		    ownerPid = upOwnerPid.Value;
		}

		AdsUserFuncDef newUserFunc = AdsUserFuncDef.Lookup.lookup(branch, classId, methodId, userClassGUIDId, ownerEntityId, ownerPid, new AdsUserFuncDef.LibFuncNameGetter(){
		    public String getLibFuncName(){
		        return ownerLibFuncName.Value;
		    }
		}, xDoc == null ? null : xDoc.AdsUserFuncDefinition,
		        sourceVersionName == null ? xSrcVersionsDoc : null, new ClientUdsObserver());
		if (newUserFunc != null && description.Value != null) {
		    newUserFunc.setName(description.Value);
		}
		if (sourceVersionName == null) {
		    userFunc = newUserFunc;
		} else {
		    if (userFuncVersions == null) {
		        userFuncVersions = new java.util.HashMap<java.lang.String,AdsUserFuncDef>();
		    }
		    userFuncVersions.put(sourceVersionName, newUserFunc);
		}

		return newUserFunc;
	}

	/*Radix::UserFunc::UserFunc:General:Model:exportToUds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:exportToUds")
	public  void exportToUds () {
		UdsExporter exporter = new UdsExporter(this);
		exporter.exportToUds(java.util.Collections.singletonList(this).iterator());
	}

	/*Radix::UserFunc::UserFunc:General:Model:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:beforeDelete")
	protected published  boolean beforeDelete () {
		//if(!Environment.( , 
		//    + " \'" + .ValueAsString + "\'"))
		//    return false;
		return super.beforeDelete();
	}

	/*Radix::UserFunc::UserFunc:General:Model:remoteCall_listUserDefinitions-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:remoteCall_listUserDefinitions")
	protected  org.radixware.schemas.userfunccommands.ReadUserDefHeadersRsDocument remoteCall_listUserDefinitions (org.radixware.ads.Meta.common.DefType.Arr request) throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_listUserDefinitions _cmd_cmdUAJCOSP4CRGSHHKG75O2TOIS34_instance_ = (org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_listUserDefinitions)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdUAJCOSP4CRGSHHKG75O2TOIS34"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		$remote$call$arg$list$store$.add(new org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo(org.radixware.kernel.common.enums.EValType.ARR_INT,request));
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmdUAJCOSP4CRGSHHKG75O2TOIS34_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : $rpc$call$result$ instanceof org.radixware.schemas.userfunccommands.ReadUserDefHeadersRsDocument ? (org.radixware.schemas.userfunccommands.ReadUserDefHeadersRsDocument) $rpc$call$result$ : (org.radixware.schemas.userfunccommands.ReadUserDefHeadersRsDocument) org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)$rpc$call$result$,org.radixware.schemas.userfunccommands.ReadUserDefHeadersRsDocument.class);

	}

	/*Radix::UserFunc::UserFunc:General:Model:remoteCall_loadUserClassXml-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:remoteCall_loadUserClassXml")
	public  org.radixware.schemas.adsdef.AdsDefinitionDocument remoteCall_loadUserClassXml (Str classId) throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_loadUserClassXml _cmd_cmd2UFOL65XPNAZFBUXA35TKOZYCE_instance_ = (org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_loadUserClassXml)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2UFOL65XPNAZFBUXA35TKOZYCE"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		$remote$call$arg$list$store$.add(new org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo(org.radixware.kernel.common.enums.EValType.STR,classId));
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmd2UFOL65XPNAZFBUXA35TKOZYCE_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : $rpc$call$result$ instanceof org.radixware.schemas.adsdef.AdsDefinitionDocument ? (org.radixware.schemas.adsdef.AdsDefinitionDocument) $rpc$call$result$ : (org.radixware.schemas.adsdef.AdsDefinitionDocument) org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)$rpc$call$result$,org.radixware.schemas.adsdef.AdsDefinitionDocument.class);

	}

	/*Radix::UserFunc::UserFunc:General:Model:syncUdsBuildPath-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:syncUdsBuildPath")
	public static  void syncUdsBuildPath (org.radixware.kernel.common.client.IClientEnvironment env) {
		String bp = System.getProperty("org.radixware.kernel.uds.buildPath");
		if (bp == null || bp.isEmpty()) {
		    final Client.Env::ClientEnvironment environment = env;
		    Runnable call = new Runnable() {
		        public void run() {
		            try {
		                try {
		                    CommandsXsd:SyncBuildPathRqDocument xIn = CommandsXsd:SyncBuildPathRqDocument.Factory.newInstance();
		                    java.io.File cacheDir = org.radixware.kernel.release.fs.RfsUtils.createTmpDir();
		                    cacheDir.delete();
		                    cacheDir.mkdirs();
		                    xIn.addNewSyncBuildPathRq().CacheDir = cacheDir.AbsolutePath;
		                    environment.getEasSession().executeContextlessCommand(idof[SyncUdsBuildPath], xIn, null);
		                    java.io.File[] files = cacheDir.listFiles();
		                    StringBuilder sb = new StringBuilder();
		                    for (java.io.File f : files) {
		                        sb.append(f.AbsolutePath).append(java.io.File.pathSeparator);
		                    }
		                    System.setProperty("org.radixware.kernel.uds.buildPath", sb.toString());
		                } catch (Exception e) {
		                    environment.processException(e);
		                }
		            } catch (Exception e) {
		            }
		        }
		    };

		    if (com.trolltech.qt.gui.QApplication.instance().thread() == Thread.currentThread()) {
		        try {
		            call.run();
		        } catch (Exception e) {
		        }
		    } else {
		        org.radixware.kernel.explorer.utils.QtJambiExecutor executor = new org.radixware.kernel.explorer.utils.QtJambiExecutor();
		        try {
		            executor.invoke(call);
		        } catch (InterruptedException e) {
		        }
		    }
		}


	}

	/*Radix::UserFunc::UserFunc:General:Model:remoteCall_loadUserFuncXml-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:remoteCall_loadUserFuncXml")
	public  org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument remoteCall_loadUserFuncXml (Str libUfId) throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_loadUserFuncXml _cmd_cmdAJHTMHT6FJEUVC7PUDONQ3RZXQ_instance_ = (org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_loadUserFuncXml)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAJHTMHT6FJEUVC7PUDONQ3RZXQ"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		$remote$call$arg$list$store$.add(new org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo(org.radixware.kernel.common.enums.EValType.STR,libUfId));
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmdAJHTMHT6FJEUVC7PUDONQ3RZXQ_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : $rpc$call$result$ instanceof org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument ? (org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument) $rpc$call$result$ : (org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument) org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)$rpc$call$result$,org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.class);

	}

	/*Radix::UserFunc::UserFunc:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();

		((Explorer.Views::Editor) getView()).setCommandBarHidden(true);
		((Explorer.Views::Editor) getView()).setMenuHidden(true);

	}

	/*Radix::UserFunc::UserFunc:General:Model:beforeSelectParent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:beforeSelectParent")
	public published  boolean beforeSelectParent (org.radixware.kernel.common.client.models.items.properties.PropertyRef propertyRef, org.radixware.kernel.common.client.models.GroupModel parentGroup) {

		if (propertyRef==linkedLibFunc && parentGroup instanceof LibUserFunc:Filtered:Model){
		     AdsUserFuncDef userFunc = findUserFunc(null);

		    if (userFunc != null) {
		        org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.Profile profile = userFunc.findProfile();
		        if (profile != null && profile.ReturnValue != null && profile.ReturnValue.Type != null) {
		            ((LibUserFunc:Filtered:Model) parentGroup).returnType = profile.ReturnValue.Type;
		        }
		    }
		}

		return super.beforeSelectParent(propertyRef, parentGroup);

	}

	/*Radix::UserFunc::UserFunc:General:Model:generalPageView_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:generalPageView_opened")
	public  void generalPageView_opened (com.trolltech.qt.gui.QWidget widget) {


	}

	/*Radix::UserFunc::UserFunc:General:Model:finishEdit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:finishEdit")
	public published  void finishEdit () {
		super.finishEdit();
		if (getView() != null) {
		    editorWidget.Model.finishEdit();
		}

	}

	/*Radix::UserFunc::UserFunc:General:Model:generalPageView_closed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:generalPageView_closed")
	public  void generalPageView_closed () {

	}

	/*Radix::UserFunc::UserFunc:General:Model:getEditorWidgetModel-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:getEditorWidgetModel")
	public  org.radixware.ads.UserFunc.explorer.UserFuncEditorWidget:Model getEditorWidgetModel () {
		return editorWidget.Model;
	}

	/*Radix::UserFunc::UserFunc:General:Model:updateWithCompile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:updateWithCompile")
	public  void updateWithCompile (boolean needCompile) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceClientException,org.radixware.kernel.common.client.exceptions.ModelException {
		try {
		    this.compileOnUpdate = needCompile;
		    this.update();
		} finally {
		    this.compileOnUpdate = true;
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:validateParametersBinding-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:validateParametersBinding")
	public  boolean validateParametersBinding () {
		if (libFuncGuid != null) {

		    final boolean wasErrors[] = new boolean[]{false};
		    IProblemHandler handler = new IProblemHandler() {
		        public void accept(RadixProblem problem) {
		            if (problem.Severity == RadixProblem.ESeverity.ERROR) {
		                wasErrors[0] = true;
		            }
		            if (problemList != null) {
		                problemList.setVisible(true);
		                problemList.acceptProblem(id.Value.longValue(), problem);
		            }
		        }
		    };
		    if (problemList != null) {
		        problemList.clear(id.Value.longValue());
		    }

		    AdsUserFuncDef uf = findUserFunc(null);
		    Explorer.Types::Pid targetPid = null;
		    AdsUserFuncDef targetUf = null;

		    if (this.linkedLibFunc.Value != null) {
		        try {
		            LibUserFunc luf = (LibUserFunc) Explorer.Models::EntityModel.openContextlessModel(getEnvironment(), this.linkedLibFunc.Value.getPid(), idof[LibUserFunc], idof[LibUserFunc:General]);
		            if (luf.upUserFunc.Value != null) {
		                if (luf.upUserFunc.Value != null) {
		                    targetPid = luf.upUserFunc.Value.getPid();
		                    UserFunc ufModel = luf.upUserFunc.openEntityModel();
		                    if (ufModel instanceof UserFunc:General:Model) {
		                        targetUf = ((UserFunc:General:Model) ufModel).findUserFunc(null);

		                        //so we have a target, now check for loops in bindings
		                        if (targetUf != null && !this.isNew() && this.getPid() != null) {


		                            UserFunc current = ufModel;
		                            java.util.Set<String> pidsPath = new java.util.HashSet<String>();
		                            pidsPath.add(this.getPid().toStr());
		                            pidsPath.add(((UserFunc:General:Model) ufModel).getPid().toStr());

		                            while (current.libFuncGuid.Value != null) {
		                                if (current.linkedLibFunc.Value == null) {
		                                    break;
		                                }
		                                LibUserFunc funcObject = current.linkedLibFunc.openEntityModel();
		                                Explorer.Types::Pid ufPid = funcObject.upUserFunc.Value.getPid();
		                                if (pidsPath.contains(ufPid.toStr())) {
		                                    handler.accept(RadixProblem.Factory.newError(targetUf, "Сyclic binding. Infinite recursion is possible"));
		                                    break;
		                                }
		                                current = funcObject.upUserFunc.openEntityModel();
		                                if (current != null) {
		                                    pidsPath.add(((Explorer.Models::EntityModel) current).getPid().toStr());
		                                } else
		                                    break;
		                            }

		                        }
		                    }
		                }
		            }
		        } catch (Exceptions::ServiceClientException e) {

		            getEnvironment().processException(e);
		            return false;
		        } catch (Throwable e) {
		            return false;
		        }
		    }

		    new ClientUserFuncBindingValidator(getEnvironment()).validateLinkedFuncParams(uf, targetUf, getPid() == null ? null : getPid().toString(), targetPid == null ? null : targetPid.toString(), handler, paramBinding.Value == null ? null : paramBinding.Value);
		    problemList.open(id.Value.longValue());
		    return !wasErrors[0];

		}
		return true;
	}

	/*Radix::UserFunc::UserFunc:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (!checkUserRights()) {
		    return false;
		}

		boolean result = true;

		if (isLinkUsed.Value != null && isLinkUsed.Value.booleanValue()) {
		    result = validateOnClose();
		    if (result) {
		        //Changes in uf code will be discarded after save, 
		        //so to calc used definitions we must use source on moment of editor opening
		        final Meta::XscmlXsd:JmlType javaSrcOnOpenEditor = javaSrc.Value.AdsUserFuncDefinition.Source;
		        final java.util.List<org.radixware.kernel.common.defs.ads.AdsDefinition> usedWrappers
		                = editorWidget.Model.getCodeEditor().getUserFunc().getUsedWrappers(javaSrcOnOpenEditor);
		        updateUsedDefinitions(usedWrappers);
		    }
		} else {
		    if (!linkedLibFuncNotUsedConfirmation()) {
		        return false;
		    }
		    
		    if (!editorWidget.Model.saveUserFunc(compileOnUpdate, false)) {
		        result = false;
		    } else {
		        srcHash = calcDigest(editorWidget.Model.getCodeEditor().getUserFunc().getSource().toString());
		    }
		}

		if (!result) {
		    return false;
		}
		return super.beforeUpdate();
	}

	/*Radix::UserFunc::UserFunc:General:Model:onCommand_MoveToLibrary-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:onCommand_MoveToLibrary")
	public  void onCommand_MoveToLibrary (org.radixware.ads.UserFunc.explorer.UserFunc.MoveToLibrary command) {
		try {
		    if (this.isNew()) {
		        Explorer.Models::EntityCreationResult res = create();
		        if (Explorer.Models::EntityCreationResult:SUCCESS.equals(res)) {
		            command.send();
		        }
		    } else {
		        if (update()) {
		            command.send();
		        }
		    }
		    this.read();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		} catch (Explorer.Exceptions::ModelException e) {
		    showException(e);
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:isCommandEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:isCommandEnabled")
	public published  boolean isCommandEnabled (org.radixware.kernel.common.client.meta.RadCommandDef command) {
		if (command.Id == idof[UserFunc:MoveToLibrary]) {
		    return isLinkUsed.Value == null || !isLinkUsed.Value.booleanValue();
		}

		return super.isCommandEnabled(command);
	}

	/*Radix::UserFunc::UserFunc:General:Model:canSafelyClean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:canSafelyClean")
	public published  boolean canSafelyClean (org.radixware.kernel.common.client.models.CleanModelController controller) {
		if (!checkUserRights()) {
		    return false;
		}
		if (editorWidget!=null){
		    return editorWidget.Model.canSafelyClean(controller) && getView().canSafelyClose(controller);
		} else {
		    return super.canSafelyClean(controller);
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:validateOnClose-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:validateOnClose")
	public  boolean validateOnClose () {

		if (!validateParametersBinding()) {
		    if (getEnvironment().messageConfirmation("Invalid Function State", "Some problems found via function validation. Do you really want to continue?")) {
		        return true;
		    } else {
		        return false;
		    }
		}
		return true;

	}

	/*Radix::UserFunc::UserFunc:General:Model:Editor_closed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:Editor_closed")
	public  void Editor_closed () {
		this.problemList = null;
		this.userFuncLocator = null;
		if(this.editorWidget !=null && this.getEditorWidgetModel() != null){
		    this.getEditorWidgetModel().close();
		}
		this.editorWidget = null;
		this.userFunc = null;
	}

	/*Radix::UserFunc::UserFunc:General:Model:Editor_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:Editor_opened")
	public  void Editor_opened (com.trolltech.qt.gui.QWidget widget) {
		isEditorReadOnlyMode = this.getRestrictions().getIsUpdateRestricted() || !isCodeEditAccessible;
		if (isEditorReadOnlyMode) {
		    getCommand(idof[UserFunc:MoveToLibrary]).setEnabled(false);
		}

		((Explorer.Views::Editor) getView()).setToolBarHidden(true);
		((Explorer.Views::Editor) getView()).setMenuHidden(true);

		editorWidget = new UserFuncEditorWidget(getEnvironment(), UserFunc:General:General:View:generalPageView);
		editorWidget.open();

		if (this.getContext() instanceof Explorer.Context::InSelectorEditingContext) {
		    Explorer.Context::InSelectorEditingContext ctx = (Explorer.Context::InSelectorEditingContext) this.getContext();
		    Explorer.Models::GroupModel group = ctx.getGroupModel();
		    if (group instanceof UserFunc:General:Model) {
		        this.problemList = ((UserFunc:General:Model) group).problemList;
		        if (((UserFunc:General:Model) group).userFuncLocator != null) {
		            ((UserFunc:General:Model) group).userFuncLocator.editorUfLocator = this.userFuncLocator;
		        }
		    }
		}

		if (problemList == null) {
		    Explorer.Views::Splitter splitter = new Splitter(UserFunc:General:General:View:generalPageView, getEnvironment().getConfigStore());
		    UserFunc:General:General:View:generalPageView.layout().addWidget(splitter);
		    splitter.setOrientation(com.trolltech.qt.core.Qt.Orientation.Vertical);
		    splitter.addWidget(editorWidget);
		    problemList = new JmlProblemList(userFuncLocator, (Explorer.Views::Editor) getView());
		    splitter.addWidget(problemList);
		} else {
		    UserFunc:General:General:View:generalPageView.layout().addWidget(editorWidget);
		}

		UserFuncEditorWidget:Model widgetModel = editorWidget.Model;
		problemList.open(id.Value.longValue());
		widgetModel.open(this, userFuncLocator, problemList);
		if (postOpenHook != null) {
		    postOpenHook.perform(userFuncLocator);
		    postOpenHook = null;
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:getObjectFromOptimizerCache-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:getObjectFromOptimizerCache")
	protected  Str getObjectFromOptimizerCache (Str key) {
		for (String entry : optimizerCache.Value) {
		    int keySeparatorPos = entry.indexOf("=");
		    if (keySeparatorPos != -1 && entry.substring(0, keySeparatorPos).equals(key)) {
		        return entry.substring(keySeparatorPos + 1);
		    }
		}
		return null;
	}

	/*Radix::UserFunc::UserFunc:General:Model:cancelChanges-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:cancelChanges")
	public published  void cancelChanges () {
		if(problemList != null) {
		    problemList.onCancelChanges(id.Value.longValue());
		}
		super.cancelChanges();
	}

	/*Radix::UserFunc::UserFunc:General:Model:calcDigest-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:calcDigest")
	private final  byte[] calcDigest (Str str) {
		if (str == null) {
		    return null;
		}
		try {
		    java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		    digest.update(str.getBytes("UTF-8"));
		    return digest.digest();
		} catch (java.security.NoSuchAlgorithmException ex) {
		    Environment.getTracer().error("Error on calculation user-function hash", ex);
		} catch (Exceptions::UnsupportedEncodingException ex) {
		    Environment.getTracer().error("Error on calculation user-function hash", ex);
		}
		return null;
	}

	/*Radix::UserFunc::UserFunc:General:Model:onCommand_Export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:onCommand_Export")
	public  void onCommand_Export (org.radixware.ads.UserFunc.explorer.UserFunc.Export command) {
		try {
		    Str filepath = null;
		    if ((isOwnerCfgRef.Value == null || !isOwnerCfgRef.Value.booleanValue()) || !checkOwnerContainsCfgAdvizor()) {
		        java.io.File f = CfgManagement::DesktopUtils.openFileForExport(findNearestView(), "Save File");
		        if (f != null) {
		            filepath = f.getAbsolutePath();
		        } else {
		            return;
		        }
		    }
		    
		    Arte::TypesXsd:MapStrStrDocument xRq = Arte::TypesXsd:MapStrStrDocument.Factory.newInstance();
		    org.radixware.schemas.types.MapStrStr.Entry xmlEntry = xRq.addNewMapStrStr().addNewEntry();
		    Meta::AdsDefXsd:AdsUserFuncDefinitionDocument xUfDocFromEditor = 
		            editorWidget.Model.getCodeEditor().getUserFuncXml();
		    xmlEntry.Key = "UserFuncXmlFromEditorBase64";
		    xmlEntry.Value = Utils::Base64.encode(xUfDocFromEditor.xmlText().getBytes(java.nio.charset.StandardCharsets.UTF_8));
		    if (filepath != null) {
		        org.radixware.schemas.types.MapStrStr.Entry filepathEntry = xRq.MapStrStr.addNewEntry();
		        filepathEntry.Key = "Filepath";
		        filepathEntry.Value = filepath;
		    }
		    command.send(xRq);
		    
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:getOwnerTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:getOwnerTitle")
	public final published  Str getOwnerTitle () {
		if (isNew() && getContext() instanceof Explorer.Context::ObjectPropCreatingContext) {
		    final Explorer.Context::ObjectPropCreatingContext ctx = (Explorer.Context::ObjectPropCreatingContext) getContext();
		    if (ctx.propOwner.isNew()){
		        return ctx.propOwner.getTitle();
		    }
		}
		return ownerTitle.Value;
	}

	/*Radix::UserFunc::UserFunc:General:Model:afterPrepareCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:afterPrepareCreate")
	protected published  boolean afterPrepareCreate () {
		if (isInOwnerCreationContext()){
		    isOwnerWasNotCreated.Value = true;
		}
		return super.afterPrepareCreate();
	}

	/*Radix::UserFunc::UserFunc:General:Model:isInOwnerCreationContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:isInOwnerCreationContext")
	public final published  boolean isInOwnerCreationContext () {
		if (isNew() && getContext() instanceof Explorer.Context::ObjectPropCreatingContext) {
		    return ((Explorer.Context::ObjectPropCreatingContext) getContext()).propOwner.isNew();
		}
		return false;
	}

	/*Radix::UserFunc::UserFunc:General:Model:updateUsedDefinitions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:updateUsedDefinitions")
	protected  void updateUsedDefinitions (org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef userFunc) {
		if (userFunc == null) {
		    return;
		}
		java.util.List<org.radixware.kernel.common.defs.ads.AdsDefinition> usedWrappers = userFunc.UsedWrappers;
		updateUsedDefinitions(usedWrappers);
	}

	/*Radix::UserFunc::UserFunc:General:Model:updateUsedDefinitions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:updateUsedDefinitions")
	private final  void updateUsedDefinitions (java.util.List<org.radixware.kernel.common.defs.ads.AdsDefinition> usedWrappers) {
		usedDefinitions.Value = UserFuncUtils.usedDefinitionsListToStr(usedWrappers);
	}

	/*Radix::UserFunc::UserFunc:General:Model:remoteCall_getCurrentVersionMethodId-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:remoteCall_getCurrentVersionMethodId")
	protected  Str remoteCall_getCurrentVersionMethodId () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_getCurrentVersionMethodId _cmd_cmd77QKAJRRVFDGBNIIXSH4UDQ6QY_instance_ = (org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_getCurrentVersionMethodId)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd77QKAJRRVFDGBNIIXSH4UDQ6QY"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmd77QKAJRRVFDGBNIIXSH4UDQ6QY_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : (Str)$rpc$call$result$;

	}

	/*Radix::UserFunc::UserFunc:General:Model:linkedLibFuncNotUsedConfirmation-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:linkedLibFuncNotUsedConfirmation")
	private final  boolean linkedLibFuncNotUsedConfirmation () {
		final boolean oneOfLinkedLibFuncPropsModified = isLinkUsed.isValEdited()
		        || libFuncGuid.isValEdited()
		        || paramBinding.isValEdited();
		final boolean linkedLibFuncDefinedButNotUsed = isLinkUsed.Value != null
		        && !isLinkUsed.Value.booleanValue()
		        && libFuncGuid.Value != null;

		if (oneOfLinkedLibFuncPropsModified && linkedLibFuncDefinedButNotUsed) {
		    if (!getEnvironment().messageConfirmation("Linked Library User Function",
		            "Linked library user function is defined, but not used. Do you want to save changes?")) {
		        return false;
		    }
		}
		return true;
	}

	/*Radix::UserFunc::UserFunc:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (!checkUserRights()) {
		    return false;
		}
		if (!linkedLibFuncNotUsedConfirmation()) {
		    return false;
		}
		if (super.beforeCreate() && getView() != null && editorWidget != null && editorWidget.Model != null) {
		    return editorWidget.Model.saveUserFunc(true, false);
		}
		return false;
	}

	/*Radix::UserFunc::UserFunc:General:Model:checkOwnerContainsCfgAdvizor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:checkOwnerContainsCfgAdvizor")
	private final  boolean checkOwnerContainsCfgAdvizor () {
		try {
		    final Bool ownerContainsAdvizor = remoteCall_isOwnerContainsLookupAdvizor();
		    if (ownerContainsAdvizor == null || !ownerContainsAdvizor.booleanValue()) {
		        getEnvironment().messageError("User defined function owner does not support export function to package.");
		        return false;
		    }
		    return true;
		} catch (Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		    getEnvironment().getTracer().error(ex);
		    return false;
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:remoteCall_isOwnerContainsLookupAdvizor-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:remoteCall_isOwnerContainsLookupAdvizor")
	private final  Bool remoteCall_isOwnerContainsLookupAdvizor () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_isOwnerContainsLookupAdvizor _cmd_cmdLTBI2UOZY5DPHIGICOHUX5II5M_instance_ = (org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_isOwnerContainsLookupAdvizor)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLTBI2UOZY5DPHIGICOHUX5II5M"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmdLTBI2UOZY5DPHIGICOHUX5II5M_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : (Bool)$rpc$call$result$;

	}

	/*Radix::UserFunc::UserFunc:General:Model:getDescriptionWithChangelogInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:getDescriptionWithChangelogInfo")
	protected  Str getDescriptionWithChangelogInfo (Str descr) {
		return UserFuncUtils.getDescriptionWithLastRevisionDate(descr, lastChangelogRevisionDate.Value);
	}

	/*Radix::UserFunc::UserFunc:General:Model:checkUserRights-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:checkUserRights")
	private final  boolean checkUserRights () {
		if (getEnvironment().isUserFuncDevelopmentAccessible()) {
		    return true;
		}

		final boolean thereIsRuntimeOrLinkedFunc = javaRuntime.Value != null
		        || (linkedLibFunc.Value != null && isLinkUsed.Value != null && isLinkUsed.Value.booleanValue());
		if (!thereIsRuntimeOrLinkedFunc) {
		    
		    getEnvironment().messageError("Current user can only create functions that use binding to library functions");
		    return false;
		}
		return true;
	}
	public static class RemoteCall_loadUserClassXml extends org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_loadUserClassXml{
		protected RemoteCall_loadUserClassXml(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class RemoteCall_getCurrentVersionMethodId extends org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_getCurrentVersionMethodId{
		protected RemoteCall_getCurrentVersionMethodId(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class RemoteCall_loadUserFuncXml extends org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_loadUserFuncXml{
		protected RemoteCall_loadUserFuncXml(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public final class MoveToLibrary extends org.radixware.ads.UserFunc.explorer.UserFunc.MoveToLibrary{
		protected MoveToLibrary(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveToLibrary( this );
		}

	}

	public static class RemoteCall_isOwnerContainsLookupAdvizor extends org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_isOwnerContainsLookupAdvizor{
		protected RemoteCall_isOwnerContainsLookupAdvizor(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class RemoteCall_listUserDefinitions extends org.radixware.ads.UserFunc.explorer.UserFunc.RemoteCall_listUserDefinitions{
		protected RemoteCall_listUserDefinitions(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public final class Export extends org.radixware.ads.UserFunc.explorer.UserFunc.Export{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Export( this );
		}

	}






























}

/* Radix::UserFunc::UserFunc:General:Model - Desktop Meta*/

/*Radix::UserFunc::UserFunc:General:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem7IKMB24XZHOBDCMTAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::UserFunc:General:Model:Properties-Properties*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

								/*Radix::UserFunc::UserFunc:General:Model:versionAccessor:versionAccessor:PropertyPresentation-Property Presentation*/
								new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4VZCA4B24BHM5MSOPZMUDBZ6BE"),
									"versionAccessor",
									null,
									null,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
									org.radixware.kernel.common.enums.EValType.INT,
									org.radixware.kernel.common.enums.EPropNature.VIRTUAL,
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
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									false,false,
									false,
									false,
									null,
									false,

									/*Radix::UserFunc::UserFunc:General:Model:versionAccessor:versionAccessor:PropertyPresentation:Edit Options:-Edit Mask Int*/
									new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
									null,
									null,
									null,
									true,-1,-1,1,
									false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
						},
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

/* Radix::UserFunc::UserFunc:General:General:View - Desktop Executable*/

/*Radix::UserFunc::UserFunc:General:General:View-Custom Page Editor for Desktop*/

package org.radixware.ads.UserFunc.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:General:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View generalPageView;
	public View getGeneralPageView(){ return generalPageView;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		generalPageView = this;
		generalPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 674, 448));
		generalPageView.setObjectName("generalPageView");
		generalPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QVBoxLayout $layout1 = new com.trolltech.qt.gui.QVBoxLayout(generalPageView);
		$layout1.setObjectName("verticalLayout");
		$layout1.setContentsMargins(0, 0, 0, 0);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setSpacing(0);
		generalPageView.opened.connect(model, "mthAGYRTNP5MBCUTCSN4V5APU5CHQ(com.trolltech.qt.gui.QWidget)");
		generalPageView.closed.connect(model, "mthNRSHT2SWFVAKLFYINCYDJPKHSM()");
		opened.emit(this);
	}
	public final org.radixware.ads.UserFunc.explorer.General:Model getModel() {
		return (org.radixware.ads.UserFunc.explorer.General:Model) super.getModel();
	}

}

/* Radix::UserFunc::UserFunc:General:General:WebView - Web Executable*/

/*Radix::UserFunc::UserFunc:General:General:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.UserFunc.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:General:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public org.radixware.wps.views.editor.PropertiesGrid widget;
	public org.radixware.wps.views.editor.PropertiesGrid getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::UserFunc::UserFunc:General:General:WebView:widget ==============
		this.widget = this;
		widget.setWidth(600);
		widget.setHeight(400);
		//============ Radix::UserFunc::UserFunc:General:General:WebView:widget:widget ==============
		this.widget = new org.radixware.wps.views.editor.PropertiesGrid();
		widget.setObjectName("widget");
		this.widget.add(this.widget);
		widget.getAnchors().setTop(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setLeft(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setBottom(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		widget.getAnchors().setRight(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		this.wdgW3Y3J2RAURG6TKTLDNV2N7BYPA.bind();
		fireOpened();
	}
}

/* Radix::UserFunc::UserFunc:General:View - Desktop Executable*/

/*Radix::UserFunc::UserFunc:General:View-Custom Editor for Desktop*/

package org.radixware.ads.UserFunc.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:View")
public class View extends org.radixware.kernel.explorer.views.Editor {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View Editor;
	public View getEditor(){ return Editor;}
	public org.radixware.kernel.explorer.widgets.EditorPage editorPage;
	public org.radixware.kernel.explorer.widgets.EditorPage getEditorPage(){ return editorPage;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession) {
		super(userSession);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		Editor = this;
		Editor.setObjectName("Editor");
		Editor.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 625, 386));
		Editor.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QVBoxLayout $layout1 = new com.trolltech.qt.gui.QVBoxLayout(this.content);
		$layout1.setObjectName("verticalLayout");
		$layout1.setContentsMargins(0, 0, 0, 0);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setSpacing(6);
		editorPage = new org.radixware.kernel.explorer.widgets.EditorPage(getEntityModel().getEditorPage(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGWV57X7UTNDE3HWQIAKKSKDBLM")));
		editorPage.setParent(this.content);
		editorPage.bind();
		editorPage.setObjectName("editorPage");
		editorPage.setFont(DEFAULT_FONT);
		$layout1.addWidget(editorPage);
		Editor.closed.connect(model, "mth5APATPYBTNHUNOTSEHNVYUYEZU()");
		Editor.opened.connect(model, "mthIZIIGMY3WRA6NKEUQAE5PIFNOM(com.trolltech.qt.gui.QWidget)");
		opened.emit(this.content);
	}
	public final org.radixware.ads.UserFunc.explorer.General:Model getModel() {
		return (org.radixware.ads.UserFunc.explorer.General:Model) super.getModel();
	}

}

/* Radix::UserFunc::UserFunc:Create - Desktop Meta*/

/*Radix::UserFunc::UserFunc:Create-Editor Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGWQBF5FKIVGRFHCOVCDXBLS3TQ"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
	null,
	null,

	/*Radix::UserFunc::UserFunc:Create:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::UserFunc::UserFunc:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::UserFunc::UserFunc:Create:Model - Desktop Executable*/

/*Radix::UserFunc::UserFunc:Create:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:Create:Model")
public class Create:Model  extends org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::UserFunc::UserFunc:Create:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFunc:Create:Model:Properties-Properties*/

	/*Radix::UserFunc::UserFunc:Create:Model:Methods-Methods*/






}

/* Radix::UserFunc::UserFunc:Create:Model - Desktop Meta*/

/*Radix::UserFunc::UserFunc:Create:Model-Entity Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemGWQBF5FKIVGRFHCOVCDXBLS3TQ"),
						"Create:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::UserFunc:Create:Model:Properties-Properties*/
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

/* Radix::UserFunc::UserFunc:Usages - Desktop Meta*/

/*Radix::UserFunc::UserFunc:Usages-Selector Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class Usages_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Usages_mi();
	private Usages_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHMDW6GTMKJC47FL5GNCYGTMKQU"),
		"Usages",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		18559,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPFPZWKHWJNFB5H4GA2XM6MYFE4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQY3XHAZTHZB57GEKAHI6K23SEM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLKXRGY3PCRBR3ED4HQEB25EODE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEY7LJLL77RDNLLC655BJGHGEL4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP73O4GPGEBHW7O4YHOK5RDTGF4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5HQW5K6ILZEC7LCIL66JEZNP7U"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYF2NT733ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCGFACL4ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.UserFunc.explorer.UserFunc.DefaultGroupModel(userSession,this);
	}
}
/* Radix::UserFunc::UserFunc:General - Desktop Meta*/

/*Radix::UserFunc::UserFunc:General-Selector Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4DRA4YXDZNALTA4KT73WNRMYSQ"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXFHMVDYMSJCGNHQBMW6YCHTRTQ"),
		null,
		false,
		true,
		null,
		17467,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNTOBFKCWDNA7DCJMVLXTSYYEUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd6BI243J5TFAVVN3NA2I6TPTYUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdG3QHVWEBQBGNPJQV3RFULJWQNE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZYKIKMNUXFAJXKWYRPUKGT4LWE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAEPY27UUVJHVXC3BHUPA7QJSC4")},
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPFPZWKHWJNFB5H4GA2XM6MYFE4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2ALXV2PR4FBRJF4M77FXFVJVIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQY3XHAZTHZB57GEKAHI6K23SEM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ4TLAHYKT5ECVLDKAXYMT44BXY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLVJEVPXKWVFVLIBJL7NHJNZJKU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLKXRGY3PCRBR3ED4HQEB25EODE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5HQW5K6ILZEC7LCIL66JEZNP7U"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEY7LJLL77RDNLLC655BJGHGEL4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP73O4GPGEBHW7O4YHOK5RDTGF4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYF2NT733ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNCGFACL4ZHOBDCMTAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::UserFunc::UserFunc:General:Model - Desktop Executable*/

/*Radix::UserFunc::UserFunc:General:Model-Group Model Class*/

package org.radixware.ads.UserFunc.explorer;

import org.radixware.kernel.explorer.editors.jmleditor.dialogs.StackTraceDialog;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model")
public class General:Model  extends org.radixware.ads.UserFunc.explorer.UserFunc.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::UserFunc::UserFunc:General:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFunc:General:Model:Properties-Properties*/

	/*Radix::UserFunc::UserFunc:General:Model:problemList-Dynamic Property*/



	protected org.radixware.kernel.explorer.editors.jmleditor.JmlProblemList problemList=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:problemList")
	public  org.radixware.kernel.explorer.editors.jmleditor.JmlProblemList getProblemList() {
		return problemList;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:problemList")
	public   void setProblemList(org.radixware.kernel.explorer.editors.jmleditor.JmlProblemList val) {
		problemList = val;
	}

	/*Radix::UserFunc::UserFunc:General:Model:userFuncLocator-Dynamic Property*/



	protected org.radixware.ads.UserFunc.explorer.SelectorUserFuncLocator userFuncLocator=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:userFuncLocator")
	public  org.radixware.ads.UserFunc.explorer.SelectorUserFuncLocator getUserFuncLocator() {
		return userFuncLocator;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:userFuncLocator")
	public   void setUserFuncLocator(org.radixware.ads.UserFunc.explorer.SelectorUserFuncLocator val) {
		userFuncLocator = val;
	}






	/*Radix::UserFunc::UserFunc:General:Model:Methods-Methods*/

	/*Radix::UserFunc::UserFunc:General:Model:onCommand_CompileAll-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:onCommand_CompileAll")
	private final  void onCommand_CompileAll (org.radixware.ads.UserFunc.explorer.UserFuncGroup.CompileAll command) {
		CommandsXsd:CompileRqDocument xRqDoc = CommandsXsd:CompileRqDocument.Factory.newInstance();

		xRqDoc.addNewCompileRq().Diagnose = false;//model.;
		try {
		    problemList.clear(-1);
		    CommandsXsd:CompileRsDocument xRsDoc = command.send(xRqDoc);
		    reread();

		    final String compileMessHeader = "Compile All";
		    if (xRsDoc != null && xRsDoc.CompileRs != null) {
		        problemList.acceptProblemList(userFuncLocator, xRsDoc);
		        if (xRsDoc.CompileRs.ResultHtml != null) {
		            Common.Dlg::ClientUtils.viewText(xRsDoc.CompileRs.ResultHtml, "Compilation Result");
		        } else if (!xRsDoc.CompileRs.CompileWasCancelled.booleanValue()) {
		            Environment.messageInformation(compileMessHeader, "Compilation complete!");
		        } else {
		            Environment.messageWarning(compileMessHeader, "Compilation was canceled.");
		        }
		    }
		} catch (Exceptions::InterruptedException e) {
		    Explorer.Env::Application.processException(e);
		} catch (Exceptions::ServiceClientException e) {
		    Explorer.Env::Application.processException(e);
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:onCommand_ExportAll-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:onCommand_ExportAll")
	public  void onCommand_ExportAll (org.radixware.ads.UserFunc.explorer.UserFuncGroup.ExportAll command) {
		try {
		    final Str fileName = com.trolltech.qt.gui.QFileDialog.getSaveFileName(
		            Explorer.Env::Application.getMainWindow(),
		            "Save File",
		            null,
		            new com.trolltech.qt.gui.QFileDialog.Filter("XML Files (*.xml)"));
		    if (fileName != null && !fileName.isEmpty()) {
		        Arte::TypesXsd:StrDocument strDoc = Arte::TypesXsd:StrDocument.Factory.newInstance();
		        strDoc.Str = fileName;
		        org.radixware.schemas.types.IntDocument xDoc = command.send(strDoc);
		        
		        if (xDoc == null || xDoc.Int == null || xDoc.Int.intValue() == 0) {
		            getEnvironment().messageInformation("Export", "No functions exported");
		        } else {
		            getEnvironment().messageInformation("Export", xDoc.Int.intValue() + " " + "functions exported");
		        }
		    }
		} catch (InterruptedException e) {
		} catch (Exceptions::ServiceClientException e) {
		    getEnvironment().processException(e);
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:onCommand_StackTrace-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:onCommand_StackTrace")
	public  void onCommand_StackTrace (org.radixware.ads.UserFunc.explorer.UserFuncGroup.StackTrace command) {
		final Explorer.Models::GroupModelReader userFuncReader = new GroupModelReader(this, java.util.EnumSet.of(Explorer.Models::GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));

		StackTraceDialog.IPidGetter pidGetter = new StackTraceDialog.IPidGetter() {
		    public Explorer.Types::Pid getPidByOwnerPid(String upDefId) {
		        for (Explorer.Models::EntityModel userFunc : userFuncReader) {
		            UserFunc:General:Model userFuncModel = (UserFunc:General:Model) userFunc;
		            if (userFuncModel != null && userFuncModel.upOwnerPid.Value.equals(upDefId))
		                return ((UserFunc:General:Model) userFunc).getPid();
		        }
		        return null;
		    }

		    public org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef getUserFuncByPid(long pid) {
		        Explorer.Models::GroupModelReader reader = new GroupModelReader(UserFunc:General:Model.this);
		        for (Explorer.Models::EntityModel entity : reader) {
		            if (entity instanceof UserFunc:General:Model) {
		                UserFunc:General:Model uf = (UserFunc:General:Model) entity;
		                if (uf.id.Value == pid) {
		                    return uf.findUserFunc(null);
		                }
		            }
		        }

		        return null;
		    }
		};

		StackTraceDialog dialog = new StackTraceDialog(getEnvironment(), (Explorer.Qt.Types::QWidget) getView(), userFuncLocator, pidGetter);
		dialog.exec();

	}

	/*Radix::UserFunc::UserFunc:General:Model:onCommand_ValidateAll-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:onCommand_ValidateAll")
	public  void onCommand_ValidateAll (org.radixware.ads.UserFunc.explorer.UserFuncGroup.ValidateAll command) {
		try{
		    problemList.clear(-1);
		    CommandsXsd:CompileRsDocument xRsDoc = command.send();
		    reread();
		    
		    if(xRsDoc != null && xRsDoc.CompileRs != null){
		        problemList.acceptProblemList(userFuncLocator,xRsDoc);
		    }
		    String mess="Validation complete!";
		    Environment.messageInformation(mess, mess);
		}catch(Exceptions::InterruptedException e){
		    Explorer.Env::Application.processException(e);
		}catch(Exceptions::ServiceClientException e){
		    Explorer.Env::Application.processException(e);
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:onCommand_CheckForAPIChanges-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:onCommand_CheckForAPIChanges")
	public  void onCommand_CheckForAPIChanges (org.radixware.ads.UserFunc.explorer.UserFuncGroup.CheckForAPIChanges command) {
		try {
		    String fileName = com.trolltech.qt.gui.QFileDialog.getOpenFileName(
		            null,
		            "Select API changes file",
		            null,
		            new com.trolltech.qt.gui.QFileDialog.Filter("XML Files (*.xml)"));

		    if (fileName == null || fileName.isEmpty())
		        return;
		    java.io.File file = new java.io.File(fileName);
		    try {
		        Common::ProductXsd:ApiChangesDocument xIn = Common::ProductXsd:ApiChangesDocument.Factory.parse(file);
		        command.send(xIn);
		    } catch (Exceptions::IOException e) {
		        getEnvironment().messageError("Unable to read API description file");
		    } catch (Exceptions::XmlException e) {
		        getEnvironment().messageError("Wrong file format or file corrupted");
		    }

		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::UserFunc::UserFunc:General:Model:clean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:General:Model:clean")
	public published  void clean () {
		super.clean();
		problemList = null;
		userFuncLocator = null;
	}
	public final class ExportAll extends org.radixware.ads.UserFunc.explorer.UserFuncGroup.ExportAll{
		protected ExportAll(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ExportAll( this );
		}

	}

	public final class CheckForAPIChanges extends org.radixware.ads.UserFunc.explorer.UserFuncGroup.CheckForAPIChanges{
		protected CheckForAPIChanges(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_CheckForAPIChanges( this );
		}

	}

	public final class StackTrace extends org.radixware.ads.UserFunc.explorer.UserFuncGroup.StackTrace{
		protected StackTrace(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_StackTrace( this );
		}

	}

	public final class CompileAll extends org.radixware.ads.UserFunc.explorer.UserFuncGroup.CompileAll{
		protected CompileAll(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_CompileAll( this );
		}

	}

	public final class ValidateAll extends org.radixware.ads.UserFunc.explorer.UserFuncGroup.ValidateAll{
		protected ValidateAll(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ValidateAll( this );
		}

	}

























}

/* Radix::UserFunc::UserFunc:General:Model - Desktop Meta*/

/*Radix::UserFunc::UserFunc:General:Model-Group Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4DRA4YXDZNALTA4KT73WNRMYSQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::UserFunc:General:Model:Properties-Properties*/
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

/* Radix::UserFunc::UserFunc:CreateInLibrary - Desktop Meta*/

/*Radix::UserFunc::UserFunc:CreateInLibrary-Selector Presentation*/

package org.radixware.ads.UserFunc.explorer;
public final class CreateInLibrary_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new CreateInLibrary_mi();
	private CreateInLibrary_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJXA4VMNLWBH6DPNJHIXMNHXBHA"),
		"CreateInLibrary",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGWQBF5FKIVGRFHCOVCDXBLS3TQ")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6V3CVGAHT5GRXAQ6VRLN3YVQGQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.UserFunc.explorer.UserFunc.DefaultGroupModel(userSession,this);
	}
}
/* Radix::UserFunc::UserFunc:Invalid:Model - Desktop Executable*/

/*Radix::UserFunc::UserFunc:Invalid:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:Invalid:Model")
public class Invalid:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Invalid:Model_mi.rdxMeta; }



	public Invalid:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::UserFunc::UserFunc:Invalid:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFunc:Invalid:Model:Properties-Properties*/






	/*Radix::UserFunc::UserFunc:Invalid:Model:Methods-Methods*/






}

/* Radix::UserFunc::UserFunc:Invalid:Model - Desktop Meta*/

/*Radix::UserFunc::UserFunc:Invalid:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Invalid:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcSSCZW2PBHBAIXEYY2GD3H5UVUA"),
						"Invalid:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::UserFunc:Invalid:Model:Properties-Properties*/
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

/* Radix::UserFunc::UserFunc:Undefined:Model - Desktop Executable*/

/*Radix::UserFunc::UserFunc:Undefined:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:Undefined:Model")
public class Undefined:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Undefined:Model_mi.rdxMeta; }



	public Undefined:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::UserFunc::UserFunc:Undefined:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFunc:Undefined:Model:Properties-Properties*/






	/*Radix::UserFunc::UserFunc:Undefined:Model:Methods-Methods*/






}

/* Radix::UserFunc::UserFunc:Undefined:Model - Desktop Meta*/

/*Radix::UserFunc::UserFunc:Undefined:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Undefined:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcR2YI43ZJIJESJNSIB2DBS646JE"),
						"Undefined:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::UserFunc:Undefined:Model:Properties-Properties*/
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

/* Radix::UserFunc::UserFunc:BySourceText:Model - Desktop Executable*/

/*Radix::UserFunc::UserFunc:BySourceText:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:BySourceText:Model")
public class BySourceText:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return BySourceText:Model_mi.rdxMeta; }



	public BySourceText:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::UserFunc::UserFunc:BySourceText:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFunc:BySourceText:Model:Properties-Properties*/








	/*Radix::UserFunc::UserFunc:BySourceText:Model:Methods-Methods*/

	/*Radix::UserFunc::UserFunc:BySourceText:snippet:snippet-Presentation Property*/




	public class Snippet extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Snippet(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:BySourceText:snippet:snippet")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:BySourceText:snippet:snippet")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Snippet getSnippet(){return (Snippet)getProperty(prm26DWQSCW5VF37NOE7F7MHY7H7A);}






}

/* Radix::UserFunc::UserFunc:BySourceText:Model - Desktop Meta*/

/*Radix::UserFunc::UserFunc:BySourceText:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class BySourceText:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmc56W7TVKG35DX7C376TDH4KGQYY"),
						"BySourceText:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::UserFunc:BySourceText:Model:Properties-Properties*/
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

/* Radix::UserFunc::UserFunc:ById:Model - Desktop Executable*/

/*Radix::UserFunc::UserFunc:ById:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ById:Model")
public class ById:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ById:Model_mi.rdxMeta; }



	public ById:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::UserFunc::UserFunc:ById:Model:Nested classes-Nested Classes*/

	/*Radix::UserFunc::UserFunc:ById:Model:Properties-Properties*/








	/*Radix::UserFunc::UserFunc:ById:Model:Methods-Methods*/

	/*Radix::UserFunc::UserFunc:ById:idParam:idParam-Presentation Property*/




	public class IdParam extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public IdParam(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ById:idParam:idParam")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::UserFunc::UserFunc:ById:idParam:idParam")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public IdParam getIdParam(){return (IdParam)getProperty(prmGREQZOIXRFGW7GHYAT65QIUQ6M);}






}

/* Radix::UserFunc::UserFunc:ById:Model - Desktop Meta*/

/*Radix::UserFunc::UserFunc:ById:Model-Filter Model Class*/

package org.radixware.ads.UserFunc.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ById:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcYAHTHJU32BB67JJVNKDRDKFIMI"),
						"ById:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::UserFunc::UserFunc:ById:Model:Properties-Properties*/
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

/* Radix::UserFunc::UserFunc - Localizing Bundle */
package org.radixware.ads.UserFunc.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserFunc - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"The following objects used by the UDF have not been found in the database:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Следующие объекты, используемые UDF, не найдены в базе данных:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2R27VRMBHVA2BO5C4XDZI7COIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Required version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Требуемая версия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2X4FBYL2FVDJTL5JF775ZP4URM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class GUID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"GUID класса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ZRQY5J2HZDJJOX6KRXGTLD4XM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"functions exported");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"функций экспортировано");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3DD5OOELGNH4HDEN56MUVX3UXU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wrong file format or file corrupted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Файл поврежден или не является файлом описания изменений API");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3RO5MJ4H7VEWLNSDXWWIYDJZJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"upOwnerEntityId");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"upOwnerEntityId");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4N3LV2N2DRHFXLYRA4YYKT3LMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Current user can only create functions that use binding to library functions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текущий пользователь может создавать только функции использующие связывание с библиотечными");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4PXRAW6SHBC23OOQNSUIDH5JTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML Files (*.xml)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML файлы (*.xml)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4S5LW36FXRB5HEZ47HTT2RTVUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation Result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат компиляции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4SSXPNDOQJCBTLX3VDQ4TMJJP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Owner class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс владельца");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls53PZY46IXFFORNNC64KWN6FW5A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class provides the methods to configure the import parameters and perform the import of the user-defined function.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс позволяет сконфигурировать параметры импорта и произвести импорт пользовательской функции.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DHICWAKU5AURLL3DRZYPZSZXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select API changes file");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Укажите файл описания изменений API");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5E2HRFXX5VACVNVBS3Q42VZPNE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation was canceled.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Компиляция была отменена.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5KYAWGVTP5FJ5ONEYFPYT346O4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Deprecated: Use Radix::UserFunc::UserFunc:import(Entity, Id, boolean, Common::CommonXsd::UserFunc, ICfgImportHelper) instead.\n2 reasons:\n1) Possibility of actualizing of function owner tag before function compilation during import;\n2) Using ICfgImportHelper to notify user about import result.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устаревшее API: Используйте Radix::UserFunc::UserFunc:import(Entity, Id, boolean, Common::CommonXsd::UserFunc, ICfgImportHelper).\n2 Причины:\n1) Возможность актуализации тега со ссылкой на владельца функции перед компиляций;\n2) Использование ICfgImportHelper для оповещения пользователя о результатах импорта.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5P63NCUAVZEMLFDTQKMTIWSBNU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Owner - library function (can be null)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Владелец - библиотечная функция (может быть null)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5VAJ54P6BFDALEWYD3F3NQKQAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compiled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Откомпилирована");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls62NPHG2O6RAPZIP7IKPBG2BCI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compile All");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Скомпилировать всё");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6AVHPGASQZCQLDCTZQKAIXH6SI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Сyclic binding. Infinite recursion is possible");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Циклическое связывание. Возможна бесконечная рекурсия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6GFSHIGRJ5DCRKR7DG32T46ZAM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls72ZCCCQQPBCWXNZVAXOHBETBSM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Ambiguous link, following entities of type \'%s\':");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неоднозначная связь, следующие сущности типа \'%s\':");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls747F47Y5HBBHVIP737FKU2RR3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Confirm Object Deletion");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подтверждение удаления объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls74E3G6TYZNA4PITYCHNGV6R4DI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation complete!");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Компиляция завершена!");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls74ZZH6U2HJDFTPAULLFFTG62SU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Validation complete!");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Валидация завершена!");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7GOZXWY34VA6TJBU7GNY2IEOBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7WRENQIAR5EDTPAQD626WDWRZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XD4EC4XZHOBDCMTAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Some problems found via function validation. Do you really want to continue?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверка обнаружила ошибки. Вы действительно хотите продолжить?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAMZ33LWJCREW5N65KVIJEIBOAI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Retreives and locks version of the UDF stored in database. Version lock disables on-the-fly update of the UDF version, which is required for situations where developer needs to decide which prototype of the UDF has to be called. unlockVersion() should always be called after lockVersion() with try-finally combination");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAVYI7DOLFRB5RCZPZNOJSY3RHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"file");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAZ3BLZSQDNEC7NNSD6CJNHGNMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBARCF67KSJAKRFXZWBPRBHYRM4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Save File");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сохранить файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBCVA35MBCJAHLOGKC5WKKVVXXA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Insufficient access rights to develop user-defined functions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Недостаточно прав для разработки пользовательских функций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBF6ETBTHGRGEFGMKHZD4PLWEBY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML Files (*.xml)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML файлы (*.xml)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBS34NDDHQFEB7GFI4WTS5Q2LLI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBV63MAUXZHOBDCMTAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Extended source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Другой исходный код");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCVWF5UUWZHOBDCMTAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Function ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор функции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD5BW5C77CNC2XLXQDTTPLCWR7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Indicates that UF and UF owner creation appears at the same time.\nIt is necessary to actualize javaSrc and javaSrcVers in before create event when this flag is set.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDA4HNOJORRCATPQA2IHYL7EKQ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательская функция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDNXNYRJEPND5NLJF3ON36TY6XI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unexpected error occurred on function compilation. See details in trace.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неожиданная ошибка произошла при компиляции функции. См. подробности в трассе.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDOAZFB2ZKRHHZG3WHKXQTHB5HU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDSKYHRHE3JDSDEC75Z3I6HHNWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Linked Library User Function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Связанная библиотечная функция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE73A4EU4GJFWNMHCF2RPYAP3OA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invalid Function State");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция некорректна");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDM2XOF4OVEAXOFRPPIOBJ465E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIRMIQ7DRVDJXC2TU6SSE4I3BQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Save File");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сохранить файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF35DBJSWBJGXROMXORQGF5JMR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF4NDFLISBZHRVC5RVNOYO4R4RQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<function defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<функция определена>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF4VSULBWXBFQ3CYJ744FFEEQQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"linked library function (\'%s\') not found");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"связанная библиотечная функция (\'%s\') не найдена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFCKLTVPLTJFUBKDYX4E3MMAQWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFEVTY3TYBVC43OGRLRICVHGUQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFU7IVGEOOFCD3JV6JWKYXSFIVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"No functions exported");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не экспортировано ни одной функции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGEHBZDSFMFDWZBRDLKNKYBOVXQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unlock UDF version, see lockVersion()");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGH6MTKIR6ZHETCLRYQJPEOBIWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGKNFFLGI2ZACHERCDHLX72BUAI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invalid UDF");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невалидная функция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHU5U6AQO4BAPFO23LNHYAET6BA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"returns list of library functions, used by current function");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsICTXSCUUYNHDLDERQPVNOLIGHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"has same ExtGuid: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"имеют одинаковый ExtGuid: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIGZJIVXVM5FAZL7JLPXC7QRE7Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User \'%1\' modified user-defined function \'%2\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь \"%1\" изменил пользовательскую функцию \"%2\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIVSB22MX6FBOLBVMABUIXK4S2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Audit",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По идентификатору");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ4EBVNECQJH77P5MHNLQYEQLOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By modification time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По времени модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJFYGCCXBZAI7OWHG63SKXU3OY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<source method not found>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<исходный метод не найден>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJR667VUAAZHGLMOTJY6OMJBPFM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Functions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательские функции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKNZKVTMXZHOBDCMTAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Function ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. функции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLEIFVQYTCRCR7O5LQRVK5NPKFA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Method ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. метода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJET4FUYTFDELDVA5FDANT7DUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last saved version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Фактическая версия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsML67B7EKHFE4DGNS6RTOX2NZNY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System not support version %s of user function profile. Current profile version will be used instead: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Система не поддерживает версию %s профиля пользовательской функции. Будет использована текущая версия профиля: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN3DGFVQMBBFYLFOOQDLWRRSMZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Do you really want to delete");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вы действительно хотите удалить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN3TRK2C3YVB5DO3E466THNJA2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Library Function ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. библиотечной функции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNGONIZIIEBC55PWT6HDFZUYDQI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-defined function owner does not exist");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Владелец пользовательской функции не существует");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO3V6WNHN3ND7FMKIILN5JVPFRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"upOwnerClassId");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"upOwnerClassId");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOFA44NLVNJCA7DBT2VCDOM3A5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<owner object not found, PID ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<владелец не найден, PID ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKGDO5Q43FBIXDPC6NQBTBNGOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Bind with library function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Связать с библиотечной функцией");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPK4O44NEJ5ANBIL2CFSAIQBCZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Owner");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Владелец");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQKET7J6H2RHVPMW6OPVRQ5ZUFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User defined function owner does not support export function to package.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Владелец пользовательской функции не поддерживает экспорт функции в пакет конфигурации.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBPWGLZD7ZDTLBCFXCHL5SMEQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Not compiled or erroneous");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не откомпилированные или содержащие ошибки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRTIXYU5XQZBVJPCK4OZI6JBNKM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Linked library user function is defined, but not used. Do you want to save changes?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Связанная библиотечная функция определена, но не используется. Вы хотите сохранить изменения?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS6GKYSPVDJBTTK67QY3VWMNGIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Сyclic binding. Infinite recursion is possible");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Циклическое связывание. Возможна бесконечная рекурсия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSU5SDN2ZOFGKTEVQH5NY6BOD64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Runtime data");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Информация времени исполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSUXRA2ZYCVEUXFPRVQHFZPOIRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По идентификатору.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCMXJLZWWJCLNF3YYF7BMLSSWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"RADIX-12221:\nOn moment of import user function owner can be not created yet, so we must save ownerClassId from xml to successfully compile user function with owner tags.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTFHDQV66WRH55AN3ZQSECGVZJI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Empty");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неописанные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXUGMTUUGNE6LLFEVUEY34AMAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"upOwnerPid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"upOwnerPid");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7LYHXK53FHL7AWV3I6STSEVR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Convert to Library Function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Преобразовать в библиотечную функцию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWTEHTIAQBRHVZFDONBUORXOZS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательская функция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWUMB3KKA6ZA63APKRKZLUGG7RI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to read API description file");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось прочитать файл описания изменений API");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX3M2EUJPEBC6VP6GJEFH7RLBFA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity of type \'%s\' with ExtGuid \'%s\' not found");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сущность типа \'%s\' с ExtGuid \'%s\' не найдена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDYEPDYR2FHH7JGRALOVCPLY3Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Backup before importing from %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резервная копия перед импортом из %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXLFSZXTAIVHLNEGFU4JB3I7GVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Owner property");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Свойство владельца");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY2G2XCQ53BH6ZCKSJVBJVFTGOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Base class describing the user-defined function storage provides utility methods to compile, load, save, and edit user-defined functions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Базовый класс, описывающий хранение пользовательской функции, предоставляет служебные методы для компиляции, загрузки, сохранения и редактирования пользовательских функций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYLOVLXP7OZD4PEOT6JHBHHD7HQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By source code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По тексту");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZNAHTKFXBRBAFFOLDXF2NJP4H4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"upDefId");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"upDefId");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUAWK5V3IZE2PD25UDIX3V6BIE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-defined class GUID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"GUID пользовательского класса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUV3IEIL7FCCPNWBF3O4BHG5FQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Java source code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Java исходный код");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZVMPR4UWZHOBDCMTAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(UserFunc - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecJ6SOXKD3ZHOBDCMTAALOMT5GDM"),"UserFunc - Localizing Bundle",$$$items$$$);
}


/* Radix::SystemMonitor::StackData - Server Executable*/

/*Radix::SystemMonitor::StackData-Entity Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData")
public final class StackData  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return StackData_mi.rdxMeta;}

	/*Radix::SystemMonitor::StackData:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::StackData:Properties-Properties*/

	/*Radix::SystemMonitor::StackData:compressedStack-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:compressedStack")
	public  java.sql.Blob getCompressedStack() {
		return compressedStack;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:compressedStack")
	public   void setCompressedStack(java.sql.Blob val) {
		compressedStack = val;
	}

	/*Radix::SystemMonitor::StackData:digest-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:digest")
	public  Str getDigest() {
		return digest;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:digest")
	public   void setDigest(Str val) {
		digest = val;
	}

	/*Radix::SystemMonitor::StackData:stackTop-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:stackTop")
	public  Str getStackTop() {
		return stackTop;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:stackTop")
	public   void setStackTop(Str val) {
		stackTop = val;
	}

	/*Radix::SystemMonitor::StackData:uncompressedStack-Dynamic Property*/



	protected Str uncompressedStack=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:uncompressedStack")
	public  Str getUncompressedStack() {

		if (internal[uncompressedStack] != null) {
		    return internal[uncompressedStack];
		}

		Blob blob = null;
		try {
		    blob = compressedStack;
		} catch (Exceptions::EntityObjectNotExistsError ex) {
		    internal[uncompressedStack] = "\"Stack not exists\" error";
		    return internal[uncompressedStack];
		}

		byte[] compressedStack;
		try {
		    compressedStack = compressedStack == null ? null : compressedStack.getBytes(1L, (int)compressedStack.length());
		} catch (Exception e) {
		    throw new Error("Failed to get compressedStack bytes", e);
		}

		String uncompressedStack;
		try {
		    uncompressedStack = org.radixware.kernel.common.utils.Utils.decompressString(compressedStack);
		} catch (Exception e) {
		    throw new Error("Failed to decompress stack", e);
		}

		internal[uncompressedStack] = uncompressedStack == null ? "" : uncompressedStack;
		return internal[uncompressedStack];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:uncompressedStack")
	public   void setUncompressedStack(Str val) {
		uncompressedStack = val;
	}





















































	/*Radix::SystemMonitor::StackData:Methods-Methods*/


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::StackData - Server Meta*/

/*Radix::SystemMonitor::StackData-Entity Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class StackData_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),"StackData",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::SystemMonitor::StackData:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
							/*Owner Class Name*/
							"StackData",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::SystemMonitor::StackData:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::StackData:compressedStack:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUILPI7J34VDJTGKLOXKYLVL2K4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::StackData:digest:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIAUYIKCLEBHP5MU4ELPXVEQWJU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::StackData:stackTop:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLS76H6PRRGDTL2GZCCMYNFJXM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::StackData:uncompressedStack:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd222VBCWD3NE7FDJQTFYYZYZD2U"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							null,
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::SystemMonitor::StackData:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),null,null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28679,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2R4PKYCFCVHGRP2YVVFOORGWIE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::SystemMonitor::StackData:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::StackData:compressedStack-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUILPI7J34VDJTGKLOXKYLVL2K4"),"compressedStack",null,org.radixware.kernel.common.enums.EValType.BLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::StackData:digest-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIAUYIKCLEBHP5MU4ELPXVEQWJU"),"digest",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::StackData:stackTop-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLS76H6PRRGDTL2GZCCMYNFJXM"),"stackTop",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::StackData:uncompressedStack-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd222VBCWD3NE7FDJQTFYYZYZD2U"),"uncompressedStack",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::StackData:Methods-Methods*/
						null,
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::SystemMonitor::StackData - Desktop Executable*/

/*Radix::SystemMonitor::StackData-Entity Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData")
public interface StackData {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.SystemMonitor.explorer.StackData.StackData_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.SystemMonitor.explorer.StackData.StackData_DefaultModel )  super.getEntity(i);}
	}










































	/*Radix::SystemMonitor::StackData:digest:digest-Presentation Property*/


	public class Digest extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Digest(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:digest:digest")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:digest:digest")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Digest getDigest();
	/*Radix::SystemMonitor::StackData:stackTop:stackTop-Presentation Property*/


	public class StackTop extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public StackTop(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:stackTop:stackTop")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:stackTop:stackTop")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StackTop getStackTop();
	/*Radix::SystemMonitor::StackData:compressedStack:compressedStack-Presentation Property*/


	public class CompressedStack extends org.radixware.kernel.common.client.models.items.properties.PropertyBlob{
		public CompressedStack(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:compressedStack:compressedStack")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:compressedStack:compressedStack")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public CompressedStack getCompressedStack();
	/*Radix::SystemMonitor::StackData:uncompressedStack:uncompressedStack-Presentation Property*/


	public class UncompressedStack extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UncompressedStack(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:uncompressedStack:uncompressedStack")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:uncompressedStack:uncompressedStack")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UncompressedStack getUncompressedStack();


}

/* Radix::SystemMonitor::StackData - Desktop Meta*/

/*Radix::SystemMonitor::StackData-Entity Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class StackData_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::StackData:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
			"Radix::SystemMonitor::StackData",
			null,
			null,
			null,
			null,null,null,28679,

			/*Radix::SystemMonitor::StackData:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::StackData:compressedStack:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUILPI7J34VDJTGKLOXKYLVL2K4"),
						"compressedStack",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
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

					/*Radix::SystemMonitor::StackData:digest:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIAUYIKCLEBHP5MU4ELPXVEQWJU"),
						"digest",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
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

						/*Radix::SystemMonitor::StackData:digest:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::StackData:stackTop:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLS76H6PRRGDTL2GZCCMYNFJXM"),
						"stackTop",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::StackData:stackTop:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::StackData:uncompressedStack:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd222VBCWD3NE7FDJQTFYYZYZD2U"),
						"uncompressedStack",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
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

						/*Radix::SystemMonitor::StackData:uncompressedStack:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
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
			false,false,false);
}

/* Radix::SystemMonitor::StackData - Web Executable*/

/*Radix::SystemMonitor::StackData-Entity Class*/

package org.radixware.ads.SystemMonitor.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData")
public interface StackData {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.SystemMonitor.web.StackData.StackData_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.SystemMonitor.web.StackData.StackData_DefaultModel )  super.getEntity(i);}
	}










































	/*Radix::SystemMonitor::StackData:digest:digest-Presentation Property*/


	public class Digest extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Digest(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:digest:digest")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:digest:digest")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Digest getDigest();
	/*Radix::SystemMonitor::StackData:stackTop:stackTop-Presentation Property*/


	public class StackTop extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public StackTop(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:stackTop:stackTop")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:stackTop:stackTop")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StackTop getStackTop();
	/*Radix::SystemMonitor::StackData:compressedStack:compressedStack-Presentation Property*/


	public class CompressedStack extends org.radixware.kernel.common.client.models.items.properties.PropertyBlob{
		public CompressedStack(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:compressedStack:compressedStack")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:compressedStack:compressedStack")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public CompressedStack getCompressedStack();
	/*Radix::SystemMonitor::StackData:uncompressedStack:uncompressedStack-Presentation Property*/


	public class UncompressedStack extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UncompressedStack(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:uncompressedStack:uncompressedStack")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::StackData:uncompressedStack:uncompressedStack")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UncompressedStack getUncompressedStack();


}

/* Radix::SystemMonitor::StackData - Web Meta*/

/*Radix::SystemMonitor::StackData-Entity Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class StackData_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::StackData:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
			"Radix::SystemMonitor::StackData",
			null,
			null,
			null,
			null,null,null,28679,

			/*Radix::SystemMonitor::StackData:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::StackData:compressedStack:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUILPI7J34VDJTGKLOXKYLVL2K4"),
						"compressedStack",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
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

					/*Radix::SystemMonitor::StackData:digest:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIAUYIKCLEBHP5MU4ELPXVEQWJU"),
						"digest",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
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

						/*Radix::SystemMonitor::StackData:digest:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::StackData:stackTop:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLS76H6PRRGDTL2GZCCMYNFJXM"),
						"stackTop",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::StackData:stackTop:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::StackData:uncompressedStack:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd222VBCWD3NE7FDJQTFYYZYZD2U"),
						"uncompressedStack",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
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

						/*Radix::SystemMonitor::StackData:uncompressedStack:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
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
			false,false,false);
}

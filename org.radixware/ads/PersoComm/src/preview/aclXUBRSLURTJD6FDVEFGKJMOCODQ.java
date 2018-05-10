
/* Radix::PersoComm::Unit.Channel.Wns - Server Executable*/

/*Radix::PersoComm::Unit.Channel.Wns-Application Class*/

package org.radixware.ads.PersoComm.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns")
public class Unit.Channel.Wns  extends org.radixware.ads.PersoComm.server.Unit.Channel.AbstractPush  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Unit.Channel.Wns_mi.rdxMeta;}

	/*Radix::PersoComm::Unit.Channel.Wns:Nested classes-Nested Classes*/

	/*Radix::PersoComm::Unit.Channel.Wns:Properties-Properties*/

	/*Radix::PersoComm::Unit.Channel.Wns:wnsClientId-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientId")
	public  Str getWnsClientId() {
		return wnsClientId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientId")
	public   void setWnsClientId(Str val) {
		wnsClientId = val;
	}

	/*Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret")
	public  Str getWnsClientSecret() {
		return wnsClientSecret;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret")
	public   void setWnsClientSecret(Str val) {
		wnsClientSecret = val;
	}









































	/*Radix::PersoComm::Unit.Channel.Wns:Methods-Methods*/

	/*Radix::PersoComm::Unit.Channel.Wns:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:afterInit")
	protected  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		this.kind = ChannelKind:WNS;
		this.type = System::UnitType:DPC_WNS;
		this.sendAddress = "<not used>";
		this.recvAddress = "<not used>";
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::PersoComm::Unit.Channel.Wns - Server Meta*/

/*Radix::PersoComm::Unit.Channel.Wns-Application Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.Wns_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),"Unit.Channel.Wns",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SZPPJRFVFA2BD72G4FZO5MCQU"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::PersoComm::Unit.Channel.Wns:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
							/*Owner Class Name*/
							"Unit.Channel.Wns",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SZPPJRFVFA2BD72G4FZO5MCQU"),
							/*Property presentations*/

							/*Radix::PersoComm::Unit.Channel.Wns:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::PersoComm::Unit.Channel.Wns:wnsClientId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCS76OB3OTNFKPEVX7KUMX4KSGQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXHLUMSNPNDZHPAOHQSR42QJO4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::Unit.Channel.Wns:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWFGYPTR5FD4RE3U6RK6ZNPMGQ"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM"),
									36016,
									null,

									/*Radix::PersoComm::Unit.Channel.Wns:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM")),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::Unit.Channel.Wns:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDSYGBLVXMBCT7NRC7V7Y2JPHRE"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWFGYPTR5FD4RE3U6RK6ZNPMGQ"),
									40112,
									null,

									/*Radix::PersoComm::Unit.Channel.Wns:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWFGYPTR5FD4RE3U6RK6ZNPMGQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDSYGBLVXMBCT7NRC7V7Y2JPHRE")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::PersoComm::Unit.Channel.Wns:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKOGFFVPJAHOBDA26AAMPGXSZKU"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctC4I7K35XCFFEDEJ4E3TVGTP4G4"),165.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUSOKQ66VW5AO3GE7FKEPHT6VDY"),

						/*Radix::PersoComm::Unit.Channel.Wns:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::Unit.Channel.Wns:wnsClientId-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCS76OB3OTNFKPEVX7KUMX4KSGQ"),"wnsClientId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KWBYK5I3NF5XM7RIFLA4DA76A"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colDOMJ5BIHIRDLBBZX6CMS5BL5OA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXHLUMSNPNDZHPAOHQSR42QJO4"),"wnsClientSecret",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJKXKKNZGXBEJZHIU7LSULSZWDQ"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colD4FMZZ5MN5EZ5LB6Y366GDNJ2I"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::Unit.Channel.Wns:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYZM7BYAKUZH4VJIINN7HMCWFVQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXEM4NGHHZ5F5LMIP3Z7IRJJTL4"))
								},null)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM")},
						null,null,null,false);
}

/* Radix::PersoComm::Unit.Channel.Wns - Desktop Executable*/

/*Radix::PersoComm::Unit.Channel.Wns-Application Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns")
public interface Unit.Channel.Wns   extends org.radixware.ads.PersoComm.explorer.Unit.Channel.AbstractPush  {

































































	/*Radix::PersoComm::Unit.Channel.Wns:wnsClientId:wnsClientId-Presentation Property*/


	public class WnsClientId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public WnsClientId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientId:wnsClientId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientId:wnsClientId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public WnsClientId getWnsClientId();
	/*Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:wnsClientSecret-Presentation Property*/


	public class WnsClientSecret extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public WnsClientSecret(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:wnsClientSecret")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:wnsClientSecret")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public WnsClientSecret getWnsClientSecret();


}

/* Radix::PersoComm::Unit.Channel.Wns - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.Wns-Application Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.Wns_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::Unit.Channel.Wns:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
			"Radix::PersoComm::Unit.Channel.Wns",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUSOKQ66VW5AO3GE7FKEPHT6VDY"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SZPPJRFVFA2BD72G4FZO5MCQU"),null,null,0,

			/*Radix::PersoComm::Unit.Channel.Wns:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::Unit.Channel.Wns:wnsClientId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCS76OB3OTNFKPEVX7KUMX4KSGQ"),
						"wnsClientId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KWBYK5I3NF5XM7RIFLA4DA76A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.Wns:wnsClientId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXHLUMSNPNDZHPAOHQSR42QJO4"),
						"wnsClientSecret",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJKXKKNZGXBEJZHIU7LSULSZWDQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,true,400,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWFGYPTR5FD4RE3U6RK6ZNPMGQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDSYGBLVXMBCT7NRC7V7Y2JPHRE")},
			true,true,false);
}

/* Radix::PersoComm::Unit.Channel.Wns - Web Executable*/

/*Radix::PersoComm::Unit.Channel.Wns-Application Class*/

package org.radixware.ads.PersoComm.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns")
public interface Unit.Channel.Wns   extends org.radixware.ads.PersoComm.web.Unit.Channel.AbstractPush  {



































































	/*Radix::PersoComm::Unit.Channel.Wns:wnsClientId:wnsClientId-Presentation Property*/


	public class WnsClientId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public WnsClientId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientId:wnsClientId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientId:wnsClientId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public WnsClientId getWnsClientId();
	/*Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:wnsClientSecret-Presentation Property*/


	public class WnsClientSecret extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public WnsClientSecret(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:wnsClientSecret")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:wnsClientSecret")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public WnsClientSecret getWnsClientSecret();


}

/* Radix::PersoComm::Unit.Channel.Wns - Web Meta*/

/*Radix::PersoComm::Unit.Channel.Wns-Application Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.Wns_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::Unit.Channel.Wns:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
			"Radix::PersoComm::Unit.Channel.Wns",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUSOKQ66VW5AO3GE7FKEPHT6VDY"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SZPPJRFVFA2BD72G4FZO5MCQU"),null,null,0,

			/*Radix::PersoComm::Unit.Channel.Wns:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::Unit.Channel.Wns:wnsClientId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCS76OB3OTNFKPEVX7KUMX4KSGQ"),
						"wnsClientId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KWBYK5I3NF5XM7RIFLA4DA76A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.Wns:wnsClientId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXHLUMSNPNDZHPAOHQSR42QJO4"),
						"wnsClientSecret",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJKXKKNZGXBEJZHIU7LSULSZWDQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.Wns:wnsClientSecret:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,true,400,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			null,
			true,true,false);
}

/* Radix::PersoComm::Unit.Channel.Wns:Edit - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.Wns:Edit-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWFGYPTR5FD4RE3U6RK6ZNPMGQ"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,

			/*Radix::PersoComm::Unit.Channel.Wns:Edit:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::PersoComm::Unit.Channel.Wns:Edit:Channel-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRTEJ2K77VXOBDCLVAALOMT5GDM"),"Channel",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSBXL5LJXRFYVPWIIYAB5NHMXI"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVZDYDMEVTOBDCLTAALOMT5GDM"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBZDYDMEVTOBDCLTAALOMT5GDM"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7HPHZW2DJRHVJEYJSANRF7CASM"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4TIXFUZ7YNDYPDT5MVYA5XWERA"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXRZDYDMEVTOBDCLTAALOMT5GDM"),0,7,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZYMZD3ODGNDZZDU2Q746Q65XXE"),0,9,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCFB4V6V7VC47GPA5WGBSCKPWA"),0,8,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7GIBM6CVJDIXB2MCZKYXAAEJI"),0,10,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCS76OB3OTNFKPEVX7KUMX4KSGQ"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXHLUMSNPNDZHPAOHQSR42QJO4"),0,2,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRTEJ2K77VXOBDCLVAALOMT5GDM")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
			,

			/*Radix::PersoComm::Unit.Channel.Wns:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.Unit.Channel.Wns.Unit.Channel.Wns_DefaultModel.eprGVZBTVH6VXOBDCLVAALOMT5GDM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::PersoComm::Unit.Channel.Wns:Edit - Web Meta*/

/*Radix::PersoComm::Unit.Channel.Wns:Edit-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWFGYPTR5FD4RE3U6RK6ZNPMGQ"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,

			/*Radix::PersoComm::Unit.Channel.Wns:Edit:Editor Pages-Editor Presentation Pages*/
			null,
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
			}
			,

			/*Radix::PersoComm::Unit.Channel.Wns:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.Unit.Channel.Wns.Unit.Channel.Wns_DefaultModel.eprGVZBTVH6VXOBDCLVAALOMT5GDM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::PersoComm::Unit.Channel.Wns:Create - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.Wns:Create-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDSYGBLVXMBCT7NRC7V7Y2JPHRE"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWFGYPTR5FD4RE3U6RK6ZNPMGQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			null,
			null,

			/*Radix::PersoComm::Unit.Channel.Wns:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::PersoComm::Unit.Channel.Wns:Create - Web Meta*/

/*Radix::PersoComm::Unit.Channel.Wns:Create-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDSYGBLVXMBCT7NRC7V7Y2JPHRE"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWFGYPTR5FD4RE3U6RK6ZNPMGQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXUBRSLURTJD6FDVEFGKJMOCODQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			null,
			null,

			/*Radix::PersoComm::Unit.Channel.Wns:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::PersoComm::Unit.Channel.Wns - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.Wns - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Коммуникационный канал - Windows Push Notification Services");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Windows Push Notification Services Communication Channel");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SZPPJRFVFA2BD72G4FZO5MCQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. клиента");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client Id");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KWBYK5I3NF5XM7RIFLA4DA76A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Секретное слово");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client secret");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJKXKKNZGXBEJZHIU7LSULSZWDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Канал");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSBXL5LJXRFYVPWIIYAB5NHMXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Unit.Channel.Wns - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclXUBRSLURTJD6FDVEFGKJMOCODQ"),"Unit.Channel.Wns - Localizing Bundle",$$$items$$$);
}


/* Radix::Acs::PartitionGroup - Server Executable*/

/*Radix::Acs::PartitionGroup-Entity Class*/

package org.radixware.ads.Acs.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup")
public abstract published class PartitionGroup  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return PartitionGroup_mi.rdxMeta;}

	/*Radix::Acs::PartitionGroup:Nested classes-Nested Classes*/

	/*Radix::Acs::PartitionGroup:Properties-Properties*/

	/*Radix::Acs::PartitionGroup:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::Acs::PartitionGroup:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:classTitle")
	public published  Str getClassTitle() {

		final Types::Id id = Types::Id.Factory.loadFrom(this.classGuid);
		final org.radixware.kernel.server.meta.clazzes.RadClassDef clazz = 
		     this.getArte().getDefManager().getClassDef(id);
		if (clazz == null) {
		     return null;
		}
		return clazz.getTitle();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:classTitle")
	public published   void setClassTitle(Str val) {
		classTitle = val;
	}

	/*Radix::Acs::PartitionGroup:familySelectorPresentationId-Dynamic Property*/



	protected Str familySelectorPresentationId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:familySelectorPresentationId")
	public published  Str getFamilySelectorPresentationId() {

		return getFamilySelectorPresentationId().toString();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:familySelectorPresentationId")
	public published   void setFamilySelectorPresentationId(Str val) {
		familySelectorPresentationId = val;
	}

	/*Radix::Acs::PartitionGroup:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::Acs::PartitionGroup:partitions-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitions")
	public published  java.sql.Clob getPartitions() {
		return partitions;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitions")
	public published   void setPartitions(java.sql.Clob val) {
		partitions = val;
	}

	/*Radix::Acs::PartitionGroup:partitionsTitle-Dynamic Property*/



	protected Str partitionsTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitionsTitle")
	public published  Str getPartitionsTitle() {

		Str val="";
		if (partitions!=null) {
		    try {
		        final int len = (int)partitions.length();
		        if (len>0) {
		            val=partitions.getSubString(1, len);
		        }
		    }
		    catch (Exceptions::SQLException ex) {
		        throw new org.radixware.kernel.common.exceptions.RadixError("Unable read Radix::Acs::PartitionGroup:partitions", ex);
		    }
		}

		final org.radixware.kernel.server.types.ArrEntity<? extends org.radixware.kernel.server.types.Entity> 
		    arrEntity = org.radixware.kernel.server.types.ArrEntity.fromValAsStr(getArte(), val);
		    
		final java.lang.StringBuffer collector = new java.lang.StringBuffer();
		boolean first = true;    
		if (arrEntity!=null)
		for (org.radixware.kernel.server.types.Entity entity : arrEntity) {
		    final Types::Entity e = (Types::Entity)entity;
		    final String title;
		    if (e !=null){
		        title = e.calcTitle();
		    }
		    else {
		        title = "<not defined>";
		    }
		    if (first) {
		        first = false;
		    }
		    else {
		        collector.append("; "); 
		    }
		    collector.append(title);
		}

		return collector.toString();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitionsTitle")
	public published   void setPartitionsTitle(Str val) {
		partitionsTitle = val;
	}

	/*Radix::Acs::PartitionGroup:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:title")
	public published   void setTitle(Str val) {
		title = val;
	}







































































	/*Radix::Acs::PartitionGroup:Methods-Methods*/

	/*Radix::Acs::PartitionGroup:getFamilyId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:getFamilyId")
	public abstract published  org.radixware.kernel.common.types.Id getFamilyId ();

	/*Radix::Acs::PartitionGroup:getFamilySelectorPresentationId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:getFamilySelectorPresentationId")
	public abstract published  org.radixware.kernel.common.types.Id getFamilySelectorPresentationId ();

	/*Radix::Acs::PartitionGroup:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:beforeDelete")
	protected published  boolean beforeDelete () {
		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    if (!this.getArte().getRights().curUserHasRightsOnPartitionGroup(this.getFamilyId(), this.id.intValue())){
		        throw new InvalidEasRequestClientFault("Attempt to revoke rights that you cannot assign");
		    }
		}
		return super.beforeDelete();
	}

	/*Radix::Acs::PartitionGroup:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:beforeUpdate")
	protected published  boolean beforeUpdate () {
		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()) {
		    if (!this.getArte().getRights().curUserHasRightsOnPartitionGroup(this.getFamilyId(), this.id.intValue())){
		        throw new InvalidEasRequestClientFault( "Attempt to modify exceeding rights");
		    }
		    
		    String partitions=null;
		    try{
		        if (partitions!=null) {
		            partitions = partitions.getSubString(1, (int)partitions.length());
		        }
		    }
		    catch (Exceptions::SQLException ex) {
		        throw new org.radixware.kernel.common.exceptions.RadixError("Unable read Radix::Acs::PartitionGroup:partitions", ex);
		    }
		    if (!this.getArte().getRights().curUserHasRightsOnPartitionGroup(this.getFamilyId(), partitions)){
		        throw new InvalidEasRequestClientFault("Attempt to assign exceeding rights");
		    }    
		    
		    
		}
		return super.beforeUpdate();
	}

	/*Radix::Acs::PartitionGroup:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);
		this.getArte().getRights().compileRights();
	}

	/*Radix::Acs::PartitionGroup:afterDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:afterDelete")
	protected published  void afterDelete () {
		super.afterDelete();
		getArte().Rights.compileRights();
	}

	/*Radix::Acs::PartitionGroup:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:afterUpdate")
	protected published  void afterUpdate () {
		super.afterUpdate();
		getArte().Rights.compileRights();
	}

	/*Radix::Acs::PartitionGroup:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		//System.out.println("Radix::Acs::PartitionGroup:beforeCreate");
		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    String partitions=null;
		    try{
		        if (partitions!=null) {
		            partitions = partitions.getSubString(1, (int)partitions.length());
		        }
		    }
		    catch (Exceptions::SQLException ex) {
		        throw new org.radixware.kernel.common.exceptions.RadixError("Unable read Radix::Acs::PartitionGroup:partitions", ex);
		    }
		    if (!this.getArte().getRights().curUserHasRightsOnPartitionGroup(this.getFamilyId(), partitions)){
		        throw new InvalidEasRequestClientFault("Attempt to assign exceeding rights");
		    }
		}
		return super.beforeCreate(src);
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Acs::PartitionGroup - Server Meta*/

/*Radix::Acs::PartitionGroup-Entity Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),"PartitionGroup",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TFJIVBSRNAMDPOVFK2B2NEJUY"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Acs::PartitionGroup:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
							/*Owner Class Name*/
							"PartitionGroup",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TFJIVBSRNAMDPOVFK2B2NEJUY"),
							/*Property presentations*/

							/*Radix::Acs::PartitionGroup:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Acs::PartitionGroup:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4HBYVGTHNJBCXNSRBE6RZDF2WU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::PartitionGroup:familySelectorPresentationId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMVROKB2G3ZGTRMY5373GMWDNNY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::PartitionGroup:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::PartitionGroup:partitions:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::PartitionGroup:partitionsTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGYG2I7DBANDTNODUJLMTUEM73Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::PartitionGroup:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::Acs::PartitionGroup:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Acs::PartitionGroup:General:Children-Explorer Items*/
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
									/*Radix::Acs::PartitionGroup:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprERNWXLTKHZACZMQWIDSAZPVCPY"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGYG2I7DBANDTNODUJLMTUEM73Q"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4HBYVGTHNJBCXNSRBE6RZDF2WU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("eccYCNCT2DEA5HNVJIPJ7IS55QHUM")),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Acs::PartitionGroup:UserGroups-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprU4ITQMDYM5GKJPXE3OKFNOQFGY"),"UserGroups",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E"),16632,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecLNMBHO73GNE2LF5TXFR7QEMICM\" PropId=\"col4LTOC32FKFEO5MREJS6HD7I2MQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"acl26XYRH2LI5ADTP4JPLQ2ZWMCGE\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},null,null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Acs::PartitionGroup:DashConfig-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTPTX2VY26VCE5MFPXEA3KOE6GQ"),"DashConfig",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E"),16632,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecLNMBHO73GNE2LF5TXFR7QEMICM\" PropId=\"col4LTOC32FKFEO5MREJS6HD7I2MQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclBWXLLGQQX5FKZJVZZXIOHIXPUQ\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},null,null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Acs::PartitionGroup:ReadOnly-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E"),"ReadOnly",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(281711,null,null,null),null)
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Acs::PartitionGroup:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Acs::PartitionGroup:General-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccYCNCT2DEA5HNVJIPJ7IS55QHUM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
									}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Acs::PartitionGroup:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Acs::PartitionGroup:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4LTOC32FKFEO5MREJS6HD7I2MQ"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::PartitionGroup:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4HBYVGTHNJBCXNSRBE6RZDF2WU"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMBPNG4YB6ZFVREXBV44BNHY4IM"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::PartitionGroup:familySelectorPresentationId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMVROKB2G3ZGTRMY5373GMWDNNY"),"familySelectorPresentationId",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::PartitionGroup:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPDVCVPJTRBHTBERWOTB4GA5RIU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::PartitionGroup:partitions-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),"partitions",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3J2IUTRDRE6BMKRWCBONYNNHU"),org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::PartitionGroup:partitionsTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGYG2I7DBANDTNODUJLMTUEM73Q"),"partitionsTitle",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::PartitionGroup:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPUXQ6CWBFAGNGW64DZXK7FRAU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Acs::PartitionGroup:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVD22TSAPWBFRXA7RKUIXAFWBJY"),"getFamilyId",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXWHHTDS7H5GNNMBX22SAGYK54U"),"getFamilySelectorPresentationId",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFTOIEX6SNZAF5BXY64PYML3Z44"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UCNHZSLQVGXPG7Z4SJQJPPSAY"),"afterDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W3TRDKMDRCGFJYNWSP72FQKSE"),"afterUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQNEWVZGAYZCLPCM2SFKMAWOBJQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::Acs::PartitionGroup - Desktop Executable*/

/*Radix::Acs::PartitionGroup-Entity Class*/

package org.radixware.ads.Acs.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup")
public interface PartitionGroup {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Acs.explorer.PartitionGroup.PartitionGroup_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.explorer.PartitionGroup.PartitionGroup_DefaultModel )  super.getEntity(i);}
	}




















































	/*Radix::Acs::PartitionGroup:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Acs::PartitionGroup:partitions:partitions-Presentation Property*/


	public class Partitions extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public Partitions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitions:partitions")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitions:partitions")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Partitions getPartitions();
	/*Radix::Acs::PartitionGroup:title:title-Presentation Property*/


	public class Title extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Title(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Acs::PartitionGroup:classTitle:classTitle-Presentation Property*/


	public class ClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::Acs::PartitionGroup:partitionsTitle:partitionsTitle-Presentation Property*/


	public class PartitionsTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PartitionsTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitionsTitle:partitionsTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitionsTitle:partitionsTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PartitionsTitle getPartitionsTitle();
	/*Radix::Acs::PartitionGroup:familySelectorPresentationId:familySelectorPresentationId-Presentation Property*/


	public class FamilySelectorPresentationId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FamilySelectorPresentationId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:familySelectorPresentationId:familySelectorPresentationId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:familySelectorPresentationId:familySelectorPresentationId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FamilySelectorPresentationId getFamilySelectorPresentationId();


}

/* Radix::Acs::PartitionGroup - Desktop Meta*/

/*Radix::Acs::PartitionGroup-Entity Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::PartitionGroup:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
			"Radix::Acs::PartitionGroup",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img7BYET3YUHNAJJCJBD3EBVVVFH4"),
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TFJIVBSRNAMDPOVFK2B2NEJUY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKAKKQE4PB5F2PFWYNGWNTOUFSA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TFJIVBSRNAMDPOVFK2B2NEJUY"),0,

			/*Radix::Acs::PartitionGroup:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::PartitionGroup:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4HBYVGTHNJBCXNSRBE6RZDF2WU"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMBPNG4YB6ZFVREXBV44BNHY4IM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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

						/*Radix::Acs::PartitionGroup:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:familySelectorPresentationId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMVROKB2G3ZGTRMY5373GMWDNNY"),
						"familySelectorPresentationId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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

						/*Radix::Acs::PartitionGroup:familySelectorPresentationId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPDVCVPJTRBHTBERWOTB4GA5RIU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::PartitionGroup:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:partitions:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),
						"partitions",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3J2IUTRDRE6BMKRWCBONYNNHU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::PartitionGroup:partitions:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:partitionsTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGYG2I7DBANDTNODUJLMTUEM73Q"),
						"partitionsTitle",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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

						/*Radix::Acs::PartitionGroup:partitionsTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPUXQ6CWBFAGNGW64DZXK7FRAU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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

						/*Radix::Acs::PartitionGroup:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprERNWXLTKHZACZMQWIDSAZPVCPY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprU4ITQMDYM5GKJPXE3OKFNOQFGY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTPTX2VY26VCE5MFPXEA3KOE6GQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E")},
			true,false,false);
}

/* Radix::Acs::PartitionGroup - Web Executable*/

/*Radix::Acs::PartitionGroup-Entity Class*/

package org.radixware.ads.Acs.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup")
public interface PartitionGroup {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Acs.web.PartitionGroup.PartitionGroup_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.web.PartitionGroup.PartitionGroup_DefaultModel )  super.getEntity(i);}
	}




















































	/*Radix::Acs::PartitionGroup:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Acs::PartitionGroup:partitions:partitions-Presentation Property*/


	public class Partitions extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public Partitions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitions:partitions")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitions:partitions")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Partitions getPartitions();
	/*Radix::Acs::PartitionGroup:title:title-Presentation Property*/


	public class Title extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Title(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Acs::PartitionGroup:classTitle:classTitle-Presentation Property*/


	public class ClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::Acs::PartitionGroup:partitionsTitle:partitionsTitle-Presentation Property*/


	public class PartitionsTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PartitionsTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitionsTitle:partitionsTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:partitionsTitle:partitionsTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PartitionsTitle getPartitionsTitle();
	/*Radix::Acs::PartitionGroup:familySelectorPresentationId:familySelectorPresentationId-Presentation Property*/


	public class FamilySelectorPresentationId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FamilySelectorPresentationId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:familySelectorPresentationId:familySelectorPresentationId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:familySelectorPresentationId:familySelectorPresentationId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FamilySelectorPresentationId getFamilySelectorPresentationId();


}

/* Radix::Acs::PartitionGroup - Web Meta*/

/*Radix::Acs::PartitionGroup-Entity Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::PartitionGroup:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
			"Radix::Acs::PartitionGroup",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img7BYET3YUHNAJJCJBD3EBVVVFH4"),
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TFJIVBSRNAMDPOVFK2B2NEJUY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKAKKQE4PB5F2PFWYNGWNTOUFSA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TFJIVBSRNAMDPOVFK2B2NEJUY"),0,

			/*Radix::Acs::PartitionGroup:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::PartitionGroup:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4HBYVGTHNJBCXNSRBE6RZDF2WU"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMBPNG4YB6ZFVREXBV44BNHY4IM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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

						/*Radix::Acs::PartitionGroup:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:familySelectorPresentationId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMVROKB2G3ZGTRMY5373GMWDNNY"),
						"familySelectorPresentationId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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

						/*Radix::Acs::PartitionGroup:familySelectorPresentationId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPDVCVPJTRBHTBERWOTB4GA5RIU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::PartitionGroup:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:partitions:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),
						"partitions",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3J2IUTRDRE6BMKRWCBONYNNHU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::PartitionGroup:partitions:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:partitionsTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGYG2I7DBANDTNODUJLMTUEM73Q"),
						"partitionsTitle",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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

						/*Radix::Acs::PartitionGroup:partitionsTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::PartitionGroup:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPUXQ6CWBFAGNGW64DZXK7FRAU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
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

						/*Radix::Acs::PartitionGroup:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprERNWXLTKHZACZMQWIDSAZPVCPY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprU4ITQMDYM5GKJPXE3OKFNOQFGY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTPTX2VY26VCE5MFPXEA3KOE6GQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E")},
			true,false,false);
}

/* Radix::Acs::PartitionGroup:General - Desktop Meta*/

/*Radix::Acs::PartitionGroup:General-Editor Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
	null,
	null,

	/*Radix::Acs::PartitionGroup:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::PartitionGroup:General:main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgWUBRNXNYQVAXRMSSGRXITZ6R4Q"),"main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4HBYVGTHNJBCXNSRBE6RZDF2WU"),0,2,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgWUBRNXNYQVAXRMSSGRXITZ6R4Q"))}
	,

	/*Radix::Acs::PartitionGroup:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Acs::PartitionGroup:General - Web Meta*/

/*Radix::Acs::PartitionGroup:General-Editor Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
	null,
	null,

	/*Radix::Acs::PartitionGroup:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::PartitionGroup:General:main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgWUBRNXNYQVAXRMSSGRXITZ6R4Q"),"main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4HBYVGTHNJBCXNSRBE6RZDF2WU"),0,2,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgWUBRNXNYQVAXRMSSGRXITZ6R4Q"))}
	,

	/*Radix::Acs::PartitionGroup:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Acs::PartitionGroup:General:Model - Desktop Executable*/

/*Radix::Acs::PartitionGroup:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.PartitionGroup.PartitionGroup_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::PartitionGroup:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::PartitionGroup:General:Model:Properties-Properties*/

	/*Radix::Acs::PartitionGroup:General:Model:partitions-Presentation Property*/




	public class Partitions extends org.radixware.ads.Acs.explorer.PartitionGroup.colIDPIK7BQQREGBLDSYFORHOMRRQ{
		public Partitions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::Acs::PartitionGroup:General:Model:partitions:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::PartitionGroup:General:Model:partitions:Nested classes-Nested Classes*/

		/*Radix::Acs::PartitionGroup:General:Model:partitions:Properties-Properties*/

		/*Radix::Acs::PartitionGroup:General:Model:partitions:Methods-Methods*/

		/*Radix::Acs::PartitionGroup:General:Model:partitions:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitions:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			final Types::Id selectorPresentationId = Types::Id.Factory.loadFrom(familySelectorPresentationId.Value);
			final Explorer.EditMask::EditMaskRef editMask = new org.radixware.kernel.common.client.meta.mask.EditMaskRef(selectorPresentationId);
			return new ProxyPropEditor(this, Meta::ValType:ArrRef, editMask, Client.Types::EReferenceStringFormat.OBJECT_PID_WITH_TABLE_ID)
			{   
			    @Override
			    protected void updateEditor(final Object value, final org.radixware.kernel.common.client.editors.property.PropEditorOptions options){
			        super.updateEditor(value, options);        
			        final Explorer.ValEditors::ValEditor valEditor = getValEditor();
			        if (valEditor instanceof Explorer.ValEditors::ValArrEditor){
			            ((Explorer.ValEditors::ValArrEditor)valEditor).setDuplicatesEnabled(false);
			        }        
			    } 
			}
			;


		}

		/*Radix::Acs::PartitionGroup:General:Model:partitions:getValueAsString-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitions:getValueAsString")
		public published  Str getValueAsString () {
			final Str notDefined = super.getValueAsString();
			final boolean empty = org.radixware.kernel.common.utils.Utils.emptyOrNull(this.Value);
			return empty ? notDefined : partitionsTitle.getValue();
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitions")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitions")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Partitions getPartitions(){return (Partitions)getProperty(colIDPIK7BQQREGBLDSYFORHOMRRQ);}

	/*Radix::Acs::PartitionGroup:General:Model:partitionsTitle-Presentation Property*/




	public class PartitionsTitle extends org.radixware.ads.Acs.explorer.PartitionGroup.prdGYG2I7DBANDTNODUJLMTUEM73Q{
		public PartitionsTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitionsTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitionsTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PartitionsTitle getPartitionsTitle(){return (PartitionsTitle)getProperty(prdGYG2I7DBANDTNODUJLMTUEM73Q);}

	/*Radix::Acs::PartitionGroup:General:Model:familySelectorPresentationId-Presentation Property*/




	public class FamilySelectorPresentationId extends org.radixware.ads.Acs.explorer.PartitionGroup.prdMVROKB2G3ZGTRMY5373GMWDNNY{
		public FamilySelectorPresentationId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:familySelectorPresentationId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:familySelectorPresentationId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FamilySelectorPresentationId getFamilySelectorPresentationId(){return (FamilySelectorPresentationId)getProperty(prdMVROKB2G3ZGTRMY5373GMWDNNY);}












	/*Radix::Acs::PartitionGroup:General:Model:Methods-Methods*/


}

/* Radix::Acs::PartitionGroup:General:Model - Desktop Meta*/

/*Radix::Acs::PartitionGroup:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFXIYZC5YZRCXJNWPIUH2IHSHBI"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::PartitionGroup:General:Model:Properties-Properties*/
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

/* Radix::Acs::PartitionGroup:General:Model - Web Executable*/

/*Radix::Acs::PartitionGroup:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.PartitionGroup.PartitionGroup_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::PartitionGroup:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::PartitionGroup:General:Model:Properties-Properties*/

	/*Radix::Acs::PartitionGroup:General:Model:partitions-Presentation Property*/




	public class Partitions extends org.radixware.ads.Acs.web.PartitionGroup.colIDPIK7BQQREGBLDSYFORHOMRRQ{
		public Partitions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::Acs::PartitionGroup:General:Model:partitions:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::PartitionGroup:General:Model:partitions:Nested classes-Nested Classes*/

		/*Radix::Acs::PartitionGroup:General:Model:partitions:Properties-Properties*/

		/*Radix::Acs::PartitionGroup:General:Model:partitions:Methods-Methods*/

		/*Radix::Acs::PartitionGroup:General:Model:partitions:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitions:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			final Types::Id selectorPresentationId = Types::Id.Factory.loadFrom(familySelectorPresentationId.Value);
			final Explorer.EditMask::EditMaskRef editMask = new org.radixware.kernel.common.client.meta.mask.EditMaskRef(selectorPresentationId);
			return new ProxyPropEditor(this, Meta::ValType:ArrRef, editMask, Client.Types::EReferenceStringFormat.OBJECT_PID_WITH_TABLE_ID)
			{   
			    @Override
			    protected void updateEditor(final Object value,  final Object initialValue, final org.radixware.kernel.common.client.editors.property.PropEditorOptions options){
			        super.updateEditor(value, initialValue, options);        
			        final Web.Widgets::ValueEditor valEditor = getValEditor();
			        if (valEditor instanceof Web.Widgets::ValArrEditorController){
			            ((Web.Widgets::ValArrEditorController)valEditor).setDuplicatesEnabled(false);
			        }        
			    } 
			};

		}

		/*Radix::Acs::PartitionGroup:General:Model:partitions:getValueAsString-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitions:getValueAsString")
		public published  Str getValueAsString () {
			final Str notDefined = super.getValueAsString();
			final boolean empty = org.radixware.kernel.common.utils.Utils.emptyOrNull(this.Value);
			return empty ? notDefined : partitionsTitle.getValue();
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitions")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitions")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Partitions getPartitions(){return (Partitions)getProperty(colIDPIK7BQQREGBLDSYFORHOMRRQ);}

	/*Radix::Acs::PartitionGroup:General:Model:partitionsTitle-Presentation Property*/




	public class PartitionsTitle extends org.radixware.ads.Acs.web.PartitionGroup.prdGYG2I7DBANDTNODUJLMTUEM73Q{
		public PartitionsTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitionsTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:partitionsTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PartitionsTitle getPartitionsTitle(){return (PartitionsTitle)getProperty(prdGYG2I7DBANDTNODUJLMTUEM73Q);}

	/*Radix::Acs::PartitionGroup:General:Model:familySelectorPresentationId-Presentation Property*/




	public class FamilySelectorPresentationId extends org.radixware.ads.Acs.web.PartitionGroup.prdMVROKB2G3ZGTRMY5373GMWDNNY{
		public FamilySelectorPresentationId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:familySelectorPresentationId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::PartitionGroup:General:Model:familySelectorPresentationId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FamilySelectorPresentationId getFamilySelectorPresentationId(){return (FamilySelectorPresentationId)getProperty(prdMVROKB2G3ZGTRMY5373GMWDNNY);}












	/*Radix::Acs::PartitionGroup:General:Model:Methods-Methods*/


}

/* Radix::Acs::PartitionGroup:General:Model - Web Meta*/

/*Radix::Acs::PartitionGroup:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFXIYZC5YZRCXJNWPIUH2IHSHBI"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::PartitionGroup:General:Model:Properties-Properties*/
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

/* Radix::Acs::PartitionGroup:General - Desktop Meta*/

/*Radix::Acs::PartitionGroup:General-Selector Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprERNWXLTKHZACZMQWIDSAZPVCPY"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
		null,
		null,
		null,
		null,
		true,
		null,
		null,
		false,
		true,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGYG2I7DBANDTNODUJLMTUEM73Q"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4HBYVGTHNJBCXNSRBE6RZDF2WU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.explorer.PartitionGroup.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Acs::PartitionGroup:General - Web Meta*/

/*Radix::Acs::PartitionGroup:General-Selector Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprERNWXLTKHZACZMQWIDSAZPVCPY"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
		null,
		null,
		null,
		null,
		true,
		null,
		null,
		false,
		true,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGYG2I7DBANDTNODUJLMTUEM73Q"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4HBYVGTHNJBCXNSRBE6RZDF2WU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.web.PartitionGroup.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Acs::PartitionGroup:UserGroups - Desktop Meta*/

/*Radix::Acs::PartitionGroup:UserGroups-Selector Presentation*/

package org.radixware.ads.Acs.explorer;
public final class UserGroups_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new UserGroups_mi();
	private UserGroups_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprU4ITQMDYM5GKJPXE3OKFNOQFGY"),
		"UserGroups",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
		null,
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16632,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		false,true,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.explorer.PartitionGroup.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Acs::PartitionGroup:UserGroups - Web Meta*/

/*Radix::Acs::PartitionGroup:UserGroups-Selector Presentation*/

package org.radixware.ads.Acs.web;
public final class UserGroups_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new UserGroups_mi();
	private UserGroups_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprU4ITQMDYM5GKJPXE3OKFNOQFGY"),
		"UserGroups",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
		null,
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16632,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		false,true,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.web.PartitionGroup.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Acs::PartitionGroup:DashConfig - Desktop Meta*/

/*Radix::Acs::PartitionGroup:DashConfig-Selector Presentation*/

package org.radixware.ads.Acs.explorer;
public final class DashConfig_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new DashConfig_mi();
	private DashConfig_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTPTX2VY26VCE5MFPXEA3KOE6GQ"),
		"DashConfig",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
		null,
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16632,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		false,true,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.explorer.PartitionGroup.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Acs::PartitionGroup:DashConfig - Web Meta*/

/*Radix::Acs::PartitionGroup:DashConfig-Selector Presentation*/

package org.radixware.ads.Acs.web;
public final class DashConfig_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new DashConfig_mi();
	private DashConfig_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTPTX2VY26VCE5MFPXEA3KOE6GQ"),
		"DashConfig",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
		null,
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16632,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		false,true,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.web.PartitionGroup.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Acs::PartitionGroup:ReadOnly - Desktop Meta*/

/*Radix::Acs::PartitionGroup:ReadOnly-Selector Presentation*/

package org.radixware.ads.Acs.explorer;
public final class ReadOnly_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ReadOnly_mi();
	private ReadOnly_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E"),
		"ReadOnly",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
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
		281711,
		null,
		144,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.explorer.PartitionGroup.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Acs::PartitionGroup:ReadOnly - Web Meta*/

/*Radix::Acs::PartitionGroup:ReadOnly-Selector Presentation*/

package org.radixware.ads.Acs.web;
public final class ReadOnly_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ReadOnly_mi();
	private ReadOnly_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprV2SFEA2IYJEJJEGSGKBZYV2U6E"),
		"ReadOnly",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
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
		281711,
		null,
		144,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFXIYZC5YZRCXJNWPIUH2IHSHBI")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWFLWOFUZMJG4HJ6TOPSJAERLLU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Acs.web.PartitionGroup.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Acs::PartitionGroup - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PartitionGroup - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AASCMDPMJEKRPYTEH7E6CTTAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Partition Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа разделов ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TFJIVBSRNAMDPOVFK2B2NEJUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to assign exceeding rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка дать права, которыми пользователь не обладает");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCVZJPTJEWNDGZH5KS2J3FSMNTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Access partitions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разделы доступа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3J2IUTRDRE6BMKRWCBONYNNHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to revoke rights that you cannot assign");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка забрать права, которые вы не сможете назначить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJIDZC57KBEYVKQUVSU7L6ANWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Partition Groups");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группы разделов ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKAKKQE4PB5F2PFWYNGWNTOUFSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Title");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPUXQ6CWBFAGNGW64DZXK7FRAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMBPNG4YB6ZFVREXBV44BNHY4IM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Partition group ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. группы разделов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPDVCVPJTRBHTBERWOTB4GA5RIU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to modify exceeding rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка изменить права, которыми пользователь не обладает");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQBLV2T46VJD2XO7ALXIP5FW4RU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to assign exceeding rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка дать права, которыми пользователь не обладает");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUJ4KR47V2NH47KRYWOHQKYO5CY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(PartitionGroup - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecLNMBHO73GNE2LF5TXFR7QEMICM"),"PartitionGroup - Localizing Bundle",$$$items$$$);
}

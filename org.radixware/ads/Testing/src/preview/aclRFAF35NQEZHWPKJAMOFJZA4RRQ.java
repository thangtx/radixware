
/* Radix::Testing::TestCase.Group - Server Executable*/

/*Radix::Testing::TestCase.Group-Application Class*/

package org.radixware.ads.Testing.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group")
public published class TestCase.Group  extends org.radixware.ads.Testing.server.TestCase.AbstractGroup  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return TestCase.Group_mi.rdxMeta;}

	/*Radix::Testing::TestCase.Group:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCase.Group:Properties-Properties*/

	/*Radix::Testing::TestCase.Group:isGroup-Dynamic Property*/




	@Override
	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:isGroup")
	public published  Bool getIsGroup() {

		return true;
	}

	/*Radix::Testing::TestCase.Group:hasChildTestsGroup-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:hasChildTestsGroup")
	private final  Bool getHasChildTestsGroup() {
		return hasChildTestsGroup;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:hasChildTestsGroup")
	private final   void setHasChildTestsGroup(Bool val) {
		hasChildTestsGroup = val;
	}

	/*Radix::Testing::TestCase.Group:stopAfterTestFail-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:stopAfterTestFail")
	public published  Bool getStopAfterTestFail() {
		return stopAfterTestFail;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:stopAfterTestFail")
	public published   void setStopAfterTestFail(Bool val) {
		stopAfterTestFail = val;
	}

	/*Radix::Testing::TestCase.Group:groupTypeRef-Dynamic Property*/



	protected org.radixware.ads.Testing.server.TestCase.Group groupTypeRef=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:groupTypeRef")
	private final  org.radixware.ads.Testing.server.TestCase.Group getGroupTypeRef() {

		return (TestCase.Group) groupRef;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:groupTypeRef")
	private final   void setGroupTypeRef(org.radixware.ads.Testing.server.TestCase.Group val) {
		groupTypeRef = val;
	}

	/*Radix::Testing::TestCase.Group:isRunFailed-Dynamic Property*/



	protected Bool isRunFailed=(Bool)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("0",org.radixware.kernel.common.enums.EValType.BOOL);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:isRunFailed")
	private final  Bool getIsRunFailed() {
		return isRunFailed;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:isRunFailed")
	private final   void setIsRunFailed(Bool val) {
		isRunFailed = val;
	}



























































	/*Radix::Testing::TestCase.Group:Methods-Methods*/

	/*Radix::Testing::TestCase.Group:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:execute")
	public published  org.radixware.kernel.common.enums.EEventSeverity execute () {
		return super.execute();
	}

	/*Radix::Testing::TestCase.Group:getChildsCountFromDb-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:getChildsCountFromDb")
	public published  int getChildsCountFromDb () {
		ChildTestCountCursor countCursor = ChildTestCountCursor.open(id);
		try {
		    if (countCursor.next() && countCursor.count != null) {
		        return countCursor.count.intValue();
		    }
		} finally {
		    countCursor.close();
		}
		return 0;
	}

	/*Radix::Testing::TestCase.Group:hasChildTests-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:hasChildTests")
	  boolean hasChildTests () {
		return super.hasChildTests() || (hasChildTestsGroup != null ? hasChildTestsGroup.booleanValue() : false);
	}

	/*Radix::Testing::TestCase.Group:onCommand_RerunFailed-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:onCommand_RerunFailed")
	public  void onCommand_RerunFailed (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		setRunFailed(true);
		run();
		setRunFailed(false);
	}

	/*Radix::Testing::TestCase.Group:getChildren-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:getChildren")
	public published  java.util.List<Int> getChildren () {
		java.util.List<Int> childIds = new java.util.ArrayList<>();

		GroupTestCasesCursor childrenCursor = GroupTestCasesCursor.open(id);
		try {
		    while (childrenCursor.next()) {
		        if (isRunFailed.booleanValue() && childrenCursor.childResult.longValue() < Arte::EventSeverity:Warning.Value.longValue()) {
		            continue;
		        }
		        
		        childIds.add(childrenCursor.childId);
		        collectTestCaseChildren(childrenCursor.childId, childIds);
		    }
		} catch (Exceptions::DatabaseError ex) {
		    Arte::Trace.error(Arte::Trace.exceptionStackToString(ex), Arte::EventSource:TestCase);
		    return new java.util.ArrayList<>();
		} finally {
		    if (childrenCursor != null) {
		        childrenCursor.close();
		    }
		}

		return childIds;
	}

	/*Radix::Testing::TestCase.Group:isStopAfterTestFail-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:isStopAfterTestFail")
	protected published  boolean isStopAfterTestFail () {
		return stopAfterTestFail.booleanValue();
	}

	/*Radix::Testing::TestCase.Group:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);

		if (src instanceof TestCase.Group) {
		    TestCase.Group srcTestGroup = (TestCase.Group) src;
		    int seq = 1;
		    GroupTestCasesCursor c = GroupTestCasesCursor.open(srcTestGroup.id);
		    try {
		        while (c.next()) {
		            TestCase childCopy = (TestCase) Arte::Arte.getInstance().newObject(Types::Id.Factory.loadFrom(c.child.classGuid));
		            childCopy.init(null, c.child);
		            childCopy.groupId = id;
		            childCopy.seq = seq;
		            childCopy.create(c.child);
		            seq++;
		        }
		    } finally {
		        c.close();
		    }
		}
	}

	/*Radix::Testing::TestCase.Group:exportThis-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:exportThis")
	protected  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data, boolean exportChilds, boolean isRoot) {
		super.exportThis(data, exportChilds, isRoot);
		data.itemClassId = idof[CfgItem.GroupOfTestCases];
	}

	/*Radix::Testing::TestCase.Group:getTraceLevelForAasCall-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:getTraceLevelForAasCall")
	protected published  org.radixware.kernel.common.enums.EEventSeverity getTraceLevelForAasCall () {
		return Arte::EventSeverity:None;
	}

	/*Radix::Testing::TestCase.Group:setRunFailed-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:setRunFailed")
	private final  void setRunFailed (boolean value) {
		isRunFailed = value;
		for (Int id : getChildren()) {
		    TestCase test = TestCase.loadByPK(id, false);
		    if (test.isGroup.booleanValue()) {
		        ((TestCase.Group) test).setRunFailed(value);
		    }
		}
	}

	/*Radix::Testing::TestCase.Group:collectTestCaseChildren-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:collectTestCaseChildren")
	private final  void collectTestCaseChildren (Int testCaseId, java.util.List<Int> childIds) {
		ChildTestCasesCursor cursor = ChildTestCasesCursor.open(testCaseId);
		try {
		    while (cursor.next()) {
		        if (isRunFailed.booleanValue() && cursor.childResult.longValue() < Arte::EventSeverity:Warning.Value.longValue()) {
		            continue;
		        }
		        
		        childIds.add(cursor.childId);
		        collectTestCaseChildren(cursor.childId, childIds);
		    }
		} finally {
		    cursor.close();
		}
	}



	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdHVIQZGPGHJAMBO6NAICEGXZVBA){
			onCommand_RerunFailed(newPropValsById);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Testing::TestCase.Group - Server Meta*/

/*Radix::Testing::TestCase.Group-Application Class*/

package org.radixware.ads.Testing.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.Group_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),"TestCase.Group",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZ57E3AKKRGNDASN5LTVVJK5PI"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Testing::TestCase.Group:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
							/*Owner Class Name*/
							"TestCase.Group",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZ57E3AKKRGNDASN5LTVVJK5PI"),
							/*Property presentations*/

							/*Radix::Testing::TestCase.Group:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Testing::TestCase.Group:isGroup:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::Testing::TestCase.Group:hasChildTestsGroup:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OWXMIK2WZEB3H7MNE2VRM5TOI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase.Group:stopAfterTestFail:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIYTC6UKMOZHJTCUJWL4DKR3BME"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Testing::TestCase.Group:groupTypeRef:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVMGI6FYCIRGMTOKEZ5ZDZ7BV4E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Testing::TestCase.Group:Rerun-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHGOOKC3J3DNRDISQAAAAAAAAAA"),"Rerun",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Testing::TestCase.Group:RerunFailed-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHVIQZGPGHJAMBO6NAICEGXZVBA"),"RerunFailed",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Testing::TestCase.Group:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr35DU4J2H6FE63IB4P25Q6UZRGU"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA"),
									36016,
									null,

									/*Radix::Testing::TestCase.Group:General:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr35DU4J2H6FE63IB4P25Q6UZRGU")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Testing::TestCase.Group:All-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKGGFFVPJAHOBDA26AAMPGXSZKU"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),null,1451.875,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),

						/*Radix::Testing::TestCase.Group:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Testing::TestCase.Group:isGroup-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),"isGroup",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.Group:hasChildTestsGroup-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OWXMIK2WZEB3H7MNE2VRM5TOI"),"hasChildTestsGroup",null,org.radixware.kernel.common.enums.EValType.BOOL,"NUMBER(1,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>select count(*) from dual where exists (select id from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"col3AN3AUHYHNHNFBQ3J4PRDXRRXE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHW4OSVMS27NRDISQAAAAAAAAAA\" PropId=\"colN3UZ2Z4S27NRDISQAAAAAAAAAA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) </xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.Group:stopAfterTestFail-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIYTC6UKMOZHJTCUJWL4DKR3BME"),"stopAfterTestFail",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3XPRDERJTNEBVO74YFTMK5445M"),org.radixware.kernel.common.enums.EValType.BOOL,null,true,null,new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVMGI6FYCIRGMTOKEZ5ZDZ7BV4E")},org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIYTC6UKMOZHJTCUJWL4DKR3BME"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.Group:groupTypeRef-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVMGI6FYCIRGMTOKEZ5ZDZ7BV4E"),"groupTypeRef",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZ57E3AKKRGNDASN5LTVVJK5PI"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Testing::TestCase.Group:isRunFailed-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5CLWD7IFAJGELON6BX7CSMWR4U"),"isRunFailed",null,org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Testing::TestCase.Group:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2ORZGNH27NRDISQAAAAAAAAAA"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI3LJFAYHBFCD3KGUAZJS2PB56Y"),"getChildsCountFromDb",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNPFMHF35MFCMBKEGMWEPTZ6PNA"),"hasChildTests",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdHVIQZGPGHJAMBO6NAICEGXZVBA"),"onCommand_RerunFailed",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7MBDMYA63ZCL3MZKAGBI5O5ROU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMKGKB4FK5BDC7L527USXI3KNNA"),"getChildren",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG6UZCOPXGVD4TAWLASVY3K74T4"),"isStopAfterTestFail",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6DSVUFUIRRCHXJOFYAEEYDKVLQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthL37P4BEB7BG5DJXDQC2PLDBAPQ"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJPCNTJ3E2FF63OXVKJKFXYMKY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportChilds",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6ZD7MHWNAFFORNKERJUQWRQFHY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isRoot",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ5SVZBOA6VCFBHFXOK4UVYKFVA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4JG27OGWZRERLNHYAE247CKPCU"),"getTraceLevelForAasCall",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNRYLXITBT5CBHGOUFUIMLA6OHU"),"setRunFailed",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("value",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOKUR6PXSAVHMXCJYIKJG3JDBWU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3YQIN75POREXJBXXBJFYRUJ6K4"),"collectTestCaseChildren",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testCaseId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXADGVMLXA5GNJFDOV7GJ6TNCYA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("childIds",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXAQHDZ7XBBHPTP6VLOXBHDG6O4"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::Testing::TestCase.Group - Desktop Executable*/

/*Radix::Testing::TestCase.Group-Application Class*/

package org.radixware.ads.Testing.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group")
public interface TestCase.Group   extends org.radixware.ads.Testing.explorer.TestCase.AbstractGroup  {





























































































	/*Radix::Testing::TestCase.Group:hasChildTestsGroup:hasChildTestsGroup-Presentation Property*/


	public class HasChildTestsGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public HasChildTestsGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:hasChildTestsGroup:hasChildTestsGroup")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:hasChildTestsGroup:hasChildTestsGroup")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public HasChildTestsGroup getHasChildTestsGroup();
	/*Radix::Testing::TestCase.Group:groupTypeRef:groupTypeRef-Presentation Property*/


	public class GroupTypeRef extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public GroupTypeRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Testing.explorer.TestCase.TestCase_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Testing.explorer.TestCase.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:groupTypeRef:groupTypeRef")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:groupTypeRef:groupTypeRef")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public GroupTypeRef getGroupTypeRef();
	/*Radix::Testing::TestCase.Group:stopAfterTestFail:stopAfterTestFail-Presentation Property*/


	public class StopAfterTestFail extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public StopAfterTestFail(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:stopAfterTestFail:stopAfterTestFail")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:stopAfterTestFail:stopAfterTestFail")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public StopAfterTestFail getStopAfterTestFail();
	public static class Rerun extends org.radixware.kernel.common.client.models.items.Command{
		protected Rerun(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class RerunFailed extends org.radixware.kernel.common.client.models.items.Command{
		protected RerunFailed(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::Testing::TestCase.Group - Desktop Meta*/

/*Radix::Testing::TestCase.Group-Application Class*/

package org.radixware.ads.Testing.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.Group_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Testing::TestCase.Group:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
			"Radix::Testing::TestCase.Group",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZ57E3AKKRGNDASN5LTVVJK5PI"),null,null,0,

			/*Radix::Testing::TestCase.Group:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Testing::TestCase.Group:isGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),
						"isGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						63,
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

						/*Radix::Testing::TestCase.Group:isGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.Group:hasChildTestsGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OWXMIK2WZEB3H7MNE2VRM5TOI"),
						"hasChildTestsGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
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

						/*Radix::Testing::TestCase.Group:hasChildTestsGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.Group:stopAfterTestFail:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIYTC6UKMOZHJTCUJWL4DKR3BME"),
						"stopAfterTestFail",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3XPRDERJTNEBVO74YFTMK5445M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						true,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase.Group:stopAfterTestFail:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.Group:groupTypeRef:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVMGI6FYCIRGMTOKEZ5ZDZ7BV4E"),
						"groupTypeRef",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						null,
						null,
						null,
						133693439,
						133693439,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase.Group:Rerun-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHGOOKC3J3DNRDISQAAAAAAAAAA"),
						"Rerun",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWRGR5ELCEZERPLD2AFC7KT4TMY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWZZPS3ZBBNFVNHFNSKTITBMHQE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase.Group:RerunFailed-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHVIQZGPGHJAMBO6NAICEGXZVBA"),
						"RerunFailed",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5WG3UPXIREGNESRBQMF4PYHCA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img732FB6LFUVBQJNABPIOBBEZPHI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr35DU4J2H6FE63IB4P25Q6UZRGU")},
			true,false,false);
}

/* Radix::Testing::TestCase.Group - Web Executable*/

/*Radix::Testing::TestCase.Group-Application Class*/

package org.radixware.ads.Testing.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group")
public interface TestCase.Group   extends org.radixware.ads.Testing.web.TestCase.AbstractGroup  {












































	/*Radix::Testing::TestCase.Group:hasChildTestsGroup:hasChildTestsGroup-Presentation Property*/


	public class HasChildTestsGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public HasChildTestsGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:hasChildTestsGroup:hasChildTestsGroup")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:hasChildTestsGroup:hasChildTestsGroup")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public HasChildTestsGroup getHasChildTestsGroup();
	/*Radix::Testing::TestCase.Group:groupTypeRef:groupTypeRef-Presentation Property*/


	public class GroupTypeRef extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public GroupTypeRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Testing.web.TestCase.TestCase_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Testing.web.TestCase.TestCase_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Testing.web.TestCase.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Testing.web.TestCase.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:groupTypeRef:groupTypeRef")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:groupTypeRef:groupTypeRef")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public GroupTypeRef getGroupTypeRef();
	/*Radix::Testing::TestCase.Group:stopAfterTestFail:stopAfterTestFail-Presentation Property*/


	public class StopAfterTestFail extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public StopAfterTestFail(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:stopAfterTestFail:stopAfterTestFail")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:stopAfterTestFail:stopAfterTestFail")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public StopAfterTestFail getStopAfterTestFail();
	public static class Rerun extends org.radixware.kernel.common.client.models.items.Command{
		protected Rerun(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class RerunFailed extends org.radixware.kernel.common.client.models.items.Command{
		protected RerunFailed(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::Testing::TestCase.Group - Web Meta*/

/*Radix::Testing::TestCase.Group-Application Class*/

package org.radixware.ads.Testing.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.Group_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Testing::TestCase.Group:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
			"Radix::Testing::TestCase.Group",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclPJQ6F4H7UBDWJMTQUMZFCOMZ5Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZ57E3AKKRGNDASN5LTVVJK5PI"),null,null,0,

			/*Radix::Testing::TestCase.Group:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Testing::TestCase.Group:isGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdR7AAOIBTTNDT5JUMOATGCT22JQ"),
						"isGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						63,
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

						/*Radix::Testing::TestCase.Group:isGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.Group:hasChildTestsGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OWXMIK2WZEB3H7MNE2VRM5TOI"),
						"hasChildTestsGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
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

						/*Radix::Testing::TestCase.Group:hasChildTestsGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.Group:stopAfterTestFail:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIYTC6UKMOZHJTCUJWL4DKR3BME"),
						"stopAfterTestFail",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3XPRDERJTNEBVO74YFTMK5445M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						true,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Testing::TestCase.Group:stopAfterTestFail:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Testing::TestCase.Group:groupTypeRef:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVMGI6FYCIRGMTOKEZ5ZDZ7BV4E"),
						"groupTypeRef",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
						null,
						null,
						null,
						133693439,
						133693439,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase.Group:Rerun-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHGOOKC3J3DNRDISQAAAAAAAAAA"),
						"Rerun",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWRGR5ELCEZERPLD2AFC7KT4TMY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWZZPS3ZBBNFVNHFNSKTITBMHQE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Testing::TestCase.Group:RerunFailed-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdHVIQZGPGHJAMBO6NAICEGXZVBA"),
						"RerunFailed",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5WG3UPXIREGNESRBQMF4PYHCA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img732FB6LFUVBQJNABPIOBBEZPHI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::Testing::TestCase.Group:General - Desktop Meta*/

/*Radix::Testing::TestCase.Group:General-Editor Presentation*/

package org.radixware.ads.Testing.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr35DU4J2H6FE63IB4P25Q6UZRGU"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprBMFQWKU227NRDISQAAAAAAAAAA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA"),
	null,
	null,

	/*Radix::Testing::TestCase.Group:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Testing::TestCase.Group:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5F62VEGTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA53MXGOJGJFIVGWPXT6CUBYY24"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3UZ2Z4S27NRDISQAAAAAAAAAA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZSHM4TXE3XNRDISQAAAAAAAAAA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPMEM3RSM3PNRDISQAAAAAAAAAA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWOGO4MS27NRDISQAAAAAAAAAA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBJZDIUS27NRDISQAAAAAAAAAA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKCERAMS27NRDISQAAAAAAAAAA"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYWL4NXUS27NRDISQAAAAAAAAAA"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC46NAASN3PNRDISQAAAAAAAAAA"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWP3QCET27NRDISQAAAAAAAAAA"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ZQVM54K5FEL7D7RH4HIYI62Y4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL4QQIGK7MFGF5FCW7HHEA4YIZI"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgGS5UCEHPSJF2PBXTBWHVEEIP4I"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdROKBL5544NGPXNKFHAAELEX3SM"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQP4LKSN3IFHITPAQXU7J72H6FA"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIYTC6UKMOZHJTCUJWL4DKR3BME"),0,13,1,false,false)
			},new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgGS5UCEHPSJF2PBXTBWHVEEIP4I"),"IS props",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEGEFUWLL3FF5JBGJXA5XWLK6KE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFAF35NQEZHWPKJAMOFJZA4RRQ"),false,true,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUSPDDDJLZ5AINICL3FIA4KD7GU"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ELDBK7XVDQDA626NCPOMNAXM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI3FLY6XA5DV3LZKE3RO4PQFFQ"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTWEBHYK2JGENOQY3SHZ27BQ6I"),0,1,1,false,false)
					})
			})
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5F62VEGTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7BIMYSOU2JFUHJFWJJGELYTFZA"))}
	,

	/*Radix::Testing::TestCase.Group:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::Testing::TestCase.Group:General:Model - Desktop Executable*/

/*Radix::Testing::TestCase.Group:General:Model-Entity Model Class*/

package org.radixware.ads.Testing.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:General:Model")
public class General:Model  extends org.radixware.ads.Testing.explorer.TestCase.Group.TestCase.Group_DefaultModel.eprBMFQWKU227NRDISQAAAAAAAAAA_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Testing::TestCase.Group:General:Model:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCase.Group:General:Model:Properties-Properties*/

	/*Radix::Testing::TestCase.Group:General:Model:Methods-Methods*/

	/*Radix::Testing::TestCase.Group:General:Model:afterRerun-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:General:Model:afterRerun")
	protected  void afterRerun () throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceClientException {
		super.afterRerun();

		Explorer.Models::GroupModel group = (Explorer.Models::GroupModel)findOwnerByClass(Explorer.Models::GroupModel.class);
		    
		if (group != null) {
		    Client.Views::ISelector selector = group.getGroupView();
		    if (selector != null) {
		        Client.Views::ISelectorWidget widget = selector.getSelectorWidget();
		        if (widget instanceof Explorer.Widgets::SelectorTree) {
		            ((Explorer.Widgets::SelectorTree) widget).rereadChildrenForCurrentEntity();
		        }
		    }
		}
	}

	/*Radix::Testing::TestCase.Group:General:Model:onCommand_RerunFailed-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCase.Group:General:Model:onCommand_RerunFailed")
	public  void onCommand_RerunFailed (org.radixware.ads.Testing.explorer.TestCase.Group.RerunFailed command) {
		final Client.Types::IProgressHandle progress = Environment.getProgressHandleManager().newStandardProgressHandle();
		progress.startProgress("Running failed tests...",true);
		try {
		    command.send();
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);    
		} catch (InterruptedException e){
		    //test executing was canceled by user - do nothing.
		    return;
		} finally{
		    try{
		        if (!progress.wasCanceled()){//if test execution was not canceled...
		            afterRerun();
		        }
		    } catch(Exceptions::ServiceClientException e){
		        showException(e);
		    } catch(InterruptedException e){
		        //reading new data was canceled by user - do nothing               
		    } finally{
		        progress.finishProgress();
		    }
		}
	}
	public final class RerunFailed extends org.radixware.ads.Testing.explorer.TestCase.Group.RerunFailed{
		protected RerunFailed(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_RerunFailed( this );
		}

	}













}

/* Radix::Testing::TestCase.Group:General:Model - Desktop Meta*/

/*Radix::Testing::TestCase.Group:General:Model-Entity Model Class*/

package org.radixware.ads.Testing.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem35DU4J2H6FE63IB4P25Q6UZRGU"),
						"General:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemBMFQWKU227NRDISQAAAAAAAAAA"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Testing::TestCase.Group:General:Model:Properties-Properties*/
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

/* Radix::Testing::TestCase.Group - Localizing Bundle */
package org.radixware.ads.Testing.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCase.Group - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop after first unsuccessful test");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Останавливаться после первого неуспешного теста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3XPRDERJTNEBVO74YFTMK5445M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA53MXGOJGJFIVGWPXT6CUBYY24"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Integration Server (IS)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер интеграции (СИ)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEGEFUWLL3FF5JBGJXA5XWLK6KE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Running failed tests...");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнение неуспешных тестов...");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBBLJEMHD5FZXGSM64GRG62VYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Group of Test Cases");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа тестов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZ57E3AKKRGNDASN5LTVVJK5PI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Rerun failed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнить неуспешные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5WG3UPXIREGNESRBQMF4PYHCA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Rerun");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнить заново");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWRGR5ELCEZERPLD2AFC7KT4TMY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(TestCase.Group - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclRFAF35NQEZHWPKJAMOFJZA4RRQ"),"TestCase.Group - Localizing Bundle",$$$items$$$);
}


/* Radix::Explorer.Sqml::CustomParentRefParameter - Desktop Executable*/

/*Radix::Explorer.Sqml::CustomParentRefParameter-Desktop Dynamic Class*/

package org.radixware.ads.Explorer.Sqml.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter")
 class CustomParentRefParameter  implements org.radixware.kernel.common.client.meta.sqml.ISqmlParameter  {



	/*Radix::Explorer.Sqml::CustomParentRefParameter:Nested classes-Nested Classes*/

	/*Radix::Explorer.Sqml::CustomParentRefParameter:Properties-Properties*/

	/*Radix::Explorer.Sqml::CustomParentRefParameter:tableId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id tableId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:tableId")
	private final  org.radixware.kernel.common.types.Id getTableId() {
		return tableId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:tableId")
	private final   void setTableId(org.radixware.kernel.common.types.Id val) {
		tableId = val;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:selPresentationId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id selPresentationId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:selPresentationId")
	private final  org.radixware.kernel.common.types.Id getSelPresentationId() {
		return selPresentationId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:selPresentationId")
	private final   void setSelPresentationId(org.radixware.kernel.common.types.Id val) {
		selPresentationId = val;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:selPresentationClassId-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id selPresentationClassId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:selPresentationClassId")
	private final  org.radixware.kernel.common.types.Id getSelPresentationClassId() {
		return selPresentationClassId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:selPresentationClassId")
	private final   void setSelPresentationClassId(org.radixware.kernel.common.types.Id val) {
		selPresentationClassId = val;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:id-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id id=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:id")
	private final  org.radixware.kernel.common.types.Id getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:id")
	private final   void setId(org.radixware.kernel.common.types.Id val) {
		id = val;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:name-Dynamic Property*/



	protected Str name=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:name")
	private final  Str getName() {
		return name;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:name")
	private final   void setName(Str val) {
		name = val;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:additionalCondition-Dynamic Property*/



	protected org.radixware.schemas.xscml.Sqml additionalCondition=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:additionalCondition")
	private final  org.radixware.schemas.xscml.Sqml getAdditionalCondition() {
		return additionalCondition;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:additionalCondition")
	private final   void setAdditionalCondition(org.radixware.schemas.xscml.Sqml val) {
		additionalCondition = val;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:Methods-Methods*/

	/*Radix::Explorer.Sqml::CustomParentRefParameter:canHavePersistentValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:canHavePersistentValue")
	public published  boolean canHavePersistentValue () {
		return false;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getBasePropertyId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getBasePropertyId")
	public published  org.radixware.kernel.common.types.Id getBasePropertyId () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getEditMask-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getEditMask")
	public published  org.radixware.kernel.common.client.meta.mask.EditMask getEditMask () {
		return new EditMaskNone();
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getEnumId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getEnumId")
	public published  org.radixware.kernel.common.types.Id getEnumId () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getInitialVal-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getInitialVal")
	public published  org.radixware.kernel.common.defs.value.ValAsStr getInitialVal () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getNullString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getNullString")
	public published  Str getNullString () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getParentSelectorPresentationClassId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getParentSelectorPresentationClassId")
	public published  org.radixware.kernel.common.types.Id getParentSelectorPresentationClassId () {
		return selPresentationClassId;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getParentSelectorPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getParentSelectorPresentationId")
	public published  org.radixware.kernel.common.types.Id getParentSelectorPresentationId () {
		return selPresentationId;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getPersistentValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getPersistentValue")
	public published  org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue getPersistentValue () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getReferencedTableId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getReferencedTableId")
	public published  org.radixware.kernel.common.types.Id getReferencedTableId () {
		return tableId;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getType")
	public published  org.radixware.kernel.common.enums.EValType getType () {
		return Meta::ValType:ParentRef;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:isMandatory-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:isMandatory")
	public published  boolean isMandatory () {
		return true;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:setPersistentValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:setPersistentValue")
	public published  void setPersistentValue (org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue value) {

	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getDisplayableText-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getDisplayableText")
	public published  Str getDisplayableText (org.radixware.kernel.common.client.enums.EDefinitionDisplayMode displayMode) {
		return name;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getFullName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getFullName")
	public published  Str getFullName () {
		return name;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getIcon-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getIcon")
	public published  org.radixware.kernel.common.client.env.ClientIcon getIcon () {
		return org.radixware.kernel.common.client.env.KernelIcon.getInstance(org.radixware.kernel.common.defs.ads.AdsDefinitionIcon.SQL_CLASS_LITERAL_PARAMETER);
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getId")
	public published  org.radixware.kernel.common.types.Id getId () {
		return id;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getModuleName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getModuleName")
	public published  Str getModuleName () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getShortName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getShortName")
	public published  Str getShortName () {
		return name;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getTitle")
	public published  Str getTitle () {
		return name;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:CustomParentRefParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:CustomParentRefParameter")
	public  CustomParentRefParameter (org.radixware.kernel.common.types.Id paramId, org.radixware.kernel.common.types.Id tableId, org.radixware.kernel.common.types.Id selPresentationClassId, org.radixware.kernel.common.types.Id selPresentationId, Str name) {
		id = paramId;
		tableId = tableId;
		selPresentationClassId = selPresentationClassId;
		selPresentationId = selPresentationId;
		name = name;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getIdPath-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getIdPath")
	public published  org.radixware.kernel.common.types.Id[] getIdPath () {
		return new Types::Id[]{this.id};
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:isDeprecated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:isDeprecated")
	public published  boolean isDeprecated () {
		return false;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getMaxArrayItemsCount-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getMaxArrayItemsCount")
	public published  int getMaxArrayItemsCount () {
		return -1;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getMinArrayItemsCount-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getMinArrayItemsCount")
	public published  int getMinArrayItemsCount () {
		return -1;
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:getParentSelectorAdditionalCondition-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:getParentSelectorAdditionalCondition")
	public published  org.radixware.schemas.xscml.Sqml getParentSelectorAdditionalCondition () {
		return additionalCondition==null ? null : (Meta::XscmlXsd:Sqml)(additionalCondition.copy());
	}

	/*Radix::Explorer.Sqml::CustomParentRefParameter:setParentSelectorAdditionalCondition-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomParentRefParameter:setParentSelectorAdditionalCondition")
	public published  void setParentSelectorAdditionalCondition (org.radixware.schemas.xscml.Sqml condition) {
		additionalCondition= condition==null ? null : (Meta::XscmlXsd:Sqml)(condition.copy());
	}


}

/* Radix::Explorer.Sqml::CustomParentRefParameter - Desktop Meta*/

/*Radix::Explorer.Sqml::CustomParentRefParameter-Desktop Dynamic Class*/

package org.radixware.ads.Explorer.Sqml.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CustomParentRefParameter_mi{
}


/* Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter - Desktop Executable*/

/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter-Desktop Dynamic Class*/

package org.radixware.ads.Explorer.Sqml.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter")
 class CustomSimpleTypeSqmlParameter  implements org.radixware.kernel.common.client.meta.sqml.ISqmlParameter  {



	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:Nested classes-Nested Classes*/

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:Properties-Properties*/

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:id-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id id=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:id")
	private final  org.radixware.kernel.common.types.Id getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:id")
	private final   void setId(org.radixware.kernel.common.types.Id val) {
		id = val;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:name-Dynamic Property*/



	protected Str name=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:name")
	private final  Str getName() {
		return name;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:name")
	private final   void setName(Str val) {
		name = val;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:valType-Dynamic Property*/



	protected org.radixware.kernel.common.enums.EValType valType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:valType")
	private final  org.radixware.kernel.common.enums.EValType getValType() {
		return valType;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:valType")
	private final   void setValType(org.radixware.kernel.common.enums.EValType val) {
		valType = val;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:enumDef-Dynamic Property*/



	protected org.radixware.kernel.common.client.meta.RadEnumPresentationDef enumDef=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:enumDef")
	private final  org.radixware.kernel.common.client.meta.RadEnumPresentationDef getEnumDef() {
		return enumDef;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:enumDef")
	private final   void setEnumDef(org.radixware.kernel.common.client.meta.RadEnumPresentationDef val) {
		enumDef = val;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:Methods-Methods*/

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:canHavePersistentValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:canHavePersistentValue")
	public published  boolean canHavePersistentValue () {
		return false;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getEditMask-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getEditMask")
	public published  org.radixware.kernel.common.client.meta.mask.EditMask getEditMask () {
		return enumDef==null ? Explorer.EditMask::EditMask.newInstance(valType) : new EditMaskConstSet(enumDef.getId());
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getEnumId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getEnumId")
	public published  org.radixware.kernel.common.types.Id getEnumId () {
		return enumDef==null ? null : enumDef.getId();
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getInitialVal-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getInitialVal")
	public published  org.radixware.kernel.common.defs.value.ValAsStr getInitialVal () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getNullString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getNullString")
	public published  Str getNullString () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getParentSelectorPresentationClassId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getParentSelectorPresentationClassId")
	public published  org.radixware.kernel.common.types.Id getParentSelectorPresentationClassId () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getPersistentValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getPersistentValue")
	public published  org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue getPersistentValue () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getParentSelectorPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getParentSelectorPresentationId")
	public published  org.radixware.kernel.common.types.Id getParentSelectorPresentationId () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getBasePropertyId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getBasePropertyId")
	public published  org.radixware.kernel.common.types.Id getBasePropertyId () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getReferencedTableId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getReferencedTableId")
	public published  org.radixware.kernel.common.types.Id getReferencedTableId () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getType")
	public published  org.radixware.kernel.common.enums.EValType getType () {
		return valType;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:isMandatory-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:isMandatory")
	public published  boolean isMandatory () {
		return true;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:setPersistentValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:setPersistentValue")
	public published  void setPersistentValue (org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue value) {

	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getDisplayableText-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getDisplayableText")
	public published  Str getDisplayableText (org.radixware.kernel.common.client.enums.EDefinitionDisplayMode displayMode) {
		return name;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getFullName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getFullName")
	public published  Str getFullName () {
		return name;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getIcon-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getIcon")
	public published  org.radixware.kernel.common.client.env.ClientIcon getIcon () {
		return org.radixware.kernel.common.client.env.KernelIcon.getInstance(org.radixware.kernel.common.defs.ads.AdsDefinitionIcon.SQL_CLASS_LITERAL_PARAMETER);
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getId")
	public published  org.radixware.kernel.common.types.Id getId () {
		return id;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getModuleName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getModuleName")
	public published  Str getModuleName () {
		return null;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getShortName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getShortName")
	public published  Str getShortName () {
		return name;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getTitle")
	public published  Str getTitle () {
		return name;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:CustomSimpleTypeSqmlParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:CustomSimpleTypeSqmlParameter")
	public  CustomSimpleTypeSqmlParameter (org.radixware.kernel.common.types.Id paramId, org.radixware.kernel.common.enums.EValType paramValType, Str paramName) {
		id = paramId;
		valType = paramValType;
		name = paramName;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:CustomSimpleTypeSqmlParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:CustomSimpleTypeSqmlParameter")
	public  CustomSimpleTypeSqmlParameter (org.radixware.kernel.common.types.Id paramId, org.radixware.kernel.common.client.meta.RadEnumPresentationDef enumDef, Str paramName) {
		this(paramId, enumDef.getItemType(), paramName);
		enumDef = enumDef;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getIdPath-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getIdPath")
	public published  org.radixware.kernel.common.types.Id[] getIdPath () {

		return new Types::Id[]{this.id};
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:isDeprecated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:isDeprecated")
	public published  boolean isDeprecated () {
		return false;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getMaxArrayItemsCount-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getMaxArrayItemsCount")
	public published  int getMaxArrayItemsCount () {
		return -1;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getMinArrayItemsCount-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getMinArrayItemsCount")
	public published  int getMinArrayItemsCount () {
		return -1;
	}

	/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getParentSelectorAdditionalCondition-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter:getParentSelectorAdditionalCondition")
	public published  org.radixware.schemas.xscml.Sqml getParentSelectorAdditionalCondition () {
		return null;
	}


}

/* Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter - Desktop Meta*/

/*Radix::Explorer.Sqml::CustomSimpleTypeSqmlParameter-Desktop Dynamic Class*/

package org.radixware.ads.Explorer.Sqml.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CustomSimpleTypeSqmlParameter_mi{
}

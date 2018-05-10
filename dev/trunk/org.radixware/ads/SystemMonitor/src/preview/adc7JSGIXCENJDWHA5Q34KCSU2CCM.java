
/* Radix::SystemMonitor::UserQueryParameter - Desktop Executable*/

/*Radix::SystemMonitor::UserQueryParameter-Desktop Dynamic Class*/

package org.radixware.ads.SystemMonitor.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter")
public class UserQueryParameter  implements org.radixware.kernel.common.client.meta.sqml.ISqmlParameter  {



	/*Radix::SystemMonitor::UserQueryParameter:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::UserQueryParameter:Properties-Properties*/

	/*Radix::SystemMonitor::UserQueryParameter:valType-Dynamic Property*/



	protected org.radixware.kernel.common.enums.EValType valType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:valType")
	private final  org.radixware.kernel.common.enums.EValType getValType() {
		return valType;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:valType")
	private final   void setValType(org.radixware.kernel.common.enums.EValType val) {
		valType = val;
	}

	/*Radix::SystemMonitor::UserQueryParameter:id-Dynamic Property*/



	protected org.radixware.kernel.common.types.Id id=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:id")
	private final  org.radixware.kernel.common.types.Id getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:id")
	private final   void setId(org.radixware.kernel.common.types.Id val) {
		id = val;
	}

	/*Radix::SystemMonitor::UserQueryParameter:parameterName-Dynamic Property*/



	protected Str parameterName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:parameterName")
	private final  Str getParameterName() {
		return parameterName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:parameterName")
	private final   void setParameterName(Str val) {
		parameterName = val;
	}

	/*Radix::SystemMonitor::UserQueryParameter:Methods-Methods*/

	/*Radix::SystemMonitor::UserQueryParameter:getDisplayableText-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getDisplayableText")
	public published  Str getDisplayableText (org.radixware.kernel.common.client.enums.EDefinitionDisplayMode displayMode) {
		return parameterName;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getFullName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getFullName")
	public published  Str getFullName () {
		return parameterName;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getIcon-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getIcon")
	public published  org.radixware.kernel.common.client.env.ClientIcon getIcon () {
		return org.radixware.kernel.common.client.env.KernelIcon.getInstance(org.radixware.kernel.common.defs.ads.AdsDefinitionIcon.SQL_CLASS_LITERAL_PARAMETER);
	}

	/*Radix::SystemMonitor::UserQueryParameter:getId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getId")
	public published  org.radixware.kernel.common.types.Id getId () {
		return id;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getModuleName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getModuleName")
	public published  Str getModuleName () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getShortName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getShortName")
	public published  Str getShortName () {
		return parameterName;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getTitle")
	public published  Str getTitle () {
		return parameterName;
	}

	/*Radix::SystemMonitor::UserQueryParameter:canHavePersistentValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:canHavePersistentValue")
	public published  boolean canHavePersistentValue () {
		return false;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getBasePropertyId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getBasePropertyId")
	public published  org.radixware.kernel.common.types.Id getBasePropertyId () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getEditMask-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getEditMask")
	public published  org.radixware.kernel.common.client.meta.mask.EditMask getEditMask () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getEnumId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getEnumId")
	public published  org.radixware.kernel.common.types.Id getEnumId () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getInitialVal-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getInitialVal")
	public published  org.radixware.kernel.common.defs.value.ValAsStr getInitialVal () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getNullString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getNullString")
	public published  Str getNullString () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getParentSelectorPresentationClassId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getParentSelectorPresentationClassId")
	public published  org.radixware.kernel.common.types.Id getParentSelectorPresentationClassId () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getParentSelectorPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getParentSelectorPresentationId")
	public published  org.radixware.kernel.common.types.Id getParentSelectorPresentationId () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getPersistentValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getPersistentValue")
	public published  org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue getPersistentValue () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getReferencedTableId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getReferencedTableId")
	public published  org.radixware.kernel.common.types.Id getReferencedTableId () {
		return null;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getType")
	public published  org.radixware.kernel.common.enums.EValType getType () {
		return valType;
	}

	/*Radix::SystemMonitor::UserQueryParameter:isMandatory-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:isMandatory")
	public published  boolean isMandatory () {
		return true;
	}

	/*Radix::SystemMonitor::UserQueryParameter:setPersistentValue-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:setPersistentValue")
	public published  void setPersistentValue (org.radixware.kernel.common.client.meta.sqml.ISqmlParameterPersistentValue value) {
		return;
	}

	/*Radix::SystemMonitor::UserQueryParameter:UserQueryParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:UserQueryParameter")
	public  UserQueryParameter (org.radixware.kernel.common.types.Id id, org.radixware.kernel.common.enums.EValType type, Str parameterName) {
		id = id;
		valType = type;
		parameterName = parameterName;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getIdPath-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getIdPath")
	public published  org.radixware.kernel.common.types.Id[] getIdPath () {
		return new Types::Id[]{this.id};
	}

	/*Radix::SystemMonitor::UserQueryParameter:isDeprecated-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:isDeprecated")
	public published  boolean isDeprecated () {
		return false;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getMaxArrayItemsCount-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getMaxArrayItemsCount")
	public published  int getMaxArrayItemsCount () {
		return -1;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getMinArrayItemsCount-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getMinArrayItemsCount")
	public published  int getMinArrayItemsCount () {
		return -1;
	}

	/*Radix::SystemMonitor::UserQueryParameter:getParentSelectorAdditionalCondition-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::UserQueryParameter:getParentSelectorAdditionalCondition")
	public published  org.radixware.schemas.xscml.Sqml getParentSelectorAdditionalCondition () {
		return null;
	}


}

/* Radix::SystemMonitor::UserQueryParameter - Desktop Meta*/

/*Radix::SystemMonitor::UserQueryParameter-Desktop Dynamic Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserQueryParameter_mi{
}

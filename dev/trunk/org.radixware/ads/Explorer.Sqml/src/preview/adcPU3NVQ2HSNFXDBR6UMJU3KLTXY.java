
/* Radix::Explorer.Sqml::SqmlParametersBuilder - Desktop Executable*/

/*Radix::Explorer.Sqml::SqmlParametersBuilder-Desktop Dynamic Class*/

package org.radixware.ads.Explorer.Sqml.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder")
public published class SqmlParametersBuilder  {



	/*Radix::Explorer.Sqml::SqmlParametersBuilder:Nested classes-Nested Classes*/

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:Properties-Properties*/

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:parameters-Dynamic Property*/



	protected java.util.List<org.radixware.kernel.common.client.meta.sqml.ISqmlParameter> parameters=new java.util.ArrayList<org.radixware.kernel.common.client.meta.sqml.ISqmlParameter>();











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:parameters")
	private final  java.util.List<org.radixware.kernel.common.client.meta.sqml.ISqmlParameter> getParameters() {
		return parameters;
	}

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:Methods-Methods*/

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:SqmlParametersBuilder-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:SqmlParametersBuilder")
	public published  SqmlParametersBuilder () {

	}

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:addEnumParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:addEnumParameter")
	public published  void addEnumParameter (org.radixware.kernel.common.types.Id id, org.radixware.kernel.common.client.meta.RadEnumPresentationDef enumDef, Str name) {
		final CustomSimpleTypeSqmlParameter parameter = new CustomSimpleTypeSqmlParameter(id,enumDef,name);
		parameters.add(parameter);
	}

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:addSimpleParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:addSimpleParameter")
	public published  void addSimpleParameter (org.radixware.kernel.common.types.Id id, org.radixware.kernel.common.enums.EValType valType, Str name) {
		final CustomSimpleTypeSqmlParameter parameter = new CustomSimpleTypeSqmlParameter(id,valType,name);
		parameters.add(parameter);
	}

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:addSimpleParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:addSimpleParameter")
	public published  org.radixware.kernel.common.types.Id addSimpleParameter (org.radixware.kernel.common.enums.EValType valType, Str name) {
		final Types::Id parameterId = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:USER_FILTER_PARAMETER);
		final CustomSimpleTypeSqmlParameter parameter = new CustomSimpleTypeSqmlParameter(parameterId,valType,name);
		parameters.add(parameter);
		return parameterId;
	}

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:addEnumParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:addEnumParameter")
	public published  org.radixware.kernel.common.types.Id addEnumParameter (org.radixware.kernel.common.client.meta.RadEnumPresentationDef enumDef, Str name) {
		final Types::Id parameterId = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:USER_FILTER_PARAMETER);
		final CustomSimpleTypeSqmlParameter parameter = new CustomSimpleTypeSqmlParameter(parameterId,enumDef,name);
		parameters.add(parameter);
		return parameterId;
	}

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:addReferenceParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:addReferenceParameter")
	public published  void addReferenceParameter (org.radixware.kernel.common.types.Id paramId, org.radixware.kernel.common.types.Id tableId, org.radixware.kernel.common.types.Id selPresentationClassId, org.radixware.kernel.common.types.Id selectorPresentationId, Str name) {
		final CustomParentRefParameter parameter = 
		    new CustomParentRefParameter(paramId, tableId, selPresentationClassId, selectorPresentationId, name);
		parameters.add(parameter);
	}

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:addReferenceParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:addReferenceParameter")
	public published  org.radixware.kernel.common.types.Id addReferenceParameter (org.radixware.kernel.common.types.Id tableId, org.radixware.kernel.common.types.Id selPresentationClassId, org.radixware.kernel.common.types.Id selectorPresentationId, Str name) {
		final Types::Id paramId = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:USER_FILTER_PARAMETER);
		final CustomParentRefParameter parameter = 
		    new CustomParentRefParameter(paramId, tableId, selPresentationClassId, selectorPresentationId, name);
		parameters.add(parameter);
		return paramId;
	}

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:createParameters-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:createParameters")
	public published  org.radixware.kernel.common.client.meta.sqml.ISqmlParameters createParameters () {
		return new CustomSqmlParameters(parameters);
	}

	/*Radix::Explorer.Sqml::SqmlParametersBuilder:addReferenceParameter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Explorer.Sqml::SqmlParametersBuilder:addReferenceParameter")
	public published  org.radixware.kernel.common.types.Id addReferenceParameter (org.radixware.kernel.common.types.Id tableId, org.radixware.kernel.common.types.Id selPresentationClassId, org.radixware.kernel.common.types.Id selectorPresentationId, org.radixware.schemas.xscml.Sqml additionalCondition, Str name) {
		final Types::Id paramId = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:USER_FILTER_PARAMETER);
		final CustomParentRefParameter parameter = 
		    new CustomParentRefParameter(paramId, tableId, selPresentationClassId, selectorPresentationId, name);
		if (additionalCondition!=null){
		    parameter.setParentSelectorAdditionalCondition(additionalCondition);
		}
		parameters.add(parameter);
		return paramId;
	}


}

/* Radix::Explorer.Sqml::SqmlParametersBuilder - Desktop Meta*/

/*Radix::Explorer.Sqml::SqmlParametersBuilder-Desktop Dynamic Class*/

package org.radixware.ads.Explorer.Sqml.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SqmlParametersBuilder_mi{
}

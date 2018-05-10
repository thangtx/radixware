
/* Radix::Client.Env::GenericWebEntryPointHandler - Web Executable*/

/*Radix::Client.Env::GenericWebEntryPointHandler-Web Dynamic Class*/

package org.radixware.ads.Client.Env.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Env::GenericWebEntryPointHandler")
public published class GenericWebEntryPointHandler  implements org.radixware.kernel.common.client.env.IEntryPointHandler  {



	/*Radix::Client.Env::GenericWebEntryPointHandler:Nested classes-Nested Classes*/

	/*Radix::Client.Env::GenericWebEntryPointHandler:Properties-Properties*/

	/*Radix::Client.Env::GenericWebEntryPointHandler:Methods-Methods*/

	/*Radix::Client.Env::GenericWebEntryPointHandler:getPriority-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Env::GenericWebEntryPointHandler:getPriority")
	public published  int getPriority () {
		return 1;
	}

	/*Radix::Client.Env::GenericWebEntryPointHandler:handle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Env::GenericWebEntryPointHandler:handle")
	public published  boolean handle (org.radixware.kernel.common.client.env.EntryPointRequest entryPointRequest) {
		if ("Generic".equals(entryPointRequest.getEntryPointName())) {
		    org.radixware.kernel.common.client.IClientEnvironment environment = entryPointRequest.getEnvironment();
		    java.util.Map<String, String> parameters = entryPointRequest.getParameters();
		    String itemIdStr = parameters.get("explorerItemId");
		    if (itemIdStr != null && !itemIdStr.isEmpty()) {
		        Types::Id itemId = Types::Id.Factory.loadFrom(itemIdStr);
		        org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode newNode = environment.getTreeManager().getCurrentTree().findNodeByExplorerItemId(itemId);
		        if (newNode != null && newNode.isValid()) {                               
		            org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode parentNode = newNode.getParentNode();
		            if (parentNode != null) {
		                environment.getTreeManager().getCurrentTree().expand(parentNode);
		            }
		            if (environment.getTreeManager().getCurrentTree().setCurrent(newNode)) {
		                Client.Views::IExplorerItemView explorerItemView = newNode.getView();
		                final Explorer.Models::GroupModel model = ((Explorer.Models::GroupModel) explorerItemView.getModel());
		                if (model.getView() instanceof Web.Widgets::RwtSelector) {
		                    String filterIdStr = parameters.get("filterId");
		                    if (filterIdStr != null && !filterIdStr.isEmpty()) {
		                        Types::Id filterId = Types::Id.Factory.loadFrom(filterIdStr);
		                        try{
		                            applyFilter(parameters, model, filterId);
		                        }catch(Exceptions::InterruptedException ex) {
		                            return false;
		                        }
		                    }
		                    String sortingIdStr = parameters.get("sortingId");
		                    if (sortingIdStr != null && !sortingIdStr.isEmpty()) {
		                        Types::Id sortingId = Types::Id.Factory.loadFrom(sortingIdStr);
		                        final Explorer.Meta::SortingDef sortingDef = model.getSortings().findById(sortingId);                            
		                        if (sortingDef!=null && model.getCurrentFilter()==null || model.getCurrentFilter().getFilterDef().isBaseSortingEnabledById(sortingId)) {
		                            try {
		                                model.setSorting(sortingDef);
		                            } catch (Exceptions::ServiceClientException ex) {
		                                String warningStr = String.format("Failed to apply sorting %1$s", sortingIdStr);
		                                environment.getTracer().error(warningStr, ex);
		                            } catch (Exceptions::InterruptedException ex) {
		                                return false;
		                            }
		                        }
		                    }
		                    String insertIntoTreeStr = parameters.get("insertIntoTree");
		                    if (insertIntoTreeStr != null && !insertIntoTreeStr.isEmpty()) {
		                        final int entityNumber;
		                        try{
		                            entityNumber = Integer.parseInt(insertIntoTreeStr);
		                        }catch(java.lang.NumberFormatException ex){
		                            String warnStr = String.format("Failed to parse row number '%1$s''", insertIntoTreeStr);
		                            environment.getTracer().warning(warnStr);
		                            return false;
		                        }                        
		                        if (entityNumber >= 1) {                            
		                            Explorer.Models::EntityModel entityModel = null;
		                            try{
		                                entityModel = model.getEntity(entityNumber - 1);
		                            }catch(Explorer.Exceptions::BrokenEntityObjectException | Exceptions::ServiceClientException ex) {
		                                environment.getTracer().error(ex);
		                            }catch(Exceptions::InterruptedException ex) {
		                                return false;
		                            }
		                            if (entityModel != null) {
		                                Web.Widgets::RwtSelector selector = (Web.Widgets::RwtSelector)model.getGroupView();
		                                Explorer.Views::ExplorerItemView entityExplorerItemView = selector.insertEntity(entityModel);
		                                if (entityExplorerItemView != null) {
		                                    entityExplorerItemView.setCurrent();
		                                }
		                            }
		                        }
		                    } 
		                }
		            }
		        } else {
		            String warnString = String.format("No explorer item with id %1$s was found", itemIdStr);
		            environment.getTracer().warning(warnString);
		        }
		    } else {
		        environment.getTracer().warning("'explorerItemId' parameter was not defined");
		    }
		}
		return false;
	}

	/*Radix::Client.Env::GenericWebEntryPointHandler:applyFilter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Env::GenericWebEntryPointHandler:applyFilter")
	private final  void applyFilter (java.util.Map<Str,Str> parameters, org.radixware.kernel.common.client.models.GroupModel model, org.radixware.kernel.common.types.Id filterId) throws java.lang.InterruptedException {
		org.radixware.kernel.common.client.IClientEnvironment environment = model.getEnvironment();
		Explorer.Models::FilterModel filterModel = model.getFilters().findById(filterId);
		if (filterModel != null) {
		    Explorer.Meta::RadFilterDef radFilterDef = filterModel.getFilterDef();
		    Explorer.Meta::RadFilterParameters filterParams = radFilterDef.getParameters();
		    if (filterParams != null) {
		        for (Explorer.Sqml::ISqmlParameter param : filterParams.getAll()) {
		            String urlParamValueStr = parameters.get(param.getId().toString());
		            if (urlParamValueStr != null) {
		                Types::Id parentSelectorPresentationId = param.getParentSelectorPresentationId();
		                if (parentSelectorPresentationId != null) {
		                    final Explorer.Meta::SelectorPresentationDef selectorPresentationDef;
		                    try {
		                        selectorPresentationDef = environment.getDefManager().getSelectorPresentationDef(parentSelectorPresentationId);
		                    } catch (Exceptions::DefinitionError error) {
		                        environment.getTracer().error(error);
		                        continue;
		                    }
		                    final Types::Id tableId = selectorPresentationDef.getTableId();
		                    try {
		                        Explorer.Models.Properties::Property property = filterModel.getProperty(param.getId());
		                        if (property instanceof Explorer.Models.Properties::PropertyRef) {
		                            Explorer.Types::Pid pid = new Pid(tableId, urlParamValueStr);
		                            Explorer.Types::Reference ref = new Reference(pid);
		                            ref = ((Explorer.Models.Properties::PropertyRef) property).updateTitle(ref);
		                            filterModel.getProperty(param.getId()).setValueObject(ref);
		                        } else if (property instanceof Explorer.Models.Properties::PropertyArrRef) {
		                            ArrStr arrStr = (ArrStr) Client.Utils::ValueConverter.valAsStr2Obj(Types::ValAsStr.Factory.loadFrom(urlParamValueStr), Meta::ValType:ArrStr);
		                            Explorer.Types::ArrReference arrRef = new ArrReference();
		                            for (String str : arrStr) {
		                                arrRef.add(new Reference(new Pid(tableId, str)));
		                            }
		                            arrRef = ((Explorer.Models.Properties::PropertyArrRef) property).updateTitles(arrRef);
		                            filterModel.getProperty(param.getId()).setValueObject(arrRef);
		                        }
		                    } catch (Exceptions::ServiceClientException ex) {
		                        environment.getTracer().error(ex);
		                    }
		                } else {
		                    Explorer.Models.Properties::Property property = filterModel.getProperty(param.getId());
		                    java.lang.Object obj = Client.Utils::ValueConverter.valAsStr2Obj(Types::ValAsStr.Factory.loadFrom(urlParamValueStr), property.getType());
		                    filterModel.getProperty(param.getId()).setValueObject(obj);
		                }
		            }
		        }
		    }
		    if (filterModel.canApply()) {
		        try {
		            model.setFilter(filterModel);
		        } catch (Explorer.Exceptions::InvalidPropertyValueException | Explorer.Exceptions::PropertyIsMandatoryException | Exceptions::ServiceClientException ex) {
		            String warningStr = String.format("Failed to apply filter %1$s", filterId.toString());
		            environment.getTracer().error(warningStr, ex);
		        } 
		    }
		}
	}


}

/* Radix::Client.Env::GenericWebEntryPointHandler - Web Meta*/

/*Radix::Client.Env::GenericWebEntryPointHandler-Web Dynamic Class*/

package org.radixware.ads.Client.Env.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class GenericWebEntryPointHandler_mi{
}

/* Radix::Client.Env::GenericWebEntryPointHandler - Localizing Bundle */
package org.radixware.ads.Client.Env.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class GenericWebEntryPointHandler - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to apply filter %1$s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не получилось применить фильтр с  id %1$s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3U3JFV2VV5E6VBACU2IEEJJ2OM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to apply sorting %1$s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не получилось применить сортировку с  id %1$s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TLXQGL4YJHE3MB7UHRNMUE7YM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"\'explorerItemId\' parameter was not defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"параметр \'\'explorerItemId\' не был задан");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQHQFE6OKJBBP3MNHUTL4YS4AOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed to parse row number \'%1$s\'\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при чтении номера ряда \'%1$s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQTVU7SEETRAB3KOW26GLAHOKVQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"No explorer item with id %1$s was found");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не найден элемент проводника с id %1$s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY44XQKJ4ZNDW7KOOLKT2FW6FU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(GenericWebEntryPointHandler - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcBNBQDU5C7VGPNE7OXJJKCKWGRY"),"GenericWebEntryPointHandler - Localizing Bundle",$$$items$$$);
}

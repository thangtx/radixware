
/* Radix::Client.Types::AbstractWidgetAreaItemController - Client-Common Executable*/

/*Radix::Client.Types::AbstractWidgetAreaItemController-Client-Common Dynamic Class*/

package org.radixware.ads.Client.Types.common_client;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Types::AbstractWidgetAreaItemController")
public abstract published class AbstractWidgetAreaItemController  implements org.radixware.kernel.common.client.widgets.area.IWidgetAreaItemController  {



	/*Radix::Client.Types::AbstractWidgetAreaItemController:Nested classes-Nested Classes*/

	/*Radix::Client.Types::AbstractWidgetAreaItemController:Properties-Properties*/

	/*Radix::Client.Types::AbstractWidgetAreaItemController:Methods-Methods*/

	/*Radix::Client.Types::AbstractWidgetAreaItemController:cancelChanges-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Types::AbstractWidgetAreaItemController:cancelChanges")
	public published  void cancelChanges (org.radixware.kernel.common.client.widgets.area.WidgetAreaItem widgetAreaItem) {

	}

	/*Radix::Client.Types::AbstractWidgetAreaItemController:beforeClose-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Types::AbstractWidgetAreaItemController:beforeClose")
	public published  boolean beforeClose (org.radixware.kernel.common.client.widgets.area.WidgetAreaItem widgetAreaItem) {
		return true;
	}

	/*Radix::Client.Types::AbstractWidgetAreaItemController:applyChanges-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Types::AbstractWidgetAreaItemController:applyChanges")
	public published  boolean applyChanges (org.radixware.kernel.common.client.widgets.area.WidgetAreaItem widgetAreaItem) {
		return true;
	}

	/*Radix::Client.Types::AbstractWidgetAreaItemController:createContent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Types::AbstractWidgetAreaItemController:createContent")
	public published  org.radixware.kernel.common.client.widgets.IWidget createContent (org.radixware.kernel.common.client.widgets.area.WidgetAreaItem widgetAreaItem, org.radixware.kernel.common.client.widgets.area.WidgetAreaItem.ECreationMode creationMode, org.apache.xmlbeans.XmlObject xmlObj) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/*Radix::Client.Types::AbstractWidgetAreaItemController:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Types::AbstractWidgetAreaItemController:getTitle")
	public published  Str getTitle () {
		return AbstractWidgetAreaItemController.class.getSimpleName();
	}

	/*Radix::Client.Types::AbstractWidgetAreaItemController:isCreationEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Types::AbstractWidgetAreaItemController:isCreationEnabled")
	public published  boolean isCreationEnabled (org.radixware.kernel.common.client.widgets.area.IWidgetArea widgetArea) {
		return false;
	}

	/*Radix::Client.Types::AbstractWidgetAreaItemController:isModified-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Types::AbstractWidgetAreaItemController:isModified")
	public published  boolean isModified (org.radixware.kernel.common.client.widgets.area.WidgetAreaItem item) {
		return false;
	}

	/*Radix::Client.Types::AbstractWidgetAreaItemController:store-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Client.Types::AbstractWidgetAreaItemController:store")
	public published  org.apache.xmlbeans.XmlObject store (org.radixware.kernel.common.client.widgets.area.WidgetAreaItem widgetAreaItem) {
		return null;
	}


}

/* Radix::Client.Types::AbstractWidgetAreaItemController - Client-Common Meta*/

/*Radix::Client.Types::AbstractWidgetAreaItemController-Client-Common Dynamic Class*/

package org.radixware.ads.Client.Types.common_client;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AbstractWidgetAreaItemController_mi{
}

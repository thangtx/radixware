
/* Radix::Testing::TestCaseSelectorView - Desktop Executable*/

/*Radix::Testing::TestCaseSelectorView-Desktop Dynamic Class*/

package org.radixware.ads.Testing.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseSelectorView")
public class TestCaseSelectorView  extends org.radixware.kernel.explorer.views.selector.Selector  {

	private final Types::Id explorerItemId;
	private final Types::Id hasChildrenPropId;

	public boolean shouldEditOnInsert=false;

	/*Radix::Testing::TestCaseSelectorView:Nested classes-Nested Classes*/

	/*Radix::Testing::TestCaseSelectorView:Properties-Properties*/

	/*Radix::Testing::TestCaseSelectorView:Methods-Methods*/

	/*Radix::Testing::TestCaseSelectorView:open-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseSelectorView:open")
	public published  void open (org.radixware.kernel.common.client.models.Model groupModel) {
		super.open(groupModel);
		updateMoveCmdVisibility(false, false);

		final Explorer.Views.Wraps::StandardSelectorTreeModel model = new StandardSelectorTreeModel((Explorer.Models::GroupModel) groupModel, explorerItemId) {

		    public Types::Id getHasChildrenPropertyId(Explorer.Models::EntityModel entity) {
		        return hasChildrenPropId;
		    }

		    public boolean canCreateChild(Explorer.Models::EntityModel parent) {
		        return ((TestCase) parent).canHaveChilds.Value.booleanValue();
		    }
		};

		final Explorer.Widgets::SelectorTree selectorTree = new SelectorTree(this, model) //{
		{
		    @Override
		    protected void rowsInserted(final com.trolltech.qt.core.QModelIndex parent, final int start, final int end) {
		        super.rowsInserted(parent, start, end);
		//        if (shouldEditOnInsert) {
		//            setCurrentIndex(parent.child(0,parent.column()));
		//            this.controller.selector.actions.().();
		//            shouldEditOnInsert = false;
		//        }
		    }

		    @Override
		    public void afterPrepareCreate(final Explorer.Models::EntityModel entity) {
		        super.afterPrepareCreate(entity);
		    }
		};
		final com.trolltech.qt.gui.QVBoxLayout layout = new com.trolltech.qt.gui.QVBoxLayout(content);
		layout.setSpacing(0);
		layout.setMargin(0);
		layout.addWidget(selectorTree);
		selectorTree.setSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);
		setSelectorWidget(selectorTree);
	}

	/*Radix::Testing::TestCaseSelectorView:TestCaseSelectorView-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseSelectorView:TestCaseSelectorView")
	public  TestCaseSelectorView (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.kernel.common.types.Id explorerItemId, org.radixware.kernel.common.types.Id hasChildrenPropId) {
		super(env);
		this.explorerItemId = explorerItemId;
		this.hasChildrenPropId = hasChildrenPropId;
	}

	/*Radix::Testing::TestCaseSelectorView:setCurrentEntity-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseSelectorView:setCurrentEntity")
	public published  void setCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
		boolean moveUp = false, moveDown = false;
		if (entity != null) {
		    TestCase curTest = (TestCase) entity;
		    if (curTest.groupId.Value != null) {
		        moveUp = curTest.seq.Value.longValue() > 1;
		        
		        Explorer.Models::GroupModel thisGroupModel = Explorer.Context::Utils.getOwnerGroup(entity);
		        try {
		            if (!thisGroupModel.hasMoreRows() && thisGroupModel.getEntity(thisGroupModel.getEntitiesCount() - 1).getPid().equals(entity.getPid())) {
		                moveDown = false;
		            } else {
		                moveDown = true;
		            }
		        } catch (Explorer.Exceptions::BrokenEntityObjectException | Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		            getEnvironment().getTracer().error(ex);
		        }
		    }
		}
		updateMoveCmdVisibility(moveUp, moveDown);
		super.setCurrentEntity(entity);
	}

	/*Radix::Testing::TestCaseSelectorView:leaveCurrentEntity-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseSelectorView:leaveCurrentEntity")
	public published  boolean leaveCurrentEntity (boolean forced) {
		updateMoveCmdVisibility(false, false);
		return super.leaveCurrentEntity(forced);
	}

	/*Radix::Testing::TestCaseSelectorView:updateMoveCmdVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Testing::TestCaseSelectorView:updateMoveCmdVisibility")
	private final  void updateMoveCmdVisibility (boolean moveUp, boolean moveDown) {
		((Explorer.Models::GroupModel)getModel()).getCommand(idof[TestCaseGroup:MoveUp]).setVisible(moveUp);
		((Explorer.Models::GroupModel)getModel()).getCommand(idof[TestCaseGroup:MoveDown]).setVisible(moveDown);
	}


}

/* Radix::Testing::TestCaseSelectorView - Desktop Meta*/

/*Radix::Testing::TestCaseSelectorView-Desktop Dynamic Class*/

package org.radixware.ads.Testing.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TestCaseSelectorView_mi{
}

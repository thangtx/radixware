
/* Radix::MessageQueue::MessageQueue.Producer.Jms - Server Executable*/

/*Radix::MessageQueue::MessageQueue.Producer.Jms-Application Class*/

package org.radixware.ads.MessageQueue.server;

import java.io.IOException;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import org.radixware.kernel.server.units.mq.JmsDelegate;
import org.radixware.schemas.messagequeue.MqMessageBody;
import org.radixware.schemas.messagequeue.MqMessageMeta;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms")
public published class MessageQueue.Producer.Jms  extends org.radixware.ads.MessageQueue.server.MessageQueue.Producer  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static class MqJmsProducer implements IMqProducer, JmsDelegate.JmsUnit {
	    
	    private final JmsDelegate delegate;
	    
	    @Override
	    public void logDebug(String mess) {
	        Arte::Trace.debug(mess, Arte::EventSource:MqHandlerUnit);
	    }
	    
	    @Override
	    public void logEvent(String mess) {
	        Arte::Trace.event(mess, Arte::EventSource:MqHandlerUnit);
	    }
	    
	    @Override
	    public void logError(String mess) {
	        Arte::Trace.error(mess, Arte::EventSource:MqHandlerUnit);
	    }

	    public MqJmsProducer(final MessageQueue.Producer.Jms entity, final Str cacheItemId) throws MessageQueueException {
	        
	        delegate = new JmsDelegate(this, cacheItemId);
	    
	        try {
	            delegate.createJndiContext(entity.jndiInitialContextFactory, entity.jndiProviderUrl, entity.jndiOptionsMap);
	            delegate.createConnectionFactory(entity.jndiConnFactoryLookupName, entity.connFactoryClassName, entity.connFactoryOptionsMap);
	            
	            delegate.startAntiFreeze();
	            delegate.createConnection(entity.login, entity.password, null);
	            delegate.createSession();
	            delegate.createTopic(entity.jndiQueueLookupName, entity.queueName);
	            delegate.createProducer();
	        } catch (Exception ex) {
	            delegate.closeSilent();
	            throw new MessageQueueException("MqJmsProducer creation failed", ex);
	        } finally {
	            delegate.stopAntiFreeze();
	        }
	    }
	    
	    private Message createJmsMessage(MqMessageMeta meta, MqMessageBody body) throws JMSException {
	        Message message = null;
	        if (body.isSetStrBody()) {
	            message = delegate.session.createTextMessage(body.StrBody);
	        } else {
	            message = delegate.session.createBytesMessage();
	            ((BytesMessage)message).writeBytes(getSendBytes(meta, body));
	        }
	        if (meta.getJmsSpecific() != null) {
	            if (meta.getJmsSpecific().isSetCorrelationID()) {
	                message.setJMSCorrelationID(meta.getJmsSpecific().getCorrelationID());
	            }
	            if (meta.getJmsSpecific().isSetType()) {
	                message.setJMSType(meta.getJmsSpecific().getType());
	            }
	        }
	        return message;
	    }
	    
	    @Override
	    public String send(MqMessageMeta meta, MqMessageBody body) throws IOException {
	        try {
	            Message message = createJmsMessage(meta, body);
	            delegate.startAntiFreeze();
	            delegate.producer.send(message);
	        } catch (JMSException ex) {
	            logError("ARTE JMS send: exception: \n" + Utils::ExceptionTextFormatter.exceptionStackToString(ex));
	            throw new IOException("Error while sending message to queue", ex);
	        } finally {
	            delegate.stopAntiFreeze();
	        }
	        
	        return meta.getMessageId();
	    }
	    
	    @Override
	    public void close() throws Exception {
	        delegate.close();
	    }
	    
	    @Override
	    public boolean isConnected() {
	        return true;
	    }
	}

	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MessageQueue.Producer.Jms_mi.rdxMeta;}

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Nested classes-Nested Classes*/

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Properties-Properties*/

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName")
	public  Str getConnFactoryClassName() {
		return connFactoryClassName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName")
	public   void setConnFactoryClassName(Str val) {
		connFactoryClassName = val;
	}

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions")
	public  Str getConnFactoryOptions() {
		return connFactoryOptions;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions")
	public   void setConnFactoryOptions(Str val) {
		connFactoryOptions = val;
	}

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName")
	public  Str getJndiConnFactoryLookupName() {
		return jndiConnFactoryLookupName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName")
	public   void setJndiConnFactoryLookupName(Str val) {
		jndiConnFactoryLookupName = val;
	}

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueKind-Column-Based Property*/




	/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptionsMap-Dynamic Property*/



	protected java.util.Map<Str,Str> connFactoryOptionsMap=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptionsMap")
	public  java.util.Map<Str,Str> getConnFactoryOptionsMap() {

		return org.radixware.kernel.common.utils.Maps.fromKeyValuePairsString(connFactoryOptions);

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptionsMap")
	public   void setConnFactoryOptionsMap(java.util.Map<Str,Str> val) {
		connFactoryOptionsMap = val;
	}

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:brokerAddress-Column-Based Property*/




	/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueName-Column-Based Property*/




	/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiQueueLookupName-Column-Based Property*/
















































































	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Methods-Methods*/

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:getSender-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:getSender")
	public published  org.radixware.ads.MessageQueue.server.IMqProducer getSender (Str cacheItemId) throws org.radixware.ads.MessageQueue.common.MessageQueueException {
		return new MqJmsProducer(this, cacheItemId);
	}

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:prepareMeta-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:prepareMeta")
	public published  org.radixware.schemas.messagequeue.MqMessageMeta prepareMeta (org.radixware.schemas.messagequeue.MqMessageMeta meta, org.radixware.schemas.messagequeue.MqMessageBody body) {
		if (meta == null) {
		    meta = MessageQueueXsd:MqMessageMeta.Factory.newInstance();
		}

		if (meta.getEncoding() == null) {
		    meta.setEncoding("UTF-8");
		}

		if (meta.getQueueName() == null) {
		    meta.setQueueName(queueName);
		}

		if (meta.getMessageId() == null) {
		    meta.setMessageId(calcDefaultMessId());
		}

		meta.setDebugKey(meta.getMessageId());

		return meta;
	}

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:getPartition-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:getPartition")
	public published  Int getPartition (org.radixware.schemas.messagequeue.MqMessageMeta meta, org.radixware.schemas.messagequeue.MqMessageBody body) {
		return super.getSafPartition(meta, body);
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::MessageQueue::MessageQueue.Producer.Jms - Server Meta*/

/*Radix::MessageQueue::MessageQueue.Producer.Jms-Application Class*/

package org.radixware.ads.MessageQueue.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageQueue.Producer.Jms_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),"MessageQueue.Producer.Jms",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHDLEWVSUNHFJL3YMXLWLE35DI"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
							/*Owner Class Name*/
							"MessageQueue.Producer.Jms",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHDLEWVSUNHFJL3YMXLWLE35DI"),
							/*Property presentations*/

							/*Radix::MessageQueue::MessageQueue.Producer.Jms:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBAOT5IAUVFOJKJWMIMZ3STNDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWSVD6WMBRARBCGP6U4BAQOM2Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYVAS3VBW75FNFOLW2XDBRG7ZZA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueKind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PCH7MSGLZFSNNDIFYF6WQLWDI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::MessageQueue::MessageQueue.Producer.Jms:brokerAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOPGPZBRRSZFCFP6DSBBECKFM74"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTRIHVJKCKNDNVESJUPLUFKPVQA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiQueueLookupName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNST2OY3VRBFTHZDJPS34GEHEU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
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
									/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWRCU5UZMVNDOFJESEXOUPTAXKE"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7JNXRJQ3R5BDBMFTUG72M2JTXE"),
									101552,
									null,

									/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									null,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::MessageQueue::MessageQueue.Producer.Jms:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOHHGMQIHARERZNM5I6FK33DHFA"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWRCU5UZMVNDOFJESEXOUPTAXKE"),
									105648,
									null,

									/*Radix::MessageQueue::MessageQueue.Producer.Jms:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									null,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7JNXRJQ3R5BDBMFTUG72M2JTXE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJFYEPXEXLFAGXCM3U26EA7AJ4I")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWRCU5UZMVNDOFJESEXOUPTAXKE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOHHGMQIHARERZNM5I6FK33DHFA")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::MessageQueue::MessageQueue.Producer.Jms:Producers-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("ecc5NBNQ63LAFHETGGWPERIR4Y6GI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),null,21.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQZ2AJHN3PFDWXC5BT6I66OS5PQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIJMFWGNFU5AV3PP4PS3SDW3NKY"),

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBAOT5IAUVFOJKJWMIMZ3STNDM"),"connFactoryClassName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWS4C2KMKBRGEFINCAJJYAXABCQ"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refXBMINQMSFJFZDK2KWYNYZO2YSQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colXEEGEA3QQ5AQBIDZ337YOJMX54"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWSVD6WMBRARBCGP6U4BAQOM2Q"),"connFactoryOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQJDLVFLLDJFSPENVP6DUD22XS4"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refXBMINQMSFJFZDK2KWYNYZO2YSQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colDI2FT42XDRHXLHWGDXUNSOBGJM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYVAS3VBW75FNFOLW2XDBRG7ZZA"),"jndiConnFactoryLookupName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVX4M6YRW3NF5LFSDXAQMDDPY6E"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refXBMINQMSFJFZDK2KWYNYZO2YSQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col25LWK27XTVD3XAFUNUWWTB7ARU"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueKind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PCH7MSGLZFSNNDIFYF6WQLWDI"),"queueKind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJLBOWUHVZHMLNOEMS5JGK4Z2U"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsIXEVYI6PORHVNOJ2X4FJ4BGBE4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("JMS")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptionsMap-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7AIDU772ABDKDLZAV4TQEMLUQI"),"connFactoryOptionsMap",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueue.Producer.Jms:brokerAddress-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOPGPZBRRSZFCFP6DSBBECKFM74"),"brokerAddress",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGCAWRN7ELBAMTEZT4CLVLOXFW4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("not_required")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTRIHVJKCKNDNVESJUPLUFKPVQA"),"queueName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AHVKFJ3EJGRJHCZPA26JOKYRU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiQueueLookupName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNST2OY3VRBFTHZDJPS34GEHEU"),"jndiQueueLookupName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOVRNRKRBQNFCJHZ3V3MGJWMZPQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWXVEL5SYNRBVVP2ICAI7TF6DLI"),"getSender",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cacheItemId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN5SZ76QGTJDYVCCTDPI3CNGCOE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMHUN7KMMONHPNKHBVVHMAKTH4M"),"prepareMeta",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("meta",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA6BXU7G23RCYZGXG4CSLSXSBWI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("body",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5ROHOPA27JF55BPAYVDGQ7PUHE"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBGYR2KUAUNERPM2M3LOHS4Z3UA"),"getPartition",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("meta",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNCOIEIM44ZFGXDYI235V45E2NQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("body",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTNWQ6KNAYREVVGGDYDBX6ODZTI"))
								},org.radixware.kernel.common.enums.EValType.INT)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refXBMINQMSFJFZDK2KWYNYZO2YSQ")},
						null,null,null,false);
}

/* Radix::MessageQueue::MessageQueue.Producer.Jms - Desktop Executable*/

/*Radix::MessageQueue::MessageQueue.Producer.Jms-Application Class*/

package org.radixware.ads.MessageQueue.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms")
public interface MessageQueue.Producer.Jms   extends org.radixware.ads.MessageQueue.explorer.MessageQueue.Producer  {















































































	/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions:connFactoryOptions-Presentation Property*/


	public class ConnFactoryOptions extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ConnFactoryOptions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions:connFactoryOptions")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions:connFactoryOptions")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ConnFactoryOptions getConnFactoryOptions();
	/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName:connFactoryClassName-Presentation Property*/


	public class ConnFactoryClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ConnFactoryClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName:connFactoryClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName:connFactoryClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ConnFactoryClassName getConnFactoryClassName();
	/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName:jndiConnFactoryLookupName-Presentation Property*/


	public class JndiConnFactoryLookupName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public JndiConnFactoryLookupName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName:jndiConnFactoryLookupName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName:jndiConnFactoryLookupName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public JndiConnFactoryLookupName getJndiConnFactoryLookupName();


}

/* Radix::MessageQueue::MessageQueue.Producer.Jms - Desktop Meta*/

/*Radix::MessageQueue::MessageQueue.Producer.Jms-Application Class*/

package org.radixware.ads.MessageQueue.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageQueue.Producer.Jms_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
			"Radix::MessageQueue::MessageQueue.Producer.Jms",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIJMFWGNFU5AV3PP4PS3SDW3NKY"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHDLEWVSUNHFJL3YMXLWLE35DI"),null,null,0,

			/*Radix::MessageQueue::MessageQueue.Producer.Jms:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBAOT5IAUVFOJKJWMIMZ3STNDM"),
						"connFactoryClassName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWS4C2KMKBRGEFINCAJJYAXABCQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWSVD6WMBRARBCGP6U4BAQOM2Q"),
						"connFactoryOptions",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQJDLVFLLDJFSPENVP6DUD22XS4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:connFactoryOptions:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYVAS3VBW75FNFOLW2XDBRG7ZZA"),
						"jndiConnFactoryLookupName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVX4M6YRW3NF5LFSDXAQMDDPY6E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiConnFactoryLookupName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PCH7MSGLZFSNNDIFYF6WQLWDI"),
						"queueKind",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						63,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsIXEVYI6PORHVNOJ2X4FJ4BGBE4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("JMS"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsIXEVYI6PORHVNOJ2X4FJ4BGBE4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueue.Producer.Jms:brokerAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOPGPZBRRSZFCFP6DSBBECKFM74"),
						"brokerAddress",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						63,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("not_required"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:brokerAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTRIHVJKCKNDNVESJUPLUFKPVQA"),
						"queueName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AHVKFJ3EJGRJHCZPA26JOKYRU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						62,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:queueName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiQueueLookupName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNST2OY3VRBFTHZDJPS34GEHEU"),
						"jndiQueueLookupName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOVRNRKRBQNFCJHZ3V3MGJWMZPQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						62,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueue.Producer.Jms:jndiQueueLookupName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWRCU5UZMVNDOFJESEXOUPTAXKE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOHHGMQIHARERZNM5I6FK33DHFA")},
			true,true,false);
}

/* Radix::MessageQueue::MessageQueue.Producer.Jms - Web Meta*/

/*Radix::MessageQueue::MessageQueue.Producer.Jms-Application Class*/

package org.radixware.ads.MessageQueue.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageQueue.Producer.Jms_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
			"Radix::MessageQueue::MessageQueue.Producer.Jms",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIJMFWGNFU5AV3PP4PS3SDW3NKY"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHDLEWVSUNHFJL3YMXLWLE35DI"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::MessageQueue::MessageQueue.Producer.Jms:Edit - Desktop Meta*/

/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit-Editor Presentation*/

package org.radixware.ads.MessageQueue.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWRCU5UZMVNDOFJESEXOUPTAXKE"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7JNXRJQ3R5BDBMFTUG72M2JTXE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQZ2AJHN3PFDWXC5BT6I66OS5PQ"),
	null,
	null,

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Additional-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAMVYH3HK25A63OVVFY6XZWLH3A"),"Additional",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXBKMYTRLNRAU7KVPC77UAMOBYU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBAOT5IAUVFOJKJWMIMZ3STNDM"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWSVD6WMBRARBCGP6U4BAQOM2Q"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgP7KTV4VXUZEI7MKXIX5ER6DLLM"),0,0,1,false,false)
			},new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgP7KTV4VXUZEI7MKXIX5ER6DLLM"),"JNDI",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMO3Q4ZI7BVGA3CHBB2WFOFBGUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),false,true,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS6KAB3VU5RBUXA5HJVMUCA44VY"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDXGGXKN2HNC4NJ6JA35SXHXNCY"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYVAS3VBW75FNFOLW2XDBRG7ZZA"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNST2OY3VRBFTHZDJPS34GEHEU"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPESXTKOHY5BGBAXHNVMC5JUKCE"),0,4,1,false,false)
					})
			})
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgSSMUGGSOLNEENDZTYAGQ7AIZ5I")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAMVYH3HK25A63OVVFY6XZWLH3A")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN2YU55HZT5C5DFTUAJJYAOIW2Q")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6V4MMAXFZBDL5DFMVK7EVVGFUQ"))}
	,

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	101552,0,0);
}
/* Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model - Desktop Executable*/

/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model-Entity Model Class*/

package org.radixware.ads.MessageQueue.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model")
public class Edit:Model  extends org.radixware.ads.MessageQueue.explorer.MessageQueue.Producer.Jms.MessageQueue.Producer.Jms_DefaultModel.epr7JNXRJQ3R5BDBMFTUG72M2JTXE_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model:Properties-Properties*/

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model:Methods-Methods*/

	/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model:setVisibility-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model:setVisibility")
	public published  void setVisibility () {
		super.setVisibility();
		brokerAddress.setVisible(false);
	}


}

/* Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model - Desktop Meta*/

/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model-Entity Model Class*/

package org.radixware.ads.MessageQueue.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemWRCU5UZMVNDOFJESEXOUPTAXKE"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aem7JNXRJQ3R5BDBMFTUG72M2JTXE"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::MessageQueue::MessageQueue.Producer.Jms:Edit:Model:Properties-Properties*/
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

/* Radix::MessageQueue::MessageQueue.Producer.Jms:Create - Desktop Meta*/

/*Radix::MessageQueue::MessageQueue.Producer.Jms:Create-Editor Presentation*/

package org.radixware.ads.MessageQueue.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOHHGMQIHARERZNM5I6FK33DHFA"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWRCU5UZMVNDOFJESEXOUPTAXKE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUELGFWOND5C6ZOC23QQGSHYNIQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQZ2AJHN3PFDWXC5BT6I66OS5PQ"),
			null,
			null,
			null,
			null,

			/*Radix::MessageQueue::MessageQueue.Producer.Jms:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			105648,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.MessageQueue.explorer.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::MessageQueue::MessageQueue.Producer.Jms - Localizing Bundle */
package org.radixware.ads.MessageQueue.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageQueue.Producer.Jms - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns the default metadata settings for the given queue type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод возвращает настройки метаданных по умолчанию для своей очереди");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2IWVIFVIZ5G77EJH3EABXLOYR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Topic name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя топика");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AHVKFJ3EJGRJHCZPA26JOKYRU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Kafka-oriented sender");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отправщик сообщений Kafka");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls644UK5MIHRAFDBP66C6EIVJDPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Outbound Queue - JMS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исходящая очередь - JMS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHDLEWVSUNHFJL3YMXLWLE35DI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the message sender");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получить отправщик сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKFWMEYRECZFRLNN42EVSLTGCCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JNDI");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JNDI");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMO3Q4ZI7BVGA3CHBB2WFOFBGUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Topic lookup name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя топика для поиска");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOVRNRKRBQNFCJHZ3V3MGJWMZPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Custom options for connection factory");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительные опции фабрики соединений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQJDLVFLLDJFSPENVP6DUD22XS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connection factory lookup name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя фабрики соединений для поиска");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVX4M6YRW3NF5LFSDXAQMDDPY6E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connection factory class name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя класса фабрики соединений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWS4C2KMKBRGEFINCAJJYAXABCQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXBKMYTRLNRAU7KVPC77UAMOBYU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MessageQueue.Producer.Jms - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclUELGFWOND5C6ZOC23QQGSHYNIQ"),"MessageQueue.Producer.Jms - Localizing Bundle",$$$items$$$);
}

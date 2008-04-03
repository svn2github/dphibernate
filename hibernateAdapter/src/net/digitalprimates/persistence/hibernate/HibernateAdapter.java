/**
	Copyright (c) 2008. Digital Primates IT Consulting Group
	http://www.digitalprimates.net
	All rights reserved.
	
	This library is free software; you can redistribute it and/or modify it under the 
	terms of the GNU Lesser General Public License as published by the Free Software 
	Foundation; either version 2.1 of the License.

	This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
	without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
	See the GNU Lesser General Public License for more details.

	
	@author: Mike Nimer
	@ignore
 **/

package net.digitalprimates.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import net.digitalprimates.persistence.translators.SerializationFactory;

import flex.messaging.Destination;
import flex.messaging.config.ConfigMap;
import flex.messaging.messages.Message;
import flex.messaging.messages.RemotingMessage;
import flex.messaging.services.remoting.adapters.JavaAdapter;

@SuppressWarnings("unchecked")
public class HibernateAdapter extends JavaAdapter
{
	// private String scope = "request";
	protected Destination destination;

	/*
	 * static {
	 * PropertyProxyRegistry.getRegistry().register(HibernateProxy.class, new
	 * HibernateLazyPropertyProxy());
	 * PropertyProxyRegistry.getRegistry().register(AbstractPersistentCollection.class,
	 * new HibernateLazyCollectionProxy()); }
	 */

	private String property_hibernateSessionFactoryClass = "";// "net.digitalprimates.persistence.hibernate.tools.HibernateFactory";
	private String property_getCurrentSessionMethod = "";// "getCurrentSession";
	private String property_loadMethod = "";// "load";


	/**
	 * Initialize the adapter properties from the flex services-config.xml file
	 */
	public void initialize(String id, ConfigMap properties)
	{
		// super.initialize();
		if (properties == null || properties.size() == 0)
			return;

		// properties.getProperty("hibernateFactory");
		ConfigMap adapterProps = properties.getPropertyAsMap("hibernate", new ConfigMap());
		ConfigMap adapterHibernateProps = adapterProps.getPropertyAsMap("sessionFactory", new ConfigMap());
		property_hibernateSessionFactoryClass = adapterHibernateProps.getPropertyAsString("class", property_hibernateSessionFactoryClass);
		property_getCurrentSessionMethod = adapterHibernateProps.getPropertyAsString("getCurrentSessionMethod", property_getCurrentSessionMethod);

		ConfigMap destProps = properties.getPropertyAsMap("hibernate", new ConfigMap());
		property_loadMethod = destProps.getPropertyAsString("loadMethod", property_loadMethod);
	}


	/**
	 * Store the adapter properties in the local properties object
	 * 
	 * @param destination
	 * @param adapterSettings
	 * @param destinationSettings
	 * 
	 * public void setSettings(Destination destination, AdapterSettings
	 * adapterSettings, DestinationSettings destinationSettings) {
	 * //super.setSettings(destination, adapterSettings, destinationSettings);
	 *  // Second, initialize adapter level properties PropertiesSettings
	 * properties = adapterSettings; properties(properties);
	 *  // Third, initialize destination level properties properties =
	 * destinationSettings; properties(properties); }
	 * 
	 * 
	 * 
	 * protected void properties(PropertiesSettings propertiesSettings) {
	 * super.properties(propertiesSettings); }
	 */

	private String getLoadMethodName()
	{
		return property_loadMethod;
	}


	public Object superInvoke(Message message)
	{
		return super.invoke(message);
	}


	/**
	 * Invoke the Object.method() called through FlashRemoting
	 */
	public Object invoke(Message message)
	{
		Object results = null;

		if (message instanceof RemotingMessage)
		{
			// RemotingDestinationControl remotingDestination =
			// (RemotingDestinationControl)this.getControl().getParentControl();//destination;
			RemotingMessage remotingMessage = (RemotingMessage) message;

			// re-map our special "loadDpProxy" to the user defined hibernate
			// load method.
			if ("loadDPProxy".equals(remotingMessage.getOperation()))
			{
				try
				{
					remotingMessage.setOperation(getLoadMethodName());
					List paramArray = remotingMessage.getParameters();
					List args = new ArrayList();

					args.add(Class.forName(paramArray.get(1).getClass().getName()));
					args.add(paramArray.get(0));

					remotingMessage.setParameters(args);
				} catch (ClassNotFoundException ex)
				{
					ex.printStackTrace();
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}

			/*
			 * // Add support for the source="" attribute of the RemoteObject
			 * tag. This give developer the option // of using a single
			 * destination for all java calls, and defining the java class in
			 * their mxml // note: This can be turned off at the desination
			 * level by defined a <source/> other then "*" try { FactoryInstance
			 * factoryInstance = remotingDestination.getFactoryInstance();
			 * String className = factoryInstance.getSource();
			 *  // check for * wildcard in destination, and if exists use source
			 * defined in mxml if( "*".equals(className) ) { sourceClass =
			 * remotingMessage.getSource();
			 * factoryInstance.setSource(sourceClass); } }catch( Throwable ex ){
			 * ex.printStackTrace();}
			 */

			// Deserialize the incoming object data
			List inArgs = remotingMessage.getParameters();
			if (inArgs != null && inArgs.size() > 0)
			{
				try
				{
					Object o = SerializationFactory.getDeserializer(SerializationFactory.HIBERNATESERIALIZER).translate(this, (RemotingMessage) remotingMessage.clone(), getLoadMethodName(), property_hibernateSessionFactoryClass, property_getCurrentSessionMethod, inArgs);
					remotingMessage.setParameters((List) o);
					// remotingMessage.setBody(body);
				} catch (Exception ex)
				{
					ex.printStackTrace();
					// throw error back to flex
					// todo: replace with custom exception
					RuntimeException re = new RuntimeException(ex.getMessage());
					re.setStackTrace(ex.getStackTrace());
					throw re;
				}
			}

			// invoke the user class.method()
			results = super.invoke(remotingMessage);

			// serialize the result out
			try
			{
				results = SerializationFactory.getSerializer(SerializationFactory.HIBERNATESERIALIZER).translate(property_hibernateSessionFactoryClass, property_getCurrentSessionMethod, results);
			} catch (Exception ex)
			{
				ex.printStackTrace();
				// throw error back to flex
				// todo: replace with custom exception
				RuntimeException re = new RuntimeException(ex.getMessage());
				re.setStackTrace(ex.getStackTrace());
				throw re;
			}
		}

		return results;
	}

}

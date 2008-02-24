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

package net.digitalprimates.persistence.translators;

import net.digitalprimates.persistence.hibernate.HibernateAdapter;
import flex.messaging.messages.RemotingMessage;

/**
 * Interface for class that serialize the requests into Java
 * 
 * @author mike nimer
 */
public interface IDeserializer 
{
	Object translate(HibernateAdapter adapter, RemotingMessage message, String loadMethodName, String sessionFactoryClazz, String getSessionMethod, Object obj);
}

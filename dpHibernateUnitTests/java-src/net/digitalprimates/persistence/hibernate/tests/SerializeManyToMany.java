package net.digitalprimates.persistence.hibernate.tests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import flex.messaging.io.amf.ASObject;

import net.digitalprimates.persistence.hibernate.tests.manyToMany.M2MAddress;
import net.digitalprimates.persistence.hibernate.tests.manyToMany.M2MPerson;
import net.digitalprimates.persistence.hibernate.utils.HibernateUtil;
import net.digitalprimates.persistence.hibernate.utils.services.HibernateService;
import net.digitalprimates.persistence.translators.SerializationFactory;

@SuppressWarnings("unchecked")
public class SerializeManyToMany
{
    M2MPerson u1;
    
    
    @Before
    public void setUp() throws Exception
    {
    	// open session
    	HibernateUtil.getCurrentSession();
    	
        u1 = new M2MPerson();
        u1.id = UUID.randomUUID().toString();
        u1.firstName = "test 1";
        u1.lastName = "user 1";
        
        M2MAddress address1 = new M2MAddress();
        address1.person = u1;
        address1.address1 = "123 main st";
        address1.city = "Boston";
        address1.state = "MA";
        
        M2MAddress address2 = new M2MAddress();
        address2.person = u1;
        address2.address1 = "123 main st";
        address2.city = "Boston";
        address2.state = "MA";
        
        u1.addresses = new ArrayList();
        u1.addresses.add(address1);
        u1.addresses.add(address2);
        
        new HibernateService().save(u1);
    }
    

    @After
    public void tearDown() throws Exception
    {
        new HibernateService().delete(u1, true);
        
        HibernateUtil.closeSession();
    }
    

    @Test
    public void lazyManyToMany()
    {
    	
        Object user = new HibernateService().load(M2MPerson.class, u1.id);
        Assert.assertTrue(user instanceof M2MPerson);
    
        String sessionFactoryClazz = "net.digitalprimates.persistence.hibernate.utils.HibernateUtil";
        String method = "getCurrentSession";
        
        ASObject sUser = (ASObject) SerializationFactory.getSerializer(SerializationFactory.HIBERNATESERIALIZER).translate(sessionFactoryClazz, method, user);
        
        Assert.assertTrue(((Collection) sUser.get("addresses")).size() > 0);
        Assert.assertTrue(((Collection) sUser.get("addresses")).toArray()[0] instanceof ASObject);
        Assert.assertTrue(   "net.digitalprimates.persistence.hibernate.tests.manyToMany.M2MAddress".equals( ((ASObject) ((Collection) sUser.get("addresses")).toArray()[0]).getType()) );
    }
    
}

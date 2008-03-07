package net.digitalprimates.persistence.hibernate.tests.manyToMany;

import java.util.Collection;
import java.util.UUID;


@SuppressWarnings("unchecked")
public class M2MPerson
{
    public String id = UUID.randomUUID().toString();
    public String firstName;
    public String lastName;
    public M2MUserConnectInfo connectInfo;
    public Collection addresses;
    public Collection addresses2;
    
    
    public String getId()
    {
        return id;
    }
    

    public void setId(String id)
    {
        this.id = id;
    }
    

    public String getFirstName()
    {
        return firstName;
    }
    

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    

    public String getLastName()
    {
        return lastName;
    }
    

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    

    public M2MUserConnectInfo getConnectInfo()
    {
        return connectInfo;
    }
    

    public void setConnectInfo(M2MUserConnectInfo connectInfo)
    {
        this.connectInfo = connectInfo;
    }
    

    public Collection getAddresses()
    {
        return addresses;
    }
    

    public void setAddresses(Collection addresses)
    {
        this.addresses = addresses;
    }
    

    public Collection getAddresses2()
    {
        return addresses2;
    }
    

    public void setAddresses2(Collection addresses2)
    {
        this.addresses2 = addresses2;
    }
    
}

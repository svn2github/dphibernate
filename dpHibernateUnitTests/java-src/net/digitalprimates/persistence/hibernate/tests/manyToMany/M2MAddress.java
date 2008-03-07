package net.digitalprimates.persistence.hibernate.tests.manyToMany;


public class M2MAddress
{
    public String id;// = UUID.randomUUID().toString();
    public String address1;
    public String address2;
    public String city;
    public String state;
    public Integer zip;
    public M2MPerson person;
    
    
    public M2MPerson getPerson()
    {
        return person;
    }
    

    public void setPerson(M2MPerson user)
    {
        this.person = user;
    }
    

    public String getId()
    {
        return id;
    }
    

    public void setId(String id)
    {
        this.id = id;
    }
    

    public String getAddress1()
    {
        return address1;
    }
    

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }
    

    public String getAddress2()
    {
        return address2;
    }
    

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }
    

    public String getCity()
    {
        return city;
    }
    

    public void setCity(String city)
    {
        this.city = city;
    }
    

    public String getState()
    {
        return state;
    }
    

    public void setState(String state)
    {
        this.state = state;
    }
    

    public Integer getZip()
    {
        return zip;
    }
    

    public void setZip(Integer zip)
    {
        this.zip = zip;
    }
    
}

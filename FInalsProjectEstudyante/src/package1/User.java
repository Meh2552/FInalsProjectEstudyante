package package1;

public abstract class User 
{

    protected MainSystem system;
    protected UserRecord record;

    public User(MainSystem system, UserRecord record) 
    {
        this.system = system;
        this.record = record;
    }

    public String name() 
    { 
    	return record.getFullName(); 
    }
    
    public UserRecord.Role role() 
    {
    	return record.getRole(); 
    }

    public abstract void start();
    
}


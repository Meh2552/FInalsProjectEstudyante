package package1;

import java.util.*;

public class UserManager extends DocuHandler 
{

    @Override
    public String getFileName() 
    {
        return "users.txt";
    }

    public UserManager() 
    {
        super();
    }

    public void addUser(UserRecord record) 
    {
        append(record.toFileLine());
    }

    public boolean usernameExists(String username)
    {
        List<String> lines = read();
        
        for (String line : lines) 
        {
            String[] part = line.split(",");
            if (part.length > 0 && part[0].equals(username))
            {
            	return true;
            }
        }
        return false;
    }

    public boolean studentNumExists(String stNum) 
    {
        List<String> lines = read();
        
        for (String line : lines) 
        {
            String[] part = line.split(",");
            
            if (part.length == 5) 
            {
                if (part[3].equals(stNum))
                {
                	return true;
                }
            }
        }
        
        return false;
    }

    public UserRecord verify(String username, String password) 
    {
        List<String> lines = read();
        
        for (String line : lines) 
        {
            String[] part = line.split(",");
            
            if (part.length == 5) // student ito
            {
                if (part[0].equals(username) && part[1].equals(password)) 
                {
                    return new UserRecord(part[0], part[1], part[3], part[4]);
                }
            } 
            
            else if (part.length == 4) // employee ito
            { 
                if (part[0].equals(username) && part[1].equals(password)) 
                {
                    UserRecord.Role role = UserRecord.Role.valueOf(part[2]);
                    
                    return new UserRecord(part[0], part[1], role, part[3]);
                }
            }
        }
        return null;
    }

    public void deleteUser(String username) 
    {
        List<String> lines = read();
        
        ArrayList<String> out = new ArrayList<String>();
        
        for (String line : lines) 
        {
            String[] part = line.split(",");
            
            if (part.length > 0 && part[0].equals(username)) 
            {
                continue; 
            }
            out.add(line);
        }
        write(out);
    }

    public List<UserRecord> loadAll() 
    {
        List<String> lines = read();
        
        ArrayList<UserRecord> list = new ArrayList<UserRecord>();
        
        for (String line : lines) 
        {
            String[] part = line.split(",");
            
            if (part.length == 5) 
            {
                list.add(new UserRecord(part[0], part[1], part[3], part[4]));
            } 
            
            else if (part.length == 4) 
            {
                UserRecord.Role role = UserRecord.Role.valueOf(part[2]);
                
                list.add(new UserRecord(part[0], part[1], role, part[3]));
            }
        }
        return list;
    }
}


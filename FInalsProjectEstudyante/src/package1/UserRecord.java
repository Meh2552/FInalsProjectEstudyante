package package1;

public class UserRecord 
{

    public enum Role 
    {
        STUDENT, CASHIER, REGISTRAR, ACCOUNTING, ADMIN
    }

    private String username;
    private String password;
    private Role role;
    private String fullName;
    private String studentNum;


    public UserRecord(String username, String password, String studentNum, String fullName) 
    {
        this.username = username;
        this.password = password;
        this.role = Role.STUDENT;
        this.studentNum = studentNum;
        this.fullName = fullName;
    }


    public UserRecord(String username, String password, Role role, String fullName)
    {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.studentNum = null;
    }


    public String getUsername() 
    { 
    	return username;
    }
    
    public String getPassword()
    { 
    	return password; 
    }
    
    public Role getRole() 
    { 
    	return role; 
    }
    
    public String getFullName()
    {
    	return fullName;
    }
    
    public String getStudentNum()
    { 
    	return studentNum;
    }

    public String toFileLine() 
    {
        if (role == Role.STUDENT) 
        {
            return username + "," + password + ",STUDENT," + studentNum + "," + fullName;
        } 
        
        else 
        {
            return username + "," + password + "," + role.toString() + "," + fullName;
        }
    }
}

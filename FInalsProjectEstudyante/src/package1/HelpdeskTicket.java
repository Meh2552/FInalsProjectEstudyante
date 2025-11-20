package package1;

public class HelpdeskTicket 
{
    private int id;
    private String studentNum;
    private String issue;
    private String status;

    public HelpdeskTicket(int id, String studentNum, String issue, String status) 
    {
        this.id = id;
        this.studentNum = studentNum;
        this.issue = issue;
        this.status = status;
    }

    public int getId() 
    { 
    	return this.id; 
    }
    
    public String getStudentNum() 
    {
    	return this.studentNum; 
    }
    
    public String getIssue()
    {
    	return this.issue; 
    }
    
    public String getStatus() 
    {
    	return this.status; 
    }
    
    public void setStatus(String status)
    { 
    	this.status = status;
    }

    public String toFileLine()
    {
        return id + "," + studentNum + "," + issue + "," + status;
    }

    public static HelpdeskTicket fromLine(String line) 
    {
        String[] part = line.split(",", 4);
        
        if (part.length < 4)
        {
        	return null;
        }
        int id = 0;
        
        try 
        {
        	id = Integer.parseInt(part[0]); 
        }
        
        catch (Exception e)
        {
        	id = 0; 
        }
        
        return new HelpdeskTicket(id, part[1], part[2], part[3]);
    }
}

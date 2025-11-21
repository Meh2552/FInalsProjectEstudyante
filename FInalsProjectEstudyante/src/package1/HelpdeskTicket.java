package package1;

public class HelpdeskTicket 
{
    private int id;
    private String studentNum;
    private String issue;
    private String status;
    private String date;

    public HelpdeskTicket(int id, String studentNum, String issue, String status, String date) 
    {
        this.id = id;
        this.studentNum = studentNum;
        this.issue = issue;
        this.status = status;
        this.date = date;
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
    
    public String getDate()
    {
    	return this.date;
    }
    
    public void setStatus(String status)
    { 
    	this.status = status;
    }

    public String toFileLine()
    {
    	String safeIssue = this.issue.replace(",", "|");
        return id + "," + studentNum + "," + safeIssue + "," + status +  "," + date;
    }

    public static HelpdeskTicket fromLine(String line) 
    {
        String[] part = line.split(",", 5);
        
        if (part.length < 5)
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
        
        return new HelpdeskTicket(id, part[1], part[2], part[3], part[4]);
    }
}

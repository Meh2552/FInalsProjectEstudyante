package package1;

public class HelpdeskTicket 
{
    private int id;
    private String studentNum;
    private String issue;
    private String status;
    private String assignedWindow;
    private String date;
    

    public HelpdeskTicket(int id, String studentNum, String issue, String status, String assignedWindow, String date)
    {
        this.id = id;
        this.studentNum = studentNum;
        this.issue = issue;
        this.status = status;
        this.assignedWindow = assignedWindow;
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
    
    public String getAssignedWindow() 
    {
    	return this.assignedWindow; 
    }
    
    public void setStatus(String status)
    { 
    	this.status = status;
    }

    public void setAssignedWindow(String window) 
    { 
    	this.assignedWindow = window; 
    }
    
    public void setDate(String date)
    {
    	this.date = date;
    }
    
    public String toFileLine()
    {
        String safeIssue = this.issue.replace(",", "|");
        return this.id + "," + this.studentNum + "," + safeIssue + "," + this.status + "," + this.assignedWindow + "," + this.date;
    }

    public static HelpdeskTicket fromLine(String line)
    {

        String[] part = line.split(",", 6);
        if (part.length < 6) 
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
        
        String studentNum = part[1];
        String issue = part[2].replace("|", ",");
        String status = part[3];
        String window = part[4];
        String date = part[5];

        return new HelpdeskTicket(id, studentNum, issue, status, window, date);
    }
}

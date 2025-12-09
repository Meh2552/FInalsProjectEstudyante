package package1;

public class HelpdeskTicket 
{
    private int id;
    private String studentNum;
    private String issue;
    private String status;
    private String date;
    private String assignedWindow;

    public HelpdeskTicket(int id, String studentNum, String issue, String status, String date, String assignedWindow)
    {
        this.id = id;
        this.studentNum = studentNum;
        this.issue = issue;
        this.status = status;
        this.date = date;
        this.assignedWindow = assignedWindow;
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
    
    public String toFileLine()
    {
        String safeIssue = this.issue.replace(",", "|");
        String aw = (assignedWindow == null) ? "" : assignedWindow;
        return id + "," + studentNum + "," + safeIssue + "," + status + "," + date + "," + aw;
    }

    public static HelpdeskTicket fromLine(String line)
    {

        String[] part = line.split(",", 6);
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
        
        String studentNum = part[1];
        String issue = part[2].replace("|", ",");
        String status = part[3];
        String date = part[4];
        String aw = "";
        
        if (part.length >= 6)
        {
        	aw = part[5];
        }

        return new HelpdeskTicket(id, studentNum, issue, status, date, aw);
    }
}

package package1;

public class HelpdeskTicket 
{
    private int id;
    private String studentNum;
    private String issue;
    private String status;
    private String assignedWindow;
    private String date;
    private String endorsedTo;

    

    public HelpdeskTicket(int id, String studentNum, String issue, String status, String date, String assignedWindow)
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
    
    public String getEndorsedTo() 
    { 
    	return endorsedTo; 
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
    
    public void setEndorsedTo(String endorsed) 
    {
    	this.endorsedTo = endorsed; 
    }

    
    public String toFileLine()
    {
        String safeIssue = this.issue.replace(",", "|");
        return id + "," + studentNum + "," + safeIssue + "," + status + "," + assignedWindow + "," + date + "," + endorsedTo;
    }
    

    public static HelpdeskTicket fromLine(String line)
    {
        String[] part = line.split(",", 7);
        
        if (part.length < 6) return null;
        
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
        
        String assignedWindow;
        if (part.length > 4) {
        	
            assignedWindow = part[4];
        } 
        else 
        {
            assignedWindow = "";
        }
        
        String date;
        if (part.length > 5) 
        {
            date = part[5];
        } 
        else 
        {
            date = "";
        }
        
        String endorsedTo;
        if (part.length > 6) {
            endorsedTo = part[6];
        } else {
            endorsedTo = "";
        }
        

        return new HelpdeskTicket(id, studentNum, issue, status, date, assignedWindow);
    }
}
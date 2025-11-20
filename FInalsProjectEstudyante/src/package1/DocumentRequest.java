package package1;

public class DocumentRequest 
{
    private String username;
    private String studentNum;
    private String docName;
    private String status;

    public DocumentRequest(String username, String studentNum, String docName, String status) 
    {
        this.username = username;
        this.studentNum = studentNum;
        this.docName = docName;
        this.status = status;
    }

    public String getUsername() 
    {
    	return username; 
    }
    
    public String getStudentNum() 
    { 
    	return studentNum; 
    }
    
    public String getDocName() 
    {
    	return docName; 
    }
    
    public String getStatus() 
    {
    	return status; 
    }
    
    public void setStatus(String s) 
    { 
    	this.status = s; 
    }

    public String toFileLine() 
    {
        return username + "," + studentNum + "," + docName + "," + status;
    }

    public static DocumentRequest fromLine(String line) 
    {
        String[] p = line.split(",", 4);
        if (p.length < 4)
        {
        	return null;
        }
        return new DocumentRequest(p[0], p[1], p[2], p[3]);
    }
}


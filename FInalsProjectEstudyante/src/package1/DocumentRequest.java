package package1;

public class DocumentRequest 
{
    private String username;
    private String studentNum;
    private String docName;
    private String status;

    private String date;

    public DocumentRequest(String username, String studentNum, String docName, String status, String date) 
    {
        this.username = username;
        this.studentNum = studentNum;
        this.docName = docName;
        this.status = status;
        this.date = date;

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

    public String getDate() {
        return date;
    }

    public String toFileLine() 
    {
        return username + "," + studentNum + "," + docName + "," + status + "," + date;
    }

    public static DocumentRequest fromLine(String line) 
    {
        String[] p = line.split(",", 5);
        if (p.length < 5)
        {
        	return null;
        }
        return new DocumentRequest(p[0], p[1], p[2], p[3], p[4]);
    }
}


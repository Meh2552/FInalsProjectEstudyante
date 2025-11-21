package package1;

public class DocumentRequest 
{
    private String username;
    private String studentNum;
    private String docName;
    private String status;

    private String date;
    private String id;

    public DocumentRequest(String username, String studentNum, String docName, String status, String date, String id) 
    {
        this.username = username;
        this.studentNum = studentNum;
        this.docName = docName;
        this.status = status;
        this.date = date;
        this.id = id;

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

    public String getId() {
        return id;
    }

    public String toFileLine() 
    {
        return username + "," + studentNum + "," + docName + "," + status + "," + date + "," + id;
    }

    public static DocumentRequest fromLine(String line) 
    {
        String[] p = line.split(",", 6);
        if (p.length < 5)
        {
        	return null;
        }
        return new DocumentRequest(p[0], p[1], p[2], p[3], p[4], p[5]);
    }
}


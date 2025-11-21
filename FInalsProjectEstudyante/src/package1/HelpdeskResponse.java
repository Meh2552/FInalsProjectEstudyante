package package1;

public class HelpdeskResponse 
{
    private int ticketId;
    private String respond;
    private String message;
    private String timestamp;

    public HelpdeskResponse(int ticketId, String respond, String message, String timestamp) 
    {
        this.ticketId = ticketId;
        this.respond = respond;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getTicketId()
    { 
    	return this.ticketId;
    }
    
    public String getRespond() 
    { 
    	return this.respond; 
    }
    
    public String getMessage() 
    { 
    	return this.message; 
    }
    
    public String getTime()
    {
    	return this.timestamp;
    }

    public String toFileLine() 
    {
    	String safeMessage = this.message.replace(",", "|");
        return this.ticketId + "," + this.respond + "," + safeMessage + "," + this.timestamp;
    }

    public static HelpdeskResponse fromLine(String line) 
    {
        String[] p = line.split(",", 4);
        
        if (p.length < 4) 
        {
        	return null;
        }
        
        int id = 0;
        
        try 
        {
        	id = Integer.parseInt(p[0]); 
        } 
        
        catch (Exception e) 
        { 
        	id = 0;
        }
        
        String responder = p[1];
        String msg = p[2].replace("|", ",");
        String tStamp = p[3];
        
        return new HelpdeskResponse(id, responder, msg, tStamp);
    }
}


package package1;

public class HelpdeskResponse 
{
    private int ticketId;
    private String respond;
    private String message;
    private String timestamp;
    private int rating;

    public HelpdeskResponse(int ticketId, String responder, String message, String timestamp)
    {
        this(ticketId, responder, message, timestamp, -1);
    }

    public HelpdeskResponse(int ticketId, String respond, String message, String timestamp, int rating)
    {
        this.ticketId = ticketId;
        this.respond = respond;
        this.message = message;
        this.timestamp = timestamp;
        this.rating = rating;
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
    
    public int getRating() 
    {
    	return rating; 
    }

    public void setRating(int rating) 
    {
    	this.rating = rating; 
    }

    public String toFileLine()
    {
        String safeMessage = this.message.replace(",", "|");
        return this.ticketId + "," + this.respond + "," + safeMessage + "," + this.timestamp + "," + this.rating;
    }

    public static HelpdeskResponse fromLine(String line)
    {
        String[] p = line.split(",", 5);
        if (p.length < 4) return null;

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
        int rating = -1;
        
        if (p.length >= 5) 
        {
            try 
            { 
            	rating = Integer.parseInt(p[4]); 
            } 
            catch (Exception e) 
            { 
            	rating = -1; 
            }
        }
        return new HelpdeskResponse(id, responder, msg, tStamp, rating);
    }
}
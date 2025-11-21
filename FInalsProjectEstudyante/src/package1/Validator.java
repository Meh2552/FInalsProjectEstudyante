package package1;

import java.util.InputMismatchException;

public class Validator 
{

    private final MainSystem system;

    public Validator(MainSystem system) 
    {
        this.system = system;
    }

    
    public boolean confirm(String prompt) 
    {
        while (true) 
        {
            System.out.print(prompt + " (Y/N): ");
            String response = system.scan().nextLine().trim();
            
            if (response.equalsIgnoreCase("y"))
            {
            	return true;
            }
            
            if (response.equalsIgnoreCase("n"))
            {
            	return false;
            }
            System.out.println("Please enter Y or N.");
        }
    }

   
    public int menuChoice(String prompt, int max) //para di sumobra
    {
        while (true) 
        {
            System.out.print(prompt);
            try 
            {
                int choices = system.scan().nextInt();
                system.scan().nextLine();
                
                if (choices >= 1 && choices <= max)
                {
                	return choices;
                }
                
                System.out.println("Choose between 1 and " + max);
            }
            catch (InputMismatchException e) 
            {
                System.out.println("Invalid input. Enter a number.");
                system.scan().nextLine();
            }
        }
    }

    public int requireInt(String prompt) // para sa integer types
    {
        while (true) 
        {
            System.out.print(prompt);
            try 
            {
                int input = system.scan().nextInt();
                system.scan().nextLine();
                return input;
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Invalid number. Try again.");
                system.scan().nextLine();
            }
        }
    }

    public String requireText(String prompt) // para sa text
    {
        while (true) 
        {
            System.out.print(prompt);
            String input = system.scan().nextLine().trim();
            
            if (!input.isEmpty())
            {
            	return input;
            }
            
            System.out.println("Input cannot be empty.");
        }
    }

    public String editCancelContinue()
    {
        while (true) 
        {
            System.out.print("PRESS [E]dit [C]ancel [N]ext: ");
            String s = system.scan().nextLine().trim();
            
            if (s.equalsIgnoreCase("e"))
            {
            	return "EDIT";
            }
            
            if (s.equalsIgnoreCase("c"))
            {
            	return "CANCEL";
            }
            
            if (s.equalsIgnoreCase("n"))
            {
            	return "CONTINUE";
            }
            
            System.out.println("Invalid. Type E, C or N.");
        }
    }
}



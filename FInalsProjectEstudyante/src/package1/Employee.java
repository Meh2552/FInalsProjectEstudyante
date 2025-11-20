package package1;

public abstract class Employee extends User
{

    public Employee(MainSystem system, UserRecord record) 
    {
        super(system, record);
    }

    public abstract void employeeMenu();

    @Override
    public void start() 
    {
        employeeMenu();
    }
}

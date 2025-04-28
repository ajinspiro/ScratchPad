using System.Data.SQLite;

namespace Workflow;

public class DBConnection(string connectionString)
{
    private readonly string connectionString = connectionString;
    public bool BeginTransaction()
    {
        Console.WriteLine("BEGIN TRANSACTION........");
        return true;
    }
    public bool ExecuteQuery(string query, Dictionary<string, object> parameters)
    {
        SQLiteConnection connection = new(connectionString);
        using var command = new SQLiteCommand(query, connection);
        parameters.ToList().ForEach(p =>
        {
            command.Parameters.AddWithValue(p.Key, p.Value);
        });
        connection.Open();
        command.ExecuteNonQuery();
        return true;
    }
    public bool EndTransaction()
    {
        Console.WriteLine("END TRANSACTION...........");
        return true;
    }
}
public class COMPUTATION_CONTEXT
{
    public Dictionary<string, object> Symbols { get; set; } = [];
}
public interface IComputationCommand
{
    public bool PreExecute(COMPUTATION_CONTEXT context);
    public bool Execute(COMPUTATION_CONTEXT context);
    public bool PostExecute(COMPUTATION_CONTEXT context);
}
public class BaseContract : IComputationCommand
{
    private List<BaseContract> children = [];
    public List<BaseContract> Children => children;

    public virtual bool PreExecute(COMPUTATION_CONTEXT context)
    {
        return true;
    }
    public virtual bool Execute(COMPUTATION_CONTEXT context)
    {
        foreach (var contract in children)
        {
            if (!contract.PreExecute(context))
                return false;
            if (!contract.Execute(context))
                return false;
            if (!contract.PostExecute(context))
                return false;
        }
        return true;
    }
    public virtual bool PostExecute(COMPUTATION_CONTEXT context)
    {
        return true;
    }
}
public static class Constants
{
    public static string DBConnection => nameof(DBConnection);
}
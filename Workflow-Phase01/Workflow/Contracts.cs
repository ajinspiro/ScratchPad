namespace Workflow;

public class AddGroup : BaseContract
{
    private static readonly string insertCommand = "INSERT INTO Groups (Name) VALUES(@groupName)";

    public override bool Execute(COMPUTATION_CONTEXT context)
    {
        DBConnection? db = context.Symbols[Constants.DBConnection] as DBConnection;
        if (db is null)
        {
            throw new ArgumentException();
        }
        string groupName = context.Symbols["groupName"] as string ?? throw new Exception();
        Dictionary<string, object> parameters = new()
        {
            { "groupName", groupName }
        };
        if (!db.ExecuteQuery(insertCommand, parameters))
            return false;
        return base.Execute(context);
    }
}

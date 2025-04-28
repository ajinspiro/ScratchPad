using Workflow;

BaseContract baseContract = new();
baseContract.Children.Add(new AddGroup());

DBConnection db = new("Data source=Workflow.db");
COMPUTATION_CONTEXT context = new COMPUTATION_CONTEXT();
context.Symbols[Constants.DBConnection] = db;
context.Symbols["groupName"] = "Family";
baseContract.Execute(context);
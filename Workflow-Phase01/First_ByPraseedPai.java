//////////////////////////////////////
//
// A Simple Java Program to prototype composible 
// contracts against a single resource manager ( Database )
// In future, we need to extend this stuff to work against
// multiple resource managers with Isomorphism enabled for undo/redo
//
import java.lang.*;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
////////////////////////
// A Mock Connection Object...In future,we can 
// replace it with a JDBC connection wrapper
//
class MockConnection{
	//--------------Connection String 
	private String m_connstr;
	//------------- CTOR
	public MockConnection(String connstr ) {
		m_connstr = connstr;
	}
	//----------- Mock Start Transaction
	boolean BeginTransaction() {
		System.out.println("Begin Transaction................");
		return true;
	}
	//--------- Execute Query represents a Query Execution
	//--------- For elaborate processing,we need to return
	//--------- result set as well
	boolean ExecuteQuery(String query ) {
		System.out.println(query);
		return true;
	}
	//------------ End Transaction
	boolean EndTransaction() {
		System.out.println("End Transaction................");
		return true;
	}
}
///////////////////////
//
// A Bag for storing data 
// Parameters are passed from the Host
// to contracts through COMPUTATION_CONTEXT
//
class COMPUTATION_CONTEXT {
	//------------ Data Store
    private HashMap<String, Object> symbols = new HashMap<String, Object>();
	//------ Store data
    public void put(String k, Object v) {
        symbols.put(k, v);
    }
	//---------- retreive data
    public Object get(String k) {
        return symbols.get(k);
    }
}
///////////////////////////////////////
//--------------------------- Command 
// This Interface implements Design By Contract Idiom (DBC)
// Unlike Bertrand Meyer and MS implementation, this one is 
// stateless
interface IComputationCommand {
   public boolean PreExecute(COMPUTATION_CONTEXT ctx);
   public boolean Execute(COMPUTATION_CONTEXT ctx);
   public boolean PostExecute(COMPUTATION_CONTEXT ctx);
}
/////////////////////////////
// A Root class for Composing contracts
//
//
class BaseContract implements IComputationCommand {
	//-------------------- CTOR
	public BaseContract(){
		m_children = new ArrayList<BaseContract>();
	}
	//---------------- Transaction Boundary and Execution
	//---------------- Starts here
	public boolean ExecuteTransaction(COMPUTATION_CONTEXT ctx) {
		//---- Retrieve the Connection Object
		MockConnection obj = (MockConnection)ctx.get("connobj"); 
		//--- Begins Transaction
		obj.BeginTransaction(); 
		//--------- Start Execution of the Contracts
		Execute(ctx);
		//---- End Transaction
		obj.EndTransaction();
		return true;
	}
	//////////////////////////////
	// An accessor function for returning child Contracts
	//
	public List<BaseContract> GetChildren() { return m_children; }
	//----------Does not do much
    public boolean PreExecute(COMPUTATION_CONTEXT ctx) {
        return true;
    }
  	//--------------- Actual Execution of the Stuff starts here
    public boolean Execute(COMPUTATION_CONTEXT ctx) {
		//------ Iterate through the Children and Calls 
		//---- PreExecute, Execute and PostExecute
        for( BaseContract bs : m_children ){
			if ( !bs.PreExecute(ctx) )
				return false; // Terminate Processing
			bs.Execute(ctx);
			bs.PostExecute(ctx);
		}
        return true;
    }
	//------------- Post Execute 
    public boolean PostExecute(COMPUTATION_CONTEXT ctx) {
        return true;
    }
	//------------ List of Child Contracts
	private List<BaseContract> m_children = null;
}

////////////////////////////////////
// Contracts for ContactGroup
//
class AddContactGroup extends BaseContract{
      public boolean Execute(COMPUTATION_CONTEXT ctx) {
		int indent = (int) ctx.get("indent");
        MockConnection con = (MockConnection)ctx.get("connobj");
		con.ExecuteQuery(indent + "  " +"Added Contact Group.....");
		ctx.put("indent",indent+3);
		super.Execute(ctx);
		ctx.put("indent",indent);
        return true;
	  }

}
class UpdateContactGroup extends BaseContract{
	public boolean Execute(COMPUTATION_CONTEXT ctx) {
		int indent = (int) ctx.get("indent");
		MockConnection con = (MockConnection)ctx.get("connobj");
        con.ExecuteQuery(indent + "  " +"Update Contact Group.....");
		ctx.put("indent",indent+3);
		super.Execute(ctx);
		ctx.put("indent",indent);
        return true;
    }
}
class DeleteContactGroup extends BaseContract{
	public boolean Execute(COMPUTATION_CONTEXT ctx) {
		int indent = (int) ctx.get("indent");
		MockConnection con = (MockConnection)ctx.get("connobj");
        con.ExecuteQuery(indent + "  " +"Delete Contact Group.....");
		ctx.put("indent",indent+3);
		super.Execute(ctx);
		ctx.put("indent",indent);
        return true;
	}

}
///////////////////////////////////
// Contracts for Contacts
//
class AddContacts extends BaseContract{
	public boolean Execute(COMPUTATION_CONTEXT ctx) {
		int indent = (int) ctx.get("indent");
		MockConnection con = (MockConnection)ctx.get("connobj");
        con.ExecuteQuery(indent + "  " +"Added Contacts.....");
		ctx.put("indent",indent+3);
		super.Execute(ctx);
		ctx.put("indent",indent);
        return true;
	}
}
class UpdateContacts extends BaseContract{
	public boolean Execute(COMPUTATION_CONTEXT ctx) {
		int indent = (int) ctx.get("indent");
		MockConnection con = (MockConnection)ctx.get("connobj");
        con.ExecuteQuery(indent + "  " +"Update Contacts.....");
		ctx.put("indent",indent+3);
		super.Execute(ctx);
		ctx.put("indent",indent);
        return true;
    }
}

class DeleteContacts extends BaseContract{
	public boolean Execute(COMPUTATION_CONTEXT ctx) {   
		int indent = (int) ctx.get("indent");
		MockConnection con = (MockConnection)ctx.get("connobj");
        con.ExecuteQuery(indent + "  " +"Delete Contacts.....");
		ctx.put("indent",indent+3);
		super.Execute(ctx);
		ctx.put("indent",indent);
        return true;
      }
}

////////////////////////////////////////////////
//
// EntryPoint
//
class First {
	public static void main(String [] argv ) throws Exception {
	//----------- Create a Root Contract
	BaseContract root = new BaseContract();
	List<BaseContract> child = root.GetChildren();
	AddContactGroup ag = new AddContactGroup();
	child.add(ag);
	//-------------- Add Contracts a Hierarchy
	ag.GetChildren().add(new AddContacts());
	DeleteContacts ds = new DeleteContacts();
	ds.GetChildren().add(new UpdateContactGroup());
	ag.GetChildren().add(ds);
	root.GetChildren().add(new UpdateContacts());
	/*

                    <BaseContract>
			<AddContactGroup  vo = "$CONTACT_GROUPVO" >
				<AddContacts vo = "$CONTACT_VO" >
				<DeleteContact  id="id" >
					<UpdateDeleteContactGroup   />
				</DeleteContact>
			</AddContactGroup>
			
			</UpdateContacts  />

		    </BaseContract>

		BaseContract graph = LoadFrom("updatecontacts");
		command language
		----------------
		BASECONTRACT "contractname"
		AddContactGroup { CONTACT_GROUPVO }        DeleteContractgrou[ {CONTACT_GROPVO.id }
		AddContacts { CONTACT_VO }		   DeleteContacts { CONTACT_VO.id }
		DeleteContact	{id}			   
		UpdateDeleteContractGroup
		UpdateContacts 

		Platform
			Services { Technical }
				Solutions (Vertical Domain)

         */
	
	int indent = 3;
	MockConnection m_con = new MockConnection("DB=CONTACTS;DRIVER=SQLITE");
	//---- Initialize the Context Object 
	COMPUTATION_CONTEXT cmdbag = new COMPUTATION_CONTEXT();
  	cmdbag.put("indent",indent);
	cmdbag.put("connobj",m_con);
	//-------- Start Orchastrating the Contracts 
	root.ExecuteTransaction(cmdbag);
}
}
package query;

import global.Minibase;
import global.RID;
import parser.AST_Insert;
import parser.ParseException;
import heap.HeapFile;
import relop.Schema;
import relop.Tuple;

/**
 * Execution plan for inserting tuples.
 */
class Insert implements Plan {

	protected String fileName;
	protected Schema schema;
	protected Object[] values;


  /**
   * Optimizes the plan, given the parsed query.
   * 
   * @throws QueryException if table doesn't exists or values are invalid
   */
  public Insert(AST_Insert tree) throws QueryException {

    fileName = tree.getFileName();
    QueryCheck.tableExists(fileName);

		schema = Minibase.SystemCatalog.getSchema(fileName);

		values = tree.getValues();
		QueryCheck.insertValues(schema, values);

  }


  /**
   * Executes the plan and prints applicable output.
   */
  public void execute() {

		HeapFile hf = new HeapFile(fileName);

		Tuple tuple = new Tuple(schema, values);

		RID rid = hf.insertRecord(tuple.getData());

//		tuple.print();
    System.out.println("Tuple Inserted.");
  }

}

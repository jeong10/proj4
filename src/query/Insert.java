package query;

import global.*;
import parser.AST_Insert;
import parser.ParseException;
import heap.HeapFile;
import index.HashIndex;
import relop.*;

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

		//	if the table has index, add index of the tuple
		IndexDesc[] indexes = Minibase.SystemCatalog.getIndexes(fileName);

		if (indexes != null) {
System.out.println("index in table " + fileName);

			for (IndexDesc index : indexes) {
		    HashIndex hi = new HashIndex(index.indexName);

				SearchKey key = new SearchKey(tuple.getField(index.columnName));

				hi.insertEntry(key, new RID());
System.out.println("create index; key=" + tuple.getField(index.columnName));
			}
		}

		RID rid = hf.insertRecord(tuple.getData());

//		tuple.print();
    System.out.println("Tuple Inserted.");
  }

}

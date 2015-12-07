package query;

import global.*;
import relop.*;
import parser.AST_Delete;
import parser.ParseException;
import heap.HeapFile;

/**
 * Execution plan for deleting tuples.
 */
class Delete implements Plan {

	protected String fileName;
	protected Schema schema;
	protected Predicate[][] preds;
	protected Selection select;


  /**
   * Optimizes the plan, given the parsed query.
   * 
   * @throws QueryException if table doesn't exist or predicates are invalid
   */
  public Delete(AST_Delete tree) throws QueryException {

    fileName = tree.getFileName();
    QueryCheck.tableExists(fileName);

		schema = Minibase.SystemCatalog.getSchema(fileName);

		preds = tree.getPredicates();
		QueryCheck.predicates(schema, preds);

  }

  /**
   * Executes the plan and prints applicable output.
   */
  public void execute() {

		HeapFile hf = new HeapFile(fileName);
		FileScan fs = new FileScan(schema, hf);
		RID dummy = new RID();

		for (Predicate[] pred : preds) {
			select = new Selection(fs, pred);

			select.restart();

			while (select.hasNext()) {
				Tuple currTuple = select.getNext();
				byte[] data = currTuple.getData();

				while (fs.hasNext()) {
					Tuple checkTuple = fs.getNext();

					//	fix?
					if (currTuple.getData() == checkTuple.getData()) {
currTuple.print();
						hf.deleteRecord(dummy);

System.out.println("Tuple to delete: " + currTuple.getData());
						break;
					}
				}
			}
		}

    System.out.println("Tuple deleted.");

  }

}

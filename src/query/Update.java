package query;

import global.*;
import relop.*;
import parser.AST_Update;
import parser.ParseException;
import heap.HeapFile;

/**
 * Execution plan for updating tuples.
 */
class Update implements Plan {

	protected String fileName;
	protected Schema schema;
	protected String[] columns;
	protected Object[] values;
	protected Predicate[][] preds;
	protected Selection select;


  /**
   * Optimizes the plan, given the parsed query.
   * 
   * @throws QueryException if invalid column names, values, or pedicates
   */
  public Update(AST_Update tree) throws QueryException {

    fileName = tree.getFileName();
    QueryCheck.tableExists(fileName);

		schema = Minibase.SystemCatalog.getSchema(fileName);

		columns = tree.getColumns();
		for (int i=0; i<columns.length; i++)
			QueryCheck.columnExists(schema, columns[i]);

		preds = tree.getPredicates();
		QueryCheck.predicates(schema, preds);

		values = tree.getValues();
		QueryCheck.updateFields(schema, columns);

		// check for valid update values

  }


  /**
   * Executes the plan and prints applicable output.
   */
  public void execute() {

//UPDATE Students SET sid = 5 WHERE name = 'Chris';		

		HeapFile hf = new HeapFile(fileName);
		FileScan fs = new FileScan(schema, hf);
		RID dummy = new RID();

		for (Predicate[] pred : preds) {
			select = new Selection(fs, pred);

			select.restart();

			while (select.hasNext()) {
				Tuple currTuple = select.getNext();
				byte[] data = currTuple.getData();

				fs.restart();

				while (fs.hasNext()) {
					Tuple checkTuple = fs.getNext();

//	check if both tuples are equal
boolean eq = true;
for (int i=0; i<checkTuple.getAllFields().length; i++) {

	if (checkTuple.getStringFld(i) != null) {
		if (!checkTuple.getStringFld(i).equals(currTuple.getStringFld(i))) {
			eq = false;
		}
	}
	else if (checkTuple.getField(i) != currTuple.getField(i)) {
		eq = false;
break;
	}
}

					if (eq) {
						for (int i=0; i<columns.length; i++) {
							checkTuple.setField(columns[i], values[i]);
//System.out.println("updating " + fileName + " at: " + columns[i] + " with " + values[i]);
						}

						//	update index?
						break;
					}
				}
			}
		}



    System.out.println("Tuple Updated.");

  }

}

package query;

import global.Minibase;
import index.HashIndex;
import parser.AST_CreateIndex;
import parser.ParseException;
import relop.Schema;


/**
 * Execution plan for creating indexes.
 */
class CreateIndex implements Plan {

	protected String fileName;
	protected String iColumn;
	protected String iTable;

  protected Schema schema;


  /**
   * Optimizes the plan, given the parsed query.
   * 
   * @throws QueryException if index already exists or table/column invalid
   */
  public CreateIndex(AST_CreateIndex tree) throws QueryException {

		fileName = tree.getFileName();
    QueryCheck.fileNotExists(fileName);
//    QueryCheck.indexExists(fileName);

		iColumn = tree.getIxColumn();
//		QueryCheck.columnExists(schema, iColumn);
		
		iTable = tree.getIxTable();
		QueryCheck.tableExists(iTable);
  }

  /**
   * Executes the plan and prints applicable output.
   */
  public void execute() {

    new HashIndex(fileName);
//System.out.println("index to create: " + fileName);
    Minibase.SystemCatalog.createIndex(fileName, iTable, iColumn);

		System.out.println("Index created.");
	}
}

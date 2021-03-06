package query;

import global.Minibase;
import heap.HeapFile;
import index.HashIndex;
import parser.AST_DropTable;

/**
 * Execution plan for dropping tables.
 */
class DropTable implements Plan {

  protected String fileName;


  /**
   * Optimizes the plan, given the parsed query.
   * 
   * @throws QueryException if table doesn't exist
   */
  public DropTable(AST_DropTable tree) throws QueryException {

    fileName = tree.getFileName();
    QueryCheck.tableExists(fileName);

  }

  /**
   * Executes the plan and prints applicable output.
   */
  public void execute() {

    IndexDesc[] inds = Minibase.SystemCatalog.getIndexes(fileName);
//System.out.println("table to drop: " + fileName);
    for (IndexDesc ind : inds) {
//System.out.println("index to delete: " + ind.indexName);
}
    for (IndexDesc ind : inds) {
//      new HashIndex(ind.indexName).deleteFile();
//      HashIndex h = new HashIndex(ind.indexName);
//			h.deleteFile();
      Minibase.SystemCatalog.dropIndex(ind.indexName);
    }

//    new HeapFile(fileName).deleteFile();
    Minibase.SystemCatalog.dropTable(fileName);

    System.out.println("Table dropped.");

  }

}

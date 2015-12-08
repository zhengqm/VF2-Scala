/**
 * Created by qmzheng on 11/16/15.
 */

package vf2 {

  object vf2Main {

    import vf2.Utils._
    import vf2.dataStructure._

    def time[A](f: => A) = {
      val s = System.nanoTime
      val ret = f
      println("time: "+(System.nanoTime-s)/1e9+"s")
      ret
    }

    def setParallelismGlobally(numThreads: Int): Unit = { // Set thread pool size
    val parPkgObj = scala.collection.parallel.`package`
      val defaultTaskSupportField = parPkgObj.getClass.getDeclaredFields.find{
        _.getName == "defaultTaskSupport"
      }.get

      defaultTaskSupportField.setAccessible(true)
      defaultTaskSupportField.set(
        parPkgObj,
        new scala.collection.parallel.ForkJoinTaskSupport(
          new scala.concurrent.forkjoin.ForkJoinPool(numThreads)
        )
      )
    }

    def main(args: Array[String]) {
      if (args.size != 3) {
        println("Usage: java -jar VF2-Scala.jar [Graph DB File] [Query File] [Number of threads]")
        println("Example: java -jar VF2-Scala.jar mygraphdb.data Q4.my 8")
        return
      }

      //val databaseFile = "/Users/qmzheng/Code/VF2-Scala/data/mygraphdb.data"
      //val queryFile = "/Users/qmzheng/Code/VF2-Scala/data/Q4.my"

      val databaseFile = args(0)
      val queryFile = args(1)
      val threads = args(2).toInt

      setParallelismGlobally(threads)

      val graphDB = loadDataFile(databaseFile).par
      val queryGraphs = loadDataFile(queryFile)

      val queryResults = queryGraphs.foreach { qGraph =>
        val qResult = graphDB.map(g => new GraphMatcher(g, qGraph).check_isomorphism()).filter(_.isDefined).map(_.get).toList
        // List[Found] to represent the search result

        printResult(qGraph.graphID, qResult)

      }

    }

  }


}
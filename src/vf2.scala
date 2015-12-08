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

      val databaseFile = ""
      val queryFile = ""

      val graphDB = loadDataFile(databaseFile)
      val queryGraphs = loadDataFile(queryFile)

      val queryResults = queryGraphs.map { qGraph =>

        val qResult = graphDB.map(g => new GraphMatcher(g, qGraph).check_isomorphism()).filter(_ != None).map(_.get)
        // List[Found] to represent the search result

        (qGraph.graphID, qResult)
        // Return search result together with queryGraph ID
      }

      reportResult(queryResults)


    }

  }


}
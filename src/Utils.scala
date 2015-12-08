/**
 * Created by qmzheng on 11/16/15.
 */


import scala.io.Source
import scala.collection.mutable
import scala.collection.mutable.ListBuffer


package vf2 {

  import vf2.dataStructure._

  object Utils {

    def loadDataFile(filename: String): List[Graph] = {

      val tempGraphList = new ListBuffer[Graph]()

      var vertexIdToLabel = mutable.Map[Int, Int]()
      var neighbourVertex = mutable.Map[Int, mutable.Set[Int]]()

      var graphId = -2

      // TODO: Load DataFile
      /*
      for (line <- Source.fromFile(filename).getLines) {
        val segments = line.split(" ")
        val lineType = segments(0)
        lineType match {
          case "t" => {
            if (graphId != -2) {
              val immutableNeighbourVertex = neighbourVertex.map {case (k,v) => (k, v.toSet)}.toMap
              tempGraphList += new Graph(graphId, vertexIdToLabel.toMap, immutableNeighbourVertex)
            } // Store result

            graphId = segments(2).toInt

            if (graphId != -1) {
              vertexIdToLabel = mutable.Map[Int, Int]()
              neighbourVertex = mutable.Map[Int, mutable.Set[Int]]()
            }

          }
          case "v" => {

            val vertexId = segments(1).toInt
            val vertexLabel = segments(2).toInt
            vertexIdToLabel.put(vertexId, vertexLabel)
            neighbourVertex.put(vertexId, mutable.Set[Vertex]())
          }
          case "e" => {

            val fromId = segments(1).toInt
            val toId = segments(2).toInt
            val edgeLabel = segments(3).toInt

            neighbourVertex.get(fromId).get += new Vertex(toId, vertexIdToLabel.get(toId).get)
            neighbourVertex.get(toId).get += new Vertex(fromId, vertexIdToLabel.get(fromId).get)


          }
        }
      }
      */
      null
    }

    def reportResult(queryResult: List[(Int, List[Found])]) = {
      // TODO: Report search result
    }

  }

}


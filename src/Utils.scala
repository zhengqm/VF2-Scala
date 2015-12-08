/**
 * Created by qmzheng on 11/16/15.
 */


import scala.io.Source
import scala.collection.mutable
import scala.collection.mutable.ListBuffer


package vf2 {

import vf2.dataStructure._

import scala.collection.parallel.immutable.ParSeq

object Utils {

  def loadDataFile(filename: String): List[Graph] = {

    val tempGraphList = new ListBuffer[Graph]()

    var vertexIdToLabel = mutable.Map[Int, Int]()
    var neighbourVertex = mutable.Map[Int, mutable.Set[Int]]()
    var neighbourEdges = mutable.Map[Int, mutable.Set[Edge]]()

    var graphId = -2

    // TODO: Load DataFile

    for (line <- Source.fromFile(filename).getLines) {
      val segments = line.split(" ")
      val lineType = segments(0)
      lineType match {
        case "t" => {
          if (graphId != -2) {
            val immutableNeighbourVertex = neighbourVertex.map { case (k, v) => (k, v.toSet) }.toMap
            val immutableNeighbourEdges = neighbourEdges.map { case (k, v) => (k, v.toSet) }.toMap
            tempGraphList += new Graph(graphId, vertexIdToLabel.toMap, immutableNeighbourVertex, immutableNeighbourEdges)
          } // Store result

          graphId = segments(2).toInt

          if (graphId != -1) {
            vertexIdToLabel = mutable.Map[Int, Int]()
            neighbourVertex = mutable.Map[Int, mutable.Set[Int]]()
            neighbourEdges = mutable.Map[Int, mutable.Set[Edge]]()
          }

        }
        case "v" => {

          val vertexId = segments(1).toInt
          val vertexLabel = segments(2).toInt
          vertexIdToLabel.put(vertexId, vertexLabel)
          neighbourVertex.put(vertexId, mutable.Set[Int]())
          neighbourEdges.put(vertexId, mutable.Set[Edge]())
        }
        case "e" => {

          val fromId = segments(1).toInt
          val toId = segments(2).toInt
          val edgeLabel = segments(3).toInt

          neighbourVertex.get(fromId).get += toId
          neighbourVertex.get(toId).get += fromId
          neighbourEdges.get(fromId).get += new Edge(edgeLabel, toId)
          neighbourEdges.get(toId).get += new Edge(edgeLabel, fromId)
        }

        case _ => {}
      }
    }

    tempGraphList.toList
  }

  def reportResult(queryResult: List[(Int, List[Found])]) = {
    queryResult.foreach { case (id, foundList) =>
      println(s"Graph query $id")
      foundList.foreach {
          case Iso(origin, mapping) => {
            println(s"Iso with $origin")
            //println(mapping.mkString("  "))
          }
          case SubIso(origin, mapping) => {
            println(s"SubIso with $origin")
            //println(mapping.mkString("  "))
          }
      }
    }
  }

  def printResult(id: Int, foundList: List[Found]) = {
    println(s"Graph query $id")
    foundList.foreach {
        case Iso(origin, mapping) => {
          println(s"Iso with $origin")
          //println(mapping.mkString("  "))
        }
        case SubIso(origin, mapping) => {
          println(s"SubIso with $origin")
          //println(mapping.mkString("  "))
        }

    }

  }

}

}

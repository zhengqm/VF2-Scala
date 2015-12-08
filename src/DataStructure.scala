/**
 * Created by qmzheng on 11/16/15.
 */

package vf2.dataStructure {

import scala.collection.mutable

case class Edge(label: Int, toVertex: Int)

  class Graph(val graphID: Int, val vertexIdToLabel: Map[Int, Int]
               , immutableNeighbourVertex: Map[Int, Set[Int]]
               , immutableNeighbourEdges: Map[Int, Set[Edge]]) {

    val vertexNumber = vertexIdToLabel.size
    val vertexIDs = vertexIdToLabel.keys.toList

    def getEdges(id: Int) = {
      immutableNeighbourEdges.get(id).get
    }

    def getNeighbour(id: Int) = {
      immutableNeighbourVertex.get(id).get
    }

    def getNeighbourWithLabel(id: Int) = {
      immutableNeighbourVertex.get(id)
                              .get
                              .map(vid => (vid, vertexIdToLabel.get(vid).get))

    }

  }



  class GraphMatcher(G1: Graph, G2: Graph) {

    val core_1 = mutable.Map[Int, Int]()
    val core_2 = mutable.Map[Int, Int]()

    var inout_1 = mutable.Map[Int, Int]()
    var inout_2 = mutable.Map[Int, Int]()


    def check_isomorphism(): Option[Found] = {

      inner_check(0)
      if (core_2.size == G2.vertexNumber) {
        if (core_1.size == G1.vertexNumber) {
          Option(Iso(G1.graphID))
        } else {
          Option(SubIso(G1.graphID))
        }
      } else {
        None
      }
    }



    def inner_check(depth: Int): Unit = {
      // Run ...
      if (core_2.size == G2.vertexNumber){
        return
      }

      val candidatePairs = generateCandidatePairs()

      for (pair <- candidatePairs) {
        val G1_node = pair._1
        val G2_node = pair._2
        if (syntactic_feasibility(G1_node, G2_node) && semantic_feasibility(G1_node, G2_node)) {

          // Build state
          core_1.put(G1_node, G2_node)
          core_2.put(G2_node, G1_node)

          G1.getNeighbour(G1_node)
            .filter(v => !inout_1.contains(v))
            .foreach(v => inout_1.put(v, depth))
          G2.getNeighbour(G2_node)
            .filter(v => !inout_2.contains(v))
            .foreach(v => inout_2.put(v, depth))

          // Call recursively
          inner_check(depth + 1)

          // Restore state
          core_1.remove(G1_node)
          core_2.remove(G2_node)
          inout_1 = inout_1.retain{case (k,v) => v != depth}
          inout_2 = inout_2.retain{case (k,v) => v != depth}

        }

      }

    }

    def generateCandidatePairs() = {
      val T1_inout = inout_1.keys.filter(id => !core_1.contains(id))
      val T2_inout = inout_2.keys.filter(id => !core_2.contains(id))

      if (T2_inout.isEmpty) {
        val T2_selected = T2_inout.min
        T1_inout.map{id => (id, T2_selected) }
      } else {
        val otherNode = G2.vertexIDs.filter(id => !core_2.contains(id)).min
        G1.vertexIDs.filter(id => !core_1.contains(id)).map {id => (id, otherNode)}
      }

    }

    def syntactic_feasibility(G1_node: Int, G2_node: Int): Boolean = {

      // Pred & Succ

      val G1_check = G1.getEdges(G1_node)
        .filter(edge => core_1.contains(edge.toVertex))
        .map {edge => edge.toVertex}
        .map {n_prime => core_1.get(n_prime).get}
        .forall {m_prime => G2.getEdges(G2_node).exists(e => e.toVertex == m_prime)}

      if (!G1_check) {
        return false
      }

      val G2_check = G2.getEdges(G2_node)
        .filter(edge => core_2.contains(edge.toVertex))
        .map {edge => edge.toVertex}
        .map {m_prime => core_2.get(m_prime).get}
        .forall {n_prime => G1.getEdges(G1_node).exists(e => e.toVertex == n_prime)}

      if (!G2_check) {
        return false
      }

      // In & Out

      val G1_num = G1.getEdges(G1_node)
                      .count( e => inout_1.contains(e.toVertex) && !core_1.contains(e.toVertex))
      val G2_num = G2.getEdges(G2_node)
                      .count( e => inout_1.contains(e.toVertex) && !core_1.contains(e.toVertex))

      if (G1_num < G2_num) {
        return false
      }
      // New
      val G1_new = G1.getEdges(G1_node)
        .count( e =>  !inout_1.contains(e.toVertex))
      val G2_new = G2.getEdges(G2_node)
        .count( e =>  !inout_2.contains(e.toVertex))

      if (G1_new < G2_new) {
        return false
      }
      true
    }

    def semantic_feasibility(G1_node: Int, G2_node: Int): Boolean = {

      // Node
      if (G1.vertexIdToLabel.get(G1_node).get != G2.vertexIdToLabel.get(G2_node)) {
        return false
      }

      // Edges
      G1.getEdges(G1_node)
        .filter(edge => core_1.contains(edge.toVertex))
        .forall{ edge =>
          val m_prime = core_1.get(edge.toVertex).get
          G2.getEdges(m_prime).exists(e => e.toVertex == G2_node && e.label == edge.label)
        }
    }


    }

  sealed trait Found
  case class Iso(graphID: Int) extends Found
  case class SubIso(graphID: Int) extends Found




}

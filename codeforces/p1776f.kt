// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// SWERC 2022-2023 Problem F Train
// https://swerc.eu/2022/problems/
// https://codeforces.com/contest/1776/problem/F

fun readPairInts(): Pair<Int, Int> {
    val (a, b) = readln().split(" ").map{it.toInt()}.toList()
    return Pair(a, b)
}

fun findMissingEdge(n: Int, edges: Set<Pair<Int, Int>>): Pair<Int, Int>? {
    for (i in 1..n) {
        for (j in (i + 1)..n) {
            if (edges.contains(Pair(i, j))) continue
            return Pair(i, j)
        }
    }
    return null
}

fun constructPlan(n: Int, edges: List<Pair<Int, Int>>, a: Int, b: Int): Pair<Int, List<Int>> {
    return Pair(2, edges.map{(u, v) -> if (u == a || v == a) 1 else 2}.toList())
}

fun constructPlanFromClique(n: Int, edges: List<Pair<Int, Int>>): Pair<Int, List<Int>> {
    return Pair(3, edges.map{(u, v) ->
        if (u == 1 && v == 2) {
            1
        } else if (u == 1 || v == 1) {
            2
        } else if (u == 2 || v == 2) {
            3
        } else {
            1
        }
    }.toList())
}

fun main() {
    val numCases = readln().toInt()
    for (i in 1..numCases) {
        val (n, m) = readPairInts()
        val edges = (1..m).map{readPairInts()}.map{
            if (it.first > it.second) {
                Pair(it.second, it.first)
            } else {
                Pair(it.first, it.second)
            }
        }.toList()
        val edgeSet = edges.toSet()
        val missingEdge = findMissingEdge(n, edgeSet)
        val ans = if (missingEdge == null) {
            constructPlanFromClique(n, edges)
        } else {
            missingEdge.run{constructPlan(n, edges, first, second)}
        }
        println(ans.first)
        println(ans.second.joinToString(" "))
    }
}

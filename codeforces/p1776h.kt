// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// SWERC 2022-2023 Problem H Beppa
// https://swerc.eu/2022/problems/
// https://codeforces.com/contest/1776/problem/H

fun solveOne() {
    val n = readln().toInt()
    val (ord9, ord22) = (1..2).map{
        readln().split(" ").asReversed().map{it.toInt() - 1}
    }.toList()

    val inv = (1..n).toMutableList()
    for ((i, v) in ord9.withIndex()) inv[v] = i
    val ord = ord22.map{inv[it]}

    var i = 1
    while (i < n && ord[i - 1] < ord[i]) ++i
    println(n - i)
}

fun main() {
    val numCases = readln().toInt()
    for (i in 1..numCases) solveOne()
}

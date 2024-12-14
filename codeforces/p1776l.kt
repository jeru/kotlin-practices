// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// SWERC 2022-2023 Problem L Controllers
// https://swerc.eu/2022/problems/
// https://codeforces.com/contest/1776/problem/L

import kotlin.math.max
import kotlin.math.min

fun solveOne(numP: Long, numN: Long): Boolean {
    // p * a + (numP - p) * b - n * a - (numN - n) * b = 0
    // (p - n) * (a - b) + (numP - numN) * b = 0
    val (a, b) = readln().split(" ").map{it.toLong()}
    if (a == b) return numP == numN
    if ((numP - numN) * b % (b - a) != 0L) return false
    val d = (numP - numN) * b / (b - a)
    // 0 <= n <= numN
    // 0 <= n + d <= numP
    return max(0L, -d) <= min(numN, numP - d)
}

fun main() {
    readln()
    val pms = readln()
    val numP = pms.count{it == '+'}.toLong()
    val numN = pms.count{it == '-'}.toLong()
    val numCases = readln().toInt()
    for (i in 1..numCases) {
        println(if (solveOne(numP, numN)) "YES" else "NO")
    }
}

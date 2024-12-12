// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// SWERC 2022-2023 Problem: A. Walking Boy
// https://swerc.eu/2022/problems/
// https://codeforces.com/contest/1776/problem/A

fun main() {
    val numCases = readln().toInt()
    for (i in 1..numCases) {
        readln()
        val a = listOf(listOf(0).asIterable(),
                       readln().split(" ").map{it.toInt()},
                       listOf(1440).asIterable()).flatten()
        val num = (1..<a.size).map{(a[it] - a[it - 1]) / 120}.sum()
        println(if (num >= 2) "YES" else "NO")
    }
}

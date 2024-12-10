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
        val a = ArrayList<Int>()
        readln().split(" ").map{it.toInt()}.toCollection(a)
        a.add(0, 0)
        a.add(1440)

        val countsOverBar = {bar: Int -> (1..<a.size).filter{a[it] - a[it - 1] >= bar}.count() }
        val ans = countsOverBar(240) >= 1 || countsOverBar(120) >= 2
        println(if (ans) "YES" else "NO")
    }
}

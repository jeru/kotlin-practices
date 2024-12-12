// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// SWERC 2022-2023 Problem B
// https://swerc.eu/2022/problems/
// https://codeforces.com/contest/1776/problem/B

import kotlin.math.min

fun main() {
    val (n, h) = readln().split(" ").map{it.toInt()}.toList()
    val x = readln().split(" ").map{it.toInt()}.toList()
    val best = (0..n).map{
        MutableList(n + 1){Long.MAX_VALUE}
    }.toList()
    for (i in (n - 1) downTo 0) {
        best[i][i + 1] = h.toLong()
        for (j in (i + 2) .. n) {
            var b = Long.MAX_VALUE
            for (k in (i + 1) ..< j) {
                b = min(b, best[i][k] + best[k][j])
                //  AA BB   AA  BB
                //   AABB    AABB
                //    CC      CC
                val reach = (x[j - 1] - x[i] + 1) / 2
                if (reach <= h) b = min(b, best[i][k] + best[k][j] - (h - reach + 1))
            }
            best[i][j] = b
        }
    }
    println(best[0][n])
}

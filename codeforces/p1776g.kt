// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// SWERC 2022-2023 Problem G Wine
// https://swerc.eu/2022/problems/
// https://codeforces.com/contest/1776/problem/G

fun variant1(n: Int, t: String) {
    // This variant is too slow, timed out on the time limit 500ms.
    val psumW = t.map{if (it == 'W') 1 else 0}.runningFold(0, Int::plus)
    val ans = (0..<n).map{psumW[it + n] - psumW[it]}.max()
    println(ans)
}

fun variant2(n: Int, t: String) {
    // This variant barely succeeded with 467ms.
    val psumW = ArrayList<Int>(t.length + 1)
    psumW.add(0)
    for (c in t) psumW.add(psumW.last() + if (c == 'W') 1 else 0)
    var ans = 0
    for (i in 0..<n) {
        val v = psumW[i + n] - psumW[i]
        if (v > ans) ans = v
    }
    println(ans)
}

fun variant3(n: Int, t: String) {
    // This variant is also too slow.
    val psumW = t.asSequence().map{if (it == 'W') 1 else 0}.runningFold(0, Int::plus).toList()
    val ans = (0..<n).asSequence().map{psumW[it + n] - psumW[it]}.max()
    println(ans)
}

fun variant4(n: Int, t: String) {
    // Half variant1 + half variant2.
    val psumW = t.map{if (it == 'W') 1 else 0}.runningFold(0, Int::plus)
    var ans = 0
    for (i in 0..<n) {
        val v = psumW[i + n] - psumW[i]
        if (v > ans) ans = v
    }
    println(ans)
}

fun main(args: Array<String>) {
    val n = readln().toInt()
    val t = readln()
    // Local benchmarking result (with n == 1M, t = "WW...W"):
    // (real, user)
    // Variant1: (.195s, .430s), (.170s, .370s), (.183s, .344s)
    // Vairant2: (.124s, .161s), (.117s, .228s), (.118s, .177s)
    // Variant3: (.199s, .335s), (.214s, .394s), (.204s, .366s)
    // Variant4: (.131s, .244s), (.211s, .175s), (.128s, .233s)
    when (args.getOrNull(0)) {
        "1" -> variant1(n, t)
        "2" -> variant2(n, t)
        "3" -> variant3(n, t)
        else -> variant4(n, t)
    }
}

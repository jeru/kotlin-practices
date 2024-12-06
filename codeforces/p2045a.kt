// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: A. Scrambled Scrabble
// https://codeforces.com/problemset/problem/2045/A

import kotlin.math.min
import kotlin.math.max

fun solveWithResolvedY(numN: Int, numG: Int, numA: Int, numB: Int): Int {
	var n = numN
	var g = numG
	var b = numB
	val syllables = min(numA, (n + g + b) / 2)
	var ans = syllables
	var consos = syllables * 2

	// Prefer consuming N's, then B's, then G's.
	var t = min(n, consos)
	n -= t; consos -= t
	ans += t

	t = min(b, consos)
	b -= t; consos -= t
	ans += t

	t = min(g, consos)
	g -= t; consos -= t
	ans += t

	// Remaining G's can be padded to used N's.
	t = min(numN - n, g)
	ans += t

	return ans
}

fun solve(numN: Int, numG: Int, numY: Int, numA: Int, numB: Int): Int {
	var ans: Int = 0
	for (y in 0..numY) {
		ans = max(ans, solveWithResolvedY(numN, numG, numA + y, numB + (numY - y)))
	}
	return ans
}

fun main() {
	val inStr = readln()
	var numN: Int = 0
	var numG: Int = 0
	var numY: Int = 0
	var numA: Int = 0
	var numB: Int = 0
	for (char in inStr) {
		if (char < 'A' || char > 'Z') continue
		when (char) {
			'N' -> numN += 1
			'G' -> numG += 1
			'Y' -> numY += 1
			in "AEIOU" -> numA += 1
			else -> numB += 1
		}
	}
	// println("${numN}, ${numG}, ${numY}, ${numA}, ${numB}")
	println(solve(min(numN, numG), max(numN, numG), numY, numA, numB))
}

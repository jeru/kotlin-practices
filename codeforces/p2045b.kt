// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: B. ICPC Square
// https://codeforces.com/problemset/problem/2045/B

import kotlin.comparisons.minOf
import kotlin.math.max

fun solve(s: Long, d: Long, n: Long): Long {
	if (s > d) return s
	// Only needs at most two steps s -> sx -> sxy.
	// Can assume x >= y.
	var ans: Long = 0
	for (y in 1..n) {
		if (s * y * y > n) break
		// sxy <= n
		// sxy - sx <= d
		// sx - s <= d
		val constraint2 = if (y == 1L) {
			Long.MAX_VALUE
		} else {
			d / (y - 1) / s
		}
		val x = minOf(n / s / y, constraint2, d / s + 1)
		ans = max(ans, s * x * y)
	}
	return ans
}

fun main() {
	val inStrs: List<String> = readln().split(" ")
	// Not catching parse error exceptions.
	val inInts = inStrs.map {s: String -> s.toLong()}
	assert(inInts.size == 3)
	val (n, d, s) = inInts
	//println("n=${n}, d=${d}, s=${s}")
	println(solve(n=n, d=d, s=s))
}

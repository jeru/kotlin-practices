// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: C. Saraga
// https://codeforces.com/problemset/problem/2045/C

import kotlin.collections.Map
import kotlin.collections.mutableMapOf

fun createFirstCharMap(str: String): Map<Char, Int> {
	val map = mutableMapOf<Char, Int>()
	for ((i, ch) in str.withIndex().reversed()) {
		if (i == 0) continue
		map.put(ch, i)  // Smaller index (later) overwrites larger index.
	}
	return map
}

fun getCharIndicesWithMinSum(map1: Map<Char, Int>, map2: Map<Char, Int>): Pair<Int, Int>? {
	var ans = Int.MAX_VALUE
	var ansComposition: Pair<Int, Int>? = null
	for (ch in 'a'..'z') {
		val index1 = map1.get(ch)
		val index2 = map2.get(ch)
		if (index1 == null || index2 == null) continue
		val v = index1 + 1 + index2
		if (v < ans) {
			ans = v
			ansComposition = Pair(index1, index2)
		}
	}
	return ansComposition
}

fun main() {
	val line1 = readln()
	val line2 = readln()
	val map1 = createFirstCharMap(line1)
	val map2 = createFirstCharMap(line2.reversed())
	val bestIndices = getCharIndicesWithMinSum(map1, map2)
	if (bestIndices == null) {
		println("-1")
	} else {
		val (index1, index2) = bestIndices
		val size2 = line2.length
		println("${line1.take(index1 + 1)}${line2.takeLast(index2)}")
	}
}

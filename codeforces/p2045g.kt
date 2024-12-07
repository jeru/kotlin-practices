// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: G. X Aura
// https://codeforces.com/problemset/problem/2045/G

fun readlnToLongs(): List<Long> =
	readln().split(" ").map{x -> x.toLong()}.toList()

fun longPow(b: Long, i: Long): Long {
	var vi = i
	var vb = b
	var ans = 1L
	while (vi > 0) {
		if (vi % 2L == 1L) ans = ans * vb
		vi /= 2L
		vb = vb * vb
	}
	return ans
}

data class Board(val powTable: List<Long>, val h: List<String>) {
	fun height(r: Int, c: Int): Int = this.h[r][c].code - '0'.code
	fun score(r1: Int, c1: Int, r2: Int, c2: Int): Long =
		this.powTable[this.height(r1, c1) - this.height(r2, c2) + 9]
	fun loop(r: Int, c: Int): Long =
		this.score(r, c, r + 1, c) + this.score(r + 1, c, r + 1, c + 1) +
		this.score(r + 1, c + 1, r, c + 1) + this.score(r, c + 1, r, c)
	fun irregular(): Boolean {
		for (r in 1..<this.h.size)
			for (c in 1..<this.h[0].length) {
				if (this.loop(r - 1, c - 1) != 0L) return true
			}
		return false
	}
	fun potentialField(): List<List<Long>> {
		val row0 = mutableListOf(0L)
		for (c in 1..<this.h[0].length) {
			val v = row0.last() + this.score(0, c - 1, 0, c)
			row0.add(v)
		}
		val field = mutableListOf<List<Long>>(row0.toList())
		for (r in 1..<this.h.size) {
			field.add(field.last().withIndex()
					.map{(c, v) -> v + this.score(r - 1, c, r, c)}.toList())
		}
		return field
	}
}

fun main() {
	val (r, c, x) = readlnToLongs()
	val h: List<String> = (1..r).map{_ -> readln()}.toList()
	val (q) = readlnToLongs()
	val qs: List<List<Int>> = (1..q).map{_ -> readlnToLongs().map{x -> x.toInt() - 1}}.toList()

	val powTable: List<Long> = (-9L..9L).map{i -> longPow(i, x)}.toList()
	val board = Board(powTable, h)
	val irregular = board.irregular()
	val potentialField = board.potentialField()
	for ((r1, c1, r2, c2) in qs) {
		if (irregular) {
			println("INVALID")
		} else {
			println(potentialField[r2][c2] - potentialField[r1][c1])
		}
	}
}

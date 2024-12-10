// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: G. X Aura
// https://codeforces.com/problemset/problem/2045/G

fun readlnToInts(): List<Int> =
	readln().split(" ").map{it.toInt()}.toList()

class PowerTable(index: Int) {
    inline operator fun get(index: Int): Int = powers[index + 9]

    val powers: List<Int> = (-9..9).map{power(it, index)}.toList()

    companion object {
        private fun power(b: Int, i: Int): Int {
            var ans = 1
            for (i in 1..i) { ans *= b }
            return ans
        }
    }
}

data class Board(val powTable: PowerTable, val h: List<String>) {
	inline fun height(r: Int, c: Int): Int = h[r][c].code - '0'.code
	inline fun score(r1: Int, c1: Int, r2: Int, c2: Int): Int =
		powTable[this.height(r1, c1) - this.height(r2, c2)]
	fun loop(r: Int, c: Int): Int =
		this.score(r, c, r + 1, c) + this.score(r + 1, c, r + 1, c + 1) +
		this.score(r + 1, c + 1, r, c + 1) + this.score(r, c + 1, r, c)
	fun irregular(): Boolean {
		for (r in 1..<this.h.size)
			for (c in 1..<this.h[0].length) {
				if (this.loop(r - 1, c - 1) != 0) return true
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
	val (r, c, x) = readlnToInts()
	val h: List<String> = (1..r).map{readln()}.toList()
	val (q) = readlnToInts()

    val powTable = PowerTable(x)
    val board = Board(powTable, h)
	val irregular = board.irregular()
	val potentialField = board.potentialField()
    for (i in 1..q) {
        val (r1, c1, r2, c2) = readlnToInts().map{it.toInt() - 1}.toList()
		if (irregular) {
			println("INVALID")
		} else {
			println(potentialField[r2][c2] - potentialField[r1][c1])
		}
    }
}

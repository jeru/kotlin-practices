// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: H. Missing Separators
// https://codeforces.com/problemset/problem/2045/H
//
// Too slow (TLE 1s at case 37, more than 90 cases). C++ equivalent version passed with 843ms.

class Solver(val s: String) {
        val suffices = s.toSuffixArray()

        val matchingLengthMatrix: List<MutableList<Int>> = (1..s.length).map{_ ->
                (1..s.length).map{_ -> -1}.toMutableList()
        }.toList()

        fun computeMatchingLengths() {
                this.computeMatchingLengthsRec(0, s.length, 0)
        }

        // all suffices in [b, e) match by m0 characters.
        private fun computeMatchingLengthsRec(b: Int, e: Int, m0: Int) {
                val m = this.matchMore(this.suffices[b], this.suffices[e - 1], m0)
                if (b + 1 == e) {
                        val u = this.suffices[b]
                        this.matchingLengthMatrix[u][u] = m
                        return
                }
                var i = b
                while (i < e) {
                        val nextChar = this.getS(this.suffices[i] + m)
                        var j = i
                        while (j < e && this.getS(this.suffices[j] + m) == nextChar) j += 1
                        for (k in b..<e) {
                                if (i <= k && k < j) continue
                                for (l in i..<j) {
                                        val u = this.suffices[k]
                                        val v = this.suffices[l]
                                        this.matchingLengthMatrix[u][v] = m
                                        this.matchingLengthMatrix[v][u] = m
                                }
                        }
                        this.computeMatchingLengthsRec(i, j, m)
                        i = j
                }
        }

        private fun matchMore(a: Int, b: Int, m0: Int): Int {
                var m = m0
                while (a + m < this.n() && b + m < this.n() && this.s[a + m] == this.s[b + m]) m += 1
                return m
        }

        fun printDebugMatrix() {
                for (i in 0..<this.n()) {
                        for (j in 0..<this.n()) {
                                println("matchingLength(${this.s.substring(i)}, ${this.s.substring(j)}) = ${this.matchingLengthMatrix[i][j]}")
                        }
                }
        }

        private fun n(): Int = this.s.length

        private fun getS(i: Int): Char = if (i < this.n()) this.s[i] else '$'

        // best[i][j]: longest dict constructed from S[i..] if the first cut happens at j or later.
        val best: List<MutableList<Pair<Int, Pair<Int, Int>>>> = (1..s.length).map{_ ->
                (0..s.length).map{_ -> Pair(-1, Pair(-1, -1))}.toMutableList()
        }.toList()

        fun computeBest() {
                for (i in (this.n() - 1) downTo 0) this.computeBestOne(i)
        }

        private fun computeBestOne(i: Int) {
                this.best[i][this.n()] = Pair(1, Pair(this.n(), 0))
                for (j in (this.n() - 1) downTo i) {
                        this.best[i][j] = this.best[i][j + 1]
                        if (j == i) continue
                        val l = this.matchingLengthMatrix[i][j]
                        val minNextL = if (l >= j - i) {  // Fully consume first string.
                                if (j - i + j >= this.n()) continue
                                j - i + j + 1
                        } else {
                                assert(j + l <= this.n())
                                if (j + l == this.n() || this.s[j + l] < this.s[i + l]) continue
                                j + l + 1
                        }
                        val v = this.best[j][minNextL].first
                        if (v >= 0 && v + 1 > this.best[i][j].first) {
                                this.best[i][j] = Pair(v + 1, Pair(j, minNextL))
                        }
                }
        }

        fun printAnswer(i: Pair<Int, Int>) {
                if (i.first == this.n()) return
                val j = this.best[i.first][i.second].second
                println(this.s.substring(i.first, j.first))
                this.printAnswer(j)
        }
}

fun main() {
        val solver = Solver(readln())
        solver.computeMatchingLengths()
        //solver.printDebugMatrix()
        solver.computeBest()

        val ans = solver.best[0][0].first
        assert(ans > 0)
        println(ans)
        solver.printAnswer(Pair(0, 0))
}

// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Suffix array in O(N log N) time.

fun String.toSuffixArray(): List<Int> {
        val helper = SuffixArrayHelper(this)
        return helper.run()
}

private class SuffixArrayHelper(val s: String) {
        var orderId: MutableList<Int> = mutableListOf()
        var delta: Int = 0

        fun run(): List<Int> {
                if (s.length == 0) {
                        return listOf()
                } else if (s.length == 1) {
                        return listOf(0)
                }
                val charToOrderId: Map<Char, Int> = s.asIterable().sorted().withIndex().map{(i, c) -> Pair(c, i)}.toMap()
                orderId = s.asIterable().map{c -> charToOrderId.get(c)!!}.toMutableList()
                delta = 1
                while (delta < s.length) {
                        computeNewOrderId()
                        delta *= 2
                }

                val ans = (1..s.length).map{0}.toMutableList()
                for ((i, v) in orderId.withIndex()) ans[v] = i
                return ans.toList()
        }

        private fun getFirstOrderId(i: Int): Int = orderId[i]
        private fun getSecondOrderId(i: Int): Int =
                if (i + delta < s.length) orderId[i + delta] else s.length
        private fun getCombinedId(i: Int): Long =
                getFirstOrderId(i).toLong() * (s.length + 1) + getSecondOrderId(i)
        
        private val order = (1..s.length).map{_ -> 0}.toMutableList()
        private val newOrderId = (1..s.length).map{_ -> 0}.toMutableList()

        private fun computeNewOrderId() {
                for (i in 0..<s.length) order[i] = i
                sortOrderStablyBy(this::getSecondOrderId)
                sortOrderStablyBy(this::getFirstOrderId)

                var nextId = 0
                for (i in 0..<s.length) {
                        if (i == 0 || getCombinedId(order[i - 1]) != getCombinedId(order[i])) {
                                newOrderId[order[i]] = nextId++
                        } else {
                                newOrderId[order[i]] = newOrderId[order[i - 1]]
                        }
                }
                for (i in 0..<s.length) orderId[i] = newOrderId[i]
        }

        private val newOrder = (1..s.length).map{0}.toMutableList()
        private val freq = (1..(s.length + 2)).map{0}.toMutableList()

        // byFun return range: 0..s.length
        private fun sortOrderStablyBy(byFun: (Int) -> Int) {
                freq.fill(0)
                for (v in order) freq[byFun(v) + 1] += 1
                for (i in 1..<freq.size) freq[i] += freq[i - 1]

                for (v in order) {
                        val f = byFun(v)
                        newOrder[freq[f]++] = v
                }

                for (i in 0..<order.size) order[i] = newOrder[i]
        }
}

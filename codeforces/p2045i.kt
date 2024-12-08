// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: I. Microwavable Subsequence
// https://codeforces.com/problemset/problem/2045/I

fun main() {
    val m = readln().split(" ")[1].toInt()
    val a = readln().split(" ").map{it.toInt()}.toList()
    val numPresent = a.toSet().size.toLong()
    val numMissing = m - numPresent

    var ans = numPresent * numMissing + numPresent * (numPresent - 1) / 2
    val previous = (0..m).map{-1}.toMutableList()
    val rangeSum = RangeSum(a.size + 1)
    for ((i, v) in a.withIndex()) {
        if (previous[v] == -1) {
            ans += rangeSum.range(0, i)
        } else {
            rangeSum.update(previous[v], -1)
            ans += rangeSum.range(previous[v], i)
        }
        rangeSum.update(i, 1)
        previous[v] = i
    }
    println(ans)
}

class RangeSum constructor(n0: Int) {
    val n: Int = nextPow2(n0)
    val s: MutableList<Long> = (1..(n * 2 - 1)).map{0L}.toMutableList()

    // Adds `delta` to cell `p`.
    fun update(p: Int, delta: Long) {
        assert(p < n)
        var i = p + (n - 1)
        while (true) {
            s[i] += delta
            if (i == 0) break
            i = parent(i)
        }
    }

    // Sums the cells [b, e).
    fun range(b: Int, e: Int): Long {
        assert(b <= e)
        if (b == e) return 0L

        var l = b + (n - 1)
        var r = (e - 1) + (n - 1)
        if (l == r) return s[l]

        var ans = s[l] + s[r]
        while (true) {
            val lParent = parent(l)
            val rParent = parent(r)
            if (lParent == rParent) return ans

            if (l == lChild(lParent)) ans += s[rChild(lParent)]
            if (r == rChild(rParent)) ans += s[lChild(rParent)]

            l = lParent
            r = rParent
        }
    }

    companion object {
        private fun nextPow2(n: Int): Int {
            var p = 2
            while (p < n) p *= 2
            return p
        }
        private fun parent(n: Int): Int = (n - 1) / 2
        private fun lChild(n: Int): Int = n * 2 + 1
        private fun rChild(n: Int): Int = n * 2 + 2
    }
}

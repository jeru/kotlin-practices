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

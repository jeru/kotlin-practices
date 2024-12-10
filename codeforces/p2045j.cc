// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: J. Xorderable Array
// https://codeforces.com/problemset/problem/2045/J

#include <cstdio>
#include <cassert>
#include <algorithm>
#include <deque>
#include <iostream>
#include <optional>
#include <span>
#include <tuple>
#include <vector>
using namespace std;

typedef long long ll;

inline void check(bool x) {
    if (x) return;
    std::terminate();
}

template <typename C, typename Pred>
void RemoveIf(C& c, Pred pred) {
    c.erase(remove_if(c.begin(), c.end(), pred),
            c.end());
}

template <typename C>
void RemoveIfSmall(C& c) {
    RemoveIf(c, [](auto x) { return x.size() < 2; });
}

template <typename C>
ll Sum2(const C& c) {
    ll n = c.size();
    return n * (n - 1) / 2;
}

ll SumInner(const deque<span<int>>& xs) {
    ll ans = 0;
    for (const auto& x : xs) ans += Sum2(x);
    return ans;
}

ll SumCross(const deque<pair<span<int>, span<int>>>& xps) {
    ll ans = 0;
    for (const auto& xp : xps) ans += (ll)xp.first.size() * (ll)xp.second.size();
    return ans;
}

bool HasDupBit(int bit, const deque<span<int>>& as) {
    for (const auto& a : as) {
        if (a.size() < 2) continue;
        int u = a[0], v = a[1];
        if ((u >> bit & 1) == (v >> bit & 1)) return true;
        u = a[a.size() - 2]; v = a[a.size() - 1];
        if ((u >> bit & 1) == (v >> bit & 1)) return true;
    }
    return false;
}

bool HasZeroBit(int bit, const span<int>& ds) {
    for (int d : ds) {
        if (~(d >> bit) & 1) return true;
    }
    return false;
}

pair<span<int>, span<int>> SplitByBit(span<int> x, int bit) {
    int i = 0;
    while (i < x.size() && !(x[i] >> bit)) ++i;
    for (int& p : x.subspan(i)) p &= ~(1 << bit);
    return {x.subspan(0, i), x.subspan(i)};
}

ll CountDiffPairs(span<int> ds, deque<pair<span<int>, span<int>>> xps, int bit) {
    if (ds.empty() || bit < 0) return SumCross(xps);
    RemoveIf(xps, [](const auto& xp) { return xp.first.empty() || xp.second.empty(); });
    if (xps.empty()) return 0;
    if (HasZeroBit(bit, ds)) {
        auto it = partition(ds.begin(), ds.end(), [bit](int d) { return ((d >> bit) & 1) == 0; });
        ds = ds.subspan(0, it - ds.begin());

        int xps_size0 = xps.size();
        for (int i = 0; i < xps_size0; ++i) {
            span<int> xL0, xL1, xR0, xR1;
            tie(xL0, xL1) = SplitByBit(xps[i].first, bit);
            tie(xR0, xR1) = SplitByBit(xps[i].second, bit);
            xps[i] = {xL0, xR0};
            xps.push_back({xL1, xR1});
        }
        return CountDiffPairs(ds, move(xps), bit - 1);
    }
    // No zero bit.
    for (int& d : ds) { d &= ~(1 << bit); }

    ll ans = 0;
    deque<pair<span<int>, span<int>>> xps1;
    for (auto& xp : xps) {
        span<int> xL0, xL1, xR0, xR1;
        tie(xL0, xL1) = SplitByBit(xp.first, bit);
        tie(xR0, xR1) = SplitByBit(xp.second, bit);
        ans += (ll)xL0.size() * (ll)xR0.size();
        ans += (ll)xL1.size() * (ll)xR1.size();
        xps1.push_back({xL0, xR1});
        xps1.push_back({xL1, xR0});
    }
    return ans + CountDiffPairs(ds, move(xps1), bit - 1);
}

ll CountPairs(deque<span<int>> as, deque<span<int>> xs, int bit) {
    if (as.empty() || bit < 0) return SumInner(xs);
    deque<span<int>> extra_as, extra_xs;
    if (HasDupBit(bit, as)) {
        for (span<int>& a : as) {
            span<int> a1;
            tie(a, a1) = SplitByBit(a, bit);
            extra_as.push_back(move(a1));
        }
        as.insert(as.end(), extra_as.begin(), extra_as.end());
        RemoveIfSmall(as);

        for (span<int>& x : xs) {
            span<int> x1;
            tie(x, x1) = SplitByBit(x, bit);
            extra_xs.push_back(move(x1));
        }
        xs.insert(xs.end(), extra_xs.begin(), extra_xs.end());
        RemoveIfSmall(xs);

        return CountPairs(move(as), move(xs), bit - 1);
    }
    // No dup bit. Then two choices of pairs:
    // (1) Same x bit: totally free.
    // (2) Different x bit. 

    ll ans = 0;
    for (span<int>& x : xs) {
        span<int> x1;
        tie(x, x1) = SplitByBit(x, bit);
        ans += Sum2(x) + Sum2(x1);
        extra_xs.push_back(move(x1));
    }

    deque<pair<span<int>, span<int>>> xps;
    for (int i = 0; i < xs.size(); ++i) {
        xps.emplace_back(xs[i], extra_xs[i]);
    }

    vector<int> ds;
    for (span<int>& a : as) {
        check(a.size() <= 2);
        if (a.size() == 2) {
            int d = a[0] ^ a[1];
            check(d >> bit);
            d &= ~(1 << bit);
            ds.push_back(d);
        }
    }
    return ans + CountDiffPairs(ds, xps, bit - 1);
}

int main() {
    int n, m;
    check(scanf("%d%d", &n, &m) == 2);
    vector<int> a(n);
    for (int& v : a) check(scanf("%d", &v) == 1);
    sort(a.begin(), a.end());
    vector<int> x(m);
    for (int& v : x) check(scanf("%d", &v) == 1);
    sort(x.begin(), x.end());

    int max_val = max(a.back(), x.back());
    int bit = 0;
    while (max_val >> bit) ++bit;

    ll ans = CountPairs({a}, {x}, bit);
    cout << ans << '\n';
}

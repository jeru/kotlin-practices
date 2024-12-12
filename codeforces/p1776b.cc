// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// SWERC 2022-2023 Problem B
// https://swerc.eu/2022/problems/
// https://codeforces.com/contest/1776/problem/B

#include <algorithm>
#include <iostream>
#include <limits>
#include <string>
#include <vector>
using namespace std;

typedef long long ll;

int main() {
    cin.sync_with_stdio(false);
    int N, H;
    cin >> N >> H;
    vector<int> X(N);
    for (int& x : X) cin >> x;

    vector<vector<ll>> best(N + 1, vector(N + 1, numeric_limits<ll>::max()));
    for (int i = N - 1; i >= 0; --i) {
        best[i][i + 1] = H;
        for (int j = i + 2; j <= N; ++j) {
            ll b = numeric_limits<ll>::max();
            for (int k = i + 1; k < j; ++k) b = min(b, best[i][k] + best[k][j]);
            int reach = (X[j - 1] - X[i] + 1) / 2;
            best[i][j] = reach <= H ? b - (H - reach + 1) : b;
        }
    }

    cout << best[0][N] << '\n';
}

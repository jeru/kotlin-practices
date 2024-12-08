// Copyright 2024 Cheng Sheng
// SPDX-License-Identifier: Apache-2.0
//
// Problem: H. Missing Separators
// https://codeforces.com/problemset/problem/2045/H
//
// SEMI-LITERALLY translated from kotlin for performance comparison.

#include <cstdio>
#include <cassert>

#include <algorithm>
#include <iostream>
#include <string>
#include <string_view>
#include <vector>
using namespace std;

class Solver {
public:
    Solver(string _s) : s0(_s), s(s0) {
        for (int i = 0; i < s.size(); ++i) suffices.push_back(i);
        sort(suffices.begin(), suffices.end(), [this](int a, int b) {
                return s.substr(a) < s.substr(b);
        });
    }

    string s0;
    string_view s;
	vector<int> suffices;

    vector<vector<int>> matchingLengthMatrix;

	void computeMatchingLengths() {
        matchingLengthMatrix.assign(s.size(), vector<int>(s.size(), -1));
		computeMatchingLengthsRec(0, s.size(), 0);
	}

	// all suffices in [b, e) match by m0 characters.
	void computeMatchingLengthsRec(int b, int e, int m0) {
		int m = matchMore(suffices[b], suffices[e - 1], m0);
		if (b + 1 == e) {
			int u = suffices[b];
			matchingLengthMatrix[u][u] = m;
			return;
		}
		int i = b;
		while (i < e) {
			int nextChar = getS(suffices[i] + m);
			int j = i;
			while (j < e && getS(suffices[j] + m) == nextChar) j += 1;
            for (int k = b; k < e; ++k) {
				if (i <= k && k < j) continue;
                for (int l = i; l < j; ++l) {
					int u = suffices[k];
					int v = suffices[l];
					matchingLengthMatrix[u][v] = m;
					matchingLengthMatrix[v][u] = m;
				}
			}
			computeMatchingLengthsRec(i, j, m);
			i = j;
		}
	}

	int matchMore(int a, int b, int m0) {
		int m = m0;
		while (a + m < n() && b + m < n() && s[a + m] == s[b + m]) m += 1;
		return m;
	}

	void printDebugMatrix() {
        for (int i = 0; i < n(); ++i) {
			for (int j = 0; j < n(); ++j) {
				printf("matchingLength(%s, %s) = %d\n",
                        string(s.substr(i)).c_str(),
                        string(s.substr(j)).c_str(),
                        matchingLengthMatrix[i][j]);
			}
		}
	}

	int n() { return s.size(); }

	char getS(int i) { return (i < n()) ? s[i] : '$'; }

	// best[i][j]: longest dict constructed from S[i..] if the first cut happens at j or later.
    vector<vector<pair<int, pair<int, int>> >> best;

	void computeBest() {
        best.assign(s.size(), vector(s.size() + 1, make_pair(-1, make_pair(-1, -1))));
        for (int i = n() - 1; i >= 0; --i) computeBestOne(i);
	}

	void computeBestOne(int i) {
		best[i][n()] = {1, {n(), 0}};
		for (int j = (n() - 1); j >= i; --j) {
			best[i][j] = best[i][j + 1];
			if (j == i) continue;
			int l = matchingLengthMatrix[i][j];
			int minNextL;
            if (l >= j - i) {  // Fully consume first string.
				if (j - i + j >= n()) continue;
				minNextL = j - i + j + 1;
			} else {
				assert(j + l <= n());
				if (j + l == n() || s[j + l] < s[i + l]) continue;
				minNextL = j + l + 1;
			}
			int v = best[j][minNextL].first;
			if (v >= 0 && v + 1 > best[i][j].first) {
				best[i][j] = {v + 1, {j, minNextL}};
			}
		}
	}

	void printAnswer(pair<int, int> i) {
		if (i.first == n()) return;
		auto j = best[i.first][i.second].second;
		puts(string(s.substr(i.first, j.first - i.first)).c_str());
		printAnswer(j);
	}
};

int main() {
    string s;
    cin >> s;
	Solver solver(s);
	solver.computeMatchingLengths();
	//solver.printDebugMatrix()
	solver.computeBest();

	int ans = solver.best[0][0].first;
	assert(ans > 0);
    printf("%d\n", ans);
	solver.printAnswer({0, 0});
}

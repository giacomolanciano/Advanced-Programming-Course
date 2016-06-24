/*
 * when compiling this file, link also 'point.cpp' that implements 
 * 'point.hpp' member functions */

#include "point.hpp"
#include <iostream>

using namespace std;

int main() {
	
	cout << "point p: ";
	point p(2,5);
	p.print();
	
	cout << "point q: ";
	point q(2,4);
	q.print();
	
	cout << "point t (result of p.sum(q)): ";
	point t = p.sum(q);
	t.print();
	
	cout << "point t (result of t+q): ";
	point r = t+q;
	r.print();
	
	cout << "point s (using one parameter constructor): ";
	point s(4);
	s.print();
	
	return 0;
}

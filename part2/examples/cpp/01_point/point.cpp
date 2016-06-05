/*
 * when compiling this file, use option '-c' to produce only the object file ('point.o')
 * otherwise a compiling error arises, because main function is not defined */

#include "point.hpp"
#include <iostream>

using namespace std;

point::point(double a, double b) : x(a), y(b) {}

point::point(double a) : x(a), y(0) {}

point::~point() {
	cout << "sayonara\n";
}

void point::print() {
	cout << x << " " << y << "\n";
}

point point::sum(point a) {
	point t(a.x + x, a.y + y);
	return t;
}

point point::operator+(point a) {
	point t(a.x + x, a.y + y);
	return t;
}

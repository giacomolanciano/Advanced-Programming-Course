#include "comparable.hpp"
#include <iostream>

using namespace std;

class point : public comparable/*<point>*/, public printable {
	double x,y;
public:
	point(double a, double b) : x(a), y(b) {}
	int compare_to(/*const*/ comparable/*<point>*/& c) {
		/* 
		 * we need a downcasting, to compare points*/
		 point* o = (point*)&c;
		 if(x < o->x) return -1;
		 if(x > o->x) return 1;
		 if(y < o->y) return -1;
		 if(y > o->y) return 1;
		 return 0;
	}
	void print() {
		cout << x << " " << y << endl;
	}
};

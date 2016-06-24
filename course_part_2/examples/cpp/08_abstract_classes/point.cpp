#include "comparable.hpp"
#include <iostream>

using namespace std;

class point : public comparable/*<point>*/, public printable {
	double x,y;
public:
	point(double a, double b) : x(a), y(b) {}
	int compare_to(/*const*/ comparable/*<point>*/& c) {
		/* 
		 * we need a downcasting to compare points*/
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

int main() {
	point p1(3.14, 5.9), p2(3.14, 2.2);
	/*const*/ comparable/*<point>*/* c1 = &p1;
	/*const*/ comparable/*<point>*/* c2 = &p2;
	/*const*/ printable/*<point>*/* t1 = &p1;
	/*const*/ printable/*<point>*/* t2 = &p2;
	
	cout << "compare_to: " << c1->compare_to(*c2) << endl;
	
	t1->print();
	t2->print();
}

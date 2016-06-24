#include <stddef.h>
template<class T>
struct point {
	T x,y;
	
	/*
	 * NOTE: cannot define constructor, otherwise initialization by {...}
	 * is not possible
	 * */
	//point(T a, T b) : x(a), y(b) {}
};

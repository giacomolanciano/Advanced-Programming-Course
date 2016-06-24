#include <iostream>
#include "point.hpp"

int main() {
	point<double> p = { 5.2, 7.3 };
	std::cout << p.x << " " << p.y << std::endl;
}

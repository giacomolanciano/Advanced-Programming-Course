#include <iostream>

struct pair { int a, b; };

std::ostream& operator << (std::ostream& o, const pair& p) {
	return o << "(" << p.a << "," << p.b << ")";
}

int main() {
	pair p = {5,7};
	std::cout << p << std::endl;
	return 0;
}

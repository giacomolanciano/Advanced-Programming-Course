#include <iostream>

struct pair { int a, b; };

std::ostream& operator << (std::ostream& o, const pair& p) {
	/*
	 * 'const' and '&' are not necessary for pair parameter,
	 * used for efficiency purposes
	 * */
	return o << "(" << p.a << "," << p.b << ")";
}

int main() {
	pair p = {5,7};
	std::cout << p << std::endl;
	return 0;
}

#include <iostream>
template<class T>
void myswap(T& a, T& b) {
	T aux = a;
	a = b;
	b = aux;
}
int main() {
	double x = 10, y = 20;
	myswap<double>(x,y);
	std::cout << x << " " << y << std::endl;
	return 0;
}

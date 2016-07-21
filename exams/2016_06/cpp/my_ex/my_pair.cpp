#include <iostream>

/*
 * create a customized int pair such that main executes
 * 
 * NOTE: compile with '-D DEBUG' for debug messages
 * */
class my_int_pair {
	int x,y;
	
public:
	my_int_pair() {
		#ifdef DEBUG
			std::cout << "empty constructor" << std::endl;
		#endif
	}
	my_int_pair(int a) : x(a) {
		y = 0;
		#ifdef DEBUG
			std::cout << "1-par constructor" << std::endl;
		#endif
	}
	my_int_pair(int a, int b) : x(a), y(b) {
		#ifdef DEBUG
			std::cout << "2-par constructor" << std::endl;
		#endif
	}
	int getX() {return x;}
	int getY() {return y;}
	my_int_pair operator + (my_int_pair& t) {
		my_int_pair z(x + t.getX(), y + t.getY());
		return z;
	}	
};
 
int main() {
	/*
	 * empty constructor is called when variables are declared
	 * */
	my_int_pair z,t;
	
	/*
	 * 2-par constructor is called when variable is assigned in this way
	 * (compiler gives warning, no need to overload operator '=')
	 * */
	z = {10,20};
	
	/*
	 * 1-par constructor is called when variable is assigned (no need 
	 * to overload operator '=')
	 * */
	t = 10;
	
	/*
	 * operator '+' overloading is needed
	 * NOTE: 2-par constructor is called in operator overloading 
	 * */
	z = z + t;
	
	/*
	 * getter member functions are needed
	 * */
	std::cout << "(" << z.getX() << "," << z.getY() << ")" << std::endl;
	
	return 0;
}

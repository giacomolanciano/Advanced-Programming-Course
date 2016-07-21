#include <iostream>

/*
 * create a customized int such that main executes
 * 
 * NOTE: compile with '-D DEBUG' for debug messages
 * */
class my_int {
	int x;
public:
	my_int() {
		#ifdef DEBUG
			std::cout << "empty constructor" << std::endl;
		#endif
	}
	my_int(int a) : x(a) {
		#ifdef DEBUG
			std::cout << "1-par constructor" << std::endl;
		#endif
	}
	int getValue() {return x;}
	my_int operator + (my_int& y) {
		my_int z = x + y.getValue();
		return z;
	}
};
 
int main() {
	/*
	 * empty constructor is called when variables are declared
	 * */
	my_int x,y;
	
	/*
	 * 1-par constructor is called when variables are assigned (no need 
	 * to overload operator '=')
	 * */
	x = 10;
	y = 20;
	
	/*
	 * operator '+' overloading is needed
	 * NOTE: 1-par constructor is called in operator overloading
	 * */
	x = x + y;
	
	/*
	 * getValue() member function is needed
	 * */
	std::cout << "values sum up to " << x.getValue() << std::endl;
	
	return 0;
}

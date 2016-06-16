#include "stack.hpp"
#include <iostream>

using namespace std;

int main() {
	stack<double> s;	//specify type
	s.push(3.4);		//cannot pass a constant if T&
	s.push(9.7);
	s.push(1.4);
	while(!s.isempty()) {
		double val = s.pop();
		cout << val << endl;
	}
	
	return 0;
}

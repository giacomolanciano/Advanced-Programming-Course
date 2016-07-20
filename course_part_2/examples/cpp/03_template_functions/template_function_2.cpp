#include <iostream>

/*
 * in java cannot have int in generic types, in c++ we can do it as a 
 * preprocessing to embedd an instance of a value
 * */
template <class T, int N>
T sum(T x){
	return x + N;
}

int main() {
	int x = 30;
	/*
	 * 10 is hardcoded in the code, i.e. a specific version of this function 
	 * with 10 as parameters -> faster!!!
	 * */
	std::cout << sum<int,10>(x) << "\n";
	return 0;
}

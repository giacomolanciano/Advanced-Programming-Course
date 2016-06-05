#include <iostream>

template <class T, int N>   //in java cannot have int in generic types, in c++ we can do it as a preprocessing to embedd an instance of a value
T sum(T x){
	return x + N;
}

int main() {
	int x = 30;
	std::cout << sum<int,10>(x) << "\n";   //10 is hardcoded in the code, specific version of this function with 10 as parameters -> faster!!!
	return 0;
}

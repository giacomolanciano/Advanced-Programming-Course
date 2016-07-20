int sumint(int x, int y) {
	return x+y;
}

/*
 * with this implementation we can use the "same" function with many
 * different data types (~ java generics)
 * */
template <class T>
T sum(T x, T y) {
	return x+y;
}

int f1() {
	return sum<int>(10, 20);	//specialize template function for int
}


int f2() {
	/*
	 * NOTE: looking at the assembly implementation of the two specializations
	 * we can see that they are completely different
	 * */
	return sum<int>(10, 20) + sum<double>(2.5, 7.3);
}

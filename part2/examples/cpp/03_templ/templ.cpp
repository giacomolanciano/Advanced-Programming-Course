template <class T>
T sum(T x, T y) {
    return x+y;
}

int sumint(int x, int y) {
    return x+y;
}

/*
int f() {
    return sum<int>(10, 20);    //specialize template function
}
*/

int f() {
    return sum<int>(10, 20) + sum<double>(2.5, 7.3);    //looking at assembly the impl of the two is completely different
}

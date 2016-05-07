#include <iostream>
#include "rat.hpp"

static int gcd(int x, int y) {
    if (y == 0) return x;
    return gcd(y, x % y);
}

rat::rat(int a, int b) {
    int sgn = (a < 0) ^ (b < 0);
    if (a < 0) a = -a;
    if (b < 0) b = -b;
    int g = gcd(a,b);
    n = sgn ? -a/g : a/g;
    d = b/g;
}

rat rat::operator+(const rat& o) {
    return rat(o.d*n+d*o.n, d*o.d);
}

rat rat::operator-(const rat& o) {
    return rat(o.d*n-d*o.n, d*o.d);
}

rat rat::operator*(const rat& o) {
    return rat(n*o.n, d*o.d);
}

rat rat::operator/(const rat& o) {
    return rat(n*o.d, d*o.n);
}

void rat::unpack(int& a, int& b) {
    a = n; 
    b = d;
}
    
void rat::print() {
    std::cout << n << "/" << d << std::endl;
}

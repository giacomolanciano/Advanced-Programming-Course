#include "rat.hpp"
#include <iostream>

using namespace std;

int main() {
    rat r1(2,7);
    rat r2(8,6);
    rat r3(4,14);
    int n, d;
    r3.unpack(n,d);
    cout << "r3=" << n << "/" << d << endl; // 2/7
    (r1+r2).print(); // prints 34/21
    (r1-r2).print(); // prints -22/21
    (r1*r2).print(); // prints 8/21
    (r1/r2).print(); // prints 3/14
    return 0;
}

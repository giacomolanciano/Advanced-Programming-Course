#include "rat.hpp"
#include <iostream>
#include <stdlib.h>

using namespace std;

static int euclide(int x, int y) {
    
    /*
     * x & y must be non-negative */
    
    if (y == 0) {
        return x;
    }
    int mod = x%y;
    return euclide(y, mod);
    
}

rat::rat(int a, int b){
    
    /*
     * used abs() to avoid that d is negative (if the fraction has to be negative) */
    
    int gcd = euclide(abs(a), abs(b));
    
    if(b < 0) {
        d = -(b/gcd);
        n = -(a/gcd);
    } else {
        n = a/gcd;
        d = b/gcd;
    }
    
    
}

void rat::unpack(int &a, int &b) {
    a = n;
    b = d;
}

rat rat::operator+(rat r) {
    int den = d * r.d;
    int num = n*(den/d) + r.n*(den/r.d);
    
    rat nr(num, den);
    return nr;
    
}

rat rat::operator-(rat r) {
    int den = d * r.d;
    int num = n*(den/d) - r.n*(den/r.d);
    
    rat nr(num, den);
    return nr;
    
}

rat rat::operator*(rat r) {
    int den = d * r.d;
    int num = n*r.n;
    
    rat nr(num, den);
    return nr;
    
}

rat rat::operator/(rat r) {
    int den = d * r.n;
    int num = n*r.d;
    
    rat nr(num, den);
    return nr;
    
}


void rat::print() {        
    cout << n << "/" << d << endl;
}



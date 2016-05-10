#include <iostream>

using namespace std;

class person {
    
public:
    char* name;
    person(char* n) {
        name=n;
    }
    friend ostream& operator << (ostream& o, const person& p);
};

//binary operator overloading (in outer scope)
ostream& operator << (ostream& o, const person& p) {
        
        //this is not the same function called in the main funct, it depends from second argument
        cout << "that's a test" << endl;    
        
        return o << p.name;
}

int main() {
    
    person p("anna");
    cout << p << endl;
    
    return 0;
}

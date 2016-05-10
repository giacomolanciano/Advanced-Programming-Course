#include <iostream>
#include <string.h>

using namespace std;

class mystr {
    char* s;
public:
    class iterator {
        char* s;    //copy of string
        int i;      //current index
        iterator(char* ss, int ii) : s(ss), i(ii) {} //private constructor
        friend mystr;   //to allow mystr to access private fields
    public:
        bool operator != (const iterator& it) { //& for efficiency
            //assuming not mixing iterators for different strings (otherwise control also that strings are different)
            return i != it.i;   
        }
        
        iterator& operator ++ () {  //this works only for pre-increment
            //increase index, side-effect
            i++;    
            return *this;
        }
        
        iterator operator ++ (int) {  //this works only for post-increment
            //result has to be a copy of the obj before increment
            
            iterator it = *this;
            i++;    //increase index, side-effect
            return it;  
        }
        
        char operator * () {
            return s[i];  
        }
    };
    
    mystr(char* ss) : s(ss) {}
    
    iterator begin() {
        return iterator(s, 0);
    }
    
    iterator end() {
        return iterator(s, strlen(s));
    }
    
};

int main() {
    
    mystr s("hello");
    
    /*
     * our goal is to define a custom iterator for mystr, as a subclass*/
    for(mystr::iterator it = s.begin(); it != s.end(); ++it)
            cout << *it << endl;
    
    return 0;
}

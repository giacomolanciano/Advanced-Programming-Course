#include <string>
#include <iostream>

using namespace std;

int main() {
    
    string s = "hello";
    s = s + " world "; //not possible in C
    cout << s << s.length() << " " << s.size() << endl;     //length and size do the same work
    
    //standar way to iterate over a string, iterator is a subclass of string (defined within string)
    for(string::iterator it = s.begin(); it != s.end(); it++)    
        cout << *it << endl;    //*it is a single char of the string
        
    cout << s[3] << endl;   //l
    cout << s.capacity() << endl;
    
    /* 
     * string classe has a friend function that overrides << operator, as we did*/
     
    const char* cs = s.c_str();   //to have a pointer to string, as we do in C (read-only reference)

    
    return 0;
}

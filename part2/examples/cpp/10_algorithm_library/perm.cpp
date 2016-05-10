#include <algorithm>
#include <string>
#include <iostream>

using namespace std;

int main() {
    /*
     * if you want all the anagrams we first need to sort the string and then
     * generate permutations (next_permutation works with lexicographical order)*/
     
    //string s = "anna";
    string s = "4123";
    sort(s.begin(), s.end());
    
    do {
        cout << s << endl;
    } while (next_permutation(s.begin(), s.end()));
    
    /*
     * generate all subsets of certain cardinality of a set*/
    s = "11000000";
    sort(s.begin(), s.end());
    do {
        cout << s << endl;
    } while (next_permutation(s.begin(), s.end()));
    
    /* 
     * ex: create class that generates anagrams of string*/
     
         
    return 0;
}

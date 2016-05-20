#include <iostream>
#include "anagrams.hpp"

int main() {
    anagrams a("advancedprog");
    
    /*
     * to better understand how anagrams.hpp can be optimized, just count
     * anagrams instead of printing them (much faster)
     * */
    int c;
    
    for (anagrams::iterator it = a.begin(); it != a.end(); ++it) {
        //std::cout << *it << std::endl;
        c++;
    }
    
    std::cout << c << std::endl;
    
    return 0;
}

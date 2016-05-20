/*
 * this version compute all anagrams at the beginning and store them 
 * in a vector. anagrams::iterator simply allows to scan the vector.
 * program can be optimized avoiding to store all anagrams, just computing
 * them when is required
 * */

#include <algorithm>
#include <string>
#include <vector>

using namespace std;

class anagrams {
    string s;
    vector<string> perm;
public:
    class iterator {
        vector<string> perm;
        int i;      //current index
        iterator(vector<string>&  p, int ii) : perm(p), i(ii) {} //private constructor
        friend anagrams;   //to allow anagrams to access private fields
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
        
        string operator * () {
            return perm[i];  
        }
    };
    
    anagrams(string ss) : s(ss) {
        
        perm.push_back(s);
        
        sort(ss.begin(), ss.end());
                
        do {
            
            if(ss != s)
                perm.push_back(ss);
                
        } while (next_permutation(ss.begin(), ss.end()));
        
    }
    
    iterator begin() {
        return iterator(perm, 0);
    }
    
    iterator end() {
        return iterator(perm, perm.size());
    }
    
};

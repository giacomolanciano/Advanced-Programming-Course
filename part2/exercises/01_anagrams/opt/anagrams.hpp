#include <algorithm>
#include <string>
#include <vector>

using namespace std;

class anagrams {
    string s;
public:
    class iterator {
        
        string s;
        bool has_next;
        
        iterator() {
            has_next = false;
        }
        
        iterator(const string ss) : s(ss) {
            has_next = true;
            sort(s.begin(), s.end());
        }
        
        friend anagrams;   //to allow anagrams to access private fields
        
    public:
        bool operator != (const iterator& it) { //& for efficiency
            /*
             * assuming that operator is only used to check wheter iterator
             * has been entirely consumed
             * */
             
            return has_next != it.has_next;   
        }
        
        bool operator == (const iterator& it) { //& for efficiency
            /*
             * assuming that operator is only used to check wheter iterator
             * has been entirely consumed
             * */
             
            return has_next == it.has_next;   
        }
        
        void next_anagram() {
            has_next = next_permutation(s.begin(), s.end());
        }
        
        iterator& operator ++ () {
            /*
             * this works only for pre-increment
             * side-effect on s
             * */
             
            next_anagram();
            return *this;
        }
        
        iterator operator ++ (int) {  
            /*
             * this works only for post-increment
             * result has to be a copy of the obj before side-effect on s
             * */
            
            iterator it = *this;
            next_anagram();
            return it;  
        }
        
        string operator * () {
            return s;  
        }
    };
    
    anagrams(string ss) : s(ss) {}
    
    iterator begin() {
        return iterator(s);
    }
    
    iterator end() {
        return iterator();
    }
    
};

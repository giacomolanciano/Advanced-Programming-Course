#include <algorithm>
#include <string>

/*
 * when developing headers, it is suggested not to use namespaces, better to 
 * prefix std (for instance) all the times to avoid conflicts (be as general
 * as possible).
 * 
 * NOTE: don't compile headers unless when doing a makefile! otherwise 
 * programs which include the header will always use the precompiled version
 * (for efficiency). then whenever you modify it you would have to recompile it
 * all the times (better to let the compiler do it for us)
 * */

class anagrams {
	std::string s;

public:
	class iterator {
		
		std::string s;
		bool has_next;
		
		iterator(const std::string ss) : s(ss) {
			has_next = s.size() > 0;
		}
		
		void next_anagram() {
			has_next = next_permutation(s.begin(), s.end());
		}
		
		/*
		 * to allow anagrams to access private fields
		 * */
		friend anagrams;   
		
	public:
		bool operator != (const iterator& it) {
			/*
			 * & for efficiency, const is required
			 * 
			 * assuming that operator is only used to check wheter iterator
			 * has been entirely consumed (i.e. it is the result of end())
			 * */
			 
			return has_next != it.has_next;   
		}
		
		bool operator == (const iterator& it) {
			/*
			 * & for efficiency, const is required
			 * 
			 * assuming that operator is only used to check wheter iterator
			 * has been entirely consumed (i.e. it is the result of end())
			 * */
			 
			return has_next == it.has_next;   
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
		
		std::string& operator * () {
			return s;  
		}
	};

	anagrams(std::string ss) : s(ss) {}
	
	iterator begin() {
		iterator it(s);
		
		if(it.has_next) 
			sort(it.s.begin(), it.s.end());
		
		return it;
	}
	
	iterator end() {		
		return iterator("");
	}
};

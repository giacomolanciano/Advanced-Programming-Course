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
		
		/*
		 * defined in order to use empty iterator (see below)
		 * */
		iterator() {}
		
		iterator(const std::string ss) : s(ss) {
			has_next = s.size() > 0;
			sort(s.begin(), s.end());
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
			
			/*
			 * TODO: ask why return statement is not necessary
			 * */
			//return *this;
		}
		
		iterator operator ++ (int) {  
			/*
			 * this works only for post-increment
			 * result has to be a copy of the obj before side-effect on s
			 * */
			
			iterator it = *this;
			next_anagram();
			
			/*
			 * TODO: ask why return statement is not necessary
			 * */
			//return it;  
		}
		
		std::string& operator * () {
			return s;  
		}
	};

private:
	/*
	 * to make execution faster, create an empty iterator (with
	 * has_next set to false) that will be returned all the times 
	 * that end() is called
	 * 
	 * NOTE: it has to be declared after the definition of iterator class
	 * and before its usage in anagrams functions. therefore it is put in 
	 * a private section of anagrams class (between two public sections) 
	 * since also info hiding is important.
	 * */
	iterator end_iter;
	
public:
	anagrams(std::string ss) : s(ss) {
		end_iter.has_next = false;
	}
	
	iterator begin() {
		return iterator(s);
	}
	
	iterator& end() {		
		/*
		 * since end_iter is not created here, return a reference to it
		 * (defining return type as iterator&)
		 * this trick further improves the efficiency
		 * */
		return end_iter;
	}
	
};

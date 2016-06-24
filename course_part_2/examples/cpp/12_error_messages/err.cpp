#include <vector>

template <typename T>   //typename can be used as class
class C {
	T x;
	std::vector<T> v;
};

int main() {
	std::vector<int> v;
	std::vector<std::pair<int, float> > p;
	std::vector<int> q;
	
	typedef int int2;
	C<int> y;
	
	return 0;
}


/*
 * not putting std::vector we get:
 * 
 * err.cpp: In function ‘int main()’:
	err.cpp:4:5: error: ‘vector’ was not declared in this scope
		 vector<int> v;
		 ^
	err.cpp:4:5: note: suggested alternative:
	In file included from /usr/include/c++/4.8/vector:64:0,
					 from err.cpp:1:
	/usr/include/c++/4.8/bits/stl_vector.h:210:11: note:   ‘std::vector’
		 class vector : protected _Vector_base<_Tp, _Alloc>
			   ^
	err.cpp:4:12: error: expected primary-expression before ‘int’
		 vector<int> v;
				^
	err.cpp:4:12: error: expected ‘;’ before ‘int’
* 
* 
* 
* */


/*
 * std::vector<std::pair<int, float> > p;
 * 
 * we have to put a space between '>' and '>', otherwise compiler sees the shift operator
 * 
 * err.cpp: In function ‘int main()’:
	err.cpp:5:37: error: ‘>>’ should be ‘> >’ within a nested template argument list
	 std::vector<std::pair<int, float>> p;
									 ^
*/




/*
 * 
 * std::vector<int, float> q;   (add an extra parameter to vector)
 * 
 * A LOT OF ERROR MESSAGES starting with:
 * 
 * In file included from /usr/include/c++/4.8/bits/stl_construct.h:61:0,
				 from /usr/include/c++/4.8/vector:62,
				 from err.cpp:1:
 *
 * 
 * this is an error of PREPROCESSOR
 * 
 * */
 
 
 
 /*
  *	 C<int,float> y;
  * 
  * 
  * err.cpp: In function ‘int main()’:
	err.cpp:14:16: error: wrong number of template arguments (2, should be 1)
		 C<int,float> y;
					^
	err.cpp:4:7: error: provided for ‘template<class T> class C’
	 class C {
		   ^
	err.cpp:14:19: error: invalid type in declaration before ‘;’ token
		 C<int,float> y;
  *
  * 
  * 
*/





/*
 * int int2 = 10;
	C<int2> y;
 * 
 * 
 * err.cpp: In function ‘int main()’:
err.cpp:15:7: error: ‘int2’ cannot appear in a constant-expression
	 C<int2> y;
	   ^
err.cpp:15:11: error: template argument 1 is invalid
	 C<int2> y;
		   ^
err.cpp:15:14: error: invalid type in declaration before ‘;’ token
	 C<int2> y;

 * 
 * */

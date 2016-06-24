#include <stddef.h>

template <class T>
class node {
	public:
		T elem;
		node<T>* next;
};

template <class T>
class stack {
	node<T>* head;
	public:
		stack() : head(NULL) {};
		bool isempty();
		void push(T);
		T pop();
		T top();
};

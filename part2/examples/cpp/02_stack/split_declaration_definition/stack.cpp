#include "stack.hpp"

/*
template <class T>
class node {
	public:
		T elem;
		node<T>* next;
};
*/

template <class T>
bool stack<T>::isempty() {
	return head == NULL;
}

template <class T>
void stack<T>::push(T elem) {
	node<T>* n = new node <T>;
	n->elem = elem; 
	n->next = head;
	head = n;
}

template <class T>
T stack<T>::pop() {
	T elem = head->elem;
	node<T>* dead = head;
	head = head->next;
	delete dead;
	return elem;
}

template <class T>
T stack<T>::top() {
	return head->elem;
}

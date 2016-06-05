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
void stack<T>::push(T elem) {  //we cannot pass a constant if T&
	node<T>* n = new node <T>;
	n->elem = elem; 
	n->next = head;
	head = n;
}


template <class T>
T stack<T>::pop() {
	T elem = head->elem;	//best practice, make a copy of smth to return if the obj is going to be deleted
	node<T>* dead = head;   //to be able to call delete
	head = head->next;
	delete dead;
	return elem;
}

template <class T>
T stack<T>::top() {
	return head->elem;
}
